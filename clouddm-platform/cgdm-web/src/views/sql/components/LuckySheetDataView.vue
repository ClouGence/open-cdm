<script>
import { h } from 'vue';
import {
  VerticalRightOutlined,
  LeftOutlined,
  RightOutlined,
  ReloadOutlined,
  DownloadOutlined,
  VerticalLeftOutlined,
  MinusOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';
import deepClone from 'lodash.clonedeep';
import { Modal } from 'ant-design-vue';
import copyMixin from '@/mixins/copyMixin';
import exportMixin from '@/mixins/exportMixin';
import dayjs from 'dayjs';
import * as XLSX from 'xlsx';
import browseMixin from '@/mixins/browseMixin';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import i18n from '@/i18n';
import { TabManager } from '@/views/sql/tabManager';
import { mapState } from 'vuex';
import CCModal from '@/components/ui/CCModal.vue';

const BG_COLOR = {
  ADD: 'rgb(236, 255, 220)',
  DELETE: 'rgb(250, 128, 114)',
  ADD_DELETE: 'orange',
  UPDATE: 'yellow',
  CUSTOM: '#fff'
};

const ORDER_TEXT = {
  desc: '↓',
  asc: '↑',
  '': '↕'
};

const CELL_RIGHT_MENU = {
  COPY_COLUMN_NAME: {
    id: 'luckysheet-custom-copy-column-name',
    title: '复制列名'
  },
  COPY_ROW: {
    id: 'luckysheet-custom-copy-row',
    title: i18n.global.t('fu-zhi-xuan-zhong-hang')
  },
  DIVIDER1: {
    title: 'divider'
  },
  SET_NULL: {
    id: 'luckysheet-custom-set-null',
    title: i18n.global.t('she-zhi-wei-null')
  },
  SET_EMPTY_STRING: {
    id: 'luckysheet-custom-set-empty_string',
    title: i18n.global.t('she-zhi-wei-kong-zi-fu-chuan')
  },
  ROLLBACK_DATA: {
    id: 'luckysheet-custom-rollback-data',
    title: i18n.global.t('che-xiao-xiu-gai')
  },
  DIVIDER2: {
    title: 'divider'
  },
  // ROLLBACK_DELETE_ROW: {
  //   id: 'luckysheet-custom-rollback_delete-row',
  //   title: '撤销 删除行'
  // },
  ADD_ROW: {
    id: 'luckysheet-custom-add-row',
    title: i18n.global.t('xin-zeng-hang')
  },
  DELETE_ROW: {
    id: 'luckysheet-custom-delete-row',
    title: i18n.global.t('shan-chu-hang')
  },
  DIVIDER3: {
    title: 'divider'
  },
  EXPORT_DATA_CSV: {
    id: 'luckysheet-custom-export-data-csv',
    title: i18n.global.t('dao-chu-xuan-zhong-dan-yuan-ge-csv-ge-shi')
  },
  EXPORT_ROW_CSV: {
    id: 'luckysheet-custom-export-row-csv',
    title: i18n.global.t('dao-chu-xuan-zhong-hang-csv-ge-shi')
  },
  EXPORT_PAGE_CSV: {
    id: 'luckysheet-custom-export-page-csv',
    title: i18n.global.t('dao-chu-dang-qian-ye-csv-ge-shi')
  }
};

export default {
  name: 'LuckySheetDataView',
  components: {
    CCModal,
    ReadOnlyEditor,
    VerticalRightOutlined,
    LeftOutlined,
    RightOutlined,
    ReloadOutlined,
    DownloadOutlined,
    VerticalLeftOutlined,
    MinusOutlined,
    PlusOutlined
  },
  props: {
    tab: Object,
    setLoading: Function,
    handleClickDsStatusIcon: Function
  },
  computed: {
    ...mapState(['userInfo']),
    isEditing() {
      const hasAddOrDeleteRow = this.tab.addRows.length || this.tab.deleteRows.length;
      if (hasAddOrDeleteRow) {
        return true;
      }

      for (let rowIndex = 0; rowIndex < this.tab.updateCellList.length; rowIndex++) {
        const row = this.tab.updateCellList[rowIndex];
        const rowKeys = Object.keys(row);
        if (rowKeys.length) {
          return true;
        }
      }

      return false;
    }
  },
  mixins: [copyMixin, exportMixin, browseMixin],
  data() {
    return {
      tabManager: null,
      deleteList: {},
      createList: {},
      updateList: {},
      refreshAfterExecute: false,
      showExecuteInfoModal: false,
      executeInfo: [],
      executeSQLLoading: false,
      loading: false,
      sqls: [],
      renewData: {},
      sqlString: '',
      showSqlModal: false,

      options: {
        container: 'luckysheet',
        lang: 'zh',
        allowCopy: false,
        showtoolbar: false,
        showtoolbarConfig: false,
        showinfobar: false,
        showsheetbar: false,
        showsheetbarConfig: false,
        // showstatisticBar: false,
        // showstatisticBarConfig: false,
        enableAddBackTop: false,
        enableAddRow: false,
        cellRightClickConfig: {
          copy: true, // 复制
          copyAs: false, // 复制为
          paste: false, // 粘贴
          insertRow: false, // 插入行
          insertColumn: false, // 插入列
          deleteRow: false, // 删除选中行
          deleteColumn: false, // 删除选中列
          deleteCell: false, // 删除单元格
          hideRow: false, // 隐藏选中行和显示选中行
          hideColumn: false, // 隐藏选中列和显示选中列
          rowHeight: false, // 行高
          columnWidth: false, // 列宽
          clear: false, // 清除内容
          matrix: false, // 矩阵操作选区
          sort: false, // 排序选区
          filter: false, // 筛选选区
          chart: false, // 图表生成
          image: false, // 插入图片
          link: false, // 插入链接
          data: false, // 数据验证
          cellFormat: false, // 设置单元格格式
          customs: []
        },
        hook: {
          updated: (operate) => {
            console.log('update');
            if (operate && operate.curData) {
              operate.curData.forEach((row, rowIndex) => {
                let arr;
                if (Array.isArray(row)) {
                  arr = row;
                } else {
                  arr = Object.values(row);
                }
                arr.forEach((cell) => {
                  if (cell && cell.custom && !cell.custom.new && !cell.custom.delete) {
                    if (cell.custom.update) {
                      this.tab.updateCellList[rowIndex][cell.custom.column.column] = true;
                    } else {
                      delete this.tab.updateCellList[rowIndex][cell.custom.column.column];
                    }
                  }
                });
              });
            }
          },
          cellUpdated: (r, c, oldValue, newValue) => {
            if (newValue && newValue.custom && !newValue.custom.new && !newValue.custom.delete) {
              if (newValue.custom.update) {
                this.tab.updateCellList[r][newValue.custom.column.column] = true;
              } else {
                delete this.tab.updateCellList[r][newValue.custom.column.column];
              }
            }
          },
          cellUpdateBefore: () => {
            console.log('cell update before');
          },
          cellRenderAfter: (cell, position, sheet, ctx) => {
            if (cell && cell.custom && Object.is(cell.v, null)) {
              const { start_r, end_r, start_c, end_c } = position;
              const width = end_c - start_c;
              const height = end_r - start_r;
              ctx.clearRect(start_c, 0, width - 1, height - 1);
              ctx.font = 'italic bold 12px 微软雅黑';
              ctx.fillStyle = '#ccc';
              let text = '<NULL>';
              if (cell.custom.column.hasDefault) {
                text = '<DEFAULT>';
              }
              if (cell.custom.column.autoincrement) {
                text = '<AUTO>';
              }

              ctx.fillText(text, start_c, start_r + height / 1.5);
            }

            if (cell && cell.custom && cell.custom.more) {
              const { start_r, end_r, start_c, end_c } = position;
              const width = end_c - start_c;
              const height = end_r - start_r;

              ctx.fillStyle = 'rgb(240, 240, 239)';
              ctx.fillRect(start_c + width - 52, start_r - 1, 51, height - 2);

              ctx.fillStyle = '#000';
              ctx.fillText(this.$t('wen-ben-jie-duan'), start_c + width - 52, start_r + height / 1.6);
            }
          },
          workbookCreateAfter: () => {
            this.setLoading(false);
            window.luckysheet.setRangeShow({ row: [0, 0], column: [0, 0] }, { show: false });
          },
          scroll: () => {
            window.luckysheet.exitEditMode();
          },
          columnTitleCellRenderAfter: (_, position, ctx) => {
            const { c, left, height, width } = position;

            let offset = 0;

            const column = window.luckysheetData[window.luckysheetData.activeKey].column[c];

            ctx.clearRect(left, 0, width - 1, height - 1);

            if (column.isPk) {
              const pkImg = window.luckysheetData.storeUserImage.pkImg;

              ctx.drawImage(pkImg, left, 3, 12, 12);

              offset += 12;
            }

            if (column.isUk) {
              const ukImg = window.luckysheetData.storeUserImage.ukImg;

              ctx.drawImage(ukImg, left + offset, 3, 12, 12);

              offset += 12;
            }
            ctx.fillText(`${column.isNullable ? '' : '*'}${column.column}`, left + offset, height / 2);
            ctx.fillStyle = 'rgb(240, 240, 239)';
            ctx.fillRect(left + width - height - 1, -1, height, height);
            ctx.globalCompositeOperation = 'source-over';
            ctx.fillStyle = '#000';
            ctx.fillText(ORDER_TEXT[column.order], left + width - height + 6, height / 2);
          },
          cellMousedown: (cell, position) => this.handleCellMousedown(cell, position),
          sheetMousemove: (cell) => {
            if (cell && cell.custom && cell.custom.disableEdit) {
              document.querySelector('.luckysheet-cell-sheettable').style.cursor = 'not-allowed';
            } else {
              document.querySelector('.luckysheet-cell-sheettable').style.cursor = 'default';
            }
          },
          sheetMouseup: (cell, position, sheet, moveState, ctx) => this.handleSheetMouseUp(cell, position, sheet, moveState, ctx)
        },
        data: [
          {
            name: 'data',
            celldata: [],
            config: {
              columnlen: {},
              authority: {
                allowRangeList: [{}]
              }
            },
            dataVerification: {}
          }
        ]
      }
    };
  },
  watch: {
    'tab.key': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.tab.total = null;
          this.handleGetTableData(-2, true);
        }
      },
      deep: true
    }
  },
  async mounted() {
    this.tabManager = new TabManager();
    Object.keys(CELL_RIGHT_MENU).forEach((key) => {
      const item = CELL_RIGHT_MENU[key];
      this.options.cellRightClickConfig.customs.push({
        title: item.title,
        id: item.id,
        onClick: (clickEvent, event, params) => this.handleRightMenuClick(item.id, clickEvent, event, params)
      });
    });
    if (this.tab.connected) {
      await this.handleGetTableData(-2, true, true, false, true);
    } else {
      this.showConnectedModal = true;
    }
  },
  methods: {
    h,
    dayjs,
    handleExport(type) {
      let exportData = [];
      const range = window.luckysheet.getRange();
      const { row, column } = range[0];
      switch (type) {
        case 'csvCurrentPage':
          exportData = this.tab.rawTableData.resultSet;
          break;
        case 'csvSelectedRows':
          for (let rowIndex = row[0]; rowIndex <= row[1]; rowIndex++) {
            exportData.push(this.tab.rawTableData.resultSet[rowIndex]);
          }
          break;
        case 'csvSelectedData':
          for (let rowIndex = row[0]; rowIndex <= row[1]; rowIndex++) {
            const generatedRow = {};
            for (let columnIndex = column[0]; columnIndex <= column[1]; columnIndex++) {
              const col = this.tab.columnWithoutHidden[columnIndex];
              generatedRow[col.column] = this.tab.rawTableData.resultSet[rowIndex][col.column];
            }
            exportData.push(generatedRow);
          }
          break;
        default:
          break;
      }

      const workbook = XLSX.utils.book_new();
      const worksheet = XLSX.utils.json_to_sheet(exportData);
      XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');

      XLSX.writeFile(workbook, `${this.tab.key}.csv`);
    },
    async handleRun() {
      this.executeSQLLoading = true;
      const data = this.generateDatasourceParams();
      this.executeInfo = [];

      this.createList = {};
      this.updateList = {};
      this.deleteList = {};
      let error = false;
      try {
        for await (const sqlObj of this.sqls) {
          if (error) {
            this.executeInfo.unshift({
              database: this.tab.node.SCHEMA.name,
              queryBody: sqlObj.sql,
              refresh: sqlObj.refresh,
              success: false,
              message: this.$t('wei-zhi-hang')
            });
            this.refreshAfterExecute = false;
          } else {
            const { sequence, sql } = sqlObj;
            data.columnList = this.tab.rawTableData.columnList;
            data.changeRow = deepClone(this.renewData[sequence]);
            // data.sqlMessage = { sql, sequence, refresh };
            const res = await this.$services.dmEditorDataSaveData({
              data
            });

            if (res.success) {
              this.executeInfo.unshift({
                database: this.tab.node.SCHEMA.name,
                queryBody: sql,
                ...res.data
              });

              if (!res.success) {
                error = true;
                this.refreshAfterExecute = false;
              } else {
                this.refreshAfterExecute = res.data.refresh;
                const { resultSet, resultSetMore } = res.data;

                if (this.renewData[sequence].type === 'deleteParam') {
                  this.deleteList[sequence] = {
                    sequence,
                    resultSet,
                    resultSetMore
                  };
                }

                if (this.renewData[sequence].type === 'createParam') {
                  this.createList[sequence] = {
                    sequence,
                    resultSet,
                    resultSetMore
                  };
                }

                if (this.renewData[sequence].type === 'updateParam') {
                  this.updateList[sequence] = {
                    sequence,
                    resultSet,
                    resultSetMore
                  };
                }
              }
            }
          }

          if (this.showSqlModal) {
            this.showSqlModal = false;
          }
          if (!this.showExecuteInfoModal) {
            this.showExecuteInfoModal = true;
          }
        }

        this.executeSQLLoading = false;
      } catch (e) {
        this.executeSQLLoading = false;
      }

      if (!error) {
        this.tab.isEditing = false;
      }
    },
    async handleCloseExecuteInfoModal() {
      console.log('handleCloseExecuteInfoModal');
      this.showExecuteInfoModal = false;
      if (this.refreshAfterExecute) {
        await this.handleGetTableData(-2, true, true, true);
      } else {
        const celldata = [];
        const preData = [];
        const { resultSet, resultSetMore } = this.tab.rawTableData;
        const generateResultSet = [];
        const generateResultSetMore = [];
        const deleteRows = [];
        const addRows = [];
        const updateCellList = [];

        Object.keys(this.createList).forEach((key) => {
          resultSet[key] = this.createList[key].resultSet[0];
          resultSetMore[key] = this.createList[key].resultSetMore[0];
        });

        Object.keys(this.updateList).forEach((key) => {
          resultSet[key] = this.updateList[key].resultSet[0];
          resultSetMore[key] = this.updateList[key].resultSetMore[0];
        });

        resultSet.forEach((row, rowIndex) => {
          if (!this.deleteList[rowIndex]) {
            generateResultSet.push(row);
          }
        });

        resultSetMore.forEach((row, rowIndex) => {
          if (!this.deleteList[rowIndex]) {
            generateResultSetMore.push(row);
          }
        });

        window.luckysheet.getSheetData().forEach((row, rowIndex) => {
          if (!this.deleteList[rowIndex] && !(row[0].custom.new && row[0].custom.delete)) {
            let arr;
            if (Array.isArray(row)) {
              arr = row;
            } else {
              arr = Object.values(row);
            }
            arr.forEach((cell, columnIndex) => {
              cell.custom.r = rowIndex;
              cell.custom.c = columnIndex;
            });
            preData.push(arr);
          }
        });

        preData.forEach((row, rowIndex) => {
          row = Array.isArray(row) ? row : Object.values(row);
          updateCellList[rowIndex] = [];
          row.forEach((cell, columnIndex) => {
            if (this.tab.addRows.includes(cell.custom.r)) {
              if (this.createList[cell.custom.r]) {
                cell.custom.new = false;
                cell.custom.v = this.createList[cell.custom.r].resultSet[0][cell.custom.column.column];
                cell.bg = BG_COLOR.CUSTOM;
                cell.v = this.createList[cell.custom.r].resultSet[0][cell.custom.column.column];
                cell.m = cell.v;
                cell.r = rowIndex;
              }

              cell.custom.r = rowIndex;
              cell.custom.c = columnIndex;
              celldata.push({
                r: rowIndex,
                c: columnIndex,
                v: {
                  ...cell
                }
              });
            } else {
              if (this.updateList[cell.custom.r]) {
                cell.custom.update = false;
                cell.custom.v = this.updateList[cell.custom.r].resultSet[0][cell.custom.column.column];
                cell.bg = BG_COLOR.CUSTOM;
                cell.v = this.updateList[cell.custom.r].resultSet[0][cell.custom.column.column];
                cell.m = cell.v;
                cell.r = rowIndex;
              }

              cell.custom.r = rowIndex;
              cell.custom.c = columnIndex;
              celldata.push({
                r: rowIndex,
                c: columnIndex,
                v: {
                  ...cell
                }
              });
            }
          });
        });

        celldata.forEach((cell) => {
          if (cell.v.custom.new) {
            console.log(cell.v.custom.r);
            if (!addRows.includes(cell.v.custom.r)) {
              addRows.push(cell.v.custom.r);
            }
          }
          if (cell.v.custom.delete) {
            if (!deleteRows.includes(cell.v.custom.r)) {
              deleteRows.push(cell.v.custom.r);
            }
          }

          if (cell.v.custom.update) {
            if (!updateCellList[cell.v.custom.r][cell.v.custom.column.column]) {
              updateCellList[cell.v.custom.r][cell.v.custom.column.column] = true;
            }
          }
        });

        delete this.options.data[0].data;
        this.options.data[0].celldata = celldata;
        this.options.data[0].row = preData.length;

        this.tab.rawTableData.resultSet = generateResultSet;
        this.tab.rawTableData.resultSetMore = generateResultSetMore;
        this.tab.addRows = addRows;
        this.tab.deleteRows = deleteRows;
        this.tab.updateCellList = updateCellList;

        if (celldata.length) {
          window.luckysheet.updataSheet(
            {
              data: this.options.data
            },
            {
              success: () => {
                window.luckysheet.refresh();
              }
            }
          );
        }
      }
    },
    async handleSubmit() {
      window.luckysheet.exitEditMode();
      window.luckysheet.setRangeShow({ row: [0, 0], column: [0, 0] }, { show: false });
      const copyEle = document.querySelector('#luckysheet-selection-copy>.luckysheet-selection-copy');
      if (copyEle) {
        copyEle.style.display = 'none';
      }
      const { data } = window.luckysheet.getLuckysheetfile()[0];
      const dataParamList = [];
      const renewData = {};

      // create
      for (let rowIndex = this.tab.rawTableData.resultSet.length; rowIndex < data.length; rowIndex++) {
        if (this.tab.addRows.includes(rowIndex) && !this.tab.deleteRows.includes(rowIndex)) {
          const row = {};
          if (Array.isArray(data[rowIndex])) {
            data[rowIndex].forEach((col) => {
              row[col.custom.column.column] = col.v;
            });
          } else {
            Object.values(data[rowIndex]).forEach((col) => {
              row[col.custom.column.column] = col.v;
            });
          }
          const item = {
            newData: row,
            sequence: rowIndex,
            type: 'createParam'
          };
          dataParamList.push(item);
          renewData[rowIndex] = item;
        }
      }

      // delete
      for (let rowIndex = 0; rowIndex < this.tab.rawTableData.resultSet.length; rowIndex++) {
        if (this.tab.deleteRows.includes(rowIndex)) {
          const deleteRow = {};
          this.tab.whereKeyList.forEach((key) => {
            deleteRow[key] = this.tab.rawTableData.resultSet[rowIndex][key];
          });
          const item = {
            whereData: deleteRow,
            sequence: rowIndex,
            type: 'deleteParam'
          };
          dataParamList.push(item);
          renewData[rowIndex] = item;
        }
      }

      // update
      for (let rowIndex = 0; rowIndex < this.tab.rawTableData.resultSet.length; rowIndex++) {
        const whereData = {};
        const updateData = {};
        this.tab.whereKeyList.forEach((key) => {
          whereData[key] = this.tab.rawTableData.resultSet[rowIndex][key];
        });
        for (let colIndex = 0; colIndex < this.tab.columnWithoutHidden.length; colIndex++) {
          const column = this.tab.columnWithoutHidden[colIndex];
          if (
            data[rowIndex][colIndex] &&
            data[rowIndex][colIndex].custom &&
            data[rowIndex][colIndex].custom.update &&
            !data[rowIndex][colIndex].custom.new &&
            !data[rowIndex][colIndex].custom.delete
          ) {
            updateData[column.column] = data[rowIndex][colIndex].v;
          }
        }

        if (Object.keys(updateData).length) {
          const item = {
            whereData,
            newData: updateData,
            sequence: rowIndex,
            type: 'updateParam'
          };

          dataParamList.push(item);
          renewData[rowIndex] = item;
        }
      }

      const res = await this.$services.dmEditorDataGenerateDml({
        data: {
          ...this.generateDatasourceParams(),
          changeRows: dataParamList,
          columnList: this.tab.rawTableData.columnList
        }
      });

      if (res.success) {
        this.createSQLLoading = false;
        this.sqls = res.data;
        this.renewData = renewData;
        const sqlList = [];
        res.data.forEach((sql) => {
          sqlList.push(sql.sql);
        });
        if (sqlList.length) {
          this.sqlString = sqlList.join('\n');
          this.showSqlModal = true;
        } else {
          delete this.options.data[0].data;
          window.luckysheet.updataSheet({
            data: this.options.data
          });
          this.handleEmptyUpdate();
          Modal.info({
            title: this.$t('ti-shi'),
            content: this.$t('biao-shu-ju-wei-jin-hang-xiu-gai')
          });
        }
      }
    },
    async handleGetTableDataTotal() {
      this.setLoading(true);
      const res = await this.$services.dmEditorDataFetchCount({
        data: {
          ...this.generateDatasourceParams(),
          condition: this.tab.condition
        }
      });
      this.setLoading(false);
      if (res.success) {
        this.tab.total = res.data;
      }
    },
    generateDatasourceParams() {
      const { node, selectedTable, targetType } = this.tab;
      const data = {
        levels: this.browseGenLevelsData(node),
        targetName: selectedTable.title,
        targetType
      };

      return data;
    },
    disableCellEdit(rangeValue) {
      if (rangeValue) {
        if (rangeValue.custom && rangeValue.custom.column) {
          if (!rangeValue.custom.new && rangeValue.custom.column.updateReadOnly) {
            return true;
          }
          if (rangeValue.custom.new && rangeValue.custom.column.insertReadOnly) {
            return true;
          }
        }
      }

      return false;
    },
    handlePageSizeChange(event) {
      this.tab.pageSize = parseInt(event.key, 10);
      this.tab.selectedPageKeys = [event.key];
      this.handleGetTableData(-2, true);
    },
    async handleGetTableData(newOffset = -2, refresh = false, cancelSelect = true, force = false, mounted = false) {
      const getData = async () => {
        $('#luckysheet-postil-overshow').remove();
        this.handleEmptyUpdate();
        const sheetList = window.luckysheet.getLuckysheetfile();
        if (sheetList && sheetList.length && sheetList[0].celldata && sheetList[0].celldata.length && cancelSelect) {
          window.luckysheet.setRangeShow({ row: [0, 0], column: [0, 0] }, { show: false });
        }
        console.log('mounted', mounted, this.tab);
        if (mounted && this.tab.rawTableData?.columnList) {
          window.luckysheetData.activeKey = this.tab.key;
          window.luckysheetData[this.tab.key] = {
            addRows: [],
            column: this.tab.columnWithoutHidden
          };
          this.options = { ...this.tab.options, cellRightClickConfig: this.options.cellRightClickConfig, hook: this.options.hook };
        } else {
          this.setLoading(true);
          this.tab.options = this.options;
          const res = await this.$services.dmEditorDataFetchData({
            data: {
              ...this.generateDatasourceParams(),
              offset: newOffset > -2 ? newOffset : this.tab.offset,
              pageSize: this.tab.pageSize,
              condition: this.tab.condition,
              orderBy: this.tab.orderBy
            }
          });
          this.setLoading(false);
          if (res.success) {
            this.tab.rawTableData = deepClone(res.data);
            const { columnList, resultSet, resultSetMore, offset, pageSize, hasNext, isFirst, readOnly } = this.tab.rawTableData;
            this.options.allowEdit = !readOnly;
            const celldata = [];
            const columnlen = {};
            const dataVerification = {};
            const updateCellList = [];
            const whereKeyList = [];
            const spareWhereList = [];
            const columnWithoutHidden = columnList.filter((column) => column.column !== 'ROWNUM' && !column.hide);
            for (let i = 0; i < resultSet.length; i++) {
              updateCellList[i] = {};
            }
            this.tab.columnWithoutHidden = columnWithoutHidden;
            columnList.forEach((column) => {
              column.order = this.tab.currentSortConfig.column === column.column ? this.tab.currentSortConfig.sortBy : '';

              if (column.whereKey) {
                whereKeyList.push(column.column);
              }
              if (column.spareWhere) {
                spareWhereList.push(column.column);
              }
            });
            this.tab.whereKeyList = whereKeyList.length ? whereKeyList : spareWhereList;

            window.luckysheetData.activeKey = this.tab.key;
            window.luckysheetData[this.tab.key] = {
              addRows: [],
              column: columnWithoutHidden
            };

            columnWithoutHidden.forEach((column, columnIndex) => {
              columnlen[columnIndex] = column.column.length * 10 + 36;
              column.width = columnlen[columnIndex];
              resultSet.forEach((row, rowIndex) => {
                let psValue = column.columnType;
                if (row[column.column] && row[column.column].length > column.column.length) {
                  columnlen[columnIndex] = row[column.column].length * 10 + 36;
                  if (columnlen[columnIndex] > 500) {
                    columnlen[columnIndex] = 500;
                  }
                }
                if (column.columnType === 'SET') {
                  dataVerification[`${rowIndex}_${columnIndex}`] = {
                    type: 'dropdown',
                    type2: 'true',
                    value1: column.option.join(','),
                    // hintShow: true,
                    hintText: this.$t('qing-yong-dou-hao-ge-kai')
                  };
                } else if (column.columnType === 'ENUM') {
                  dataVerification[`${rowIndex}_${columnIndex}`] = {
                    type: 'dropdown',
                    type2: null,
                    value1: column.option.join(',')
                  };
                } else if (column.check && column.check.length) {
                  dataVerification[`${rowIndex}_${columnIndex}`] = {
                    type: 'expr',
                    type2: column.check
                  };
                }

                if (column.isPk) {
                  psValue = `${psValue}\n主键`;
                }
                if (column.autoincrement) {
                  psValue = `${psValue}\n自增`;
                }
                if (!column.isNullable) {
                  psValue = `${psValue}\n不能为NULL`;
                }

                const item = {
                  r: rowIndex,
                  c: columnIndex,
                  v: {
                    ct: {
                      fa: '@',
                      t: 's'
                    },
                    v: row[column.column],
                    m: row[column.column],
                    custom: {
                      v: row[column.column],
                      column,
                      more: resultSetMore[rowIndex][column.column]
                    }
                    // ps: {
                    //   isshow: false,
                    //   value: psValue
                    // }
                  }
                };
                item.v.custom.disableEdit = this.disableCellEdit(item.v);
                celldata.push(item);
              });
            });

            this.tab.hasNext = hasNext;
            this.tab.isFirst = isFirst;
            this.tab.offset = offset;
            this.tab.pageSize = pageSize;
            this.tab.updateCellList = updateCellList;
            this.tab.columnlen = columnlen;

            this.options.data[0].celldata = celldata;
            this.options.data[0].dataVerification = dataVerification;
            this.options.data[0].row = resultSet.length;
            this.options.data[0].column = columnWithoutHidden.length;

            delete this.options.data[0].data;
          }
        }

        if (this.tab.rawTableData?.resultSet.length) {
          if (refresh || !window.luckysheet.getLuckysheetfile()) {
            this.options.data[0].config.columnlen = this.tab.columnlen;
            window.luckysheet.destroy();
            window.luckysheet.create(this.options);
          } else {
            const scrollLeft = $('#luckysheet-cell-main').scrollLeft();
            window.luckysheet.updataSheet({
              data: this.options.data,
              success: () => {
                setTimeout(() => {
                  window.luckysheet.scroll({ scrollLeft });
                });
              }
            });
          }
        } else {
          window.luckysheet.destroy();
        }
      };

      if (this.isEditing && !force && !mounted) {
        Modal.confirm({
          title: this.$t('jing-gao'),
          content: this.$t('dang-qian-ye-mian-you-wei-ti-jiao-de-xiu-gai-dian-ji-que-ding-jiang-huo-qu-xin-shu-ju-bing-diu-shi-xiu-gai'),
          onOk: async () => {
            await getData();
          }
        });
      } else {
        await getData();
      }
    },
    handleRollbackCell() {
      const rangeList = window.luckysheet.getRange();
      rangeList.forEach((range) => {
        for (let rowIndex = range.row[0]; rowIndex <= range.row[1]; rowIndex++) {
          const isNew = this.tab.addRows.includes(rowIndex);
          const isDelete = this.tab.deleteRows.includes(rowIndex);

          if (!isNew && !isDelete) {
            for (let columnIndex = range.column[0]; columnIndex <= range.column[1]; columnIndex++) {
              const isRefresh = rowIndex === range.row[1] && columnIndex === range.column[1];
              const cell = window.luckysheet.getSheetData()[rowIndex][columnIndex];
              window.luckysheet.setCellValue(
                rowIndex,
                columnIndex,
                {
                  v: cell.custom.v,
                  m: cell.custom.v,
                  bg: BG_COLOR.CUSTOM
                },
                { isRefresh }
              );
            }
          }

          if (isNew && !isDelete) {
            this.handleDeleteRow([{ row: [rowIndex, rowIndex] }]);
          }

          if (isDelete && !isNew) {
            this.handleRollbackDeleteRow([{ row: [rowIndex, rowIndex] }]);
          }
        }
      });
    },
    handleRightMenuClick(type, clickEvent, event, params) {
      const rangeList = window.luckysheet.getRange();
      switch (type) {
        case CELL_RIGHT_MENU.COPY_COLUMN_NAME.id:
          this.copyText(this.tab.rawTableData.columnList?.[params.columnIndex]?.column);
          break;
        case CELL_RIGHT_MENU.SET_NULL.id:
          rangeList.forEach((range) => {
            const { row, column } = range;
            for (let rowIndex = row[0]; rowIndex <= row[1]; rowIndex++) {
              for (let columnIndex = column[0]; columnIndex <= column[1]; columnIndex++) {
                const isRefresh = rowIndex === row[1] && columnIndex === column[1];
                const cell = window.luckysheet.getSheetData()[rowIndex][columnIndex];
                let bg = cell.bg;
                if (!cell.custom.new && !cell.custom.delete) {
                  bg = Object.is(cell.custom.v, null) ? '#fff' : 'yellow';
                }
                window.luckysheet.setCellValue(rowIndex, columnIndex, { v: null, m: null, bg }, { isRefresh });
              }
            }
          });
          break;
        case CELL_RIGHT_MENU.SET_EMPTY_STRING.id:
          rangeList.forEach((range) => {
            const { row, column } = range;
            for (let rowIndex = row[0]; rowIndex <= row[1]; rowIndex++) {
              for (let columnIndex = column[0]; columnIndex <= column[1]; columnIndex++) {
                const isRefresh = rowIndex === row[1] && columnIndex === column[1];
                const cell = window.luckysheet.getSheetData()[rowIndex][columnIndex];
                let bg = cell.bg;
                if (!cell.custom.new && !cell.custom.delete) {
                  bg = Object.is(cell.custom.v, '') ? '#fff' : 'yellow';
                }
                window.luckysheet.setCellValue(rowIndex, columnIndex, { v: '', m: '', bg }, { isRefresh });
              }
            }
          });
          break;
        case CELL_RIGHT_MENU.EXPORT_DATA_CSV.id:
          this.handleExport('csvSelectedData');
          break;
        case CELL_RIGHT_MENU.EXPORT_ROW_CSV.id:
          this.handleExport('csvSelectedRows');
          break;
        case CELL_RIGHT_MENU.EXPORT_PAGE_CSV:
          this.handleExport('csvCurrentPage');
          break;
        case CELL_RIGHT_MENU.ROLLBACK_DATA.id:
          this.handleRollbackCell();
          break;
        case CELL_RIGHT_MENU.ADD_ROW.id:
          this.handleAddRow(params);
          break;
        case CELL_RIGHT_MENU.DELETE_ROW.id:
          this.handleDeleteRow();
          break;
        // case CELL_RIGHT_MENU.ROLLBACK_DELETE_ROW.id:
        //   this.handleRollbackDeleteRow();
        //   break;
        case CELL_RIGHT_MENU.COPY_ROW.id:
          this.handleAddRow(params, 'copy');
          break;
        default:
          break;
      }
    },
    handleCellMousedown(cell, position) {
      console.log('cell mouse down', cell, position);

      if (!cell) {
        window.luckysheet.setRangeShow({ row: [0, 0], column: [0, 0] }, { show: false });
      }
    },
    handleSheetMouseUp(cell, position, sheet, moveState) {
      if (moveState.colsSelectedStatus && !moveState.rightClick && moveState.mouse[0] > position.end_c - 19 && moveState.mouse[0] < position.end_c) {
        let order = window.luckysheetData[window.luckysheetData.activeKey].column[position.c].order;
        if (order === '') {
          order = 'asc';
        } else if (order === 'asc') {
          order = 'desc';
        } else if (order === 'desc') {
          order = '';
        }

        const column = window.luckysheetData[window.luckysheetData.activeKey].column[position.c];
        window.luckysheetData[window.luckysheetData.activeKey].column[position.c].order = order;
        this.tab.orderBy = order ? `${column.column} ${order}` : '';
        this.tab.currentSortConfig = {
          column: column.column,
          sortBy: order
        };

        this.handleGetTableData(-2, false, false);
      }
    },
    handleEmptyUpdate() {
      this.tab.addRows = [];
      this.tab.deleteRows = [];
      this.tab.rawTableData.resultSet.forEach((row, rowIndex) => {
        this.tab.updateCellList[rowIndex] = {};
      });
    },
    handleAddRow(params = {}, type = 'new') {
      if (
        this.tab.rawTableData.resultSet.length ||
        (window.luckysheet.getLuckysheetfile() && window.luckysheet.getLuckysheetfile()[0].celldata.length)
      ) {
        const currentSheet = window.luckysheet.getLuckysheetfile()[0];
        const rowIndex = currentSheet.data.length;
        this.tab.addRows.push(rowIndex);

        window.luckysheet.insertRow(rowIndex, { number: 1 });

        this.tab.columnWithoutHidden.forEach((column, columnIndex) => {
          const isRefresh = columnIndex === this.tab.columnWithoutHidden.length - 1;
          if (column.columnType === 'SET') {
            this.options.data[0].dataVerification[`${rowIndex}_${columnIndex}`] = {
              type: 'dropdown',
              type2: 'true',
              value1: column.option.join(','),
              hintText: this.$t('qing-yong-dou-hao-ge-kai')
            };
          } else if (column.columnType === 'ENUM') {
            this.options.data[0].dataVerification[`${rowIndex}_${columnIndex}`] = {
              type: 'dropdown',
              type2: null,
              value1: column.option.join(',')
            };
          } else if (column.check && column.check.length) {
            this.options.data[0].dataVerification[`${rowIndex}_${columnIndex}`] = {
              type: 'expr',
              type2: column.check
            };
          }
          let v;
          if (type === 'copy') {
            v = window.luckysheet.getCellValue(params.rowIndex, columnIndex, { type: 'm' });
          } else {
            v = null;
          }

          window.luckysheet.setCellValue(
            rowIndex,
            columnIndex,
            {
              v,
              m: v,
              ct: {
                fa: '@',
                t: 's'
              },
              bg: BG_COLOR.ADD,
              custom: {
                column,
                new: true
              }
            },
            { isRefresh }
          );
        });
      } else {
        this.tab.columnWithoutHidden.forEach((column, columnIndex) => {
          if (column.columnType === 'SET') {
            this.options.data[0].dataVerification[`0_${columnIndex}`] = {
              type: 'dropdown',
              type2: 'true',
              value1: column.option.join(','),
              hintText: this.$t('qing-yong-dou-hao-ge-kai')
            };
          } else if (column.columnType === 'ENUM') {
            this.options.data[0].dataVerification[`0_${columnIndex}`] = {
              type: 'dropdown',
              type2: null,
              value1: column.option.join(',')
            };
          } else if (column.check && column.check.length) {
            this.options.data[0].dataVerification[`0_${columnIndex}`] = {
              type: 'expr',
              type2: column.check
            };
          }

          const cell = {
            r: 0,
            c: columnIndex,
            v: {
              v: null,
              m: null,
              ct: {
                fa: '@',
                t: 's'
              },
              bg: BG_COLOR.ADD,
              custom: {
                column,
                new: true
              }
            }
          };
          this.options.data[0].celldata.push(cell);
        });

        this.options.data[0].config.columnlen = this.tab.columnlen;
        this.tab.addRows.push(0);
        window.luckysheet.destroy();
        window.luckysheet.create(this.options);
      }
    },
    handleDeleteRow(list = []) {
      const rangeList = list.length ? list : window.luckysheet.getRange();
      rangeList.forEach((range) => {
        for (let rowIndex = range.row[0]; rowIndex <= range.row[1]; rowIndex++) {
          const isNew = this.tab.addRows.includes(rowIndex);
          this.tab.deleteRows.push(rowIndex);

          this.tab.columnWithoutHidden.forEach((column, columnIndex) => {
            console.log(column.column, columnIndex);
            const isRefresh = columnIndex === this.tab.columnWithoutHidden.length - 1;
            const cell = window.luckysheet.getLuckysheetfile()[0].data[rowIndex][columnIndex];
            window.luckysheet.setCellValue(
              rowIndex,
              columnIndex,
              {
                bg: isNew ? BG_COLOR.ADD_DELETE : BG_COLOR.DELETE,
                custom: { ...cell.custom, delete: true }
              },
              { isRefresh }
            );
          });
        }
      });
    },
    handleRollbackDeleteRow(list = []) {
      const rangeList = list.length ? list : window.luckysheet.getRange();
      rangeList.forEach((range) => {
        for (let rowIndex = range.row[0]; rowIndex <= range.row[1]; rowIndex++) {
          const isNew = this.tab.addRows.includes(rowIndex);
          const isDelete = this.tab.deleteRows.includes(rowIndex);
          this.tab.deleteRows = this.tab.deleteRows.filter((rowNum) => rowNum !== rowIndex);

          if (isDelete) {
            this.tab.columnWithoutHidden.forEach((column, columnIndex) => {
              const isRefresh = columnIndex === this.tab.columnWithoutHidden.length - 1;
              const cell = window.luckysheet.getLuckysheetfile()[0].data[rowIndex][columnIndex];
              const bg = isNew ? BG_COLOR.ADD : cell.custom.update ? BG_COLOR.UPDATE : BG_COLOR.CUSTOM;
              console.log(cell.custom.update, bg);
              window.luckysheet.setCellValue(
                rowIndex,
                columnIndex,
                {
                  bg,
                  custom: { ...cell.custom, delete: false }
                },
                { isRefresh }
              );
            });
          }
        }
      });
    }
  },
  beforeUnmount() {
    window.luckysheet.destroy();
  }
};
</script>

