//package com.clougence.rdp.component.sso.Impl;
//
//import java.net.URLEncoder;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.stereotype.Service;
//
//import com.clougence.rdp.component.sso.model.SsoUserInfo;
//import com.clougence.rdp.component.sso.model.fo.SsoRegisterAndLoginFO;
//import com.clougence.utils.StringUtils;
//import com.lark.oapi.Client;
//import com.lark.oapi.core.request.RequestOptions;
//import com.lark.oapi.service.authen.v1.model.*;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@ConditionalOnClass(name = { "com.lark.oapi.Client" })
//@Service("RdpFeishuSsoLoginRegServiceImpl")
//public class RdpFeishuSsoLoginRegServiceImpl extends AbstractSsoLoginRegService {
//
//    private final static String FEISHU     = "feishu";
//    private static final String GRANT_TYPE = "authorization_code";
//
//    private Client              client;
//
//    @Value("${console.config.sso.feishu.client.id:#{NULL}}")
//    private String              clientId;
//    @Value("${console.config.sso.feishu.client.secret:#{NULL}}")
//    private String              clientSecret;
//    @Value("${console.config.sso.feishu.callback.url:#{NULL}}")
//    private String              callbackUrl;
//
//    @Override
//    public SsoUserInfo fetchUserInfo(SsoRegisterAndLoginFO ssoFO) {
//        String accessToken = fetchAccessToken(ssoFO.getCode());
//        GetUserInfoRespBody resp = fetchUserInfoByToken(accessToken);
//        return SsoUserInfo.convertFeiShu(resp);
//    }
//
//    private Client fetchClient() {
//        if (client == null) {
//            client = Client.newBuilder(clientId, clientSecret).build();
//        }
//        return client;
//    }
//
//    private GetUserInfoRespBody fetchUserInfoByToken(String accessToken) {
//        try {
//            Client client = fetchClient();
//            GetUserInfoResp resp = client.authen().userInfo().get(RequestOptions.newBuilder().userAccessToken(accessToken).build());
//            return resp.getData();
//        } catch (Exception e) {
//            String msg = "fetch user info from sso login info error ,type is " + FEISHU;
//            log.error(msg, e);
//        }
//        return null;
//    }
//
//    private String fetchAccessToken(String code) {
//        try {
//            Client client = fetchClient();
//            CreateOidcAccessTokenReq req = CreateOidcAccessTokenReq.newBuilder()
//                .createOidcAccessTokenReqBody(CreateOidcAccessTokenReqBody.newBuilder().grantType(GRANT_TYPE).code(code).build())
//                .build();
//            CreateOidcAccessTokenResp resp = client.authen().oidcAccessToken().create(req);
//            return resp.getData().getAccessToken();
//        } catch (Exception e) {
//            String msg = "fetch user info from sso login info error ,type is " + FEISHU;
//            log.error(msg, e);
//        }
//        return null;
//    }
//
//    @Override
//    public String fetchCallback(String src, String target) {
//        if (StringUtils.isBlank(callbackUrl) || StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret)) {
//            return null;
//        }
//
//        return "https://passport.feishu.cn/suite/passport/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + URLEncoder.encode(callbackUrl) + "&response_type=code&state="
//               + this.encodeState(src, target);
//    }
//
//    @Override
//    public String fetchSsoType() {
//        return FEISHU;
//    }
//}
