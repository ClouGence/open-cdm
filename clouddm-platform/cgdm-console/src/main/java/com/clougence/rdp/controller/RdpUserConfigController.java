package com.clougence.rdp.controller;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_PRI_USER_KV_CONF_W;
import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.controller.model.fo.UpsertUserConfigFO;
import com.clougence.rdp.controller.model.fo.user.GetUserSpecifiedConfsFO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.lo.UpsertUserConfigLO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2020/3/11
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user/config")
@Slf4j
public class RdpUserConfigController {

    @Resource
    private RdpUserConfigService rdpUserConfigService;
    @Resource
    private RdpUserService       rdpUserService;
    @Resource
    private RdpOpAuditService    rdpOpAuditService;

    @RequestAuth(strategy = AuthStrategy.RefAnyOnes)
    @RequestMapping(value = "/getcurruserconfigs", method = RequestMethod.POST)
    public ResWebData<?> getCurrUserConfigs(HttpServletRequest request) {
        // prepare auth info
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        return ResWebDataUtils.buildSuccess(rdpUserConfigService.queryUserConfigVosWithNewEntries(puid));
    }

    @RequestAuth(strategy = AuthStrategy.RefAnyOnes)
    @RequestMapping(value = "/getUserSpecifiedConfs", method = RequestMethod.POST)
    public ResWebData<?> getUserSpecifiedConfs(@RequestBody @Valid GetUserSpecifiedConfsFO configFO, HttpServletRequest request) {
        if (configFO.getConfigNames() == null || configFO.getConfigNames().isEmpty()) {
            return ResWebDataUtils.buildSuccess(new HashMap<>());
        }

        // prepare auth info
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (StringUtils.isBlank(puid)) {
            throw new IllegalArgumentException("Empty puid.maybe not login.");
        }

        return ResWebDataUtils.buildSuccess(rdpUserConfigService.queryWithNewEntriesAndSpecifiedConfs(puid, configFO.getConfigNames()));
    }

    @RequestAuth(level = HIGH, value = RDP_PRI_USER_KV_CONF_W)
    @RequestMapping(value = "/upsertuserconfigs", method = RequestMethod.POST)
    public ResWebData<?> upsertUserConfigs(@RequestBody @Valid UpsertUserConfigFO configFO, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(configFO.getUpdateConfigs()) && CollectionUtils.isEmpty(configFO.getNeedCreateConfigs())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_CONFIG_PARAMS_ARE_EMPTY.name()));
        }

        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<UpsertUserConfigLO> configLOs = rdpUserConfigService.upsertConfigValue(puid, configFO);

        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
            .getRemoteAddr(), uid, configLOs, SecurityLevel.HIGH, AuditType.UPDATE_SYSTEM_CONFIG, ResourceType.ACCOUNT);
        return ResWebDataUtils.buildSuccess();
    }
}
