package com.clougence.rdp.controller.model.fo;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @author bucketli 2021/1/13 09:42
 */
@Data
public class CheckDsAuthFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long         dataSourceId;

    /** schema or schema,table... decide by auth level */
    @NotEmpty(message = "{notempty.levelelements}")
    private List<String> levelElements;

    private String       dsAuthKind;
}
