<template>
  <div class="security-ds-range-selector" v-if="selectable">
    <div class="security-ds-range-selected" :class="{ 'security-ds-range-selected-empty': !selectedSortedDsRange.length }" @click="openDsModal">
      <div class="security-ds-range-selected-content">
        <template v-if="selectedSortedDsRange.length">
          <span v-for="ds in selectedVisibleDsRange" :key="`selected-${ds}`" class="security-ds-range-selected-card" :title="getDsDisplayName(ds)">
            <CustomIcon :type="ds" size="14px" leftMargin="0" rightMargin="6px" />
            <span class="security-ds-range-selected-card-text">{{ getDsDisplayName(ds) }}</span>
          </span>
          <span class="security-ds-range-more security-ds-range-selected-count">{{ selectedCountText }}</span>
        </template>
        <span v-else class="security-ds-range-placeholder">{{ placeholder }}</span>
      </div>
      <Icon type="ios-arrow-down" class="security-ds-range-selected-arrow" />
    </div>
    <button type="button" class="security-ds-range-picker-button" :disabled="disabled" @click="openDsModal">
      <Icon type="md-add" />
      <span>{{ $t('xuan-ze-shu-ju-yuan') }}</span>
    </button>
    <Modal
      v-model="dsModalVisible"
      transfer
      class-name="security-ds-range-modal"
      :title="$t('xuan-ze-shu-ju-yuan')"
      :width="760"
      @on-cancel="handleCancelDsRange"
    >
      <div class="security-ds-range-modal-body">
        <div class="security-ds-range-modal-toolbar">
          <div class="security-ds-range-modal-summary">{{ $t('yi-xuan-ze-shu-ju-yuan') }}：{{ modalSelectedSortedDsRange.length }}</div>
          <Checkbox
            class="security-ds-range-select-all"
            v-model="modalSelectAllChecked"
            :indeterminate="isModalDsPartiallySelected"
            :disabled="disabled || !selectableOptions.length"
          >
            {{ $t('quan-xuan') }}
          </Checkbox>
        </div>
        <div class="security-ds-range-option-grid">
          <button
            v-for="ds in selectableOptions"
            :key="`option-${ds.name}`"
            type="button"
            class="security-ds-range-select-card"
            :class="{ 'security-ds-range-select-card-active': isModalDsSelected(ds.name) }"
            :disabled="disabled"
            :title="getDsDisplayName(ds.name)"
            :aria-pressed="isModalDsSelected(ds.name)"
            @click="toggleModalDs(ds.name)"
          >
            <span class="security-ds-range-select-card-icon">
              <CustomIcon :type="ds.name" size="16px" leftMargin="0" rightMargin="0" />
            </span>
            <span class="security-ds-range-select-card-text">{{ getDsDisplayName(ds.name) }}</span>
          </button>
        </div>
      </div>
      <template #footer>
        <div class="security-ds-range-modal-footer">
          <Button type="primary" @click="handleConfirmDsRange">
            {{ $t('que-ding') }}
          </Button>
        </div>
      </template>
    </Modal>
  </div>
  <div class="security-ds-range-tags" v-else-if="sortedDsRange.length">
    <span v-for="ds in visibleDsRange" :key="ds" class="security-ds-range-icon-card" :title="getDsDisplayName(ds)">
      <CustomIcon :type="ds" size="16px" leftMargin="0" rightMargin="0" />
    </span>
    <Poptip trigger="click" transfer placement="bottom" :width="popoverWidth" class="security-ds-range-more-popover">
      <span class="security-ds-range-more">{{ countText }}</span>
      <template #content>
        <div class="security-ds-range-popover-content">
          <div class="security-ds-range-popover-title">
            {{ $t('shi-yong-shu-ju-yuan-count-ge', { count: sortedDsRange.length }) }}
          </div>
          <div v-for="group in dsGroups" :key="group.key" class="security-ds-range-popover-group">
            <div class="security-ds-range-popover-group-title">
              <Icon :type="group.icon" />
              <span>{{ group.title }}</span>
              <span class="security-ds-range-popover-group-count">{{ getGroupCountText(group.items.length) }}</span>
            </div>
            <div class="security-ds-range-popover-tags">
              <span
                v-for="ds in group.items"
                :key="`${group.key}-${ds}`"
                class="security-ds-range-tag security-ds-range-tag-popover"
                :title="getDsDisplayName(ds)"
              >
                <CustomIcon :type="ds" size="14px" leftMargin="0" rightMargin="6px" />
                <span class="security-ds-range-tag-text">{{ getDsDisplayName(ds) }}</span>
              </span>
            </div>
          </div>
        </div>
      </template>
    </Poptip>
  </div>
  <span v-else class="security-ds-range-empty">{{ emptyText }}</span>
