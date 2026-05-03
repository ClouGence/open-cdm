package com.clougence.rdp.controller.model.fo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 10:18
 */
@Getter
@Setter
public class ModifyAuthWithDsForUpdateFO {

    private long         authId;

    private long         dsId;

    private List<String> levels;

    private List<String> dsAuthKinds;

    private String       startTime;

    private String       endTime;
}
