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
package com.clougence.clouddm.faker.config.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.JDBCType;
import java.sql.Types;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.config.dsl.DslException;
import com.clougence.clouddm.faker.config.dsl.TypeProcessConf;
import com.clougence.clouddm.faker.config.dsl.TypeProcessConfSet;
import com.clougence.clouddm.faker.config.dsl.model.DataModel;
import com.clougence.clouddm.faker.config.dsl.model.ValueModel;
import com.clougence.clouddm.faker.config.parameter.ParameterProcessor;
import com.clougence.clouddm.faker.config.parameter.ParameterRegistry;
import com.clougence.clouddm.faker.generator.TypeProcessor;
import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.faker.seed.array.ArraySeedConfig;
import com.clougence.clouddm.faker.seed.array.ArraySeedFactory;
import com.clougence.clouddm.faker.seed.bool.BooleanSeedConfig;
import com.clougence.clouddm.faker.seed.bool.BooleanSeedFactory;
import com.clougence.clouddm.faker.seed.bytes.BytesSeedConfig;
import com.clougence.clouddm.faker.seed.bytes.BytesSeedFactory;
import com.clougence.clouddm.faker.seed.date.DateSeedConfig;
import com.clougence.clouddm.faker.seed.date.DateSeedFactory;
import com.clougence.clouddm.faker.seed.date.DateType;
import com.clougence.clouddm.faker.seed.date.GenType;
import com.clougence.clouddm.faker.seed.number.NumberSeedConfig;
import com.clougence.clouddm.faker.seed.number.NumberSeedFactory;
import com.clougence.clouddm.faker.seed.number.NumberType;
import com.clougence.clouddm.faker.seed.string.CharacterSet;
import com.clougence.clouddm.faker.seed.string.StringSeedConfig;
import com.clougence.clouddm.faker.seed.string.StringSeedFactory;
import com.clougence.clouddm.faker.types.TypeHandlerFactory;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.utils.BeanUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;
import com.clougence.utils.io.input.AutoCloseInputStream;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

import com.clougence.utils.setting.SettingNode;

/**
 * 读取并解析 tpc 配置文件，并根据类型和数据库信息选择对应的 tpc 配置。利用 tpc 的配置信息来创建 TypeProcessor。
 * @version : 2023-02-14
 * @author 赵永春 (zyc@hasor.net)
 */
public class DslTypeProcessorFactory implements TypeProcessorFactory {

    protected final static Logger                                 logger         = LoggerFactory.getLogger(DslTypeProcessorFactory.class);
    private final List<String>                                    innerParameter = Arrays.asList(                                         //
            "seedType", "jdbcType", "arrayMinSize", "arrayMaxSize", "arrayTypeHandler", "arrayDimension");
    private final DataSourceType                                  dsType;
    private final Map<String, Object>                             globalVariables;
    private final Map<String, Map<String, List<TypeProcessConf>>> colTypeConfig;
    private final Map<String, String>                             colTypeThrow;
    private final FakerEngineConfig                               fakerConfig;

    public DslTypeProcessorFactory(DataSourceType dsType, Map<String, Object> globalVariables, FakerEngineConfig fakerConfig) throws IOException{
        this.dsType = dsType;
        this.globalVariables = globalVariables;
        this.colTypeConfig = new LinkedCaseInsensitiveMap<>();
        this.colTypeThrow = new LinkedCaseInsensitiveMap<>();
        this.fakerConfig = fakerConfig;

        this.initTypeProcessorPriority(dsType, fakerConfig);
    }

