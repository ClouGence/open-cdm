package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbResourceDomain extends RuleDomain {

    private String     catalog;
    private String     schema;
    private String     name;

    private TargetType target;

    private boolean    needSupply;
    //    private boolean    datasourceAuth;

    public RdbResourceDomain(){
    }

    public RdbResourceDomain(SecQueryType secQueryType, SecQueryKind kind){
        this(secQueryType, kind, false, TargetType.Unknown);
    }

    public RdbResourceDomain(SecQueryType secQueryType, SecQueryKind kind, boolean needSupply, TargetType type){
        this.setSqlType(secQueryType);
        this.setAuditKind(kind);
        this.needSupply = needSupply;
        this.target = type;
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        return Collections.emptyList();
    }
}
