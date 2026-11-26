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
package com.clougence.clouddm.console.web.service.openapi;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.clougence.clouddm.console.web.global.session.SessionTokens;
import com.clougence.clouddm.platform.dal.mapper.openapi.DmOpenApiIdempotencyMapper;
import com.clougence.clouddm.platform.dal.model.openapi.DmOpenApiIdempotencyDO;
import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.clouddm.sdk.openapi.OpenApiRequest;
import com.clougence.clouddm.sdk.openapi.OpenApiResponse;
import com.clougence.clouddm.sdk.openapi.OpenApiWriteOperation;

import jakarta.annotation.Resource;

@Service
public class OpenApiIdempotencyService {

    private static final long          RETENTION_MS = Duration.ofHours(24).toMillis();

    @Resource
    private DmOpenApiIdempotencyMapper idempotencyMapper;
    @Resource
    private PlatformTransactionManager transactionManager;

    /** An empty result means the key was used for different request content. */
    public Optional<OpenApiResponse> execute(OpenApiRequest request, OpenApiCaller caller, String queryString, OpenApiWriteOperation operation) {
        DmOpenApiIdempotencyDO record = new DmOpenApiIdempotencyDO();
        record.setCallerUid(caller.uid());
        record.setOperationPath(request.getPath());
        record.setIdempotencyKey(request.getIdempotencyKey());
        record.setRequestHash(SessionTokens.sha256Hex(Objects.toString(queryString, "") + "\n" + SessionTokens.sha256Hex(request.getBody())));

        TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        return transaction.execute(status -> {
            // Keep the row locked through authorization and execution, including across Console nodes.
            this.idempotencyMapper.reserve(record);
            Supplier<OpenApiResponse> write = operation.authorize(request, caller);

            DmOpenApiIdempotencyDO completed = this.idempotencyMapper.queryCompleted(record);
            if (completed != null) {
                if (!record.getRequestHash().equals(completed.getRequestHash())) {
                    return Optional.empty();
                }
                return Optional.of(new OpenApiResponse(completed.getHttpStatus(), completed.getResponseBody()));
            }

            OpenApiResponse response = write.get();
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                record.setHttpStatus(response.getStatusCode());
                record.setResponseBody(response.getBody());
                this.idempotencyMapper.complete(record, RETENTION_MS);
            } else {
                status.setRollbackOnly();
            }
            return Optional.of(response);
        });
    }
}
