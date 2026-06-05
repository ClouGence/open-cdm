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
package com.clougence.clouddm.console.web.service.faker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import com.clougence.clouddm.console.web.model.fo.faker.FakerColumnFO;
import com.clougence.clouddm.console.web.model.fo.faker.FakerConfigFO;
import com.clougence.clouddm.console.web.service.faker.model.FakerColumnDTO;
import com.clougence.clouddm.console.web.service.faker.model.FakerConfigDTO;
import com.clougence.clouddm.console.web.service.faker.model.FakerTableDTO;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author olddream
 */
public class ConfigConvertYamlUtils {

    /**yaml template: clouddm-plugins/clouddm-faker/src/test/resources/example-config.yaml*/
    public static String foConvertYaml(FakerConfigFO fakerConfigFO, Map<UmiTypes, Object> param) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO);
        Yaml yaml = new Yaml(new YamlRepresenter(), dumperOptions);
        yaml.setName("config");
        FakerConfigDTO configDTO = FO2DTO(fakerConfigFO, param);
        HashMap<String, List<FakerTableDTO>> configMap = new HashMap<>();
        configMap.put("config", configDTO.getTable());
        return replaceName(yaml.dumpAsMap(configMap));
    }

    private static String replaceName(String yaml) {
        String name = "- !!" + FakerTableDTO.class.getName();
        if (yaml.contains(FakerTableDTO.class.getName())) {
            yaml = StringUtils.replace(yaml, name, "- table:");
        }
        StringBuilder sb = new StringBuilder();
        String[] split = yaml.split("\n");
        for (String s : split) {
            if (!s.equals("config:") && !s.equals("- table:")) {
                s = "     " + s + "\n";
            } else if (s.equals("- table:")) {
                s = " " + s + "\n";
            } else {
                s = s + "\n";
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private static FakerConfigDTO FO2DTO(FakerConfigFO fakerConfigFO, Map<UmiTypes, Object> param) {
        FakerConfigDTO configDTO = new FakerConfigDTO();
        List<FakerTableDTO> tables = fakerConfigFO.getTableConfigs().stream().map(item -> {
            FakerTableDTO dto = new FakerTableDTO();
            dto.setCatalog((String) param.get(UmiTypes.Catalog));
            dto.setSchema((String) param.get(UmiTypes.Schema));
            dto.setTable(item.getName());
            dto.setTotal(item.getTotal());
            dto.setUpdatePolitic(item.getUpdatePolitic());
            dto.setWherePolitic(item.getWherePolitic());
            dto.setInsertPolitic(item.getInsertPolitic());
            Map<String, FakerColumnDTO> columnMap = new HashMap<>();
            for (FakerColumnFO columnFO : item.getColumnConfigs()) {
                FakerColumnDTO columnDTO = fillFakerColumn(columnFO);
                columnMap.put(columnFO.getName(), columnDTO);
            }
            dto.setColumns(columnMap);
            return dto;
        }).collect(Collectors.toList());
        configDTO.setTable(tables);
        return configDTO;
    }

    @NotNull
    private static FakerColumnDTO fillFakerColumn(FakerColumnFO columnFO) {
        Map<String, Object> seedConfig = columnFO.getSeedConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        FakerColumnDTO columnDTO = objectMapper.convertValue(seedConfig, FakerColumnDTO.class);
        columnDTO.setSeedType(columnFO.getSeedType());
        columnDTO.setIgnoreColsDeleteWhere(columnFO.getIgnoreColsDeleteWhere());
        columnDTO.setIgnoreColsInsert(columnFO.getIgnoreColsInsert());
        columnDTO.setIgnoreColsUpdate(columnFO.getIgnoreColsUpdate());
        columnDTO.setIgnoreColsUpdateWhere(columnFO.getIgnoreColsUpdateWhere());
        if (seedConfig.containsKey("numberType")) {
            columnDTO.setNumberType((String) seedConfig.get("numberType"));
        }

        if (isNotBlank(seedConfig, "max") || isNotBlank(seedConfig, "min")) {
            StringBuilder sb = new StringBuilder();
            if (seedConfig.containsKey("min")) {
                sb.append(seedConfig.get("min"));
            } else {
                sb.append("0");
            }
            sb.append("-");
            if (seedConfig.containsKey("max")) {
                sb.append(seedConfig.get("max"));
            } else {
                sb.append("0");
            }
            columnDTO.setMinMax(sb.toString());
        }
        return columnDTO;
    }

    private static boolean isNotBlank(Map<String, Object> seedConfig, String key) {
        Object o = seedConfig.get(key);
        return o != null && StringUtils.isNotBlank(o.toString());
    }

    static class YamlRepresenter extends Representer {

        public YamlRepresenter(){
            super(new DumperOptions());
        }

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
            if (propertyValue == null) {
                return null;
            }
            return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        }
    }
}
