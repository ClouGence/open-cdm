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
package com.clougence.clouddm.ds.iotdb.metadata;

import java.util.List;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.ui.template.CmdExample;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import com.clougence.utils.StringUtils;

public class IotdbCmdTemplateSpi implements CmdTemplateSpi {

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "SHOW VERSION;";
    }

    @Override
    public List<CmdExample> getExamples() {
        return List.of(new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TREE_CREATE,
            "CREATE TIMESERIES root.demo.device1.temperature WITH DATATYPE=FLOAT, ENCODING=GORILLA, COMPRESSOR=LZ4;"), new CmdExample(
                IotdbI18nKeys.IOTDB_EXAMPLE_TREE_CREATE_ALIGNED,
                "CREATE ALIGNED TIMESERIES root.demo.device2 (temperature FLOAT ENCODING=GORILLA COMPRESSOR=LZ4, pressure INT32 ENCODING=RLE COMPRESSOR=LZ4);"), new CmdExample(
                    IotdbI18nKeys.IOTDB_EXAMPLE_TREE_INSERT,
                    "INSERT INTO root.demo.device1 (time, temperature) VALUES (1, 21.5);"), new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TREE_INSERT_ALIGNED,
                        "INSERT INTO root.demo.device2 (time, temperature, pressure) ALIGNED VALUES (1, 22.5, 101);"), new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TREE_DELETE,
                            "DELETE FROM root.demo.device1.temperature WHERE time >= 1 AND time <= 10;"), new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TREE_DROP,
                                "DROP TIMESERIES root.demo.device1.temperature;"), new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TABLE_CREATE,
                                    "CREATE TABLE demo.samples (device STRING TAG, model STRING ATTRIBUTE, temperature FLOAT FIELD, pressure INT32 FIELD);"), new CmdExample(
                                        IotdbI18nKeys.IOTDB_EXAMPLE_TABLE_ALTER,
                                        "ALTER TABLE demo.samples ADD COLUMN humidity FLOAT FIELD;"), new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TABLE_DROP_COLUMN,
                                            "ALTER TABLE demo.samples DROP COLUMN humidity;"), new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TABLE_INSERT,
                                                "INSERT INTO demo.samples (time, device, temperature) VALUES (1, 'device1', 23.5);"), new CmdExample(
                                                    IotdbI18nKeys.IOTDB_EXAMPLE_TABLE_UPDATE,
                                                    "UPDATE demo.samples SET model = 'model_b' WHERE device = 'device1';"), new CmdExample(IotdbI18nKeys.IOTDB_EXAMPLE_TABLE_DELETE,
                                                        "DELETE FROM demo.samples WHERE device = 'device1' AND time >= 1 AND time <= 10;"));
    }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        if (IotdbSqlDialect.parse(option.getCatalog()) != IotdbSqlDialect.TABLE || StringUtils.isBlank(option.getSchema()) || StringUtils.isBlank(option.getTargetName())) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_OPERATION_UNSUPPORTED, "drop table");
        }
        String prefix = "DROP TABLE ";
        if (option.isUsingExists()) {
            prefix += "IF EXISTS ";
        }
        String database = option.getSchema().replace("\"", "\"\"");
        String table = option.getTargetName().replace("\"", "\"\"");
        return List.of(prefix + "\"" + database + "\".\"" + table + "\";");
    }
}
