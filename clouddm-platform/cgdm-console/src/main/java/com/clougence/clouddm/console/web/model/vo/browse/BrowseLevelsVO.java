package com.clougence.clouddm.console.web.model.vo.browse;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class BrowseLevelsVO {

    private String              objId;
    private String              objName;
    private String              objAlias;
    private String              objType;
    private Map<String, Object> objAttr;

    @Override
    public String toString() {
        return "BrowseLevelsVO{" + "objId=" + objId + ", objName='" + objName + '\'' + ", objAlias='" + objAlias + '\'' + ", objType='" + objType + '\'' + '}';
    }
}
