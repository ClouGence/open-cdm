<template>
  <section class="page-section flow-notice-section">
    <div class="page-section__title">{{ $t('tong-zhi-pei-zhi') }}</div>
    <div class="notice-layout">
      <div class="notice-channel-panel">
        <div class="field-label required notice-section-label">{{ $t('tong-zhi-qu-dao') }}</div>
        <div class="channel-grid channel-type-card-group">
          <button
            v-for="im in imDefList"
            :key="im.imType"
            type="button"
            :class="{ active: imDefSelected.imType === im.imType }"
            class="type-card channel-card"
            @click="$emit('im-def-select', im)"
          >
            <CustomIcon v-if="im.imType === 'none'" type="Disable" size="18px" />
            <CustomIcon v-else-if="im.iconResource" :resource="im.iconResource" :alt="im.imTypeI18n" size="18px" />
            <span>{{ im.imTypeI18n }}</span>
          </button>
        </div>

        <div class="notice-form-row">
          <Form label-position="top">
            <FormItem :label="$t('im-fu-wu-ti-gong-fang')">
              <Select
                ref="imProviderSelect"
                v-model="flowImForm.imId"
                :disabled="isImDisabled"
                :placeholder="$t('qing-xuan-ze-yi-ge-im-ti-gong-zhe')"
                :not-found-text="$t('zan-wu-shu-ju')"
                placement="bottom-start"
                transfer
                transfer-class-name="release-flow-select-dropdown"
                events-enabled
                @on-change="$emit('im-provider-change', $event)"
                @on-open-change="$emit('select-open-change', $event, $refs.imProviderSelect)"
              >
                <template #prefix>
                  <CustomIcon v-if="imDefSelected.imType === 'none'" type="Disable" rightMargin />
                  <CustomIcon
                    v-else-if="imDefSelected.iconResource"
                    :resource="imDefSelected.iconResource"
                    :alt="imDefSelected.imTypeI18n"
                    size="20px"
                    rightMargin
                  />
                </template>
                <Option v-for="item in imProviderList" :key="item.imId" :value="item.imId" :label="item.display" :disabled="!item.enable">
                  {{ item.display }}
                </Option>
              </Select>
            </FormItem>
          </Form>
        </div>
      </div>

      <div class="subscription-panel">
        <div class="subscription-title">{{ $t('ding-yue-xiao-xi') }}</div>
        <div class="subscription-list">
          <div v-for="item in subscriptionItems" :key="item.key" class="subscription-row">
            <i-switch v-model="flowImForm[item.key]" true-color="#18b566" :disabled="isImDisabled" />
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'ReleaseFlowNoticeConfig',
  props: {
    flowImForm: { type: Object, required: true },
    imDefList: { type: Array, required: true },
    imDefSelected: { type: Object, required: true },
    isImDisabled: { type: Boolean, required: true },
    imProviderList: { type: Array, required: true },
    subscriptionItems: { type: Array, required: true }
  },
  emits: ['im-def-select', 'im-provider-change', 'select-open-change']
};
</script>
