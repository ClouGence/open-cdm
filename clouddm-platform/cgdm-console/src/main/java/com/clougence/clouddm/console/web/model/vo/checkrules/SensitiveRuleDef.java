package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.SecMatchMode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class SensitiveRuleDef {

    private List<I18nKeyVal>                                         matchMode;
    private Map<SecMatchMode, Map<DataSourceType, List<I18nKeyVal>>> scopeByMatchMode;
    private List<I18nKeyVal>                                         senMode;

}
