package com.clougence.rdp.controller.model.vo;

import java.util.List;

import lombok.Data;

/**
 * @author wanshao create time is 2020/4/16
 **/
@Data
public class AlertEventListVO {

    List<AlertEventLogVO> alertEventLogVOList;

    long                  totalCount;
}
