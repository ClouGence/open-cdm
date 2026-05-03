<script>
import { mapActions, mapState } from 'vuex';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import TicketEditor from '@/components/editor/TicketEditor';
import { EMPTY_FORCE_RULE_MODAL } from '@/const';
import deepClone from 'lodash.clonedeep';

export default {
  name: 'RuleDetail',
  components: { TicketEditor, ReadOnlyEditor },
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
    }
  },
  mounted() {
    this.getRuleSetting();
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
    handleDsRangChange(dsRange) {
      const targetsList = [];
      if (dsRange && Array.isArray(dsRange)) {
        dsRange.forEach((dsType) => {
          if (this.ruleSetting.queryConf.targets[dsType]) {
            targetsList.push(this.ruleSetting.queryConf.targets[dsType]);
          }
        });
      }

      console.log(targetsList);
      this.supportTargets = targetsList.reduce((data, item) => data.filter((i) => item.some((j) => i.name === j.name)));
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
                  path: '/system/dmrulelist',
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
    <Breadcrumb>
      <BreadcrumbItem :to="`/system/dmrulelist?ruleKind=${ruleForm.ruleKind}`">
        {{ $t('gui-ze-lie-biao') }}
      </BreadcrumbItem>
      <BreadcrumbItem>
        {{ type === 'create' ? $t('xin-jian-gui-ze') : type === 'view' ? $t('gui-ze-xiang-qing') : $t('bian-ji-gui-ze') }}
      </BreadcrumbItem>
    </Breadcrumb>
    <div class="rule-detail">
      <div class="left">
        <div class="editor">
          <div class="title">{{ $t('jiao-ben') }}</div>
          <div class="content">
            <ReadOnlyEditor :text="ruleForm.ruleContent" v-if="isView" />
            <TicketEditor ref="ruleEditor" v-else />
          </div>
        </div>
        <div class="params">
          <div class="title" v-if="!isView">
            {{ $t('can-shu') }}
            <Button @click="handleExtractParam" type="text" size="small">
              {{ $t('ti-qu-can-shu') }}
            </Button>
          </div>
          <div class="content">
            <Table :columns="ruleParamColumns" :data="ruleParamList" size="small" border></Table>
          </div>
        </div>
      </div>
      <div class="right">
        <Form ref="ruleForm" :model="ruleForm" :rules="ruleFormValidate">
          <FormItem :label="$t('gui-ze-lei-xing')" prop="ruleKind">
            <Select v-model="ruleForm.ruleKind" :disabled="isView" clearable>
              <Option value="QUERY">{{ $t('cha-xun') }}</Option>
              <Option value="SENSITIVE">{{ $t('tuo-min') }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('gui-ze-ming-cheng')" prop="ruleName">
            <Input v-model="ruleForm.ruleName" :disabled="isView" clearable />
          </FormItem>
          <FormItem :label="$t('gui-ze-miao-shu')">
            <Input v-model="ruleForm.ruleDesc" type="textarea" :disabled="isView" clearable />
          </FormItem>
          <FormItem :label="$t('shu-ju-yuan')" v-if="ruleForm.ruleKind === 'QUERY'" prop="dsRange">
            <Select v-model="ruleForm.dsRange" multiple :disabled="isView" clearable @on-change="handleDsRangChange">
              <Option v-for="ds in ruleSetting.queryConf.supportDs" :value="ds.name" :key="ds.name">
                {{ ds.i18n }}
              </Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('dui-xiang-lei-xing')" v-if="ruleForm.ruleKind === 'QUERY'" prop="targetType">
            <Select v-model="ruleForm.targetType" :disabled="isView" clearable>
              <Option v-for="target in supportTargets" :value="target.name" :key="target.name">
                {{ target.i18n }}
              </Option>
            </Select>
          </FormItem>
          <FormItem>
            <div style="display: flex; justify-content: flex-end">
              <Button type="primary" @click="handleEditRule(false)" v-if="!isView">
                {{ type === 'create' ? $t('xin-jian-gui-ze') : $t('bian-ji-gui-ze') }}
              </Button>
            </div>
          </FormItem>
        </Form>
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
  padding: 10px;
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;

  .rule-detail {
    margin-top: 10px;
    flex: 1;
    border: 1px solid #ccc;
    display: flex;
    width: 100%;

    .left {
      width: calc(~'100% - 300px');
      border-right: 1px solid #ccc;
      display: flex;
      flex-direction: column;

      .editor {
        flex: 1;
        min-height: 0;
      }

      .params {
        border-top: 1px solid #ccc;
        height: 300px;
      }

      .editor,
      .params {
        width: 100%;
        display: flex;
        flex-direction: column;

        .title {
          padding-left: 10px;
          border-bottom: 1px solid #ccc;
          height: 36px;
          display: flex;
          align-items: center;
          font-weight: bold;
        }

        .content {
          width: 100%;
          flex: 1;
          min-height: 0;
          overflow: auto;
        }
      }
    }

    .right {
      width: 300px;
      padding: 20px;
    }
  }
}
</style>
