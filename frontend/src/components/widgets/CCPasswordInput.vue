<template>
  <Input v-model="pwd" :placeholder="placeholder" :size="size" :type="eyeVisible ? 'text' : 'password'" @keydown.enter="handleEnter">
    <template #suffix>
      <div style="height: 100%; display: flex">
        <CustomIcon :type="eyeVisible ? 'eye-fill' : 'eyeclose-fill'" size="16px" hoverStyle @click="handleChangeEye" />
      </div>
    </template>
  </Input>
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
      pwd: this.value,
      eyeVisible: false
    };
  },
  methods: {
    handleChangeEye() {
      this.eyeVisible = !this.eyeVisible;
    }
  },
  watch: {
    pwd(value) {
      this.$emit('update:value', value);
    },
    modelValue(value) {
      if (this.pwd !== value) {
        this.pwd = value;
      }
    }
  }
};
</script>

<style scoped>
.h-14 {
  border-radius: 4px;
}
</style>
