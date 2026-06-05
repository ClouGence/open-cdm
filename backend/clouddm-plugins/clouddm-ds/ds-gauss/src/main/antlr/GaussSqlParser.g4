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

parser grammar GaussSqlParser;

import PgSqlParser;

@header {
    import com.clougence.clouddm.ds.gauss.parser.base.GaussSqlParserBase;
}

options {
    tokenVocab = GaussSqlLexer;
    superClass = GaussSqlParserBase;
}




createfunc_opt_item
    : AS func_as
    | LANGUAGE nonreservedword_or_sconst
    | TRANSFORM transform_type_list
    | WINDOW
    | common_func_opt_item
    | AS DECLARE? func_body
    | NOT? FENCED
    | NOT? SHIPPABLE
    ;

partitionspec
    : PARTITION BY colid OPEN_PAREN part_params CLOSE_PAREN
    | PARTITION BY colid OPEN_PAREN part_params CLOSE_PAREN (OPEN_PAREN (partition_item | partition_item ',')+  CLOSE_PAREN)
    ;

union_context
    : (UNION | EXCEPT | INTERSECT)
    | ( UNION | EXCEPT | INTERSECT | MINUS_)
    ;


