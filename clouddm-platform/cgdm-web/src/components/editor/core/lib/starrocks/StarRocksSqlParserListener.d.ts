import { ParseTreeListener } from "antlr4ts/tree/ParseTreeListener";
import { TableAtomContext } from "./StarRocksSqlParser";
import { InlineTableContext } from "./StarRocksSqlParser";
import { SubqueryWithAliasContext } from "./StarRocksSqlParser";
import { TableFunctionContext } from "./StarRocksSqlParser";
import { NormalizedTableFunctionContext } from "./StarRocksSqlParser";
import { FileTableFunctionContext } from "./StarRocksSqlParser";
import { ParenthesizedRelationContext } from "./StarRocksSqlParser";
import { DecimalValueContext } from "./StarRocksSqlParser";
import { DoubleValueContext } from "./StarRocksSqlParser";
import { IntegerValueContext } from "./StarRocksSqlParser";
import { ExtractContext } from "./StarRocksSqlParser";
import { GroupingOperationContext } from "./StarRocksSqlParser";
import { InformationFunctionContext } from "./StarRocksSqlParser";
import { SpecialDateTimeContext } from "./StarRocksSqlParser";
import { SpecialFunctionContext } from "./StarRocksSqlParser";
import { AggregationFunctionCallContext } from "./StarRocksSqlParser";
import { WindowFunctionCallContext } from "./StarRocksSqlParser";
import { SimpleFunctionCallContext } from "./StarRocksSqlParser";
import { FromContext } from "./StarRocksSqlParser";
import { DualContext } from "./StarRocksSqlParser";
import { SetNamesContext } from "./StarRocksSqlParser";
import { SetPasswordContext } from "./StarRocksSqlParser";
import { SetUserVarContext } from "./StarRocksSqlParser";
import { SetSystemVarContext } from "./StarRocksSqlParser";
import { SetTransactionContext } from "./StarRocksSqlParser";
import { KeyPartitionListContext } from "./StarRocksSqlParser";
import { InSubqueryContext } from "./StarRocksSqlParser";
import { InListContext } from "./StarRocksSqlParser";
import { BetweenContext } from "./StarRocksSqlParser";
import { LikeContext } from "./StarRocksSqlParser";
import { GrantRoleToUserContext } from "./StarRocksSqlParser";
import { GrantRoleToRoleContext } from "./StarRocksSqlParser";
import { BooleanExpressionDefaultContext } from "./StarRocksSqlParser";
import { IsNullContext } from "./StarRocksSqlParser";
import { ComparisonContext } from "./StarRocksSqlParser";
import { ScalarSubqueryContext } from "./StarRocksSqlParser";
import { NullLiteralContext } from "./StarRocksSqlParser";
import { BooleanLiteralContext } from "./StarRocksSqlParser";
import { NumericLiteralContext } from "./StarRocksSqlParser";
import { DateLiteralContext } from "./StarRocksSqlParser";
import { StringLiteralContext } from "./StarRocksSqlParser";
import { IntervalLiteralContext } from "./StarRocksSqlParser";
import { UnitBoundaryLiteralContext } from "./StarRocksSqlParser";
import { BinaryLiteralContext } from "./StarRocksSqlParser";
import { GrantOnUserContext } from "./StarRocksSqlParser";
import { GrantOnTableBriefContext } from "./StarRocksSqlParser";
import { GrantOnFuncContext } from "./StarRocksSqlParser";
import { GrantOnSystemContext } from "./StarRocksSqlParser";
import { GrantOnPrimaryObjContext } from "./StarRocksSqlParser";
import { GrantOnAllContext } from "./StarRocksSqlParser";
import { RevokeOnUserContext } from "./StarRocksSqlParser";
import { RevokeOnTableBriefContext } from "./StarRocksSqlParser";
import { RevokeOnFuncContext } from "./StarRocksSqlParser";
import { RevokeOnSystemContext } from "./StarRocksSqlParser";
import { RevokeOnPrimaryObjContext } from "./StarRocksSqlParser";
import { RevokeOnAllContext } from "./StarRocksSqlParser";
import { RollupContext } from "./StarRocksSqlParser";
import { CubeContext } from "./StarRocksSqlParser";
import { MultipleGroupingSetsContext } from "./StarRocksSqlParser";
import { SingleGroupingSetContext } from "./StarRocksSqlParser";
import { UserVariableExpressionContext } from "./StarRocksSqlParser";
import { SystemVariableExpressionContext } from "./StarRocksSqlParser";
import { FunctionCallExpressionContext } from "./StarRocksSqlParser";
import { OdbcFunctionCallExpressionContext } from "./StarRocksSqlParser";
import { CollateContext } from "./StarRocksSqlParser";
import { LiteralContext } from "./StarRocksSqlParser";
import { ColumnRefContext } from "./StarRocksSqlParser";
import { DereferenceContext } from "./StarRocksSqlParser";
import { ConcatContext } from "./StarRocksSqlParser";
import { ArithmeticUnaryContext } from "./StarRocksSqlParser";
import { ParenthesizedExpressionContext } from "./StarRocksSqlParser";
import { ExistsContext } from "./StarRocksSqlParser";
import { SubqueryExpressionContext } from "./StarRocksSqlParser";
import { CastContext } from "./StarRocksSqlParser";
import { ConvertContext } from "./StarRocksSqlParser";
import { SimpleCaseContext } from "./StarRocksSqlParser";
import { SearchedCaseContext } from "./StarRocksSqlParser";
import { ArrayConstructorContext } from "./StarRocksSqlParser";
import { MapConstructorContext } from "./StarRocksSqlParser";
import { CollectionSubscriptContext } from "./StarRocksSqlParser";
import { ArraySliceContext } from "./StarRocksSqlParser";
import { ArrowExpressionContext } from "./StarRocksSqlParser";
import { LambdaFunctionExprContext } from "./StarRocksSqlParser";
import { ExpressionDefaultContext } from "./StarRocksSqlParser";
import { LogicalNotContext } from "./StarRocksSqlParser";
import { LogicalBinaryContext } from "./StarRocksSqlParser";
import { ShowAllAuthenticationContext } from "./StarRocksSqlParser";
import { ShowAuthenticationForUserContext } from "./StarRocksSqlParser";
import { UnquotedIdentifierContext } from "./StarRocksSqlParser";
import { DigitIdentifierContext } from "./StarRocksSqlParser";
import { BackQuotedIdentifierContext } from "./StarRocksSqlParser";
import { ValueExpressionDefaultContext } from "./StarRocksSqlParser";
import { ArithmeticBinaryContext } from "./StarRocksSqlParser";
import { QueryPrimaryDefaultContext } from "./StarRocksSqlParser";
import { QueryWithParenthesesContext } from "./StarRocksSqlParser";
import { SetOperationContext } from "./StarRocksSqlParser";
import { SelectSingleContext } from "./StarRocksSqlParser";
import { SelectAllContext } from "./StarRocksSqlParser";
import { RevokeRoleFromUserContext } from "./StarRocksSqlParser";
import { RevokeRoleFromRoleContext } from "./StarRocksSqlParser";
import { AuthWithoutPluginContext } from "./StarRocksSqlParser";
import { AuthWithPluginContext } from "./StarRocksSqlParser";
import { UserWithoutHostContext } from "./StarRocksSqlParser";
import { UserWithHostContext } from "./StarRocksSqlParser";
import { UserWithHostAndBlanketContext } from "./StarRocksSqlParser";
import { ProgramContext } from "./StarRocksSqlParser";
import { SqlStatementsContext } from "./StarRocksSqlParser";
import { SingleStatementContext } from "./StarRocksSqlParser";
import { StatementContext } from "./StarRocksSqlParser";
import { UseDatabaseStatementContext } from "./StarRocksSqlParser";
import { UseCatalogStatementContext } from "./StarRocksSqlParser";
import { SetCatalogStatementContext } from "./StarRocksSqlParser";
import { ShowDatabasesStatementContext } from "./StarRocksSqlParser";
import { AlterDbQuotaStatementContext } from "./StarRocksSqlParser";
import { CreateDbStatementContext } from "./StarRocksSqlParser";
import { DropDbStatementContext } from "./StarRocksSqlParser";
import { ShowCreateDbStatementContext } from "./StarRocksSqlParser";
import { AlterDatabaseRenameStatementContext } from "./StarRocksSqlParser";
import { RecoverDbStmtContext } from "./StarRocksSqlParser";
import { ShowDataStmtContext } from "./StarRocksSqlParser";
import { CreateTableStatementContext } from "./StarRocksSqlParser";
import { ColumnDescContext } from "./StarRocksSqlParser";
import { CharsetNameContext } from "./StarRocksSqlParser";
import { DefaultDescContext } from "./StarRocksSqlParser";
import { MaterializedColumnDescContext } from "./StarRocksSqlParser";
import { IndexDescContext } from "./StarRocksSqlParser";
import { EngineDescContext } from "./StarRocksSqlParser";
import { CharsetDescContext } from "./StarRocksSqlParser";
import { CollateDescContext } from "./StarRocksSqlParser";
import { KeyDescContext } from "./StarRocksSqlParser";
import { OrderByDescContext } from "./StarRocksSqlParser";
import { AggDescContext } from "./StarRocksSqlParser";
import { RollupDescContext } from "./StarRocksSqlParser";
import { RollupItemContext } from "./StarRocksSqlParser";
import { DupKeysContext } from "./StarRocksSqlParser";
import { FromRollupContext } from "./StarRocksSqlParser";
import { WithMaskingPolicyContext } from "./StarRocksSqlParser";
import { WithRowAccessPolicyContext } from "./StarRocksSqlParser";
import { CreateTemporaryTableStatementContext } from "./StarRocksSqlParser";
import { CreateTableAsSelectStatementContext } from "./StarRocksSqlParser";
import { DropTableStatementContext } from "./StarRocksSqlParser";
import { AlterTableStatementContext } from "./StarRocksSqlParser";
import { CreateIndexStatementContext } from "./StarRocksSqlParser";
import { DropIndexStatementContext } from "./StarRocksSqlParser";
import { IndexTypeContext } from "./StarRocksSqlParser";
import { ShowTableStatementContext } from "./StarRocksSqlParser";
import { ShowCreateTableStatementContext } from "./StarRocksSqlParser";
import { ShowColumnStatementContext } from "./StarRocksSqlParser";
import { ShowTableStatusStatementContext } from "./StarRocksSqlParser";
import { RefreshTableStatementContext } from "./StarRocksSqlParser";
import { ShowAlterStatementContext } from "./StarRocksSqlParser";
import { DescTableStatementContext } from "./StarRocksSqlParser";
import { CreateTableLikeStatementContext } from "./StarRocksSqlParser";
import { ShowIndexStatementContext } from "./StarRocksSqlParser";
import { RecoverTableStatementContext } from "./StarRocksSqlParser";
import { TruncateTableStatementContext } from "./StarRocksSqlParser";
import { CancelAlterTableStatementContext } from "./StarRocksSqlParser";
import { ShowPartitionsStatementContext } from "./StarRocksSqlParser";
import { RecoverPartitionStatementContext } from "./StarRocksSqlParser";
import { CreateViewStatementContext } from "./StarRocksSqlParser";
import { AlterViewStatementContext } from "./StarRocksSqlParser";
import { DropViewStatementContext } from "./StarRocksSqlParser";
import { ColumnNameWithCommentContext } from "./StarRocksSqlParser";
import { SubmitTaskStatementContext } from "./StarRocksSqlParser";
import { TaskExecSqlContext } from "./StarRocksSqlParser";
import { DropTaskStatementContext } from "./StarRocksSqlParser";
import { CreateMaterializedViewStatementContext } from "./StarRocksSqlParser";
import { MaterializedViewDescContext } from "./StarRocksSqlParser";
import { ShowMaterializedViewsStatementContext } from "./StarRocksSqlParser";
import { DropMaterializedViewStatementContext } from "./StarRocksSqlParser";
import { AlterMaterializedViewStatementContext } from "./StarRocksSqlParser";
import { RefreshMaterializedViewStatementContext } from "./StarRocksSqlParser";
import { CancelRefreshMaterializedViewStatementContext } from "./StarRocksSqlParser";
import { AdminSetConfigStatementContext } from "./StarRocksSqlParser";
import { AdminSetReplicaStatusStatementContext } from "./StarRocksSqlParser";
import { AdminShowConfigStatementContext } from "./StarRocksSqlParser";
import { AdminShowReplicaDistributionStatementContext } from "./StarRocksSqlParser";
import { AdminShowReplicaStatusStatementContext } from "./StarRocksSqlParser";
import { AdminRepairTableStatementContext } from "./StarRocksSqlParser";
import { AdminCancelRepairTableStatementContext } from "./StarRocksSqlParser";
import { AdminCheckTabletsStatementContext } from "./StarRocksSqlParser";
import { KillStatementContext } from "./StarRocksSqlParser";
import { SyncStatementContext } from "./StarRocksSqlParser";
import { AlterSystemStatementContext } from "./StarRocksSqlParser";
import { CancelAlterSystemStatementContext } from "./StarRocksSqlParser";
import { ShowComputeNodesStatementContext } from "./StarRocksSqlParser";
import { CreateExternalCatalogStatementContext } from "./StarRocksSqlParser";
import { ShowCreateExternalCatalogStatementContext } from "./StarRocksSqlParser";
import { DropExternalCatalogStatementContext } from "./StarRocksSqlParser";
import { ShowCatalogsStatementContext } from "./StarRocksSqlParser";
import { CreateWarehouseStatementContext } from "./StarRocksSqlParser";
import { ShowWarehousesStatementContext } from "./StarRocksSqlParser";
import { DropWarehouseStatementContext } from "./StarRocksSqlParser";
import { AlterWarehouseStatementContext } from "./StarRocksSqlParser";
import { ShowClustersStatementContext } from "./StarRocksSqlParser";
import { SuspendWarehouseStatementContext } from "./StarRocksSqlParser";
import { ResumeWarehouseStatementContext } from "./StarRocksSqlParser";
import { CreateStorageVolumeStatementContext } from "./StarRocksSqlParser";
import { TypeDescContext } from "./StarRocksSqlParser";
import { LocationsDescContext } from "./StarRocksSqlParser";
import { ShowStorageVolumesStatementContext } from "./StarRocksSqlParser";
import { DropStorageVolumeStatementContext } from "./StarRocksSqlParser";
import { AlterStorageVolumeStatementContext } from "./StarRocksSqlParser";
import { AlterStorageVolumeClauseContext } from "./StarRocksSqlParser";
import { ModifyStorageVolumePropertiesClauseContext } from "./StarRocksSqlParser";
import { ModifyStorageVolumeCommentClauseContext } from "./StarRocksSqlParser";
import { DescStorageVolumeStatementContext } from "./StarRocksSqlParser";
import { SetDefaultStorageVolumeStatementContext } from "./StarRocksSqlParser";
import { AlterClauseContext } from "./StarRocksSqlParser";
import { AddFrontendClauseContext } from "./StarRocksSqlParser";
import { DropFrontendClauseContext } from "./StarRocksSqlParser";
import { ModifyFrontendHostClauseContext } from "./StarRocksSqlParser";
import { AddBackendClauseContext } from "./StarRocksSqlParser";
import { DropBackendClauseContext } from "./StarRocksSqlParser";
import { DecommissionBackendClauseContext } from "./StarRocksSqlParser";
import { ModifyBackendHostClauseContext } from "./StarRocksSqlParser";
import { AddComputeNodeClauseContext } from "./StarRocksSqlParser";
import { DropComputeNodeClauseContext } from "./StarRocksSqlParser";
import { ModifyBrokerClauseContext } from "./StarRocksSqlParser";
import { AlterLoadErrorUrlClauseContext } from "./StarRocksSqlParser";
import { CreateImageClauseContext } from "./StarRocksSqlParser";
import { CleanTabletSchedQClauseContext } from "./StarRocksSqlParser";
import { CreateIndexClauseContext } from "./StarRocksSqlParser";
import { DropIndexClauseContext } from "./StarRocksSqlParser";
import { TableRenameClauseContext } from "./StarRocksSqlParser";
import { SwapTableClauseContext } from "./StarRocksSqlParser";
import { ModifyTablePropertiesClauseContext } from "./StarRocksSqlParser";
import { ModifyCommentClauseContext } from "./StarRocksSqlParser";
import { AddColumnClauseContext } from "./StarRocksSqlParser";
import { AddColumnsClauseContext } from "./StarRocksSqlParser";
import { DropColumnClauseContext } from "./StarRocksSqlParser";
import { ModifyColumnClauseContext } from "./StarRocksSqlParser";
import { ColumnRenameClauseContext } from "./StarRocksSqlParser";
import { ReorderColumnsClauseContext } from "./StarRocksSqlParser";
import { RollupRenameClauseContext } from "./StarRocksSqlParser";
import { CompactionClauseContext } from "./StarRocksSqlParser";
import { ApplyMaskingPolicyClauseContext } from "./StarRocksSqlParser";
import { ApplyRowAccessPolicyClauseContext } from "./StarRocksSqlParser";
import { AddPartitionClauseContext } from "./StarRocksSqlParser";
import { DropPartitionClauseContext } from "./StarRocksSqlParser";
import { TruncatePartitionClauseContext } from "./StarRocksSqlParser";
import { ModifyPartitionClauseContext } from "./StarRocksSqlParser";
import { ReplacePartitionClauseContext } from "./StarRocksSqlParser";
import { PartitionRenameClauseContext } from "./StarRocksSqlParser";
import { InsertStatementContext } from "./StarRocksSqlParser";
import { UpdateStatementContext } from "./StarRocksSqlParser";
import { DeleteStatementContext } from "./StarRocksSqlParser";
import { CreateRoutineLoadStatementContext } from "./StarRocksSqlParser";
import { AlterRoutineLoadStatementContext } from "./StarRocksSqlParser";
import { DataSourceContext } from "./StarRocksSqlParser";
import { LoadPropertiesExprContext } from "./StarRocksSqlParser";
import { LoadPropertiesContext } from "./StarRocksSqlParser";
import { ColSeparatorPropertyContext } from "./StarRocksSqlParser";
import { RowDelimiterPropertyContext } from "./StarRocksSqlParser";
import { ImportColumnsContext } from "./StarRocksSqlParser";
import { ColumnPropertiesContext } from "./StarRocksSqlParser";
import { JobPropertiesContext } from "./StarRocksSqlParser";
import { DataSourcePropertiesContext } from "./StarRocksSqlParser";
import { StopRoutineLoadStatementContext } from "./StarRocksSqlParser";
import { ResumeRoutineLoadStatementContext } from "./StarRocksSqlParser";
import { PauseRoutineLoadStatementContext } from "./StarRocksSqlParser";
import { ShowRoutineLoadStatementContext } from "./StarRocksSqlParser";
import { ShowRoutineLoadTaskStatementContext } from "./StarRocksSqlParser";
import { ShowStreamLoadStatementContext } from "./StarRocksSqlParser";
import { AnalyzeStatementContext } from "./StarRocksSqlParser";
import { DropStatsStatementContext } from "./StarRocksSqlParser";
import { AnalyzeHistogramStatementContext } from "./StarRocksSqlParser";
import { DropHistogramStatementContext } from "./StarRocksSqlParser";
import { CreateAnalyzeStatementContext } from "./StarRocksSqlParser";
import { DropAnalyzeJobStatementContext } from "./StarRocksSqlParser";
import { ShowAnalyzeStatementContext } from "./StarRocksSqlParser";
import { ShowStatsMetaStatementContext } from "./StarRocksSqlParser";
import { ShowHistogramMetaStatementContext } from "./StarRocksSqlParser";
import { KillAnalyzeStatementContext } from "./StarRocksSqlParser";
import { AnalyzeProfileStatementContext } from "./StarRocksSqlParser";
import { CreateResourceGroupStatementContext } from "./StarRocksSqlParser";
import { DropResourceGroupStatementContext } from "./StarRocksSqlParser";
import { AlterResourceGroupStatementContext } from "./StarRocksSqlParser";
import { ShowResourceGroupStatementContext } from "./StarRocksSqlParser";
import { CreateResourceStatementContext } from "./StarRocksSqlParser";
import { AlterResourceStatementContext } from "./StarRocksSqlParser";
import { DropResourceStatementContext } from "./StarRocksSqlParser";
import { ShowResourceStatementContext } from "./StarRocksSqlParser";
import { ClassifierContext } from "./StarRocksSqlParser";
import { ShowFunctionsStatementContext } from "./StarRocksSqlParser";
import { DropFunctionStatementContext } from "./StarRocksSqlParser";
import { CreateFunctionStatementContext } from "./StarRocksSqlParser";
import { TypeListContext } from "./StarRocksSqlParser";
import { LoadStatementContext } from "./StarRocksSqlParser";
import { LabelNameContext } from "./StarRocksSqlParser";
import { DataDescListContext } from "./StarRocksSqlParser";
import { DataDescContext } from "./StarRocksSqlParser";
import { FormatPropsContext } from "./StarRocksSqlParser";
import { BrokerDescContext } from "./StarRocksSqlParser";
import { ResourceDescContext } from "./StarRocksSqlParser";
import { ShowLoadStatementContext } from "./StarRocksSqlParser";
import { ShowLoadWarningsStatementContext } from "./StarRocksSqlParser";
import { CancelLoadStatementContext } from "./StarRocksSqlParser";
import { AlterLoadStatementContext } from "./StarRocksSqlParser";
import { CancelCompactionStatementContext } from "./StarRocksSqlParser";
import { ShowAuthorStatementContext } from "./StarRocksSqlParser";
import { ShowBackendsStatementContext } from "./StarRocksSqlParser";
import { ShowBrokerStatementContext } from "./StarRocksSqlParser";
import { ShowCharsetStatementContext } from "./StarRocksSqlParser";
import { ShowCollationStatementContext } from "./StarRocksSqlParser";
import { ShowDeleteStatementContext } from "./StarRocksSqlParser";
import { ShowDynamicPartitionStatementContext } from "./StarRocksSqlParser";
import { ShowEventsStatementContext } from "./StarRocksSqlParser";
import { ShowEnginesStatementContext } from "./StarRocksSqlParser";
import { ShowFrontendsStatementContext } from "./StarRocksSqlParser";
import { ShowPluginsStatementContext } from "./StarRocksSqlParser";
import { ShowRepositoriesStatementContext } from "./StarRocksSqlParser";
import { ShowOpenTableStatementContext } from "./StarRocksSqlParser";
import { ShowPrivilegesStatementContext } from "./StarRocksSqlParser";
import { ShowProcedureStatementContext } from "./StarRocksSqlParser";
import { ShowProcStatementContext } from "./StarRocksSqlParser";
import { ShowProcesslistStatementContext } from "./StarRocksSqlParser";
import { ShowProfilelistStatementContext } from "./StarRocksSqlParser";
import { ShowStatusStatementContext } from "./StarRocksSqlParser";
import { ShowTabletStatementContext } from "./StarRocksSqlParser";
import { ShowTransactionStatementContext } from "./StarRocksSqlParser";
import { ShowTriggersStatementContext } from "./StarRocksSqlParser";
import { ShowUserPropertyStatementContext } from "./StarRocksSqlParser";
import { ShowVariablesStatementContext } from "./StarRocksSqlParser";
import { ShowWarningStatementContext } from "./StarRocksSqlParser";
import { HelpStatementContext } from "./StarRocksSqlParser";
import { CreateUserStatementContext } from "./StarRocksSqlParser";
import { DropUserStatementContext } from "./StarRocksSqlParser";
import { AlterUserStatementContext } from "./StarRocksSqlParser";
import { ShowUserStatementContext } from "./StarRocksSqlParser";
import { ShowAuthenticationStatementContext } from "./StarRocksSqlParser";
import { ExecuteAsStatementContext } from "./StarRocksSqlParser";
import { CreateRoleStatementContext } from "./StarRocksSqlParser";
import { AlterRoleStatementContext } from "./StarRocksSqlParser";
import { DropRoleStatementContext } from "./StarRocksSqlParser";
import { ShowRolesStatementContext } from "./StarRocksSqlParser";
import { GrantRoleStatementContext } from "./StarRocksSqlParser";
import { RevokeRoleStatementContext } from "./StarRocksSqlParser";
import { SetRoleStatementContext } from "./StarRocksSqlParser";
import { SetDefaultRoleStatementContext } from "./StarRocksSqlParser";
import { GrantRevokeClauseContext } from "./StarRocksSqlParser";
import { GrantPrivilegeStatementContext } from "./StarRocksSqlParser";
import { RevokePrivilegeStatementContext } from "./StarRocksSqlParser";
import { ShowGrantsStatementContext } from "./StarRocksSqlParser";
import { CreateSecurityIntegrationStatementContext } from "./StarRocksSqlParser";
import { AlterSecurityIntegrationStatementContext } from "./StarRocksSqlParser";
import { DropSecurityIntegrationStatementContext } from "./StarRocksSqlParser";
import { ShowSecurityIntegrationStatementContext } from "./StarRocksSqlParser";
import { ShowCreateSecurityIntegrationStatementContext } from "./StarRocksSqlParser";
import { CreateRoleMappingStatementContext } from "./StarRocksSqlParser";
import { AlterRoleMappingStatementContext } from "./StarRocksSqlParser";
import { DropRoleMappingStatementContext } from "./StarRocksSqlParser";
import { ShowRoleMappingStatementContext } from "./StarRocksSqlParser";
import { RefreshRoleMappingStatementContext } from "./StarRocksSqlParser";
import { AuthOptionContext } from "./StarRocksSqlParser";
import { PrivObjectNameContext } from "./StarRocksSqlParser";
import { PrivObjectNameListContext } from "./StarRocksSqlParser";
import { PrivFunctionObjectNameListContext } from "./StarRocksSqlParser";
import { PrivilegeTypeListContext } from "./StarRocksSqlParser";
import { PrivilegeTypeContext } from "./StarRocksSqlParser";
import { PrivObjectTypeContext } from "./StarRocksSqlParser";
import { PrivObjectTypePluralContext } from "./StarRocksSqlParser";
import { CreateMaskingPolicyStatementContext } from "./StarRocksSqlParser";
import { DropMaskingPolicyStatementContext } from "./StarRocksSqlParser";
import { AlterMaskingPolicyStatementContext } from "./StarRocksSqlParser";
import { ShowMaskingPolicyStatementContext } from "./StarRocksSqlParser";
import { ShowCreateMaskingPolicyStatementContext } from "./StarRocksSqlParser";
import { CreateRowAccessPolicyStatementContext } from "./StarRocksSqlParser";
import { DropRowAccessPolicyStatementContext } from "./StarRocksSqlParser";
import { AlterRowAccessPolicyStatementContext } from "./StarRocksSqlParser";
import { ShowRowAccessPolicyStatementContext } from "./StarRocksSqlParser";
import { ShowCreateRowAccessPolicyStatementContext } from "./StarRocksSqlParser";
import { PolicySignatureContext } from "./StarRocksSqlParser";
import { BackupStatementContext } from "./StarRocksSqlParser";
import { CancelBackupStatementContext } from "./StarRocksSqlParser";
import { ShowBackupStatementContext } from "./StarRocksSqlParser";
import { RestoreStatementContext } from "./StarRocksSqlParser";
import { CancelRestoreStatementContext } from "./StarRocksSqlParser";
import { ShowRestoreStatementContext } from "./StarRocksSqlParser";
import { ShowSnapshotStatementContext } from "./StarRocksSqlParser";
import { CreateRepositoryStatementContext } from "./StarRocksSqlParser";
import { DropRepositoryStatementContext } from "./StarRocksSqlParser";
import { AddSqlBlackListStatementContext } from "./StarRocksSqlParser";
import { DelSqlBlackListStatementContext } from "./StarRocksSqlParser";
import { ShowSqlBlackListStatementContext } from "./StarRocksSqlParser";
import { ShowWhiteListStatementContext } from "./StarRocksSqlParser";
import { ExportStatementContext } from "./StarRocksSqlParser";
import { CancelExportStatementContext } from "./StarRocksSqlParser";
import { ShowExportStatementContext } from "./StarRocksSqlParser";
import { InstallPluginStatementContext } from "./StarRocksSqlParser";
import { UninstallPluginStatementContext } from "./StarRocksSqlParser";
import { CreateFileStatementContext } from "./StarRocksSqlParser";
import { DropFileStatementContext } from "./StarRocksSqlParser";
import { ShowSmallFilesStatementContext } from "./StarRocksSqlParser";
import { SetStatementContext } from "./StarRocksSqlParser";
import { SetVarContext } from "./StarRocksSqlParser";
import { Transaction_characteristicsContext } from "./StarRocksSqlParser";
import { Transaction_access_modeContext } from "./StarRocksSqlParser";
import { Isolation_levelContext } from "./StarRocksSqlParser";
import { Isolation_typesContext } from "./StarRocksSqlParser";
import { SetExprOrDefaultContext } from "./StarRocksSqlParser";
import { SetUserPropertyStatementContext } from "./StarRocksSqlParser";
import { RoleListContext } from "./StarRocksSqlParser";
import { ExecuteScriptStatementContext } from "./StarRocksSqlParser";
import { UnsupportedStatementContext } from "./StarRocksSqlParser";
import { Lock_itemContext } from "./StarRocksSqlParser";
import { Lock_typeContext } from "./StarRocksSqlParser";
import { QueryStatementContext } from "./StarRocksSqlParser";
import { QueryRelationContext } from "./StarRocksSqlParser";
import { WithClauseContext } from "./StarRocksSqlParser";
import { QueryNoWithContext } from "./StarRocksSqlParser";
import { TemporalClauseContext } from "./StarRocksSqlParser";
import { QueryPrimaryContext } from "./StarRocksSqlParser";
import { SubqueryContext } from "./StarRocksSqlParser";
import { RowConstructorContext } from "./StarRocksSqlParser";
import { SortItemContext } from "./StarRocksSqlParser";
import { LimitElementContext } from "./StarRocksSqlParser";
import { QuerySpecificationContext } from "./StarRocksSqlParser";
import { FromClauseContext } from "./StarRocksSqlParser";
import { GroupingElementContext } from "./StarRocksSqlParser";
import { GroupingSetContext } from "./StarRocksSqlParser";
import { CommonTableExpressionContext } from "./StarRocksSqlParser";
import { SetQuantifierContext } from "./StarRocksSqlParser";
import { SelectItemContext } from "./StarRocksSqlParser";
import { RelationsContext } from "./StarRocksSqlParser";
import { RelationContext } from "./StarRocksSqlParser";
import { RelationPrimaryContext } from "./StarRocksSqlParser";
import { JoinRelationContext } from "./StarRocksSqlParser";
import { CrossOrInnerJoinTypeContext } from "./StarRocksSqlParser";
import { OuterAndSemiJoinTypeContext } from "./StarRocksSqlParser";
import { BracketHintContext } from "./StarRocksSqlParser";
import { SetVarHintContext } from "./StarRocksSqlParser";
import { HintMapContext } from "./StarRocksSqlParser";
import { JoinCriteriaContext } from "./StarRocksSqlParser";
import { ColumnAliasesContext } from "./StarRocksSqlParser";
import { PartitionNamesContext } from "./StarRocksSqlParser";
import { KeyPartitionsContext } from "./StarRocksSqlParser";
import { TabletListContext } from "./StarRocksSqlParser";
import { ExpressionsWithDefaultContext } from "./StarRocksSqlParser";
import { ExpressionOrDefaultContext } from "./StarRocksSqlParser";
import { MapExpressionListContext } from "./StarRocksSqlParser";
import { MapExpressionContext } from "./StarRocksSqlParser";
import { ExpressionSingletonContext } from "./StarRocksSqlParser";
import { ExpressionContext } from "./StarRocksSqlParser";
import { ExpressionListContext } from "./StarRocksSqlParser";
import { BooleanExpressionContext } from "./StarRocksSqlParser";
import { PredicateContext } from "./StarRocksSqlParser";
import { TupleInSubqueryContext } from "./StarRocksSqlParser";
import { PredicateOperationsContext } from "./StarRocksSqlParser";
import { ValueExpressionContext } from "./StarRocksSqlParser";
import { PrimaryExpressionContext } from "./StarRocksSqlParser";
import { LiteralExpressionContext } from "./StarRocksSqlParser";
import { FunctionCallContext } from "./StarRocksSqlParser";
import { AggregationFunctionContext } from "./StarRocksSqlParser";
import { UserVariableContext } from "./StarRocksSqlParser";
import { SystemVariableContext } from "./StarRocksSqlParser";
import { ColumnReferenceContext } from "./StarRocksSqlParser";
import { InformationFunctionExpressionContext } from "./StarRocksSqlParser";
import { SpecialDateTimeExpressionContext } from "./StarRocksSqlParser";
import { SpecialFunctionExpressionContext } from "./StarRocksSqlParser";
import { WindowFunctionContext } from "./StarRocksSqlParser";
import { WhenClauseContext } from "./StarRocksSqlParser";
import { OverContext } from "./StarRocksSqlParser";
import { IgnoreNullsContext } from "./StarRocksSqlParser";
import { TableDescContext } from "./StarRocksSqlParser";
import { RestoreTableDescContext } from "./StarRocksSqlParser";
import { ExplainDescContext } from "./StarRocksSqlParser";
import { OptimizerTraceContext } from "./StarRocksSqlParser";
import { PartitionDescContext } from "./StarRocksSqlParser";
import { ListPartitionDescContext } from "./StarRocksSqlParser";
import { SingleItemListPartitionDescContext } from "./StarRocksSqlParser";
import { MultiItemListPartitionDescContext } from "./StarRocksSqlParser";
import { StringListContext } from "./StarRocksSqlParser";
import { RangePartitionDescContext } from "./StarRocksSqlParser";
import { SingleRangePartitionContext } from "./StarRocksSqlParser";
import { MultiRangePartitionContext } from "./StarRocksSqlParser";
import { PartitionRangeDescContext } from "./StarRocksSqlParser";
import { PartitionKeyDescContext } from "./StarRocksSqlParser";
import { PartitionValueListContext } from "./StarRocksSqlParser";
import { KeyPartitionContext } from "./StarRocksSqlParser";
import { PartitionValueContext } from "./StarRocksSqlParser";
import { DistributionClauseContext } from "./StarRocksSqlParser";
import { DistributionDescContext } from "./StarRocksSqlParser";
import { RefreshSchemeDescContext } from "./StarRocksSqlParser";
import { StatusDescContext } from "./StarRocksSqlParser";
import { PropertiesContext } from "./StarRocksSqlParser";
import { ExtPropertiesContext } from "./StarRocksSqlParser";
import { PropertyListContext } from "./StarRocksSqlParser";
import { UserPropertyListContext } from "./StarRocksSqlParser";
import { PropertyContext } from "./StarRocksSqlParser";
import { VarTypeContext } from "./StarRocksSqlParser";
import { CommentContext } from "./StarRocksSqlParser";
import { OutfileContext } from "./StarRocksSqlParser";
import { FileFormatContext } from "./StarRocksSqlParser";
import { StringContext } from "./StarRocksSqlParser";
import { BinaryContext } from "./StarRocksSqlParser";
import { ComparisonOperatorContext } from "./StarRocksSqlParser";
import { BooleanValueContext } from "./StarRocksSqlParser";
import { IntervalContext } from "./StarRocksSqlParser";
import { UnitIdentifierContext } from "./StarRocksSqlParser";
import { UnitBoundaryContext } from "./StarRocksSqlParser";
import { TypeContext } from "./StarRocksSqlParser";
import { ArrayTypeContext } from "./StarRocksSqlParser";
import { MapTypeContext } from "./StarRocksSqlParser";
import { SubfieldDescContext } from "./StarRocksSqlParser";
import { SubfieldDescsContext } from "./StarRocksSqlParser";
import { StructTypeContext } from "./StarRocksSqlParser";
import { TypeParameterContext } from "./StarRocksSqlParser";
import { BaseTypeContext } from "./StarRocksSqlParser";
import { DecimalTypeContext } from "./StarRocksSqlParser";
import { QualifiedNameContext } from "./StarRocksSqlParser";
import { IdentifierContext } from "./StarRocksSqlParser";
import { IdentifierListContext } from "./StarRocksSqlParser";
import { IdentifierOrStringContext } from "./StarRocksSqlParser";
import { IdentifierOrStringListContext } from "./StarRocksSqlParser";
import { IdentifierOrStringOrStarContext } from "./StarRocksSqlParser";
import { UserContext } from "./StarRocksSqlParser";
import { AssignmentContext } from "./StarRocksSqlParser";
import { AssignmentListContext } from "./StarRocksSqlParser";
import { NumberContext } from "./StarRocksSqlParser";
import { NonReservedContext } from "./StarRocksSqlParser";
/**
 * This interface defines a complete listener for a parse tree produced by
 * `StarRocksSqlParser`.
 */
