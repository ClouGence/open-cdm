/*
 * Copyright 2015-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
 * 标记在类型上用于配置对应的索引，该注解能力有限。并不能替代纯 SQL 方式。
 * - 如果存在 xml 和注解共用的情况下，注解配置将会失效。
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-12-06
 */
@Repeatable(IndexDescribeSet.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexDescribe {
    /** 索引名，如果不指定则会自动生成 */
    String name() default "";

    /** 唯一索引 */
    boolean unique() default false;

    /** 通过列名来确定索引的列有哪些 */
    String[] columns();

    /** 索引备注 */
    String comment() default "";

    /** 在生成所以创建语句的时候用于拼接的其它信息，开发者可以随意指定。会在 'create index' 语句生成的最后时自动追加 */
    String other() default "";
}