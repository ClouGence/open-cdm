<script>
import { h } from 'vue';
import { PlusOutlined, MinusOutlined } from '@ant-design/icons-vue';
import { Modal } from 'ant-design-vue';
import copyMixin from '@/mixins/copyMixin';
import exportMixin from '@/mixins/exportMixin';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import CCModal from '@/components/ui/CCModal.vue';
import { cloneDeep as deepClone } from '@/utils/lodash';

const BG_COLOR = {
  ADD: 'rgb(236, 255, 220)',
  DELETE: 'rgb(250, 128, 114)',
  ADD_DELETE: 'orange',
  UPDATE: 'yellow',
  CUSTOM: '#fff'
};

const CELL_RIGHT_MENU = {
  COPY_COLUMN_NAME: {
    id: 'luckysheet-custom-copy-column-name',
    title: '复制列名'
  },
  COPY_ROW: {
    id: 'luckysheet-custom-copy-row',
    title: '复制选中行'
  },
  DIVIDER1: {
    title: 'divider'
  },
  SET_NULL: {
    id: 'luckysheet-custom-set-null',
    title: '设置为NULL'
  },
  SET_EMPTY_STRING: {
    id: 'luckysheet-custom-set-empty_string',
    title: '设置为空字符串'
  },
  ROLLBACK_DATA: {
    id: 'luckysheet-custom-rollback-data',
    title: '撤销修改'
  },
  DIVIDER2: {
    title: 'divider'
  },
  ADD_ROW: {
    id: 'luckysheet-custom-add-row',
    title: '新增行'
  },
  DELETE_ROW: {
    id: 'luckysheet-custom-delete-row',
    title: '删除行'
  },
  DIVIDER3: {
    title: 'divider'
  },
  EXPORT_DATA_CSV: {
    id: 'luckysheet-custom-export-data-csv',
    title: '导出选中单元格CSV格式'
  },
  EXPORT_ROW_CSV: {
    id: 'luckysheet-custom-export-row-csv',
    title: '导出选中行CSV格式'
  },
  EXPORT_PAGE_CSV: {
    id: 'luckysheet-custom-export-page-csv',
    title: '导出当前页CSV格式'
  }
};