export interface StarRocksSqlParserListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by the `tableAtom`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterTableAtom?: (ctx: TableAtomContext) => void;
    /**
     * Exit a parse tree produced by the `tableAtom`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitTableAtom?: (ctx: TableAtomContext) => void;
    /**
     * Enter a parse tree produced by the `inlineTable`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterInlineTable?: (ctx: InlineTableContext) => void;
    /**
     * Exit a parse tree produced by the `inlineTable`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitInlineTable?: (ctx: InlineTableContext) => void;
    /**
     * Enter a parse tree produced by the `subqueryWithAlias`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterSubqueryWithAlias?: (ctx: SubqueryWithAliasContext) => void;
    /**
     * Exit a parse tree produced by the `subqueryWithAlias`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitSubqueryWithAlias?: (ctx: SubqueryWithAliasContext) => void;
    /**
     * Enter a parse tree produced by the `tableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterTableFunction?: (ctx: TableFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `tableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitTableFunction?: (ctx: TableFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `normalizedTableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterNormalizedTableFunction?: (ctx: NormalizedTableFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `normalizedTableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitNormalizedTableFunction?: (ctx: NormalizedTableFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `fileTableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterFileTableFunction?: (ctx: FileTableFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `fileTableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitFileTableFunction?: (ctx: FileTableFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `parenthesizedRelation`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterParenthesizedRelation?: (ctx: ParenthesizedRelationContext) => void;
    /**
     * Exit a parse tree produced by the `parenthesizedRelation`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitParenthesizedRelation?: (ctx: ParenthesizedRelationContext) => void;
    /**
     * Enter a parse tree produced by the `decimalValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    enterDecimalValue?: (ctx: DecimalValueContext) => void;
    /**
     * Exit a parse tree produced by the `decimalValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    exitDecimalValue?: (ctx: DecimalValueContext) => void;
    /**
     * Enter a parse tree produced by the `doubleValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    enterDoubleValue?: (ctx: DoubleValueContext) => void;
    /**
     * Exit a parse tree produced by the `doubleValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    exitDoubleValue?: (ctx: DoubleValueContext) => void;
    /**
     * Enter a parse tree produced by the `integerValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    enterIntegerValue?: (ctx: IntegerValueContext) => void;
    /**
     * Exit a parse tree produced by the `integerValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    exitIntegerValue?: (ctx: IntegerValueContext) => void;
    /**
     * Enter a parse tree produced by the `extract`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterExtract?: (ctx: ExtractContext) => void;
    /**
     * Exit a parse tree produced by the `extract`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitExtract?: (ctx: ExtractContext) => void;
    /**
     * Enter a parse tree produced by the `groupingOperation`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterGroupingOperation?: (ctx: GroupingOperationContext) => void;
    /**
     * Exit a parse tree produced by the `groupingOperation`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitGroupingOperation?: (ctx: GroupingOperationContext) => void;
    /**
     * Enter a parse tree produced by the `informationFunction`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterInformationFunction?: (ctx: InformationFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `informationFunction`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitInformationFunction?: (ctx: InformationFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `specialDateTime`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterSpecialDateTime?: (ctx: SpecialDateTimeContext) => void;
    /**
     * Exit a parse tree produced by the `specialDateTime`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitSpecialDateTime?: (ctx: SpecialDateTimeContext) => void;
    /**
     * Enter a parse tree produced by the `specialFunction`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterSpecialFunction?: (ctx: SpecialFunctionContext) => void;
    /**
     * Exit a parse tree produced by the `specialFunction`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitSpecialFunction?: (ctx: SpecialFunctionContext) => void;
    /**
     * Enter a parse tree produced by the `aggregationFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterAggregationFunctionCall?: (ctx: AggregationFunctionCallContext) => void;
    /**
     * Exit a parse tree produced by the `aggregationFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitAggregationFunctionCall?: (ctx: AggregationFunctionCallContext) => void;
    /**
     * Enter a parse tree produced by the `windowFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterWindowFunctionCall?: (ctx: WindowFunctionCallContext) => void;
    /**
     * Exit a parse tree produced by the `windowFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitWindowFunctionCall?: (ctx: WindowFunctionCallContext) => void;
    /**
     * Enter a parse tree produced by the `simpleFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterSimpleFunctionCall?: (ctx: SimpleFunctionCallContext) => void;
    /**
     * Exit a parse tree produced by the `simpleFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitSimpleFunctionCall?: (ctx: SimpleFunctionCallContext) => void;
    /**
     * Enter a parse tree produced by the `from`
     * labeled alternative in `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     */
    enterFrom?: (ctx: FromContext) => void;
    /**
     * Exit a parse tree produced by the `from`
     * labeled alternative in `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     */
    exitFrom?: (ctx: FromContext) => void;
    /**
     * Enter a parse tree produced by the `dual`
     * labeled alternative in `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     */
    enterDual?: (ctx: DualContext) => void;
    /**
     * Exit a parse tree produced by the `dual`
     * labeled alternative in `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     */
    exitDual?: (ctx: DualContext) => void;
    /**
     * Enter a parse tree produced by the `setNames`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetNames?: (ctx: SetNamesContext) => void;
    /**
     * Exit a parse tree produced by the `setNames`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetNames?: (ctx: SetNamesContext) => void;
    /**
     * Enter a parse tree produced by the `setPassword`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetPassword?: (ctx: SetPasswordContext) => void;
    /**
     * Exit a parse tree produced by the `setPassword`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetPassword?: (ctx: SetPasswordContext) => void;
    /**
     * Enter a parse tree produced by the `setUserVar`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetUserVar?: (ctx: SetUserVarContext) => void;
    /**
     * Exit a parse tree produced by the `setUserVar`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetUserVar?: (ctx: SetUserVarContext) => void;
    /**
     * Enter a parse tree produced by the `setSystemVar`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetSystemVar?: (ctx: SetSystemVarContext) => void;
    /**
     * Exit a parse tree produced by the `setSystemVar`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetSystemVar?: (ctx: SetSystemVarContext) => void;
    /**
     * Enter a parse tree produced by the `setTransaction`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetTransaction?: (ctx: SetTransactionContext) => void;
    /**
     * Exit a parse tree produced by the `setTransaction`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetTransaction?: (ctx: SetTransactionContext) => void;
    /**
     * Enter a parse tree produced by the `keyPartitionList`
     * labeled alternative in `StarRocksSqlParser.keyPartitions`.
     * @param ctx the parse tree
     */
    enterKeyPartitionList?: (ctx: KeyPartitionListContext) => void;
    /**
     * Exit a parse tree produced by the `keyPartitionList`
     * labeled alternative in `StarRocksSqlParser.keyPartitions`.
     * @param ctx the parse tree
     */
    exitKeyPartitionList?: (ctx: KeyPartitionListContext) => void;
    /**
     * Enter a parse tree produced by the `inSubquery`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterInSubquery?: (ctx: InSubqueryContext) => void;
    /**
     * Exit a parse tree produced by the `inSubquery`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitInSubquery?: (ctx: InSubqueryContext) => void;
    /**
     * Enter a parse tree produced by the `inList`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterInList?: (ctx: InListContext) => void;
    /**
     * Exit a parse tree produced by the `inList`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitInList?: (ctx: InListContext) => void;
    /**
     * Enter a parse tree produced by the `between`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterBetween?: (ctx: BetweenContext) => void;
    /**
     * Exit a parse tree produced by the `between`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitBetween?: (ctx: BetweenContext) => void;
    /**
     * Enter a parse tree produced by the `like`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterLike?: (ctx: LikeContext) => void;
    /**
     * Exit a parse tree produced by the `like`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitLike?: (ctx: LikeContext) => void;
    /**
     * Enter a parse tree produced by the `grantRoleToUser`
     * labeled alternative in `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    enterGrantRoleToUser?: (ctx: GrantRoleToUserContext) => void;
    /**
     * Exit a parse tree produced by the `grantRoleToUser`
     * labeled alternative in `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    exitGrantRoleToUser?: (ctx: GrantRoleToUserContext) => void;
    /**
     * Enter a parse tree produced by the `grantRoleToRole`
     * labeled alternative in `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    enterGrantRoleToRole?: (ctx: GrantRoleToRoleContext) => void;
    /**
     * Exit a parse tree produced by the `grantRoleToRole`
     * labeled alternative in `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    exitGrantRoleToRole?: (ctx: GrantRoleToRoleContext) => void;
    /**
     * Enter a parse tree produced by the `booleanExpressionDefault`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterBooleanExpressionDefault?: (ctx: BooleanExpressionDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `booleanExpressionDefault`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitBooleanExpressionDefault?: (ctx: BooleanExpressionDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `isNull`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterIsNull?: (ctx: IsNullContext) => void;
    /**
     * Exit a parse tree produced by the `isNull`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitIsNull?: (ctx: IsNullContext) => void;
    /**
     * Enter a parse tree produced by the `comparison`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterComparison?: (ctx: ComparisonContext) => void;
    /**
     * Exit a parse tree produced by the `comparison`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitComparison?: (ctx: ComparisonContext) => void;
    /**
     * Enter a parse tree produced by the `scalarSubquery`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterScalarSubquery?: (ctx: ScalarSubqueryContext) => void;
    /**
     * Exit a parse tree produced by the `scalarSubquery`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitScalarSubquery?: (ctx: ScalarSubqueryContext) => void;
    /**
     * Enter a parse tree produced by the `nullLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterNullLiteral?: (ctx: NullLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `nullLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitNullLiteral?: (ctx: NullLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `booleanLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterBooleanLiteral?: (ctx: BooleanLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `booleanLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitBooleanLiteral?: (ctx: BooleanLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `numericLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterNumericLiteral?: (ctx: NumericLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `numericLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitNumericLiteral?: (ctx: NumericLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `dateLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterDateLiteral?: (ctx: DateLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `dateLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitDateLiteral?: (ctx: DateLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `stringLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterStringLiteral?: (ctx: StringLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `stringLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitStringLiteral?: (ctx: StringLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `intervalLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterIntervalLiteral?: (ctx: IntervalLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `intervalLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitIntervalLiteral?: (ctx: IntervalLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `unitBoundaryLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterUnitBoundaryLiteral?: (ctx: UnitBoundaryLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `unitBoundaryLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitUnitBoundaryLiteral?: (ctx: UnitBoundaryLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `binaryLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterBinaryLiteral?: (ctx: BinaryLiteralContext) => void;
    /**
     * Exit a parse tree produced by the `binaryLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitBinaryLiteral?: (ctx: BinaryLiteralContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnUser`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnUser?: (ctx: GrantOnUserContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnUser`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnUser?: (ctx: GrantOnUserContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnTableBrief`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnTableBrief?: (ctx: GrantOnTableBriefContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnTableBrief`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnTableBrief?: (ctx: GrantOnTableBriefContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnFunc`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnFunc?: (ctx: GrantOnFuncContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnFunc`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnFunc?: (ctx: GrantOnFuncContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnSystem`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnSystem?: (ctx: GrantOnSystemContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnSystem`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnSystem?: (ctx: GrantOnSystemContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnPrimaryObj`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnPrimaryObj?: (ctx: GrantOnPrimaryObjContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnPrimaryObj`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnPrimaryObj?: (ctx: GrantOnPrimaryObjContext) => void;
    /**
     * Enter a parse tree produced by the `grantOnAll`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantOnAll?: (ctx: GrantOnAllContext) => void;
    /**
     * Exit a parse tree produced by the `grantOnAll`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantOnAll?: (ctx: GrantOnAllContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnUser`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnUser?: (ctx: RevokeOnUserContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnUser`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnUser?: (ctx: RevokeOnUserContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnTableBrief`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnTableBrief?: (ctx: RevokeOnTableBriefContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnTableBrief`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnTableBrief?: (ctx: RevokeOnTableBriefContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnFunc`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnFunc?: (ctx: RevokeOnFuncContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnFunc`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnFunc?: (ctx: RevokeOnFuncContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnSystem`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnSystem?: (ctx: RevokeOnSystemContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnSystem`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnSystem?: (ctx: RevokeOnSystemContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnPrimaryObj`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnPrimaryObj?: (ctx: RevokeOnPrimaryObjContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnPrimaryObj`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnPrimaryObj?: (ctx: RevokeOnPrimaryObjContext) => void;
    /**
     * Enter a parse tree produced by the `revokeOnAll`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokeOnAll?: (ctx: RevokeOnAllContext) => void;
    /**
     * Exit a parse tree produced by the `revokeOnAll`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokeOnAll?: (ctx: RevokeOnAllContext) => void;
    /**
     * Enter a parse tree produced by the `rollup`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterRollup?: (ctx: RollupContext) => void;
    /**
     * Exit a parse tree produced by the `rollup`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitRollup?: (ctx: RollupContext) => void;
    /**
     * Enter a parse tree produced by the `cube`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterCube?: (ctx: CubeContext) => void;
    /**
     * Exit a parse tree produced by the `cube`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitCube?: (ctx: CubeContext) => void;
    /**
     * Enter a parse tree produced by the `multipleGroupingSets`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterMultipleGroupingSets?: (ctx: MultipleGroupingSetsContext) => void;
    /**
     * Exit a parse tree produced by the `multipleGroupingSets`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitMultipleGroupingSets?: (ctx: MultipleGroupingSetsContext) => void;
    /**
     * Enter a parse tree produced by the `singleGroupingSet`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterSingleGroupingSet?: (ctx: SingleGroupingSetContext) => void;
    /**
     * Exit a parse tree produced by the `singleGroupingSet`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitSingleGroupingSet?: (ctx: SingleGroupingSetContext) => void;
    /**
     * Enter a parse tree produced by the `userVariableExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterUserVariableExpression?: (ctx: UserVariableExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `userVariableExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitUserVariableExpression?: (ctx: UserVariableExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `systemVariableExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSystemVariableExpression?: (ctx: SystemVariableExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `systemVariableExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSystemVariableExpression?: (ctx: SystemVariableExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `functionCallExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterFunctionCallExpression?: (ctx: FunctionCallExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `functionCallExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitFunctionCallExpression?: (ctx: FunctionCallExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `odbcFunctionCallExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterOdbcFunctionCallExpression?: (ctx: OdbcFunctionCallExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `odbcFunctionCallExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitOdbcFunctionCallExpression?: (ctx: OdbcFunctionCallExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `collate`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterCollate?: (ctx: CollateContext) => void;
    /**
     * Exit a parse tree produced by the `collate`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitCollate?: (ctx: CollateContext) => void;
    /**
     * Enter a parse tree produced by the `literal`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterLiteral?: (ctx: LiteralContext) => void;
    /**
     * Exit a parse tree produced by the `literal`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitLiteral?: (ctx: LiteralContext) => void;
    /**
     * Enter a parse tree produced by the `columnRef`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterColumnRef?: (ctx: ColumnRefContext) => void;
    /**
     * Exit a parse tree produced by the `columnRef`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitColumnRef?: (ctx: ColumnRefContext) => void;
    /**
     * Enter a parse tree produced by the `dereference`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterDereference?: (ctx: DereferenceContext) => void;
    /**
     * Exit a parse tree produced by the `dereference`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitDereference?: (ctx: DereferenceContext) => void;
    /**
     * Enter a parse tree produced by the `concat`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterConcat?: (ctx: ConcatContext) => void;
    /**
     * Exit a parse tree produced by the `concat`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitConcat?: (ctx: ConcatContext) => void;
    /**
     * Enter a parse tree produced by the `arithmeticUnary`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArithmeticUnary?: (ctx: ArithmeticUnaryContext) => void;
    /**
     * Exit a parse tree produced by the `arithmeticUnary`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArithmeticUnary?: (ctx: ArithmeticUnaryContext) => void;
    /**
     * Enter a parse tree produced by the `parenthesizedExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterParenthesizedExpression?: (ctx: ParenthesizedExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `parenthesizedExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitParenthesizedExpression?: (ctx: ParenthesizedExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `exists`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterExists?: (ctx: ExistsContext) => void;
    /**
     * Exit a parse tree produced by the `exists`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitExists?: (ctx: ExistsContext) => void;
    /**
     * Enter a parse tree produced by the `subqueryExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSubqueryExpression?: (ctx: SubqueryExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `subqueryExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSubqueryExpression?: (ctx: SubqueryExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `cast`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterCast?: (ctx: CastContext) => void;
    /**
     * Exit a parse tree produced by the `cast`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitCast?: (ctx: CastContext) => void;
    /**
     * Enter a parse tree produced by the `convert`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterConvert?: (ctx: ConvertContext) => void;
    /**
     * Exit a parse tree produced by the `convert`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitConvert?: (ctx: ConvertContext) => void;
    /**
     * Enter a parse tree produced by the `simpleCase`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSimpleCase?: (ctx: SimpleCaseContext) => void;
    /**
     * Exit a parse tree produced by the `simpleCase`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSimpleCase?: (ctx: SimpleCaseContext) => void;
    /**
     * Enter a parse tree produced by the `searchedCase`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterSearchedCase?: (ctx: SearchedCaseContext) => void;
    /**
     * Exit a parse tree produced by the `searchedCase`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitSearchedCase?: (ctx: SearchedCaseContext) => void;
    /**
     * Enter a parse tree produced by the `arrayConstructor`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArrayConstructor?: (ctx: ArrayConstructorContext) => void;
    /**
     * Exit a parse tree produced by the `arrayConstructor`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArrayConstructor?: (ctx: ArrayConstructorContext) => void;
    /**
     * Enter a parse tree produced by the `mapConstructor`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterMapConstructor?: (ctx: MapConstructorContext) => void;
    /**
     * Exit a parse tree produced by the `mapConstructor`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitMapConstructor?: (ctx: MapConstructorContext) => void;
    /**
     * Enter a parse tree produced by the `collectionSubscript`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterCollectionSubscript?: (ctx: CollectionSubscriptContext) => void;
    /**
     * Exit a parse tree produced by the `collectionSubscript`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitCollectionSubscript?: (ctx: CollectionSubscriptContext) => void;
    /**
     * Enter a parse tree produced by the `arraySlice`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArraySlice?: (ctx: ArraySliceContext) => void;
    /**
     * Exit a parse tree produced by the `arraySlice`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArraySlice?: (ctx: ArraySliceContext) => void;
    /**
     * Enter a parse tree produced by the `arrowExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterArrowExpression?: (ctx: ArrowExpressionContext) => void;
    /**
     * Exit a parse tree produced by the `arrowExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitArrowExpression?: (ctx: ArrowExpressionContext) => void;
    /**
     * Enter a parse tree produced by the `lambdaFunctionExpr`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterLambdaFunctionExpr?: (ctx: LambdaFunctionExprContext) => void;
    /**
     * Exit a parse tree produced by the `lambdaFunctionExpr`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitLambdaFunctionExpr?: (ctx: LambdaFunctionExprContext) => void;
    /**
     * Enter a parse tree produced by the `expressionDefault`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    enterExpressionDefault?: (ctx: ExpressionDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `expressionDefault`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    exitExpressionDefault?: (ctx: ExpressionDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `logicalNot`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    enterLogicalNot?: (ctx: LogicalNotContext) => void;
    /**
     * Exit a parse tree produced by the `logicalNot`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    exitLogicalNot?: (ctx: LogicalNotContext) => void;
    /**
     * Enter a parse tree produced by the `logicalBinary`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    enterLogicalBinary?: (ctx: LogicalBinaryContext) => void;
    /**
     * Exit a parse tree produced by the `logicalBinary`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    exitLogicalBinary?: (ctx: LogicalBinaryContext) => void;
    /**
     * Enter a parse tree produced by the `showAllAuthentication`
     * labeled alternative in `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    enterShowAllAuthentication?: (ctx: ShowAllAuthenticationContext) => void;
    /**
     * Exit a parse tree produced by the `showAllAuthentication`
     * labeled alternative in `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    exitShowAllAuthentication?: (ctx: ShowAllAuthenticationContext) => void;
    /**
     * Enter a parse tree produced by the `showAuthenticationForUser`
     * labeled alternative in `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    enterShowAuthenticationForUser?: (ctx: ShowAuthenticationForUserContext) => void;
    /**
     * Exit a parse tree produced by the `showAuthenticationForUser`
     * labeled alternative in `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    exitShowAuthenticationForUser?: (ctx: ShowAuthenticationForUserContext) => void;
    /**
     * Enter a parse tree produced by the `unquotedIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    enterUnquotedIdentifier?: (ctx: UnquotedIdentifierContext) => void;
    /**
     * Exit a parse tree produced by the `unquotedIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    exitUnquotedIdentifier?: (ctx: UnquotedIdentifierContext) => void;
    /**
     * Enter a parse tree produced by the `digitIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    enterDigitIdentifier?: (ctx: DigitIdentifierContext) => void;
    /**
     * Exit a parse tree produced by the `digitIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    exitDigitIdentifier?: (ctx: DigitIdentifierContext) => void;
    /**
     * Enter a parse tree produced by the `backQuotedIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    enterBackQuotedIdentifier?: (ctx: BackQuotedIdentifierContext) => void;
    /**
     * Exit a parse tree produced by the `backQuotedIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    exitBackQuotedIdentifier?: (ctx: BackQuotedIdentifierContext) => void;
    /**
     * Enter a parse tree produced by the `valueExpressionDefault`
     * labeled alternative in `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     */
    enterValueExpressionDefault?: (ctx: ValueExpressionDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `valueExpressionDefault`
     * labeled alternative in `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     */
    exitValueExpressionDefault?: (ctx: ValueExpressionDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `arithmeticBinary`
     * labeled alternative in `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     */
    enterArithmeticBinary?: (ctx: ArithmeticBinaryContext) => void;
    /**
     * Exit a parse tree produced by the `arithmeticBinary`
     * labeled alternative in `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     */
    exitArithmeticBinary?: (ctx: ArithmeticBinaryContext) => void;
    /**
     * Enter a parse tree produced by the `queryPrimaryDefault`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterQueryPrimaryDefault?: (ctx: QueryPrimaryDefaultContext) => void;
    /**
     * Exit a parse tree produced by the `queryPrimaryDefault`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitQueryPrimaryDefault?: (ctx: QueryPrimaryDefaultContext) => void;
    /**
     * Enter a parse tree produced by the `queryWithParentheses`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterQueryWithParentheses?: (ctx: QueryWithParenthesesContext) => void;
    /**
     * Exit a parse tree produced by the `queryWithParentheses`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitQueryWithParentheses?: (ctx: QueryWithParenthesesContext) => void;
    /**
     * Enter a parse tree produced by the `setOperation`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterSetOperation?: (ctx: SetOperationContext) => void;
    /**
     * Exit a parse tree produced by the `setOperation`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitSetOperation?: (ctx: SetOperationContext) => void;
    /**
     * Enter a parse tree produced by the `selectSingle`
     * labeled alternative in `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     */
    enterSelectSingle?: (ctx: SelectSingleContext) => void;
    /**
     * Exit a parse tree produced by the `selectSingle`
     * labeled alternative in `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     */
    exitSelectSingle?: (ctx: SelectSingleContext) => void;
    /**
     * Enter a parse tree produced by the `selectAll`
     * labeled alternative in `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     */
    enterSelectAll?: (ctx: SelectAllContext) => void;
    /**
     * Exit a parse tree produced by the `selectAll`
     * labeled alternative in `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     */
    exitSelectAll?: (ctx: SelectAllContext) => void;
    /**
     * Enter a parse tree produced by the `revokeRoleFromUser`
     * labeled alternative in `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    enterRevokeRoleFromUser?: (ctx: RevokeRoleFromUserContext) => void;
    /**
     * Exit a parse tree produced by the `revokeRoleFromUser`
     * labeled alternative in `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    exitRevokeRoleFromUser?: (ctx: RevokeRoleFromUserContext) => void;
    /**
     * Enter a parse tree produced by the `revokeRoleFromRole`
     * labeled alternative in `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    enterRevokeRoleFromRole?: (ctx: RevokeRoleFromRoleContext) => void;
    /**
     * Exit a parse tree produced by the `revokeRoleFromRole`
     * labeled alternative in `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    exitRevokeRoleFromRole?: (ctx: RevokeRoleFromRoleContext) => void;
    /**
     * Enter a parse tree produced by the `authWithoutPlugin`
     * labeled alternative in `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     */
    enterAuthWithoutPlugin?: (ctx: AuthWithoutPluginContext) => void;
    /**
     * Exit a parse tree produced by the `authWithoutPlugin`
     * labeled alternative in `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     */
    exitAuthWithoutPlugin?: (ctx: AuthWithoutPluginContext) => void;
    /**
     * Enter a parse tree produced by the `authWithPlugin`
     * labeled alternative in `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     */
    enterAuthWithPlugin?: (ctx: AuthWithPluginContext) => void;
    /**
     * Exit a parse tree produced by the `authWithPlugin`
     * labeled alternative in `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     */
    exitAuthWithPlugin?: (ctx: AuthWithPluginContext) => void;
    /**
     * Enter a parse tree produced by the `userWithoutHost`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    enterUserWithoutHost?: (ctx: UserWithoutHostContext) => void;
    /**
     * Exit a parse tree produced by the `userWithoutHost`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    exitUserWithoutHost?: (ctx: UserWithoutHostContext) => void;
    /**
     * Enter a parse tree produced by the `userWithHost`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    enterUserWithHost?: (ctx: UserWithHostContext) => void;
    /**
     * Exit a parse tree produced by the `userWithHost`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    exitUserWithHost?: (ctx: UserWithHostContext) => void;
    /**
     * Enter a parse tree produced by the `userWithHostAndBlanket`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    enterUserWithHostAndBlanket?: (ctx: UserWithHostAndBlanketContext) => void;
    /**
     * Exit a parse tree produced by the `userWithHostAndBlanket`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    exitUserWithHostAndBlanket?: (ctx: UserWithHostAndBlanketContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.program`.
     * @param ctx the parse tree
     */
    enterProgram?: (ctx: ProgramContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.program`.
     * @param ctx the parse tree
     */
    exitProgram?: (ctx: ProgramContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.sqlStatements`.
     * @param ctx the parse tree
     */
    enterSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.sqlStatements`.
     * @param ctx the parse tree
     */
    exitSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.singleStatement`.
     * @param ctx the parse tree
     */
    enterSingleStatement?: (ctx: SingleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.singleStatement`.
     * @param ctx the parse tree
     */
    exitSingleStatement?: (ctx: SingleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.statement`.
     * @param ctx the parse tree
     */
    enterStatement?: (ctx: StatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.statement`.
     * @param ctx the parse tree
     */
    exitStatement?: (ctx: StatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.useDatabaseStatement`.
     * @param ctx the parse tree
     */
    enterUseDatabaseStatement?: (ctx: UseDatabaseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.useDatabaseStatement`.
     * @param ctx the parse tree
     */
    exitUseDatabaseStatement?: (ctx: UseDatabaseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.useCatalogStatement`.
     * @param ctx the parse tree
     */
    enterUseCatalogStatement?: (ctx: UseCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.useCatalogStatement`.
     * @param ctx the parse tree
     */
    exitUseCatalogStatement?: (ctx: UseCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setCatalogStatement`.
     * @param ctx the parse tree
     */
    enterSetCatalogStatement?: (ctx: SetCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setCatalogStatement`.
     * @param ctx the parse tree
     */
    exitSetCatalogStatement?: (ctx: SetCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showDatabasesStatement`.
     * @param ctx the parse tree
     */
    enterShowDatabasesStatement?: (ctx: ShowDatabasesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showDatabasesStatement`.
     * @param ctx the parse tree
     */
    exitShowDatabasesStatement?: (ctx: ShowDatabasesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterDbQuotaStatement`.
     * @param ctx the parse tree
     */
    enterAlterDbQuotaStatement?: (ctx: AlterDbQuotaStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterDbQuotaStatement`.
     * @param ctx the parse tree
     */
    exitAlterDbQuotaStatement?: (ctx: AlterDbQuotaStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createDbStatement`.
     * @param ctx the parse tree
     */
    enterCreateDbStatement?: (ctx: CreateDbStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createDbStatement`.
     * @param ctx the parse tree
     */
    exitCreateDbStatement?: (ctx: CreateDbStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropDbStatement`.
     * @param ctx the parse tree
     */
    enterDropDbStatement?: (ctx: DropDbStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropDbStatement`.
     * @param ctx the parse tree
     */
    exitDropDbStatement?: (ctx: DropDbStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCreateDbStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateDbStatement?: (ctx: ShowCreateDbStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCreateDbStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateDbStatement?: (ctx: ShowCreateDbStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterDatabaseRenameStatement`.
     * @param ctx the parse tree
     */
    enterAlterDatabaseRenameStatement?: (ctx: AlterDatabaseRenameStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterDatabaseRenameStatement`.
     * @param ctx the parse tree
     */
    exitAlterDatabaseRenameStatement?: (ctx: AlterDatabaseRenameStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.recoverDbStmt`.
     * @param ctx the parse tree
     */
    enterRecoverDbStmt?: (ctx: RecoverDbStmtContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.recoverDbStmt`.
     * @param ctx the parse tree
     */
    exitRecoverDbStmt?: (ctx: RecoverDbStmtContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showDataStmt`.
     * @param ctx the parse tree
     */
    enterShowDataStmt?: (ctx: ShowDataStmtContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showDataStmt`.
     * @param ctx the parse tree
     */
    exitShowDataStmt?: (ctx: ShowDataStmtContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createTableStatement`.
     * @param ctx the parse tree
     */
    enterCreateTableStatement?: (ctx: CreateTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createTableStatement`.
     * @param ctx the parse tree
     */
    exitCreateTableStatement?: (ctx: CreateTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.columnDesc`.
     * @param ctx the parse tree
     */
    enterColumnDesc?: (ctx: ColumnDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.columnDesc`.
     * @param ctx the parse tree
     */
    exitColumnDesc?: (ctx: ColumnDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.charsetName`.
     * @param ctx the parse tree
     */
    enterCharsetName?: (ctx: CharsetNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.charsetName`.
     * @param ctx the parse tree
     */
    exitCharsetName?: (ctx: CharsetNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.defaultDesc`.
     * @param ctx the parse tree
     */
    enterDefaultDesc?: (ctx: DefaultDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.defaultDesc`.
     * @param ctx the parse tree
     */
    exitDefaultDesc?: (ctx: DefaultDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.materializedColumnDesc`.
     * @param ctx the parse tree
     */
    enterMaterializedColumnDesc?: (ctx: MaterializedColumnDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.materializedColumnDesc`.
     * @param ctx the parse tree
     */
    exitMaterializedColumnDesc?: (ctx: MaterializedColumnDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.indexDesc`.
     * @param ctx the parse tree
     */
    enterIndexDesc?: (ctx: IndexDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.indexDesc`.
     * @param ctx the parse tree
     */
    exitIndexDesc?: (ctx: IndexDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.engineDesc`.
     * @param ctx the parse tree
     */
    enterEngineDesc?: (ctx: EngineDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.engineDesc`.
     * @param ctx the parse tree
     */
    exitEngineDesc?: (ctx: EngineDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.charsetDesc`.
     * @param ctx the parse tree
     */
    enterCharsetDesc?: (ctx: CharsetDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.charsetDesc`.
     * @param ctx the parse tree
     */
    exitCharsetDesc?: (ctx: CharsetDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.collateDesc`.
     * @param ctx the parse tree
     */
    enterCollateDesc?: (ctx: CollateDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.collateDesc`.
     * @param ctx the parse tree
     */
    exitCollateDesc?: (ctx: CollateDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.keyDesc`.
     * @param ctx the parse tree
     */
    enterKeyDesc?: (ctx: KeyDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.keyDesc`.
     * @param ctx the parse tree
     */
    exitKeyDesc?: (ctx: KeyDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.orderByDesc`.
     * @param ctx the parse tree
     */
    enterOrderByDesc?: (ctx: OrderByDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.orderByDesc`.
     * @param ctx the parse tree
     */
    exitOrderByDesc?: (ctx: OrderByDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.aggDesc`.
     * @param ctx the parse tree
     */
    enterAggDesc?: (ctx: AggDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.aggDesc`.
     * @param ctx the parse tree
     */
    exitAggDesc?: (ctx: AggDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.rollupDesc`.
     * @param ctx the parse tree
     */
    enterRollupDesc?: (ctx: RollupDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.rollupDesc`.
     * @param ctx the parse tree
     */
    exitRollupDesc?: (ctx: RollupDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.rollupItem`.
     * @param ctx the parse tree
     */
    enterRollupItem?: (ctx: RollupItemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.rollupItem`.
     * @param ctx the parse tree
     */
    exitRollupItem?: (ctx: RollupItemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dupKeys`.
     * @param ctx the parse tree
     */
    enterDupKeys?: (ctx: DupKeysContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dupKeys`.
     * @param ctx the parse tree
     */
    exitDupKeys?: (ctx: DupKeysContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.fromRollup`.
     * @param ctx the parse tree
     */
    enterFromRollup?: (ctx: FromRollupContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.fromRollup`.
     * @param ctx the parse tree
     */
    exitFromRollup?: (ctx: FromRollupContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.withMaskingPolicy`.
     * @param ctx the parse tree
     */
    enterWithMaskingPolicy?: (ctx: WithMaskingPolicyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.withMaskingPolicy`.
     * @param ctx the parse tree
     */
    exitWithMaskingPolicy?: (ctx: WithMaskingPolicyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.withRowAccessPolicy`.
     * @param ctx the parse tree
     */
    enterWithRowAccessPolicy?: (ctx: WithRowAccessPolicyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.withRowAccessPolicy`.
     * @param ctx the parse tree
     */
    exitWithRowAccessPolicy?: (ctx: WithRowAccessPolicyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createTemporaryTableStatement`.
     * @param ctx the parse tree
     */
    enterCreateTemporaryTableStatement?: (ctx: CreateTemporaryTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createTemporaryTableStatement`.
     * @param ctx the parse tree
     */
    exitCreateTemporaryTableStatement?: (ctx: CreateTemporaryTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createTableAsSelectStatement`.
     * @param ctx the parse tree
     */
    enterCreateTableAsSelectStatement?: (ctx: CreateTableAsSelectStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createTableAsSelectStatement`.
     * @param ctx the parse tree
     */
    exitCreateTableAsSelectStatement?: (ctx: CreateTableAsSelectStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropTableStatement`.
     * @param ctx the parse tree
     */
    enterDropTableStatement?: (ctx: DropTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropTableStatement`.
     * @param ctx the parse tree
     */
    exitDropTableStatement?: (ctx: DropTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterTableStatement`.
     * @param ctx the parse tree
     */
    enterAlterTableStatement?: (ctx: AlterTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterTableStatement`.
     * @param ctx the parse tree
     */
    exitAlterTableStatement?: (ctx: AlterTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createIndexStatement`.
     * @param ctx the parse tree
     */
    enterCreateIndexStatement?: (ctx: CreateIndexStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createIndexStatement`.
     * @param ctx the parse tree
     */
    exitCreateIndexStatement?: (ctx: CreateIndexStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropIndexStatement`.
     * @param ctx the parse tree
     */
    enterDropIndexStatement?: (ctx: DropIndexStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropIndexStatement`.
     * @param ctx the parse tree
     */
    exitDropIndexStatement?: (ctx: DropIndexStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.indexType`.
     * @param ctx the parse tree
     */
    enterIndexType?: (ctx: IndexTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.indexType`.
     * @param ctx the parse tree
     */
    exitIndexType?: (ctx: IndexTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showTableStatement`.
     * @param ctx the parse tree
     */
    enterShowTableStatement?: (ctx: ShowTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showTableStatement`.
     * @param ctx the parse tree
     */
    exitShowTableStatement?: (ctx: ShowTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCreateTableStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateTableStatement?: (ctx: ShowCreateTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCreateTableStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateTableStatement?: (ctx: ShowCreateTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showColumnStatement`.
     * @param ctx the parse tree
     */
    enterShowColumnStatement?: (ctx: ShowColumnStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showColumnStatement`.
     * @param ctx the parse tree
     */
    exitShowColumnStatement?: (ctx: ShowColumnStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showTableStatusStatement`.
     * @param ctx the parse tree
     */
    enterShowTableStatusStatement?: (ctx: ShowTableStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showTableStatusStatement`.
     * @param ctx the parse tree
     */
    exitShowTableStatusStatement?: (ctx: ShowTableStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.refreshTableStatement`.
     * @param ctx the parse tree
     */
    enterRefreshTableStatement?: (ctx: RefreshTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.refreshTableStatement`.
     * @param ctx the parse tree
     */
    exitRefreshTableStatement?: (ctx: RefreshTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showAlterStatement`.
     * @param ctx the parse tree
     */
    enterShowAlterStatement?: (ctx: ShowAlterStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showAlterStatement`.
     * @param ctx the parse tree
     */
    exitShowAlterStatement?: (ctx: ShowAlterStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.descTableStatement`.
     * @param ctx the parse tree
     */
    enterDescTableStatement?: (ctx: DescTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.descTableStatement`.
     * @param ctx the parse tree
     */
    exitDescTableStatement?: (ctx: DescTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createTableLikeStatement`.
     * @param ctx the parse tree
     */
    enterCreateTableLikeStatement?: (ctx: CreateTableLikeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createTableLikeStatement`.
     * @param ctx the parse tree
     */
    exitCreateTableLikeStatement?: (ctx: CreateTableLikeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showIndexStatement`.
     * @param ctx the parse tree
     */
    enterShowIndexStatement?: (ctx: ShowIndexStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showIndexStatement`.
     * @param ctx the parse tree
     */
    exitShowIndexStatement?: (ctx: ShowIndexStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.recoverTableStatement`.
     * @param ctx the parse tree
     */
    enterRecoverTableStatement?: (ctx: RecoverTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.recoverTableStatement`.
     * @param ctx the parse tree
     */
    exitRecoverTableStatement?: (ctx: RecoverTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.truncateTableStatement`.
     * @param ctx the parse tree
     */
    enterTruncateTableStatement?: (ctx: TruncateTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.truncateTableStatement`.
     * @param ctx the parse tree
     */
    exitTruncateTableStatement?: (ctx: TruncateTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelAlterTableStatement`.
     * @param ctx the parse tree
     */
    enterCancelAlterTableStatement?: (ctx: CancelAlterTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelAlterTableStatement`.
     * @param ctx the parse tree
     */
    exitCancelAlterTableStatement?: (ctx: CancelAlterTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showPartitionsStatement`.
     * @param ctx the parse tree
     */
    enterShowPartitionsStatement?: (ctx: ShowPartitionsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showPartitionsStatement`.
     * @param ctx the parse tree
     */
    exitShowPartitionsStatement?: (ctx: ShowPartitionsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.recoverPartitionStatement`.
     * @param ctx the parse tree
     */
    enterRecoverPartitionStatement?: (ctx: RecoverPartitionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.recoverPartitionStatement`.
     * @param ctx the parse tree
     */
    exitRecoverPartitionStatement?: (ctx: RecoverPartitionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createViewStatement`.
     * @param ctx the parse tree
     */
    enterCreateViewStatement?: (ctx: CreateViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createViewStatement`.
     * @param ctx the parse tree
     */
    exitCreateViewStatement?: (ctx: CreateViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterViewStatement`.
     * @param ctx the parse tree
     */
    enterAlterViewStatement?: (ctx: AlterViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterViewStatement`.
     * @param ctx the parse tree
     */
    exitAlterViewStatement?: (ctx: AlterViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropViewStatement`.
     * @param ctx the parse tree
     */
    enterDropViewStatement?: (ctx: DropViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropViewStatement`.
     * @param ctx the parse tree
     */
    exitDropViewStatement?: (ctx: DropViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.columnNameWithComment`.
     * @param ctx the parse tree
     */
    enterColumnNameWithComment?: (ctx: ColumnNameWithCommentContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.columnNameWithComment`.
     * @param ctx the parse tree
     */
    exitColumnNameWithComment?: (ctx: ColumnNameWithCommentContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.submitTaskStatement`.
     * @param ctx the parse tree
     */
    enterSubmitTaskStatement?: (ctx: SubmitTaskStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.submitTaskStatement`.
     * @param ctx the parse tree
     */
    exitSubmitTaskStatement?: (ctx: SubmitTaskStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.taskExecSql`.
     * @param ctx the parse tree
     */
    enterTaskExecSql?: (ctx: TaskExecSqlContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.taskExecSql`.
     * @param ctx the parse tree
     */
    exitTaskExecSql?: (ctx: TaskExecSqlContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropTaskStatement`.
     * @param ctx the parse tree
     */
    enterDropTaskStatement?: (ctx: DropTaskStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropTaskStatement`.
     * @param ctx the parse tree
     */
    exitDropTaskStatement?: (ctx: DropTaskStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterCreateMaterializedViewStatement?: (ctx: CreateMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitCreateMaterializedViewStatement?: (ctx: CreateMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.materializedViewDesc`.
     * @param ctx the parse tree
     */
    enterMaterializedViewDesc?: (ctx: MaterializedViewDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.materializedViewDesc`.
     * @param ctx the parse tree
     */
    exitMaterializedViewDesc?: (ctx: MaterializedViewDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showMaterializedViewsStatement`.
     * @param ctx the parse tree
     */
    enterShowMaterializedViewsStatement?: (ctx: ShowMaterializedViewsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showMaterializedViewsStatement`.
     * @param ctx the parse tree
     */
    exitShowMaterializedViewsStatement?: (ctx: ShowMaterializedViewsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterDropMaterializedViewStatement?: (ctx: DropMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitDropMaterializedViewStatement?: (ctx: DropMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterAlterMaterializedViewStatement?: (ctx: AlterMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitAlterMaterializedViewStatement?: (ctx: AlterMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.refreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterRefreshMaterializedViewStatement?: (ctx: RefreshMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.refreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitRefreshMaterializedViewStatement?: (ctx: RefreshMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelRefreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    enterCancelRefreshMaterializedViewStatement?: (ctx: CancelRefreshMaterializedViewStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelRefreshMaterializedViewStatement`.
     * @param ctx the parse tree
     */
    exitCancelRefreshMaterializedViewStatement?: (ctx: CancelRefreshMaterializedViewStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminSetConfigStatement`.
     * @param ctx the parse tree
     */
    enterAdminSetConfigStatement?: (ctx: AdminSetConfigStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminSetConfigStatement`.
     * @param ctx the parse tree
     */
    exitAdminSetConfigStatement?: (ctx: AdminSetConfigStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminSetReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    enterAdminSetReplicaStatusStatement?: (ctx: AdminSetReplicaStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminSetReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    exitAdminSetReplicaStatusStatement?: (ctx: AdminSetReplicaStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminShowConfigStatement`.
     * @param ctx the parse tree
     */
    enterAdminShowConfigStatement?: (ctx: AdminShowConfigStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminShowConfigStatement`.
     * @param ctx the parse tree
     */
    exitAdminShowConfigStatement?: (ctx: AdminShowConfigStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminShowReplicaDistributionStatement`.
     * @param ctx the parse tree
     */
    enterAdminShowReplicaDistributionStatement?: (ctx: AdminShowReplicaDistributionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminShowReplicaDistributionStatement`.
     * @param ctx the parse tree
     */
    exitAdminShowReplicaDistributionStatement?: (ctx: AdminShowReplicaDistributionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminShowReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    enterAdminShowReplicaStatusStatement?: (ctx: AdminShowReplicaStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminShowReplicaStatusStatement`.
     * @param ctx the parse tree
     */
    exitAdminShowReplicaStatusStatement?: (ctx: AdminShowReplicaStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminRepairTableStatement`.
     * @param ctx the parse tree
     */
    enterAdminRepairTableStatement?: (ctx: AdminRepairTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminRepairTableStatement`.
     * @param ctx the parse tree
     */
    exitAdminRepairTableStatement?: (ctx: AdminRepairTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminCancelRepairTableStatement`.
     * @param ctx the parse tree
     */
    enterAdminCancelRepairTableStatement?: (ctx: AdminCancelRepairTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminCancelRepairTableStatement`.
     * @param ctx the parse tree
     */
    exitAdminCancelRepairTableStatement?: (ctx: AdminCancelRepairTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.adminCheckTabletsStatement`.
     * @param ctx the parse tree
     */
    enterAdminCheckTabletsStatement?: (ctx: AdminCheckTabletsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.adminCheckTabletsStatement`.
     * @param ctx the parse tree
     */
    exitAdminCheckTabletsStatement?: (ctx: AdminCheckTabletsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.killStatement`.
     * @param ctx the parse tree
     */
    enterKillStatement?: (ctx: KillStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.killStatement`.
     * @param ctx the parse tree
     */
    exitKillStatement?: (ctx: KillStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.syncStatement`.
     * @param ctx the parse tree
     */
    enterSyncStatement?: (ctx: SyncStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.syncStatement`.
     * @param ctx the parse tree
     */
    exitSyncStatement?: (ctx: SyncStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterSystemStatement`.
     * @param ctx the parse tree
     */
    enterAlterSystemStatement?: (ctx: AlterSystemStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterSystemStatement`.
     * @param ctx the parse tree
     */
    exitAlterSystemStatement?: (ctx: AlterSystemStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelAlterSystemStatement`.
     * @param ctx the parse tree
     */
    enterCancelAlterSystemStatement?: (ctx: CancelAlterSystemStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelAlterSystemStatement`.
     * @param ctx the parse tree
     */
    exitCancelAlterSystemStatement?: (ctx: CancelAlterSystemStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showComputeNodesStatement`.
     * @param ctx the parse tree
     */
    enterShowComputeNodesStatement?: (ctx: ShowComputeNodesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showComputeNodesStatement`.
     * @param ctx the parse tree
     */
    exitShowComputeNodesStatement?: (ctx: ShowComputeNodesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    enterCreateExternalCatalogStatement?: (ctx: CreateExternalCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    exitCreateExternalCatalogStatement?: (ctx: CreateExternalCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCreateExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateExternalCatalogStatement?: (ctx: ShowCreateExternalCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCreateExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateExternalCatalogStatement?: (ctx: ShowCreateExternalCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    enterDropExternalCatalogStatement?: (ctx: DropExternalCatalogStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropExternalCatalogStatement`.
     * @param ctx the parse tree
     */
    exitDropExternalCatalogStatement?: (ctx: DropExternalCatalogStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCatalogsStatement`.
     * @param ctx the parse tree
     */
    enterShowCatalogsStatement?: (ctx: ShowCatalogsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCatalogsStatement`.
     * @param ctx the parse tree
     */
    exitShowCatalogsStatement?: (ctx: ShowCatalogsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterCreateWarehouseStatement?: (ctx: CreateWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitCreateWarehouseStatement?: (ctx: CreateWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showWarehousesStatement`.
     * @param ctx the parse tree
     */
    enterShowWarehousesStatement?: (ctx: ShowWarehousesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showWarehousesStatement`.
     * @param ctx the parse tree
     */
    exitShowWarehousesStatement?: (ctx: ShowWarehousesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterDropWarehouseStatement?: (ctx: DropWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitDropWarehouseStatement?: (ctx: DropWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterAlterWarehouseStatement?: (ctx: AlterWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitAlterWarehouseStatement?: (ctx: AlterWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showClustersStatement`.
     * @param ctx the parse tree
     */
    enterShowClustersStatement?: (ctx: ShowClustersStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showClustersStatement`.
     * @param ctx the parse tree
     */
    exitShowClustersStatement?: (ctx: ShowClustersStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.suspendWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterSuspendWarehouseStatement?: (ctx: SuspendWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.suspendWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitSuspendWarehouseStatement?: (ctx: SuspendWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.resumeWarehouseStatement`.
     * @param ctx the parse tree
     */
    enterResumeWarehouseStatement?: (ctx: ResumeWarehouseStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.resumeWarehouseStatement`.
     * @param ctx the parse tree
     */
    exitResumeWarehouseStatement?: (ctx: ResumeWarehouseStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterCreateStorageVolumeStatement?: (ctx: CreateStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitCreateStorageVolumeStatement?: (ctx: CreateStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.typeDesc`.
     * @param ctx the parse tree
     */
    enterTypeDesc?: (ctx: TypeDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.typeDesc`.
     * @param ctx the parse tree
     */
    exitTypeDesc?: (ctx: TypeDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.locationsDesc`.
     * @param ctx the parse tree
     */
    enterLocationsDesc?: (ctx: LocationsDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.locationsDesc`.
     * @param ctx the parse tree
     */
    exitLocationsDesc?: (ctx: LocationsDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showStorageVolumesStatement`.
     * @param ctx the parse tree
     */
    enterShowStorageVolumesStatement?: (ctx: ShowStorageVolumesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showStorageVolumesStatement`.
     * @param ctx the parse tree
     */
    exitShowStorageVolumesStatement?: (ctx: ShowStorageVolumesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterDropStorageVolumeStatement?: (ctx: DropStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitDropStorageVolumeStatement?: (ctx: DropStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterAlterStorageVolumeStatement?: (ctx: AlterStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitAlterStorageVolumeStatement?: (ctx: AlterStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterStorageVolumeClause`.
     * @param ctx the parse tree
     */
    enterAlterStorageVolumeClause?: (ctx: AlterStorageVolumeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterStorageVolumeClause`.
     * @param ctx the parse tree
     */
    exitAlterStorageVolumeClause?: (ctx: AlterStorageVolumeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyStorageVolumePropertiesClause`.
     * @param ctx the parse tree
     */
    enterModifyStorageVolumePropertiesClause?: (ctx: ModifyStorageVolumePropertiesClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyStorageVolumePropertiesClause`.
     * @param ctx the parse tree
     */
    exitModifyStorageVolumePropertiesClause?: (ctx: ModifyStorageVolumePropertiesClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyStorageVolumeCommentClause`.
     * @param ctx the parse tree
     */
    enterModifyStorageVolumeCommentClause?: (ctx: ModifyStorageVolumeCommentClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyStorageVolumeCommentClause`.
     * @param ctx the parse tree
     */
    exitModifyStorageVolumeCommentClause?: (ctx: ModifyStorageVolumeCommentClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.descStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterDescStorageVolumeStatement?: (ctx: DescStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.descStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitDescStorageVolumeStatement?: (ctx: DescStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setDefaultStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    enterSetDefaultStorageVolumeStatement?: (ctx: SetDefaultStorageVolumeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setDefaultStorageVolumeStatement`.
     * @param ctx the parse tree
     */
    exitSetDefaultStorageVolumeStatement?: (ctx: SetDefaultStorageVolumeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterClause`.
     * @param ctx the parse tree
     */
    enterAlterClause?: (ctx: AlterClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterClause`.
     * @param ctx the parse tree
     */
    exitAlterClause?: (ctx: AlterClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.addFrontendClause`.
     * @param ctx the parse tree
     */
    enterAddFrontendClause?: (ctx: AddFrontendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.addFrontendClause`.
     * @param ctx the parse tree
     */
    exitAddFrontendClause?: (ctx: AddFrontendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropFrontendClause`.
     * @param ctx the parse tree
     */
    enterDropFrontendClause?: (ctx: DropFrontendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropFrontendClause`.
     * @param ctx the parse tree
     */
    exitDropFrontendClause?: (ctx: DropFrontendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyFrontendHostClause`.
     * @param ctx the parse tree
     */
    enterModifyFrontendHostClause?: (ctx: ModifyFrontendHostClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyFrontendHostClause`.
     * @param ctx the parse tree
     */
    exitModifyFrontendHostClause?: (ctx: ModifyFrontendHostClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.addBackendClause`.
     * @param ctx the parse tree
     */
    enterAddBackendClause?: (ctx: AddBackendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.addBackendClause`.
     * @param ctx the parse tree
     */
    exitAddBackendClause?: (ctx: AddBackendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropBackendClause`.
     * @param ctx the parse tree
     */
    enterDropBackendClause?: (ctx: DropBackendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropBackendClause`.
     * @param ctx the parse tree
     */
    exitDropBackendClause?: (ctx: DropBackendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.decommissionBackendClause`.
     * @param ctx the parse tree
     */
    enterDecommissionBackendClause?: (ctx: DecommissionBackendClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.decommissionBackendClause`.
     * @param ctx the parse tree
     */
    exitDecommissionBackendClause?: (ctx: DecommissionBackendClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyBackendHostClause`.
     * @param ctx the parse tree
     */
    enterModifyBackendHostClause?: (ctx: ModifyBackendHostClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyBackendHostClause`.
     * @param ctx the parse tree
     */
    exitModifyBackendHostClause?: (ctx: ModifyBackendHostClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.addComputeNodeClause`.
     * @param ctx the parse tree
     */
    enterAddComputeNodeClause?: (ctx: AddComputeNodeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.addComputeNodeClause`.
     * @param ctx the parse tree
     */
    exitAddComputeNodeClause?: (ctx: AddComputeNodeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropComputeNodeClause`.
     * @param ctx the parse tree
     */
    enterDropComputeNodeClause?: (ctx: DropComputeNodeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropComputeNodeClause`.
     * @param ctx the parse tree
     */
    exitDropComputeNodeClause?: (ctx: DropComputeNodeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyBrokerClause`.
     * @param ctx the parse tree
     */
    enterModifyBrokerClause?: (ctx: ModifyBrokerClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyBrokerClause`.
     * @param ctx the parse tree
     */
    exitModifyBrokerClause?: (ctx: ModifyBrokerClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterLoadErrorUrlClause`.
     * @param ctx the parse tree
     */
    enterAlterLoadErrorUrlClause?: (ctx: AlterLoadErrorUrlClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterLoadErrorUrlClause`.
     * @param ctx the parse tree
     */
    exitAlterLoadErrorUrlClause?: (ctx: AlterLoadErrorUrlClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createImageClause`.
     * @param ctx the parse tree
     */
    enterCreateImageClause?: (ctx: CreateImageClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createImageClause`.
     * @param ctx the parse tree
     */
    exitCreateImageClause?: (ctx: CreateImageClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cleanTabletSchedQClause`.
     * @param ctx the parse tree
     */
    enterCleanTabletSchedQClause?: (ctx: CleanTabletSchedQClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cleanTabletSchedQClause`.
     * @param ctx the parse tree
     */
    exitCleanTabletSchedQClause?: (ctx: CleanTabletSchedQClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createIndexClause`.
     * @param ctx the parse tree
     */
    enterCreateIndexClause?: (ctx: CreateIndexClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createIndexClause`.
     * @param ctx the parse tree
     */
    exitCreateIndexClause?: (ctx: CreateIndexClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropIndexClause`.
     * @param ctx the parse tree
     */
    enterDropIndexClause?: (ctx: DropIndexClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropIndexClause`.
     * @param ctx the parse tree
     */
    exitDropIndexClause?: (ctx: DropIndexClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.tableRenameClause`.
     * @param ctx the parse tree
     */
    enterTableRenameClause?: (ctx: TableRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.tableRenameClause`.
     * @param ctx the parse tree
     */
    exitTableRenameClause?: (ctx: TableRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.swapTableClause`.
     * @param ctx the parse tree
     */
    enterSwapTableClause?: (ctx: SwapTableClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.swapTableClause`.
     * @param ctx the parse tree
     */
    exitSwapTableClause?: (ctx: SwapTableClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyTablePropertiesClause`.
     * @param ctx the parse tree
     */
    enterModifyTablePropertiesClause?: (ctx: ModifyTablePropertiesClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyTablePropertiesClause`.
     * @param ctx the parse tree
     */
    exitModifyTablePropertiesClause?: (ctx: ModifyTablePropertiesClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyCommentClause`.
     * @param ctx the parse tree
     */
    enterModifyCommentClause?: (ctx: ModifyCommentClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyCommentClause`.
     * @param ctx the parse tree
     */
    exitModifyCommentClause?: (ctx: ModifyCommentClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.addColumnClause`.
     * @param ctx the parse tree
     */
    enterAddColumnClause?: (ctx: AddColumnClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.addColumnClause`.
     * @param ctx the parse tree
     */
    exitAddColumnClause?: (ctx: AddColumnClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.addColumnsClause`.
     * @param ctx the parse tree
     */
    enterAddColumnsClause?: (ctx: AddColumnsClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.addColumnsClause`.
     * @param ctx the parse tree
     */
    exitAddColumnsClause?: (ctx: AddColumnsClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropColumnClause`.
     * @param ctx the parse tree
     */
    enterDropColumnClause?: (ctx: DropColumnClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropColumnClause`.
     * @param ctx the parse tree
     */
    exitDropColumnClause?: (ctx: DropColumnClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyColumnClause`.
     * @param ctx the parse tree
     */
    enterModifyColumnClause?: (ctx: ModifyColumnClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyColumnClause`.
     * @param ctx the parse tree
     */
    exitModifyColumnClause?: (ctx: ModifyColumnClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.columnRenameClause`.
     * @param ctx the parse tree
     */
    enterColumnRenameClause?: (ctx: ColumnRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.columnRenameClause`.
     * @param ctx the parse tree
     */
    exitColumnRenameClause?: (ctx: ColumnRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.reorderColumnsClause`.
     * @param ctx the parse tree
     */
    enterReorderColumnsClause?: (ctx: ReorderColumnsClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.reorderColumnsClause`.
     * @param ctx the parse tree
     */
    exitReorderColumnsClause?: (ctx: ReorderColumnsClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.rollupRenameClause`.
     * @param ctx the parse tree
     */
    enterRollupRenameClause?: (ctx: RollupRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.rollupRenameClause`.
     * @param ctx the parse tree
     */
    exitRollupRenameClause?: (ctx: RollupRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.compactionClause`.
     * @param ctx the parse tree
     */
    enterCompactionClause?: (ctx: CompactionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.compactionClause`.
     * @param ctx the parse tree
     */
    exitCompactionClause?: (ctx: CompactionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.applyMaskingPolicyClause`.
     * @param ctx the parse tree
     */
    enterApplyMaskingPolicyClause?: (ctx: ApplyMaskingPolicyClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.applyMaskingPolicyClause`.
     * @param ctx the parse tree
     */
    exitApplyMaskingPolicyClause?: (ctx: ApplyMaskingPolicyClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.applyRowAccessPolicyClause`.
     * @param ctx the parse tree
     */
    enterApplyRowAccessPolicyClause?: (ctx: ApplyRowAccessPolicyClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.applyRowAccessPolicyClause`.
     * @param ctx the parse tree
     */
    exitApplyRowAccessPolicyClause?: (ctx: ApplyRowAccessPolicyClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.addPartitionClause`.
     * @param ctx the parse tree
     */
    enterAddPartitionClause?: (ctx: AddPartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.addPartitionClause`.
     * @param ctx the parse tree
     */
    exitAddPartitionClause?: (ctx: AddPartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropPartitionClause`.
     * @param ctx the parse tree
     */
    enterDropPartitionClause?: (ctx: DropPartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropPartitionClause`.
     * @param ctx the parse tree
     */
    exitDropPartitionClause?: (ctx: DropPartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.truncatePartitionClause`.
     * @param ctx the parse tree
     */
    enterTruncatePartitionClause?: (ctx: TruncatePartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.truncatePartitionClause`.
     * @param ctx the parse tree
     */
    exitTruncatePartitionClause?: (ctx: TruncatePartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.modifyPartitionClause`.
     * @param ctx the parse tree
     */
    enterModifyPartitionClause?: (ctx: ModifyPartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.modifyPartitionClause`.
     * @param ctx the parse tree
     */
    exitModifyPartitionClause?: (ctx: ModifyPartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.replacePartitionClause`.
     * @param ctx the parse tree
     */
    enterReplacePartitionClause?: (ctx: ReplacePartitionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.replacePartitionClause`.
     * @param ctx the parse tree
     */
    exitReplacePartitionClause?: (ctx: ReplacePartitionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.partitionRenameClause`.
     * @param ctx the parse tree
     */
    enterPartitionRenameClause?: (ctx: PartitionRenameClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.partitionRenameClause`.
     * @param ctx the parse tree
     */
    exitPartitionRenameClause?: (ctx: PartitionRenameClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.insertStatement`.
     * @param ctx the parse tree
     */
    enterInsertStatement?: (ctx: InsertStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.insertStatement`.
     * @param ctx the parse tree
     */
    exitInsertStatement?: (ctx: InsertStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.updateStatement`.
     * @param ctx the parse tree
     */
    enterUpdateStatement?: (ctx: UpdateStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.updateStatement`.
     * @param ctx the parse tree
     */
    exitUpdateStatement?: (ctx: UpdateStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.deleteStatement`.
     * @param ctx the parse tree
     */
    enterDeleteStatement?: (ctx: DeleteStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.deleteStatement`.
     * @param ctx the parse tree
     */
    exitDeleteStatement?: (ctx: DeleteStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterCreateRoutineLoadStatement?: (ctx: CreateRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitCreateRoutineLoadStatement?: (ctx: CreateRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterAlterRoutineLoadStatement?: (ctx: AlterRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitAlterRoutineLoadStatement?: (ctx: AlterRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dataSource`.
     * @param ctx the parse tree
     */
    enterDataSource?: (ctx: DataSourceContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dataSource`.
     * @param ctx the parse tree
     */
    exitDataSource?: (ctx: DataSourceContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.loadPropertiesExpr`.
     * @param ctx the parse tree
     */
    enterLoadPropertiesExpr?: (ctx: LoadPropertiesExprContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.loadPropertiesExpr`.
     * @param ctx the parse tree
     */
    exitLoadPropertiesExpr?: (ctx: LoadPropertiesExprContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.loadProperties`.
     * @param ctx the parse tree
     */
    enterLoadProperties?: (ctx: LoadPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.loadProperties`.
     * @param ctx the parse tree
     */
    exitLoadProperties?: (ctx: LoadPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.colSeparatorProperty`.
     * @param ctx the parse tree
     */
    enterColSeparatorProperty?: (ctx: ColSeparatorPropertyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.colSeparatorProperty`.
     * @param ctx the parse tree
     */
    exitColSeparatorProperty?: (ctx: ColSeparatorPropertyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.rowDelimiterProperty`.
     * @param ctx the parse tree
     */
    enterRowDelimiterProperty?: (ctx: RowDelimiterPropertyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.rowDelimiterProperty`.
     * @param ctx the parse tree
     */
    exitRowDelimiterProperty?: (ctx: RowDelimiterPropertyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.importColumns`.
     * @param ctx the parse tree
     */
    enterImportColumns?: (ctx: ImportColumnsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.importColumns`.
     * @param ctx the parse tree
     */
    exitImportColumns?: (ctx: ImportColumnsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.columnProperties`.
     * @param ctx the parse tree
     */
    enterColumnProperties?: (ctx: ColumnPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.columnProperties`.
     * @param ctx the parse tree
     */
    exitColumnProperties?: (ctx: ColumnPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.jobProperties`.
     * @param ctx the parse tree
     */
    enterJobProperties?: (ctx: JobPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.jobProperties`.
     * @param ctx the parse tree
     */
    exitJobProperties?: (ctx: JobPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dataSourceProperties`.
     * @param ctx the parse tree
     */
    enterDataSourceProperties?: (ctx: DataSourcePropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dataSourceProperties`.
     * @param ctx the parse tree
     */
    exitDataSourceProperties?: (ctx: DataSourcePropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.stopRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterStopRoutineLoadStatement?: (ctx: StopRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.stopRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitStopRoutineLoadStatement?: (ctx: StopRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.resumeRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterResumeRoutineLoadStatement?: (ctx: ResumeRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.resumeRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitResumeRoutineLoadStatement?: (ctx: ResumeRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.pauseRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterPauseRoutineLoadStatement?: (ctx: PauseRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.pauseRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitPauseRoutineLoadStatement?: (ctx: PauseRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    enterShowRoutineLoadStatement?: (ctx: ShowRoutineLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showRoutineLoadStatement`.
     * @param ctx the parse tree
     */
    exitShowRoutineLoadStatement?: (ctx: ShowRoutineLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showRoutineLoadTaskStatement`.
     * @param ctx the parse tree
     */
    enterShowRoutineLoadTaskStatement?: (ctx: ShowRoutineLoadTaskStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showRoutineLoadTaskStatement`.
     * @param ctx the parse tree
     */
    exitShowRoutineLoadTaskStatement?: (ctx: ShowRoutineLoadTaskStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showStreamLoadStatement`.
     * @param ctx the parse tree
     */
    enterShowStreamLoadStatement?: (ctx: ShowStreamLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showStreamLoadStatement`.
     * @param ctx the parse tree
     */
    exitShowStreamLoadStatement?: (ctx: ShowStreamLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.analyzeStatement`.
     * @param ctx the parse tree
     */
    enterAnalyzeStatement?: (ctx: AnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.analyzeStatement`.
     * @param ctx the parse tree
     */
    exitAnalyzeStatement?: (ctx: AnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropStatsStatement`.
     * @param ctx the parse tree
     */
    enterDropStatsStatement?: (ctx: DropStatsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropStatsStatement`.
     * @param ctx the parse tree
     */
    exitDropStatsStatement?: (ctx: DropStatsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.analyzeHistogramStatement`.
     * @param ctx the parse tree
     */
    enterAnalyzeHistogramStatement?: (ctx: AnalyzeHistogramStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.analyzeHistogramStatement`.
     * @param ctx the parse tree
     */
    exitAnalyzeHistogramStatement?: (ctx: AnalyzeHistogramStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropHistogramStatement`.
     * @param ctx the parse tree
     */
    enterDropHistogramStatement?: (ctx: DropHistogramStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropHistogramStatement`.
     * @param ctx the parse tree
     */
    exitDropHistogramStatement?: (ctx: DropHistogramStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createAnalyzeStatement`.
     * @param ctx the parse tree
     */
    enterCreateAnalyzeStatement?: (ctx: CreateAnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createAnalyzeStatement`.
     * @param ctx the parse tree
     */
    exitCreateAnalyzeStatement?: (ctx: CreateAnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropAnalyzeJobStatement`.
     * @param ctx the parse tree
     */
    enterDropAnalyzeJobStatement?: (ctx: DropAnalyzeJobStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropAnalyzeJobStatement`.
     * @param ctx the parse tree
     */
    exitDropAnalyzeJobStatement?: (ctx: DropAnalyzeJobStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showAnalyzeStatement`.
     * @param ctx the parse tree
     */
    enterShowAnalyzeStatement?: (ctx: ShowAnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showAnalyzeStatement`.
     * @param ctx the parse tree
     */
    exitShowAnalyzeStatement?: (ctx: ShowAnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showStatsMetaStatement`.
     * @param ctx the parse tree
     */
    enterShowStatsMetaStatement?: (ctx: ShowStatsMetaStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showStatsMetaStatement`.
     * @param ctx the parse tree
     */
    exitShowStatsMetaStatement?: (ctx: ShowStatsMetaStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showHistogramMetaStatement`.
     * @param ctx the parse tree
     */
    enterShowHistogramMetaStatement?: (ctx: ShowHistogramMetaStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showHistogramMetaStatement`.
     * @param ctx the parse tree
     */
    exitShowHistogramMetaStatement?: (ctx: ShowHistogramMetaStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.killAnalyzeStatement`.
     * @param ctx the parse tree
     */
    enterKillAnalyzeStatement?: (ctx: KillAnalyzeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.killAnalyzeStatement`.
     * @param ctx the parse tree
     */
    exitKillAnalyzeStatement?: (ctx: KillAnalyzeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.analyzeProfileStatement`.
     * @param ctx the parse tree
     */
    enterAnalyzeProfileStatement?: (ctx: AnalyzeProfileStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.analyzeProfileStatement`.
     * @param ctx the parse tree
     */
    exitAnalyzeProfileStatement?: (ctx: AnalyzeProfileStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterCreateResourceGroupStatement?: (ctx: CreateResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitCreateResourceGroupStatement?: (ctx: CreateResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterDropResourceGroupStatement?: (ctx: DropResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitDropResourceGroupStatement?: (ctx: DropResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterAlterResourceGroupStatement?: (ctx: AlterResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitAlterResourceGroupStatement?: (ctx: AlterResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showResourceGroupStatement`.
     * @param ctx the parse tree
     */
    enterShowResourceGroupStatement?: (ctx: ShowResourceGroupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showResourceGroupStatement`.
     * @param ctx the parse tree
     */
    exitShowResourceGroupStatement?: (ctx: ShowResourceGroupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createResourceStatement`.
     * @param ctx the parse tree
     */
    enterCreateResourceStatement?: (ctx: CreateResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createResourceStatement`.
     * @param ctx the parse tree
     */
    exitCreateResourceStatement?: (ctx: CreateResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterResourceStatement`.
     * @param ctx the parse tree
     */
    enterAlterResourceStatement?: (ctx: AlterResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterResourceStatement`.
     * @param ctx the parse tree
     */
    exitAlterResourceStatement?: (ctx: AlterResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropResourceStatement`.
     * @param ctx the parse tree
     */
    enterDropResourceStatement?: (ctx: DropResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropResourceStatement`.
     * @param ctx the parse tree
     */
    exitDropResourceStatement?: (ctx: DropResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showResourceStatement`.
     * @param ctx the parse tree
     */
    enterShowResourceStatement?: (ctx: ShowResourceStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showResourceStatement`.
     * @param ctx the parse tree
     */
    exitShowResourceStatement?: (ctx: ShowResourceStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.classifier`.
     * @param ctx the parse tree
     */
    enterClassifier?: (ctx: ClassifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.classifier`.
     * @param ctx the parse tree
     */
    exitClassifier?: (ctx: ClassifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showFunctionsStatement`.
     * @param ctx the parse tree
     */
    enterShowFunctionsStatement?: (ctx: ShowFunctionsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showFunctionsStatement`.
     * @param ctx the parse tree
     */
    exitShowFunctionsStatement?: (ctx: ShowFunctionsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropFunctionStatement`.
     * @param ctx the parse tree
     */
    enterDropFunctionStatement?: (ctx: DropFunctionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropFunctionStatement`.
     * @param ctx the parse tree
     */
    exitDropFunctionStatement?: (ctx: DropFunctionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createFunctionStatement`.
     * @param ctx the parse tree
     */
    enterCreateFunctionStatement?: (ctx: CreateFunctionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createFunctionStatement`.
     * @param ctx the parse tree
     */
    exitCreateFunctionStatement?: (ctx: CreateFunctionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.typeList`.
     * @param ctx the parse tree
     */
    enterTypeList?: (ctx: TypeListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.typeList`.
     * @param ctx the parse tree
     */
    exitTypeList?: (ctx: TypeListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.loadStatement`.
     * @param ctx the parse tree
     */
    enterLoadStatement?: (ctx: LoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.loadStatement`.
     * @param ctx the parse tree
     */
    exitLoadStatement?: (ctx: LoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.labelName`.
     * @param ctx the parse tree
     */
    enterLabelName?: (ctx: LabelNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.labelName`.
     * @param ctx the parse tree
     */
    exitLabelName?: (ctx: LabelNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dataDescList`.
     * @param ctx the parse tree
     */
    enterDataDescList?: (ctx: DataDescListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dataDescList`.
     * @param ctx the parse tree
     */
    exitDataDescList?: (ctx: DataDescListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dataDesc`.
     * @param ctx the parse tree
     */
    enterDataDesc?: (ctx: DataDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dataDesc`.
     * @param ctx the parse tree
     */
    exitDataDesc?: (ctx: DataDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.formatProps`.
     * @param ctx the parse tree
     */
    enterFormatProps?: (ctx: FormatPropsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.formatProps`.
     * @param ctx the parse tree
     */
    exitFormatProps?: (ctx: FormatPropsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.brokerDesc`.
     * @param ctx the parse tree
     */
    enterBrokerDesc?: (ctx: BrokerDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.brokerDesc`.
     * @param ctx the parse tree
     */
    exitBrokerDesc?: (ctx: BrokerDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.resourceDesc`.
     * @param ctx the parse tree
     */
    enterResourceDesc?: (ctx: ResourceDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.resourceDesc`.
     * @param ctx the parse tree
     */
    exitResourceDesc?: (ctx: ResourceDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showLoadStatement`.
     * @param ctx the parse tree
     */
    enterShowLoadStatement?: (ctx: ShowLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showLoadStatement`.
     * @param ctx the parse tree
     */
    exitShowLoadStatement?: (ctx: ShowLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showLoadWarningsStatement`.
     * @param ctx the parse tree
     */
    enterShowLoadWarningsStatement?: (ctx: ShowLoadWarningsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showLoadWarningsStatement`.
     * @param ctx the parse tree
     */
    exitShowLoadWarningsStatement?: (ctx: ShowLoadWarningsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelLoadStatement`.
     * @param ctx the parse tree
     */
    enterCancelLoadStatement?: (ctx: CancelLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelLoadStatement`.
     * @param ctx the parse tree
     */
    exitCancelLoadStatement?: (ctx: CancelLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterLoadStatement`.
     * @param ctx the parse tree
     */
    enterAlterLoadStatement?: (ctx: AlterLoadStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterLoadStatement`.
     * @param ctx the parse tree
     */
    exitAlterLoadStatement?: (ctx: AlterLoadStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelCompactionStatement`.
     * @param ctx the parse tree
     */
    enterCancelCompactionStatement?: (ctx: CancelCompactionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelCompactionStatement`.
     * @param ctx the parse tree
     */
    exitCancelCompactionStatement?: (ctx: CancelCompactionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showAuthorStatement`.
     * @param ctx the parse tree
     */
    enterShowAuthorStatement?: (ctx: ShowAuthorStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showAuthorStatement`.
     * @param ctx the parse tree
     */
    exitShowAuthorStatement?: (ctx: ShowAuthorStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showBackendsStatement`.
     * @param ctx the parse tree
     */
    enterShowBackendsStatement?: (ctx: ShowBackendsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showBackendsStatement`.
     * @param ctx the parse tree
     */
    exitShowBackendsStatement?: (ctx: ShowBackendsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showBrokerStatement`.
     * @param ctx the parse tree
     */
    enterShowBrokerStatement?: (ctx: ShowBrokerStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showBrokerStatement`.
     * @param ctx the parse tree
     */
    exitShowBrokerStatement?: (ctx: ShowBrokerStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCharsetStatement`.
     * @param ctx the parse tree
     */
    enterShowCharsetStatement?: (ctx: ShowCharsetStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCharsetStatement`.
     * @param ctx the parse tree
     */
    exitShowCharsetStatement?: (ctx: ShowCharsetStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCollationStatement`.
     * @param ctx the parse tree
     */
    enterShowCollationStatement?: (ctx: ShowCollationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCollationStatement`.
     * @param ctx the parse tree
     */
    exitShowCollationStatement?: (ctx: ShowCollationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showDeleteStatement`.
     * @param ctx the parse tree
     */
    enterShowDeleteStatement?: (ctx: ShowDeleteStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showDeleteStatement`.
     * @param ctx the parse tree
     */
    exitShowDeleteStatement?: (ctx: ShowDeleteStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showDynamicPartitionStatement`.
     * @param ctx the parse tree
     */
    enterShowDynamicPartitionStatement?: (ctx: ShowDynamicPartitionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showDynamicPartitionStatement`.
     * @param ctx the parse tree
     */
    exitShowDynamicPartitionStatement?: (ctx: ShowDynamicPartitionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showEventsStatement`.
     * @param ctx the parse tree
     */
    enterShowEventsStatement?: (ctx: ShowEventsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showEventsStatement`.
     * @param ctx the parse tree
     */
    exitShowEventsStatement?: (ctx: ShowEventsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showEnginesStatement`.
     * @param ctx the parse tree
     */
    enterShowEnginesStatement?: (ctx: ShowEnginesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showEnginesStatement`.
     * @param ctx the parse tree
     */
    exitShowEnginesStatement?: (ctx: ShowEnginesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showFrontendsStatement`.
     * @param ctx the parse tree
     */
    enterShowFrontendsStatement?: (ctx: ShowFrontendsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showFrontendsStatement`.
     * @param ctx the parse tree
     */
    exitShowFrontendsStatement?: (ctx: ShowFrontendsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showPluginsStatement`.
     * @param ctx the parse tree
     */
    enterShowPluginsStatement?: (ctx: ShowPluginsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showPluginsStatement`.
     * @param ctx the parse tree
     */
    exitShowPluginsStatement?: (ctx: ShowPluginsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showRepositoriesStatement`.
     * @param ctx the parse tree
     */
    enterShowRepositoriesStatement?: (ctx: ShowRepositoriesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showRepositoriesStatement`.
     * @param ctx the parse tree
     */
    exitShowRepositoriesStatement?: (ctx: ShowRepositoriesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showOpenTableStatement`.
     * @param ctx the parse tree
     */
    enterShowOpenTableStatement?: (ctx: ShowOpenTableStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showOpenTableStatement`.
     * @param ctx the parse tree
     */
    exitShowOpenTableStatement?: (ctx: ShowOpenTableStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showPrivilegesStatement`.
     * @param ctx the parse tree
     */
    enterShowPrivilegesStatement?: (ctx: ShowPrivilegesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showPrivilegesStatement`.
     * @param ctx the parse tree
     */
    exitShowPrivilegesStatement?: (ctx: ShowPrivilegesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showProcedureStatement`.
     * @param ctx the parse tree
     */
    enterShowProcedureStatement?: (ctx: ShowProcedureStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showProcedureStatement`.
     * @param ctx the parse tree
     */
    exitShowProcedureStatement?: (ctx: ShowProcedureStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showProcStatement`.
     * @param ctx the parse tree
     */
    enterShowProcStatement?: (ctx: ShowProcStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showProcStatement`.
     * @param ctx the parse tree
     */
    exitShowProcStatement?: (ctx: ShowProcStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showProcesslistStatement`.
     * @param ctx the parse tree
     */
    enterShowProcesslistStatement?: (ctx: ShowProcesslistStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showProcesslistStatement`.
     * @param ctx the parse tree
     */
    exitShowProcesslistStatement?: (ctx: ShowProcesslistStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showProfilelistStatement`.
     * @param ctx the parse tree
     */
    enterShowProfilelistStatement?: (ctx: ShowProfilelistStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showProfilelistStatement`.
     * @param ctx the parse tree
     */
    exitShowProfilelistStatement?: (ctx: ShowProfilelistStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showStatusStatement`.
     * @param ctx the parse tree
     */
    enterShowStatusStatement?: (ctx: ShowStatusStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showStatusStatement`.
     * @param ctx the parse tree
     */
    exitShowStatusStatement?: (ctx: ShowStatusStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showTabletStatement`.
     * @param ctx the parse tree
     */
    enterShowTabletStatement?: (ctx: ShowTabletStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showTabletStatement`.
     * @param ctx the parse tree
     */
    exitShowTabletStatement?: (ctx: ShowTabletStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showTransactionStatement`.
     * @param ctx the parse tree
     */
    enterShowTransactionStatement?: (ctx: ShowTransactionStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showTransactionStatement`.
     * @param ctx the parse tree
     */
    exitShowTransactionStatement?: (ctx: ShowTransactionStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showTriggersStatement`.
     * @param ctx the parse tree
     */
    enterShowTriggersStatement?: (ctx: ShowTriggersStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showTriggersStatement`.
     * @param ctx the parse tree
     */
    exitShowTriggersStatement?: (ctx: ShowTriggersStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showUserPropertyStatement`.
     * @param ctx the parse tree
     */
    enterShowUserPropertyStatement?: (ctx: ShowUserPropertyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showUserPropertyStatement`.
     * @param ctx the parse tree
     */
    exitShowUserPropertyStatement?: (ctx: ShowUserPropertyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showVariablesStatement`.
     * @param ctx the parse tree
     */
    enterShowVariablesStatement?: (ctx: ShowVariablesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showVariablesStatement`.
     * @param ctx the parse tree
     */
    exitShowVariablesStatement?: (ctx: ShowVariablesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showWarningStatement`.
     * @param ctx the parse tree
     */
    enterShowWarningStatement?: (ctx: ShowWarningStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showWarningStatement`.
     * @param ctx the parse tree
     */
    exitShowWarningStatement?: (ctx: ShowWarningStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.helpStatement`.
     * @param ctx the parse tree
     */
    enterHelpStatement?: (ctx: HelpStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.helpStatement`.
     * @param ctx the parse tree
     */
    exitHelpStatement?: (ctx: HelpStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createUserStatement`.
     * @param ctx the parse tree
     */
    enterCreateUserStatement?: (ctx: CreateUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createUserStatement`.
     * @param ctx the parse tree
     */
    exitCreateUserStatement?: (ctx: CreateUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropUserStatement`.
     * @param ctx the parse tree
     */
    enterDropUserStatement?: (ctx: DropUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropUserStatement`.
     * @param ctx the parse tree
     */
    exitDropUserStatement?: (ctx: DropUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterUserStatement`.
     * @param ctx the parse tree
     */
    enterAlterUserStatement?: (ctx: AlterUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterUserStatement`.
     * @param ctx the parse tree
     */
    exitAlterUserStatement?: (ctx: AlterUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showUserStatement`.
     * @param ctx the parse tree
     */
    enterShowUserStatement?: (ctx: ShowUserStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showUserStatement`.
     * @param ctx the parse tree
     */
    exitShowUserStatement?: (ctx: ShowUserStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    enterShowAuthenticationStatement?: (ctx: ShowAuthenticationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     */
    exitShowAuthenticationStatement?: (ctx: ShowAuthenticationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.executeAsStatement`.
     * @param ctx the parse tree
     */
    enterExecuteAsStatement?: (ctx: ExecuteAsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.executeAsStatement`.
     * @param ctx the parse tree
     */
    exitExecuteAsStatement?: (ctx: ExecuteAsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createRoleStatement`.
     * @param ctx the parse tree
     */
    enterCreateRoleStatement?: (ctx: CreateRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createRoleStatement`.
     * @param ctx the parse tree
     */
    exitCreateRoleStatement?: (ctx: CreateRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterRoleStatement`.
     * @param ctx the parse tree
     */
    enterAlterRoleStatement?: (ctx: AlterRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterRoleStatement`.
     * @param ctx the parse tree
     */
    exitAlterRoleStatement?: (ctx: AlterRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropRoleStatement`.
     * @param ctx the parse tree
     */
    enterDropRoleStatement?: (ctx: DropRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropRoleStatement`.
     * @param ctx the parse tree
     */
    exitDropRoleStatement?: (ctx: DropRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showRolesStatement`.
     * @param ctx the parse tree
     */
    enterShowRolesStatement?: (ctx: ShowRolesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showRolesStatement`.
     * @param ctx the parse tree
     */
    exitShowRolesStatement?: (ctx: ShowRolesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    enterGrantRoleStatement?: (ctx: GrantRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     */
    exitGrantRoleStatement?: (ctx: GrantRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    enterRevokeRoleStatement?: (ctx: RevokeRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     */
    exitRevokeRoleStatement?: (ctx: RevokeRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setRoleStatement`.
     * @param ctx the parse tree
     */
    enterSetRoleStatement?: (ctx: SetRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setRoleStatement`.
     * @param ctx the parse tree
     */
    exitSetRoleStatement?: (ctx: SetRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setDefaultRoleStatement`.
     * @param ctx the parse tree
     */
    enterSetDefaultRoleStatement?: (ctx: SetDefaultRoleStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setDefaultRoleStatement`.
     * @param ctx the parse tree
     */
    exitSetDefaultRoleStatement?: (ctx: SetDefaultRoleStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.grantRevokeClause`.
     * @param ctx the parse tree
     */
    enterGrantRevokeClause?: (ctx: GrantRevokeClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.grantRevokeClause`.
     * @param ctx the parse tree
     */
    exitGrantRevokeClause?: (ctx: GrantRevokeClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterGrantPrivilegeStatement?: (ctx: GrantPrivilegeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitGrantPrivilegeStatement?: (ctx: GrantPrivilegeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    enterRevokePrivilegeStatement?: (ctx: RevokePrivilegeStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     */
    exitRevokePrivilegeStatement?: (ctx: RevokePrivilegeStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showGrantsStatement`.
     * @param ctx the parse tree
     */
    enterShowGrantsStatement?: (ctx: ShowGrantsStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showGrantsStatement`.
     * @param ctx the parse tree
     */
    exitShowGrantsStatement?: (ctx: ShowGrantsStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterCreateSecurityIntegrationStatement?: (ctx: CreateSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitCreateSecurityIntegrationStatement?: (ctx: CreateSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterAlterSecurityIntegrationStatement?: (ctx: AlterSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitAlterSecurityIntegrationStatement?: (ctx: AlterSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterDropSecurityIntegrationStatement?: (ctx: DropSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitDropSecurityIntegrationStatement?: (ctx: DropSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterShowSecurityIntegrationStatement?: (ctx: ShowSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitShowSecurityIntegrationStatement?: (ctx: ShowSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCreateSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateSecurityIntegrationStatement?: (ctx: ShowCreateSecurityIntegrationStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCreateSecurityIntegrationStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateSecurityIntegrationStatement?: (ctx: ShowCreateSecurityIntegrationStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterCreateRoleMappingStatement?: (ctx: CreateRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitCreateRoleMappingStatement?: (ctx: CreateRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterAlterRoleMappingStatement?: (ctx: AlterRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitAlterRoleMappingStatement?: (ctx: AlterRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterDropRoleMappingStatement?: (ctx: DropRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitDropRoleMappingStatement?: (ctx: DropRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterShowRoleMappingStatement?: (ctx: ShowRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitShowRoleMappingStatement?: (ctx: ShowRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.refreshRoleMappingStatement`.
     * @param ctx the parse tree
     */
    enterRefreshRoleMappingStatement?: (ctx: RefreshRoleMappingStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.refreshRoleMappingStatement`.
     * @param ctx the parse tree
     */
    exitRefreshRoleMappingStatement?: (ctx: RefreshRoleMappingStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     */
    enterAuthOption?: (ctx: AuthOptionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     */
    exitAuthOption?: (ctx: AuthOptionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.privObjectName`.
     * @param ctx the parse tree
     */
    enterPrivObjectName?: (ctx: PrivObjectNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.privObjectName`.
     * @param ctx the parse tree
     */
    exitPrivObjectName?: (ctx: PrivObjectNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.privObjectNameList`.
     * @param ctx the parse tree
     */
    enterPrivObjectNameList?: (ctx: PrivObjectNameListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.privObjectNameList`.
     * @param ctx the parse tree
     */
    exitPrivObjectNameList?: (ctx: PrivObjectNameListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.privFunctionObjectNameList`.
     * @param ctx the parse tree
     */
    enterPrivFunctionObjectNameList?: (ctx: PrivFunctionObjectNameListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.privFunctionObjectNameList`.
     * @param ctx the parse tree
     */
    exitPrivFunctionObjectNameList?: (ctx: PrivFunctionObjectNameListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.privilegeTypeList`.
     * @param ctx the parse tree
     */
    enterPrivilegeTypeList?: (ctx: PrivilegeTypeListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.privilegeTypeList`.
     * @param ctx the parse tree
     */
    exitPrivilegeTypeList?: (ctx: PrivilegeTypeListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.privilegeType`.
     * @param ctx the parse tree
     */
    enterPrivilegeType?: (ctx: PrivilegeTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.privilegeType`.
     * @param ctx the parse tree
     */
    exitPrivilegeType?: (ctx: PrivilegeTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.privObjectType`.
     * @param ctx the parse tree
     */
    enterPrivObjectType?: (ctx: PrivObjectTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.privObjectType`.
     * @param ctx the parse tree
     */
    exitPrivObjectType?: (ctx: PrivObjectTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.privObjectTypePlural`.
     * @param ctx the parse tree
     */
    enterPrivObjectTypePlural?: (ctx: PrivObjectTypePluralContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.privObjectTypePlural`.
     * @param ctx the parse tree
     */
    exitPrivObjectTypePlural?: (ctx: PrivObjectTypePluralContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterCreateMaskingPolicyStatement?: (ctx: CreateMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitCreateMaskingPolicyStatement?: (ctx: CreateMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterDropMaskingPolicyStatement?: (ctx: DropMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitDropMaskingPolicyStatement?: (ctx: DropMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterAlterMaskingPolicyStatement?: (ctx: AlterMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitAlterMaskingPolicyStatement?: (ctx: AlterMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowMaskingPolicyStatement?: (ctx: ShowMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowMaskingPolicyStatement?: (ctx: ShowMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCreateMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateMaskingPolicyStatement?: (ctx: ShowCreateMaskingPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCreateMaskingPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateMaskingPolicyStatement?: (ctx: ShowCreateMaskingPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterCreateRowAccessPolicyStatement?: (ctx: CreateRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitCreateRowAccessPolicyStatement?: (ctx: CreateRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterDropRowAccessPolicyStatement?: (ctx: DropRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitDropRowAccessPolicyStatement?: (ctx: DropRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.alterRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterAlterRowAccessPolicyStatement?: (ctx: AlterRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.alterRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitAlterRowAccessPolicyStatement?: (ctx: AlterRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowRowAccessPolicyStatement?: (ctx: ShowRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowRowAccessPolicyStatement?: (ctx: ShowRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showCreateRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    enterShowCreateRowAccessPolicyStatement?: (ctx: ShowCreateRowAccessPolicyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showCreateRowAccessPolicyStatement`.
     * @param ctx the parse tree
     */
    exitShowCreateRowAccessPolicyStatement?: (ctx: ShowCreateRowAccessPolicyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.policySignature`.
     * @param ctx the parse tree
     */
    enterPolicySignature?: (ctx: PolicySignatureContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.policySignature`.
     * @param ctx the parse tree
     */
    exitPolicySignature?: (ctx: PolicySignatureContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.backupStatement`.
     * @param ctx the parse tree
     */
    enterBackupStatement?: (ctx: BackupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.backupStatement`.
     * @param ctx the parse tree
     */
    exitBackupStatement?: (ctx: BackupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelBackupStatement`.
     * @param ctx the parse tree
     */
    enterCancelBackupStatement?: (ctx: CancelBackupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelBackupStatement`.
     * @param ctx the parse tree
     */
    exitCancelBackupStatement?: (ctx: CancelBackupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showBackupStatement`.
     * @param ctx the parse tree
     */
    enterShowBackupStatement?: (ctx: ShowBackupStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showBackupStatement`.
     * @param ctx the parse tree
     */
    exitShowBackupStatement?: (ctx: ShowBackupStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.restoreStatement`.
     * @param ctx the parse tree
     */
    enterRestoreStatement?: (ctx: RestoreStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.restoreStatement`.
     * @param ctx the parse tree
     */
    exitRestoreStatement?: (ctx: RestoreStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelRestoreStatement`.
     * @param ctx the parse tree
     */
    enterCancelRestoreStatement?: (ctx: CancelRestoreStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelRestoreStatement`.
     * @param ctx the parse tree
     */
    exitCancelRestoreStatement?: (ctx: CancelRestoreStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showRestoreStatement`.
     * @param ctx the parse tree
     */
    enterShowRestoreStatement?: (ctx: ShowRestoreStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showRestoreStatement`.
     * @param ctx the parse tree
     */
    exitShowRestoreStatement?: (ctx: ShowRestoreStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showSnapshotStatement`.
     * @param ctx the parse tree
     */
    enterShowSnapshotStatement?: (ctx: ShowSnapshotStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showSnapshotStatement`.
     * @param ctx the parse tree
     */
    exitShowSnapshotStatement?: (ctx: ShowSnapshotStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createRepositoryStatement`.
     * @param ctx the parse tree
     */
    enterCreateRepositoryStatement?: (ctx: CreateRepositoryStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createRepositoryStatement`.
     * @param ctx the parse tree
     */
    exitCreateRepositoryStatement?: (ctx: CreateRepositoryStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropRepositoryStatement`.
     * @param ctx the parse tree
     */
    enterDropRepositoryStatement?: (ctx: DropRepositoryStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropRepositoryStatement`.
     * @param ctx the parse tree
     */
    exitDropRepositoryStatement?: (ctx: DropRepositoryStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.addSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    enterAddSqlBlackListStatement?: (ctx: AddSqlBlackListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.addSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    exitAddSqlBlackListStatement?: (ctx: AddSqlBlackListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.delSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    enterDelSqlBlackListStatement?: (ctx: DelSqlBlackListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.delSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    exitDelSqlBlackListStatement?: (ctx: DelSqlBlackListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    enterShowSqlBlackListStatement?: (ctx: ShowSqlBlackListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showSqlBlackListStatement`.
     * @param ctx the parse tree
     */
    exitShowSqlBlackListStatement?: (ctx: ShowSqlBlackListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showWhiteListStatement`.
     * @param ctx the parse tree
     */
    enterShowWhiteListStatement?: (ctx: ShowWhiteListStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showWhiteListStatement`.
     * @param ctx the parse tree
     */
    exitShowWhiteListStatement?: (ctx: ShowWhiteListStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.exportStatement`.
     * @param ctx the parse tree
     */
    enterExportStatement?: (ctx: ExportStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.exportStatement`.
     * @param ctx the parse tree
     */
    exitExportStatement?: (ctx: ExportStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.cancelExportStatement`.
     * @param ctx the parse tree
     */
    enterCancelExportStatement?: (ctx: CancelExportStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.cancelExportStatement`.
     * @param ctx the parse tree
     */
    exitCancelExportStatement?: (ctx: CancelExportStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showExportStatement`.
     * @param ctx the parse tree
     */
    enterShowExportStatement?: (ctx: ShowExportStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showExportStatement`.
     * @param ctx the parse tree
     */
    exitShowExportStatement?: (ctx: ShowExportStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.installPluginStatement`.
     * @param ctx the parse tree
     */
    enterInstallPluginStatement?: (ctx: InstallPluginStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.installPluginStatement`.
     * @param ctx the parse tree
     */
    exitInstallPluginStatement?: (ctx: InstallPluginStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.uninstallPluginStatement`.
     * @param ctx the parse tree
     */
    enterUninstallPluginStatement?: (ctx: UninstallPluginStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.uninstallPluginStatement`.
     * @param ctx the parse tree
     */
    exitUninstallPluginStatement?: (ctx: UninstallPluginStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.createFileStatement`.
     * @param ctx the parse tree
     */
    enterCreateFileStatement?: (ctx: CreateFileStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.createFileStatement`.
     * @param ctx the parse tree
     */
    exitCreateFileStatement?: (ctx: CreateFileStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.dropFileStatement`.
     * @param ctx the parse tree
     */
    enterDropFileStatement?: (ctx: DropFileStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.dropFileStatement`.
     * @param ctx the parse tree
     */
    exitDropFileStatement?: (ctx: DropFileStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.showSmallFilesStatement`.
     * @param ctx the parse tree
     */
    enterShowSmallFilesStatement?: (ctx: ShowSmallFilesStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.showSmallFilesStatement`.
     * @param ctx the parse tree
     */
    exitShowSmallFilesStatement?: (ctx: ShowSmallFilesStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setStatement`.
     * @param ctx the parse tree
     */
    enterSetStatement?: (ctx: SetStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setStatement`.
     * @param ctx the parse tree
     */
    exitSetStatement?: (ctx: SetStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    enterSetVar?: (ctx: SetVarContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     */
    exitSetVar?: (ctx: SetVarContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.transaction_characteristics`.
     * @param ctx the parse tree
     */
    enterTransaction_characteristics?: (ctx: Transaction_characteristicsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.transaction_characteristics`.
     * @param ctx the parse tree
     */
    exitTransaction_characteristics?: (ctx: Transaction_characteristicsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.transaction_access_mode`.
     * @param ctx the parse tree
     */
    enterTransaction_access_mode?: (ctx: Transaction_access_modeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.transaction_access_mode`.
     * @param ctx the parse tree
     */
    exitTransaction_access_mode?: (ctx: Transaction_access_modeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.isolation_level`.
     * @param ctx the parse tree
     */
    enterIsolation_level?: (ctx: Isolation_levelContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.isolation_level`.
     * @param ctx the parse tree
     */
    exitIsolation_level?: (ctx: Isolation_levelContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.isolation_types`.
     * @param ctx the parse tree
     */
    enterIsolation_types?: (ctx: Isolation_typesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.isolation_types`.
     * @param ctx the parse tree
     */
    exitIsolation_types?: (ctx: Isolation_typesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setExprOrDefault`.
     * @param ctx the parse tree
     */
    enterSetExprOrDefault?: (ctx: SetExprOrDefaultContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setExprOrDefault`.
     * @param ctx the parse tree
     */
    exitSetExprOrDefault?: (ctx: SetExprOrDefaultContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setUserPropertyStatement`.
     * @param ctx the parse tree
     */
    enterSetUserPropertyStatement?: (ctx: SetUserPropertyStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setUserPropertyStatement`.
     * @param ctx the parse tree
     */
    exitSetUserPropertyStatement?: (ctx: SetUserPropertyStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.roleList`.
     * @param ctx the parse tree
     */
    enterRoleList?: (ctx: RoleListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.roleList`.
     * @param ctx the parse tree
     */
    exitRoleList?: (ctx: RoleListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.executeScriptStatement`.
     * @param ctx the parse tree
     */
    enterExecuteScriptStatement?: (ctx: ExecuteScriptStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.executeScriptStatement`.
     * @param ctx the parse tree
     */
    exitExecuteScriptStatement?: (ctx: ExecuteScriptStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.unsupportedStatement`.
     * @param ctx the parse tree
     */
    enterUnsupportedStatement?: (ctx: UnsupportedStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.unsupportedStatement`.
     * @param ctx the parse tree
     */
    exitUnsupportedStatement?: (ctx: UnsupportedStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.lock_item`.
     * @param ctx the parse tree
     */
    enterLock_item?: (ctx: Lock_itemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.lock_item`.
     * @param ctx the parse tree
     */
    exitLock_item?: (ctx: Lock_itemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.lock_type`.
     * @param ctx the parse tree
     */
    enterLock_type?: (ctx: Lock_typeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.lock_type`.
     * @param ctx the parse tree
     */
    exitLock_type?: (ctx: Lock_typeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.queryStatement`.
     * @param ctx the parse tree
     */
    enterQueryStatement?: (ctx: QueryStatementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.queryStatement`.
     * @param ctx the parse tree
     */
    exitQueryStatement?: (ctx: QueryStatementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.queryRelation`.
     * @param ctx the parse tree
     */
    enterQueryRelation?: (ctx: QueryRelationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.queryRelation`.
     * @param ctx the parse tree
     */
    exitQueryRelation?: (ctx: QueryRelationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.withClause`.
     * @param ctx the parse tree
     */
    enterWithClause?: (ctx: WithClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.withClause`.
     * @param ctx the parse tree
     */
    exitWithClause?: (ctx: WithClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.queryNoWith`.
     * @param ctx the parse tree
     */
    enterQueryNoWith?: (ctx: QueryNoWithContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.queryNoWith`.
     * @param ctx the parse tree
     */
    exitQueryNoWith?: (ctx: QueryNoWithContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.temporalClause`.
     * @param ctx the parse tree
     */
    enterTemporalClause?: (ctx: TemporalClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.temporalClause`.
     * @param ctx the parse tree
     */
    exitTemporalClause?: (ctx: TemporalClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    enterQueryPrimary?: (ctx: QueryPrimaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     */
    exitQueryPrimary?: (ctx: QueryPrimaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.subquery`.
     * @param ctx the parse tree
     */
    enterSubquery?: (ctx: SubqueryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.subquery`.
     * @param ctx the parse tree
     */
    exitSubquery?: (ctx: SubqueryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.rowConstructor`.
     * @param ctx the parse tree
     */
    enterRowConstructor?: (ctx: RowConstructorContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.rowConstructor`.
     * @param ctx the parse tree
     */
    exitRowConstructor?: (ctx: RowConstructorContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.sortItem`.
     * @param ctx the parse tree
     */
    enterSortItem?: (ctx: SortItemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.sortItem`.
     * @param ctx the parse tree
     */
    exitSortItem?: (ctx: SortItemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.limitElement`.
     * @param ctx the parse tree
     */
    enterLimitElement?: (ctx: LimitElementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.limitElement`.
     * @param ctx the parse tree
     */
    exitLimitElement?: (ctx: LimitElementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.querySpecification`.
     * @param ctx the parse tree
     */
    enterQuerySpecification?: (ctx: QuerySpecificationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.querySpecification`.
     * @param ctx the parse tree
     */
    exitQuerySpecification?: (ctx: QuerySpecificationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     */
    enterFromClause?: (ctx: FromClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     */
    exitFromClause?: (ctx: FromClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    enterGroupingElement?: (ctx: GroupingElementContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     */
    exitGroupingElement?: (ctx: GroupingElementContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.groupingSet`.
     * @param ctx the parse tree
     */
    enterGroupingSet?: (ctx: GroupingSetContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.groupingSet`.
     * @param ctx the parse tree
     */
    exitGroupingSet?: (ctx: GroupingSetContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.commonTableExpression`.
     * @param ctx the parse tree
     */
    enterCommonTableExpression?: (ctx: CommonTableExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.commonTableExpression`.
     * @param ctx the parse tree
     */
    exitCommonTableExpression?: (ctx: CommonTableExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setQuantifier`.
     * @param ctx the parse tree
     */
    enterSetQuantifier?: (ctx: SetQuantifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setQuantifier`.
     * @param ctx the parse tree
     */
    exitSetQuantifier?: (ctx: SetQuantifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     */
    enterSelectItem?: (ctx: SelectItemContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     */
    exitSelectItem?: (ctx: SelectItemContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.relations`.
     * @param ctx the parse tree
     */
    enterRelations?: (ctx: RelationsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.relations`.
     * @param ctx the parse tree
     */
    exitRelations?: (ctx: RelationsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.relation`.
     * @param ctx the parse tree
     */
    enterRelation?: (ctx: RelationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.relation`.
     * @param ctx the parse tree
     */
    exitRelation?: (ctx: RelationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    enterRelationPrimary?: (ctx: RelationPrimaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     */
    exitRelationPrimary?: (ctx: RelationPrimaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.joinRelation`.
     * @param ctx the parse tree
     */
    enterJoinRelation?: (ctx: JoinRelationContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.joinRelation`.
     * @param ctx the parse tree
     */
    exitJoinRelation?: (ctx: JoinRelationContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.crossOrInnerJoinType`.
     * @param ctx the parse tree
     */
    enterCrossOrInnerJoinType?: (ctx: CrossOrInnerJoinTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.crossOrInnerJoinType`.
     * @param ctx the parse tree
     */
    exitCrossOrInnerJoinType?: (ctx: CrossOrInnerJoinTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.outerAndSemiJoinType`.
     * @param ctx the parse tree
     */
    enterOuterAndSemiJoinType?: (ctx: OuterAndSemiJoinTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.outerAndSemiJoinType`.
     * @param ctx the parse tree
     */
    exitOuterAndSemiJoinType?: (ctx: OuterAndSemiJoinTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.bracketHint`.
     * @param ctx the parse tree
     */
    enterBracketHint?: (ctx: BracketHintContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.bracketHint`.
     * @param ctx the parse tree
     */
    exitBracketHint?: (ctx: BracketHintContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.setVarHint`.
     * @param ctx the parse tree
     */
    enterSetVarHint?: (ctx: SetVarHintContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.setVarHint`.
     * @param ctx the parse tree
     */
    exitSetVarHint?: (ctx: SetVarHintContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.hintMap`.
     * @param ctx the parse tree
     */
    enterHintMap?: (ctx: HintMapContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.hintMap`.
     * @param ctx the parse tree
     */
    exitHintMap?: (ctx: HintMapContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.joinCriteria`.
     * @param ctx the parse tree
     */
    enterJoinCriteria?: (ctx: JoinCriteriaContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.joinCriteria`.
     * @param ctx the parse tree
     */
    exitJoinCriteria?: (ctx: JoinCriteriaContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.columnAliases`.
     * @param ctx the parse tree
     */
    enterColumnAliases?: (ctx: ColumnAliasesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.columnAliases`.
     * @param ctx the parse tree
     */
    exitColumnAliases?: (ctx: ColumnAliasesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.partitionNames`.
     * @param ctx the parse tree
     */
    enterPartitionNames?: (ctx: PartitionNamesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.partitionNames`.
     * @param ctx the parse tree
     */
    exitPartitionNames?: (ctx: PartitionNamesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.keyPartitions`.
     * @param ctx the parse tree
     */
    enterKeyPartitions?: (ctx: KeyPartitionsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.keyPartitions`.
     * @param ctx the parse tree
     */
    exitKeyPartitions?: (ctx: KeyPartitionsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.tabletList`.
     * @param ctx the parse tree
     */
    enterTabletList?: (ctx: TabletListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.tabletList`.
     * @param ctx the parse tree
     */
    exitTabletList?: (ctx: TabletListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.expressionsWithDefault`.
     * @param ctx the parse tree
     */
    enterExpressionsWithDefault?: (ctx: ExpressionsWithDefaultContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.expressionsWithDefault`.
     * @param ctx the parse tree
     */
    exitExpressionsWithDefault?: (ctx: ExpressionsWithDefaultContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.expressionOrDefault`.
     * @param ctx the parse tree
     */
    enterExpressionOrDefault?: (ctx: ExpressionOrDefaultContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.expressionOrDefault`.
     * @param ctx the parse tree
     */
    exitExpressionOrDefault?: (ctx: ExpressionOrDefaultContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.mapExpressionList`.
     * @param ctx the parse tree
     */
    enterMapExpressionList?: (ctx: MapExpressionListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.mapExpressionList`.
     * @param ctx the parse tree
     */
    exitMapExpressionList?: (ctx: MapExpressionListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.mapExpression`.
     * @param ctx the parse tree
     */
    enterMapExpression?: (ctx: MapExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.mapExpression`.
     * @param ctx the parse tree
     */
    exitMapExpression?: (ctx: MapExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.expressionSingleton`.
     * @param ctx the parse tree
     */
    enterExpressionSingleton?: (ctx: ExpressionSingletonContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.expressionSingleton`.
     * @param ctx the parse tree
     */
    exitExpressionSingleton?: (ctx: ExpressionSingletonContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    enterExpression?: (ctx: ExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     */
    exitExpression?: (ctx: ExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.expressionList`.
     * @param ctx the parse tree
     */
    enterExpressionList?: (ctx: ExpressionListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.expressionList`.
     * @param ctx the parse tree
     */
    exitExpressionList?: (ctx: ExpressionListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    enterBooleanExpression?: (ctx: BooleanExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     */
    exitBooleanExpression?: (ctx: BooleanExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.predicate`.
     * @param ctx the parse tree
     */
    enterPredicate?: (ctx: PredicateContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.predicate`.
     * @param ctx the parse tree
     */
    exitPredicate?: (ctx: PredicateContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.tupleInSubquery`.
     * @param ctx the parse tree
     */
    enterTupleInSubquery?: (ctx: TupleInSubqueryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.tupleInSubquery`.
     * @param ctx the parse tree
     */
    exitTupleInSubquery?: (ctx: TupleInSubqueryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    enterPredicateOperations?: (ctx: PredicateOperationsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     */
    exitPredicateOperations?: (ctx: PredicateOperationsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     */
    enterValueExpression?: (ctx: ValueExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     */
    exitValueExpression?: (ctx: ValueExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    enterPrimaryExpression?: (ctx: PrimaryExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     */
    exitPrimaryExpression?: (ctx: PrimaryExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    enterLiteralExpression?: (ctx: LiteralExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     */
    exitLiteralExpression?: (ctx: LiteralExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    enterFunctionCall?: (ctx: FunctionCallContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     */
    exitFunctionCall?: (ctx: FunctionCallContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.aggregationFunction`.
     * @param ctx the parse tree
     */
    enterAggregationFunction?: (ctx: AggregationFunctionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.aggregationFunction`.
     * @param ctx the parse tree
     */
    exitAggregationFunction?: (ctx: AggregationFunctionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.userVariable`.
     * @param ctx the parse tree
     */
    enterUserVariable?: (ctx: UserVariableContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.userVariable`.
     * @param ctx the parse tree
     */
    exitUserVariable?: (ctx: UserVariableContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.systemVariable`.
     * @param ctx the parse tree
     */
    enterSystemVariable?: (ctx: SystemVariableContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.systemVariable`.
     * @param ctx the parse tree
     */
    exitSystemVariable?: (ctx: SystemVariableContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.columnReference`.
     * @param ctx the parse tree
     */
    enterColumnReference?: (ctx: ColumnReferenceContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.columnReference`.
     * @param ctx the parse tree
     */
    exitColumnReference?: (ctx: ColumnReferenceContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.informationFunctionExpression`.
     * @param ctx the parse tree
     */
    enterInformationFunctionExpression?: (ctx: InformationFunctionExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.informationFunctionExpression`.
     * @param ctx the parse tree
     */
    exitInformationFunctionExpression?: (ctx: InformationFunctionExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.specialDateTimeExpression`.
     * @param ctx the parse tree
     */
    enterSpecialDateTimeExpression?: (ctx: SpecialDateTimeExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.specialDateTimeExpression`.
     * @param ctx the parse tree
     */
    exitSpecialDateTimeExpression?: (ctx: SpecialDateTimeExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.specialFunctionExpression`.
     * @param ctx the parse tree
     */
    enterSpecialFunctionExpression?: (ctx: SpecialFunctionExpressionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.specialFunctionExpression`.
     * @param ctx the parse tree
     */
    exitSpecialFunctionExpression?: (ctx: SpecialFunctionExpressionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.windowFunction`.
     * @param ctx the parse tree
     */
    enterWindowFunction?: (ctx: WindowFunctionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.windowFunction`.
     * @param ctx the parse tree
     */
    exitWindowFunction?: (ctx: WindowFunctionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.whenClause`.
     * @param ctx the parse tree
     */
    enterWhenClause?: (ctx: WhenClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.whenClause`.
     * @param ctx the parse tree
     */
    exitWhenClause?: (ctx: WhenClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.over`.
     * @param ctx the parse tree
     */
    enterOver?: (ctx: OverContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.over`.
     * @param ctx the parse tree
     */
    exitOver?: (ctx: OverContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.ignoreNulls`.
     * @param ctx the parse tree
     */
    enterIgnoreNulls?: (ctx: IgnoreNullsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.ignoreNulls`.
     * @param ctx the parse tree
     */
    exitIgnoreNulls?: (ctx: IgnoreNullsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.tableDesc`.
     * @param ctx the parse tree
     */
    enterTableDesc?: (ctx: TableDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.tableDesc`.
     * @param ctx the parse tree
     */
    exitTableDesc?: (ctx: TableDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.restoreTableDesc`.
     * @param ctx the parse tree
     */
    enterRestoreTableDesc?: (ctx: RestoreTableDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.restoreTableDesc`.
     * @param ctx the parse tree
     */
    exitRestoreTableDesc?: (ctx: RestoreTableDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.explainDesc`.
     * @param ctx the parse tree
     */
    enterExplainDesc?: (ctx: ExplainDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.explainDesc`.
     * @param ctx the parse tree
     */
    exitExplainDesc?: (ctx: ExplainDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.optimizerTrace`.
     * @param ctx the parse tree
     */
    enterOptimizerTrace?: (ctx: OptimizerTraceContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.optimizerTrace`.
     * @param ctx the parse tree
     */
    exitOptimizerTrace?: (ctx: OptimizerTraceContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.partitionDesc`.
     * @param ctx the parse tree
     */
    enterPartitionDesc?: (ctx: PartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.partitionDesc`.
     * @param ctx the parse tree
     */
    exitPartitionDesc?: (ctx: PartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.listPartitionDesc`.
     * @param ctx the parse tree
     */
    enterListPartitionDesc?: (ctx: ListPartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.listPartitionDesc`.
     * @param ctx the parse tree
     */
    exitListPartitionDesc?: (ctx: ListPartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.singleItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    enterSingleItemListPartitionDesc?: (ctx: SingleItemListPartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.singleItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    exitSingleItemListPartitionDesc?: (ctx: SingleItemListPartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.multiItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    enterMultiItemListPartitionDesc?: (ctx: MultiItemListPartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.multiItemListPartitionDesc`.
     * @param ctx the parse tree
     */
    exitMultiItemListPartitionDesc?: (ctx: MultiItemListPartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.stringList`.
     * @param ctx the parse tree
     */
    enterStringList?: (ctx: StringListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.stringList`.
     * @param ctx the parse tree
     */
    exitStringList?: (ctx: StringListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.rangePartitionDesc`.
     * @param ctx the parse tree
     */
    enterRangePartitionDesc?: (ctx: RangePartitionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.rangePartitionDesc`.
     * @param ctx the parse tree
     */
    exitRangePartitionDesc?: (ctx: RangePartitionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.singleRangePartition`.
     * @param ctx the parse tree
     */
    enterSingleRangePartition?: (ctx: SingleRangePartitionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.singleRangePartition`.
     * @param ctx the parse tree
     */
    exitSingleRangePartition?: (ctx: SingleRangePartitionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.multiRangePartition`.
     * @param ctx the parse tree
     */
    enterMultiRangePartition?: (ctx: MultiRangePartitionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.multiRangePartition`.
     * @param ctx the parse tree
     */
    exitMultiRangePartition?: (ctx: MultiRangePartitionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.partitionRangeDesc`.
     * @param ctx the parse tree
     */
    enterPartitionRangeDesc?: (ctx: PartitionRangeDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.partitionRangeDesc`.
     * @param ctx the parse tree
     */
    exitPartitionRangeDesc?: (ctx: PartitionRangeDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.partitionKeyDesc`.
     * @param ctx the parse tree
     */
    enterPartitionKeyDesc?: (ctx: PartitionKeyDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.partitionKeyDesc`.
     * @param ctx the parse tree
     */
    exitPartitionKeyDesc?: (ctx: PartitionKeyDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.partitionValueList`.
     * @param ctx the parse tree
     */
    enterPartitionValueList?: (ctx: PartitionValueListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.partitionValueList`.
     * @param ctx the parse tree
     */
    exitPartitionValueList?: (ctx: PartitionValueListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.keyPartition`.
     * @param ctx the parse tree
     */
    enterKeyPartition?: (ctx: KeyPartitionContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.keyPartition`.
     * @param ctx the parse tree
     */
    exitKeyPartition?: (ctx: KeyPartitionContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.partitionValue`.
     * @param ctx the parse tree
     */
    enterPartitionValue?: (ctx: PartitionValueContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.partitionValue`.
     * @param ctx the parse tree
     */
    exitPartitionValue?: (ctx: PartitionValueContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.distributionClause`.
     * @param ctx the parse tree
     */
    enterDistributionClause?: (ctx: DistributionClauseContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.distributionClause`.
     * @param ctx the parse tree
     */
    exitDistributionClause?: (ctx: DistributionClauseContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.distributionDesc`.
     * @param ctx the parse tree
     */
    enterDistributionDesc?: (ctx: DistributionDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.distributionDesc`.
     * @param ctx the parse tree
     */
    exitDistributionDesc?: (ctx: DistributionDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.refreshSchemeDesc`.
     * @param ctx the parse tree
     */
    enterRefreshSchemeDesc?: (ctx: RefreshSchemeDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.refreshSchemeDesc`.
     * @param ctx the parse tree
     */
    exitRefreshSchemeDesc?: (ctx: RefreshSchemeDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.statusDesc`.
     * @param ctx the parse tree
     */
    enterStatusDesc?: (ctx: StatusDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.statusDesc`.
     * @param ctx the parse tree
     */
    exitStatusDesc?: (ctx: StatusDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.properties`.
     * @param ctx the parse tree
     */
    enterProperties?: (ctx: PropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.properties`.
     * @param ctx the parse tree
     */
    exitProperties?: (ctx: PropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.extProperties`.
     * @param ctx the parse tree
     */
    enterExtProperties?: (ctx: ExtPropertiesContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.extProperties`.
     * @param ctx the parse tree
     */
    exitExtProperties?: (ctx: ExtPropertiesContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.propertyList`.
     * @param ctx the parse tree
     */
    enterPropertyList?: (ctx: PropertyListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.propertyList`.
     * @param ctx the parse tree
     */
    exitPropertyList?: (ctx: PropertyListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.userPropertyList`.
     * @param ctx the parse tree
     */
    enterUserPropertyList?: (ctx: UserPropertyListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.userPropertyList`.
     * @param ctx the parse tree
     */
    exitUserPropertyList?: (ctx: UserPropertyListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.property`.
     * @param ctx the parse tree
     */
    enterProperty?: (ctx: PropertyContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.property`.
     * @param ctx the parse tree
     */
    exitProperty?: (ctx: PropertyContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.varType`.
     * @param ctx the parse tree
     */
    enterVarType?: (ctx: VarTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.varType`.
     * @param ctx the parse tree
     */
    exitVarType?: (ctx: VarTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.comment`.
     * @param ctx the parse tree
     */
    enterComment?: (ctx: CommentContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.comment`.
     * @param ctx the parse tree
     */
    exitComment?: (ctx: CommentContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.outfile`.
     * @param ctx the parse tree
     */
    enterOutfile?: (ctx: OutfileContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.outfile`.
     * @param ctx the parse tree
     */
    exitOutfile?: (ctx: OutfileContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.fileFormat`.
     * @param ctx the parse tree
     */
    enterFileFormat?: (ctx: FileFormatContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.fileFormat`.
     * @param ctx the parse tree
     */
    exitFileFormat?: (ctx: FileFormatContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.string`.
     * @param ctx the parse tree
     */
    enterString?: (ctx: StringContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.string`.
     * @param ctx the parse tree
     */
    exitString?: (ctx: StringContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.binary`.
     * @param ctx the parse tree
     */
    enterBinary?: (ctx: BinaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.binary`.
     * @param ctx the parse tree
     */
    exitBinary?: (ctx: BinaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.comparisonOperator`.
     * @param ctx the parse tree
     */
    enterComparisonOperator?: (ctx: ComparisonOperatorContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.comparisonOperator`.
     * @param ctx the parse tree
     */
    exitComparisonOperator?: (ctx: ComparisonOperatorContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.booleanValue`.
     * @param ctx the parse tree
     */
    enterBooleanValue?: (ctx: BooleanValueContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.booleanValue`.
     * @param ctx the parse tree
     */
    exitBooleanValue?: (ctx: BooleanValueContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.interval`.
     * @param ctx the parse tree
     */
    enterInterval?: (ctx: IntervalContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.interval`.
     * @param ctx the parse tree
     */
    exitInterval?: (ctx: IntervalContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.unitIdentifier`.
     * @param ctx the parse tree
     */
    enterUnitIdentifier?: (ctx: UnitIdentifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.unitIdentifier`.
     * @param ctx the parse tree
     */
    exitUnitIdentifier?: (ctx: UnitIdentifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.unitBoundary`.
     * @param ctx the parse tree
     */
    enterUnitBoundary?: (ctx: UnitBoundaryContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.unitBoundary`.
     * @param ctx the parse tree
     */
    exitUnitBoundary?: (ctx: UnitBoundaryContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.type`.
     * @param ctx the parse tree
     */
    enterType?: (ctx: TypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.type`.
     * @param ctx the parse tree
     */
    exitType?: (ctx: TypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.arrayType`.
     * @param ctx the parse tree
     */
    enterArrayType?: (ctx: ArrayTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.arrayType`.
     * @param ctx the parse tree
     */
    exitArrayType?: (ctx: ArrayTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.mapType`.
     * @param ctx the parse tree
     */
    enterMapType?: (ctx: MapTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.mapType`.
     * @param ctx the parse tree
     */
    exitMapType?: (ctx: MapTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.subfieldDesc`.
     * @param ctx the parse tree
     */
    enterSubfieldDesc?: (ctx: SubfieldDescContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.subfieldDesc`.
     * @param ctx the parse tree
     */
    exitSubfieldDesc?: (ctx: SubfieldDescContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.subfieldDescs`.
     * @param ctx the parse tree
     */
    enterSubfieldDescs?: (ctx: SubfieldDescsContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.subfieldDescs`.
     * @param ctx the parse tree
     */
    exitSubfieldDescs?: (ctx: SubfieldDescsContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.structType`.
     * @param ctx the parse tree
     */
    enterStructType?: (ctx: StructTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.structType`.
     * @param ctx the parse tree
     */
    exitStructType?: (ctx: StructTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.typeParameter`.
     * @param ctx the parse tree
     */
    enterTypeParameter?: (ctx: TypeParameterContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.typeParameter`.
     * @param ctx the parse tree
     */
    exitTypeParameter?: (ctx: TypeParameterContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.baseType`.
     * @param ctx the parse tree
     */
    enterBaseType?: (ctx: BaseTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.baseType`.
     * @param ctx the parse tree
     */
    exitBaseType?: (ctx: BaseTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.decimalType`.
     * @param ctx the parse tree
     */
    enterDecimalType?: (ctx: DecimalTypeContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.decimalType`.
     * @param ctx the parse tree
     */
    exitDecimalType?: (ctx: DecimalTypeContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.qualifiedName`.
     * @param ctx the parse tree
     */
    enterQualifiedName?: (ctx: QualifiedNameContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.qualifiedName`.
     * @param ctx the parse tree
     */
    exitQualifiedName?: (ctx: QualifiedNameContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    enterIdentifier?: (ctx: IdentifierContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     */
    exitIdentifier?: (ctx: IdentifierContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.identifierList`.
     * @param ctx the parse tree
     */
    enterIdentifierList?: (ctx: IdentifierListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.identifierList`.
     * @param ctx the parse tree
     */
    exitIdentifierList?: (ctx: IdentifierListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.identifierOrString`.
     * @param ctx the parse tree
     */
    enterIdentifierOrString?: (ctx: IdentifierOrStringContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.identifierOrString`.
     * @param ctx the parse tree
     */
    exitIdentifierOrString?: (ctx: IdentifierOrStringContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.identifierOrStringList`.
     * @param ctx the parse tree
     */
    enterIdentifierOrStringList?: (ctx: IdentifierOrStringListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.identifierOrStringList`.
     * @param ctx the parse tree
     */
    exitIdentifierOrStringList?: (ctx: IdentifierOrStringListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.identifierOrStringOrStar`.
     * @param ctx the parse tree
     */
    enterIdentifierOrStringOrStar?: (ctx: IdentifierOrStringOrStarContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.identifierOrStringOrStar`.
     * @param ctx the parse tree
     */
    exitIdentifierOrStringOrStar?: (ctx: IdentifierOrStringOrStarContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    enterUser?: (ctx: UserContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     */
    exitUser?: (ctx: UserContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.assignment`.
     * @param ctx the parse tree
     */
    enterAssignment?: (ctx: AssignmentContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.assignment`.
     * @param ctx the parse tree
     */
    exitAssignment?: (ctx: AssignmentContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.assignmentList`.
     * @param ctx the parse tree
     */
    enterAssignmentList?: (ctx: AssignmentListContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.assignmentList`.
     * @param ctx the parse tree
     */
    exitAssignmentList?: (ctx: AssignmentListContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    enterNumber?: (ctx: NumberContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     */
    exitNumber?: (ctx: NumberContext) => void;
    /**
     * Enter a parse tree produced by `StarRocksSqlParser.nonReserved`.
     * @param ctx the parse tree
     */
    enterNonReserved?: (ctx: NonReservedContext) => void;
    /**
     * Exit a parse tree produced by `StarRocksSqlParser.nonReserved`.
     * @param ctx the parse tree
     */
    exitNonReserved?: (ctx: NonReservedContext) => void;
}
