package com.clougence.rdp.controller.model.lo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chunlin create time is 2024/11/5
 */
@Getter
@Setter
public class UpdateUserInfoLO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String targetUid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newSubAccount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldSubAccount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newUserName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldUserName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newEmail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldEmail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newPhone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldPhone;
}
