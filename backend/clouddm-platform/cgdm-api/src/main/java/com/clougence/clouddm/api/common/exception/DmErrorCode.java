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
package com.clougence.clouddm.api.common.exception;

public enum DmErrorCode {

    //COMM ERROR 100xx
    COMM_SYSTEM_ERROR("10001"),

    // DS ERROR  101xx
    DS_DISCONNECT_ERROR("10103"),
    DS_LANGUAGE_ERROR("10104"),

    //Worker & Cluster & RPC   102xx
    CLUSTER_HAVE_NO_WORKS_ERROR("10201"),

    // DAO ERROR 103xx
    O_AUTHORITY("10301"),
    O_DATA("10302"),

    // Plugin error 104xx
    PLUGIN_DAMAGED_ERROR("10401"),

    //Ticket error
    TICKET_SQL_PARSE_FAILED("20001");

    private final String code;

    DmErrorCode(String code){
        this.code = code;
    }

    public String code() {
        return this.code;
    }
}
