/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.utils.setting;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.utils.BeanUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;
import com.clougence.utils.token.GenericTokenParser;
import com.clougence.utils.token.TokenHandler;

import com.clougence.utils.setting.data.TreeNode;

/**
 * Settings接口的抽象实现。
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2013-4-2
 */
public class BasicSettings extends AbstractSettings implements Settings {
    protected static Logger                logger  = LoggerFactory.getLogger(BasicSettings.class);
    private final    Map<String, TreeNode> dataMap = new ConcurrentHashMap<>();
    private final    Map<String, String>   envMap  = new ConcurrentHashMap<>();

    protected Map<String, TreeNode> allSettingValue() {
        return this.dataMap;
    }

    @Override
    protected Map<String, String> envMap() {
        return this.envMap;
    }

    /** 清空已经装载的所有数据 */
    protected void cleanData() {
        this.envMap().clear();
        this.allSettingValue().clear();
    }

    /** 加载预定义的环境变量 */
    protected void loadEnvironment() throws IOException {
        // 1st，System.getProperties()
        Properties prop = System.getProperties();
        for (Object propKey : prop.keySet()) {
            String k = propKey.toString();
            Object v = prop.get(propKey);
            if (v != null) {
                envMap().put(k.toUpperCase(), v.toString());
            }
        }

        // 2st，System.getenv()
        Map<String, String> envMap = System.getenv();
        for (String key : envMap.keySet()) {
            envMap().put(key.toUpperCase(), envMap.get(key));
        }
    }

    /** 加载预定义的配置信息 */
    protected void loadSettings() throws IOException {

    }

    /** replace environment variable in config */
    protected void updateSettings() {
        Collection<TreeNode> valueSet = this.allSettingValue().values();
        for (TreeNode sv : valueSet) {
            sv.update((dataNode, context) -> {
                String[] values = dataNode.getValues();
                for (int index = 0; index < values.length; index++) {
                    String oldVar = values[index];
                    String newVal = evalSetting(oldVar);
                    if (!StringUtils.equals(oldVar, newVal)) {
                        dataNode.replace(index, newVal);
                    }
                }
            }, this);
        }
    }

    @Override
    public void refresh() throws IOException {
        this.loadEnvironment();
        this.updateSettings();
    }

    public String evalSetting(String evalString) {
        if (StringUtils.isBlank(evalString)) {
            return "";
        }

        String newEvalString = new GenericTokenParser(new TokenHandler() {

            @Override
            public String handleToken(String content) {
                String varKey = content;
                String varDefault = "";
                int defaultIndexOf = content.indexOf(":");
                if (defaultIndexOf != -1) {
                    varDefault = content.substring(defaultIndexOf + 1);
                    varKey = content.substring(0, defaultIndexOf);
                }

                String envKey = "%" + varKey.toUpperCase() + "%";
                String var = evalEnv(envKey);
                if (StringUtils.isBlank(var) && StringUtils.isNotBlank(varDefault)) {
                    var = varDefault;
                }

                if (envKey.equalsIgnoreCase(var)) {
                    return envKey;
                } else {
                    return var;
                }
            }

            @Override
            public String getOpenToken() {
                return "${";
            }

            @Override
            public String getCloseToken() {
                return "}";
            }
        }).parse(evalString);

        if (!evalString.equalsIgnoreCase(newEvalString)) {
            logger.debug("replace settingValue '" + evalString + "' to '" + newEvalString + "'.");
        }
        return newEvalString;
    }

    private String evalEnv(String evalString) {
        if (StringUtils.isBlank(evalString)) {
            return "";
        }
        Pattern keyPattern = Pattern.compile("(?:%([\\w\\._-]+)%){1,1}");//  (?:%([\w\._-]+)%)
        Matcher keyM = keyPattern.matcher(evalString);
        Map<String, String> data = new HashMap<>();
        Map<String, String> envMap = this.envMap();
        while (keyM.find()) {
            String varKeyOri = keyM.group(1);
            String keyName = "%" + varKeyOri + "%";
            String var = envMap.get(varKeyOri.toUpperCase());
            if (var == null) {
                data.put(keyName, "");
            } else {
                data.put(keyName, evalEnv(var));
            }
        }
        String newEvalString = evalString;
        for (String key : data.keySet()) {
            newEvalString = newEvalString.replace(key, data.get(key));
        }
        logger.debug("evalString '" + evalString + "' eval to '" + newEvalString + "'.");
        return newEvalString;
    }

    /** 获取可用的命名空间 */
    public String[] getSettingArray() {
        Set<String> nsSet = this.allSettingValue().keySet();
        return nsSet.toArray(new String[0]);
    }

    protected boolean isNsView() {
        return false;
    }

    /** 获取指在某个特定命名空间下的Settings接口对象 */
    public final BasicSettings getSettings(final String namespace) {
        final Map<String, TreeNode> localData = Collections.unmodifiableMap(new HashMap<String, TreeNode>() {{
            put(namespace, allSettingValue().get(namespace));
        }});
        return new BasicSettings() {
            public Map<String, TreeNode> allSettingValue() {
                return localData;
            }

            protected boolean isNsView() {
                return true;
            }
        };
    }

