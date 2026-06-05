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
package com.clougence.clouddm.comm.util;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseData<T> implements Serializable {

    private String  code;
    private String  msg;
    private T       data;

    private Boolean permission;
    private String  needResource;
    private String  needAuthKey;
    private String  needAuthKind;

    /**
     * Rsocket request will carry the requestId to help fetch the async result for sender. Other normal request, the field can be null.//todo need change to use requestId in workerIdentity
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  requestId;

    /**
     * Rsocket request will carry the wsn to help the rsocket server return async result to related sidecar. Other normal request, the field can be null.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  workerSeqNumber;

    public ResponseData(String code, String msg, String requestId, T data){
        this.code = code;
        this.msg = msg;
        this.requestId = requestId;
        this.data = data;
        this.permission = null;
    }

    public ResponseData(){
    }

    public boolean isSuccess() { return ResultEnum.SUCCESS.getCode().equals(code); }

    public boolean isFail() { return !ResultEnum.SUCCESS.getCode().equals(code); }
}
