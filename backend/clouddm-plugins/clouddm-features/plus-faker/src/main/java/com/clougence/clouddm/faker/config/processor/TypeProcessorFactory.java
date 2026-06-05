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
package com.clougence.clouddm.faker.config.processor;

import com.clougence.clouddm.faker.config.dsl.TypeProcessConf;
import com.clougence.clouddm.faker.generator.TypeProcessor;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.schema.umi.special.rdb.RdbColumn;

import com.clougence.utils.setting.SettingNode;

/**
 * 读取并解析 tpc 配置文件，并根据类型和数据库信息选择对应的 tpc 配置。利用 tpc 的配置信息来创建 TypeProcessor。
 * @version : 2023-02-14
 * @author 赵永春 (zyc@hasor.net)
 */
public interface TypeProcessorFactory {

    TypeProcessor createSeedFactory(RdbColumn rdbColumn, SettingNode columnConfig) throws ReflectiveOperationException;

    void fillSeedConfig(RdbColumn rdbColumn, SettingNode columnConfig, TypeProcessor typeProcessor) throws ReflectiveOperationException;

    SeedType findSeedType(RdbColumn rdbColumn);

    TypeProcessConf findTypeProcessConf(String colType, String colName);
}
