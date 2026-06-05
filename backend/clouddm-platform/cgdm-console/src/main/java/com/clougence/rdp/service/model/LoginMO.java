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
package com.clougence.rdp.service.model;

import com.clougence.clouddm.console.web.model.fo.LoginAutoRegisterFO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginMO {

    private boolean             success;

    private boolean             needMore;

    private boolean             needMfa;

    private String              mfaPreActionToken;

    private LoginAutoRegisterFO moreInfo;

    private String              errMsg;

    private String              uid;

    private String              puid;

    private String              username;

    private String              token;

    private String              requestId;

    public LoginMO(){
    }

    public LoginMO(boolean success, String errMsg){
        this.success = success;
        this.errMsg = errMsg;
    }

    public LoginMO(boolean success, String errMsg, String requestId){
        this.success = success;
        this.errMsg = errMsg;
        this.requestId = requestId;
    }
}
