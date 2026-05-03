<template>
  <div>
    <div class="ticket-status" :style="`color:${TICKET_STATUS_COLOR[process && process.finish ? 'FINISHED' : '']}`">
      <div class="type" :style="styleVar" />
      <div class="content">
        {{ process && process.finished ? $t('yi-wan-cheng') : '' }}
      </div>
    </div>
  </div>
</template>
<script>
import { TICKET_STATUS, TICKET_STATUS_COLOR } from '../../../const/index';

export default {
  props: {
    currentStep: Number,
    ticketDetail: Object,
    process: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      TICKET_STATUS,
      TICKET_STATUS_COLOR
    };
  },
  computed: {
    styleVar() {
      return {
        '--status-color': TICKET_STATUS_COLOR[this.process && this.process.finished ? 'FINISHED' : '']
      };
    }
  }
};
</script>
<style lang="less" scoped>
.ticket-status {
  display: flex;
  align-items: center;

  .type {
    .circle(8px, #52c41a);
    background: var(--status-color);
    margin-right: 6px;
  }
}
</style>
