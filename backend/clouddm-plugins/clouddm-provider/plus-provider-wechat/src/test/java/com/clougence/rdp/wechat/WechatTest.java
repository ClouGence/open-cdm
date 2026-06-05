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
package com.clougence.rdp.wechat;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.team.provider.wechat.approval.WechatApprovalCallbackSpi;
import com.clougence.clouddm.team.provider.wechat.client.WechatClient;

import cn.felord.DefaultAgent;
import cn.felord.WeComTokenCacheable;
import cn.felord.api.ApprovalApi;
import cn.felord.api.WorkWeChatApi;
import cn.felord.domain.approval.ApprovalSpNo;

public class WechatTest {

    @Test
    public void test() {
        WorkWeChatApi workWeChatApi = new WorkWeChatApi(new WeComTokenCacheable() {

            @Override
            public String putCorpTicket(String corpId, String agentId, String corpTicket) {
                return corpTicket;
            }

            @Override
            public String getCorpTicket(String corpId, String agentId) {
                return null;
            }

            @Override
            public String putAgentTicket(String corpId, String agentId, String agentTicket) {
                return agentTicket;
            }

            @Override
            public String getAgentTicket(String corpId, String agentId) {
                return null;
            }

            @Override
            public String putAccessToken(String corpId, String agentId, String accessToken) {
                return accessToken;
            }

            @Override
            public String getAccessToken(String corpId, String agentId) {
                return null;
            }

            @Override
            public void clearAccessToken(String corpId, String agentId) {

            }
        });
        ApprovalApi approvalApi = workWeChatApi.approvalApi(DefaultAgent.of("ww47e80c1d7930f4aa", "-NMX64gEdBf0XbfwFzUGNQBN4LAgsgtMUOUknvCkC3Y", "1000002"));
        System.out.println(approvalApi.getApprovalDetail(new ApprovalSpNo("202412090001")));
    }

    @Test
    public void test2() {
        WechatApprovalCallbackSpi wechatEventCallbackService = new WechatApprovalCallbackSpi(null, null);
        Properties properties = new Properties();
        properties.setProperty("wechatApprovalToken", "Yxt1sGadg8safvUEuE4wMcGemuRm");
        properties.setProperty("wechatApprovalEncodingAesKey", "HpOzpdj1MbJMdUFjQd5mJo0tbvOwV0Aqlggup9N3sZO");
        properties.setProperty("wechatApprovalCorpId", "ww47e80c1d7930f4aa");

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("msg_signature", "a55fa28e7b254f09db769d8f098325aacbd61411");
        stringStringHashMap
            .put("requestBody", "<xml><ToUserName><![CDATA[ww47e80c1d7930f4aa]]></ToUserName><Encrypt><![CDATA[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]]></Encrypt><AgentID><![CDATA[1000002]]></AgentID></xml>");
        stringStringHashMap.put("nonce", "1733562308");
        stringStringHashMap.put("timestamp", "1733815971");
        wechatEventCallbackService.handle("123", /*JsonUtils.toJson(properties),*/ stringStringHashMap);
    }

    private static WechatClient createClientManager() {
        return new WechatClient(null, "ww47e80c1d7930f4aa", "1000002", "-NMX64gEdBf0XbfwFzUGNQBN4LAgsgtMUOUknvCkC3Y", null);
    }

    @Test
    public void getTemplate() {
        //WechatApi wechatApi = createClientManager("123").approvalApi("123");
        try {
            //  System.out.println(wechatApi.getTemplate("C4ZULB1Kd1rV1D5rVDRSjAq9RmHCD9FTqGq3S7fQVu"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        //        properties.setProperty("wechatApprovalToken","Yxt1sGadg8safvUEuE4wMcGemuRm");
        //        properties.setProperty("wechatApprovalEncodingAesKey","HpOzpdj1MbJMdUFjQd5mJo0tbvOwV0Aqlggup9N3sZO");
        //        properties.setProperty("wechatApprovalCorpId","ww47e80c1d7930f4aa");
        //        properties.setProperty("wechatApprovalSecret","-NMX64gEdBf0XbfwFzUGNQBN4LAgsgtMUOUknvCkC3Y");
        //        properties.setProperty("wechatApprovalAgentId","1000002");
        properties.setProperty("wechatApprovalToken", "Yxt1sGadg8safvUEuE4wMcGemuRm");
        properties.setProperty("wechatApprovalEncodingAesKey", "HpOzpdj1MbJMdUFjQd5mJo0tbvOwV0Aqlggup9N3sZO");
        properties.setProperty("wechatApprovalCorpId", "ww47e80c1d7930f4aa");
        properties.setProperty("wechatApprovalSecret", "-NMX64gEdBf0XbfwFzUGNQBN4LAgsgtMUOUknvCkC3Y");
        properties.setProperty("wechatApprovalAgentId", "1000002");
        return properties;
    }

    @Test
    public void getUserName() {
        WechatClient wechatApi = createClientManager();
        try {
            System.out.println(wechatApi.getBindApi().getUserIdByPhone("19974077527"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void create() {
        //        WechatClientManager clientManager = createClientManager("123");
        //        WechatApprovalProviderSpi wechatService = new WechatApprovalProviderSpi(clientManager);
        //        ApprovalCreateInstanceInfo info = new ApprovalCreateInstanceInfo();
        //        info.setTicketDesc("fwe");
        //        info.setTicketTitle("ew");
        //        info.setTemplateIdentity("few");
        //        try {
        //            wechatService.createApprovalInstance("123", info);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

    }

    @Test
    public void getDetail() throws InterruptedException {
        WechatClient wechatApi = this.createClientManager();
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            for (int i = 0; i < 10000; i++) {
                executorService.submit(() -> {
                    try {
                        System.out.println(1);
                        System.out.println(wechatApi.getBindApi().getInstanceDetail("202412110001"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(10000000);
    }
}
