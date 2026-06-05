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
package com.clougence.clouddm.comm.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Send back async response will use this class
 *
 * @author wanshao create time is 2021/1/11
 **/
@Getter
@Setter
public class RSocketRespDTO<T> extends RSocketRequestWrapperDTO {

    private String code;

    private String msg;

    private T      data;

    public RSocketRespDTO(){
    }

    public RSocketRespDTO(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RSocketRespDTO(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public RSocketRespDTO(RSocketRespCode resultEnum){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public RSocketRespDTO(RSocketRespCode resultEnum, T data){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = data;
    }

}
