package com.clougence.clouddm.console.web.model.fo.faker;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TailLogFO {

    @NotNull(message = "{faker.session_id.notnull}")
    private String toolSessionId;

    private int    startLine;
}
