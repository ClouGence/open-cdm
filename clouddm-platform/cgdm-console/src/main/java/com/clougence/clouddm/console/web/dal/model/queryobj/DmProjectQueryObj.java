package com.clougence.clouddm.console.web.dal.model.queryobj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/** 
 * @author mode
 * @date 2024/5/10 13:46 */
@Getter
@Setter
@Builder
public class DmProjectQueryObj {

    private String searchKeywords;
    private String mark;
    private String status;
}
