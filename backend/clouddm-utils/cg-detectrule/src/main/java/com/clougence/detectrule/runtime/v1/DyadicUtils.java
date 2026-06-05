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
package com.clougence.detectrule.runtime.v1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.clougence.detectrule.lang.*;
import com.clougence.detectrule.lang.reflect.Accessible;
import com.clougence.detectrule.lang.reflect.ReflectHelper;
import com.clougence.detectrule.lang.reflect.Type;
import com.clougence.detectrule.lang.reflect.TypeType;
import com.clougence.detectrule.parser.DetectRulesFeature;
import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.expr.DyadicExpression;
import com.clougence.detectrule.runtime.DetectRuleRuntimeError;
import com.clougence.detectrule.runtime.EngineOption;
import com.clougence.utils.StringUtils;

class DyadicUtils {

    private final Set<DetectRulesFeature> features = new HashSet<>();

    public DyadicUtils(EngineOption option){
        if (option.getFeatures() != null) {
            Collections.addAll(this.features, option.getFeatures());
        }
    }

    public boolean isBigInteger(BigDecimal decimalValue) {
        BigDecimal[] decimals = decimalValue.divideAndRemainder(BigDecimal.ONE);
        return decimals[1].equals(BigDecimal.ZERO);
    }

    private void checkRequiredNumber(DyadicExpression inst, LangObject firstObj, LangObject secondObj) {
        String symbol = inst.getSymbol().getValue();
        TypeType firstType = firstObj.getType();
        TypeType secondType = secondObj.getType();

        if (!(firstType == TypeType.Integer || firstType == TypeType.Float || firstType == TypeType.Decimal)) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Integer/Float/Decimal");
        }
        if (!(secondType == TypeType.Integer || secondType == TypeType.Float || secondType == TypeType.Decimal)) {
            throw new DetectRuleRuntimeError(inst.getSecondExpr(), "In dyadic '" + symbol + "'operation, the expression needs to be Integer/Float/Decimal");
        }
    }

    private void checkRequiredInteger(DyadicExpression inst, LangObject firstObj, LangObject secondObj) {
        String symbol = inst.getSymbol().getValue();
        TypeType firstType = firstObj.getType();
        TypeType secondType = secondObj.getType();

        if (!(firstType == TypeType.Integer || firstType == TypeType.Decimal)) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Integer/Decimal(BigInteger)");
        } else {
            if (firstType == TypeType.Decimal && !isBigInteger((BigDecimal) firstObj.unwrap())) {
                throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Decimal(BigInteger)");
            }
        }

        if (!(secondType == TypeType.Integer || secondType == TypeType.Decimal)) {
            throw new DetectRuleRuntimeError(inst.getSecondExpr(), "In dyadic '" + symbol + "'operation, the expression needs to be Integer/Decimal(BigInteger)");
        } else {
            if (secondType == TypeType.Decimal && !isBigInteger((BigDecimal) secondObj.unwrap())) {
                throw new DetectRuleRuntimeError(inst.getSecondExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Decimal(BigInteger)");
            }
        }
    }

    private TypeType numberImplicit(DyadicExpression inst, TypeType firstObj, TypeType secondObj) {
        switch (firstObj) {
            case Integer:
                switch (secondObj) {
                    case Integer:
                        return TypeType.Integer;
                    case Float:
                    case Decimal:
                        return TypeType.Decimal;
                    default:
                        break;
                }
            case Float:
                switch (secondObj) {
                    case Float:
                        return TypeType.Float;
                    case Integer:
                    case Decimal:
                        return TypeType.Decimal;
                    default:
                        break;
                }
            case Decimal:
                switch (secondObj) {
                    case Float:
                    case Integer:
                    case Decimal:
                        return TypeType.Decimal;
                    default:
                        break;
                }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "Unsupported type implicit conversions, use cast(...)");
    }

    private TypedExpr numberImplicitUpgrade(Expression inst, TypedExpr value, TypeType toType) {
        TypeType valueType = value.getType().getTypeType();
        Object valueData = value.getData().unwrap();
        switch (valueType) {
            case Integer:
                switch (toType) {
                    case Integer:
                        return value;
                    case Decimal:
                        Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                        ValueObject valData = new ValueObject(new BigDecimal((long) valueData), TypeType.Decimal);
                        return new TypedExpr(value.getLocation(), valData, valRef);
                    default:
                        break;
                }
            case Float:
                switch (toType) {
                    case Float:
                        return value;
                    case Decimal:
                        Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                        ValueObject valData = new ValueObject(BigDecimal.valueOf((Double) valueData), TypeType.Decimal);
                        return new TypedExpr(value.getLocation(), valData, valRef);
                    default:
                        break;
                }
            case Decimal:
                switch (toType) {
                    case Decimal:
                        return value;
                    default:
                        break;
                }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "Unsupported type implicit conversions, use cast(...)");
    }

    private boolean anyNull(LangObject firstObj, LangObject secondObj) {
        return firstObj.unwrap() == null || secondObj.unwrap() == null;
    }

    private boolean bothOfNull(LangObject firstObj, LangObject secondObj) {
        return firstObj.unwrap() == null && secondObj.unwrap() == null;
    }

    private boolean bothOfBoolean(Accessible firstObj, Accessible secondObj) {
        return firstObj.getTypeType() == TypeType.Boolean && secondObj.getTypeType() == TypeType.Boolean;
    }

    private boolean bothOfString(Accessible firstObj, Accessible secondObj) {
        return firstObj.getTypeType() == TypeType.String && secondObj.getTypeType() == TypeType.String;
    }

    private boolean onceString(Accessible firstObj, Accessible secondObj) {
        if (firstObj.getTypeType() == TypeType.String || secondObj.getTypeType() == TypeType.String) {
            return firstObj.getTypeType() != secondObj.getTypeType();
        } else {
            return false;
        }
    }

    // Arithmetic operations
    public TypedExpr plus(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            if (this.features.contains(DetectRulesFeature.AllowUndefinedVariableFeature)) {
                return secondObj;
            } else {
                throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
            }
        }
        if (secondObj.getData().unwrap() == null) {
            if (this.features.contains(DetectRulesFeature.AllowUndefinedVariableFeature)) {
                return firstObj;
            } else {
                throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
            }
        }

        if (onceString(firstObj.getType(), secondObj.getType())) {
            Object obj1 = firstObj.getData().unwrap();
            Object obj2 = secondObj.getData().unwrap();
            String str1 = obj1 == null ? "" : obj1.toString();
            String str2 = obj2 == null ? "" : obj2.toString();

            Type valRef = ReflectHelper.atomicType(TypeType.String, Boolean.TYPE);
            ValueObject valData = new ValueObject(str1 + str2, TypeType.String);
            return new TypedExpr(inst, valData, valRef);
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Long.TYPE);
                ValueObject valData = new ValueObject(long1 + long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Float, Double.TYPE);
                ValueObject valData = new ValueObject(double1 + double2, TypeType.Float);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(decimal1.add(decimal2), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr minus(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Long.TYPE);
                ValueObject valData = new ValueObject(long1 - long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Float, Double.TYPE);
                ValueObject valData = new ValueObject(double1 - double2, TypeType.Float);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(decimal1.subtract(decimal2), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr mul(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Long.TYPE);
                ValueObject valData = new ValueObject(long1 * long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Float, Double.TYPE);
                ValueObject valData = new ValueObject(double1 * double2, TypeType.Float);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(decimal1.multiply(decimal2), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr div(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Long.TYPE);
                ValueObject valData = new ValueObject(long1 / long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Float, Double.TYPE);
                ValueObject valData = new ValueObject(double1 / double2, TypeType.Float);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(decimal1.divide(decimal2), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr mod(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Long.TYPE);
                ValueObject valData = new ValueObject(long1 % long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Float, Double.TYPE);
                ValueObject valData = new ValueObject(double1 % double2, TypeType.Float);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(decimal1.remainder(decimal2), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    // Comparison operation
    public TypedExpr eq(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);

        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            ValueObject valData = new ValueObject(true, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } else if (anyNull(firstObj.getData(), secondObj.getData())) {
            boolean result = firstObj.getData().unwrap() == null && secondObj.getData().unwrap() == null;

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);

        } else if (bothOfBoolean(firstObj.getType(), secondObj.getType())) {
            boolean bool1 = (boolean) firstObj.getData().unwrap();
            boolean bool2 = (boolean) secondObj.getData().unwrap();
            boolean result = bool1 == bool2;

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);

        } else if (bothOfString(firstObj.getType(), secondObj.getType())) {
            String str1 = (String) firstObj.getData().unwrap();
            String str2 = (String) secondObj.getData().unwrap();
            boolean result = Objects.equals(str1, str2);

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);

        } else if (onceString(firstObj.getType(), secondObj.getType())) {
            ValueObject valData = new ValueObject(false, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();
                boolean result = long1 == long2;

                ValueObject valData = new ValueObject(result, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();
                boolean result = decimal1.equals(decimal2);

                ValueObject valData = new ValueObject(result, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr ieq(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);

        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            ValueObject valData = new ValueObject(true, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } else if (bothOfBoolean(firstObj.getType(), secondObj.getType())) {
            boolean bool1 = (boolean) firstObj.getData().unwrap();
            boolean bool2 = (boolean) secondObj.getData().unwrap();
            boolean result = bool1 == bool2;

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } else if (bothOfString(firstObj.getType(), secondObj.getType())) {
            String str1 = (String) firstObj.getData().unwrap();
            String str2 = (String) secondObj.getData().unwrap();
            boolean result = StringUtils.equalsIgnoreCase(str1, str2);

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        String one = (firstObj.getData().unwrap() == null) ? "" : firstObj.getData().unwrap().toString();
        String sec = (secondObj.getData().unwrap() == null) ? "" : secondObj.getData().unwrap().toString();
        boolean result = StringUtils.equalsIgnoreCase(one, sec);

        ValueObject valData = new ValueObject(result, TypeType.Boolean);
        return new TypedExpr(inst, valData, valRef);
    }

    public TypedExpr ne(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);

        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            ValueObject valData = new ValueObject(false, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } else if (anyNull(firstObj.getData(), secondObj.getData())) {
            boolean result = !(firstObj.getData().unwrap() == null && secondObj.getData().unwrap() == null);

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);

        } else if (bothOfBoolean(firstObj.getType(), secondObj.getType())) {
            boolean bool1 = (boolean) firstObj.getData().unwrap();
            boolean bool2 = (boolean) secondObj.getData().unwrap();
            boolean result = bool1 != bool2;

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } else if (bothOfString(firstObj.getType(), secondObj.getType())) {
            String str1 = (String) firstObj.getData().unwrap();
            String str2 = (String) secondObj.getData().unwrap();
            boolean result = !Objects.equals(str1, str2);

            ValueObject valData = new ValueObject(result, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } else if (onceString(firstObj.getType(), secondObj.getType())) {
            ValueObject valData = new ValueObject(true, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();
                boolean result = long1 != long2;

                ValueObject valData = new ValueObject(result, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();
                boolean result = !decimal1.equals(decimal2);

                ValueObject valData = new ValueObject(result, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr gt(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return new TypedExpr(inst, CastUtils.NULL, valRef);
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(long1 > long2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(double1 > double2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(decimal1.compareTo(decimal2) > 0, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr ge(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return new TypedExpr(inst, CastUtils.NULL, valRef);
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(long1 >= long2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(double1 >= double2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(decimal1.compareTo(decimal2) >= 0, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr lt(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return new TypedExpr(inst, CastUtils.NULL, valRef);
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(long1 < long2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(double1 < double2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(decimal1.compareTo(decimal2) < 0, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr le(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return new TypedExpr(inst, CastUtils.NULL, valRef);
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredNumber(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(long1 <= long2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Float: {
                double double1 = (double) afterFirst.getData().unwrap();
                double double2 = (double) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(double1 <= double2, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigDecimal decimal1 = (BigDecimal) afterFirst.getData().unwrap();
                BigDecimal decimal2 = (BigDecimal) afterSecond.getData().unwrap();

                ValueObject valData = new ValueObject(decimal1.compareTo(decimal2) <= 0, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    // Bit operation
    public TypedExpr band(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        if (bothOfBoolean(firstObj.getType(), secondObj.getType())) {
            boolean bool1 = (boolean) firstObj.getData().unwrap();
            boolean bool2 = (boolean) secondObj.getData().unwrap();

            Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
            ValueObject valData = new ValueObject(bool1 & bool2, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Integer.TYPE);
                ValueObject valData = new ValueObject(long1 & long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(new BigDecimal(decimal1.and(decimal2)), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr bor(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        if (bothOfBoolean(firstObj.getType(), secondObj.getType())) {
            boolean bool1 = (boolean) firstObj.getData().unwrap();
            boolean bool2 = (boolean) secondObj.getData().unwrap();

            Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
            ValueObject valData = new ValueObject(bool1 | bool2, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Integer.TYPE);
                ValueObject valData = new ValueObject(long1 | long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(new BigDecimal(decimal1.or(decimal2)), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr xor(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        if (bothOfBoolean(firstObj.getType(), secondObj.getType())) {
            boolean bool1 = (boolean) firstObj.getData().unwrap();
            boolean bool2 = (boolean) secondObj.getData().unwrap();

            Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
            ValueObject valData = new ValueObject(bool1 ^ bool2, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Integer.TYPE);
                ValueObject valData = new ValueObject(long1 ^ long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();

                Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                ValueObject valData = new ValueObject(new BigDecimal(decimal1.xor(decimal2)), TypeType.Decimal);
                return new TypedExpr(inst, valData, valRef);
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr ushr(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Integer.TYPE);
                ValueObject valData = new ValueObject(long1 >>> long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();

                if (!decimal2.toString().equals(String.valueOf(decimal2.intValue()))) {
                    throw new DetectRuleRuntimeError(inst.getSecondExpr(), "The shiftRight value '" + decimal2 + "' is out of bounds");
                } else {
                    Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                    ValueObject valData = new ValueObject(new BigDecimal(decimal1.shiftRight(decimal2.intValue())), TypeType.Decimal);
                    return new TypedExpr(inst, valData, valRef);
                }
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr shr(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Integer.TYPE);
                ValueObject valData = new ValueObject(long1 >> long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();

                if (!decimal2.toString().equals(String.valueOf(decimal2.intValue()))) {
                    throw new DetectRuleRuntimeError(inst.getSecondExpr(), "The shiftRight value '" + decimal2 + "' is out of bounds");
                } else {
                    Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                    ValueObject valData = new ValueObject(new BigDecimal(decimal1.shiftRight(decimal2.intValue())), TypeType.Decimal);
                    return new TypedExpr(inst, valData, valRef);
                }
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    public TypedExpr shl(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return firstObj;
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        checkRequiredInteger(inst, firstObj.getData(), secondObj.getData());
        TypeType bigger = numberImplicit(inst, firstObj.getType().getTypeType(), secondObj.getType().getTypeType());
        TypedExpr afterFirst = numberImplicitUpgrade(inst.getFirstExpr(), firstObj, bigger);
        TypedExpr afterSecond = numberImplicitUpgrade(inst.getSecondExpr(), secondObj, bigger);

        switch (bigger) {
            case Integer: {
                long long1 = (long) afterFirst.getData().unwrap();
                long long2 = (long) afterSecond.getData().unwrap();

                Type valRef = ReflectHelper.atomicType(TypeType.Integer, Integer.TYPE);
                ValueObject valData = new ValueObject(long1 << long2, TypeType.Integer);
                return new TypedExpr(inst, valData, valRef);
            }
            case Decimal: {
                BigInteger decimal1 = ((BigDecimal) afterFirst.getData().unwrap()).toBigInteger();
                BigInteger decimal2 = ((BigDecimal) afterSecond.getData().unwrap()).toBigInteger();

                if (!decimal2.toString().equals(String.valueOf(decimal2.intValue()))) {
                    throw new DetectRuleRuntimeError(inst.getSecondExpr(), "The shiftLeft value '" + decimal2 + "' is out of bounds");
                } else {
                    Type valRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                    ValueObject valData = new ValueObject(new BigDecimal(decimal1.shiftLeft(decimal2.intValue())), TypeType.Decimal);
                    return new TypedExpr(inst, valData, valRef);
                }
            }
            default:
                break;
        }

        throw new DetectRuleRuntimeError(inst, "RuntimeError, code unreachable");
    }

    // Logical operations
    public TypedExpr and(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return new TypedExpr(inst, CastUtils.NULL, valRef);
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        String symbol = inst.getSymbol().getValue();
        if (firstObj.getType().getTypeType() != TypeType.Boolean) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Boolean");
        }
        if (secondObj.getType().getTypeType() != TypeType.Boolean) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Boolean");
        }

        boolean bool1 = (boolean) firstObj.getData().unwrap();
        boolean bool2 = (boolean) secondObj.getData().unwrap();

        ValueObject valData = new ValueObject(bool1 && bool2, TypeType.Boolean);
        return new TypedExpr(inst, valData, valRef);
    }

    public TypedExpr or(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            return new TypedExpr(inst, CastUtils.NULL, valRef);
        }

        if (firstObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(firstObj.getLocation(), "Invalid expression.");
        }
        if (secondObj.getData().unwrap() == null) {
            throw new DetectRuleRuntimeError(secondObj.getLocation(), "Invalid expression.");
        }

        String symbol = inst.getSymbol().getValue();
        if (firstObj.getType().getTypeType() != TypeType.Boolean) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Boolean");
        }
        if (secondObj.getType().getTypeType() != TypeType.Boolean) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be Boolean");
        }

        boolean bool1 = (boolean) firstObj.getData().unwrap();
        boolean bool2 = (boolean) secondObj.getData().unwrap();
        ValueObject valData = new ValueObject(bool1 || bool2, TypeType.Boolean);
        return new TypedExpr(inst, valData, valRef);
    }

    // Matching operation
    public TypedExpr in(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            ValueObject valData = new ValueObject(true, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        String symbol = inst.getSymbol().getValue();
        TypeType containerType = secondObj.getType().getTypeType();
        switch (containerType) {
            case Null: {
                ValueObject valData = new ValueObject(false, TypeType.Boolean);
                return new TypedExpr(inst, valData, valRef);
            }
            case KeyPair:
            case Collection:
                break;
            default:
                throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the second expression needs to be KeyPair/Collection");
        }

        switch (firstObj.getType().getTypeType()) {
            case Boolean:
            case Integer:
            case Float:
            case Decimal:
            case String:
            case Time:
            case Date:
            case Datetime:
            case Null:
                CollectionAccess arrayKey = new ArrayObject();
                arrayKey.putElement(firstObj.getData());
                switch (containerType) {
                    case KeyPair: {
                        boolean result = testIn(arrayKey, (KeyPairAccess) secondObj.getData());

                        ValueObject valData = new ValueObject(result, TypeType.Boolean);
                        return new TypedExpr(inst, valData, valRef);
                    }
                    case Collection: {
                        boolean result = testIn(arrayKey, (CollectionAccess) secondObj.getData());

                        ValueObject valData = new ValueObject(result, TypeType.Boolean);
                        return new TypedExpr(inst, valData, valRef);
                    }
                    default:
                        throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the second expression needs to be KeyPair/Collection");
                }
            case KeyPair:
                switch (containerType) {
                    case KeyPair: {
                        boolean result = testIn((KeyPairAccess) firstObj.getData(), (KeyPairAccess) secondObj.getData());

                        ValueObject valData = new ValueObject(result, TypeType.Boolean);
                        return new TypedExpr(inst, valData, valRef);
                    }
                    case Collection: {
                        boolean result = testIn((KeyPairAccess) firstObj.getData(), (ArrayObject) secondObj.getData());

                        ValueObject valData = new ValueObject(result, TypeType.Boolean);
                        return new TypedExpr(inst, valData, valRef);
                    }
                    default:
                        throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the second expression needs to be KeyPair/Collection");
                }
            case Collection:
                switch (containerType) {
                    case KeyPair: {
                        boolean result = testIn((ArrayObject) firstObj.getData(), (KeyPairAccess) secondObj.getData());

                        ValueObject valData = new ValueObject(result, TypeType.Boolean);
                        return new TypedExpr(inst, valData, valRef);
                    }
                    case Collection: {
                        boolean result = testIn((ArrayObject) firstObj.getData(), (ArrayObject) secondObj.getData());

                        ValueObject valData = new ValueObject(result, TypeType.Boolean);
                        return new TypedExpr(inst, valData, valRef);
                    }
                    default:
                        throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the second expression needs to be KeyPair/Collection");
                }
            default:
                break;
        }
        throw new DetectRuleRuntimeError(inst.getFirstExpr(),
            "In dyadic '" + symbol + "' operation, the expression needs to be Boolean/Integer/Float/Decimal/String/Time/Date/Datetime/Null/KeyPair/Collection");
    }

    private boolean testIn(CollectionAccess test, CollectionAccess container) {
        for (Iterator<LangObject> it = test.toIterator(); it.hasNext();) {
            LangObject langObject = it.next();

            if (!container.contains(langObject)) {
                return false;
            }
        }
        return true;
    }

    private boolean testIn(CollectionAccess test, KeyPairAccess container) {
        for (Iterator<LangObject> it = test.toIterator(); it.hasNext();) {
            LangObject langObject = it.next();

            if (langObject.getType() == TypeType.Null) {
                if (!container.containPair(null)) {
                    return false;
                }
            } else if (langObject.getType() == TypeType.String) {
                if (!container.containPair((String) langObject.unwrap())) {
                    return false;
                }
            } else if (langObject.getType() == TypeType.Integer || langObject.getType() == TypeType.Float) {
                if (!container.containPair(langObject.unwrap().toString())) {
                    return false;
                }
            } else if (langObject.getType() == TypeType.Decimal) {
                if (!container.containPair(((BigDecimal) langObject.unwrap()).toPlainString())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean testIn(KeyPairAccess test, CollectionAccess container) {
        for (String langObject : test.getKeySet()) {
            if (!container.contains(new ValueObject(langObject, TypeType.String))) {
                return false;
            }
        }
        return true;
    }

    private boolean testIn(KeyPairAccess test, KeyPairAccess container) {
        for (String langObject : test.getKeySet()) {
            if (!container.containPair(langObject)) {
                return false;
            }

            LangObject value1 = test.getPair(langObject);
            LangObject value2 = container.getPair(langObject);

            if (value1.getType() != value2.getType()) {
                return false;
            }

            if (!Objects.equals(value1.unwrap(), value2.unwrap())) {
                return false;
            }
        }

        return true;
    }

    public TypedExpr matches(DyadicExpression inst, TypedExpr firstObj, TypedExpr secondObj) {
        Type valRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        if (bothOfNull(firstObj.getData(), secondObj.getData())) {
            ValueObject valData = new ValueObject(true, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        if (firstObj.getData() == null || firstObj.getType().getTypeType() == TypeType.Null) {
            ValueObject valData = new ValueObject(true, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        }

        String symbol = inst.getSymbol().getValue();
        if (firstObj.getType().getTypeType() != TypeType.String) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be String");
        }
        if (secondObj.getType().getTypeType() == TypeType.Null || secondObj.getData().unwrap() == null) {
            ValueObject valData = new ValueObject(false, TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } else if (secondObj.getType().getTypeType() != TypeType.String) {
            throw new DetectRuleRuntimeError(inst.getFirstExpr(), "In dyadic '" + symbol + "' operation, the expression needs to be String");
        }

        try {
            String str1 = (String) firstObj.getData().unwrap();
            String str2 = (String) secondObj.getData().unwrap();

            ValueObject valData = new ValueObject(str1.matches(str2), TypeType.Boolean);
            return new TypedExpr(inst, valData, valRef);
        } catch (Exception e) {
            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' matches has error.", e);
        }
    }
}
