package com.clougence.clouddm.console.web.model.vo.ticket;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmQueryTicketVO {

    private Long            id;

    private String          bizId;

    private String          gmtCreate;

    private String          gmtModified;

    private Long            dataSourceId;

    private String          rawSql;

    private SecQueryType    sqlType;

    private Integer         totalCount;

    private String          description;

    private String          statusMessage;

    private Long            expectedAffectedRows;

    private Boolean         immediately;

    private String          rollBackSql;

    private String          ticketMessage;

    private boolean         autoExec;

    private List<CheckedVO> checkedList;
}
