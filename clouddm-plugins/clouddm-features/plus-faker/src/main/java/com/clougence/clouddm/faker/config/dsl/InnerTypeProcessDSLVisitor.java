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
package com.clougence.clouddm.faker.config.dsl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.faker.config.dsl.antlr.TypeProcessorDSLParserBaseVisitor;
import com.clougence.clouddm.faker.config.dsl.antlr.TypeProcessorDSLParser.*;
import com.clougence.clouddm.faker.config.dsl.model.*;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * DSL 解析器
 * @version : 2023-02-10
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
class InnerTypeProcessDSLVisitor extends TypeProcessorDSLParserBaseVisitor<TypeProcessConfSet> {

    private final Stack<Object>      instStack   = new Stack<>();
    private final TypeProcessConfSet confSet     = new TypeProcessConfSet();
    private List<String>             colTypeList = new ArrayList<>();

    public TypeProcessConfSet getConfSet() { return this.confSet; }

    @Override
    public TypeProcessConfSet visitRootInstSet(RootInstSetContext ctx) {
        super.visitRootInstSet(ctx);
        this.confSet.finishParser();
        return this.confSet;
    }

    @Override
    public TypeProcessConfSet visitDefineInst(DefineInstContext ctx) {
        ctx.idStr().accept(this);

        String strDsType = (String) this.instStack.pop();
        DataSourceType dsType;
        try {
            dsType = DataSourceType.getTypeByName(strDsType);
        } catch (UnsupportedOperationException e) {
            log.error("Unsupported dsType for '" + strDsType + "'");
            dsType = null;
        }

        this.confSet.putDbType(dsType);

        if (ctx.ALIAS() != null) {
            for (IdStrContext aliasNames : ctx.defineAlias().idStr()) {
                aliasNames.accept(this);
                dsType = DataSourceType.getTypeByName((String) this.instStack.pop());
                this.confSet.putDbType(dsType);
            }
        }

        List<DefineConfContext> defConfList = ctx.defineConf();
        for (DefineConfContext conf : defConfList) {
            conf.accept(this);
        }

        return null;
    }

    @Override
    public TypeProcessConfSet visitDefineConf(DefineConfContext ctx) {
        ctx.idStr().accept(this);
        String defConfName = (String) this.instStack.pop();

        ctx.anyValue().accept(this);
        DataModel defConfVal = (DataModel) this.instStack.pop();

        confSet.putDefConfig(defConfName, defConfVal);
        return null;
    }

    @Override
    public TypeProcessConfSet visitTypeInst(TypeInstContext ctx) {
        ctx.colTypeName().accept(this);
        String colType = (String) this.instStack.pop();
        this.colTypeList.add(colType);

        boolean configSet = CollectionUtils.isNotEmpty(ctx.colTypeConf());
        boolean flowSpecial = ctx.FOLLOW() != null;
        boolean throwMsg = ctx.THROW() != null;

        if (configSet && !flowSpecial && !throwMsg) {

            this.instStack.push(new ArrayList<TypeProcessConf>());
            List<ColTypeConfContext> confContexts = ctx.colTypeConf();
            for (ColTypeConfContext conf : confContexts) {
                conf.accept(this);
            }

            List<TypeProcessConf> confList = (List<TypeProcessConf>) this.instStack.pop();
            for (String typeName : this.colTypeList) {
                this.confSet.putConfig(typeName, confList);
            }
            this.colTypeList.clear();

        } else if (!configSet && flowSpecial && !throwMsg) {

            ctx.flowName().accept(this);
            String flowName = (String) this.instStack.pop();
            List<TypeProcessConf> refConfList = this.confSet.getConfig(flowName);
            refConfList = refConfList == null ? new ArrayList<>() : refConfList;

            for (String typeName : this.colTypeList) {
                this.confSet.putConfig(typeName, refConfList);
            }
            this.colTypeList.clear();

        } else if (!configSet && !flowSpecial && throwMsg) {

            String throwString = fixIdOrStr(ctx.STRING());
            for (String typeName : this.colTypeList) {
                this.confSet.putThrow(typeName, throwString);
            }
            this.colTypeList.clear();

        } else if (!configSet && !flowSpecial && !throwMsg) {
            // do nothing
        } else {
            throw new UnsupportedOperationException();
        }

        return null;
    }

    @Override
    public TypeProcessConfSet visitColTypeName(ColTypeNameContext ctx) {
        if (ctx.ALL() != null) {
            this.instStack.push("*");
        } else {
            ctx.idStr().accept(this);
            String typeName = (String) this.instStack.pop();
            this.instStack.push(typeName);
        }
        return null;
    }

    @Override
    public TypeProcessConfSet visitColTypeConf(ColTypeConfContext ctx) {
        ctx.idStr().accept(this);
        String confName = (String) this.instStack.pop();

        boolean useAppend = ctx.APPEND() != null;

        ctx.anyValue().accept(this);
        DataModel confValue = (DataModel) this.instStack.pop();

        List<Object> configList = (List<Object>) this.instStack.peek();
        configList.add(new TypeProcessConf(confName, useAppend, confValue));
        return null;
    }

