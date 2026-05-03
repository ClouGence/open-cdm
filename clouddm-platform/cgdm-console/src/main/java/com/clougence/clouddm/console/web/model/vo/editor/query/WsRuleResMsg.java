package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class WsRuleResMsg extends WsResMsg {

    private String             queryBody;
    private List<WsRuleEntity> message;
    private WarnLevel          warnLevel;
}
