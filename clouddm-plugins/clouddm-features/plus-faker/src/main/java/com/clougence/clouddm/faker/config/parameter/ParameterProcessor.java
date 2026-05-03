package com.clougence.clouddm.faker.config.parameter;

import com.clougence.clouddm.faker.config.FakerEngineConfig;
import com.clougence.clouddm.faker.generator.TypeProcessor;
import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.schema.umi.special.rdb.RdbColumn;

import com.clougence.utils.setting.SettingNode;

/**
 * 自定义参数配置方式
 * @version : 2023-02-14
 * @author 赵永春 (zyc@hasor.net)
 */
public interface ParameterProcessor {

    void processor(FakerEngineConfig fakerConfig, RdbColumn colMeta, SettingNode colSetting, //
                   SeedConfig seedConfig, TypeProcessor typeProcessor, boolean isAppend, Object parameter) throws ReflectiveOperationException;
}