</template>

<script>
import DataSourceGroup from '@/views/dataSourceGroup.json';

const DS_DISPLAY_ORDER = [
  'MySQL',
  'Oracle',
  'PostgreSQL',
  'SQLServer',
  'Db2',
  'GaussDB',
  'GaussDBForOpenGauss',
  'KingbaseES',
  'MariaDB',
  'ObForOracle',
  'OceanBase',
  'TiDB',
  'PolarDBPg',
  'PolarDbMySQL',
  'PolarDbX',
  'Redis',
  'MongoDB',
  'ElasticSearch',
  'ClickHouse',
  'Doris',
  'Greenplum',
  'SelectDB',
  'StarRocks',
  'AdbForMySQL',
  'Hologres',
  'MaxCompute',
  'Kafka',
  'Pulsar'
];

const DS_LABEL_MAP = {
  AdbForMySQL: 'ADB for MySQL',
  ADBforMySQL: 'ADB for MySQL',
  GaussDBForOpenGauss: 'OpenGauss',
  ObForOracle: 'ObForOracle',
  PolarDBPg: 'PolarDBPg',
  PolarDbMySQL: 'PolarDbMySQL',
  PolarDbX: 'PolarDB-X',
  SQLServer: 'SQLServer'
};

const buildDsSet = (...groups) => new Set(groups.flat().filter(Boolean));

const DS_GROUP_DEFS = [
  {
    key: 'rdb',
    labelKey: 'guan-xi-xing-shu-ju-ku',
    icon: 'ios-server-outline',
    types: buildDsSet(
      DataSourceGroup.mysql,
      DataSourceGroup.polar,
      DataSourceGroup.oracle,
      DataSourceGroup.pg,
      DataSourceGroup.sqlServer,
      DataSourceGroup.db2,
      DataSourceGroup.tidb,
      DataSourceGroup.mariaDb,
      DataSourceGroup.gaussDB,
      DataSourceGroup.ob,
      DataSourceGroup.polarDbX,
      DataSourceGroup.dameng,
      ['AdbForMySQL', 'ADBforMySQL', 'GaussDBForOpenGauss', 'OceanBase', 'ObForOracle', 'PolarDBPg']
    )
  },
  {
    key: 'nosql',
    labelKey: 'no-sql-huan-cun',
    icon: 'ios-cube-outline',
    types: buildDsSet(DataSourceGroup.redis, DataSourceGroup.mongo, DataSourceGroup.es, DataSourceGroup.dynamoDB, ['Elasticsearch', 'ElasticSearch'])
  },
  {
    key: 'analytics',
    labelKey: 'fen-xi-xing-shu-ju-ku',
    icon: 'ios-stats-outline',
    types: buildDsSet(
      DataSourceGroup.ck,
      DataSourceGroup.starrocks,
      DataSourceGroup.maxCompute,
      DataSourceGroup.hive,
      DataSourceGroup.kudu,
      DataSourceGroup.iceberg,
      DataSourceGroup.paimon,
      ['ClickHouse', 'Doris', 'SelectDB', 'StarRocks', 'Hologres', 'MaxCompute', 'Greenplum']
    )
  },
  {
    key: 'mq',
    labelKey: 'xiao-xi-dui-lie',
    icon: 'ios-git-network',
    types: buildDsSet(DataSourceGroup.mq, DataSourceGroup.kafka, DataSourceGroup.pulsar)
  }
];