    private void initTypeProcessorPriority(DataSourceType dbType, FakerEngineConfig config) throws IOException {
        // all tpcConf
        List<URI> uriList = loadTpcURIs(config);

        // read tpcConf
        List<TypeProcessConfSet> allTpcConf = parseTypeProcessConf(dbType, uriList);

        // found final TypeProcessConfSet
        TypeProcessConfSet useConfSet = chooseTypeProcessConf(config, allTpcConf);

        // init columnConf
        if (useConfSet != null) {
            DataModel policyValue = useConfSet.getDefConfig("policy");
            Object policyName = policyValue == null ? "" : policyValue.recover(globalVariables);

            logger.info("DSL TypeProcessor policy['" + policyName + "'] use '" + useConfSet.getSource() + "'");
            for (String colType : useConfSet.getConfigKeys()) {
                List<TypeProcessConf> colConfList = useConfSet.getConfig(colType);
                if (colConfList == null || colConfList.isEmpty()) {
                    continue;
                }

                Map<String, List<TypeProcessConf>> typeConfMap = this.colTypeConfig.computeIfAbsent(colType, s -> new LinkedHashMap<>());
                for (TypeProcessConf confItem : colConfList) {
                    typeConfMap.computeIfAbsent(confItem.getConfName(), s -> new ArrayList<>()).add(confItem);
                }

            }

            for (String colType : useConfSet.getThrowKeys()) {
                String throwMessage = useConfSet.getThrow(colType);
                if (throwMessage != null) {
                    this.colTypeThrow.put(colType, throwMessage);
                }
            }
        } else {
            logger.warn("DSL TypeProcessor not found use 'DefaultTypeProcessorFactory'");
        }
    }

    /** 查找可用的 tpcConfig 配置 */
    protected List<URI> loadTpcURIs(FakerEngineConfig config) throws IOException {
        String customTpcConf = config.getCustomTpcConf();

        // custom TpcConf
        if (StringUtils.isNotBlank(customTpcConf)) {
            URL tpcConfURL = ResourcesUtils.getResource(config.getClassLoader(), customTpcConf);
            if (tpcConfURL == null) {
                String errorMsg = "custom tpcConf '" + customTpcConf + "' not found.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            } else {
                try {
                    URI tpcConfURI = tpcConfURL.toURI();
                    logger.info("use custom tpcConf '" + customTpcConf + "' overwrite default.");
                    return Collections.singletonList(tpcConfURI);
                } catch (Exception e) {
                    logger.error("parse custom tpcConf '" + customTpcConf + "' failed, msg is " + e.getMessage(), e);
                }
            }
        }
        // default TpcConf
        URL url = new ClassPathResourceLoader(DslTypeProcessorFactory.class.getClassLoader(), "META-INF").getResource("META-INF/faker-default-dbtpc/");
        JarURLConnection jarc = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarc.getJarFile();
        URL content = new File(jarFile.getName()).toURI().toURL();
        Iterator<JarEntry> zipEntry = jarFile.stream().iterator();
        List<URI> result = new ArrayList<>();
        while (zipEntry.hasNext()) {
            JarEntry entry = zipEntry.next();
            try {
                URI uri = new URL("jar:" + content + "!/" + entry.getName()).toURI();
                if (StringUtils.endsWithIgnoreCase(uri.toString(), ".tpc")) {
                    result.add(uri);
                }
            } catch (URISyntaxException e) {
                if (logger.isDebugEnabled()) {
                    logger.warn("scanJarFile :" + e.getMessage(), e);
                } else {
                    logger.debug("scanJarFile :" + e.getMessage());
                }
            }
        }
        return result;
        // default TpcConf
        //                return new ClassPathResourceLoader(DslTypeProcessorFactory.class.getClassLoader()).scanResources(MatchType.Prefix, event -> {
        //                    URI resource = event.getResource();
        //                    if (StringUtils.endsWithIgnoreCase(resource.toString(), ".tpc")) {
        //                        return resource;
        //                    } else {
        //                        return null;
        //                    }
        //                }, new String[] { "META-INF/faker-default-dbtpc/" });
    }

