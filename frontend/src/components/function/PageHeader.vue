<template>
  <div class="page-header">
    <div class="page-header-container border-radius-card">
      <div class="page-header-detail">
        <div class="page-header-main">
          <div class="ivu-page-header-row">
            <div class="ivu-page-header-title">
              {{ title }}
              <slot />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    title: String
  }
};
</script>

<style lang="less">
.page-header {
  margin-top: 24px;
  margin-left: -16px;
  margin-right: -16px;

  .page-header-container {
    background: #fff;
    border-bottom: 1px solid #e8eaec;
  }

  .page-header-detail {
    display: flex;
  }

  .page-header-main {
    width: 100%;
    flex: 0 1 auto;

    .ivu-page-header-row {
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
    }
  }

  .ivu-page-header-row {
    width: 100%;
  }

  .ivu-page-header-title {
    margin-bottom: 16px;
    -webkit-box-flex: 1;
    -ms-flex: auto;
    flex: auto;
    display: inline-block;
    color: #17233d;
    font-weight: 500;
    font-size: 20px;
  }

  .ivu-page-header-content {
    margin-bottom: 16px;
    -webkit-box-flex: 1;
    -ms-flex: auto;
    flex: auto;
    font-size: 14px;
  }
}
</style>
