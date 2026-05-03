package com.clougence.clouddm.console.web.model.fo.faker;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.model.faker.FakerRunModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakerDefFO {

    @NotNull(message = "{faker.levels.notnull}")
    private List<String>  levels;

    @NotNull(message = "{faker.table.notnull}")
    private String        table;

    @NotNull(message = "{faker.type.notnull}")
    private FakerRunModel type;
}
