<script>
import { mapGetters } from 'vuex';
import { RULE_WARN_LEVEL } from '@/utils';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import DataSourceRangeTags from '@/views/security/components/DataSourceRangeTags';

export default {
  name: 'SpecRuleDetail',
  components: { DataSourceRangeTags, ReadOnlyEditor },
  data() {
    return {
      loading: false,
      specId: '',
      ruleId: '',
      ruleKind: 'QUERY',
      ruleDetail: {},
      ruleParamColumns: [
        {
          title: this.$t('ming-cheng'),
          key: 'name',
          minWidth: 160
        },
        {
          title: this.$t('miao-shu'),
          key: 'hint',
          minWidth: 260
        },
        {
          title: this.$t('zhi'),
          slot: 'value',
          minWidth: 180
        }
      ],
      RULE_WARN_LEVEL
    };
  },
  computed: {
    ...mapGetters(['getTargetType', 'getSenMode']),
    isQuery() {
      return this.ruleKind === 'QUERY';
    },
    ruleKindText() {
      return this.isQuery ? this.$t('cha-xun-gui-ze') : this.$t('tuo-min-gui-ze');
    },
    targetTypeText() {
      if (!this.ruleDetail.targetType) {
        return '-';
      }
      const targetType = this.getTargetType(this.ruleDetail.targetType);
      return targetType?.i18n || this.ruleDetail.targetTypeI18n || this.ruleDetail.targetType;
    },
    warnLevelText() {
      return this.RULE_WARN_LEVEL[this.ruleDetail.warnLevel] || this.ruleDetail.warnLevel || '-';
    },
    senModeText() {
      if (!this.ruleDetail.senMode) {
        return '-';
      }
      return this.ruleDetail.senModeI18n || this.getSenMode(this.ruleDetail.senMode).i18n || this.ruleDetail.senMode;
    },
    ruleParamList() {
      const parameterDef = Array.isArray(this.ruleDetail.parameterDef) ? this.ruleDetail.parameterDef : [];
      const parameterValue = this.ruleDetail.parameterValue || {};
      return parameterDef.map((param) => {
        const hasValue = Object.prototype.hasOwnProperty.call(parameterValue, param.name);
        return {
          ...param,
          value: hasValue ? parameterValue[param.name] : param.defaultValue
        };
      });
    }
  },
  mounted() {
    this.specId = this.$route.params.specId;
    this.ruleId = this.$route.params.ruleId;
    this.ruleKind = this.$route.query.ruleKind || 'QUERY';
    this.getSpecRuleDetail();
  },
  methods: {
    formatValue(value) {
      return value === undefined || value === null || value === '' ? '-' : value;
    },
    async getSpecRuleDetail() {
      this.loading = true;
      const res = await this.$services.dmSecurityRulesSpecRuleDetail({
        data: {
          specId: this.specId,
          ruleId: this.ruleId,
          ruleKind: this.ruleKind
        }
      });
      this.loading = false;

      if (res.success) {
        this.ruleDetail = res.data || {};
        if (this.ruleDetail.ruleKind) {
          this.ruleKind = this.ruleDetail.ruleKind;
        }
        return;
      }

      this.$Message.error(res.message || this.$t('cao-zuo-shi-bai'));
    }
  }
};
</script>

