package com.clougence.clouddm.console.web.model.vo.editor.query;

import lombok.Getter;

/**
 * @author mode 2020-01-20 12:28
 * @since 1.1.3
 */
@Getter
public class OperationSessionVO {

    private final String  sessionId;
    private final boolean reopen;

    public OperationSessionVO(String sessionId, boolean reopen){
        this.sessionId = sessionId;
        this.reopen = reopen;
    }
}
