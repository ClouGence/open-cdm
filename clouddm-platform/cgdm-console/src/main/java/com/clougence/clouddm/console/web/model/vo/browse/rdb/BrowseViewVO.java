package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseViewVO extends BrowseObjectVO {

    private String              sql;

    private Map<String, Object> features = new HashMap<>();
}
