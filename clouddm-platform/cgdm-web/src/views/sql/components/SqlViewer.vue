<template>
  <div class="sql-viewer">
    <operators
      :tab="tab"
      :stopping="stopping"
      :handleRun="onRun"
      :handlePlan="onRun"
      :handleRollback="handleRollback"
      :handleCommit="handleCommit"
      :handleSetIsolation="handleSetIsolation"
      :handleSetTx="handleSetTx"
      :handleStop="handleStop"
      :handleReadOnly="handleReadOnly"
      :storeQueryTabs="storeQueryTabs"
      :formatSql="formatSql"
    />
    <Editor
      :set-editor-instance="setEditorInstance"
      :current-tab="tab"
      ref="editor"
      style="border-bottom: 1px solid #ccc"
      :completion-data="completionData"
      :store-query-tabs="storeQueryTabs"
      :rdb-object-detail="rdbObjectDetail"
      :on-run="onRun"
    />
    <div class="editor-resize" />
    <div :class="`message ${tab.message.type}`" v-if="tab.message.text && tab.message.show && tab.connected">
      <div v-html="tab.message.text"></div>
      <CustomIcon type="icon-v2-close2" @click="handleCloseError" hoverStyle />
    </div>
    <div class="message Error" v-if="!tab.connected && tab.msgContent">
      {{ tab.msgContent }}
      <Button type="text" size="small" @click="handleClickDsStatusIcon">
        {{ $t('zhong-xin-lian-jie') }}
      </Button>
    </div>
    <CCModal v-model="showNoPassedRuleModal" :title="$t('gui-ze-xiao-yan-shi-bai')" :width="800">
      <Table :columns="noPassedRuleColumns" :data="noPassedRuleList" border stripe>
        <template #lines="{ row }">
          <Tooltip :content="row.lines?.join?.(', ')" placement="top" transfer>
            <div class="lines-cell">
              {{ row.lines?.join?.(', ') }}
            </div>
          </Tooltip>
        </template>
        <template #warnLevel="{ row }">
          <Tag :color="row.level === 'SUGGEST' ? 'warning' : 'error'">
            {{ RULE_WARN_LEVEL[row.level] }}
          </Tag>
        </template>
      </Table>
      <template #footer>
        <Button @click="handleReRunAfterRuleCheck" type="primary" v-if="currentWarnLevel === 'SUGGEST'">
          {{ $t('zai-ci-zhi-hang') }}
        </Button>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import * as monaco from 'monaco-editor';
import { mapGetters, mapMutations, mapState } from 'vuex';
import Operators from '@/views/sql/components/Operators';
import { chunk } from 'xe-utils';
import Editor from '@/components/editor';
import browseMixin from '@/mixins/browseMixin';
import { sendWebSocket } from '@/services/socket';
import sqlMixin from '@/mixins/sqlMixin';
import { RULE_WARN_LEVEL, WS_REQ_QUERY_TYPE, WS_TYPE } from '@/utils';
import { UPDATE_SOCKET_STATUS } from '@/store/mutationTypes';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import formatError from '@/services/formatError';

