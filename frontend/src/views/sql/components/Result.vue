<template>
  <div style="height: 100%" class="result-container">
    <div style="height: 100%; display: flex; flex-direction: column">
      <div class="tab-group">
        <a-tabs
          v-model:activeKey="tab.result.active"
          :style="`width: ${tab.result?.list?.length ? '110px' : '100%'}`"
          type="card"
          @tabClick="handleResultTabChange"
        >
          <a-tab-pane name="message" key="message">
            <template #tab>
              <div @contextmenu.prevent.stop="onContextmenu($event, { resultId: 'message' })">
                <CustomIcon type="InfoColorful" />
                <span class="ml-[5px] mr-[5px]">{{ $t('zhi-hang-xin-xi') }}</span>
              </div>
            </template>
          </a-tab-pane>
        </a-tabs>
        <a-tabs v-model:activeKey="tab.result.active" class="right" type="card" @tabClick="handleResultTabChange" v-if="tab.result?.list?.length">
          <a-tab-pane v-for="res in tab.result.list" :key="res.resultId" :name="res.resultId">
            <template #tab>
              <div @contextmenu.prevent.stop="onContextmenu($event, res)">
                <CustomIcon type="SuccessColorful" />
                <span class="ml-[5px] mr-[5px]">{{ `${this.$t('jie-guo')}${res.showIndex}` }}</span>
                <CustomIcon
                  class="close-icon"
                  type="icon-v2-close2"
                  hoverStyle
                  customStyle="radius-hover"
                  @click.native="handleCloseResultTab('current', res.resultId)"
                />
              </div>
            </template>
          </a-tab-pane>
          <template #rightExtra>
            <a-dropdown trigger="click" placement="bottomRight" v-if="tab.result.list.length" overlayClassName="result-tab-dropdown">
              <CustomIcon type="icon-v2-ArrowDown" hoverStyle customStyle="icon-v2-hover" style="margin: 0 4px" />
              <template #overlay>
                <a-menu :selectedKeys="[tab.result.active]">
                  <a-menu-item v-for="res in tab.result.list" :key="res.resultId" :name="res.resultId" @click="handleResultTabChange(res.resultId)">
                    <div class="dropdown-item">
                      <CustomIcon type="icon-v2-Table" />
                      <div style="margin-left: 5px; white-space: nowrap">{{ `${$t('jie-guo')}${res.showIndex}` }}</div>
                      <div class="dropdown-item-close">
                        <CustomIcon type="icon-v2-close2" customStyle="icon-v2-hover" @click.native="handleCloseResultTab('current', res.resultId)" />
                      </div>
                    </div>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </template>
        </a-tabs>
      </div>
      <div id="result-info-container" class="result-info-container" style="height: 100%" v-if="tab.result.active === 'message'">
        <div class="result-info-messages">
          <div v-for="(info, index) in tab.executeInfo" :key="index" class="result-info">
            <div class="info" v-if="info.resultType === 'QueryScript'">
              <div class="level">{{ info.line }}</div>
              <div class="message">
                {{ info.script }}
              </div>
            </div>
            <div class="info" v-else>
              <div class="time">[{{ info.time }}]</div>
              <div :class="`message ${info.level}`">
                {{ info.message }}
                <a href="#" v-if="info.message === $t('gui-ze-xiao-yan-shi-bai')" @click="handleViewNoPassedRuleList(index)">
                  {{ $t('cha-kan-xiang-qing') }}
                </a>
              </div>
            </div>
          </div>
        </div>
        <div class="result-info-buttons">
          <div class="btn-group">
            <div class="btn-group-item" @click="handleScrollUpMessage">
              <CustomIcon type="icon-v2-scroll_up" size="18px" />
            </div>
            <div class="btn-group-item" @click="handleScrollDownMessage">
              <CustomIcon type="icon-v2-scroll_down" size="18px" :custom-style="tab.executeInfoScrollDown ? 'btn-group-item-hover' : ''" />
            </div>
            <div class="btn-group-item" @click="handleClearMessage">
              <CustomIcon type="icon-v2-Delete2" size="18px" />
            </div>
          </div>
        </div>
      </div>
      <div
        v-if="!['message', 'async'].includes(tab.result.active)"
        class="result-content-wrapper"
        style="display: flex; flex-direction: column; flex: 1; min-height: 0"
      >
        <div class="tip-footer">
          <div v-if="selectedTab.receiveMode !== 'STREAM'" style="display: flex; align-items: center; gap: 8px">
            <div v-if="selectedTab.receiveMode === 'PAGINATED' && paginatedLoading[selectedTab.resultId]" class="paginated-loading">
              <div class="loading-spinner"></div>
            </div>
            <Page
              :model-value="selectedTab.page"
              :page-size="selectedTab.receiveMode === 'PAGINATED' ? 30 : 50"
              :total="selectedTab.receiveMode === 'PAGINATED' ? selectedTab.fetchCount || selectedTab.total : selectedTab.total"
              placement="top"
              show-total
              size="small"
              @on-change="changePage($event)"
            ></Page>
          </div>
          <div v-else class="stream-info" style="line-height: 30px; padding: 0 10px">
            <span>{{ $t('liu-shi-mo-shi-xian-shi-zui-xin-tiao-zong-ji-tiao', [selectedTab.fetchCount || selectedTab.total || 0]) }}</span>
          </div>
          <Poptip word-wrap trigger="hover" transfer placement="bottom">
            <template #content>
              <div v-if="selectedTab.rewriteTags?.length">
                {{ $t('zhong-xie-mo-kuai') }}
                <a-tag v-for="(tag, index) in selectedTab.rewriteTags" :key="index" color="blue">{{ tag }}</a-tag>
              </div>
              <div>
                {{ $t('yuan-shi-yu-ju') }}
                <span class="font-bold">{{ selectedTab.rewriteTags?.length ? selectedTab.original : selectedTab.querySql }}</span>
              </div>
            </template>
            <span style="padding-right: 8px; float: left; max-width: 400px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap">
              <a-tag v-if="selectedTab.queryType === 'plan'" color="green">{{ $t('ji-hua') }}</a-tag>
              <a-tag v-if="selectedTab.rewriteTags?.length > 0" color="blue">{{ $t('zhong-xie') }}</a-tag>
              {{ this.selectedTab.querySql }}
            </span>
          </Poptip>
          <a-popover v-if="tab.cost && tab.cost.popIndex > -1 && selectedTab && selectedTab.resultId" class="cost-pop">
            <template #content>
              <div v-for="costPop in tab.cost.popList" :key="costPop.text">
                <a-icon :type="costPop.icon" :style="`color: ${costPop.color}`" :theme="costPop.theme" />
                {{ costPop.text }}
              </div>
            </template>
            <div style="cursor: pointer" @click="handleClickCostPop">
              <a-icon
                :type="tab.cost.popList[tab.cost.popIndex].icon"
                :style="`color: ${tab.cost.popList[tab.cost.popIndex].color}`"
                :theme="tab.cost.popList[tab.cost.popIndex].theme"
              />
              {{ tab.cost.popList[tab.cost.popIndex].text }}
            </div>
          </a-popover>
          <div class="tip-footer-right" style="margin-left: auto">
            <div style="display: flex; align-items: center; gap: 4px">
              <Poptip
                v-if="selectedTab.exportState?.errorStatus === 'FAILED' && selectedTab.exportState?.errorMessage"
                :content="selectedTab.exportState.errorMessage"
                trigger="hover"
                placement="top-end"
                word-wrap
                transfer
              >
                <CustomIcon type="icon-v2-ErrorColorful" size="16px" color="#ff4d4f" style="cursor: pointer" />
              </Poptip>
              <div
                v-if="!selectedTab.exportState?.exporting"
                style="display: flex; align-items: center; gap: 4px; cursor: pointer"
                @click="handleResultExport"
              >
                <CustomIcon type="icon-v2-daochu" hoverStyle />
                <span>{{ $t('dao-chu') }}</span>
              </div>
            </div>
            <div class="download-warp">
              <a v-if="selectedTab.exportState?.percent === 100" @click.prevent="resetTabExportState">{{ $t('fan-hui') }}</a>
              <a v-if="selectedTab.exportState?.percent === 100" @click.prevent="downloadExportedFile" class="download-btn">
                {{ $t('xia-zai') }}
              </a>
              <div v-if="selectedTab.exportState?.exporting" class="export-progress-modal">
                <a-progress :percent="selectedTab.exportState?.percent || 0" size="small" style="width: 100px" />
              </div>
            </div>
          </div>
        </div>
        <div class="result-table-container" v-if="selectedTab">
          <a-table
            class="result-set-style"
            :ref="`result_table_${tab.result.active}`"
            :columns="antdColumns"
            :dataSource="selectedTab.showData"
            :pagination="false"
            size="small"
            bordered
            :rowKey="(record, index) => index"
            :customRow="handleCustomRow"
          >
            <template #headerCell="{ column }">
              <div class="header-cell-content">
                <a-tooltip :title="column.originalTitle || column.title" placement="top">
                  <span class="header-title">{{ column.originalTitle || column.title }}</span>
                </a-tooltip>
                <div v-if="column.resizable" class="resize-handle" @mousedown.stop="handleResize($event, column)" @click.stop></div>
              </div>
            </template>
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.dataIndex !== 'seq'">
                <div class="vxe-input-tpl" @dblclick.stop="handleCellDetail(record, column, index)">
                  <span v-if="record[column.dataIndex] === null" style="color: #ccc; font-style: italic">NULL</span>
                  <pre v-else style="overflow: hidden; margin: 0" v-html="record[column.dataIndex]"></pre>
                  <div v-if="!getCellComplete(column, index)" class="cell-incomplete-badge"></div>
                  <div class="op">
                    <div @click.stop="handleCellCopy(record[column.dataIndex])" style="margin-right: 3px">
                      <cc-iconfont name="copy" :size="12" />
                    </div>
                    <div @click.stop="handleCellDetail(record, column, index)">
                      <cc-iconfont name="eye" :size="12" />
                    </div>
                  </div>
                </div>
              </template>
              <template v-else>
                <span>{{ getRowNumber(index) }}</span>
              </template>
            </template>
          </a-table>
        </div>
      </div>
    </div>
    <CCModal v-model="showInsertSqlModal" @on-cancel="hideShowInsertSqlModal" title="Insert SQL" :width="1000" :mask-closable="false" transfer>
      <div class="insert-sql-modal">
        <div class="insert-sql-operation">
          <Input v-model="currentTableName" :placeholder="$t('qing-shu-ru-biao-ming')" style="margin-right: 10px" />
          <Button @click="generateInsertSql">{{ $t('sheng-cheng-insert-yu-ju') }}</Button>
        </div>
        <div class="insert-sql-content">
          <div v-for="(sql, index) in sqls" :key="index">{{ sql }}</div>
        </div>
      </div>
      <template #footer>
        <Button type="primary" ghost @click="copyText(sqls.join('\n'))">{{ $t('fu-zhi') }}</Button>
        <Button @click="hideShowInsertSqlModal">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <!-- SQL导出弹窗 -->
    <CCModal v-model="showSqlExportOptionModal" :title="$t('dao-chu') + ' SQL'" :width="860" :mask-closable="false" transfer>
      <div class="sql-export-modern">
        <div class="left">
          <a-form layout="vertical">
            <a-form-item :label="$t('biao-ming')">
              <a-input v-model:value="insertOption.tableName" :placeholder="$t('qing-shu-ru-biao-ming')" allow-clear />
            </a-form-item>
            <a-form-item :label="$t('mu-biao-shu-ju-ku-lei-xing')">
              <a-select
                v-model:value="insertOption.dataSourceType"
                :options="dsTypeOptions.map((d) => ({ label: d, value: d }))"
                allow-clear
                show-search
                :filter-option="(input, option) => option.label.toLowerCase().includes(input.toLowerCase())"
                :placeholder="$t('qing-xuan-ze-shu-ju-ku-lei-xing')"
              />
            </a-form-item>
            <a-form-item v-if="!['Oracle', 'ObForOracle'].includes(insertOption.dataSourceType)">
              <a-checkbox v-model:checked="insertOption.mergeInsert">{{ $t('he-bing-wei-yi-ge-insert-yu-ju') }}</a-checkbox>
            </a-form-item>
            <a-form-item
              v-if="!['Oracle', 'ObForOracle'].includes(insertOption.dataSourceType) && insertOption.mergeInsert"
              :label="$t('mei-ge-insert-de-values-shu-liang')"
            >
              <a-input v-model:value="insertOption.valueSize" :min="1" :max="100000" :placeholder="$t('qing-shu-ru')" style="width: 100%" />
            </a-form-item>
          </a-form>
        </div>
        <div class="right">
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
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.key === 'export'">
                <div>
                  <a-checkbox v-model:checked="record.export" />
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
                    @blur="finishEditField(record, index)"
                    @keyup.enter="finishEditField(record, index)"
                    @keyup.escape="cancelEditField(record, index)"
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
      </div>
      <template #footer>
        <Button @click="showSqlExportOptionModal = false">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="confirmSqlExportOption">{{ $t('que-ren-dao-chu') }}</Button>
      </template>
    </CCModal>

    <!-- 通用导出弹窗（仅字段选择） -->
    <CCModal v-model="showExportOptionModal" :title="exportModalTitle || $t('dao-chu')" :width="600" :mask-closable="false" transfer>
      <div class="export-option-modal">
        <div class="export-options-header" style="margin-bottom: 16px; display: flex; flex-direction: column; gap: 12px">
          <div style="display: flex; align-items: center">
            <span style="width: 80px">{{ $t('dao-chu-fan-wei') }}:</span>
            <a-radio-group v-model:value="exportRangeType" @change="handleExportRangeChange">
              <a-radio value="single" :disabled="!isFromContextMenu">{{ $t('dan-hang-dao-chu') }}</a-radio>
              <a-radio value="page">{{ $t('dan-ye-dao-chu') }}</a-radio>
              <a-radio value="all">{{ $t('quan-bu-dao-chu') }}</a-radio>
            </a-radio-group>
          </div>
          <div style="display: flex; align-items: center">
            <span style="width: 80px">{{ $t('dao-chu-ge-shi') }}:</span>
            <a-radio-group v-model:value="currentExportType">
              <a-radio v-for="item in exportTypes" :key="item.name" :value="item.name">
                <div style="display: inline-flex; align-items: center; gap: 6px">
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
          <template #bodyCell="{ column, record, index }">
            <template v-if="column.key === 'export'">
              <div>
                <a-checkbox v-model:checked="record.export" />
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
                  @blur="finishEditField(record, index)"
                  @keyup.enter="finishEditField(record, index)"
                  @keyup.escape="cancelEditField(record, index)"
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
        <Button @click="showExportOptionModal = false">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="confirmExportOption">{{ $t('que-ren-dao-chu') }}</Button>
      </template>
    </CCModal>
  </div>
