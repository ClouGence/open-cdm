<template>
  <div class="system-side-nav overflow-y-auto">
    <div class="side-nav-user-info pt-12 pb-10" :style="{ backgroundImage: `url(${bgImageUrl})` }">
      <img class="mx-auto" :alt="$t('tou-xiang')" src="../../../assets/head2.png" />
      <p class="domain">{{ userInfo.username }}</p>
    </div>
    <Menu class="mt-3" :active-name="currentKey" style="width: 100%">
      <MenuGroup :title="$t('xi-tong-she-zhi')" v-if="mySystemMenuItems?.length">
        <MenuItem v-for="item in mySystemMenuItems" :key="item.key" :name="item.key">
          <a :href="item.href" class="flex items-center">
            <CustomIcon :type="item.iconName" rightMargin />
            {{ item.label }}
          </a>
        </MenuItem>
      </MenuGroup>
      <MenuGroup :title="$t('zhang-hao')">
        <MenuItem name="/system/profile" key="/system/profile">
          <a href="/#/system/profile">
            <CustomIcon type="profile" rightMargin />
            {{ $t('ge-ren-zi-liao') }}
          </a>
        </MenuItem>
        <!-- 仅子账号下的dm，开放我的权限 -->
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
      console.log('changeSidebar');
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
        console.log('this.currentKey', newVal, oldVal, this.currentKey, this.mySystemMenuItems);
      },
      deep: true,
      immediate: true
    }
  },
  // mounted() {
  //   const pathArr = this.$route.path.split('/');
  //   const length = pathArr.length;
  //   if (length === 2) {
  //     this.currentKey = [`/${pathArr[1]}`];
  //   } else if (length >= 3) {
  //     this.currentKey = [`/${pathArr[1]}/${pathArr[2]}`];
  //   }
  // },
  unmounted() {
    this.$bus.off('changeSidebar');
  },
  computed: {
    ...mapGetters(['includesDM']),
    ...mapState(['myCatLog', 'userInfo', 'globalSetting', 'mySystemMenuItems', 'theme']),
    bgImageUrl() {
      return this.theme === 'dark' ? require('../../../assets/bg-img-dark.png') : require('../../../assets/bg-img.png');
    }
  }
};
</script>

<style lang="less" scoped>
.system-side-nav {
  background: #f4f4f4;
  //min-width: 160px;
  //max-width: 160px;
  position: fixed;
  top: 48px; /* 菜单栏下方 */
  width: 240px;
  height: calc(100vh - 48px);
  transition: background-color 0.2s ease;

  .side-nav-user-info {
    text-align: center;

    img {
      width: 60px;
      height: 60px;
    }

    .domain {
      margin-top: 15px;
      font-size: 14px;
      color: rgba(0, 0, 0, 0.88);
      transition: color 0.2s ease;
    }
  }

  .ant-menu {
    background: #f4f4f4;
    transition: background-color 0.2s ease;

    :deep(.ant-menu-item),
    :deep(.ant-menu-item-only-child) {
      min-height: 28px;
      height: 28px;
      line-height: 28px;
      margin-top: 0;
      margin-bottom: 0;
      padding-top: 0;
      padding-bottom: 0;
    }

    :deep(.ant-menu-item-group-title) {
      padding-top: 8px;
      padding-bottom: 4px;
    }

    .ant-menu-item-selected {
      background: #dddddd;
      font-weight: 500;
      font-family: PingFangSC-Semibold;
      position: relative;
      transition: background-color 0.2s ease;

      &::before {
        content: '';
        height: 100%;
        width: 3px;
        background: rgba(0, 0, 0, 0.88);
        position: absolute;
        left: 0;
        top: 0;
        transition: background-color 0.2s ease;
      }

      a {
        color: rgba(0, 0, 0, 0.88);
        transition: color 0.2s ease;
      }
    }

    .ant-menu-item > a {
      color: rgba(0, 0, 0, 0.88);
      transition: color 0.2s ease;
    }
  }

  :deep(.ivu-menu-item) {
    min-height: 28px;
    height: 28px;
    line-height: 28px;
    margin-top: 0;
    margin-bottom: 0;
    padding-top: 0;
    padding-bottom: 0;

    a {
      display: flex;
      align-items: center;
      min-height: 28px;
      line-height: 28px;
    }
  }

  :deep(.ivu-menu-item-group-title) {
    padding-top: 8px;
    padding-bottom: 4px;
  }
}
</style>

<style lang="less">
// 暗色模式适配 - 全局样式
[data-theme='dark'] .system-side-nav {
  background: var(--bg-secondary, #1f1f1f);

  .side-nav-user-info {
    .domain {
      color: var(--text-primary, #ffffff);
    }
  }

  // iview Menu 组件适配
  .ivu-menu {
    background: var(--bg-secondary, #1f1f1f);

    // MenuGroup 标题颜色
    .ivu-menu-group-title {
      color: var(--text-secondary, rgba(255, 255, 255, 0.65)) !important;
    }

    // MenuItem 基础样式
    .ivu-menu-item {
      color: var(--text-primary, #ffffff) !important;
      transition: all 0.2s ease;

      // hover 状态
      &:hover {
        color: var(--primary-color) !important;

        a {
          color: var(--primary-color) !important;
        }
      }

      // 选中状态
      &.ivu-menu-item-active,
      &.ivu-menu-item-selected {
        background-color: var(--bg-select, #2d2d2d) !important;
        color: var(--text-primary, #ffffff) !important;
        font-weight: 500;
        font-family: PingFangSC-Semibold;
        position: relative;

        &::before {
          content: '';
          height: 100%;
          width: 3px;
          background: var(--primary-color, #1890ff) !important;
          position: absolute;
          left: 0;
          top: 0;
        }

        a {
          color: var(--text-primary, #ffffff) !important;
        }
      }

      // 链接文字颜色
      a {
        color: var(--text-primary, #ffffff) !important;
        transition: color 0.2s ease;
      }
    }
  }

  // Ant Design Menu 组件适配
  .ant-menu {
    background: var(--bg-secondary, #1f1f1f);

    // MenuGroup 标题颜色
    .ant-menu-item-group-title {
      color: var(--text-secondary, rgba(255, 255, 255, 0.65)) !important;
    }

    // MenuItem 基础样式
    .ant-menu-item {
      color: var(--text-primary, #ffffff) !important;
      transition: all 0.2s ease;

      // hover 状态
      &:hover {
        background-color: var(--bg-hover, #333333) !important;
        color: var(--text-primary, #ffffff) !important;

        a {
          color: var(--text-primary, #ffffff) !important;
        }
      }

      // 选中状态
      &.ant-menu-item-selected {
        background: var(--bg-select, #2d2d2d) !important;

        &::before {
          background: var(--primary-color, #1890ff) !important;
        }

        a {
          color: var(--text-primary, #ffffff) !important;
        }
      }

      // 链接文字颜色
      > a {
        color: var(--text-primary, #ffffff) !important;
        transition: color 0.2s ease;
      }
    }
  }
}
</style>
