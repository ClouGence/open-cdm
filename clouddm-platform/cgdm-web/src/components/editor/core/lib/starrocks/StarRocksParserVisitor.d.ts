import { ParseTreeVisitor } from "antlr4ts/tree/ParseTreeVisitor";
import { TableAtomContext } from "./StarRocksParser";
import { InlineTableContext } from "./StarRocksParser";
import { SubqueryWithAliasContext } from "./StarRocksParser";
import { TableFunctionContext } from "./StarRocksParser";
import { NormalizedTableFunctionContext } from "./StarRocksParser";
import { FileTableFunctionContext } from "./StarRocksParser";
import { ParenthesizedRelationContext } from "./StarRocksParser";
import { DecimalValueContext } from "./StarRocksParser";
import { DoubleValueContext } from "./StarRocksParser";
import { IntegerValueContext } from "./StarRocksParser";
import { ExtractContext } from "./StarRocksParser";
import { GroupingOperationContext } from "./StarRocksParser";
import { InformationFunctionContext } from "./StarRocksParser";
import { SpecialDateTimeContext } from "./StarRocksParser";
import { SpecialFunctionContext } from "./StarRocksParser";
import { AggregationFunctionCallContext } from "./StarRocksParser";
import { WindowFunctionCallContext } from "./StarRocksParser";
import { SimpleFunctionCallContext } from "./StarRocksParser";
import { FromContext } from "./StarRocksParser";
import { DualContext } from "./StarRocksParser";
import { SetNamesContext } from "./StarRocksParser";
import { SetPasswordContext } from "./StarRocksParser";
import { SetUserVarContext } from "./StarRocksParser";
import { SetSystemVarContext } from "./StarRocksParser";
import { SetTransactionContext } from "./StarRocksParser";
import { KeyPartitionListContext } from "./StarRocksParser";
import { InSubqueryContext } from "./StarRocksParser";
import { InListContext } from "./StarRocksParser";
import { BetweenContext } from "./StarRocksParser";
import { LikeContext } from "./StarRocksParser";
import { GrantRoleToUserContext } from "./StarRocksParser";
import { GrantRoleToRoleContext } from "./StarRocksParser";
import { BooleanExpressionDefaultContext } from "./StarRocksParser";
import { IsNullContext } from "./StarRocksParser";
import { ComparisonContext } from "./StarRocksParser";
import { ScalarSubqueryContext } from "./StarRocksParser";
import { NullLiteralContext } from "./StarRocksParser";
import { BooleanLiteralContext } from "./StarRocksParser";
import { NumericLiteralContext } from "./StarRocksParser";
import { DateLiteralContext } from "./StarRocksParser";
import { StringLiteralContext } from "./StarRocksParser";
import { IntervalLiteralContext } from "./StarRocksParser";
import { UnitBoundaryLiteralContext } from "./StarRocksParser";
import { BinaryLiteralContext } from "./StarRocksParser";
import { GrantOnUserContext } from "./StarRocksParser";
import { GrantOnTableBriefContext } from "./StarRocksParser";
import { GrantOnFuncContext } from "./StarRocksParser";
import { GrantOnSystemContext } from "./StarRocksParser";
import { GrantOnPrimaryObjContext } from "./StarRocksParser";
import { GrantOnAllContext } from "./StarRocksParser";
import { RevokeOnUserContext } from "./StarRocksParser";
import { RevokeOnTableBriefContext } from "./StarRocksParser";
import { RevokeOnFuncContext } from "./StarRocksParser";
import { RevokeOnSystemContext } from "./StarRocksParser";
import { RevokeOnPrimaryObjContext } from "./StarRocksParser";
import { RevokeOnAllContext } from "./StarRocksParser";
import { RollupContext } from "./StarRocksParser";
import { CubeContext } from "./StarRocksParser";
import { MultipleGroupingSetsContext } from "./StarRocksParser";
import { SingleGroupingSetContext } from "./StarRocksParser";
import { UserVariableExpressionContext } from "./StarRocksParser";
import { SystemVariableExpressionContext } from "./StarRocksParser";
import { FunctionCallExpressionContext } from "./StarRocksParser";
import { OdbcFunctionCallExpressionContext } from "./StarRocksParser";
import { CollateContext } from "./StarRocksParser";
import { LiteralContext } from "./StarRocksParser";
import { ColumnRefContext } from "./StarRocksParser";
import { DereferenceContext } from "./StarRocksParser";
import { ConcatContext } from "./StarRocksParser";
import { ArithmeticUnaryContext } from "./StarRocksParser";
import { ParenthesizedExpressionContext } from "./StarRocksParser";
import { ExistsContext } from "./StarRocksParser";
import { SubqueryExpressionContext } from "./StarRocksParser";
import { CastContext } from "./StarRocksParser";
import { ConvertContext } from "./StarRocksParser";
import { SimpleCaseContext } from "./StarRocksParser";
import { SearchedCaseContext } from "./StarRocksParser";
import { ArrayConstructorContext } from "./StarRocksParser";
import { MapConstructorContext } from "./StarRocksParser";
import { CollectionSubscriptContext } from "./StarRocksParser";
import { ArraySliceContext } from "./StarRocksParser";
import { ArrowExpressionContext } from "./StarRocksParser";
import { LambdaFunctionExprContext } from "./StarRocksParser";
import { ExpressionDefaultContext } from "./StarRocksParser";
import { LogicalNotContext } from "./StarRocksParser";
import { LogicalBinaryContext } from "./StarRocksParser";
import { ShowAllAuthenticationContext } from "./StarRocksParser";
import { ShowAuthenticationForUserContext } from "./StarRocksParser";
import { UnquotedIdentifierContext } from "./StarRocksParser";
import { DigitIdentifierContext } from "./StarRocksParser";
import { BackQuotedIdentifierContext } from "./StarRocksParser";
import { ValueExpressionDefaultContext } from "./StarRocksParser";
import { ArithmeticBinaryContext } from "./StarRocksParser";
import { QueryPrimaryDefaultContext } from "./StarRocksParser";
import { QueryWithParenthesesContext } from "./StarRocksParser";
import { SetOperationContext } from "./StarRocksParser";
import { SelectSingleContext } from "./StarRocksParser";
import { SelectAllContext } from "./StarRocksParser";
import { RevokeRoleFromUserContext } from "./StarRocksParser";
import { RevokeRoleFromRoleContext } from "./StarRocksParser";
import { AuthWithoutPluginContext } from "./StarRocksParser";
import { AuthWithPluginContext } from "./StarRocksParser";
import { UserWithoutHostContext } from "./StarRocksParser";
import { UserWithHostContext } from "./StarRocksParser";
import { UserWithHostAndBlanketContext } from "./StarRocksParser";
import { ProgramContext } from "./StarRocksParser";
import { SqlStatementsContext } from "./StarRocksParser";
import { SingleStatementContext } from "./StarRocksParser";
import { StatementContext } from "./StarRocksParser";
import { UseDatabaseStatementContext } from "./StarRocksParser";
import { UseCatalogStatementContext } from "./StarRocksParser";
import { SetCatalogStatementContext } from "./StarRocksParser";
import { ShowDatabasesStatementContext } from "./StarRocksParser";
import { AlterDbQuotaStatementContext } from "./StarRocksParser";
import { CreateDbStatementContext } from "./StarRocksParser";
import { DropDbStatementContext } from "./StarRocksParser";
import { ShowCreateDbStatementContext } from "./StarRocksParser";
import { AlterDatabaseRenameStatementContext } from "./StarRocksParser";
import { RecoverDbStmtContext } from "./StarRocksParser";
import { ShowDataStmtContext } from "./StarRocksParser";
import { CreateTableStatementContext } from "./StarRocksParser";
import { ColumnDescContext } from "./StarRocksParser";
import { CharsetNameContext } from "./StarRocksParser";
import { DefaultDescContext } from "./StarRocksParser";
import { MaterializedColumnDescContext } from "./StarRocksParser";
import { IndexDescContext } from "./StarRocksParser";
import { EngineDescContext } from "./StarRocksParser";
import { CharsetDescContext } from "./StarRocksParser";
import { CollateDescContext } from "./StarRocksParser";
import { KeyDescContext } from "./StarRocksParser";
import { OrderByDescContext } from "./StarRocksParser";
import { AggDescContext } from "./StarRocksParser";
import { RollupDescContext } from "./StarRocksParser";
import { RollupItemContext } from "./StarRocksParser";
import { DupKeysContext } from "./StarRocksParser";
import { FromRollupContext } from "./StarRocksParser";
import { WithMaskingPolicyContext } from "./StarRocksParser";
import { WithRowAccessPolicyContext } from "./StarRocksParser";
import { CreateTemporaryTableStatementContext } from "./StarRocksParser";
import { CreateTableAsSelectStatementContext } from "./StarRocksParser";
import { DropTableStatementContext } from "./StarRocksParser";
import { AlterTableStatementContext } from "./StarRocksParser";
import { CreateIndexStatementContext } from "./StarRocksParser";
import { DropIndexStatementContext } from "./StarRocksParser";
import { IndexTypeContext } from "./StarRocksParser";
import { ShowTableStatementContext } from "./StarRocksParser";
import { ShowCreateTableStatementContext } from "./StarRocksParser";
import { ShowColumnStatementContext } from "./StarRocksParser";
import { ShowTableStatusStatementContext } from "./StarRocksParser";
import { RefreshTableStatementContext } from "./StarRocksParser";
import { ShowAlterStatementContext } from "./StarRocksParser";
import { DescTableStatementContext } from "./StarRocksParser";
import { CreateTableLikeStatementContext } from "./StarRocksParser";
import { ShowIndexStatementContext } from "./StarRocksParser";
import { RecoverTableStatementContext } from "./StarRocksParser";
import { TruncateTableStatementContext } from "./StarRocksParser";
import { CancelAlterTableStatementContext } from "./StarRocksParser";
import { ShowPartitionsStatementContext } from "./StarRocksParser";
import { RecoverPartitionStatementContext } from "./StarRocksParser";
import { CreateViewStatementContext } from "./StarRocksParser";
import { AlterViewStatementContext } from "./StarRocksParser";
import { DropViewStatementContext } from "./StarRocksParser";
import { ColumnNameWithCommentContext } from "./StarRocksParser";
import { SubmitTaskStatementContext } from "./StarRocksParser";
import { TaskExecSqlContext } from "./StarRocksParser";
import { DropTaskStatementContext } from "./StarRocksParser";
import { CreateMaterializedViewStatementContext } from "./StarRocksParser";
import { MaterializedViewDescContext } from "./StarRocksParser";
import { ShowMaterializedViewsStatementContext } from "./StarRocksParser";
import { DropMaterializedViewStatementContext } from "./StarRocksParser";
import { AlterMaterializedViewStatementContext } from "./StarRocksParser";
import { RefreshMaterializedViewStatementContext } from "./StarRocksParser";
import { CancelRefreshMaterializedViewStatementContext } from "./StarRocksParser";
import { AdminSetConfigStatementContext } from "./StarRocksParser";
import { AdminSetReplicaStatusStatementContext } from "./StarRocksParser";
import { AdminShowConfigStatementContext } from "./StarRocksParser";
import { AdminShowReplicaDistributionStatementContext } from "./StarRocksParser";
import { AdminShowReplicaStatusStatementContext } from "./StarRocksParser";
import { AdminRepairTableStatementContext } from "./StarRocksParser";
import { AdminCancelRepairTableStatementContext } from "./StarRocksParser";
import { AdminCheckTabletsStatementContext } from "./StarRocksParser";
import { KillStatementContext } from "./StarRocksParser";
import { SyncStatementContext } from "./StarRocksParser";
import { AlterSystemStatementContext } from "./StarRocksParser";
import { CancelAlterSystemStatementContext } from "./StarRocksParser";
import { ShowComputeNodesStatementContext } from "./StarRocksParser";
import { CreateExternalCatalogStatementContext } from "./StarRocksParser";
import { ShowCreateExternalCatalogStatementContext } from "./StarRocksParser";
import { DropExternalCatalogStatementContext } from "./StarRocksParser";
import { ShowCatalogsStatementContext } from "./StarRocksParser";
import { CreateWarehouseStatementContext } from "./StarRocksParser";
import { ShowWarehousesStatementContext } from "./StarRocksParser";
import { DropWarehouseStatementContext } from "./StarRocksParser";
import { AlterWarehouseStatementContext } from "./StarRocksParser";
import { ShowClustersStatementContext } from "./StarRocksParser";
import { SuspendWarehouseStatementContext } from "./StarRocksParser";
import { ResumeWarehouseStatementContext } from "./StarRocksParser";
import { CreateStorageVolumeStatementContext } from "./StarRocksParser";
import { TypeDescContext } from "./StarRocksParser";
import { LocationsDescContext } from "./StarRocksParser";
import { ShowStorageVolumesStatementContext } from "./StarRocksParser";
import { DropStorageVolumeStatementContext } from "./StarRocksParser";
import { AlterStorageVolumeStatementContext } from "./StarRocksParser";
import { AlterStorageVolumeClauseContext } from "./StarRocksParser";
import { ModifyStorageVolumePropertiesClauseContext } from "./StarRocksParser";
import { ModifyStorageVolumeCommentClauseContext } from "./StarRocksParser";
import { DescStorageVolumeStatementContext } from "./StarRocksParser";
import { SetDefaultStorageVolumeStatementContext } from "./StarRocksParser";
import { AlterClauseContext } from "./StarRocksParser";
import { AddFrontendClauseContext } from "./StarRocksParser";
import { DropFrontendClauseContext } from "./StarRocksParser";
import { ModifyFrontendHostClauseContext } from "./StarRocksParser";
import { AddBackendClauseContext } from "./StarRocksParser";
import { DropBackendClauseContext } from "./StarRocksParser";
import { DecommissionBackendClauseContext } from "./StarRocksParser";
import { ModifyBackendHostClauseContext } from "./StarRocksParser";
import { AddComputeNodeClauseContext } from "./StarRocksParser";
import { DropComputeNodeClauseContext } from "./StarRocksParser";
import { ModifyBrokerClauseContext } from "./StarRocksParser";
import { AlterLoadErrorUrlClauseContext } from "./StarRocksParser";
import { CreateImageClauseContext } from "./StarRocksParser";
import { CleanTabletSchedQClauseContext } from "./StarRocksParser";
import { CreateIndexClauseContext } from "./StarRocksParser";
import { DropIndexClauseContext } from "./StarRocksParser";
import { TableRenameClauseContext } from "./StarRocksParser";
import { SwapTableClauseContext } from "./StarRocksParser";
import { ModifyTablePropertiesClauseContext } from "./StarRocksParser";
import { ModifyCommentClauseContext } from "./StarRocksParser";
import { AddColumnClauseContext } from "./StarRocksParser";
import { AddColumnsClauseContext } from "./StarRocksParser";
import { DropColumnClauseContext } from "./StarRocksParser";
import { ModifyColumnClauseContext } from "./StarRocksParser";
import { ColumnRenameClauseContext } from "./StarRocksParser";
import { ReorderColumnsClauseContext } from "./StarRocksParser";
import { RollupRenameClauseContext } from "./StarRocksParser";
import { CompactionClauseContext } from "./StarRocksParser";
import { ApplyMaskingPolicyClauseContext } from "./StarRocksParser";
import { ApplyRowAccessPolicyClauseContext } from "./StarRocksParser";
import { AddPartitionClauseContext } from "./StarRocksParser";
import { DropPartitionClauseContext } from "./StarRocksParser";
import { TruncatePartitionClauseContext } from "./StarRocksParser";
import { ModifyPartitionClauseContext } from "./StarRocksParser";
import { ReplacePartitionClauseContext } from "./StarRocksParser";
import { PartitionRenameClauseContext } from "./StarRocksParser";
import { InsertStatementContext } from "./StarRocksParser";
import { UpdateStatementContext } from "./StarRocksParser";
import { DeleteStatementContext } from "./StarRocksParser";
import { CreateRoutineLoadStatementContext } from "./StarRocksParser";
import { AlterRoutineLoadStatementContext } from "./StarRocksParser";
import { DataSourceContext } from "./StarRocksParser";
import { LoadPropertiesExprContext } from "./StarRocksParser";
import { LoadPropertiesContext } from "./StarRocksParser";
import { ColSeparatorPropertyContext } from "./StarRocksParser";
import { RowDelimiterPropertyContext } from "./StarRocksParser";
import { ImportColumnsContext } from "./StarRocksParser";
import { ColumnPropertiesContext } from "./StarRocksParser";
import { JobPropertiesContext } from "./StarRocksParser";
import { DataSourcePropertiesContext } from "./StarRocksParser";
import { StopRoutineLoadStatementContext } from "./StarRocksParser";
import { ResumeRoutineLoadStatementContext } from "./StarRocksParser";
import { PauseRoutineLoadStatementContext } from "./StarRocksParser";
import { ShowRoutineLoadStatementContext } from "./StarRocksParser";
import { ShowRoutineLoadTaskStatementContext } from "./StarRocksParser";
import { ShowStreamLoadStatementContext } from "./StarRocksParser";
import { AnalyzeStatementContext } from "./StarRocksParser";
import { DropStatsStatementContext } from "./StarRocksParser";
import { AnalyzeHistogramStatementContext } from "./StarRocksParser";
import { DropHistogramStatementContext } from "./StarRocksParser";
import { CreateAnalyzeStatementContext } from "./StarRocksParser";
import { DropAnalyzeJobStatementContext } from "./StarRocksParser";
import { ShowAnalyzeStatementContext } from "./StarRocksParser";
import { ShowStatsMetaStatementContext } from "./StarRocksParser";
import { ShowHistogramMetaStatementContext } from "./StarRocksParser";
import { KillAnalyzeStatementContext } from "./StarRocksParser";
import { AnalyzeProfileStatementContext } from "./StarRocksParser";
import { CreateResourceGroupStatementContext } from "./StarRocksParser";
import { DropResourceGroupStatementContext } from "./StarRocksParser";
import { AlterResourceGroupStatementContext } from "./StarRocksParser";
import { ShowResourceGroupStatementContext } from "./StarRocksParser";
import { CreateResourceStatementContext } from "./StarRocksParser";
import { AlterResourceStatementContext } from "./StarRocksParser";
import { DropResourceStatementContext } from "./StarRocksParser";
import { ShowResourceStatementContext } from "./StarRocksParser";
import { ClassifierContext } from "./StarRocksParser";
import { ShowFunctionsStatementContext } from "./StarRocksParser";
import { DropFunctionStatementContext } from "./StarRocksParser";
import { CreateFunctionStatementContext } from "./StarRocksParser";
import { TypeListContext } from "./StarRocksParser";
import { LoadStatementContext } from "./StarRocksParser";
import { LabelNameContext } from "./StarRocksParser";
import { DataDescListContext } from "./StarRocksParser";
import { DataDescContext } from "./StarRocksParser";
import { FormatPropsContext } from "./StarRocksParser";
import { BrokerDescContext } from "./StarRocksParser";
import { ResourceDescContext } from "./StarRocksParser";
import { ShowLoadStatementContext } from "./StarRocksParser";
import { ShowLoadWarningsStatementContext } from "./StarRocksParser";
import { CancelLoadStatementContext } from "./StarRocksParser";
import { AlterLoadStatementContext } from "./StarRocksParser";
import { CancelCompactionStatementContext } from "./StarRocksParser";
import { ShowAuthorStatementContext } from "./StarRocksParser";
import { ShowBackendsStatementContext } from "./StarRocksParser";
import { ShowBrokerStatementContext } from "./StarRocksParser";
import { ShowCharsetStatementContext } from "./StarRocksParser";
import { ShowCollationStatementContext } from "./StarRocksParser";
import { ShowDeleteStatementContext } from "./StarRocksParser";
import { ShowDynamicPartitionStatementContext } from "./StarRocksParser";
import { ShowEventsStatementContext } from "./StarRocksParser";
import { ShowEnginesStatementContext } from "./StarRocksParser";
import { ShowFrontendsStatementContext } from "./StarRocksParser";
import { ShowPluginsStatementContext } from "./StarRocksParser";
import { ShowRepositoriesStatementContext } from "./StarRocksParser";
import { ShowOpenTableStatementContext } from "./StarRocksParser";
import { ShowPrivilegesStatementContext } from "./StarRocksParser";
import { ShowProcedureStatementContext } from "./StarRocksParser";
import { ShowProcStatementContext } from "./StarRocksParser";
import { ShowProcesslistStatementContext } from "./StarRocksParser";
import { ShowProfilelistStatementContext } from "./StarRocksParser";
import { ShowStatusStatementContext } from "./StarRocksParser";
import { ShowTabletStatementContext } from "./StarRocksParser";
import { ShowTransactionStatementContext } from "./StarRocksParser";
import { ShowTriggersStatementContext } from "./StarRocksParser";
import { ShowUserPropertyStatementContext } from "./StarRocksParser";
import { ShowVariablesStatementContext } from "./StarRocksParser";
import { ShowWarningStatementContext } from "./StarRocksParser";
import { HelpStatementContext } from "./StarRocksParser";
import { CreateUserStatementContext } from "./StarRocksParser";
import { DropUserStatementContext } from "./StarRocksParser";
import { AlterUserStatementContext } from "./StarRocksParser";
import { ShowUserStatementContext } from "./StarRocksParser";
import { ShowAuthenticationStatementContext } from "./StarRocksParser";
import { ExecuteAsStatementContext } from "./StarRocksParser";
import { CreateRoleStatementContext } from "./StarRocksParser";
import { AlterRoleStatementContext } from "./StarRocksParser";
import { DropRoleStatementContext } from "./StarRocksParser";
import { ShowRolesStatementContext } from "./StarRocksParser";
import { GrantRoleStatementContext } from "./StarRocksParser";
import { RevokeRoleStatementContext } from "./StarRocksParser";
import { SetRoleStatementContext } from "./StarRocksParser";
import { SetDefaultRoleStatementContext } from "./StarRocksParser";
import { GrantRevokeClauseContext } from "./StarRocksParser";
import { GrantPrivilegeStatementContext } from "./StarRocksParser";
import { RevokePrivilegeStatementContext } from "./StarRocksParser";
import { ShowGrantsStatementContext } from "./StarRocksParser";
import { CreateSecurityIntegrationStatementContext } from "./StarRocksParser";
import { AlterSecurityIntegrationStatementContext } from "./StarRocksParser";
import { DropSecurityIntegrationStatementContext } from "./StarRocksParser";
import { ShowSecurityIntegrationStatementContext } from "./StarRocksParser";
import { ShowCreateSecurityIntegrationStatementContext } from "./StarRocksParser";
import { CreateRoleMappingStatementContext } from "./StarRocksParser";
import { AlterRoleMappingStatementContext } from "./StarRocksParser";
import { DropRoleMappingStatementContext } from "./StarRocksParser";
import { ShowRoleMappingStatementContext } from "./StarRocksParser";
import { RefreshRoleMappingStatementContext } from "./StarRocksParser";
import { AuthOptionContext } from "./StarRocksParser";
import { PrivObjectNameContext } from "./StarRocksParser";
import { PrivObjectNameListContext } from "./StarRocksParser";
import { PrivFunctionObjectNameListContext } from "./StarRocksParser";
import { PrivilegeTypeListContext } from "./StarRocksParser";
import { PrivilegeTypeContext } from "./StarRocksParser";
import { PrivObjectTypeContext } from "./StarRocksParser";
import { PrivObjectTypePluralContext } from "./StarRocksParser";
import { CreateMaskingPolicyStatementContext } from "./StarRocksParser";
import { DropMaskingPolicyStatementContext } from "./StarRocksParser";
import { AlterMaskingPolicyStatementContext } from "./StarRocksParser";
import { ShowMaskingPolicyStatementContext } from "./StarRocksParser";
import { ShowCreateMaskingPolicyStatementContext } from "./StarRocksParser";
import { CreateRowAccessPolicyStatementContext } from "./StarRocksParser";
import { DropRowAccessPolicyStatementContext } from "./StarRocksParser";
import { AlterRowAccessPolicyStatementContext } from "./StarRocksParser";
import { ShowRowAccessPolicyStatementContext } from "./StarRocksParser";
import { ShowCreateRowAccessPolicyStatementContext } from "./StarRocksParser";
import { PolicySignatureContext } from "./StarRocksParser";
import { BackupStatementContext } from "./StarRocksParser";
import { CancelBackupStatementContext } from "./StarRocksParser";
import { ShowBackupStatementContext } from "./StarRocksParser";
import { RestoreStatementContext } from "./StarRocksParser";
import { CancelRestoreStatementContext } from "./StarRocksParser";
import { ShowRestoreStatementContext } from "./StarRocksParser";
import { ShowSnapshotStatementContext } from "./StarRocksParser";
import { CreateRepositoryStatementContext } from "./StarRocksParser";
import { DropRepositoryStatementContext } from "./StarRocksParser";
import { AddSqlBlackListStatementContext } from "./StarRocksParser";
import { DelSqlBlackListStatementContext } from "./StarRocksParser";
import { ShowSqlBlackListStatementContext } from "./StarRocksParser";
import { ShowWhiteListStatementContext } from "./StarRocksParser";
import { ExportStatementContext } from "./StarRocksParser";
import { CancelExportStatementContext } from "./StarRocksParser";
import { ShowExportStatementContext } from "./StarRocksParser";
import { InstallPluginStatementContext } from "./StarRocksParser";
import { UninstallPluginStatementContext } from "./StarRocksParser";
import { CreateFileStatementContext } from "./StarRocksParser";
import { DropFileStatementContext } from "./StarRocksParser";
import { ShowSmallFilesStatementContext } from "./StarRocksParser";
import { SetStatementContext } from "./StarRocksParser";
import { SetVarContext } from "./StarRocksParser";
import { Transaction_characteristicsContext } from "./StarRocksParser";
import { Transaction_access_modeContext } from "./StarRocksParser";
import { Isolation_levelContext } from "./StarRocksParser";
import { Isolation_typesContext } from "./StarRocksParser";
import { SetExprOrDefaultContext } from "./StarRocksParser";
import { SetUserPropertyStatementContext } from "./StarRocksParser";
import { RoleListContext } from "./StarRocksParser";
import { ExecuteScriptStatementContext } from "./StarRocksParser";
import { UnsupportedStatementContext } from "./StarRocksParser";
import { Lock_itemContext } from "./StarRocksParser";
import { Lock_typeContext } from "./StarRocksParser";
import { QueryStatementContext } from "./StarRocksParser";
import { QueryRelationContext } from "./StarRocksParser";
import { WithClauseContext } from "./StarRocksParser";
import { QueryNoWithContext } from "./StarRocksParser";
import { TemporalClauseContext } from "./StarRocksParser";
import { QueryPrimaryContext } from "./StarRocksParser";
import { SubqueryContext } from "./StarRocksParser";
import { RowConstructorContext } from "./StarRocksParser";
import { SortItemContext } from "./StarRocksParser";
import { LimitElementContext } from "./StarRocksParser";
import { QuerySpecificationContext } from "./StarRocksParser";
import { FromClauseContext } from "./StarRocksParser";
import { GroupingElementContext } from "./StarRocksParser";
import { GroupingSetContext } from "./StarRocksParser";
import { CommonTableExpressionContext } from "./StarRocksParser";
import { SetQuantifierContext } from "./StarRocksParser";
import { SelectItemContext } from "./StarRocksParser";
import { RelationsContext } from "./StarRocksParser";
import { RelationContext } from "./StarRocksParser";
import { RelationPrimaryContext } from "./StarRocksParser";
import { JoinRelationContext } from "./StarRocksParser";
import { CrossOrInnerJoinTypeContext } from "./StarRocksParser";
import { OuterAndSemiJoinTypeContext } from "./StarRocksParser";
import { BracketHintContext } from "./StarRocksParser";
import { SetVarHintContext } from "./StarRocksParser";
import { HintMapContext } from "./StarRocksParser";
import { JoinCriteriaContext } from "./StarRocksParser";
import { ColumnAliasesContext } from "./StarRocksParser";
import { PartitionNamesContext } from "./StarRocksParser";
import { KeyPartitionsContext } from "./StarRocksParser";
import { TabletListContext } from "./StarRocksParser";
import { ExpressionsWithDefaultContext } from "./StarRocksParser";
import { ExpressionOrDefaultContext } from "./StarRocksParser";
import { MapExpressionListContext } from "./StarRocksParser";
import { MapExpressionContext } from "./StarRocksParser";
import { ExpressionSingletonContext } from "./StarRocksParser";
import { ExpressionContext } from "./StarRocksParser";
import { ExpressionListContext } from "./StarRocksParser";
import { BooleanExpressionContext } from "./StarRocksParser";
import { PredicateContext } from "./StarRocksParser";
import { TupleInSubqueryContext } from "./StarRocksParser";
import { PredicateOperationsContext } from "./StarRocksParser";
import { ValueExpressionContext } from "./StarRocksParser";
import { PrimaryExpressionContext } from "./StarRocksParser";
import { LiteralExpressionContext } from "./StarRocksParser";
import { FunctionCallContext } from "./StarRocksParser";
import { AggregationFunctionContext } from "./StarRocksParser";
import { UserVariableContext } from "./StarRocksParser";
import { SystemVariableContext } from "./StarRocksParser";
import { ColumnReferenceContext } from "./StarRocksParser";
import { InformationFunctionExpressionContext } from "./StarRocksParser";
import { SpecialDateTimeExpressionContext } from "./StarRocksParser";
import { SpecialFunctionExpressionContext } from "./StarRocksParser";
import { WindowFunctionContext } from "./StarRocksParser";
import { WhenClauseContext } from "./StarRocksParser";
import { OverContext } from "./StarRocksParser";
import { IgnoreNullsContext } from "./StarRocksParser";
import { TableDescContext } from "./StarRocksParser";
import { RestoreTableDescContext } from "./StarRocksParser";
import { ExplainDescContext } from "./StarRocksParser";
import { OptimizerTraceContext } from "./StarRocksParser";
import { PartitionDescContext } from "./StarRocksParser";
import { ListPartitionDescContext } from "./StarRocksParser";
import { SingleItemListPartitionDescContext } from "./StarRocksParser";
import { MultiItemListPartitionDescContext } from "./StarRocksParser";
import { StringListContext } from "./StarRocksParser";
import { RangePartitionDescContext } from "./StarRocksParser";
import { SingleRangePartitionContext } from "./StarRocksParser";
import { MultiRangePartitionContext } from "./StarRocksParser";
import { PartitionRangeDescContext } from "./StarRocksParser";
import { PartitionKeyDescContext } from "./StarRocksParser";
import { PartitionValueListContext } from "./StarRocksParser";
import { KeyPartitionContext } from "./StarRocksParser";
import { PartitionValueContext } from "./StarRocksParser";
import { DistributionClauseContext } from "./StarRocksParser";
import { DistributionDescContext } from "./StarRocksParser";
import { RefreshSchemeDescContext } from "./StarRocksParser";
import { StatusDescContext } from "./StarRocksParser";
import { PropertiesContext } from "./StarRocksParser";
import { ExtPropertiesContext } from "./StarRocksParser";
import { PropertyListContext } from "./StarRocksParser";
import { UserPropertyListContext } from "./StarRocksParser";
import { PropertyContext } from "./StarRocksParser";
import { VarTypeContext } from "./StarRocksParser";
import { CommentContext } from "./StarRocksParser";
import { OutfileContext } from "./StarRocksParser";
import { FileFormatContext } from "./StarRocksParser";
import { StringContext } from "./StarRocksParser";
import { BinaryContext } from "./StarRocksParser";
import { ComparisonOperatorContext } from "./StarRocksParser";
import { BooleanValueContext } from "./StarRocksParser";
import { IntervalContext } from "./StarRocksParser";
import { UnitIdentifierContext } from "./StarRocksParser";
import { UnitBoundaryContext } from "./StarRocksParser";
import { TypeContext } from "./StarRocksParser";
import { ArrayTypeContext } from "./StarRocksParser";
import { MapTypeContext } from "./StarRocksParser";
import { SubfieldDescContext } from "./StarRocksParser";
import { SubfieldDescsContext } from "./StarRocksParser";
import { StructTypeContext } from "./StarRocksParser";
import { TypeParameterContext } from "./StarRocksParser";
import { BaseTypeContext } from "./StarRocksParser";
import { DecimalTypeContext } from "./StarRocksParser";
import { QualifiedNameContext } from "./StarRocksParser";
import { IdentifierContext } from "./StarRocksParser";
import { IdentifierListContext } from "./StarRocksParser";
import { IdentifierOrStringContext } from "./StarRocksParser";
import { IdentifierOrStringListContext } from "./StarRocksParser";
import { IdentifierOrStringOrStarContext } from "./StarRocksParser";
import { UserContext } from "./StarRocksParser";
import { AssignmentContext } from "./StarRocksParser";
import { AssignmentListContext } from "./StarRocksParser";
import { NumberContext } from "./StarRocksParser";
import { NonReservedContext } from "./StarRocksParser";
/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by `StarRocksParser`.
 *
 * @param <Result> The return type of the visit operation. Use `void` for
 * operations with no return type.
 */
