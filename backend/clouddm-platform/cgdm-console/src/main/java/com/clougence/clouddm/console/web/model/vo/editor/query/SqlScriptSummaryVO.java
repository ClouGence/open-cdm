/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlScriptSummaryVO {

    private Long   scriptId;
    private String name;
    private String dsType;
    private Long   version;
    private Date   gmtCreate;
    private Date   gmtModified;
}
