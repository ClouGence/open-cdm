package com.clougence.clouddm.api.common.crypt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordInfo {

    private String plainPassword;
    private String encryptPassword;
    private String key;
    private String salt;

}
