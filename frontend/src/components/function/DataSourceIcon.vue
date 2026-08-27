<template>
  <div :style="rootStyle">
    <CustomIcon :size="size" v-if="!pngDs.includes(type)" :type="type" :instance-type="instanceType" :leftMargin="leftMargin" />
    <span v-if="pngDs.includes(type)" class="png-ds-icon">
      <img :style="`width: ${size}; height: ${size}; color: currentcolor`" :src="require(`../../assets/datasource/${type}.png`)" :alt="type" />
    </span>
  </div>
</template>
<script>
const goldenDBTypes = ['GoldenDB', 'GoldenDBMySQL', 'GoldenDBOracle'];

export default {
  name: 'DataSourceIcon',
  props: {
    type: String,
    instanceType: String,
    size: {
      type: String,
      default: '16px'
    },
    leftMargin: {
      type: String,
      default: '4px'
    }
  },
  data() {
    return {
      pngDs: ['LocalAI', 'DashScope', 'ZhipuAI']
    };
  },
  computed: {
    rootStyle() {
      const style = { display: 'inline-block' };
      if (goldenDBTypes.includes(this.type)) {
        const width = `calc(${this.size} + ${this.size} + ${this.leftMargin || '0px'})`;
        style.width = width;
        style['min-width'] = width;
        style.flex = `0 0 ${width}`;
      }
      return style;
    }
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
