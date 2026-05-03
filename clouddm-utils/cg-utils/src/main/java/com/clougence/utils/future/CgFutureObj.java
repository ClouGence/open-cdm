package com.clougence.utils.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Basic implementation of the {@link CgFuture} interface. <tt>BasicFuture<tt>
 * can be put into a completed state by invoking any of the following methods:
 * {@link #cancel()}, {@link #failed(Throwable)}, or {@link #completed(Object)}.
 *
 * @param <T> the future result type of an asynchronous operation.
 * @author 赵永春 (zyc@hasor.net)
 * @since 4.2
 */
public class CgFutureObj<T> implements CgFuture<T> {

    private final FutureCallback<T>                      callback;
    private volatile List<CgFutureListener<CgFuture<T>>> completedListener;
    private volatile List<CgFutureListener<CgFuture<T>>> failedListener;
    private volatile List<CgFutureListener<CgFuture<T>>> cancelListener;
    private volatile List<CgFutureListener<CgFuture<T>>> finalListener;
    private volatile boolean                             completed;
    private volatile boolean                             cancelled;
    private volatile T                                   result;
    private volatile Throwable                           ex;

    public CgFutureObj(){
        super();
        this.callback = null;
    }

    public CgFutureObj(T value){
        this();
        this.completed = true;
        this.result = value;
    }

    public CgFutureObj(final FutureCallback<T> callback){
        super();
        this.callback = callback;
    }

    @Override
    public boolean isCancelled() { return this.cancelled; }

    @Override
    public boolean isDone() { return this.completed; }

    @Override
    public Throwable getCause() { return this.ex; }

    @Override
    public T getResult() { return (this.ex != null) ? null : this.result; }

    private T result() throws ExecutionException {
        if (this.ex != null) {
            throw new ExecutionException(this.ex.getMessage(), this.ex);
        }
        return this.result;
    }

    @Override
    public synchronized T get() throws InterruptedException, ExecutionException {
        while (!this.completed) {
            wait();
        }
        return result();
    }

    @Override
    public synchronized T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null) {
            throw new NullPointerException("Time unit");
        }

        final long msecs = unit.toMillis(timeout);
        final long startTime = (msecs <= 0) ? 0 : System.currentTimeMillis();
        long waitTime = msecs;
        if (this.completed) {
            return result();
        } else if (waitTime <= 0) {
            throw new TimeoutException();
        } else {
            for (;;) {
                wait(waitTime);
                if (this.completed) {
                    return result();
                } else {
                    waitTime = msecs - (System.currentTimeMillis() - startTime);
                    if (waitTime <= 0) {
                        throw new TimeoutException();
                    }
                }
            }
        }
    }

    @Override
    public boolean completed(final T result) {
        if (this.completed) {
            return false;
        }

        synchronized (this) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.result = result;

            if (this.callback != null) {
                this.callback.completed(result);
            }

            this.triggerListener(this.completedListener);
            this.triggerListener(this.finalListener);

            notifyAll();
        }
        return true;
    }

    @Override
    public boolean failed(final Throwable exception) {
        if (this.completed) {
            return false;
        }

        synchronized (this) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.ex = exception;

            if (this.callback != null) {
                this.callback.failed(exception);
            }

            this.triggerListener(this.failedListener);
            this.triggerListener(this.finalListener);

            notifyAll();
        }
        return true;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (this.completed) {
            return false;
        }

        synchronized (this) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.cancelled = true;

            if (this.callback != null) {
                if (this.callback instanceof CancellFutureCallback) {
                    ((CancellFutureCallback) this.callback).cancelled();
                } else {
                    this.failed(new CancellationException());
                    return true;
                }
            }

            this.triggerListener(this.cancelListener);
            this.triggerListener(this.finalListener);

            notifyAll();
        }
        return true;
    }

    @Override
    public boolean cancel() {
        return cancel(true);
    }

    private void triggerListener(List<CgFutureListener<CgFuture<T>>> listenerList) {
        if (listenerList != null) {
            for (CgFutureListener<CgFuture<T>> callback : listenerList) {
                try {
                    callback.operationComplete(this);
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public CgFuture<T> onCompleted(CgFutureListener<CgFuture<T>> listener) {
        if (listener == null) {
            return this;
        }

        synchronized (this) {
            if (this.completed && !this.cancelled && this.ex == null) {
                listener.operationComplete(this);
                return this;
            }

            if (this.completedListener == null) {
                if (this.completedListener == null) {
                    this.completedListener = new ArrayList<>();
                }
            }

            this.completedListener.add(listener);
            return this;
        }
    }

    @Override
    public CgFuture<T> onFailed(CgFutureListener<CgFuture<T>> listener) {
        if (listener == null) {
            return this;
        }

        synchronized (this) {
            if (this.completed && !this.cancelled && this.ex != null) {
                listener.operationComplete(this);
                return this;
            }

            if (this.failedListener == null) {
                if (this.failedListener == null) {
                    this.failedListener = new ArrayList<>();
                }
            }

            this.failedListener.add(listener);
            return this;
        }
    }

    @Override
    public CgFuture<T> onCancel(CgFutureListener<CgFuture<T>> listener) {
        if (listener == null) {
            return this;
        }

        synchronized (this) {
            if (this.completed && this.cancelled) {
                listener.operationComplete(this);
                return this;
            }

            if (this.cancelListener == null) {
                if (this.cancelListener == null) {
                    this.cancelListener = new ArrayList<>();
                }
            }

            this.cancelListener.add(listener);
            return this;
        }
    }

    @Override
    public CgFuture<T> onFinal(CgFutureListener<CgFuture<T>> listener) {
        if (listener == null) {
            return this;
        }

        synchronized (this) {
            if (this.completed) {
                listener.operationComplete(this);
                return this;
            }

            if (this.finalListener == null) {
                if (this.finalListener == null) {
                    this.finalListener = new ArrayList<>();
                }
            }

            this.finalListener.add(listener);
            return this;
        }
    }
}
