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
package com.clougence.sql.common.antlr;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class AntlrAstUtils {

    private AntlrAstUtils(){
    }

    public static <T extends ParserRuleContext> List<T> descendants(ParserRuleContext ctx, Class<T> type) {
        List<T> result = new ArrayList<>();
        collect(ctx, type, result);
        return result;
    }

    public static List<String> identifiers(ParserRuleContext ctx, int tokenType) {
        List<String> result = new ArrayList<>();
        collectIdentifiers(ctx, tokenType, result);
        return result;
    }

    public static String cleanIdentifier(String identifier) {
        if (identifier == null || identifier.length() < 2) {
            return identifier;
        }
        if (identifier.charAt(0) == '"' && identifier.charAt(identifier.length() - 1) == '"') {
            return identifier.substring(1, identifier.length() - 1);
        }
        return identifier;
    }

    private static <T extends ParserRuleContext> void collect(ParseTree node, Class<T> type, List<T> result) {
        if (type.isInstance(node)) {
            result.add(type.cast(node));
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            collect(node.getChild(i), type, result);
        }
    }

    private static void collectIdentifiers(ParseTree node, int tokenType, List<String> result) {
        if (node instanceof TerminalNode terminal && terminal.getSymbol().getType() == tokenType) {
            result.add(cleanIdentifier(terminal.getText()));
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            collectIdentifiers(node.getChild(i), tokenType, result);
        }
    }
}
