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

parser grammar TypeProcessorDSLParser;
options { tokenVocab = TypeProcessorDSLLexer; }
/* ----------------------------------------------------------------------------------- Statement & Command */

/* Entry point */
rootInstSet : defineInst? typeSetInst* EOF;

defineInst  : DEFINE idStr (ALIAS LSBT defineAlias RSBT)? (OCBR defineConf* CCBR)?;
defineAlias : idStr+;
defineConf  : idStr SET anyValue SEM?;

typeSetInst : (typeInst SEM?)+;
typeInst    : colTypeName (colTypeConf+ | (FOLLOW flowName) | (THROW STRING))?;
colTypeName : LSBT (ALL | idStr) RSBT;
colTypeConf : idStr (SET | APPEND) anyValue;
flowName    : colTypeName; // to alias name

/*---------------------------------------------------------------------------*/
anyValue    : extValue | baseValue | listValue | objectValue | envValue | funcCall;

/* Function */
funcCall    : IDENTIFIER LBT ( anyValue (COMMA anyValue)* )? RBT;
/* Base type */
baseValue   : STRING                                                     #stringValue    // String
            | NULL                                                       #nullValue      // Null value
            | (TRUE | FALSE)                                             #booleanValue   // boolean type
            | (DECIMAL_NUM | INTEGER_NUM | HEX_NUM | OCT_NUM | BIT_NUM)  #numberValue    // Numeric type
            | TYPE                                                       #typeValue      // Type
            ;
/* Extended type */
extValue    : SIZE | IDENTIFIER;
/* Environment variable */
envValue    : ENV idStr CCBR;
/* List structure */
listValue   : LSBT (anyValue+ (COMMA anyValue)*)? RSBT;
/* Object structure */
objectValue : OCBR (objectItem+ ( COMMA objectItem)*)? CCBR;
objectItem  : idStr COLON anyValue;
/* ID\type\string */
idStr       : STRING | TYPE | IDENTIFIER;
