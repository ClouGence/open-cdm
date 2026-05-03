package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.Date;

import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskProcessType;
import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Getter
@Setter
public class DmAsyncTaskVO {

    private Long                   id;
    private String                 title;
    private String                 description;
    private String                 bizId;
    private String                 bizType;
    private DmAsyncTaskProcessType processType;
    private String                 processValue;
    private DmAsyncTaskStatus      status;
    private String                 statusMsg;
    private Date                   timeOfStart;
    private Date                   timeOfLast;
    private Date                   timeOfFinish;
}
