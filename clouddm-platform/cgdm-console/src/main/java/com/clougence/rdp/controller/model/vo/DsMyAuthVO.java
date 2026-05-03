package com.clougence.rdp.controller.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/12 20:04
 */
@Getter
@Setter
public class DsMyAuthVO {

    private Long         id;

    private String       dsId;

    private String       level;

    private boolean      diffuse;

    private List<String> dsAuthKinds;

    private String       startTime;

    private String       endTime;
}
