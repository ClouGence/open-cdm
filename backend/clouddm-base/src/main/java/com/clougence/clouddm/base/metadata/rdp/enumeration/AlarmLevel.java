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
package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author bucketli 2020/2/12 19:08
 */
public enum AlarmLevel {

    /**
     * 1.function not work well,but work normal and correct ,just some metrics shows
     * it is go to dangerous 2.resource will not enough,pre-alarm send SMS/MAIL/IM
     * message to admin 3. console exception in controller. Need pay attention, but
     * may not alert
     */
    Major,

    /**
     * 1.data job delay 2.data job crash send SMS/MAIL/IM/PHONE to admin and data
     * job owner
     */
    Critical,

    /**
     * 1.console/sidecar crash 2.resource not enough send SMS/MAIL/IM/PHONE message
     * to admin
     */
    Blocker
}
