<template>
  <section v-if="isCreateMode" class="flow-section-card basic-info-card">
    <div class="accent-title">
      <span>{{ $t('ji-ben-xin-xi') }}</span>
    </div>
    <Form ref="basicForm" :model="flowBasicForm" :rules="basicRules" label-position="top" class="basic-form">
      <FormItem :label="$t('xiang-mu-ming-cheng')" prop="flowName">
        <Input v-model="flowBasicForm.flowName" :placeholder="$t('qing-shu-ru-bian-geng-liu-cheng-ming-cheng')" />
      </FormItem>
      <FormItem :label="$t('miao-shu')" prop="flowDesc">
        <Input v-model="flowBasicForm.flowDesc" :placeholder="$t('qing-shu-ru-miao-shu-ke-xuan')" />
      </FormItem>
      <FormItem :label="$t('fu-ze-ren')" prop="flowManagerUid">
        <Select
          ref="managerSelect"
          v-model="flowBasicForm.flowManagerUid"
          placement="bottom-start"
          transfer
          transfer-class-name="release-flow-select-dropdown"
          events-enabled
          filterable
          @on-open-change="$emit('select-open-change', $event, $refs.managerSelect)"
        >
          <template #prefix>
            <CustomIcon type="icon-v2-svg-USER" rightMargin />
          </template>
          <Option v-for="item in devopsUsers" :value="item.userUid" :key="item.userUid" :label="item.userName">
            {{ item.userName }}
          </Option>
        </Select>
      </FormItem>
    </Form>
  </section>
</template>

<script>
export default {
  name: 'ReleaseFlowBasicInfo',
  props: {
    isCreateMode: {
      type: Boolean,
      required: true
    },
    flowBasicForm: {
      type: Object,
      required: true
    },
    basicRules: {
      type: Object,
      required: true
    },
    devopsUsers: {
      type: Array,
      required: true
    }
  },
  emits: ['select-open-change'],
  methods: {
    validate() {
      return this.$refs.basicForm?.validate?.() ?? Promise.resolve(true);
    }
  }
};
</script>
