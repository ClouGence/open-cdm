package com.clougence.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.utils.future.CgFutureObj;
import org.junit.jupiter.api.Test;

import com.clougence.utils.future.CgFuture;
import com.clougence.utils.future.CgFutureObj;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-11-01
 */
public class FutureTest {

    @Test
    public void submitSoTask_Test() throws ExecutionException, InterruptedException {

        int round = 5000;
        boolean result = true;

        while (round > 0) {
            round--;
            AtomicInteger cnt = new AtomicInteger(0);

            CgFuture<Object> future = new CgFutureObj().onCompleted(f -> {
                cnt.incrementAndGet();
            }).onFailed(f -> {
                //
            });

            ThreadUtils.daemonThread(Thread.currentThread().getContextClassLoader(), () -> {
                future.completed(new Object());
            }).start();

            future.get();
            int i = cnt.get();
            if (i != 1) {
                result = false;
                break;
            }
        }

        assert result;
    }
}
