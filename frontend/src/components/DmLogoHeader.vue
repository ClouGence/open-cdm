<template>
  <div class="logo-header" :class="{ 'is-dark': currentTheme === 'dark' }">
    <div class="logo-header-container">
      <div class="left">
        <span class="product-title-frame">
          <img class="product-title" :src="headerTitleUrl" alt="CloudDM" />
        </span>
        <div class="login-type">
          {{ headerTypeText }}
        </div>
      </div>
      <div class="right">
        <LangSwitcher class="lang-switcher" />
        <div class="go-login" v-if="showGoLogin">
          {{ $t('yi-you-zhang-hao-qu') }}
          <a @click="goLogin">{{ $t('deng-lu') }}</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LangSwitcher from '@/components/LangSwitcher';
import logoClouddm from '@/assets/logo-clouddm.svg';

export default {
  name: 'DmLogoHeader',
  props: {
    type: {
      type: String,
      default: 'login'
    },
    title: {
      type: String,
      default: ''
    }
  },
  computed: {
    currentTheme() {
      return this.$store.state.theme || 'light';
    },
    headerTitleUrl() {
      return logoClouddm;
    },
    headerTypeText() {
      return this.title || this.TYPES[this.type] || '';
    },
    showGoLogin() {
      return this.type !== 'login' && !this.title;
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
  margin: 0;
  height: 72px;
  justify-content: space-between;
  position: relative;

  .logo-header-container {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .login-type {
    color: #707070;
    font-size: 18px;
    font-weight: 500;
  }

  .left {
    display: flex;
    align-items: center;

    .product-title-frame {
      width: 184px;
      height: 32px;
      flex: 0 0 184px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      overflow: hidden;
    }

    .product-title {
      width: 184px;
      height: 32px;
      display: block;
      object-fit: contain;
      object-position: center;
    }

    div {
      font-size: 18px;
      font-weight: 500;
      border-left: 1px solid #dfdfdf;
      padding-left: 16px;
      margin-left: 16px;
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
