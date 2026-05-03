package com.clougence.clouddm.console.web.controller.asynctask;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskScheduleService;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.DmAsyncTaskMapper;
import com.clougence.clouddm.console.web.dal.model.DmAsyncTaskDO;
import com.clougence.clouddm.console.web.model.fo.asyntask.ActionAsyncTaskFO;
import com.clougence.clouddm.console.web.model.vo.faker.DmAsyncTaskVO;
import com.clougence.clouddm.console.web.service.asyntask.AsyncTaskService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.service.RdpUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(DmControllerUrlPrefix.CONSOLE_PREFIX + "/asynctask/dockTask")
@Slf4j
public class DockTaskController {

    @Resource
    private DmAsyncTaskMapper        asyncTaskMapper;
    @Resource
    private AsyncTaskService         asyncTaskService;
    @Resource
    private AsyncTaskScheduleService scheduleService;

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/listDockTask", method = RequestMethod.POST)
    public ResWebData<?> listDockTask(HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<DmAsyncTaskDO> tasks = this.asyncTaskService.listDockList(uid);
        List<DmAsyncTaskVO> vos = tasks.stream().map(DmConvertUtils::convertToDmAsyncTaskVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/cancelTask", method = RequestMethod.POST)
    public ResWebData<?> cancelTask(@Valid @RequestBody ActionAsyncTaskFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmAsyncTaskDO taskDO = this.asyncTaskMapper.queryByIdAndOwnerUid(fo.getTaskId(), uid);
        if (taskDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_NOT_EXIST_ERROR.name(), fo.getTaskId()));
        } else {
            this.scheduleService.cancelTask(fo.getTaskId(), DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_CANCEL_AT_CONSOLE_MESSAGE.name()));
            return ResWebDataUtils.buildSuccess();
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/pauseTask", method = RequestMethod.POST)
    public ResWebData<?> pauseTask(@Valid @RequestBody ActionAsyncTaskFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmAsyncTaskDO taskDO = this.asyncTaskMapper.queryByIdAndOwnerUid(fo.getTaskId(), uid);
        if (taskDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_NOT_EXIST_ERROR.name(), fo.getTaskId()));
        } else {
            this.scheduleService.pauseTask(fo.getTaskId(), DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_PAUSE_AT_CONSOLE_MESSAGE.name()));
            return ResWebDataUtils.buildSuccess();
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/retryTask", method = RequestMethod.POST)
    public ResWebData<?> retryTask(@Valid @RequestBody ActionAsyncTaskFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmAsyncTaskDO taskDO = this.asyncTaskMapper.queryByIdAndOwnerUid(fo.getTaskId(), uid);
        if (taskDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_NOT_EXIST_ERROR.name(), fo.getTaskId()));
        } else {
            this.scheduleService.retryTask(fo.getTaskId(), DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_RETRY_AT_CONSOLE_MESSAGE.name()));
            return ResWebDataUtils.buildSuccess();
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/resumeTask", method = RequestMethod.POST)
    public ResWebData<?> resumeTask(@Valid @RequestBody ActionAsyncTaskFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmAsyncTaskDO taskDO = this.asyncTaskMapper.queryByIdAndOwnerUid(fo.getTaskId(), uid);
        if (taskDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_NOT_EXIST_ERROR.name(), fo.getTaskId()));
        } else {
            this.scheduleService.resumeTask(fo.getTaskId(), DmI18nUtils.getMessage(I18nDmMsgKeys.TASK_RESUME_AT_CONSOLE_MESSAGE.name()));
            return ResWebDataUtils.buildSuccess();
        }
    }
}
