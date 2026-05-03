//package com.clougence.rdp.component.sso.Impl;
//
//import java.net.URLEncoder;
//import java.util.HashMap;
//
//import javax.annotation.Resource;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.clougence.rdp.component.sso.model.SsoUserInfo;
//import com.clougence.rdp.component.sso.model.dto.WechatTokenDTO;
//import com.clougence.rdp.component.sso.model.dto.WechatUserDTO;
//import com.clougence.rdp.component.sso.model.fo.SsoRegisterAndLoginFO;
//import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
//import com.clougence.rdp.controller.model.enumeration.VerifyType;
//import com.clougence.rdp.dal.enumeration.AreaCode;
//import com.clougence.rdp.dal.enumeration.RegisterStatus;
//import com.clougence.rdp.dal.enumeration.SsoType;
//import com.clougence.rdp.dal.mapper.RdpSsoRegisterMapper;
//import com.clougence.rdp.dal.mapper.RdpUserMapper;
//import com.clougence.rdp.dal.model.RdpSsoRegisterDO;
//import com.clougence.rdp.dal.model.RdpUserDO;
//import com.clougence.rdp.service.RdpVerifyService;
//import com.clougence.rdp.service.model.CheckVerifyMO;
//import com.clougence.rdp.util.RdpHttpClient;
//import com.clougence.utils.StringUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service(value = "RdpWechatSsoLoginRegServiceImpl")
//public class RdpWechatSsoLoginRegServiceImpl extends AbstractSsoLoginRegService {
//
//    private final static String  WECHAT = "wechat";
//
//    private final ObjectMapper   mapper;
//
//    @Resource
//    private RdpSsoRegisterMapper rdpSsoRegisterMapper;
//    @Resource
//    private RdpVerifyService     rdpVerifyService;
//    @Resource
//    private RdpUserMapper        rdpUserMapper;
//
//    @Value("${console.config.sso.wechat.client.id:#{NULL}}")
//    private String               clientId;
//    @Value("${console.config.sso.wechat.client.secret:#{NULL}}")
//    private String               clientSecret;
//    @Value("${console.config.sso.wechat.callback.url:#{NULL}}")
//    private String               callbackUrl;
//
//    public RdpWechatSsoLoginRegServiceImpl(){
//        this.mapper = new ObjectMapper();
//    }
//
//    @Override
//    public SsoUserInfo fetchUserInfo(SsoRegisterAndLoginFO ssoFO) {
//        SsoUserInfo userInfo = new SsoUserInfo();
//
//        if (StringUtils.isNotBlank(ssoFO.getCode())) {
//            try {
//                String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + clientId + "&secret=" + clientSecret + "&code=" + ssoFO.getCode()
//                                  + "&grant_type=authorization_code";
//                String tokenResp = RdpHttpClient.getWithString(tokenUrl, new HashMap<>());
//                WechatTokenDTO wechatTokenDTO = this.mapper.readValue(tokenResp, WechatTokenDTO.class);
//                String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + wechatTokenDTO.getAccessToken() + "&openid=" + wechatTokenDTO.getOpenId();
//                String userResp = RdpHttpClient.getWithString(userUrl, new HashMap<>());
//                WechatUserDTO wechatUserDTO = this.mapper.readValue(userResp, WechatUserDTO.class);
//                userInfo.setUnionId(wechatUserDTO.getUnionId());
//                userInfo.setNick(wechatUserDTO.getNickName());
//                userInfo.setSsoType(SsoType.Wechat);
//            } catch (Exception e) {
//                String msg = "Fetch user info from sso login info error,type is " + WECHAT;
//                log.error(msg, e);
//            }
//
//            return userInfo;
//        }
//
//        if (StringUtils.isNotBlank(ssoFO.getRequestId()) && StringUtils.isNotBlank(ssoFO.getPhone()) && StringUtils.isNotBlank(ssoFO.getVerifyCode())) {
//            RdpSsoRegisterDO regDO = rdpSsoRegisterMapper.queryByRequestId(ssoFO.getRequestId());
//            if (regDO == null || RegisterStatus.WAIT_REGISTER != regDO.getRegisterStatus()) {
//                String msg = "fetch register info error, request id " + ssoFO.getRequestId();
//                log.error(msg);
//                throw new IllegalArgumentException(msg);
//            }
//
//            // need check the ssm first
//            checkVerify(ssoFO.getPhone(), ssoFO.getVerifyCode());
//
//            rdpSsoRegisterMapper.updateStatusByRequestId(ssoFO.getRequestId(), RegisterStatus.SUCCESS);
//            RdpUserDO user = rdpUserMapper.queryPrimaryByPhone(ssoFO.getPhone());
//            if (user != null) {
//                rdpUserMapper.updateUnionId(user.getId(), regDO.getUnionId(), SsoType.Wechat);
//            }
//
//            userInfo.setMobile(ssoFO.getPhone());
//            userInfo.setCompany(ssoFO.getCompany());
//            userInfo.setNick(regDO.getNickname());
//            userInfo.setUnionId(regDO.getUnionId());
//            userInfo.setSsoType(SsoType.Wechat);
//            return userInfo;
//        }
//
//        return userInfo;
//    }
//
//    private void checkVerify(String phone, String verifyCode) {
//        CheckVerifyMO verifyMO = new CheckVerifyMO();
//        verifyMO.setPhoneNumber(phone);
//        verifyMO.setPhoneAreaCode(AreaCode.CHINA);
//        verifyMO.setVerifyCode(verifyCode);
//        verifyMO.setVerifyType(VerifyType.SMS_VERIFY_CODE);
//        verifyMO.setVerifyCodeType(VerifyCodeType.SSO_REGISTER_BIND);
//        this.rdpVerifyService.checkVerifyCode(verifyMO);
//    }
//
//    @Override
//    public String fetchCallback(String src, String target) {
//        if (StringUtils.isBlank(callbackUrl) || StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret)) {
//            return null;
//        }
//
//        // encode state
//        return "https://open.weixin.qq.com/connect/qrconnect?appid=" + clientId + "&redirect_uri=" + URLEncoder.encode(callbackUrl)
//               + "&response_type=code&scope=snsapi_login&state=" + this.encodeState(src, target) + "#wechat_redirect";
//    }
//
//    @Override
//    public String fetchSsoType() {
//        return WECHAT;
//    }
//}
