<template>
  <div class="cc-status">
    <div :style="styleVar" class="type" />
    <div class="content" v-if="content !== ''">{{ content }}</div>
  </div>
</template>

<script>
import { STATUS_COLOR } from '@/const';

export default {
  name: 'CCStatus',
  props: {
    type: {
      type: String,
      default: 'success'
    },
    content: {
      type: String,
      default: ''
    },
    color: String
  },
  computed: {
    styleVar() {
      return {
        '--status-color': this.color || STATUS_COLOR[this.type],
        '--status-mr': this.content !== '' ? '6px' : '0px'
      };
    }
  }
};
</script>

<style lang="less" scoped>
.cc-status {
  display: flex;
  align-items: center;
  .type {
    .circle(8px, #52c41a);
    background: var(--status-color);
    margin-right: var(--status-mr);
  }
}
</style>
