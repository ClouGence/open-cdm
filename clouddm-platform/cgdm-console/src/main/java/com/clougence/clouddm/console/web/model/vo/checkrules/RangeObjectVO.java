package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.Map;

import com.clougence.clouddm.console.web.dal.enumeration.SecRangeType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class RangeObjectVO {

    private String              objId;
    private String              objName;
    private String              objDesc;
    private SecRangeType        objType;
    private Map<String, Object> objAttr;
}
