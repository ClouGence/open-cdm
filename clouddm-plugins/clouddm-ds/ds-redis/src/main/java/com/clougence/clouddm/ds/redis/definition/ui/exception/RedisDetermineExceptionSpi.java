package com.clougence.clouddm.ds.redis.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class RedisDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> REDIS_CONNECT_ERROR_MESSAGES;
    protected static final List<String> REDIS_AUTH_ERROR_MESSAGES;

    static {
        REDIS_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        REDIS_CONNECT_ERROR_MESSAGES.add("Failed to create socket");
        REDIS_CONNECT_ERROR_MESSAGES.add("Failed to connect to any host resolved for DNS name");
        REDIS_CONNECT_ERROR_MESSAGES.add("Failed to create socket");

        REDIS_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        REDIS_AUTH_ERROR_MESSAGES.add("WRONGPASS invalid username-password pair or user is disabled");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return REDIS_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return REDIS_AUTH_ERROR_MESSAGES; }
}
