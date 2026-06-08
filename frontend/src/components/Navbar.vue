<template>
  <a-menu v-model:selectedKeys="currentMenu" mode="horizontal" class="app-shell-menu">
    <!--    <a-menu-item key="ticket" v-if="!isDesktop && includesDM && myCatLog.includes('CAT_RDP_WORKER_ORDER')"><a href="/#/ticket" >{{ $t('gong-dan') }}</a></a-menu-item>-->
    <a-menu-item key="sql" v-if="includesDM && myCatLog.includes('CAT_DM_CONSOLE')">
      <a href="/#/sql">{{ $t('sql-cha-xun') }}</a>
    </a-menu-item>
    <a-menu-item key="project" v-if="includesDM && myCatLog.includes('CAT_DM_PROJECT') && !isDesktop">
      <a href="/#/project">{{ $t('xiang-mu') }}</a>
    </a-menu-item>
    <a-menu-item key="ticket" v-if="includesDM && myCatLog.includes('CAT_RDP_WORKER_ORDER') && !isDesktop">
      <a href="/#/ticket">{{ $t('gong-dan') }}</a>
    </a-menu-item>
  </a-menu>
</template>

<script>
import { mapGetters, mapState } from 'vuex';

export default {
  name: 'Navbar',
  data() {
    return {
      currentMenu: ['sql']
    };
  },
  created() {
    this.handlePath();
  },
  computed: {
    ...mapGetters(['includesCC', 'includesDM']),
    ...mapState(['myCatLog', 'userInfo', 'globalSetting']),
    ...mapGetters(['isDesktop'])
  },
  methods: {
    handlePath() {
      const path = this.$route.path;
      if (path.indexOf('/system/sql_log') > -1) {
        this.currentMenu = [];
      } else if (path.indexOf('/sql') > -1) {
        this.currentMenu = ['sql'];
      } else if (path.indexOf('/system') > -1) {
        this.currentMenu = [];
      } else if (path.indexOf('/project') > -1) {
        this.currentMenu = ['project'];
      } else if (path.indexOf('/ticket') > -1) {
        this.currentMenu = ['ticket'];
      } else {
        this.currentMenu = [];
      }
    }
  },
  watch: {
    isDesktop(val) {
      if (val) {
        this.handlePath();
      }
    },
    $route() {
      this.handlePath();
    }
  }
};
</script>
