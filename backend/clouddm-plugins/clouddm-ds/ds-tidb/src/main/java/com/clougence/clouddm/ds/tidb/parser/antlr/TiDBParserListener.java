// Generated from TiDBParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.tidb.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TiDBParser}.
 */
public interface TiDBParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TiDBParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDdlStatement(TiDBParser.DdlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#ddlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDdlStatement(TiDBParser.DdlStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEngine(TiDBParser.TableOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEngine}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEngine(TiDBParser.TableOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAutoIncrement(TiDBParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAutoIncrement}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAutoIncrement(TiDBParser.TableOptionAutoIncrementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionAverage(TiDBParser.TableOptionAverageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionAverage}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionAverage(TiDBParser.TableOptionAverageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCharset(TiDBParser.TableOptionCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCharset}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCharset(TiDBParser.TableOptionCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionChecksum(TiDBParser.TableOptionChecksumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionChecksum}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionChecksum(TiDBParser.TableOptionChecksumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCollate(TiDBParser.TableOptionCollateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCollate}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCollate(TiDBParser.TableOptionCollateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionComment(TiDBParser.TableOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionComment}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionComment(TiDBParser.TableOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionCompression(TiDBParser.TableOptionCompressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionCompression}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionCompression(TiDBParser.TableOptionCompressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionConnection(TiDBParser.TableOptionConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionConnection}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionConnection(TiDBParser.TableOptionConnectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDataDirectory(TiDBParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDataDirectory}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDataDirectory(TiDBParser.TableOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionDelay(TiDBParser.TableOptionDelayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionDelay}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionDelay(TiDBParser.TableOptionDelayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionEncryption(TiDBParser.TableOptionEncryptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionEncryption}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionEncryption(TiDBParser.TableOptionEncryptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionIndexDirectory(TiDBParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionIndexDirectory}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionIndexDirectory(TiDBParser.TableOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionInsertMethod(TiDBParser.TableOptionInsertMethodContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionInsertMethod}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionInsertMethod(TiDBParser.TableOptionInsertMethodContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionKeyBlockSize(TiDBParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionKeyBlockSize}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionKeyBlockSize(TiDBParser.TableOptionKeyBlockSizeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMaxRows(TiDBParser.TableOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMaxRows}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMaxRows(TiDBParser.TableOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionMinRows(TiDBParser.TableOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionMinRows}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionMinRows(TiDBParser.TableOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPackKeys(TiDBParser.TableOptionPackKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPackKeys}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPackKeys(TiDBParser.TableOptionPackKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPassword(TiDBParser.TableOptionPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPassword}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPassword(TiDBParser.TableOptionPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRowFormat(TiDBParser.TableOptionRowFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRowFormat}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRowFormat(TiDBParser.TableOptionRowFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionRecalculation(TiDBParser.TableOptionRecalculationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionRecalculation}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionRecalculation(TiDBParser.TableOptionRecalculationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionPersistent(TiDBParser.TableOptionPersistentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionPersistent}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionPersistent(TiDBParser.TableOptionPersistentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionSamplePage(TiDBParser.TableOptionSamplePageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionSamplePage}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionSamplePage(TiDBParser.TableOptionSamplePageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTablespace(TiDBParser.TableOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTablespace}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTablespace(TiDBParser.TableOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTableType(TiDBParser.TableOptionTableTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTableType}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTableType(TiDBParser.TableOptionTableTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionTransactional(TiDBParser.TableOptionTransactionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionTransactional}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionTransactional(TiDBParser.TableOptionTransactionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTableOptionUnion(TiDBParser.TableOptionUnionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableOptionUnion}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTableOptionUnion(TiDBParser.TableOptionUnionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tidbTableOptionTTL}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void enterTidbTableOptionTTL(TiDBParser.TidbTableOptionTTLContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tidbTableOptionTTL}
	 * labeled alternative in {@link TiDBParser#tableOption}.
	 * @param ctx the parse tree
	 */
	void exitTidbTableOptionTTL(TiDBParser.TidbTableOptionTTLContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterNullColumnConstraint(TiDBParser.NullColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitNullColumnConstraint(TiDBParser.NullColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterDefaultColumnConstraint(TiDBParser.DefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitDefaultColumnConstraint(TiDBParser.DefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterInvisibleColumnConstraint(TiDBParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code invisibleColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitInvisibleColumnConstraint(TiDBParser.InvisibleColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterAutoIncrementColumnConstraint(TiDBParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code autoIncrementColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitAutoIncrementColumnConstraint(TiDBParser.AutoIncrementColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyColumnConstraint(TiDBParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyColumnConstraint(TiDBParser.PrimaryKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyColumnConstraint(TiDBParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyColumnConstraint(TiDBParser.UniqueKeyColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCommentColumnConstraint(TiDBParser.CommentColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCommentColumnConstraint(TiDBParser.CommentColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterFormatColumnConstraint(TiDBParser.FormatColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code formatColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitFormatColumnConstraint(TiDBParser.FormatColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterStorageColumnConstraint(TiDBParser.StorageColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code storageColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitStorageColumnConstraint(TiDBParser.StorageColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterReferenceColumnConstraint(TiDBParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code referenceColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitReferenceColumnConstraint(TiDBParser.ReferenceColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCollateColumnConstraint(TiDBParser.CollateColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCollateColumnConstraint(TiDBParser.CollateColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterGeneratedColumnConstraint(TiDBParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code generatedColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitGeneratedColumnConstraint(TiDBParser.GeneratedColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterSerialDefaultColumnConstraint(TiDBParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code serialDefaultColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitSerialDefaultColumnConstraint(TiDBParser.SerialDefaultColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckColumnConstraint(TiDBParser.CheckColumnConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkColumnConstraint}
	 * labeled alternative in {@link TiDBParser#columnConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckColumnConstraint(TiDBParser.CheckColumnConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryKeyTableConstraint(TiDBParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryKeyTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryKeyTableConstraint(TiDBParser.PrimaryKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterUniqueKeyTableConstraint(TiDBParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uniqueKeyTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitUniqueKeyTableConstraint(TiDBParser.UniqueKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterForeignKeyTableConstraint(TiDBParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code foreignKeyTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitForeignKeyTableConstraint(TiDBParser.ForeignKeyTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableConstraint(TiDBParser.CheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code checkTableConstraint}
	 * labeled alternative in {@link TiDBParser#tableConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableConstraint(TiDBParser.CheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createSequence}.
	 * @param ctx the parse tree
	 */
	void enterCreateSequence(TiDBParser.CreateSequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createSequence}.
	 * @param ctx the parse tree
	 */
	void exitCreateSequence(TiDBParser.CreateSequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropSequence}.
	 * @param ctx the parse tree
	 */
	void enterDropSequence(TiDBParser.DropSequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropSequence}.
	 * @param ctx the parse tree
	 */
	void exitDropSequence(TiDBParser.DropSequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#secquenceCount}.
	 * @param ctx the parse tree
	 */
	void enterSecquenceCount(TiDBParser.SecquenceCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#secquenceCount}.
	 * @param ctx the parse tree
	 */
	void exitSecquenceCount(TiDBParser.SecquenceCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#sequence_name}.
	 * @param ctx the parse tree
	 */
	void enterSequence_name(TiDBParser.Sequence_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#sequence_name}.
	 * @param ctx the parse tree
	 */
	void exitSequence_name(TiDBParser.Sequence_nameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionHash(TiDBParser.PartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionHash}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionHash(TiDBParser.PartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionKey(TiDBParser.PartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionKey}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionKey(TiDBParser.PartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionRange(TiDBParser.PartitionFunctionRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionRange}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionRange(TiDBParser.PartitionFunctionRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionFunctionList(TiDBParser.PartitionFunctionListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionFunctionList}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionFunctionList(TiDBParser.PartitionFunctionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tiIntervalPartition}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTiIntervalPartition(TiDBParser.TiIntervalPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tiIntervalPartition}
	 * labeled alternative in {@link TiDBParser#partitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTiIntervalPartition(TiDBParser.TiIntervalPartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinitions(TiDBParser.PartitionDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinitions(TiDBParser.PartitionDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(TiDBParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(TiDBParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatements(TiDBParser.SqlStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#sqlStatements}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatements(TiDBParser.SqlStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatement(TiDBParser.SqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatement(TiDBParser.SqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(TiDBParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(TiDBParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDmlStatement(TiDBParser.DmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDmlStatement(TiDBParser.DmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterTransactionStatement(TiDBParser.TransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#transactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitTransactionStatement(TiDBParser.TransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#replicationStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplicationStatement(TiDBParser.ReplicationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#replicationStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplicationStatement(TiDBParser.ReplicationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#preparedStatement}.
	 * @param ctx the parse tree
	 */
	void enterPreparedStatement(TiDBParser.PreparedStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#preparedStatement}.
	 * @param ctx the parse tree
	 */
	void exitPreparedStatement(TiDBParser.PreparedStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(TiDBParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(TiDBParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void enterAdministrationStatement(TiDBParser.AdministrationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#administrationStatement}.
	 * @param ctx the parse tree
	 */
	void exitAdministrationStatement(TiDBParser.AdministrationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#utilityStatement}.
	 * @param ctx the parse tree
	 */
	void enterUtilityStatement(TiDBParser.UtilityStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#utilityStatement}.
	 * @param ctx the parse tree
	 */
	void exitUtilityStatement(TiDBParser.UtilityStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(TiDBParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(TiDBParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void enterDatabaseName(TiDBParser.DatabaseNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#databaseName}.
	 * @param ctx the parse tree
	 */
	void exitDatabaseName(TiDBParser.DatabaseNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void enterCreateEvent(TiDBParser.CreateEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void exitCreateEvent(TiDBParser.CreateEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndex(TiDBParser.CreateIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndex(TiDBParser.CreateIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#indexName}.
	 * @param ctx the parse tree
	 */
	void enterIndexName(TiDBParser.IndexNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#indexName}.
	 * @param ctx the parse tree
	 */
	void exitIndexName(TiDBParser.IndexNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterCreateLogfileGroup(TiDBParser.CreateLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitCreateLogfileGroup(TiDBParser.CreateLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcedure(TiDBParser.CreateProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcedure(TiDBParser.CreateProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunction(TiDBParser.CreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunction(TiDBParser.CreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createRole}.
	 * @param ctx the parse tree
	 */
	void enterCreateRole(TiDBParser.CreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createRole}.
	 * @param ctx the parse tree
	 */
	void exitCreateRole(TiDBParser.CreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createServer}.
	 * @param ctx the parse tree
	 */
	void enterCreateServer(TiDBParser.CreateServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createServer}.
	 * @param ctx the parse tree
	 */
	void exitCreateServer(TiDBParser.CreateServerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link TiDBParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterCopyCreateTable(TiDBParser.CopyCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code copyCreateTable}
	 * labeled alternative in {@link TiDBParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitCopyCreateTable(TiDBParser.CopyCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link TiDBParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterQueryCreateTable(TiDBParser.QueryCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryCreateTable}
	 * labeled alternative in {@link TiDBParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitQueryCreateTable(TiDBParser.QueryCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link TiDBParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterColumnCreateTable(TiDBParser.ColumnCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnCreateTable}
	 * labeled alternative in {@link TiDBParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitColumnCreateTable(TiDBParser.ColumnCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void enterTemporary_(TiDBParser.Temporary_Context ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#temporary_}.
	 * @param ctx the parse tree
	 */
	void exitTemporary_(TiDBParser.Temporary_Context ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceInnodb(TiDBParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceInnodb(TiDBParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceNdb(TiDBParser.CreateTablespaceNdbContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceNdb(TiDBParser.CreateTablespaceNdbContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void enterCreateTrigger(TiDBParser.CreateTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void exitCreateTrigger(TiDBParser.CreateTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createView}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(TiDBParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createView}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(TiDBParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabaseOption(TiDBParser.CreateDatabaseOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createDatabaseOption}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabaseOption(TiDBParser.CreateDatabaseOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void enterOwnerStatement(TiDBParser.OwnerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void exitOwnerStatement(TiDBParser.OwnerStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link TiDBParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreciseSchedule(TiDBParser.PreciseScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code preciseSchedule}
	 * labeled alternative in {@link TiDBParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreciseSchedule(TiDBParser.PreciseScheduleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link TiDBParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalSchedule(TiDBParser.IntervalScheduleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalSchedule}
	 * labeled alternative in {@link TiDBParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalSchedule(TiDBParser.IntervalScheduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void enterTimestampValue(TiDBParser.TimestampValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void exitTimestampValue(TiDBParser.TimestampValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpr(TiDBParser.IntervalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#intervalExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpr(TiDBParser.IntervalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void enterIntervalType(TiDBParser.IntervalTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#intervalType}.
	 * @param ctx the parse tree
	 */
	void exitIntervalType(TiDBParser.IntervalTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#enableType}.
	 * @param ctx the parse tree
	 */
	void enterEnableType(TiDBParser.EnableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#enableType}.
	 * @param ctx the parse tree
	 */
	void exitEnableType(TiDBParser.EnableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#indexType}.
	 * @param ctx the parse tree
	 */
	void enterIndexType(TiDBParser.IndexTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#indexType}.
	 * @param ctx the parse tree
	 */
	void exitIndexType(TiDBParser.IndexTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void enterIndexOption(TiDBParser.IndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void exitIndexOption(TiDBParser.IndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void enterProcedureParameter(TiDBParser.ProcedureParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void exitProcedureParameter(TiDBParser.ProcedureParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParameter(TiDBParser.FunctionParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#functionParameter}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParameter(TiDBParser.FunctionParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineComment(TiDBParser.RoutineCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineComment}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineComment(TiDBParser.RoutineCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineLanguage(TiDBParser.RoutineLanguageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineLanguage}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineLanguage(TiDBParser.RoutineLanguageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBehavior(TiDBParser.RoutineBehaviorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineBehavior}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBehavior(TiDBParser.RoutineBehaviorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineData(TiDBParser.RoutineDataContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineData}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineData(TiDBParser.RoutineDataContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineSecurity(TiDBParser.RoutineSecurityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routineSecurity}
	 * labeled alternative in {@link TiDBParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineSecurity(TiDBParser.RoutineSecurityContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void enterServerOption(TiDBParser.ServerOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void exitServerOption(TiDBParser.ServerOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterCreateDefinitions(TiDBParser.CreateDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitCreateDefinitions(TiDBParser.CreateDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link TiDBParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDeclaration(TiDBParser.ColumnDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnDeclaration}
	 * labeled alternative in {@link TiDBParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDeclaration(TiDBParser.ColumnDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link TiDBParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstraintDeclaration(TiDBParser.ConstraintDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintDeclaration}
	 * labeled alternative in {@link TiDBParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstraintDeclaration(TiDBParser.ConstraintDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link TiDBParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void enterIndexDeclaration(TiDBParser.IndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexDeclaration}
	 * labeled alternative in {@link TiDBParser#createDefinition}.
	 * @param ctx the parse tree
	 */
	void exitIndexDeclaration(TiDBParser.IndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefinition(TiDBParser.ColumnDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefinition(TiDBParser.ColumnDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterReferenceDefinition(TiDBParser.ReferenceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitReferenceDefinition(TiDBParser.ReferenceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void enterReferenceAction(TiDBParser.ReferenceActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#referenceAction}.
	 * @param ctx the parse tree
	 */
	void exitReferenceAction(TiDBParser.ReferenceActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void enterReferenceControlType(TiDBParser.ReferenceControlTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#referenceControlType}.
	 * @param ctx the parse tree
	 */
	void exitReferenceControlType(TiDBParser.ReferenceControlTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link TiDBParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSimpleIndexDeclaration(TiDBParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleIndexDeclaration}
	 * labeled alternative in {@link TiDBParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSimpleIndexDeclaration(TiDBParser.SimpleIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link TiDBParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSpecialIndexDeclaration(TiDBParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialIndexDeclaration}
	 * labeled alternative in {@link TiDBParser#indexColumnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSpecialIndexDeclaration(TiDBParser.SpecialIndexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tableType}.
	 * @param ctx the parse tree
	 */
	void enterTableType(TiDBParser.TableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tableType}.
	 * @param ctx the parse tree
	 */
	void exitTableType(TiDBParser.TableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void enterTablespaceStorage(TiDBParser.TablespaceStorageContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tablespaceStorage}.
	 * @param ctx the parse tree
	 */
	void exitTablespaceStorage(TiDBParser.TablespaceStorageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link TiDBParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionHash(TiDBParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionHash}
	 * labeled alternative in {@link TiDBParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionHash(TiDBParser.SubPartitionFunctionHashContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link TiDBParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitionFunctionKey(TiDBParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subPartitionFunctionKey}
	 * labeled alternative in {@link TiDBParser#subpartitionFunctionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitionFunctionKey(TiDBParser.SubPartitionFunctionKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionComparision(TiDBParser.PartitionComparisionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionComparision}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionComparision(TiDBParser.PartitionComparisionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListAtom(TiDBParser.PartitionListAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListAtom}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListAtom(TiDBParser.PartitionListAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionListVector(TiDBParser.PartitionListVectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionListVector}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionListVector(TiDBParser.PartitionListVectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSimple(TiDBParser.PartitionSimpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionSimple}
	 * labeled alternative in {@link TiDBParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSimple(TiDBParser.PartitionSimpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerAtom(TiDBParser.PartitionDefinerAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#partitionDefinerAtom}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerAtom(TiDBParser.PartitionDefinerAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinerVector(TiDBParser.PartitionDefinerVectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#partitionDefinerVector}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinerVector(TiDBParser.PartitionDefinerVectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubpartitionDefinition(TiDBParser.SubpartitionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubpartitionDefinition(TiDBParser.SubpartitionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionEngine(TiDBParser.PartitionOptionEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionEngine}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionEngine(TiDBParser.PartitionOptionEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionComment(TiDBParser.PartitionOptionCommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionComment}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionComment(TiDBParser.PartitionOptionCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionDataDirectory(TiDBParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionDataDirectory}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionDataDirectory(TiDBParser.PartitionOptionDataDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionIndexDirectory(TiDBParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionIndexDirectory}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionIndexDirectory(TiDBParser.PartitionOptionIndexDirectoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMaxRows(TiDBParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMaxRows}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMaxRows(TiDBParser.PartitionOptionMaxRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionMinRows(TiDBParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionMinRows}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionMinRows(TiDBParser.PartitionOptionMinRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionTablespace(TiDBParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionTablespace}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionTablespace(TiDBParser.PartitionOptionTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionOptionNodeGroup(TiDBParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partitionOptionNodeGroup}
	 * labeled alternative in {@link TiDBParser#partitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionOptionNodeGroup(TiDBParser.PartitionOptionNodeGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link TiDBParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterSimpleDatabase(TiDBParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterSimpleDatabase}
	 * labeled alternative in {@link TiDBParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterSimpleDatabase(TiDBParser.AlterSimpleDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link TiDBParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterUpgradeName(TiDBParser.AlterUpgradeNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUpgradeName}
	 * labeled alternative in {@link TiDBParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterUpgradeName(TiDBParser.AlterUpgradeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void enterAlterEvent(TiDBParser.AlterEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void exitAlterEvent(TiDBParser.AlterEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void enterAlterFunction(TiDBParser.AlterFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void exitAlterFunction(TiDBParser.AlterFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void enterAlterInstance(TiDBParser.AlterInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void exitAlterInstance(TiDBParser.AlterInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterAlterLogfileGroup(TiDBParser.AlterLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitAlterLogfileGroup(TiDBParser.AlterLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void enterAlterProcedure(TiDBParser.AlterProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void exitAlterProcedure(TiDBParser.AlterProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void enterAlterServer(TiDBParser.AlterServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void exitAlterServer(TiDBParser.AlterServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void enterAlterTable(TiDBParser.AlterTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void exitAlterTable(TiDBParser.AlterTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablespace(TiDBParser.AlterTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablespace(TiDBParser.AlterTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#alterView}.
	 * @param ctx the parse tree
	 */
	void enterAlterView(TiDBParser.AlterViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#alterView}.
	 * @param ctx the parse tree
	 */
	void exitAlterView(TiDBParser.AlterViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTableOption(TiDBParser.AlterByTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTableOption}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTableOption(TiDBParser.AlterByTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumn(TiDBParser.AlterByAddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumn(TiDBParser.AlterByAddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddColumns(TiDBParser.AlterByAddColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddColumns}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddColumns(TiDBParser.AlterByAddColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddIndex(TiDBParser.AlterByAddIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddIndex(TiDBParser.AlterByAddIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPrimaryKey(TiDBParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPrimaryKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPrimaryKey(TiDBParser.AlterByAddPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddUniqueKey(TiDBParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddUniqueKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddUniqueKey(TiDBParser.AlterByAddUniqueKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddSpecialIndex(TiDBParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddSpecialIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddSpecialIndex(TiDBParser.AlterByAddSpecialIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddForeignKey(TiDBParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddForeignKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddForeignKey(TiDBParser.AlterByAddForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddCheckTableConstraint(TiDBParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddCheckTableConstraint}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddCheckTableConstraint(TiDBParser.AlterByAddCheckTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterBySetAlgorithm(TiDBParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterBySetAlgorithm}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterBySetAlgorithm(TiDBParser.AlterBySetAlgorithmContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeDefault(TiDBParser.AlterByChangeDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeDefault}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeDefault(TiDBParser.AlterByChangeDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByChangeColumn(TiDBParser.AlterByChangeColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByChangeColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByChangeColumn(TiDBParser.AlterByChangeColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameColumn(TiDBParser.AlterByRenameColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameColumn(TiDBParser.AlterByRenameColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByLock(TiDBParser.AlterByLockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByLock}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByLock(TiDBParser.AlterByLockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByModifyColumn(TiDBParser.AlterByModifyColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByModifyColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByModifyColumn(TiDBParser.AlterByModifyColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropColumn(TiDBParser.AlterByDropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropColumn}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropColumn(TiDBParser.AlterByDropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropConstraintCheck(TiDBParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropConstraintCheck}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropConstraintCheck(TiDBParser.AlterByDropConstraintCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPrimaryKey(TiDBParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPrimaryKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPrimaryKey(TiDBParser.AlterByDropPrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropIndex(TiDBParser.AlterByDropIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropIndex(TiDBParser.AlterByDropIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRenameIndex(TiDBParser.AlterByRenameIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRenameIndex}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRenameIndex(TiDBParser.AlterByRenameIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAlterIndexVisibility(TiDBParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAlterIndexVisibility}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAlterIndexVisibility(TiDBParser.AlterByAlterIndexVisibilityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropForeignKey(TiDBParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropForeignKey}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropForeignKey(TiDBParser.AlterByDropForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDisableKeys(TiDBParser.AlterByDisableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDisableKeys}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDisableKeys(TiDBParser.AlterByDisableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByEnableKeys(TiDBParser.AlterByEnableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByEnableKeys}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByEnableKeys(TiDBParser.AlterByEnableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRename(TiDBParser.AlterByRenameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRename}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRename(TiDBParser.AlterByRenameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOrder(TiDBParser.AlterByOrderContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOrder}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOrder(TiDBParser.AlterByOrderContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByConvertCharset(TiDBParser.AlterByConvertCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByConvertCharset}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByConvertCharset(TiDBParser.AlterByConvertCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDefaultCharset(TiDBParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDefaultCharset}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDefaultCharset(TiDBParser.AlterByDefaultCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardTablespace(TiDBParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardTablespace}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardTablespace(TiDBParser.AlterByDiscardTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportTablespace(TiDBParser.AlterByImportTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportTablespace}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportTablespace(TiDBParser.AlterByImportTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByForce(TiDBParser.AlterByForceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByForce}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByForce(TiDBParser.AlterByForceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByValidate(TiDBParser.AlterByValidateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByValidate}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByValidate(TiDBParser.AlterByValidateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAddPartition(TiDBParser.AlterByAddPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAddPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAddPartition(TiDBParser.AlterByAddPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDropPartition(TiDBParser.AlterByDropPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDropPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDropPartition(TiDBParser.AlterByDropPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByDiscardPartition(TiDBParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByDiscardPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByDiscardPartition(TiDBParser.AlterByDiscardPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByImportPartition(TiDBParser.AlterByImportPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByImportPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByImportPartition(TiDBParser.AlterByImportPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByTruncatePartition(TiDBParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByTruncatePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByTruncatePartition(TiDBParser.AlterByTruncatePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCoalescePartition(TiDBParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCoalescePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCoalescePartition(TiDBParser.AlterByCoalescePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByReorganizePartition(TiDBParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByReorganizePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByReorganizePartition(TiDBParser.AlterByReorganizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByExchangePartition(TiDBParser.AlterByExchangePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByExchangePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByExchangePartition(TiDBParser.AlterByExchangePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByAnalyzePartition(TiDBParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByAnalyzePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByAnalyzePartition(TiDBParser.AlterByAnalyzePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByCheckPartition(TiDBParser.AlterByCheckPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByCheckPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByCheckPartition(TiDBParser.AlterByCheckPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByOptimizePartition(TiDBParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByOptimizePartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByOptimizePartition(TiDBParser.AlterByOptimizePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRebuildPartition(TiDBParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRebuildPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRebuildPartition(TiDBParser.AlterByRebuildPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRepairPartition(TiDBParser.AlterByRepairPartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRepairPartition}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRepairPartition(TiDBParser.AlterByRepairPartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByRemovePartitioning(TiDBParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByRemovePartitioning}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByRemovePartitioning(TiDBParser.AlterByRemovePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void enterAlterByUpgradePartitioning(TiDBParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterByUpgradePartitioning}
	 * labeled alternative in {@link TiDBParser#alterSpecification}.
	 * @param ctx the parse tree
	 */
	void exitAlterByUpgradePartitioning(TiDBParser.AlterByUpgradePartitioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(TiDBParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(TiDBParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void enterDropEvent(TiDBParser.DropEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void exitDropEvent(TiDBParser.DropEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropIndex(TiDBParser.DropIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropIndex(TiDBParser.DropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterDropLogfileGroup(TiDBParser.DropLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitDropLogfileGroup(TiDBParser.DropLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void enterDropProcedure(TiDBParser.DropProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void exitDropProcedure(TiDBParser.DropProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(TiDBParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(TiDBParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void enterDropServer(TiDBParser.DropServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void exitDropServer(TiDBParser.DropServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(TiDBParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(TiDBParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void enterDropTablespace(TiDBParser.DropTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void exitDropTablespace(TiDBParser.DropTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void enterDropTrigger(TiDBParser.DropTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void exitDropTrigger(TiDBParser.DropTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropView}.
	 * @param ctx the parse tree
	 */
	void enterDropView(TiDBParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropView}.
	 * @param ctx the parse tree
	 */
	void exitDropView(TiDBParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(TiDBParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(TiDBParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameTableClause(TiDBParser.RenameTableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#renameTableClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameTableClause(TiDBParser.RenameTableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(TiDBParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(TiDBParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void enterCallStatement(TiDBParser.CallStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void exitCallStatement(TiDBParser.CallStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#procedureArgs}.
	 * @param ctx the parse tree
	 */
	void enterProcedureArgs(TiDBParser.ProcedureArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#procedureArgs}.
	 * @param ctx the parse tree
	 */
	void exitProcedureArgs(TiDBParser.ProcedureArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(TiDBParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(TiDBParser.DeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoStatement(TiDBParser.DoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoStatement(TiDBParser.DoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerStatement(TiDBParser.HandlerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerStatement(TiDBParser.HandlerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(TiDBParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(TiDBParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadDataStatement(TiDBParser.LoadDataStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadDataStatement(TiDBParser.LoadDataStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadXmlStatement(TiDBParser.LoadXmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadXmlStatement(TiDBParser.LoadXmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void enterReplaceStatement(TiDBParser.ReplaceStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#replaceStatement}.
	 * @param ctx the parse tree
	 */
	void exitReplaceStatement(TiDBParser.ReplaceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelect(TiDBParser.SimpleSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelect(TiDBParser.SimpleSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisSelect(TiDBParser.ParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesisSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisSelect(TiDBParser.ParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionSelect(TiDBParser.UnionSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionSelect(TiDBParser.UnionSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesisSelect(TiDBParser.UnionParenthesisSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionParenthesisSelect}
	 * labeled alternative in {@link TiDBParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesisSelect(TiDBParser.UnionParenthesisSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void enterUpdateStatement(TiDBParser.UpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void exitUpdateStatement(TiDBParser.UpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void enterWithSelectStatement(TiDBParser.WithSelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#withSelectStatement}.
	 * @param ctx the parse tree
	 */
	void exitWithSelectStatement(TiDBParser.WithSelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#withClause}.
	 * @param ctx the parse tree
	 */
	void enterWithClause(TiDBParser.WithClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#withClause}.
	 * @param ctx the parse tree
	 */
	void exitWithClause(TiDBParser.WithClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#withSelectExpr}.
	 * @param ctx the parse tree
	 */
	void enterWithSelectExpr(TiDBParser.WithSelectExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#withSelectExpr}.
	 * @param ctx the parse tree
	 */
	void exitWithSelectExpr(TiDBParser.WithSelectExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectInsertValue}
	 * labeled alternative in {@link TiDBParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void enterSelectInsertValue(TiDBParser.SelectInsertValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectInsertValue}
	 * labeled alternative in {@link TiDBParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void exitSelectInsertValue(TiDBParser.SelectInsertValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentInsertValue}
	 * labeled alternative in {@link TiDBParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void enterCommentInsertValue(TiDBParser.CommentInsertValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentInsertValue}
	 * labeled alternative in {@link TiDBParser#insertStatementValue}.
	 * @param ctx the parse tree
	 */
	void exitCommentInsertValue(TiDBParser.CommentInsertValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void enterUpdatedElement(TiDBParser.UpdatedElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#updatedElement}.
	 * @param ctx the parse tree
	 */
	void exitUpdatedElement(TiDBParser.UpdatedElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentField(TiDBParser.AssignmentFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#assignmentField}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentField(TiDBParser.AssignmentFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void enterLockClause(TiDBParser.LockClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void exitLockClause(TiDBParser.LockClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleDeleteStatement(TiDBParser.SingleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#singleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleDeleteStatement(TiDBParser.SingleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleDeleteStatement(TiDBParser.MultipleDeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#multipleDeleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleDeleteStatement(TiDBParser.MultipleDeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerOpenStatement(TiDBParser.HandlerOpenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerOpenStatement(TiDBParser.HandlerOpenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadIndexStatement(TiDBParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadIndexStatement(TiDBParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadStatement(TiDBParser.HandlerReadStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadStatement(TiDBParser.HandlerReadStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerCloseStatement(TiDBParser.HandlerCloseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerCloseStatement(TiDBParser.HandlerCloseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleUpdateStatement(TiDBParser.SingleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#singleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleUpdateStatement(TiDBParser.SingleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#ignore_}.
	 * @param ctx the parse tree
	 */
	void enterIgnore_(TiDBParser.Ignore_Context ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#ignore_}.
	 * @param ctx the parse tree
	 */
	void exitIgnore_(TiDBParser.Ignore_Context ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleUpdateStatement(TiDBParser.MultipleUpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#multipleUpdateStatement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleUpdateStatement(TiDBParser.MultipleUpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(TiDBParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(TiDBParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpression(TiDBParser.OrderByExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpression(TiDBParser.OrderByExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void enterTableSources(TiDBParser.TableSourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tableSources}.
	 * @param ctx the parse tree
	 */
	void exitTableSources(TiDBParser.TableSourcesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link TiDBParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceBase(TiDBParser.TableSourceBaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceBase}
	 * labeled alternative in {@link TiDBParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceBase(TiDBParser.TableSourceBaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link TiDBParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceNested(TiDBParser.TableSourceNestedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceNested}
	 * labeled alternative in {@link TiDBParser#tableSource}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceNested(TiDBParser.TableSourceNestedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterAtomTableItem(TiDBParser.AtomTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomTableItem}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitAtomTableItem(TiDBParser.AtomTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryTableItem(TiDBParser.SubqueryTableItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryTableItem}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryTableItem(TiDBParser.SubqueryTableItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourcesItem(TiDBParser.TableSourcesItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourcesItem}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourcesItem(TiDBParser.TableSourcesItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void enterTableSourceWith(TiDBParser.TableSourceWithContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableSourceWith}
	 * labeled alternative in {@link TiDBParser#tableSourceItem}.
	 * @param ctx the parse tree
	 */
	void exitTableSourceWith(TiDBParser.TableSourceWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void enterIndexHint(TiDBParser.IndexHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void exitIndexHint(TiDBParser.IndexHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void enterIndexHintType(TiDBParser.IndexHintTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#indexHintType}.
	 * @param ctx the parse tree
	 */
	void exitIndexHintType(TiDBParser.IndexHintTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoin(TiDBParser.InnerJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code innerJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoin(TiDBParser.InnerJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterStraightJoin(TiDBParser.StraightJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code straightJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitStraightJoin(TiDBParser.StraightJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoin(TiDBParser.OuterJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code outerJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoin(TiDBParser.OuterJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoin(TiDBParser.NaturalJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code naturalJoin}
	 * labeled alternative in {@link TiDBParser#joinPart}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoin(TiDBParser.NaturalJoinContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoinType(TiDBParser.InnerJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoinType(TiDBParser.InnerJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoinType(TiDBParser.OuterJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoinType(TiDBParser.OuterJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoinType(TiDBParser.NaturalJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoinType(TiDBParser.NaturalJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpression(TiDBParser.QueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpression(TiDBParser.QueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpressionNointo(TiDBParser.QueryExpressionNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#queryExpressionNointo}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpressionNointo(TiDBParser.QueryExpressionNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(TiDBParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(TiDBParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecificationNointo(TiDBParser.QuerySpecificationNointoContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#querySpecificationNointo}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecificationNointo(TiDBParser.QuerySpecificationNointoContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void enterUnionParenthesis(TiDBParser.UnionParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#unionParenthesis}.
	 * @param ctx the parse tree
	 */
	void exitUnionParenthesis(TiDBParser.UnionParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void enterUnionStatement(TiDBParser.UnionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#unionStatement}.
	 * @param ctx the parse tree
	 */
	void exitUnionStatement(TiDBParser.UnionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void enterSelectSpec(TiDBParser.SelectSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void exitSelectSpec(TiDBParser.SelectSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void enterSelectElements(TiDBParser.SelectElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void exitSelectElements(TiDBParser.SelectElementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStarElement(TiDBParser.SelectStarElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectStarElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStarElement(TiDBParser.SelectStarElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectColumnElement(TiDBParser.SelectColumnElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectColumnElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectColumnElement(TiDBParser.SelectColumnElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectFunctionElement(TiDBParser.SelectFunctionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectFunctionElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectFunctionElement(TiDBParser.SelectFunctionElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpressionElement(TiDBParser.SelectExpressionElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectExpressionElement}
	 * labeled alternative in {@link TiDBParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpressionElement(TiDBParser.SelectExpressionElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#aliasName}.
	 * @param ctx the parse tree
	 */
	void enterAliasName(TiDBParser.AliasNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#aliasName}.
	 * @param ctx the parse tree
	 */
	void exitAliasName(TiDBParser.AliasNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link TiDBParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoVariables(TiDBParser.SelectIntoVariablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoVariables}
	 * labeled alternative in {@link TiDBParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoVariables(TiDBParser.SelectIntoVariablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link TiDBParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoDumpFile(TiDBParser.SelectIntoDumpFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoDumpFile}
	 * labeled alternative in {@link TiDBParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoDumpFile(TiDBParser.SelectIntoDumpFileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link TiDBParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoTextFile(TiDBParser.SelectIntoTextFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectIntoTextFile}
	 * labeled alternative in {@link TiDBParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoTextFile(TiDBParser.SelectIntoTextFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectFieldsInto(TiDBParser.SelectFieldsIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectFieldsInto(TiDBParser.SelectFieldsIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectLinesInto(TiDBParser.SelectLinesIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectLinesInto(TiDBParser.SelectLinesIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(TiDBParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(TiDBParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(TiDBParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(TiDBParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupClause(TiDBParser.GroupClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupClause(TiDBParser.GroupClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(TiDBParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(TiDBParser.HavingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void enterGroupByItem(TiDBParser.GroupByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#groupByItem}.
	 * @param ctx the parse tree
	 */
	void exitGroupByItem(TiDBParser.GroupByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(TiDBParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(TiDBParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void enterLimitClauseAtom(TiDBParser.LimitClauseAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#limitClauseAtom}.
	 * @param ctx the parse tree
	 */
	void exitLimitClauseAtom(TiDBParser.LimitClauseAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void enterStartTransaction(TiDBParser.StartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#startTransaction}.
	 * @param ctx the parse tree
	 */
	void exitStartTransaction(TiDBParser.StartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void enterBeginWork(TiDBParser.BeginWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#beginWork}.
	 * @param ctx the parse tree
	 */
	void exitBeginWork(TiDBParser.BeginWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void enterCommitWork(TiDBParser.CommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#commitWork}.
	 * @param ctx the parse tree
	 */
	void exitCommitWork(TiDBParser.CommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterRollbackWork(TiDBParser.RollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#rollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitRollbackWork(TiDBParser.RollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void enterSavepointStatement(TiDBParser.SavepointStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#savepointStatement}.
	 * @param ctx the parse tree
	 */
	void exitSavepointStatement(TiDBParser.SavepointStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void enterRollbackStatement(TiDBParser.RollbackStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#rollbackStatement}.
	 * @param ctx the parse tree
	 */
	void exitRollbackStatement(TiDBParser.RollbackStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void enterReleaseStatement(TiDBParser.ReleaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#releaseStatement}.
	 * @param ctx the parse tree
	 */
	void exitReleaseStatement(TiDBParser.ReleaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void enterLockTables(TiDBParser.LockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#lockTables}.
	 * @param ctx the parse tree
	 */
	void exitLockTables(TiDBParser.LockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void enterUnlockTables(TiDBParser.UnlockTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#unlockTables}.
	 * @param ctx the parse tree
	 */
	void exitUnlockTables(TiDBParser.UnlockTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommitStatement(TiDBParser.SetAutocommitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#setAutocommitStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommitStatement(TiDBParser.SetAutocommitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransactionStatement(TiDBParser.SetTransactionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#setTransactionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransactionStatement(TiDBParser.SetTransactionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterTransactionMode(TiDBParser.TransactionModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitTransactionMode(TiDBParser.TransactionModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void enterLockTableElement(TiDBParser.LockTableElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#lockTableElement}.
	 * @param ctx the parse tree
	 */
	void exitLockTableElement(TiDBParser.LockTableElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void enterLockAction(TiDBParser.LockActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#lockAction}.
	 * @param ctx the parse tree
	 */
	void exitLockAction(TiDBParser.LockActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void enterTransactionOption(TiDBParser.TransactionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#transactionOption}.
	 * @param ctx the parse tree
	 */
	void exitTransactionOption(TiDBParser.TransactionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevel(TiDBParser.TransactionLevelContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#transactionLevel}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevel(TiDBParser.TransactionLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void enterChangeMaster(TiDBParser.ChangeMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#changeMaster}.
	 * @param ctx the parse tree
	 */
	void exitChangeMaster(TiDBParser.ChangeMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterChangeReplicationFilter(TiDBParser.ChangeReplicationFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#changeReplicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitChangeReplicationFilter(TiDBParser.ChangeReplicationFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void enterPurgeBinaryLogs(TiDBParser.PurgeBinaryLogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#purgeBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void exitPurgeBinaryLogs(TiDBParser.PurgeBinaryLogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void enterResetMaster(TiDBParser.ResetMasterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#resetMaster}.
	 * @param ctx the parse tree
	 */
	void exitResetMaster(TiDBParser.ResetMasterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void enterResetSlave(TiDBParser.ResetSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#resetSlave}.
	 * @param ctx the parse tree
	 */
	void exitResetSlave(TiDBParser.ResetSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#resetReplica}.
	 * @param ctx the parse tree
	 */
	void enterResetReplica(TiDBParser.ResetReplicaContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#resetReplica}.
	 * @param ctx the parse tree
	 */
	void exitResetReplica(TiDBParser.ResetReplicaContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void enterStartSlave(TiDBParser.StartSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#startSlave}.
	 * @param ctx the parse tree
	 */
	void exitStartSlave(TiDBParser.StartSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void enterStopSlave(TiDBParser.StopSlaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#stopSlave}.
	 * @param ctx the parse tree
	 */
	void exitStopSlave(TiDBParser.StopSlaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStartGroupReplication(TiDBParser.StartGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#startGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStartGroupReplication(TiDBParser.StartGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void enterStopGroupReplication(TiDBParser.StopGroupReplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#stopGroupReplication}.
	 * @param ctx the parse tree
	 */
	void exitStopGroupReplication(TiDBParser.StopGroupReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterStringOption(TiDBParser.MasterStringOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterStringOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterStringOption(TiDBParser.MasterStringOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterDecimalOption(TiDBParser.MasterDecimalOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterDecimalOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterDecimalOption(TiDBParser.MasterDecimalOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterBoolOption(TiDBParser.MasterBoolOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterBoolOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterBoolOption(TiDBParser.MasterBoolOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterRealOption(TiDBParser.MasterRealOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterRealOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterRealOption(TiDBParser.MasterRealOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterUidListOption(TiDBParser.MasterUidListOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterUidListOption}
	 * labeled alternative in {@link TiDBParser#masterOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterUidListOption(TiDBParser.MasterUidListOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterStringMasterOption(TiDBParser.StringMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#stringMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitStringMasterOption(TiDBParser.StringMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterDecimalMasterOption(TiDBParser.DecimalMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#decimalMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitDecimalMasterOption(TiDBParser.DecimalMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void enterBoolMasterOption(TiDBParser.BoolMasterOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#boolMasterOption}.
	 * @param ctx the parse tree
	 */
	void exitBoolMasterOption(TiDBParser.BoolMasterOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelOption(TiDBParser.ChannelOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelOption(TiDBParser.ChannelOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoDbReplication(TiDBParser.DoDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doDbReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoDbReplication(TiDBParser.DoDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreDbReplication(TiDBParser.IgnoreDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreDbReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreDbReplication(TiDBParser.IgnoreDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterDoTableReplication(TiDBParser.DoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitDoTableReplication(TiDBParser.DoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreTableReplication(TiDBParser.IgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreTableReplication(TiDBParser.IgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildDoTableReplication(TiDBParser.WildDoTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildDoTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildDoTableReplication(TiDBParser.WildDoTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterWildIgnoreTableReplication(TiDBParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildIgnoreTableReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitWildIgnoreTableReplication(TiDBParser.WildIgnoreTableReplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void enterRewriteDbReplication(TiDBParser.RewriteDbReplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rewriteDbReplication}
	 * labeled alternative in {@link TiDBParser#replicationFilter}.
	 * @param ctx the parse tree
	 */
	void exitRewriteDbReplication(TiDBParser.RewriteDbReplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void enterTablePair(TiDBParser.TablePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tablePair}.
	 * @param ctx the parse tree
	 */
	void exitTablePair(TiDBParser.TablePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#threadType}.
	 * @param ctx the parse tree
	 */
	void enterThreadType(TiDBParser.ThreadTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#threadType}.
	 * @param ctx the parse tree
	 */
	void exitThreadType(TiDBParser.ThreadTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterGtidsUntilOption(TiDBParser.GtidsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gtidsUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitGtidsUntilOption(TiDBParser.GtidsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterMasterLogUntilOption(TiDBParser.MasterLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code masterLogUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitMasterLogUntilOption(TiDBParser.MasterLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterRelayLogUntilOption(TiDBParser.RelayLogUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relayLogUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitRelayLogUntilOption(TiDBParser.RelayLogUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void enterSqlGapsUntilOption(TiDBParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sqlGapsUntilOption}
	 * labeled alternative in {@link TiDBParser#untilOption}.
	 * @param ctx the parse tree
	 */
	void exitSqlGapsUntilOption(TiDBParser.SqlGapsUntilOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterUserConnectionOption(TiDBParser.UserConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code userConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitUserConnectionOption(TiDBParser.UserConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPasswordConnectionOption(TiDBParser.PasswordConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPasswordConnectionOption(TiDBParser.PasswordConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterDefaultAuthConnectionOption(TiDBParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultAuthConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitDefaultAuthConnectionOption(TiDBParser.DefaultAuthConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void enterPluginDirConnectionOption(TiDBParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pluginDirConnectionOption}
	 * labeled alternative in {@link TiDBParser#connectionOption}.
	 * @param ctx the parse tree
	 */
	void exitPluginDirConnectionOption(TiDBParser.PluginDirConnectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void enterGtuidSet(TiDBParser.GtuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#gtuidSet}.
	 * @param ctx the parse tree
	 */
	void exitGtuidSet(TiDBParser.GtuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaStartTransaction(TiDBParser.XaStartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xaStartTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaStartTransaction(TiDBParser.XaStartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void enterXaEndTransaction(TiDBParser.XaEndTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xaEndTransaction}.
	 * @param ctx the parse tree
	 */
	void exitXaEndTransaction(TiDBParser.XaEndTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterXaPrepareStatement(TiDBParser.XaPrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xaPrepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitXaPrepareStatement(TiDBParser.XaPrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void enterXaCommitWork(TiDBParser.XaCommitWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xaCommitWork}.
	 * @param ctx the parse tree
	 */
	void exitXaCommitWork(TiDBParser.XaCommitWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRollbackWork(TiDBParser.XaRollbackWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xaRollbackWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRollbackWork(TiDBParser.XaRollbackWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void enterXaRecoverWork(TiDBParser.XaRecoverWorkContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xaRecoverWork}.
	 * @param ctx the parse tree
	 */
	void exitXaRecoverWork(TiDBParser.XaRecoverWorkContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrepareStatement(TiDBParser.PrepareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#prepareStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrepareStatement(TiDBParser.PrepareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStatement(TiDBParser.ExecuteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#executeStatement}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStatement(TiDBParser.ExecuteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void enterDeallocatePrepare(TiDBParser.DeallocatePrepareContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#deallocatePrepare}.
	 * @param ctx the parse tree
	 */
	void exitDeallocatePrepare(TiDBParser.DeallocatePrepareContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBody(TiDBParser.RoutineBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBody(TiDBParser.RoutineBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(TiDBParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(TiDBParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(TiDBParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(TiDBParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(TiDBParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(TiDBParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterateStatement(TiDBParser.IterateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterateStatement(TiDBParser.IterateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void enterLeaveStatement(TiDBParser.LeaveStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void exitLeaveStatement(TiDBParser.LeaveStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(TiDBParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(TiDBParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(TiDBParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(TiDBParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(TiDBParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(TiDBParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(TiDBParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(TiDBParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link TiDBParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterCloseCursor(TiDBParser.CloseCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CloseCursor}
	 * labeled alternative in {@link TiDBParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitCloseCursor(TiDBParser.CloseCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link TiDBParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterFetchCursor(TiDBParser.FetchCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchCursor}
	 * labeled alternative in {@link TiDBParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitFetchCursor(TiDBParser.FetchCursorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link TiDBParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterOpenCursor(TiDBParser.OpenCursorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OpenCursor}
	 * labeled alternative in {@link TiDBParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitOpenCursor(TiDBParser.OpenCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void enterDeclareVariable(TiDBParser.DeclareVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#declareVariable}.
	 * @param ctx the parse tree
	 */
	void exitDeclareVariable(TiDBParser.DeclareVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCondition(TiDBParser.DeclareConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#declareCondition}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCondition(TiDBParser.DeclareConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void enterDeclareCursor(TiDBParser.DeclareCursorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#declareCursor}.
	 * @param ctx the parse tree
	 */
	void exitDeclareCursor(TiDBParser.DeclareCursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void enterDeclareHandler(TiDBParser.DeclareHandlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#declareHandler}.
	 * @param ctx the parse tree
	 */
	void exitDeclareHandler(TiDBParser.DeclareHandlerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionCode(TiDBParser.HandlerConditionCodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionCode}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionCode(TiDBParser.HandlerConditionCodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionState(TiDBParser.HandlerConditionStateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionState}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionState(TiDBParser.HandlerConditionStateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionName(TiDBParser.HandlerConditionNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionName}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionName(TiDBParser.HandlerConditionNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionWarning(TiDBParser.HandlerConditionWarningContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionWarning}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionWarning(TiDBParser.HandlerConditionWarningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionNotfound(TiDBParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionNotfound}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionNotfound(TiDBParser.HandlerConditionNotfoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void enterHandlerConditionException(TiDBParser.HandlerConditionExceptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code handlerConditionException}
	 * labeled alternative in {@link TiDBParser#handlerConditionValue}.
	 * @param ctx the parse tree
	 */
	void exitHandlerConditionException(TiDBParser.HandlerConditionExceptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterProcedureSqlStatement(TiDBParser.ProcedureSqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#procedureSqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitProcedureSqlStatement(TiDBParser.ProcedureSqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseAlternative(TiDBParser.CaseAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#caseAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseAlternative(TiDBParser.CaseAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void enterElifAlternative(TiDBParser.ElifAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#elifAlternative}.
	 * @param ctx the parse tree
	 */
	void exitElifAlternative(TiDBParser.ElifAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link TiDBParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV56(TiDBParser.AlterUserMysqlV56Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV56}
	 * labeled alternative in {@link TiDBParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV56(TiDBParser.AlterUserMysqlV56Context ctx);
	/**
	 * Enter a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link TiDBParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserMysqlV57(TiDBParser.AlterUserMysqlV57Context ctx);
	/**
	 * Exit a parse tree produced by the {@code alterUserMysqlV57}
	 * labeled alternative in {@link TiDBParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserMysqlV57(TiDBParser.AlterUserMysqlV57Context ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createUser}.
	 * @param ctx the parse tree
	 */
	void enterCreateUser(TiDBParser.CreateUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createUser}.
	 * @param ctx the parse tree
	 */
	void exitCreateUser(TiDBParser.CreateUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void enterDropUser(TiDBParser.DropUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void exitDropUser(TiDBParser.DropUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void enterDropRole(TiDBParser.DropRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void exitDropRole(TiDBParser.DropRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void enterGrantStatement(TiDBParser.GrantStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#grantStatement}.
	 * @param ctx the parse tree
	 */
	void exitGrantStatement(TiDBParser.GrantStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void enterRoleOption(TiDBParser.RoleOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#roleOption}.
	 * @param ctx the parse tree
	 */
	void exitRoleOption(TiDBParser.RoleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void enterGrantProxy(TiDBParser.GrantProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#grantProxy}.
	 * @param ctx the parse tree
	 */
	void exitGrantProxy(TiDBParser.GrantProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void enterRenameUser(TiDBParser.RenameUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void exitRenameUser(TiDBParser.RenameUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void enterRevokeStatement(TiDBParser.RevokeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#revokeStatement}.
	 * @param ctx the parse tree
	 */
	void exitRevokeStatement(TiDBParser.RevokeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void enterRevokeProxy(TiDBParser.RevokeProxyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#revokeProxy}.
	 * @param ctx the parse tree
	 */
	void exitRevokeProxy(TiDBParser.RevokeProxyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPasswordStatement(TiDBParser.SetPasswordStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#setPasswordStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPasswordStatement(TiDBParser.SetPasswordStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void enterUserSpecification(TiDBParser.UserSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#userSpecification}.
	 * @param ctx the parse tree
	 */
	void exitUserSpecification(TiDBParser.UserSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterHashAuthOption(TiDBParser.HashAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code hashAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitHashAuthOption(TiDBParser.HashAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterStringAuthOption(TiDBParser.StringAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitStringAuthOption(TiDBParser.StringAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterModuleAuthOption(TiDBParser.ModuleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitModuleAuthOption(TiDBParser.ModuleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAuthOption(TiDBParser.SimpleAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAuthOption}
	 * labeled alternative in {@link TiDBParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAuthOption(TiDBParser.SimpleAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code module}
	 * labeled alternative in {@link TiDBParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterModule(TiDBParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code module}
	 * labeled alternative in {@link TiDBParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitModule(TiDBParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link TiDBParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void enterPasswordModuleOption(TiDBParser.PasswordModuleOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordModuleOption}
	 * labeled alternative in {@link TiDBParser#authenticationRule}.
	 * @param ctx the parse tree
	 */
	void exitPasswordModuleOption(TiDBParser.PasswordModuleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void enterTlsOption(TiDBParser.TlsOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void exitTlsOption(TiDBParser.TlsOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void enterUserResourceOption(TiDBParser.UserResourceOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#userResourceOption}.
	 * @param ctx the parse tree
	 */
	void exitUserResourceOption(TiDBParser.UserResourceOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void enterUserPasswordOption(TiDBParser.UserPasswordOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#userPasswordOption}.
	 * @param ctx the parse tree
	 */
	void exitUserPasswordOption(TiDBParser.UserPasswordOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void enterUserLockOption(TiDBParser.UserLockOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#userLockOption}.
	 * @param ctx the parse tree
	 */
	void exitUserLockOption(TiDBParser.UserLockOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void enterPrivelegeClause(TiDBParser.PrivelegeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#privelegeClause}.
	 * @param ctx the parse tree
	 */
	void exitPrivelegeClause(TiDBParser.PrivelegeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(TiDBParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(TiDBParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterCurrentSchemaPriviLevel(TiDBParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentSchemaPriviLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitCurrentSchemaPriviLevel(TiDBParser.CurrentSchemaPriviLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterGlobalPrivLevel(TiDBParser.GlobalPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalPrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitGlobalPrivLevel(TiDBParser.GlobalPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteSchemaPrivLevel(TiDBParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteSchemaPrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteSchemaPrivLevel(TiDBParser.DefiniteSchemaPrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel(TiDBParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel(TiDBParser.DefiniteFullTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteFullTablePrivLevel2(TiDBParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteFullTablePrivLevel2}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteFullTablePrivLevel2(TiDBParser.DefiniteFullTablePrivLevel2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void enterDefiniteTablePrivLevel(TiDBParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code definiteTablePrivLevel}
	 * labeled alternative in {@link TiDBParser#privilegeLevel}.
	 * @param ctx the parse tree
	 */
	void exitDefiniteTablePrivLevel(TiDBParser.DefiniteTablePrivLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void enterRenameUserClause(TiDBParser.RenameUserClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#renameUserClause}.
	 * @param ctx the parse tree
	 */
	void exitRenameUserClause(TiDBParser.RenameUserClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void enterAnalyzeTable(TiDBParser.AnalyzeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void exitAnalyzeTable(TiDBParser.AnalyzeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void enterCheckTable(TiDBParser.CheckTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void exitCheckTable(TiDBParser.CheckTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void enterChecksumTable(TiDBParser.ChecksumTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void exitChecksumTable(TiDBParser.ChecksumTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void enterOptimizeTable(TiDBParser.OptimizeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void exitOptimizeTable(TiDBParser.OptimizeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void enterRepairTable(TiDBParser.RepairTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void exitRepairTable(TiDBParser.RepairTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableOption(TiDBParser.CheckTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableOption(TiDBParser.CheckTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#createUdfFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateUdfFunction(TiDBParser.CreateUdfFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#createUdfFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateUdfFunction(TiDBParser.CreateUdfFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void enterInstallPlugin(TiDBParser.InstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void exitInstallPlugin(TiDBParser.InstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void enterUninstallPlugin(TiDBParser.UninstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void exitUninstallPlugin(TiDBParser.UninstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetVariable(TiDBParser.SetVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setVariable}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetVariable(TiDBParser.SetVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetCharset(TiDBParser.SetCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setCharset}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetCharset(TiDBParser.SetCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNames(TiDBParser.SetNamesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNames}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNames(TiDBParser.SetNamesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetPassword(TiDBParser.SetPasswordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setPassword}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetPassword(TiDBParser.SetPasswordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetTransaction(TiDBParser.SetTransactionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTransaction}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetTransaction(TiDBParser.SetTransactionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetAutocommit(TiDBParser.SetAutocommitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setAutocommit}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetAutocommit(TiDBParser.SetAutocommitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNewValueInsideTrigger(TiDBParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setNewValueInsideTrigger}
	 * labeled alternative in {@link TiDBParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNewValueInsideTrigger(TiDBParser.SetNewValueInsideTriggerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowMasterLogs(TiDBParser.ShowMasterLogsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showMasterLogs}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowMasterLogs(TiDBParser.ShowMasterLogsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCharset(TiDBParser.ShowCharsetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCharset}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCharset(TiDBParser.ShowCharsetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowLogEvents(TiDBParser.ShowLogEventsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showLogEvents}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowLogEvents(TiDBParser.ShowLogEventsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowObjectFilter(TiDBParser.ShowObjectFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showObjectFilter}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowObjectFilter(TiDBParser.ShowObjectFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(TiDBParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(TiDBParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(TiDBParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(TiDBParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateDb(TiDBParser.ShowCreateDbContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateDb}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateDb(TiDBParser.ShowCreateDbContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateFullIdObject(TiDBParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateFullIdObject}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateFullIdObject(TiDBParser.ShowCreateFullIdObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateUser(TiDBParser.ShowCreateUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateUser}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateUser(TiDBParser.ShowCreateUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowEngine(TiDBParser.ShowEngineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEngine}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowEngine(TiDBParser.ShowEngineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showEngines}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowEngines(TiDBParser.ShowEnginesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showEngines}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowEngines(TiDBParser.ShowEnginesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowStatus(TiDBParser.ShowStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showStatus}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowStatus(TiDBParser.ShowStatusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowPlugins(TiDBParser.ShowPluginsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showPlugins}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowPlugins(TiDBParser.ShowPluginsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowPrivileges(TiDBParser.ShowPrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showPrivileges}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowPrivileges(TiDBParser.ShowPrivilegesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProcessList(TiDBParser.ShowProcessListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProcessList}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProcessList(TiDBParser.ShowProcessListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProfiles}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProfiles(TiDBParser.ShowProfilesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProfiles}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProfiles(TiDBParser.ShowProfilesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSlaveHosts}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveHosts(TiDBParser.ShowSlaveHostsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSlaveHosts}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveHosts(TiDBParser.ShowSlaveHostsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showAuthros}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowAuthros(TiDBParser.ShowAuthrosContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showAuthros}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowAuthros(TiDBParser.ShowAuthrosContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showContributors}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowContributors(TiDBParser.ShowContributorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showContributors}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowContributors(TiDBParser.ShowContributorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowErrors(TiDBParser.ShowErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showErrors}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowErrors(TiDBParser.ShowErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowCountErrors(TiDBParser.ShowCountErrorsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCountErrors}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowCountErrors(TiDBParser.ShowCountErrorsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaFilter(TiDBParser.ShowSchemaFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSchemaFilter}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaFilter(TiDBParser.ShowSchemaFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoutine(TiDBParser.ShowRoutineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoutine}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoutine(TiDBParser.ShowRoutineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(TiDBParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(TiDBParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowIndexes(TiDBParser.ShowIndexesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showIndexes}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowIndexes(TiDBParser.ShowIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowOpenTables(TiDBParser.ShowOpenTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showOpenTables}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowOpenTables(TiDBParser.ShowOpenTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowProfile(TiDBParser.ShowProfileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showProfile}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowProfile(TiDBParser.ShowProfileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveStatus(TiDBParser.ShowSlaveStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSlaveStatus}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveStatus(TiDBParser.ShowSlaveStatusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowReplicaStatus(TiDBParser.ShowReplicaStatusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showReplicaStatus}
	 * labeled alternative in {@link TiDBParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowReplicaStatus(TiDBParser.ShowReplicaStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void enterVariableClause(TiDBParser.VariableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#variableClause}.
	 * @param ctx the parse tree
	 */
	void exitVariableClause(TiDBParser.VariableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowCommonEntity(TiDBParser.ShowCommonEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#showCommonEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowCommonEntity(TiDBParser.ShowCommonEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void enterShowFilter(TiDBParser.ShowFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void exitShowFilter(TiDBParser.ShowFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void enterShowGlobalInfoClause(TiDBParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#showGlobalInfoClause}.
	 * @param ctx the parse tree
	 */
	void exitShowGlobalInfoClause(TiDBParser.ShowGlobalInfoClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemaEntity(TiDBParser.ShowSchemaEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#showSchemaEntity}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemaEntity(TiDBParser.ShowSchemaEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void enterShowProfileType(TiDBParser.ShowProfileTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void exitShowProfileType(TiDBParser.ShowProfileTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void enterBinlogStatement(TiDBParser.BinlogStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#binlogStatement}.
	 * @param ctx the parse tree
	 */
	void exitBinlogStatement(TiDBParser.BinlogStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterCacheIndexStatement(TiDBParser.CacheIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#cacheIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitCacheIndexStatement(TiDBParser.CacheIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void enterFlushStatement(TiDBParser.FlushStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#flushStatement}.
	 * @param ctx the parse tree
	 */
	void exitFlushStatement(TiDBParser.FlushStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void enterKillStatement(TiDBParser.KillStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#killStatement}.
	 * @param ctx the parse tree
	 */
	void exitKillStatement(TiDBParser.KillStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void enterLoadIndexIntoCache(TiDBParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#loadIndexIntoCache}.
	 * @param ctx the parse tree
	 */
	void exitLoadIndexIntoCache(TiDBParser.LoadIndexIntoCacheContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void enterResetStatement(TiDBParser.ResetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void exitResetStatement(TiDBParser.ResetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void enterShutdownStatement(TiDBParser.ShutdownStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#shutdownStatement}.
	 * @param ctx the parse tree
	 */
	void exitShutdownStatement(TiDBParser.ShutdownStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterTableIndexes(TiDBParser.TableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitTableIndexes(TiDBParser.TableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link TiDBParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFlushOption(TiDBParser.SimpleFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFlushOption}
	 * labeled alternative in {@link TiDBParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFlushOption(TiDBParser.SimpleFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link TiDBParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelFlushOption(TiDBParser.ChannelFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code channelFlushOption}
	 * labeled alternative in {@link TiDBParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelFlushOption(TiDBParser.ChannelFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link TiDBParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterTableFlushOption(TiDBParser.TableFlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableFlushOption}
	 * labeled alternative in {@link TiDBParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitTableFlushOption(TiDBParser.TableFlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void enterFlushTableOption(TiDBParser.FlushTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#flushTableOption}.
	 * @param ctx the parse tree
	 */
	void exitFlushTableOption(TiDBParser.FlushTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void enterLoadedTableIndexes(TiDBParser.LoadedTableIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#loadedTableIndexes}.
	 * @param ctx the parse tree
	 */
	void exitLoadedTableIndexes(TiDBParser.LoadedTableIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDescribeStatement(TiDBParser.SimpleDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#simpleDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDescribeStatement(TiDBParser.SimpleDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void enterFullDescribeStatement(TiDBParser.FullDescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#fullDescribeStatement}.
	 * @param ctx the parse tree
	 */
	void exitFullDescribeStatement(TiDBParser.FullDescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void enterHelpStatement(TiDBParser.HelpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#helpStatement}.
	 * @param ctx the parse tree
	 */
	void exitHelpStatement(TiDBParser.HelpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void enterUseStatement(TiDBParser.UseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#useStatement}.
	 * @param ctx the parse tree
	 */
	void exitUseStatement(TiDBParser.UseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void enterSignalStatement(TiDBParser.SignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void exitSignalStatement(TiDBParser.SignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void enterResignalStatement(TiDBParser.ResignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void exitResignalStatement(TiDBParser.ResignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void enterSignalConditionInformation(TiDBParser.SignalConditionInformationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#signalConditionInformation}.
	 * @param ctx the parse tree
	 */
	void exitSignalConditionInformation(TiDBParser.SignalConditionInformationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsStatement(TiDBParser.DiagnosticsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#diagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsStatement(TiDBParser.DiagnosticsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void enterDiagnosticsConditionInformationName(TiDBParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#diagnosticsConditionInformationName}.
	 * @param ctx the parse tree
	 */
	void exitDiagnosticsConditionInformationName(TiDBParser.DiagnosticsConditionInformationNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link TiDBParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeStatements(TiDBParser.DescribeStatementsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeStatements}
	 * labeled alternative in {@link TiDBParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeStatements(TiDBParser.DescribeStatementsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link TiDBParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void enterDescribeConnection(TiDBParser.DescribeConnectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeConnection}
	 * labeled alternative in {@link TiDBParser#describeObjectClause}.
	 * @param ctx the parse tree
	 */
	void exitDescribeConnection(TiDBParser.DescribeConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#fullId}.
	 * @param ctx the parse tree
	 */
	void enterFullId(TiDBParser.FullIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#fullId}.
	 * @param ctx the parse tree
	 */
	void exitFullId(TiDBParser.FullIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(TiDBParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(TiDBParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#procName}.
	 * @param ctx the parse tree
	 */
	void enterProcName(TiDBParser.ProcNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#procName}.
	 * @param ctx the parse tree
	 */
	void exitProcName(TiDBParser.ProcNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#customFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterCustomFunctionName(TiDBParser.CustomFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#customFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitCustomFunctionName(TiDBParser.CustomFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#roleName}.
	 * @param ctx the parse tree
	 */
	void enterRoleName(TiDBParser.RoleNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#roleName}.
	 * @param ctx the parse tree
	 */
	void exitRoleName(TiDBParser.RoleNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnName(TiDBParser.FullColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#fullColumnName}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnName(TiDBParser.FullColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnName(TiDBParser.IndexColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#indexColumnName}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnName(TiDBParser.IndexColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#userName}.
	 * @param ctx the parse tree
	 */
	void enterUserName(TiDBParser.UserNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#userName}.
	 * @param ctx the parse tree
	 */
	void exitUserName(TiDBParser.UserNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariable(TiDBParser.MysqlVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#mysqlVariable}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariable(TiDBParser.MysqlVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void enterCharsetName(TiDBParser.CharsetNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void exitCharsetName(TiDBParser.CharsetNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#collationName}.
	 * @param ctx the parse tree
	 */
	void enterCollationName(TiDBParser.CollationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#collationName}.
	 * @param ctx the parse tree
	 */
	void exitCollationName(TiDBParser.CollationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#engineName}.
	 * @param ctx the parse tree
	 */
	void enterEngineName(TiDBParser.EngineNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#engineName}.
	 * @param ctx the parse tree
	 */
	void exitEngineName(TiDBParser.EngineNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void enterUuidSet(TiDBParser.UuidSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#uuidSet}.
	 * @param ctx the parse tree
	 */
	void exitUuidSet(TiDBParser.UuidSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xid}.
	 * @param ctx the parse tree
	 */
	void enterXid(TiDBParser.XidContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xid}.
	 * @param ctx the parse tree
	 */
	void exitXid(TiDBParser.XidContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void enterXuidStringId(TiDBParser.XuidStringIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#xuidStringId}.
	 * @param ctx the parse tree
	 */
	void exitXuidStringId(TiDBParser.XuidStringIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void enterAuthPlugin(TiDBParser.AuthPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#authPlugin}.
	 * @param ctx the parse tree
	 */
	void exitAuthPlugin(TiDBParser.AuthPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#uid}.
	 * @param ctx the parse tree
	 */
	void enterUid(TiDBParser.UidContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#uid}.
	 * @param ctx the parse tree
	 */
	void exitUid(TiDBParser.UidContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void enterSimpleId(TiDBParser.SimpleIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#simpleId}.
	 * @param ctx the parse tree
	 */
	void exitSimpleId(TiDBParser.SimpleIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void enterDottedId(TiDBParser.DottedIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dottedId}.
	 * @param ctx the parse tree
	 */
	void exitDottedId(TiDBParser.DottedIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(TiDBParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(TiDBParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFileSizeLiteral(TiDBParser.FileSizeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFileSizeLiteral(TiDBParser.FileSizeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(TiDBParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(TiDBParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(TiDBParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(TiDBParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiteral(TiDBParser.HexadecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#hexadecimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiteral(TiDBParser.HexadecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void enterNullNotnull(TiDBParser.NullNotnullContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void exitNullNotnull(TiDBParser.NullNotnullContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(TiDBParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(TiDBParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterStringDataType(TiDBParser.StringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitStringDataType(TiDBParser.StringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalStringDataType(TiDBParser.NationalStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalStringDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalStringDataType(TiDBParser.NationalStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterNationalVaryingStringDataType(TiDBParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nationalVaryingStringDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitNationalVaryingStringDataType(TiDBParser.NationalVaryingStringDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDimensionDataType(TiDBParser.DimensionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dimensionDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDimensionDataType(TiDBParser.DimensionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDataType(TiDBParser.SimpleDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDataType(TiDBParser.SimpleDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterCollectionDataType(TiDBParser.CollectionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collectionDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitCollectionDataType(TiDBParser.CollectionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterSpatialDataType(TiDBParser.SpatialDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code spatialDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitSpatialDataType(TiDBParser.SpatialDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarcharDataType(TiDBParser.LongVarcharDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarcharDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarcharDataType(TiDBParser.LongVarcharDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterLongVarbinaryDataType(TiDBParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code longVarbinaryDataType}
	 * labeled alternative in {@link TiDBParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitLongVarbinaryDataType(TiDBParser.LongVarbinaryDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOptions(TiDBParser.CollectionOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#collectionOptions}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOptions(TiDBParser.CollectionOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void enterCollectionOption(TiDBParser.CollectionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#collectionOption}.
	 * @param ctx the parse tree
	 */
	void exitCollectionOption(TiDBParser.CollectionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void enterConvertedDataType(TiDBParser.ConvertedDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void exitConvertedDataType(TiDBParser.ConvertedDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthOneDimension(TiDBParser.LengthOneDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#lengthOneDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthOneDimension(TiDBParser.LengthOneDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoDimension(TiDBParser.LengthTwoDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#lengthTwoDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoDimension(TiDBParser.LengthTwoDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void enterLengthTwoOptionalDimension(TiDBParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#lengthTwoOptionalDimension}.
	 * @param ctx the parse tree
	 */
	void exitLengthTwoOptionalDimension(TiDBParser.LengthTwoOptionalDimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#uidList}.
	 * @param ctx the parse tree
	 */
	void enterUidList(TiDBParser.UidListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#uidList}.
	 * @param ctx the parse tree
	 */
	void exitUidList(TiDBParser.UidListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#tables}.
	 * @param ctx the parse tree
	 */
	void enterTables(TiDBParser.TablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#tables}.
	 * @param ctx the parse tree
	 */
	void exitTables(TiDBParser.TablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void enterIndexColumnNames(TiDBParser.IndexColumnNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#indexColumnNames}.
	 * @param ctx the parse tree
	 */
	void exitIndexColumnNames(TiDBParser.IndexColumnNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterExpressions(TiDBParser.ExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitExpressions(TiDBParser.ExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void enterExpressionsWithDefaults(TiDBParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#expressionsWithDefaults}.
	 * @param ctx the parse tree
	 */
	void exitExpressionsWithDefaults(TiDBParser.ExpressionsWithDefaultsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#constants}.
	 * @param ctx the parse tree
	 */
	void enterConstants(TiDBParser.ConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#constants}.
	 * @param ctx the parse tree
	 */
	void exitConstants(TiDBParser.ConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStrings(TiDBParser.SimpleStringsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#simpleStrings}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStrings(TiDBParser.SimpleStringsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void enterUserVariables(TiDBParser.UserVariablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#userVariables}.
	 * @param ctx the parse tree
	 */
	void exitUserVariables(TiDBParser.UserVariablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(TiDBParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(TiDBParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterCurrentTimestamp(TiDBParser.CurrentTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#currentTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitCurrentTimestamp(TiDBParser.CurrentTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void enterExpressionOrDefault(TiDBParser.ExpressionOrDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#expressionOrDefault}.
	 * @param ctx the parse tree
	 */
	void exitExpressionOrDefault(TiDBParser.ExpressionOrDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void enterIfExists(TiDBParser.IfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void exitIfExists(TiDBParser.IfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void enterIfNotExists(TiDBParser.IfNotExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void exitIfNotExists(TiDBParser.IfNotExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterSpecificFunctionCall(TiDBParser.SpecificFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitSpecificFunctionCall(TiDBParser.SpecificFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunctionCall(TiDBParser.AggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunctionCall(TiDBParser.AggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionCall(TiDBParser.ScalarFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionCall(TiDBParser.ScalarFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterUdfFunctionCall(TiDBParser.UdfFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code udfFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitUdfFunctionCall(TiDBParser.UdfFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionCall(TiDBParser.PasswordFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passwordFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionCall(TiDBParser.PasswordFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonAggregateFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterNonAggregateFunctionCall(TiDBParser.NonAggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonAggregateFunctionCall}
	 * labeled alternative in {@link TiDBParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitNonAggregateFunctionCall(TiDBParser.NonAggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#nonAggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterNonAggregateFunction(TiDBParser.NonAggregateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#nonAggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitNonAggregateFunction(TiDBParser.NonAggregateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#overClause}.
	 * @param ctx the parse tree
	 */
	void enterOverClause(TiDBParser.OverClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#overClause}.
	 * @param ctx the parse tree
	 */
	void exitOverClause(TiDBParser.OverClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void enterWindow_specification(TiDBParser.Window_specificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#window_specification}.
	 * @param ctx the parse tree
	 */
	void exitWindow_specification(TiDBParser.Window_specificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void enterFrame_clause(TiDBParser.Frame_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#frame_clause}.
	 * @param ctx the parse tree
	 */
	void exitFrame_clause(TiDBParser.Frame_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void enterFrame_extent(TiDBParser.Frame_extentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#frame_extent}.
	 * @param ctx the parse tree
	 */
	void exitFrame_extent(TiDBParser.Frame_extentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#frame_start_or_end}.
	 * @param ctx the parse tree
	 */
	void enterFrame_start_or_end(TiDBParser.Frame_start_or_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#frame_start_or_end}.
	 * @param ctx the parse tree
	 */
	void exitFrame_start_or_end(TiDBParser.Frame_start_or_endContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFunctionCall(TiDBParser.SimpleFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFunctionCall(TiDBParser.SimpleFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialTimeCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSpecialTimeCall(TiDBParser.SpecialTimeCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialTimeCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSpecialTimeCall(TiDBParser.SpecialTimeCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeFunctionCall(TiDBParser.DataTypeFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeFunctionCall(TiDBParser.DataTypeFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterValuesFunctionCall(TiDBParser.ValuesFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valuesFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitValuesFunctionCall(TiDBParser.ValuesFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCaseFunctionCall(TiDBParser.CaseFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCaseFunctionCall(TiDBParser.CaseFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCharFunctionCall(TiDBParser.CharFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCharFunctionCall(TiDBParser.CharFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterPositionFunctionCall(TiDBParser.PositionFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitPositionFunctionCall(TiDBParser.PositionFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterSubstrFunctionCall(TiDBParser.SubstrFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substrFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitSubstrFunctionCall(TiDBParser.SubstrFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterTrimFunctionCall(TiDBParser.TrimFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trimFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitTrimFunctionCall(TiDBParser.TrimFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterWeightFunctionCall(TiDBParser.WeightFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code weightFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitWeightFunctionCall(TiDBParser.WeightFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterExtractFunctionCall(TiDBParser.ExtractFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extractFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitExtractFunctionCall(TiDBParser.ExtractFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterGetFormatFunctionCall(TiDBParser.GetFormatFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getFormatFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitGetFormatFunctionCall(TiDBParser.GetFormatFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterJsonValueFunctionCall(TiDBParser.JsonValueFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonValueFunctionCall}
	 * labeled alternative in {@link TiDBParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitJsonValueFunctionCall(TiDBParser.JsonValueFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseFuncAlternative(TiDBParser.CaseFuncAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseFuncAlternative(TiDBParser.CaseFuncAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link TiDBParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightList(TiDBParser.LevelWeightListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightList}
	 * labeled alternative in {@link TiDBParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightList(TiDBParser.LevelWeightListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link TiDBParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void enterLevelWeightRange(TiDBParser.LevelWeightRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code levelWeightRange}
	 * labeled alternative in {@link TiDBParser#levelsInWeightString}.
	 * @param ctx the parse tree
	 */
	void exitLevelWeightRange(TiDBParser.LevelWeightRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void enterLevelInWeightListElement(TiDBParser.LevelInWeightListElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void exitLevelInWeightListElement(TiDBParser.LevelInWeightListElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunction(TiDBParser.AggregateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunction(TiDBParser.AggregateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionName(TiDBParser.ScalarFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionName(TiDBParser.ScalarFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void enterPasswordFunctionClause(TiDBParser.PasswordFunctionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#passwordFunctionClause}.
	 * @param ctx the parse tree
	 */
	void exitPasswordFunctionClause(TiDBParser.PasswordFunctionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArgs(TiDBParser.FunctionArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArgs(TiDBParser.FunctionArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArg(TiDBParser.FunctionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArg(TiDBParser.FunctionArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsExpression(TiDBParser.IsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsExpression(TiDBParser.IsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(TiDBParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(TiDBParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(TiDBParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(TiDBParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPredicateExpression(TiDBParser.PredicateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link TiDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPredicateExpression(TiDBParser.PredicateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSoundsLikePredicate(TiDBParser.SoundsLikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code soundsLikePredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSoundsLikePredicate(TiDBParser.SoundsLikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAtomPredicate(TiDBParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAtomPredicate(TiDBParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterJsonMemberOfPredicate(TiDBParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonMemberOfPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitJsonMemberOfPredicate(TiDBParser.JsonMemberOfPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInPredicate(TiDBParser.InPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInPredicate(TiDBParser.InPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryComparasionPredicate(TiDBParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryComparasionPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryComparasionPredicate(TiDBParser.SubqueryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullSearchPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterFullSearchPredicate(TiDBParser.FullSearchPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullSearchPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitFullSearchPredicate(TiDBParser.FullSearchPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBetweenPredicate(TiDBParser.BetweenPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code betweenPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBetweenPredicate(TiDBParser.BetweenPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBinaryComparasionPredicate(TiDBParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryComparasionPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBinaryComparasionPredicate(TiDBParser.BinaryComparasionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterIsNullPredicate(TiDBParser.IsNullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitIsNullPredicate(TiDBParser.IsNullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterLikePredicate(TiDBParser.LikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitLikePredicate(TiDBParser.LikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterRegexpPredicate(TiDBParser.RegexpPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link TiDBParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitRegexpPredicate(TiDBParser.RegexpPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#search_modifier}.
	 * @param ctx the parse tree
	 */
	void enterSearch_modifier(TiDBParser.Search_modifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#search_modifier}.
	 * @param ctx the parse tree
	 */
	void exitSearch_modifier(TiDBParser.Search_modifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionAtom(TiDBParser.UnaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionAtom(TiDBParser.UnaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterCollateExpressionAtom(TiDBParser.CollateExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code collateExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitCollateExpressionAtom(TiDBParser.CollateExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpessionAtom(TiDBParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpessionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpessionAtom(TiDBParser.SubqueryExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMysqlVariableExpressionAtom(TiDBParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mysqlVariableExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMysqlVariableExpressionAtom(TiDBParser.MysqlVariableExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedExpressionAtom(TiDBParser.NestedExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedExpressionAtom(TiDBParser.NestedExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedRowExpressionAtom(TiDBParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedRowExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedRowExpressionAtom(TiDBParser.NestedRowExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMathExpressionAtom(TiDBParser.MathExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMathExpressionAtom(TiDBParser.MathExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpressionAtom(TiDBParser.IntervalExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpressionAtom(TiDBParser.IntervalExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterJsonExpressionAtom(TiDBParser.JsonExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jsonExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitJsonExpressionAtom(TiDBParser.JsonExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterExistsExpessionAtom(TiDBParser.ExistsExpessionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code existsExpessionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitExistsExpessionAtom(TiDBParser.ExistsExpessionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpressionAtom(TiDBParser.ConstantExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpressionAtom(TiDBParser.ConstantExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpressionAtom(TiDBParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpressionAtom(TiDBParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpressionAtom(TiDBParser.BinaryExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpressionAtom(TiDBParser.BinaryExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnNameExpressionAtom(TiDBParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnNameExpressionAtom(TiDBParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterBitExpressionAtom(TiDBParser.BitExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitExpressionAtom}
	 * labeled alternative in {@link TiDBParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitBitExpressionAtom(TiDBParser.BitExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(TiDBParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(TiDBParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(TiDBParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(TiDBParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(TiDBParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(TiDBParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void enterBitOperator(TiDBParser.BitOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#bitOperator}.
	 * @param ctx the parse tree
	 */
	void exitBitOperator(TiDBParser.BitOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void enterMathOperator(TiDBParser.MathOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void exitMathOperator(TiDBParser.MathOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void enterJsonOperator(TiDBParser.JsonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#jsonOperator}.
	 * @param ctx the parse tree
	 */
	void exitJsonOperator(TiDBParser.JsonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void enterCharsetNameBase(TiDBParser.CharsetNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#charsetNameBase}.
	 * @param ctx the parse tree
	 */
	void exitCharsetNameBase(TiDBParser.CharsetNameBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void enterTransactionLevelBase(TiDBParser.TransactionLevelBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#transactionLevelBase}.
	 * @param ctx the parse tree
	 */
	void exitTransactionLevelBase(TiDBParser.TransactionLevelBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void enterPrivilegesBase(TiDBParser.PrivilegesBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#privilegesBase}.
	 * @param ctx the parse tree
	 */
	void exitPrivilegesBase(TiDBParser.PrivilegesBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterIntervalTypeBase(TiDBParser.IntervalTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#intervalTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitIntervalTypeBase(TiDBParser.IntervalTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeBase(TiDBParser.DataTypeBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#dataTypeBase}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeBase(TiDBParser.DataTypeBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void enterKeywordsCanBeId(TiDBParser.KeywordsCanBeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void exitKeywordsCanBeId(TiDBParser.KeywordsCanBeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link TiDBParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void enterFunctionNameBase(TiDBParser.FunctionNameBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TiDBParser#functionNameBase}.
	 * @param ctx the parse tree
	 */
	void exitFunctionNameBase(TiDBParser.FunctionNameBaseContext ctx);
}