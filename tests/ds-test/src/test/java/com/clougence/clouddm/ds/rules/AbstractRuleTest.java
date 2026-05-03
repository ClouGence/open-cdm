package com.clougence.clouddm.ds.rules;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sec.rules.domain.CheckerDomain;
import com.clougence.clouddm.sec.rules.domain.func.FuncLoggerUtils;
import com.clougence.clouddm.sec.rules.execute.DomainHelper;
import com.clougence.detectrule.dsl.DetectRuleDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.antlr.ThrowingListener;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.detectrule.engine.lang.LangObject;
import com.clougence.detectrule.engine.lang.ValueObject;
import com.clougence.detectrule.engine.lang.reflect.ReflectHelper;
import com.clougence.detectrule.engine.lang.reflect.Type;
import com.clougence.detectrule.engine.lang.reflect.TypeType;
import com.clougence.detectrule.engine.runtime.DefaultDataTimeValueParser;
import com.clougence.detectrule.engine.runtime.EngineOption;
import com.clougence.detectrule.engine.runtime.v1.DetectRuleEngineV1;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.ResourcesUtils;

public abstract class AbstractRuleTest {

    private SecDomainResolveSpi resolveSpi = this.createSPI();

    protected abstract SecDomainResolveSpi createSPI();

    protected abstract DataSourceType currentDsType();

    static {
        DslHelper.register("DetectRule", new DetectRuleDslProvider());

        ReflectHelper.addIgnoreField("com.clougence.clouddm.sdk.service.secrules.RuleDomain.children");
        ReflectHelper.addIgnoreField("com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain.selectColumns");
        ReflectHelper.addIgnoreField("com.clougence.clouddm.sdk.service.secrules.RuleDomain.splitScript");
        ReflectHelper.addIgnoreField("com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain.tableAlias");
    }

    public static CharStream fromResource(String script) throws IOException {
        return CharStreams.fromStream(ResourcesUtils.getResourceAsStream(script));
    }

    public static CharStream fromString(String script) {
        return CharStreams.fromString(script);
    }

    public boolean runScript(String checkRuleScript, String testScript, Map<String, String> vars) throws IOException {
        List<RuleDomain> domains = this.resolveSpi.resolveDomain(currentDsType(), codeInfo(testScript), contextInfo());
        List<CheckerDomain> forChecker = DomainHelper.create(domains);

        for (CheckerDomain checkerData : forChecker) {
            CharStream chars = fromResource(checkRuleScript);
            if (!(boolean) runScript(chars, checkerData, vars)) {
                return false;
            }
        }
        return true;
    }

    public static boolean runScript(String script, List<CheckerDomain> domainData, Map<String, String> vars) throws IOException {
        for (CheckerDomain domain : domainData) {
            CharStream chars = fromResource(script);
            if (!(boolean) runScript(chars, domain, vars)) {
                return false;
            }
        }
        return true;
    }

    public static Object runScript(CharStream script, CheckerDomain domainData, Map<String, String> vars) {
        DslProvider provider = DslHelper.getProvider("DetectRule");
        Lexer lexer = provider.createLexer(script);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingListener.INSTANCE);

        Parser qlParser = provider.createParser(lexer);
        qlParser.removeErrorListeners();
        qlParser.addErrorListener(ThrowingListener.INSTANCE);

        StatementSet statements = provider.doParser(lexer, qlParser);
        Type domainType = ReflectHelper.resolveDomain(domainData.getClass());

        EngineOption option = new EngineOption();
        option.setDataTimeValueParser(new DefaultDataTimeValueParser());
        DetectRuleEngineV1 visitor = new DetectRuleEngineV1(domainData, domainType, option);
        visitor.putVariables(vars);

        try {
            statements.accept(visitor);
        } finally {
            //for (String log : FuncLoggerUtils.outputLog) {
            //    System.out.println("LOG: " + log);
            //}
            FuncLoggerUtils.outputLog.clear();
        }
        LangObject returnData = visitor.returnData(new ValueObject(true, TypeType.Boolean));
        return returnData.unwrap();
    }

    public List<RdbSchemaDomain> schemaDomainSql(String sql) {
        List<RuleDomain> list = this.resolveSpi.resolveDomain(currentDsType(), codeInfo(sql), contextInfo());
        return list.stream().filter(ruleDomain -> ruleDomain instanceof RdbSchemaDomain).map(o -> (RdbSchemaDomain) o).collect(Collectors.toList());
    }

    public List<RdbTableDomain> tableDomainSql(String sql) {
        List<RuleDomain> list = this.resolveSpi.resolveDomain(currentDsType(), codeInfo(sql), contextInfo());
        return list.stream().filter(ruleDomain -> ruleDomain instanceof RdbTableDomain).map(o -> (RdbTableDomain) o).collect(Collectors.toList());
    }

    public List<RdbConstraintDomain> constraintDomainSql(String sql) {
        List<RuleDomain> list = this.resolveSpi.resolveDomain(currentDsType(), codeInfo(sql), contextInfo());
        return list.stream().filter(ruleDomain -> ruleDomain instanceof RdbConstraintDomain).map(o -> (RdbConstraintDomain) o).collect(Collectors.toList());
    }

    public List<RdbColumnDomain> columnDomainSql(String sql) {
        List<RuleDomain> list = this.resolveSpi.resolveDomain(currentDsType(), codeInfo(sql), contextInfo());
        return list.stream().filter(ruleDomain -> ruleDomain instanceof RdbColumnDomain).map(o -> (RdbColumnDomain) o).collect(Collectors.toList());
    }

    public List<RuleDomain> queryDomainSql(String sql) {
        return this.resolveSpi.resolveDomain(currentDsType(), codeInfo(sql), contextInfo());
    }

    protected static CodeInfo codeInfo(String sql) {
        return CodeInfo.builder().query(sql).baseLine(1).baseColumn(0).build();
    }

    protected  ContextInfo contextInfo() {
        return ContextInfo.builder().build();
    }
}
