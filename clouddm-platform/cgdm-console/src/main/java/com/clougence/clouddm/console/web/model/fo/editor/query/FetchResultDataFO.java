package com.clougence.clouddm.console.web.model.fo.editor.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/16 16:36
 */
@Getter
@Setter
public class FetchResultDataFO {

    private String resultId;
    private long   rowNumber;
    private int    colNumber;

    private long   offset;
    private int    fetchSize;
}
