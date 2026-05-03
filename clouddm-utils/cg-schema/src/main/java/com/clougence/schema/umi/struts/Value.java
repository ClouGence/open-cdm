package com.clougence.schema.umi.struts;

import java.util.Map;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * a umi data as be value
 * 
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-05-21
 */
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public interface Value extends UmiData {

    Map<String, String> getAttributes();

    String asValue();

    UmiTypes getUmiType();
}
