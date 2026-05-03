package com.clougence.rdp.dal.model.dto;

import com.clougence.rdp.dal.model.RdpUserDO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chunlin create time is 2024/9/23
 */
@Getter
@Setter
public class RdpAuthUserDTO {

    private RdpUserDO user;
    private boolean   success;
    private String    errMsg;

    public RdpAuthUserDTO(RdpUserDO user, boolean success, String errMsg){
        this.user = user;
        this.success = success;
        this.errMsg = errMsg;
    }
}
