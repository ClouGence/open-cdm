package com.clougence.clouddm.console.web.component.dsconfig.mode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsMenu {

    private String  menuId;
    private String  i18n;
    private boolean needTarget;
    //private int    shortcutKey;

    public DsMenu clone() {
        DsMenu r = new DsMenu();
        r.setMenuId(this.menuId);
        r.setI18n(this.i18n);
        r.setNeedTarget(this.needTarget);
        return r;
    }
}
