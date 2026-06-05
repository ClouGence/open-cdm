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

import com.clougence.clouddm.platform.dal.model.auth.VerifyCodeType;
import com.clougence.clouddm.platform.dal.model.auth.VerifyType;

import lombok.Data;

/**
 * @author bucketli 2020/2/28 14:01
 */
@Data
public class CheckVerifyMO {

    private boolean        isSubAccount;

    private String         subAccountName;

    private VerifyType     verifyType;

    private String         email;

    private String         phoneNumber;

    private String         verifyCode;

    private VerifyCodeType verifyCodeType;

    private String         uid;
}
