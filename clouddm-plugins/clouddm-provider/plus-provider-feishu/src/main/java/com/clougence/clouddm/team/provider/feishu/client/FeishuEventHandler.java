package com.clougence.clouddm.team.provider.feishu.client;

import com.lark.oapi.core.request.EventReq;

public interface FeishuEventHandler {

    FeishuEventHandler EMPTY = event -> {

    };

    void onEvent(EventReq event);
}
