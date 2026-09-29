/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.console.web.service.editor.script;

import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptCreateFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptListFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptUpdateFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptDetailVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptListVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptMutationVO;

public interface SqlScriptService {

    SqlScriptMutationVO create(String ownerUid, SqlScriptCreateFO fo);

    SqlScriptMutationVO update(String ownerUid, SqlScriptUpdateFO fo);

    SqlScriptListVO list(String ownerUid, SqlScriptListFO fo);

    SqlScriptDetailVO detail(String ownerUid, long scriptId);

    void delete(String ownerUid, long scriptId);
}
