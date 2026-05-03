//package com.clougence.rdp.component.sso.Impl;
//
//import java.net.URLEncoder;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
//import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
//import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
//import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
//import com.aliyun.teaopenapi.models.Config;
//import com.aliyun.teautil.models.RuntimeOptions;
//import com.clougence.rdp.component.sso.model.SsoUserInfo;
//import com.clougence.rdp.component.sso.model.fo.SsoRegisterAndLoginFO;
//import com.clougence.utils.StringUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service(value = "RdpDingtalkSsoLoginRegServiceImpl")
//public class RdpDingtalkSsoLoginRegServiceImpl extends AbstractSsoLoginRegService {
//
//    private static final String DINGTALK   = "dingtalk";
//    private static final String GRANT_TYPE = "authorization_code";
//    private static final String UNION_ID   = "me";
//    @Value("${console.config.sso.dingtalk.client.id:#{NULL}}")
//    private String              clientId;
//    @Value("${console.config.sso.dingtalk.client.secret:#{NULL}}")
//    private String              clientSecret;
//    @Value("${console.config.sso.dingtalk.callback.url:#{NULL}}")
//    private String              callbackUrl;
//
//    @Override
//    public SsoUserInfo fetchUserInfo(SsoRegisterAndLoginFO fo) {
//        try {
//            com.aliyun.dingtalkoauth2_1_0.Client authClient = authClient();
//            GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest().setClientId(clientId)
//                .setClientSecret(clientSecret)
//                .setCode(fo.getAuthCode())
//                .setGrantType(GRANT_TYPE);
//            GetUserTokenResponse getUserTokenResponse = authClient.getUserToken(getUserTokenRequest);
//            String accessToken = getUserTokenResponse.getBody().getAccessToken();
//            com.aliyun.dingtalkcontact_1_0.Client userClient = contactClient();
//            GetUserHeaders userHeaders = new GetUserHeaders().setXAcsDingtalkAccessToken(accessToken);
//            GetUserResponseBody userInfo = userClient.getUserWithOptions(UNION_ID, userHeaders, new RuntimeOptions()).getBody();
//            return SsoUserInfo.convertDingTalk(userInfo);
//        } catch (Exception e) {
//            String msg = "Dingtalk fetch user info from sso login error, error is " + e.getMessage();
//            log.error(msg, e);
//            throw new RuntimeException(msg);
//        }
//    }
//
//    @Override
//    public String fetchCallback(String src, String target) {
//        if (StringUtils.isBlank(callbackUrl) || StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret)) {
//            return null;
//        }
//
//        return "https://login.dingtalk.com/oauth2/challenge.htm?redirect_uri=" + URLEncoder.encode(callbackUrl) + "&response_type=code&client_id=" + clientId
//               + "&scope=openid&state=" + this.encodeState(src, target) + "&prompt=consent";
//    }
//
//    @Override
//    public String fetchSsoType() {
//        return DINGTALK;
//    }
//
//    public com.aliyun.dingtalkoauth2_1_0.Client authClient() throws Exception {
//        Config config = new Config();
//        config.protocol = "https";
//        config.regionId = "central";
//        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
//    }
//
//    public com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
//        Config config = new Config();
//        config.protocol = "https";
//        config.regionId = "central";
//        return new com.aliyun.dingtalkcontact_1_0.Client(config);
//    }
//}
