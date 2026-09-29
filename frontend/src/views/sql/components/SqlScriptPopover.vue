<template>
  <div ref="root" class="sql-script-control">
    <Button
      class="favorite-trigger"
      size="small"
      :class="{ active: !!tab.scriptId, dirty: isDirty }"
      :aria-label="$t('sql-favorites-title')"
      :title="$t('sql-favorites-title')"
      @click.stop="toggle"
    >
      <BookOutlined />
      <span v-if="isDirty" class="dirty-dot" aria-hidden="true" />
    </Button>

    <div v-if="visible" class="favorites-popover" role="dialog" :aria-label="$t('sql-favorites-title')" @click.stop>
      <div class="favorites-header">
        <span class="favorites-title">{{ $t('sql-favorites-title') }}</span>
        <div class="favorites-actions">
          <button
            type="button"
            class="favorites-icon-button"
            :aria-label="$t('sql-favorites-save-current')"
            :title="$t('sql-favorites-save-current')"
            @click.stop="openSaveModal('create')"
          >
            <FileAddOutlined />
          </button>
          <button
            v-if="isDirty"
            type="button"
            class="favorites-icon-button"
            :aria-label="$t('sql-favorites-save-changes')"
            :title="$t('sql-favorites-save-changes')"
            :disabled="saving"
            @click.stop="openOverwriteModal"
          >
            <SaveOutlined />
            <span class="dirty-dot" aria-hidden="true" />
          </button>
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

    <CCModal v-model="saveModalVisible" :title="saveModalTitle" :width="460">
      <Form label-position="top">
        <FormItem :label="$t('ming-cheng')">
          <Input v-model="saveName" maxlength="128" show-word-limit :placeholder="$t('sql-favorites-name-placeholder')" />
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="saveModalVisible = false">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="saving" @click="confirmSave">{{ $t('bao-cun') }}</Button>
      </template>
    </CCModal>

    <CCModal v-model="overwriteModalVisible" :title="$t('sql-favorites-overwrite-title')" :width="460" :closable="!saving" :keyboard="!saving">
      <a-select
        class="overwrite-select"
        :value="{ value: overwriteSelection?.scriptId, label: overwriteSelection?.name }"
        :options="overwriteOptions"
        :search-value="overwriteKeyword"
        :aria-label="$t('sql-favorites-overwrite-title')"
        :placeholder="$t('sql-favorites-search-placeholder')"
        :disabled="saving"
        :loading="overwriteLoading"
        :filter-option="false"
        show-search
        label-in-value
        @search="scheduleOverwriteLoad"
        @select="(_, option) => selectOverwriteTarget(option.script)"
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
          {{ $t('sql-favorites-overwrite-confirm') }}
        </Button>
      </template>
    </CCModal>

    <CCModal v-model="conflictVisible" :title="$t('sql-favorites-conflict-title')" :width="500">
      <p>{{ $t('sql-favorites-conflict-description') }}</p>
      <template #footer>
        <Button @click="conflictVisible = false">{{ $t('qu-xiao') }}</Button>
        <Button @click="loadServerVersion">{{ $t('sql-favorites-load-server') }}</Button>
        <Button type="primary" @click="openSaveModal('create')">{{ $t('sql-favorites-save-copy') }}</Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import dayjs from 'dayjs';
import { BookOutlined, FileAddOutlined, MoreOutlined, SaveOutlined, SearchOutlined } from '@ant-design/icons-vue';

const savingTabs = new WeakSet();

