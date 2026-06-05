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

/**
 * @author wanshao create time is 2021/1/11
 **/
public enum RSocketRespCode {

    /**
     * Success result enum.
     */
    SUCCESS("1", "Request success"),
    /**
     * Error result enum.
     */
    ERROR("0", "Request failure");

    private String code;
    private String msg;

    RSocketRespCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() { return code; }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) { this.code = code; }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() { return msg; }

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) { this.msg = msg; }
}
