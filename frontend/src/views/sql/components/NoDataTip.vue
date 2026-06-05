<template>
  <div class="no-data-tip">
    <div class="tip" @click="handleJump">
      <a-icon type="plus-square" style="font-size: 40px; font-weight: 200" />
      <div style="margin-top: 10px; font-size: 16px; font-weight: bold">
        {{ $t('tian-jia-shu-ju-yuan') }}
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'NoDataTip',
  methods: {
    handleJump() {
      this.$router.push({ name: 'System_DataSource_Add' });
    }
  }
};
</script>
<style lang="less" scoped>
.no-data-tip {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.title {
  font-size: 20px;
  color: #999;
  margin-bottom: 4px;
  text-align: center;
}

.tip {
  cursor: pointer;
  border: 4px dotted #ccc;
  border-radius: 10px;
  padding: 20px;
  color: #999999;
  text-align: center;
}
</style>
