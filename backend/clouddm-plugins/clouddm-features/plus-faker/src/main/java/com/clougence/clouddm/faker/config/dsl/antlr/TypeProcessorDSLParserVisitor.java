// Generated from TypeProcessorDSLParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.faker.config.dsl.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TypeProcessorDSLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TypeProcessorDSLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#rootInstSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRootInstSet(TypeProcessorDSLParser.RootInstSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#defineInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineInst(TypeProcessorDSLParser.DefineInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#defineAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineAlias(TypeProcessorDSLParser.DefineAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#defineConf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineConf(TypeProcessorDSLParser.DefineConfContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#typeSetInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSetInst(TypeProcessorDSLParser.TypeSetInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#typeInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeInst(TypeProcessorDSLParser.TypeInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#colTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColTypeName(TypeProcessorDSLParser.ColTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#colTypeConf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColTypeConf(TypeProcessorDSLParser.ColTypeConfContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#flowName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowName(TypeProcessorDSLParser.FlowNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#anyValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyValue(TypeProcessorDSLParser.AnyValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(TypeProcessorDSLParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringValue(TypeProcessorDSLParser.StringValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(TypeProcessorDSLParser.NullValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(TypeProcessorDSLParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberValue(TypeProcessorDSLParser.NumberValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeValue}
	 * labeled alternative in {@link TypeProcessorDSLParser#baseValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeValue(TypeProcessorDSLParser.TypeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#extValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtValue(TypeProcessorDSLParser.ExtValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#envValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnvValue(TypeProcessorDSLParser.EnvValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#listValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListValue(TypeProcessorDSLParser.ListValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#objectValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectValue(TypeProcessorDSLParser.ObjectValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#objectItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectItem(TypeProcessorDSLParser.ObjectItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeProcessorDSLParser#idStr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdStr(TypeProcessorDSLParser.IdStrContext ctx);
}