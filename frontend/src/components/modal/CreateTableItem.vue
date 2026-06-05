<script>
import { PlusOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue';
import dayjs from 'dayjs';
import { TAB_TYPE } from '@/const';
import browseMixin from '@/mixins/browseMixin';

export default {
  name: 'CreateTableItem',
  props: {
    type: {
      type: String,
      default: 'node'
    },
    tab: Object,
    currentSchema: Object,
    formData: [Object, Array],
    nodeType: String,
    selectedIndex: Number,
    selectedNode: Object
  },
  components: {
    PlusOutlined,
    QuestionCircleOutlined
  },
  mixins: [browseMixin],
  computed: {
    isAdd() {
      if (this.tab.type === TAB_TYPE.STRUCT && this.formData[this.nodeType][this.selectedIndex]) {
        return this.formData[this.nodeType][this.selectedIndex].isAdd;
      } else {
        return false;
      }
    }
  },
  data() {
    return {
      expandedKeys: [],
      selectedKeys: [],
      selectedData: {},
      selectedColumn: {},
      selectedColumnIndex: -1,
      selectedTemplate: {},
      selectedTemplateIndex: -1,
      selectedLeaf: null,
      schema: {},
      inputVisible: false,
      inputValue: ''
    };
  },
  watch: {
    currentSchema: {
      handler(newVal) {
        this.schema = newVal;

        // 处理有 children 的表单
        if (newVal.children && newVal.children.length) {
          newVal.children.forEach((item) => {
            if (!(item.field in this.selectedData)) {
              this.selectedData[item.field] = item.defaultVal;
            }
          });
        }

        // 处理通过 options 字段渲染子表单的情况
        if (newVal.options && newVal.options.length && this.selectedIndex > -1) {
          if (!this.selectedData || Object.keys(this.selectedData).length === 0) {
            this.selectedData = this.formData[this.nodeType][this.selectedIndex];
          }

          if (this.selectedData && this.selectedData[newVal.field]) {
            this.generateOptionSchema(newVal.options, this.selectedData[newVal.field], newVal);
          }
        }
      },
      immediate: true,
      deep: true
    },
    nodeType: {
      handler(newVal) {
        if (newVal === 'tableInfo') {
          this.selectedData = this.formData[newVal];
        } else if (newVal === 'params') {
          this.selectedData = this.formData;
        } else {
          this.selectedColumn = {};
          if (this.selectedIndex > -1) {
            this.selectedData = this.formData[newVal][this.selectedIndex];
          }
        }
      },
      immediate: true
    },
    formData: {
      handler(newVal) {
        if (this.nodeType === 'params' && newVal) {
          this.selectedData = this.formData;
        }
      },
      deep: true
    },
    'selectedNode.key': {
      handler(newVal, oldVal) {
        if (this.tab.type === TAB_TYPE.QUERY && newVal !== oldVal) {
          if (this.selectedNode.configType === 'tableConfig') {
            this.formData.forEach((table) => {
              if (table.name === this.selectedNode.title) {
                this.selectedData = table;
                this.currentSchema.children.forEach((item) => {
                  if (!(item.field in this.selectedData)) {
                    this.selectedData[item.field] = item.defaultVal;
                  }
                });
              }
            });
          } else if (this.nodeType !== 'params') {
            this.formData.forEach((table) => {
              if (table.name === this.selectedNode.tableName) {
                table.columnConfigs.forEach((column) => {
                  if (column.name === this.selectedNode.title) {
                    this.selectedData = column;
                  }
                  this.currentSchema.children.forEach((item) => {
                    if (!(item.field in this.selectedData)) {
                      this.selectedData[item.field] = item.defaultVal;
                    }
                  });
                });
              }
            });
          }
        }
      },
      immediate: true,
      deep: true
    },
    'tab.selectedNode.key': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal && this.nodeType !== 'tableInfo') {
          if (this.nodeType === 'params') {
            this.selectedData = this.formData;
          } else if (this.selectedIndex > -1) {
            this.selectedData = this.formData[this.nodeType][this.selectedIndex];
          }
        }
      },
      immediate: true,
      deep: true
    },
    'tab.key': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          if (this.nodeType === 'tableInfo') {
            this.selectedData = this.formData[this.nodeType];
          } else {
            this.selectedColumn = {};
            if (this.selectedIndex > -1) {
              this.selectedData = this.formData[this.nodeType][this.selectedIndex];
            }
          }
        }
      },
      deep: true
    },
    isAdd: {
      handler(newVal) {
        if (newVal) {
          // 当isAdd变为true时，延迟聚焦name输入框
          this.$nextTick(() => {
            this.focusNameInput();
          });
        }
      },
      immediate: true
    }
  },
  methods: {
    handleClickColumn(index, column) {
      this.selectedColumnIndex = index;
      this.selectedColumn = column;
    },
    handleAddColumn(schema) {
      const column = {
        key: dayjs().valueOf()
      };
      if (schema.children && schema.children.length) {
        schema.children.forEach((child) => {
          column[child.field] = child.defaultVal;
        });
      }

      if (this.selectedData[schema.field]) {
        if (
          schema?.typeConfig?.maxLevel &&
          this.selectedData[schema.field] &&
          this.selectedData[schema.field].length >= schema?.typeConfig?.maxLevel
        ) {
          return;
        }
        this.selectedData[schema.field].push(column);
      } else {
        this.selectedData[schema.field] = [column];
      }

      this.selectedColumn = column;
      this.selectedColumnIndex = this.selectedData[schema.field].length - 1;
    },
    handleDeleteColumn(schema) {
      const index = this.selectedData[schema.field].findIndex((column) => column.key === this.selectedColumn.key);
      this.selectedData[schema.field].splice(index, 1);
      this.selectedColumn = {};
      this.selectedColumnIndex = -1;
      this.$forceUpdate();
    },
    handleAddTree(schema) {
      const leaf = {
        key: dayjs().valueOf(),
        scopedSlots: { title: 'custom' },
        title: this.$t('fen-qu'),
        subItems: [],
        deep: 1
      };

      if (schema.children && schema.children.length) {
        schema.children.forEach((child) => {
          leaf[child.field] = child.defaultVal;
        });
      }
      this.selectedColumn = leaf;

      if (this.selectedData[schema.field]) {
        this.selectedData[schema.field].push(leaf);
      } else {
        this.selectedData[schema.field] = [leaf];
      }

      this.selectedData = { ...this.selectedData };
    },
    addExpandedKey(key) {
      if (!this.expandedKeys.includes(key)) {
        this.expandedKeys.push(key);
      }
    },
    removeExpandedKey(key) {
      this.expandedKeys = this.expandedKeys.filter((expandedKey) => expandedKey !== key);
    },
    handleAddLeaf(schema, item) {
      const leaf = {
        parentKey: item.key,
        key: dayjs().valueOf(),
        scopedSlots: { title: 'custom' },
        title: this.$t('fen-qu'),
        subItems: [],
        deep: item.deep + 1
      };

      if (schema.children && schema.children.length) {
        schema.children.forEach((child) => {
          leaf[child.field] = child.defaultVal;
        });
      }

      item.subItems.push(leaf);
      this.selectedColumn = leaf;
      this.selectedKeys = [leaf.key];
      this.addExpandedKey(leaf.parentKey);

      this.selectedData = { ...this.selectedData };
    },
    handleDeleteLeaf(schema, item) {
      if (item.deep === 1) {
        const leafIndex = this.selectedData[schema.field].findIndex((subItem) => subItem.key === item.key);
        if (leafIndex > -1) {
          this.selectedData[schema.field].splice(leafIndex, 1);
        }
      } else {
        const { key, parentKey } = item;
        const findByIdRecursive = (array, key1) => {
          for (let index = 0; index < array.length; index++) {
            const element = array[index];
            if (element.key === key1) {
              return element;
            } else {
              if (element.subItems) {
                const found = findByIdRecursive(element.subItems, key1);

                if (found) {
                  return found;
                }
              }
            }
          }
        };

        const parent = findByIdRecursive(this.selectedData[schema.field], parentKey);
        if (parent) {
          parent.subItems = parent.subItems.filter((subItem) => subItem.key !== item.key);
          this.selectedData = { ...this.selectedData };
        }
      }
    },
    handleClickLeaf(selectedKeys, e) {
      this.selectedColumn = e.node.dataRef;
      if (this.expandedKeys.includes(e.node.dataRef.key)) {
        this.removeExpandedKey(e.node.dataRef.key);
      } else {
        this.addExpandedKey(e.node.dataRef.key);
      }
    },
    handleAddTemplate(parentSchema, schema) {
      const template = {
        key: `${dayjs().valueOf()}`
      };
      if (schema.children && schema.children.length) {
        schema.children.forEach((child) => {});
      }
      if (this.selectedColumn[schema.field]) {
        this.selectedColumn[schema.field].push(template);
      } else {
        this.selectedColumn[schema.field] = [template];
      }
      this.selectedData[parentSchema.field][this.selectedColumnIndex] = { ...this.selectedColumn };
      this.selectedColumn = { ...this.selectedColumn };
      this.selectedTemplate = template;
      this.selectedTemplateIndex = this.selectedColumn[schema.field].length - 1;
    },
    handleDeleteTemplate(parentSchema, schema) {
      console.log(this.selectedData[parentSchema.field], parentSchema.field, schema.field, this.selectedColumnIndex, this.selectedTemplateIndex);
      this.selectedData[parentSchema.field][this.selectedColumnIndex][schema.field].splice(this.selectedTemplateIndex, 1);
      this.selectedTemplate = {};
      this.selectedTemplateIndex = -1;
    },
    handleClickTemplate(index, template) {
      this.selectedTemplateIndex = index;
      this.selectedTemplate = template;
    },
    async handleReferenceSchemaOpenChange(open) {
      console.log(open);
      if (open) {
        const res = await this.$services.dmBrowseListLevels({
          data: {
            levels: [this.tab.node.ENV.id, this.tab.node.INSTANCE.id]
          }
        });

        if (res.success) {
          this.tab.referenceSchemaList = res.data;
          this.tab.referenceTableList = [];
          this.tab.referenceColumnList = [];
        }
      }
    },
    async handleReferenceTableOpenChange() {
      const res = await this.$services.dmBrowseListLeaf({
        data: {
          levels: [this.tab.node.ENV.id, this.tab.node.INSTANCE.id, this.selectedData.referencedSchema],
          leafType: 'TABLE'
        }
      });

      if (res.success) {
        this.tab.referenceTableList = res.data;
        this.tab.referenceColumnList = [];
      }
    },
    async handleReferenceColumnOpenChange(config) {
      const res = await this.$services.dmBrowseActionsFetchParamOptions({
        data: {
          levels: [this.tab.node.ENV.id, this.tab.node.INSTANCE.id, this.selectedData.referencedSchema],
          table: this.selectedData.referencedTable,
          type: config ? config.type : ''
        }
      });

      if (res.success) {
        this.tab.referenceColumnList = res.data;
      }
    },
    isReadOnly(schema) {
      if (this.isAdd) {
        return false;
      } else {
        return schema.readOnly;
      }
    },
    handleInputChange(field, event) {
      if (field === 'name' && this.type === 'node') {
        if (this.tab.formData?.keys) {
          this.tab.formData.keys.forEach((indexKey) => {
            indexKey.columns.forEach((column) => {
              if (column.name === this.selectedData[field]) {
                column.name = event.target.value;
              }
            });
          });
        }

        if (this.tab.formData?.indexes) {
          this.tab.formData.indexes.forEach((indexKey) => {
            indexKey.columns.forEach((column) => {
              if (column.name === this.selectedData[field]) {
                column.name = event.target.value;
              }
            });
          });
        }
      }

      this.selectedData[field] = event.target.value;
    },
    handleCheckChange() {
      this.$forceUpdate();
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(function () {
        this.$refs.input.focus();
      });
    },
    handleInputConfirm() {
      let tags = this.selectedData[this.schema.field];
      if (!tags) {
        tags = [];
      }
      if (!tags.includes(this.inputValue)) {
        tags.push(this.inputValue);
      }

      this.selectedData[this.schema.field] = tags;
      this.inputValue = '';
      this.inputVisible = false;
    },
    async generateOptionSchema(options, value, schema) {
      console.log('generateOptionSchema', value);
      for (let i = 0; i < options.length; i++) {
        const option = options[i];
        if (option.value === value && option.children && option.children.length) {
          schema.children = option.children;
          for (let j = 0; j < option.children.length; j++) {
            const child = option.children[j];
            console.log(child.type);

            if (!(child.field in this.selectedData)) {
              this.selectedData[child.field] = child.defaultVal;
            }

            if (child.type === 'Tag') {
              this.selectedData[child.field] = [];
            } else if (child.type === 'Options') {
              this.generateOptionSchema(child.options, child.defaultVal || '', child);
            } else if (child.type === 'SelectColumns' || child.type === 'SelectorList') {
              this.selectedData[child.field] = [];
            } else if (child.type === 'TriggerColumns') {
              this.selectedData[child.field] = [];
            } else if (child.type === 'Radios') {
              if (!this.selectedData[child.field]) {
                this.selectedData[child.field] = child.defaultVal || '';
              }
            }
          }
        }
      }
    },
    handleOptionChange(options, e) {
      let temp = e;
      if (e?.target?.value) {
        temp = e.target.value;
      }

      this.schema.children = [];
      this.generateOptionSchema(options, temp, this.schema);
      this.$forceUpdate();
    },
    handleCheckboxChange(schema, option, e) {
      if (!e && option.value === 'update') {
        this.schema.children = [];
      }
      if (e) {
        this.generateOptionSchema(schema.options, option.value, this.schema);
      }
      this.$forceUpdate();
    },
    handleChildOptionChange(options, e) {
      options.forEach((option) => {
        if (option.value === e && option.children) {
          this.selectedColumn.childList = option.children;
        }
      });
    },
    handleCloseTag(removedTag) {
      let tags = this.selectedData[this.schema.field];
      tags = tags.filter((tag) => tag !== removedTag);
      this.selectedData[this.schema.field] = tags;
    },
    focusNameInput() {
      // 如果是name字段且是新添加的项目，则聚焦并全选
      if (this.schema.field === 'name' && this.isAdd && this.$refs.nameInput) {
        this.$nextTick(() => {
          const input = this.$refs.nameInput.$el.querySelector('input');
          if (input) {
            input.focus();
            input.select();
          }
        });
      }
    }
  }
};
</script>

