<template>
  <CCModal
    class="dm-modal-footer-center"
    :model-value="true"
    :title="$t('ai.shareConversation')"
    :aria-label="$t('ai.shareConversation')"
    :width="900"
    :draggable="false"
    :closable="!exporting"
    :keyboard="!exporting"
    :mask-closable="!exporting"
    @update:model-value="close"
    @on-cancel="close"
  >
    <div ref="previewScroll" class="ai-share-preview">
      <div ref="content" class="ai-share-content" :style="sqlTypography">
        <div class="ai-share-brand"><AppBrandLogo /></div>
        <div
          v-for="row in rows"
          :key="row.id"
          class="ai-share-row"
          :data-share-row="row.id"
          role="checkbox"
          tabindex="0"
          :aria-checked="selectedIds.includes(row.id)"
          :aria-label="$t(row.role === 'user' ? 'ai.shareUserMessage' : 'ai.shareAssistantMessage')"
          :aria-disabled="exporting"
          @click.prevent="toggle(row.id)"
          @keydown.space.prevent="toggle(row.id)"
          @keydown.enter.prevent="toggle(row.id)"
        >
          <span data-share-control class="ai-share-check" aria-hidden="true">
            <CheckCircleFilled v-if="selectedIds.includes(row.id)" />
            <span v-else class="ai-share-unchecked" />
          </span>
          <div data-share-content :class="{ 'ai-share-unselected': !selectedIds.includes(row.id), 'ai-share-user': row.role === 'user' }">
            <template v-if="row.role === 'user'">
              <div v-if="row.selection" class="ai-share-reference">
                <span>{{ row.selection.title }} {{ $t('ai.selectionLines', { start: row.selection.startLine, end: row.selection.endLine }) }}</span>
                <pre>{{ row.selection.sql }}</pre>
              </div>
              <p class="ai-share-prompt">{{ row.text }}</p>
            </template>
            <template v-else>
              <div class="ai-share-markdown" v-html="renderMessage(row.text)"></div>
              <AiUserInputCard
                v-if="row.request"
                :request="row.request"
                :state="row.inputState === 'answered' ? 'answered' : 'expired'"
                :answers="row.answers"
                disabled
              />
              <div v-if="row.sql" class="ai-share-sql">
                <div class="ai-share-sql-label">{{ $t('ai.sqlCode') }}</div>
                <pre>{{ row.sql }}</pre>
              </div>
            </template>
          </div>
        </div>
        <div class="ai-share-links">
          <div v-for="link in shareLinks" :key="link.url" class="ai-share-link" role="img" :aria-label="$t(link.label)">
            <div class="ai-share-qrcode">
              <!-- Overlay the rounded icon without clearing a larger square of QR modules. -->
              <QRCodeSVG
                :value="link.url"
                :size="110"
                level="H"
                fg-color="#000000"
                bg-color="#ffffff"
                :image-settings="{ src: link.icon, width: 18, height: 18, excavate: false }"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <p v-if="error" class="ai-share-error" role="alert">{{ error }}</p>
    <template #footer>
      <div class="ai-share-footer">
        <a-checkbox :checked="allSelected" :indeterminate="selectedIds.length > 0 && !allSelected" :disabled="exporting" @change="toggleAll">
          {{ $t('ai.shareSelectAll') }}
        </a-checkbox>
        <div class="ai-share-export-actions">
          <Button :disabled="!selectedIds.length || exporting" @click="copyImage">
            <LoadingOutlined v-if="copying" spin />
            <CheckOutlined v-else-if="copied" />
            <CopyOutlined v-else />
            {{ $t(copied ? 'ai.copied' : 'ai.shareCopyImage') }}
          </Button>
          <Button type="primary" :loading="downloading" :disabled="!selectedIds.length || exporting" @click="downloadImage">
            <DownloadOutlined />
            {{ $t('ai.shareDownloadImage') }}
          </Button>
        </div>
      </div>
    </template>
  </CCModal>
</template>

<script>
import CCModal from '@/components/ui/CCModal.vue';
import AppBrandLogo from '@/components/layout/AppBrandLogo.vue';
import AiUserInputCard from './AiUserInputCard.vue';
import githubIcon from '@/assets/share-github.svg';
import giteeIcon from '@/assets/share-gitee.svg';
import { QRCodeSVG } from 'ant-design-vue/es/qrcode/QRCode';
import { CheckCircleFilled, CheckOutlined, CopyOutlined, DownloadOutlined, LoadingOutlined } from '@ant-design/icons-vue';
import { SQL_EDITOR_TYPOGRAPHY } from '@/components/editor/sqlEditorTypography';
import { renderAiMessage } from '@/services/ai/aiMarkdown';
import { renderAiShareImage } from '@/services/ai/aiShareImage';

