package com.clougence.clouddm.console.web.model.vo.checkrules;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
public class SpecVO {

    private long    specId;
    private String  lastModified;
    private String  name;
    private String  description;
    private boolean enable;
}
