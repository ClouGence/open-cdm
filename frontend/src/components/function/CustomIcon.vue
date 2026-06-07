<template>
  <div
    class="data-source-icon"
    :class="{ 'hover-pointer': hoverStyle && !disabled, disabled: disabled, 'resource-icon': resource }"
    :style="wrapperStyle"
  >
    <img
      v-if="resource"
      :class="`custom-icon-resource ${customStyle}`"
      :src="resourceUrl"
      :alt="alt"
      :style="resourceStyle"
      @click="handleClick"
      @error="handleResourceError"
    />
    <svg v-else :class="`icon-v2 ${customStyle}`" aria-hidden="true" :style="iconStyle" @click="handleClick">
      <use :xlink:href="`#icon-v2-${iconName}`"></use>
    </svg>
  </div>
</template>

<script>
import { getPluginResourceUrl } from '@/utils/pluginResource';

/**
 * IconFont-v2, 自定义icon组件
 * https://clougence.yuque.com/ig5uby/pt7mq6/ql8cf0ii9lvkoe6t
 */
export default {
  emits: ['click'],
  props: {
    type: String, // icon唯一标识
    resource: String, // 插件资源标识
    alt: {
      type: String,
      default: ''
    },
    instanceType: String, // 数据源的部署类型
    size: {
      type: String,
      default: '16px'
    },
    color: {
      type: String,
      default: 'currentColor'
    },
    al: {
      type: String,
      default: 'currentColor'
    },
    leftMargin: {
      type: String,
      default: '0'
    },
    rightMargin: {
      type: String,
      default: '0'
    },
    topMargin: {
      type: String,
      default: '0'
    },
    bottomMargin: {
      type: String,
      default: '0'
    },
    hoverStyle: {
      type: Boolean,
      default: false
    },
    customStyle: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    darkType: {
      type: String,
      default: ''
    }
  },
  computed: {
    theme() {
      return this.$store && this.$store.state && this.$store.state.theme;
    },
    iconStyle() {
      return {
        width: this.size,
        height: this.size,
        color: this.disabled ? '#A8A8A8' : this.color,
        filter: this.disabled ? 'grayscale(100%)' : 'none'
      };
    },
    resourceUrl() {
      return this.resource ? getPluginResourceUrl(this.resource) : '';
    },
    resourceStyle() {
      return {
        width: '100%',
        height: '100%',
        filter: this.disabled ? 'grayscale(100%)' : 'none',
        opacity: this.disabled ? 0.45 : 1
      };
    },
    wrapperStyle() {
      const style = {
        'margin-left': this.leftMargin || '5px',
        'margin-right': this.rightMargin || '5px',
        'margin-top': this.topMargin || '0',
        'margin-bottom': this.bottomMargin || '0'
      };

      if (this.resource) {
        style.width = this.size;
        style.height = this.size;
        style['min-width'] = this.size;
        style['line-height'] = this.size;
      }
      return style;
    },
    iconName() {
      const noPrefixIcon = this.type?.startsWith('icon-v2-') ? this.type?.slice(8) : this.type;
      const noPrefixDarkIcon = this.darkType?.startsWith('icon-v2-') ? this.darkType?.slice(8) : this.darkType;
      // 数据源类icon中，对部分icon在部署类型不同下表现不一致做特殊处理
      const icons = {
        MySQL: this.instanceType !== 'ALIBABA_CLOUD_HOSTED' ? 'MySQL' : 'RDSforMySQL',
        PostgreSQL: this.instanceType !== 'ALIBABA_CLOUD_HOSTED' ? 'PostgreSQL' : 'RDSforPostgreSQL',
        Greenplum: this.instanceType !== 'ALIBABA_CLOUD_HOSTED' ? 'Greenplum' : 'ADBforPG',
        SQLServer: this.instanceType === 'ALIBABA_CLOUD_HOSTED' ? 'SQLServerBlue' : 'SQLServer'
      };

      // 根据当前主题
      if (this.theme === 'dark' && this.darkType) {
        return icons[noPrefixDarkIcon] || noPrefixDarkIcon;
      }
      return icons[noPrefixIcon] || noPrefixIcon;
    }
  },
  methods: {
    handleClick(event) {
      if (!this.disabled) {
        this.$emit('click', event);
      }
    },
    handleResourceError(event) {
      event.target.style.display = 'none';
    }
  }
};
</script>

<style lang="less" scoped>
.data-source-icon {
  display: inline-block; // 防止下面的inline-flex不生效导致布局乱掉
  display: inline-flex !important;
  align-items: center;
  vertical-align: middle;
}
.data-source-icon.hover-pointer {
  cursor: pointer;
}
.data-source-icon.disabled {
  cursor: not-allowed;
}
.data-source-icon .icon-v2 {
  vertical-align: middle;
}
.data-source-icon .custom-icon-resource {
  display: block;
  object-fit: contain;
}
</style>
