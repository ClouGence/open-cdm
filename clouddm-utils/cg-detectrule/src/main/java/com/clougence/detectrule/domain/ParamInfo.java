package com.clougence.detectrule.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * param
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class ParamInfo {

    private String       name;
    private String       type;
    private String       defaultValue;
    private List<String> enums;
    private String       hint;
}
