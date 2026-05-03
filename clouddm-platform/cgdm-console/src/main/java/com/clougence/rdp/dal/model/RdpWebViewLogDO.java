package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

/**
 * @author wanshao create time is 2019/12/11 10:10 下午
 **/

@Data
@TableName(value = "rdp_web_view_log")
public class RdpWebViewLogDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    private String uid;

    /**
     * referrer
     */
    private String src;

    /**
     * kw
     */
    private String keyword;

    /**
     * baidu vb_id
     */
    private String vbId;

    private String uri;

    /**
     * browser client_id
     */
    private String clientId;
}
