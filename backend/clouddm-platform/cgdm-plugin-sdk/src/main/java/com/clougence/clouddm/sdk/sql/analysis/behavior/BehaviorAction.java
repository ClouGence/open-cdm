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

import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;

import lombok.Getter;

/**
 * Semantic action between one behavior subject and one or more behavior objects.
 *
 * <p>An action describes what the statement does and maps to an existing coarse-grained CloudDM
 * authorization kind. It does not represent a database-native privilege.</p>
 */
@Getter
public enum BehaviorAction {

    READ(SecDataAuthKind.READ),
    CREATE(SecDataAuthKind.DDL),
    ALTER(SecDataAuthKind.DDL),
    DROP(SecDataAuthKind.DDL),
    RENAME(SecDataAuthKind.DDL),
    INSERT(SecDataAuthKind.WRITE),
    UPDATE(SecDataAuthKind.WRITE),
    DELETE(SecDataAuthKind.WRITE),
    MERGE(SecDataAuthKind.WRITE),
    REPLACE(SecDataAuthKind.WRITE),
    CALL(SecDataAuthKind.CALL),
    GRANT(SecDataAuthKind.ADMIN),
    REVOKE(SecDataAuthKind.ADMIN),
    TRANSFER(SecDataAuthKind.ADMIN),
    IMPORT(SecDataAuthKind.WRITE),
    EXPORT(SecDataAuthKind.READ),
    COPY(SecDataAuthKind.WRITE),
    MOVE(SecDataAuthKind.WRITE),
    LOCK(SecDataAuthKind.OTHER),
    CONFIGURE(SecDataAuthKind.ADMIN),
    SWITCH(SecDataAuthKind.READ),
    ADMIN(SecDataAuthKind.ADMIN),
    OTHER(SecDataAuthKind.OTHER);

    private final SecDataAuthKind authKind;

    BehaviorAction(SecDataAuthKind authKind){
        this.authKind = authKind;
    }
}
