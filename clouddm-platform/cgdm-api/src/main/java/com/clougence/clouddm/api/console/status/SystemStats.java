package com.clougence.clouddm.api.console.status;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/1/22
 **/
@Getter
@Setter
public class SystemStats {

    /**
     * os name
     */
    private String osName;

    /**
     * os architecture
     */
    private String osArch;
}
