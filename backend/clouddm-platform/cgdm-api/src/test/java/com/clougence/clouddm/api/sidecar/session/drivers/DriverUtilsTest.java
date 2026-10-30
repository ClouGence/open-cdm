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
package com.clougence.clouddm.api.sidecar.session.drivers;

import org.junit.Assert;
import org.junit.Test;

public class DriverUtilsTest {

    @Test
    public void should_parse_driver_ref_from_json_array_format() {
        DriverRef driverRef = DriverUtils.parseDriverRef("[\"MySQL Connector/J\",\"/5.1.49\"]");

        Assert.assertEquals("MySQL Connector/J", driverRef.getDriverFamily());
        Assert.assertEquals("5.1.49", driverRef.getDriverVersion());
    }
}
