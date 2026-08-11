<template>
  <AppPageTabs :model-value="activeName" :tabs="tabs" @change="handleTabChange" />
</template>

<script>
import AppPageTabs from '@/components/layout/AppPageTabs';

export default {
  name: 'ManagementSectionTabs',
  components: { AppPageTabs },
  props: {
    tabs: {
      type: Array,
      required: true
    }
  },
  computed: {
    activeName() {
      const matched = this.$route.matched.find((record) => record.meta && record.meta.managementTab);
      if (matched) {
        return matched.meta.managementTab;
      }
      return this.tabs[0] ? this.tabs[0].name : '';
    }
  },
  methods: {
    handleTabChange(name) {
      const tab = this.tabs.find((item) => item.name === name);
      if (tab && tab.to !== this.$route.path) {
        this.$router.push(tab.to);
      }
    }
  }
};
</script>
