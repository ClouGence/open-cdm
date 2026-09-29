/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.List;

import com.clougence.clouddm.console.web.model.vo.DmPageVO;

public class SqlScriptListVO extends DmPageVO<SqlScriptSummaryVO> {

    public SqlScriptListVO(long current, long size, long total, List<SqlScriptSummaryVO> records) {
        super(current, size, total, records);
    }
}
