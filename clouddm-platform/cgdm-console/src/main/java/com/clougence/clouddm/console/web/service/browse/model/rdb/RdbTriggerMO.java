package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbTriggerMO extends BrowseObjectMO {

    private String       triggerName;
    private String       triggerBody;
    private List<String> triggerEvent;
    private String       triggerTable;
    private String       triggerTime;
}
