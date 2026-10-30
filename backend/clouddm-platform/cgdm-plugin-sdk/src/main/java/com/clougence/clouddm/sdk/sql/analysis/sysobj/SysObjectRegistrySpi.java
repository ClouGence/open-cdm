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
package com.clougence.clouddm.sdk.sql.analysis.sysobj;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

/**
 * Named SQL-engine registry for resources which do not require object authorization.
 *
 * <p>Implementations are registered by SQL parser plugins under the same name as their
 * {@code SqlEngineSpi}. The registry classifies derived resource requests and does not modify
 * behavior analysis results.</p>
 */
public interface SysObjectRegistrySpi extends Spi {

    boolean isPermissionExempt(BehaviorAction action, TargetType targetType,    //
                               String catalog, String schema, String objectName,//
                               String dbVersion);
}
