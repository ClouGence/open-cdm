<template>
  <div class="sql-home-container sql-workspace" :class="{ 'sql-workspace--compact': compactWorkspace }">
    <AppPageTabs v-if="compactWorkspace" v-model="mobileView" :tabs="mobileTabs" @change="selectMobileWorkspace" />
    <div class="content">
      <data-source-tree
        v-show="!compactWorkspace || mobileView === 'datasources'"
        ref="dataSourceTree"
        :current-tab="currentTab"
        :tree-data="treeData"
        :loading="dataSourceLoading"
        :list-levels="listLevels"
        :detail-levels="detailLevels"
        :list-leaf="listLeaf"
        :get-node-data="getDataSourceNodeData"
        :get-node-by-key="getNodeByKey"
        :get-data-source-list="getDataSourceData"
        :handle-add-tab="handleAddTab"
        :set-loading="setLoading"
        :refresh-tab-select-options="refreshTabSelectOptions"
        :get-workspace-connection="getWorkspaceConnection"
        :handle-workspace-connection="handleWorkspaceConnection"
        @sidebar-state-change="handleDataSourceSidebarStateChange"
      />
      <div class="query-container" v-show="!compactWorkspace || mobileView !== 'datasources'">
        <div class="content">
          <SqlLoadingSkeleton v-if="restoringWorkspace && !tabs.length" variant="workspace" />
          <div v-show="!restoringWorkspace || tabs.length" class="tab-wrap">
            <div class="sql-sidebar-toggle-slot" :class="{ 'sql-sidebar-toggle-slot--visible': dataSourceSidebarHidden }">
              <transition name="sql-sidebar-toggle">
                <button
                  v-if="dataSourceSidebarHidden"
                  type="button"
                  class="sql-tab-sidebar-toggle"
                  :aria-label="$t('sql-expand-datasource-sidebar')"
                  :title="$t('sql-expand-datasource-sidebar')"
                  @click="handleExpandDataSourceSidebar"
                >
                  <DoubleRightOutlined aria-hidden="true" />
                </button>
              </transition>
            </div>
            <div class="sql-tabs-style" v-if="tabs.length">
              <a-tabs type="card" v-model:activeKey="active" @tabClick="handleChangeTab">
                <a-tab-pane v-for="(tab, index) in tabs" :key="tab.key">
                  <template #tab>
                    <div @contextmenu.prevent.stop="onContextmenu($event, tab)">
                      <div
                        v-if="tab.isEditing"
                        style="position: absolute; width: 8px; height: 8px; border-radius: 50%; background: var(--primary-color); left: 13px"
                      />
                      <cc-svg-icon name="TABLE" v-if="tab.icon === 'Table'" style="display: inline-block" />
                      <CustomIcon :type="tab.icon" v-else />
                      <Tooltip :content="getTabDisplayTitle(tab)" transfer placement="top">
                        <span class="tab-title-text">{{ getTabDisplayTitle(tab) }}</span>
                      </Tooltip>
                      <CustomIcon
                        class="close-icon"
                        type="icon-v2-close2"
                        hoverStyle
                        customStyle="radius-hover"
                        @click.stop="handleCloseTab(tab.key)"
                      />
                    </div>
                  </template>
                </a-tab-pane>
                <template #rightExtra>
                  <a-dropdown trigger="click" placement="bottom" overlayClassName="sql-tab-dropdown" v-if="tabs.length">
                    <CustomIcon type="icon-v2-ArrowDown" hoverStyle customStyle="icon-v2-hover" />
                    <template #overlay>
                      <a-menu :selectedKeys="[active]">
                        <a-menu-item v-for="tab in tabs" :key="tab.key" :name="tab.key" @click="handleChangeTab(tab.key)">
                          <div class="dropdown-item">
                            <CustomIcon :type="tab.icon" class="dropdown-item-icon" />
                            <Tooltip :content="getTabDisplayTitle(tab)" transfer placement="top">
                              <span class="dropdown-item-title truncate">{{ getTabDisplayTitle(tab) }}</span>
                            </Tooltip>
                            <span class="dropdown-item-desc truncate">
                              <template v-if="tab.node?.INSTANCE">
                                [{{ tab.node.INSTANCE.attr.dsInstanceDesc || tab.node.INSTANCE.attr.dsInstance }}]
                              </template>
                              <template v-else>[{{ $t('sql-query-unconnected') }}]</template>
                            </span>
                            <div class="dropdown-item-close">
                              <CustomIcon type="icon-v2-close2" customStyle="icon-v2-hover" hoverStyle @click.stop="handleCloseTab(tab.key)" />
                            </div>
                          </div>
                        </a-menu-item>
                      </a-menu>
                    </template>
                  </a-dropdown>
                </template>
              </a-tabs>
            </div>
          </div>
          <!-- Empty Status -->
          <div v-if="!restoringWorkspace && !tabs.length" class="empty-state-container">
            <SqlEmptyState :has-data-source="treeData.length > 0" />
          </div>

          <Transition name="sql-content-reveal">
            <div class="query-content-container" v-if="tabs.length">
              <loading :active.sync="fullLoading" :is-full-page="false"></loading>
              <div v-if="currentTab.type === TAB_TYPE.QUERY && globalDsSetting[currentTab.dsType]" class="query-content">
                <div class="query">
                  <TableList
                    v-if="currentTab.connected && currentTab.node && currentTab.leafGroup?.length"
                    v-show="!compactWorkspace || mobileView === 'objects'"
                    ref="tableList"
                    :get-node-data="getTableNodeData"
                    :handle-add-tab="handleAddTab"
                    :list-leaf="listLeaf"
                    :table-list-loading="tableListLoading || sessionLoading || restoringWorkspace"
                    :currentTab="currentTab"
                    :handle-query-table="handleQueryTable"
                    :rdb-object-detail="rdbObjectDetail"
                  />
                  <SqlLoadingSkeleton
                    v-else-if="currentTab.node && ['connecting', 'reconnecting'].includes(currentTab.workspaceConnectionState)"
                    v-show="!compactWorkspace || mobileView === 'objects'"
                    variant="objects"
                    class="table-list-container"
                  />
                  <div
                    class="query-editor-container"
                    :class="{
                      'query-editor-container--results-collapsed': currentTab.result.collapsed,
                      'query-editor-container--results-maximized': currentTab.result.maximized
                    }"
                    v-show="!compactWorkspace || mobileView === 'query'"
                  >
                    <div class="layout-content-main">
                      <SqlViewer
                        v-if="currentTab.support"
                        ref="sqlViewer"
                        :handleGetDsSetting="handleGetDsSetting"
                        :createSession="createSession"
                        :storeQueryTabs="storeQueryTabs"
                        :tab="currentTab"
                        :tabs="tabs"
                        :completion-data="completionData"
                        :rdb-object-detail="rdbObjectDetail"
                        :handle-click-ds-status-icon="handleClickDsStatusIcon"
                        @switch-tab="handleChangeTab"
                        @workspace-connection-error="handleWorkspaceConnectionError"
                        @workspace-connection-success="handleWorkspaceConnectionSuccess"
                        @metadata-changed="refreshQueryMetadata"
                        @open-script="handleOpenSqlScript"
                      >
                        <template #connection-context>
                          <div v-if="currentTab.connected && currentTab.node" class="query-schema-select__content">
                            <a-select
                              v-if="currentTab.selectOptions && currentTab.node.levels[currentTab.node.levels.length - 1] !== 'INSTANCE'"
                              class="schema-select-style"
                              v-model:value="currentTab.selectValue"
                              show-search
                              size="small"
                              :options="currentTab.selectOptions || []"
                              @select="handleChangeSchema"
                            ></a-select>
                            <CustomIcon
                              class="query-connection-icon"
                              :type="currentTab.dsType"
                              :instance-type="currentTab.node.INSTANCE.attr.dsDeployType"
                              size="14px"
                              aria-hidden="true"
                            />
                            <div class="query-connection-label">@{{ currentTab.node.INSTANCE.attr.dsHost }}</div>
                          </div>
                        </template>
                      </SqlViewer>
                    </div>
                    <div ref="result" class="result-wrapper">
                      <Result :id="`result_${currentTab.key}`" :ref="`result_`" :resultList="currentTab.resultList" :tab="currentTab" />
                    </div>
                  </div>
                </div>
              </div>
              <div v-if="currentTab.type !== TAB_TYPE.QUERY && !currentTab.connected" class="workspace-connection-notice" role="status">
                {{ currentTab.msgContent || $t('sql-connection-disconnected') }}
                <Button v-if="!getWorkspaceConnection(currentTab.dsId).busy" @click="handleClickDsStatusIcon">{{ $t('sql-connection-open') }}</Button>
              </div>
              <div v-if="currentTab.type === TAB_TYPE.STRUCT" class="query-content" :inert="!currentTab.connected">
                <StructView
                  :handleClickDsStatusIcon="handleClickDsStatusIcon"
                  :tab="currentTab"
                  :set-active-key="setActiveKey"
                  :handle-refresh-tables="handleRefreshTables"
                  :store-query-tabs="storeQueryTabs"
                  ref="struct-view"
                />
              </div>
              <div v-if="currentTab.type === TAB_TYPE.DATA" class="query-content" :inert="!currentTab.connected">
                <TableDataView :tab="currentTab" ref="data-view" :set-loading="setLoading" :handle-click-ds-status-icon="handleClickDsStatusIcon" />
              </div>
            </div>
          </Transition>
        </div>
      </div>
    </div>
    <!-- Hide, avoid double-counting. -->
    <CCModal v-model="showConnectedModal" :title="$t('cuo-wu')" :zIndex="1100">
      <div class="connection-error-content">
        <CustomIcon
          class="connection-error-icon"
          :type="connectedInstance.INSTANCE.attr.dsType"
          :instance-type="connectedInstance.INSTANCE.attr.dsDeployType"
          size="20px"
          aria-hidden="true"
        />
        <span>{{ $t('sql-connection-error-source', { name: connectedInstance.INSTANCE.name }) }}</span>
      </div>
      <template #footer>
        <Button @click="showConnectedModal = false">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import appLogger from '@/utils/logger';
