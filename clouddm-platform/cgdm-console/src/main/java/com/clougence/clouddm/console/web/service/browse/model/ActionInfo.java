package com.clougence.clouddm.console.web.service.browse.model;

import java.util.List;
import java.util.Map;

import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionInfo {

    private String                envId;
    private List<String>          oriLevels;
    private List<String>          dbLevels;
    private RdpDataSourceDO       dsDO;
    private List<UmiTypes>        levelsDef;
    private Map<UmiTypes, Object> levelsParam;
    private String                dataAuth;
    private UmiTypes              targetType;
    private String                targetName;
    private String                targetNewName;
    private String                targetExactName;
    // prepare for database object create
    private Map<String, Object>   options;
}
