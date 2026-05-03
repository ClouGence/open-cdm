package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AreaCode;

import lombok.Data;

/**
 * @author bucketli 2021/1/8 22:01
 */
@Data
public class RegisterFO {

    @NotBlank(message = "{notblank.username}")
    @Size(min = 2, max = 64, message = "{size.username}")
    private String     userName;

    @NotBlank(message = "{notblank.password}")
    private String     password;

    @NotNull(message = "{notnull.verifytype}")
    private VerifyType verifyType;

    @NotBlank(message = "{notblank.verifycode}")
    private String     verifyCode;

    //  validate by GlobalDeploySite
    //    @NotBlank(message = "{notblank.email}")
    //    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "{pattern.email}")
    private String     email;

    //  validate by GlobalDeploySite
    //    @NotBlank(message = "{notblank.phone}")
    //    @Pattern(regexp = "^\\d{1,20}$", message = "{pattern.phone}")
    private String     phone;

    @NotBlank(message = "{notblank.country}")
    private String     country;

    private AreaCode   phoneAreaCode;

    private String     company;

    private boolean    contactMe;

    private String     src;

    private String     keyword;

    private String     clientId;

    private String     convertUrl;
}
