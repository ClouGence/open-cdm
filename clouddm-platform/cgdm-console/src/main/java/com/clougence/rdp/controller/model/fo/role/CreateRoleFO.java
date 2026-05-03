package com.clougence.rdp.controller.model.fo.role;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 15:13
 */
@Getter
@Setter
public class CreateRoleFO {

    private String       roleName;

    private List<String> authLabelList;
}
