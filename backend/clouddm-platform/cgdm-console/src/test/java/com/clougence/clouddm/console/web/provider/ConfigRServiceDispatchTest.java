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
package com.clougence.clouddm.console.web.provider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.impl.MainRequestDispatcher;
import com.clougence.clouddm.comm.component.impl.RSocketApiManager;
import com.clougence.clouddm.comm.model.*;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.global.rsocket.RSocketSerializationImpl;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.utils.JsonUtils;

/**
 * Sends the 1 argument fetchDsConfig request that the sidecar AutoExecJob sends, through the console RSocket registry and
 * MainRequestDispatcher, and checks that the response carries the plugin serialization provider the sidecar needs to decode it.
 */
public class ConfigRServiceDispatchTest {

    @Serialization(provider = "test-ds")
    public static class PluginDsConfig extends DataSourceConfig {
    }

    @Test
    public void sidecarFetchDsConfigByDsIdReturnsPluginConfig() throws Exception {
        DmDsConfigService dsConfigService = mock(DmDsConfigService.class);
        when(dsConfigService.fetchDsConfigFromExists(42L)).thenReturn(new PluginDsConfig());
        ConfigRServiceProvider provider = new ConfigRServiceProvider();
        ReflectionTestUtils.setField(provider, "dsConfigService", dsConfigService);
        RSocketApiManager.scanAllApiAndRegister(getClass().getClassLoader(), "com.clougence.clouddm.console.web.provider", type -> type == ConfigRServiceProvider.class ? provider : null);

        List<String> encodeProviders = new ArrayList<>();
        RSocketSerialization serialization = new RSocketSerialization() {

            @Override
            public String encode(String provider, Object argData) {
                encodeProviders.add(provider);
                return JsonUtils.toJson(argData);
            }

            @Override
            public Object decode(String provider, String jsonData, Type tryType) {
                return RSocketSerializationImpl.DEFAULT.decode(provider, jsonData, tryType);
            }
        };
        CompletableFuture<RSocketRespDTO<?>> sentBack = new CompletableFuture<>();
        RSocketRequestManager requestManager = mock(RSocketRequestManager.class);
        doAnswer(invocation -> sentBack.complete(invocation.getArgument(0))).when(requestManager).sendAsyncResultBack(any());
        MainRequestDispatcher dispatcher = new MainRequestDispatcher(requestManager, throwable -> {}, serialization);

        RSocketRequestWrapperDTO request = new RSocketRequestWrapperDTO();
        request.setRequestId("fetch-ds-config");
        request.setRSocketDirectionType(RSocketDirectionType.CLIENT_TO_SERVER);
        request.setApiFullMethodName(ConfigRService.class.getCanonicalName() + ".fetchDsConfig");
        request.setParamJsonValues(new ArrayList<>(List.of(JsonUtils.toJson(new RSocketParam(null, JsonUtils.toJson(42L))))));
        dispatcher.dispatchByRouteName(request);

        RSocketRespDTO<?> response = sentBack.get(20, TimeUnit.SECONDS);
        assertEquals(response.getMsg(), RSocketRespCode.SUCCESS.getCode(), response.getCode());
        verify(dsConfigService).fetchDsConfigFromExists(42L);
        RSocketParam result = JsonUtils.toObj(String.valueOf(response.getData()), RSocketParam.class);
        assertEquals("test-ds", result.getSerializer());
        assertEquals(List.of("test-ds"), encodeProviders);
    }
}
