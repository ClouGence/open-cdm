package com.clougence.rdp.service;

import java.util.Date;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.controller.model.fo.LoginFO;
import com.clougence.rdp.controller.model.fo.RegisterFO;
import com.clougence.rdp.controller.model.vo.LoginUserVO;
import com.clougence.rdp.service.model.LoginMO;

/**
 * @author wanshao create time is 2019/12/12 9:36 下午
 **/
public interface RdpUserLoginRegService {

    LoginMO login(LoginFO loginFO);

    void fillSubAccountPwdValidDays(LoginUserVO userVO, Date lastDateUpdatePwd, String pUid);

    ResWebData<Boolean> register(RegisterFO registerFO);

    boolean isLogoutUsingJump(String uid);

    String logoutJumpUrl(String puid, String uid);
}
