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
package com.clougence.clouddm.ds.es.execute;

import java.util.concurrent.CompletableFuture;

import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;

final class EsResponseListener extends CompletableFuture<Response> implements ResponseListener {
    @Override
    public void onSuccess(Response response) {
        complete(response);
    }

    @Override
    public void onFailure(Exception exception) {
        completeExceptionally(exception);
    }
}
