package com.clougence.clouddm.console.web.global.notify;

import java.util.List;

import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;

public interface ConsoleSqlNotifyService {

    void sqlExecNotify(List<SqlExecNotifyDTO> audits, String wsn);
}
