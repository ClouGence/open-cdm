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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;
/* ------------------------------------------------------------ */

/** Convert an {@link Enum} to JSON.
 * If fromJSON is true in the constructor, the JSON generated will
 * be of the form {class="com.acme.TrafficLight",value="Green"}
 * If fromJSON is false, then only the string value of the enum is generated. */
@Deprecated // use com.clougence.utils.JsonUtils
public class JSONEnumConvertor implements JSON.Convertor {

    protected final static Logger logger = Logger.getLogger(JSONEnumConvertor.class.getName());
    private boolean               _fromJSON;
    private Method                _valueOf;

    {
        try {
            Class e = Loader.loadClass(getClass(), "java.lang.Enum");
            _valueOf = e.getMethod("valueOf", new Class[] { Class.class, String.class });
        } catch (Exception e) {
            throw new RuntimeException("!Enums", e);
        }
    }

    public JSONEnumConvertor(){
        this(false);
    }

    public JSONEnumConvertor(boolean fromJSON){
        _fromJSON = fromJSON;
    }

    public Object fromJSON(Map map) {
        if (!_fromJSON)
            throw new UnsupportedOperationException();
        try {
            Class c = Loader.loadClass(getClass(), (String) map.get("class"));
            return _valueOf.invoke(null, new Object[] { c, map.get("value") });
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    public void toJSON(Object obj, JSON.Output out) {
        if (_fromJSON) {
            out.addClass(obj.getClass());
            out.add("value", obj.toString());
        } else {
            out.add(obj.toString());
        }
    }
}
