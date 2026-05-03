package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.DsSessionType;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.utils.JsonUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_ds_session")
public class DmDsSessionDO {

    @TableId(type = IdType.AUTO)
    private Long          id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date          gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date          gmtModified;

    private Date          lastQueryTime;

    private String        uid;

    private String        sessionId;

    private DsSessionType sessionType;

    //private String        channelKeys;

    private String        wsn;

    private String        clusterId;

    private Long          datasourceId;

    private String        datasourceType;

    private String        config;

    private String        attach;

    public SessionContextDTO toRdbCtx() {
        SessionContextDTO contextDTO = JsonUtils.toObj(this.config, SessionContextDTO.class);
        contextDTO.setSessionId(this.sessionId);
        return contextDTO;
    }

    public ToolSessionContextDTO toToolCtx() {
        ToolSessionContextDTO contextDTO = JsonUtils.toObj(this.config, ToolSessionContextDTO.class);
        contextDTO.setSessionId(this.sessionId);
        return contextDTO;
    }
}
