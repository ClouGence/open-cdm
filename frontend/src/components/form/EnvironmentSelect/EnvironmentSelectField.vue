<template>
  <Select :model-value="modelValue" transfer style="width: 280px" @on-change="$emit('change', $event)">
    <Option v-for="env in envList" :key="env.id" :value="env.id">
      {{ env.envName }}
    </Option>
  </Select>
</template>

<script>
export default {
  name: 'EnvironmentSelectField',
  props: {
    modelValue: {
      type: [Number, String],
      default: ''
    },
    envList: {
      type: Array,
      default: () => []
    }
  },
  emits: ['change']
};
</script>
