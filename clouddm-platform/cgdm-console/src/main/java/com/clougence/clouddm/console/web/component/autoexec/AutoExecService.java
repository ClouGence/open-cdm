package com.clougence.clouddm.console.web.component.autoexec;

import java.util.List;

import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecTaskStatus;
import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.rdp.util.RdpPageDO;

public interface AutoExecService {

    // true == jobFinish
    boolean skipTask(String bizId, SQLJobBizType type, long taskId, String uid);

    void continueTask(String bizId, SQLJobBizType type, long taskId);

    void retryJob(String bizId, SQLJobBizType type, String uid);

    void endJob(String bizId, SQLJobBizType type, String uid);

    void stopJob(String bizId, SQLJobBizType type, String uid);

    DmAutoExecJobVO queryAutoExecJob(String bizId, SQLJobBizType type, boolean canOperate);

    DmPageVO<DmAutoExecTaskVO> queryAutoExecTaskList(String bizId, SQLJobBizType type, boolean canOperate, AutoExecTaskStatus status, RdpPageDO page);

    void createJob(String ownerUid, String execUser, DmAutoExecConfigFO config, DsLevels dsLevels, SQLJobBizType bizType, String bizId, List<SplitScript> scripts);
}
