<template>
  <a-input-password :value="pwd" :placeholder="placeholder" :size="antdSize" @update:value="handleInput" @pressEnter="handleEnter" />
</template>

<script>
export default {
  name: 'CCPasswordInput',
  props: {
    value: String,
    placeholder: {
      type: String
    },
    size: {
      type: String,
      default: 'default'
    },
    handleEnter: {
      type: Function,
      default: () => {}
    }
  },
  emits: ['update:value'],
  data() {
    return {
      pwd: this.value
    };
  },
  computed: {
    antdSize() {
      if (this.size === 'small') {
        return 'small';
      }
      if (this.size === 'large') {
        return 'large';
      }
      return 'middle';
    }
  },
  methods: {
    handleInput(value) {
      this.pwd = value;
      this.$emit('update:value', value);
    }
  },
  watch: {
    value(value) {
      if (this.pwd !== value) {
        this.pwd = value;
      }
    }
  }
};
</script>
