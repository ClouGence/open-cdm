/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.model.fo.editor.query;

import com.clougence.clouddm.platform.dal.util.PageObj;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlScriptListFO {

    @Size(max = 128)
    private String  keyword;
    private PageObj page;
}
