package com.clougence.schema.umi.struts.constraint;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.UmiConstraintType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class NonNull extends ConstraintObject {

    @Override
    public final UmiConstraintType getConstraintType() { return GeneralConstraintType.NonNull; }
}