import Loading from 'vue-loading-overlay';
import { useMediaQuery } from '@vueuse/core';
import AppPageTabs from '@/components/layout/AppPageTabs.vue';
import 'vue-loading-overlay/dist/css/index.css';
import { cloneDeep as deepClone } from '@/utils/lodash';
import { Modal } from 'ant-design-vue';
import { mapGetters, mapMutations, mapState } from 'vuex';
import dayjs from 'dayjs';
import { isCancel } from 'axios';
import SqlViewer from '@/views/sql/components/SqlViewer';
import Result from '@/views/sql/components/Result';
import './style/index.less';
import { ACTION_TYPE, BIZ_TYPE, TAB_TYPE } from '@/const';
import TableList from '@/views/sql/components/TableList';
import StructView from '@/views/sql/components/StructView';
import DataSourceTree from '@/views/sql/components/DataSourceTree';
import SqlEmptyState from '@/views/sql/components/SqlEmptyState';
import SqlLoadingSkeleton from '@/views/sql/components/SqlLoadingSkeleton';
import { clearAllPending } from '@/services/http/cancelRequest';
import TableDataView from '@/views/sql/components/TableDataView.vue';
import browseMixin from '@/mixins/browseMixin';
import { UPDATE_EDITOR_SET } from '@/store/mutationTypes';
import { ASYNC_TASK_STATUS, SOCKET_TYPE, noStruct, WS_REQ_QUERY_TYPE, WS_TYPE } from '@/utils';
import { sendWebSocket } from '@/services/socket';
import sqlMixin from '@/mixins/sqlMixin';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import { nanoid } from 'nanoid';
import { TabManager } from '@/views/sql/tabManager';
import CustomIcon from '@/components/function/CustomIcon.vue';
import ContextMenu from '@imengyu/vue3-context-menu';
import { DoubleRightOutlined } from '@ant-design/icons-vue';

const TAB_ACTION = {
  DELETE_ALL: 'DELETE_ALL',
  DELETE_OTHER_ALL: 'DELETE_OTHER_ALL',
  DELETE_LEFT_ALL: 'DELETE_LEFT_ALL',
  DELETE_RIGHT_ALL: 'DELETE_RIGHT_ALL'
};

