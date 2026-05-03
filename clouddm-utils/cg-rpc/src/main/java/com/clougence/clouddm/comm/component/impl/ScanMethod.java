package com.clougence.clouddm.comm.component.impl;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScanMethod {

    private String   scanMethodKey;
    private Method   scanMethod;
    private Class<?> scanTarget;
}
