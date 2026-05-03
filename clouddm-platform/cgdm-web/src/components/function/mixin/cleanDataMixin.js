import _ from 'lodash';
import store from '@/store/index';
import DataSourceGroup from '@/views/dataSourceGroup.json';

export default {
  data() {
    return {
      sinkDbTableMap: {},
      DataSourceGroup,
      store,
      dbTablesMap: {},
      tableList: [],
      selectedItem: {},
      batchSource: {},
      batchTarget: {},
      sourceType: '',
      sinkType: '',
      typeMeta: []
    };
  },
  methods: {
    getBatchData(item) {
      let batchSource = {};
      let batchTarget = {};

      if (this.type === 'edit') {
        this.sourceType = this.taskInfo.jobData.sourceDsVO.dataSourceType;
        this.sinkType = this.taskInfo.jobData.targetDsVO.dataSourceType;

        batchSource = {
          host: this.taskInfo.jobData.sourceDsVO.host,
          privateHost: this.taskInfo.jobData.sourceDsVO.privateHost,
          publicHost: this.taskInfo.jobData.sourceDsVO.publicHost,
          hostType: this.taskInfo.jobData.sourceDsHostType,
          type: this.sourceType,
          dbName: item.sourceDb ? item.sourceDb : item,
          dataSourceId: this.taskInfo.jobData.sourceDsVO.id,
          referenceDataJobId: this.taskInfo.jobData.dataJobId,
          referenceDsEndPointType: 'SOURCE',
          // tableName:info.sourceTable,
          dbTablesMap: this.dbTablesMap,
          tableSchema: this.selectedItem.sourceSchema,
          tableMetas: this.sourceTableMetaList[item.sourceDb ? item.sourceDb : item]
        };
        batchTarget = {
          host: this.taskInfo.jobData.targetDsVO.host,
          privateHost: this.taskInfo.jobData.targetDsVO.privateHost,
          publicHost: this.taskInfo.jobData.targetDsVO.publicHost,
          hostType: this.taskInfo.jobData.targetDsHostType,
          type: this.sinkType,
          dbName: item.targetDb || item.sinkDb,
          dataSourceId: this.taskInfo.jobData.targetDsVO.id,
          referenceDataJobId: this.taskInfo.jobData.dataJobId,
          referenceDsEndPointType: 'TARGET',
          // tableName:info.sourceTable,
          dbTablesMap: this.sinkDbTableMap,
          tableSchema: this.selectedItem.targetSchema,
          tableMetas: this.sinkTableMetaList[item.targetDb || item.sinkDb]
        };

        return {
          batchSource,
          batchTarget
        };
      }
      if (this.type === 'create') {
        const existCheckIndices = [];

        if (DataSourceGroup.es.includes(this.taskInfo.sinkType)) {
          Object.values(this.sinkDbTableMap).forEach((dbTables) => {
            dbTables.forEach((esindex) => {
              existCheckIndices.push(esindex);
            });
          });
        }

        this.sourceType = this.taskInfo.sourceType;
        this.sinkType = this.taskInfo.sinkType;

        batchSource = {
          host: this.taskInfo.sourceHostType === 'PUBLIC' ? this.taskInfo.sourcePublicHost : this.taskInfo.sourcePrivateHost,
          privateHost: this.taskInfo.sourcePrivateHost,
          publicHost: this.taskInfo.sourcePublicHost,
          hostType: this.taskInfo.sourceHostType,
          type: this.sourceType,
          userName:
            DataSourceGroup.oracle.indexOf(this.taskInfo.sourceType) > -1 && this.taskInfo.sourceAccountRole
              ? `${this.taskInfo.sourceAccount} as SYSDBA`
              : this.taskInfo.sourceAccount,
          dbName: item.sourceDb,
          dbTablesMap: this.dbTablesMap,
          dataSourceId: this.taskInfo.sourceDataSourceId,
          tableSchema: this.selectedItem.sourceSchema,
          tableMetas: this.sourceTableMetaList[item.sourceDb],
          clusterId: this.taskInfo.clusterId
        };
        batchTarget = {
          host: this.taskInfo.targetHostType === 'PUBLIC' ? this.taskInfo.sinkPublicHost : this.taskInfo.sinkPrivateHost,
          privateHost: this.taskInfo.sinkPrivateHost,
          publicHost: this.taskInfo.sinkPublicHost,
          hostType: this.taskInfo.targetHostType,
          type: this.sinkType,
          dbName: item.sinkDb,
          dbTablesMap: this.sinkDbTableMap,
          dataSourceId: this.taskInfo.targetDataSourceId,
          tableSchema: this.selectedItem.targetSchema,
          tableMetas: this.sinkTableMetaList[item.sinkDb],
          clusterId: this.taskInfo.clusterId,
          existCheckIndices
        };
        return {
          batchSource,
          batchTarget
        };
      }
    },
    getMqColumnData(item) {
      let selectedItem = {};

      if (item) {
        selectedItem = item;
      } else {
        selectedItem = this.selectedItem;
      }
      const batchData = this.getBatchData(selectedItem);
      this.promiseList.push(
        new Promise((resolve) => {
          this.$services
            .ccDataSourceColumnBatchList({
              data: batchData.batchTarget
            })
            .then((res) => {
              res.item = item;
              res.selectedItem = selectedItem;
              resolve(res);
            })
            .catch(() => {
              resolve(1);
            });
        })
      );
    },
    getSourceAndTargetColumnData(item) {
      let selectedItem = {};

      if (item) {
        selectedItem = item;
      } else {
        selectedItem = this.selectedItem;
      }
      const batchData = this.getBatchData(selectedItem);

      this.updateLoading(true);
      this.promiseList.push(
        new Promise((resolve) => {
          this.$services
            .ccDataSourceColumnBatchList({
              data: batchData.batchTarget
            })
            .then((response) => {
              let sinkTables = {};
              // console.log(selectedItem, response);

              try {
                if (response.success) {
                  // if (DataSourceGroup.oracle.indexOf(this.taskInfo.sinkType)>-1){
                  //     sinkTables = response.data.data.tableMetaDataMap[selectedItem.targetSchema];
                  // } else {

                  if (DataSourceGroup.es.includes(this.sinkType)) {
                    sinkTables = response.data.tableMetaDataMap.empty;
                  } else {
                    sinkTables = response.data.tableMetaDataMap[selectedItem.sinkDb ? selectedItem.sinkDb : selectedItem.targetDb];
                  }
                  // }

                  this.$services
                    .ccDataSourceColumnBatchList({
                      data: batchData.batchSource
                    })
                    .then((res) => {
                      res.item = item;
                      res.selectedItem = selectedItem;
                      res.sinkTables = sinkTables;
                      resolve(res);
                    })
                    .catch(() => {
                      resolve(1);
                    });
                } else {
                  resolve(false);
                }
                this.taskInfo.cleanDataSinkTables = sinkTables;
                if (sinkTables && Object.keys(sinkTables).length > 0) {
                  Object.keys(sinkTables).forEach((key) => {
                    if (!this.taskInfo.sinkColumns[selectedItem.sinkDb ? selectedItem.sinkDb : selectedItem.targetDb]) {
                      this.taskInfo.sinkColumns[selectedItem.sinkDb ? selectedItem.sinkDb : selectedItem.targetDb] = {};
                    }
                    this.taskInfo.sinkColumns[selectedItem.sinkDb ? selectedItem.sinkDb : selectedItem.targetDb][key] = sinkTables[key];
                  });
                  this.taskInfo.sinkColumns = { ...this.taskInfo.sinkColumns };
                }
              } catch (e) {
                console.log('e', e);
              }
            })
            .catch(() => {
              resolve(1);
            });
        })
      );
    },
    getSourceColumnData(item) {
      let selectedItem = {};

      if (item) {
        selectedItem = item;
      } else {
        selectedItem = this.selectedItem;
      }
      const batchData = this.getBatchData(selectedItem);
      this.promiseList.push(
        new Promise((resolve) => {
          this.$services
            .ccDataSourceColumnBatchList({
              data: batchData.batchSource
            })
            .then((res) => {
              res.item = item;
              res.selectedItem = selectedItem;
              resolve(res);
            })
            .catch(() => {
              resolve(1);
            });
        })
      );
    },
    getDsTypeMeta() {
      this.$services
        .ccConstantDsListVirtualColType({
          data: {
            srcDsType: this.taskInfo.sourceType,
            dstDsType: this.taskInfo.sinkType
          }
        })
        .then((res) => {
          if (res.success) {
            this.typeMeta = res.data;
          }
        });
    }
  }
};
