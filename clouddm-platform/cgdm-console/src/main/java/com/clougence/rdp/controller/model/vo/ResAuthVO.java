package com.clougence.rdp.controller.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/12 20:04
 */
@Getter
@Setter
public class ResAuthVO {

    private Long         id;

    private Long         resId;

    private String       resInstId;

    private String       resDesc;

    private String       level;

    private List<String> dsAuthKinds;

    private String       startTime;

    private String       endTime;
}
