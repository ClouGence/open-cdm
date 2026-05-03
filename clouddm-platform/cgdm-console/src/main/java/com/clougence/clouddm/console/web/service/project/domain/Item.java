package com.clougence.clouddm.console.web.service.project.domain;

import com.clougence.clouddm.console.web.dal.model.DmProjectChangeItemDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectDevopsItemDO;

import lombok.Getter;

@Getter
public class Item {

    private final String                name;
    private final int                   index;
    private final DmProjectDevopsItemDO devopsItem;
    private final DmProjectChangeItemDO changeItem;

    public Item(DmProjectDevopsItemDO i){
        this.name = i.getContentName();
        this.index = i.getContentIndex();
        this.devopsItem = i;
        this.changeItem = null;
    }

    public Item(DmProjectChangeItemDO i){
        this.name = i.getContentName();
        this.index = i.getContentIndex();
        this.devopsItem = null;
        this.changeItem = i;
    }
}
