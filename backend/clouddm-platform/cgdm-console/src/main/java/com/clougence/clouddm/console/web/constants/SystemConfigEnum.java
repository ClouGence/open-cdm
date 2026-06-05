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
package com.clougence.clouddm.console.web.constants;

import lombok.Getter;

/**
 * @author mode 2020/7/15 16:31
 */
@Getter
public enum SystemConfigEnum {

    EMAIL_HOST_KEY("spring.mail.host"),
    EMAIL_PORT_KEY("spring.mail.port"),
    EMAIL_USERNAME_KEY("spring.mail.username"),
    EMAIL_PASSWORD_KEY("spring.mail.password"),
    EMAIL_FROM_KEY("spring.mail.properties.from"),

    DINGDING_URL_TOKEN_KEY("clougence.rdp.alert.dingtalk.alerturl");

    private final String configCode;

    SystemConfigEnum(String configCode){
        this.configCode = configCode;
    }
}
