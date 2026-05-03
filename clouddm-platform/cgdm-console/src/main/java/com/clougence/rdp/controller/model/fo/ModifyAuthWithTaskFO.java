package com.clougence.rdp.controller.model.fo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 09:42
 */
@Getter
@Setter
public class ModifyAuthWithTaskFO {

    private List<ModifyAuthWithTaskForAppendFO> appends;

    private List<ModifyAuthWithTaskForUpdateFO> updates;

    private List<ModifyAuthWithTaskForDeleteFO> deletes;
}
