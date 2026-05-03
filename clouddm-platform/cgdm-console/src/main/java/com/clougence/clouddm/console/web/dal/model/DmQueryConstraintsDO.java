package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.console.web.dal.enumeration.QueryConstraintType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_query_constraints")
public class DmQueryConstraintsDO {

    @TableId(type = IdType.AUTO)
    private Long             id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;
    private String           primaryUid;
    private Long             dsId;
    private String           path;
    @TableField(value = "constraints_json", typeHandler = JacksonTypeHandler.class)
    private List<Constraint> constraints;

    @Getter
    @Setter
    public static class Constraint {

        private String              column;
        private QueryConstraintType type;
        private String              config;
    }
}
