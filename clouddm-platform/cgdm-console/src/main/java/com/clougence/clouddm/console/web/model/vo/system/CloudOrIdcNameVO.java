package com.clougence.clouddm.console.web.model.vo.system;

import com.clougence.clouddm.console.web.constants.CloudOrIdcName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/5/9 09:39:03
 */
@Getter
@Setter
public class CloudOrIdcNameVO {

    private CloudOrIdcName cloudOrIdcName;

    private String         i18nName;

    private boolean        defaultCheck;

}
