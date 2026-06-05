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
package com.clougence.clouddm.console.web.service.editor.query;

import java.util.concurrent.TimeUnit;

import lombok.Getter;

@Getter
public class ExitCode {

    private boolean               useDelay;
    private int                   delayTime;
    private TimeUnit              delayUnit;

    //
    private static final ExitCode FINISH;
    private static final ExitCode DELAY_0MS;
    private static final ExitCode DELAY_5MS;
    private static final ExitCode DELAY_10MS;
    private static final ExitCode DELAY_30MS;
    private static final ExitCode DELAY_50MS;
    private static final ExitCode DELAY_100MS;
    private static final ExitCode DELAY_250MS;
    private static final ExitCode DELAY_500MS;
    private static final ExitCode DELAY_1000MS;

    static {
        FINISH = new ExitCode();
        FINISH.useDelay = false;

        DELAY_0MS = new ExitCode();
        DELAY_0MS.useDelay = true;
        DELAY_0MS.delayTime = 0;
        DELAY_0MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_5MS = new ExitCode();
        DELAY_5MS.useDelay = true;
        DELAY_5MS.delayTime = 5;
        DELAY_5MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_10MS = new ExitCode();
        DELAY_10MS.useDelay = true;
        DELAY_10MS.delayTime = 10;
        DELAY_10MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_30MS = new ExitCode();
        DELAY_30MS.useDelay = true;
        DELAY_30MS.delayTime = 30;
        DELAY_30MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_50MS = new ExitCode();
        DELAY_50MS.useDelay = true;
        DELAY_50MS.delayTime = 50;
        DELAY_50MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_100MS = new ExitCode();
        DELAY_100MS.useDelay = true;
        DELAY_100MS.delayTime = 100;
        DELAY_100MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_250MS = new ExitCode();
        DELAY_250MS.useDelay = true;
        DELAY_250MS.delayTime = 250;
        DELAY_250MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_500MS = new ExitCode();
        DELAY_500MS.useDelay = true;
        DELAY_500MS.delayTime = 500;
        DELAY_500MS.delayUnit = TimeUnit.MILLISECONDS;

        DELAY_1000MS = new ExitCode();
        DELAY_1000MS.useDelay = true;
        DELAY_1000MS.delayTime = 1;
        DELAY_1000MS.delayUnit = TimeUnit.SECONDS;
    }

    private ExitCode(){

    }

    public static ExitCode again() {
        return DELAY_0MS;
    }

    public static ExitCode delay5ms() {
        return DELAY_5MS;
    }

    public static ExitCode delay10ms() {
        return DELAY_10MS;
    }

    public static ExitCode delay30ms() {
        return DELAY_30MS;
    }

    public static ExitCode delay50ms() {
        return DELAY_50MS;
    }

    public static ExitCode delay100ms() {
        return DELAY_100MS;
    }

    public static ExitCode delay250ms() {
        return DELAY_250MS;
    }

    public static ExitCode delay500ms() {
        return DELAY_500MS;
    }

    public static ExitCode delay1000ms() {
        return DELAY_1000MS;
    }

    public static ExitCode delayTimes(long times) {
        if (times == 0) {
            return again();
        } else if (times == 1 || times == 2) {
            return delay5ms();
        } else if (times == 3 || times == 4) {
            return delay10ms();
        } else if (times == 5 || times == 6) {
            return delay30ms();
        } else if (times == 7 || times == 8) {
            return delay50ms();
        } else if (times == 9 || times == 10) {
            return delay100ms();
        } else if (times == 11 || times == 12) {
            return delay250ms();
        } else if (times == 13 || times == 14) {
            return delay500ms();
        } else {
            return delay1000ms();
        }
    }

    public static ExitCode delay(int delayTime, TimeUnit delayUnit) {
        ExitCode exitCode = new ExitCode();
        exitCode.useDelay = true;
        exitCode.delayTime = delayTime;
        exitCode.delayUnit = delayUnit;
        return exitCode;
    }

    public static ExitCode finish() {
        return FINISH;
    }
}
