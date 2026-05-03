package com.clougence.clouddm.sdk.service.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2021/1/5
 **/
@Getter
@Setter
public class RoleData {

    private long         roleId;
    private String       roleName;
    private String       aliasName;
    private boolean      innerTag;
    private List<String> labels;
}
