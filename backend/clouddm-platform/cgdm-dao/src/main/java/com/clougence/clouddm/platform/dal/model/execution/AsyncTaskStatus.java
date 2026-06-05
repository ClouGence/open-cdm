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
package com.clougence.clouddm.platform.dal.model.execution;

/**
 * The type async_task do.
 * <pre>
 *   INIT ───> BLOCK ─────────────────────────────────────────────> CANCEL
 *     │         ↑                ↑               ↑                   ↑
 *     │         │             (cancel)         (skip)                │
 *     │         ↓                ↑               ↑                   │
 *     ╰───> WAIT_START ─────> RUNNING ─────>  FAILURE or COMPLETE    │
 *               ↑                ↓                                   │
 *               │             (pause)                                │
 *               │                ↓                                   │
 *               ╰─ (resume) ── PAUSE  ──── (skip) ───────────────────╯
 * </pre>
 * @author mode create time is 2019/12/11 10:10 下午 finish
 */
public enum AsyncTaskStatus {
    INIT,
    BLOCK,
    WAIT_START,
    RUNNING,
    FAILURE,
    CANCEL,
    CANCELING,
    COMPLETE,
    PAUSE,
    PAUSING,
}
