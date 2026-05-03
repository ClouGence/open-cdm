package com.clougence.detectrule.runtime.v1;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.clougence.detectrule.lang.*;
import com.clougence.detectrule.lang.reflect.*;
import com.clougence.detectrule.parser.DetectRulesFeature;
import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.expr.*;
import com.clougence.detectrule.parser.ast.primary.*;
import com.clougence.detectrule.parser.ast.statement.*;
import com.clougence.detectrule.parser.ast.token.CastType;
import com.clougence.detectrule.parser.ast.token.DoubleSymbolToken;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.detectrule.runtime.DataTimeValueParser;
import com.clougence.detectrule.runtime.DetectRuleRuntimeError;
import com.clougence.detectrule.runtime.DetectRuleRuntimeException;
import com.clougence.detectrule.runtime.EngineOption;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.utils.StringUtils;

public class DetectRuleEngineV1 implements DetectRuleAstVisitor {

    // env data
    private final Object                  rootDomain;
    private final Type                    rootType;
    private final Map<String, String>     envVariables      = new HashMap<>();
    private final EngineOption            option;
    private final Set<DetectRulesFeature> features          = new HashSet<>();
    private final DyadicUtils             dyadicUtils;

    // runtime data
    private final Stack<Accessible>       refStack          = new Stack<>();
    private final Stack<LangObject>       dataStack         = new Stack<>();

    private final Map<String, LangObject> localVariableData = new HashMap<>();
    private final Map<String, Accessible> localVariableType = new HashMap<>();
    private LangObject                    returnData;
    private boolean                       exitSignal        = false;

    public DetectRuleEngineV1(Object rootDomain, Type rootType, EngineOption option){
        this.rootDomain = rootDomain;
        this.rootType = rootType;
        this.option = option;
        if (option.getFeatures() != null) {
            Collections.addAll(this.features, option.getFeatures());
        }
        this.dyadicUtils = new DyadicUtils(option);
    }

    private static ValueObject negativeOrNot(boolean negative, long num) {
        long result = negative ? -num : num;
        return new ValueObject(result, TypeType.Integer);
    }

    private static ValueObject negativeOrNot(boolean negative, double num) {
        double result = negative ? -num : num;
        return new ValueObject(result, TypeType.Float);
    }

    private static ValueObject negativeOrNot(boolean negative, BigDecimal num) {
        BigDecimal result = negative ? num.negate() : num;
        return new ValueObject(result, TypeType.Decimal);
    }

    public void putVariables(Map<String, String> variables) {
        if (variables != null) {
            this.envVariables.putAll(variables);
        }
    }

    public LangObject returnData(LangObject defaultValue) {
        return this.returnData == null ? defaultValue : this.returnData;
    }

    @Override
    public void visitStatementList(VisitorContext<StatementList> visitCtx) {
        StatementList inst = visitCtx.getInst();
        for (Statement statement : inst.getStatementList()) {
            if (this.exitSignal) {
                return;
            }
            statement.accept(this);
        }
    }

    @Override
    public void visitDefineStatement(VisitorContext<DefineStatement> visitCtx) {
        DefineStatement inst = visitCtx.getInst();
        StringToken defaultValue = inst.getDefaultValue();

        String varName = inst.getName().getEscapeValue();
        String varValue = defaultValue != null ? defaultValue.getEscapeValue() : null;

        if (!this.envVariables.containsKey(varName)) {
            this.envVariables.put(varName, varValue);
        }
    }

    @Override
    public void visitAssertStatement(VisitorContext<AssertStatement> visitCtx) {
        AssertStatement inst = visitCtx.getInst();

        inst.getTestExpr().accept(this);
        Accessible testRef = this.refStack.pop();
        LangObject testVal = this.dataStack.pop();

        if (!(boolean) testVal.unwrap()) {
            inst.getDataExpr().accept(this);
            Accessible returnRef = this.refStack.pop();
            LangObject returnVal = this.dataStack.pop();

            this.returnData = returnVal;
            this.exitSignal = true;
        }
    }

    @Override
    public void visitReturnStatement(VisitorContext<ReturnStatement> visitCtx) {
        ReturnStatement inst = visitCtx.getInst();
        inst.getExpr().accept(this);

        Accessible exprRef = this.refStack.pop();
        LangObject exprVal = this.dataStack.pop();

        this.returnData = exprVal;
        this.exitSignal = true;
    }

    @Override
    public void visitSwitchStatement(VisitorContext<SwitchStatement> visitCtx) {
        SwitchStatement inst = visitCtx.getInst();

        List<SwitchThenStatement> ifStatements = inst.getElseIfStatements();
        for (SwitchThenStatement statement : ifStatements) {
            statement.getTestExpr().accept(this);
            Accessible testRef = this.refStack.pop();
            LangObject testVal = this.dataStack.pop();

            if ((boolean) testVal.unwrap()) {
                for (Statement s : statement.getStatements()) {
                    if (this.exitSignal) {
                        return;
                    } else {
                        s.accept(this);
                    }
                }
                return;
            }
        }

        SwitchElseStatement elseStatements = inst.getElseStatements();
        if (elseStatements != null) {
            for (Statement s : elseStatements.getStatements()) {
                if (this.exitSignal) {
                    return;
                }
                s.accept(this);
            }
        }
    }

    @Override
    public void visitSwitchThenStatement(VisitorContext<SwitchThenStatement> visitCtx) {
        // do nothing
    }

    @Override
    public void visitSwitchElseStatement(VisitorContext<SwitchElseStatement> visitCtx) {
        // do nothing
    }

