<template>
  <CCModal
    :model-value="modelValue"
    :width="1040"
    :title="$t('xuan-ze-shu-ju-yuan-lei-xing')"
    class="add-datasource-type-modal"
    @update:model-value="$emit('update:modelValue', $event)"
    @on-cancel="$emit('update:modelValue', false)"
  >
    <div class="add-datasource-type-modal-body">
      <nav class="add-datasource-deploy-list" :aria-label="$t('bu-shu-lei-xing')">
        <button
          v-for="type in deployTypes"
          :key="type.name"
          type="button"
          class="add-datasource-deploy-card"
          :class="{ active: deployType === type.name }"
          :aria-pressed="deployType === type.name"
          @click="selectDeployType(type.name)"
        >
          <span class="add-datasource-deploy-icon" aria-hidden="true">
            <svg v-if="type.icon === 'aliyun'" width="18" height="18" viewBox="0 0 1664 1024">
              <use xlink:href="#icon-v2-aliyun" width="4608" height="1024" />
            </svg>
            <CustomIcon v-else :type="type.icon" size="18px" />
          </span>
          <span class="add-datasource-deploy-label">{{ type.label }}</span>
        </button>
      </nav>
      <div class="add-datasource-type-main">
        <Input v-model="keyword" class="add-datasource-type-search" clearable :placeholder="$t('sou-suo-shu-ju-yuan-lei-xing')">
          <template #prefix><Icon type="ios-search" /></template>
        </Input>
        <AppPageTabs v-model="classify" class="add-datasource-classify-tabs" :tabs="classifyTabs" @change="selectedKey = ''" />
        <div class="add-datasource-type-grid" :class="{ 'is-empty': !filteredTypes.length }">
          <button
            v-for="type in filteredTypes"
            :key="type.dsType"
            type="button"
            class="add-datasource-type-card"
            :class="{ active: selectedKey === type.dsType }"
            :aria-pressed="selectedKey === type.dsType"
            @click="selectedKey = type.dsType"
          >
            <span class="add-datasource-type-icon" :class="{ 'is-goldendb': ['GoldenDBMySQL', 'GoldenDBOracle'].includes(type.dsType) }">
              <DataSourceIcon size="20px" :type="type.dsType" leftMargin="0" />
            </span>
            <span class="add-datasource-type-name" :title="type.displayName">{{ type.displayName }}</span>
          </button>
          <div v-if="!filteredTypes.length" class="add-datasource-type-empty">{{ $t('zan-wu-shu-ju') }}</div>
        </div>
      </div>
    </div>
    <template #footer>
      <div class="add-datasource-type-footer">
        <Button @click="$emit('update:modelValue', false)">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :disabled="!selectedType" @click="confirm">{{ $t('que-ding') }}</Button>
      </div>
    </template>
  </CCModal>
</template>

<script>
import { mapState } from 'vuex';
import AppPageTabs from '@/components/layout/AppPageTabs';
import CustomIcon from '@/components/function/CustomIcon';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import { flattenDsSupportNameGroups, normalizeDsSupportNameGroups } from '@/utils/datasourceSupport';
import { DEPLOY_TYPE_ICONS, DEPLOY_TYPE_LABELS, SELF_MANAGED } from '@/utils/datasourceDeploy';

export default {
  name: 'DataSourceTypeModal',
  components: { AppPageTabs, CustomIcon, DataSourceIcon },
  props: {
    modelValue: Boolean,
    initialSelection: { type: Object, default: null }
  },
  emits: ['update:modelValue', 'select'],
  data() {
    return { deployType: SELF_MANAGED, classify: 'ALL', keyword: '', selectedKey: '' };
  },
  computed: {
    ...mapState(['dmGlobalSetting', 'globalDsSetting']),
    deployTypes() {
      return Object.entries(DEPLOY_TYPE_LABELS).map(([name, label]) => ({
        name,
        label: this.$t(label),
        icon: DEPLOY_TYPE_ICONS[name]
      }));
    },
    availableTypes() {
      return flattenDsSupportNameGroups(normalizeDsSupportNameGroups(this.dmGlobalSetting.dsSupportNames || []));
    },
    filteredTypes() {
      const keyword = this.keyword.trim().toLowerCase();
      return this.availableTypes
        .filter(
          (type) =>
            type.deployTypes.includes(this.deployType) &&
            (this.classify === 'ALL' || this.globalDsSetting[type.dsKey].classify === this.classify) &&
            [type.displayName, type.dsKey].some((name) => name.toLowerCase().includes(keyword))
        )
        .map((type) => ({ dsType: type.dsKey, displayName: type.displayName, deployType: this.deployType }));
    },
    classifyTabs() {
      return [
        ['ALL', 'quan-bu'],
        ['RDB', 'guan-xi-xing-shu-ju-ku'],
        ['CACHE', 'huan-cun'],
        ['DOCUMENT', 'datasource-classify-document'],
        ['MQ', 'xiao-xi-dui-lie'],
        ['SEARCH', 'datasource-classify-search']
      ].map(([name, labelKey]) => ({ name, label: this.$t(labelKey) }));
    },
    selectedType() {
      return this.filteredTypes.find((type) => type.dsType === this.selectedKey);
    }
  },
  watch: {
    modelValue(open) {
      if (open) {
        this.keyword = '';
        this.classify = 'ALL';
        this.deployType = this.initialSelection?.deployType || SELF_MANAGED;
        this.selectedKey = this.initialSelection?.dsType || '';
      }
    }
  },
  methods: {
    selectDeployType(type) {
      if (type === this.deployType) {
        return;
      }
      this.deployType = type;
      this.selectedKey = '';
    },
    confirm() {
      if (this.selectedType) {
        this.$emit('select', this.selectedType);
        this.$emit('update:modelValue', false);
      }
    }
  }
};
</script>

