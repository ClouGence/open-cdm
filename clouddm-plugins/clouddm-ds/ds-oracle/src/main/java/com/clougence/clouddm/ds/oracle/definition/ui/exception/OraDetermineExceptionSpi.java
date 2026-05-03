package com.clougence.clouddm.ds.oracle.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class OraDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> ORA_CONNECT_ERROR_MESSAGES;
    protected static final List<String> ORA_AUTH_ERROR_MESSAGES;

    static {
        ORA_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        ORA_CONNECT_ERROR_MESSAGES.add("The Network Adapter could not establish the connection");
        ORA_CONNECT_ERROR_MESSAGES.add("Operation timed out");
        ORA_CONNECT_ERROR_MESSAGES.add("Socket read timed out");

        ORA_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        ORA_AUTH_ERROR_MESSAGES.add("ORA-01017: invalid username/password;");
        ORA_AUTH_ERROR_MESSAGES.add("ORA-01031: insufficient privileges");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return ORA_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return ORA_AUTH_ERROR_MESSAGES; }
}
