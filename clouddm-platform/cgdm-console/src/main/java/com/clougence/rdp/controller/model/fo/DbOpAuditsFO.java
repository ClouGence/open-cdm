package com.clougence.rdp.controller.model.fo;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author bucketli 2021/1/18 17:47
 */
@Data
public class DbOpAuditsFO {

    @NotNull(message = "start id can not be null.")
    @Min(value = 0, message = "start id can not be negative.")
    private Long    startId;

    @NotNull(message = "page size can not be null.")
    @Min(value = 1, message = "page size must large than 0.")
    @Max(value = 100, message = "page size must small than 101.")
    private Integer pageSize;

    private String  userName;

    private Date    startExecDate;

    private Date    endExecDate;
}
