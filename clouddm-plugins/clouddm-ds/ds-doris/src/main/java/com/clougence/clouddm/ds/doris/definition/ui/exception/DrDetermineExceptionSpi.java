package com.clougence.clouddm.ds.doris.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;

public class DrDetermineExceptionSpi extends MyDetermineExceptionSpi {

    protected static final List<String> DR_CONNECT_ERROR_MESSAGES;
    protected static final List<String> DR_AUTH_ERROR_MESSAGES;

    static {
        DR_CONNECT_ERROR_MESSAGES = new ArrayList<>(FAMILY_CONNECT_ERROR_MESSAGES);

        DR_AUTH_ERROR_MESSAGES = new ArrayList<>(FAMILY_AUTH_ERROR_MESSAGES);
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return DR_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return DR_AUTH_ERROR_MESSAGES; }
}
