/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.model.fo.editor.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlScriptIdFO {

    @NotNull
    @Positive
    private Long scriptId;
}
