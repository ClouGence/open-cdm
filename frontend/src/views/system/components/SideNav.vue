<template>
  <div class="system-side-nav">
    <div class="side-nav-user-info">
      <img class="mx-auto" :alt="$t('tou-xiang')" src="../../../assets/head2.png" />
      <p class="domain">{{ userInfo.username }}</p>
    </div>
    <Menu class="side-nav-menu" :active-name="currentKey" style="width: 100%">
      <MenuGroup :title="$t('xi-tong-she-zhi')" v-if="mySystemMenuItems?.length">
        <MenuItem v-for="item in mySystemMenuItems" :key="item.key" :name="item.key">
          <a :href="item.href" class="flex items-center">
            <CustomIcon :type="item.iconName" rightMargin />
            {{ item.label }}
          </a>
        </MenuItem>
      </MenuGroup>
      <MenuGroup :title="$t('zhang-hao')">
        <MenuItem name="/settings/profile" key="/settings/profile">
          <a href="/#/settings/profile">
            <CustomIcon type="profile" rightMargin />
            {{ $t('ge-ren-zi-liao') }}
          </a>
        </MenuItem>
        <MenuItem name="permission" key="/system/permission" v-if="this.userInfo.accountType !== 'PRIMARY_ACCOUNT' && this.includesDM">
          <a href="/#/system/permission">
            <CustomIcon type="icon-v2-MyAuth" rightMargin />
            {{ $t('my-permissions') }}
          </a>
        </MenuItem>
      </MenuGroup>
    </Menu>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';

export default {
  name: 'SideNav',
  data() {
    return {
      menuItemKeys: [],
      currentKey: ''
    };
  },
  created() {
    this.$bus.on('changeSidebar', (name) => {
      this.currentKey = name;
    });
  },
  watch: {
    '$route.path': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          const pathArr = newVal.split('/');
          const length = pathArr.length;
          if (length === 2) {
            this.currentKey = `/${pathArr[1]}`;
          } else if (length >= 3) {
            this.currentKey = `/${pathArr[1]}/${pathArr[2]}`;
          }
        }
      },
      deep: true,
      immediate: true
    }
  },
  unmounted() {
    this.$bus.off('changeSidebar');
  },
  computed: {
    ...mapGetters(['includesDM']),
    ...mapState(['myCatLog', 'userInfo', 'globalSetting', 'mySystemMenuItems', 'theme'])
  }
};
</script>

<style lang="less" scoped>
.side-nav-menu {
  margin-top: 0;
}
</style>
