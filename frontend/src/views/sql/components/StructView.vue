<script>
import appLogger from '@/utils/logger';
import Loading from 'vue-loading-overlay';
import dayjs from 'dayjs';
import { Modal } from 'ant-design-vue';
import { hasSchema } from '@/utils';
import { ACTION_TYPE } from '@/const';
import copyMixin from '@/mixins/copyMixin';
import { isUndefined } from 'xe-utils';
import browseMixin from '@/mixins/browseMixin';
import { cloneDeep as deepClone, isEqual } from '@/utils/lodash';
import CCModal from '@/components/ui/CCModal.vue';
import TableEditorColumnsPanel from './tableEditor/TableEditorColumnsPanel';
import TableEditorEntityPanel from './tableEditor/TableEditorEntityPanel';
import TableEditorFormPanel from './tableEditor/TableEditorFormPanel';
import { findFieldSchema, initializeFields, normalizeEditorItem, validatePanelItem } from './tableEditor/tableEditorUtils';

const SQL_PREVIEW_PANEL = 'sqlPreview';
const PANEL_PRIORITY = ['tableInfo', 'columns', 'indexes', 'foreignKeys', 'constraints', 'partitions'];

export default {
  name: 'StructView',
  components: {
    CCModal,
    Loading,
    TableEditorColumnsPanel,
    TableEditorEntityPanel,
    TableEditorFormPanel
  },
  mixins: [copyMixin, browseMixin],
  props: {
    handleClickDsStatusIcon: Function,
    storeQueryTabs: Function,
    setActiveKey: Function,
    tab: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      ACTION_TYPE,
      SQL_PREVIEW_PANEL,
      activePanel: 'tableInfo',
      initEditorLoading: false,
      needFetchNewData: true,
      sqlString: '',
      sqlStale: false,
      createSQLLoading: false,
      executeSQLLoading: false,
      showExecuteInfoModal: false,
      sqls: [],
      showTicketModal: false,
      permission: {
        hasPermission: false,
        permissionI18n: ''
      },
      ticketData: {
        ticketTitle: '',
        description: ''
      }
    };
  },
  computed: {
    isEditTable() {
      return this.tab.editorType === ACTION_TYPE.EDIT_TABLE;
    },
    isDirty() {
      if (!this.tab.init) {
        return false;
      }
      return !isEqual(this.tab.originalFormData, this.tab.formData);
    },
    editorTabs() {
      const availablePanels = Object.keys(this.tab.schemaDef || {}).filter((panelKey) => {
        if (panelKey === 'keys' && this.tab.schemaDef.columns) {
          return false;
        }
        return true;
      });
      const orderedPanels = [
        ...PANEL_PRIORITY.filter((panelKey) => availablePanels.includes(panelKey)),
        ...availablePanels.filter((panelKey) => !PANEL_PRIORITY.includes(panelKey))
      ];
      const tabs = orderedPanels.map((panelKey) => ({
        name: panelKey,
        label: this.panelLabel(panelKey),
        errorCount: this.panelErrorCount(panelKey)
      }));
      tabs.push({
        name: SQL_PREVIEW_PANEL,
        label: this.$t('table-editor-sql-preview'),
        errorCount: 0
      });
      return tabs;
    },
    activePanelSchema() {
      const panelSchema = this.tab.schemaDef?.[this.activePanel] || null;
      if (this.activePanel !== 'tableInfo' || !panelSchema) {
        return panelSchema;
      }

      const tableInfoSchema = deepClone(panelSchema);
      const nameFieldSchema = findFieldSchema(tableInfoSchema, 'name');
      if (nameFieldSchema) {
        nameFieldSchema.titleI18N = this.$t('biao-ming');
      }
      return tableInfoSchema;
    },
    ticketRuleValidate() {
      return {
        ticketTitle: [
          {
            required: true,
            message: this.$t('biao-ti-bu-neng-wei-kong'),
            trigger: ['blur', 'change']
          }
        ],
        description: [
          {
            required: true,
            message: this.$t('xu-qiu-miao-shu-bu-neng-wei-kong'),
            trigger: ['blur', 'change']
          }
        ]
      };
    }
  },
  watch: {
    'tab.editorType': {
      async handler(newVal) {
        if (!this.tab.init && newVal) {
          await this.getSchemaAndInitData();
        }
      },
      deep: true,
      immediate: true
    },
    'tab.formData': {
      handler() {
        if (!this.tab.init) {
          return;
        }
        if (this.sqlString) {
          this.sqlStale = true;
        }
        this.storeQueryTabs();
      },
      deep: true
    }
  },
  methods: {
    dayjs,
    hasSchema,
    panelLabel(panelKey) {
      const labelKeys = {
        tableInfo: 'table-editor-general',
        columns: 'table-editor-fields',
        keys: 'table-editor-primary-key',
        foreignKeys: 'table-editor-foreign-keys',
        constraints: 'table-editor-check-constraints',
        indexes: 'table-editor-indexes',
        partitions: 'table-editor-partitions'
      };
      if (labelKeys[panelKey]) {
        return this.$t(labelKeys[panelKey]);
      }
      return this.tab.schemaDef?.[panelKey]?.titleI18N || panelKey;
    },
    databasePath() {
      if (hasSchema(this.tab.dsType)) {
        return `${this.tab.node.INSTANCE.name}.${this.tab.node.CATALOG.name}.${this.tab.node.SCHEMA.name}`;
      }
      return `${this.tab.node.INSTANCE.name}.${this.tab.node.SCHEMA.name}`;
    },
    panelErrorCount(panelKey) {
      return this.collectValidationErrors().filter((error) => error.panelKey === panelKey).length;
    },
    collectValidationErrors() {
      const errors = [];
      Object.keys(this.tab.schemaDef || {}).forEach((panelKey) => {
        const panelSchema = this.tab.schemaDef[panelKey];
        if (panelKey === 'tableInfo') {
          validatePanelItem(panelSchema, this.tab.formData.tableInfo || {}).forEach((error) => {
            errors.push({ panelKey, ...error });
          });
          return;
        }
        const panelItems = this.tab.formData[panelKey] || [];
        panelItems.forEach((item, rowIndex) => {
          validatePanelItem(panelSchema, item).forEach((error) => {
            errors.push({
              panelKey: panelKey === 'keys' && this.tab.schemaDef.columns ? 'columns' : panelKey,
              sourcePanelKey: panelKey,
              rowIndex,
              ...error
            });
          });
        });
        if (panelKey !== 'columns' && findFieldSchema(panelSchema, 'name')) {
          const nameCounts = new Map();
          panelItems.forEach((item) => {
            const name = String(item.name || '').trim();
            if (name) {
              nameCounts.set(name, (nameCounts.get(name) || 0) + 1);
            }
          });
          nameCounts.forEach((count, name) => {
            if (count > 1) {
              errors.push({
                panelKey,
                rowIndex: panelItems.findIndex((item) => String(item.name || '').trim() === name),
                field: 'name',
                label: this.$t('table-editor-duplicate-name', [name])
              });
            }
          });
        }
      });

      const columns = this.tab.formData.columns || [];
      if (this.tab.schemaDef.columns && !columns.length) {
        errors.push({
          panelKey: 'columns',
          label: this.$t('table-editor-at-least-one-column')
        });
      }
      const columnNameCount = new Map();
      columns.forEach((column) => {
        const name = String(column.name || '').trim();
        if (name) {
          columnNameCount.set(name, (columnNameCount.get(name) || 0) + 1);
        }
      });
      columnNameCount.forEach((count, name) => {
        if (count > 1) {
          errors.push({
            panelKey: 'columns',
            rowIndex: columns.findIndex((column) => String(column.name || '').trim() === name),
            field: 'name',
            label: this.$t('table-editor-duplicate-column', [name])
          });
        }
      });
      return errors;
    },
    validateEditorData() {
      const errors = this.collectValidationErrors();
      if (!errors.length) {
        return true;
      }
      const firstError = errors[0];
      this.tab.validationTarget = {
        ...firstError,
        nonce: dayjs().valueOf()
      };
      this.handlePanelChange(firstError.panelKey);
      this.$Message.error(this.$t('table-editor-validation-failed', [this.panelLabel(firstError.panelKey), firstError.label || firstError.field]));
      return false;
    },
    async getSchemaAndInitData() {
      this.tab.init = false;
      this.initEditorLoading = true;
      try {
        if (this.tab.editorType) {
          await this.getSchema();
        }
        if (this.tab.editorType === ACTION_TYPE.EDIT_TABLE) {
          await this.initSchemaEditor();
        }

        this.tab.originalFormData = deepClone(this.tab.formData);
        this.tab.isEditing = false;
        this.tab.init = true;
        this.sqlString = '';
        this.sqls = [];
        this.sqlStale = false;
        this.permission = {
          hasPermission: false,
          permissionI18n: ''
        };
        const requestedPanel = this.tab.activeEditorPanel;
        const initialPanel = this.editorTabs.some((item) => item.name === requestedPanel) ? requestedPanel : this.editorTabs[0]?.name || 'tableInfo';
        this.handlePanelChange(initialPanel);
      } catch (error) {
        appLogger.error(error);
      } finally {
        this.initEditorLoading = false;
      }
    },
    async getSchema() {
      const { node } = this.tab;
      const res = await this.$services.dmEditorTableEditorDef({
        data: {
          levels: this.browseGenLevelsData(node),
          viewMode: this.tab.editorType
        }
      });

      if (!res.success) {
        return;
      }
      const { order, uiPanels } = res.data;
      const schema = {};
      order.forEach((name) => {
        schema[name] = uiPanels[name];
      });
      this.tab.schemaDef = schema;
      this.tab.order = order;
      this.initData();
    },
    initData() {
      const formData = {};
      Object.keys(this.tab.schemaDef).forEach((panelKey) => {
        if (panelKey === 'tableInfo') {
          formData[panelKey] = initializeFields({}, this.tab.schemaDef[panelKey].children);
          return;
        }
        formData[panelKey] = [];
      });
      this.tab.formData = formData;
      this.tab.nodeType = 'tableInfo';
      this.tab.selectedIndex = -1;
      this.tab.selectedNode = null;
    },
    async initSchemaEditor() {
      const { selectedTable: table, node } = this.tab;
      const res = await this.$services.dmEditorTableInitEditor({
        data: {
          levels: this.browseGenLevelsData(node),
          table: table.title,
          refreshCache: true
        }
      });
      if (!res.success) {
        return;
      }
      this.tab.initTableData = deepClone(res.data);
      this.formatSchemaData(res.data);
    },
    formatSchemaData(sourceData) {
      Object.keys(this.tab.schemaDef).forEach((panelKey) => {
        let panelData = sourceData[panelKey];
        if (panelData === undefined || panelData === null) {
          return;
        }
        if (panelKey === 'tableInfo') {
          const normalized = normalizeEditorItem(panelKey, panelData);
          delete normalized.key;
          delete normalized.schema;
          delete normalized.isAdd;
          this.tab.formData[panelKey] = initializeFields(normalized, this.tab.schemaDef[panelKey].children);
          return;
        }
        if ((panelKey === 'keys' || panelKey === 'partitions') && !Array.isArray(panelData)) {
          panelData = [panelData];
        }
        if (!Array.isArray(panelData)) {
          return;
        }
        this.tab.formData[panelKey] = panelData.map((item) => {
          const normalized = normalizeEditorItem(panelKey, item);
          return initializeFields(normalized, this.tab.schemaDef[panelKey].children);
        });
      });
    },
    async handlePanelChange(panelName) {
      this.activePanel = panelName;
      this.tab.activeEditorPanel = panelName;
      if (panelName !== SQL_PREVIEW_PANEL) {
        this.tab.nodeType = panelName;
        return;
      }
      if (!this.createSQLLoading && (!this.sqlString || this.sqlStale)) {
        await this.generateSql({
          switchPanel: false,
          showNoChanges: false
        });
      }
    },
    setOptionsAttr(attr, topItem, data) {
      if (attr.type === 'Options') {
        if (attr.field in topItem) {
          data[attr.field] = topItem[attr.field];
          if (isUndefined(topItem[attr.field])) {
            data[attr.field] = null;
          }
        }
        (attr.options || []).forEach((option) => {
          if (option.value === topItem[attr.field] && option.children) {
            option.children.forEach((child) => {
              if (child.type === 'SelectColumns' || child.type === 'SelectorList') {
                const columns = [];
                (topItem[child.field] || []).forEach((column) => {
                  const indexColumn = {};
                  (child.children || []).forEach((childField) => {
                    indexColumn[childField.field] = column[childField.field];
                  });
                  columns.push(indexColumn);
                });
                data[child.field] = columns;
                return;
              }
              data[child.field] = topItem[child.field];
              this.setOptionsAttr(child, topItem, data);
            });
          }
        });
        return;
      }
      data[attr.field] = topItem[attr.field];
    },
    generateEditData() {
      const tableSchema = {};

      Object.keys(this.tab.schemaDef).forEach((panelKey) => {
        if (panelKey === 'tableInfo') {
          tableSchema[panelKey] = {};
          this.tab.schemaDef[panelKey].children.forEach((attr) => {
            if (attr.type === 'SelectColumns') {
              tableSchema[panelKey][attr.field] = (this.tab.formData[panelKey][attr.field] || []).map((column) => {
                const result = {};
                (attr.children || []).forEach((child) => {
                  result[child.field] = column[child.field];
                });
                return result;
              });
              return;
            }
            this.setOptionsAttr(attr, this.tab.formData[panelKey], tableSchema[panelKey]);
          });
          return;
        }

        tableSchema[panelKey] = [];
        (this.tab.formData[panelKey] || []).forEach((item) => {
          const result = {};
          this.tab.schemaDef[panelKey].children.forEach((attr) => {
            if (attr.type === 'SelectColumns') {
              result[attr.field] = (item[attr.field] || []).map((column) => {
                const selectedColumn = {};
                (attr.children || []).forEach((child) => {
                  selectedColumn[child.field] = column[child.field];
                });
                return selectedColumn;
              });
              return;
            }
            if (attr.type === 'Radios') {
              result[attr.field] = item[attr.field];
              (attr.options || []).forEach((option) => {
                if (item[attr.field] === option.value) {
                  (option.children || []).forEach((child) => {
                    result[child.field] = item[child.field];
                  });
                }
              });
              return;
            }
            this.setOptionsAttr(attr, item, result);
          });
          tableSchema[panelKey].push(result);
        });
      });

      if (Array.isArray(tableSchema.keys)) {
        tableSchema.keys = tableSchema.keys[0] || null;
      }
      if (Array.isArray(tableSchema.partitions)) {
        tableSchema.partitions = tableSchema.partitions[0] || null;
      }
      return tableSchema;
    },
    async generateSql(options = {}) {
      const { switchPanel = true, showNoChanges = true } = options;
      if (!this.validateEditorData()) {
        return false;
      }

      this.createSQLLoading = true;
      const tableSchema = this.generateEditData();
      const { node } = this.tab;
      const data = {
        levels: this.browseGenLevelsData(node),
        table: this.tab.editorType === ACTION_TYPE.EDIT_TABLE ? this.tab.initTableData.tableInfo.name : null,
        tableSchema,
        actionType: this.tab.editorType
      };

      this.storeQueryTabs();
      try {
        const res = await this.$services.dmEditorTableGenerateScript({ data });
        if (!res.success) {
          return false;
        }
        this.permission.hasPermission = res?.permission;
        this.permission.permissionI18n = res?.permissionI18n;
        this.sqls = res.data || [];
        this.sqlString = this.sqls.map((sql) => sql.sql).join('\n');
        this.sqlStale = false;
        if (!this.sqlString && showNoChanges) {
          Modal.info({
            title: this.$t('ti-shi'),
            content: this.$t('biao-jie-gou-wei-jin-hang-xiu-gai')
          });
        }
        if (switchPanel) {
          this.activePanel = SQL_PREVIEW_PANEL;
          this.tab.activeEditorPanel = SQL_PREVIEW_PANEL;
        }
        return true;
      } finally {
        this.createSQLLoading = false;
      }
    },
    handleCancelChanges() {
      if (!this.isDirty) {
        return;
      }
      Modal.confirm({
        title: this.$t('table-editor-cancel-title'),
        content: this.$t('table-editor-cancel-content'),
        okText: this.$t('que-ding'),
        cancelText: this.$t('qu-xiao'),
        onOk: () => {
          this.tab.formData = deepClone(this.tab.originalFormData);
          this.tab.selectedIndex = -1;
          this.tab.selectedNode = null;
          this.sqlString = '';
          this.sqls = [];
          this.sqlStale = false;
          this.handlePanelChange('tableInfo');
          this.storeQueryTabs();
        }
      });
    },
    async handleRun() {
      if (this.sqlStale) {
        this.$Message.warning(this.$t('table-editor-sql-stale'));
        return;
      }
      this.executeSQLLoading = true;
      const sqlList = this.sqls.map((sql) => sql.sql);
      const { node } = this.tab;
      const data = {
        levels: this.browseGenLevelsData(node)
      };
      this.tab.executeInfo = [];
      let database = null;
      if (this.tab.node.levels?.length) {
        const lastLevel = this.tab.node.levels[this.tab.node.levels.length - 1];
        if (this.tab.node[lastLevel]) {
          database = this.tab.node[lastLevel].name;
        }
      }

      let error = false;
      for await (const sql of sqlList) {
        data.sqlList = [sql];
        if (error) {
          this.tab.executeInfo.unshift({
            database,
            sql,
            success: false,
            message: this.$t('wei-zhi-hang')
          });
          continue;
        }
        const res = await this.$services.dmEditorTableScriptExecute({ data });
        if (res.success) {
          this.tab.executeInfo.unshift({
            database,
            ...res.data[0]
          });
          if (!res.data[0].success) {
            error = true;
            this.needFetchNewData = false;
          }
        }
        if (!this.showExecuteInfoModal) {
          this.showExecuteInfoModal = true;
        }
      }
      this.executeSQLLoading = false;
    },
    async handleCloseExecuteInfoModal() {
      this.showExecuteInfoModal = false;
      if (this.needFetchNewData) {
        const tabKey = `${this.tab.prefixKey}.\`${this.tab.formData.tableInfo.name}\``;
        this.tab.key = tabKey;
        this.tab.title = this.$t('thistabformdatatableinfoname-de-biao-jie-gou', [this.tab.formData.tableInfo.name]);
        this.tab.initTableData.tableInfo.name = this.tab.formData.tableInfo.name;
        this.tab.selectedTable.title = this.tab.formData.tableInfo.name;
        this.tab.editorType = ACTION_TYPE.EDIT_TABLE;
        this.setActiveKey(tabKey);
        await this.getSchemaAndInitData();
      }
      this.needFetchNewData = true;
    },
    async submitTicket() {
      try {
        const valid = await this.$refs.ticketContent.validate();
        if (!valid) {
          return;
        }
        const { node } = this.tab;
        const data = {
          dbLevels: this.browseGenLevelsData(node),
          rawSql: this.sqlString,
          description: this.ticketData.description,
          ticketTitle: this.ticketData.ticketTitle,
          force: true
        };
        const res = await this.$services.dmTicketCreate({ data });
        if (!res.success) {
          this.$Message.error(res.msg);
          this.showTicketModal = false;
          return;
        }
        const path = `/ticket/${res.data?.ticketId}`;
        this.$Message.success({
          duration: 2.5,
          render: (h) =>
            h('div', [
              this.$t('ti-jiao-cheng-gong'),
              ', ',
              h(
                'a',
                {
                  style: {
                    position: 'relative',
                    top: '1px',
                    color: 'var(--info-color)',
                    textDecoration: 'underline',
                    cursor: 'pointer'
                  },
                  on: {
                    click: () => this.$router.push(path)
                  }
                },
                this.$t('dian-ji-tiao-zhuan-zhi-gong-dan')
              )
            ])
        });
        this.showTicketModal = false;
        this.resetTicketForm();
      } catch (error) {
        appLogger.error(error);
      }
    },
    resetTicketForm() {
      this.ticketData = {
        ticketTitle: '',
        description: ''
      };
      if (this.$refs.ticketContent) {
        this.$refs.ticketContent.clearValidate();
      }
    },
    handleCloseTicketModal() {
      this.showTicketModal = false;
      this.resetTicketForm();
    },
    handleOpenTicketModal() {
      if (this.sqlStale) {
        this.$Message.warning(this.$t('table-editor-sql-stale'));
        return;
      }
      this.ticketData = {
        ticketTitle: `${this.$t('gong-dan')}${new Date().getTime()}`,
        description: ''
      };
      this.$nextTick(() => {
        if (this.$refs.ticketContent) {
          this.$refs.ticketContent.clearValidate();
        }
      });
      this.showTicketModal = true;
    }
  }
};
</script>

