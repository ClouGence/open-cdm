package com.clougence.schema.umi.struts;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public interface UmiConstraint {

    String getName();

    UmiConstraintType getConstraintType();
}
