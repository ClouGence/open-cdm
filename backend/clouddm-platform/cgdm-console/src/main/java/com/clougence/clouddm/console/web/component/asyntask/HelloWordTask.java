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
package com.clougence.clouddm.console.web.component.asyntask;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
@Slf4j
@Service
@Scope("prototype")
public class HelloWordTask extends AsyncTask {

    private int cnt;

    @Override
    protected void executeTask(int doCnt, String configData) {
        if (this.isInterrupted()) {
            String msg = "task break cnt " + (cnt++);

            this.updateMessage(msg);
            log.info(msg);
            this.finishTask(null);
            return;
        }

        if (cnt >= 100) {
            String msg = "task finish.";

            this.updateMessage(msg);
            log.info(msg);
            this.finishTask(msg);
        } else {
            String msg = "task cnt " + (cnt++);

            this.updateMessage(msg);
            log.info(msg);
            this.delayTask(1, TimeUnit.SECONDS);
        }
    }
}
