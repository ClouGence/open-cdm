<template>
  <div class="system-page">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'System'
};
</script>

<style lang="less" scoped>
.system-page {
  width: 100%;
  height: 100%;
  min-height: 0;
}
</style>
