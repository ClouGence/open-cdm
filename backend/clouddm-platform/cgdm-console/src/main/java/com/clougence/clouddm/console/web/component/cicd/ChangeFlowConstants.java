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
package com.clougence.clouddm.console.web.component.cicd;

import java.util.regex.Pattern;

/**
 * Shared safety and maintenance limits for CI/CD change-flow processing.
 */
public final class ChangeFlowConstants {

    /**
     * Maximum raw Webhook request-body size, in bytes. The body is rejected above 2 MiB before JSON parsing to
     * prevent a single request from consuming unbounded heap memory.
     */
    public static final int     MAX_WEBHOOK_BODY_BYTES                  = 2 * 1024 * 1024;
    /**
     * Maximum Webhook delivery-ID length, in characters. This matches the {@code delivery_id varchar(255)}
     * persistence column.
     */
    public static final int     MAX_WEBHOOK_DELIVERY_ID_LENGTH          = 255;
    /**
     * Maximum number of orphan trigger receipts selected and deleted by one scheduled cleanup run.
     */
    public static final int     ORPHAN_RECEIPT_CLEANUP_BATCH_SIZE       = 1_000;
    /**
     * Delay between orphan trigger-receipt cleanup runs, in minutes. The same value is used for the initial delay.
     */
    public static final int     ORPHAN_RECEIPT_CLEANUP_INTERVAL_MINUTES = 60;
    /**
     * Accepted immutable Git commit format: a case-insensitive 40-character SHA-1 or 64-character SHA-256 value.
     */
    public static final Pattern WEBHOOK_COMMIT_SHA_PATTERN              = Pattern.compile("(?i)^[0-9a-f]{40,64}$");

    private ChangeFlowConstants(){
    }
}
