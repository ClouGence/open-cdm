package com.clougence.rdp.controller.model.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/12 20:04
 */
@Getter
@Setter
public class DsAuthKindGroupVO {

    private String             group;

    private String             groupI18n;

    private List<DsAuthKindVO> kinds = new ArrayList<>();

}
