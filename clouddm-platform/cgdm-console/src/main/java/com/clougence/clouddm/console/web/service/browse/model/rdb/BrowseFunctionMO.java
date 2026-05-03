package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseFunctionMO extends BrowseObjectMO {

    private List<BrowseParamMO> params;
    private BrowseParamMO       returns;

}
