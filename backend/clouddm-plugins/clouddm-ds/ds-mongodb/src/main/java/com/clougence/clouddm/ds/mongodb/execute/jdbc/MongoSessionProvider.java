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
package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import org.bson.Document;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;

class MongoSessionProvider {

    private static final String LEGACY_DRIVER_VERSION = "4.7.2";

    private final MongoClient   client;
    private final String        driverVersion;
    private Boolean             sessionSupported;

    MongoSessionProvider(MongoClient client, String driverVersion){
        this.client = client;
        this.driverVersion = driverVersion;
    }

    public synchronized ClientSession startSession() {
        if (this.sessionSupported == null) {
            this.sessionSupported = detectSessionSupport();
        }
        if (!this.sessionSupported) {
            return null;
        }
        return this.client.startSession();
    }

    private boolean detectSessionSupport() {
        if (!LEGACY_DRIVER_VERSION.equals(this.driverVersion)) {
            return true;
        }

        Document hello = this.client.getDatabase("admin").runCommand(new Document("isMaster", 1));
        return hello.get("logicalSessionTimeoutMinutes") != null;
    }
}
