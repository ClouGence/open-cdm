package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseTriggerVO extends BrowseObjectVO {

    private String              sql;
    private Object              triggerEvent;
    private String              triggerTable;
    private String              triggerTime;
    private List<String>        triggerColumns;

    private Map<String, Object> features = new HashMap<>();
}
