package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.utils.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/5/7 18:00
*/
@Getter
@Setter
@TableName(value = "dm_ticket_details_inst")
public class DmTicketDO {

    @TableId(type = IdType.AUTO)
    private Long         id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtModified;

    private String       rdpTicketInsId;

    private String       sessionId;

    private String       explainSqlData;

    private Integer      riskSqlCount;

    private String       rawSql;

    private Integer      totalCount;

    @TableField("`expected_affected_rows`")
    private Long         expectedAffectedRows;

    private String       rollBackSql;

    private Boolean      deleted;

    private String       ticketInfo;

    @TableField(value = "levels", typeHandler = JacksonTypeHandler.class)
    private List<String> levels;

    private String       checkedInfo;

    public String getLevelPath() {
        if (CollectionUtils.isEmpty(this.levels)) {
            return "/";
        }
        StringBuilder sb = new StringBuilder("/");
        for (String level : this.levels) {
            sb.append(level);
            sb.append("/");
        }
        return sb.toString();
    }

}
