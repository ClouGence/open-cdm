package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.AreaCode;

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
    private AreaCode    phoneAreaCode;

    @Pattern(regexp = "^\\d{1,20}$", message = "phone format is illegal.")
    private String      phone;

    @NotNull(message = "{notnull.verifytype}")
    private VerifyType  verifyType;

    @NotBlank(message = "{notblank.verifycode}")
    private String      verifyCode;

    @NotBlank(message = "{notblank.password}")
    private String      password;
}
