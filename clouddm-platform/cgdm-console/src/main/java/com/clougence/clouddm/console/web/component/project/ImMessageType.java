package com.clougence.clouddm.console.web.component.project;

import java.util.function.Predicate;

import com.clougence.clouddm.console.web.dal.model.DmProjectMsgDO;

public enum ImMessageType {

    ProjectStatus(DmProjectMsgDO::isEventProjectStatus),
    ProjectConfig(DmProjectMsgDO::isEventProjectConfig),
    ChangeLife(DmProjectMsgDO::isEventChangeLife),
    ChangeNotice(DmProjectMsgDO::isEventChangeNotice);

    private final Predicate<DmProjectMsgDO> testEnable;

    ImMessageType(Predicate<DmProjectMsgDO> testEnable){
        this.testEnable = testEnable;
    }

    public boolean testEnable(DmProjectMsgDO msgDO) {
        return this.testEnable.test(msgDO);
    }
}
