package com.clougence.clouddm.console.web.component.project.action;

import com.clougence.clouddm.console.web.dal.model.DmProjectChangeDO;

public interface ChangeAction {

    void doAction(DmProjectChangeDO change) throws Exception;
}
