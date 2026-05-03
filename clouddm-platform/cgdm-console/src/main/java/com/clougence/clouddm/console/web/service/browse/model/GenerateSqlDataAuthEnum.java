package com.clougence.clouddm.console.web.service.browse.model;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public enum GenerateSqlDataAuthEnum {

    MENU_BROWSE_CATALOG_CREATE(DsFeatureIDs.MENU_BROWSE_CATALOG_CREATE, SecDataAuthLabel.DM_DAUTH_SPACE, false),
    MENU_BROWSE_CATALOG_DROP(DsFeatureIDs.MENU_BROWSE_CATALOG_DROP, SecDataAuthLabel.DM_DAUTH_SPACE, true),
    MENU_BROWSE_CATALOG_RENAME(DsFeatureIDs.MENU_BROWSE_CATALOG_RENAME, SecDataAuthLabel.DM_DAUTH_SPACE, true),
    MENU_BROWSE_SCHEMA_CREATE(DsFeatureIDs.MENU_BROWSE_SCHEMA_CREATE, SecDataAuthLabel.DM_DAUTH_SPACE, false),
    MENU_BROWSE_SCHEMA_DROP(DsFeatureIDs.MENU_BROWSE_SCHEMA_DROP, SecDataAuthLabel.DM_DAUTH_SPACE, true),
    MENU_BROWSE_SCHEMA_RENAME(DsFeatureIDs.MENU_BROWSE_SCHEMA_RENAME, SecDataAuthLabel.DM_DAUTH_SPACE, true),
    //
    MENU_BROWSE_TABLE_DROP(DsFeatureIDs.MENU_BROWSE_TABLE_DROP, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_TABLE_RENAME(DsFeatureIDs.MENU_BROWSE_TABLE_RENAME, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_TABLE_TRUNCATE(DsFeatureIDs.MENU_BROWSE_TABLE_TRUNCATE, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_COLUMN_DROP(DsFeatureIDs.MENU_BROWSE_COLUMN_DROP, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_COLUMN_RENAME(DsFeatureIDs.MENU_BROWSE_COLUMN_RENAME, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_PRIMARY_DROP(DsFeatureIDs.MENU_BROWSE_PRIMARY_DROP, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_PRIMARY_RENAME(DsFeatureIDs.MENU_BROWSE_PRIMARY_RENAME, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_INDEX_DROP(DsFeatureIDs.MENU_BROWSE_INDEX_DROP, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_INDEX_RENAME(DsFeatureIDs.MENU_BROWSE_INDEX_RENAME, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_PARTITION_DROP(DsFeatureIDs.MENU_BROWSE_PARTITION_DROP, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_KEY_DROP(DsFeatureIDs.MENU_BROWSE_KEY_DROP, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_KEY_RENAME(DsFeatureIDs.MENU_BROWSE_KEY_RENAME, SecDataAuthLabel.DM_DAUTH_DDL, true),
    MENU_BROWSE_CONSTRAINT_ENABLE(DsFeatureIDs.MENU_BROWSE_CONSTRAINT_ENABLE, SecDataAuthLabel.DM_DAUTH_DDL, false),
    MENU_BROWSE_CONSTRAINT_DISABLE(DsFeatureIDs.MENU_BROWSE_CONSTRAINT_DISABLE, SecDataAuthLabel.DM_DAUTH_DDL, false),
    //
    MENU_BROWSE_VIEW_DROP(DsFeatureIDs.MENU_BROWSE_VIEW_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_VIEW_RENAME(DsFeatureIDs.MENU_BROWSE_VIEW_RENAME, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_VIEW_CREATE(DsFeatureIDs.MENU_BROWSE_VIEW_CREATE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_VIEW_ALTER(DsFeatureIDs.MENU_BROWSE_VIEW_ALTER, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_VIEW_COMPILE(DsFeatureIDs.MENU_BROWSE_VIEW_COMPILE, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_TRIGGER_CREATE(DsFeatureIDs.MENU_BROWSE_TRIGGER_CREATE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_TRIGGER_DROP(DsFeatureIDs.MENU_BROWSE_TRIGGER_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_PROCEDURE_DROP(DsFeatureIDs.MENU_BROWSE_PROCEDURE_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_FUNCTION_DROP(DsFeatureIDs.MENU_BROWSE_FUNCTION_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_FUNCTION_ALTER(DsFeatureIDs.MENU_BROWSE_FUNCTION_ALTER, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_FUNCTION_CREATE(DsFeatureIDs.MENU_BROWSE_FUNCTION_CREATE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_FUNCTION_COMPILE(DsFeatureIDs.MENU_BROWSE_FUNCTION_COMPILE, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_MATERIALIZED_DROP(DsFeatureIDs.MENU_BROWSE_MATERIALIZED_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_SYNONYM_DROP(DsFeatureIDs.MENU_BROWSE_SYNONYM_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_SEQUENCE_DROP(DsFeatureIDs.MENU_BROWSE_SEQUENCE_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_TRIGGER_ALTER(DsFeatureIDs.MENU_BROWSE_TRIGGER_ALTER, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_TRIGGER_COMPILE(DsFeatureIDs.MENU_BROWSE_TRIGGER_COMPILE, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_PROCEDURE_CREATE(DsFeatureIDs.MENU_BROWSE_PROCEDURE_CREATE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_PROCEDURE_ALTER(DsFeatureIDs.MENU_BROWSE_PROCEDURE_ALTER, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_PROCEDURE_COMPILE(DsFeatureIDs.MENU_BROWSE_PROCEDURE_COMPILE, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_DBLINK_CREATE(DsFeatureIDs.MENU_BROWSE_DBLINK_CREATE, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_DBLINK_DROP(DsFeatureIDs.MENU_BROWSE_DBLINK_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_DBLINK_TEST(DsFeatureIDs.MENU_BROWSE_DBLINK_TEST, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_JOB_CREATE(DsFeatureIDs.MENU_BROWSE_JOB_CREATE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_JOB_ALTER(DsFeatureIDs.MENU_BROWSE_JOB_ALTER, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_JOB_DROP(DsFeatureIDs.MENU_BROWSE_JOB_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_JOB_DISABLE(DsFeatureIDs.MENU_BROWSE_JOB_DISABLE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_JOB_ENABLE(DsFeatureIDs.MENU_BROWSE_JOB_ENABLE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_JOB_RUN(DsFeatureIDs.MENU_BROWSE_JOB_RUN, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_SCHEDULE_DROP(DsFeatureIDs.MENU_BROWSE_SCHEDULE_DROP, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_SCHEDULE_ENABLE(DsFeatureIDs.MENU_BROWSE_SCHEDULE_ENABLE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_SCHEDULE_DISABLE(DsFeatureIDs.MENU_BROWSE_SCHEDULE_DISABLE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    MENU_BROWSE_SCHEDULE_RUN(DsFeatureIDs.MENU_BROWSE_SCHEDULE_RUN, SecDataAuthLabel.DM_DAUTH_OBJ, true),
    MENU_BROWSE_SCHEDULE_CREATE(DsFeatureIDs.MENU_BROWSE_SCHEDULE_CREATE, SecDataAuthLabel.DM_DAUTH_OBJ, false),
    //
    MENU_BROWSE_USER_DROP(DsFeatureIDs.MENU_BROWSE_USER_DROP, SecDataAuthLabel.DM_DAUTH_DCL, true),;

    private final String  featureID;
    private final String  dataAuth;
    private final boolean danger;

    GenerateSqlDataAuthEnum(String featureID, String dataAuth, boolean danger){
        this.featureID = featureID;
        this.dataAuth = dataAuth;
        this.danger = danger;
    }

    public static GenerateSqlDataAuthEnum valueOfCode(String eventType) {
        for (GenerateSqlDataAuthEnum feature : GenerateSqlDataAuthEnum.values()) {
            if (StringUtils.equals(eventType, feature.featureID)) {
                return feature;
            }
        }
        return null;
    }

    public boolean isDanger() { return this.danger; }
}
