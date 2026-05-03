package com.clougence.schema.editor.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/5/9 17:56:00
 */
@Getter
@Setter
public class ETableLike {

    private String catalog;

    private String schema;

    private String table;

    public ETableLike cloneLike() {
        ETableLike l = new ETableLike();
        l.setCatalog(this.catalog);
        l.setSchema(this.schema);
        l.setTable(this.table);
        return l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ETableLike like = (ETableLike) o;

        if (catalog != null ? !catalog.equals(like.catalog) : like.catalog != null)
            return false;
        if (schema != null ? !schema.equals(like.schema) : like.schema != null)
            return false;
        return table.equals(like.table);
    }

    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + table.hashCode();
        return result;
    }
}
