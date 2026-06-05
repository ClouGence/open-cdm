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
package com.clougence.clouddm.faker.config.ui;

/**
 * @author olddream
 */
public interface FakerUiFieldKeys {

    String GLOBAL_PTHREADCNT    = "producer";
    String GLOBAL_WTHREADCNT    = "writer";
    String GLOBAL_TRANSACTION   = "transaction";
    String GLOBAL_IGNOREERRORS  = "ignoreErrors";
    //
    String TABLE_TOTAL          = "total";
    String COLUMN_SEED          = "seedType";
    String COLUMN_NAME          = "name";
    String COLUMN_SUPPORT       = "support";
    //
    String ALLOW_NULLABLE       = "allowNullable";
    String NULLABLE_RATIO       = "nullableRatio";
    String MIN_LENGTH           = "minLength";
    String MAX_LENGTH           = "maxLength";
    String ALLOW_EMPTY          = "allowEmpty";
    String NUMBER_TYPE          = "numberType";
    String MIN                  = "min";
    String MAX                  = "max";
    String PRECISION            = "precision";
    String SCALE                = "scale";
    String ABS                  = "abs";
    String DATE_TYPE            = "dateType";
    String GEOMETRY_TYPE        = "geometryType";
    String FORMAT_TYPE          = "formatType";
    String MIN_POINT_SIZE       = "minPointSize";
    String MAX_POINT_SIZE       = "maxPointSize";
    String GEN_TYPE             = "genType";
    String RANDOM_FORM          = "randomForm";
    String RANDOM_TO            = "randomTo";
    String RANDOM_ZONE_FORM     = "zoneForm";
    String RANDOM_ZONE_TO       = "zoneTo";
    String START_TIME           = "startTime";
    String MIN_INTERVAL         = "minInterval";
    String MAX_INTERVAL         = "maxInterval";
    String INTERVAL_SCOPE       = "intervalScope";
    String MIN_SIZE             = "minSize";
    String MAX_SIZE             = "maxSize";

    //increment
    String GLOBAL_TIME          = "time";
    String GLOBAL_INSERT_RATIO  = "insertRatio";
    String GLOBAL_DELETE_RATIO  = "deleteRatio";
    String GLOBAL_UPDATE_RATIO  = "updateRatio";

    //
    String TABLE_WHERE_POLITIC  = "wherePolitic";
    String TABLE_UPDATE_POLITIC = "updatePolitic";
    String TABLE_INSERT_POLITIC = "insertPolitic";

    //
    String COLUMN_INSERT        = "ignoreColsInsert";
    String COLUMN_UPDATE_SET    = "ignoreColsUpdate";
    String COLUMN_UPDATE_WHERE  = "ignoreColsUpdateWhere";
    String COLUMN_DELETE_WHERE  = "ignoreColsDeleteWhere";
    String COLUMN_IGNORE_ACT    = "ignoreAct";
}
