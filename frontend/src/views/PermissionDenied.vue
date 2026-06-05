<template>
  <div>
    <div :style="`height:${minHeight}px;text-align: center;line-height: ${minHeight}px;font-size: 24px;color: #808695;`">
      <p>
        <Icon type="md-sad" style="margin-right: 5px; font-size: 28px" />
        {{ $t('bao-qian-nin-mei-you-jin-hang-gai-cao-zuo-de-quan-xian-ru-you-wen-ti-qing-lian-xi-guan-li-yuan') }}
      </p>
    </div>
  </div>
</template>
<script>
export default {
  created() {
    const windowHeight = window.innerHeight;

    this.minHeight = windowHeight - 70;
    if (localStorage.getItem('console_last_url')) {
      const lastUrl = localStorage.getItem('console_last_url');

      this.$services.ccAuthListCatalog().then((res) => {
        if (res.success) {
          if (res.data.indexOf(lastUrl) > -1) {
            this.$router.push({
              path: lastUrl
            });
          }
        }
      });
    }
  },
  data() {
    return {
      minHeight: 0
    };
  }
};
</script>
