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
package com.clougence.clouddm.ds.db2zos.execute;

import com.clougence.clouddm.dsfamily.db2.execute.Db2Hooks;
import com.clougence.clouddm.dsfamily.db2.execute.Db2MetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

/**
 * https://www.ibm.com/docs/zh/i/7.5?topic=statements-set-transaction
 * @author mode create time is 2021/1/12
 **/
public class Db2ForZosHooks extends Db2Hooks {

    @Override
    public Db2MetaService createMetaService(Session session) {
        return new Db2ForZosMetaService(session);
    }
}
