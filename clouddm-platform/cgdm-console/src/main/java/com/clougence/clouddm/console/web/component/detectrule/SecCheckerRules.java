package com.clougence.clouddm.console.web.component.detectrule;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.secrules.CheckerRule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecCheckerRules {

    private final boolean           valid;
    private final long              envId;
    private final long              dsId;
    private final String            dsName;
    private final DataSourceType    dsType;

    private final String            dsUseSpecName;

    private final List<CheckerRule> queryRuleList;
    private final List<CheckerRule> senRuleList;

    public SecCheckerRules(){
        this.valid = false;
        this.envId = 0L;
        this.dsId = 0L;
        this.dsName = null;
        this.dsType = null;
        this.dsUseSpecName = "";
        this.queryRuleList = Collections.emptyList();
        this.senRuleList = Collections.emptyList();

    }

    public SecCheckerRules(long envId, long dsId, String dsName, DataSourceType dsType, String dsUseSpecName, List<CheckerRule> queryRuleList, List<CheckerRule> senRuleList){
        this.valid = true;
        this.envId = envId;
        this.dsId = dsId;
        this.dsName = dsName;
        this.dsType = dsType;
        this.dsUseSpecName = dsUseSpecName;
        this.queryRuleList = (queryRuleList == null) ? Collections.emptyList() : queryRuleList;
        this.senRuleList = (senRuleList == null) ? Collections.emptyList() : senRuleList;
    }

    public boolean isEmpty() { return this.queryRuleList.isEmpty() && this.senRuleList.isEmpty(); }
}
