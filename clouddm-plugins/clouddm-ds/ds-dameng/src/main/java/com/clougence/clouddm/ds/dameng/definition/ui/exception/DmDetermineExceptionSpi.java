package com.clougence.clouddm.ds.dameng.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class DmDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> DM_CONNECT_ERROR_MESSAGES;
    protected static final List<String> DM_AUTH_ERROR_MESSAGES;

    static {
        DM_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        DM_CONNECT_ERROR_MESSAGES.add("网络通信异常");

        DM_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        DM_AUTH_ERROR_MESSAGES.add("用户名或密码错误");
        DM_AUTH_ERROR_MESSAGES.add("Invalid username or password");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return DM_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return DM_AUTH_ERROR_MESSAGES; }
}
