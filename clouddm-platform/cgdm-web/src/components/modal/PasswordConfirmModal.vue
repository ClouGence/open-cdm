<template>
  <CCModal
    :modelValue="visible"
    @update:modelValue="(val) => $emit('update:visible', val)"
    :title="title"
    :width="450"
    :mask-closable="false"
    @on-cancel="handleEmptyVerifyCodeModalData"
  >
    <div>
      <div class="msgContent" v-if="errMsg">
        <a-alert banner type="error">
          <template #message>
            <span v-html="errMsg"></span>
          </template>
        </a-alert>
      </div>
      <Form :label-width="90" ref="passwordConfirmForm">
        <FormItem :label="label ? label : $t('lao-mi-ma')">
          <Input type="password" password v-model="originPassword" :placeholder="placeholder ? placeholder : $t('qing-shu-ru-lao-mi-ma')"></Input>
        </FormItem>
        <slot name="content" />
      </Form>
    </div>
    <template #footer>
      <Button @click="handleEmptyVerifyCodeModalData">{{ $t('guan-bi') }}</Button>
      <Button v-if="hasNextStep" type="primary" @click="handleConfirm">
        {{ $t('xia-yi-bu') }}
      </Button>
      <Button v-if="!hasNextStep" type="primary" @click="handleConfirm">
        {{ $t('que-ding') }}
      </Button>
    </template>
  </CCModal>
</template>

<script>
import store from '@/store';
import { mapGetters } from 'vuex';

export default {
  name: 'PasswordConfirmModal',
  props: {
    visible: Boolean,
    title: String,
    label: String,
    placeholder: String,
    handleCloseModal: Function,
    handleConfirmCallback: Function,
    footerHide: Boolean,
    verifyCodeType: String,
    width: { type: [Number, String], default: 700 },
    hasNextStep: {
      type: Boolean,
      default: false
    },
    newPhone: {
      type: Boolean,
      default: false
    },
    newEmail: {
      type: Boolean,
      default: false
    },
    phoneNumber: String,
    email: String,
    errMsg: String
  },
  computed: {
    ...mapGetters(['isProductTrail', 'verifyType'])
  },
  data() {
    return {
      originPassword: '',
      store
    };
  },
  methods: {
    handleEmptyData() {
      this.originPassword = '';
    },
    handleEmptyVerifyCodeModalData() {
      this.handleEmptyData();
      this.handleCloseModal();
    },
    setErrorMsg(msgContent) {
      this.verifyCodeError = msgContent;
    },
    handleConfirm() {
      this.handleConfirmCallback(this.originPassword, true);
      this.handleEmptyData();
    }
  },
  unmounted() {
    clearInterval(this.sendCodeAgainTime);
  }
};
</script>

<style scoped>
.msgContent {
  margin-bottom: 10px;
}
</style>
