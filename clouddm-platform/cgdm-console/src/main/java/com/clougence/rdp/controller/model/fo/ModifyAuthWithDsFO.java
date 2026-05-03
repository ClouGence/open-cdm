package com.clougence.rdp.controller.model.fo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 09:42
 */
@Getter
@Setter
public class ModifyAuthWithDsFO {

    private List<ModifyAuthWithDsForAppendFO> appends;

    private List<ModifyAuthWithDsForUpdateFO> updates;

    private List<ModifyAuthWithDsForDeleteFO> deletes;
}