    @Override
    public TypeProcessConfSet visitExtValue(ExtValueContext ctx) {
        TerminalNode sizeNode = ctx.SIZE();
        TerminalNode identifierNode = ctx.IDENTIFIER();

        if (sizeNode != null) {
            String sizeText = sizeNode.getText();
            if (StringUtils.endsWithIgnoreCase(sizeText, "mb")) {
                int value = Integer.parseInt(sizeText.substring(0, sizeText.length() - 2)) * 1024 * 1024;
                this.instStack.push(new ValueModel(BigInteger.valueOf(value)));
            } else if (StringUtils.endsWithIgnoreCase(sizeText, "kb")) {
                int value = Integer.parseInt(sizeText.substring(0, sizeText.length() - 2)) * 1024;
                this.instStack.push(new ValueModel(BigInteger.valueOf(value)));
            } else if (StringUtils.endsWithIgnoreCase(sizeText, "b")) {
                int value = Integer.parseInt(sizeText.substring(0, sizeText.length() - 1));
                this.instStack.push(new ValueModel(BigInteger.valueOf(value)));
            } else {
                this.instStack.push(new ValueModel(BigInteger.ZERO));
            }
        } else if (identifierNode != null) {
            this.instStack.push(new ValueModel(fixIdOrStr(identifierNode)));
        } else {
            this.instStack.push(ValueModel.NULL);
        }
        return null;
    }

    @Override
    public TypeProcessConfSet visitFuncCall(FuncCallContext ctx) {
        List<DataModel> params = new ArrayList<>();
        for (AnyValueContext anyValue : ctx.anyValue()) {
            anyValue.accept(this);
            params.add((DataModel) this.instStack.pop());
        }

        String fooName = fixIdOrStr(ctx.IDENTIFIER());
        this.instStack.push(new FunctionModel(fooName, params));
        return null;
    }

    @Override
    public TypeProcessConfSet visitEnvValue(EnvValueContext ctx) {
        ctx.idStr().accept(this);
        String envString = (String) this.instStack.pop();
        this.instStack.push(new OgnlModel(envString));
        return null;
    }

    @Override
    public TypeProcessConfSet visitListValue(ListValueContext ctx) {
        ListModel value = new ListModel();

        for (AnyValueContext anyValue : ctx.anyValue()) {
            anyValue.accept(this);
            value.add((DataModel) this.instStack.pop());
        }

        this.instStack.push(value);
        return null;
    }

    @Override
    public TypeProcessConfSet visitObjectValue(ObjectValueContext ctx) {
        this.instStack.push(new ObjectModel());
        return super.visitObjectValue(ctx);
    }

    @Override
    public TypeProcessConfSet visitObjectItem(ObjectItemContext ctx) {
        ctx.idStr().accept(this);
        String key = (String) this.instStack.pop();

        ctx.anyValue().accept(this);
        DataModel value = (DataModel) this.instStack.pop();

        ((ObjectModel) this.instStack.peek()).put(key, value);
        return null;
    }

    @Override
    public TypeProcessConfSet visitStringValue(StringValueContext ctx) {
        String string = fixIdOrStr(ctx.STRING());
        this.instStack.push(new ValueModel(string));
        return null;
    }

    @Override
    public TypeProcessConfSet visitNullValue(NullValueContext ctx) {
        this.instStack.push(ValueModel.NULL);
        return null;
    }

    @Override
    public TypeProcessConfSet visitBooleanValue(BooleanValueContext ctx) {
        boolean boolValue = ctx.TRUE() != null;
        this.instStack.push(boolValue ? ValueModel.TRUE : ValueModel.FALSE);
        return null;
    }

    @Override
    public TypeProcessConfSet visitNumberValue(NumberValueContext ctx) {
        TerminalNode bitNode = ctx.BIT_NUM();
        TerminalNode octNode = ctx.OCT_NUM();
        TerminalNode intNode = ctx.INTEGER_NUM();
        TerminalNode hexNode = ctx.HEX_NUM();
        TerminalNode decimalNode = ctx.DECIMAL_NUM();
        //
        int radix = 10;
        String radixNumber = null;
        if (bitNode != null) {
            radix = 2;
            radixNumber = bitNode.getText();
            radixNumber = (radixNumber.charAt(0) == '-') ? ("-" + radixNumber.substring(3)) : radixNumber.substring(2);
        }
        if (octNode != null) {
            radix = 8;
            radixNumber = octNode.getText();
            radixNumber = (radixNumber.charAt(0) == '-') ? ("-" + radixNumber.substring(3)) : radixNumber.substring(2);
        }
        if (intNode != null) {
            radix = 10;
            radixNumber = intNode.getText();
        }
        if (hexNode != null) {
            radix = 16;
            radixNumber = hexNode.getText();
            radixNumber = (radixNumber.charAt(0) == '-') ? ("-" + radixNumber.substring(3)) : radixNumber.substring(2);
        }
        if (radixNumber != null) {
            BigInteger bigInt = new BigInteger(radixNumber, radix);
            this.instStack.push(new ValueModel(bigInt));
            return null;
        } else {
            BigDecimal bigDec = new BigDecimal(decimalNode.getText());
            this.instStack.push(new ValueModel(bigDec));
            return null;
        }
    }

    @Override
    public TypeProcessConfSet visitTypeValue(TypeValueContext ctx) {
        this.instStack.push(new ValueModel(ctx.TYPE().getText()));
        return null;
    }

    @Override
    public TypeProcessConfSet visitIdStr(IdStrContext ctx) {
        TerminalNode strNode = ctx.STRING();
        TerminalNode typeNode = ctx.TYPE();
        TerminalNode identifierNode = ctx.IDENTIFIER();

        if (strNode != null) {
            this.instStack.push(fixIdOrStr(strNode));
        }
        if (typeNode != null) {
            this.instStack.push(ctx.TYPE().getText());
        }
        if (identifierNode != null) {
            this.instStack.push(fixIdOrStr(identifierNode));
        }

        return null;
    }

    private String fixIdOrStr(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        char firstChar = nodeText.charAt(0);
        char lastChar = nodeText.charAt(nodeText.length() - 1);
        if ((firstChar == '"' && lastChar == '"') || (firstChar == '\'' && lastChar == '\'') || (firstChar == '`' && lastChar == '`')) {
            return nodeText.substring(1, nodeText.length() - 1);
        } else {
            return nodeText;
        }
    }
}
