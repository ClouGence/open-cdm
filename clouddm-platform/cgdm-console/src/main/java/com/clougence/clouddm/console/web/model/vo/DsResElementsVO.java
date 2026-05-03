package com.clougence.clouddm.console.web.model.vo;

import com.clougence.clouddm.console.web.constants.DsResElement;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/18 12:27
 */
@Getter
@Setter
public class DsResElementsVO {

    private String       i18nName;

    private String       paramName;

    private DsResElement element;
}
