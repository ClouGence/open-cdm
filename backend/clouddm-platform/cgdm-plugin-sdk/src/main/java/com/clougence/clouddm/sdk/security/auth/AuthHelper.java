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
package com.clougence.clouddm.sdk.security.auth;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.BeanUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.FieldUtils;
import com.clougence.utils.StringUtils;

public final class AuthHelper {

    public static List<AuthInfo> lookUpLabel(Class<?> defType) {
        return AuthHelper.lookUp(defType).stream().filter(a -> {
            return a.getAuthType() == AuthInfoType.Auth;
        }).collect(Collectors.toList());
    }

    public static List<AuthInfo> lookUpLabel(Class<?> defType, List<String> includeTag) {
        List<AuthInfo> infos = AuthHelper.lookUp(defType);
        List<AuthInfo> result = new ArrayList<>();
        for (AuthInfo info : infos) {
            if (info.getAuthType() != AuthInfoType.Auth) {
                continue;
            }

            if (!info.getTag().isEmpty() && CollectionUtils.isNotEmpty(includeTag)) {
                if (!CollectionUtils.containsAny(info.getTag(), includeTag)) {
                    continue;
                }
            }

            result.add(info);
        }

        return result;
    }

    private static final Map<AuthKind, List<AuthElementType>> ALL = new HashMap<>();
    static {
        for (AuthKind kind : AuthKind.values()) {
            ALL.put(kind, Arrays.asList(AuthElementType.values()));
        }
    }

    public static List<AuthInfo> lookUp(Class<?> defType, DataSourceType specialDs) {
        List<AuthInfo> data = lookUp(defType);
        if (specialDs != null) {
            data.forEach(a -> {
                a.setScope(AuthInfoScope.DataSource);
                a.setScopeDs(specialDs);
            });
        }
        return data;
    }

    public static List<AuthInfo> lookUp(Class<?> defType) {
        List<String> forProduct = Collections.emptyList();

        List<Field> allFields = BeanUtils.findALLFields(defType);
        List<AuthInfo> result = new ArrayList<>();

        for (Field field : allFields) {

            if (field.isAnnotationPresent(AuthCategory.class)) {
                AuthCategory category = field.getAnnotation(AuthCategory.class);

                AuthInfo info = new AuthInfo();
                info.setAuthType(AuthInfoType.Category);
                info.setDefField(field.getName());
                info.setKey(readStaticField(field));
                info.setKeyI18n(category.i18nKey());
                info.setOrder(category.order());
                info.setForProduct(forProduct);

                info.setParent(category.parent());
                info.setHidden(category.hidden());

                info.setScope(AuthInfoScope.Default);

                result.add(info);
            } else if (field.isAnnotationPresent(AuthLabel.class)) {
                AuthLabel label = field.getAnnotation(AuthLabel.class);
                AuthKindCondition[] conditions = field.getAnnotationsByType(AuthKindCondition.class);

                AuthInfo info = new AuthInfo();
                info.setDefField(field.getName());
                info.setKey(readStaticField(field));
                info.setKeyI18n(label.i18nKey());
                info.setOrder(label.order());
                info.setForProduct(forProduct);
                info.setAuthType(AuthInfoType.Auth);
                info.setKinds(Arrays.asList(label.kind()));
                info.setUsedOfRole(label.usedOfRole());

                info.setCategory(label.category());
                info.setMustSelectAndReadOnly(label.isMustSelectAndReadOnly());
                info.setInclude(Arrays.asList(label.include()));
                info.setTag(Arrays.asList(label.tag()));

                info.setCondition(new HashMap<>(ALL));
                for (AuthKindCondition condition : conditions) {
                    info.getCondition().put(condition.kind(), Arrays.asList(condition.condition()));
                }

                info.setScope(label.global() ? AuthInfoScope.Public : AuthInfoScope.Default);

                result.add(info);
            }
        }
        return result;
    }

    /**
     * Read a static Field.
     * @param field to read
     * @return the field value
     * @throws IllegalArgumentException if the field is null or not static
     */
    private static String readStaticField(final Field field) {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("The field '" + field.getName() + "' is not static");
        }
        String key;
        try {
            key = (String) FieldUtils.readField(field, (Object) null, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("The field '" + field.getName() + "' value is blank.");
        }
        return key;
    }
}
