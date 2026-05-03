package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class WsQueryInfoMsg extends WsResMsg {

    private String       line;
    private String       script;
    private String       original;
    private List<String> rewriteTags;
}
