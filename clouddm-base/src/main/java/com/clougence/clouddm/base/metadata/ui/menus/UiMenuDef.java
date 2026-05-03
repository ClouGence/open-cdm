package com.clougence.clouddm.base.metadata.ui.menus;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;

/**
 * @author mode 2020/11/7 14:27
 */
public class UiMenuDef implements DsFeatureIDs {

    public final static List<String> DEFAULT_EMPTY = Arrays.asList(//
            MENU_BROWSE_COPY_NAME,          // Copy
            MENU_BROWSE_REFRESH,            // Refresh
            MENU_BROWSE_PERMISSIONS         // Permissions
    );

    public final static List<String> DEFAULT_ENV   = Arrays.asList(//
            MENU_BROWSE_INSTANCE_CREATE,    // add DataSource
            MENU_SEPARATOR,                 // --- separator ---
            MENU_BROWSE_COPY_NAME,          // Copy
            MENU_BROWSE_REFRESH,            // Refresh
            MENU_BROWSE_PERMISSIONS         // Permissions
    );
}
