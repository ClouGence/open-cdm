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
package com.clougence.clouddm.base.metadata.ui.form;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ui.form.value.*;

public class UiUtils {

    public static ValueDef strValueDef(String value) {
        return ObjectValueDef.builder().value(value).build();
    }

    public static ValueDef boolValueDef(Boolean value) {
        return ObjectValueDef.builder().value(value).build();
    }

    public static ValueDef ListValueDef(List<Object> value) {
        return ObjectValueDef.builder().value(value).build();
    }

    public static ValueDef numValueDef(Number value) {
        return ObjectValueDef.builder().value(value).build();
    }

    public static ValueDef kvValueDef(String key, String value) {
        Map<String, Object> valMap = new LinkedHashMap<>();
        valMap.put("key", key);
        valMap.put("value", value);
        return MapValueDef.builder().data(valMap).build();
    }

    public static ValueDef optionDef(String labelI18N, String value) {
        return OptionValueDef.builder().labelI18N(labelI18N).value(value).build();
    }

    public static FieldOptionValueDef fieldOptionDef(String labelI18N, String value) {
        return FieldOptionValueDef.builder().labelI18N(labelI18N).value(value).build();
    }

    public static FieldOptionValueDef fieldOptionDef(String labelI18N, String value, boolean readOnly) {
        return FieldOptionValueDef.builder().labelI18N(labelI18N).value(value).readOnly(readOnly).build();
    }

    //    public static FieldOptionValueDef fieldOptionDef(String labelI18N, boolean value) {
    //        return FieldOptionValueDef.builder().labelI18N(labelI18N).value(value).build();
    //    }
}
