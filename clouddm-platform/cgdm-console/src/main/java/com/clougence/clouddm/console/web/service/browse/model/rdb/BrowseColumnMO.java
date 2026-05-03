package com.clougence.clouddm.console.web.service.browse.model.rdb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseColumnMO implements BrowseTips {

    private String           name;
    private String           dbType;
    private String           tips;
    private BrowseColumnType dataType;

    private boolean          dbKey;
    private boolean          dbUnique;
    private boolean          dbIndex;
    private boolean          dbForeign;
}