export default {
  name: 'Sql',
  mixins: [browseMixin, sqlMixin],
  components: {
    AppPageTabs,
    CustomIcon,
    DoubleRightOutlined,
    TableDataView,
    DataSourceTree,
    StructView,
    SqlViewer,
    Result,
    TableList,
    SqlEmptyState,
    SqlLoadingSkeleton,
    Loading
  },
  setup() {
    return { compactWorkspace: useMediaQuery('(max-width: 767px)') };
  },
  data() {
    return {
      mobileView: 'datasources',
      needGetDataFromIndexedDb: true,
      tabManager: null,
      contextData: null,
      connectedInstance: {},
      showConnectedModal: false,
      showAsyncTaskList: false,
      asyncTaskList: [],
      asyncTaskObj: {},
      scrollTop: 0,
      treeInstance: null,
      tableListLoading: false,
      tableListRequestId: 0,
      rdbObjectDetailLoading: true,
      completionData: {},
      TAB_TYPE,
      active: '',
      treeData: [],
      dataSourceLoading: true,
      restoringWorkspace: true,
      newMode: true,
      TAB_ACTION,
      dataSourceSidebarHidden: false,
      dataSourceWidth: 250,
      preDataSourceWidth: 0,
      ACTION_TYPE,
      observer: new IntersectionObserver(this.infiniteScroll),
      search: '',
      limit: 50,
      loading: false,
      sessionLoading: false,
      createOrDropLoading: false,
      tabs: [],
      preRequestData: {},
      currentMethod: '',
      currentTab: {
        schema: '',
        executeInfoScrollDown: true,
        selectOptions: []
      },
      showCount: 20,
      spinning: false,
      noStruct,
      datasourceList: [],
      hasDatasource: false,
      pendingCloseTabKeys: [],
      bulkCloseFocusKey: '',
      scriptLoadSequence: 0,
      workspaceConnections: {},
      workspaceBlockedDsIds: [],
      sessionRequests: new Map(),
      workspaceUnmounted: false
    };
  },
  beforeRouteEnter(to, from, next) {
    appLogger.debug(to, from);
    next((vm) => {
      if (from.name) {
        vm.needGetDataFromIndexedDb = true;
      }
    });
  },
  beforeRouteLeave(to, from, next) {
    this.storeActiveTab();
    next();
  },
  async mounted() {
    this.tabManager = new TabManager();
    // if (!this.needGetDataFromIndexedDb) {
    //   console.log('clear indexeddb');
    //   this.tabManager.tabTable.where('uid').equals(this.userInfo.uid).delete();
    // }
    this.workspaceBlockedDsIds = JSON.parse(localStorage.getItem(`clouddm_workspace_disconnected_${this.userInfo.uid}`) || '[]');
    document.addEventListener('visibilitychange', this.handleWorkspaceVisibility);
    this.listDockTask().catch((error) => appLogger.error('Load async tasks failed', error));
    try {
      await this.getDataSourceData();
      await this.getLocalQueryData();
    } finally {
      this.restoringWorkspace = false;
    }
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_ASYNC_EVENT, this.handleAsyncEvent);
    this.$bus.on(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_CLOSE, this.handleSocketConnectionClose);
    this.$bus.on(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_OPEN, this.handleGetDsSetting);
    this.$bus.on(EVENT_BUS_NAME_LIST.SET_DATA_SOURCE_STATUS, (data) => this.setInstanceErrorIcon(data));

    this.currentTab.executeInfoScrollDown = true;
  },
  computed: {
    mobileTabs() {
      return [
        { name: 'datasources', label: this.$t('sql-mobile-datasources') },
        { name: 'objects', label: this.$t('sql-mobile-objects'), disabled: !this.currentTab.connected || !this.currentTab.leafGroup?.length },
        { name: 'query', label: this.$t('sql-mobile-query'), disabled: !this.tabs.length }
      ];
    },
    fullLoading() {
      return this.loading;
    },
    ...mapState(['globalDsSetting', 'userInfo', 'socket']),
    ...mapGetters(['isDesktop', 'getNodeType', 'getLeafGroup', 'getLevels', 'getLeafExpand'])
  },
  watch: {
    compactWorkspace(compact) {
      if (compact) this.selectMobileWorkspace(this.tabs.length ? 'query' : 'datasources');
    },
    'currentTab.key'() {
      if (this.compactWorkspace) this.selectMobileWorkspace(this.tabs.length ? 'query' : 'datasources');
    }
  },
  methods: {
    selectMobileWorkspace(view) {
      this.mobileView = view;
      if (view === 'datasources' && this.dataSourceSidebarHidden) {
        this.$nextTick(() => this.handleExpandDataSourceSidebar());
      }
    },
    handleWorkspaceVisibility() {
      if (document.visibilityState === 'visible') this.handleGetDsSetting();
    },
    getWorkspaceConnection(dsId) {
      if (!this.workspaceConnections[dsId]) {
        this.workspaceConnections[dsId] = {
          status: 'disconnected',
          blocked: this.workspaceBlockedDsIds.includes(dsId),
          busy: false,
          wasConnected: false,
          generation: 0
        };
      }
      return this.workspaceConnections[dsId];
    },
    setWorkspaceConnectionState(dsId, status, message = '') {
      const connection = this.getWorkspaceConnection(dsId);
      connection.status = status;
      if (status === 'connected') connection.wasConnected = true;
      this.tabs
        .filter((tab) => tab.dsId === dsId)
        .forEach((tab) => {
          tab.workspaceConnectionState = status;
          tab.connected = status === 'connected';
          tab.msgContent = message || this.$t(`sql-connection-${status}`);
          tab.msgFromWs = false;
        });
    },
    handleWorkspaceConnectionSuccess(tab) {
      const connection = this.getWorkspaceConnection(tab.dsId);
      if (connection.blocked || connection.busy) return;
      this.setWorkspaceConnectionState(tab.dsId, 'connected');
    },
    handleWorkspaceConnectionError(tab) {
      const connection = this.getWorkspaceConnection(tab.dsId);
      if (connection.blocked || connection.busy) return;
      this.setWorkspaceConnectionState(tab.dsId, 'failed', tab.msgContent);
      tab.msgFromWs = true;
    },
    storeWorkspaceDisconnect(dsId, disconnected) {
      this.workspaceBlockedDsIds = this.workspaceBlockedDsIds.filter((id) => id !== dsId);
      if (disconnected) this.workspaceBlockedDsIds.push(dsId);
      localStorage.setItem(`clouddm_workspace_disconnected_${this.userInfo.uid}`, JSON.stringify(this.workspaceBlockedDsIds));
    },
    getWorkspaceInstance(node) {
      const tree = this.$refs.dataSourceTree;
      const key = `\`${node.ENV.id}\`.\`${node.INSTANCE.id}\``;
      return (
        tree?.handleGetNode(key) || {
          ...node,
          levels: ['ENV', 'INSTANCE'],
          nodeType: 'INSTANCE',
          key,
          title: node.INSTANCE.name,
          children: []
        }
      );
    },
    async stopWorkspaceQuery(tab) {
      if (!tab.running && !tab.stopping) return;
      if (!this.socket.connected) throw new Error(this.$t('sql-connection-stop-unavailable'));
      const stopped = new Promise((resolve, reject) => {
        const unwatch = this.$watch(
          () => tab.running || tab.stopping,
          (running) => {
            if (!running) {
              clearTimeout(timer);
              unwatch();
              resolve();
            }
          }
        );
        const timer = setTimeout(() => {
          unwatch();
          reject(new Error(this.$t('sql-connection-stop-timeout')));
        }, 30000);
      });
      tab.stopping = true;
      sendWebSocket(
        {
          type: WS_TYPE.WS_REQ_QUERY,
          object: {
            sessionId: tab.sessionId,
            queryType: WS_REQ_QUERY_TYPE.CANCEL_QUERY,
            levels: this.browseGenLevelsData(tab.node),
            rdbAutoCommit: tab.autoCommit,
            rdbReadOnly: tab.readOnly,
            rdbIsolation: tab.isolation
          }
        },
        { message: this.$refs.sqlViewer?.onmessage }
      );
      await stopped;
      if (!this.socket.connected) throw new Error(this.$t('sql-connection-stop-unavailable'));
    },
    async handleWorkspaceConnection(action, node, automatic = false, resolve = null) {
      const instance = this.getWorkspaceInstance(node);
      const dsId = instance.INSTANCE.id;
      const connection = this.getWorkspaceConnection(dsId);
      if (connection.busy || this.workspaceUnmounted) return false;
      if (automatic && connection.blocked) return false;
      const tabs = this.tabs.filter((tab) => tab.dsId === dsId);
      const editor = this.$refs['struct-view'] || this.$refs['data-view'];
      if (
        this.currentTab.dsId === dsId &&
        this.currentTab.type !== TAB_TYPE.QUERY &&
        (this.loading || editor?.executeSQLLoading || editor?.createSQLLoading || editor?.initEditorLoading)
      ) {
        this.$Message.warning(this.$t('sql-connection-editor-busy'));
        return false;
      }
      connection.busy = true;
      let started = false;
      let oldSessionsClosed = false;
      try {
        const hasRunningQuery = tabs.some((tab) => tab.running || tab.stopping);
        const hasTransaction = tabs.some((tab) => tab.type === TAB_TYPE.QUERY && tab.sessionId && !tab.autoCommit);
        if (hasRunningQuery || hasTransaction) {
          const confirmed = await new Promise((resolve) =>
            Modal.confirm({
              title: this.$t('sql-connection-confirm-title'),
              content: this.$t('sql-connection-confirm-content', { name: instance.title }),
              okText: this.$t('que-ding'),
              cancelText: this.$t('qu-xiao'),
              onOk: () => resolve(true),
              onCancel: () => resolve(false)
            })
          );
          if (!confirmed) return false;
        }
        this.syncCurrentEditorTextToTab();
        connection.generation++;
        connection.blocked = true;
        this.storeWorkspaceDisconnect(dsId, true);
        started = true;
        const pending = [...this.sessionRequests.entries()].filter(([tab]) => tab.dsId === dsId).map(([, request]) => request);
        this.setWorkspaceConnectionState(dsId, `${action === 'open' ? 'connect' : action}ing`);
        this.storeQueryTabs();
        await Promise.all(pending);
        await Promise.all(tabs.filter((tab) => tab.type === TAB_TYPE.QUERY).map((tab) => this.stopWorkspaceQuery(tab)));
        if (!(await this.closesSession(tabs))) throw new Error(this.$t('sql-connection-close-failed'));
        oldSessionsClosed = true;
        tabs.forEach((tab) => {
          tab.sessionId = '';
          tab.running = false;
          tab.stopping = false;
          tab.message = { text: '', show: false, type: '' };
          tab.cost = { popIndex: -1, popList: [] };
        });
        if (this.workspaceUnmounted) return false;
        if (action === 'disconnect') {
          this.storeWorkspaceDisconnect(dsId, true);
          this.setWorkspaceConnectionState(dsId, 'disconnected');
          this.$refs.dataSourceTree?.collapseWorkspaceInstance(instance);
        } else {
          const dsLevels = this.getLevels(instance.INSTANCE.attr.dsType);
          let accessed = false;
          if (dsLevels.length > instance.levels.length) {
            accessed = await this.listLevels(instance, {}, resolve, null, true, true);
            if (!accessed) throw new Error(this.$t('sql-connection-open-failed'));
          }
          for (const tab of tabs.filter((item) => item.type === TAB_TYPE.QUERY && this.tabs.includes(item))) {
            if (!(await this.createSession(tab, null, true))) throw new Error(this.$t('sql-connection-open-failed'));
            if (!tab.autoCommit) accessed = true;
          }
          if (this.currentTab.dsId === dsId && this.currentTab.type === TAB_TYPE.QUERY) {
            if (!(await this.listLeaf(true, true))) throw new Error(this.$t('sql-connection-open-failed'));
            accessed = true;
          }
          if (this.workspaceUnmounted) return false;
          connection.blocked = false;
          this.storeWorkspaceDisconnect(dsId, false);
          if (accessed) {
            this.setWorkspaceConnectionState(dsId, 'connected');
          } else {
            this.setWorkspaceConnectionState(dsId, 'disconnected');
          }
        }
        this.storeQueryTabs();
        if (!automatic && (action === 'disconnect' || connection.status === 'connected')) {
          this.$Message.success(this.$t(`sql-connection-${connection.status}`));
        }
        return true;
      } catch (error) {
        appLogger.error('Workspace connection operation failed', error);
        if (oldSessionsClosed && action !== 'disconnect') {
          try {
            if (await this.closesSession(tabs))
              tabs.forEach((tab) => {
                tab.sessionId = '';
              });
          } catch (closeError) {
            appLogger.error('Close replacement workspace sessions failed', closeError);
          }
        }
        if (started) {
          this.setWorkspaceConnectionState(dsId, 'failed', error.message || this.$t('sql-connection-open-failed'));
          this.storeQueryTabs();
        }
        this.$Message.error(error.message || this.$t('sql-connection-open-failed'));
        return false;
      } finally {
        connection.busy = false;
      }
    },
    handleDataSourceSidebarStateChange(hidden) {
      this.dataSourceSidebarHidden = hidden;
      if (hidden && this.compactWorkspace && this.mobileView === 'datasources') {
        this.selectMobileWorkspace('datasources');
      }
    },
    handleExpandDataSourceSidebar() {
      this.$refs.dataSourceTree?.handleSwitchHide();
    },
    async handleOpenSqlScript(script) {
      const targetTab = this.currentTab;
      const editor = this.$refs.sqlViewer?.monacoEditor;
      if (!targetTab || targetTab.type !== TAB_TYPE.QUERY || !editor) return;
      const model = editor.getModel();
      const selection = editor.getSelection();
      const version = model.getVersionId();
      const requestSequence = ++this.scriptLoadSequence;
      const res = await this.$services.dmQueryScriptDetail({ data: { scriptId: script.scriptId } });
      if (requestSequence !== this.scriptLoadSequence || this.currentTab !== targetTab || this.$refs.sqlViewer?.monacoEditor !== editor) return;
      if (!res.success) {
        this.$Message.error(res.msg || this.$t('sql-favorites-load-failed'));
        return;
      }
      if (editor.getModel() !== model || model.getVersionId() !== version) {
        this.$Message.warning(this.$t('sql-favorites-load-cancelled-edited'));
        return;
      }
      if (!this.$refs.sqlViewer.insertFavoriteSql(res.data.sqlContent, selection)) return;
      targetTab.lastInsertedScriptId = res.data.scriptId;
      this.storeQueryTabs();
    },
    onContextmenu(event, tab) {
      this.contextData = tab;
      ContextMenu.showContextMenu({
        x: event.x,
        y: event.y,
        theme: 'flat',
        items: [
          {
            label: this.$t('guan-bi-qi-ta-biao-qian'),
            onClick: () => this.handleTabAction(TAB_ACTION.DELETE_OTHER_ALL)
          }
        ],
        event,
        customClass: 'sql-context-menu',
        zIndex: 3,
        minWidth: 176
      });
    },
    handleSocketConnectionClose() {
      this.tabs.forEach((tab) => {
        tab.stopping = false;
        tab.running = false;
        tab.message = {
          text: '',
          show: false,
          type: ''
        };
        tab.cost = {
          popIndex: -1,
          popList: []
        };
      });
    },
    ...mapMutations([UPDATE_EDITOR_SET]),
    getTreeData() {
      return this.$refs.dataSourceTree?.handleGetTreeData?.() || [];
    },
    async getDataSourceData() {
      this.dataSourceLoading = true;
      try {
        await this.listLevels();
        const hasStoredExpandedKeys = this.$refs.dataSourceTree?.hasStoredExpandedKeys;
        if (this.treeData.length) {
          this.hasDatasource = true;
          if (!hasStoredExpandedKeys) {
            await this.listLevels(this.treeData[0], {}, () => {});
          }
        }
      } finally {
        this.dataSourceLoading = false;
      }
    },
    getNodeByKey(tree, curKey, keyField = 'key', node = null) {
      const stack = [];
      for (const item of tree) {
        if (item) {
          stack.push(item);
          while (stack.length) {
            const temp = stack.pop();

            if (temp[keyField] === curKey) {
              node = temp;
              break;
            }

            const children = temp.children || [];
            for (let i = children.length - 1; i >= 0; i--) {
              stack.push(children[i]);
            }
          }
        }
      }
      return node;
    },
    async detailLevels(node, callback, isRefreshCache = false) {
      const connection = node?.INSTANCE && this.getWorkspaceConnection(node.INSTANCE.id);
      if (connection && (connection.blocked || connection.busy)) return;
      const generation = connection?.generation;
      const res = await this.$services.dmBrowseDetailLevels({
        data: {
          levels: this.browseGenLevelsData(node),
          refreshCache: isRefreshCache
        }
      });

      if (connection && (connection.generation !== generation || connection.blocked || this.workspaceUnmounted)) return;
      if (res.success && res?.data) {
        if (node) {
          const { objId, objName, objAttr, objType } = res.data;

          await this.$refs.dataSourceTree.handleUpdateNode(node.key, {
            [objType]: {
              id: objId === '-1' ? objName : objId,
              name: objName,
              attr: objAttr
            },
            title: objName,
            popTip: `${node.parentPoptip}.${objName}`
          });
        }

        if (callback) {
          callback();
        }
      }
    },
    async listLevels(node = null, params = {}, resolve, reject, isRefreshCache = false, connectionAction = false) {
      const connection = node?.INSTANCE && this.getWorkspaceConnection(node.INSTANCE.id);
      if (connection && (connection.blocked || connection.busy) && !connectionAction) {
        if (resolve) resolve([]);
        return false;
      }
      const generation = connection?.generation;
      appLogger.debug('listLevels', node);

      let statusRes = '';
      const checkingConnection = node?.nodeType === 'INSTANCE' && !connectionAction && connection.status !== 'connected';
      if (checkingConnection) {
        connection.busy = true;
        this.setWorkspaceConnectionState(node.INSTANCE.id, 'connecting');
      }
      try {
        if (node?.nodeType === 'INSTANCE') {
          statusRes = await this.$services.dmQueryFetchDsStatusConf({ data: { dsId: node.INSTANCE.id } });
        }
        const levelRes = await this.$services.dmBrowseListLevels({
          data: {
            levels: node ? this.browseGenLevelsData(node) : [],
            cancelPending: true,
            refreshCache: isRefreshCache
          }
        });
        if (connection && (connection.generation !== generation || this.workspaceUnmounted)) {
          if (resolve) resolve([]);
          return false;
        }
        if (levelRes.success && Array.isArray(levelRes.data)) {
          if (!node) this.treeData = [];
          const children = [];
          if (node && isRefreshCache) {
            this.completionData[node.key] = [];
          }
          levelRes.data.forEach((level) => {
            const { objId, objName, objType, objAttr = null } = level;
            if (node) {
              if (this.completionData[node.key]) {
                this.completionData[node.key].push(level);
              } else {
                this.completionData[node.key] = [level];
              }
              const preLevels = [...node.levels];
              const leaf = {};
              preLevels.forEach((levelKey) => {
                leaf[levelKey] = node[levelKey];
              });
              Object.assign(leaf, {
                [objType]: {
                  id: objId === '-1' ? objName : objId,
                  name: objName,
                  attr: objAttr
                },
                icon: objType === 'INSTANCE' ? objAttr.dsType : objType,
                isLeaf: false,
                selected: params.selected === `${node.key}.\`${objId === '-1' ? objName : objId}\``,
                title: objName,
                popTip: `${node.popTip}.${objName}`,
                parentKey: node.key,
                parentPoptip: node.popTip,
                key: `${node.key}.\`${objId === '-1' ? objName : objId}\``,
                children: [],
                nodeType: objType,
                levels: [...preLevels, objType]
              });
              if (objType === 'INSTANCE') {
                const workspace = this.getWorkspaceConnection(objId);
                leaf.connected = workspace.status === 'connected';
                leaf.connectedMsg = objAttr.msgContent;
              }

              const dsLevels = this.getLevels(leaf.INSTANCE.attr.dsType);
              if (dsLevels && dsLevels.length === leaf.levels.length) {
                leaf.isLeaf = true;
              }

              children.push(leaf);
            } else {
              const leaf = {
                [objType]: {
                  id: objId,
                  name: objName
                },
                icon: objType,
                isLeaf: false,
                popTip: objName,
                title: objName,
                key: `\`${objId}\``,
                children: [],
                nodeType: objType,
                levels: [objType]
              };
              this.treeData.push(leaf);
            }
          });

          if (connection && !connectionAction) {
            this.setWorkspaceConnectionState(node.INSTANCE.id, 'connected');
          }
          if (statusRes?.data?.dsStatus && node?.nodeType === 'INSTANCE') {
            await this.$refs.dataSourceTree.handleUpdateNode(node.key, {
              ...node,
              connected: connectionAction || connection.status === 'connected'
            });
          }

          // if (node) {
          //   this.$refs.dataSourceTree.handleSetExpandedKeys(node);
          // }

          if (node) {
            if (resolve) {
              resolve(children);
            } else if (isRefreshCache) {
              await this.$refs.dataSourceTree.handleUpdateNode(node.key, { children });
            } else {
              this.$refs.dataSourceTree.handleAppendList(node.key, children);
            }
          } else {
            await this.$refs.dataSourceTree.handleSetData(this.treeData, true);
          }
          return true;
          // await this.$refs.dataSourceTree.handleSetData(this.treeData, true);
          // if (node) {
          //   await this.$refs.dataSourceTree.handleSetSelected(node.key);
          // }
        } else {
          appLogger.debug('fail', levelRes);
          if (typeof levelRes === 'object' && levelRes.toString().includes('Cancel')) {
            appLogger.debug('reset');
            if (resolve) resolve([]);
            return;
          }

          if (resolve) resolve([]);
        }
      } catch (e) {
        appLogger.error(e);
        if (resolve) resolve([]);
      } finally {
        if (checkingConnection) connection.busy = false;
      }
      if (connection && !connectionAction && connection.generation === generation && !connection.blocked)
        this.setWorkspaceConnectionState(node.INSTANCE.id, 'failed', this.$t('sql-connection-open-failed'));
      return false;
    },
    async setInstanceErrorIcon(data) {
      const instanceId = typeof data === 'object' ? data.instanceId : data;
      const connection = this.getWorkspaceConnection(instanceId);
      if (connection.blocked || connection.busy) return;
      if (typeof data === 'object' && data.connected) return;
      this.setWorkspaceConnectionState(instanceId, 'failed', data?.msgContent || this.$t('sql-connection-open-failed'));
      if (typeof data !== 'object') return;
      if (this.$refs.dataSourceTree) {
        const treeData = this.$refs.dataSourceTree.handleGetTreeData();
        treeData.forEach((env) => {
          if (env.children) {
            env.children.forEach(async (instance) => {
              if (instance.INSTANCE.id === instanceId) {
                instance.connected = data?.connected;
                instance.connectedMsg = data?.msgContent;
                await this.$refs.dataSourceTree.handleUpdateNode(instance.key, {
                  connected: data?.connected,
                  connectedMsg: data?.msgContent
                });
                if (!data?.connected && !data?.noModal) {
                  this.connectedInstance = { code: data?.code, ...instance };
                  this.showConnectedModal = true;
                }
              }
            });
          }
        });
      }
    },
    handleChangeSchema(event) {
      const { node, schemaParentKey, prefix } = this.currentTab;
      if (event) {
        const lastLevel = node.levels[node.levels.length - 1];
        node[lastLevel].name = event;
        node[lastLevel].id = event;
        node.key = `${schemaParentKey}.\`${event}\``;
        this.currentTab.key = `${prefix}.${schemaParentKey}.\`${event}\``;
        const sameIndex = this.tabs.findIndex((tab) => tab.key === this.currentTab.key);
        if (sameIndex > -1) {
          this.currentTab.key = `${prefix}.${schemaParentKey}.\`${event}\`.\`${dayjs().valueOf()}\``;
        }
        this.currentTab.prefixKey = this.currentTab.key;
        this.currentTab.title = event;
        const { levels } = node;
        let popTip = '';
        const node2 = {
          levels
        };
        levels.forEach((level) => {
          node2[level] = node[level];
          popTip += `${node[level].name}.`;
        });
        if (popTip) {
          popTip = popTip.substring(0, popTip.lastIndexOf('.'));
        }
        this.currentTab.popTip = popTip;
        this.listLeaf();
      }

      this.active = this.currentTab.key;
      this.currentTab.message.show = false;
      sendWebSocket(
        {
          type: WS_TYPE.WS_REQ_QUERY,
          object: {
            sessionId: this.currentTab.sessionId,
            queryType: WS_REQ_QUERY_TYPE.SWITCH_CTX,
            levels: this.browseGenLevelsData(this.currentTab.node),
            rdbAutoCommit: this.currentTab.autoCommit,
            rdbReadOnly: this.currentTab.readOnly,
            rdbIsolation: this.currentTab.isolation
          }
        },
        { message: this.$refs.sqlViewer?.onmessage }
      );
    },
    async refreshQueryMetadata(tab) {
      const tree = this.$refs.dataSourceTree;
      const parent = tree?.handleGetNode(this.getWorkspaceInstance(tab.node).key);
      const connection = this.getWorkspaceConnection(tab.dsId);
      if (!parent?.key || connection.blocked || connection.busy) return;

      if (!(await this.listLevels(parent, {}, undefined, undefined, true, true))) return;
      this.refreshTabSelectOptions(parent.key);

      const current = this.currentTab;
      const { leafType } = current;
      if (current.type !== TAB_TYPE.QUERY || current.dsId !== tab.dsId || !leafType) return;
      if (current.node?.SCHEMA && !current.selectOptions?.some((option) => option.value === current.node.SCHEMA.name)) {
        current[leafType].treeData = [];
        current.expandedKeys = [];
        current.selectedTable = null;
        await this.$refs.tableList?.handleSetData([]);
        this.storeQueryTabs();
        return;
      }
      const expanded = new Set(this.$refs.tableList?.handleGetExpandKeys());
      const requestId = this.tableListRequestId + 1;
      if (!(await this.listLeaf(true, false, true))) return;

      const isCurrent = () => this.currentTab === current && current.leafType === leafType && this.tableListRequestId === requestId;
      for (const node of current[leafType].treeData) {
        if (!isCurrent()) return;
        if (node.isLeaf || !expanded.has(node.key)) continue;
        await this.rdbObjectDetail(node.title, { expand: false, selected: false, metadataRefresh: true }, undefined, true);
        if (!isCurrent()) return;
        node.expand = node.children.length > 0;
        node.children.forEach((child) => {
          child.expand = expanded.has(child.key);
        });
      }
      if (expanded.size && isCurrent()) await this.$refs.tableList?.handleSetCurrentObjectPage();
    },
    async listLeaf(isRefreshCache = false, connectionAction = false, metadataRefresh = false) {
      const currentTab = this.currentTab;
      if (!currentTab.node?.INSTANCE) return false;
      const connection = this.getWorkspaceConnection(currentTab.dsId);
      if ((connection.blocked || connection.busy) && !connectionAction) return false;
      const generation = connection.generation;
      const { node, leafType } = currentTab;
      if (!leafType) return;

      const requestId = ++this.tableListRequestId;
      this.tableListLoading = true;

      const leafList = [];

      // Invalid Name of Example
      const noLegal = node?.key?.includes('/');
      if (noLegal) {
        this.$Message.warning(this.$t('fa-mi-ming-cheng-bu-zhi-chi'));
        this.tableListLoading = false;
        return false;
      }

      try {
        const leafRes = await this.$services.dmBrowseListLeaf({
          data: {
            levels: node.levels.map((l) => node[l].id),
            leafType,
            refreshCache: isRefreshCache
          }
        });

        if (metadataRefresh && requestId !== this.tableListRequestId) return false;
        if (connection.generation !== generation || (connection.blocked && !connectionAction) || this.workspaceUnmounted || isCancel(leafRes)) {
          return false;
        }
        if (leafRes.success) {
          const completionKey = node.key;
          const obj = Object.create(null);
          leafRes.data.forEach((leaf) => {
            const { objName, objType, objAttr } = leaf;
            const tempObjName = objAttr && objAttr.rdb_roi ? objAttr.rdb_roi : objName;
            obj[objName] = leaf;
            leafList.push({
              title: tempObjName,
              key: `${node.key}.\`${tempObjName}\``,
              leafKey: `${node.key}.\`${tempObjName}\``,
              nodeType: objType,
              children: [],
              icon: objType,
              isRoot: true,
              objAttr,
              objName,
              isLeaf: !this.getLeafExpand(node.INSTANCE.attr.dsType).includes(objType)
            });
          });

          if (this.completionData[completionKey]) {
            this.completionData[completionKey][leafType] = obj;
          } else {
            this.completionData[completionKey] = { [leafType]: obj };
          }

          currentTab[leafType].treeData = leafList;
          currentTab[leafType].loaded = true;
          if (!connectionAction && !metadataRefresh) this.setWorkspaceConnectionState(currentTab.dsId, 'connected');
          if (this.currentTab.key === currentTab.key && currentTab.leafType === leafType && this.$refs.tableList) {
            await this.$refs.tableList.handleSetData(leafList);
            await this.$refs.tableList.handleFilter(currentTab[leafType].searchKey);
          }
          return true;
        }
      } catch (e) {
        appLogger.error(e);
      } finally {
        if (requestId === this.tableListRequestId) this.tableListLoading = false;
        this.storeQueryTabs();
      }
      if (!metadataRefresh && !connectionAction && connection.generation === generation && !connection.blocked) {
        this.setWorkspaceConnectionState(currentTab.dsId, 'failed', this.$t('sql-connection-open-failed'));
      }
      return false;
    },
    async handleAddTab(type, node, options = {}) {
      const connection = this.getWorkspaceConnection(node.INSTANCE.id);
      if (connection.blocked || connection.busy) {
        this.$Message.warning(this.$t('sql-connection-disconnected'));
        return;
      }
      this.storeActiveTab();
      if (this.disableAddTab()) {
        return;
      }
      this.syncCurrentEditorTextToTab();
      this.storeQueryTabs();
      const prefix = `tab_${type}${options.editorType ? `_${options.editorType}` : ''}`;
      const prefixKey = `${prefix}.${node.key}`;
      const key = `${prefixKey}${options.table ? `.\`${options.table.title}\`` : ''}${options.force ? `.${dayjs().valueOf()}` : ''}`;

      let hasSameTab = false;
      for (let i = 0; i < this.tabs.length; i++) {
        if (key === this.tabs[i].key) {
          this.active = key;
          this.currentTab = this.tabs[i];
          hasSameTab = true;
        }
      }

      if (hasSameTab) {
        if (this.compactWorkspace) this.selectMobileWorkspace('query');
        if (type === TAB_TYPE.QUERY) {
          if (await this.createSession(this.currentTab)) await this.listLeaf();
        }
        return;
      }

      const { levels } = node;
      let popTip = '';
      const node2 = {
        levels
      };
      levels.forEach((level) => {
        node2[level] = node[level];
        popTip += `${node[level].name}.`;
      });
      if (popTip) {
        popTip = popTip.substring(0, popTip.lastIndexOf('.'));
      }
      node2.children = node.children;
      node2.key = node.key;
      const tab = {
        tabId: nanoid(),
        node: node2,
        dsType: node.INSTANCE.attr.dsType,
        isEditing: false,
        dsId: node.INSTANCE.id,
        popTip,
        running: false,
        type,
        prefix,
        key,
        prefixKey,
        title: node.title,
        loading: false,
        cost: {
          popIndex: -1,
          popList: []
        },
        executeInfo: [],
        executeInfoScrollDown: true,
        executeInfoScrollPosition: 0
      };

      const res = await this.$services.dmQueryFetchDsStatusConf({
        data: {
          dsId: tab.dsId
        }
      });

      if (!res.success) return;

      tab.support = res.data;
      tab.connected = res.data.dsStatus === 'Normal';
      tab.workspaceConnectionState = connection.status;
      tab.msgContent = res.data.dsStatusMessage;
      tab.isolation = res.data.isolation.defaultValue;
      tab.autoCommit = res.data.autoCommit.defaultValue === 'true';
      tab.readOnly = res.data.readOnly.defaultValue === 'true';

      switch (type) {
        case TAB_TYPE.QUERY:
          tab.schemaParentKey = node._parent.key;
          // Make sure Children is an array and contains objects
          tab.selectOptions = this.buildSchemaOptions(node._parent.children, node._parent);
          tab.leafGroup = [];
          tab.selectValue = node.title;
          tab.selectedTable = null;
          tab.message = {
            text: '',
            show: false,
            type: ''
          };
          tab.expandedKeys = [];
          tab.result = {
            active: 'message',
            collapsed: false,
            maximized: false,
            collapsedBeforeMaximize: false,
            list: []
          };
          tab.text = '';
          tab.icon = node.INSTANCE.attr.dsType;
          tab.sessionId = '';
          tab.executeInfo = [];
          // Which side group to use according to node level
          const lastLevel = node.levels[node.levels.length - 1];
          const leafGroup = this.getLeafGroup(tab.node.INSTANCE.attr.dsType, lastLevel, tab.node[lastLevel].attr);

          if (leafGroup && leafGroup.length) {
            tab.leafGroup = leafGroup;
            tab.leafType = leafGroup[0].type;
            leafGroup.forEach((tabKey) => {
              tab[tabKey.type] = {
                ...tabKey,
                searchKey: '',
                treeData: []
              };
            });
          }
          break;
        case TAB_TYPE.DATA:
          tab.icon = 'TableEdit';
          Object.assign(tab, {
            title: this.$t('optionstabletitle-biao-de-shu-ju', [options.table.title]),
            targetType: this.currentTab.leafType,
            selectedTable: {
              title: options.table ? options.table.title : ''
            },
            options: {},
            columnlen: {},
            updateCellList: [],
            addRows: [],
            deleteRows: [],
            currentSortConfig: {},
            whereKeyList: [],
            offset: 0,
            pageSize: 50,
            selectedPageKeys: ['50'],
            hasNext: false,
            isFirst: false,
            total: null,
            condition: '',
            orderBy: '',
            rawTableData: {
              resultSet: [],
              resultSetMore: []
            },
            columns: [],
            columnWithoutHidden: []
          });
          break;
        case TAB_TYPE.STRUCT:
          tab.icon = 'Table';
          Object.assign(tab, {
            referenceSchemaList: [],
            referenceTableList: [],
            referenceColumnList: [],
            title:
              options.editorType === ACTION_TYPE.EDIT_TABLE
                ? this.$t('optionstabletitle-biao-de-jie-gou', [options.table.title])
                : this.$t('xin-jian-biao'),
            editorType: options.editorType,
            selectedTable: {
              title: options.table ? options.table.title : ''
            },
            selectedKeys: [],
            selectedData: {},
            selectedIndex: -1,
            expandedKeys: [],
            nodeType: '',
            formData: {
              tableInfo: {}
            },
            originalFormData: {},
            initTableData: {
              tableInfo: {}
            },
            selectedNode: null,
            selectedSchema: null,
            schemaDef: {},
            treeData: [],
            init: false
          });
          break;
        default:
          break;
      }

      this.tabs.push(tab);
      this.active = key;
      this.currentTab = tab;

      switch (type) {
        case TAB_TYPE.QUERY:
          if (await this.createSession(this.currentTab)) await this.listLeaf();
          break;
        case TAB_TYPE.STRUCT:
        case TAB_TYPE.DATA:
          break;
        default:
          break;
      }
      this.storeQueryTabs();
    },
    getTabDisplayTitle(tab) {
      if (!tab?.schemaParentKey) return tab?.title || '';
      const parentNode = this.$refs.dataSourceTree?.handleGetNode(tab.schemaParentKey);
      let catalogTitle = '';
      if (parentNode && parentNode.nodeType === 'CATALOG') {
        catalogTitle = parentNode.title;
      } else if (tab.node?.levels?.length) {
        const catalogLevel = tab.node.levels.find((level) => level === 'CATALOG');
        if (catalogLevel && tab.node[catalogLevel]?.name) {
          catalogTitle = tab.node[catalogLevel].name;
        }
      }
      if (catalogTitle) {
        return catalogTitle + '.' + tab.title;
      }
      return tab.title;
    },
    buildSchemaOptions(children, parentNode) {
      if (!Array.isArray(children)) return [];
      let prefix = '';
      if (parentNode && parentNode.nodeType === 'CATALOG') {
        prefix = parentNode.title + '.';
      }
      return children
        .filter((child) => child && typeof child === 'object' && child.title)
        .map((child) => ({ value: child.title, label: prefix + child.title }));
    },
    refreshTabSelectOptions(key) {
      const node = this.$refs.dataSourceTree.handleGetNode(key);
      if (node.key && node._parent && node._parent.children) {
        this.tabs.forEach((tab) => {
          if (tab.type === TAB_TYPE.QUERY && tab.schemaParentKey === key) {
            // Make sure Children is an array and contains objects
            tab.selectOptions = this.buildSchemaOptions(node.children, node);
          }
        });
        this.storeQueryTabs();
      }
    },
    cancelAllLoading() {
      this.loading = false;
      this.sessionLoading = false;
      this.createOrDropLoading = false;
      this.tableListLoading = false;
    },
    setLoading(loading) {
      this.loading = loading;
    },
    async onOpen() {
      if (this.hasNextPage) {
        await this.$nextTick();
        this.observer.observe(this.$refs.load);
      }
    },
    onClose() {
      this.observer.disconnect();
    },
    async infiniteScroll([{ isIntersecting, target }]) {
      if (isIntersecting) {
        const ul = target.offsetParent;
        const scrollTop = target.offsetParent.scrollTop;
        this.limit += 50;
        await this.$nextTick();
        ul.scrollTop = scrollTop;
      }
    },
    async getLocalQueryData() {
      appLogger.debug('getLocalQueryData', this.userInfo.uid);
      try {
        const localData = localStorage.getItem(`clouddm_new_tabs_${this.userInfo.uid}`);
        if (localData) {
          let tabs = [];
          const data = JSON.parse(localData);
          this.active = data.active;

          if (this.needGetDataFromIndexedDb) {
            const tabManagerInstance = this.tabManager.getInstance();
            if (tabManagerInstance) {
              const tabDataList = await tabManagerInstance.where('uid').equals(this.userInfo.uid).toArray();
              data.tabData.forEach((item) => {
                let isIndexedDb = false;
                tabDataList.forEach((item2) => {
                  if (item.tabId === item2.id) {
                    isIndexedDb = true;
                    tabs.push(item2.data);
                  }
                });

                if (!isIndexedDb) {
                  tabs.push(item);
                }
              });
            }
          } else {
            tabs = data.tabData || [];
          }

          this.tabs = tabs;

          if (this.tabs.length) {
            let hasActiveKey = false;
            this.tabs.forEach((tab) => {
              if (tab.type === TAB_TYPE.QUERY) {
                // Migrate legacy favorite tabs to ordinary query drafts.
                if (!tab.dsType) tab.dsType = tab.scriptDsType;
                for (const field of ['scriptId', 'scriptName', 'scriptVersion', 'scriptSavedText', 'scriptDsType', 'scriptAssociationRevision']) {
                  delete tab[field];
                }
                tab.isEditing = false;
              }
              if (tab.type === TAB_TYPE.STRUCT) {
                this.resetStoredStructEditorState(tab);
              }
              if (!tab.dsId && tab.node) {
                tab.dsId = tab.node.INSTANCE.id;
              }
              tab.message = {
                text: '',
                show: false,
                type: ''
              };
              tab.cost = {
                popIndex: -1,
                popList: []
              };
              tab.executeInfo = [];
              tab.msgFromWs = false;
              tab.msgContent = '';
              const connection = this.getWorkspaceConnection(tab.dsId);
              if (connection.blocked) {
                tab.connected = false;
                tab.workspaceConnectionState = 'disconnected';
                tab.msgContent = this.$t('sql-connection-disconnected');
              }
              // Ensures that physical options exist
              if (!tab.selectOptions) {
                tab.selectOptions = [];
              }
              if (tab.leafGroup) {
                tab.leafGroup.forEach((leaf) => {
                  tab[leaf.type] = {
                    searchKey: '',
                    treeData: []
                  };
                });
              }
              if (tab.key === this.active) {
                this.currentTab = tab;
                hasActiveKey = true;
              }
            });

            if (hasActiveKey) {
              await this.handleGetDsSetting(true);
              if (this.currentTab.type === TAB_TYPE.QUERY && this.currentTab.connected) {
                await this.listLeaf();
              }
            }
          }
        }
      } catch (e) {
        appLogger.debug(e);
      }
    },
    storeActiveTab() {
      appLogger.debug('store active tab', this.currentTab.type, this.currentTab.tabId);
      if (this.currentTab.type === 'DATA') {
        const { uid } = this.userInfo;
        if (this.currentTab.options) {
          delete this.currentTab.options.cellRightClickConfig;
          delete this.currentTab.options.hook;
        }
        this.tabManager.setTabData({ uid, id: this.currentTab.tabId, data: this.currentTab });
      }
    },
    syncCurrentEditorTextToTab() {
      if (this.currentTab.type !== TAB_TYPE.QUERY) {
        return;
      }
      const sqlViewer = this.$refs.sqlViewer;
      if (!sqlViewer || !sqlViewer.monacoEditor) {
        return;
      }
      this.currentTab.text = sqlViewer.monacoEditor.getValue();
    },
    resetStoredStructEditorState(tab) {
      Object.assign(tab, {
        formData: {
          tableInfo: {}
        },
        originalFormData: {},
        initTableData: {
          tableInfo: {}
        },
        selectedIndex: -1,
        nodeType: '',
        selectedNode: null,
        selectedSchema: null,
        schemaDef: {},
        order: [],
        validationTarget: null,
        init: false,
        isEditing: false
      });
    },
    storeQueryTabs() {
      appLogger.debug('store query tabs');
      const { uid } = this.userInfo;
      const key = `clouddm_new_tabs_${uid}`;

      const tabData = deepClone(this.tabs);

      tabData.forEach((tab) => {
        if (tab.type === TAB_TYPE.STRUCT) {
          this.resetStoredStructEditorState(tab);
        }
        if (tab.leafGroup) {
          tab.leafGroup.forEach((leaf) => {
            tab[leaf.type] = {
              searchKey: '',
              treeData: []
            };
          });
        }

        if (tab.node && tab.node.children) {
          tab.node.children = [];
        }
        tab.rawTableData = {
          resultSet: [],
          resultSetMore: []
        };
        tab.options = {};
        tab.expandedKeys = [];
        tab.updateCellList = [];
        tab.addRows = [];
        tab.deleteRows = [];
        tab.currentSortConfig = {};
        tab.whereKeyList = [];
        tab.selectedRowKeys = [];
        tab.startEditing = false;
        tab.selectedCell = {
          rowIndex: -1,
          column: {},
          row: {}
        };

        tab.result = {
          active: 'message',
          collapsed: false,
          maximized: false,
          collapsedBeforeMaximize: false,
          list: []
        };
        tab.executeInfo = [];
        tab.schemaList = [];
        tab.allSchemaList = [];
        tab.childrenList = [];
        tab.running = false;
      });
      localStorage.setItem(key, JSON.stringify({ active: this.active, tabData }));
    },
    getDataSourceNodeData(node, params = {}, resolve = () => {}, reject) {
      if (node && !node.isLeaf) {
        this.listLevels(node, params, resolve, reject);
      } else {
        resolve();
      }
    },
    getTableNodeData(node, params, resolve) {
      if (node && !node.isLeaf) {
        this.rdbObjectDetail(node.title, { ...params, expand: true, selected: true }, resolve);
      } else {
        resolve();
      }
    },
    async rdbObjectDetail(tableName, params = { expand: true, selected: true }, resolve = () => {}, isRefreshCache = false) {
      const { expand, selected, selectedNode, metadataRefresh = false } = params;
      const currentTab = this.currentTab;
      const { node, leafType } = currentTab;
      const requestId = this.tableListRequestId;
      const connection = this.getWorkspaceConnection(currentTab.dsId);
      if (connection.blocked || connection.busy) {
        resolve([]);
        return;
      }
      const generation = connection.generation;
      try {
        const res = await this.$services.dmBrowseRdbObjectDetail({
          data: {
            levels: this.browseGenLevelsData(node),
            targetName: tableName,
            targetType: leafType,
            refreshCache: isRefreshCache
          }
        });

        if (metadataRefresh && requestId !== this.tableListRequestId) return;
        if (this.currentTab !== currentTab || currentTab.leafType !== leafType || connection.blocked || connection.generation !== generation) {
          resolve();
          return;
        }

        if (res.success) {
          const curNode = this.getNodeByKey(currentTab[leafType].treeData, `${node.key}.\`${tableName}\``);
          const completionItem = this.completionData[node.key]?.[leafType]?.[tableName];
          if (!curNode || !completionItem) {
            resolve();
            return;
          }
          const children = [];
          const columnList = [];
          res.data.group.forEach((groupItem) => {
            const child = {
              expand,
              nodeType: groupItem.type,
              title: groupItem.name,
              key: `${node.key}.\`${tableName}\`.${groupItem.type}`,
              leafKey: curNode.leafKey,
              isLeaf: groupItem.items.length === 0,
              icon: 'folder',
              children: []
            };
            groupItem.items.forEach((column) => {
              const item = {
                title: column.name,
                key: `${child.key}.\`${column.name}\``,
                leafKey: child.leafKey,
                icon: column.icon || column.type,
                tips: column.tips && `${column.tips}`,
                nodeType: column.type,
                isLeaf: true
              };
              child.children.push(item);
              if (groupItem.type === 'RDB_COLUMN_GROUP') {
                columnList.push(item);
              }
            });
            children.push(child);
            // if (expand) {
            //   console.log('set expand');
            //   this.$refs.tableList.handleSetExpandedKeys(child);
            // }
          });

          curNode.children = children;
          completionItem.columnList = columnList;
          // await this.$refs.tableList.handleSetData(this.currentTab[this.currentTab.leafType].treeData);
          if (selected && this.currentTab.key === currentTab.key) {
            if (selectedNode) {
              this.$refs.tableList.handleSetSelected(selectedNode);
            } else {
              this.$refs.tableList.handleSetSelected(curNode);
            }
          }
          if (!metadataRefresh) this.setWorkspaceConnectionState(currentTab.dsId, 'connected');
          resolve(children);
        } else {
          if (!metadataRefresh) this.setInstanceErrorIcon(node?.INSTANCE?.id);
          // await this.$refs.tableList.handleSetData(this.currentTab[this.currentTab.leafType].treeData);
          resolve();
        }
      } catch (e) {
        appLogger.error(e);
        if (!metadataRefresh) this.setInstanceErrorIcon(node?.INSTANCE?.id);
        // await this.$refs.tableList.handleSetData(this.currentTab[this.currentTab.leafType].treeData);
        resolve();
      }
    },
    async closesSession(tabs) {
      const sessionId = [];
      tabs.forEach((tab) => {
        if (tab.sessionId) {
          sessionId.push(tab.sessionId);
        }
      });

      if (!sessionId.length) return true;
      const res = await this.$services.dmQueryClosesSession({ data: { sessionId } });
      return res.success;
    },
    handleTabAction(actionType) {
      const index = this.tabs.findIndex((tab) => tab.key === this.contextData.key);
      switch (actionType) {
        case TAB_ACTION.DELETE_ALL:
          this.bulkCloseFocusKey = '';
          this.queueTabsForClose(this.tabs.map((tab) => tab.key));
          break;
        case TAB_ACTION.DELETE_OTHER_ALL:
          this.bulkCloseFocusKey = this.tabs[index]?.key || '';
          this.queueTabsForClose(this.tabs.filter((tab, tabIndex) => tabIndex !== index).map((tab) => tab.key));
          break;
        case TAB_ACTION.DELETE_LEFT_ALL:
          break;
        case TAB_ACTION.DELETE_RIGHT_ALL:
          break;
        default:
          break;
      }
    },
    handleCloseTab(targetKey) {
      this.bulkCloseFocusKey = '';
      this.queueTabsForClose([targetKey]);
    },
    queueTabsForClose(keys) {
      this.pendingCloseTabKeys = keys.filter((key) => this.tabs.some((tab) => tab.key === key));
      this.continueClosingTabs();
    },
    continueClosingTabs() {
      const targetKey = this.pendingCloseTabKeys.shift();
      if (!targetKey) {
        const focusKey = this.bulkCloseFocusKey;
        this.bulkCloseFocusKey = '';
        if (focusKey && this.tabs.some((tab) => tab.key === focusKey)) this.handleChangeTab(focusKey);
        return;
      }
      const editTab = this.tabs.find((tab) => tab.key === targetKey);
      if (!editTab) {
        this.continueClosingTabs();
        return;
      }
      if (editTab.type === 'DATA' && this.dataViewIsEditing(editTab)) {
        this.confirmCloseEditedTab(
          targetKey,
          'dang-qian-ye-mian-huan-you-wei-ti-jiao-de-xiu-gai-qing-xian-ti-jiao-hou-guan-bi-dian-ji-li-ji-guan-bi-jiang-diu-shi-dang-qian-xiu-gai'
        );
        return;
      }
      if (editTab.type === 'STRUCT' && this.structViewIsEditing(editTab)) {
        this.confirmCloseEditedTab(
          targetKey,
          'dang-qian-biao-jie-gou-huan-you-wei-ti-jiao-de-xiu-gai-qing-xian-ti-jiao-hou-guan-bi-dian-ji-li-ji-guan-bi-jiang-diu-shi-dang-qian-xiu-gai'
        );
        return;
      }
      this.removeTab(targetKey);
      this.$nextTick(this.continueClosingTabs);
    },
    confirmCloseEditedTab(targetKey, contentKey) {
      Modal.confirm({
        title: this.$t('ti-shi'),
        content: this.$t(contentKey),
        okText: this.$t('li-ji-guan-bi'),
        onOk: () => {
          this.removeTab(targetKey);
          this.$nextTick(this.continueClosingTabs);
        },
        onCancel: this.cancelPendingTabClose
      });
    },
    cancelPendingTabClose() {
      this.pendingCloseTabKeys = [];
      this.bulkCloseFocusKey = '';
    },
    removeTab(key) {
      const deleteIndex = this.tabs.findIndex((tab) => tab.key === key);
      if (deleteIndex < 0) return;
      const wasCurrent = this.currentTab.key === key;
      const [deletedTab] = this.tabs.splice(deleteIndex, 1);
      this.closesSession([deletedTab]);
      this.tabManager.deleteTabData(deletedTab.tabId);

      const len = this.tabs.length;
      if (len > 0 && wasCurrent) {
        const activeIndex = deleteIndex ? deleteIndex - 1 : 0;
        this.active = this.tabs[activeIndex].key;
        this.handleChangeTab(this.active);
      } else if (!len) {
        this.active = '';
        this.currentTab = {};
      }

      this.storeQueryTabs();
    },
    async createSession(tab, next, connectionAction = false) {
      const connection = this.getWorkspaceConnection(tab.dsId);
      if ((connection.blocked || connection.busy) && !connectionAction) return false;
      if (tab.sessionId) return true;
      if (this.sessionRequests.has(tab)) return this.sessionRequests.get(tab);
      const generation = connection.generation;
      this.sessionLoading = true;
      const request = (async () => {
        try {
          const res = await this.$services.dmQueryCreateSession({
            data: {
              levels: this.browseGenLevelsData(tab.node),
              initAutoCommit: tab.autoCommit,
              initIsolation: tab.isolation
            }
          });
          if (isCancel(res)) return false;
          if (!res.success || !res.data?.sessionId) throw new Error(this.$t('sql-connection-open-failed'));
          if (!this.tabs.includes(tab) || this.workspaceUnmounted) {
            await this.$services.dmQueryClosesSession({ data: { sessionId: [res.data.sessionId] } });
            return false;
          }
          tab.sessionId = res.data.sessionId;
          if (connection.generation !== generation) return false;
          if (!tab.autoCommit && !connectionAction) this.setWorkspaceConnectionState(tab.dsId, 'connected');
          if (next) next();
          return true;
        } catch (error) {
          if (!connectionAction && connection.generation === generation && !connection.blocked && !this.workspaceUnmounted) {
            this.setWorkspaceConnectionState(tab.dsId, 'failed', error.message || this.$t('sql-connection-open-failed'));
          }
          return false;
        } finally {
          this.sessionRequests.delete(tab);
          this.sessionLoading = this.sessionRequests.size > 0;
        }
      })();
      this.sessionRequests.set(tab, request);
      return request;
    },
    handleQueryTable(text) {
      if (this.compactWorkspace) this.selectMobileWorkspace('query');
      this.$refs.sqlViewer.setSql(text);
    },
    disableAddTab() {
      if (this.tabs.length >= 20) {
        Modal.warning({
          title: this.$t('chuang-kou-shang-xian-ti-shi'),
          content: this.$t('ni-kai-qi-de-chuang-kou-shu-liang-yi-da-shang-xian-qing-guan-bi-qi-ta-chuang-kou-hou-zhong-xin-cao-zuo'),
          okText: this.$t('que-ding')
        });
      }

      return this.tabs.length >= 20;
    },
    setActiveKey(key) {
      this.active = key;
    },
    async handleChangeTab(activeKey) {
      // Closing or reselecting the current tab must not refresh its object list.
      if (this.currentTab.key === activeKey) return;
      this.scriptLoadSequence++;
      appLogger.debug(this.currentTab.type);
      if (this.currentTab.type === 'STRUCT') {
        appLogger.debug('editing', this.structViewIsEditing(this.currentTab));
        this.currentTab.isEditing = this.structViewIsEditing(this.currentTab);
      } else if (this.currentTab.type === 'DATA') {
        appLogger.debug('editing', this.dataViewIsEditing(this.currentTab));
        this.currentTab.isEditing = this.dataViewIsEditing(this.currentTab);
      }

      appLogger.debug('change tab', activeKey);
      this.syncCurrentEditorTextToTab();
      const changeTab = async (key) => {
        if (this.$refs['data-view']) {
          this.$refs['data-view'].handleEmptyUpdate();
        }
        this.$nextTick(async () => {
          this.storeQueryTabs();
          this.tabs.forEach((item) => {
            if (item.key === key) {
              clearAllPending();
              this.cancelAllLoading();
              this.currentTab = item;
              this.active = key;
              if (this.$refs.tableList && item.leafType && item[item.leafType] && item[item.leafType].treeData) {
                this.$refs.tableList.selectedNode = null;
                this.$refs.tableList.handleSetData(item[item.leafType].treeData);
              }
            }
          });
          await this.handleGetDsSetting();
          // Make sure that when you refresh the browser, switch the tab and get the data up and up.
          if (this.currentTab.type === TAB_TYPE.QUERY && this.currentTab.connected) {
            await this.listLeaf();
          }
          if (this.$refs.sqlViewer) {
            this.$refs.sqlViewer.handleGetRecoveryStatus();
          }
        });
      };

      if (this.currentTab.type === 'STRUCT') {
        this.structViewIsEditing(this.currentTab);
      }

      await changeTab(activeKey);
    },
    async handleClickDsStatusIcon() {
      if (!this.currentTab.node?.INSTANCE) return;
      const connection = this.getWorkspaceConnection(this.currentTab.dsId);
      await this.handleWorkspaceConnection(connection.status === 'disconnected' ? 'open' : 'reconnect', this.currentTab.node);
    },
    async handleGetDsSetting(restore = false) {
      const targetTab = this.currentTab;
      if (targetTab && targetTab.dsId) {
        const dsId = targetTab.dsId;
        const connection = this.getWorkspaceConnection(dsId);
        if (connection.blocked || connection.busy) return;
        const generation = connection.generation;
        // Do not call interface if websocket returns msgContent
        const hasWsMessage = targetTab.msgFromWs;

        if (hasWsMessage) {
          return;
        }

        const res = await this.$services.dmQueryFetchDsStatusConf({
          data: {
            dsId
          }
        });
        if (
          !this.tabs.includes(targetTab) ||
          targetTab.dsId !== dsId ||
          connection.blocked ||
          connection.busy ||
          connection.generation !== generation
        )
          return;

        if (res.success) {
          targetTab.support = res.data;
          if (res.data.dsStatus !== 'Normal') {
            this.setWorkspaceConnectionState(dsId, 'failed', res.data.dsStatusMessage);
          } else if (connection.status !== 'failed') {
            targetTab.connected = true;
          }

          if (!restore && !targetTab.sessionId) {
            targetTab.isolation = res.data.isolation.defaultValue;
            targetTab.autoCommit = res.data.autoCommit.defaultValue === 'true';
            targetTab.readOnly = res.data.readOnly.defaultValue === 'true';
          }

          if (targetTab.sessionId && targetTab.connected) {
            sendWebSocket(
              {
                type: WS_TYPE.WS_REQ_QUERY,
                object: {
                  sessionId: targetTab.sessionId,
                  queryType: WS_REQ_QUERY_TYPE.RECOVER_STATUS,
                  levels: this.browseGenLevelsData(targetTab.node),
                  rdbAutoCommit: targetTab.autoCommit,
                  rdbReadOnly: targetTab.readOnly,
                  rdbIsolation: targetTab.isolation
                }
              },
              { message: this.$refs.sqlViewer?.onmessage }
            );
          }
        }
      }
    },
    async getRedisKey() {
      this.spinning = true;

      const res = await this.$services.dmDataSourceSpecialRedisTopKeysWithLimit({
        data: {
          dataSourceId: this.currentTab.instanceId,
          pattern: this.currentTab.searchKey || '*'
        }
      });
      if (res.success) {
        const keysData = [];
        res.data.forEach((data) => {
          keysData.push({
            name: data,
            type: 'KEY'
          });
        });
        this.currentTab.keyList = keysData || [];
        this.currentTab.filteredKeyList = deepClone(this.currentTab.keyList) || [];
        this.currentTab.showKeyList = this.currentTab.filteredKeyList.slice(0, this.currentTab.pageData.size);

        this.$nextTick(() => {
          this.$forceUpdate();
        });
      }
      this.spinning = false;
    },
    async handleRefreshTables(tabSetting = {}) {
      if (tabSetting.type === 'key') {
        await this.getRedisKey();
      } else {
        await this.getTableList(tabSetting);
      }
    },
    async handleShowTaskDetail(task) {
      this.$bus.emit('showFakerModal', task);
    },
    async handleAsyncEventAction(actionType, task) {
      let res;
      let msgContent;
      switch (actionType) {
        case 'cancel':
          res = await this.$services.dmAsyncTaskDockTaskCancelTask({
            data: {
              taskId: task.id
            }
          });
          msgContent = this.$t('qu-xiao-cheng-gong');
          break;
        case 'pause':
          res = await this.$services.dmAsyncTaskDockTaskPauseTask({
            data: {
              taskId: task.id
            }
          });
          msgContent = this.$t('zan-ting-cheng-gong');
          break;
        case 'retry':
          res = await this.$services.dmAsyncTaskDockTaskRetryTask({
            data: {
              taskId: task.id
            }
          });
          msgContent = this.$t('zhong-shi-cheng-gong');
          break;
        case 'resume':
          res = await this.$services.dmAsyncTaskDockTaskResumeTask({
            data: {
              taskId: task.id
            }
          });
          msgContent = this.$t('hui-fu-cheng-gong');
          break;
        default:
          break;
      }

      if (res.success) {
        this.$message.success(msgContent);
      }
    },
    showTaskCancelBtn(task) {
      const { INIT, BLOCK, RUNNING } = ASYNC_TASK_STATUS;
      return [INIT, BLOCK, RUNNING].includes(task.status);
    },
    showTaskPauseBtn(task) {
      const { BLOCK, RUNNING } = ASYNC_TASK_STATUS;
      return [BLOCK, RUNNING].includes(task.status);
    },
    showTaskRetryBtn(task) {
      const { FAILURE, CANCEL } = ASYNC_TASK_STATUS;
      return [FAILURE, CANCEL].includes(task.status);
    },
    showTaskResumeBtn(task) {
      const { PAUSE } = ASYNC_TASK_STATUS;
      return [PAUSE].includes(task.status);
    },
    async handleToggleAsyncTaskList() {
      await this.listDockTask();
      if (this.asyncTaskList.length && !this.showAsyncTaskList) {
        this.showAsyncTaskList = true;
      } else {
        this.showAsyncTaskList = false;
      }
    },
    async listDockTask() {
      const res = await this.$services.dmAsyncTaskDockTaskListDockTask();

      if (res.success) {
        this.asyncTaskList = res.data.map((task) => {
          if (task.processType === 'PROGRESS' && task.processValue) {
            task.processPercent = parseFloat(task.processValue);
          }

          return task;
        });
      }
    },
    handleAsyncEvent(data) {
      const { object } = data;
      if (object.processType === 'PROGRESS' && object.processValue) {
        object.processPercent = parseFloat(object.processValue);
      }
      const index = this.asyncTaskList.findIndex((task) => task.id === object.id);
      if (index > -1) {
        if (object.status === 'COMPLETE' || object.status === 'CANCEL') {
          this.asyncTaskList.splice(index, 1);
          if (!this.asyncTaskList.length) {
            this.showAsyncTaskList = false;
          }
        } else {
          this.asyncTaskList[index] = object;
        }
      }
      if (index === -1 && object.status === 'RUNNING') {
        this.asyncTaskList.unshift(object);
        this.showAsyncTaskList = true;
      }

      this.$forceUpdate();
    }
  },
  beforeUnmount() {
    this.workspaceUnmounted = true;
    document.removeEventListener('visibilitychange', this.handleWorkspaceVisibility);
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_ASYNC_EVENT);
    this.$bus.off(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_OPEN);
    this.$bus.off(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_CLOSE);
    this.$bus.off(EVENT_BUS_NAME_LIST.SET_DATA_SOURCE_STATUS);
  }
};
</script>
<style scoped lang="less">
.sql-workspace--compact {
  > .app-page-tabs {
    flex-shrink: 0;
  }

  :deep(.data-source-container),
  :deep(.table-list-container) {
    width: 100% !important;
    min-width: 0 !important;
    flex: 1;
  }

  :deep(.tree-resize),
  :deep(.table-list-resize),
  :deep(.data-source-sidebar-toggle),
  .sql-sidebar-toggle-slot {
    display: none;
  }

  .query-editor-container {
    overflow-y: auto;
  }
}

