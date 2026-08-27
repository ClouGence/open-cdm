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
    <img v-else-if="bundledIcon" :class="`custom-icon-image ${customStyle}`" :src="bundledIcon" alt="" :style="iconStyle" @click="handleClick" />
    <svg v-else :class="`icon-v2 ${customStyle}`" aria-hidden="true" :style="iconStyle" @click="handleClick">
      <use :xlink:href="`#icon-v2-${iconName}`"></use>
    </svg>
  </div>
</template>

<script>
import cloudberryIcon from '@/assets/datasource/cloudberry.svg';
import goldendbIcon from '@/assets/datasource/goldendb.png';
import valkeyIcon from '@/assets/datasource/valkey.svg';
import { getPluginResourceUrl } from '@/utils/pluginResource';

const goldenDBTypes = ['GoldenDB', 'GoldenDBMySQL', 'GoldenDBOracle'];
const bundledIcons = {
  Cloudberry: cloudberryIcon,
  GoldenDB: goldendbIcon,
  GoldenDBMySQL: goldendbIcon,
  GoldenDBOracle: goldendbIcon,
  Valkey: valkeyIcon
};

/**
 * IconFont-v2, Customicon Component
 * https://clougence.yuque.com/ig5uby/pt7mq6/ql8cf0ii9lvkoe6t
 */
export default {
  emits: ['click'],
  props: {
    type: String, // icon unique identifier
    resource: String, // Plugin resource identifier
    alt: {
      type: String,
      default: ''
    },
    instanceType: String, // Type of deployment of data sources
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
      let width = this.size;
      if (goldenDBTypes.includes(this.type)) {
        width = '100%';
      }
      return {
        width,
        height: this.size,
        color: this.disabled ? '#A8A8A8' : this.color,
        filter: this.disabled ? 'grayscale(100%)' : 'none'
      };
    },
    bundledIcon() {
      return bundledIcons[this.type] || '';
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

      if (this.resource || this.bundledIcon) {
        let width = this.size;
        if (goldenDBTypes.includes(this.type)) {
          width = `calc(${this.size} + ${this.size})`;
        }
        style.width = width;
        style.height = this.size;
        style['min-width'] = width;
        style['line-height'] = this.size;
      }
      return style;
    },
    iconName() {
      const noPrefixIcon = this.type?.startsWith('icon-v2-') ? this.type?.slice(8) : this.type;
      const noPrefixDarkIcon = this.darkType?.startsWith('icon-v2-') ? this.darkType?.slice(8) : this.darkType;
      // Special treatment of part of the data source category, icon, for inconsistent performance under different types of deployment
      const icons = {
        MySQL: this.instanceType !== 'ALIBABA_CLOUD_HOSTED' ? 'MySQL' : 'RDSforMySQL',
        PostgreSQL: this.instanceType !== 'ALIBABA_CLOUD_HOSTED' ? 'PostgreSQL' : 'RDSforPostgreSQL',
        Greenplum: this.instanceType !== 'ALIBABA_CLOUD_HOSTED' ? 'Greenplum' : 'ADBforPG',
        SQLServer: this.instanceType === 'ALIBABA_CLOUD_HOSTED' ? 'SQLServerBlue' : 'SQLServer',
        KingbaseESPostgreSQL: 'KingbaseES',
        KingbaseESMySQL: 'KingbaseES',
        KingbaseESOracle: 'KingbaseES',
        KingbaseESSQLServer: 'KingbaseES'
      };

      // Based on current theme
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
  display: inline-block; // To prevent inline-flix below from coming into effect, leading to a disordered layout
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
.data-source-icon .custom-icon-image,
.data-source-icon .custom-icon-resource {
  display: block;
  object-fit: contain;
}
</style>
