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
  isGrepTime,
  isTDengine,
  isFile,
  isNoDb,
  isRagApi,
  isDynamoDB,
  isTdsqlMySQL
} from '@/utils';
import _ from 'lodash';
import { parseCron } from '@/components/util';
import DataSourceGroup from '@/views/dataSourceGroup.json';

export default {
  data() {
    return {
      selectedColumns: {}
    };
  },
  methods: {
    containsDb(list, db) {
      let isSame = false;

      list.forEach((item) => {
        if (item.dbName === db) {
          isSame = true;
        }
      });
      return isSame;
    },
    containsSchema(list, db, schemaName) {
      let isSame = false;

      list.forEach((item) => {
        if (item.dbName === db) {
          item.schemas.forEach((schema) => {
            if (this.taskInfo.targetCaseSensitive === 'false') {
              if (db && schema) {
                if (schema.toUpperCase() === schemaName.toUpperCase()) {
                  isSame = true;
                }
              }
            } else if (schema === schemaName) {
              isSame = true;
            }
          });
        }
      });
      return isSame;
    },
    hasSelectedTheDb(item, type = 'sink') {
      for (const db of this.taskInfo.dbMap) {
        if (type === 'source' && item === db.sourceDb) {
          return true;
        }
        if (type === 'sink' && item === db.sinkDb) {
          return true;
        }
      }

      return false;
    },
    handleAddMap() {
      this.taskInfo.dbMap.push({
        selectedAll: false,
        selectedPage: false,
        pageIndeterminate: false,
        indeterminate: false,
        selectedTables: [],
        sourceDb: '',
        sinkDb: '',
        targetSchema: 'public',
        sourceSchema: 'public'
      });
    },
    getSchemaList(sinkDbList, db) {
      let list = [];

      sinkDbList.forEach((item) => {
        if (item.dbName === db) {
          list = item.schemas;
        }
      });
      return list;
    },
    getExistTargetDb(db, mappingJson) {
      let dbMethod = 'DB_DB';
      let schemaMethod = '';
      let targetDb = db.db;
      let targetSchema = db.schemas ? db.schemas[0].schema : db.tableSpaces ? db.tableSpaces[0].tableSpace : 'public';
      if (DataSourceGroup.sqlServer.includes(this.taskInfo.sinkType)) {
        targetSchema = 'dbo';
      }

      // 对于 MQ 源端（如 Kafka）到 hasSchema 类型对端（如 Oracle），使用 ANY_SCHEMA 方法
      if (isMQ(this.taskInfo.sourceType) && DataSourceGroup.hasSchema.includes(this.taskInfo.sinkType)) {
        dbMethod = 'ANY_SCHEMA';
      } else if (!DataSourceGroup.hasSchema.includes(this.taskInfo.sourceType) && DataSourceGroup.hasSchema.includes(this.taskInfo.sinkType)) {
        dbMethod = 'DB_SCHEMA';
      } else if (DataSourceGroup.hasSchema.includes(this.taskInfo.sourceType) && DataSourceGroup.hasSchema.includes(this.taskInfo.sinkType)) {
        schemaMethod = 'SCHEMA_SCHEMA';
      } else if (DataSourceGroup.hasSchema.includes(this.taskInfo.sourceType) && !DataSourceGroup.hasSchema.includes(this.taskInfo.sinkType)) {
        schemaMethod = 'SCHEMA_DB';
      } else if (isDynamoDB(this.taskInfo.sourceType)) {
        dbMethod = 'ANY_DB';
      }
      let dbMapping = {};
      let schemaMapping = {};
      mappingJson.forEach((mapping) => {
        if (mapping.method === dbMethod) {
          dbMapping = mapping.serializeMapping;
        } else if (mapping.method === schemaMethod) {
          schemaMapping = mapping.serializeMapping;
        }
      });
      console.log('dbMethod', dbMethod, dbMapping);

      // 处理 ANY_SCHEMA 映射（MQ 源端到 hasSchema 对端）
      if (dbMethod === 'ANY_SCHEMA') {
        const theKey = {
          value: 'ANY_SCHEMA'
        };
        Object.keys(dbMapping).forEach((theMapping) => {
          if (theMapping === JSON.stringify(theKey)) {
            const theValue = JSON.parse(dbMapping[theMapping]);
            if (theValue.parent) {
              targetDb = theValue.parent.value;
              targetSchema = theValue.value;
            } else {
              targetSchema = theValue.value;
            }
          }
        });
      } else {
        Object.keys(dbMapping).forEach((theMapping) => {
          const theKey = {
            value: db.db
          };
          if (theMapping === JSON.stringify(theKey)) {
            const theValue = JSON.parse(dbMapping[theMapping]);
            if (theValue.parent) {
              targetDb = theValue.parent.value;
              targetSchema = theValue.value;
            } else {
              targetDb = theValue.value;
            }
          }
        });
      }

      if (isDynamoDB(this.taskInfo.sourceType)) {
        const theKey = {
          value: 'ANY_DB'
        };
        Object.keys(dbMapping).forEach((theMapping) => {
          if (theMapping === JSON.stringify(theKey)) {
            const theValue = JSON.parse(dbMapping[theMapping]);
            targetDb = theValue.value;
          }
        });
      }

      console.log('targetDb', targetDb);
      Object.keys(schemaMapping).forEach((theMapping) => {
        let theKey = {
          value: db.db
        };
        if (schemaMethod === 'SCHEMA_SCHEMA' || schemaMethod === 'SCHEMA_DB') {
          theKey = {
            parent: {
              value: db.db
            },
            value: db.schemas ? db.schemas[0].schema : db.tableSpaces[0].tableSpace
          };
        }
        // console.log('theMapping', theMapping, JSON.stringify(theKey));
        if (theMapping === JSON.stringify(theKey)) {
          const theValue = JSON.parse(schemaMapping[theMapping]);
          // console.log('theValue', theValue);
          if (schemaMethod === 'SCHEMA_DB') {
            targetDb = theValue.value;
          } else {
            targetSchema = theValue.value;
          }
        }
      });
      return {
        db: targetDb,
        schema: targetSchema
      };
    },
    parseExistSchemaData(data) {
      console.log('parseExist', data);
      this.taskInfo.dbMap = [];
      const sourceSchemaJson = JSON.parse(data.sourceSchema);
      const mappingJson = JSON.parse(data.mappingConfig);
      const targetSchemaJson = data.targetSchema ? JSON.parse(data.targetSchema) : [];
      let schema = [];
      // console.log('sourceSchemaJson', sourceSchemaJson, mappingJson, targetSchemaJson);
      if (DataSourceGroup.noDb.includes(this.taskInfo.sourceType)) {
        if (isMQ(this.taskInfo.sinkType)) {
          schema = sourceSchemaJson;
        } else {
          schema = targetSchemaJson;
        }

        if (isDynamoDB(this.taskInfo.sourceType)) {
          schema = sourceSchemaJson;
        }
      } else {
        schema = sourceSchemaJson;
      }
      console.log('schema', schema);
      if (isMQ(this.taskInfo.sourceType) && isMQ(this.taskInfo.sinkType)) {
        const dbMapItem = {
          sourceDb: '',
          sinkDb: '',
          targetSchema: '',
          sourceSchema: '',
          needAutoCreated: '',
          actions: [],
          tableSelectMode: this.tableSelectMode,
          whiteTabs: ''
        };

        this.taskInfo.dbMap.push(dbMapItem);
      } else {
        schema.forEach((db) => {
          console.log('db', db);
          const whiteTabs = [];
          if (isHasSchema(this.taskInfo.sourceType)) {
            if (isOracle(this.taskInfo.sourceType)) {
              if (db.tableSpaces && Array.isArray(db.tableSpaces)) {
                db.tableSpaces.forEach((currentSchema) => {
                  if (currentSchema.tables && Array.isArray(currentSchema.tables)) {
                    currentSchema.tables.forEach((table) => {
                      whiteTabs.push(table.table);
                    });
                  }
                });
              }
            } else {
              if (db.schemas?.length) {
                db.schemas.forEach((currentSchema) => {
                  if (currentSchema.tables && Array.isArray(currentSchema.tables)) {
                    currentSchema.tables.forEach((table) => {
                      whiteTabs.push(table.table);
                    });
                  }
                });
              }
            }
          } else if (isMongoDB(this.taskInfo.sourceType) && !isDynamoDB(this.taskInfo.sourceType)) {
            if (db.collections && Array.isArray(db.collections)) {
              db.collections.forEach((table) => {
                whiteTabs.push(table.collection);
              });
            }
          } else {
            if (db.tables && Array.isArray(db.tables)) {
              db.tables.forEach((table) => {
                whiteTabs.push(table.table);
              });
            }
          }
          const dbMapItem = {
            sourceDb: db.db,
            sinkDb: this.getExistTargetDb(db, mappingJson).db,
            targetSchema: this.getExistTargetDb(db, mappingJson).schema,
            sourceSchema: db.schemas ? db.schemas[0].schema : db.tableSpaces ? db.tableSpaces[0].tableSpace : 'public',
            needAutoCreated: db.targetAutoCreate,
            actions: [], // 后续不再使用actions白名单，而是blackActs黑名单(表级别)
            tableSelectMode: this.tableSelectMode,
            whiteTabs: whiteTabs.join(';')
          };

          if (this.taskInfo.processType === 'edit') {
            dbMapItem.hideWhiteTabs = whiteTabs.join(';');
            dbMapItem.whiteTabs = '';
          }

          this.taskInfo.dbMap.push(dbMapItem);
        });
      }
      if (DataSourceGroup.es.includes(this.taskInfo.sinkType)) {
        // console.log('targetSchemaJson[0].globalTimeZone', targetSchemaJson[0].globalTimeZone);
        this.taskInfo.globalTimeZone = targetSchemaJson[0].globalTimeZone;
      }
    },
    getDataJobData() {
      this.taskInfo.coreConfigWrapper = {
        processorConfigList: [],
        processorConsoleConfigs: []
      };

      if (this.taskInfo.virtualColumns.length > 0) {
        this.taskInfo.coreConfigWrapper.processorConfigList.push({
          processorConfigType: 'FIELD_MAKER_PROCESSOR',
          allProcessorContextJson: JSON.stringify(this.taskInfo.virtualColumns)
        });
        this.taskInfo.coreConfigWrapper.processorConsoleConfigs.push({
          processorConfigType: 'FIELD_MAKER_PROCESSOR',
          allProcessorContextJson: JSON.stringify(this.taskInfo.customVirtualColumns)
        });
      }

      if (this.taskInfo.wideTables.length > 0) {
        this.taskInfo.coreConfigWrapper.processorConfigList.push({
          processorConfigType: 'WIDE_TABLE_PROCESSOR',
          allProcessorContextJson: JSON.stringify(this.taskInfo.wideTables)
        });
      }

      const transformColumnArr = Object.values(this.taskInfo.transformColumnData);
      if (transformColumnArr.length) {
        const list = [];
        transformColumnArr.forEach((transformColumn) => {
          const { db, schema, table, columns } = transformColumn;
          const fieldsScripts = {};
          columns.forEach((column) => {
            fieldsScripts[column.columnName] = column.script;
          });

          list.push({
            db,
            schema,
            table,
            fieldsScripts
          });
        });

        this.taskInfo.coreConfigWrapper.processorConfigList.push({
          processorConfigType: 'FIELD_TRANSFORM_PROCESSOR',
          allProcessorContextJson: JSON.stringify(list)
        });
      }
      // llm embedding
      if (this.taskInfo.embeddingData && this.taskInfo.embeddingData.length > 0) {
        const llmProcessor = {};
        llmProcessor.llmExtraFields = this.taskInfo.llmDefaultColInfo;
        llmProcessor.embeddingFields = this.taskInfo.embeddingData;
        this.taskInfo.coreConfigWrapper.processorConfigList.push({
          processorConfigType: 'LLM_EMBEDDING_PROCESSOR',
          allProcessorContextJson: JSON.stringify(llmProcessor)
        });
      }
      const taskInfo = _.cloneDeep(this.taskInfo);
      const configData = this.getConfigData(taskInfo.dbMap, taskInfo.sourceType, taskInfo.sinkType);

      if (this.taskInfo.globalTimeZone === 'NotSet') {
        this.taskInfo.globalTimeZone = null;
      }

      return {
        jobName: taskInfo.taskName,
        jobType: taskInfo.type,
        reviseBindCheckTaskId: taskInfo.reviseBindCheckTaskId,
        initialSync: taskInfo.type === 'SYNC' && taskInfo.mode.init,
        shortTermSync: taskInfo.type === 'MIGRATION' && taskInfo.mode.synchronize,
        shortTermNum: taskInfo.type === 'MIGRATION' && taskInfo.mode.synchronize ? taskInfo.mode.shortTermNum : 0,
        oraIncrMode: taskInfo.oraIncrMode,
        oraBuildRedoDicWhenCreate: taskInfo.oraBuildRedoDicWhenCreate,
        sourceHost: taskInfo.sourceInstance,
        sinkHost: taskInfo.sinkInstance,
        structMigration: configData.structMigration,
        dataJobDesc: taskInfo.desc,
        checkOnce: taskInfo.type === 'Revise' ? false : taskInfo.checkMode === 'checkOnce',
        checkPeriod: taskInfo.type === 'Revise' ? false : taskInfo.checkMode === 'checkPeriod',
        checkPeriodCronExpr: parseCron(taskInfo.checkPeriodDate),
        fullPeriod: taskInfo.fullPeriod,
        fullPeriodCronExpr: parseCron(taskInfo.fullPeriodDate),
        sourceCaseSensitive: JSON.parse(taskInfo.sourceCaseSensitive),
        targetCaseSensitive: JSON.parse(taskInfo.targetCaseSensitive),
        commonRule: taskInfo.commonRule,
        // useQualifiers:JSON.parse(taskInfo.useQualifiers),
        autoStart: JSON.parse(taskInfo.autoStart),
        clusterId: taskInfo.clusterId,
        specId: taskInfo.spec ? taskInfo.spec.id : 15,
        sourceDataSourceId: taskInfo.sourceDataSourceId,
        sourceHostType: taskInfo.sourceHostType,
        targetDataSourceId: taskInfo.targetDataSourceId,
        targetHostType: taskInfo.targetHostType,
        // indexMetaMap: indexMetaMap,
        globalTimeZone: this.taskInfo.globalTimeZone,
        srcRocketMqGroupId: this.taskInfo.consumerGroupId,
        srcRabbitMqVhost: this.taskInfo.srcRabbitMqVhost,
        srcRabbitExchange: this.taskInfo.srcRabbitExchange,
        dstRabbitMqVhost: this.taskInfo.dstRabbitMqVhost,
        dstRabbitExchange: this.taskInfo.dstRabbitExchange,
        kafkaConsumerGroupId: this.taskInfo.consumerGroupId,
        srcDsCharset: this.taskInfo.sourceCharset,
        tarDsCharset: this.taskInfo.targetCharset,
        srcSchemaLessFormat: this.taskInfo.srcSchemaLessFormat,
        originDecodeMsgFormat: this.taskInfo.originDecodeMsgFormat,
        dstSchemaLessFormat: this.taskInfo.dstSchemaLessFormat,
        schemaWhiteListLevel: this.taskInfo.schemaWhiteListLevel || null,
        dstMqDefaultTopic: this.taskInfo.dstMqDefaultTopic,
        dstMqDefaultTopicPartitions: this.taskInfo.dstMqDefaultTopicPartitions,
        dstMqDdlTopic: this.taskInfo.dstMqDdlTopic,
        dstMqDdlTopicPartitions: this.taskInfo.dstMqDdlTopicPartitions,
        dstCkTableEngine: this.taskInfo.dstCkTableEngine,
        dstSrOrDorisTableModel: this.taskInfo.dstSrOrDorisTableModel,
        targetTimeDefaultStrategy: this.taskInfo.targetTimeDefaultStrategy,
        keyConflictStrategy: isTiDB(this.taskInfo.sourceType) ? 'REPLACE' : this.taskInfo.keyConflictStrategy,
        filterDDL: JSON.parse(this.taskInfo.ddl),
        cleanTargetBeforeFull: this.taskInfo.cleanTargetBeforeFull,
        kuduNumReplicas: this.taskInfo.kuduNumReplicasSelected ? this.taskInfo.kuduNumReplicas : '',
        kuduNumBuckets: this.taskInfo.kuduNumBucketsSelected ? this.taskInfo.kuduNumBuckets : '',
        pkgDescription: this.taskInfo.pkgDescription,
        sourceSchema: configData.finalSourceSchema,
        targetSchema: configData.finalTargetSchema,
        mappingDef: JSON.stringify(configData.mappingDef),
        processorConfigList: this.taskInfo.coreConfigWrapper ? this.taskInfo.coreConfigWrapper.processorConfigList : [],
        syncPartitionInfo: this.taskInfo.syncPartitionInfo,
        migrationBucketNumber: taskInfo.migrationBucketNumber,
        migrationPropertiesConfig: taskInfo.sinkType === 'StarRocks' ? taskInfo.migrationPropertiesConfigWithSr : taskInfo.migrationPropertiesConfig,
        obTenant: taskInfo.obTenant,
        dataCheckType: taskInfo.checkMode === 'noCheck' ? null : taskInfo.dataCheckType,
        dataReviseType: taskInfo.checkMode === 'noCheck' ? null : taskInfo.dataReviseType,
        reSchemaMigration: taskInfo.reSchemaMigration,
        srcPulsarTenant: taskInfo.sourcePulsarTenant,
        srcPulsarNamespace: taskInfo.sourcePulsarNamespace,
        dstPulsarTenant: taskInfo.sinkPulsarTenant,
        dstPulsarNamespace: taskInfo.sinkPulsarNamespace,
        pushDownEnabled: JSON.parse(taskInfo.pushDownEnabled),
        llmEmbeddingName: taskInfo.llmConfig ? taskInfo.llmConfig.selectedConfig : '',
        llmEmbeddingDsId: taskInfo.llmConfig ? taskInfo.llmConfig.dsId : null,
        llmChatName: taskInfo.llmConfig ? taskInfo.llmConfig.llmChatName : '',
        llmChatDsId: taskInfo.llmConfig ? taskInfo.llmConfig.llmChatDsId : null,
        cgTableType: this.taskInfo.cgTableType,
        jobKvConfigTemplId: this.taskInfo.selectedTemplateId,
        patternMeta: taskInfo.patternMeta,
        ...this.taskInfo.advancedSetting
      };
    },
    getConfigData(list, sourceType, sinkType, type, data, selectedColumns) {
      try {
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

        dbMapping.method = 'DB_DB';
        dbMapping.serializeMapping = {};
        dbMapping.serializeAutoGenRules = {};
        dbMapping.commonGenRule = 'MIRROR';

        anyDbMapping.method = 'ANY_DB';
        anyDbMapping.serializeMapping = {};
        anyDbMapping.serializeAutoGenRules = {};
        anyDbMapping.commonGenRule = 'MIRROR';

        anySchemaMapping.method = 'ANY_SCHEMA';
        anySchemaMapping.serializeMapping = {};
        anySchemaMapping.serializeAutoGenRules = {};
        anySchemaMapping.commonGenRule = 'MIRROR';

        if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
          if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
            schemaMapping.method = 'SCHEMA_SCHEMA';
          } else {
            schemaMapping.method = 'SCHEMA_DB';
          }
        } else if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
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
        topicIndexMapping.method = 'TOPIC_INDEX';
        topicIndexMapping.serializeAutoGenRules = {};
        topicIndexMapping.commonGenRule = 'MIRROR';

        tableIndexMapping.serializeMapping = {};
        tableIndexMapping.method = 'TABLE_INDEX';
        tableIndexMapping.serializeAutoGenRules = {};
        tableIndexMapping.commonGenRule = 'MIRROR';

        indexIndexMapping.serializeMapping = {};
        indexIndexMapping.method = 'TABLE_TABLE';
        indexIndexMapping.serializeAutoGenRules = {};
        indexIndexMapping.commonGenRule = 'MIRROR';

        schemaIndexMapping.serializeMapping = {};
        schemaIndexMapping.method = 'TABLE_INDEX';
        schemaIndexMapping.serializeAutoGenRules = {};
        schemaIndexMapping.commonGenRule = 'MIRROR';

        tableMapping.serializeMapping = {};
        tableMapping.method = 'TABLE_TABLE';
        tableMapping.serializeAutoGenRules = {};
        if (['MIRROR', 'TO_UPPER_CASE', 'TO_LOWER_CASE'].includes(this.taskInfo.selectedTableMapping)) {
          tableMapping.commonGenRule = this.taskInfo.selectedTableMapping;
        } else {
          tableMapping.commonGenRule = 'MIRROR';
        }

        noTargetDbTableMapping.serializeMapping = {};
        noTargetDbTableMapping.method = 'TABLE_TABLE';
        noTargetDbTableMapping.serializeAutoGenRules = {};
        noTargetDbTableMapping.commonGenRule = 'MIRROR';
        columnMapping.method = 'COLUMN_COLUMN';
        columnMapping.serializeMapping = {};
        columnMapping.serializeAutoGenRules = {};
        if (['MIRROR', 'TO_UPPER_CASE', 'TO_LOWER_CASE'].includes(this.taskInfo.selectedColumnMappingRule)) {
          columnMapping.commonGenRule = this.taskInfo.selectedColumnMappingRule;
        } else {
          columnMapping.commonGenRule = 'MIRROR';
        }
        list.forEach((db) => {
          console.log(db);
          const sourceDbSchema = {};
          const targetDbSchema = {};

          sourceDbSchema.db = db.sourceDb;
          sourceDbSchema.actions = []; // 后续不再使用actions白名单，而是blackActs黑名单(表级别)
          // sourceDbSchema.new = db.new;
          if (DataSourceGroup.oracle.indexOf(sourceType) === -1) {
            if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
              sourceDbSchema.schemas = [];
              sourceDbSchema.schemas.push({
                schema: db.sourceSchema,
                schemaPattern: '',
                tables: [],
                targetAutoCreate: DataSourceGroup.hasSchema.indexOf(sinkType) > -1 ? db.schemaAutoCreate : db.needAutoCreated
              });
            } else {
              if (DataSourceGroup.mongo.includes(sourceType) && !isDynamoDB(sourceType)) {
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
              targetAutoCreate: DataSourceGroup.hasSchema.indexOf(sinkType) > -1 ? db.schemaAutoCreate : db.needAutoCreated
            });
          }
          if (DataSourceGroup.mq.indexOf(sinkType) > -1) {
            targetDbSchema.topics = [];
          } else {
            targetDbSchema.db = db.sinkDb;
            targetDbSchema.dbPattern = '';
            if (DataSourceGroup.pg.indexOf(sinkType) > -1) {
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

          if (DataSourceGroup.noDb.includes(sourceType)) {
            if (DataSourceGroup.mq.indexOf(sourceType) > -1 || isDynamoDB(sourceType)) {
              if (DataSourceGroup.hasSchema.includes(sinkType)) {
                const key = {
                  value: 'ANY_SCHEMA'
                };
                const value = {
                  parent: {
                    value: db.sinkDb
                  },
                  value: db.targetSchema
                };
                anySchemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              } else {
                const key = {
                  value: 'ANY_DB'
                };
                const value = {
                  value: db.sinkDb
                };

                anyDbMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              }
            } else if (data && data[db] && data[db].sourceDb !== data[db].targetDb) {
              const key = {
                value: data[db].sourceDb
              };
              const value = {
                value: data[db].targetDb
              };

              dbMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            }
          } else if (dbName !== db.sinkDb) {
            const key = {
              value: dbName
            };
            const value = {
              value: db.sinkDb
            };

            dbMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
          }
          if (db.needAutoCreated || (isHasSchema(sinkType) && !isHasSchema(sourceType) && db.schemaAutoCreate)) {
            sourceDbSchema.targetAutoCreate = true;
            structMigration = true;
          }
          // console.log('DataSourceGroup.hasSchema.indexOf(this.sourceType)', sourceType, DataSourceGroup.hasSchema.indexOf(sourceType));
          if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
            if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
              if (dbName !== db.sinkDb || db.sourceSchema !== db.targetSchema) {
                const key = {
                  parent: {
                    value: dbName
                  },
                  value: db.sourceSchema
                };
                const value = {
                  parent: {
                    value: db.sinkDb
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
                value: db.sinkDb
              };

              schemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            }
          } else if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1 && !DataSourceGroup.kafka.includes(sourceType)) {
            // console.log(23333);
            if (!dbName) {
              const key = {
                value: db.sourceDb
              };
              const value = {
                parent: {
                  value: db.targetDb
                },
                value: data[db].targetSchema
              };

              schemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            } else {
              const key = {
                value: dbName
              };
              const value = {
                parent: {
                  value: db.sinkDb
                },
                value: db.targetSchema
              };

              schemaMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            }
          }
          if (db.selectedTables) {
            tableList = db.selectedTables;
          } else if (data) {
            data[dbName].tableList.forEach((item) => {
              item.selected = true;
            });
            tableList = data[dbName].tableList;
          }
          if (this.taskInfo.schemaWhiteListLevel === 'DB') {
            tableList = [];
          }
          tableList.forEach((table) => {
            const sourceSchemaTable = {};
            const targetSchemaTable = {};
            sourceSchemaTable.table = table.sourceTable || table.tablePattern;
            sourceSchemaTable.pattern = db.isTablePattern;
            sourceSchemaTable.columns = [];
            sourceSchemaTable.actions = []; // 后续不再使用actions白名单，而是blackActs黑名单
            sourceSchemaTable.blackActs = table.actionBlacklist;
            sourceSchemaTable.inBlackList = true;
            sourceSchemaTable.targetAutoCreate = false;
            sourceSchemaTable.specifiedPks = table.cols || [];
            // if (isStarRocks(sinkType)) {
            //   sourceSchemaTable.specifiedDisKeys = table.specifiedDisKeys || [];
            // }
            targetSchemaTable.columns = [];
            if (!this.taskInfo || !this.taskInfo.schemaWhiteListLevel) {
              if (table.selected || (type === 'reduceData' && table.hasInJob)) {
                targetSchemaTable.table = table.sinkTable ? table.sinkTable : table.targetTable;
                if (DataSourceGroup.drds.indexOf(sinkType) > -1) {
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

                if (isTdsqlMySQL(sinkType)) {
                  if (table.shardTableType) {
                    targetSchemaTable.shardTableType = table.shardTableType;
                  }
                  if (table.partitionCols) {
                    targetSchemaTable.partitionCols = table.partitionCols;
                  }
                  if (table.partitionExpr) {
                    targetSchemaTable.partitionExpr = table.partitionExpr;
                  }
                }
                // set timeIndex to targetSchema
                if (isGrepTime(sinkType) && table.timeIndex) {
                  targetSchemaTable.timeIndexes = [table.timeIndex];
                }
                if (hasPartitionExpr(sinkType)) {
                  if (table.partitionExpr) {
                    targetSchemaTable.partitionExpr = table.partitionExpr;
                  }
                }
                if (DataSourceGroup.hive.indexOf(sinkType) > -1 && table.partitionData) {
                  const partitionKeyList = [];
                  partitionKeyList.push({
                    keyName: table.partitionData.partition[0].key,
                    partitionFunction: table.partitionData.partition[0].func
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
                  table.sourceTable !== table.sinkTable
                    ? table.sinkTable
                    : table.targetTable || this.getTargetValue(table.sourceTable, tableMapping.commonGenRule) !== table.sinkTable
                      ? table.sinkTable
                      : table.targetTable
                ) {
                  if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
                    if (DataSourceGroup.oracle.indexOf(sinkType) > -1) {
                      const key =
                        DataSourceGroup.hasSchema.indexOf(sourceType) > -1
                          ? {
                              parent: {
                                value: table.sourceSchema,
                                parent: {
                                  value: table.sourceDb || table.db
                                }
                              },
                              value: table.sourceTable
                            }
                          : {
                              parent: {
                                value: table.sourceDb || table.db
                              },
                              value: table.sourceTable
                            };
                      const value = {
                        value: table.sinkTable ? table.sinkTable : table.targetTable,
                        parent: {
                          value: table.targetSchema,
                          parent: {
                            value: table.sinkDb ? table.sinkDb : table.targetDb
                          }
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    } else {
                      if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
                        const key = {
                          parent: {
                            value: table.sourceSchema,
                            parent: {
                              value: table.sourceDb || table.db
                            }
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.sinkTable ? table.sinkTable : table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.sinkDb ? table.sinkDb : table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      } else {
                        const key = {
                          parent: {
                            value: table.sourceDb || table.db
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.sinkTable ? table.sinkTable : table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.sinkDb ? table.sinkDb : table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      }
                    }
                  } else {
                    if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
                      if (DataSourceGroup.oracle.includes(sourceType) && DataSourceGroup.kudu.includes(sinkType)) {
                        const key = {
                          parent: {
                            parent: {
                              value: table.sourceDb || table.db
                            },
                            value: table.sourceSchema
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.sinkTable ? table.sinkTable : table.targetTable
                        };

                        noTargetDbTableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      } else {
                        const key = {
                          parent: {
                            value: table.sourceSchema,
                            parent: {
                              value: table.sourceDb || table.db
                            }
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.sinkTable ? table.sinkTable : table.targetTable,
                          parent: {
                            value: table.sinkDb ? table.sinkDb : table.targetDb
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      }
                    } else {
                      const key = {
                        parent: {
                          value: table.sourceDb || table.db
                        },
                        value: table.sourceTable || table.tablePattern
                      };
                      const value = {
                        value: table.sinkTable ? table.sinkTable : table.targetTable,
                        parent: {
                          value: table.sinkDb ? table.sinkDb : table.targetDb
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    }
                  }
                }
                if (DataSourceGroup.mq.indexOf(sinkType) > -1 || isRagApi(sinkType) || isDynamoDB(sinkType)) {
                  if (DataSourceGroup.hasSchema.includes(sourceType)) {
                    const key = {
                      parent: {
                        parent: {
                          value: table.sourceDb || table.db
                        },
                        value: table.sourceSchema
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    schemaTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else if (isKafka(sourceType) || isRocketMQ(sourceType) || isPulsar(sourceType)) {
                    const key = {
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    topicTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const key = {
                      parent: {
                        value: table.sourceDb || table.db
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    tableTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    topicTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (DataSourceGroup.redis.includes(sinkType)) {
                  const key = {
                    parent: {
                      value: table.sourceDb || table.db
                    },
                    value: table.sourceTable
                  };
                  const value = {
                    value: table.sinkTable ? table.sinkTable : table.targetTable
                  };

                  tableKeyPrefixMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (DataSourceGroup.es.indexOf(sinkType) > -1) {
                  if (isHasSchema(sourceType)) {
                    const key = {
                      parent: {
                        parent: {
                          value: table.sourceDb || table.db
                        },
                        value: table.sourceSchema
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    schemaIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                  if (DataSourceGroup.es.indexOf(sourceType) > -1) {
                    const key = {
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    indexIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const key = {
                      parent: {
                        value: table.sourceDb || table.db
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    tableIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
                  const key = {
                    value: table.sourceTable
                  };

                  if (DataSourceGroup.noDb.includes(sinkType)) {
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    topicIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const value = {
                      parent: {
                        value: table.sinkDb ? table.sinkDb : table.targetDb
                      },
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    topicTableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (DataSourceGroup.es.indexOf(sinkType) > -1) {
                  if (isES(sourceType)) {
                    indexMetaMap[table.sourceTable] = {};
                    indexMetaMap[table.sourceTable].indexName = table.sourceTable;
                    indexMetaMap[table.sourceTable].numberOfShards = table.shards;
                    indexMetaMap[table.sourceTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.sourceTable].fieldMetaList = [];
                  } else {
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable] = {};
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].indexName = table.sinkTable
                      ? table.sinkTable
                      : table.targetTable;
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].numberOfShards = table.shards;
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].fieldMetaList = [];
                  }
                }
                if (isMongoDB(sourceType) && !isDynamoDB(sourceType) && table.idMapping) {
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
                      value: table.sinkTable ? table.sinkTable : table.targetTable,
                      parent: {
                        value: db.sinkDb
                      }
                    }
                  };

                  columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (table.needAutoCreated && table.selected) {
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

                let selectedColumnList = [];

                if (db.selectedColumns) {
                  if (DataSourceGroup.mq.indexOf(sourceType) > -1 || isDynamoDB(sourceType)) {
                    selectedColumnList = db.selectedColumns[table.sinkTable ? table.sinkTable : table.targetTable];
                  } else {
                    selectedColumnList = db.selectedColumns[table.sourceTable];
                  }
                  if (this.selectedColumns[db] && this.selectedColumns[db][table.sourceTable]) {
                    selectedColumnList = this.selectedColumns[db][table.sourceTable];
                  }
                } else if (selectedColumns) {
                  selectedColumnList = selectedColumns[dbName][table.sourceTable];
                }
                if (selectedColumnList) {
                  selectedColumnList.forEach((column) => {
                    column.selected = column._checked;
                    this.setColumns(column, db, table, dbName, columnMapping, structMigration, sourceSchemaTable, targetSchemaTable, indexMetaMap);
                  });
                }
                let path = '';
                if (isHasSchema(this.taskInfo.sourceType)) {
                  path = `/${table.sourceDb}/${table.sourceSchema}/${table.sourceTable}`;
                } else {
                  path = `/${table.sourceDb}/${table.sourceTable}`;
                }
                if (
                  this.taskInfo.virtualColumnData &&
                  this.taskInfo.virtualColumnData[path] &&
                  this.taskInfo.selectedColumnMappingRule !== 'MIRROR'
                ) {
                  this.taskInfo.virtualColumnData[path].forEach((column) => {
                    column.sourceColumn = column.customName || column.targetVirtualName;
                    column.selected = true;
                    column.sinkColumn = this.getTargetValue(column.sourceColumn, this.taskInfo.selectedColumnMappingRule);
                    this.setColumns(
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
                if (DataSourceGroup.pg.indexOf(sourceType) > -1 || isDb2(sourceType) || isFile(sourceType)) {
                  sourceDbSchema.schemas[0].tables.push(sourceSchemaTable);
                } else if (DataSourceGroup.oracle.indexOf(sourceType) > -1) {
                  sourceDbSchema.tableSpaces[0].tables.push(sourceSchemaTable);
                } else if (DataSourceGroup.mongo.indexOf(sourceType) > -1 && !isDynamoDB(sourceType)) {
                  sourceDbSchema.collections.push({
                    collection: sourceSchemaTable.table,
                    actions: sourceSchemaTable.actions,
                    targetAutoCreate: sourceSchemaTable.targetAutoCreate,
                    specifiedPks: table.cols || []
                  });
                } else if (isTDengine(this.taskInfo.sourceType)) {
                  if (!this.taskInfo.tableInfo.length) return;
                  const superTable = this.taskInfo.tableInfo.find((item) => item.index === table?.index);
                  if (superTable?.subWhereCondition) {
                    sourceSchemaTable.subWhereCondition = superTable.subWhereCondition;
                  }
                  sourceSchemaTable.tableType = table?.tableType || '';
                  sourceDbSchema.tables.push(sourceSchemaTable);
                } else {
                  sourceDbSchema.tables.push(sourceSchemaTable);
                }
                if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
                  if (DataSourceGroup.rabbitMq.indexOf(sourceType) > -1) {
                    topics.push({
                      queue: table.sourceTable,
                      queuePattern: '',
                      inBlackList: !table.selected || type === 'reduceData',
                      targetAutoCreate: table.needAutoCreated
                    });
                  } else if (isKafka(sourceType)) {
                    const fields = [];
                    if (selectedColumnList) {
                      selectedColumnList.forEach((column) => {
                        fields.push({
                          name: column.sinkColumn,
                          iskey: column.isPk,
                          inBlackList: !column.selected,
                          targetAutoCreate: false
                        });
                      });
                    }
                    topics.push({
                      topic: table.sourceTable,
                      topicPattern: '',
                      partitions: table.partitions,
                      pulsarTopicPersistent: table.needAutoCreated ? true : table.pulsarTopicPersistent,
                      partitionKeys: table.partitionKeys,
                      inBlackList: !table.selected || type === 'reduceData',
                      targetAutoCreate: table.needAutoCreated,
                      specifiedPks: table.cols || [],
                      fields
                    });
                  } else {
                    topics.push({
                      topic: table.sourceTable,
                      topicPattern: '',
                      partitions: table.partitions,
                      pulsarTopicPersistent: table.needAutoCreated ? true : table.pulsarTopicPersistent,
                      partitionKeys: table.partitionKeys,
                      inBlackList: !table.selected || type === 'reduceData',
                      targetAutoCreate: table.needAutoCreated
                    });
                  }

                  if (isKafka(sinkType) || isPulsar(sinkType)) {
                    if (DataSourceGroup.rabbitMq.indexOf(sinkType) > -1) {
                      targetTopics.push({
                        queue: table.sinkTable ? table.sinkTable : table.targetTable,
                        queuePattern: ''
                      });
                    } else {
                      console.log(3);
                      targetTopics.push({
                        topic: table.sinkTable ? table.sinkTable : table.targetTable,
                        topicPattern: '',
                        partitions: isKafka(sourceType) && table.sinkPartitions ? table.sinkPartitions : table.partitions,
                        pulsarTopicPersistent: table.needAutoCreated ? true : table.sinkPulsarTopicPersistent,
                        partitionKeys: table.partitionKeys
                      });
                    }
                  }

                  if (isES(sinkType)) {
                    let fileds = [];
                    if (indexMetaMap[table.sinkTable || table.targetTable]) {
                      fileds = indexMetaMap[table.sinkTable || table.targetTable].fieldMetaList;
                    }
                    const idFields = [];
                    const pkFields = [];

                    fileds.forEach((field) => {
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
                      indexName: table.sinkTable ? table.sinkTable : table.targetTable,
                      fields: fileds,
                      numberOfShards: table.shards,
                      numberOfReplicas: table.replication,
                      globalTimeZone: this.taskInfo.globalTimeZone,
                      idFieldNames: idFields.length ? idFields : pkFields
                    });
                  }
                } else if (DataSourceGroup.mq.indexOf(sinkType) > -1) {
                  if (DataSourceGroup.rabbitMq.indexOf(sinkType) > -1) {
                    topics.push({
                      queue: table.sinkTable ? table.sinkTable : table.targetTable,
                      queuePattern: ''
                    });
                  } else {
                    console.log(4);
                    topics.push({
                      topic: table.sinkTable ? table.sinkTable : table.targetTable,
                      topicPattern: '',
                      partitions: table.partitions,
                      pulsarTopicPersistent: table.needAutoCreated ? true : table.sinkPulsarTopicPersistent,
                      partitionKeys: table.partitionKeys
                    });
                  }
                } else {
                  if (DataSourceGroup.es.indexOf(sourceType) > -1) {
                    let fileds = [];
                    if (indexMetaMap[table.sourceTable]) {
                      fileds = indexMetaMap[table.sourceTable].fieldMetaList;
                    }
                    const idFields = [];
                    const pkFields = [];

                    fileds.forEach((field) => {
                      // console.log(field);
                      if (field.isPk) {
                        pkFields.push(field.fieldName);
                      }
                      if (field.isId) {
                        idFields.push(field.fieldName);
                      }
                    });
                    console.log('source es', fileds);

                    sourceEsIndex.push({
                      indexName: table.sourceTable,
                      fields: fileds,
                      numberOfShards: table.shards,
                      numberOfReplicas: table.replication,
                      globalTimeZone: this.taskInfo.globalTimeZone,
                      idFieldNames: idFields.length ? idFields : pkFields,
                      targetAutoCreate: table.needAutoCreated
                    });
                  }
                  if (DataSourceGroup.redis.includes(sinkType)) {
                    redisKeys.push({
                      prefix: table.sinkTable,
                      suffixFields: table.suffixFields || []
                    });
                  } else if (DataSourceGroup.es.indexOf(sinkType) > -1) {
                    console.log('sinkES');
                    let fileds = [];
                    if (indexMetaMap[table.sinkTable || table.targetTable]) {
                      fileds = indexMetaMap[table.sinkTable || table.targetTable].fieldMetaList;
                    }
                    const idFields = [];
                    const pkFields = [];

                    fileds.forEach((field) => {
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
                      indexName: table.sinkTable ? table.sinkTable : table.targetTable,
                      fields: fileds,
                      numberOfShards: table.shards,
                      numberOfReplicas: table.replication,
                      globalTimeZone: this.taskInfo.globalTimeZone,
                      idFieldNames: idFields.length ? idFields : pkFields
                    });
                  } else if (DataSourceGroup.pg.indexOf(sinkType) > -1) {
                    targetDbSchema.schemas[0].tables.push(targetSchemaTable);
                  } else if (hasPartitionExpr(sinkType)) {
                    if (table.partitionExpr) {
                      targetDbSchema.tables.push(targetSchemaTable);
                    }
                  } else if (isGrepTime(sinkType) && table.timeIndex) {
                    targetDbSchema.tables.push(targetSchemaTable);
                  } else {
                    targetDbSchema.tables.push(targetSchemaTable);
                  }
                }
              }
            } else {
              if (table.selected || !this.taskInfo.schemaWhiteListLevel) {
                if (type !== 'reduceData') {
                  targetSchemaTable.inBlackList = false;
                  sourceSchemaTable.inBlackList = false;
                }
                targetSchemaTable.table = table.sinkTable ? table.sinkTable : table.targetTable;
                if (this.taskInfo && DataSourceGroup.drds.indexOf(this.taskInfo.sinkType) > -1) {
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
                if (isTdsqlMySQL(sinkType)) {
                  if (table.shardTableType) {
                    targetSchemaTable.shardTableType = table.shardTableType;
                  }
                  if (table.partitionCols) {
                    targetSchemaTable.partitionCols = table.partitionCols;
                  }
                  if (table.partitionExpr) {
                    targetSchemaTable.partitionExpr = table.partitionExpr;
                  }
                }
                if (isGrepTime(sinkType) && table.timeIndex) {
                  targetSchemaTable.timeIndexes = [table.timeIndex];
                }
                if (hasPartitionExpr(sinkType)) {
                  if (table.partitionExpr) {
                    targetSchemaTable.partitionExpr = table.partitionExpr;
                  }
                }
                if (DataSourceGroup.hive.indexOf(sinkType) > -1 && table.partitionData) {
                  const partitionKeyList = [];
                  partitionKeyList.push({
                    keyName: table.partitionData.partition[0].key,
                    partitionFunction: table.partitionData.partition[0].func
                  });
                  targetSchemaTable.partitionKeys = partitionKeyList;
                  targetSchemaTable.fileFormat = table.partitionData.fileFormat;
                  targetSchemaTable.compressType = table.partitionData.compressType;
                }
                if (
                  table.sourceTable !== table.sinkTable
                    ? table.sinkTable
                    : table.targetTable || this.getTargetValue(table.sourceTable, tableMapping.commonGenRule) !== table.sinkTable
                      ? table.sinkTable
                      : table.targetTable
                ) {
                  if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
                    if (DataSourceGroup.oracle.indexOf(sinkType) > -1) {
                      const key = {
                        parent: {
                          value: table.sourceDb || table.db
                        },
                        value: table.sourceTable
                      };
                      const value = {
                        value: table.sinkTable ? table.sinkTable : table.targetTable,
                        parent: {
                          value: table.targetSchema,
                          parent: {
                            value: table.sinkDb ? table.sinkDb : table.targetDb
                          }
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    } else {
                      if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
                        const key = {
                          parent: {
                            value: table.sourceSchema,
                            parent: {
                              value: table.sourceDb || table.db
                            }
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.sinkTable ? table.sinkTable : table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.sinkDb ? table.sinkDb : table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      } else {
                        const key = {
                          parent: {
                            value: table.sourceDb || table.db
                          },
                          value: table.sourceTable
                        };
                        const value = {
                          value: table.sinkTable ? table.sinkTable : table.targetTable,
                          parent: {
                            value: table.targetSchema,
                            parent: {
                              value: table.sinkDb ? table.sinkDb : table.targetDb
                            }
                          }
                        };

                        tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                      }
                    }
                  } else {
                    if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
                      const key = {
                        parent: {
                          value: table.sourceSchema,
                          parent: {
                            value: table.sourceDb || table.db
                          }
                        },
                        value: table.sourceTable
                      };
                      const value = {
                        value: table.sinkTable ? table.sinkTable : table.targetTable,
                        parent: {
                          value: table.sinkDb ? table.sinkDb : table.targetDb
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    } else {
                      const key = {
                        parent: {
                          value: table.sourceDb || table.db
                        },
                        value: table.sourceTable
                      };
                      const value = {
                        value: table.sinkTable ? table.sinkTable : table.targetTable,
                        parent: {
                          value: table.sinkDb ? table.sinkDb : table.targetDb
                        }
                      };

                      tableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                    }
                  }
                }
                if (DataSourceGroup.mq.indexOf(sinkType) > -1 || isRagApi(sinkType)) {
                  const key = {
                    parent: {
                      value: table.sourceDb || table.db
                    },
                    value: table.sourceTable
                  };
                  const value = {
                    value: table.sinkTable ? table.sinkTable : table.targetTable
                  };

                  tableTopicMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (DataSourceGroup.redis.indexOf(sinkType) > -1) {
                  const key = {
                    parent: {
                      value: table.sourceDb || table.db
                    },
                    value: table.sourceTable
                  };
                  const value = {
                    value: table.sinkTable ? table.sinkTable : table.targetTable
                  };

                  tableKeyPrefixMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (DataSourceGroup.es.indexOf(sinkType) > -1) {
                  if (DataSourceGroup.es.indexOf(sourceType) > -1) {
                    const key = {
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    indexIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  } else {
                    const key = {
                      parent: {
                        value: table.sourceDb || table.db
                      },
                      value: table.sourceTable
                    };
                    const value = {
                      value: table.sinkTable ? table.sinkTable : table.targetTable
                    };

                    tableIndexMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                  }
                }
                if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
                  const key = {
                    value: table.sourceTable
                  };
                  const value = {
                    parent: {
                      value: table.sinkDb ? table.sinkDb : table.targetDb
                    },
                    value: table.sinkTable ? table.sinkTable : table.targetTable
                  };

                  topicTableMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                }
                if (DataSourceGroup.es.indexOf(sinkType) > -1) {
                  if (isES(sourceType)) {
                    indexMetaMap[table.sourceTable] = {};
                    indexMetaMap[table.sourceTable].indexName = table.sourceTable;
                    indexMetaMap[table.sourceTable].numberOfShards = table.shards;
                    indexMetaMap[table.sourceTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.sourceTable].fieldMetaList = [];
                  } else {
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable] = {};
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].indexName = table.sinkTable
                      ? table.sinkTable
                      : table.targetTable;
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].numberOfShards = table.shards;
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].numberOfReplicas = table.replication;
                    indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].fieldMetaList = [];
                  }
                }

                if (isMongoDB(sourceType) && !isDynamoDB(sourceType) && table.idMapping) {
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
                      value: table.sinkTable ? table.sinkTable : table.targetTable,
                      parent: {
                        value: db.sinkDb
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
              if (table.needAutoCreated && table.selected) {
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

              if (db.selectedColumns) {
                if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
                  selectedColumnList = db.selectedColumns[table.sinkTable ? table.sinkTable : table.targetTable];
                } else {
                  selectedColumnList = db.selectedColumns[table.sourceTable];
                }
                if (this.selectedColumns[db] && this.selectedColumns[db][table.sourceTable]) {
                  selectedColumnList = this.selectedColumns[db][table.sourceTable];
                }
              } else if (selectedColumns) {
                selectedColumnList = selectedColumns[dbName][table.sourceTable];
              }
              if (selectedColumnList) {
                selectedColumnList.forEach((column) => {
                  const sourceColumns = {};
                  const targetColumns = {};

                  sourceColumns.column = column.sourceColumn;
                  sourceColumns.targetAutoCreate = false;
                  sourceColumns.inBlackList = true;
                  targetColumns.column = column.sinkColumn;
                  targetColumns.targetAutoCreate = false;
                  targetColumns.inBlackList = true;
                  if (!this.taskInfo || !this.taskInfo.schemaWhiteListLevel) {
                    if (column.selected) {
                      targetColumns.inBlackList = false;
                      sourceColumns.inBlackList = false;
                      console.log('column.sourceColumn', column.sourceColumn, column.sinkColumn);

                      const isMapiing =
                        column.sourceColumn !== column.sinkColumn
                          ? column.sinkColumn
                          : column.targetColumn || this.getTargetValue(column.sourceColumn, columnMapping.commonGenRule) !== column.sinkColumn
                            ? column.sinkColumn
                            : column.targetColumn;
                      if (!isNoColumn(this.taskInfo.sourceType) && isMapiing) {
                        if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
                          if (DataSourceGroup.oracle.indexOf(sinkType) > -1) {
                            if (!(column.commonRule === 'UpperCase' || (!this.taskInfo && DataSourceGroup.oracle.indexOf(sinkType) > -1))) {
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
                                value: column.sinkColumn,
                                parent: {
                                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                                  parent: {
                                    value: db.sinkDb
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          } else {
                            if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
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
                                value: column.sinkColumn,
                                parent: {
                                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.sinkDb
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
                                value: column.sinkColumn,
                                parent: {
                                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.sinkDb
                                    }
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          }
                        } else {
                          if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
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
                              value: column.sinkColumn,
                              parent: {
                                value: table.sinkTable ? table.sinkTable : table.targetTable,
                                parent: {
                                  value: db.sinkDb
                                }
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          } else if (DataSourceGroup.es.includes(sinkType)) {
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
                              value: column.sinkColumn,
                              parent: {
                                value: table.sinkTable ? table.sinkTable : table.targetTable
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
                              value: column.sinkColumn,
                              parent: {
                                value: table.sinkTable ? table.sinkTable : table.targetTable,
                                parent: {
                                  value: db.sinkDb
                                }
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          }
                        }
                      }
                      if (column.needAutoCreated && column.selected) {
                        if (!(!table.needAutoCreated && DataSourceGroup.mq.indexOf(sinkType) > -1)) {
                          sourceColumns.targetAutoCreate = true;
                          structMigration = true;
                        }
                      }
                      console.log('push columns 1');
                      sourceSchemaTable.columns.push(sourceColumns);
                      targetSchemaTable.columns.push(targetColumns);
                      if (DataSourceGroup.es.indexOf(sinkType) > -1 && column._checked) {
                        indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].fieldMetaList.push({
                          fieldName: column.sinkColumn,
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
                    if (column.selected) {
                      targetColumns.inBlackList = false;

                      sourceColumns.inBlackList = false;
                      console.log('column.sourceColumn', column.sourceColumn, column.sinkColumn);
                      const isMapiing =
                        column.sourceColumn !== column.sinkColumn
                          ? column.sinkColumn
                          : column.targetColumn || this.getTargetValue(column.sourceColumn, columnMapping.commonGenRule) !== column.sinkColumn
                            ? column.sinkColumn
                            : column.targetColumn;
                      if (!isNoColumn(this.taskInfo.sourceType) && isMapiing) {
                        if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
                          if (DataSourceGroup.oracle.indexOf(sinkType) > -1) {
                            if (!(column.commonRule === 'UpperCase' || (!this.taskInfo && DataSourceGroup.oracle.indexOf(sinkType) > -1))) {
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
                                value: column.sinkColumn,
                                parent: {
                                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                                  parent: {
                                    value: db.sinkDb
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          } else {
                            if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
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
                                value: column.sinkColumn,
                                parent: {
                                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.sinkDb
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
                                value: column.sinkColumn,
                                parent: {
                                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                                  parent: {
                                    value: db.targetSchema,
                                    parent: {
                                      value: db.sinkDb
                                    }
                                  }
                                }
                              };

                              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                            }
                          }
                        } else {
                          if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
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
                              value: column.sinkColumn,
                              parent: {
                                value: table.sinkTable ? table.sinkTable : table.targetTable,
                                parent: {
                                  value: db.sinkDb
                                }
                              }
                            };

                            columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
                          } else if (DataSourceGroup.es.includes(sinkType)) {
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
                              value: column.sinkColumn,
                              parent: {
                                value: table.sinkTable ? table.sinkTable : table.targetTable
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
                              value: column.sinkColumn,
                              parent: {
                                value: table.sinkTable ? table.sinkTable : table.targetTable,
                                parent: {
                                  value: db.sinkDb
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
                    if (column.needAutoCreated && column.selected) {
                      if (!(!table.needAutoCreated && DataSourceGroup.mq.indexOf(sinkType) > -1)) {
                        sourceColumns.targetAutoCreate = true;
                        structMigration = true;
                      }
                    }

                    console.log('push columns 2');
                    sourceSchemaTable.columns.push(sourceColumns);
                    targetSchemaTable.columns.push(targetColumns);
                    if (DataSourceGroup.es.indexOf(sinkType) > -1 && column._checked) {
                      indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].fieldMetaList.push({
                        fieldName: column.sinkColumn,
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
              if (DataSourceGroup.pg.indexOf(sourceType) > -1) {
                sourceDbSchema.schemas[0].tables.push(sourceSchemaTable);
              } else if (DataSourceGroup.oracle.indexOf(sourceType) > -1) {
                sourceDbSchema.tableSpaces[0].tables.push(sourceSchemaTable);
              } else {
                sourceDbSchema.tables.push(sourceSchemaTable);
              }
              if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
                if (DataSourceGroup.rabbitMq.indexOf(sourceType) > -1) {
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
              if (DataSourceGroup.es.indexOf(sourceType) > -1) {
                // console.log('table', table, indexMetaMap);
                const fileds = indexMetaMap[table.sourceTable].fieldMetaList;
                const idFields = [];
                const pkFields = [];

                fileds.forEach((field) => {
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
                  fields: fileds,
                  numberOfShards: table.shards,
                  numberOfReplicas: table.replication,
                  globalTimeZone: this.taskInfo.globalTimeZone,
                  idFieldNames: idFields.length ? idFields : pkFields,
                  targetAutoCreate: table.needAutoCreated
                });
              }
              if (DataSourceGroup.mq.indexOf(sinkType) > -1) {
                if (DataSourceGroup.rabbitMq.indexOf(sinkType) > -1) {
                  topics.push({
                    queue: table.sinkTable ? table.sinkTable : table.targetTable,
                    queuePattern: ''
                  });
                } else {
                  console.log(6, 'targetTopics');
                  topics.push({
                    topic: table.sinkTable ? table.sinkTable : table.targetTable,
                    topicPattern: '',
                    partitions: table.partitions,
                    pulsarTopicPersistent: table.needAutoCreated ? true : table.pulsarTopicPersistent,
                    partitionKeys: table.partitionKeys
                  });
                }
              } else if (DataSourceGroup.es.indexOf(sinkType) > -1) {
                // console.log('table', table, indexMetaMap);
                const fileds = indexMetaMap[table.sinkTable || table.targetTable].fieldMetaList;
                const idFields = [];
                const pkFields = [];

                fileds.forEach((field) => {
                  if (field.isPk) {
                    pkFields.push(field.fieldName);
                  }
                  if (field.isId) {
                    idFields.push(field.fieldName);
                  }
                });
                // console.log(idFields);
                esIndex.push({
                  indexName: table.sinkTable ? table.sinkTable : table.targetTable,
                  fields: fileds,
                  numberOfShards: table.shards,
                  numberOfReplicas: table.replication,
                  globalTimeZone: this.taskInfo.globalTimeZone,
                  idFieldNames: idFields.length ? idFields : pkFields
                });
              } else if (DataSourceGroup.pg.indexOf(sinkType) > -1) {
                targetDbSchema.schemas[0].tables.push(targetSchemaTable);
              } else if (DataSourceGroup.redis.indexOf(sinkType) > -1) {
                redisKeys.push({
                  prefix: table.sinkTable,
                  suffixFields: table.suffixFields || []
                });
              } else if (hasPartitionExpr(sinkType)) {
                if (table.partitionExpr) {
                  targetDbSchema.tables.push(targetSchemaTable);
                }
              } else if (isGrepTime(sinkType) && table.timeIndex) {
                targetDbSchema.tables.push(targetSchemaTable);
              } else {
                targetDbSchema.tables.push(targetSchemaTable);
              }
            }
          });
          sourceSchema.push(sourceDbSchema);
          targetSchema.push(targetDbSchema);
        });
        if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
          if (DataSourceGroup.hasSchema.includes(sinkType)) {
            mappingDef.push(anySchemaMapping);
            mappingDef.push(topicTableMapping);
          } else if (!DataSourceGroup.noDb.includes(sinkType)) {
            mappingDef.push(anyDbMapping);
            if (isTunnel(sourceType)) {
              mappingDef.push(tableMapping);
            } else {
              mappingDef.push(topicTableMapping);
            }
          } else if (isKafka(sinkType) || isRocketMQ(sinkType) || isPulsar(sinkType)) {
            mappingDef.push(topicTopicMapping);
          } else {
            mappingDef.push(topicIndexMapping);
          }
        } else if (DataSourceGroup.hasSchema.indexOf(sourceType) > -1) {
          if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
            mappingDef.push(dbMapping);
            mappingDef.push(schemaMapping);
            mappingDef.push(tableMapping);
            mappingDef.push(columnMapping);
          } else if (DataSourceGroup.oracle.includes(sourceType) && DataSourceGroup.kudu.includes(sinkType)) {
            mappingDef.push(noTargetDbTableMapping);
            mappingDef.push(columnMapping);
          } else if (DataSourceGroup.hasSchema.includes(sourceType) && (DataSourceGroup.mq.includes(sinkType) || isRagApi(sinkType))) {
            mappingDef.push(schemaTopicMapping);
            mappingDef.push(columnMapping);
          } else if (isES(sinkType)) {
            mappingDef.push(schemaIndexMapping);
            mappingDef.push(columnMapping);
          } else {
            mappingDef.push(schemaMapping);
            mappingDef.push(tableMapping);
            mappingDef.push(columnMapping);
          }
        } else if (DataSourceGroup.hasSchema.indexOf(sinkType) > -1) {
          mappingDef.push(schemaMapping);
          mappingDef.push(tableMapping);
          mappingDef.push(columnMapping);
        } else if (DataSourceGroup.mq.indexOf(sinkType) > -1 || isRagApi(sinkType)) {
          mappingDef.push(tableTopicMapping);
          mappingDef.push(columnMapping);
        } else if (DataSourceGroup.redis.includes(sinkType)) {
          mappingDef.push(tableKeyPrefixMapping);
          mappingDef.push(columnMapping);
        } else if (DataSourceGroup.es.indexOf(sinkType) > -1) {
          if (DataSourceGroup.es.indexOf(sourceType) > -1) {
            mappingDef.push(indexIndexMapping);
            mappingDef.push(columnMapping);
          } else {
            mappingDef.push(tableIndexMapping);
            mappingDef.push(columnMapping);
          }
        } else if (isDynamoDB(sourceType)) {
          mappingDef.push(anyDbMapping);
          mappingDef.push(tableMapping);
          mappingDef.push(columnMapping);
        } else if (isDynamoDB(sinkType)) {
          tableTopicMapping.method = 'TABLE_TABLE';
          mappingDef.push(tableTopicMapping);
          mappingDef.push(columnMapping);
        } else {
          mappingDef.push(dbMapping);
          mappingDef.push(tableMapping);
          mappingDef.push(columnMapping);
        }
        if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
          finalSourceSchema = JSON.stringify(topics);
        } else if (DataSourceGroup.es.indexOf(sourceType) > -1) {
          finalSourceSchema = JSON.stringify(sourceEsIndex);
        } else {
          finalSourceSchema = sourceSchema.length > 0 ? JSON.stringify(sourceSchema) : null;
          if (isDynamoDB(sourceType)) {
            finalSourceSchema =
              sourceSchema && sourceSchema.length && sourceSchema[0] && sourceSchema[0].tables ? JSON.stringify(sourceSchema[0].tables) : null;
          }
        }
        if (isPulsar(sourceType) && isPulsar(sinkType)) {
          finalTargetSchema = JSON.stringify(targetTopics);
        } else if (isKafka(sourceType) && isKafka(sinkType)) {
          finalTargetSchema = JSON.stringify(targetTopics);
        } else if (DataSourceGroup.mq.indexOf(sinkType) > -1) {
          finalTargetSchema = JSON.stringify(topics);
        } else if (DataSourceGroup.redis.includes(sinkType)) {
          finalTargetSchema = JSON.stringify(redisKeys);
        } else if (DataSourceGroup.es.indexOf(sinkType) > -1) {
          finalTargetSchema = JSON.stringify(esIndex);
        } else if (DataSourceGroup.mq.indexOf(sourceType) > -1) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else if (DataSourceGroup.drds.indexOf(sinkType) > -1 || isTdsqlMySQL(sinkType)) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else if (hasPartitionExpr(sinkType)) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else if (isGrepTime(sinkType)) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else if (DataSourceGroup.hive.indexOf(sinkType) > -1) {
          finalTargetSchema = JSON.stringify(targetSchema);
        } else {
          finalTargetSchema = null;
          // finalTargetSchema = JSON.stringify(targetSchema);
        }

        if (this.taskInfo && this.taskInfo.customVirtualColumns.length > 0) {
          structMigration = true;
        }

        if (DataSourceGroup.redis.includes(sinkType)) {
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
              parseSchema = JSON.parse(finalSourceSchema).filter((schema) => !newData.empty.selectedTables[schema.indexName]);
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
                if (isHasSchema(this.taskInfo.sourceType)) {
                  if (isOracle(this.taskInfo.sourceType)) {
                    schema.tableSpaces.forEach((tableSpace) => {
                      tableSpace.tables = tableSpace.tables.filter((table) => {
                        console.log(table, tableSpace, newData[schema.db].selectedTables[table.table]);
                        return (
                          !newData[schema.db].selectedTables[table.table] ||
                          (newData[schema.db].selectedTables[table.table] &&
                            tableSpace.tableSpace !== newData[schema.db].selectedTables[table.table].sourceSchema)
                        );
                      });
                    });
                  } else {
                    schema.schemas.forEach((s) => {
                      s.tables = s.tables.filter((table) => {
                        console.log(table, s, newData[schema.db].selectedTables[table.table]);
                        return (
                          !newData[schema.db].selectedTables[table.table] ||
                          (newData[schema.db].selectedTables[table.table] && s.schema !== newData[schema.db].selectedTables[table.table].sourceSchema)
                        );
                      });
                    });
                  }
                } else {
                  if (schema.collections) {
                    schema.collections = schema.collections.filter((table) => !newData[schema.db].selectedTables[table.collection]);
                  } else {
                    schema.tables = schema.tables.filter((table) => !newData[schema.db].selectedTables[table.table]);
                  }
                }

                return true;
              }
              return true;
            });
          }

          finalSourceSchema = JSON.stringify(parseSchema);

          if (isKafka(sourceType) && isKafka(sinkType)) {
            // eslint-disable-next-line array-callback-return
            mappingDef.forEach((mapping) => {
              Object.values(newData).forEach((db) => {
                Object.keys(db.selectedTables).forEach((table) => {
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

        if (isRedis(sourceType) && isRedis(sinkType)) {
          finalSourceSchema = JSON.stringify([]);
          finalTargetSchema = JSON.stringify([]);
          mappingDef = [];
        }

        return {
          structMigration,
          finalSourceSchema,
          finalTargetSchema,
          mappingDef
        };
      } catch (e) {
        console.log('err', e);
      }
    },
    setColumns(column, db, table, dbName, columnMapping, structMigration, sourceSchemaTable, targetSchemaTable, indexMetaMap, isVirtual) {
      // console.log(
      //   'setColumns',
      //   column,
      //   db,
      //   table,
      //   dbName,
      //   columnMapping,
      //   structMigration,
      //   sourceSchemaTable,
      //   targetSchemaTable,
      //   indexMetaMap,
      //   isVirtual
      // );
      const sourceColumns = {};
      const targetColumns = {};

      sourceColumns.column = isDynamoDB(this.taskInfo.sourceType) ? column.sinkColumn : column.sourceColumn;
      sourceColumns.targetAutoCreate = false;
      sourceColumns.inBlackList = true;
      targetColumns.column = column.sinkColumn;
      targetColumns.targetAutoCreate = false;
      targetColumns.inBlackList = true;

      if (!column.selected) {
        targetColumns.inBlackList = true;
        sourceColumns.inBlackList = true;
      } else {
        targetColumns.inBlackList = false;
        sourceColumns.inBlackList = false;
        const isMapiing =
          column.sourceColumn !== column.sinkColumn
            ? column.sinkColumn
            : column.targetColumn || this.getTargetValue(column.sourceColumn, columnMapping.commonGenRule) !== column.sinkColumn
              ? column.sinkColumn
              : column.targetColumn;
        if (!isNoColumn(this.taskInfo.sourceType) && isMapiing) {
          if (DataSourceGroup.hasSchema.indexOf(this.taskInfo.sinkType) > -1) {
            if (DataSourceGroup.oracle.indexOf(this.taskInfo.sinkType) > -1) {
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
              if (DataSourceGroup.hasSchema.indexOf(this.taskInfo.sourceType) > -1) {
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
                value: column.sinkColumn,
                parent: {
                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                  parent: {
                    value: db.sinkDb
                  }
                }
              };

              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              // }
            } else {
              if (DataSourceGroup.hasSchema.indexOf(this.taskInfo.sourceType) > -1) {
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
                  value: column.sinkColumn,
                  parent: {
                    value: table.sinkTable ? table.sinkTable : table.targetTable,
                    parent: {
                      value: db.targetSchema,
                      parent: {
                        value: db.sinkDb
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
                  value: column.sinkColumn,
                  parent: {
                    value: table.sinkTable ? table.sinkTable : table.targetTable,
                    parent: {
                      value: db.targetSchema,
                      parent: {
                        value: db.sinkDb
                      }
                    }
                  }
                };

                columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
              }
            }
          } else {
            if (DataSourceGroup.hasSchema.indexOf(this.taskInfo.sourceType) > -1) {
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
                value: column.sinkColumn,
                parent: {
                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                  parent: {
                    value: db.sinkDb
                  }
                }
              };

              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            } else if (DataSourceGroup.es.includes(this.taskInfo.sinkType)) {
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
                value: column.sinkColumn,
                parent: {
                  value: table.sinkTable ? table.sinkTable : table.targetTable
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
                value: column.sinkColumn,
                parent: {
                  value: table.sinkTable ? table.sinkTable : table.targetTable,
                  parent: {
                    value: db.sinkDb
                  }
                }
              };

              columnMapping.serializeMapping[JSON.stringify(key)] = JSON.stringify(value);
            }
          }
        }
      }
      if (column.needAutoCreated && column.selected) {
        if (!(!table.needAutoCreated && DataSourceGroup.mq.indexOf(this.taskInfo.sinkType) > -1)) {
          sourceColumns.targetAutoCreate = true;
          structMigration = true;
        }
      }
      if (!isVirtual) {
        sourceSchemaTable.columns.push(sourceColumns);
        targetSchemaTable.columns.push(targetColumns);
        if (DataSourceGroup.es.indexOf(this.taskInfo.sinkType) > -1) {
          if (isES(this.taskInfo.sourceType)) {
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
              indexMetaMap[table.sinkTable ? table.sinkTable : table.targetTable].fieldMetaList.push({
                fieldName: column.sinkColumn,
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
    parseCron(date) {
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
    getTargetValue(target, rule) {
      if (target) {
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
  }
};