export default {
  name: 'ResultEditView',
  components: {
    CCModal,
    ReadOnlyEditor,
    PlusOutlined,
    MinusOutlined
  },
  mixins: [copyMixin, exportMixin],
  props: {
    resultData: {
      type: Object,
      required: true
    },
    columnMeta: {
      type: Array,
      required: true
    },
    levels: {
      type: Array,
      required: true
    },
    targetName: {
      type: String,
      required: true
    },
    targetType: {
      type: String,
      default: 'TABLE'
    }
  },
  emits: ['saved', 'cancel'],
  data() {
    return {
      rawTableData: { resultSet: [], resultSetMore: [], columnList: [] },
      addRows: [],
      deleteRows: [],
      updateCellList: [],
      whereKeyList: [],
      columnWithoutHidden: [],
      columnlen: {},

      createList: {},
      updateList: {},
      deleteList: {},
      renewData: {},
      sqls: [],
      sqlString: '',
      executeInfo: [],
      executeSQLLoading: false,
      refreshAfterExecute: false,
      showSqlModal: false,
      showExecuteInfoModal: false,
      loading: false,

      options: {
        container: 'luckysheet-result-edit',
        lang: 'zh',
        loading: {
          image: '',
          text: '',
          customClass: 'result-edit-luckysheet-loading-hidden'
        },
        allowCopy: false,
        showtoolbar: false,
        showtoolbarConfig: false,
        showinfobar: false,
        showsheetbar: false,
        showsheetbarConfig: false,
        enableAddBackTop: false,
        enableAddRow: false,
        cellRightClickConfig: {
          copy: true,
          copyAs: false,
          paste: false,
          insertRow: false,
          insertColumn: false,
          deleteRow: false,
          deleteColumn: false,
          deleteCell: false,
          hideRow: false,
          hideColumn: false,
          rowHeight: false,
          columnWidth: false,
          clear: false,
          matrix: false,
          sort: false,
          filter: false,
          chart: false,
          image: false,
          link: false,
          data: false,
          cellFormat: false,
          customs: []
        },
        hook: {
          updated: (operate) => {
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
                      this.updateCellList[rowIndex][cell.custom.column.column] = true;
                    } else {
                      delete this.updateCellList[rowIndex][cell.custom.column.column];
                    }
                  }
                });
              });
            }
          },
          cellUpdated: (r, c, oldValue, newValue) => {
            if (newValue && newValue.custom && !newValue.custom.new && !newValue.custom.delete) {
              if (newValue.custom.update) {
                this.updateCellList[r][newValue.custom.column.column] = true;
              } else {
                delete this.updateCellList[r][newValue.custom.column.column];
              }
            }
          },
          cellUpdateBefore: () => {},
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
              ctx.fillText('文本截断', start_c + width - 52, start_r + height / 1.6);
            }
          },
          workbookCreateAfter: () => {
            this.loading = false;
            window.luckysheet.setRangeShow({ row: [0, 0], column: [0, 0] }, { show: false });
          },
          scroll: () => {
            window.luckysheet.exitEditMode();
          },
          columnTitleCellRenderAfter: (_, position, ctx) => {
            const { c, left, height, width } = position;
            ctx.clearRect(left, 0, width - 1, height - 1);
            const column = this.columnWithoutHidden[c];
            if (!column) return;
            let offset = 0;
            if (column.isPk) {
              const pkImg = window.luckysheetData && window.luckysheetData.storeUserImage && window.luckysheetData.storeUserImage.pkImg;
              if (pkImg) {
                ctx.drawImage(pkImg, left, 3, 12, 12);
                offset += 12;
              }
            }
            if (column.isUk) {
              const ukImg = window.luckysheetData && window.luckysheetData.storeUserImage && window.luckysheetData.storeUserImage.ukImg;
              if (ukImg) {
                ctx.drawImage(ukImg, left + offset, 3, 12, 12);
                offset += 12;
              }
            }
            ctx.fillText(`${column.isNullable ? '' : '*'}${column.column}`, left + offset, height / 2);
          },
          cellMousedown: () => {},
          sheetMousemove: (cell) => {
            if (cell && cell.custom && cell.custom.disableEdit) {
              const sheetTable = document.querySelector('.luckysheet-cell-sheettable');
              if (sheetTable) {
                sheetTable.style.cursor = 'not-allowed';
              }
            } else {
              const sheetTable = document.querySelector('.luckysheet-cell-sheettable');
              if (sheetTable) {
                sheetTable.style.cursor = 'default';
              }
            }
          }
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
  computed: {
    isEditing() {
      const hasAddOrDeleteRow = this.addRows.length || this.deleteRows.length;
      if (hasAddOrDeleteRow) {
        return true;
      }
      for (let rowIndex = 0; rowIndex < this.updateCellList.length; rowIndex++) {
        const row = this.updateCellList[rowIndex];
        const rowKeys = Object.keys(row);
        if (rowKeys.length) {
          return true;
        }
      }
      return false;
    }
  },
  mounted() {
    Object.keys(CELL_RIGHT_MENU).forEach((key) => {
      const item = CELL_RIGHT_MENU[key];
      if (!item.id) {
        return;
      }
      this.options.cellRightClickConfig.customs.push({
        title: item.title,
        id: item.id,
        onClick: (clickEvent, event, params) => this.handleRightMenuClick(item.id, clickEvent, event, params)
      });
    });
    this.initEditData();
  },
  beforeUnmount() {
    this.showSqlModal = false;
    this.showExecuteInfoModal = false;
    this.destroyLuckysheet();
  },
  methods: {
    destroyLuckysheet() {
      try {
        const container = document.getElementById('luckysheet-result-edit');
        if (!container) {
          return;
        }
        if (window.luckysheet?.getLuckysheetfile?.()) {
          window.luckysheet.destroy();
        }
        container.innerHTML = '';
      } catch (e) {
        // ignore destroy errors on unmount
      }
    },
    resolveResultSet() {
      const { dataArr, showData, page = 1 } = this.resultData;
      // Always prefer the currently displayed page; never flatten all pages (can freeze Luckysheet).
      if (Array.isArray(showData)) {
        return showData;
      }
      if (Array.isArray(dataArr) && dataArr.length) {
        const pageData = dataArr[page - 1];
        if (Array.isArray(pageData)) {
          return pageData;
        }
        if (!Array.isArray(dataArr[0])) {
          return dataArr;
        }
      }
      return [];
    },
    getRowCellValue(row, columnName) {
      if (!row || !columnName) {
        return undefined;
      }
      if (Object.prototype.hasOwnProperty.call(row, columnName)) {
        return row[columnName];
      }
      const matchedKey = Object.keys(row).find((key) => key.toLowerCase() === columnName.toLowerCase());
      if (matchedKey) {
        return row[matchedKey];
      }
      return undefined;
    },
    formatCellDisplay(value) {
      if (value === null || value === undefined) {
        return null;
      }
      if (typeof value === 'string') {
        return value;
      }
      if (typeof value === 'number' || typeof value === 'boolean' || typeof value === 'bigint') {
        return String(value);
      }
      try {
        return JSON.stringify(value);
      } catch (e) {
        return String(value);
      }
    },
    mountLuckysheet() {
      this.$nextTick(() => {
        requestAnimationFrame(() => {
          const container = document.getElementById('luckysheet-result-edit');
          if (!container) {
            this.loading = false;
            Modal.error({
              title: this.$t('ti-shi'),
              content: '编辑面板容器未就绪，请重试'
            });
            this.$emit('cancel');
            return;
          }

          try {
            $('#luckysheet-postil-overshow').remove();
            if (window.luckysheet.getLuckysheetfile()) {
              window.luckysheet.destroy();
            }
            window.luckysheet.create(this.options);
          } catch (e) {
            console.error('Failed to create luckysheet:', e);
            this.loading = false;
            Modal.error({
              title: this.$t('ti-shi'),
              content: '编辑面板加载失败，请重试'
            });
            this.$emit('cancel');
          }
        });
      });
    },
    initEditData() {
      this.loading = true;
      const resultSet = deepClone(this.resolveResultSet()).filter((row) => row);
      const resultSetMore = resultSet.map(() => ({}));

      const columnWithoutHidden = this.columnMeta.filter((c) => c.column !== 'ROWNUM' && !c.hide).map((c) => ({ ...c }));
      const resultColumnList = this.resultData.columnList;
      let editableColumns = columnWithoutHidden;
      if (Array.isArray(resultColumnList) && resultColumnList.length) {
        const resultColumnSet = new Set(resultColumnList.map((name) => String(name).toLowerCase()));
        const matchedColumns = columnWithoutHidden.filter((column) => {
          if (column.isPk || column.whereKey || column.spareWhere) {
            return true;
          }
          return resultColumnSet.has(column.column.toLowerCase());
        });
        if (matchedColumns.length) {
          editableColumns = matchedColumns;
        }
      }
      if (!editableColumns.length) {
        this.loading = false;
        Modal.error({
          title: this.$t('ti-shi'),
          content: '无可编辑列'
        });
        this.$emit('cancel');
        return;
      }

      const whereKeyList = [];
      const spareWhereList = [];
      columnWithoutHidden.forEach((column) => {
        if (column.whereKey || column.isPk) {
          whereKeyList.push(column.column);
        }
        if (column.spareWhere) {
          spareWhereList.push(column.column);
        }
      });

      this.whereKeyList = whereKeyList.length ? whereKeyList : spareWhereList;
      this.columnWithoutHidden = editableColumns;

      const updateCellList = [];
      for (let i = 0; i < resultSet.length; i++) {
        updateCellList[i] = {};
      }
      this.updateCellList = updateCellList;

      this.rawTableData = { resultSet, resultSetMore, columnList: editableColumns };

      const celldata = [];
      const columnlen = {};
      const dataVerification = {};

      editableColumns.forEach((column, columnIndex) => {
        columnlen[columnIndex] = column.column.length * 10 + 36;
        column.width = columnlen[columnIndex];
        resultSet.forEach((row, rowIndex) => {
          const cellValue = this.formatCellDisplay(this.getRowCellValue(row, column.column));
          if (cellValue && cellValue.length > column.column.length) {
            columnlen[columnIndex] = cellValue.length * 10 + 36;
            if (columnlen[columnIndex] > 500) {
              columnlen[columnIndex] = 500;
            }
          }
          if (column.columnType === 'SET') {
            dataVerification[`${rowIndex}_${columnIndex}`] = {
              type: 'dropdown',
              type2: 'true',
              value1: (column.option || []).join(','),
              hintText: '请用逗号隔开'
            };
          } else if (column.columnType === 'ENUM') {
            dataVerification[`${rowIndex}_${columnIndex}`] = {
              type: 'dropdown',
              type2: null,
              value1: (column.option || []).join(',')
            };
          }

          const item = {
            r: rowIndex,
            c: columnIndex,
            v: {
              ct: {
                fa: '@',
                t: 's'
              },
              v: cellValue,
              m: cellValue,
              custom: {
                v: cellValue,
                column,
                more: (resultSetMore[rowIndex] && resultSetMore[rowIndex][column.column]) || 0
              }
            }
          };
          item.v.custom.disableEdit = this.disableCellEdit(item.v);
          celldata.push(item);
        });
      });

      this.columnlen = columnlen;
      const sheet = this.options.data[0];
      sheet.name = 'data';
      sheet.celldata = celldata;
      sheet.dataVerification = dataVerification;
      sheet.row = Math.max(resultSet.length, 1);
      sheet.column = Math.max(editableColumns.length, 1);
      sheet.config = {
        columnlen,
        authority: {
          allowRangeList: [{}]
        }
      };
      delete sheet.data;

      this.mountLuckysheet();
    },

    disableCellEdit(cellValue) {
      if (cellValue && cellValue.custom && cellValue.custom.column) {
        if (!cellValue.custom.new && cellValue.custom.column.updateReadOnly) {
          return true;
        }
        if (cellValue.custom.new && cellValue.custom.column.insertReadOnly) {
          return true;
        }
      }
      return false;
    },

    generateDatasourceParams() {
      return {
        levels: this.levels,
        targetName: this.targetName,
        targetType: this.targetType
      };
    },

    handleEmptyUpdate() {
      this.addRows = [];
      this.deleteRows = [];
      this.rawTableData.resultSet.forEach((row, rowIndex) => {
        this.updateCellList[rowIndex] = {};
      });
    },

    handleAddRow() {
      const currentSheet = window.luckysheet.getLuckysheetfile()[0];
      if (!currentSheet || !currentSheet.data) return;
      const rowIndex = currentSheet.data.length;
      this.addRows.push(rowIndex);
      window.luckysheet.insertRow(rowIndex, { number: 1 });

      this.columnWithoutHidden.forEach((column, columnIndex) => {
        const isRefresh = columnIndex === this.columnWithoutHidden.length - 1;
        window.luckysheet.setCellValue(
          rowIndex,
          columnIndex,
          {
            v: null,
            m: null,
            ct: { fa: '@', t: 's' },
            bg: BG_COLOR.ADD,
            custom: {
              column,
              new: true
            }
          },
          { isRefresh }
        );
      });
    },

    handleDeleteRow(list = []) {
      const rangeList = list.length ? list : window.luckysheet.getRange();
      rangeList.forEach((range) => {
        for (let rowIndex = range.row[0]; rowIndex <= range.row[1]; rowIndex++) {
          const isNew = this.addRows.includes(rowIndex);
          this.deleteRows.push(rowIndex);
          this.columnWithoutHidden.forEach((column, columnIndex) => {
            const isRefresh = columnIndex === this.columnWithoutHidden.length - 1;
            const cell = window.luckysheet.getLuckysheetfile()[0].data[rowIndex][columnIndex];
            if (cell) {
              window.luckysheet.setCellValue(
                rowIndex,
                columnIndex,
                {
                  bg: isNew ? BG_COLOR.ADD_DELETE : BG_COLOR.DELETE,
                  custom: { ...cell.custom, delete: true }
                },
                { isRefresh }
              );
            }
          });
        }
      });
    },

    handleRollbackCell() {
      const rangeList = window.luckysheet.getRange();
      rangeList.forEach((range) => {
        for (let rowIndex = range.row[0]; rowIndex <= range.row[1]; rowIndex++) {
          const isNew = this.addRows.includes(rowIndex);
          const isDelete = this.deleteRows.includes(rowIndex);
          if (!isNew && !isDelete) {
            for (let columnIndex = range.column[0]; columnIndex <= range.column[1]; columnIndex++) {
              const isRefresh = rowIndex === range.row[1] && columnIndex === range.column[1];
              const cell = window.luckysheet.getSheetData()[rowIndex][columnIndex];
              if (cell && cell.custom) {
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

    handleRollbackDeleteRow(list = []) {
      const rangeList = list.length ? list : window.luckysheet.getRange();
      rangeList.forEach((range) => {
        for (let rowIndex = range.row[0]; rowIndex <= range.row[1]; rowIndex++) {
          const isNew = this.addRows.includes(rowIndex);
          const isDelete = this.deleteRows.includes(rowIndex);
          this.deleteRows = this.deleteRows.filter((rowNum) => rowNum !== rowIndex);
          if (isDelete) {
            this.columnWithoutHidden.forEach((column, columnIndex) => {
              const isRefresh = columnIndex === this.columnWithoutHidden.length - 1;
              const cell = window.luckysheet.getLuckysheetfile()[0].data[rowIndex][columnIndex];
              if (cell) {
                const bg = isNew ? BG_COLOR.ADD : cell.custom && cell.custom.update ? BG_COLOR.UPDATE : BG_COLOR.CUSTOM;
                window.luckysheet.setCellValue(
                  rowIndex,
                  columnIndex,
                  {
                    bg,
                    custom: { ...cell.custom, delete: false }
                  },
                  { isRefresh }
                );
              }
            });
          }
        }
      });
    },

    handleRightMenuClick(type, clickEvent, event, params) {
      const rangeList = window.luckysheet.getRange();
      switch (type) {
        case CELL_RIGHT_MENU.COPY_COLUMN_NAME.id:
          this.copyText(this.columnWithoutHidden[params.columnIndex]?.column);
          break;
        case CELL_RIGHT_MENU.SET_NULL.id:
          rangeList.forEach((range) => {
            const { row, column } = range;
            for (let rowIndex = row[0]; rowIndex <= row[1]; rowIndex++) {
              for (let columnIndex = column[0]; columnIndex <= column[1]; columnIndex++) {
                const isRefresh = rowIndex === row[1] && columnIndex === column[1];
                const cell = window.luckysheet.getSheetData()[rowIndex][columnIndex];
                let bg = cell && cell.bg;
                if (cell && cell.custom && !cell.custom.new && !cell.custom.delete) {
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
                let bg = cell && cell.bg;
                if (cell && cell.custom && !cell.custom.new && !cell.custom.delete) {
                  bg = Object.is(cell.custom.v, '') ? '#fff' : 'yellow';
                }
                window.luckysheet.setCellValue(rowIndex, columnIndex, { v: '', m: '', bg }, { isRefresh });
              }
            }
          });
          break;
        case CELL_RIGHT_MENU.ROLLBACK_DATA.id:
          this.handleRollbackCell();
          break;
        case CELL_RIGHT_MENU.ADD_ROW.id:
          this.handleAddRow();
          break;
        case CELL_RIGHT_MENU.DELETE_ROW.id:
          this.handleDeleteRow();
          break;
        case CELL_RIGHT_MENU.COPY_ROW.id:
          this.copyText(this.getSelectedRowText());
          break;
        case CELL_RIGHT_MENU.EXPORT_DATA_CSV.id:
          this.handleExport('csvSelectedData');
          break;
        case CELL_RIGHT_MENU.EXPORT_ROW_CSV.id:
          this.handleExport('csvSelectedRows');
          break;
        case CELL_RIGHT_MENU.EXPORT_PAGE_CSV.id:
          this.handleExport('csvCurrentPage');
          break;
        default:
          break;
      }
    },

    getSelectedRowText() {
      const rangeList = window.luckysheet.getRange();
      if (!rangeList.length) return '';
      const data = window.luckysheet.getSheetData();
      const lines = [];
      rangeList.forEach((range) => {
        for (let r = range.row[0]; r <= range.row[1]; r++) {
          const vals = [];
          for (let c = range.column[0]; c <= range.column[1]; c++) {
            if (data[r] && data[r][c]) {
              vals.push(data[r][c].v || '');
            }
          }
          lines.push(vals.join('\t'));
        }
      });
      return lines.join('\n');
    },

    buildWhereData(rowIndex) {
      const row = this.rawTableData.resultSet[rowIndex];
      const whereData = {};
      const sheetData = window.luckysheet.getLuckysheetfile()?.[0]?.data;

      this.whereKeyList.forEach((key) => {
        let value = this.getRowCellValue(row, key);
        if (value === undefined && sheetData && sheetData[rowIndex]) {
          const colIndex = this.columnWithoutHidden.findIndex((column) => column.column === key);
          if (colIndex >= 0) {
            const cell = sheetData[rowIndex][colIndex];
            if (cell && cell.custom) {
              value = cell.custom.v;
            } else if (cell) {
              value = cell.v;
            }
          }
        }
        whereData[key] = value;
      });
      return whereData;
    },

    hasWhereData(whereData) {
      return this.whereKeyList.every((key) => whereData[key] !== undefined);
    },

    hasPendingLocalResults() {
      return Object.keys(this.createList).length > 0 || Object.keys(this.updateList).length > 0 || Object.keys(this.deleteList).length > 0;
    },

    async handleSubmit() {
      try {
        window.luckysheet.exitEditMode();
        window.luckysheet.setRangeShow({ row: [0, 0], column: [0, 0] }, { show: false });
        const copyEle = document.querySelector('#luckysheet-selection-copy>.luckysheet-selection-copy');
        if (copyEle) {
          copyEle.style.display = 'none';
        }
        const luckysheetFile = window.luckysheet.getLuckysheetfile();
        if (!luckysheetFile || !luckysheetFile.length) return;
        const { data } = luckysheetFile[0];
        const dataParamList = [];
        const renewData = {};

        // create
        for (let rowIndex = this.rawTableData.resultSet.length; rowIndex < data.length; rowIndex++) {
          if (this.addRows.includes(rowIndex) && !this.deleteRows.includes(rowIndex)) {
            const row = {};
            const rowData = data[rowIndex];
            if (Array.isArray(rowData)) {
              rowData.forEach((col) => {
                if (col && col.custom) {
                  row[col.custom.column.column] = col.v;
                }
              });
            } else if (rowData) {
              Object.values(rowData).forEach((col) => {
                if (col && col.custom) {
                  row[col.custom.column.column] = col.v;
                }
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
        for (let rowIndex = 0; rowIndex < this.rawTableData.resultSet.length; rowIndex++) {
          if (this.deleteRows.includes(rowIndex)) {
            const deleteRow = this.buildWhereData(rowIndex);
            if (!this.hasWhereData(deleteRow)) {
              continue;
            }
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
        for (let rowIndex = 0; rowIndex < this.rawTableData.resultSet.length; rowIndex++) {
          if (this.deleteRows.includes(rowIndex)) continue;
          const whereData = this.buildWhereData(rowIndex);
          if (!this.hasWhereData(whereData)) {
            continue;
          }
          const updateData = {};
          for (let colIndex = 0; colIndex < this.columnWithoutHidden.length; colIndex++) {
            const column = this.columnWithoutHidden[colIndex];
            const cell = data[rowIndex] && data[rowIndex][colIndex];
            if (cell && cell.custom && cell.custom.update && !cell.custom.new && !cell.custom.delete) {
              updateData[column.column] = cell.v;
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

        if (!dataParamList.length) {
          Modal.info({
            title: '提示',
            content: '表数据未进行修改'
          });
          return;
        }

        const res = await this.$services.dmEditorDataGenerateDml({
          data: {
            ...this.generateDatasourceParams(),
            changeRows: dataParamList,
            columnList: this.columnWithoutHidden
          }
        });

        if (res.success) {
          this.sqls = res.data;
          this.renewData = renewData;
          const sqlList = [];
          res.data.forEach((sql) => {
            sqlList.push(sql.sql);
          });
          if (sqlList.length) {
            this.sqlString = sqlList.join('\n');
            this.showSqlModal = true;
          }
        }
      } catch (e) {
        console.error('ResultEditView submit failed:', e);
        Modal.error({
          title: this.$t('ti-shi'),
          content: '提交失败，请重试'
        });
      }
    },

    async handleRun() {
      this.executeSQLLoading = true;
      const data = this.generateDatasourceParams();
      this.executeInfo = [];
      this.refreshAfterExecute = false;
      this.createList = {};
      this.updateList = {};
      this.deleteList = {};
      let error = false;
      try {
        for (const sqlObj of this.sqls) {
          if (error) {
            this.executeInfo.unshift({
              database: this.targetName,
              queryBody: sqlObj.sql,
              refresh: sqlObj.refresh,
              success: false,
              message: '未执行'
            });
            this.refreshAfterExecute = false;
          } else {
            const { sequence, sql } = sqlObj;
            data.columnList = this.columnWithoutHidden;
            data.changeRow = deepClone(this.renewData[sequence]);
            const res = await this.$services.dmEditorDataSaveData({
              data
            });

            const executeSuccess = !!(res.success && res.data && res.data.success !== false);
            const info = {
              database: this.targetName,
              queryBody: sql,
              ...(res.data || {}),
              success: executeSuccess,
              message: res.data?.message || res.message
            };
            this.executeInfo.unshift(info);

            if (!executeSuccess) {
              error = true;
              this.refreshAfterExecute = false;
            } else {
              this.refreshAfterExecute = this.refreshAfterExecute || res.data.refresh;
              const { resultSet, resultSetMore } = res.data;

              if (resultSet && resultSet.length) {
                if (this.renewData[sequence].type === 'deleteParam') {
                  this.deleteList[sequence] = { sequence, resultSet, resultSetMore };
                }
                if (this.renewData[sequence].type === 'createParam') {
                  this.createList[sequence] = { sequence, resultSet, resultSetMore };
                }
                if (this.renewData[sequence].type === 'updateParam') {
                  this.updateList[sequence] = { sequence, resultSet, resultSetMore };
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
    },

    async handleCloseExecuteInfoModal() {
      this.showExecuteInfoModal = false;
      const hasSuccess = this.executeInfo.some((info) => info.success);
      if (this.refreshAfterExecute || hasSuccess) {
        this.$emit('saved', { refresh: true });
        return;
      }
      this.createList = {};
      this.updateList = {};
      this.deleteList = {};
      if (this.hasPendingLocalResults()) {
        this.applyLocalResults();
      }
    },

    applyLocalResults() {
      if (!this.hasPendingLocalResults()) {
        return;
      }
      const { resultSet, resultSetMore } = this.rawTableData;
      const generateResultSet = [];
      const generateResultSetMore = [];

      Object.keys(this.createList).forEach((key) => {
        const row = this.createList[key].resultSet?.[0];
        if (row) {
          resultSet[key] = row;
          resultSetMore[key] = this.createList[key].resultSetMore?.[0] || {};
        }
      });

      Object.keys(this.updateList).forEach((key) => {
        const row = this.updateList[key].resultSet?.[0];
        if (row) {
          resultSet[key] = row;
          resultSetMore[key] = this.updateList[key].resultSetMore?.[0] || {};
        }
      });

      resultSet.forEach((row, rowIndex) => {
        if (!this.deleteList[rowIndex] && row) {
          generateResultSet.push(row);
        }
      });

      resultSetMore.forEach((row, rowIndex) => {
        if (!this.deleteList[rowIndex] && row) {
          generateResultSetMore.push(row);
        }
      });

      this.rawTableData.resultSet = generateResultSet;
      this.rawTableData.resultSetMore = generateResultSetMore;

      const celldata = [];
      const deleteRows = [];
      const addRows = [];
      const updateCellList = [];
      const preData = [];
      window.luckysheet.getSheetData().forEach((row, rowIndex) => {
        if (this.deleteList[rowIndex]) {
          return;
        }
        let arr;
        if (Array.isArray(row)) {
          arr = row;
        } else {
          arr = Object.values(row);
        }
        const firstCell = arr[0];
        if (firstCell && firstCell.custom && firstCell.custom.new && firstCell.custom.delete) {
          return;
        }
        arr.forEach((cell, colIndex) => {
          if (cell && cell.custom) {
            cell.custom.r = rowIndex;
            cell.custom.c = colIndex;
          }
        });
        preData.push(arr);
      });

      preData.forEach((row, rowIndex) => {
        updateCellList[rowIndex] = [];
        row.forEach((cell, columnIndex) => {
          if (!cell || !cell.custom) return;
          if (cell.custom.r !== undefined && this.addRows.includes(cell.custom.r)) {
            if (this.createList[cell.custom.r]) {
              cell.custom.new = false;
              cell.custom.v = this.createList[cell.custom.r].resultSet[0][cell.custom.column.column];
              cell.bg = BG_COLOR.CUSTOM;
              cell.v = cell.custom.v;
              cell.m = cell.v;
            }
            cell.custom.r = rowIndex;
            cell.custom.c = columnIndex;
            celldata.push({ r: rowIndex, c: columnIndex, v: { ...cell } });
          } else {
            if (cell.custom.r !== undefined && this.updateList[cell.custom.r]) {
              cell.custom.update = false;
              cell.custom.v = this.updateList[cell.custom.r].resultSet[0][cell.custom.column.column];
              cell.bg = BG_COLOR.CUSTOM;
              cell.v = cell.custom.v;
              cell.m = cell.v;
            }
            cell.custom.r = rowIndex;
            cell.custom.c = columnIndex;
            celldata.push({ r: rowIndex, c: columnIndex, v: { ...cell } });
          }
        });
      });

      celldata.forEach((cell) => {
        if (cell.v.custom && cell.v.custom.new) {
          if (!addRows.includes(cell.v.custom.r)) {
            addRows.push(cell.v.custom.r);
          }
        }
        if (cell.v.custom && cell.v.custom.delete) {
          if (!deleteRows.includes(cell.v.custom.r)) {
            deleteRows.push(cell.v.custom.r);
          }
        }
        if (cell.v.custom && cell.v.custom.update) {
          if (!updateCellList[cell.v.custom.r]) {
            updateCellList[cell.v.custom.r] = [];
          }
          updateCellList[cell.v.custom.r][cell.v.custom.column.column] = true;
        }
      });

      this.addRows = addRows;
      this.deleteRows = deleteRows;
      this.updateCellList = updateCellList;

      if (celldata.length) {
        delete this.options.data[0].data;
        this.options.data[0].celldata = celldata;
        this.options.data[0].row = preData.length;
        window.luckysheet.updataSheet(
          { data: this.options.data },
          {
            success: () => {
              window.luckysheet.refresh();
            }
          }
        );
      }
    },

    handleExport(format) {
      // delegate to exportMixin
      if (format === 'csvSelectedData') {
        this.exportSelectedData();
      } else if (format === 'csvSelectedRows') {
        this.exportSelectedRows();
      } else if (format === 'csvCurrentPage') {
        this.exportCurrentPage();
      }
    },

    exportSelectedData() {
      const rangeList = window.luckysheet.getRange();
      if (!rangeList.length) return;
      const data = window.luckysheet.getSheetData();
      const lines = [];
      rangeList.forEach((range) => {
        for (let r = range.row[0]; r <= range.row[1]; r++) {
          const vals = [];
          for (let c = range.column[0]; c <= range.column[1]; c++) {
            if (data[r] && data[r][c]) {
              vals.push(data[r][c].v || '');
            }
          }
          lines.push(vals.join(','));
        }
      });
      this.copyText(lines.join('\n'));
    },

    exportSelectedRows() {
      const rangeList = window.luckysheet.getRange();
      if (!rangeList.length) return;
      const data = window.luckysheet.getSheetData();
      const lines = [];
      const colCount = this.columnWithoutHidden.length;
      rangeList.forEach((range) => {
        for (let r = range.row[0]; r <= range.row[1]; r++) {
          const vals = [];
          for (let c = 0; c < colCount; c++) {
            if (data[r] && data[r][c]) {
              vals.push(data[r][c].v || '');
            }
          }
          lines.push(vals.join(','));
        }
      });
      this.copyText(lines.join('\n'));
    },

    exportCurrentPage() {
      const data = window.luckysheet.getSheetData();
      const lines = [];
      const colCount = this.columnWithoutHidden.length;
      for (let r = 0; r < data.length; r++) {
        const vals = [];
        for (let c = 0; c < colCount; c++) {
          if (data[r] && data[r][c]) {
            vals.push(data[r][c].v || '');
          }
        }
        lines.push(vals.join(','));
      }
      this.copyText(lines.join('\n'));
    },

    handleCancel() {
      const doCancel = () => {
        this.showSqlModal = false;
        this.showExecuteInfoModal = false;
        this.executeSQLLoading = false;
        this.$emit('cancel');
      };
      if (this.isEditing) {
        Modal.confirm({
          title: '警告',
          content: '当前有未提交的修改，确定要退出编辑吗？',
          onOk: () => {
            doCancel();
          }
        });
      } else {
        doCancel();
      }
    }
  }
};
</script>

<template>
  <div class="result-edit-view" :class="{ 'is-modal-open': showSqlModal || showExecuteInfoModal }">
    <div class="header">
      <div class="toolbar">
        <a-button class="op" size="small" @click="handleAddRow" :disabled="executeSQLLoading">
          <template #icon>
            <PlusOutlined />
          </template>
          {{ $t('xin-zeng-hang') }}
        </a-button>
        <a-button class="op" size="small" @click="handleDeleteRow()" :disabled="executeSQLLoading">
          <template #icon>
            <MinusOutlined />
          </template>
          {{ $t('shan-chu-hang') }}
        </a-button>
        <a-button type="primary" size="small" @click="handleSubmit" :disabled="executeSQLLoading">
          {{ $t('ti-jiao') }}
        </a-button>
        <a-button size="small" @click="handleCancel">
          {{ $t('qu-xiao') }}
        </a-button>
      </div>
    </div>
    <div class="table-container">
      <div v-if="loading" class="edit-loading-overlay">
        <div class="edit-loading-indicator" aria-hidden="true">
          <span class="edit-loading-ring"></span>
          <span class="edit-loading-dot"></span>
        </div>
        <span class="edit-loading-text">
          {{ $t('huo-qu-shu-ju-zhong') }}
          <span class="edit-loading-ellipsis" aria-hidden="true">
            <i></i>
            <i></i>
            <i></i>
          </span>
        </span>
      </div>
      <div id="luckysheet-result-edit"></div>
    </div>
    <CCModal v-model="showSqlModal" :title="$t('sql-yu-ju')" :width="800" :mask-closable="false" :zIndex="1100">
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
      :zIndex="1100"
      @on-cancel="handleCloseExecuteInfoModal"
    >
      <div style="height: 500px; overflow: auto">
        <div v-for="(info, index) in executeInfo" :key="index" class="result-info">
          <div class="first">
            <div :class="`level ${info.success ? 'Info' : 'Error'}`">{{ info.database }}</div>
            <div class="sql">{{ info.queryBody }}</div>
          </div>
          <div class="second">
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
.result-edit-view {
  display: flex;
  flex-direction: column;
  min-height: 0;
  position: relative;

  .header {
    height: 40px;
    display: flex;
    align-items: center;
    padding: 0 8px;
    border-bottom: 1px solid #f0f0f0;
    flex-shrink: 0;

    .toolbar {
      display: flex;
      align-items: center;
      gap: 4px;

      .op {
        display: flex;
        align-items: center;
        gap: 2px;
      }
    }
  }

  .table-container {
    position: relative;
    flex: 1;
    width: 100%;
    height: 100%;
    min-height: 0;
    overflow: hidden;

    #luckysheet-result-edit {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
    }
  }

  .edit-loading-overlay {
    position: absolute;
    inset: 0;
    z-index: 20;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 14px;
    background: rgba(255, 255, 255, 0.94);
  }

  .edit-loading-indicator {
    position: relative;
    width: 36px;
    height: 36px;
  }

  .edit-loading-ring {
    position: absolute;
    inset: 0;
    border: 2px solid #e8eaed;
    border-top-color: #181d26;
    border-radius: 50%;
    animation: result-edit-loading-spin 0.9s linear infinite;
  }

  .edit-loading-dot {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 6px;
    height: 6px;
    margin: -3px 0 0 -3px;
    border-radius: 50%;
    background: #181d26;
    animation: result-edit-loading-pulse 1.2s ease-in-out infinite;
  }

  .edit-loading-text {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-size: 14px;
    line-height: 20px;
    color: #41454d;
    letter-spacing: 0.02em;
  }

  .edit-loading-ellipsis {
    display: inline-flex;
    align-items: center;
    gap: 3px;
    height: 20px;

    i {
      width: 4px;
      height: 4px;
      border-radius: 50%;
      background: #41454d;
      opacity: 0.25;
      animation: result-edit-loading-dot-fade 1.2s ease-in-out infinite;
    }

    i:nth-child(2) {
      animation-delay: 0.15s;
    }

    i:nth-child(3) {
      animation-delay: 0.3s;
    }
  }
}

@keyframes result-edit-loading-spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

@keyframes result-edit-loading-pulse {
  0%,
  100% {
    transform: scale(0.85);
    opacity: 0.45;
  }

  50% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes result-edit-loading-dot-fade {
  0%,
  80%,
  100% {
    opacity: 0.2;
    transform: translateY(0);
  }

  40% {
    opacity: 1;
    transform: translateY(-2px);
  }
}

:deep(.luckysheet-loading-mask),
:deep(.result-edit-luckysheet-loading-hidden) {
  display: none !important;
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

:deep(.luckysheet-stat-area) {
  display: none !important;
}

:deep(.luckysheet-cell-main) {
  background: #fff;
}

:deep(.luckysheet-cs-draghandle) {
  display: none !important;
}

:deep(.luckysheet-scrollbar-ltr) {
  z-index: 1 !important;
}

.result-edit-view.is-modal-open {
  :deep(#luckysheet-scrollbar-x),
  :deep(#luckysheet-scrollbar-y) {
    visibility: hidden;
    pointer-events: none;
  }
}

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

  .message-error {
    color: #ed4014;
  }
}
</style>
