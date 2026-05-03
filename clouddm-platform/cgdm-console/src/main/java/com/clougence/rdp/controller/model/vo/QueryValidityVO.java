package com.clougence.rdp.controller.model.vo;

import java.util.Date;

import lombok.Data;

/**
 * @author wanshao <344277934@qq.com> create time is 2022/6/2
 **/
@Data
public class QueryValidityVO {

    private Date    endTime;

    private Long    leftDays;

    private Long    remainingTimeMs;

    private Boolean needRemind;
    /**
     * If there is no auth code or auth fail, success is false
     */
    private Boolean success;

    public QueryValidityVO(Date endTime, Long leftDays, Long remainingTimeMs, Boolean needRemind, Boolean success){
        this.endTime = endTime;
        this.leftDays = leftDays;
        this.needRemind = needRemind;
        this.success = success;
        this.remainingTimeMs = remainingTimeMs;
    }
}
