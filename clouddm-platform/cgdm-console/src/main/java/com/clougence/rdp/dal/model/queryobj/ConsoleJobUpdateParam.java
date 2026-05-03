package com.clougence.rdp.dal.model.queryobj;

import java.util.Date;

import com.clougence.rdp.dal.enumeration.ConsoleTaskState;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author bucketli 2020-01-03 16:35
 * @since 1.1.3
 */
@Data
@Builder
public class ConsoleJobUpdateParam {

    @Tolerate
    public ConsoleJobUpdateParam(){
    }

    private long             id;

    private ConsoleTaskState taskState;

    private Date             launchTime;

    private Date             finishTime;
}
