<template>
  <div class="status-icon">
    <div class="circle" :style="`background-color: ${color}`"></div>
    <div class="text" :style="`color: ${color}`">{{ text }}</div>
  </div>
</template>

<script>
export default {
  name: 'StatusIcon',
  props: {
    text: String,
    color: String
  }
};
</script>

<style scoped lang="less">
.status-icon {
  display: flex;
  align-items: center;
  .circle {
    width: 10px;
    height: 10px;
    border-radius: 50%;
  }
  .text {
    margin-left: 8px;
  }
}
</style>
