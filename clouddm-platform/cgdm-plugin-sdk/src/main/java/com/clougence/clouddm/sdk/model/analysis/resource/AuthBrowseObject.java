package com.clougence.clouddm.sdk.model.analysis.resource;

import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.AuthElementType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class AuthBrowseObject {

    private long                objId;
    private String              objName;
    private String              objDesc;
    private AuthElementType     objType;
    private Map<String, Object> objAttr;
    private boolean             leaf;

    @Override
    public String toString() {
        return "AuthBrowseObject{" + "objId=" + objId + ", objName='" + objName + '\'' + ", objDesc='" + objDesc + '\'' + ", objType=" + objType + ", objAttr=" + objAttr
               + ", leaf=" + leaf + '}';
    }
}