export default {
  name: 'AiShareModal',
  components: {
    CCModal,
    AppBrandLogo,
    AiUserInputCard,
    QRCodeSVG,
    CheckCircleFilled,
    CheckOutlined,
    CopyOutlined,
    DownloadOutlined,
    LoadingOutlined
  },
  props: {
    rows: { type: Array, required: true },
    initialSelectedIds: { type: Array, required: true }
  },
  emits: ['close'],
  data() {
    return {
      shareLinks: [
        { label: 'ai.shareGithub', url: 'https://github.com/ClouGence/open-cdm', icon: githubIcon },
        { label: 'ai.shareGitee', url: 'https://gitee.com/clougence/open-cdm', icon: giteeIcon }
      ],
      selectedIds: [...this.initialSelectedIds],
      copying: false,
      downloading: false,
      copied: false,
      error: '',
      disposed: false,
      copyTimer: null
    };
  },
  computed: {
    exporting() {
      return this.copying || this.downloading;
    },
    allSelected() {
      return this.selectedIds.length === this.rows.length;
    },
    sqlTypography() {
      return {
        '--share-sql-font': SQL_EDITOR_TYPOGRAPHY.fontFamily,
        '--share-sql-size': `${SQL_EDITOR_TYPOGRAPHY.fontSize}px`,
        '--share-sql-line-height': `${SQL_EDITOR_TYPOGRAPHY.lineHeight}px`
      };
    }
  },
  mounted() {
    this.$nextTick(() => {
      const row = Array.from(this.$refs.content.querySelectorAll('[data-share-row]')).find((item) =>
        this.selectedIds.includes(item.dataset.shareRow)
      );
      if (row) this.$refs.previewScroll.scrollTop = row.offsetTop - this.$refs.content.offsetTop;
    });
  },
  beforeUnmount() {
    this.disposed = true;
    clearTimeout(this.copyTimer);
  },
  methods: {
    renderMessage: renderAiMessage,
    close() {
      if (!this.exporting) this.$emit('close');
    },
    toggle(id) {
      if (this.exporting) return;
      this.copied = false;
      this.error = '';
      if (this.selectedIds.includes(id)) this.selectedIds = this.selectedIds.filter((value) => value !== id);
      else this.selectedIds.push(id);
    },
    toggleAll() {
      if (this.exporting) return;
      this.copied = false;
      this.error = '';
      if (this.allSelected) this.selectedIds = [];
      else this.selectedIds = this.rows.map((row) => row.id);
    },
    showError(error) {
      if (this.disposed) return;
      console.error('[ai-share] Image export failed', error);
      this.error = this.$t('ai.shareImageFailed');
    },
    async copyImage() {
      if (!this.selectedIds.length || this.exporting) return;
      this.error = '';
      if (!navigator.clipboard?.write || typeof ClipboardItem === 'undefined') {
        this.error = this.$t('ai.shareClipboardUnavailable');
        return;
      }
      this.copying = true;
      try {
        // Pass the image promise immediately to retain user activation in Safari.
        const image = renderAiShareImage(this.$refs.content, [...this.selectedIds], 'png', this.$t('ai.shareExportTruncated'));
        image.catch(() => {});
        await navigator.clipboard.write([new ClipboardItem({ 'image/png': image })]);
        if (this.disposed) return;
        this.copied = true;
        clearTimeout(this.copyTimer);
        this.copyTimer = setTimeout(() => {
          this.copied = false;
        }, 2000);
      } catch (error) {
        this.showError(error);
      } finally {
        this.copying = false;
      }
    },
    async downloadImage() {
      if (!this.selectedIds.length || this.exporting) return;
      this.error = '';
      this.downloading = true;
      try {
        const blob = await renderAiShareImage(this.$refs.content, [...this.selectedIds], 'jpeg', this.$t('ai.shareExportTruncated'));
        if (this.disposed) return;
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `CloudDM-${Date.now()}.jpg`;
        document.body.appendChild(link);
        link.click();
        link.remove();
        setTimeout(() => URL.revokeObjectURL(url), 1000);
      } catch (error) {
        this.showError(error);
      } finally {
        this.downloading = false;
      }
    }
  }
};
</script>

