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
package com.clougence.clouddm.ds.opensearch.execute;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.ResultColMeta;

final class RecordingOpenSearchResults {
    final List<Map<String, Object>>                  rows        = new CopyOnWriteArrayList<>();
    final List<Map<String, ResultColMeta>>           metadata    = new CopyOnWriteArrayList<>();
    final List<Long>                                 counts      = new CopyOnWriteArrayList<>();
    final List<String>                               completions = new CopyOnWriteArrayList<>();
    final List<String>                               messages    = new CopyOnWriteArrayList<>();
    final List<Boolean>                              silentRows  = new CopyOnWriteArrayList<>();
    final List<Integer>                              metrics     = new CopyOnWriteArrayList<>();
    boolean                                          refreshOnProgress;
    int                                              flushes;
    java.util.function.Consumer<Map<String, Object>> onRow       = row -> {
                                                                 };
    boolean                                          errorNotified;
    private final ResultBuilder                      builder     = proxy(ResultBuilder.class);

    ResultBuilder builder() {
        return builder;
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(Class<T> type) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, (instance, method, args) -> {
            switch (method.getName()) {
                case "newResultId":
                    return "result-" + metadata.size();
                case "newResultMeta":
                    return proxy(ResultBuilder.ResultSetMetaBuild.class);
                case "newResultCount":
                    return proxy(ResultBuilder.ResultCountBuild.class);
                case "newMessage":
                    return proxy(ResultBuilder.ResultMessageBuild.class);
                case "newRowCountUpdate":
                    return proxy(ResultBuilder.ResultSetRowCountUpdateBuild.class);
                case "receiveMeta":
                    metadata.add((Map<String, ResultColMeta>) args[0]);
                    return proxy(ResultBuilder.ResultSetRowsBuild.class);
                case "receiveRow":
                    onRow.accept((Map<String, Object>) args[1]);
                    silentRows.add((Boolean) args[0]);
                    rows.add((Map<String, Object>) args[1]);
                    break;
                case "receiveUpdateCount":
                    counts.add((Long) args[0]);
                    break;
                case "receiveMessage":
                    messages.add((String) args[1]);
                    if (args.length == 3 && Boolean.TRUE.equals(args[2])) {
                        errorNotified = true;
                    }
                    break;
                case "finishRecord", "finishAndSilent":
                    completions.add(type.getSimpleName() + ":" + args[0]);
                    break;
                case "dataSize":
                    return 0L;
                case "setRefreshOnProgress":
                    refreshOnProgress = (Boolean) args[0];
                    break;
                case "collectMetric":
                    metrics.add((Integer) args[0]);
                    break;
                case "flushData":
                    flushes++;
                    break;
                case "collectCost", "finishAndContinue":
                    break;
                default:
                    throw new AssertionError("Unexpected result callback: " + method.getName());
            }
            return null;
        });
    }
}
