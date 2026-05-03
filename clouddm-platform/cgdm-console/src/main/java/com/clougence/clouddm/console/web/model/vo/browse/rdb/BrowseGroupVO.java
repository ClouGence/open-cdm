package com.clougence.clouddm.console.web.model.vo.browse.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseGroupVO {

    private String             name;
    private String             type;
    private List<BrowseItemVO> items;
}
