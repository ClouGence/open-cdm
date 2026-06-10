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
package com.clougence.utils.reflect.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark as a map table on type
 * - If the note is configured with xml, XML will overwrite the note.
 * - If xml is configured as a resultMap, the datalog/ schema/table or value is set to be empty.
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-10-31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultMap {
    /** space */
    String space() default "";

    /** Map ID if empty */
    String value() default "";

    /** Map ID if empty */
    String id() default "";

    /** Whether or not to automatically map all fields under the type and columns in the database. false means you must adopt @Column */
    boolean autoMapping() default true;

    /** Whether or not to be sensitive to listing, default true not sensitive */
    boolean caseInsensitive() default true;

    /** Convert table and attribute names to underscored names according to camel-case rules. */
    boolean mapUnderscoreToCamelCase() default false;
}
