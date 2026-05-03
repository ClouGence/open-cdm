package com.clougence.rdp.dal.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-05-28 16:20
 */
@Getter
@Setter
public class RdpTicketApproPersonDO {

    private String       uid;

    private String       username;

    private Long         resId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roleAuthLabels;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> resAuthLabel;
}
