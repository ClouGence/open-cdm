package com.clougence.schema.umi.special.rdb;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.AttributeUmiData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbPartition extends AttributeUmiData {

    // main partition
    private String                 partitionMethod;
    private String                 partitionExpression;
    private Boolean                manualPartition;
    private Long                   partitionCount;
    // subpartition
    private String                 subPartitionMethod;
    private String                 subPartitionExpression;
    private Boolean                subManualPartition;
    private Long                   subPartitionCount;

    private List<RdbPartitionDef>  ptDefs  = new ArrayList<>();
    private List<RdbPartitionItem> ptItems = new ArrayList<>();
}
