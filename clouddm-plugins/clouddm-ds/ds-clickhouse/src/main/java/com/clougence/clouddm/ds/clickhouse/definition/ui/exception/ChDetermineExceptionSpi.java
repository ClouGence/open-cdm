package com.clougence.clouddm.ds.clickhouse.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class ChDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> CH_CONNECT_ERROR_MESSAGES;
    protected static final List<String> CH_AUTH_ERROR_MESSAGES;

    static {
        CH_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        CH_CONNECT_ERROR_MESSAGES.add("nodename nor servname provided, or not known");

        CH_AUTH_ERROR_MESSAGES = new ArrayList<>();
        CH_AUTH_ERROR_MESSAGES.add("Authentication failed");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return CH_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return CH_AUTH_ERROR_MESSAGES; }
}
