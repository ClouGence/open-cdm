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

parser grammar ObForOracleParser;

import PlSqlParser;

@header {
    import com.clougence.clouddm.ds.oracle.parser.base.PlSqlParserBase;
}

options {
    tokenVocab=ObForOracleLexer;
    superClass = PlSqlParserBase;
}


physical_attributes_clause
    : (
        PCTFREE pctfree = UNSIGNED_INTEGER
        | PCTUSED pctused = UNSIGNED_INTEGER
        | INITRANS inittrans = UNSIGNED_INTEGER
        | MAXTRANS maxtrans = UNSIGNED_INTEGER
        | COMPUTE STATISTICS
        | storage_clause
        | compute_clauses

//
        | COMPRESS FOR ARCHIVE
        | REPLICA_NUM '=' UNSIGNED_INTEGER
        | BLOCK_SIZE '=' UNSIGNED_INTEGER
        | USE_BLOOM_FILTER '=' (TRUE | FALSE)
        | TABLET_SIZE '=' UNSIGNED_INTEGER
        | PCTFREE '=' UNSIGNED_INTEGER
    )+
    ;
