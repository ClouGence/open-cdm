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

lexer grammar PolardbXLexer;

import MySqlLexer;

COVERING: C O V E R I N G;
DBPARTITION: D B P A R T I T I O N;
TBPARTITION: T B P A R T I T I O N;
TBPARTITIONS: T B P A R T I T I O N S;
DBPARTITIONS: D B P A R T I T I O N S;
RANGE_HASH: R A N G E '_' H A S H;
BROADCAST: B R O A D C A S T;
LOCALITY: L O C A L I T Y;
RIGHT_SHIFT: R I G H T '_' S H I F T;
UNI_HASH: U N I '_' H A S H;
PARTITION_MODE: P A R T I T I O N '_' M O D E;
SHARDING: S H A R D I N G;
DN: D N;


