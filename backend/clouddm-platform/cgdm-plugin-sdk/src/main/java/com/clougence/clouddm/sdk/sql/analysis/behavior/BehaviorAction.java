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
package com.clougence.clouddm.sdk.sql.analysis.behavior;

/**
 * Semantic action between one behavior subject and one or more behavior objects.
 *
 * <p>An action only describes what the statement does. Authorization requirements are derived
 * from both the action and its resource.</p>
 */
public enum BehaviorAction {

    CREATE,
    ALTER,
    DROP,
    RENAME,
    //
    READ,
    INSERT,
    UPDATE,
    DELETE,
    MERGE,
    REPLACE,
    IMPORT,
    EXPORT,
    //
    CALL,
    //
    GRANT,
    REVOKE,
    TRANSFER,
    //
    COPY,
    MOVE,
    LOCK,
    CONFIGURE,
    SWITCH,
    ADMIN,
    OTHER;
}
