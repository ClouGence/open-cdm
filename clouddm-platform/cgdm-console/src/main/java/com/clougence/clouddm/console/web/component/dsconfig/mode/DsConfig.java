package com.clougence.clouddm.console.web.component.dsconfig.mode;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DsClassify;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsConfig {

    private DsClassify                classify;
    private Map<String, Object>       features;
    private DsConstantConfig          constant;
    private DsCategories              categories;
    private Map<String, List<DsMenu>> menus;
    private List<String>              targetDsList;
    private List<String>              ddlList;
    private List<DsIsolation>         isolations;
    private List<DsDriverFamily>      driverFamilies;

    public DsConfig clone() {
        DsConfig dsConfig = new DsConfig();
        dsConfig.setClassify(this.classify);
        dsConfig.setFeatures(this.features.isEmpty() ? Collections.emptyMap() : new HashMap<>(this.features));
        dsConfig.setConstant(this.constant.clone());
        dsConfig.setCategories(this.categories.clone());
        if (this.menus.isEmpty()) {
            dsConfig.setMenus(Collections.emptyMap());
        } else {
            Map<String, List<DsMenu>> menuMap = new HashMap<>();
            this.menus.forEach((k, v) -> {
                menuMap.put(k, v.isEmpty() ? Collections.emptyList() : v.stream().map(DsMenu::clone).collect(Collectors.toList()));
            });
            dsConfig.setMenus(menuMap);
        }
        dsConfig.setTargetDsList(this.targetDsList.isEmpty() ? Collections.emptyList() : new ArrayList<>(this.targetDsList));
        dsConfig.setDdlList(this.ddlList.isEmpty() ? Collections.emptyList() : new ArrayList<>(this.ddlList));
        dsConfig.setIsolations(this.isolations.isEmpty() ? Collections.emptyList() : this.isolations.stream().map(DsIsolation::clone).collect(Collectors.toList()));
        dsConfig.setDriverFamilies(this.driverFamilies.isEmpty() ? Collections.emptyList() : this.driverFamilies.stream().map(DsDriverFamily::clone).collect(Collectors.toList()));
        return dsConfig;
    }
}
