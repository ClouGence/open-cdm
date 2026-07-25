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
package com.clougence.clouddm.ds.hana.sql;

import com.clougence.clouddm.ds.hana.sql.analysis.behavior.HanaBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.hana.sql.parser.HanaSplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

/** @author mode */
public class HanaSqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "SAP Hana SQL";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final BehaviorAnalysisSpi     behaviorAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;
    private final RewriteSpi              rewriteSpi;

    public HanaSqlEngineSpi(){
        this.splitAnalysisSpi = new HanaSplitAnalysisSpi();
        this.secDomainResolveSpi = null;
        this.behaviorAnalysisSpi = new HanaBehaviorAnalysisSpi(splitAnalysisSpi);
        this.selectColumnAnalysisSpi = null;
        this.rewriteSpi = null;
    }

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        throw new UnsupportedOperationException("SAP Hana does not support DslProvider.");
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        return splitAnalysisSpi;
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        return secDomainResolveSpi;
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        return behaviorAnalysisSpi;
    }

    @Override
    public SelectColumnAnalysisSpi selectColumnAnalysisSpi(SqlParserParameters parameters) {
        return selectColumnAnalysisSpi;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return rewriteSpi;
    }

}