export default {
  name: 'DataSourceRangeTags',
  data() {
    return {
      dsModalVisible: false,
      modalSelectedDsRange: []
    };
  },
  props: {
    modelValue: {
      type: Array,
      default: undefined
    },
    value: {
      type: Array,
      default: undefined
    },
    options: {
      type: Array,
      default: () => []
    },
    selectable: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: '请选择数据源'
    },
    dsRange: {
      type: Array,
      default: () => []
    },
    limit: {
      type: Number,
      default: 4
    },
    popoverWidth: {
      type: Number,
      default: 520
    },
    emptyText: {
      type: String,
      default: '-'
    }
  },
  computed: {
    sortedDsRange() {
      return this.sortDsRange(this.dsRange);
    },
    visibleDsRange() {
      return this.sortedDsRange.slice(0, this.limit);
    },
    countText() {
      return `${this.sortedDsRange.length}`;
    },
    dsGroups() {
      return this.buildDsGroups(this.sortedDsRange);
    },
    selectedDsRange() {
      if (Array.isArray(this.modelValue)) {
        return this.modelValue;
      }
      if (Array.isArray(this.value)) {
        return this.value;
      }
      return [];
    },
    selectedSortedDsRange() {
      return this.sortDsRange(this.selectedDsRange);
    },
    selectedVisibleDsRange() {
      return this.selectedSortedDsRange.slice(0, this.limit);
    },
    selectedCountText() {
      return `${this.selectedSortedDsRange.length}`;
    },
    selectedDsGroups() {
      return this.buildDsGroups(this.selectedSortedDsRange);
    },
    modalSelectedSortedDsRange() {
      return this.sortDsRange(this.modalSelectedDsRange);
    },
    modalSelectableDsNames() {
      return this.selectableOptions.map((option) => option.name);
    },
    isAllModalDsSelected() {
      return this.modalSelectableDsNames.length > 0 && this.modalSelectableDsNames.every((ds) => this.modalSelectedDsRange.includes(ds));
    },
    modalSelectAllChecked: {
      get() {
        return this.isAllModalDsSelected;
      },
      set(checked) {
        this.handleToggleAllDs(checked);
      }
    },
    isModalDsPartiallySelected() {
      return this.modalSelectedDsRange.length > 0 && !this.isAllModalDsSelected;
    },
    selectableOptions() {
      const normalizedOptions = this.options
        .map((option) => {
          if (typeof option === 'string') {
            return { name: option };
          }
          return {
            name: option.name || option.value || option.type,
            i18n: option.i18n || option.label || option.name || option.value || option.type
          };
        })
        .filter((option) => option.name);
      return this.sortDsRange(normalizedOptions.map((option) => option.name)).map((name) => {
        return normalizedOptions.find((option) => option.name === name) || { name };
      });
    }
  },
  methods: {
    sortDsRange(dsRange) {
      const uniqueDsRange = Array.from(new Set(Array.isArray(dsRange) ? dsRange.filter(Boolean) : []));
      return uniqueDsRange.sort((left, right) => {
        const leftIndex = DS_DISPLAY_ORDER.indexOf(left);
        const rightIndex = DS_DISPLAY_ORDER.indexOf(right);
        const normalizedLeftIndex = leftIndex === -1 ? DS_DISPLAY_ORDER.length : leftIndex;
        const normalizedRightIndex = rightIndex === -1 ? DS_DISPLAY_ORDER.length : rightIndex;
        if (normalizedLeftIndex !== normalizedRightIndex) {
          return normalizedLeftIndex - normalizedRightIndex;
        }
        return this.getDsDisplayName(left).localeCompare(this.getDsDisplayName(right));
      });
    },
    buildDsGroups(dsRange) {
      const groupedDs = new Set();
      const groups = DS_GROUP_DEFS.map((group) => {
        const items = dsRange.filter((ds) => group.types.has(ds) && !groupedDs.has(ds));
        items.forEach((ds) => groupedDs.add(ds));
        return {
          key: group.key,
          title: this.$t(group.labelKey),
          icon: group.icon,
          items
        };
      }).filter((group) => group.items.length);
      const otherItems = dsRange.filter((ds) => !groupedDs.has(ds));
      if (otherItems.length) {
        groups.push({
          key: 'other',
          title: this.$t('qi-ta'),
          icon: 'ios-apps-outline',
          items: otherItems
        });
      }
      return groups;
    },
    getDsDisplayName(ds) {
      return DS_LABEL_MAP[ds] || ds;
    },
    getGroupCountText(count) {
      return `(${count})`;
    },
    isModalDsSelected(ds) {
      return this.modalSelectedDsRange.includes(ds);
    },
    openDsModal() {
      if (this.disabled) {
        return;
      }
      this.modalSelectedDsRange = [...this.selectedSortedDsRange];
      this.dsModalVisible = true;
    },
    handleCancelDsRange() {
      this.modalSelectedDsRange = [];
    },
    handleConfirmDsRange() {
      this.commitDsRange(this.modalSelectedDsRange);
      this.modalSelectedDsRange = [];
      this.dsModalVisible = false;
    },
    toggleModalDs(ds) {
      if (this.disabled) {
        return;
      }
      const selectedDs = new Set(this.modalSelectedDsRange);
      if (selectedDs.has(ds)) {
        selectedDs.delete(ds);
      } else {
        selectedDs.add(ds);
      }
      this.modalSelectedDsRange = this.sortDsRange(Array.from(selectedDs));
    },
    handleToggleAllDs(checked) {
      if (this.disabled) {
        return;
      }
      this.modalSelectedDsRange = checked ? [...this.modalSelectableDsNames] : [];
    },
    commitDsRange(dsRange) {
      const nextValue = this.sortDsRange(dsRange);
      this.$emit('update:modelValue', nextValue);
      this.$emit('input', nextValue);
      this.$emit('change', nextValue);
      this.$emit('on-change', nextValue);
    }
  }
};
</script>

