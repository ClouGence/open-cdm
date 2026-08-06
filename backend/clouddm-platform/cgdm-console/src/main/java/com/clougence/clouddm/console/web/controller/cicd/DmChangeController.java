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
package com.clougence.clouddm.console.web.controller.cicd;

import static com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_CICD_FLOW_OPERATE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_CICD_FLOW_READ;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeListFO;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeRequestFO;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeSqlPreviewFO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeSqlPreviewVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.cicd.DmChangeService;
import com.clougence.clouddm.console.web.service.cicd.DmScmService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeDO;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/cicd/change")
@Slf4j
public class DmChangeController {

    @Resource
    private ChangeFlowDal   changeFlowDal;
    @Resource
    private DataSourceDal   dsDal;
    @Resource
    private DmChangeService dmChangeService;
    @Resource
    private DmScmService    dmScmService;

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/changeList", method = RequestMethod.POST)
    public ResWebData<?> changeList(HttpServletRequest request, @Valid @RequestBody ChangeListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmPageVO<ChangeVO> result = this.dmChangeService.queryChangeByFlowAndQuery(puid, fo.getFlowId(), fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/changeDetail", method = RequestMethod.POST)
    public ResWebData<?> changeDetail(HttpServletRequest request, @Valid @RequestBody ChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmChangeDO changeDO = this.dmChangeService.queryChangeById(fo.getChangeId());
        if (changeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        DmChangeFlowDO gitOpsFlowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(puid, changeDO.getRefFlowId());
        DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(gitOpsFlowDO.getDsId());
        DmGitOpsScmDO scmDO = this.dmScmService.queryScmById(puid, gitOpsFlowDO.getRefScmId());
        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(puid, changeDO.getRefFlowId());

        ChangeVO vo = DmConvertUtils.convertToChangeVO(flowDO, changeDO, //
                CollectionUtils.asMap(gitOpsFlowDO.getId(), gitOpsFlowDO),//
                CollectionUtils.asMap(dsDO.getId(), dsDO),//
                CollectionUtils.asMap(scmDO.getId(), scmDO));
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/changeSqlPreview", method = RequestMethod.POST)
    public ResWebData<?> changeSqlPreview(@RequestBody ChangeSqlPreviewFO fo) {
        if (fo.getStartLine() < 1 || fo.getLineCount() < 1 || fo.getLineCount() > 1000) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_PREVIEW_RANGE_INVALID_ERROR.name()));
        }
        ChangeSqlPreviewVO vo = this.dmChangeService.previewChangeSql(fo.getChangeId(), fo.getStartLine(), fo.getLineCount(), fo.getContentName());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/changeApproval", method = RequestMethod.POST)
    public ResWebData<?> changeApproval(HttpServletRequest request, @Valid @RequestBody ChangeRequestFO fo) {
        DmChangeDO changeDO = this.dmChangeService.queryChangeById(fo.getChangeId());
        if (changeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        switch (changeDO.getCurrentStep()) {
            case INIT:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_STEP_NO_BODY_ERROR.name()));
            default:
                break;
        }

        ChangeTicketInfoResult result = this.dmChangeService.fetchChangeApprovalByChangeId(fo.getChangeId());
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(level = HIGH, value = DM_CICD_FLOW_OPERATE)
    @RequestMapping(value = "/changeRetry", method = RequestMethod.POST)
    public ResWebData<?> changeRetry(HttpServletRequest request, @Valid @RequestBody ChangeRequestFO fo) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.retryChange(uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_CICD_FLOW_OPERATE)
    @RequestMapping(value = "/changeClose", method = RequestMethod.POST)
    public ResWebData<?> changeClose(HttpServletRequest request, @Valid @RequestBody ChangeRequestFO fo) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.closeChange(uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }
}
