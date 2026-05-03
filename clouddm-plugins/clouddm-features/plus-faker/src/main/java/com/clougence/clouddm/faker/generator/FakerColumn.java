/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.clouddm.faker.generator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.clougence.clouddm.faker.config.FakerConfigEnum;
import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import com.clougence.utils.setting.SettingNode;

/**
 * 要生成数据的列基本信息和配置信息
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class FakerColumn {

    private final FakerTable    table;
    @Getter
    private final String        column;
    @Getter
    private final boolean       key;
    @Getter
    private boolean             canBeCut;
    private final Set<UseFor>   ignoreAct;
    private final TypeProcessor typeProcessor;
    @Setter
    @Getter
    private String              selectTemplate;
    @Getter
    private String              insertTemplate;
    @Setter
    @Getter
    private String              setColTemplate;
    @Setter
    @Getter
    private String              setValueTemplate;
    @Getter
    private String              whereColTemplate;
    @Getter
    private String              whereValueTemplate;

    FakerColumn(FakerTable table, RdbColumn rdbColumn, TypeProcessor typeProcessor, Set<UseFor> ignoreAct, FakerFactory factory, SettingNode columnConfig){
        this.table = table;
        this.column = rdbColumn.getName();
        //this.columnType = rdbColumn.getSqlType();
        this.key = rdbColumn.hasConstraint(GeneralConstraintType.Primary) || rdbColumn.hasConstraint(GeneralConstraintType.Unique);
        this.canBeCut = StringUtils.isNotBlank(rdbColumn.getDefaultValue()) || !rdbColumn.hasConstraint(GeneralConstraintType.NonNull);
        this.ignoreAct = new HashSet<>(ignoreAct);
        this.typeProcessor = typeProcessor;
        //this.factory = factory;

        if (columnConfig != null) {
            this.selectTemplate = columnConfig.getSubValue(FakerConfigEnum.SELECT_TEMPLATE.getConfigKey());
            this.insertTemplate = columnConfig.getSubValue(FakerConfigEnum.INSERT_TEMPLATE.getConfigKey());
            this.setColTemplate = columnConfig.getSubValue(FakerConfigEnum.SET_COL_TEMPLATE.getConfigKey());
            this.setValueTemplate = columnConfig.getSubValue(FakerConfigEnum.SET_VALUE_TEMPLATE.getConfigKey());
            this.whereColTemplate = columnConfig.getSubValue(FakerConfigEnum.WHERE_COL_TEMPLATE.getConfigKey());
            this.whereValueTemplate = columnConfig.getSubValue(FakerConfigEnum.WHERE_VALUE_TEMPLATE.getConfigKey());
        }

        if (StringUtils.isBlank(this.selectTemplate)) {
            this.selectTemplate = "{name}";
        }
        if (StringUtils.isBlank(this.insertTemplate)) {
            this.insertTemplate = "?";
        }
        if (StringUtils.isBlank(this.setColTemplate)) {
            this.setColTemplate = "{name}";
        }
        if (StringUtils.isBlank(this.setValueTemplate)) {
            this.setValueTemplate = "?";
        }
        if (StringUtils.isBlank(this.whereColTemplate)) {
            this.whereColTemplate = "{name}";
        }
        if (StringUtils.isBlank(this.whereValueTemplate)) {
            this.whereValueTemplate = "?";
        }

        String colName = factory.getDialect().fmtName(table.isUseQualifier(), this.column);
        this.selectTemplate = this.selectTemplate.replace("{name}", colName);
        this.setColTemplate = this.setColTemplate.replace("{name}", colName);
        this.whereColTemplate = this.whereColTemplate.replace("{name}", colName);
    }

    public boolean isGenerator(UseFor useFor) {
        return !this.ignoreAct.contains(useFor);
    }

    /** 生成随机值 */
    public SqlArg generatorData() {
        return this.typeProcessor.buildData(this.column);
    }

    /** 从 RS 中读取并生成 SqlArg */
    public SqlArg readData(ResultSet rs) throws SQLException {
        return this.typeProcessor.buildData(rs, this.column);
    }

    /** 随机种子的配置 */
    public <T extends SeedConfig> T seedConfig() {
        return (T) this.typeProcessor.getSeedConfig();
    }

    @Override
    public String toString() {
        String seedAndWriterString = this.typeProcessor.toString();
        return this.column + ", ignoreAct=" + ignoreAct + ", seedAndWriter=" + seedAndWriterString + '}';
    }

    /** 像列配置一个忽略规则 */
    public FakerColumn ignoreAct(UseFor... ignoreAct) {
        this.ignoreAct.addAll(Arrays.asList(ignoreAct));
        return this;
    }

    /** 重置列忽略规则 */
    public FakerColumn ignoreReset() {
        this.ignoreAct.clear();
        this.ignoreAct.addAll(this.typeProcessor.getDefaultIgnoreAct());
        return this;
    }

    public FakerColumn doNotCut() {
        this.canBeCut = false;
        return this;
    }

    public FakerColumn canBeCut() {
        this.canBeCut = true;
        return this;
    }

    /** 重新创建随机数据发生器 */
    void applyConfig() {
        this.typeProcessor.applyConfig();
        this.ignoreAct.addAll(this.typeProcessor.getDefaultIgnoreAct());
    }
}
