package com.clougence.clouddm.comm.component;

/**
 * Decide how rsocket handle exception
 *
 * @author wanshao create time is 2021/1/7
 **/
public interface RSocketExceptionManager {

    void saveExcOrAlert(Throwable throwable);
}
