package com.clougence.rdp.controller.model.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author wanshao <344277934@qq.com> create time is 2022/6/20
 **/
@Data
@Builder
public class RdpAuthedResourceInfoVO {

    @Tolerate
    public RdpAuthedResourceInfoVO(){
    }

    private Long    startTimeMs;

    private Long    remainingTimeMs;

    private Long    endTimeMs;

    private Integer authedTaskCount;

    private String  licenseUuid;

    private Long    authedSubAccountCount;

    private Long    authedTaskTableCount;

    private Long    authedDsQueryCount;
}
