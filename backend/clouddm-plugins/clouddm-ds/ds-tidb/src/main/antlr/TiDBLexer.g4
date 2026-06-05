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

lexer grammar TiDBLexer;

import MySqlLexer;

TTL: T T L;
TTL_ENABLE : T T L '_' E N A B L E;
AUTO_RANDOM: A U T O '_' R A N D O M;

NOCYCLE: N O C Y C L E;
CYCLE: C Y C L E;
NOCACHE: N O C A C H E;
MINVALUE: M I N V A L U E;
NOMINVALUE: N O M I N V A L U E;
NOMAXVALUE: N O M A X V A L U E;
SEQUENCE: S E Q U E N C E;
INCREMENT: I N C R E M E N T;
CLUSTERED: C L U S T E R E D;
NONCLUSTERED: N O N C L U S T E R E D;