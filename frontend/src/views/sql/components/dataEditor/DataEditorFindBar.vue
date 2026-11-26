<script>
import { defineComponent, nextTick, ref, watch } from 'vue';
import { UpOutlined, DownOutlined, CloseOutlined, SearchOutlined } from '@ant-design/icons-vue';

const LABELS = {
  CASE: 'Aa',
  REGEX: '.*',
  PLACEHOLDER: 'Find in page...',
  MATCH_CASE: 'Match Case',
  REGEX_TIP: 'Regular Expression',
  PREV_TIP: 'Previous (Shift+Enter)',
  NEXT_TIP: 'Next (Enter)',
  CLOSE_TIP: 'Close (Esc)'
};

export default defineComponent({
  name: 'DataEditorFindBar',
  components: {
    UpOutlined,
    DownOutlined,
    CloseOutlined,
    SearchOutlined
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    query: {
      type: String,
      default: ''
    },
    caseSensitive: {
      type: Boolean,
      default: false
    },
    useRegex: {
      type: Boolean,
      default: false
    },
    matchCount: {
      type: Number,
      default: 0
    },
    activeMatchIndex: {
      type: Number,
      default: -1
    },
    regexError: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:query', 'update:caseSensitive', 'update:useRegex', 'next', 'prev', 'close'],
  setup(props, { emit }) {
    const inputRef = ref(null);

    const focusInput = () => {
      nextTick(() => {
        if (inputRef.value) {
          inputRef.value.focus();
          inputRef.value.select();
        }
      });
    };

    watch(
      () => props.visible,
      (val) => {
        if (val) {
          focusInput();
        }
      }
    );

    const handleKeydown = (e) => {
      if (e.key === 'Escape') {
        e.preventDefault();
        emit('close');
      } else if (e.key === 'Enter') {
        e.preventDefault();
        if (e.shiftKey) {
          emit('prev');
        } else {
          emit('next');
        }
      }
    };

    return {
      inputRef,
      focusInput,
      handleKeydown,
      LABELS
    };
  },
  computed: {
    counterText() {
      if (!this.query) return '';
      if (this.regexError) return 'Invalid';
      if (this.matchCount === 0) return '0 / 0';
      return `${this.activeMatchIndex + 1} / ${this.matchCount}`;
    }
  }
});
</script>

<template>
  <div v-if="visible" class="dg-find-bar" @keydown.stop>
    <SearchOutlined class="dg-find-icon" />
    <input
      ref="inputRef"
      type="text"
      class="dg-find-input"
      :class="{ 'has-error': regexError }"
      :value="query"
      :placeholder="LABELS.PLACEHOLDER"
      @input="$emit('update:query', $event.target.value)"
      @keydown="handleKeydown"
    />
    <span v-if="counterText" class="dg-find-count" :class="{ 'is-zero': matchCount === 0 || regexError }">
      {{ counterText }}
    </span>
    <button
      type="button"
      class="dg-find-toggle"
      :class="{ 'is-active': caseSensitive }"
      :title="LABELS.MATCH_CASE"
      @click="$emit('update:caseSensitive', !caseSensitive)"
    >
      {{ LABELS.CASE }}
    </button>
    <button
      type="button"
      class="dg-find-toggle"
      :class="{ 'is-active': useRegex }"
      :title="LABELS.REGEX_TIP"
      @click="$emit('update:useRegex', !useRegex)"
    >
      {{ LABELS.REGEX }}
    </button>
    <span class="dg-find-sep" />
    <button type="button" class="dg-find-nav" :disabled="matchCount === 0" :title="LABELS.PREV_TIP" @click="$emit('prev')">
      <UpOutlined />
    </button>
    <button type="button" class="dg-find-nav" :disabled="matchCount === 0" :title="LABELS.NEXT_TIP" @click="$emit('next')">
      <DownOutlined />
    </button>
    <button type="button" class="dg-find-nav" :title="LABELS.CLOSE_TIP" @click="$emit('close')">
      <CloseOutlined />
    </button>
  </div>
</template>

<style scoped lang="less">
.dg-find-bar {
  position: absolute;
  top: 8px;
  right: 16px;
  z-index: 20;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid var(--border-light, #d1d5db);
  background: var(--bg-primary, #ffffff);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.14);
  font-size: 12px;
}

.dg-find-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary, #6b7280);
  font-size: 13px;
  line-height: 1;

  :deep(svg) {
    display: block;
  }
}

.dg-find-input {
  width: 180px;
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
  background: transparent !important;
  color: var(--text-primary, #1f2329);
  font-size: 12px;

  &:focus,
  &:focus-visible {
    border: none !important;
    outline: none !important;
    box-shadow: none !important;
  }

  &.has-error {
    color: #ef4444;
  }
}

.dg-find-count {
  min-width: 44px;
  text-align: right;
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  font-size: 11px;
  line-height: 1;
  color: var(--text-secondary, #6b7280);
  font-variant-numeric: tabular-nums;

  &.is-zero {
    color: #ef4444;
  }
}

.dg-find-toggle,
.dg-find-nav {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 22px;
  min-width: 22px;
  padding: 0 4px;
  border: 1px solid transparent;
  border-radius: 4px;
  background: transparent;
  color: var(--text-secondary, #6b7280);
  cursor: pointer;
  font-size: 11px;
  line-height: 1;
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    line-height: 1;
  }

  :deep(svg) {
    display: block;
  }

  &:hover:not(:disabled) {
    background: var(--bg-secondary, #f3f4f6);
    color: var(--text-primary, #1f2329);
  }

  &:disabled {
    opacity: 0.4;
    cursor: not-allowed;
  }

  &.is-active {
    background: rgba(62, 207, 142, 0.14);
    border-color: rgba(62, 207, 142, 0.35);
    color: var(--primary-color, #3ecf8e);
    font-weight: 600;
  }
}

.dg-find-sep {
  width: 1px;
  height: 14px;
  margin: 0 2px;
  background: var(--border-light, #e5e7eb);
}
</style>
