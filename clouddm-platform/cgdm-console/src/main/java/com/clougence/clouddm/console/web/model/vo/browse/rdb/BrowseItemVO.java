package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseItemVO {

    private String              name;
    private String              type;
    private String              icon;
    private String              dbType;
    private String              tips;
    private Map<String, Object> attrs;
}
