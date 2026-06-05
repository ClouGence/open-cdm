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
package com.clougence.detectrule.runtime.v1;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clougence.detectrule.lang.*;
import com.clougence.detectrule.lang.reflect.Accessible;
import com.clougence.detectrule.lang.reflect.Type;
import com.clougence.detectrule.lang.reflect.TypeType;
import com.clougence.detectrule.runtime.DetectRuleRuntimeError;
import com.clougence.dslpaser.ast.location.Location;

class RefUtils {

    public static LangObject toBooleanOrNull(Boolean refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        } else {
            return new ValueObject(refObject, TypeType.Boolean);
        }
    }

    public static LangObject toFloatOrNull(Number refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        } else {
            return new ValueObject(refObject.doubleValue(), TypeType.Float);
        }
    }

    public static LangObject toIntegerOrNull(Number refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        } else {
            return new ValueObject(refObject.longValue(), TypeType.Integer);
        }
    }

    public static LangObject toDecimalOrNull(BigDecimal refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        } else {
            return new ValueObject(refObject, TypeType.Decimal);
        }
    }

    public static LangObject toStringOrNull(Object refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        } else if (refObject instanceof Enum && refObject instanceof EnumAccess) {
            return new ValueObject(((EnumAccess<?>) refObject).codeName(), TypeType.String);
        } else {
            return new ValueObject(refObject.toString(), TypeType.String);
        }
    }

    public static LangObject toDateOrNull(Location inst, TypeType srcType, Object refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        }

        Object unwrap = refObject;
        if (unwrap instanceof java.sql.Date) {
            return new ValueObject(((java.sql.Date) unwrap).toLocalDate(), TypeType.Date);

        } else if (unwrap instanceof java.sql.Timestamp) {
            //LocalDateTime instant = ((Timestamp) unwrap).toLocalDateTime().toLocalDate();
            java.sql.Timestamp st = (Timestamp) unwrap;
            return new ValueObject(LocalDate.of(st.getYear() + 1900, st.getMonth() + 1, st.getDate()), TypeType.Date);

        } else if (unwrap instanceof java.sql.Time) {
            return new ValueObject(LocalDate.of(1970, 1, 1), TypeType.Date);

        } else if (unwrap instanceof java.util.Date) {
            //LocalDate localDate = ((Date) unwrap).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            java.util.Date d = (java.util.Date) unwrap;
            LocalDate localDate = LocalDate.of(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
            return new ValueObject(localDate, TypeType.Date);

        } else if (unwrap instanceof java.time.LocalDate) {
            return new ValueObject(unwrap, TypeType.Date);

        } else if (unwrap instanceof java.time.LocalTime || unwrap instanceof java.time.OffsetTime) {
            return new ValueObject(LocalDate.of(1970, 1, 1), TypeType.Date);

        } else if (unwrap instanceof java.time.Instant) {
            LocalDate localDate = ((Instant) unwrap).atZone(ZoneId.systemDefault()).toLocalDate();
            return new ValueObject(localDate, TypeType.Date);

        } else if (unwrap instanceof java.time.LocalDateTime) {
            return new ValueObject(((LocalDateTime) unwrap).toLocalDate(), TypeType.Date);

        } else if (unwrap instanceof java.time.OffsetDateTime) {
            return new ValueObject(((OffsetDateTime) unwrap).toLocalDate(), TypeType.Date);

        } else if (unwrap instanceof java.time.ZonedDateTime) {
            return new ValueObject(((ZonedDateTime) unwrap).toLocalDate(), TypeType.Date);

        } else if (unwrap instanceof java.time.chrono.JapaneseDate) {
            return new ValueObject(LocalDate.ofEpochDay(((JapaneseDate) unwrap).toEpochDay()), TypeType.Date);

        } else {
            throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Date.");
        }
    }

    public static LangObject toTimeOrNull(Location inst, TypeType srcType, Object refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        }

        Object unwrap = refObject;
        if (unwrap instanceof java.sql.Date) {
            return new ValueObject(LocalTime.of(0, 0, 0), TypeType.Time);

        } else if (unwrap instanceof java.sql.Timestamp) {
            java.sql.Timestamp st = (java.sql.Timestamp) unwrap;
            LocalTime localTime = LocalTime.of(st.getHours(), st.getMinutes(), st.getSeconds(), st.getNanos());
            return new ValueObject(localTime, TypeType.Time);

        } else if (unwrap instanceof java.sql.Time) {
            return new ValueObject(((Time) unwrap).toLocalTime(), TypeType.Time);

        } else if (unwrap instanceof java.util.Date) {
            LocalTime localTime = ((Date) unwrap).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            return new ValueObject(localTime, TypeType.Time);

        } else if (unwrap instanceof java.time.LocalDate) {
            return new ValueObject(LocalTime.of(0, 0, 0), TypeType.Time);

        } else if (unwrap instanceof java.time.LocalTime) {
            return new ValueObject(unwrap, TypeType.Time);

        } else if (unwrap instanceof java.time.OffsetTime) {
            return new ValueObject(((OffsetTime) unwrap).toLocalTime(), TypeType.Time);

        } else if (unwrap instanceof java.time.Instant) {
            LocalTime localTime = ((Instant) unwrap).atZone(ZoneId.systemDefault()).toLocalTime();
            return new ValueObject(localTime, TypeType.Time);

        } else if (unwrap instanceof java.time.LocalDateTime) {
            return new ValueObject(((LocalDateTime) unwrap).toLocalTime(), TypeType.Time);

        } else if (unwrap instanceof java.time.OffsetDateTime) {
            return new ValueObject(((OffsetDateTime) unwrap).toLocalTime(), TypeType.Time);

        } else if (unwrap instanceof java.time.ZonedDateTime) {
            return new ValueObject(((ZonedDateTime) unwrap).toLocalTime(), TypeType.Time);

        } else if (unwrap instanceof java.time.chrono.JapaneseDate) {
            return new ValueObject(LocalTime.of(0, 0, 0), TypeType.Time);

        } else {
            throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Time.");
        }
    }

    public static LangObject toDatetimeOrNull(Location inst, TypeType srcType, Object refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        }

        Object unwrap = refObject;
        if (unwrap instanceof java.sql.Date) {
            java.sql.Date st = (java.sql.Date) unwrap;
            LocalDateTime dateTime = LocalDateTime.of(st.getYear() + 1900, st.getMonth() + 1, st.getDate(), 0, 0, 0);
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.sql.Timestamp) {
            java.sql.Timestamp st = (java.sql.Timestamp) unwrap;
            LocalDateTime dateTime = LocalDateTime.of(st.getYear() + 1900, st.getMonth() + 1, st.getDate(), st.getHours(), st.getMinutes(), st.getSeconds(), st.getNanos());
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.sql.Time) {
            java.sql.Time st = (java.sql.Time) unwrap;
            LocalDateTime dateTime = LocalDateTime.of(1970, 1, 1, st.getHours(), st.getMinutes(), st.getSeconds());
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.util.Date) {
            LocalDateTime dateTime = ((java.util.Date) unwrap).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.time.LocalDate) {
            LocalDateTime dateTime = LocalDateTime.of((java.time.LocalDate) unwrap, LocalTime.of(0, 0, 0));
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.time.LocalTime) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(1970, 1, 1), (java.time.LocalTime) unwrap);
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.time.OffsetTime) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(1970, 1, 1), ((OffsetTime) unwrap).toLocalTime());
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.time.Instant) {
            LocalDateTime dateTime = ((Instant) unwrap).atZone(ZoneId.systemDefault()).toLocalDateTime();
            return new ValueObject(dateTime, TypeType.Datetime);

        } else if (unwrap instanceof java.time.LocalDateTime) {
            return new ValueObject(unwrap, TypeType.Datetime);

        } else if (unwrap instanceof java.time.OffsetDateTime) {
            return new ValueObject(((OffsetDateTime) unwrap).toLocalDateTime(), TypeType.Datetime);

        } else if (unwrap instanceof java.time.ZonedDateTime) {
            return new ValueObject(((ZonedDateTime) unwrap).toLocalDateTime(), TypeType.Datetime);

        } else if (unwrap instanceof java.time.chrono.JapaneseDate) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.ofEpochDay(((JapaneseDate) unwrap).toEpochDay()), LocalTime.of(0, 0, 0));
            return new ValueObject(dateTime, TypeType.Datetime);

        } else {
            throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Datetime.");
        }
    }

    public static LangObject toCollectionOrNull(Location inst, Accessible parent, TypeType srcType, Object refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        }

        if (refObject instanceof CollectionAccess) {
            return (LangObject) refObject;
        }

        Class<?> refType = refObject.getClass();
        if (TypeType.valueOfType(refType) != TypeType.Collection) {
            throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Collection.");
        }

        if (refType.isArray()) {
            return arrayToCollection(parent, refObject);
        } else if (java.util.List.class.isAssignableFrom(refType)) {
            return listToCollection(parent, refObject);
        } else {
            throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to Collection.");
        }
    }

    private static ArrayObject arrayToCollection(Accessible parent, Object refObject) {
        Type elementType = ((Type) parent).getComponentType();
        TypeType elementTypeType = elementType.getTypeType();

        ArrayObject arrayObject;
        if (elementTypeType.isAtomic()) {
            arrayObject = new ArrayObject(elementType);
        } else {
            arrayObject = new ArrayObject();
        }

        int len = Array.getLength(refObject);
        for (int i = 0; i < len; i++) {
            Object arrObj = Array.get(refObject, i);
            if (arrObj == null) {
                arrayObject.putElement(CastUtils.NULL);
            } else {
                TypeType arrObjType = elementTypeType.isAtomic() ? elementTypeType : TypeType.valueOfType(arrObj.getClass());
                arrayObject.putElement(new ValueObject(arrObj, arrObjType));
            }
        }

        return arrayObject;
    }

    private static ArrayObject listToCollection(Accessible parent, Object refObject) {
        Type elementType = ((Type) parent).getComponentType();
        TypeType elementTypeType = elementType.getTypeType();

        ArrayObject arrayObject;
        if (elementTypeType.isAtomic()) {
            arrayObject = new ArrayObject(elementType);
        } else {
            arrayObject = new ArrayObject();
        }

        for (Object arrObj : (List) refObject) {
            if (arrObj == null) {
                arrayObject.putElement(CastUtils.NULL);
            } else {
                TypeType arrObjType = elementTypeType.isAtomic() ? elementTypeType : TypeType.valueOfType(arrObj.getClass());
                arrayObject.putElement(new ValueObject(arrObj, arrObjType));
            }
        }

        return arrayObject;
    }

    public static LangObject toKeyPairOrNull(Location inst, Accessible parent, TypeType srcType, Object refObject) {
        if (refObject == null) {
            return CastUtils.NULL;
        }

        if (refObject instanceof KeyPairAccess) {
            return (LangObject) refObject;
        }

        Class<?> refType = refObject.getClass();
        if (TypeType.valueOfType(refType) != TypeType.KeyPair) {
            throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to KeyPair.");
        }

        if (java.util.Map.class.isAssignableFrom(refType)) {
            return mapToKeyPair(parent, refObject);
        } else {
            throw new DetectRuleRuntimeError(inst, "the type '" + srcType + "' Cannot be cast to KeyPair.");
        }
    }

    private static KeyPairObject mapToKeyPair(Accessible parent, Object refObject) {
        Type elementType = ((Type) parent).getComponentType();
        TypeType elementTypeType = elementType.getTypeType();

        KeyPairObject keyPairObject;
        if (elementTypeType.isAtomic()) {
            keyPairObject = new KeyPairObject(elementType);
        } else {
            keyPairObject = new KeyPairObject();
        }

        Map refMap = (Map) refObject;
        for (Object key : refMap.keySet()) {
            if (key == null) {
                continue;
            }

            String keyStr = String.valueOf(key.toString());
            Object pairObj = refMap.get(keyStr);

            if (pairObj == null) {
                keyPairObject.putKeyPair(keyStr, CastUtils.NULL);
            } else {
                TypeType arrObjType = elementTypeType.isAtomic() ? elementTypeType : TypeType.valueOfType(pairObj.getClass());
                keyPairObject.putKeyPair(keyStr, new ValueObject(pairObj, arrObjType));
            }
        }

        return keyPairObject;
    }
}
