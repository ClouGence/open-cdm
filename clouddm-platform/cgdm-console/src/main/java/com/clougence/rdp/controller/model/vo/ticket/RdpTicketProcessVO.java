package com.clougence.rdp.controller.model.vo.ticket;

import java.util.List;

import com.clougence.rdp.dal.enumeration.RdpTicketProcessStatus;
import com.clougence.rdp.dal.enumeration.RdpTicketStage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpTicketProcessVO {

    private Long                      ticketProcessId;

    private String                    gmtCreate;

    private String                    gmtModified;

    private RdpTicketStage            ticketStage;

    private String                    ticketStageTitle;

    private String                    finishTime;

    private RdpTicketProcessStatus    ticketProcessStatus;

    private String                    stageContext;

    private boolean                   hasActivity;

    private List<RdpTicketActivityVO> activityList;

}
