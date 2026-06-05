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
package com.clougence.drivers;

import com.clougence.drivers.def.ResDef;

public interface DriverPrepareProgress {

    default void onStart(DriverVersion driverVersion, ResDef resDef, int index, int totalCount) {
    }

    default void onProgress(DriverVersion driverVersion, ResDef resDef, String fileName, long current, long total) {
    }

    default void onComplete(DriverVersion driverVersion, ResDef resource, int index, int totalCount) {
    }

    default void onError(DriverVersion driverVersion, ResDef resource, Exception exception) {
    }
}
