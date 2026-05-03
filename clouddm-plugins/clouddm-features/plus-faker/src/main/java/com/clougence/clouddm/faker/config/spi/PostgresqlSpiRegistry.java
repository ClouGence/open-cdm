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
package com.clougence.clouddm.faker.config.spi;

import com.clougence.clouddm.faker.config.dsl.DslFunction;
import com.clougence.clouddm.faker.config.dsl.DslFunctionLoopUp;
import com.clougence.clouddm.faker.config.dsl.DslFunctionRegistry;
import com.clougence.clouddm.faker.config.parameter.ParameterProcessorLookUp;
import com.clougence.clouddm.faker.config.parameter.ParameterRegistry;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;

/**
 * DslFunctionLoopUp, ParameterProcessorLookUp 的 Postgresql 专属扩展。
 * @version : 2023-02-14
 * @author 赵永春 (zyc@hasor.net)
 */
public class PostgresqlSpiRegistry implements DslFunctionLoopUp, ParameterProcessorLookUp {

    @Override
    public void loopUp(DslFunctionRegistry registry) {
        registry.register("pgArrayDimension", dslFuncPgArrayDimension());
        registry.register("pgFmtType", dslFuncPgFmtType());
        registry.register("pgToGeoType", dslFuncPgToGeoType());
        registry.register("pgNumericValid", dslFuncPgNumericValid());
        registry.register("pgColIsArray", dslFuncPgColIsArray());
        registry.register("pgElementType", dslFuncPgElementType());
    }

    // 'pgArrayDimension()' DslFunction
    private static DslFunction dslFuncPgArrayDimension() {
        return (args, context) -> evalDimCount(String.valueOf(context.get("sqlType")));
    }

    // 'pgFmtType("?", "numeric", "money")' DslFunction
    private static DslFunction dslFuncPgFmtType() {
        return (args, context) -> {
            if (args == null || args.size() < 2) {
                throw new IllegalArgumentException("min need 2 args.");
            }

            String pad = "";
            int dimCount = evalDimCount(String.valueOf(context.get("sqlType")));
            if (dimCount > 0) {
                pad = StringUtils.repeat("[]", dimCount);
            }

            String fmtType = "";
            for (int i = 1; i < args.size(); i++) {
                if (StringUtils.equalsIgnoreCase("bit", String.valueOf(args.get(i)))) {
                    int columnSize = (Integer) ConverterUtils.convert(Integer.TYPE, context.get("numericPrecision"));
                    fmtType += ("::" + args.get(i) + "(" + columnSize + ")" + pad);
                } else if (StringUtils.equalsIgnoreCase("bit varying", String.valueOf(args.get(i)))) {
                    fmtType += ("::" + args.get(i) + "(" + context.get("@@maxLength") + ")" + pad);
                } else {
                    fmtType += ("::" + args.get(i) + pad);
                }
            }
            return args.get(0) + fmtType;
        };
    }

    // 'pgToGeoType(${columnType})' DslFunction
    private static DslFunction dslFuncPgToGeoType() {
        return (args, context) -> {
            if (args == null || args.size() < 1) {
                throw new IllegalArgumentException("min need 1 args.");
            }

            String columnType = String.valueOf(args.get(0));
            while (columnType.length() > 0 && columnType.charAt(0) == '_') {
                columnType = columnType.substring(1);
            }

            if (StringUtils.equalsIgnoreCase(columnType, "point")) {
                return "Point";
            } else if (StringUtils.equalsIgnoreCase(columnType, "line")) {
                return "Line";
            } else if (StringUtils.equalsIgnoreCase(columnType, "lseg")) {
                return "Lseg";
            } else if (StringUtils.equalsIgnoreCase(columnType, "box")) {
                return "Box";
            } else if (StringUtils.equalsIgnoreCase(columnType, "circle")) {
                return "Circle";
            } else if (StringUtils.equalsIgnoreCase(columnType, "path")) {
                return "Path";
            } else if (StringUtils.equalsIgnoreCase(columnType, "polygon")) {
                return "Polygon";
            } else if (StringUtils.equalsIgnoreCase(columnType, "geometry")) {
                return "MultiPolygon";
            } else {
                throw new IllegalArgumentException("");
            }
        };
    }

    // 'pgNumericValid()' DslFunction
    private static DslFunction dslFuncPgNumericValid() {
        return (args, context) -> {
            Object columnSize = context.get("numericPrecision");
            Object decimalDigits = context.get("numericScale");
            int dimCount = evalDimCount(String.valueOf(context.get("sqlType")));

            int columnSizeInt = columnSize == null ? 0 : (Integer) ConverterUtils.convert(Integer.TYPE, columnSize);
            Integer decimalDigitsInt = decimalDigits == null ? null : (Integer) ConverterUtils.convert(Integer.TYPE, decimalDigits);

            if (columnSizeInt == 0 && decimalDigitsInt == null) {
                return false; // 0 的情况下 Faker 不能正常工作，PG 允许值最大 1000 位，这里取一个较小值
            } else if (dimCount > 0 && columnSizeInt > 300) {
                return false;
            } else {
                return true;
            }
        };
    }

    // 'pgColIsArray()' DslFunction
    private static DslFunction dslFuncPgColIsArray() {
        return (args, context) -> evalDimCount(String.valueOf(context.get("sqlType"))) > 0;
    }

    // 'pgElementType(${sqlType})' DslFunction
    private static DslFunction dslFuncPgElementType() {
        return (args, context) -> {
            if (args == null || args.size() < 1) {
                throw new IllegalArgumentException("min need 1 args.");
            }
            if (args.get(0) == null) {
                return null;
            }

            String columnType = String.valueOf(args.get(0));
            while (columnType.length() > 0 && columnType.charAt(0) == '_') {
                columnType = columnType.substring(1);
            }
            return columnType;
        };
    }

    @Override
    public void loopUp(ParameterRegistry registry) {

    }

    private static int evalDimCount(String pgColumnType) {
        if (pgColumnType == null) {
            return 0;
        }

        String columnType = pgColumnType;
        int dimCount = 0;
        while (columnType.length() > 0 && columnType.charAt(0) == '_') {
            columnType = columnType.substring(1);
            dimCount++;
        }
        return dimCount;
    }
}
