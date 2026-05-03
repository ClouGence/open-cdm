package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevopsImAddFO {

    private Long   imId;
    private ImType imType;
    private String display;
    private String webhookUrl;
    private String secret;
}
