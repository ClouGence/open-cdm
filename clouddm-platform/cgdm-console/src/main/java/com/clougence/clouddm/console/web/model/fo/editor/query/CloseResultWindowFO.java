package com.clougence.clouddm.console.web.model.fo.editor.query;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/16 16:36
 */
@Getter
@Setter
public class CloseResultWindowFO {

    private String       sessionId;
    private List<String> resultIds;
}
