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
package com.clougence.rdp.feishu;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.lark.oapi.Client;
import com.lark.oapi.service.authen.v1.model.CreateOidcAccessTokenReq;
import com.lark.oapi.service.authen.v1.model.CreateOidcAccessTokenReqBody;

public class FeiShuTest {

    @Test
    @Ignore("Manual smoke test for Feishu OpenAPI connectivity.")
    public void sdkShouldReachFeishuOpenApi() throws Exception {
        Client client = Client.newBuilder("probe", "probe").requestTimeout(5, java.util.concurrent.TimeUnit.SECONDS).build();
        CreateOidcAccessTokenReq req = CreateOidcAccessTokenReq.newBuilder()
            .createOidcAccessTokenReqBody(CreateOidcAccessTokenReqBody.newBuilder().grantType("authorization_code").code("probe").build())
            .build();

        try {
            client.authen().oidcAccessToken().create(req);
        } catch (Throwable e) {
            Assert.assertFalse("Feishu SDK request timed out: " + e, isTimeout(e));
            return;
        }

        Assert.fail("Unexpected success with probe app credentials.");
    }

    private static boolean isTimeout(Throwable e) {
        Throwable cursor = e;
        while (cursor != null) {
            String className = cursor.getClass().getName();
            String message = cursor.getMessage();
            if (className.contains("Timeout") || (message != null && message.toLowerCase().contains("timeout"))) {
                return true;
            }
            cursor = cursor.getCause();
        }
        return false;
    }
}
