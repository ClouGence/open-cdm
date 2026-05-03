package com.clougence.clouddm.sdk.service.execute;

import lombok.Getter;
import lombok.Setter;

/**
 * The final return fields
 * such as
 * select id from a union select id from b
 * SelectColumn : {
 *     columnAlias: id,
 *     columns[ a.id, b.id]
 * }
 */
@Getter
@Setter
public class MetaCol {

    private String column;
    private String table;
    private String schema;
    private String catalog;
}
