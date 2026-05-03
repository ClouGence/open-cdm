package com.clougence.utils.future;

/**
 *
 * @version : 2014年11月15日
 * @author 赵永春 (zyc@hasor.net)
 */
public interface CancellFutureCallback<T> extends FutureCallback<T> {

    /** when cancelled */
    void cancelled();
}
