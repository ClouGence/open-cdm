<template>
  <div class="logo-header" :class="{ 'is-dark': currentTheme === 'dark' }">
    <div class="logo-header-container">
      <div class="left">
        <cc-iconfont name="dm" size="28" :color="`${currentTheme === 'dark' ? '#ffffff' : '#222222'}`" />
        <div class="login-type">
          {{ TYPES[type] }}
        </div>
      </div>
      <div class="right">
        <LangSwitcher class="lang-switcher" />
        <div class="go-login" v-if="type !== 'login'">
          {{ $t('yi-you-zhang-hao-qu') }}
          <a @click="goLogin">{{ $t('deng-lu') }}</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LangSwitcher from '@/components/LangSwitcher';

export default {
  name: 'DmLogoHeader',
  props: {
    type: {
      type: String,
      default: 'login'
    }
  },
  computed: {
    currentTheme() {
      return this.$store.state.theme || 'light';
    }
  },
  data() {
    return {
      currentLang: localStorage.getItem('lang') || this.$i18n.global.locale.value,
      TYPES: {
        register: this.$t('zhu-ce'),
        login: this.$t('deng-lu'),
        reset: this.$t('zhao-hui-mi-ma')
      }
    };
  },
  components: {
    LangSwitcher
  },
  methods: {
    goLogin() {
      this.$router.push({ name: 'Login' });
    }
  }
};
</script>

<style lang="less" scoped>
.logo-header {
  display: flex;
  align-items: center;
  margin: 0 105px;
  height: 80px;
  justify-content: space-between;
  position: relative;

  .logo-header-container {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .login-type {
    color: #555555;
  }

  .left {
    display: flex;
    align-items: center;

    div {
      font-size: 24px;
      font-weight: bold;
      border-left: 1px solid #dadada;
      padding-left: 20px;
      margin-left: 20px;
    }
  }

  .right {
    display: flex;

    .lang-switcher {
      margin-right: 20px;
      display: inline-flex;
      align-items: center;
    }

    .login {
      font-size: 14px;

      a {
        font-size: 14px;
      }

      span {
        color: @primary-color;
      }
    }
  }
}

&.is-dark {
  .logo-header {
    .login-type {
      color: #fff;
    }
  }
}
</style>
