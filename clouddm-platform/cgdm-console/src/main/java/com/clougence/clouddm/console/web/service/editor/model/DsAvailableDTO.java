package com.clougence.clouddm.console.web.service.editor.model;

import com.clougence.clouddm.console.web.dal.enumeration.DataSourceStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsAvailableDTO {

    private long             dsId;
    private DataSourceStatus dsStatus;
    private String           dsStatusMessage;
}
