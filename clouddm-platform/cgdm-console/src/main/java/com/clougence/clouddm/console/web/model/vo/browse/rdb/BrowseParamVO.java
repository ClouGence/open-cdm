package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseParamVO {

    private String  name;
    private String  mode;
    private String  paramType;

    private Long    length;
    private Integer numericPrecision;
    private Integer numericScale;
    private Integer datetimePrecision;

}
