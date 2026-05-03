package com.clougence.clouddm.console.web.component.execute;

import java.util.List;

import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;

public interface AfterSqlExecuteService {

    void handleAfterSqlSuccess(List<SqlExecNotifyDTO> audits);
}
