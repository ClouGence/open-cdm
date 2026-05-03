package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseForeignKeyMO {

    private String             name;
    private boolean            enabled = true;
    private String             tips;
    private List<BrowseTermMO> columns;
}
