<template>
  <section class="page-section flow-execute-section">
    <div class="page-section__title">{{ $t('zhi-xing-pei-zhi') }}</div>
    <div class="flow-config-list">
      <div v-for="item in configItems" :key="item.key" class="flow-config-row" :class="{ 'flow-config-row-reserved': item.reserved }">
        <div class="flow-config-label">
          <span>{{ item.title }}</span>
          <Tooltip :content="changeFlowDescription(item.type, item.value)">
            <Icon type="ios-information-circle-outline" />
          </Tooltip>
        </div>
        <div class="flow-config-control">
          <RadioGroup v-model="flowBasicForm[item.model]" class="flow-config-radio-row" :class="item.radioClass" @on-change="item.onChange">
            <Radio v-for="option in item.options" :key="option.value" :label="option.value" :disabled="item.disableWhenNotAuto && !flowExecuteIsAuto">
              {{ option.label }}
            </Radio>
          </RadioGroup>
          <div class="field-hint flow-config-hint" :class="{ 'flow-config-hint-reserved': item.reserved }">
            {{ changeFlowDescription(item.type, item.value) }}
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'ReleaseFlowExecuteConfig',
  props: {
    flowBasicForm: { type: Object, required: true },
    checkOptions: { type: Array, required: true },
    approveOptions: { type: Array, required: true },
    publishOptions: { type: Array, required: true },
    transactionalOptions: { type: Array, required: true },
    errorOptions: { type: Array, required: true },
    flowExecuteIsAuto: { type: Boolean, required: true },
    changeFlowDescription: { type: Function, required: true }
  },
  emits: ['execute-strategy-change'],
  computed: {
    configItems() {
      return [
        {
          key: 'check',
          type: 'check',
          model: 'checkStrategy',
          title: this.$t('sql-shen-he-0'),
          value: this.flowBasicForm.checkStrategy,
          options: this.checkOptions,
          radioClass: 'strategy-radio-row'
        },
        {
          key: 'approve',
          type: 'approve',
          model: 'approveStrategy',
          title: this.$t('shen-pi-liu'),
          value: this.flowBasicForm.approveStrategy,
          options: this.approveOptions,
          radioClass: 'strategy-radio-row'
        },
        {
          key: 'execute',
          type: 'execute',
          model: 'executeStrategy',
          title: this.$t('fa-bu-fang-shi'),
          value: this.flowBasicForm.executeStrategy,
          options: this.publishOptions,
          radioClass: 'execution-radio-row',
          onChange: (value) => this.$emit('execute-strategy-change', value)
        },
        {
          key: 'transactional',
          type: 'transactional',
          model: 'transactional',
          title: this.$t('shi-yong-shi-wu'),
          value: this.flowBasicForm.transactional,
          options: this.transactionalOptions,
          radioClass: 'execution-radio-row',
          reserved: true,
          disableWhenNotAuto: true
        },
        {
          key: 'error',
          type: 'error',
          model: 'errorStrategy',
          title: this.$t('cuo-wu-ce-lve'),
          value: this.flowBasicForm.errorStrategy,
          options: this.errorOptions,
          radioClass: 'execution-radio-row',
          reserved: true,
          disableWhenNotAuto: true
        }
      ];
    }
  }
};
</script>
