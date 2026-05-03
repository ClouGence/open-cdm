package com.clougence.rdp.component.resulttask;

import java.util.concurrent.Callable;

import com.clougence.utils.future.CgFuture;

/**
 *  for Asynchronous task wait result and not strongly required to return results
 */
public interface AsyncTaskWithResultService {

    <T> CgFuture<T> submitTask(String key, Callable<T> task);
}
