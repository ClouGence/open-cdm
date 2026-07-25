/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

/**
 * Builds behavior objects from dialect AST identifier contexts.
 */
public class RdbBehaviorObjectFactory {

    private final Map<UmiTypes, Object> levels;
    private final int                   baseLine;
    private final int                   baseColumn;

    public RdbBehaviorObjectFactory(Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.levels = levels;
        this.baseLine = Math.max(1, baseLine);
        this.baseColumn = Math.max(0, baseColumn);
    }

    public BehaviorObject object(TargetType type, ParserRuleContext context, int identifierTokenType) {
        List<String> names = new ArrayList<>();
        collectNames(context, identifierTokenType, names);
        return object(type, context, names);
    }

    public BehaviorObject object(TargetType type, ParserRuleContext context, List<String> names) {
        if (names.isEmpty()) {
            return null;
        }

        List<String> path = new ArrayList<>();
        addLevelPath(path, UmiTypes.Instance);
        if (type == TargetType.Catalog) {
            path.add(names.get(names.size() - 1));
        } else if (type == TargetType.Schema) {
            addLevel(path, UmiTypes.Catalog);
            path.add(names.get(names.size() - 1));
        } else {
            if (names.size() == 1) {
                addLevel(path, UmiTypes.Catalog);
                addLevel(path, UmiTypes.Schema);
            } else if (names.size() == 2) {
                addLevel(path, UmiTypes.Catalog);
            }
            path.addAll(names);
        }

        Token start = context.getStart();
        Token stop = context.getStop();
        BehaviorObject object = new BehaviorObject();
        object.setTargetType(type);
        object.setResourcePath(path.isEmpty() ? "/" : "/" + String.join("/", path) + "/");
        object.setStartLine(line(start));
        object.setStartColumn(column(start));
        object.setEndLine(line(stop));
        object.setEndColumn(column(stop) + stop.getText().length());
        return object;
    }

    private void collectNames(ParseTree tree, int identifierTokenType, List<String> names) {
        if (tree instanceof TerminalNode node && node.getSymbol().getType() == identifierTokenType) {
            names.add(unquote(node.getText()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNames(tree.getChild(i), identifierTokenType, names);
        }
    }

    private void addLevelPath(List<String> path, UmiTypes level) {
        String value = level(level);
        if (StringUtils.isBlank(value)) {
            return;
        }
        for (String node : value.split("/")) {
            if (StringUtils.isNotBlank(node)) {
                path.add(node);
            }
        }
    }

    private void addLevel(List<String> path, UmiTypes level) {
        String value = level(level);
        if (StringUtils.isNotBlank(value)) {
            path.add(value);
        }
    }

    private String level(UmiTypes level) {
        if (levels == null || levels.get(level) == null) {
            return null;
        }
        return StringUtils.toString(levels.get(level));
    }

    private int line(Token token) {
        return baseLine + token.getLine() - 1;
    }

    private int column(Token token) {
        if (token.getLine() == 1) {
            return baseColumn + token.getCharPositionInLine();
        }
        return token.getCharPositionInLine();
    }

    private String unquote(String value) {
        if (value.length() < 2) {
            return value;
        }
        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        if (first == '"' && last == '"' || first == '[' && last == ']' || first == '`' && last == '`') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
