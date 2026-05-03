package com.clougence.clouddm.console.web.model.fo.checkrules;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleSensitiveMode;
import com.clougence.clouddm.console.web.dal.enumeration.RuleTarget;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RuleSaveFO {

    private Long                 ruleId;
    @NotNull(message = "{checkrules.rulekind.notnull}")
    private RuleKind             ruleKind;
    private boolean              force;

    @NotBlank(message = "{checkrules.rulename.notblank}")
    private String               ruleName;
    private String               ruleDesc;
    @NotNull(message = "{checkrules.ruletype.notnull}")
    private RuleScriptType       ruleType;
    @NotBlank(message = "{checkrules.content.notblank}")
    private String               content;

    // for QUERY
    private List<DataSourceType> dsRange;
    private RuleTarget           targetType;

    // for SENSITIVE
    private RuleSensitiveMode    senMode;
}
