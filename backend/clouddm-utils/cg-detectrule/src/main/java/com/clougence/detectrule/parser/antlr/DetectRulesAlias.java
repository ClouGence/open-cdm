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
package com.clougence.detectrule.parser.antlr;

import java.util.HashMap;
import java.util.Map;

class DetectRulesAlias {

    static Map<String, Integer> define = new HashMap<>();
    static Map<String, Integer> advancedTypeAlias = new HashMap<>();
    static Map<String, Integer> compareSymbolAlias = new HashMap<>();
    static Map<String, Integer> arithmeticSymbolAlias = new HashMap<>();
    static Map<String, Integer> bitwiseSymbolAlias = new HashMap<>();
    static Map<String, Integer> logicSymbolAlias = new HashMap<>();
    static Map<String, Integer> programFeature = new HashMap<>();

    static {
        define.put("DEFINE", DetectRulesLexer.DEFINE);
        define.put("DEFAULT", DetectRulesLexer.DEFAULT);
        define.put("ENUM", DetectRulesLexer.ENUM);
        define.put("HINT", DetectRulesLexer.HINT);
    }

    static {
        advancedTypeAlias.put("BOOL", DetectRulesLexer.BOOL);
        advancedTypeAlias.put("FLOAT", DetectRulesLexer.FLOAT);
        advancedTypeAlias.put("INTEGER", DetectRulesLexer.INTEGER);
        advancedTypeAlias.put("INT", DetectRulesLexer.INTEGER);
        advancedTypeAlias.put("DECIMAL", DetectRulesLexer.DECIMAL);
        advancedTypeAlias.put("STRING", DetectRulesLexer.STRING);
        advancedTypeAlias.put("DATE", DetectRulesLexer.DATE);
        advancedTypeAlias.put("TIME", DetectRulesLexer.TIME);
        advancedTypeAlias.put("DATETIME", DetectRulesLexer.DATETIME);

        advancedTypeAlias.put("CAST", DetectRulesLexer.CAST);
        advancedTypeAlias.put("UP", DetectRulesLexer.UP);
        advancedTypeAlias.put("DOWN", DetectRulesLexer.DOWN);
        advancedTypeAlias.put("CEILING", DetectRulesLexer.CEILING);
        advancedTypeAlias.put("FLOOR", DetectRulesLexer.FLOOR);
        advancedTypeAlias.put("HALF_UP", DetectRulesLexer.HALF_UP);
        advancedTypeAlias.put("HALF_DOWN", DetectRulesLexer.HALF_DOWN);
        advancedTypeAlias.put("HALF_EVEN", DetectRulesLexer.HALF_EVEN);
        advancedTypeAlias.put("UNNECESSARY", DetectRulesLexer.UNNECESSARY);

        advancedTypeAlias.put("INSTANCEOF", DetectRulesLexer.INSTANCEOF); // keep Words for Types
        advancedTypeAlias.put("TYPEOF", DetectRulesLexer.TYPEOF);         // keep Words for Types
    }

    static {
        compareSymbolAlias.put("EQ", DetectRulesLexer.CO_EQ);
        compareSymbolAlias.put("IEQ", DetectRulesLexer.CO_IEQ);
        compareSymbolAlias.put("NE", DetectRulesLexer.CO_NE);
        compareSymbolAlias.put("GT", DetectRulesLexer.CO_GT);
        compareSymbolAlias.put("GE", DetectRulesLexer.CO_GE);
        compareSymbolAlias.put("LT", DetectRulesLexer.CO_LT);
        compareSymbolAlias.put("LE", DetectRulesLexer.CO_LE);
    }

    static {
        arithmeticSymbolAlias.put("INCR", DetectRulesLexer.AO_INCR);
        arithmeticSymbolAlias.put("DECR", DetectRulesLexer.AO_DECR);
        arithmeticSymbolAlias.put("PLUS", DetectRulesLexer.AO_PLUS);
        arithmeticSymbolAlias.put("MINUS", DetectRulesLexer.AO_MINUS);
        arithmeticSymbolAlias.put("MUL", DetectRulesLexer.AO_MUL);
        arithmeticSymbolAlias.put("DIV", DetectRulesLexer.AO_DIV);
        arithmeticSymbolAlias.put("MOD", DetectRulesLexer.AO_MOD);
    }

    static {
        bitwiseSymbolAlias.put("BNOT", DetectRulesLexer.BO_NOT);
        bitwiseSymbolAlias.put("BAND", DetectRulesLexer.BO_AND);
        bitwiseSymbolAlias.put("BOR", DetectRulesLexer.BO_OR);
        bitwiseSymbolAlias.put("XOR", DetectRulesLexer.BO_XOR);
        //put("NOT", DetectRulesLexer.BO_NOT); //conflict cannot be used.
        bitwiseSymbolAlias.put("USHR", DetectRulesLexer.BO_RSHIFT2);
        bitwiseSymbolAlias.put("SHR", DetectRulesLexer.BO_RSHIFT);
        bitwiseSymbolAlias.put("SHL", DetectRulesLexer.BO_LSHIFT);
    }

    static {
        logicSymbolAlias.put("AND", DetectRulesLexer.LO_AND);
        logicSymbolAlias.put("OR", DetectRulesLexer.LO_OR);
    }

    static {
        programFeature.put("LABEL", DetectRulesLexer.LABEL);
        programFeature.put("GOTO", DetectRulesLexer.GOTO);
        programFeature.put("SWITCH", DetectRulesLexer.SWITCH);
        programFeature.put("CASE", DetectRulesLexer.CASE);
        programFeature.put("FOR", DetectRulesLexer.FOR);
        programFeature.put("CONTINUE", DetectRulesLexer.CONTINUE);
        programFeature.put("BREAK", DetectRulesLexer.BREAK);
        programFeature.put("VAR", DetectRulesLexer.VAR);
        programFeature.put("DEF", DetectRulesLexer.DEF);
        programFeature.put("FUNC", DetectRulesLexer.FUNC);
    }
}
