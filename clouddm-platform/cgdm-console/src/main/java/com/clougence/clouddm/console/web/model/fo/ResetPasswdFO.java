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
package com.clougence.clouddm.console.web.model.fo;

import com.clougence.clouddm.platform.dal.model.auth.VerifyType;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author bucketli 2021/1/8 21:55
 */
@Data
public class ResetPasswdFO {

    @NotNull(message = "{notnull.accounttype}")
    private AccountType accountType;

    private String      subAccount;

    /**
     * verify by email
     */
    private String      email;

    /**
     * verify by phone
     */

    @Pattern(regexp = "^\\d{1,20}$", message = "phone format is illegal.")
    private String      phone;

    @NotNull(message = "{notnull.verifytype}")
    private VerifyType  verifyType;

    @NotBlank(message = "{notblank.verifycode}")
    private String      verifyCode;

    @NotBlank(message = "{notblank.password}")
    private String      password;
}
