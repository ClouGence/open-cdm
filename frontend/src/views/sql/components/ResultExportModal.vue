<template>
  <div>
    <CCModal v-model="showExportOptionModal" :title="exportModalTitle || $t('dao-chu')" :width="600" :mask-closable="false" transfer>
      <div class="export-option-modal">
        <div class="export-options-header">
          <div class="export-option-row">
            <span class="export-option-label">{{ exportRangeLabel }}</span>
            <a-radio-group v-model:value="exportRangeType" @change="handleExportRangeChange">
              <a-radio value="single" :disabled="!allowSingleRow">{{ $t('dan-hang-dao-chu') }}</a-radio>
              <a-radio value="page">{{ $t('dan-ye-dao-chu') }}</a-radio>
              <a-radio v-if="showAllOption" value="all">{{ $t('quan-bu-dao-chu') }}</a-radio>
            </a-radio-group>
          </div>
          <div class="export-option-row">
            <span class="export-option-label">{{ exportFormatLabel }}</span>
            <a-radio-group v-model:value="currentExportType">
              <a-radio v-for="item in exportTypes" :key="item.name" :value="item.name">
                <div class="export-format-item">
                  <CustomIcon v-if="item?.icon" :type="item.icon" size="14px" />
                  <span>{{ item.description }}</span>
                </div>
              </a-radio>
            </a-radio-group>
          </div>
        </div>
        <div class="toolbar">
          <a-input v-model:value="columnSearch" :placeholder="$t('zi-duan-ming')" allow-clear size="small" />
        </div>
        <a-table
          :columns="exportTableColumns"
          :dataSource="filteredColumns"
          :pagination="false"
          :scroll="{ y: 420 }"
          size="small"
          :rowKey="(record, index) => index"
          class="export-table"
        >
          <template #headerCell="{ column }">
            <template v-if="column.key === 'export'">
              <a-checkbox v-model:checked="columnSelectAll" @change="toggleSelectAll" />
            </template>
          </template>
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'export'">
              <div>
                <a-checkbox v-model:checked="record.export" @change="syncSelectAllState" />
              </div>
            </template>
            <template v-if="column.key === 'originalColumnName'">
              <div class="export-table-cell">
                <span>{{ record.originalColumnName }}</span>
              </div>
            </template>
            <template v-if="column.key === 'columnName'">
              <div class="export-table-cell">
                <a-input
                  v-model:value="record.columnName"
                  size="small"
                  :placeholder="$t('qing-shu-ru-zi-duan-ming')"
                  @blur="finishEditField(record)"
                  @keyup.enter="finishEditField(record)"
                  @keyup.escape="cancelEditField(record)"
                />
              </div>
            </template>
            <template v-if="column.key === 'modified'">
              <div class="export-table-cell">
                <span v-if="record.columnName !== record.originalColumnName" class="modified-indicator">{{ $t('yi-xiu-gai') }}</span>
              </div>
            </template>
          </template>
        </a-table>
      </div>
      <template #footer>
        <Button @click="closeExportModal">{{ $t('guan-bi') }}</Button>
        <Button type="primary" :loading="confirmLoading" @click="handleConfirmExport">{{ $t('que-ren-dao-chu') }}</Button>
      </template>
    </CCModal>

    <CCModal
      v-model="downloadFileNameModalVisible"
      :title="$t('xia-zai-wen-jian')"
      :width="480"
      :mask-closable="false"
      transfer
      @on-cancel="closeDownloadFileNameModal"
    >
      <a-form layout="vertical">
        <a-form-item :label="$t('wen-jian-ming')" :validate-status="downloadFileNameError ? 'error' : ''" :help="downloadFileNameError">
          <a-input
            v-model:value="downloadFileName"
            :addon-after="downloadFileSuffix"
            :placeholder="$t('qing-shu-ru')"
            autofocus
            @input="downloadFileNameError = ''"
            @keyup.enter="confirmDownloadExportedFile"
          />
        </a-form-item>
      </a-form>
      <template #footer>
        <Button :disabled="downloadLoading" @click="closeDownloadFileNameModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="downloadLoading" @click="confirmDownloadExportedFile">{{ $t('xia-zai') }}</Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import appLogger from '@/utils/logger';
import CCModal from '@/components/ui/CCModal.vue';
import CustomIcon from '@/components/function/CustomIcon.vue';

