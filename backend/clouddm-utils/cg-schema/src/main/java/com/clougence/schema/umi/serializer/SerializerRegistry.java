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
package com.clougence.schema.umi.serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.clougence.schema.umi.serializer.rdb.*;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiData;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.schema.umi.struts.constraint.Primary;
import com.clougence.schema.umi.struts.constraint.Unique;

public class SerializerRegistry {

    private static final Map<String, Serializer<?>>     serializerMap     = new HashMap<>();
    private static final Map<String, Supplier<UmiData>> umiDataCreatorMap = new HashMap<>();

    private static void addSerializer(Class<?> type, Serializer<?> serializer, Supplier<UmiData> creator) {
        serializerMap.put(type.getSimpleName(), serializer);
        umiDataCreatorMap.put(type.getSimpleName(), creator);
    }

    static {
        addSerializer(ConstraintObject.class, new ConstraintObjectSerializer<>(), ConstraintObject::new);
        addSerializer(NonNull.class, new NonNullSerializer<>(), NonNull::new);
        addSerializer(Primary.class, new PrimarySerializer<>(), Primary::new);
        addSerializer(Unique.class, new UniqueSerializer<>(), Unique::new);
        //
        addSerializer(RdbValue.class, new RdbValueSerializer(), RdbValue::new);
        addSerializer(RdbTable.class, new RdbTableSerializer(), RdbTable::new);
        addSerializer(RdbColumn.class, new RdbColumnSerializer(), RdbColumn::new);
        addSerializer(RdbPrimaryKey.class, new RdbPrimaryKeySerializer(), RdbPrimaryKey::new);
        addSerializer(RdbUniqueKey.class, new RdbUniqueKeySerializer(), RdbUniqueKey::new);
        addSerializer(RdbForeignKey.class, new RdbForeignKeySerializer(), RdbForeignKey::new);
        addSerializer(RdbIndex.class, new RdbIndexSerializer(), RdbIndex::new);
        addSerializer(RdbTrigger.class, new RdbTriggerSerializer(), RdbTrigger::new);
        addSerializer(RdbView.class, new RdbViewSerializer(), RdbView::new);
        addSerializer(RdbFunction.class, new RdbFunctionSerializer(), RdbFunction::new);
        addSerializer(RdbProcedure.class, new RdbProcedureSerializer(), RdbProcedure::new);
        addSerializer(RdbParam.class, new RdbParamSerializer(), RdbParam::new);
        addSerializer(RdbJob.class, new RdbJobSerializer(), RdbJob::new);
        addSerializer(RdbScheduleJob.class, new RdbScheduleJobSerializer(), RdbScheduleJob::new);
        addSerializer(RdbDbLink.class, new RdbDbLinkSerializer(), RdbDbLink::new);
        addSerializer(RdbSequence.class, new RdbSequenceSerializer(), RdbSequence::new);
        addSerializer(RdbUser.class, new RdbUserSerializer(), RdbUser::new);
        addSerializer(RdbRole.class, new RdbRoleSerializer(), RdbRole::new);
        addSerializer(RdbSynonym.class, new RdbSynonymSerializer(), RdbSynonym::new);
    }

    public static UmiData createByType(String aClassName) {
        Supplier<UmiData> creator = umiDataCreatorMap.get(aClassName);
        if (creator == null) {
            throw new UnsupportedOperationException("serialize schema '" + aClassName + "' Unsupported.");
        } else {
            return creator.get();
        }
    }

    public static <T> Serializer<T> lookSerializer(String aClassName) {
        Serializer<?> serializer = serializerMap.get(aClassName);
        if (serializer == null) {
            throw new UnsupportedOperationException("serialize schema '" + aClassName + "' Unsupported.");
        } else {
            return (Serializer<T>) serializer;
        }
    }
}
