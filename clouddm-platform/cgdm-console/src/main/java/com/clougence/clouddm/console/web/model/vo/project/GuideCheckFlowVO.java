package com.clougence.clouddm.console.web.model.vo.project;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideCheckFlowVO {

    private boolean                          success;
    private String                           message;
    private List<GuideCheckFlowRefProjectVO> referer;
}
