package com.clougence.rdp.component.mcp;

import static com.clougence.rdp.component.mcp.McpConstants.*;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.rdp.component.openapi.OpenApiSigner;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;
import com.fasterxml.uuid.Generators;

/**
 * @author bucketli 2021/10/11 19:29
 */
public class McpSessionManager implements HandlerInterceptor {

    private static final int COMMON_PARAMS_HAS_EMTPY = 499;

    private static final int USER_NOT_EXIST          = 498;

    private static final int SIGNATURE_ERROR         = 497;

    private final String     MCP_URI_PREFIX;

    @Resource
    private RdpUserService   rdpUserService;

    public McpSessionManager(String mcpUriPrefix){
        this.MCP_URI_PREFIX = mcpUriPrefix;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (!uri.startsWith(MCP_URI_PREFIX)) {
            return true;
        }

        String ak = request.getParameter("AccessKeyId");
        String signature = request.getParameter("Signature");
        String signatureMethod = request.getParameter("SignatureMethod");
        String signatureNonce = request.getParameter("SignatureNonce");

        if (StringUtils.isBlank(ak) || StringUtils.isBlank(signature) || StringUtils.isBlank(signatureMethod) || StringUtils.isBlank(signatureNonce)) {
            // also support get parameter from header
            ak = request.getHeader("AccessKeyId");
            signature = request.getHeader("Signature");
            signatureMethod = request.getHeader("SignatureMethod");
            signatureNonce = request.getHeader("SignatureNonce");
        }

        if (StringUtils.isBlank(ak) || StringUtils.isBlank(signature) || StringUtils.isBlank(signatureMethod) || StringUtils.isBlank(signatureNonce)) {
            responseSystemError(response, COMMON_PARAMS_HAS_EMTPY);
            return false;
        }

        RdpUserDO userDO = rdpUserService.getUserByAk(ak);
        if (userDO == null) {
            responseSystemError(response, USER_NOT_EXIST);
            return false;
        }

        String sk = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userDO.getSecretKey());

        Map<String, String> paramToSign = new HashMap<>();
        paramToSign.put("SignatureMethod", signatureMethod);
        paramToSign.put("SignatureNonce", signatureNonce);
        paramToSign.put("AccessKeyId", ak);

        String paramStr = OpenApiSigner.composeStringToSign(paramToSign);
        String regenSignature = OpenApiSigner.signString(paramStr, sk);

        if (!signature.equals(regenSignature)) {
            responseSystemError(response, SIGNATURE_ERROR);
            return false;
        }

        request.setAttribute(RdpUserService.UID, userDO.getUid());
        request.setAttribute(RdpUserService.USER_ROLE, userDO.getRoleId());
        request.setAttribute(RdpUserService.IS_MAINTAINER, userDO.isMaintainer());

        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            request.setAttribute(RdpUserService.PUID, userDO.getUid());
        } else {
            RdpUserDO primaryUser = this.rdpUserService.getUserById(userDO.getParentId());
            request.setAttribute(RdpUserService.PUID, primaryUser.getUid());
        }

        request.setAttribute(MCP_API_REQUEST_ID, generateRequestId());
        request.setAttribute(MCP_API_REQUEST_AK, ak);
        request.setAttribute(MCP_API_REQUEST_SK, CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userDO.getSecretKey()));

        String api = request.getServerName() + ":" + request.getServerPort();
        request.setAttribute(MCP_API_REQUEST_API_HOST, api);
        return true;
    }

    protected void responseSystemError(HttpServletResponse response, int code) throws Exception {
        response.setStatus(code);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write("{}");
            writer.flush();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    protected String generateRequestId() {
        return Generators.timeBasedGenerator().generate().toString();
    }
}
