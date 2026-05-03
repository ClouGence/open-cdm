package com.clougence.clouddm.ds.maxcompute.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class McDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> MC_CONNECT_ERROR_MESSAGES;
    protected static final List<String> MC_AUTH_ERROR_MESSAGES;

    static {
        MC_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        //MC_CONNECT_ERROR_MESSAGES.add("网络通信异常");

        MC_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        //MC_AUTH_ERROR_MESSAGES.add("用户名或密码错误");
        //MC_AUTH_ERROR_MESSAGES.add("Invalid username or password");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return MC_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return MC_AUTH_ERROR_MESSAGES; }
}