<style lang="less" scoped>
.security-ds-range-tags {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  max-width: 100%;
  overflow: hidden;
  white-space: nowrap;
}

.security-ds-range-selector {
  width: 100%;
}

.security-ds-range-selected {
  display: flex;
  align-items: center;
  min-height: 34px;
  gap: 8px;
  padding: 3px 8px;
  background: #fff;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
}

.security-ds-range-selected-empty {
  color: #b9c0cc;
}

.security-ds-range-selected-content {
  display: inline-flex;
  align-items: center;
  flex: 1;
  min-width: 0;
  gap: 8px;
  overflow: hidden;
}

.security-ds-range-selected-arrow {
  flex: 0 0 auto;
  color: #8a94a6;
  font-size: 16px;
}

.security-ds-range-placeholder {
  color: #b9c0cc;
  font-size: 13px;
}

.security-ds-range-picker-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 34px;
  margin-top: 8px;
  color: #24a877;
  font-size: 13px;
  font-weight: 600;
  line-height: 32px;
  background: #fff;
  border: 1px dashed #cfd6df;
  border-radius: 4px;
  cursor: pointer;
  transition:
    color 0.15s ease,
    border-color 0.15s ease,
    background 0.15s ease;
}

.security-ds-range-picker-button:hover {
  color: #139766;
  background: #f7fcfa;
  border-color: #3ecf8e;
}

.security-ds-range-picker-button:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.security-ds-range-picker-button .ivu-icon {
  margin-right: 6px;
  font-size: 16px;
}

.security-ds-range-modal-body {
  max-height: 520px;
  overflow: auto;
}

.security-ds-range-modal-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  gap: 16px;
}

.security-ds-range-modal-summary {
  flex: 1;
  min-width: 0;
  color: #5b667a;
  font-size: 13px;
  line-height: 20px;
}

