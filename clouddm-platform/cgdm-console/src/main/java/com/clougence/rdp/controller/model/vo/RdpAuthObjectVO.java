package com.clougence.rdp.controller.model.vo;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class RdpAuthObjectVO {

    private long                objId;
    private String              objName;
    private String              objDesc;
    private String              objType;
    private Map<String, Object> objAttr;
    private boolean             leaf;

    @Override
    public String toString() {
        return "RdpAuthObjectVO{" + "objId=" + objId + ", objName='" + objName + '\'' + ", objDesc='" + objDesc + '\'' + ", objType='" + objType + '\'' + ", objAttr=" + objAttr
               + ", leaf=" + leaf + '}';
    }
}
