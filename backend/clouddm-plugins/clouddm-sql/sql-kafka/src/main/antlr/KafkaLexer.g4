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
lexer grammar KafkaLexer;

channels { COMMENTS }

TOPICS : 'kafka-topics' ('.sh')?;
GROUPS : 'kafka-consumer-groups' ('.sh')?;
CONSUMER : 'kafka-console-consumer' ('.sh')?;

HELP : '--help';
LIST : '--list';
DESCRIBE : '--describe';
CREATE : '--create';
DELETE : '--delete';
IF_EXISTS : '--if-exists';
IF_NOT_EXISTS : '--if-not-exists';
OFFSETS : '--offsets';
MEMBERS : '--members';
VERBOSE : '--verbose';
STATE : '--state';
FROM_BEGINNING : '--from-beginning';
TOPIC : '--topic';
PARTITIONS : '--partitions';
REPLICATION_FACTOR : '--replication-factor';
CONFIG : '--config';
GROUP : '--group';
PARTITION : '--partition';
OFFSET : '--offset';
MAX_MESSAGES : '--max-messages';
TIMEOUT_MS : '--timeout-ms';
UNKNOWN_OPTION : '--' ~[ \t\f\r\n;='"\\|&<>`]+;

EQUAL : '=';
SEMICOLON : ';';
EOL : '\r'? '\n';
WS : [ \t\f\r]+ -> channel(HIDDEN);
LINE_CONTINUATION : CONTINUATION -> channel(HIDDEN);
COMMENT : '#' ~[\r\n]* -> channel(COMMENTS);

// A word owns quoted/escaped parts and embedded continuations. A standalone
// continuation is hidden, so a following # still starts a comment.
WORD : { _input.LA(1) != '-' || _input.LA(2) != '-' }? FIRST_PART WORD_PART*;
UNFINISHED_WORD : (FIRST_PART WORD_PART*)?
    ('\'' ~[']* EOF | '"' ('\\' . | ~["\\])* '\\'? EOF | '\\' EOF);
SHELL_OPERATOR : [|&<>`]+;
INVALID : .;

fragment FIRST_PART : ~[ \t\f\r\n;='"\\#|&<>`] | ESCAPE | SINGLE_QUOTED | DOUBLE_QUOTED;
fragment WORD_PART : ~[ \t\f\r\n;'"\\|&<>`] | ESCAPE | SINGLE_QUOTED | DOUBLE_QUOTED | CONTINUATION;
fragment ESCAPE : '\\' ~[\r\n];
fragment CONTINUATION : '\\' '\r'? '\n';
fragment SINGLE_QUOTED : '\'' ~[']* '\'';
fragment DOUBLE_QUOTED : '"' ('\\' . | ~["\\])* '"';
