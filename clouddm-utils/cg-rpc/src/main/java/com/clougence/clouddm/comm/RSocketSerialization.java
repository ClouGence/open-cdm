package com.clougence.clouddm.comm;

import java.lang.reflect.Type;

public interface RSocketSerialization {

    String encode(String provider, Object argData);

    Object decode(String provider, String jsonData, Type tryType);
}
