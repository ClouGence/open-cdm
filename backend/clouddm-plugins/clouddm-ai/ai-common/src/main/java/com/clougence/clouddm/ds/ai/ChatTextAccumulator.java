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
package com.clougence.clouddm.ds.ai;

/** Separates inline think tags even when a tag spans multiple transport chunks. */
public class ChatTextAccumulator {
    private static final String THINK_START = "<think>";
    private static final String THINK_END   = "</think>";
    private final StringBuilder text;
    private final StringBuilder reasoning;
    private final StringBuilder pending     = new StringBuilder();
    private boolean             thinking;

    public ChatTextAccumulator(StringBuilder text, StringBuilder reasoning){
        this.text = text;
        this.reasoning = reasoning;
    }

    public void append(String chunk, ChatStreamListener listener) {
        pending.append(chunk);
        while (!pending.isEmpty()) {
            String tag = THINK_START;
            if (thinking) {
                tag = THINK_END;
            }
            int index = pending.indexOf(tag);
            if (index >= 0) {
                write(pending.substring(0, index), listener);
                pending.delete(0, index + tag.length());
                thinking = !thinking;
                continue;
            }
            // Only hold a possible tag prefix; ordinary short deltas must be visible immediately.
            int suffixLength = Math.min(pending.length(), tag.length() - 1);
            while (suffixLength > 0 && !tag.startsWith(pending.substring(pending.length() - suffixLength))) {
                suffixLength--;
            }
            int safeLength = pending.length() - suffixLength;
            if (safeLength == 0) {
                return;
            }
            write(pending.substring(0, safeLength), listener);
            pending.delete(0, safeLength);
        }
    }

    public void finish(ChatStreamListener listener) {
        write(pending.toString(), listener);
        pending.setLength(0);
    }

    public String textSnapshot() {
        if (thinking) {
            return text.toString();
        }
        return text.toString() + pending;
    }

    public String reasoningSnapshot() {
        if (thinking) {
            return reasoning.toString() + pending;
        }
        return reasoning.toString();
    }

    private void write(String chunk, ChatStreamListener listener) {
        if (thinking) {
            reasoning.append(chunk);
            listener.getReasoning().accept(chunk);
        } else if (!chunk.isEmpty()) {
            text.append(chunk);
            listener.getText().accept(chunk);
        }
    }
}
