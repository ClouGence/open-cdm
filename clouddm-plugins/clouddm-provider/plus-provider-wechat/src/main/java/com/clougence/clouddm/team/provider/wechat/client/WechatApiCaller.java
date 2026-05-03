package com.clougence.clouddm.team.provider.wechat.client;

public interface WechatApiCaller<T> {

    T call(WechatClient client) throws Exception;
}
