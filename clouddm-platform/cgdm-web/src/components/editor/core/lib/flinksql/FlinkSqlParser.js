// Generated from dt-sql-parser/src/grammar/flinksql/FlinkSqlParser.g4 by ANTLR 4.9.0-SNAPSHOT
import { ATN } from "antlr4ts/atn/ATN";
import { ATNDeserializer } from "antlr4ts/atn/ATNDeserializer";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { NoViableAltException } from "antlr4ts/NoViableAltException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { ParserATNSimulator } from "antlr4ts/atn/ParserATNSimulator";
import { RecognitionException } from "antlr4ts/RecognitionException";
import { Token } from "antlr4ts/Token";
import { VocabularyImpl } from "antlr4ts/VocabularyImpl";
import * as Utils from "antlr4ts/misc/Utils";
export class FlinkSqlParser extends Parser {
    // @Override
    // @NotNull
    get vocabulary() {
        return FlinkSqlParser.VOCABULARY;
    }
    // tslint:enable:no-trailing-whitespace
    // @Override
    get grammarFileName() { return "FlinkSqlParser.g4"; }
    // @Override
    get ruleNames() { return FlinkSqlParser.ruleNames; }
    // @Override
    get serializedATN() { return FlinkSqlParser._serializedATN; }
    createFailedPredicateException(predicate, message) {
        return new FailedPredicateException(this, predicate, message);
    }
    constructor(input) {
        super(input);
        this._interp = new ParserATNSimulator(FlinkSqlParser._ATN, this);
    }
    // @RuleVersion(0)
    program() {
        let _localctx = new ProgramContext(this._ctx, this.state);
        this.enterRule(_localctx, 0, FlinkSqlParser.RULE_program);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 379;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.KW_ALTER || _la === FlinkSqlParser.KW_BEGIN || _la === FlinkSqlParser.KW_CREATE || ((((_la - 109)) & ~0x1F) === 0 && ((1 << (_la - 109)) & ((1 << (FlinkSqlParser.KW_DESCRIBE - 109)) | (1 << (FlinkSqlParser.KW_DROP - 109)) | (1 << (FlinkSqlParser.KW_EXECUTE - 109)) | (1 << (FlinkSqlParser.KW_EXPLAIN - 109)))) !== 0) || _la === FlinkSqlParser.KW_INSERT || ((((_la - 312)) & ~0x1F) === 0 && ((1 << (_la - 312)) & ((1 << (FlinkSqlParser.KW_RESET - 312)) | (1 << (FlinkSqlParser.KW_SELECT - 312)) | (1 << (FlinkSqlParser.KW_SET - 312)) | (1 << (FlinkSqlParser.KW_SHOW - 312)))) !== 0) || ((((_la - 410)) & ~0x1F) === 0 && ((1 << (_la - 410)) & ((1 << (FlinkSqlParser.KW_USE - 410)) | (1 << (FlinkSqlParser.KW_VALUES - 410)) | (1 << (FlinkSqlParser.KW_WITH - 410)) | (1 << (FlinkSqlParser.KW_ADD - 410)))) !== 0) || _la === FlinkSqlParser.KW_DESC || _la === FlinkSqlParser.KW_LOAD || ((((_la - 489)) & ~0x1F) === 0 && ((1 << (_la - 489)) & ((1 << (FlinkSqlParser.KW_REMOVE - 489)) | (1 << (FlinkSqlParser.KW_UNLOAD - 489)) | (1 << (FlinkSqlParser.LR_BRACKET - 489)))) !== 0) || _la === FlinkSqlParser.SEMICOLON) {
                    {
                        {
                            this.state = 376;
                            this.singleStatement();
                        }
                    }
                    this.state = 381;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
                this.state = 382;
                this.match(FlinkSqlParser.EOF);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    singleStatement() {
        let _localctx = new SingleStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 2, FlinkSqlParser.RULE_singleStatement);
        try {
            this.state = 389;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_ALTER:
                case FlinkSqlParser.KW_BEGIN:
                case FlinkSqlParser.KW_CREATE:
                case FlinkSqlParser.KW_DESCRIBE:
                case FlinkSqlParser.KW_DROP:
                case FlinkSqlParser.KW_EXECUTE:
                case FlinkSqlParser.KW_EXPLAIN:
                case FlinkSqlParser.KW_INSERT:
                case FlinkSqlParser.KW_RESET:
                case FlinkSqlParser.KW_SELECT:
                case FlinkSqlParser.KW_SET:
                case FlinkSqlParser.KW_SHOW:
                case FlinkSqlParser.KW_USE:
                case FlinkSqlParser.KW_VALUES:
                case FlinkSqlParser.KW_WITH:
                case FlinkSqlParser.KW_ADD:
                case FlinkSqlParser.KW_DESC:
                case FlinkSqlParser.KW_LOAD:
                case FlinkSqlParser.KW_REMOVE:
                case FlinkSqlParser.KW_UNLOAD:
                case FlinkSqlParser.LR_BRACKET:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 384;
                        this.sqlStatement();
                        this.state = 386;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 1, this._ctx)) {
                            case 1:
                                {
                                    this.state = 385;
                                    this.match(FlinkSqlParser.SEMICOLON);
                                }
                                break;
                        }
                    }
                    break;
                case FlinkSqlParser.SEMICOLON:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 388;
                        this.emptyStatement();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    sqlStatement() {
        let _localctx = new SqlStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 4, FlinkSqlParser.RULE_sqlStatement);
        try {
            this.state = 403;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 3, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 391;
                        this.ddlStatement();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 392;
                        this.dmlStatement();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 393;
                        this.describeStatement();
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 394;
                        this.explainStatement();
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 395;
                        this.useStatement();
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 396;
                        this.showStatememt();
                    }
                    break;
                case 7:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 397;
                        this.loadStatement();
                    }
                    break;
                case 8:
                    this.enterOuterAlt(_localctx, 8);
                    {
                        this.state = 398;
                        this.unloadStatememt();
                    }
                    break;
                case 9:
                    this.enterOuterAlt(_localctx, 9);
                    {
                        this.state = 399;
                        this.setStatememt();
                    }
                    break;
                case 10:
                    this.enterOuterAlt(_localctx, 10);
                    {
                        this.state = 400;
                        this.resetStatememt();
                    }
                    break;
                case 11:
                    this.enterOuterAlt(_localctx, 11);
                    {
                        this.state = 401;
                        this.jarStatememt();
                    }
                    break;
                case 12:
                    this.enterOuterAlt(_localctx, 12);
                    {
                        this.state = 402;
                        this.dtAddStatement();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    emptyStatement() {
        let _localctx = new EmptyStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 6, FlinkSqlParser.RULE_emptyStatement);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 405;
                this.match(FlinkSqlParser.SEMICOLON);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    ddlStatement() {
        let _localctx = new DdlStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 8, FlinkSqlParser.RULE_ddlStatement);
        try {
            this.state = 421;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 4, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 407;
                        this.createTable();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 408;
                        this.createDatabase();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 409;
                        this.createView();
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 410;
                        this.createFunction();
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 411;
                        this.createCatalog();
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 412;
                        this.alterTable();
                    }
                    break;
                case 7:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 413;
                        this.alertView();
                    }
                    break;
                case 8:
                    this.enterOuterAlt(_localctx, 8);
                    {
                        this.state = 414;
                        this.alterDatabase();
                    }
                    break;
                case 9:
                    this.enterOuterAlt(_localctx, 9);
                    {
                        this.state = 415;
                        this.alterFunction();
                    }
                    break;
                case 10:
                    this.enterOuterAlt(_localctx, 10);
                    {
                        this.state = 416;
                        this.dropCatalog();
                    }
                    break;
                case 11:
                    this.enterOuterAlt(_localctx, 11);
                    {
                        this.state = 417;
                        this.dropTable();
                    }
                    break;
                case 12:
                    this.enterOuterAlt(_localctx, 12);
                    {
                        this.state = 418;
                        this.dropDatabase();
                    }
                    break;
                case 13:
                    this.enterOuterAlt(_localctx, 13);
                    {
                        this.state = 419;
                        this.dropView();
                    }
                    break;
                case 14:
                    this.enterOuterAlt(_localctx, 14);
                    {
                        this.state = 420;
                        this.dropFunction();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dmlStatement() {
        let _localctx = new DmlStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 10, FlinkSqlParser.RULE_dmlStatement);
        try {
            this.state = 425;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_SELECT:
                case FlinkSqlParser.KW_VALUES:
                case FlinkSqlParser.KW_WITH:
                case FlinkSqlParser.LR_BRACKET:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 423;
                        this.queryStatement(0);
                    }
                    break;
                case FlinkSqlParser.KW_BEGIN:
                case FlinkSqlParser.KW_EXECUTE:
                case FlinkSqlParser.KW_INSERT:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 424;
                        this.insertStatement();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    describeStatement() {
        let _localctx = new DescribeStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 12, FlinkSqlParser.RULE_describeStatement);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 427;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_DESCRIBE || _la === FlinkSqlParser.KW_DESC)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 428;
                this.tablePath();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    explainStatement() {
        let _localctx = new ExplainStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 14, FlinkSqlParser.RULE_explainStatement);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 430;
                this.match(FlinkSqlParser.KW_EXPLAIN);
                this.state = 434;
                this._errHandler.sync(this);
                switch (this._input.LA(1)) {
                    case FlinkSqlParser.KW_CHANGELOG_MODE:
                    case FlinkSqlParser.KW_ESTIMATED_COST:
                    case FlinkSqlParser.KW_JSON_EXECUTION_PLAN:
                        {
                            this.state = 431;
                            this.explainDetails();
                        }
                        break;
                    case FlinkSqlParser.KW_PLAN:
                        {
                            this.state = 432;
                            this.match(FlinkSqlParser.KW_PLAN);
                            this.state = 433;
                            this.match(FlinkSqlParser.KW_FOR);
                        }
                        break;
                    case FlinkSqlParser.KW_BEGIN:
                    case FlinkSqlParser.KW_EXECUTE:
                    case FlinkSqlParser.KW_INSERT:
                    case FlinkSqlParser.KW_SELECT:
                    case FlinkSqlParser.KW_STATEMENT:
                    case FlinkSqlParser.KW_VALUES:
                    case FlinkSqlParser.KW_WITH:
                    case FlinkSqlParser.LR_BRACKET:
                        break;
                    default:
                        break;
                }
                this.state = 439;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 7, this._ctx)) {
                    case 1:
                        {
                            this.state = 436;
                            this.dmlStatement();
                        }
                        break;
                    case 2:
                        {
                            this.state = 437;
                            this.insertSimpleStatement();
                        }
                        break;
                    case 3:
                        {
                            this.state = 438;
                            this.insertMulStatement();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    explainDetails() {
        let _localctx = new ExplainDetailsContext(this._ctx, this.state);
        this.enterRule(_localctx, 16, FlinkSqlParser.RULE_explainDetails);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 441;
                this.explainDetail();
                this.state = 446;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 442;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 443;
                            this.explainDetail();
                        }
                    }
                    this.state = 448;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    explainDetail() {
        let _localctx = new ExplainDetailContext(this._ctx, this.state);
        this.enterRule(_localctx, 18, FlinkSqlParser.RULE_explainDetail);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 449;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_CHANGELOG_MODE || _la === FlinkSqlParser.KW_ESTIMATED_COST || _la === FlinkSqlParser.KW_JSON_EXECUTION_PLAN)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    useStatement() {
        let _localctx = new UseStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 20, FlinkSqlParser.RULE_useStatement);
        try {
            this.state = 457;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 9, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 451;
                        this.match(FlinkSqlParser.KW_USE);
                        this.state = 452;
                        this.match(FlinkSqlParser.KW_CATALOG);
                        this.state = 453;
                        this.catalogPath();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 454;
                        this.match(FlinkSqlParser.KW_USE);
                        this.state = 455;
                        this.databasePath();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 456;
                        this.useModuleStatement();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    useModuleStatement() {
        let _localctx = new UseModuleStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 22, FlinkSqlParser.RULE_useModuleStatement);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 459;
                this.match(FlinkSqlParser.KW_USE);
                this.state = 460;
                this.match(FlinkSqlParser.KW_MODULES);
                this.state = 461;
                this.uid();
                this.state = 466;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 462;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 463;
                            this.uid();
                        }
                    }
                    this.state = 468;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    showStatememt() {
        let _localctx = new ShowStatememtContext(this._ctx, this.state);
        this.enterRule(_localctx, 24, FlinkSqlParser.RULE_showStatememt);
        let _la;
        try {
            this.state = 511;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 18, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 469;
                        this.match(FlinkSqlParser.KW_SHOW);
                        this.state = 470;
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_CATALOGS || _la === FlinkSqlParser.KW_DATABASES || _la === FlinkSqlParser.KW_VIEWS || _la === FlinkSqlParser.KW_JARS)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 471;
                        this.match(FlinkSqlParser.KW_SHOW);
                        this.state = 472;
                        this.match(FlinkSqlParser.KW_CURRENT);
                        this.state = 473;
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_CATALOG || _la === FlinkSqlParser.KW_DATABASE)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 474;
                        this.match(FlinkSqlParser.KW_SHOW);
                        this.state = 475;
                        this.match(FlinkSqlParser.KW_TABLES);
                        this.state = 478;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_FROM || _la === FlinkSqlParser.KW_IN) {
                            {
                                this.state = 476;
                                _la = this._input.LA(1);
                                if (!(_la === FlinkSqlParser.KW_FROM || _la === FlinkSqlParser.KW_IN)) {
                                    this._errHandler.recoverInline(this);
                                }
                                else {
                                    if (this._input.LA(1) === Token.EOF) {
                                        this.matchedEOF = true;
                                    }
                                    this._errHandler.reportMatch(this);
                                    this.consume();
                                }
                                this.state = 477;
                                this.databasePath();
                            }
                        }
                        this.state = 481;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_LIKE || _la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 480;
                                this.likePredicate();
                            }
                        }
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 483;
                        this.match(FlinkSqlParser.KW_SHOW);
                        this.state = 484;
                        this.match(FlinkSqlParser.KW_COLUMNS);
                        this.state = 485;
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_FROM || _la === FlinkSqlParser.KW_IN)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                        this.state = 488;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 13, this._ctx)) {
                            case 1:
                                {
                                    this.state = 486;
                                    this.viewPath();
                                }
                                break;
                            case 2:
                                {
                                    this.state = 487;
                                    this.tablePath();
                                }
                                break;
                        }
                        this.state = 491;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_LIKE || _la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 490;
                                this.likePredicate();
                            }
                        }
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 493;
                        this.match(FlinkSqlParser.KW_SHOW);
                        this.state = 494;
                        this.match(FlinkSqlParser.KW_CREATE);
                        this.state = 499;
                        this._errHandler.sync(this);
                        switch (this._input.LA(1)) {
                            case FlinkSqlParser.KW_TABLE:
                                {
                                    this.state = 495;
                                    this.match(FlinkSqlParser.KW_TABLE);
                                    this.state = 496;
                                    this.tablePath();
                                }
                                break;
                            case FlinkSqlParser.KW_VIEW:
                                {
                                    this.state = 497;
                                    this.match(FlinkSqlParser.KW_VIEW);
                                    this.state = 498;
                                    this.viewPath();
                                }
                                break;
                            default:
                                throw new NoViableAltException(this);
                        }
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 501;
                        this.match(FlinkSqlParser.KW_SHOW);
                        this.state = 503;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_USER) {
                            {
                                this.state = 502;
                                this.match(FlinkSqlParser.KW_USER);
                            }
                        }
                        this.state = 505;
                        this.match(FlinkSqlParser.KW_FUNCTIONS);
                    }
                    break;
                case 7:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 506;
                        this.match(FlinkSqlParser.KW_SHOW);
                        this.state = 508;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_FULL) {
                            {
                                this.state = 507;
                                this.match(FlinkSqlParser.KW_FULL);
                            }
                        }
                        this.state = 510;
                        this.match(FlinkSqlParser.KW_MODULES);
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    loadStatement() {
        let _localctx = new LoadStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 26, FlinkSqlParser.RULE_loadStatement);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 513;
                this.match(FlinkSqlParser.KW_LOAD);
                this.state = 514;
                this.match(FlinkSqlParser.KW_MODULE);
                this.state = 515;
                this.uid();
                this.state = 518;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 19, this._ctx)) {
                    case 1:
                        {
                            this.state = 516;
                            this.match(FlinkSqlParser.KW_WITH);
                            this.state = 517;
                            this.tablePropertyList();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    unloadStatememt() {
        let _localctx = new UnloadStatememtContext(this._ctx, this.state);
        this.enterRule(_localctx, 28, FlinkSqlParser.RULE_unloadStatememt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 520;
                this.match(FlinkSqlParser.KW_UNLOAD);
                this.state = 521;
                this.match(FlinkSqlParser.KW_MODULE);
                this.state = 522;
                this.uid();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    setStatememt() {
        let _localctx = new SetStatememtContext(this._ctx, this.state);
        this.enterRule(_localctx, 30, FlinkSqlParser.RULE_setStatememt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 524;
                this.match(FlinkSqlParser.KW_SET);
                this.state = 526;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 20, this._ctx)) {
                    case 1:
                        {
                            this.state = 525;
                            this.tableProperty();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    resetStatememt() {
        let _localctx = new ResetStatememtContext(this._ctx, this.state);
        this.enterRule(_localctx, 32, FlinkSqlParser.RULE_resetStatememt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 528;
                this.match(FlinkSqlParser.KW_RESET);
                this.state = 530;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 21, this._ctx)) {
                    case 1:
                        {
                            this.state = 529;
                            this.tablePropertyKey();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    jarStatememt() {
        let _localctx = new JarStatememtContext(this._ctx, this.state);
        this.enterRule(_localctx, 34, FlinkSqlParser.RULE_jarStatememt);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 532;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_ADD || _la === FlinkSqlParser.KW_REMOVE)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 533;
                this.match(FlinkSqlParser.KW_JAR);
                this.state = 534;
                this.jarFileName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dtAddStatement() {
        let _localctx = new DtAddStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 36, FlinkSqlParser.RULE_dtAddStatement);
        let _la;
        try {
            this.state = 586;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 25, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 536;
                        this.match(FlinkSqlParser.KW_ADD);
                        this.state = 537;
                        this.match(FlinkSqlParser.KW_JAR);
                        this.state = 538;
                        this.match(FlinkSqlParser.KW_WITH);
                        this.state = 539;
                        this.dtFilePath();
                        this.state = 542;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_AS) {
                            {
                                this.state = 540;
                                this.match(FlinkSqlParser.KW_AS);
                                this.state = 541;
                                this.uid();
                            }
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 544;
                        this.match(FlinkSqlParser.KW_ADD);
                        this.state = 545;
                        this.match(FlinkSqlParser.KW_FILE);
                        this.state = 546;
                        this.match(FlinkSqlParser.KW_WITH);
                        this.state = 547;
                        this.dtFilePath();
                        this.state = 550;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_AS) {
                            {
                                this.state = 548;
                                this.match(FlinkSqlParser.KW_AS);
                                this.state = 549;
                                this.uid();
                            }
                        }
                        this.state = 554;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_RENAME) {
                            {
                                this.state = 552;
                                this.match(FlinkSqlParser.KW_RENAME);
                                this.state = 553;
                                this.uid();
                            }
                        }
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 556;
                        this.match(FlinkSqlParser.KW_ADD);
                        this.state = 557;
                        _la = this._input.LA(1);
                        if (!(((((_la - 482)) & ~0x1F) === 0 && ((1 << (_la - 482)) & ((1 << (FlinkSqlParser.KW_PYTHON_ARCHIVES - 482)) | (1 << (FlinkSqlParser.KW_PYTHON_DEPENDENCIES - 482)) | (1 << (FlinkSqlParser.KW_PYTHON_FILES - 482)) | (1 << (FlinkSqlParser.KW_PYTHON_JAR - 482)) | (1 << (FlinkSqlParser.KW_PYTHON_REQUIREMENTS - 482)))) !== 0))) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                        this.state = 558;
                        this.match(FlinkSqlParser.KW_WITH);
                        this.state = 559;
                        this.dtFilePath();
                        this.state = 560;
                        this.match(FlinkSqlParser.KW_RENAME);
                        this.state = 561;
                        this.uid();
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 563;
                        this.match(FlinkSqlParser.KW_ADD);
                        this.state = 564;
                        this.match(FlinkSqlParser.KW_PYTHON_PARAMETER);
                        this.state = 565;
                        this.dtFilePath();
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 566;
                        this.match(FlinkSqlParser.KW_ADD);
                        this.state = 567;
                        this.match(FlinkSqlParser.KW_ENGINE);
                        this.state = 568;
                        this.match(FlinkSqlParser.KW_FILE);
                        this.state = 569;
                        this.match(FlinkSqlParser.KW_WITH);
                        this.state = 570;
                        this.dtFilePath();
                        this.state = 571;
                        this.match(FlinkSqlParser.KW_RENAME);
                        this.state = 572;
                        this.uid();
                        this.state = 573;
                        this.match(FlinkSqlParser.KW_KEY);
                        this.state = 574;
                        this.uid();
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 576;
                        this.match(FlinkSqlParser.KW_ADD);
                        this.state = 577;
                        this.match(FlinkSqlParser.KW_CONFIG);
                        this.state = 578;
                        this.match(FlinkSqlParser.KW_FILE);
                        this.state = 579;
                        this.match(FlinkSqlParser.KW_WITH);
                        this.state = 580;
                        this.dtFilePath();
                        this.state = 581;
                        this.match(FlinkSqlParser.KW_FOR);
                        this.state = 582;
                        this.uid();
                        this.state = 583;
                        this.match(FlinkSqlParser.KW_AS);
                        this.state = 584;
                        this.uid();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dtFilePath() {
        let _localctx = new DtFilePathContext(this._ctx, this.state);
        this.enterRule(_localctx, 38, FlinkSqlParser.RULE_dtFilePath);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 592;
                this._errHandler.sync(this);
                _alt = 1;
                do {
                    switch (_alt) {
                        case 1:
                            {
                                {
                                    this.state = 589;
                                    this._errHandler.sync(this);
                                    _la = this._input.LA(1);
                                    if (_la === FlinkSqlParser.SLASH_SIGN) {
                                        {
                                            this.state = 588;
                                            this.match(FlinkSqlParser.SLASH_SIGN);
                                        }
                                    }
                                    this.state = 591;
                                    this.uid();
                                }
                            }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    this.state = 594;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 27, this._ctx);
                } while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    createTable() {
        let _localctx = new CreateTableContext(this._ctx, this.state);
        this.enterRule(_localctx, 40, FlinkSqlParser.RULE_createTable);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 598;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 28, this._ctx)) {
                    case 1:
                        {
                            this.state = 596;
                            this.simpleCreateTable();
                        }
                        break;
                    case 2:
                        {
                            this.state = 597;
                            this.createTableAsSelect();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    simpleCreateTable() {
        let _localctx = new SimpleCreateTableContext(this._ctx, this.state);
        this.enterRule(_localctx, 42, FlinkSqlParser.RULE_simpleCreateTable);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 600;
                this.match(FlinkSqlParser.KW_CREATE);
                this.state = 602;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_TEMPORARY) {
                    {
                        this.state = 601;
                        this.match(FlinkSqlParser.KW_TEMPORARY);
                    }
                }
                this.state = 604;
                this.match(FlinkSqlParser.KW_TABLE);
                this.state = 606;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 605;
                        this.ifNotExists();
                    }
                }
                this.state = 608;
                this.tablePathCreate();
                this.state = 609;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 610;
                this.columnOptionDefinition();
                this.state = 615;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 31, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        {
                            {
                                this.state = 611;
                                this.match(FlinkSqlParser.COMMA);
                                this.state = 612;
                                this.columnOptionDefinition();
                            }
                        }
                    }
                    this.state = 617;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 31, this._ctx);
                }
                this.state = 620;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 32, this._ctx)) {
                    case 1:
                        {
                            this.state = 618;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 619;
                            this.watermarkDefinition();
                        }
                        break;
                }
                this.state = 624;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 33, this._ctx)) {
                    case 1:
                        {
                            this.state = 622;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 623;
                            this.tableConstraint();
                        }
                        break;
                }
                this.state = 628;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.COMMA) {
                    {
                        this.state = 626;
                        this.match(FlinkSqlParser.COMMA);
                        this.state = 627;
                        this.selfDefinitionClause();
                    }
                }
                this.state = 630;
                this.match(FlinkSqlParser.RR_BRACKET);
                this.state = 632;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_COMMENT) {
                    {
                        this.state = 631;
                        this.commentSpec();
                    }
                }
                this.state = 635;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_PARTITIONED) {
                    {
                        this.state = 634;
                        this.partitionDefinition();
                    }
                }
                this.state = 637;
                this.withOption();
                this.state = 639;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_LIKE) {
                    {
                        this.state = 638;
                        this.likeDefinition();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    createTableAsSelect() {
        let _localctx = new CreateTableAsSelectContext(this._ctx, this.state);
        this.enterRule(_localctx, 44, FlinkSqlParser.RULE_createTableAsSelect);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 641;
                this.match(FlinkSqlParser.KW_CREATE);
                this.state = 642;
                this.match(FlinkSqlParser.KW_TABLE);
                this.state = 644;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 643;
                        this.ifNotExists();
                    }
                }
                this.state = 646;
                this.tablePathCreate();
                this.state = 647;
                this.withOption();
                this.state = 650;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_AS) {
                    {
                        this.state = 648;
                        this.match(FlinkSqlParser.KW_AS);
                        this.state = 649;
                        this.queryStatement(0);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    columnOptionDefinition() {
        let _localctx = new ColumnOptionDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 46, FlinkSqlParser.RULE_columnOptionDefinition);
        try {
            this.state = 655;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 40, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 652;
                        this.physicalColumnDefinition();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 653;
                        this.metadataColumnDefinition();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 654;
                        this.computedColumnDefinition();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    physicalColumnDefinition() {
        let _localctx = new PhysicalColumnDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 48, FlinkSqlParser.RULE_physicalColumnDefinition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 657;
                this.columnNameCreate();
                this.state = 658;
                this.columnType();
                this.state = 660;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_CONSTRAINT || _la === FlinkSqlParser.KW_NOT || _la === FlinkSqlParser.KW_NULL || _la === FlinkSqlParser.KW_PRIMARY) {
                    {
                        this.state = 659;
                        this.columnConstraint();
                    }
                }
                this.state = 663;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_COMMENT) {
                    {
                        this.state = 662;
                        this.commentSpec();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    columnNameCreate() {
        let _localctx = new ColumnNameCreateContext(this._ctx, this.state);
        this.enterRule(_localctx, 50, FlinkSqlParser.RULE_columnNameCreate);
        try {
            this.state = 667;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 43, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 665;
                        this.uid();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 666;
                        this.expression();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    columnName() {
        let _localctx = new ColumnNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 52, FlinkSqlParser.RULE_columnName);
        try {
            this.state = 671;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 44, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 669;
                        this.uid();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 670;
                        this.expression();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    columnNameList() {
        let _localctx = new ColumnNameListContext(this._ctx, this.state);
        this.enterRule(_localctx, 54, FlinkSqlParser.RULE_columnNameList);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 673;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 674;
                this.columnName();
                this.state = 679;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 675;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 676;
                            this.columnName();
                        }
                    }
                    this.state = 681;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
                this.state = 682;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    columnType() {
        let _localctx = new ColumnTypeContext(this._ctx, this.state);
        this.enterRule(_localctx, 56, FlinkSqlParser.RULE_columnType);
        let _la;
        try {
            this.state = 721;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_BOOLEAN:
                case FlinkSqlParser.KW_DATE:
                case FlinkSqlParser.KW_NULL:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 684;
                        _localctx._typeName = this._input.LT(1);
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_BOOLEAN || _la === FlinkSqlParser.KW_DATE || _la === FlinkSqlParser.KW_NULL)) {
                            _localctx._typeName = this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                    break;
                case FlinkSqlParser.KW_BIGINT:
                case FlinkSqlParser.KW_BINARY:
                case FlinkSqlParser.KW_BYTES:
                case FlinkSqlParser.KW_CHAR:
                case FlinkSqlParser.KW_DATETIME:
                case FlinkSqlParser.KW_INT:
                case FlinkSqlParser.KW_INTEGER:
                case FlinkSqlParser.KW_SMALLINT:
                case FlinkSqlParser.KW_STRING:
                case FlinkSqlParser.KW_TIME:
                case FlinkSqlParser.KW_TIMESTAMP_LTZ:
                case FlinkSqlParser.KW_TINYINT:
                case FlinkSqlParser.KW_VARBINARY:
                case FlinkSqlParser.KW_VARCHAR:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 685;
                        _localctx._typeName = this._input.LT(1);
                        _la = this._input.LA(1);
                        if (!(((((_la - 28)) & ~0x1F) === 0 && ((1 << (_la - 28)) & ((1 << (FlinkSqlParser.KW_BIGINT - 28)) | (1 << (FlinkSqlParser.KW_BINARY - 28)) | (1 << (FlinkSqlParser.KW_BYTES - 28)) | (1 << (FlinkSqlParser.KW_CHAR - 28)))) !== 0) || _la === FlinkSqlParser.KW_DATETIME || _la === FlinkSqlParser.KW_INT || _la === FlinkSqlParser.KW_INTEGER || _la === FlinkSqlParser.KW_SMALLINT || _la === FlinkSqlParser.KW_STRING || ((((_la - 378)) & ~0x1F) === 0 && ((1 << (_la - 378)) & ((1 << (FlinkSqlParser.KW_TIME - 378)) | (1 << (FlinkSqlParser.KW_TIMESTAMP_LTZ - 378)) | (1 << (FlinkSqlParser.KW_TINYINT - 378)))) !== 0) || _la === FlinkSqlParser.KW_VARBINARY || _la === FlinkSqlParser.KW_VARCHAR)) {
                            _localctx._typeName = this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                        this.state = 687;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.LR_BRACKET) {
                            {
                                this.state = 686;
                                this.lengthOneDimension();
                            }
                        }
                    }
                    break;
                case FlinkSqlParser.KW_TIMESTAMP:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 689;
                        _localctx._typeName = this.match(FlinkSqlParser.KW_TIMESTAMP);
                        this.state = 691;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.LR_BRACKET) {
                            {
                                this.state = 690;
                                this.lengthOneDimension();
                            }
                        }
                        this.state = 699;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_WITH || _la === FlinkSqlParser.KW_WITHOUT) {
                            {
                                this.state = 693;
                                _la = this._input.LA(1);
                                if (!(_la === FlinkSqlParser.KW_WITH || _la === FlinkSqlParser.KW_WITHOUT)) {
                                    this._errHandler.recoverInline(this);
                                }
                                else {
                                    if (this._input.LA(1) === Token.EOF) {
                                        this.matchedEOF = true;
                                    }
                                    this._errHandler.reportMatch(this);
                                    this.consume();
                                }
                                this.state = 695;
                                this._errHandler.sync(this);
                                _la = this._input.LA(1);
                                if (_la === FlinkSqlParser.KW_LOCAL) {
                                    {
                                        this.state = 694;
                                        this.match(FlinkSqlParser.KW_LOCAL);
                                    }
                                }
                                this.state = 697;
                                this.match(FlinkSqlParser.KW_TIME);
                                this.state = 698;
                                this.match(FlinkSqlParser.KW_ZONE);
                            }
                        }
                    }
                    break;
                case FlinkSqlParser.KW_DEC:
                case FlinkSqlParser.KW_DECIMAL:
                case FlinkSqlParser.KW_DOUBLE:
                case FlinkSqlParser.KW_FLOAT:
                case FlinkSqlParser.KW_NUMERIC:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 701;
                        _localctx._typeName = this._input.LT(1);
                        _la = this._input.LA(1);
                        if (!(((((_la - 101)) & ~0x1F) === 0 && ((1 << (_la - 101)) & ((1 << (FlinkSqlParser.KW_DEC - 101)) | (1 << (FlinkSqlParser.KW_DECIMAL - 101)) | (1 << (FlinkSqlParser.KW_DOUBLE - 101)))) !== 0) || _la === FlinkSqlParser.KW_FLOAT || _la === FlinkSqlParser.KW_NUMERIC)) {
                            _localctx._typeName = this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                        this.state = 703;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.LR_BRACKET) {
                            {
                                this.state = 702;
                                this.lengthTwoOptionalDimension();
                            }
                        }
                    }
                    break;
                case FlinkSqlParser.KW_ARRAY:
                case FlinkSqlParser.KW_MULTISET:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 705;
                        _localctx._type = this._input.LT(1);
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_ARRAY || _la === FlinkSqlParser.KW_MULTISET)) {
                            _localctx._type = this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                        this.state = 707;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.LESS_SYMBOL) {
                            {
                                this.state = 706;
                                this.lengthOneTypeDimension();
                            }
                        }
                    }
                    break;
                case FlinkSqlParser.KW_MAP:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 709;
                        _localctx._type = this.match(FlinkSqlParser.KW_MAP);
                        this.state = 711;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.LESS_SYMBOL) {
                            {
                                this.state = 710;
                                this.mapTypeDimension();
                            }
                        }
                    }
                    break;
                case FlinkSqlParser.KW_ROW:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 713;
                        _localctx._type = this.match(FlinkSqlParser.KW_ROW);
                        this.state = 715;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.LESS_SYMBOL) {
                            {
                                this.state = 714;
                                this.rowTypeDimension();
                            }
                        }
                    }
                    break;
                case FlinkSqlParser.KW_RAW:
                    this.enterOuterAlt(_localctx, 8);
                    {
                        this.state = 717;
                        _localctx._type = this.match(FlinkSqlParser.KW_RAW);
                        this.state = 719;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.LR_BRACKET) {
                            {
                                this.state = 718;
                                this.lengthTwoStringDimension();
                            }
                        }
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    lengthOneDimension() {
        let _localctx = new LengthOneDimensionContext(this._ctx, this.state);
        this.enterRule(_localctx, 58, FlinkSqlParser.RULE_lengthOneDimension);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 723;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 724;
                this.decimalLiteral();
                this.state = 725;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    lengthTwoOptionalDimension() {
        let _localctx = new LengthTwoOptionalDimensionContext(this._ctx, this.state);
        this.enterRule(_localctx, 60, FlinkSqlParser.RULE_lengthTwoOptionalDimension);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 727;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 728;
                this.decimalLiteral();
                this.state = 731;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.COMMA) {
                    {
                        this.state = 729;
                        this.match(FlinkSqlParser.COMMA);
                        this.state = 730;
                        this.decimalLiteral();
                    }
                }
                this.state = 733;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    lengthTwoStringDimension() {
        let _localctx = new LengthTwoStringDimensionContext(this._ctx, this.state);
        this.enterRule(_localctx, 62, FlinkSqlParser.RULE_lengthTwoStringDimension);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 735;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 736;
                this.stringLiteral();
                this.state = 739;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.COMMA) {
                    {
                        this.state = 737;
                        this.match(FlinkSqlParser.COMMA);
                        this.state = 738;
                        this.stringLiteral();
                    }
                }
                this.state = 741;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    lengthOneTypeDimension() {
        let _localctx = new LengthOneTypeDimensionContext(this._ctx, this.state);
        this.enterRule(_localctx, 64, FlinkSqlParser.RULE_lengthOneTypeDimension);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 743;
                this.match(FlinkSqlParser.LESS_SYMBOL);
                this.state = 744;
                this.columnType();
                this.state = 745;
                this.match(FlinkSqlParser.GREATER_SYMBOL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    mapTypeDimension() {
        let _localctx = new MapTypeDimensionContext(this._ctx, this.state);
        this.enterRule(_localctx, 66, FlinkSqlParser.RULE_mapTypeDimension);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 747;
                this.match(FlinkSqlParser.LESS_SYMBOL);
                this.state = 748;
                this.columnType();
                {
                    this.state = 749;
                    this.match(FlinkSqlParser.COMMA);
                    this.state = 750;
                    this.columnType();
                }
                this.state = 752;
                this.match(FlinkSqlParser.GREATER_SYMBOL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    rowTypeDimension() {
        let _localctx = new RowTypeDimensionContext(this._ctx, this.state);
        this.enterRule(_localctx, 68, FlinkSqlParser.RULE_rowTypeDimension);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 754;
                this.match(FlinkSqlParser.LESS_SYMBOL);
                this.state = 755;
                this.columnName();
                this.state = 756;
                this.columnType();
                this.state = 763;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 757;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 758;
                            this.columnName();
                            this.state = 759;
                            this.columnType();
                        }
                    }
                    this.state = 765;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
                this.state = 766;
                this.match(FlinkSqlParser.GREATER_SYMBOL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    columnConstraint() {
        let _localctx = new ColumnConstraintContext(this._ctx, this.state);
        this.enterRule(_localctx, 70, FlinkSqlParser.RULE_columnConstraint);
        let _la;
        try {
            this.state = 782;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_CONSTRAINT:
                case FlinkSqlParser.KW_PRIMARY:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 770;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_CONSTRAINT) {
                            {
                                this.state = 768;
                                this.match(FlinkSqlParser.KW_CONSTRAINT);
                                this.state = 769;
                                this.constraintName();
                            }
                        }
                        this.state = 772;
                        this.match(FlinkSqlParser.KW_PRIMARY);
                        this.state = 773;
                        this.match(FlinkSqlParser.KW_KEY);
                        this.state = 776;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 774;
                                this.match(FlinkSqlParser.KW_NOT);
                                this.state = 775;
                                this.match(FlinkSqlParser.KW_ENFORCED);
                            }
                        }
                    }
                    break;
                case FlinkSqlParser.KW_NOT:
                case FlinkSqlParser.KW_NULL:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 779;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 778;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 781;
                        this.match(FlinkSqlParser.KW_NULL);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    commentSpec() {
        let _localctx = new CommentSpecContext(this._ctx, this.state);
        this.enterRule(_localctx, 72, FlinkSqlParser.RULE_commentSpec);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 784;
                this.match(FlinkSqlParser.KW_COMMENT);
                this.state = 785;
                this.match(FlinkSqlParser.STRING_LITERAL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    metadataColumnDefinition() {
        let _localctx = new MetadataColumnDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 74, FlinkSqlParser.RULE_metadataColumnDefinition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 787;
                this.columnNameCreate();
                this.state = 788;
                this.columnType();
                this.state = 789;
                this.match(FlinkSqlParser.KW_METADATA);
                this.state = 792;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_FROM) {
                    {
                        this.state = 790;
                        this.match(FlinkSqlParser.KW_FROM);
                        this.state = 791;
                        this.metadataKey();
                    }
                }
                this.state = 795;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_VIRTUAL) {
                    {
                        this.state = 794;
                        this.match(FlinkSqlParser.KW_VIRTUAL);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    metadataKey() {
        let _localctx = new MetadataKeyContext(this._ctx, this.state);
        this.enterRule(_localctx, 76, FlinkSqlParser.RULE_metadataKey);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 797;
                this.match(FlinkSqlParser.STRING_LITERAL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    computedColumnDefinition() {
        let _localctx = new ComputedColumnDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 78, FlinkSqlParser.RULE_computedColumnDefinition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 799;
                this.columnNameCreate();
                this.state = 800;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 801;
                this.computedColumnExpression();
                this.state = 803;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_COMMENT) {
                    {
                        this.state = 802;
                        this.commentSpec();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    computedColumnExpression() {
        let _localctx = new ComputedColumnExpressionContext(this._ctx, this.state);
        this.enterRule(_localctx, 80, FlinkSqlParser.RULE_computedColumnExpression);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 805;
                this.expression();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    watermarkDefinition() {
        let _localctx = new WatermarkDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 82, FlinkSqlParser.RULE_watermarkDefinition);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 807;
                this.match(FlinkSqlParser.KW_WATERMARK);
                this.state = 808;
                this.match(FlinkSqlParser.KW_FOR);
                this.state = 809;
                this.columnName();
                this.state = 810;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 811;
                this.expression();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tableConstraint() {
        let _localctx = new TableConstraintContext(this._ctx, this.state);
        this.enterRule(_localctx, 84, FlinkSqlParser.RULE_tableConstraint);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 815;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_CONSTRAINT) {
                    {
                        this.state = 813;
                        this.match(FlinkSqlParser.KW_CONSTRAINT);
                        this.state = 814;
                        this.constraintName();
                    }
                }
                this.state = 817;
                this.match(FlinkSqlParser.KW_PRIMARY);
                this.state = 818;
                this.match(FlinkSqlParser.KW_KEY);
                this.state = 819;
                this.columnNameList();
                this.state = 820;
                this.match(FlinkSqlParser.KW_NOT);
                this.state = 821;
                this.match(FlinkSqlParser.KW_ENFORCED);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    constraintName() {
        let _localctx = new ConstraintNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 86, FlinkSqlParser.RULE_constraintName);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 823;
                this.identifier();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    selfDefinitionClause() {
        let _localctx = new SelfDefinitionClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 88, FlinkSqlParser.RULE_selfDefinitionClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 825;
                this.match(FlinkSqlParser.KW_PERIOD);
                this.state = 826;
                this.match(FlinkSqlParser.KW_FOR);
                this.state = 827;
                this.match(FlinkSqlParser.KW_SYSTEM_TIME);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    partitionDefinition() {
        let _localctx = new PartitionDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 90, FlinkSqlParser.RULE_partitionDefinition);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 829;
                this.match(FlinkSqlParser.KW_PARTITIONED);
                this.state = 830;
                this.match(FlinkSqlParser.KW_BY);
                this.state = 831;
                this.transformList();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    transformList() {
        let _localctx = new TransformListContext(this._ctx, this.state);
        this.enterRule(_localctx, 92, FlinkSqlParser.RULE_transformList);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 833;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 834;
                this.transform();
                this.state = 839;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 835;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 836;
                            this.transform();
                        }
                    }
                    this.state = 841;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
                this.state = 842;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    transform() {
        let _localctx = new TransformContext(this._ctx, this.state);
        this.enterRule(_localctx, 94, FlinkSqlParser.RULE_transform);
        let _la;
        try {
            this.state = 857;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 69, this._ctx)) {
                case 1:
                    _localctx = new IdentityTransformContext(_localctx);
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 844;
                        this.columnName();
                    }
                    break;
                case 2:
                    _localctx = new ColumnTransformContext(_localctx);
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 845;
                        this.qualifiedName();
                    }
                    break;
                case 3:
                    _localctx = new ApplyTransformContext(_localctx);
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 846;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 847;
                        this.transformArgument();
                        this.state = 852;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        while (_la === FlinkSqlParser.COMMA) {
                            {
                                {
                                    this.state = 848;
                                    this.match(FlinkSqlParser.COMMA);
                                    this.state = 849;
                                    this.transformArgument();
                                }
                            }
                            this.state = 854;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        }
                        this.state = 855;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    transformArgument() {
        let _localctx = new TransformArgumentContext(this._ctx, this.state);
        this.enterRule(_localctx, 96, FlinkSqlParser.RULE_transformArgument);
        try {
            this.state = 861;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 70, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 859;
                        this.qualifiedName();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 860;
                        this.constant();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    likeDefinition() {
        let _localctx = new LikeDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 98, FlinkSqlParser.RULE_likeDefinition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 863;
                this.match(FlinkSqlParser.KW_LIKE);
                this.state = 864;
                this.tablePath();
                this.state = 873;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 72, this._ctx)) {
                    case 1:
                        {
                            this.state = 865;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 869;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            while (_la === FlinkSqlParser.KW_OVERWRITING || _la === FlinkSqlParser.KW_EXCLUDING || _la === FlinkSqlParser.KW_INCLUDING) {
                                {
                                    {
                                        this.state = 866;
                                        this.likeOption();
                                    }
                                }
                                this.state = 871;
                                this._errHandler.sync(this);
                                _la = this._input.LA(1);
                            }
                            this.state = 872;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    likeOption() {
        let _localctx = new LikeOptionContext(this._ctx, this.state);
        this.enterRule(_localctx, 100, FlinkSqlParser.RULE_likeOption);
        let _la;
        try {
            this.state = 879;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 73, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        {
                            this.state = 875;
                            _la = this._input.LA(1);
                            if (!(_la === FlinkSqlParser.KW_EXCLUDING || _la === FlinkSqlParser.KW_INCLUDING)) {
                                this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                            this.state = 876;
                            _la = this._input.LA(1);
                            if (!(_la === FlinkSqlParser.KW_ALL || _la === FlinkSqlParser.KW_PARTITIONS || _la === FlinkSqlParser.KW_CONSTRAINTS)) {
                                this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        {
                            this.state = 877;
                            _la = this._input.LA(1);
                            if (!(_la === FlinkSqlParser.KW_OVERWRITING || _la === FlinkSqlParser.KW_EXCLUDING || _la === FlinkSqlParser.KW_INCLUDING)) {
                                this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                            this.state = 878;
                            _la = this._input.LA(1);
                            if (!(_la === FlinkSqlParser.KW_WATERMARKS || _la === FlinkSqlParser.KW_GENERATED || _la === FlinkSqlParser.KW_OPTIONS)) {
                                this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    createCatalog() {
        let _localctx = new CreateCatalogContext(this._ctx, this.state);
        this.enterRule(_localctx, 102, FlinkSqlParser.RULE_createCatalog);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 881;
                this.match(FlinkSqlParser.KW_CREATE);
                this.state = 882;
                this.match(FlinkSqlParser.KW_CATALOG);
                this.state = 883;
                this.catalogPathCreate();
                this.state = 884;
                this.withOption();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    createDatabase() {
        let _localctx = new CreateDatabaseContext(this._ctx, this.state);
        this.enterRule(_localctx, 104, FlinkSqlParser.RULE_createDatabase);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 886;
                this.match(FlinkSqlParser.KW_CREATE);
                this.state = 887;
                this.match(FlinkSqlParser.KW_DATABASE);
                this.state = 889;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 888;
                        this.ifNotExists();
                    }
                }
                this.state = 891;
                this.databasePathCreate();
                this.state = 893;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_COMMENT) {
                    {
                        this.state = 892;
                        this.commentSpec();
                    }
                }
                this.state = 895;
                this.withOption();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    createView() {
        let _localctx = new CreateViewContext(this._ctx, this.state);
        this.enterRule(_localctx, 106, FlinkSqlParser.RULE_createView);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 897;
                this.match(FlinkSqlParser.KW_CREATE);
                this.state = 899;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_TEMPORARY) {
                    {
                        this.state = 898;
                        this.match(FlinkSqlParser.KW_TEMPORARY);
                    }
                }
                this.state = 901;
                this.match(FlinkSqlParser.KW_VIEW);
                this.state = 903;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 902;
                        this.ifNotExists();
                    }
                }
                this.state = 905;
                this.viewPathCreate();
                this.state = 907;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.LR_BRACKET) {
                    {
                        this.state = 906;
                        this.columnNameList();
                    }
                }
                this.state = 910;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_COMMENT) {
                    {
                        this.state = 909;
                        this.commentSpec();
                    }
                }
                this.state = 912;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 913;
                this.queryStatement(0);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    createFunction() {
        let _localctx = new CreateFunctionContext(this._ctx, this.state);
        this.enterRule(_localctx, 108, FlinkSqlParser.RULE_createFunction);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 915;
                this.match(FlinkSqlParser.KW_CREATE);
                this.state = 919;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 80, this._ctx)) {
                    case 1:
                        {
                            this.state = 916;
                            this.match(FlinkSqlParser.KW_TEMPORARY);
                        }
                        break;
                    case 2:
                        {
                            this.state = 917;
                            this.match(FlinkSqlParser.KW_TEMPORARY);
                            this.state = 918;
                            this.match(FlinkSqlParser.KW_SYSTEM);
                        }
                        break;
                }
                this.state = 921;
                this.match(FlinkSqlParser.KW_FUNCTION);
                this.state = 923;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 922;
                        this.ifNotExists();
                    }
                }
                this.state = 925;
                this.functionNameCreate();
                this.state = 926;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 927;
                this.identifier();
                this.state = 930;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_LANGUAGE) {
                    {
                        this.state = 928;
                        this.match(FlinkSqlParser.KW_LANGUAGE);
                        this.state = 929;
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_SCALA || _la === FlinkSqlParser.KW_JAVA || _la === FlinkSqlParser.KW_PYTHON)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 933;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_USING) {
                    {
                        this.state = 932;
                        this.usingClause();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    usingClause() {
        let _localctx = new UsingClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 110, FlinkSqlParser.RULE_usingClause);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 935;
                this.match(FlinkSqlParser.KW_USING);
                this.state = 936;
                this.match(FlinkSqlParser.KW_JAR);
                this.state = 937;
                this.jarFileName();
                this.state = 943;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 938;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 939;
                            this.match(FlinkSqlParser.KW_JAR);
                            this.state = 940;
                            this.jarFileName();
                        }
                    }
                    this.state = 945;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    jarFileName() {
        let _localctx = new JarFileNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 112, FlinkSqlParser.RULE_jarFileName);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 946;
                this.match(FlinkSqlParser.STRING_LITERAL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    alterTable() {
        let _localctx = new AlterTableContext(this._ctx, this.state);
        this.enterRule(_localctx, 114, FlinkSqlParser.RULE_alterTable);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 948;
                this.match(FlinkSqlParser.KW_ALTER);
                this.state = 949;
                this.match(FlinkSqlParser.KW_TABLE);
                this.state = 951;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 950;
                        this.ifExists();
                    }
                }
                this.state = 953;
                this.tablePath();
                this.state = 959;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 86, this._ctx)) {
                    case 1:
                        {
                            this.state = 954;
                            this.renameDefinition();
                        }
                        break;
                    case 2:
                        {
                            this.state = 955;
                            this.setKeyValueDefinition();
                        }
                        break;
                    case 3:
                        {
                            this.state = 956;
                            this.addConstraint();
                        }
                        break;
                    case 4:
                        {
                            this.state = 957;
                            this.dropConstraint();
                        }
                        break;
                    case 5:
                        {
                            this.state = 958;
                            this.addUnique();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    renameDefinition() {
        let _localctx = new RenameDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 116, FlinkSqlParser.RULE_renameDefinition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 961;
                this.match(FlinkSqlParser.KW_RENAME);
                this.state = 963;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 437)) & ~0x1F) === 0 && ((1 << (_la - 437)) & ((1 << (FlinkSqlParser.KW_ADD - 437)) | (1 << (FlinkSqlParser.KW_AFTER - 437)) | (1 << (FlinkSqlParser.KW_ASC - 437)) | (1 << (FlinkSqlParser.KW_CASCADE - 437)) | (1 << (FlinkSqlParser.KW_CATALOG - 437)) | (1 << (FlinkSqlParser.KW_CENTURY - 437)) | (1 << (FlinkSqlParser.KW_CONFIG - 437)) | (1 << (FlinkSqlParser.KW_CONSTRAINTS - 437)) | (1 << (FlinkSqlParser.KW_CUMULATE - 437)) | (1 << (FlinkSqlParser.KW_DATA - 437)) | (1 << (FlinkSqlParser.KW_DATABASE - 437)) | (1 << (FlinkSqlParser.KW_DAYS - 437)) | (1 << (FlinkSqlParser.KW_DECADE - 437)) | (1 << (FlinkSqlParser.KW_DESC - 437)) | (1 << (FlinkSqlParser.KW_DESCRIPTOR - 437)) | (1 << (FlinkSqlParser.KW_DIV - 437)) | (1 << (FlinkSqlParser.KW_ENGINE - 437)) | (1 << (FlinkSqlParser.KW_EPOCH - 437)) | (1 << (FlinkSqlParser.KW_EXCLUDING - 437)) | (1 << (FlinkSqlParser.KW_FILE - 437)) | (1 << (FlinkSqlParser.KW_FIRST - 437)) | (1 << (FlinkSqlParser.KW_GENERATED - 437)) | (1 << (FlinkSqlParser.KW_HOP - 437)) | (1 << (FlinkSqlParser.KW_HOURS - 437)) | (1 << (FlinkSqlParser.KW_IGNORE - 437)) | (1 << (FlinkSqlParser.KW_INCLUDING - 437)) | (1 << (FlinkSqlParser.KW_JAR - 437)) | (1 << (FlinkSqlParser.KW_JARS - 437)) | (1 << (FlinkSqlParser.KW_JAVA - 437)) | (1 << (FlinkSqlParser.KW_KEY - 437)) | (1 << (FlinkSqlParser.KW_LAST - 437)) | (1 << (FlinkSqlParser.KW_LOAD - 437)))) !== 0) || ((((_la - 469)) & ~0x1F) === 0 && ((1 << (_la - 469)) & ((1 << (FlinkSqlParser.KW_MAP - 469)) | (1 << (FlinkSqlParser.KW_MICROSECOND - 469)) | (1 << (FlinkSqlParser.KW_MILLENNIUM - 469)) | (1 << (FlinkSqlParser.KW_MILLISECOND - 469)) | (1 << (FlinkSqlParser.KW_MINUTES - 469)) | (1 << (FlinkSqlParser.KW_MONTHS - 469)) | (1 << (FlinkSqlParser.KW_NANOSECOND - 469)) | (1 << (FlinkSqlParser.KW_NULLS - 469)) | (1 << (FlinkSqlParser.KW_OPTIONS - 469)) | (1 << (FlinkSqlParser.KW_PAST - 469)) | (1 << (FlinkSqlParser.KW_PLAN - 469)) | (1 << (FlinkSqlParser.KW_PRECEDING - 469)) | (1 << (FlinkSqlParser.KW_PYTHON - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_ARCHIVES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_DEPENDENCIES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_FILES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_JAR - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_PARAMETER - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_REQUIREMENTS - 469)) | (1 << (FlinkSqlParser.KW_QUARTER - 469)) | (1 << (FlinkSqlParser.KW_REMOVE - 469)) | (1 << (FlinkSqlParser.KW_RESTRICT - 469)) | (1 << (FlinkSqlParser.KW_SECONDS - 469)) | (1 << (FlinkSqlParser.KW_SESSION - 469)) | (1 << (FlinkSqlParser.KW_SETS - 469)) | (1 << (FlinkSqlParser.KW_SIZE - 469)) | (1 << (FlinkSqlParser.KW_SLIDE - 469)) | (1 << (FlinkSqlParser.KW_STEP - 469)) | (1 << (FlinkSqlParser.KW_TEMPORARY - 469)) | (1 << (FlinkSqlParser.KW_TIMECOL - 469)) | (1 << (FlinkSqlParser.KW_TUMBLE - 469)) | (1 << (FlinkSqlParser.KW_UNLOAD - 469)))) !== 0) || ((((_la - 501)) & ~0x1F) === 0 && ((1 << (_la - 501)) & ((1 << (FlinkSqlParser.KW_VIEW - 501)) | (1 << (FlinkSqlParser.KW_WEEK - 501)) | (1 << (FlinkSqlParser.KW_YEARS - 501)) | (1 << (FlinkSqlParser.KW_ZONE - 501)))) !== 0) || ((((_la - 537)) & ~0x1F) === 0 && ((1 << (_la - 537)) & ((1 << (FlinkSqlParser.STRING_LITERAL - 537)) | (1 << (FlinkSqlParser.DIG_LITERAL - 537)) | (1 << (FlinkSqlParser.ID_LITERAL - 537)))) !== 0)) {
                    {
                        this.state = 962;
                        this.uid();
                    }
                }
                this.state = 965;
                this.match(FlinkSqlParser.KW_TO);
                this.state = 966;
                this.uid();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    setKeyValueDefinition() {
        let _localctx = new SetKeyValueDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 118, FlinkSqlParser.RULE_setKeyValueDefinition);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 968;
                this.match(FlinkSqlParser.KW_SET);
                this.state = 969;
                this.tablePropertyList();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    addConstraint() {
        let _localctx = new AddConstraintContext(this._ctx, this.state);
        this.enterRule(_localctx, 120, FlinkSqlParser.RULE_addConstraint);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 971;
                this.match(FlinkSqlParser.KW_ADD);
                this.state = 972;
                this.match(FlinkSqlParser.KW_CONSTRAINT);
                this.state = 973;
                this.constraintName();
                this.state = 974;
                this.match(FlinkSqlParser.KW_PRIMARY);
                this.state = 975;
                this.match(FlinkSqlParser.KW_KEY);
                this.state = 976;
                this.columnNameList();
                this.state = 978;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_NOT) {
                    {
                        this.state = 977;
                        this.notForced();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dropConstraint() {
        let _localctx = new DropConstraintContext(this._ctx, this.state);
        this.enterRule(_localctx, 122, FlinkSqlParser.RULE_dropConstraint);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 980;
                this.match(FlinkSqlParser.KW_DROP);
                this.state = 981;
                this.match(FlinkSqlParser.KW_CONSTRAINT);
                this.state = 982;
                this.constraintName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    addUnique() {
        let _localctx = new AddUniqueContext(this._ctx, this.state);
        this.enterRule(_localctx, 124, FlinkSqlParser.RULE_addUnique);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 984;
                this.match(FlinkSqlParser.KW_ADD);
                this.state = 985;
                this.match(FlinkSqlParser.KW_UNIQUE);
                this.state = 986;
                this.columnNameList();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    notForced() {
        let _localctx = new NotForcedContext(this._ctx, this.state);
        this.enterRule(_localctx, 126, FlinkSqlParser.RULE_notForced);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 988;
                this.match(FlinkSqlParser.KW_NOT);
                this.state = 989;
                this.match(FlinkSqlParser.KW_ENFORCED);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    alertView() {
        let _localctx = new AlertViewContext(this._ctx, this.state);
        this.enterRule(_localctx, 128, FlinkSqlParser.RULE_alertView);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 991;
                this.match(FlinkSqlParser.KW_ALTER);
                this.state = 992;
                this.match(FlinkSqlParser.KW_VIEW);
                this.state = 993;
                this.viewPath();
                this.state = 997;
                this._errHandler.sync(this);
                switch (this._input.LA(1)) {
                    case FlinkSqlParser.KW_RENAME:
                        {
                            this.state = 994;
                            this.renameDefinition();
                        }
                        break;
                    case FlinkSqlParser.KW_AS:
                        {
                            this.state = 995;
                            this.match(FlinkSqlParser.KW_AS);
                            this.state = 996;
                            this.queryStatement(0);
                        }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    alterDatabase() {
        let _localctx = new AlterDatabaseContext(this._ctx, this.state);
        this.enterRule(_localctx, 130, FlinkSqlParser.RULE_alterDatabase);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 999;
                this.match(FlinkSqlParser.KW_ALTER);
                this.state = 1000;
                this.match(FlinkSqlParser.KW_DATABASE);
                this.state = 1001;
                this.databasePath();
                this.state = 1002;
                this.setKeyValueDefinition();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    alterFunction() {
        let _localctx = new AlterFunctionContext(this._ctx, this.state);
        this.enterRule(_localctx, 132, FlinkSqlParser.RULE_alterFunction);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1004;
                this.match(FlinkSqlParser.KW_ALTER);
                this.state = 1008;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 90, this._ctx)) {
                    case 1:
                        {
                            this.state = 1005;
                            this.match(FlinkSqlParser.KW_TEMPORARY);
                        }
                        break;
                    case 2:
                        {
                            this.state = 1006;
                            this.match(FlinkSqlParser.KW_TEMPORARY);
                            this.state = 1007;
                            this.match(FlinkSqlParser.KW_SYSTEM);
                        }
                        break;
                }
                this.state = 1010;
                this.match(FlinkSqlParser.KW_FUNCTION);
                this.state = 1012;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 91, this._ctx)) {
                    case 1:
                        {
                            this.state = 1011;
                            this.ifExists();
                        }
                        break;
                }
                this.state = 1014;
                this.functionName();
                this.state = 1015;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 1016;
                this.identifier();
                this.state = 1019;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_LANGUAGE) {
                    {
                        this.state = 1017;
                        this.match(FlinkSqlParser.KW_LANGUAGE);
                        this.state = 1018;
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_SCALA || _la === FlinkSqlParser.KW_JAVA || _la === FlinkSqlParser.KW_PYTHON)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dropCatalog() {
        let _localctx = new DropCatalogContext(this._ctx, this.state);
        this.enterRule(_localctx, 134, FlinkSqlParser.RULE_dropCatalog);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1021;
                this.match(FlinkSqlParser.KW_DROP);
                this.state = 1022;
                this.match(FlinkSqlParser.KW_CATALOG);
                this.state = 1024;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 1023;
                        this.ifExists();
                    }
                }
                this.state = 1026;
                this.catalogPath();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dropTable() {
        let _localctx = new DropTableContext(this._ctx, this.state);
        this.enterRule(_localctx, 136, FlinkSqlParser.RULE_dropTable);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1028;
                this.match(FlinkSqlParser.KW_DROP);
                this.state = 1030;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_TEMPORARY) {
                    {
                        this.state = 1029;
                        this.match(FlinkSqlParser.KW_TEMPORARY);
                    }
                }
                this.state = 1032;
                this.match(FlinkSqlParser.KW_TABLE);
                this.state = 1034;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 1033;
                        this.ifExists();
                    }
                }
                this.state = 1036;
                this.tablePath();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dropDatabase() {
        let _localctx = new DropDatabaseContext(this._ctx, this.state);
        this.enterRule(_localctx, 138, FlinkSqlParser.RULE_dropDatabase);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1038;
                this.match(FlinkSqlParser.KW_DROP);
                this.state = 1039;
                this.match(FlinkSqlParser.KW_DATABASE);
                this.state = 1041;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 1040;
                        this.ifExists();
                    }
                }
                this.state = 1043;
                this.databasePath();
                this.state = 1045;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_CASCADE || _la === FlinkSqlParser.KW_RESTRICT) {
                    {
                        this.state = 1044;
                        _localctx._dropType = this._input.LT(1);
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_CASCADE || _la === FlinkSqlParser.KW_RESTRICT)) {
                            _localctx._dropType = this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dropView() {
        let _localctx = new DropViewContext(this._ctx, this.state);
        this.enterRule(_localctx, 140, FlinkSqlParser.RULE_dropView);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1047;
                this.match(FlinkSqlParser.KW_DROP);
                this.state = 1049;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_TEMPORARY) {
                    {
                        this.state = 1048;
                        this.match(FlinkSqlParser.KW_TEMPORARY);
                    }
                }
                this.state = 1051;
                this.match(FlinkSqlParser.KW_VIEW);
                this.state = 1053;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_IF) {
                    {
                        this.state = 1052;
                        this.ifExists();
                    }
                }
                this.state = 1055;
                this.viewPath();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dropFunction() {
        let _localctx = new DropFunctionContext(this._ctx, this.state);
        this.enterRule(_localctx, 142, FlinkSqlParser.RULE_dropFunction);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1057;
                this.match(FlinkSqlParser.KW_DROP);
                this.state = 1061;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 100, this._ctx)) {
                    case 1:
                        {
                            this.state = 1058;
                            this.match(FlinkSqlParser.KW_TEMPORARY);
                        }
                        break;
                    case 2:
                        {
                            this.state = 1059;
                            this.match(FlinkSqlParser.KW_TEMPORARY);
                            this.state = 1060;
                            this.match(FlinkSqlParser.KW_SYSTEM);
                        }
                        break;
                }
                this.state = 1063;
                this.match(FlinkSqlParser.KW_FUNCTION);
                this.state = 1065;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 101, this._ctx)) {
                    case 1:
                        {
                            this.state = 1064;
                            this.ifExists();
                        }
                        break;
                }
                this.state = 1067;
                this.functionName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    insertStatement() {
        let _localctx = new InsertStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 144, FlinkSqlParser.RULE_insertStatement);
        let _la;
        try {
            this.state = 1076;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 103, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        {
                            this.state = 1070;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === FlinkSqlParser.KW_EXECUTE) {
                                {
                                    this.state = 1069;
                                    this.match(FlinkSqlParser.KW_EXECUTE);
                                }
                            }
                            this.state = 1072;
                            this.insertSimpleStatement();
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1073;
                        this.insertMulStatementCompatibility();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        {
                            this.state = 1074;
                            this.match(FlinkSqlParser.KW_EXECUTE);
                            this.state = 1075;
                            this.insertMulStatement();
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    insertSimpleStatement() {
        let _localctx = new InsertSimpleStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 146, FlinkSqlParser.RULE_insertSimpleStatement);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1078;
                this.match(FlinkSqlParser.KW_INSERT);
                this.state = 1079;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_INTO || _la === FlinkSqlParser.KW_OVERWRITE)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1080;
                this.tablePath();
                this.state = 1089;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 106, this._ctx)) {
                    case 1:
                        {
                            this.state = 1082;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === FlinkSqlParser.KW_PARTITION) {
                                {
                                    this.state = 1081;
                                    this.insertPartitionDefinition();
                                }
                            }
                            this.state = 1085;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 105, this._ctx)) {
                                case 1:
                                    {
                                        this.state = 1084;
                                        this.columnNameList();
                                    }
                                    break;
                            }
                            this.state = 1087;
                            this.queryStatement(0);
                        }
                        break;
                    case 2:
                        {
                            this.state = 1088;
                            this.valuesDefinition();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    insertPartitionDefinition() {
        let _localctx = new InsertPartitionDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 148, FlinkSqlParser.RULE_insertPartitionDefinition);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1091;
                this.match(FlinkSqlParser.KW_PARTITION);
                this.state = 1092;
                this.tablePropertyList();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    valuesDefinition() {
        let _localctx = new ValuesDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 150, FlinkSqlParser.RULE_valuesDefinition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1094;
                this.match(FlinkSqlParser.KW_VALUES);
                this.state = 1095;
                this.valuesRowDefinition();
                this.state = 1100;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 1096;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1097;
                            this.valuesRowDefinition();
                        }
                    }
                    this.state = 1102;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    valuesRowDefinition() {
        let _localctx = new ValuesRowDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 152, FlinkSqlParser.RULE_valuesRowDefinition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1103;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1104;
                this.constant();
                this.state = 1109;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 1105;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1106;
                            this.constant();
                        }
                    }
                    this.state = 1111;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
                this.state = 1112;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    insertMulStatementCompatibility() {
        let _localctx = new InsertMulStatementCompatibilityContext(this._ctx, this.state);
        this.enterRule(_localctx, 154, FlinkSqlParser.RULE_insertMulStatementCompatibility);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1114;
                this.match(FlinkSqlParser.KW_BEGIN);
                this.state = 1115;
                this.match(FlinkSqlParser.KW_STATEMENT);
                this.state = 1116;
                this.match(FlinkSqlParser.KW_SET);
                this.state = 1117;
                this.match(FlinkSqlParser.SEMICOLON);
                this.state = 1121;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1118;
                            this.insertSimpleStatement();
                            this.state = 1119;
                            this.match(FlinkSqlParser.SEMICOLON);
                        }
                    }
                    this.state = 1123;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === FlinkSqlParser.KW_INSERT);
                this.state = 1125;
                this.match(FlinkSqlParser.KW_END);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    insertMulStatement() {
        let _localctx = new InsertMulStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 156, FlinkSqlParser.RULE_insertMulStatement);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1127;
                this.match(FlinkSqlParser.KW_STATEMENT);
                this.state = 1128;
                this.match(FlinkSqlParser.KW_SET);
                this.state = 1129;
                this.match(FlinkSqlParser.KW_BEGIN);
                this.state = 1133;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1130;
                            this.insertSimpleStatement();
                            this.state = 1131;
                            this.match(FlinkSqlParser.SEMICOLON);
                        }
                    }
                    this.state = 1135;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === FlinkSqlParser.KW_INSERT);
                this.state = 1137;
                this.match(FlinkSqlParser.KW_END);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    queryStatement(_p) {
        if (_p === undefined) {
            _p = 0;
        }
        let _parentctx = this._ctx;
        let _parentState = this.state;
        let _localctx = new QueryStatementContext(this._ctx, _parentState);
        let _prevctx = _localctx;
        let _startState = 158;
        this.enterRecursionRule(_localctx, 158, FlinkSqlParser.RULE_queryStatement, _p);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1162;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 115, this._ctx)) {
                    case 1:
                        {
                            this.state = 1140;
                            this.valuesCaluse();
                        }
                        break;
                    case 2:
                        {
                            this.state = 1141;
                            this.withClause();
                            this.state = 1142;
                            this.queryStatement(5);
                        }
                        break;
                    case 3:
                        {
                            this.state = 1144;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1145;
                            this.queryStatement(0);
                            this.state = 1146;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 4:
                        {
                            this.state = 1148;
                            this.selectClause();
                            this.state = 1150;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 111, this._ctx)) {
                                case 1:
                                    {
                                        this.state = 1149;
                                        this.orderByCaluse();
                                    }
                                    break;
                            }
                            this.state = 1153;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 112, this._ctx)) {
                                case 1:
                                    {
                                        this.state = 1152;
                                        this.limitClause();
                                    }
                                    break;
                            }
                        }
                        break;
                    case 5:
                        {
                            this.state = 1155;
                            this.selectStatement();
                            this.state = 1157;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 113, this._ctx)) {
                                case 1:
                                    {
                                        this.state = 1156;
                                        this.orderByCaluse();
                                    }
                                    break;
                            }
                            this.state = 1160;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 114, this._ctx)) {
                                case 1:
                                    {
                                        this.state = 1159;
                                        this.limitClause();
                                    }
                                    break;
                            }
                        }
                        break;
                }
                this._ctx._stop = this._input.tryLT(-1);
                this.state = 1178;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 119, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        if (this._parseListeners != null) {
                            this.triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new QueryStatementContext(_parentctx, _parentState);
                                _localctx._left = _prevctx;
                                this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_queryStatement);
                                this.state = 1164;
                                if (!(this.precpred(this._ctx, 3))) {
                                    throw this.createFailedPredicateException("this.precpred(this._ctx, 3)");
                                }
                                this.state = 1165;
                                _localctx._operator = this._input.LT(1);
                                _la = this._input.LA(1);
                                if (!(_la === FlinkSqlParser.KW_EXCEPT || _la === FlinkSqlParser.KW_INTERSECT || _la === FlinkSqlParser.KW_UNION)) {
                                    _localctx._operator = this._errHandler.recoverInline(this);
                                }
                                else {
                                    if (this._input.LA(1) === Token.EOF) {
                                        this.matchedEOF = true;
                                    }
                                    this._errHandler.reportMatch(this);
                                    this.consume();
                                }
                                this.state = 1167;
                                this._errHandler.sync(this);
                                _la = this._input.LA(1);
                                if (_la === FlinkSqlParser.KW_ALL) {
                                    {
                                        this.state = 1166;
                                        this.match(FlinkSqlParser.KW_ALL);
                                    }
                                }
                                this.state = 1169;
                                _localctx._right = this.queryStatement(0);
                                this.state = 1171;
                                this._errHandler.sync(this);
                                switch (this.interpreter.adaptivePredict(this._input, 117, this._ctx)) {
                                    case 1:
                                        {
                                            this.state = 1170;
                                            this.orderByCaluse();
                                        }
                                        break;
                                }
                                this.state = 1174;
                                this._errHandler.sync(this);
                                switch (this.interpreter.adaptivePredict(this._input, 118, this._ctx)) {
                                    case 1:
                                        {
                                            this.state = 1173;
                                            this.limitClause();
                                        }
                                        break;
                                }
                            }
                        }
                    }
                    this.state = 1180;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 119, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }
    // @RuleVersion(0)
    valuesCaluse() {
        let _localctx = new ValuesCaluseContext(this._ctx, this.state);
        this.enterRule(_localctx, 160, FlinkSqlParser.RULE_valuesCaluse);
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1181;
                this.match(FlinkSqlParser.KW_VALUES);
                this.state = 1182;
                this.expression();
                this.state = 1187;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 120, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        {
                            {
                                this.state = 1183;
                                this.match(FlinkSqlParser.COMMA);
                                this.state = 1184;
                                this.expression();
                            }
                        }
                    }
                    this.state = 1189;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 120, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    withClause() {
        let _localctx = new WithClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 162, FlinkSqlParser.RULE_withClause);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1190;
                this.match(FlinkSqlParser.KW_WITH);
                this.state = 1191;
                this.withItem();
                this.state = 1196;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 1192;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1193;
                            this.withItem();
                        }
                    }
                    this.state = 1198;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    withItem() {
        let _localctx = new WithItemContext(this._ctx, this.state);
        this.enterRule(_localctx, 164, FlinkSqlParser.RULE_withItem);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1199;
                this.withItemName();
                this.state = 1211;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.LR_BRACKET) {
                    {
                        this.state = 1200;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1201;
                        this.columnName();
                        this.state = 1206;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        while (_la === FlinkSqlParser.COMMA) {
                            {
                                {
                                    this.state = 1202;
                                    this.match(FlinkSqlParser.COMMA);
                                    this.state = 1203;
                                    this.columnName();
                                }
                            }
                            this.state = 1208;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        }
                        this.state = 1209;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                }
                this.state = 1213;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 1214;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1215;
                this.queryStatement(0);
                this.state = 1216;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    withItemName() {
        let _localctx = new WithItemNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 166, FlinkSqlParser.RULE_withItemName);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1218;
                this.identifier();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    selectStatement() {
        let _localctx = new SelectStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 168, FlinkSqlParser.RULE_selectStatement);
        try {
            this.state = 1240;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 129, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1220;
                        this.selectClause();
                        this.state = 1222;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 124, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1221;
                                    this.fromClause();
                                }
                                break;
                        }
                        this.state = 1225;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 125, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1224;
                                    this.whereClause();
                                }
                                break;
                        }
                        this.state = 1228;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 126, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1227;
                                    this.groupByClause();
                                }
                                break;
                        }
                        this.state = 1231;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 127, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1230;
                                    this.havingClause();
                                }
                                break;
                        }
                        this.state = 1234;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 128, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1233;
                                    this.windowClause();
                                }
                                break;
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1236;
                        this.selectClause();
                        this.state = 1237;
                        this.fromClause();
                        this.state = 1238;
                        this.matchRecognizeClause();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    selectClause() {
        let _localctx = new SelectClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 170, FlinkSqlParser.RULE_selectClause);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1242;
                this.match(FlinkSqlParser.KW_SELECT);
                this.state = 1244;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_ALL || _la === FlinkSqlParser.KW_DISTINCT) {
                    {
                        this.state = 1243;
                        this.setQuantifier();
                    }
                }
                this.state = 1255;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 132, this._ctx)) {
                    case 1:
                        {
                            this.state = 1246;
                            this.match(FlinkSqlParser.ASTERISK_SIGN);
                        }
                        break;
                    case 2:
                        {
                            this.state = 1247;
                            this.projectItemDefinition();
                            this.state = 1252;
                            this._errHandler.sync(this);
                            _alt = this.interpreter.adaptivePredict(this._input, 131, this._ctx);
                            while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                                if (_alt === 1) {
                                    {
                                        {
                                            this.state = 1248;
                                            this.match(FlinkSqlParser.COMMA);
                                            this.state = 1249;
                                            this.projectItemDefinition();
                                        }
                                    }
                                }
                                this.state = 1254;
                                this._errHandler.sync(this);
                                _alt = this.interpreter.adaptivePredict(this._input, 131, this._ctx);
                            }
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    projectItemDefinition() {
        let _localctx = new ProjectItemDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 172, FlinkSqlParser.RULE_projectItemDefinition);
        let _la;
        try {
            this.state = 1265;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 135, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1257;
                        this.overWindowItem();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1258;
                        this.columnName();
                        this.state = 1263;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 134, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1260;
                                    this._errHandler.sync(this);
                                    _la = this._input.LA(1);
                                    if (_la === FlinkSqlParser.KW_AS) {
                                        {
                                            this.state = 1259;
                                            this.match(FlinkSqlParser.KW_AS);
                                        }
                                    }
                                    this.state = 1262;
                                    this.expression();
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    overWindowItem() {
        let _localctx = new OverWindowItemContext(this._ctx, this.state);
        this.enterRule(_localctx, 174, FlinkSqlParser.RULE_overWindowItem);
        try {
            this.state = 1279;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 136, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1267;
                        this.primaryExpression(0);
                        this.state = 1268;
                        this.match(FlinkSqlParser.KW_OVER);
                        this.state = 1269;
                        this.windowSpec();
                        this.state = 1270;
                        this.match(FlinkSqlParser.KW_AS);
                        this.state = 1271;
                        this.identifier();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1273;
                        this.primaryExpression(0);
                        this.state = 1274;
                        this.match(FlinkSqlParser.KW_OVER);
                        this.state = 1275;
                        this.errorCapturingIdentifier();
                        this.state = 1276;
                        this.match(FlinkSqlParser.KW_AS);
                        this.state = 1277;
                        this.identifier();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    fromClause() {
        let _localctx = new FromClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 176, FlinkSqlParser.RULE_fromClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1281;
                this.match(FlinkSqlParser.KW_FROM);
                this.state = 1282;
                this.tableExpression(0);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tableExpression(_p) {
        if (_p === undefined) {
            _p = 0;
        }
        let _parentctx = this._ctx;
        let _parentState = this.state;
        let _localctx = new TableExpressionContext(this._ctx, _parentState);
        let _prevctx = _localctx;
        let _startState = 178;
        this.enterRecursionRule(_localctx, 178, FlinkSqlParser.RULE_tableExpression, _p);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1295;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 138, this._ctx)) {
                    case 1:
                        {
                            this.state = 1285;
                            this.tableReference();
                            this.state = 1290;
                            this._errHandler.sync(this);
                            _alt = this.interpreter.adaptivePredict(this._input, 137, this._ctx);
                            while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                                if (_alt === 1) {
                                    {
                                        {
                                            this.state = 1286;
                                            this.match(FlinkSqlParser.COMMA);
                                            this.state = 1287;
                                            this.tableReference();
                                        }
                                    }
                                }
                                this.state = 1292;
                                this._errHandler.sync(this);
                                _alt = this.interpreter.adaptivePredict(this._input, 137, this._ctx);
                            }
                        }
                        break;
                    case 2:
                        {
                            this.state = 1293;
                            this.inlineDataValueClause();
                        }
                        break;
                    case 3:
                        {
                            this.state = 1294;
                            this.windoTVFClause();
                        }
                        break;
                }
                this._ctx._stop = this._input.tryLT(-1);
                this.state = 1318;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 144, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        if (this._parseListeners != null) {
                            this.triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            this.state = 1316;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 143, this._ctx)) {
                                case 1:
                                    {
                                        _localctx = new TableExpressionContext(_parentctx, _parentState);
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_tableExpression);
                                        this.state = 1297;
                                        if (!(this.precpred(this._ctx, 3))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 3)");
                                        }
                                        this.state = 1298;
                                        this.match(FlinkSqlParser.KW_CROSS);
                                        this.state = 1299;
                                        this.match(FlinkSqlParser.KW_JOIN);
                                        this.state = 1300;
                                        this.tableExpression(4);
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx = new TableExpressionContext(_parentctx, _parentState);
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_tableExpression);
                                        this.state = 1301;
                                        if (!(this.precpred(this._ctx, 4))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 4)");
                                        }
                                        this.state = 1303;
                                        this._errHandler.sync(this);
                                        _la = this._input.LA(1);
                                        if (_la === FlinkSqlParser.KW_NATURAL) {
                                            {
                                                this.state = 1302;
                                                this.match(FlinkSqlParser.KW_NATURAL);
                                            }
                                        }
                                        this.state = 1306;
                                        this._errHandler.sync(this);
                                        _la = this._input.LA(1);
                                        if (_la === FlinkSqlParser.KW_FULL || _la === FlinkSqlParser.KW_INNER || _la === FlinkSqlParser.KW_LEFT || _la === FlinkSqlParser.KW_RIGHT) {
                                            {
                                                this.state = 1305;
                                                _la = this._input.LA(1);
                                                if (!(_la === FlinkSqlParser.KW_FULL || _la === FlinkSqlParser.KW_INNER || _la === FlinkSqlParser.KW_LEFT || _la === FlinkSqlParser.KW_RIGHT)) {
                                                    this._errHandler.recoverInline(this);
                                                }
                                                else {
                                                    if (this._input.LA(1) === Token.EOF) {
                                                        this.matchedEOF = true;
                                                    }
                                                    this._errHandler.reportMatch(this);
                                                    this.consume();
                                                }
                                            }
                                        }
                                        this.state = 1309;
                                        this._errHandler.sync(this);
                                        _la = this._input.LA(1);
                                        if (_la === FlinkSqlParser.KW_OUTER) {
                                            {
                                                this.state = 1308;
                                                this.match(FlinkSqlParser.KW_OUTER);
                                            }
                                        }
                                        this.state = 1311;
                                        this.match(FlinkSqlParser.KW_JOIN);
                                        this.state = 1312;
                                        this.tableExpression(0);
                                        this.state = 1314;
                                        this._errHandler.sync(this);
                                        switch (this.interpreter.adaptivePredict(this._input, 142, this._ctx)) {
                                            case 1:
                                                {
                                                    this.state = 1313;
                                                    this.joinCondition();
                                                }
                                                break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    this.state = 1320;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 144, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tableReference() {
        let _localctx = new TableReferenceContext(this._ctx, this.state);
        this.enterRule(_localctx, 180, FlinkSqlParser.RULE_tableReference);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1321;
                this.tablePrimary();
                this.state = 1323;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 145, this._ctx)) {
                    case 1:
                        {
                            this.state = 1322;
                            this.tableAlias();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tablePrimary() {
        let _localctx = new TablePrimaryContext(this._ctx, this.state);
        this.enterRule(_localctx, 182, FlinkSqlParser.RULE_tablePrimary);
        let _la;
        try {
            this.state = 1376;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 155, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1326;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_TABLE) {
                            {
                                this.state = 1325;
                                this.match(FlinkSqlParser.KW_TABLE);
                            }
                        }
                        this.state = 1328;
                        this.tablePath();
                        this.state = 1330;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 147, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1329;
                                    this.systemTimePeriod();
                                }
                                break;
                        }
                        this.state = 1336;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 149, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1333;
                                    this._errHandler.sync(this);
                                    _la = this._input.LA(1);
                                    if (_la === FlinkSqlParser.KW_AS) {
                                        {
                                            this.state = 1332;
                                            this.match(FlinkSqlParser.KW_AS);
                                        }
                                    }
                                    this.state = 1335;
                                    this.correlationName();
                                }
                                break;
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1338;
                        this.viewPath();
                        this.state = 1340;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 150, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1339;
                                    this.systemTimePeriod();
                                }
                                break;
                        }
                        this.state = 1346;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 152, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1343;
                                    this._errHandler.sync(this);
                                    _la = this._input.LA(1);
                                    if (_la === FlinkSqlParser.KW_AS) {
                                        {
                                            this.state = 1342;
                                            this.match(FlinkSqlParser.KW_AS);
                                        }
                                    }
                                    this.state = 1345;
                                    this.correlationName();
                                }
                                break;
                        }
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1348;
                        this.match(FlinkSqlParser.KW_LATERAL);
                        this.state = 1349;
                        this.match(FlinkSqlParser.KW_TABLE);
                        this.state = 1350;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1351;
                        this.functionName();
                        this.state = 1352;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1353;
                        this.functionParam();
                        this.state = 1358;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        while (_la === FlinkSqlParser.COMMA) {
                            {
                                {
                                    this.state = 1354;
                                    this.match(FlinkSqlParser.COMMA);
                                    this.state = 1355;
                                    this.functionParam();
                                }
                            }
                            this.state = 1360;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        }
                        this.state = 1361;
                        this.match(FlinkSqlParser.RR_BRACKET);
                        this.state = 1362;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1365;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_LATERAL) {
                            {
                                this.state = 1364;
                                this.match(FlinkSqlParser.KW_LATERAL);
                            }
                        }
                        this.state = 1367;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1368;
                        this.queryStatement(0);
                        this.state = 1369;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 1371;
                        this.match(FlinkSqlParser.KW_UNNEST);
                        this.state = 1372;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1373;
                        this.expression();
                        this.state = 1374;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    systemTimePeriod() {
        let _localctx = new SystemTimePeriodContext(this._ctx, this.state);
        this.enterRule(_localctx, 184, FlinkSqlParser.RULE_systemTimePeriod);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1378;
                this.match(FlinkSqlParser.KW_FOR);
                this.state = 1379;
                this.match(FlinkSqlParser.KW_SYSTEM_TIME);
                this.state = 1380;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 1381;
                this.match(FlinkSqlParser.KW_OF);
                this.state = 1382;
                this.dateTimeExpression();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dateTimeExpression() {
        let _localctx = new DateTimeExpressionContext(this._ctx, this.state);
        this.enterRule(_localctx, 186, FlinkSqlParser.RULE_dateTimeExpression);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1384;
                this.expression();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    inlineDataValueClause() {
        let _localctx = new InlineDataValueClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 188, FlinkSqlParser.RULE_inlineDataValueClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1386;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1387;
                this.valuesDefinition();
                this.state = 1388;
                this.match(FlinkSqlParser.RR_BRACKET);
                this.state = 1389;
                this.tableAlias();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    windoTVFClause() {
        let _localctx = new WindoTVFClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 190, FlinkSqlParser.RULE_windoTVFClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1391;
                this.match(FlinkSqlParser.KW_TABLE);
                this.state = 1392;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1393;
                this.windowTVFExression();
                this.state = 1394;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    windowTVFExression() {
        let _localctx = new WindowTVFExressionContext(this._ctx, this.state);
        this.enterRule(_localctx, 192, FlinkSqlParser.RULE_windowTVFExression);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1396;
                this.windoTVFName();
                this.state = 1397;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1398;
                this.windowTVFParam();
                this.state = 1403;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 1399;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1400;
                            this.windowTVFParam();
                        }
                    }
                    this.state = 1405;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
                this.state = 1406;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    windoTVFName() {
        let _localctx = new WindoTVFNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 194, FlinkSqlParser.RULE_windoTVFName);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1408;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_CUMULATE || _la === FlinkSqlParser.KW_HOP || _la === FlinkSqlParser.KW_TUMBLE)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    windowTVFParam() {
        let _localctx = new WindowTVFParamContext(this._ctx, this.state);
        this.enterRule(_localctx, 196, FlinkSqlParser.RULE_windowTVFParam);
        try {
            this.state = 1425;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 157, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1410;
                        this.match(FlinkSqlParser.KW_TABLE);
                        this.state = 1411;
                        this.timeAttrColumn();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1412;
                        this.columnDescriptor();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1413;
                        this.timeIntervalExpression();
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1414;
                        this.match(FlinkSqlParser.KW_DATA);
                        this.state = 1415;
                        this.match(FlinkSqlParser.DOUBLE_RIGHT_ARROW);
                        this.state = 1416;
                        this.match(FlinkSqlParser.KW_TABLE);
                        this.state = 1417;
                        this.timeAttrColumn();
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 1418;
                        this.match(FlinkSqlParser.KW_TIMECOL);
                        this.state = 1419;
                        this.match(FlinkSqlParser.DOUBLE_RIGHT_ARROW);
                        this.state = 1420;
                        this.columnDescriptor();
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 1421;
                        this.timeIntervalParamName();
                        this.state = 1422;
                        this.match(FlinkSqlParser.DOUBLE_RIGHT_ARROW);
                        this.state = 1423;
                        this.timeIntervalExpression();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    timeIntervalParamName() {
        let _localctx = new TimeIntervalParamNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 198, FlinkSqlParser.RULE_timeIntervalParamName);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1427;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_OFFSET || _la === FlinkSqlParser.KW_DATA || ((((_la - 494)) & ~0x1F) === 0 && ((1 << (_la - 494)) & ((1 << (FlinkSqlParser.KW_SIZE - 494)) | (1 << (FlinkSqlParser.KW_SLIDE - 494)) | (1 << (FlinkSqlParser.KW_STEP - 494)) | (1 << (FlinkSqlParser.KW_TIMECOL - 494)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    columnDescriptor() {
        let _localctx = new ColumnDescriptorContext(this._ctx, this.state);
        this.enterRule(_localctx, 200, FlinkSqlParser.RULE_columnDescriptor);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1429;
                this.match(FlinkSqlParser.KW_DESCRIPTOR);
                this.state = 1430;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1431;
                this.columnName();
                this.state = 1432;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    joinCondition() {
        let _localctx = new JoinConditionContext(this._ctx, this.state);
        this.enterRule(_localctx, 202, FlinkSqlParser.RULE_joinCondition);
        try {
            this.state = 1438;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_ON:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1434;
                        this.match(FlinkSqlParser.KW_ON);
                        this.state = 1435;
                        this.booleanExpression(0);
                    }
                    break;
                case FlinkSqlParser.KW_USING:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1436;
                        this.match(FlinkSqlParser.KW_USING);
                        this.state = 1437;
                        this.columnNameList();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    whereClause() {
        let _localctx = new WhereClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 204, FlinkSqlParser.RULE_whereClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1440;
                this.match(FlinkSqlParser.KW_WHERE);
                this.state = 1441;
                this.booleanExpression(0);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    groupByClause() {
        let _localctx = new GroupByClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 206, FlinkSqlParser.RULE_groupByClause);
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1443;
                this.match(FlinkSqlParser.KW_GROUP);
                this.state = 1444;
                this.match(FlinkSqlParser.KW_BY);
                this.state = 1445;
                this.groupItemDefinition();
                this.state = 1450;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 159, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        {
                            {
                                this.state = 1446;
                                this.match(FlinkSqlParser.COMMA);
                                this.state = 1447;
                                this.groupItemDefinition();
                            }
                        }
                    }
                    this.state = 1452;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 159, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    groupItemDefinition() {
        let _localctx = new GroupItemDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 208, FlinkSqlParser.RULE_groupItemDefinition);
        let _la;
        try {
            this.state = 1492;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 163, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1453;
                        this.columnName();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1454;
                        this.groupWindowFunction();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1455;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1456;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1457;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1458;
                        this.expression();
                        this.state = 1463;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        while (_la === FlinkSqlParser.COMMA) {
                            {
                                {
                                    this.state = 1459;
                                    this.match(FlinkSqlParser.COMMA);
                                    this.state = 1460;
                                    this.expression();
                                }
                            }
                            this.state = 1465;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        }
                        this.state = 1466;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 1468;
                        this.groupingSetsNotaionName();
                        this.state = 1469;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1470;
                        this.expression();
                        this.state = 1475;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        while (_la === FlinkSqlParser.COMMA) {
                            {
                                {
                                    this.state = 1471;
                                    this.match(FlinkSqlParser.COMMA);
                                    this.state = 1472;
                                    this.expression();
                                }
                            }
                            this.state = 1477;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        }
                        this.state = 1478;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 1480;
                        this.groupingSets();
                        this.state = 1481;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1482;
                        this.groupItemDefinition();
                        this.state = 1487;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        while (_la === FlinkSqlParser.COMMA) {
                            {
                                {
                                    this.state = 1483;
                                    this.match(FlinkSqlParser.COMMA);
                                    this.state = 1484;
                                    this.groupItemDefinition();
                                }
                            }
                            this.state = 1489;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        }
                        this.state = 1490;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    groupingSets() {
        let _localctx = new GroupingSetsContext(this._ctx, this.state);
        this.enterRule(_localctx, 210, FlinkSqlParser.RULE_groupingSets);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1494;
                this.match(FlinkSqlParser.KW_GROUPING);
                this.state = 1495;
                this.match(FlinkSqlParser.KW_SETS);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    groupingSetsNotaionName() {
        let _localctx = new GroupingSetsNotaionNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 212, FlinkSqlParser.RULE_groupingSetsNotaionName);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1497;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_CUBE || _la === FlinkSqlParser.KW_ROLLUP)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    groupWindowFunction() {
        let _localctx = new GroupWindowFunctionContext(this._ctx, this.state);
        this.enterRule(_localctx, 214, FlinkSqlParser.RULE_groupWindowFunction);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1499;
                this.groupWindowFunctionName();
                this.state = 1500;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1501;
                this.timeAttrColumn();
                this.state = 1502;
                this.match(FlinkSqlParser.COMMA);
                this.state = 1503;
                this.timeIntervalExpression();
                this.state = 1504;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    groupWindowFunctionName() {
        let _localctx = new GroupWindowFunctionNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 216, FlinkSqlParser.RULE_groupWindowFunctionName);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1506;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_HOP || _la === FlinkSqlParser.KW_SESSION || _la === FlinkSqlParser.KW_TUMBLE)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    timeAttrColumn() {
        let _localctx = new TimeAttrColumnContext(this._ctx, this.state);
        this.enterRule(_localctx, 218, FlinkSqlParser.RULE_timeAttrColumn);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1508;
                this.uid();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    havingClause() {
        let _localctx = new HavingClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 220, FlinkSqlParser.RULE_havingClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1510;
                this.match(FlinkSqlParser.KW_HAVING);
                this.state = 1511;
                this.booleanExpression(0);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    windowClause() {
        let _localctx = new WindowClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 222, FlinkSqlParser.RULE_windowClause);
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1513;
                this.match(FlinkSqlParser.KW_WINDOW);
                this.state = 1514;
                this.namedWindow();
                this.state = 1519;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 164, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        {
                            {
                                this.state = 1515;
                                this.match(FlinkSqlParser.COMMA);
                                this.state = 1516;
                                this.namedWindow();
                            }
                        }
                    }
                    this.state = 1521;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 164, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    namedWindow() {
        let _localctx = new NamedWindowContext(this._ctx, this.state);
        this.enterRule(_localctx, 224, FlinkSqlParser.RULE_namedWindow);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1522;
                _localctx._name = this.errorCapturingIdentifier();
                this.state = 1523;
                this.match(FlinkSqlParser.KW_AS);
                this.state = 1524;
                this.windowSpec();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    windowSpec() {
        let _localctx = new WindowSpecContext(this._ctx, this.state);
        this.enterRule(_localctx, 226, FlinkSqlParser.RULE_windowSpec);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1527;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 437)) & ~0x1F) === 0 && ((1 << (_la - 437)) & ((1 << (FlinkSqlParser.KW_ADD - 437)) | (1 << (FlinkSqlParser.KW_AFTER - 437)) | (1 << (FlinkSqlParser.KW_ASC - 437)) | (1 << (FlinkSqlParser.KW_CASCADE - 437)) | (1 << (FlinkSqlParser.KW_CATALOG - 437)) | (1 << (FlinkSqlParser.KW_CENTURY - 437)) | (1 << (FlinkSqlParser.KW_CONFIG - 437)) | (1 << (FlinkSqlParser.KW_CONSTRAINTS - 437)) | (1 << (FlinkSqlParser.KW_CUMULATE - 437)) | (1 << (FlinkSqlParser.KW_DATA - 437)) | (1 << (FlinkSqlParser.KW_DATABASE - 437)) | (1 << (FlinkSqlParser.KW_DAYS - 437)) | (1 << (FlinkSqlParser.KW_DECADE - 437)) | (1 << (FlinkSqlParser.KW_DESC - 437)) | (1 << (FlinkSqlParser.KW_DESCRIPTOR - 437)) | (1 << (FlinkSqlParser.KW_DIV - 437)) | (1 << (FlinkSqlParser.KW_ENGINE - 437)) | (1 << (FlinkSqlParser.KW_EPOCH - 437)) | (1 << (FlinkSqlParser.KW_EXCLUDING - 437)) | (1 << (FlinkSqlParser.KW_FILE - 437)) | (1 << (FlinkSqlParser.KW_FIRST - 437)) | (1 << (FlinkSqlParser.KW_GENERATED - 437)) | (1 << (FlinkSqlParser.KW_HOP - 437)) | (1 << (FlinkSqlParser.KW_HOURS - 437)) | (1 << (FlinkSqlParser.KW_IGNORE - 437)) | (1 << (FlinkSqlParser.KW_INCLUDING - 437)) | (1 << (FlinkSqlParser.KW_JAR - 437)) | (1 << (FlinkSqlParser.KW_JARS - 437)) | (1 << (FlinkSqlParser.KW_JAVA - 437)) | (1 << (FlinkSqlParser.KW_KEY - 437)) | (1 << (FlinkSqlParser.KW_LAST - 437)) | (1 << (FlinkSqlParser.KW_LOAD - 437)))) !== 0) || ((((_la - 469)) & ~0x1F) === 0 && ((1 << (_la - 469)) & ((1 << (FlinkSqlParser.KW_MAP - 469)) | (1 << (FlinkSqlParser.KW_MICROSECOND - 469)) | (1 << (FlinkSqlParser.KW_MILLENNIUM - 469)) | (1 << (FlinkSqlParser.KW_MILLISECOND - 469)) | (1 << (FlinkSqlParser.KW_MINUTES - 469)) | (1 << (FlinkSqlParser.KW_MONTHS - 469)) | (1 << (FlinkSqlParser.KW_NANOSECOND - 469)) | (1 << (FlinkSqlParser.KW_NULLS - 469)) | (1 << (FlinkSqlParser.KW_OPTIONS - 469)) | (1 << (FlinkSqlParser.KW_PAST - 469)) | (1 << (FlinkSqlParser.KW_PLAN - 469)) | (1 << (FlinkSqlParser.KW_PRECEDING - 469)) | (1 << (FlinkSqlParser.KW_PYTHON - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_ARCHIVES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_DEPENDENCIES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_FILES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_JAR - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_PARAMETER - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_REQUIREMENTS - 469)) | (1 << (FlinkSqlParser.KW_QUARTER - 469)) | (1 << (FlinkSqlParser.KW_REMOVE - 469)) | (1 << (FlinkSqlParser.KW_RESTRICT - 469)) | (1 << (FlinkSqlParser.KW_SECONDS - 469)) | (1 << (FlinkSqlParser.KW_SESSION - 469)) | (1 << (FlinkSqlParser.KW_SETS - 469)) | (1 << (FlinkSqlParser.KW_SIZE - 469)) | (1 << (FlinkSqlParser.KW_SLIDE - 469)) | (1 << (FlinkSqlParser.KW_STEP - 469)) | (1 << (FlinkSqlParser.KW_TEMPORARY - 469)) | (1 << (FlinkSqlParser.KW_TIMECOL - 469)) | (1 << (FlinkSqlParser.KW_TUMBLE - 469)) | (1 << (FlinkSqlParser.KW_UNLOAD - 469)))) !== 0) || ((((_la - 501)) & ~0x1F) === 0 && ((1 << (_la - 501)) & ((1 << (FlinkSqlParser.KW_VIEW - 501)) | (1 << (FlinkSqlParser.KW_WEEK - 501)) | (1 << (FlinkSqlParser.KW_YEARS - 501)) | (1 << (FlinkSqlParser.KW_ZONE - 501)))) !== 0) || ((((_la - 537)) & ~0x1F) === 0 && ((1 << (_la - 537)) & ((1 << (FlinkSqlParser.STRING_LITERAL - 537)) | (1 << (FlinkSqlParser.DIG_LITERAL - 537)) | (1 << (FlinkSqlParser.ID_LITERAL - 537)))) !== 0)) {
                    {
                        this.state = 1526;
                        _localctx._name = this.errorCapturingIdentifier();
                    }
                }
                this.state = 1529;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1531;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_PARTITION) {
                    {
                        this.state = 1530;
                        this.partitionByClause();
                    }
                }
                this.state = 1534;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_ORDER) {
                    {
                        this.state = 1533;
                        this.orderByCaluse();
                    }
                }
                this.state = 1537;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_RANGE || _la === FlinkSqlParser.KW_ROWS) {
                    {
                        this.state = 1536;
                        this.windowFrame();
                    }
                }
                this.state = 1539;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    matchRecognizeClause() {
        let _localctx = new MatchRecognizeClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 228, FlinkSqlParser.RULE_matchRecognizeClause);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1541;
                this.match(FlinkSqlParser.KW_MATCH_RECOGNIZE);
                this.state = 1542;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1544;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_PARTITION) {
                    {
                        this.state = 1543;
                        this.partitionByClause();
                    }
                }
                this.state = 1547;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_ORDER) {
                    {
                        this.state = 1546;
                        this.orderByCaluse();
                    }
                }
                this.state = 1550;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_MEASURES) {
                    {
                        this.state = 1549;
                        this.measuresClause();
                    }
                }
                this.state = 1553;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_ALL || _la === FlinkSqlParser.KW_ONE) {
                    {
                        this.state = 1552;
                        this.outputMode();
                    }
                }
                this.state = 1556;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_AFTER) {
                    {
                        this.state = 1555;
                        this.afterMatchStrategy();
                    }
                }
                this.state = 1559;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_PATTERN) {
                    {
                        this.state = 1558;
                        this.patternDefination();
                    }
                }
                this.state = 1561;
                this.patternVariablesDefination();
                this.state = 1562;
                this.match(FlinkSqlParser.RR_BRACKET);
                this.state = 1567;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 176, this._ctx)) {
                    case 1:
                        {
                            this.state = 1564;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === FlinkSqlParser.KW_AS) {
                                {
                                    this.state = 1563;
                                    this.match(FlinkSqlParser.KW_AS);
                                }
                            }
                            this.state = 1566;
                            this.identifier();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    orderByCaluse() {
        let _localctx = new OrderByCaluseContext(this._ctx, this.state);
        this.enterRule(_localctx, 230, FlinkSqlParser.RULE_orderByCaluse);
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1569;
                this.match(FlinkSqlParser.KW_ORDER);
                this.state = 1570;
                this.match(FlinkSqlParser.KW_BY);
                this.state = 1571;
                this.orderItemDefition();
                this.state = 1576;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 177, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        {
                            {
                                this.state = 1572;
                                this.match(FlinkSqlParser.COMMA);
                                this.state = 1573;
                                this.orderItemDefition();
                            }
                        }
                    }
                    this.state = 1578;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 177, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    orderItemDefition() {
        let _localctx = new OrderItemDefitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 232, FlinkSqlParser.RULE_orderItemDefition);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1579;
                this.columnName();
                this.state = 1581;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 178, this._ctx)) {
                    case 1:
                        {
                            this.state = 1580;
                            _localctx._ordering = this._input.LT(1);
                            _la = this._input.LA(1);
                            if (!(_la === FlinkSqlParser.KW_ASC || _la === FlinkSqlParser.KW_DESC)) {
                                _localctx._ordering = this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                        }
                        break;
                }
                this.state = 1585;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 179, this._ctx)) {
                    case 1:
                        {
                            this.state = 1583;
                            this.match(FlinkSqlParser.KW_NULLS);
                            this.state = 1584;
                            _localctx._nullOrder = this._input.LT(1);
                            _la = this._input.LA(1);
                            if (!(_la === FlinkSqlParser.KW_FIRST || _la === FlinkSqlParser.KW_LAST)) {
                                _localctx._nullOrder = this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    limitClause() {
        let _localctx = new LimitClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 234, FlinkSqlParser.RULE_limitClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1587;
                this.match(FlinkSqlParser.KW_LIMIT);
                this.state = 1590;
                this._errHandler.sync(this);
                switch (this._input.LA(1)) {
                    case FlinkSqlParser.KW_ALL:
                        {
                            this.state = 1588;
                            this.match(FlinkSqlParser.KW_ALL);
                        }
                        break;
                    case FlinkSqlParser.KW_ABS:
                    case FlinkSqlParser.KW_ARRAY:
                    case FlinkSqlParser.KW_AVG:
                    case FlinkSqlParser.KW_CARDINALITY:
                    case FlinkSqlParser.KW_CASE:
                    case FlinkSqlParser.KW_CAST:
                    case FlinkSqlParser.KW_CEIL:
                    case FlinkSqlParser.KW_CEILING:
                    case FlinkSqlParser.KW_COALESCE:
                    case FlinkSqlParser.KW_COLLECT:
                    case FlinkSqlParser.KW_COUNT:
                    case FlinkSqlParser.KW_CUME_DIST:
                    case FlinkSqlParser.KW_CURRENT_DATE:
                    case FlinkSqlParser.KW_CURRENT_TIME:
                    case FlinkSqlParser.KW_CURRENT_TIMESTAMP:
                    case FlinkSqlParser.KW_DATE:
                    case FlinkSqlParser.KW_DAY:
                    case FlinkSqlParser.KW_DAYOFWEEK:
                    case FlinkSqlParser.KW_DAYOFYEAR:
                    case FlinkSqlParser.KW_DENSE_RANK:
                    case FlinkSqlParser.KW_ELEMENT:
                    case FlinkSqlParser.KW_EXISTS:
                    case FlinkSqlParser.KW_EXP:
                    case FlinkSqlParser.KW_EXTRACT:
                    case FlinkSqlParser.KW_FALSE:
                    case FlinkSqlParser.KW_FIRST_VALUE:
                    case FlinkSqlParser.KW_FLOOR:
                    case FlinkSqlParser.KW_GROUPING:
                    case FlinkSqlParser.KW_HOUR:
                    case FlinkSqlParser.KW_IF:
                    case FlinkSqlParser.KW_INTERVAL:
                    case FlinkSqlParser.KW_LAG:
                    case FlinkSqlParser.KW_LAST_VALUE:
                    case FlinkSqlParser.KW_LEAD:
                    case FlinkSqlParser.KW_LEFT:
                    case FlinkSqlParser.KW_LN:
                    case FlinkSqlParser.KW_LOCALTIME:
                    case FlinkSqlParser.KW_LOCALTIMESTAMP:
                    case FlinkSqlParser.KW_LOWER:
                    case FlinkSqlParser.KW_MAX:
                    case FlinkSqlParser.KW_MIN:
                    case FlinkSqlParser.KW_MINUTE:
                    case FlinkSqlParser.KW_MOD:
                    case FlinkSqlParser.KW_MONTH:
                    case FlinkSqlParser.KW_NOT:
                    case FlinkSqlParser.KW_NTILE:
                    case FlinkSqlParser.KW_NULL:
                    case FlinkSqlParser.KW_NULLIF:
                    case FlinkSqlParser.KW_OVERLAY:
                    case FlinkSqlParser.KW_PERCENT_RANK:
                    case FlinkSqlParser.KW_POSITION:
                    case FlinkSqlParser.KW_POWER:
                    case FlinkSqlParser.KW_RANK:
                    case FlinkSqlParser.KW_RIGHT:
                    case FlinkSqlParser.KW_ROW:
                    case FlinkSqlParser.KW_ROWS:
                    case FlinkSqlParser.KW_ROW_NUMBER:
                    case FlinkSqlParser.KW_SECOND:
                    case FlinkSqlParser.KW_STDDEV_POP:
                    case FlinkSqlParser.KW_STDDEV_SAMP:
                    case FlinkSqlParser.KW_SUBSTRING:
                    case FlinkSqlParser.KW_SUM:
                    case FlinkSqlParser.KW_TIME:
                    case FlinkSqlParser.KW_TIMESTAMP:
                    case FlinkSqlParser.KW_TIMESTAMP_DIFF:
                    case FlinkSqlParser.KW_TRIM:
                    case FlinkSqlParser.KW_TRUE:
                    case FlinkSqlParser.KW_TRUNCATE:
                    case FlinkSqlParser.KW_TRY_CAST:
                    case FlinkSqlParser.KW_UPPER:
                    case FlinkSqlParser.KW_VAR_POP:
                    case FlinkSqlParser.KW_VAR_SAMP:
                    case FlinkSqlParser.KW_YEAR:
                    case FlinkSqlParser.KW_ADD:
                    case FlinkSqlParser.KW_AFTER:
                    case FlinkSqlParser.KW_ASC:
                    case FlinkSqlParser.KW_CASCADE:
                    case FlinkSqlParser.KW_CATALOG:
                    case FlinkSqlParser.KW_CENTURY:
                    case FlinkSqlParser.KW_CONFIG:
                    case FlinkSqlParser.KW_CONSTRAINTS:
                    case FlinkSqlParser.KW_CUMULATE:
                    case FlinkSqlParser.KW_DATA:
                    case FlinkSqlParser.KW_DATABASE:
                    case FlinkSqlParser.KW_DAYS:
                    case FlinkSqlParser.KW_DECADE:
                    case FlinkSqlParser.KW_DESC:
                    case FlinkSqlParser.KW_DESCRIPTOR:
                    case FlinkSqlParser.KW_DIV:
                    case FlinkSqlParser.KW_ENGINE:
                    case FlinkSqlParser.KW_EPOCH:
                    case FlinkSqlParser.KW_EXCLUDING:
                    case FlinkSqlParser.KW_FILE:
                    case FlinkSqlParser.KW_FIRST:
                    case FlinkSqlParser.KW_GENERATED:
                    case FlinkSqlParser.KW_HOP:
                    case FlinkSqlParser.KW_HOURS:
                    case FlinkSqlParser.KW_IGNORE:
                    case FlinkSqlParser.KW_INCLUDING:
                    case FlinkSqlParser.KW_JAR:
                    case FlinkSqlParser.KW_JARS:
                    case FlinkSqlParser.KW_JAVA:
                    case FlinkSqlParser.KW_KEY:
                    case FlinkSqlParser.KW_LAST:
                    case FlinkSqlParser.KW_LOAD:
                    case FlinkSqlParser.KW_MAP:
                    case FlinkSqlParser.KW_MICROSECOND:
                    case FlinkSqlParser.KW_MILLENNIUM:
                    case FlinkSqlParser.KW_MILLISECOND:
                    case FlinkSqlParser.KW_MINUTES:
                    case FlinkSqlParser.KW_MONTHS:
                    case FlinkSqlParser.KW_NANOSECOND:
                    case FlinkSqlParser.KW_NULLS:
                    case FlinkSqlParser.KW_OPTIONS:
                    case FlinkSqlParser.KW_PAST:
                    case FlinkSqlParser.KW_PLAN:
                    case FlinkSqlParser.KW_PRECEDING:
                    case FlinkSqlParser.KW_PYTHON:
                    case FlinkSqlParser.KW_PYTHON_ARCHIVES:
                    case FlinkSqlParser.KW_PYTHON_DEPENDENCIES:
                    case FlinkSqlParser.KW_PYTHON_FILES:
                    case FlinkSqlParser.KW_PYTHON_JAR:
                    case FlinkSqlParser.KW_PYTHON_PARAMETER:
                    case FlinkSqlParser.KW_PYTHON_REQUIREMENTS:
                    case FlinkSqlParser.KW_QUARTER:
                    case FlinkSqlParser.KW_REMOVE:
                    case FlinkSqlParser.KW_RESTRICT:
                    case FlinkSqlParser.KW_SECONDS:
                    case FlinkSqlParser.KW_SESSION:
                    case FlinkSqlParser.KW_SETS:
                    case FlinkSqlParser.KW_SIZE:
                    case FlinkSqlParser.KW_SLIDE:
                    case FlinkSqlParser.KW_STEP:
                    case FlinkSqlParser.KW_TEMPORARY:
                    case FlinkSqlParser.KW_TIMECOL:
                    case FlinkSqlParser.KW_TUMBLE:
                    case FlinkSqlParser.KW_UNLOAD:
                    case FlinkSqlParser.KW_VIEW:
                    case FlinkSqlParser.KW_WEEK:
                    case FlinkSqlParser.KW_YEARS:
                    case FlinkSqlParser.KW_ZONE:
                    case FlinkSqlParser.BIT_NOT_OP:
                    case FlinkSqlParser.LR_BRACKET:
                    case FlinkSqlParser.ASTERISK_SIGN:
                    case FlinkSqlParser.HYPNEN_SIGN:
                    case FlinkSqlParser.ADD_SIGN:
                    case FlinkSqlParser.STRING_LITERAL:
                    case FlinkSqlParser.DIG_LITERAL:
                    case FlinkSqlParser.REAL_LITERAL:
                    case FlinkSqlParser.BIT_STRING:
                    case FlinkSqlParser.ID_LITERAL:
                        {
                            this.state = 1589;
                            _localctx._limit = this.expression();
                        }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    partitionByClause() {
        let _localctx = new PartitionByClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 236, FlinkSqlParser.RULE_partitionByClause);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1592;
                this.match(FlinkSqlParser.KW_PARTITION);
                this.state = 1593;
                this.match(FlinkSqlParser.KW_BY);
                this.state = 1594;
                this.columnName();
                this.state = 1599;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 1595;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1596;
                            this.columnName();
                        }
                    }
                    this.state = 1601;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    quantifiers() {
        let _localctx = new QuantifiersContext(this._ctx, this.state);
        this.enterRule(_localctx, 238, FlinkSqlParser.RULE_quantifiers);
        try {
            this.state = 1618;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 182, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        {
                            this.state = 1602;
                            this.match(FlinkSqlParser.ASTERISK_SIGN);
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        {
                            this.state = 1603;
                            this.match(FlinkSqlParser.ADD_SIGN);
                        }
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        {
                            this.state = 1604;
                            this.match(FlinkSqlParser.QUESTION_MARK_SIGN);
                        }
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        {
                            this.state = 1605;
                            this.match(FlinkSqlParser.LB_BRACKET);
                            this.state = 1606;
                            this.match(FlinkSqlParser.DIG_LITERAL);
                            this.state = 1607;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1608;
                            this.match(FlinkSqlParser.DIG_LITERAL);
                            this.state = 1609;
                            this.match(FlinkSqlParser.RB_BRACKET);
                        }
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        {
                            this.state = 1610;
                            this.match(FlinkSqlParser.LB_BRACKET);
                            this.state = 1611;
                            this.match(FlinkSqlParser.DIG_LITERAL);
                            this.state = 1612;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1613;
                            this.match(FlinkSqlParser.RB_BRACKET);
                        }
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        {
                            this.state = 1614;
                            this.match(FlinkSqlParser.LB_BRACKET);
                            this.state = 1615;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1616;
                            this.match(FlinkSqlParser.DIG_LITERAL);
                            this.state = 1617;
                            this.match(FlinkSqlParser.RB_BRACKET);
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    measuresClause() {
        let _localctx = new MeasuresClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 240, FlinkSqlParser.RULE_measuresClause);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1620;
                this.match(FlinkSqlParser.KW_MEASURES);
                this.state = 1621;
                this.projectItemDefinition();
                this.state = 1626;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 1622;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1623;
                            this.projectItemDefinition();
                        }
                    }
                    this.state = 1628;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    patternDefination() {
        let _localctx = new PatternDefinationContext(this._ctx, this.state);
        this.enterRule(_localctx, 242, FlinkSqlParser.RULE_patternDefination);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1629;
                this.match(FlinkSqlParser.KW_PATTERN);
                this.state = 1630;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 1632;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1631;
                            this.patternVariable();
                        }
                    }
                    this.state = 1634;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === FlinkSqlParser.DIG_LITERAL || _la === FlinkSqlParser.ID_LITERAL);
                this.state = 1636;
                this.match(FlinkSqlParser.RR_BRACKET);
                this.state = 1638;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_WITHIN) {
                    {
                        this.state = 1637;
                        this.withinClause();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    patternVariable() {
        let _localctx = new PatternVariableContext(this._ctx, this.state);
        this.enterRule(_localctx, 244, FlinkSqlParser.RULE_patternVariable);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1640;
                this.unquotedIdentifier();
                this.state = 1642;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 518)) & ~0x1F) === 0 && ((1 << (_la - 518)) & ((1 << (FlinkSqlParser.LB_BRACKET - 518)) | (1 << (FlinkSqlParser.ASTERISK_SIGN - 518)) | (1 << (FlinkSqlParser.ADD_SIGN - 518)) | (1 << (FlinkSqlParser.QUESTION_MARK_SIGN - 518)))) !== 0)) {
                    {
                        this.state = 1641;
                        this.quantifiers();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    outputMode() {
        let _localctx = new OutputModeContext(this._ctx, this.state);
        this.enterRule(_localctx, 246, FlinkSqlParser.RULE_outputMode);
        try {
            this.state = 1652;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_ALL:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1644;
                        this.match(FlinkSqlParser.KW_ALL);
                        this.state = 1645;
                        this.match(FlinkSqlParser.KW_ROWS);
                        this.state = 1646;
                        this.match(FlinkSqlParser.KW_PER);
                        this.state = 1647;
                        this.match(FlinkSqlParser.KW_MATCH);
                    }
                    break;
                case FlinkSqlParser.KW_ONE:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1648;
                        this.match(FlinkSqlParser.KW_ONE);
                        this.state = 1649;
                        this.match(FlinkSqlParser.KW_ROW);
                        this.state = 1650;
                        this.match(FlinkSqlParser.KW_PER);
                        this.state = 1651;
                        this.match(FlinkSqlParser.KW_MATCH);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    afterMatchStrategy() {
        let _localctx = new AfterMatchStrategyContext(this._ctx, this.state);
        this.enterRule(_localctx, 248, FlinkSqlParser.RULE_afterMatchStrategy);
        try {
            this.state = 1678;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 188, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1654;
                        this.match(FlinkSqlParser.KW_AFTER);
                        this.state = 1655;
                        this.match(FlinkSqlParser.KW_MATCH);
                        this.state = 1656;
                        this.match(FlinkSqlParser.KW_SKIP);
                        this.state = 1657;
                        this.match(FlinkSqlParser.KW_PAST);
                        this.state = 1658;
                        this.match(FlinkSqlParser.KW_LAST);
                        this.state = 1659;
                        this.match(FlinkSqlParser.KW_ROW);
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1660;
                        this.match(FlinkSqlParser.KW_AFTER);
                        this.state = 1661;
                        this.match(FlinkSqlParser.KW_MATCH);
                        this.state = 1662;
                        this.match(FlinkSqlParser.KW_SKIP);
                        this.state = 1663;
                        this.match(FlinkSqlParser.KW_TO);
                        this.state = 1664;
                        this.match(FlinkSqlParser.KW_NEXT);
                        this.state = 1665;
                        this.match(FlinkSqlParser.KW_ROW);
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1666;
                        this.match(FlinkSqlParser.KW_AFTER);
                        this.state = 1667;
                        this.match(FlinkSqlParser.KW_MATCH);
                        this.state = 1668;
                        this.match(FlinkSqlParser.KW_SKIP);
                        this.state = 1669;
                        this.match(FlinkSqlParser.KW_TO);
                        this.state = 1670;
                        this.match(FlinkSqlParser.KW_LAST);
                        this.state = 1671;
                        this.unquotedIdentifier();
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1672;
                        this.match(FlinkSqlParser.KW_AFTER);
                        this.state = 1673;
                        this.match(FlinkSqlParser.KW_MATCH);
                        this.state = 1674;
                        this.match(FlinkSqlParser.KW_SKIP);
                        this.state = 1675;
                        this.match(FlinkSqlParser.KW_TO);
                        this.state = 1676;
                        this.match(FlinkSqlParser.KW_FIRST);
                        this.state = 1677;
                        this.unquotedIdentifier();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    patternVariablesDefination() {
        let _localctx = new PatternVariablesDefinationContext(this._ctx, this.state);
        this.enterRule(_localctx, 250, FlinkSqlParser.RULE_patternVariablesDefination);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1680;
                this.match(FlinkSqlParser.KW_DEFINE);
                this.state = 1681;
                this.projectItemDefinition();
                this.state = 1686;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 1682;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 1683;
                            this.projectItemDefinition();
                        }
                    }
                    this.state = 1688;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    windowFrame() {
        let _localctx = new WindowFrameContext(this._ctx, this.state);
        this.enterRule(_localctx, 252, FlinkSqlParser.RULE_windowFrame);
        try {
            this.state = 1698;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_RANGE:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1689;
                        this.match(FlinkSqlParser.KW_RANGE);
                        this.state = 1690;
                        this.match(FlinkSqlParser.KW_BETWEEN);
                        this.state = 1691;
                        this.timeIntervalExpression();
                        this.state = 1692;
                        this.frameBound();
                    }
                    break;
                case FlinkSqlParser.KW_ROWS:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1694;
                        this.match(FlinkSqlParser.KW_ROWS);
                        this.state = 1695;
                        this.match(FlinkSqlParser.KW_BETWEEN);
                        this.state = 1696;
                        this.match(FlinkSqlParser.DIG_LITERAL);
                        this.state = 1697;
                        this.frameBound();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    frameBound() {
        let _localctx = new FrameBoundContext(this._ctx, this.state);
        this.enterRule(_localctx, 254, FlinkSqlParser.RULE_frameBound);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1700;
                this.match(FlinkSqlParser.KW_PRECEDING);
                this.state = 1701;
                this.match(FlinkSqlParser.KW_AND);
                this.state = 1702;
                this.match(FlinkSqlParser.KW_CURRENT);
                this.state = 1703;
                this.match(FlinkSqlParser.KW_ROW);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    withinClause() {
        let _localctx = new WithinClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 256, FlinkSqlParser.RULE_withinClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1705;
                this.match(FlinkSqlParser.KW_WITHIN);
                this.state = 1706;
                this.timeIntervalExpression();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    expression() {
        let _localctx = new ExpressionContext(this._ctx, this.state);
        this.enterRule(_localctx, 258, FlinkSqlParser.RULE_expression);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1708;
                this.booleanExpression(0);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    booleanExpression(_p) {
        if (_p === undefined) {
            _p = 0;
        }
        let _parentctx = this._ctx;
        let _parentState = this.state;
        let _localctx = new BooleanExpressionContext(this._ctx, _parentState);
        let _prevctx = _localctx;
        let _startState = 260;
        this.enterRecursionRule(_localctx, 260, FlinkSqlParser.RULE_booleanExpression, _p);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1722;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 192, this._ctx)) {
                    case 1:
                        {
                            _localctx = new LogicalNotContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1711;
                            this.match(FlinkSqlParser.KW_NOT);
                            this.state = 1712;
                            this.booleanExpression(6);
                        }
                        break;
                    case 2:
                        {
                            _localctx = new ExistsContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1713;
                            this.match(FlinkSqlParser.KW_EXISTS);
                            this.state = 1714;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1715;
                            this.queryStatement(0);
                            this.state = 1716;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 3:
                        {
                            _localctx = new PredicatedContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1718;
                            this.valueExpression(0);
                            this.state = 1720;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 191, this._ctx)) {
                                case 1:
                                    {
                                        this.state = 1719;
                                        this.predicate();
                                    }
                                    break;
                            }
                        }
                        break;
                }
                this._ctx._stop = this._input.tryLT(-1);
                this.state = 1738;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 195, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        if (this._parseListeners != null) {
                            this.triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            this.state = 1736;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 194, this._ctx)) {
                                case 1:
                                    {
                                        _localctx = new LogicalBinaryContext(new BooleanExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_booleanExpression);
                                        this.state = 1724;
                                        if (!(this.precpred(this._ctx, 3))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 3)");
                                        }
                                        this.state = 1725;
                                        _localctx._operator = this.match(FlinkSqlParser.KW_AND);
                                        this.state = 1726;
                                        _localctx._right = this.booleanExpression(4);
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx = new LogicalBinaryContext(new BooleanExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_booleanExpression);
                                        this.state = 1727;
                                        if (!(this.precpred(this._ctx, 2))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 2)");
                                        }
                                        this.state = 1728;
                                        _localctx._operator = this.match(FlinkSqlParser.KW_OR);
                                        this.state = 1729;
                                        _localctx._right = this.booleanExpression(3);
                                    }
                                    break;
                                case 3:
                                    {
                                        _localctx = new LogicalNestedContext(new BooleanExpressionContext(_parentctx, _parentState));
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_booleanExpression);
                                        this.state = 1730;
                                        if (!(this.precpred(this._ctx, 1))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 1)");
                                        }
                                        this.state = 1731;
                                        this.match(FlinkSqlParser.KW_IS);
                                        this.state = 1733;
                                        this._errHandler.sync(this);
                                        _la = this._input.LA(1);
                                        if (_la === FlinkSqlParser.KW_NOT) {
                                            {
                                                this.state = 1732;
                                                this.match(FlinkSqlParser.KW_NOT);
                                            }
                                        }
                                        this.state = 1735;
                                        _localctx._kind = this._input.LT(1);
                                        _la = this._input.LA(1);
                                        if (!(_la === FlinkSqlParser.KW_FALSE || _la === FlinkSqlParser.KW_NULL || _la === FlinkSqlParser.KW_TRUE || _la === FlinkSqlParser.KW_UNKNOWN)) {
                                            _localctx._kind = this._errHandler.recoverInline(this);
                                        }
                                        else {
                                            if (this._input.LA(1) === Token.EOF) {
                                                this.matchedEOF = true;
                                            }
                                            this._errHandler.reportMatch(this);
                                            this.consume();
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    this.state = 1740;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 195, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }
    // @RuleVersion(0)
    predicate() {
        let _localctx = new PredicateContext(this._ctx, this.state);
        this.enterRule(_localctx, 262, FlinkSqlParser.RULE_predicate);
        let _la;
        try {
            this.state = 1808;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 206, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1742;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1741;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1744;
                        _localctx._kind = this.match(FlinkSqlParser.KW_BETWEEN);
                        this.state = 1746;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_ASYMMETRIC || _la === FlinkSqlParser.KW_SYMMETRIC) {
                            {
                                this.state = 1745;
                                _la = this._input.LA(1);
                                if (!(_la === FlinkSqlParser.KW_ASYMMETRIC || _la === FlinkSqlParser.KW_SYMMETRIC)) {
                                    this._errHandler.recoverInline(this);
                                }
                                else {
                                    if (this._input.LA(1) === Token.EOF) {
                                        this.matchedEOF = true;
                                    }
                                    this._errHandler.reportMatch(this);
                                    this.consume();
                                }
                            }
                        }
                        this.state = 1748;
                        _localctx._lower = this.valueExpression(0);
                        this.state = 1749;
                        this.match(FlinkSqlParser.KW_AND);
                        this.state = 1750;
                        _localctx._upper = this.valueExpression(0);
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1753;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1752;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1755;
                        _localctx._kind = this.match(FlinkSqlParser.KW_IN);
                        this.state = 1756;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1757;
                        this.expression();
                        this.state = 1762;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        while (_la === FlinkSqlParser.COMMA) {
                            {
                                {
                                    this.state = 1758;
                                    this.match(FlinkSqlParser.COMMA);
                                    this.state = 1759;
                                    this.expression();
                                }
                            }
                            this.state = 1764;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        }
                        this.state = 1765;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1768;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1767;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1770;
                        _localctx._kind = this.match(FlinkSqlParser.KW_IN);
                        this.state = 1771;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1772;
                        this.queryStatement(0);
                        this.state = 1773;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1775;
                        _localctx._kind = this.match(FlinkSqlParser.KW_EXISTS);
                        this.state = 1776;
                        this.match(FlinkSqlParser.LR_BRACKET);
                        this.state = 1777;
                        this.queryStatement(0);
                        this.state = 1778;
                        this.match(FlinkSqlParser.RR_BRACKET);
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 1781;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1780;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1783;
                        _localctx._kind = this.match(FlinkSqlParser.KW_RLIKE);
                        this.state = 1784;
                        _localctx._pattern = this.valueExpression(0);
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 1785;
                        this.likePredicate();
                    }
                    break;
                case 7:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 1786;
                        this.match(FlinkSqlParser.KW_IS);
                        this.state = 1788;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1787;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1790;
                        _localctx._kind = this._input.LT(1);
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_FALSE || _la === FlinkSqlParser.KW_NULL || _la === FlinkSqlParser.KW_TRUE || _la === FlinkSqlParser.KW_UNKNOWN)) {
                            _localctx._kind = this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                    break;
                case 8:
                    this.enterOuterAlt(_localctx, 8);
                    {
                        this.state = 1791;
                        this.match(FlinkSqlParser.KW_IS);
                        this.state = 1793;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1792;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1795;
                        _localctx._kind = this.match(FlinkSqlParser.KW_DISTINCT);
                        this.state = 1796;
                        this.match(FlinkSqlParser.KW_FROM);
                        this.state = 1797;
                        _localctx._right = this.valueExpression(0);
                    }
                    break;
                case 9:
                    this.enterOuterAlt(_localctx, 9);
                    {
                        this.state = 1799;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1798;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1801;
                        _localctx._kind = this.match(FlinkSqlParser.KW_SIMILAR);
                        this.state = 1802;
                        this.match(FlinkSqlParser.KW_TO);
                        this.state = 1803;
                        _localctx._right = this.valueExpression(0);
                        this.state = 1806;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 205, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1804;
                                    this.match(FlinkSqlParser.KW_ESCAPE);
                                    this.state = 1805;
                                    this.stringLiteral();
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    likePredicate() {
        let _localctx = new LikePredicateContext(this._ctx, this.state);
        this.enterRule(_localctx, 264, FlinkSqlParser.RULE_likePredicate);
        let _la;
        try {
            this.state = 1839;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 212, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1811;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1810;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1813;
                        _localctx._kind = this.match(FlinkSqlParser.KW_LIKE);
                        this.state = 1814;
                        _localctx._quantifier = this._input.LT(1);
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.KW_ALL || _la === FlinkSqlParser.KW_ANY)) {
                            _localctx._quantifier = this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                        this.state = 1828;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 209, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1815;
                                    this.match(FlinkSqlParser.LR_BRACKET);
                                    this.state = 1816;
                                    this.match(FlinkSqlParser.RR_BRACKET);
                                }
                                break;
                            case 2:
                                {
                                    this.state = 1817;
                                    this.match(FlinkSqlParser.LR_BRACKET);
                                    this.state = 1818;
                                    this.expression();
                                    this.state = 1823;
                                    this._errHandler.sync(this);
                                    _la = this._input.LA(1);
                                    while (_la === FlinkSqlParser.COMMA) {
                                        {
                                            {
                                                this.state = 1819;
                                                this.match(FlinkSqlParser.COMMA);
                                                this.state = 1820;
                                                this.expression();
                                            }
                                        }
                                        this.state = 1825;
                                        this._errHandler.sync(this);
                                        _la = this._input.LA(1);
                                    }
                                    this.state = 1826;
                                    this.match(FlinkSqlParser.RR_BRACKET);
                                }
                                break;
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1831;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 1830;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 1833;
                        _localctx._kind = this.match(FlinkSqlParser.KW_LIKE);
                        this.state = 1834;
                        _localctx._pattern = this.valueExpression(0);
                        this.state = 1837;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 211, this._ctx)) {
                            case 1:
                                {
                                    this.state = 1835;
                                    this.match(FlinkSqlParser.KW_ESCAPE);
                                    this.state = 1836;
                                    this.stringLiteral();
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    valueExpression(_p) {
        if (_p === undefined) {
            _p = 0;
        }
        let _parentctx = this._ctx;
        let _parentState = this.state;
        let _localctx = new ValueExpressionContext(this._ctx, _parentState);
        let _prevctx = _localctx;
        let _startState = 266;
        this.enterRecursionRule(_localctx, 266, FlinkSqlParser.RULE_valueExpression, _p);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1845;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 213, this._ctx)) {
                    case 1:
                        {
                            _localctx = new ValueExpressionDefaultContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1842;
                            this.primaryExpression(0);
                        }
                        break;
                    case 2:
                        {
                            _localctx = new ArithmeticUnaryContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1843;
                            _localctx._operator = this._input.LT(1);
                            _la = this._input.LA(1);
                            if (!(((((_la - 509)) & ~0x1F) === 0 && ((1 << (_la - 509)) & ((1 << (FlinkSqlParser.BIT_NOT_OP - 509)) | (1 << (FlinkSqlParser.HYPNEN_SIGN - 509)) | (1 << (FlinkSqlParser.ADD_SIGN - 509)))) !== 0))) {
                                _localctx._operator = this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                            this.state = 1844;
                            this.valueExpression(7);
                        }
                        break;
                }
                this._ctx._stop = this._input.tryLT(-1);
                this.state = 1868;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 215, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        if (this._parseListeners != null) {
                            this.triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            this.state = 1866;
                            this._errHandler.sync(this);
                            switch (this.interpreter.adaptivePredict(this._input, 214, this._ctx)) {
                                case 1:
                                    {
                                        _localctx = new ArithmeticBinaryContext(new ValueExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_valueExpression);
                                        this.state = 1847;
                                        if (!(this.precpred(this._ctx, 6))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 6)");
                                        }
                                        this.state = 1848;
                                        _localctx._operator = this._input.LT(1);
                                        _la = this._input.LA(1);
                                        if (!(_la === FlinkSqlParser.KW_DIV || ((((_la - 527)) & ~0x1F) === 0 && ((1 << (_la - 527)) & ((1 << (FlinkSqlParser.ASTERISK_SIGN - 527)) | (1 << (FlinkSqlParser.PENCENT_SIGN - 527)) | (1 << (FlinkSqlParser.SLASH_SIGN - 527)))) !== 0))) {
                                            _localctx._operator = this._errHandler.recoverInline(this);
                                        }
                                        else {
                                            if (this._input.LA(1) === Token.EOF) {
                                                this.matchedEOF = true;
                                            }
                                            this._errHandler.reportMatch(this);
                                            this.consume();
                                        }
                                        this.state = 1849;
                                        _localctx._right = this.valueExpression(7);
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx = new ArithmeticBinaryContext(new ValueExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_valueExpression);
                                        this.state = 1850;
                                        if (!(this.precpred(this._ctx, 5))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 5)");
                                        }
                                        this.state = 1851;
                                        _localctx._operator = this._input.LT(1);
                                        _la = this._input.LA(1);
                                        if (!(((((_la - 529)) & ~0x1F) === 0 && ((1 << (_la - 529)) & ((1 << (FlinkSqlParser.HYPNEN_SIGN - 529)) | (1 << (FlinkSqlParser.ADD_SIGN - 529)) | (1 << (FlinkSqlParser.DOUBLE_VERTICAL_SIGN - 529)))) !== 0))) {
                                            _localctx._operator = this._errHandler.recoverInline(this);
                                        }
                                        else {
                                            if (this._input.LA(1) === Token.EOF) {
                                                this.matchedEOF = true;
                                            }
                                            this._errHandler.reportMatch(this);
                                            this.consume();
                                        }
                                        this.state = 1852;
                                        _localctx._right = this.valueExpression(6);
                                    }
                                    break;
                                case 3:
                                    {
                                        _localctx = new ArithmeticBinaryContext(new ValueExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_valueExpression);
                                        this.state = 1853;
                                        if (!(this.precpred(this._ctx, 4))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 4)");
                                        }
                                        this.state = 1854;
                                        _localctx._operator = this.match(FlinkSqlParser.BIT_AND_OP);
                                        this.state = 1855;
                                        _localctx._right = this.valueExpression(5);
                                    }
                                    break;
                                case 4:
                                    {
                                        _localctx = new ArithmeticBinaryContext(new ValueExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_valueExpression);
                                        this.state = 1856;
                                        if (!(this.precpred(this._ctx, 3))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 3)");
                                        }
                                        this.state = 1857;
                                        _localctx._operator = this.match(FlinkSqlParser.BIT_XOR_OP);
                                        this.state = 1858;
                                        _localctx._right = this.valueExpression(4);
                                    }
                                    break;
                                case 5:
                                    {
                                        _localctx = new ArithmeticBinaryContext(new ValueExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_valueExpression);
                                        this.state = 1859;
                                        if (!(this.precpred(this._ctx, 2))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 2)");
                                        }
                                        this.state = 1860;
                                        _localctx._operator = this.match(FlinkSqlParser.BIT_OR_OP);
                                        this.state = 1861;
                                        _localctx._right = this.valueExpression(3);
                                    }
                                    break;
                                case 6:
                                    {
                                        _localctx = new ComparisonContext(new ValueExpressionContext(_parentctx, _parentState));
                                        _localctx._left = _prevctx;
                                        this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_valueExpression);
                                        this.state = 1862;
                                        if (!(this.precpred(this._ctx, 1))) {
                                            throw this.createFailedPredicateException("this.precpred(this._ctx, 1)");
                                        }
                                        this.state = 1863;
                                        this.comparisonOperator();
                                        this.state = 1864;
                                        _localctx._right = this.valueExpression(2);
                                    }
                                    break;
                            }
                        }
                    }
                    this.state = 1870;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 215, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }
    // @RuleVersion(0)
    primaryExpression(_p) {
        if (_p === undefined) {
            _p = 0;
        }
        let _parentctx = this._ctx;
        let _parentState = this.state;
        let _localctx = new PrimaryExpressionContext(this._ctx, _parentState);
        let _prevctx = _localctx;
        let _startState = 268;
        this.enterRecursionRule(_localctx, 268, FlinkSqlParser.RULE_primaryExpression, _p);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1963;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 225, this._ctx)) {
                    case 1:
                        {
                            _localctx = new SearchedCaseContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1872;
                            this.match(FlinkSqlParser.KW_CASE);
                            this.state = 1874;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            do {
                                {
                                    {
                                        this.state = 1873;
                                        this.whenClause();
                                    }
                                }
                                this.state = 1876;
                                this._errHandler.sync(this);
                                _la = this._input.LA(1);
                            } while (_la === FlinkSqlParser.KW_WHEN);
                            this.state = 1880;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === FlinkSqlParser.KW_ELSE) {
                                {
                                    this.state = 1878;
                                    this.match(FlinkSqlParser.KW_ELSE);
                                    this.state = 1879;
                                    _localctx._elseExpression = this.expression();
                                }
                            }
                            this.state = 1882;
                            this.match(FlinkSqlParser.KW_END);
                        }
                        break;
                    case 2:
                        {
                            _localctx = new SimpleCaseContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1884;
                            this.match(FlinkSqlParser.KW_CASE);
                            this.state = 1885;
                            _localctx._value = this.expression();
                            this.state = 1887;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            do {
                                {
                                    {
                                        this.state = 1886;
                                        this.whenClause();
                                    }
                                }
                                this.state = 1889;
                                this._errHandler.sync(this);
                                _la = this._input.LA(1);
                            } while (_la === FlinkSqlParser.KW_WHEN);
                            this.state = 1893;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === FlinkSqlParser.KW_ELSE) {
                                {
                                    this.state = 1891;
                                    this.match(FlinkSqlParser.KW_ELSE);
                                    this.state = 1892;
                                    _localctx._elseExpression = this.expression();
                                }
                            }
                            this.state = 1895;
                            this.match(FlinkSqlParser.KW_END);
                        }
                        break;
                    case 3:
                        {
                            _localctx = new CastContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1897;
                            this.match(FlinkSqlParser.KW_CAST);
                            this.state = 1898;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1899;
                            this.expression();
                            this.state = 1900;
                            this.match(FlinkSqlParser.KW_AS);
                            this.state = 1901;
                            this.columnType();
                            this.state = 1902;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 4:
                        {
                            _localctx = new FirstContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1904;
                            this.match(FlinkSqlParser.KW_FIRST);
                            this.state = 1905;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1906;
                            this.expression();
                            this.state = 1909;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === FlinkSqlParser.KW_IGNORE) {
                                {
                                    this.state = 1907;
                                    this.match(FlinkSqlParser.KW_IGNORE);
                                    this.state = 1908;
                                    this.match(FlinkSqlParser.KW_NULLS);
                                }
                            }
                            this.state = 1911;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 5:
                        {
                            _localctx = new LastContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1913;
                            this.match(FlinkSqlParser.KW_LAST);
                            this.state = 1914;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1915;
                            this.expression();
                            this.state = 1918;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === FlinkSqlParser.KW_IGNORE) {
                                {
                                    this.state = 1916;
                                    this.match(FlinkSqlParser.KW_IGNORE);
                                    this.state = 1917;
                                    this.match(FlinkSqlParser.KW_NULLS);
                                }
                            }
                            this.state = 1920;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 6:
                        {
                            _localctx = new PositionContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1922;
                            this.match(FlinkSqlParser.KW_POSITION);
                            this.state = 1923;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1924;
                            _localctx._substr = this.valueExpression(0);
                            this.state = 1925;
                            this.match(FlinkSqlParser.KW_IN);
                            this.state = 1926;
                            _localctx._str = this.valueExpression(0);
                            this.state = 1927;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 7:
                        {
                            _localctx = new ConstantDefaultContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1929;
                            this.constant();
                        }
                        break;
                    case 8:
                        {
                            _localctx = new StarContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1930;
                            this.match(FlinkSqlParser.ASTERISK_SIGN);
                        }
                        break;
                    case 9:
                        {
                            _localctx = new StarContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1931;
                            this.uid();
                            this.state = 1932;
                            this.match(FlinkSqlParser.DOT);
                            this.state = 1933;
                            this.match(FlinkSqlParser.ASTERISK_SIGN);
                        }
                        break;
                    case 10:
                        {
                            _localctx = new SubqueryExpressionContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1935;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1936;
                            this.queryStatement(0);
                            this.state = 1937;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 11:
                        {
                            _localctx = new FunctionCallContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1939;
                            this.functionName();
                            this.state = 1940;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1952;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << FlinkSqlParser.KW_ABS) | (1 << FlinkSqlParser.KW_ALL) | (1 << FlinkSqlParser.KW_ARRAY) | (1 << FlinkSqlParser.KW_AVG))) !== 0) || ((((_la - 33)) & ~0x1F) === 0 && ((1 << (_la - 33)) & ((1 << (FlinkSqlParser.KW_BOTH - 33)) | (1 << (FlinkSqlParser.KW_CARDINALITY - 33)) | (1 << (FlinkSqlParser.KW_CASE - 33)) | (1 << (FlinkSqlParser.KW_CAST - 33)) | (1 << (FlinkSqlParser.KW_CEIL - 33)) | (1 << (FlinkSqlParser.KW_CEILING - 33)) | (1 << (FlinkSqlParser.KW_COALESCE - 33)) | (1 << (FlinkSqlParser.KW_COLLECT - 33)))) !== 0) || ((((_la - 69)) & ~0x1F) === 0 && ((1 << (_la - 69)) & ((1 << (FlinkSqlParser.KW_COUNT - 69)) | (1 << (FlinkSqlParser.KW_CUME_DIST - 69)) | (1 << (FlinkSqlParser.KW_CURRENT_DATE - 69)) | (1 << (FlinkSqlParser.KW_CURRENT_TIME - 69)) | (1 << (FlinkSqlParser.KW_CURRENT_TIMESTAMP - 69)) | (1 << (FlinkSqlParser.KW_DATE - 69)) | (1 << (FlinkSqlParser.KW_DAY - 69)) | (1 << (FlinkSqlParser.KW_DAYOFWEEK - 69)) | (1 << (FlinkSqlParser.KW_DAYOFYEAR - 69)))) !== 0) || ((((_la - 107)) & ~0x1F) === 0 && ((1 << (_la - 107)) & ((1 << (FlinkSqlParser.KW_DENSE_RANK - 107)) | (1 << (FlinkSqlParser.KW_DISTINCT - 107)) | (1 << (FlinkSqlParser.KW_ELEMENT - 107)) | (1 << (FlinkSqlParser.KW_EXISTS - 107)) | (1 << (FlinkSqlParser.KW_EXP - 107)))) !== 0) || ((((_la - 139)) & ~0x1F) === 0 && ((1 << (_la - 139)) & ((1 << (FlinkSqlParser.KW_EXTRACT - 139)) | (1 << (FlinkSqlParser.KW_FALSE - 139)) | (1 << (FlinkSqlParser.KW_FIRST_VALUE - 139)) | (1 << (FlinkSqlParser.KW_FLOOR - 139)) | (1 << (FlinkSqlParser.KW_GROUPING - 139)) | (1 << (FlinkSqlParser.KW_HOUR - 139)) | (1 << (FlinkSqlParser.KW_IF - 139)))) !== 0) || ((((_la - 182)) & ~0x1F) === 0 && ((1 << (_la - 182)) & ((1 << (FlinkSqlParser.KW_INTERVAL - 182)) | (1 << (FlinkSqlParser.KW_LAG - 182)) | (1 << (FlinkSqlParser.KW_LAST_VALUE - 182)) | (1 << (FlinkSqlParser.KW_LEAD - 182)) | (1 << (FlinkSqlParser.KW_LEADING - 182)) | (1 << (FlinkSqlParser.KW_LEFT - 182)) | (1 << (FlinkSqlParser.KW_LN - 182)) | (1 << (FlinkSqlParser.KW_LOCALTIME - 182)) | (1 << (FlinkSqlParser.KW_LOCALTIMESTAMP - 182)) | (1 << (FlinkSqlParser.KW_LOWER - 182)))) !== 0) || ((((_la - 214)) & ~0x1F) === 0 && ((1 << (_la - 214)) & ((1 << (FlinkSqlParser.KW_MAX - 214)) | (1 << (FlinkSqlParser.KW_MIN - 214)) | (1 << (FlinkSqlParser.KW_MINUTE - 214)) | (1 << (FlinkSqlParser.KW_MOD - 214)) | (1 << (FlinkSqlParser.KW_MONTH - 214)) | (1 << (FlinkSqlParser.KW_NOT - 214)) | (1 << (FlinkSqlParser.KW_NTILE - 214)) | (1 << (FlinkSqlParser.KW_NULL - 214)) | (1 << (FlinkSqlParser.KW_NULLIF - 214)))) !== 0) || ((((_la - 264)) & ~0x1F) === 0 && ((1 << (_la - 264)) & ((1 << (FlinkSqlParser.KW_OVERLAY - 264)) | (1 << (FlinkSqlParser.KW_PERCENT_RANK - 264)) | (1 << (FlinkSqlParser.KW_POSITION - 264)) | (1 << (FlinkSqlParser.KW_POWER - 264)) | (1 << (FlinkSqlParser.KW_RANK - 264)))) !== 0) || ((((_la - 317)) & ~0x1F) === 0 && ((1 << (_la - 317)) & ((1 << (FlinkSqlParser.KW_RIGHT - 317)) | (1 << (FlinkSqlParser.KW_ROW - 317)) | (1 << (FlinkSqlParser.KW_ROWS - 317)) | (1 << (FlinkSqlParser.KW_ROW_NUMBER - 317)) | (1 << (FlinkSqlParser.KW_SECOND - 317)))) !== 0) || ((((_la - 357)) & ~0x1F) === 0 && ((1 << (_la - 357)) & ((1 << (FlinkSqlParser.KW_STDDEV_POP - 357)) | (1 << (FlinkSqlParser.KW_STDDEV_SAMP - 357)) | (1 << (FlinkSqlParser.KW_SUBSTRING - 357)) | (1 << (FlinkSqlParser.KW_SUM - 357)) | (1 << (FlinkSqlParser.KW_TIME - 357)) | (1 << (FlinkSqlParser.KW_TIMESTAMP - 357)) | (1 << (FlinkSqlParser.KW_TIMESTAMP_DIFF - 357)))) !== 0) || ((((_la - 389)) & ~0x1F) === 0 && ((1 << (_la - 389)) & ((1 << (FlinkSqlParser.KW_TRAILING - 389)) | (1 << (FlinkSqlParser.KW_TRIM - 389)) | (1 << (FlinkSqlParser.KW_TRUE - 389)) | (1 << (FlinkSqlParser.KW_TRUNCATE - 389)) | (1 << (FlinkSqlParser.KW_TRY_CAST - 389)) | (1 << (FlinkSqlParser.KW_UPPER - 389)) | (1 << (FlinkSqlParser.KW_VALUE - 389)) | (1 << (FlinkSqlParser.KW_VAR_POP - 389)) | (1 << (FlinkSqlParser.KW_VAR_SAMP - 389)))) !== 0) || ((((_la - 427)) & ~0x1F) === 0 && ((1 << (_la - 427)) & ((1 << (FlinkSqlParser.KW_WEEKS - 427)) | (1 << (FlinkSqlParser.KW_YEAR - 427)) | (1 << (FlinkSqlParser.KW_ADD - 427)) | (1 << (FlinkSqlParser.KW_AFTER - 427)) | (1 << (FlinkSqlParser.KW_ASC - 427)) | (1 << (FlinkSqlParser.KW_CASCADE - 427)) | (1 << (FlinkSqlParser.KW_CATALOG - 427)) | (1 << (FlinkSqlParser.KW_CENTURY - 427)) | (1 << (FlinkSqlParser.KW_CONFIG - 427)) | (1 << (FlinkSqlParser.KW_CONSTRAINTS - 427)) | (1 << (FlinkSqlParser.KW_CUMULATE - 427)) | (1 << (FlinkSqlParser.KW_DATA - 427)) | (1 << (FlinkSqlParser.KW_DATABASE - 427)) | (1 << (FlinkSqlParser.KW_DAYS - 427)) | (1 << (FlinkSqlParser.KW_DECADE - 427)) | (1 << (FlinkSqlParser.KW_DESC - 427)) | (1 << (FlinkSqlParser.KW_DESCRIPTOR - 427)) | (1 << (FlinkSqlParser.KW_DIV - 427)) | (1 << (FlinkSqlParser.KW_ENGINE - 427)) | (1 << (FlinkSqlParser.KW_EPOCH - 427)) | (1 << (FlinkSqlParser.KW_EXCLUDING - 427)) | (1 << (FlinkSqlParser.KW_FILE - 427)) | (1 << (FlinkSqlParser.KW_FIRST - 427)) | (1 << (FlinkSqlParser.KW_GENERATED - 427)))) !== 0) || ((((_la - 459)) & ~0x1F) === 0 && ((1 << (_la - 459)) & ((1 << (FlinkSqlParser.KW_HOP - 459)) | (1 << (FlinkSqlParser.KW_HOURS - 459)) | (1 << (FlinkSqlParser.KW_IGNORE - 459)) | (1 << (FlinkSqlParser.KW_INCLUDING - 459)) | (1 << (FlinkSqlParser.KW_JAR - 459)) | (1 << (FlinkSqlParser.KW_JARS - 459)) | (1 << (FlinkSqlParser.KW_JAVA - 459)) | (1 << (FlinkSqlParser.KW_KEY - 459)) | (1 << (FlinkSqlParser.KW_LAST - 459)) | (1 << (FlinkSqlParser.KW_LOAD - 459)) | (1 << (FlinkSqlParser.KW_MAP - 459)) | (1 << (FlinkSqlParser.KW_MICROSECOND - 459)) | (1 << (FlinkSqlParser.KW_MILLENNIUM - 459)) | (1 << (FlinkSqlParser.KW_MILLISECOND - 459)) | (1 << (FlinkSqlParser.KW_MINUTES - 459)) | (1 << (FlinkSqlParser.KW_MONTHS - 459)) | (1 << (FlinkSqlParser.KW_NANOSECOND - 459)) | (1 << (FlinkSqlParser.KW_NULLS - 459)) | (1 << (FlinkSqlParser.KW_OPTIONS - 459)) | (1 << (FlinkSqlParser.KW_PAST - 459)) | (1 << (FlinkSqlParser.KW_PLAN - 459)) | (1 << (FlinkSqlParser.KW_PRECEDING - 459)) | (1 << (FlinkSqlParser.KW_PYTHON - 459)) | (1 << (FlinkSqlParser.KW_PYTHON_ARCHIVES - 459)) | (1 << (FlinkSqlParser.KW_PYTHON_DEPENDENCIES - 459)) | (1 << (FlinkSqlParser.KW_PYTHON_FILES - 459)) | (1 << (FlinkSqlParser.KW_PYTHON_JAR - 459)) | (1 << (FlinkSqlParser.KW_PYTHON_PARAMETER - 459)) | (1 << (FlinkSqlParser.KW_PYTHON_REQUIREMENTS - 459)) | (1 << (FlinkSqlParser.KW_QUARTER - 459)) | (1 << (FlinkSqlParser.KW_REMOVE - 459)) | (1 << (FlinkSqlParser.KW_RESTRICT - 459)))) !== 0) || ((((_la - 491)) & ~0x1F) === 0 && ((1 << (_la - 491)) & ((1 << (FlinkSqlParser.KW_SECONDS - 491)) | (1 << (FlinkSqlParser.KW_SESSION - 491)) | (1 << (FlinkSqlParser.KW_SETS - 491)) | (1 << (FlinkSqlParser.KW_SIZE - 491)) | (1 << (FlinkSqlParser.KW_SLIDE - 491)) | (1 << (FlinkSqlParser.KW_STEP - 491)) | (1 << (FlinkSqlParser.KW_TEMPORARY - 491)) | (1 << (FlinkSqlParser.KW_TIMECOL - 491)) | (1 << (FlinkSqlParser.KW_TUMBLE - 491)) | (1 << (FlinkSqlParser.KW_UNLOAD - 491)) | (1 << (FlinkSqlParser.KW_VIEW - 491)) | (1 << (FlinkSqlParser.KW_WEEK - 491)) | (1 << (FlinkSqlParser.KW_YEARS - 491)) | (1 << (FlinkSqlParser.KW_ZONE - 491)) | (1 << (FlinkSqlParser.BIT_NOT_OP - 491)) | (1 << (FlinkSqlParser.LR_BRACKET - 491)))) !== 0) || ((((_la - 527)) & ~0x1F) === 0 && ((1 << (_la - 527)) & ((1 << (FlinkSqlParser.ASTERISK_SIGN - 527)) | (1 << (FlinkSqlParser.HYPNEN_SIGN - 527)) | (1 << (FlinkSqlParser.ADD_SIGN - 527)) | (1 << (FlinkSqlParser.STRING_LITERAL - 527)) | (1 << (FlinkSqlParser.DIG_LITERAL - 527)) | (1 << (FlinkSqlParser.REAL_LITERAL - 527)) | (1 << (FlinkSqlParser.BIT_STRING - 527)) | (1 << (FlinkSqlParser.ID_LITERAL - 527)))) !== 0)) {
                                {
                                    this.state = 1942;
                                    this._errHandler.sync(this);
                                    switch (this.interpreter.adaptivePredict(this._input, 222, this._ctx)) {
                                        case 1:
                                            {
                                                this.state = 1941;
                                                this.setQuantifier();
                                            }
                                            break;
                                    }
                                    this.state = 1944;
                                    this.functionParam();
                                    this.state = 1949;
                                    this._errHandler.sync(this);
                                    _la = this._input.LA(1);
                                    while (_la === FlinkSqlParser.COMMA) {
                                        {
                                            {
                                                this.state = 1945;
                                                this.match(FlinkSqlParser.COMMA);
                                                this.state = 1946;
                                                this.functionParam();
                                            }
                                        }
                                        this.state = 1951;
                                        this._errHandler.sync(this);
                                        _la = this._input.LA(1);
                                    }
                                }
                            }
                            this.state = 1954;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 12:
                        {
                            _localctx = new ColumnReferenceContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1956;
                            this.identifier();
                        }
                        break;
                    case 13:
                        {
                            _localctx = new DereferenceContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1957;
                            this.dereferenceDefinition();
                        }
                        break;
                    case 14:
                        {
                            _localctx = new ParenthesizedExpressionContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1958;
                            this.match(FlinkSqlParser.LR_BRACKET);
                            this.state = 1959;
                            this.expression();
                            this.state = 1960;
                            this.match(FlinkSqlParser.RR_BRACKET);
                        }
                        break;
                    case 15:
                        {
                            _localctx = new DateFunctionExpressionContext(_localctx);
                            this._ctx = _localctx;
                            _prevctx = _localctx;
                            this.state = 1962;
                            this.match(FlinkSqlParser.KW_CURRENT_TIMESTAMP);
                        }
                        break;
                }
                this._ctx._stop = this._input.tryLT(-1);
                this.state = 1972;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 226, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        if (this._parseListeners != null) {
                            this.triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new SubscriptContext(new PrimaryExpressionContext(_parentctx, _parentState));
                                _localctx._value = _prevctx;
                                this.pushNewRecursionContext(_localctx, _startState, FlinkSqlParser.RULE_primaryExpression);
                                this.state = 1965;
                                if (!(this.precpred(this._ctx, 5))) {
                                    throw this.createFailedPredicateException("this.precpred(this._ctx, 5)");
                                }
                                this.state = 1966;
                                this.match(FlinkSqlParser.LS_BRACKET);
                                this.state = 1967;
                                _localctx._index = this.valueExpression(0);
                                this.state = 1968;
                                this.match(FlinkSqlParser.RS_BRACKET);
                            }
                        }
                    }
                    this.state = 1974;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 226, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }
    // @RuleVersion(0)
    functionNameCreate() {
        let _localctx = new FunctionNameCreateContext(this._ctx, this.state);
        this.enterRule(_localctx, 270, FlinkSqlParser.RULE_functionNameCreate);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1975;
                this.uid();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    functionName() {
        let _localctx = new FunctionNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 272, FlinkSqlParser.RULE_functionName);
        try {
            this.state = 1979;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 227, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1977;
                        this.reservedKeywordsUsedAsFuncName();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1978;
                        this.uid();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    functionParam() {
        let _localctx = new FunctionParamContext(this._ctx, this.state);
        this.enterRule(_localctx, 274, FlinkSqlParser.RULE_functionParam);
        try {
            this.state = 1985;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 228, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1981;
                        this.reservedKeywordsUsedAsFuncParam();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1982;
                        this.timeIntervalUnit();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1983;
                        this.timePointUnit();
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1984;
                        this.expression();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    dereferenceDefinition() {
        let _localctx = new DereferenceDefinitionContext(this._ctx, this.state);
        this.enterRule(_localctx, 276, FlinkSqlParser.RULE_dereferenceDefinition);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1987;
                this.uid();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    correlationName() {
        let _localctx = new CorrelationNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 278, FlinkSqlParser.RULE_correlationName);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1989;
                this.identifier();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    qualifiedName() {
        let _localctx = new QualifiedNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 280, FlinkSqlParser.RULE_qualifiedName);
        try {
            this.state = 1993;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 229, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1991;
                        this.identifier();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1992;
                        this.dereferenceDefinition();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    timeIntervalExpression() {
        let _localctx = new TimeIntervalExpressionContext(this._ctx, this.state);
        this.enterRule(_localctx, 282, FlinkSqlParser.RULE_timeIntervalExpression);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1995;
                this.match(FlinkSqlParser.KW_INTERVAL);
                this.state = 1998;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 230, this._ctx)) {
                    case 1:
                        {
                            this.state = 1996;
                            this.errorCapturingMultiUnitsInterval();
                        }
                        break;
                    case 2:
                        {
                            this.state = 1997;
                            this.errorCapturingUnitToUnitInterval();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    errorCapturingMultiUnitsInterval() {
        let _localctx = new ErrorCapturingMultiUnitsIntervalContext(this._ctx, this.state);
        this.enterRule(_localctx, 284, FlinkSqlParser.RULE_errorCapturingMultiUnitsInterval);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2000;
                this.multiUnitsInterval();
                this.state = 2002;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 231, this._ctx)) {
                    case 1:
                        {
                            this.state = 2001;
                            this.unitToUnitInterval();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    multiUnitsInterval() {
        let _localctx = new MultiUnitsIntervalContext(this._ctx, this.state);
        this.enterRule(_localctx, 286, FlinkSqlParser.RULE_multiUnitsInterval);
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2007;
                this._errHandler.sync(this);
                _alt = 1;
                do {
                    switch (_alt) {
                        case 1:
                            {
                                {
                                    this.state = 2004;
                                    this.intervalValue();
                                    this.state = 2005;
                                    this.timeIntervalUnit();
                                }
                            }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    this.state = 2009;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 232, this._ctx);
                } while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    errorCapturingUnitToUnitInterval() {
        let _localctx = new ErrorCapturingUnitToUnitIntervalContext(this._ctx, this.state);
        this.enterRule(_localctx, 288, FlinkSqlParser.RULE_errorCapturingUnitToUnitInterval);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2011;
                _localctx._body = this.unitToUnitInterval();
                this.state = 2014;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 233, this._ctx)) {
                    case 1:
                        {
                            this.state = 2012;
                            _localctx._error1 = this.multiUnitsInterval();
                        }
                        break;
                    case 2:
                        {
                            this.state = 2013;
                            _localctx._error2 = this.unitToUnitInterval();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    unitToUnitInterval() {
        let _localctx = new UnitToUnitIntervalContext(this._ctx, this.state);
        this.enterRule(_localctx, 290, FlinkSqlParser.RULE_unitToUnitInterval);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2016;
                _localctx._value = this.intervalValue();
                this.state = 2017;
                _localctx._from = this.timeIntervalUnit();
                this.state = 2018;
                this.match(FlinkSqlParser.KW_TO);
                this.state = 2019;
                _localctx._to = this.timeIntervalUnit();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    intervalValue() {
        let _localctx = new IntervalValueContext(this._ctx, this.state);
        this.enterRule(_localctx, 292, FlinkSqlParser.RULE_intervalValue);
        let _la;
        try {
            this.state = 2026;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.HYPNEN_SIGN:
                case FlinkSqlParser.ADD_SIGN:
                case FlinkSqlParser.DIG_LITERAL:
                case FlinkSqlParser.REAL_LITERAL:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2022;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.HYPNEN_SIGN || _la === FlinkSqlParser.ADD_SIGN) {
                            {
                                this.state = 2021;
                                _la = this._input.LA(1);
                                if (!(_la === FlinkSqlParser.HYPNEN_SIGN || _la === FlinkSqlParser.ADD_SIGN)) {
                                    this._errHandler.recoverInline(this);
                                }
                                else {
                                    if (this._input.LA(1) === Token.EOF) {
                                        this.matchedEOF = true;
                                    }
                                    this._errHandler.reportMatch(this);
                                    this.consume();
                                }
                            }
                        }
                        this.state = 2024;
                        _la = this._input.LA(1);
                        if (!(_la === FlinkSqlParser.DIG_LITERAL || _la === FlinkSqlParser.REAL_LITERAL)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                    break;
                case FlinkSqlParser.STRING_LITERAL:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2025;
                        this.match(FlinkSqlParser.STRING_LITERAL);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tableAlias() {
        let _localctx = new TableAliasContext(this._ctx, this.state);
        this.enterRule(_localctx, 294, FlinkSqlParser.RULE_tableAlias);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2029;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_AS) {
                    {
                        this.state = 2028;
                        this.match(FlinkSqlParser.KW_AS);
                    }
                }
                this.state = 2031;
                this.identifier();
                this.state = 2033;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 237, this._ctx)) {
                    case 1:
                        {
                            this.state = 2032;
                            this.identifierList();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    errorCapturingIdentifier() {
        let _localctx = new ErrorCapturingIdentifierContext(this._ctx, this.state);
        this.enterRule(_localctx, 296, FlinkSqlParser.RULE_errorCapturingIdentifier);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2035;
                this.identifier();
                this.state = 2036;
                this.errorCapturingIdentifierExtra();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    errorCapturingIdentifierExtra() {
        let _localctx = new ErrorCapturingIdentifierExtraContext(this._ctx, this.state);
        this.enterRule(_localctx, 298, FlinkSqlParser.RULE_errorCapturingIdentifierExtra);
        let _la;
        try {
            this.state = 2045;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_MINUS:
                    _localctx = new ErrorIdentContext(_localctx);
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2040;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 2038;
                                    this.match(FlinkSqlParser.KW_MINUS);
                                    this.state = 2039;
                                    this.identifier();
                                }
                            }
                            this.state = 2042;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (_la === FlinkSqlParser.KW_MINUS);
                    }
                    break;
                case FlinkSqlParser.KW_AS:
                case FlinkSqlParser.LR_BRACKET:
                    _localctx = new RealIdentContext(_localctx);
                    this.enterOuterAlt(_localctx, 2);
                    // tslint:disable-next-line:no-empty
                    {
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    identifierList() {
        let _localctx = new IdentifierListContext(this._ctx, this.state);
        this.enterRule(_localctx, 300, FlinkSqlParser.RULE_identifierList);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2047;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 2048;
                this.identifierSeq();
                this.state = 2049;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    identifierSeq() {
        let _localctx = new IdentifierSeqContext(this._ctx, this.state);
        this.enterRule(_localctx, 302, FlinkSqlParser.RULE_identifierSeq);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2051;
                this.identifier();
                this.state = 2056;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 2052;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 2053;
                            this.identifier();
                        }
                    }
                    this.state = 2058;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    identifier() {
        let _localctx = new IdentifierContext(this._ctx, this.state);
        this.enterRule(_localctx, 304, FlinkSqlParser.RULE_identifier);
        try {
            this.state = 2062;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.DIG_LITERAL:
                case FlinkSqlParser.ID_LITERAL:
                    _localctx = new UnquotedIdentifierAlternativeContext(_localctx);
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2059;
                        this.unquotedIdentifier();
                    }
                    break;
                case FlinkSqlParser.STRING_LITERAL:
                    _localctx = new QuotedIdentifierAlternativeContext(_localctx);
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2060;
                        this.quotedIdentifier();
                    }
                    break;
                case FlinkSqlParser.KW_ADD:
                case FlinkSqlParser.KW_AFTER:
                case FlinkSqlParser.KW_ASC:
                case FlinkSqlParser.KW_CASCADE:
                case FlinkSqlParser.KW_CATALOG:
                case FlinkSqlParser.KW_CENTURY:
                case FlinkSqlParser.KW_CONFIG:
                case FlinkSqlParser.KW_CONSTRAINTS:
                case FlinkSqlParser.KW_CUMULATE:
                case FlinkSqlParser.KW_DATA:
                case FlinkSqlParser.KW_DATABASE:
                case FlinkSqlParser.KW_DAYS:
                case FlinkSqlParser.KW_DECADE:
                case FlinkSqlParser.KW_DESC:
                case FlinkSqlParser.KW_DESCRIPTOR:
                case FlinkSqlParser.KW_DIV:
                case FlinkSqlParser.KW_ENGINE:
                case FlinkSqlParser.KW_EPOCH:
                case FlinkSqlParser.KW_EXCLUDING:
                case FlinkSqlParser.KW_FILE:
                case FlinkSqlParser.KW_FIRST:
                case FlinkSqlParser.KW_GENERATED:
                case FlinkSqlParser.KW_HOP:
                case FlinkSqlParser.KW_HOURS:
                case FlinkSqlParser.KW_IGNORE:
                case FlinkSqlParser.KW_INCLUDING:
                case FlinkSqlParser.KW_JAR:
                case FlinkSqlParser.KW_JARS:
                case FlinkSqlParser.KW_JAVA:
                case FlinkSqlParser.KW_KEY:
                case FlinkSqlParser.KW_LAST:
                case FlinkSqlParser.KW_LOAD:
                case FlinkSqlParser.KW_MAP:
                case FlinkSqlParser.KW_MICROSECOND:
                case FlinkSqlParser.KW_MILLENNIUM:
                case FlinkSqlParser.KW_MILLISECOND:
                case FlinkSqlParser.KW_MINUTES:
                case FlinkSqlParser.KW_MONTHS:
                case FlinkSqlParser.KW_NANOSECOND:
                case FlinkSqlParser.KW_NULLS:
                case FlinkSqlParser.KW_OPTIONS:
                case FlinkSqlParser.KW_PAST:
                case FlinkSqlParser.KW_PLAN:
                case FlinkSqlParser.KW_PRECEDING:
                case FlinkSqlParser.KW_PYTHON:
                case FlinkSqlParser.KW_PYTHON_ARCHIVES:
                case FlinkSqlParser.KW_PYTHON_DEPENDENCIES:
                case FlinkSqlParser.KW_PYTHON_FILES:
                case FlinkSqlParser.KW_PYTHON_JAR:
                case FlinkSqlParser.KW_PYTHON_PARAMETER:
                case FlinkSqlParser.KW_PYTHON_REQUIREMENTS:
                case FlinkSqlParser.KW_QUARTER:
                case FlinkSqlParser.KW_REMOVE:
                case FlinkSqlParser.KW_RESTRICT:
                case FlinkSqlParser.KW_SECONDS:
                case FlinkSqlParser.KW_SESSION:
                case FlinkSqlParser.KW_SETS:
                case FlinkSqlParser.KW_SIZE:
                case FlinkSqlParser.KW_SLIDE:
                case FlinkSqlParser.KW_STEP:
                case FlinkSqlParser.KW_TEMPORARY:
                case FlinkSqlParser.KW_TIMECOL:
                case FlinkSqlParser.KW_TUMBLE:
                case FlinkSqlParser.KW_UNLOAD:
                case FlinkSqlParser.KW_VIEW:
                case FlinkSqlParser.KW_WEEK:
                case FlinkSqlParser.KW_YEARS:
                case FlinkSqlParser.KW_ZONE:
                    _localctx = new NonReservedKeywordsAlternativeContext(_localctx);
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 2061;
                        this.nonReservedKeywords();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    unquotedIdentifier() {
        let _localctx = new UnquotedIdentifierContext(this._ctx, this.state);
        this.enterRule(_localctx, 306, FlinkSqlParser.RULE_unquotedIdentifier);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2064;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.DIG_LITERAL || _la === FlinkSqlParser.ID_LITERAL)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    quotedIdentifier() {
        let _localctx = new QuotedIdentifierContext(this._ctx, this.state);
        this.enterRule(_localctx, 308, FlinkSqlParser.RULE_quotedIdentifier);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2066;
                this.match(FlinkSqlParser.STRING_LITERAL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    whenClause() {
        let _localctx = new WhenClauseContext(this._ctx, this.state);
        this.enterRule(_localctx, 310, FlinkSqlParser.RULE_whenClause);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2068;
                this.match(FlinkSqlParser.KW_WHEN);
                this.state = 2069;
                _localctx._condition = this.expression();
                this.state = 2070;
                this.match(FlinkSqlParser.KW_THEN);
                this.state = 2071;
                _localctx._result = this.expression();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    catalogPath() {
        let _localctx = new CatalogPathContext(this._ctx, this.state);
        this.enterRule(_localctx, 312, FlinkSqlParser.RULE_catalogPath);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2073;
                this.identifier();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    catalogPathCreate() {
        let _localctx = new CatalogPathCreateContext(this._ctx, this.state);
        this.enterRule(_localctx, 314, FlinkSqlParser.RULE_catalogPathCreate);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2075;
                this.identifier();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    databasePath() {
        let _localctx = new DatabasePathContext(this._ctx, this.state);
        this.enterRule(_localctx, 316, FlinkSqlParser.RULE_databasePath);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2077;
                this.identifier();
                this.state = 2080;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.DOT) {
                    {
                        this.state = 2078;
                        this.match(FlinkSqlParser.DOT);
                        this.state = 2079;
                        this.identifier();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    databasePathCreate() {
        let _localctx = new DatabasePathCreateContext(this._ctx, this.state);
        this.enterRule(_localctx, 318, FlinkSqlParser.RULE_databasePathCreate);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2082;
                this.identifier();
                this.state = 2085;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.DOT) {
                    {
                        this.state = 2083;
                        this.match(FlinkSqlParser.DOT);
                        this.state = 2084;
                        this.identifier();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tablePathCreate() {
        let _localctx = new TablePathCreateContext(this._ctx, this.state);
        this.enterRule(_localctx, 320, FlinkSqlParser.RULE_tablePathCreate);
        let _la;
        try {
            this.state = 2099;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 246, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2087;
                        this.identifier();
                        this.state = 2090;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.DOT) {
                            {
                                this.state = 2088;
                                this.match(FlinkSqlParser.DOT);
                                this.state = 2089;
                                this.identifier();
                            }
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2092;
                        this.identifier();
                        this.state = 2093;
                        this.match(FlinkSqlParser.DOT);
                        this.state = 2094;
                        this.identifier();
                        this.state = 2097;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.DOT) {
                            {
                                this.state = 2095;
                                this.match(FlinkSqlParser.DOT);
                                this.state = 2096;
                                this.identifier();
                            }
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tablePath() {
        let _localctx = new TablePathContext(this._ctx, this.state);
        this.enterRule(_localctx, 322, FlinkSqlParser.RULE_tablePath);
        try {
            this.state = 2113;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 249, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2101;
                        this.identifier();
                        this.state = 2104;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 247, this._ctx)) {
                            case 1:
                                {
                                    this.state = 2102;
                                    this.match(FlinkSqlParser.DOT);
                                    this.state = 2103;
                                    this.identifier();
                                }
                                break;
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2106;
                        this.identifier();
                        this.state = 2107;
                        this.match(FlinkSqlParser.DOT);
                        this.state = 2108;
                        this.identifier();
                        this.state = 2111;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 248, this._ctx)) {
                            case 1:
                                {
                                    this.state = 2109;
                                    this.match(FlinkSqlParser.DOT);
                                    this.state = 2110;
                                    this.identifier();
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    viewPath() {
        let _localctx = new ViewPathContext(this._ctx, this.state);
        this.enterRule(_localctx, 324, FlinkSqlParser.RULE_viewPath);
        try {
            this.state = 2127;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 252, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2115;
                        this.identifier();
                        this.state = 2118;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 250, this._ctx)) {
                            case 1:
                                {
                                    this.state = 2116;
                                    this.match(FlinkSqlParser.DOT);
                                    this.state = 2117;
                                    this.identifier();
                                }
                                break;
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2120;
                        this.identifier();
                        this.state = 2121;
                        this.match(FlinkSqlParser.DOT);
                        this.state = 2122;
                        this.identifier();
                        this.state = 2125;
                        this._errHandler.sync(this);
                        switch (this.interpreter.adaptivePredict(this._input, 251, this._ctx)) {
                            case 1:
                                {
                                    this.state = 2123;
                                    this.match(FlinkSqlParser.DOT);
                                    this.state = 2124;
                                    this.identifier();
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    viewPathCreate() {
        let _localctx = new ViewPathCreateContext(this._ctx, this.state);
        this.enterRule(_localctx, 326, FlinkSqlParser.RULE_viewPathCreate);
        let _la;
        try {
            this.state = 2141;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 255, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2129;
                        this.identifier();
                        this.state = 2132;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.DOT) {
                            {
                                this.state = 2130;
                                this.match(FlinkSqlParser.DOT);
                                this.state = 2131;
                                this.identifier();
                            }
                        }
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2134;
                        this.identifier();
                        this.state = 2135;
                        this.match(FlinkSqlParser.DOT);
                        this.state = 2136;
                        this.identifier();
                        this.state = 2139;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.DOT) {
                            {
                                this.state = 2137;
                                this.match(FlinkSqlParser.DOT);
                                this.state = 2138;
                                this.identifier();
                            }
                        }
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    uid() {
        let _localctx = new UidContext(this._ctx, this.state);
        this.enterRule(_localctx, 328, FlinkSqlParser.RULE_uid);
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2143;
                this.identifier();
                this.state = 2148;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 256, this._ctx);
                while (_alt !== 1 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1 + 1) {
                        {
                            {
                                this.state = 2144;
                                this.match(FlinkSqlParser.DOT);
                                this.state = 2145;
                                this.identifier();
                            }
                        }
                    }
                    this.state = 2150;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 256, this._ctx);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    withOption() {
        let _localctx = new WithOptionContext(this._ctx, this.state);
        this.enterRule(_localctx, 330, FlinkSqlParser.RULE_withOption);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2151;
                this.match(FlinkSqlParser.KW_WITH);
                this.state = 2152;
                this.tablePropertyList();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    ifNotExists() {
        let _localctx = new IfNotExistsContext(this._ctx, this.state);
        this.enterRule(_localctx, 332, FlinkSqlParser.RULE_ifNotExists);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2154;
                this.match(FlinkSqlParser.KW_IF);
                this.state = 2155;
                this.match(FlinkSqlParser.KW_NOT);
                this.state = 2156;
                this.match(FlinkSqlParser.KW_EXISTS);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    ifExists() {
        let _localctx = new IfExistsContext(this._ctx, this.state);
        this.enterRule(_localctx, 334, FlinkSqlParser.RULE_ifExists);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2158;
                this.match(FlinkSqlParser.KW_IF);
                this.state = 2159;
                this.match(FlinkSqlParser.KW_EXISTS);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tablePropertyList() {
        let _localctx = new TablePropertyListContext(this._ctx, this.state);
        this.enterRule(_localctx, 336, FlinkSqlParser.RULE_tablePropertyList);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2161;
                this.match(FlinkSqlParser.LR_BRACKET);
                this.state = 2162;
                this.tableProperty();
                this.state = 2167;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === FlinkSqlParser.COMMA) {
                    {
                        {
                            this.state = 2163;
                            this.match(FlinkSqlParser.COMMA);
                            this.state = 2164;
                            this.tableProperty();
                        }
                    }
                    this.state = 2169;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
                this.state = 2170;
                this.match(FlinkSqlParser.RR_BRACKET);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tableProperty() {
        let _localctx = new TablePropertyContext(this._ctx, this.state);
        this.enterRule(_localctx, 338, FlinkSqlParser.RULE_tableProperty);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2172;
                _localctx._key = this.tablePropertyKey();
                this.state = 2177;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === FlinkSqlParser.KW_FALSE || _la === FlinkSqlParser.KW_TRUE || _la === FlinkSqlParser.EQUAL_SYMBOL || ((((_la - 537)) & ~0x1F) === 0 && ((1 << (_la - 537)) & ((1 << (FlinkSqlParser.STRING_LITERAL - 537)) | (1 << (FlinkSqlParser.DIG_LITERAL - 537)) | (1 << (FlinkSqlParser.REAL_LITERAL - 537)))) !== 0)) {
                    {
                        this.state = 2174;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.EQUAL_SYMBOL) {
                            {
                                this.state = 2173;
                                this.match(FlinkSqlParser.EQUAL_SYMBOL);
                            }
                        }
                        this.state = 2176;
                        _localctx._value = this.tablePropertyValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tablePropertyKey() {
        let _localctx = new TablePropertyKeyContext(this._ctx, this.state);
        this.enterRule(_localctx, 340, FlinkSqlParser.RULE_tablePropertyKey);
        try {
            this.state = 2182;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 260, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2179;
                        this.identifier();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2180;
                        this.dereferenceDefinition();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 2181;
                        this.match(FlinkSqlParser.STRING_LITERAL);
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    tablePropertyValue() {
        let _localctx = new TablePropertyValueContext(this._ctx, this.state);
        this.enterRule(_localctx, 342, FlinkSqlParser.RULE_tablePropertyValue);
        try {
            this.state = 2188;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.DIG_LITERAL:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2184;
                        this.match(FlinkSqlParser.DIG_LITERAL);
                    }
                    break;
                case FlinkSqlParser.REAL_LITERAL:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2185;
                        this.match(FlinkSqlParser.REAL_LITERAL);
                    }
                    break;
                case FlinkSqlParser.KW_FALSE:
                case FlinkSqlParser.KW_TRUE:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 2186;
                        this.booleanLiteral();
                    }
                    break;
                case FlinkSqlParser.STRING_LITERAL:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 2187;
                        this.match(FlinkSqlParser.STRING_LITERAL);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    logicalOperator() {
        let _localctx = new LogicalOperatorContext(this._ctx, this.state);
        this.enterRule(_localctx, 344, FlinkSqlParser.RULE_logicalOperator);
        try {
            this.state = 2196;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_AND:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2190;
                        this.match(FlinkSqlParser.KW_AND);
                    }
                    break;
                case FlinkSqlParser.BIT_AND_OP:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2191;
                        this.match(FlinkSqlParser.BIT_AND_OP);
                        this.state = 2192;
                        this.match(FlinkSqlParser.BIT_AND_OP);
                    }
                    break;
                case FlinkSqlParser.KW_OR:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 2193;
                        this.match(FlinkSqlParser.KW_OR);
                    }
                    break;
                case FlinkSqlParser.BIT_OR_OP:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 2194;
                        this.match(FlinkSqlParser.BIT_OR_OP);
                        this.state = 2195;
                        this.match(FlinkSqlParser.BIT_OR_OP);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    comparisonOperator() {
        let _localctx = new ComparisonOperatorContext(this._ctx, this.state);
        this.enterRule(_localctx, 346, FlinkSqlParser.RULE_comparisonOperator);
        try {
            this.state = 2212;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 263, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2198;
                        this.match(FlinkSqlParser.EQUAL_SYMBOL);
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2199;
                        this.match(FlinkSqlParser.GREATER_SYMBOL);
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 2200;
                        this.match(FlinkSqlParser.LESS_SYMBOL);
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 2201;
                        this.match(FlinkSqlParser.LESS_SYMBOL);
                        this.state = 2202;
                        this.match(FlinkSqlParser.EQUAL_SYMBOL);
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 2203;
                        this.match(FlinkSqlParser.GREATER_SYMBOL);
                        this.state = 2204;
                        this.match(FlinkSqlParser.EQUAL_SYMBOL);
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 2205;
                        this.match(FlinkSqlParser.LESS_SYMBOL);
                        this.state = 2206;
                        this.match(FlinkSqlParser.GREATER_SYMBOL);
                    }
                    break;
                case 7:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 2207;
                        this.match(FlinkSqlParser.EXCLAMATION_SYMBOL);
                        this.state = 2208;
                        this.match(FlinkSqlParser.EQUAL_SYMBOL);
                    }
                    break;
                case 8:
                    this.enterOuterAlt(_localctx, 8);
                    {
                        this.state = 2209;
                        this.match(FlinkSqlParser.LESS_SYMBOL);
                        this.state = 2210;
                        this.match(FlinkSqlParser.EQUAL_SYMBOL);
                        this.state = 2211;
                        this.match(FlinkSqlParser.GREATER_SYMBOL);
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    bitOperator() {
        let _localctx = new BitOperatorContext(this._ctx, this.state);
        this.enterRule(_localctx, 348, FlinkSqlParser.RULE_bitOperator);
        try {
            this.state = 2221;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.LESS_SYMBOL:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2214;
                        this.match(FlinkSqlParser.LESS_SYMBOL);
                        this.state = 2215;
                        this.match(FlinkSqlParser.LESS_SYMBOL);
                    }
                    break;
                case FlinkSqlParser.GREATER_SYMBOL:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2216;
                        this.match(FlinkSqlParser.GREATER_SYMBOL);
                        this.state = 2217;
                        this.match(FlinkSqlParser.GREATER_SYMBOL);
                    }
                    break;
                case FlinkSqlParser.BIT_AND_OP:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 2218;
                        this.match(FlinkSqlParser.BIT_AND_OP);
                    }
                    break;
                case FlinkSqlParser.BIT_XOR_OP:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 2219;
                        this.match(FlinkSqlParser.BIT_XOR_OP);
                    }
                    break;
                case FlinkSqlParser.BIT_OR_OP:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 2220;
                        this.match(FlinkSqlParser.BIT_OR_OP);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    mathOperator() {
        let _localctx = new MathOperatorContext(this._ctx, this.state);
        this.enterRule(_localctx, 350, FlinkSqlParser.RULE_mathOperator);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2223;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_DIV || ((((_la - 527)) & ~0x1F) === 0 && ((1 << (_la - 527)) & ((1 << (FlinkSqlParser.ASTERISK_SIGN - 527)) | (1 << (FlinkSqlParser.HYPNEN_SIGN - 527)) | (1 << (FlinkSqlParser.ADD_SIGN - 527)) | (1 << (FlinkSqlParser.PENCENT_SIGN - 527)) | (1 << (FlinkSqlParser.DOUBLE_HYPNEN_SIGN - 527)) | (1 << (FlinkSqlParser.SLASH_SIGN - 527)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    unaryOperator() {
        let _localctx = new UnaryOperatorContext(this._ctx, this.state);
        this.enterRule(_localctx, 352, FlinkSqlParser.RULE_unaryOperator);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2225;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_NOT || ((((_la - 508)) & ~0x1F) === 0 && ((1 << (_la - 508)) & ((1 << (FlinkSqlParser.EXCLAMATION_SYMBOL - 508)) | (1 << (FlinkSqlParser.BIT_NOT_OP - 508)) | (1 << (FlinkSqlParser.HYPNEN_SIGN - 508)) | (1 << (FlinkSqlParser.ADD_SIGN - 508)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    constant() {
        let _localctx = new ConstantContext(this._ctx, this.state);
        this.enterRule(_localctx, 354, FlinkSqlParser.RULE_constant);
        let _la;
        try {
            this.state = 2241;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case FlinkSqlParser.KW_INTERVAL:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 2227;
                        this.timeIntervalExpression();
                    }
                    break;
                case FlinkSqlParser.KW_DAY:
                case FlinkSqlParser.KW_HOUR:
                case FlinkSqlParser.KW_MINUTE:
                case FlinkSqlParser.KW_MONTH:
                case FlinkSqlParser.KW_SECOND:
                case FlinkSqlParser.KW_YEAR:
                case FlinkSqlParser.KW_MICROSECOND:
                case FlinkSqlParser.KW_MILLISECOND:
                case FlinkSqlParser.KW_QUARTER:
                case FlinkSqlParser.KW_WEEK:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 2228;
                        this.timePointLiteral();
                    }
                    break;
                case FlinkSqlParser.STRING_LITERAL:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 2229;
                        this.stringLiteral();
                    }
                    break;
                case FlinkSqlParser.HYPNEN_SIGN:
                case FlinkSqlParser.DIG_LITERAL:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 2231;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.HYPNEN_SIGN) {
                            {
                                this.state = 2230;
                                this.match(FlinkSqlParser.HYPNEN_SIGN);
                            }
                        }
                        this.state = 2233;
                        this.decimalLiteral();
                    }
                    break;
                case FlinkSqlParser.KW_FALSE:
                case FlinkSqlParser.KW_TRUE:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 2234;
                        this.booleanLiteral();
                    }
                    break;
                case FlinkSqlParser.REAL_LITERAL:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 2235;
                        this.match(FlinkSqlParser.REAL_LITERAL);
                    }
                    break;
                case FlinkSqlParser.BIT_STRING:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 2236;
                        this.match(FlinkSqlParser.BIT_STRING);
                    }
                    break;
                case FlinkSqlParser.KW_NOT:
                case FlinkSqlParser.KW_NULL:
                    this.enterOuterAlt(_localctx, 8);
                    {
                        this.state = 2238;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === FlinkSqlParser.KW_NOT) {
                            {
                                this.state = 2237;
                                this.match(FlinkSqlParser.KW_NOT);
                            }
                        }
                        this.state = 2240;
                        this.match(FlinkSqlParser.KW_NULL);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    timePointLiteral() {
        let _localctx = new TimePointLiteralContext(this._ctx, this.state);
        this.enterRule(_localctx, 356, FlinkSqlParser.RULE_timePointLiteral);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2243;
                this.timePointUnit();
                this.state = 2244;
                this.stringLiteral();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    stringLiteral() {
        let _localctx = new StringLiteralContext(this._ctx, this.state);
        this.enterRule(_localctx, 358, FlinkSqlParser.RULE_stringLiteral);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2246;
                this.match(FlinkSqlParser.STRING_LITERAL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    decimalLiteral() {
        let _localctx = new DecimalLiteralContext(this._ctx, this.state);
        this.enterRule(_localctx, 360, FlinkSqlParser.RULE_decimalLiteral);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2248;
                this.match(FlinkSqlParser.DIG_LITERAL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    booleanLiteral() {
        let _localctx = new BooleanLiteralContext(this._ctx, this.state);
        this.enterRule(_localctx, 362, FlinkSqlParser.RULE_booleanLiteral);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2250;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_FALSE || _la === FlinkSqlParser.KW_TRUE)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    setQuantifier() {
        let _localctx = new SetQuantifierContext(this._ctx, this.state);
        this.enterRule(_localctx, 364, FlinkSqlParser.RULE_setQuantifier);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2252;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_ALL || _la === FlinkSqlParser.KW_DISTINCT)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    timePointUnit() {
        let _localctx = new TimePointUnitContext(this._ctx, this.state);
        this.enterRule(_localctx, 366, FlinkSqlParser.RULE_timePointUnit);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2254;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_DAY || _la === FlinkSqlParser.KW_HOUR || _la === FlinkSqlParser.KW_MINUTE || _la === FlinkSqlParser.KW_MONTH || _la === FlinkSqlParser.KW_SECOND || _la === FlinkSqlParser.KW_YEAR || ((((_la - 470)) & ~0x1F) === 0 && ((1 << (_la - 470)) & ((1 << (FlinkSqlParser.KW_MICROSECOND - 470)) | (1 << (FlinkSqlParser.KW_MILLISECOND - 470)) | (1 << (FlinkSqlParser.KW_QUARTER - 470)))) !== 0) || _la === FlinkSqlParser.KW_WEEK)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    timeIntervalUnit() {
        let _localctx = new TimeIntervalUnitContext(this._ctx, this.state);
        this.enterRule(_localctx, 368, FlinkSqlParser.RULE_timeIntervalUnit);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2256;
                _la = this._input.LA(1);
                if (!(_la === FlinkSqlParser.KW_DAY || _la === FlinkSqlParser.KW_HOUR || _la === FlinkSqlParser.KW_MINUTE || _la === FlinkSqlParser.KW_MONTH || _la === FlinkSqlParser.KW_SECOND || ((((_la - 427)) & ~0x1F) === 0 && ((1 << (_la - 427)) & ((1 << (FlinkSqlParser.KW_WEEKS - 427)) | (1 << (FlinkSqlParser.KW_YEAR - 427)) | (1 << (FlinkSqlParser.KW_CENTURY - 427)) | (1 << (FlinkSqlParser.KW_DAYS - 427)) | (1 << (FlinkSqlParser.KW_DECADE - 427)) | (1 << (FlinkSqlParser.KW_EPOCH - 427)))) !== 0) || ((((_la - 460)) & ~0x1F) === 0 && ((1 << (_la - 460)) & ((1 << (FlinkSqlParser.KW_HOURS - 460)) | (1 << (FlinkSqlParser.KW_MICROSECOND - 460)) | (1 << (FlinkSqlParser.KW_MILLENNIUM - 460)) | (1 << (FlinkSqlParser.KW_MILLISECOND - 460)) | (1 << (FlinkSqlParser.KW_MINUTES - 460)) | (1 << (FlinkSqlParser.KW_MONTHS - 460)) | (1 << (FlinkSqlParser.KW_NANOSECOND - 460)) | (1 << (FlinkSqlParser.KW_QUARTER - 460)) | (1 << (FlinkSqlParser.KW_SECONDS - 460)))) !== 0) || _la === FlinkSqlParser.KW_WEEK || _la === FlinkSqlParser.KW_YEARS)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    reservedKeywordsUsedAsFuncParam() {
        let _localctx = new ReservedKeywordsUsedAsFuncParamContext(this._ctx, this.state);
        this.enterRule(_localctx, 370, FlinkSqlParser.RULE_reservedKeywordsUsedAsFuncParam);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2258;
                _la = this._input.LA(1);
                if (!(((((_la - 5)) & ~0x1F) === 0 && ((1 << (_la - 5)) & ((1 << (FlinkSqlParser.KW_ALL - 5)) | (1 << (FlinkSqlParser.KW_ARRAY - 5)) | (1 << (FlinkSqlParser.KW_BOTH - 5)))) !== 0) || _la === FlinkSqlParser.KW_CURRENT_TIMESTAMP || _la === FlinkSqlParser.KW_DISTINCT || _la === FlinkSqlParser.KW_LEADING || _la === FlinkSqlParser.KW_TRAILING || _la === FlinkSqlParser.KW_VALUE || _la === FlinkSqlParser.ASTERISK_SIGN)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    reservedKeywordsUsedAsFuncName() {
        let _localctx = new ReservedKeywordsUsedAsFuncNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 372, FlinkSqlParser.RULE_reservedKeywordsUsedAsFuncName);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2260;
                _la = this._input.LA(1);
                if (!((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << FlinkSqlParser.KW_ABS) | (1 << FlinkSqlParser.KW_ARRAY) | (1 << FlinkSqlParser.KW_AVG))) !== 0) || ((((_la - 38)) & ~0x1F) === 0 && ((1 << (_la - 38)) & ((1 << (FlinkSqlParser.KW_CARDINALITY - 38)) | (1 << (FlinkSqlParser.KW_CAST - 38)) | (1 << (FlinkSqlParser.KW_CEIL - 38)) | (1 << (FlinkSqlParser.KW_CEILING - 38)) | (1 << (FlinkSqlParser.KW_COALESCE - 38)) | (1 << (FlinkSqlParser.KW_COLLECT - 38)) | (1 << (FlinkSqlParser.KW_COUNT - 38)))) !== 0) || ((((_la - 75)) & ~0x1F) === 0 && ((1 << (_la - 75)) & ((1 << (FlinkSqlParser.KW_CUME_DIST - 75)) | (1 << (FlinkSqlParser.KW_CURRENT_DATE - 75)) | (1 << (FlinkSqlParser.KW_CURRENT_TIME - 75)) | (1 << (FlinkSqlParser.KW_CURRENT_TIMESTAMP - 75)) | (1 << (FlinkSqlParser.KW_DATE - 75)) | (1 << (FlinkSqlParser.KW_DAYOFWEEK - 75)) | (1 << (FlinkSqlParser.KW_DAYOFYEAR - 75)))) !== 0) || ((((_la - 107)) & ~0x1F) === 0 && ((1 << (_la - 107)) & ((1 << (FlinkSqlParser.KW_DENSE_RANK - 107)) | (1 << (FlinkSqlParser.KW_ELEMENT - 107)) | (1 << (FlinkSqlParser.KW_EXP - 107)))) !== 0) || ((((_la - 139)) & ~0x1F) === 0 && ((1 << (_la - 139)) & ((1 << (FlinkSqlParser.KW_EXTRACT - 139)) | (1 << (FlinkSqlParser.KW_FIRST_VALUE - 139)) | (1 << (FlinkSqlParser.KW_FLOOR - 139)) | (1 << (FlinkSqlParser.KW_GROUPING - 139)) | (1 << (FlinkSqlParser.KW_HOUR - 139)) | (1 << (FlinkSqlParser.KW_IF - 139)))) !== 0) || ((((_la - 194)) & ~0x1F) === 0 && ((1 << (_la - 194)) & ((1 << (FlinkSqlParser.KW_LAG - 194)) | (1 << (FlinkSqlParser.KW_LAST_VALUE - 194)) | (1 << (FlinkSqlParser.KW_LEAD - 194)) | (1 << (FlinkSqlParser.KW_LEFT - 194)) | (1 << (FlinkSqlParser.KW_LN - 194)) | (1 << (FlinkSqlParser.KW_LOCALTIME - 194)) | (1 << (FlinkSqlParser.KW_LOCALTIMESTAMP - 194)) | (1 << (FlinkSqlParser.KW_LOWER - 194)) | (1 << (FlinkSqlParser.KW_MAX - 194)) | (1 << (FlinkSqlParser.KW_MIN - 194)) | (1 << (FlinkSqlParser.KW_MINUTE - 194)) | (1 << (FlinkSqlParser.KW_MOD - 194)))) !== 0) || ((((_la - 229)) & ~0x1F) === 0 && ((1 << (_la - 229)) & ((1 << (FlinkSqlParser.KW_MONTH - 229)) | (1 << (FlinkSqlParser.KW_NTILE - 229)) | (1 << (FlinkSqlParser.KW_NULLIF - 229)))) !== 0) || ((((_la - 264)) & ~0x1F) === 0 && ((1 << (_la - 264)) & ((1 << (FlinkSqlParser.KW_OVERLAY - 264)) | (1 << (FlinkSqlParser.KW_PERCENT_RANK - 264)) | (1 << (FlinkSqlParser.KW_POSITION - 264)) | (1 << (FlinkSqlParser.KW_POWER - 264)) | (1 << (FlinkSqlParser.KW_RANK - 264)))) !== 0) || ((((_la - 317)) & ~0x1F) === 0 && ((1 << (_la - 317)) & ((1 << (FlinkSqlParser.KW_RIGHT - 317)) | (1 << (FlinkSqlParser.KW_ROW - 317)) | (1 << (FlinkSqlParser.KW_ROWS - 317)) | (1 << (FlinkSqlParser.KW_ROW_NUMBER - 317)) | (1 << (FlinkSqlParser.KW_SECOND - 317)))) !== 0) || ((((_la - 357)) & ~0x1F) === 0 && ((1 << (_la - 357)) & ((1 << (FlinkSqlParser.KW_STDDEV_POP - 357)) | (1 << (FlinkSqlParser.KW_STDDEV_SAMP - 357)) | (1 << (FlinkSqlParser.KW_SUBSTRING - 357)) | (1 << (FlinkSqlParser.KW_SUM - 357)) | (1 << (FlinkSqlParser.KW_TIME - 357)) | (1 << (FlinkSqlParser.KW_TIMESTAMP - 357)) | (1 << (FlinkSqlParser.KW_TIMESTAMP_DIFF - 357)))) !== 0) || ((((_la - 395)) & ~0x1F) === 0 && ((1 << (_la - 395)) & ((1 << (FlinkSqlParser.KW_TRIM - 395)) | (1 << (FlinkSqlParser.KW_TRUNCATE - 395)) | (1 << (FlinkSqlParser.KW_TRY_CAST - 395)) | (1 << (FlinkSqlParser.KW_UPPER - 395)) | (1 << (FlinkSqlParser.KW_VAR_POP - 395)) | (1 << (FlinkSqlParser.KW_VAR_SAMP - 395)))) !== 0) || _la === FlinkSqlParser.KW_YEAR || _la === FlinkSqlParser.KW_MAP || _la === FlinkSqlParser.KW_QUARTER || _la === FlinkSqlParser.KW_WEEK)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    nonReservedKeywords() {
        let _localctx = new NonReservedKeywordsContext(this._ctx, this.state);
        this.enterRule(_localctx, 374, FlinkSqlParser.RULE_nonReservedKeywords);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 2262;
                _la = this._input.LA(1);
                if (!(((((_la - 437)) & ~0x1F) === 0 && ((1 << (_la - 437)) & ((1 << (FlinkSqlParser.KW_ADD - 437)) | (1 << (FlinkSqlParser.KW_AFTER - 437)) | (1 << (FlinkSqlParser.KW_ASC - 437)) | (1 << (FlinkSqlParser.KW_CASCADE - 437)) | (1 << (FlinkSqlParser.KW_CATALOG - 437)) | (1 << (FlinkSqlParser.KW_CENTURY - 437)) | (1 << (FlinkSqlParser.KW_CONFIG - 437)) | (1 << (FlinkSqlParser.KW_CONSTRAINTS - 437)) | (1 << (FlinkSqlParser.KW_CUMULATE - 437)) | (1 << (FlinkSqlParser.KW_DATA - 437)) | (1 << (FlinkSqlParser.KW_DATABASE - 437)) | (1 << (FlinkSqlParser.KW_DAYS - 437)) | (1 << (FlinkSqlParser.KW_DECADE - 437)) | (1 << (FlinkSqlParser.KW_DESC - 437)) | (1 << (FlinkSqlParser.KW_DESCRIPTOR - 437)) | (1 << (FlinkSqlParser.KW_DIV - 437)) | (1 << (FlinkSqlParser.KW_ENGINE - 437)) | (1 << (FlinkSqlParser.KW_EPOCH - 437)) | (1 << (FlinkSqlParser.KW_EXCLUDING - 437)) | (1 << (FlinkSqlParser.KW_FILE - 437)) | (1 << (FlinkSqlParser.KW_FIRST - 437)) | (1 << (FlinkSqlParser.KW_GENERATED - 437)) | (1 << (FlinkSqlParser.KW_HOP - 437)) | (1 << (FlinkSqlParser.KW_HOURS - 437)) | (1 << (FlinkSqlParser.KW_IGNORE - 437)) | (1 << (FlinkSqlParser.KW_INCLUDING - 437)) | (1 << (FlinkSqlParser.KW_JAR - 437)) | (1 << (FlinkSqlParser.KW_JARS - 437)) | (1 << (FlinkSqlParser.KW_JAVA - 437)) | (1 << (FlinkSqlParser.KW_KEY - 437)) | (1 << (FlinkSqlParser.KW_LAST - 437)) | (1 << (FlinkSqlParser.KW_LOAD - 437)))) !== 0) || ((((_la - 469)) & ~0x1F) === 0 && ((1 << (_la - 469)) & ((1 << (FlinkSqlParser.KW_MAP - 469)) | (1 << (FlinkSqlParser.KW_MICROSECOND - 469)) | (1 << (FlinkSqlParser.KW_MILLENNIUM - 469)) | (1 << (FlinkSqlParser.KW_MILLISECOND - 469)) | (1 << (FlinkSqlParser.KW_MINUTES - 469)) | (1 << (FlinkSqlParser.KW_MONTHS - 469)) | (1 << (FlinkSqlParser.KW_NANOSECOND - 469)) | (1 << (FlinkSqlParser.KW_NULLS - 469)) | (1 << (FlinkSqlParser.KW_OPTIONS - 469)) | (1 << (FlinkSqlParser.KW_PAST - 469)) | (1 << (FlinkSqlParser.KW_PLAN - 469)) | (1 << (FlinkSqlParser.KW_PRECEDING - 469)) | (1 << (FlinkSqlParser.KW_PYTHON - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_ARCHIVES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_DEPENDENCIES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_FILES - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_JAR - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_PARAMETER - 469)) | (1 << (FlinkSqlParser.KW_PYTHON_REQUIREMENTS - 469)) | (1 << (FlinkSqlParser.KW_QUARTER - 469)) | (1 << (FlinkSqlParser.KW_REMOVE - 469)) | (1 << (FlinkSqlParser.KW_RESTRICT - 469)) | (1 << (FlinkSqlParser.KW_SECONDS - 469)) | (1 << (FlinkSqlParser.KW_SESSION - 469)) | (1 << (FlinkSqlParser.KW_SETS - 469)) | (1 << (FlinkSqlParser.KW_SIZE - 469)) | (1 << (FlinkSqlParser.KW_SLIDE - 469)) | (1 << (FlinkSqlParser.KW_STEP - 469)) | (1 << (FlinkSqlParser.KW_TEMPORARY - 469)) | (1 << (FlinkSqlParser.KW_TIMECOL - 469)) | (1 << (FlinkSqlParser.KW_TUMBLE - 469)) | (1 << (FlinkSqlParser.KW_UNLOAD - 469)))) !== 0) || ((((_la - 501)) & ~0x1F) === 0 && ((1 << (_la - 501)) & ((1 << (FlinkSqlParser.KW_VIEW - 501)) | (1 << (FlinkSqlParser.KW_WEEK - 501)) | (1 << (FlinkSqlParser.KW_YEARS - 501)) | (1 << (FlinkSqlParser.KW_ZONE - 501)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    sempred(_localctx, ruleIndex, predIndex) {
        switch (ruleIndex) {
            case 79:
                return this.queryStatement_sempred(_localctx, predIndex);
            case 89:
                return this.tableExpression_sempred(_localctx, predIndex);
            case 130:
                return this.booleanExpression_sempred(_localctx, predIndex);
            case 133:
                return this.valueExpression_sempred(_localctx, predIndex);
            case 134:
                return this.primaryExpression_sempred(_localctx, predIndex);
        }
        return true;
    }
    queryStatement_sempred(_localctx, predIndex) {
        switch (predIndex) {
            case 0:
                return this.precpred(this._ctx, 3);
        }
        return true;
    }
    tableExpression_sempred(_localctx, predIndex) {
        switch (predIndex) {
            case 1:
                return this.precpred(this._ctx, 3);
            case 2:
                return this.precpred(this._ctx, 4);
        }
        return true;
    }
    booleanExpression_sempred(_localctx, predIndex) {
        switch (predIndex) {
            case 3:
                return this.precpred(this._ctx, 3);
            case 4:
                return this.precpred(this._ctx, 2);
            case 5:
                return this.precpred(this._ctx, 1);
        }
        return true;
    }
    valueExpression_sempred(_localctx, predIndex) {
        switch (predIndex) {
            case 6:
                return this.precpred(this._ctx, 6);
            case 7:
                return this.precpred(this._ctx, 5);
            case 8:
                return this.precpred(this._ctx, 4);
            case 9:
                return this.precpred(this._ctx, 3);
            case 10:
                return this.precpred(this._ctx, 2);
            case 11:
                return this.precpred(this._ctx, 1);
        }
        return true;
    }
    primaryExpression_sempred(_localctx, predIndex) {
        switch (predIndex) {
            case 12:
                return this.precpred(this._ctx, 5);
        }
        return true;
    }
    static get _ATN() {
        if (!FlinkSqlParser.__ATN) {
            FlinkSqlParser.__ATN = new ATNDeserializer().deserialize(Utils.toCharArray(FlinkSqlParser._serializedATN));
        }
        return FlinkSqlParser.__ATN;
    }
}
FlinkSqlParser.SPACE = 1;
FlinkSqlParser.COMMENT_INPUT = 2;
FlinkSqlParser.LINE_COMMENT = 3;
FlinkSqlParser.KW_ABS = 4;
FlinkSqlParser.KW_ALL = 5;
FlinkSqlParser.KW_ALLOCATE = 6;
FlinkSqlParser.KW_ALLOW = 7;
FlinkSqlParser.KW_ALTER = 8;
FlinkSqlParser.KW_ANALYZE = 9;
FlinkSqlParser.KW_AND = 10;
FlinkSqlParser.KW_ANY = 11;
FlinkSqlParser.KW_ARE = 12;
FlinkSqlParser.KW_ARRAY = 13;
FlinkSqlParser.KW_ARRAY_AGG = 14;
FlinkSqlParser.KW_ARRAY_CONCAT_AGG = 15;
FlinkSqlParser.KW_ARRAY_MAX_CARDINALITY = 16;
FlinkSqlParser.KW_AS = 17;
FlinkSqlParser.KW_ASENSITIVE = 18;
FlinkSqlParser.KW_ASYMMETRIC = 19;
FlinkSqlParser.KW_AT = 20;
FlinkSqlParser.KW_ATOMIC = 21;
FlinkSqlParser.KW_AUTHORIZATION = 22;
FlinkSqlParser.KW_AVG = 23;
FlinkSqlParser.KW_BEGIN = 24;
FlinkSqlParser.KW_BEGIN_FRAME = 25;
FlinkSqlParser.KW_BEGIN_PARTITION = 26;
FlinkSqlParser.KW_BETWEEN = 27;
FlinkSqlParser.KW_BIGINT = 28;
FlinkSqlParser.KW_BINARY = 29;
FlinkSqlParser.KW_BIT = 30;
FlinkSqlParser.KW_BLOB = 31;
FlinkSqlParser.KW_BOOLEAN = 32;
FlinkSqlParser.KW_BOTH = 33;
FlinkSqlParser.KW_BY = 34;
FlinkSqlParser.KW_BYTES = 35;
FlinkSqlParser.KW_CALL = 36;
FlinkSqlParser.KW_CALLED = 37;
FlinkSqlParser.KW_CARDINALITY = 38;
FlinkSqlParser.KW_CASCADED = 39;
FlinkSqlParser.KW_CASE = 40;
FlinkSqlParser.KW_CAST = 41;
FlinkSqlParser.KW_CATALOGS = 42;
FlinkSqlParser.KW_CEIL = 43;
FlinkSqlParser.KW_CEILING = 44;
FlinkSqlParser.KW_CHANGELOG_MODE = 45;
FlinkSqlParser.KW_CHAR = 46;
FlinkSqlParser.KW_CHARACTER = 47;
FlinkSqlParser.KW_CHARACTER_LENGTH = 48;
FlinkSqlParser.KW_CHAR_LENGTH = 49;
FlinkSqlParser.KW_CHECK = 50;
FlinkSqlParser.KW_CLASSIFIER = 51;
FlinkSqlParser.KW_CLOB = 52;
FlinkSqlParser.KW_CLOSE = 53;
FlinkSqlParser.KW_COALESCE = 54;
FlinkSqlParser.KW_COLLATE = 55;
FlinkSqlParser.KW_COLLECT = 56;
FlinkSqlParser.KW_COLUMN = 57;
FlinkSqlParser.KW_COLUMNS = 58;
FlinkSqlParser.KW_COMMENT = 59;
FlinkSqlParser.KW_COMMIT = 60;
FlinkSqlParser.KW_COMPUTE = 61;
FlinkSqlParser.KW_CONDITION = 62;
FlinkSqlParser.KW_CONNECT = 63;
FlinkSqlParser.KW_CONSTRAINT = 64;
FlinkSqlParser.KW_CONTAINS = 65;
FlinkSqlParser.KW_CONVERT = 66;
FlinkSqlParser.KW_CORR = 67;
FlinkSqlParser.KW_CORRESPONDING = 68;
FlinkSqlParser.KW_COUNT = 69;
FlinkSqlParser.KW_COVAR_POP = 70;
FlinkSqlParser.KW_COVAR_SAMP = 71;
FlinkSqlParser.KW_CREATE = 72;
FlinkSqlParser.KW_CROSS = 73;
FlinkSqlParser.KW_CUBE = 74;
FlinkSqlParser.KW_CUME_DIST = 75;
FlinkSqlParser.KW_CURRENT = 76;
FlinkSqlParser.KW_CURRENT_CATALOG = 77;
FlinkSqlParser.KW_CURRENT_DATE = 78;
FlinkSqlParser.KW_CURRENT_DEFAULT_TRANSFORM_GROUP = 79;
FlinkSqlParser.KW_CURRENT_PATH = 80;
FlinkSqlParser.KW_CURRENT_ROLE = 81;
FlinkSqlParser.KW_CURRENT_ROW = 82;
FlinkSqlParser.KW_CURRENT_SCHEMA = 83;
FlinkSqlParser.KW_CURRENT_TIME = 84;
FlinkSqlParser.KW_CURRENT_TIMESTAMP = 85;
FlinkSqlParser.KW_CURRENT_TRANSFORM_GROUP_FOR_TYPE = 86;
FlinkSqlParser.KW_CURRENT_USER = 87;
FlinkSqlParser.KW_CURSOR = 88;
FlinkSqlParser.KW_CYCLE = 89;
FlinkSqlParser.KW_DATABASES = 90;
FlinkSqlParser.KW_DATE = 91;
FlinkSqlParser.KW_DATETIME = 92;
FlinkSqlParser.KW_DATETIME_DIFF = 93;
FlinkSqlParser.KW_DATETIME_TRUNC = 94;
FlinkSqlParser.KW_DATE_DIFF = 95;
FlinkSqlParser.KW_DATE_TRUNC = 96;
FlinkSqlParser.KW_DAY = 97;
FlinkSqlParser.KW_DAYOFWEEK = 98;
FlinkSqlParser.KW_DAYOFYEAR = 99;
FlinkSqlParser.KW_DEALLOCATE = 100;
FlinkSqlParser.KW_DEC = 101;
FlinkSqlParser.KW_DECIMAL = 102;
FlinkSqlParser.KW_DECLARE = 103;
FlinkSqlParser.KW_DEFAULT = 104;
FlinkSqlParser.KW_DEFINE = 105;
FlinkSqlParser.KW_DELETE = 106;
FlinkSqlParser.KW_DENSE_RANK = 107;
FlinkSqlParser.KW_DEREF = 108;
FlinkSqlParser.KW_DESCRIBE = 109;
FlinkSqlParser.KW_DETERMINISTIC = 110;
FlinkSqlParser.KW_DISALLOW = 111;
FlinkSqlParser.KW_DISCONNECT = 112;
FlinkSqlParser.KW_DISTINCT = 113;
FlinkSqlParser.KW_DOT = 114;
FlinkSqlParser.KW_DOUBLE = 115;
FlinkSqlParser.KW_DROP = 116;
FlinkSqlParser.KW_DYNAMIC = 117;
FlinkSqlParser.KW_EACH = 118;
FlinkSqlParser.KW_ELEMENT = 119;
FlinkSqlParser.KW_ELSE = 120;
FlinkSqlParser.KW_EMPTY = 121;
FlinkSqlParser.KW_END = 122;
FlinkSqlParser.KW_END_FRAME = 123;
FlinkSqlParser.KW_END_PARTITION = 124;
FlinkSqlParser.KW_ENFORCED = 125;
FlinkSqlParser.KW_EQUALS = 126;
FlinkSqlParser.KW_ESCAPE = 127;
FlinkSqlParser.KW_ESTIMATED_COST = 128;
FlinkSqlParser.KW_EVERY = 129;
FlinkSqlParser.KW_EXCEPT = 130;
FlinkSqlParser.KW_EXEC = 131;
FlinkSqlParser.KW_EXECUTE = 132;
FlinkSqlParser.KW_EXISTS = 133;
FlinkSqlParser.KW_EXP = 134;
FlinkSqlParser.KW_EXPLAIN = 135;
FlinkSqlParser.KW_EXTEND = 136;
FlinkSqlParser.KW_EXTENDED = 137;
FlinkSqlParser.KW_EXTERNAL = 138;
FlinkSqlParser.KW_EXTRACT = 139;
FlinkSqlParser.KW_FALSE = 140;
FlinkSqlParser.KW_FETCH = 141;
FlinkSqlParser.KW_FILTER = 142;
FlinkSqlParser.KW_FIRST_VALUE = 143;
FlinkSqlParser.KW_FLOAT = 144;
FlinkSqlParser.KW_FLOOR = 145;
FlinkSqlParser.KW_FOR = 146;
FlinkSqlParser.KW_FOREIGN = 147;
FlinkSqlParser.KW_FRAME_ROW = 148;
FlinkSqlParser.KW_FREE = 149;
FlinkSqlParser.KW_FRIDAY = 150;
FlinkSqlParser.KW_FROM = 151;
FlinkSqlParser.KW_FULL = 152;
FlinkSqlParser.KW_FUNCTION = 153;
FlinkSqlParser.KW_FUNCTIONS = 154;
FlinkSqlParser.KW_FUSION = 155;
FlinkSqlParser.KW_GET = 156;
FlinkSqlParser.KW_GLOBAL = 157;
FlinkSqlParser.KW_GRANT = 158;
FlinkSqlParser.KW_GROUP = 159;
FlinkSqlParser.KW_GROUPING = 160;
FlinkSqlParser.KW_GROUPS = 161;
FlinkSqlParser.KW_GROUP_CONCAT = 162;
FlinkSqlParser.KW_HAVING = 163;
FlinkSqlParser.KW_HOLD = 164;
FlinkSqlParser.KW_HOUR = 165;
FlinkSqlParser.KW_IDENTITY = 166;
FlinkSqlParser.KW_IF = 167;
FlinkSqlParser.KW_ILIKE = 168;
FlinkSqlParser.KW_IMPORT = 169;
FlinkSqlParser.KW_IN = 170;
FlinkSqlParser.KW_INCLUDE = 171;
FlinkSqlParser.KW_INDICATOR = 172;
FlinkSqlParser.KW_INITIAL = 173;
FlinkSqlParser.KW_INNER = 174;
FlinkSqlParser.KW_INOUT = 175;
FlinkSqlParser.KW_INSENSITIVE = 176;
FlinkSqlParser.KW_INSERT = 177;
FlinkSqlParser.KW_INT = 178;
FlinkSqlParser.KW_INTEGER = 179;
FlinkSqlParser.KW_INTERSECT = 180;
FlinkSqlParser.KW_INTERSECTION = 181;
FlinkSqlParser.KW_INTERVAL = 182;
FlinkSqlParser.KW_INTO = 183;
FlinkSqlParser.KW_IS = 184;
FlinkSqlParser.KW_JOIN = 185;
FlinkSqlParser.KW_JSON_ARRAY = 186;
FlinkSqlParser.KW_JSON_ARRAYAGG = 187;
FlinkSqlParser.KW_JSON_EXECUTION_PLAN = 188;
FlinkSqlParser.KW_JSON_EXISTS = 189;
FlinkSqlParser.KW_JSON_OBJECT = 190;
FlinkSqlParser.KW_JSON_OBJECTAGG = 191;
FlinkSqlParser.KW_JSON_QUERY = 192;
FlinkSqlParser.KW_JSON_VALUE = 193;
FlinkSqlParser.KW_LAG = 194;
FlinkSqlParser.KW_LANGUAGE = 195;
FlinkSqlParser.KW_LARGE = 196;
FlinkSqlParser.KW_LAST_VALUE = 197;
FlinkSqlParser.KW_LATERAL = 198;
FlinkSqlParser.KW_LEAD = 199;
FlinkSqlParser.KW_LEADING = 200;
FlinkSqlParser.KW_LEFT = 201;
FlinkSqlParser.KW_LIKE = 202;
FlinkSqlParser.KW_LIKE_REGEX = 203;
FlinkSqlParser.KW_LIMIT = 204;
FlinkSqlParser.KW_LN = 205;
FlinkSqlParser.KW_LOCAL = 206;
FlinkSqlParser.KW_LOCALTIME = 207;
FlinkSqlParser.KW_LOCALTIMESTAMP = 208;
FlinkSqlParser.KW_LOWER = 209;
FlinkSqlParser.KW_MATCH = 210;
FlinkSqlParser.KW_MATCHES = 211;
FlinkSqlParser.KW_MATCH_NUMBER = 212;
FlinkSqlParser.KW_MATCH_RECOGNIZE = 213;
FlinkSqlParser.KW_MAX = 214;
FlinkSqlParser.KW_MEASURES = 215;
FlinkSqlParser.KW_MEMBER = 216;
FlinkSqlParser.KW_MERGE = 217;
FlinkSqlParser.KW_METADATA = 218;
FlinkSqlParser.KW_METHOD = 219;
FlinkSqlParser.KW_MIN = 220;
FlinkSqlParser.KW_MINUS = 221;
FlinkSqlParser.KW_MINUTE = 222;
FlinkSqlParser.KW_MOD = 223;
FlinkSqlParser.KW_MODIFIES = 224;
FlinkSqlParser.KW_MODIFY = 225;
FlinkSqlParser.KW_MODULE = 226;
FlinkSqlParser.KW_MODULES = 227;
FlinkSqlParser.KW_MONDAY = 228;
FlinkSqlParser.KW_MONTH = 229;
FlinkSqlParser.KW_MORE = 230;
FlinkSqlParser.KW_MULTISET = 231;
FlinkSqlParser.KW_NATIONAL = 232;
FlinkSqlParser.KW_NATURAL = 233;
FlinkSqlParser.KW_NCHAR = 234;
FlinkSqlParser.KW_NCLOB = 235;
FlinkSqlParser.KW_NEW = 236;
FlinkSqlParser.KW_NEXT = 237;
FlinkSqlParser.KW_NO = 238;
FlinkSqlParser.KW_NONE = 239;
FlinkSqlParser.KW_NORMALIZE = 240;
FlinkSqlParser.KW_NOT = 241;
FlinkSqlParser.KW_NTH_VALUE = 242;
FlinkSqlParser.KW_NTILE = 243;
FlinkSqlParser.KW_NULL = 244;
FlinkSqlParser.KW_NULLIF = 245;
FlinkSqlParser.KW_NUMERIC = 246;
FlinkSqlParser.KW_OCCURRENCES_REGEX = 247;
FlinkSqlParser.KW_OCTET_LENGTH = 248;
FlinkSqlParser.KW_OF = 249;
FlinkSqlParser.KW_OFFSET = 250;
FlinkSqlParser.KW_OLD = 251;
FlinkSqlParser.KW_OMIT = 252;
FlinkSqlParser.KW_ON = 253;
FlinkSqlParser.KW_ONE = 254;
FlinkSqlParser.KW_ONLY = 255;
FlinkSqlParser.KW_OPEN = 256;
FlinkSqlParser.KW_OR = 257;
FlinkSqlParser.KW_ORDER = 258;
FlinkSqlParser.KW_ORDINAL = 259;
FlinkSqlParser.KW_OUT = 260;
FlinkSqlParser.KW_OUTER = 261;
FlinkSqlParser.KW_OVER = 262;
FlinkSqlParser.KW_OVERLAPS = 263;
FlinkSqlParser.KW_OVERLAY = 264;
FlinkSqlParser.KW_OVERWRITE = 265;
FlinkSqlParser.KW_OVERWRITING = 266;
FlinkSqlParser.KW_PARAMETER = 267;
FlinkSqlParser.KW_PARTITION = 268;
FlinkSqlParser.KW_PARTITIONED = 269;
FlinkSqlParser.KW_PARTITIONS = 270;
FlinkSqlParser.KW_PATTERN = 271;
FlinkSqlParser.KW_PER = 272;
FlinkSqlParser.KW_PERCENT = 273;
FlinkSqlParser.KW_PERCENTILE_CONT = 274;
FlinkSqlParser.KW_PERCENTILE_DISC = 275;
FlinkSqlParser.KW_PERCENT_RANK = 276;
FlinkSqlParser.KW_PERIOD = 277;
FlinkSqlParser.KW_PERMUTE = 278;
FlinkSqlParser.KW_PIVOT = 279;
FlinkSqlParser.KW_PORTION = 280;
FlinkSqlParser.KW_POSITION = 281;
FlinkSqlParser.KW_POSITION_REGEX = 282;
FlinkSqlParser.KW_POWER = 283;
FlinkSqlParser.KW_PRECEDES = 284;
FlinkSqlParser.KW_PRECISION = 285;
FlinkSqlParser.KW_PREPARE = 286;
FlinkSqlParser.KW_PREV = 287;
FlinkSqlParser.KW_PRIMARY = 288;
FlinkSqlParser.KW_PROCEDURE = 289;
FlinkSqlParser.KW_QUALIFY = 290;
FlinkSqlParser.KW_QUARTERS = 291;
FlinkSqlParser.KW_RANGE = 292;
FlinkSqlParser.KW_RANK = 293;
FlinkSqlParser.KW_RAW = 294;
FlinkSqlParser.KW_READS = 295;
FlinkSqlParser.KW_REAL = 296;
FlinkSqlParser.KW_RECURSIVE = 297;
FlinkSqlParser.KW_REF = 298;
FlinkSqlParser.KW_REFERENCES = 299;
FlinkSqlParser.KW_REFERENCING = 300;
FlinkSqlParser.KW_REGR_AVGX = 301;
FlinkSqlParser.KW_REGR_AVGY = 302;
FlinkSqlParser.KW_REGR_COUNT = 303;
FlinkSqlParser.KW_REGR_INTERCEPT = 304;
FlinkSqlParser.KW_REGR_R2 = 305;
FlinkSqlParser.KW_REGR_SLOPE = 306;
FlinkSqlParser.KW_REGR_SXX = 307;
FlinkSqlParser.KW_REGR_SXY = 308;
FlinkSqlParser.KW_REGR_SYY = 309;
FlinkSqlParser.KW_RELEASE = 310;
FlinkSqlParser.KW_RENAME = 311;
FlinkSqlParser.KW_RESET = 312;
FlinkSqlParser.KW_RESULT = 313;
FlinkSqlParser.KW_RETURN = 314;
FlinkSqlParser.KW_RETURNS = 315;
FlinkSqlParser.KW_REVOKE = 316;
FlinkSqlParser.KW_RIGHT = 317;
FlinkSqlParser.KW_RLIKE = 318;
FlinkSqlParser.KW_ROLLBACK = 319;
FlinkSqlParser.KW_ROLLUP = 320;
FlinkSqlParser.KW_ROW = 321;
FlinkSqlParser.KW_ROWS = 322;
FlinkSqlParser.KW_ROW_NUMBER = 323;
FlinkSqlParser.KW_RUNNING = 324;
FlinkSqlParser.KW_SAFE_CAST = 325;
FlinkSqlParser.KW_SAFE_OFFSET = 326;
FlinkSqlParser.KW_SAFE_ORDINAL = 327;
FlinkSqlParser.KW_SATURDAY = 328;
FlinkSqlParser.KW_SAVEPOINT = 329;
FlinkSqlParser.KW_SCALA = 330;
FlinkSqlParser.KW_SCOPE = 331;
FlinkSqlParser.KW_SCROLL = 332;
FlinkSqlParser.KW_SEARCH = 333;
FlinkSqlParser.KW_SECOND = 334;
FlinkSqlParser.KW_SEEK = 335;
FlinkSqlParser.KW_SELECT = 336;
FlinkSqlParser.KW_SENSITIVE = 337;
FlinkSqlParser.KW_SEPARATOR = 338;
FlinkSqlParser.KW_SESSION_USER = 339;
FlinkSqlParser.KW_SET = 340;
FlinkSqlParser.KW_SHOW = 341;
FlinkSqlParser.KW_SIMILAR = 342;
FlinkSqlParser.KW_SKIP = 343;
FlinkSqlParser.KW_SMALLINT = 344;
FlinkSqlParser.KW_SOME = 345;
FlinkSqlParser.KW_SPECIFIC = 346;
FlinkSqlParser.KW_SPECIFICTYPE = 347;
FlinkSqlParser.KW_SQL = 348;
FlinkSqlParser.KW_SQLEXCEPTION = 349;
FlinkSqlParser.KW_SQLSTATE = 350;
FlinkSqlParser.KW_SQLWARNING = 351;
FlinkSqlParser.KW_SQRT = 352;
FlinkSqlParser.KW_START = 353;
FlinkSqlParser.KW_STATEMENT = 354;
FlinkSqlParser.KW_STATIC = 355;
FlinkSqlParser.KW_STATISTICS = 356;
FlinkSqlParser.KW_STDDEV_POP = 357;
FlinkSqlParser.KW_STDDEV_SAMP = 358;
FlinkSqlParser.KW_STREAM = 359;
FlinkSqlParser.KW_STRING = 360;
FlinkSqlParser.KW_STRING_AGG = 361;
FlinkSqlParser.KW_SUBMULTISET = 362;
FlinkSqlParser.KW_SUBSET = 363;
FlinkSqlParser.KW_SUBSTRING = 364;
FlinkSqlParser.KW_SUBSTRING_REGEX = 365;
FlinkSqlParser.KW_SUCCEEDS = 366;
FlinkSqlParser.KW_SUM = 367;
FlinkSqlParser.KW_SUNDAY = 368;
FlinkSqlParser.KW_SYMMETRIC = 369;
FlinkSqlParser.KW_SYSTEM = 370;
FlinkSqlParser.KW_SYSTEM_TIME = 371;
FlinkSqlParser.KW_SYSTEM_USER = 372;
FlinkSqlParser.KW_TABLE = 373;
FlinkSqlParser.KW_TABLES = 374;
FlinkSqlParser.KW_TABLESAMPLE = 375;
FlinkSqlParser.KW_THEN = 376;
FlinkSqlParser.KW_THURSDAY = 377;
FlinkSqlParser.KW_TIME = 378;
FlinkSqlParser.KW_TIMESTAMP = 379;
FlinkSqlParser.KW_TIMESTAMP_DIFF = 380;
FlinkSqlParser.KW_TIMESTAMP_LTZ = 381;
FlinkSqlParser.KW_TIMESTAMP_TRUNC = 382;
FlinkSqlParser.KW_TIMEZONE_HOUR = 383;
FlinkSqlParser.KW_TIMEZONE_MINUTE = 384;
FlinkSqlParser.KW_TIME_DIFF = 385;
FlinkSqlParser.KW_TIME_TRUNC = 386;
FlinkSqlParser.KW_TINYINT = 387;
FlinkSqlParser.KW_TO = 388;
FlinkSqlParser.KW_TRAILING = 389;
FlinkSqlParser.KW_TRANSLATE = 390;
FlinkSqlParser.KW_TRANSLATE_REGEX = 391;
FlinkSqlParser.KW_TRANSLATION = 392;
FlinkSqlParser.KW_TREAT = 393;
FlinkSqlParser.KW_TRIGGER = 394;
FlinkSqlParser.KW_TRIM = 395;
FlinkSqlParser.KW_TRIM_ARRAY = 396;
FlinkSqlParser.KW_TRUE = 397;
FlinkSqlParser.KW_TRUNCATE = 398;
FlinkSqlParser.KW_TRY_CAST = 399;
FlinkSqlParser.KW_TUESDAY = 400;
FlinkSqlParser.KW_UESCAPE = 401;
FlinkSqlParser.KW_UNION = 402;
FlinkSqlParser.KW_UNIQUE = 403;
FlinkSqlParser.KW_UNKNOWN = 404;
FlinkSqlParser.KW_UNNEST = 405;
FlinkSqlParser.KW_UNPIVOT = 406;
FlinkSqlParser.KW_UPDATE = 407;
FlinkSqlParser.KW_UPPER = 408;
FlinkSqlParser.KW_UPSERT = 409;
FlinkSqlParser.KW_USE = 410;
FlinkSqlParser.KW_USER = 411;
FlinkSqlParser.KW_USING = 412;
FlinkSqlParser.KW_VALUE = 413;
FlinkSqlParser.KW_VALUES = 414;
FlinkSqlParser.KW_VALUE_OF = 415;
FlinkSqlParser.KW_VARBINARY = 416;
FlinkSqlParser.KW_VARCHAR = 417;
FlinkSqlParser.KW_VARYING = 418;
FlinkSqlParser.KW_VAR_POP = 419;
FlinkSqlParser.KW_VAR_SAMP = 420;
FlinkSqlParser.KW_VERSIONING = 421;
FlinkSqlParser.KW_VIEWS = 422;
FlinkSqlParser.KW_VIRTUAL = 423;
FlinkSqlParser.KW_WATERMARK = 424;
FlinkSqlParser.KW_WATERMARKS = 425;
FlinkSqlParser.KW_WEDNESDAY = 426;
FlinkSqlParser.KW_WEEKS = 427;
FlinkSqlParser.KW_WHEN = 428;
FlinkSqlParser.KW_WHENEVER = 429;
FlinkSqlParser.KW_WHERE = 430;
FlinkSqlParser.KW_WIDTH_BUCKET = 431;
FlinkSqlParser.KW_WINDOW = 432;
FlinkSqlParser.KW_WITH = 433;
FlinkSqlParser.KW_WITHIN = 434;
FlinkSqlParser.KW_WITHOUT = 435;
FlinkSqlParser.KW_YEAR = 436;
FlinkSqlParser.KW_ADD = 437;
FlinkSqlParser.KW_AFTER = 438;
FlinkSqlParser.KW_ASC = 439;
FlinkSqlParser.KW_CASCADE = 440;
FlinkSqlParser.KW_CATALOG = 441;
FlinkSqlParser.KW_CENTURY = 442;
FlinkSqlParser.KW_CONFIG = 443;
FlinkSqlParser.KW_CONSTRAINTS = 444;
FlinkSqlParser.KW_CUMULATE = 445;
FlinkSqlParser.KW_DATA = 446;
FlinkSqlParser.KW_DATABASE = 447;
FlinkSqlParser.KW_DAYS = 448;
FlinkSqlParser.KW_DECADE = 449;
FlinkSqlParser.KW_DESC = 450;
FlinkSqlParser.KW_DESCRIPTOR = 451;
FlinkSqlParser.KW_DIV = 452;
FlinkSqlParser.KW_ENGINE = 453;
FlinkSqlParser.KW_EPOCH = 454;
FlinkSqlParser.KW_EXCLUDING = 455;
FlinkSqlParser.KW_FILE = 456;
FlinkSqlParser.KW_FIRST = 457;
FlinkSqlParser.KW_GENERATED = 458;
FlinkSqlParser.KW_HOP = 459;
FlinkSqlParser.KW_HOURS = 460;
FlinkSqlParser.KW_IGNORE = 461;
FlinkSqlParser.KW_INCLUDING = 462;
FlinkSqlParser.KW_JAR = 463;
FlinkSqlParser.KW_JARS = 464;
FlinkSqlParser.KW_JAVA = 465;
FlinkSqlParser.KW_KEY = 466;
FlinkSqlParser.KW_LAST = 467;
FlinkSqlParser.KW_LOAD = 468;
FlinkSqlParser.KW_MAP = 469;
FlinkSqlParser.KW_MICROSECOND = 470;
FlinkSqlParser.KW_MILLENNIUM = 471;
FlinkSqlParser.KW_MILLISECOND = 472;
FlinkSqlParser.KW_MINUTES = 473;
FlinkSqlParser.KW_MONTHS = 474;
FlinkSqlParser.KW_NANOSECOND = 475;
FlinkSqlParser.KW_NULLS = 476;
FlinkSqlParser.KW_OPTIONS = 477;
FlinkSqlParser.KW_PAST = 478;
FlinkSqlParser.KW_PLAN = 479;
FlinkSqlParser.KW_PRECEDING = 480;
FlinkSqlParser.KW_PYTHON = 481;
FlinkSqlParser.KW_PYTHON_ARCHIVES = 482;
FlinkSqlParser.KW_PYTHON_DEPENDENCIES = 483;
FlinkSqlParser.KW_PYTHON_FILES = 484;
FlinkSqlParser.KW_PYTHON_JAR = 485;
FlinkSqlParser.KW_PYTHON_PARAMETER = 486;
FlinkSqlParser.KW_PYTHON_REQUIREMENTS = 487;
FlinkSqlParser.KW_QUARTER = 488;
FlinkSqlParser.KW_REMOVE = 489;
FlinkSqlParser.KW_RESTRICT = 490;
FlinkSqlParser.KW_SECONDS = 491;
FlinkSqlParser.KW_SESSION = 492;
FlinkSqlParser.KW_SETS = 493;
FlinkSqlParser.KW_SIZE = 494;
FlinkSqlParser.KW_SLIDE = 495;
FlinkSqlParser.KW_STEP = 496;
FlinkSqlParser.KW_TEMPORARY = 497;
FlinkSqlParser.KW_TIMECOL = 498;
FlinkSqlParser.KW_TUMBLE = 499;
FlinkSqlParser.KW_UNLOAD = 500;
FlinkSqlParser.KW_VIEW = 501;
FlinkSqlParser.KW_WEEK = 502;
FlinkSqlParser.KW_YEARS = 503;
FlinkSqlParser.KW_ZONE = 504;
FlinkSqlParser.EQUAL_SYMBOL = 505;
FlinkSqlParser.GREATER_SYMBOL = 506;
FlinkSqlParser.LESS_SYMBOL = 507;
FlinkSqlParser.EXCLAMATION_SYMBOL = 508;
FlinkSqlParser.BIT_NOT_OP = 509;
FlinkSqlParser.BIT_OR_OP = 510;
FlinkSqlParser.BIT_AND_OP = 511;
FlinkSqlParser.BIT_XOR_OP = 512;
FlinkSqlParser.DOT = 513;
FlinkSqlParser.LS_BRACKET = 514;
FlinkSqlParser.RS_BRACKET = 515;
FlinkSqlParser.LR_BRACKET = 516;
FlinkSqlParser.RR_BRACKET = 517;
FlinkSqlParser.LB_BRACKET = 518;
FlinkSqlParser.RB_BRACKET = 519;
FlinkSqlParser.COMMA = 520;
FlinkSqlParser.SEMICOLON = 521;
FlinkSqlParser.AT_SIGN = 522;
FlinkSqlParser.SINGLE_QUOTE_SYMB = 523;
FlinkSqlParser.DOUBLE_QUOTE_SYMB = 524;
FlinkSqlParser.REVERSE_QUOTE_SYMB = 525;
FlinkSqlParser.COLON_SYMB = 526;
FlinkSqlParser.ASTERISK_SIGN = 527;
FlinkSqlParser.UNDERLINE_SIGN = 528;
FlinkSqlParser.HYPNEN_SIGN = 529;
FlinkSqlParser.ADD_SIGN = 530;
FlinkSqlParser.PENCENT_SIGN = 531;
FlinkSqlParser.DOUBLE_VERTICAL_SIGN = 532;
FlinkSqlParser.DOUBLE_HYPNEN_SIGN = 533;
FlinkSqlParser.SLASH_SIGN = 534;
FlinkSqlParser.QUESTION_MARK_SIGN = 535;
FlinkSqlParser.DOUBLE_RIGHT_ARROW = 536;
FlinkSqlParser.STRING_LITERAL = 537;
FlinkSqlParser.DIG_LITERAL = 538;
FlinkSqlParser.REAL_LITERAL = 539;
FlinkSqlParser.BIT_STRING = 540;
FlinkSqlParser.ID_LITERAL = 541;
FlinkSqlParser.RULE_program = 0;
FlinkSqlParser.RULE_singleStatement = 1;
FlinkSqlParser.RULE_sqlStatement = 2;
FlinkSqlParser.RULE_emptyStatement = 3;
FlinkSqlParser.RULE_ddlStatement = 4;
FlinkSqlParser.RULE_dmlStatement = 5;
FlinkSqlParser.RULE_describeStatement = 6;
FlinkSqlParser.RULE_explainStatement = 7;
FlinkSqlParser.RULE_explainDetails = 8;
FlinkSqlParser.RULE_explainDetail = 9;
FlinkSqlParser.RULE_useStatement = 10;
FlinkSqlParser.RULE_useModuleStatement = 11;
FlinkSqlParser.RULE_showStatememt = 12;
FlinkSqlParser.RULE_loadStatement = 13;
FlinkSqlParser.RULE_unloadStatememt = 14;
FlinkSqlParser.RULE_setStatememt = 15;
FlinkSqlParser.RULE_resetStatememt = 16;
FlinkSqlParser.RULE_jarStatememt = 17;
FlinkSqlParser.RULE_dtAddStatement = 18;
FlinkSqlParser.RULE_dtFilePath = 19;
FlinkSqlParser.RULE_createTable = 20;
FlinkSqlParser.RULE_simpleCreateTable = 21;
FlinkSqlParser.RULE_createTableAsSelect = 22;
FlinkSqlParser.RULE_columnOptionDefinition = 23;
FlinkSqlParser.RULE_physicalColumnDefinition = 24;
FlinkSqlParser.RULE_columnNameCreate = 25;
FlinkSqlParser.RULE_columnName = 26;
FlinkSqlParser.RULE_columnNameList = 27;
FlinkSqlParser.RULE_columnType = 28;
FlinkSqlParser.RULE_lengthOneDimension = 29;
FlinkSqlParser.RULE_lengthTwoOptionalDimension = 30;
FlinkSqlParser.RULE_lengthTwoStringDimension = 31;
FlinkSqlParser.RULE_lengthOneTypeDimension = 32;
FlinkSqlParser.RULE_mapTypeDimension = 33;
FlinkSqlParser.RULE_rowTypeDimension = 34;
FlinkSqlParser.RULE_columnConstraint = 35;
FlinkSqlParser.RULE_commentSpec = 36;
FlinkSqlParser.RULE_metadataColumnDefinition = 37;
FlinkSqlParser.RULE_metadataKey = 38;
FlinkSqlParser.RULE_computedColumnDefinition = 39;
FlinkSqlParser.RULE_computedColumnExpression = 40;
FlinkSqlParser.RULE_watermarkDefinition = 41;
FlinkSqlParser.RULE_tableConstraint = 42;
FlinkSqlParser.RULE_constraintName = 43;
FlinkSqlParser.RULE_selfDefinitionClause = 44;
FlinkSqlParser.RULE_partitionDefinition = 45;
FlinkSqlParser.RULE_transformList = 46;
FlinkSqlParser.RULE_transform = 47;
FlinkSqlParser.RULE_transformArgument = 48;
FlinkSqlParser.RULE_likeDefinition = 49;
FlinkSqlParser.RULE_likeOption = 50;
FlinkSqlParser.RULE_createCatalog = 51;
FlinkSqlParser.RULE_createDatabase = 52;
FlinkSqlParser.RULE_createView = 53;
FlinkSqlParser.RULE_createFunction = 54;
FlinkSqlParser.RULE_usingClause = 55;
FlinkSqlParser.RULE_jarFileName = 56;
FlinkSqlParser.RULE_alterTable = 57;
FlinkSqlParser.RULE_renameDefinition = 58;
FlinkSqlParser.RULE_setKeyValueDefinition = 59;
FlinkSqlParser.RULE_addConstraint = 60;
FlinkSqlParser.RULE_dropConstraint = 61;
FlinkSqlParser.RULE_addUnique = 62;
FlinkSqlParser.RULE_notForced = 63;
FlinkSqlParser.RULE_alertView = 64;
FlinkSqlParser.RULE_alterDatabase = 65;
FlinkSqlParser.RULE_alterFunction = 66;
FlinkSqlParser.RULE_dropCatalog = 67;
FlinkSqlParser.RULE_dropTable = 68;
FlinkSqlParser.RULE_dropDatabase = 69;
FlinkSqlParser.RULE_dropView = 70;
FlinkSqlParser.RULE_dropFunction = 71;
FlinkSqlParser.RULE_insertStatement = 72;
FlinkSqlParser.RULE_insertSimpleStatement = 73;
FlinkSqlParser.RULE_insertPartitionDefinition = 74;
FlinkSqlParser.RULE_valuesDefinition = 75;
FlinkSqlParser.RULE_valuesRowDefinition = 76;
FlinkSqlParser.RULE_insertMulStatementCompatibility = 77;
FlinkSqlParser.RULE_insertMulStatement = 78;
FlinkSqlParser.RULE_queryStatement = 79;
FlinkSqlParser.RULE_valuesCaluse = 80;
FlinkSqlParser.RULE_withClause = 81;
FlinkSqlParser.RULE_withItem = 82;
FlinkSqlParser.RULE_withItemName = 83;
FlinkSqlParser.RULE_selectStatement = 84;
FlinkSqlParser.RULE_selectClause = 85;
FlinkSqlParser.RULE_projectItemDefinition = 86;
FlinkSqlParser.RULE_overWindowItem = 87;
FlinkSqlParser.RULE_fromClause = 88;
FlinkSqlParser.RULE_tableExpression = 89;
FlinkSqlParser.RULE_tableReference = 90;
FlinkSqlParser.RULE_tablePrimary = 91;
FlinkSqlParser.RULE_systemTimePeriod = 92;
FlinkSqlParser.RULE_dateTimeExpression = 93;
FlinkSqlParser.RULE_inlineDataValueClause = 94;
FlinkSqlParser.RULE_windoTVFClause = 95;
FlinkSqlParser.RULE_windowTVFExression = 96;
FlinkSqlParser.RULE_windoTVFName = 97;
FlinkSqlParser.RULE_windowTVFParam = 98;
FlinkSqlParser.RULE_timeIntervalParamName = 99;
FlinkSqlParser.RULE_columnDescriptor = 100;
FlinkSqlParser.RULE_joinCondition = 101;
FlinkSqlParser.RULE_whereClause = 102;
FlinkSqlParser.RULE_groupByClause = 103;
FlinkSqlParser.RULE_groupItemDefinition = 104;
FlinkSqlParser.RULE_groupingSets = 105;
FlinkSqlParser.RULE_groupingSetsNotaionName = 106;
FlinkSqlParser.RULE_groupWindowFunction = 107;
FlinkSqlParser.RULE_groupWindowFunctionName = 108;
FlinkSqlParser.RULE_timeAttrColumn = 109;
FlinkSqlParser.RULE_havingClause = 110;
FlinkSqlParser.RULE_windowClause = 111;
FlinkSqlParser.RULE_namedWindow = 112;
FlinkSqlParser.RULE_windowSpec = 113;
FlinkSqlParser.RULE_matchRecognizeClause = 114;
FlinkSqlParser.RULE_orderByCaluse = 115;
FlinkSqlParser.RULE_orderItemDefition = 116;
FlinkSqlParser.RULE_limitClause = 117;
FlinkSqlParser.RULE_partitionByClause = 118;
FlinkSqlParser.RULE_quantifiers = 119;
FlinkSqlParser.RULE_measuresClause = 120;
FlinkSqlParser.RULE_patternDefination = 121;
FlinkSqlParser.RULE_patternVariable = 122;
FlinkSqlParser.RULE_outputMode = 123;
FlinkSqlParser.RULE_afterMatchStrategy = 124;
FlinkSqlParser.RULE_patternVariablesDefination = 125;
FlinkSqlParser.RULE_windowFrame = 126;
FlinkSqlParser.RULE_frameBound = 127;
FlinkSqlParser.RULE_withinClause = 128;
FlinkSqlParser.RULE_expression = 129;
FlinkSqlParser.RULE_booleanExpression = 130;
FlinkSqlParser.RULE_predicate = 131;
FlinkSqlParser.RULE_likePredicate = 132;
FlinkSqlParser.RULE_valueExpression = 133;
FlinkSqlParser.RULE_primaryExpression = 134;
FlinkSqlParser.RULE_functionNameCreate = 135;
FlinkSqlParser.RULE_functionName = 136;
FlinkSqlParser.RULE_functionParam = 137;
FlinkSqlParser.RULE_dereferenceDefinition = 138;
FlinkSqlParser.RULE_correlationName = 139;
FlinkSqlParser.RULE_qualifiedName = 140;
FlinkSqlParser.RULE_timeIntervalExpression = 141;
FlinkSqlParser.RULE_errorCapturingMultiUnitsInterval = 142;
FlinkSqlParser.RULE_multiUnitsInterval = 143;
FlinkSqlParser.RULE_errorCapturingUnitToUnitInterval = 144;
FlinkSqlParser.RULE_unitToUnitInterval = 145;
FlinkSqlParser.RULE_intervalValue = 146;
FlinkSqlParser.RULE_tableAlias = 147;
FlinkSqlParser.RULE_errorCapturingIdentifier = 148;
FlinkSqlParser.RULE_errorCapturingIdentifierExtra = 149;
FlinkSqlParser.RULE_identifierList = 150;
FlinkSqlParser.RULE_identifierSeq = 151;
FlinkSqlParser.RULE_identifier = 152;
FlinkSqlParser.RULE_unquotedIdentifier = 153;
FlinkSqlParser.RULE_quotedIdentifier = 154;
FlinkSqlParser.RULE_whenClause = 155;
FlinkSqlParser.RULE_catalogPath = 156;
FlinkSqlParser.RULE_catalogPathCreate = 157;
FlinkSqlParser.RULE_databasePath = 158;
FlinkSqlParser.RULE_databasePathCreate = 159;
FlinkSqlParser.RULE_tablePathCreate = 160;
FlinkSqlParser.RULE_tablePath = 161;
FlinkSqlParser.RULE_viewPath = 162;
FlinkSqlParser.RULE_viewPathCreate = 163;
FlinkSqlParser.RULE_uid = 164;
FlinkSqlParser.RULE_withOption = 165;
FlinkSqlParser.RULE_ifNotExists = 166;
FlinkSqlParser.RULE_ifExists = 167;
FlinkSqlParser.RULE_tablePropertyList = 168;
FlinkSqlParser.RULE_tableProperty = 169;
FlinkSqlParser.RULE_tablePropertyKey = 170;
FlinkSqlParser.RULE_tablePropertyValue = 171;
FlinkSqlParser.RULE_logicalOperator = 172;
FlinkSqlParser.RULE_comparisonOperator = 173;
FlinkSqlParser.RULE_bitOperator = 174;
FlinkSqlParser.RULE_mathOperator = 175;
FlinkSqlParser.RULE_unaryOperator = 176;
FlinkSqlParser.RULE_constant = 177;
FlinkSqlParser.RULE_timePointLiteral = 178;
FlinkSqlParser.RULE_stringLiteral = 179;
FlinkSqlParser.RULE_decimalLiteral = 180;
FlinkSqlParser.RULE_booleanLiteral = 181;
FlinkSqlParser.RULE_setQuantifier = 182;
FlinkSqlParser.RULE_timePointUnit = 183;
FlinkSqlParser.RULE_timeIntervalUnit = 184;
FlinkSqlParser.RULE_reservedKeywordsUsedAsFuncParam = 185;
FlinkSqlParser.RULE_reservedKeywordsUsedAsFuncName = 186;
FlinkSqlParser.RULE_nonReservedKeywords = 187;
// tslint:disable:no-trailing-whitespace
FlinkSqlParser.ruleNames = [
    "program", "singleStatement", "sqlStatement", "emptyStatement", "ddlStatement",
    "dmlStatement", "describeStatement", "explainStatement", "explainDetails",
    "explainDetail", "useStatement", "useModuleStatement", "showStatememt",
    "loadStatement", "unloadStatememt", "setStatememt", "resetStatememt",
    "jarStatememt", "dtAddStatement", "dtFilePath", "createTable", "simpleCreateTable",
    "createTableAsSelect", "columnOptionDefinition", "physicalColumnDefinition",
    "columnNameCreate", "columnName", "columnNameList", "columnType", "lengthOneDimension",
    "lengthTwoOptionalDimension", "lengthTwoStringDimension", "lengthOneTypeDimension",
    "mapTypeDimension", "rowTypeDimension", "columnConstraint", "commentSpec",
    "metadataColumnDefinition", "metadataKey", "computedColumnDefinition",
    "computedColumnExpression", "watermarkDefinition", "tableConstraint",
    "constraintName", "selfDefinitionClause", "partitionDefinition", "transformList",
    "transform", "transformArgument", "likeDefinition", "likeOption", "createCatalog",
    "createDatabase", "createView", "createFunction", "usingClause", "jarFileName",
    "alterTable", "renameDefinition", "setKeyValueDefinition", "addConstraint",
    "dropConstraint", "addUnique", "notForced", "alertView", "alterDatabase",
    "alterFunction", "dropCatalog", "dropTable", "dropDatabase", "dropView",
    "dropFunction", "insertStatement", "insertSimpleStatement", "insertPartitionDefinition",
    "valuesDefinition", "valuesRowDefinition", "insertMulStatementCompatibility",
    "insertMulStatement", "queryStatement", "valuesCaluse", "withClause",
    "withItem", "withItemName", "selectStatement", "selectClause", "projectItemDefinition",
    "overWindowItem", "fromClause", "tableExpression", "tableReference", "tablePrimary",
    "systemTimePeriod", "dateTimeExpression", "inlineDataValueClause", "windoTVFClause",
    "windowTVFExression", "windoTVFName", "windowTVFParam", "timeIntervalParamName",
    "columnDescriptor", "joinCondition", "whereClause", "groupByClause", "groupItemDefinition",
    "groupingSets", "groupingSetsNotaionName", "groupWindowFunction", "groupWindowFunctionName",
    "timeAttrColumn", "havingClause", "windowClause", "namedWindow", "windowSpec",
    "matchRecognizeClause", "orderByCaluse", "orderItemDefition", "limitClause",
    "partitionByClause", "quantifiers", "measuresClause", "patternDefination",
    "patternVariable", "outputMode", "afterMatchStrategy", "patternVariablesDefination",
    "windowFrame", "frameBound", "withinClause", "expression", "booleanExpression",
    "predicate", "likePredicate", "valueExpression", "primaryExpression",
    "functionNameCreate", "functionName", "functionParam", "dereferenceDefinition",
    "correlationName", "qualifiedName", "timeIntervalExpression", "errorCapturingMultiUnitsInterval",
    "multiUnitsInterval", "errorCapturingUnitToUnitInterval", "unitToUnitInterval",
    "intervalValue", "tableAlias", "errorCapturingIdentifier", "errorCapturingIdentifierExtra",
    "identifierList", "identifierSeq", "identifier", "unquotedIdentifier",
    "quotedIdentifier", "whenClause", "catalogPath", "catalogPathCreate",
    "databasePath", "databasePathCreate", "tablePathCreate", "tablePath",
    "viewPath", "viewPathCreate", "uid", "withOption", "ifNotExists", "ifExists",
    "tablePropertyList", "tableProperty", "tablePropertyKey", "tablePropertyValue",
    "logicalOperator", "comparisonOperator", "bitOperator", "mathOperator",
    "unaryOperator", "constant", "timePointLiteral", "stringLiteral", "decimalLiteral",
    "booleanLiteral", "setQuantifier", "timePointUnit", "timeIntervalUnit",
    "reservedKeywordsUsedAsFuncParam", "reservedKeywordsUsedAsFuncName", "nonReservedKeywords",
];
FlinkSqlParser._LITERAL_NAMES = [
    undefined, undefined, undefined, undefined, "'ABS'", "'ALL'", "'ALLOCATE'",
    "'ALLOW'", "'ALTER'", "'ANALYZE'", "'AND'", "'ANY'", "'ARE'", "'ARRAY'",
    "'ARRAY_AGG'", "'ARRAY_CONCAT_AGG'", "'ARRAY_MAX_CARDINALITY'", "'AS'",
    "'ASENSITIVE'", "'ASYMMETRIC'", "'AT'", "'ATOMIC'", "'AUTHORIZATION'",
    "'AVG'", "'BEGIN'", "'BEGIN_FRAME'", "'BEGIN_PARTITION'", "'BETWEEN'",
    "'BIGINT'", "'BINARY'", "'BIT'", "'BLOB'", "'BOOLEAN'", "'BOTH'", "'BY'",
    "'BYTES'", "'CALL'", "'CALLED'", "'CARDINALITY'", "'CASCADED'", "'CASE'",
    "'CAST'", "'CATALOGS'", "'CEIL'", "'CEILING'", "'CHANGELOG_MODE'", "'CHAR'",
    "'CHARACTER'", "'CHARACTER_LENGTH'", "'CHAR_LENGTH'", "'CHECK'", "'CLASSIFIER'",
    "'CLOB'", "'CLOSE'", "'COALESCE'", "'COLLATE'", "'COLLECT'", "'COLUMN'",
    "'COLUMNS'", "'COMMENT'", "'COMMIT'", "'COMPUTE'", "'CONDITION'", "'CONNECT'",
    "'CONSTRAINT'", "'CONTAINS'", "'CONVERT'", "'CORR'", "'CORRESPONDING'",
    "'COUNT'", "'COVAR_POP'", "'COVAR_SAMP'", "'CREATE'", "'CROSS'", "'CUBE'",
    "'CUME_DIST'", "'CURRENT'", "'CURRENT_CATALOG'", "'CURRENT_DATE'", "'CURRENT_DEFAULT_TRANSFORM_GROUP'",
    "'CURRENT_PATH'", "'CURRENT_ROLE'", "'CURRENT_ROW'", "'CURRENT_SCHEMA'",
    "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'CURRENT_TRANSFORM_GROUP_FOR_TYPE'",
    "'CURRENT_USER'", "'CURSOR'", "'CYCLE'", "'DATABASES'", "'DATE'", "'DATETIME'",
    "'DATETIME_DIFF'", "'DATETIME_TRUNC'", "'DATE_DIFF'", "'DATE_TRUNC'",
    "'DAY'", "'DAYOFWEEK'", "'DAYOFYEAR'", "'DEALLOCATE'", "'DEC'", "'DECIMAL'",
    "'DECLARE'", "'DEFAULT'", "'DEFINE'", "'DELETE'", "'DENSE_RANK'", "'DEREF'",
    "'DESCRIBE'", "'DETERMINISTIC'", "'DISALLOW'", "'DISCONNECT'", "'DISTINCT'",
    "'DOT'", "'DOUBLE'", "'DROP'", "'DYNAMIC'", "'EACH'", "'ELEMENT'", "'ELSE'",
    "'EMPTY'", "'END'", "'END_FRAME'", "'END_PARTITION'", "'ENFORCED'", "'EQUALS'",
    "'ESCAPE'", "'ESTIMATED_COST'", "'EVERY'", "'EXCEPT'", "'EXEC'", "'EXECUTE'",
    "'EXISTS'", "'EXP'", "'EXPLAIN'", "'EXTEND'", "'EXTENDED'", "'EXTERNAL'",
    "'EXTRACT'", "'FALSE'", "'FETCH'", "'FILTER'", "'FIRST_VALUE'", "'FLOAT'",
    "'FLOOR'", "'FOR'", "'FOREIGN'", "'FRAME_ROW'", "'FREE'", "'FRIDAY'",
    "'FROM'", "'FULL'", "'FUNCTION'", "'FUNCTIONS'", "'FUSION'", "'GET'",
    "'GLOBAL'", "'GRANT'", "'GROUP'", "'GROUPING'", "'GROUPS'", "'GROUP_CONCAT'",
    "'HAVING'", "'HOLD'", "'HOUR'", "'IDENTITY'", "'IF'", "'ILIKE'", "'IMPORT'",
    "'IN'", "'INCLUDE'", "'INDICATOR'", "'INITIAL'", "'INNER'", "'INOUT'",
    "'INSENSITIVE'", "'INSERT'", "'INT'", "'INTEGER'", "'INTERSECT'", "'INTERSECTION'",
    "'INTERVAL'", "'INTO'", "'IS'", "'JOIN'", "'JSON_ARRAY'", "'JSON_ARRAYAGG'",
    "'JSON_EXECUTION_PLAN'", "'JSON_EXISTS'", "'JSON_OBJECT'", "'JSON_OBJECTAGG'",
    "'JSON_QUERY'", "'JSON_VALUE'", "'LAG'", "'LANGUAGE'", "'LARGE'", "'LAST_VALUE'",
    "'LATERAL'", "'LEAD'", "'LEADING'", "'LEFT'", "'LIKE'", "'LIKE_REGEX'",
    "'LIMIT'", "'LN'", "'LOCAL'", "'LOCALTIME'", "'LOCALTIMESTAMP'", "'LOWER'",
    "'MATCH'", "'MATCHES'", "'MATCH_NUMBER'", "'MATCH_RECOGNIZE'", "'MAX'",
    "'MEASURES'", "'MEMBER'", "'MERGE'", "'METADATA'", "'METHOD'", "'MIN'",
    "'MINUS'", "'MINUTE'", "'MOD'", "'MODIFIES'", "'MODIFY'", "'MODULE'",
    "'MODULES'", "'MONDAY'", "'MONTH'", "'MORE'", "'MULTISET'", "'NATIONAL'",
    "'NATURAL'", "'NCHAR'", "'NCLOB'", "'NEW'", "'NEXT'", "'NO'", "'NONE'",
    "'NORMALIZE'", "'NOT'", "'NTH_VALUE'", "'NTILE'", "'NULL'", "'NULLIF'",
    "'NUMERIC'", "'OCCURRENCES_REGEX'", "'OCTET_LENGTH'", "'OF'", "'OFFSET'",
    "'OLD'", "'OMIT'", "'ON'", "'ONE'", "'ONLY'", "'OPEN'", "'OR'", "'ORDER'",
    "'ORDINAL'", "'OUT'", "'OUTER'", "'OVER'", "'OVERLAPS'", "'OVERLAY'",
    "'OVERWRITE'", "'OVERWRITING'", "'PARAMETER'", "'PARTITION'", "'PARTITIONED'",
    "'PARTITIONS'", "'PATTERN'", "'PER'", "'PERCENT'", "'PERCENTILE_CONT'",
    "'PERCENTILE_DISC'", "'PERCENT_RANK'", "'PERIOD'", "'PERMUTE'", "'PIVOT'",
    "'PORTION'", "'POSITION'", "'POSITION_REGEX'", "'POWER'", "'PRECEDES'",
    "'PRECISION'", "'PREPARE'", "'PREV'", "'PRIMARY'", "'PROCEDURE'", "'QUALIFY'",
    "'QUARTERS'", "'RANGE'", "'RANK'", "'RAW'", "'READS'", "'REAL'", "'RECURSIVE'",
    "'REF'", "'REFERENCES'", "'REFERENCING'", "'REGR_AVGX'", "'REGR_AVGY'",
    "'REGR_COUNT'", "'REGR_INTERCEPT'", "'REGR_R2'", "'REGR_SLOPE'", "'REGR_SXX'",
    "'REGR_SXY'", "'REGR_SYY'", "'RELEASE'", "'RENAME'", "'RESET'", "'RESULT'",
    "'RETURN'", "'RETURNS'", "'REVOKE'", "'RIGHT'", "'RLIKE'", "'ROLLBACK'",
    "'ROLLUP'", "'ROW'", "'ROWS'", "'ROW_NUMBER'", "'RUNNING'", "'SAFE_CAST'",
    "'SAFE_OFFSET'", "'SAFE_ORDINAL'", "'SATURDAY'", "'SAVEPOINT'", "'SCALA'",
    "'SCOPE'", "'SCROLL'", "'SEARCH'", "'SECOND'", "'SEEK'", "'SELECT'", "'SENSITIVE'",
    "'SEPARATOR'", "'SESSION_USER'", "'SET'", "'SHOW'", "'SIMILAR'", "'SKIP'",
    "'SMALLINT'", "'SOME'", "'SPECIFIC'", "'SPECIFICTYPE'", "'SQL'", "'SQLEXCEPTION'",
    "'SQLSTATE'", "'SQLWARNING'", "'SQRT'", "'START'", "'STATEMENT'", "'STATIC'",
    "'STATISTICS'", "'STDDEV_POP'", "'STDDEV_SAMP'", "'STREAM'", "'STRING'",
    "'STRING_AGG'", "'SUBMULTISET'", "'SUBSET'", "'SUBSTRING'", "'SUBSTRING_REGEX'",
    "'SUCCEEDS'", "'SUM'", "'SUNDAY'", "'SYMMETRIC'", "'SYSTEM'", "'SYSTEM_TIME'",
    "'SYSTEM_USER'", "'TABLE'", "'TABLES'", "'TABLESAMPLE'", "'THEN'", "'THURSDAY'",
    "'TIME'", "'TIMESTAMP'", "'TIMESTAMP_DIFF'", "'TIMESTAMP_LTZ'", "'TIMESTAMP_TRUNC'",
    "'TIMEZONE_HOUR'", "'TIMEZONE_MINUTE'", "'TIME_DIFF'", "'TIME_TRUNC'",
    "'TINYINT'", "'TO'", "'TRAILING'", "'TRANSLATE'", "'TRANSLATE_REGEX'",
    "'TRANSLATION'", "'TREAT'", "'TRIGGER'", "'TRIM'", "'TRIM_ARRAY'", "'TRUE'",
    "'TRUNCATE'", "'TRY_CAST'", "'TUESDAY'", "'UESCAPE'", "'UNION'", "'UNIQUE'",
    "'UNKNOWN'", "'UNNEST'", "'UNPIVOT'", "'UPDATE'", "'UPPER'", "'UPSERT'",
    "'USE'", "'USER'", "'USING'", "'VALUE'", "'VALUES'", "'VALUE_OF'", "'VARBINARY'",
    "'VARCHAR'", "'VARYING'", "'VAR_POP'", "'VAR_SAMP'", "'VERSIONING'", "'VIEWS'",
    "'VIRTUAL'", "'WATERMARK'", "'WATERMARKS'", "'WEDNESDAY'", "'WEEKS'",
    "'WHEN'", "'WHENEVER'", "'WHERE'", "'WIDTH_BUCKET'", "'WINDOW'", "'WITH'",
    "'WITHIN'", "'WITHOUT'", "'YEAR'", "'ADD'", "'AFTER'", "'ASC'", "'CASCADE'",
    "'CATALOG'", "'CENTURY'", "'CONFIG'", "'CONSTRAINTS'", "'CUMULATE'", "'DATA'",
    "'DATABASE'", "'DAYS'", "'DECADE'", "'DESC'", "'DESCRIPTOR'", "'DIV'",
    "'ENGINE'", "'EPOCH'", "'EXCLUDING'", "'FILE'", "'FIRST'", "'GENERATED'",
    "'HOP'", "'HOURS'", "'IGNORE'", "'INCLUDING'", "'JAR'", "'JARS'", "'JAVA'",
    "'KEY'", "'LAST'", "'LOAD'", "'MAP'", "'MICROSECOND'", "'MILLENNIUM'",
    "'MILLISECOND'", "'MINUTES'", "'MONTHS'", "'NANOSECOND'", "'NULLS'", "'OPTIONS'",
    "'PAST'", "'PLAN'", "'PRECEDING'", "'PYTHON'", "'PYTHON_ARCHIVES'", "'PYTHON_DEPENDENCIES'",
    "'PYTHON_FILES'", "'PYTHON_JAR'", "'PYTHON_PARAMETER'", "'PYTHON_REQUIREMENTS'",
    "'QUARTER'", "'REMOVE'", "'RESTRICT'", "'SECONDS'", "'SESSION'", "'SETS'",
    "'SIZE'", "'SLIDE'", "'STEP'", "'TEMPORARY'", "'TIMECOL'", "'TUMBLE'",
    "'UNLOAD'", "'VIEW'", "'WEEK'", "'YEARS'", "'ZONE'", "'='", "'>'", "'<'",
    "'!'", "'~'", "'|'", "'&'", "'^'", "'.'", "'['", "']'", "'('", "')'",
    "'{'", "'}'", "','", "';'", "'@'", "'''", "'\"'", "'`'", "':'", "'*'",
    "'_'", "'-'", "'+'", "'%'", "'||'", "'--'", "'/'", "'?'", "'=>'",
];
FlinkSqlParser._SYMBOLIC_NAMES = [
    undefined, "SPACE", "COMMENT_INPUT", "LINE_COMMENT", "KW_ABS", "KW_ALL",
    "KW_ALLOCATE", "KW_ALLOW", "KW_ALTER", "KW_ANALYZE", "KW_AND", "KW_ANY",
    "KW_ARE", "KW_ARRAY", "KW_ARRAY_AGG", "KW_ARRAY_CONCAT_AGG", "KW_ARRAY_MAX_CARDINALITY",
    "KW_AS", "KW_ASENSITIVE", "KW_ASYMMETRIC", "KW_AT", "KW_ATOMIC", "KW_AUTHORIZATION",
    "KW_AVG", "KW_BEGIN", "KW_BEGIN_FRAME", "KW_BEGIN_PARTITION", "KW_BETWEEN",
    "KW_BIGINT", "KW_BINARY", "KW_BIT", "KW_BLOB", "KW_BOOLEAN", "KW_BOTH",
    "KW_BY", "KW_BYTES", "KW_CALL", "KW_CALLED", "KW_CARDINALITY", "KW_CASCADED",
    "KW_CASE", "KW_CAST", "KW_CATALOGS", "KW_CEIL", "KW_CEILING", "KW_CHANGELOG_MODE",
    "KW_CHAR", "KW_CHARACTER", "KW_CHARACTER_LENGTH", "KW_CHAR_LENGTH", "KW_CHECK",
    "KW_CLASSIFIER", "KW_CLOB", "KW_CLOSE", "KW_COALESCE", "KW_COLLATE", "KW_COLLECT",
    "KW_COLUMN", "KW_COLUMNS", "KW_COMMENT", "KW_COMMIT", "KW_COMPUTE", "KW_CONDITION",
    "KW_CONNECT", "KW_CONSTRAINT", "KW_CONTAINS", "KW_CONVERT", "KW_CORR",
    "KW_CORRESPONDING", "KW_COUNT", "KW_COVAR_POP", "KW_COVAR_SAMP", "KW_CREATE",
    "KW_CROSS", "KW_CUBE", "KW_CUME_DIST", "KW_CURRENT", "KW_CURRENT_CATALOG",
    "KW_CURRENT_DATE", "KW_CURRENT_DEFAULT_TRANSFORM_GROUP", "KW_CURRENT_PATH",
    "KW_CURRENT_ROLE", "KW_CURRENT_ROW", "KW_CURRENT_SCHEMA", "KW_CURRENT_TIME",
    "KW_CURRENT_TIMESTAMP", "KW_CURRENT_TRANSFORM_GROUP_FOR_TYPE", "KW_CURRENT_USER",
    "KW_CURSOR", "KW_CYCLE", "KW_DATABASES", "KW_DATE", "KW_DATETIME", "KW_DATETIME_DIFF",
    "KW_DATETIME_TRUNC", "KW_DATE_DIFF", "KW_DATE_TRUNC", "KW_DAY", "KW_DAYOFWEEK",
    "KW_DAYOFYEAR", "KW_DEALLOCATE", "KW_DEC", "KW_DECIMAL", "KW_DECLARE",
    "KW_DEFAULT", "KW_DEFINE", "KW_DELETE", "KW_DENSE_RANK", "KW_DEREF", "KW_DESCRIBE",
    "KW_DETERMINISTIC", "KW_DISALLOW", "KW_DISCONNECT", "KW_DISTINCT", "KW_DOT",
    "KW_DOUBLE", "KW_DROP", "KW_DYNAMIC", "KW_EACH", "KW_ELEMENT", "KW_ELSE",
    "KW_EMPTY", "KW_END", "KW_END_FRAME", "KW_END_PARTITION", "KW_ENFORCED",
    "KW_EQUALS", "KW_ESCAPE", "KW_ESTIMATED_COST", "KW_EVERY", "KW_EXCEPT",
    "KW_EXEC", "KW_EXECUTE", "KW_EXISTS", "KW_EXP", "KW_EXPLAIN", "KW_EXTEND",
    "KW_EXTENDED", "KW_EXTERNAL", "KW_EXTRACT", "KW_FALSE", "KW_FETCH", "KW_FILTER",
    "KW_FIRST_VALUE", "KW_FLOAT", "KW_FLOOR", "KW_FOR", "KW_FOREIGN", "KW_FRAME_ROW",
    "KW_FREE", "KW_FRIDAY", "KW_FROM", "KW_FULL", "KW_FUNCTION", "KW_FUNCTIONS",
    "KW_FUSION", "KW_GET", "KW_GLOBAL", "KW_GRANT", "KW_GROUP", "KW_GROUPING",
    "KW_GROUPS", "KW_GROUP_CONCAT", "KW_HAVING", "KW_HOLD", "KW_HOUR", "KW_IDENTITY",
    "KW_IF", "KW_ILIKE", "KW_IMPORT", "KW_IN", "KW_INCLUDE", "KW_INDICATOR",
    "KW_INITIAL", "KW_INNER", "KW_INOUT", "KW_INSENSITIVE", "KW_INSERT", "KW_INT",
    "KW_INTEGER", "KW_INTERSECT", "KW_INTERSECTION", "KW_INTERVAL", "KW_INTO",
    "KW_IS", "KW_JOIN", "KW_JSON_ARRAY", "KW_JSON_ARRAYAGG", "KW_JSON_EXECUTION_PLAN",
    "KW_JSON_EXISTS", "KW_JSON_OBJECT", "KW_JSON_OBJECTAGG", "KW_JSON_QUERY",
    "KW_JSON_VALUE", "KW_LAG", "KW_LANGUAGE", "KW_LARGE", "KW_LAST_VALUE",
    "KW_LATERAL", "KW_LEAD", "KW_LEADING", "KW_LEFT", "KW_LIKE", "KW_LIKE_REGEX",
    "KW_LIMIT", "KW_LN", "KW_LOCAL", "KW_LOCALTIME", "KW_LOCALTIMESTAMP",
    "KW_LOWER", "KW_MATCH", "KW_MATCHES", "KW_MATCH_NUMBER", "KW_MATCH_RECOGNIZE",
    "KW_MAX", "KW_MEASURES", "KW_MEMBER", "KW_MERGE", "KW_METADATA", "KW_METHOD",
    "KW_MIN", "KW_MINUS", "KW_MINUTE", "KW_MOD", "KW_MODIFIES", "KW_MODIFY",
    "KW_MODULE", "KW_MODULES", "KW_MONDAY", "KW_MONTH", "KW_MORE", "KW_MULTISET",
    "KW_NATIONAL", "KW_NATURAL", "KW_NCHAR", "KW_NCLOB", "KW_NEW", "KW_NEXT",
    "KW_NO", "KW_NONE", "KW_NORMALIZE", "KW_NOT", "KW_NTH_VALUE", "KW_NTILE",
    "KW_NULL", "KW_NULLIF", "KW_NUMERIC", "KW_OCCURRENCES_REGEX", "KW_OCTET_LENGTH",
    "KW_OF", "KW_OFFSET", "KW_OLD", "KW_OMIT", "KW_ON", "KW_ONE", "KW_ONLY",
    "KW_OPEN", "KW_OR", "KW_ORDER", "KW_ORDINAL", "KW_OUT", "KW_OUTER", "KW_OVER",
    "KW_OVERLAPS", "KW_OVERLAY", "KW_OVERWRITE", "KW_OVERWRITING", "KW_PARAMETER",
    "KW_PARTITION", "KW_PARTITIONED", "KW_PARTITIONS", "KW_PATTERN", "KW_PER",
    "KW_PERCENT", "KW_PERCENTILE_CONT", "KW_PERCENTILE_DISC", "KW_PERCENT_RANK",
    "KW_PERIOD", "KW_PERMUTE", "KW_PIVOT", "KW_PORTION", "KW_POSITION", "KW_POSITION_REGEX",
    "KW_POWER", "KW_PRECEDES", "KW_PRECISION", "KW_PREPARE", "KW_PREV", "KW_PRIMARY",
    "KW_PROCEDURE", "KW_QUALIFY", "KW_QUARTERS", "KW_RANGE", "KW_RANK", "KW_RAW",
    "KW_READS", "KW_REAL", "KW_RECURSIVE", "KW_REF", "KW_REFERENCES", "KW_REFERENCING",
    "KW_REGR_AVGX", "KW_REGR_AVGY", "KW_REGR_COUNT", "KW_REGR_INTERCEPT",
    "KW_REGR_R2", "KW_REGR_SLOPE", "KW_REGR_SXX", "KW_REGR_SXY", "KW_REGR_SYY",
    "KW_RELEASE", "KW_RENAME", "KW_RESET", "KW_RESULT", "KW_RETURN", "KW_RETURNS",
    "KW_REVOKE", "KW_RIGHT", "KW_RLIKE", "KW_ROLLBACK", "KW_ROLLUP", "KW_ROW",
    "KW_ROWS", "KW_ROW_NUMBER", "KW_RUNNING", "KW_SAFE_CAST", "KW_SAFE_OFFSET",
    "KW_SAFE_ORDINAL", "KW_SATURDAY", "KW_SAVEPOINT", "KW_SCALA", "KW_SCOPE",
    "KW_SCROLL", "KW_SEARCH", "KW_SECOND", "KW_SEEK", "KW_SELECT", "KW_SENSITIVE",
    "KW_SEPARATOR", "KW_SESSION_USER", "KW_SET", "KW_SHOW", "KW_SIMILAR",
    "KW_SKIP", "KW_SMALLINT", "KW_SOME", "KW_SPECIFIC", "KW_SPECIFICTYPE",
    "KW_SQL", "KW_SQLEXCEPTION", "KW_SQLSTATE", "KW_SQLWARNING", "KW_SQRT",
    "KW_START", "KW_STATEMENT", "KW_STATIC", "KW_STATISTICS", "KW_STDDEV_POP",
    "KW_STDDEV_SAMP", "KW_STREAM", "KW_STRING", "KW_STRING_AGG", "KW_SUBMULTISET",
    "KW_SUBSET", "KW_SUBSTRING", "KW_SUBSTRING_REGEX", "KW_SUCCEEDS", "KW_SUM",
    "KW_SUNDAY", "KW_SYMMETRIC", "KW_SYSTEM", "KW_SYSTEM_TIME", "KW_SYSTEM_USER",
    "KW_TABLE", "KW_TABLES", "KW_TABLESAMPLE", "KW_THEN", "KW_THURSDAY", "KW_TIME",
    "KW_TIMESTAMP", "KW_TIMESTAMP_DIFF", "KW_TIMESTAMP_LTZ", "KW_TIMESTAMP_TRUNC",
    "KW_TIMEZONE_HOUR", "KW_TIMEZONE_MINUTE", "KW_TIME_DIFF", "KW_TIME_TRUNC",
    "KW_TINYINT", "KW_TO", "KW_TRAILING", "KW_TRANSLATE", "KW_TRANSLATE_REGEX",
    "KW_TRANSLATION", "KW_TREAT", "KW_TRIGGER", "KW_TRIM", "KW_TRIM_ARRAY",
    "KW_TRUE", "KW_TRUNCATE", "KW_TRY_CAST", "KW_TUESDAY", "KW_UESCAPE", "KW_UNION",
    "KW_UNIQUE", "KW_UNKNOWN", "KW_UNNEST", "KW_UNPIVOT", "KW_UPDATE", "KW_UPPER",
    "KW_UPSERT", "KW_USE", "KW_USER", "KW_USING", "KW_VALUE", "KW_VALUES",
    "KW_VALUE_OF", "KW_VARBINARY", "KW_VARCHAR", "KW_VARYING", "KW_VAR_POP",
    "KW_VAR_SAMP", "KW_VERSIONING", "KW_VIEWS", "KW_VIRTUAL", "KW_WATERMARK",
    "KW_WATERMARKS", "KW_WEDNESDAY", "KW_WEEKS", "KW_WHEN", "KW_WHENEVER",
    "KW_WHERE", "KW_WIDTH_BUCKET", "KW_WINDOW", "KW_WITH", "KW_WITHIN", "KW_WITHOUT",
    "KW_YEAR", "KW_ADD", "KW_AFTER", "KW_ASC", "KW_CASCADE", "KW_CATALOG",
    "KW_CENTURY", "KW_CONFIG", "KW_CONSTRAINTS", "KW_CUMULATE", "KW_DATA",
    "KW_DATABASE", "KW_DAYS", "KW_DECADE", "KW_DESC", "KW_DESCRIPTOR", "KW_DIV",
    "KW_ENGINE", "KW_EPOCH", "KW_EXCLUDING", "KW_FILE", "KW_FIRST", "KW_GENERATED",
    "KW_HOP", "KW_HOURS", "KW_IGNORE", "KW_INCLUDING", "KW_JAR", "KW_JARS",
    "KW_JAVA", "KW_KEY", "KW_LAST", "KW_LOAD", "KW_MAP", "KW_MICROSECOND",
    "KW_MILLENNIUM", "KW_MILLISECOND", "KW_MINUTES", "KW_MONTHS", "KW_NANOSECOND",
    "KW_NULLS", "KW_OPTIONS", "KW_PAST", "KW_PLAN", "KW_PRECEDING", "KW_PYTHON",
    "KW_PYTHON_ARCHIVES", "KW_PYTHON_DEPENDENCIES", "KW_PYTHON_FILES", "KW_PYTHON_JAR",
    "KW_PYTHON_PARAMETER", "KW_PYTHON_REQUIREMENTS", "KW_QUARTER", "KW_REMOVE",
    "KW_RESTRICT", "KW_SECONDS", "KW_SESSION", "KW_SETS", "KW_SIZE", "KW_SLIDE",
    "KW_STEP", "KW_TEMPORARY", "KW_TIMECOL", "KW_TUMBLE", "KW_UNLOAD", "KW_VIEW",
    "KW_WEEK", "KW_YEARS", "KW_ZONE", "EQUAL_SYMBOL", "GREATER_SYMBOL", "LESS_SYMBOL",
    "EXCLAMATION_SYMBOL", "BIT_NOT_OP", "BIT_OR_OP", "BIT_AND_OP", "BIT_XOR_OP",
    "DOT", "LS_BRACKET", "RS_BRACKET", "LR_BRACKET", "RR_BRACKET", "LB_BRACKET",
    "RB_BRACKET", "COMMA", "SEMICOLON", "AT_SIGN", "SINGLE_QUOTE_SYMB", "DOUBLE_QUOTE_SYMB",
    "REVERSE_QUOTE_SYMB", "COLON_SYMB", "ASTERISK_SIGN", "UNDERLINE_SIGN",
    "HYPNEN_SIGN", "ADD_SIGN", "PENCENT_SIGN", "DOUBLE_VERTICAL_SIGN", "DOUBLE_HYPNEN_SIGN",
    "SLASH_SIGN", "QUESTION_MARK_SIGN", "DOUBLE_RIGHT_ARROW", "STRING_LITERAL",
    "DIG_LITERAL", "REAL_LITERAL", "BIT_STRING", "ID_LITERAL",
];
FlinkSqlParser.VOCABULARY = new VocabularyImpl(FlinkSqlParser._LITERAL_NAMES, FlinkSqlParser._SYMBOLIC_NAMES, []);
FlinkSqlParser._serializedATNSegments = 5;
FlinkSqlParser._serializedATNSegment0 = "\x03\uC91D\uCABA\u058D\uAFBA\u4F53\u0607\uEA8B\uC241\x03\u021F\u08DB\x04" +
    "\x02\t\x02\x04\x03\t\x03\x04\x04\t\x04\x04\x05\t\x05\x04\x06\t\x06\x04" +
    "\x07\t\x07\x04\b\t\b\x04\t\t\t\x04\n\t\n\x04\v\t\v\x04\f\t\f\x04\r\t\r" +
    "\x04\x0E\t\x0E\x04\x0F\t\x0F\x04\x10\t\x10\x04\x11\t\x11\x04\x12\t\x12" +
    "\x04\x13\t\x13\x04\x14\t\x14\x04\x15\t\x15\x04\x16\t\x16\x04\x17\t\x17" +
    "\x04\x18\t\x18\x04\x19\t\x19\x04\x1A\t\x1A\x04\x1B\t\x1B\x04\x1C\t\x1C" +
    "\x04\x1D\t\x1D\x04\x1E\t\x1E\x04\x1F\t\x1F\x04 \t \x04!\t!\x04\"\t\"\x04" +
    "#\t#\x04$\t$\x04%\t%\x04&\t&\x04\'\t\'\x04(\t(\x04)\t)\x04*\t*\x04+\t" +
    "+\x04,\t,\x04-\t-\x04.\t.\x04/\t/\x040\t0\x041\t1\x042\t2\x043\t3\x04" +
    "4\t4\x045\t5\x046\t6\x047\t7\x048\t8\x049\t9\x04:\t:\x04;\t;\x04<\t<\x04" +
    "=\t=\x04>\t>\x04?\t?\x04@\t@\x04A\tA\x04B\tB\x04C\tC\x04D\tD\x04E\tE\x04" +
    "F\tF\x04G\tG\x04H\tH\x04I\tI\x04J\tJ\x04K\tK\x04L\tL\x04M\tM\x04N\tN\x04" +
    "O\tO\x04P\tP\x04Q\tQ\x04R\tR\x04S\tS\x04T\tT\x04U\tU\x04V\tV\x04W\tW\x04" +
    "X\tX\x04Y\tY\x04Z\tZ\x04[\t[\x04\\\t\\\x04]\t]\x04^\t^\x04_\t_\x04`\t" +
    "`\x04a\ta\x04b\tb\x04c\tc\x04d\td\x04e\te\x04f\tf\x04g\tg\x04h\th\x04" +
    "i\ti\x04j\tj\x04k\tk\x04l\tl\x04m\tm\x04n\tn\x04o\to\x04p\tp\x04q\tq\x04" +
    "r\tr\x04s\ts\x04t\tt\x04u\tu\x04v\tv\x04w\tw\x04x\tx\x04y\ty\x04z\tz\x04" +
    "{\t{\x04|\t|\x04}\t}\x04~\t~\x04\x7F\t\x7F\x04\x80\t\x80\x04\x81\t\x81" +
    "\x04\x82\t\x82\x04\x83\t\x83\x04\x84\t\x84\x04\x85\t\x85\x04\x86\t\x86" +
    "\x04\x87\t\x87\x04\x88\t\x88\x04\x89\t\x89\x04\x8A\t\x8A\x04\x8B\t\x8B" +
    "\x04\x8C\t\x8C\x04\x8D\t\x8D\x04\x8E\t\x8E\x04\x8F\t\x8F\x04\x90\t\x90" +
    "\x04\x91\t\x91\x04\x92\t\x92\x04\x93\t\x93\x04\x94\t\x94\x04\x95\t\x95" +
    "\x04\x96\t\x96\x04\x97\t\x97\x04\x98\t\x98\x04\x99\t\x99\x04\x9A\t\x9A" +
    "\x04\x9B\t\x9B\x04\x9C\t\x9C\x04\x9D\t\x9D\x04\x9E\t\x9E\x04\x9F\t\x9F" +
    "\x04\xA0\t\xA0\x04\xA1\t\xA1\x04\xA2\t\xA2\x04\xA3\t\xA3\x04\xA4\t\xA4" +
    "\x04\xA5\t\xA5\x04\xA6\t\xA6\x04\xA7\t\xA7\x04\xA8\t\xA8\x04\xA9\t\xA9" +
    "\x04\xAA\t\xAA\x04\xAB\t\xAB\x04\xAC\t\xAC\x04\xAD\t\xAD\x04\xAE\t\xAE" +
    "\x04\xAF\t\xAF\x04\xB0\t\xB0\x04\xB1\t\xB1\x04\xB2\t\xB2\x04\xB3\t\xB3" +
    "\x04\xB4\t\xB4\x04\xB5\t\xB5\x04\xB6\t\xB6\x04\xB7\t\xB7\x04\xB8\t\xB8" +
    "\x04\xB9\t\xB9\x04\xBA\t\xBA\x04\xBB\t\xBB\x04\xBC\t\xBC\x04\xBD\t\xBD" +
    "\x03\x02\x07\x02\u017C\n\x02\f\x02\x0E\x02\u017F\v\x02\x03\x02\x03\x02" +
    "\x03\x03\x03\x03\x05\x03\u0185\n\x03\x03\x03\x05\x03\u0188\n\x03\x03\x04" +
    "\x03\x04\x03\x04\x03\x04\x03\x04\x03\x04\x03\x04\x03\x04\x03\x04\x03\x04" +
    "\x03\x04\x03\x04\x05\x04\u0196\n\x04\x03\x05\x03\x05\x03\x06\x03\x06\x03" +
    "\x06\x03\x06\x03\x06\x03\x06\x03\x06\x03\x06\x03\x06\x03\x06\x03\x06\x03" +
    "\x06\x03\x06\x03\x06\x05\x06\u01A8\n\x06\x03\x07\x03\x07\x05\x07\u01AC" +
    "\n\x07\x03\b\x03\b\x03\b\x03\t\x03\t\x03\t\x03\t\x05\t\u01B5\n\t\x03\t" +
    "\x03\t\x03\t\x05\t\u01BA\n\t\x03\n\x03\n\x03\n\x07\n\u01BF\n\n\f\n\x0E" +
    "\n\u01C2\v\n\x03\v\x03\v\x03\f\x03\f\x03\f\x03\f\x03\f\x03\f\x05\f\u01CC" +
    "\n\f\x03\r\x03\r\x03\r\x03\r\x03\r\x07\r\u01D3\n\r\f\r\x0E\r\u01D6\v\r" +
    "\x03\x0E\x03\x0E\x03\x0E\x03\x0E\x03\x0E\x03\x0E\x03\x0E\x03\x0E\x03\x0E" +
    "\x05\x0E\u01E1\n\x0E\x03\x0E\x05\x0E\u01E4\n\x0E\x03\x0E\x03\x0E\x03\x0E" +
    "\x03\x0E\x03\x0E\x05\x0E\u01EB\n\x0E\x03\x0E\x05\x0E\u01EE\n\x0E\x03\x0E" +
    "\x03\x0E\x03\x0E\x03\x0E\x03\x0E\x03\x0E\x05\x0E\u01F6\n\x0E\x03\x0E\x03" +
    "\x0E\x05\x0E\u01FA\n\x0E\x03\x0E\x03\x0E\x03\x0E\x05\x0E\u01FF\n\x0E\x03" +
    "\x0E\x05\x0E\u0202\n\x0E\x03\x0F\x03\x0F\x03\x0F\x03\x0F\x03\x0F\x05\x0F" +
    "\u0209\n\x0F\x03\x10\x03\x10\x03\x10\x03\x10\x03\x11\x03\x11\x05\x11\u0211" +
    "\n\x11\x03\x12\x03\x12\x05\x12\u0215\n\x12\x03\x13\x03\x13\x03\x13\x03" +
    "\x13\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x05\x14\u0221\n\x14" +
    "\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x05\x14\u0229\n\x14\x03" +
    "\x14\x03\x14\x05\x14\u022D\n\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14" +
    "\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14" +
    "\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14" +
    "\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x05\x14\u024D" +
    "\n\x14\x03\x15\x05\x15\u0250\n\x15\x03\x15\x06\x15\u0253\n\x15\r\x15\x0E" +
    "\x15\u0254\x03\x16\x03\x16\x05\x16\u0259\n\x16\x03\x17\x03\x17\x05\x17" +
    "\u025D\n\x17\x03\x17\x03\x17\x05\x17\u0261\n\x17\x03\x17\x03\x17\x03\x17" +
    "\x03\x17\x03\x17\x07\x17\u0268\n\x17\f\x17\x0E\x17\u026B\v\x17\x03\x17" +
    "\x03\x17\x05\x17\u026F\n\x17\x03\x17\x03\x17\x05\x17\u0273\n\x17\x03\x17" +
    "\x03\x17\x05\x17\u0277\n\x17\x03\x17\x03\x17\x05\x17\u027B\n\x17\x03\x17" +
    "\x05\x17\u027E\n\x17\x03\x17\x03\x17\x05\x17\u0282\n\x17\x03\x18\x03\x18" +
    "\x03\x18\x05\x18\u0287\n\x18\x03\x18\x03\x18\x03\x18\x03\x18\x05\x18\u028D" +
    "\n\x18\x03\x19\x03\x19\x03\x19\x05\x19\u0292\n\x19\x03\x1A\x03\x1A\x03" +
    "\x1A\x05\x1A\u0297\n\x1A\x03\x1A\x05\x1A\u029A\n\x1A\x03\x1B\x03\x1B\x05" +
    "\x1B\u029E\n\x1B\x03\x1C\x03\x1C\x05\x1C\u02A2\n\x1C\x03\x1D\x03\x1D\x03" +
    "\x1D\x03\x1D\x07\x1D\u02A8\n\x1D\f\x1D\x0E\x1D\u02AB\v\x1D\x03\x1D\x03" +
    "\x1D\x03\x1E\x03\x1E\x03\x1E\x05\x1E\u02B2\n\x1E\x03\x1E\x03\x1E\x05\x1E" +
    "\u02B6\n\x1E\x03\x1E\x03\x1E\x05\x1E\u02BA\n\x1E\x03\x1E\x03\x1E\x05\x1E" +
    "\u02BE\n\x1E\x03\x1E\x03\x1E\x05\x1E\u02C2\n\x1E\x03\x1E\x03\x1E\x05\x1E" +
    "\u02C6\n\x1E\x03\x1E\x03\x1E\x05\x1E\u02CA\n\x1E\x03\x1E\x03\x1E\x05\x1E" +
    "\u02CE\n\x1E\x03\x1E\x03\x1E\x05\x1E\u02D2\n\x1E\x05\x1E\u02D4\n\x1E\x03" +
    "\x1F\x03\x1F\x03\x1F\x03\x1F\x03 \x03 \x03 \x03 \x05 \u02DE\n \x03 \x03" +
    " \x03!\x03!\x03!\x03!\x05!\u02E6\n!\x03!\x03!\x03\"\x03\"\x03\"\x03\"" +
    "\x03#\x03#\x03#\x03#\x03#\x03#\x03#\x03$\x03$\x03$\x03$\x03$\x03$\x03" +
    "$\x07$\u02FC\n$\f$\x0E$\u02FF\v$\x03$\x03$\x03%\x03%\x05%\u0305\n%\x03" +
    "%\x03%\x03%\x03%\x05%\u030B\n%\x03%\x05%\u030E\n%\x03%\x05%\u0311\n%\x03" +
    "&\x03&\x03&\x03\'\x03\'\x03\'\x03\'\x03\'\x05\'\u031B\n\'\x03\'\x05\'" +
    "\u031E\n\'\x03(\x03(\x03)\x03)\x03)\x03)\x05)\u0326\n)\x03*\x03*\x03+" +
    "\x03+\x03+\x03+\x03+\x03+\x03,\x03,\x05,\u0332\n,\x03,\x03,\x03,\x03," +
    "\x03,\x03,\x03-\x03-\x03.\x03.\x03.\x03.\x03/\x03/\x03/\x03/\x030\x03" +
    "0\x030\x030\x070\u0348\n0\f0\x0E0\u034B\v0\x030\x030\x031\x031\x031\x03" +
    "1\x031\x031\x071\u0355\n1\f1\x0E1\u0358\v1\x031\x031\x051\u035C\n1\x03" +
    "2\x032\x052\u0360\n2\x033\x033\x033\x033\x073\u0366\n3\f3\x0E3\u0369\v" +
    "3\x033\x053\u036C\n3\x034\x034\x034\x034\x054\u0372\n4\x035\x035\x035" +
    "\x035\x035\x036\x036\x036\x056\u037C\n6\x036\x036\x056\u0380\n6\x036\x03" +
    "6\x037\x037\x057\u0386\n7\x037\x037\x057\u038A\n7\x037\x037\x057\u038E" +
    "\n7\x037\x057\u0391\n7\x037\x037\x037\x038\x038\x038\x038\x058\u039A\n" +
    "8\x038\x038\x058\u039E\n8\x038\x038\x038\x038\x038\x058\u03A5\n8\x038" +
    "\x058\u03A8\n8\x039\x039\x039\x039\x039\x039\x079\u03B0\n9\f9\x0E9\u03B3" +
    "\v9\x03:\x03:\x03;\x03;\x03;\x05;\u03BA\n;\x03;\x03;\x03;\x03;\x03;\x03" +
    ";\x05;\u03C2\n;\x03<\x03<\x05<\u03C6\n<\x03<\x03<\x03<\x03=\x03=\x03=" +
    "\x03>\x03>\x03>\x03>\x03>\x03>\x03>\x05>\u03D5\n>\x03?\x03?\x03?\x03?" +
    "\x03@\x03@\x03@\x03@\x03A\x03A\x03A\x03B\x03B\x03B\x03B\x03B\x03B\x05" +
    "B\u03E8\nB\x03C\x03C\x03C\x03C\x03C\x03D\x03D\x03D\x03D\x05D\u03F3\nD" +
    "\x03D\x03D\x05D\u03F7\nD\x03D\x03D\x03D\x03D\x03D\x05D\u03FE\nD\x03E\x03" +
    "E\x03E\x05E\u0403\nE\x03E\x03E\x03F\x03F\x05F\u0409\nF\x03F\x03F\x05F" +
    "\u040D\nF\x03F\x03F\x03G\x03G\x03G\x05G\u0414\nG\x03G\x03G\x05G\u0418" +
    "\nG\x03H\x03H\x05H\u041C\nH\x03H\x03H\x05H\u0420\nH\x03H\x03H\x03I\x03" +
    "I\x03I\x03I\x05I\u0428\nI\x03I\x03I\x05I\u042C\nI\x03I\x03I\x03J\x05J" +
    "\u0431\nJ\x03J\x03J\x03J\x03J\x05J\u0437\nJ\x03K\x03K\x03K\x03K\x05K\u043D" +
    "\nK\x03K\x05K\u0440\nK\x03K\x03K\x05K\u0444\nK\x03L\x03L\x03L\x03M\x03" +
    "M\x03M\x03M\x07M\u044D\nM\fM\x0EM\u0450\vM\x03N\x03N\x03N\x03N\x07N\u0456" +
    "\nN\fN\x0EN\u0459\vN\x03N\x03N\x03O\x03O\x03O\x03O\x03O\x03O\x03O\x06" +
    "O\u0464\nO\rO\x0EO\u0465\x03O\x03O\x03P\x03P\x03P\x03P\x03P\x03P\x06P" +
    "\u0470\nP\rP\x0EP\u0471\x03P\x03P\x03Q\x03Q\x03Q\x03Q\x03Q\x03Q\x03Q\x03" +
    "Q\x03Q\x03Q\x03Q\x05Q\u0481\nQ\x03Q\x05Q\u0484\nQ\x03Q\x03Q\x05Q\u0488" +
    "\nQ\x03Q\x05Q\u048B\nQ\x05Q\u048D\nQ\x03Q\x03Q\x03Q\x05Q\u0492\nQ\x03" +
    "Q\x03Q\x05Q\u0496\nQ\x03Q\x05Q\u0499\nQ\x07Q\u049B\nQ\fQ\x0EQ\u049E\v" +
    "Q\x03R\x03R\x03R\x03R\x07R\u04A4\nR\fR\x0ER\u04A7\vR\x03S\x03S\x03S\x03" +
    "S\x07S\u04AD\nS\fS\x0ES\u04B0\vS\x03T\x03T\x03T\x03T\x03T\x07T\u04B7\n" +
    "T\fT\x0ET\u04BA\vT\x03T\x03T\x05T\u04BE\nT\x03T\x03T\x03T\x03T\x03T\x03" +
    "U\x03U\x03V\x03V\x05V\u04C9\nV\x03V\x05V\u04CC\nV\x03V\x05V\u04CF\nV\x03" +
    "V\x05V\u04D2\nV\x03V\x05V\u04D5\nV\x03V\x03V\x03V\x03V\x05V\u04DB\nV\x03" +
    "W\x03W\x05W\u04DF\nW\x03W\x03W\x03W\x03W\x07W\u04E5\nW\fW\x0EW\u04E8\v" +
    "W\x05W\u04EA\nW\x03X\x03X\x03X\x05X\u04EF\nX\x03X\x05X\u04F2\nX\x05X\u04F4" +
    "\nX\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x05Y\u0502" +
    "\nY\x03Z\x03Z\x03Z\x03[\x03[\x03[\x03[\x07[\u050B\n[\f[\x0E[\u050E\v[" +
    "\x03[\x03[\x05[\u0512\n[\x03[\x03[\x03[\x03[\x03[\x03[\x05[\u051A\n[\x03" +
    "[\x05[\u051D\n[\x03[\x05[\u0520\n[\x03[\x03[\x03[\x05[\u0525\n[\x07[\u0527" +
    "\n[\f[\x0E[\u052A\v[\x03\\\x03\\\x05\\\u052E\n\\\x03]\x05]\u0531\n]\x03" +
    "]\x03]\x05]\u0535\n]\x03]\x05]\u0538\n]\x03]\x05]\u053B\n]\x03]\x03]\x05" +
    "]\u053F\n]\x03]\x05]\u0542\n]\x03]\x05]\u0545\n]\x03]\x03]\x03]\x03]\x03" +
    "]\x03]\x03]\x03]\x07]\u054F\n]\f]\x0E]\u0552\v]\x03]\x03]\x03]\x03]\x05" +
    "]\u0558\n]\x03]\x03]\x03]\x03]\x03]\x03]\x03]\x03]\x03]\x05]\u0563\n]" +
    "\x03^\x03^\x03^\x03^\x03^\x03^\x03_\x03_\x03`\x03`\x03`\x03`\x03`\x03" +
    "a\x03a\x03a\x03a\x03a\x03b\x03b\x03b\x03b\x03b\x07b\u057C\nb\fb\x0Eb\u057F" +
    "\vb\x03b\x03b\x03c\x03c\x03d\x03d\x03d\x03d\x03d\x03d\x03d\x03d\x03d\x03" +
    "d\x03d\x03d\x03d\x03d\x03d\x05d\u0594\nd\x03e\x03e\x03f\x03f\x03f\x03" +
    "f\x03f\x03g\x03g\x03g\x03g\x05g\u05A1\ng\x03h\x03h\x03h\x03i\x03i\x03" +
    "i\x03i\x03i\x07i\u05AB\ni\fi\x0Ei\u05AE\vi\x03j\x03j\x03j\x03j\x03j\x03" +
    "j\x03j\x03j\x07j\u05B8\nj\fj\x0Ej\u05BB\vj\x03j\x03j\x03j\x03j\x03j\x03" +
    "j\x03j\x07j\u05C4\nj\fj\x0Ej\u05C7\vj\x03j\x03j\x03j\x03j\x03j\x03j\x03" +
    "j\x07j\u05D0\nj\fj\x0Ej\u05D3\vj\x03j\x03j\x05j\u05D7\nj\x03k\x03k\x03" +
    "k\x03l\x03l\x03m\x03m\x03m\x03m\x03m\x03m\x03m\x03n\x03n\x03o\x03o\x03" +
    "p\x03p\x03p\x03q\x03q\x03q\x03q\x07q\u05F0\nq\fq\x0Eq\u05F3\vq\x03r\x03" +
    "r\x03r\x03r\x03s\x05s\u05FA\ns\x03s\x03s\x05s\u05FE\ns\x03s\x05s\u0601" +
    "\ns\x03s\x05s\u0604\ns\x03s\x03s\x03t\x03t\x03t\x05t\u060B\nt\x03t\x05" +
    "t\u060E\nt\x03t\x05t\u0611\nt\x03t\x05t\u0614\nt\x03t\x05t\u0617\nt\x03" +
    "t\x05t\u061A\nt\x03t\x03t\x03t\x05t\u061F\nt\x03t\x05t\u0622\nt\x03u\x03" +
    "u\x03u\x03u\x03u\x07u\u0629\nu\fu\x0Eu\u062C\vu\x03v\x03v\x05v\u0630\n" +
    "v\x03v\x03v\x05v\u0634\nv\x03w\x03w\x03w\x05w\u0639\nw\x03x\x03x\x03x" +
    "\x03x\x03x\x07x\u0640\nx\fx\x0Ex\u0643\vx\x03y\x03y\x03y\x03y\x03y\x03" +
    "y\x03y\x03y\x03y\x03y\x03y\x03y\x03y\x03y\x03y\x03y\x05y\u0655\ny\x03" +
    "z\x03z\x03z\x03z\x07z\u065B\nz\fz\x0Ez\u065E\vz\x03{\x03{\x03{\x06{\u0663" +
    "\n{\r{\x0E{\u0664\x03{\x03{\x05{\u0669\n{\x03|\x03|\x05|\u066D\n|\x03" +
    "}\x03}\x03}\x03}\x03}\x03}\x03}\x03}\x05}\u0677\n}\x03~\x03~\x03~\x03" +
    "~\x03~\x03~\x03~\x03~\x03~\x03~\x03~\x03~\x03~\x03~\x03~\x03~\x03~\x03" +
    "~\x03~\x03~\x03~\x03~\x03~\x03~\x05~\u0691\n~\x03\x7F\x03\x7F\x03\x7F" +
    "\x03\x7F\x07\x7F\u0697\n\x7F\f\x7F\x0E\x7F\u069A\v\x7F\x03\x80\x03\x80" +
    "\x03\x80\x03\x80\x03\x80\x03\x80\x03\x80\x03\x80\x03\x80\x05\x80\u06A5" +
    "\n\x80\x03\x81\x03\x81\x03\x81\x03\x81\x03\x81\x03\x82\x03\x82\x03\x82" +
    "\x03\x83\x03\x83\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84" +
    "\x03\x84\x03\x84\x03\x84\x05\x84\u06BB\n\x84\x05\x84\u06BD\n\x84\x03\x84" +
    "\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84\x05\x84" +
    "\u06C8\n\x84\x03\x84\x07\x84\u06CB\n\x84\f\x84\x0E\x84\u06CE\v\x84\x03" +
    "\x85\x05\x85\u06D1\n\x85\x03\x85\x03\x85\x05\x85\u06D5\n\x85\x03\x85\x03" +
    "\x85\x03\x85\x03\x85\x03\x85\x05\x85\u06DC\n\x85\x03\x85\x03\x85\x03\x85" +
    "\x03\x85\x03\x85\x07\x85\u06E3\n\x85\f\x85\x0E\x85\u06E6\v\x85\x03\x85" +
    "\x03\x85\x03\x85\x05\x85\u06EB\n\x85\x03\x85\x03\x85\x03\x85\x03\x85\x03" +
    "\x85\x03\x85\x03\x85\x03\x85\x03\x85\x03\x85\x03\x85\x05\x85\u06F8\n\x85" +
    "\x03\x85\x03\x85\x03\x85\x03\x85\x03\x85\x05\x85\u06FF\n\x85\x03\x85\x03" +
    "\x85\x03\x85\x05\x85\u0704\n\x85\x03\x85\x03\x85\x03\x85\x03\x85\x05\x85" +
    "\u070A\n\x85\x03\x85\x03\x85\x03\x85\x03\x85\x03\x85\x05\x85\u0711\n\x85" +
    "\x05\x85\u0713\n\x85\x03\x86\x05\x86\u0716\n\x86\x03\x86\x03\x86\x03\x86" +
    "\x03\x86\x03\x86\x03\x86\x03\x86\x03\x86\x07\x86\u0720\n\x86\f\x86\x0E" +
    "\x86\u0723\v\x86\x03\x86\x03\x86\x05\x86\u0727\n\x86\x03\x86\x05\x86\u072A" +
    "\n\x86\x03\x86\x03\x86\x03\x86\x03\x86\x05\x86\u0730\n\x86\x05\x86\u0732" +
    "\n\x86\x03\x87\x03\x87\x03\x87\x03\x87\x05\x87\u0738\n\x87\x03\x87\x03" +
    "\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03" +
    "\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x03\x87\x07" +
    "\x87\u074D\n\x87\f\x87\x0E\x87\u0750\v\x87\x03\x88\x03\x88\x03\x88\x06" +
    "\x88\u0755\n\x88\r\x88\x0E\x88\u0756\x03\x88\x03\x88\x05\x88\u075B\n\x88" +
    "\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x06\x88\u0762\n\x88\r\x88\x0E" +
    "\x88\u0763\x03\x88\x03\x88\x05\x88\u0768\n\x88\x03\x88\x03\x88\x03\x88" +
    "\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88" +
    "\x03\x88\x03\x88\x05\x88\u0778\n\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03" +
    "\x88\x03\x88\x03\x88\x05\x88\u0781\n\x88\x03\x88\x03\x88\x03\x88\x03\x88" +
    "\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88" +
    "\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88" +
    "\x05\x88\u0799\n\x88\x03\x88\x03\x88\x03\x88\x07\x88\u079E\n\x88\f\x88" +
    "\x0E\x88\u07A1\v\x88\x05\x88\u07A3\n\x88\x03\x88\x03\x88\x03\x88\x03\x88" +
    "\x03\x88\x03\x88\x03\x88\x03\x88\x03\x88\x05\x88\u07AE\n\x88\x03\x88\x03" +
    "\x88\x03\x88\x03\x88\x03\x88\x07\x88\u07B5\n\x88\f\x88\x0E\x88\u07B8\v" +
    "\x88\x03\x89\x03\x89\x03\x8A\x03\x8A\x05\x8A\u07BE\n\x8A\x03\x8B\x03\x8B" +
    "\x03\x8B\x03\x8B\x05\x8B\u07C4\n\x8B\x03\x8C\x03\x8C\x03\x8D\x03\x8D\x03" +
    "\x8E\x03\x8E\x05\x8E\u07CC\n\x8E\x03\x8F\x03\x8F\x03\x8F\x05\x8F\u07D1" +
    "\n\x8F\x03\x90\x03\x90\x05\x90\u07D5\n\x90\x03\x91\x03\x91\x03\x91\x06" +
    "\x91\u07DA\n\x91\r\x91\x0E\x91\u07DB\x03\x92\x03\x92\x03\x92\x05\x92\u07E1" +
    "\n\x92\x03\x93\x03\x93\x03\x93\x03\x93\x03\x93\x03\x94\x05\x94\u07E9\n" +
    "\x94\x03\x94\x03\x94\x05\x94\u07ED\n\x94\x03\x95\x05\x95\u07F0\n\x95\x03" +
    "\x95\x03\x95\x05\x95\u07F4\n\x95\x03\x96\x03\x96\x03\x96\x03\x97\x03\x97" +
    "\x06\x97\u07FB\n\x97\r\x97\x0E\x97\u07FC\x03\x97\x05\x97\u0800\n\x97\x03" +
    "\x98\x03\x98\x03\x98\x03\x98\x03\x99\x03\x99\x03\x99\x07\x99\u0809\n\x99" +
    "\f\x99\x0E\x99\u080C\v\x99\x03\x9A\x03\x9A\x03\x9A\x05\x9A\u0811\n\x9A" +
    "\x03\x9B\x03\x9B\x03\x9C\x03\x9C\x03\x9D\x03\x9D\x03\x9D\x03\x9D\x03\x9D" +
    "\x03\x9E\x03\x9E\x03\x9F\x03\x9F\x03\xA0\x03\xA0\x03\xA0\x05\xA0\u0823" +
    "\n\xA0\x03\xA1\x03\xA1\x03\xA1\x05\xA1\u0828\n\xA1\x03\xA2\x03\xA2\x03" +
    "\xA2\x05\xA2\u082D\n\xA2\x03\xA2\x03\xA2\x03\xA2\x03\xA2\x03\xA2\x05\xA2" +
    "\u0834\n\xA2\x05\xA2\u0836\n\xA2\x03\xA3\x03\xA3\x03\xA3\x05\xA3\u083B" +
    "\n\xA3\x03\xA3\x03\xA3\x03\xA3\x03\xA3\x03\xA3\x05\xA3\u0842\n\xA3\x05" +
    "\xA3\u0844\n\xA3\x03\xA4\x03\xA4\x03\xA4\x05\xA4\u0849\n\xA4\x03\xA4\x03" +
    "\xA4\x03\xA4\x03\xA4\x03\xA4\x05\xA4\u0850\n\xA4\x05\xA4\u0852\n\xA4\x03" +
    "\xA5\x03\xA5\x03\xA5\x05\xA5\u0857\n\xA5\x03\xA5\x03\xA5\x03\xA5\x03\xA5" +
    "\x03\xA5\x05\xA5\u085E\n\xA5\x05\xA5\u0860\n\xA5\x03\xA6\x03\xA6\x03\xA6" +
    "\x07\xA6\u0865\n\xA6\f\xA6\x0E\xA6\u0868\v\xA6\x03\xA7\x03\xA7\x03\xA7" +
    "\x03\xA8\x03\xA8\x03\xA8\x03\xA8\x03\xA9\x03\xA9\x03\xA9\x03\xAA\x03\xAA" +
    "\x03\xAA\x03\xAA\x07\xAA\u0878\n\xAA\f\xAA\x0E\xAA\u087B\v\xAA\x03\xAA" +
    "\x03\xAA\x03\xAB\x03\xAB\x05\xAB\u0881\n\xAB\x03\xAB\x05\xAB\u0884\n\xAB" +
    "\x03\xAC\x03\xAC\x03\xAC\x05\xAC\u0889\n\xAC\x03\xAD\x03\xAD\x03\xAD\x03" +
    "\xAD\x05\xAD\u088F\n\xAD\x03\xAE\x03\xAE\x03\xAE\x03\xAE\x03\xAE\x03\xAE" +
    "\x05\xAE\u0897\n\xAE\x03\xAF\x03\xAF\x03\xAF\x03\xAF\x03\xAF\x03\xAF\x03" +
    "\xAF\x03\xAF\x03\xAF\x03\xAF\x03\xAF\x03\xAF\x03\xAF\x03\xAF\x05\xAF\u08A7" +
    "\n\xAF\x03\xB0\x03\xB0\x03\xB0\x03\xB0\x03\xB0\x03\xB0\x03\xB0\x05\xB0" +
    "\u08B0\n\xB0\x03\xB1\x03\xB1\x03\xB2\x03\xB2\x03\xB3\x03\xB3\x03\xB3\x03" +
    "\xB3\x05\xB3\u08BA\n\xB3\x03\xB3\x03\xB3\x03\xB3\x03\xB3\x03\xB3\x05\xB3" +
    "\u08C1\n\xB3\x03\xB3\x05\xB3\u08C4\n\xB3\x03\xB4\x03\xB4\x03\xB4\x03\xB5" +
    "\x03\xB5\x03\xB6\x03\xB6\x03\xB7\x03\xB7\x03\xB8\x03\xB8\x03\xB9\x03\xB9" +
    "\x03\xBA\x03\xBA\x03\xBB\x03\xBB\x03\xBC\x03\xBC\x03\xBD\x03\xBD\x03\xBD" +
    "\x03\u0866\x02\x07\xA0\xB4\u0106\u010C\u010E\xBE\x02\x02\x04\x02\x06\x02" +
    "\b\x02\n\x02\f\x02\x0E\x02\x10\x02\x12\x02\x14\x02\x16\x02\x18\x02\x1A" +
    "\x02\x1C\x02\x1E\x02 \x02\"\x02$\x02&\x02(\x02*\x02,\x02.\x020\x022\x02" +
    "4\x026\x028\x02:\x02<\x02>\x02@\x02B\x02D\x02F\x02H\x02J\x02L\x02N\x02" +
    "P\x02R\x02T\x02V\x02X\x02Z\x02\\\x02^\x02`\x02b\x02d\x02f\x02h\x02j\x02" +
    "l\x02n\x02p\x02r\x02t\x02v\x02x\x02z\x02|\x02~\x02\x80\x02\x82\x02\x84" +
    "\x02\x86\x02\x88\x02";
FlinkSqlParser._serializedATNSegment1 = "\x8A\x02\x8C\x02\x8E\x02\x90\x02\x92\x02\x94\x02\x96\x02\x98\x02\x9A\x02" +
    "\x9C\x02\x9E\x02\xA0\x02\xA2\x02\xA4\x02\xA6\x02\xA8\x02\xAA\x02\xAC\x02" +
    "\xAE\x02\xB0\x02\xB2\x02\xB4\x02\xB6\x02\xB8\x02\xBA\x02\xBC\x02\xBE\x02" +
    "\xC0\x02\xC2\x02\xC4\x02\xC6\x02\xC8\x02\xCA\x02\xCC\x02\xCE\x02\xD0\x02" +
    "\xD2\x02\xD4\x02\xD6\x02\xD8\x02\xDA\x02\xDC\x02\xDE\x02\xE0\x02\xE2\x02" +
    "\xE4\x02\xE6\x02\xE8\x02\xEA\x02\xEC\x02\xEE\x02\xF0\x02\xF2\x02\xF4\x02" +
    "\xF6\x02\xF8\x02\xFA\x02\xFC\x02\xFE\x02\u0100\x02\u0102\x02\u0104\x02" +
    "\u0106\x02\u0108\x02\u010A\x02\u010C\x02\u010E\x02\u0110\x02\u0112\x02" +
    "\u0114\x02\u0116\x02\u0118\x02\u011A\x02\u011C\x02\u011E\x02\u0120\x02" +
    "\u0122\x02\u0124\x02\u0126\x02\u0128\x02\u012A\x02\u012C\x02\u012E\x02" +
    "\u0130\x02\u0132\x02\u0134\x02\u0136\x02\u0138\x02\u013A\x02\u013C\x02" +
    "\u013E\x02\u0140\x02\u0142\x02\u0144\x02\u0146\x02\u0148\x02\u014A\x02" +
    "\u014C\x02\u014E\x02\u0150\x02\u0152\x02\u0154\x02\u0156\x02\u0158\x02" +
    "\u015A\x02\u015C\x02\u015E\x02\u0160\x02\u0162\x02\u0164\x02\u0166\x02" +
    "\u0168\x02\u016A\x02\u016C\x02\u016E\x02\u0170\x02\u0172\x02\u0174\x02" +
    "\u0176\x02\u0178\x02\x02/\x04\x02oo\u01C4\u01C4\x05\x02//\x82\x82\xBE" +
    "\xBE\x06\x02,,\\\\\u01A8\u01A8\u01D2\u01D2\x04\x02\u01BB\u01BB\u01C1\u01C1" +
    "\x04\x02\x99\x99\xAC\xAC\x04\x02\u01B7\u01B7\u01EB\u01EB\x04\x02\u01E4" +
    "\u01E7\u01E9\u01E9\x05\x02\"\"]]\xF6\xF6\r\x02\x1E\x1F%%00^^\xB4\xB5\u015A" +
    "\u015A\u016A\u016A\u017C\u017C\u017F\u017F\u0185\u0185\u01A2\u01A3\x04" +
    "\x02\u01B3\u01B3\u01B5\u01B5\x06\x02ghuu\x92\x92\xF8\xF8\x04\x02\x0F\x0F" +
    "\xE9\xE9\x04\x02\u01C9\u01C9\u01D0\u01D0\x05\x02\x07\x07\u0110\u0110\u01BE" +
    "\u01BE\x05\x02\u010C\u010C\u01C9\u01C9\u01D0\u01D0\x05\x02\u01AB\u01AB" +
    "\u01CC\u01CC\u01DF\u01DF\x05\x02\u014C\u014C\u01D3\u01D3\u01E3\u01E3\x04" +
    "\x02\u01BA\u01BA\u01EC\u01EC\x04\x02\xB9\xB9\u010B\u010B\x05\x02\x84\x84" +
    "\xB6\xB6\u0194\u0194\x06\x02\x9A\x9A\xB0\xB0\xCB\xCB\u013F\u013F\x05\x02" +
    "\u01BF\u01BF\u01CD\u01CD\u01F5\u01F5\x06\x02\xFC\xFC\u01C0\u01C0\u01F0" +
    "\u01F2\u01F4\u01F4\x04\x02LL\u0142\u0142\x05\x02\u01CD\u01CD\u01EE\u01EE" +
    "\u01F5\u01F5\x04\x02\u01B9\u01B9\u01C4\u01C4\x04\x02\u01CB\u01CB\u01D5" +
    "\u01D5\x06\x02\x8E\x8E\xF6\xF6\u018F\u018F\u0196\u0196\x04\x02\x15\x15" +
    "\u0173\u0173\x04\x02\x07\x07\r\r\x04\x02\u01FF\u01FF\u0213\u0214\x06\x02" +
    "\u01C6\u01C6\u0211\u0211\u0215\u0215\u0218\u0218\x04\x02\u0213\u0214\u0216" +
    "\u0216\x03\x02\u0213\u0214\x03\x02\u021C\u021D\x04\x02\u021C\u021C\u021F" +
    "\u021F\x06\x02\u01C6\u01C6\u0211\u0211\u0213\u0215\u0217\u0218\x05\x02" +
    "\xF3\xF3\u01FE\u01FF\u0213\u0214\x04\x02\x8E\x8E\u018F\u018F\x04\x02\x07" +
    "\x07ss\f\x02cc\xA7\xA7\xE0\xE0\xE7\xE7\u0150\u0150\u01B6\u01B6\u01D8\u01D8" +
    "\u01DA\u01DA\u01EA\u01EA\u01F8\u01F8\x11\x02cc\xA7\xA7\xE0\xE0\xE7\xE7" +
    "\u0150\u0150\u01AD\u01AD\u01B6\u01B6\u01BC\u01BC\u01C2\u01C3\u01C8\u01C8" +
    "\u01CE\u01CE\u01D8\u01DD\u01EA\u01EA\u01ED\u01ED\u01F8\u01F9\v\x02\x07" +
    "\x07\x0F\x0F##WWss\xCA\xCA\u0187\u0187\u019F\u019F\u0211\u02119\x02\x06" +
    "\x06\x0F\x0F\x19\x19((++-.88::GGMMPPVW]]demmyy\x88\x88\x8D\x8D\x91\x91" +
    "\x93\x93\xA2\xA2\xA7\xA7\xA9\xA9\xC4\xC4\xC7\xC7\xC9\xC9\xCB\xCB\xCF\xCF" +
    "\xD1\xD3\xD8\xD8\xDE\xDE\xE0\xE1\xE7\xE7\xF5\xF5\xF7\xF7\u010A\u010A\u0116" +
    "\u0116\u011B\u011B\u011D\u011D\u0127\u0127\u013F\u013F\u0143\u0145\u0150" +
    "\u0150\u0167\u0168\u016E\u016E\u0171\u0171\u017C\u017E\u018D\u018D\u0190" +
    "\u0191\u019A\u019A\u01A5\u01A6\u01B6\u01B6\u01D7\u01D7\u01EA\u01EA\u01F8" +
    "\u01F8\x03\x02\u01B7\u01FA\x02\u09A3\x02\u017D\x03\x02\x02\x02\x04\u0187" +
    "\x03\x02\x02\x02\x06\u0195\x03\x02\x02\x02\b\u0197\x03\x02\x02\x02\n\u01A7" +
    "\x03\x02\x02\x02\f\u01AB\x03\x02\x02\x02\x0E\u01AD\x03\x02\x02\x02\x10" +
    "\u01B0\x03\x02\x02\x02\x12\u01BB\x03\x02\x02\x02\x14\u01C3\x03\x02\x02" +
    "\x02\x16\u01CB\x03\x02\x02\x02\x18\u01CD\x03\x02\x02\x02\x1A\u0201\x03" +
    "\x02\x02\x02\x1C\u0203\x03\x02\x02\x02\x1E\u020A\x03\x02\x02\x02 \u020E" +
    "\x03\x02\x02\x02\"\u0212\x03\x02\x02\x02$\u0216\x03\x02\x02\x02&\u024C" +
    "\x03\x02\x02\x02(\u0252\x03\x02\x02\x02*\u0258\x03\x02\x02\x02,\u025A" +
    "\x03\x02\x02\x02.\u0283\x03\x02\x02\x020\u0291\x03\x02\x02\x022\u0293" +
    "\x03\x02\x02\x024\u029D\x03\x02\x02\x026\u02A1\x03\x02\x02\x028\u02A3" +
    "\x03\x02\x02\x02:\u02D3\x03\x02\x02\x02<\u02D5\x03\x02\x02\x02>\u02D9" +
    "\x03\x02\x02\x02@\u02E1\x03\x02\x02\x02B\u02E9\x03\x02\x02\x02D\u02ED" +
    "\x03\x02\x02\x02F\u02F4\x03\x02\x02\x02H\u0310\x03\x02\x02\x02J\u0312" +
    "\x03\x02\x02\x02L\u0315\x03\x02\x02\x02N\u031F\x03\x02\x02\x02P\u0321" +
    "\x03\x02\x02\x02R\u0327\x03\x02\x02\x02T\u0329\x03\x02\x02\x02V\u0331" +
    "\x03\x02\x02\x02X\u0339\x03\x02\x02\x02Z\u033B\x03\x02\x02\x02\\\u033F" +
    "\x03\x02\x02\x02^\u0343\x03\x02\x02\x02`\u035B\x03\x02\x02\x02b\u035F" +
    "\x03\x02\x02\x02d\u0361\x03\x02\x02\x02f\u0371\x03\x02\x02\x02h\u0373" +
    "\x03\x02\x02\x02j\u0378\x03\x02\x02\x02l\u0383\x03\x02\x02\x02n\u0395" +
    "\x03\x02\x02\x02p\u03A9\x03\x02\x02\x02r\u03B4\x03\x02\x02\x02t\u03B6" +
    "\x03\x02\x02\x02v\u03C3\x03\x02\x02\x02x\u03CA\x03\x02\x02\x02z\u03CD" +
    "\x03\x02\x02\x02|\u03D6\x03\x02\x02\x02~\u03DA\x03\x02\x02\x02\x80\u03DE" +
    "\x03\x02\x02\x02\x82\u03E1\x03\x02\x02\x02\x84\u03E9\x03\x02\x02\x02\x86" +
    "\u03EE\x03\x02\x02\x02\x88\u03FF\x03\x02\x02\x02\x8A\u0406\x03\x02\x02" +
    "\x02\x8C\u0410\x03\x02\x02\x02\x8E\u0419\x03\x02\x02\x02\x90\u0423\x03" +
    "\x02\x02\x02\x92\u0436\x03\x02\x02\x02\x94\u0438\x03\x02\x02\x02\x96\u0445" +
    "\x03\x02\x02\x02\x98\u0448\x03\x02\x02\x02\x9A\u0451\x03\x02\x02\x02\x9C" +
    "\u045C\x03\x02\x02\x02\x9E\u0469\x03\x02\x02\x02\xA0\u048C\x03\x02\x02" +
    "\x02\xA2\u049F\x03\x02\x02\x02\xA4\u04A8\x03\x02\x02\x02\xA6\u04B1\x03" +
    "\x02\x02\x02\xA8\u04C4\x03\x02\x02\x02\xAA\u04DA\x03\x02\x02\x02\xAC\u04DC" +
    "\x03\x02\x02\x02\xAE\u04F3\x03\x02\x02\x02\xB0\u0501\x03\x02\x02\x02\xB2" +
    "\u0503\x03\x02\x02\x02\xB4\u0511\x03\x02\x02\x02\xB6\u052B\x03\x02\x02" +
    "\x02\xB8\u0562\x03\x02\x02\x02\xBA\u0564\x03\x02\x02\x02\xBC\u056A\x03" +
    "\x02\x02\x02\xBE\u056C\x03\x02\x02\x02\xC0\u0571\x03\x02\x02\x02\xC2\u0576" +
    "\x03\x02\x02\x02\xC4\u0582\x03\x02\x02\x02\xC6\u0593\x03\x02\x02\x02\xC8" +
    "\u0595\x03\x02\x02\x02\xCA\u0597\x03\x02\x02\x02\xCC\u05A0\x03\x02\x02" +
    "\x02\xCE\u05A2\x03\x02\x02\x02\xD0\u05A5\x03\x02\x02\x02\xD2\u05D6\x03" +
    "\x02\x02\x02\xD4\u05D8\x03\x02\x02\x02\xD6\u05DB\x03\x02\x02\x02\xD8\u05DD" +
    "\x03\x02\x02\x02\xDA\u05E4\x03\x02\x02\x02\xDC\u05E6\x03\x02\x02\x02\xDE" +
    "\u05E8\x03\x02\x02\x02\xE0\u05EB\x03\x02\x02\x02\xE2\u05F4\x03\x02\x02" +
    "\x02\xE4\u05F9\x03\x02\x02\x02\xE6\u0607\x03\x02\x02\x02\xE8\u0623\x03" +
    "\x02\x02\x02\xEA\u062D\x03\x02\x02\x02\xEC\u0635\x03\x02\x02\x02\xEE\u063A" +
    "\x03\x02\x02\x02\xF0\u0654\x03\x02\x02\x02\xF2\u0656\x03\x02\x02\x02\xF4" +
    "\u065F\x03\x02\x02\x02\xF6\u066A\x03\x02\x02\x02\xF8\u0676\x03\x02\x02" +
    "\x02\xFA\u0690\x03\x02\x02\x02\xFC\u0692\x03\x02\x02\x02\xFE\u06A4\x03" +
    "\x02\x02\x02\u0100\u06A6\x03\x02\x02\x02\u0102\u06AB\x03\x02\x02\x02\u0104" +
    "\u06AE\x03\x02\x02\x02\u0106\u06BC\x03\x02\x02\x02\u0108\u0712\x03\x02" +
    "\x02\x02\u010A\u0731\x03\x02\x02\x02\u010C\u0737\x03\x02\x02\x02\u010E" +
    "\u07AD\x03\x02\x02\x02\u0110\u07B9\x03\x02\x02\x02\u0112\u07BD\x03\x02" +
    "\x02\x02\u0114\u07C3\x03\x02\x02\x02\u0116\u07C5\x03\x02\x02\x02\u0118" +
    "\u07C7\x03\x02\x02\x02\u011A\u07CB\x03\x02\x02\x02\u011C\u07CD\x03\x02" +
    "\x02\x02\u011E\u07D2\x03\x02\x02\x02\u0120\u07D9\x03\x02\x02\x02\u0122" +
    "\u07DD\x03\x02\x02\x02\u0124\u07E2\x03\x02\x02\x02\u0126\u07EC\x03\x02" +
    "\x02\x02\u0128\u07EF\x03\x02\x02\x02\u012A\u07F5\x03\x02\x02\x02\u012C" +
    "\u07FF\x03\x02\x02\x02\u012E\u0801\x03\x02\x02\x02\u0130\u0805\x03\x02" +
    "\x02\x02\u0132\u0810\x03\x02\x02\x02\u0134\u0812\x03\x02\x02\x02\u0136" +
    "\u0814\x03\x02\x02\x02\u0138\u0816\x03\x02\x02\x02\u013A\u081B\x03\x02" +
    "\x02\x02\u013C\u081D\x03\x02\x02\x02\u013E\u081F\x03\x02\x02\x02\u0140" +
    "\u0824\x03\x02\x02\x02\u0142\u0835\x03\x02\x02\x02\u0144\u0843\x03\x02" +
    "\x02\x02\u0146\u0851\x03\x02\x02\x02\u0148\u085F\x03\x02\x02\x02\u014A" +
    "\u0861\x03\x02\x02\x02\u014C\u0869\x03\x02\x02\x02\u014E\u086C\x03\x02" +
    "\x02\x02\u0150\u0870\x03\x02\x02\x02\u0152\u0873\x03\x02\x02\x02\u0154" +
    "\u087E\x03\x02\x02\x02\u0156\u0888\x03\x02\x02\x02\u0158\u088E\x03\x02" +
    "\x02\x02\u015A\u0896\x03\x02\x02\x02\u015C\u08A6\x03\x02\x02\x02\u015E" +
    "\u08AF\x03\x02\x02\x02\u0160\u08B1\x03\x02\x02\x02\u0162\u08B3\x03\x02" +
    "\x02\x02\u0164\u08C3\x03\x02\x02\x02\u0166\u08C5\x03\x02\x02\x02\u0168" +
    "\u08C8\x03\x02\x02\x02\u016A\u08CA\x03\x02\x02\x02\u016C\u08CC\x03\x02" +
    "\x02\x02\u016E\u08CE\x03\x02\x02\x02\u0170\u08D0\x03\x02\x02\x02\u0172" +
    "\u08D2\x03\x02\x02\x02\u0174\u08D4\x03\x02\x02\x02\u0176\u08D6\x03\x02" +
    "\x02\x02\u0178\u08D8\x03\x02\x02\x02\u017A\u017C\x05\x04\x03\x02\u017B" +
    "\u017A\x03\x02\x02\x02\u017C\u017F\x03\x02\x02\x02\u017D\u017B\x03\x02" +
    "\x02\x02\u017D\u017E\x03\x02\x02\x02\u017E\u0180\x03\x02\x02\x02\u017F" +
    "\u017D\x03\x02\x02\x02\u0180\u0181\x07\x02\x02\x03\u0181\x03\x03\x02\x02" +
    "\x02\u0182\u0184\x05\x06\x04\x02\u0183\u0185\x07\u020B\x02\x02\u0184\u0183" +
    "\x03\x02\x02\x02\u0184\u0185\x03\x02\x02\x02\u0185\u0188\x03\x02\x02\x02" +
    "\u0186\u0188\x05\b\x05\x02\u0187\u0182\x03\x02\x02\x02\u0187\u0186\x03" +
    "\x02\x02\x02\u0188\x05\x03\x02\x02\x02\u0189\u0196\x05\n\x06\x02\u018A" +
    "\u0196\x05\f\x07\x02\u018B\u0196\x05\x0E\b\x02\u018C\u0196\x05\x10\t\x02" +
    "\u018D\u0196\x05\x16\f\x02\u018E\u0196\x05\x1A\x0E\x02\u018F\u0196\x05" +
    "\x1C\x0F\x02\u0190\u0196\x05\x1E\x10\x02\u0191\u0196\x05 \x11\x02\u0192" +
    "\u0196\x05\"\x12\x02\u0193\u0196\x05$\x13\x02\u0194\u0196\x05&\x14\x02" +
    "\u0195\u0189\x03\x02\x02\x02\u0195\u018A\x03\x02\x02\x02\u0195\u018B\x03" +
    "\x02\x02\x02\u0195\u018C\x03\x02\x02\x02\u0195\u018D\x03\x02\x02\x02\u0195" +
    "\u018E\x03\x02\x02\x02\u0195\u018F\x03\x02\x02\x02\u0195\u0190\x03\x02" +
    "\x02\x02\u0195\u0191\x03\x02\x02\x02\u0195\u0192\x03\x02\x02\x02\u0195" +
    "\u0193\x03\x02\x02\x02\u0195\u0194\x03\x02\x02\x02\u0196\x07\x03\x02\x02" +
    "\x02\u0197\u0198\x07\u020B\x02\x02\u0198\t\x03\x02\x02\x02\u0199\u01A8" +
    "\x05*\x16\x02\u019A\u01A8\x05j6\x02\u019B\u01A8\x05l7\x02\u019C\u01A8" +
    "\x05n8\x02\u019D\u01A8\x05h5\x02\u019E\u01A8\x05t;\x02\u019F\u01A8\x05" +
    "\x82B\x02\u01A0\u01A8\x05\x84C\x02\u01A1\u01A8\x05\x86D\x02\u01A2\u01A8" +
    "\x05\x88E\x02\u01A3\u01A8\x05\x8AF\x02\u01A4\u01A8\x05\x8CG\x02\u01A5" +
    "\u01A8\x05\x8EH\x02\u01A6\u01A8\x05\x90I\x02\u01A7\u0199\x03\x02\x02\x02" +
    "\u01A7\u019A\x03\x02\x02\x02\u01A7\u019B\x03\x02\x02\x02\u01A7\u019C\x03" +
    "\x02\x02\x02\u01A7\u019D\x03\x02\x02\x02\u01A7\u019E\x03\x02\x02\x02\u01A7" +
    "\u019F\x03\x02\x02\x02\u01A7\u01A0\x03\x02\x02\x02\u01A7\u01A1\x03\x02" +
    "\x02\x02\u01A7\u01A2\x03\x02\x02\x02\u01A7\u01A3\x03\x02\x02\x02\u01A7" +
    "\u01A4\x03\x02\x02\x02\u01A7\u01A5\x03\x02\x02\x02\u01A7\u01A6\x03\x02" +
    "\x02\x02\u01A8\v\x03\x02\x02\x02\u01A9\u01AC\x05\xA0Q\x02\u01AA\u01AC" +
    "\x05\x92J\x02\u01AB\u01A9\x03\x02\x02\x02\u01AB\u01AA\x03\x02\x02\x02" +
    "\u01AC\r\x03\x02\x02\x02\u01AD\u01AE\t\x02\x02\x02\u01AE\u01AF\x05\u0144" +
    "\xA3\x02\u01AF\x0F\x03\x02\x02\x02\u01B0\u01B4\x07\x89\x02\x02\u01B1\u01B5" +
    "\x05\x12\n\x02\u01B2\u01B3\x07\u01E1\x02\x02\u01B3\u01B5\x07\x94\x02\x02" +
    "\u01B4\u01B1\x03\x02\x02\x02\u01B4\u01B2\x03\x02\x02\x02\u01B4\u01B5\x03" +
    "\x02\x02\x02\u01B5\u01B9\x03\x02\x02\x02\u01B6\u01BA\x05\f\x07\x02\u01B7" +
    "\u01BA\x05\x94K\x02\u01B8\u01BA\x05\x9EP\x02\u01B9\u01B6\x03\x02\x02\x02" +
    "\u01B9\u01B7\x03\x02\x02\x02\u01B9\u01B8\x03\x02\x02\x02\u01BA\x11\x03" +
    "\x02\x02\x02\u01BB\u01C0\x05\x14\v\x02\u01BC\u01BD\x07\u020A\x02\x02\u01BD" +
    "\u01BF\x05\x14\v\x02\u01BE\u01BC\x03\x02\x02\x02\u01BF\u01C2\x03\x02\x02" +
    "\x02\u01C0\u01BE\x03\x02\x02\x02\u01C0\u01C1\x03\x02\x02\x02\u01C1\x13" +
    "\x03\x02\x02\x02\u01C2\u01C0\x03\x02\x02\x02\u01C3\u01C4\t\x03\x02\x02" +
    "\u01C4\x15\x03\x02\x02\x02\u01C5\u01C6\x07\u019C\x02\x02\u01C6\u01C7\x07" +
    "\u01BB\x02\x02\u01C7\u01CC\x05\u013A\x9E\x02\u01C8\u01C9\x07\u019C\x02" +
    "\x02\u01C9\u01CC\x05\u013E\xA0\x02\u01CA\u01CC\x05\x18\r\x02\u01CB\u01C5" +
    "\x03\x02\x02\x02\u01CB\u01C8\x03\x02\x02\x02\u01CB\u01CA\x03\x02\x02\x02" +
    "\u01CC\x17\x03\x02\x02\x02\u01CD\u01CE\x07\u019C\x02\x02\u01CE\u01CF\x07" +
    "\xE5\x02\x02\u01CF\u01D4\x05\u014A\xA6\x02\u01D0\u01D1\x07\u020A\x02\x02" +
    "\u01D1\u01D3\x05\u014A\xA6\x02\u01D2\u01D0\x03\x02\x02\x02\u01D3\u01D6" +
    "\x03\x02\x02\x02\u01D4\u01D2\x03\x02\x02\x02\u01D4\u01D5\x03\x02\x02\x02" +
    "\u01D5\x19\x03\x02\x02\x02\u01D6\u01D4\x03\x02\x02\x02\u01D7\u01D8\x07" +
    "\u0157\x02\x02\u01D8\u0202\t\x04\x02\x02\u01D9\u01DA\x07\u0157\x02\x02" +
    "\u01DA\u01DB\x07N\x02\x02\u01DB\u0202\t\x05\x02\x02\u01DC\u01DD\x07\u0157" +
    "\x02\x02\u01DD\u01E0\x07\u0178\x02\x02\u01DE\u01DF\t\x06\x02\x02\u01DF" +
    "\u01E1\x05\u013E\xA0\x02\u01E0\u01DE\x03\x02\x02\x02\u01E0\u01E1\x03\x02" +
    "\x02\x02\u01E1\u01E3\x03\x02\x02\x02\u01E2\u01E4\x05\u010A\x86\x02\u01E3" +
    "\u01E2\x03\x02\x02\x02\u01E3\u01E4\x03\x02\x02\x02\u01E4\u0202\x03\x02" +
    "\x02\x02\u01E5\u01E6\x07\u0157\x02\x02\u01E6\u01E7\x07<\x02\x02\u01E7" +
    "\u01EA\t\x06\x02\x02\u01E8\u01EB\x05\u0146\xA4\x02\u01E9\u01EB\x05\u0144" +
    "\xA3\x02\u01EA\u01E8\x03\x02\x02\x02\u01EA\u01E9\x03\x02\x02\x02\u01EB" +
    "\u01ED\x03\x02\x02\x02\u01EC\u01EE\x05\u010A\x86\x02\u01ED\u01EC\x03\x02" +
    "\x02\x02\u01ED\u01EE\x03\x02\x02\x02\u01EE\u0202\x03\x02\x02\x02\u01EF" +
    "\u01F0\x07\u0157\x02\x02\u01F0\u01F5\x07J\x02\x02\u01F1\u01F2\x07\u0177" +
    "\x02\x02\u01F2\u01F6\x05\u0144\xA3\x02\u01F3\u01F4\x07\u01F7\x02\x02\u01F4" +
    "\u01F6\x05\u0146\xA4\x02\u01F5\u01F1\x03\x02\x02\x02\u01F5\u01F3\x03\x02" +
    "\x02\x02\u01F6\u0202\x03\x02\x02\x02\u01F7\u01F9\x07\u0157\x02\x02\u01F8" +
    "\u01FA\x07\u019D\x02\x02\u01F9\u01F8\x03\x02\x02\x02\u01F9\u01FA\x03\x02" +
    "\x02\x02\u01FA\u01FB\x03\x02\x02\x02\u01FB\u0202\x07\x9C\x02\x02\u01FC" +
    "\u01FE\x07\u0157\x02\x02\u01FD\u01FF\x07\x9A\x02\x02\u01FE\u01FD\x03\x02" +
    "\x02\x02\u01FE\u01FF\x03\x02\x02\x02\u01FF\u0200\x03\x02\x02\x02\u0200" +
    "\u0202\x07\xE5\x02\x02\u0201\u01D7\x03\x02\x02\x02\u0201\u01D9\x03\x02" +
    "\x02\x02\u0201\u01DC\x03\x02\x02\x02\u0201\u01E5\x03\x02\x02\x02\u0201" +
    "\u01EF\x03\x02\x02\x02\u0201\u01F7\x03\x02\x02\x02\u0201\u01FC\x03\x02" +
    "\x02\x02\u0202\x1B\x03\x02\x02\x02\u0203\u0204\x07\u01D6\x02\x02\u0204" +
    "\u0205\x07\xE4\x02\x02\u0205\u0208\x05\u014A\xA6\x02\u0206\u0207\x07\u01B3" +
    "\x02\x02\u0207\u0209\x05\u0152\xAA\x02\u0208\u0206\x03\x02\x02\x02\u0208" +
    "\u0209\x03\x02\x02\x02\u0209\x1D\x03\x02\x02\x02\u020A\u020B\x07\u01F6" +
    "\x02\x02\u020B\u020C\x07\xE4\x02\x02\u020C\u020D\x05\u014A\xA6\x02\u020D" +
    "\x1F\x03\x02\x02\x02\u020E\u0210\x07\u0156\x02\x02\u020F\u0211\x05\u0154" +
    "\xAB\x02\u0210\u020F\x03\x02\x02\x02\u0210\u0211\x03\x02\x02\x02\u0211" +
    "!\x03\x02\x02\x02\u0212\u0214\x07\u013A\x02\x02\u0213\u0215\x05\u0156" +
    "\xAC\x02\u0214\u0213\x03\x02\x02\x02\u0214\u0215\x03\x02\x02\x02\u0215" +
    "#\x03\x02\x02\x02\u0216\u0217\t\x07\x02\x02\u0217\u0218\x07\u01D1\x02" +
    "\x02\u0218\u0219\x05r:\x02\u0219%\x03\x02\x02\x02\u021A\u021B\x07\u01B7" +
    "\x02\x02\u021B\u021C\x07\u01D1\x02\x02\u021C\u021D\x07\u01B3\x02\x02\u021D" +
    "\u0220\x05(\x15\x02\u021E\u021F\x07\x13\x02\x02\u021F\u0221\x05\u014A" +
    "\xA6\x02\u0220\u021E\x03\x02\x02\x02\u0220\u0221\x03\x02\x02\x02\u0221" +
    "\u024D\x03\x02\x02\x02\u0222\u0223\x07\u01B7\x02\x02\u0223\u0224\x07\u01CA" +
    "\x02\x02\u0224\u0225\x07\u01B3\x02\x02\u0225\u0228\x05(\x15\x02\u0226" +
    "\u0227\x07\x13\x02\x02\u0227\u0229\x05\u014A\xA6\x02\u0228\u0226\x03\x02" +
    "\x02\x02\u0228\u0229\x03\x02\x02\x02\u0229\u022C\x03\x02\x02\x02\u022A" +
    "\u022B\x07\u0139\x02\x02\u022B\u022D\x05\u014A\xA6\x02\u022C\u022A\x03" +
    "\x02\x02\x02\u022C\u022D\x03\x02\x02\x02\u022D\u024D\x03\x02\x02\x02\u022E" +
    "\u022F\x07\u01B7\x02\x02\u022F\u0230\t\b\x02\x02\u0230\u0231\x07\u01B3" +
    "\x02\x02\u0231\u0232\x05(\x15\x02\u0232\u0233\x07\u0139\x02\x02\u0233" +
    "\u0234\x05\u014A\xA6\x02\u0234\u024D\x03\x02\x02\x02\u0235\u0236\x07\u01B7" +
    "\x02\x02\u0236\u0237\x07\u01E8\x02\x02\u0237\u024D\x05(\x15\x02\u0238" +
    "\u0239\x07\u01B7\x02\x02\u0239\u023A\x07\u01C7\x02\x02\u023A\u023B\x07" +
    "\u01CA\x02\x02\u023B\u023C\x07\u01B3\x02\x02\u023C\u023D\x05(\x15\x02" +
    "\u023D\u023E\x07\u0139\x02\x02\u023E\u023F\x05\u014A\xA6\x02\u023F\u0240" +
    "\x07\u01D4\x02\x02\u0240\u0241\x05\u014A\xA6\x02\u0241\u024D\x03\x02\x02" +
    "\x02\u0242\u0243\x07\u01B7\x02\x02\u0243\u0244\x07\u01BD\x02\x02\u0244" +
    "\u0245\x07\u01CA\x02\x02\u0245\u0246\x07\u01B3\x02\x02\u0246\u0247\x05" +
    "(\x15\x02\u0247\u0248\x07\x94\x02\x02\u0248\u0249\x05\u014A\xA6\x02\u0249" +
    "\u024A\x07\x13\x02\x02\u024A\u024B\x05\u014A\xA6\x02\u024B\u024D\x03\x02" +
    "\x02\x02\u024C\u021A\x03\x02\x02\x02\u024C\u0222\x03\x02\x02\x02\u024C" +
    "\u022E\x03\x02\x02\x02\u024C\u0235\x03\x02\x02\x02\u024C\u0238\x03\x02" +
    "\x02\x02\u024C\u0242\x03\x02\x02\x02\u024D\'\x03\x02\x02\x02\u024E\u0250" +
    "\x07\u0218\x02\x02\u024F\u024E\x03\x02\x02\x02\u024F\u0250\x03\x02\x02" +
    "\x02\u0250\u0251\x03\x02\x02\x02\u0251\u0253\x05\u014A\xA6\x02\u0252\u024F" +
    "\x03\x02\x02\x02\u0253\u0254\x03\x02\x02\x02\u0254\u0252\x03\x02\x02\x02" +
    "\u0254\u0255\x03\x02\x02\x02\u0255)\x03\x02\x02\x02\u0256\u0259\x05,\x17" +
    "\x02\u0257\u0259\x05.\x18\x02\u0258\u0256\x03\x02\x02\x02\u0258\u0257" +
    "\x03\x02\x02\x02\u0259+\x03\x02\x02\x02\u025A\u025C\x07J\x02\x02\u025B" +
    "\u025D\x07\u01F3\x02\x02\u025C\u025B\x03\x02\x02\x02\u025C\u025D\x03\x02" +
    "\x02\x02\u025D\u025E\x03\x02\x02\x02\u025E\u0260\x07\u0177\x02\x02\u025F" +
    "\u0261\x05\u014E\xA8\x02\u0260\u025F\x03\x02\x02\x02\u0260\u0261\x03\x02" +
    "\x02\x02\u0261\u0262\x03\x02\x02\x02\u0262\u0263\x05\u0142\xA2\x02\u0263" +
    "\u0264\x07\u0206\x02\x02\u0264\u0269\x050\x19\x02\u0265\u0266\x07\u020A" +
    "\x02\x02\u0266\u0268\x050\x19\x02\u0267\u0265\x03\x02\x02\x02\u0268\u026B" +
    "\x03\x02\x02\x02\u0269\u0267\x03\x02\x02\x02\u0269\u026A\x03\x02\x02\x02" +
    "\u026A\u026E\x03\x02\x02\x02\u026B\u0269\x03\x02\x02\x02\u026C\u026D\x07" +
    "\u020A\x02\x02\u026D\u026F\x05T+\x02\u026E\u026C\x03\x02\x02\x02\u026E" +
    "\u026F\x03\x02\x02\x02\u026F\u0272\x03\x02\x02\x02\u0270\u0271\x07\u020A" +
    "\x02\x02\u0271\u0273\x05V,\x02\u0272\u0270\x03\x02\x02\x02\u0272\u0273" +
    "\x03\x02\x02\x02\u0273\u0276\x03\x02\x02\x02\u0274\u0275\x07\u020A\x02" +
    "\x02\u0275\u0277\x05Z.\x02\u0276\u0274\x03\x02\x02\x02\u0276\u0277\x03" +
    "\x02\x02\x02\u0277\u0278\x03\x02\x02\x02\u0278\u027A\x07\u0207\x02\x02" +
    "\u0279\u027B\x05J&\x02\u027A\u0279\x03\x02\x02\x02\u027A\u027B\x03\x02" +
    "\x02\x02\u027B\u027D\x03\x02\x02\x02\u027C\u027E\x05\\/\x02\u027D\u027C" +
    "\x03\x02\x02\x02\u027D\u027E\x03\x02\x02\x02\u027E\u027F\x03\x02\x02\x02" +
    "\u027F\u0281\x05\u014C\xA7\x02\u0280\u0282\x05d3\x02\u0281\u0280\x03\x02" +
    "\x02\x02\u0281\u0282\x03\x02\x02\x02\u0282-\x03\x02\x02\x02\u0283\u0284" +
    "\x07J\x02\x02\u0284\u0286\x07\u0177\x02\x02\u0285\u0287\x05\u014E\xA8" +
    "\x02\u0286\u0285\x03\x02\x02\x02\u0286\u0287\x03\x02\x02\x02\u0287\u0288" +
    "\x03\x02\x02\x02\u0288\u0289\x05\u0142\xA2\x02\u0289\u028C\x05\u014C\xA7" +
    "\x02\u028A\u028B\x07\x13\x02\x02\u028B\u028D\x05\xA0Q\x02\u028C\u028A" +
    "\x03\x02\x02\x02\u028C\u028D\x03\x02\x02\x02\u028D/\x03\x02\x02\x02\u028E" +
    "\u0292\x052\x1A\x02\u028F\u0292\x05L\'\x02\u0290\u0292\x05P)\x02\u0291" +
    "\u028E\x03\x02\x02\x02\u0291\u028F\x03\x02\x02\x02\u0291\u0290\x03\x02" +
    "\x02\x02\u02921\x03\x02\x02\x02\u0293\u0294\x054\x1B\x02\u0294\u0296\x05" +
    ":\x1E\x02\u0295\u0297\x05H%\x02\u0296\u0295\x03\x02\x02\x02\u0296\u0297" +
    "\x03\x02\x02\x02\u0297\u0299\x03\x02\x02\x02\u0298\u029A\x05J&\x02\u0299" +
    "\u0298\x03\x02\x02\x02\u0299\u029A\x03\x02\x02\x02\u029A3\x03\x02\x02" +
    "\x02\u029B\u029E\x05\u014A\xA6\x02\u029C\u029E\x05\u0104\x83\x02\u029D" +
    "\u029B\x03\x02\x02\x02\u029D\u029C\x03\x02\x02\x02\u029E5\x03\x02\x02" +
    "\x02\u029F\u02A2\x05\u014A\xA6\x02\u02A0\u02A2\x05\u0104\x83\x02\u02A1" +
    "\u029F\x03\x02\x02\x02\u02A1\u02A0\x03\x02\x02\x02\u02A27\x03\x02\x02" +
    "\x02\u02A3\u02A4\x07\u0206\x02\x02\u02A4\u02A9\x056\x1C\x02\u02A5\u02A6" +
    "\x07\u020A\x02\x02\u02A6\u02A8\x056\x1C\x02\u02A7\u02A5\x03\x02\x02\x02" +
    "\u02A8\u02AB\x03\x02\x02\x02\u02A9\u02A7\x03\x02\x02\x02\u02A9\u02AA\x03" +
    "\x02\x02\x02\u02AA\u02AC\x03\x02\x02\x02\u02AB\u02A9\x03\x02\x02\x02\u02AC" +
    "\u02AD\x07\u0207\x02\x02\u02AD9\x03\x02\x02\x02\u02AE\u02D4\t\t\x02\x02" +
    "\u02AF\u02B1\t\n\x02\x02\u02B0\u02B2\x05<\x1F\x02\u02B1\u02B0\x03\x02" +
    "\x02\x02\u02B1\u02B2\x03\x02\x02\x02\u02B2\u02D4\x03\x02\x02\x02\u02B3" +
    "\u02B5\x07\u017D\x02\x02\u02B4\u02B6\x05<\x1F\x02\u02B5\u02B4\x03\x02" +
    "\x02\x02\u02B5\u02B6\x03\x02\x02\x02\u02B6\u02BD\x03\x02\x02\x02\u02B7" +
    "\u02B9\t\v\x02\x02\u02B8\u02BA\x07\xD0\x02\x02\u02B9\u02B8\x03\x02\x02" +
    "\x02\u02B9\u02BA\x03\x02\x02\x02\u02BA\u02BB\x03\x02\x02\x02\u02BB\u02BC" +
    "\x07\u017C\x02\x02\u02BC\u02BE\x07\u01FA\x02\x02\u02BD\u02B7\x03\x02\x02" +
    "\x02\u02BD\u02BE\x03\x02\x02\x02\u02BE\u02D4\x03\x02\x02\x02\u02BF\u02C1" +
    "\t\f\x02\x02\u02C0\u02C2\x05> \x02\u02C1\u02C0\x03\x02\x02\x02\u02C1\u02C2" +
    "\x03\x02\x02\x02\u02C2\u02D4\x03\x02\x02\x02\u02C3\u02C5\t\r\x02\x02\u02C4" +
    "\u02C6\x05B\"\x02\u02C5\u02C4\x03\x02\x02\x02\u02C5\u02C6\x03\x02\x02" +
    "\x02\u02C6\u02D4\x03\x02\x02\x02\u02C7\u02C9\x07\u01D7\x02\x02\u02C8\u02CA" +
    "\x05D#\x02\u02C9\u02C8\x03\x02\x02\x02\u02C9\u02CA\x03\x02\x02\x02\u02CA" +
    "\u02D4\x03\x02\x02\x02\u02CB\u02CD\x07\u0143\x02\x02\u02CC\u02CE\x05F" +
    "$\x02\u02CD\u02CC\x03\x02\x02\x02\u02CD\u02CE\x03\x02\x02\x02\u02CE\u02D4" +
    "\x03\x02\x02\x02\u02CF\u02D1\x07\u0128\x02\x02\u02D0\u02D2\x05@!\x02\u02D1" +
    "\u02D0\x03\x02\x02\x02\u02D1\u02D2\x03\x02\x02\x02\u02D2\u02D4\x03\x02" +
    "\x02\x02\u02D3\u02AE\x03\x02\x02\x02\u02D3\u02AF\x03\x02\x02\x02\u02D3" +
    "\u02B3\x03\x02\x02\x02\u02D3\u02BF\x03\x02\x02\x02\u02D3\u02C3\x03\x02" +
    "\x02\x02\u02D3\u02C7\x03\x02\x02\x02\u02D3\u02CB\x03\x02\x02\x02\u02D3" +
    "\u02CF\x03\x02\x02\x02\u02D4;\x03\x02\x02\x02\u02D5\u02D6\x07\u0206\x02" +
    "\x02\u02D6\u02D7\x05\u016A\xB6\x02\u02D7\u02D8\x07\u0207\x02\x02\u02D8" +
    "=\x03\x02\x02\x02\u02D9\u02DA\x07\u0206\x02\x02\u02DA\u02DD\x05\u016A" +
    "\xB6\x02\u02DB\u02DC\x07\u020A\x02\x02\u02DC\u02DE\x05\u016A\xB6\x02\u02DD" +
    "\u02DB\x03\x02\x02\x02\u02DD\u02DE\x03\x02\x02\x02\u02DE\u02DF\x03\x02" +
    "\x02\x02\u02DF\u02E0\x07\u0207\x02\x02\u02E0?\x03\x02\x02\x02\u02E1\u02E2" +
    "\x07\u0206\x02\x02\u02E2\u02E5\x05\u0168\xB5\x02\u02E3\u02E4\x07\u020A" +
    "\x02\x02\u02E4\u02E6\x05\u0168\xB5\x02\u02E5\u02E3\x03\x02\x02\x02\u02E5" +
    "\u02E6\x03\x02\x02\x02\u02E6\u02E7\x03\x02\x02\x02\u02E7\u02E8\x07\u0207" +
    "\x02\x02\u02E8A\x03\x02\x02\x02\u02E9\u02EA\x07\u01FD\x02\x02\u02EA\u02EB" +
    "\x05:\x1E\x02\u02EB\u02EC\x07\u01FC\x02\x02\u02ECC\x03\x02\x02\x02\u02ED" +
    "\u02EE\x07\u01FD\x02\x02\u02EE\u02EF\x05:\x1E\x02\u02EF\u02F0\x07\u020A" +
    "\x02\x02\u02F0\u02F1\x05:\x1E\x02\u02F1\u02F2\x03\x02\x02\x02\u02F2\u02F3" +
    "\x07\u01FC\x02\x02\u02F3E\x03\x02\x02\x02\u02F4\u02F5\x07\u01FD\x02\x02" +
    "\u02F5\u02F6\x056\x1C\x02\u02F6\u02FD\x05:\x1E\x02\u02F7\u02F8\x07\u020A" +
    "\x02\x02\u02F8\u02F9\x056\x1C\x02\u02F9\u02FA\x05:\x1E\x02\u02FA\u02FC" +
    "\x03\x02\x02\x02\u02FB\u02F7\x03\x02\x02\x02\u02FC\u02FF\x03\x02\x02\x02" +
    "\u02FD\u02FB\x03\x02\x02\x02\u02FD\u02FE\x03\x02\x02\x02\u02FE\u0300\x03" +
    "\x02\x02\x02\u02FF\u02FD\x03\x02\x02\x02\u0300\u0301\x07\u01FC\x02\x02" +
    "\u0301G\x03\x02\x02\x02\u0302\u0303\x07B\x02\x02\u0303\u0305\x05X-\x02" +
    "\u0304\u0302\x03\x02\x02\x02\u0304\u0305\x03\x02\x02\x02\u0305\u0306\x03" +
    "\x02\x02\x02\u0306\u0307\x07\u0122\x02\x02\u0307\u030A\x07\u01D4\x02\x02" +
    "\u0308\u0309\x07\xF3\x02\x02\u0309\u030B\x07\x7F\x02\x02\u030A\u0308\x03" +
    "\x02\x02\x02\u030A\u030B\x03\x02\x02\x02\u030B\u0311\x03\x02\x02\x02\u030C" +
    "\u030E\x07\xF3\x02\x02\u030D\u030C\x03\x02\x02\x02\u030D\u030E\x03\x02" +
    "\x02\x02\u030E\u030F\x03\x02\x02\x02\u030F\u0311\x07\xF6\x02\x02\u0310" +
    "\u0304\x03\x02\x02\x02\u0310\u030D\x03\x02\x02\x02\u0311I\x03\x02\x02" +
    "\x02\u0312\u0313\x07=\x02\x02\u0313\u0314\x07\u021B\x02\x02\u0314K\x03" +
    "\x02\x02\x02\u0315\u0316\x054\x1B\x02\u0316\u0317\x05:\x1E\x02\u0317\u031A" +
    "\x07\xDC\x02\x02\u0318\u0319\x07\x99\x02\x02\u0319\u031B\x05N(\x02\u031A" +
    "\u0318\x03\x02\x02\x02\u031A\u031B\x03\x02\x02\x02\u031B\u031D\x03\x02";
FlinkSqlParser._serializedATNSegment2 = "\x02\x02\u031C\u031E\x07\u01A9\x02\x02\u031D\u031C\x03\x02\x02\x02\u031D" +
    "\u031E\x03\x02\x02\x02\u031EM\x03\x02\x02\x02\u031F\u0320\x07\u021B\x02" +
    "\x02\u0320O\x03\x02\x02\x02\u0321\u0322\x054\x1B\x02\u0322\u0323\x07\x13" +
    "\x02\x02\u0323\u0325\x05R*\x02\u0324\u0326\x05J&\x02\u0325\u0324\x03\x02" +
    "\x02\x02\u0325\u0326\x03\x02\x02\x02\u0326Q\x03\x02\x02\x02\u0327\u0328" +
    "\x05\u0104\x83\x02\u0328S\x03\x02\x02\x02\u0329\u032A\x07\u01AA\x02\x02" +
    "\u032A\u032B\x07\x94\x02\x02\u032B\u032C\x056\x1C\x02\u032C\u032D\x07" +
    "\x13\x02\x02\u032D\u032E\x05\u0104\x83\x02\u032EU\x03\x02\x02\x02\u032F" +
    "\u0330\x07B\x02\x02\u0330\u0332\x05X-\x02\u0331\u032F\x03\x02\x02\x02" +
    "\u0331\u0332\x03\x02\x02\x02\u0332\u0333\x03\x02\x02\x02\u0333\u0334\x07" +
    "\u0122\x02\x02\u0334\u0335\x07\u01D4\x02\x02\u0335\u0336\x058\x1D\x02" +
    "\u0336\u0337\x07\xF3\x02\x02\u0337\u0338\x07\x7F\x02\x02\u0338W\x03\x02" +
    "\x02\x02\u0339\u033A\x05\u0132\x9A\x02\u033AY\x03\x02\x02\x02\u033B\u033C" +
    "\x07\u0117\x02\x02\u033C\u033D\x07\x94\x02\x02\u033D\u033E\x07\u0175\x02" +
    "\x02\u033E[\x03\x02\x02\x02\u033F\u0340\x07\u010F\x02\x02\u0340\u0341" +
    "\x07$\x02\x02\u0341\u0342\x05^0\x02\u0342]\x03\x02\x02\x02\u0343\u0344" +
    "\x07\u0206\x02\x02\u0344\u0349\x05`1\x02\u0345\u0346\x07\u020A\x02\x02" +
    "\u0346\u0348\x05`1\x02\u0347\u0345\x03\x02\x02\x02\u0348\u034B\x03\x02" +
    "\x02\x02\u0349\u0347\x03\x02\x02\x02\u0349\u034A\x03\x02\x02\x02\u034A" +
    "\u034C\x03\x02\x02\x02\u034B\u0349\x03\x02\x02\x02\u034C\u034D\x07\u0207" +
    "\x02\x02\u034D_\x03\x02\x02\x02\u034E\u035C\x056\x1C\x02\u034F\u035C\x05" +
    "\u011A\x8E\x02\u0350\u0351\x07\u0206\x02\x02\u0351\u0356\x05b2\x02\u0352" +
    "\u0353\x07\u020A\x02\x02\u0353\u0355\x05b2\x02\u0354\u0352\x03\x02\x02" +
    "\x02\u0355\u0358\x03\x02\x02\x02\u0356\u0354\x03\x02\x02\x02\u0356\u0357" +
    "\x03\x02\x02\x02\u0357\u0359\x03\x02\x02\x02\u0358\u0356\x03\x02\x02\x02" +
    "\u0359\u035A\x07\u0207\x02\x02\u035A\u035C\x03\x02\x02\x02\u035B\u034E" +
    "\x03\x02\x02\x02\u035B\u034F\x03\x02\x02\x02\u035B\u0350\x03\x02\x02\x02" +
    "\u035Ca\x03\x02\x02\x02\u035D\u0360\x05\u011A\x8E\x02\u035E\u0360\x05" +
    "\u0164\xB3\x02\u035F\u035D\x03\x02\x02\x02\u035F\u035E\x03\x02\x02\x02" +
    "\u0360c\x03\x02\x02\x02\u0361\u0362\x07\xCC\x02\x02\u0362\u036B\x05\u0144" +
    "\xA3\x02\u0363\u0367\x07\u0206\x02\x02\u0364\u0366\x05f4\x02\u0365\u0364" +
    "\x03\x02\x02\x02\u0366\u0369\x03\x02\x02\x02\u0367\u0365\x03\x02\x02\x02" +
    "\u0367\u0368\x03\x02\x02\x02\u0368\u036A\x03\x02\x02\x02\u0369\u0367\x03" +
    "\x02\x02\x02\u036A\u036C\x07\u0207\x02\x02\u036B\u0363\x03\x02\x02\x02" +
    "\u036B\u036C\x03\x02\x02\x02\u036Ce\x03\x02\x02\x02\u036D\u036E\t\x0E" +
    "\x02\x02\u036E\u0372\t\x0F\x02\x02\u036F\u0370\t\x10\x02\x02\u0370\u0372" +
    "\t\x11\x02\x02\u0371\u036D\x03\x02\x02\x02\u0371\u036F\x03\x02\x02\x02" +
    "\u0372g\x03\x02\x02\x02\u0373\u0374\x07J\x02\x02\u0374\u0375\x07\u01BB" +
    "\x02\x02\u0375\u0376\x05\u013C\x9F\x02\u0376\u0377\x05\u014C\xA7\x02\u0377" +
    "i\x03\x02\x02\x02\u0378\u0379\x07J\x02\x02\u0379\u037B\x07\u01C1\x02\x02" +
    "\u037A\u037C\x05\u014E\xA8\x02\u037B\u037A\x03\x02\x02\x02\u037B\u037C" +
    "\x03\x02\x02\x02\u037C\u037D\x03\x02\x02\x02\u037D\u037F\x05\u0140\xA1" +
    "\x02\u037E\u0380\x05J&\x02\u037F\u037E\x03\x02\x02\x02\u037F\u0380\x03" +
    "\x02\x02\x02\u0380\u0381\x03\x02\x02\x02\u0381\u0382\x05\u014C\xA7\x02" +
    "\u0382k\x03\x02\x02\x02\u0383\u0385\x07J\x02\x02\u0384\u0386\x07\u01F3" +
    "\x02\x02\u0385\u0384\x03\x02\x02\x02\u0385\u0386\x03\x02\x02\x02\u0386" +
    "\u0387\x03\x02\x02\x02\u0387\u0389\x07\u01F7\x02\x02\u0388\u038A\x05\u014E" +
    "\xA8\x02\u0389\u0388\x03\x02\x02\x02\u0389\u038A\x03\x02\x02\x02\u038A" +
    "\u038B\x03\x02\x02\x02\u038B\u038D\x05\u0148\xA5\x02\u038C\u038E\x058" +
    "\x1D\x02\u038D\u038C\x03\x02\x02\x02\u038D\u038E\x03\x02\x02\x02\u038E" +
    "\u0390\x03\x02\x02\x02\u038F\u0391\x05J&\x02\u0390\u038F\x03\x02\x02\x02" +
    "\u0390\u0391\x03\x02\x02\x02\u0391\u0392\x03\x02\x02\x02\u0392\u0393\x07" +
    "\x13\x02\x02\u0393\u0394\x05\xA0Q\x02\u0394m\x03\x02\x02\x02\u0395\u0399" +
    "\x07J\x02\x02\u0396\u039A\x07\u01F3\x02\x02\u0397\u0398\x07\u01F3\x02" +
    "\x02\u0398\u039A\x07\u0174\x02\x02\u0399\u0396\x03\x02\x02\x02\u0399\u0397" +
    "\x03\x02\x02\x02\u0399\u039A\x03\x02\x02\x02\u039A\u039B\x03\x02\x02\x02" +
    "\u039B\u039D\x07\x9B\x02\x02\u039C\u039E\x05\u014E\xA8\x02\u039D\u039C" +
    "\x03\x02\x02\x02\u039D\u039E\x03\x02\x02\x02\u039E\u039F\x03\x02\x02\x02" +
    "\u039F\u03A0\x05\u0110\x89\x02\u03A0\u03A1\x07\x13\x02\x02\u03A1\u03A4" +
    "\x05\u0132\x9A\x02\u03A2\u03A3\x07\xC5\x02\x02\u03A3\u03A5\t\x12\x02\x02" +
    "\u03A4\u03A2\x03\x02\x02\x02\u03A4\u03A5\x03\x02\x02\x02\u03A5\u03A7\x03" +
    "\x02\x02\x02\u03A6\u03A8\x05p9\x02\u03A7\u03A6\x03\x02\x02\x02\u03A7\u03A8" +
    "\x03\x02\x02\x02\u03A8o\x03\x02\x02\x02\u03A9\u03AA\x07\u019E\x02\x02" +
    "\u03AA\u03AB\x07\u01D1\x02\x02\u03AB\u03B1\x05r:\x02\u03AC\u03AD\x07\u020A" +
    "\x02\x02\u03AD\u03AE\x07\u01D1\x02\x02\u03AE\u03B0\x05r:\x02\u03AF\u03AC" +
    "\x03\x02\x02\x02\u03B0\u03B3\x03\x02\x02\x02\u03B1\u03AF\x03\x02\x02\x02" +
    "\u03B1\u03B2\x03\x02\x02\x02\u03B2q\x03\x02\x02\x02\u03B3\u03B1\x03\x02" +
    "\x02\x02\u03B4\u03B5\x07\u021B\x02\x02\u03B5s\x03\x02\x02\x02\u03B6\u03B7" +
    "\x07\n\x02\x02\u03B7\u03B9\x07\u0177\x02\x02\u03B8\u03BA\x05\u0150\xA9" +
    "\x02\u03B9\u03B8\x03\x02\x02\x02\u03B9\u03BA\x03\x02\x02\x02\u03BA\u03BB" +
    "\x03\x02\x02\x02\u03BB\u03C1\x05\u0144\xA3\x02\u03BC\u03C2\x05v<\x02\u03BD" +
    "\u03C2\x05x=\x02\u03BE\u03C2\x05z>\x02\u03BF\u03C2\x05|?\x02\u03C0\u03C2" +
    "\x05~@\x02\u03C1\u03BC\x03\x02\x02\x02\u03C1\u03BD\x03\x02\x02\x02\u03C1" +
    "\u03BE\x03\x02\x02\x02\u03C1\u03BF\x03\x02\x02\x02\u03C1\u03C0\x03\x02" +
    "\x02\x02\u03C2u\x03\x02\x02\x02\u03C3\u03C5\x07\u0139\x02\x02\u03C4\u03C6" +
    "\x05\u014A\xA6\x02\u03C5\u03C4\x03\x02\x02\x02\u03C5\u03C6\x03\x02\x02" +
    "\x02\u03C6\u03C7\x03\x02\x02\x02\u03C7\u03C8\x07\u0186\x02\x02\u03C8\u03C9" +
    "\x05\u014A\xA6\x02\u03C9w\x03\x02\x02\x02\u03CA\u03CB\x07\u0156\x02\x02" +
    "\u03CB\u03CC\x05\u0152\xAA\x02\u03CCy\x03\x02\x02\x02\u03CD\u03CE\x07" +
    "\u01B7\x02\x02\u03CE\u03CF\x07B\x02\x02\u03CF\u03D0\x05X-\x02\u03D0\u03D1" +
    "\x07\u0122\x02\x02\u03D1\u03D2\x07\u01D4\x02\x02\u03D2\u03D4\x058\x1D" +
    "\x02\u03D3\u03D5\x05\x80A\x02\u03D4\u03D3\x03\x02\x02\x02\u03D4\u03D5" +
    "\x03\x02\x02\x02\u03D5{\x03\x02\x02\x02\u03D6\u03D7\x07v\x02\x02\u03D7" +
    "\u03D8\x07B\x02\x02\u03D8\u03D9\x05X-\x02\u03D9}\x03\x02\x02\x02\u03DA" +
    "\u03DB\x07\u01B7\x02\x02\u03DB\u03DC\x07\u0195\x02\x02\u03DC\u03DD\x05" +
    "8\x1D\x02\u03DD\x7F\x03\x02\x02\x02\u03DE\u03DF\x07\xF3\x02\x02\u03DF" +
    "\u03E0\x07\x7F\x02\x02\u03E0\x81\x03\x02\x02\x02\u03E1\u03E2\x07\n\x02" +
    "\x02\u03E2\u03E3\x07\u01F7\x02\x02\u03E3\u03E7\x05\u0146\xA4\x02\u03E4" +
    "\u03E8\x05v<\x02\u03E5\u03E6\x07\x13\x02\x02\u03E6\u03E8\x05\xA0Q\x02" +
    "\u03E7\u03E4\x03\x02\x02\x02\u03E7\u03E5\x03\x02\x02\x02\u03E8\x83\x03" +
    "\x02\x02\x02\u03E9\u03EA\x07\n\x02\x02\u03EA\u03EB\x07\u01C1\x02\x02\u03EB" +
    "\u03EC\x05\u013E\xA0\x02\u03EC\u03ED\x05x=\x02\u03ED\x85\x03\x02\x02\x02" +
    "\u03EE\u03F2\x07\n\x02\x02\u03EF\u03F3\x07\u01F3\x02\x02\u03F0\u03F1\x07" +
    "\u01F3\x02\x02\u03F1\u03F3\x07\u0174\x02\x02\u03F2\u03EF\x03\x02\x02\x02" +
    "\u03F2\u03F0\x03\x02\x02\x02\u03F2\u03F3\x03\x02\x02\x02\u03F3\u03F4\x03" +
    "\x02\x02\x02\u03F4\u03F6\x07\x9B\x02\x02\u03F5\u03F7\x05\u0150\xA9\x02" +
    "\u03F6\u03F5\x03\x02\x02\x02\u03F6\u03F7\x03\x02\x02\x02\u03F7\u03F8\x03" +
    "\x02\x02\x02\u03F8\u03F9\x05\u0112\x8A\x02\u03F9\u03FA\x07\x13\x02\x02" +
    "\u03FA\u03FD\x05\u0132\x9A\x02\u03FB\u03FC\x07\xC5\x02\x02\u03FC\u03FE" +
    "\t\x12\x02\x02\u03FD\u03FB\x03\x02\x02\x02\u03FD\u03FE\x03\x02\x02\x02" +
    "\u03FE\x87\x03\x02\x02\x02\u03FF\u0400\x07v\x02\x02\u0400\u0402\x07\u01BB" +
    "\x02\x02\u0401\u0403\x05\u0150\xA9\x02\u0402\u0401\x03\x02\x02\x02\u0402" +
    "\u0403\x03\x02\x02\x02\u0403\u0404\x03\x02\x02\x02\u0404\u0405\x05\u013A" +
    "\x9E\x02\u0405\x89\x03\x02\x02\x02\u0406\u0408\x07v\x02\x02\u0407\u0409" +
    "\x07\u01F3\x02\x02\u0408\u0407\x03\x02\x02\x02\u0408\u0409\x03\x02\x02" +
    "\x02\u0409\u040A\x03\x02\x02\x02\u040A\u040C\x07\u0177\x02\x02\u040B\u040D" +
    "\x05\u0150\xA9\x02\u040C\u040B\x03\x02\x02\x02\u040C\u040D\x03\x02\x02" +
    "\x02\u040D\u040E\x03\x02\x02\x02\u040E\u040F\x05\u0144\xA3\x02\u040F\x8B" +
    "\x03\x02\x02\x02\u0410\u0411\x07v\x02\x02\u0411\u0413\x07\u01C1\x02\x02" +
    "\u0412\u0414\x05\u0150\xA9\x02\u0413\u0412\x03\x02\x02\x02\u0413\u0414" +
    "\x03\x02\x02\x02\u0414\u0415\x03\x02\x02\x02\u0415\u0417\x05\u013E\xA0" +
    "\x02\u0416\u0418\t\x13\x02\x02\u0417\u0416\x03\x02\x02\x02\u0417\u0418" +
    "\x03\x02\x02\x02\u0418\x8D\x03\x02\x02\x02\u0419\u041B\x07v\x02\x02\u041A" +
    "\u041C\x07\u01F3\x02\x02\u041B\u041A\x03\x02\x02\x02\u041B\u041C\x03\x02" +
    "\x02\x02\u041C\u041D\x03\x02\x02\x02\u041D\u041F\x07\u01F7\x02\x02\u041E" +
    "\u0420\x05\u0150\xA9\x02\u041F\u041E\x03\x02\x02\x02\u041F\u0420\x03\x02" +
    "\x02\x02\u0420\u0421\x03\x02\x02\x02\u0421\u0422\x05\u0146\xA4\x02\u0422" +
    "\x8F\x03\x02\x02\x02\u0423\u0427\x07v\x02\x02\u0424\u0428\x07\u01F3\x02" +
    "\x02\u0425\u0426\x07\u01F3\x02\x02\u0426\u0428\x07\u0174\x02\x02\u0427" +
    "\u0424\x03\x02\x02\x02\u0427\u0425\x03\x02\x02\x02\u0427\u0428\x03\x02" +
    "\x02\x02\u0428\u0429\x03\x02\x02\x02\u0429\u042B\x07\x9B\x02\x02\u042A" +
    "\u042C\x05\u0150\xA9\x02\u042B\u042A\x03\x02\x02\x02\u042B\u042C\x03\x02" +
    "\x02\x02\u042C\u042D\x03\x02\x02\x02\u042D\u042E\x05\u0112\x8A\x02\u042E" +
    "\x91\x03\x02\x02\x02\u042F\u0431\x07\x86\x02\x02\u0430\u042F\x03\x02\x02" +
    "\x02\u0430\u0431\x03\x02\x02\x02\u0431\u0432\x03\x02\x02\x02\u0432\u0437" +
    "\x05\x94K\x02\u0433\u0437\x05\x9CO\x02\u0434\u0435\x07\x86\x02\x02\u0435" +
    "\u0437\x05\x9EP\x02\u0436\u0430\x03\x02\x02\x02\u0436\u0433\x03\x02\x02" +
    "\x02\u0436\u0434\x03\x02\x02\x02\u0437\x93\x03\x02\x02\x02\u0438\u0439" +
    "\x07\xB3\x02\x02\u0439\u043A\t\x14\x02\x02\u043A\u0443\x05\u0144\xA3\x02" +
    "\u043B\u043D\x05\x96L\x02\u043C\u043B\x03\x02\x02\x02\u043C\u043D\x03" +
    "\x02\x02\x02\u043D\u043F\x03\x02\x02\x02\u043E\u0440\x058\x1D\x02\u043F" +
    "\u043E\x03\x02\x02\x02\u043F\u0440\x03\x02\x02\x02\u0440\u0441\x03\x02" +
    "\x02\x02\u0441\u0444\x05\xA0Q\x02\u0442\u0444\x05\x98M\x02\u0443\u043C" +
    "\x03\x02\x02\x02\u0443\u0442\x03\x02\x02\x02\u0444\x95\x03\x02\x02\x02" +
    "\u0445\u0446\x07\u010E\x02\x02\u0446\u0447\x05\u0152\xAA\x02\u0447\x97" +
    "\x03\x02\x02\x02\u0448\u0449\x07\u01A0\x02\x02\u0449\u044E\x05\x9AN\x02" +
    "\u044A\u044B\x07\u020A\x02\x02\u044B\u044D\x05\x9AN\x02\u044C\u044A\x03" +
    "\x02\x02\x02\u044D\u0450\x03\x02\x02\x02\u044E\u044C\x03\x02\x02\x02\u044E" +
    "\u044F\x03\x02\x02\x02\u044F\x99\x03\x02\x02\x02\u0450\u044E\x03\x02\x02" +
    "\x02\u0451\u0452\x07\u0206\x02\x02\u0452\u0457\x05\u0164\xB3\x02\u0453" +
    "\u0454\x07\u020A\x02\x02\u0454\u0456\x05\u0164\xB3\x02\u0455\u0453\x03" +
    "\x02\x02\x02\u0456\u0459\x03\x02\x02\x02\u0457\u0455\x03\x02\x02\x02\u0457" +
    "\u0458\x03\x02\x02\x02\u0458\u045A\x03\x02\x02\x02\u0459\u0457\x03\x02" +
    "\x02\x02\u045A\u045B\x07\u0207\x02\x02\u045B\x9B\x03\x02\x02\x02\u045C" +
    "\u045D\x07\x1A\x02\x02\u045D\u045E\x07\u0164\x02\x02\u045E\u045F\x07\u0156" +
    "\x02\x02\u045F\u0463\x07\u020B\x02\x02\u0460\u0461\x05\x94K\x02\u0461" +
    "\u0462\x07\u020B\x02\x02\u0462\u0464\x03\x02\x02\x02\u0463\u0460\x03\x02" +
    "\x02\x02\u0464\u0465\x03\x02\x02\x02\u0465\u0463\x03\x02\x02\x02\u0465" +
    "\u0466\x03\x02\x02\x02\u0466\u0467\x03\x02\x02\x02\u0467\u0468\x07|\x02" +
    "\x02\u0468\x9D\x03\x02\x02\x02\u0469\u046A\x07\u0164\x02\x02\u046A\u046B" +
    "\x07\u0156\x02\x02\u046B\u046F\x07\x1A\x02\x02\u046C\u046D\x05\x94K\x02" +
    "\u046D\u046E\x07\u020B\x02\x02\u046E\u0470\x03\x02\x02\x02\u046F\u046C" +
    "\x03\x02\x02\x02\u0470\u0471\x03\x02\x02\x02\u0471\u046F\x03\x02\x02\x02" +
    "\u0471\u0472\x03\x02\x02\x02\u0472\u0473\x03\x02\x02\x02\u0473\u0474\x07" +
    "|\x02\x02\u0474\x9F\x03\x02\x02\x02\u0475\u0476\bQ\x01\x02\u0476\u048D" +
    "\x05\xA2R\x02\u0477\u0478\x05\xA4S\x02\u0478\u0479\x05\xA0Q\x07\u0479" +
    "\u048D\x03\x02\x02\x02\u047A\u047B\x07\u0206\x02\x02\u047B\u047C\x05\xA0" +
    "Q\x02\u047C\u047D\x07\u0207\x02\x02\u047D\u048D\x03\x02\x02\x02\u047E" +
    "\u0480\x05\xACW\x02\u047F\u0481\x05\xE8u\x02\u0480\u047F\x03\x02\x02\x02" +
    "\u0480\u0481\x03\x02\x02\x02\u0481\u0483\x03\x02\x02\x02\u0482\u0484\x05" +
    "\xECw\x02\u0483\u0482\x03\x02\x02\x02\u0483\u0484\x03\x02\x02\x02\u0484" +
    "\u048D\x03\x02\x02\x02\u0485\u0487\x05\xAAV\x02\u0486\u0488\x05\xE8u\x02" +
    "\u0487\u0486\x03\x02\x02\x02\u0487\u0488\x03\x02\x02\x02\u0488\u048A\x03" +
    "\x02\x02\x02\u0489\u048B\x05\xECw\x02\u048A\u0489\x03\x02\x02\x02\u048A" +
    "\u048B\x03\x02\x02\x02\u048B\u048D\x03\x02\x02\x02\u048C\u0475\x03\x02" +
    "\x02\x02\u048C\u0477\x03\x02\x02\x02\u048C\u047A\x03\x02\x02\x02\u048C" +
    "\u047E\x03\x02\x02\x02\u048C\u0485\x03\x02\x02\x02\u048D\u049C\x03\x02" +
    "\x02\x02\u048E\u048F\f\x05\x02\x02\u048F\u0491\t\x15\x02\x02\u0490\u0492" +
    "\x07\x07\x02\x02\u0491\u0490\x03\x02\x02\x02\u0491\u0492\x03\x02\x02\x02" +
    "\u0492\u0493\x03\x02\x02\x02\u0493\u0495\x05\xA0Q\x02\u0494\u0496\x05" +
    "\xE8u\x02\u0495\u0494\x03\x02\x02\x02\u0495\u0496\x03\x02\x02\x02\u0496" +
    "\u0498\x03\x02\x02\x02\u0497\u0499\x05\xECw\x02\u0498\u0497\x03\x02\x02" +
    "\x02\u0498\u0499\x03\x02\x02\x02\u0499\u049B\x03\x02\x02\x02\u049A\u048E" +
    "\x03\x02\x02\x02\u049B\u049E\x03\x02\x02\x02\u049C\u049A\x03\x02\x02\x02" +
    "\u049C\u049D\x03\x02\x02\x02\u049D\xA1\x03\x02\x02\x02\u049E\u049C\x03" +
    "\x02\x02\x02\u049F\u04A0\x07\u01A0\x02\x02\u04A0\u04A5\x05\u0104\x83\x02" +
    "\u04A1\u04A2\x07\u020A\x02\x02\u04A2\u04A4\x05\u0104\x83\x02\u04A3\u04A1" +
    "\x03\x02\x02\x02\u04A4\u04A7\x03\x02\x02\x02\u04A5\u04A3\x03\x02\x02\x02" +
    "\u04A5\u04A6\x03\x02\x02\x02\u04A6\xA3\x03\x02\x02\x02\u04A7\u04A5\x03" +
    "\x02\x02\x02\u04A8\u04A9\x07\u01B3\x02\x02\u04A9\u04AE\x05\xA6T\x02\u04AA" +
    "\u04AB\x07\u020A\x02\x02\u04AB\u04AD\x05\xA6T\x02\u04AC\u04AA\x03\x02" +
    "\x02\x02\u04AD\u04B0\x03\x02\x02\x02\u04AE\u04AC\x03\x02\x02\x02\u04AE" +
    "\u04AF\x03\x02\x02\x02\u04AF\xA5\x03\x02\x02\x02\u04B0\u04AE\x03\x02\x02" +
    "\x02\u04B1\u04BD\x05\xA8U\x02\u04B2\u04B3\x07\u0206\x02\x02\u04B3\u04B8" +
    "\x056\x1C\x02\u04B4\u04B5\x07\u020A\x02\x02\u04B5\u04B7\x056\x1C\x02\u04B6" +
    "\u04B4\x03\x02\x02\x02\u04B7\u04BA\x03\x02\x02\x02\u04B8\u04B6\x03\x02" +
    "\x02\x02\u04B8\u04B9\x03\x02\x02\x02\u04B9\u04BB\x03\x02\x02\x02\u04BA" +
    "\u04B8\x03\x02\x02\x02\u04BB\u04BC\x07\u0207\x02\x02\u04BC\u04BE\x03\x02" +
    "\x02\x02\u04BD\u04B2\x03\x02\x02\x02\u04BD\u04BE\x03\x02\x02\x02\u04BE" +
    "\u04BF\x03\x02\x02\x02\u04BF\u04C0\x07\x13\x02\x02\u04C0\u04C1\x07\u0206" +
    "\x02\x02\u04C1\u04C2\x05\xA0Q\x02\u04C2\u04C3\x07\u0207\x02\x02\u04C3" +
    "\xA7\x03\x02\x02\x02\u04C4\u04C5\x05\u0132\x9A\x02\u04C5\xA9\x03\x02\x02" +
    "\x02\u04C6\u04C8\x05\xACW\x02\u04C7\u04C9\x05\xB2Z\x02\u04C8\u04C7\x03" +
    "\x02\x02\x02\u04C8\u04C9\x03\x02\x02\x02\u04C9\u04CB\x03\x02\x02\x02\u04CA" +
    "\u04CC\x05\xCEh\x02\u04CB\u04CA\x03\x02\x02\x02\u04CB\u04CC\x03\x02\x02" +
    "\x02\u04CC\u04CE\x03\x02\x02\x02\u04CD\u04CF\x05\xD0i\x02\u04CE\u04CD" +
    "\x03\x02\x02\x02\u04CE\u04CF\x03\x02\x02\x02\u04CF\u04D1\x03\x02\x02\x02" +
    "\u04D0\u04D2\x05\xDEp\x02\u04D1\u04D0\x03\x02\x02\x02\u04D1\u04D2\x03" +
    "\x02\x02\x02\u04D2\u04D4\x03\x02\x02\x02\u04D3\u04D5\x05\xE0q\x02\u04D4" +
    "\u04D3\x03\x02\x02\x02\u04D4\u04D5\x03\x02\x02\x02\u04D5\u04DB\x03\x02" +
    "\x02\x02\u04D6\u04D7\x05\xACW\x02\u04D7\u04D8\x05\xB2Z\x02\u04D8\u04D9" +
    "\x05\xE6t\x02\u04D9\u04DB\x03\x02\x02\x02\u04DA\u04C6\x03\x02\x02\x02" +
    "\u04DA\u04D6\x03\x02\x02\x02\u04DB\xAB\x03\x02\x02\x02\u04DC\u04DE\x07" +
    "\u0152\x02\x02\u04DD\u04DF\x05\u016E\xB8\x02\u04DE\u04DD\x03\x02\x02\x02" +
    "\u04DE\u04DF\x03\x02\x02\x02\u04DF\u04E9\x03\x02\x02\x02\u04E0\u04EA\x07" +
    "\u0211\x02\x02\u04E1\u04E6\x05\xAEX\x02\u04E2\u04E3\x07\u020A\x02\x02" +
    "\u04E3\u04E5\x05\xAEX\x02\u04E4\u04E2\x03\x02\x02\x02\u04E5\u04E8\x03" +
    "\x02\x02\x02\u04E6\u04E4\x03\x02\x02\x02\u04E6\u04E7\x03\x02\x02\x02\u04E7" +
    "\u04EA\x03\x02\x02\x02\u04E8\u04E6\x03\x02\x02\x02\u04E9\u04E0\x03\x02" +
    "\x02\x02\u04E9\u04E1\x03\x02\x02\x02\u04EA\xAD\x03\x02\x02\x02\u04EB\u04F4" +
    "\x05\xB0Y\x02\u04EC\u04F1\x056\x1C\x02\u04ED\u04EF\x07\x13\x02\x02\u04EE" +
    "\u04ED\x03\x02\x02\x02\u04EE\u04EF\x03\x02\x02\x02\u04EF\u04F0\x03\x02" +
    "\x02\x02\u04F0\u04F2\x05\u0104\x83\x02\u04F1\u04EE\x03\x02\x02\x02\u04F1" +
    "\u04F2\x03\x02\x02\x02\u04F2\u04F4\x03\x02\x02\x02\u04F3\u04EB\x03\x02" +
    "\x02\x02\u04F3\u04EC\x03\x02\x02\x02\u04F4\xAF\x03\x02\x02\x02\u04F5\u04F6" +
    "\x05\u010E\x88\x02\u04F6\u04F7\x07\u0108\x02\x02\u04F7\u04F8\x05\xE4s" +
    "\x02\u04F8\u04F9\x07\x13\x02\x02\u04F9\u04FA\x05\u0132\x9A\x02\u04FA\u0502" +
    "\x03\x02\x02\x02\u04FB\u04FC\x05\u010E\x88\x02\u04FC\u04FD\x07\u0108\x02" +
    "\x02\u04FD\u04FE\x05\u012A\x96\x02\u04FE\u04FF\x07\x13\x02\x02\u04FF\u0500" +
    "\x05\u0132\x9A\x02\u0500\u0502\x03\x02\x02\x02\u0501\u04F5\x03\x02\x02" +
    "\x02\u0501\u04FB\x03\x02\x02\x02\u0502\xB1\x03\x02\x02\x02\u0503\u0504" +
    "\x07\x99\x02\x02\u0504\u0505\x05\xB4[\x02\u0505\xB3\x03\x02\x02\x02\u0506" +
    "\u0507\b[\x01\x02\u0507\u050C\x05\xB6\\\x02\u0508\u0509\x07\u020A\x02" +
    "\x02\u0509\u050B\x05\xB6\\\x02\u050A\u0508\x03\x02\x02\x02\u050B\u050E" +
    "\x03\x02\x02\x02\u050C\u050A\x03\x02\x02\x02\u050C\u050D\x03\x02\x02\x02" +
    "\u050D\u0512\x03\x02\x02\x02\u050E\u050C\x03\x02\x02\x02\u050F\u0512\x05" +
    "\xBE`\x02\u0510\u0512\x05\xC0a\x02\u0511\u0506\x03\x02\x02\x02\u0511\u050F" +
    "\x03\x02\x02\x02\u0511\u0510\x03\x02\x02\x02\u0512\u0528\x03\x02\x02\x02" +
    "\u0513\u0514\f\x05\x02\x02\u0514\u0515\x07K\x02\x02\u0515\u0516\x07\xBB" +
    "\x02\x02\u0516\u0527\x05\xB4[\x06\u0517\u0519\f\x06\x02\x02\u0518\u051A" +
    "\x07\xEB\x02\x02\u0519\u0518\x03\x02\x02\x02\u0519\u051A\x03\x02\x02\x02" +
    "\u051A\u051C\x03\x02\x02\x02\u051B\u051D\t\x16\x02\x02\u051C\u051B\x03" +
    "\x02\x02\x02\u051C\u051D\x03\x02\x02\x02\u051D\u051F\x03\x02\x02\x02\u051E" +
    "\u0520\x07\u0107\x02\x02\u051F\u051E\x03\x02\x02\x02\u051F\u0520\x03\x02" +
    "\x02\x02\u0520\u0521\x03\x02\x02\x02\u0521\u0522\x07\xBB\x02\x02\u0522" +
    "\u0524\x05\xB4[\x02\u0523\u0525\x05\xCCg\x02\u0524\u0523\x03\x02\x02\x02" +
    "\u0524\u0525\x03\x02\x02\x02\u0525\u0527\x03\x02\x02\x02\u0526\u0513\x03" +
    "\x02\x02\x02\u0526\u0517\x03\x02\x02\x02\u0527\u052A\x03\x02\x02\x02\u0528" +
    "\u0526\x03\x02\x02\x02\u0528\u0529\x03\x02\x02\x02\u0529\xB5\x03\x02\x02" +
    "\x02\u052A\u0528\x03\x02\x02\x02\u052B\u052D\x05\xB8]\x02\u052C\u052E" +
    "\x05\u0128\x95\x02\u052D\u052C\x03\x02\x02\x02\u052D\u052E\x03\x02\x02" +
    "\x02\u052E\xB7\x03\x02\x02\x02\u052F\u0531\x07\u0177\x02\x02\u0530\u052F" +
    "\x03\x02\x02\x02\u0530\u0531\x03\x02\x02\x02\u0531\u0532\x03\x02\x02\x02" +
    "\u0532\u0534\x05\u0144\xA3\x02\u0533\u0535\x05\xBA^\x02\u0534\u0533\x03" +
    "\x02\x02\x02\u0534\u0535\x03\x02\x02\x02\u0535\u053A\x03\x02\x02\x02\u0536" +
    "\u0538\x07\x13\x02\x02\u0537\u0536\x03\x02\x02\x02\u0537\u0538\x03\x02" +
    "\x02\x02\u0538\u0539\x03\x02\x02\x02\u0539\u053B\x05\u0118\x8D\x02\u053A" +
    "\u0537\x03\x02\x02\x02\u053A\u053B\x03\x02\x02\x02\u053B\u0563\x03\x02" +
    "\x02\x02\u053C\u053E\x05\u0146\xA4\x02\u053D\u053F\x05\xBA^\x02\u053E" +
    "\u053D\x03\x02\x02\x02\u053E\u053F\x03\x02\x02\x02\u053F\u0544\x03\x02" +
    "\x02\x02\u0540\u0542\x07\x13\x02\x02\u0541\u0540\x03\x02\x02\x02\u0541" +
    "\u0542\x03\x02\x02\x02\u0542\u0543\x03\x02\x02\x02\u0543\u0545\x05\u0118" +
    "\x8D\x02\u0544\u0541\x03\x02\x02\x02\u0544\u0545\x03\x02\x02\x02\u0545" +
    "\u0563\x03\x02\x02\x02\u0546\u0547\x07\xC8\x02\x02\u0547\u0548\x07\u0177" +
    "\x02\x02\u0548\u0549\x07\u0206\x02\x02\u0549\u054A\x05\u0112\x8A\x02\u054A" +
    "\u054B\x07\u0206\x02\x02\u054B\u0550\x05\u0114\x8B\x02\u054C\u054D\x07" +
    "\u020A\x02\x02\u054D\u054F\x05\u0114\x8B\x02\u054E\u054C\x03\x02\x02\x02" +
    "\u054F\u0552\x03\x02\x02\x02\u0550\u054E\x03\x02\x02\x02\u0550\u0551\x03" +
    "\x02\x02\x02\u0551\u0553\x03\x02\x02\x02\u0552\u0550\x03\x02\x02\x02\u0553" +
    "\u0554\x07\u0207\x02\x02\u0554\u0555\x07\u0207\x02\x02\u0555\u0563\x03" +
    "\x02\x02\x02\u0556\u0558\x07\xC8\x02\x02\u0557\u0556\x03\x02\x02\x02\u0557" +
    "\u0558\x03\x02\x02\x02\u0558\u0559\x03\x02\x02\x02\u0559\u055A\x07\u0206" +
    "\x02\x02\u055A\u055B\x05\xA0Q\x02\u055B\u055C\x07\u0207\x02\x02\u055C" +
    "\u0563\x03\x02\x02\x02\u055D\u055E\x07\u0197\x02\x02\u055E\u055F\x07\u0206" +
    "\x02\x02\u055F\u0560\x05\u0104\x83\x02\u0560\u0561\x07\u0207\x02\x02\u0561" +
    "\u0563\x03\x02\x02\x02\u0562\u0530\x03\x02\x02\x02\u0562\u053C\x03\x02" +
    "\x02\x02\u0562\u0546\x03\x02\x02\x02\u0562\u0557\x03\x02\x02\x02\u0562" +
    "\u055D\x03\x02\x02\x02\u0563\xB9\x03\x02\x02\x02\u0564\u0565\x07\x94\x02" +
    "\x02\u0565\u0566\x07\u0175\x02\x02\u0566\u0567\x07\x13\x02\x02\u0567\u0568" +
    "\x07\xFB\x02\x02\u0568\u0569\x05\xBC_\x02\u0569\xBB\x03\x02\x02\x02\u056A" +
    "\u056B\x05\u0104\x83\x02\u056B\xBD\x03\x02\x02\x02\u056C\u056D\x07\u0206" +
    "\x02\x02\u056D\u056E\x05\x98M\x02\u056E\u056F\x07\u0207\x02\x02\u056F" +
    "\u0570\x05\u0128\x95\x02\u0570\xBF\x03\x02\x02\x02\u0571\u0572\x07\u0177" +
    "\x02\x02\u0572\u0573\x07\u0206\x02\x02\u0573\u0574\x05\xC2b\x02\u0574" +
    "\u0575\x07\u0207\x02\x02\u0575\xC1\x03\x02\x02\x02\u0576\u0577\x05\xC4" +
    "c\x02\u0577\u0578\x07\u0206\x02\x02\u0578\u057D\x05\xC6d\x02\u0579\u057A" +
    "\x07\u020A\x02\x02\u057A\u057C\x05\xC6d\x02\u057B\u0579\x03\x02\x02\x02" +
    "\u057C\u057F\x03\x02\x02\x02\u057D\u057B\x03\x02\x02\x02\u057D\u057E\x03" +
    "\x02\x02\x02\u057E\u0580\x03\x02\x02\x02\u057F\u057D\x03\x02\x02\x02\u0580" +
    "\u0581\x07\u0207\x02\x02\u0581\xC3\x03\x02\x02\x02\u0582\u0583\t\x17\x02" +
    "\x02\u0583\xC5\x03\x02\x02\x02\u0584\u0585\x07\u0177\x02\x02\u0585\u0594" +
    "\x05\xDCo\x02\u0586\u0594\x05\xCAf\x02\u0587\u0594\x05\u011C\x8F\x02\u0588" +
    "\u0589\x07\u01C0\x02\x02\u0589\u058A\x07\u021A\x02\x02\u058A\u058B\x07" +
    "\u0177\x02\x02\u058B\u0594\x05\xDCo\x02\u058C\u058D\x07\u01F4\x02\x02" +
    "\u058D\u058E\x07\u021A\x02\x02\u058E\u0594\x05\xCAf\x02\u058F\u0590\x05" +
    "\xC8e\x02\u0590\u0591\x07\u021A\x02\x02\u0591\u0592\x05\u011C\x8F\x02" +
    "\u0592\u0594\x03\x02\x02\x02\u0593\u0584\x03\x02\x02\x02\u0593\u0586\x03" +
    "\x02\x02\x02\u0593\u0587\x03\x02\x02\x02\u0593\u0588\x03\x02\x02\x02\u0593" +
    "\u058C\x03\x02\x02\x02\u0593\u058F\x03\x02\x02\x02\u0594\xC7\x03\x02\x02" +
    "\x02\u0595\u0596\t\x18\x02\x02\u0596\xC9\x03\x02\x02\x02\u0597\u0598\x07" +
    "\u01C5\x02\x02\u0598\u0599\x07\u0206\x02\x02\u0599\u059A\x056\x1C\x02" +
    "\u059A\u059B\x07\u0207\x02\x02\u059B\xCB\x03\x02\x02\x02\u059C\u059D\x07" +
    "\xFF\x02\x02\u059D\u05A1\x05\u0106\x84\x02\u059E\u059F\x07\u019E\x02\x02" +
    "\u059F\u05A1\x058\x1D\x02\u05A0\u059C\x03\x02\x02\x02\u05A0\u059E\x03" +
    "\x02\x02\x02\u05A1\xCD\x03\x02\x02\x02\u05A2\u05A3\x07\u01B0\x02\x02\u05A3" +
    "\u05A4\x05\u0106\x84\x02\u05A4\xCF\x03\x02\x02\x02\u05A5\u05A6\x07\xA1" +
    "\x02\x02\u05A6\u05A7\x07$\x02\x02\u05A7\u05AC\x05\xD2j\x02\u05A8\u05A9" +
    "\x07\u020A\x02\x02\u05A9\u05AB\x05\xD2j\x02\u05AA\u05A8\x03\x02\x02\x02" +
    "\u05AB\u05AE\x03\x02\x02\x02\u05AC\u05AA\x03\x02\x02\x02\u05AC\u05AD\x03" +
    "\x02\x02\x02\u05AD\xD1\x03\x02\x02\x02\u05AE\u05AC\x03\x02\x02\x02\u05AF" +
    "\u05D7\x056\x1C\x02\u05B0\u05D7\x05\xD8m\x02\u05B1\u05B2\x07\u0206\x02" +
    "\x02\u05B2\u05D7\x07\u0207\x02\x02\u05B3\u05B4\x07\u0206\x02\x02\u05B4" +
    "\u05B9\x05\u0104\x83\x02\u05B5\u05B6\x07\u020A\x02\x02\u05B6\u05B8\x05" +
    "\u0104\x83\x02\u05B7\u05B5\x03\x02\x02\x02\u05B8\u05BB\x03\x02\x02\x02" +
    "\u05B9\u05B7\x03\x02\x02\x02\u05B9\u05BA\x03\x02\x02\x02\u05BA\u05BC\x03" +
    "\x02\x02\x02\u05BB\u05B9\x03\x02\x02\x02\u05BC\u05BD\x07\u0207\x02\x02" +
    "\u05BD\u05D7\x03\x02\x02\x02\u05BE\u05BF\x05\xD6l\x02\u05BF\u05C0\x07" +
    "\u0206\x02\x02\u05C0\u05C5\x05\u0104\x83\x02\u05C1\u05C2\x07\u020A\x02" +
    "\x02\u05C2\u05C4\x05\u0104\x83\x02\u05C3\u05C1\x03\x02\x02\x02\u05C4\u05C7" +
    "\x03\x02\x02\x02\u05C5\u05C3\x03\x02\x02\x02\u05C5\u05C6\x03\x02\x02\x02" +
    "\u05C6\u05C8\x03\x02\x02\x02\u05C7\u05C5\x03\x02\x02\x02\u05C8\u05C9\x07" +
    "\u0207\x02\x02\u05C9\u05D7\x03\x02\x02\x02\u05CA\u05CB\x05\xD4k\x02\u05CB" +
    "\u05CC\x07\u0206\x02\x02\u05CC\u05D1\x05\xD2j\x02\u05CD\u05CE\x07\u020A" +
    "\x02\x02\u05CE\u05D0\x05\xD2j\x02\u05CF\u05CD\x03\x02\x02\x02\u05D0\u05D3" +
    "\x03\x02\x02\x02\u05D1\u05CF\x03\x02\x02\x02\u05D1\u05D2\x03\x02\x02\x02" +
    "\u05D2\u05D4\x03\x02\x02\x02\u05D3\u05D1\x03\x02\x02\x02\u05D4\u05D5\x07" +
    "\u0207\x02\x02\u05D5\u05D7\x03\x02\x02\x02\u05D6\u05AF\x03\x02\x02\x02" +
    "\u05D6\u05B0\x03\x02\x02\x02\u05D6\u05B1\x03\x02\x02\x02\u05D6\u05B3\x03" +
    "\x02\x02\x02\u05D6\u05BE\x03\x02\x02\x02\u05D6\u05CA\x03\x02\x02\x02\u05D7" +
    "\xD3\x03\x02\x02\x02\u05D8\u05D9\x07\xA2\x02\x02\u05D9\u05DA\x07\u01EF" +
    "\x02\x02\u05DA\xD5\x03\x02\x02\x02\u05DB\u05DC\t\x19\x02\x02\u05DC\xD7" +
    "\x03\x02\x02\x02\u05DD\u05DE\x05\xDAn\x02\u05DE\u05DF\x07\u0206\x02\x02" +
    "\u05DF\u05E0\x05\xDCo\x02\u05E0\u05E1\x07\u020A\x02\x02\u05E1\u05E2\x05" +
    "\u011C\x8F\x02";
FlinkSqlParser._serializedATNSegment3 = "\u05E2\u05E3\x07\u0207\x02\x02\u05E3\xD9\x03\x02\x02\x02\u05E4\u05E5\t" +
    "\x1A\x02\x02\u05E5\xDB\x03\x02\x02\x02\u05E6\u05E7\x05\u014A\xA6\x02\u05E7" +
    "\xDD\x03\x02\x02\x02\u05E8\u05E9\x07\xA5\x02\x02\u05E9\u05EA\x05\u0106" +
    "\x84\x02\u05EA\xDF\x03\x02\x02\x02\u05EB\u05EC\x07\u01B2\x02\x02\u05EC" +
    "\u05F1\x05\xE2r\x02\u05ED\u05EE\x07\u020A\x02\x02\u05EE\u05F0\x05\xE2" +
    "r\x02\u05EF\u05ED\x03\x02\x02\x02\u05F0\u05F3\x03\x02\x02\x02\u05F1\u05EF" +
    "\x03\x02\x02\x02\u05F1\u05F2\x03\x02\x02\x02\u05F2\xE1\x03\x02\x02\x02" +
    "\u05F3\u05F1\x03\x02\x02\x02\u05F4\u05F5\x05\u012A\x96\x02\u05F5\u05F6" +
    "\x07\x13\x02\x02\u05F6\u05F7\x05\xE4s\x02\u05F7\xE3\x03\x02\x02\x02\u05F8" +
    "\u05FA\x05\u012A\x96\x02\u05F9\u05F8\x03\x02\x02\x02\u05F9\u05FA\x03\x02" +
    "\x02\x02\u05FA\u05FB\x03\x02\x02\x02\u05FB\u05FD\x07\u0206\x02\x02\u05FC" +
    "\u05FE\x05\xEEx\x02\u05FD\u05FC\x03\x02\x02\x02\u05FD\u05FE\x03\x02\x02" +
    "\x02\u05FE\u0600\x03\x02\x02\x02\u05FF\u0601\x05\xE8u\x02\u0600\u05FF" +
    "\x03\x02\x02\x02\u0600\u0601\x03\x02\x02\x02\u0601\u0603\x03\x02\x02\x02" +
    "\u0602\u0604\x05\xFE\x80\x02\u0603\u0602\x03\x02\x02\x02\u0603\u0604\x03" +
    "\x02\x02\x02\u0604\u0605\x03\x02\x02\x02\u0605\u0606\x07\u0207\x02\x02" +
    "\u0606\xE5\x03\x02\x02\x02\u0607\u0608\x07\xD7\x02\x02\u0608\u060A\x07" +
    "\u0206\x02\x02\u0609\u060B\x05\xEEx\x02\u060A\u0609\x03\x02\x02\x02\u060A" +
    "\u060B\x03\x02\x02\x02\u060B\u060D\x03\x02\x02\x02\u060C\u060E\x05\xE8" +
    "u\x02\u060D\u060C\x03\x02\x02\x02\u060D\u060E\x03\x02\x02\x02\u060E\u0610" +
    "\x03\x02\x02\x02\u060F\u0611\x05\xF2z\x02\u0610\u060F\x03\x02\x02\x02" +
    "\u0610\u0611\x03\x02\x02\x02\u0611\u0613\x03\x02\x02\x02\u0612\u0614\x05" +
    "\xF8}\x02\u0613\u0612\x03\x02\x02\x02\u0613\u0614\x03\x02\x02\x02\u0614" +
    "\u0616\x03\x02\x02\x02\u0615\u0617\x05\xFA~\x02\u0616\u0615\x03\x02\x02" +
    "\x02\u0616\u0617\x03\x02\x02\x02\u0617\u0619\x03\x02\x02\x02\u0618\u061A" +
    "\x05\xF4{\x02\u0619\u0618\x03\x02\x02\x02\u0619\u061A\x03\x02\x02\x02" +
    "\u061A\u061B\x03\x02\x02\x02\u061B\u061C\x05\xFC\x7F\x02\u061C\u0621\x07" +
    "\u0207\x02\x02\u061D\u061F\x07\x13\x02\x02\u061E\u061D\x03\x02\x02\x02" +
    "\u061E\u061F\x03\x02\x02\x02\u061F\u0620\x03\x02\x02\x02\u0620\u0622\x05" +
    "\u0132\x9A\x02\u0621\u061E\x03\x02\x02\x02\u0621\u0622\x03\x02\x02\x02" +
    "\u0622\xE7\x03\x02\x02\x02\u0623\u0624\x07\u0104\x02\x02\u0624\u0625\x07" +
    "$\x02\x02\u0625\u062A\x05\xEAv\x02\u0626\u0627\x07\u020A\x02\x02\u0627" +
    "\u0629\x05\xEAv\x02\u0628\u0626\x03\x02\x02\x02\u0629\u062C\x03\x02\x02" +
    "\x02\u062A\u0628\x03\x02\x02\x02\u062A\u062B\x03\x02\x02\x02\u062B\xE9" +
    "\x03\x02\x02\x02\u062C\u062A\x03\x02\x02\x02\u062D\u062F\x056\x1C\x02" +
    "\u062E\u0630\t\x1B\x02\x02\u062F\u062E\x03\x02\x02\x02\u062F\u0630\x03" +
    "\x02\x02\x02\u0630\u0633\x03\x02\x02\x02\u0631\u0632\x07\u01DE\x02\x02" +
    "\u0632\u0634\t\x1C\x02\x02\u0633\u0631\x03\x02\x02\x02\u0633\u0634\x03" +
    "\x02\x02\x02\u0634\xEB\x03\x02\x02\x02\u0635\u0638\x07\xCE\x02\x02\u0636" +
    "\u0639\x07\x07\x02\x02\u0637\u0639\x05\u0104\x83\x02\u0638\u0636\x03\x02" +
    "\x02\x02\u0638\u0637\x03\x02\x02\x02\u0639\xED\x03\x02\x02\x02\u063A\u063B" +
    "\x07\u010E\x02\x02\u063B\u063C\x07$\x02\x02\u063C\u0641\x056\x1C\x02\u063D" +
    "\u063E\x07\u020A\x02\x02\u063E\u0640\x056\x1C\x02\u063F\u063D\x03\x02" +
    "\x02\x02\u0640\u0643\x03\x02\x02\x02\u0641\u063F\x03\x02\x02\x02\u0641" +
    "\u0642\x03\x02\x02\x02\u0642\xEF\x03\x02\x02\x02\u0643\u0641\x03\x02\x02" +
    "\x02\u0644\u0655\x07\u0211\x02\x02\u0645\u0655\x07\u0214\x02\x02\u0646" +
    "\u0655\x07\u0219\x02\x02\u0647\u0648\x07\u0208\x02\x02\u0648\u0649\x07" +
    "\u021C\x02\x02\u0649\u064A\x07\u020A\x02\x02\u064A\u064B\x07\u021C\x02" +
    "\x02\u064B\u0655\x07\u0209\x02\x02\u064C\u064D\x07\u0208\x02\x02\u064D" +
    "\u064E\x07\u021C\x02\x02\u064E\u064F\x07\u020A\x02\x02\u064F\u0655\x07" +
    "\u0209\x02\x02\u0650\u0651\x07\u0208\x02\x02\u0651\u0652\x07\u020A\x02" +
    "\x02\u0652\u0653\x07\u021C\x02\x02\u0653\u0655\x07\u0209\x02\x02\u0654" +
    "\u0644\x03\x02\x02\x02\u0654\u0645\x03\x02\x02\x02\u0654\u0646\x03\x02" +
    "\x02\x02\u0654\u0647\x03\x02\x02\x02\u0654\u064C\x03\x02\x02\x02\u0654" +
    "\u0650\x03\x02\x02\x02\u0655\xF1\x03\x02\x02\x02\u0656\u0657\x07\xD9\x02" +
    "\x02\u0657\u065C\x05\xAEX\x02\u0658\u0659\x07\u020A\x02\x02\u0659\u065B" +
    "\x05\xAEX\x02\u065A\u0658\x03\x02\x02\x02\u065B\u065E\x03\x02\x02\x02" +
    "\u065C\u065A\x03\x02\x02\x02\u065C\u065D\x03\x02\x02\x02\u065D\xF3\x03" +
    "\x02\x02\x02\u065E\u065C\x03\x02\x02\x02\u065F\u0660\x07\u0111\x02\x02" +
    "\u0660\u0662\x07\u0206\x02\x02\u0661\u0663\x05\xF6|\x02\u0662\u0661\x03" +
    "\x02\x02\x02\u0663\u0664\x03\x02\x02\x02\u0664\u0662\x03\x02\x02\x02\u0664" +
    "\u0665\x03\x02\x02\x02\u0665\u0666\x03\x02\x02\x02\u0666\u0668\x07\u0207" +
    "\x02\x02\u0667\u0669\x05\u0102\x82\x02\u0668\u0667\x03\x02\x02\x02\u0668" +
    "\u0669\x03\x02\x02\x02\u0669\xF5\x03\x02\x02\x02\u066A\u066C\x05\u0134" +
    "\x9B\x02\u066B\u066D\x05\xF0y\x02\u066C\u066B\x03\x02\x02\x02\u066C\u066D" +
    "\x03\x02\x02\x02\u066D\xF7\x03\x02\x02\x02\u066E\u066F\x07\x07\x02\x02" +
    "\u066F\u0670\x07\u0144\x02\x02\u0670\u0671\x07\u0112\x02\x02\u0671\u0677" +
    "\x07\xD4\x02\x02\u0672\u0673\x07\u0100\x02\x02\u0673\u0674\x07\u0143\x02" +
    "\x02\u0674\u0675\x07\u0112\x02\x02\u0675\u0677\x07\xD4\x02\x02\u0676\u066E" +
    "\x03\x02\x02\x02\u0676\u0672\x03\x02\x02\x02\u0677\xF9\x03\x02\x02\x02" +
    "\u0678\u0679\x07\u01B8\x02\x02\u0679\u067A\x07\xD4\x02\x02\u067A\u067B" +
    "\x07\u0159\x02\x02\u067B\u067C\x07\u01E0\x02\x02\u067C\u067D\x07\u01D5" +
    "\x02\x02\u067D\u0691\x07\u0143\x02\x02\u067E\u067F\x07\u01B8\x02\x02\u067F" +
    "\u0680\x07\xD4\x02\x02\u0680\u0681\x07\u0159\x02\x02\u0681\u0682\x07\u0186" +
    "\x02\x02\u0682\u0683\x07\xEF\x02\x02\u0683\u0691\x07\u0143\x02\x02\u0684" +
    "\u0685\x07\u01B8\x02\x02\u0685\u0686\x07\xD4\x02\x02\u0686\u0687\x07\u0159" +
    "\x02\x02\u0687\u0688\x07\u0186\x02\x02\u0688\u0689\x07\u01D5\x02\x02\u0689" +
    "\u0691\x05\u0134\x9B\x02\u068A\u068B\x07\u01B8\x02\x02\u068B\u068C\x07" +
    "\xD4\x02\x02\u068C\u068D\x07\u0159\x02\x02\u068D\u068E\x07\u0186\x02\x02" +
    "\u068E\u068F\x07\u01CB\x02\x02\u068F\u0691\x05\u0134\x9B\x02\u0690\u0678" +
    "\x03\x02\x02\x02\u0690\u067E\x03\x02\x02\x02\u0690\u0684\x03\x02\x02\x02" +
    "\u0690\u068A\x03\x02\x02\x02\u0691\xFB\x03\x02\x02\x02\u0692\u0693\x07" +
    "k\x02\x02\u0693\u0698\x05\xAEX\x02\u0694\u0695\x07\u020A\x02\x02\u0695" +
    "\u0697\x05\xAEX\x02\u0696\u0694\x03\x02\x02\x02\u0697\u069A\x03\x02\x02" +
    "\x02\u0698\u0696\x03\x02\x02\x02\u0698\u0699\x03\x02\x02\x02\u0699\xFD" +
    "\x03\x02\x02\x02\u069A\u0698\x03\x02\x02\x02\u069B\u069C\x07\u0126\x02" +
    "\x02\u069C\u069D\x07\x1D\x02\x02\u069D\u069E\x05\u011C\x8F\x02\u069E\u069F" +
    "\x05\u0100\x81\x02\u069F\u06A5\x03\x02\x02\x02\u06A0\u06A1\x07\u0144\x02" +
    "\x02\u06A1\u06A2\x07\x1D\x02\x02\u06A2\u06A3\x07\u021C\x02\x02\u06A3\u06A5" +
    "\x05\u0100\x81\x02\u06A4\u069B\x03\x02\x02\x02\u06A4\u06A0\x03\x02\x02" +
    "\x02\u06A5\xFF\x03\x02\x02\x02\u06A6\u06A7\x07\u01E2\x02\x02\u06A7\u06A8" +
    "\x07\f\x02\x02\u06A8\u06A9\x07N\x02\x02\u06A9\u06AA\x07\u0143\x02\x02" +
    "\u06AA\u0101\x03\x02\x02\x02\u06AB\u06AC\x07\u01B4\x02\x02\u06AC\u06AD" +
    "\x05\u011C\x8F\x02\u06AD\u0103\x03\x02\x02\x02\u06AE\u06AF\x05\u0106\x84" +
    "\x02\u06AF\u0105\x03\x02\x02\x02\u06B0\u06B1\b\x84\x01\x02\u06B1\u06B2" +
    "\x07\xF3\x02\x02\u06B2\u06BD\x05\u0106\x84\b\u06B3\u06B4\x07\x87\x02\x02" +
    "\u06B4\u06B5\x07\u0206\x02\x02\u06B5\u06B6\x05\xA0Q\x02\u06B6\u06B7\x07" +
    "\u0207\x02\x02\u06B7\u06BD\x03\x02\x02\x02\u06B8\u06BA\x05\u010C\x87\x02" +
    "\u06B9\u06BB\x05\u0108\x85\x02\u06BA\u06B9\x03\x02\x02\x02\u06BA\u06BB" +
    "\x03\x02\x02\x02\u06BB\u06BD\x03\x02\x02\x02\u06BC\u06B0\x03\x02\x02\x02" +
    "\u06BC\u06B3\x03\x02\x02\x02\u06BC\u06B8\x03\x02\x02\x02\u06BD\u06CC\x03" +
    "\x02\x02\x02\u06BE\u06BF\f\x05\x02\x02\u06BF\u06C0\x07\f\x02\x02\u06C0" +
    "\u06CB\x05\u0106\x84\x06\u06C1\u06C2\f\x04\x02\x02\u06C2\u06C3\x07\u0103" +
    "\x02\x02\u06C3\u06CB\x05\u0106\x84\x05\u06C4\u06C5\f\x03\x02\x02\u06C5" +
    "\u06C7\x07\xBA\x02\x02\u06C6\u06C8\x07\xF3\x02\x02\u06C7\u06C6\x03\x02" +
    "\x02\x02\u06C7\u06C8\x03\x02\x02\x02\u06C8\u06C9\x03\x02\x02\x02\u06C9" +
    "\u06CB\t\x1D\x02\x02\u06CA\u06BE\x03\x02\x02\x02\u06CA\u06C1\x03\x02\x02" +
    "\x02\u06CA\u06C4\x03\x02\x02\x02\u06CB\u06CE\x03\x02\x02\x02\u06CC\u06CA" +
    "\x03\x02\x02\x02\u06CC\u06CD\x03\x02\x02\x02\u06CD\u0107\x03\x02\x02\x02" +
    "\u06CE\u06CC\x03\x02\x02\x02\u06CF\u06D1\x07\xF3\x02\x02\u06D0\u06CF\x03" +
    "\x02\x02\x02\u06D0\u06D1\x03\x02\x02\x02\u06D1\u06D2\x03\x02\x02\x02\u06D2" +
    "\u06D4\x07\x1D\x02\x02\u06D3\u06D5\t\x1E\x02\x02\u06D4\u06D3\x03\x02\x02" +
    "\x02\u06D4\u06D5\x03\x02\x02\x02\u06D5\u06D6\x03\x02\x02\x02\u06D6\u06D7" +
    "\x05\u010C\x87\x02\u06D7\u06D8\x07\f\x02\x02\u06D8\u06D9\x05\u010C\x87" +
    "\x02\u06D9\u0713\x03\x02\x02\x02\u06DA\u06DC\x07\xF3\x02\x02\u06DB\u06DA" +
    "\x03\x02\x02\x02\u06DB\u06DC\x03\x02\x02\x02\u06DC\u06DD\x03\x02\x02\x02" +
    "\u06DD\u06DE\x07\xAC\x02\x02\u06DE\u06DF\x07\u0206\x02\x02\u06DF\u06E4" +
    "\x05\u0104\x83\x02\u06E0\u06E1\x07\u020A\x02\x02\u06E1\u06E3\x05\u0104" +
    "\x83\x02\u06E2\u06E0\x03\x02\x02\x02\u06E3\u06E6\x03\x02\x02\x02\u06E4" +
    "\u06E2\x03\x02\x02\x02\u06E4\u06E5\x03\x02\x02\x02\u06E5\u06E7\x03\x02" +
    "\x02\x02\u06E6\u06E4\x03\x02\x02\x02\u06E7\u06E8\x07\u0207\x02\x02\u06E8" +
    "\u0713\x03\x02\x02\x02\u06E9\u06EB\x07\xF3\x02\x02\u06EA\u06E9\x03\x02" +
    "\x02\x02\u06EA\u06EB\x03\x02\x02\x02\u06EB\u06EC\x03\x02\x02\x02\u06EC" +
    "\u06ED\x07\xAC\x02\x02\u06ED\u06EE\x07\u0206\x02\x02\u06EE\u06EF\x05\xA0" +
    "Q\x02\u06EF\u06F0\x07\u0207\x02\x02\u06F0\u0713\x03\x02\x02\x02\u06F1" +
    "\u06F2\x07\x87\x02\x02\u06F2\u06F3\x07\u0206\x02\x02\u06F3\u06F4\x05\xA0" +
    "Q\x02\u06F4\u06F5\x07\u0207\x02\x02\u06F5\u0713\x03\x02\x02\x02\u06F6" +
    "\u06F8\x07\xF3\x02\x02\u06F7\u06F6\x03\x02\x02\x02\u06F7\u06F8\x03\x02" +
    "\x02\x02\u06F8\u06F9\x03\x02\x02\x02\u06F9\u06FA\x07\u0140\x02\x02\u06FA" +
    "\u0713\x05\u010C\x87\x02\u06FB\u0713\x05\u010A\x86\x02\u06FC\u06FE\x07" +
    "\xBA\x02\x02\u06FD\u06FF\x07\xF3\x02\x02\u06FE\u06FD\x03\x02\x02\x02\u06FE" +
    "\u06FF\x03\x02\x02\x02\u06FF\u0700\x03\x02\x02\x02\u0700\u0713\t\x1D\x02" +
    "\x02\u0701\u0703\x07\xBA\x02\x02\u0702\u0704\x07\xF3\x02\x02\u0703\u0702" +
    "\x03\x02\x02\x02\u0703\u0704\x03\x02\x02\x02\u0704\u0705\x03\x02\x02\x02" +
    "\u0705\u0706\x07s\x02\x02\u0706\u0707\x07\x99\x02\x02\u0707\u0713\x05" +
    "\u010C\x87\x02\u0708\u070A\x07\xF3\x02\x02\u0709\u0708\x03\x02\x02\x02" +
    "\u0709\u070A\x03\x02\x02\x02\u070A\u070B\x03\x02\x02\x02\u070B\u070C\x07" +
    "\u0158\x02\x02\u070C\u070D\x07\u0186\x02\x02\u070D\u0710\x05\u010C\x87" +
    "\x02\u070E\u070F\x07\x81\x02\x02\u070F\u0711\x05\u0168\xB5\x02\u0710\u070E" +
    "\x03\x02\x02\x02\u0710\u0711\x03\x02\x02\x02\u0711\u0713\x03\x02\x02\x02" +
    "\u0712\u06D0\x03\x02\x02\x02\u0712\u06DB\x03\x02\x02\x02\u0712\u06EA\x03" +
    "\x02\x02\x02\u0712\u06F1\x03\x02\x02\x02\u0712\u06F7\x03\x02\x02\x02\u0712" +
    "\u06FB\x03\x02\x02\x02\u0712\u06FC\x03\x02\x02\x02\u0712\u0701\x03\x02" +
    "\x02\x02\u0712\u0709\x03\x02\x02\x02\u0713\u0109\x03\x02\x02\x02\u0714" +
    "\u0716\x07\xF3\x02\x02\u0715\u0714\x03\x02\x02\x02\u0715\u0716\x03\x02" +
    "\x02\x02\u0716\u0717\x03\x02\x02\x02\u0717\u0718\x07\xCC\x02\x02\u0718" +
    "\u0726\t\x1F\x02\x02\u0719\u071A\x07\u0206\x02\x02\u071A\u0727\x07\u0207" +
    "\x02\x02\u071B\u071C\x07\u0206\x02\x02\u071C\u0721\x05\u0104\x83\x02\u071D" +
    "\u071E\x07\u020A\x02\x02\u071E\u0720\x05\u0104\x83\x02\u071F\u071D\x03" +
    "\x02\x02\x02\u0720\u0723\x03\x02\x02\x02\u0721\u071F\x03\x02\x02\x02\u0721" +
    "\u0722\x03\x02\x02\x02\u0722\u0724\x03\x02\x02\x02\u0723\u0721\x03\x02" +
    "\x02\x02\u0724\u0725\x07\u0207\x02\x02\u0725\u0727\x03\x02\x02\x02\u0726" +
    "\u0719\x03\x02\x02\x02\u0726\u071B\x03\x02\x02\x02\u0727\u0732\x03\x02" +
    "\x02\x02\u0728\u072A\x07\xF3\x02\x02\u0729\u0728\x03\x02\x02\x02\u0729" +
    "\u072A\x03\x02\x02\x02\u072A\u072B\x03\x02\x02\x02\u072B\u072C\x07\xCC" +
    "\x02\x02\u072C\u072F\x05\u010C\x87\x02\u072D\u072E\x07\x81\x02\x02\u072E" +
    "\u0730\x05\u0168\xB5\x02\u072F\u072D\x03\x02\x02\x02\u072F\u0730\x03\x02" +
    "\x02\x02\u0730\u0732\x03\x02\x02\x02\u0731\u0715\x03\x02\x02\x02\u0731" +
    "\u0729\x03\x02\x02\x02\u0732\u010B\x03\x02\x02\x02\u0733\u0734\b\x87\x01" +
    "\x02\u0734\u0738\x05\u010E\x88\x02\u0735\u0736\t \x02\x02\u0736\u0738" +
    "\x05\u010C\x87\t\u0737\u0733\x03\x02\x02\x02\u0737\u0735\x03\x02\x02\x02" +
    "\u0738\u074E\x03\x02\x02\x02\u0739\u073A\f\b\x02\x02\u073A\u073B\t!\x02" +
    "\x02\u073B\u074D\x05\u010C\x87\t\u073C\u073D\f\x07\x02\x02\u073D\u073E" +
    "\t\"\x02\x02\u073E\u074D\x05\u010C\x87\b\u073F\u0740\f\x06\x02\x02\u0740" +
    "\u0741\x07\u0201\x02\x02\u0741\u074D\x05\u010C\x87\x07\u0742\u0743\f\x05" +
    "\x02\x02\u0743\u0744\x07\u0202\x02\x02\u0744\u074D\x05\u010C\x87\x06\u0745" +
    "\u0746\f\x04\x02\x02\u0746\u0747\x07\u0200\x02\x02\u0747\u074D\x05\u010C" +
    "\x87\x05\u0748\u0749\f\x03\x02\x02\u0749\u074A\x05\u015C\xAF\x02\u074A" +
    "\u074B\x05\u010C\x87\x04\u074B\u074D\x03\x02\x02\x02\u074C\u0739\x03\x02" +
    "\x02\x02\u074C\u073C\x03\x02\x02\x02\u074C\u073F\x03\x02\x02\x02\u074C" +
    "\u0742\x03\x02\x02\x02\u074C\u0745\x03\x02\x02\x02\u074C\u0748\x03\x02" +
    "\x02\x02\u074D\u0750\x03\x02\x02\x02\u074E\u074C\x03\x02\x02\x02\u074E" +
    "\u074F\x03\x02\x02\x02\u074F\u010D\x03\x02\x02\x02\u0750\u074E\x03\x02" +
    "\x02\x02\u0751\u0752\b\x88\x01\x02\u0752\u0754\x07*\x02\x02\u0753\u0755" +
    "\x05\u0138\x9D\x02\u0754\u0753\x03\x02\x02\x02\u0755\u0756\x03\x02\x02" +
    "\x02\u0756\u0754\x03\x02\x02\x02\u0756\u0757\x03\x02\x02\x02\u0757\u075A" +
    "\x03\x02\x02\x02\u0758\u0759\x07z\x02\x02\u0759\u075B\x05\u0104\x83\x02" +
    "\u075A\u0758\x03\x02\x02\x02\u075A\u075B\x03\x02\x02\x02\u075B\u075C\x03" +
    "\x02\x02\x02\u075C\u075D\x07|\x02\x02\u075D\u07AE\x03\x02\x02\x02\u075E" +
    "\u075F\x07*\x02\x02\u075F\u0761\x05\u0104\x83\x02\u0760\u0762\x05\u0138" +
    "\x9D\x02\u0761\u0760\x03\x02\x02\x02\u0762\u0763\x03\x02\x02\x02\u0763" +
    "\u0761\x03\x02\x02\x02\u0763\u0764\x03\x02\x02\x02\u0764\u0767\x03\x02" +
    "\x02\x02\u0765\u0766\x07z\x02\x02\u0766\u0768\x05\u0104\x83\x02\u0767" +
    "\u0765\x03\x02\x02\x02\u0767\u0768\x03\x02\x02\x02\u0768\u0769\x03\x02" +
    "\x02\x02\u0769\u076A\x07|\x02\x02\u076A\u07AE\x03\x02\x02\x02\u076B\u076C" +
    "\x07+\x02\x02\u076C\u076D\x07\u0206\x02\x02\u076D\u076E\x05\u0104\x83" +
    "\x02\u076E\u076F\x07\x13\x02\x02\u076F\u0770\x05:\x1E\x02\u0770\u0771" +
    "\x07\u0207\x02\x02\u0771\u07AE\x03\x02\x02\x02\u0772\u0773\x07\u01CB\x02" +
    "\x02\u0773\u0774\x07\u0206\x02\x02\u0774\u0777\x05\u0104\x83\x02\u0775" +
    "\u0776\x07\u01CF\x02\x02\u0776\u0778\x07\u01DE\x02\x02\u0777\u0775\x03" +
    "\x02\x02\x02\u0777\u0778\x03\x02\x02\x02\u0778\u0779\x03\x02\x02\x02\u0779" +
    "\u077A\x07\u0207\x02\x02\u077A\u07AE\x03\x02\x02\x02\u077B\u077C\x07\u01D5" +
    "\x02\x02\u077C\u077D\x07\u0206\x02\x02\u077D\u0780\x05\u0104\x83\x02\u077E" +
    "\u077F\x07\u01CF\x02\x02\u077F\u0781\x07\u01DE\x02\x02\u0780\u077E\x03" +
    "\x02\x02\x02\u0780\u0781\x03\x02\x02\x02\u0781\u0782\x03\x02\x02\x02\u0782" +
    "\u0783\x07\u0207\x02\x02\u0783\u07AE\x03\x02\x02\x02\u0784\u0785\x07\u011B" +
    "\x02\x02\u0785\u0786\x07\u0206\x02\x02\u0786\u0787\x05\u010C\x87\x02\u0787" +
    "\u0788\x07\xAC\x02\x02\u0788\u0789\x05\u010C\x87\x02\u0789\u078A\x07\u0207" +
    "\x02\x02\u078A\u07AE\x03\x02\x02\x02\u078B\u07AE\x05\u0164\xB3\x02\u078C" +
    "\u07AE\x07\u0211\x02\x02\u078D\u078E\x05\u014A\xA6\x02\u078E\u078F\x07" +
    "\u0203\x02\x02\u078F\u0790\x07\u0211\x02\x02\u0790\u07AE\x03\x02\x02\x02" +
    "\u0791\u0792\x07\u0206\x02\x02\u0792\u0793\x05\xA0Q\x02\u0793\u0794\x07" +
    "\u0207\x02\x02\u0794\u07AE\x03\x02\x02\x02\u0795\u0796\x05\u0112\x8A\x02" +
    "\u0796\u07A2\x07\u0206\x02\x02\u0797\u0799\x05\u016E\xB8\x02\u0798\u0797" +
    "\x03\x02\x02\x02\u0798\u0799\x03\x02\x02\x02\u0799\u079A\x03\x02\x02\x02" +
    "\u079A\u079F\x05\u0114\x8B\x02\u079B\u079C\x07\u020A\x02\x02\u079C\u079E" +
    "\x05\u0114\x8B\x02\u079D\u079B\x03\x02\x02\x02\u079E\u07A1\x03\x02\x02" +
    "\x02\u079F\u079D\x03\x02\x02\x02\u079F\u07A0\x03\x02\x02\x02\u07A0\u07A3" +
    "\x03\x02\x02\x02\u07A1\u079F\x03\x02\x02\x02\u07A2\u0798\x03\x02\x02\x02" +
    "\u07A2\u07A3\x03\x02\x02\x02\u07A3\u07A4\x03\x02\x02\x02\u07A4\u07A5\x07" +
    "\u0207\x02\x02\u07A5\u07AE\x03\x02\x02\x02\u07A6\u07AE\x05\u0132\x9A\x02" +
    "\u07A7\u07AE\x05\u0116\x8C\x02\u07A8\u07A9\x07\u0206\x02\x02\u07A9\u07AA" +
    "\x05\u0104\x83\x02\u07AA\u07AB\x07\u0207\x02\x02\u07AB\u07AE\x03\x02\x02" +
    "\x02\u07AC\u07AE\x07W\x02\x02\u07AD\u0751\x03\x02\x02\x02\u07AD\u075E" +
    "\x03\x02\x02\x02\u07AD\u076B\x03\x02\x02\x02\u07AD\u0772\x03\x02\x02\x02" +
    "\u07AD\u077B\x03\x02\x02\x02\u07AD\u0784\x03\x02\x02\x02\u07AD\u078B\x03" +
    "\x02\x02\x02\u07AD\u078C\x03\x02\x02\x02\u07AD\u078D\x03\x02\x02\x02\u07AD" +
    "\u0791\x03\x02\x02\x02\u07AD\u0795\x03\x02\x02\x02\u07AD\u07A6\x03\x02" +
    "\x02\x02\u07AD\u07A7\x03\x02\x02\x02\u07AD\u07A8\x03\x02\x02\x02\u07AD" +
    "\u07AC\x03\x02\x02\x02\u07AE\u07B6\x03\x02\x02\x02\u07AF\u07B0\f\x07\x02" +
    "\x02\u07B0\u07B1\x07\u0204\x02\x02\u07B1\u07B2\x05\u010C\x87\x02\u07B2" +
    "\u07B3\x07\u0205\x02\x02\u07B3\u07B5\x03\x02\x02\x02\u07B4\u07AF\x03\x02" +
    "\x02\x02\u07B5\u07B8\x03\x02\x02\x02\u07B6\u07B4\x03\x02\x02\x02\u07B6" +
    "\u07B7\x03\x02\x02\x02\u07B7\u010F\x03\x02\x02\x02\u07B8\u07B6\x03\x02" +
    "\x02\x02\u07B9\u07BA\x05\u014A\xA6\x02\u07BA\u0111\x03\x02\x02\x02\u07BB" +
    "\u07BE\x05\u0176\xBC\x02\u07BC\u07BE\x05\u014A\xA6\x02\u07BD\u07BB\x03" +
    "\x02\x02\x02\u07BD\u07BC\x03\x02\x02\x02\u07BE\u0113\x03\x02\x02\x02\u07BF" +
    "\u07C4\x05\u0174\xBB\x02\u07C0\u07C4\x05\u0172\xBA\x02\u07C1\u07C4\x05" +
    "\u0170\xB9\x02\u07C2\u07C4\x05\u0104\x83\x02\u07C3\u07BF\x03\x02\x02\x02" +
    "\u07C3\u07C0\x03\x02\x02\x02\u07C3\u07C1\x03\x02\x02\x02\u07C3\u07C2\x03" +
    "\x02\x02\x02\u07C4\u0115\x03\x02\x02\x02\u07C5\u07C6\x05\u014A\xA6\x02" +
    "\u07C6\u0117\x03\x02\x02\x02\u07C7\u07C8\x05\u0132\x9A\x02\u07C8\u0119" +
    "\x03\x02\x02\x02\u07C9\u07CC\x05\u0132\x9A\x02\u07CA\u07CC\x05\u0116\x8C" +
    "\x02\u07CB\u07C9\x03\x02\x02\x02\u07CB\u07CA\x03\x02\x02\x02\u07CC\u011B" +
    "\x03\x02\x02\x02\u07CD\u07D0\x07\xB8\x02\x02\u07CE\u07D1\x05\u011E\x90" +
    "\x02\u07CF\u07D1\x05\u0122\x92\x02\u07D0\u07CE\x03\x02\x02\x02\u07D0\u07CF" +
    "\x03\x02\x02\x02\u07D0\u07D1\x03\x02\x02\x02\u07D1\u011D\x03\x02\x02\x02" +
    "\u07D2\u07D4\x05\u0120\x91\x02\u07D3\u07D5\x05\u0124\x93\x02\u07D4\u07D3" +
    "\x03\x02\x02\x02\u07D4\u07D5\x03\x02\x02\x02\u07D5\u011F\x03\x02\x02\x02" +
    "\u07D6\u07D7\x05\u0126\x94\x02\u07D7\u07D8\x05\u0172\xBA\x02\u07D8\u07DA" +
    "\x03\x02\x02\x02\u07D9\u07D6\x03\x02\x02\x02\u07DA\u07DB\x03\x02\x02\x02" +
    "\u07DB\u07D9\x03\x02\x02\x02\u07DB\u07DC\x03\x02\x02\x02\u07DC\u0121\x03" +
    "\x02\x02\x02\u07DD\u07E0\x05\u0124\x93\x02\u07DE\u07E1\x05\u0120\x91\x02" +
    "\u07DF\u07E1\x05\u0124\x93\x02\u07E0\u07DE\x03\x02\x02\x02\u07E0\u07DF" +
    "\x03\x02\x02\x02\u07E0\u07E1\x03\x02\x02\x02\u07E1\u0123\x03\x02\x02\x02" +
    "\u07E2\u07E3\x05\u0126\x94\x02\u07E3\u07E4\x05\u0172\xBA\x02\u07E4\u07E5" +
    "\x07\u0186\x02\x02\u07E5\u07E6\x05\u0172\xBA\x02\u07E6\u0125\x03\x02\x02" +
    "\x02\u07E7\u07E9\t#\x02\x02\u07E8\u07E7\x03\x02\x02\x02\u07E8\u07E9\x03" +
    "\x02\x02\x02\u07E9\u07EA\x03\x02\x02\x02\u07EA\u07ED\t$\x02\x02\u07EB" +
    "\u07ED\x07\u021B\x02\x02\u07EC\u07E8\x03\x02\x02\x02\u07EC\u07EB\x03\x02" +
    "\x02\x02\u07ED\u0127\x03\x02\x02\x02\u07EE\u07F0\x07\x13\x02\x02\u07EF" +
    "\u07EE\x03\x02\x02\x02\u07EF\u07F0\x03\x02\x02\x02\u07F0\u07F1\x03\x02" +
    "\x02\x02\u07F1\u07F3\x05\u0132\x9A\x02\u07F2\u07F4\x05\u012E\x98\x02\u07F3" +
    "\u07F2\x03\x02\x02\x02\u07F3\u07F4\x03\x02\x02\x02\u07F4\u0129\x03\x02" +
    "\x02\x02\u07F5\u07F6\x05\u0132\x9A\x02\u07F6\u07F7\x05\u012C\x97\x02\u07F7" +
    "\u012B\x03\x02\x02\x02\u07F8\u07F9\x07\xDF\x02\x02\u07F9\u07FB\x05\u0132" +
    "\x9A\x02\u07FA\u07F8\x03\x02\x02\x02\u07FB\u07FC\x03\x02\x02\x02\u07FC" +
    "\u07FA\x03\x02\x02\x02\u07FC\u07FD\x03\x02\x02\x02\u07FD\u0800\x03\x02" +
    "\x02\x02\u07FE\u0800\x03\x02\x02\x02\u07FF\u07FA\x03\x02\x02\x02\u07FF" +
    "\u07FE\x03\x02\x02\x02\u0800\u012D\x03\x02\x02\x02\u0801\u0802\x07\u0206" +
    "\x02\x02\u0802\u0803\x05\u0130\x99\x02\u0803\u0804\x07\u0207\x02\x02\u0804" +
    "\u012F\x03\x02\x02\x02\u0805\u080A\x05\u0132\x9A\x02\u0806\u0807\x07\u020A" +
    "\x02\x02\u0807\u0809\x05\u0132\x9A\x02\u0808\u0806\x03\x02\x02\x02\u0809" +
    "\u080C\x03\x02\x02\x02\u080A\u0808\x03\x02\x02\x02\u080A\u080B\x03\x02" +
    "\x02\x02\u080B\u0131\x03\x02\x02\x02\u080C\u080A\x03\x02\x02\x02\u080D" +
    "\u0811\x05\u0134\x9B\x02\u080E\u0811\x05\u0136\x9C\x02\u080F\u0811\x05" +
    "\u0178\xBD\x02\u0810\u080D\x03\x02\x02\x02\u0810\u080E\x03\x02\x02\x02" +
    "\u0810\u080F\x03\x02\x02\x02\u0811\u0133\x03\x02\x02\x02\u0812\u0813\t" +
    "%\x02\x02\u0813\u0135\x03\x02\x02\x02\u0814\u0815\x07\u021B\x02\x02\u0815" +
    "\u0137\x03\x02\x02\x02\u0816\u0817\x07\u01AE\x02\x02\u0817\u0818\x05\u0104" +
    "\x83\x02\u0818\u0819\x07\u017A\x02\x02\u0819\u081A\x05\u0104\x83\x02\u081A" +
    "\u0139\x03\x02\x02\x02\u081B\u081C\x05\u0132\x9A\x02\u081C\u013B\x03\x02" +
    "\x02\x02\u081D\u081E\x05\u0132\x9A\x02\u081E\u013D\x03\x02\x02\x02\u081F" +
    "\u0822\x05\u0132\x9A\x02\u0820\u0821\x07\u0203\x02\x02\u0821\u0823\x05" +
    "\u0132\x9A\x02\u0822\u0820\x03\x02\x02\x02\u0822\u0823\x03\x02\x02\x02" +
    "\u0823\u013F\x03\x02\x02\x02\u0824\u0827\x05\u0132\x9A\x02\u0825\u0826" +
    "\x07\u0203\x02\x02\u0826\u0828\x05\u0132\x9A\x02\u0827\u0825\x03\x02\x02" +
    "\x02\u0827\u0828\x03\x02\x02\x02\u0828\u0141\x03\x02\x02\x02\u0829\u082C" +
    "\x05\u0132\x9A\x02\u082A\u082B\x07\u0203\x02\x02\u082B\u082D\x05\u0132" +
    "\x9A\x02\u082C\u082A\x03\x02\x02\x02\u082C\u082D\x03\x02\x02\x02\u082D" +
    "\u0836\x03\x02\x02\x02\u082E\u082F\x05\u0132\x9A\x02\u082F\u0830\x07\u0203" +
    "\x02\x02\u0830\u0833\x05\u0132\x9A\x02\u0831\u0832\x07\u0203\x02\x02\u0832" +
    "\u0834\x05\u0132\x9A\x02\u0833\u0831\x03\x02\x02\x02\u0833\u0834\x03\x02" +
    "\x02\x02\u0834\u0836\x03\x02\x02\x02\u0835\u0829\x03\x02\x02\x02\u0835" +
    "\u082E\x03\x02\x02\x02\u0836\u0143\x03\x02\x02\x02\u0837\u083A\x05\u0132" +
    "\x9A\x02\u0838\u0839\x07\u0203\x02\x02\u0839\u083B\x05\u0132\x9A\x02\u083A" +
    "\u0838\x03\x02\x02\x02\u083A\u083B\x03\x02\x02\x02\u083B\u0844\x03\x02" +
    "\x02\x02\u083C\u083D\x05\u0132\x9A\x02\u083D\u083E\x07\u0203\x02\x02\u083E" +
    "\u0841\x05\u0132\x9A\x02\u083F\u0840\x07\u0203\x02\x02\u0840\u0842\x05" +
    "\u0132\x9A\x02\u0841\u083F\x03\x02\x02\x02\u0841\u0842\x03\x02\x02\x02" +
    "\u0842\u0844\x03\x02\x02\x02\u0843\u0837\x03\x02\x02\x02\u0843\u083C\x03" +
    "\x02\x02\x02\u0844\u0145\x03\x02\x02\x02\u0845\u0848\x05\u0132\x9A\x02" +
    "\u0846\u0847\x07\u0203\x02\x02\u0847\u0849\x05\u0132\x9A\x02\u0848\u0846" +
    "\x03\x02\x02\x02\u0848\u0849\x03\x02\x02\x02\u0849\u0852\x03\x02\x02\x02" +
    "\u084A\u084B\x05\u0132\x9A\x02\u084B\u084C\x07\u0203\x02\x02\u084C\u084F" +
    "\x05\u0132\x9A\x02\u084D\u084E\x07\u0203\x02\x02\u084E\u0850\x05\u0132" +
    "\x9A\x02\u084F\u084D\x03\x02\x02\x02\u084F\u0850\x03\x02\x02\x02\u0850" +
    "\u0852\x03\x02\x02\x02\u0851\u0845\x03\x02\x02\x02\u0851\u084A\x03\x02" +
    "\x02\x02\u0852\u0147\x03\x02\x02\x02\u0853\u0856\x05\u0132\x9A\x02\u0854" +
    "\u0855\x07\u0203\x02\x02\u0855\u0857\x05\u0132\x9A\x02\u0856\u0854\x03" +
    "\x02\x02\x02\u0856\u0857\x03\x02\x02\x02\u0857\u0860\x03\x02\x02\x02\u0858" +
    "\u0859\x05\u0132\x9A\x02\u0859\u085A\x07\u0203\x02\x02\u085A\u085D\x05" +
    "\u0132\x9A\x02\u085B\u085C\x07\u0203\x02\x02\u085C\u085E\x05\u0132\x9A" +
    "\x02\u085D\u085B\x03\x02\x02\x02\u085D\u085E\x03\x02\x02\x02\u085E\u0860" +
    "\x03\x02\x02\x02\u085F\u0853\x03\x02\x02\x02\u085F\u0858\x03\x02\x02\x02" +
    "\u0860\u0149\x03\x02\x02\x02\u0861\u0866\x05\u0132\x9A\x02\u0862\u0863" +
    "\x07\u0203\x02\x02\u0863\u0865\x05\u0132\x9A\x02\u0864\u0862\x03\x02\x02" +
    "\x02\u0865\u0868\x03\x02\x02\x02\u0866\u0867\x03\x02\x02\x02\u0866\u0864" +
    "\x03\x02\x02\x02\u0867\u014B\x03\x02\x02\x02\u0868\u0866\x03\x02\x02\x02" +
    "\u0869\u086A\x07\u01B3\x02\x02\u086A\u086B\x05\u0152\xAA\x02\u086B\u014D" +
    "\x03\x02\x02\x02\u086C\u086D\x07\xA9\x02\x02\u086D\u086E\x07\xF3\x02\x02" +
    "\u086E\u086F\x07\x87\x02\x02\u086F\u014F\x03\x02\x02\x02\u0870\u0871\x07" +
    "\xA9\x02\x02\u0871\u0872\x07\x87\x02\x02\u0872\u0151\x03\x02\x02\x02\u0873" +
    "\u0874\x07\u0206\x02\x02\u0874\u0879\x05\u0154\xAB\x02\u0875\u0876\x07" +
    "\u020A\x02\x02\u0876\u0878\x05\u0154\xAB\x02\u0877\u0875\x03\x02\x02\x02" +
    "\u0878\u087B\x03\x02\x02\x02\u0879\u0877\x03\x02\x02\x02\u0879\u087A\x03" +
    "\x02\x02\x02\u087A\u087C\x03\x02\x02\x02\u087B\u0879\x03\x02\x02\x02\u087C" +
    "\u087D\x07\u0207\x02\x02\u087D\u0153\x03\x02\x02\x02\u087E\u0883\x05\u0156" +
    "\xAC\x02\u087F\u0881\x07\u01FB\x02\x02\u0880\u087F\x03\x02\x02\x02\u0880" +
    "\u0881\x03\x02\x02\x02\u0881\u0882\x03\x02\x02\x02\u0882\u0884\x05\u0158" +
    "\xAD\x02\u0883\u0880\x03\x02\x02\x02\u0883\u0884\x03\x02\x02\x02\u0884" +
    "\u0155\x03\x02\x02\x02\u0885\u0889\x05\u0132\x9A\x02\u0886\u0889\x05\u0116" +
    "\x8C\x02\u0887\u0889\x07\u021B\x02\x02\u0888\u0885\x03\x02\x02\x02\u0888" +
    "\u0886\x03\x02\x02\x02\u0888\u0887\x03\x02\x02\x02\u0889\u0157\x03\x02" +
    "\x02\x02\u088A\u088F\x07\u021C\x02\x02\u088B\u088F\x07\u021D\x02\x02\u088C" +
    "\u088F\x05\u016C\xB7\x02\u088D\u088F\x07\u021B\x02\x02\u088E\u088A\x03" +
    "\x02\x02\x02\u088E\u088B\x03\x02\x02\x02\u088E\u088C\x03\x02\x02\x02\u088E" +
    "\u088D\x03\x02\x02\x02\u088F\u0159\x03\x02\x02\x02\u0890\u0897\x07\f\x02" +
    "\x02\u0891\u0892\x07\u0201\x02\x02\u0892\u0897\x07\u0201\x02\x02\u0893" +
    "\u0897\x07\u0103\x02\x02\u0894\u0895\x07\u0200\x02\x02\u0895\u0897\x07" +
    "\u0200\x02\x02\u0896\u0890\x03\x02\x02\x02\u0896\u0891\x03\x02\x02\x02" +
    "\u0896\u0893\x03\x02\x02\x02\u0896\u0894";
FlinkSqlParser._serializedATNSegment4 = "\x03\x02\x02\x02\u0897\u015B\x03\x02\x02\x02\u0898\u08A7\x07\u01FB\x02" +
    "\x02\u0899\u08A7\x07\u01FC\x02\x02\u089A\u08A7\x07\u01FD\x02\x02\u089B" +
    "\u089C\x07\u01FD\x02\x02\u089C\u08A7\x07\u01FB\x02\x02\u089D\u089E\x07" +
    "\u01FC\x02\x02\u089E\u08A7\x07\u01FB\x02\x02\u089F\u08A0\x07\u01FD\x02" +
    "\x02\u08A0\u08A7\x07\u01FC\x02\x02\u08A1\u08A2\x07\u01FE\x02\x02\u08A2" +
    "\u08A7\x07\u01FB\x02\x02\u08A3\u08A4\x07\u01FD\x02\x02\u08A4\u08A5\x07" +
    "\u01FB\x02\x02\u08A5\u08A7\x07\u01FC\x02\x02\u08A6\u0898\x03\x02\x02\x02" +
    "\u08A6\u0899\x03\x02\x02\x02\u08A6\u089A\x03\x02\x02\x02\u08A6\u089B\x03" +
    "\x02\x02\x02\u08A6\u089D\x03\x02\x02\x02\u08A6\u089F\x03\x02\x02\x02\u08A6" +
    "\u08A1\x03\x02\x02\x02\u08A6\u08A3\x03\x02\x02\x02\u08A7\u015D\x03\x02" +
    "\x02\x02\u08A8\u08A9\x07\u01FD\x02\x02\u08A9\u08B0\x07\u01FD\x02\x02\u08AA" +
    "\u08AB\x07\u01FC\x02\x02\u08AB\u08B0\x07\u01FC\x02\x02\u08AC\u08B0\x07" +
    "\u0201\x02\x02\u08AD\u08B0\x07\u0202\x02\x02\u08AE\u08B0\x07\u0200\x02" +
    "\x02\u08AF\u08A8\x03\x02\x02\x02\u08AF\u08AA\x03\x02\x02\x02\u08AF\u08AC" +
    "\x03\x02\x02\x02\u08AF\u08AD\x03\x02\x02\x02\u08AF\u08AE\x03\x02\x02\x02" +
    "\u08B0\u015F\x03\x02\x02\x02\u08B1\u08B2\t&\x02\x02\u08B2\u0161\x03\x02" +
    "\x02\x02\u08B3\u08B4\t\'\x02\x02\u08B4\u0163\x03\x02\x02\x02\u08B5\u08C4" +
    "\x05\u011C\x8F\x02\u08B6\u08C4\x05\u0166\xB4\x02\u08B7\u08C4\x05\u0168" +
    "\xB5\x02\u08B8\u08BA\x07\u0213\x02\x02\u08B9\u08B8\x03\x02\x02\x02\u08B9" +
    "\u08BA\x03\x02\x02\x02\u08BA\u08BB\x03\x02\x02\x02\u08BB\u08C4\x05\u016A" +
    "\xB6\x02\u08BC\u08C4\x05\u016C\xB7\x02\u08BD\u08C4\x07\u021D\x02\x02\u08BE" +
    "\u08C4\x07\u021E\x02\x02\u08BF\u08C1\x07\xF3\x02\x02\u08C0\u08BF\x03\x02" +
    "\x02\x02\u08C0\u08C1\x03\x02\x02\x02\u08C1\u08C2\x03\x02\x02\x02\u08C2" +
    "\u08C4\x07\xF6\x02\x02\u08C3\u08B5\x03\x02\x02\x02\u08C3\u08B6\x03\x02" +
    "\x02\x02\u08C3\u08B7\x03\x02\x02\x02\u08C3\u08B9\x03\x02\x02\x02\u08C3" +
    "\u08BC\x03\x02\x02\x02\u08C3\u08BD\x03\x02\x02\x02\u08C3\u08BE\x03\x02" +
    "\x02\x02\u08C3\u08C0\x03\x02\x02\x02\u08C4\u0165\x03\x02\x02\x02\u08C5" +
    "\u08C6\x05\u0170\xB9\x02\u08C6\u08C7\x05\u0168\xB5\x02\u08C7\u0167\x03" +
    "\x02\x02\x02\u08C8\u08C9\x07\u021B\x02\x02\u08C9\u0169\x03\x02\x02\x02" +
    "\u08CA\u08CB\x07\u021C\x02\x02\u08CB\u016B\x03\x02\x02\x02\u08CC\u08CD" +
    "\t(\x02\x02\u08CD\u016D\x03\x02\x02\x02\u08CE\u08CF\t)\x02\x02\u08CF\u016F" +
    "\x03\x02\x02\x02\u08D0\u08D1\t*\x02\x02\u08D1\u0171\x03\x02\x02\x02\u08D2" +
    "\u08D3\t+\x02\x02\u08D3\u0173\x03\x02\x02\x02\u08D4\u08D5\t,\x02\x02\u08D5" +
    "\u0175\x03\x02\x02\x02\u08D6\u08D7\t-\x02\x02\u08D7\u0177\x03\x02\x02" +
    "\x02\u08D8\u08D9\t.\x02\x02\u08D9\u0179\x03\x02\x02\x02\u010E\u017D\u0184" +
    "\u0187\u0195\u01A7\u01AB\u01B4\u01B9\u01C0\u01CB\u01D4\u01E0\u01E3\u01EA" +
    "\u01ED\u01F5\u01F9\u01FE\u0201\u0208\u0210\u0214\u0220\u0228\u022C\u024C" +
    "\u024F\u0254\u0258\u025C\u0260\u0269\u026E\u0272\u0276\u027A\u027D\u0281" +
    "\u0286\u028C\u0291\u0296\u0299\u029D\u02A1\u02A9\u02B1\u02B5\u02B9\u02BD" +
    "\u02C1\u02C5\u02C9\u02CD\u02D1\u02D3\u02DD\u02E5\u02FD\u0304\u030A\u030D" +
    "\u0310\u031A\u031D\u0325\u0331\u0349\u0356\u035B\u035F\u0367\u036B\u0371" +
    "\u037B\u037F\u0385\u0389\u038D\u0390\u0399\u039D\u03A4\u03A7\u03B1\u03B9" +
    "\u03C1\u03C5\u03D4\u03E7\u03F2\u03F6\u03FD\u0402\u0408\u040C\u0413\u0417" +
    "\u041B\u041F\u0427\u042B\u0430\u0436\u043C\u043F\u0443\u044E\u0457\u0465" +
    "\u0471\u0480\u0483\u0487\u048A\u048C\u0491\u0495\u0498\u049C\u04A5\u04AE" +
    "\u04B8\u04BD\u04C8\u04CB\u04CE\u04D1\u04D4\u04DA\u04DE\u04E6\u04E9\u04EE" +
    "\u04F1\u04F3\u0501\u050C\u0511\u0519\u051C\u051F\u0524\u0526\u0528\u052D" +
    "\u0530\u0534\u0537\u053A\u053E\u0541\u0544\u0550\u0557\u0562\u057D\u0593" +
    "\u05A0\u05AC\u05B9\u05C5\u05D1\u05D6\u05F1\u05F9\u05FD\u0600\u0603\u060A" +
    "\u060D\u0610\u0613\u0616\u0619\u061E\u0621\u062A\u062F\u0633\u0638\u0641" +
    "\u0654\u065C\u0664\u0668\u066C\u0676\u0690\u0698\u06A4\u06BA\u06BC\u06C7" +
    "\u06CA\u06CC\u06D0\u06D4\u06DB\u06E4\u06EA\u06F7\u06FE\u0703\u0709\u0710" +
    "\u0712\u0715\u0721\u0726\u0729\u072F\u0731\u0737\u074C\u074E\u0756\u075A" +
    "\u0763\u0767\u0777\u0780\u0798\u079F\u07A2\u07AD\u07B6\u07BD\u07C3\u07CB" +
    "\u07D0\u07D4\u07DB\u07E0\u07E8\u07EC\u07EF\u07F3\u07FC\u07FF\u080A\u0810" +
    "\u0822\u0827\u082C\u0833\u0835\u083A\u0841\u0843\u0848\u084F\u0851\u0856" +
    "\u085D\u085F\u0866\u0879\u0880\u0883\u0888\u088E\u0896\u08A6\u08AF\u08B9" +
    "\u08C0\u08C3";
FlinkSqlParser._serializedATN = Utils.join([
    FlinkSqlParser._serializedATNSegment0,
    FlinkSqlParser._serializedATNSegment1,
    FlinkSqlParser._serializedATNSegment2,
    FlinkSqlParser._serializedATNSegment3,
    FlinkSqlParser._serializedATNSegment4,
], "");
export class ProgramContext extends ParserRuleContext {
    EOF() { return this.getToken(FlinkSqlParser.EOF, 0); }
    singleStatement(i) {
        if (i === undefined) {
            return this.getRuleContexts(SingleStatementContext);
        }
        else {
            return this.getRuleContext(i, SingleStatementContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_program; }
    // @Override
    enterRule(listener) {
        if (listener.enterProgram) {
            listener.enterProgram(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitProgram) {
            listener.exitProgram(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitProgram) {
            return visitor.visitProgram(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SingleStatementContext extends ParserRuleContext {
    sqlStatement() {
        return this.tryGetRuleContext(0, SqlStatementContext);
    }
    SEMICOLON() { return this.tryGetToken(FlinkSqlParser.SEMICOLON, 0); }
    emptyStatement() {
        return this.tryGetRuleContext(0, EmptyStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_singleStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterSingleStatement) {
            listener.enterSingleStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSingleStatement) {
            listener.exitSingleStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSingleStatement) {
            return visitor.visitSingleStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SqlStatementContext extends ParserRuleContext {
    ddlStatement() {
        return this.tryGetRuleContext(0, DdlStatementContext);
    }
    dmlStatement() {
        return this.tryGetRuleContext(0, DmlStatementContext);
    }
    describeStatement() {
        return this.tryGetRuleContext(0, DescribeStatementContext);
    }
    explainStatement() {
        return this.tryGetRuleContext(0, ExplainStatementContext);
    }
    useStatement() {
        return this.tryGetRuleContext(0, UseStatementContext);
    }
    showStatememt() {
        return this.tryGetRuleContext(0, ShowStatememtContext);
    }
    loadStatement() {
        return this.tryGetRuleContext(0, LoadStatementContext);
    }
    unloadStatememt() {
        return this.tryGetRuleContext(0, UnloadStatememtContext);
    }
    setStatememt() {
        return this.tryGetRuleContext(0, SetStatememtContext);
    }
    resetStatememt() {
        return this.tryGetRuleContext(0, ResetStatememtContext);
    }
    jarStatememt() {
        return this.tryGetRuleContext(0, JarStatememtContext);
    }
    dtAddStatement() {
        return this.tryGetRuleContext(0, DtAddStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_sqlStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterSqlStatement) {
            listener.enterSqlStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSqlStatement) {
            listener.exitSqlStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSqlStatement) {
            return visitor.visitSqlStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class EmptyStatementContext extends ParserRuleContext {
    SEMICOLON() { return this.getToken(FlinkSqlParser.SEMICOLON, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_emptyStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterEmptyStatement) {
            listener.enterEmptyStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitEmptyStatement) {
            listener.exitEmptyStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitEmptyStatement) {
            return visitor.visitEmptyStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DdlStatementContext extends ParserRuleContext {
    createTable() {
        return this.tryGetRuleContext(0, CreateTableContext);
    }
    createDatabase() {
        return this.tryGetRuleContext(0, CreateDatabaseContext);
    }
    createView() {
        return this.tryGetRuleContext(0, CreateViewContext);
    }
    createFunction() {
        return this.tryGetRuleContext(0, CreateFunctionContext);
    }
    createCatalog() {
        return this.tryGetRuleContext(0, CreateCatalogContext);
    }
    alterTable() {
        return this.tryGetRuleContext(0, AlterTableContext);
    }
    alertView() {
        return this.tryGetRuleContext(0, AlertViewContext);
    }
    alterDatabase() {
        return this.tryGetRuleContext(0, AlterDatabaseContext);
    }
    alterFunction() {
        return this.tryGetRuleContext(0, AlterFunctionContext);
    }
    dropCatalog() {
        return this.tryGetRuleContext(0, DropCatalogContext);
    }
    dropTable() {
        return this.tryGetRuleContext(0, DropTableContext);
    }
    dropDatabase() {
        return this.tryGetRuleContext(0, DropDatabaseContext);
    }
    dropView() {
        return this.tryGetRuleContext(0, DropViewContext);
    }
    dropFunction() {
        return this.tryGetRuleContext(0, DropFunctionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_ddlStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterDdlStatement) {
            listener.enterDdlStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDdlStatement) {
            listener.exitDdlStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDdlStatement) {
            return visitor.visitDdlStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DmlStatementContext extends ParserRuleContext {
    queryStatement() {
        return this.tryGetRuleContext(0, QueryStatementContext);
    }
    insertStatement() {
        return this.tryGetRuleContext(0, InsertStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dmlStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterDmlStatement) {
            listener.enterDmlStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDmlStatement) {
            listener.exitDmlStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDmlStatement) {
            return visitor.visitDmlStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DescribeStatementContext extends ParserRuleContext {
    tablePath() {
        return this.getRuleContext(0, TablePathContext);
    }
    KW_DESCRIBE() { return this.tryGetToken(FlinkSqlParser.KW_DESCRIBE, 0); }
    KW_DESC() { return this.tryGetToken(FlinkSqlParser.KW_DESC, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_describeStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterDescribeStatement) {
            listener.enterDescribeStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDescribeStatement) {
            listener.exitDescribeStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDescribeStatement) {
            return visitor.visitDescribeStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ExplainStatementContext extends ParserRuleContext {
    KW_EXPLAIN() { return this.getToken(FlinkSqlParser.KW_EXPLAIN, 0); }
    dmlStatement() {
        return this.tryGetRuleContext(0, DmlStatementContext);
    }
    insertSimpleStatement() {
        return this.tryGetRuleContext(0, InsertSimpleStatementContext);
    }
    insertMulStatement() {
        return this.tryGetRuleContext(0, InsertMulStatementContext);
    }
    explainDetails() {
        return this.tryGetRuleContext(0, ExplainDetailsContext);
    }
    KW_PLAN() { return this.tryGetToken(FlinkSqlParser.KW_PLAN, 0); }
    KW_FOR() { return this.tryGetToken(FlinkSqlParser.KW_FOR, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_explainStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterExplainStatement) {
            listener.enterExplainStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitExplainStatement) {
            listener.exitExplainStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitExplainStatement) {
            return visitor.visitExplainStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ExplainDetailsContext extends ParserRuleContext {
    explainDetail(i) {
        if (i === undefined) {
            return this.getRuleContexts(ExplainDetailContext);
        }
        else {
            return this.getRuleContext(i, ExplainDetailContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_explainDetails; }
    // @Override
    enterRule(listener) {
        if (listener.enterExplainDetails) {
            listener.enterExplainDetails(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitExplainDetails) {
            listener.exitExplainDetails(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitExplainDetails) {
            return visitor.visitExplainDetails(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ExplainDetailContext extends ParserRuleContext {
    KW_CHANGELOG_MODE() { return this.tryGetToken(FlinkSqlParser.KW_CHANGELOG_MODE, 0); }
    KW_JSON_EXECUTION_PLAN() { return this.tryGetToken(FlinkSqlParser.KW_JSON_EXECUTION_PLAN, 0); }
    KW_ESTIMATED_COST() { return this.tryGetToken(FlinkSqlParser.KW_ESTIMATED_COST, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_explainDetail; }
    // @Override
    enterRule(listener) {
        if (listener.enterExplainDetail) {
            listener.enterExplainDetail(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitExplainDetail) {
            listener.exitExplainDetail(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitExplainDetail) {
            return visitor.visitExplainDetail(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UseStatementContext extends ParserRuleContext {
    KW_USE() { return this.tryGetToken(FlinkSqlParser.KW_USE, 0); }
    KW_CATALOG() { return this.tryGetToken(FlinkSqlParser.KW_CATALOG, 0); }
    catalogPath() {
        return this.tryGetRuleContext(0, CatalogPathContext);
    }
    databasePath() {
        return this.tryGetRuleContext(0, DatabasePathContext);
    }
    useModuleStatement() {
        return this.tryGetRuleContext(0, UseModuleStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_useStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterUseStatement) {
            listener.enterUseStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUseStatement) {
            listener.exitUseStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUseStatement) {
            return visitor.visitUseStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UseModuleStatementContext extends ParserRuleContext {
    KW_USE() { return this.getToken(FlinkSqlParser.KW_USE, 0); }
    KW_MODULES() { return this.getToken(FlinkSqlParser.KW_MODULES, 0); }
    uid(i) {
        if (i === undefined) {
            return this.getRuleContexts(UidContext);
        }
        else {
            return this.getRuleContext(i, UidContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_useModuleStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterUseModuleStatement) {
            listener.enterUseModuleStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUseModuleStatement) {
            listener.exitUseModuleStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUseModuleStatement) {
            return visitor.visitUseModuleStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ShowStatememtContext extends ParserRuleContext {
    KW_SHOW() { return this.getToken(FlinkSqlParser.KW_SHOW, 0); }
    KW_CATALOGS() { return this.tryGetToken(FlinkSqlParser.KW_CATALOGS, 0); }
    KW_DATABASES() { return this.tryGetToken(FlinkSqlParser.KW_DATABASES, 0); }
    KW_VIEWS() { return this.tryGetToken(FlinkSqlParser.KW_VIEWS, 0); }
    KW_JARS() { return this.tryGetToken(FlinkSqlParser.KW_JARS, 0); }
    KW_CURRENT() { return this.tryGetToken(FlinkSqlParser.KW_CURRENT, 0); }
    KW_CATALOG() { return this.tryGetToken(FlinkSqlParser.KW_CATALOG, 0); }
    KW_DATABASE() { return this.tryGetToken(FlinkSqlParser.KW_DATABASE, 0); }
    KW_TABLES() { return this.tryGetToken(FlinkSqlParser.KW_TABLES, 0); }
    databasePath() {
        return this.tryGetRuleContext(0, DatabasePathContext);
    }
    likePredicate() {
        return this.tryGetRuleContext(0, LikePredicateContext);
    }
    KW_FROM() { return this.tryGetToken(FlinkSqlParser.KW_FROM, 0); }
    KW_IN() { return this.tryGetToken(FlinkSqlParser.KW_IN, 0); }
    KW_COLUMNS() { return this.tryGetToken(FlinkSqlParser.KW_COLUMNS, 0); }
    viewPath() {
        return this.tryGetRuleContext(0, ViewPathContext);
    }
    tablePath() {
        return this.tryGetRuleContext(0, TablePathContext);
    }
    KW_CREATE() { return this.tryGetToken(FlinkSqlParser.KW_CREATE, 0); }
    KW_TABLE() { return this.tryGetToken(FlinkSqlParser.KW_TABLE, 0); }
    KW_VIEW() { return this.tryGetToken(FlinkSqlParser.KW_VIEW, 0); }
    KW_FUNCTIONS() { return this.tryGetToken(FlinkSqlParser.KW_FUNCTIONS, 0); }
    KW_USER() { return this.tryGetToken(FlinkSqlParser.KW_USER, 0); }
    KW_MODULES() { return this.tryGetToken(FlinkSqlParser.KW_MODULES, 0); }
    KW_FULL() { return this.tryGetToken(FlinkSqlParser.KW_FULL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_showStatememt; }
    // @Override
    enterRule(listener) {
        if (listener.enterShowStatememt) {
            listener.enterShowStatememt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitShowStatememt) {
            listener.exitShowStatememt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitShowStatememt) {
            return visitor.visitShowStatememt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LoadStatementContext extends ParserRuleContext {
    KW_LOAD() { return this.getToken(FlinkSqlParser.KW_LOAD, 0); }
    KW_MODULE() { return this.getToken(FlinkSqlParser.KW_MODULE, 0); }
    uid() {
        return this.getRuleContext(0, UidContext);
    }
    KW_WITH() { return this.tryGetToken(FlinkSqlParser.KW_WITH, 0); }
    tablePropertyList() {
        return this.tryGetRuleContext(0, TablePropertyListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_loadStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterLoadStatement) {
            listener.enterLoadStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLoadStatement) {
            listener.exitLoadStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLoadStatement) {
            return visitor.visitLoadStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UnloadStatememtContext extends ParserRuleContext {
    KW_UNLOAD() { return this.getToken(FlinkSqlParser.KW_UNLOAD, 0); }
    KW_MODULE() { return this.getToken(FlinkSqlParser.KW_MODULE, 0); }
    uid() {
        return this.getRuleContext(0, UidContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_unloadStatememt; }
    // @Override
    enterRule(listener) {
        if (listener.enterUnloadStatememt) {
            listener.enterUnloadStatememt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUnloadStatememt) {
            listener.exitUnloadStatememt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUnloadStatememt) {
            return visitor.visitUnloadStatememt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SetStatememtContext extends ParserRuleContext {
    KW_SET() { return this.getToken(FlinkSqlParser.KW_SET, 0); }
    tableProperty() {
        return this.tryGetRuleContext(0, TablePropertyContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_setStatememt; }
    // @Override
    enterRule(listener) {
        if (listener.enterSetStatememt) {
            listener.enterSetStatememt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSetStatememt) {
            listener.exitSetStatememt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSetStatememt) {
            return visitor.visitSetStatememt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ResetStatememtContext extends ParserRuleContext {
    KW_RESET() { return this.getToken(FlinkSqlParser.KW_RESET, 0); }
    tablePropertyKey() {
        return this.tryGetRuleContext(0, TablePropertyKeyContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_resetStatememt; }
    // @Override
    enterRule(listener) {
        if (listener.enterResetStatememt) {
            listener.enterResetStatememt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitResetStatememt) {
            listener.exitResetStatememt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitResetStatememt) {
            return visitor.visitResetStatememt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class JarStatememtContext extends ParserRuleContext {
    KW_JAR() { return this.getToken(FlinkSqlParser.KW_JAR, 0); }
    jarFileName() {
        return this.getRuleContext(0, JarFileNameContext);
    }
    KW_ADD() { return this.tryGetToken(FlinkSqlParser.KW_ADD, 0); }
    KW_REMOVE() { return this.tryGetToken(FlinkSqlParser.KW_REMOVE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_jarStatememt; }
    // @Override
    enterRule(listener) {
        if (listener.enterJarStatememt) {
            listener.enterJarStatememt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitJarStatememt) {
            listener.exitJarStatememt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitJarStatememt) {
            return visitor.visitJarStatememt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DtAddStatementContext extends ParserRuleContext {
    KW_ADD() { return this.getToken(FlinkSqlParser.KW_ADD, 0); }
    KW_JAR() { return this.tryGetToken(FlinkSqlParser.KW_JAR, 0); }
    KW_WITH() { return this.tryGetToken(FlinkSqlParser.KW_WITH, 0); }
    dtFilePath() {
        return this.getRuleContext(0, DtFilePathContext);
    }
    KW_AS() { return this.tryGetToken(FlinkSqlParser.KW_AS, 0); }
    uid(i) {
        if (i === undefined) {
            return this.getRuleContexts(UidContext);
        }
        else {
            return this.getRuleContext(i, UidContext);
        }
    }
    KW_FILE() { return this.tryGetToken(FlinkSqlParser.KW_FILE, 0); }
    KW_RENAME() { return this.tryGetToken(FlinkSqlParser.KW_RENAME, 0); }
    KW_PYTHON_FILES() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_FILES, 0); }
    KW_PYTHON_REQUIREMENTS() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_REQUIREMENTS, 0); }
    KW_PYTHON_DEPENDENCIES() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_DEPENDENCIES, 0); }
    KW_PYTHON_JAR() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_JAR, 0); }
    KW_PYTHON_ARCHIVES() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_ARCHIVES, 0); }
    KW_PYTHON_PARAMETER() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_PARAMETER, 0); }
    KW_ENGINE() { return this.tryGetToken(FlinkSqlParser.KW_ENGINE, 0); }
    KW_KEY() { return this.tryGetToken(FlinkSqlParser.KW_KEY, 0); }
    KW_CONFIG() { return this.tryGetToken(FlinkSqlParser.KW_CONFIG, 0); }
    KW_FOR() { return this.tryGetToken(FlinkSqlParser.KW_FOR, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dtAddStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterDtAddStatement) {
            listener.enterDtAddStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDtAddStatement) {
            listener.exitDtAddStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDtAddStatement) {
            return visitor.visitDtAddStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DtFilePathContext extends ParserRuleContext {
    uid(i) {
        if (i === undefined) {
            return this.getRuleContexts(UidContext);
        }
        else {
            return this.getRuleContext(i, UidContext);
        }
    }
    SLASH_SIGN(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.SLASH_SIGN);
        }
        else {
            return this.getToken(FlinkSqlParser.SLASH_SIGN, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dtFilePath; }
    // @Override
    enterRule(listener) {
        if (listener.enterDtFilePath) {
            listener.enterDtFilePath(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDtFilePath) {
            listener.exitDtFilePath(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDtFilePath) {
            return visitor.visitDtFilePath(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CreateTableContext extends ParserRuleContext {
    simpleCreateTable() {
        return this.tryGetRuleContext(0, SimpleCreateTableContext);
    }
    createTableAsSelect() {
        return this.tryGetRuleContext(0, CreateTableAsSelectContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_createTable; }
    // @Override
    enterRule(listener) {
        if (listener.enterCreateTable) {
            listener.enterCreateTable(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCreateTable) {
            listener.exitCreateTable(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCreateTable) {
            return visitor.visitCreateTable(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SimpleCreateTableContext extends ParserRuleContext {
    KW_CREATE() { return this.getToken(FlinkSqlParser.KW_CREATE, 0); }
    KW_TABLE() { return this.getToken(FlinkSqlParser.KW_TABLE, 0); }
    tablePathCreate() {
        return this.getRuleContext(0, TablePathCreateContext);
    }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    columnOptionDefinition(i) {
        if (i === undefined) {
            return this.getRuleContexts(ColumnOptionDefinitionContext);
        }
        else {
            return this.getRuleContext(i, ColumnOptionDefinitionContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    withOption() {
        return this.getRuleContext(0, WithOptionContext);
    }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    ifNotExists() {
        return this.tryGetRuleContext(0, IfNotExistsContext);
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    watermarkDefinition() {
        return this.tryGetRuleContext(0, WatermarkDefinitionContext);
    }
    tableConstraint() {
        return this.tryGetRuleContext(0, TableConstraintContext);
    }
    selfDefinitionClause() {
        return this.tryGetRuleContext(0, SelfDefinitionClauseContext);
    }
    commentSpec() {
        return this.tryGetRuleContext(0, CommentSpecContext);
    }
    partitionDefinition() {
        return this.tryGetRuleContext(0, PartitionDefinitionContext);
    }
    likeDefinition() {
        return this.tryGetRuleContext(0, LikeDefinitionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_simpleCreateTable; }
    // @Override
    enterRule(listener) {
        if (listener.enterSimpleCreateTable) {
            listener.enterSimpleCreateTable(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSimpleCreateTable) {
            listener.exitSimpleCreateTable(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSimpleCreateTable) {
            return visitor.visitSimpleCreateTable(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CreateTableAsSelectContext extends ParserRuleContext {
    KW_CREATE() { return this.getToken(FlinkSqlParser.KW_CREATE, 0); }
    KW_TABLE() { return this.getToken(FlinkSqlParser.KW_TABLE, 0); }
    tablePathCreate() {
        return this.getRuleContext(0, TablePathCreateContext);
    }
    withOption() {
        return this.getRuleContext(0, WithOptionContext);
    }
    ifNotExists() {
        return this.tryGetRuleContext(0, IfNotExistsContext);
    }
    KW_AS() { return this.tryGetToken(FlinkSqlParser.KW_AS, 0); }
    queryStatement() {
        return this.tryGetRuleContext(0, QueryStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_createTableAsSelect; }
    // @Override
    enterRule(listener) {
        if (listener.enterCreateTableAsSelect) {
            listener.enterCreateTableAsSelect(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCreateTableAsSelect) {
            listener.exitCreateTableAsSelect(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCreateTableAsSelect) {
            return visitor.visitCreateTableAsSelect(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnOptionDefinitionContext extends ParserRuleContext {
    physicalColumnDefinition() {
        return this.tryGetRuleContext(0, PhysicalColumnDefinitionContext);
    }
    metadataColumnDefinition() {
        return this.tryGetRuleContext(0, MetadataColumnDefinitionContext);
    }
    computedColumnDefinition() {
        return this.tryGetRuleContext(0, ComputedColumnDefinitionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_columnOptionDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnOptionDefinition) {
            listener.enterColumnOptionDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnOptionDefinition) {
            listener.exitColumnOptionDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnOptionDefinition) {
            return visitor.visitColumnOptionDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PhysicalColumnDefinitionContext extends ParserRuleContext {
    columnNameCreate() {
        return this.getRuleContext(0, ColumnNameCreateContext);
    }
    columnType() {
        return this.getRuleContext(0, ColumnTypeContext);
    }
    columnConstraint() {
        return this.tryGetRuleContext(0, ColumnConstraintContext);
    }
    commentSpec() {
        return this.tryGetRuleContext(0, CommentSpecContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_physicalColumnDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterPhysicalColumnDefinition) {
            listener.enterPhysicalColumnDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPhysicalColumnDefinition) {
            listener.exitPhysicalColumnDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPhysicalColumnDefinition) {
            return visitor.visitPhysicalColumnDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnNameCreateContext extends ParserRuleContext {
    uid() {
        return this.tryGetRuleContext(0, UidContext);
    }
    expression() {
        return this.tryGetRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_columnNameCreate; }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnNameCreate) {
            listener.enterColumnNameCreate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnNameCreate) {
            listener.exitColumnNameCreate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnNameCreate) {
            return visitor.visitColumnNameCreate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnNameContext extends ParserRuleContext {
    uid() {
        return this.tryGetRuleContext(0, UidContext);
    }
    expression() {
        return this.tryGetRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_columnName; }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnName) {
            listener.enterColumnName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnName) {
            listener.exitColumnName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnName) {
            return visitor.visitColumnName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnNameListContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    columnName(i) {
        if (i === undefined) {
            return this.getRuleContexts(ColumnNameContext);
        }
        else {
            return this.getRuleContext(i, ColumnNameContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_columnNameList; }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnNameList) {
            listener.enterColumnNameList(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnNameList) {
            listener.exitColumnNameList(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnNameList) {
            return visitor.visitColumnNameList(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnTypeContext extends ParserRuleContext {
    KW_DATE() { return this.tryGetToken(FlinkSqlParser.KW_DATE, 0); }
    KW_BOOLEAN() { return this.tryGetToken(FlinkSqlParser.KW_BOOLEAN, 0); }
    KW_NULL() { return this.tryGetToken(FlinkSqlParser.KW_NULL, 0); }
    KW_CHAR() { return this.tryGetToken(FlinkSqlParser.KW_CHAR, 0); }
    KW_VARCHAR() { return this.tryGetToken(FlinkSqlParser.KW_VARCHAR, 0); }
    KW_STRING() { return this.tryGetToken(FlinkSqlParser.KW_STRING, 0); }
    KW_BINARY() { return this.tryGetToken(FlinkSqlParser.KW_BINARY, 0); }
    KW_VARBINARY() { return this.tryGetToken(FlinkSqlParser.KW_VARBINARY, 0); }
    KW_BYTES() { return this.tryGetToken(FlinkSqlParser.KW_BYTES, 0); }
    KW_TINYINT() { return this.tryGetToken(FlinkSqlParser.KW_TINYINT, 0); }
    KW_SMALLINT() { return this.tryGetToken(FlinkSqlParser.KW_SMALLINT, 0); }
    KW_INT() { return this.tryGetToken(FlinkSqlParser.KW_INT, 0); }
    KW_INTEGER() { return this.tryGetToken(FlinkSqlParser.KW_INTEGER, 0); }
    KW_BIGINT() { return this.tryGetToken(FlinkSqlParser.KW_BIGINT, 0); }
    KW_TIME() { return this.tryGetToken(FlinkSqlParser.KW_TIME, 0); }
    KW_TIMESTAMP_LTZ() { return this.tryGetToken(FlinkSqlParser.KW_TIMESTAMP_LTZ, 0); }
    KW_DATETIME() { return this.tryGetToken(FlinkSqlParser.KW_DATETIME, 0); }
    lengthOneDimension() {
        return this.tryGetRuleContext(0, LengthOneDimensionContext);
    }
    KW_TIMESTAMP() { return this.tryGetToken(FlinkSqlParser.KW_TIMESTAMP, 0); }
    KW_ZONE() { return this.tryGetToken(FlinkSqlParser.KW_ZONE, 0); }
    KW_WITHOUT() { return this.tryGetToken(FlinkSqlParser.KW_WITHOUT, 0); }
    KW_WITH() { return this.tryGetToken(FlinkSqlParser.KW_WITH, 0); }
    KW_LOCAL() { return this.tryGetToken(FlinkSqlParser.KW_LOCAL, 0); }
    KW_DECIMAL() { return this.tryGetToken(FlinkSqlParser.KW_DECIMAL, 0); }
    KW_DEC() { return this.tryGetToken(FlinkSqlParser.KW_DEC, 0); }
    KW_NUMERIC() { return this.tryGetToken(FlinkSqlParser.KW_NUMERIC, 0); }
    KW_FLOAT() { return this.tryGetToken(FlinkSqlParser.KW_FLOAT, 0); }
    KW_DOUBLE() { return this.tryGetToken(FlinkSqlParser.KW_DOUBLE, 0); }
    lengthTwoOptionalDimension() {
        return this.tryGetRuleContext(0, LengthTwoOptionalDimensionContext);
    }
    KW_ARRAY() { return this.tryGetToken(FlinkSqlParser.KW_ARRAY, 0); }
    KW_MULTISET() { return this.tryGetToken(FlinkSqlParser.KW_MULTISET, 0); }
    lengthOneTypeDimension() {
        return this.tryGetRuleContext(0, LengthOneTypeDimensionContext);
    }
    KW_MAP() { return this.tryGetToken(FlinkSqlParser.KW_MAP, 0); }
    mapTypeDimension() {
        return this.tryGetRuleContext(0, MapTypeDimensionContext);
    }
    KW_ROW() { return this.tryGetToken(FlinkSqlParser.KW_ROW, 0); }
    rowTypeDimension() {
        return this.tryGetRuleContext(0, RowTypeDimensionContext);
    }
    KW_RAW() { return this.tryGetToken(FlinkSqlParser.KW_RAW, 0); }
    lengthTwoStringDimension() {
        return this.tryGetRuleContext(0, LengthTwoStringDimensionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_columnType; }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnType) {
            listener.enterColumnType(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnType) {
            listener.exitColumnType(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnType) {
            return visitor.visitColumnType(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LengthOneDimensionContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    decimalLiteral() {
        return this.getRuleContext(0, DecimalLiteralContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_lengthOneDimension; }
    // @Override
    enterRule(listener) {
        if (listener.enterLengthOneDimension) {
            listener.enterLengthOneDimension(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLengthOneDimension) {
            listener.exitLengthOneDimension(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLengthOneDimension) {
            return visitor.visitLengthOneDimension(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LengthTwoOptionalDimensionContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    decimalLiteral(i) {
        if (i === undefined) {
            return this.getRuleContexts(DecimalLiteralContext);
        }
        else {
            return this.getRuleContext(i, DecimalLiteralContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA() { return this.tryGetToken(FlinkSqlParser.COMMA, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_lengthTwoOptionalDimension; }
    // @Override
    enterRule(listener) {
        if (listener.enterLengthTwoOptionalDimension) {
            listener.enterLengthTwoOptionalDimension(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLengthTwoOptionalDimension) {
            listener.exitLengthTwoOptionalDimension(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLengthTwoOptionalDimension) {
            return visitor.visitLengthTwoOptionalDimension(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LengthTwoStringDimensionContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    stringLiteral(i) {
        if (i === undefined) {
            return this.getRuleContexts(StringLiteralContext);
        }
        else {
            return this.getRuleContext(i, StringLiteralContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA() { return this.tryGetToken(FlinkSqlParser.COMMA, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_lengthTwoStringDimension; }
    // @Override
    enterRule(listener) {
        if (listener.enterLengthTwoStringDimension) {
            listener.enterLengthTwoStringDimension(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLengthTwoStringDimension) {
            listener.exitLengthTwoStringDimension(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLengthTwoStringDimension) {
            return visitor.visitLengthTwoStringDimension(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LengthOneTypeDimensionContext extends ParserRuleContext {
    LESS_SYMBOL() { return this.getToken(FlinkSqlParser.LESS_SYMBOL, 0); }
    columnType() {
        return this.getRuleContext(0, ColumnTypeContext);
    }
    GREATER_SYMBOL() { return this.getToken(FlinkSqlParser.GREATER_SYMBOL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_lengthOneTypeDimension; }
    // @Override
    enterRule(listener) {
        if (listener.enterLengthOneTypeDimension) {
            listener.enterLengthOneTypeDimension(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLengthOneTypeDimension) {
            listener.exitLengthOneTypeDimension(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLengthOneTypeDimension) {
            return visitor.visitLengthOneTypeDimension(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MapTypeDimensionContext extends ParserRuleContext {
    LESS_SYMBOL() { return this.getToken(FlinkSqlParser.LESS_SYMBOL, 0); }
    columnType(i) {
        if (i === undefined) {
            return this.getRuleContexts(ColumnTypeContext);
        }
        else {
            return this.getRuleContext(i, ColumnTypeContext);
        }
    }
    GREATER_SYMBOL() { return this.getToken(FlinkSqlParser.GREATER_SYMBOL, 0); }
    COMMA() { return this.tryGetToken(FlinkSqlParser.COMMA, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_mapTypeDimension; }
    // @Override
    enterRule(listener) {
        if (listener.enterMapTypeDimension) {
            listener.enterMapTypeDimension(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMapTypeDimension) {
            listener.exitMapTypeDimension(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMapTypeDimension) {
            return visitor.visitMapTypeDimension(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class RowTypeDimensionContext extends ParserRuleContext {
    LESS_SYMBOL() { return this.getToken(FlinkSqlParser.LESS_SYMBOL, 0); }
    columnName(i) {
        if (i === undefined) {
            return this.getRuleContexts(ColumnNameContext);
        }
        else {
            return this.getRuleContext(i, ColumnNameContext);
        }
    }
    columnType(i) {
        if (i === undefined) {
            return this.getRuleContexts(ColumnTypeContext);
        }
        else {
            return this.getRuleContext(i, ColumnTypeContext);
        }
    }
    GREATER_SYMBOL() { return this.getToken(FlinkSqlParser.GREATER_SYMBOL, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_rowTypeDimension; }
    // @Override
    enterRule(listener) {
        if (listener.enterRowTypeDimension) {
            listener.enterRowTypeDimension(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitRowTypeDimension) {
            listener.exitRowTypeDimension(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitRowTypeDimension) {
            return visitor.visitRowTypeDimension(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnConstraintContext extends ParserRuleContext {
    KW_PRIMARY() { return this.tryGetToken(FlinkSqlParser.KW_PRIMARY, 0); }
    KW_KEY() { return this.tryGetToken(FlinkSqlParser.KW_KEY, 0); }
    KW_CONSTRAINT() { return this.tryGetToken(FlinkSqlParser.KW_CONSTRAINT, 0); }
    constraintName() {
        return this.tryGetRuleContext(0, ConstraintNameContext);
    }
    KW_NOT() { return this.tryGetToken(FlinkSqlParser.KW_NOT, 0); }
    KW_ENFORCED() { return this.tryGetToken(FlinkSqlParser.KW_ENFORCED, 0); }
    KW_NULL() { return this.tryGetToken(FlinkSqlParser.KW_NULL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_columnConstraint; }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnConstraint) {
            listener.enterColumnConstraint(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnConstraint) {
            listener.exitColumnConstraint(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnConstraint) {
            return visitor.visitColumnConstraint(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CommentSpecContext extends ParserRuleContext {
    KW_COMMENT() { return this.getToken(FlinkSqlParser.KW_COMMENT, 0); }
    STRING_LITERAL() { return this.getToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_commentSpec; }
    // @Override
    enterRule(listener) {
        if (listener.enterCommentSpec) {
            listener.enterCommentSpec(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCommentSpec) {
            listener.exitCommentSpec(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCommentSpec) {
            return visitor.visitCommentSpec(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MetadataColumnDefinitionContext extends ParserRuleContext {
    columnNameCreate() {
        return this.getRuleContext(0, ColumnNameCreateContext);
    }
    columnType() {
        return this.getRuleContext(0, ColumnTypeContext);
    }
    KW_METADATA() { return this.getToken(FlinkSqlParser.KW_METADATA, 0); }
    KW_FROM() { return this.tryGetToken(FlinkSqlParser.KW_FROM, 0); }
    metadataKey() {
        return this.tryGetRuleContext(0, MetadataKeyContext);
    }
    KW_VIRTUAL() { return this.tryGetToken(FlinkSqlParser.KW_VIRTUAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_metadataColumnDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterMetadataColumnDefinition) {
            listener.enterMetadataColumnDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMetadataColumnDefinition) {
            listener.exitMetadataColumnDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMetadataColumnDefinition) {
            return visitor.visitMetadataColumnDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MetadataKeyContext extends ParserRuleContext {
    STRING_LITERAL() { return this.getToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_metadataKey; }
    // @Override
    enterRule(listener) {
        if (listener.enterMetadataKey) {
            listener.enterMetadataKey(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMetadataKey) {
            listener.exitMetadataKey(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMetadataKey) {
            return visitor.visitMetadataKey(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ComputedColumnDefinitionContext extends ParserRuleContext {
    columnNameCreate() {
        return this.getRuleContext(0, ColumnNameCreateContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    computedColumnExpression() {
        return this.getRuleContext(0, ComputedColumnExpressionContext);
    }
    commentSpec() {
        return this.tryGetRuleContext(0, CommentSpecContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_computedColumnDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterComputedColumnDefinition) {
            listener.enterComputedColumnDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitComputedColumnDefinition) {
            listener.exitComputedColumnDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitComputedColumnDefinition) {
            return visitor.visitComputedColumnDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ComputedColumnExpressionContext extends ParserRuleContext {
    expression() {
        return this.getRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_computedColumnExpression; }
    // @Override
    enterRule(listener) {
        if (listener.enterComputedColumnExpression) {
            listener.enterComputedColumnExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitComputedColumnExpression) {
            listener.exitComputedColumnExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitComputedColumnExpression) {
            return visitor.visitComputedColumnExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WatermarkDefinitionContext extends ParserRuleContext {
    KW_WATERMARK() { return this.getToken(FlinkSqlParser.KW_WATERMARK, 0); }
    KW_FOR() { return this.getToken(FlinkSqlParser.KW_FOR, 0); }
    columnName() {
        return this.getRuleContext(0, ColumnNameContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    expression() {
        return this.getRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_watermarkDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterWatermarkDefinition) {
            listener.enterWatermarkDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWatermarkDefinition) {
            listener.exitWatermarkDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWatermarkDefinition) {
            return visitor.visitWatermarkDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TableConstraintContext extends ParserRuleContext {
    KW_PRIMARY() { return this.getToken(FlinkSqlParser.KW_PRIMARY, 0); }
    KW_KEY() { return this.getToken(FlinkSqlParser.KW_KEY, 0); }
    columnNameList() {
        return this.getRuleContext(0, ColumnNameListContext);
    }
    KW_NOT() { return this.getToken(FlinkSqlParser.KW_NOT, 0); }
    KW_ENFORCED() { return this.getToken(FlinkSqlParser.KW_ENFORCED, 0); }
    KW_CONSTRAINT() { return this.tryGetToken(FlinkSqlParser.KW_CONSTRAINT, 0); }
    constraintName() {
        return this.tryGetRuleContext(0, ConstraintNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tableConstraint; }
    // @Override
    enterRule(listener) {
        if (listener.enterTableConstraint) {
            listener.enterTableConstraint(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTableConstraint) {
            listener.exitTableConstraint(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTableConstraint) {
            return visitor.visitTableConstraint(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ConstraintNameContext extends ParserRuleContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_constraintName; }
    // @Override
    enterRule(listener) {
        if (listener.enterConstraintName) {
            listener.enterConstraintName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitConstraintName) {
            listener.exitConstraintName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitConstraintName) {
            return visitor.visitConstraintName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SelfDefinitionClauseContext extends ParserRuleContext {
    KW_PERIOD() { return this.getToken(FlinkSqlParser.KW_PERIOD, 0); }
    KW_FOR() { return this.getToken(FlinkSqlParser.KW_FOR, 0); }
    KW_SYSTEM_TIME() { return this.getToken(FlinkSqlParser.KW_SYSTEM_TIME, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_selfDefinitionClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterSelfDefinitionClause) {
            listener.enterSelfDefinitionClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSelfDefinitionClause) {
            listener.exitSelfDefinitionClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSelfDefinitionClause) {
            return visitor.visitSelfDefinitionClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PartitionDefinitionContext extends ParserRuleContext {
    KW_PARTITIONED() { return this.getToken(FlinkSqlParser.KW_PARTITIONED, 0); }
    KW_BY() { return this.getToken(FlinkSqlParser.KW_BY, 0); }
    transformList() {
        return this.getRuleContext(0, TransformListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_partitionDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterPartitionDefinition) {
            listener.enterPartitionDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPartitionDefinition) {
            listener.exitPartitionDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPartitionDefinition) {
            return visitor.visitPartitionDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TransformListContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    transform(i) {
        if (i === undefined) {
            return this.getRuleContexts(TransformContext);
        }
        else {
            return this.getRuleContext(i, TransformContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_transformList; }
    // @Override
    enterRule(listener) {
        if (listener.enterTransformList) {
            listener.enterTransformList(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTransformList) {
            listener.exitTransformList(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTransformList) {
            return visitor.visitTransformList(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TransformContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_transform; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class IdentityTransformContext extends TransformContext {
    columnName() {
        return this.getRuleContext(0, ColumnNameContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterIdentityTransform) {
            listener.enterIdentityTransform(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIdentityTransform) {
            listener.exitIdentityTransform(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIdentityTransform) {
            return visitor.visitIdentityTransform(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnTransformContext extends TransformContext {
    qualifiedName() {
        return this.getRuleContext(0, QualifiedNameContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnTransform) {
            listener.enterColumnTransform(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnTransform) {
            listener.exitColumnTransform(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnTransform) {
            return visitor.visitColumnTransform(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ApplyTransformContext extends TransformContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    transformArgument(i) {
        if (i === undefined) {
            return this.getRuleContexts(TransformArgumentContext);
        }
        else {
            return this.getRuleContext(i, TransformArgumentContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterApplyTransform) {
            listener.enterApplyTransform(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitApplyTransform) {
            listener.exitApplyTransform(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitApplyTransform) {
            return visitor.visitApplyTransform(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TransformArgumentContext extends ParserRuleContext {
    qualifiedName() {
        return this.tryGetRuleContext(0, QualifiedNameContext);
    }
    constant() {
        return this.tryGetRuleContext(0, ConstantContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_transformArgument; }
    // @Override
    enterRule(listener) {
        if (listener.enterTransformArgument) {
            listener.enterTransformArgument(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTransformArgument) {
            listener.exitTransformArgument(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTransformArgument) {
            return visitor.visitTransformArgument(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LikeDefinitionContext extends ParserRuleContext {
    KW_LIKE() { return this.getToken(FlinkSqlParser.KW_LIKE, 0); }
    tablePath() {
        return this.getRuleContext(0, TablePathContext);
    }
    LR_BRACKET() { return this.tryGetToken(FlinkSqlParser.LR_BRACKET, 0); }
    RR_BRACKET() { return this.tryGetToken(FlinkSqlParser.RR_BRACKET, 0); }
    likeOption(i) {
        if (i === undefined) {
            return this.getRuleContexts(LikeOptionContext);
        }
        else {
            return this.getRuleContext(i, LikeOptionContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_likeDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterLikeDefinition) {
            listener.enterLikeDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLikeDefinition) {
            listener.exitLikeDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLikeDefinition) {
            return visitor.visitLikeDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LikeOptionContext extends ParserRuleContext {
    KW_INCLUDING() { return this.tryGetToken(FlinkSqlParser.KW_INCLUDING, 0); }
    KW_EXCLUDING() { return this.tryGetToken(FlinkSqlParser.KW_EXCLUDING, 0); }
    KW_ALL() { return this.tryGetToken(FlinkSqlParser.KW_ALL, 0); }
    KW_CONSTRAINTS() { return this.tryGetToken(FlinkSqlParser.KW_CONSTRAINTS, 0); }
    KW_PARTITIONS() { return this.tryGetToken(FlinkSqlParser.KW_PARTITIONS, 0); }
    KW_OVERWRITING() { return this.tryGetToken(FlinkSqlParser.KW_OVERWRITING, 0); }
    KW_GENERATED() { return this.tryGetToken(FlinkSqlParser.KW_GENERATED, 0); }
    KW_OPTIONS() { return this.tryGetToken(FlinkSqlParser.KW_OPTIONS, 0); }
    KW_WATERMARKS() { return this.tryGetToken(FlinkSqlParser.KW_WATERMARKS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_likeOption; }
    // @Override
    enterRule(listener) {
        if (listener.enterLikeOption) {
            listener.enterLikeOption(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLikeOption) {
            listener.exitLikeOption(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLikeOption) {
            return visitor.visitLikeOption(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CreateCatalogContext extends ParserRuleContext {
    KW_CREATE() { return this.getToken(FlinkSqlParser.KW_CREATE, 0); }
    KW_CATALOG() { return this.getToken(FlinkSqlParser.KW_CATALOG, 0); }
    catalogPathCreate() {
        return this.getRuleContext(0, CatalogPathCreateContext);
    }
    withOption() {
        return this.getRuleContext(0, WithOptionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_createCatalog; }
    // @Override
    enterRule(listener) {
        if (listener.enterCreateCatalog) {
            listener.enterCreateCatalog(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCreateCatalog) {
            listener.exitCreateCatalog(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCreateCatalog) {
            return visitor.visitCreateCatalog(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CreateDatabaseContext extends ParserRuleContext {
    KW_CREATE() { return this.getToken(FlinkSqlParser.KW_CREATE, 0); }
    KW_DATABASE() { return this.getToken(FlinkSqlParser.KW_DATABASE, 0); }
    databasePathCreate() {
        return this.getRuleContext(0, DatabasePathCreateContext);
    }
    withOption() {
        return this.getRuleContext(0, WithOptionContext);
    }
    ifNotExists() {
        return this.tryGetRuleContext(0, IfNotExistsContext);
    }
    commentSpec() {
        return this.tryGetRuleContext(0, CommentSpecContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_createDatabase; }
    // @Override
    enterRule(listener) {
        if (listener.enterCreateDatabase) {
            listener.enterCreateDatabase(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCreateDatabase) {
            listener.exitCreateDatabase(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCreateDatabase) {
            return visitor.visitCreateDatabase(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CreateViewContext extends ParserRuleContext {
    KW_CREATE() { return this.getToken(FlinkSqlParser.KW_CREATE, 0); }
    KW_VIEW() { return this.getToken(FlinkSqlParser.KW_VIEW, 0); }
    viewPathCreate() {
        return this.getRuleContext(0, ViewPathCreateContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    queryStatement() {
        return this.getRuleContext(0, QueryStatementContext);
    }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    ifNotExists() {
        return this.tryGetRuleContext(0, IfNotExistsContext);
    }
    columnNameList() {
        return this.tryGetRuleContext(0, ColumnNameListContext);
    }
    commentSpec() {
        return this.tryGetRuleContext(0, CommentSpecContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_createView; }
    // @Override
    enterRule(listener) {
        if (listener.enterCreateView) {
            listener.enterCreateView(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCreateView) {
            listener.exitCreateView(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCreateView) {
            return visitor.visitCreateView(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CreateFunctionContext extends ParserRuleContext {
    KW_CREATE() { return this.getToken(FlinkSqlParser.KW_CREATE, 0); }
    KW_FUNCTION() { return this.getToken(FlinkSqlParser.KW_FUNCTION, 0); }
    functionNameCreate() {
        return this.getRuleContext(0, FunctionNameCreateContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    KW_SYSTEM() { return this.tryGetToken(FlinkSqlParser.KW_SYSTEM, 0); }
    ifNotExists() {
        return this.tryGetRuleContext(0, IfNotExistsContext);
    }
    KW_LANGUAGE() { return this.tryGetToken(FlinkSqlParser.KW_LANGUAGE, 0); }
    usingClause() {
        return this.tryGetRuleContext(0, UsingClauseContext);
    }
    KW_JAVA() { return this.tryGetToken(FlinkSqlParser.KW_JAVA, 0); }
    KW_SCALA() { return this.tryGetToken(FlinkSqlParser.KW_SCALA, 0); }
    KW_PYTHON() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_createFunction; }
    // @Override
    enterRule(listener) {
        if (listener.enterCreateFunction) {
            listener.enterCreateFunction(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCreateFunction) {
            listener.exitCreateFunction(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCreateFunction) {
            return visitor.visitCreateFunction(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UsingClauseContext extends ParserRuleContext {
    KW_USING() { return this.getToken(FlinkSqlParser.KW_USING, 0); }
    KW_JAR(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.KW_JAR);
        }
        else {
            return this.getToken(FlinkSqlParser.KW_JAR, i);
        }
    }
    jarFileName(i) {
        if (i === undefined) {
            return this.getRuleContexts(JarFileNameContext);
        }
        else {
            return this.getRuleContext(i, JarFileNameContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_usingClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterUsingClause) {
            listener.enterUsingClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUsingClause) {
            listener.exitUsingClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUsingClause) {
            return visitor.visitUsingClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class JarFileNameContext extends ParserRuleContext {
    STRING_LITERAL() { return this.getToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_jarFileName; }
    // @Override
    enterRule(listener) {
        if (listener.enterJarFileName) {
            listener.enterJarFileName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitJarFileName) {
            listener.exitJarFileName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitJarFileName) {
            return visitor.visitJarFileName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class AlterTableContext extends ParserRuleContext {
    KW_ALTER() { return this.getToken(FlinkSqlParser.KW_ALTER, 0); }
    KW_TABLE() { return this.getToken(FlinkSqlParser.KW_TABLE, 0); }
    tablePath() {
        return this.getRuleContext(0, TablePathContext);
    }
    renameDefinition() {
        return this.tryGetRuleContext(0, RenameDefinitionContext);
    }
    setKeyValueDefinition() {
        return this.tryGetRuleContext(0, SetKeyValueDefinitionContext);
    }
    addConstraint() {
        return this.tryGetRuleContext(0, AddConstraintContext);
    }
    dropConstraint() {
        return this.tryGetRuleContext(0, DropConstraintContext);
    }
    addUnique() {
        return this.tryGetRuleContext(0, AddUniqueContext);
    }
    ifExists() {
        return this.tryGetRuleContext(0, IfExistsContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_alterTable; }
    // @Override
    enterRule(listener) {
        if (listener.enterAlterTable) {
            listener.enterAlterTable(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitAlterTable) {
            listener.exitAlterTable(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitAlterTable) {
            return visitor.visitAlterTable(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class RenameDefinitionContext extends ParserRuleContext {
    KW_RENAME() { return this.getToken(FlinkSqlParser.KW_RENAME, 0); }
    KW_TO() { return this.getToken(FlinkSqlParser.KW_TO, 0); }
    uid(i) {
        if (i === undefined) {
            return this.getRuleContexts(UidContext);
        }
        else {
            return this.getRuleContext(i, UidContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_renameDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterRenameDefinition) {
            listener.enterRenameDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitRenameDefinition) {
            listener.exitRenameDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitRenameDefinition) {
            return visitor.visitRenameDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SetKeyValueDefinitionContext extends ParserRuleContext {
    KW_SET() { return this.getToken(FlinkSqlParser.KW_SET, 0); }
    tablePropertyList() {
        return this.getRuleContext(0, TablePropertyListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_setKeyValueDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterSetKeyValueDefinition) {
            listener.enterSetKeyValueDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSetKeyValueDefinition) {
            listener.exitSetKeyValueDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSetKeyValueDefinition) {
            return visitor.visitSetKeyValueDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class AddConstraintContext extends ParserRuleContext {
    KW_ADD() { return this.getToken(FlinkSqlParser.KW_ADD, 0); }
    KW_CONSTRAINT() { return this.getToken(FlinkSqlParser.KW_CONSTRAINT, 0); }
    constraintName() {
        return this.getRuleContext(0, ConstraintNameContext);
    }
    KW_PRIMARY() { return this.getToken(FlinkSqlParser.KW_PRIMARY, 0); }
    KW_KEY() { return this.getToken(FlinkSqlParser.KW_KEY, 0); }
    columnNameList() {
        return this.getRuleContext(0, ColumnNameListContext);
    }
    notForced() {
        return this.tryGetRuleContext(0, NotForcedContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_addConstraint; }
    // @Override
    enterRule(listener) {
        if (listener.enterAddConstraint) {
            listener.enterAddConstraint(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitAddConstraint) {
            listener.exitAddConstraint(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitAddConstraint) {
            return visitor.visitAddConstraint(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DropConstraintContext extends ParserRuleContext {
    KW_DROP() { return this.getToken(FlinkSqlParser.KW_DROP, 0); }
    KW_CONSTRAINT() { return this.getToken(FlinkSqlParser.KW_CONSTRAINT, 0); }
    constraintName() {
        return this.getRuleContext(0, ConstraintNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dropConstraint; }
    // @Override
    enterRule(listener) {
        if (listener.enterDropConstraint) {
            listener.enterDropConstraint(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDropConstraint) {
            listener.exitDropConstraint(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDropConstraint) {
            return visitor.visitDropConstraint(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class AddUniqueContext extends ParserRuleContext {
    KW_ADD() { return this.getToken(FlinkSqlParser.KW_ADD, 0); }
    KW_UNIQUE() { return this.getToken(FlinkSqlParser.KW_UNIQUE, 0); }
    columnNameList() {
        return this.getRuleContext(0, ColumnNameListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_addUnique; }
    // @Override
    enterRule(listener) {
        if (listener.enterAddUnique) {
            listener.enterAddUnique(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitAddUnique) {
            listener.exitAddUnique(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitAddUnique) {
            return visitor.visitAddUnique(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class NotForcedContext extends ParserRuleContext {
    KW_NOT() { return this.getToken(FlinkSqlParser.KW_NOT, 0); }
    KW_ENFORCED() { return this.getToken(FlinkSqlParser.KW_ENFORCED, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_notForced; }
    // @Override
    enterRule(listener) {
        if (listener.enterNotForced) {
            listener.enterNotForced(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitNotForced) {
            listener.exitNotForced(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitNotForced) {
            return visitor.visitNotForced(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class AlertViewContext extends ParserRuleContext {
    KW_ALTER() { return this.getToken(FlinkSqlParser.KW_ALTER, 0); }
    KW_VIEW() { return this.getToken(FlinkSqlParser.KW_VIEW, 0); }
    viewPath() {
        return this.getRuleContext(0, ViewPathContext);
    }
    renameDefinition() {
        return this.tryGetRuleContext(0, RenameDefinitionContext);
    }
    KW_AS() { return this.tryGetToken(FlinkSqlParser.KW_AS, 0); }
    queryStatement() {
        return this.tryGetRuleContext(0, QueryStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_alertView; }
    // @Override
    enterRule(listener) {
        if (listener.enterAlertView) {
            listener.enterAlertView(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitAlertView) {
            listener.exitAlertView(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitAlertView) {
            return visitor.visitAlertView(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class AlterDatabaseContext extends ParserRuleContext {
    KW_ALTER() { return this.getToken(FlinkSqlParser.KW_ALTER, 0); }
    KW_DATABASE() { return this.getToken(FlinkSqlParser.KW_DATABASE, 0); }
    databasePath() {
        return this.getRuleContext(0, DatabasePathContext);
    }
    setKeyValueDefinition() {
        return this.getRuleContext(0, SetKeyValueDefinitionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_alterDatabase; }
    // @Override
    enterRule(listener) {
        if (listener.enterAlterDatabase) {
            listener.enterAlterDatabase(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitAlterDatabase) {
            listener.exitAlterDatabase(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitAlterDatabase) {
            return visitor.visitAlterDatabase(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class AlterFunctionContext extends ParserRuleContext {
    KW_ALTER() { return this.getToken(FlinkSqlParser.KW_ALTER, 0); }
    KW_FUNCTION() { return this.getToken(FlinkSqlParser.KW_FUNCTION, 0); }
    functionName() {
        return this.getRuleContext(0, FunctionNameContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    KW_SYSTEM() { return this.tryGetToken(FlinkSqlParser.KW_SYSTEM, 0); }
    ifExists() {
        return this.tryGetRuleContext(0, IfExistsContext);
    }
    KW_LANGUAGE() { return this.tryGetToken(FlinkSqlParser.KW_LANGUAGE, 0); }
    KW_JAVA() { return this.tryGetToken(FlinkSqlParser.KW_JAVA, 0); }
    KW_SCALA() { return this.tryGetToken(FlinkSqlParser.KW_SCALA, 0); }
    KW_PYTHON() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_alterFunction; }
    // @Override
    enterRule(listener) {
        if (listener.enterAlterFunction) {
            listener.enterAlterFunction(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitAlterFunction) {
            listener.exitAlterFunction(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitAlterFunction) {
            return visitor.visitAlterFunction(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DropCatalogContext extends ParserRuleContext {
    KW_DROP() { return this.getToken(FlinkSqlParser.KW_DROP, 0); }
    KW_CATALOG() { return this.getToken(FlinkSqlParser.KW_CATALOG, 0); }
    catalogPath() {
        return this.getRuleContext(0, CatalogPathContext);
    }
    ifExists() {
        return this.tryGetRuleContext(0, IfExistsContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dropCatalog; }
    // @Override
    enterRule(listener) {
        if (listener.enterDropCatalog) {
            listener.enterDropCatalog(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDropCatalog) {
            listener.exitDropCatalog(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDropCatalog) {
            return visitor.visitDropCatalog(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DropTableContext extends ParserRuleContext {
    KW_DROP() { return this.getToken(FlinkSqlParser.KW_DROP, 0); }
    KW_TABLE() { return this.getToken(FlinkSqlParser.KW_TABLE, 0); }
    tablePath() {
        return this.getRuleContext(0, TablePathContext);
    }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    ifExists() {
        return this.tryGetRuleContext(0, IfExistsContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dropTable; }
    // @Override
    enterRule(listener) {
        if (listener.enterDropTable) {
            listener.enterDropTable(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDropTable) {
            listener.exitDropTable(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDropTable) {
            return visitor.visitDropTable(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DropDatabaseContext extends ParserRuleContext {
    KW_DROP() { return this.getToken(FlinkSqlParser.KW_DROP, 0); }
    KW_DATABASE() { return this.getToken(FlinkSqlParser.KW_DATABASE, 0); }
    databasePath() {
        return this.getRuleContext(0, DatabasePathContext);
    }
    ifExists() {
        return this.tryGetRuleContext(0, IfExistsContext);
    }
    KW_RESTRICT() { return this.tryGetToken(FlinkSqlParser.KW_RESTRICT, 0); }
    KW_CASCADE() { return this.tryGetToken(FlinkSqlParser.KW_CASCADE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dropDatabase; }
    // @Override
    enterRule(listener) {
        if (listener.enterDropDatabase) {
            listener.enterDropDatabase(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDropDatabase) {
            listener.exitDropDatabase(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDropDatabase) {
            return visitor.visitDropDatabase(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DropViewContext extends ParserRuleContext {
    KW_DROP() { return this.getToken(FlinkSqlParser.KW_DROP, 0); }
    KW_VIEW() { return this.getToken(FlinkSqlParser.KW_VIEW, 0); }
    viewPath() {
        return this.getRuleContext(0, ViewPathContext);
    }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    ifExists() {
        return this.tryGetRuleContext(0, IfExistsContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dropView; }
    // @Override
    enterRule(listener) {
        if (listener.enterDropView) {
            listener.enterDropView(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDropView) {
            listener.exitDropView(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDropView) {
            return visitor.visitDropView(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DropFunctionContext extends ParserRuleContext {
    KW_DROP() { return this.getToken(FlinkSqlParser.KW_DROP, 0); }
    KW_FUNCTION() { return this.getToken(FlinkSqlParser.KW_FUNCTION, 0); }
    functionName() {
        return this.getRuleContext(0, FunctionNameContext);
    }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    KW_SYSTEM() { return this.tryGetToken(FlinkSqlParser.KW_SYSTEM, 0); }
    ifExists() {
        return this.tryGetRuleContext(0, IfExistsContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dropFunction; }
    // @Override
    enterRule(listener) {
        if (listener.enterDropFunction) {
            listener.enterDropFunction(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDropFunction) {
            listener.exitDropFunction(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDropFunction) {
            return visitor.visitDropFunction(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class InsertStatementContext extends ParserRuleContext {
    insertSimpleStatement() {
        return this.tryGetRuleContext(0, InsertSimpleStatementContext);
    }
    KW_EXECUTE() { return this.tryGetToken(FlinkSqlParser.KW_EXECUTE, 0); }
    insertMulStatementCompatibility() {
        return this.tryGetRuleContext(0, InsertMulStatementCompatibilityContext);
    }
    insertMulStatement() {
        return this.tryGetRuleContext(0, InsertMulStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_insertStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterInsertStatement) {
            listener.enterInsertStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitInsertStatement) {
            listener.exitInsertStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitInsertStatement) {
            return visitor.visitInsertStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class InsertSimpleStatementContext extends ParserRuleContext {
    KW_INSERT() { return this.getToken(FlinkSqlParser.KW_INSERT, 0); }
    tablePath() {
        return this.getRuleContext(0, TablePathContext);
    }
    KW_INTO() { return this.tryGetToken(FlinkSqlParser.KW_INTO, 0); }
    KW_OVERWRITE() { return this.tryGetToken(FlinkSqlParser.KW_OVERWRITE, 0); }
    queryStatement() {
        return this.tryGetRuleContext(0, QueryStatementContext);
    }
    valuesDefinition() {
        return this.tryGetRuleContext(0, ValuesDefinitionContext);
    }
    insertPartitionDefinition() {
        return this.tryGetRuleContext(0, InsertPartitionDefinitionContext);
    }
    columnNameList() {
        return this.tryGetRuleContext(0, ColumnNameListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_insertSimpleStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterInsertSimpleStatement) {
            listener.enterInsertSimpleStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitInsertSimpleStatement) {
            listener.exitInsertSimpleStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitInsertSimpleStatement) {
            return visitor.visitInsertSimpleStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class InsertPartitionDefinitionContext extends ParserRuleContext {
    KW_PARTITION() { return this.getToken(FlinkSqlParser.KW_PARTITION, 0); }
    tablePropertyList() {
        return this.getRuleContext(0, TablePropertyListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_insertPartitionDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterInsertPartitionDefinition) {
            listener.enterInsertPartitionDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitInsertPartitionDefinition) {
            listener.exitInsertPartitionDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitInsertPartitionDefinition) {
            return visitor.visitInsertPartitionDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ValuesDefinitionContext extends ParserRuleContext {
    KW_VALUES() { return this.getToken(FlinkSqlParser.KW_VALUES, 0); }
    valuesRowDefinition(i) {
        if (i === undefined) {
            return this.getRuleContexts(ValuesRowDefinitionContext);
        }
        else {
            return this.getRuleContext(i, ValuesRowDefinitionContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_valuesDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterValuesDefinition) {
            listener.enterValuesDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitValuesDefinition) {
            listener.exitValuesDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitValuesDefinition) {
            return visitor.visitValuesDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ValuesRowDefinitionContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    constant(i) {
        if (i === undefined) {
            return this.getRuleContexts(ConstantContext);
        }
        else {
            return this.getRuleContext(i, ConstantContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_valuesRowDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterValuesRowDefinition) {
            listener.enterValuesRowDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitValuesRowDefinition) {
            listener.exitValuesRowDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitValuesRowDefinition) {
            return visitor.visitValuesRowDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class InsertMulStatementCompatibilityContext extends ParserRuleContext {
    KW_BEGIN() { return this.getToken(FlinkSqlParser.KW_BEGIN, 0); }
    KW_STATEMENT() { return this.getToken(FlinkSqlParser.KW_STATEMENT, 0); }
    KW_SET() { return this.getToken(FlinkSqlParser.KW_SET, 0); }
    SEMICOLON(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.SEMICOLON);
        }
        else {
            return this.getToken(FlinkSqlParser.SEMICOLON, i);
        }
    }
    KW_END() { return this.getToken(FlinkSqlParser.KW_END, 0); }
    insertSimpleStatement(i) {
        if (i === undefined) {
            return this.getRuleContexts(InsertSimpleStatementContext);
        }
        else {
            return this.getRuleContext(i, InsertSimpleStatementContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_insertMulStatementCompatibility; }
    // @Override
    enterRule(listener) {
        if (listener.enterInsertMulStatementCompatibility) {
            listener.enterInsertMulStatementCompatibility(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitInsertMulStatementCompatibility) {
            listener.exitInsertMulStatementCompatibility(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitInsertMulStatementCompatibility) {
            return visitor.visitInsertMulStatementCompatibility(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class InsertMulStatementContext extends ParserRuleContext {
    KW_STATEMENT() { return this.getToken(FlinkSqlParser.KW_STATEMENT, 0); }
    KW_SET() { return this.getToken(FlinkSqlParser.KW_SET, 0); }
    KW_BEGIN() { return this.getToken(FlinkSqlParser.KW_BEGIN, 0); }
    KW_END() { return this.getToken(FlinkSqlParser.KW_END, 0); }
    insertSimpleStatement(i) {
        if (i === undefined) {
            return this.getRuleContexts(InsertSimpleStatementContext);
        }
        else {
            return this.getRuleContext(i, InsertSimpleStatementContext);
        }
    }
    SEMICOLON(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.SEMICOLON);
        }
        else {
            return this.getToken(FlinkSqlParser.SEMICOLON, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_insertMulStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterInsertMulStatement) {
            listener.enterInsertMulStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitInsertMulStatement) {
            listener.exitInsertMulStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitInsertMulStatement) {
            return visitor.visitInsertMulStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class QueryStatementContext extends ParserRuleContext {
    valuesCaluse() {
        return this.tryGetRuleContext(0, ValuesCaluseContext);
    }
    withClause() {
        return this.tryGetRuleContext(0, WithClauseContext);
    }
    queryStatement(i) {
        if (i === undefined) {
            return this.getRuleContexts(QueryStatementContext);
        }
        else {
            return this.getRuleContext(i, QueryStatementContext);
        }
    }
    LR_BRACKET() { return this.tryGetToken(FlinkSqlParser.LR_BRACKET, 0); }
    RR_BRACKET() { return this.tryGetToken(FlinkSqlParser.RR_BRACKET, 0); }
    KW_INTERSECT() { return this.tryGetToken(FlinkSqlParser.KW_INTERSECT, 0); }
    KW_UNION() { return this.tryGetToken(FlinkSqlParser.KW_UNION, 0); }
    KW_EXCEPT() { return this.tryGetToken(FlinkSqlParser.KW_EXCEPT, 0); }
    KW_ALL() { return this.tryGetToken(FlinkSqlParser.KW_ALL, 0); }
    orderByCaluse() {
        return this.tryGetRuleContext(0, OrderByCaluseContext);
    }
    limitClause() {
        return this.tryGetRuleContext(0, LimitClauseContext);
    }
    selectClause() {
        return this.tryGetRuleContext(0, SelectClauseContext);
    }
    selectStatement() {
        return this.tryGetRuleContext(0, SelectStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_queryStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterQueryStatement) {
            listener.enterQueryStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitQueryStatement) {
            listener.exitQueryStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitQueryStatement) {
            return visitor.visitQueryStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ValuesCaluseContext extends ParserRuleContext {
    KW_VALUES() { return this.getToken(FlinkSqlParser.KW_VALUES, 0); }
    expression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ExpressionContext);
        }
        else {
            return this.getRuleContext(i, ExpressionContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_valuesCaluse; }
    // @Override
    enterRule(listener) {
        if (listener.enterValuesCaluse) {
            listener.enterValuesCaluse(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitValuesCaluse) {
            listener.exitValuesCaluse(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitValuesCaluse) {
            return visitor.visitValuesCaluse(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WithClauseContext extends ParserRuleContext {
    KW_WITH() { return this.getToken(FlinkSqlParser.KW_WITH, 0); }
    withItem(i) {
        if (i === undefined) {
            return this.getRuleContexts(WithItemContext);
        }
        else {
            return this.getRuleContext(i, WithItemContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_withClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterWithClause) {
            listener.enterWithClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWithClause) {
            listener.exitWithClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWithClause) {
            return visitor.visitWithClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WithItemContext extends ParserRuleContext {
    withItemName() {
        return this.getRuleContext(0, WithItemNameContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    LR_BRACKET(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.LR_BRACKET);
        }
        else {
            return this.getToken(FlinkSqlParser.LR_BRACKET, i);
        }
    }
    queryStatement() {
        return this.getRuleContext(0, QueryStatementContext);
    }
    RR_BRACKET(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.RR_BRACKET);
        }
        else {
            return this.getToken(FlinkSqlParser.RR_BRACKET, i);
        }
    }
    columnName(i) {
        if (i === undefined) {
            return this.getRuleContexts(ColumnNameContext);
        }
        else {
            return this.getRuleContext(i, ColumnNameContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_withItem; }
    // @Override
    enterRule(listener) {
        if (listener.enterWithItem) {
            listener.enterWithItem(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWithItem) {
            listener.exitWithItem(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWithItem) {
            return visitor.visitWithItem(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WithItemNameContext extends ParserRuleContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_withItemName; }
    // @Override
    enterRule(listener) {
        if (listener.enterWithItemName) {
            listener.enterWithItemName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWithItemName) {
            listener.exitWithItemName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWithItemName) {
            return visitor.visitWithItemName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SelectStatementContext extends ParserRuleContext {
    selectClause() {
        return this.getRuleContext(0, SelectClauseContext);
    }
    fromClause() {
        return this.tryGetRuleContext(0, FromClauseContext);
    }
    whereClause() {
        return this.tryGetRuleContext(0, WhereClauseContext);
    }
    groupByClause() {
        return this.tryGetRuleContext(0, GroupByClauseContext);
    }
    havingClause() {
        return this.tryGetRuleContext(0, HavingClauseContext);
    }
    windowClause() {
        return this.tryGetRuleContext(0, WindowClauseContext);
    }
    matchRecognizeClause() {
        return this.tryGetRuleContext(0, MatchRecognizeClauseContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_selectStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterSelectStatement) {
            listener.enterSelectStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSelectStatement) {
            listener.exitSelectStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSelectStatement) {
            return visitor.visitSelectStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SelectClauseContext extends ParserRuleContext {
    KW_SELECT() { return this.getToken(FlinkSqlParser.KW_SELECT, 0); }
    ASTERISK_SIGN() { return this.tryGetToken(FlinkSqlParser.ASTERISK_SIGN, 0); }
    projectItemDefinition(i) {
        if (i === undefined) {
            return this.getRuleContexts(ProjectItemDefinitionContext);
        }
        else {
            return this.getRuleContext(i, ProjectItemDefinitionContext);
        }
    }
    setQuantifier() {
        return this.tryGetRuleContext(0, SetQuantifierContext);
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_selectClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterSelectClause) {
            listener.enterSelectClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSelectClause) {
            listener.exitSelectClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSelectClause) {
            return visitor.visitSelectClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ProjectItemDefinitionContext extends ParserRuleContext {
    overWindowItem() {
        return this.tryGetRuleContext(0, OverWindowItemContext);
    }
    columnName() {
        return this.tryGetRuleContext(0, ColumnNameContext);
    }
    expression() {
        return this.tryGetRuleContext(0, ExpressionContext);
    }
    KW_AS() { return this.tryGetToken(FlinkSqlParser.KW_AS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_projectItemDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterProjectItemDefinition) {
            listener.enterProjectItemDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitProjectItemDefinition) {
            listener.exitProjectItemDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitProjectItemDefinition) {
            return visitor.visitProjectItemDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class OverWindowItemContext extends ParserRuleContext {
    primaryExpression() {
        return this.getRuleContext(0, PrimaryExpressionContext);
    }
    KW_OVER() { return this.getToken(FlinkSqlParser.KW_OVER, 0); }
    windowSpec() {
        return this.tryGetRuleContext(0, WindowSpecContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    errorCapturingIdentifier() {
        return this.tryGetRuleContext(0, ErrorCapturingIdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_overWindowItem; }
    // @Override
    enterRule(listener) {
        if (listener.enterOverWindowItem) {
            listener.enterOverWindowItem(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitOverWindowItem) {
            listener.exitOverWindowItem(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitOverWindowItem) {
            return visitor.visitOverWindowItem(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FromClauseContext extends ParserRuleContext {
    KW_FROM() { return this.getToken(FlinkSqlParser.KW_FROM, 0); }
    tableExpression() {
        return this.getRuleContext(0, TableExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_fromClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterFromClause) {
            listener.enterFromClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFromClause) {
            listener.exitFromClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFromClause) {
            return visitor.visitFromClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TableExpressionContext extends ParserRuleContext {
    tableReference(i) {
        if (i === undefined) {
            return this.getRuleContexts(TableReferenceContext);
        }
        else {
            return this.getRuleContext(i, TableReferenceContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    tableExpression(i) {
        if (i === undefined) {
            return this.getRuleContexts(TableExpressionContext);
        }
        else {
            return this.getRuleContext(i, TableExpressionContext);
        }
    }
    KW_JOIN() { return this.tryGetToken(FlinkSqlParser.KW_JOIN, 0); }
    KW_NATURAL() { return this.tryGetToken(FlinkSqlParser.KW_NATURAL, 0); }
    KW_OUTER() { return this.tryGetToken(FlinkSqlParser.KW_OUTER, 0); }
    joinCondition() {
        return this.tryGetRuleContext(0, JoinConditionContext);
    }
    KW_LEFT() { return this.tryGetToken(FlinkSqlParser.KW_LEFT, 0); }
    KW_RIGHT() { return this.tryGetToken(FlinkSqlParser.KW_RIGHT, 0); }
    KW_FULL() { return this.tryGetToken(FlinkSqlParser.KW_FULL, 0); }
    KW_INNER() { return this.tryGetToken(FlinkSqlParser.KW_INNER, 0); }
    KW_CROSS() { return this.tryGetToken(FlinkSqlParser.KW_CROSS, 0); }
    inlineDataValueClause() {
        return this.tryGetRuleContext(0, InlineDataValueClauseContext);
    }
    windoTVFClause() {
        return this.tryGetRuleContext(0, WindoTVFClauseContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tableExpression; }
    // @Override
    enterRule(listener) {
        if (listener.enterTableExpression) {
            listener.enterTableExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTableExpression) {
            listener.exitTableExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTableExpression) {
            return visitor.visitTableExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TableReferenceContext extends ParserRuleContext {
    tablePrimary() {
        return this.getRuleContext(0, TablePrimaryContext);
    }
    tableAlias() {
        return this.tryGetRuleContext(0, TableAliasContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tableReference; }
    // @Override
    enterRule(listener) {
        if (listener.enterTableReference) {
            listener.enterTableReference(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTableReference) {
            listener.exitTableReference(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTableReference) {
            return visitor.visitTableReference(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TablePrimaryContext extends ParserRuleContext {
    tablePath() {
        return this.tryGetRuleContext(0, TablePathContext);
    }
    KW_TABLE() { return this.tryGetToken(FlinkSqlParser.KW_TABLE, 0); }
    systemTimePeriod() {
        return this.tryGetRuleContext(0, SystemTimePeriodContext);
    }
    correlationName() {
        return this.tryGetRuleContext(0, CorrelationNameContext);
    }
    KW_AS() { return this.tryGetToken(FlinkSqlParser.KW_AS, 0); }
    viewPath() {
        return this.tryGetRuleContext(0, ViewPathContext);
    }
    KW_LATERAL() { return this.tryGetToken(FlinkSqlParser.KW_LATERAL, 0); }
    LR_BRACKET(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.LR_BRACKET);
        }
        else {
            return this.getToken(FlinkSqlParser.LR_BRACKET, i);
        }
    }
    functionName() {
        return this.tryGetRuleContext(0, FunctionNameContext);
    }
    functionParam(i) {
        if (i === undefined) {
            return this.getRuleContexts(FunctionParamContext);
        }
        else {
            return this.getRuleContext(i, FunctionParamContext);
        }
    }
    RR_BRACKET(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.RR_BRACKET);
        }
        else {
            return this.getToken(FlinkSqlParser.RR_BRACKET, i);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    queryStatement() {
        return this.tryGetRuleContext(0, QueryStatementContext);
    }
    KW_UNNEST() { return this.tryGetToken(FlinkSqlParser.KW_UNNEST, 0); }
    expression() {
        return this.tryGetRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tablePrimary; }
    // @Override
    enterRule(listener) {
        if (listener.enterTablePrimary) {
            listener.enterTablePrimary(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTablePrimary) {
            listener.exitTablePrimary(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTablePrimary) {
            return visitor.visitTablePrimary(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SystemTimePeriodContext extends ParserRuleContext {
    KW_FOR() { return this.getToken(FlinkSqlParser.KW_FOR, 0); }
    KW_SYSTEM_TIME() { return this.getToken(FlinkSqlParser.KW_SYSTEM_TIME, 0); }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    KW_OF() { return this.getToken(FlinkSqlParser.KW_OF, 0); }
    dateTimeExpression() {
        return this.getRuleContext(0, DateTimeExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_systemTimePeriod; }
    // @Override
    enterRule(listener) {
        if (listener.enterSystemTimePeriod) {
            listener.enterSystemTimePeriod(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSystemTimePeriod) {
            listener.exitSystemTimePeriod(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSystemTimePeriod) {
            return visitor.visitSystemTimePeriod(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DateTimeExpressionContext extends ParserRuleContext {
    expression() {
        return this.getRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dateTimeExpression; }
    // @Override
    enterRule(listener) {
        if (listener.enterDateTimeExpression) {
            listener.enterDateTimeExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDateTimeExpression) {
            listener.exitDateTimeExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDateTimeExpression) {
            return visitor.visitDateTimeExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class InlineDataValueClauseContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    valuesDefinition() {
        return this.getRuleContext(0, ValuesDefinitionContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    tableAlias() {
        return this.getRuleContext(0, TableAliasContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_inlineDataValueClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterInlineDataValueClause) {
            listener.enterInlineDataValueClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitInlineDataValueClause) {
            listener.exitInlineDataValueClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitInlineDataValueClause) {
            return visitor.visitInlineDataValueClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WindoTVFClauseContext extends ParserRuleContext {
    KW_TABLE() { return this.getToken(FlinkSqlParser.KW_TABLE, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    windowTVFExression() {
        return this.getRuleContext(0, WindowTVFExressionContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_windoTVFClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterWindoTVFClause) {
            listener.enterWindoTVFClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWindoTVFClause) {
            listener.exitWindoTVFClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWindoTVFClause) {
            return visitor.visitWindoTVFClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WindowTVFExressionContext extends ParserRuleContext {
    windoTVFName() {
        return this.getRuleContext(0, WindoTVFNameContext);
    }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    windowTVFParam(i) {
        if (i === undefined) {
            return this.getRuleContexts(WindowTVFParamContext);
        }
        else {
            return this.getRuleContext(i, WindowTVFParamContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_windowTVFExression; }
    // @Override
    enterRule(listener) {
        if (listener.enterWindowTVFExression) {
            listener.enterWindowTVFExression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWindowTVFExression) {
            listener.exitWindowTVFExression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWindowTVFExression) {
            return visitor.visitWindowTVFExression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WindoTVFNameContext extends ParserRuleContext {
    KW_TUMBLE() { return this.tryGetToken(FlinkSqlParser.KW_TUMBLE, 0); }
    KW_HOP() { return this.tryGetToken(FlinkSqlParser.KW_HOP, 0); }
    KW_CUMULATE() { return this.tryGetToken(FlinkSqlParser.KW_CUMULATE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_windoTVFName; }
    // @Override
    enterRule(listener) {
        if (listener.enterWindoTVFName) {
            listener.enterWindoTVFName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWindoTVFName) {
            listener.exitWindoTVFName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWindoTVFName) {
            return visitor.visitWindoTVFName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WindowTVFParamContext extends ParserRuleContext {
    KW_TABLE() { return this.tryGetToken(FlinkSqlParser.KW_TABLE, 0); }
    timeAttrColumn() {
        return this.tryGetRuleContext(0, TimeAttrColumnContext);
    }
    columnDescriptor() {
        return this.tryGetRuleContext(0, ColumnDescriptorContext);
    }
    timeIntervalExpression() {
        return this.tryGetRuleContext(0, TimeIntervalExpressionContext);
    }
    KW_DATA() { return this.tryGetToken(FlinkSqlParser.KW_DATA, 0); }
    DOUBLE_RIGHT_ARROW() { return this.tryGetToken(FlinkSqlParser.DOUBLE_RIGHT_ARROW, 0); }
    KW_TIMECOL() { return this.tryGetToken(FlinkSqlParser.KW_TIMECOL, 0); }
    timeIntervalParamName() {
        return this.tryGetRuleContext(0, TimeIntervalParamNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_windowTVFParam; }
    // @Override
    enterRule(listener) {
        if (listener.enterWindowTVFParam) {
            listener.enterWindowTVFParam(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWindowTVFParam) {
            listener.exitWindowTVFParam(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWindowTVFParam) {
            return visitor.visitWindowTVFParam(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TimeIntervalParamNameContext extends ParserRuleContext {
    KW_DATA() { return this.tryGetToken(FlinkSqlParser.KW_DATA, 0); }
    KW_TIMECOL() { return this.tryGetToken(FlinkSqlParser.KW_TIMECOL, 0); }
    KW_SIZE() { return this.tryGetToken(FlinkSqlParser.KW_SIZE, 0); }
    KW_OFFSET() { return this.tryGetToken(FlinkSqlParser.KW_OFFSET, 0); }
    KW_STEP() { return this.tryGetToken(FlinkSqlParser.KW_STEP, 0); }
    KW_SLIDE() { return this.tryGetToken(FlinkSqlParser.KW_SLIDE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_timeIntervalParamName; }
    // @Override
    enterRule(listener) {
        if (listener.enterTimeIntervalParamName) {
            listener.enterTimeIntervalParamName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTimeIntervalParamName) {
            listener.exitTimeIntervalParamName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTimeIntervalParamName) {
            return visitor.visitTimeIntervalParamName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnDescriptorContext extends ParserRuleContext {
    KW_DESCRIPTOR() { return this.getToken(FlinkSqlParser.KW_DESCRIPTOR, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    columnName() {
        return this.getRuleContext(0, ColumnNameContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_columnDescriptor; }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnDescriptor) {
            listener.enterColumnDescriptor(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnDescriptor) {
            listener.exitColumnDescriptor(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnDescriptor) {
            return visitor.visitColumnDescriptor(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class JoinConditionContext extends ParserRuleContext {
    KW_ON() { return this.tryGetToken(FlinkSqlParser.KW_ON, 0); }
    booleanExpression() {
        return this.tryGetRuleContext(0, BooleanExpressionContext);
    }
    KW_USING() { return this.tryGetToken(FlinkSqlParser.KW_USING, 0); }
    columnNameList() {
        return this.tryGetRuleContext(0, ColumnNameListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_joinCondition; }
    // @Override
    enterRule(listener) {
        if (listener.enterJoinCondition) {
            listener.enterJoinCondition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitJoinCondition) {
            listener.exitJoinCondition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitJoinCondition) {
            return visitor.visitJoinCondition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WhereClauseContext extends ParserRuleContext {
    KW_WHERE() { return this.getToken(FlinkSqlParser.KW_WHERE, 0); }
    booleanExpression() {
        return this.getRuleContext(0, BooleanExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_whereClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterWhereClause) {
            listener.enterWhereClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWhereClause) {
            listener.exitWhereClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWhereClause) {
            return visitor.visitWhereClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class GroupByClauseContext extends ParserRuleContext {
    KW_GROUP() { return this.getToken(FlinkSqlParser.KW_GROUP, 0); }
    KW_BY() { return this.getToken(FlinkSqlParser.KW_BY, 0); }
    groupItemDefinition(i) {
        if (i === undefined) {
            return this.getRuleContexts(GroupItemDefinitionContext);
        }
        else {
            return this.getRuleContext(i, GroupItemDefinitionContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_groupByClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterGroupByClause) {
            listener.enterGroupByClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitGroupByClause) {
            listener.exitGroupByClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitGroupByClause) {
            return visitor.visitGroupByClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class GroupItemDefinitionContext extends ParserRuleContext {
    columnName() {
        return this.tryGetRuleContext(0, ColumnNameContext);
    }
    groupWindowFunction() {
        return this.tryGetRuleContext(0, GroupWindowFunctionContext);
    }
    LR_BRACKET() { return this.tryGetToken(FlinkSqlParser.LR_BRACKET, 0); }
    RR_BRACKET() { return this.tryGetToken(FlinkSqlParser.RR_BRACKET, 0); }
    expression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ExpressionContext);
        }
        else {
            return this.getRuleContext(i, ExpressionContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    groupingSetsNotaionName() {
        return this.tryGetRuleContext(0, GroupingSetsNotaionNameContext);
    }
    groupingSets() {
        return this.tryGetRuleContext(0, GroupingSetsContext);
    }
    groupItemDefinition(i) {
        if (i === undefined) {
            return this.getRuleContexts(GroupItemDefinitionContext);
        }
        else {
            return this.getRuleContext(i, GroupItemDefinitionContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_groupItemDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterGroupItemDefinition) {
            listener.enterGroupItemDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitGroupItemDefinition) {
            listener.exitGroupItemDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitGroupItemDefinition) {
            return visitor.visitGroupItemDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class GroupingSetsContext extends ParserRuleContext {
    KW_GROUPING() { return this.getToken(FlinkSqlParser.KW_GROUPING, 0); }
    KW_SETS() { return this.getToken(FlinkSqlParser.KW_SETS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_groupingSets; }
    // @Override
    enterRule(listener) {
        if (listener.enterGroupingSets) {
            listener.enterGroupingSets(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitGroupingSets) {
            listener.exitGroupingSets(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitGroupingSets) {
            return visitor.visitGroupingSets(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class GroupingSetsNotaionNameContext extends ParserRuleContext {
    KW_CUBE() { return this.tryGetToken(FlinkSqlParser.KW_CUBE, 0); }
    KW_ROLLUP() { return this.tryGetToken(FlinkSqlParser.KW_ROLLUP, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_groupingSetsNotaionName; }
    // @Override
    enterRule(listener) {
        if (listener.enterGroupingSetsNotaionName) {
            listener.enterGroupingSetsNotaionName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitGroupingSetsNotaionName) {
            listener.exitGroupingSetsNotaionName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitGroupingSetsNotaionName) {
            return visitor.visitGroupingSetsNotaionName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class GroupWindowFunctionContext extends ParserRuleContext {
    groupWindowFunctionName() {
        return this.getRuleContext(0, GroupWindowFunctionNameContext);
    }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    timeAttrColumn() {
        return this.getRuleContext(0, TimeAttrColumnContext);
    }
    COMMA() { return this.getToken(FlinkSqlParser.COMMA, 0); }
    timeIntervalExpression() {
        return this.getRuleContext(0, TimeIntervalExpressionContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_groupWindowFunction; }
    // @Override
    enterRule(listener) {
        if (listener.enterGroupWindowFunction) {
            listener.enterGroupWindowFunction(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitGroupWindowFunction) {
            listener.exitGroupWindowFunction(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitGroupWindowFunction) {
            return visitor.visitGroupWindowFunction(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class GroupWindowFunctionNameContext extends ParserRuleContext {
    KW_TUMBLE() { return this.tryGetToken(FlinkSqlParser.KW_TUMBLE, 0); }
    KW_HOP() { return this.tryGetToken(FlinkSqlParser.KW_HOP, 0); }
    KW_SESSION() { return this.tryGetToken(FlinkSqlParser.KW_SESSION, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_groupWindowFunctionName; }
    // @Override
    enterRule(listener) {
        if (listener.enterGroupWindowFunctionName) {
            listener.enterGroupWindowFunctionName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitGroupWindowFunctionName) {
            listener.exitGroupWindowFunctionName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitGroupWindowFunctionName) {
            return visitor.visitGroupWindowFunctionName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TimeAttrColumnContext extends ParserRuleContext {
    uid() {
        return this.getRuleContext(0, UidContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_timeAttrColumn; }
    // @Override
    enterRule(listener) {
        if (listener.enterTimeAttrColumn) {
            listener.enterTimeAttrColumn(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTimeAttrColumn) {
            listener.exitTimeAttrColumn(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTimeAttrColumn) {
            return visitor.visitTimeAttrColumn(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class HavingClauseContext extends ParserRuleContext {
    KW_HAVING() { return this.getToken(FlinkSqlParser.KW_HAVING, 0); }
    booleanExpression() {
        return this.getRuleContext(0, BooleanExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_havingClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterHavingClause) {
            listener.enterHavingClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitHavingClause) {
            listener.exitHavingClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitHavingClause) {
            return visitor.visitHavingClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WindowClauseContext extends ParserRuleContext {
    KW_WINDOW() { return this.getToken(FlinkSqlParser.KW_WINDOW, 0); }
    namedWindow(i) {
        if (i === undefined) {
            return this.getRuleContexts(NamedWindowContext);
        }
        else {
            return this.getRuleContext(i, NamedWindowContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_windowClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterWindowClause) {
            listener.enterWindowClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWindowClause) {
            listener.exitWindowClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWindowClause) {
            return visitor.visitWindowClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class NamedWindowContext extends ParserRuleContext {
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    windowSpec() {
        return this.getRuleContext(0, WindowSpecContext);
    }
    errorCapturingIdentifier() {
        return this.getRuleContext(0, ErrorCapturingIdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_namedWindow; }
    // @Override
    enterRule(listener) {
        if (listener.enterNamedWindow) {
            listener.enterNamedWindow(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitNamedWindow) {
            listener.exitNamedWindow(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitNamedWindow) {
            return visitor.visitNamedWindow(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WindowSpecContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    partitionByClause() {
        return this.tryGetRuleContext(0, PartitionByClauseContext);
    }
    orderByCaluse() {
        return this.tryGetRuleContext(0, OrderByCaluseContext);
    }
    windowFrame() {
        return this.tryGetRuleContext(0, WindowFrameContext);
    }
    errorCapturingIdentifier() {
        return this.tryGetRuleContext(0, ErrorCapturingIdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_windowSpec; }
    // @Override
    enterRule(listener) {
        if (listener.enterWindowSpec) {
            listener.enterWindowSpec(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWindowSpec) {
            listener.exitWindowSpec(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWindowSpec) {
            return visitor.visitWindowSpec(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MatchRecognizeClauseContext extends ParserRuleContext {
    KW_MATCH_RECOGNIZE() { return this.getToken(FlinkSqlParser.KW_MATCH_RECOGNIZE, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    patternVariablesDefination() {
        return this.getRuleContext(0, PatternVariablesDefinationContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    partitionByClause() {
        return this.tryGetRuleContext(0, PartitionByClauseContext);
    }
    orderByCaluse() {
        return this.tryGetRuleContext(0, OrderByCaluseContext);
    }
    measuresClause() {
        return this.tryGetRuleContext(0, MeasuresClauseContext);
    }
    outputMode() {
        return this.tryGetRuleContext(0, OutputModeContext);
    }
    afterMatchStrategy() {
        return this.tryGetRuleContext(0, AfterMatchStrategyContext);
    }
    patternDefination() {
        return this.tryGetRuleContext(0, PatternDefinationContext);
    }
    identifier() {
        return this.tryGetRuleContext(0, IdentifierContext);
    }
    KW_AS() { return this.tryGetToken(FlinkSqlParser.KW_AS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_matchRecognizeClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterMatchRecognizeClause) {
            listener.enterMatchRecognizeClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMatchRecognizeClause) {
            listener.exitMatchRecognizeClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMatchRecognizeClause) {
            return visitor.visitMatchRecognizeClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class OrderByCaluseContext extends ParserRuleContext {
    KW_ORDER() { return this.getToken(FlinkSqlParser.KW_ORDER, 0); }
    KW_BY() { return this.getToken(FlinkSqlParser.KW_BY, 0); }
    orderItemDefition(i) {
        if (i === undefined) {
            return this.getRuleContexts(OrderItemDefitionContext);
        }
        else {
            return this.getRuleContext(i, OrderItemDefitionContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_orderByCaluse; }
    // @Override
    enterRule(listener) {
        if (listener.enterOrderByCaluse) {
            listener.enterOrderByCaluse(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitOrderByCaluse) {
            listener.exitOrderByCaluse(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitOrderByCaluse) {
            return visitor.visitOrderByCaluse(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class OrderItemDefitionContext extends ParserRuleContext {
    columnName() {
        return this.getRuleContext(0, ColumnNameContext);
    }
    KW_NULLS() { return this.tryGetToken(FlinkSqlParser.KW_NULLS, 0); }
    KW_ASC() { return this.tryGetToken(FlinkSqlParser.KW_ASC, 0); }
    KW_DESC() { return this.tryGetToken(FlinkSqlParser.KW_DESC, 0); }
    KW_LAST() { return this.tryGetToken(FlinkSqlParser.KW_LAST, 0); }
    KW_FIRST() { return this.tryGetToken(FlinkSqlParser.KW_FIRST, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_orderItemDefition; }
    // @Override
    enterRule(listener) {
        if (listener.enterOrderItemDefition) {
            listener.enterOrderItemDefition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitOrderItemDefition) {
            listener.exitOrderItemDefition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitOrderItemDefition) {
            return visitor.visitOrderItemDefition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LimitClauseContext extends ParserRuleContext {
    KW_LIMIT() { return this.getToken(FlinkSqlParser.KW_LIMIT, 0); }
    KW_ALL() { return this.tryGetToken(FlinkSqlParser.KW_ALL, 0); }
    expression() {
        return this.tryGetRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_limitClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterLimitClause) {
            listener.enterLimitClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLimitClause) {
            listener.exitLimitClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLimitClause) {
            return visitor.visitLimitClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PartitionByClauseContext extends ParserRuleContext {
    KW_PARTITION() { return this.getToken(FlinkSqlParser.KW_PARTITION, 0); }
    KW_BY() { return this.getToken(FlinkSqlParser.KW_BY, 0); }
    columnName(i) {
        if (i === undefined) {
            return this.getRuleContexts(ColumnNameContext);
        }
        else {
            return this.getRuleContext(i, ColumnNameContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_partitionByClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterPartitionByClause) {
            listener.enterPartitionByClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPartitionByClause) {
            listener.exitPartitionByClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPartitionByClause) {
            return visitor.visitPartitionByClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class QuantifiersContext extends ParserRuleContext {
    ASTERISK_SIGN() { return this.tryGetToken(FlinkSqlParser.ASTERISK_SIGN, 0); }
    ADD_SIGN() { return this.tryGetToken(FlinkSqlParser.ADD_SIGN, 0); }
    QUESTION_MARK_SIGN() { return this.tryGetToken(FlinkSqlParser.QUESTION_MARK_SIGN, 0); }
    LB_BRACKET() { return this.tryGetToken(FlinkSqlParser.LB_BRACKET, 0); }
    DIG_LITERAL(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.DIG_LITERAL);
        }
        else {
            return this.getToken(FlinkSqlParser.DIG_LITERAL, i);
        }
    }
    COMMA() { return this.tryGetToken(FlinkSqlParser.COMMA, 0); }
    RB_BRACKET() { return this.tryGetToken(FlinkSqlParser.RB_BRACKET, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_quantifiers; }
    // @Override
    enterRule(listener) {
        if (listener.enterQuantifiers) {
            listener.enterQuantifiers(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitQuantifiers) {
            listener.exitQuantifiers(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitQuantifiers) {
            return visitor.visitQuantifiers(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MeasuresClauseContext extends ParserRuleContext {
    KW_MEASURES() { return this.getToken(FlinkSqlParser.KW_MEASURES, 0); }
    projectItemDefinition(i) {
        if (i === undefined) {
            return this.getRuleContexts(ProjectItemDefinitionContext);
        }
        else {
            return this.getRuleContext(i, ProjectItemDefinitionContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_measuresClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterMeasuresClause) {
            listener.enterMeasuresClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMeasuresClause) {
            listener.exitMeasuresClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMeasuresClause) {
            return visitor.visitMeasuresClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PatternDefinationContext extends ParserRuleContext {
    KW_PATTERN() { return this.getToken(FlinkSqlParser.KW_PATTERN, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    patternVariable(i) {
        if (i === undefined) {
            return this.getRuleContexts(PatternVariableContext);
        }
        else {
            return this.getRuleContext(i, PatternVariableContext);
        }
    }
    withinClause() {
        return this.tryGetRuleContext(0, WithinClauseContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_patternDefination; }
    // @Override
    enterRule(listener) {
        if (listener.enterPatternDefination) {
            listener.enterPatternDefination(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPatternDefination) {
            listener.exitPatternDefination(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPatternDefination) {
            return visitor.visitPatternDefination(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PatternVariableContext extends ParserRuleContext {
    unquotedIdentifier() {
        return this.getRuleContext(0, UnquotedIdentifierContext);
    }
    quantifiers() {
        return this.tryGetRuleContext(0, QuantifiersContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_patternVariable; }
    // @Override
    enterRule(listener) {
        if (listener.enterPatternVariable) {
            listener.enterPatternVariable(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPatternVariable) {
            listener.exitPatternVariable(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPatternVariable) {
            return visitor.visitPatternVariable(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class OutputModeContext extends ParserRuleContext {
    KW_ALL() { return this.tryGetToken(FlinkSqlParser.KW_ALL, 0); }
    KW_ROWS() { return this.tryGetToken(FlinkSqlParser.KW_ROWS, 0); }
    KW_PER() { return this.getToken(FlinkSqlParser.KW_PER, 0); }
    KW_MATCH() { return this.getToken(FlinkSqlParser.KW_MATCH, 0); }
    KW_ONE() { return this.tryGetToken(FlinkSqlParser.KW_ONE, 0); }
    KW_ROW() { return this.tryGetToken(FlinkSqlParser.KW_ROW, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_outputMode; }
    // @Override
    enterRule(listener) {
        if (listener.enterOutputMode) {
            listener.enterOutputMode(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitOutputMode) {
            listener.exitOutputMode(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitOutputMode) {
            return visitor.visitOutputMode(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class AfterMatchStrategyContext extends ParserRuleContext {
    KW_AFTER() { return this.getToken(FlinkSqlParser.KW_AFTER, 0); }
    KW_MATCH() { return this.getToken(FlinkSqlParser.KW_MATCH, 0); }
    KW_SKIP() { return this.getToken(FlinkSqlParser.KW_SKIP, 0); }
    KW_PAST() { return this.tryGetToken(FlinkSqlParser.KW_PAST, 0); }
    KW_LAST() { return this.tryGetToken(FlinkSqlParser.KW_LAST, 0); }
    KW_ROW() { return this.tryGetToken(FlinkSqlParser.KW_ROW, 0); }
    KW_TO() { return this.tryGetToken(FlinkSqlParser.KW_TO, 0); }
    KW_NEXT() { return this.tryGetToken(FlinkSqlParser.KW_NEXT, 0); }
    unquotedIdentifier() {
        return this.tryGetRuleContext(0, UnquotedIdentifierContext);
    }
    KW_FIRST() { return this.tryGetToken(FlinkSqlParser.KW_FIRST, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_afterMatchStrategy; }
    // @Override
    enterRule(listener) {
        if (listener.enterAfterMatchStrategy) {
            listener.enterAfterMatchStrategy(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitAfterMatchStrategy) {
            listener.exitAfterMatchStrategy(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitAfterMatchStrategy) {
            return visitor.visitAfterMatchStrategy(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PatternVariablesDefinationContext extends ParserRuleContext {
    KW_DEFINE() { return this.getToken(FlinkSqlParser.KW_DEFINE, 0); }
    projectItemDefinition(i) {
        if (i === undefined) {
            return this.getRuleContexts(ProjectItemDefinitionContext);
        }
        else {
            return this.getRuleContext(i, ProjectItemDefinitionContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_patternVariablesDefination; }
    // @Override
    enterRule(listener) {
        if (listener.enterPatternVariablesDefination) {
            listener.enterPatternVariablesDefination(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPatternVariablesDefination) {
            listener.exitPatternVariablesDefination(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPatternVariablesDefination) {
            return visitor.visitPatternVariablesDefination(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WindowFrameContext extends ParserRuleContext {
    KW_RANGE() { return this.tryGetToken(FlinkSqlParser.KW_RANGE, 0); }
    KW_BETWEEN() { return this.getToken(FlinkSqlParser.KW_BETWEEN, 0); }
    timeIntervalExpression() {
        return this.tryGetRuleContext(0, TimeIntervalExpressionContext);
    }
    frameBound() {
        return this.getRuleContext(0, FrameBoundContext);
    }
    KW_ROWS() { return this.tryGetToken(FlinkSqlParser.KW_ROWS, 0); }
    DIG_LITERAL() { return this.tryGetToken(FlinkSqlParser.DIG_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_windowFrame; }
    // @Override
    enterRule(listener) {
        if (listener.enterWindowFrame) {
            listener.enterWindowFrame(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWindowFrame) {
            listener.exitWindowFrame(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWindowFrame) {
            return visitor.visitWindowFrame(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FrameBoundContext extends ParserRuleContext {
    KW_PRECEDING() { return this.getToken(FlinkSqlParser.KW_PRECEDING, 0); }
    KW_AND() { return this.getToken(FlinkSqlParser.KW_AND, 0); }
    KW_CURRENT() { return this.getToken(FlinkSqlParser.KW_CURRENT, 0); }
    KW_ROW() { return this.getToken(FlinkSqlParser.KW_ROW, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_frameBound; }
    // @Override
    enterRule(listener) {
        if (listener.enterFrameBound) {
            listener.enterFrameBound(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFrameBound) {
            listener.exitFrameBound(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFrameBound) {
            return visitor.visitFrameBound(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WithinClauseContext extends ParserRuleContext {
    KW_WITHIN() { return this.getToken(FlinkSqlParser.KW_WITHIN, 0); }
    timeIntervalExpression() {
        return this.getRuleContext(0, TimeIntervalExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_withinClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterWithinClause) {
            listener.enterWithinClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWithinClause) {
            listener.exitWithinClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWithinClause) {
            return visitor.visitWithinClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ExpressionContext extends ParserRuleContext {
    booleanExpression() {
        return this.getRuleContext(0, BooleanExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_expression; }
    // @Override
    enterRule(listener) {
        if (listener.enterExpression) {
            listener.enterExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitExpression) {
            listener.exitExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitExpression) {
            return visitor.visitExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class BooleanExpressionContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_booleanExpression; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class LogicalNotContext extends BooleanExpressionContext {
    KW_NOT() { return this.getToken(FlinkSqlParser.KW_NOT, 0); }
    booleanExpression() {
        return this.getRuleContext(0, BooleanExpressionContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterLogicalNot) {
            listener.enterLogicalNot(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLogicalNot) {
            listener.exitLogicalNot(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLogicalNot) {
            return visitor.visitLogicalNot(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ExistsContext extends BooleanExpressionContext {
    KW_EXISTS() { return this.getToken(FlinkSqlParser.KW_EXISTS, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    queryStatement() {
        return this.getRuleContext(0, QueryStatementContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterExists) {
            listener.enterExists(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitExists) {
            listener.exitExists(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitExists) {
            return visitor.visitExists(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PredicatedContext extends BooleanExpressionContext {
    valueExpression() {
        return this.getRuleContext(0, ValueExpressionContext);
    }
    predicate() {
        return this.tryGetRuleContext(0, PredicateContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterPredicated) {
            listener.enterPredicated(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPredicated) {
            listener.exitPredicated(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPredicated) {
            return visitor.visitPredicated(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LogicalBinaryContext extends BooleanExpressionContext {
    booleanExpression(i) {
        if (i === undefined) {
            return this.getRuleContexts(BooleanExpressionContext);
        }
        else {
            return this.getRuleContext(i, BooleanExpressionContext);
        }
    }
    KW_AND() { return this.tryGetToken(FlinkSqlParser.KW_AND, 0); }
    KW_OR() { return this.tryGetToken(FlinkSqlParser.KW_OR, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterLogicalBinary) {
            listener.enterLogicalBinary(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLogicalBinary) {
            listener.exitLogicalBinary(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLogicalBinary) {
            return visitor.visitLogicalBinary(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LogicalNestedContext extends BooleanExpressionContext {
    booleanExpression() {
        return this.getRuleContext(0, BooleanExpressionContext);
    }
    KW_IS() { return this.getToken(FlinkSqlParser.KW_IS, 0); }
    KW_TRUE() { return this.tryGetToken(FlinkSqlParser.KW_TRUE, 0); }
    KW_FALSE() { return this.tryGetToken(FlinkSqlParser.KW_FALSE, 0); }
    KW_UNKNOWN() { return this.tryGetToken(FlinkSqlParser.KW_UNKNOWN, 0); }
    KW_NULL() { return this.tryGetToken(FlinkSqlParser.KW_NULL, 0); }
    KW_NOT() { return this.tryGetToken(FlinkSqlParser.KW_NOT, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterLogicalNested) {
            listener.enterLogicalNested(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLogicalNested) {
            listener.exitLogicalNested(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLogicalNested) {
            return visitor.visitLogicalNested(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PredicateContext extends ParserRuleContext {
    KW_AND() { return this.tryGetToken(FlinkSqlParser.KW_AND, 0); }
    KW_BETWEEN() { return this.tryGetToken(FlinkSqlParser.KW_BETWEEN, 0); }
    valueExpression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ValueExpressionContext);
        }
        else {
            return this.getRuleContext(i, ValueExpressionContext);
        }
    }
    KW_NOT() { return this.tryGetToken(FlinkSqlParser.KW_NOT, 0); }
    KW_ASYMMETRIC() { return this.tryGetToken(FlinkSqlParser.KW_ASYMMETRIC, 0); }
    KW_SYMMETRIC() { return this.tryGetToken(FlinkSqlParser.KW_SYMMETRIC, 0); }
    LR_BRACKET() { return this.tryGetToken(FlinkSqlParser.LR_BRACKET, 0); }
    expression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ExpressionContext);
        }
        else {
            return this.getRuleContext(i, ExpressionContext);
        }
    }
    RR_BRACKET() { return this.tryGetToken(FlinkSqlParser.RR_BRACKET, 0); }
    KW_IN() { return this.tryGetToken(FlinkSqlParser.KW_IN, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    queryStatement() {
        return this.tryGetRuleContext(0, QueryStatementContext);
    }
    KW_EXISTS() { return this.tryGetToken(FlinkSqlParser.KW_EXISTS, 0); }
    KW_RLIKE() { return this.tryGetToken(FlinkSqlParser.KW_RLIKE, 0); }
    likePredicate() {
        return this.tryGetRuleContext(0, LikePredicateContext);
    }
    KW_IS() { return this.tryGetToken(FlinkSqlParser.KW_IS, 0); }
    KW_TRUE() { return this.tryGetToken(FlinkSqlParser.KW_TRUE, 0); }
    KW_FALSE() { return this.tryGetToken(FlinkSqlParser.KW_FALSE, 0); }
    KW_UNKNOWN() { return this.tryGetToken(FlinkSqlParser.KW_UNKNOWN, 0); }
    KW_NULL() { return this.tryGetToken(FlinkSqlParser.KW_NULL, 0); }
    KW_FROM() { return this.tryGetToken(FlinkSqlParser.KW_FROM, 0); }
    KW_DISTINCT() { return this.tryGetToken(FlinkSqlParser.KW_DISTINCT, 0); }
    KW_TO() { return this.tryGetToken(FlinkSqlParser.KW_TO, 0); }
    KW_SIMILAR() { return this.tryGetToken(FlinkSqlParser.KW_SIMILAR, 0); }
    KW_ESCAPE() { return this.tryGetToken(FlinkSqlParser.KW_ESCAPE, 0); }
    stringLiteral() {
        return this.tryGetRuleContext(0, StringLiteralContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_predicate; }
    // @Override
    enterRule(listener) {
        if (listener.enterPredicate) {
            listener.enterPredicate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPredicate) {
            listener.exitPredicate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPredicate) {
            return visitor.visitPredicate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LikePredicateContext extends ParserRuleContext {
    KW_LIKE() { return this.getToken(FlinkSqlParser.KW_LIKE, 0); }
    KW_ANY() { return this.tryGetToken(FlinkSqlParser.KW_ANY, 0); }
    KW_ALL() { return this.tryGetToken(FlinkSqlParser.KW_ALL, 0); }
    LR_BRACKET() { return this.tryGetToken(FlinkSqlParser.LR_BRACKET, 0); }
    RR_BRACKET() { return this.tryGetToken(FlinkSqlParser.RR_BRACKET, 0); }
    expression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ExpressionContext);
        }
        else {
            return this.getRuleContext(i, ExpressionContext);
        }
    }
    KW_NOT() { return this.tryGetToken(FlinkSqlParser.KW_NOT, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    valueExpression() {
        return this.tryGetRuleContext(0, ValueExpressionContext);
    }
    KW_ESCAPE() { return this.tryGetToken(FlinkSqlParser.KW_ESCAPE, 0); }
    stringLiteral() {
        return this.tryGetRuleContext(0, StringLiteralContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_likePredicate; }
    // @Override
    enterRule(listener) {
        if (listener.enterLikePredicate) {
            listener.enterLikePredicate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLikePredicate) {
            listener.exitLikePredicate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLikePredicate) {
            return visitor.visitLikePredicate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ValueExpressionContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_valueExpression; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class ValueExpressionDefaultContext extends ValueExpressionContext {
    primaryExpression() {
        return this.getRuleContext(0, PrimaryExpressionContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterValueExpressionDefault) {
            listener.enterValueExpressionDefault(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitValueExpressionDefault) {
            listener.exitValueExpressionDefault(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitValueExpressionDefault) {
            return visitor.visitValueExpressionDefault(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ArithmeticUnaryContext extends ValueExpressionContext {
    valueExpression() {
        return this.getRuleContext(0, ValueExpressionContext);
    }
    HYPNEN_SIGN() { return this.tryGetToken(FlinkSqlParser.HYPNEN_SIGN, 0); }
    ADD_SIGN() { return this.tryGetToken(FlinkSqlParser.ADD_SIGN, 0); }
    BIT_NOT_OP() { return this.tryGetToken(FlinkSqlParser.BIT_NOT_OP, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterArithmeticUnary) {
            listener.enterArithmeticUnary(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitArithmeticUnary) {
            listener.exitArithmeticUnary(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitArithmeticUnary) {
            return visitor.visitArithmeticUnary(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ArithmeticBinaryContext extends ValueExpressionContext {
    valueExpression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ValueExpressionContext);
        }
        else {
            return this.getRuleContext(i, ValueExpressionContext);
        }
    }
    ASTERISK_SIGN() { return this.tryGetToken(FlinkSqlParser.ASTERISK_SIGN, 0); }
    SLASH_SIGN() { return this.tryGetToken(FlinkSqlParser.SLASH_SIGN, 0); }
    PENCENT_SIGN() { return this.tryGetToken(FlinkSqlParser.PENCENT_SIGN, 0); }
    KW_DIV() { return this.tryGetToken(FlinkSqlParser.KW_DIV, 0); }
    ADD_SIGN() { return this.tryGetToken(FlinkSqlParser.ADD_SIGN, 0); }
    HYPNEN_SIGN() { return this.tryGetToken(FlinkSqlParser.HYPNEN_SIGN, 0); }
    DOUBLE_VERTICAL_SIGN() { return this.tryGetToken(FlinkSqlParser.DOUBLE_VERTICAL_SIGN, 0); }
    BIT_AND_OP() { return this.tryGetToken(FlinkSqlParser.BIT_AND_OP, 0); }
    BIT_XOR_OP() { return this.tryGetToken(FlinkSqlParser.BIT_XOR_OP, 0); }
    BIT_OR_OP() { return this.tryGetToken(FlinkSqlParser.BIT_OR_OP, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterArithmeticBinary) {
            listener.enterArithmeticBinary(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitArithmeticBinary) {
            listener.exitArithmeticBinary(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitArithmeticBinary) {
            return visitor.visitArithmeticBinary(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ComparisonContext extends ValueExpressionContext {
    comparisonOperator() {
        return this.getRuleContext(0, ComparisonOperatorContext);
    }
    valueExpression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ValueExpressionContext);
        }
        else {
            return this.getRuleContext(i, ValueExpressionContext);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterComparison) {
            listener.enterComparison(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitComparison) {
            listener.exitComparison(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitComparison) {
            return visitor.visitComparison(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PrimaryExpressionContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_primaryExpression; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class SearchedCaseContext extends PrimaryExpressionContext {
    KW_CASE() { return this.getToken(FlinkSqlParser.KW_CASE, 0); }
    KW_END() { return this.getToken(FlinkSqlParser.KW_END, 0); }
    whenClause(i) {
        if (i === undefined) {
            return this.getRuleContexts(WhenClauseContext);
        }
        else {
            return this.getRuleContext(i, WhenClauseContext);
        }
    }
    KW_ELSE() { return this.tryGetToken(FlinkSqlParser.KW_ELSE, 0); }
    expression() {
        return this.tryGetRuleContext(0, ExpressionContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterSearchedCase) {
            listener.enterSearchedCase(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSearchedCase) {
            listener.exitSearchedCase(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSearchedCase) {
            return visitor.visitSearchedCase(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SimpleCaseContext extends PrimaryExpressionContext {
    KW_CASE() { return this.getToken(FlinkSqlParser.KW_CASE, 0); }
    KW_END() { return this.getToken(FlinkSqlParser.KW_END, 0); }
    expression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ExpressionContext);
        }
        else {
            return this.getRuleContext(i, ExpressionContext);
        }
    }
    whenClause(i) {
        if (i === undefined) {
            return this.getRuleContexts(WhenClauseContext);
        }
        else {
            return this.getRuleContext(i, WhenClauseContext);
        }
    }
    KW_ELSE() { return this.tryGetToken(FlinkSqlParser.KW_ELSE, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterSimpleCase) {
            listener.enterSimpleCase(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSimpleCase) {
            listener.exitSimpleCase(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSimpleCase) {
            return visitor.visitSimpleCase(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CastContext extends PrimaryExpressionContext {
    KW_CAST() { return this.getToken(FlinkSqlParser.KW_CAST, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    expression() {
        return this.getRuleContext(0, ExpressionContext);
    }
    KW_AS() { return this.getToken(FlinkSqlParser.KW_AS, 0); }
    columnType() {
        return this.getRuleContext(0, ColumnTypeContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCast) {
            listener.enterCast(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCast) {
            listener.exitCast(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCast) {
            return visitor.visitCast(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FirstContext extends PrimaryExpressionContext {
    KW_FIRST() { return this.getToken(FlinkSqlParser.KW_FIRST, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    expression() {
        return this.getRuleContext(0, ExpressionContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    KW_IGNORE() { return this.tryGetToken(FlinkSqlParser.KW_IGNORE, 0); }
    KW_NULLS() { return this.tryGetToken(FlinkSqlParser.KW_NULLS, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterFirst) {
            listener.enterFirst(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFirst) {
            listener.exitFirst(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFirst) {
            return visitor.visitFirst(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LastContext extends PrimaryExpressionContext {
    KW_LAST() { return this.getToken(FlinkSqlParser.KW_LAST, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    expression() {
        return this.getRuleContext(0, ExpressionContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    KW_IGNORE() { return this.tryGetToken(FlinkSqlParser.KW_IGNORE, 0); }
    KW_NULLS() { return this.tryGetToken(FlinkSqlParser.KW_NULLS, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterLast) {
            listener.enterLast(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLast) {
            listener.exitLast(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLast) {
            return visitor.visitLast(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PositionContext extends PrimaryExpressionContext {
    KW_POSITION() { return this.getToken(FlinkSqlParser.KW_POSITION, 0); }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    KW_IN() { return this.getToken(FlinkSqlParser.KW_IN, 0); }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    valueExpression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ValueExpressionContext);
        }
        else {
            return this.getRuleContext(i, ValueExpressionContext);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterPosition) {
            listener.enterPosition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPosition) {
            listener.exitPosition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPosition) {
            return visitor.visitPosition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ConstantDefaultContext extends PrimaryExpressionContext {
    constant() {
        return this.getRuleContext(0, ConstantContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterConstantDefault) {
            listener.enterConstantDefault(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitConstantDefault) {
            listener.exitConstantDefault(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitConstantDefault) {
            return visitor.visitConstantDefault(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class StarContext extends PrimaryExpressionContext {
    ASTERISK_SIGN() { return this.getToken(FlinkSqlParser.ASTERISK_SIGN, 0); }
    uid() {
        return this.tryGetRuleContext(0, UidContext);
    }
    DOT() { return this.tryGetToken(FlinkSqlParser.DOT, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterStar) {
            listener.enterStar(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitStar) {
            listener.exitStar(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitStar) {
            return visitor.visitStar(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SubqueryExpressionContext extends PrimaryExpressionContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    queryStatement() {
        return this.getRuleContext(0, QueryStatementContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterSubqueryExpression) {
            listener.enterSubqueryExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSubqueryExpression) {
            listener.exitSubqueryExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSubqueryExpression) {
            return visitor.visitSubqueryExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FunctionCallContext extends PrimaryExpressionContext {
    functionName() {
        return this.getRuleContext(0, FunctionNameContext);
    }
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    functionParam(i) {
        if (i === undefined) {
            return this.getRuleContexts(FunctionParamContext);
        }
        else {
            return this.getRuleContext(i, FunctionParamContext);
        }
    }
    setQuantifier() {
        return this.tryGetRuleContext(0, SetQuantifierContext);
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterFunctionCall) {
            listener.enterFunctionCall(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFunctionCall) {
            listener.exitFunctionCall(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFunctionCall) {
            return visitor.visitFunctionCall(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SubscriptContext extends PrimaryExpressionContext {
    LS_BRACKET() { return this.getToken(FlinkSqlParser.LS_BRACKET, 0); }
    RS_BRACKET() { return this.getToken(FlinkSqlParser.RS_BRACKET, 0); }
    primaryExpression() {
        return this.getRuleContext(0, PrimaryExpressionContext);
    }
    valueExpression() {
        return this.getRuleContext(0, ValueExpressionContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterSubscript) {
            listener.enterSubscript(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSubscript) {
            listener.exitSubscript(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSubscript) {
            return visitor.visitSubscript(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ColumnReferenceContext extends PrimaryExpressionContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterColumnReference) {
            listener.enterColumnReference(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitColumnReference) {
            listener.exitColumnReference(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitColumnReference) {
            return visitor.visitColumnReference(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DereferenceContext extends PrimaryExpressionContext {
    dereferenceDefinition() {
        return this.getRuleContext(0, DereferenceDefinitionContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterDereference) {
            listener.enterDereference(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDereference) {
            listener.exitDereference(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDereference) {
            return visitor.visitDereference(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ParenthesizedExpressionContext extends PrimaryExpressionContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    expression() {
        return this.getRuleContext(0, ExpressionContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterParenthesizedExpression) {
            listener.enterParenthesizedExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitParenthesizedExpression) {
            listener.exitParenthesizedExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitParenthesizedExpression) {
            return visitor.visitParenthesizedExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DateFunctionExpressionContext extends PrimaryExpressionContext {
    KW_CURRENT_TIMESTAMP() { return this.getToken(FlinkSqlParser.KW_CURRENT_TIMESTAMP, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterDateFunctionExpression) {
            listener.enterDateFunctionExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDateFunctionExpression) {
            listener.exitDateFunctionExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDateFunctionExpression) {
            return visitor.visitDateFunctionExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FunctionNameCreateContext extends ParserRuleContext {
    uid() {
        return this.getRuleContext(0, UidContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_functionNameCreate; }
    // @Override
    enterRule(listener) {
        if (listener.enterFunctionNameCreate) {
            listener.enterFunctionNameCreate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFunctionNameCreate) {
            listener.exitFunctionNameCreate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFunctionNameCreate) {
            return visitor.visitFunctionNameCreate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FunctionNameContext extends ParserRuleContext {
    reservedKeywordsUsedAsFuncName() {
        return this.tryGetRuleContext(0, ReservedKeywordsUsedAsFuncNameContext);
    }
    uid() {
        return this.tryGetRuleContext(0, UidContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_functionName; }
    // @Override
    enterRule(listener) {
        if (listener.enterFunctionName) {
            listener.enterFunctionName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFunctionName) {
            listener.exitFunctionName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFunctionName) {
            return visitor.visitFunctionName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FunctionParamContext extends ParserRuleContext {
    reservedKeywordsUsedAsFuncParam() {
        return this.tryGetRuleContext(0, ReservedKeywordsUsedAsFuncParamContext);
    }
    timeIntervalUnit() {
        return this.tryGetRuleContext(0, TimeIntervalUnitContext);
    }
    timePointUnit() {
        return this.tryGetRuleContext(0, TimePointUnitContext);
    }
    expression() {
        return this.tryGetRuleContext(0, ExpressionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_functionParam; }
    // @Override
    enterRule(listener) {
        if (listener.enterFunctionParam) {
            listener.enterFunctionParam(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFunctionParam) {
            listener.exitFunctionParam(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFunctionParam) {
            return visitor.visitFunctionParam(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DereferenceDefinitionContext extends ParserRuleContext {
    uid() {
        return this.getRuleContext(0, UidContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_dereferenceDefinition; }
    // @Override
    enterRule(listener) {
        if (listener.enterDereferenceDefinition) {
            listener.enterDereferenceDefinition(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDereferenceDefinition) {
            listener.exitDereferenceDefinition(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDereferenceDefinition) {
            return visitor.visitDereferenceDefinition(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CorrelationNameContext extends ParserRuleContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_correlationName; }
    // @Override
    enterRule(listener) {
        if (listener.enterCorrelationName) {
            listener.enterCorrelationName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCorrelationName) {
            listener.exitCorrelationName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCorrelationName) {
            return visitor.visitCorrelationName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class QualifiedNameContext extends ParserRuleContext {
    identifier() {
        return this.tryGetRuleContext(0, IdentifierContext);
    }
    dereferenceDefinition() {
        return this.tryGetRuleContext(0, DereferenceDefinitionContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_qualifiedName; }
    // @Override
    enterRule(listener) {
        if (listener.enterQualifiedName) {
            listener.enterQualifiedName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitQualifiedName) {
            listener.exitQualifiedName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitQualifiedName) {
            return visitor.visitQualifiedName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TimeIntervalExpressionContext extends ParserRuleContext {
    KW_INTERVAL() { return this.getToken(FlinkSqlParser.KW_INTERVAL, 0); }
    errorCapturingMultiUnitsInterval() {
        return this.tryGetRuleContext(0, ErrorCapturingMultiUnitsIntervalContext);
    }
    errorCapturingUnitToUnitInterval() {
        return this.tryGetRuleContext(0, ErrorCapturingUnitToUnitIntervalContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_timeIntervalExpression; }
    // @Override
    enterRule(listener) {
        if (listener.enterTimeIntervalExpression) {
            listener.enterTimeIntervalExpression(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTimeIntervalExpression) {
            listener.exitTimeIntervalExpression(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTimeIntervalExpression) {
            return visitor.visitTimeIntervalExpression(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ErrorCapturingMultiUnitsIntervalContext extends ParserRuleContext {
    multiUnitsInterval() {
        return this.getRuleContext(0, MultiUnitsIntervalContext);
    }
    unitToUnitInterval() {
        return this.tryGetRuleContext(0, UnitToUnitIntervalContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_errorCapturingMultiUnitsInterval; }
    // @Override
    enterRule(listener) {
        if (listener.enterErrorCapturingMultiUnitsInterval) {
            listener.enterErrorCapturingMultiUnitsInterval(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitErrorCapturingMultiUnitsInterval) {
            listener.exitErrorCapturingMultiUnitsInterval(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitErrorCapturingMultiUnitsInterval) {
            return visitor.visitErrorCapturingMultiUnitsInterval(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MultiUnitsIntervalContext extends ParserRuleContext {
    intervalValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntervalValueContext);
        }
        else {
            return this.getRuleContext(i, IntervalValueContext);
        }
    }
    timeIntervalUnit(i) {
        if (i === undefined) {
            return this.getRuleContexts(TimeIntervalUnitContext);
        }
        else {
            return this.getRuleContext(i, TimeIntervalUnitContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_multiUnitsInterval; }
    // @Override
    enterRule(listener) {
        if (listener.enterMultiUnitsInterval) {
            listener.enterMultiUnitsInterval(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMultiUnitsInterval) {
            listener.exitMultiUnitsInterval(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMultiUnitsInterval) {
            return visitor.visitMultiUnitsInterval(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ErrorCapturingUnitToUnitIntervalContext extends ParserRuleContext {
    unitToUnitInterval(i) {
        if (i === undefined) {
            return this.getRuleContexts(UnitToUnitIntervalContext);
        }
        else {
            return this.getRuleContext(i, UnitToUnitIntervalContext);
        }
    }
    multiUnitsInterval() {
        return this.tryGetRuleContext(0, MultiUnitsIntervalContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_errorCapturingUnitToUnitInterval; }
    // @Override
    enterRule(listener) {
        if (listener.enterErrorCapturingUnitToUnitInterval) {
            listener.enterErrorCapturingUnitToUnitInterval(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitErrorCapturingUnitToUnitInterval) {
            listener.exitErrorCapturingUnitToUnitInterval(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitErrorCapturingUnitToUnitInterval) {
            return visitor.visitErrorCapturingUnitToUnitInterval(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UnitToUnitIntervalContext extends ParserRuleContext {
    KW_TO() { return this.getToken(FlinkSqlParser.KW_TO, 0); }
    intervalValue() {
        return this.getRuleContext(0, IntervalValueContext);
    }
    timeIntervalUnit(i) {
        if (i === undefined) {
            return this.getRuleContexts(TimeIntervalUnitContext);
        }
        else {
            return this.getRuleContext(i, TimeIntervalUnitContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_unitToUnitInterval; }
    // @Override
    enterRule(listener) {
        if (listener.enterUnitToUnitInterval) {
            listener.enterUnitToUnitInterval(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUnitToUnitInterval) {
            listener.exitUnitToUnitInterval(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUnitToUnitInterval) {
            return visitor.visitUnitToUnitInterval(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IntervalValueContext extends ParserRuleContext {
    DIG_LITERAL() { return this.tryGetToken(FlinkSqlParser.DIG_LITERAL, 0); }
    REAL_LITERAL() { return this.tryGetToken(FlinkSqlParser.REAL_LITERAL, 0); }
    ADD_SIGN() { return this.tryGetToken(FlinkSqlParser.ADD_SIGN, 0); }
    HYPNEN_SIGN() { return this.tryGetToken(FlinkSqlParser.HYPNEN_SIGN, 0); }
    STRING_LITERAL() { return this.tryGetToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_intervalValue; }
    // @Override
    enterRule(listener) {
        if (listener.enterIntervalValue) {
            listener.enterIntervalValue(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIntervalValue) {
            listener.exitIntervalValue(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIntervalValue) {
            return visitor.visitIntervalValue(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TableAliasContext extends ParserRuleContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    KW_AS() { return this.tryGetToken(FlinkSqlParser.KW_AS, 0); }
    identifierList() {
        return this.tryGetRuleContext(0, IdentifierListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tableAlias; }
    // @Override
    enterRule(listener) {
        if (listener.enterTableAlias) {
            listener.enterTableAlias(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTableAlias) {
            listener.exitTableAlias(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTableAlias) {
            return visitor.visitTableAlias(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ErrorCapturingIdentifierContext extends ParserRuleContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    errorCapturingIdentifierExtra() {
        return this.getRuleContext(0, ErrorCapturingIdentifierExtraContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_errorCapturingIdentifier; }
    // @Override
    enterRule(listener) {
        if (listener.enterErrorCapturingIdentifier) {
            listener.enterErrorCapturingIdentifier(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitErrorCapturingIdentifier) {
            listener.exitErrorCapturingIdentifier(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitErrorCapturingIdentifier) {
            return visitor.visitErrorCapturingIdentifier(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ErrorCapturingIdentifierExtraContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_errorCapturingIdentifierExtra; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class ErrorIdentContext extends ErrorCapturingIdentifierExtraContext {
    KW_MINUS(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.KW_MINUS);
        }
        else {
            return this.getToken(FlinkSqlParser.KW_MINUS, i);
        }
    }
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterErrorIdent) {
            listener.enterErrorIdent(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitErrorIdent) {
            listener.exitErrorIdent(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitErrorIdent) {
            return visitor.visitErrorIdent(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class RealIdentContext extends ErrorCapturingIdentifierExtraContext {
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterRealIdent) {
            listener.enterRealIdent(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitRealIdent) {
            listener.exitRealIdent(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitRealIdent) {
            return visitor.visitRealIdent(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IdentifierListContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    identifierSeq() {
        return this.getRuleContext(0, IdentifierSeqContext);
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_identifierList; }
    // @Override
    enterRule(listener) {
        if (listener.enterIdentifierList) {
            listener.enterIdentifierList(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIdentifierList) {
            listener.exitIdentifierList(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIdentifierList) {
            return visitor.visitIdentifierList(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IdentifierSeqContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_identifierSeq; }
    // @Override
    enterRule(listener) {
        if (listener.enterIdentifierSeq) {
            listener.enterIdentifierSeq(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIdentifierSeq) {
            listener.exitIdentifierSeq(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIdentifierSeq) {
            return visitor.visitIdentifierSeq(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IdentifierContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_identifier; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class UnquotedIdentifierAlternativeContext extends IdentifierContext {
    unquotedIdentifier() {
        return this.getRuleContext(0, UnquotedIdentifierContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterUnquotedIdentifierAlternative) {
            listener.enterUnquotedIdentifierAlternative(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUnquotedIdentifierAlternative) {
            listener.exitUnquotedIdentifierAlternative(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUnquotedIdentifierAlternative) {
            return visitor.visitUnquotedIdentifierAlternative(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class QuotedIdentifierAlternativeContext extends IdentifierContext {
    quotedIdentifier() {
        return this.getRuleContext(0, QuotedIdentifierContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterQuotedIdentifierAlternative) {
            listener.enterQuotedIdentifierAlternative(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitQuotedIdentifierAlternative) {
            listener.exitQuotedIdentifierAlternative(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitQuotedIdentifierAlternative) {
            return visitor.visitQuotedIdentifierAlternative(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class NonReservedKeywordsAlternativeContext extends IdentifierContext {
    nonReservedKeywords() {
        return this.getRuleContext(0, NonReservedKeywordsContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterNonReservedKeywordsAlternative) {
            listener.enterNonReservedKeywordsAlternative(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitNonReservedKeywordsAlternative) {
            listener.exitNonReservedKeywordsAlternative(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitNonReservedKeywordsAlternative) {
            return visitor.visitNonReservedKeywordsAlternative(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UnquotedIdentifierContext extends ParserRuleContext {
    DIG_LITERAL() { return this.tryGetToken(FlinkSqlParser.DIG_LITERAL, 0); }
    ID_LITERAL() { return this.tryGetToken(FlinkSqlParser.ID_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_unquotedIdentifier; }
    // @Override
    enterRule(listener) {
        if (listener.enterUnquotedIdentifier) {
            listener.enterUnquotedIdentifier(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUnquotedIdentifier) {
            listener.exitUnquotedIdentifier(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUnquotedIdentifier) {
            return visitor.visitUnquotedIdentifier(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class QuotedIdentifierContext extends ParserRuleContext {
    STRING_LITERAL() { return this.getToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_quotedIdentifier; }
    // @Override
    enterRule(listener) {
        if (listener.enterQuotedIdentifier) {
            listener.enterQuotedIdentifier(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitQuotedIdentifier) {
            listener.exitQuotedIdentifier(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitQuotedIdentifier) {
            return visitor.visitQuotedIdentifier(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WhenClauseContext extends ParserRuleContext {
    KW_WHEN() { return this.getToken(FlinkSqlParser.KW_WHEN, 0); }
    KW_THEN() { return this.getToken(FlinkSqlParser.KW_THEN, 0); }
    expression(i) {
        if (i === undefined) {
            return this.getRuleContexts(ExpressionContext);
        }
        else {
            return this.getRuleContext(i, ExpressionContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_whenClause; }
    // @Override
    enterRule(listener) {
        if (listener.enterWhenClause) {
            listener.enterWhenClause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWhenClause) {
            listener.exitWhenClause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWhenClause) {
            return visitor.visitWhenClause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CatalogPathContext extends ParserRuleContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_catalogPath; }
    // @Override
    enterRule(listener) {
        if (listener.enterCatalogPath) {
            listener.enterCatalogPath(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCatalogPath) {
            listener.exitCatalogPath(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCatalogPath) {
            return visitor.visitCatalogPath(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CatalogPathCreateContext extends ParserRuleContext {
    identifier() {
        return this.getRuleContext(0, IdentifierContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_catalogPathCreate; }
    // @Override
    enterRule(listener) {
        if (listener.enterCatalogPathCreate) {
            listener.enterCatalogPathCreate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCatalogPathCreate) {
            listener.exitCatalogPathCreate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCatalogPathCreate) {
            return visitor.visitCatalogPathCreate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DatabasePathContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    DOT() { return this.tryGetToken(FlinkSqlParser.DOT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_databasePath; }
    // @Override
    enterRule(listener) {
        if (listener.enterDatabasePath) {
            listener.enterDatabasePath(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDatabasePath) {
            listener.exitDatabasePath(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDatabasePath) {
            return visitor.visitDatabasePath(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DatabasePathCreateContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    DOT() { return this.tryGetToken(FlinkSqlParser.DOT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_databasePathCreate; }
    // @Override
    enterRule(listener) {
        if (listener.enterDatabasePathCreate) {
            listener.enterDatabasePathCreate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDatabasePathCreate) {
            listener.exitDatabasePathCreate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDatabasePathCreate) {
            return visitor.visitDatabasePathCreate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TablePathCreateContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    DOT(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.DOT);
        }
        else {
            return this.getToken(FlinkSqlParser.DOT, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tablePathCreate; }
    // @Override
    enterRule(listener) {
        if (listener.enterTablePathCreate) {
            listener.enterTablePathCreate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTablePathCreate) {
            listener.exitTablePathCreate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTablePathCreate) {
            return visitor.visitTablePathCreate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TablePathContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    DOT(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.DOT);
        }
        else {
            return this.getToken(FlinkSqlParser.DOT, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tablePath; }
    // @Override
    enterRule(listener) {
        if (listener.enterTablePath) {
            listener.enterTablePath(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTablePath) {
            listener.exitTablePath(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTablePath) {
            return visitor.visitTablePath(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ViewPathContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    DOT(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.DOT);
        }
        else {
            return this.getToken(FlinkSqlParser.DOT, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_viewPath; }
    // @Override
    enterRule(listener) {
        if (listener.enterViewPath) {
            listener.enterViewPath(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitViewPath) {
            listener.exitViewPath(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitViewPath) {
            return visitor.visitViewPath(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ViewPathCreateContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    DOT(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.DOT);
        }
        else {
            return this.getToken(FlinkSqlParser.DOT, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_viewPathCreate; }
    // @Override
    enterRule(listener) {
        if (listener.enterViewPathCreate) {
            listener.enterViewPathCreate(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitViewPathCreate) {
            listener.exitViewPathCreate(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitViewPathCreate) {
            return visitor.visitViewPathCreate(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UidContext extends ParserRuleContext {
    identifier(i) {
        if (i === undefined) {
            return this.getRuleContexts(IdentifierContext);
        }
        else {
            return this.getRuleContext(i, IdentifierContext);
        }
    }
    DOT(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.DOT);
        }
        else {
            return this.getToken(FlinkSqlParser.DOT, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_uid; }
    // @Override
    enterRule(listener) {
        if (listener.enterUid) {
            listener.enterUid(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUid) {
            listener.exitUid(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUid) {
            return visitor.visitUid(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class WithOptionContext extends ParserRuleContext {
    KW_WITH() { return this.getToken(FlinkSqlParser.KW_WITH, 0); }
    tablePropertyList() {
        return this.getRuleContext(0, TablePropertyListContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_withOption; }
    // @Override
    enterRule(listener) {
        if (listener.enterWithOption) {
            listener.enterWithOption(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitWithOption) {
            listener.exitWithOption(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitWithOption) {
            return visitor.visitWithOption(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IfNotExistsContext extends ParserRuleContext {
    KW_IF() { return this.getToken(FlinkSqlParser.KW_IF, 0); }
    KW_NOT() { return this.getToken(FlinkSqlParser.KW_NOT, 0); }
    KW_EXISTS() { return this.getToken(FlinkSqlParser.KW_EXISTS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_ifNotExists; }
    // @Override
    enterRule(listener) {
        if (listener.enterIfNotExists) {
            listener.enterIfNotExists(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIfNotExists) {
            listener.exitIfNotExists(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIfNotExists) {
            return visitor.visitIfNotExists(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IfExistsContext extends ParserRuleContext {
    KW_IF() { return this.getToken(FlinkSqlParser.KW_IF, 0); }
    KW_EXISTS() { return this.getToken(FlinkSqlParser.KW_EXISTS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_ifExists; }
    // @Override
    enterRule(listener) {
        if (listener.enterIfExists) {
            listener.enterIfExists(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIfExists) {
            listener.exitIfExists(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIfExists) {
            return visitor.visitIfExists(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TablePropertyListContext extends ParserRuleContext {
    LR_BRACKET() { return this.getToken(FlinkSqlParser.LR_BRACKET, 0); }
    tableProperty(i) {
        if (i === undefined) {
            return this.getRuleContexts(TablePropertyContext);
        }
        else {
            return this.getRuleContext(i, TablePropertyContext);
        }
    }
    RR_BRACKET() { return this.getToken(FlinkSqlParser.RR_BRACKET, 0); }
    COMMA(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.COMMA);
        }
        else {
            return this.getToken(FlinkSqlParser.COMMA, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tablePropertyList; }
    // @Override
    enterRule(listener) {
        if (listener.enterTablePropertyList) {
            listener.enterTablePropertyList(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTablePropertyList) {
            listener.exitTablePropertyList(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTablePropertyList) {
            return visitor.visitTablePropertyList(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TablePropertyContext extends ParserRuleContext {
    tablePropertyKey() {
        return this.getRuleContext(0, TablePropertyKeyContext);
    }
    tablePropertyValue() {
        return this.tryGetRuleContext(0, TablePropertyValueContext);
    }
    EQUAL_SYMBOL() { return this.tryGetToken(FlinkSqlParser.EQUAL_SYMBOL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tableProperty; }
    // @Override
    enterRule(listener) {
        if (listener.enterTableProperty) {
            listener.enterTableProperty(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTableProperty) {
            listener.exitTableProperty(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTableProperty) {
            return visitor.visitTableProperty(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TablePropertyKeyContext extends ParserRuleContext {
    identifier() {
        return this.tryGetRuleContext(0, IdentifierContext);
    }
    dereferenceDefinition() {
        return this.tryGetRuleContext(0, DereferenceDefinitionContext);
    }
    STRING_LITERAL() { return this.tryGetToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tablePropertyKey; }
    // @Override
    enterRule(listener) {
        if (listener.enterTablePropertyKey) {
            listener.enterTablePropertyKey(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTablePropertyKey) {
            listener.exitTablePropertyKey(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTablePropertyKey) {
            return visitor.visitTablePropertyKey(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TablePropertyValueContext extends ParserRuleContext {
    DIG_LITERAL() { return this.tryGetToken(FlinkSqlParser.DIG_LITERAL, 0); }
    REAL_LITERAL() { return this.tryGetToken(FlinkSqlParser.REAL_LITERAL, 0); }
    booleanLiteral() {
        return this.tryGetRuleContext(0, BooleanLiteralContext);
    }
    STRING_LITERAL() { return this.tryGetToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_tablePropertyValue; }
    // @Override
    enterRule(listener) {
        if (listener.enterTablePropertyValue) {
            listener.enterTablePropertyValue(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTablePropertyValue) {
            listener.exitTablePropertyValue(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTablePropertyValue) {
            return visitor.visitTablePropertyValue(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LogicalOperatorContext extends ParserRuleContext {
    KW_AND() { return this.tryGetToken(FlinkSqlParser.KW_AND, 0); }
    BIT_AND_OP(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.BIT_AND_OP);
        }
        else {
            return this.getToken(FlinkSqlParser.BIT_AND_OP, i);
        }
    }
    KW_OR() { return this.tryGetToken(FlinkSqlParser.KW_OR, 0); }
    BIT_OR_OP(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.BIT_OR_OP);
        }
        else {
            return this.getToken(FlinkSqlParser.BIT_OR_OP, i);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_logicalOperator; }
    // @Override
    enterRule(listener) {
        if (listener.enterLogicalOperator) {
            listener.enterLogicalOperator(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLogicalOperator) {
            listener.exitLogicalOperator(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLogicalOperator) {
            return visitor.visitLogicalOperator(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ComparisonOperatorContext extends ParserRuleContext {
    EQUAL_SYMBOL() { return this.tryGetToken(FlinkSqlParser.EQUAL_SYMBOL, 0); }
    GREATER_SYMBOL() { return this.tryGetToken(FlinkSqlParser.GREATER_SYMBOL, 0); }
    LESS_SYMBOL() { return this.tryGetToken(FlinkSqlParser.LESS_SYMBOL, 0); }
    EXCLAMATION_SYMBOL() { return this.tryGetToken(FlinkSqlParser.EXCLAMATION_SYMBOL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_comparisonOperator; }
    // @Override
    enterRule(listener) {
        if (listener.enterComparisonOperator) {
            listener.enterComparisonOperator(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitComparisonOperator) {
            listener.exitComparisonOperator(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitComparisonOperator) {
            return visitor.visitComparisonOperator(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class BitOperatorContext extends ParserRuleContext {
    LESS_SYMBOL(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.LESS_SYMBOL);
        }
        else {
            return this.getToken(FlinkSqlParser.LESS_SYMBOL, i);
        }
    }
    GREATER_SYMBOL(i) {
        if (i === undefined) {
            return this.getTokens(FlinkSqlParser.GREATER_SYMBOL);
        }
        else {
            return this.getToken(FlinkSqlParser.GREATER_SYMBOL, i);
        }
    }
    BIT_AND_OP() { return this.tryGetToken(FlinkSqlParser.BIT_AND_OP, 0); }
    BIT_XOR_OP() { return this.tryGetToken(FlinkSqlParser.BIT_XOR_OP, 0); }
    BIT_OR_OP() { return this.tryGetToken(FlinkSqlParser.BIT_OR_OP, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_bitOperator; }
    // @Override
    enterRule(listener) {
        if (listener.enterBitOperator) {
            listener.enterBitOperator(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitBitOperator) {
            listener.exitBitOperator(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitBitOperator) {
            return visitor.visitBitOperator(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MathOperatorContext extends ParserRuleContext {
    ASTERISK_SIGN() { return this.tryGetToken(FlinkSqlParser.ASTERISK_SIGN, 0); }
    SLASH_SIGN() { return this.tryGetToken(FlinkSqlParser.SLASH_SIGN, 0); }
    PENCENT_SIGN() { return this.tryGetToken(FlinkSqlParser.PENCENT_SIGN, 0); }
    KW_DIV() { return this.tryGetToken(FlinkSqlParser.KW_DIV, 0); }
    ADD_SIGN() { return this.tryGetToken(FlinkSqlParser.ADD_SIGN, 0); }
    HYPNEN_SIGN() { return this.tryGetToken(FlinkSqlParser.HYPNEN_SIGN, 0); }
    DOUBLE_HYPNEN_SIGN() { return this.tryGetToken(FlinkSqlParser.DOUBLE_HYPNEN_SIGN, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_mathOperator; }
    // @Override
    enterRule(listener) {
        if (listener.enterMathOperator) {
            listener.enterMathOperator(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMathOperator) {
            listener.exitMathOperator(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMathOperator) {
            return visitor.visitMathOperator(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class UnaryOperatorContext extends ParserRuleContext {
    EXCLAMATION_SYMBOL() { return this.tryGetToken(FlinkSqlParser.EXCLAMATION_SYMBOL, 0); }
    BIT_NOT_OP() { return this.tryGetToken(FlinkSqlParser.BIT_NOT_OP, 0); }
    ADD_SIGN() { return this.tryGetToken(FlinkSqlParser.ADD_SIGN, 0); }
    HYPNEN_SIGN() { return this.tryGetToken(FlinkSqlParser.HYPNEN_SIGN, 0); }
    KW_NOT() { return this.tryGetToken(FlinkSqlParser.KW_NOT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_unaryOperator; }
    // @Override
    enterRule(listener) {
        if (listener.enterUnaryOperator) {
            listener.enterUnaryOperator(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitUnaryOperator) {
            listener.exitUnaryOperator(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitUnaryOperator) {
            return visitor.visitUnaryOperator(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ConstantContext extends ParserRuleContext {
    timeIntervalExpression() {
        return this.tryGetRuleContext(0, TimeIntervalExpressionContext);
    }
    timePointLiteral() {
        return this.tryGetRuleContext(0, TimePointLiteralContext);
    }
    stringLiteral() {
        return this.tryGetRuleContext(0, StringLiteralContext);
    }
    decimalLiteral() {
        return this.tryGetRuleContext(0, DecimalLiteralContext);
    }
    HYPNEN_SIGN() { return this.tryGetToken(FlinkSqlParser.HYPNEN_SIGN, 0); }
    booleanLiteral() {
        return this.tryGetRuleContext(0, BooleanLiteralContext);
    }
    REAL_LITERAL() { return this.tryGetToken(FlinkSqlParser.REAL_LITERAL, 0); }
    BIT_STRING() { return this.tryGetToken(FlinkSqlParser.BIT_STRING, 0); }
    KW_NULL() { return this.tryGetToken(FlinkSqlParser.KW_NULL, 0); }
    KW_NOT() { return this.tryGetToken(FlinkSqlParser.KW_NOT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_constant; }
    // @Override
    enterRule(listener) {
        if (listener.enterConstant) {
            listener.enterConstant(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitConstant) {
            listener.exitConstant(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitConstant) {
            return visitor.visitConstant(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TimePointLiteralContext extends ParserRuleContext {
    timePointUnit() {
        return this.getRuleContext(0, TimePointUnitContext);
    }
    stringLiteral() {
        return this.getRuleContext(0, StringLiteralContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_timePointLiteral; }
    // @Override
    enterRule(listener) {
        if (listener.enterTimePointLiteral) {
            listener.enterTimePointLiteral(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTimePointLiteral) {
            listener.exitTimePointLiteral(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTimePointLiteral) {
            return visitor.visitTimePointLiteral(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class StringLiteralContext extends ParserRuleContext {
    STRING_LITERAL() { return this.getToken(FlinkSqlParser.STRING_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_stringLiteral; }
    // @Override
    enterRule(listener) {
        if (listener.enterStringLiteral) {
            listener.enterStringLiteral(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitStringLiteral) {
            listener.exitStringLiteral(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitStringLiteral) {
            return visitor.visitStringLiteral(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class DecimalLiteralContext extends ParserRuleContext {
    DIG_LITERAL() { return this.getToken(FlinkSqlParser.DIG_LITERAL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_decimalLiteral; }
    // @Override
    enterRule(listener) {
        if (listener.enterDecimalLiteral) {
            listener.enterDecimalLiteral(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitDecimalLiteral) {
            listener.exitDecimalLiteral(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitDecimalLiteral) {
            return visitor.visitDecimalLiteral(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class BooleanLiteralContext extends ParserRuleContext {
    KW_TRUE() { return this.tryGetToken(FlinkSqlParser.KW_TRUE, 0); }
    KW_FALSE() { return this.tryGetToken(FlinkSqlParser.KW_FALSE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_booleanLiteral; }
    // @Override
    enterRule(listener) {
        if (listener.enterBooleanLiteral) {
            listener.enterBooleanLiteral(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitBooleanLiteral) {
            listener.exitBooleanLiteral(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitBooleanLiteral) {
            return visitor.visitBooleanLiteral(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SetQuantifierContext extends ParserRuleContext {
    KW_DISTINCT() { return this.tryGetToken(FlinkSqlParser.KW_DISTINCT, 0); }
    KW_ALL() { return this.tryGetToken(FlinkSqlParser.KW_ALL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_setQuantifier; }
    // @Override
    enterRule(listener) {
        if (listener.enterSetQuantifier) {
            listener.enterSetQuantifier(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSetQuantifier) {
            listener.exitSetQuantifier(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSetQuantifier) {
            return visitor.visitSetQuantifier(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TimePointUnitContext extends ParserRuleContext {
    KW_YEAR() { return this.tryGetToken(FlinkSqlParser.KW_YEAR, 0); }
    KW_QUARTER() { return this.tryGetToken(FlinkSqlParser.KW_QUARTER, 0); }
    KW_MONTH() { return this.tryGetToken(FlinkSqlParser.KW_MONTH, 0); }
    KW_WEEK() { return this.tryGetToken(FlinkSqlParser.KW_WEEK, 0); }
    KW_DAY() { return this.tryGetToken(FlinkSqlParser.KW_DAY, 0); }
    KW_HOUR() { return this.tryGetToken(FlinkSqlParser.KW_HOUR, 0); }
    KW_MINUTE() { return this.tryGetToken(FlinkSqlParser.KW_MINUTE, 0); }
    KW_SECOND() { return this.tryGetToken(FlinkSqlParser.KW_SECOND, 0); }
    KW_MILLISECOND() { return this.tryGetToken(FlinkSqlParser.KW_MILLISECOND, 0); }
    KW_MICROSECOND() { return this.tryGetToken(FlinkSqlParser.KW_MICROSECOND, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_timePointUnit; }
    // @Override
    enterRule(listener) {
        if (listener.enterTimePointUnit) {
            listener.enterTimePointUnit(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTimePointUnit) {
            listener.exitTimePointUnit(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTimePointUnit) {
            return visitor.visitTimePointUnit(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class TimeIntervalUnitContext extends ParserRuleContext {
    KW_MILLENNIUM() { return this.tryGetToken(FlinkSqlParser.KW_MILLENNIUM, 0); }
    KW_CENTURY() { return this.tryGetToken(FlinkSqlParser.KW_CENTURY, 0); }
    KW_DECADE() { return this.tryGetToken(FlinkSqlParser.KW_DECADE, 0); }
    KW_YEAR() { return this.tryGetToken(FlinkSqlParser.KW_YEAR, 0); }
    KW_YEARS() { return this.tryGetToken(FlinkSqlParser.KW_YEARS, 0); }
    KW_QUARTER() { return this.tryGetToken(FlinkSqlParser.KW_QUARTER, 0); }
    KW_MONTH() { return this.tryGetToken(FlinkSqlParser.KW_MONTH, 0); }
    KW_MONTHS() { return this.tryGetToken(FlinkSqlParser.KW_MONTHS, 0); }
    KW_WEEK() { return this.tryGetToken(FlinkSqlParser.KW_WEEK, 0); }
    KW_WEEKS() { return this.tryGetToken(FlinkSqlParser.KW_WEEKS, 0); }
    KW_DAY() { return this.tryGetToken(FlinkSqlParser.KW_DAY, 0); }
    KW_DAYS() { return this.tryGetToken(FlinkSqlParser.KW_DAYS, 0); }
    KW_HOUR() { return this.tryGetToken(FlinkSqlParser.KW_HOUR, 0); }
    KW_HOURS() { return this.tryGetToken(FlinkSqlParser.KW_HOURS, 0); }
    KW_MINUTE() { return this.tryGetToken(FlinkSqlParser.KW_MINUTE, 0); }
    KW_MINUTES() { return this.tryGetToken(FlinkSqlParser.KW_MINUTES, 0); }
    KW_SECOND() { return this.tryGetToken(FlinkSqlParser.KW_SECOND, 0); }
    KW_SECONDS() { return this.tryGetToken(FlinkSqlParser.KW_SECONDS, 0); }
    KW_MILLISECOND() { return this.tryGetToken(FlinkSqlParser.KW_MILLISECOND, 0); }
    KW_MICROSECOND() { return this.tryGetToken(FlinkSqlParser.KW_MICROSECOND, 0); }
    KW_NANOSECOND() { return this.tryGetToken(FlinkSqlParser.KW_NANOSECOND, 0); }
    KW_EPOCH() { return this.tryGetToken(FlinkSqlParser.KW_EPOCH, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_timeIntervalUnit; }
    // @Override
    enterRule(listener) {
        if (listener.enterTimeIntervalUnit) {
            listener.enterTimeIntervalUnit(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitTimeIntervalUnit) {
            listener.exitTimeIntervalUnit(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitTimeIntervalUnit) {
            return visitor.visitTimeIntervalUnit(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ReservedKeywordsUsedAsFuncParamContext extends ParserRuleContext {
    KW_ARRAY() { return this.tryGetToken(FlinkSqlParser.KW_ARRAY, 0); }
    KW_ALL() { return this.tryGetToken(FlinkSqlParser.KW_ALL, 0); }
    KW_BOTH() { return this.tryGetToken(FlinkSqlParser.KW_BOTH, 0); }
    KW_CURRENT_TIMESTAMP() { return this.tryGetToken(FlinkSqlParser.KW_CURRENT_TIMESTAMP, 0); }
    KW_DISTINCT() { return this.tryGetToken(FlinkSqlParser.KW_DISTINCT, 0); }
    KW_LEADING() { return this.tryGetToken(FlinkSqlParser.KW_LEADING, 0); }
    KW_TRAILING() { return this.tryGetToken(FlinkSqlParser.KW_TRAILING, 0); }
    KW_VALUE() { return this.tryGetToken(FlinkSqlParser.KW_VALUE, 0); }
    ASTERISK_SIGN() { return this.tryGetToken(FlinkSqlParser.ASTERISK_SIGN, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_reservedKeywordsUsedAsFuncParam; }
    // @Override
    enterRule(listener) {
        if (listener.enterReservedKeywordsUsedAsFuncParam) {
            listener.enterReservedKeywordsUsedAsFuncParam(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitReservedKeywordsUsedAsFuncParam) {
            listener.exitReservedKeywordsUsedAsFuncParam(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitReservedKeywordsUsedAsFuncParam) {
            return visitor.visitReservedKeywordsUsedAsFuncParam(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class ReservedKeywordsUsedAsFuncNameContext extends ParserRuleContext {
    KW_ABS() { return this.tryGetToken(FlinkSqlParser.KW_ABS, 0); }
    KW_ARRAY() { return this.tryGetToken(FlinkSqlParser.KW_ARRAY, 0); }
    KW_AVG() { return this.tryGetToken(FlinkSqlParser.KW_AVG, 0); }
    KW_CARDINALITY() { return this.tryGetToken(FlinkSqlParser.KW_CARDINALITY, 0); }
    KW_CAST() { return this.tryGetToken(FlinkSqlParser.KW_CAST, 0); }
    KW_CEIL() { return this.tryGetToken(FlinkSqlParser.KW_CEIL, 0); }
    KW_CEILING() { return this.tryGetToken(FlinkSqlParser.KW_CEILING, 0); }
    KW_COALESCE() { return this.tryGetToken(FlinkSqlParser.KW_COALESCE, 0); }
    KW_COLLECT() { return this.tryGetToken(FlinkSqlParser.KW_COLLECT, 0); }
    KW_COUNT() { return this.tryGetToken(FlinkSqlParser.KW_COUNT, 0); }
    KW_CUME_DIST() { return this.tryGetToken(FlinkSqlParser.KW_CUME_DIST, 0); }
    KW_CURRENT_DATE() { return this.tryGetToken(FlinkSqlParser.KW_CURRENT_DATE, 0); }
    KW_CURRENT_TIME() { return this.tryGetToken(FlinkSqlParser.KW_CURRENT_TIME, 0); }
    KW_CURRENT_TIMESTAMP() { return this.tryGetToken(FlinkSqlParser.KW_CURRENT_TIMESTAMP, 0); }
    KW_DATE() { return this.tryGetToken(FlinkSqlParser.KW_DATE, 0); }
    KW_DAYOFWEEK() { return this.tryGetToken(FlinkSqlParser.KW_DAYOFWEEK, 0); }
    KW_DAYOFYEAR() { return this.tryGetToken(FlinkSqlParser.KW_DAYOFYEAR, 0); }
    KW_DENSE_RANK() { return this.tryGetToken(FlinkSqlParser.KW_DENSE_RANK, 0); }
    KW_ELEMENT() { return this.tryGetToken(FlinkSqlParser.KW_ELEMENT, 0); }
    KW_EXP() { return this.tryGetToken(FlinkSqlParser.KW_EXP, 0); }
    KW_EXTRACT() { return this.tryGetToken(FlinkSqlParser.KW_EXTRACT, 0); }
    KW_FIRST_VALUE() { return this.tryGetToken(FlinkSqlParser.KW_FIRST_VALUE, 0); }
    KW_FLOOR() { return this.tryGetToken(FlinkSqlParser.KW_FLOOR, 0); }
    KW_GROUPING() { return this.tryGetToken(FlinkSqlParser.KW_GROUPING, 0); }
    KW_HOUR() { return this.tryGetToken(FlinkSqlParser.KW_HOUR, 0); }
    KW_IF() { return this.tryGetToken(FlinkSqlParser.KW_IF, 0); }
    KW_LAG() { return this.tryGetToken(FlinkSqlParser.KW_LAG, 0); }
    KW_LAST_VALUE() { return this.tryGetToken(FlinkSqlParser.KW_LAST_VALUE, 0); }
    KW_LEAD() { return this.tryGetToken(FlinkSqlParser.KW_LEAD, 0); }
    KW_LEFT() { return this.tryGetToken(FlinkSqlParser.KW_LEFT, 0); }
    KW_LN() { return this.tryGetToken(FlinkSqlParser.KW_LN, 0); }
    KW_LOCALTIME() { return this.tryGetToken(FlinkSqlParser.KW_LOCALTIME, 0); }
    KW_LOCALTIMESTAMP() { return this.tryGetToken(FlinkSqlParser.KW_LOCALTIMESTAMP, 0); }
    KW_LOWER() { return this.tryGetToken(FlinkSqlParser.KW_LOWER, 0); }
    KW_MAP() { return this.tryGetToken(FlinkSqlParser.KW_MAP, 0); }
    KW_MAX() { return this.tryGetToken(FlinkSqlParser.KW_MAX, 0); }
    KW_MIN() { return this.tryGetToken(FlinkSqlParser.KW_MIN, 0); }
    KW_MINUTE() { return this.tryGetToken(FlinkSqlParser.KW_MINUTE, 0); }
    KW_MOD() { return this.tryGetToken(FlinkSqlParser.KW_MOD, 0); }
    KW_MONTH() { return this.tryGetToken(FlinkSqlParser.KW_MONTH, 0); }
    KW_NULLIF() { return this.tryGetToken(FlinkSqlParser.KW_NULLIF, 0); }
    KW_NTILE() { return this.tryGetToken(FlinkSqlParser.KW_NTILE, 0); }
    KW_OVERLAY() { return this.tryGetToken(FlinkSqlParser.KW_OVERLAY, 0); }
    KW_PERCENT_RANK() { return this.tryGetToken(FlinkSqlParser.KW_PERCENT_RANK, 0); }
    KW_POSITION() { return this.tryGetToken(FlinkSqlParser.KW_POSITION, 0); }
    KW_POWER() { return this.tryGetToken(FlinkSqlParser.KW_POWER, 0); }
    KW_QUARTER() { return this.tryGetToken(FlinkSqlParser.KW_QUARTER, 0); }
    KW_ROW() { return this.tryGetToken(FlinkSqlParser.KW_ROW, 0); }
    KW_ROWS() { return this.tryGetToken(FlinkSqlParser.KW_ROWS, 0); }
    KW_ROW_NUMBER() { return this.tryGetToken(FlinkSqlParser.KW_ROW_NUMBER, 0); }
    KW_RANK() { return this.tryGetToken(FlinkSqlParser.KW_RANK, 0); }
    KW_RIGHT() { return this.tryGetToken(FlinkSqlParser.KW_RIGHT, 0); }
    KW_SECOND() { return this.tryGetToken(FlinkSqlParser.KW_SECOND, 0); }
    KW_STDDEV_POP() { return this.tryGetToken(FlinkSqlParser.KW_STDDEV_POP, 0); }
    KW_STDDEV_SAMP() { return this.tryGetToken(FlinkSqlParser.KW_STDDEV_SAMP, 0); }
    KW_SUBSTRING() { return this.tryGetToken(FlinkSqlParser.KW_SUBSTRING, 0); }
    KW_SUM() { return this.tryGetToken(FlinkSqlParser.KW_SUM, 0); }
    KW_TIME() { return this.tryGetToken(FlinkSqlParser.KW_TIME, 0); }
    KW_TIMESTAMP() { return this.tryGetToken(FlinkSqlParser.KW_TIMESTAMP, 0); }
    KW_TIMESTAMP_DIFF() { return this.tryGetToken(FlinkSqlParser.KW_TIMESTAMP_DIFF, 0); }
    KW_TRIM() { return this.tryGetToken(FlinkSqlParser.KW_TRIM, 0); }
    KW_TRUNCATE() { return this.tryGetToken(FlinkSqlParser.KW_TRUNCATE, 0); }
    KW_TRY_CAST() { return this.tryGetToken(FlinkSqlParser.KW_TRY_CAST, 0); }
    KW_UPPER() { return this.tryGetToken(FlinkSqlParser.KW_UPPER, 0); }
    KW_VAR_POP() { return this.tryGetToken(FlinkSqlParser.KW_VAR_POP, 0); }
    KW_VAR_SAMP() { return this.tryGetToken(FlinkSqlParser.KW_VAR_SAMP, 0); }
    KW_WEEK() { return this.tryGetToken(FlinkSqlParser.KW_WEEK, 0); }
    KW_YEAR() { return this.tryGetToken(FlinkSqlParser.KW_YEAR, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_reservedKeywordsUsedAsFuncName; }
    // @Override
    enterRule(listener) {
        if (listener.enterReservedKeywordsUsedAsFuncName) {
            listener.enterReservedKeywordsUsedAsFuncName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitReservedKeywordsUsedAsFuncName) {
            listener.exitReservedKeywordsUsedAsFuncName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitReservedKeywordsUsedAsFuncName) {
            return visitor.visitReservedKeywordsUsedAsFuncName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class NonReservedKeywordsContext extends ParserRuleContext {
    KW_ADD() { return this.tryGetToken(FlinkSqlParser.KW_ADD, 0); }
    KW_AFTER() { return this.tryGetToken(FlinkSqlParser.KW_AFTER, 0); }
    KW_ASC() { return this.tryGetToken(FlinkSqlParser.KW_ASC, 0); }
    KW_CASCADE() { return this.tryGetToken(FlinkSqlParser.KW_CASCADE, 0); }
    KW_CATALOG() { return this.tryGetToken(FlinkSqlParser.KW_CATALOG, 0); }
    KW_CENTURY() { return this.tryGetToken(FlinkSqlParser.KW_CENTURY, 0); }
    KW_CONFIG() { return this.tryGetToken(FlinkSqlParser.KW_CONFIG, 0); }
    KW_CONSTRAINTS() { return this.tryGetToken(FlinkSqlParser.KW_CONSTRAINTS, 0); }
    KW_CUMULATE() { return this.tryGetToken(FlinkSqlParser.KW_CUMULATE, 0); }
    KW_DATA() { return this.tryGetToken(FlinkSqlParser.KW_DATA, 0); }
    KW_DATABASE() { return this.tryGetToken(FlinkSqlParser.KW_DATABASE, 0); }
    KW_DAYS() { return this.tryGetToken(FlinkSqlParser.KW_DAYS, 0); }
    KW_DECADE() { return this.tryGetToken(FlinkSqlParser.KW_DECADE, 0); }
    KW_DESC() { return this.tryGetToken(FlinkSqlParser.KW_DESC, 0); }
    KW_DESCRIPTOR() { return this.tryGetToken(FlinkSqlParser.KW_DESCRIPTOR, 0); }
    KW_DIV() { return this.tryGetToken(FlinkSqlParser.KW_DIV, 0); }
    KW_ENGINE() { return this.tryGetToken(FlinkSqlParser.KW_ENGINE, 0); }
    KW_EPOCH() { return this.tryGetToken(FlinkSqlParser.KW_EPOCH, 0); }
    KW_EXCLUDING() { return this.tryGetToken(FlinkSqlParser.KW_EXCLUDING, 0); }
    KW_FILE() { return this.tryGetToken(FlinkSqlParser.KW_FILE, 0); }
    KW_FIRST() { return this.tryGetToken(FlinkSqlParser.KW_FIRST, 0); }
    KW_GENERATED() { return this.tryGetToken(FlinkSqlParser.KW_GENERATED, 0); }
    KW_HOP() { return this.tryGetToken(FlinkSqlParser.KW_HOP, 0); }
    KW_HOURS() { return this.tryGetToken(FlinkSqlParser.KW_HOURS, 0); }
    KW_IGNORE() { return this.tryGetToken(FlinkSqlParser.KW_IGNORE, 0); }
    KW_INCLUDING() { return this.tryGetToken(FlinkSqlParser.KW_INCLUDING, 0); }
    KW_JAR() { return this.tryGetToken(FlinkSqlParser.KW_JAR, 0); }
    KW_JARS() { return this.tryGetToken(FlinkSqlParser.KW_JARS, 0); }
    KW_JAVA() { return this.tryGetToken(FlinkSqlParser.KW_JAVA, 0); }
    KW_KEY() { return this.tryGetToken(FlinkSqlParser.KW_KEY, 0); }
    KW_LAST() { return this.tryGetToken(FlinkSqlParser.KW_LAST, 0); }
    KW_LOAD() { return this.tryGetToken(FlinkSqlParser.KW_LOAD, 0); }
    KW_MAP() { return this.tryGetToken(FlinkSqlParser.KW_MAP, 0); }
    KW_MICROSECOND() { return this.tryGetToken(FlinkSqlParser.KW_MICROSECOND, 0); }
    KW_MILLENNIUM() { return this.tryGetToken(FlinkSqlParser.KW_MILLENNIUM, 0); }
    KW_MILLISECOND() { return this.tryGetToken(FlinkSqlParser.KW_MILLISECOND, 0); }
    KW_MINUTES() { return this.tryGetToken(FlinkSqlParser.KW_MINUTES, 0); }
    KW_MONTHS() { return this.tryGetToken(FlinkSqlParser.KW_MONTHS, 0); }
    KW_NANOSECOND() { return this.tryGetToken(FlinkSqlParser.KW_NANOSECOND, 0); }
    KW_NULLS() { return this.tryGetToken(FlinkSqlParser.KW_NULLS, 0); }
    KW_OPTIONS() { return this.tryGetToken(FlinkSqlParser.KW_OPTIONS, 0); }
    KW_PAST() { return this.tryGetToken(FlinkSqlParser.KW_PAST, 0); }
    KW_PLAN() { return this.tryGetToken(FlinkSqlParser.KW_PLAN, 0); }
    KW_PRECEDING() { return this.tryGetToken(FlinkSqlParser.KW_PRECEDING, 0); }
    KW_PYTHON() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON, 0); }
    KW_PYTHON_ARCHIVES() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_ARCHIVES, 0); }
    KW_PYTHON_DEPENDENCIES() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_DEPENDENCIES, 0); }
    KW_PYTHON_FILES() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_FILES, 0); }
    KW_PYTHON_JAR() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_JAR, 0); }
    KW_PYTHON_PARAMETER() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_PARAMETER, 0); }
    KW_PYTHON_REQUIREMENTS() { return this.tryGetToken(FlinkSqlParser.KW_PYTHON_REQUIREMENTS, 0); }
    KW_QUARTER() { return this.tryGetToken(FlinkSqlParser.KW_QUARTER, 0); }
    KW_REMOVE() { return this.tryGetToken(FlinkSqlParser.KW_REMOVE, 0); }
    KW_RESTRICT() { return this.tryGetToken(FlinkSqlParser.KW_RESTRICT, 0); }
    KW_SECONDS() { return this.tryGetToken(FlinkSqlParser.KW_SECONDS, 0); }
    KW_SESSION() { return this.tryGetToken(FlinkSqlParser.KW_SESSION, 0); }
    KW_SETS() { return this.tryGetToken(FlinkSqlParser.KW_SETS, 0); }
    KW_SIZE() { return this.tryGetToken(FlinkSqlParser.KW_SIZE, 0); }
    KW_SLIDE() { return this.tryGetToken(FlinkSqlParser.KW_SLIDE, 0); }
    KW_STEP() { return this.tryGetToken(FlinkSqlParser.KW_STEP, 0); }
    KW_TEMPORARY() { return this.tryGetToken(FlinkSqlParser.KW_TEMPORARY, 0); }
    KW_TIMECOL() { return this.tryGetToken(FlinkSqlParser.KW_TIMECOL, 0); }
    KW_TUMBLE() { return this.tryGetToken(FlinkSqlParser.KW_TUMBLE, 0); }
    KW_UNLOAD() { return this.tryGetToken(FlinkSqlParser.KW_UNLOAD, 0); }
    KW_VIEW() { return this.tryGetToken(FlinkSqlParser.KW_VIEW, 0); }
    KW_WEEK() { return this.tryGetToken(FlinkSqlParser.KW_WEEK, 0); }
    KW_YEARS() { return this.tryGetToken(FlinkSqlParser.KW_YEARS, 0); }
    KW_ZONE() { return this.tryGetToken(FlinkSqlParser.KW_ZONE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return FlinkSqlParser.RULE_nonReservedKeywords; }
    // @Override
    enterRule(listener) {
        if (listener.enterNonReservedKeywords) {
            listener.enterNonReservedKeywords(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitNonReservedKeywords) {
            listener.exitNonReservedKeywords(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitNonReservedKeywords) {
            return visitor.visitNonReservedKeywords(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