    /** 解析 tpcConfig 配置 */
    private List<TypeProcessConfSet> parseTypeProcessConf(DataSourceType dbType, List<URI> uriList) throws IOException {
        List<TypeProcessConfSet> allTpcConf = new ArrayList<>();
        for (URI uri : uriList) {
            try {
                InputStream tpcStream = new AutoCloseInputStream(uri.toURL().openStream());
                TypeProcessConfSet dslConf = TypeProcessConfSet.parserTypeProcessConf(tpcStream, StandardCharsets.UTF_8);
                dslConf.setSource(uri);

                for (DataSourceType defDbType : dslConf.getDbTypes()) {
                    if (defDbType == dbType) {
                        allTpcConf.add(dslConf);
                    }
                }
            } catch (DslException e) {
                logger.error("passer '" + uri.toURL() + "' failed. " + e.getMessage());
                throw e;
            }
        }

        // sort tpcConf by priority
        allTpcConf.sort((o1, o2) -> {
            ValueModel o1Priority = (ValueModel) o1.getDefConfig("priority");
            ValueModel o2Priority = (ValueModel) o2.getDefConfig("priority");
            int o1Int = o1Priority == null ? 0 : (int) ConverterUtils.convert(Integer.TYPE, o1Priority.recover(globalVariables));
            int o2Int = o2Priority == null ? 0 : (int) ConverterUtils.convert(Integer.TYPE, o2Priority.recover(globalVariables));
            return -Integer.compare(o1Int, o2Int);
        });

        return allTpcConf;
    }

    /** 选择一个 tpcConfig 配置 */
    protected TypeProcessConfSet chooseTypeProcessConf(FakerEngineConfig config, List<TypeProcessConfSet> allTpcConf) throws IOException {
        boolean isCustom = StringUtils.isNotBlank(config.getCustomTpcConf());
        if (isCustom) {
            if (!allTpcConf.isEmpty()) {
                return allTpcConf.get(0);
            } else {
                throw new IOException("custom tpcConf '" + fakerConfig.getCustomTpcConf() + "' is exist, but database type does not match [" + dsType + "].");
            }
        }

        TypeProcessConfSet useConfSet = null;
        for (TypeProcessConfSet confSet : allTpcConf) {
            DataModel policyValue = confSet.getDefConfig("policy");
            Object policyName = policyValue == null ? null : policyValue.recover(globalVariables);
            if (policyName == null) {
                continue;
            }

            if (StringUtils.equalsIgnoreCase(policyName.toString(), config.getPolicy())) {
                useConfSet = confSet;
                break;
            }
        }

        if (useConfSet == null) {
            for (TypeProcessConfSet confSet : allTpcConf) {
                DataModel defaultValue = confSet.getDefConfig("default");
                Object isDefault = defaultValue == null ? null : defaultValue.recover(globalVariables);
                if (isDefault == null) {
                    continue;
                }

                if ((boolean) ConverterUtils.convert(Boolean.TYPE, isDefault)) {
                    useConfSet = confSet;
                    break;
                }
            }
        }
        return useConfSet;
    }

    @Override
    public TypeProcessor createSeedFactory(RdbColumn rdbColumn, SettingNode columnConfig) throws ReflectiveOperationException {
        FieldType colType = rdbColumn.getSqlType();
        String colTypeStr = colType.getCodeKey();

        // need throw
        if (this.colTypeThrow.containsKey(colTypeStr)) {
            String throwMessage = this.colTypeThrow.get(colTypeStr);
            throw new UnsupportedOperationException("unsupported columnName " + rdbColumn.getName()//
                                                    + ", columnType '" + colTypeStr + "', msg is " + throwMessage);
        }

        // use default
        if (!this.colTypeConfig.containsKey(colTypeStr)) {
            return this.defaultSeedFactory(rdbColumn);
        }

        Map<String, Object> variables = new HashMap<>(this.globalVariables);
        BeanUtils.copyProperties(variables, rdbColumn);

        // create and config
        Map<String, List<TypeProcessConf>> typeConfMap = this.colTypeConfig.get(colTypeStr);
        SeedFactory<? extends SeedConfig> seedFactory = this.createSeedFactory(colTypeStr, typeConfMap, variables);
        SeedConfig seedConfig = seedFactory.newConfig();
        TypeProcessor typeProcessor = createTypeProcessor(colTypeStr, rdbColumn, columnConfig, typeConfMap, seedFactory, seedConfig, variables);

        this.applyConfigSet(rdbColumn, columnConfig, seedConfig, typeProcessor, typeConfMap, variables);
        return typeProcessor;
    }

