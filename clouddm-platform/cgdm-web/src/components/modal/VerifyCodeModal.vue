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
      <slot name="content" />
      <Alert type="error" v-if="verifyCodeError" show-icon>{{ verifyCodeError }}</Alert>
      <p :class="{ 'ml-20': withMargin }">
        <Input style="width: 180px" type="text" v-model="verifyCode" :placeholder="$t('dian-ji-huo-qu-shu-ru-yan-zheng-ma')" />
        <Button type="primary" ghost style="margin-left: 10px" :disabled="sendCodeDisabled" @click="handleSendVerifyCode">
          {{
            sendCodeDisabled
              ? $t('zai-ci-huo-qu') + sendCodeAgainTime + 's)'
              : isSmsVerify
                ? $t('huo-qu-duan-xin-yan-zheng-ma')
                : $t('huo-qu-you-xiang-yan-zheng-ma')
          }}
        </Button>
        <Tooltip
          transfer
          :content="
            isSmsVerify
              ? $t('dian-ji-hou-yan-zheng-ma-jiang-fa-song-dao-nin-zhu-ce-de-shou-ji-shang')
              : $t('dian-ji-hou-yan-zheng-ma-jiang-fa-song-dao-nin-zhu-ce-de-you-xiang-shang')
          "
          placement="right"
        >
          <CustomIcon type="icon-v2-HelpCircle" size="16" leftMargin />
        </Tooltip>
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
import store from '@/store';
import { mapGetters } from 'vuex';

export default {
  name: 'VerifyCodeModal',
  props: {
    visible: Boolean,
    title: String,
    loading: Boolean,
    handleCloseModal: Function,
    handleConfirmCallback: Function,
    footerHide: Boolean,
    verifyCodeType: String,
    width: { type: [Number, String], default: 400 },
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
    withMargin: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters(['isProductTrail', 'verifyType']),
    isSmsVerify() {
      return this.verifyType === 'SMS_VERIFY_CODE';
    }
  },
  data() {
    return {
      verifyCodeError: '',
      sendCodeDisabled: false,
      verifyCode: '',
      sendCodeAgainTime: 60,
      store
    };
  },
  methods: {
    handleEmptyData() {
      this.sendCodeDisabled = false;
      this.verifyCode = '';
      this.verifyCodeError = '';
      this.sendCodeAgainTime = 60;
    },
    handleEmptyVerifyCodeModalData() {
      this.handleEmptyData();
      this.handleCloseModal();
    },
    setErrorMsg(msg) {
      this.verifyCodeError = msg;
    },
    handleConfirm() {
      this.handleConfirmCallback(this.verifyCode, true);
      this.handleEmptyData();
    },
    handleSendVerifyCode() {
      this.sendCodeDisabled = true;
      this.sendCodeAgainTime = 60;
      const that = this;

      this.sendCodeAgain = setInterval(() => {
        if (that.sendCodeAgainTime > 0) {
          that.sendCodeAgainTime--;
        } else {
          clearInterval(that.sendCodeAgain);
          that.sendCodeDisabled = false;
        }
      }, 1000);
      let serviceName = '';
      if (this.newPhone || this.newEmail) {
        serviceName = this.$services.rdpVerifySendCodeByAccount;
      } else {
        serviceName = this.$services.rdpVerifySendCodeInLoginState;
      }
      serviceName({
        data: {
          verifyType: this.verifyType,
          verifyCodeType: this.verifyCodeType,
          phoneNumber: this.phoneNumber,
          email: this.email,
          phoneAreaCode: 'CHINA'
        }
      })
        .then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('fa-song-cheng-gong'));
          } else {
            this.sendCodeDisabled = false;
            this.sendCodeAgainTime = 60;
            clearInterval(this.sendCodeAgain);
            try {
              this.verifyCodeError = JSON.parse(res.msg).join(' ');
            } catch (err) {
              this.verifyCodeError = res.msg;
            }
          }
        })
        .catch((res) => {
          this.sendCodeDisabled = false;
          this.sendCodeAgainTime = 60;
          clearInterval(this.sendCodeAgain);
          this.$Modal.error({
            title: 'ERROR',
            content: `${res.msg}`
          });
        });
    }
  },
  unmounted() {
    clearInterval(this.sendCodeAgainTime);
  }
};
</script>

<style scoped></style>
