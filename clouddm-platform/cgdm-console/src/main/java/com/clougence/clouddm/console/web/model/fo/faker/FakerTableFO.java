package com.clougence.clouddm.console.web.model.fo.faker;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 *
 */

@Getter
@Setter
public class FakerTableFO {

    @NotBlank(message = "table name must not blank")
    private String              name;

    @NotNull(message = "generate rows value must not null")
    private int                 total;

    private String              wherePolitic;

    private String              updatePolitic;

    private String              insertPolitic;

    private List<FakerColumnFO> columnConfigs;
}
