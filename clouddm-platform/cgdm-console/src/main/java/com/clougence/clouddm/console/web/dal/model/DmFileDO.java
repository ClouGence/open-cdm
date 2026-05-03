package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.FileStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_files")
public class DmFileDO {

    @TableId(type = IdType.AUTO)
    private Long       id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date       gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date       gmtModified;

    private String     ownerUid;

    private String     userId;

    private String     fileUri;

    private String     fileFormat;

    private Boolean    innerFormat;

    private FileStatus status;

    private String     message;

    private String     queryId;

    private String     uniqueId;

    private Date       heartbeat;

    private int        tryCount;
}
