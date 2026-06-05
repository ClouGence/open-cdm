<script>
import Loading from 'vue-loading-overlay';
import dayjs from 'dayjs';
import { Modal } from 'ant-design-vue';
import { hasSchema } from '@/utils';
import CreateTableItem from '@/components/modal/CreateTableItem';
import { ACTION_TYPE } from '@/const';
import copyMixin from '@/mixins/copyMixin';
import { isUndefined } from 'xe-utils';
import browseMixin from '@/mixins/browseMixin';
import deepClone from 'lodash.clonedeep';
import CCModal from '@/components/ui/CCModal.vue';

export default {
  name: 'StructView',
  components: {
    CCModal,
    CreateTableItem,
    Loading
  },
  mixins: [copyMixin, browseMixin],
  props: {
    handleClickDsStatusIcon: Function,
    storeQueryTabs: Function,
    setActiveKey: Function,
    tab: {
      type: Object,
      default: () => {}
    }
  },
  computed: {
    isEditTable() {
      return this.tab.editorType === ACTION_TYPE.EDIT_TABLE;
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
        if (this.tab.init) {
          this.storeQueryTabs();
        }
      },
      deep: true
    }
  },
  data() {
    return {
      initEditorLoading: false,
      needFetchNewData: true,
      showItem: true,
      ACTION_TYPE,
      sqlString: '',
      createSQLLoading: false,
      refreshLoading: false,
      executeSQLLoading: false,
      showExecuteInfoModal: false,
      sqls: [],
      showTicketModal: false,
      showSqlModal: false,
      permission: {
        hasPermission: false,
        permissionI18n: ''
      },
      ticketData: {
        ticketTitle: '',
        description: ''
      },
      replaceFields: {
        title: 'name'
      },
      hoveredNodeKey: null
    };
  },
  methods: {
    dayjs,
    hasSchema,
    async getSchemaAndInitData() {
      console.log('getSchemaAndInitData');
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
      } catch (e) {
        console.error(e);
      } finally {
        this.initEditorLoading = false;
      }
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
    async handleRun() {
      this.executeSQLLoading = true;
      const sqlList = this.sqls.map((sql) => sql.sql);
      const { node } = this.tab;
      const data = {
        levels: this.browseGenLevelsData(node)
      };
      this.tab.executeInfo = [];
      let database = null;
      if (this.tab.node.levels && this.tab.node.levels.length) {
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
        } else {
          const res = await this.$services.dmEditorTableScriptExecute({
            data
          });

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
        }

        // this.$forceUpdate();
        if (this.showSqlModal) {
          this.showSqlModal = false;
        }
        if (!this.showExecuteInfoModal) {
          this.showExecuteInfoModal = true;
        }
      }

      this.createSQLLoading = false;
      this.executeSQLLoading = false;
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
      if (res.success) {
        console.log('initSchemaEditor');
        this.tab.initTableData = deepClone(res.data);
        this.formatSchemaData(res.data);
      }
    },
    formatSchemaData(data) {
      Object.keys(data).forEach((key) => {
        if (key && this.tab.schemaDef[key] && data[key]) {
          if (key === 'keys' || key === 'partitions') {
            data[key] = [data[key]];
          }
          if (Array.isArray(data[key])) {
            console.log(key);
            this.tab.treeData.forEach((node) => {
              if (node.schema === key) {
                data[key].forEach((item) => {
                  const column = { scopedSlots: { title: 'custom' } };
                  Object.keys(item).forEach((attr) => {
                    column[attr] = item[attr];
                    if (column[attr] === 'false') {
                      column[attr] = false;
                    }
                    if (column[attr] === 'true') {
                      column[attr] = true;
                    }
                    if (Array.isArray(item[attr])) {
                      column[attr] = item[attr].map((col) => {
                        const indexColumn = {};
                        Object.keys(col).forEach((colKey) => {
                          indexColumn[colKey] = col[colKey];
                        });
                        indexColumn.key = `${col.name}-${dayjs().valueOf()}`;
                        console.log(indexColumn, attr);
                        return indexColumn;
                      });
                    }
                  });
                  column.key = `${key}-${column.name}-${dayjs().valueOf()}`;
                  column.schema = key;
                  column.showForm = true;
                  column.canDelete = true;
                  column.canAdd = true;
                  column.parentKey = node.key;
                  node.num++;
                  if (node.schema === 'keys' || node.schema === 'partitions') {
                    column.canAdd = false;
                    node.canAdd = false;
                  }
                  this.tab.formData[key].push(column);
                  node.children.push(column);
                });
                if (!this.tab.expandedKeys.includes(node.key)) {
                  this.tab.expandedKeys.push(node.key);
                }
              }
            });
          } else {
            const item = data[key];
            Object.keys(item).forEach((attr) => {
              this.tab.formData[key][attr] = item[attr];
              if (item[attr] === 'false') {
                this.tab.formData[key][attr] = false;
              }
              if (item[attr] === 'true') {
                this.tab.formData[key][attr] = true;
              }

              if (Array.isArray(item[attr])) {
                this.tab.formData[key][attr] = item[attr].map((col) => {
                  const indexColumn = {};
                  Object.keys(col).forEach((colKey) => {
                    indexColumn[colKey] = col[colKey];
                  });
                  indexColumn.key = `${col.name}-${dayjs().valueOf()}`;
                  return indexColumn;
                });
              }
            });
          }
        }
      });

      this.handleSelect([this.tab.treeData[0].key], {
        node: {
          pos: '0-0',
          dataRef: {
            schema: 'tableInfo',
            showForm: true,
            canAdd: false,
            canDelete: false
          }
        }
      });
    },
    async getSchema() {
      const { node } = this.tab;
      const res = await this.$services.dmEditorTableEditorDef({
        data: {
          levels: this.browseGenLevelsData(node),
          viewMode: this.tab.editorType
        }
      });

      if (res.success) {
        console.log('get schema');
        const { order, uiPanels } = res.data;
        const schema = {};
        order.forEach((name) => {
          schema[name] = uiPanels[name];
        });
        this.tab.schemaDef = schema;
        this.tab.order = order;
        this.initData();
      }
    },
    handleAddFromNode(item) {
      // 先选中该节点
      const schemaIndex = this.tab.order.findIndex((e) => e === item.schema);
      let pos = `0-${schemaIndex}`;

      // 如果是子节点，需要找到它在父节点中的索引
      if (item.parentKey) {
        const parentNode = this.tab.treeData.find((node) => node.key === item.parentKey);
        if (parentNode) {
          const childIndex = parentNode.children.findIndex((child) => child.key === item.key);
          pos = `0-${this.tab.order.findIndex((e) => e === item.schema)}-${childIndex}`;
        }
      }

      this.handleSelect([item.key], {
        node: {
          pos,
          dataRef: item
        }
      });

      // 然后执行添加操作
      this.$nextTick(() => {
        this.handleAdd();
      });
    },
    handleDeleteFromNode(item) {
      // 先选中该节点
      const schemaIndex = this.tab.order.findIndex((e) => e === item.schema);
      const parentNode = this.tab.treeData.find((node) => node.key === item.parentKey);
      if (parentNode) {
        const childIndex = parentNode.children.findIndex((child) => child.key === item.key);
        this.handleSelect([item.key], {
          node: {
            pos: `0-${schemaIndex}-${childIndex}`,
            dataRef: item
          }
        });

        // 然后执行删除操作
        this.$nextTick(() => {
          this.handleDelete();
        });
      }
    },
    handleAdd() {
      const formData = this.tab.formData;
      const treeData = this.tab.treeData;
      const schema = {
        scopedSlots: { title: 'custom' }
      };
      if (!this.tab.formData[this.tab.nodeType]) {
        this.tab.formData[this.tab.nodeType] = [];
      }

      this.tab.schemaDef[this.tab.nodeType].children.forEach((column) => {
        schema[column.field] = column.defaultVal;
      });

      schema.key = `${this.tab.nodeType}-${dayjs().valueOf()}`;
      schema.schema = this.tab.nodeType;
      schema.showForm = true;
      schema.canDelete = true;
      schema.canAdd = true;
      schema.isAdd = true;
      if (this.tab.selectedNode.dataRef.schema === 'keys' || this.tab.selectedNode.dataRef.schema === 'partitions') {
        schema.canAdd = false;
        this.tab.selectedNode.dataRef.canAdd = false;
      }

      treeData.forEach((item) => {
        if (item.key === this.tab.selectedNode.dataRef.key || item.key === this.tab.selectedNode.dataRef.parentKey) {
          schema.name = schema.name ? (item.num ? `${schema.name}_${item.num}` : schema.name) : '';
          item.num++;
          schema.parentKey = item.key;
          schema.columns = [];
          item.children.push(schema);
          formData[this.tab.nodeType].push(schema);
        }
      });

      if (!this.tab.expandedKeys.includes(this.tab.selectedNode.dataRef.key)) {
        this.tab.expandedKeys.push(this.tab.selectedNode.dataRef.key);
      }

      this.tab.treeData = treeData;
      this.tab.formData = formData;

      const schemaIndex = this.tab.order.findIndex((e) => e === this.tab.nodeType);

      this.handleSelect([schema.key], {
        node: {
          pos: `0-${schemaIndex}-${formData[this.tab.nodeType].length - 1}`,
          dataRef: {
            ...schema
          }
        }
      });

      // 自动聚焦name
      this.$nextTick(() => {
        this.focusAndSelectNameField();
      });
    },
    focusAndSelectNameField() {
      this.$nextTick(() => {
        const nameLabels = this.$el.querySelectorAll('div');
        nameLabels.forEach((label) => {
          if (label.textContent && label.textContent.includes('名称')) {
            const parentContainer = label.parentElement;
            if (parentContainer) {
              const input = parentContainer.querySelector('input[type="text"]');
              if (input) {
                input.focus();
                input.select();
              }
            }
          }
        });
      });
    },
    handleDelete() {
      this.tab.treeData.forEach((node) => {
        if (node.children) {
          let index = node.children.findIndex((child) => child.key === this.tab.selectedNode.dataRef.key);
          if (index > -1) {
            node.canAdd = true;
            node.children.splice(index, 1);
            this.tab.formData[this.tab.nodeType].splice(index, 1);
            if (this.tab.formData[this.tab.nodeType].length) {
              if (this.tab.formData[this.tab.nodeType].length === index) {
                index--;
              }

              const schemaIndex = this.tab.order.findIndex((e) => e === this.tab.nodeType);
              const nextNode = this.tab.formData[this.tab.nodeType][index];

              this.handleSelect([nextNode.key], {
                node: {
                  pos: `0-${schemaIndex}-${index}`,
                  dataRef: {
                    ...nextNode
                  }
                }
              });
            } else {
              this.clearRightPanel();
            }
          }
        }
      });
    },
    clearRightPanel() {
      // 清空右边面板
      this.showItem = false;
      this.tab.selectedKeys = [];
      this.tab.selectedNode = null;
      this.tab.selectedIndex = -1;
      this.tab.selectedSchema = [];
      this.tab.nodeType = null;
    },
    initData() {
      const formData = {};
      const treeData = [];

      Object.keys(this.tab.schemaDef).forEach((key) => {
        const treeKey = `${key}-${dayjs().valueOf()}`;
        if (key === 'tableInfo') {
          formData[key] = {};
          this.tab.nodeType = key;
          treeData.push({
            name: this.tab.schemaDef[key].titleI18N,
            key: treeKey,
            schema: key,
            showForm: true,
            canDelete: false,
            canAdd: false,
            scopedSlots: { title: 'custom' }
          });
          this.tab.expandedKeys.push(treeKey);
        } else {
          formData[key] = [];
          treeData.push({
            name: this.tab.schemaDef[key].titleI18N,
            key: treeKey,
            schema: key,
            showForm: false,
            num: 0,
            canDelete: false,
            canAdd: true,
            scopedSlots: { title: 'custom' },
            children: []
          });
        }
      });

      this.tab.formData = formData;
      this.tab.treeData = treeData;

      this.handleSelect([treeData[0].key], {
        node: {
          pos: '0-0',
          dataRef: {
            schema: 'tableInfo',
            showForm: true,
            canAdd: false,
            canDelete: false
          }
        }
      });
    },
    handleSelect(selectedKeys, e) {
      if (!selectedKeys.length) {
        return;
      }
      this.showItem = false;
      this.tab.selectedKeys = selectedKeys;
      this.tab.selectedNode = {
        key: selectedKeys[0],
        dataRef: e.node.dataRef
      };
      const { schema, showForm } = e.node.dataRef;
      this.tab.nodeType = schema;
      if (!showForm) {
        this.tab.selectedIndex = -1;
        this.tab.selectedSchema = [];
        return;
      }
      const posArr = e.node.pos.split('-');
      this.tab.selectedIndex = parseInt(posArr[posArr.length - 1], 10);
      const selectedData =
        this.tab.nodeType === 'tableInfo' ? this.tab.formData[schema] : this.tab.formData[this.tab.nodeType][this.tab.selectedIndex];
      const selectedSchema = deepClone(this.tab.schemaDef[schema].children);
      const generateOptionSchema = (item) => {
        if (item.type === 'Options' && selectedData) {
          const optionData = selectedData[item.field];
          item.options.forEach((option) => {
            if (option.value === optionData && option.children && option.children.length) {
              item.children = option.children;
              option.children.forEach((child) => {
                if (!(child.field in selectedData)) {
                  selectedData[child.field] = child.defaultVal;
                }
                generateOptionSchema(child);
              });
            }
          });
        }
        if (!(item.field in selectedData)) {
          selectedData[item.field] = item.defaultVal;
        }
      };
      selectedSchema.forEach((item) => {
        generateOptionSchema(item);
      });
      this.tab.selectedSchema = selectedSchema;
      this.$nextTick(() => {
        this.showItem = true;
      });
    },
    setOptionsAttr(attr, topItem, data) {
      console.log(attr.field, topItem);
      if (attr.type === 'Options') {
        const attrOptionsLength = attr.options.length;
        if (attr.field in topItem) {
          data[attr.field] = topItem[attr.field];
          if (isUndefined(topItem[attr.field])) {
            data[attr.field] = null;
          }
        }
        if (attrOptionsLength) {
          attr.options.forEach((option) => {
            if (option.value === topItem[attr.field] && option.children) {
              option.children.forEach((child) => {
                if (child.type === 'SelectColumns' || child.type === 'SelectorList') {
                  const columns = [];
                  if (topItem[child.field]) {
                    topItem[child.field].forEach((col) => {
                      const indexColumn = {};
                      child.children.forEach((child2) => {
                        indexColumn[child2.field] = col[child2.field];
                      });
                      columns.push(indexColumn);
                    });
                  }
                  data[child.field] = columns;
                } else {
                  data[child.field] = topItem[child.field];
                  this.setOptionsAttr(child, topItem, data);
                }
              });
            }
          });
        }
      } else {
        data[attr.field] = topItem[attr.field];
      }
    },
    generateEditData() {
      const tableSchema = {};

      Object.keys(this.tab.schemaDef).forEach((key) => {
        if (key === 'tableInfo') {
          this.tab.schemaDef[key].children.forEach((attr) => {
            if (!tableSchema[key]) {
              tableSchema[key] = {};
            }
            if (attr.type === 'SelectColumns' && this.tab.formData[key][attr.field]) {
              const columns = [];
              this.tab.formData[key][attr.field].forEach((col) => {
                const indexColumn = {};
                attr.children.forEach((child) => {
                  indexColumn[child.field] = col[child.field];
                });
                columns.push(indexColumn);
              });
              tableSchema[key][attr.field] = columns;
            } else {
              this.setOptionsAttr(attr, this.tab.formData[key], tableSchema[key]);
            }
          });
        } else {
          console.log(this.tab.formData[key], key);
          this.tab.formData[key].forEach((item) => {
            if (!tableSchema[key]) {
              tableSchema[key] = [];
            }

            const column = {};

            this.tab.schemaDef[key].children.forEach((attr) => {
              if (attr.type === 'SelectColumns') {
                column[attr.field] = item[attr.field];
                const columns = [];
                item[attr.field].forEach((col) => {
                  const indexColumn = {};
                  attr.children.forEach((child) => {
                    indexColumn[child.field] = col[child.field];
                  });
                  columns.push(indexColumn);
                });
                column[attr.field] = columns;
              } else if (attr.type === 'Radios') {
                column[attr.field] = item[attr.field];
                if (attr.options) {
                  attr.options.forEach((option) => {
                    if (item[attr.field] === option.value) {
                      if (option.children) {
                        option.children.forEach((child) => {
                          column[child.field] = item[child.field];
                        });
                      }
                    }
                  });
                }
              } else {
                this.setOptionsAttr(attr, item, column);
              }
            });

            tableSchema[key].push(column);
          });
        }
      });

      if (tableSchema.keys && tableSchema.keys.length) {
        tableSchema.keys = tableSchema.keys[0];
      }

      if (tableSchema.partitions && tableSchema.partitions.length) {
        tableSchema.partitions = tableSchema.partitions[0];
      }

      return tableSchema;
    },
    async handleCreateTable(type = 'create') {
      console.log('handleCreateTable');
      if (type === 'refresh') {
        this.refreshLoading = true;
      } else {
        this.createSQLLoading = true;
      }
      const tableSchema = this.generateEditData();
      const { node } = this.tab;

      const data = {
        levels: this.browseGenLevelsData(node),
        table: this.tab.editorType === ACTION_TYPE.EDIT_TABLE ? this.tab.initTableData.tableInfo.name : null,
        tableSchema,
        actionType: this.tab.editorType
      };

      this.storeQueryTabs();

      const res = await this.$services.dmEditorTableGenerateScript({ data });
      if (res.success) {
        this.permission.hasPermission = res?.permission;
        this.permission.permissionI18n = res?.permissionI18n;
      }
      if (type === 'refresh') {
        this.refreshLoading = false;
      } else {
        this.createSQLLoading = false;
      }

      if (res.success) {
        this.sqls = res.data;
        const sqlList = [];
        res.data.forEach((sql) => {
          sqlList.push(sql.sql);
        });
        if (sqlList.length) {
          if (type === 'refresh') {
            Modal.confirm({
              title: this.$t('ti-shi'),
              content: this.$t('shua-xin-hui-diu-shi-xiu-gai-que-ding-yao-shua-xin-ma'),
              onOk: async () => this.getSchemaAndInitData()
            });
          } else {
            this.sqlString = sqlList.join('\n');
            this.showSqlModal = true;
          }
        } else {
          if (type === 'refresh') {
            await this.getSchemaAndInitData();
          } else {
            Modal.info({
              title: this.$t('ti-shi'),
              content: this.$t('biao-jie-gou-wei-jin-hang-xiu-gai')
            });
          }
        }
      }
    },
    handleExpandTree(expandedKeys, e) {
      const { eventKey } = e.node;
      if (e.expanded) {
        expandedKeys.push(eventKey);
      } else {
        expandedKeys = expandedKeys.filter((key) => key !== eventKey);
      }
      this.tab.expandedKeys = expandedKeys;
    },
    handleRefresh() {
      const tableScehma = this.generateEditData();
      console.log(tableScehma);
    },
    async submitTicket() {
      try {
        const valid = await this.$refs.ticketContent.validate();
        if (valid) {
          const { node } = this.tab;
          const dbLevels = this.browseGenLevelsData(node);
          const data = {
            dbLevels,
            rawSql: this.sqlString,
            description: this.ticketData.description,
            ticketTitle: this.ticketData.ticketTitle,
            force: true
          };
          const res = await this.$services.dmTicketCreate({ data });
          if (res.success) {
            const path = `/ticket/${res.data?.ticketId}`;
            this.$Message.success({
              duration: 2.5,
              render: (h) => {
                return h('div', [
                  this.$t('ti-jiao-cheng-gong'),
                  ', ',
                  h(
                    'a',
                    {
                      style: {
                        position: 'relative',
                        top: '1px',
                        color: '#2d8cf0',
                        textDecoration: 'underline',
                        cursor: 'pointer'
                      },
                      on: {
                        click: () => {
                          this.$router.push(path);
                        }
                      }
                    },
                    this.$t('dian-ji-tiao-zhuan-zhi-gong-dan')
                  )
                ]);
              }
            });
            this.showSqlModal = false;
            this.showTicketModal = false;
            this.resetTicketForm();
          } else {
            this.$Message.error(res.msg);
            this.showTicketModal = false;
          }
        }
      } catch (error) {
        console.error(error);
      }
    },
    resetTicketForm() {
      // 重置表单数据
      this.ticketData = {
        ticketTitle: '',
        description: ''
      };
      // 清除表单校验状态
      if (this.$refs.ticketContent) {
        this.$refs.ticketContent.clearValidate();
      }
    },
    handleCloseTicketModal() {
      this.showTicketModal = false;
      this.resetTicketForm();
    },
    handleOpenTicketModal() {
      // 重置表单数据
      this.ticketData = {
        ticketTitle: `${this.$t('gong-dan')}${new Date().getTime()}`,
        description: ''
      };
      // 清除之前的校验状态
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
    <div class="header" style="font-weight: bold">
      <div class="title" style="display: flex; align-items: center">
        <CustomIcon size="16" rightMargin :type="tab.node.INSTANCE.attr.dsType" />
        {{
          hasSchema(tab.dsType)
            ? `${tab.node.INSTANCE.name}.${tab.node.CATALOG.name}.${tab.node.SCHEMA.name}`
            : `${tab.node.INSTANCE.name}.${tab.node.SCHEMA.name}`
        }}
      </div>
      <div class="op">
        <a-button
          type="primary"
          @click="handleCreateTable"
          :loading="createSQLLoading"
          size="small"
          style="margin-right: 10px"
          :disabled="refreshLoading"
        >
          {{ createSQLLoading ? $t('zheng-zai-sheng-cheng-sql-yu-ju') : $t('bao-cun') }}
        </a-button>
        <a-button size="small" style="width: 40px" @click="handleCreateTable('refresh')" :loading="refreshLoading" :disabled="createSQLLoading">
          <CustomIcon type="icon-v2-Refresh" v-if="!refreshLoading" />
        </a-button>
      </div>
    </div>
    <div class="struct-view-content">
      <loading :active.sync="initEditorLoading" :is-full-page="false"></loading>
      <div class="left">
        <div class="struct-resize" />
        <div style="flex: 1; overflow: auto">
          <a-tree
            :tree-data="tab.treeData"
            @select="handleSelect"
            :replace-fields="replaceFields"
            :expandedKeys="tab.expandedKeys"
            v-if="tab.treeData.length"
            @expand="handleExpandTree"
            :selectedKeys="tab.selectedKeys"
          >
            <template #title="item">
              <div class="tree-node-wrapper" @mouseenter="hoveredNodeKey = item.key" @mouseleave="hoveredNodeKey = null">
                <div class="tree-node-content">
                  <span :style="`font-weight: bold;color: ${item.isAdd ? 'green' : ''}`">
                    {{ item.name }}
                  </span>
                  <span v-if="item.columnType" style="color: #aaa; font-style: italic">{{ ` (${item.columnType})` }}</span>
                </div>
                <div class="tree-node-actions" v-if="hoveredNodeKey === item.key || (tab.selectedKeys && tab.selectedKeys.includes(item.key))">
                  <span v-if="item.canAdd && !item.parentKey" class="action-icon-wrapper" @click.stop="handleAddFromNode(item)">
                    <CustomIcon type="icon-v2-Add" :size="14" />
                  </span>
                  <span v-if="item.canDelete && item.parentKey" class="action-icon-wrapper" @click.stop="handleDeleteFromNode(item)">
                    <CustomIcon type="icon-v2-Delete2" :size="14" />
                  </span>
                </div>
              </div>
            </template>
          </a-tree>
        </div>
      </div>
      <div class="right">
        <div class="create-table" v-if="showItem">
          <create-table-item
            :current-schema="component"
            :form-data="tab.formData"
            :node-type="tab.nodeType"
            :selected-index="tab.selectedIndex"
            :tab="tab"
            v-for="component in tab.selectedSchema"
            :key="component.field"
          />
        </div>
      </div>
    </div>
    <CCModal v-model="showSqlModal" :title="$t('sql-yu-ju')" :width="700" :mask-closable="false">
      <div style="border: 1px solid #ccc; padding: 5px; height: 450px; overflow: scroll">
        <pre>{{ sqlString }}</pre>
      </div>
      <template #footer>
        <a-button @click="copyText(sqlString)">{{ $t('fu-zhi-sql-yu-ju') }}</a-button>
        <a-button v-if="permission.hasPermission" type="primary" @click="handleRun" :loading="executeSQLLoading">
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button v-if="!permission.hasPermission" type="primary" @click="handleOpenTicketModal">
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
      </template>
    </CCModal>
    <CCModal
      v-model="showExecuteInfoModal"
      :title="$t('zhi-hang-xin-xi')"
      :width="800"
      :mask-closable="false"
      :keyboard="false"
      @on-cancel="handleCloseExecuteInfoModal"
    >
      <div style="height: 600px; overflow: auto">
        <div v-for="(info, index) in tab.executeInfo" :key="index" class="result-info">
          <div class="first">
            <div :class="`level ${info.success ? 'Info' : 'Error'}`">{{ info.database }}></div>
            <div class="sql">{{ info.sql }}</div>
          </div>
          <div class="second">
            <div class="time">[{{ dayjs(info.startTimestamp).format('YYYY-MM-DD HH:mm:ss') }}]</div>
            <div :class="`message ${!info.success ? 'message-error' : ''}`">
              {{ info.message }}
            </div>
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
          <a-input type="textarea" v-model:value="ticketData.description" :rows="4" />
        </a-form-item>
      </a-form>
      <template #footer>
        <a-button type="text" @click="handleCloseTicketModal">{{ $t('qu-xiao') }}</a-button>
        <a-button type="primary" @click="submitTicket">{{ $t('que-ding') }}</a-button>
      </template>
    </CCModal>
  </div>
</template>

<style scoped lang="less">
.result-info {
  margin-bottom: 5px;
  font-weight: bold;

  .first {
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

.struct-view {
  width: 100%;
  padding: 0 10px;
  display: flex;
  flex-direction: column;
  height: 100%;

  .header {
    height: 30px;
    line-height: 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .title {
      display: flex;
      align-items: center;
    }
  }

  .struct-view-content {
    margin-bottom: 10px;
    display: flex;
    border: 1px solid #ccc;
    flex: 1;
    min-height: 0;

    .left {
      border-right: 1px solid #ccc;
      width: 200px;
      display: flex;
      flex-direction: column;
      position: relative;

      .struct-resize {
        height: 100%;
        width: 6px;
        background: rgba(0, 0, 0, 0);
        position: absolute;
        right: -3px;
        cursor: col-resize;
        z-index: 9;
      }

      :deep(.ant-btn) {
        border: none;
        border-bottom: 1px solid #ccc;

        &:first-child {
          border-right: 1px solid #ccc;
        }
      }

      > div {
        overflow-x: hidden !important;
        overflow-y: auto;
      }
    }

    .right {
      flex: 1;
      overflow: auto;
      padding: 10px;
    }

    :deep(.ant-tree-child-tree > li:first-child) {
      padding: 0;
    }

    :deep(.ant-tree > li:first-child) {
      padding: 0;
    }

    :deep(.ant-tree li) {
      padding: 0;
    }

    :deep(.ant-tree li ul) {
      padding-left: 8px;
    }

    :deep(.ant-tree li .ant-tree-node-content-wrapper) {
      width: 100%;
      padding: 4px 8px;
      border-radius: 4px;
      transition: all 0.3s;
      display: inline-block;
      line-height: 16px;
    }

    :deep(.ant-tree li .ant-tree-node-content-wrapper:hover) {
      background-color: #f5f5f5;
    }

    :deep(.ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected) {
      background-color: #e6f7ff;
    }
    :deep(.ant-tree-node-content-wrapper) {
      width: 100% !important;
      overflow: hidden;
    }
    :deep(.ant-tree-treenode) {
      width: 100% !important;
      padding-bottom: 0;
      white-space: nowrap;
      overflow: hidden;
    }
    :deep(.ant-tree-treenode-selected) {
      width: 100% !important;
      background-color: #e6f4ff !important;
      padding-bottom: 0;
      overflow: hidden;
    }
    :deep(.ant-tree-node-selected) {
      width: 100% !important;
      overflow: hidden;
    }

    :deep(.ant-tree) {
      overflow-x: hidden !important;
    }

    .tree-node-wrapper {
      position: relative;
      display: flex;
      align-items: center;
      width: 100%;
      min-height: 24px;
    }

    .tree-node-content {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      padding-right: 20px;
    }

    .tree-node-actions {
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%);
      display: flex;
      gap: 4px;
      padding-left: 16px;
      padding-right: 4px;
      background: linear-gradient(to right, transparent 0%, rgba(245, 245, 245, 0.8) 15%, #f5f5f5 100%);

      .action-icon-wrapper {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 22px;
        height: 22px;
        border-radius: 4px;
        cursor: pointer;
        transition: all 0.2s ease;
        color: rgba(0, 0, 0, 0.45);

        &:hover {
          background-color: rgba(0, 0, 0, 0.06);
          color: rgba(0, 0, 0, 0.88);
          transform: scale(1.1);
        }

        &:active {
          transform: scale(0.95);
        }
      }
    }

    :deep(.ant-tree-node-content-wrapper.ant-tree-node-selected) {
      .tree-node-actions {
        background: linear-gradient(to right, transparent 0%, rgba(230, 247, 255, 0.8) 15%, #e6f7ff 100%);
      }
    }
  }
}
</style>
