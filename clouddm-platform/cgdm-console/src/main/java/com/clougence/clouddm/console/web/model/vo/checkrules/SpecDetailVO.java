package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
public class SpecDetailVO {

    private long              specId;
    private String            specName;
    private String            specDesc;
    private boolean           enable;
    private List<SpecRulesVO> ruleList;

}
