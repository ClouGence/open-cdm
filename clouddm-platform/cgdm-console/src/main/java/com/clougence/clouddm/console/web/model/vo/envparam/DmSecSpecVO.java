package com.clougence.clouddm.console.web.model.vo.envparam;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-06-03 17:31
 */
@Getter
@Setter
@Builder
public class DmSecSpecVO {

    private long    id;
    private String  specName;
    private String  specDesc;
    private boolean enable;
}
