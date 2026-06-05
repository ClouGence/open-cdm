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
package com.clougence.clouddm.console.web.model.fo.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author bucketli 2021/1/11 10:34
 */
@Data
public class AddSubAccountFO {

    @NotBlank(message = "{notblank.username}")
    private String userName;

    @NotBlank(message = "{notblank.subaccount}")
    private String subAccount;

    @Min(value = 1, message = "{min.roleid}")
    private long   roleId;

    @NotBlank(message = "{notblank.password}")
    private String password;

    @NotBlank(message = "{notblank.email}")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "email format is illegal.")
    private String email;

    @NotBlank(message = "{notblank.phone}")
    @Pattern(regexp = "^\\d{5,20}$", message = "phone format is illegal.")
    private String phone;
}
