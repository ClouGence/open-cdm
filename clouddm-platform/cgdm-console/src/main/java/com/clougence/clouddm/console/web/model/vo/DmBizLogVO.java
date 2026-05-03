package com.clougence.clouddm.console.web.model.vo;

import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;
import com.clougence.clouddm.console.web.dal.enumeration.Loglevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmBizLogVO {

    private Long               id;

    private String             time;

    private String             dependOnBizId;

    private DmLogDependBizType dependOnBizType;

    private String             content;

    private Loglevel           logLevel;
}
