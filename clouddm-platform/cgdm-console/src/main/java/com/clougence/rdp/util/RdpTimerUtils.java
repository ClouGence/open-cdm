package com.clougence.rdp.util;

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