</template>
<script lang="jsx">
import dayjs from 'dayjs';
import { Modal, Tooltip } from 'ant-design-vue';
import { mysqlInsert, pgInsert } from '@/views/sql/components/typeGroup';
import copyMixin from '@/mixins/copyMixin';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import { mapGetters, mapState } from 'vuex';
import CustomIcon from '@/components/function/CustomIcon.vue';
import ContextMenu from '@imengyu/vue3-context-menu';
import XEClipboard from 'xe-clipboard';

export default {
  name: 'Result',
  mixins: [copyMixin],
  props: {
    tab: Object
  },
  components: {
    CustomIcon
    // AsyncJobDetail,
    // AsyncJobList
  },
  data() {
    return {
      exportTypes: [],
      contextData: null,
      actionType: '',
      currentTableName: '',
      columnsList: [],
      searchText: '',
      sqls: [],
      showInsertSqlModal: false,
      showSqlExportOptionModal: false,
      showExportOptionModal: false,
      exportModalTitle: '',
      currentExportType: '', // 导出格式
      exportRangeType: 'all', // 导出范围：'single' 单行, 'page' 单页, 'all' 全部
      selectedRowIndex: null, // 选中的行索引（用于单行导出）
      isFromContextMenu: false, // 是否从右键菜单进入
      insertOption: {
        tableName: '',
        columns: [],
        dataSourceType: '',
        mergeInsert: false,
        valueSize: 100,
        offset: 0,
        limit: 1000000
      },
      dsTypeOptions: [],
      columnSearch: '',
      columnSelectAll: true,
      rowIndex: 0,
      pageHeight: 0,
      tableMenu: {
        body: {
          options: [
            [
              {
                code: 'exportRowInsert',
                name: this.$t('dao-chu-dang-qian-hang-insert')
              }
              // {
              //   code: 'exportCellInsert',
              //   name: '导出单元格insert'
              // }
            ]
          ]
        }
      },
      toolbarConfig: {
        custom: true,
        slots: {
          buttons: 'toolbar_buttons'
        }
      },
      exportConfig: {},
      editorHeight: 250,
      paginatedLoading: {}, // 存储每个结果集的loading状态 {resultId: boolean}
      paginatedLoadingTimer: null, // loading 定时器
      columnWidths: {} // 存储列宽度
    };
  },
  computed: {
    ...mapState(['dmGlobalSetting']),
    ...mapGetters(['getQualifier']),
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
          title: '原始字段',
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
    },
    selectedTab() {
      if (!['message'].includes(this.tab.result.active)) {
        let tab = {};
        for (let i = 0; i < this.tab.result.list.length; i++) {
          if (this.tab.result.list[i].resultId === this.tab.result.active) {
            tab = this.tab.result.list[i];
            break;
          }
        }
        return tab;
      } else {
        return {};
      }
    },
    antdColumns() {
      if (!this.selectedTab || !this.selectedTab.columnListSeq) {
        return [];
      }
      return this.selectedTab.columnListSeq.map((col, index) => {
        const colKey = col.type === 'seq' ? 'seq' : col.field;
        const currentWidth = this.columnWidths[colKey] || (col.type === 'seq' ? 50 : col.width || 100);

        if (col.type === 'seq') {
          return {
            title: col.title,
            dataIndex: 'seq',
            key: 'seq',
            width: currentWidth,
            fixed: 'left',
            align: 'center',
            resizable: false
          };
        }
        return {
          title: col.title,
          dataIndex: col.field,
          key: col.field,
          width: currentWidth,
          ellipsis: {
            showTitle: true
          },
          resizable: true,
          originalTitle: col.title
        };
      });
    }
  },
  watch: {
    // 监听 PAGINATED 模式下 fetchCount 的变化，显示 loading
    'selectedTab.fetchCount': {
      handler(newVal, oldVal) {
        if (this.selectedTab?.receiveMode === 'PAGINATED' && this.selectedTab?.resultId) {
          if (newVal !== undefined && oldVal !== undefined && newVal > oldVal) {
            this.paginatedLoading[this.selectedTab.resultId] = true;
            if (this.paginatedLoadingTimer) {
              clearTimeout(this.paginatedLoadingTimer);
            }
            this.paginatedLoadingTimer = setTimeout(() => {
              if (this.selectedTab?.resultId) {
                this.paginatedLoading[this.selectedTab.resultId] = false;
              }
            }, 2000);
          }
        }
      },
      immediate: false
    }
  },
  mounted() {
    this.exportTypes = this.dmGlobalSetting.fmtConvertDef;
    this.initAllTabsExportState();
    this.$bus.on(EVENT_BUS_NAME_LIST.GET_RESULT_EXPORT_INFO, (info) => {
      // 导出信息事件，ResultSetMeta 中包含 resultId，不再需要处理 cacheFile
      // resultId 已经在 ResultSetMeta 对象中，会直接保存到 tab 中
    });
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_EXPORT_EVENT, (exportData) => {
      const targetTab = this.tab.result.list.find((tab) => tab.exportState?.downloadFile?.trackId === exportData.trackId);
      if (targetTab && targetTab.exportState) {
        targetTab.exportState.percent = exportData.percent;
        if (exportData?.status === 'FAILED') {
          // 保存错误信息到 exportState
          targetTab.exportState.errorStatus = exportData.status;
          targetTab.exportState.errorMessage = exportData.message || this.$t('cao-zuo-shi-bai-qing-zhong-xin-zhi-hang-cha-xun');
          targetTab.exportState.exporting = false;
          this.$Message.warning(this.$t('cao-zuo-shi-bai-qing-zhong-xin-zhi-hang-cha-xun'));
        } else if (exportData?.status === 'SUCCESS' || exportData?.percent === 100) {
          // 成功时清除错误状态
          if (targetTab.exportState.errorStatus) {
            targetTab.exportState.errorStatus = null;
            targetTab.exportState.errorMessage = null;
          }
        }
      }
    });
    this.$bus.on('setEditorHeight', (height) => {
      this.handleEditorHeightChange(height);
    });
    this.$bus.on('consoleMessageAppend', (curTab) => {
      this.handleMessageAppend(curTab);
    });
    this.pageHeight = window.innerHeight - 70;
    window.onresize = () => {
      this.$nextTick(() => {
        this.pageHeight = window.innerHeight - 70;
      });
    };
    // 初始化 SQL 导出配置默认值
    this.resetInsertOption();
    this.initDsTypeOptions();
  },
  beforeUnmount() {
    this.$bus.off('setEditorHeight');
    this.$bus.off('consoleMessageAppend');
    this.$bus.off(EVENT_BUS_NAME_LIST.GET_RESULT_EXPORT_INFO);
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_EXPORT_EVENT);
    if (this.paginatedLoadingTimer) {
      clearTimeout(this.paginatedLoadingTimer);
    }
  },
  methods: {
    handleClickCostPop() {
      console.warn(123, this.tab);
    },
    handleResize(e, column) {
      const startX = e.clientX;
      const colKey = column.dataIndex || column.key;
      const startWidth = this.columnWidths[colKey] || column.width || 100;
      const minWidth = 50;

      const onMouseMove = (moveEvent) => {
        moveEvent.preventDefault();
        const currentX = moveEvent.clientX;
        const offset = currentX - startX;
        let newWidth = startWidth + offset;
        if (newWidth < minWidth) newWidth = minWidth;

        this.columnWidths[colKey] = newWidth;
      };

      const onMouseUp = () => {
        document.body.removeEventListener('mousemove', onMouseMove);
        document.body.removeEventListener('mouseup', onMouseUp);
        document.body.style.cursor = '';
      };

      document.body.addEventListener('mousemove', onMouseMove);
      document.body.addEventListener('mouseup', onMouseUp);
      document.body.style.cursor = 'col-resize';
    },
    getRowNumber(index) {
      if (!this.selectedTab) return index + 1;

      const page = this.selectedTab.page || 1;
      const receiveMode = this.selectedTab.receiveMode || 'PAGE_FULL';

      let pageSize = 50; // 默认
      if (receiveMode === 'PAGINATED') {
        pageSize = 30;
      } else if (receiveMode === 'STREAM') {
        // STREAM 模式不分页，直接返回索引
        return index + 1;
      }

      return (page - 1) * pageSize + index + 1;
    },
    onContextmenu(event, tab) {
      console.log(event, tab);
      this.contextData = tab;
      ContextMenu.showContextMenu({
        zIndex: 999,
        x: event.x,
        y: event.y,
        theme: 'flat',
        items: [
          {
            label: this.$t('guan-bi-qi-ta-biao-qian'),
            onClick: () => this.handleCloseResultTab('other')
          }
        ],
        event,
        customClass: 'custom-class',
        minWidth: 100
      });
    },
    handleCloseResultTab(type, key) {
      console.log(type, key);
      if (type === 'current') {
        const deleteIndex = this.tab.result.list.findIndex((tab) => tab.resultId === key);
        const closingTab = this.tab.result.list[deleteIndex];
        if (closingTab) {
          this.callCloseResultWindow(closingTab);
          if (closingTab.exportState) {
            closingTab.exportState = null;
          }
        }
        this.tab.result.list.splice(deleteIndex, 1);
        const activeIndex = deleteIndex ? deleteIndex - 1 : 0;
        this.tab.result.active = this.tab.result.list.length ? this.tab.result.list[activeIndex].resultId : 'message';
      } else if (type === 'other') {
        if (this.contextData.resultId === 'message') {
          this.tab.result.active = 'message';
          this.tab.result.list.forEach((tab) => {
            this.callCloseResultWindow(tab);
            if (tab.exportState) {
              tab.exportState = null;
            }
          });
          this.tab.result.list = [];
        } else if (type === 'other') {
          const deleteIndex = this.tab.result.list.findIndex((tab) => tab.resultId === this.contextData.resultId);
          this.tab.result.active = this.contextData.resultId;
          const keepTab = this.tab.result.list[deleteIndex];
          this.tab.result.list.forEach((tab, index) => {
            if (index !== deleteIndex) {
              this.callCloseResultWindow(tab);
              if (tab.exportState) {
                tab.exportState = null;
              }
            }
          });
          this.tab.result.list = [keepTab];
        }
      }
    },
    callCloseResultWindow(resultTab) {
      if (!resultTab || !resultTab.resultId) {
        return;
      }

      const params = {
        data: {
          sessionId: this.tab.sessionId || '',
          resultIds: [resultTab.resultId || '']
        }
      };

      this.$services.dmQueryCloseResultWindow(params).catch((error) => {
        console.error('err:', error);
      });
    },
    handleResultTabChange(activeKey) {
      this.tab.result.active = activeKey;

      // process message table scroll position
      if (activeKey !== 'message') {
        return;
      }

      setTimeout(() => {
        const ele = document.getElementById('result-info-container');
        if (!ele) {
          return;
        }

        if (this.tab.executeInfoScrollDown) {
          ele.scrollTop = ele.scrollHeight;
        } else {
          ele.scrollTop = this.tab.executeInfoScrollPosition;
        }
      }, 30);
    },
    handleClearMessage() {
      this.tab.executeInfo = [];
      this.tab.executeInfo.push({
        resultType: 'Message',
        time: dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss'),
        level: 'info',
        message: this.$t('qing-xiu-cheng-gong')
      });
    },
    handleScrollUpMessage() {
      const ele = document.getElementById('result-info-container');
      if (ele) {
        ele.scrollTop = 0;
      }
    },
    handleScrollDownMessage() {
      this.tab.executeInfoScrollDown = !this.tab.executeInfoScrollDown;
      const ele = document.getElementById('result-info-container');
      if (ele) {
        ele.scrollTop = ele.scrollHeight;
      }
    },
    //
    handleMessageAppend(curTab) {
      if (curTab.result.active !== 'message') {
        return;
      }

      setTimeout(() => {
        const ele = document.getElementById('result-info-container');
        if (!ele) {
          return;
        }

        if (curTab.executeInfoScrollDown) {
          ele.scrollTop = ele.scrollHeight;
        }
        curTab.executeInfoScrollPosition = ele.scrollTop;
      }, 30);
    },
    handleEditorHeightChange(height) {
      this.editorHeight = height;
      this.pageHeight = window.innerHeight - 70;
    },
    //
    handleViewNoPassedRuleList(index) {
      this.$bus.emit('showNoPassedRuleListModal', index);
    },
    hideShowInsertSqlModal() {
      this.showInsertSqlModal = false;
      this.currentTableName = '';
    },
    handleCustomRow(record, index) {
      return {
        onClick: (event) => {
          this.handleRowClick(record, index, event);
        },
        onContextmenu: (event) => {
          this.handleRowContextMenu(record, index, event);
        }
      };
    },
    handleRowClick(record, index, event) {
      // 处理行点击
      this.rowIndex = index;
      this.selectedRow = record;
    },
    handleRowContextMenu(record, index, event) {
      event.preventDefault();
      this.rowIndex = index;
      this.selectedRow = record;

      ContextMenu.showContextMenu({
        x: event.x,
        y: event.y,
        theme: 'flat',
        items: [
          {
            label: this.$t('dan-hang-dao-chu'),
            onClick: () => {
              this.handleRowExport(index);
            }
          }
        ],
        event,
        customClass: 'custom-class',
        zIndex: 99,
        minWidth: 100
      });
    },
    handleCellCopy(value) {
      if (value !== null && value !== undefined) {
        if (XEClipboard.copy(value)) {
          this.$message.success(this.$t('fu-zhi-cheng-gong'));
        }
      }
    },
    // 获取单元格的 complete 字段，用于判断是否显示角标
    getCellComplete(column, rowIndex) {
      try {
        const colIndex = this.selectedTab.columnList?.findIndex((col) => col === column.dataIndex || col === column.property) ?? -1;
        if (colIndex < 0) return true;

        const receiveMode = this.selectedTab.receiveMode || 'PAGE_FULL';

        if (receiveMode === 'PAGINATED') {
          const currentPage = this.selectedTab.page || 1;
          const rowSetCache = this.selectedTab.rowSetCache;

          if (rowSetCache && rowSetCache[currentPage] && rowSetCache[currentPage][rowIndex]) {
            const rowItem = rowSetCache[currentPage][rowIndex];
            const rowData = rowItem.data || rowItem.row;

            if (rowData && Array.isArray(rowData) && rowData[colIndex]) {
              return rowData[colIndex].complete !== undefined ? rowData[colIndex].complete : true;
            }
          }
        } else if (receiveMode === 'STREAM') {
          const rowSetStream = this.selectedTab.rowSetStream;
          const streamData = this.selectedTab.streamData || [];

          const displayCount = 30;
          const startIndex = streamData.length > displayCount ? streamData.length - displayCount : 0;
          const actualIndex = startIndex + rowIndex;

          if (rowSetStream && rowSetStream[actualIndex]) {
            const rowItem = rowSetStream[actualIndex];
            const rowData = rowItem.data || rowItem.row;

            if (rowData && Array.isArray(rowData) && rowData[colIndex]) {
              return rowData[colIndex].complete !== undefined ? rowData[colIndex].complete : true;
            }
          }
        } else {
          if (this.selectedTab.data && this.selectedTab.data[rowIndex]) {
            const rowData = this.selectedTab.data[rowIndex];
            if (Array.isArray(rowData) && rowData[colIndex]) {
              return rowData[colIndex].complete !== undefined ? rowData[colIndex].complete : true;
            }
          }
        }
      } catch (err) {
        console.log('获取单元格 complete 字段失败:', err);
      }
      return true;
    },
    handleCellDetail(record, column, rowIndex) {
      const colIndex = this.selectedTab.columnList?.findIndex((col) => col === column.dataIndex || col === column.property) ?? -1;

      // 计算实际行号（考虑分页）
      let rowNumber = rowIndex;
      if (this.selectedTab.receiveMode === 'PAGINATED') {
        const pageSize = 30;
        rowNumber = (this.selectedTab.page - 1) * pageSize + rowIndex;
      } else if (this.selectedTab.receiveMode === 'STREAM') {
        rowNumber = rowIndex;
      } else {
        const pageSize = 50;
        rowNumber = (this.selectedTab.page - 1) * pageSize + rowIndex;
      }

      const cellValue = record[column.dataIndex || column.property] || '';

      let moreSize = 0;
      let totalSize = 0;
      let complete = true;
      let error = false;
      let mask = false;
      try {
        const receiveMode = this.selectedTab.receiveMode || 'PAGE_FULL';

        if (receiveMode === 'PAGINATED') {
          const currentPage = this.selectedTab.page || 1;
          const rowSetCache = this.selectedTab.rowSetCache;

          if (rowSetCache && rowSetCache[currentPage] && rowSetCache[currentPage][rowIndex]) {
            const rowItem = rowSetCache[currentPage][rowIndex];
            const rowData = rowItem.data || rowItem.row;

            if (rowData && Array.isArray(rowData) && rowData[colIndex]) {
              moreSize = rowData[colIndex].moreSize || 0;
              totalSize = rowData[colIndex].totalSize || 0;
              complete = rowData[colIndex].complete !== undefined ? rowData[colIndex].complete : true;
              error = rowData[colIndex].error || false;
              mask = rowData[colIndex].mask || false;
            }
          }
        } else if (receiveMode === 'STREAM') {
          const rowSetStream = this.selectedTab.rowSetStream;
          const streamData = this.selectedTab.streamData || [];

          const displayCount = 30;
          const startIndex = streamData.length > displayCount ? streamData.length - displayCount : 0;
          const actualIndex = startIndex + rowIndex;

          if (rowSetStream && rowSetStream[actualIndex]) {
            const rowItem = rowSetStream[actualIndex];
            const rowData = rowItem.data || rowItem.row;

            if (rowData && Array.isArray(rowData) && rowData[colIndex]) {
              moreSize = rowData[colIndex].moreSize || 0;
              totalSize = rowData[colIndex].totalSize || 0;
              complete = rowData[colIndex].complete !== undefined ? rowData[colIndex].complete : true;
              error = rowData[colIndex].error || false;
              mask = rowData[colIndex].mask || false;
            }
          }
        } else {
          if (this.selectedTab.data && this.selectedTab.data[rowIndex]) {
            const rowData = this.selectedTab.data[rowIndex];
            if (Array.isArray(rowData) && rowData[colIndex]) {
              moreSize = rowData[colIndex].moreSize || 0;
              totalSize = rowData[colIndex].totalSize || 0;
              complete = rowData[colIndex].complete !== undefined ? rowData[colIndex].complete : true;
              error = rowData[colIndex].error || false;
              mask = rowData[colIndex].mask || false;
            }
          }
        }
      } catch (err) {
        console.log('获取单元格原始数据失败:', err);
      }

      this.$bus.emit('showCellDetailModal', {
        row: record,
        column: { property: column.dataIndex || column.property },
        resultId: this.selectedTab.resultId,
        rowNumber,
        colNumber: colIndex,
        cellValue,
        moreSize,
        totalSize,
        complete,
        error,
        mask
      });
    },
    generateRowInsert(row) {
      const { dsType } = this.tab;
      const { columnList, columnType, resource } = this.selectedTab;
      const tableName = this.currentTableName || resource || 'my_table';
      this.currentTableName = tableName;
      let keyStr = '';
      const qualifier = this.getQualifier(dsType);
      const { left, right } = qualifier;
      keyStr = `${left}${columnList.join(`${right}, ${left}`)}${right}`;
      let valueStr = '';
      columnList.forEach((key1, index) => {
        const value = row[key1];
        let insertType;
        if (this.tab.dataSourceType === 'MySQL') {
          insertType = mysqlInsert;
        } else {
          insertType = pgInsert;
        }
        if (index !== columnList.length - 1) {
          if (value === null) {
            valueStr += 'null, ';
          } else if (insertType.needQuote.indexOf(columnType[index]) > -1) {
            valueStr += `'${value}', `;
          } else if (insertType.noNeedQuote.indexOf(columnType[index]) > -1) {
            valueStr += `${value}, `;
          } else {
            valueStr += `'${value}', `;
          }
        } else {
          if (value === null) {
            valueStr += 'null';
          } else if (insertType.needQuote.indexOf(columnType[index]) > -1) {
            valueStr += `'${value}'`;
          } else if (insertType.noNeedQuote.indexOf(columnType[index]) > -1) {
            valueStr += `${value} `;
          } else {
            valueStr += `'${value}' `;
          }
        }
      });

      console.log(`INSERT INTO ${left}${tableName}${right} (${keyStr}) VALUES (${valueStr})`);

      return `INSERT INTO ${left}${tableName}${right} (${keyStr}) VALUES (${valueStr})`;
    },
    async handleExport({ key, rowIndex }) {
      this.actionType = key;
      const { showData } = this.selectedTab;
      const sqls = [];
      let showInsertSqlModal = false;
      switch (key) {
        case 'all':
          const { instance, database, currentTable } = this.tab;
          const list = [];
          const columns = this.selectedTab.columnList;
          if (this.selectedTab && this.selectedTab.data) {
            this.selectedTab.data.forEach((item) => {
              const currentRow = {};
              for (let i = 0; i < columns.length; i++) {
                currentRow[columns[i]] = item[i].value;
                currentRow[`render_${columns[i]}`] = item[i].value;
              }
              list.push(currentRow);
            });

            this.$refs[`result_table_${this.tab.result.active}`].exportData({
              filename: `${instance}/${database}/${currentTable}`,
              type: 'csv',
              data: list,
              columnFilterMethod: ({ column }) => column.property
            });
          } else {
            Modal.info({
              title: this.$t('ti-shi'),
              content: this.$t('wu-shu-ju')
            });
          }
          break;
        case 'currentInsert':
          if (this.selectedRow) {
            showInsertSqlModal = true;
            let selectedRow = [];
            if (rowIndex) {
              selectedRow = showData[rowIndex];
            } else {
              selectedRow = showData[this.rowIndex];
            }
            sqls.push(this.generateRowInsert(selectedRow));
          } else {
            Modal.warning({
              content: this.$t('qing-zhi-shao-xuan-ze-yi-tiao-shu-ju')
            });
          }

          break;
        case 'allInsert':
          if (showData) {
            showInsertSqlModal = true;
            showData.forEach((row) => {
              sqls.push(this.generateRowInsert(row));
            });
          } else {
            Modal.info({
              title: this.$t('ti-shi'),
              content: this.$t('wu-shu-ju')
            });
          }
          break;
        default:
          break;
      }
      this.sqls = sqls;
      this.showInsertSqlModal = showInsertSqlModal;
    },
    generateInsertSql() {
      const { showData } = this.selectedTab;
      const sqls = [];
      if (this.actionType === 'currentInsert') {
        sqls.push(this.generateRowInsert(showData[this.rowIndex]));
      } else if (this.actionType === 'allInsert') {
        showData.forEach((row) => {
          sqls.push(this.generateRowInsert(row));
        });
      }
      this.sqls = sqls;
    },
    exportData() {
      const { instance, database, currentTable } = this.currentTab;
      this.$refs[`result_table_${this.tab.result.active}`][0].openExport({
        filename: `${instance}-${database}-${currentTable}`,
        types: ['csv']
      });
    },
    async changePage(page) {
      const tab = this.selectedTab;
      const receiveMode = tab.receiveMode || 'PAGE_FULL';

      // STREAM 模式不支持分页切换
      if (receiveMode === 'STREAM') {
        return;
      }

      if (receiveMode === 'PAGINATED') {
        // 后端分页模式
        tab.page = page;
        const pageSize = 30;
        const offsetRow = (page - 1) * pageSize;

        // 检查缓存
        if (tab.pageCache && tab.pageCache[page]) {
          tab.showData = tab.pageCache[page];
          return;
        }

        // 调用接口获取数据
        try {
          const res = await this.$services.dmQueryFetchResultPage({
            data: {
              resultId: tab.resultId,
              offsetRow,
              pageSize
            }
          });

          if (res.success && res.data && res.data.rowSet) {
            const { rowSet } = res.data;
            const { columnList } = tab;
            const list = [];

            if (rowSet && columnList) {
              rowSet.forEach((item) => {
                const currentRow = {};
                const rowData = item.data || item.row;
                if (rowData) {
                  for (let i = 0; i < columnList?.length; i++) {
                    if (rowData[i]) {
                      currentRow[columnList[i]] = rowData[i].value;
                    }
                  }
                }
                list.push(currentRow);
              });
            }

            if (!tab.pageCache) {
              tab.pageCache = {};
            }
            tab.pageCache[page] = list;
            tab.showData = list;
            // 保存原始 rowSet 数据，用于获取 moreSize 等信息
            if (!tab.rowSetCache) {
              tab.rowSetCache = {};
            }
            tab.rowSetCache[page] = rowSet; // 保存当前页的原始数据
          }
        } catch (error) {
          console.error('获取分页数据失败:', error);
          this.$Message.error(this.$t('huo-qu-fen-ye-shu-ju-shi-bai'));
        }
      } else if (receiveMode === 'STREAM') {
        tab.page = page;
      } else {
        tab.page = page;
        tab.showData = tab.dataArr[page - 1];
      }
    },
    handleResultExport() {
      this.isFromContextMenu = false;
      this.exportRangeType = 'all';
      this.selectedRowIndex = null;

      const jsonType = this.exportTypes.find((t) => t.name === 'application/json' || t.name === 'json');
      this.currentExportType = jsonType?.name || (this.exportTypes.length > 0 ? this.exportTypes[0].name : '');

      this.exportModalTitle = this.$t('dao-chu');
      this.resetInsertOption();
      this.showExportOptionModal = true;
    },
    handleRowExport(rowIndex) {
      this.isFromContextMenu = true;
      this.exportRangeType = 'single';
      this.selectedRowIndex = rowIndex;

      const jsonType = this.exportTypes.find((t) => t.name === 'application/json' || t.name === 'json');
      this.currentExportType = jsonType?.name || (this.exportTypes.length > 0 ? this.exportTypes[0].name : '');

      this.exportModalTitle = this.$t('dao-chu');
      this.resetInsertOption();
      this.showExportOptionModal = true;
    },
    handleExportRangeChange() {
      if (!this.isFromContextMenu && this.exportRangeType === 'single') {
        this.exportRangeType = 'all';
      }
      this.resetInsertOption();
    },
    getExportTypeIcon(type) {
      const item = this.exportTypes.find((t) => t.name === type);
      return item?.icon || '';
    },
    getExportTypeDescription(type) {
      const item = this.exportTypes.find((t) => t.name === type);
      return item?.description || '';
    },
    resetInsertOption() {
      const columns = Array.isArray(this.selectedTab?.columnList) ? this.selectedTab.columnList : [];
      const tableName = this.selectedTab?.resource || 'table_name';

      let offset = 0;
      let limit = -1;

      if (this.exportRangeType === 'single') {
        const receiveMode = this.selectedTab?.receiveMode || 'PAGE_FULL';
        const page = this.selectedTab?.page || 1;
        const rowIndex = this.selectedRowIndex !== null ? this.selectedRowIndex : 0;

        if (receiveMode === 'PAGINATED') {
          offset = (page - 1) * 30 + rowIndex;
          limit = 1;
        } else if (receiveMode === 'STREAM') {
          const streamData = this.selectedTab?.streamData || [];
          const displayCount = 30;
          const startIndex = streamData.length > displayCount ? streamData.length - displayCount : 0;
          offset = startIndex + rowIndex;
          limit = 1;
        } else {
          offset = (page - 1) * 30 + rowIndex;
          limit = 1;
        }
      } else if (this.exportRangeType === 'page') {
        const receiveMode = this.selectedTab?.receiveMode || 'PAGE_FULL';
        const page = this.selectedTab?.page || 1;

        if (receiveMode === 'PAGINATED') {
          offset = (page - 1) * 30;
          limit = 30;
        } else if (receiveMode === 'STREAM') {
          offset = 0;
          limit = 30;
        } else {
          offset = (page - 1) * 50;
          limit = 30;
        }
      } else {
        // 全部导出：limit为-1
        offset = 0;
        limit = -1;
      }

      this.insertOption = {
        tableName,
        columns: columns.map((name) => ({
          columnName: name,
          originalColumnName: name,
          export: true,
          isEditing: false
        })),
        dataSourceType: this.tab.dataSourceType || this.tab.dsType || '',
        mergeInsert: false,
        valueSize: 50,
        offset,
        limit
      };
      this.columnSearch = '';
      this.columnSelectAll = true;
    },
    toggleSelectAll() {
      const checked = this.columnSelectAll;
      (this.insertOption.columns || []).forEach((c) => (c.export = checked));
    },
    startEditField(col, idx) {
      col.isEditing = true;
      this.$nextTick(() => {
        const input = this.$refs.fieldInputs?.[idx];
        if (input) {
          input.focus();
          input.select();
        }
      });
    },
    finishEditField(col, idx) {
      col.isEditing = false;
      // 如果字段名为空，恢复为原始字段名
      if (!col.columnName.trim()) {
        col.columnName = col.originalColumnName;
      }
    },
    cancelEditField(col, idx) {
      col.isEditing = false;
      col.columnName = col.originalColumnName;
    },
    initDsTypeOptions() {
      if (this.dmGlobalSetting && this.dmGlobalSetting.dsSettingDef) {
        this.dsTypeOptions = Object.keys(this.dmGlobalSetting.dsSettingDef);
      }
    },
    async confirmSqlExportOption() {
      try {
        const res = await this.$services.dmQueryExportResult({
          data: {
            resultId: this.selectedTab.resultId || '',
            dstFileName: '',
            dstFormatName: 'application/sql',
            option: this.insertOption
          }
        });

        if (res.success) {
          this.showSqlExportOptionModal = false;
          this.initTabExportState();
          this.selectedTab.exportState.exporting = true;
          this.selectedTab.exportState.downloadFile = {
            trackId: res?.data?.trackId,
            file: res?.data?.newFile,
            format: res?.data?.newFormat
          };
        } else {
          throw new Error(res.message);
        }
      } catch (error) {
        console.error('err:', error);
      }
    },
    async confirmExportOption() {
      try {
        let offset = 0;
        let limit = -1;

        if (this.exportRangeType === 'single') {
          // 单行导出
          const receiveMode = this.selectedTab?.receiveMode || 'PAGE_FULL';
          const page = this.selectedTab?.page || 1;
          const rowIndex = this.selectedRowIndex !== null ? this.selectedRowIndex : 0;

          if (receiveMode === 'PAGINATED') {
            offset = (page - 1) * 30 + rowIndex;
            limit = 1;
          } else if (receiveMode === 'STREAM') {
            const streamData = this.selectedTab?.streamData || [];
            const displayCount = 30;
            const startIndex = streamData.length > displayCount ? streamData.length - displayCount : 0;
            offset = startIndex + rowIndex;
            limit = 1;
          } else {
            offset = (page - 1) * 50 + rowIndex;
            limit = 1;
          }
        } else if (this.exportRangeType === 'page') {
          // 单页导出
          const receiveMode = this.selectedTab?.receiveMode || 'PAGE_FULL';
          const page = this.selectedTab?.page || 1;

          if (receiveMode === 'PAGINATED') {
            offset = (page - 1) * 30;
            limit = 30;
          } else if (receiveMode === 'STREAM') {
            offset = 0;
            limit = 30;
          } else {
            offset = (page - 1) * 50;
            limit = 30;
          }
        } else {
          // 全部导出
          offset = 0;
          limit = -1;
        }

        // 更新insertOption中的offset和limit
        const exportOption = {
          ...this.insertOption,
          offset,
          limit
        };

        const res = await this.$services.dmQueryExportResult({
          data: {
            resultId: this.selectedTab.resultId || '',
            dstFileName: '',
            dstFormatName: this.currentExportType,
            option: exportOption
          }
        });

        if (res.success) {
          this.showExportOptionModal = false;
          this.initTabExportState();
          this.selectedTab.exportState.exporting = true;
          this.selectedTab.exportState.downloadFile = {
            trackId: res?.data?.trackId,
            file: res?.data?.newFile,
            format: res?.data?.newFormat
          };
        } else {
          throw new Error(res.message);
        }
      } catch (error) {
        console.error('err:', error);
      }
    },
    async downloadExportedFile() {
      if (!this.selectedTab.resultId) {
        return;
      }

      try {
        const res = await this.$services.dmQueryDownloadResult({
          data: {
            resultId: this.selectedTab.exportState.downloadFile.trackId
          },
          responseType: 'blob',
          modal: false
        });

        const blob = res && res.data instanceof Blob ? res.data : res;
        if (!(blob instanceof Blob)) {
          this.$Message.warning(this.$t('wen-jian-yi-shi-xiao'));
          throw new Error('响应数据格式不正确');
        }

        // 从 Content-Disposition 解析文件名
        let fileName = '';
        try {
          const dispositionRaw = res && res.headers ? res.headers['Content-Disposition'] || res.headers['content-disposition'] || '' : '';
          const disposition = typeof dispositionRaw === 'string' ? dispositionRaw.trim() : '';
          fileName = disposition.split('filename=')[1];
        } catch (e) {
          console.log(e);
        }

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
      } catch (error) {
        console.error('err:', error);
        if (this.selectedTab.exportState) {
          // this.selectedTab.exportState.exporting = false;
        }
      }
    },

    initTabExportState() {
      if (!this.selectedTab.exportState) {
        this.selectedTab.exportState = {
          exporting: false,
          percent: 0,
          downloadFile: {
            trackId: '',
            file: '',
            format: ''
          },
          cacheFile: '',
          errorStatus: null,
          errorMessage: null
        };
      }
    },
    initAllTabsExportState() {
      this.tab.result.list.forEach((tab) => {
        if (!tab.exportState) {
          tab.exportState = {
            exporting: false,
            percent: 0,
            downloadFile: {
              trackId: '',
              file: '',
              format: ''
            },
            cacheFile: '',
            errorStatus: null,
            errorMessage: null
          };
        }
      });
    },
    resetTabExportState() {
      if (this.selectedTab.exportState) {
        this.selectedTab.exportState.exporting = false;
        this.selectedTab.exportState.percent = 0;
        this.selectedTab.exportState.downloadFile = {
          trackId: '',
          file: '',
          format: ''
        };
        this.selectedTab.exportState.errorStatus = null;
        this.selectedTab.exportState.errorMessage = null;
      }
    }
  }
};
</script>
<style lang="less" scoped>
.cost-pop {
  color: #aaa;
  font-size: 12px;
}
// common
.dropdown-item {
  display: flex;
  justify-content: right;

  .dropdown-item-title {
    width: calc(100% - 16px);
  }

  .dropdown-item-close {
    padding-left: 5px;
    width: 16px;
  }
}

