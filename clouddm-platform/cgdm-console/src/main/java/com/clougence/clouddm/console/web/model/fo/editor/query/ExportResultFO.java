package com.clougence.clouddm.console.web.model.fo.editor.query;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/16 16:36
 */
@Getter
@Setter
public class ExportResultFO {

    private String              resultId;

    private String              dstFileName;
    private String              dstFormatName;

    private Map<String, Object> option;

    // private boolean compress;
    // private String  compressFormat;
}
