package com.clougence.clouddm.api.console.status;

import lombok.Getter;
import lombok.Setter;

/**
 * worker stat
 *
 * @author wanshao create time is 2020/1/22
 **/
@Getter
@Setter
public class UsageStats {

    private Integer sessionPoolUse;

    private Integer sessionPoolMax;

    private Integer toolsPoolUse;

    private Integer toolsPoolMax;

}
