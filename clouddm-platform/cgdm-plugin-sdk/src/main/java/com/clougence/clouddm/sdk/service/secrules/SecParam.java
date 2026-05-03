package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
public class SecParam {

    private String       name;
    private String       type;
    private String       defaultValue;
    private List<String> range;
    private String       hint;
}
