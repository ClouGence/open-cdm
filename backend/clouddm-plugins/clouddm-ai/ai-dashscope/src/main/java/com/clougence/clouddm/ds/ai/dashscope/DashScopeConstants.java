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
package com.clougence.clouddm.ds.ai.dashscope;

import java.util.Locale;

public final class DashScopeConstants {
    public static final String GENERATION_TASK = "text-generation";
    public static final String MULTIMODAL_TASK = "multimodal-generation";
    public static final String GENERATION_PATH = "services/aigc/text-generation/generation";
    public static final String MULTIMODAL_PATH = "services/aigc/multimodal-generation/generation";

    private DashScopeConstants(){
    }

    public static boolean isMultimodal(String model) {
        String name = model.toLowerCase(Locale.ROOT);
        return name.contains("kimi") || name.contains("qwen3.5") || name.contains("qwen3.6") || name.contains("qwen3.7") || name.contains("-vl") || name.contains("vl-")
               || name.contains("-audio") || name.contains("audio-") || name.contains("qvq") || name.contains("omni");
    }
}
