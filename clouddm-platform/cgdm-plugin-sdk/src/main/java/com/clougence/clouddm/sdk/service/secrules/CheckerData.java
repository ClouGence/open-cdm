package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;

import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
public class CheckerData {

    private final String     content;
    private final RuleDomain domain;

    private List<UmiTypes>   dsLevelsDef;
    private String           currentCatalog;
    private String           currentSchema;
    private int              startLine;
    private int              startColumn;

    public CheckerData(String content, RuleDomain domain){
        this.content = content;
        this.domain = domain;
    }
}
