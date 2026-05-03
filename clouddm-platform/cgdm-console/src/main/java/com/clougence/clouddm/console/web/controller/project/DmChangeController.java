package com.clougence.clouddm.console.web.controller.project;

import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_PROJECT_OPERATE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_PROJECT_READ;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.project.model.ChangeCheckMO;
import com.clougence.clouddm.console.web.component.project.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStep;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.model.fo.project.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeBodyVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.console.web.service.project.DmChangeService;
import com.clougence.clouddm.console.web.service.project.DmScmService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/project/change")
@Slf4j
public class DmChangeController {

    @Resource
    private DmChangeService       dmChangeService;
    @Resource
    private DmScmService          dmScmService;
    @Resource
    private DmProjectMapper       dmProjectMapper;
    @Resource
    private DmProjectDevopsMapper dmProjectDevopsMapper;
    @Resource
    private RdpDataSourceMapper   rdpDataSourceMapper;

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeList", method = RequestMethod.POST)
    public ResWebData<?> projectChangeList(HttpServletRequest request, @Valid @RequestBody ProjectChangeListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        IPage<ProjectChangeVO> result = this.dmChangeService.queryChangeByProjectAndQuery(puid, fo.getProjectId(), fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeDetail", method = RequestMethod.POST)
    public ResWebData<?> changeDetail(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmProjectChangeDO changeDO = this.dmChangeService.queryChangeById(puid, fo.getChangeId());
        if (changeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(puid, changeDO.getRefDevopsId());
        RdpDataSourceDO dsDO = this.rdpDataSourceMapper.queryDsIdentityById(devopsDO.getDsId());
        DmProjectScmDO scmDO = this.dmScmService.queryScmById(puid, devopsDO.getRefScmId());
        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(puid, changeDO.getRefProjectId());

        ProjectChangeVO vo = DmConvertUtils.convertToProjectChangeVO(projectDO, changeDO, //
                CollectionUtils.asMap(devopsDO.getId(), devopsDO),//
                CollectionUtils.asMap(dsDO.getId(), dsDO),//
                CollectionUtils.asMap(scmDO.getId(), scmDO));
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeBody", method = RequestMethod.POST)
    public ResWebData<?> changeBody(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmProjectChangeDO changeDO = this.dmChangeService.queryChangeById(puid, fo.getChangeId());
        if (changeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        ProjectChangeBodyVO vo = this.dmChangeService.fetchChangeBodyByChangeId(puid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeChecks", method = RequestMethod.POST)
    public ResWebData<?> changeChecks(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmProjectChangeDO changeDO = this.dmChangeService.queryChangeById(puid, fo.getChangeId());
        if (changeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (changeDO.getCurrentStep() == ProjectChangeStep.INIT) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_STEP_NO_BODY_ERROR.name()));
        }

        List<DmProjectChangeItemDO> changeList = this.dmChangeService.fetchChangeCheckByChangeId(puid, fo.getChangeId());
        List<ChangeCheckMO> checkList = new ArrayList<>();

        for (DmProjectChangeItemDO item : changeList) {
            checkList.add(JsonUtils.toObj(item.getContent(), ChangeCheckMO.class));
        }

        return ResWebDataUtils.buildSuccess(checkList);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeApproval", method = RequestMethod.POST)
    public ResWebData<?> changeApproval(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmProjectChangeDO changeDO = this.dmChangeService.queryChangeById(puid, fo.getChangeId());
        if (changeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        switch (changeDO.getCurrentStep()) {
            case INIT:
            case CHECK:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_STEP_NO_BODY_ERROR.name()));
            default:
                break;
        }

        ChangeTicketInfoResult result = this.dmChangeService.fetchChangeApprovalByChangeId(puid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeExecute", method = RequestMethod.POST)
    public ResWebData<?> changeExecute(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmProjectChangeDO changeDO = this.dmChangeService.queryChangeById(puid, fo.getChangeId());
        if (changeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        switch (changeDO.getCurrentStep()) {
            case INIT:
            case CHECK:
            case APPROVAL:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_STEP_NO_BODY_ERROR.name()));
            default:
                break;
        }

        ChangeExecuteInfo result = this.dmChangeService.fetchChangeExecuteByChangeId(puid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/skipChecks", method = RequestMethod.POST)
    public ResWebData<?> skipChecks(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.skipCheck(puid, uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/confirmExec", method = RequestMethod.POST)
    public ResWebData<?> confirmExec(HttpServletRequest request, @Valid @RequestBody ProjectChangeConfirmExecRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.confirmExec(puid, uid, fo.getChangeId(), fo.getConfig());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeExecJobInfo", method = RequestMethod.POST)
    public ResWebData<?> changeExecJobInfo(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmAutoExecJobVO vo = this.dmChangeService.queryExecJobInfo(puid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeExecTaskList", method = RequestMethod.POST)
    public ResWebData<?> changeExecTaskList(HttpServletRequest request, @Valid @RequestBody ProjectChangeExecTaskListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmPageVO<DmAutoExecTaskVO> vo = this.dmChangeService.queryExecTaskList(puid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/changeExecLog", method = RequestMethod.POST)
    public ResWebData<?> changeExecJobLog(HttpServletRequest request, @Valid @RequestBody ProjectChangeExecLogFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<DmBizLogVO> vo = this.dmChangeService.queryExecLog(puid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/changeExecJobPause", method = RequestMethod.POST)
    public ResWebData<?> changeExecJobPause(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.pauseExecJob(puid, uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/changeExecJobStart", method = RequestMethod.POST)
    public ResWebData<?> changeExecJobStart(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.startExecJob(puid, uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/changeExecJobRetry", method = RequestMethod.POST)
    public ResWebData<?> changeExecJobRetry(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.retryExecJob(puid, uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/changeExecJobAbort", method = RequestMethod.POST)
    public ResWebData<?> changeExecJobAbort(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.abortExecJob(puid, uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/changeExecTaskSkip", method = RequestMethod.POST)
    public ResWebData<?> skipAutoExecTask(HttpServletRequest request, @Valid @RequestBody ProjectChangeExecSkipTaskFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.skipExecTask(puid, uid, fo.getChangeId(), fo.getTaskId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/changeRetry", method = RequestMethod.POST)
    public ResWebData<?> changeRetry(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.retryChange(puid, uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = DM_PROJECT_OPERATE)
    @RequestMapping(value = "/changeClose", method = RequestMethod.POST)
    public ResWebData<?> changeClose(HttpServletRequest request, @Valid @RequestBody ProjectChangeRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.closeChange(puid, uid, fo.getChangeId());
        return ResWebDataUtils.buildSuccess();
    }
}
