<script>
export default {
  name: 'CCSvgIcon',
  computed: {
    iconName() {
      const aliases = { TOPIC: 'SEQUENCE', MQ_ATTRIBUTE: 'COLUMN-DEFAULT' };
      return `#icon-svg-${aliases[this.name] || this.name}`;
    }
  },
  props: {
    cursor: {
      type: Boolean,
      default: true
    },
    name: String,
    size: {
      type: Number,
      default: 16
    },
    color: {
      type: String,
      default: ''
    }
  }
};
</script>

<template>
  <svg class="svg-icon" aria-hidden="true" :style="`width: ${size}px;height: ${size}px;cursor: ${cursor ? 'pointer' : 'default'};`">
    <use :xlink:href="iconName" />
  </svg>
</template>

<style scoped lang="less"></style>
