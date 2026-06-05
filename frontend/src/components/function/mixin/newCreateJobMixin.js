import {
  isDb2,
  isES,
  isMQ,
  isHasSchema,
  isKafka,
  isMariaDB,
  isMySQL,
  isOracle,
  isPG,
  isPolar,
  isPolarDbX,
  isRedis,
  isRocketMQ,
  isTiDB,
  isTunnel,
  isMongoDB,
  isNoColumn,
  hasPartitionExpr,
  isPulsar,
  isNoDb,
  isDrDs,
  isHive,
  isKudu,
  isSQLServer,
  isRabbitMQ,
  MAPPING_METHOD
} from '@/utils';
import _ from 'lodash';
import { parseCron } from '@/components/util';

export default {
  data() {
    return {
      selectedColumns: {}
    };
  },
  methods: {
    newParseExistSchemaData(data) {
      console.log('parseExist');
      const { sourceType, targetType } = this.taskInfo;
      this.taskInfo.dbMap = [];
      const sourceSchemaJson = JSON.parse(data.sourceSchema);
      const mappingJson = JSON.parse(data.mappingConfig);
      const targetSchemaJson = data.targetSchema ? JSON.parse(data.targetSchema) : [];
      let schema = [];
      // console.log('sourceSchemaJson', sourceSchemaJson, mappingJson, targetSchemaJson);
      if (isNoDb(sourceType)) {
        if (isMQ(targetType)) {
          schema = sourceSchemaJson;
        } else {
          schema = targetSchemaJson;
        }
      } else {
        schema = sourceSchemaJson;
      }
      if (isMQ(sourceType) && isMQ(targetType)) {
        const dbMapItem = {
          sourceDb: '',
          targetDb: '',
          targetSchema: '',
          sourceSchema: '',
          needAutoCreated: '',
          actions: []
        };

        this.taskInfo.dbMap.push(dbMapItem);
      } else {
        schema.forEach((db) => {
          const dbMapItem = {
            sourceDb: db.db,
            targetDb: this.getExistTargetDb(db, mappingJson).db,
            targetSchema: this.getExistTargetDb(db, mappingJson).schema,
            sourceSchema: db.schemas ? db.schemas[0].schema : db.tableSpaces ? db.tableSpaces[0].tableSpace : 'public',
            needAutoCreated: db.targetAutoCreate,
            actions: db.actions
          };

          this.taskInfo.dbMap.push(dbMapItem);
        });
      }
      if (isES(targetType)) {
        // console.log('targetSchemaJson[0].globalTimeZone', targetSchemaJson[0].globalTimeZone);
        this.taskInfo.globalTimeZone = targetSchemaJson[0].globalTimeZone;
      }
    },
    newGetDataJobData() {
      const processorConfigList = [];
      const taskInfo = _.cloneDeep(this.taskInfo);
      const configData = this.newGetConfigData();

      if (configData.allVirtualColumnList?.length) {
        processorConfigList.push({
          processorConfigType: 'FIELD_MAKER_PROCESSOR',
          allProcessorContextJson: JSON.stringify(configData.allVirtualColumnList)
        });
      }

      if (configData.allDataTransformList?.length) {
        processorConfigList.push({
          processorConfigType: 'FIELD_TRANSFORM_PROCESSOR',
          allProcessorContextJson: JSON.stringify(configData.allDataTransformList)
        });
      }

      if (configData.allEmbeddingList?.length) {
        const llmProcessor = {};
        llmProcessor.llmExtraFields = this.taskInfo.cleanDataConfig.llmDefaultColInfo;
        llmProcessor.embeddingFields = configData.allEmbeddingList;
        processorConfigList.push({
          processorConfigType: 'LLM_EMBEDDING_PROCESSOR',
          allProcessorContextJson: JSON.stringify(llmProcessor)
        });
      }

      if (this.taskInfo.cleanDataConfig.wideTables?.length) {
        processorConfigList.push({
          processorConfigType: 'WIDE_TABLE_PROCESSOR',
          allProcessorContextJson: JSON.stringify(this.taskInfo.cleanDataConfig.wideTables)
        });
      }

      const { jobName, jobType, originalConfig, functionConfig, cleanDataConfig, reviseBindCheckTaskId } = taskInfo;

      return {
        // TODO 存疑
        processorConfigList,
        // common
        jobName,
        jobType,
        reviseBindCheckTaskId,
        // originalConfig
        clusterId: originalConfig.clusterId,
        oraIncrMode: originalConfig.oraIncrMode,
        oraBuildRedoDicWhenCreate: originalConfig.oraBuildRedoDicWhenCreate,
        sourceHost: originalConfig.sourceInstance,
        sinkHost: originalConfig.targetInstance,
        sourceDataSourceId: originalConfig.sourceDataSourceId,
        sourceHostType: originalConfig.sourceHostType,
        targetDataSourceId: originalConfig.targetDataSourceId,
        targetHostType: originalConfig.targetHostType,
        globalTimeZone: originalConfig.globalTimeZone,
        srcRocketMqGroupId: originalConfig.consumerGroupId,
        srcRabbitMqVhost: originalConfig.srcRabbitMqVhost,
        srcRabbitExchange: originalConfig.srcRabbitExchange,
        dstRabbitMqVhost: originalConfig.dstRabbitMqVhost,
        dstRabbitExchange: originalConfig.dstRabbitExchange,
        kafkaConsumerGroupId: originalConfig.consumerGroupId,
        srcDsCharset: originalConfig.sourceCharset,
        tarDsCharset: originalConfig.targetCharset,
        srcSchemaLessFormat: originalConfig.srcSchemaLessFormat,
        dstSchemaLessFormat: originalConfig.dstSchemaLessFormat,
        originDecodeMsgFormat: originalConfig.originDecodeMsgFormat,
        schemaWhiteListLevel: originalConfig.schemaWhiteListLevel,
        dstMqDefaultTopic: originalConfig.dstMqDefaultTopic,
        dstMqDefaultTopicPartitions: originalConfig.dstMqDefaultTopicPartitions,
        dstMqDdlTopic: originalConfig.dstMqDdlTopic,
        dstMqDdlTopicPartitions: originalConfig.dstMqDdlTopicPartitions,
        dstCkTableEngine: originalConfig.dstCkTableEngine,
        dstSrOrDorisTableModel: originalConfig.dstSrOrDorisTableModel,
        targetTimeDefaultStrategy: originalConfig.targetTimeDefaultStrategy,
        keyConflictStrategy: isTiDB(originalConfig.sourceType) ? 'REPLACE' : originalConfig.keyConflictStrategy,
        kuduNumReplicas: originalConfig.kuduNumReplicasSelected ? originalConfig.kuduNumReplicas : '',
        kuduNumBuckets: originalConfig.kuduNumBucketsSelected ? originalConfig.kuduNumBuckets : '',
        migrationBucketNumber: originalConfig.migrationBucketNumber,
        migrationPropertiesConfig: this.isStarRocks(originalConfig.targetType)
          ? originalConfig.migrationPropertiesConfigWithSr
          : originalConfig.migrationPropertiesConfig,
        obTenant: originalConfig.obTenant,
        reSchemaMigration: originalConfig.reSchemaMigration,
        srcPulsarTenant: originalConfig.sourcePulsarTenant,
        srcPulsarNamespace: originalConfig.sourcePulsarNamespace,
        dstPulsarTenant: originalConfig.sinkPulsarTenant,
        dstPulsarNamespace: originalConfig.sinkPulsarNamespace,
        cgTableType: this.taskInfo.cgTableType,
        ...this.taskInfo.advancedSetting,
        // functionConfig
        dataJobDesc: functionConfig.dataJobDesc,
        initialSync: this.isSyncJob(functionConfig.jobType) && functionConfig.initialSync,
        shortTermSync: this.isMigrationJob(functionConfig.jobType) && functionConfig.shortTermSync,
        shortTermNum: this.isMigrationJob(functionConfig.jobType) && functionConfig.shortTermSync ? functionConfig.shortTermNum : 0,
        checkOnce: this.isReviseJob(functionConfig.jobType) ? false : this.isCheckOneMode(functionConfig.checkMode),
        checkPeriod: this.isReviseJob(functionConfig.jobType) ? false : this.isCheckPeriodMode(functionConfig.checkMode),
        checkPeriodCronExpr: this.newParseCron(functionConfig.checkPeriodDate),
        fullPeriod: functionConfig.fullPeriod,
        fullPeriodCronExpr: this.newParseCron(functionConfig.fullPeriodDate),
        autoStart: JSON.parse(functionConfig.autoStart),
        specId: functionConfig.spec.id ? functionConfig.spec.id : 15,
        filterDDL: JSON.parse(functionConfig.filterDDL),
        cleanTargetBeforeFull: functionConfig.cleanTargetBeforeFull,
        syncPartitionInfo: functionConfig.syncPartitionInfo,
        dataCheckType: this.isNoCheckMode(functionConfig.checkMode) ? null : functionConfig.dataCheckType,
        dataReviseType: this.isNoCheckMode(functionConfig.checkMode) ? null : functionConfig.dataReviseType,
        pushDownEnabled: functionConfig.pushDownEnabled,
        // cleanDataConfig
        llmEmbeddingName: cleanDataConfig.llmConfig.selectedConfig,
        llmEmbeddingDsId: cleanDataConfig.llmConfig.dsId,
        llmChatName: cleanDataConfig.llmConfig.llmChatName,
        llmChatDsId: cleanDataConfig.llmConfig.llmChatDsId,
        // other
        pkgDescription: cleanDataConfig.pkgDescription,
        // configData
        structMigration: configData.structMigration,
        sourceSchema: configData.finalSourceSchema,
        targetSchema: configData.finalTargetSchema,
        mappingDef: JSON.stringify(configData.mappingDef),
        jobKvConfigTemplId: this.taskInfo.selectedTemplateId
      };
    },
    newGetConfigData(type) {
      const { sourceType, targetType, common, tableFilterConfig, cleanDataConfig } = this.taskInfo;
      try {
        const allVirtualColumnList = [];
        const allCustomVirtualColumnList = [];
        const allDataTransformList = [];
        const allEmbeddingList = [];
        const sourceSchema = [];
        const targetSchema = [];
        let mappingDef = [];
        const schemaTopicMapping = {};
        const tableKeyPrefixMapping = {};
        const tableTopicMapping = {};
        const topicTopicMapping = {};
        const tableIndexMapping = {};
        const indexIndexMapping = {};
        const schemaIndexMapping = {};
        const columnMapping = {};
        const topicTableMapping = {};
        const topicIndexMapping = {};
        const indexMetaMap = {};
        const schemaMapping = {};
        const tableMapping = {};
        const topics = [];
        const targetTopics = [];
        const sourceEsIndex = [];
        const esIndex = [];
        const dbMapping = {};
        const anyDbMapping = {};
        const anySchemaMapping = {};
        const redisKeys = [];
        const noTargetDbTableMapping = {};

        let structMigration = false;
        let tableList = [];
        let finalSourceSchema = [];
        let finalTargetSchema = [];

        dbMapping.method = MAPPING_METHOD.DB_DB;
        dbMapping.serializeMapping = {};
        dbMapping.serializeAutoGenRules = {};
        dbMapping.commonGenRule = 'MIRROR';

        anyDbMapping.method = MAPPING_METHOD.ANY_DB;
        anyDbMapping.serializeMapping = {};
        anyDbMapping.serializeAutoGenRules = {};
        anyDbMapping.commonGenRule = 'MIRROR';

        anySchemaMapping.method = MAPPING_METHOD.ANY_SCHEMA;
        anySchemaMapping.serializeMapping = {};
        anySchemaMapping.serializeAutoGenRules = {};
        anySchemaMapping.commonGenRule = 'MIRROR';

        if (isHasSchema(sourceType)) {
          if (isHasSchema(targetType)) {
            schemaMapping.method = 'SCHEMA_SCHEMA';
          } else {
            schemaMapping.method = 'SCHEMA_DB';
          }
        } else if (isHasSchema(targetType)) {
          schemaMapping.method = 'DB_SCHEMA';
        }
        schemaMapping.serializeMapping = {};
        schemaMapping.serializeAutoGenRules = {};
        schemaMapping.commonGenRule = 'MIRROR';

        tableTopicMapping.serializeMapping = {};
        tableTopicMapping.method = 'TABLE_TOPIC';
        tableTopicMapping.serializeAutoGenRules = {};
        tableTopicMapping.commonGenRule = 'MIRROR';

        topicTopicMapping.serializeMapping = {};
        topicTopicMapping.method = 'TOPIC_TOPIC';
        topicTopicMapping.serializeAutoGenRules = {};
        topicTopicMapping.commonGenRule = 'MIRROR';

        schemaTopicMapping.serializeMapping = {};
        schemaTopicMapping.method = 'TABLE_TOPIC';
        schemaTopicMapping.serializeAutoGenRules = {};
        schemaTopicMapping.commonGenRule = 'MIRROR';

        if (this.taskInfo && this.taskInfo.schemaWhiteListLevel) {
          tableTopicMapping.commonGenRule = 'DEFAULT_TOPIC';
        }

        tableKeyPrefixMapping.serializeMapping = {};
        tableKeyPrefixMapping.method = 'TABLE_KEYPREFIX';
        tableKeyPrefixMapping.serializeAutoGenRules = {};
        tableKeyPrefixMapping.commonGenRule = 'MIRROR';

        topicTableMapping.serializeMapping = {};
        topicTableMapping.method = 'TOPIC_TABLE';
        topicTableMapping.serializeAutoGenRules = {};
        topicTableMapping.commonGenRule = 'MIRROR';

        topicIndexMapping.serializeMapping = {};
        topicIndexMapping.method = MAPPING_METHOD.TOPIC_INDEX;
        topicIndexMapping.serializeAutoGenRules = {};
        topicIndexMapping.commonGenRule = 'MIRROR';

        tableIndexMapping.serializeMapping = {};
        tableIndexMapping.method = MAPPING_METHOD.TABLE_INDEX;
        tableIndexMapping.serializeAutoGenRules = {};
        tableIndexMapping.commonGenRule = 'MIRROR';

        indexIndexMapping.serializeMapping = {};
        indexIndexMapping.method = MAPPING_METHOD.INDEX_INDEX;
        indexIndexMapping.serializeAutoGenRules = {};
        indexIndexMapping.commonGenRule = 'MIRROR';

        schemaIndexMapping.serializeMapping = {};
        schemaIndexMapping.method = 'TABLE_INDEX';
        schemaIndexMapping.serializeAutoGenRules = {};
        schemaIndexMapping.commonGenRule = 'MIRROR';

        tableMapping.serializeMapping = {};
        tableMapping.method = 'TABLE_TABLE';
        tableMapping.serializeAutoGenRules = {};
        if (['MIRROR', 'TO_UPPER_CASE', 'TO_LOWER_CASE'].includes(tableFilterConfig.selectedTableMappingRuleName)) {
          tableMapping.commonGenRule = tableFilterConfig.selectedTableMappingRuleName;
        } else {
          tableMapping.commonGenRule = 'MIRROR';
        }
        // if (isMySQL(sinkType) || isPolar(sinkType) || isMariaDB(sinkType) || isPolarDbX(sinkType)) {
        //   tableMapping.commonGenRule = 'TO_LOWER_CASE';
        // }
        // if (this.taskInfo && this.taskInfo.commonRule === 'UpperCase' || !this.taskInfo && DataSourceGroup.oracle.indexOf(sinkType) > -1) {
        //   tableMapping.commonGenRule = 'TO_UPPER_CASE';
        // }
        // if (isPG(sinkType) && this.taskInfo.tableMappingRule.length && this.taskInfo.tableMappingRule[this.taskInfo.tableMappingIndex].rule === 'TO_LOWER_CASE') {
        //   tableMapping.commonGenRule = 'TO_LOWER_CASE';
        // }

        noTargetDbTableMapping.serializeMapping = {};
        noTargetDbTableMapping.method = 'TABLE_TABLE';
        noTargetDbTableMapping.serializeAutoGenRules = {};
        noTargetDbTableMapping.commonGenRule = 'MIRROR';
        columnMapping.method = 'COLUMN_COLUMN';
        columnMapping.serializeMapping = {};
        columnMapping.serializeAutoGenRules = {};
        if (['MIRROR', 'TO_UPPER_CASE', 'TO_LOWER_CASE'].includes(cleanDataConfig.selectedColumnMappingRuleName)) {
          columnMapping.commonGenRule = cleanDataConfig.selectedColumnMappingRuleName;
        } else {
          columnMapping.commonGenRule = 'MIRROR';
        }
        // if (isMySQL(sinkType) || isPolar(sinkType) || isMariaDB(sinkType) || isPolarDbX(sinkType)) {
        //   columnMapping.commonGenRule = 'TO_LOWER_CASE';
        // }
        // if (this.taskInfo && this.taskInfo.commonRule === 'UpperCase' || !this.taskInfo && DataSourceGroup.oracle.indexOf(sinkType) > -1) {
        //   columnMapping.commonGenRule = 'TO_UPPER_CASE';
        // }
        // if (isPG(sinkType) && this.taskInfo.columnMappingRule.length && this.taskInfo.columnMappingRule[this.taskInfo.columnMappingIndex].rule === 'TO_LOWER_CASE') {
        //   columnMapping.commonGenRule = 'TO_LOWER_CASE';
        // }
        this.taskInfo.dbMap.forEach((db) => {
          console.log(db);
          const sourceDbSchema = {};
          const targetDbSchema = {};

          sourceDbSchema.db = db.sourceDb;
          sourceDbSchema.actions = [];
          // sourceDbSchema.new = db.new;
          if (!isOracle(sourceType)) {
            if (isHasSchema(sourceType)) {
              sourceDbSchema.schemas = [];
              sourceDbSchema.schemas.push({
                schema: db.sourceSchema,
                schemaPattern: '',
                tables: [],
                targetAutoCreate: isHasSchema(targetType) ? db.schemaAutoCreate : db.needAutoCreated
              });
            } else {
              if (isMongoDB(sourceType)) {
                sourceDbSchema.dbPattern = '';
                sourceDbSchema.collections = [];
              } else {
                sourceDbSchema.dbPattern = '';
                sourceDbSchema.tables = [];
              }
            }
          } else {
            sourceDbSchema.tableSpaces = [];
            sourceDbSchema.tableSpaces.push({
              tableSpace: db.sourceSchema,
              tableSpacePattern: '',
              tables: [],
              targetAutoCreate: isHasSchema(targetType) ? db.schemaAutoCreate : db.needAutoCreated
            });
          }
          if (isMQ(targetType)) {
            targetDbSchema.topics = [];
          } else {
            targetDbSchema.db = db.targetDb;
            targetDbSchema.dbPattern = '';
            if (isPG(targetType)) {
              targetDbSchema.schemas = [];
              targetDbSchema.schemas.push({
                schema: db.targetSchema,
                schemaPattern: '',
                tables: []
              });
            } else {
              targetDbSchema.tables = [];
            }
          }
          sourceDbSchema.targetAutoCreate = false;
          targetDbSchema.targetAutoCreate = false;
          // if (type === 'reduceData') {
          //   sourceDbSchema.inBlackList = true;
          // } else {
          //   sourceDbSchema.inBlackList = false;
          // }
          sourceDbSchema.inBlackList = false;
          targetDbSchema.inBlackList = false;
          const dbName = db.sourceDb;

          if (isNoDb(sourceType)) {
            if (isMQ(sourceType)) {
              if (isHasSchema(targetType)) {
                const key = {
                  value: 'ANY_SCHEMA'
                };
                const value = {
                  parent: {
                    value: db.targetDb
                  },
                  value: db.targetSchema
                };
                anySchemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              } else {
                const key = {
                  value: 'ANY_DB'
                };
                const value = {
                  value: db.targetDb
                };

                anyDbMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              }
            }
          } else if (dbName !== db.targetDb) {
            const key = {
              value: dbName
            };
            const value = {
              value: db.targetDb
            };

            dbMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
          }
          if (db.needAutoCreated || (isHasSchema(targetType) && !isHasSchema(sourceType) && db.schemaAutoCreate)) {
            sourceDbSchema.targetAutoCreate = true;
            structMigration = true;
          }
          // console.log('DataSourceGroup.hasSchema.indexOf(this.sourceType)', sourceType, DataSourceGroup.hasSchema.indexOf(sourceType));
          if (isHasSchema(sourceType)) {
            if (isHasSchema(targetType)) {
              if (dbName !== db.targetDb || db.sourceSchema !== db.targetSchema) {
                const key = {
                  parent: {
                    value: dbName
                  },
                  value: db.sourceSchema
                };
                const value = {
                  parent: {
                    value: db.targetDb
                  },
                  value: db.targetSchema
                };
                schemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              }
            } else {
              const key = {
                parent: {
                  value: dbName
                },
                value: db.sourceSchema
              };
              const value = {
                value: db.targetDb
              };

              schemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            }
          } else if (isHasSchema(targetType) && !isKafka(sourceType)) {
            // console.log(23333);
            if (!dbName) {
              const key = {
                value: db.sourceDb
              };
              const value = {
                parent: {
                  value: db.targetDb
                },
                value: db.targetSchema
              };

              schemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            } else {
              const key = {
                value: dbName
              };
              const value = {
                parent: {
                  value: db.targetDb
                },
                value: db.targetSchema
              };

              schemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            }
          }
          // 从allTableList中筛选出选中的表
          if (db.allTableList) {
            tableList = db.allTableList.filter((table) => table._checked);
          }
          if (this.taskInfo.common.isFullDatabaseSync) {
            tableList = [];
          }

          tableList.forEach((table) => {
            if (table.virtualColumnList?.length) {
              let hasCustom = false;
              table.virtualColumnList.forEach((virtualColumn) => {
                if (virtualColumn.needAutoCreate) {
                  hasCustom = true;
                }
              });

              allVirtualColumnList.push({
                fieldPathName: table.path,
                customFieldList: table.virtualColumnList
              });

              if (hasCustom) {
                allCustomVirtualColumnList.push({
                  fieldPathName: table.path,
                  customFieldList: table.virtualColumnList,
                  needAutoCreated: true
                });
              }
            }

            if (table.dataTransformList?.length) {
              const fieldsScripts = {};
              table.dataTransformList.forEach((column) => {
                fieldsScripts[column.columnName] = column.script;
              });
              allDataTransformList.push({
                db: table.sourceDb,
                schema: isHasSchema(sourceType) ? table.sourceSchema : null,
                table: table.sourceTable,
                fieldsScripts
              });
            }

            if (table.embeddingData?.embeddingCols?.length) {
              allEmbeddingList.push(table.embeddingData);
            }

            const sourceSchemaTable = {};
            const targetSchemaTable = {};

            sourceSchemaTable.table = table.sourceTable;
            sourceSchemaTable.tablePattern = '';
            sourceSchemaTable.columns = [];
            sourceSchemaTable.actions = [];
            sourceSchemaTable.blackActs = table.actionBlacklist || [];
            sourceSchemaTable.inBlackList = true;
            sourceSchemaTable.targetAutoCreate = false;
            sourceSchemaTable.specifiedPks = table.cols || [];
            // if (isStarRocks(sinkType)) {
            //   sourceSchemaTable.specifiedDisKeys = table.specifiedDisKeys || [];
            // }
            targetSchemaTable.tablePattern = '';
            targetSchemaTable.columns = [];
            if (!this.taskInfo || !this.taskInfo.common.isFullDatabaseSync) {
              if (table._checked || (type === 'reduceData' && table.hasInJob)) {
                targetSchemaTable.table = table.targetTable;
                if (isDrDs(targetType)) {
                  if (table.dbFuncs && table.dbpartition.length) {
                    targetSchemaTable.dbPartitionFunc = table.dbFuncs;
                    targetSchemaTable.dbPartitionCols = table.dbpartition;
                  }
                  if (table.tbFuncs && table.tbpartition.length > 0 && table.tbpartitions) {
                    targetSchemaTable.tbPartitionFunc = table.tbFuncs;
                    targetSchemaTable.tbPartitionCols = table.tbpartition;
                    targetSchemaTable.tbPartitions = table.tbpartitions;
                  }
                }
                if (hasPartitionExpr(targetType)) {
                  if (table.partitionExpr) {
                    targetSchemaTable.partitionExpr = table.partitionExpr;
                  }
                }
                if (isHive(targetType) && table.partitionData) {
                  const partitionKeyList = [];
                  partitionKeyList.push({
                    keyName: table.partitionData.partition[0].key,
                    partitionFunction: table.partitionData.partition[0].functionConfig
                  });
                  targetSchemaTable.partitionKeys = partitionKeyList;
                  targetSchemaTable.fileFormat = table.partitionData.fileFormat;
                  targetSchemaTable.compressType = table.partitionData.compressType;
                }
                if (type !== 'reduceData') {
                  targetSchemaTable.inBlackList = false;
                  sourceSchemaTable.inBlackList = false;
                }
                if (
                  table.sourceTable !== table.targetTable ||
                  this.newGetTargetValue(table.sourceTable, tableMapping.commonGenRule) !== table.targetTable
                ) {
                  if (isHasSchema(targetType)) {
                    if (isOracle(targetType)) {
                      const key = isHasSchema(sourceType)
                        ? {
                            parent: {
                              value: table.sourceSchema,
                              parent: {
                                value: table.sourceDb
                              }
                            },
                            value: table.sourceTable
                          }
                        : {
                            parent: {
                              value: table.sourceDb
                            },
                            value: table.sourceTable
                          };
                      const value = {
                        value: table.targetTable,
                        parent: {
                          value: table.targetSchema,
                          parent: {
                            value: table.targetDb ? table.targetDb : table.targetDb
                          }
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    } else {
                      if (isHasSchema(sourceType)) {
                        const key = {
                          parent: {
                            value: table.sourceSchema,
                            parent: {
                              value: table.sourceDb
                            }
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      } else {
                        const key = {
                          parent: {
                            value: table.sourceDb
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.targetDb ? table.targetDb : table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      }
                    }
                  } else {
                    if (isHasSchema(sourceType)) {
                      if (isOracle(sourceType) && isKudu(targetType)) {
                        const key = {
                          parent: {
                            parent: {
                              value: table.sourceDb
                            },
                            value: table.sourceSchema
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.targetTable
                        };

                        noTargetDbTableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      } else {
                        const key = {
                          parent: {
                            value: table.sourceSchema,
                            parent: {
                              value: table.sourceDb
                            }
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.targetTable,
                          parent: {
                            value: table.targetDb ? table.targetDb : table.targetDb
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      }
                    } else {
                      const key = {
                        parent: {
                          value: table.sourceDb
                        },
                        value: table.sourceTable
                      };
                      const value = {
                        value: table.targetTable,
                        parent: {
                          value: table.targetDb ? table.targetDb : table.targetDb
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    }
                  }
                }
                if (isMQ(targetType)) {
                  if (isHasSchema(sourceType)) {
                    const key = {
                      parent: {
                        parent: {
                          value: table.sourceDb
                        },
                        value: table.sourceSchema
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    schemaTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else if (isKafka(sourceType) || isRocketMQ(sourceType) || isPulsar(sourceType)) {
                    const key = {
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    topicTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const key = {
                      parent: {
                        value: table.sourceDb
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    tableTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    topicTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (isRedis(targetType)) {
                  const key = {
                    parent: {
                      value: table.sourceDb
                    },
                    value: table.sourceTable
                  };
                  const value = {
                    value: table.targetTable
                  };

                  tableKeyPrefixMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (isES(targetType)) {
                  if (isHasSchema(sourceType)) {
                    const key = {
                      parent: {
                        parent: {
                          value: table.sourceDb
                        },
                        value: table.sourceSchema
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    schemaIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                  if (isES(sourceType)) {
                    const key = {
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    indexIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const key = {
                      parent: {
                        value: table.sourceDb
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    tableIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (isMQ(sourceType)) {
                  const key = {
                    value: table.sourceTable
                  };

                  if (isNoDb(targetType)) {
                    const value = {
                      value: table.targetTable
                    };

                    topicIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const value = {
                      parent: {
                        value: table.targetDb ? table.targetDb : table.targetDb
                      },
                      value: table.targetTable
                    };

                    topicTableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (isES(targetType)) {
                  if (isES(sourceType)) {
                    indexMetaMap[table.sourceTable] = {};
                    indexMetaMap[table.sourceTable].indexName = table.sourceTable;
                    indexMetaMap[table.sourceTable].numberOfShards = table.shards;
                    indexMetaMap[table.sourceTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.sourceTable].fieldMetaList = [];
                  } else {
                    indexMetaMap[table.targetTable] = {};
                    indexMetaMap[table.targetTable].indexName = table.targetTable;
                    indexMetaMap[table.targetTable].numberOfShards = table.shards;
                    indexMetaMap[table.targetTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.targetTable].fieldMetaList = [];
                  }
                }
                if (isMongoDB(sourceType) && table.idMapping) {
                  const key = {
                    parent: {
                      value: table.sourceTable,
                      parent: {
                        value: dbName
                      }
                    },
                    value: '_id'
                  };
                  const value = {
                    value: table.idMapping,
                    parent: {
                      value: table.targetTable,
                      parent: {
                        value: db.targetDb
                      }
                    }
                  };

                  columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  console.log('columnMapping111', columnMapping);
                }
                if (table.needAutoCreated && table._checked) {
                  sourceSchemaTable.targetAutoCreate = true;
                  structMigration = true;
                }
                if (table.whereCondition) {
                  sourceSchemaTable.dataFilter = {
                    type: table.dataFilterType,
                    expression: table.whereCondition
                  };
                }
                if (table.targetWhereCondition) {
                  sourceSchemaTable.dstUpdateCondition = table.targetWhereCondition;
                }

                const { selectedColumnList } = table;

                // if (table.selectedColumnsList) {
                //   if (isMQ(sourceType)) {
                //     selectedColumnList = db._checkedColumns[table.targetTable];
                //   } else {
                //     selectedColumnList = db._checkedColumns[table.sourceTable];
                //   }
                //   if (this._checkedColumns[db] && this._checkedColumns[db][table.sourceTable]) {
                //     selectedColumnList = this._checkedColumns[db][table.sourceTable];
                //   }
                // }
                if (selectedColumnList) {
                  selectedColumnList.forEach((column) => {
                    this.newSetColumns(column, db, table, dbName, columnMapping, structMigration, sourceSchemaTable, targetSchemaTable, indexMetaMap);
                  });
                }

                if (cleanDataConfig.selectedColumnMappingRuleName !== 'MIRROR') {
                  table.virtualColumnList.forEach((column) => {
                    column.sourceColumn = column.fieldName;
                    column._checked = true;
                    column.targetColumn = this.getTargetValue(column.sourceColumn, this.taskInfo.cleanDataConfig.selectedColumnMappingRuleName);
                    this.newSetColumns(
                      column,
                      db,
                      table,
                      dbName,
                      columnMapping,
                      structMigration,
                      sourceSchemaTable,
                      targetSchemaTable,
                      indexMetaMap,
                      true
                    );
                  });
                }
                if (isPG(sourceType) || isDb2(sourceType)) {
                  sourceDbSchema.schemas[0].tables.push(sourceSchemaTable);
                } else if (isOracle(sourceType)) {
                  sourceDbSchema.tableSpaces[0].tables.push(sourceSchemaTable);
                } else if (isMongoDB(sourceType)) {
                  sourceDbSchema.collections.push({
                    collection: sourceSchemaTable.table,
                    actions: [],
                    blackActs: sourceSchemaTable.blackActs || [],
                    targetAutoCreate: sourceSchemaTable.targetAutoCreate
                  });
                } else {
                  sourceDbSchema.tables.push(sourceSchemaTable);
                }
                if (isMQ(sourceType)) {
                  if (isRabbitMQ(sourceType)) {
                    topics.push({
                      queue: table.sourceTable,
                      queuePattern: '',
                      inBlackList: !table._checked || type === 'reduceData',
                      targetAutoCreate: table.needAutoCreated
                    });
                  } else if (isKafka(sourceType)) {
                    const fields = [];
                    if (selectedColumnList) {
                      selectedColumnList.forEach((column) => {
                        fields.push({
                          name: column.targetColumn,
                          iskey: column.isPk,
                          inBlackList: !column._checked,
                          targetAutoCreate: false
                        });
                      });
                    }
                    console.log(1);
                    topics.push({
                      topic: table.sourceTable,
                      topicPattern: '',
                      partitions: table.partitions,
                      pulsarTopicPersistent: table.needAutoCreated ? true : table.pulsarTopicPersistent,
                      partitionKeys: table.partitionKeys,
                      inBlackList: !table._checked || type === 'reduceData',
                      targetAutoCreate: table.needAutoCreated,
                      specifiedPks: table.cols || [],
                      fields
                    });
                  } else {
                    console.log(2);
                    topics.push({
                      topic: table.sourceTable,
                      topicPattern: '',
                      partitions: table.partitions,
                      pulsarTopicPersistent: table.needAutoCreated ? true : table.pulsarTopicPersistent,
                      partitionKeys: table.partitionKeys,
                      inBlackList: !table._checked || type === 'reduceData',
                      targetAutoCreate: table.needAutoCreated
                    });
                  }

                  if (isKafka(targetType) || isPulsar(targetType)) {
                    if (isRabbitMQ(targetType)) {
                      targetTopics.push({
                        queue: table.targetTable,
                        queuePattern: ''
                      });
                    } else {
                      console.log(3);
                      targetTopics.push({
                        topic: table.targetTable,
                        topicPattern: '',
                        partitions: table.partitions,
                        pulsarTopicPersistent: table.needAutoCreated ? true : table.sinkPulsarTopicPersistent,
                        partitionKeys: table.partitionKeys
                      });
                    }
                  }

                  if (isES(targetType)) {
                    let fields = [];
                    if (indexMetaMap[table.targetTable]) {
                      fields = indexMetaMap[table.targetTable].fieldMetaList;
                    }
                    const idFields = [];
                    const pkFields = [];

                    fields.forEach((field) => {
                      // console.log(field);
                      if (field.isPk) {
                        pkFields.push(field.fieldName);
                      }
                      if (field.isId) {
                        idFields.push(field.fieldName);
                      }
                    });
                    // console.log(idFields);

                    esIndex.push({
                      indexName: table.targetTable,
                      fields,
                      numberOfShards: table.shards,
                      numberOfReplicas: table.replication,
                      globalTimeZone: this.taskInfo.globalTimeZone,
                      idFieldNames: idFields.length ? idFields : pkFields
                    });
                  }
                } else if (isMQ(targetType)) {
                  if (isRabbitMQ(targetType)) {
                    topics.push({
                      queue: table.targetTable,
                      queuePattern: ''
                    });
                  } else {
                    console.log(4);
                    topics.push({
                      topic: table.targetTable,
                      topicPattern: '',
                      partitions: table.partitions,
                      pulsarTopicPersistent: table.needAutoCreated ? true : table.sinkPulsarTopicPersistent,
                      partitionKeys: table.partitionKeys
                    });
                  }
                } else {
                  if (isES(sourceType)) {
                    let fields = [];
                    if (indexMetaMap[table.sourceTable]) {
                      fields = indexMetaMap[table.sourceTable].fieldMetaList;
                    }
                    const idFields = [];
                    const pkFields = [];

                    fields.forEach((field) => {
                      // console.log(field);
                      if (field.isPk) {
                        pkFields.push(field.fieldName);
                      }
                      if (field.isId) {
                        idFields.push(field.fieldName);
                      }
                    });
                    console.log('source es', fields);

                    sourceEsIndex.push({
                      indexName: table.sourceTable,
                      fields,
                      numberOfShards: table.shards,
                      numberOfReplicas: table.replication,
                      globalTimeZone: this.taskInfo.globalTimeZone,
                      idFieldNames: idFields.length ? idFields : pkFields,
                      targetAutoCreate: table.needAutoCreated
                    });
                  }
                  if (isRedis(targetType)) {
                    redisKeys.push({
                      prefix: table.sinkTable,
                      suffixFields: table.suffixFields || []
                    });
                  } else if (isES(targetType)) {
                    console.log('sinkES');
                    let fields = [];
                    if (indexMetaMap[table.targetTable]) {
                      fields = indexMetaMap[table.targetTable].fieldMetaList;
                    }
                    const idFields = [];
                    const pkFields = [];

                    fields.forEach((field) => {
                      // console.log(field);
                      if (field.isPk) {
                        pkFields.push(field.fieldName);
                      }
                      if (field.isId) {
                        idFields.push(field.fieldName);
                      }
                    });
                    // console.log(idFields);

                    esIndex.push({
                      indexName: table.targetTable,
                      fields,
                      numberOfShards: table.shards,
                      numberOfReplicas: table.replication,
                      globalTimeZone: this.taskInfo.globalTimeZone,
                      idFieldNames: idFields.length ? idFields : pkFields
                    });
                  } else if (isPG(targetType)) {
                    targetDbSchema.schemas[0].tables.push(targetSchemaTable);
                  } else if (hasPartitionExpr(targetType)) {
                    if (table.partitionExpr) {
                      targetDbSchema.tables.push(targetSchemaTable);
                    }
                  } else {
                    targetDbSchema.tables.push(targetSchemaTable);
                  }
                }
              }
            } else {
              if (table._checked || !this.taskInfo.schemaWhiteListLevel) {
                if (type !== 'reduceData') {
                  targetSchemaTable.inBlackList = false;
                  sourceSchemaTable.inBlackList = false;
                }
                targetSchemaTable.table = table.targetTable;
                if (this.taskInfo && isDrDs(targetType)) {
                  if (table.dbFuncs && table.dbpartition.length > 0) {
                    targetSchemaTable.dbPartitionFunc = table.dbFuncs;
                    targetSchemaTable.dbPartitionCols = table.dbpartition;
                  }
                  if (table.tbFuncs && table.tbpartition.length > 0 && table.tbpartitions) {
                    targetSchemaTable.tbPartitionFunc = table.tbFuncs;
                    targetSchemaTable.tbPartitionCols = table.tbpartition;
                    targetSchemaTable.tbPartitions = table.tbpartitions;
                  }
                }
                if (hasPartitionExpr(targetType)) {
                  if (table.partitionExpr) {
                    targetSchemaTable.partitionExpr = table.partitionExpr;
                  }
                }
                if (isHive(targetType) && table.partitionData) {
                  const partitionKeyList = [];
                  partitionKeyList.push({
                    keyName: table.partitionData.partition[0].key,
                    partitionFunction: table.partitionData.partition[0].functionConfig
                  });
                  targetSchemaTable.partitionKeys = partitionKeyList;
                  targetSchemaTable.fileFormat = table.partitionData.fileFormat;
                  targetSchemaTable.compressType = table.partitionData.compressType;
                }
                if (
                  table.sourceTable !== table.targetTable ||
                  this.newGetTargetValue(table.sourceTable, tableMapping.commonGenRule) !== table.targetTable
                ) {
                  if (isHasSchema(targetType)) {
                    if (isOracle(targetType)) {
                      const key = {
                        parent: {
                          value: table.sourceDb
                        },
                        value: table.sourceTable
                      };
                      const value = {
                        value: table.targetTable,
                        parent: {
                          value: table.targetSchema,
                          parent: {
                            value: table.targetDb ? table.targetDb : table.targetDb
                          }
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    } else {
                      if (isHasSchema(sourceType)) {
                        const key = {
                          parent: {
                            value: table.sourceSchema,
                            parent: {
                              value: table.sourceDb
                            }
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.targetDb ? table.targetDb : table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      } else {
                        const key = {
                          parent: {
                            value: table.sourceDb
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.targetDb ? table.targetDb : table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      }
                    }
                  } else {
                    if (isHasSchema(sourceType)) {
                      const key = {
                        parent: {
                          value: table.sourceSchema,
                          parent: {
                            value: table.sourceDb
                          }
                        },
                        value: table.sourceTable
                      };
                      const value = {
                        value: table.targetTable,
                        parent: {
                          value: table.targetDb ? table.targetDb : table.targetDb
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    } else {
                      const key = {
                        parent: {
                          value: table.sourceDb
                        },
                        value: table.sourceTable
                      };
                      const value = {
                        value: table.targetTable,
                        parent: {
                          value: table.targetDb ? table.targetDb : table.targetDb
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    }
                  }
                }
                if (isMQ(targetType)) {
                  const key = {
                    parent: {
                      value: table.sourceDb
                    },
                    value: table.sourceTable
                  };
                  const value = {
                    value: table.targetTable
                  };

                  tableTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (isRedis(targetType)) {
                  const key = {
                    parent: {
                      value: table.sourceDb
                    },
                    value: table.sourceTable
                  };
                  const value = {
                    value: table.targetTable
                  };

                  tableKeyPrefixMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (isES(targetType)) {
                  if (isES(sourceType)) {
                    const key = {
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    indexIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const key = {
                      parent: {
                        value: table.sourceDb
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.targetTable
                    };

                    tableIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (isMQ(sourceType)) {
                  const key = {
                    value: table.sourceTable
                  };
                  const value = {
                    parent: {
                      value: table.targetDb ? table.targetDb : table.targetDb
                    },
                    value: table.targetTable
                  };

                  topicTableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (isES(targetType)) {
                  if (isES(sourceType)) {
                    indexMetaMap[table.sourceTable] = {};
                    indexMetaMap[table.sourceTable].indexName = table.sourceTable;
                    indexMetaMap[table.sourceTable].numberOfShards = table.shards;
                    indexMetaMap[table.sourceTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.sourceTable].fieldMetaList = [];
                  } else {
                    indexMetaMap[table.targetTable] = {};
                    indexMetaMap[table.targetTable].indexName = table.targetTable;
                    indexMetaMap[table.targetTable].numberOfShards = table.shards;
                    indexMetaMap[table.targetTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.targetTable].fieldMetaList = [];
                  }
                }

                if (isMongoDB(sourceType) && table.idMapping) {
                  const key = {
                    parent: {
                      value: table.sourceTable,
                      parent: {
                        value: dbName
                      }
                    },
                    value: '_id'
                  };
                  const value = {
                    value: table.idMapping,
                    parent: {
                      value: table.targetTable,
                      parent: {
                        value: db.targetDb
                      }
                    }
                  };

                  columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  console.log('columnMapping112', columnMapping);
                }
              } else {
                targetSchemaTable.inBlackList = true;
                sourceSchemaTable.inBlackList = true;
              }
              if (table.needAutoCreated && table._checked) {
                sourceSchemaTable.targetAutoCreate = true;
                structMigration = true;
              }
              if (table.whereCondition) {
                sourceSchemaTable.dataFilter = {
                  type: table.dataFilterType,
                  expression: table.whereCondition
                };
              }
              if (table.targetWhereCondition) {
                sourceSchemaTable.dstUpdateCondition = table.whereCondition;
              }
              let selectedColumnList = [];

              if (db._checkedColumns) {
                if (isMQ(sourceType)) {
                  selectedColumnList = db._checkedColumns[table.targetTable];
                } else {
                  selectedColumnList = db._checkedColumns[table.sourceTable];
                }
                if (this._checkedColumns[db] && this._checkedColumns[db][table.sourceTable]) {
                  selectedColumnList = this._checkedColumns[db][table.sourceTable];
                }
              }
              if (selectedColumnList) {
                selectedColumnList.forEach((column) => {
                  const sourceColumns = {};
                  const targetColumns = {};

                  sourceColumns.column = column.sourceColumn;
                  sourceColumns.targetAutoCreate = false;
                  sourceColumns.inBlackList = true;
                  targetColumns.column = column.targetColumn;
                  targetColumns.targetAutoCreate = false;
                  targetColumns.inBlackList = true;
                  if (!this.taskInfo || !this.taskInfo.schemaWhiteListLevel) {
                    if (column._checked) {
                      targetColumns.inBlackList = false;
                      sourceColumns.inBlackList = false;
                      console.log('column.sourceColumn', column.sourceColumn, column.targetColumn);
                      if (!isNoColumn(sourceType) && column.sourceColumn !== column.targetColumn) {
                        if (isHasSchema(targetType)) {
                          if (isOracle(targetType)) {
                            if (!(column.commonRule === 'UpperCase' || (!this.taskInfo && isOracle(targetType)))) {
                              const key = {
                                parent: {
                                  value: table.sourceTable,
                                  parent: {
                                    value: dbName
                                  }
                                },
                                value: column.sourceColumn
                              };
                              const value = {
                                value: column.targetColumn,
                                parent: {
                                  value: table.targetTable,
                                  parent: {
                                    value: db.targetDb
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          } else {
                            if (isHasSchema(sourceType)) {
                              const key = {
                                parent: {
                                  value: table.sourceTable,
                                  parent: {
                                    value: db.sourceSchema,
                                    parent: {
                                      value: dbName
                                    }
                                  }
                                },
                                value: column.sourceColumn
                              };
                              const value = {
                                value: column.targetColumn,
                                parent: {
                                  value: table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.targetDb
                                    }
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            } else {
                              const key = {
                                parent: {
                                  value: table.sourceTable,
                                  parent: {
                                    value: dbName
                                  }
                                },
                                value: column.sourceColumn
                              };
                              const value = {
                                value: column.targetColumn,
                                parent: {
                                  value: table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.targetDb
                                    }
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          }
                        } else {
                          if (isHasSchema(sourceType)) {
                            const key = {
                              parent: {
                                value: table.sourceTable,
                                parent: {
                                  value: db.sourceSchema,
                                  parent: {
                                    value: dbName
                                  }
                                }
                              },
                              value: column.sourceColumn
                            };
                            const value = {
                              value: column.targetColumn,
                              parent: {
                                value: table.targetTable,
                                parent: {
                                  value: db.targetDb
                                }
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          } else if (isES(targetType)) {
                            const key = {
                              parent: {
                                value: table.sourceTable,
                                parent: {
                                  value: dbName
                                }
                              },
                              value: column.sourceColumn
                            };
                            const value = {
                              value: column.targetColumn,
                              parent: {
                                value: table.targetTable
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          } else {
                            const key = {
                              parent: {
                                value: table.sourceTable,
                                parent: {
                                  value: dbName
                                }
                              },
                              value: column.sourceColumn
                            };
                            const value = {
                              value: column.targetColumn,
                              parent: {
                                value: table.targetTable,
                                parent: {
                                  value: db.targetDb
                                }
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          }
                        }
                      }
                      if (column.needAutoCreated && column._checked) {
                        if (!(!table.needAutoCreated && isMQ(targetType))) {
                          sourceColumns.targetAutoCreate = true;
                          structMigration = true;
                        }
                      }
                      sourceSchemaTable.columns.push(sourceColumns);
                      targetSchemaTable.columns.push(targetColumns);
                      if (isES(targetType) && column._checked) {
                        indexMetaMap[table.targetTable].fieldMetaList.push({
                          fieldName: column.targetColumn,
                          needIndex: column.isIndex,
                          fieldTypeName: column.sinkType,
                          esAnalyzerType: column.analyzer,
                          targetAutoCreate: column.needAutoCreated,
                          timeFormat: column.timeFormat,
                          jsonValue: column.jsonValue,
                          isPk: column.isPk,
                          isId: column.isId
                        });
                      }
                    }
                  } else {
                    if (column._checked) {
                      targetColumns.inBlackList = false;

                      sourceColumns.inBlackList = false;
                      console.log('column.sourceColumn', column.sourceColumn, column.targetColumn);
                      if (!isNoColumn(sourceType) && column.sourceColumn !== column.targetColumn) {
                        if (isHasSchema(targetType)) {
                          if (isOracle(targetType)) {
                            if (!(column.commonRule === 'UpperCase' || (!this.taskInfo && isOracle(targetType)))) {
                              const key = {
                                parent: {
                                  value: table.sourceTable,
                                  parent: {
                                    value: dbName
                                  }
                                },
                                value: column.sourceColumn
                              };
                              const value = {
                                value: column.targetColumn,
                                parent: {
                                  value: table.targetTable,
                                  parent: {
                                    value: db.targetDb
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          } else {
                            if (isHasSchema(sourceType)) {
                              const key = {
                                parent: {
                                  value: table.sourceTable,
                                  parent: {
                                    value: db.sourceSchema,
                                    parent: {
                                      value: dbName
                                    }
                                  }
                                },
                                value: column.sourceColumn
                              };
                              const value = {
                                value: column.targetColumn,
                                parent: {
                                  value: table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.targetDb
                                    }
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            } else {
                              const key = {
                                parent: {
                                  value: table.sourceTable,
                                  parent: {
                                    value: dbName
                                  }
                                },
                                value: column.sourceColumn
                              };
                              const value = {
                                value: column.targetColumn,
                                parent: {
                                  value: table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.targetDb
                                    }
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          }
                        } else {
                          if (isHasSchema(sourceType)) {
                            const key = {
                              parent: {
                                value: table.sourceTable,
                                parent: {
                                  value: db.sourceSchema,
                                  parent: {
                                    value: dbName
                                  }
                                }
                              },
                              value: column.sourceColumn
                            };
                            const value = {
                              value: column.targetColumn,
                              parent: {
                                value: table.targetTable,
                                parent: {
                                  value: db.targetDb
                                }
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          } else if (isES(targetType)) {
                            const key = {
                              parent: {
                                value: table.sourceTable,
                                parent: {
                                  value: dbName
                                }
                              },
                              value: column.sourceColumn
                            };
                            const value = {
                              value: column.targetColumn,
                              parent: {
                                value: table.targetTable
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          } else {
                            const key = {
                              parent: {
                                value: table.sourceTable,
                                parent: {
                                  value: dbName
                                }
                              },
                              value: column.sourceColumn
                            };
                            const value = {
                              value: column.targetColumn,
                              parent: {
                                value: table.targetTable,
                                parent: {
                                  value: db.targetDb
                                }
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          }
                        }
                      }
                    } else {
                      targetColumns.inBlackList = true;
                      sourceColumns.inBlackList = true;
                    }
                    if (column.needAutoCreated && column._checked) {
                      if (!(!table.needAutoCreated && isMQ(targetType))) {
                        sourceColumns.targetAutoCreate = true;
                        structMigration = true;
                      }
                    }
                    sourceSchemaTable.columns.push(sourceColumns);
                    targetSchemaTable.columns.push(targetColumns);
                    if (isES(targetType) && column._checked) {
                      indexMetaMap[table.targetTable].fieldMetaList.push({
                        fieldName: column.targetColumn,
                        needIndex: column.isIndex,
                        fieldTypeName: column.sinkType,
                        esAnalyzerType: column.analyzer,
                        targetAutoCreate: column.needAutoCreated,
                        timeFormat: column.timeFormat,
                        jsonValue: column.jsonValue,
                        isPk: column.isPk,
                        isId: column.isId
                      });
                    }
                  }
                });
              }
              if (isPG(sourceType)) {
                sourceDbSchema.schemas[0].tables.push(sourceSchemaTable);
              } else if (isOracle(sourceType)) {
                sourceDbSchema.tableSpaces[0].tables.push(sourceSchemaTable);
              } else {
                sourceDbSchema.tables.push(sourceSchemaTable);
              }
              if (isMQ(sourceType)) {
                if (isRabbitMQ(sourceType)) {
                  topics.push({
                    queue: table.sourceTable,
                    queuePattern: ''
                  });
                } else {
                  console.log(5);
                  topics.push({
                    topic: table.sourceTable,
                    topicPattern: '',
                    partitions: table.partitions,
                    pulsarTopicPersistent: table.needAutoCreated ? true : table.pulsarTopicPersistent,
                    partitionKeys: table.partitionKeys
                  });
                }
              }
              if (isES(sourceType)) {
                // console.log('table', table, indexMetaMap);
                const fields = indexMetaMap[table.sourceTable].fieldMetaList;
                const idFields = [];
                const pkFields = [];

                fields.forEach((field) => {
                  if (field.isPk) {
                    pkFields.push(field.fieldName);
                  }
                  if (field.isId) {
                    idFields.push(field.fieldName);
                  }
                });
                // console.log(idFields);
                sourceEsIndex.push({
                  indexName: table.sourceTable,
                  fields,
                  numberOfShards: table.shards,
                  numberOfReplicas: table.replication,
                  globalTimeZone: this.taskInfo.globalTimeZone,
                  idFieldNames: idFields.length ? idFields : pkFields,
                  targetAutoCreate: table.needAutoCreated
                });
              }
              if (isMQ(targetType)) {
                if (isRabbitMQ(targetType)) {
                  topics.push({
                    queue: table.targetTable,
                    queuePattern: ''
                  });
                } else {
                  console.log(6);
                  topics.push({
                    topic: table.targetTable,
                    topicPattern: '',
                    partitions: table.partitions,
                    pulsarTopicPersistent: table.needAutoCreated ? true : table.pulsarTopicPersistent,
                    partitionKeys: table.partitionKeys
                  });
                }
              } else if (isES(targetType)) {
                // console.log('table', table, indexMetaMap);
                const fields = indexMetaMap[table.targetTable].fieldMetaList;
                const idFields = [];
                const pkFields = [];

                fields.forEach((field) => {
                  if (field.isPk) {
                    pkFields.push(field.fieldName);
                  }
                  if (field.isId) {
                    idFields.push(field.fieldName);
                  }
                });
                // console.log(idFields);
                esIndex.push({
                  indexName: table.targetTable,
                  fields,
                  numberOfShards: table.shards,
                  numberOfReplicas: table.replication,
                  globalTimeZone: this.taskInfo.globalTimeZone,
                  idFieldNames: idFields.length ? idFields : pkFields
                });
              } else if (isPG(targetType)) {
                targetDbSchema.schemas[0].tables.push(targetSchemaTable);
              } else if (isRedis(targetType)) {
                redisKeys.push({
                  prefix: table.sinkTable,
                  suffixFields: table.suffixFields || []
                });
              } else if (hasPartitionExpr(targetType)) {
                if (table.partitionExpr) {
                  targetDbSchema.tables.push(targetSchemaTable);
                }
              } else {
                targetDbSchema.tables.push(targetSchemaTable);
              }
            }
          });
          console.log(sourceDbSchema);
          sourceSchema.push(sourceDbSchema);
          targetSchema.push(targetDbSchema);
        });
        if (isMQ(sourceType)) {
          if (isHasSchema(targetType)) {
            mappingDef.push(anySchemaMapping);
            mappingDef.push(topicTableMapping);
          } else if (!isNoDb(targetType)) {
            mappingDef.push(anyDbMapping);
            if (isTunnel(sourceType)) {
              mappingDef.push(tableMapping);
            } else {
              mappingDef.push(topicTableMapping);
            }
          } else if (isKafka(targetType) || isRocketMQ(targetType) || isPulsar(targetType)) {
            mappingDef.push(topicTopicMapping);
          } else {
            mappingDef.push(topicIndexMapping);
          }
        } else if (isHasSchema(sourceType)) {
          if (isHasSchema(targetType)) {
            mappingDef.push(dbMapping);
            mappingDef.push(schemaMapping);
            mappingDef.push(tableMapping);
            mappingDef.push(columnMapping);
          } else if (isOracle(sourceType) && isKudu(targetType)) {
            mappingDef.push(noTargetDbTableMapping);
            mappingDef.push(columnMapping);
          } else if (isHasSchema(sourceType) && isMQ(targetType)) {
            mappingDef.push(schemaTopicMapping);
            mappingDef.push(columnMapping);
          } else if (isES(targetType)) {
            mappingDef.push(schemaIndexMapping);
            mappingDef.push(columnMapping);
          } else {
            mappingDef.push(schemaMapping);
            mappingDef.push(tableMapping);
            mappingDef.push(columnMapping);
          }
        } else if (isHasSchema(targetType)) {
          mappingDef.push(schemaMapping);
          mappingDef.push(tableMapping);
          mappingDef.push(columnMapping);
        } else if (isMQ(targetType)) {
          mappingDef.push(tableTopicMapping);
          mappingDef.push(columnMapping);
        } else if (isRedis(targetType)) {
          mappingDef.push(tableKeyPrefixMapping);
          mappingDef.push(columnMapping);
        } else if (isES(targetType)) {
          if (isES(sourceType)) {
            mappingDef.push(indexIndexMapping);
            mappingDef.push(columnMapping);
          } else {
            mappingDef.push(tableIndexMapping);
            mappingDef.push(columnMapping);
          }
        } else {
          mappingDef.push(dbMapping);
          mappingDef.push(tableMapping);
          mappingDef.push(columnMapping);
        }
        if (isMQ(sourceType)) {
          finalSourceSchema = JSON.stringify(topics);
        } else if (isES(sourceType)) {
          finalSourceSchema = JSON.stringify(sourceEsIndex);
        } else {
          finalSourceSchema = sourceSchema.length > 0 ? JSON.stringify(sourceSchema) : null;
        }
        if (isPulsar(sourceType) && isPulsar(targetType)) {
          finalTargetSchema = JSON.stringify(targetTopics);
        } else if (isKafka(sourceType) && isKafka(targetType)) {
          finalTargetSchema = JSON.stringify(targetTopics);
        } else if (isMQ(targetType)) {
          finalTargetSchema = JSON.stringify(topics);
        } else if (isRedis(targetType)) {
          finalTargetSchema = JSON.stringify(redisKeys);
        } else if (isES(targetType)) {
          finalTargetSchema = JSON.stringify(esIndex);
        } else if (isMQ(sourceType)) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else if (isDrDs(targetType)) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else if (hasPartitionExpr(targetType)) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else if (isHive(targetType)) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else {
          finalTargetSchema = null;
          // finalTargetSchema = JSON.stringify(targetSchema);
        }

        if (this.taskInfo && this.taskInfo.customVirtualColumns?.length > 0) {
          structMigration = true;
        }

        if (isRedis(targetType)) {
          structMigration = false;
        }

        // console.log(JSON.parse(finalSourceSchema));

        let parseSchema = [];
        if (this.taskInfo.processType === 'edit' && type !== 'newData' && this.currentStep !== 2) {
          const { newData } = this.taskInfo;
          console.log(newData);

          if (isES(sourceType)) {
            parseSchema = JSON.parse(finalSourceSchema);
            if (newData.empty) {
              parseSchema = JSON.parse(finalSourceSchema).filter((schema) => !newData.empty._checkedTables[schema.indexName]);
            }
          } else {
            parseSchema = JSON.parse(finalSourceSchema).filter((schema) => {
              // console.log('schema', schema);
              if (newData[schema.db]) {
                const db = newData[schema.db];
                // console.log('db', db);
                if (db.new) {
                  // console.log('new', db);
                  return false;
                }
                if (isHasSchema(sourceType)) {
                  if (isOracle(sourceType)) {
                    schema.tableSpaces.forEach((tableSpace) => {
                      tableSpace.tables = tableSpace.tables.filter((table) => {
                        console.log(table, tableSpace, newData[schema.db]._checkedTables[table.table]);
                        return (
                          !newData[schema.db]._checkedTables[table.table] ||
                          (newData[schema.db]._checkedTables[table.table] &&
                            tableSpace.tableSpace !== newData[schema.db]._checkedTables[table.table].sourceSchema)
                        );
                      });
                    });
                  } else {
                    schema.schemas.forEach((s) => {
                      s.tables = s.tables.filter((table) => {
                        console.log(table, s, newData[schema.db]._checkedTables[table.table]);
                        return (
                          !newData[schema.db]._checkedTables[table.table] ||
                          (newData[schema.db]._checkedTables[table.table] && s.schema !== newData[schema.db]._checkedTables[table.table].sourceSchema)
                        );
                      });
                    });
                  }
                } else {
                  if (schema.collections) {
                    schema.collections = schema.collections.filter((table) => !newData[schema.db]._checkedTables[table.collection]);
                  } else {
                    schema.tables = schema.tables.filter((table) => !newData[schema.db]._checkedTables[table.table]);
                  }
                }

                return true;
              }
              return true;
            });
          }

          finalSourceSchema = JSON.stringify(parseSchema);

          if (isKafka(sourceType) && isKafka(targetType)) {
            // eslint-disable-next-line array-callback-return
            mappingDef.forEach((mapping) => {
              Object.values(newData).forEach((db) => {
                Object.keys(db._checkedTables).forEach((table) => {
                  const key = {
                    value: table
                  };
                  if (mapping.serializeMapping[JSON.stringify(key)]) {
                    delete mapping.serializeMapping[JSON.stringify(key)];
                  }
                });
              });
            });
          }
        }

        if (isRedis(sourceType) && isRedis(targetType)) {
          finalSourceSchema = JSON.stringify([]);
          finalTargetSchema = JSON.stringify([]);
          mappingDef = [];
        }

        return {
          allVirtualColumnList,
          allCustomVirtualColumnList,
          allDataTransformList,
          allEmbeddingList,
          structMigration,
          finalSourceSchema,
          finalTargetSchema,
          mappingDef
        };
      } catch (e) {
        console.log('err', e);
      }
    },
    newSetColumns(column, db, table, dbName, columnMapping, structMigration, sourceSchemaTable, targetSchemaTable, indexMetaMap, isVirtual) {
      const { sourceType, targetType } = this.taskInfo;
      const sourceColumns = {};
      const targetColumns = {};

      sourceColumns.column = column.sourceColumn;
      sourceColumns.targetAutoCreate = false;
      sourceColumns.inBlackList = true;
      targetColumns.column = column.targetColumn;
      targetColumns.targetAutoCreate = false;
      targetColumns.inBlackList = true;

      if (!column._checked) {
        targetColumns.inBlackList = true;
        sourceColumns.inBlackList = true;
      } else {
        targetColumns.inBlackList = false;
        sourceColumns.inBlackList = false;
        if (!isNoColumn(sourceType) && column.sourceColumn !== column.targetColumn) {
          if (isHasSchema(targetType)) {
            if (isOracle(targetType)) {
              // if (!(column.commonRule === 'UpperCase' || !this.taskInfo && DataSourceGroup.oracle.indexOf(sinkType) > -1)) {
              let key = {
                parent: {
                  value: table.sourceTable,
                  parent: {
                    value: dbName
                  }
                },
                value: column.sourceColumn
              };
              if (isHasSchema(sourceType)) {
                key = {
                  parent: {
                    value: table.sourceTable,
                    parent: {
                      value: db.sourceSchema,
                      parent: {
                        value: dbName
                      }
                    }
                  },
                  value: column.sourceColumn
                };
              }
              const value = {
                value: column.targetColumn,
                parent: {
                  value: table.targetTable,
                  parent: {
                    value: db.targetDb
                  }
                }
              };

              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              // }
            } else {
              if (isHasSchema(sourceType)) {
                const key = {
                  parent: {
                    value: table.sourceTable,
                    parent: {
                      value: db.sourceSchema,
                      parent: {
                        value: dbName
                      }
                    }
                  },
                  value: column.sourceColumn
                };
                const value = {
                  value: column.targetColumn,
                  parent: {
                    value: table.targetTable,
                    parent: {
                      value: db.targetSchema,
                      parent: {
                        value: db.targetDb
                      }
                    }
                  }
                };

                columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              } else {
                const key = {
                  parent: {
                    value: table.sourceTable,
                    parent: {
                      value: dbName
                    }
                  },
                  value: column.sourceColumn
                };
                const value = {
                  value: column.targetColumn,
                  parent: {
                    value: table.targetTable,
                    parent: {
                      value: db.targetSchema,
                      parent: {
                        value: db.targetDb
                      }
                    }
                  }
                };

                columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              }
            }
          } else {
            if (isHasSchema(sourceType)) {
              const key = {
                parent: {
                  value: table.sourceTable,
                  parent: {
                    value: db.sourceSchema,
                    parent: {
                      value: dbName
                    }
                  }
                },
                value: column.sourceColumn
              };
              const value = {
                value: column.targetColumn,
                parent: {
                  value: table.targetTable,
                  parent: {
                    value: db.targetDb
                  }
                }
              };

              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            } else if (isES(targetType)) {
              const key = {
                parent: {
                  value: table.sourceTable,
                  parent: {
                    value: dbName
                  }
                },
                value: column.sourceColumn
              };
              const value = {
                value: column.targetColumn,
                parent: {
                  value: table.targetTable
                }
              };

              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            } else {
              const key = {
                parent: {
                  value: table.sourceTable,
                  parent: {
                    value: dbName
                  }
                },
                value: column.sourceColumn
              };
              const value = {
                value: column.targetColumn,
                parent: {
                  value: table.targetTable,
                  parent: {
                    value: db.targetDb
                  }
                }
              };

              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            }
          }
        }
      }
      if (column.needAutoCreated && column._checked) {
        if (!(!table.needAutoCreated && isMQ(targetType) > -1)) {
          sourceColumns.targetAutoCreate = true;
          structMigration = true;
        }
      }
      if (!isVirtual) {
        sourceSchemaTable.columns.push(sourceColumns);
        targetSchemaTable.columns.push(targetColumns);
        if (isES(targetType)) {
          if (isES(sourceType)) {
            indexMetaMap[table.sourceTable].fieldMetaList.push({
              fieldName: column.sourceColumn,
              needIndex: column.isIndex,
              fieldTypeName: column.sinkType,
              esAnalyzerType: column.analyzer,
              targetAutoCreate: column.needAutoCreated,
              timeFormat: column.timeFormat,
              jsonValue: column.jsonValue,
              isPk: column.isPk,
              isId: column.isId,
              inBlackList: !column._checked
            });
          } else {
            if (column._checked) {
              indexMetaMap[table.targetTable].fieldMetaList.push({
                fieldName: column.targetColumn,
                needIndex: column.isIndex,
                fieldTypeName: column.sinkType,
                esAnalyzerType: column.analyzer,
                targetAutoCreate: column.needAutoCreated,
                timeFormat: column.timeFormat,
                jsonValue: column.jsonValue,
                isPk: column.isPk,
                isId: column.isId
              });
            }
          }
        }
      }
    },
    newParseCron(date) {
      let cron = '';

      if (date.dayType === 'DAY') {
        cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} * * ? *`;
        return cron;
      }
      if (date.dayType === 'MONTH') {
        cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} ${date.date} * ? *`;
        return cron;
      }
      if (date.dayType === 'YEAR') {
        cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} ${date.date} ${date.month} ? *`;
        return cron;
      }
      if (date.dayType === 'HOUR') {
        cron += `0 ${date.hour} * * * ? *`;
        return cron;
      }
      if (date.dayType === 'WEEK') {
        cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} ? * ${date.week}`;
        return cron;
      }
    },
    newGetTargetValue(target, rule) {
      switch (rule) {
        case 'TO_LOWER_CASE':
          return target.toLowerCase();
        case 'TO_UPPER_CASE':
          return target.toUpperCase();
        case 'MIRROR':
          return target;
        default:
          return target;
      }
    }
  }
};
