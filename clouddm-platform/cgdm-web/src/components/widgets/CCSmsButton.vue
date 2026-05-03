<template>
  <a-button ghost :disabled="counting" :size="size" :type="counting ? 'default' : 'primary'" @click="handleCountdownStart">
    <vue-countdown v-if="counting" :time="60000" @end="handleCountdownEnd" v-slot="{ totalSeconds }">
      {{ $t('propstotalseconds-miao-hou-chang-shi', [totalSeconds || 0]) }}
    </vue-countdown>
    <span v-else>{{ $t('huo-qu-yan-zheng-ma') }}</span>
  </a-button>
</template>

<script>
export default {
  name: 'CCSmsButton',
  props: {
    sub: {
      type: Boolean,
      default: false
    },
    account: String,
    phoneNumber: String,
    verifyCodeType: String,
    verifyType: String,
    login: {
      type: Boolean,
      default: false
    },
    size: {
      type: String,
      default: 'default'
    }
  },
  data() {
    return {
      counting: false
    };
  },
  methods: {
    async handleCountdownStart() {
      const { phoneNumber, verifyCodeType, verifyType, login } = this;

      if (!phoneNumber && !login) {
        this.$Message.error(this.$t('qing-shu-ru-shou-ji-hao'));
        return;
      }
      this.counting = true;
      const data = {
        verifyType,
        verifyCodeType
      };

      if (!login) {
        data.phoneNumber = phoneNumber;
        data.sub = this.sub;
        data.account = this.account;
      }
      const res = this.login ? await this.$services.rdpVerifySendCodeInLoginState({ data }) : await this.$services.rdpVerifySendCode({ data });
      if (res.success) {
        this.$Message.success(this.$t('yan-zheng-ma-fa-song-cheng-gong'));
      } else {
        this.counting = false;
      }
    },
    handleCountdownEnd() {
      this.counting = false;
    }
  }
};
</script>

<style scoped></style>
