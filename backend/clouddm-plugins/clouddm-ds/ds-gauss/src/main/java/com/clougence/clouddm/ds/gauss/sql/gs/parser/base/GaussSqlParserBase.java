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
package com.clougence.clouddm.ds.gauss.sql.gs.parser.base;

import java.util.List;

import org.antlr.v4.runtime.*;

import com.clougence.clouddm.ds.gauss.sql.gs.parser.antlr.GaussSqlLexer;
import com.clougence.clouddm.ds.gauss.sql.gs.parser.antlr.GaussSqlParser;

public abstract class GaussSqlParserBase extends Parser {

    public GaussSqlParserBase(TokenStream input){
        super(input);
    }

    public void ParseRoutineBody() {
        GaussSqlParser.Createfunc_opt_listContext _localctx = (GaussSqlParser.Createfunc_opt_listContext) this.getContext();
        String lang = null;
        for (GaussSqlParser.Createfunc_opt_itemContext coi : _localctx.createfunc_opt_item()) {
            if (coi.LANGUAGE() != null) {
                if (coi.nonreservedword_or_sconst() != null)
                    if (coi.nonreservedword_or_sconst().nonreservedword() != null)
                        if (coi.nonreservedword_or_sconst().nonreservedword().identifier() != null)
                            if (coi.nonreservedword_or_sconst().nonreservedword().identifier().Identifier() != null) {
                                lang = coi.nonreservedword_or_sconst().nonreservedword().identifier().Identifier().getText();
                                break;
                            }
            }
        }
        if (null == lang)
            return;
        GaussSqlParser.Createfunc_opt_itemContext func_as = null;
        for (GaussSqlParser.Createfunc_opt_itemContext a : _localctx.createfunc_opt_item()) {
            if (a.func_as() != null) {
                func_as = a;
                break;

            }

        }
        if (func_as != null) {
            String txt = GetRoutineBodyString(func_as.func_as().sconst(0));
            switch (lang) {
                case "plpgsql":
                    //NB: Cannot be done this way.
                    //GaussSqlParser ph = GetGaussSqlParser(txt);
                    //func_as.func_as().Definition = ph.plsqlroot();
                    break;
                case "sql":
                    //func_as.func_as().Definition = ph.root();
                    break;
            }
        }
    }

    private String TrimQuotes(String s) {
        return (s == null || s.isEmpty()) ? s : s.substring(1, s.length() - 1);
    }

    public String unquote(String s) {
        int slength = s.length();
        StringBuilder r = new StringBuilder(slength);
        int i = 0;
        while (i < slength) {
            Character c = s.charAt(i);
            r.append(c);
            if (c == '\'' && i < slength - 1 && (s.charAt(i + 1) == '\''))
                i++;
            i++;
        }
        return r.toString();
    }

    public String GetRoutineBodyString(GaussSqlParser.SconstContext rule) {
        GaussSqlParser.AnysconstContext anysconst = rule.anysconst();
        org.antlr.v4.runtime.tree.TerminalNode StringConstant = anysconst.StringConstant();
        if (null != StringConstant)
            return unquote(TrimQuotes(StringConstant.getText()));
        org.antlr.v4.runtime.tree.TerminalNode UnicodeEscapeStringConstant = anysconst.UnicodeEscapeStringConstant();
        if (null != UnicodeEscapeStringConstant)
            return TrimQuotes(UnicodeEscapeStringConstant.getText());
        org.antlr.v4.runtime.tree.TerminalNode EscapeStringConstant = anysconst.EscapeStringConstant();
        if (null != EscapeStringConstant)
            return TrimQuotes(EscapeStringConstant.getText());
        String result = "";
        List<org.antlr.v4.runtime.tree.TerminalNode> dollartext = anysconst.DollarText();
        for (org.antlr.v4.runtime.tree.TerminalNode s : dollartext) {
            result += s.getText();
        }
        return result;
    }

    public GaussSqlParser GetGaussSqlParser(String script) {
        CharStream charStream = CharStreams.fromString(script);
        Lexer lexer = new GaussSqlLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GaussSqlParser parser = new GaussSqlParser(tokens);
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        LexerDispatchingErrorListener listener_lexer = new LexerDispatchingErrorListener((Lexer) (this.getInputStream().getTokenSource()));
        ParserDispatchingErrorListener listener_parser = new ParserDispatchingErrorListener(this);
        lexer.addErrorListener(listener_lexer);
        parser.addErrorListener(listener_parser);
        return parser;
    }

    public boolean OnlyAcceptableOps() {
        Token c = this.getInputStream().LT(1);
        String text = c.getText();
        return text.equals("!") || text.equals("!!") || text.equals("!=-");
    }

}
