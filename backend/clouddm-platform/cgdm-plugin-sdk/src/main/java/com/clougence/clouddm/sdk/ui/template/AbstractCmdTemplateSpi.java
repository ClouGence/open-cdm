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
package com.clougence.clouddm.sdk.ui.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.umi.struts.UmiAttributeNames;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractCmdTemplateSpi implements CmdTemplateSpi {

    protected String fmtName(CmdTemplateOption option, String name) {
        return getDialect().fmtName(option.isDelimited(), name);
    }

    protected EColumn parseColumn(Param param) {
        EColumn eColumn = new EColumn();
        eColumn.setDbType(param.getParamType());
        eColumn.setNumericScale(param.getNumericScale());
        eColumn.setLength(param.getLength());
        eColumn.setNumericPrecision(param.getNumericPrecision());
        eColumn.setDatetimePrecision(param.getDatetimePrecision());
        eColumn.setDefaultValue(param.getDefaultValue());

        return eColumn;
    }

    protected boolean isEmpty(CmdTemplateOption option, UmiAttributeNames name) {
        if (option.getData().get(name.getCodeKey()) == null) {
            return true;
        }
        return StringUtils.isEmpty(option.getData().get(name.getCodeKey()).toString());
    }

    protected boolean isNotEmpty(CmdTemplateOption option, UmiAttributeNames name) {
        return !isEmpty(option, name);
    }

    protected boolean isBlank(CmdTemplateOption option, UmiAttributeNames name) {
        if (option.getData().get(name.getCodeKey()) == null) {
            return true;
        }
        return StringUtils.isBlank(option.getData().get(name.getCodeKey()).toString());
    }

    protected boolean isNotBlank(CmdTemplateOption option, UmiAttributeNames name) {
        return !isBlank(option, name);
    }

    protected abstract Dialect getDialect();

    protected String parseString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    protected String parseString(CmdTemplateOption option, UmiAttributeNames name) {
        Object obj = option.getData().get(name.getCodeKey());
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    protected boolean parseBoolean(CmdTemplateOption option, UmiAttributeNames name) {
        Object obj = option.getData().get(name.getCodeKey());
        if (obj == null) {
            return false;
        }
        return "true".equalsIgnoreCase(obj.toString());
    }

    protected List<String> parseList(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof List<?>) {
            List<?> values = (List<?>) value;
            if (values.stream().allMatch(item -> item == null || item instanceof String)) {
                List<String> list = new ArrayList<>(values.size());
                for (Object item : values) {
                    list.add((String) item);
                }
                return list;
            }
        }

        return null;
    }

    protected List<Map<String, Object>> parseMapList(Object value) {
        if (!(value instanceof List<?>)) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object item : (List<?>) value) {
            if (!(item instanceof Map<?, ?>)) {
                return null;
            }

            Map<String, Object> map = new java.util.LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) item).entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    return null;
                }
                map.put((String) entry.getKey(), entry.getValue());
            }
            list.add(map);
        }
        return list;
    }

    protected Integer parseInteger(Object value) {
        if (value == null) {
            return null;
        }
        return Integer.valueOf(value.toString());
    }

    protected Long parseLong(Object value) {
        if (value == null) {
            return null;
        }
        return Long.valueOf(value.toString());
    }

    protected View convertToView(Map<String, Object> data) {
        View view = new View();
        view.setName(parseString(data.get(ViewEditorFields.VIEW_NAME)));
        view.setSql(parseString(data.get(ViewEditorFields.SQL)));
        String comment = parseString(data.get(ViewEditorFields.COMMENT));
        if (comment == null) {
            view.setComment("");
        } else {
            view.setComment(comment);
        }

        return view;
    }

    protected Trigger convertToTrigger(Map<String, Object> data) {
        Trigger trigger = new Trigger();
        trigger.setName(parseString(data.get(TriggerEditorFields.TRIGGER_NAME)));
        trigger.setTriggerTable(parseString(data.get(TriggerEditorFields.TRIGGER_TABLE)));
        trigger.setTime(parseString(data.get(TriggerEditorFields.TRIGGER_TIME)));
        trigger.setColumnList(parseList(data.get(TriggerEditorFields.TRIGGER_COLUMNS)));
        trigger.setEventList(parseList(data.get(TriggerEditorFields.TRIGGER_EVENT)));
        trigger.setSql(parseString(data.get(TriggerEditorFields.TRIGGER_BODY)));
        return trigger;
    }

    protected Function convertToFunction(Map<String, Object> data) {
        Function function = new Function();
        function.setName(parseString(data.get(FunctionEditorFields.FUNCTION_NAME)));

        Param returnType = new Param();
        returnType.setName(parseString(data.get(FunctionEditorFields.PARAM_NAME)));

        returnType.setLength(parseLong(data.get(FunctionEditorFields.PARAM_LENGTH)));
        returnType.setMode(parseString(data.get(FunctionEditorFields.PARAM_MODE)));
        returnType.setParamType(parseString(data.get(FunctionEditorFields.RETURN_TYPE)));
        returnType.setDefaultValue(parseString(data.get(FunctionEditorFields.PARAM_DEFAULT_VALUE)));
        returnType.setDatetimePrecision(parseInteger(data.get(FunctionEditorFields.PARAM_DATE_PRECISION)));
        returnType.setNumericPrecision(parseInteger(data.get(FunctionEditorFields.PARAM_NUM_PRECISION)));
        returnType.setNumericScale(parseInteger(data.get(FunctionEditorFields.PARAM_NUM_SCALE)));
        function.setReturnType(returnType);

        List<Map<String, Object>> params = parseMapList(data.get(FunctionEditorFields.FUNCTION_PARAMS));
        if (CollectionUtils.isNotEmpty(params)) {
            for (Map<String, Object> param : params) {
                Param param1 = new Param();
                param1.setName(parseString(param.get(FunctionEditorFields.PARAM_NAME)));
                param1.setLength(parseLong(param.get(FunctionEditorFields.PARAM_LENGTH)));
                param1.setMode(parseString(param.get(FunctionEditorFields.PARAM_MODE)));
                param1.setParamType(parseString(param.get(FunctionEditorFields.PARAM_DATATYPE)));
                param1.setDefaultValue(parseString(param.get(FunctionEditorFields.PARAM_DEFAULT_VALUE)));
                param1.setDatetimePrecision(parseInteger(param.get(FunctionEditorFields.PARAM_DATE_PRECISION)));
                param1.setNumericPrecision(parseInteger(param.get(FunctionEditorFields.PARAM_NUM_PRECISION)));
                param1.setNumericScale(parseInteger(param.get(FunctionEditorFields.PARAM_NUM_SCALE)));
                function.getParams().add(param1);
            }
        }

        function.setSql(parseString(data.get(FunctionEditorFields.SQL)));
        return function;

    }

    //
    @Getter
    @Setter
    protected static class Param {

        private String  name;
        private String  mode;
        private String  paramType;
        private String  defaultValue;

        private Long    length;
        private Integer numericPrecision;
        private Integer numericScale;
        private Integer datetimePrecision;
    }

    @Getter
    @Setter
    protected static class Function {

        private List<Param> params = new ArrayList<>();
        private Param       returnType;
        private String      name;
        private String      sql;
    }

    @Getter
    @Setter
    protected static class View {

        private String name;
        private String comment;
        private String sql;
    }

    @Getter
    @Setter
    protected static class Trigger {

        private String              name;
        private String              triggerTable;
        private List<String>        eventList;
        private List<String>        columnList;
        private String              time;

        private String              sql;
        private Map<String, Object> features;

    }

}
