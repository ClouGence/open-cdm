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
package com.clougence.clouddm.sdk.security.auth;

import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;

import lombok.Getter;

@Getter
public enum SecDataAuthKind {

    // for all
    READ(SecDataAuthLabel.DM_DAUTH_QUERY),
    WRITE(SecDataAuthLabel.DM_DAUTH_DML),
    ADMIN(SecDataAuthLabel.DM_DAUTH_DCL),
    OTHER(SecDataAuthLabel.DM_DAUTH_OTHER),

    // for RDBMS
    SPACE(SecDataAuthLabel.DM_DAUTH_SPACE),
    DDL(SecDataAuthLabel.DM_DAUTH_DDL),
    OBJECT(SecDataAuthLabel.DM_DAUTH_OBJ),
    CALL(SecDataAuthLabel.DM_DAUTH_CALL),;

    private final String authLabel;

    SecDataAuthKind(String authLabel){
        this.authLabel = authLabel;
    }
}
