import { ParseTreeListener } from "antlr4ts/tree/ParseTreeListener";
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
 * This interface defines a complete listener for a parse tree produced by
 * `StarRocksParser`.
 */
export interface StarRocksParserListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by the `tableAtom`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterTableAtom?: (ctx: TableAtomContext) => void;
    /**
     * Exit a parse tree produced by the `tableAtom`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitTableAtom?: (ctx: TableAtomContext) => void;
    /**
     * Enter a parse tree produced by the `inlineTable`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterInlineTable?: (ctx: InlineTableContext) => void;
    /**
     * Exit a parse tree produced by the `inlineTable`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitInlineTable?: (ctx: InlineTableContext) => void;
    /**
     * Enter a parse tree produced by the `subqueryWithAlias`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterSubqueryWithAlias?: (ctx: SubqueryWithAliasContext) => void;
    /**
     * Exit a parse tree produced by the `subqueryWithAlias`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitSubqueryWithAlias?: (ctx: SubqueryWithAliasContext) => void;
    /**
     * Enter a parse tree produced by the `tableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterTableFunction?: (ctx: TableFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `tableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitTableFunction?: (ctx: TableFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `normalizedTableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterNormalizedTableFunction?: (ctx: NormalizedTableFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `normalizedTableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitNormalizedTableFunction?: (ctx: NormalizedTableFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `fileTableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterFileTableFunction?: (ctx: FileTableFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `fileTableFunction`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitFileTableFunction?: (ctx: FileTableFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `parenthesizedRelation`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterParenthesizedRelation?: (ctx: ParenthesizedRelationContext) => void;
    /**
     * Exit a parse tree produced by the `parenthesizedRelation`
     * labeled alternative in `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitParenthesizedRelation?: (ctx: ParenthesizedRelationContext) => void;
    /**
     * Enter a parse tree produced by the `decimalValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    enterDecimalValue?: (ctx: DecimalValueContext) => void;
    /**
     * Exit a parse tree produced by the `decimalValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    exitDecimalValue?: (ctx: DecimalValueContext) => void;
    /**
     * Enter a parse tree produced by the `doubleValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    enterDoubleValue?: (ctx: DoubleValueContext) => void;
    /**
     * Exit a parse tree produced by the `doubleValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    exitDoubleValue?: (ctx: DoubleValueContext) => void;
    /**
     * Enter a parse tree produced by the `integerValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    enterIntegerValue?: (ctx: IntegerValueContext) => void;
    /**
     * Exit a parse tree produced by the `integerValue`
     * labeled alternative in `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    exitIntegerValue?: (ctx: IntegerValueContext) => void;
    /**
     * Enter a parse tree produced by the `extract`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterExtract?: (ctx: ExtractContext) => void;
    /**
     * Exit a parse tree produced by the `extract`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitExtract?: (ctx: ExtractContext) => void;
    /**
     * Enter a parse tree produced by the `groupingOperation`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterGroupingOperation?: (ctx: GroupingOperationContext) => void;
    /**
     * Exit a parse tree produced by the `groupingOperation`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitGroupingOperation?: (ctx: GroupingOperationContext) => void;
    /**
     * Enter a parse tree produced by the `informationFunction`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterInformationFunction?: (ctx: InformationFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `informationFunction`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitInformationFunction?: (ctx: InformationFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `specialDateTime`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterSpecialDateTime?: (ctx: SpecialDateTimeContext) => void;
    /**
     * Exit a parse tree produced by the `specialDateTime`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitSpecialDateTime?: (ctx: SpecialDateTimeContext) => void;
    /**
     * Enter a parse tree produced by the `specialFunction`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterSpecialFunction?: (ctx: SpecialFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `specialFunction`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitSpecialFunction?: (ctx: SpecialFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `aggregationFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterAggregationFunctionCall?: (ctx: AggregationFunctionCallContext) => void;
    /**
     * Exit a parse tree produced by the `aggregationFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitAggregationFunctionCall?: (ctx: AggregationFunctionCallContext) => void;
    /**
     * Enter a parse tree produced by the `windowFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterWindowFunctionCall?: (ctx: WindowFunctionCallContext) => void;
    /**
     * Exit a parse tree produced by the `windowFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitWindowFunctionCall?: (ctx: WindowFunctionCallContext) => void;
    /**
     * Enter a parse tree produced by the `simpleFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterSimpleFunctionCall?: (ctx: SimpleFunctionCallContext) => void;
    /**
     * Exit a parse tree produced by the `simpleFunctionCall`
     * labeled alternative in `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitSimpleFunctionCall?: (ctx: SimpleFunctionCallContext) => void;
    /**
     * Enter a parse tree produced by the `from`
     * labeled alternative in `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     */
    enterFrom?: (ctx: FromContext) => void;
    /**
     * Exit a parse tree produced by the `from`
     * labeled alternative in `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     */
    exitFrom?: (ctx: FromContext) => void;
    /**
     * Enter a parse tree produced by the `dual`
     * labeled alternative in `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     */
    enterDual?: (ctx: DualContext) => void;
    /**
     * Exit a parse tree produced by the `dual`
     * labeled alternative in `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     */
    exitDual?: (ctx: DualContext) => void;
    /**
     * Enter a parse tree produced by the `setNames`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetNames?: (ctx: SetNamesContext) => void;
    /**
     * Exit a parse tree produced by the `setNames`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetNames?: (ctx: SetNamesContext) => void;
    /**
     * Enter a parse tree produced by the `setPassword`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetPassword?: (ctx: SetPasswordContext) => void;
    /**
     * Exit a parse tree produced by the `setPassword`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetPassword?: (ctx: SetPasswordContext) => void;
    /**
     * Enter a parse tree produced by the `setUserVar`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetUserVar?: (ctx: SetUserVarContext) => void;
    /**
     * Exit a parse tree produced by the `setUserVar`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetUserVar?: (ctx: SetUserVarContext) => void;
    /**
     * Enter a parse tree produced by the `setSystemVar`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetSystemVar?: (ctx: SetSystemVarContext) => void;
    /**
     * Exit a parse tree produced by the `setSystemVar`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetSystemVar?: (ctx: SetSystemVarContext) => void;
    /**
     * Enter a parse tree produced by the `setTransaction`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetTransaction?: (ctx: SetTransactionContext) => void;
    /**
     * Exit a parse tree produced by the `setTransaction`
     * labeled alternative in `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetTransaction?: (ctx: SetTransactionContext) => void;
    /**
     * Enter a parse tree produced by the `keyPartitionList`
     * labeled alternative in `StarRocksParser.keyPartitions`.
     * @param ctx the parse tree
     */
    enterKeyPartitionList?: (ctx: KeyPartitionListContext) => void;
    /**
     * Exit a parse tree produced by the `keyPartitionList`
     * labeled alternative in `StarRocksParser.keyPartitions`.
     * @param ctx the parse tree
     */
    exitKeyPartitionList?: (ctx: KeyPartitionListContext) => void;
    /**
     * Enter a parse tree produced by the `inSubquery`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterInSubquery?: (ctx: InSubqueryContext) => void;
    /**
     * Exit a parse tree produced by the `inSubquery`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitInSubquery?: (ctx: InSubqueryContext) => void;
    /**
     * Enter a parse tree produced by the `inList`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterInList?: (ctx: InListContext) => void;
    /**
     * Exit a parse tree produced by the `inList`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitInList?: (ctx: InListContext) => void;
    /**
     * Enter a parse tree produced by the `between`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterBetween?: (ctx: BetweenContext) => void;
    /**
     * Exit a parse tree produced by the `between`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitBetween?: (ctx: BetweenContext) => void;
    /**
     * Enter a parse tree produced by the `like`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterLike?: (ctx: LikeContext) => void;
    /**
     * Exit a parse tree produced by the `like`
     * labeled alternative in `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitLike?: (ctx: LikeContext) => void;
    /**
     * Enter a parse tree produced by the `grantRoleToUser`
     * labeled alternative in `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    enterGrantRoleToUser?: (ctx: GrantRoleToUserContext) => void;
    /**
     * Exit a parse tree produced by the `grantRoleToUser`
     * labeled alternative in `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    exitGrantRoleToUser?: (ctx: GrantRoleToUserContext) => void;
    /**
     * Enter a parse tree produced by the `grantRoleToRole`
     * labeled alternative in `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    enterGrantRoleToRole?: (ctx: GrantRoleToRoleContext) => void;
    /**
     * Exit a parse tree produced by the `grantRoleToRole`
     * labeled alternative in `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    exitGrantRoleToRole?: (ctx: GrantRoleToRoleContext) => void;
    /**
     * Enter a parse tree produced by the `booleanExpressionDefault`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterBooleanExpressionDefault?: (ctx: BooleanExpressionDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `booleanExpressionDefault`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitBooleanExpressionDefault?: (ctx: BooleanExpressionDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `isNull`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterIsNull?: (ctx: IsNullContext) => void;
    /**
     * Exit a parse tree produced by the `isNull`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitIsNull?: (ctx: IsNullContext) => void;
    /**
     * Enter a parse tree produced by the `comparison`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterComparison?: (ctx: ComparisonContext) => void;
    /**
     * Exit a parse tree produced by the `comparison`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitComparison?: (ctx: ComparisonContext) => void;
    /**
     * Enter a parse tree produced by the `scalarSubquery`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterScalarSubquery?: (ctx: ScalarSubqueryContext) => void;
    /**
     * Exit a parse tree produced by the `scalarSubquery`
     * labeled alternative in `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitScalarSubquery?: (ctx: ScalarSubqueryContext) => void;
    /**
     * Enter a parse tree produced by the `nullLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterNullLiteral?: (ctx: NullLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `nullLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitNullLiteral?: (ctx: NullLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `booleanLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterBooleanLiteral?: (ctx: BooleanLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `booleanLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitBooleanLiteral?: (ctx: BooleanLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `numericLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterNumericLiteral?: (ctx: NumericLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `numericLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitNumericLiteral?: (ctx: NumericLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `dateLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterDateLiteral?: (ctx: DateLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `dateLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitDateLiteral?: (ctx: DateLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `stringLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterStringLiteral?: (ctx: StringLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `stringLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitStringLiteral?: (ctx: StringLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `intervalLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterIntervalLiteral?: (ctx: IntervalLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `intervalLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitIntervalLiteral?: (ctx: IntervalLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `unitBoundaryLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterUnitBoundaryLiteral?: (ctx: UnitBoundaryLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `unitBoundaryLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitUnitBoundaryLiteral?: (ctx: UnitBoundaryLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `binaryLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterBinaryLiteral?: (ctx: BinaryLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `binaryLiteral`
     * labeled alternative in `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitBinaryLiteral?: (ctx: BinaryLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnUser`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnUser?: (ctx: GrantOnUserContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnUser`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnUser?: (ctx: GrantOnUserContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnTableBrief`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnTableBrief?: (ctx: GrantOnTableBriefContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnTableBrief`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnTableBrief?: (ctx: GrantOnTableBriefContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnFunc`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnFunc?: (ctx: GrantOnFuncContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnFunc`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnFunc?: (ctx: GrantOnFuncContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnSystem`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnSystem?: (ctx: GrantOnSystemContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnSystem`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnSystem?: (ctx: GrantOnSystemContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnPrimaryObj`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnPrimaryObj?: (ctx: GrantOnPrimaryObjContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnPrimaryObj`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnPrimaryObj?: (ctx: GrantOnPrimaryObjContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnAll`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnAll?: (ctx: GrantOnAllContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnAll`
     * labeled alternative in `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnAll?: (ctx: GrantOnAllContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnUser`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnUser?: (ctx: RevokeOnUserContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnUser`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnUser?: (ctx: RevokeOnUserContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnTableBrief`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnTableBrief?: (ctx: RevokeOnTableBriefContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnTableBrief`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnTableBrief?: (ctx: RevokeOnTableBriefContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnFunc`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnFunc?: (ctx: RevokeOnFuncContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnFunc`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnFunc?: (ctx: RevokeOnFuncContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnSystem`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnSystem?: (ctx: RevokeOnSystemContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnSystem`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnSystem?: (ctx: RevokeOnSystemContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnPrimaryObj`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnPrimaryObj?: (ctx: RevokeOnPrimaryObjContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnPrimaryObj`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnPrimaryObj?: (ctx: RevokeOnPrimaryObjContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnAll`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnAll?: (ctx: RevokeOnAllContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnAll`
     * labeled alternative in `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnAll?: (ctx: RevokeOnAllContext) => void;
    /**
     * Enter a parse tree produced by the `rollup`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterRollup?: (ctx: RollupContext) => void;
    /**
     * Exit a parse tree produced by the `rollup`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitRollup?: (ctx: RollupContext) => void;
    /**
     * Enter a parse tree produced by the `cube`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterCube?: (ctx: CubeContext) => void;
    /**
     * Exit a parse tree produced by the `cube`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitCube?: (ctx: CubeContext) => void;
    /**
     * Enter a parse tree produced by the `multipleGroupingSets`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterMultipleGroupingSets?: (ctx: MultipleGroupingSetsContext) => void;
    /**
     * Exit a parse tree produced by the `multipleGroupingSets`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitMultipleGroupingSets?: (ctx: MultipleGroupingSetsContext) => void;
    /**
     * Enter a parse tree produced by the `singleGroupingSet`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterSingleGroupingSet?: (ctx: SingleGroupingSetContext) => void;
    /**
     * Exit a parse tree produced by the `singleGroupingSet`
     * labeled alternative in `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitSingleGroupingSet?: (ctx: SingleGroupingSetContext) => void;
    /**
     * Enter a parse tree produced by the `userVariableExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterUserVariableExpression?: (ctx: UserVariableExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `userVariableExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitUserVariableExpression?: (ctx: UserVariableExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `systemVariableExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSystemVariableExpression?: (ctx: SystemVariableExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `systemVariableExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSystemVariableExpression?: (ctx: SystemVariableExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `functionCallExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterFunctionCallExpression?: (ctx: FunctionCallExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `functionCallExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitFunctionCallExpression?: (ctx: FunctionCallExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `odbcFunctionCallExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterOdbcFunctionCallExpression?: (ctx: OdbcFunctionCallExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `odbcFunctionCallExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitOdbcFunctionCallExpression?: (ctx: OdbcFunctionCallExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `collate`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterCollate?: (ctx: CollateContext) => void;
    /**
     * Exit a parse tree produced by the `collate`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitCollate?: (ctx: CollateContext) => void;
    /**
     * Enter a parse tree produced by the `literal`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterLiteral?: (ctx: LiteralContext) => void;
    /**
     * Exit a parse tree produced by the `literal`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitLiteral?: (ctx: LiteralContext) => void;
    /**
     * Enter a parse tree produced by the `columnRef`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterColumnRef?: (ctx: ColumnRefContext) => void;
    /**
     * Exit a parse tree produced by the `columnRef`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitColumnRef?: (ctx: ColumnRefContext) => void;
    /**
     * Enter a parse tree produced by the `dereference`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterDereference?: (ctx: DereferenceContext) => void;
    /**
     * Exit a parse tree produced by the `dereference`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitDereference?: (ctx: DereferenceContext) => void;
    /**
     * Enter a parse tree produced by the `concat`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterConcat?: (ctx: ConcatContext) => void;
    /**
     * Exit a parse tree produced by the `concat`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitConcat?: (ctx: ConcatContext) => void;
    /**
     * Enter a parse tree produced by the `arithmeticUnary`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArithmeticUnary?: (ctx: ArithmeticUnaryContext) => void;
    /**
     * Exit a parse tree produced by the `arithmeticUnary`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArithmeticUnary?: (ctx: ArithmeticUnaryContext) => void;
    /**
     * Enter a parse tree produced by the `parenthesizedExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterParenthesizedExpression?: (ctx: ParenthesizedExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `parenthesizedExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitParenthesizedExpression?: (ctx: ParenthesizedExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `exists`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterExists?: (ctx: ExistsContext) => void;
    /**
     * Exit a parse tree produced by the `exists`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitExists?: (ctx: ExistsContext) => void;
    /**
     * Enter a parse tree produced by the `subqueryExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSubqueryExpression?: (ctx: SubqueryExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `subqueryExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSubqueryExpression?: (ctx: SubqueryExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `cast`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterCast?: (ctx: CastContext) => void;
    /**
     * Exit a parse tree produced by the `cast`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitCast?: (ctx: CastContext) => void;
    /**
     * Enter a parse tree produced by the `convert`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterConvert?: (ctx: ConvertContext) => void;
    /**
     * Exit a parse tree produced by the `convert`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitConvert?: (ctx: ConvertContext) => void;
    /**
     * Enter a parse tree produced by the `simpleCase`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSimpleCase?: (ctx: SimpleCaseContext) => void;
    /**
     * Exit a parse tree produced by the `simpleCase`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSimpleCase?: (ctx: SimpleCaseContext) => void;
    /**
     * Enter a parse tree produced by the `searchedCase`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSearchedCase?: (ctx: SearchedCaseContext) => void;
    /**
     * Exit a parse tree produced by the `searchedCase`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSearchedCase?: (ctx: SearchedCaseContext) => void;
    /**
     * Enter a parse tree produced by the `arrayConstructor`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArrayConstructor?: (ctx: ArrayConstructorContext) => void;
    /**
     * Exit a parse tree produced by the `arrayConstructor`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArrayConstructor?: (ctx: ArrayConstructorContext) => void;
    /**
     * Enter a parse tree produced by the `mapConstructor`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterMapConstructor?: (ctx: MapConstructorContext) => void;
    /**
     * Exit a parse tree produced by the `mapConstructor`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitMapConstructor?: (ctx: MapConstructorContext) => void;
    /**
     * Enter a parse tree produced by the `collectionSubscript`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterCollectionSubscript?: (ctx: CollectionSubscriptContext) => void;
    /**
     * Exit a parse tree produced by the `collectionSubscript`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitCollectionSubscript?: (ctx: CollectionSubscriptContext) => void;
    /**
     * Enter a parse tree produced by the `arraySlice`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArraySlice?: (ctx: ArraySliceContext) => void;
    /**
     * Exit a parse tree produced by the `arraySlice`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArraySlice?: (ctx: ArraySliceContext) => void;
    /**
     * Enter a parse tree produced by the `arrowExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArrowExpression?: (ctx: ArrowExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `arrowExpression`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArrowExpression?: (ctx: ArrowExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `lambdaFunctionExpr`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterLambdaFunctionExpr?: (ctx: LambdaFunctionExprContext) => void;
    /**
     * Exit a parse tree produced by the `lambdaFunctionExpr`
     * labeled alternative in `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitLambdaFunctionExpr?: (ctx: LambdaFunctionExprContext) => void;
    /**
     * Enter a parse tree produced by the `expressionDefault`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    enterExpressionDefault?: (ctx: ExpressionDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `expressionDefault`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    exitExpressionDefault?: (ctx: ExpressionDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `logicalNot`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    enterLogicalNot?: (ctx: LogicalNotContext) => void;
    /**
     * Exit a parse tree produced by the `logicalNot`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    exitLogicalNot?: (ctx: LogicalNotContext) => void;
    /**
     * Enter a parse tree produced by the `logicalBinary`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    enterLogicalBinary?: (ctx: LogicalBinaryContext) => void;
    /**
     * Exit a parse tree produced by the `logicalBinary`
     * labeled alternative in `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    exitLogicalBinary?: (ctx: LogicalBinaryContext) => void;
    /**
     * Enter a parse tree produced by the `showAllAuthentication`
     * labeled alternative in `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    enterShowAllAuthentication?: (ctx: ShowAllAuthenticationContext) => void;
    /**
     * Exit a parse tree produced by the `showAllAuthentication`
     * labeled alternative in `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    exitShowAllAuthentication?: (ctx: ShowAllAuthenticationContext) => void;
    /**
     * Enter a parse tree produced by the `showAuthenticationForUser`
     * labeled alternative in `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    enterShowAuthenticationForUser?: (ctx: ShowAuthenticationForUserContext) => void;
    /**
     * Exit a parse tree produced by the `showAuthenticationForUser`
     * labeled alternative in `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    exitShowAuthenticationForUser?: (ctx: ShowAuthenticationForUserContext) => void;
    /**
     * Enter a parse tree produced by the `unquotedIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    enterUnquotedIdentifier?: (ctx: UnquotedIdentifierContext) => void;
    /**
     * Exit a parse tree produced by the `unquotedIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    exitUnquotedIdentifier?: (ctx: UnquotedIdentifierContext) => void;
    /**
     * Enter a parse tree produced by the `digitIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    enterDigitIdentifier?: (ctx: DigitIdentifierContext) => void;
    /**
     * Exit a parse tree produced by the `digitIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    exitDigitIdentifier?: (ctx: DigitIdentifierContext) => void;
    /**
     * Enter a parse tree produced by the `backQuotedIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    enterBackQuotedIdentifier?: (ctx: BackQuotedIdentifierContext) => void;
    /**
     * Exit a parse tree produced by the `backQuotedIdentifier`
     * labeled alternative in `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    exitBackQuotedIdentifier?: (ctx: BackQuotedIdentifierContext) => void;
    /**
     * Enter a parse tree produced by the `valueExpressionDefault`
     * labeled alternative in `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     */
    enterValueExpressionDefault?: (ctx: ValueExpressionDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `valueExpressionDefault`
     * labeled alternative in `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     */
    exitValueExpressionDefault?: (ctx: ValueExpressionDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `arithmeticBinary`
     * labeled alternative in `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     */
    enterArithmeticBinary?: (ctx: ArithmeticBinaryContext) => void;
    /**
     * Exit a parse tree produced by the `arithmeticBinary`
     * labeled alternative in `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     */
    exitArithmeticBinary?: (ctx: ArithmeticBinaryContext) => void;
    /**
     * Enter a parse tree produced by the `queryPrimaryDefault`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterQueryPrimaryDefault?: (ctx: QueryPrimaryDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `queryPrimaryDefault`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitQueryPrimaryDefault?: (ctx: QueryPrimaryDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `queryWithParentheses`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterQueryWithParentheses?: (ctx: QueryWithParenthesesContext) => void;
    /**
     * Exit a parse tree produced by the `queryWithParentheses`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitQueryWithParentheses?: (ctx: QueryWithParenthesesContext) => void;
    /**
     * Enter a parse tree produced by the `setOperation`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterSetOperation?: (ctx: SetOperationContext) => void;
    /**
     * Exit a parse tree produced by the `setOperation`
     * labeled alternative in `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitSetOperation?: (ctx: SetOperationContext) => void;
    /**
     * Enter a parse tree produced by the `selectSingle`
     * labeled alternative in `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     */
    enterSelectSingle?: (ctx: SelectSingleContext) => void;
    /**
     * Exit a parse tree produced by the `selectSingle`
     * labeled alternative in `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     */
    exitSelectSingle?: (ctx: SelectSingleContext) => void;
    /**
     * Enter a parse tree produced by the `selectAll`
     * labeled alternative in `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     */
    enterSelectAll?: (ctx: SelectAllContext) => void;
    /**
     * Exit a parse tree produced by the `selectAll`
     * labeled alternative in `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     */
    exitSelectAll?: (ctx: SelectAllContext) => void;
    /**
     * Enter a parse tree produced by the `revokeRoleFromUser`
     * labeled alternative in `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    enterRevokeRoleFromUser?: (ctx: RevokeRoleFromUserContext) => void;
    /**
     * Exit a parse tree produced by the `revokeRoleFromUser`
     * labeled alternative in `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    exitRevokeRoleFromUser?: (ctx: RevokeRoleFromUserContext) => void;
    /**
     * Enter a parse tree produced by the `revokeRoleFromRole`
     * labeled alternative in `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    enterRevokeRoleFromRole?: (ctx: RevokeRoleFromRoleContext) => void;
    /**
     * Exit a parse tree produced by the `revokeRoleFromRole`
     * labeled alternative in `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    exitRevokeRoleFromRole?: (ctx: RevokeRoleFromRoleContext) => void;
    /**
     * Enter a parse tree produced by the `authWithoutPlugin`
     * labeled alternative in `StarRocksParser.authOption`.
     * @param ctx the parse tree
     */
    enterAuthWithoutPlugin?: (ctx: AuthWithoutPluginContext) => void;
    /**
     * Exit a parse tree produced by the `authWithoutPlugin`
     * labeled alternative in `StarRocksParser.authOption`.
     * @param ctx the parse tree
     */
    exitAuthWithoutPlugin?: (ctx: AuthWithoutPluginContext) => void;
    /**
     * Enter a parse tree produced by the `authWithPlugin`
     * labeled alternative in `StarRocksParser.authOption`.
     * @param ctx the parse tree
     */
    enterAuthWithPlugin?: (ctx: AuthWithPluginContext) => void;
    /**
     * Exit a parse tree produced by the `authWithPlugin`
     * labeled alternative in `StarRocksParser.authOption`.
     * @param ctx the parse tree
     */
    exitAuthWithPlugin?: (ctx: AuthWithPluginContext) => void;
    /**
     * Enter a parse tree produced by the `userWithoutHost`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    enterUserWithoutHost?: (ctx: UserWithoutHostContext) => void;
    /**
     * Exit a parse tree produced by the `userWithoutHost`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    exitUserWithoutHost?: (ctx: UserWithoutHostContext) => void;
    /**
     * Enter a parse tree produced by the `userWithHost`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    enterUserWithHost?: (ctx: UserWithHostContext) => void;
    /**
     * Exit a parse tree produced by the `userWithHost`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    exitUserWithHost?: (ctx: UserWithHostContext) => void;
    /**
     * Enter a parse tree produced by the `userWithHostAndBlanket`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    enterUserWithHostAndBlanket?: (ctx: UserWithHostAndBlanketContext) => void;
    /**
     * Exit a parse tree produced by the `userWithHostAndBlanket`
     * labeled alternative in `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    exitUserWithHostAndBlanket?: (ctx: UserWithHostAndBlanketContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.program`.
     * @param ctx the parse tree
     */
    enterProgram?: (ctx: ProgramContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.program`.
     * @param ctx the parse tree
     */
    exitProgram?: (ctx: ProgramContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.sqlStatements`.
     * @param ctx the parse tree
     */
    enterSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.sqlStatements`.
     * @param ctx the parse tree
     */
    exitSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.singleStatement`.
     * @param ctx the parse tree
     */
    enterSingleStatement?: (ctx: SingleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.singleStatement`.
     * @param ctx the parse tree
     */
    exitSingleStatement?: (ctx: SingleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.statement`.
     * @param ctx the parse tree
     */
    enterStatement?: (ctx: StatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.statement`.
     * @param ctx the parse tree
     */
    exitStatement?: (ctx: StatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.useDatabaseStatement`.
     * @param ctx the parse tree
     */
    enterUseDatabaseStatement?: (ctx: UseDatabaseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.useDatabaseStatement`.
     * @param ctx the parse tree
     */
    exitUseDatabaseStatement?: (ctx: UseDatabaseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.useCatalogStatement`.
     * @param ctx the parse tree
     */
    enterUseCatalogStatement?: (ctx: UseCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.useCatalogStatement`.
     * @param ctx the parse tree
     */
    exitUseCatalogStatement?: (ctx: UseCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setCatalogStatement`.
     * @param ctx the parse tree
     */
    enterSetCatalogStatement?: (ctx: SetCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setCatalogStatement`.
     * @param ctx the parse tree
     */
    exitSetCatalogStatement?: (ctx: SetCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showDatabasesStatement`.
     * @param ctx the parse tree
     */
    enterShowDatabasesStatement?: (ctx: ShowDatabasesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showDatabasesStatement`.
     * @param ctx the parse tree
     */
    exitShowDatabasesStatement?: (ctx: ShowDatabasesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterDbQuotaStatement`.
     * @param ctx the parse tree
     */
    enterAlterDbQuotaStatement?: (ctx: AlterDbQuotaStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterDbQuotaStatement`.
     * @param ctx the parse tree
     */
    exitAlterDbQuotaStatement?: (ctx: AlterDbQuotaStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createDbStatement`.
     * @param ctx the parse tree
     */
    enterCreateDbStatement?: (ctx: CreateDbStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createDbStatement`.
     * @param ctx the parse tree
     */
    exitCreateDbStatement?: (ctx: CreateDbStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropDbStatement`.
     * @param ctx the parse tree
     */
    enterDropDbStatement?: (ctx: DropDbStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropDbStatement`.
     * @param ctx the parse tree
     */
    exitDropDbStatement?: (ctx: DropDbStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCreateDbStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateDbStatement?: (ctx: ShowCreateDbStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCreateDbStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateDbStatement?: (ctx: ShowCreateDbStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterDatabaseRenameStatement`.
     * @param ctx the parse tree
     */
    enterAlterDatabaseRenameStatement?: (ctx: AlterDatabaseRenameStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterDatabaseRenameStatement`.
     * @param ctx the parse tree
     */
    exitAlterDatabaseRenameStatement?: (ctx: AlterDatabaseRenameStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.recoverDbStmt`.
     * @param ctx the parse tree
     */
    enterRecoverDbStmt?: (ctx: RecoverDbStmtContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.recoverDbStmt`.
     * @param ctx the parse tree
     */
    exitRecoverDbStmt?: (ctx: RecoverDbStmtContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showDataStmt`.
     * @param ctx the parse tree
     */
    enterShowDataStmt?: (ctx: ShowDataStmtContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showDataStmt`.
     * @param ctx the parse tree
     */
    exitShowDataStmt?: (ctx: ShowDataStmtContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createTableStatement`.
     * @param ctx the parse tree
     */
    enterCreateTableStatement?: (ctx: CreateTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createTableStatement`.
     * @param ctx the parse tree
     */
    exitCreateTableStatement?: (ctx: CreateTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.columnDesc`.
     * @param ctx the parse tree
     */
    enterColumnDesc?: (ctx: ColumnDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.columnDesc`.
     * @param ctx the parse tree
     */
    exitColumnDesc?: (ctx: ColumnDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.charsetName`.
     * @param ctx the parse tree
     */
    enterCharsetName?: (ctx: CharsetNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.charsetName`.
     * @param ctx the parse tree
     */
    exitCharsetName?: (ctx: CharsetNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.defaultDesc`.
     * @param ctx the parse tree
     */
    enterDefaultDesc?: (ctx: DefaultDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.defaultDesc`.
     * @param ctx the parse tree
     */
    exitDefaultDesc?: (ctx: DefaultDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.materializedColumnDesc`.
     * @param ctx the parse tree
     */
    enterMaterializedColumnDesc?: (ctx: MaterializedColumnDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.materializedColumnDesc`.
     * @param ctx the parse tree
     */
    exitMaterializedColumnDesc?: (ctx: MaterializedColumnDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.indexDesc`.
     * @param ctx the parse tree
     */
    enterIndexDesc?: (ctx: IndexDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.indexDesc`.
     * @param ctx the parse tree
     */
    exitIndexDesc?: (ctx: IndexDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.engineDesc`.
     * @param ctx the parse tree
     */
    enterEngineDesc?: (ctx: EngineDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.engineDesc`.
     * @param ctx the parse tree
     */
    exitEngineDesc?: (ctx: EngineDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.charsetDesc`.
     * @param ctx the parse tree
     */
    enterCharsetDesc?: (ctx: CharsetDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.charsetDesc`.
     * @param ctx the parse tree
     */
    exitCharsetDesc?: (ctx: CharsetDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.collateDesc`.
     * @param ctx the parse tree
     */
    enterCollateDesc?: (ctx: CollateDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.collateDesc`.
     * @param ctx the parse tree
     */
    exitCollateDesc?: (ctx: CollateDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.keyDesc`.
     * @param ctx the parse tree
     */
    enterKeyDesc?: (ctx: KeyDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.keyDesc`.
     * @param ctx the parse tree
     */
    exitKeyDesc?: (ctx: KeyDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.orderByDesc`.
     * @param ctx the parse tree
     */
    enterOrderByDesc?: (ctx: OrderByDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.orderByDesc`.
     * @param ctx the parse tree
     */
    exitOrderByDesc?: (ctx: OrderByDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.aggDesc`.
     * @param ctx the parse tree
     */
    enterAggDesc?: (ctx: AggDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.aggDesc`.
     * @param ctx the parse tree
     */
    exitAggDesc?: (ctx: AggDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.rollupDesc`.
     * @param ctx the parse tree
     */
    enterRollupDesc?: (ctx: RollupDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.rollupDesc`.
     * @param ctx the parse tree
     */
    exitRollupDesc?: (ctx: RollupDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.rollupItem`.
     * @param ctx the parse tree
     */
    enterRollupItem?: (ctx: RollupItemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.rollupItem`.
     * @param ctx the parse tree
     */
    exitRollupItem?: (ctx: RollupItemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dupKeys`.
     * @param ctx the parse tree
     */
    enterDupKeys?: (ctx: DupKeysContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dupKeys`.
     * @param ctx the parse tree
     */
    exitDupKeys?: (ctx: DupKeysContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.fromRollup`.
     * @param ctx the parse tree
     */
    enterFromRollup?: (ctx: FromRollupContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.fromRollup`.
     * @param ctx the parse tree
     */
    exitFromRollup?: (ctx: FromRollupContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.withMaskingPolicy`.
     * @param ctx the parse tree
     */
    enterWithMaskingPolicy?: (ctx: WithMaskingPolicyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.withMaskingPolicy`.
     * @param ctx the parse tree
     */
    exitWithMaskingPolicy?: (ctx: WithMaskingPolicyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.withRowAccessPolicy`.
     * @param ctx the parse tree
     */
    enterWithRowAccessPolicy?: (ctx: WithRowAccessPolicyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.withRowAccessPolicy`.
     * @param ctx the parse tree
     */
    exitWithRowAccessPolicy?: (ctx: WithRowAccessPolicyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createTemporaryTableStatement`.
     * @param ctx the parse tree
     */
    enterCreateTemporaryTableStatement?: (ctx: CreateTemporaryTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createTemporaryTableStatement`.
     * @param ctx the parse tree
     */
    exitCreateTemporaryTableStatement?: (ctx: CreateTemporaryTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createTableAsSelectStatement`.
     * @param ctx the parse tree
     */
    enterCreateTableAsSelectStatement?: (ctx: CreateTableAsSelectStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createTableAsSelectStatement`.
     * @param ctx the parse tree
     */
    exitCreateTableAsSelectStatement?: (ctx: CreateTableAsSelectStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropTableStatement`.
     * @param ctx the parse tree
     */
    enterDropTableStatement?: (ctx: DropTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropTableStatement`.
     * @param ctx the parse tree
     */
    exitDropTableStatement?: (ctx: DropTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterTableStatement`.
     * @param ctx the parse tree
     */
    enterAlterTableStatement?: (ctx: AlterTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterTableStatement`.
     * @param ctx the parse tree
     */
    exitAlterTableStatement?: (ctx: AlterTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createIndexStatement`.
     * @param ctx the parse tree
     */
    enterCreateIndexStatement?: (ctx: CreateIndexStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createIndexStatement`.
     * @param ctx the parse tree
     */
    exitCreateIndexStatement?: (ctx: CreateIndexStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropIndexStatement`.
     * @param ctx the parse tree
     */
    enterDropIndexStatement?: (ctx: DropIndexStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropIndexStatement`.
     * @param ctx the parse tree
     */
    exitDropIndexStatement?: (ctx: DropIndexStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.indexType`.
     * @param ctx the parse tree
     */
    enterIndexType?: (ctx: IndexTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.indexType`.
     * @param ctx the parse tree
     */
    exitIndexType?: (ctx: IndexTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showTableStatement`.
     * @param ctx the parse tree
     */
    enterShowTableStatement?: (ctx: ShowTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showTableStatement`.
     * @param ctx the parse tree
     */
    exitShowTableStatement?: (ctx: ShowTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCreateTableStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateTableStatement?: (ctx: ShowCreateTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCreateTableStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateTableStatement?: (ctx: ShowCreateTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showColumnStatement`.
     * @param ctx the parse tree
     */
    enterShowColumnStatement?: (ctx: ShowColumnStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showColumnStatement`.
     * @param ctx the parse tree
     */
    exitShowColumnStatement?: (ctx: ShowColumnStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showTableStatusStatement`.
     * @param ctx the parse tree
     */
    enterShowTableStatusStatement?: (ctx: ShowTableStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showTableStatusStatement`.
     * @param ctx the parse tree
     */
    exitShowTableStatusStatement?: (ctx: ShowTableStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.refreshTableStatement`.
     * @param ctx the parse tree
     */
    enterRefreshTableStatement?: (ctx: RefreshTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.refreshTableStatement`.
     * @param ctx the parse tree
     */
    exitRefreshTableStatement?: (ctx: RefreshTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showAlterStatement`.
     * @param ctx the parse tree
     */
    enterShowAlterStatement?: (ctx: ShowAlterStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showAlterStatement`.
     * @param ctx the parse tree
     */
    exitShowAlterStatement?: (ctx: ShowAlterStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.descTableStatement`.
     * @param ctx the parse tree
     */
    enterDescTableStatement?: (ctx: DescTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.descTableStatement`.
     * @param ctx the parse tree
     */
    exitDescTableStatement?: (ctx: DescTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createTableLikeStatement`.
     * @param ctx the parse tree
     */
    enterCreateTableLikeStatement?: (ctx: CreateTableLikeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createTableLikeStatement`.
     * @param ctx the parse tree
     */
    exitCreateTableLikeStatement?: (ctx: CreateTableLikeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showIndexStatement`.
     * @param ctx the parse tree
     */
    enterShowIndexStatement?: (ctx: ShowIndexStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showIndexStatement`.
     * @param ctx the parse tree
     */
    exitShowIndexStatement?: (ctx: ShowIndexStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.recoverTableStatement`.
     * @param ctx the parse tree
     */
    enterRecoverTableStatement?: (ctx: RecoverTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.recoverTableStatement`.
     * @param ctx the parse tree
     */
    exitRecoverTableStatement?: (ctx: RecoverTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.truncateTableStatement`.
     * @param ctx the parse tree
     */
    enterTruncateTableStatement?: (ctx: TruncateTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.truncateTableStatement`.
     * @param ctx the parse tree
     */
    exitTruncateTableStatement?: (ctx: TruncateTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelAlterTableStatement`.
     * @param ctx the parse tree
     */
    enterCancelAlterTableStatement?: (ctx: CancelAlterTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelAlterTableStatement`.
     * @param ctx the parse tree
     */
    exitCancelAlterTableStatement?: (ctx: CancelAlterTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showPartitionsStatement`.
     * @param ctx the parse tree
     */
    enterShowPartitionsStatement?: (ctx: ShowPartitionsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showPartitionsStatement`.
     * @param ctx the parse tree
     */
    exitShowPartitionsStatement?: (ctx: ShowPartitionsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.recoverPartitionStatement`.
     * @param ctx the parse tree
     */
    enterRecoverPartitionStatement?: (ctx: RecoverPartitionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.recoverPartitionStatement`.
     * @param ctx the parse tree
     */
    exitRecoverPartitionStatement?: (ctx: RecoverPartitionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createViewStatement`.
     * @param ctx the parse tree
     */
    enterCreateViewStatement?: (ctx: CreateViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createViewStatement`.
     * @param ctx the parse tree
     */
    exitCreateViewStatement?: (ctx: CreateViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterViewStatement`.
     * @param ctx the parse tree
     */
    enterAlterViewStatement?: (ctx: AlterViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterViewStatement`.
     * @param ctx the parse tree
     */
    exitAlterViewStatement?: (ctx: AlterViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropViewStatement`.
     * @param ctx the parse tree
     */
    enterDropViewStatement?: (ctx: DropViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropViewStatement`.
     * @param ctx the parse tree
     */
    exitDropViewStatement?: (ctx: DropViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.columnNameWithComment`.
     * @param ctx the parse tree
     */
    enterColumnNameWithComment?: (ctx: ColumnNameWithCommentContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.columnNameWithComment`.
     * @param ctx the parse tree
     */
    exitColumnNameWithComment?: (ctx: ColumnNameWithCommentContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.submitTaskStatement`.
     * @param ctx the parse tree
     */
    enterSubmitTaskStatement?: (ctx: SubmitTaskStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.submitTaskStatement`.
     * @param ctx the parse tree
     */
    exitSubmitTaskStatement?: (ctx: SubmitTaskStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.taskExecSql`.
     * @param ctx the parse tree
     */
    enterTaskExecSql?: (ctx: TaskExecSqlContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.taskExecSql`.
     * @param ctx the parse tree
     */
    exitTaskExecSql?: (ctx: TaskExecSqlContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropTaskStatement`.
     * @param ctx the parse tree
     */
    enterDropTaskStatement?: (ctx: DropTaskStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropTaskStatement`.
     * @param ctx the parse tree
     */
    exitDropTaskStatement?: (ctx: DropTaskStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterCreateMaterializedViewStatement?: (ctx: CreateMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitCreateMaterializedViewStatement?: (ctx: CreateMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.materializedViewDesc`.
     * @param ctx the parse tree
     */
    enterMaterializedViewDesc?: (ctx: MaterializedViewDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.materializedViewDesc`.
     * @param ctx the parse tree
     */
    exitMaterializedViewDesc?: (ctx: MaterializedViewDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showMaterializedViewsStatement`.
     * @param ctx the parse tree
     */
    enterShowMaterializedViewsStatement?: (ctx: ShowMaterializedViewsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showMaterializedViewsStatement`.
     * @param ctx the parse tree
     */
    exitShowMaterializedViewsStatement?: (ctx: ShowMaterializedViewsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterDropMaterializedViewStatement?: (ctx: DropMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitDropMaterializedViewStatement?: (ctx: DropMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterAlterMaterializedViewStatement?: (ctx: AlterMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitAlterMaterializedViewStatement?: (ctx: AlterMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.refreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterRefreshMaterializedViewStatement?: (ctx: RefreshMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.refreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitRefreshMaterializedViewStatement?: (ctx: RefreshMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelRefreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterCancelRefreshMaterializedViewStatement?: (ctx: CancelRefreshMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelRefreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitCancelRefreshMaterializedViewStatement?: (ctx: CancelRefreshMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminSetConfigStatement`.
     * @param ctx the parse tree
     */
    enterAdminSetConfigStatement?: (ctx: AdminSetConfigStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminSetConfigStatement`.
     * @param ctx the parse tree
     */
    exitAdminSetConfigStatement?: (ctx: AdminSetConfigStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminSetReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    enterAdminSetReplicaStatusStatement?: (ctx: AdminSetReplicaStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminSetReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    exitAdminSetReplicaStatusStatement?: (ctx: AdminSetReplicaStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminShowConfigStatement`.
     * @param ctx the parse tree
     */
    enterAdminShowConfigStatement?: (ctx: AdminShowConfigStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminShowConfigStatement`.
     * @param ctx the parse tree
     */
    exitAdminShowConfigStatement?: (ctx: AdminShowConfigStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminShowReplicaDistributionStatement`.
     * @param ctx the parse tree
     */
    enterAdminShowReplicaDistributionStatement?: (ctx: AdminShowReplicaDistributionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminShowReplicaDistributionStatement`.
     * @param ctx the parse tree
     */
    exitAdminShowReplicaDistributionStatement?: (ctx: AdminShowReplicaDistributionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminShowReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    enterAdminShowReplicaStatusStatement?: (ctx: AdminShowReplicaStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminShowReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    exitAdminShowReplicaStatusStatement?: (ctx: AdminShowReplicaStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminRepairTableStatement`.
     * @param ctx the parse tree
     */
    enterAdminRepairTableStatement?: (ctx: AdminRepairTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminRepairTableStatement`.
     * @param ctx the parse tree
     */
    exitAdminRepairTableStatement?: (ctx: AdminRepairTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminCancelRepairTableStatement`.
     * @param ctx the parse tree
     */
    enterAdminCancelRepairTableStatement?: (ctx: AdminCancelRepairTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminCancelRepairTableStatement`.
     * @param ctx the parse tree
     */
    exitAdminCancelRepairTableStatement?: (ctx: AdminCancelRepairTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.adminCheckTabletsStatement`.
     * @param ctx the parse tree
     */
    enterAdminCheckTabletsStatement?: (ctx: AdminCheckTabletsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.adminCheckTabletsStatement`.
     * @param ctx the parse tree
     */
    exitAdminCheckTabletsStatement?: (ctx: AdminCheckTabletsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.killStatement`.
     * @param ctx the parse tree
     */
    enterKillStatement?: (ctx: KillStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.killStatement`.
     * @param ctx the parse tree
     */
    exitKillStatement?: (ctx: KillStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.syncStatement`.
     * @param ctx the parse tree
     */
    enterSyncStatement?: (ctx: SyncStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.syncStatement`.
     * @param ctx the parse tree
     */
    exitSyncStatement?: (ctx: SyncStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterSystemStatement`.
     * @param ctx the parse tree
     */
    enterAlterSystemStatement?: (ctx: AlterSystemStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterSystemStatement`.
     * @param ctx the parse tree
     */
    exitAlterSystemStatement?: (ctx: AlterSystemStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelAlterSystemStatement`.
     * @param ctx the parse tree
     */
    enterCancelAlterSystemStatement?: (ctx: CancelAlterSystemStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelAlterSystemStatement`.
     * @param ctx the parse tree
     */
    exitCancelAlterSystemStatement?: (ctx: CancelAlterSystemStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showComputeNodesStatement`.
     * @param ctx the parse tree
     */
    enterShowComputeNodesStatement?: (ctx: ShowComputeNodesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showComputeNodesStatement`.
     * @param ctx the parse tree
     */
    exitShowComputeNodesStatement?: (ctx: ShowComputeNodesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    enterCreateExternalCatalogStatement?: (ctx: CreateExternalCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    exitCreateExternalCatalogStatement?: (ctx: CreateExternalCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCreateExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateExternalCatalogStatement?: (ctx: ShowCreateExternalCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCreateExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateExternalCatalogStatement?: (ctx: ShowCreateExternalCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    enterDropExternalCatalogStatement?: (ctx: DropExternalCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    exitDropExternalCatalogStatement?: (ctx: DropExternalCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCatalogsStatement`.
     * @param ctx the parse tree
     */
    enterShowCatalogsStatement?: (ctx: ShowCatalogsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCatalogsStatement`.
     * @param ctx the parse tree
     */
    exitShowCatalogsStatement?: (ctx: ShowCatalogsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterCreateWarehouseStatement?: (ctx: CreateWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitCreateWarehouseStatement?: (ctx: CreateWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showWarehousesStatement`.
     * @param ctx the parse tree
     */
    enterShowWarehousesStatement?: (ctx: ShowWarehousesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showWarehousesStatement`.
     * @param ctx the parse tree
     */
    exitShowWarehousesStatement?: (ctx: ShowWarehousesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterDropWarehouseStatement?: (ctx: DropWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitDropWarehouseStatement?: (ctx: DropWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterAlterWarehouseStatement?: (ctx: AlterWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitAlterWarehouseStatement?: (ctx: AlterWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showClustersStatement`.
     * @param ctx the parse tree
     */
    enterShowClustersStatement?: (ctx: ShowClustersStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showClustersStatement`.
     * @param ctx the parse tree
     */
    exitShowClustersStatement?: (ctx: ShowClustersStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.suspendWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterSuspendWarehouseStatement?: (ctx: SuspendWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.suspendWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitSuspendWarehouseStatement?: (ctx: SuspendWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.resumeWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterResumeWarehouseStatement?: (ctx: ResumeWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.resumeWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitResumeWarehouseStatement?: (ctx: ResumeWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterCreateStorageVolumeStatement?: (ctx: CreateStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitCreateStorageVolumeStatement?: (ctx: CreateStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.typeDesc`.
     * @param ctx the parse tree
     */
    enterTypeDesc?: (ctx: TypeDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.typeDesc`.
     * @param ctx the parse tree
     */
    exitTypeDesc?: (ctx: TypeDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.locationsDesc`.
     * @param ctx the parse tree
     */
    enterLocationsDesc?: (ctx: LocationsDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.locationsDesc`.
     * @param ctx the parse tree
     */
    exitLocationsDesc?: (ctx: LocationsDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showStorageVolumesStatement`.
     * @param ctx the parse tree
     */
    enterShowStorageVolumesStatement?: (ctx: ShowStorageVolumesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showStorageVolumesStatement`.
     * @param ctx the parse tree
     */
    exitShowStorageVolumesStatement?: (ctx: ShowStorageVolumesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterDropStorageVolumeStatement?: (ctx: DropStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitDropStorageVolumeStatement?: (ctx: DropStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterAlterStorageVolumeStatement?: (ctx: AlterStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitAlterStorageVolumeStatement?: (ctx: AlterStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterStorageVolumeClause`.
     * @param ctx the parse tree
     */
    enterAlterStorageVolumeClause?: (ctx: AlterStorageVolumeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterStorageVolumeClause`.
     * @param ctx the parse tree
     */
    exitAlterStorageVolumeClause?: (ctx: AlterStorageVolumeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyStorageVolumePropertiesClause`.
     * @param ctx the parse tree
     */
    enterModifyStorageVolumePropertiesClause?: (ctx: ModifyStorageVolumePropertiesClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyStorageVolumePropertiesClause`.
     * @param ctx the parse tree
     */
    exitModifyStorageVolumePropertiesClause?: (ctx: ModifyStorageVolumePropertiesClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyStorageVolumeCommentClause`.
     * @param ctx the parse tree
     */
    enterModifyStorageVolumeCommentClause?: (ctx: ModifyStorageVolumeCommentClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyStorageVolumeCommentClause`.
     * @param ctx the parse tree
     */
    exitModifyStorageVolumeCommentClause?: (ctx: ModifyStorageVolumeCommentClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.descStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterDescStorageVolumeStatement?: (ctx: DescStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.descStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitDescStorageVolumeStatement?: (ctx: DescStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setDefaultStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterSetDefaultStorageVolumeStatement?: (ctx: SetDefaultStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setDefaultStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitSetDefaultStorageVolumeStatement?: (ctx: SetDefaultStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterClause`.
     * @param ctx the parse tree
     */
    enterAlterClause?: (ctx: AlterClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterClause`.
     * @param ctx the parse tree
     */
    exitAlterClause?: (ctx: AlterClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.addFrontendClause`.
     * @param ctx the parse tree
     */
    enterAddFrontendClause?: (ctx: AddFrontendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.addFrontendClause`.
     * @param ctx the parse tree
     */
    exitAddFrontendClause?: (ctx: AddFrontendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropFrontendClause`.
     * @param ctx the parse tree
     */
    enterDropFrontendClause?: (ctx: DropFrontendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropFrontendClause`.
     * @param ctx the parse tree
     */
    exitDropFrontendClause?: (ctx: DropFrontendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyFrontendHostClause`.
     * @param ctx the parse tree
     */
    enterModifyFrontendHostClause?: (ctx: ModifyFrontendHostClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyFrontendHostClause`.
     * @param ctx the parse tree
     */
    exitModifyFrontendHostClause?: (ctx: ModifyFrontendHostClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.addBackendClause`.
     * @param ctx the parse tree
     */
    enterAddBackendClause?: (ctx: AddBackendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.addBackendClause`.
     * @param ctx the parse tree
     */
    exitAddBackendClause?: (ctx: AddBackendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropBackendClause`.
     * @param ctx the parse tree
     */
    enterDropBackendClause?: (ctx: DropBackendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropBackendClause`.
     * @param ctx the parse tree
     */
    exitDropBackendClause?: (ctx: DropBackendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.decommissionBackendClause`.
     * @param ctx the parse tree
     */
    enterDecommissionBackendClause?: (ctx: DecommissionBackendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.decommissionBackendClause`.
     * @param ctx the parse tree
     */
    exitDecommissionBackendClause?: (ctx: DecommissionBackendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyBackendHostClause`.
     * @param ctx the parse tree
     */
    enterModifyBackendHostClause?: (ctx: ModifyBackendHostClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyBackendHostClause`.
     * @param ctx the parse tree
     */
    exitModifyBackendHostClause?: (ctx: ModifyBackendHostClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.addComputeNodeClause`.
     * @param ctx the parse tree
     */
    enterAddComputeNodeClause?: (ctx: AddComputeNodeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.addComputeNodeClause`.
     * @param ctx the parse tree
     */
    exitAddComputeNodeClause?: (ctx: AddComputeNodeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropComputeNodeClause`.
     * @param ctx the parse tree
     */
    enterDropComputeNodeClause?: (ctx: DropComputeNodeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropComputeNodeClause`.
     * @param ctx the parse tree
     */
    exitDropComputeNodeClause?: (ctx: DropComputeNodeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyBrokerClause`.
     * @param ctx the parse tree
     */
    enterModifyBrokerClause?: (ctx: ModifyBrokerClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyBrokerClause`.
     * @param ctx the parse tree
     */
    exitModifyBrokerClause?: (ctx: ModifyBrokerClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterLoadErrorUrlClause`.
     * @param ctx the parse tree
     */
    enterAlterLoadErrorUrlClause?: (ctx: AlterLoadErrorUrlClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterLoadErrorUrlClause`.
     * @param ctx the parse tree
     */
    exitAlterLoadErrorUrlClause?: (ctx: AlterLoadErrorUrlClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createImageClause`.
     * @param ctx the parse tree
     */
    enterCreateImageClause?: (ctx: CreateImageClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createImageClause`.
     * @param ctx the parse tree
     */
    exitCreateImageClause?: (ctx: CreateImageClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cleanTabletSchedQClause`.
     * @param ctx the parse tree
     */
    enterCleanTabletSchedQClause?: (ctx: CleanTabletSchedQClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cleanTabletSchedQClause`.
     * @param ctx the parse tree
     */
    exitCleanTabletSchedQClause?: (ctx: CleanTabletSchedQClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createIndexClause`.
     * @param ctx the parse tree
     */
    enterCreateIndexClause?: (ctx: CreateIndexClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createIndexClause`.
     * @param ctx the parse tree
     */
    exitCreateIndexClause?: (ctx: CreateIndexClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropIndexClause`.
     * @param ctx the parse tree
     */
    enterDropIndexClause?: (ctx: DropIndexClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropIndexClause`.
     * @param ctx the parse tree
     */
    exitDropIndexClause?: (ctx: DropIndexClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.tableRenameClause`.
     * @param ctx the parse tree
     */
    enterTableRenameClause?: (ctx: TableRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.tableRenameClause`.
     * @param ctx the parse tree
     */
    exitTableRenameClause?: (ctx: TableRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.swapTableClause`.
     * @param ctx the parse tree
     */
    enterSwapTableClause?: (ctx: SwapTableClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.swapTableClause`.
     * @param ctx the parse tree
     */
    exitSwapTableClause?: (ctx: SwapTableClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyTablePropertiesClause`.
     * @param ctx the parse tree
     */
    enterModifyTablePropertiesClause?: (ctx: ModifyTablePropertiesClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyTablePropertiesClause`.
     * @param ctx the parse tree
     */
    exitModifyTablePropertiesClause?: (ctx: ModifyTablePropertiesClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyCommentClause`.
     * @param ctx the parse tree
     */
    enterModifyCommentClause?: (ctx: ModifyCommentClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyCommentClause`.
     * @param ctx the parse tree
     */
    exitModifyCommentClause?: (ctx: ModifyCommentClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.addColumnClause`.
     * @param ctx the parse tree
     */
    enterAddColumnClause?: (ctx: AddColumnClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.addColumnClause`.
     * @param ctx the parse tree
     */
    exitAddColumnClause?: (ctx: AddColumnClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.addColumnsClause`.
     * @param ctx the parse tree
     */
    enterAddColumnsClause?: (ctx: AddColumnsClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.addColumnsClause`.
     * @param ctx the parse tree
     */
    exitAddColumnsClause?: (ctx: AddColumnsClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropColumnClause`.
     * @param ctx the parse tree
     */
    enterDropColumnClause?: (ctx: DropColumnClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropColumnClause`.
     * @param ctx the parse tree
     */
    exitDropColumnClause?: (ctx: DropColumnClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyColumnClause`.
     * @param ctx the parse tree
     */
    enterModifyColumnClause?: (ctx: ModifyColumnClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyColumnClause`.
     * @param ctx the parse tree
     */
    exitModifyColumnClause?: (ctx: ModifyColumnClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.columnRenameClause`.
     * @param ctx the parse tree
     */
    enterColumnRenameClause?: (ctx: ColumnRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.columnRenameClause`.
     * @param ctx the parse tree
     */
    exitColumnRenameClause?: (ctx: ColumnRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.reorderColumnsClause`.
     * @param ctx the parse tree
     */
    enterReorderColumnsClause?: (ctx: ReorderColumnsClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.reorderColumnsClause`.
     * @param ctx the parse tree
     */
    exitReorderColumnsClause?: (ctx: ReorderColumnsClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.rollupRenameClause`.
     * @param ctx the parse tree
     */
    enterRollupRenameClause?: (ctx: RollupRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.rollupRenameClause`.
     * @param ctx the parse tree
     */
    exitRollupRenameClause?: (ctx: RollupRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.compactionClause`.
     * @param ctx the parse tree
     */
    enterCompactionClause?: (ctx: CompactionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.compactionClause`.
     * @param ctx the parse tree
     */
    exitCompactionClause?: (ctx: CompactionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.applyMaskingPolicyClause`.
     * @param ctx the parse tree
     */
    enterApplyMaskingPolicyClause?: (ctx: ApplyMaskingPolicyClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.applyMaskingPolicyClause`.
     * @param ctx the parse tree
     */
    exitApplyMaskingPolicyClause?: (ctx: ApplyMaskingPolicyClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.applyRowAccessPolicyClause`.
     * @param ctx the parse tree
     */
    enterApplyRowAccessPolicyClause?: (ctx: ApplyRowAccessPolicyClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.applyRowAccessPolicyClause`.
     * @param ctx the parse tree
     */
    exitApplyRowAccessPolicyClause?: (ctx: ApplyRowAccessPolicyClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.addPartitionClause`.
     * @param ctx the parse tree
     */
    enterAddPartitionClause?: (ctx: AddPartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.addPartitionClause`.
     * @param ctx the parse tree
     */
    exitAddPartitionClause?: (ctx: AddPartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropPartitionClause`.
     * @param ctx the parse tree
     */
    enterDropPartitionClause?: (ctx: DropPartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropPartitionClause`.
     * @param ctx the parse tree
     */
    exitDropPartitionClause?: (ctx: DropPartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.truncatePartitionClause`.
     * @param ctx the parse tree
     */
    enterTruncatePartitionClause?: (ctx: TruncatePartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.truncatePartitionClause`.
     * @param ctx the parse tree
     */
    exitTruncatePartitionClause?: (ctx: TruncatePartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.modifyPartitionClause`.
     * @param ctx the parse tree
     */
    enterModifyPartitionClause?: (ctx: ModifyPartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.modifyPartitionClause`.
     * @param ctx the parse tree
     */
    exitModifyPartitionClause?: (ctx: ModifyPartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.replacePartitionClause`.
     * @param ctx the parse tree
     */
    enterReplacePartitionClause?: (ctx: ReplacePartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.replacePartitionClause`.
     * @param ctx the parse tree
     */
    exitReplacePartitionClause?: (ctx: ReplacePartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.partitionRenameClause`.
     * @param ctx the parse tree
     */
    enterPartitionRenameClause?: (ctx: PartitionRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.partitionRenameClause`.
     * @param ctx the parse tree
     */
    exitPartitionRenameClause?: (ctx: PartitionRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.insertStatement`.
     * @param ctx the parse tree
     */
    enterInsertStatement?: (ctx: InsertStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.insertStatement`.
     * @param ctx the parse tree
     */
    exitInsertStatement?: (ctx: InsertStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.updateStatement`.
     * @param ctx the parse tree
     */
    enterUpdateStatement?: (ctx: UpdateStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.updateStatement`.
     * @param ctx the parse tree
     */
    exitUpdateStatement?: (ctx: UpdateStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.deleteStatement`.
     * @param ctx the parse tree
     */
    enterDeleteStatement?: (ctx: DeleteStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.deleteStatement`.
     * @param ctx the parse tree
     */
    exitDeleteStatement?: (ctx: DeleteStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterCreateRoutineLoadStatement?: (ctx: CreateRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitCreateRoutineLoadStatement?: (ctx: CreateRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterAlterRoutineLoadStatement?: (ctx: AlterRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitAlterRoutineLoadStatement?: (ctx: AlterRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dataSource`.
     * @param ctx the parse tree
     */
    enterDataSource?: (ctx: DataSourceContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dataSource`.
     * @param ctx the parse tree
     */
    exitDataSource?: (ctx: DataSourceContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.loadPropertiesExpr`.
     * @param ctx the parse tree
     */
    enterLoadPropertiesExpr?: (ctx: LoadPropertiesExprContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.loadPropertiesExpr`.
     * @param ctx the parse tree
     */
    exitLoadPropertiesExpr?: (ctx: LoadPropertiesExprContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.loadProperties`.
     * @param ctx the parse tree
     */
    enterLoadProperties?: (ctx: LoadPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.loadProperties`.
     * @param ctx the parse tree
     */
    exitLoadProperties?: (ctx: LoadPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.colSeparatorProperty`.
     * @param ctx the parse tree
     */
    enterColSeparatorProperty?: (ctx: ColSeparatorPropertyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.colSeparatorProperty`.
     * @param ctx the parse tree
     */
    exitColSeparatorProperty?: (ctx: ColSeparatorPropertyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.rowDelimiterProperty`.
     * @param ctx the parse tree
     */
    enterRowDelimiterProperty?: (ctx: RowDelimiterPropertyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.rowDelimiterProperty`.
     * @param ctx the parse tree
     */
    exitRowDelimiterProperty?: (ctx: RowDelimiterPropertyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.importColumns`.
     * @param ctx the parse tree
     */
    enterImportColumns?: (ctx: ImportColumnsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.importColumns`.
     * @param ctx the parse tree
     */
    exitImportColumns?: (ctx: ImportColumnsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.columnProperties`.
     * @param ctx the parse tree
     */
    enterColumnProperties?: (ctx: ColumnPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.columnProperties`.
     * @param ctx the parse tree
     */
    exitColumnProperties?: (ctx: ColumnPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.jobProperties`.
     * @param ctx the parse tree
     */
    enterJobProperties?: (ctx: JobPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.jobProperties`.
     * @param ctx the parse tree
     */
    exitJobProperties?: (ctx: JobPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dataSourceProperties`.
     * @param ctx the parse tree
     */
    enterDataSourceProperties?: (ctx: DataSourcePropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dataSourceProperties`.
     * @param ctx the parse tree
     */
    exitDataSourceProperties?: (ctx: DataSourcePropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.stopRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterStopRoutineLoadStatement?: (ctx: StopRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.stopRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitStopRoutineLoadStatement?: (ctx: StopRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.resumeRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterResumeRoutineLoadStatement?: (ctx: ResumeRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.resumeRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitResumeRoutineLoadStatement?: (ctx: ResumeRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.pauseRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterPauseRoutineLoadStatement?: (ctx: PauseRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.pauseRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitPauseRoutineLoadStatement?: (ctx: PauseRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterShowRoutineLoadStatement?: (ctx: ShowRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitShowRoutineLoadStatement?: (ctx: ShowRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showRoutineLoadTaskStatement`.
     * @param ctx the parse tree
     */
    enterShowRoutineLoadTaskStatement?: (ctx: ShowRoutineLoadTaskStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showRoutineLoadTaskStatement`.
     * @param ctx the parse tree
     */
    exitShowRoutineLoadTaskStatement?: (ctx: ShowRoutineLoadTaskStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showStreamLoadStatement`.
     * @param ctx the parse tree
     */
    enterShowStreamLoadStatement?: (ctx: ShowStreamLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showStreamLoadStatement`.
     * @param ctx the parse tree
     */
    exitShowStreamLoadStatement?: (ctx: ShowStreamLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.analyzeStatement`.
     * @param ctx the parse tree
     */
    enterAnalyzeStatement?: (ctx: AnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.analyzeStatement`.
     * @param ctx the parse tree
     */
    exitAnalyzeStatement?: (ctx: AnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropStatsStatement`.
     * @param ctx the parse tree
     */
    enterDropStatsStatement?: (ctx: DropStatsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropStatsStatement`.
     * @param ctx the parse tree
     */
    exitDropStatsStatement?: (ctx: DropStatsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.analyzeHistogramStatement`.
     * @param ctx the parse tree
     */
    enterAnalyzeHistogramStatement?: (ctx: AnalyzeHistogramStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.analyzeHistogramStatement`.
     * @param ctx the parse tree
     */
    exitAnalyzeHistogramStatement?: (ctx: AnalyzeHistogramStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropHistogramStatement`.
     * @param ctx the parse tree
     */
    enterDropHistogramStatement?: (ctx: DropHistogramStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropHistogramStatement`.
     * @param ctx the parse tree
     */
    exitDropHistogramStatement?: (ctx: DropHistogramStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createAnalyzeStatement`.
     * @param ctx the parse tree
     */
    enterCreateAnalyzeStatement?: (ctx: CreateAnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createAnalyzeStatement`.
     * @param ctx the parse tree
     */
    exitCreateAnalyzeStatement?: (ctx: CreateAnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropAnalyzeJobStatement`.
     * @param ctx the parse tree
     */
    enterDropAnalyzeJobStatement?: (ctx: DropAnalyzeJobStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropAnalyzeJobStatement`.
     * @param ctx the parse tree
     */
    exitDropAnalyzeJobStatement?: (ctx: DropAnalyzeJobStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showAnalyzeStatement`.
     * @param ctx the parse tree
     */
    enterShowAnalyzeStatement?: (ctx: ShowAnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showAnalyzeStatement`.
     * @param ctx the parse tree
     */
    exitShowAnalyzeStatement?: (ctx: ShowAnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showStatsMetaStatement`.
     * @param ctx the parse tree
     */
    enterShowStatsMetaStatement?: (ctx: ShowStatsMetaStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showStatsMetaStatement`.
     * @param ctx the parse tree
     */
    exitShowStatsMetaStatement?: (ctx: ShowStatsMetaStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showHistogramMetaStatement`.
     * @param ctx the parse tree
     */
    enterShowHistogramMetaStatement?: (ctx: ShowHistogramMetaStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showHistogramMetaStatement`.
     * @param ctx the parse tree
     */
    exitShowHistogramMetaStatement?: (ctx: ShowHistogramMetaStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.killAnalyzeStatement`.
     * @param ctx the parse tree
     */
    enterKillAnalyzeStatement?: (ctx: KillAnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.killAnalyzeStatement`.
     * @param ctx the parse tree
     */
    exitKillAnalyzeStatement?: (ctx: KillAnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.analyzeProfileStatement`.
     * @param ctx the parse tree
     */
    enterAnalyzeProfileStatement?: (ctx: AnalyzeProfileStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.analyzeProfileStatement`.
     * @param ctx the parse tree
     */
    exitAnalyzeProfileStatement?: (ctx: AnalyzeProfileStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterCreateResourceGroupStatement?: (ctx: CreateResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitCreateResourceGroupStatement?: (ctx: CreateResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterDropResourceGroupStatement?: (ctx: DropResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitDropResourceGroupStatement?: (ctx: DropResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterAlterResourceGroupStatement?: (ctx: AlterResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitAlterResourceGroupStatement?: (ctx: AlterResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterShowResourceGroupStatement?: (ctx: ShowResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitShowResourceGroupStatement?: (ctx: ShowResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createResourceStatement`.
     * @param ctx the parse tree
     */
    enterCreateResourceStatement?: (ctx: CreateResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createResourceStatement`.
     * @param ctx the parse tree
     */
    exitCreateResourceStatement?: (ctx: CreateResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterResourceStatement`.
     * @param ctx the parse tree
     */
    enterAlterResourceStatement?: (ctx: AlterResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterResourceStatement`.
     * @param ctx the parse tree
     */
    exitAlterResourceStatement?: (ctx: AlterResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropResourceStatement`.
     * @param ctx the parse tree
     */
    enterDropResourceStatement?: (ctx: DropResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropResourceStatement`.
     * @param ctx the parse tree
     */
    exitDropResourceStatement?: (ctx: DropResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showResourceStatement`.
     * @param ctx the parse tree
     */
    enterShowResourceStatement?: (ctx: ShowResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showResourceStatement`.
     * @param ctx the parse tree
     */
    exitShowResourceStatement?: (ctx: ShowResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.classifier`.
     * @param ctx the parse tree
     */
    enterClassifier?: (ctx: ClassifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.classifier`.
     * @param ctx the parse tree
     */
    exitClassifier?: (ctx: ClassifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showFunctionsStatement`.
     * @param ctx the parse tree
     */
    enterShowFunctionsStatement?: (ctx: ShowFunctionsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showFunctionsStatement`.
     * @param ctx the parse tree
     */
    exitShowFunctionsStatement?: (ctx: ShowFunctionsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropFunctionStatement`.
     * @param ctx the parse tree
     */
    enterDropFunctionStatement?: (ctx: DropFunctionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropFunctionStatement`.
     * @param ctx the parse tree
     */
    exitDropFunctionStatement?: (ctx: DropFunctionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createFunctionStatement`.
     * @param ctx the parse tree
     */
    enterCreateFunctionStatement?: (ctx: CreateFunctionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createFunctionStatement`.
     * @param ctx the parse tree
     */
    exitCreateFunctionStatement?: (ctx: CreateFunctionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.typeList`.
     * @param ctx the parse tree
     */
    enterTypeList?: (ctx: TypeListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.typeList`.
     * @param ctx the parse tree
     */
    exitTypeList?: (ctx: TypeListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.loadStatement`.
     * @param ctx the parse tree
     */
    enterLoadStatement?: (ctx: LoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.loadStatement`.
     * @param ctx the parse tree
     */
    exitLoadStatement?: (ctx: LoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.labelName`.
     * @param ctx the parse tree
     */
    enterLabelName?: (ctx: LabelNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.labelName`.
     * @param ctx the parse tree
     */
    exitLabelName?: (ctx: LabelNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dataDescList`.
     * @param ctx the parse tree
     */
    enterDataDescList?: (ctx: DataDescListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dataDescList`.
     * @param ctx the parse tree
     */
    exitDataDescList?: (ctx: DataDescListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dataDesc`.
     * @param ctx the parse tree
     */
    enterDataDesc?: (ctx: DataDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dataDesc`.
     * @param ctx the parse tree
     */
    exitDataDesc?: (ctx: DataDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.formatProps`.
     * @param ctx the parse tree
     */
    enterFormatProps?: (ctx: FormatPropsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.formatProps`.
     * @param ctx the parse tree
     */
    exitFormatProps?: (ctx: FormatPropsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.brokerDesc`.
     * @param ctx the parse tree
     */
    enterBrokerDesc?: (ctx: BrokerDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.brokerDesc`.
     * @param ctx the parse tree
     */
    exitBrokerDesc?: (ctx: BrokerDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.resourceDesc`.
     * @param ctx the parse tree
     */
    enterResourceDesc?: (ctx: ResourceDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.resourceDesc`.
     * @param ctx the parse tree
     */
    exitResourceDesc?: (ctx: ResourceDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showLoadStatement`.
     * @param ctx the parse tree
     */
    enterShowLoadStatement?: (ctx: ShowLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showLoadStatement`.
     * @param ctx the parse tree
     */
    exitShowLoadStatement?: (ctx: ShowLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showLoadWarningsStatement`.
     * @param ctx the parse tree
     */
    enterShowLoadWarningsStatement?: (ctx: ShowLoadWarningsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showLoadWarningsStatement`.
     * @param ctx the parse tree
     */
    exitShowLoadWarningsStatement?: (ctx: ShowLoadWarningsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelLoadStatement`.
     * @param ctx the parse tree
     */
    enterCancelLoadStatement?: (ctx: CancelLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelLoadStatement`.
     * @param ctx the parse tree
     */
    exitCancelLoadStatement?: (ctx: CancelLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterLoadStatement`.
     * @param ctx the parse tree
     */
    enterAlterLoadStatement?: (ctx: AlterLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterLoadStatement`.
     * @param ctx the parse tree
     */
    exitAlterLoadStatement?: (ctx: AlterLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelCompactionStatement`.
     * @param ctx the parse tree
     */
    enterCancelCompactionStatement?: (ctx: CancelCompactionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelCompactionStatement`.
     * @param ctx the parse tree
     */
    exitCancelCompactionStatement?: (ctx: CancelCompactionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showAuthorStatement`.
     * @param ctx the parse tree
     */
    enterShowAuthorStatement?: (ctx: ShowAuthorStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showAuthorStatement`.
     * @param ctx the parse tree
     */
    exitShowAuthorStatement?: (ctx: ShowAuthorStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showBackendsStatement`.
     * @param ctx the parse tree
     */
    enterShowBackendsStatement?: (ctx: ShowBackendsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showBackendsStatement`.
     * @param ctx the parse tree
     */
    exitShowBackendsStatement?: (ctx: ShowBackendsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showBrokerStatement`.
     * @param ctx the parse tree
     */
    enterShowBrokerStatement?: (ctx: ShowBrokerStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showBrokerStatement`.
     * @param ctx the parse tree
     */
    exitShowBrokerStatement?: (ctx: ShowBrokerStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCharsetStatement`.
     * @param ctx the parse tree
     */
    enterShowCharsetStatement?: (ctx: ShowCharsetStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCharsetStatement`.
     * @param ctx the parse tree
     */
    exitShowCharsetStatement?: (ctx: ShowCharsetStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCollationStatement`.
     * @param ctx the parse tree
     */
    enterShowCollationStatement?: (ctx: ShowCollationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCollationStatement`.
     * @param ctx the parse tree
     */
    exitShowCollationStatement?: (ctx: ShowCollationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showDeleteStatement`.
     * @param ctx the parse tree
     */
    enterShowDeleteStatement?: (ctx: ShowDeleteStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showDeleteStatement`.
     * @param ctx the parse tree
     */
    exitShowDeleteStatement?: (ctx: ShowDeleteStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showDynamicPartitionStatement`.
     * @param ctx the parse tree
     */
    enterShowDynamicPartitionStatement?: (ctx: ShowDynamicPartitionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showDynamicPartitionStatement`.
     * @param ctx the parse tree
     */
    exitShowDynamicPartitionStatement?: (ctx: ShowDynamicPartitionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showEventsStatement`.
     * @param ctx the parse tree
     */
    enterShowEventsStatement?: (ctx: ShowEventsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showEventsStatement`.
     * @param ctx the parse tree
     */
    exitShowEventsStatement?: (ctx: ShowEventsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showEnginesStatement`.
     * @param ctx the parse tree
     */
    enterShowEnginesStatement?: (ctx: ShowEnginesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showEnginesStatement`.
     * @param ctx the parse tree
     */
    exitShowEnginesStatement?: (ctx: ShowEnginesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showFrontendsStatement`.
     * @param ctx the parse tree
     */
    enterShowFrontendsStatement?: (ctx: ShowFrontendsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showFrontendsStatement`.
     * @param ctx the parse tree
     */
    exitShowFrontendsStatement?: (ctx: ShowFrontendsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showPluginsStatement`.
     * @param ctx the parse tree
     */
    enterShowPluginsStatement?: (ctx: ShowPluginsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showPluginsStatement`.
     * @param ctx the parse tree
     */
    exitShowPluginsStatement?: (ctx: ShowPluginsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showRepositoriesStatement`.
     * @param ctx the parse tree
     */
    enterShowRepositoriesStatement?: (ctx: ShowRepositoriesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showRepositoriesStatement`.
     * @param ctx the parse tree
     */
    exitShowRepositoriesStatement?: (ctx: ShowRepositoriesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showOpenTableStatement`.
     * @param ctx the parse tree
     */
    enterShowOpenTableStatement?: (ctx: ShowOpenTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showOpenTableStatement`.
     * @param ctx the parse tree
     */
    exitShowOpenTableStatement?: (ctx: ShowOpenTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showPrivilegesStatement`.
     * @param ctx the parse tree
     */
    enterShowPrivilegesStatement?: (ctx: ShowPrivilegesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showPrivilegesStatement`.
     * @param ctx the parse tree
     */
    exitShowPrivilegesStatement?: (ctx: ShowPrivilegesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showProcedureStatement`.
     * @param ctx the parse tree
     */
    enterShowProcedureStatement?: (ctx: ShowProcedureStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showProcedureStatement`.
     * @param ctx the parse tree
     */
    exitShowProcedureStatement?: (ctx: ShowProcedureStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showProcStatement`.
     * @param ctx the parse tree
     */
    enterShowProcStatement?: (ctx: ShowProcStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showProcStatement`.
     * @param ctx the parse tree
     */
    exitShowProcStatement?: (ctx: ShowProcStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showProcesslistStatement`.
     * @param ctx the parse tree
     */
    enterShowProcesslistStatement?: (ctx: ShowProcesslistStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showProcesslistStatement`.
     * @param ctx the parse tree
     */
    exitShowProcesslistStatement?: (ctx: ShowProcesslistStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showProfilelistStatement`.
     * @param ctx the parse tree
     */
    enterShowProfilelistStatement?: (ctx: ShowProfilelistStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showProfilelistStatement`.
     * @param ctx the parse tree
     */
    exitShowProfilelistStatement?: (ctx: ShowProfilelistStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showStatusStatement`.
     * @param ctx the parse tree
     */
    enterShowStatusStatement?: (ctx: ShowStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showStatusStatement`.
     * @param ctx the parse tree
     */
    exitShowStatusStatement?: (ctx: ShowStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showTabletStatement`.
     * @param ctx the parse tree
     */
    enterShowTabletStatement?: (ctx: ShowTabletStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showTabletStatement`.
     * @param ctx the parse tree
     */
    exitShowTabletStatement?: (ctx: ShowTabletStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showTransactionStatement`.
     * @param ctx the parse tree
     */
    enterShowTransactionStatement?: (ctx: ShowTransactionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showTransactionStatement`.
     * @param ctx the parse tree
     */
    exitShowTransactionStatement?: (ctx: ShowTransactionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showTriggersStatement`.
     * @param ctx the parse tree
     */
    enterShowTriggersStatement?: (ctx: ShowTriggersStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showTriggersStatement`.
     * @param ctx the parse tree
     */
    exitShowTriggersStatement?: (ctx: ShowTriggersStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showUserPropertyStatement`.
     * @param ctx the parse tree
     */
    enterShowUserPropertyStatement?: (ctx: ShowUserPropertyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showUserPropertyStatement`.
     * @param ctx the parse tree
     */
    exitShowUserPropertyStatement?: (ctx: ShowUserPropertyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showVariablesStatement`.
     * @param ctx the parse tree
     */
    enterShowVariablesStatement?: (ctx: ShowVariablesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showVariablesStatement`.
     * @param ctx the parse tree
     */
    exitShowVariablesStatement?: (ctx: ShowVariablesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showWarningStatement`.
     * @param ctx the parse tree
     */
    enterShowWarningStatement?: (ctx: ShowWarningStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showWarningStatement`.
     * @param ctx the parse tree
     */
    exitShowWarningStatement?: (ctx: ShowWarningStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.helpStatement`.
     * @param ctx the parse tree
     */
    enterHelpStatement?: (ctx: HelpStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.helpStatement`.
     * @param ctx the parse tree
     */
    exitHelpStatement?: (ctx: HelpStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createUserStatement`.
     * @param ctx the parse tree
     */
    enterCreateUserStatement?: (ctx: CreateUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createUserStatement`.
     * @param ctx the parse tree
     */
    exitCreateUserStatement?: (ctx: CreateUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropUserStatement`.
     * @param ctx the parse tree
     */
    enterDropUserStatement?: (ctx: DropUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropUserStatement`.
     * @param ctx the parse tree
     */
    exitDropUserStatement?: (ctx: DropUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterUserStatement`.
     * @param ctx the parse tree
     */
    enterAlterUserStatement?: (ctx: AlterUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterUserStatement`.
     * @param ctx the parse tree
     */
    exitAlterUserStatement?: (ctx: AlterUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showUserStatement`.
     * @param ctx the parse tree
     */
    enterShowUserStatement?: (ctx: ShowUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showUserStatement`.
     * @param ctx the parse tree
     */
    exitShowUserStatement?: (ctx: ShowUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    enterShowAuthenticationStatement?: (ctx: ShowAuthenticationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    exitShowAuthenticationStatement?: (ctx: ShowAuthenticationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.executeAsStatement`.
     * @param ctx the parse tree
     */
    enterExecuteAsStatement?: (ctx: ExecuteAsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.executeAsStatement`.
     * @param ctx the parse tree
     */
    exitExecuteAsStatement?: (ctx: ExecuteAsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createRoleStatement`.
     * @param ctx the parse tree
     */
    enterCreateRoleStatement?: (ctx: CreateRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createRoleStatement`.
     * @param ctx the parse tree
     */
    exitCreateRoleStatement?: (ctx: CreateRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterRoleStatement`.
     * @param ctx the parse tree
     */
    enterAlterRoleStatement?: (ctx: AlterRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterRoleStatement`.
     * @param ctx the parse tree
     */
    exitAlterRoleStatement?: (ctx: AlterRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropRoleStatement`.
     * @param ctx the parse tree
     */
    enterDropRoleStatement?: (ctx: DropRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropRoleStatement`.
     * @param ctx the parse tree
     */
    exitDropRoleStatement?: (ctx: DropRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showRolesStatement`.
     * @param ctx the parse tree
     */
    enterShowRolesStatement?: (ctx: ShowRolesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showRolesStatement`.
     * @param ctx the parse tree
     */
    exitShowRolesStatement?: (ctx: ShowRolesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    enterGrantRoleStatement?: (ctx: GrantRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    exitGrantRoleStatement?: (ctx: GrantRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    enterRevokeRoleStatement?: (ctx: RevokeRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    exitRevokeRoleStatement?: (ctx: RevokeRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setRoleStatement`.
     * @param ctx the parse tree
     */
    enterSetRoleStatement?: (ctx: SetRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setRoleStatement`.
     * @param ctx the parse tree
     */
    exitSetRoleStatement?: (ctx: SetRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setDefaultRoleStatement`.
     * @param ctx the parse tree
     */
    enterSetDefaultRoleStatement?: (ctx: SetDefaultRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setDefaultRoleStatement`.
     * @param ctx the parse tree
     */
    exitSetDefaultRoleStatement?: (ctx: SetDefaultRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.grantRevokeClause`.
     * @param ctx the parse tree
     */
    enterGrantRevokeClause?: (ctx: GrantRevokeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.grantRevokeClause`.
     * @param ctx the parse tree
     */
    exitGrantRevokeClause?: (ctx: GrantRevokeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantPrivilegeStatement?: (ctx: GrantPrivilegeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantPrivilegeStatement?: (ctx: GrantPrivilegeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokePrivilegeStatement?: (ctx: RevokePrivilegeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokePrivilegeStatement?: (ctx: RevokePrivilegeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showGrantsStatement`.
     * @param ctx the parse tree
     */
    enterShowGrantsStatement?: (ctx: ShowGrantsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showGrantsStatement`.
     * @param ctx the parse tree
     */
    exitShowGrantsStatement?: (ctx: ShowGrantsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterCreateSecurityIntegrationStatement?: (ctx: CreateSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitCreateSecurityIntegrationStatement?: (ctx: CreateSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterAlterSecurityIntegrationStatement?: (ctx: AlterSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitAlterSecurityIntegrationStatement?: (ctx: AlterSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterDropSecurityIntegrationStatement?: (ctx: DropSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitDropSecurityIntegrationStatement?: (ctx: DropSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterShowSecurityIntegrationStatement?: (ctx: ShowSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitShowSecurityIntegrationStatement?: (ctx: ShowSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCreateSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateSecurityIntegrationStatement?: (ctx: ShowCreateSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCreateSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateSecurityIntegrationStatement?: (ctx: ShowCreateSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterCreateRoleMappingStatement?: (ctx: CreateRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitCreateRoleMappingStatement?: (ctx: CreateRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterAlterRoleMappingStatement?: (ctx: AlterRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitAlterRoleMappingStatement?: (ctx: AlterRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterDropRoleMappingStatement?: (ctx: DropRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitDropRoleMappingStatement?: (ctx: DropRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterShowRoleMappingStatement?: (ctx: ShowRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitShowRoleMappingStatement?: (ctx: ShowRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.refreshRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterRefreshRoleMappingStatement?: (ctx: RefreshRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.refreshRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitRefreshRoleMappingStatement?: (ctx: RefreshRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.authOption`.
     * @param ctx the parse tree
     */
    enterAuthOption?: (ctx: AuthOptionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.authOption`.
     * @param ctx the parse tree
     */
    exitAuthOption?: (ctx: AuthOptionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.privObjectName`.
     * @param ctx the parse tree
     */
    enterPrivObjectName?: (ctx: PrivObjectNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.privObjectName`.
     * @param ctx the parse tree
     */
    exitPrivObjectName?: (ctx: PrivObjectNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.privObjectNameList`.
     * @param ctx the parse tree
     */
    enterPrivObjectNameList?: (ctx: PrivObjectNameListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.privObjectNameList`.
     * @param ctx the parse tree
     */
    exitPrivObjectNameList?: (ctx: PrivObjectNameListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.privFunctionObjectNameList`.
     * @param ctx the parse tree
     */
    enterPrivFunctionObjectNameList?: (ctx: PrivFunctionObjectNameListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.privFunctionObjectNameList`.
     * @param ctx the parse tree
     */
    exitPrivFunctionObjectNameList?: (ctx: PrivFunctionObjectNameListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.privilegeTypeList`.
     * @param ctx the parse tree
     */
    enterPrivilegeTypeList?: (ctx: PrivilegeTypeListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.privilegeTypeList`.
     * @param ctx the parse tree
     */
    exitPrivilegeTypeList?: (ctx: PrivilegeTypeListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.privilegeType`.
     * @param ctx the parse tree
     */
    enterPrivilegeType?: (ctx: PrivilegeTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.privilegeType`.
     * @param ctx the parse tree
     */
    exitPrivilegeType?: (ctx: PrivilegeTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.privObjectType`.
     * @param ctx the parse tree
     */
    enterPrivObjectType?: (ctx: PrivObjectTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.privObjectType`.
     * @param ctx the parse tree
     */
    exitPrivObjectType?: (ctx: PrivObjectTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.privObjectTypePlural`.
     * @param ctx the parse tree
     */
    enterPrivObjectTypePlural?: (ctx: PrivObjectTypePluralContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.privObjectTypePlural`.
     * @param ctx the parse tree
     */
    exitPrivObjectTypePlural?: (ctx: PrivObjectTypePluralContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterCreateMaskingPolicyStatement?: (ctx: CreateMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitCreateMaskingPolicyStatement?: (ctx: CreateMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterDropMaskingPolicyStatement?: (ctx: DropMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitDropMaskingPolicyStatement?: (ctx: DropMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterAlterMaskingPolicyStatement?: (ctx: AlterMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitAlterMaskingPolicyStatement?: (ctx: AlterMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowMaskingPolicyStatement?: (ctx: ShowMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowMaskingPolicyStatement?: (ctx: ShowMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCreateMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateMaskingPolicyStatement?: (ctx: ShowCreateMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCreateMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateMaskingPolicyStatement?: (ctx: ShowCreateMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterCreateRowAccessPolicyStatement?: (ctx: CreateRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitCreateRowAccessPolicyStatement?: (ctx: CreateRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterDropRowAccessPolicyStatement?: (ctx: DropRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitDropRowAccessPolicyStatement?: (ctx: DropRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.alterRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterAlterRowAccessPolicyStatement?: (ctx: AlterRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.alterRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitAlterRowAccessPolicyStatement?: (ctx: AlterRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowRowAccessPolicyStatement?: (ctx: ShowRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowRowAccessPolicyStatement?: (ctx: ShowRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showCreateRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateRowAccessPolicyStatement?: (ctx: ShowCreateRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showCreateRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateRowAccessPolicyStatement?: (ctx: ShowCreateRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.policySignature`.
     * @param ctx the parse tree
     */
    enterPolicySignature?: (ctx: PolicySignatureContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.policySignature`.
     * @param ctx the parse tree
     */
    exitPolicySignature?: (ctx: PolicySignatureContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.backupStatement`.
     * @param ctx the parse tree
     */
    enterBackupStatement?: (ctx: BackupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.backupStatement`.
     * @param ctx the parse tree
     */
    exitBackupStatement?: (ctx: BackupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelBackupStatement`.
     * @param ctx the parse tree
     */
    enterCancelBackupStatement?: (ctx: CancelBackupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelBackupStatement`.
     * @param ctx the parse tree
     */
    exitCancelBackupStatement?: (ctx: CancelBackupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showBackupStatement`.
     * @param ctx the parse tree
     */
    enterShowBackupStatement?: (ctx: ShowBackupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showBackupStatement`.
     * @param ctx the parse tree
     */
    exitShowBackupStatement?: (ctx: ShowBackupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.restoreStatement`.
     * @param ctx the parse tree
     */
    enterRestoreStatement?: (ctx: RestoreStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.restoreStatement`.
     * @param ctx the parse tree
     */
    exitRestoreStatement?: (ctx: RestoreStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelRestoreStatement`.
     * @param ctx the parse tree
     */
    enterCancelRestoreStatement?: (ctx: CancelRestoreStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelRestoreStatement`.
     * @param ctx the parse tree
     */
    exitCancelRestoreStatement?: (ctx: CancelRestoreStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showRestoreStatement`.
     * @param ctx the parse tree
     */
    enterShowRestoreStatement?: (ctx: ShowRestoreStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showRestoreStatement`.
     * @param ctx the parse tree
     */
    exitShowRestoreStatement?: (ctx: ShowRestoreStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showSnapshotStatement`.
     * @param ctx the parse tree
     */
    enterShowSnapshotStatement?: (ctx: ShowSnapshotStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showSnapshotStatement`.
     * @param ctx the parse tree
     */
    exitShowSnapshotStatement?: (ctx: ShowSnapshotStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createRepositoryStatement`.
     * @param ctx the parse tree
     */
    enterCreateRepositoryStatement?: (ctx: CreateRepositoryStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createRepositoryStatement`.
     * @param ctx the parse tree
     */
    exitCreateRepositoryStatement?: (ctx: CreateRepositoryStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropRepositoryStatement`.
     * @param ctx the parse tree
     */
    enterDropRepositoryStatement?: (ctx: DropRepositoryStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropRepositoryStatement`.
     * @param ctx the parse tree
     */
    exitDropRepositoryStatement?: (ctx: DropRepositoryStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.addSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    enterAddSqlBlackListStatement?: (ctx: AddSqlBlackListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.addSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    exitAddSqlBlackListStatement?: (ctx: AddSqlBlackListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.delSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    enterDelSqlBlackListStatement?: (ctx: DelSqlBlackListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.delSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    exitDelSqlBlackListStatement?: (ctx: DelSqlBlackListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    enterShowSqlBlackListStatement?: (ctx: ShowSqlBlackListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    exitShowSqlBlackListStatement?: (ctx: ShowSqlBlackListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showWhiteListStatement`.
     * @param ctx the parse tree
     */
    enterShowWhiteListStatement?: (ctx: ShowWhiteListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showWhiteListStatement`.
     * @param ctx the parse tree
     */
    exitShowWhiteListStatement?: (ctx: ShowWhiteListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.exportStatement`.
     * @param ctx the parse tree
     */
    enterExportStatement?: (ctx: ExportStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.exportStatement`.
     * @param ctx the parse tree
     */
    exitExportStatement?: (ctx: ExportStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.cancelExportStatement`.
     * @param ctx the parse tree
     */
    enterCancelExportStatement?: (ctx: CancelExportStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.cancelExportStatement`.
     * @param ctx the parse tree
     */
    exitCancelExportStatement?: (ctx: CancelExportStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showExportStatement`.
     * @param ctx the parse tree
     */
    enterShowExportStatement?: (ctx: ShowExportStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showExportStatement`.
     * @param ctx the parse tree
     */
    exitShowExportStatement?: (ctx: ShowExportStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.installPluginStatement`.
     * @param ctx the parse tree
     */
    enterInstallPluginStatement?: (ctx: InstallPluginStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.installPluginStatement`.
     * @param ctx the parse tree
     */
    exitInstallPluginStatement?: (ctx: InstallPluginStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.uninstallPluginStatement`.
     * @param ctx the parse tree
     */
    enterUninstallPluginStatement?: (ctx: UninstallPluginStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.uninstallPluginStatement`.
     * @param ctx the parse tree
     */
    exitUninstallPluginStatement?: (ctx: UninstallPluginStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.createFileStatement`.
     * @param ctx the parse tree
     */
    enterCreateFileStatement?: (ctx: CreateFileStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.createFileStatement`.
     * @param ctx the parse tree
     */
    exitCreateFileStatement?: (ctx: CreateFileStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.dropFileStatement`.
     * @param ctx the parse tree
     */
    enterDropFileStatement?: (ctx: DropFileStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.dropFileStatement`.
     * @param ctx the parse tree
     */
    exitDropFileStatement?: (ctx: DropFileStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.showSmallFilesStatement`.
     * @param ctx the parse tree
     */
    enterShowSmallFilesStatement?: (ctx: ShowSmallFilesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.showSmallFilesStatement`.
     * @param ctx the parse tree
     */
    exitShowSmallFilesStatement?: (ctx: ShowSmallFilesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setStatement`.
     * @param ctx the parse tree
     */
    enterSetStatement?: (ctx: SetStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setStatement`.
     * @param ctx the parse tree
     */
    exitSetStatement?: (ctx: SetStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetVar?: (ctx: SetVarContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetVar?: (ctx: SetVarContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.transaction_characteristics`.
     * @param ctx the parse tree
     */
    enterTransaction_characteristics?: (ctx: Transaction_characteristicsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.transaction_characteristics`.
     * @param ctx the parse tree
     */
    exitTransaction_characteristics?: (ctx: Transaction_characteristicsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.transaction_access_mode`.
     * @param ctx the parse tree
     */
    enterTransaction_access_mode?: (ctx: Transaction_access_modeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.transaction_access_mode`.
     * @param ctx the parse tree
     */
    exitTransaction_access_mode?: (ctx: Transaction_access_modeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.isolation_level`.
     * @param ctx the parse tree
     */
    enterIsolation_level?: (ctx: Isolation_levelContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.isolation_level`.
     * @param ctx the parse tree
     */
    exitIsolation_level?: (ctx: Isolation_levelContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.isolation_types`.
     * @param ctx the parse tree
     */
    enterIsolation_types?: (ctx: Isolation_typesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.isolation_types`.
     * @param ctx the parse tree
     */
    exitIsolation_types?: (ctx: Isolation_typesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setExprOrDefault`.
     * @param ctx the parse tree
     */
    enterSetExprOrDefault?: (ctx: SetExprOrDefaultContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setExprOrDefault`.
     * @param ctx the parse tree
     */
    exitSetExprOrDefault?: (ctx: SetExprOrDefaultContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setUserPropertyStatement`.
     * @param ctx the parse tree
     */
    enterSetUserPropertyStatement?: (ctx: SetUserPropertyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setUserPropertyStatement`.
     * @param ctx the parse tree
     */
    exitSetUserPropertyStatement?: (ctx: SetUserPropertyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.roleList`.
     * @param ctx the parse tree
     */
    enterRoleList?: (ctx: RoleListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.roleList`.
     * @param ctx the parse tree
     */
    exitRoleList?: (ctx: RoleListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.executeScriptStatement`.
     * @param ctx the parse tree
     */
    enterExecuteScriptStatement?: (ctx: ExecuteScriptStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.executeScriptStatement`.
     * @param ctx the parse tree
     */
    exitExecuteScriptStatement?: (ctx: ExecuteScriptStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.unsupportedStatement`.
     * @param ctx the parse tree
     */
    enterUnsupportedStatement?: (ctx: UnsupportedStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.unsupportedStatement`.
     * @param ctx the parse tree
     */
    exitUnsupportedStatement?: (ctx: UnsupportedStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.lock_item`.
     * @param ctx the parse tree
     */
    enterLock_item?: (ctx: Lock_itemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.lock_item`.
     * @param ctx the parse tree
     */
    exitLock_item?: (ctx: Lock_itemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.lock_type`.
     * @param ctx the parse tree
     */
    enterLock_type?: (ctx: Lock_typeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.lock_type`.
     * @param ctx the parse tree
     */
    exitLock_type?: (ctx: Lock_typeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.queryStatement`.
     * @param ctx the parse tree
     */
    enterQueryStatement?: (ctx: QueryStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.queryStatement`.
     * @param ctx the parse tree
     */
    exitQueryStatement?: (ctx: QueryStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.queryRelation`.
     * @param ctx the parse tree
     */
    enterQueryRelation?: (ctx: QueryRelationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.queryRelation`.
     * @param ctx the parse tree
     */
    exitQueryRelation?: (ctx: QueryRelationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.withClause`.
     * @param ctx the parse tree
     */
    enterWithClause?: (ctx: WithClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.withClause`.
     * @param ctx the parse tree
     */
    exitWithClause?: (ctx: WithClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.queryNoWith`.
     * @param ctx the parse tree
     */
    enterQueryNoWith?: (ctx: QueryNoWithContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.queryNoWith`.
     * @param ctx the parse tree
     */
    exitQueryNoWith?: (ctx: QueryNoWithContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.temporalClause`.
     * @param ctx the parse tree
     */
    enterTemporalClause?: (ctx: TemporalClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.temporalClause`.
     * @param ctx the parse tree
     */
    exitTemporalClause?: (ctx: TemporalClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterQueryPrimary?: (ctx: QueryPrimaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitQueryPrimary?: (ctx: QueryPrimaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.subquery`.
     * @param ctx the parse tree
     */
    enterSubquery?: (ctx: SubqueryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.subquery`.
     * @param ctx the parse tree
     */
    exitSubquery?: (ctx: SubqueryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.rowConstructor`.
     * @param ctx the parse tree
     */
    enterRowConstructor?: (ctx: RowConstructorContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.rowConstructor`.
     * @param ctx the parse tree
     */
    exitRowConstructor?: (ctx: RowConstructorContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.sortItem`.
     * @param ctx the parse tree
     */
    enterSortItem?: (ctx: SortItemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.sortItem`.
     * @param ctx the parse tree
     */
    exitSortItem?: (ctx: SortItemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.limitElement`.
     * @param ctx the parse tree
     */
    enterLimitElement?: (ctx: LimitElementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.limitElement`.
     * @param ctx the parse tree
     */
    exitLimitElement?: (ctx: LimitElementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.querySpecification`.
     * @param ctx the parse tree
     */
    enterQuerySpecification?: (ctx: QuerySpecificationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.querySpecification`.
     * @param ctx the parse tree
     */
    exitQuerySpecification?: (ctx: QuerySpecificationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     */
    enterFromClause?: (ctx: FromClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.fromClause`.
     * @param ctx the parse tree
     */
    exitFromClause?: (ctx: FromClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterGroupingElement?: (ctx: GroupingElementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitGroupingElement?: (ctx: GroupingElementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.groupingSet`.
     * @param ctx the parse tree
     */
    enterGroupingSet?: (ctx: GroupingSetContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.groupingSet`.
     * @param ctx the parse tree
     */
    exitGroupingSet?: (ctx: GroupingSetContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.commonTableExpression`.
     * @param ctx the parse tree
     */
    enterCommonTableExpression?: (ctx: CommonTableExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.commonTableExpression`.
     * @param ctx the parse tree
     */
    exitCommonTableExpression?: (ctx: CommonTableExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setQuantifier`.
     * @param ctx the parse tree
     */
    enterSetQuantifier?: (ctx: SetQuantifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setQuantifier`.
     * @param ctx the parse tree
     */
    exitSetQuantifier?: (ctx: SetQuantifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     */
    enterSelectItem?: (ctx: SelectItemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.selectItem`.
     * @param ctx the parse tree
     */
    exitSelectItem?: (ctx: SelectItemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.relations`.
     * @param ctx the parse tree
     */
    enterRelations?: (ctx: RelationsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.relations`.
     * @param ctx the parse tree
     */
    exitRelations?: (ctx: RelationsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.relation`.
     * @param ctx the parse tree
     */
    enterRelation?: (ctx: RelationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.relation`.
     * @param ctx the parse tree
     */
    exitRelation?: (ctx: RelationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterRelationPrimary?: (ctx: RelationPrimaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitRelationPrimary?: (ctx: RelationPrimaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.joinRelation`.
     * @param ctx the parse tree
     */
    enterJoinRelation?: (ctx: JoinRelationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.joinRelation`.
     * @param ctx the parse tree
     */
    exitJoinRelation?: (ctx: JoinRelationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.crossOrInnerJoinType`.
     * @param ctx the parse tree
     */
    enterCrossOrInnerJoinType?: (ctx: CrossOrInnerJoinTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.crossOrInnerJoinType`.
     * @param ctx the parse tree
     */
    exitCrossOrInnerJoinType?: (ctx: CrossOrInnerJoinTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.outerAndSemiJoinType`.
     * @param ctx the parse tree
     */
    enterOuterAndSemiJoinType?: (ctx: OuterAndSemiJoinTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.outerAndSemiJoinType`.
     * @param ctx the parse tree
     */
    exitOuterAndSemiJoinType?: (ctx: OuterAndSemiJoinTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.bracketHint`.
     * @param ctx the parse tree
     */
    enterBracketHint?: (ctx: BracketHintContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.bracketHint`.
     * @param ctx the parse tree
     */
    exitBracketHint?: (ctx: BracketHintContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.setVarHint`.
     * @param ctx the parse tree
     */
    enterSetVarHint?: (ctx: SetVarHintContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.setVarHint`.
     * @param ctx the parse tree
     */
    exitSetVarHint?: (ctx: SetVarHintContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.hintMap`.
     * @param ctx the parse tree
     */
    enterHintMap?: (ctx: HintMapContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.hintMap`.
     * @param ctx the parse tree
     */
    exitHintMap?: (ctx: HintMapContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.joinCriteria`.
     * @param ctx the parse tree
     */
    enterJoinCriteria?: (ctx: JoinCriteriaContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.joinCriteria`.
     * @param ctx the parse tree
     */
    exitJoinCriteria?: (ctx: JoinCriteriaContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.columnAliases`.
     * @param ctx the parse tree
     */
    enterColumnAliases?: (ctx: ColumnAliasesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.columnAliases`.
     * @param ctx the parse tree
     */
    exitColumnAliases?: (ctx: ColumnAliasesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.partitionNames`.
     * @param ctx the parse tree
     */
    enterPartitionNames?: (ctx: PartitionNamesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.partitionNames`.
     * @param ctx the parse tree
     */
    exitPartitionNames?: (ctx: PartitionNamesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.keyPartitions`.
     * @param ctx the parse tree
     */
    enterKeyPartitions?: (ctx: KeyPartitionsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.keyPartitions`.
     * @param ctx the parse tree
     */
    exitKeyPartitions?: (ctx: KeyPartitionsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.tabletList`.
     * @param ctx the parse tree
     */
    enterTabletList?: (ctx: TabletListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.tabletList`.
     * @param ctx the parse tree
     */
    exitTabletList?: (ctx: TabletListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.expressionsWithDefault`.
     * @param ctx the parse tree
     */
    enterExpressionsWithDefault?: (ctx: ExpressionsWithDefaultContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.expressionsWithDefault`.
     * @param ctx the parse tree
     */
    exitExpressionsWithDefault?: (ctx: ExpressionsWithDefaultContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.expressionOrDefault`.
     * @param ctx the parse tree
     */
    enterExpressionOrDefault?: (ctx: ExpressionOrDefaultContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.expressionOrDefault`.
     * @param ctx the parse tree
     */
    exitExpressionOrDefault?: (ctx: ExpressionOrDefaultContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.mapExpressionList`.
     * @param ctx the parse tree
     */
    enterMapExpressionList?: (ctx: MapExpressionListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.mapExpressionList`.
     * @param ctx the parse tree
     */
    exitMapExpressionList?: (ctx: MapExpressionListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.mapExpression`.
     * @param ctx the parse tree
     */
    enterMapExpression?: (ctx: MapExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.mapExpression`.
     * @param ctx the parse tree
     */
    exitMapExpression?: (ctx: MapExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.expressionSingleton`.
     * @param ctx the parse tree
     */
    enterExpressionSingleton?: (ctx: ExpressionSingletonContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.expressionSingleton`.
     * @param ctx the parse tree
     */
    exitExpressionSingleton?: (ctx: ExpressionSingletonContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    enterExpression?: (ctx: ExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.expression`.
     * @param ctx the parse tree
     */
    exitExpression?: (ctx: ExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.expressionList`.
     * @param ctx the parse tree
     */
    enterExpressionList?: (ctx: ExpressionListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.expressionList`.
     * @param ctx the parse tree
     */
    exitExpressionList?: (ctx: ExpressionListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterBooleanExpression?: (ctx: BooleanExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitBooleanExpression?: (ctx: BooleanExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.predicate`.
     * @param ctx the parse tree
     */
    enterPredicate?: (ctx: PredicateContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.predicate`.
     * @param ctx the parse tree
     */
    exitPredicate?: (ctx: PredicateContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.tupleInSubquery`.
     * @param ctx the parse tree
     */
    enterTupleInSubquery?: (ctx: TupleInSubqueryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.tupleInSubquery`.
     * @param ctx the parse tree
     */
    exitTupleInSubquery?: (ctx: TupleInSubqueryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterPredicateOperations?: (ctx: PredicateOperationsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitPredicateOperations?: (ctx: PredicateOperationsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     */
    enterValueExpression?: (ctx: ValueExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.valueExpression`.
     * @param ctx the parse tree
     */
    exitValueExpression?: (ctx: ValueExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterPrimaryExpression?: (ctx: PrimaryExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitPrimaryExpression?: (ctx: PrimaryExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterLiteralExpression?: (ctx: LiteralExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitLiteralExpression?: (ctx: LiteralExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    enterFunctionCall?: (ctx: FunctionCallContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.functionCall`.
     * @param ctx the parse tree
     */
    exitFunctionCall?: (ctx: FunctionCallContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.aggregationFunction`.
     * @param ctx the parse tree
     */
    enterAggregationFunction?: (ctx: AggregationFunctionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.aggregationFunction`.
     * @param ctx the parse tree
     */
    exitAggregationFunction?: (ctx: AggregationFunctionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.userVariable`.
     * @param ctx the parse tree
     */
    enterUserVariable?: (ctx: UserVariableContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.userVariable`.
     * @param ctx the parse tree
     */
    exitUserVariable?: (ctx: UserVariableContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.systemVariable`.
     * @param ctx the parse tree
     */
    enterSystemVariable?: (ctx: SystemVariableContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.systemVariable`.
     * @param ctx the parse tree
     */
    exitSystemVariable?: (ctx: SystemVariableContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.columnReference`.
     * @param ctx the parse tree
     */
    enterColumnReference?: (ctx: ColumnReferenceContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.columnReference`.
     * @param ctx the parse tree
     */
    exitColumnReference?: (ctx: ColumnReferenceContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.informationFunctionExpression`.
     * @param ctx the parse tree
     */
    enterInformationFunctionExpression?: (ctx: InformationFunctionExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.informationFunctionExpression`.
     * @param ctx the parse tree
     */
    exitInformationFunctionExpression?: (ctx: InformationFunctionExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.specialDateTimeExpression`.
     * @param ctx the parse tree
     */
    enterSpecialDateTimeExpression?: (ctx: SpecialDateTimeExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.specialDateTimeExpression`.
     * @param ctx the parse tree
     */
    exitSpecialDateTimeExpression?: (ctx: SpecialDateTimeExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.specialFunctionExpression`.
     * @param ctx the parse tree
     */
    enterSpecialFunctionExpression?: (ctx: SpecialFunctionExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.specialFunctionExpression`.
     * @param ctx the parse tree
     */
    exitSpecialFunctionExpression?: (ctx: SpecialFunctionExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.windowFunction`.
     * @param ctx the parse tree
     */
    enterWindowFunction?: (ctx: WindowFunctionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.windowFunction`.
     * @param ctx the parse tree
     */
    exitWindowFunction?: (ctx: WindowFunctionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.whenClause`.
     * @param ctx the parse tree
     */
    enterWhenClause?: (ctx: WhenClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.whenClause`.
     * @param ctx the parse tree
     */
    exitWhenClause?: (ctx: WhenClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.over`.
     * @param ctx the parse tree
     */
    enterOver?: (ctx: OverContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.over`.
     * @param ctx the parse tree
     */
    exitOver?: (ctx: OverContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.ignoreNulls`.
     * @param ctx the parse tree
     */
    enterIgnoreNulls?: (ctx: IgnoreNullsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.ignoreNulls`.
     * @param ctx the parse tree
     */
    exitIgnoreNulls?: (ctx: IgnoreNullsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.tableDesc`.
     * @param ctx the parse tree
     */
    enterTableDesc?: (ctx: TableDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.tableDesc`.
     * @param ctx the parse tree
     */
    exitTableDesc?: (ctx: TableDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.restoreTableDesc`.
     * @param ctx the parse tree
     */
    enterRestoreTableDesc?: (ctx: RestoreTableDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.restoreTableDesc`.
     * @param ctx the parse tree
     */
    exitRestoreTableDesc?: (ctx: RestoreTableDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.explainDesc`.
     * @param ctx the parse tree
     */
    enterExplainDesc?: (ctx: ExplainDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.explainDesc`.
     * @param ctx the parse tree
     */
    exitExplainDesc?: (ctx: ExplainDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.optimizerTrace`.
     * @param ctx the parse tree
     */
    enterOptimizerTrace?: (ctx: OptimizerTraceContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.optimizerTrace`.
     * @param ctx the parse tree
     */
    exitOptimizerTrace?: (ctx: OptimizerTraceContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.partitionDesc`.
     * @param ctx the parse tree
     */
    enterPartitionDesc?: (ctx: PartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.partitionDesc`.
     * @param ctx the parse tree
     */
    exitPartitionDesc?: (ctx: PartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.listPartitionDesc`.
     * @param ctx the parse tree
     */
    enterListPartitionDesc?: (ctx: ListPartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.listPartitionDesc`.
     * @param ctx the parse tree
     */
    exitListPartitionDesc?: (ctx: ListPartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.singleItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    enterSingleItemListPartitionDesc?: (ctx: SingleItemListPartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.singleItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    exitSingleItemListPartitionDesc?: (ctx: SingleItemListPartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.multiItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    enterMultiItemListPartitionDesc?: (ctx: MultiItemListPartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.multiItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    exitMultiItemListPartitionDesc?: (ctx: MultiItemListPartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.stringList`.
     * @param ctx the parse tree
     */
    enterStringList?: (ctx: StringListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.stringList`.
     * @param ctx the parse tree
     */
    exitStringList?: (ctx: StringListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.rangePartitionDesc`.
     * @param ctx the parse tree
     */
    enterRangePartitionDesc?: (ctx: RangePartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.rangePartitionDesc`.
     * @param ctx the parse tree
     */
    exitRangePartitionDesc?: (ctx: RangePartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.singleRangePartition`.
     * @param ctx the parse tree
     */
    enterSingleRangePartition?: (ctx: SingleRangePartitionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.singleRangePartition`.
     * @param ctx the parse tree
     */
    exitSingleRangePartition?: (ctx: SingleRangePartitionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.multiRangePartition`.
     * @param ctx the parse tree
     */
    enterMultiRangePartition?: (ctx: MultiRangePartitionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.multiRangePartition`.
     * @param ctx the parse tree
     */
    exitMultiRangePartition?: (ctx: MultiRangePartitionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.partitionRangeDesc`.
     * @param ctx the parse tree
     */
    enterPartitionRangeDesc?: (ctx: PartitionRangeDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.partitionRangeDesc`.
     * @param ctx the parse tree
     */
    exitPartitionRangeDesc?: (ctx: PartitionRangeDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.partitionKeyDesc`.
     * @param ctx the parse tree
     */
    enterPartitionKeyDesc?: (ctx: PartitionKeyDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.partitionKeyDesc`.
     * @param ctx the parse tree
     */
    exitPartitionKeyDesc?: (ctx: PartitionKeyDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.partitionValueList`.
     * @param ctx the parse tree
     */
    enterPartitionValueList?: (ctx: PartitionValueListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.partitionValueList`.
     * @param ctx the parse tree
     */
    exitPartitionValueList?: (ctx: PartitionValueListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.keyPartition`.
     * @param ctx the parse tree
     */
    enterKeyPartition?: (ctx: KeyPartitionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.keyPartition`.
     * @param ctx the parse tree
     */
    exitKeyPartition?: (ctx: KeyPartitionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.partitionValue`.
     * @param ctx the parse tree
     */
    enterPartitionValue?: (ctx: PartitionValueContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.partitionValue`.
     * @param ctx the parse tree
     */
    exitPartitionValue?: (ctx: PartitionValueContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.distributionClause`.
     * @param ctx the parse tree
     */
    enterDistributionClause?: (ctx: DistributionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.distributionClause`.
     * @param ctx the parse tree
     */
    exitDistributionClause?: (ctx: DistributionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.distributionDesc`.
     * @param ctx the parse tree
     */
    enterDistributionDesc?: (ctx: DistributionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.distributionDesc`.
     * @param ctx the parse tree
     */
    exitDistributionDesc?: (ctx: DistributionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.refreshSchemeDesc`.
     * @param ctx the parse tree
     */
    enterRefreshSchemeDesc?: (ctx: RefreshSchemeDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.refreshSchemeDesc`.
     * @param ctx the parse tree
     */
    exitRefreshSchemeDesc?: (ctx: RefreshSchemeDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.statusDesc`.
     * @param ctx the parse tree
     */
    enterStatusDesc?: (ctx: StatusDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.statusDesc`.
     * @param ctx the parse tree
     */
    exitStatusDesc?: (ctx: StatusDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.properties`.
     * @param ctx the parse tree
     */
    enterProperties?: (ctx: PropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.properties`.
     * @param ctx the parse tree
     */
    exitProperties?: (ctx: PropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.extProperties`.
     * @param ctx the parse tree
     */
    enterExtProperties?: (ctx: ExtPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.extProperties`.
     * @param ctx the parse tree
     */
    exitExtProperties?: (ctx: ExtPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.propertyList`.
     * @param ctx the parse tree
     */
    enterPropertyList?: (ctx: PropertyListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.propertyList`.
     * @param ctx the parse tree
     */
    exitPropertyList?: (ctx: PropertyListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.userPropertyList`.
     * @param ctx the parse tree
     */
    enterUserPropertyList?: (ctx: UserPropertyListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.userPropertyList`.
     * @param ctx the parse tree
     */
    exitUserPropertyList?: (ctx: UserPropertyListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.property`.
     * @param ctx the parse tree
     */
    enterProperty?: (ctx: PropertyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.property`.
     * @param ctx the parse tree
     */
    exitProperty?: (ctx: PropertyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.varType`.
     * @param ctx the parse tree
     */
    enterVarType?: (ctx: VarTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.varType`.
     * @param ctx the parse tree
     */
    exitVarType?: (ctx: VarTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.comment`.
     * @param ctx the parse tree
     */
    enterComment?: (ctx: CommentContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.comment`.
     * @param ctx the parse tree
     */
    exitComment?: (ctx: CommentContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.outfile`.
     * @param ctx the parse tree
     */
    enterOutfile?: (ctx: OutfileContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.outfile`.
     * @param ctx the parse tree
     */
    exitOutfile?: (ctx: OutfileContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.fileFormat`.
     * @param ctx the parse tree
     */
    enterFileFormat?: (ctx: FileFormatContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.fileFormat`.
     * @param ctx the parse tree
     */
    exitFileFormat?: (ctx: FileFormatContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.string`.
     * @param ctx the parse tree
     */
    enterString?: (ctx: StringContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.string`.
     * @param ctx the parse tree
     */
    exitString?: (ctx: StringContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.binary`.
     * @param ctx the parse tree
     */
    enterBinary?: (ctx: BinaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.binary`.
     * @param ctx the parse tree
     */
    exitBinary?: (ctx: BinaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.comparisonOperator`.
     * @param ctx the parse tree
     */
    enterComparisonOperator?: (ctx: ComparisonOperatorContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.comparisonOperator`.
     * @param ctx the parse tree
     */
    exitComparisonOperator?: (ctx: ComparisonOperatorContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.booleanValue`.
     * @param ctx the parse tree
     */
    enterBooleanValue?: (ctx: BooleanValueContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.booleanValue`.
     * @param ctx the parse tree
     */
    exitBooleanValue?: (ctx: BooleanValueContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.interval`.
     * @param ctx the parse tree
     */
    enterInterval?: (ctx: IntervalContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.interval`.
     * @param ctx the parse tree
     */
    exitInterval?: (ctx: IntervalContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.unitIdentifier`.
     * @param ctx the parse tree
     */
    enterUnitIdentifier?: (ctx: UnitIdentifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.unitIdentifier`.
     * @param ctx the parse tree
     */
    exitUnitIdentifier?: (ctx: UnitIdentifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.unitBoundary`.
     * @param ctx the parse tree
     */
    enterUnitBoundary?: (ctx: UnitBoundaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.unitBoundary`.
     * @param ctx the parse tree
     */
    exitUnitBoundary?: (ctx: UnitBoundaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.type`.
     * @param ctx the parse tree
     */
    enterType?: (ctx: TypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.type`.
     * @param ctx the parse tree
     */
    exitType?: (ctx: TypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.arrayType`.
     * @param ctx the parse tree
     */
    enterArrayType?: (ctx: ArrayTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.arrayType`.
     * @param ctx the parse tree
     */
    exitArrayType?: (ctx: ArrayTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.mapType`.
     * @param ctx the parse tree
     */
    enterMapType?: (ctx: MapTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.mapType`.
     * @param ctx the parse tree
     */
    exitMapType?: (ctx: MapTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.subfieldDesc`.
     * @param ctx the parse tree
     */
    enterSubfieldDesc?: (ctx: SubfieldDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.subfieldDesc`.
     * @param ctx the parse tree
     */
    exitSubfieldDesc?: (ctx: SubfieldDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.subfieldDescs`.
     * @param ctx the parse tree
     */
    enterSubfieldDescs?: (ctx: SubfieldDescsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.subfieldDescs`.
     * @param ctx the parse tree
     */
    exitSubfieldDescs?: (ctx: SubfieldDescsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.structType`.
     * @param ctx the parse tree
     */
    enterStructType?: (ctx: StructTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.structType`.
     * @param ctx the parse tree
     */
    exitStructType?: (ctx: StructTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.typeParameter`.
     * @param ctx the parse tree
     */
    enterTypeParameter?: (ctx: TypeParameterContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.typeParameter`.
     * @param ctx the parse tree
     */
    exitTypeParameter?: (ctx: TypeParameterContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.baseType`.
     * @param ctx the parse tree
     */
    enterBaseType?: (ctx: BaseTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.baseType`.
     * @param ctx the parse tree
     */
    exitBaseType?: (ctx: BaseTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.decimalType`.
     * @param ctx the parse tree
     */
    enterDecimalType?: (ctx: DecimalTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.decimalType`.
     * @param ctx the parse tree
     */
    exitDecimalType?: (ctx: DecimalTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.qualifiedName`.
     * @param ctx the parse tree
     */
    enterQualifiedName?: (ctx: QualifiedNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.qualifiedName`.
     * @param ctx the parse tree
     */
    exitQualifiedName?: (ctx: QualifiedNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    enterIdentifier?: (ctx: IdentifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.identifier`.
     * @param ctx the parse tree
     */
    exitIdentifier?: (ctx: IdentifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.identifierList`.
     * @param ctx the parse tree
     */
    enterIdentifierList?: (ctx: IdentifierListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.identifierList`.
     * @param ctx the parse tree
     */
    exitIdentifierList?: (ctx: IdentifierListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.identifierOrString`.
     * @param ctx the parse tree
     */
    enterIdentifierOrString?: (ctx: IdentifierOrStringContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.identifierOrString`.
     * @param ctx the parse tree
     */
    exitIdentifierOrString?: (ctx: IdentifierOrStringContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.identifierOrStringList`.
     * @param ctx the parse tree
     */
    enterIdentifierOrStringList?: (ctx: IdentifierOrStringListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.identifierOrStringList`.
     * @param ctx the parse tree
     */
    exitIdentifierOrStringList?: (ctx: IdentifierOrStringListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.identifierOrStringOrStar`.
     * @param ctx the parse tree
     */
    enterIdentifierOrStringOrStar?: (ctx: IdentifierOrStringOrStarContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.identifierOrStringOrStar`.
     * @param ctx the parse tree
     */
    exitIdentifierOrStringOrStar?: (ctx: IdentifierOrStringOrStarContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    enterUser?: (ctx: UserContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.user`.
     * @param ctx the parse tree
     */
    exitUser?: (ctx: UserContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.assignment`.
     * @param ctx the parse tree
     */
    enterAssignment?: (ctx: AssignmentContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.assignment`.
     * @param ctx the parse tree
     */
    exitAssignment?: (ctx: AssignmentContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.assignmentList`.
     * @param ctx the parse tree
     */
    enterAssignmentList?: (ctx: AssignmentListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.assignmentList`.
     * @param ctx the parse tree
     */
    exitAssignmentList?: (ctx: AssignmentListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    enterNumber?: (ctx: NumberContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.number`.
     * @param ctx the parse tree
     */
    exitNumber?: (ctx: NumberContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksParser.nonReserved`.
     * @param ctx the parse tree
     */
    enterNonReserved?: (ctx: NonReservedContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksParser.nonReserved`.
     * @param ctx the parse tree
     */
    exitNonReserved?: (ctx: NonReservedContext) => void;
}
