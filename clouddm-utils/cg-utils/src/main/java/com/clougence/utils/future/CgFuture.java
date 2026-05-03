package com.clougence.utils.future;

import java.util.concurrent.Future;

/**
 * Basic implementation of the {@link java.util.concurrent.Future} interface. <tt>BasicFuture<tt>
 * can be put into a completed state by invoking any of the following methods:
 * {@link #cancel()}, {@link #failed(Throwable)}, or {@link #completed(Object)}.
 *
 * @param <T> the future result type of an asynchronous operation.
 */
public interface CgFuture<T> extends Future<T>, Cancellable {

    /** Don't block, don't throw exceptions, get results Return result if completed, empty if failed or cancel.*/
    T getResult();

    /** Blocking waits until success or failure */
    default void await() {
        try {
            this.get();
        } catch (Exception ignored) {
        }
    }

    Throwable getCause();

    boolean completed(final T result);

    boolean failed(final Throwable exception);

    @Override
    boolean cancel(final boolean mayInterruptIfRunning);

    boolean cancel();

    CgFuture<T> onCompleted(CgFutureListener<CgFuture<T>> listener);

    CgFuture<T> onFailed(CgFutureListener<CgFuture<T>> listener);

    CgFuture<T> onCancel(CgFutureListener<CgFuture<T>> listener);

    CgFuture<T> onFinal(CgFutureListener<CgFuture<T>> listener);
}
