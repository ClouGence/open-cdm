package com.clougence.clouddm.sdk.ui.exception;

import com.clougence.clouddm.sdk.Spi;

public interface DetermineExceptionSpi extends Spi {

    ConnectionExceptionType checkExceptionType(Throwable error);
}
