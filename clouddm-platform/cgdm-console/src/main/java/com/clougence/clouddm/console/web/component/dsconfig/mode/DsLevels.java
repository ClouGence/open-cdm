package com.clougence.clouddm.console.web.component.dsconfig.mode;

import java.util.List;
import java.util.Map;

import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.rdp.util.RdpAuthUtils;
import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Getter;

@Getter
public class DsLevels {

    private final String                envId;
    private final List<String>          levels;
    private final List<String>          dbLevels;
    private final RdpDataSourceDO       dsDO;
    private final List<UmiTypes>        levelsDef;
    private final Map<UmiTypes, Object> levelsParam;

    public DsLevels(String envId, RdpDataSourceDO dsDO, List<String> levels, List<String> dbLevels, List<UmiTypes> levelsDef, Map<UmiTypes, Object> levelsParam){
        this.envId = envId;
        this.dsDO = dsDO;
        this.levels = levels;
        this.dbLevels = dbLevels;
        this.levelsDef = levelsDef;
        this.levelsParam = levelsParam;
    }

    public DsResPath asResPath() {
        return RdpAuthUtils.genResPathByList(this.dbLevels);
    }
}
