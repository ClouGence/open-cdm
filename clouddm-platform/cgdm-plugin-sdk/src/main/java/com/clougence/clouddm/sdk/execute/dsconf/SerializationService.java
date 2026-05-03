package com.clougence.clouddm.sdk.execute.dsconf;

import java.lang.reflect.Type;

import com.clougence.clouddm.sdk.Spi;

public interface SerializationService extends Spi {

    String encode(Object argData);

    Object decode(String jsonData, Type tryType);
}