    //todo
    @Override
    public void fillSeedConfig(RdbColumn rdbColumn, SettingNode columnConfig, TypeProcessor typeProcessor) throws ReflectiveOperationException {
        FieldType colType = rdbColumn.getSqlType();
        String colTypeStr = colType.getCodeKey();

        // need throw
        if (this.colTypeThrow.containsKey(colTypeStr)) {
            String throwMessage = this.colTypeThrow.get(colTypeStr);
            throw new UnsupportedOperationException("unsupported columnName " + rdbColumn.getName()//
                                                    + ", columnType '" + colTypeStr + "', msg is " + throwMessage);
        }

        Map<String, Object> variables = new HashMap<>(this.globalVariables);
        BeanUtils.copyProperties(variables, rdbColumn);

        // create and config
        Map<String, List<TypeProcessConf>> typeConfMap = this.colTypeConfig.get(colTypeStr);

        List<TypeProcessConf> allConf = new ArrayList<>();
        for (Map.Entry<String, List<TypeProcessConf>> entry : typeConfMap.entrySet()) {
            if (!innerParameter.contains(entry.getKey())) {
                allConf.addAll(entry.getValue());
            }
        }
        SeedConfig seedConfig = typeProcessor.getSeedConfig();
        for (TypeProcessConf conf : allConf) {

            String columnName = rdbColumn.getName();
            String confName = conf.getConfName();
            if (seedConfig.getConfigMap().containsKey(confName)) {
                continue;
            }
            Object parameter = conf.recover(variables, "recover column [" + columnName + "]");
            variables.put("@@" + confName, parameter);

            // customize
            ParameterProcessor processor = ParameterRegistry.DEFAULT.findByName(confName, seedConfig.getClass());
            if (processor != null) {
                processor.processor(this.fakerConfig, rdbColumn, columnConfig, seedConfig, typeProcessor, conf.isUseAppend(), parameter);
                continue;
            }

            boolean write = BeanUtils.writeProperty(seedConfig, confName, parameter);
            if (!write) {
                logger.warn("column '" + columnName + "' applyConfig '" + confName + "' failed.");
            }
        }
    }

    @Override
    public SeedType findSeedType(RdbColumn rdbColumn) {
        FieldType colType = rdbColumn.getSqlType();
        String colTypeStr = colType.getCodeKey();

        Map<String, List<TypeProcessConf>> typeConfMap = this.colTypeConfig.get(colTypeStr);
        if (typeConfMap == null) {
            return null;
        }
        TypeProcessConf seedTypeConf = findOne("seedType", typeConfMap);
        Map<String, Object> variables = new HashMap<>(this.globalVariables);
        BeanUtils.copyProperties(variables, rdbColumn);

        if (seedTypeConf == null) {
            throw new IllegalArgumentException("columnType '" + colType + "' missing parameter seedType.");
        }

        Object seedType = seedTypeConf.recover(variables, "recover colType [" + colType + "]");
        if (seedType == null || StringUtils.isBlank(seedType.toString())) {
            throw new IllegalArgumentException("columnType '" + colType + "', the seedType parameter is incorrect.");
        }
        String seedTypeStr = seedType.toString();
        SeedType seedTypeEnum = SeedType.valueOfCode(seedTypeStr);
        if (seedTypeEnum == null) {
            throw new IllegalArgumentException("columnType '" + colType + "', the seedType parameter is incorrect.");
        }
        return seedTypeEnum;
    }

