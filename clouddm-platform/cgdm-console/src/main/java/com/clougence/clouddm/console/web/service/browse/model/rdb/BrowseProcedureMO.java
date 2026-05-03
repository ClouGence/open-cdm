package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseProcedureMO extends BrowseObjectMO {

    private List<BrowseParamMO> params;

}
