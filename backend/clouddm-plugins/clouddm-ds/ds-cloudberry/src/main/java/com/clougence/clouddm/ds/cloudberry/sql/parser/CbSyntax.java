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
package com.clougence.clouddm.ds.cloudberry.sql.parser;

import java.util.ArrayList;
import java.util.List;
import java.io.Reader;
import java.io.StringWriter;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Trees;

import com.clougence.sql.postgres.parser.antlr.PgSqlLexer;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser.Extension_clauseContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CbSyntax {

    private CbSyntax() {
    }

    public static boolean isExtensionStatement(TokenStream tokens) {
        String first = tokens.LT(1).getText();
        String second = tokens.LT(2).getText();
        if ("RETRIEVE".equalsIgnoreCase(first)) {
            return true;
        }
        if ("GRANT".equalsIgnoreCase(first) || "REVOKE".equalsIgnoreCase(first)) {
            return containsSequence(tokens, "ON", "PROTOCOL");
        }
        if (!"CREATE".equalsIgnoreCase(first) && !"ALTER".equalsIgnoreCase(first) && !"DROP".equalsIgnoreCase(first)) {
            return false;
        }
        if (("CREATE".equalsIgnoreCase(first) || "ALTER".equalsIgnoreCase(first))
            && ("ROLE".equalsIgnoreCase(second) || "USER".equalsIgnoreCase(second))) {
            return isCloudberryRoleStatement(tokens, "ALTER".equalsIgnoreCase(first));
        }
        if ("RESOURCE".equalsIgnoreCase(second) || "PROTOCOL".equalsIgnoreCase(second) || "EXTERNAL".equalsIgnoreCase(second)) {
            return true;
        }
        if ("CREATE".equalsIgnoreCase(first)) {
            return "TRUSTED".equalsIgnoreCase(second) && "PROTOCOL".equalsIgnoreCase(tokens.LT(3).getText())
                || ("READABLE".equalsIgnoreCase(second) || "WRITABLE".equalsIgnoreCase(second))
                    && "EXTERNAL".equalsIgnoreCase(tokens.LT(3).getText());
        }
        return false;
    }

    private static boolean isCloudberryRoleStatement(TokenStream tokens, boolean alter) {
        String option = tokens.LT(4).getText();
        if (alter && ("RENAME".equalsIgnoreCase(option) || "SET".equalsIgnoreCase(option)
            || "RESET".equalsIgnoreCase(option))) {
            return false;
        }
        return contains(tokens, "CREATEEXTTABLE") || contains(tokens, "NOCREATEEXTTABLE") || contains(tokens, "DENY")
               || containsSequence(tokens, "RESOURCE", "QUEUE") || containsSequence(tokens, "RESOURCE", "GROUP");
    }

    private static boolean contains(TokenStream tokens, String expected) {
        for (int index = 4; tokens.LT(index).getType() != Token.EOF
            && tokens.LT(index).getType() != PgSqlLexer.SEMI; index++) {
            if (expected.equalsIgnoreCase(tokens.LT(index).getText())) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsSequence(TokenStream tokens, String first, String second) {
        for (int index = 1; tokens.LT(index).getType() != Token.EOF
            && tokens.LT(index).getType() != PgSqlLexer.SEMI; index++) {
            if (first.equalsIgnoreCase(tokens.LT(index).getText())
                && second.equalsIgnoreCase(tokens.LT(index + 1).getText())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExtensionStatement(String sql) {
        PgSqlLexer lexer = new PgSqlLexer(CharStreams.fromString(sql));
        lexer.setVersion(PostgresVersion.POSTGRES_14);
        return isExtensionStatement(new CommonTokenStream(lexer));
    }

    public static String readSql(Reader reader) {
        StringWriter writer = new StringWriter();
        try {
            reader.transferTo(writer);
        } catch (IOException e) {
            String msg = "Read SQL failed";
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        return writer.toString();
    }

    public static boolean isDistributionStart(TokenStream tokens) {
        return "DISTRIBUTED".equalsIgnoreCase(tokens.LT(1).getText());
    }

    public static boolean isParallelRetrieve(TokenStream tokens) {
        return "PARALLEL".equalsIgnoreCase(tokens.LT(1).getText())
            && "RETRIEVE".equalsIgnoreCase(tokens.LT(2).getText());
    }

    public static void validateDistribution(Extension_clauseContext clause) {
        List<String> parts = new ArrayList<>();
        for (var node : Trees.getDescendants(clause)) {
            if (node instanceof TerminalNode terminal) {
                parts.add(terminal.getText());
            }
        }
        if (parts.size() == 2 && equals(parts.get(0), "DISTRIBUTED")
            && (equals(parts.get(1), "RANDOMLY") || equals(parts.get(1), "REPLICATED"))) {
            return;
        }
        if (parts.size() >= 5 && equals(parts.get(0), "DISTRIBUTED") && equals(parts.get(1), "BY")
            && "(".equals(parts.get(2)) && ")".equals(parts.get(parts.size() - 1))) {
            for (int i = 3; i < parts.size() - 1; i++) {
                String part = parts.get(i);
                if (i % 2 == 0 && !",".equals(part)) {
                    break;
                }
                if (i % 2 == 1 && !identifier(part)) {
                    break;
                }
                if (i == parts.size() - 2 && identifier(part)) {
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Invalid Cloudberry distribution clause");
    }

    private static boolean identifier(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return true;
        }
        return value.matches("[A-Za-z_][A-Za-z_0-9$]*");
    }

    private static boolean equals(String value, String expected) {
        return expected.equalsIgnoreCase(value);
    }
}
