<template>
  <CCModal
    :modelValue="visible"
    @update:modelValue="(val) => $emit('update:visible', val)"
    :title="title"
    :width="width"
    :mask-closable="false"
    @on-cancel="handleEmptyVerifyCodeModalData"
  >
    <div>
      <slot />
      <p>
        <Input type="text" v-model="mfaCode" :placeholder="$t('qing-shu-ru-liu-wei-shu-de-mfa-yan-zheng-ma')" />
        <Alert class="mt-4" type="error" v-if="errMsg" show-icon>{{ errMsg }}</Alert>
      </p>
    </div>
    <template #footer>
      <Button @click="handleEmptyVerifyCodeModalData">{{ $t('guan-bi') }}</Button>
      <Button v-if="hasNextStep" type="primary" @click="handleConfirm">
        {{ $t('xia-yi-bu') }}
      </Button>
      <Button v-if="!hasNextStep" type="primary" :loading="loading" @click="handleConfirm">
        {{ $t('que-ding') }}
      </Button>
    </template>
  </CCModal>
</template>

<script>
export default {
  name: 'VerifyMfaModal',
  props: {
    visible: Boolean,
    title: String,
    loading: Boolean,
    handleCloseModal: Function,
    handleConfirmCallback: Function,
    footerHide: Boolean,
    width: { type: [Number, String], default: 400 },
    hasNextStep: {
      type: Boolean,
      default: false
    },
    errMsg: String
  },
  data() {
    return {
      mfaCode: ''
    };
  },
  methods: {
    handleEmptyData() {
      this.mfaCode = '';
    },
    handleEmptyVerifyCodeModalData() {
      this.handleEmptyData();
      this.handleCloseModal();
    },
    handleConfirm() {
      this.handleConfirmCallback(this.mfaCode, true);
      this.handleEmptyData();
    }
  }
};
</script>

<style scoped></style>
