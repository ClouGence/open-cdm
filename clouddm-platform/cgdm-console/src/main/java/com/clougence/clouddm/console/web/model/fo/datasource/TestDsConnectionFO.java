package com.clougence.clouddm.console.web.model.fo.datasource;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDsConnectionFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;
    //private String       keyWords;
}
