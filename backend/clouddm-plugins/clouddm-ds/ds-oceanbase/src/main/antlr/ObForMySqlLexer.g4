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

lexer grammar ObForMySqlLexer;

import MySqlLexer;

COMPRESSION: C O M P R E S S I O N;
REPLICA_NUM: R E P L I C A '_' N U M;
BLOCK_SIZE: B L O C K '_' S I Z E;
USE_BLOOM_FILTER: U S E '_' B L O O M '_' F I L T E R;
TABLET_SIZE: T A B L E T '_' S I Z E;
PCTFREE: P C T F R E E;
