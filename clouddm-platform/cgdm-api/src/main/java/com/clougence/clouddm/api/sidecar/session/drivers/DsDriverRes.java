package com.clougence.clouddm.api.sidecar.session.drivers;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsDriverRes {

    private String  name;
    private boolean prepared;
    private String  type;

    public static DsDriverRes of(String type, String name, boolean prepared) {
        DsDriverRes res = new DsDriverRes();
        res.type = type;
        res.name = name;
        res.prepared = prepared;
        return res;
    }
}
