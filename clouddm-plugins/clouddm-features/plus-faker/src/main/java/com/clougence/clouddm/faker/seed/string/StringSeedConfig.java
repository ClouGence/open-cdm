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
package com.clougence.clouddm.faker.seed.string;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.type.TypeHandler;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.faker.types.TypeHandlerRegistryUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

/**
 * 字符串类型的 SeedConfig
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class StringSeedConfig extends SeedConfig {

    private static final Map<String, Characters> CHARACTERS_MAP = new LinkedCaseInsensitiveMap<>();
    private Set<Characters>                      characterSet;
    private int                                  minLength;
    private int                                  maxLength;
    private boolean                              allowEmpty;

    static {
        Field[] charsField = CharacterSet.class.getFields();
        for (Field field : charsField) {
            if (!Characters.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                field.setAccessible(true);
                Characters o = (Characters) field.get(null);
                CHARACTERS_MAP.put(field.getName(), o);
            } catch (Exception ignored) {

            }
        }
    }

    public final SeedType getSeedType() { return SeedType.String; }

    @Override
    protected TypeHandler<?> defaultTypeHandler() {
        return TypeHandlerRegistryUtils.getTypeHandler(String.class);
    }

    public Set<Characters> getCharacterSet() { return characterSet; }

    public void setCharacterSet(Set<Characters> characterSet) { this.characterSet = characterSet; }

    public int getMinLength() { return minLength; }

    public void setMinLength(int minLength) { this.minLength = minLength; }

    public int getMaxLength() { return maxLength; }

    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }

    public boolean isAllowEmpty() { return allowEmpty; }

    public void setAllowEmpty(boolean allowEmpty) { this.allowEmpty = allowEmpty; }

    public void addCharacter(String character) {
        Characters object = CHARACTERS_MAP.get(character);
        if (object != null) {
            this.addCharacter(object);
        }
    }

    public void addCharacter(Characters character) {
        if (this.characterSet == null) {
            this.characterSet = new HashSet<>();
        }
        if (!this.characterSet.contains(character)) {
            this.characterSet.add(character);
        }
    }
}
