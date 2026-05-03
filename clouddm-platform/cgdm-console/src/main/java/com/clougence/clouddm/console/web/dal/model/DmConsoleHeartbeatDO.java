package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Deprecated
@TableName(value = "dm_console_heartbeat")
public class DmConsoleHeartbeatDO {

    @TableId(type = IdType.AUTO)
    private Long    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtModified;

    private String  consoleIp;

    private String  macAddress;

    private Boolean active;

    /**
     * json
     */
    private String  cpuStat;

    /**
     * json
     */
    private String  memStat;

    /**
     * json
     */
    private String  diskStat;

    private String  version;

    private Date    consoleSendTime;

    /**
     * Server Hardware Uuid
     */
    private String  hardwareUuid;
}
