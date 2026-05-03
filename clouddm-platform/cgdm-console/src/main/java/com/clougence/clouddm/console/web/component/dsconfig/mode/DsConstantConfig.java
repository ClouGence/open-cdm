package com.clougence.clouddm.console.web.component.dsconfig.mode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.sdk.ui.browser.DsCaseType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsConstantConfig {

    private Map<String, String> quickQueryMap;
    private DsCaseType          caseType;
    private String              leftQualifier;
    private String              rightQualifier;

    public DsConstantConfig clone() {
        DsConstantConfig r = new DsConstantConfig();
        r.setQuickQueryMap(this.quickQueryMap.isEmpty() ? Collections.emptyMap() : new HashMap<>(this.quickQueryMap));
        r.setCaseType(this.caseType);
        r.setLeftQualifier(this.leftQualifier);
        r.setRightQualifier(this.rightQualifier);
        return r;
    }
}
