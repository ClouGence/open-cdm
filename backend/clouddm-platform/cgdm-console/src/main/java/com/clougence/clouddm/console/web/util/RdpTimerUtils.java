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
package com.clougence.clouddm.console.web.util;

import java.util.concurrent.TimeUnit;

import com.clougence.utils.ThreadUtils;
import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.TimerTask;

/**
 * @author mode 2020-01-04 09:44
 * @since 1.1.3
 */
public class RdpTimerUtils {

    private static final HashedWheelTimer GLOBAL_TIMER;
    static {
        ClassLoader classLoader = RdpTimerUtils.class.getClassLoader();
        GLOBAL_TIMER = new HashedWheelTimer(ThreadUtils.daemonThreadFactory(classLoader, "Global-Timer-%s"));
    }

    public static void onTimeout(TimerTask task, long delay, TimeUnit unit) {
        GLOBAL_TIMER.newTimeout(task, delay, unit);
    }
}