<template>
  <div class="spec-rule-detail-page">
    <Spin v-if="loading" fix />
    <div v-else class="spec-rule-detail-layout">
      <div class="spec-rule-config-card">
        <div class="spec-rule-section-title">{{ $t('gui-ze-pei-zhi') }}</div>
        <dl class="spec-rule-info-list">
          <div class="spec-rule-info-item">
            <dt>{{ $t('gui-ze-lei-xing') }}</dt>
            <dd>{{ ruleKindText }}</dd>
          </div>
          <div class="spec-rule-info-item">
            <dt>{{ $t('ming-cheng') }}</dt>
            <dd>{{ ruleDetail.ruleName || '-' }}</dd>
          </div>
          <div class="spec-rule-info-item">
            <dt>{{ $t('miao-shu') }}</dt>
            <dd>{{ ruleDetail.ruleDesc || '-' }}</dd>
          </div>
          <div class="spec-rule-info-item" v-if="isQuery">
            <dt>{{ $t('dui-xiang-lei-xing') }}</dt>
            <dd>{{ targetTypeText }}</dd>
          </div>
          <div class="spec-rule-info-item" v-if="isQuery">
            <dt>{{ $t('deng-ji') }}</dt>
            <dd>
              <Tag :color="ruleDetail.warnLevel === 'SUGGEST' ? 'warning' : 'error'">
                {{ warnLevelText }}
              </Tag>
            </dd>
          </div>
          <div class="spec-rule-info-item" v-if="!isQuery">
            <dt>{{ $t('tuo-min-fang-shi') }}</dt>
            <dd>{{ senModeText }}</dd>
          </div>
          <div class="spec-rule-info-item">
            <dt>{{ $t('qi-yong') }}</dt>
            <dd>
              <i-switch v-model="ruleDetail.enable" true-color="#52C41A" disabled />
            </dd>
          </div>
          <div class="spec-rule-info-item spec-rule-info-item-full" v-if="isQuery">
            <dt>{{ $t('shu-ju-yuan') }}</dt>
            <dd>
              <DataSourceRangeTags :ds-range="ruleDetail.dsRange" />
            </dd>
          </div>
        </dl>
      </div>
      <div class="spec-rule-workspace">
        <div class="spec-rule-panel spec-rule-script-panel">
          <div class="spec-rule-panel-header">{{ $t('jiao-ben-nei-rong') }}</div>
          <div class="spec-rule-editor-content">
            <ReadOnlyEditor :text="ruleDetail.ruleContent" :max-height="520" />
          </div>
        </div>
        <div class="spec-rule-panel spec-rule-param-panel">
          <div class="spec-rule-panel-header">{{ $t('can-shu') }}</div>
          <Table :columns="ruleParamColumns" :data="ruleParamList" size="small" border :locale="{ emptyText: $t('zan-wu-shu-ju') }">
            <template #value="{ row }">
              {{ formatValue(row.value) }}
            </template>
            <template #empty>
              <span class="spec-rule-empty-text">{{ $t('zan-wu-shu-ju') }}</span>
            </template>
          </Table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.spec-rule-detail-page {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  min-height: 0;
  padding: 16px;
  overflow: auto;
  background: #fff;
}

.spec-rule-detail-layout {
  display: grid;
  grid-template-columns: minmax(340px, 420px) minmax(0, 1fr);
  gap: 16px;
  min-height: 0;
  flex: 1;
}

.spec-rule-config-card,
.spec-rule-panel {
  min-width: 0;
  background: #fff;
  border: 1px solid #e5e6eb;
  border-radius: 8px;
}

.spec-rule-config-card {
  padding: 20px;
  overflow: auto;
}

.spec-rule-section-title,
.spec-rule-panel-header {
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
}

.spec-rule-section-title {
  margin-bottom: 18px;
}

.spec-rule-info-list {
  margin: 0;
}

.spec-rule-info-item {
  margin-bottom: 18px;
}

.spec-rule-info-item dt {
  margin-bottom: 8px;
  color: #5b667a;
  font-weight: 600;
  line-height: 22px;
}

.spec-rule-info-item dd {
  min-height: 32px;
  margin: 0;
  color: #1f2937;
  line-height: 22px;
  word-break: break-word;
}

.spec-rule-info-item-full dd {
  min-height: 0;
}

.spec-rule-workspace {
  display: grid;
  grid-template-rows: minmax(360px, 1fr) 240px;
  gap: 16px;
  min-width: 0;
  min-height: 0;
}

.spec-rule-panel {
  display: flex;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
}

.spec-rule-panel-header {
  display: flex;
  align-items: center;
  height: 48px;
  padding: 0 18px;
  border-bottom: 1px solid #e5e6eb;
}

.spec-rule-editor-content {
  flex: 1;
  min-height: 0;
  overflow: auto;
}

.spec-rule-param-panel {
  :deep(.ivu-table-wrapper),
  :deep(.ivu-table) {
    border-radius: 0;
  }
}

.spec-rule-empty-text {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 58px;
  color: #9aa3b2;
}

@media (max-width: 1180px) {
  .spec-rule-detail-layout {
    grid-template-columns: 1fr;
  }

  .spec-rule-workspace {
    grid-template-rows: 420px 240px;
  }
}
</style>
