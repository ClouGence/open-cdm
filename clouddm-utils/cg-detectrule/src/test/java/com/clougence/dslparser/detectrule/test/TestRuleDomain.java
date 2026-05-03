package com.clougence.dslparser.detectrule.test;

import java.util.ArrayList;
import java.util.List;

import com.clougence.dslparser.detectrule.test.func.FuncGroupUtils;
import com.clougence.dslparser.detectrule.test.rule.RdbColumnDomain;
import com.clougence.dslparser.detectrule.test.rule.SecQueryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestRuleDomain {

    private FuncGroupUtils     fun;
    private RdbColumnDomain    domain;

    private List<String>       stringList = new ArrayList<>();
    private String[]           stringArray;

    private List<SecQueryType> queryTypes = new ArrayList<>();
}
