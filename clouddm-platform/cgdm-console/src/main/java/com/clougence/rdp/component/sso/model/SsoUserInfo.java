//package com.clougence.rdp.component.sso.model;
//
//import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
//import com.clougence.rdp.component.sso.model.dto.GoogleUserInfoDTO;
//import com.clougence.rdp.dal.enumeration.SsoType;
//import com.lark.oapi.service.authen.v1.model.GetUserInfoRespBody;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class SsoUserInfo {
//
//    private String  email;
//
//    private String  mobile;
//
//    private String  nick;
//
//    private String  openId;
//
//    private String  stateCode;
//
//    private String  unionId;
//
//    private SsoType ssoType;
//
//    private String  company;
//
//    private String  src;
//
//    public static SsoUserInfo convertDingTalk(GetUserResponseBody responseBody) {
//        SsoUserInfo userInfo = new SsoUserInfo();
//        userInfo.setEmail(responseBody.getEmail());
//        userInfo.setMobile(responseBody.getMobile());
//        userInfo.setNick(responseBody.getNick());
//        userInfo.setUnionId(responseBody.getUnionId());
//        userInfo.setStateCode(responseBody.getStateCode());
//        userInfo.setSsoType(SsoType.DingTalk);
//        return userInfo;
//    }
//
//    public static SsoUserInfo convertGoogle(GoogleUserInfoDTO data) {
//        SsoUserInfo userInfo = new SsoUserInfo();
//        if (data == null) {
//            return userInfo;
//        }
//        userInfo.setUnionId(data.getId());
//        userInfo.setEmail(data.getEmail());
//        userInfo.setNick(data.getName());
//        userInfo.setSsoType(SsoType.Google);
//        return userInfo;
//    }
//
//    public static SsoUserInfo convertFeiShu(GetUserInfoRespBody resp) {
//        SsoUserInfo userInfo = new SsoUserInfo();
//        userInfo.setNick(resp.getName());
//        userInfo.setEmail(resp.getEmail());
//        userInfo.setMobile(resp.getMobile());
//        userInfo.setUnionId(resp.getUnionId());
//        userInfo.setSsoType(SsoType.FeiShu);
//        return userInfo;
//    }
//}
