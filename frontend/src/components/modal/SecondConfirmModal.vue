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
      <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
      <Button @click="handleOk" type="primary">{{ $t('que-ding') }}</Button>
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
    text: String
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
    }
  },
  methods: {
    handleOk() {
      if (this.inputEvent.trim() === this.confirmTarget) {
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

<style scoped lang="less"></style>
