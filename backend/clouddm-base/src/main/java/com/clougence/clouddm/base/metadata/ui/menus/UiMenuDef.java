/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
