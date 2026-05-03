package com.clougence.clouddm.ds.hana.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;
import com.clougence.clouddm.sdk.ui.exception.ConnectionExceptionType;

public class HanaDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> HANA_CONNECT_ERROR_MESSAGES;
    protected static final List<String> HANA_AUTH_ERROR_MESSAGES;

    static {
        HANA_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        HANA_CONNECT_ERROR_MESSAGES.add("UnresolvedAddressException");
        HANA_CONNECT_ERROR_MESSAGES.add("SAP DBTech JDBC: Cannot connect");

        HANA_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        HANA_AUTH_ERROR_MESSAGES.add("authentication failed");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return HANA_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return HANA_AUTH_ERROR_MESSAGES; }

    @Override
    public ConnectionExceptionType checkExceptionType(Throwable error) {
        ConnectionExceptionType errorType = super.checkExceptionType(error);
        if (errorType != ConnectionExceptionType.Other) {
            return errorType;
        }
        return super.checkExceptionType(error.getCause());
    }
}
