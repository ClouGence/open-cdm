<script>
import { mapActions, mapState } from 'vuex';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import TicketEditor from '@/components/editor/TicketEditor';
import { EMPTY_FORCE_RULE_MODAL } from '@/const';
import DataSourceRangeTags from '@/views/security/components/DataSourceRangeTags';
import { cloneDeep as deepClone } from '@/utils/lodash';

export default {
  name: 'RuleDetail',
  components: { DataSourceRangeTags, TicketEditor, ReadOnlyEditor },
  data() {
    return {
      supportTargets: [],
      type: 'create',
      forceRuleModal: deepClone(EMPTY_FORCE_RULE_MODAL),
      ruleParamColumns: [
        {
          title: this.$t('ming-cheng'),
          key: 'name'
        },
        {
          title: this.$t('ti-shi'),
          key: 'hint'
        },
        {
          title: this.$t('mo-ren-zhi'),
          key: 'defaultValue'
        },
        {
          title: this.$t('lei-xing'),
          key: 'type'
        },
        {
          title: this.$t('fan-wei'),
          key: 'range'
        }
      ],
      ruleParamList: [],
      ruleFormValidate: {
        ruleKind: {
          required: true,
          message: this.$t('gui-ze-lei-xing-bu-neng-wei-kong')
        },
        ruleName: {
          required: true,
          message: this.$t('gui-ze-ming-cheng-bu-neng-wei-kong')
        },
        dsRange: {
          required: true,
          message: this.$t('shu-ju-yuan-bu-neng-wei-kong')
        },
        targetType: {
          required: false,
          message: this.$t('dui-xiang-lei-xing-bu-neng-wei-kong')
        }
      },
      ruleForm: {
        ruleId: null,
        ruleKind: '',
        force: false,
        ruleName: '',
        ruleDesc: '',
        ruleType: 'DetectRules',
        ruleContent: '',
        dsRange: [],
        targetType: '',
        senMode: 'ROW'
      }
    };
  },
  computed: {
    ...mapState(['ruleSetting']),
    isView() {
      return this.type === 'view';
    },
    querySupportDs() {
      return this.ruleSetting?.queryConf?.supportDs || [];
    },
    saveButtonText() {
      return this.$t('que-ren');
    }
  },
  async mounted() {
    await this.getRuleSetting();
    if (this.$route.query.ruleKind) {
      this.ruleForm.ruleKind = this.$route.query.ruleKind;
    } else {
      this.ruleForm.ruleKind = 'QUERY';
    }
    if (this.$route.query.type) {
      this.type = this.$route.query.type;
      this.ruleForm.ruleId = this.$route.params.id;
      this.handleGetRuleDetail();
    }
  },
  methods: {
    ...mapActions(['getRuleSetting']),
    async handleGetRuleDetail() {
      const res = await this.$services.dmSecurityRulesRuleDetail({
        data: {
          ruleId: this.ruleForm.ruleId,
          ruleKind: this.ruleForm.ruleKind
        }
      });

      if (res.success) {
        const { ruleName, ruleDesc, ruleType, senMode, targetType, dsRange, ruleParameter, ruleContent, ruleKind } = res.data;
        this.ruleForm = {
          ...this.ruleForm,
          ruleContent,
          ruleKind,
          ruleName,
          ruleDesc,
          ruleType,
          senMode,
          targetType,
          dsRange
        };
        this.ruleParamList = ruleParameter;
        this.handleDsRangChange(dsRange, false);
        if (this.$refs.ruleEditor) {
          this.$refs.ruleEditor.setSql(ruleContent);
        }
      }
    },
    async handleExtractParam() {
      const res = await this.$services.dmSecurityRulesRuleExtract({
        data: {
          type: this.ruleForm.ruleType || 'DetectRules',
          content: this.$refs.ruleEditor.getSql()
        }
      });

      if (res.success) {
        this.ruleParamList = res.data;
      }
    },
    handleCloseModal() {
      this.forceRuleModal = deepClone(EMPTY_FORCE_RULE_MODAL);
    },
    handleRuleKindChange(ruleKind) {
      if (ruleKind !== 'QUERY') {
        this.ruleForm.dsRange = [];
        this.ruleForm.targetType = '';
        this.supportTargets = [];
      }
    },
    handleDsRangChange(dsRange, shouldValidate = true) {
      this.ruleForm.dsRange = Array.isArray(dsRange) ? dsRange : [];
      const targetsList = [];
      const targetsMap = this.ruleSetting?.queryConf?.targets || {};
      if (dsRange && Array.isArray(dsRange)) {
        dsRange.forEach((dsType) => {
          if (targetsMap[dsType]) {
            targetsList.push(targetsMap[dsType]);
          }
        });
      }

      this.supportTargets = targetsList.length ? targetsList.reduce((data, item) => data.filter((i) => item.some((j) => i.name === j.name))) : [];
      if (!this.supportTargets.some((target) => target.name === this.ruleForm.targetType)) {
        this.ruleForm.targetType = '';
      }
      if (shouldValidate && this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('dsRange');
      }
    },
    async handleEditRule(force = false) {
      this.$refs.ruleForm.validate(async (valid) => {
        if (valid) {
          const data = this.forceRuleModal.show
            ? this.forceRuleModal.data
            : {
                ...this.ruleForm,
                force,
                content: this.$refs.ruleEditor.getSql()
              };
          const res = await this.$services.dmSecurityRulesRuleSave({
            data
          });

          if (res.success) {
            if (res.data) {
              if (res.data.success) {
                this.$Message.success(res.data.message);
                this.forceRuleModal = deepClone(EMPTY_FORCE_RULE_MODAL);
                await this.$router.push({
                  path: '/data-access/rules',
                  query: {
                    ruleKind: this.ruleForm.ruleKind
                  }
                });
              } else {
                this.forceRuleModal.show = true;
                this.forceRuleModal.event = this.handleEditRule;
                this.forceRuleModal.data = data;
                this.forceRuleModal.text = res.data.message;
                this.forceRuleModal.title = this.$t('qiang-zhi-xiu-gai');
                this.forceRuleModal.refererList = res.data.referer;
              }
            }
          }
        }
      });
    }
  }
};
</script>

