package com.clougence.clouddm.ds.greenplum.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.exception.PgDetermineExceptionSpi;

public class GpDetermineExceptionSpi extends PgDetermineExceptionSpi {

    protected static final List<String> GP_CONNECT_ERROR_MESSAGES;
    protected static final List<String> GP_AUTH_ERROR_MESSAGES;

    static {
        GP_CONNECT_ERROR_MESSAGES = new ArrayList<>(FAMILY_CONNECT_ERROR_MESSAGES);
        GP_CONNECT_ERROR_MESSAGES.add("尝试连线已失败");

        GP_AUTH_ERROR_MESSAGES = new ArrayList<>(FAMILY_AUTH_ERROR_MESSAGES);
        GP_AUTH_ERROR_MESSAGES.add("FATAL: role");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return GP_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return GP_AUTH_ERROR_MESSAGES; }
}
