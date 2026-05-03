package com.clougence.schema.umi.struts;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 参数类型
 * 
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-05-21
 */
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public interface UmiData {

    Object unwrap();
}
