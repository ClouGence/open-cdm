<template>
  <a-button
    class="dm-button"
    :type="antdType"
    :ghost="isGhost"
    :danger="isDanger"
    :loading="loading"
    :disabled="disabled"
    :size="antdSize"
    @click="handleClick"
  >
    <CustomIcon v-if="icon && !$slots.default" :type="iconType" size="14px" />
    <slot />
  </a-button>
</template>

<script>
const ICON_MAP = {
  'md-add': 'icon-v2-add',
  'md-refresh': 'icon-v2-Refresh'
};

export default {
  name: 'DmButton',
  props: {
    type: {
      type: String,
      default: 'default'
    },
    ghost: Boolean,
    loading: Boolean,
    disabled: Boolean,
    size: String,
    icon: String
  },
  emits: ['click'],
  computed: {
    isGhost() {
      return this.ghost || this.type === 'ghost' || this.type === 'dashed';
    },
    isDanger() {
      return this.type === 'error';
    },
    antdType() {
      if (this.type === 'primary' || this.type === 'ghost') {
        return 'primary';
      }
      if (this.type === 'text') {
        return 'link';
      }
      if (this.type === 'error') {
        return 'primary';
      }
      return 'default';
    },
    antdSize() {
      if (this.size === 'small') {
        return 'small';
      }
      if (this.size === 'large') {
        return 'large';
      }
      return 'middle';
    },
    iconType() {
      if (!this.icon) {
        return '';
      }
      if (this.icon.startsWith('icon-')) {
        return this.icon;
      }
      return ICON_MAP[this.icon] || 'icon-v2-add';
    }
  },
  methods: {
    handleClick(event) {
      this.$emit('click', event);
    }
  }
};
</script>