:deep(.radius-hover:hover) {
  vertical-align: middle;
  color: var(--primary-color);
}

// result
.result-container {
  :deep(.ant-table .ant-table-tbody tr td) {
    padding: 0 !important;
  }

  overflow: hidden;

  .tab-group {
    display: flex;
    width: 100%;

    .right {
      flex: 1;
      min-width: 0;
    }

    :deep(.ant-tabs-top > .ant-tabs-nav) {
      margin: 0;
    }

    :deep(.ant-tabs-nav-more) {
      display: none;
    }
  }

  :deep(.result-tab-dropdown) {
    margin-right: 16px !important;
  }

  .result-content-wrapper {
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }

  .result-table-container {
    flex: 1;
    min-height: 0;
    width: 100%;
    overflow-x: auto;
    overflow-y: auto;

    .result-set-style {
      :deep(.ant-table) {
        font-size: 12px;
      }

      :deep(.ant-table-container) {
        overflow: visible !important;
      }

      :deep(.ant-table-body) {
        overflow: visible !important;
      }

      :deep(.ant-table-content) {
        overflow: visible !important;
      }

      :deep(.ant-table-thead > tr > th) {
        padding: 4px 8px;
        font-weight: 500;
        cursor: default;
        overflow: visible;
        text-overflow: ellipsis;
        white-space: nowrap;
        position: relative;
        user-select: none;
      }

      :deep(.ant-table-thead > tr > th::after) {
        display: none !important;
        width: 0 !important;
        height: 0 !important;
        pointer-events: none !important;
      }

      :deep(.ant-table-thead > tr > th) {
        &::before {
          display: none !important;
        }
      }

      :deep(.ant-table-thead > tr > th) {
        resize: none !important;
      }

      :deep(.ant-table-thead > tr > th .header-cell-content) {
        position: relative;
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 100%;
        width: 100%;

        .header-title {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          flex: 1;
        }

        .resize-handle {
          position: absolute;
          right: 0;
          top: 0;
          bottom: 0;
          width: 4px;
          cursor: col-resize;
          z-index: 10;
          background: transparent;

          &::after {
            content: '';
            position: absolute;
            right: 0;
            top: 10%;
            bottom: 10%;
            width: 2px;
            background-color: #d9d9d9;
            transition: background-color 0.2s;
          }

          &:hover::after {
            background-color: #1890ff;
            width: 3px;
          }
        }
      }

      :deep(.ant-table-tbody > tr > td) {
        padding: 2px 8px;
      }

      // 去除空白行
      :deep(.ant-table-placeholder) {
        display: none;
      }
    }

    :deep(.seq-content) {
      padding: 0 4px !important;
    }

    :deep(.seq-header) {
      padding: 0 4px;
    }

    :deep(.cell-header) {
      padding: 0 4px;
    }
  }

  .ivu-table-small .ivu-table-header thead tr th {
    height: 27px;
    line-height: 27px;
  }

  .result-info-container {
    overflow-y: scroll;
    width: 100%;

    .result-info-messages {
      width: calc(100% - 28px);
      min-height: 100%;
      display: inline-block;
      border-right: #c0c4cc solid 1px;
      margin-left: 3px;
      padding-top: 3px;

      .result-info {
        margin-bottom: 1px;
        font-weight: bold;
        font-size: 12px;

        .info {
          display: flex;

          .level {
            border-radius: 1px;
            height: 18px;
            margin-right: 3px;
            color: #19be6b;
          }

          .time {
            margin-right: 5px;
            color: #aaa;
          }

          .message {
            flex: 1;
            word-break: break-all;

            &.Warn {
              color: #f90;
            }

            &.Error {
              color: #ed4014;
            }
          }
        }
      }
    }

    .result-info-buttons {
      width: 24px;
      display: inline-block;
      vertical-align: top;

      .btn-group {
        position: fixed;

        .btn-group-item {
          padding: 0 2px;
          margin: 2px 2px;
          display: block;
        }

        :deep(.btn-group-item-hover),
        .btn-group-item:hover {
          vertical-align: middle;
          background: #e4e4e4;
          border-radius: 3%;
          cursor: pointer;
        }
      }
    }
  }
}

