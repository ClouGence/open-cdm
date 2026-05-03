package com.clougence.rdp.service.model;

import com.clougence.rdp.constant.UserConfigTagType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2024/7/18
 */
@Getter
@Setter
public class UserConfigMO {

    private String            config;
    private String            newValue;
    private String            oldValue;
    private String            defaultValue;
    private UserConfigTagType tagType;
    private boolean           insert;
    private boolean           update;
    private boolean           delete;
}
