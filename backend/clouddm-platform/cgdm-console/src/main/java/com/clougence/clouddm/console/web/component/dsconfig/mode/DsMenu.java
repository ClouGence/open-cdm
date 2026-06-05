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
package com.clougence.clouddm.console.web.component.dsconfig.mode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/18 17:37
 */
@Getter
@Setter
public class DsMenu {

    private String  menuId;
    private String  i18n;
    private boolean needTarget;
    //private int    shortcutKey;

    @Override
    public DsMenu clone() {
        DsMenu r = new DsMenu();
        r.setMenuId(this.menuId);
        r.setI18n(this.i18n);
        r.setNeedTarget(this.needTarget);
        return r;
    }
}
