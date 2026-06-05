<template>
  <div>
    <div class="success-add-datasource">
      <i class="iconfont iconwancheng"></i>
      <p class="success-add-title">{{ $t('xin-zeng-shu-ju-yuan-cheng-gong') }}</p>
      <p class="success-add-guide">
        {{ $t('nin-ke-yi-zai-shu-ju-yuan-guan-li-jie-mian') }}
        <Button type="primary" ghost @click="handleGoDatasource">
          {{ $t('cha-kan-shu-ju-yuan') }}
        </Button>
        {{ $t('huo-ji-xu') }}
        <Button type="primary" ghost @click="handleGooAddDatasource">
          {{ $t('xin-zeng-shu-ju-yuan') }}
        </Button>
      </p>
    </div>
  </div>
</template>
<script>
export default {
  methods: {
    handleGoDatasource() {
      this.$router.push({ path: '/system/ccdatasource' });
    },
    handleGooAddDatasource() {
      window.location.reload();
    }
  }
};
</script>
<style lang="less" scoped>
.success-add-datasource {
  padding: 80px 0;
  text-align: center;

  .iconfont {
    font-size: 80px;
  }

  .iconwancheng {
    color: #52c41a;
  }

  .success-add-title {
    font-size: 24px;
  }

  .success-add-guide {
    font-size: 16px;
    margin-top: 20px;

    button {
      margin: 0 10px;
    }
  }
}
</style>
