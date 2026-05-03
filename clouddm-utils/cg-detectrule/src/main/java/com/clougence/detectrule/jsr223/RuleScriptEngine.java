///*
// * Copyright 2008-2009 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.clougence.engine.detectrule.jsr223;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.io.StringReader;
//
//import javax.script.*;
//
//import net.hasor.dataql.Finder;
//import net.hasor.dataql.compiler.qil.QIL;
//import net.hasor.dataql.parser.QueryModel;
//import net.hasor.dataql.runtime.CompilerArguments;
//import net.hasor.dataql.runtime.HintsSet;
//import net.hasor.dataql.runtime.QueryHelper;
//
///**
// * JSR223 引擎机制的实现。
// * @author 赵永春 (zyc@hasor.net)
// * @version : 2017-10-19
// */
//public class RuleScriptEngine extends AbstractScriptEngine implements ScriptEngine, Compilable {
//
//    private final HintsSet                optionSet = new HintsSet();
//    private final RuleScriptEngineFactory engineFactory;
//    private Finder                        finder    = Finder.DEFAULT;
//
//    RuleScriptEngine(RuleScriptEngineFactory engineFactory){
//        this.engineFactory = engineFactory;
//    }
//
//    // -------------------------------------------------------------------------------------------- ScriptEngine
//
//    @Override
//    public ScriptEngineFactory getFactory() { return this.engineFactory; }
//
//    @Override
//    public Bindings createBindings() {
//        return new SimpleBindings();
//    }
//    // -------------------------------------------------------------------------------------------- ScriptEngine
//
//    @Override
//    public CompiledScript compile(Reader queryString) throws ScriptException {
//        try {
//            Bindings global = this.getBindings(ScriptContext.GLOBAL_SCOPE);
//            if (global == null) {
//                global = createBindings();
//                this.setBindings(createBindings(), ScriptContext.GLOBAL_SCOPE);
//            }
//            //
//            QueryModel queryModel = QueryHelper.queryParser(queryString);
//            CompilerArguments compilerArguments = CompilerArguments.DEFAULT.copyAsNew();
//            compilerArguments.getCompilerVar().addAll(global.keySet());
//            QIL compilerQIL = QueryHelper.queryCompiler(queryModel, compilerArguments, this.getFinder());
//            return new RuleCompiledScript(compilerQIL, this);
//        } catch (IOException e) {
//            throw new ScriptException(e);
//        }
//    }
//
//    @Override
//    public CompiledScript compile(String queryString) throws ScriptException {
//        return this.compile(new StringReader(queryString));
//    }
//
//    @Override
//    public Object eval(Reader queryString, ScriptContext context) throws ScriptException {
//        this.setContext(context);
//        return compile(queryString).eval();
//    }
//
//    @Override
//    public Object eval(String queryString, ScriptContext context) throws ScriptException {
//        this.setContext(context);
//        return compile(queryString).eval();
//    }
//}
