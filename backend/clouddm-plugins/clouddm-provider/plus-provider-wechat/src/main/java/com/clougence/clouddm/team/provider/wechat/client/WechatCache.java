/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.team.provider.wechat.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.felord.WeComTokenCacheable;
import lombok.Getter;

public class WechatCache implements WeComTokenCacheable {

    @Getter
    private static WechatCache  instance = new WechatCache();
    private Map<String, String> cache    = new ConcurrentHashMap<>();

    @Override
    public String putCorpTicket(String corpId, String agentId, String corpTicket) {
        String key = "cropTicket" + "-" + corpId + "-" + agentId;
        cache.put(key, corpTicket);
        return corpTicket;
    }

    @Override
    public String getCorpTicket(String corpId, String agentId) {
        String key = "cropTicket" + "-" + corpId + "-" + agentId;
        return cache.get(key);
    }

    @Override
    public String putAgentTicket(String corpId, String agentId, String agentTicket) {
        String key = "agentTicket" + "-" + corpId + "-" + agentId;
        cache.put(key, agentTicket);
        return agentTicket;
    }

    @Override
    public String getAgentTicket(String corpId, String agentId) {
        String key = "agentTicket" + "-" + corpId + "-" + agentId;
        return cache.get(key);
    }

    @Override
    public String putAccessToken(String corpId, String agentId, String accessToken) {
        String key = "accessToken" + "-" + corpId + "-" + agentId;
        cache.put(key, accessToken);
        return accessToken;
    }

    @Override
    public String getAccessToken(String corpId, String agentId) {
        String key = "accessToken" + "-" + corpId + "-" + agentId;
        return cache.get(key);
    }

    @Override
    public void clearAccessToken(String corpId, String agentId) {
        String key1 = "cropTicket" + "-" + corpId + "-" + agentId;
        String key2 = "accessToken" + "-" + corpId + "-" + agentId;
        String key3 = "agentTicket" + "-" + corpId + "-" + agentId;
        cache.remove(key1);
        cache.remove(key2);
        cache.remove(key3);
    }
}
