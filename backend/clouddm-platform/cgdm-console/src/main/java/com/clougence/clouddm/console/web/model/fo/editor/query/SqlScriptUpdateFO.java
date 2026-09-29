/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.model.fo.editor.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlScriptUpdateFO {

    @NotNull
    @Positive
    private Long scriptId;
    @NotNull
    @PositiveOrZero
    private Long version;
    @NotBlank
    @Size(max = 128)
    private String name;
    @NotBlank
    @Size(max = 64)
    private String dsType;
    @NotNull
    private String sqlContent;
}
