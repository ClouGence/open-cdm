package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseIndexMO implements BrowseTips {

    private String             name;
    private String             type;
    private String             tips;
    private List<BrowseTermMO> columns;

    private boolean            unique;
    private boolean            foreign;
}
