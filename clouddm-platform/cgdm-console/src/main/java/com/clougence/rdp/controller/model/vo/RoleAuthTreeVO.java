package com.clougence.rdp.controller.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/12 18:36
 */
@Getter
@Setter
public class RoleAuthTreeVO {

    private String               key;

    private String               i18nName;

    private boolean              mustSelectAndReadOnly;

    private boolean              category;

    @JsonIgnore
    public String                parent;

    private List<RoleAuthTreeVO> children = new ArrayList<>();
}
