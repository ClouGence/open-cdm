package com.clougence.rdp.controller.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/18 16:25
 */
@Getter
@Setter
public class ResourceSummaryVO {

    private long subAccountCounts;

    private long workerCounts;

    private long dsCounts;

    private long dsAuthCounts;
}
