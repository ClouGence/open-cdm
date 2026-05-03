package com.clougence.schema.umi.special.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.struts.AttributeUmiData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RdbPartitionItem extends AttributeUmiData {

    private String                 name;
    private String                 description;
    private String                 tablespace;
    private String                 partitionMethod;
    private String                 comment;

    private Map<String, String>    valueMap;

    private List<RdbPartitionItem> subItems = new ArrayList<>();
}
