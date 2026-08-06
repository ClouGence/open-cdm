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
package com.clougence.sql.common.parser;

import java.util.Objects;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

/** Fixed-size pool of exclusively leased ANTLR prediction caches. */
final class AntlrPredictionCaches {

    private static final int       DEFAULT_SLOTS             = Math.max(1, Runtime.getRuntime().availableProcessors());
    private static final int       DEFAULT_MAX_SLOTS_PER_KEY = 3;
    private static final int       DEFAULT_MAX_DFA_STATES    = 2_000;
    private static final CachePool CACHE_POOL                = new CachePool(DEFAULT_SLOTS, DEFAULT_MAX_SLOTS_PER_KEY, DEFAULT_MAX_DFA_STATES);

    private AntlrPredictionCaches(){
    }

    static Lease acquire(Lexer lexer, Parser parser, Object scope) {
        return CACHE_POOL.acquire(lexer, parser, scope);
    }

    private static int configuredPositive(String property, int defaultValue) {
        int value = Integer.getInteger(property, defaultValue);
        if (value < 1) {
            throw new IllegalArgumentException(property + " must be greater than zero: " + value);
        }
        return value;
    }

    static final class Lease implements AutoCloseable {

        private final CachePool pool;
        private final CacheSlot slot;
        private final boolean   retained;
        private boolean         closed;

        private Lease(CachePool pool, CacheSlot slot, boolean retained, Lexer lexer, Parser parser){
            this.pool = pool;
            this.slot = slot;
            this.retained = retained;
            slot.caches.install(lexer, parser);
        }

        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.pool != null) {
                this.pool.release(this.slot, this.retained);
            }
        }
    }

    private static final class CachePool {

        private final CacheSlot[]            slots;
        private final ThreadLocal<CacheSlot> overflowSlot = ThreadLocal.withInitial(CacheSlot::new);
        private final int                    maxSlotsPerKey;
        private final int                    maxDfaStates;
        private long                         sequence;

        private CachePool(int slotCount, int maxSlotsPerKey, int maxDfaStates){
            this.slots = new CacheSlot[slotCount];
            for (int i = 0; i < slotCount; i++) {
                this.slots[i] = new CacheSlot();
            }
            this.maxSlotsPerKey = maxSlotsPerKey;
            this.maxDfaStates = maxDfaStates;
        }

        private Lease acquire(Lexer lexer, Parser parser, Object scope) {
            ATN lexerAtn = lexer.getATN();
            ATN parserAtn = parser.getATN();
            CacheSlot slot;
            synchronized (this) {
                slot = matchingSlot(lexerAtn, parserAtn, scope);
                if (slot == null && retainedSlotCount(lexerAtn, parserAtn, scope) < this.maxSlotsPerKey) {
                    slot = replacementSlot();
                }
                if (slot != null) {
                    if (!slot.matches(lexerAtn, parserAtn, scope)) {
                        slot.initialize(lexerAtn, parserAtn, scope);
                    }
                    slot.borrowed = true;
                    slot.lastUsed = ++this.sequence;
                }
            }
            if (slot != null) {
                return new Lease(this, slot, true, lexer, parser);
            }

            // Keep overflow caches outside the retained global pool. A worker may reuse its
            // overflow cache while it keeps processing the same parser key, avoiding a cold
            // DFA rebuild for every SQL. Switching key, exceeding the state limit, or ending
            // the worker lifetime drops the old cache.
            CacheSlot overflow = this.overflowSlot.get();
            if (overflow.borrowed) {
                // Defensive fallback for a nested parse on the same worker thread.
                CacheSlot temporary = new CacheSlot();
                temporary.initialize(lexerAtn, parserAtn, scope);
                temporary.borrowed = true;
                return new Lease(null, temporary, false, lexer, parser);
            }
            if (!overflow.matches(lexerAtn, parserAtn, scope)) {
                overflow.initialize(lexerAtn, parserAtn, scope);
            }
            overflow.borrowed = true;
            return new Lease(this, overflow, false, lexer, parser);
        }

        private CacheSlot matchingSlot(ATN lexerAtn, ATN parserAtn, Object scope) {
            for (CacheSlot slot : this.slots) {
                if (!slot.borrowed && slot.matches(lexerAtn, parserAtn, scope)) {
                    return slot;
                }
            }
            return null;
        }

        private int retainedSlotCount(ATN lexerAtn, ATN parserAtn, Object scope) {
            int count = 0;
            for (CacheSlot slot : this.slots) {
                if (slot.matches(lexerAtn, parserAtn, scope)) {
                    count++;
                }
            }
            return count;
        }

        private CacheSlot replacementSlot() {
            CacheSlot replacement = null;
            for (CacheSlot slot : this.slots) {
                if (slot.borrowed) {
                    continue;
                }
                if (slot.caches == null) {
                    return slot;
                }
                if (replacement == null || slot.lastUsed < replacement.lastUsed) {
                    replacement = slot;
                }
            }
            return replacement;
        }

        private void release(CacheSlot slot, boolean retained) {
            if (!retained) {
                if (slot.caches.stateCount() > this.maxDfaStates) {
                    slot.discard();
                }
                slot.borrowed = false;
                return;
            }
            releaseRetained(slot);
        }

        private synchronized void releaseRetained(CacheSlot slot) {
            if (slot.caches.stateCount() > this.maxDfaStates) {
                slot.discard();
            }
            slot.borrowed = false;
            slot.lastUsed = ++this.sequence;
        }
    }

    private static final class CacheSlot {

        private ATN              lexerAtn;
        private ATN              parserAtn;
        private Object           scope;
        private PredictionCaches caches;
        private boolean          borrowed;
        private long             lastUsed;

        private boolean matches(ATN lexerAtn, ATN parserAtn, Object scope) {
            return this.lexerAtn == lexerAtn && this.parserAtn == parserAtn && Objects.equals(this.scope, scope);
        }

        private void initialize(ATN lexerAtn, ATN parserAtn, Object scope) {
            this.lexerAtn = lexerAtn;
            this.parserAtn = parserAtn;
            this.scope = scope;
            this.caches = new PredictionCaches(lexerAtn, parserAtn);
        }

        private void discard() {
            this.lexerAtn = null;
            this.parserAtn = null;
            this.scope = null;
            this.caches = null;
        }
    }

    private record PredictionCaches(PredictionCache lexer, PredictionCache parser) {

        private PredictionCaches(ATN lexerAtn, ATN parserAtn){
            this(new PredictionCache(lexerAtn), new PredictionCache(parserAtn));
        }

        private void install(Lexer lexer, Parser parser) {
            lexer.setInterpreter(new LexerATNSimulator(lexer, lexer.getATN(), this.lexer.decisionToDfa, this.lexer.contextCache));
            parser.setInterpreter(new ParserATNSimulator(parser, parser.getATN(), this.parser.decisionToDfa, this.parser.contextCache));
        }

        private long stateCount() {
            return this.lexer.stateCount() + this.parser.stateCount();
        }
    }

    private static final class PredictionCache {

        private final DFA[]                  decisionToDfa;
        private final PredictionContextCache contextCache = new PredictionContextCache();

        private PredictionCache(ATN atn){
            this.decisionToDfa = new DFA[atn.getNumberOfDecisions()];
            for (int i = 0; i < this.decisionToDfa.length; i++) {
                this.decisionToDfa[i] = new DFA(atn.getDecisionState(i), i);
            }
        }

        private long stateCount() {
            long count = 0;
            for (DFA dfa : this.decisionToDfa) {
                count += dfa.states.size();
            }
            return count;
        }
    }
}
