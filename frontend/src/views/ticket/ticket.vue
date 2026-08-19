<template>
  <div class="ticket-create-container">
    <div class="create-content-container">
      <div class="create-ticket-editor">
        <div class="create-ticket-editor-toolbar">
          <div class="create-ticket-editor-operator">
            <DsSelect
              :ticketData="ticketData"
              :handleChangeInstance="handleChangeInstance"
              :allDsList="allDsList"
              :handle-catalog-change="handleCatalogChange"
              :selectedDs="selectedDs"
              required
              :error="instanceRequiredError"
              @restore-schema="restoreSchema"
              @restore-catalog="restoreCatalog"
            ></DsSelect>
          </div>
          <div class="sql-mode-actions">
            <Button size="small" :type="contentType === 'INLINE' ? 'primary' : 'default'" @click="switchToInline">
              {{ $t('ticket-sql-online-edit') }}
            </Button>
            <Button size="small" :type="contentType === 'ATTACHMENT' ? 'primary' : 'default'" @click="handleAttachmentModeAction">
              {{ $t(contentType === 'ATTACHMENT' && sqlAttachment ? 'ticket-sql-reupload' : 'ticket-sql-file-upload') }}
            </Button>
          </div>
        </div>
        <div class="editor">
          <div class="collapse raw">
            <div v-if="contentType === 'ATTACHMENT' && sqlAttachment" class="sql-file-meta">
              <span class="sql-file-name">{{ sqlAttachment.fileName }}</span>
              <span>{{ formatFileSize(sqlAttachment.fileSize) }}</span>
              <span>{{ $t('ticket-sql-readonly') }}</span>
            </div>
            <div v-if="partialSqlNotice && contentType === 'INLINE'" class="partial-sql-notice">
              <span>{{ $t('ticket-sql-partial-loaded') }}</span>
              <button type="button" @click="partialSqlNotice = false" :aria-label="$t('guan-bi')">
                <Icon type="md-close" />
              </button>
            </div>
            <div class="content sql-editor-content" @wheel="handlePreviewWheel">
              <ticket-editor
                ref="rawSqlEditor"
                :data-source-type="ticketData.dataSourceType"
                :read-only="contentType === 'ATTACHMENT'"
                :virtual-scroll-mode="contentType === 'ATTACHMENT'"
              />
              <input
                v-if="contentType === 'ATTACHMENT' && sqlAttachment"
                v-model.number="previewStartLine"
                class="virtual-scrollbar"
                type="range"
                min="1"
                :max="previewMaxStartLine"
                step="1"
                :aria-label="$t('ticket-sql-virtual-scrollbar')"
                aria-orientation="vertical"
                @input="schedulePreview"
              />
            </div>
          </div>
          <div class="resize-handle" v-if="showValidationResult" @mousedown="startResize"></div>
          <div class="collapse validation-result" v-if="showValidationResult" :style="{ height: validationResultHeight + 'px' }">
            <div class="title" style="display: flex; justify-content: space-between">
              <div>
                <Icon type="md-warning" :style="{ color: hasError ? '#f5222d' : '#faad14', marginRight: '8px' }" />
                <span>{{ $t('gui-ze-xiao-yan-jie-guo') }}</span>
              </div>
              <div>
                <Checkbox v-model="showCheckedOnlyError">{{ $t('jin-xian-shi-yan-zhong') }}</Checkbox>
              </div>
            </div>
            <div class="content">
              <div class="validation-content">
                <div v-for="(rule, index) in checkRoleResultList()" :key="index" class="rule-item">
                  <div class="rule-header">
                    <Tag :color="rule.ruleLevel === 'SUGGEST' ? 'warning' : 'error'" class="rule-level">
                      {{ RULE_WARN_LEVEL[rule.ruleLevel] }}
                    </Tag>
                    <span class="rule-name">{{ rule.name }}</span>
                    <div v-if="rule.lines && rule.lines.length" class="rule-lines">
                      <span class="lines-label">{{ $t('wei-zhi-0') }}:</span>
                      <span v-for="line in rule.lines" :key="line" class="lines-content">{{ line }}</span>
                      <span v-if="rule.hitCount > rule.lines.length" class="lines-content">
                        {{ $t('ticket-rule-location-total', { count: rule.hitCount }) }}
                      </span>
                    </div>
                  </div>
                  <div class="rule-desc">{{ rule.desc }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="create-ticket-content">
        <a-form label-position="top" :labelCol="{ span: 24 }" :label-wrap="true" :model="ticketData" :rules="ticketRuleValidate" ref="ticketContent">
          <a-form-item :label="$t('biao-ti')" name="ticketTitle">
            <Input v-model="ticketData.ticketTitle" />
          </a-form-item>
          <a-form-item :label="$t('xu-qiu-miao-shu')" name="description">
            <Input type="textarea" v-model="ticketData.description" :rows="4" />
          </a-form-item>
        </a-form>
        <div class="create-ticket-form-btn">
          <Button type="primary" :loading="loading" :disabled="loading || !ticketData.ticketEnable" @click="handleSubmitTicket(false)">
            {{ $t('jiao-yan-0') }}
          </Button>
          <Button type="primary" ghost v-if="showForceBtn" :loading="loading" :disabled="loading" @click="handleSubmitTicket(true)">
            {{ $t('qiang-zhi-ti-jiao') }}
          </Button>
        </div>
      </div>
    </div>
    <CCModal v-model="showNoPassedRuleModal" :title="$t('gui-ze-xiao-yan-shi-bai')" width="880">
      <Table :columns="noPassedRuleColumns" :data="noPassedRuleList" size="small" border>
        <template #warnLevel="{ row }">
          <Tag :color="row.ruleLevel === 'SUGGEST' ? 'warning' : 'error'">
            {{ RULE_WARN_LEVEL[row.ruleLevel] }}
          </Tag>
        </template>
        <template #lines="{ row }">
          <Poptip :content="formatRuleLocations(row)" trigger="hover" transfer>
            <span style="width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap">
              {{ formatRuleLocations(row) }}
            </span>
          </Poptip>
        </template>
      </Table>
      <template #footer>
        <Button type="primary" :loading="loading" :disabled="loading" @click="handleSubmitTicket(true)" v-if="showForceBtn">
          {{ $t('ji-xu-ti-jiao') }}
        </Button>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showNoPassedRuleModalWithLine" :title="$t('gui-ze-xiao-yan-shi-bai')" width="880">
      <Table :columns="noPassedRuleColumnsWithLine" :data="noPassedRuleList" size="small" border>
        <template #warnLevel="{ row }">
          <Tag :color="row.ruleLevel === 'SUGGEST' ? 'warning' : 'error'">
            {{ RULE_WARN_LEVEL[row.ruleLevel] }}
          </Tag>
        </template>
        <template #lines="{ row }">
          <Poptip :content="formatRuleLocations(row)" trigger="hover" transfer>
            <span style="width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap">
              {{ formatRuleLocations(row) }}
            </span>
          </Poptip>
        </template>
      </Table>
      <template #footer>
        <Button type="primary" :loading="loading" :disabled="loading" @click="handleSubmitTicket(true)" v-if="showForceBtn">
          {{ $t('ji-xu-ti-jiao') }}
        </Button>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <SqlFileUploadModal v-model="showSqlUploadModal" :loading="sqlUploading" :max-mega-byte="sqlFileMaxMegaByte" @confirm="uploadSqlFile" />
  </div>
</template>
<script lang="js">
import DsSelect from '@/views/ticket/components/DsSelect';
import TicketEditor from '@/components/editor/TicketEditor';
import SqlFileUploadModal from '@/components/function/SqlFileUploadModal.vue';
import { RULE_WARN_LEVEL } from '@/utils';
import { mapGetters, mapState } from 'vuex';

export default {
  name: 'Ticket',
  components: {
    TicketEditor,
    DsSelect,
    SqlFileUploadModal
  },
  computed: {
    ...mapState(['dmGlobalSetting']),
    ...mapGetters(['hasCatalogAndSchema']),
    sqlFileMaxMegaByte() {
      return this.dmGlobalSetting?.sqlFileMaxSize || 20;
    },
    showValidationResult() {
      const hasResults = this.noPassedRuleList && this.noPassedRuleList.length > 0;

      if (hasResults) {
        this.initializeHeights();
      }

      return hasResults;
    },
    hasError() {
      return this.noPassedRuleList.some((rule) => rule.ruleLevel !== 'SUGGEST');
    },
    previewMaxStartLine() {
      return Math.max(1, this.previewTotalLines - this.previewLineCount + 1);
    },
    noPassedRuleColumns() {
      const columns = [
        {
          title: this.$t('deng-ji'),
          slot: 'warnLevel',
          width: 90
        },
        {
          title: this.$t('ming-cheng'),
          key: 'name',
          width: 200
        },
        {
          title: this.$t('miao-shu'),
          key: 'desc'
        }
      ];

      const dsConfig = this.dmGlobalSetting.dsSettingDef[this.ticketData.dataSourceType];
      if (dsConfig && dsConfig.features && dsConfig.features.FUNC_LINES_SUPPORT === true) {
        columns.push({
          title: this.$t('hang-hao'),
          slot: 'lines',
          width: 80
        });
      }
      return columns;
    }
  },
  data() {
    return {
      showForceBtn: false,
      RULE_WARN_LEVEL,
      noPassedRuleList: [],
      showRawSql: true,
      showCheckedOnlyError: false,
      checkedSql: '',
      loading: false,
      contentType: 'INLINE',
      showSqlUploadModal: false,
      sqlUploading: false,
      sqlAttachment: null,
      previewStartLine: 1,
      previewTotalLines: 1,
      previewLineCount: 30,
      previewTimer: null,
      partialSqlNotice: false,
      currentMethod: '',
      allDsList: [],
      templateList: [],
      personList: [],
      selectedDs: {},
      instanceRequiredError: false,
      validationResultHeight: 150,
      isResizing: false,
      startY: 0,
      startHeight: 0,
      ticketData: {
        ticketEnable: true,
        showCatalogSelect: false,
        showSchemaSelect: false,
        instanceId: undefined,
        catalog: '',
        schema: '',
        approvalType: 'Internal',
        immediately: 'immediately',
        dataSourceType: 'MySQL',
        envId: '',
        approPersonUids: [],
        ticketTitle: ''
      },
      ticketRuleValidate: {
        ticketTitle: [
          {
            required: true,
            message: this.$t('biao-ti-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        description: [
          {
            required: true,
            message: this.$t('xu-qiu-miao-shu-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ]
      }
    };
  },
  mounted() {
    this.listAllDs();
    this.ticketData.ticketTitle = `${this.$t('gong-dan')}${new Date().getTime()}`;

    this.$nextTick(() => {
      this.initializeHeights();
    });
    window.addEventListener('resize', this.handleWindowResize);
  },
  beforeDestroy() {
    this.stopResize();
    clearTimeout(this.previewTimer);
    window.removeEventListener('resize', this.handleWindowResize);
  },
  watch: {
    noPassedRuleList: {
      handler() {
        this.$nextTick(() => {
          this.initializeHeights();
        });
      },
      deep: true
    }
  },
  methods: {
    openSqlUploadModal() {
      this.showSqlUploadModal = true;
    },
    async uploadSqlFile(files) {
      const file = files?.[0];
      if (!file || this.sqlUploading) {
        return;
      }

      this.sqlUploading = true;
      try {
        const data = new FormData();
        data.append('file', file);
        const res = await this.$services.dmTicketUploadSqlFile({
          data,
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        if (res.success) {
          this.sqlAttachment = res.data;
          this.contentType = 'ATTACHMENT';
          this.previewStartLine = 1;
          this.previewTotalLines = 1;
          this.partialSqlNotice = false;
          this.showSqlUploadModal = false;
          await this.$nextTick();
          await this.loadSqlPreview();
        }
      } finally {
        this.sqlUploading = false;
      }
    },
    async switchToInline() {
      if (this.contentType === 'INLINE') {
        return;
      }
      if (!this.sqlAttachment) {
        this.contentType = 'INLINE';
        return;
      }
      const res = await this.$services.dmTicketPreviewSqlFile({
        data: {
          attachmentId: this.sqlAttachment.attachmentId,
          startLine: 1,
          lineCount: 1000
        }
      });
      if (res.success) {
        this.contentType = 'INLINE';
        await this.$nextTick();
        this.$refs.rawSqlEditor?.setSql(res.data.content || '');
        this.partialSqlNotice = res.data.totalLines >= 1000;
      }
    },
    async switchToAttachment() {
      if (!this.sqlAttachment) {
        this.openSqlUploadModal();
        return;
      }
      this.contentType = 'ATTACHMENT';
      this.partialSqlNotice = false;
      await this.$nextTick();
      await this.loadSqlPreview();
    },
    async handleAttachmentModeAction() {
      if (this.contentType === 'ATTACHMENT' && this.sqlAttachment) {
        this.openSqlUploadModal();
        return;
      }
      await this.switchToAttachment();
    },
    schedulePreview() {
      clearTimeout(this.previewTimer);
      this.previewTimer = setTimeout(() => {
        this.loadSqlPreview();
      }, 120);
    },
    async loadSqlPreview() {
      if (this.contentType !== 'ATTACHMENT' || !this.sqlAttachment) {
        return;
      }
      this.previewLineCount = this.$refs.rawSqlEditor?.getVisibleLineCount() || 30;
      const res = await this.$services.dmTicketPreviewSqlFile({
        data: {
          attachmentId: this.sqlAttachment.attachmentId,
          startLine: this.previewStartLine,
          lineCount: this.previewLineCount
        }
      });
      if (res.success) {
        this.previewStartLine = res.data.startLine || 1;
        this.previewTotalLines = res.data.totalLines || 1;
        this.$refs.rawSqlEditor?.setSql(res.data.content || '', res.data.startLine || 1);
      }
    },
    handlePreviewWheel(event) {
      if (this.contentType !== 'ATTACHMENT' || !this.sqlAttachment) {
        return;
      }
      event.preventDefault();
      const step = Math.max(1, Math.floor(this.previewLineCount / 3));
      const delta = event.deltaY > 0 ? step : -step;
      this.previewStartLine = Math.max(1, Math.min(this.previewMaxStartLine, this.previewStartLine + delta));
      this.schedulePreview();
    },
    formatFileSize(size) {
      if (size < 1024) {
        return `${size} B`;
      }
      if (size < 1024 * 1024) {
        return `${(size / 1024).toFixed(1)} KB`;
      }
      return `${(size / 1024 / 1024).toFixed(1)} MB`;
    },
    startResize(e) {
      this.isResizing = true;
      this.startY = e.clientY;
      this.startHeight = this.validationResultHeight;

      document.addEventListener('mousemove', this.onResize);
      document.addEventListener('mouseup', this.stopResize);

      e.preventDefault();
    },

    initializeHeights() {
      this.$nextTick(() => {
        this.layoutEditors();
      });
    },

    layoutEditors() {
      const editors = [this.$refs.rawSqlEditor];
      editors.forEach((editor) => {
        editor?.monacoEditor?.layout();
      });
    },

    onResize(e) {
      if (!this.isResizing) return;

      const deltaY = this.startY - e.clientY;
      const newHeight = Math.max(100, Math.min(400, this.startHeight + deltaY));
      this.validationResultHeight = newHeight;
      this.layoutEditors();
    },

    stopResize() {
      this.isResizing = false;
      document.removeEventListener('mousemove', this.onResize);
      document.removeEventListener('mouseup', this.stopResize);
    },

    handleWindowResize() {
      this.layoutEditors();
    },

    handleCloseModal() {
      this.showForceBtn = false;
      this.noPassedRuleList = [];
    },
    handleCatalogChange(isRefreshCache = false) {
      const selectCatalog = this.selectedDs.CATALOG_LIST.find((catalog) => catalog.objName === this.ticketData.catalog);
      this.listLevels(selectCatalog.levels, isRefreshCache);
    },
    async listAllDs() {
      const res = await this.$services.dmTicketListDsInsLevels();
      if (res.success) {
        this.allDsList = res.data.map((ds) => ({
          ...ds,
          levels: [ds.objAttr.dsEnvId, ds.objId]
        }));
      }
    },
    async listLevels(levels, isRefreshCache = false) {
      const res = await this.$services.dmTicketListDbLevels({
        data: {
          levels,
          refreshCache: isRefreshCache
        }
      });

      if (res.success) {
        if (res.data && res.data.length) {
          if (res.data[0].objType === 'SCHEMA') {
            this.ticketData.showSchemaSelect = true;
            this.selectedDs[`${res.data[0].objType}_LIST`] = res.data.map((schema) => ({
              ...schema,
              levels: [...levels, schema.objName]
            }));
          }

          if (res.data[0].objType === 'CATALOG') {
            this.ticketData.schema = '';
            this.ticketData.showCatalogSelect = true;
            this.selectedDs[`${res.data[0].objType}_LIST`] = res.data.map((catalog) => ({
              ...catalog,
              levels: [...levels, catalog.objName]
            }));
          }
        }
      }

      if (res.code === '10201') {
        this.$Message.error(res.msg);
      }
    },
    async handleSubmitTicket(force = false) {
      if (this.loading) {
        return;
      }

      this.showCheckedOnlyError = false;
      if (!this.ticketData.instanceId) {
        this.instanceRequiredError = true;
        this.$Message.error(this.$t('qing-xuan-ze-shu-ju-yuan-shi-li'));
        return;
      }
      if (this.contentType === 'ATTACHMENT' && !this.sqlAttachment) {
        this.$Message.error(this.$t('ticket-sql-select-file'));
        return;
      }
      this.loading = true;

      try {
        await this.$refs.ticketContent.validate();
        const dbLevels = [this.ticketData.envId, this.ticketData.instanceId];

        if (this.ticketData.catalog && this.hasCatalogAndSchema(this.ticketData.dataSourceType)) {
          dbLevels.push(this.ticketData.catalog);
        }

        if (this.ticketData.schema) {
          dbLevels.push(this.ticketData.schema);
        }

        const data = {
          approvalType: this.ticketData.approvalType,
          dbLevels,
          contentType: this.contentType,
          attachmentId: this.contentType === 'ATTACHMENT' ? this.sqlAttachment.attachmentId : null,
          rawSql: this.contentType === 'INLINE' && this.$refs.rawSqlEditor ? this.$refs.rawSqlEditor?.getSql() : null,
          description: this.ticketData.description,
          ticketTitle: this.ticketData.ticketTitle,
          immediately: this.ticketData.immediately === 'immediately',
          templateIdentity: '',
          approTemplateName: '',
          force
        };

        const res = await this.$services.dmTicketCreate({ data });
        if (res.success) {
          if (res.data.failure || res.data.confirm) {
            if (res.data.confirm && !res.data.failure) {
              this.showForceBtn = true;
              if (force) {
                // Forced surrender, no obstruction rules.
                this.noPassedRuleList = [];
                this.showForceBtn = false;
                await this.$router.push({ path: `/ticket/${res.data.ticketId}` });
              }
            }
            this.noPassedRuleList = res.data.checkedVOS;
          } else {
            this.noPassedRuleList = [];
            this.showForceBtn = false;
            await this.$router.push({ path: `/ticket/${res.data.ticketId}` });
          }
        } else if (res.code === '20001') {
          this.$Modal.confirm({
            title: this.$t('gong-dan-yi-chang-ti-shi'),
            content: res.msg,
            okText: this.$t('qiang-zhi-ti-jiao'),
            cancelText: this.$t('qu-xiao'),
            onOk: () => {
              this.handleSubmitTicket(true);
            }
          });
        }
      } catch {
        return;
      } finally {
        this.loading = false;
      }
    },
    async listDsSchema() {
      const res = await this.$services.dmDataSourceSchemaListSchemas({
        data: {
          dataSourceId: this.ticketData.instanceId,
          parentData: this.ticketData.database,
          useVisibility: true
        }
      });
      if (res.success) {
        this.ticketData.schemaList = res.data;
        this.ticketData = { ...this.ticketData };
      }
    },
    async listSchemaFirstLevel() {
      const res = await this.$services.dmDataSourceSchemaListFirstLevel({
        data: {
          dataSourceId: this.ticketData.instanceId,
          useVisibility: true
        },
        page: 'ticket'
      });
      if (res.success) {
        this.ticketData.dbList = res.data.nameList;
        this.ticketData.hasNextLevel = res.data.hasNextLevel;
        this.ticketData = { ...this.ticketData };
      }
      this.currentMethod = 'listFirstLevel';
    },
    async handleChangeInstance(e) {
      this.instanceRequiredError = false;
      this.ticketData.database = null;
      this.ticketData.schema = null;
      this.ticketData.showCatalogSelect = false;
      this.ticketData.showSchemaSelect = false;
      this.allDsList.forEach((ds) => {
        if (ds.objId === this.ticketData.instanceId) {
          this.selectedDs = ds;
          this.ticketData.dataSourceType = ds.objAttr.dsType;
          this.ticketData.envId = ds.objAttr.dsEnvId;
        }
      });

      const res = await this.$services.dmEnvParamFetchTicketParamForSec({
        data: {
          envId: this.ticketData.envId,
          paramKey: 'ticket_info'
        },
        page: 'ticket'
      });
      if (res.success && res.data) {
        this.ticketData.ticketEnable = res.data.openTicket;
      } else {
        this.ticketData.ticketEnable = true;
      }
      if (this.ticketData.ticketEnable) {
        this.listLevels(this.selectedDs.levels);
      } else {
        this.ticketData.showCatalogSelect = false;
        this.ticketData.showSchemaSelect = false;
      }
    },
    restoreLevel(type, value) {
      let selectCatalog;
      if (type === 'schema') {
        selectCatalog = this.selectedDs.CATALOG_LIST?.find((catalog) => catalog.objName === this.ticketData.catalog);
      } else if (type === 'catalog') {
        selectCatalog = this.selectedDs.CATALOG_LIST?.find((catalog) => catalog.objName === value);
      }
      this.listLevels(selectCatalog?.levels || this.selectedDs.levels, true);
      this.$Message.success(this.$t('shua-xin-cheng-gong'));
      setTimeout(() => {
        if (type === 'schema') this.ticketData.schema = value;
        if (type === 'catalog') this.ticketData.catalog = value;
      }, 0);
    },
    restoreSchema(data) {
      this.restoreLevel('schema', data);
    },
    restoreCatalog(data) {
      this.restoreLevel('catalog', data);
    },
    checkRoleResultList() {
      if (!this.showCheckedOnlyError) {
        return this.noPassedRuleList;
      } else {
        return this.noPassedRuleList.filter((rule) => rule.ruleLevel !== 'SUGGEST');
      }
    },
    formatRuleLocations(rule) {
      const lines = rule.lines || [];
      const locations = lines.join(', ');
      if (rule.hitCount > lines.length) {
        return `${locations} ${this.$t('ticket-rule-location-total', { count: rule.hitCount })}`.trim();
      }
      return locations;
    }
  }
};
</script>
<style lang="less">
.ticket-create-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
  overflow: hidden;

  .create-content-container {
    flex: 1;
    display: flex;
    min-height: 0;
    overflow: hidden;
    padding: 16px 24px;
    gap: 24px;

    .create-ticket-editor {
      flex: 1;
      min-width: 0;
      min-height: 0;
      overflow: hidden;
      display: flex;
      flex-direction: column;

      .create-ticket-editor-toolbar {
        flex-shrink: 0;
        padding-bottom: 16px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 16px;

        .create-ticket-editor-operator {
          min-width: 0;
        }

        .sql-mode-actions {
          flex: none;
          display: flex;
          align-items: center;
          gap: 8px;
        }
      }

      .editor {
        flex: 1;
        min-height: 0;
        display: flex;
        flex-direction: column;

        .collapse {
          display: flex;
          flex-direction: column;
          min-height: 0;

          &.raw {
            flex: 1;
            min-height: 200px;
            overflow: hidden;
          }

          &.validation-result {
            flex: none;
            min-height: 100px;
            max-height: 400px;
            border-radius: 6px;
            border: 1px solid #eee;
            overflow: hidden;

            .title {
              padding: 10px 16px;
              color: #d4380d;
              font-size: 14px;
              font-weight: 500;
              background: #fff7f5;
              border-bottom: 1px solid #ffebeb;
            }

            .content {
              padding: 12px 16px;
              overflow-y: auto;
              height: calc(100% - 41px);

              .validation-content {
                .rule-item {
                  padding: 0;

                  &:not(:last-child) {
                    border-bottom: 1px solid #f5f5f5;
                    margin-bottom: 10px;
                    padding-bottom: 10px;
                  }

                  .rule-header {
                    display: flex;
                    align-items: center;
                    margin-bottom: 4px;

                    .rule-level {
                      margin-right: 8px;
                      font-weight: 500;
                    }

                    .rule-name {
                      font-weight: 600;
                      color: #262626;
                    }

                    .rule-lines {
                      display: flex;
                      align-items: center;
                      font-size: 12px;
                      padding-left: 10px;

                      .lines-label {
                        color: #8c8c8c;
                        margin-right: 4px;
                      }

                      .lines-content {
                        color: #595959;
                        background: #f5f5f5;
                        padding: 2px 6px;
                        margin-right: 5px;
                        border-radius: 3px;
                        font-family: monospace;
                      }
                    }
                  }

                  .rule-desc {
                    color: #595959;
                    line-height: 1.5;
                  }
                }
              }
            }
          }

          .title {
            position: relative;
            height: 36px;
            display: flex;
            align-items: center;
            margin-bottom: 8px;
            padding-left: 12px;
            font-size: 14px;
            font-weight: 500;
            color: #181d26;

            &::before {
              content: '';
              position: absolute;
              top: 50%;
              left: 0;
              width: 3px;
              height: 14px;
              border-radius: 2px;
              background: #18b566;
              transform: translateY(-50%);
            }
          }

          .sql-file-meta {
            flex: none;
            display: flex;
            min-width: 0;
            min-height: 36px;
            align-items: center;
            gap: 12px;
            padding: 0 12px;
            margin-bottom: 8px;
            background: var(--bg-secondary, #f8fafc);
            color: var(--text-secondary, #707070);
            font-size: 13px;
            font-weight: 400;

            .sql-file-name {
              max-width: 320px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }

          .partial-sql-notice {
            flex: none;
            min-height: 36px;
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 8px 12px;
            margin-bottom: 8px;
            background: var(--bg-secondary, #f8fafc);
            color: var(--text-primary, #333840);
            font-size: 13px;

            button {
              margin-left: auto;
              border: 0;
              background: transparent;
              cursor: pointer;
              color: var(--text-secondary, #41454d);
            }
          }

          .content {
            flex: 1;
            min-height: 0;
            border: 1px solid #eaeaea;
            border-radius: 6px;
            overflow: hidden;
          }

          .sql-editor-content {
            position: relative;

            .virtual-scrollbar {
              position: absolute;
              z-index: 3;
              top: 8px;
              right: 4px;
              width: 14px;
              height: calc(100% - 16px);
              margin: 0;
              writing-mode: vertical-lr;
              direction: ltr;
              appearance: none;
              background: transparent;
              cursor: pointer;

              &::-webkit-slider-runnable-track {
                width: 10px;
                height: 100%;
                background: transparent;
              }

              &::-webkit-slider-thumb {
                width: 10px;
                height: 20px;
                border: 0;
                border-radius: 0;
                appearance: none;
                background: rgba(100, 100, 100, 0.45);
              }

              &::-moz-range-track {
                width: 10px;
                background: transparent;
              }

              &::-moz-range-thumb {
                width: 10px;
                height: 20px;
                border: 0;
                border-radius: 0;
                background: rgba(100, 100, 100, 0.45);
              }

              &:hover::-webkit-slider-thumb {
                background: rgba(100, 100, 100, 0.7);
              }

              &:hover::-moz-range-thumb {
                background: rgba(100, 100, 100, 0.7);
              }
            }
          }
        }

        .resize-handle {
          height: 6px;
          background: #eee;
          cursor: ns-resize;
          position: relative;
          border-radius: 3px;
          margin: 2px 0;

          &:hover {
            background: #ddd;
          }

          &:active {
            background: #ccc;
          }

          &::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 20px;
            height: 2px;
            background: #999;
            border-radius: 1px;
          }
        }
      }
    }

    .create-ticket-content {
      width: 320px;
      flex-shrink: 0;
      background: #f8fafc;
      padding: 24px;
      border-radius: 10px;
      overflow-y: auto;

      .ticket-template-container {
        .ticket-from {
          display: inline-block;
        }

        .ivu-select {
          display: inline-block;
        }
      }

      .create-ticket-form-btn {
        margin-top: 24px;

        button {
          margin-right: 12px;
        }
      }
    }
  }
}
</style>
