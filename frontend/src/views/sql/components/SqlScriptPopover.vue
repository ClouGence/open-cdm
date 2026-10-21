<template>
  <div ref="root" class="sql-script-control">
    <Button class="favorite-trigger" size="small" :aria-label="$t('sql-favorites-title')" :title="$t('sql-favorites-title')" @click.stop="toggle">
      <BookOutlined />
    </Button>

    <div v-if="visible" class="favorites-popover" role="dialog" :aria-label="$t('sql-favorites-title')" @click.stop>
      <div class="favorites-header">
        <span class="favorites-title">{{ $t('sql-favorites-title') }}</span>
        <div class="favorites-actions">
          <a-dropdown :trigger="['click']" placement="bottomRight">
            <button
              type="button"
              class="favorites-icon-button"
              :aria-label="$t('sql-favorites-save')"
              :title="$t('sql-favorites-save')"
              :disabled="saving"
              @click.stop
            >
              <FileAddOutlined />
            </button>
            <template #overlay>
              <a-menu>
                <a-menu-item key="create" @click="openSaveModal('create')">{{ $t('sql-favorites-save-as') }}</a-menu-item>
                <a-menu-item key="overwrite" @click="openOverwriteModal">{{ $t('sql-favorites-save-existing') }}</a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
          <button
            type="button"
            class="favorites-icon-button"
            :aria-label="$t('shua-xin')"
            :title="$t('shua-xin')"
            :disabled="loading"
            @click="loadScripts(1)"
          >
            <cc-svg-icon name="refresh" :size="16" />
          </button>
        </div>
      </div>
      <a-input v-model:value="keyword" class="favorites-search" size="small" allow-clear :placeholder="$t('sql-favorites-search-placeholder')">
        <template #prefix>
          <SearchOutlined />
        </template>
      </a-input>
      <div ref="list" class="favorites-list" :class="{ loading }">
        <div v-for="script in scripts" :key="script.scriptId" class="favorite-row">
          <button type="button" class="favorite-open" @click="openScript(script)">
            <span class="favorite-copy">
              <span class="favorite-name">{{ script.name }}</span>
              <span class="favorite-meta">{{ formatTime(script.gmtModified) }}</span>
            </span>
          </button>
          <button
            type="button"
            class="favorite-more"
            :aria-label="$t('sql-favorites-more-actions')"
            :aria-expanded="activeMenuScriptId === script.scriptId"
            @click.stop="toggleMenu(script.scriptId, $event)"
          >
            <MoreOutlined />
          </button>
        </div>
        <div v-if="!loading && !scripts.length" class="favorites-empty">{{ $t('sql-favorites-empty') }}</div>
      </div>
      <div v-if="pages > 1" class="favorites-pagination">
        <Button type="text" size="small" :disabled="page <= 1" @click="loadScripts(page - 1)">
          {{ $t('sql-favorites-previous-page') }}
        </Button>
        <span>{{ page }} / {{ pages }}</span>
        <Button type="text" size="small" :disabled="page >= pages" @click="loadScripts(page + 1)">
          {{ $t('sql-favorites-next-page') }}
        </Button>
      </div>
    </div>

    <Teleport to="body">
      <div
        v-if="activeMenuScript"
        ref="menu"
        class="favorite-menu"
        :style="{
          top: `${menuPosition.top}px`,
          left: `${menuPosition.left}px`,
          visibility: menuPositioned ? 'visible' : 'hidden'
        }"
        @click.stop
      >
        <button type="button" @click="handleMenu('open', activeMenuScript)">{{ $t('sql-favorites-open') }}</button>
        <button type="button" @click="handleMenu('rename', activeMenuScript)">{{ $t('sql-favorites-rename') }}</button>
        <button type="button" class="favorite-delete-action" @click="handleMenu('delete', activeMenuScript)">
          {{ $t('shan-chu') }}
        </button>
      </div>
    </Teleport>

    <CCModal v-model="saveModalVisible" :title="saveModalTitle" :width="460" :closable="!saving" :keyboard="!saving">
      <Form label-position="top">
        <FormItem :label="$t('ming-cheng')">
          <Input v-model="saveName" :disabled="saving" maxlength="128" show-word-limit :placeholder="$t('sql-favorites-name-placeholder')" />
        </FormItem>
      </Form>
      <template #footer>
        <Button :disabled="saving" @click="saveModalVisible = false">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="saving" @click="confirmSave">{{ $t('sql-favorites-save') }}</Button>
      </template>
    </CCModal>

    <CCModal v-model="overwriteModalVisible" :title="$t('sql-favorites-save-title')" :width="460" :closable="!saving" :keyboard="!saving">
      <p class="favorites-save-description">{{ $t('sql-favorites-save-description') }}</p>
      <a-select
        class="favorites-save-select"
        :value="overwriteSelection && { value: overwriteSelection.scriptId, label: overwriteSelection.name }"
        :options="overwriteOptions"
        :search-value="overwriteKeyword"
        :aria-label="$t('sql-favorites-save-target')"
        :placeholder="$t('sql-favorites-save-target')"
        :disabled="saving"
        :loading="overwriteLoading"
        :filter-option="false"
        show-search
        label-in-value
        @search="scheduleOverwriteLoad"
        @select="(_, option) => (overwriteSelection = option.script)"
      >
        <template #notFoundContent>
          <span v-if="overwriteLoading">{{ $t('zheng-zai-jia-zai-shu-ju') }}</span>
          <span v-else>{{ $t('sql-favorites-empty') }}</span>
        </template>
        <template #dropdownRender="{ menuNode }">
          <component :is="menuNode" />
          <div v-if="overwriteLoadFailed" class="favorites-pagination" @mousedown.prevent>
            <Button type="text" @click="loadOverwriteScripts(1)">{{ $t('shua-xin') }}</Button>
          </div>
          <div v-else-if="overwritePages > 1" class="favorites-pagination" @mousedown.prevent>
            <Button
              type="text"
              size="small"
              :disabled="saving || overwriteLoading || overwritePage <= 1"
              @click="loadOverwriteScripts(overwritePage - 1)"
            >
              {{ $t('sql-favorites-previous-page') }}
            </Button>
            <span>{{ overwritePage }} / {{ overwritePages }}</span>
            <Button
              type="text"
              size="small"
              :disabled="saving || overwriteLoading || overwritePage >= overwritePages"
              @click="loadOverwriteScripts(overwritePage + 1)"
            >
              {{ $t('sql-favorites-next-page') }}
            </Button>
          </div>
        </template>
      </a-select>
      <template #footer>
        <Button :disabled="saving" @click="overwriteModalVisible = false">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="saving" :disabled="!overwriteSelection || overwriteLoading || overwriteLoadFailed" @click="confirmOverwrite">
          {{ $t('sql-favorites-save') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import dayjs from 'dayjs';
import { BookOutlined, FileAddOutlined, MoreOutlined, SearchOutlined } from '@ant-design/icons-vue';

export default {
  name: 'SqlScriptPopover',
  components: { BookOutlined, FileAddOutlined, MoreOutlined, SearchOutlined },
  props: {
    tab: { type: Object, required: true },
    getEditorContent: Function
  },
  emits: ['open-script'],
  data() {
    return {
      visible: false,
      loading: false,
      saving: false,
      scripts: [],
      keyword: '',
      page: 1,
      pages: 1,
      loadTimer: null,
      loadSequence: 0,
      saveModalVisible: false,
      saveMode: 'create',
      saveName: '',
      saveDsType: '',
      selectedDetail: null,
      saveSqlContent: '',
      overwriteModalVisible: false,
      overwriteSource: null,
      overwriteSelection: null,
      overwriteScripts: [],
      overwriteKeyword: '',
      overwritePage: 1,
      overwritePages: 1,
      overwriteLoading: false,
      overwriteLoadFailed: false,
      overwriteLoadTimer: null,
      overwriteLoadSequence: 0,
      activeMenuScriptId: null,
      menuAnchor: null,
      menuPosition: { top: 0, left: 0 },
      menuPositioned: false
    };
  },
  computed: {
    listDsType() {
      return this.tab.dsType;
    },
    saveModalTitle() {
      if (this.saveMode === 'rename') return this.$t('sql-favorites-rename-title');
      return this.$t('sql-favorites-create-title');
    },
    overwriteOptions() {
      return this.overwriteScripts.map((script) => ({ value: script.scriptId, label: script.name, script, disabled: this.overwriteLoading }));
    },
    activeMenuScript() {
      return this.scripts.find((script) => script.scriptId === this.activeMenuScriptId) || null;
    }
  },
  watch: {
    listDsType() {
      clearTimeout(this.loadTimer);
      this.loadSequence++;
      this.scripts = [];
      this.page = 1;
      this.pages = 1;
      this.closeMenu();
      if (this.visible) this.loadScripts(1);
    },
    keyword() {
      this.scheduleLoad();
    },
    overwriteModalVisible(visible) {
      if (visible) return;
      clearTimeout(this.overwriteLoadTimer);
      this.overwriteLoadSequence++;
    },
    tab() {
      if (!this.saving) this.overwriteModalVisible = false;
    }
  },
  mounted() {
    document.addEventListener('click', this.handleOutsideClick);
    document.addEventListener('keydown', this.handleKeydown);
    window.addEventListener('scroll', this.positionMenu, true);
    window.addEventListener('resize', this.positionMenu);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleOutsideClick);
    document.removeEventListener('keydown', this.handleKeydown);
    window.removeEventListener('scroll', this.positionMenu, true);
    window.removeEventListener('resize', this.positionMenu);
    clearTimeout(this.loadTimer);
    this.loadSequence++;
    clearTimeout(this.overwriteLoadTimer);
    this.overwriteLoadSequence++;
  },
  methods: {
    async toggle() {
      this.visible = !this.visible;
      this.closeMenu();
      if (this.visible) await this.loadScripts(1);
    },
    handleOutsideClick(event) {
      if (this.$refs.root?.contains(event.target) || this.$refs.menu?.contains(event.target)) return;
      this.visible = false;
      this.closeMenu();
    },
    handleKeydown(event) {
      if (event.key !== 'Escape') return;
      this.visible = false;
      this.closeMenu();
    },
    scheduleLoad() {
      clearTimeout(this.loadTimer);
      this.loadTimer = setTimeout(() => this.loadScripts(1), 250);
    },
    async loadScripts(page = 1) {
      const sequence = ++this.loadSequence;
      this.closeMenu();
      this.loading = true;
      const res = await this.$services.dmQueryScriptList({
        data: { keyword: this.keyword, dsType: this.listDsType, page: { pageNum: page, pageSize: 10 } }
      });
      if (sequence !== this.loadSequence) return;
      this.loading = false;
      if (!res.success) {
        this.$Message.error(res.msg || this.$t('sql-favorites-load-failed'));
        return;
      }
      this.scripts = res.data.records || [];
      this.page = Number(res.data.current || 1);
      this.pages = Number(res.data.pages || 1);
      return true;
    },
    formatTime(value) {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm') : '';
    },
    openScript(script) {
      this.visible = false;
      this.closeMenu();
      this.$emit('open-script', script);
    },
    toggleMenu(scriptId, event) {
      if (this.activeMenuScriptId === scriptId) {
        this.closeMenu();
        return;
      }
      this.activeMenuScriptId = scriptId;
      this.menuAnchor = event.currentTarget;
      this.menuPositioned = false;
      this.$nextTick(this.positionMenu);
    },
    closeMenu() {
      this.activeMenuScriptId = null;
      this.menuAnchor = null;
      this.menuPositioned = false;
    },
    positionMenu() {
      const anchor = this.menuAnchor;
      const menu = this.$refs.menu;
      if (!anchor || !menu || !anchor.isConnected) return;

      const anchorRect = anchor.getBoundingClientRect();
      const listRect = this.$refs.list?.getBoundingClientRect();
      if (listRect && (anchorRect.bottom <= listRect.top || anchorRect.top >= listRect.bottom)) {
        this.closeMenu();
        return;
      }

      const gap = 4;
      const viewportPadding = 8;
      const menuWidth = menu.offsetWidth;
      const menuHeight = menu.offsetHeight;
      let top = anchorRect.bottom + gap;
      if (top + menuHeight > window.innerHeight - viewportPadding) {
        top = anchorRect.top - menuHeight - gap;
      }
      top = Math.max(viewportPadding, Math.min(top, window.innerHeight - menuHeight - viewportPadding));

      let left = anchorRect.right - menuWidth;
      left = Math.max(viewportPadding, Math.min(left, window.innerWidth - menuWidth - viewportPadding));
      this.menuPosition = { top, left };
      this.menuPositioned = true;
    },
    async handleMenu(action, script) {
      this.closeMenu();
      if (action === 'open') {
        this.openScript(script);
        return;
      }
      if (action === 'delete') {
        this.confirmDelete(script);
        return;
      }
      const detail = await this.fetchDetail(script.scriptId);
      if (!detail) return;
      this.selectedDetail = detail;
      if (action === 'rename') this.openSaveModal('rename', true);
    },
    openSaveModal(mode, useSelectedDetail = false) {
      if (this.saving) return;
      if (!useSelectedDetail) this.selectedDetail = null;
      this.saveMode = mode;
      this.saveDsType = this.tab.dsType;
      this.saveSqlContent = this.getEditorContent?.() || '';
      if (mode === 'rename') {
        this.saveName = this.selectedDetail.name;
      } else {
        this.saveName = this.tab.title || '';
      }
      this.saveModalVisible = true;
    },
    async confirmSave() {
      const name = this.saveName.trim();
      if (!name) {
        this.$Message.warning(this.$t('sql-favorites-name-required'));
        return;
      }
      const source = this.selectedDetail;
      if (this.saving) return;
      const mode = this.saveMode;
      const dsType = this.saveDsType;
      let sqlContent = this.saveSqlContent;
      if (mode === 'rename') sqlContent = source.sqlContent;
      this.saving = true;
      try {
        if (!this.validateContent(sqlContent)) return;
        let res;
        if (mode === 'rename') {
          res = await this.$services.dmQueryScriptUpdate({
            data: { scriptId: source.scriptId, version: source.version, name, dsType: source.dsType, sqlContent }
          });
        } else {
          res = await this.$services.dmQueryScriptCreate({ data: { name, dsType, sqlContent } });
        }
        if (!res.success) {
          this.$Message.error(res.msg || this.$t('sql-favorites-save-failed'));
          return;
        }
        this.saveModalVisible = false;
        this.selectedDetail = null;
        await this.loadScripts(1);
        this.$Message.success(this.$t('sql-favorites-save-success'));
      } finally {
        this.saving = false;
      }
    },
    openOverwriteModal() {
      if (this.saving) return;
      const sqlContent = this.getEditorContent?.() || '';
      if (!this.validateContent(sqlContent)) return;
      this.overwriteSource = { dsType: this.tab.dsType, sqlContent, lastInsertedScriptId: this.tab.lastInsertedScriptId };
      this.overwriteSelection = null;
      this.overwriteKeyword = '';
      this.overwriteScripts = [];
      this.overwritePage = 1;
      this.overwritePages = 1;
      this.visible = false;
      this.closeMenu();
      this.overwriteModalVisible = true;
      this.loadOverwriteScripts(1, true);
    },
    scheduleOverwriteLoad(keyword) {
      this.overwriteKeyword = keyword;
      if (keyword) this.overwriteSelection = null;
      clearTimeout(this.overwriteLoadTimer);
      this.overwriteLoadSequence++;
      this.overwriteLoading = true;
      this.overwriteLoadTimer = setTimeout(() => this.loadOverwriteScripts(1), 250);
    },
    async loadOverwriteScripts(page = 1, selectDefault = false) {
      clearTimeout(this.overwriteLoadTimer);
      const sequence = ++this.overwriteLoadSequence;
      this.overwriteLoading = true;
      this.overwriteLoadFailed = false;
      try {
        const res = await this.$services.dmQueryScriptList({
          data: { keyword: this.overwriteKeyword, dsType: this.overwriteSource.dsType, page: { pageNum: page, pageSize: 10 } }
        });
        if (sequence !== this.overwriteLoadSequence || !this.overwriteModalVisible) return;
        if (!res.success) {
          this.overwriteLoadFailed = true;
          this.overwriteScripts = [];
          this.$Message.error(res.msg || this.$t('sql-favorites-load-failed'));
          return;
        }
        this.overwriteScripts = res.data.records;
        if (selectDefault && this.overwriteSource.lastInsertedScriptId) {
          const scriptId = this.overwriteSource.lastInsertedScriptId;
          let recentScript = this.overwriteScripts.find((script) => script.scriptId === scriptId);
          if (!recentScript) {
            const detail = await this.$services.dmQueryScriptDetail({ data: { scriptId } });
            if (sequence !== this.overwriteLoadSequence || !this.overwriteModalVisible) return;
            if (detail.success && detail.data.dsType === this.overwriteSource.dsType) recentScript = detail.data;
          }
          this.overwriteSelection = recentScript || null;
        }
        this.overwritePage = Number(res.data.current);
        this.overwritePages = Number(res.data.pages);
      } finally {
        if (sequence === this.overwriteLoadSequence) this.overwriteLoading = false;
      }
    },
    async confirmOverwrite() {
      if (this.saving || !this.overwriteSelection || this.overwriteLoading || this.overwriteLoadFailed) return;
      const source = this.overwriteSource;
      const target = this.overwriteSelection;
      this.saving = true;
      try {
        const res = await this.$services.dmQueryScriptUpdate({
          data: { scriptId: target.scriptId, version: target.version, name: target.name, dsType: source.dsType, sqlContent: source.sqlContent }
        });
        if (!res.success) {
          this.$Message.error(res.msg || this.$t('sql-favorites-save-failed'));
          this.overwriteSelection = null;
          await this.loadOverwriteScripts(this.overwritePage);
          return;
        }
        this.overwriteModalVisible = false;
        await this.loadScripts(1);
        this.$Message.success(this.$t('sql-favorites-save-success'));
      } finally {
        this.saving = false;
      }
    },
    confirmDelete(script) {
      this.$Modal.confirm({
        title: this.$t('sql-favorites-delete-title'),
        content: this.$t('sql-favorites-delete-description'),
        okText: this.$t('shan-chu'),
        cancelText: this.$t('qu-xiao'),
        onOk: async () => {
          const res = await this.$services.dmQueryScriptDelete({ data: { scriptId: script.scriptId } });
          if (!res.success) {
            this.$Message.error(res.msg || this.$t('sql-favorites-delete-failed'));
            return;
          }
          await this.loadScripts(1);
          this.$Message.success(this.$t('sql-favorites-delete-success'));
        }
      });
    },
    async fetchDetail(scriptId) {
      const res = await this.$services.dmQueryScriptDetail({ data: { scriptId } });
      if (res.success) return res.data;
      this.$Message.error(res.msg || this.$t('sql-favorites-load-failed'));
      return null;
    },
    validateContent(content) {
      if (typeof content !== 'string' || !content.trim()) {
        this.$Message.warning(this.$t('sql-favorites-content-required'));
        return false;
      }
      return true;
    }
  }
};
</script>

<style scoped lang="less">
.sql-script-control {
  position: relative;
}

.favorite-trigger {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 32px;
  padding: 0 8px;
  line-height: 1;
}

.favorite-trigger :deep(.anticon) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
}

