package com.clougence.rdp.controller.model.fo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 09:42
 */
@Getter
@Setter
public class ModifyAuthWithTaskForAppendFO {

    private long         dataJobId;

    private List<String> dsAuthKinds;

    private String       startTime;

    private String       endTime;
}
