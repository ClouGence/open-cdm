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
package com.clougence.rdp.service;

import com.clougence.rdp.service.model.CheckVerifyMO;

/**
 * @author bucketli 2020/2/27 20:31
 */
public interface RdpVerifyService {

    int MAX_FAIL_TIME                            = 5;
    int PUNISH_MINUTES_WHEN_EXCEED_MAX_FAIL_TIME = 30;
    int CODE_EXPIRE_MINUTES                      = 20;
    int CODE_SEND_FREQUENCY_SECONDS              = 50;

    // /** when user registered ,init several verify records 1. verify DOs is exist 2. insert DO */
    // void initUserVerify(DmUserDO userDO);

    void dropUserVerify(String uid);

    /**
     * 1. query {@link CheckVerifyMO} by phone/email and code. 2. if exist, check fail times,if exceed the max value and fail datetime less than punish time (or re-count fail times), wait a period
     * time 3. if exist and valid ,return true 4. if is empty ,return false
     */
    void checkVerifyCode(CheckVerifyMO checkVerifyPO);

    void updateEmailOrPhoneByUid(String uid, String phone, String email);
}
