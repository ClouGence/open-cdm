package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowsePrimaryMO {

    private String             name;
    private String             type;
    private String             tips;
    private List<BrowseTermMO> columns;
}
