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
package com.clougence.detectrule.runtime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.clougence.utils.DateTimeUtils;
import com.clougence.utils.format.WellKnowFormat;

public class DefaultDataTimeValueParser implements DataTimeValueParser {

    protected static final Map<String, WellKnowFormat> CACHE;
    static {
        CACHE = new HashMap<>();
        for (WellKnowFormat wkf : WellKnowFormat.values()) {
            CACHE.put(wkf.toPattern().toString(), wkf);
        }
    }

    @Override
    public Object resolve(String fmt, String value) throws ParseException {
        String fmtStr = DateTimeFormatter.ofPattern(fmt).toString();
        if (CACHE.containsKey(fmtStr)) {
            WellKnowFormat wkf = CACHE.get(fmtStr);
            switch (wkf) {
                case WKF_DATE8:
                case WKF_DATE10:
                    return LocalDate.parse(value, wkf.toPattern());
                case WKF_TIME24:
                case WKF_TIME24_S0:
                case WKF_TIME24_S1:
                case WKF_TIME24_S2:
                case WKF_TIME24_S3:
                case WKF_TIME24_S4:
                case WKF_TIME24_S5:
                case WKF_TIME24_S6:
                case WKF_TIME24_S7:
                case WKF_TIME24_S8:
                case WKF_TIME24_S9:
                    return LocalTime.parse(value, wkf.toPattern());
                case WKF_DATE_TIME24:
                case WKF_DATE_TIME24_S0:
                case WKF_DATE_TIME24_S1:
                case WKF_DATE_TIME24_S2:
                case WKF_DATE_TIME24_S3:
                case WKF_DATE_TIME24_S4:
                case WKF_DATE_TIME24_S5:
                case WKF_DATE_TIME24_S6:
                case WKF_DATE_TIME24_S7:
                case WKF_DATE_TIME24_S8:
                case WKF_DATE_TIME24_S9:
                    return LocalDateTime.parse(value, wkf.toPattern());
                case WKF_OFFSET_DATE_TIME24:
                case WKF_OFFSET_DATE_TIME24_S0:
                case WKF_OFFSET_DATE_TIME24_S1:
                case WKF_OFFSET_DATE_TIME24_S2:
                case WKF_OFFSET_DATE_TIME24_S3:
                case WKF_OFFSET_DATE_TIME24_S4:
                case WKF_OFFSET_DATE_TIME24_S5:
                case WKF_OFFSET_DATE_TIME24_S6:
                case WKF_OFFSET_DATE_TIME24_S7:
                case WKF_OFFSET_DATE_TIME24_S8:
                case WKF_OFFSET_DATE_TIME24_S9:
                    return OffsetDateTime.parse(value, wkf.toPattern());
                case WKF_OFFSET_TIME24:
                case WKF_OFFSET_TIME24_S0:
                case WKF_OFFSET_TIME24_S1:
                case WKF_OFFSET_TIME24_S2:
                case WKF_OFFSET_TIME24_S3:
                case WKF_OFFSET_TIME24_S4:
                case WKF_OFFSET_TIME24_S5:
                case WKF_OFFSET_TIME24_S6:
                case WKF_OFFSET_TIME24_S7:
                case WKF_OFFSET_TIME24_S8:
                case WKF_OFFSET_TIME24_S9:
                    return OffsetTime.parse(value, wkf.toPattern());
                case WKF_OFFSET_DATE8:
                case WKF_OFFSET_DATE10:
                    String zone = DateTimeUtils.extractZone(value);
                    String date = DateTimeUtils.extractDate(value);
                    return OffsetDateTime.parse(date + " 00:00:00" + zone, WellKnowFormat.WKF_OFFSET_DATE_TIME24.toPattern());
            }
        }
        return new SimpleDateFormat(fmt).parse(value);
    }
}
