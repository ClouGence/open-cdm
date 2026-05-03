package com.clougence.dslparser.detectrule.test.rule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbColumnDomain extends RuleDomain {

    private String  catalog;
    private String  schema;
    private String  table;
    private String  column;
    private String  comment;

    private String  typeDesc;
    private boolean nullable;
    private boolean primary;
    private boolean unique;
    private boolean index;
}
