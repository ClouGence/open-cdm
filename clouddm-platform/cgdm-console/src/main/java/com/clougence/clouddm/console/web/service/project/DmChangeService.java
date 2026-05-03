package com.clougence.clouddm.console.web.service.project;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.component.project.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeItemDO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeExecLogFO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeExecTaskListFO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeListFO;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeBodyVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.console.web.service.project.domain.CreateSuggest;

public interface DmChangeService {

    IPage<ProjectChangeVO> queryChangeByProjectAndQuery(String ownerUid, long projectId, ProjectChangeListFO fo);

    DmProjectChangeDO queryChangeById(String ownerUid, long changeId);

    ProjectChangeBodyVO fetchChangeBodyByChangeId(String ownerUid, long changeId);

    List<DmProjectChangeItemDO> fetchChangeCheckByChangeId(String ownerUid, long changeId);

    ChangeTicketInfoResult fetchChangeApprovalByChangeId(String ownerUid, long changeId);

    ChangeExecuteInfo fetchChangeExecuteByChangeId(String ownerUid, long changeId);

    void skipCheck(String ownerUid, String userUid, long changeId);

    void confirmExec(String ownerUid, String userUid, long changeId, DmAutoExecConfigFO fo);

    DmAutoExecJobVO queryExecJobInfo(String ownerUid, long changeId);

    DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String ownerUid, ProjectChangeExecTaskListFO fo);

    List<DmBizLogVO> queryExecLog(String ownerUid, ProjectChangeExecLogFO fo);

    void pauseExecJob(String ownerUid, String curUid, long changeId);

    void startExecJob(String ownerUid, String curUid, long changeId);

    void retryExecJob(String ownerUid, String curUid, long changeId);

    void abortExecJob(String ownerUid, String curUid, long changeId);

    void skipExecTask(String ownerUid, String curUid, long changeId, long taskId);

    void retryChange(String ownerUid, String curUid, long changeId);

    void restartChange(String ownerUid, String curUid, long changeId);

    void closeChange(String ownerUid, String curUid, long changeId);

    void verifyDevops(String ownerUid, long projectId, long devopsId);

    CreateSuggest createChangeSuggest(String ownerUid, long projectId, long devopsId, String commitId);

    ResWebData<String> triggerChangeSuggest(String ownerUid, long projectId, long devopsId, String commitId);
}