export interface StarRocksParserVisitor<Result> extends ParseTreeVisitor<Result> {
    /**
     * Visit a parse tree produced by the `tableAtom`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableAtom?: (ctx: TableAtomContext) => Result;
    /**
     * Visit a parse tree produced by the `inlineTable`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInlineTable?: (ctx: InlineTableContext) => Result;
    /**
     * Visit a parse tree produced by the `subqueryWithAlias`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubqueryWithAlias?: (ctx: SubqueryWithAliasContext) => Result;
    /**
     * Visit a parse tree produced by the `tableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableFunction?: (ctx: TableFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `normalizedTableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNormalizedTableFunction?: (ctx: NormalizedTableFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `fileTableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFileTableFunction?: (ctx: FileTableFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `parenthesizedRelation`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParenthesizedRelation?: (ctx: ParenthesizedRelationContext) => Result;
    /**
     * Visit a parse tree produced by the `decimalValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDecimalValue?: (ctx: DecimalValueContext) => Result;
    /**
     * Visit a parse tree produced by the `doubleValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDoubleValue?: (ctx: DoubleValueContext) => Result;
    /**
     * Visit a parse tree produced by the `integerValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntegerValue?: (ctx: IntegerValueContext) => Result;
    /**
     * Visit a parse tree produced by the `extract`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExtract?: (ctx: ExtractContext) => Result;
    /**
     * Visit a parse tree produced by the `groupingOperation`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroupingOperation?: (ctx: GroupingOperationContext) => Result;
    /**
     * Visit a parse tree produced by the `informationFunction`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInformationFunction?: (ctx: InformationFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `specialDateTime`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialDateTime?: (ctx: SpecialDateTimeContext) => Result;
    /**
     * Visit a parse tree produced by the `specialFunction`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialFunction?: (ctx: SpecialFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `aggregationFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAggregationFunctionCall?: (ctx: AggregationFunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by the `windowFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWindowFunctionCall?: (ctx: WindowFunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by the `simpleFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSimpleFunctionCall?: (ctx: SimpleFunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by the `from`
     * labeled alternative in `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFrom?: (ctx: FromContext) => Result;
    /**
     * Visit a parse tree produced by the `dual`
     * labeled alternative in `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDual?: (ctx: DualContext) => Result;
    /**
     * Visit a parse tree produced by the `setNames`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetNames?: (ctx: SetNamesContext) => Result;
    /**
     * Visit a parse tree produced by the `setPassword`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetPassword?: (ctx: SetPasswordContext) => Result;
    /**
     * Visit a parse tree produced by the `setUserVar`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetUserVar?: (ctx: SetUserVarContext) => Result;
    /**
     * Visit a parse tree produced by the `setSystemVar`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetSystemVar?: (ctx: SetSystemVarContext) => Result;
    /**
     * Visit a parse tree produced by the `setTransaction`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetTransaction?: (ctx: SetTransactionContext) => Result;
    /**
     * Visit a parse tree produced by the `keyPartitionList`
     * labeled alternative in `StarRocksParser.keyPartitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyPartitionList?: (ctx: KeyPartitionListContext) => Result;
    /**
     * Visit a parse tree produced by the `inSubquery`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInSubquery?: (ctx: InSubqueryContext) => Result;
    /**
     * Visit a parse tree produced by the `inList`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInList?: (ctx: InListContext) => Result;
    /**
     * Visit a parse tree produced by the `between`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBetween?: (ctx: BetweenContext) => Result;
    /**
     * Visit a parse tree produced by the `like`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLike?: (ctx: LikeContext) => Result;
    /**
     * Visit a parse tree produced by the `grantRoleToUser`
     * labeled alternative in `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRoleToUser?: (ctx: GrantRoleToUserContext) => Result;
    /**
     * Visit a parse tree produced by the `grantRoleToRole`
     * labeled alternative in `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRoleToRole?: (ctx: GrantRoleToRoleContext) => Result;
    /**
     * Visit a parse tree produced by the `booleanExpressionDefault`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanExpressionDefault?: (ctx: BooleanExpressionDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `isNull`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIsNull?: (ctx: IsNullContext) => Result;
    /**
     * Visit a parse tree produced by the `comparison`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComparison?: (ctx: ComparisonContext) => Result;
    /**
     * Visit a parse tree produced by the `scalarSubquery`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitScalarSubquery?: (ctx: ScalarSubqueryContext) => Result;
    /**
     * Visit a parse tree produced by the `nullLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNullLiteral?: (ctx: NullLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `booleanLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanLiteral?: (ctx: BooleanLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `numericLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumericLiteral?: (ctx: NumericLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `dateLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDateLiteral?: (ctx: DateLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `stringLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStringLiteral?: (ctx: StringLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `intervalLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntervalLiteral?: (ctx: IntervalLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `unitBoundaryLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnitBoundaryLiteral?: (ctx: UnitBoundaryLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `binaryLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBinaryLiteral?: (ctx: BinaryLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnUser`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnUser?: (ctx: GrantOnUserContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnTableBrief`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnTableBrief?: (ctx: GrantOnTableBriefContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnFunc`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnFunc?: (ctx: GrantOnFuncContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnSystem`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnSystem?: (ctx: GrantOnSystemContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnPrimaryObj`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnPrimaryObj?: (ctx: GrantOnPrimaryObjContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnAll`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnAll?: (ctx: GrantOnAllContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnUser`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnUser?: (ctx: RevokeOnUserContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnTableBrief`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnTableBrief?: (ctx: RevokeOnTableBriefContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnFunc`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnFunc?: (ctx: RevokeOnFuncContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnSystem`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnSystem?: (ctx: RevokeOnSystemContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnPrimaryObj`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnPrimaryObj?: (ctx: RevokeOnPrimaryObjContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnAll`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnAll?: (ctx: RevokeOnAllContext) => Result;
    /**
     * Visit a parse tree produced by the `rollup`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollup?: (ctx: RollupContext) => Result;
    /**
     * Visit a parse tree produced by the `cube`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCube?: (ctx: CubeContext) => Result;
    /**
     * Visit a parse tree produced by the `multipleGroupingSets`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultipleGroupingSets?: (ctx: MultipleGroupingSetsContext) => Result;
    /**
     * Visit a parse tree produced by the `singleGroupingSet`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleGroupingSet?: (ctx: SingleGroupingSetContext) => Result;
    /**
     * Visit a parse tree produced by the `userVariableExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserVariableExpression?: (ctx: UserVariableExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `systemVariableExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystemVariableExpression?: (ctx: SystemVariableExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `functionCallExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunctionCallExpression?: (ctx: FunctionCallExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `odbcFunctionCallExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOdbcFunctionCallExpression?: (ctx: OdbcFunctionCallExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `collate`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollate?: (ctx: CollateContext) => Result;
    /**
     * Visit a parse tree produced by the `literal`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLiteral?: (ctx: LiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `columnRef`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnRef?: (ctx: ColumnRefContext) => Result;
    /**
     * Visit a parse tree produced by the `dereference`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDereference?: (ctx: DereferenceContext) => Result;
    /**
     * Visit a parse tree produced by the `concat`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConcat?: (ctx: ConcatContext) => Result;
    /**
     * Visit a parse tree produced by the `arithmeticUnary`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArithmeticUnary?: (ctx: ArithmeticUnaryContext) => Result;
    /**
     * Visit a parse tree produced by the `parenthesizedExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParenthesizedExpression?: (ctx: ParenthesizedExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `exists`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExists?: (ctx: ExistsContext) => Result;
    /**
     * Visit a parse tree produced by the `subqueryExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubqueryExpression?: (ctx: SubqueryExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `cast`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCast?: (ctx: CastContext) => Result;
    /**
     * Visit a parse tree produced by the `convert`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConvert?: (ctx: ConvertContext) => Result;
    /**
     * Visit a parse tree produced by the `simpleCase`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSimpleCase?: (ctx: SimpleCaseContext) => Result;
    /**
     * Visit a parse tree produced by the `searchedCase`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSearchedCase?: (ctx: SearchedCaseContext) => Result;
    /**
     * Visit a parse tree produced by the `arrayConstructor`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArrayConstructor?: (ctx: ArrayConstructorContext) => Result;
    /**
     * Visit a parse tree produced by the `mapConstructor`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapConstructor?: (ctx: MapConstructorContext) => Result;
    /**
     * Visit a parse tree produced by the `collectionSubscript`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollectionSubscript?: (ctx: CollectionSubscriptContext) => Result;
    /**
     * Visit a parse tree produced by the `arraySlice`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArraySlice?: (ctx: ArraySliceContext) => Result;
    /**
     * Visit a parse tree produced by the `arrowExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArrowExpression?: (ctx: ArrowExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `lambdaFunctionExpr`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLambdaFunctionExpr?: (ctx: LambdaFunctionExprContext) => Result;
    /**
     * Visit a parse tree produced by the `expressionDefault`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionDefault?: (ctx: ExpressionDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `logicalNot`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogicalNot?: (ctx: LogicalNotContext) => Result;
    /**
     * Visit a parse tree produced by the `logicalBinary`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogicalBinary?: (ctx: LogicalBinaryContext) => Result;
    /**
     * Visit a parse tree produced by the `showAllAuthentication`
     * labeled alternative in `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAllAuthentication?: (ctx: ShowAllAuthenticationContext) => Result;
    /**
     * Visit a parse tree produced by the `showAuthenticationForUser`
     * labeled alternative in `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAuthenticationForUser?: (ctx: ShowAuthenticationForUserContext) => Result;
    /**
     * Visit a parse tree produced by the `unquotedIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnquotedIdentifier?: (ctx: UnquotedIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by the `digitIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDigitIdentifier?: (ctx: DigitIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by the `backQuotedIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBackQuotedIdentifier?: (ctx: BackQuotedIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by the `valueExpressionDefault`
     * labeled alternative in `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitValueExpressionDefault?: (ctx: ValueExpressionDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `arithmeticBinary`
     * labeled alternative in `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArithmeticBinary?: (ctx: ArithmeticBinaryContext) => Result;
    /**
     * Visit a parse tree produced by the `queryPrimaryDefault`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryPrimaryDefault?: (ctx: QueryPrimaryDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `queryWithParentheses`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryWithParentheses?: (ctx: QueryWithParenthesesContext) => Result;
    /**
     * Visit a parse tree produced by the `setOperation`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetOperation?: (ctx: SetOperationContext) => Result;
    /**
     * Visit a parse tree produced by the `selectSingle`
     * labeled alternative in `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelectSingle?: (ctx: SelectSingleContext) => Result;
    /**
     * Visit a parse tree produced by the `selectAll`
     * labeled alternative in `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelectAll?: (ctx: SelectAllContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeRoleFromUser`
     * labeled alternative in `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeRoleFromUser?: (ctx: RevokeRoleFromUserContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeRoleFromRole`
     * labeled alternative in `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeRoleFromRole?: (ctx: RevokeRoleFromRoleContext) => Result;
    /**
     * Visit a parse tree produced by the `authWithoutPlugin`
     * labeled alternative in `StarRocksParser.authOption`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuthWithoutPlugin?: (ctx: AuthWithoutPluginContext) => Result;
    /**
     * Visit a parse tree produced by the `authWithPlugin`
     * labeled alternative in `StarRocksParser.authOption`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuthWithPlugin?: (ctx: AuthWithPluginContext) => Result;
    /**
     * Visit a parse tree produced by the `userWithoutHost`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserWithoutHost?: (ctx: UserWithoutHostContext) => Result;
    /**
     * Visit a parse tree produced by the `userWithHost`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserWithHost?: (ctx: UserWithHostContext) => Result;
    /**
     * Visit a parse tree produced by the `userWithHostAndBlanket`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserWithHostAndBlanket?: (ctx: UserWithHostAndBlanketContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.program`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProgram?: (ctx: ProgramContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.sqlStatements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlStatements?: (ctx: SqlStatementsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.singleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleStatement?: (ctx: SingleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatement?: (ctx: StatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.useDatabaseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUseDatabaseStatement?: (ctx: UseDatabaseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.useCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUseCatalogStatement?: (ctx: UseCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetCatalogStatement?: (ctx: SetCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showDatabasesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDatabasesStatement?: (ctx: ShowDatabasesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterDbQuotaStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterDbQuotaStatement?: (ctx: AlterDbQuotaStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createDbStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateDbStatement?: (ctx: CreateDbStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropDbStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropDbStatement?: (ctx: DropDbStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCreateDbStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateDbStatement?: (ctx: ShowCreateDbStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterDatabaseRenameStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterDatabaseRenameStatement?: (ctx: AlterDatabaseRenameStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.recoverDbStmt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecoverDbStmt?: (ctx: RecoverDbStmtContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showDataStmt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDataStmt?: (ctx: ShowDataStmtContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTableStatement?: (ctx: CreateTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.columnDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnDesc?: (ctx: ColumnDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.charsetName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCharsetName?: (ctx: CharsetNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.defaultDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefaultDesc?: (ctx: DefaultDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.materializedColumnDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaterializedColumnDesc?: (ctx: MaterializedColumnDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.indexDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndexDesc?: (ctx: IndexDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.engineDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEngineDesc?: (ctx: EngineDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.charsetDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCharsetDesc?: (ctx: CharsetDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.collateDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollateDesc?: (ctx: CollateDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.keyDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyDesc?: (ctx: KeyDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.orderByDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOrderByDesc?: (ctx: OrderByDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.aggDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAggDesc?: (ctx: AggDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.rollupDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollupDesc?: (ctx: RollupDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.rollupItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollupItem?: (ctx: RollupItemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dupKeys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDupKeys?: (ctx: DupKeysContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.fromRollup`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFromRollup?: (ctx: FromRollupContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.withMaskingPolicy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithMaskingPolicy?: (ctx: WithMaskingPolicyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.withRowAccessPolicy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithRowAccessPolicy?: (ctx: WithRowAccessPolicyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createTemporaryTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTemporaryTableStatement?: (ctx: CreateTemporaryTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createTableAsSelectStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTableAsSelectStatement?: (ctx: CreateTableAsSelectStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropTableStatement?: (ctx: DropTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterTableStatement?: (ctx: AlterTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createIndexStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateIndexStatement?: (ctx: CreateIndexStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropIndexStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropIndexStatement?: (ctx: DropIndexStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.indexType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndexType?: (ctx: IndexTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTableStatement?: (ctx: ShowTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCreateTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateTableStatement?: (ctx: ShowCreateTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showColumnStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowColumnStatement?: (ctx: ShowColumnStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showTableStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTableStatusStatement?: (ctx: ShowTableStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.refreshTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshTableStatement?: (ctx: RefreshTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showAlterStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAlterStatement?: (ctx: ShowAlterStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.descTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDescTableStatement?: (ctx: DescTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createTableLikeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTableLikeStatement?: (ctx: CreateTableLikeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showIndexStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowIndexStatement?: (ctx: ShowIndexStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.recoverTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecoverTableStatement?: (ctx: RecoverTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.truncateTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTruncateTableStatement?: (ctx: TruncateTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelAlterTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelAlterTableStatement?: (ctx: CancelAlterTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showPartitionsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowPartitionsStatement?: (ctx: ShowPartitionsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.recoverPartitionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecoverPartitionStatement?: (ctx: RecoverPartitionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateViewStatement?: (ctx: CreateViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterViewStatement?: (ctx: AlterViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropViewStatement?: (ctx: DropViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.columnNameWithComment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnNameWithComment?: (ctx: ColumnNameWithCommentContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.submitTaskStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubmitTaskStatement?: (ctx: SubmitTaskStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.taskExecSql`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTaskExecSql?: (ctx: TaskExecSqlContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropTaskStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropTaskStatement?: (ctx: DropTaskStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateMaterializedViewStatement?: (ctx: CreateMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.materializedViewDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaterializedViewDesc?: (ctx: MaterializedViewDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showMaterializedViewsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowMaterializedViewsStatement?: (ctx: ShowMaterializedViewsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropMaterializedViewStatement?: (ctx: DropMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterMaterializedViewStatement?: (ctx: AlterMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.refreshMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshMaterializedViewStatement?: (ctx: RefreshMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelRefreshMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelRefreshMaterializedViewStatement?: (ctx: CancelRefreshMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminSetConfigStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminSetConfigStatement?: (ctx: AdminSetConfigStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminSetReplicaStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminSetReplicaStatusStatement?: (ctx: AdminSetReplicaStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminShowConfigStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminShowConfigStatement?: (ctx: AdminShowConfigStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminShowReplicaDistributionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminShowReplicaDistributionStatement?: (ctx: AdminShowReplicaDistributionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminShowReplicaStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminShowReplicaStatusStatement?: (ctx: AdminShowReplicaStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminRepairTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminRepairTableStatement?: (ctx: AdminRepairTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminCancelRepairTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminCancelRepairTableStatement?: (ctx: AdminCancelRepairTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.adminCheckTabletsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminCheckTabletsStatement?: (ctx: AdminCheckTabletsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.killStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKillStatement?: (ctx: KillStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.syncStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSyncStatement?: (ctx: SyncStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterSystemStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterSystemStatement?: (ctx: AlterSystemStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelAlterSystemStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelAlterSystemStatement?: (ctx: CancelAlterSystemStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showComputeNodesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowComputeNodesStatement?: (ctx: ShowComputeNodesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createExternalCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateExternalCatalogStatement?: (ctx: CreateExternalCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCreateExternalCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateExternalCatalogStatement?: (ctx: ShowCreateExternalCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropExternalCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropExternalCatalogStatement?: (ctx: DropExternalCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCatalogsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCatalogsStatement?: (ctx: ShowCatalogsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateWarehouseStatement?: (ctx: CreateWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showWarehousesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowWarehousesStatement?: (ctx: ShowWarehousesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropWarehouseStatement?: (ctx: DropWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterWarehouseStatement?: (ctx: AlterWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showClustersStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowClustersStatement?: (ctx: ShowClustersStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.suspendWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSuspendWarehouseStatement?: (ctx: SuspendWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.resumeWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResumeWarehouseStatement?: (ctx: ResumeWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateStorageVolumeStatement?: (ctx: CreateStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.typeDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTypeDesc?: (ctx: TypeDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.locationsDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLocationsDesc?: (ctx: LocationsDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showStorageVolumesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStorageVolumesStatement?: (ctx: ShowStorageVolumesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropStorageVolumeStatement?: (ctx: DropStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterStorageVolumeStatement?: (ctx: AlterStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterStorageVolumeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterStorageVolumeClause?: (ctx: AlterStorageVolumeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyStorageVolumePropertiesClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyStorageVolumePropertiesClause?: (ctx: ModifyStorageVolumePropertiesClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyStorageVolumeCommentClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyStorageVolumeCommentClause?: (ctx: ModifyStorageVolumeCommentClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.descStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDescStorageVolumeStatement?: (ctx: DescStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setDefaultStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetDefaultStorageVolumeStatement?: (ctx: SetDefaultStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterClause?: (ctx: AlterClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.addFrontendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddFrontendClause?: (ctx: AddFrontendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropFrontendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropFrontendClause?: (ctx: DropFrontendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyFrontendHostClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyFrontendHostClause?: (ctx: ModifyFrontendHostClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.addBackendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddBackendClause?: (ctx: AddBackendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropBackendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropBackendClause?: (ctx: DropBackendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.decommissionBackendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDecommissionBackendClause?: (ctx: DecommissionBackendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyBackendHostClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyBackendHostClause?: (ctx: ModifyBackendHostClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.addComputeNodeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddComputeNodeClause?: (ctx: AddComputeNodeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropComputeNodeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropComputeNodeClause?: (ctx: DropComputeNodeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyBrokerClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyBrokerClause?: (ctx: ModifyBrokerClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterLoadErrorUrlClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterLoadErrorUrlClause?: (ctx: AlterLoadErrorUrlClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createImageClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateImageClause?: (ctx: CreateImageClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cleanTabletSchedQClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCleanTabletSchedQClause?: (ctx: CleanTabletSchedQClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createIndexClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateIndexClause?: (ctx: CreateIndexClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropIndexClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropIndexClause?: (ctx: DropIndexClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.tableRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableRenameClause?: (ctx: TableRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.swapTableClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSwapTableClause?: (ctx: SwapTableClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyTablePropertiesClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyTablePropertiesClause?: (ctx: ModifyTablePropertiesClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyCommentClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyCommentClause?: (ctx: ModifyCommentClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.addColumnClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddColumnClause?: (ctx: AddColumnClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.addColumnsClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddColumnsClause?: (ctx: AddColumnsClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropColumnClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropColumnClause?: (ctx: DropColumnClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyColumnClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyColumnClause?: (ctx: ModifyColumnClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.columnRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnRenameClause?: (ctx: ColumnRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.reorderColumnsClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReorderColumnsClause?: (ctx: ReorderColumnsClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.rollupRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollupRenameClause?: (ctx: RollupRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.compactionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompactionClause?: (ctx: CompactionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.applyMaskingPolicyClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitApplyMaskingPolicyClause?: (ctx: ApplyMaskingPolicyClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.applyRowAccessPolicyClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitApplyRowAccessPolicyClause?: (ctx: ApplyRowAccessPolicyClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.addPartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddPartitionClause?: (ctx: AddPartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropPartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropPartitionClause?: (ctx: DropPartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.truncatePartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTruncatePartitionClause?: (ctx: TruncatePartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.modifyPartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyPartitionClause?: (ctx: ModifyPartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.replacePartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReplacePartitionClause?: (ctx: ReplacePartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.partitionRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionRenameClause?: (ctx: PartitionRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.insertStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInsertStatement?: (ctx: InsertStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.updateStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdateStatement?: (ctx: UpdateStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.deleteStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDeleteStatement?: (ctx: DeleteStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRoutineLoadStatement?: (ctx: CreateRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRoutineLoadStatement?: (ctx: AlterRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dataSource`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataSource?: (ctx: DataSourceContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.loadPropertiesExpr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLoadPropertiesExpr?: (ctx: LoadPropertiesExprContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.loadProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLoadProperties?: (ctx: LoadPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.colSeparatorProperty`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColSeparatorProperty?: (ctx: ColSeparatorPropertyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.rowDelimiterProperty`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRowDelimiterProperty?: (ctx: RowDelimiterPropertyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.importColumns`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImportColumns?: (ctx: ImportColumnsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.columnProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnProperties?: (ctx: ColumnPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.jobProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJobProperties?: (ctx: JobPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dataSourceProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataSourceProperties?: (ctx: DataSourcePropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.stopRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStopRoutineLoadStatement?: (ctx: StopRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.resumeRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResumeRoutineLoadStatement?: (ctx: ResumeRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.pauseRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPauseRoutineLoadStatement?: (ctx: PauseRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRoutineLoadStatement?: (ctx: ShowRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showRoutineLoadTaskStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRoutineLoadTaskStatement?: (ctx: ShowRoutineLoadTaskStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showStreamLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStreamLoadStatement?: (ctx: ShowStreamLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.analyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnalyzeStatement?: (ctx: AnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropStatsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropStatsStatement?: (ctx: DropStatsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.analyzeHistogramStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnalyzeHistogramStatement?: (ctx: AnalyzeHistogramStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropHistogramStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropHistogramStatement?: (ctx: DropHistogramStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createAnalyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateAnalyzeStatement?: (ctx: CreateAnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropAnalyzeJobStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropAnalyzeJobStatement?: (ctx: DropAnalyzeJobStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showAnalyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAnalyzeStatement?: (ctx: ShowAnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showStatsMetaStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStatsMetaStatement?: (ctx: ShowStatsMetaStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showHistogramMetaStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowHistogramMetaStatement?: (ctx: ShowHistogramMetaStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.killAnalyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKillAnalyzeStatement?: (ctx: KillAnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.analyzeProfileStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnalyzeProfileStatement?: (ctx: AnalyzeProfileStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateResourceGroupStatement?: (ctx: CreateResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropResourceGroupStatement?: (ctx: DropResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterResourceGroupStatement?: (ctx: AlterResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowResourceGroupStatement?: (ctx: ShowResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateResourceStatement?: (ctx: CreateResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterResourceStatement?: (ctx: AlterResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropResourceStatement?: (ctx: DropResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowResourceStatement?: (ctx: ShowResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.classifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClassifier?: (ctx: ClassifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showFunctionsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowFunctionsStatement?: (ctx: ShowFunctionsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropFunctionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropFunctionStatement?: (ctx: DropFunctionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createFunctionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateFunctionStatement?: (ctx: CreateFunctionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.typeList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTypeList?: (ctx: TypeListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.loadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLoadStatement?: (ctx: LoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.labelName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLabelName?: (ctx: LabelNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dataDescList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataDescList?: (ctx: DataDescListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dataDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataDesc?: (ctx: DataDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.formatProps`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFormatProps?: (ctx: FormatPropsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.brokerDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBrokerDesc?: (ctx: BrokerDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.resourceDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResourceDesc?: (ctx: ResourceDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowLoadStatement?: (ctx: ShowLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showLoadWarningsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowLoadWarningsStatement?: (ctx: ShowLoadWarningsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelLoadStatement?: (ctx: CancelLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterLoadStatement?: (ctx: AlterLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelCompactionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelCompactionStatement?: (ctx: CancelCompactionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showAuthorStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAuthorStatement?: (ctx: ShowAuthorStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showBackendsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowBackendsStatement?: (ctx: ShowBackendsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showBrokerStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowBrokerStatement?: (ctx: ShowBrokerStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCharsetStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCharsetStatement?: (ctx: ShowCharsetStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCollationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCollationStatement?: (ctx: ShowCollationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showDeleteStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDeleteStatement?: (ctx: ShowDeleteStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showDynamicPartitionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDynamicPartitionStatement?: (ctx: ShowDynamicPartitionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showEventsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowEventsStatement?: (ctx: ShowEventsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showEnginesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowEnginesStatement?: (ctx: ShowEnginesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showFrontendsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowFrontendsStatement?: (ctx: ShowFrontendsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showPluginsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowPluginsStatement?: (ctx: ShowPluginsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showRepositoriesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRepositoriesStatement?: (ctx: ShowRepositoriesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showOpenTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowOpenTableStatement?: (ctx: ShowOpenTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showPrivilegesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowPrivilegesStatement?: (ctx: ShowPrivilegesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showProcedureStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProcedureStatement?: (ctx: ShowProcedureStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showProcStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProcStatement?: (ctx: ShowProcStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showProcesslistStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProcesslistStatement?: (ctx: ShowProcesslistStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showProfilelistStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProfilelistStatement?: (ctx: ShowProfilelistStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStatusStatement?: (ctx: ShowStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showTabletStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTabletStatement?: (ctx: ShowTabletStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showTransactionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTransactionStatement?: (ctx: ShowTransactionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showTriggersStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTriggersStatement?: (ctx: ShowTriggersStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showUserPropertyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowUserPropertyStatement?: (ctx: ShowUserPropertyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showVariablesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowVariablesStatement?: (ctx: ShowVariablesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showWarningStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowWarningStatement?: (ctx: ShowWarningStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.helpStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHelpStatement?: (ctx: HelpStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateUserStatement?: (ctx: CreateUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropUserStatement?: (ctx: DropUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterUserStatement?: (ctx: AlterUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowUserStatement?: (ctx: ShowUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAuthenticationStatement?: (ctx: ShowAuthenticationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.executeAsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExecuteAsStatement?: (ctx: ExecuteAsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRoleStatement?: (ctx: CreateRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRoleStatement?: (ctx: AlterRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRoleStatement?: (ctx: DropRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showRolesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRolesStatement?: (ctx: ShowRolesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRoleStatement?: (ctx: GrantRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeRoleStatement?: (ctx: RevokeRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetRoleStatement?: (ctx: SetRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setDefaultRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetDefaultRoleStatement?: (ctx: SetDefaultRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.grantRevokeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRevokeClause?: (ctx: GrantRevokeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantPrivilegeStatement?: (ctx: GrantPrivilegeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokePrivilegeStatement?: (ctx: RevokePrivilegeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showGrantsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowGrantsStatement?: (ctx: ShowGrantsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateSecurityIntegrationStatement?: (ctx: CreateSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterSecurityIntegrationStatement?: (ctx: AlterSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropSecurityIntegrationStatement?: (ctx: DropSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSecurityIntegrationStatement?: (ctx: ShowSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCreateSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateSecurityIntegrationStatement?: (ctx: ShowCreateSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRoleMappingStatement?: (ctx: CreateRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRoleMappingStatement?: (ctx: AlterRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRoleMappingStatement?: (ctx: DropRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRoleMappingStatement?: (ctx: ShowRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.refreshRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshRoleMappingStatement?: (ctx: RefreshRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.authOption`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuthOption?: (ctx: AuthOptionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.privObjectName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectName?: (ctx: PrivObjectNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.privObjectNameList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectNameList?: (ctx: PrivObjectNameListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.privFunctionObjectNameList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivFunctionObjectNameList?: (ctx: PrivFunctionObjectNameListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.privilegeTypeList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivilegeTypeList?: (ctx: PrivilegeTypeListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.privilegeType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivilegeType?: (ctx: PrivilegeTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.privObjectType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectType?: (ctx: PrivObjectTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.privObjectTypePlural`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectTypePlural?: (ctx: PrivObjectTypePluralContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateMaskingPolicyStatement?: (ctx: CreateMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropMaskingPolicyStatement?: (ctx: DropMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterMaskingPolicyStatement?: (ctx: AlterMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowMaskingPolicyStatement?: (ctx: ShowMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCreateMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateMaskingPolicyStatement?: (ctx: ShowCreateMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRowAccessPolicyStatement?: (ctx: CreateRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRowAccessPolicyStatement?: (ctx: DropRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.alterRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRowAccessPolicyStatement?: (ctx: AlterRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRowAccessPolicyStatement?: (ctx: ShowRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showCreateRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateRowAccessPolicyStatement?: (ctx: ShowCreateRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.policySignature`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPolicySignature?: (ctx: PolicySignatureContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.backupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBackupStatement?: (ctx: BackupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelBackupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelBackupStatement?: (ctx: CancelBackupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showBackupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowBackupStatement?: (ctx: ShowBackupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.restoreStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRestoreStatement?: (ctx: RestoreStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelRestoreStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelRestoreStatement?: (ctx: CancelRestoreStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showRestoreStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRestoreStatement?: (ctx: ShowRestoreStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showSnapshotStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSnapshotStatement?: (ctx: ShowSnapshotStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createRepositoryStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRepositoryStatement?: (ctx: CreateRepositoryStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropRepositoryStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRepositoryStatement?: (ctx: DropRepositoryStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.addSqlBlackListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddSqlBlackListStatement?: (ctx: AddSqlBlackListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.delSqlBlackListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDelSqlBlackListStatement?: (ctx: DelSqlBlackListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showSqlBlackListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSqlBlackListStatement?: (ctx: ShowSqlBlackListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showWhiteListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowWhiteListStatement?: (ctx: ShowWhiteListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.exportStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExportStatement?: (ctx: ExportStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.cancelExportStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelExportStatement?: (ctx: CancelExportStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showExportStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowExportStatement?: (ctx: ShowExportStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.installPluginStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInstallPluginStatement?: (ctx: InstallPluginStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.uninstallPluginStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUninstallPluginStatement?: (ctx: UninstallPluginStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.createFileStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateFileStatement?: (ctx: CreateFileStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.dropFileStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropFileStatement?: (ctx: DropFileStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.showSmallFilesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSmallFilesStatement?: (ctx: ShowSmallFilesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetStatement?: (ctx: SetStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetVar?: (ctx: SetVarContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.transaction_characteristics`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTransaction_characteristics?: (ctx: Transaction_characteristicsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.transaction_access_mode`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTransaction_access_mode?: (ctx: Transaction_access_modeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.isolation_level`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIsolation_level?: (ctx: Isolation_levelContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.isolation_types`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIsolation_types?: (ctx: Isolation_typesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setExprOrDefault`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetExprOrDefault?: (ctx: SetExprOrDefaultContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setUserPropertyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetUserPropertyStatement?: (ctx: SetUserPropertyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.roleList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRoleList?: (ctx: RoleListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.executeScriptStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExecuteScriptStatement?: (ctx: ExecuteScriptStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.unsupportedStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnsupportedStatement?: (ctx: UnsupportedStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.lock_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLock_item?: (ctx: Lock_itemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.lock_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLock_type?: (ctx: Lock_typeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.queryStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryStatement?: (ctx: QueryStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.queryRelation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryRelation?: (ctx: QueryRelationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.withClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithClause?: (ctx: WithClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.queryNoWith`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryNoWith?: (ctx: QueryNoWithContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.temporalClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTemporalClause?: (ctx: TemporalClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryPrimary?: (ctx: QueryPrimaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.subquery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubquery?: (ctx: SubqueryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.rowConstructor`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRowConstructor?: (ctx: RowConstructorContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.sortItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSortItem?: (ctx: SortItemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.limitElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLimitElement?: (ctx: LimitElementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.querySpecification`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuerySpecification?: (ctx: QuerySpecificationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFromClause?: (ctx: FromClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroupingElement?: (ctx: GroupingElementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.groupingSet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroupingSet?: (ctx: GroupingSetContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.commonTableExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCommonTableExpression?: (ctx: CommonTableExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setQuantifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetQuantifier?: (ctx: SetQuantifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelectItem?: (ctx: SelectItemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.relations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelations?: (ctx: RelationsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.relation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelation?: (ctx: RelationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelationPrimary?: (ctx: RelationPrimaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.joinRelation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoinRelation?: (ctx: JoinRelationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.crossOrInnerJoinType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCrossOrInnerJoinType?: (ctx: CrossOrInnerJoinTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.outerAndSemiJoinType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOuterAndSemiJoinType?: (ctx: OuterAndSemiJoinTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.bracketHint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBracketHint?: (ctx: BracketHintContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.setVarHint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetVarHint?: (ctx: SetVarHintContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.hintMap`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHintMap?: (ctx: HintMapContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.joinCriteria`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoinCriteria?: (ctx: JoinCriteriaContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.columnAliases`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnAliases?: (ctx: ColumnAliasesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.partitionNames`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionNames?: (ctx: PartitionNamesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.keyPartitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyPartitions?: (ctx: KeyPartitionsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.tabletList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTabletList?: (ctx: TabletListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.expressionsWithDefault`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionsWithDefault?: (ctx: ExpressionsWithDefaultContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.expressionOrDefault`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionOrDefault?: (ctx: ExpressionOrDefaultContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.mapExpressionList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapExpressionList?: (ctx: MapExpressionListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.mapExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapExpression?: (ctx: MapExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.expressionSingleton`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionSingleton?: (ctx: ExpressionSingletonContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpression?: (ctx: ExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.expressionList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionList?: (ctx: ExpressionListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanExpression?: (ctx: BooleanExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.predicate`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPredicate?: (ctx: PredicateContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.tupleInSubquery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTupleInSubquery?: (ctx: TupleInSubqueryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPredicateOperations?: (ctx: PredicateOperationsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitValueExpression?: (ctx: ValueExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrimaryExpression?: (ctx: PrimaryExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLiteralExpression?: (ctx: LiteralExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunctionCall?: (ctx: FunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.aggregationFunction`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAggregationFunction?: (ctx: AggregationFunctionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.userVariable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserVariable?: (ctx: UserVariableContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.systemVariable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystemVariable?: (ctx: SystemVariableContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.columnReference`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnReference?: (ctx: ColumnReferenceContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.informationFunctionExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInformationFunctionExpression?: (ctx: InformationFunctionExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.specialDateTimeExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialDateTimeExpression?: (ctx: SpecialDateTimeExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.specialFunctionExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialFunctionExpression?: (ctx: SpecialFunctionExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.windowFunction`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWindowFunction?: (ctx: WindowFunctionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.whenClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWhenClause?: (ctx: WhenClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.over`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOver?: (ctx: OverContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.ignoreNulls`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIgnoreNulls?: (ctx: IgnoreNullsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.tableDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableDesc?: (ctx: TableDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.restoreTableDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRestoreTableDesc?: (ctx: RestoreTableDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.explainDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExplainDesc?: (ctx: ExplainDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.optimizerTrace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOptimizerTrace?: (ctx: OptimizerTraceContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.partitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionDesc?: (ctx: PartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.listPartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitListPartitionDesc?: (ctx: ListPartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.singleItemListPartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleItemListPartitionDesc?: (ctx: SingleItemListPartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.multiItemListPartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultiItemListPartitionDesc?: (ctx: MultiItemListPartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.stringList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStringList?: (ctx: StringListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.rangePartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRangePartitionDesc?: (ctx: RangePartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.singleRangePartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleRangePartition?: (ctx: SingleRangePartitionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.multiRangePartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultiRangePartition?: (ctx: MultiRangePartitionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.partitionRangeDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionRangeDesc?: (ctx: PartitionRangeDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.partitionKeyDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionKeyDesc?: (ctx: PartitionKeyDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.partitionValueList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionValueList?: (ctx: PartitionValueListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.keyPartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyPartition?: (ctx: KeyPartitionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.partitionValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionValue?: (ctx: PartitionValueContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.distributionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDistributionClause?: (ctx: DistributionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.distributionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDistributionDesc?: (ctx: DistributionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.refreshSchemeDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshSchemeDesc?: (ctx: RefreshSchemeDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.statusDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatusDesc?: (ctx: StatusDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProperties?: (ctx: PropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.extProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExtProperties?: (ctx: ExtPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.propertyList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPropertyList?: (ctx: PropertyListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.userPropertyList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserPropertyList?: (ctx: UserPropertyListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.property`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProperty?: (ctx: PropertyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.varType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVarType?: (ctx: VarTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.comment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComment?: (ctx: CommentContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.outfile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOutfile?: (ctx: OutfileContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.fileFormat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFileFormat?: (ctx: FileFormatContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.string`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitString?: (ctx: StringContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.binary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBinary?: (ctx: BinaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.comparisonOperator`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComparisonOperator?: (ctx: ComparisonOperatorContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.booleanValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanValue?: (ctx: BooleanValueContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.interval`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInterval?: (ctx: IntervalContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.unitIdentifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnitIdentifier?: (ctx: UnitIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.unitBoundary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnitBoundary?: (ctx: UnitBoundaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType?: (ctx: TypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.arrayType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArrayType?: (ctx: ArrayTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.mapType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapType?: (ctx: MapTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.subfieldDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubfieldDesc?: (ctx: SubfieldDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.subfieldDescs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubfieldDescs?: (ctx: SubfieldDescsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.structType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStructType?: (ctx: StructTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.typeParameter`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTypeParameter?: (ctx: TypeParameterContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.baseType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBaseType?: (ctx: BaseTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.decimalType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDecimalType?: (ctx: DecimalTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.qualifiedName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQualifiedName?: (ctx: QualifiedNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifier?: (ctx: IdentifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.identifierList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierList?: (ctx: IdentifierListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.identifierOrString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierOrString?: (ctx: IdentifierOrStringContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.identifierOrStringList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierOrStringList?: (ctx: IdentifierOrStringListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.identifierOrStringOrStar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierOrStringOrStar?: (ctx: IdentifierOrStringOrStarContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser?: (ctx: UserContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.assignment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAssignment?: (ctx: AssignmentContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.assignmentList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAssignmentList?: (ctx: AssignmentListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumber?: (ctx: NumberContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksParser.nonReserved`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNonReserved?: (ctx: NonReservedContext) => Result;
}
