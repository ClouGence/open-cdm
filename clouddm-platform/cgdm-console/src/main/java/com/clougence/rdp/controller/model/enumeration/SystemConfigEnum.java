package com.clougence.rdp.controller.model.enumeration;

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
