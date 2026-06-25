<template>
  <div class="release-flow-success">
    <div class="success-card">
      <CustomIcon type="icon-v2-SuccessColorful" size="72px" />
      <h2>{{ $t('xiang-mu-chuang-jian-cheng-gong') }}</h2>
      <p>{{ $t('xiang-mu-yi-jing-chuang-jian-nin-huan-xu-yao-dao') }} {{ $t('cang-ku-pei-zhi-webhook') }}</p>

      <div class="webhook-fields">
        <div class="webhook-row">
          <span>{{ $t('cang-ku-di-zhi') }}</span>
          <Input :model-value="webhook.repoUrl" readonly>
            <template #suffix>
              <Icon type="ios-link" @click="$emit('open-url', webhook.repoUrl)" />
            </template>
          </Input>
        </div>
        <div class="webhook-row">
          <span>{{ $t('webhook-url') }}</span>
          <Input :model-value="webhook.url" readonly>
            <template #suffix>
              <Icon type="ios-copy" @click="$emit('copy', webhook.url)" />
            </template>
          </Input>
        </div>
        <div class="webhook-row">
          <span>{{ $t('webhook-mi-ma') }}</span>
          <Input :model-value="webhook.password" readonly>
            <template #suffix>
              <Icon type="ios-copy" @click="$emit('copy', webhook.password)" />
            </template>
          </Input>
        </div>
      </div>

      <div class="success-actions">
        <Button type="primary" ghost @click="$emit('jump-doc')">{{ $t('cha-kan-wen-dang') }}</Button>
        <Button type="primary" @click="$emit('go-created-flow')">{{ $t('jin-ru-bian-geng-liu') }}</Button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReleaseFlowSuccess',
  props: {
    webhook: {
      type: Object,
      required: true
    }
  },
  emits: ['copy', 'open-url', 'jump-doc', 'go-created-flow']
};
</script>
