<template>
  <div class="sql-home-container">
    <div class="content">
      <data-source-tree
        ref="dataSourceTree"
        :current-tab="currentTab"
        :tree-data="treeData"
        :list-levels="listLevels"
        :detail-levels="detailLevels"
        :list-leaf="listLeaf"
        :get-node-data="getDataSourceNodeData"
        :get-node-by-key="getNodeByKey"
        :get-data-source-list="getDataSourceData"
        :handle-add-tab="handleAddTab"
        :set-loading="setLoading"
        :refresh-tab-select-options="refreshTabSelectOptions"
      />
      <div class="query-container">
        <div class="content">
          <div class="tab-wrap">
            <div class="sql-tabs-style" v-if="tabs.length">
              <a-tabs type="card" v-model:activeKey="active" @tabClick="handleChangeTab" :key="tabsKey">
                <a-tab-pane v-for="(tab, index) in tabs" :key="tab.key">
                  <template #tab>
                    <div @contextmenu.prevent.stop="onContextmenu($event, tab)">
                      <div
                        v-if="tab.isEditing"
                        style="position: absolute; width: 8px; height: 8px; border-radius: 50%; background: green; left: 13px"
                      />
                      <cc-svg-icon name="TABLE" v-if="tab.icon === 'Table'" style="display: inline-block" />
                      <CustomIcon :type="tab.icon" v-else />
                      <span style="margin-left: 5px; margin-right: 5px">{{ tab.title }}</span>
                      <CustomIcon
                        class="close-icon"
                        type="icon-v2-close2"
                        hoverStyle
                        customStyle="radius-hover"
                        @click.native="handleCloseTab(tab.key)"
                      />
                    </div>
                  </template>
                </a-tab-pane>
                <template #rightExtra>
                  <a-dropdown trigger="click" placement="bottom" v-if="tabs.length">
                    <CustomIcon type="icon-v2-ArrowDown" hoverStyle customStyle="icon-v2-hover" />
                    <template #overlay>
                      <a-menu :selectedKeys="[active]">
                        <a-menu-item v-for="tab in tabs" :key="tab.key" :name="tab.key" @click="handleChangeTab(tab.key)">
                          <div class="dropdown-item">
                            <CustomIcon :type="tab.icon" />
                            <span style="margin-left: 5px">{{ tab.title }}</span>
                            <span class="truncate" style="margin-left: 5px">
                              [{{ tab.node.INSTANCE.attr.dsInstanceDesc || tab.node.INSTANCE.attr.dsInstance }}]
                            </span>
                            <div class="dropdown-item-close">
                              <CustomIcon type="icon-v2-close2" customStyle="icon-v2-hover" hoverStyle @click.native="handleCloseTab(tab.key)" />
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
          <!-- 空状态 -->
          <div v-if="!tabs.length" class="empty-state-container">
            <SqlEmptyState :has-datasource="hasDatasource" />
          </div>

          <div class="query-content-container" v-if="tabs.length">
            <loading :active.sync="fullLoading" :is-full-page="false"></loading>
            <div v-if="currentTab.type === TAB_TYPE.QUERY && globalDsSetting[currentTab.dsType]" class="query-content">
              <div class="query-schema-select">
                <div style="display: flex; align-items: center">
                  <div class="status">
                    <CheckCircleOutlined
                      v-if="successStatus"
                      :style="{ fontSize: '16px', cursor: 'pointer', color: '#52c41a' }"
                      @click="handleClickDsStatusIcon"
                    />
                    <a-tooltip v-else placement="left">
                      <CloseCircleOutlined :style="{ fontSize: '16px', cursor: 'pointer', color: '#ff4d4f' }" @click="handleClickDsStatusIcon" />
                      <template #title>
                        <div v-if="!socket.connected">{{ socket.msgContent }}</div>
                        <div v-if="!currentTab.connected">{{ currentTab.msgContent }}</div>
                      </template>
                    </a-tooltip>
                  </div>
                  <a-select
                    v-if="currentTab.selectOptions"
                    class="schema-select-style"
                    v-model:value="currentTab.selectValue"
                    show-search
                    size="small"
                    :options="currentTab.selectOptions || []"
                    @select="handleChangeSchema"
                  ></a-select>
                  <div style="white-space: nowrap">
                    @{{ currentTab.node.INSTANCE.attr.dsHost }}【{{
                      currentTab.node.INSTANCE.attr.dsInstanceDesc || currentTab.node.INSTANCE.attr.dsInstance
                    }}】
                  </div>
                </div>
              </div>
              <div class="query">
                <TableList
                  ref="tableList"
                  :get-node-data="getTableNodeData"
                  :handle-add-tab="handleAddTab"
                  :list-leaf="listLeaf"
                  :table-list-loading="tableListLoading"
                  :currentTab="currentTab"
                  :handle-query-table="handleQueryTable"
                  :rdb-object-detail="rdbObjectDetail"
                />
                <div class="query-editor-container">
                  <div class="layout-content-main">
                    <SqlViewer
                      ref="sqlViewer"
                      :handleGetDsSetting="handleGetDsSetting"
                      :createSession="createSession"
                      :storeQueryTabs="storeQueryTabs"
                      :tab="currentTab"
                      :tabs="tabs"
                      :completion-data="completionData"
                      :rdb-object-detail="rdbObjectDetail"
                      :handle-click-ds-status-icon="handleClickDsStatusIcon"
                    />
                  </div>
                  <div ref="result" class="result-wrapper">
                    <Result :id="`result_${currentTab.key}`" :ref="`result_`" :resultList="currentTab.resultList" :tab="currentTab" />
                  </div>
                </div>
              </div>
            </div>
            <div v-if="currentTab.type === TAB_TYPE.STRUCT" class="query-content">
              <StructView
                :handleClickDsStatusIcon="handleClickDsStatusIcon"
                :tab="currentTab"
                :set-active-key="setActiveKey"
                :handle-refresh-tables="handleRefreshTables"
                :store-query-tabs="storeQueryTabs"
                ref="struct-view"
              />
            </div>
            <div v-if="currentTab.type === TAB_TYPE.DATA" class="query-content">
              <lucky-sheet-data-view
                :tab="currentTab"
                ref="data-view"
                :set-loading="setLoading"
                :handle-click-ds-status-icon="handleClickDsStatusIcon"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 隐藏，避免双重报错 -->
    <CCModal v-model="showConnectedModal" :title="$t('cuo-wu')" :zIndex="1100">
      <div>{{ connectedInstance.connectedMsg }}</div>
      <template #footer>
        <Button @click="handleCloseConnectedModal(false)">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="handleCloseConnectedModal(true)">
          {{ $t('zhong-xin-lian-jie') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons-vue';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';
import deepClone from 'lodash.clonedeep';
import { Modal } from 'ant-design-vue';
import { mapGetters, mapMutations, mapState } from 'vuex';
import dayjs from 'dayjs';
import SqlViewer from '@/views/sql/components/SqlViewer';
import Result from '@/views/sql/components/Result';
import './style/index.less';
import { ACTION_TYPE, BIZ_TYPE, TAB_TYPE } from '@/const';
import TableList from '@/views/sql/components/TableList';
import StructView from '@/views/sql/components/StructView';
import DataSourceTree from '@/views/sql/components/DataSourceTree';
import SqlEmptyState from '@/views/sql/components/SqlEmptyState';
import { clearAllPending } from '@/services/http/cancelRequest';
import LuckySheetDataView from '@/views/sql/components/LuckySheetDataView';
import browseMixin from '@/mixins/browseMixin';
import { UPDATE_EDITOR_SET } from '@/store/mutationTypes';
import { ASYNC_TASK_STATUS, SOCKET_TYPE, hasSchema, noStruct, WS_REQ_QUERY_TYPE, WS_TYPE } from '@/utils';
import { sendWebSocket } from '@/services/socket';
import sqlMixin from '@/mixins/sqlMixin';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import { nanoid } from 'nanoid';
import { TabManager } from '@/views/sql/tabManager';
import CustomIcon from '@/components/function/CustomIcon.vue';
import ContextMenu from '@imengyu/vue3-context-menu';

window.luckysheetData = {
  activeKey: '',
  storeUserImage: {}
};

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
    CheckCircleOutlined,
    CloseCircleOutlined,
    CustomIcon,
    LuckySheetDataView,
    DataSourceTree,
    StructView,
    SqlViewer,
    Result,
    TableList,
    SqlEmptyState,
    Loading
  },
  data() {
    return {
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
      rdbObjectDetailLoading: true,
      completionData: {},
      TAB_TYPE,
      active: '',
      tabsKey: 0,
      treeData: [],
      newMode: true,
      TAB_ACTION,
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
      hasSchema,
      noStruct,
      datasourceList: [],
      hasDatasource: false
    };
  },
  beforeRouteEnter(to, from, next) {
    console.log(to, from);
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
    document.addEventListener('visibilitychange', () => {
      const state = document.visibilityState;
      if (state === 'visible') {
        this.handleGetDsSetting();
      }
    });
    await this.listDockTask();
    await this.setDataViewImage();
    await this.getDataSourceData();
    await this.getLocalQueryData();
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_ASYNC_EVENT, this.handleAsyncEvent);
    this.$bus.on(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_CLOSE, this.handleSocketConnectionClose);
    this.$bus.on(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_OPEN, this.handleGetDsSetting);
    this.$bus.on(EVENT_BUS_NAME_LIST.SET_DATA_SOURCE_STATUS, (data) => this.setInstanceErrorIcon(data));

    this.currentTab.executeInfoScrollDown = true;
  },
  computed: {
    fullLoading() {
      return this.loading;
    },
    successStatus() {
      return this.socket.connected && this.currentTab.connected;
    },
    ...mapState(['globalDsSetting', 'userInfo', 'socket']),
    ...mapGetters(['isDesktop', 'getNodeType', 'getLeafGroup', 'getLevels', 'getLeafExpand'])
  },
  methods: {
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
        customClass: 'custom-class',
        zIndex: 3,
        minWidth: 100
      });
    },
    async handleCloseConnectedModal(reConnected = true) {
      this.showConnectedModal = false;
      if (reConnected) {
        await this.handleTestConnect();
      }
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
      const treeData = this.$refs.dataSourceTree.handleGetTreeData();
      return treeData;
    },
    async getDataSourceData() {
      this.treeData = [];
      await this.listLevels();
      if (this.treeData.length) {
        this.hasDatasource = true;
        await this.listLevels(this.treeData[0], {}, () => {});
      }
    },
    setDataViewImage() {
      const pkImg = new Image();
      pkImg.src =
        'data:image/png;base64,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';
      window.luckysheetData.storeUserImage.pkImg = pkImg;

      const ukImg = new Image();
      ukImg.src =
        'data:image/png;base64,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';
      window.luckysheetData.storeUserImage.ukImg = ukImg;
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
      const res = await this.$services.dmBrowseDetailLevels({
        data: {
          levels: this.browseGenLevelsData(node),
          refreshCache: isRefreshCache
        }
      });

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
    async listLevels(node = null, params = {}, resolve, reject, isRefreshCache = false) {
      console.log('listLevels', node);
      if (!node) {
        this.treeData = [];
      }

      let statusRes = '';
      if (node?.INSTANCE?.id && node?.nodeType === 'INSTANCE') {
        statusRes = await this.$services.dmQueryFetchDsStatusConf({
          data: {
            dsId: node?.INSTANCE?.id
          }
        });
      }

      try {
        const levelRes = await this.$services.dmBrowseListLevels({
          data: {
            levels: node ? this.browseGenLevelsData(node) : [],
            cancelPending: true,
            refreshCache: isRefreshCache
          }
        });
        if (levelRes.success && levelRes.data && levelRes.data.length) {
          const children = [];
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
                leaf.connected = objAttr.status === 'Normal';
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

          if (statusRes?.data?.dsStatus && node?.nodeType === 'INSTANCE') {
            await this.$refs.dataSourceTree.handleUpdateNode(node.key, {
              ...node,
              connected: statusRes.data.dsStatus === 'Normal'
            });
          }

          // if (node) {
          //   this.$refs.dataSourceTree.handleSetExpandedKeys(node);
          // }

          if (levelRes.data.length) {
            if (node) {
              if (resolve) {
                resolve(children);
              } else {
                this.$refs.dataSourceTree.handleAppendList(node.key, children);
              }
            } else {
              await this.$refs.dataSourceTree.handleSetData(this.treeData, true);
            }
            // await this.$refs.dataSourceTree.handleSetData(this.treeData, true);
            // if (node) {
            //   await this.$refs.dataSourceTree.handleSetSelected(node.key);
            // }
          } else {
            if (resolve) {
              resolve();
            }
          }
        } else {
          console.log('fail', levelRes);
          if (typeof levelRes === 'object' && levelRes.toString().includes('Cancel')) {
            console.log('reset');
            return;
          }

          if (levelRes.code !== '10103' && levelRes.code !== '10201') {
            // this.$refs.dataSourceTree.handleSetData(this.treeData);
            if (resolve) {
              resolve();
            }
          }
        }
      } catch (e) {
        console.error(e);
      }
    },
    async setInstanceErrorIcon(data) {
      if (this.currentTab && this.currentTab.node && this.currentTab.node.INSTANCE.id === data.instanceId) {
        let tempConnected = null;
        const res = await this.$services.dmQueryFetchDsStatusConf({
          data: {
            dsId: data.instanceId
          }
        });
        tempConnected = res?.data?.dsStatus && res?.data?.dsStatus === 'Normal';

        this.currentTab.connected = tempConnected;
        this.currentTab.msgContent = data.msgContent;
      }

      if (this.$refs.dataSourceTree) {
        const treeData = this.$refs.dataSourceTree.handleGetTreeData();
        treeData.forEach((env) => {
          if (env.children) {
            env.children.forEach(async (instance) => {
              if (instance.INSTANCE.id === data.instanceId) {
                instance.connected = data.connected;
                instance.connectedMsg = data.msgContent;
                await this.$refs.dataSourceTree.handleUpdateNode(instance.key, {
                  connected: data.connected,
                  connectedMsg: data.msgContent
                });
                if (!data.connected && !data?.noModal) {
                  this.showConnectedModal = true;
                  this.connectedInstance = { code: data.code, ...instance };
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
    async listLeaf(isRefreshCache = false) {
      const { node, leafType } = this.currentTab;

      this.tableListLoading = true;

      const leafList = [];

      // 实例名称是否合法
      const noLegal = node?.key?.includes('/');
      if (noLegal) {
        this.$Message.warning(this.$t('fa-mi-ming-cheng-bu-zhi-chi'));
        return;
      }

      try {
        const leafRes = await this.$services.dmBrowseListLeaf({
          data: {
            levels: node.levels.map((l) => this.currentTab.node[l].id),
            leafType,
            refreshCache: isRefreshCache
          }
        });

        if (leafRes.success) {
          const completionKey = node.key;
          const obj = {};
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

          this.currentTab[leafType].treeData = leafList;
          await this.$refs.tableList.handleSetData(leafList);
          await this.$refs.tableList.handleFilter(this.currentTab[this.currentTab.leafType].searchKey);
        } else {
          this.setInstanceErrorIcon(this.currentTab?.node?.INSTANCE?.id);
          if (this.$refs.tableList) {
            await this.$refs.tableList.handleSetData(leafList);
          }
        }
      } catch (e) {
        this.setInstanceErrorIcon(this.currentTab?.node?.INSTANCE?.id);
        if (this.$refs.tableList) {
          await this.$refs.tableList.handleSetData(leafList);
        }
      } finally {
        this.tableListLoading = false;
      }

      this.storeQueryTabs();
    },
    async handleAddTab(type, node, options = {}) {
      this.storeActiveTab();
      if (this.disableAddTab()) {
        return;
      }
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
        if (type === TAB_TYPE.QUERY) {
          await this.createSession(this.currentTab);
          await this.listLeaf();
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

      if (res.success) {
        tab.support = res.data;
        tab.connected = res.data.dsStatus === 'Normal';
        tab.msgContent = res.data.dsStatusMessage;
        tab.isolation = res.data.isolation.defaultValue;
        tab.autoCommit = res.data.autoCommit.defaultValue === 'true';
        tab.readOnly = res.data.readOnly.defaultValue === 'true';
      }

      switch (type) {
        case TAB_TYPE.QUERY:
          tab.schemaParentKey = node._parent.key;
          // 确保 children 是数组且包含对象
          if (Array.isArray(node._parent.children)) {
            tab.selectOptions = node._parent.children
              .filter((child) => child && typeof child === 'object' && child.title)
              .map((child) => ({ value: child.title, label: child.title }));
          } else {
            tab.selectOptions = [];
          }
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
            list: []
          };
          tab.text = '';
          tab.icon = node.INSTANCE.attr.dsType;
          tab.sessionId = '';
          tab.executeInfo = [];
          // 根据节点层级决定使用哪个 leafGroup
          const lastLevel = node.levels[node.levels.length - 1];
          let leafGroup = this.getLeafGroup(tab.node.INSTANCE.attr.dsType, lastLevel);

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
          await this.createSession(this.currentTab);
          await this.listLeaf();
          break;
        case TAB_TYPE.STRUCT:
        case TAB_TYPE.DATA:
          break;
        default:
          break;
      }
      this.storeQueryTabs();
    },
    refreshTabSelectOptions(key) {
      const node = this.$refs.dataSourceTree.handleGetNode(key);
      if (node.key && node._parent && node._parent.children) {
        this.tabs.forEach((tab) => {
          if (tab.type === TAB_TYPE.QUERY && tab.schemaParentKey === key) {
            // 确保 children 是数组且包含对象
            if (Array.isArray(node.children)) {
              tab.selectOptions = node.children
                .filter((child) => child && typeof child === 'object' && child.title)
                .map((child) => ({ value: child.title, label: child.title }));
            } else {
              tab.selectOptions = [];
            }
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
      console.log('getLocalQueryData', this.userInfo.uid);
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
              // 确保 selectOptions 存在
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
        console.log(e);
      }
    },
    storeActiveTab() {
      console.log('store active tab', this.currentTab.type, this.currentTab.tabId);
      if (this.currentTab.type === 'DATA') {
        const { uid } = this.userInfo;
        if (this.currentTab.options) {
          delete this.currentTab.options.cellRightClickConfig;
          delete this.currentTab.options.hook;
        }
        this.tabManager.setTabData({ uid, id: this.currentTab.tabId, data: this.currentTab });
      }
    },
    storeQueryTabs() {
      console.log('store query tabs');
      const { uid } = this.userInfo;
      const key = `clouddm_new_tabs_${uid}`;

      if (this.currentTab.type === TAB_TYPE.QUERY && this.$refs.sqlViewer && this.$refs.sqlViewer.monacoEditor) {
        this.currentTab.text = this.$refs.sqlViewer.monacoEditor.getValue();
      }

      const tabData = deepClone(this.tabs);

      tabData.forEach((tab) => {
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
      const { expand, selected, selectedNode } = params;
      const { node } = this.currentTab;
      try {
        const res = await this.$services.dmBrowseRdbObjectDetail({
          data: {
            levels: this.browseGenLevelsData(this.currentTab.node),
            targetName: tableName,
            targetType: this.currentTab.leafType,
            refreshCache: isRefreshCache
          }
        });

        if (res.success) {
          const curNode = this.getNodeByKey(this.currentTab[this.currentTab.leafType].treeData, `${node.key}.\`${tableName}\``);
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
          this.completionData[this.currentTab.node.key][this.currentTab.leafType][tableName].columnList = columnList;
          // await this.$refs.tableList.handleSetData(this.currentTab[this.currentTab.leafType].treeData);
          if (selected) {
            if (selectedNode) {
              this.$refs.tableList.handleSetSelected(selectedNode);
            } else {
              this.$refs.tableList.handleSetSelected(curNode);
            }
          }
          this.setInstanceErrorIcon(this.currentTab?.node?.INSTANCE?.id, true);
          resolve(children);
        } else {
          this.setInstanceErrorIcon(this.currentTab?.node?.INSTANCE?.id);
          // await this.$refs.tableList.handleSetData(this.currentTab[this.currentTab.leafType].treeData);
          resolve();
        }
      } catch (e) {
        console.error(e);
        this.setInstanceErrorIcon(this.currentTab?.node?.INSTANCE?.id);
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

      await this.$services.dmQueryClosesSession({ data: { sessionId } });
    },
    handleTabAction(actionType) {
      const index = this.tabs.findIndex((tab) => tab.key === this.contextData.key);
      switch (actionType) {
        case TAB_ACTION.DELETE_ALL:
          this.tabs = [];
          this.active = '';
          break;
        case TAB_ACTION.DELETE_OTHER_ALL:
          this.currentTab = this.tabs[index];
          const tabs = [];
          for (let i = 0; i < this.tabs.length; i++) {
            if (i !== index) {
              tabs.push(this.tabs[i]);
            }
          }
          this.closesSession(tabs);
          const removeTabIds = [];
          this.tabs.forEach((tab) => {
            if (tab.tabId !== this.currentTab.tabId) {
              removeTabIds.push(tab.tabId);
            }
          });

          const tabInstance = this.tabManager.getInstance();
          if (tabInstance) {
            tabInstance.bulkDelete(removeTabIds);
          }
          this.tabs = [this.currentTab];
          this.active = this.currentTab.key;
          break;
        case TAB_ACTION.DELETE_LEFT_ALL:
          break;
        case TAB_ACTION.DELETE_RIGHT_ALL:
          break;
        default:
          break;
      }

      this.storeQueryTabs();
    },
    handleCloseTab(targetKey) {
      const editTabList = this.tabs.filter((tab) => tab.key === targetKey);
      if (editTabList.length) {
        const editTab = editTabList[0];
        if (editTab.type === 'DATA' && this.dataViewIsEditing(editTab)) {
          Modal.confirm({
            title: this.$t('ti-shi'),
            content: this.$t(
              'dang-qian-ye-mian-huan-you-wei-ti-jiao-de-xiu-gai-qing-xian-ti-jiao-hou-guan-bi-dian-ji-li-ji-guan-bi-jiang-diu-shi-dang-qian-xiu-gai'
            ),
            okText: this.$t('li-ji-guan-bi'),
            onOk: () => this.removeTab(targetKey)
          });
        } else if (editTab.type === 'STRUCT' && this.structViewIsEditing(editTab)) {
          Modal.confirm({
            title: this.$t('ti-shi'),
            content: this.$t(
              'dang-qian-biao-jie-gou-huan-you-wei-ti-jiao-de-xiu-gai-qing-xian-ti-jiao-hou-guan-bi-dian-ji-li-ji-guan-bi-jiang-diu-shi-dang-qian-xiu-gai'
            ),
            okText: this.$t('li-ji-guan-bi'),
            onOk: () => this.removeTab(targetKey)
          });
        } else {
          this.removeTab(targetKey);
        }
      }
    },
    removeTab(key) {
      let deleteIndex = 0;
      let deleteTabId = '';
      this.tabs.forEach((tab, index) => {
        if (tab.key === key) {
          delete window.luckysheetData[this.active];
          deleteIndex = index;
          deleteTabId = tab.tabId;
          this.tabs.splice(index, 1);
          this.closesSession([tab]);
          this.tabManager.deleteTabData(tab.tabId);
        }
      });

      const len = this.tabs.length;
      if (len > 0) {
        const activeIndex = deleteIndex ? deleteIndex - 1 : 0;
        this.active = this.tabs[activeIndex].key;
        this.tabsKey++;
        window.luckysheetData.activeKey = this.active;
        this.handleChangeTab(this.active, true);
      } else {
        this.active = '';
        this.currentTab = {};
      }

      this.storeQueryTabs();
      if (deleteTabId) {
        this.tabManager.deleteTabData(deleteTabId);
      }
    },
    async createSession(tab, next) {
      this.sessionLoading = true;
      const { node, autoCommit, isolation } = tab;
      try {
        const res = await this.$services.dmQueryCreateSession({
          data: {
            levels: this.browseGenLevelsData(node),
            initAutoCommit: autoCommit,
            initIsolation: isolation
          }
        });
        if (res.success) {
          this.tabs.forEach((item) => {
            if (item.key === tab.key) {
              item.sessionId = res.data.sessionId;
            }
          });
          if (next) {
            next();
          }
        }
      } finally {
        this.sessionLoading = false;
      }
    },
    handleQueryTable(text) {
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
      console.log(this.currentTab.type);
      if (this.currentTab.type === 'STRUCT') {
        console.log('editing', this.structViewIsEditing(this.currentTab));
        this.currentTab.isEditing = this.structViewIsEditing(this.currentTab);
      } else if (this.currentTab.type === 'DATA') {
        console.log('editing', this.dataViewIsEditing(this.currentTab));
        this.currentTab.isEditing = this.dataViewIsEditing(this.currentTab);
      }

      console.log('change tab', activeKey);
      const changeTab = async (key) => {
        if (this.$refs['data-view']) {
          this.$refs['data-view'].handleEmptyUpdate();
        }
        this.$nextTick(async () => {
          this.tabs.forEach((item) => {
            if (item.key === key) {
              clearAllPending();
              this.cancelAllLoading();
              this.storeQueryTabs();
              this.currentTab = item;
              this.active = key;
              if (this.$refs.tableList && item.leafType && item[item.leafType] && item[item.leafType].treeData) {
                this.$refs.tableList.selectedNode = null;
                this.$refs.tableList.handleSetData(item[item.leafType].treeData);
              }
            }
          });
          await this.handleGetDsSetting();
          // 保证刷新浏览器后，切换tab也能正常刷新出来数据刷新
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
    async handleTestConnect() {
      if (this.connectedInstance.ENV.id && this.connectedInstance.INSTANCE.id) {
        try {
          const res = await this.$services.dmDataSourceTestConnect({
            data: {
              levels: [this.connectedInstance.ENV.id, this.connectedInstance.INSTANCE.id]
            }
          });
        } catch (e) {
          console.error(e);
        }
      }
    },
    async handleClickDsStatusIcon() {
      if (this.currentTab && this.currentTab.node.ENV?.id && this.currentTab.node.INSTANCE?.id) {
        try {
          const res = await this.$services.dmDataSourceTestConnect({
            data: {
              levels: [this.currentTab.node.ENV.id, this.currentTab.node.INSTANCE.id]
            }
          });
        } catch (e) {
          console.error(e);
        }
      }
    },
    async handleGetDsSetting(restore = false) {
      if (this.currentTab && this.currentTab.dsId) {
        // 如果已经有 websocket 返回的 msgContent，就不调用接口
        const hasWsMessage = this.currentTab.msgFromWs;

        if (hasWsMessage) {
          return;
        }

        const res = await this.$services.dmQueryFetchDsStatusConf({
          data: {
            dsId: this.currentTab.dsId
          }
        });

        if (res.success) {
          this.currentTab.support = res.data;
          this.currentTab.connected = res.data.dsStatus === 'Normal';

          if (!restore) {
            this.currentTab.msgContent = res.data.dsStatusMessage;
            this.currentTab.isolation = res.data.isolation.defaultValue;
            this.currentTab.autoCommit = res.data.autoCommit.defaultValue === 'true';
            this.currentTab.readOnly = res.data.readOnly.defaultValue === 'true';
          }

          if (this.currentTab.sessionId) {
            sendWebSocket(
              {
                type: WS_TYPE.WS_REQ_QUERY,
                object: {
                  sessionId: this.currentTab.sessionId,
                  queryType: WS_REQ_QUERY_TYPE.RECOVER_STATUS,
                  levels: this.browseGenLevelsData(this.currentTab.node),
                  rdbAutoCommit: this.currentTab.autoCommit,
                  rdbReadOnly: this.currentTab.readOnly,
                  rdbIsolation: this.currentTab.isolation
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
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_ASYNC_EVENT);
    this.$bus.off(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_OPEN);
    this.$bus.off(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_CLOSE);
    this.$bus.off(EVENT_BUS_NAME_LIST.SET_DATA_SOURCE_STATUS);
  }
};
</script>
<style scoped lang="less">
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
  justify-content: right;

  .sql-tabs-style {
    width: calc(100% - 29px);

    :deep(.ant-tabs-nav) {
      margin: 0;
    }

    :deep(.ant-tabs-nav-more) {
      display: none;
    }

    :deep(.ant-tabs-tab) {
      border-bottom: none;
    }

    :deep(.ant-tabs-extra-content) {
      height: 40px;
      line-height: 40px;
      border-bottom: 1px solid #ccc;
      padding-right: 5px;
    }

    // 修改tab下边框颜色，使其更深
    :deep(.ant-tabs-nav-wrap) {
      border-bottom: 1px solid #ccc !important;
    }

    :deep(.ant-tabs > .ant-tabs-nav .ant-tabs-nav-list) {
      height: 90%;
      top: 3px;
    }

    // 修改未被选中的tab颜色，使其更深
    :deep(.ant-tabs-tab) {
      background-color: #f5f5f5 !important;
      border-color: #d9d9d9 !important;
      color: #666666 !important;

      // hover 时不再加深背景色，只让文字变为主题色
      &:hover {
        color: var(--primary-color) !important;
      }
    }

    // 修改被选中的tab颜色
    :deep(.ant-tabs-tab-active) {
      background-color: #ffffff !important;
      border-bottom-color: #666666 !important;
      color: #333333 !important;
    }

    .tab-menus {
      padding: 6px 7px 7px;
      height: 32px;
      width: 32px;
    }
  }
}

.schema-select-style {
  display: inline-block;
  width: 400px;
  margin-left: 5px;
  margin-right: 5px;
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

// tab dropdown menu

.dropdown-item {
  display: flex;
  justify-content: right;

  .dropdown-item-close {
    padding-left: 5px;
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
