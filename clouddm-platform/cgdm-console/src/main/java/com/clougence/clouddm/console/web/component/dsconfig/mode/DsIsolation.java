package com.clougence.clouddm.console.web.component.dsconfig.mode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsIsolation {

    private String key;
    private String i18n;

    public DsIsolation(){
    }

    public DsIsolation(String key, String i18n){
        this.key = key;
        this.i18n = i18n;
    }

    public DsIsolation clone() {
        DsIsolation r = new DsIsolation();
        r.setKey(this.key);
        r.setI18n(this.i18n);
        return r;
    }
}
