package com.clougence.schema.umi.special.rdb;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.constraint.Check;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbCheckConstraint extends Check {

    private String expression;
}
