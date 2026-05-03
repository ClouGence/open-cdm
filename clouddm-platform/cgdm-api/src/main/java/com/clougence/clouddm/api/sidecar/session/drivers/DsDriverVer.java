package com.clougence.clouddm.api.sidecar.session.drivers;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsDriverVer {

    private String            version;
    private String            dsFactory;
    private List<DsDriverRes> resources;
}
