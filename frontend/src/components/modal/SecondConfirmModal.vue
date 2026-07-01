<template>
  <CCModal
    :title="title"
    :modelValue="visible"
    @update:modelValue="(val) => $emit('update:visible', val)"
    @on-ok="handleOk"
    @on-cancel="handleCancel"
    width="600px"
  >
    <slot />
    <Alert v-if="text">
      {{ $t('qing-shu-ru-dong-zuo-ming-cheng') }}
      <span class="warn-font">{{ confirmTarget }}</span>
      {{ text }}
    </Alert>
    <Input v-model="inputEvent" @on-change="handleValidate" style="margin-bottom: 10px" />
    <Alert type="error" show-icon v-if="showError">
      {{ $t('qing-shu-ru-zheng-que-de-dong-zuo-ming-cheng') }}
    </Alert>
    <template #footer>
      <Button v-if="!hideCancelButton" @click="handleCancel">{{ $t('guan-bi') }}</Button>
      <Button
        @click="handleOk"
        :type="confirmButtonType"
        :disabled="disableConfirmUntilMatched && !isInputMatched"
        :class="{ 'second-confirm-danger-button': confirmButtonDanger }"
      >
        {{ confirmButtonText || $t('que-ding') }}
      </Button>
    </template>
  </CCModal>
</template>

<script>
export default {
  name: 'SecondConfirmModal',
  props: {
    visible: Boolean,
    title: String,
    handleConfirm: Function,
    handleClose: Function,
    event: String,
    confirmText: String,
    text: String,
    confirmButtonText: String,
    confirmButtonType: {
      type: String,
      default: 'primary'
    },
    hideCancelButton: Boolean,
    disableConfirmUntilMatched: Boolean,
    confirmButtonDanger: Boolean
  },
  data() {
    return {
      inputEvent: '',
      showError: false
    };
  },
  computed: {
    confirmTarget() {
      return this.confirmText || this.event;
    },
    isInputMatched() {
      return this.inputEvent.trim() === this.confirmTarget;
    }
  },
  methods: {
    handleOk() {
      if (this.isInputMatched) {
        this.showError = false;
        this.inputEvent = '';
        this.handleConfirm();
      } else {
        this.showError = true;
      }
    },
    handleCancel() {
      this.showError = false;
      this.inputEvent = '';
      this.handleClose();
    },
    handleValidate() {
      if (this.inputEvent.trim() !== this.confirmTarget) {
        this.showError = true;
      } else {
        this.showError = false;
      }
    }
  }
};
</script>

<style scoped lang="less">
:deep(.second-confirm-danger-button) {
  min-width: 112px;
  font-weight: 500;

  &:not([disabled]) {
    background: var(--error-color, #ff1815);
    border-color: var(--error-color, #ff1815);
    color: #ffffff;
  }

  &:not([disabled]):hover,
  &:not([disabled]):focus {
    background: #ff4d4f;
    border-color: #ff4d4f;
    color: #ffffff;
  }

  &:not([disabled]):active {
    background: #d9363e;
    border-color: #d9363e;
    color: #ffffff;
  }
}
</style>
