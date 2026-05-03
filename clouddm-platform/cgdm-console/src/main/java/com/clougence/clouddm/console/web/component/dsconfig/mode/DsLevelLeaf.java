package com.clougence.clouddm.console.web.component.dsconfig.mode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsLevelLeaf {

    private String type;
    private String i18n;

    public DsLevelLeaf clone() {
        DsLevelLeaf r = new DsLevelLeaf();
        r.setType(this.type);
        r.setI18n(this.i18n);
        return r;
    }
}
