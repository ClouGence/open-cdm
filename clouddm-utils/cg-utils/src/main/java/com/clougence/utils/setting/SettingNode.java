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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import com.clougence.utils.setting.data.UpdateValue;

/**
 * 属性节点。
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-02-01
 */
public interface SettingNode {
    /** @return 获取父节点 */
    SettingNode getParent();

    String getSpace();

    boolean isDefault();

    boolean isEmpty();

    /** @return 获取节点名称 */
    String getName();

    /** @return 获取节点完整的名称 */
    String getFullName();

    /** @return 获取Xml节点文本值 */
    String getValue();

    /** @return 获取Xml节点Xml文本值 */
    String[] getValues();

    void setValue(String value);

    void addValue(String value);

    void clearValue();

    /** @return 获取Xml节点文本值 */
    String getSubValue(String elementName);

    /** @return 获取Xml节点文本值 */
    String[] getSubValues(String elementName);

    /** @return 获取属性集合 */
    SettingNode getSubNode(String elementName);

    SettingNode[] getSubNodes(String elementName);

    SettingNode[] getSubNodes(String elementName, Predicate<SettingNode> predicate);

    String[] getSubKeys();

    /** @return 获取属性集合 */
    SettingNode[] getSubNodes();

    SettingNode newNode(String elementName);

    SettingNode newLast(String configKey);

    void clearSub();

    void clearSub(String elementName);

    SettingNode addSubNode(SettingNode target);

    SettingNode addSubNode(String elementName, SettingNode target);

    void setNode(String configKey, SettingNode target);

    void addNode(String configKey, SettingNode target);

    void setValue(String configKey, String value);

    void addValue(String configKey, String value);

    SettingNode findNode(String configKey);

    SettingNode findOrNew(String configKey);

    List<SettingNode> findNodes(String configKey);

    String findValue(String configKey);

    String[] findValues(String configKey);

    void visitNodes(Consumer<SettingNode> consumer);

    void clear();

    void findClear(String configKey);

    void update(UpdateValue updateValue, Settings context);

    Map<String, String> toMap();

    Map<String, List<String>> toMapList();

    String toXml();
}
