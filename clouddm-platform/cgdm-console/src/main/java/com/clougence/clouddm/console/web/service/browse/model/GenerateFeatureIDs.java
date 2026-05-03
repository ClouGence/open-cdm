package com.clougence.clouddm.console.web.service.browse.model;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public enum GenerateFeatureIDs {

    MENU_BROWSE_CATALOG_CREATE(DsFeatureIDs.MENU_BROWSE_CATALOG_CREATE, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_CATALOG_DROP(DsFeatureIDs.MENU_BROWSE_CATALOG_DROP, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_CATALOG_RENAME(DsFeatureIDs.MENU_BROWSE_CATALOG_RENAME, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_SCHEMA_CREATE(DsFeatureIDs.MENU_BROWSE_SCHEMA_CREATE, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_SCHEMA_DROP(DsFeatureIDs.MENU_BROWSE_SCHEMA_DROP, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_SCHEMA_RENAME(DsFeatureIDs.MENU_BROWSE_SCHEMA_RENAME, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_TABLE_DROP(DsFeatureIDs.MENU_BROWSE_TABLE_DROP, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_TABLE_RENAME(DsFeatureIDs.MENU_BROWSE_TABLE_RENAME, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_TABLE_TRUNCATE(DsFeatureIDs.MENU_BROWSE_TABLE_TRUNCATE, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_VIEW_DROP(DsFeatureIDs.MENU_BROWSE_VIEW_DROP, SecDataAuthLabel.DM_DAUTH_DDL),
    MENU_BROWSE_VIEW_RENAME(DsFeatureIDs.MENU_BROWSE_VIEW_RENAME, SecDataAuthLabel.DM_DAUTH_DDL),;

    private final String featureID;
    private final String dsAuthKind;

    GenerateFeatureIDs(String featureID, String dsAuthKind){
        this.featureID = featureID;
        this.dsAuthKind = dsAuthKind;
    }

    public static GenerateFeatureIDs valueOfCode(String eventType) {
        for (GenerateFeatureIDs feature : GenerateFeatureIDs.values()) {
            if (StringUtils.equals(eventType, feature.featureID)) {
                return feature;
            }
        }
        return null;
    }
}
