package com.clougence.clouddm.console.web.model.vo.checkrules;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/8 15:01
 */
@Getter
@Setter
public class SpecUpdateVO {

    private boolean        success;
    private String         message;
    private List<RefEnvVO> referer;
}
