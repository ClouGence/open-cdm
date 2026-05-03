package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideImFO {

    private long    imId;
    private String  language;
    private boolean eventProjectStatus;
    private boolean eventProjectConfig;
    private boolean eventChangeLife;
    private boolean eventChangeNotice;
}
