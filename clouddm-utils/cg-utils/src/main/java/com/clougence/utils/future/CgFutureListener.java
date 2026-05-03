package com.clougence.utils.future;

import java.util.EventListener;
import java.util.concurrent.Future;

/**
 *
 * @version : 2014年11月15日
 * @author 赵永春 (zyc@hasor.net)
 */
@FunctionalInterface
public interface CgFutureListener<F extends Future<?>> extends EventListener {

    /**
     * Invoked when the operation associated with the {@link Future} has been completed.
     * @param future  the source {@link Future} which called this callback
     */
    void operationComplete(F future);
}
