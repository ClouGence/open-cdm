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
package com.clougence.clouddm.console.web.controller.audit;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SQL_AUDIT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.audit.SqlAuditFO;
import com.clougence.clouddm.console.web.model.fo.project.GuideUsersFO;
import com.clougence.clouddm.console.web.model.vo.audit.OperateUserVO;
import com.clougence.clouddm.console.web.model.vo.audit.SqlAuditVO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.audit.SqlAuditService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/audit/sqlAudit")
@Slf4j
public class SqlAuditController {

    @Resource
    private AuthDal         authDal;
    @Resource
    private SqlAuditService sqlAuditService;
    @Resource
    private BrowseService   browseService;

    @RequestAuth(DM_SQL_AUDIT)
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public ResWebData<?> queryAll(@Valid @RequestBody SqlAuditFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<SqlAuditVO> sqlAuditVOS = sqlAuditService.queryUserAllAudit(puid, fo.getUserUid(), fo.getSqlKind(), fo.getResourcePath(), fo.getDsId(), fo.getRequester(), fo
            .getStatus(), fo.getOpStart(), fo.getOpEnd(), fo.getPageData().getStartId(), fo.getPageData().getPageSize());

        return ResWebDataUtils.buildSuccess(sqlAuditVOS);
    }

    @RequestAuth(DM_SQL_AUDIT)
    @RequestMapping(value = "/listDs", method = RequestMethod.POST)
    public ResWebData<?> listDs(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds list
        List<BrowseLevelsVO> levels = this.browseService.listDsIncludeAllEnv(puid, uid);
        return ResWebDataUtils.buildSuccess(levels);
    }

    @RequestAuth(DM_SQL_AUDIT)
    @RequestMapping(value = "/operateUser", method = RequestMethod.POST)
    public ResWebData<?> operateUser(HttpServletRequest request, @Valid @RequestBody GuideUsersFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmAuthUserDO mainUser = this.authDal.userMapper().queryByUid(puid);
        String search = StringUtils.isBlank(fo.getSearch()) ? null : fo.getSearch();
        List<RsAuthPersonObj> result = this.authDal.userMapper().searchUserByKeywords(mainUser.getId(), search);
        List<OperateUserVO> vos = result.stream().map(DmConvertUtils::convertToOperateUserVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }
}
