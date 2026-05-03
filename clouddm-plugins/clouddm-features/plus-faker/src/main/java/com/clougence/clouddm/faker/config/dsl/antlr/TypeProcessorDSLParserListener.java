// Generated from TypeProcessorDSLParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.faker.config.dsl.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TypeProcessorDSLParser}.
 */
public interface TypeProcessorDSLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#rootInstSet}.
	 * @param ctx the parse tree
	 */
	void enterRootInstSet(TypeProcessorDSLParser.RootInstSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#rootInstSet}.
	 * @param ctx the parse tree
	 */
	void exitRootInstSet(TypeProcessorDSLParser.RootInstSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#defineInst}.
	 * @param ctx the parse tree
	 */
	void enterDefineInst(TypeProcessorDSLParser.DefineInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#defineInst}.
	 * @param ctx the parse tree
	 */
	void exitDefineInst(TypeProcessorDSLParser.DefineInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#defineAlias}.
	 * @param ctx the parse tree
	 */
	void enterDefineAlias(TypeProcessorDSLParser.DefineAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#defineAlias}.
	 * @param ctx the parse tree
	 */
	void exitDefineAlias(TypeProcessorDSLParser.DefineAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#defineConf}.
	 * @param ctx the parse tree
	 */
	void enterDefineConf(TypeProcessorDSLParser.DefineConfContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#defineConf}.
	 * @param ctx the parse tree
	 */
	void exitDefineConf(TypeProcessorDSLParser.DefineConfContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#typeSetInst}.
	 * @param ctx the parse tree
	 */
	void enterTypeSetInst(TypeProcessorDSLParser.TypeSetInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#typeSetInst}.
	 * @param ctx the parse tree
	 */
	void exitTypeSetInst(TypeProcessorDSLParser.TypeSetInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#typeInst}.
	 * @param ctx the parse tree
	 */
	void enterTypeInst(TypeProcessorDSLParser.TypeInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#typeInst}.
	 * @param ctx the parse tree
	 */
	void exitTypeInst(TypeProcessorDSLParser.TypeInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#colTypeName}.
	 * @param ctx the parse tree
	 */
	void enterColTypeName(TypeProcessorDSLParser.ColTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#colTypeName}.
	 * @param ctx the parse tree
	 */
	void exitColTypeName(TypeProcessorDSLParser.ColTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#colTypeConf}.
	 * @param ctx the parse tree
	 */
	void enterColTypeConf(TypeProcessorDSLParser.ColTypeConfContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#colTypeConf}.
	 * @param ctx the parse tree
	 */
	void exitColTypeConf(TypeProcessorDSLParser.ColTypeConfContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#flowName}.
	 * @param ctx the parse tree
	 */
	void enterFlowName(TypeProcessorDSLParser.FlowNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#flowName}.
	 * @param ctx the parse tree
	 */
	void exitFlowName(TypeProcessorDSLParser.FlowNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#anyValue}.
	 * @param ctx the parse tree
	 */
	void enterAnyValue(TypeProcessorDSLParser.AnyValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#anyValue}.
	 * @param ctx the parse tree
	 */
	void exitAnyValue(TypeProcessorDSLParser.AnyValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(TypeProcessorDSLParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(TypeProcessorDSLParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void enterStringValue(TypeProcessorDSLParser.StringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void exitStringValue(TypeProcessorDSLParser.StringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void enterNullValue(TypeProcessorDSLParser.NullValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void exitNullValue(TypeProcessorDSLParser.NullValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(TypeProcessorDSLParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(TypeProcessorDSLParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void enterNumberValue(TypeProcessorDSLParser.NumberValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void exitNumberValue(TypeProcessorDSLParser.NumberValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void enterTypeValue(TypeProcessorDSLParser.TypeValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 */
	void exitTypeValue(TypeProcessorDSLParser.TypeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#extValue}.
	 * @param ctx the parse tree
	 */
	void enterExtValue(TypeProcessorDSLParser.ExtValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#extValue}.
	 * @param ctx the parse tree
	 */
	void exitExtValue(TypeProcessorDSLParser.ExtValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#envValue}.
	 * @param ctx the parse tree
	 */
	void enterEnvValue(TypeProcessorDSLParser.EnvValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#envValue}.
	 * @param ctx the parse tree
	 */
	void exitEnvValue(TypeProcessorDSLParser.EnvValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#listValue}.
	 * @param ctx the parse tree
	 */
	void enterListValue(TypeProcessorDSLParser.ListValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#listValue}.
	 * @param ctx the parse tree
	 */
	void exitListValue(TypeProcessorDSLParser.ListValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#objectValue}.
	 * @param ctx the parse tree
	 */
	void enterObjectValue(TypeProcessorDSLParser.ObjectValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#objectValue}.
	 * @param ctx the parse tree
	 */
	void exitObjectValue(TypeProcessorDSLParser.ObjectValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#objectItem}.
	 * @param ctx the parse tree
	 */
	void enterObjectItem(TypeProcessorDSLParser.ObjectItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#objectItem}.
	 * @param ctx the parse tree
	 */
	void exitObjectItem(TypeProcessorDSLParser.ObjectItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeProcessorDSLParser#idStr}.
	 * @param ctx the parse tree
	 */
	void enterIdStr(TypeProcessorDSLParser.IdStrContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeProcessorDSLParser#idStr}.
	 * @param ctx the parse tree
	 */
	void exitIdStr(TypeProcessorDSLParser.IdStrContext ctx);
}