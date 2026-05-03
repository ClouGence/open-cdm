package com.clougence.clouddm.console.web.component.asyntask;

/**
 * Low-latency task distribution
 *
 *     //    INIT          : cancel、
 *     //    BLOCK         : cancel、pause
 *     //    WAIT_START    :
 *     //    RUNNING       : cancel、pause
 *     //    FAILURE       : retry
 *     //    CANCEL        : retry
 *     //    COMPLETE      :
 *     //    PAUSE         : resume
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-10-09
 */
public interface AsyncTaskScheduleService {

    void trigger();

    void pauseTask(long asyncTaskId, String reasons);

    void cancelTask(long asyncTaskId, String reasons);

    void resumeTask(long asyncTaskId, String reasons);

    void retryTask(long asyncTaskId, String reasons);
}