<template>
  <div class="lucky-sheet-data-view">
    <div class="header">
      <div class="pagination">
        <a-button class="op" @click="handleGetTableData(0)" :disabled="tab.isFirst || loading" size="small">
          <template #icon>
            <VerticalRightOutlined />
          </template>
        </a-button>
        <a-button class="op" :disabled="tab.isFirst || loading" size="small" @click="handleGetTableData(tab.offset - tab.pageSize)">
          <template #icon>
            <LeftOutlined />
          </template>
        </a-button>
        <a-dropdown class="op" :disabled="loading">
          <template #overlay>
            <a-menu size="small" v-model:selectedKeys="tab.selectedPageKeys" @click="handlePageSizeChange">
              <a-menu-item key="1">1</a-menu-item>
              <a-menu-item key="20">20</a-menu-item>
              <a-menu-item key="50">50</a-menu-item>
              <a-menu-item key="100">100</a-menu-item>
              <a-menu-item key="200">200</a-menu-item>
              <a-menu-item key="500">500</a-menu-item>
              <a-menu-item key="1000">1000</a-menu-item>
              <a-menu-item key="2000">2000</a-menu-item>
              <a-menu-item key="5000">5000</a-menu-item>
              <a-menu-item key="10000">10000</a-menu-item>
              <!--            <a-menu-item key="-1">全部</a-menu-item>-->
            </a-menu>
          </template>
          <a-button size="small" class="op" :disabled="loading">{{ tab.offset + 1 }}-{{ tab.offset + tab.rawTableData.resultSet.length }}</a-button>
        </a-dropdown>
        of
        <a-button class="op" size="small" @click="handleGetTableDataTotal" :disabled="loading">
          {{ tab.total || `+${tab.offset + tab.rawTableData.resultSet.length}` }}
        </a-button>
        <a-button class="op" :disabled="!tab.hasNext || loading" size="small" @click="handleGetTableData(tab.offset + tab.pageSize)">
          <template #icon>
            <RightOutlined />
          </template>
        </a-button>
        <a-button class="op" :disabled="!tab.hasNext || loading" size="small" @click="handleGetTableData(-1)">
          <template #icon>
            <VerticalLeftOutlined />
          </template>
        </a-button>
        <a-button size="small" @click="handleGetTableData(tab.offset)" class="op" :disabled="loading">
          <template #icon>
            <ReloadOutlined />
          </template>
        </a-button>
        <a-dropdown>
          <template #overlay>
            <a-menu :disabled="loading">
              <a-menu-item key="csvSelectedData" @click="handleExport('csvSelectedData')">
                {{ $t('dao-chu-suo-xuan-shu-ju-wei-csv') }}
              </a-menu-item>
              <a-menu-item key="csvSelectedRows" @click="handleExport('csvSelectedRows')">
                {{ $t('dao-chu-suo-xuan-shu-ju-suo-zai-hang-wei-csv') }}
              </a-menu-item>
              <a-menu-item key="csvCurrentPage" @click="handleExport('csvCurrentPage')">
                {{ $t('dao-chu-dang-qian-ye-quan-bu-shu-ju-wei-csv') }}
              </a-menu-item>
            </a-menu>
          </template>
          <a-button class="op" size="small">
            <template ##icon>
              <DownloadOutlined />
            </template>
          </a-button>
        </a-dropdown>
        <a-button class="op" size="small" @click="handleAddRow" :disabled="loading">
          <template #icon>
            <PlusOutlined />
          </template>
        </a-button>
        <a-button class="op" size="small" @click="handleDeleteRow()" :disabled="loading">
          <template #icon>
            <MinusOutlined />
          </template>
        </a-button>
        <a-button type="primary" size="small" @click="handleSubmit" :disabled="loading">
          {{ $t('ti-jiao') }}
        </a-button>
      </div>
      <div class="where">
        <a-input v-model:value="tab.condition" size="small" @pressEnter="handleGetTableData(tab.offset)" :addon-before="'WHERE'" :allowClear="true" />
      </div>
      <div class="order">
        <a-input
          v-model:value="tab.orderBy"
          size="small"
          @pressEnter="handleGetTableData(tab.offset)"
          :addon-before="'ORDER BY'"
          :allowClear="true"
        />
      </div>
      <a-button type="primary" size="small" @click="handleGetTableData(tab.offset)" :disabled="loading" style="margin-left: 3px">
        {{ $t('cha-xun') }}
      </a-button>
    </div>
    <div class="table-container">
      <div id="luckysheet" v-show="options.data[0].celldata.length"></div>
      <div class="column-container" v-show="!options.data[0].celldata.length">
        <div v-for="column in tab.columnWithoutHidden" :key="column.column" class="column-name" :style="`min-width: ${column.width}px`">
          {{ column.column }}
        </div>
      </div>
    </div>
    <CCModal v-model="showSqlModal" :title="$t('sql-yu-ju')" :width="800" :mask-closable="false">
      <ReadOnlyEditor :text="sqlString" :max-height="500" />
      <template #footer>
        <a-button @click="copyText(sqlString)">{{ $t('fu-zhi-sql-yu-ju') }}</a-button>
        <a-button type="primary" @click="handleRun" :loading="executeSQLLoading">
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
      </template>
    </CCModal>
    <CCModal
      v-model="showExecuteInfoModal"
      :title="$t('zhi-hang-xin-xi')"
      :width="800"
      :mask-closable="false"
      :closable="false"
      @on-cancel="handleCloseExecuteInfoModal"
    >
      <div style="height: 500px; overflow: auto">
        <div v-for="(info, index) in executeInfo" :key="index" class="result-info">
          <div class="first">
            <div :class="`level ${info.success ? 'Info' : 'Error'}`">{{ info.database }}></div>
            <div class="sql">{{ info.queryBody }}</div>
          </div>
          <div class="second">
            <div class="time">[{{ dayjs(info.startTimestamp).format('YYYY-MM-DD HH:mm:ss') }}]</div>
            <div :class="`message ${info.success ? '' : 'message-error'}`">
              {{ info.message }}
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <a-button @click="handleCloseExecuteInfoModal" :loading="executeSQLLoading">
          {{ executeSQLLoading ? $t('zheng-zai-zhi-hang') : $t('guan-bi') }}
        </a-button>
      </template>
    </CCModal>
  </div>
