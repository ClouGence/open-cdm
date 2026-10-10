<template>
  <div class="command-examples-control">
    <Button size="small" :title="$t('query-examples-title')" :aria-label="$t('query-examples-title')" @click.stop="visible = true">
      {{ $t('query-examples-icon') }}
    </Button>
    <CCModal v-model="visible" :title="$t('query-examples-title')" :width="800">
      <div class="command-examples-list">
        <section v-for="example in examples" :key="example.statement" class="command-example">
          <h3>{{ example.titleI18N }}</h3>
          <pre><code>{{ example.statement }}</code></pre>
        </section>
      </div>
      <template #footer>
        <a-button @click="visible = false">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import CCModal from '@/components/ui/CCModal.vue';

export default {
  name: 'CommandExamples',
  components: { CCModal },
  props: {
    examples: { type: Array, required: true }
  },
  data() {
    return { visible: false };
  }
};
</script>

<style scoped lang="less">
.command-examples-control {
  display: inline-flex;
}

.command-examples-list {
  max-height: 60vh;
  overflow: auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.command-example h3 {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
}

.command-example pre {
  margin: 8px 0 0;
  padding: 12px;
  background: var(--bg-secondary);
  color: var(--text-primary);
  white-space: pre-wrap;
  overflow-wrap: anywhere;
  font-size: 13px;
  line-height: 1.6;
}
</style>
