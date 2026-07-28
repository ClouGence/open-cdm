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
package com.clougence.clouddm.ds.dameng.sql.parser.base;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;

public abstract class DmSqlParserBase extends Parser {

    private final Deque<Boolean> siblingsOrderAllowedScopes = new ArrayDeque<>();
    private boolean              siblingsOrderAllowed;

    public DmSqlParserBase(TokenStream input){
        super(input);
    }

    protected boolean isKeywordAhead(String keyword) {
        return _input.LT(1).getText().equalsIgnoreCase(keyword);
    }

    protected final boolean isSchemaDefinitionCreateAhead() {
        int offset = 1;
        if (_input.LA(offset) != DmSqlParser.CREATE) {
            return false;
        }
        offset++;
        if (_input.LA(offset) == DmSqlParser.OR && _input.LA(offset + 1) == DmSqlParser.REPLACE) {
            offset += 2;
        }
        int type = _input.LA(offset);
        if (type == DmSqlParser.PUBLIC) {
            return _input.LA(offset + 1) == DmSqlParser.SYNONYM
                || _input.LA(offset + 1) == DmSqlParser.LINK;
        }
        if (type == DmSqlParser.EXTERNAL) {
            return _input.LA(offset + 1) == DmSqlParser.TABLE;
        }
        if (type == DmSqlParser.MATERIALIZED) {
            return _input.LA(offset + 1) == DmSqlParser.VIEW;
        }
        if (type == DmSqlParser.CONTEXT || type == DmSqlParser.CLUSTER) {
            return _input.LA(offset + 1) == DmSqlParser.INDEX;
        }
        return type == DmSqlParser.TABLE
            || type == DmSqlParser.GLOBAL
            || type == DmSqlParser.LOCAL
            || type == DmSqlParser.TEMPORARY
            || type == DmSqlParser.HUGE
            || type == DmSqlParser.VIEW
            || type == DmSqlParser.INDEX
            || type == DmSqlParser.SEQUENCE
            || type == DmSqlParser.PROCEDURE
            || type == DmSqlParser.FUNCTION
            || type == DmSqlParser.TRIGGER
            || type == DmSqlParser.SYNONYM
            || type == DmSqlParser.DOMAIN
            || type == DmSqlParser.OPERATOR
            || type == DmSqlParser.LINK
            || type == DmSqlParser.PACKAGE
            || type == DmSqlParser.CLASS;
    }

    protected boolean isCreateTableSelectTailAhead() {
        int current = _input.LA(1);
        int next = _input.LA(2);
        if (current == DmSqlParser.DISTRIBUTED) {
            return next == DmSqlParser.FULLY || next == DmSqlParser.RANDOMLY || next == DmSqlParser.BY || next == DmSqlParser.SEMI || next == DmSqlParser.SLASH
                   || next == Token.EOF;
        }
        return current == DmSqlParser.AUTO_INCREMENT && next == DmSqlParser.EQ;
    }

    protected boolean isBareTableAliasAhead() {
        if (isCreateTableSelectTailAhead()) {
            return false;
        }
        switch (_input.LA(1)) {
            case DmSqlParser.JOIN:
            case DmSqlParser.INNER:
            case DmSqlParser.LEFT:
            case DmSqlParser.RIGHT:
            case DmSqlParser.FULL:
            case DmSqlParser.CROSS:
            case DmSqlParser.OUTER:
            case DmSqlParser.NATURAL:
            case DmSqlParser.APPLY:
            case DmSqlParser.PARTITION:
            case DmSqlParser.SUBPARTITION:
            case DmSqlParser.MODEL:
            case DmSqlParser.PIVOT:
            case DmSqlParser.UNPIVOT:
            case DmSqlParser.CONNECT:
            case DmSqlParser.START:
            case DmSqlParser.WHERE:
            case DmSqlParser.GROUP:
            case DmSqlParser.HAVING:
            case DmSqlParser.ORDER:
            case DmSqlParser.LIMIT:
            case DmSqlParser.OFFSET:
            case DmSqlParser.FETCH:
            case DmSqlParser.FOR:
            case DmSqlParser.WITH:
            case DmSqlParser.UNION:
            case DmSqlParser.INTERSECT:
            case DmSqlParser.MINUS_SET:
            case DmSqlParser.EXCEPT:
            case DmSqlParser.LOG:
            case DmSqlParser.RETURN:
            case DmSqlParser.RETURNING:
            case DmSqlParser.SEMI:
            case DmSqlParser.SLASH:
            case DmSqlParser.COMMA:
            case DmSqlParser.RPAREN:
            case Token.EOF:
                return false;
            default:
                return true;
        }
    }

