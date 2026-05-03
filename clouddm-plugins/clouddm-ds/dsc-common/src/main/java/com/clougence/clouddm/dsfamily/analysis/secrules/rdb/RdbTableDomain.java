package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.*;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.detectrule.lang.reflect.RuleIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbTableDomain extends RuleDomain {

    private String                    catalog;
    private String                    schema;
    private String                    table;
    private String                    comment;

    private boolean                   hasPrimary;
    private boolean                   hasUnique;
    private boolean                   hasForeignKey;
    private boolean                   hasIndex;

    private List<String>              columns;

    // for create table like
    private String                    sourceSchema;
    private String                    sourceTable;

    // for table rename
    private String                    newName;

    // for select
    private boolean                   virtual;
    private String                    alias;

    @RuleIgnore
    private List<RdbColumnDomain>     columnDomains     = new ArrayList<>();
    @RuleIgnore
    private List<RdbConstraintDomain> constraintDomains = new ArrayList<>();
    @RuleIgnore
    private List<RdbIndexDomain>      indexDomains      = new ArrayList<>();

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        if (virtual) {
            List<Map<TargetType, String>> result = new ArrayList<>();
            for (RuleDomain child : getChildren()) {
                result.addAll(child.resolveResource());
            }
            return result;
        } else {
            Map<TargetType, String> map = new HashMap<>();
            map.put(TargetType.Catalog, this.catalog);
            map.put(TargetType.Schema, this.schema);
            map.put(TargetType.Table, this.table);
            return Collections.singletonList(map);
        }
    }
}
