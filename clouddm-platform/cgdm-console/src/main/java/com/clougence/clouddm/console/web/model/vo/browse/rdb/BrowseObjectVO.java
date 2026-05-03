package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseObjectVO {

    private String              objId;
    private String              name;
    private String              type;
    private String              tips;

    private List<BrowseGroupVO> group;
}
