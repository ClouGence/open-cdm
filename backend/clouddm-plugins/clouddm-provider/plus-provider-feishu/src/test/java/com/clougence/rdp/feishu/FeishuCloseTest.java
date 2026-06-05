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
package com.clougence.rdp.feishu;

import com.clougence.clouddm.team.provider.feishu.client.FeishuClient;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeishuCloseTest {

    public static void main(String[] args) throws Exception {
        Thread.currentThread().setName("ROOT");
        FeishuClient feishuClient1 = new FeishuClient(log, "cli_a7c3966dce61900e", "zI0LJaQ2yYGg6XjmsSFlLgGqgTNESg38", 5, null);
        feishuClient1.start(true);

        FeishuClient feishuClient2 = new FeishuClient(log, "cli_a7c3966dce61900e", "zI0LJaQ2yYGg6XjmsSFlLgGqgTNESg38", 5, null);
        feishuClient2.start(true);

        System.out.println("close wait 5s");
        Thread.sleep(10000);

        System.out.println("close all");
        feishuClient1.close();
        feishuClient2.close();

        while (true) {
            ThreadUtils.safeSleep(1000);
        }
    }
}
