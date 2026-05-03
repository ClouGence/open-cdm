package com.clougence.clouddm.sdk.analysis.column;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealColumn {

    private String  column;
    private String  table;
    private String  schema;
    private String  catalog;

    private int     tableId;
    private boolean onlyOneColumn = true;

    private boolean skipDesensitization;

    public String toDsResPath() {
        StringBuilder resPathLike = new StringBuilder();
        if (StringUtils.isNotBlank(this.catalog)) {
            resPathLike.append("/").append(this.catalog);
        }
        if (StringUtils.isNotBlank(this.schema)) {
            resPathLike.append("/").append(this.schema);
        }
        if (StringUtils.isNotBlank(this.table)) {
            resPathLike.append("/").append(this.table);
        }
        if (StringUtils.isNotBlank(this.column)) {
            resPathLike.append("/").append(this.column);
        }

        resPathLike.append("/");
        return resPathLike.toString();
    }
}
