package com.clougence.clouddm.dsfamily.mysql.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class MyDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> FAMILY_CONNECT_ERROR_MESSAGES;
    protected static final List<String> FAMILY_AUTH_ERROR_MESSAGES;

    static {
        FAMILY_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        FAMILY_CONNECT_ERROR_MESSAGES.add("Expected to read 4 bytes, read 0 bytes before connection was unexpectedly lost.");
        FAMILY_CONNECT_ERROR_MESSAGES.add("The driver has not received any packets from the server.");

        FAMILY_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        FAMILY_AUTH_ERROR_MESSAGES.add("password authentication failed for user");
        FAMILY_AUTH_ERROR_MESSAGES.add("Access denied for user");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return FAMILY_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return FAMILY_AUTH_ERROR_MESSAGES; }
}
