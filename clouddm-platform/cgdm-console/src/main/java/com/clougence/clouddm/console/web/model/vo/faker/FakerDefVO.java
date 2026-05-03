package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakerDefVO {

    //    private List<String>              order;
    private Map<String, FakerPanelVO> uiPanels;
}
