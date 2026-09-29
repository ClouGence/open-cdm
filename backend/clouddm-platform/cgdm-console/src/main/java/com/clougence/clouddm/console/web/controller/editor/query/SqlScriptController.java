/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.controller.editor.query;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptCreateFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptIdFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptListFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.SqlScriptUpdateFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptDetailVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptListVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SqlScriptMutationVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.editor.script.SqlScriptService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/query/script")
public class SqlScriptController {

    @Resource
    private SqlScriptService sqlScriptService;

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResWebData<SqlScriptMutationVO> create(@Valid @RequestBody SqlScriptCreateFO fo, HttpServletRequest request) {
        return ResWebDataUtils.buildSuccess(this.sqlScriptService.create(uid(request), fo));
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResWebData<SqlScriptMutationVO> update(@Valid @RequestBody SqlScriptUpdateFO fo, HttpServletRequest request) {
        return ResWebDataUtils.buildSuccess(this.sqlScriptService.update(uid(request), fo));
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<SqlScriptListVO> list(@Valid @RequestBody SqlScriptListFO fo, HttpServletRequest request) {
        return ResWebDataUtils.buildSuccess(this.sqlScriptService.list(uid(request), fo));
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResWebData<SqlScriptDetailVO> detail(@Valid @RequestBody SqlScriptIdFO fo, HttpServletRequest request) {
        return ResWebDataUtils.buildSuccess(this.sqlScriptService.detail(uid(request), fo.getScriptId()));
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> delete(@Valid @RequestBody SqlScriptIdFO fo, HttpServletRequest request) {
        this.sqlScriptService.delete(uid(request), fo.getScriptId());
        return ResWebDataUtils.buildSuccess("ok");
    }

    private static String uid(HttpServletRequest request) {
        return (String) request.getAttribute(RdpUserService.UID);
    }
}