</template>

<style scoped lang="less">
.result-info {
  margin-bottom: 5px;
  font-weight: bold;

  .first,
  .second {
    display: flex;
  }

  .level {
    padding: 0 5px;
    border-radius: 3px;
    height: 20px;
    margin-right: 3px;
    color: #fff;

    &.Info {
      background: #19be6b;
    }

    &.Warn {
      background: #f90;
    }

    &.Error {
      background: #ed4014;
    }
  }

  .time {
    margin-right: 5px;
    color: #aaa;
  }

  .message {
    flex: 1;
  }
}
.lucky-sheet-data-view {
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;

  .header {
    height: 40px;
    display: flex;
    align-items: center;
    padding: 0 10px;
    border-bottom: 1px solid #ccc;

    .op {
      margin-right: 3px;
      border: none;
      border-radius: 2px;
    }

    .where,
    .order {
      flex: 1;
      display: flex;
      align-items: center;
      margin-left: 10px;
    }
  }

  .table-container {
    flex: 1;
    width: 100%;
    height: 100%;
    min-height: 0;

    #luckysheet {
      top: 40px;
      position: absolute;
      width: 100%;
      height: calc(100% - 40px);
    }

    .column-container {
      display: flex;
      overflow: scroll;
      padding-bottom: 10px;
    }

    .column-name {
      background: #eee;
      height: 18px;
      line-height: 18px;
      border: 1px solid #ccc;
    }
  }
}

:deep(.luckysheet-freezebar-horizontal-drop-bar) {
  margin-left: 0 !important;
}

:deep(.luckysheet-work-area) {
  display: none;
}

:deep(.luckysheet-copy-btn) {
  display: none;
}

:deep(.luckysheet-rows-h) {
  top: -11px;
}

:deep(#luckysheet-dataVerification-dropdown-List .dropdown-List-item.multi.checked) {
  background: yellow;
}

:deep(#luckysheet-dataVerification-dropdown-List .dropdown-List-item.single.checked) {
  background: yellow;
}

:deep(.luckysheet-stat-area) {
  display: none !important;
}

:deep(.luckysheet-cell-main) {
  background: #fff;
}

:deep(.luckysheet-cs-draghandle) {
  display: none !important;
}

:deep(.luckysheet-cs-fillhandle) {
  display: none !important;
}

:deep(.luckysheet-scrollbar-ltr) {
  z-index: 1000 !important;
}
</style>
