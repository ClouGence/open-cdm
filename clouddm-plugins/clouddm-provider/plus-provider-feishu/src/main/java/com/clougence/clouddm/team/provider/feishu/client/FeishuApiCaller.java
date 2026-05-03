package com.clougence.clouddm.team.provider.feishu.client;

public interface FeishuApiCaller<T> {

    T call(FeishuClient client) throws Exception;
}