    @Override
    public void visitAssignStatement(VisitorContext<AssignStatement> visitCtx) {
        AssignStatement inst = visitCtx.getInst();
        String varName = inst.getVarName().getValue();

        TypedExpr typedExpr;
        Expression varExpr = inst.getVarExpr();
        if (varExpr instanceof DetectExpression) {
            typedExpr = accessDetectExpression((DetectExpression) varExpr);
        } else if (varExpr instanceof FunctionExpression) {
            typedExpr = accessFunctionExpression((FunctionExpression) varExpr);
        } else if (varExpr instanceof ParamExpression) {
            typedExpr = accessParamExpression((ParamExpression) varExpr);
        } else if (varExpr instanceof BooleanValue) {
            typedExpr = accessBooleanValue((BooleanValue) varExpr);
        } else if (varExpr instanceof DataTimeValue) {
            typedExpr = accessDataTimeValue((DataTimeValue) varExpr);
        } else if (varExpr instanceof NullValue) {
            typedExpr = new TypedExpr(varExpr, CastUtils.NULL, ReflectHelper.atomicType(TypeType.Null, Void.class));
        } else if (varExpr instanceof NumberValue) {
            typedExpr = accessNumberValue((NumberValue) varExpr);
        } else if (varExpr instanceof XxValue) {
            typedExpr = accessXxValue((XxValue) varExpr);
        } else if (varExpr instanceof StringValue) {
            typedExpr = accessStringValue((StringValue) varExpr);
        } else if (varExpr instanceof ListValue) {
            typedExpr = accessListValue((ListValue) varExpr);
        } else if (varExpr instanceof ObjectValue) {
            typedExpr = accessObjectValue((ObjectValue) varExpr);
        } else if (varExpr instanceof CastExpression) {
            ((CastExpression) varExpr).getExpr().accept(this);
            typedExpr = accessCastExpressionAsType(((CastExpression) varExpr).getCastType());
        } else {
            throw new DetectRuleRuntimeError(inst, "Unsupported.");
        }

        this.localVariableData.put(varName, typedExpr.getData());
        this.localVariableType.put(varName, typedExpr.getType());
    }

    @Override
    public void visitThrowStatement(VisitorContext<ThrowStatement> visitCtx) {
        ThrowStatement inst = visitCtx.getInst();
        inst.getExpr().accept(this);

        Accessible exprRef = this.refStack.pop();
        LangObject exprVal = this.dataStack.pop();

        this.returnData = exprVal;
        this.exitSignal = true;
        throw new DetectRuleRuntimeException(inst, this.returnData);
    }

