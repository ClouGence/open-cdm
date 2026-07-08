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
package com.clougence.clouddm.ds.selectdb.execute;

import com.clougence.clouddm.ds.doris.execute.DrHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * only for integration test
 *
 * @author Ekko 2022/11/03 16:48
 **/
@Slf4j
public class SelHooks extends DrHooks {

    public SelHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new SelMetaService(session);
    }
}
