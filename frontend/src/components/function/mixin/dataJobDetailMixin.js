import { isES } from '@/utils';
import DataSourceGroup from '@/views/dataSourceGroup.json';

export default {
  computed: {
    getDelayColor() {
      return (delay) => {
        // 位点延迟小于1分钟，正常，为绿色，展示数据延迟。1分钟<位点延迟<5分钟，黄色，展示位点延迟。位点延迟>5分钟，异常，为红色，展示位点延迟。
        if (delay <= 60000) {
          return 'color: #19be6b';
        }
        if (delay < 300000) {
          return 'color: #ff9900';
        }
        return 'color: #ed4014';
      };
    }
  },
  methods: {
    getTargetData(type, sourceDb, sourceTable, sourceColumn) {
      let data = '';

      this.mappingDef.forEach((item) => {
        if (item.method === type) {
          if (type === 'DB_DB') {
            const mapping = {
              value: sourceDb.db
            };

            if (item.serializeMapping[JSON.stringify(mapping)]) {
              data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
            } else {
              data = sourceDb.db;
            }
          } else if (type === 'SCHEMA_SCHEMA') {
            const mapping = {
              parent: {
                value: sourceDb.db
              },
              value: sourceDb.tableSpaces ? sourceDb.tableSpaces[0].tableSpace : sourceDb.schemas[0].schema
            };

            if (item.serializeMapping[JSON.stringify(mapping)]) {
              const mappingData = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]);

              data = `${mappingData.parent.value}.${mappingData.value}`;
            } else {
              data = sourceDb.db;
            }
          } else if (type === 'DB_SCHEMA') {
            const mapping = {
              value: sourceDb.db
            };

            if (item.serializeMapping[JSON.stringify(mapping)]) {
              const mappingData = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]);

              data = `${mappingData.parent.value}.${mappingData.value}`;
            } else {
              data = sourceDb.db;
            }
          } else if (type === 'SCHEMA_DB') {
            if (sourceDb.tableSpaces) {
              const mapping = {
                parent: {
                  value: sourceDb.db
                },
                value: sourceDb.tableSpaces[0].tableSpace
              };

              if (item.serializeMapping[JSON.stringify(mapping)]) {
                const mappingData = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]);
                data = mappingData.value;
              } else {
                data = sourceDb.db;
              }
            } else {
              const mapping = {
                parent: {
                  value: sourceDb.db
                },
                value: sourceDb.schemas[0].schema
              };

              if (item.serializeMapping[JSON.stringify(mapping)]) {
                const mappingData = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]);

                data = mappingData.value;
              } else {
                data = sourceDb.db;
              }
            }
          } else if (type === 'TABLE_TABLE') {
            if (DataSourceGroup.hasSchema.includes(this.jobData.sourceDsVO.dataSourceType)) {
              if (DataSourceGroup.pg.includes(this.jobData.sourceDsVO.dataSourceType)) {
                const mapping = {
                  parent: {
                    value: sourceDb.schemas[0].schema,
                    parent: {
                      value: sourceDb.db
                    }
                  },
                  value: sourceTable.table
                };

                if (item.serializeMapping[JSON.stringify(mapping)]) {
                  data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
                } else {
                  data = sourceTable.table;
                }
              } else if (DataSourceGroup.oracle.includes(this.jobData.sourceDsVO.dataSourceType)) {
                const mapping = {
                  parent: {
                    value: sourceDb.tableSpaces[0].tableSpace,
                    parent: {
                      value: sourceDb.db
                    }
                  },
                  value: sourceTable.table
                };

                if (item.serializeMapping[JSON.stringify(mapping)]) {
                  data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
                } else {
                  data = sourceTable.table;
                }
              }
            } else if (isES(this.jobData.sourceDsVO.dataSourceType)) {
              const mapping = {
                value: sourceTable.table
              };
              if (item.serializeMapping[JSON.stringify(mapping)]) {
                data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
              } else {
                data = sourceTable.table || sourceTable.collection;
              }
            } else {
              const mapping = {
                parent: {
                  value: sourceDb.db
                },
                value: sourceTable.table || sourceTable.collection
              };

              if (item.serializeMapping[JSON.stringify(mapping)]) {
                data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
              } else {
                data = sourceTable.table || sourceTable.collection;
              }
            }
          } else if (type === 'TABLE_INDEX') {
            let mapping;
            if (sourceDb.schemas || sourceDb.tableSpaces) {
              mapping = {
                parent: {
                  parent: {
                    value: sourceDb.db
                  },
                  value: sourceDb.schemas ? sourceDb.schemas[0].schema : sourceDb.tableSpaces[0].tableSpace
                },
                value: sourceTable.table || sourceTable.collection
              };
            } else {
              mapping = {
                parent: {
                  value: sourceDb.db
                },
                value: sourceTable.table || sourceTable.collection
              };
            }

            if (item.serializeMapping[JSON.stringify(mapping)]) {
              data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
            } else {
              data = sourceTable.table || sourceTable.collection;
            }
          } else if (type === 'TABLE_TOPIC') {
            let mapping;
            if (sourceDb.schemas || sourceDb.tableSpaces) {
              mapping = {
                parent: {
                  parent: {
                    value: sourceDb.db
                  },
                  value: sourceDb.schemas ? sourceDb.schemas[0].schema : sourceDb.tableSpaces[0].tableSpace
                },
                value: sourceTable.table || sourceTable.collection
              };
            } else {
              mapping = {
                parent: {
                  value: sourceDb.db
                },
                value: sourceTable.table || sourceTable.collection
              };
            }

            if (item.serializeMapping[JSON.stringify(mapping)]) {
              data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
            } else {
              data = sourceTable.table || sourceTable.collection;
            }
          } else if (type === 'TABLE_KEYPREFIX') {
            const mapping = {
              parent: {
                value: sourceDb.db
              },
              value: sourceTable.table
            };

            if (item.serializeMapping[JSON.stringify(mapping)]) {
              const prefix = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;

              data = prefix;
              this.targetSchema.forEach((target) => {
                if (target.prefix === prefix) {
                  target.suffixFields.forEach((field) => {
                    data += `:{${field}}`;
                  });
                }
              });
            } else {
              data = sourceTable.table;
            }
          } else if (type === 'TOPIC_TABLE') {
            const mapping = {
              value: sourceTable.topic
            };
            if (item.serializeMapping[JSON.stringify(mapping)]) {
              data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
            }
          } else if (type === 'TOPIC_TOPIC') {
            const mapping = {
              value: sourceTable.topic
            };
            if (item.serializeMapping[JSON.stringify(mapping)]) {
              data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
            }
          } else if (type === 'TOPIC_INDEX') {
            const mapping = {
              value: sourceTable.topic
            };
            if (item.serializeMapping[JSON.stringify(mapping)]) {
              data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
            }
          } else if (type === 'COLUMN_COLUMN') {
            if (DataSourceGroup.hasSchema.includes(this.jobData.sourceDsVO.dataSourceType)) {
              if (DataSourceGroup.pg.includes(this.jobData.sourceDsVO.dataSourceType)) {
                const mapping = {
                  parent: {
                    parent: {
                      parent: {
                        value: sourceDb.db
                      },
                      value: sourceDb.schemas[0].schema
                    },
                    value: sourceTable.table
                  },
                  value: sourceColumn.column
                };

                const mapping2 = {
                  parent: {
                    value: sourceTable.table,
                    parent: {
                      value: sourceDb.schemas[0].schema,
                      parent: {
                        value: sourceDb.db
                      }
                    }
                  },
                  value: sourceColumn.column
                };

                if (item.serializeMapping[JSON.stringify(mapping)]) {
                  data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
                } else if (item.serializeMapping[JSON.stringify(mapping2)]) {
                  data = JSON.parse(item.serializeMapping[JSON.stringify(mapping2)]).value;
                } else {
                  data = sourceColumn.column;
                }
              } else if (DataSourceGroup.oracle.includes(this.jobData.sourceDsVO.dataSourceType)) {
                const mapping = {
                  parent: {
                    value: sourceTable.table,
                    parent: {
                      value: sourceDb.tableSpaces[0].tableSpace,
                      parent: {
                        value: sourceDb.db
                      }
                    }
                  },
                  value: sourceColumn.column
                };

                if (item.serializeMapping[JSON.stringify(mapping)]) {
                  data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
                } else {
                  data = sourceColumn.column;
                }
              }
            } else {
              const mapping = {
                parent: {
                  value: sourceTable.table,
                  parent: {
                    value: sourceDb.db
                  }
                },
                value: sourceColumn.column
              };

              if (item.serializeMapping[JSON.stringify(mapping)]) {
                data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
              } else {
                data = sourceColumn.column;
              }
            }
          }
        } else if (type === 'TABLE_TABLE_WITH_SCHEMA' && item.method === 'TABLE_TABLE') {
          const mapping = {
            parent: {
              value: sourceDb.tableSpaces ? sourceDb.tableSpaces[0].tableSpace : sourceDb.schemas[0].schema,
              parent: {
                value: sourceDb.db
              }
            },
            value: sourceTable.table
          };

          if (item.serializeMapping[JSON.stringify(mapping)]) {
            data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
          } else {
            data = sourceTable.table;
          }
        } else if (type === 'COLUMN_COLUMN_WITH_SCHEMA' && item.method === 'COLUMN_COLUMN') {
          const mapping = {
            parent: {
              value: sourceTable.table,
              parent: {
                value: sourceDb.tableSpaces ? sourceDb.tableSpaces[0].tableSpace : sourceDb.schemas[0].schema,
                parent: {
                  value: sourceDb.db
                }
              }
            },
            value: sourceColumn.column
          };

          if (item.serializeMapping[JSON.stringify(mapping)]) {
            data = JSON.parse(item.serializeMapping[JSON.stringify(mapping)]).value;
          } else {
            data = sourceColumn.column;
          }
        }
      });

      return data;
    },
    getProgressStatus(status, health) {
      if (status === 'RUNNING') {
        if (health === 'Unhealthy') {
          return 'wrong';
        }
        return 'active';
      }
      if (status === 'ABNORMAL') {
        return 'wrong';
      }
      if (status === 'COMPLETE') {
        return 'success';
      }
      if (status !== 'STOP') {
        if (health === 'Unhealthy') {
          return 'wrong';
        }
        return 'normal';
      }
      return 'normal';
    }
  }
};
