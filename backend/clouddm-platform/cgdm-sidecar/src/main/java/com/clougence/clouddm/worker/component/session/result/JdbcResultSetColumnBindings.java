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
package com.clougence.clouddm.worker.component.session.result;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

final class JdbcResultSetColumnBindings {

    private static final Map<Method, Method> INDEX_METHOD_BY_LABEL_METHOD = indexMethodByLabelMethod();

    private final String[]    columnNames;
    private final int[]       columnIndexes;
    private final ResultSet[] resultSets;
    private boolean           bound;

    JdbcResultSetColumnBindings(String[] columnNames, int[] columnIndexes){
        this.columnNames = columnNames;
        this.columnIndexes = columnIndexes;
        this.resultSets = new ResultSet[columnNames.length];
    }

    void bind(ResultSet resultSet) throws SQLException {
        if (this.bound) {
            return;
        }
        for (int i = 0; i < this.columnNames.length; i++) {
            String columnName = this.columnNames[i];
            int columnIndex = this.columnIndexes[i];
            if (columnIndex <= 0 || resultSet.findColumn(columnName) == columnIndex) {
                this.resultSets[i] = resultSet;
                continue;
            }
            this.resultSets[i] = (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class },
                (proxy, method, args) -> invokeForColumn(resultSet, columnName, columnIndex, method, args));
        }
        this.bound = true;
    }

    ResultSet get(int index) {
        return this.resultSets[index];
    }

    private static Object invokeForColumn(ResultSet resultSet, String columnName, int columnIndex, Method method, Object[] args) throws Throwable {
        Method targetMethod = method;
        Object[] targetArgs = args;
        if (args != null && args.length > 0 && columnName.equals(args[0])) {
            Method indexMethod = INDEX_METHOD_BY_LABEL_METHOD.get(method);
            if (indexMethod != null) {
                targetMethod = indexMethod;
                targetArgs = args.clone();
                targetArgs[0] = columnIndex;
            }
        }
        try {
            return targetMethod.invoke(resultSet, targetArgs);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private static Map<Method, Method> indexMethodByLabelMethod() {
        Map<Method, Method> result = new HashMap<>();
        for (Method method : ResultSet.class.getMethods()) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0 || parameterTypes[0] != String.class) {
                continue;
            }
            parameterTypes = parameterTypes.clone();
            parameterTypes[0] = int.class;
            try {
                result.put(method, ResultSet.class.getMethod(method.getName(), parameterTypes));
            } catch (NoSuchMethodException ignored) {
                // ResultSet methods without an index overload keep the original column label.
            }
        }
        return Map.copyOf(result);
    }
}
