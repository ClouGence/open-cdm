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
parser grammar KafkaParser;
options { tokenVocab = KafkaLexer; }

script : separator* (command (separator+ command)* separator*)? EOF;
singleCommand : separator* command separator* EOF;
separator : SEMICOLON | EOL;

command : commandName option*;
commandName : TOPICS | CONSUMER;
option : flagOption | valueOption;
flagOption : HELP | LIST | DESCRIBE | CREATE | DELETE | IF_EXISTS | IF_NOT_EXISTS
           | FROM_BEGINNING;
valueOption : valueName EQUAL? argument;
valueName : TOPIC | PARTITIONS | REPLICATION_FACTOR | CONFIG | PARTITION
          | OFFSET | MAX_MESSAGES | TIMEOUT_MS;
argument : WORD | TOPICS | CONSUMER;
