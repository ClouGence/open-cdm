package com.clougence.clouddm.ds.tidb.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;

public class TiDetermineExceptionSpi extends MyDetermineExceptionSpi {

    protected static final List<String> TI_CONNECT_ERROR_MESSAGES;
    protected static final List<String> TI_AUTH_ERROR_MESSAGES;

    static {
        TI_CONNECT_ERROR_MESSAGES = new ArrayList<>(FAMILY_CONNECT_ERROR_MESSAGES);

        TI_AUTH_ERROR_MESSAGES = new ArrayList<>(FAMILY_AUTH_ERROR_MESSAGES);
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return TI_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return TI_AUTH_ERROR_MESSAGES; }
}