<template>
  <div style="display: flex; margin-top: 5px" v-if="!schema.hide">
    <div style="min-width: 100px; line-height: 24px" v-if="schema.type !== 'Fold'">{{ schema.titleI18N }}:</div>
    <div style="flex: 1">
      <div style="display: flex; align-items: center; justify-content: space-between" v-if="schema.type !== 'Fold'">
        <a-input
          v-if="schema.type === 'Input'"
          v-model:value="selectedData[schema.field]"
          size="small"
          @change.native="handleInputChange(schema.field, $event)"
          style="width: 100%"
          :disabled="isReadOnly(schema)"
          :ref="schema.field === 'name' ? 'nameInput' : null"
        />
        <a-textarea
          v-else-if="schema.type === 'TextArea'"
          v-model:value="selectedData[schema.field]"
          size="small"
          @change.native="handleInputChange(schema.field, $event)"
          :autosize="{ minRows: 5, maxRows: 10 }"
          style="width: 100%"
          :disabled="isReadOnly(schema)"
        />
        <a-select
          v-else-if="schema.type === 'Options'"
          v-model:value="selectedData[schema.field]"
          size="small"
          style="width: 100%"
          allow-clear
          show-search
          :disabled="isReadOnly(schema)"
          @change="handleOptionChange(schema.options, $event)"
        >
          <a-select-option v-for="option in schema.options" :key="`${tab.tabId}-${option.value}`" :value="option.value">
            {{ option.label }}
          </a-select-option>
        </a-select>
        <a-select v-else-if="schema.type === 'TriggerColumns'" v-model:value="selectedData[schema.field]" size="small" mode="multiple">
          <a-select-option v-for="option in this.tab.triggerColumnList" :key="`${tab.tabId}-${option.name}`" :value="option.name">
            {{ option.name }}
          </a-select-option>
        </a-select>
        <a-select v-else-if="schema.type === 'DataType'" v-model="selectedData[schema.field]" size="small" :disabled="isReadOnly(schema)">
          <a-select-option v-for="option in schema.options" :key="`${tab.tabId}-${option.key}`" :value="option.value">
            {{ option.key }}
          </a-select-option>
        </a-select>
        <a-checkbox
          v-else-if="schema.type === 'Check'"
          v-model:checked="selectedData[schema.field]"
          size="small"
          @change="handleCheckChange"
          :disabled="isReadOnly(schema)"
        />
        <a-checkbox-group
          v-else-if="schema.type === 'CheckBox'"
          v-model:value="selectedData[schema.field]"
          size="small"
          :disabled="isReadOnly(schema)"
        >
          <a-checkbox
            v-for="option in schema.options"
            :value="option.value"
            :key="option.value"
            @change="handleCheckboxChange(schema, option, $event)"
          >
            {{ option.label }}
          </a-checkbox>
        </a-checkbox-group>
        <a-radio-group
          v-else-if="schema.type === 'Radios'"
          v-model:value="selectedData[schema.field]"
          :disabled="isReadOnly(schema)"
          @change="handleOptionChange(schema.options, $event)"
        >
          <a-radio v-for="option in schema.options" :key="option.key" :value="option.value">
            {{ option.label }}
          </a-radio>
        </a-radio-group>
        <div v-else-if="schema.type === 'Tag'">
          <a-tag v-for="tag in selectedData[schema.field]" :key="tag" @close="() => handleCloseTag(tag)" closable :disabled="isReadOnly(schema)">
            {{ tag }}
          </a-tag>
          <a-input
            v-if="inputVisible"
            ref="input"
            type="text"
            size="small"
            :style="{ width: '78px' }"
            v-model:value="inputValue"
            @keyup.enter="handleInputConfirm()"
            :disabled="isReadOnly(schema)"
          />
          <a-tag v-else style="background: #fff; borderstyle: dashed" @click="showInput">
            <PlusOutlined />
            {{ $t('xin-zeng') }}
          </a-tag>
        </div>
        <div v-else-if="schema.type === 'AutoIncrement'">
          <a-checkbox v-model:value="selectedData[schema.field].enable" size="small" :disabled="isReadOnly(schema)" />
          <a-input
            v-model:value="selectedData[schema.field].value"
            :disabled="!selectedData[schema.field].enable || isReadOnly(schema)"
            size="small"
          />
        </div>
        <div
          v-else-if="
            schema.type === 'SelectColumns' || schema.type === 'ReferenceRelation' || schema.type === 'PartitionDefineList' || schema.type === 'Tree'
          "
          style="display: flex; width: 100%; border: 1px solid #ccc"
        >
          <div class="left" v-if="schema.type === 'Tree'" style="width: 200px; border-right: 1px solid #ccc; display: flex; flex-direction: column">
            <div style="width: 100%; display: flex">
              <a-button
                @click="handleAddTree(schema)"
                size="small"
                style="flex: 1"
                :disabled="isReadOnly(schema) || !selectedData.definition?.length"
              >
                {{ $t('zeng-jia') }}
              </a-button>
            </div>
            <a-tree
              :tree-data="selectedData[schema.field]"
              @select="handleClickLeaf"
              style="flex: 1; overflow: auto; height: 100%; min-height: 80px; max-height: 300px"
              default-expand-all
              :replaceFields="{ children: 'subItems' }"
              :selectedKeys.sync="selectedKeys"
              :expandedKeys.sync="expandedKeys"
            >
              <template #custom="item">
                <span>{{ item.name || item.title }}</span>
                <a-button size="small" type="link" @click.stop="handleAddLeaf(schema, item)" v-if="item.deep < selectedData.definition.length">
                  {{ $t('zeng-jia-zi-fen-qu') }}
                </a-button>
                <a-button size="small" type="link" @click.stop="handleDeleteLeaf(schema, item)">
                  {{ $t('shan-chu') }}
                </a-button>
              </template>
            </a-tree>
          </div>
          <div class="left" v-else style="width: 200px; border-right: 1px solid #ccc">
            <div style="width: 100%; display: flex">
              <a-button @click="handleAddColumn(schema)" size="small" style="flex: 1" :disabled="isReadOnly(schema)">
                {{ $t('zeng-jia') }}
              </a-button>
              <a-button @click="handleDeleteColumn(schema)" size="small" style="flex: 1" :disabled="isReadOnly(schema)">
                {{ $t('shan-chu') }}
              </a-button>
            </div>
            <div :style="`min-height: 80px;height: ${selectedData[schema.field] ? selectedData[schema.field].length * 20 : 0}px`">
              <div
                v-for="(column, index) in selectedData[schema.field]"
                :key="column.key"
                :disabled="isReadOnly(schema)"
                @click="handleClickColumn(index, column)"
                :style="`background: ${column.key === selectedColumn.key ? '#ccc' : ''};height: 20px;margin-left: ${
                  schema.type === 'PartitionDefineList' ? index * 5 : 0
                }px;`"
              >
                <div v-if="schema.type === 'ReferenceRelation'">{{ column.name }} - {{ column.referenceColumnName }}</div>
                <div v-else-if="schema.type === 'PartitionDefineList'">{{ column.type }}({{ column.expression }})</div>
                <div v-else>
                  {{ column.name || column[schema.field] || 'null' }}
                </div>
              </div>
            </div>
          </div>
          <div class="right" style="flex: 1; padding: 5px">
            <div v-if="selectedColumn && selectedColumn.key">
              <div v-for="childSchema in schema.children" :key="childSchema.field">
                <div v-if="!childSchema.hide" style="display: flex; margin-top: 5px">
                  <div style="width: 80px" v-if="!(childSchema.type === 'PartitionTemplateList' && !selectedColumnIndex)">
                    {{ childSchema.titleI18N }}:
                  </div>
                  <a-select
                    v-model:value="selectedColumn[childSchema.field]"
                    style="width: 100%"
                    size="small"
                    v-if="childSchema.type === 'Columns'"
                    allow-clear
                    show-search
                    :disabled="childSchema.readOnly"
                  >
                    <a-select-option v-for="column in formData.columns" :key="`${tab.tabId}-${column.key}`" :value="column.name">
                      {{ column.name }}
                    </a-select-option>
                  </a-select>
                  <a-select
                    v-else-if="childSchema.type === 'ReferenceColumn'"
                    size="small"
                    style="width: 100%"
                    v-model:value="selectedColumn[childSchema.field]"
                    @dropdownVisibleChange="handleReferenceColumnOpenChange(childSchema.typeConfig)"
                  >
                    <a-select-option
                      v-for="referenceColumn in tab.referenceColumnList"
                      :value="referenceColumn"
                      :key="`${tab.tabId}-${referenceColumn}`"
                    >
                      {{ referenceColumn }}
                    </a-select-option>
                  </a-select>
                  <a-input
                    v-else-if="childSchema.type === 'Input'"
                    v-model:value="selectedColumn[childSchema.field]"
                    size="small"
                    style="width: 100%"
                    :disabled="childSchema.readOnly"
                  />
                  <a-select
                    v-else-if="childSchema.type === 'Options'"
                    v-model:value="selectedColumn[childSchema.field]"
                    @change="handleChildOptionChange(childSchema.options, $event)"
                    size="small"
                    style="width: 100%"
                    allow-clear
                    show-search
                    :disabled="isReadOnly(childSchema)"
                  >
                    <!-- <a-select-option v-for="option in childSchema.options" :key="`${tab.tabId}-${option.value}`" :value="option.value">
                      {{ option.label }}
                    </a-select-option> -->
                  </a-select>
                  <div
                    v-else-if="childSchema.type === 'PartitionTemplateList' && selectedColumnIndex"
                    style="display: flex; width: 100%; border: 1px solid #ccc"
                  >
                    <div class="left" style="width: 200px; border-right: 1px solid #ccc">
                      <div style="width: 100%; display: flex">
                        <a-button @click="handleAddTemplate(schema, childSchema)" size="small" style="flex: 1" :disabled="isReadOnly(childSchema)">
                          {{ $t('zeng-jia') }}
                        </a-button>
                        <a-button @click="handleDeleteTemplate(schema, childSchema)" size="small" style="flex: 1" :disabled="isReadOnly(childSchema)">
                          {{ $t('shan-chu') }}
                        </a-button>
                      </div>
                      <div style="min-height: 80px; max-height: 300px; overflow: auto">
                        <div
                          v-for="(template, index) in selectedColumn[childSchema.field]"
                          :key="template.key"
                          :disabled="isReadOnly(childSchema)"
                          @click="handleClickTemplate(index, template)"
                          :style="`background: ${template.key === selectedTemplate.key ? '#ccc' : ''};height: 20px;`"
                        >
                          <div>{{ $t('mo-ban-0') }}({{ template.name }})</div>
                        </div>
                      </div>
                    </div>
                    <div class="right" style="flex: 1; padding: 5px">
                      <div v-if="selectedTemplate && selectedTemplate.key">
                        <div v-for="childChildSchema in childSchema.children" :key="childChildSchema.field">
                          <div v-if="!childChildSchema.hide" style="display: flex; margin-top: 5px">
                            <div style="width: 80px">{{ childChildSchema.titleI18N }}:</div>
                            <a-input
                              v-if="childChildSchema.type === 'Input'"
                              v-model:value="selectedTemplate[childChildSchema.field]"
                              size="small"
                              style="width: 100%"
                              :disabled="childChildSchema.readOnly"
                            />
                            <a-select
                              v-else-if="childChildSchema.type === 'Options'"
                              v-model:value="selectedTemplate[childChildSchema.field]"
                              size="small"
                              style="width: 100%"
                              allow-clear
                              show-search
                              :disabled="isReadOnly(childChildSchema)"
                            >
                              <a-select-option v-for="option in childChildSchema.options" :key="`${tab.tabId}-${option.value}`" :value="option.value">
                                {{ option.label }}
                              </a-select-option>
                            </a-select>
                            <a-tooltip placement="left" style="margin-left: 3px">
                              <QuestionCircleOutlined v-if="childChildSchema.descI18N" class="ml-[4px]" />
                              <template #title>
                                {{ childChildSchema.descI18N }}
                              </template>
                            </a-tooltip>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <a-tooltip placement="left" style="margin-left: 3px" v-if="!(childSchema.type === 'PartitionTemplateList' && !selectedColumnIndex)">
                    <QuestionCircleOutlined v-if="childSchema.descI18N" class="ml-[4px]" />
                    <template #title>
                      {{ childSchema.descI18N }}
                    </template>
                  </a-tooltip>
                </div>
              </div>
              <div v-if="selectedColumn.childList">
                <div v-for="selectColumnsChild in selectedColumn.childList" :key="`${selectColumnsChild.field}${selectedColumnIndex}`">
                  <div v-if="!selectColumnsChild.hide" style="display: flex; margin-top: 5px">
                    <div style="width: 80px">{{ selectColumnsChild.titleI18N }}:</div>
                    <a-input
                      v-if="selectColumnsChild.type === 'Input'"
                      v-model:value="selectedColumn[selectColumnsChild.field]"
                      size="small"
                      style="width: 100%"
                      :disabled="selectColumnsChild.readOnly"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <a-select
          v-else-if="schema.type === 'ReferenceSchema'"
          size="small"
          @dropdownVisibleChange="handleReferenceSchemaOpenChange"
          v-model:value="selectedData[schema.field]"
          style="width: 100%"
        >
          <a-select-option
            v-for="referenceSchema in tab.referenceSchemaList"
            :value="referenceSchema.objName"
            :key="`${tab.tabId}-${referenceSchema.objName}`"
          >
            {{ referenceSchema.objName }}
          </a-select-option>
        </a-select>
        <a-select
          v-else-if="schema.type === 'ReferenceTable'"
          size="small"
          @dropdownVisibleChange="handleReferenceTableOpenChange"
          style="width: 100%"
          v-model:value="selectedData[schema.field]"
        >
          <a-select-option
            v-for="referenceTable in tab.referenceTableList"
            :value="referenceTable.objName"
            :key="`${tab.tabId}-${referenceTable.objName}`"
          >
            {{ referenceTable.objName }}
          </a-select-option>
        </a-select>
        <a-tooltip placement="left" style="margin-left: 3px">
          <QuestionCircleOutlined v-if="schema.descI18N" class="ml-[4px]" />
          <template #title>
            {{ schema.descI18N }}
          </template>
        </a-tooltip>
      </div>
      <a-collapse v-if="schema.type === 'Fold'" size="small">
        <a-collapse-panel :header="schema.titleI18N">
          <CreateTableItem
            v-for="childSchema in schema.children"
            :key="childSchema.field"
            :current-schema="childSchema"
            :node-type="nodeType"
            :selected-index="selectedIndex"
            :form-data="formData"
            :selected-node="selectedNode"
            :tab="tab"
          />
        </a-collapse-panel>
      </a-collapse>
      <template
        v-if="
          schema.children &&
          schema.children.length &&
          schema.type !== 'SelectColumns' &&
          schema.type !== 'Fold' &&
          schema.type !== 'ReferenceRelation' &&
          schema.type !== 'PartitionDefineList' &&
          schema.type !== 'Tree' &&
          schema.type !== 'PartitionTemplateList'
        "
      >
        <CreateTableItem
          v-for="childSchema in schema.children"
          :key="childSchema.field"
          style="flex: 1; margin-left: -100px"
          :current-schema="childSchema"
          :node-type="nodeType"
          :selected-index="selectedIndex"
          :form-data="formData"
          :selected-node="selectedNode"
          :tab="tab"
        />
      </template>
    </div>
  </div>
</template>

<style scoped lang="less">
:deep(.ivu-select-small.ivu-select-single .ivu-select-selection) {
  border-radius: 0;
  border-color: #d9d9d9;
}

.left {
  :deep(.ant-btn) {
    border: none;
    border-bottom: 1px solid #ccc;

    &:first-child {
      border-right: 1px solid #ccc;
    }
  }
}

.selector-list {
  width: 100%;
  display: flex;
  border: 1px solid #ccc;

  .left {
    width: 200px;

    .list {
      min-height: 300px;
    }
  }

  .right {
    flex: 1;
    border-left: 1px solid #ccc;
  }
}
</style>
