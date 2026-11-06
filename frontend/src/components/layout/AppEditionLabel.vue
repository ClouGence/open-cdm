<template>
  <button v-if="featureLoadState === 'error'" type="button" class="app-edition-label" @click.stop="$store.dispatch('getUserInfo')">
    {{ $t('workspaceEdition.retry') }}
  </button>
  <span v-else-if="featureLoadState !== 'ready'" class="app-edition-label" role="status">{{ $t('workspaceEdition.loading') }}</span>
  <span v-else class="app-edition-label" data-testid="workspace-edition">{{ editionLabel }}</span>
</template>

<script>
import { mapGetters, mapState } from 'vuex';

export default {
  name: 'AppEditionLabel',
  computed: {
    ...mapGetters(['hasFeature']),
    ...mapState(['featureLoadState']),
    editionLabel() {
      if (this.hasFeature('ENTERPRISE_EDITION')) return this.$t('workspaceEdition.enterprise');
      return this.$t('workspaceEdition.community');
    }
  }
};
</script>

<style lang="less" scoped>
.app-edition-label {
  flex-shrink: 0;
  padding: 0;
  border: 0;
  background: transparent;
  color: inherit;
  font: inherit;
  font-size: 11px;
  white-space: nowrap;
}
</style>
