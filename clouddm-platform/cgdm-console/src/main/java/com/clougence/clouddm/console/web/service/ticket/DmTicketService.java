package com.clougence.clouddm.console.web.service.ticket;

import java.util.List;

import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;

/**
 * @author Ekko
 * @date 2024/5/7 16:36
*/
public interface DmTicketService {

    DmTicketResultVO createSqlTicket(String puid, String uid, DmAddTicketFO fo);

    void confirmTicket(String puid, long ticketId, DmConfirmTicketFO fo);

    DmQueryTicketVO queryQueryTicketDetail(String puid, DmQueryTicketDetailFO fo);

    DmAutoExecJobVO queryExecJobInfo(String puid, String uid, long ticketId);

    DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String puid, String uid, DmQueryTaskListFO fo);

    void retryJob(String puid, String uid, long ticketId);

    void skipTask(String puid, String uid, DmQueryAutoExecFO fo);

    void canceledSkipTask(String puid, String uid, DmQueryAutoExecFO fo);

    List<DmBizLogVO> queryExecLog(String puid, DmQueryExecLogFO fo);

    void stopJob(String puid, String uid, long ticketId);

    void endAutoExecJob(String puid, String uid, long ticketId);
}
