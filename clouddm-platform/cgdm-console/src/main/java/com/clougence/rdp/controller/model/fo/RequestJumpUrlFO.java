package com.clougence.rdp.controller.model.fo;

import com.clougence.rdp.controller.model.enumeration.LoginAuthType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/2/1 19:15
 */
@Getter
@Setter
public class RequestJumpUrlFO {

    private String        primaryUid;
    private LoginAuthType type;

}
