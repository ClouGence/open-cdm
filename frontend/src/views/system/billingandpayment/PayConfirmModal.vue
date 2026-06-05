<template>
  <CCModal :title="$t('zhi-fu-que-ren')" v-model="localShow" footer-hide :closable="false" :mask-closable="false">
    <div class="p-2">
      <p class="mb-5">
        {{ $t('qing-zai-xin-da-kai-de-ye-mian-wan-cheng-zhi-fu') }}
        <span class="font-bold text-orange-500">
          {{ $t('zhi-fu-wan-cheng-qian-qing-bu-yao-guan-bi-ci-chuang-kou') }}
        </span>
        {{ $t('wan-cheng-zhi-fu-hou-qing-gen-ju-nin-de-qing-kuang-dian-ji-yi-xia-an-niu') }}
      </p>
      <div class="text-center my-4">
        <Button class="w-48 mr-2" @click="handleConfirmFinish" type="primary">
          {{ $t('yi-wan-cheng-zhi-fu') }}
        </Button>
        <Button class="w-48" @click="handleConfirmFinish">
          {{ $t('yu-dao-wen-ti-shao-hou-zai-shi') }}
        </Button>
      </div>
      <p class="opacity-80">
        {{ $t('ru-you-yi-wen-xu-yao-bang-zhu-qing-lian-xi') }}
        <a style="color: #0bb8f8" :href="getContactUrl" target="_blank">
          {{ $t('lian-xi-wo-men') }}
        </a>
      </p>
    </div>
  </CCModal>
</template>
<script>
export default {
  name: 'PayConfirm',
  props: {
    showPayConfirm: Boolean,
    handleClickPay: Function,
    handleConfirmFinish: Function
  },
  data() {
    return {
      localShow: this.showPayConfirm
    };
  },
  watch: {
    showPayConfirm(newVal) {
      this.localShow = newVal;
    },
    localShow(newVal) {
      this.$emit('update:showPayConfirm', newVal);
    }
  },
  computed: {
    getContactUrl() {
      return 'https://www.clougence.com/about';
    }
  }
};
</script>
