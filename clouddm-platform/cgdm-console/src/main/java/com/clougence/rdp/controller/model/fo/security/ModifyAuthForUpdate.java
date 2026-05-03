package com.clougence.rdp.controller.model.fo.security;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/21 11:16:58
 */
@Getter
@Setter
public class ModifyAuthForUpdate {

    private long         authId;

    private long         resId;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)

    private List<String> resPaths;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)

    private List<String> authLabels;

    private String       startTime;

    private String       endTime;
}