<template>
  <div class="rule-detail-container">
    <div class="rule-detail-layout">
      <div class="rule-config-card">
        <div class="rule-section-title">{{ $t('gui-ze-pei-zhi') }}</div>
        <Form ref="ruleForm" class="rule-config-form" :model="ruleForm" :rules="ruleFormValidate" label-position="top">
          <FormItem :label="$t('gui-ze-lei-xing')" prop="ruleKind">
            <Select v-model="ruleForm.ruleKind" :disabled="isView" clearable @on-change="handleRuleKindChange">
              <Option value="QUERY">{{ $t('cha-xun') }}</Option>
              <Option value="SENSITIVE">{{ $t('tuo-min') }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('gui-ze-ming-cheng')" prop="ruleName">
            <Input v-model="ruleForm.ruleName" :disabled="isView" clearable maxlength="64" :placeholder="$t('qing-shu-ru-gui-ze-ming-cheng')" />
          </FormItem>
          <FormItem :label="$t('gui-ze-miao-shu')">
            <Input
              v-model="ruleForm.ruleDesc"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 3 }"
              :disabled="isView"
              clearable
              maxlength="200"
              :placeholder="$t('qing-shu-ru-gui-ze-miao-shu-ke-xuan')"
            />
          </FormItem>
          <FormItem class="rule-ds-range-form-item" :label="$t('shu-ju-yuan')" v-if="ruleForm.ruleKind === 'QUERY'" prop="dsRange">
            <DataSourceRangeTags v-if="isView" :ds-range="ruleForm.dsRange" />
            <DataSourceRangeTags
              v-else
              v-model="ruleForm.dsRange"
              selectable
              :options="querySupportDs"
              :placeholder="$t('qing-xuan-ze-shu-ju-yuan')"
              @change="handleDsRangChange"
            />
          </FormItem>
          <FormItem :label="$t('dui-xiang-lei-xing')" v-if="ruleForm.ruleKind === 'QUERY'" prop="targetType">
            <Select v-model="ruleForm.targetType" :disabled="isView" clearable :placeholder="$t('qing-xuan-ze-dui-xiang-lei-xing-ke-xuan')">
              <Option v-for="target in supportTargets" :value="target.name" :key="target.name">
                {{ target.i18n }}
              </Option>
            </Select>
          </FormItem>
        </Form>
      </div>
      <div class="rule-workspace">
        <div class="rule-panel rule-script-panel">
          <div class="rule-panel-header rule-script-header">
            <span>{{ $t('jiao-ben-nei-rong') }}</span>
            <Button type="primary" @click="handleEditRule(false)" v-if="!isView">
              {{ saveButtonText }}
            </Button>
          </div>
          <div class="rule-editor-content">
            <ReadOnlyEditor :text="ruleForm.ruleContent" :border="0" v-if="isView" />
            <TicketEditor ref="ruleEditor" v-else />
          </div>
        </div>
        <div class="rule-panel rule-param-panel">
          <div class="rule-panel-header rule-param-header">
            <span class="rule-param-title">{{ $t('can-shu') }}</span>
            <button type="button" class="rule-param-extract-button" @click="handleExtractParam" v-if="!isView">
              {{ $t('ti-qu-can-shu') }}
            </button>
          </div>
          <Table :columns="ruleParamColumns" :data="ruleParamList" size="small" border :locale="{ emptyText: $t('zan-wu-shu-ju') }">
            <template #empty>
              <span class="rule-param-empty-text">{{ $t('zan-wu-shu-ju') }}</span>
            </template>
          </Table>
        </div>
      </div>
    </div>
    <Modal
      v-model="forceRuleModal.show"
      :title="forceRuleModal.title"
      @on-cancel="handleCloseModal"
      @on-ok="forceRuleModal.event(true)"
      :ok-text="forceRuleModal.title"
    >
      <div class="title" v-html="forceRuleModal.text" style="margin-bottom: 10px"></div>
      <Table :columns="forceRuleModal.refererColumns" :data="forceRuleModal.refererList" size="small" />
    </Modal>
  </div>
