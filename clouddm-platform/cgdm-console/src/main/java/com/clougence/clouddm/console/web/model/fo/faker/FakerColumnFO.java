package com.clougence.clouddm.console.web.model.fo.faker;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */

@Getter
@Setter
public class FakerColumnFO {

    @NotBlank(message = "column name must not null")
    private String              name;
    @NotBlank(message = "seedType  must not null")
    private String              seedType;

    private Boolean             ignoreColsInsert;

    private Boolean             ignoreColsUpdate;

    private Boolean             ignoreColsUpdateWhere;

    private Boolean             ignoreColsDeleteWhere;

    private Map<String, Object> seedConfig;
}
