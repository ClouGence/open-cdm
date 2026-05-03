package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseFunctionVO extends BrowseObjectVO {

    private String              sql;

    private String              mode;
    private String              returnType;

    private Long                length;
    private Integer             numericPrecision;
    private Integer             numericScale;
    private Integer             datetimePrecision;

    private Map<String, Object> features;
    private List<BrowseParamVO> params;

    //    private Map<String, Object> baseInfo;
    //    private Map<String, Object> returnType;
    //    private List<?>             params;
    //    private String              sql;
    //    private Map<String, Object> features;

}
