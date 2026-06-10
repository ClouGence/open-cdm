<template>
  <div class="sso-page">
    <div class="sso-page__placeholder">{{ $t('nav-sso-placeholder') }}</div>
  </div>
</template>

<script>
export default {
  name: 'SsoPage'
};
</script>

<style lang="less" scoped>
.sso-page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  min-height: 240px;
  padding: 24px;

  &__placeholder {
    color: var(--text-secondary);
    font-size: 14px;
  }
}
</style>
