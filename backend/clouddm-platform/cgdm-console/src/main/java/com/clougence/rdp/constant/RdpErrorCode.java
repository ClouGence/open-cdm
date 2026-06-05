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
package com.clougence.rdp.constant;

/**
 * @author bucketli 2020/11/5 19:42
 */
public interface RdpErrorCode {

    String COMM_SYSTEM_ERROR               = "10001";
    String COMM_BAD_ARG_ERROR              = "10002";
    String COMM_ROLE_AUTH_PERMISSION_ERROR = "10003";
    String COMM_DATA_AUTH_PERMISSION_ERROR = "10004";
    String COMM_USER_RELOAD_ERROR          = "10005";

}
