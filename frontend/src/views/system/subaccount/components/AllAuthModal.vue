<template>
  <a-modal :visible="visible" :title="$t('yi-shou-quan-quan-xian')" @cancel="handleCloseModal" :mask-closable="false" :width="1000">
    <all-auth :sub-account="subAccount" />
    <div class="footer">
      <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
import AllAuth from '@/views/system/subaccount/components/AllAuth';

export default {
  name: 'AllAuthModal',
  components: { AllAuth },
  props: {
    visible: Boolean,
    handleCloseModal: Function,
    subAccount: Object
  }
};
</script>
