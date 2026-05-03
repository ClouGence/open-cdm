package com.clougence.rdp.dal.model.queryobj;

import java.util.Date;

import com.clougence.rdp.dal.enumeration.ConsoleTaskState;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author bucketli 2020-01-03 16:32
 * @since 1.1.3
 */
@Data
@Builder
public class ConsoleTaskUpdateParam {

    @Tolerate
    public ConsoleTaskUpdateParam(){
    }

    private long             id;

    private String           message;

    private Date             executeTime;

    private Date             finishTime;

    private String           host;

    private ConsoleTaskState taskState;
}