export default {
  name: 'ResultExportModal',
  components: {
    CCModal,
    CustomIcon
  },
  props: {
    confirmLoading: {
      type: Boolean,
      default: false
    }
  },
  emits: ['confirm'],
  data() {
    return {
      showExportOptionModal: false,
      exportModalTitle: '',
      allowSingleRow: false,
      showAllOption: true,
      exportRangeType: 'all',
      currentExportType: '',
      columnSearch: '',
      columnSelectAll: true,
      insertOption: {
        tableName: 'table_name',
        columns: [],
        dataSourceType: '',
        mergeInsert: false,
        valueSize: 50,
        offset: 0,
        limit: -1
      },
      downloadFileNameModalVisible: false,
      downloadFileName: '',
      downloadFileSuffix: '',
      downloadFileNameError: '',
      downloadTrackId: '',
      downloadLoading: false
    };
  },
  computed: {
    ...mapState(['dmGlobalSetting']),
    exportTypes() {
      return this.dmGlobalSetting?.fmtConvertDef || [];
    },
    exportRangeLabel() {
      return `${this.$t('dao-chu-fan-wei')}:`;
    },
    exportFormatLabel() {
      return `${this.$t('dao-chu-ge-shi')}:`;
    },
    filteredColumns() {
      const keyword = (this.columnSearch || '').toLowerCase();
      return (this.insertOption.columns || []).filter((c) => !keyword || (c.columnName || '').toLowerCase().includes(keyword));
    },
    exportTableColumns() {
      return [
        {
          title: '',
          key: 'export',
          width: 60,
          align: 'center'
        },
        {
          title: this.$t('yuan-shi-zi-duan') !== 'yuan-shi-zi-duan' ? this.$t('yuan-shi-zi-duan') : '原始字段',
          key: 'originalColumnName',
          width: 180
        },
        {
          title: this.$t('xiu-gai-hou-de-zi-duan-ming'),
          key: 'columnName'
        },
        {
          title: '',
          key: 'modified',
          width: 80
        }
      ];
    }
  },
  methods: {
    openExportModal({
      columns = [],
      tableName = 'table_name',
      dataSourceType = '',
      allowSingleRow = false,
      showAllOption = true,
      defaultRange = 'all',
      title = ''
    } = {}) {
      this.allowSingleRow = Boolean(allowSingleRow);
      this.showAllOption = showAllOption !== false;
      const fallbackRange = this.showAllOption ? 'all' : 'page';
      let nextRange = defaultRange || fallbackRange;
      if (nextRange === 'single' && !this.allowSingleRow) {
        nextRange = fallbackRange;
      } else if (nextRange === 'all' && !this.showAllOption) {
        nextRange = 'page';
      }
      this.exportRangeType = nextRange;
      const jsonType = this.exportTypes.find((t) => t.name === 'application/json' || t.name === 'json');
      this.currentExportType = jsonType?.name || (this.exportTypes.length > 0 ? this.exportTypes[0].name : '');
      this.exportModalTitle = title || this.$t('dao-chu');
      this.insertOption = {
        tableName: tableName || 'table_name',
        columns: columns.map((name) => ({
          columnName: name,
          originalColumnName: name,
          export: true,
          isEditing: false
        })),
        dataSourceType: dataSourceType || '',
        mergeInsert: false,
        valueSize: 50,
        offset: 0,
        limit: -1
      };
      this.columnSearch = '';
      this.columnSelectAll = true;
      this.showExportOptionModal = true;
    },
    closeExportModal() {
      this.showExportOptionModal = false;
    },
    handleExportRangeChange() {
      if (!this.allowSingleRow && this.exportRangeType === 'single') {
        this.exportRangeType = this.showAllOption ? 'all' : 'page';
      } else if (!this.showAllOption && this.exportRangeType === 'all') {
        this.exportRangeType = 'page';
      }
    },
    toggleSelectAll() {
      const checked = this.columnSelectAll;
      (this.insertOption.columns || []).forEach((c) => {
        c.export = checked;
      });
    },
    syncSelectAllState() {
      const cols = this.insertOption.columns || [];
      this.columnSelectAll = cols.length > 0 && cols.every((c) => c.export);
    },
    finishEditField(col) {
      col.isEditing = false;
      if (!col.columnName || !col.columnName.trim()) {
        col.columnName = col.originalColumnName;
      }
    },
    cancelEditField(col) {
      col.isEditing = false;
      col.columnName = col.originalColumnName;
    },
    handleConfirmExport() {
      const hasSelected = (this.insertOption.columns || []).some((c) => c.export);
      if (!hasSelected) {
        this.$Message.warning(
          this.$t('qing-xuan-ze-dao-chu-zi-duan') !== 'qing-xuan-ze-dao-chu-zi-duan'
            ? this.$t('qing-xuan-ze-dao-chu-zi-duan')
            : '请至少选择一个导出字段'
        );
        return;
      }
      this.$emit('confirm', {
        dstFormatName: this.currentExportType,
        exportRangeType: this.exportRangeType,
        option: {
          ...this.insertOption,
          columns: (this.insertOption.columns || []).map((c) => ({ ...c }))
        }
      });
    },
    openDownloadModal(downloadFile) {
      if (!downloadFile?.trackId) {
        return;
      }
      let fileName = downloadFile.trackId;
      if (downloadFile.file) {
        try {
          const decodedFilePath = decodeURIComponent(downloadFile.file.replace(/\+/g, '%20'));
          fileName = decodedFilePath.split(/[\\/]/).pop() || fileName;
        } catch (error) {
          appLogger.debug(error);
        }
      }

      const suffixIndex = fileName.lastIndexOf('.');
      this.downloadFileName = fileName;
      this.downloadFileSuffix = '';
      if (suffixIndex > 0) {
        this.downloadFileName = fileName.substring(0, suffixIndex);
        this.downloadFileSuffix = fileName.substring(suffixIndex);
      }
      this.downloadTrackId = downloadFile.trackId;
      this.downloadFileNameError = '';
      this.downloadFileNameModalVisible = true;
    },
    closeDownloadFileNameModal() {
      if (this.downloadLoading) {
        return;
      }
      this.downloadFileNameModalVisible = false;
      this.downloadFileNameError = '';
      this.downloadTrackId = '';
    },
    async confirmDownloadExportedFile() {
      if (this.downloadLoading) {
        return;
      }

      const fileName = this.downloadFileName.trim();
      if (!fileName) {
        this.downloadFileNameError = this.$t('wen-jian-ming-bu-neng-wei-kong');
        return;
      }
      const hasControlCharacter = Array.from(fileName).some((character) => character.charCodeAt(0) < 32);
      if (fileName === '.' || fileName === '..' || /[<>:"/\\|?*]/.test(fileName) || hasControlCharacter) {
        this.downloadFileNameError = this.$t('wen-jian-ming-bao-han-fei-fa-zi-fu');
        return;
      }

      this.downloadLoading = true;
      try {
        const downloadFileName = `${fileName}${this.downloadFileSuffix}`;
        const res = await this.$services.dmQueryDownloadResult({
          data: {
            resultId: this.downloadTrackId,
            downloadFileName
          },
          responseType: 'blob',
          modal: false
        });

        const blob = res && res.data instanceof Blob ? res.data : res;
        if (!(blob instanceof Blob)) {
          this.$Message.warning(this.$t('wen-jian-yi-shi-xiao'));
          throw new Error('响应数据格式不正确');
        }

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = downloadFileName;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
        this.downloadFileNameModalVisible = false;
        this.downloadTrackId = '';
      } catch (error) {
        appLogger.error('err:', error);
      } finally {
        this.downloadLoading = false;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.export-option-modal {
  .export-options-header {
    margin-bottom: 16px;
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .export-option-row {
    display: flex;
    align-items: center;
  }

  .export-option-label {
    width: 80px;
    flex-shrink: 0;
  }

  .export-format-item {
    display: inline-flex;
    align-items: center;
    gap: 6px;
  }

  .toolbar {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    margin-bottom: 8px;
    gap: 8px;
  }

  .export-table {
    :deep(.ant-table) {
      font-size: 12px;
    }

    :deep(.ant-table-thead > tr > th) {
      padding-top: 8px;
      padding-bottom: 8px;
    }

    :deep(.ant-table-tbody > tr > td) {
      padding-top: 4px;
      padding-bottom: 4px;
    }

    .export-table-cell {
      padding: 3px 6px !important;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
    }

    .modified-indicator {
      color: #ff8c00;
      font-size: 12px;
      font-weight: 500;
    }

    :deep(.ant-input) {
      font-size: 12px;
    }
  }
}
</style>
