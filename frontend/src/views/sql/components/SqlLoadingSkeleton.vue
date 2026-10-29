<template>
  <div class="sql-loading-skeleton" :class="`sql-loading-skeleton--${variant}`" role="status" :aria-label="$t('zheng-zai-jia-zai')" aria-busy="true">
    <div class="skeleton-content" aria-hidden="true">
      <template v-if="variant === 'workspace'">
        <div class="skeleton-tabs">
          <span class="skeleton-block skeleton-tab" />
          <span class="skeleton-block skeleton-tab" />
        </div>
        <div class="skeleton-workspace">
          <div class="skeleton-objects">
            <div class="skeleton-toolbar"><span class="skeleton-block skeleton-search" /></div>
            <Skeleton :title="false" :paragraph="treeParagraph" />
            <div class="skeleton-footer"><span class="skeleton-block skeleton-short" /></div>
          </div>
          <div class="skeleton-query">
            <div class="skeleton-toolbar">
              <span v-for="button in 4" :key="button" class="skeleton-block skeleton-button" />
            </div>
            <div class="skeleton-editor"><Skeleton :title="false" :paragraph="codeParagraph" /></div>
            <div class="skeleton-result-tabs"><span class="skeleton-block skeleton-tab" /></div>
          </div>
        </div>
      </template>
      <template v-else-if="variant === 'objects'">
        <div class="skeleton-toolbar"><span class="skeleton-block skeleton-search" /></div>
        <Skeleton :title="false" :paragraph="treeParagraph" />
        <div class="skeleton-footer"><span class="skeleton-block skeleton-short" /></div>
      </template>
      <Skeleton v-else-if="variant === 'editor'" :title="false" :paragraph="codeParagraph" />
      <Skeleton v-else :title="false" :paragraph="treeParagraph" />
    </div>
  </div>
</template>

<script>
import { Skeleton } from 'ant-design-vue';

export default {
  name: 'SqlLoadingSkeleton',
  components: { Skeleton },
  props: {
    variant: { type: String, default: 'tree' }
  },
  data() {
    return {
      treeParagraph: { rows: 8, width: ['68%', '82%', '58%', '76%', '62%', '80%', '56%', '72%'] },
      codeParagraph: { rows: 4, width: ['48%', '64%', '36%', '56%'] }
    };
  }
};
</script>

<style scoped lang="less">
.sql-loading-skeleton {
  flex: 1;
  min-width: 0;
  min-height: 0;
  overflow: hidden;
  pointer-events: none;

  .skeleton-content {
    height: 100%;
    display: flex;
    flex-direction: column;
    animation: sql-skeleton-pulse 1.6s ease-in-out infinite;
  }

  :deep(.ant-skeleton) {
    padding: 12px 16px;
  }

  :deep(.ant-skeleton-paragraph) {
    margin-bottom: 0;
  }

  :deep(.ant-skeleton-paragraph > li),
  .skeleton-block {
    height: 12px;
    border-radius: 4px;
    background: var(--border-primary);
  }

  :deep(.ant-skeleton-paragraph > li + li) {
    margin-top: 18px;
  }

  .skeleton-tabs,
  .skeleton-toolbar,
  .skeleton-result-tabs {
    height: 44px;
    flex: 0 0 44px;
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 8px 12px;
    overflow: hidden;
  }

  .skeleton-tabs,
  .skeleton-objects,
  .skeleton-result-tabs {
    background: var(--bg-secondary);
  }

  .skeleton-tab {
    width: 132px;
    flex-shrink: 0;
  }
  .skeleton-button {
    width: 72px;
    height: 28px;
    flex-shrink: 0;
  }
  .skeleton-search {
    width: 100%;
    height: 28px;
  }
  .skeleton-short {
    width: 88px;
  }

  .skeleton-workspace {
    flex: 1;
    min-height: 0;
    display: flex;
  }

  .skeleton-objects {
    width: 260px;
    min-width: 180px;
    display: flex;
    flex-direction: column;
    flex-shrink: 1;
  }

  .skeleton-query {
    flex: 1;
    min-width: 0;
    background: var(--bg-primary);
  }

  .skeleton-editor {
    height: 250px;
  }
  .skeleton-footer {
    height: 40px;
    flex: 0 0 40px;
    margin-top: auto;
    padding: 12px;
  }

  &--editor {
    background: var(--bg-primary);
    :deep(.ant-skeleton) {
      padding-left: 48px;
    }
    :deep(.ant-skeleton-paragraph > li + li) {
      margin-top: 9px;
    }
  }

  &--objects {
    flex: 0 1 260px;
    background: var(--bg-secondary);
  }
}

@keyframes sql-skeleton-pulse {
  0%,
  100% {
    opacity: 0.55;
  }
  50% {
    opacity: 0.9;
  }
}

@media (prefers-reduced-motion: reduce) {
  .sql-loading-skeleton .skeleton-content {
    animation: none;
  }
}
</style>
