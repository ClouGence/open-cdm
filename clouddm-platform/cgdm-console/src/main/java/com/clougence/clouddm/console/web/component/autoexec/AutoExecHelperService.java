package com.clougence.clouddm.console.web.component.autoexec;

import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;

public interface AutoExecHelperService {

    AutoExecHelper getHelper(SQLJobBizType type);
}