    protected TypeProcessor defaultSeedFactory(RdbColumn rdbColumn) {
        Integer jdbcType = rdbColumn.getSqlType().getJdbcType();
        if (jdbcType == null) {
            jdbcType = Types.OTHER;
        }

        switch (jdbcType) {
            case Types.BIT:
            case Types.BOOLEAN: {
                BooleanSeedFactory seedFactory = new BooleanSeedFactory();
                BooleanSeedConfig seedConfig = seedFactory.newConfig();
                return new TypeProcessor(seedFactory, seedConfig, jdbcType);
            }
            case Types.TINYINT:
            case Types.SMALLINT: {
                NumberSeedFactory seedFactory = new NumberSeedFactory();
                NumberSeedConfig seedConfig = seedFactory.newConfig();
                seedConfig.setNumberType(NumberType.Integer);
                seedConfig.addMinMax(new BigDecimal("0"), new BigDecimal("100"));
                return new TypeProcessor(seedFactory, seedConfig, Types.INTEGER);
            }
            case Types.INTEGER: {
                NumberSeedFactory seedFactory = new NumberSeedFactory();
                NumberSeedConfig seedConfig = seedFactory.newConfig();
                seedConfig.setNumberType(NumberType.Integer);
                seedConfig.addMinMax(new BigDecimal("0"), new BigDecimal("99999999"));
                return new TypeProcessor(seedFactory, seedConfig, Types.INTEGER);
            }
            case Types.BIGINT: {
                NumberSeedFactory seedFactory = new NumberSeedFactory();
                NumberSeedConfig seedConfig = seedFactory.newConfig();
                seedConfig.setNumberType(NumberType.Long);
                seedConfig.addMinMax(new BigDecimal("0"), new BigDecimal("9999999999"));
                return new TypeProcessor(seedFactory, seedConfig, Types.BIGINT);
            }
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL: {
                NumberSeedFactory seedFactory = new NumberSeedFactory();
                NumberSeedConfig seedConfig = seedFactory.newConfig();
                seedConfig.setNumberType(NumberType.Decimal);
                seedConfig.addMinMax(new BigDecimal("0.0"), new BigDecimal("9999999.999"));
                seedConfig.setScale(Math.min(rdbColumn.getNumericScale(), 3));
                seedConfig.setAbs(true);
                return new TypeProcessor(seedFactory, seedConfig, Types.DECIMAL);
            }
            case Types.CHAR:
            case Types.NCHAR:
            case Types.VARCHAR:
            case Types.NVARCHAR:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.NCLOB: {
                StringSeedFactory seedFactory = new StringSeedFactory();
                StringSeedConfig seedConfig = seedFactory.newConfig();
                seedConfig.setCharacterSet(new HashSet<>(Collections.singletonList(CharacterSet.LETTER_NUMBER)));
                seedConfig.setMinLength(1);
                Long charLength = rdbColumn.getCharLength();
                if (charLength == null) {
                    seedConfig.setMaxLength(10);
                } else {
                    seedConfig.setMaxLength((int) Math.min(charLength, 250));
                }
                return new TypeProcessor(seedFactory, seedConfig, jdbcType);
            }
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB: {
                BytesSeedFactory seedFactory = new BytesSeedFactory();
                BytesSeedConfig seedConfig = seedFactory.newConfig();
                return new TypeProcessor(seedFactory, seedConfig, jdbcType);
            }
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.TIME_WITH_TIMEZONE:
            case Types.TIMESTAMP_WITH_TIMEZONE: {
                DateSeedFactory seedFactory = new DateSeedFactory();
                DateSeedConfig seedConfig = seedFactory.newConfig();
                seedConfig.setDateType(DateType.JavaDate);
                seedConfig.setGenType(GenType.Random);
                seedConfig.setPrecision(3);
                seedConfig.setRangeForm("2000-01-01 00:00:00.000");
                seedConfig.setRangeTo("2030-12-31 23:59:59.999");
                return new TypeProcessor(seedFactory, seedConfig, jdbcType);
            }
            case Types.SQLXML:
            case Types.STRUCT:
            case Types.ARRAY:
            case Types.DATALINK:
            case Types.NULL:
            case Types.JAVA_OBJECT:
            case Types.DISTINCT:
            case Types.REF:
            case Types.ROWID:
            case Types.REF_CURSOR:
            case Types.OTHER:
            default:
                throw new UnsupportedOperationException("unsupported columnName " + rdbColumn.getName()//
                                                        + ", sqlType '" + rdbColumn.getSqlType()//
                                                        + "' and jdbcType '" + jdbcType + "'");
        }
    }

