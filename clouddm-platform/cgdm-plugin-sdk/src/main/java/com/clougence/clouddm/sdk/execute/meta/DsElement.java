package com.clougence.clouddm.sdk.execute.meta;

import java.util.Map;

import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class DsElement {

    private long                objId;
    private String              objName;
    private UmiTypes            objType;
    private Map<String, Object> objAttr;
}
