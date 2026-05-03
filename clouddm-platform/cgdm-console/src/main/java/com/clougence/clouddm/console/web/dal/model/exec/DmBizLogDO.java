package com.clougence.clouddm.console.web.dal.model.exec;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;
import com.clougence.clouddm.console.web.dal.enumeration.Loglevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_biz_log")
public class DmBizLogDO {

    @TableId(type = IdType.AUTO)
    private Long               id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date               gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date               gmtModified;

    private String             dependOnBizId;

    private DmLogDependBizType dependOnBizType;

    private String             content;

    private Loglevel           logLevel;

    public DmBizLogDO(Loglevel logLevel, String content, DmLogDependBizType dependOnBizType, String dependOnBizId){
        this.logLevel = logLevel;
        this.content = content;
        this.dependOnBizType = dependOnBizType;
        this.dependOnBizId = dependOnBizId;
    }

    public DmBizLogDO(){
    }

}
