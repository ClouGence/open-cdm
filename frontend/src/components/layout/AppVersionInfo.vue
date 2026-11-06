<template>
  <div v-if="displayVersion" class="app-version-info" data-testid="workspace-version-info">
    <span class="app-version-info__version app-sidebar-version-chip" data-testid="workspace-version" :title="displayVersion">
      {{ displayVersion }}
    </span>
    <AppEditionLabel />
  </div>
</template>

<script>
import { mapState } from 'vuex';
import AppEditionLabel from '@/components/layout/AppEditionLabel';
import { resolveDisplayVersion } from '@/utils/version';
import appLogger from '@/utils/logger';

export default {
  name: 'AppVersionInfo',
  components: { AppEditionLabel },
  data() {
    return { fetchedVersion: '' };
  },
  computed: {
    ...mapState(['dmGlobalSetting']),
    displayVersion() {
      return this.fetchedVersion || resolveDisplayVersion(this.dmGlobalSetting);
    }
  },
  async mounted() {
    if (this.displayVersion) return;
    try {
      const res = await this.$services.dmGlobalSettings();
      if (res.success) this.fetchedVersion = resolveDisplayVersion(res.data);
    } catch (error) {
      appLogger.error(error);
    }
  }
};
</script>

<style lang="less" scoped>
.app-version-info {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
  max-width: 100%;
  font-size: 11px;
  line-height: 16px;
  color: var(--text-tertiary);

  &__version {
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-variant-numeric: tabular-nums;
  }
}
</style>
