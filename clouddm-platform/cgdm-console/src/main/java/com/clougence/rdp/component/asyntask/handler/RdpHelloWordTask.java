package com.clougence.rdp.component.asyntask.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.clougence.rdp.component.asyntask.RdpAsyncTask;

import lombok.extern.slf4j.Slf4j;

/**
 * default Task
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-09-24
 */
@Slf4j
@Service
@Scope("prototype")
public class RdpHelloWordTask extends RdpAsyncTask {

    private int cnt;

    @Override
    protected void executeTask(int doCnt, String configData) {
        if (this.isInterrupted()) {
            String msg = "task break cnt " + (cnt++);

            this.updateProcess(msg);
            log.info(msg);
            this.finishTask(null);
            return;
        }

        if (cnt >= 100) {
            String msg = "task finish.";

            this.updateProcess(msg);
            log.info(msg);
            this.finishTask(msg);
        } else {
            String msg = "task cnt " + (cnt++);

            this.updateProcess(msg);
            log.info(msg);
            this.delayTask(1, TimeUnit.SECONDS);
        }
    }
}
