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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The type of mark is used to configure the corresponding index, which has limited ability to comment. Could not close temporary folder: %s
 * - If xml and note are shared, the note configuration will expire.
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-12-06
 */
@Repeatable(IndexDescribeSet.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexDescribe {
    /** index name, automatically generated if not specified */
    String name() default "";

    /** Unique index */
    boolean unique() default false;

    /** Listing to determine what is listed in the index */
    String[] columns();

    /** Index Remarks */
    String comment() default "";

    /** When generating other information that you use to spell when you create a statement, the developer is free to specify. Auto-add to 'create index' statement */
    String other() default "";
}