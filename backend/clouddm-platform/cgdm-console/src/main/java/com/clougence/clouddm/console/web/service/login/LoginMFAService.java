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
package com.clougence.clouddm.console.web.service.login;

import com.clougence.clouddm.console.web.constants.MfaAccountType;
import com.clougence.clouddm.console.web.model.vo.MfaCodeVO;

public interface LoginMFAService {

    String MFA_PRE_ACTION_TYPE = "MFA_PRE_ACTION_TYPE";
    String MFA_LOGIN_JWT_TOKEN = "MFA_LOGIN_JWT_TOKEN";

    MfaCodeVO initMFA(String uid, MfaAccountType mfaAccountType);

    byte[] resetMFA(String uid, int mfaCode, MfaAccountType mfaAccountType);

    void confirmFMA(String uid, boolean reset, int mfaCode);

    boolean validMFA(String uid, int mfaCode);

    void closeMFA(String uid, int mfaCode);
}
