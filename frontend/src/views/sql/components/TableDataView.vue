<script>
import { computed, defineComponent, nextTick, ref, watch } from 'vue';
import { columnResizingFeature, columnSizingFeature, tableFeatures, useTable } from '@tanstack/vue-table';
import { useVirtualizer } from '@tanstack/vue-virtual';
import ContextMenu from '@imengyu/vue3-context-menu';
import { Modal } from 'ant-design-vue';
import dayjs from 'dayjs';
import { PushpinFilled } from '@ant-design/icons-vue';
import { cloneDeep as deepClone } from '@/utils/lodash';
import copyMixin from '@/mixins/copyMixin';
import browseMixin from '@/mixins/browseMixin';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import CCModal from '@/components/ui/CCModal.vue';
import DataEditorToolbar from '@/views/sql/components/dataEditor/DataEditorToolbar.vue';
import DataEditorFindBar from '@/views/sql/components/dataEditor/DataEditorFindBar.vue';
import DataEditorStatusBar from '@/views/sql/components/dataEditor/DataEditorStatusBar.vue';
import ResultExportModal from '@/views/sql/components/ResultExportModal.vue';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import {
  DATA_EXTRACTORS,
  appendFilterCondition,
  buildChangeRowsPayload,
  buildFilterClause,
  buildOrderByString,
  computeAutoFitColumnWidth,
  computeColumnWidths,
  computeSelectionStats,
  countChangeBreakdown,
  createNewRowModel,
  createRowModels,
  formatSelectionWithExtractor,
  getCellPlaceholder,
  isCellReadOnly,
  parseClipboardMatrix,
  resolveDatabaseLabel,
  syncTabEditTracking,
  toggleMultiColumnSort
} from '@/views/sql/components/dataEditor/dataEditorState';

const ROW_HEIGHT = 30;
const ROW_NUM_COL_WIDTH = 54;
const TRANSPOSE_COL_WIDTH = 200;
const TRANSPOSE_VAL_WIDTH = 200;
const HEADER_BADGES = {
  ROW_NUM: '#',
  PK: 'PK',
  UK: 'UK',
  STAR: '*',
  COLUMN_HDR: 'Column',
  SORT_TIP: 'Click to sort (Shift+Click for multi-column sort)'
};

const ORDER_TEXT = {
  desc: '↓',
  asc: '↑',
  '': '↕'
};

