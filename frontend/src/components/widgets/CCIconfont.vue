<template>
  <i :class="`cc-iconfont i-clouddm icon-${name}`" :style="styleVar" />
</template>

<script>
export default {
  name: 'CCIconfont',
  props: {
    name: String,
    color: {
      type: String,
      default: ''
    },
    size: {
      type: [Number, String],
      default: 16
    }
  },
  computed: {
    styleVar() {
      return {
        '--icon-size': `${this.size}px`,
        '--icon-color': this.color
      };
    }
  }
};
</script>

<style lang="less" scoped>
.cc-iconfont {
  font-size: var(--icon-size);
  color: var(--icon-color);
  cursor: pointer;
}
</style>
