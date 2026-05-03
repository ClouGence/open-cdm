package com.clougence.clouddm.console.web.service.project.domain;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmImDef {

    private ImType imType;
    private String imTypeI18n;
    private String helpUrl;
}
