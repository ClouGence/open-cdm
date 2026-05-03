import { ParseTreeVisitor } from "antlr4ts/tree/ParseTreeVisitor";
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
 * This interface defines a complete generic visitor for a parse tree produced
 * by `StarRocksSqlParser`.
 *
 * @param <Result> The return type of the visit operation. Use `void` for
 * operations with no return type.
 */
export interface StarRocksSqlParserVisitor<Result> extends ParseTreeVisitor<Result> {
    /**
     * Visit a parse tree produced by the `tableAtom`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableAtom?: (ctx: TableAtomContext) => Result;
    /**
     * Visit a parse tree produced by the `inlineTable`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInlineTable?: (ctx: InlineTableContext) => Result;
    /**
     * Visit a parse tree produced by the `subqueryWithAlias`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubqueryWithAlias?: (ctx: SubqueryWithAliasContext) => Result;
    /**
     * Visit a parse tree produced by the `tableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableFunction?: (ctx: TableFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `normalizedTableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNormalizedTableFunction?: (ctx: NormalizedTableFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `fileTableFunction`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFileTableFunction?: (ctx: FileTableFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `parenthesizedRelation`
     * labeled alternative in `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParenthesizedRelation?: (ctx: ParenthesizedRelationContext) => Result;
    /**
     * Visit a parse tree produced by the `decimalValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDecimalValue?: (ctx: DecimalValueContext) => Result;
    /**
     * Visit a parse tree produced by the `doubleValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDoubleValue?: (ctx: DoubleValueContext) => Result;
    /**
     * Visit a parse tree produced by the `integerValue`
     * labeled alternative in `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntegerValue?: (ctx: IntegerValueContext) => Result;
    /**
     * Visit a parse tree produced by the `extract`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExtract?: (ctx: ExtractContext) => Result;
    /**
     * Visit a parse tree produced by the `groupingOperation`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroupingOperation?: (ctx: GroupingOperationContext) => Result;
    /**
     * Visit a parse tree produced by the `informationFunction`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInformationFunction?: (ctx: InformationFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `specialDateTime`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialDateTime?: (ctx: SpecialDateTimeContext) => Result;
    /**
     * Visit a parse tree produced by the `specialFunction`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialFunction?: (ctx: SpecialFunctionContext) => Result;
    /**
     * Visit a parse tree produced by the `aggregationFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAggregationFunctionCall?: (ctx: AggregationFunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by the `windowFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWindowFunctionCall?: (ctx: WindowFunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by the `simpleFunctionCall`
     * labeled alternative in `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSimpleFunctionCall?: (ctx: SimpleFunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by the `from`
     * labeled alternative in `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFrom?: (ctx: FromContext) => Result;
    /**
     * Visit a parse tree produced by the `dual`
     * labeled alternative in `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDual?: (ctx: DualContext) => Result;
    /**
     * Visit a parse tree produced by the `setNames`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetNames?: (ctx: SetNamesContext) => Result;
    /**
     * Visit a parse tree produced by the `setPassword`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetPassword?: (ctx: SetPasswordContext) => Result;
    /**
     * Visit a parse tree produced by the `setUserVar`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetUserVar?: (ctx: SetUserVarContext) => Result;
    /**
     * Visit a parse tree produced by the `setSystemVar`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetSystemVar?: (ctx: SetSystemVarContext) => Result;
    /**
     * Visit a parse tree produced by the `setTransaction`
     * labeled alternative in `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetTransaction?: (ctx: SetTransactionContext) => Result;
    /**
     * Visit a parse tree produced by the `keyPartitionList`
     * labeled alternative in `StarRocksSqlParser.keyPartitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyPartitionList?: (ctx: KeyPartitionListContext) => Result;
    /**
     * Visit a parse tree produced by the `inSubquery`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInSubquery?: (ctx: InSubqueryContext) => Result;
    /**
     * Visit a parse tree produced by the `inList`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInList?: (ctx: InListContext) => Result;
    /**
     * Visit a parse tree produced by the `between`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBetween?: (ctx: BetweenContext) => Result;
    /**
     * Visit a parse tree produced by the `like`
     * labeled alternative in `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLike?: (ctx: LikeContext) => Result;
    /**
     * Visit a parse tree produced by the `grantRoleToUser`
     * labeled alternative in `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRoleToUser?: (ctx: GrantRoleToUserContext) => Result;
    /**
     * Visit a parse tree produced by the `grantRoleToRole`
     * labeled alternative in `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRoleToRole?: (ctx: GrantRoleToRoleContext) => Result;
    /**
     * Visit a parse tree produced by the `booleanExpressionDefault`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanExpressionDefault?: (ctx: BooleanExpressionDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `isNull`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIsNull?: (ctx: IsNullContext) => Result;
    /**
     * Visit a parse tree produced by the `comparison`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComparison?: (ctx: ComparisonContext) => Result;
    /**
     * Visit a parse tree produced by the `scalarSubquery`
     * labeled alternative in `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitScalarSubquery?: (ctx: ScalarSubqueryContext) => Result;
    /**
     * Visit a parse tree produced by the `nullLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNullLiteral?: (ctx: NullLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `booleanLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanLiteral?: (ctx: BooleanLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `numericLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumericLiteral?: (ctx: NumericLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `dateLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDateLiteral?: (ctx: DateLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `stringLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStringLiteral?: (ctx: StringLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `intervalLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntervalLiteral?: (ctx: IntervalLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `unitBoundaryLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnitBoundaryLiteral?: (ctx: UnitBoundaryLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `binaryLiteral`
     * labeled alternative in `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBinaryLiteral?: (ctx: BinaryLiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnUser`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnUser?: (ctx: GrantOnUserContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnTableBrief`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnTableBrief?: (ctx: GrantOnTableBriefContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnFunc`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnFunc?: (ctx: GrantOnFuncContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnSystem`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnSystem?: (ctx: GrantOnSystemContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnPrimaryObj`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnPrimaryObj?: (ctx: GrantOnPrimaryObjContext) => Result;
    /**
     * Visit a parse tree produced by the `grantOnAll`
     * labeled alternative in `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantOnAll?: (ctx: GrantOnAllContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnUser`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnUser?: (ctx: RevokeOnUserContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnTableBrief`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnTableBrief?: (ctx: RevokeOnTableBriefContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnFunc`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnFunc?: (ctx: RevokeOnFuncContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnSystem`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnSystem?: (ctx: RevokeOnSystemContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnPrimaryObj`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnPrimaryObj?: (ctx: RevokeOnPrimaryObjContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeOnAll`
     * labeled alternative in `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeOnAll?: (ctx: RevokeOnAllContext) => Result;
    /**
     * Visit a parse tree produced by the `rollup`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollup?: (ctx: RollupContext) => Result;
    /**
     * Visit a parse tree produced by the `cube`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCube?: (ctx: CubeContext) => Result;
    /**
     * Visit a parse tree produced by the `multipleGroupingSets`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultipleGroupingSets?: (ctx: MultipleGroupingSetsContext) => Result;
    /**
     * Visit a parse tree produced by the `singleGroupingSet`
     * labeled alternative in `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleGroupingSet?: (ctx: SingleGroupingSetContext) => Result;
    /**
     * Visit a parse tree produced by the `userVariableExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserVariableExpression?: (ctx: UserVariableExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `systemVariableExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystemVariableExpression?: (ctx: SystemVariableExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `functionCallExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunctionCallExpression?: (ctx: FunctionCallExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `odbcFunctionCallExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOdbcFunctionCallExpression?: (ctx: OdbcFunctionCallExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `collate`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollate?: (ctx: CollateContext) => Result;
    /**
     * Visit a parse tree produced by the `literal`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLiteral?: (ctx: LiteralContext) => Result;
    /**
     * Visit a parse tree produced by the `columnRef`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnRef?: (ctx: ColumnRefContext) => Result;
    /**
     * Visit a parse tree produced by the `dereference`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDereference?: (ctx: DereferenceContext) => Result;
    /**
     * Visit a parse tree produced by the `concat`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConcat?: (ctx: ConcatContext) => Result;
    /**
     * Visit a parse tree produced by the `arithmeticUnary`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArithmeticUnary?: (ctx: ArithmeticUnaryContext) => Result;
    /**
     * Visit a parse tree produced by the `parenthesizedExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitParenthesizedExpression?: (ctx: ParenthesizedExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `exists`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExists?: (ctx: ExistsContext) => Result;
    /**
     * Visit a parse tree produced by the `subqueryExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubqueryExpression?: (ctx: SubqueryExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `cast`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCast?: (ctx: CastContext) => Result;
    /**
     * Visit a parse tree produced by the `convert`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitConvert?: (ctx: ConvertContext) => Result;
    /**
     * Visit a parse tree produced by the `simpleCase`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSimpleCase?: (ctx: SimpleCaseContext) => Result;
    /**
     * Visit a parse tree produced by the `searchedCase`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSearchedCase?: (ctx: SearchedCaseContext) => Result;
    /**
     * Visit a parse tree produced by the `arrayConstructor`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArrayConstructor?: (ctx: ArrayConstructorContext) => Result;
    /**
     * Visit a parse tree produced by the `mapConstructor`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapConstructor?: (ctx: MapConstructorContext) => Result;
    /**
     * Visit a parse tree produced by the `collectionSubscript`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollectionSubscript?: (ctx: CollectionSubscriptContext) => Result;
    /**
     * Visit a parse tree produced by the `arraySlice`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArraySlice?: (ctx: ArraySliceContext) => Result;
    /**
     * Visit a parse tree produced by the `arrowExpression`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArrowExpression?: (ctx: ArrowExpressionContext) => Result;
    /**
     * Visit a parse tree produced by the `lambdaFunctionExpr`
     * labeled alternative in `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLambdaFunctionExpr?: (ctx: LambdaFunctionExprContext) => Result;
    /**
     * Visit a parse tree produced by the `expressionDefault`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionDefault?: (ctx: ExpressionDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `logicalNot`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogicalNot?: (ctx: LogicalNotContext) => Result;
    /**
     * Visit a parse tree produced by the `logicalBinary`
     * labeled alternative in `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLogicalBinary?: (ctx: LogicalBinaryContext) => Result;
    /**
     * Visit a parse tree produced by the `showAllAuthentication`
     * labeled alternative in `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAllAuthentication?: (ctx: ShowAllAuthenticationContext) => Result;
    /**
     * Visit a parse tree produced by the `showAuthenticationForUser`
     * labeled alternative in `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAuthenticationForUser?: (ctx: ShowAuthenticationForUserContext) => Result;
    /**
     * Visit a parse tree produced by the `unquotedIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnquotedIdentifier?: (ctx: UnquotedIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by the `digitIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDigitIdentifier?: (ctx: DigitIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by the `backQuotedIdentifier`
     * labeled alternative in `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBackQuotedIdentifier?: (ctx: BackQuotedIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by the `valueExpressionDefault`
     * labeled alternative in `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitValueExpressionDefault?: (ctx: ValueExpressionDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `arithmeticBinary`
     * labeled alternative in `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArithmeticBinary?: (ctx: ArithmeticBinaryContext) => Result;
    /**
     * Visit a parse tree produced by the `queryPrimaryDefault`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryPrimaryDefault?: (ctx: QueryPrimaryDefaultContext) => Result;
    /**
     * Visit a parse tree produced by the `queryWithParentheses`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryWithParentheses?: (ctx: QueryWithParenthesesContext) => Result;
    /**
     * Visit a parse tree produced by the `setOperation`
     * labeled alternative in `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetOperation?: (ctx: SetOperationContext) => Result;
    /**
     * Visit a parse tree produced by the `selectSingle`
     * labeled alternative in `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelectSingle?: (ctx: SelectSingleContext) => Result;
    /**
     * Visit a parse tree produced by the `selectAll`
     * labeled alternative in `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelectAll?: (ctx: SelectAllContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeRoleFromUser`
     * labeled alternative in `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeRoleFromUser?: (ctx: RevokeRoleFromUserContext) => Result;
    /**
     * Visit a parse tree produced by the `revokeRoleFromRole`
     * labeled alternative in `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeRoleFromRole?: (ctx: RevokeRoleFromRoleContext) => Result;
    /**
     * Visit a parse tree produced by the `authWithoutPlugin`
     * labeled alternative in `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuthWithoutPlugin?: (ctx: AuthWithoutPluginContext) => Result;
    /**
     * Visit a parse tree produced by the `authWithPlugin`
     * labeled alternative in `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuthWithPlugin?: (ctx: AuthWithPluginContext) => Result;
    /**
     * Visit a parse tree produced by the `userWithoutHost`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserWithoutHost?: (ctx: UserWithoutHostContext) => Result;
    /**
     * Visit a parse tree produced by the `userWithHost`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserWithHost?: (ctx: UserWithHostContext) => Result;
    /**
     * Visit a parse tree produced by the `userWithHostAndBlanket`
     * labeled alternative in `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserWithHostAndBlanket?: (ctx: UserWithHostAndBlanketContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.program`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProgram?: (ctx: ProgramContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.sqlStatements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlStatements?: (ctx: SqlStatementsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.singleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleStatement?: (ctx: SingleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.statement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatement?: (ctx: StatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.useDatabaseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUseDatabaseStatement?: (ctx: UseDatabaseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.useCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUseCatalogStatement?: (ctx: UseCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetCatalogStatement?: (ctx: SetCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showDatabasesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDatabasesStatement?: (ctx: ShowDatabasesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterDbQuotaStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterDbQuotaStatement?: (ctx: AlterDbQuotaStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createDbStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateDbStatement?: (ctx: CreateDbStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropDbStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropDbStatement?: (ctx: DropDbStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCreateDbStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateDbStatement?: (ctx: ShowCreateDbStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterDatabaseRenameStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterDatabaseRenameStatement?: (ctx: AlterDatabaseRenameStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.recoverDbStmt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecoverDbStmt?: (ctx: RecoverDbStmtContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showDataStmt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDataStmt?: (ctx: ShowDataStmtContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTableStatement?: (ctx: CreateTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.columnDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnDesc?: (ctx: ColumnDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.charsetName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCharsetName?: (ctx: CharsetNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.defaultDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDefaultDesc?: (ctx: DefaultDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.materializedColumnDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaterializedColumnDesc?: (ctx: MaterializedColumnDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.indexDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndexDesc?: (ctx: IndexDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.engineDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEngineDesc?: (ctx: EngineDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.charsetDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCharsetDesc?: (ctx: CharsetDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.collateDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCollateDesc?: (ctx: CollateDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.keyDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyDesc?: (ctx: KeyDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.orderByDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOrderByDesc?: (ctx: OrderByDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.aggDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAggDesc?: (ctx: AggDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.rollupDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollupDesc?: (ctx: RollupDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.rollupItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollupItem?: (ctx: RollupItemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dupKeys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDupKeys?: (ctx: DupKeysContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.fromRollup`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFromRollup?: (ctx: FromRollupContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.withMaskingPolicy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithMaskingPolicy?: (ctx: WithMaskingPolicyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.withRowAccessPolicy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithRowAccessPolicy?: (ctx: WithRowAccessPolicyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createTemporaryTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTemporaryTableStatement?: (ctx: CreateTemporaryTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createTableAsSelectStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTableAsSelectStatement?: (ctx: CreateTableAsSelectStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropTableStatement?: (ctx: DropTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterTableStatement?: (ctx: AlterTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createIndexStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateIndexStatement?: (ctx: CreateIndexStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropIndexStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropIndexStatement?: (ctx: DropIndexStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.indexType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIndexType?: (ctx: IndexTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTableStatement?: (ctx: ShowTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCreateTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateTableStatement?: (ctx: ShowCreateTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showColumnStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowColumnStatement?: (ctx: ShowColumnStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showTableStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTableStatusStatement?: (ctx: ShowTableStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.refreshTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshTableStatement?: (ctx: RefreshTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showAlterStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAlterStatement?: (ctx: ShowAlterStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.descTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDescTableStatement?: (ctx: DescTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createTableLikeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateTableLikeStatement?: (ctx: CreateTableLikeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showIndexStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowIndexStatement?: (ctx: ShowIndexStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.recoverTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecoverTableStatement?: (ctx: RecoverTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.truncateTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTruncateTableStatement?: (ctx: TruncateTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelAlterTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelAlterTableStatement?: (ctx: CancelAlterTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showPartitionsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowPartitionsStatement?: (ctx: ShowPartitionsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.recoverPartitionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRecoverPartitionStatement?: (ctx: RecoverPartitionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateViewStatement?: (ctx: CreateViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterViewStatement?: (ctx: AlterViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropViewStatement?: (ctx: DropViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.columnNameWithComment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnNameWithComment?: (ctx: ColumnNameWithCommentContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.submitTaskStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubmitTaskStatement?: (ctx: SubmitTaskStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.taskExecSql`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTaskExecSql?: (ctx: TaskExecSqlContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropTaskStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropTaskStatement?: (ctx: DropTaskStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateMaterializedViewStatement?: (ctx: CreateMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.materializedViewDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaterializedViewDesc?: (ctx: MaterializedViewDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showMaterializedViewsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowMaterializedViewsStatement?: (ctx: ShowMaterializedViewsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropMaterializedViewStatement?: (ctx: DropMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterMaterializedViewStatement?: (ctx: AlterMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.refreshMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshMaterializedViewStatement?: (ctx: RefreshMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelRefreshMaterializedViewStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelRefreshMaterializedViewStatement?: (ctx: CancelRefreshMaterializedViewStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminSetConfigStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminSetConfigStatement?: (ctx: AdminSetConfigStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminSetReplicaStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminSetReplicaStatusStatement?: (ctx: AdminSetReplicaStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminShowConfigStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminShowConfigStatement?: (ctx: AdminShowConfigStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminShowReplicaDistributionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminShowReplicaDistributionStatement?: (ctx: AdminShowReplicaDistributionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminShowReplicaStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminShowReplicaStatusStatement?: (ctx: AdminShowReplicaStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminRepairTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminRepairTableStatement?: (ctx: AdminRepairTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminCancelRepairTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminCancelRepairTableStatement?: (ctx: AdminCancelRepairTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.adminCheckTabletsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAdminCheckTabletsStatement?: (ctx: AdminCheckTabletsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.killStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKillStatement?: (ctx: KillStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.syncStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSyncStatement?: (ctx: SyncStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterSystemStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterSystemStatement?: (ctx: AlterSystemStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelAlterSystemStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelAlterSystemStatement?: (ctx: CancelAlterSystemStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showComputeNodesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowComputeNodesStatement?: (ctx: ShowComputeNodesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createExternalCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateExternalCatalogStatement?: (ctx: CreateExternalCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCreateExternalCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateExternalCatalogStatement?: (ctx: ShowCreateExternalCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropExternalCatalogStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropExternalCatalogStatement?: (ctx: DropExternalCatalogStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCatalogsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCatalogsStatement?: (ctx: ShowCatalogsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateWarehouseStatement?: (ctx: CreateWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showWarehousesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowWarehousesStatement?: (ctx: ShowWarehousesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropWarehouseStatement?: (ctx: DropWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterWarehouseStatement?: (ctx: AlterWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showClustersStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowClustersStatement?: (ctx: ShowClustersStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.suspendWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSuspendWarehouseStatement?: (ctx: SuspendWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.resumeWarehouseStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResumeWarehouseStatement?: (ctx: ResumeWarehouseStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateStorageVolumeStatement?: (ctx: CreateStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.typeDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTypeDesc?: (ctx: TypeDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.locationsDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLocationsDesc?: (ctx: LocationsDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showStorageVolumesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStorageVolumesStatement?: (ctx: ShowStorageVolumesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropStorageVolumeStatement?: (ctx: DropStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterStorageVolumeStatement?: (ctx: AlterStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterStorageVolumeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterStorageVolumeClause?: (ctx: AlterStorageVolumeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyStorageVolumePropertiesClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyStorageVolumePropertiesClause?: (ctx: ModifyStorageVolumePropertiesClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyStorageVolumeCommentClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyStorageVolumeCommentClause?: (ctx: ModifyStorageVolumeCommentClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.descStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDescStorageVolumeStatement?: (ctx: DescStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setDefaultStorageVolumeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetDefaultStorageVolumeStatement?: (ctx: SetDefaultStorageVolumeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterClause?: (ctx: AlterClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.addFrontendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddFrontendClause?: (ctx: AddFrontendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropFrontendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropFrontendClause?: (ctx: DropFrontendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyFrontendHostClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyFrontendHostClause?: (ctx: ModifyFrontendHostClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.addBackendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddBackendClause?: (ctx: AddBackendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropBackendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropBackendClause?: (ctx: DropBackendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.decommissionBackendClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDecommissionBackendClause?: (ctx: DecommissionBackendClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyBackendHostClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyBackendHostClause?: (ctx: ModifyBackendHostClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.addComputeNodeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddComputeNodeClause?: (ctx: AddComputeNodeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropComputeNodeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropComputeNodeClause?: (ctx: DropComputeNodeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyBrokerClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyBrokerClause?: (ctx: ModifyBrokerClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterLoadErrorUrlClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterLoadErrorUrlClause?: (ctx: AlterLoadErrorUrlClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createImageClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateImageClause?: (ctx: CreateImageClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cleanTabletSchedQClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCleanTabletSchedQClause?: (ctx: CleanTabletSchedQClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createIndexClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateIndexClause?: (ctx: CreateIndexClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropIndexClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropIndexClause?: (ctx: DropIndexClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.tableRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableRenameClause?: (ctx: TableRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.swapTableClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSwapTableClause?: (ctx: SwapTableClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyTablePropertiesClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyTablePropertiesClause?: (ctx: ModifyTablePropertiesClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyCommentClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyCommentClause?: (ctx: ModifyCommentClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.addColumnClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddColumnClause?: (ctx: AddColumnClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.addColumnsClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddColumnsClause?: (ctx: AddColumnsClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropColumnClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropColumnClause?: (ctx: DropColumnClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyColumnClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyColumnClause?: (ctx: ModifyColumnClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.columnRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnRenameClause?: (ctx: ColumnRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.reorderColumnsClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReorderColumnsClause?: (ctx: ReorderColumnsClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.rollupRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRollupRenameClause?: (ctx: RollupRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.compactionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCompactionClause?: (ctx: CompactionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.applyMaskingPolicyClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitApplyMaskingPolicyClause?: (ctx: ApplyMaskingPolicyClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.applyRowAccessPolicyClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitApplyRowAccessPolicyClause?: (ctx: ApplyRowAccessPolicyClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.addPartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddPartitionClause?: (ctx: AddPartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropPartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropPartitionClause?: (ctx: DropPartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.truncatePartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTruncatePartitionClause?: (ctx: TruncatePartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.modifyPartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitModifyPartitionClause?: (ctx: ModifyPartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.replacePartitionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitReplacePartitionClause?: (ctx: ReplacePartitionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.partitionRenameClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionRenameClause?: (ctx: PartitionRenameClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.insertStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInsertStatement?: (ctx: InsertStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.updateStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUpdateStatement?: (ctx: UpdateStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.deleteStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDeleteStatement?: (ctx: DeleteStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRoutineLoadStatement?: (ctx: CreateRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRoutineLoadStatement?: (ctx: AlterRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dataSource`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataSource?: (ctx: DataSourceContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.loadPropertiesExpr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLoadPropertiesExpr?: (ctx: LoadPropertiesExprContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.loadProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLoadProperties?: (ctx: LoadPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.colSeparatorProperty`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColSeparatorProperty?: (ctx: ColSeparatorPropertyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.rowDelimiterProperty`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRowDelimiterProperty?: (ctx: RowDelimiterPropertyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.importColumns`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitImportColumns?: (ctx: ImportColumnsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.columnProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnProperties?: (ctx: ColumnPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.jobProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJobProperties?: (ctx: JobPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dataSourceProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataSourceProperties?: (ctx: DataSourcePropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.stopRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStopRoutineLoadStatement?: (ctx: StopRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.resumeRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResumeRoutineLoadStatement?: (ctx: ResumeRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.pauseRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPauseRoutineLoadStatement?: (ctx: PauseRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showRoutineLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRoutineLoadStatement?: (ctx: ShowRoutineLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showRoutineLoadTaskStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRoutineLoadTaskStatement?: (ctx: ShowRoutineLoadTaskStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showStreamLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStreamLoadStatement?: (ctx: ShowStreamLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.analyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnalyzeStatement?: (ctx: AnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropStatsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropStatsStatement?: (ctx: DropStatsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.analyzeHistogramStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnalyzeHistogramStatement?: (ctx: AnalyzeHistogramStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropHistogramStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropHistogramStatement?: (ctx: DropHistogramStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createAnalyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateAnalyzeStatement?: (ctx: CreateAnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropAnalyzeJobStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropAnalyzeJobStatement?: (ctx: DropAnalyzeJobStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showAnalyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAnalyzeStatement?: (ctx: ShowAnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showStatsMetaStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStatsMetaStatement?: (ctx: ShowStatsMetaStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showHistogramMetaStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowHistogramMetaStatement?: (ctx: ShowHistogramMetaStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.killAnalyzeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKillAnalyzeStatement?: (ctx: KillAnalyzeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.analyzeProfileStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAnalyzeProfileStatement?: (ctx: AnalyzeProfileStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateResourceGroupStatement?: (ctx: CreateResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropResourceGroupStatement?: (ctx: DropResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterResourceGroupStatement?: (ctx: AlterResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showResourceGroupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowResourceGroupStatement?: (ctx: ShowResourceGroupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateResourceStatement?: (ctx: CreateResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterResourceStatement?: (ctx: AlterResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropResourceStatement?: (ctx: DropResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showResourceStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowResourceStatement?: (ctx: ShowResourceStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.classifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitClassifier?: (ctx: ClassifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showFunctionsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowFunctionsStatement?: (ctx: ShowFunctionsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropFunctionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropFunctionStatement?: (ctx: DropFunctionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createFunctionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateFunctionStatement?: (ctx: CreateFunctionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.typeList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTypeList?: (ctx: TypeListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.loadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLoadStatement?: (ctx: LoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.labelName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLabelName?: (ctx: LabelNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dataDescList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataDescList?: (ctx: DataDescListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dataDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDataDesc?: (ctx: DataDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.formatProps`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFormatProps?: (ctx: FormatPropsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.brokerDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBrokerDesc?: (ctx: BrokerDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.resourceDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitResourceDesc?: (ctx: ResourceDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowLoadStatement?: (ctx: ShowLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showLoadWarningsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowLoadWarningsStatement?: (ctx: ShowLoadWarningsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelLoadStatement?: (ctx: CancelLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterLoadStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterLoadStatement?: (ctx: AlterLoadStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelCompactionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelCompactionStatement?: (ctx: CancelCompactionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showAuthorStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAuthorStatement?: (ctx: ShowAuthorStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showBackendsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowBackendsStatement?: (ctx: ShowBackendsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showBrokerStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowBrokerStatement?: (ctx: ShowBrokerStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCharsetStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCharsetStatement?: (ctx: ShowCharsetStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCollationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCollationStatement?: (ctx: ShowCollationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showDeleteStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDeleteStatement?: (ctx: ShowDeleteStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showDynamicPartitionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowDynamicPartitionStatement?: (ctx: ShowDynamicPartitionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showEventsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowEventsStatement?: (ctx: ShowEventsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showEnginesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowEnginesStatement?: (ctx: ShowEnginesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showFrontendsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowFrontendsStatement?: (ctx: ShowFrontendsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showPluginsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowPluginsStatement?: (ctx: ShowPluginsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showRepositoriesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRepositoriesStatement?: (ctx: ShowRepositoriesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showOpenTableStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowOpenTableStatement?: (ctx: ShowOpenTableStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showPrivilegesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowPrivilegesStatement?: (ctx: ShowPrivilegesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showProcedureStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProcedureStatement?: (ctx: ShowProcedureStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showProcStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProcStatement?: (ctx: ShowProcStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showProcesslistStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProcesslistStatement?: (ctx: ShowProcesslistStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showProfilelistStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowProfilelistStatement?: (ctx: ShowProfilelistStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showStatusStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowStatusStatement?: (ctx: ShowStatusStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showTabletStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTabletStatement?: (ctx: ShowTabletStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showTransactionStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTransactionStatement?: (ctx: ShowTransactionStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showTriggersStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowTriggersStatement?: (ctx: ShowTriggersStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showUserPropertyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowUserPropertyStatement?: (ctx: ShowUserPropertyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showVariablesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowVariablesStatement?: (ctx: ShowVariablesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showWarningStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowWarningStatement?: (ctx: ShowWarningStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.helpStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHelpStatement?: (ctx: HelpStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateUserStatement?: (ctx: CreateUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropUserStatement?: (ctx: DropUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterUserStatement?: (ctx: AlterUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showUserStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowUserStatement?: (ctx: ShowUserStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showAuthenticationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowAuthenticationStatement?: (ctx: ShowAuthenticationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.executeAsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExecuteAsStatement?: (ctx: ExecuteAsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRoleStatement?: (ctx: CreateRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRoleStatement?: (ctx: AlterRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRoleStatement?: (ctx: DropRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showRolesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRolesStatement?: (ctx: ShowRolesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.grantRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRoleStatement?: (ctx: GrantRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.revokeRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokeRoleStatement?: (ctx: RevokeRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetRoleStatement?: (ctx: SetRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setDefaultRoleStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetDefaultRoleStatement?: (ctx: SetDefaultRoleStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.grantRevokeClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantRevokeClause?: (ctx: GrantRevokeClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.grantPrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGrantPrivilegeStatement?: (ctx: GrantPrivilegeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.revokePrivilegeStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRevokePrivilegeStatement?: (ctx: RevokePrivilegeStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showGrantsStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowGrantsStatement?: (ctx: ShowGrantsStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateSecurityIntegrationStatement?: (ctx: CreateSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterSecurityIntegrationStatement?: (ctx: AlterSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropSecurityIntegrationStatement?: (ctx: DropSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSecurityIntegrationStatement?: (ctx: ShowSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCreateSecurityIntegrationStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateSecurityIntegrationStatement?: (ctx: ShowCreateSecurityIntegrationStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRoleMappingStatement?: (ctx: CreateRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRoleMappingStatement?: (ctx: AlterRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRoleMappingStatement?: (ctx: DropRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRoleMappingStatement?: (ctx: ShowRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.refreshRoleMappingStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshRoleMappingStatement?: (ctx: RefreshRoleMappingStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.authOption`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAuthOption?: (ctx: AuthOptionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.privObjectName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectName?: (ctx: PrivObjectNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.privObjectNameList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectNameList?: (ctx: PrivObjectNameListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.privFunctionObjectNameList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivFunctionObjectNameList?: (ctx: PrivFunctionObjectNameListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.privilegeTypeList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivilegeTypeList?: (ctx: PrivilegeTypeListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.privilegeType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivilegeType?: (ctx: PrivilegeTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.privObjectType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectType?: (ctx: PrivObjectTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.privObjectTypePlural`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrivObjectTypePlural?: (ctx: PrivObjectTypePluralContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateMaskingPolicyStatement?: (ctx: CreateMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropMaskingPolicyStatement?: (ctx: DropMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterMaskingPolicyStatement?: (ctx: AlterMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowMaskingPolicyStatement?: (ctx: ShowMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCreateMaskingPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateMaskingPolicyStatement?: (ctx: ShowCreateMaskingPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRowAccessPolicyStatement?: (ctx: CreateRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRowAccessPolicyStatement?: (ctx: DropRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.alterRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAlterRowAccessPolicyStatement?: (ctx: AlterRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRowAccessPolicyStatement?: (ctx: ShowRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showCreateRowAccessPolicyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowCreateRowAccessPolicyStatement?: (ctx: ShowCreateRowAccessPolicyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.policySignature`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPolicySignature?: (ctx: PolicySignatureContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.backupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBackupStatement?: (ctx: BackupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelBackupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelBackupStatement?: (ctx: CancelBackupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showBackupStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowBackupStatement?: (ctx: ShowBackupStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.restoreStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRestoreStatement?: (ctx: RestoreStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelRestoreStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelRestoreStatement?: (ctx: CancelRestoreStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showRestoreStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowRestoreStatement?: (ctx: ShowRestoreStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showSnapshotStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSnapshotStatement?: (ctx: ShowSnapshotStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createRepositoryStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateRepositoryStatement?: (ctx: CreateRepositoryStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropRepositoryStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropRepositoryStatement?: (ctx: DropRepositoryStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.addSqlBlackListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAddSqlBlackListStatement?: (ctx: AddSqlBlackListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.delSqlBlackListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDelSqlBlackListStatement?: (ctx: DelSqlBlackListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showSqlBlackListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSqlBlackListStatement?: (ctx: ShowSqlBlackListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showWhiteListStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowWhiteListStatement?: (ctx: ShowWhiteListStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.exportStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExportStatement?: (ctx: ExportStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.cancelExportStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCancelExportStatement?: (ctx: CancelExportStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showExportStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowExportStatement?: (ctx: ShowExportStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.installPluginStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInstallPluginStatement?: (ctx: InstallPluginStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.uninstallPluginStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUninstallPluginStatement?: (ctx: UninstallPluginStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.createFileStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCreateFileStatement?: (ctx: CreateFileStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.dropFileStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDropFileStatement?: (ctx: DropFileStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.showSmallFilesStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitShowSmallFilesStatement?: (ctx: ShowSmallFilesStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetStatement?: (ctx: SetStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setVar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetVar?: (ctx: SetVarContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.transaction_characteristics`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTransaction_characteristics?: (ctx: Transaction_characteristicsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.transaction_access_mode`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTransaction_access_mode?: (ctx: Transaction_access_modeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.isolation_level`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIsolation_level?: (ctx: Isolation_levelContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.isolation_types`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIsolation_types?: (ctx: Isolation_typesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setExprOrDefault`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetExprOrDefault?: (ctx: SetExprOrDefaultContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setUserPropertyStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetUserPropertyStatement?: (ctx: SetUserPropertyStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.roleList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRoleList?: (ctx: RoleListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.executeScriptStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExecuteScriptStatement?: (ctx: ExecuteScriptStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.unsupportedStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnsupportedStatement?: (ctx: UnsupportedStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.lock_item`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLock_item?: (ctx: Lock_itemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.lock_type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLock_type?: (ctx: Lock_typeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.queryStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryStatement?: (ctx: QueryStatementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.queryRelation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryRelation?: (ctx: QueryRelationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.withClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWithClause?: (ctx: WithClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.queryNoWith`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryNoWith?: (ctx: QueryNoWithContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.temporalClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTemporalClause?: (ctx: TemporalClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.queryPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQueryPrimary?: (ctx: QueryPrimaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.subquery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubquery?: (ctx: SubqueryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.rowConstructor`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRowConstructor?: (ctx: RowConstructorContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.sortItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSortItem?: (ctx: SortItemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.limitElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLimitElement?: (ctx: LimitElementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.querySpecification`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQuerySpecification?: (ctx: QuerySpecificationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.fromClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFromClause?: (ctx: FromClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.groupingElement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroupingElement?: (ctx: GroupingElementContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.groupingSet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGroupingSet?: (ctx: GroupingSetContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.commonTableExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCommonTableExpression?: (ctx: CommonTableExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setQuantifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetQuantifier?: (ctx: SetQuantifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.selectItem`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSelectItem?: (ctx: SelectItemContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.relations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelations?: (ctx: RelationsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.relation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelation?: (ctx: RelationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.relationPrimary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRelationPrimary?: (ctx: RelationPrimaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.joinRelation`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoinRelation?: (ctx: JoinRelationContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.crossOrInnerJoinType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCrossOrInnerJoinType?: (ctx: CrossOrInnerJoinTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.outerAndSemiJoinType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOuterAndSemiJoinType?: (ctx: OuterAndSemiJoinTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.bracketHint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBracketHint?: (ctx: BracketHintContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.setVarHint`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSetVarHint?: (ctx: SetVarHintContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.hintMap`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitHintMap?: (ctx: HintMapContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.joinCriteria`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitJoinCriteria?: (ctx: JoinCriteriaContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.columnAliases`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnAliases?: (ctx: ColumnAliasesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.partitionNames`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionNames?: (ctx: PartitionNamesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.keyPartitions`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyPartitions?: (ctx: KeyPartitionsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.tabletList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTabletList?: (ctx: TabletListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.expressionsWithDefault`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionsWithDefault?: (ctx: ExpressionsWithDefaultContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.expressionOrDefault`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionOrDefault?: (ctx: ExpressionOrDefaultContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.mapExpressionList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapExpressionList?: (ctx: MapExpressionListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.mapExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapExpression?: (ctx: MapExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.expressionSingleton`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionSingleton?: (ctx: ExpressionSingletonContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.expression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpression?: (ctx: ExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.expressionList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExpressionList?: (ctx: ExpressionListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.booleanExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanExpression?: (ctx: BooleanExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.predicate`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPredicate?: (ctx: PredicateContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.tupleInSubquery`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTupleInSubquery?: (ctx: TupleInSubqueryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.predicateOperations`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPredicateOperations?: (ctx: PredicateOperationsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.valueExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitValueExpression?: (ctx: ValueExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.primaryExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPrimaryExpression?: (ctx: PrimaryExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.literalExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLiteralExpression?: (ctx: LiteralExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.functionCall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFunctionCall?: (ctx: FunctionCallContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.aggregationFunction`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAggregationFunction?: (ctx: AggregationFunctionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.userVariable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserVariable?: (ctx: UserVariableContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.systemVariable`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSystemVariable?: (ctx: SystemVariableContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.columnReference`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitColumnReference?: (ctx: ColumnReferenceContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.informationFunctionExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInformationFunctionExpression?: (ctx: InformationFunctionExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.specialDateTimeExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialDateTimeExpression?: (ctx: SpecialDateTimeExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.specialFunctionExpression`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSpecialFunctionExpression?: (ctx: SpecialFunctionExpressionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.windowFunction`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWindowFunction?: (ctx: WindowFunctionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.whenClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitWhenClause?: (ctx: WhenClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.over`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOver?: (ctx: OverContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.ignoreNulls`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIgnoreNulls?: (ctx: IgnoreNullsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.tableDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTableDesc?: (ctx: TableDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.restoreTableDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRestoreTableDesc?: (ctx: RestoreTableDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.explainDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExplainDesc?: (ctx: ExplainDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.optimizerTrace`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOptimizerTrace?: (ctx: OptimizerTraceContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.partitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionDesc?: (ctx: PartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.listPartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitListPartitionDesc?: (ctx: ListPartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.singleItemListPartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleItemListPartitionDesc?: (ctx: SingleItemListPartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.multiItemListPartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultiItemListPartitionDesc?: (ctx: MultiItemListPartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.stringList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStringList?: (ctx: StringListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.rangePartitionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRangePartitionDesc?: (ctx: RangePartitionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.singleRangePartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSingleRangePartition?: (ctx: SingleRangePartitionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.multiRangePartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultiRangePartition?: (ctx: MultiRangePartitionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.partitionRangeDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionRangeDesc?: (ctx: PartitionRangeDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.partitionKeyDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionKeyDesc?: (ctx: PartitionKeyDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.partitionValueList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionValueList?: (ctx: PartitionValueListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.keyPartition`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyPartition?: (ctx: KeyPartitionContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.partitionValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPartitionValue?: (ctx: PartitionValueContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.distributionClause`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDistributionClause?: (ctx: DistributionClauseContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.distributionDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDistributionDesc?: (ctx: DistributionDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.refreshSchemeDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRefreshSchemeDesc?: (ctx: RefreshSchemeDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.statusDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStatusDesc?: (ctx: StatusDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.properties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProperties?: (ctx: PropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.extProperties`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitExtProperties?: (ctx: ExtPropertiesContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.propertyList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPropertyList?: (ctx: PropertyListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.userPropertyList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUserPropertyList?: (ctx: UserPropertyListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.property`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProperty?: (ctx: PropertyContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.varType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitVarType?: (ctx: VarTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.comment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComment?: (ctx: CommentContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.outfile`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitOutfile?: (ctx: OutfileContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.fileFormat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFileFormat?: (ctx: FileFormatContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.string`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitString?: (ctx: StringContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.binary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBinary?: (ctx: BinaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.comparisonOperator`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitComparisonOperator?: (ctx: ComparisonOperatorContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.booleanValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBooleanValue?: (ctx: BooleanValueContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.interval`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInterval?: (ctx: IntervalContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.unitIdentifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnitIdentifier?: (ctx: UnitIdentifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.unitBoundary`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUnitBoundary?: (ctx: UnitBoundaryContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.type`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitType?: (ctx: TypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.arrayType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitArrayType?: (ctx: ArrayTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.mapType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMapType?: (ctx: MapTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.subfieldDesc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubfieldDesc?: (ctx: SubfieldDescContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.subfieldDescs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSubfieldDescs?: (ctx: SubfieldDescsContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.structType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStructType?: (ctx: StructTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.typeParameter`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitTypeParameter?: (ctx: TypeParameterContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.baseType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitBaseType?: (ctx: BaseTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.decimalType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitDecimalType?: (ctx: DecimalTypeContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.qualifiedName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitQualifiedName?: (ctx: QualifiedNameContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.identifier`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifier?: (ctx: IdentifierContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.identifierList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierList?: (ctx: IdentifierListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.identifierOrString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierOrString?: (ctx: IdentifierOrStringContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.identifierOrStringList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierOrStringList?: (ctx: IdentifierOrStringListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.identifierOrStringOrStar`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdentifierOrStringOrStar?: (ctx: IdentifierOrStringOrStarContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.user`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitUser?: (ctx: UserContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.assignment`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAssignment?: (ctx: AssignmentContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.assignmentList`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitAssignmentList?: (ctx: AssignmentListContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.number`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNumber?: (ctx: NumberContext) => Result;
    /**
     * Visit a parse tree produced by `StarRocksSqlParser.nonReserved`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitNonReserved?: (ctx: NonReservedContext) => Result;
}
