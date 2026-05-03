<template>
  <div>
    <div :style="`height:${minHeight}px;text-align: center;line-height: ${minHeight}px;font-size: 24px;color: #808695;`">
      <p>
        <Icon type="ios-alert-outline" style="margin-right: 5px; font-size: 28px" />
        {{ $t('bao-qian-nin-de-zhang-hao-cun-zai-yi-chang-qing-dian-ji-tui-chu-deng-lu-zhong-xin-deng-lu-ru-you-wen-ti-qing-lian-xi-guan-li-yuan') }}
      </p>
    </div>
  </div>
</template>
<script>
export default {
  created() {
    const windowHeight = window.innerHeight;

    this.minHeight = windowHeight - 70;

    this.$services.ccUserQueryLoginUser().then((res) => {
      if (res.success) {
        if (localStorage.getItem('console_last_url')) {
          const lasturl = localStorage.getItem('console_last_url');

          this.$router.push({
            path: lasturl
          });
        } else {
          this.$router.push({
            path: this.$store.state.defaultRedirectUrl || '/sql'
          });
        }
      }
    });
  },
  data() {
    return {
      minHeight: 0
    };
  }
};
</script>
