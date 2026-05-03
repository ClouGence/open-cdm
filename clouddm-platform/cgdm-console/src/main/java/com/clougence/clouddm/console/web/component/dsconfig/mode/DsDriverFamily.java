package com.clougence.clouddm.console.web.component.dsconfig.mode;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsDriverFamily {

    private String       name;
    private List<String> versions;

    @Override
    public DsDriverFamily clone() {
        DsDriverFamily f = new DsDriverFamily();
        f.setName(this.name);
        f.setVersions(new ArrayList<>(this.versions));
        return f;
    }
}
