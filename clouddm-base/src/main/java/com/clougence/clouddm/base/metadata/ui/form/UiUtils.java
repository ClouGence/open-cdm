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
