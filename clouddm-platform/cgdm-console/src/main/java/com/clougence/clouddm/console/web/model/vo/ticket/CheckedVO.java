package com.clougence.clouddm.console.web.model.vo.ticket;

import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.RuleLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 */
@Getter
@Setter
public class CheckedVO {

    private String        name;
    private String        desc;
    private RuleLevel     ruleLevel;
    private List<Integer> lines;
}
