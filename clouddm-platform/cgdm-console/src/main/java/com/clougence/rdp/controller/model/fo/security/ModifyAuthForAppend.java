package com.clougence.rdp.controller.model.fo.security;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/21 11:16:58
 */
@Getter
@Setter
public class ModifyAuthForAppend {

    private long         resId;

    private List<String> resPaths;

    private List<String> authLabels;

    private String       startTime;

    private String       endTime;
}
