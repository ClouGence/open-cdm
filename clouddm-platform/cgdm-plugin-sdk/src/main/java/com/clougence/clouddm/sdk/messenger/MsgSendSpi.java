package com.clougence.clouddm.sdk.messenger;

import com.clougence.clouddm.sdk.Spi;

/**
 * @author mode create time is 2019/12/12 9:36 下午
 **/
public interface MsgSendSpi extends Spi {

    String name();

    String getServiceUrl();

    String getHelpUrl();

    MsgSendResult sendMessage(MsgSendConfig config, MsgContent message);
}