<style lang="less">
.add-datasource-type-modal {
  .ant-modal-content {
    overflow: hidden;
    border: 1px solid var(--border-primary);
    border-radius: 10px !important;
    box-shadow: 0 12px 32px rgba(15, 23, 42, 0.18);
  }

  .ant-modal-body,
  .ivu-modal-body {
    padding: 20px 24px 12px;
  }

  .ant-modal-footer,
  .ivu-modal-footer {
    margin-top: 0;
    padding: 8px 24px 20px;
    border-top: 0;
  }
}

.add-datasource-type-modal-body {
  display: flex;
  gap: 16px;
  height: min(408px, calc(100dvh - 220px));
  min-height: min(408px, calc(100dvh - 220px));
}

.add-datasource-deploy-list {
  display: flex;
  flex: 0 0 128px;
  flex-direction: column;
  gap: 8px;
  overflow-y: auto;
}

.add-datasource-deploy-card {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  min-height: 40px;
  padding: 8px 12px;
  border: 1px solid var(--border-primary);
  border-radius: 6px;
  background: var(--bg-card);
  color: var(--text-primary);
  font-size: 13px;
  font-weight: 500;
  text-align: left;
  cursor: pointer;
  transition:
    border-color 0.16s ease,
    background-color 0.16s ease,
    color 0.16s ease;

  &:hover {
    border-color: var(--primary-color);
    background: var(--bg-hover);
    color: var(--primary-color);
  }

  &.active {
    border-color: var(--primary-color);
    background: var(--bg-secondary);
    color: var(--primary-color);
  }

  &:focus-visible {
    outline: 2px solid var(--primary-color);
    outline-offset: 2px;
  }
}

.add-datasource-deploy-icon {
  display: inline-flex;
  flex: 0 0 18px;
  align-items: center;
  justify-content: center;
}

.add-datasource-deploy-card .add-datasource-deploy-label {
  font-size: inherit;
  line-height: 18px;
}

.add-datasource-type-main {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  gap: 16px;
  min-width: 0;
  min-height: 0;
}

@media (max-width: 767px) {
  .add-datasource-type-modal-body {
    gap: 12px;
  }

  .add-datasource-deploy-list {
    flex-basis: 88px;
  }

  .add-datasource-deploy-card {
    gap: 4px;
    padding: 8px 6px;
    font-size: 12px;
  }
}

.add-datasource-type-search {
  width: 320px;
  max-width: 100%;
  flex: 0 0 auto;
}

.add-datasource-classify-tabs {
  flex: 0 0 auto;
}

.add-datasource-type-grid {
  display: grid;
  flex: 1 1 auto;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  align-content: start;
  gap: 8px;
  min-height: 0;
  min-width: 0;
  overflow-y: auto;
  padding: 0 4px 0 0;

  &.is-empty {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  @media (max-width: 767px) {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  @media (max-width: 479px) {
    grid-template-columns: minmax(0, 1fr);
  }
}

.add-datasource-type-card {
  display: flex;
  min-width: 0;
  min-height: 44px;
  align-items: center;
  gap: 8px;
  border: 1px solid #e0e6ee;
  border-radius: 6px;
  padding: 0 12px;
  background: #ffffff;
  color: #1f2937;
  cursor: pointer;
  text-align: left;
  transition:
    border-color 0.16s ease,
    box-shadow 0.16s ease,
    background-color 0.16s ease;

  &:focus-visible {
    outline: 2px solid var(--primary-color);
    outline-offset: 2px;
  }

  &:hover {
    border-color: #41d28f;
    background: var(--bg-hover);
    box-shadow: inset 0 0 0 1px #41d28f;
  }

  &.active {
    border-color: #18ae66;
    background: var(--sidebar-active-bg);
    box-shadow: inset 0 0 0 1px #18ae66;
  }
}

.add-datasource-type-icon {
  display: inline-flex;
  width: 20px;
  flex: 0 0 20px;
  align-items: center;
  justify-content: center;

  &.is-goldendb {
    width: 40px;
    flex-basis: 40px;
  }
}

.add-datasource-type-name {
  min-width: 0;
  font-size: 13px;
  font-weight: 500;
  line-height: 17px;
  white-space: normal;
  word-break: normal;
  overflow-wrap: anywhere;
}

.add-datasource-type-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary);
  font-size: 13px;
}

.add-datasource-type-footer {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;

  button {
    min-width: 92px;
  }
}
</style>