</template>

<style scoped lang="less">
.rule-detail-container {
  padding: 16px;
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  min-height: 0;
  overflow: auto;
  background: #fff;
}

.rule-detail-layout {
  display: grid;
  grid-template-columns: minmax(340px, 420px) minmax(0, 1fr);
  gap: 16px;
  flex: 1;
  min-height: 0;
}

.rule-config-card,
.rule-panel {
  background: #fff;
  border: 1px solid #e5e6eb;
  border-radius: 8px;
}

.rule-config-card {
  min-width: 0;
  padding: 20px;
  overflow: auto;
}

.rule-section-title,
.rule-panel-header {
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
}

.rule-section-title {
  margin-bottom: 18px;
}

.rule-config-form {
  :deep(.ivu-form-item) {
    margin-bottom: 18px;
  }

  :deep(.ivu-form-item-label) {
    color: #5b667a;
    font-weight: 600;
  }

  :deep(.ivu-form-item-error-tip) {
    position: static !important;
    top: auto;
    left: auto;
    padding-top: 6px;
    line-height: 18px;
  }
}

.rule-workspace {
  display: grid;
  grid-template-rows: minmax(0, 1fr) 240px;
  gap: 16px;
  min-width: 0;
  min-height: 0;
}

.rule-panel {
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 0;
  overflow: hidden;
}

.rule-panel-header {
  display: flex;
  align-items: center;
  height: 48px;
  padding: 0 18px;
  border-bottom: 1px solid #e5e6eb;
}

.rule-script-header {
  justify-content: space-between;
}

.rule-panel-header .ivu-btn {
  margin-left: 10px;
}

.rule-param-header {
  align-items: center;
}

.rule-param-title,
.rule-param-extract-button {
  display: inline-flex;
  align-items: baseline;
  height: 22px;
  font-size: 16px;
  font-weight: 600;
  line-height: 22px;
}

.rule-param-title {
  color: #1f2937;
}

.rule-param-extract-button {
  margin-left: 12px;
  padding: 0;
  color: #24a877;
  font-family: inherit;
  background: transparent;
  border: 0;
  outline: none;
  cursor: pointer;
}

.rule-param-extract-button:hover {
  color: #139766;
}

.rule-editor-content {
  flex: 1;
  min-height: 0;
  overflow: hidden;

  :deep(.read-only-editor-wrapper),
  :deep(.read-only-editor) {
    height: 100% !important;
  }
}

.rule-param-panel {
  :deep(.ivu-table-wrapper),
  :deep(.ivu-table) {
    border-radius: 0;
  }
}

.rule-param-empty-text {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 58px;
  color: #9aa3b2;
}

@media (max-width: 1180px) {
  .rule-detail-layout {
    grid-template-columns: 1fr;
    flex: none;
  }

  .rule-config-card {
    overflow: visible;
  }

  .rule-workspace {
    grid-template-rows: 420px 240px;
    min-height: 676px;
  }
}
</style>
