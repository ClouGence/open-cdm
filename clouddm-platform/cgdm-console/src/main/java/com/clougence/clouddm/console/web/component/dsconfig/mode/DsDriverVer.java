package com.clougence.clouddm.console.web.component.dsconfig.mode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsDriverVer {

    private String version;
    private String dsFactory;

    @Override
    public DsDriverVer clone() {
        DsDriverVer f = new DsDriverVer();
        f.setVersion(this.version);
        f.setDsFactory(this.dsFactory);
        return f;
    }
}
