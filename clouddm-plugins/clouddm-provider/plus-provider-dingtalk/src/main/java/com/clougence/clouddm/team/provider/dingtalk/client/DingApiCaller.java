package com.clougence.clouddm.team.provider.dingtalk.client;

public interface DingApiCaller<T> {

    T call(DingClient client, String token) throws Exception;
}
