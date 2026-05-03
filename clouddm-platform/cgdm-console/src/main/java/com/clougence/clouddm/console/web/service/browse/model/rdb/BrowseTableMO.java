package com.clougence.clouddm.console.web.service.browse.model.rdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseTableMO extends BrowseObjectMO implements BrowseTips {

    private List<BrowseColumnMO>     columns;
    private List<BrowsePrimaryMO>    keys;
    private List<BrowseIndexMO>      indexes;
    private List<BrowsePartitionMO>  partitions;
    private List<BrowseConstraintMO> constraints;
    private List<BrowseForeignKeyMO> foreignKeys;
}