export default defineComponent({
  name: 'TableDataView',
  components: {
    CCModal,
    ReadOnlyEditor,
    DataEditorToolbar,
    DataEditorFindBar,
    DataEditorStatusBar,
    ResultExportModal,
    PushpinFilled
  },
  mixins: [copyMixin, browseMixin],
  props: {
    tab: {
      type: Object,
      required: true
    },
    setLoading: {
      type: Function,
      required: true
    },
    handleClickDsStatusIcon: {
      type: Function,
      default: null
    }
  },
  setup(props) {
    const rows = ref([]);
    const allColumns = ref([]);
    const hiddenColumnSet = ref({});
    const pinnedColumnSet = ref({});
    const columnSizing = ref({});
    const scrollContainerRef = ref(null);

    const isReadOnly = computed(() => Boolean(props.tab?.rawTableData?.readOnly));
    const changeBreakdown = computed(() => countChangeBreakdown(rows.value));
    const pendingChangeCount = computed(() => changeBreakdown.value.total);
    const isEditing = computed(() => pendingChangeCount.value > 0);

    // Pinned columns come first, then unpinned visible columns
    const visibleColumns = computed(() => {
      const visible = allColumns.value.filter((col) => !hiddenColumnSet.value[col.column]);
      const pinned = visible.filter((col) => pinnedColumnSet.value[col.column]);
      const unpinned = visible.filter((col) => !pinnedColumnSet.value[col.column]);
      return [...pinned, ...unpinned];
    });

    const pinnedLeftOffsets = computed(() => {
      const map = {};
      let left = ROW_NUM_COL_WIDTH;
      visibleColumns.value.forEach((col) => {
        if (pinnedColumnSet.value[col.column]) {
          map[col.column] = left;
          left += columnSizing.value[col.column] || col.width || 140;
        }
      });
      return map;
    });

    const tanstackColumns = computed(() =>
      visibleColumns.value.map((col, colIndex) => ({
        id: col.column,
        accessorFn: (row) => row.values[col.column],
        header: col.column,
        size: columnSizing.value[col.column] || col.width || 140,
        minSize: 80,
        maxSize: 600,
        meta: {
          columnDef: col,
          colIndex
        }
      }))
    );

    const features = tableFeatures({
      columnSizingFeature,
      columnResizingFeature
    });

    const tableState = computed(() => ({
      columnSizing: columnSizing.value
    }));

    const table = useTable({
      features,
      data: rows,
      columns: tanstackColumns,
      state: tableState,
      enableColumnResizing: true,
      columnResizeMode: 'onChange',
      onColumnSizingChange: (updater) => {
        const next = typeof updater === 'function' ? updater(columnSizing.value) : updater;
        columnSizing.value = next;
        if (props.tab) {
          props.tab.columnlen = { ...next };
        }
      }
    });

    const tableRows = computed(() => table.getRowModel().rows);

    const rowVirtualizer = useVirtualizer(
      computed(() => ({
        count: tableRows.value.length,
        getScrollElement: () => scrollContainerRef.value,
        estimateSize: () => ROW_HEIGHT,
        overscan: 14
      }))
    );

    const virtualRows = computed(() => rowVirtualizer.value.getVirtualItems());
    const totalHeight = computed(() => rowVirtualizer.value.getTotalSize());
    const totalTableWidth = computed(() => {
      const colsWidth = table.getAllLeafColumns().reduce((sum, col) => sum + col.getSize(), 0);
      return ROW_NUM_COL_WIDTH + colsWidth;
    });

    watch(
      rows,
      (val) => {
        syncTabEditTracking(props.tab, val);
      },
      { deep: true }
    );

    return {
      rows,
      allColumns,
      visibleColumns,
      hiddenColumnSet,
      pinnedColumnSet,
      pinnedLeftOffsets,
      columnSizing,
      scrollContainerRef,
      isReadOnly,
      changeBreakdown,
      pendingChangeCount,
      isEditing,
      table,
      tableRows,
      rowVirtualizer,
      virtualRows,
      totalHeight,
      totalTableWidth,
      ROW_HEIGHT,
      ROW_NUM_COL_WIDTH,
      TRANSPOSE_COL_WIDTH,
      TRANSPOSE_VAL_WIDTH,
      HEADER_BADGES,
      ORDER_TEXT
    };
  },
  data() {
    return {
      loading: false,
      createSQLLoading: false,
      executeSQLLoading: false,
      showSqlModal: false,
      showExecuteInfoModal: false,
      sqlString: '',
      sqls: [],
      renewData: {},
      executeInfo: [],
      refreshAfterExecute: false,
      createList: {},
      updateList: {},
      deleteList: {},
      permission: {
        hasPermission: true,
        permissionI18n: ''
      },
      selection: null,
      anchorCell: null,
      isDraggingSelection: false,
      isDraggingRowNum: false,
      editingCell: null,
      editingValue: '',
      awaitingFirstInputKeydown: false,
      initialTypedCharGuard: null,
      activeExtractor: 'tsv',
      isTransposed: false,
      sortConfigs: [],
      filterHistory: [],
      findVisible: false,
      findQuery: '',
      findCaseSensitive: false,
      findUseRegex: false,
      activeMatchIndex: -1,
      exportRowIndex: null,
      exportSubmitting: false,
      exportState: {
        exporting: false,
        percent: 0,
        downloadFile: {
          trackId: '',
          file: '',
          format: ''
        },
        errorStatus: null,
        errorMessage: null
      }
    };
  },
  computed: {
    hasSelection() {
      return Boolean(this.selection && this.rows.length > 0 && this.visibleColumns.length > 0);
    },
    normalizedSelection() {
      if (!this.selection) return null;
      return {
        startRow: Math.min(this.selection.startRow, this.selection.endRow),
        endRow: Math.max(this.selection.startRow, this.selection.endRow),
        startCol: Math.min(this.selection.startCol, this.selection.endCol),
        endCol: Math.max(this.selection.startCol, this.selection.endCol)
      };
    },
    selectionStats() {
      return computeSelectionStats(this.rows, this.visibleColumns, this.normalizedSelection);
    },
    activeExtractorLabel() {
      return (DATA_EXTRACTORS.find((e) => e.id === this.activeExtractor) || DATA_EXTRACTORS[0]).label;
    },
    findState() {
      const q = this.findQuery;
      if (!this.findVisible || !q) {
        return { matches: [], matchSet: {}, regexError: false };
      }
      let matcher = null;
      if (this.findUseRegex) {
        try {
          const flags = this.findCaseSensitive ? '' : 'i';
          const re = new RegExp(q, flags);
          matcher = (text) => re.test(text);
        } catch {
          return { matches: [], matchSet: {}, regexError: true };
        }
      } else {
        const needle = this.findCaseSensitive ? q : q.toLowerCase();
        matcher = (text) => (this.findCaseSensitive ? text : text.toLowerCase()).includes(needle);
      }

      const matches = [];
      const matchSet = {};
      this.rows.forEach((row, rIdx) => {
        this.visibleColumns.forEach((col, cIdx) => {
          const val = row.values[col.column];
          if (val === null || val === undefined) return;
          if (matcher(String(val))) {
            matches.push({ row: rIdx, col: cIdx });
            matchSet[`${rIdx}:${cIdx}`] = true;
          }
        });
      });
      return { matches, matchSet, regexError: false };
    }
  },
  watch: {
    'tab.connected'(connected) {
      if (connected && !this.tab.rawTableData?.columnList?.length) {
        this.handleGetTableData(-2, true, true, false, true);
      }
    },
    'tab.key'(newVal, oldVal) {
      if (newVal && newVal !== oldVal) {
        this.cancelEdit();
        this.clearSelection();
        this.tab.total = null;
        this.hiddenColumnSet = {};
        this.pinnedColumnSet = {};
        this.sortConfigs = [];
        this.resetExportState();
        this.handleGetTableData(-2, true, true, true, true);
      }
    },
    'findState.matches'(matches) {
      if (!matches.length) {
        this.activeMatchIndex = -1;
      } else if (this.activeMatchIndex < 0 || this.activeMatchIndex >= matches.length) {
        this.activeMatchIndex = 0;
        this.jumpToMatch(0);
      }
    }
  },
  async mounted() {
    window.addEventListener('mouseup', this.handleWindowMouseUp);
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_EXPORT_EVENT, this.handleWsExportEvent);
    if (this.tab.connected) {
      await this.handleGetTableData(-2, true, true, false, true);
    }
  },
  beforeUnmount() {
    window.removeEventListener('mouseup', this.handleWindowMouseUp);
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_EXPORT_EVENT, this.handleWsExportEvent);
  },
  methods: {
    dayjs,
    getCellPlaceholder,
    isCellReadOnly(rowState, column) {
      return isCellReadOnly(rowState, column, this.isReadOnly);
    },
    generateDatasourceParams() {
      const { node, selectedTable, targetType } = this.tab;
      return {
        levels: this.browseGenLevelsData(node),
        targetName: selectedTable.title,
        targetType
      };
    },
    clearSelection() {
      this.selection = null;
      this.anchorCell = null;
    },
    isCellSelected(rowIndex, colIndex) {
      const range = this.normalizedSelection;
      if (!range) return false;
      return rowIndex >= range.startRow && rowIndex <= range.endRow && colIndex >= range.startCol && colIndex <= range.endCol;
    },
    isPrimaryCell(rowIndex, colIndex) {
      return Boolean(this.anchorCell && this.anchorCell.row === rowIndex && this.anchorCell.col === colIndex);
    },
    isCellEditing(rowIndex, colIndex) {
      return Boolean(this.editingCell && this.editingCell.row === rowIndex && this.editingCell.col === colIndex);
    },
    isFindMatch(rowIndex, colIndex) {
      return Boolean(this.findState.matchSet[`${rowIndex}:${colIndex}`]);
    },
    isActiveFindMatch(rowIndex, colIndex) {
      const m = this.findState.matches[this.activeMatchIndex];
      return Boolean(m && m.row === rowIndex && m.col === colIndex);
    },
    getSortInfo(colName) {
      const idx = this.sortConfigs.findIndex((s) => s.column === colName);
      if (idx < 0) return { dir: '', priority: 0 };
      return { dir: this.sortConfigs[idx].sortBy, priority: this.sortConfigs.length > 1 ? idx + 1 : 0 };
    },
    selectCell(rowIndex, colIndex, extend = false) {
      if (this.editingCell && (this.editingCell.row !== rowIndex || this.editingCell.col !== colIndex)) {
        this.commitEdit();
      }
      if (extend && this.anchorCell) {
        this.selection = {
          startRow: this.anchorCell.row,
          endRow: rowIndex,
          startCol: this.anchorCell.col,
          endCol: colIndex
        };
      } else {
        this.anchorCell = { row: rowIndex, col: colIndex };
        this.selection = {
          startRow: rowIndex,
          endRow: rowIndex,
          startCol: colIndex,
          endCol: colIndex
        };
      }
    },
    handleCellMouseDown(event, rowIndex, colIndex) {
      if (event.button !== 0) return;
      this.selectCell(rowIndex, colIndex, event.shiftKey);
      if (!event.shiftKey) {
        this.isDraggingSelection = true;
      }
    },
    handleCellMouseEnter(rowIndex, colIndex) {
      if (this.isDraggingSelection && this.anchorCell) {
        this.selection = {
          startRow: this.anchorCell.row,
          endRow: rowIndex,
          startCol: this.anchorCell.col,
          endCol: colIndex
        };
      }
    },
    handleRowNumMouseDown(event, rowIndex) {
      if (event.button !== 0 || this.visibleColumns.length === 0) return;
      this.selectRow(rowIndex, event.shiftKey);
      if (!event.shiftKey) {
        this.isDraggingRowNum = true;
      }
    },
    handleRowNumMouseEnter(rowIndex) {
      if (this.isDraggingRowNum && this.anchorCell && this.visibleColumns.length > 0) {
        this.selection = {
          startRow: this.anchorCell.row,
          endRow: rowIndex,
          startCol: 0,
          endCol: this.visibleColumns.length - 1
        };
      }
    },
    handleWindowMouseUp() {
      this.isDraggingSelection = false;
      this.isDraggingRowNum = false;
    },
    selectRow(rowIndex, extend = false) {
      if (this.visibleColumns.length === 0) return;
      if (this.editingCell) {
        this.commitEdit();
      }
      const lastCol = this.visibleColumns.length - 1;
      if (extend && this.anchorCell) {
        this.selection = {
          startRow: this.anchorCell.row,
          endRow: rowIndex,
          startCol: 0,
          endCol: lastCol
        };
      } else {
        this.anchorCell = { row: rowIndex, col: 0 };
        this.selection = {
          startRow: rowIndex,
          endRow: rowIndex,
          startCol: 0,
          endCol: lastCol
        };
      }
    },
    selectColumn(colIndex, extend = false) {
      if (this.rows.length === 0) return;
      if (this.editingCell) {
        this.commitEdit();
      }
      const lastRow = this.rows.length - 1;
      if (extend && this.anchorCell) {
        this.selection = {
          startRow: 0,
          endRow: lastRow,
          startCol: this.anchorCell.col,
          endCol: colIndex
        };
      } else {
        this.anchorCell = { row: 0, col: colIndex };
        this.selection = {
          startRow: 0,
          endRow: lastRow,
          startCol: colIndex,
          endCol: colIndex
        };
      }
    },
    selectAllCells() {
      if (!this.rows.length || !this.visibleColumns.length) return;
      if (this.editingCell) this.commitEdit();
      this.anchorCell = { row: 0, col: 0 };
      this.selection = {
        startRow: 0,
        endRow: this.rows.length - 1,
        startCol: 0,
        endCol: this.visibleColumns.length - 1
      };
    },
    handleEmptyUpdate() {
      this.cancelEdit();
      this.rows = createRowModels(this.tab.rawTableData?.resultSet || [], this.tab.rawTableData?.resultSetMore || []);
      syncTabEditTracking(this.tab, this.rows);
    },
    handleRevertAll() {
      if (!this.isEditing) return;
      this.handleEmptyUpdate();
    },
    recordFilterHistory(cond) {
      const trimmed = String(cond || '').trim();
      if (!trimmed) return;
      const next = [trimmed, ...this.filterHistory.filter((item) => item !== trimmed)].slice(0, 12);
      this.filterHistory = next;
    },
    applyFilterHistory(cond) {
      this.tab.condition = cond;
      this.handleGetTableData(0);
    },
    hydrateFromRawTableData() {
      const raw = this.tab.rawTableData || {};
      const columnList = raw.columnList || [];
      const resultSet = raw.resultSet || [];
      const resultSetMore = raw.resultSetMore || [];

      const filteredColumns = columnList.filter((column) => column.column !== 'ROWNUM' && !column.hide);
      const whereKeyList = [];
      const spareWhereList = [];

      columnList.forEach((column) => {
        if (column.whereKey) whereKeyList.push(column.column);
        if (column.spareWhere) spareWhereList.push(column.column);
      });

      this.tab.whereKeyList = whereKeyList.length ? whereKeyList : spareWhereList;
      this.tab.columnWithoutHidden = filteredColumns;
      this.allColumns = filteredColumns;

      const computedWidths = computeColumnWidths(filteredColumns, resultSet);
      const mergedWidths = {};
      filteredColumns.forEach((col) => {
        mergedWidths[col.column] = this.tab.columnlen?.[col.column] || computedWidths[col.column];
        col.width = mergedWidths[col.column];
      });
      this.columnSizing = mergedWidths;
      this.tab.columnlen = { ...mergedWidths };

      this.rows = createRowModels(resultSet, resultSetMore);
      syncTabEditTracking(this.tab, this.rows);
      this.clearSelection();
    },
    async handleGetTableData(newOffset = -2, refresh = false, cancelSelect = true, force = false, mounted = false) {
      if (!this.tab.connected) return;
      const getData = async () => {
        this.cancelEdit();
        if (cancelSelect) {
          this.clearSelection();
        }
        if (mounted && this.tab.rawTableData?.columnList?.length && !force) {
          this.hydrateFromRawTableData();
          return;
        }
        this.recordFilterHistory(this.tab.condition);
        this.loading = true;
        this.setLoading(true);
        try {
          const res = await this.$services.dmEditorDataFetchData({
            data: {
              ...this.generateDatasourceParams(),
              offset: newOffset > -2 ? newOffset : this.tab.offset,
              pageSize: this.tab.pageSize,
              condition: this.tab.condition,
              orderBy: this.tab.orderBy
            }
          });
          if (res.success && res.data) {
            this.tab.rawTableData = deepClone(res.data);
            this.tab.hasNext = res.data.hasNext;
            this.tab.isFirst = res.data.isFirst;
            this.tab.offset = res.data.offset;
            this.tab.pageSize = res.data.pageSize;
            this.tab.selectedPageKeys = [String(res.data.pageSize)];
            this.hydrateFromRawTableData();
            if (this.scrollContainerRef) {
              this.scrollContainerRef.scrollTop = 0;
            }
          }
        } finally {
          this.loading = false;
          this.setLoading(false);
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
    async handleGetTableDataTotal() {
      if (!this.tab.connected) return;
      this.loading = true;
      this.setLoading(true);
      try {
        const res = await this.$services.dmEditorDataFetchCount({
          data: {
            ...this.generateDatasourceParams(),
            condition: this.tab.condition
          }
        });
        if (res.success) {
          this.tab.total = res.data;
        }
      } finally {
        this.loading = false;
        this.setLoading(false);
      }
    },
    handlePageSizeChange(event) {
      const nextSize = parseInt(event.key, 10);
      const maxPageSize = this.tab.rawTableData?.maxPageSize;
      if (!Number.isFinite(nextSize) || nextSize <= 0 || (maxPageSize > 0 && nextSize > maxPageSize)) {
        return;
      }
      this.tab.pageSize = nextSize;
      this.tab.selectedPageKeys = [String(nextSize)];
      this.handleGetTableData(0, true);
    },
    handleHeaderSort(column, multi = false) {
      this.sortConfigs = toggleMultiColumnSort(this.sortConfigs, column.column, multi);
      this.tab.orderBy = buildOrderByString(this.sortConfigs);
      this.handleGetTableData(this.tab.offset, false, false);
    },
    setColumnSort(column, dir) {
      const filtered = this.sortConfigs.filter((s) => s.column !== column.column);
      if (dir) {
        filtered.unshift({ column: column.column, sortBy: dir });
      }
      this.sortConfigs = filtered;
      this.tab.orderBy = buildOrderByString(this.sortConfigs);
      this.handleGetTableData(this.tab.offset, false, false);
    },
    handleAutoFitColumn(column) {
      const width = computeAutoFitColumnWidth(column, this.rows);
      this.columnSizing = {
        ...this.columnSizing,
        [column.column]: width
      };
      this.tab.columnlen = { ...this.columnSizing };
    },
    toggleColumnVisibility(colName) {
      const next = { ...this.hiddenColumnSet };
      if (next[colName]) {
        delete next[colName];
      } else if (this.visibleColumns.length > 1) {
        next[colName] = true;
      }
      this.hiddenColumnSet = next;
    },
    showAllColumns() {
      this.hiddenColumnSet = {};
    },
    toggleColumnPin(colName) {
      const next = { ...this.pinnedColumnSet };
      if (next[colName]) {
        delete next[colName];
      } else {
        next[colName] = true;
      }
      this.pinnedColumnSet = next;
    },
    jumpToColumn(colName) {
      if (this.hiddenColumnSet[colName]) {
        this.toggleColumnVisibility(colName);
      }
      nextTick(() => {
        const colIdx = this.visibleColumns.findIndex((c) => c.column === colName);
        if (colIdx < 0 || !this.scrollContainerRef) return;
        let offsetLeft = 0;
        for (let i = 0; i < colIdx; i++) {
          const c = this.visibleColumns[i];
          offsetLeft += this.columnSizing[c.column] || c.width || 140;
        }
        this.scrollContainerRef.scrollLeft = offsetLeft;
        if (this.rows.length > 0) {
          this.selectCell(this.anchorCell ? this.anchorCell.row : 0, colIdx, false);
        }
      });
    },
    startEdit(rowIndex, colIndex, initialTypedChar = null) {
      const row = this.rows[rowIndex];
      const column = this.visibleColumns[colIndex];
      if (!row || !column || this.isCellReadOnly(row, column)) return;

      this.selectCell(rowIndex, colIndex, false);
      const currentVal = row.values[column.column];
      this.editingCell = { row: rowIndex, col: colIndex };
      this.awaitingFirstInputKeydown = initialTypedChar !== null;
      this.initialTypedCharGuard = initialTypedChar;

      if (column.columnType === 'SET' && Array.isArray(column.option) && column.option.length) {
        this.editingValue = currentVal ? String(currentVal).split(',').filter(Boolean) : [];
      } else if (column.columnType === 'ENUM' && Array.isArray(column.option) && column.option.length) {
        this.editingValue = currentVal === null || currentVal === undefined ? undefined : String(currentVal);
      } else if (initialTypedChar !== null) {
        this.editingValue = initialTypedChar;
      } else {
        this.editingValue = currentVal === null || currentVal === undefined ? '' : String(currentVal);
      }

      nextTick(() => {
        const inputEl = this.$el?.querySelector('.tdv-inline-input');
        if (inputEl && typeof inputEl.focus === 'function') {
          inputEl.focus();
          if (initialTypedChar === null && typeof inputEl.select === 'function') {
            inputEl.select();
          } else if (typeof inputEl.setSelectionRange === 'function') {
            const len = String(this.editingValue).length;
            inputEl.setSelectionRange(len, len);
          }
        }
      });
    },
    applyCellValue(rowIndex, colIndex, nextValue, { explicitNull = false } = {}) {
      const row = this.rows[rowIndex];
      const column = this.visibleColumns[colIndex];
      if (!row || !column || this.isCellReadOnly(row, column)) return;

      const colName = column.column;
      row.values[colName] = nextValue;

      if (row.isNew) {
        if (nextValue === null) {
          row.explicitNull[colName] = Boolean(explicitNull);
        } else {
          row.explicitNull[colName] = false;
        }
      } else {
        const orig = row.originalValues[colName];
        const unchanged = Object.is(orig, nextValue) || (orig === undefined && nextValue === null);
        if (unchanged) {
          delete row.dirtyFields[colName];
        } else {
          row.dirtyFields[colName] = true;
        }
      }
      syncTabEditTracking(this.tab, this.rows);
    },
    commitEdit() {
      if (!this.editingCell) return;
      const { row: rowIndex, col: colIndex } = this.editingCell;
      const row = this.rows[rowIndex];
      const column = this.visibleColumns[colIndex];
      this.editingCell = null;
      if (!row || !column) return;

      const colName = column.column;
      const oldVal = row.values[colName];
      let nextVal;

      if (column.columnType === 'SET' && Array.isArray(column.option) && column.option.length) {
        nextVal = Array.isArray(this.editingValue) ? this.editingValue.join(',') : '';
      } else if (column.columnType === 'ENUM' && Array.isArray(column.option) && column.option.length) {
        nextVal = this.editingValue === undefined ? null : this.editingValue;
      } else {
        if (oldVal === null && this.editingValue === '') {
          return;
        }
        nextVal = this.editingValue;
      }

      if (row.isNew && nextVal === '' && (column.hasDefault || column.autoincrement) && oldVal !== null) {
        this.applyCellValue(rowIndex, colIndex, null, { explicitNull: false });
        return;
      }

      this.applyCellValue(rowIndex, colIndex, nextVal, { explicitNull: false });
    },
    cancelEdit() {
      this.editingCell = null;
      this.editingValue = '';
      this.awaitingFirstInputKeydown = false;
      this.initialTypedCharGuard = null;
    },
    handleInlineInputKeydown() {
      this.awaitingFirstInputKeydown = false;
    },
    handleInlineBeforeInput(event) {
      if (this.awaitingFirstInputKeydown && event.data === this.initialTypedCharGuard) {
        event.preventDefault();
        this.awaitingFirstInputKeydown = false;
      }
    },
    handleInlineInput(event) {
      if (this.awaitingFirstInputKeydown && event.target.value === `${this.initialTypedCharGuard}${this.initialTypedCharGuard}`) {
        this.awaitingFirstInputKeydown = false;
        event.target.value = this.initialTypedCharGuard;
        this.editingValue = this.initialTypedCharGuard;
        return;
      }
      this.awaitingFirstInputKeydown = false;
      this.editingValue = event.target.value;
    },
    handleAddRow(sourceRowIndex = null) {
      if (this.isReadOnly || !this.allColumns.length) return;
      this.commitEdit();
      const sourceRow = typeof sourceRowIndex === 'number' && sourceRowIndex >= 0 ? this.rows[sourceRowIndex] : null;
      const newRow = createNewRowModel(this.allColumns, sourceRow);
      this.rows = [...this.rows, newRow];
      const newIndex = this.rows.length - 1;
      syncTabEditTracking(this.tab, this.rows);

      nextTick(() => {
        if (!this.isTransposed) {
          this.rowVirtualizer.scrollToIndex(newIndex, { align: 'end' });
        }
        const firstEditableCol = this.visibleColumns.findIndex((col) => !col.insertReadOnly);
        this.selectCell(newIndex, firstEditableCol >= 0 ? firstEditableCol : 0, false);
      });
    },
    handleCloneSelectedRow() {
      const rowIdx = this.anchorCell ? this.anchorCell.row : -1;
      if (rowIdx < 0) return;
      this.handleAddRow(rowIdx);
    },
    handleDeleteRow(targetRowIndex = null) {
      if (this.isReadOnly) return;
      this.commitEdit();
      const indices = [];
      if (typeof targetRowIndex === 'number' && targetRowIndex >= 0) {
        indices.push(targetRowIndex);
      } else {
        const range = this.normalizedSelection;
        if (range) {
          for (let r = range.startRow; r <= range.endRow; r++) {
            indices.push(r);
          }
        }
      }
      if (!indices.length) return;
      indices.forEach((rowIdx) => {
        const row = this.rows[rowIdx];
        if (row) row.markedDelete = true;
      });
      syncTabEditTracking(this.tab, this.rows);
    },
    handleRollbackSelection(targetRowIndex = null, targetColIndex = null) {
      if (this.isReadOnly) return;
      this.commitEdit();

      let range = this.normalizedSelection;
      if (typeof targetRowIndex === 'number' && targetRowIndex >= 0) {
        const c = typeof targetColIndex === 'number' && targetColIndex >= 0 ? targetColIndex : 0;
        range = { startRow: targetRowIndex, endRow: targetRowIndex, startCol: c, endCol: c };
      }
      if (!range) return;

      for (let r = range.startRow; r <= range.endRow; r++) {
        const row = this.rows[r];
        if (!row) continue;
        if (row.markedDelete) {
          row.markedDelete = false;
          continue;
        }
        if (row.isNew) {
          row.markedDelete = true;
          continue;
        }
        for (let c = range.startCol; c <= range.endCol; c++) {
          const col = this.visibleColumns[c];
          if (!col) continue;
          const colName = col.column;
          row.values[colName] = row.originalValues[colName];
          delete row.dirtyFields[colName];
        }
      }
      syncTabEditTracking(this.tab, this.rows);
    },
    handleSetSelectionValue(nextValue, { explicitNull = false } = {}) {
      if (this.isReadOnly) return;
      this.commitEdit();
      const range = this.normalizedSelection;
      if (!range) return;

      for (let r = range.startRow; r <= range.endRow; r++) {
        for (let c = range.startCol; c <= range.endCol; c++) {
          this.applyCellValue(r, c, nextValue, { explicitNull });
        }
      }
    },
    handleQuickFilter(rowIndex, colIndex, operator = '=') {
      const row = this.rows[rowIndex];
      const column = this.visibleColumns[colIndex];
      if (!row || !column) return;
      const clause = buildFilterClause(column, row.values[column.column], operator);
      if (!clause) return;
      this.tab.condition = appendFilterCondition(this.tab.condition, clause);
      this.handleGetTableData(0);
    },
    copySelectedCells(extractorOverride = null) {
      const range = this.normalizedSelection;
      if (!range) return;
      const extractor = extractorOverride || this.activeExtractor;
      const text = formatSelectionWithExtractor(extractor, this.rows, this.visibleColumns, range, {
        tableName: this.tab.selectedTable?.title || 'table_name',
        whereKeyList: this.tab.whereKeyList || []
      });
      this.copyText(text);
    },
    applyPasteText(text) {
      if (this.isReadOnly || !this.anchorCell) return;
      const matrix = parseClipboardMatrix(text);
      if (!matrix.length) return;
      const range = this.normalizedSelection;

      // Single-value fill across entire selection (DataGrip behavior)
      if (matrix.length === 1 && matrix[0].length === 1 && range && (range.endRow > range.startRow || range.endCol > range.startCol)) {
        const val = matrix[0][0];
        for (let r = range.startRow; r <= range.endRow; r++) {
          for (let c = range.startCol; c <= range.endCol; c++) {
            this.applyCellValue(r, c, val, { explicitNull: false });
          }
        }
        return;
      }

      // Matrix paste starting at anchorCell
      const startRow = range ? range.startRow : this.anchorCell.row;
      const startCol = range ? range.startCol : this.anchorCell.col;
      matrix.forEach((rowVals, rOffset) => {
        const targetRow = startRow + rOffset;
        if (targetRow >= this.rows.length) return;
        rowVals.forEach((cellVal, cOffset) => {
          const targetCol = startCol + cOffset;
          if (targetCol >= this.visibleColumns.length) return;
          this.applyCellValue(targetRow, targetCol, cellVal, { explicitNull: false });
        });
      });
    },
    handleGridPaste(event) {
      if (this.editingCell || this.isReadOnly) return;
      const text = event.clipboardData?.getData('text/plain');
      if (typeof text === 'string') {
        event.preventDefault();
        this.applyPasteText(text);
      }
    },
    async handlePasteFromContextMenu() {
      if (this.isReadOnly || !navigator.clipboard?.readText) return;
      try {
        const text = await navigator.clipboard.readText();
        this.applyPasteText(text);
      } catch {
        // clipboard permission denied
      }
    },
    handleOpenExportModal(rowIndex = null) {
      const resultId = this.tab.rawTableData?.resultId;
      if (!resultId || !this.allColumns.length) {
        this.$Message.info(this.$t('zan-wu-shu-ju'));
        return;
      }

      const isFromRowMenu = rowIndex !== null && rowIndex !== undefined;
      const targetRow = isFromRowMenu ? rowIndex : this.normalizedSelection ? this.normalizedSelection.startRow : null;
      this.exportRowIndex = targetRow;

      this.$refs.resultExportModal?.openExportModal({
        columns: this.allColumns.map((col) => col.column),
        tableName: this.tab.selectedTable?.title || this.tab.rawTableData?.table || 'table_name',
        dataSourceType: this.tab.dataSourceType || this.tab.dsType || '',
        allowSingleRow: targetRow !== null,
        showAllOption: false,
        defaultRange: isFromRowMenu ? 'single' : 'page'
      });
    },
    async handleConfirmExport({ dstFormatName, exportRangeType, option }) {
      const resultId = this.tab.rawTableData?.resultId;
      if (!resultId) {
        this.$Message.warning(this.$t('zan-wu-shu-ju'));
        return;
      }

      let offset = 0;
      let limit = -1;
      if (exportRangeType === 'single') {
        offset = this.exportRowIndex !== null && this.exportRowIndex !== undefined ? this.exportRowIndex : 0;
        limit = 1;
      } else if (exportRangeType === 'page') {
        offset = 0;
        limit = this.rows.length || -1;
      }

      this.exportSubmitting = true;
      try {
        const res = await this.$services.dmQueryExportResult({
          data: {
            resultId,
            dstFileName: '',
            dstFormatName,
            option: {
              ...option,
              offset,
              limit
            }
          }
        });

        if (res.success) {
          this.$refs.resultExportModal?.closeExportModal();
          this.exportState = {
            exporting: true,
            percent: 0,
            downloadFile: {
              trackId: res?.data?.trackId || '',
              file: res?.data?.newFile || '',
              format: res?.data?.newFormat || ''
            },
            errorStatus: null,
            errorMessage: null
          };
        } else {
          this.$Message.error(res.message || this.$t('cao-zuo-shi-bai'));
        }
      } catch (error) {
        this.$Message.error(error?.message || this.$t('cao-zuo-shi-bai'));
      } finally {
        this.exportSubmitting = false;
      }
    },
    handleWsExportEvent(exportData) {
      if (!exportData?.trackId || exportData.trackId !== this.exportState?.downloadFile?.trackId) {
        return;
      }
      this.exportState.percent = exportData.percent ?? this.exportState.percent;
      if (exportData.status === 'FAILED') {
        this.exportState.errorStatus = exportData.status;
        this.exportState.errorMessage = exportData.message || this.$t('cao-zuo-shi-bai-qing-zhong-xin-zhi-hang-cha-xun');
        this.exportState.exporting = false;
        this.$Message.warning(this.exportState.errorMessage);
      } else if (exportData.status === 'SUCCESS' || exportData.percent === 100) {
        this.exportState.exporting = false;
        this.exportState.percent = 100;
        this.exportState.errorStatus = null;
        this.exportState.errorMessage = null;
      }
    },
    openDownloadExportModal() {
      if (this.exportState?.downloadFile?.trackId) {
        this.$refs.resultExportModal?.openDownloadModal(this.exportState.downloadFile);
      }
    },
    resetExportState() {
      this.exportState = {
        exporting: false,
        percent: 0,
        downloadFile: {
          trackId: '',
          file: '',
          format: ''
        },
        errorStatus: null,
        errorMessage: null
      };
    },
    jumpToMatch(index) {
      const m = this.findState.matches[index];
      if (!m) return;
      this.activeMatchIndex = index;
      this.selectCell(m.row, m.col, false);
      if (!this.isTransposed) {
        this.rowVirtualizer.scrollToIndex(m.row, { align: 'center' });
      }
    },
    handleFindNext() {
      const len = this.findState.matches.length;
      if (!len) return;
      this.jumpToMatch((this.activeMatchIndex + 1) % len);
    },
    handleFindPrev() {
      const len = this.findState.matches.length;
      if (!len) return;
      this.jumpToMatch((this.activeMatchIndex - 1 + len) % len);
    },
    handleGridWheel(event) {
      if (!event.shiftKey) return;
      const scroller = event.currentTarget;
      if (!scroller || scroller.scrollWidth <= scroller.clientWidth) return;
      const delta = Math.abs(event.deltaX) > Math.abs(event.deltaY) ? event.deltaX : event.deltaY;
      if (!delta) return;
      event.preventDefault();
      scroller.scrollLeft += delta;
    },
    handleGridKeydown(event) {
      const mod = event.metaKey || event.ctrlKey;

      if (mod && event.key.toLowerCase() === 'f') {
        event.preventDefault();
        this.findVisible = true;
        nextTick(() => this.$refs.findBar?.focusInput());
        return;
      }

      if (mod && event.key === 'Enter') {
        event.preventDefault();
        if (!this.isReadOnly && this.isEditing) {
          this.handleSubmit();
        }
        return;
      }

      if (this.editingCell) {
        if (event.key === 'Escape') {
          event.preventDefault();
          this.cancelEdit();
        } else if (event.key === 'Enter' && !event.shiftKey) {
          event.preventDefault();
          const { row, col } = this.editingCell;
          this.commitEdit();
          if (row + 1 < this.rows.length) {
            this.selectCell(row + 1, col, false);
          }
        } else if (event.key === 'Tab') {
          event.preventDefault();
          const { row, col } = this.editingCell;
          this.commitEdit();
          const nextCol = event.shiftKey ? col - 1 : col + 1;
          if (nextCol >= 0 && nextCol < this.visibleColumns.length) {
            this.selectCell(row, nextCol, false);
          }
        }
        return;
      }

      if (mod && event.key.toLowerCase() === 'a') {
        event.preventDefault();
        this.selectAllCells();
        return;
      }

      if (mod && event.key.toLowerCase() === 'r') {
        event.preventDefault();
        this.handleGetTableData(this.tab.offset);
        return;
      }

      if (event.altKey && event.key === 'Insert') {
        event.preventDefault();
        this.handleAddRow();
        return;
      }

      if (!this.anchorCell || !this.rows.length || !this.visibleColumns.length) return;

      if (mod && event.key.toLowerCase() === 'c') {
        event.preventDefault();
        this.copySelectedCells();
        return;
      }

      if (mod && event.key.toLowerCase() === 'd') {
        event.preventDefault();
        this.handleCloneSelectedRow();
        return;
      }

      if (mod && event.key.toLowerCase() === 'y') {
        event.preventDefault();
        this.handleDeleteRow();
        return;
      }

      if (mod && event.key.toLowerCase() === 'z') {
        event.preventDefault();
        this.handleRollbackSelection();
        return;
      }

      if ((event.key === 'Delete' || event.key === 'Backspace') && !this.isReadOnly) {
        event.preventDefault();
        this.handleSetSelectionValue(null, { explicitNull: true });
        return;
      }

      const { row, col } = this.anchorCell;
      if (event.key === 'ArrowUp') {
        event.preventDefault();
        const nextRow = Math.max(0, row - 1);
        this.selectCell(nextRow, col, event.shiftKey);
        if (!this.isTransposed) this.rowVirtualizer.scrollToIndex(nextRow, { align: 'auto' });
      } else if (event.key === 'ArrowDown') {
        event.preventDefault();
        const nextRow = Math.min(this.rows.length - 1, row + 1);
        this.selectCell(nextRow, col, event.shiftKey);
        if (!this.isTransposed) this.rowVirtualizer.scrollToIndex(nextRow, { align: 'auto' });
      } else if (event.key === 'ArrowLeft') {
        event.preventDefault();
        this.selectCell(row, Math.max(0, col - 1), event.shiftKey);
      } else if (event.key === 'ArrowRight') {
        event.preventDefault();
        this.selectCell(row, Math.min(this.visibleColumns.length - 1, col + 1), event.shiftKey);
      } else if (event.key === 'Tab') {
        event.preventDefault();
        const nextCol = event.shiftKey ? col - 1 : col + 1;
        if (nextCol >= 0 && nextCol < this.visibleColumns.length) {
          this.selectCell(row, nextCol, false);
        }
      } else if (event.key === 'Enter' || event.key === 'F2') {
        event.preventDefault();
        this.startEdit(row, col);
      } else if (!mod && !event.altKey && event.key.length === 1 && !this.isReadOnly) {
        // DataGrip Type-to-Edit
        const targetRow = this.rows[row];
        const targetCol = this.visibleColumns[col];
        if (!this.isCellReadOnly(targetRow, targetCol)) {
          event.preventDefault();
          event.stopPropagation();
          this.startEdit(row, col, event.key);
        }
      }
    },
    handleHeaderContextMenu(event, column) {
      const isPinned = Boolean(this.pinnedColumnSet[column.column]);
      ContextMenu.showContextMenu({
        zIndex: 1050,
        x: event.x,
        y: event.y,
        theme: 'flat',
        customClass: 'sql-context-menu',
        minWidth: 180,
        items: [
          { label: 'Sort Ascending', onClick: () => this.setColumnSort(column, 'asc') },
          { label: 'Sort Descending', onClick: () => this.setColumnSort(column, 'desc') },
          { label: 'Clear Sort', onClick: () => this.setColumnSort(column, '') },
          { divided: true },
          { label: isPinned ? 'Unpin Column' : 'Pin Column Left', onClick: () => this.toggleColumnPin(column.column) },
          { label: 'Auto-fit Width', onClick: () => this.handleAutoFitColumn(column) },
          { label: 'Hide Column', disabled: this.visibleColumns.length <= 1, onClick: () => this.toggleColumnVisibility(column.column) },
          { divided: true },
          { label: `${this.$t('fu-zhi')} ${column.column}`, onClick: () => this.copyText(column.column) }
        ]
      });
    },
    handleCellContextMenu(event, rowIndex, colIndex) {
      if (!this.isCellSelected(rowIndex, colIndex)) {
        this.selectCell(rowIndex, colIndex, false);
      }
      const row = this.rows[rowIndex];
      const column = this.visibleColumns[colIndex];
      const cellReadOnly = this.isCellReadOnly(row, column);
      const eqClause = buildFilterClause(column, row?.values?.[column?.column], '=');
      const neqClause = buildFilterClause(column, row?.values?.[column?.column], '!=');

      const items = [
        {
          label: `Filter by ${eqClause.length > 32 ? `${eqClause.slice(0, 32)}...` : eqClause}`,
          onClick: () => this.handleQuickFilter(rowIndex, colIndex, '=')
        },
        {
          label: `Exclude ${neqClause.length > 32 ? `${neqClause.slice(0, 32)}...` : neqClause}`,
          onClick: () => this.handleQuickFilter(rowIndex, colIndex, '!=')
        },
        { divided: true },
        {
          label: `${this.$t('fu-zhi')} (${this.activeExtractorLabel})`,
          onClick: () => this.copySelectedCells()
        },
        {
          label: 'Copy As',
          children: DATA_EXTRACTORS.map((ext) => ({
            label: ext.label,
            onClick: () => this.copySelectedCells(ext.id)
          }))
        }
      ];

      if (!this.isReadOnly) {
        items.push(
          { divided: true },
          {
            label: this.$t('she-zhi-wei-null'),
            disabled: cellReadOnly,
            onClick: () => this.handleSetSelectionValue(null, { explicitNull: true })
          },
          {
            label: 'Set DEFAULT',
            disabled: cellReadOnly || !row?.isNew,
            onClick: () => this.handleSetSelectionValue(null, { explicitNull: false })
          },
          {
            label: this.$t('she-zhi-wei-kong-zi-fu-chuan'),
            disabled: cellReadOnly,
            onClick: () => this.handleSetSelectionValue('', { explicitNull: false })
          },
          {
            label: 'Paste',
            disabled: cellReadOnly,
            onClick: () => this.handlePasteFromContextMenu()
          },
          { divided: true },
          {
            label: this.$t('xin-zeng-hang'),
            onClick: () => this.handleAddRow()
          },
          {
            label: this.$t('fu-zhi-xuan-zhong-hang'),
            onClick: () => this.handleAddRow(rowIndex)
          },
          {
            label: this.$t('shan-chu-hang'),
            onClick: () => this.handleDeleteRow()
          },
          {
            label: this.$t('che-xiao-xiu-gai'),
            onClick: () => this.handleRollbackSelection()
          }
        );
      }

      items.push(
        { divided: true },
        {
          label: this.$t('dao-chu'),
          onClick: () => this.handleOpenExportModal(rowIndex)
        }
      );

      ContextMenu.showContextMenu({
        zIndex: 1050,
        x: event.x,
        y: event.y,
        theme: 'flat',
        customClass: 'sql-context-menu',
        minWidth: 220,
        items
      });
    },
    async handleSubmit() {
      if (this.isReadOnly) return;
      this.commitEdit();
      const { changeRows, renewData } = buildChangeRowsPayload(this.rows, this.allColumns, this.tab.whereKeyList || []);

      if (!changeRows.length) {
        Modal.info({
          title: this.$t('ti-shi'),
          content: this.$t('biao-shu-ju-wei-jin-hang-xiu-gai')
        });
        return;
      }

      this.createSQLLoading = true;
      try {
        const res = await this.$services.dmEditorDataGenerateDml({
          data: {
            ...this.generateDatasourceParams(),
            changeRows,
            columnList: this.tab.rawTableData.columnList
          }
        });

        if (res.success) {
          const sqlList = Array.isArray(res.data) ? res.data : [];
          this.sqls = sqlList;
          this.renewData = renewData;
          this.permission = {
            hasPermission: res.permission !== false,
            permissionI18n: res.permissionI18n || ''
          };

          if (sqlList.length) {
            this.sqlString = sqlList.map((item) => item.sql).join('\n');
            this.showSqlModal = true;
          } else {
            Modal.info({
              title: this.$t('ti-shi'),
              content: this.$t('biao-shu-ju-wei-jin-hang-xiu-gai')
            });
          }
        }
      } finally {
        this.createSQLLoading = false;
      }
    },
    async handleRun() {
      if (!this.tab.connected || !this.permission.hasPermission) return;
      this.executeSQLLoading = true;
      const baseData = this.generateDatasourceParams();
      const dbLabel = resolveDatabaseLabel(this.tab.node);
      this.executeInfo = [];
      this.createList = {};
      this.updateList = {};
      this.deleteList = {};
      this.refreshAfterExecute = false;
      let error = false;

      try {
        for (const sqlObj of this.sqls) {
          if (error) {
            this.executeInfo.unshift({
              database: dbLabel,
              queryBody: sqlObj.sql,
              startTimestamp: Date.now(),
              success: false,
              message: this.$t('wei-zhi-hang')
            });
            this.refreshAfterExecute = false;
          } else {
            const { sequence, sql } = sqlObj;
            const payload = {
              ...baseData,
              columnList: this.tab.rawTableData.columnList,
              changeRow: deepClone(this.renewData[sequence])
            };

            let res;
            try {
              res = await this.$services.dmEditorDataSaveData({ data: payload });
            } catch (e) {
              res = { success: false, msg: e.message };
            }

            if (res.success && res.permission === false) {
              error = true;
              this.refreshAfterExecute = false;
              this.executeInfo.unshift({
                database: dbLabel,
                queryBody: sql,
                startTimestamp: Date.now(),
                success: false,
                message: res.permissionI18n || res.msg
              });
            } else if (res.success && res.data) {
              this.executeInfo.unshift({
                database: dbLabel,
                queryBody: sql,
                startTimestamp: Date.now(),
                ...res.data
              });

              if (!res.data.success) {
                error = true;
                this.refreshAfterExecute = false;
              } else {
                this.refreshAfterExecute = this.refreshAfterExecute || Boolean(res.data.refresh);
                const { resultSet, resultSetMore } = res.data;
                const rowParam = this.renewData[sequence];
                if (rowParam.type === 'deleteParam') {
                  this.deleteList[sequence] = { sequence, resultSet, resultSetMore };
                } else if (rowParam.type === 'createParam') {
                  this.createList[sequence] = { sequence, resultSet, resultSetMore };
                } else if (rowParam.type === 'updateParam') {
                  this.updateList[sequence] = { sequence, resultSet, resultSetMore };
                }
              }
            } else {
              error = true;
              this.refreshAfterExecute = false;
              this.executeInfo.unshift({
                database: dbLabel,
                queryBody: sql,
                startTimestamp: Date.now(),
                success: false,
                message: res.msg
              });
            }
          }

          if (this.showSqlModal) this.showSqlModal = false;
          if (!this.showExecuteInfoModal) this.showExecuteInfoModal = true;
        }
      } finally {
        this.executeSQLLoading = false;
      }

      if (!error) {
        this.tab.isEditing = false;
      }
    },
    async handleCloseExecuteInfoModal() {
      this.showExecuteInfoModal = false;
      if (this.refreshAfterExecute) {
        await this.handleGetTableData(this.tab.offset, true, true, true);
        return;
      }

      const nextRows = [];
      const nextRawResultSet = [];
      const nextRawResultSetMore = [];

      this.rows.forEach((row, idx) => {
        if (this.deleteList[idx] || (row.isNew && row.markedDelete)) return;
        if (this.createList[idx]) {
          const createdRow = this.createList[idx].resultSet?.[0] || row.values;
          const createdMore = this.createList[idx].resultSetMore?.[0] || {};
          nextRawResultSet.push(deepClone(createdRow));
          nextRawResultSetMore.push(deepClone(createdMore));
          nextRows.push({
            ...row,
            isNew: false,
            markedDelete: false,
            originalValues: deepClone(createdRow),
            values: deepClone(createdRow),
            more: deepClone(createdMore),
            dirtyFields: {},
            explicitNull: {}
          });
          return;
        }
        if (this.updateList[idx]) {
          const updatedRow = this.updateList[idx].resultSet?.[0] || row.values;
          const updatedMore = this.updateList[idx].resultSetMore?.[0] || row.more;
          nextRawResultSet.push(deepClone(updatedRow));
          nextRawResultSetMore.push(deepClone(updatedMore));
          nextRows.push({
            ...row,
            isNew: false,
            markedDelete: false,
            originalValues: deepClone(updatedRow),
            values: deepClone(updatedRow),
            more: deepClone(updatedMore),
            dirtyFields: {},
            explicitNull: {}
          });
          return;
        }
        if (!row.isNew) {
          nextRawResultSet.push(deepClone(row.originalValues));
          nextRawResultSetMore.push(deepClone(row.more));
        }
        nextRows.push(row);
      });

      this.tab.rawTableData.resultSet = nextRawResultSet;
      this.tab.rawTableData.resultSetMore = nextRawResultSetMore;
      this.rows = nextRows;
      syncTabEditTracking(this.tab, this.rows);
    }
  }
});
</script>

<template>
  <div class="table-data-view">
    <DataEditorToolbar
      :tab="tab"
      :loading="loading"
      :create-s-q-l-loading="createSQLLoading"
      :is-read-only="isReadOnly"
      :is-editing="isEditing"
      :pending-change-count="pendingChangeCount"
      :has-selection="hasSelection"
      :is-transposed="isTransposed"
      :find-visible="findVisible"
      :all-columns="allColumns"
      :hidden-column-set="hiddenColumnSet"
      :pinned-column-set="pinnedColumnSet"
      :filter-history="filterHistory"
      :export-state="exportState"
      @fetch-page="handleGetTableData($event)"
      @fetch-total="handleGetTableDataTotal"
      @page-size-change="handlePageSizeChange"
      @add-row="handleAddRow()"
      @clone-row="handleCloneSelectedRow"
      @delete-row="handleDeleteRow()"
      @rollback-selection="handleRollbackSelection()"
      @revert-all="handleRevertAll"
      @submit="handleSubmit"
      @export="handleOpenExportModal()"
      @download-export="openDownloadExportModal"
      @reset-export="resetExportState"
      @toggle-transpose="isTransposed = !isTransposed"
      @toggle-find="findVisible = !findVisible"
      @toggle-column-visibility="toggleColumnVisibility"
      @show-all-columns="showAllColumns"
      @toggle-column-pin="toggleColumnPin"
      @jump-to-column="jumpToColumn"
      @apply-filter-history="applyFilterHistory"
    />

    <div class="tdv-workspace">
      <div class="tdv-grid-stage">
        <DataEditorFindBar
          ref="findBar"
          :visible="findVisible"
          :query="findQuery"
          :case-sensitive="findCaseSensitive"
          :use-regex="findUseRegex"
          :match-count="findState.matches.length"
          :active-match-index="activeMatchIndex"
          :regex-error="findState.regexError"
          @update:query="findQuery = $event"
          @update:case-sensitive="findCaseSensitive = $event"
          @update:use-regex="findUseRegex = $event"
          @next="handleFindNext"
          @prev="handleFindPrev"
          @close="findVisible = false"
        />

        <!-- Standard Grid View -->
        <div
          v-if="!isTransposed"
          ref="scrollContainerRef"
          class="tdv-grid-scroll"
          data-overlay-top-offset="44"
          tabindex="0"
          @wheel="handleGridWheel"
          @keydown="handleGridKeydown"
          @paste="handleGridPaste"
        >
          <div class="tdv-grid-inner" :style="{ width: `${totalTableWidth}px` }">
            <!-- Dual-line DataGrip Sticky Header -->
            <div class="tdv-header-row">
              <div
                class="tdv-cell tdv-header-cell tdv-rownum-col tdv-corner-cell"
                :style="{ width: `${ROW_NUM_COL_WIDTH}px` }"
                @click="selectAllCells"
              >
                {{ HEADER_BADGES.ROW_NUM }}
              </div>
              <div v-for="headerGroup in table.getHeaderGroups()" :key="headerGroup.id" class="tdv-header-group">
                <div
                  v-for="header in headerGroup.headers"
                  :key="header.id"
                  class="tdv-cell tdv-header-cell"
                  :class="{
                    'is-pinned': Boolean(pinnedColumnSet[header.column.id])
                  }"
                  :style="{
                    width: `${header.getSize()}px`,
                    left: pinnedColumnSet[header.column.id] ? `${pinnedLeftOffsets[header.column.id]}px` : undefined
                  }"
                  :title="`${header.column.columnDef.meta.columnDef.column} (${header.column.columnDef.meta.columnDef.columnType || ''})`"
                  @click="selectColumn(header.column.columnDef.meta.colIndex, $event.shiftKey)"
                  @contextmenu.prevent.stop="handleHeaderContextMenu($event, header.column.columnDef.meta.columnDef)"
                >
                  <div class="tdv-header-stack">
                    <div class="tdv-header-top">
                      <span v-if="header.column.columnDef.meta.columnDef.isPk" class="tdv-key-badge tdv-key-pk">
                        {{ HEADER_BADGES.PK }}
                      </span>
                      <span v-else-if="header.column.columnDef.meta.columnDef.isUk" class="tdv-key-badge tdv-key-uk">
                        {{ HEADER_BADGES.UK }}
                      </span>
                      <span v-if="!header.column.columnDef.meta.columnDef.isNullable" class="tdv-required-star">
                        {{ HEADER_BADGES.STAR }}
                      </span>
                      <span class="tdv-header-name">{{ header.column.columnDef.meta.columnDef.column }}</span>
                      <PushpinFilled v-if="pinnedColumnSet[header.column.id]" class="tdv-pin-indicator" />
                    </div>
                    <div class="tdv-header-type">{{ header.column.columnDef.meta.columnDef.columnType }}</div>
                  </div>
                  <button
                    type="button"
                    class="tdv-sort-btn"
                    :class="{ 'is-active': Boolean(getSortInfo(header.column.id).dir) }"
                    :title="HEADER_BADGES.SORT_TIP"
                    @click.stop="handleHeaderSort(header.column.columnDef.meta.columnDef, $event.shiftKey)"
                  >
                    <span class="tdv-sort-glyph">{{ ORDER_TEXT[getSortInfo(header.column.id).dir] }}</span>
                    <sup v-if="getSortInfo(header.column.id).priority > 0" class="tdv-sort-sup">{{ getSortInfo(header.column.id).priority }}</sup>
                  </button>
                  <div
                    class="tdv-resize-handle"
                    :class="{ 'is-resizing': header.column.getIsResizing() }"
                    @mousedown.stop="header.getResizeHandler()($event)"
                    @touchstart.stop="header.getResizeHandler()($event)"
                    @dblclick.stop="handleAutoFitColumn(header.column.columnDef.meta.columnDef)"
                  />
                </div>
              </div>
            </div>

            <!-- Virtual Body -->
            <div v-if="tableRows.length > 0" class="tdv-body" :style="{ height: `${totalHeight}px` }">
              <div
                v-for="vRow in virtualRows"
                :key="tableRows[vRow.index].original.uid"
                class="tdv-data-row"
                :class="{
                  'is-new': tableRows[vRow.index].original.isNew && !tableRows[vRow.index].original.markedDelete,
                  'is-delete': !tableRows[vRow.index].original.isNew && tableRows[vRow.index].original.markedDelete,
                  'is-new-delete': tableRows[vRow.index].original.isNew && tableRows[vRow.index].original.markedDelete,
                  'is-modified':
                    !tableRows[vRow.index].original.isNew &&
                    !tableRows[vRow.index].original.markedDelete &&
                    Object.keys(tableRows[vRow.index].original.dirtyFields || {}).length > 0
                }"
                :style="{
                  height: `${ROW_HEIGHT}px`,
                  transform: `translateY(${vRow.start}px)`
                }"
              >
                <div
                  class="tdv-cell tdv-rownum-col tdv-rownum-cell"
                  :style="{ width: `${ROW_NUM_COL_WIDTH}px` }"
                  @mousedown.prevent="handleRowNumMouseDown($event, vRow.index)"
                  @mouseenter="handleRowNumMouseEnter(vRow.index)"
                >
                  {{ tab.offset + vRow.index + 1 }}
                </div>

                <div
                  v-for="cell in tableRows[vRow.index].getAllCells()"
                  :key="cell.id"
                  class="tdv-cell tdv-data-cell"
                  :class="{
                    'is-pinned': Boolean(pinnedColumnSet[cell.column.id]),
                    'is-selected': isCellSelected(vRow.index, cell.column.columnDef.meta.colIndex),
                    'is-primary': isPrimaryCell(vRow.index, cell.column.columnDef.meta.colIndex),
                    'is-editing': isCellEditing(vRow.index, cell.column.columnDef.meta.colIndex),
                    'is-dirty':
                      !tableRows[vRow.index].original.isNew &&
                      !tableRows[vRow.index].original.markedDelete &&
                      tableRows[vRow.index].original.dirtyFields[cell.column.id],
                    'is-readonly': isCellReadOnly(tableRows[vRow.index].original, cell.column.columnDef.meta.columnDef),
                    'is-find-match': isFindMatch(vRow.index, cell.column.columnDef.meta.colIndex),
                    'is-find-active': isActiveFindMatch(vRow.index, cell.column.columnDef.meta.colIndex)
                  }"
                  :style="{
                    width: `${cell.column.getSize()}px`,
                    left: pinnedColumnSet[cell.column.id] ? `${pinnedLeftOffsets[cell.column.id]}px` : undefined
                  }"
                  @mousedown="handleCellMouseDown($event, vRow.index, cell.column.columnDef.meta.colIndex)"
                  @mouseenter="handleCellMouseEnter(vRow.index, cell.column.columnDef.meta.colIndex)"
                  @dblclick="startEdit(vRow.index, cell.column.columnDef.meta.colIndex)"
                  @contextmenu.prevent.stop="handleCellContextMenu($event, vRow.index, cell.column.columnDef.meta.colIndex)"
                >
                  <!-- Inline Editor -->
                  <template v-if="editingCell && editingCell.row === vRow.index && editingCell.col === cell.column.columnDef.meta.colIndex">
                    <a-select
                      v-if="cell.column.columnDef.meta.columnDef.columnType === 'ENUM' && cell.column.columnDef.meta.columnDef.option?.length"
                      v-model:value="editingValue"
                      size="small"
                      class="tdv-inline-select"
                      :open="true"
                      allow-clear
                      @change="commitEdit"
                      @blur="commitEdit"
                    >
                      <a-select-option v-for="opt in cell.column.columnDef.meta.columnDef.option" :key="opt" :value="opt">
                        {{ opt }}
                      </a-select-option>
                    </a-select>
                    <a-select
                      v-else-if="cell.column.columnDef.meta.columnDef.columnType === 'SET' && cell.column.columnDef.meta.columnDef.option?.length"
                      v-model:value="editingValue"
                      mode="multiple"
                      size="small"
                      class="tdv-inline-select"
                      :max-tag-count="1"
                      @blur="commitEdit"
                    >
                      <a-select-option v-for="opt in cell.column.columnDef.meta.columnDef.option" :key="opt" :value="opt">
                        {{ opt }}
                      </a-select-option>
                    </a-select>
                    <input
                      v-else
                      :value="editingValue"
                      type="text"
                      class="tdv-inline-input"
                      @keydown="handleInlineInputKeydown"
                      @beforeinput="handleInlineBeforeInput"
                      @input="handleInlineInput"
                      @blur="commitEdit"
                    />
                  </template>

                  <!-- Display Value -->
                  <template v-else>
                    <span
                      v-if="
                        getCellPlaceholder(
                          tableRows[vRow.index].original,
                          cell.column.columnDef.meta.columnDef,
                          tableRows[vRow.index].original.values[cell.column.id]
                        )
                      "
                      class="tdv-placeholder"
                    >
                      {{
                        getCellPlaceholder(
                          tableRows[vRow.index].original,
                          cell.column.columnDef.meta.columnDef,
                          tableRows[vRow.index].original.values[cell.column.id]
                        )
                      }}
                    </span>
                    <span v-else class="tdv-cell-text">
                      {{ tableRows[vRow.index].original.values[cell.column.id] }}
                    </span>

                    <span v-if="tableRows[vRow.index].original.more?.[cell.column.id]" class="tdv-truncated-badge">
                      {{ $t('wen-ben-jie-duan') }}
                    </span>
                  </template>
                </div>
              </div>
            </div>

            <div v-else-if="!loading" class="tdv-empty">
              <span>{{ $t('zan-wu-shu-ju') }}</span>
            </div>
          </div>
        </div>

        <!-- DataGrip Transpose View (Rows <-> Columns) -->
        <div
          v-else
          class="tdv-grid-scroll tdv-transpose-scroll"
          data-overlay-top-offset="36"
          tabindex="0"
          @wheel="handleGridWheel"
          @keydown="handleGridKeydown"
          @paste="handleGridPaste"
        >
          <div
            v-if="rows.length > 0 && visibleColumns.length > 0"
            class="tdv-transpose-table"
            :style="{ width: `${TRANSPOSE_COL_WIDTH + rows.length * TRANSPOSE_VAL_WIDTH}px` }"
          >
            <div class="tdv-transpose-header">
              <div class="tdv-cell tdv-header-cell tdv-transpose-sticky" :style="{ width: `${TRANSPOSE_COL_WIDTH}px` }">
                {{ HEADER_BADGES.COLUMN_HDR }}
              </div>
              <div
                v-for="(row, rIdx) in rows"
                :key="row.uid"
                class="tdv-cell tdv-header-cell"
                :class="{
                  'is-new': row.isNew && !row.markedDelete,
                  'is-delete': !row.isNew && row.markedDelete
                }"
                :style="{ width: `${TRANSPOSE_VAL_WIDTH}px` }"
              >
                {{ `${HEADER_BADGES.ROW_NUM}${tab.offset + rIdx + 1}` }}
              </div>
            </div>

            <div v-for="(col, cIdx) in visibleColumns" :key="col.column" class="tdv-transpose-row">
              <div class="tdv-cell tdv-transpose-sticky tdv-transpose-col-label" :style="{ width: `${TRANSPOSE_COL_WIDTH}px` }">
                <span v-if="col.isPk" class="tdv-key-badge tdv-key-pk">{{ HEADER_BADGES.PK }}</span>
                <span v-else-if="col.isUk" class="tdv-key-badge tdv-key-uk">{{ HEADER_BADGES.UK }}</span>
                <span class="tdv-header-name">{{ col.column }}</span>
                <span class="tdv-transpose-type">{{ col.columnType }}</span>
              </div>

              <div
                v-for="(row, rIdx) in rows"
                :key="`${col.column}_${row.uid}`"
                class="tdv-cell tdv-data-cell"
                :class="{
                  'is-selected': isCellSelected(rIdx, cIdx),
                  'is-primary': isPrimaryCell(rIdx, cIdx),
                  'is-editing': isCellEditing(rIdx, cIdx),
                  'is-dirty': !row.isNew && !row.markedDelete && row.dirtyFields[col.column],
                  'is-readonly': isCellReadOnly(row, col),
                  'is-find-match': isFindMatch(rIdx, cIdx),
                  'is-find-active': isActiveFindMatch(rIdx, cIdx)
                }"
                :style="{ width: `${TRANSPOSE_VAL_WIDTH}px` }"
                @mousedown="handleCellMouseDown($event, rIdx, cIdx)"
                @dblclick="startEdit(rIdx, cIdx)"
                @contextmenu.prevent.stop="handleCellContextMenu($event, rIdx, cIdx)"
              >
                <template v-if="editingCell && editingCell.row === rIdx && editingCell.col === cIdx">
                  <input
                    :value="editingValue"
                    type="text"
                    class="tdv-inline-input"
                    @keydown="handleInlineInputKeydown"
                    @beforeinput="handleInlineBeforeInput"
                    @input="handleInlineInput"
                    @blur="commitEdit"
                  />
                </template>
                <template v-else>
                  <span v-if="getCellPlaceholder(row, col, row.values[col.column])" class="tdv-placeholder">
                    {{ getCellPlaceholder(row, col, row.values[col.column]) }}
                  </span>
                  <span v-else class="tdv-cell-text">{{ row.values[col.column] }}</span>
                </template>
              </div>
            </div>
          </div>
          <div v-else-if="!loading" class="tdv-empty">
            <span>{{ $t('zan-wu-shu-ju') }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Bottom DataGrip Status Bar -->
    <DataEditorStatusBar
      :selection-stats="selectionStats"
      :change-breakdown="changeBreakdown"
      :is-read-only="isReadOnly"
      :is-transposed="isTransposed"
      :total-rows-loaded="rows.length"
      :visible-col-count="visibleColumns.length"
      :total-col-count="allColumns.length"
      @preview-dml="handleSubmit"
    />

    <!-- Shared Result Export Modal -->
    <ResultExportModal ref="resultExportModal" :confirm-loading="exportSubmitting" @confirm="handleConfirmExport" />

    <!-- SQL Preview Modal -->
    <CCModal v-model="showSqlModal" :title="$t('sql-yu-ju')" :width="800" :mask-closable="false">
      <a-alert
        v-if="!permission.hasPermission && permission.permissionI18n"
        type="warning"
        show-icon
        :message="permission.permissionI18n"
        class="tdv-permission-alert"
      />
      <ReadOnlyEditor :text="sqlString" :max-height="500" />
      <template #footer>
        <a-button @click="copyText(sqlString)">{{ $t('fu-zhi-sql-yu-ju') }}</a-button>
        <a-button v-if="permission.hasPermission" type="primary" :loading="executeSQLLoading" @click="handleRun">
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
      </template>
    </CCModal>

    <!-- Execution Info Modal -->
    <CCModal
      v-model="showExecuteInfoModal"
      :title="$t('zhi-hang-xin-xi')"
      :width="800"
      :mask-closable="false"
      :closable="false"
      @on-cancel="handleCloseExecuteInfoModal"
    >
      <div class="tdv-execute-info-list">
        <div v-for="(info, index) in executeInfo" :key="index" class="tdv-result-info">
          <div class="tdv-result-first">
            <div :class="`tdv-result-level ${info.success ? 'Info' : 'Error'}`">{{ info.database }}></div>
            <div class="tdv-result-sql">{{ info.queryBody }}</div>
          </div>
          <div class="tdv-result-second">
            <div class="tdv-result-time">[{{ dayjs(info.startTimestamp).format('YYYY-MM-DD HH:mm:ss') }}]</div>
            <div :class="`tdv-result-message ${info.success ? '' : 'is-error'}`">{{ info.message }}</div>
          </div>
        </div>
      </div>
      <template #footer>
        <a-button :loading="executeSQLLoading" @click="handleCloseExecuteInfoModal">
          {{ executeSQLLoading ? $t('zheng-zai-zhi-hang') : $t('guan-bi') }}
        </a-button>
      </template>
    </CCModal>
  </div>
</template>

<style scoped lang="less">
.table-data-view {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  background: var(--bg-primary, #ffffff);
  color: var(--text-primary, #1f2329);
}

.tdv-workspace {
  flex: 1;
  min-height: 0;
  display: flex;
  overflow: hidden;
}

.tdv-grid-stage {
  flex: 1;
  min-width: 0;
  min-height: 0;
  position: relative;
  display: flex;
  flex-direction: column;
}

.tdv-grid-scroll {
  flex: 1;
  min-height: 0;
  overflow: auto;
  position: relative;
  outline: none;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
}

.tdv-grid-inner {
  min-width: 100%;
  position: relative;
}

.tdv-header-row {
  position: sticky;
  top: 0;
  z-index: 6;
  display: flex;
  height: 44px;
  background: var(--bg-secondary, #f8fafc);
  border-bottom: 1px solid var(--border-light, #d1d5db);
}

.tdv-header-group {
  display: flex;
}

.tdv-body {
  position: relative;
  width: 100%;
}

.tdv-data-row {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  border-bottom: 1px solid var(--border-light, #eef0f3);
  background: var(--bg-primary, #ffffff);

  &:hover {
    background: rgba(59, 130, 246, 0.04);
  }

  &.is-new {
    background: rgba(34, 197, 94, 0.12);

    .tdv-rownum-cell {
      box-shadow: inset 3px 0 0 #16a34a;
    }
  }

  &.is-modified {
    .tdv-rownum-cell {
      box-shadow: inset 3px 0 0 #f59e0b;
    }
  }

  &.is-delete {
    background: rgba(239, 68, 68, 0.13);

    .tdv-rownum-cell {
      box-shadow: inset 3px 0 0 #ef4444;
    }

    .tdv-cell-text,
    .tdv-placeholder {
      text-decoration: line-through;
      opacity: 0.72;
    }
  }

  &.is-new-delete {
    background: rgba(249, 115, 22, 0.15);

    .tdv-rownum-cell {
      box-shadow: inset 3px 0 0 #f97316;
    }

    .tdv-cell-text,
    .tdv-placeholder {
      text-decoration: line-through;
      opacity: 0.65;
    }
  }
}

.tdv-cell {
  position: relative;
  box-sizing: border-box;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 8px;
  border-right: 1px solid var(--border-light, #eef0f3);
  overflow: hidden;
  user-select: none;

  &.is-pinned {
    position: sticky;
    z-index: 3;
    background: inherit;
    box-shadow: 2px 0 4px -2px rgba(0, 0, 0, 0.12);
  }
}

.tdv-header-cell.is-pinned {
  z-index: 7;
  background: var(--bg-secondary, #f8fafc);
}

.tdv-rownum-col {
  position: sticky;
  left: 0;
  z-index: 4;
  justify-content: center;
  background: var(--bg-secondary, #f8fafc);
  color: var(--text-secondary, #6b7280);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11px;
  line-height: 1;
}

.tdv-header-row .tdv-rownum-col {
  z-index: 8;
}

.tdv-corner-cell,
.tdv-rownum-cell {
  cursor: pointer;
}

.tdv-header-cell {
  cursor: pointer;
  color: var(--text-primary, #1f2329);
  gap: 4px;
}

.tdv-header-stack {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2px;
  min-width: 0;
  flex: 1;
}

.tdv-header-top {
  display: flex;
  align-items: center;
  gap: 4px;
  min-width: 0;
}

.tdv-header-name {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-weight: 600;
  font-size: 12px;
  line-height: 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.tdv-header-type {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 10px;
  line-height: 12px;
  font-weight: 400;
  color: var(--text-secondary, #9ca3af);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tdv-key-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 9px;
  line-height: 14px;
  height: 14px;
  padding: 0 3px;
  border-radius: 3px;
  font-weight: 700;
  flex-shrink: 0;
}

.tdv-key-pk {
  background: rgba(245, 158, 11, 0.2);
  color: #b45309;
}

.tdv-key-uk {
  background: rgba(59, 130, 246, 0.16);
  color: #1d4ed8;
}

.tdv-required-star {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  line-height: 1;
  color: #ef4444;
  font-weight: 700;
  flex-shrink: 0;
}

.tdv-pin-indicator {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  line-height: 1;
  color: var(--primary-color, #1677ff);
  flex-shrink: 0;
}

.tdv-sort-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  border: none;
  border-radius: 4px;
  background: transparent;
  color: var(--text-tertiary, #9ca3af);
  cursor: pointer;
  padding: 0;
  flex-shrink: 0;
  transition:
    background-color 0.12s ease,
    color 0.12s ease;

  .tdv-sort-glyph {
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
    font-size: 11px;
    line-height: 1;
  }

  .tdv-sort-sup {
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
    font-size: 9px;
    line-height: 1;
    margin-left: 1px;
  }

  &:hover {
    background: var(--bg-hover, rgba(0, 0, 0, 0.05));
    color: var(--text-primary, #1f2329);
  }

  &.is-active {
    color: var(--primary-color, #1677ff);
    background: rgba(62, 207, 142, 0.12);
    font-weight: 700;
  }
}

.tdv-resize-handle {
  position: absolute;
  top: 0;
  right: 0;
  width: 5px;
  height: 100%;
  cursor: col-resize;
  z-index: 2;

  &:hover,
  &.is-resizing {
    background: var(--primary-color, #1677ff);
    opacity: 0.65;
  }
}

.tdv-data-cell {
  cursor: cell;

  &.is-readonly {
    cursor: not-allowed;
  }

  &.is-dirty {
    background: rgba(250, 204, 21, 0.22);
  }

  &.is-find-match {
    background: rgba(251, 191, 36, 0.32);
  }

  &.is-find-active {
    background: rgba(245, 158, 11, 0.55);
  }

  &.is-selected {
    background: rgba(59, 130, 246, 0.1);
  }

  &.is-primary {
    box-shadow: inset 0 0 0 1.5px var(--primary-color, #1677ff);
  }

  &.is-editing {
    padding: 0;
    background: var(--bg-primary, #ffffff) !important;
    box-shadow: inset 0 0 0 1.5px var(--primary-color, #1677ff) !important;
    z-index: 5;
  }

  &:hover .tdv-expand-btn {
    opacity: 1;
  }
}

.tdv-cell-text {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  line-height: 18px;
  color: inherit;
}

.tdv-placeholder {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  line-height: 18px;
  font-style: italic;
  color: var(--text-tertiary, #9ca3af);
}

.tdv-truncated-badge {
  flex-shrink: 0;
  margin-left: 4px;
  padding: 0 4px;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 10px;
  line-height: 16px;
  border-radius: 3px;
  background: var(--bg-secondary, #f3f4f6);
  color: var(--text-secondary, #6b7280);
  border: 1px solid var(--border-light, #e5e7eb);
}

.tdv-expand-btn {
  opacity: 0;
  flex-shrink: 0;
  margin-left: 4px;
  width: 20px;
  height: 20px;
  border: none;
  border-radius: 3px;
  background: transparent;
  color: var(--text-secondary, #6b7280);
  cursor: pointer;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition:
    opacity 0.12s ease,
    background-color 0.12s ease,
    color 0.12s ease;

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 11px;
    line-height: 1;
  }

  :deep(svg) {
    display: block;
  }

  &:hover {
    background: var(--bg-hover, rgba(0, 0, 0, 0.06));
    color: var(--primary-color, #1677ff);
  }
}

.tdv-inline-input {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  margin: 0;
  padding: 0 8px;
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
  border-radius: 0;
  background: transparent !important;
  color: var(--text-primary, #1f2329);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  line-height: 18px;

  &:focus,
  &:focus-visible {
    border: none !important;
    outline: none !important;
    box-shadow: none !important;
  }
}

.tdv-inline-select {
  width: 100%;
  height: 100%;

  :deep(.ant-select-selector) {
    height: 100% !important;
    padding: 0 8px !important;
    border: none !important;
    border-radius: 0 !important;
    background: transparent !important;
    box-shadow: none !important;
    display: flex;
    align-items: center;
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
    font-size: 12px;
  }
}

.tdv-empty {
  padding: 40px 16px;
  text-align: center;
  color: var(--text-secondary, #9ca3af);
}

.tdv-transpose-table {
  display: flex;
  flex-direction: column;
}

.tdv-transpose-header {
  position: sticky;
  top: 0;
  z-index: 6;
  display: flex;
  height: 32px;
  background: var(--bg-secondary, #f8fafc);
  border-bottom: 1px solid var(--border-light, #d1d5db);
}

.tdv-transpose-row {
  display: flex;
  height: 30px;
  border-bottom: 1px solid var(--border-light, #eef0f3);
}

.tdv-transpose-sticky {
  position: sticky;
  left: 0;
  z-index: 4;
  background: var(--bg-secondary, #f8fafc);
  gap: 6px;
}

.tdv-transpose-header .tdv-transpose-sticky {
  z-index: 7;
}

.tdv-transpose-type {
  font-size: 10px;
  color: var(--text-secondary, #9ca3af);
  flex-shrink: 0;
}

.tdv-permission-alert {
  margin-bottom: 12px;
}

.tdv-execute-info-list {
  height: 460px;
  overflow: auto;
}

.tdv-result-info {
  margin-bottom: 8px;
  font-size: 12px;
}

.tdv-result-first,
.tdv-result-second {
  display: flex;
  align-items: flex-start;
  gap: 6px;
}

.tdv-result-level {
  padding: 0 6px;
  border-radius: 3px;
  line-height: 20px;
  color: #fff;
  font-weight: 600;
  flex-shrink: 0;

  &.Info {
    background: #19be6b;
  }

  &.Error {
    background: #ed4014;
  }
}

.tdv-result-sql {
  word-break: break-all;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
}

.tdv-result-time {
  color: var(--text-secondary, #9ca3af);
  flex-shrink: 0;
}

.tdv-result-message {
  flex: 1;
  word-break: break-word;

  &.is-error {
    color: #ed4014;
  }
}
</style>
