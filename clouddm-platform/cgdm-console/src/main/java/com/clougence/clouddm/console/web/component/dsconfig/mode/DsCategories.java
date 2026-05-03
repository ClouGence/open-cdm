package com.clougence.clouddm.console.web.component.dsconfig.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsCategories {

    private List<String>                   levels;
    private List<String>                   leafExpand;
    private Map<String, List<DsLevelLeaf>> leafGroup;

    public DsCategories clone() {
        DsCategories r = new DsCategories();
        r.setLevels(this.levels.isEmpty() ? Collections.emptyList() : new ArrayList<>(this.levels));
        r.setLeafExpand(this.leafExpand.isEmpty() ? Collections.emptyList() : new ArrayList<>(this.leafExpand));
        r.setLeafGroup(this.leafGroup.isEmpty() ? Collections.emptyMap() : this.leafGroup.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> {
            List<DsLevelLeaf> l = new ArrayList<>();
            for (DsLevelLeaf levelLeaf : e.getValue()) {
                l.add(levelLeaf.clone());
            }
            return l;
        })));
        return r;
    }
}
