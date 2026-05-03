package com.clougence.clouddm.console.web.model.fo.editor.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/16 16:36
 */
@Getter
@Setter
public class FetchResultPageFO {

    private String resultId;

    private long   offsetRow;
    private int    pageSize;
}