.security-ds-range-select-all {
  flex: 0 0 auto;
  margin-right: 0;
  color: #27364d;
  font-size: 13px;
  line-height: 20px;
}

.security-ds-range-option-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(132px, 1fr));
  gap: 8px;
  margin-top: 10px;
}

.security-ds-range-select-card {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 0;
  height: 34px;
  padding: 0 10px;
  color: #596377;
  font-size: 13px;
  line-height: 32px;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition:
    color 0.15s ease,
    background 0.15s ease,
    border-color 0.15s ease;
}

.security-ds-range-select-card:hover {
  color: #24a877;
  border-color: #3ecf8e;
}

.security-ds-range-select-card-active {
  color: #24a877;
  background: #edf8f3;
  border-color: #3ecf8e;
}

.security-ds-range-select-card:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.security-ds-range-select-card-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  width: 20px;
  margin-right: 6px;
}

.security-ds-range-select-card-text {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.security-ds-range-icon-card {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  width: 32px;
  height: 26px;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition:
    border-color 0.15s ease,
    background 0.15s ease;
}

.security-ds-range-icon-card:hover {
  background: #f7fbff;
  border-color: #3ecf8e;
}

.security-ds-range-selected-card {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 1 116px;
  min-width: 0;
  max-width: 116px;
  height: 26px;
  padding: 0 8px;
  color: #596377;
  font-size: 12px;
  line-height: 24px;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.security-ds-range-selected-card-text {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.security-ds-range-tag {
  display: inline-flex;
  align-items: center;
  flex: 0 0 auto;
  max-width: 132px;
  height: 26px;
  padding: 0 10px;
  color: #596377;
  font-size: 12px;
  line-height: 24px;
  vertical-align: middle;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition:
    border-color 0.15s ease,
    background 0.15s ease,
    color 0.15s ease;
}

.security-ds-range-tag:hover {
  color: #344054;
  background: #f7fbff;
  border-color: #3ecf8e;
}

.security-ds-range-tag-text {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.security-ds-range-more {
  display: inline-flex;
  align-items: center;
  height: 22px;
  padding: 0 9px;
  color: #24a877;
  font-size: 12px;
  font-weight: 600;
  line-height: 22px;
  white-space: nowrap;
  background: #eaf8f2;
  border-radius: 11px;
  cursor: pointer;
  transition:
    color 0.15s ease,
    background 0.15s ease;
}

.security-ds-range-more:hover {
  color: #139766;
  background: #daf3e9;
}

.security-ds-range-selected-count {
  flex: 0 0 auto;
  cursor: default;
  pointer-events: none;
}

.security-ds-range-selected-count:hover {
  color: #24a877;
  background: #eaf8f2;
}

/deep/.security-ds-range-modal .ivu-modal-footer {
  padding: 14px 18px 18px;
}

.security-ds-range-modal-footer {
  display: flex;
  justify-content: center;
  align-items: center;
}

.security-ds-range-modal-footer .ivu-btn {
  min-width: 96px;
}

/deep/.security-ds-range-more-popover .ivu-poptip-rel {
  display: inline-flex;
  align-items: center;
}

.security-ds-range-popover-content {
  padding: 2px 0 0;
}

.security-ds-range-popover-title {
  padding: 2px 0 10px;
  color: #27364d;
  font-size: 13px;
  font-weight: 600;
  line-height: 20px;
  border-bottom: 1px solid #edf0f5;
}

.security-ds-range-popover-group {
  padding: 12px 0;
  border-bottom: 1px solid #f0f2f5;
}

.security-ds-range-popover-group-title {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  color: #4d5a70;
  font-size: 12px;
  font-weight: 600;
  line-height: 18px;
}

.security-ds-range-popover-group-count {
  color: #8a94a6;
  font-weight: 500;
}

.security-ds-range-popover-tags {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px;
}

.security-ds-range-tag-popover {
  justify-content: center;
  width: 100%;
  max-width: none;
}

.security-ds-range-empty {
  color: #9aa3b2;
}
</style>
