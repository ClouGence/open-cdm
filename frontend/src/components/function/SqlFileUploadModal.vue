<template>
  <CCModal v-model="visible" :title="$t('ticket-sql-upload-title')" :width="520">
    <div class="sql-upload-dialog">
      <label class="sql-upload-drop" @dragover.prevent @drop.prevent="handleDrop">
        <input ref="fileInput" type="file" accept=".sql" @change="handleSelected" />
        <Icon type="ios-cloud-upload-outline" size="32" />
        <span>{{ $t('ticket-sql-select-file') }}</span>
        <small>{{ $t('ticket-sql-upload-limit', { size: maxMegaByte }) }}</small>
      </label>
      <div v-if="selectedFiles.length" class="sql-upload-selected-list">
        <div v-for="file in selectedFiles" :key="`${file.name}-${file.size}-${file.lastModified}`" class="sql-upload-selected">
          <span class="sql-upload-file-name">{{ file.name }}</span>
          <span>{{ formatSqlFileSize(file.size) }}</span>
        </div>
      </div>
    </div>
    <template #footer>
      <Button type="primary" :loading="loading" :disabled="!selectedFiles.length" @click="confirmSelection">
        {{ $t('ticket-sql-confirm-upload') }}
      </Button>
    </template>
  </CCModal>
</template>

<script>
import { formatSqlFileSize, validateSqlFiles } from './sqlFileUpload';

export default {
  name: 'SqlFileUploadModal',
  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    },
    maxMegaByte: {
      type: Number,
      default: 20
    }
  },
  emits: ['update:modelValue', 'confirm'],
  data() {
    return {
      selectedFiles: []
    };
  },
  computed: {
    visible: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit('update:modelValue', value);
      }
    }
  },
  watch: {
    modelValue(value) {
      if (value) {
        this.resetSelection();
      }
    }
  },
  methods: {
    formatSqlFileSize,
    handleSelected(event) {
      this.selectFiles(event.target.files);
    },
    handleDrop(event) {
      this.selectFiles(event.dataTransfer.files);
    },
    selectFiles(fileList) {
      const result = validateSqlFiles(fileList, {
        maxMegaByte: this.maxMegaByte
      });
      if (result.errorKey) {
        this.$Message.error(this.$t(result.errorKey, result.errorParams || {}));
        this.resetSelection();
        return;
      }
      this.selectedFiles = result.files;
    },
    confirmSelection() {
      if (!this.selectedFiles.length || this.loading) {
        return;
      }
      this.$emit('confirm', [...this.selectedFiles]);
    },
    resetSelection() {
      this.selectedFiles = [];
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = '';
      }
    }
  }
};
</script>

<style lang="less" scoped>
.sql-upload-dialog {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.sql-upload-drop {
  min-height: 160px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: 1px dashed var(--border-primary, #c7c7c7);
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-primary, #333840);

  input {
    position: absolute;
    width: 1px;
    height: 1px;
    overflow: hidden;
    opacity: 0;
  }

  small {
    color: var(--text-secondary, #707070);
  }
}

.sql-upload-selected-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sql-upload-selected {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  padding: 8px 0;
}

.sql-upload-file-name {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
