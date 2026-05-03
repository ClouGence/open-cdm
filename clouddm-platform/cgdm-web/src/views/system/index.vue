<template>
  <div class="system">
    <side-nav :menu-items="userInfo.menuItems" />
    <div class="content" :style="`padding: ${noPadding ? '0' : '16px'}`">
      <router-view />
    </div>
  </div>
</template>

<script>
import SideNav from '@/views/system/components/SideNav';
import { mapState } from 'vuex';

export default {
  name: 'System',
  components: { SideNav },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo
    })
  },
  watch: {
    '$route.name': {
      handler(newVal) {
        // 用来辅助layout，全部重构完成后移除
        this.noPadding = [
          'System_Env',
          'rdpOperationLog',
          'System_Role',
          'System_Sub_Account',
          'Permission',
          'System_Sub_Account_AuthDm',
          'Invoice',
          'Payment',
          'Billing',
          'Profile',
          'BillingDetail',
          'Credits',
          'License',
          'Order',
          'Measurement',
          'sqlLog',
          'InvoiceApplyList',
          'InvoiceApply',
          'Prepay',
          'Balance'
        ].includes(newVal);
      },
      deep: true,
      immediate: true
    }
  },
  data() {
    return {
      noPadding: false
    };
  }
};
</script>

<style lang="less" scoped>
.system {
  width: 100%;
  display: flex;
  flex: 1;

  .content {
    flex: 1;
    margin-left: 240px; /* 与目录宽度相同 */
    height: calc(100vh - 48px); /* 视口高度减去菜单栏高度 */
    overflow-y: auto; /* 超出内容滚动 */
  }
}
</style>
