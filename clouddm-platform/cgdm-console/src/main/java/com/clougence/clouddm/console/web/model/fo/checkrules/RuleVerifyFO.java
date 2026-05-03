package com.clougence.clouddm.console.web.model.fo.checkrules;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RuleVerifyFO {

    @NotNull(message = "{checkrules.ruletype.notnull}")
    private RuleScriptType type;

    @NotBlank(message = "{checkrules.content.notblank}")
    private String         content;
}
