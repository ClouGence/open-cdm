package com.clougence.clouddm.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static final Logger logger = LoggerFactory.getLogger("third-party-logger");

    public static Logger getLoggerAppender() { return logger; }

}
