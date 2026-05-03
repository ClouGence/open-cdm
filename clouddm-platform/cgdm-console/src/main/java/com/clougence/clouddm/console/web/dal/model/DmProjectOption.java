package com.clougence.clouddm.console.web.dal.model;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
public class DmProjectOption {

    // exec default
    private boolean       transactional;
    private ErrorStrategy errorStrategy;
    private Long          retryWaitTime;
    private Long          retryCount;

    // flow
    private boolean       snapshot;
}