    @Override
    public void visitParamExpression(VisitorContext<ParamExpression> visitCtx) {
        ParamExpression inst = visitCtx.getInst();
        TypedExpr typedExpr = accessParamExpression(inst);

        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    @Override
    public void visitDetectExpression(VisitorContext<DetectExpression> visitCtx) {
        DetectExpression inst = visitCtx.getInst();
        TypedExpr typedExpr = accessDetectExpression(inst);

        List<Expression> continuous = inst.getExpr().getContinuous();
        Expression lastExpr = continuous.get(continuous.size() - 1);

        this.pushToStack(typedExpr.getType(), typedExpr.getData(), lastExpr);
    }

    @Override
    public void visitFunctionExpression(VisitorContext<FunctionExpression> visitCtx) {
        FunctionExpression inst = visitCtx.getInst();

        // do call
        TypedExpr typedExpr = accessFunctionExpression(inst);

        // find last expr for error.
        List<Expression> continuous = inst.getIdentify().getExpr().getContinuous();
        Expression lastExpr = continuous.get(continuous.size() - 1);
        this.pushToStack(typedExpr.getType(), typedExpr.getData(), lastExpr);
    }

    @Override
    public void visitFunctionParameters(VisitorContext<FunctionParameters> visitCtx) {
        visitCtx.visitChildren(this);
    }

    private TypedExpr accessParamExpression(ParamExpression inst) {
        String parameter = inst.getParameter().getEscapeValue();

        String value = this.envVariables.get(parameter);
        if (value == null) {
            ValueObject objData = new ValueObject(null, TypeType.Null);
            Type objType = ReflectHelper.atomicType(TypeType.Null, Void.class);
            return new TypedExpr(inst, objData, objType);
        } else {
            ValueObject objData = new ValueObject(value, TypeType.String);
            Type objType = ReflectHelper.atomicType(TypeType.String, String.class);
            return new TypedExpr(inst, objData, objType);
        }
    }

    private TypedExpr accessDetectExpression(DetectExpression inst) {
        List<Expression> continuous = inst.getExpr().getContinuous();

        this.refStack.push(this.rootType);
        this.dataStack.push(new RefObject(this.rootDomain));

        Expression last = inst;
        for (Expression expression : continuous) {
            expression.accept(this);
            last = expression;
        }

        Accessible popType = this.refStack.pop();
        LangObject popData = this.dataStack.pop();
        return new TypedExpr(last, popData, popType);
    }

    private TypedExpr accessFunctionExpression(FunctionExpression inst) {
        List<Expression> continuous = inst.getIdentify().getExpr().getContinuous();

        // expr root, and eval expr
        this.refStack.push(this.rootType);
        this.dataStack.push(new RefObject(this.rootDomain));
        LangObject last = null;
        Expression lastExpr = inst;
        for (int i = 0; i < continuous.size(); i++) {
            if (i > 0) {
                last = this.dataStack.peek(); // hold last parent, for invoke obj.
            }
            lastExpr = continuous.get(i);
            lastExpr.accept(this);
        }

        // args
        List<Expression> argsExprList = inst.getParameters().getArgs();
        Object[] argsValue = new Object[argsExprList.size()];
        Accessible[] argsType = new Accessible[argsExprList.size()];
        for (int i = 0; i < argsExprList.size(); i++) {
            Expression argExpr = argsExprList.get(i);
            argExpr.accept(this);

            Accessible argRef = this.refStack.pop();
            LangObject argObj = this.dataStack.pop();

            argsType[i] = argRef;
            argsValue[i] = argObj.unwrap();
        }

        // call
        try {
            Accessible popFuncRef = this.refStack.pop();
            LangObject popFunObj = this.dataStack.pop();
            if (popFunObj.unwrap() == null && this.features.contains(DetectRulesFeature.AllowUndefinedVariableFeature)) {
                if (popFuncRef.getTypeType() == TypeType.Null) {
                    Type refType = ReflectHelper.atomicType(TypeType.Null, null);
                    return new TypedExpr(lastExpr, CastUtils.NULL, refType);
                } else {
                    Func funRef = (Func) popFuncRef;
                    return new TypedExpr(lastExpr, CastUtils.NULL, funRef.getFuncReturn());
                }
            }

            Func funRef = (Func) popFuncRef;
            RefObject funObj = (RefObject) popFunObj;

            Type funcReturnType = funRef.getFuncReturn();
            Object funcReturnData;
            if (funRef.isStaticMethod()) {
                funcReturnData = ((Method) funObj.unwrap()).invoke(null, argsValue);
            } else {
                funcReturnData = ((Method) funObj.unwrap()).invoke(last.unwrap(), argsValue);
            }

            if (funcReturnData == null) {
                return new TypedExpr(lastExpr, CastUtils.NULL, funcReturnType);
            } else if (funcReturnType.getTypeType().isAtomic()) {
                Type typed = ReflectHelper.atomicType(funcReturnType.getTypeType(), funcReturnData.getClass());
                return new TypedExpr(lastExpr, new ValueObject(funcReturnData, funcReturnType.getTypeType()), typed);
            } else {
                return new TypedExpr(lastExpr, new RefObject(funcReturnData), funcReturnType);
            }
        } catch (Exception e) {
            throw new DetectRuleRuntimeError(inst, "invokeMethod '" + inst + "' failed, " + e.getMessage(), e);
        }
    }

    private void pushToStack(Accessible popType, LangObject popOwner, Expression lastExpr) {
        Object theData = popOwner.unwrap();
        LangObject pushData;
        switch (popType.getTypeType()) {
            case Boolean: {
                pushData = RefUtils.toBooleanOrNull((Boolean) theData);
                break;
            }
            case Float: {
                pushData = RefUtils.toFloatOrNull((Number) theData);
                break;
            }
            case Integer: {
                pushData = RefUtils.toIntegerOrNull((Number) theData);
                break;
            }
            case Decimal: {
                pushData = RefUtils.toDecimalOrNull((BigDecimal) theData);
                break;
            }
            case String: {
                pushData = RefUtils.toStringOrNull(theData);
                break;
            }
            case Date: {
                pushData = RefUtils.toDateOrNull(lastExpr, popOwner.getType(), theData);
                break;
            }
            case Time: {
                pushData = RefUtils.toTimeOrNull(lastExpr, popOwner.getType(), theData);
                break;
            }
            case Datetime: {
                pushData = RefUtils.toDatetimeOrNull(lastExpr, popOwner.getType(), theData);
                break;
            }
            case Null:
                pushData = CastUtils.NULL;
                break;
            case Collection: {
                pushData = RefUtils.toCollectionOrNull(lastExpr, popType, popOwner.getType(), theData);
                break;
            }
            case KeyPair: {
                pushData = RefUtils.toKeyPairOrNull(lastExpr, popType, popOwner.getType(), theData);
                break;
            }
            case Func: {
                pushData = popOwner;
                break;
            }
            default:
                throw new DetectRuleRuntimeError(lastExpr, "expression '" + popType.getTypeType() + "' type Unsupported.");
        }

        this.refStack.push(popType);
        this.dataStack.push(pushData);
    }

    private Accessible dynamicAccessible(LangObject valObj) {
        if (valObj == null) {
            return ReflectHelper.atomicType(TypeType.Null, null);
        }

        Object val = valObj.unwrap();
        if (val == null) {
            return ReflectHelper.atomicType(TypeType.Null, null);
        }

        TypeType valTypeType = TypeType.valueOfType(val.getClass());
        if (valTypeType.isAtomic()) {
            return ReflectHelper.atomicType(valTypeType, null);
        } else if (valTypeType.isSelfType()) {
            if (valTypeType == TypeType.Null) {
                return ReflectHelper.atomicType(TypeType.Null, null);
            } else if (valTypeType == TypeType.Collection) {
                return ReflectHelper.arrayType(val.getClass(), null);
            } else if (valTypeType == TypeType.KeyPair) {
                return ReflectHelper.keyPairType(val.getClass(), null);
            } else {
                return null;
            }
        } else {
            return ReflectHelper.resolveDomain(val.getClass());
        }
    }

    @Override
    public void visitIdentifyNameExpression(VisitorContext<IdentifyNameExpression> visitCtx) {
        IdentifyNameExpression inst = visitCtx.getInst();
        Accessible popType = this.refStack.pop();
        LangObject popOwner = this.dataStack.pop();

        if (!(popType instanceof Type)) {
            throw new DetectRuleRuntimeError(inst, "readField '" + inst.getIdentify() + "' failed, the field owner is not Type.");
        }
        Type owner = (Type) popType;

        // Map
        if (owner.getTypeType() == TypeType.KeyPair) {
            String keyStr = inst.getIdentify();
            LangObject valObj;
            Accessible valRef;

            if (popOwner instanceof KeyPairAccess) {
                valObj = ((KeyPairAccess) popOwner).getPair(keyStr);
                valRef = ((KeyPairAccess) popOwner).getPairReflect(keyStr);
            } else {
                RefObject refObject = (RefObject) popOwner;
                Object tmpObj = ((Map) refObject.getValue()).get(keyStr);
                if (tmpObj instanceof LangObject) {
                    valObj = (LangObject) tmpObj;
                } else {
                    valObj = new RefObject(tmpObj);
                }
                valRef = null;
            }

            if (valRef == null) {
                valRef = this.dynamicAccessible(valObj);
                if (valRef == null) {
                    throw new DetectRuleRuntimeError(inst, "pairType Unsupported '" + inst.getIdentify() + "' on domain '" + owner.getLocalType() + "'");
                }
            }

            this.refStack.push(valRef);
            this.dataStack.push(valObj);
            return;
        }

        // field
        Field ownerField = owner.getField(inst.getIdentify());
        Func ownerMethod = owner.getMethod(inst.getIdentify());
        if (ownerField == null && ownerMethod == null) {
            if (this.features.contains(DetectRulesFeature.AllowUndefinedVariableFeature)) {
                this.refStack.push(ReflectHelper.atomicType(TypeType.Null, null));
                this.dataStack.push(CastUtils.NULL);
                return;
            } else {
                throw new DetectRuleRuntimeError(inst, "notFound Field or Method '" + inst.getIdentify() + "' on domain '" + owner.getLocalType() + "'");
            }
        }

        if (ownerField != null) {
            if (ownerField.getAccessMode() == AccessMode.WriteOnly) {
                throw new DetectRuleRuntimeError(inst, "foundField '" + inst.getIdentify() + "' is WriteOnly.");
            }

            try {
                Object invoke = ownerField.getReadMethod().invoke(popOwner.unwrap());
                Type valueType = ownerField.getType();
                if (invoke == null) {
                    this.refStack.push(valueType);
                    this.dataStack.push(CastUtils.NULL);
                } else {
                    LangObject valueObj;
                    TypeType fieldTypeType = ownerField.getType().getTypeType();
                    if (fieldTypeType.isAtomic()) {
                        valueObj = new ValueObject(invoke, fieldTypeType);
                    } else {
                        valueObj = new RefObject(invoke);
                    }

                    this.refStack.push(valueType);
                    this.dataStack.push(valueObj);
                }
                return;
            } catch (Exception e) {
                throw new DetectRuleRuntimeError(inst, "readField '" + inst.getIdentify() + "' failed.", e);
            }
        }

        if (ownerMethod != null) {
            try {
                this.refStack.push(ownerMethod);
                this.dataStack.push(new RefObject(ownerMethod.getLocalMethod()));
                return;
            } catch (Exception e) {
                throw new DetectRuleRuntimeError(inst, "readField '" + inst.getIdentify() + "' failed.", e);
            }
        }
        throw new DetectRuleRuntimeError(visitCtx.getInst(), "RuntimeError, code unreachable");
    }

    @Override
    public void visitIdentifyGroupExpression(VisitorContext<IdentifyGroupExpression> visitCtx) {
        IdentifyGroupExpression inst = visitCtx.getInst();
        List<Expression> continuous = inst.getContinuous();

        for (int i = 0; i < continuous.size(); i++) {
            Expression expr = continuous.get(i);
            if (i == 0) {
                if (!(expr instanceof IdentifyNameExpression)) {
                    throw new DetectRuleRuntimeError(inst, "RuntimeError, the first expr is not identify.");
                } else {
                    String idName = ((IdentifyNameExpression) expr).getIdentify();

                    if (this.localVariableData.containsKey(idName)) {
                        LangObject idData = this.localVariableData.get(idName);
                        Accessible idType = this.localVariableType.get(idName);
                        this.refStack.push(idType);
                        this.dataStack.push(idData);
                    } else if (this.features.contains(DetectRulesFeature.AllowUndefinedVariableFeature)) {
                        this.refStack.push(ReflectHelper.atomicType(TypeType.Null, null));
                        this.dataStack.push(CastUtils.NULL);
                    } else {
                        throw new DetectRuleRuntimeError(expr, "ScriptError, " + idName + " undefined.");
                    }
                }
            } else {
                expr.accept(this);
            }
        }
    }

    @Override
    public void visitIdentifySubExpression(VisitorContext<IdentifySubExpression> visitCtx) {
        IdentifySubExpression inst = visitCtx.getInst();
        visitCtx.visitChildren(this);

        Accessible subType = this.refStack.pop();
        LangObject subObj = this.dataStack.pop();
        Accessible dataContainerType = this.refStack.pop();
        LangObject dataContainerObj = this.dataStack.pop();

        if (dataContainerType.getTypeType() == TypeType.Collection) {
            switch (subType.getTypeType()) {
                case Null: {
                    this.refStack.push(ReflectHelper.atomicType(TypeType.Null, null));
                    this.dataStack.push(CastUtils.NULL);
                    return;
                }
                case Integer: {
                    Long intValue = (Long) subObj.unwrap();
                    if (intValue < 0) {
                        throw new DetectRuleRuntimeError(inst, "subExpr '" + inst + "' failed, The subExpr value must be >= 0.");
                    } else {
                        Object realSubObj = dataContainerObj.unwrap();
                        CollectionAccess collection = (CollectionAccess) RefUtils.toCollectionOrNull(inst, dataContainerType, subObj.getType(), realSubObj);
                        try {
                            LangObject element = collection.getElement(intValue.intValue());
                            this.refStack.push(collection.getElementType());
                            this.dataStack.push(new RefObject(element.unwrap()));
                            return;
                        } catch (IndexOutOfBoundsException e) {
                            throw new DetectRuleRuntimeError(inst, "IndexOutOfBounds, Index: " + intValue.intValue() + ", Size: " + collection.getSize());
                        }
                    }
                }
                case Decimal: {
                    BigDecimal decimalValue = (BigDecimal) subObj.unwrap();
                    if (this.dyadicUtils.isBigInteger(decimalValue)) {
                        int intValue = decimalValue.intValue();
                        if (intValue < 0) {
                            throw new DetectRuleRuntimeError(inst, "subExpr '" + inst + "' failed, The subExpr value must be >= 0.");
                        } else {
                            Object realSubObj = dataContainerObj.unwrap();
                            CollectionAccess collection = (CollectionAccess) RefUtils.toCollectionOrNull(inst, dataContainerType, subObj.getType(), realSubObj);
                            LangObject element = collection.getElement(intValue);

                            this.refStack.push(collection.getElementType());
                            this.dataStack.push(new RefObject(element.unwrap()));
                            return;
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            throw new DetectRuleRuntimeError(inst, "subExpr '" + inst + "' failed, The subExpr value type must be a Integer/Decimal(BigInteger).");

        } else if (dataContainerType.getTypeType() == TypeType.KeyPair) {
            switch (subType.getTypeType()) {
                case Null: {
                    this.refStack.push(ReflectHelper.atomicType(TypeType.Null, null));
                    this.dataStack.push(CastUtils.NULL);
                    return;
                }
                case String: {
                    String strValue = (String) subObj.unwrap();
                    if (strValue == null) {
                        throw new DetectRuleRuntimeError(inst, "subExpr '" + inst + "' failed, The key is null.");
                    } else {
                        Object realSubObj = dataContainerObj.unwrap();
                        KeyPairAccess collection = (KeyPairAccess) RefUtils.toKeyPairOrNull(inst, dataContainerType, subObj.getType(), realSubObj);
                        LangObject pairValue = collection.getPair(strValue);
                        Accessible pairReflect = collection.getPairReflect(strValue);

                        if (pairValue == null) {
                            pairValue = CastUtils.NULL;
                        }
                        if (pairReflect == null) {
                            pairReflect = this.dynamicAccessible(pairValue);
                        }

                        this.refStack.push(pairReflect);
                        this.dataStack.push(new RefObject(pairValue.unwrap()));
                        return;
                    }
                }
                default:
                    break;
            }
            throw new DetectRuleRuntimeError(inst, "subExpr '" + inst + "' failed, The subExpr value type must be a String.");
        } else {
            throw new DetectRuleRuntimeError(inst, "subExpr '" + inst + "' failed, subExpr are only allowed for Collection/KeyPair.");
        }
    }

    @Override
    public void visitUnaryExpression(VisitorContext<UnaryExpression> visitCtx) {
        UnaryExpression inst = visitCtx.getInst();
        inst.getExpr().accept(this);
        String symbol = inst.getSymbol().getValue();

        Accessible dataRef = this.refStack.pop();
        LangObject dataObj = this.dataStack.pop();

        switch (inst.getSide()) {
            case Left:
                if (symbol.equals("-") || StringUtils.equalsIgnoreCase(symbol, "minus")) {
                    switch (dataRef.getTypeType()) {
                        case Integer:
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(-(long) dataObj.unwrap(), TypeType.Integer));
                            return;
                        case Float:
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(-(double) dataObj.unwrap(), TypeType.Float));
                            return;
                        case Decimal:
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(((BigDecimal) dataObj.unwrap()).negate(), TypeType.Decimal));
                            return;
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Integer/Float/Decimal");
                    }
                } else if (symbol.equals("+") || StringUtils.equalsIgnoreCase(symbol, "plus")) {
                    switch (dataRef.getTypeType()) {
                        case Integer:
                        case Float:
                        case Decimal:
                            this.refStack.push(dataRef);
                            this.dataStack.push(dataObj);
                            return;
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Integer/Float/Decimal");
                    }
                } else if (symbol.equals("!") || StringUtils.equalsIgnoreCase(symbol, "not")) {
                    switch (dataRef.getTypeType()) {
                        case Null: // !null  --> not false --> true
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(true, TypeType.Boolean));
                            return;
                        case Boolean:
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(!(Boolean) dataObj.unwrap(), TypeType.Boolean));
                            return;
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Boolean");
                    }
                } else if (symbol.equals("~") || StringUtils.equalsIgnoreCase(symbol, "bnot")) {
                    switch (dataRef.getTypeType()) {
                        case Boolean:
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(!(Boolean) dataObj.unwrap(), TypeType.Boolean));
                            return;
                        case Integer:
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(~(Long) dataObj.unwrap(), TypeType.Integer));
                            return;
                        case Decimal:
                            BigDecimal decimalValue = (BigDecimal) dataObj.unwrap();
                            if (this.dyadicUtils.isBigInteger(decimalValue)) {
                                String bitStr = decimalValue.toBigInteger().toString(2);
                                StringBuilder strBuilder = new StringBuilder();
                                for (char code : bitStr.toCharArray()) {
                                    if (code == '0') {
                                        strBuilder.append("1");
                                    } else {
                                        strBuilder.append("0");
                                    }
                                }
                                BigDecimal decimal = new BigDecimal(new BigInteger(strBuilder.toString(), 2));
                                this.refStack.push(dataRef);
                                this.dataStack.push(new ValueObject(decimal, TypeType.Decimal));
                                return;
                            }
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Boolean/Integer/Decimal(BigInteger)");
                    }
                } else if (symbol.equals("--") || StringUtils.equalsIgnoreCase(symbol, "decr")) {
                    switch (dataRef.getTypeType()) {
                        case Integer:
                            long longValue = (long) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(longValue - 1, TypeType.Integer));
                            return;
                        case Float:
                            double floatValue = (double) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(floatValue - 1, TypeType.Float));
                            return;
                        case Decimal:
                            BigDecimal decimalValue = (BigDecimal) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(decimalValue.subtract(BigDecimal.ONE), TypeType.Decimal));
                            return;
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Integer/Float/Decimal");
                    }
                } else if (symbol.equals("++") || StringUtils.equalsIgnoreCase(symbol, "incr")) {
                    switch (dataRef.getTypeType()) {
                        case Integer:
                            long longValue = (long) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(longValue + 1, TypeType.Integer));
                            return;
                        case Float:
                            double floatValue = (double) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(floatValue + 1, TypeType.Float));
                            return;
                        case Decimal:
                            BigDecimal decimalValue = (BigDecimal) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(decimalValue.add(BigDecimal.ONE), TypeType.Decimal));
                            return;
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Integer/Float/Decimal");
                    }
                } else {
                    throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' on left unsupported.");
                }
            case Right:
                if (symbol.equals("--") || StringUtils.equalsIgnoreCase(symbol, "decr")) {
                    switch (dataRef.getTypeType()) {
                        case Integer:
                            long longValue = (long) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(longValue - 1, TypeType.Integer));
                            return;
                        case Float:
                            double floatValue = (double) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(floatValue - 1, TypeType.Float));
                            return;
                        case Decimal:
                            BigDecimal decimalValue = (BigDecimal) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(decimalValue.subtract(BigDecimal.ONE), TypeType.Decimal));
                            return;
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Integer/Float/Decimal");
                    }
                } else if (symbol.equals("++") || StringUtils.equalsIgnoreCase(symbol, "incr")) {
                    switch (dataRef.getTypeType()) {
                        case Integer:
                            long longValue = (long) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(longValue + 1, TypeType.Integer));
                            return;
                        case Float:
                            double floatValue = (double) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(floatValue + 1, TypeType.Float));
                            return;
                        case Decimal:
                            BigDecimal decimalValue = (BigDecimal) dataObj.unwrap();
                            this.refStack.push(dataRef);
                            this.dataStack.push(new ValueObject(decimalValue.add(BigDecimal.ONE), TypeType.Decimal));
                            return;
                        default:
                            throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' only support Integer/Float/Decimal");
                    }
                } else {
                    throw new DetectRuleRuntimeError(inst, "Unary expression has en error, '" + symbol + "' on right unsupported.");
                }
            default:
                throw new DetectRuleRuntimeError(inst, "Unary expression has en error, symbol side unclear.");
        }
    }

    @Override
    public void visitDyadicExpression(VisitorContext<DyadicExpression> visitCtx) {
        this._visitDyadicExpression(visitCtx.getInst());
    }

    private TypedExpr dyadicFirstExprObj(DyadicExpression inst) {
        inst.getFirstExpr().accept(this);

        Accessible exprRef = this.refStack.pop();
        LangObject exprVal = this.dataStack.pop();
        return new TypedExpr(inst.getFirstExpr(), exprVal, exprRef);
    }

    private TypedExpr dyadicSecondExprObj(DyadicExpression inst) {
        inst.getSecondExpr().accept(this);

        Accessible exprRef = this.refStack.pop();
        LangObject exprVal = this.dataStack.pop();
        return new TypedExpr(inst.getSecondExpr(), exprVal, exprRef);
    }

    private void _visitDyadicExpression(DyadicExpression inst) {
        String symbol = inst.getSymbol().getValue();

        // Arithmetic operations
        if (symbol.equals("+") || StringUtils.equalsIgnoreCase(symbol, "plus")) {
            TypedExpr result = this.dyadicUtils.plus(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("-") || StringUtils.equalsIgnoreCase(symbol, "minus")) {
            TypedExpr result = this.dyadicUtils.minus(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("*") || StringUtils.equalsIgnoreCase(symbol, "mul")) {
            TypedExpr result = this.dyadicUtils.mul(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("/") || StringUtils.equalsIgnoreCase(symbol, "div")) {
            TypedExpr result = this.dyadicUtils.div(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("%") || StringUtils.equalsIgnoreCase(symbol, "mod")) {
            TypedExpr result = this.dyadicUtils.mod(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        }

        // Comparison operation
        if (symbol.equals("==") || StringUtils.equalsIgnoreCase(symbol, "eq")) {
            TypedExpr result = this.dyadicUtils.eq(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("===") || StringUtils.equalsIgnoreCase(symbol, "ieq")) {
            TypedExpr result = this.dyadicUtils.ieq(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("!=") || StringUtils.equalsIgnoreCase(symbol, "ne")) {
            TypedExpr result = this.dyadicUtils.ne(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals(">") || StringUtils.equalsIgnoreCase(symbol, "gt")) {
            TypedExpr result = this.dyadicUtils.gt(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals(">=") || StringUtils.equalsIgnoreCase(symbol, "ge")) {
            TypedExpr result = this.dyadicUtils.ge(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("<") || StringUtils.equalsIgnoreCase(symbol, "lt")) {
            TypedExpr result = this.dyadicUtils.lt(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("<=") || StringUtils.equalsIgnoreCase(symbol, "le")) {
            TypedExpr result = this.dyadicUtils.le(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        }

        // Bit operation
        if (symbol.equals("&") || StringUtils.equalsIgnoreCase(symbol, "band")) {
            TypedExpr result = this.dyadicUtils.band(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("|") || StringUtils.equalsIgnoreCase(symbol, "bor")) {
            TypedExpr result = this.dyadicUtils.bor(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("^") || StringUtils.equalsIgnoreCase(symbol, "xor")) {
            TypedExpr result = this.dyadicUtils.xor(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals(">>>") || StringUtils.equalsIgnoreCase(symbol, "ushr")) {
            TypedExpr result = this.dyadicUtils.ushr(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals(">>") || StringUtils.equalsIgnoreCase(symbol, "shr")) {
            TypedExpr result = this.dyadicUtils.shr(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("<<") || StringUtils.equalsIgnoreCase(symbol, "shl")) {
            TypedExpr result = this.dyadicUtils.shl(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        }

        // Logical operations
        if (symbol.equals("&&") || StringUtils.equalsIgnoreCase(symbol, "and")) {
            TypedExpr result = this.dyadicUtils.and(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("||") || StringUtils.equalsIgnoreCase(symbol, "or")) {
            TypedExpr result = this.dyadicUtils.or(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        }

        // Matching operation
        if (symbol.equals("in")) {
            TypedExpr result = this.dyadicUtils.in(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            if (testInOrMatchesHasNot(inst)) {
                boolean testResult = (boolean) result.getData().unwrap();
                result = new TypedExpr(inst, new ValueObject(!testResult, TypeType.Boolean), result.getType());
            }
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        } else if (symbol.equals("matches")) {
            TypedExpr result = this.dyadicUtils.matches(inst, dyadicFirstExprObj(inst), dyadicSecondExprObj(inst));
            if (testInOrMatchesHasNot(inst)) {
                boolean testResult = (boolean) result.getData().unwrap();
                result = new TypedExpr(inst, new ValueObject(!testResult, TypeType.Boolean), result.getType());
            }
            this.pushToStack(result.getType(), result.getData(), inst);
            return;
        }

        throw new DetectRuleRuntimeError(inst.getSymbol(), "symbol '" + symbol + "' is not supported");
    }

    private boolean testInOrMatchesHasNot(DyadicExpression inst) {
        if (inst.getSymbol() instanceof DoubleSymbolToken) {
            String lesser = ((DoubleSymbolToken) inst.getSymbol()).getLesser();
            String main = ((DoubleSymbolToken) inst.getSymbol()).getValue();
            boolean isMatch = StringUtils.equalsIgnoreCase(main, "in") || StringUtils.equalsIgnoreCase(main, "matches");
            return isMatch && StringUtils.equalsIgnoreCase(lesser, "not");
        }
        return false;
    }

    @Override
    public void visitTernaryExpression(VisitorContext<TernaryExpression> visitCtx) {
        TernaryExpression inst = visitCtx.getInst();
        Expression testExpr = inst.getTestExpr();
        testExpr.accept(this);

        Accessible testRef = this.refStack.pop();
        LangObject testObj = this.dataStack.pop();

        if (testRef.getTypeType() != TypeType.Boolean) {
            throw new DetectRuleRuntimeError(testExpr, "Ternary test expressions are required Boolean.");
        }

        if ((boolean) testObj.unwrap()) {
            inst.getThenExpr().accept(this);
        } else {
            inst.getElseExpr().accept(this);
        }
    }

    @Override
    public void visitPrivilegeExpression(VisitorContext<PrivilegeExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitBooleanValue(VisitorContext<BooleanValue> visitCtx) {
        BooleanValue inst = visitCtx.getInst();

        TypedExpr typedExpr = accessBooleanValue(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessBooleanValue(BooleanValue inst) {
        LangObject objData = CastUtils.castAsBoolean(inst, TypeType.String, new ValueObject(inst.getValue(), TypeType.String));;
        Type objType = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
        return new TypedExpr(inst, objData, objType);
    }

    @Override
    public void visitDataTimeValue(VisitorContext<DataTimeValue> visitCtx) {
        DataTimeValue inst = visitCtx.getInst();

        TypedExpr typedExpr = accessDataTimeValue(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessDataTimeValue(DataTimeValue inst) {
        StringToken value = inst.getValue();
        StringToken format = inst.getFormat() != null ? inst.getFormat().getFormat() : null;
        DataTimeValueParser valueParser = this.option.getDataTimeValueParser();

        if (format != null && valueParser == null) {
            throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed, DataTimeValueParser provider is undefined.");
        }

        Object resolve = null;
        if (format != null) {
            try {
                resolve = valueParser.resolve(format.getValue(), value.getValue());
            } catch (Exception e) {
                throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed, " + e.getMessage(), e);
            }
        } else {
            String val = value.getValue();
            int valLen = val.length();
            if (resolve == null && valLen >= 19) {
                resolve = CastUtils.tryParse(val, OffsetDateTime::parse);
            }
            if (resolve == null && valLen >= 19) {
                resolve = CastUtils.tryParse(val, LocalDateTime::parse);
            }
            if (resolve == null && valLen >= 19) {
                resolve = CastUtils.tryParse(val, s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (resolve == null && valLen == 10) {
                resolve = CastUtils.tryParse(val, LocalDate::parse);
            }
            if (resolve == null && valLen >= 8) {
                resolve = CastUtils.tryParse(val, OffsetTime::parse);
            }
            if (resolve == null && valLen >= 8) {
                resolve = CastUtils.tryParse(val, LocalTime::parse);
            }
        }

        if (resolve == null) {
            throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed.");
        }

        Class<?> dateClass = resolve.getClass();
        TypeType typeType = TypeType.valueOfType(dateClass);
        if (typeType != TypeType.Date && typeType != TypeType.Time && typeType != TypeType.Datetime) {
            throw new DetectRuleRuntimeError(inst, "the '" + inst + "' data parsing failed.");
        }

        LangObject objData = new ValueObject(resolve, typeType);
        Type objType = ReflectHelper.atomicType(typeType, dateClass);
        return new TypedExpr(inst, objData, objType);
    }

    @Override
    public void visitNullValue(VisitorContext<NullValue> visitCtx) {
        this.refStack.push(ReflectHelper.atomicType(TypeType.Null, null));
        this.dataStack.push(CastUtils.NULL);
    }

    @Override
    public void visitNumberValue(VisitorContext<NumberValue> visitCtx) {
        //infinity
        NumberValue inst = visitCtx.getInst();
        TypedExpr typedExpr = accessNumberValue(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessNumberValue(NumberValue inst) {
        boolean negative = inst.isNegative();
        String value = inst.getValue();

        ValueObject valueObj = null;
        switch (inst.getType()) {
            case Integer:
                try {
                    valueObj = negativeOrNot(negative, Long.parseLong(value));
                } catch (NumberFormatException e) {
                    valueObj = negativeOrNot(negative, new BigDecimal(value));
                }
                break;
            case Float:
                try {
                    valueObj = negativeOrNot(negative, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    valueObj = negativeOrNot(negative, new BigDecimal(value));
                }
                break;
            case Decimal:
                valueObj = negativeOrNot(negative, new BigDecimal(value));
                break;
            default:
                throw new DetectRuleRuntimeError(inst, "the value '" + inst.getValue() + "' is not Number.");
        }

        Type objType = ReflectHelper.atomicType(valueObj.getType(), valueObj.getValue().getClass());
        return new TypedExpr(inst, valueObj, objType);
    }

    @Override
    public void visitXxValue(VisitorContext<XxValue> visitCtx) {
        XxValue inst = visitCtx.getInst();

        TypedExpr typedExpr = accessXxValue(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessXxValue(XxValue inst) {
        String value = StringUtils.trimStart(inst.getValue(), '0');

        ValueObject valueObj = null;
        int dataLen = value.length();
        if (dataLen == 0) {
            valueObj = new ValueObject(0L, TypeType.Integer);
        } else {
            final int bitMaxLength = 63;// "111111111111111111111111111111111111111111111111111111111111111".length();
            final int octMaxLength = 21;// "777777777777777777777".length();
            final int hexMaxLength = 15;// "FFFFFFFFFFFFFFF".length();
            XxType xxType = inst.getType();

            int maxLength = xxType == XxType.Bit ? bitMaxLength : (xxType == XxType.Oct) ? octMaxLength : hexMaxLength;
            switch (xxType) {
                case Bit:
                    if (dataLen <= maxLength) {
                        valueObj = new ValueObject(Long.parseLong(value, 2), TypeType.Integer);
                    } else {
                        valueObj = new ValueObject(new BigDecimal(new BigInteger(value, 2)), TypeType.Decimal);
                    }
                    break;
                case Oct:
                    if (dataLen <= maxLength) {
                        valueObj = new ValueObject(Long.parseLong(value, 8), TypeType.Integer);
                    } else {
                        valueObj = new ValueObject(new BigDecimal(new BigInteger(value, 8)), TypeType.Decimal);
                    }
                    break;
                case Hex:
                    if (dataLen <= maxLength) {
                        valueObj = new ValueObject(Long.parseLong(value, 16), TypeType.Integer);
                    } else {
                        valueObj = new ValueObject(new BigDecimal(new BigInteger(value, 16)), TypeType.Decimal);
                    }
                    break;
                default:
                    throw new DetectRuleRuntimeError(inst, "the value '" + value + "' is not Bit or Oct or Hex.");
            }
        }

        Type objType = ReflectHelper.atomicType(valueObj.getType(), valueObj.getValue().getClass());
        return new TypedExpr(inst, valueObj, objType);
    }

    @Override
    public void visitStringValue(VisitorContext<StringValue> visitCtx) {
        StringValue inst = visitCtx.getInst();

        TypedExpr typedExpr = this.accessStringValue(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessStringValue(StringValue inst) {
        LangObject objData = new ValueObject(inst.getEscapeValue(), TypeType.String);
        Type objType = ReflectHelper.atomicType(TypeType.String, String.class);
        return new TypedExpr(inst, objData, objType);
    }

    @Override
    public void visitListValue(VisitorContext<ListValue> visitCtx) {
        ListValue inst = visitCtx.getInst();

        TypedExpr typedExpr = this.accessListValue(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessListValue(ListValue inst) {
        ArrayObject arrayObject = new ArrayObject();

        List<Expression> collection = inst.getCollection();
        for (Expression expr : collection) {
            expr.accept(this);
            Accessible valueRef = this.refStack.pop();
            LangObject valueData = this.dataStack.pop();

            if (valueData == null) {
                arrayObject.putElement(CastUtils.NULL);
            } else {
                arrayObject.putElement(valueData);
            }
        }

        Type typed = ReflectHelper.arrayType(CollectionAccess.class, Object.class);
        return new TypedExpr(inst, arrayObject, typed);
    }

    @Override
    public void visitObjectValue(VisitorContext<ObjectValue> visitCtx) {
        ObjectValue inst = visitCtx.getInst();

        TypedExpr typedExpr = this.accessObjectValue(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessObjectValue(ObjectValue inst) {
        KeyPairObject keyPairObject = new KeyPairObject();

        List<KeyPairValue> keyPairs = inst.getKeyPairValues();
        for (KeyPairValue keyPair : keyPairs) {
            StringToken pairKey = keyPair.getKey();
            Expression pairValue = keyPair.getValue();
            pairValue.accept(this);

            String key = pairKey.getEscapeValue();
            Accessible valueRef = this.refStack.pop();
            LangObject valueData = this.dataStack.pop();

            if (valueData == null) {
                keyPairObject.putKeyPair(key, CastUtils.NULL);
            } else {
                keyPairObject.putKeyPair(key, valueData);
            }
        }

        Type typed = ReflectHelper.keyPairType(KeyPairAccess.class, Object.class);
        return new TypedExpr(inst, keyPairObject, typed);
    }

    @Override
    public void visitKeyPairValue(VisitorContext<KeyPairValue> visitCtx) {
        // do nothing
    }

    @Override
    public void visitCastExpression(VisitorContext<CastExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitCastExpressionAsType(VisitorContext<CastExpressionAsType> visitCtx) {
        CastExpressionAsType inst = visitCtx.getInst();

        TypedExpr typedExpr = this.accessCastExpressionAsType(inst);
        this.refStack.push(typedExpr.getType());
        this.dataStack.push(typedExpr.getData());
    }

    private TypedExpr accessCastExpressionAsType(CastExpressionAsType inst) {
        CastType castType = inst.getCastType();

        Accessible valueRef = this.refStack.pop();
        LangObject valueData = this.dataStack.pop();
        switch (castType) {
            case Bool:
                if (valueData.unwrap() == null) {
                    valueData = new ValueObject(false, TypeType.Boolean);
                    valueRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
                } else {
                    valueData = CastUtils.castAsBoolean(inst, valueRef.getTypeType(), valueData);
                    valueRef = ReflectHelper.atomicType(TypeType.Boolean, Boolean.TYPE);
                }
                break;
            case Integer:
                if (valueData.unwrap() == null) {
                    valueData = new ValueObject(0L, TypeType.Integer);
                    valueRef = ReflectHelper.atomicType(TypeType.Integer, Long.TYPE);
                } else {
                    valueData = CastUtils.castAsInteger(inst, valueRef.getTypeType(), valueData, inst.getNumFmtToken());
                    valueRef = ReflectHelper.atomicType(TypeType.Integer, Long.TYPE);
                }
                break;
            case Float:
                if (valueData.unwrap() == null) {
                    valueData = new ValueObject(0.0d, TypeType.Float);
                    valueRef = ReflectHelper.atomicType(TypeType.Float, Double.TYPE);
                } else {
                    valueData = CastUtils.castAsFloat(inst, valueRef.getTypeType(), valueData, inst.getNumFmtToken());
                    valueRef = ReflectHelper.atomicType(TypeType.Float, Double.TYPE);
                }
                break;
            case Decimal:
                if (valueData.unwrap() == null) {
                    valueData = new ValueObject(BigDecimal.ZERO, TypeType.Decimal);
                    valueRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                } else {
                    valueData = CastUtils.castAsDecimal(inst, valueRef.getTypeType(), valueData, inst.getNumFmtToken());
                    valueRef = ReflectHelper.atomicType(TypeType.Decimal, BigDecimal.class);
                }
                break;
            case Date:
                valueData = CastUtils.castAsDate(inst, valueRef.getTypeType(), valueData);
                valueRef = ReflectHelper.atomicType(TypeType.Date, LocalDate.class);
                break;
            case Time:
                valueData = CastUtils.castAsTime(inst, valueRef.getTypeType(), valueData);
                valueRef = ReflectHelper.atomicType(TypeType.Time, LocalTime.class);
                break;
            case DateTime:
                valueData = CastUtils.castAsDateTime(inst, valueRef.getTypeType(), valueData);
                valueRef = ReflectHelper.atomicType(TypeType.Datetime, LocalDateTime.class);
                break;
            case DateTimeFormat:
                valueData = CastUtils.castAsDateTimeFormat(inst, valueRef.getTypeType(), valueData, inst.getDtFmtToken(), this.option.getDataTimeValueParser());
                valueRef = ReflectHelper.atomicType(TypeType.Datetime, LocalDateTime.class); // TODO time zone nonsupport
                break;
            case String:
                valueData = CastUtils.castAsString(inst, valueRef.getTypeType(), valueData);
                valueRef = ReflectHelper.atomicType(TypeType.String, String.class);
                break;
            default:
                throw new DetectRuleRuntimeError(inst, "the CastType '" + castType + "' Unsupported.");
        }

        return new TypedExpr(inst, valueData, valueRef);
    }
}