.workspace-connection-notice {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  color: var(--text-secondary);
  background: var(--bg-secondary);
}

.connection-error-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  overflow-wrap: anywhere;
  white-space: pre-wrap;
  text-align: center;
}

.connection-error-icon {
  flex-shrink: 0;
}

// common style
:deep(.icon-v2-hover:hover) {
  vertical-align: middle;
  color: var(--primary-color);
}

:deep(.radius-hover:hover) {
  vertical-align: middle;
  color: var(--primary-color);
}

// tab Bar
.tab-wrap {
  display: flex;
  height: 44px;
  flex: 0 0 44px;
  justify-content: flex-start;

  .sql-sidebar-toggle-slot {
    position: relative;
    width: 0;
    height: 44px;
    flex: 0 0 0;
    overflow: hidden;
    transition:
      width 0.26s cubic-bezier(0.4, 0, 0.2, 1),
      flex-basis 0.26s cubic-bezier(0.4, 0, 0.2, 1);

    &--visible {
      width: 44px;
      flex-basis: 44px;
    }
  }

  .sql-tab-sidebar-toggle {
    position: absolute;
    inset: 0;
    display: flex;
    width: 44px;
    height: 44px;
    align-items: center;
    justify-content: center;
    padding: 0;
    border: 0;
    border-right: 1px solid var(--border-primary);
    border-bottom: 1px solid var(--border-primary);
    border-radius: 0;
    background: var(--bg-secondary);
    color: var(--text-tertiary);
    cursor: pointer;
    transition:
      color 0.2s ease,
      background-color 0.2s ease;

    &:hover,
    &:focus-visible {
      color: var(--text-primary);
      background: var(--bg-hover);
    }

    &:focus-visible {
      outline: 1px solid var(--primary-color);
      outline-offset: -2px;
    }
  }

  .sql-sidebar-toggle-enter-active {
    transition:
      opacity 0.16s ease-out 0.08s,
      transform 0.2s cubic-bezier(0.22, 1, 0.36, 1) 0.06s;
  }

  .sql-sidebar-toggle-leave-active {
    transition:
      opacity 0.1s ease-in,
      transform 0.12s ease-in;
  }

  .sql-sidebar-toggle-enter-from {
    opacity: 0;
    transform: translateX(-4px);
  }

  .sql-sidebar-toggle-leave-to {
    opacity: 0;
    transform: translateX(-3px);
  }

  @media (prefers-reduced-motion: reduce) {
    .sql-sidebar-toggle-slot,
    .sql-sidebar-toggle-enter-active,
    .sql-sidebar-toggle-leave-active {
      transition: none;
    }
  }

  .sql-tabs-style {
    height: 44px;
    width: auto;
    min-width: 0;
    flex: 1 1 auto;

    :deep(.ant-tabs-nav) {
      height: 44px;
      margin: 0;
    }

    :deep(.ant-tabs-nav-list) {
      align-items: stretch;
      min-height: 44px;
      padding-top: 0;
      box-sizing: border-box;
    }

    :deep(.ant-tabs-nav-more) {
      display: none;
    }

    :deep(.ant-tabs-tab) {
      border-bottom: none;
    }

    :deep(.ant-tabs-extra-content) {
      height: 44px;
      line-height: 44px;
      border-bottom: 1px solid var(--border-primary);
      padding-right: 8px;
    }

    :deep(.ant-tabs-nav-wrap) {
      border-bottom: 1px solid var(--border-primary) !important;
    }

    :deep(.ant-tabs-tab) {
      height: 44px;
      align-items: center;
      margin: 0 !important;
      background-color: var(--bg-tertiary) !important;
      border-color: var(--border-primary) !important;
      border-top: 0 !important;
      border-left: 0 !important;
      color: var(--text-secondary) !important;
      border-radius: 0 !important;
      padding: 0 8px !important;

      &:hover {
        color: var(--primary-color) !important;
      }
    }

    :deep(.ant-tabs-tab-active) {
      background-color: var(--bg-primary) !important;
      border-bottom-color: var(--bg-primary) !important;
      color: var(--text-primary) !important;
    }

    .tab-menus {
      padding: 6px 7px 7px;
      height: 32px;
      width: 32px;
    }
  }
}

