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
package com.clougence.utils.json;

import java.util.Map;

/**
 *
 */
@Deprecated // use com.clougence.utils.JsonUtils
public class JSONPojoConvertorFactory implements JSON.Convertor {

    private final JSON    _json;
    private final boolean _fromJson;

    public JSONPojoConvertorFactory(JSON json){
        if (json == null) {
            throw new IllegalArgumentException();
        }
        _json = json;
        _fromJson = true;
    }
    /* ------------------------------------------------------------ */

    /**
     * @param json The JSON instance to use
     * @param fromJSON If true, the class name of the objects is included
     * in the generated JSON and is used to instantiate the object when
     * JSON is parsed (otherwise a Map is used).
     */
    public JSONPojoConvertorFactory(JSON json, boolean fromJSON){
        if (json == null) {
            throw new IllegalArgumentException();
        }
        _json = json;
        _fromJson = fromJSON;
    }

    /* ------------------------------------------------------------ */
    public void toJSON(Object obj, JSON.Output out) {
        String clsName = obj.getClass().getName();
        JSON.Convertor convertor = _json.getConvertorFor(clsName);
        if (convertor == null) {
            try {
                Class cls = Loader.loadClass(JSON.class, clsName);
                convertor = new JSONPojoConvertor(cls, _fromJson);
                _json.addConvertorFor(clsName, convertor);
            } catch (ClassNotFoundException e) {
                JSON.logger.warning(e.getMessage());
            }
        }
        if (convertor != null) {
            convertor.toJSON(obj, out);
        }
    }

    public Object fromJSON(Map object) {
        Map map = object;
        String clsName = (String) map.get("class");
        if (clsName != null) {
            JSON.Convertor convertor = _json.getConvertorFor(clsName);
            if (convertor == null) {
                try {
                    Class cls = Loader.loadClass(JSON.class, clsName);
                    convertor = new JSONPojoConvertor(cls, _fromJson);
                    _json.addConvertorFor(clsName, convertor);
                } catch (ClassNotFoundException e) {
                    JSON.logger.warning(e.getMessage());
                }
            }
            if (convertor != null) {
                return convertor.fromJSON(object);
            }
        }
        return map;
    }
}
