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