export default {
  name: 'SqlScriptPopover',
  components: { BookOutlined, FileAddOutlined, MoreOutlined, SaveOutlined, SearchOutlined },
  props: {
    tab: { type: Object, required: true },
    getEditorContent: Function,
    storeQueryTabs: Function
  },
  emits: ['open-script', 'script-deleted', 'script-renamed'],
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
      saveModalVisible: false,
      saveMode: 'create',
      saveName: '',
      saveDsType: '',
      selectedScript: null,
      selectedDetail: null,
      conflictVisible: false,
      conflictTargetTab: null,
      saveTargetTab: null,
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
    isDirty() {
      return !!this.tab.scriptId && this.tab.text !== this.tab.scriptSavedText;
    },
    saveModalTitle() {
      if (this.saveMode === 'rename') return this.$t('sql-favorites-rename-title');
      return this.$t('sql-favorites-create-title');
    },
    overwriteOptions() {
      return this.overwriteScripts.map((script) => ({
        value: script.scriptId,
        label: script.name,
        script,
        disabled: this.overwriteLoading
      }));
    },
    activeMenuScript() {
      return this.scripts.find((script) => script.scriptId === this.activeMenuScriptId) || null;
    }
  },
  watch: {
    keyword() {
      this.scheduleLoad();
    },
    overwriteModalVisible(visible) {
      if (visible) return;
      clearTimeout(this.overwriteLoadTimer);
      this.overwriteLoadSequence++;
    },
    tab(tab) {
      if (this.overwriteSource && tab !== this.overwriteSource.tab) this.overwriteModalVisible = false;
      if (this.conflictTargetTab && tab !== this.conflictTargetTab) {
        this.conflictVisible = false;
        this.conflictTargetTab = null;
      }
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
      this.closeMenu();
      this.loading = true;
      const res = await this.$services.dmQueryScriptList({
        data: { keyword: this.keyword, page: { pageNum: page, pageSize: 10 } }
      });
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
      this.selectedScript = script;
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
    async openSaveModal(mode, useSelectedDetail = false) {
      const conflictTarget = this.conflictVisible && mode === 'create' ? this.conflictTargetTab : null;
      this.conflictVisible = false;
      if (!useSelectedDetail) this.selectedDetail = null;
      const editorContent = this.getEditorContent?.();
      if (typeof editorContent === 'string') this.tab.text = editorContent;
      this.saveTargetTab = conflictTarget || this.tab;
      this.saveMode = mode;
      this.saveDsType = this.saveTargetTab.scriptDsType || this.saveTargetTab.dsType;
      if (mode === 'rename') {
        this.saveName = this.selectedDetail.name;
      } else {
        this.saveName = this.saveTargetTab.title || '';
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
      const targetTab = this.saveTargetTab || this.tab;
      if (this.saving || savingTabs.has(targetTab)) return;
      const mode = this.saveMode;
      const dsType = this.saveDsType;
      const sqlContent = mode === 'rename' ? source.sqlContent : targetTab.text || '';
      const associationRevision = targetTab.scriptAssociationRevision || 0;
      this.saving = true;
      savingTabs.add(targetTab);
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
        if (mode === 'create') {
          this.bindTab(targetTab, res.data, name, dsType, sqlContent, associationRevision);
        }
        if (mode === 'rename') {
          this.$emit('script-renamed', {
            scriptId: source.scriptId,
            name,
            previousVersion: source.version,
            version: res.data.version,
            sqlContent
          });
        }
        this.saveModalVisible = false;
        this.selectedDetail = null;
        this.selectedScript = null;
        this.saveTargetTab = null;
        await this.loadScripts(1);
        this.$Message.success(this.$t('sql-favorites-save-success'));
      } finally {
        this.saving = false;
        savingTabs.delete(targetTab);
      }
    },
    openOverwriteModal() {
      if (this.saving || savingTabs.has(this.tab)) return;
      const targetTab = this.tab;
      const editorContent = this.getEditorContent?.();
      if (typeof editorContent === 'string') targetTab.text = editorContent;
      if (!this.validateContent(targetTab.text)) return;
      this.overwriteSource = {
        tab: targetTab,
        scriptId: targetTab.scriptId,
        version: targetTab.scriptVersion,
        name: targetTab.scriptName,
        dsType: targetTab.scriptDsType,
        sqlContent: targetTab.text,
        associationRevision: targetTab.scriptAssociationRevision || 0
      };
      this.overwriteSelection = {
        scriptId: targetTab.scriptId,
        version: targetTab.scriptVersion,
        name: targetTab.scriptName
      };
      this.overwriteKeyword = '';
      this.overwriteScripts = [];
      this.overwritePage = 1;
      this.overwritePages = 1;
      this.visible = false;
      this.closeMenu();
      this.overwriteModalVisible = true;
      this.loadOverwriteScripts(1);
    },
    scheduleOverwriteLoad(keyword) {
      this.overwriteKeyword = keyword;
      clearTimeout(this.overwriteLoadTimer);
      this.overwriteLoadSequence++;
      this.overwriteLoading = true;
      this.overwriteLoadTimer = setTimeout(() => this.loadOverwriteScripts(1), 250);
    },
    async loadOverwriteScripts(page = 1) {
      clearTimeout(this.overwriteLoadTimer);
      const sequence = ++this.overwriteLoadSequence;
      this.overwriteLoading = true;
      this.overwriteLoadFailed = false;
      try {
        const res = await this.$services.dmQueryScriptList({
          data: { keyword: this.overwriteKeyword, page: { pageNum: page, pageSize: 10 } }
        });
        if (sequence !== this.overwriteLoadSequence || !this.overwriteModalVisible) return;
        if (!res.success) {
          this.overwriteLoadFailed = true;
          this.overwriteScripts = [];
          this.$Message.error(res.msg || this.$t('sql-favorites-load-failed'));
          return;
        }
        this.overwriteScripts = res.data.records;
        this.overwritePage = Number(res.data.current);
        this.overwritePages = Number(res.data.pages);
      } finally {
        if (sequence === this.overwriteLoadSequence) this.overwriteLoading = false;
      }
    },
    selectOverwriteTarget(script) {
      this.overwriteSelection = { scriptId: script.scriptId, version: script.version, name: script.name };
      // Keep the version originally loaded into the editor when saving its associated favorite.
      if (script.scriptId === this.overwriteSource.scriptId) {
        this.overwriteSelection.version = this.overwriteSource.version;
        this.overwriteSelection.name = this.overwriteSource.name;
      }
      if (this.overwriteKeyword) this.scheduleOverwriteLoad('');
    },
    async confirmOverwrite() {
      const source = this.overwriteSource;
      if (!this.overwriteModalVisible || !this.overwriteSelection || this.overwriteLoading || this.overwriteLoadFailed) return false;
      const targetTab = source.tab;
      if (this.saving || savingTabs.has(targetTab)) return false;
      const snapshot = {
        ...this.overwriteSelection,
        sqlContent: source.sqlContent,
        dsType: source.dsType,
        associationRevision: source.associationRevision
      };
      this.saving = true;
      savingTabs.add(targetTab);
      try {
        if (!this.validateContent(snapshot.sqlContent)) return false;
        const res = await this.$services.dmQueryScriptUpdate({
          data: {
            scriptId: snapshot.scriptId,
            version: snapshot.version,
            name: snapshot.name,
            dsType: snapshot.dsType,
            sqlContent: snapshot.sqlContent
          }
        });
        if (!res.success) {
          if (!this.overwriteModalVisible || this.overwriteSource !== source) return false;
          if (!this.isTabAssociationCurrent(targetTab, snapshot.associationRevision)) return false;
          const latest = await this.fetchDetail(snapshot.scriptId, false);
          if (
            !this.overwriteModalVisible ||
            this.overwriteSource !== source ||
            !this.isTabAssociationCurrent(targetTab, snapshot.associationRevision)
          )
            return false;
          if (latest && latest.version !== snapshot.version) {
            const saved = latest.name === snapshot.name && latest.dsType === snapshot.dsType && latest.sqlContent === snapshot.sqlContent;
            if (saved) {
              this.bindTab(targetTab, latest, snapshot.name, snapshot.dsType, snapshot.sqlContent, snapshot.associationRevision);
              this.overwriteModalVisible = false;
              await this.loadScripts(this.page);
              this.$Message.success(this.$t('sql-favorites-save-success'));
              return targetTab.text === targetTab.scriptSavedText;
            }
            this.selectedDetail = latest;
            this.conflictTargetTab = targetTab;
            this.overwriteModalVisible = false;
            this.conflictVisible = true;
          } else {
            this.$Message.error(res.msg || this.$t('sql-favorites-save-failed'));
          }
          return false;
        }
        this.bindTab(targetTab, res.data, snapshot.name, snapshot.dsType, snapshot.sqlContent, snapshot.associationRevision);
        if (this.overwriteSource === source) this.overwriteModalVisible = false;
        await this.loadScripts(this.page);
        this.$Message.success(this.$t('sql-favorites-save-success'));
        return targetTab.text === targetTab.scriptSavedText;
      } finally {
        this.saving = false;
        savingTabs.delete(targetTab);
      }
    },
    isTabAssociationCurrent(targetTab, expectedRevision) {
      return (targetTab.scriptAssociationRevision || 0) === expectedRevision;
    },
    bindTab(targetTab, result, name, dsType, content, expectedRevision) {
      if (!this.isTabAssociationCurrent(targetTab, expectedRevision)) return false;
      targetTab.scriptId = result.scriptId;
      targetTab.scriptVersion = result.version;
      targetTab.scriptName = name;
      targetTab.scriptDsType = dsType;
      targetTab.scriptSavedText = content;
      targetTab.scriptAssociationRevision = expectedRevision + 1;
      targetTab.isEditing = targetTab.text !== content;
      this.storeQueryTabs?.();
      return true;
    },
    async loadServerVersion() {
      const targetTab = this.conflictTargetTab || this.tab;
      const detail = this.selectedDetail || (await this.fetchDetail(targetTab.scriptId));
      if (!detail) return;
      this.conflictVisible = false;
      this.conflictTargetTab = null;
      this.$emit('open-script', { ...detail, forceReload: true });
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
          this.$emit('script-deleted', script.scriptId);
          await this.loadScripts(1);
          this.$Message.success(this.$t('sql-favorites-delete-success'));
        }
      });
    },
    async fetchDetail(scriptId, showError = true) {
      const res = await this.$services.dmQueryScriptDetail({ data: { scriptId } });
      if (res.success) return res.data;
      if (showError) this.$Message.error(res.msg || this.$t('sql-favorites-load-failed'));
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
.favorite-trigger.active {
  background: var(--bg-secondary);
  color: var(--text-primary);
}
.dirty-dot {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--primary-color);
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
.overwrite-select {
  width: 100%;
}
</style>
