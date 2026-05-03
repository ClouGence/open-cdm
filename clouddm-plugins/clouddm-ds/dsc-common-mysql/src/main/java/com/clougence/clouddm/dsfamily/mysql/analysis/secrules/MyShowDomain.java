package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class MyShowDomain extends RuleDomain {

    private MyShowType showType;

    // -- for DDL or Metadata
    private TargetType target;
    private String     catalog;
    private String     schema;
    private String     table;
    private String     view;
    private String     event;
    private String     proc;
    private String     trigger;
    private String     userOrRole;
    private String     func;

    public MyShowTypeKind getShowKind() {
        if (this.showType == null) {
            return null;
        } else {
            return this.showType.getTypeKind();
        }
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, StringUtils.isNotBlank(this.table) ? this.table : this.view);
        map.put(TargetType.Event, this.event);
        map.put(TargetType.Procedure, this.proc);
        map.put(TargetType.Trigger, this.trigger);
        map.put(TargetType.Function, this.func);
        map.put(TargetType.UserOrRole, this.userOrRole);
        map.put(TargetType.View, this.view);
        return Collections.singletonList(map);
    }
}
