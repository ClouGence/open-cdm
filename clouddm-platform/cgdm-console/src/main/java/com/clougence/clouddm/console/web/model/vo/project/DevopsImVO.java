package com.clougence.clouddm.console.web.model.vo.project;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevopsImVO {

    private long    imId;
    private ImType  imType;
    private String  imTypeI18n;
    private String  display;
    private String  webhookUrl;
    private boolean enable;
    private String  enableMessage;
}
