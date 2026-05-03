package com.clougence.rdp.component.jwtsession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.rdp.controller.model.enumeration.MfaPreActionType;
import com.clougence.rdp.dal.model.RdpUserDO;

public interface RdpJwtService {

    String jwtTokenName      = "jwt_token";

    String opPwdToken        = "op_pwd_token";

    int    minLoginExpireSec = 1200;

    DecodedJWT verify(HttpServletRequest request);

    void refreshCookiePeriodOfValidity(HttpServletRequest request, HttpServletResponse response);

    void refreshJwtTokenPeriodOfValidity(HttpServletRequest request, HttpServletResponse response, RdpUserDO user);

    DecodedJWT verifyOpToken(HttpServletRequest request);

    DecodedJWT verifyJwtToken(String jwtToken);

    DecodedJWT verifyMfaActionToken(String mfaActionToken);

    String genJwtToken(RdpUserDO user);

    String genOpPwdToken(RdpUserDO user);

    String genMfaActionToken(String uid, MfaPreActionType actionType, String jwtToken);
}
