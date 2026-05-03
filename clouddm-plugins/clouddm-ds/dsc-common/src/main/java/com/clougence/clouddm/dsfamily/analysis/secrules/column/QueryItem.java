package com.clougence.clouddm.dsfamily.analysis.secrules.column;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryItem {

    private List<RuleDomain> columns = new ArrayList<>();

    // for find true column
    private String           itemAlias;

    //
    private String           column;
    private String           table;

    private boolean          isSelectAll;

    public void addDomain(RuleDomain domain) {
        this.columns.add(domain);
    }
}