.favorite-trigger :deep(.anticon > svg) {
  display: block;
}

.favorites-popover {
  position: absolute;
  top: 36px;
  right: 0;
  width: 336px;
  padding: 16px;
  border: 1px solid var(--border-primary);
  border-radius: 10px;
  background: var(--bg-primary, #fff);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  color: var(--text-primary);
}

.favorites-header,
.favorite-meta,
.favorites-pagination {
  display: flex;
  align-items: center;
}

.favorites-header {
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 12px;
}

.favorites-title {
  font-size: 16px;
  font-weight: 500;
}

.favorites-actions {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  gap: 8px;
}

.favorites-save-description {
  margin-bottom: 16px;
  color: var(--text-secondary);
}

.favorites-save-select {
  width: 100%;
}

.favorites-search {
  width: 100%;
  min-width: 0;
  height: 32px;
  border-radius: 6px;
}

.favorites-search :deep(.ant-input-prefix) {
  margin-right: 8px;
  color: var(--text-primary);
  font-size: 16px;
}

.favorites-icon-button {
  position: relative;
  display: flex;
  flex: 0 0 32px;
  width: 32px;
  height: 32px;
  align-items: center;
  justify-content: center;
  padding: 0;
  border: 0;
  border-radius: 4px;
  background: transparent;
  color: var(--text-primary);
  font-size: 16px;
  cursor: pointer;
}

.favorites-icon-button:hover,
.favorites-icon-button:focus-visible {
  background: var(--bg-hover);
}

.favorites-icon-button:focus-visible {
  outline: 1px solid var(--primary-color);
  outline-offset: 1px;
}

.favorites-icon-button:disabled {
  opacity: 0.5;
  cursor: default;
}

.favorite-meta,
.favorites-pagination {
  color: var(--text-secondary);
  font-size: 12px;
}

.favorites-list {
  max-height: 320px;
  margin: 8px -8px 0;
  overflow-y: auto;
}

.favorite-row {
  position: relative;
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  border-radius: 6px;
  background: transparent;
  color: inherit;
}

.favorite-row:hover {
  background: var(--bg-secondary);
}

.favorite-open {
  flex: 1;
  min-width: 0;
  padding: 10px 8px;
  border: 0;
  background: transparent;
  color: inherit;
  text-align: left;
  cursor: pointer;
}

.favorite-more {
  padding: 4px 8px;
  border: 0;
  background: transparent;
  color: inherit;
  cursor: pointer;
}

.favorite-menu {
  position: fixed;
  z-index: 1100;
  display: flex;
  width: 140px;
  max-width: calc(100vw - 16px);
  flex-direction: column;
  padding: 4px 0;
  border: 1px solid var(--border-primary);
  border-radius: 4px;
  background: var(--bg-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.favorite-menu button {
  padding: 8px 12px;
  border: 0;
  background: transparent;
  color: inherit;
  text-align: left;
  cursor: pointer;
}

.favorite-menu button:hover {
  background: var(--bg-secondary);
}

.favorite-copy,
.favorite-name {
  display: block;
  min-width: 0;
}

.favorite-copy {
  flex: 1;
}

.favorite-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.favorite-meta {
  margin-top: 4px;
}

.favorites-empty {
  padding: 32px 8px;
  color: var(--text-secondary);
  text-align: center;
}

.favorites-pagination {
  justify-content: center;
  gap: 8px;
}

.favorite-delete-action {
  color: #ed4014;
}
</style>