<template>
  <div class="struct-view">
    <loading :active.sync="initEditorLoading" :is-full-page="false" />

    <div class="table-editor-header">
      <div class="table-editor-context">
        <CustomIcon size="18" right-margin :type="tab.node.INSTANCE.attr.dsType" />
        <div class="table-editor-context__text">
          <span class="table-editor-context__path">{{ databasePath() }}</span>
          <span v-if="tab.formData.tableInfo?.name" class="table-editor-context__table">/ {{ tab.formData.tableInfo.name }}</span>
          <span v-if="isDirty" class="table-editor-dirty">{{ $t('table-editor-unsaved') }}</span>
        </div>
      </div>
    </div>

    <div v-if="tab.init" class="table-editor-workspace">
      <nav class="table-editor-sidebar" :aria-label="$t('table-editor-navigation')">
        <button
          v-for="tabItem in editorTabs"
          :key="tabItem.name"
          type="button"
          class="table-editor-sidebar__item"
          :class="{
            'table-editor-sidebar__item--active': activePanel === tabItem.name
          }"
          :aria-current="activePanel === tabItem.name ? 'page' : undefined"
          @click="handlePanelChange(tabItem.name)"
        >
          <span class="table-editor-sidebar__label">{{ tabItem.label }}</span>
          <span v-if="tabItem.errorCount" class="table-editor-tab-error">{{ tabItem.errorCount }}</span>
        </button>
      </nav>

      <main class="table-editor-main">
        <div class="table-editor-body">
          <TableEditorFormPanel
            v-if="activePanel === 'tableInfo' && activePanelSchema"
            :tab="tab"
            panel-key="tableInfo"
            :panel-schema="activePanelSchema"
          />

          <TableEditorColumnsPanel
            v-else-if="activePanel === 'columns' && activePanelSchema"
            :tab="tab"
            :panel-schema="activePanelSchema"
            :keys-schema="tab.schemaDef.keys"
          />

          <TableEditorEntityPanel
            v-else-if="activePanel !== SQL_PREVIEW_PANEL && activePanelSchema"
            :key="activePanel"
            :tab="tab"
            :panel-key="activePanel"
            :panel-schema="activePanelSchema"
          />

          <div v-else-if="activePanel === SQL_PREVIEW_PANEL" class="sql-preview-panel">
            <div v-if="sqlStale" class="sql-preview-notice">
              <span>{{ $t('table-editor-sql-stale') }}</span>
              <a-button type="link" :loading="createSQLLoading" @click="generateSql({ switchPanel: false })">
                {{ $t('table-editor-regenerate-sql') }}
              </a-button>
            </div>
            <div v-if="sqlString" class="sql-preview-code">
              <pre>{{ sqlString }}</pre>
            </div>
            <div v-else class="sql-preview-empty">
              <div>{{ $t('table-editor-no-sql-preview') }}</div>
              <a-button type="primary" :loading="createSQLLoading" @click="generateSql({ switchPanel: false })">
                {{ $t('table-editor-generate-sql') }}
              </a-button>
            </div>
            <div v-if="sqlString" class="sql-preview-actions">
              <a-button @click="copyText(sqlString)">{{ $t('fu-zhi-sql-yu-ju') }}</a-button>
              <a-button v-if="permission.hasPermission" type="primary" :disabled="sqlStale" :loading="executeSQLLoading" @click="handleRun">
                {{ $t('li-ji-zhi-hang') }}
              </a-button>
              <a-button v-else type="primary" :disabled="sqlStale" @click="handleOpenTicketModal">
                {{ $t('ti-jiao-gong-dan') }}
              </a-button>
            </div>
          </div>
        </div>

        <footer v-if="activePanel !== SQL_PREVIEW_PANEL" class="table-editor-footer">
          <a-button :disabled="!isDirty || createSQLLoading" @click="handleCancelChanges">
            {{ $t('qu-xiao') }}
          </a-button>
          <a-button type="primary" :loading="createSQLLoading" @click="generateSql()">
            {{ createSQLLoading ? $t('zheng-zai-sheng-cheng-sql-yu-ju') : $t('table-editor-preview-changes') }}
          </a-button>
        </footer>
      </main>
    </div>

    <CCModal
      v-model="showExecuteInfoModal"
      :title="$t('zhi-hang-xin-xi')"
      :width="800"
      :mask-closable="false"
      :keyboard="false"
      @on-cancel="handleCloseExecuteInfoModal"
    >
      <div class="execute-info-list">
        <div v-for="(info, index) in tab.executeInfo" :key="index" class="result-info">
          <div class="result-info__first">
            <div :class="`result-info__level ${info.success ? 'Info' : 'Error'}`">{{ info.database }}></div>
            <div class="result-info__sql">{{ info.sql }}</div>
          </div>
          <div class="result-info__second">
            <div class="result-info__time">[{{ dayjs(info.startTimestamp).format('YYYY-MM-DD HH:mm:ss') }}]</div>
            <div :class="{ 'result-info__message--error': !info.success }">{{ info.message }}</div>
          </div>
        </div>
      </div>
      <template #footer>
        <a-button @click="handleCloseExecuteInfoModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>

    <CCModal v-model="showTicketModal" :title="$t('ti-jiao-gong-dan')" @on-cancel="handleCloseTicketModal">
      <a-form :model="ticketData" :rules="ticketRuleValidate" ref="ticketContent" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
        <a-form-item :label="$t('biao-ti')" prop="ticketTitle">
          <a-input v-model:value="ticketData.ticketTitle" />
        </a-form-item>
        <a-form-item :label="$t('xu-qiu-miao-shu')" prop="description">
          <a-textarea v-model:value="ticketData.description" :rows="4" />
        </a-form-item>
      </a-form>
      <template #footer>
        <a-button @click="handleCloseTicketModal">{{ $t('qu-xiao') }}</a-button>
        <a-button type="primary" @click="submitTicket">{{ $t('que-ding') }}</a-button>
      </template>
    </CCModal>
  </div>
