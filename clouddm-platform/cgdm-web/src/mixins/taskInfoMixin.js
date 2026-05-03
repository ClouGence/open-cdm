import {
  DDL_TYPE,
  EMPTY_DB,
  EMPTY_TASK_INFO,
  hasSchema,
  isDDLFilter,
  isES,
  isGrepTime,
  isHasSchema,
  isHive,
  isMongoDB,
  isMQ,
  isNoDb,
  isOracle,
  isPulsar,
  isRagApi,
  isRedis,
  isTunnel,
  JOB_MODE,
  JOB_STEP,
  JOB_TYPE,
  MAPPING_METHOD,
  PROCESSOR_CONFIG_TYPE,
  SELECT_MODE
} from '@/utils';
import deepClone from 'lodash.clonedeep';
import _ from 'lodash';
import { nanoid } from 'nanoid';
import { UPDATE_TASK_INFO_DB_MAP_HISTORY, UPDATE_TASK_INFO_HISTORY } from '@/store/mutationTypes';
// 在 process中使用newTaskInfo
const taskInfoMixin = {
  name: 'TaskInfoMixin',
  data() {
    return {
      DDL_TYPE,
      JOB_TYPE
    };
  },
  methods: {
    needParseSchemaData() {
      console.log('needParseSchemaData', this.isEditMode(), this.taskInfo.mode);
      return this.isEditMode() || this.taskInfo.mode === JOB_MODE.SIMILAR;
    },
    isEditMode() {
      return this.taskInfo.mode === JOB_MODE.EDIT || this.taskInfo.mode === JOB_MODE.FULL_EDIT;
    },
    parseJobData(data) {},
    newGetSchemaBasicData() {
      try {
        let tableMapping = {};
        let columnMapping = {};
        let schema = [];
        const {
          sourceType,
          targetType,
          jobData: { schemaData }
        } = this.taskInfo;
        const mappingJson = JSON.parse(schemaData.mappingConfig);
        const sourceSchemaList = JSON.parse(schemaData.sourceSchema);
        const targetSchemaList = schemaData.targetSchema ? JSON.parse(schemaData.targetSchema) : [];

        let method = MAPPING_METHOD.TABLE_TABLE;

        if (isES(targetType)) {
          if (isMQ(sourceType)) {
            method = MAPPING_METHOD.TOPIC_INDEX;
          } else if (isES(sourceType)) {
            method = MAPPING_METHOD.TABLE_TABLE;
          } else {
            method = MAPPING_METHOD.TABLE_INDEX;
          }
        } else if (isMQ(targetType) && !isMQ(sourceType)) {
          method = MAPPING_METHOD.TABLE_TOPIC;
        } else if (isMQ(sourceType) && !isMQ(targetType)) {
          method = MAPPING_METHOD.TOPIC_TABLE;
        } else if (isMQ(sourceType) && isMQ(targetType)) {
          method = MAPPING_METHOD.TOPIC_TOPIC;
        } else if (isRedis(targetType)) {
          method = MAPPING_METHOD.TABLE_KEYPREFIX;
        }

        mappingJson.forEach((mapping) => {
          try {
            if (mapping.method === method) {
              tableMapping = mapping.serializeMapping;
            }
            if (mapping.method === MAPPING_METHOD.COLUMN_COLUMN) {
              columnMapping = mapping.serializeMapping;
            }
          } catch (e) {
            console.log('e', e);
          }
        });

        if (isNoDb(sourceType)) {
          schema = targetSchemaList;
          if (isES(sourceType)) {
            schema = sourceSchemaList;
          }
        } else {
          schema = sourceSchemaList;
        }

        if (isMQ(sourceType) && isMQ(targetType)) {
          schema = [];
        }

        return {
          tableMapping,
          columnMapping,
          schema,
          sourceSchemaList,
          targetSchemaList
        };
      } catch (e) {
        console.log(e);
        return {
          tableMapping: {},
          columnMapping: {},
          schema: [],
          sourceSchemaList: [],
          targetSchemaList: []
        };
      }
    },
    newParseSearchSchemaData(data, theTable) {
      try {
        const { tableMapping, schema } = this.newGetSchemaBasicData(data);

        for (let i = 0; i < schema.length; i++) {
          const { tableName, mapping } = this.newGetTargetTableFromSchemaData(theTable, tableMapping, schema[i]);

          if (mapping) {
            return {
              targetTableName: tableName,
              mapping
            };
          }
        }

        return {
          targetTableName: '',
          mapping: false
        };
      } catch (e) {
        console.log(e);
        return {
          targetTableName: '',
          mapping: false
        };
      }
    },
    newGetTargetTableFromSchemaData(table, mapping, db) {
      console.log('newGetTargetTableFromSchemaData', table, mapping);
      let targetTable = '';
      let hasMapping = false;
      Object.keys(mapping).forEach((theMapping) => {
        let theKey1 = {
          parent: {
            value: db.db
          },
          value: table.sourceTable
        };
        let theKey2 = {
          value: table.sourceTable,
          parent: {
            value: db.db
          }
        };
        const theKey3 = {
          value: table.sourceTable
        };
        if (db.schemas || db.tableSpaces) {
          if (db.tableSpaces) {
            theKey1 = {
              parent: {
                value: table.sourceSchema,
                parent: {
                  value: db.db
                }
              },
              value: table.sourceTable
            };
            theKey2 = {
              parent: {
                parent: {
                  value: db.db
                },
                value: table.sourceSchema
              },
              value: table.sourceTable
            };
          } else {
            theKey1 = {
              parent: {
                value: table.sourceSchema,
                parent: {
                  value: db.db
                }
              },
              value: table.sourceTable
            };
            theKey2 = {
              parent: {
                parent: {
                  value: db.db
                },
                value: table.sourceSchema
              },
              value: table.sourceTable
            };
          }
        }
        if (isNoDb(this.taskInfo.sourceType)) {
          if (theMapping === JSON.stringify(theKey3)) {
            hasMapping = true;
            const theValue = JSON.parse(mapping[theMapping]);
            targetTable = theValue.value;
          }
        } else {
          if (theMapping === JSON.stringify(theKey1)) {
            hasMapping = true;
            const theValue = JSON.parse(mapping[theMapping]);
            targetTable = theValue.value;
          } else if (theMapping === JSON.stringify(theKey2)) {
            hasMapping = true;
            const theValue = JSON.parse(mapping[theMapping]);
            targetTable = theValue.value;
          }
        }
      });

      return {
        tableName: targetTable,
        mapping: hasMapping
      };
    },
    newGetTargetColumnFromSchemaData(column, mapping, db) {
      console.log('newGetTargetColumnFromSchemaData');
      let targetColumn = column.sourceColumn;
      Object.keys(mapping).forEach((theMapping) => {
        try {
          let theKey = {
            parent: {
              value: column.sourceTable,
              parent: {
                value: column.sourceDb
              }
            },
            value: column.sourceColumn
          };
          if (db.schemas || db.tableSpaces) {
            theKey = {
              parent: {
                value: column.sourceTable,
                parent: {
                  value: column.sourceSchema,
                  parent: {
                    value: column.sourceDb
                  }
                }
              },
              value: column.sourceColumn
            };
          }
          if (theMapping === JSON.stringify(theKey)) {
            const theValue = JSON.parse(mapping[theMapping]);
            targetColumn = theValue.value;
          }
        } catch (e) {
          console.log('e', e);
        }
      });

      console.log('mapping column', column.sourceColumn, targetColumn);
      return targetColumn;
    },
    getParseSchemaDataTables(parseDb, sourceSchemaList) {
      let tables = [];
      let schemaName = '';
      if (parseDb.schemas) {
        tables = parseDb.schemas[0].tables;
        schemaName = parseDb.schemas[0].schema;
      } else if (parseDb.tableSpaces) {
        tables = parseDb.tableSpaces[0].tables;
        schemaName = parseDb.tableSpaces[0].tableSpace;
      } else if (parseDb.collections) {
        tables = parseDb.collections;
      } else {
        if (parseDb.tables.length < 1) {
          tables = sourceSchemaList;
        } else {
          tables = parseDb.tables;
        }
      }
      //
      // console.log('getParseSchemaDataTables');
      // console.log('tables', tables);
      // console.log('schemaName', schemaName);
      return { tables, schemaName };
    },
    newParseTableFilterSchemaData() {
      console.log('newParseTableFilterSchemaData');
      try {
        const { sourceType, targetType } = this.taskInfo;
        const { tableMapping, schema, sourceSchemaList, targetSchemaList } = this.newGetSchemaBasicData();

        this.taskInfo.dbMap.forEach((db) => {
          db.allTableList.forEach((table) => {
            let sameTable = null;
            let sameDb = null;

            schema.forEach((parseDb) => {
              if (parseDb.db === db.sourceDb) {
                sameDb = parseDb;
                const { tables } = this.getParseSchemaDataTables(parseDb, sourceSchemaList);

                tables.forEach((parseTable) => {
                  if (isNoDb(sourceType)) {
                    if (
                      parseTable.table === table.targetTable ||
                      parseTable.topic === table.targetTable ||
                      parseTable.indexName === table.targetTable
                    ) {
                      sameTable = parseTable;
                    }
                  } else {
                    if (parseTable.collection) {
                      parseTable.table = parseTable.collection;
                    }

                    if (parseTable.table === table.sourceTable) {
                      console.log(parseTable.table, table.sourceTable, parseTable.table === table.sourceTable);
                      sameTable = parseTable;
                    }
                  }
                });
              }
            });

            if (sameTable) {
              table._checked = true;
              const { tableName } = this.newGetTargetTableFromSchemaData(table, tableMapping, sameDb);
              console.log('sameItem', sameTable.table, 'tableName', tableName);
              if (tableName) {
                table.targetTable = tableName;
              }
              table.action = sameTable.actions;

              if (!sameDb.actions && !JSON.parse(this.taskInfo.functionConfig.filterDDL) && isDDLFilter(sourceType)) {
                table.action.push(DDL_TYPE.CREATE, DDL_TYPE.ALTER, DDL_TYPE.RENAME, DDL_TYPE.TRUNCATE, DDL_TYPE.DROP);
              }

              if (this.isEditMode()) {
                table.hasInJob = true;

                if (sameTable.actions) {
                  table.originalAction = deepClone(sameTable.actions);
                  table.preAction = deepClone(sameTable.actions);
                }

                table.originalTargetTable = sameTable.table;

                // TODO
                // if (this.taskInfo.reduceData?.[db.db]?.selectedTables?.[theTable.sinkTable]) {
                //   delete this.taskInfo.reduceData[db.db].selectedTables[theTable.sinkTable];
                //   if (this.taskInfo.reduceData[db.db].selectedTables.length < 1) {
                //     delete this.taskInfo.reduceData[db.db];
                //   }
                // }
              }

              if (this.isStructMigrationJob(this.taskInfo.type)) {
                table._checked = false;
                table.action = [];
                table.originalAction = [];
              }
            } else {
              table.hasInJob = false;
              if (table.sourceTable === 'alert_receiver') {
                console.log('set table false');
              }
              table._checked = false;
              table.action = [];
              table.originalAction = [];
            }

            // TODO 有优化空间，以后将所有targetSchema逻辑放在一起
            if (isGrepTime(targetType)) {
              targetSchemaList.forEach((parseDb) => {
                if (parseDb.db === db.targetDb) {
                  db.tables.forEach((parseTable) => {
                    if (
                      parseTable.table === table.targetTable ||
                      parseTable.topic === table.targetTable ||
                      parseTable.indexName === table.targetTable
                    ) {
                      if (table.timeIndexes) {
                        table.timeIndex = table.timeIndexes[0];
                      }
                    }
                  });
                }
              });
            }

            // TODO 是否可以和上面的合并
            // if (isMQ(sourceType) && (isMQ(targetType) || isES(targetType))) {
            //   let hasSame = false;
            //   Object.keys(tableMapping).forEach((key) => {
            //     const topic = JSON.parse(key).value;
            //     let targetTable = '';
            //     targetTable = JSON.parse(tableMapping[key]).value;
            //
            //     if (topic === table.sourceTable) {
            //       console.log(topic, theTable.sourceTable, theTable._checked, true);
            //       theTable.sinkTable = sinkTable;
            //       theTable.selected = true;
            //       theTable._checked = true;
            //       theTable.preAction = [...theTable.action];
            //       theTable.originalAction = [...theTable.action];
            //
            //       if (this.taskInfo.processType === 'edit') {
            //         theTable.hasInJob = true;
            //         theTable.originalSinkTable = theTable.sinkTable;
            //       }
            //       hasSame = true;
            //     }
            //   });
            //
            //   if (!hasSame) {
            //     console.log(theTable.sourceTable, theTable._checked, false);
            //     theTable.hasInJob = false;
            //     theTable.selected = false;
            //     theTable._checked = false;
            //     theTable.action = [];
            //     theTable.originalAction = [];
            //   } else {
            //     if (this.taskInfo.processType === 'edit') {
            //       if (this.taskInfo.newData?.[theDb.sourceDb]?.selectedTables?.[theTable.sourceTable]) {
            //         delete this.taskInfo.newData[theDb.sourceDb];
            //       }
            //     }
            //   }
            // }
          });
        });
      } catch (e) {
        console.log('err', e);
      }
    },
    newParseCleanDataSchemaData() {
      console.log('newParseCleanDataSchemaData');
      const { sourceType, targetType } = this.taskInfo;
      const { sourceSchemaList, targetSchemaList, columnMapping } = this.newGetSchemaBasicData();

      if (isHive(sourceType) && targetSchemaList.length) {
        const tableInfo = targetSchemaList?.[0]?.tables?.[0];
        if (tableInfo) {
          this.partitionData = {
            partition: [
              {
                key: tableInfo.partitionKeys[0] ? tableInfo.partitionKeys[0].keyName : '',
                func: tableInfo.partitionKeys[0] ? tableInfo.partitionKeys[0].partitionFunction : ''
              }
            ],
            fileFormat: tableInfo.fileFormat,
            compressType: tableInfo.compressType
          };
        }
      }

      const { virtualColumnObj, transformColumnObj, wideTableObj } = this.newParseProcessorData();
      console.log(virtualColumnObj, transformColumnObj, wideTableObj);

      this.allTableList.forEach((table) => {
        let sameTable = null;
        let sameDb = null;
        sourceSchemaList.forEach((parseDb) => {
          const { tables, schemaName } = this.getParseSchemaDataTables(parseDb, sourceSchemaList);

          tables.forEach((parseTable) => {
            if (parseTable.collection) {
              parseTable.table = parseTable.collection;
            }

            if (parseDb.db === table.sourceDb && schemaName === table.sourceSchema && parseTable.table === table.sourceTable) {
              sameTable = parseTable;
              sameDb = parseDb;
            }
          });
        });

        if (virtualColumnObj[table.path]) {
          table.virtualColumnList = virtualColumnObj[table.path];
        }

        if (transformColumnObj[table.path]) {
          table.dataTransformList = transformColumnObj[table.path];
        }

        if (wideTableObj[table.path]) {
          table.joinedTables = wideTableObj[table.path];
        }

        if (sameTable) {
          if (sameTable.columns) {
            sameTable.columns.forEach((parseColumn) => {
              table.columnList.forEach((column) => {
                if (parseColumn.column === column.sourceColumn) {
                  if (parseColumn.inBlackList) {
                    column._checked = !parseColumn.inBlackList;
                    column.hasInJob = !parseColumn.inBlackList;
                  } else {
                    column._checked = !parseColumn.inBlackList;
                    column.hasInJob = !parseColumn.inBlackList;
                    column.targetColumn = this.newGetTargetColumnFromSchemaData(column, columnMapping, sameDb);
                  }

                  column.isPk = table.cols.includes(column.sourceColumn);
                  column.isTimeIndex = table.timeIndexes.includes(column.sourceColumn);
                }
              });
            });
          }

          if (isHive(targetType)) {
            table.partitionData = deepClone(this.partitionData);
          }

          if (sameTable.dataFilter) {
            table.whereCondition = sameTable.dataFilter.expression;
            table.dataFilterType = sameTable.dataFilter.type;
          }

          if (sameTable.specifiedPks) {
            table.cols = deepClone(sameTable.specifiedPks);
          }

          targetSchemaList.forEach((targetParseDb) => {
            if (isRedis(targetType)) {
              if (targetParseDb.prefix === sameTable.table) {
                table.suffixFields = targetParseDb.suffixFields;
              }
            }

            if (isGrepTime(targetType)) {
              if (targetParseDb.db === sameDb.db) {
                targetParseDb.tables.forEach((targetParseTable) => {
                  if (targetParseTable.table === sameTable.table) {
                    if (targetParseTable.timeIndexes) {
                      table.timeIndexes = targetParseTable.timeIndexes;
                    }
                  }
                });
              }
            }

            if (isMQ(targetType)) {
              if (targetParseDb.topic === table.targetTable) {
                table.partitions = targetParseDb.partitions;
                table.partitionKeys = targetParseDb.partitionKeys;
              }
            }
          });
        }
      });
    },
    newParseProcessorData() {
      const virtualColumnObj = {};
      const transformColumnObj = {};
      const wideTableObj = {};
      if (this.taskInfo.jobData.processorConfigListJson) {
        const processorList = JSON.parse(this.taskInfo.jobData.processorConfigListJson);
        processorList.forEach((process) => {
          if (process.allProcessorContextJson) {
            const fieldProcessList = JSON.parse(process.allProcessorContextJson);

            switch (process.processorConfigType) {
              case PROCESSOR_CONFIG_TYPE.FIELD_MAKER_PROCESSOR:
                fieldProcessList.forEach((fieldProcess) => {
                  virtualColumnObj[fieldProcess.fieldPathName] = [];
                  if (fieldProcess.customFieldList) {
                    fieldProcess.customFieldList.forEach((item) => {
                      virtualColumnObj[fieldProcess.fieldPathName].push(item);
                    });
                  }
                });
                break;
              case PROCESSOR_CONFIG_TYPE.FIELD_TRANSFORM_PROCESSOR:
                fieldProcessList.forEach((fieldProcess) => {
                  const { db, schema, table, fieldsScripts } = fieldProcess;
                  let fieldPathName = '';
                  const columns = [];
                  if (isHasSchema(this.taskInfo.sourceType)) {
                    fieldPathName = `/${db}/${schema}/${table}`;
                  } else {
                    fieldPathName = `/${db}/${table}`;
                  }

                  const values = Object.keys(fieldsScripts);
                  values.forEach((value) => {
                    columns.push({
                      columnName: value,
                      script: fieldsScripts[value]
                    });
                  });
                  transformColumnObj[fieldPathName] = columns;
                });
                break;
              case PROCESSOR_CONFIG_TYPE.WIDE_TABLE_PROCESSOR:
                fieldProcessList.forEach((fieldProcess) => {
                  const { db, schema, table } = fieldProcess;
                  let fieldPathName = '';
                  if (isHasSchema(this.taskInfo.sourceType)) {
                    fieldPathName = `/${db}/${schema}/${table}`;
                  } else {
                    fieldPathName = `/${db}/${table}`;
                  }

                  wideTableObj[fieldPathName] = [fieldProcess];
                });
                this.taskInfo.cleanDataConfig.wideTables = fieldProcessList;
                break;
              default:
                break;
            }
          }

          // TODO
          // if (process.processorConfigType === 'CUSTOM_PKG_PROCESSOR') {
          //   this.taskInfo.customPkgProcess = process;
          // }

          // TODO
          // if (process.processorConfigType === 'LLM_EMBEDDING_PROCESSOR') {
          //   const processContent = JSON.parse(process.allProcessorContextJson);
          //   console.log(processContent.llmExtraFields);
          //   this.taskInfo.llmDefaultColInfo = processContent.llmExtraFields;
          //   this.taskInfo.embeddingData = processContent.embeddingFields;
          //   if (processContent.embeddingFields) {
          //     processContent.embeddingFields.forEach((embeddingField) => {
          //       const { db, schema, table, embeddingCols } = embeddingField;
          //       let fieldPathName = '';
          //       const columns = [];
          //       if (isHasSchema(this.taskInfo.sourceType)) {
          //         fieldPathName = `/${db}/${schema}/${table}`;
          //       } else {
          //         fieldPathName = `/${db}/${table}`;
          //       }
          //
          //       this.taskInfo.embeddingColumnData[fieldPathName] = {
          //         db,
          //         schema,
          //         table,
          //         embeddingCols
          //       };
          //     });
          //   }
          //   this.taskInfo.originalEmbeddingData = deepClone(processContent.embeddingFields);
          // }

          // TODO
          // if (process.processorConfigType === 'WIDE_TABLE_PROCESSOR') {
          //   const processContent = JSON.parse(process.allProcessorContextJson);
          //   this.taskInfo.wideTables = processContent;
          // }
        });
      }

      return { virtualColumnObj, transformColumnObj, wideTableObj };
    },
    updateNextStepDisabled(disabled = true) {
      this.taskInfo.nextStepDisabled = disabled;
    },
    updateNextStepLoading(loading = true) {
      this.taskInfo.nextStepLoading = loading;
    },
    updatePreStepDisabled(disabled = true) {
      this.taskInfo.preStepDisabled = disabled;
    },
    updatePreStepLoading(loading = true) {
      this.taskInfo.preStepLoading = loading;
    },
    updateNextStepDisabledAndLoading(disabled = true) {
      this.taskInfo.nextStepDisabled = disabled;
      this.taskInfo.nextStepLoading = disabled;
    },
    updatePreStepDisabledAndLoading(disabled = true) {
      this.taskInfo.preStepDisabled = disabled;
      this.taskInfo.preStepLoading = disabled;
    },
    updateNextAndPreStepDisabled(disabled = true) {
      this.taskInfo.nextStepDisabled = disabled;
      this.taskInfo.preStepDisabled = disabled;
    },
    updateNextAndPreStepLoading(loading = true) {
      this.taskInfo.nextStepLoading = loading;
      this.taskInfo.preStepLoading = loading;
    },
    getDdlCheckboxGroup() {
      const { functionConfig, sourceType, targetType } = this.taskInfo;
      const { INSERT, UPDATE, DELETE, CREATE, ALTER, RENAME, TRUNCATE, DROP } = DDL_TYPE;
      const ddlGroup = [INSERT, UPDATE, DELETE];
      if (!JSON.parse(functionConfig.filterDDL) && this.isDDLFilter(sourceType)) {
        ddlGroup.push(CREATE, ALTER, RENAME, TRUNCATE);

        if (this.isMySQL(sourceType) && this.isMySQL(targetType)) {
          ddlGroup.push(DROP);
        }
      }

      return ddlGroup;
    },
    isFullEditMode(mode) {
      return mode === JOB_MODE.FULL_EDIT;
    },
    getTargetTableName(rule, db, table) {
      const { sourceInstanceId, theDb } = db;
      const schemaName = table.schemaName || table.sourceSchema || '';
      let targetTableName = table.tableName || table.sourceTable || '';

      switch (rule.rule) {
        case 'TO_LOWER_CASE':
          targetTableName = targetTableName.toLowerCase();
          break;
        case 'TO_UPPER_CASE':
          targetTableName = targetTableName.toUpperCase();
          break;
        case 'SOURCE_INS_DB_BY_DOT':
          targetTableName = `${sourceInstanceId}.${theDb}`;
          break;
        case 'SOURCE_INS_DB_TABLE_BY_DOT':
          targetTableName = `${sourceInstanceId}.${theDb}.${targetTableName}`;
          break;
        case 'SOURCE_INS_DB_SCHEMA_TABLE_BY_DOT':
          targetTableName = `${sourceInstanceId}.${theDb}.${schemaName}.${targetTableName}`;
          break;
        case 'SOURCE_INS_DB_BY_PERCENT':
          targetTableName = `${sourceInstanceId}%${theDb}`;
          break;
        case 'SOURCE_INS_DB_TABLE_BY_PERCENT':
          targetTableName = `${sourceInstanceId}%${theDb}%${targetTableName}`;
          break;
        case 'SOURCE_INS_DB_TABLE_BY_DOUBLE_UNDERLINE':
          targetTableName = `${sourceInstanceId}__${theDb}__${targetTableName}`;
          break;
        case 'SOURCE_INS_DB_SCHEMA_TABLE_BY_PERCENT':
          targetTableName = `${sourceInstanceId}%${theDb}%${schemaName}%${targetTableName}`;
          break;
        case 'SOURCE_LAST_ITEM_BY_DOT':
          if (targetTableName) {
            const itemList = targetTableName.split('.');
            targetTableName = itemList[itemList.length - 1];
          }
          break;
        case 'SOURCE_LAST_ITEM_BY_PERCENT':
          if (targetTableName) {
            const itemList = targetTableName.split('%');

            targetTableName = itemList[itemList.length - 1];
          }
          break;
        case 'SOURCE_LIST_ITEM_BY_DOUBLE_UNDERLINE':
          if (targetTableName) {
            const itemList = targetTableName.split('__');

            targetTableName = itemList[itemList.length - 1];
          }
          break;
        case 'NUMBER_SUFFIX_REG_EXPRESSION':
          if (targetTableName.match(new RegExp(rule.expr))) {
            targetTableName = targetTableName.match(new RegExp(rule.expr))[1];
          }
          break;
        case 'DEFAULT_TOPIC':
          // TODO 遗漏
          break;
        case 'SOURCE_DB_TABLE_BY_COLON':
          targetTableName = `${theDb}:${targetTableName}`;
          break;
        case 'SOURCE_DB_SCHEMA_TABLE_BY_COLON':
          // TODO 遗漏
          targetTableName = `${theDb}:${schemaName}:${targetTableName}`;
          break;
        case 'TO_ALIAS':
          targetTableName = table.tableAlias;
          break;
        case 'TO_CAMEL_FORMAT':
          // TODO 遗漏
          break;
        case 'SOURCE_INS_DB_SCHEMA_TABLE_BY_UNDERLINE':
          targetTableName = `cc_${sourceInstanceId}_${theDb}_${isHasSchema(db.sourceType) ? `${schemaName}_` : ''}_${targetTableName}`.toLowerCase();
          break;
        case 'SOURCE_DB_TABLE_BY_UNDERLINE':
          targetTableName = `${theDb}_${targetTableName}`;
          break;
        case 'SOURCE_DB_TABLE_BY_UNDERLINE_LOWER_CASE':
          targetTableName = `${theDb}_${targetTableName}`.toLowerCase();
          break;
        case 'SOURCE_DB_TABLE_BY_UNDERLINE_UPPER_CASE':
          targetTableName = `${theDb}_${targetTableName}`.toUpperCase();
          break;
        case 'SOURCE_DB_SCHEMA_TABLE_BY_UNDERLINE':
          targetTableName = `${theDb}_${schemaName}_${targetTableName}`;
          break;
        case 'SOURCE_DB_SCHEMA_TABLE_BY_UNDERLINE_LOWER_CASE':
          targetTableName = `${theDb}_${schemaName}_${targetTableName}`.toLowerCase();
          break;
        case 'SOURCE_DB_SCHEMA_TABLE_BY_UNDERLINE_UPPER_CASE':
          targetTableName = `${theDb}_${schemaName}_${targetTableName}`.toUpperCase();
          break;
        case 'KUDU_PRESTO_FORMAT':
          targetTableName = `presto.${theDb}.${isHasSchema(db.sourceType) ? `${schemaName}.` : ''}${targetTableName}`;
          break;
        case 'KUDU_IMPALA_FORMAT':
          targetTableName = `impala::${db}.${isHasSchema(db.sourceType) ? `${schemaName}.` : ''}${targetTableName}`;
          break;
        case 'MIRROR':
        default:
          break;
      }

      return targetTableName || '';
    },
    isNotTargetCaseSensitive() {
      return this.taskInfo.targetCaseSensitive === 'false';
    },
    containsTable(table, list) {
      if (!list) return false;
      return list.some((item) => item.tableName === table);
    },
    containsDb(list, theDb) {
      return list.some((item) => item.dbName === theDb);
    },
    containsSchema(list, db, schemaName) {
      return list.some((item) => {
        if (item.dbName !== db) return false;

        return item.schemas.some((schema) => {
          if (!db || !schema) return false;

          return this.isNotTargetCaseSensitive() ? schema.toUpperCase() === schemaName.toUpperCase() : schema === schemaName;
        });
      });
    },
    getTargetTableList(theDb) {
      if (JSON.parse(this.taskInfo.functionConfig.reSchemaMigration)) {
        return [];
      }
      return this.taskInfo.tableFilterConfig.targetTableList[theDb];
    },
    updateNextDisabled(nextDisabled) {
      this.nextDisabled = nextDisabled;
    },
    updateLoading(loading) {
      this.loading = loading;
    },
    // 使用 newTaskInfo
    async handleNewNextStep() {
      console.log(this.taskInfo.step);
      switch (this.taskInfo.step) {
        case JOB_STEP.ORIGINAL:
          this.handleOriginalNextStep();
          break;
        case JOB_STEP.FUNCTION:
          this.handleFunctionNextStep();
          break;
        case JOB_STEP.TABLE_FILTER:
          await this.handleTableFilterNextStep();
          break;
        case JOB_STEP.CLEAN_DATA:
          await this.handleCleanDataNextStep();
          break;
        default:
          break;
      }
    },
    diffTaskInfoData(step, oldConfig, newConfig, oldDbMap, newDbMap) {},
    handleOriginalNextStep() {
      this.taskInfo.originalConfigHistory = true;
      this.oldTaskInfo = deepClone(this.taskInfo);
      this.transformTaskInfoOld2New();
    },
    transformTaskInfoOld2New() {
      const oldInfo = this.oldTaskInfo;
      const sinkToTargetMap = {
        sinkType: 'targetType',
        sinkInstance: 'targetHost',
        sinkInstanceType: 'targetInstanceType',
        sinkPulsarTenant: 'targetPulsarTenant',
        sinkPulsarNamespace: 'targetPulsarNamespace',
        sinkDbList: 'targetDbList',
        sinkPrivateHost: 'targetPrivateHost',
        sinkPublicHost: 'targetPublicHost'
      };

      // 构建新的数据库映射
      const newDbMap = oldInfo.dbMap.map((db) => {
        const dbItem = {
          _id: nanoid(),
          ...deepClone(EMPTY_DB),
          ...db,
          tableDefaultSelected: this.oldTaskInfo.schemaWhiteListLevel === 'DB',
          sourceType: oldInfo.sourceType,
          targetType: oldInfo.sinkType,
          sourceDataSourceId: oldInfo.sourceDataSourceId,
          targetDataSourceId: oldInfo.targetDataSourceId,
          sourceInstanceId: oldInfo.sourceInstanceId,
          targetInstanceId: oldInfo.targetInstanceId,
          sourceSchema: hasSchema(oldInfo.sourceType) ? db.sourceSchema : '',
          sourceDb: db.sourceDb,
          targetSchema: hasSchema(oldInfo.sinkType) ? db.targetSchema : '',
          targetDb: db.sinkDb,
          theSchema: isMQ(oldInfo.sourceType) ? db.targetSchema : db.sourceSchema,
          theDb: isMQ(oldInfo.sourceType) ? db.sinkDb : db.sourceDb
        };

        if (isMQ(dbItem.sourceType)) {
          dbItem.sourceDb = '';
          dbItem.sourceSchema = '';
        }

        if (isMQ(dbItem.targetType)) {
          dbItem.targetDb = '';
          dbItem.targetSchema = '';
        }

        return dbItem;
      });

      // 构建原始配置
      const originalConfig = Object.entries(oldInfo).reduce(
        (acc, [key, value]) => {
          const targetKey = sinkToTargetMap[key] || key;
          if (targetKey !== 'dbMap') {
            acc[targetKey] = value;
          }
          return acc;
        },
        { ...EMPTY_TASK_INFO.originalConfig }
      );

      // 构建新的任务信息
      this.newTaskInfo = {
        ...deepClone(EMPTY_TASK_INFO),
        mode: this.newTaskInfo.mode,
        jobData: oldInfo.jobData,
        sourceType: originalConfig.sourceType,
        targetType: originalConfig.targetType,
        dbMap: newDbMap,
        originalConfig
      };

      // 设置表默认值
      const { schemaWhiteListLevel } = this.newTaskInfo.originalConfig;
      this.newTaskInfo.common.isFullDatabaseSync = schemaWhiteListLevel === 'DB';
      this.newTaskInfo.common.tableDefaultDisabled = this.newTaskInfo.common.isFullDatabaseSync;
    },
    handleFunctionNextStep() {
      console.log(this.newTaskInfo);
      const { fullPeriodDate, checkPeriodDate, fullPeriod, checkMode } = this.newTaskInfo.functionConfig;
      this.newTaskInfo.jobType = this.newTaskInfo.functionConfig.jobType;

      const validatePeriodDate = (date) => {
        if (!date.dayType) return false;

        const validators = {
          HOUR: () => date.hour !== undefined && date.hour !== '',
          DAY: () => date.time,
          MONTH: () => date.date && date.time,
          YEAR: () => date.month && date.date && date.time
        };

        return validators[date.dayType]?.() ?? true;
      };

      if (fullPeriod && !validatePeriodDate(fullPeriodDate)) {
        this.$Modal.warning({
          title: this.$t('qing-tian-xie-wan-zheng-qie-zheng-que-de-xin-xi'),
          content: this.$t('qing-tian-xie-wan-zheng-de-ding-shi-ren-wu-xin-xi')
        });
      } else if (this.isCheckPeriodMode(checkMode) && !validatePeriodDate(checkPeriodDate)) {
        this.$Modal.warning({
          title: this.$t('qing-tian-xie-wan-zheng-qie-zheng-que-de-xin-xi'),
          content: this.$t('qing-tian-xie-wan-zheng-de-zhou-qi-xing-xiao-yan-yu-ding-zheng-ren-wu-xin-xi')
        });
      } else {
        this.$refs.newFunctionConfig.updateNextStep();
      }
    },
    async handleTableFilterNextStep() {
      let noEmptyDb = true;
      let noTargetTable = true;
      // TODO 编辑任务
      // this.taskInfo.dbMap.forEach((db) => {
      // db.selectedTables.forEach((table) => {
      //   if (table.hasInJob && table._checked && (!_.isEqual(table.action, table.originalAction) || table.sinkTable !== table.originalSinkTable)) {
      //     console.log('edit', table.action, table.originalAction, table.sinkTable !== table.originalSinkTable);
      //     table.edit = true;
      //     db.edit = true;
      //   }
      // });
      // });
      this.emptyTable = [];
      this.newTaskInfo.dbMap.forEach((item, index) => {
        if (!item.allTableList.length && item.tableSelectMode !== SELECT_MODE.SEARCH_AND_NOT_SELECT) {
          noEmptyDb = false;
          this.$Modal.warning({
            title: this.$t('ren-wu-chuang-jian-ti-shi'),
            content: this.$t('yuan-ku-itemsourcedb-wei-kong-ku-biao-de-shu-liang-wei-0-bu-zhi-chi-qian-yi', [item.sourceDb])
          });
        } else {
          item.allTableList.forEach((table) => {
            if (table._checked) {
              if (!table.targetTable) {
                noTargetTable = false;
                this.warnEmptyTableText = this.$t(
                  'qing-wu-bi-xuan-ze-mu-biao-biao-dang-qian-yi-xia-ku-biao-mei-you-xuan-ze-mu-biao-biao-dian-ji-ke-kuai-su-tiao-zhuan'
                );
                this.emptyTable.push({
                  name: `${item.sourceDb} > ${table.sourceTable}`,
                  _id: item._id
                });
                this.showNextStepWarn = true;
              }
            }
          });
          // noTargetTable 优先级暂定更高一点，先校验是否有缺选的目标表，再校验是否有缺选的源表
          if (noTargetTable && !item.tableCount) {
            noEmptyDb = false;
            this.warnEmptyTableText = this.$t(
              'mei-ge-ku-qing-zhi-shao-xuan-ze-yi-zhang-biao-yi-xia-ku-mei-you-xuan-ze-biao-dian-ji-ke-kuai-su-tiao-zhuan-0'
            );
            this.emptyTable.push({
              name: item.sourceDb,
              _id: item._id
            });
            this.showNextStepWarn = true;
          }
        }
      });
      if (noEmptyDb && noTargetTable) {
        await this.$refs.newTableFilter.updateNextStep();
      }
    },
    async handleCleanDataNextStep() {
      console.log('clean data next step');
      await this.$refs.newCleanData.updateNextStep();
    },
    async handleNewPreStep() {
      switch (this.taskInfo.step) {
        case JOB_STEP.FUNCTION:
          await this.handleFunctionPreStep();
          break;
        case JOB_STEP.TABLE_FILTER:
          await this.handleTableFilterPreStep();
          break;
        case JOB_STEP.CLEAN_DATA:
          await this.handleCleanDataPreStep();
          break;
        case JOB_STEP.TASK_INFO:
          await this.handleTaskInfoPreStep();
          break;
        default:
          break;
      }
    },
    async handleFunctionPreStep() {
      this.taskInfo = this.oldTaskInfo;
      await this.$refs.newFunctionConfig.updatePreStep();
    },
    async handleTableFilterPreStep() {
      await this.$refs.newTableFilter.updatePreStep();
    },
    async handleCleanDataPreStep() {
      await this.$refs.newCleanData.updatePreStep();
    },
    async handleTaskInfoPreStep() {
      await this.$refs.newTaskInfo.updatePreStep();
    },
    // 计算编辑变更摘要
    calculateEditChangesSummary() {
      // 重置变更摘要
      this.editChangesSummary = {
        hasChanges: false,
        databases: {
          added: [],
          removed: []
        },
        tables: {
          added: [],
          removed: []
        },
        columns: {
          added: [],
          removed: []
        },
        targetNames: {
          tables: [],
          columns: []
        },
        mappingRules: {
          table: [],
          column: []
        },
        actionBlacklist: {
          changed: []
        },
        virtualColumns: {
          added: [],
          modified: []
        },
        fieldTransforms: {
          added: [],
          modified: []
        },
        dataFilter: {
          whereConditions: []
        },
        primaryKeys: {
          changed: []
        },
        targetUpdateConditions: {
          changed: []
        },
        wideTables: {
          added: [],
          modified: [],
          removed: []
        }
      };

      try {
        const hasSchema = isHasSchema(this.taskInfo.sourceType);

        // 1. 新增库
        if (this.taskInfo.dbMap && Array.isArray(this.taskInfo.dbMap)) {
          this.taskInfo.dbMap.forEach((db) => {
            if (db && db.new) {
              const dbInfo = {
                db: db.sourceDb,
                sinkDb: db.sinkDb
              };
              if (hasSchema) {
                dbInfo.schema = db.sourceSchema;
                dbInfo.targetSchema = db.targetSchema;
              }
              this.editChangesSummary.databases.added.push(dbInfo);
            }
          });
        }

        // 2. 统计表和列的增删及修改
        if (this.taskInfo.dbMap && Array.isArray(this.taskInfo.dbMap)) {
          this.taskInfo.dbMap.forEach((db) => {
            if (db && db.selectedTables && Array.isArray(db.selectedTables)) {
              db.selectedTables.forEach((table) => {
                if (!table) return;

                const baseInfo = {
                  db: db.sourceDb,
                  table: table.sourceTable
                };
                if (hasSchema) {
                  baseInfo.schema = db.sourceSchema;
                }

                // 新增的表
                if (!table.hasInJob && table.selected) {
                  const tableInfo = {
                    ...baseInfo,
                    sinkDb: db.sinkDb,
                    sinkTable: table.sinkTable
                  };
                  if (hasSchema) {
                    tableInfo.sinkSchema = db.targetSchema;
                  }
                  this.editChangesSummary.tables.added.push(tableInfo);

                  // 新增表的操作黑名单
                  const newTableBlacklist = Array.isArray(table.actionBlacklist) ? table.actionBlacklist : [];
                  if (newTableBlacklist.length > 0) {
                    this.editChangesSummary.actionBlacklist.changed.push({
                      ...baseInfo,
                      oldActions: [],
                      newActions: newTableBlacklist
                    });
                  }

                  // 新增表的主键
                  const newTableCols = Array.isArray(table.cols) ? table.cols : [];
                  const originalCols = Array.isArray(table.originalCols) ? table.originalCols : [];
                  if (newTableCols.length > 0) {
                    this.editChangesSummary.primaryKeys.changed.push({
                      ...baseInfo,
                      oldKeys: originalCols,
                      newKeys: newTableCols
                    });
                  }

                  // 新增表的数据过滤条件
                  if (table.whereCondition && table.whereCondition.trim() !== '') {
                    this.editChangesSummary.dataFilter.whereConditions.push({
                      ...baseInfo,
                      oldCondition: table.originalWhereCondition || '',
                      newCondition: table.whereCondition
                    });
                  }

                  // 新增表的对端更新条件
                  if (table.targetWhereCondition && table.targetWhereCondition.trim() !== '') {
                    this.editChangesSummary.targetUpdateConditions.changed.push({
                      ...baseInfo,
                      oldCondition: table.originalTargetWhereCondition || '',
                      newCondition: table.targetWhereCondition
                    });
                  }
                }

                // 已有表的修改
                if (table.hasInJob && table.selected) {
                  // 操作黑名单变化
                  const currentBlacklist = Array.isArray(table.actionBlacklist) ? table.actionBlacklist : [];
                  const originalBlacklist = Array.isArray(table.originalActionBlacklist) ? table.originalActionBlacklist : [];

                  if (!_.isEqual(currentBlacklist, originalBlacklist)) {
                    this.editChangesSummary.actionBlacklist.changed.push({
                      ...baseInfo,
                      oldActions: originalBlacklist,
                      newActions: currentBlacklist
                    });
                  }

                  // 目标表名称变化
                  if (table.sinkTable && table.originalSinkTable && table.sinkTable !== table.originalSinkTable) {
                    this.editChangesSummary.targetNames.tables.push({
                      ...baseInfo,
                      oldName: table.originalSinkTable,
                      newName: table.sinkTable
                    });
                  }

                  // 主键变化
                  const currentCols = Array.isArray(table.cols) ? table.cols : [];
                  const originalCols = Array.isArray(table.originalCols) ? table.originalCols : [];

                  if (!_.isEqual(currentCols, originalCols)) {
                    this.editChangesSummary.primaryKeys.changed.push({
                      ...baseInfo,
                      oldKeys: originalCols,
                      newKeys: currentCols
                    });
                  }

                  // 数据过滤条件
                  const currentWhereCondition = table.whereCondition || '';
                  const originalWhereCondition = table.originalWhereCondition || '';
                  // 检查过滤条件是否有变化：新增、删除或修改
                  if (currentWhereCondition !== originalWhereCondition) {
                    this.editChangesSummary.dataFilter.whereConditions.push({
                      ...baseInfo,
                      oldCondition: originalWhereCondition,
                      newCondition: currentWhereCondition
                    });
                  }

                  // 对端更新条件
                  const currentTargetWhereCondition = table.targetWhereCondition || '';
                  const originalTargetWhereCondition = table.originalTargetWhereCondition || '';
                  // 检查对端更新条件是否有变化：新增、删除或修改
                  if (currentTargetWhereCondition !== originalTargetWhereCondition) {
                    this.editChangesSummary.targetUpdateConditions.changed.push({
                      ...baseInfo,
                      oldCondition: originalTargetWhereCondition,
                      newCondition: currentTargetWhereCondition
                    });
                  }
                }

                // 统计列的增删和修改
                if (db.selectedColumns && db.selectedColumns[table.sourceTable] && Array.isArray(db.selectedColumns[table.sourceTable])) {
                  db.selectedColumns[table.sourceTable].forEach((column) => {
                    if (!column) return;

                    if (column.selected) {
                      // 新增的列
                      if (!column.hasInJob) {
                        this.editChangesSummary.columns.added.push({
                          ...baseInfo,
                          column: column.sourceColumn || column.columnName,
                          sinkColumn: column.sinkColumn,
                          type: column.columnType
                        });
                      }

                      // 列映射名称变化
                      if (column.hasInJob && column.sinkColumn && column.originalSinkColumn && column.sinkColumn !== column.originalSinkColumn) {
                        this.editChangesSummary.targetNames.columns.push({
                          ...baseInfo,
                          column: column.sourceColumn || column.columnName,
                          oldName: column.originalSinkColumn,
                          newName: column.sinkColumn
                        });
                      }
                    } else if (column.hasInJob) {
                      // 删除的列
                      this.editChangesSummary.columns.removed.push({
                        ...baseInfo,
                        column: column.sourceColumn || column.columnName
                      });
                    }
                  });
                }
              });

              // 被删除的表
              db.selectedTables.forEach((table) => {
                if (table && table.hasInJob && !table.selected) {
                  const tableInfo = {
                    db: db.sourceDb,
                    table: table.sourceTable
                  };
                  if (hasSchema) {
                    tableInfo.schema = db.sourceSchema;
                  }
                  this.editChangesSummary.tables.removed.push(tableInfo);
                }
              });
            }
          });
        }

        // 3. 虚拟列
        if (this.taskInfo.virtualColumns && Array.isArray(this.taskInfo.virtualColumns)) {
          this.taskInfo.virtualColumns.forEach((virtual) => {
            if (!virtual) return;

            const fieldPathParts = virtual.fieldPathName?.split('/').filter((p) => p);
            if (!fieldPathParts || fieldPathParts.length < 2) return;

            const virtualInfo = {
              db: fieldPathParts[0],
              table: hasSchema ? fieldPathParts[2] : fieldPathParts[1],
              virtualColumns: virtual.customFieldList || []
            };
            if (hasSchema) {
              virtualInfo.schema = fieldPathParts[1];
            }

            if (!virtual.hasInJob) {
              this.editChangesSummary.virtualColumns.added.push(virtualInfo);
            } else if (virtual.hasInJob && !virtual.inBlackList) {
              this.editChangesSummary.virtualColumns.modified.push(virtualInfo);
            }
          });
        }

        // 4. 字段转换
        const transformColumnKeys = Object.keys(this.taskInfo.transformColumnData || {});
        transformColumnKeys.forEach((key) => {
          const transform = this.taskInfo.transformColumnData[key];
          if (!transform) return;

          const transformInfo = {
            db: transform.db,
            table: transform.table,
            columns: transform.columns || []
          };
          if (hasSchema) {
            transformInfo.schema = transform.schema;
          }

          if (!transform.hasInJob) {
            this.editChangesSummary.fieldTransforms.added.push(transformInfo);
          } else if (transform.hasInJob && !transform.inBlackList) {
            this.editChangesSummary.fieldTransforms.modified.push(transformInfo);
          }
        });

        // 5. 宽表
        if (this.taskInfo.wideTables && Array.isArray(this.taskInfo.wideTables)) {
          this.taskInfo.wideTables.forEach((wideTable) => {
            if (!wideTable) return;

            const wideTableInfo = {
              mainTable: wideTable.mainTable || '',
              joinTables: wideTable.joinTables || [],
              config: wideTable
            };

            // TODO:: 宽表的diff收集
          });
        }

        // 6. 表级和列级映射规则（serializeMapping 和 commonGenRule）
        try {
          let currentMappingDef = [];
          if (this.getConfigData) {
            const dataToUse =
              this.taskInfo.taskInfoNewData && this.taskInfo.taskInfoNewData.length > 0 ? this.taskInfo.taskInfoNewData : this.taskInfo.dbMap;

            if (dataToUse && (Array.isArray(dataToUse) ? dataToUse.length > 0 : Object.keys(dataToUse).length > 0)) {
              try {
                const configData = this.getConfigData(dataToUse, this.taskInfo.sourceType, this.taskInfo.sinkType, 'newData');
                currentMappingDef = configData.mappingDef || [];
              } catch (error) {
                // 生成mappingDef失败，忽略错误
                console.log('error', error);
              }
            }
          }

          let originalMappingDef = [];
          if (this.taskInfo.jobData?.schemaData?.mappingConfig) {
            originalMappingDef = JSON.parse(this.taskInfo.jobData.schemaData.mappingConfig);
          }

          const { sourceType, targetType } = this.taskInfo;
          let tableMethod = MAPPING_METHOD.TABLE_TABLE;
          if (isES(targetType)) {
            if (isMQ(sourceType)) {
              tableMethod = MAPPING_METHOD.TOPIC_INDEX;
            } else if (isES(sourceType)) {
              tableMethod = MAPPING_METHOD.TABLE_TABLE;
            } else {
              tableMethod = MAPPING_METHOD.TABLE_INDEX;
            }
          } else if (isMQ(targetType) && !isMQ(sourceType)) {
            tableMethod = MAPPING_METHOD.TABLE_TOPIC;
          } else if (isMQ(sourceType) && !isMQ(targetType)) {
            tableMethod = MAPPING_METHOD.TOPIC_TABLE;
          } else if (isMQ(sourceType) && isMQ(targetType)) {
            tableMethod = MAPPING_METHOD.TOPIC_TOPIC;
          } else if (isRedis(targetType)) {
            tableMethod = MAPPING_METHOD.TABLE_KEYPREFIX;
          }

          let currentTableMapping = {};
          let currentTableCommonGenRule = 'MIRROR';
          let currentColumnMapping = {};
          let currentColumnCommonGenRule = 'MIRROR';

          currentMappingDef.forEach((mapping) => {
            if (mapping.method === tableMethod) {
              currentTableMapping = mapping.serializeMapping || {};
              currentTableCommonGenRule = mapping.commonGenRule || 'MIRROR';
            }
            if (mapping.method === MAPPING_METHOD.COLUMN_COLUMN) {
              currentColumnMapping = mapping.serializeMapping || {};
              currentColumnCommonGenRule = mapping.commonGenRule || 'MIRROR';
            }
          });

          let originalTableMapping = {};
          let originalTableCommonGenRule = 'MIRROR';
          let originalColumnMapping = {};
          let originalColumnCommonGenRule = 'MIRROR';

          originalMappingDef.forEach((mapping) => {
            if (mapping.method === tableMethod) {
              originalTableMapping = mapping.serializeMapping || {};
              originalTableCommonGenRule = mapping.commonGenRule || 'MIRROR';
            }
            if (mapping.method === MAPPING_METHOD.COLUMN_COLUMN) {
              originalColumnMapping = mapping.serializeMapping || {};
              originalColumnCommonGenRule = mapping.commonGenRule || 'MIRROR';
            }
          });

          let hasNewTables = false;
          if (this.taskInfo.dbMap && Array.isArray(this.taskInfo.dbMap)) {
            this.taskInfo.dbMap.forEach((db) => {
              if (db && db.selectedTables && Array.isArray(db.selectedTables)) {
                db.selectedTables.forEach((table) => {
                  if (!table.hasInJob && table.selected) {
                    hasNewTables = true;
                  }
                });
              }
            });
          }

          // 表级映射规则是否有变化
          const hasTableCommonGenRuleChanged = originalTableCommonGenRule !== currentTableCommonGenRule;
          const hasTableSerializeMapping = Object.keys(currentTableMapping).length > 0 || Object.keys(originalTableMapping).length > 0;
          const hasTableSerializeMappingChanged = !_.isEqual(currentTableMapping, originalTableMapping);

          const shouldCollectTableMapping = hasTableCommonGenRuleChanged || hasTableSerializeMappingChanged;

          if (shouldCollectTableMapping) {
            this.editChangesSummary.mappingRules.table.push({
              oldCommonGenRule: originalTableCommonGenRule,
              newCommonGenRule: currentTableCommonGenRule,
              oldSerializeMapping: originalTableMapping,
              newSerializeMapping: currentTableMapping,
              hasSerializeMapping: hasTableSerializeMapping,
              hasNewTables: hasNewTables
            });
          }

          // 列级映射规则是否有变化
          const hasColumnCommonGenRuleChanged = originalColumnCommonGenRule !== currentColumnCommonGenRule;
          const hasColumnSerializeMappingChanged = !_.isEqual(currentColumnMapping, originalColumnMapping);

          const shouldCollectColumnMapping = hasColumnCommonGenRuleChanged || hasColumnSerializeMappingChanged;

          if (shouldCollectColumnMapping) {
            this.editChangesSummary.mappingRules.column.push({
              oldCommonGenRule: originalColumnCommonGenRule,
              newCommonGenRule: currentColumnCommonGenRule,
              oldSerializeMapping: originalColumnMapping,
              newSerializeMapping: currentColumnMapping,
              hasNewTables: hasNewTables
            });
          }
        } catch (error) {
          console.log('error', error);
        }

        // 是否有任何变更
        this.editChangesSummary.hasChanges =
          this.editChangesSummary.databases.added.length > 0 ||
          this.editChangesSummary.databases.removed.length > 0 ||
          this.editChangesSummary.tables.added.length > 0 ||
          this.editChangesSummary.tables.removed.length > 0 ||
          this.editChangesSummary.columns.added.length > 0 ||
          this.editChangesSummary.columns.removed.length > 0 ||
          this.editChangesSummary.targetNames.tables.length > 0 ||
          this.editChangesSummary.targetNames.columns.length > 0 ||
          this.editChangesSummary.mappingRules.table.length > 0 ||
          this.editChangesSummary.mappingRules.column.length > 0 ||
          this.editChangesSummary.actionBlacklist.changed.length > 0 ||
          this.editChangesSummary.virtualColumns.added.length > 0 ||
          this.editChangesSummary.virtualColumns.modified.length > 0 ||
          this.editChangesSummary.fieldTransforms.added.length > 0 ||
          this.editChangesSummary.fieldTransforms.modified.length > 0 ||
          this.editChangesSummary.dataFilter.whereConditions.length > 0 ||
          this.editChangesSummary.targetUpdateConditions.changed.length > 0 ||
          this.editChangesSummary.primaryKeys.changed.length > 0 ||
          this.editChangesSummary.wideTables.added.length > 0 ||
          this.editChangesSummary.wideTables.modified.length > 0 ||
          this.editChangesSummary.wideTables.removed.length > 0;

        console.log('完整变更摘要:', this.editChangesSummary);
      } catch (error) {
        this.editChangesSummary.hasChanges = true; // 出错时默认允许提交
      }
    }
  }
};

export default taskInfoMixin;