<style scoped lang="less">
.ai-share-preview {
  max-height: ~'min(60vh, 600px)';
  overflow: auto;
  position: relative;
}
.ai-share-content {
  background: #fff;
  color: #181d26;
  padding: 24px;
  box-sizing: border-box;
  --bg-primary: #fff;
  --bg-secondary: #f8fafc;
  --bg-tertiary: #f8fafc;
  --text-primary: #181d26;
  --text-secondary: #41454d;
  --border-primary: #ddd;
  --border-light: #ddd;
}
.ai-share-brand {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
  :deep(img) {
    filter: none !important;
  }
}
.ai-share-links {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  max-width: 272px;
  margin: 24px auto 0;
  padding-top: 24px;
}
.ai-share-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  .ai-share-qrcode {
    width: 128px;
    padding: 8px;
    max-width: 100%;
    height: auto;
    border: 1px solid #f3f4f6;
    border-radius: 12px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    overflow: hidden;
  }
  :deep(svg) {
    width: 100%;
    height: auto;
    display: block;
  }
}
.ai-share-row {
  position: relative;
  padding-left: 32px;
  margin-bottom: 24px;
  cursor: pointer;
  &:last-child {
    margin-bottom: 0;
  }
  &:focus-visible {
    outline: 2px solid var(--primary-color);
    outline-offset: 4px;
  }
}
.ai-share-check {
  position: absolute;
  left: 0;
  top: 4px;
  font-size: 20px;
  color: var(--primary-color);
}
.ai-share-unchecked {
  display: block;
  width: 20px;
  height: 20px;
  border: 1px solid #ddd;
  border-radius: 50%;
}
.ai-share-unselected {
  opacity: 0.4;
}
.ai-share-user {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}
.ai-share-prompt {
  max-width: 82%;
  padding: 12px 16px;
  border-radius: 10px;
  background: #f5f5f5;
  white-space: pre-wrap;
  overflow-wrap: anywhere;
  margin: 0;
}
.ai-share-sql,
.ai-share-reference {
  margin-top: 12px;
  background: #f8fafc;
  border-radius: 6px;
  overflow: hidden;
}
.ai-share-reference {
  max-width: 100%;
  padding: 12px;
}
.ai-share-sql-label {
  padding: 8px 12px;
  font-size: 12px;
  color: #41454d;
}
pre {
  font-family: var(--share-sql-font);
  font-size: var(--share-sql-size);
  line-height: var(--share-sql-line-height);
  font-weight: 400;
  letter-spacing: 0;
  white-space: pre-wrap;
  overflow-wrap: anywhere;
  padding: 12px;
  margin: 0;
}
.ai-share-markdown {
  font-size: 16px;
  line-height: 1.75;
  overflow-wrap: anywhere;
  :deep(p) {
    margin: 0 0 12px;
  }
  :deep(h1),
  :deep(h2),
  :deep(h3) {
    font-size: 16px;
    font-weight: 500;
    margin: 16px 0 8px;
  }
  :deep(ul),
  :deep(ol) {
    padding-left: 20px;
    margin: 8px 0;
  }
  :deep(ul) {
    list-style-type: disc;
  }
  :deep(ol) {
    list-style-type: decimal;
  }
  :deep(pre) {
    white-space: pre-wrap;
    overflow-wrap: anywhere;
    padding: 12px;
    background: #f8fafc;
  }
  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    table-layout: fixed;
  }
  :deep(td),
  :deep(th) {
    padding: 8px;
    border: 1px solid #ddd;
    overflow-wrap: anywhere;
  }
}
.ai-share-footer {
  width: 100%;
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto minmax(0, 1fr);
  align-items: center;
  gap: 8px;
}
.ai-share-export-actions {
  grid-column: 2;
  display: flex;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
  :deep(.ant-btn) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    margin: 0 !important;
  }
  :deep(.ant-btn > span) {
    margin-inline-start: 0;
  }
  :deep(.ant-btn > .anticon) {
    display: inline-flex;
    flex-shrink: 0;
  }
}
.ai-share-error {
  color: var(--color-danger);
  margin-top: 12px;
}
@media (max-width: 767px) {
  .ai-share-links {
    gap: 8px;
  }
  .ai-share-content {
    padding: 16px;
  }
  .ai-share-footer {
    grid-template-columns: minmax(0, 1fr);
  }
  .ai-share-export-actions {
    grid-column: 1;
    justify-self: center;
    display: grid;
    grid-template-columns: 1fr 1fr;
    :deep(.ant-btn) {
      min-width: 0 !important;
      min-height: 44px;
      height: auto;
      padding: 6px 8px;
      white-space: normal;
    }
  }
  .ai-share-prompt {
    max-width: 100%;
  }
}
</style>
