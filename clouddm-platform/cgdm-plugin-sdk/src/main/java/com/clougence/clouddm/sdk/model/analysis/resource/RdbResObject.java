package com.clougence.clouddm.sdk.model.analysis.resource;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbResObject extends ResObject {

    private String catalog;
    private String schema;
    private String table;

    public RdbResObject(){
    }

    public RdbResObject(String catalog, String schema, String table){
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    @Override
    public String getName() {
        if (this.getType() != null) {
            switch (this.getType()) {
                case Catalog:
                    return this.catalog;
                case Schema:
                    return this.schema;
                case Table:
                case View:
                    return this.table;
                case ConfigKey:
                    return this.name;
                case Column:
                case Index:
                case Constraint:
                case Sequence:
                case Function:
                case Procedure:
                case Trigger:
                case Synonym:
                default:
                    break;
            }
        }

        return super.getName();
    }

    public boolean hasCatalog() {
        return StringUtils.isNotBlank(this.catalog);
    }

    public boolean hasSchema() {
        return StringUtils.isNotBlank(this.schema);
    }

    public boolean hasTable() {
        return StringUtils.isNotBlank(this.table);
    }

    @Override
    public DsResPath toDsResPath() {
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
        if (StringUtils.isNotBlank(this.name)) {
            resPathLike.append("/").append(this.name);
        }

        resPathLike.append("/");
        return new DsResPathObj(resPathLike.toString());
    }
}
