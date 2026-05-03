package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseProcedureVO extends BrowseObjectVO {

    private String              sql;

    private List<BrowseParamVO> params;
    private Map<String, Object> features;

}
