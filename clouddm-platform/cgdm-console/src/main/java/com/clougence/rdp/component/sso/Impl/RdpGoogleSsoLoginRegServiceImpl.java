//package com.clougence.rdp.component.sso.Impl;
//
//import java.util.HashMap;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.clougence.rdp.component.sso.model.SsoUserInfo;
//import com.clougence.rdp.component.sso.model.dto.GoogleUserInfoDTO;
//import com.clougence.rdp.component.sso.model.fo.SsoRegisterAndLoginFO;
//import com.clougence.rdp.util.RdpHttpClient;
//import com.clougence.utils.ExceptionUtils;
//import com.clougence.utils.StringUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service(value = "RdpGoogleSsoLoginRegServiceImpl")
//public class RdpGoogleSsoLoginRegServiceImpl extends AbstractSsoLoginRegService {
//
//    private final static String GOOGLE = "google";
//
//    @Value("${console.config.sso.google.client.id:#{NULL}}")
//    private String              clientId;
//
//    @Value("${console.config.sso.google.callback.url:#{NULL}}")
//    private String              callbackUrl;
//
//    @Override
//    public SsoUserInfo fetchUserInfo(SsoRegisterAndLoginFO ssoFO) {
//        if (StringUtils.isBlank(ssoFO.getAccessToken())) {
//            return new SsoUserInfo();
//        }
//
//        GoogleUserInfoDTO userDTO = null;
//        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + ssoFO.getAccessToken();
//
//        try {
//            String resp = RdpHttpClient.getWithString(url, new HashMap<>());
//            userDTO = new ObjectMapper().readValue(resp, GoogleUserInfoDTO.class);
//        } catch (Exception e) {
//            String msg = "[Rdp Google Sso Service] Fetch user info from google sso login error.msg:" + ExceptionUtils.getRootCauseMessage(e);
//            log.error(msg, e);
//        }
//
//        return SsoUserInfo.convertGoogle(userDTO);
//    }
//
//    @Override
//    public String fetchCallback(String src, String target) {
//        if (StringUtils.isBlank(callbackUrl) || StringUtils.isBlank(clientId)) {
//            return null;
//        }
//
//        return "https://accounts.google.com/o/oauth2/v2/auth" //
//               + "?scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid"
//               + "&include_granted_scopes=true&response_type=token&state=" + this.encodeState(src, target) + "&redirect_uri=" + callbackUrl + "&client_id=" + clientId;
//    }
//
//    @Override
//    public String fetchSsoType() {
//        return GOOGLE;
//    }
//}