</template>

<style scoped lang="less">
.struct-view {
  position: relative;
  display: flex;
  width: 100%;
  height: 100%;
  min-height: 0;
  flex-direction: column;
  background: var(--bg-primary);
}

.table-editor-header {
  display: flex;
  min-height: 52px;
  flex: 0 0 auto;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 8px 24px;
  border-bottom: 1px solid var(--border-light);
}

.table-editor-context {
  display: flex;
  min-width: 0;
  align-items: center;
}

.table-editor-context__text {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 6px;
  color: var(--text-primary);
}

.table-editor-context__path,
.table-editor-context__table {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.table-editor-context__path {
  font-weight: 500;
}

.table-editor-context__table {
  color: var(--text-secondary);
}

.table-editor-dirty {
  flex: 0 0 auto;
  color: var(--warning-color);
  font-size: 12px;
}

.table-editor-workspace {
  display: grid;
  flex: 1;
  min-width: 0;
  min-height: 0;
  grid-template-columns: 184px minmax(0, 1fr);
}

.table-editor-sidebar {
  display: flex;
  min-width: 0;
  min-height: 0;
  flex-direction: column;
  gap: 6px;
  overflow-y: auto;
  padding: 16px 12px;
  border-right: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.table-editor-sidebar__item {
  position: relative;
  display: flex;
  width: 100%;
  min-height: 42px;
  align-items: center;
  gap: 6px;
  padding: 0 12px;
  border: 0;
  border-radius: 6px;
  background: transparent;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 14px;
  text-align: left;
  transition:
    background-color 0.2s,
    border-color 0.2s,
    color 0.2s;
}

.table-editor-sidebar__item:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.table-editor-sidebar__item--active {
  background: var(--bg-hover);
  color: var(--primary-color);
  font-weight: 500;
}

.table-editor-sidebar__item--active::before {
  position: absolute;
  top: 8px;
  bottom: 8px;
  left: 0;
  width: 3px;
  border-radius: 2px;
  background: var(--primary-color);
  content: '';
}

.table-editor-sidebar__label {
  min-width: 0;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.table-editor-tab-error {
  display: inline-flex;
  min-width: 18px;
  height: 18px;
  align-items: center;
  justify-content: center;
  margin-left: 6px;
  padding: 0 5px;
  border-radius: 9px;
  background: var(--error-color);
  color: #fff;
  font-size: 11px;
}

.table-editor-main {
  display: flex;
  min-width: 0;
  min-height: 0;
  flex-direction: column;
  background: var(--bg-primary);
}

.table-editor-body {
  display: flex;
  flex: 1;
  min-width: 0;
  min-height: 0;
  overflow: auto;
}

.table-editor-footer {
  display: flex;
  box-sizing: border-box;
  height: 64px;
  min-height: 64px;
  max-height: 64px;
  flex: 0 0 64px;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 12px 24px;
  border-top: 1px solid var(--border-light);
  background: var(--bg-primary);
}

.table-editor-footer :deep(.ant-btn) {
  height: 36px;
  min-height: 36px;
  padding: 0 16px;
  line-height: 34px;
}

.sql-preview-panel {
  display: flex;
  flex: 1;
  min-height: 0;
  flex-direction: column;
  padding: 20px 24px;
}

.sql-preview-notice {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 12px;
  padding: 10px 12px;
  border-radius: 6px;
  background: var(--bg-secondary);
  color: var(--warning-color);
}

.sql-preview-code {
  flex: 1;
  min-height: 0;
  overflow: auto;
  border: 1px solid var(--border-primary);
  border-radius: 6px;
  background: var(--bg-secondary);
}

.sql-preview-code pre {
  min-width: max-content;
  margin: 0;
  padding: 16px;
  color: var(--text-primary);
  font-family: ui-monospace, Menlo, Monaco, Consolas, 'Liberation Mono', monospace;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
}

.sql-preview-empty {
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 16px;
  color: var(--text-secondary);
}

.sql-preview-actions {
  display: flex;
  flex: 0 0 auto;
  justify-content: center;
  gap: 8px;
  padding-top: 16px;
}

.execute-info-list {
  max-height: 600px;
  overflow: auto;
}

.result-info {
  margin-bottom: 8px;
  font-weight: 500;
}

.result-info__first,
.result-info__second {
  display: flex;
}

.result-info__level {
  height: 20px;
  margin-right: 4px;
  padding: 0 6px;
  border-radius: 4px;
  color: #fff;
}

.result-info__level.Info {
  background: var(--success-color);
}

.result-info__level.Error {
  background: var(--error-color);
}

.result-info__sql {
  flex: 1;
  min-width: 0;
}

.result-info__time {
  margin-right: 6px;
  color: var(--text-tertiary);
}

.result-info__message--error {
  color: var(--error-color);
}

@media (max-width: 900px) {
  .table-editor-workspace {
    grid-template-columns: minmax(0, 1fr);
    grid-template-rows: auto minmax(0, 1fr);
  }

  .table-editor-sidebar {
    flex-direction: row;
    gap: 8px;
    overflow-x: auto;
    padding: 8px 12px;
    border-right: 0;
    border-bottom: 1px solid var(--border-light);
    scrollbar-width: none;
  }

  .table-editor-sidebar::-webkit-scrollbar {
    display: none;
  }

  .table-editor-sidebar__item {
    width: auto;
    min-width: 128px;
    flex: 0 0 auto;
    justify-content: center;
  }

  .table-editor-sidebar__item--active::before {
    top: auto;
    right: 12px;
    bottom: -9px;
    left: 12px;
    width: auto;
    height: 3px;
    border-radius: 2px 2px 0 0;
  }
}

@media (max-width: 768px) {
  .table-editor-header {
    padding: 12px 16px;
  }

  .table-editor-context__path {
    max-width: 55vw;
  }

  .table-editor-footer {
    height: 58px;
    min-height: 58px;
    max-height: 58px;
    flex-basis: 58px;
    padding: 10px 16px;
  }

  .table-editor-footer :deep(.ant-btn) {
    height: 34px;
    min-height: 34px;
    line-height: 32px;
  }
}
</style>
