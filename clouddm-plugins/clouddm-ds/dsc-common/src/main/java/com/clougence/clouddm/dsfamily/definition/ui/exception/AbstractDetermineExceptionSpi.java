package com.clougence.clouddm.dsfamily.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.ui.exception.ConnectionExceptionType;
import com.clougence.clouddm.sdk.ui.exception.DetermineExceptionSpi;
import com.clougence.utils.StringUtils;

public abstract class AbstractDetermineExceptionSpi implements DetermineExceptionSpi {

    protected static final List<String> BASIC_CONNECT_ERROR_MESSAGES;
    protected static final List<String> BASIC_AUTH_ERROR_MESSAGES;

    static {
        BASIC_CONNECT_ERROR_MESSAGES = new ArrayList<>();
        BASIC_CONNECT_ERROR_MESSAGES.add("UnknownHostException");
        BASIC_CONNECT_ERROR_MESSAGES.add("Host is down");
        BASIC_CONNECT_ERROR_MESSAGES.add("No route to host");
        BASIC_CONNECT_ERROR_MESSAGES.add("Connection refused");
        BASIC_CONNECT_ERROR_MESSAGES.add("Connection reset");
        BASIC_CONNECT_ERROR_MESSAGES.add("Connection reset by peer");
        BASIC_CONNECT_ERROR_MESSAGES.add("Network is unreachable (connect failed)");
        BASIC_CONNECT_ERROR_MESSAGES.add("Connect timed out");
        BASIC_CONNECT_ERROR_MESSAGES.add("Connection timed out");
        BASIC_AUTH_ERROR_MESSAGES = new ArrayList<>();
    }

    @Override
    public ConnectionExceptionType checkExceptionType(Throwable error) {
        if (error.getMessage() == null) {
            return ConnectionExceptionType.Other;
        }
        for (String msg : this.getConnectionExceptionMessage()) {
            if (StringUtils.containsIgnoreCase(error.getMessage(), msg)) {
                return ConnectionExceptionType.ConnectionRefused;
            }
        }
        for (String msg : getAuthenticationExceptionMessage()) {
            if (StringUtils.containsIgnoreCase(error.getMessage(), msg)) {
                // next version change to AUTH
                return ConnectionExceptionType.Authentication;
            }
        }
        return ConnectionExceptionType.Other;
    }

    protected abstract List<String> getConnectionExceptionMessage();

    protected List<String> getAuthenticationExceptionMessage() { return new ArrayList<>(); }
}
