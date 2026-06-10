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
 * Basic implementation of the Settings interface.
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

    /** Clear all loaded data */
    protected void cleanData() {
        this.envMap().clear();
        this.allSettingValue().clear();
    }

    /** Load predefined environment variables */
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

    /** Load predefined configuration information */
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

    /** Get Available Naming Space */
    public String[] getSettingArray() {
        Set<String> nsSet = this.allSettingValue().keySet();
        return nsSet.toArray(new String[0]);
    }

    protected boolean isNsView() {
        return false;
    }

    /** Getting a Settings interface object in a given named space */
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

    /** Remove multiple values of the entire configuration item */
    public void removeSetting(String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("namespace or key is blank.");
        }
        String lowerCaseKey = key.trim();
        for (TreeNode treeNode : this.allSettingValue().values()) {
            treeNode.findClear(lowerCaseKey);
        }
    }

    /** Remove multiple values of the entire configuration item */
    public void removeSetting(String key, String namespace) {
        if (StringUtils.isBlank(namespace) || StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("namespace or key is blank.");
        }
        TreeNode treeNode = this.allSettingValue().get(namespace);
        if (treeNode != null) {
            treeNode.findClear(key.trim());
        }
    }

    /** Set parameters, overwrite if multiple values appear */
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

    /** Add parameters, and add an item if the parameter name is the same */
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
        // Sort DelfaultNameSpace to last, and get ToType to take the last one, the same namespace data add.
        // So it can only be sorted to the last. Otherwise it will not be possible to meet the requirements of the last priority in DefaultNameSpace when there are two data in different namespaces.
        dataNodeList.sort((o1, o2) -> {
            int o1Index = DefaultNameSpace.equalsIgnoreCase(o1.getSpace()) ? 0 : -1;
            int o2Index = DefaultNameSpace.equalsIgnoreCase(o2.getSpace()) ? 0 : -1;
            return Integer.compare(o1Index, o2Index);
        });
        return dataNodeList.toArray(new SettingNode[0]);
    }

    protected <T> T convertTo(Object oriObject, final Class<T> toType, final T defaultValue) {
        // ... data not available, replace with default values
        if (oriObject == null) {
            if (defaultValue != null) {
                return defaultValue;
            } else {
                return (T) BeanUtils.getDefaultValue(toType);
            }
        }
        // If data are the type of target that needs to be returned directly
        if (toType.isInstance(oriObject)) {
            return (T) oriObject;
        }
        // Type of conversion
        return (T) ConverterUtils.convert(toType, oriObject);
    }

    /** Parses global configuration parameters and returns the type specified by the type parameter. */
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
