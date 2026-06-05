<template>
  <div class="footer">
    <span>{{ footerText }}</span>
  </div>
</template>

<script>
export default {
  name: 'DmFooter',
  computed: {
    footerText() {
      return 'Copyright © 2025 ClouGence, Inc.';
    }
  }
};
</script>

<style lang="less" scoped>
.footer {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: var(--text-tertiary);
}
</style>