    /** 将整个配置项的多个值全部删除 */
    public void removeSetting(String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("namespace or key is blank.");
        }
        String lowerCaseKey = key.trim();
        for (TreeNode treeNode : this.allSettingValue().values()) {
            treeNode.findClear(lowerCaseKey);
        }
    }

    /** 将整个配置项的多个值全部删除 */
    public void removeSetting(String key, String namespace) {
        if (StringUtils.isBlank(namespace) || StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("namespace or key is blank.");
        }
        TreeNode treeNode = this.allSettingValue().get(namespace);
        if (treeNode != null) {
            treeNode.findClear(key.trim());
        }
    }

    /** 设置参数，如果出现多个值，则会覆盖 */
    public void setSetting(String key, Object value, String namespace) {
        if (StringUtils.isBlank(namespace) || StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("namespace or key is blank.");
        }

        Map<String, TreeNode> treeNodeMap = this.allSettingValue();
        TreeNode dataNode = treeNodeMap.get(namespace);
        if (dataNode == null) {
            if (isNsView()) {
                throw new IllegalStateException("namespace view mode, cannot be added new namespace.");
            }
            dataNode = new TreeNode("", namespace);
            treeNodeMap.put(namespace, dataNode);
        }

        if (value instanceof SettingNode) {
            SettingNode node = (SettingNode) value;
            dataNode.setNode(key.trim(), node);
        } else {
            String valueStr = (value == null) ? null : value.toString();
            dataNode.setValue(key.trim(), valueStr);
        }
    }

    /** 添加参数，如果参数名称相同则追加一项 */
    public void addSetting(String key, Object value, String namespace) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key is blank.");
        }
        if (StringUtils.isBlank(namespace)) {
            namespace = Settings.DefaultNameSpace;
        }

        Map<String, TreeNode> treeNodeMap = this.allSettingValue();
        TreeNode dataNode = treeNodeMap.get(namespace);
        if (dataNode == null) {
            if (isNsView()) {
                throw new IllegalStateException("namespace view mode, cannot be added new namespace.");
            }
            dataNode = new TreeNode("", namespace);
            treeNodeMap.put(namespace, dataNode);
        }

        if (value instanceof SettingNode) {
            SettingNode node = (SettingNode) value;
            dataNode.addNode(key.trim(), node);
        } else {
            String valueStr = (value == null) ? null : value.toString();
            dataNode.addValue(key.trim(), valueStr);
        }
    }

    protected SettingNode[] findSettingValue(String name) {
        if (StringUtils.isBlank(name)) {
            return new SettingNode[0];
        }

        List<SettingNode> dataNodeList = new ArrayList<>();
        String lowerCase = name.trim();
        for (TreeNode dataNode : this.allSettingValue().values()) {
            List<SettingNode> treeNodeList = dataNode.findNodes(lowerCase);
            if (treeNodeList != null) {
                treeNodeList.forEach(settingNode -> {
                    if (!settingNode.isEmpty()) {
                        dataNodeList.add(settingNode);
                    }
                });
            }
        }
        if (dataNodeList.isEmpty()) {
            return new SettingNode[0];
        }
        // 排序 DefaultNameSpace 放到最后，同时 getToType 会取最后一条，相同命名空间的数据 add 最后一条要优先前面的。
        // 因此只能通过排序放到最后。否则无法满足当 不同命名空间空间下 DefaultNameSpace 有两条数据情况下 DefaultNameSpace 中最后一条优先的要求。
        dataNodeList.sort((o1, o2) -> {
            int o1Index = DefaultNameSpace.equalsIgnoreCase(o1.getSpace()) ? 0 : -1;
            int o2Index = DefaultNameSpace.equalsIgnoreCase(o2.getSpace()) ? 0 : -1;
            return Integer.compare(o1Index, o2Index);
        });
        return dataNodeList.toArray(new SettingNode[0]);
    }

    protected <T> T convertTo(Object oriObject, final Class<T> toType, final T defaultValue) {
        // .获取不到数据，使用默认值替代
        if (oriObject == null) {
            if (defaultValue != null) {
                return defaultValue;
            } else {
                return (T) BeanUtils.getDefaultValue(toType);
            }
        }
        // .如果数据就是目标需要的类型那么就直接返回
        if (toType.isInstance(oriObject)) {
            return (T) oriObject;
        }
        // .转换类型
        return (T) ConverterUtils.convert(toType, oriObject);
    }

    /** 解析全局配置参数，并且返回toType参数指定的类型。 */
    public final <T> T getToType(final String name, final Class<T> toType, final T defaultValue) {
        SettingNode[] settingVar = this.findSettingValue(name);
        if (settingVar == null || settingVar.length == 0) {
            return defaultValue;
        }
        if (settingVar.length == 0) {
            return null;
        }
        if (SettingNode.class == toType || TreeNode.class == toType) {
            return (T) settingVar[settingVar.length - 1];
        } else {
            return convertTo(settingVar[settingVar.length - 1].getValue(), toType, defaultValue);
        }
    }

    public <T> T[] getToTypeArray(final String name, final Class<T> toType, final T defaultValue) {
        SettingNode[] varArrays = this.findSettingValue(name);
        if (varArrays == null) {
            return (T[]) Array.newInstance(toType, 0);
        }
        if (SettingNode.class == toType || TreeNode.class == toType) {
            return (T[]) varArrays;
        }
        List<T> targetObjects = new ArrayList<>();
        for (SettingNode var : varArrays) {
            for (String item : var.getValues()) {
                T finalItem = convertTo(item, toType, defaultValue);
                targetObjects.add(finalItem);
            }
        }
        return targetObjects.toArray((T[]) Array.newInstance(toType, targetObjects.size()));
    }
}
