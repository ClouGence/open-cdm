<template>
  <aside class="release-flow-summary page-aside">
    <div class="summary-title">
      <div class="summary-title-main">
        <CustomIcon type="icon-v2-jiaobenrenwu" size="24px" />
        <span>{{ $t('pei-zhi-zhai-yao') }}</span>
      </div>
      <Button type="text" class="summary-help-link" @click="$emit('open-help')">
        <Icon type="ios-help-circle-outline" />
        <span>{{ $t('shi-yong-zhi-nan') }}</span>
      </Button>
    </div>

    <div class="summary-body">
      <div class="summary-group">
        <h3>{{ $t('ji-ben-xin-xi') }}</h3>
        <div v-if="createMode" class="summary-row">
          <span>{{ $t('xiang-mu-ming-cheng') }}</span>
          <strong>{{ summaryValue(flowBasicForm.flowName) }}</strong>
        </div>
        <div v-if="createMode" class="summary-row">
          <span>{{ $t('miao-shu') }}</span>
          <strong>{{ summaryValue(flowBasicForm.flowDesc) }}</strong>
        </div>
        <div v-if="createMode" class="summary-row">
          <span>{{ $t('fu-ze-ren') }}</span>
          <strong>{{ summaryValue(selectedManagerName) }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('bian-geng-lei-xing') }}</span>
          <strong>{{ isBuiltIn ? $t('nei-zhi-bian-geng-liu') : $t('nav-git-ops') }}</strong>
        </div>
        <div v-if="isBuiltIn" class="summary-row">
          <span>{{ $t('shang-ji-bian-geng-liu') }}</span>
          <strong>{{ summaryValue(selectedParentFlowName) }}</strong>
        </div>
        <div v-if="!isBuiltIn" class="summary-row">
          <span>{{ $t('nav-git-ops') }}</span>
          <strong>{{ summaryValue(selectedScmName) }}</strong>
        </div>
        <div v-if="!isBuiltIn" class="summary-row">
          <span>{{ $t('cang-ku') }}</span>
          <strong>{{ summaryValue(flowGitOpsForm.repoName) }}</strong>
        </div>
        <div v-if="!isBuiltIn" class="summary-row">
          <span>{{ $t('mu-biao-fen-zhi') }}</span>
          <strong>{{ summaryValue(flowGitOpsForm.repoBranch) }}</strong>
        </div>
        <div v-if="!isBuiltIn" class="summary-row">
          <span>{{ $t('jiao-ben-lu-jin') }}</span>
          <strong>{{ summaryValue(flowGitOpsForm.repoScriptPath) }}</strong>
        </div>
        <div v-if="!isBuiltIn" class="summary-row">
          <span>{{ $t('chu-fa-fang-shi') }}</span>
          <strong>{{ summaryValue(flowGitOpsForm.eventType) }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('shu-ju-ku-lei-xing') }}</span>
          <strong class="summary-value-with-icon">
            <template v-if="flowGitOpsForm.databaseType">
              <CustomIcon :type="flowGitOpsForm.databaseType" size="14px" />
              <span>{{ flowGitOpsForm.databaseType }}</span>
            </template>
            <template v-else>-</template>
          </strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('shi-li-1') }}</span>
          <strong>{{ summaryValue(selectedInstanceName) }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('schema') }}</span>
          <strong>{{ summaryValue(flowGitOpsForm.schemaName) }}</strong>
        </div>
        <div v-if="!isBuiltIn" class="summary-row">
          <span>{{ $t('chu-shi-hua-fang-shi') }}</span>
          <strong>{{ summaryValue(selectedInitLabel) }}</strong>
        </div>
      </div>

      <div v-if="createMode" class="summary-group">
        <h3>{{ $t('tong-zhi-pei-zhi') }}</h3>
        <div class="summary-row">
          <span>{{ $t('tong-zhi-qu-dao') }}</span>
          <strong>{{ summaryValue(summaryImChannel) }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('im-fu-wu') }}</span>
          <strong>{{ summaryValue(selectedImProviderName) }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('ding-yue-xiao-xi') }}</span>
          <strong>{{ subscriptionSummary }}</strong>
        </div>
        <h3>{{ $t('zhi-xing-pei-zhi') }}</h3>
        <div class="summary-row">
          <span>{{ $t('sql-shen-he-0') }}</span>
          <strong>{{ $t('cicd-work-order-auto-analysis') }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('shen-pi-liu') }}</span>
          <strong>{{ $t('cicd-work-order-env-approval') }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('fa-bu-fang-shi') }}</span>
          <strong>{{ $t('cicd-work-order-confirm-execution') }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('shi-yong-shi-wu') }}</span>
          <strong>{{ $t('cicd-work-order-configured-in-ticket') }}</strong>
        </div>
        <div class="summary-row">
          <span>{{ $t('cuo-wu-ce-lve') }}</span>
          <strong>{{ $t('cicd-work-order-configured-in-ticket') }}</strong>
        </div>
      </div>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'ReleaseFlowSummary',
  props: {
    createMode: { type: Boolean, required: true },
    flowBasicForm: { type: Object, required: true },
    flowGitOpsForm: { type: Object, required: true },
    selectedManagerName: { type: String, default: '' },
    selectedScmName: { type: String, default: '' },
    selectedInstanceName: { type: String, default: '' },
    selectedInitLabel: { type: String, default: '' },
    summaryImChannel: { type: String, default: '' },
    selectedImProviderName: { type: String, default: '' },
    subscriptionSummary: { type: String, default: '' },
    isBuiltIn: { type: Boolean, default: false },
    selectedParentFlowName: { type: String, default: '' }
  },
  emits: ['open-help'],
  methods: {
    summaryValue(value) {
      return value || '-';
    }
  }
};
</script>