    protected SeedFactory<? extends SeedConfig> createSeedFactory(String colType, Map<String, List<TypeProcessConf>> typeConfMap, //
                                                                  Map<String, Object> variables) throws ReflectiveOperationException {
        TypeProcessConf seedTypeConf = findOne("seedType", typeConfMap);
        if (seedTypeConf == null) {
            throw new IllegalArgumentException("columnType '" + colType + "' missing parameter seedType.");
        }

        Object seedType = seedTypeConf.recover(variables, "recover colType [" + colType + "]");
        if (seedType == null || StringUtils.isBlank(seedType.toString())) {
            throw new IllegalArgumentException("columnType '" + colType + "', the seedType parameter is incorrect.");
        }

        String seedTypeStr = seedType.toString();
        SeedType seedTypeEnum = SeedType.valueOfCode(seedTypeStr);
        if (seedTypeEnum != null) {
            return seedTypeEnum.newFactory();
        }

        Class<?> seedFactoryType = this.fakerConfig.getClassLoader().loadClass(seedTypeStr);
        return (SeedFactory) seedFactoryType.newInstance();
    }

    protected TypeProcessor createTypeProcessor(String colType, RdbColumn colMeta, SettingNode colSetting, Map<String, List<TypeProcessConf>> typeConfMap, //
                                                SeedFactory<? extends SeedConfig> seedFactory, SeedConfig seedConfig,
                                                Map<String, Object> variables) throws ReflectiveOperationException {
        TypeProcessConf jdbcTypeConf = findOne("jdbcType", typeConfMap);
        if (jdbcTypeConf == null) {
            throw new IllegalArgumentException("columnType '" + colType + "' missing parameter jdbcType.");
        }

        Object jdbcType = jdbcTypeConf.recover(variables, "recover colType [" + colType + "]");
        if (jdbcType == null || StringUtils.isBlank(jdbcType.toString())) {
            throw new IllegalArgumentException("columnType '" + colType + "', the jdbcType parameter is incorrect.");
        }

        int jdbcTypeInt = Types.OTHER;
        for (JDBCType jdbcTypeEnum : JDBCType.values()) {
            if (StringUtils.equalsIgnoreCase(jdbcTypeEnum.name(), jdbcType.toString())) {
                jdbcTypeInt = jdbcTypeEnum.getVendorTypeNumber();
                break;
            }
        }

        //
        TypeProcessConf arrayDimensionConf = findOne("arrayDimension", typeConfMap);
        Object arrayDimension = arrayDimensionConf == null ? 0 : arrayDimensionConf.recover(variables, "recover colType [" + colType + "]");
        int arrayDimensionInt = (int) ConverterUtils.convert(Integer.TYPE, arrayDimension);
        if (arrayDimensionInt == 0) {
            return new TypeProcessor(seedFactory, seedConfig, jdbcTypeInt);
        } else {
            return this.createArrayTypeProcessor(arrayDimensionInt, colType, colMeta, colSetting, typeConfMap, seedFactory, seedConfig, variables);
        }
    }

