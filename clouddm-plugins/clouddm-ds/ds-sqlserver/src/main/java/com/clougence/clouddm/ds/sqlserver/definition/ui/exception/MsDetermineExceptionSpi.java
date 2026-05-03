package com.clougence.clouddm.ds.sqlserver.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class MsDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> MS_CONNECT_ERROR_MESSAGES;
    protected static final List<String> MS_AUTH_ERROR_MESSAGES;

    static {
        MS_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        MS_CONNECT_ERROR_MESSAGES.add("Make sure that TCP connections to the port are not blocked by a firewall.");
        MS_CONNECT_ERROR_MESSAGES.add("TCP/IP 连接失败");
        MS_CONNECT_ERROR_MESSAGES.add("Verify the connection properties");

        MS_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        MS_AUTH_ERROR_MESSAGES.add("登录失败");
        MS_AUTH_ERROR_MESSAGES.add("Login failed for user");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return MS_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return MS_AUTH_ERROR_MESSAGES; }
}
