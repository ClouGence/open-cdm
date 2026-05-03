package com.clougence.clouddm.console.web.service.editor.query;

import java.util.function.Consumer;

import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsResMsg;

public interface ConsoleQueryApi {

    void offerQueryRequest(WsQueryFO queryDTO, Consumer<WsResMsg> consumer);

    //void offerQueryResponse(ResultList result);
}
