<template>
  <section class="page-section flow-execute-section">
    <div class="page-section__title">{{ $t('zhi-xing-pei-zhi') }}</div>
    <div class="flow-config-list">
      <div v-for="item in configItems" :key="item.key" class="flow-config-row">
        <div class="flow-config-label">{{ item.title }}</div>
        <div class="flow-config-control">
          <div class="work-order-config-value">
            <Icon type="ios-checkmark-circle-outline" />
            <span>{{ item.value }}</span>
          </div>
          <div class="field-hint flow-config-hint">{{ item.description }}</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'ReleaseFlowWorkOrderConfig',
  computed: {
    configItems() {
      return [
        {
          key: 'analysis',
          title: this.$t('sql-shen-he-0'),
          value: this.$t('cicd-work-order-auto-analysis'),
          description: this.$t('cicd-work-order-auto-analysis-description')
        },
        {
          key: 'approval',
          title: this.$t('shen-pi-liu'),
          value: this.$t('cicd-work-order-env-approval'),
          description: this.$t('cicd-work-order-env-approval-description')
        },
        {
          key: 'execution',
          title: this.$t('fa-bu-fang-shi'),
          value: this.$t('cicd-work-order-confirm-execution'),
          description: this.$t('cicd-work-order-confirm-execution-description')
        },
        {
          key: 'transaction',
          title: this.$t('shi-yong-shi-wu'),
          value: this.$t('cicd-work-order-configured-in-ticket'),
          description: this.$t('cicd-work-order-transaction-description')
        },
        {
          key: 'error',
          title: this.$t('cuo-wu-ce-lve'),
          value: this.$t('cicd-work-order-configured-in-ticket'),
          description: this.$t('cicd-work-order-error-description')
        }
      ];
    }
  }
};
</script>
