package com.clougence.rdp.controller.model.lo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRoleLO {

    private String subAccountUid;
    private Long   oldRoleId;
    private String oldRoleName;
    private Long   newRoleId;
    private String newRoleName;
}