export default {
  name: 'SqlViewer',
  components: {
    Editor,
    Operators
  },
  props: {
    handleClickDsStatusIcon: Function,
    handleGetDsSetting: Function,
    tabs: {
      type: Array,
      default: () => []
    },
    rdbObjectDetail: Function,
    completionData: Object,
    tab: Object,
    storeQueryTabs: Function,
    createSession: Function
  },
  computed: {
    ...mapGetters(['isDesktop']),
    ...mapState(['productClusterList', 'dmGlobalSetting']),
    noPassedRuleColumns() {
      const columns = [
        {
          title: this.$t('deng-ji'),
          slot: 'warnLevel',
          width: 90
        },
        {
          title: this.$t('ming-cheng'),
          key: 'ruleName',
          width: 200
        },
        {
          title: this.$t('miao-shu'),
          key: 'ruleDesc'
        }
      ];

      const dsConfig = this.dmGlobalSetting.dsSettingDef[this.tab.dsType];
      if (dsConfig && dsConfig.features && dsConfig.features.FUNC_LINES_SUPPORT === true) {
        columns.push({
          title: this.$t('hang-hao'),
          slot: 'lines',
          width: 120
        });
      }

      return columns;
    }
  },
  mixins: [sqlMixin, browseMixin],
  data() {
    return {
      RULE_WARN_LEVEL,
      currentSql: '',
      position: {},
      currentWarnLevel: '',
      noPassedRuleList: [],
      showNoPassedRuleModal: false,
      stopping: false,
      monacoEditor: null
    };
  },
  mounted() {
    this.$bus.on('showNoPassedRuleListModal', (index) => this.handleShowUnPassedRuleListModal(index));
  },
  beforeUnmount() {
    this.$bus.off('showNoPassedRuleListModal');
  },
  methods: {
    ...mapMutations([UPDATE_SOCKET_STATUS]),
    handleCloseModal() {
      this.currentSql = '';
      this.currentWarnLevel = '';
      this.showNoPassedRuleModal = false;
    },
    handleShowUnPassedRuleListModal(index) {
      this.noPassedRuleList = this.tab.executeInfo[index].ruleList;
      this.showNoPassedRuleModal = true;
    },
    showNoExecutableSqlError() {
      this.$refs.editor?.showNoExecutableSqlError?.(this.$t('dang-qian-mei-you-ke-zhi-hang-de-sql-yu-ju'));
    },
    async handleReRunAfterRuleCheck() {
      const type = this.tab.currentQueryType || 'query';

      if (!this.currentSql) {
        await this.onRun(type);
        return;
      }

      if (type === 'plan') {
        this.handleInterceptor('explain', async () => {
          await this.handlePlan(this.currentSql, this.position, true);
        });
      } else {
        await this.handleRun(this.currentSql, this.position, true);
      }
    },
    //
    handleGetRecoveryStatus() {
      sendWebSocket(
        {
          type: WS_TYPE.WS_REQ_QUERY,
          object: {
            sessionId: this.tab.sessionId,
            queryType: WS_REQ_QUERY_TYPE.RECOVER_STATUS,
            levels: this.browseGenLevelsData(this.tab.node),
            rdbAutoCommit: this.tab.autoCommit,
            rdbReadOnly: this.tab.readOnly,
            rdbIsolation: this.tab.isolation
          }
        },
        { message: this.onmessage }
      );
    },
    handleReadOnly(e) {
      this.handleInterceptor('readOnly', () => {
        sendWebSocket(
          {
            type: WS_TYPE.WS_REQ_QUERY,
            object: {
              sessionId: this.tab.sessionId,
              queryType: WS_REQ_QUERY_TYPE.TX_STATUS,
              levels: this.browseGenLevelsData(this.tab.node),
              rdbAutoCommit: this.tab.autoCommit,
              rdbReadOnly: e,
              rdbIsolation: this.tab.isolation
            }
          },
          { message: this.onmessage }
        );
      });
    },
    handleStop() {
      this.handleInterceptor(
        'cancel',
        () => {
          this.tab.running = false;
          this.tab.stopping = true;
          sendWebSocket(
            {
              type: WS_TYPE.WS_REQ_QUERY,
              object: {
                sessionId: this.tab.sessionId,
                queryType: WS_REQ_QUERY_TYPE.CANCEL_QUERY,
                levels: this.browseGenLevelsData(this.tab.node),
                rdbAutoCommit: this.tab.autoCommit,
                rdbReadOnly: this.tab.readOnly,
                rdbIsolation: this.tab.isolation
              }
            },
            { message: this.onmessage }
          );
        },
        true,
        !this.tab.running
      );
    },
    handleRollback() {
      sendWebSocket(
        {
          type: WS_TYPE.WS_REQ_QUERY,
          object: {
            sessionId: this.tab.sessionId,
            queryType: WS_REQ_QUERY_TYPE.TX_ROLLBACK,
            levels: this.browseGenLevelsData(this.tab.node),
            rdbAutoCommit: this.tab.autoCommit,
            rdbReadOnly: this.tab.readOnly,
            rdbIsolation: this.tab.isolation
          }
        },
        { message: this.onmessage }
      );
    },
    handleCommit() {
      sendWebSocket(
        {
          type: WS_TYPE.WS_REQ_QUERY,
          object: {
            sessionId: this.tab.sessionId,
            queryType: WS_REQ_QUERY_TYPE.TX_COMMIT,
            levels: this.browseGenLevelsData(this.tab.node),
            rdbAutoCommit: this.tab.autoCommit,
            rdbReadOnly: this.tab.readOnly,
            rdbIsolation: this.tab.isolation
          }
        },
        { message: this.onmessage }
      );
    },
    handleSetIsolation(isolation) {
      this.handleInterceptor('isolation', () => {
        sendWebSocket(
          {
            type: WS_TYPE.WS_REQ_QUERY,
            object: {
              sessionId: this.tab.sessionId,
              queryType: WS_REQ_QUERY_TYPE.TX_STATUS,
              levels: this.browseGenLevelsData(this.tab.node),
              rdbAutoCommit: this.tab.autoCommit,
              rdbReadOnly: this.tab.readOnly,
              rdbIsolation: isolation
            }
          },
          { message: this.onmessage }
        );
      });
    },
    handleSetTx(autoCommit) {
      this.handleInterceptor('autoCommit', () => {
        sendWebSocket(
          {
            type: WS_TYPE.WS_REQ_QUERY,
            object: {
              sessionId: this.tab.sessionId,
              queryType: WS_REQ_QUERY_TYPE.TX_STATUS,
              levels: this.browseGenLevelsData(this.tab.node),
              rdbAutoCommit: autoCommit,
              rdbReadOnly: this.tab.readOnly,
              rdbIsolation: this.tab.isolation
            }
          },
          { message: this.onmessage }
        );
      });
    },
    async onRun(type = 'run', asyncForm) {
      this.storeQueryTabs();
      if (window._hmt && this.isDesktop) {
        window._hmt.push(['_trackEvent', 'execute sql', 'uid', 'personal']);
      }
      const selection = this.monacoEditor.getSelection();
      const hasSelection =
        selection.selectionStartLineNumber !== selection.positionLineNumber || selection.selectionStartColumn !== selection.positionColumn;
      let selectedSql = this.monacoEditor.getModel().getValueInRange(selection);
      let position = selection;
      if (!hasSelection) {
        const targetState = this.$refs.editor?.getExecutableSqlTargetState?.();
        if (!targetState?.hasStatement) {
          this.showNoExecutableSqlError();
          return;
        }
        const target = this.$refs.editor.getCurrentSqlTarget();
        selectedSql = target.sql;
        position = target.position || selection;
      }

      if (!selectedSql || !selectedSql.trim()) {
        this.showNoExecutableSqlError();
        return;
      }

      this.position = position;
      switch (type) {
        case 'run':
          await this.handleRun(selectedSql, position);
          break;
        case 'plan':
          this.handleInterceptor('explain', async () => {
            await this.handlePlan(selectedSql, position);
          });
          break;
        case 'ticket':
          this.handleRunTicket(selectedSql);
          break;
        default:
          break;
      }
      // this.analysisSplit(selectedSql);
    },
    async handleRun(selectedSql, position, force = false) {
      this.tab.running = true;
      this.tab.result.active = 'message';
      this.tab.currentQueryType = 'query';
      const wsData = {
        type: WS_TYPE.WS_REQ_QUERY,
        object: {
          force,
          basicCodeLine: Math.min(position.selectionStartLineNumber, position.positionLineNumber),
          basicCodeColumn: Math.min(position.selectionStartColumn, position.positionColumn),
          queryString: selectedSql,
          queryArgs: [],
          sessionId: this.tab.sessionId,
          queryType: WS_REQ_QUERY_TYPE.REQUEST_QUERY,
          levels: this.browseGenLevelsData(this.tab.node),
          rdbAutoCommit: this.tab.autoCommit,
          rdbReadOnly: this.tab.readOnly,
          rdbIsolation: this.tab.isolation
        }
      };

      this.handleCloseModal();

      this.tab.cost.popList = [
        {
          icon: 'clock-circle',
          text: '准备查询，等待...'
        },
        {
          icon: 'clock-circle',
          text: '执行查询，等待...'
        },
        {
          icon: 'clock-circle',
          text: '接收结果，等待...'
        }
      ];
      this.tab.cost.popIndex = 0;
      sendWebSocket(wsData, {
        open: this.onopen,
        message: this.onmessage,
        error: this.onerror,
        close: this.onclose
      });
    },
    async handlePlan(selectedSql, position, force = false) {
      this.tab.running = true;
      this.tab.result.active = 'message';
      this.tab.currentQueryType = 'plan';
      const wsData = {
        type: WS_TYPE.WS_REQ_QUERY,
        object: {
          force,
          basicCodeLine: Math.min(position.selectionStartLineNumber, position.positionLineNumber),
          basicCodeColumn: Math.min(position.selectionStartColumn, position.positionColumn),
          sessionId: this.tab.sessionId,
          queryType: WS_REQ_QUERY_TYPE.REQUEST_PLAN,
          levels: this.browseGenLevelsData(this.tab.node),
          queryString: selectedSql,
          queryArgs: [],
          rdbAutoCommit: this.tab.autoCommit
        }
      };

      this.handleCloseModal();

      this.tab.cost.popList = [
        {
          icon: 'clock-circle',
          text: '准备查询，等待...'
        },
        {
          icon: 'clock-circle',
          text: '执行查询，等待...'
        },
        {
          icon: 'clock-circle',
          text: '接收结果，等待...'
        }
      ];
      this.tab.cost.popIndex = 0;
      sendWebSocket(wsData, {
        open: this.onopen,
        message: this.onmessage,
        error: this.onerror,
        close: this.onclose
      });
    },
    handleRunTicket(selectedSql) {
      this.goTicketPage(selectedSql);
    },
    formatSql() {
      this.$refs.editor.formatSql();
    },
    handleInterceptor(confName, callback, focus = false, silence = false) {
      if (this.tab.support[confName].conf === 'No') {
        this.$Message.error({
          background: true,
          content: this.$t('cao-zuo-bu-zhi-chi')
        });
        return;
      }

      if (silence) {
        callback();
        return;
      }

      if (this.tab.support[confName].conf === 'Hint') {
        if (!this.tab.autoCommit || focus) {
          this.$Modal.confirm({
            title: this.tab.support[confName].hint,
            onOk: () => {
              callback.apply(this);
            }
          });
          return;
        }
      }

      callback();
    },
    //
    setEditorInstance(editor) {
      this.monacoEditor = editor;
    },
    setSql(sql) {
      console.log('setSql', sql);
      const position = this.monacoEditor.getPosition();
      let text = sql;
      if (position.column !== 1) {
        text = `\n${sql}`;
      } else {
        text = `${sql}\n`;
      }

      const range = new monaco.Range(position.lineNumber, position.column, position.lineNumber, position.column);
      this.monacoEditor.executeEdits('', [
        {
          range,
          text
        }
      ]);
      this.monacoEditor.focus();

      let selectRange;
      if (position.column !== 1) {
        // 插入了 \n${sql}，SQL 从新行第 1 列开始
        selectRange = new monaco.Range(position.lineNumber + 1, 1, position.lineNumber + 1, 1 + sql.length);
      } else {
        // 插入了 ${sql}\n，SQL 从当前行第 1 列开始
        selectRange = new monaco.Range(position.lineNumber, 1, position.lineNumber, 1 + sql.length);
      }

      this.monacoEditor.setSelection(selectRange);
    },
    getColumnsList(res) {
      // const list = [];
      const list = [
        {
          type: 'seq',
          width: 50,
          title: this.$t('xu-hao'),
          fixed: 'left',
          className: 'seq-content',
          headerClassName: 'seq-header'
        }
      ];
      // 这边根据列和值来算width，值取一批的最长值，另需要设置最小宽度
      if (res.columnList) {
        res.columnList.forEach((item, index) => {
          const minWidth = 100;
          let width = 0;
          if (width === 0) {
            width = item.length * 10;
          }
          if (width < minWidth) {
            width = minWidth;
          }

          list.push({
            field: item,
            title: item,
            width,
            showOverflow: true,
            headerClassName: 'cell-header'
          });
        });
      }
      return list;
    },
    onclose() {},
    onmessage(data) {
      try {
        const queryData = JSON.parse(data);
        let currentTab = this.tab;
        if (this.tab.sessionId !== queryData.object.sessionId) {
          currentTab = null;
          this.tabs.forEach((tab) => {
            if (tab.sessionId === queryData.object.sessionId) {
              currentTab = tab;
            }
          });
        }

        // 对于导出事件，不需要依赖特定的tab
        if (!currentTab && queryData.type !== WS_TYPE.WS_RES_EXPORT_EVENT) {
          return;
        }
        // 处理 ResultSetMeta - 包含列信息等元数据
        if (queryData.object.resultType === 'ResultSetMeta') {
          const metaData = queryData.object;
          // 生成列配置
          metaData.columnListSeq = this.getColumnsList(metaData);
          // 读取分页模式，默认为 PAGE_FULL（前端分页）
          const receiveMode = metaData.receiveMode || 'PAGE_FULL';
          metaData.receiveMode = receiveMode;
          // 保存查询类型（计划或普通查询）
          metaData.queryType = currentTab.currentQueryType || 'query';

          // 初始化结果集对象（还没有数据）
          const len = currentTab.result.list.length;
          metaData.showIndex = len ? currentTab.result.list[len - 1].showIndex + 1 : 1;
          // 结果tab初始化exportState
          metaData.exportState = {
            exporting: false,
            percent: 0,
            downloadFile: {
              trackId: '',
              file: '',
              format: ''
            },
            cacheFile: ''
          };

          // 根据分页模式初始化不同的字段
          if (receiveMode === 'PAGINATED') {
            // 后端分页模式
            metaData.page = 1;
            metaData.size = 30; // PAGINATED 模式固定为 30
            metaData.total = 0; // 初始为 0，通过 ResultSetRows 更新
            metaData.fetchCount = 0; // 已获取的总行数
            metaData.data = null;
            metaData.dataArr = []; // 存储已加载的页面数据
            metaData.showData = [];
            metaData.rowSet = null;
            metaData.pageCache = {}; // 缓存已加载的页面 {page: data}
          } else if (receiveMode === 'STREAM') {
            // 流式传输模式：数据持续累加，只显示最新 30 条
            metaData.page = 1;
            metaData.size = 30;
            metaData.total = 0;
            metaData.fetchCount = 0; // 已获取的总行数（从 ResultSet 的 fetchCount 更新）
            metaData.data = null;
            metaData.dataArr = [];
            metaData.showData = [];
            metaData.rowSet = null;
            metaData.streamData = []; // 流式数据存储（所有数据）
          } else {
            // PAGE_FULL 模式（前端分页，原有逻辑）
            metaData.page = 1;
            metaData.size = 50;
            metaData.total = 0;
            metaData.data = null;
            metaData.dataArr = [];
            metaData.showData = [];
            metaData.rowSet = null;
          }

          // 添加到结果列表
          currentTab.result.list.push(metaData);
          currentTab.result.active = metaData.resultId;
        }
        // 处理 ResultSetRows - PAGINATED 模式下更新已获取的行数
        if (queryData.object.resultType === 'ResultSetRows') {
          const { resultId, fetchCount } = queryData.object;
          const existingResult = currentTab.result.list.find((item) => item.resultId == resultId);
          if (existingResult && existingResult.receiveMode === 'PAGINATED') {
            existingResult.fetchCount = fetchCount || 0;
            existingResult.total = fetchCount || 0;
          }
        }

        // 处理 ResultSet - 包含具体的行数据
        if (queryData.object.resultType === 'ResultSet') {
          const { rowSet, resultId } = queryData.object;

          // 临时兼容：如果 rowSet 长度为 0，则不处理这个 ResultSet（后续会删除）
          if (!rowSet || rowSet.length === 0) {
            return;
          }

          // 根据 resultId 找到对应的结果集（应该已经通过 ResultSetMeta 创建）
          const existingResult = currentTab.result.list.find((item) => item.resultId == resultId);
          const { columnList, receiveMode } = existingResult;
          const list = [];

          if (rowSet && columnList) {
            rowSet.forEach((item) => {
              const currentRow = {};
              // 新数据结构使用 data 字段，旧结构使用 row 字段（向后兼容）
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

          // 根据分页模式处理数据
          if (receiveMode === 'PAGINATED') {
            // 后端分页模式：只处理第一页数据
            existingResult.showData = list;
            existingResult.pageCache[1] = list; // 缓存第一页
            existingResult.page = 1;
            // 保存原始 rowSet 数据，用于获取 moreSize 等信息
            if (!existingResult.rowSetCache) {
              existingResult.rowSetCache = {};
            }
            existingResult.rowSetCache[1] = rowSet; // 保存第一页的原始数据
          } else if (receiveMode === 'STREAM') {
            // 流式传输模式：数据持续累加，只显示最新 30 条
            existingResult.streamData = existingResult.streamData || [];
            existingResult.streamData.push(...list);

            // 保存原始 rowSet 数据，用于获取 moreSize 等信息
            if (!existingResult.rowSetCache) {
              existingResult.rowSetCache = {};
            }
            // STREAM 模式下，rowSet 数据追加到流中
            if (!existingResult.rowSetStream) {
              existingResult.rowSetStream = [];
            }
            existingResult.rowSetStream.push(...rowSet);

            // 更新 fetchCount 和 total（从 ResultSet 中获取）
            if (queryData.object.fetchCount !== undefined) {
              existingResult.fetchCount = queryData.object.fetchCount;
              existingResult.total = queryData.object.fetchCount;
            }

            // 只保留最新的 30 条数据用于显示
            const displayCount = 30;
            if (existingResult.streamData.length > displayCount) {
              existingResult.showData = existingResult.streamData.slice(-displayCount);
              // 同时保留对应的原始数据索引
              const startIndex = existingResult.streamData.length - displayCount;
              existingResult.rowSetStream = existingResult.rowSetStream.slice(startIndex);
            } else {
              existingResult.showData = existingResult.streamData;
            }
          } else {
            // PAGE_FULL 模式：前端分页，原有逻辑
            const dataArr = chunk(list, 50);
            Object.assign(existingResult, {
              total: rowSet ? rowSet.length : 0,
              data: rowSet,
              rowSet: rowSet,
              dataArr,
              showData: rowSet && dataArr.length > 0 ? dataArr[0] : []
            });
          }
        }

        if (queryData.object.resultType === 'Done') {
          this.storeQueryTabs();
          currentTab.running = false;
        }

        if (queryData.object.resultType === 'CancelDone') {
          currentTab.stopping = false;
        }

        if (queryData.object.resultType === 'QueryScript') {
          currentTab.executeInfo.push({
            time: queryData.time,
            ...queryData.object
          });
          this.$bus.emit('consoleMessageAppend', currentTab);
        }

        if (queryData.object.resultType === 'Message') {
          if (queryData.object.entities && queryData.object.entities.length) {
            const entity = queryData.object.entities[0];
            // 当 level 为 Error 时，且 original 为 RecoveryStatus 时，使用 websocket 返回的 message 设置 tab.msgContent
            if (entity.message && entity.level === 'Error' && queryData.object?.original === 'RecoveryStatus') {
              currentTab.msgContent = entity.message;
              currentTab.msgFromWs = true;
              currentTab.connected = false;
            }

            if (entity.mode === 'Hint') {
              currentTab.message.show = true;
              currentTab.message.text = formatError(entity.message);
              currentTab.message.type = entity.level;
            } else {
              currentTab.executeInfo.push({
                time: queryData.time,
                ...entity
              });
              if (entity.level === 'Error') {
                currentTab.result.active = 'message';
              }
              this.$bus.emit('consoleMessageAppend', currentTab);
            }
          }
        }

        if (queryData.object.resultType === 'RuleCheck') {
          if (!currentTab.executeInfo) {
            currentTab.executeInfo = [];
          }
          currentTab.executeInfo.push({
            time: queryData.time,
            level: 'Error',
            warnLevel: queryData.object.warnLevel,
            message: '规则校验失败',
            queryBody: queryData.object.queryBody,
            ruleList: queryData.object.message
          });

          currentTab.cost.popIndex = -1;
          currentTab.cost.popList = [];
          this.currentSql = queryData.object.queryBody;
          this.currentWarnLevel = queryData.object.warnLevel;
          if (currentTab.sessionId === this.tab.sessionId) {
            this.handleShowUnPassedRuleListModal(currentTab.executeInfo.length - 1);
          }
          this.$bus.emit('consoleMessageAppend', currentTab);
        }

        if (queryData.object.resultType === 'Cost') {
          currentTab.cost = queryData.object;
          let popIndex = -1;
          let popList = [];
          switch (queryData.object.step) {
            case 'Prepare':
              popList = [
                {
                  icon: 'loading',
                  theme: 'outlined',
                  text: '准备查询，执行中...',
                  color: 'gray'
                },
                {
                  icon: 'clock-circle',
                  theme: 'filled',
                  text: '执行查询，等待...',
                  color: 'gray'
                },
                {
                  icon: 'clock-circle',
                  theme: 'filled',
                  text: '接收结果，等待...',
                  color: 'gray'
                }
              ];
              popIndex = 0;
              break;
            case 'Query':
              popList = [
                {
                  icon: 'check-circle',
                  theme: 'filled',
                  text: `准备查询，耗时${queryData.object.preCost}毫秒`,
                  color: 'green'
                },
                {
                  icon: 'loading',
                  theme: 'outlined',
                  text: '执行查询，等待...',
                  color: 'gray'
                },
                {
                  icon: 'clock-circle',
                  theme: 'filled',
                  text: '接收结果，等待...',
                  color: 'gray'
                }
              ];
              popIndex = 1;
              break;
            case 'Receive':
              popList = [
                {
                  icon: 'check-circle',
                  theme: 'filled',
                  text: queryData.object.preCost === -1 ? '准备查询' : `准备查询，耗时${queryData.object.preCost}毫秒`,
                  color: 'green'
                },
                {
                  icon: 'check-circle',
                  theme: 'filled',
                  text: queryData.object.queryCost === -1 ? '执行查询' : `执行查询，耗时${queryData.object.queryCost}毫秒`,
                  color: 'green'
                },
                {
                  icon: 'clock-circle',
                  theme: 'filled',
                  text: '接收结果，执行中...'
                }
              ];
              popIndex = 2;
              break;
            case 'Finish':
              popList = [
                {
                  icon: 'check-circle',
                  theme: 'filled',
                  text: queryData.object.preCost === -1 ? '准备查询' : `准备查询，耗时${queryData.object.preCost}毫秒`,
                  color: 'green'
                },
                {
                  icon: 'check-circle',
                  theme: 'filled',
                  text: queryData.object.queryCost === -1 ? '执行查询' : `执行查询，耗时${queryData.object.queryCost}毫秒`,
                  color: 'green'
                },
                {
                  icon: 'check-circle',
                  theme: 'filled',
                  text: queryData.object.rcvCost === -1 ? '接收结果' : `接收结果，耗时${queryData.object.rcvCost}毫秒`,
                  color: 'green'
                }
              ];
              popIndex = 2;
              break;
            default:
              break;
          }
          currentTab.cost.popIndex = popIndex;
          currentTab.cost.popList = popList;
        }

        if (queryData.object.resultType === 'ClearHintMessage') {
          currentTab.message.show = false;
          currentTab.message.text = '';
        }

        if (queryData.object.resultType === 'Status') {
          currentTab.autoCommit = queryData.object.rdbAutoCommit;
          currentTab.readOnly = queryData.object.rdbReadOnly;
          currentTab.isolation = queryData.object.rdbTxIsolation;
        }

        // 导出信息可能在 ResultSetMeta 或 ResultSet 中，统一处理
        if (queryData.object.resultType === 'ResultSetMeta' || queryData.object.resultType === 'ResultSet') {
          const resultData =
            queryData.object.resultType === 'ResultSetMeta'
              ? queryData.object
              : currentTab.result.list.find((item) => item.resultId === queryData.object.resultId) || queryData.object;
          if (resultData) {
            this.$bus.emit(EVENT_BUS_NAME_LIST.GET_RESULT_EXPORT_INFO, resultData);
          }
        }

        if (queryData.type === WS_TYPE.WS_SYS_STATUS) {
          if (queryData.object.serviceReady) {
            this.handleGetDsSetting();
          }
        }

        if (queryData.type === WS_TYPE.WS_RES_LICENSE_ERROR) {
          this.$bus.emit(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL, queryData.object.msgContent);
          currentTab.running = false;
          currentTab.stopping = false;
        }

        if (queryData.type === WS_TYPE.WS_RES_EXPORT_EVENT) {
          this.$bus.emit(EVENT_BUS_NAME_LIST.WS_RES_EXPORT_EVENT, queryData.object);
        }
      } catch (e) {
        console.error(e);
      }
    },
    onerror() {},
    onopen() {},
    handleCloseError() {
      this.tab.message.show = false;
    }
  }
};
</script>
<style lang="less">
.sql-viewer {
  position: relative;

  .editor-resize {
    position: absolute;
    z-index: 3;
    left: 0;
    bottom: -3px;
    width: 100%;
    background: #fff;
    cursor: row-resize;
    //border-bottom: 1px solid #ddd;
    height: 6px;
    background: rgba(0, 0, 0, 0);
  }

  .message {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 10px 12px;
    z-index: 3;
    max-height: 200px;
    overflow: auto;
    display: flex;
    justify-content: space-between;
    align-items: center;

    &.Error {
      background: #ffdcdc;
    }

    &.Warn {
      background: #fffddc;
    }

    &.Info {
      background: #dedede;
    }
  }
}

.lines-cell {
  display: block;
  width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
