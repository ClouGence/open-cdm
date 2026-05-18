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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.util.Collections;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DsMenuUtilsTest {

    @Test
    void shouldCacheMenuLabelsPerLocale() {
        LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
        DsMenu zhMenu = DsMenuUtils.generationDsMenus(Collections.singletonList(DsFeatureIDs.MENU_BROWSE_TABLE_CREATE)).get(0);

        LocaleContextHolder.setLocale(Locale.US);
        DsMenu enMenu = DsMenuUtils.generationDsMenus(Collections.singletonList(DsFeatureIDs.MENU_BROWSE_TABLE_CREATE)).get(0);

        assertEquals("新建表", zhMenu.getI18n());
        assertEquals("Create Table", enMenu.getI18n());
    }
}
