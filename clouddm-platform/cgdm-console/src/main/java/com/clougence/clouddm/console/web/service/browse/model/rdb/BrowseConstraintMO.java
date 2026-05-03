package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseConstraintMO {

    private String               name;
    private BrowseConstraintType type;
    private boolean              enabled = true;
    private String               tips;
    private List<BrowseTermMO>   columns;
}
