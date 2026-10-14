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
package com.clougence.clouddm.ds.yashandb.execute.mysql;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbFunction;
import com.clougence.schema.umi.special.rdb.RdbProcedure;
import com.clougence.schema.umi.struts.Value;

/**
 * 崖山数据库（YashanDB）MySQL 兼容模式元数据读取。
 * <p>
 * 崖山 MySQL 兼容模式的 {@code information_schema} 覆盖度很高，表、列、索引、约束、视图等与
 * MySQL 完全对齐（实测 23.4.1.109：{@code SCHEMATA/TABLES/COLUMNS/STATISTICS/KEY_COLUMN_USAGE/
 * TABLE_CONSTRAINTS/REFERENTIAL_CONSTRAINTS/VIEWS/TRIGGERS/ENGINES/PARTITIONS/PROCESSLIST/PARAMETERS}
 * 均存在），因此直接复用 {@link MyMetaProviderDm}。
 * </p>
 * <p>
 * 唯一缺口是<b>没有 {@code ROUTINES} 与 {@code EVENTS} 表</b>：父类的存储过程/函数查询依赖
 * {@code information_schema.ROUTINES}，在崖山会直接抛 {@code YAS-02012 table or view does not exist}。
 * 这里对存储过程/函数做<b>降级</b>处理——返回空列表，而不是让「浏览存储过程/函数」报错。
 * </p>
 * <p>
 * 降级是「空」而不是回退到 MySQL 内置函数清单：清单里的函数崖山未必都实现，列出来反而误导；
 * 待崖山补齐 {@code information_schema.ROUTINES} 后，删掉本类的覆写即可自动恢复完整能力。
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLMetaProviderDm extends MyMetaProviderDm {

    public YashanDBMySQLMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public List<Value> selectProcedures(String schema) {
        return Collections.emptyList();
    }

    @Override
    public List<Value> selectFunctions(String schema) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbProcedure> fetchProcedureByPart(Connection conn, String catalog, String schema, List<String> procs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbFunction> fetchFunctionByPart(Connection conn, String catalog, String schema, List<String> funcs) {
        return Collections.emptyList();
    }
}
