package com.clougence.clouddm.sdk.ui.editor.table;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-07-12 14:45
 */
@Getter
@Setter
public class TableEditorUiData implements Serializable, TableEditorFields {

    private static final long         serialVersionUID = -1810374665700501556L;

    @JsonInclude()
    private Map<String, Object>       tableInfo        = new HashMap<>();

    @JsonInclude()
    private List<Map<String, Object>> columns          = new ArrayList<>();

    @JsonInclude()
    private Map<String, Object>       keys             = null;

    @JsonInclude()
    private List<Map<String, Object>> indexes          = new ArrayList<>();

    @JsonInclude()
    private Map<String, Object>       partitions       = null;

    @JsonInclude()
    private List<Map<String, Object>> constraints      = new ArrayList<>();
    @JsonInclude()
    private List<Map<String, Object>> foreignKeys      = new ArrayList<>();

    public Map<String, Object> findColumn(String name) {
        if (this.columns != null) {
            for (Map<String, Object> column : this.columns) {
                if (Objects.equals(name, column.get(MODE_COLUMN_NAME))) {
                    return column;
                }
            }
        }
        return null;
    }

    public Map<String, Object> findIndex(String name) {
        if (this.indexes != null) {
            for (Map<String, Object> column : this.indexes) {
                if (Objects.equals(name, column.get(MODE_INDEX_NAME))) {
                    return column;
                }
            }
        }
        return null;
    }
}
