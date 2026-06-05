<template>
  <div style="display: inline-block">
    <CustomIcon :size="size" v-if="!pngDs.includes(type)" :type="type" :instance-type="instanceType" leftMargin="4px" />
    <span v-if="pngDs.includes(type)" class="png-ds-icon">
      <img :style="`width: ${size}; height: ${size}; color: currentcolor`" :src="require(`../../assets/datasource/${type}.png`)" :alt="type" />
    </span>
  </div>
</template>
<script>
export default {
  name: 'DataSourceIcon',
  props: {
    type: String,
    instanceType: String,
    size: {
      type: String,
      default: '16px'
    }
  },
  data() {
    return {
      pngDs: ['LocalAI', 'DashScope', 'ZhipuAI']
    };
  }
};
</script>
<style lang="less" scoped>
.datasource-icon {
  background: #0071af;
  width: 16px;
  height: 16px;
  line-height: 16px;
  margin-right: 4px;
  vertical-align: middle;
}

.png-ds-icon {
  display: inline-block;
  display: inline-flex !important;
  align-items: center;
  vertical-align: middle;
}
</style>
