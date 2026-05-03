package com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;

public class ObDetermineExceptionSpi extends MyDetermineExceptionSpi {

    protected static final List<String> OB4MY_CONNECT_ERROR_MESSAGES;
    protected static final List<String> OB4MY_AUTH_ERROR_MESSAGES;

    static {
        OB4MY_CONNECT_ERROR_MESSAGES = new ArrayList<>(FAMILY_CONNECT_ERROR_MESSAGES);

        OB4MY_AUTH_ERROR_MESSAGES = new ArrayList<>(FAMILY_AUTH_ERROR_MESSAGES);
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return OB4MY_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return OB4MY_AUTH_ERROR_MESSAGES; }
}
