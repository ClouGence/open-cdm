package com.clougence.clouddm.comm.model;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Getter
@Setter
public class RSocketApiWrapper {

    private Object   apiObj;
    private String   apiMethodName;
    private Method   apiMethod;
    private Class<?> apiType;

}
