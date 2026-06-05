<template>
  <Button
    @click="handleClick"
    :size="size"
    :shape="shape"
    :type="type"
    :loading="loading"
    :disabled="disabled"
    :htmlType="htmlType"
    :icon="icon"
    :customIcon="customIcon"
    :ghost="ghost"
    :long="long"
  >
    <slot></slot>
  </Button>
</template>

<script>
export default {
  props: {
    type: {
      default: 'default'
    },
    shape: {},
    size: {
      default() {
        return !this?.$IVIEW || this?.$IVIEW?.size === '' ? 'default' : this?.$IVIEW?.size;
      }
    },
    loading: Boolean,
    disabled: Boolean,
    htmlType: {
      default: 'button'
    },
    icon: {
      type: String,
      default: ''
    },
    customIcon: {
      type: String,
      default: ''
    },
    long: {
      type: Boolean,
      default: false
    },
    ghost: {
      type: Boolean,
      default: false
    },
    click: {
      type: Function
    }
  },
  data() {
    return {
      isClicked: true
    };
  },
  methods: {
    handleClick(event) {
      if (this.isClicked) {
        this.isClicked = false;
        const that = this;

        setTimeout(() => {
          that.isClicked = true;
        }, 500);
        this.$emit('click', event);
      }
    }
  }
};
</script>