    protected boolean isUnsignedIntegerNumberAhead() {
        String text = _input.LT(1).getText();
        if (text == null || text.isEmpty()) {
            return false;
        }

        int index = 0;
        int length = text.length();
        while (index < length && Character.isDigit(text.charAt(index))) {
            index++;
        }
        if (index == 0) {
            return false;
        }
        if (index == length) {
            return true;
        }
        char exponentMarker = text.charAt(index);
        if (exponentMarker != 'e' && exponentMarker != 'E') {
            return false;
        }
        index++;
        if (index < length && text.charAt(index) == '+') {
            index++;
        }
        if (index == length) {
            return false;
        }
        while (index < length && Character.isDigit(text.charAt(index))) {
            index++;
        }
        return index == length;
    }

    protected boolean isUnsignedIntegerInRangeAhead(long minimum, long maximum) {
        if (!isUnsignedIntegerNumberAhead()) {
            return false;
        }
        return isUnsignedIntegerInRange(_input.LT(1).getText(), minimum, maximum);
    }

    protected boolean isUnsignedIntegerInRange(String text, long minimum, long maximum) {
        try {
            long value = new BigDecimal(text).longValueExact();
            return value >= minimum && value <= maximum;
        } catch (ArithmeticException | NumberFormatException e) {
            return false;
        }
    }

    protected boolean isValidArrayDeclaration(String dataType) {
        int openBracket = dataType.indexOf('[');
        if (openBracket < 0 || dataType.charAt(dataType.length() - 1) != ']') {
            return false;
        }
        String bounds = dataType.substring(openBracket + 1, dataType.length() - 1);
        if (bounds.isEmpty()) {
            return true;
        }

        boolean hasNumber = false;
        boolean hasComma = false;
        boolean expectNumber = true;
        for (int index = 0; index < bounds.length(); index++) {
            char current = bounds.charAt(index);
            if (Character.isDigit(current)) {
                hasNumber = true;
                expectNumber = false;
            } else if (current == ',') {
                hasComma = true;
                if (hasNumber && expectNumber) {
                    return false;
                }
                expectNumber = true;
            } else {
                return false;
            }
        }
        return !hasNumber || !expectNumber || hasComma && !hasNumber;
    }

    protected boolean isValidArrayAllocation(String dataType) {
        int openBracket = dataType.indexOf('[');
        while (openBracket >= 0) {
            int closeBracket = dataType.indexOf(']', openBracket + 1);
            if (closeBracket < 0) {
                return false;
            }
            if (closeBracket > openBracket + 1) {
                return true;
            }
            openBracket = dataType.indexOf('[', closeBracket + 1);
        }
        return false;
    }

    protected boolean isStringLiteralAhead(String... supportedValues) {
        String text = _input.LT(1).getText();
        if (text == null || text.length() < 2
            || text.charAt(0) != '\'' || text.charAt(text.length() - 1) != '\'') {
            return false;
        }
        String value = text.substring(1, text.length() - 1).replace("''", "'");
        for (String supportedValue : supportedValues) {
            if (value.equalsIgnoreCase(supportedValue)) {
                return true;
            }
        }
        return false;
    }

    protected void pushSiblingsOrderScope() {
        siblingsOrderAllowedScopes.push(siblingsOrderAllowed);
        siblingsOrderAllowed = false;
    }

    protected void popSiblingsOrderScope() {
        siblingsOrderAllowed = !siblingsOrderAllowedScopes.isEmpty() && siblingsOrderAllowedScopes.pop();
    }

    protected void allowSiblingsOrder() {
        siblingsOrderAllowed = true;
    }

    protected void disallowSiblingsOrder() {
        siblingsOrderAllowed = false;
    }

    protected boolean isSiblingsOrderAllowed() { return siblingsOrderAllowed; }

    protected boolean isFunctionName(String functionName, String... supportedNames) {
        if (functionName == null) {
            return false;
        }
        String normalized = functionName;
        int dotIndex = normalized.lastIndexOf('.');
        if (dotIndex >= 0) {
            normalized = normalized.substring(dotIndex + 1);
        }
        normalized = normalized.replace("\"", "");
        for (String supportedName : supportedNames) {
            if (normalized.equalsIgnoreCase(supportedName)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isKeepFunction(String functionName) {
        return isFunctionName(functionName, "avg", "max", "min", "count", "sum");
    }

    protected boolean isWithinGroupFunction(String functionName) {
        return isFunctionName(functionName, "listagg", "percentile_cont", "percentile_disc");
    }

    protected boolean isNullTreatmentFunction(String functionName) {
        return isFunctionName(functionName, "first_value", "last_value", "lag", "lead", "nth_value");
    }
}
