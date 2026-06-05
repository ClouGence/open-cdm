/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.global.jwtsession;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.console.web.constants.MfaPreActionType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {

    String jwtTokenName      = "jwt_token";
    String opPwdToken        = "op_pwd_token";
    int    minLoginExpireSec = 1200;

    DecodedJWT verify(HttpServletRequest request);

    void refreshCookiePeriodOfValidity(HttpServletRequest request, HttpServletResponse response);

    void refreshJwtTokenPeriodOfValidity(HttpServletRequest request, HttpServletResponse response, DmAuthUserDO user);

    DecodedJWT verifyOpToken(HttpServletRequest request);

    DecodedJWT verifyJwtToken(String jwtToken);

    DecodedJWT verifyMfaActionToken(String mfaActionToken);

    String genJwtToken(DmAuthUserDO user);

    String genOpPwdToken(DmAuthUserDO user);

    String genMfaActionToken(String uid, MfaPreActionType actionType, String jwtToken);
}
