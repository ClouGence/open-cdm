package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RuleUpdateVO {

    private long            ruleId;
    private RuleKind        ruleKind;
    private boolean         success;
    private String          message;
    private List<RefSpecVO> referer;
}
