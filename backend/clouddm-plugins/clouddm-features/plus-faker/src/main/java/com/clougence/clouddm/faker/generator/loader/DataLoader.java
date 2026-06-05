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
package com.clougence.clouddm.faker.generator.loader;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.generator.FakerTable;
import com.clougence.clouddm.faker.generator.SqlArg;
import com.clougence.clouddm.sdk.execute.session.Session;

/**
 * 用于 UPDATE、DELETE 的数据反查
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public interface DataLoader {

    List<Map<String, SqlArg>> loadSomeData(Session session, UseFor useFor, FakerTable fakerTable, int batchSize) throws Exception;
}