.insert-sql-modal {
  display: flex;
  flex-direction: column;

  .insert-sql-operation {
    display: flex;
    margin-bottom: 10px;
  }

  .insert-sql-content {
    border: 1px solid rgba(218, 218, 218, 1);
    padding: 10px;
    max-height: 540px;
    overflow: auto;
  }
}

.tip-footer {
  width: 100%;
  height: 30px;
  line-height: 30px;
  padding: 0 10px;
  display: flex;
  //justify-content: space-between;
  color: rgba(0, 0, 0, 0.88);
  background: #ffffff;
  z-index: 9;
}

.tip-footer-right {
  display: flex;
  align-items: center;
  :deep(.ant-btn) {
    display: inline-flex;
    align-items: center;
    height: 30px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
}

:deep(.ant-table-wrapper .ant-table-resize-handle) {
  cursor: initial !important;
  display: none !important;
}

.download-warp {
  display: flex;
  align-items: center;
}
.export-progress-modal {
  width: 100px;
}
.download-btn {
  margin: 0 5px 0 8px;
}

// modern sql export modal
.sql-export-modern {
  display: flex;
  gap: 16px;

  .left {
    width: 280px;
    padding: 12px;
    background: #fafafa;
    border-radius: 8px;
  }

  .right {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;

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
}

.paginated-loading {
  display: flex;
  align-items: center;
  height: 30px;
  margin-right: 4px;

  .loading-spinner {
    width: 14px;
    height: 14px;
    border: 2px solid #e8e8e8;
    border-top-color: #1890ff;
    border-radius: 50%;
    animation: loading-spin 0.8s linear infinite;
  }
}

@keyframes loading-spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

// 通用导出弹窗样式
.export-option-modal {
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

  .vxe-input-tpl {
    position: relative;
    display: flex;
    align-items: center;
    width: 100%;
    height: 100%;
    padding: 0 8px;

    pre {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .op {
      display: none;
      position: absolute;
      right: 4px;
      top: 50%;
      transform: translateY(-50%);
      background: rgba(255, 255, 255, 0.95);
      padding: 2px 4px;
      border-radius: 3px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      div {
        display: inline-block;
        cursor: pointer;
        padding: 2px;
        transition: all 0.2s;

        &:hover {
          opacity: 0.7;
        }
      }
    }

    &:hover .op {
      display: flex;
      align-items: center;
    }
  }
}
.cell-incomplete-badge {
  position: absolute;
  right: 2px;
  bottom: 2px;
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-bottom: 6px solid #ff9800;
  z-index: 1;
}

[data-theme='dark'] {
  .result-container .result-info-container .result-info-messages {
    border-right: 1px solid var(--border-primary);
  }

  .vxe-input-tpl .op {
    background: rgba(0, 0, 0, 0.9);
  }
}
:deep(.ant-table .ant-table-tbody tr td) {
  padding: 0 !important;
}
:deep(.ant-table-tbody .ant-table-cell) {
  padding: 3px !important;
}
</style>