.tab-title-text {
  display: inline-block;
  max-width: 110px;
  margin-left: 4px;
  margin-right: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
  line-height: 1.4;
}

.schema-select-style {
  display: block;
  flex: 0 1 240px;
  width: 240px;
  min-width: 160px;
  max-width: 240px;
  margin: 0;
  font-size: 12px;

  :deep(.vs__dropdown-toggle) {
    padding: 0;
  }

  :deep(.vs__selected) {
    margin: 2px 0 0;
    padding: 0;
  }

  :deep(.vs__search) {
    margin: 2px 0 0;
  }

  :deep(.vs__search:focus) {
    margin: 2px 0 0;
  }
}

.query-schema-select__content {
  display: flex;
  width: 100%;
  max-width: 680px;
  min-width: 0;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  color: var(--text-secondary);
  font-size: 12px;
}

.query-connection-label {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.query-connection-icon {
  flex: 0 0 auto;
}

// tab dropdown menu

.dropdown-item {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 6px;
  width: 100%;
  min-width: 240px;

  .dropdown-item-icon {
    flex-shrink: 0;
  }

  .dropdown-item-title {
    flex-shrink: 1;
    min-width: 0;
    max-width: 160px;
  }

  .dropdown-item-desc {
    flex-shrink: 1;
    min-width: 0;
    color: var(--text-secondary);
    font-size: 12px;
  }

  .dropdown-item-close {
    margin-left: auto;
    flex-shrink: 0;
    width: 16px;
  }
}

.empty-state-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 200px);
}

:global(.sql-tab-dropdown .ant-dropdown-menu-item) {
  padding: 6px 12px;
}

[data-theme='dark'] {
  .tab-wrap .sql-tabs-style {
    :deep(.ant-tabs-tab-active) {
      background-color: var(--bg-select) !important;
      color: var(--text-primary) !important;
    }

    :deep(.ant-tabs-extra-content) {
      border-bottom: 1px solid var(--border-primary);
    }
  }
}
.font-size-adjust {
  font-size: 14px !important;
  margin: 0 10px;
}
</style>
