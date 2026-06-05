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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;
/* ------------------------------------------------------------ */

/** Convert a {@link Date} to JSON.
 * If fromJSON is true in the constructor, the JSON generated will
 * be of the form {class="java.util.Date",value="1/1/1970 12:00 GMT"}
 * If fromJSON is false, then only the string value of the date is generated. */
@Deprecated // use com.clougence.utils.JsonUtils
public class JSONDateConvertor implements JSON.Convertor {

    protected final static Logger logger = Logger.getLogger(JSONDateConvertor.class.getName());
    private boolean               _fromJSON;
    DateCache                     _dateCache;
    SimpleDateFormat              _format;

    public JSONDateConvertor(){
        this(false);
    }

    public JSONDateConvertor(boolean fromJSON){
        this(DateCache.DEFAULT_FORMAT, TimeZone.getTimeZone("GMT"), fromJSON);
    }

    public JSONDateConvertor(String format, TimeZone zone, boolean fromJSON){
        _dateCache = new DateCache(format);
        _dateCache.setTimeZone(zone);
        _fromJSON = fromJSON;
        _format = new SimpleDateFormat(format);
        _format.setTimeZone(zone);
    }

    public JSONDateConvertor(String format, TimeZone zone, boolean fromJSON, Locale locale){
        _dateCache = new DateCache(format, locale);
        _dateCache.setTimeZone(zone);
        _fromJSON = fromJSON;
        _format = new SimpleDateFormat(format, new DateFormatSymbols(locale));
        _format.setTimeZone(zone);
    }

    public Object fromJSON(Map map) {
        if (!_fromJSON)
            throw new UnsupportedOperationException();
        try {
            synchronized (_format) {
                return _format.parseObject((String) map.get("value"));
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    public void toJSON(Object obj, JSON.Output out) {
        String date = _dateCache.format((Date) obj);
        if (_fromJSON) {
            out.addClass(obj.getClass());
            out.add("value", date);
        } else {
            out.add(date);
        }
    }
}
