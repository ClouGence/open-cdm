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

import java.sql.Connection;

import com.clougence.clouddm.ds.selectdb.dsconf.SelConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.drivers.DsObject;

/**
 * only for integration test
 *
 * @author Ekko 2022/11/03 16:48
 **/
public class SelSessionFactory extends RdbSessionFactory<SelConfig> {

    @Override
    protected Session newSession(SelConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        SelSession session = new SelSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }

}
