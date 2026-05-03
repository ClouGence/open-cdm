package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
public class FakerPreviewVO {

    private String                           sessionId;
    private Map<String, List<FakerColumnVO>> data;

}
