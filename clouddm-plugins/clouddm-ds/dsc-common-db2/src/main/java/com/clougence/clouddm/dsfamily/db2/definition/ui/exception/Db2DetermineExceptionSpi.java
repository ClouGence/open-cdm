package com.clougence.clouddm.dsfamily.db2.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class Db2DetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> DB2_CONNECT_ERROR_MESSAGES;
    protected static final List<String> DB2_AUTH_ERROR_MESSAGES;

    static {
        DB2_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        DB2_CONNECT_ERROR_MESSAGES.add("nodename nor servname provided, or not known");
        DB2_CONNECT_ERROR_MESSAGES.add("The application requester cannot establish the connection. (Connection was dropped unexpectedly.)");
        DB2_CONNECT_ERROR_MESSAGES.add("ERRORCODE=-4499");
        DB2_CONNECT_ERROR_MESSAGES.add("ERRORCODE=-4222");

        DB2_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        DB2_AUTH_ERROR_MESSAGES.add("ERRORCODE=-4214, SQLSTATE=28000");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return DB2_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return DB2_AUTH_ERROR_MESSAGES; }
}
