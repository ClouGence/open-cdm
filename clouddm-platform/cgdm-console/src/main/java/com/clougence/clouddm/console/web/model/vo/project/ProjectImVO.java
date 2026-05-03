package com.clougence.clouddm.console.web.model.vo.project;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectImVO {

    private long    imId;
    private String  imDisplay;
    private ImType  imType;
    private String  imTypeI18n;
    private boolean enable;
    private String  enableMessage;
}
