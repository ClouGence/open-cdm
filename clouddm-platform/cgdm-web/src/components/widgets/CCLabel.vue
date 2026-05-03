<template>
  <div class="cc-label">
    <div v-if="required">
      <cc-iconfont color="red" name="required" />
    </div>
    <slot />
  </div>
</template>

<script>
export default {
  name: 'CCLabel',
  props: {
    size: {
      type: Number,
      default: 12
    },
    required: {
      type: Boolean,
      default: false
    }
  }
};
</script>

<style lang="less" scoped>
.cc-label {
  .flex-center();
}
</style>
