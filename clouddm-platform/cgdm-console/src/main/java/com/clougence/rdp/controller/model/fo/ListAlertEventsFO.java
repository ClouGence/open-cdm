package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.clougence.rdp.service.enumeration.AlertEventStatus;

import lombok.Data;

/**
 * @author bucketli 2021/1/30 17:00
 */
@Data
public class ListAlertEventsFO {

    @Min(value = 0, message = "{min.startid}")
    long             startId;

    @Min(value = 1, message = "{min.pagesize}")
    @Max(value = PageData.MAX_PAGE_SIZE, message = "{max.pagesizen}" + PageData.MAX_PAGE_SIZE)
    int              pageSize;

    Long             leftTimeMillis;

    Long             rightTimeMillis;

    AlertEventStatus status;
}
