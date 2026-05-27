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
package com.clougence.clouddm.platform.dal.model.auth;

/**
 * @author bucketli 2020/2/27 19:52
 */
public enum VerifyCodeType {
    REGISTER,
    LOGIN,
    RESET_PASSWORD,
    RESET_OP_PASSWORD,
    FETCH_WORKER_DEPLOY_CORE_CONFIG,
    FETCH_USER_AK_SK,
    RESET_USER_AK_SK,
    PRODUCT_TRIAL,
    DELETE_JOB,
    DELETE_POSITION,
    UPDATE_USER_INFO,
    UPDATE_USER_PHONE,
    UPDATE_USER_EMAIL,
    FETCH_AUTH_CODE,
    VERIFY_OLD_ACCOUNT,
    SSO_REGISTER_BIND,
    VERIFY_EMAIL_TEST,
    VERIFY_IM_TEST,
}
