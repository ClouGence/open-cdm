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
parser grammar EsParser;
options { tokenVocab = EsLexer; }

script : EOL* (request (EOL+ request)* EOL*)? EOF;
singleRequest : EOL* request EOL* EOF;
request : header (EOL+ jsonObject)?;
header : METHOD HWS target (HWS headerComment?)?;
headerComment : HCOMMENT | SLASH SLASH (~EOL)*;
target : path (QUESTION parameter (AMP parameter)*)?;
path : SLASH (segment (SLASH segment)* SLASH?)?;
segment : TEXT;
parameter : TEXT (EQUALS parameterValue?)?;
parameterValue : (TEXT | SLASH | QUESTION | EQUALS)+;
jsonObject : LBRACE (member (COMMA member)*)? RBRACE;
member : STRING COLON value;
value : jsonObject | jsonArray | STRING | NUMBER | TRUE | FALSE | NULL;
jsonArray : LBRACKET (value (COMMA value)*)? RBRACKET;