    // maybe is array
    protected TypeProcessor createArrayTypeProcessor(int arrayDimension, String colType, RdbColumn colMeta, SettingNode colSetting, Map<String, List<TypeProcessConf>> typeConfMap, //
                                                     SeedFactory<? extends SeedConfig> seedFactory, SeedConfig seedConfig,
                                                     Map<String, Object> variables) throws ReflectiveOperationException {
        if (arrayDimension > 1) {
            throw new UnsupportedOperationException("colType is " + colType + ", multi-dimensional arrays are not supported.");
        }

        // create array
        TypeProcessConf arrayMinSizeConf = findOne("arrayMinSize", typeConfMap);
        TypeProcessConf arrayMaxSizeConf = findOne("arrayMaxSize", typeConfMap);
        TypeProcessConf arrayTypeHandlerConf = findOne("arrayTypeHandler", typeConfMap);

        Object arrayMinSize = arrayMinSizeConf == null ? 0 : arrayMinSizeConf.recover(variables, "recover colType [" + colType + "]");
        Object arrayMaxSize = arrayMaxSizeConf == null ? 10 : arrayMaxSizeConf.recover(variables, "recover colType [" + colType + "]");
        Object arrayTypeHandler = arrayTypeHandlerConf == null ? null : arrayTypeHandlerConf.recover(variables, "recover colType [" + colType + "]");

        int arrayMinSizeInt = (int) ConverterUtils.convert(Integer.TYPE, arrayMinSize);
        int arrayMaxSizeInt = (int) ConverterUtils.convert(Integer.TYPE, arrayMaxSize);

        ArraySeedFactory arrayFactory = new ArraySeedFactory(seedFactory);
        ArraySeedConfig arrayConfig = new ArraySeedConfig(seedConfig);
        arrayConfig.setMinSize(arrayMinSizeInt);
        arrayConfig.setMaxSize(arrayMaxSizeInt);

        if (arrayTypeHandler != null && StringUtils.isNotBlank(arrayTypeHandler.toString())) {
            ClassLoader classLoader = this.fakerConfig.getClassLoader();
            Class<?> typeHandlerType = classLoader.loadClass(arrayTypeHandler.toString());
            if (TypeHandler.class.isAssignableFrom(typeHandlerType)) {
                TypeHandler<?> instance = (TypeHandler<?>) typeHandlerType.newInstance();
                arrayConfig.setTypeHandler(instance);
            } else if (TypeHandlerFactory.class.isAssignableFrom(typeHandlerType)) {
                TypeHandlerFactory instance = (TypeHandlerFactory) typeHandlerType.newInstance();
                arrayConfig.setTypeHandler(instance.createTypeHandler(variables));
            } else {
                throw new UnsupportedOperationException("type '" + arrayTypeHandler + "' Unsupported.");
            }
        }

        return new TypeProcessor(arrayFactory, arrayConfig, Types.ARRAY);
    }

    protected static TypeProcessConf findOne(String name, Map<String, List<TypeProcessConf>> typeConfMap) {
        List<TypeProcessConf> tpcList = typeConfMap.get(name);
        if (CollectionUtils.isEmpty(tpcList)) {
            return null;
        } else {
            return tpcList.get(0);
        }
    }

    private void applyConfigSet(RdbColumn jdbcColumn, SettingNode columnConfig, SeedConfig seedConfig, //
                                TypeProcessor typeProcessor, Map<String, List<TypeProcessConf>> typeConfMap, Map<String, Object> variables) throws ReflectiveOperationException {

        List<TypeProcessConf> allConf = new ArrayList<>();
        for (Map.Entry<String, List<TypeProcessConf>> entry : typeConfMap.entrySet()) {
            if (!innerParameter.contains(entry.getKey())) {
                allConf.addAll(entry.getValue());
            }
        }

        for (TypeProcessConf conf : allConf) {
            applyConfig(jdbcColumn, columnConfig, seedConfig, typeProcessor, conf, variables);
        }
    }

    private void applyConfig(RdbColumn colMeta, SettingNode colSetting, SeedConfig seedConfig, //
                             TypeProcessor typeProcessor, TypeProcessConf confItem, Map<String, Object> variables) throws ReflectiveOperationException {
        String columnName = colMeta.getName();
        String confName = confItem.getConfName();
        Object parameter = confItem.recover(variables, "recover column [" + columnName + "]");

        variables.put("@@" + confName, parameter);

        // customize
        ParameterProcessor processor = ParameterRegistry.DEFAULT.findByName(confName, seedConfig.getClass());
        if (processor != null) {
            processor.processor(this.fakerConfig, colMeta, colSetting, seedConfig, typeProcessor, confItem.isUseAppend(), parameter);
            return;
        }

        boolean write = BeanUtils.writeProperty(seedConfig, confName, parameter);
        if (!write) {
            logger.warn("column '" + columnName + "' applyConfig '" + confName + "' failed.");
        }
    }

    @Override
    public TypeProcessConf findTypeProcessConf(String colType, String colName) {
        Map<String, List<TypeProcessConf>> typeConfMap = this.colTypeConfig.get(colType);
        if (typeConfMap == null) {
            return null;
        }
        return findOne(colName, typeConfMap);
    }
}
