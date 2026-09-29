/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SqlScriptMutationVO {

    private Long scriptId;
    private Long version;
    private Date gmtModified;
}
