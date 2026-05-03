package com.clougence.clouddm.console.web.model.vo.project;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectImConfigVO {

    private long    imConfigId;
    private long    imId;
    private ImType  imType;
    private String  imTypeI18n;
    private boolean enable;
    private String  name;
    private String  language;

    private boolean eventProjectStatus;
    private boolean eventProjectConfig;
    private boolean eventChangeLife;
    private boolean eventChangeNotice;
}
