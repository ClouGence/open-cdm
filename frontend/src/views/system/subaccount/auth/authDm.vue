<template>
  <div class="auth-container-wrapper">
    <div v-if="pageLoading" class="page-loading-mask">
      <a-spin size="large" tip="加载中..." />
    </div>
    <div class="header">
      <div class="left">
        <Breadcrumb v-if="isEdit">
          <BreadcrumbItem @click="goSubAccountPage" to="/system/account">
            {{ $t('zi-zhang-hao-guan-li') }}
          </BreadcrumbItem>
          <BreadcrumbItem>
            {{ $t('shu-ju-ku-shou-quan') }}
            <span v-if="$route.query.name">（{{ $route.query.name }}）</span>
          </BreadcrumbItem>
        </Breadcrumb>
        <Breadcrumb v-else>
          <BreadcrumbItem>{{ $t('yi-shou-quan-xian') }}</BreadcrumbItem>
        </Breadcrumb>
      </div>
    </div>
    <div class="auth-content">
      <div class="auth-container">
        <div class="auth" @mousemove="handleMouseMove" @mouseup="stopDragging">
          <div class="left" :style="{ width: leftWidth + 'px' }">
            <div class="auth-btns">
              <a-button
                :type="activeAuthTab === authTab.value ? 'primary' : 'default'"
                :disabled="disableAuthTab(authTab.value)"
                v-for="authTab in authTabs"
                :key="authTab.value"
                class="auth-btn"
                @click="handleSwitchAuth(authTab.value, authTab.type)"
              >
                {{ authTab.label }}
              </a-button>
            </div>
            <div class="search">
              <a-select v-if="isEdit" v-model:value="datasourceTreeSearchType" style="width: 130px" @change="onSearchTypChange">
                <a-select-option value="all">{{ $t('quan-bu') }}</a-select-option>
                <a-select-option value="authed">{{ $t('yi-shou-quan') }}</a-select-option>
                <a-select-option value="unAuth">{{ $t('wei-shou-quan') }}</a-select-option>
              </a-select>
              <a-input-search
                class="search"
                @search="onSearchKeyChange"
                :placeholder="$t('sou-suo-shu-xing-jie-gou-zhong-zhan-kai-de-nei-rong')"
                allow-clear
                v-model:value="leftTreeKeyword"
                @change="onSearchKeyChange"
              />
              <!-- <div class="operate-btn">
                <Button @click="$refs.dataSourceTree.scrollTo(originLeftTree[0]?.key)">
                  <CustomIcon type='icon-v2-BackToTop' />
                </Button>
                <Button @click="handleCloseExpand">
                  <CustomIcon type='icon-v2-PackUp' />
                </Button>
              </div> -->
            </div>
            <v-tree
              v-model="selectedNodeKey"
              animation
              :expandedKeys="expandedKeys"
              ref="dataSourceTree"
              :cascade="false"
              keyField="key"
              :emptyText="$t('zan-wu-shu-ju')"
              :render="renderNode"
              :disabled="!isEdit"
              class="datasource-tree"
              :selectable="isSingleSelect"
              :checkable="!isSingleSelect"
              :unselect-on-click="false"
              @click="leftTreeNodeClick"
              @expand="handleDsExpand"
            />
          </div>
          <div class="divider" @mousedown="startDragging" />
          <div :class="`middle ${showAuthTree ? '' : 'no-auth'}`">
            <div class="auth-tree-container">
              <a-spin class="auth-loading" v-if="loadingAuth" />
              <Tabs :animated="false" class="auth-tree" v-model="curRightTreeTab">
                <template #extra>
                  <a class="extra-tab" v-show="timeList?.[curNode.key]?.length">
                    <Poptip trigger="hover" placement="bottom-end" width="350">
                      <span>{{ $t('shou-quan-shi-jian-0') }}</span>
                      <template #content>
                        <div class="auth-time-popover">
                          <div v-for="(item, index) in processedTimeList" :key="index" class="time-range-item">
                            <div class="time-range">
                              <CustomIcon type="Time" rightMargin />
                              {{ formattedTime(item) }}
                            </div>
                            <div>{{ item?.level }}</div>
                            <div class="auth-tags">
                              <Tag v-for="(auth, authIndex) in item.auths" :key="authIndex" color="primary" class="auth-tag">
                                {{ authMap[auth] }}
                              </Tag>
                            </div>
                            <Divider v-if="index < processedTimeList.length - 1" />
                          </div>
                        </div>
                      </template>
                    </Poptip>
                  </a>
                </template>

                <TabPane :label="$t('shi-li-quan-xian')" name="Instance" :disabled="!['Instance', 'INSTANCE', 'AllType'].includes(curElementType)">
                  <v-tree
                    :emptyText="$t('zan-wu-shu-ju')"
                    :render="renderAuthNode"
                    ref="instanceTree"
                    keyField="key"
                    checkable
                    titleField="i18nName"
                    @checked-change="handleAuthCheck"
                    :defaultExpandAll="true"
                    :disableAll="previewMode || isView"
                  />
                </TabPane>
                <TabPane
                  :label="$t('catalog-quan-xian')"
                  name="CATALOG"
                  :disabled="!['Catalog', 'CATALOG', 'EXTERNAL_CATALOG', 'AllType'].includes(curElementType)"
                >
                  <v-tree
                    :emptyText="$t('zan-wu-shu-ju')"
                    :render="renderAuthNode"
                    ref="catalogTree"
                    keyField="key"
                    checkable
                    titleField="i18nName"
                    @checked-change="handleAuthCheck"
                    :defaultExpandAll="true"
                    :disableAll="previewMode || isView"
                  />
                </TabPane>
                <TabPane
                  :label="$t('schema-quan-xian')"
                  name="SCHEMA"
                  :disabled="!['Schema', 'SCHEMA', 'EXTERNAL_SCHEMA', 'AllType'].includes(curElementType)"
                >
                  <v-tree
                    :emptyText="$t('zan-wu-shu-ju')"
                    :render="renderAuthNode"
                    ref="schemaTree"
                    keyField="key"
                    checkable
                    titleField="i18nName"
                    @checked-change="handleAuthCheck"
                    :defaultExpandAll="true"
                    :disableAll="previewMode || isView"
                  />
                </TabPane>

                <TabPane :label="$t('biao-quan-xian')" name="TABLE" :disabled="!['Table', 'TABLE', 'AllType'].includes(curElementType)">
                  <v-tree
                    :emptyText="$t('zan-wu-shu-ju')"
                    :render="renderAuthNode"
                    ref="tableTree"
                    keyField="key"
                    checkable
                    titleField="i18nName"
                    @checked-change="handleAuthCheck"
                    :defaultExpandAll="true"
                    :disableAll="previewMode || isView"
                  />
                </TabPane>
              </Tabs>
              <div class="auth-tree-container-right">
                <div class="setting" v-if="!isView || previewMode">
                  <div class="label-title">
                    {{ $t('xuan-xiang') }}
                  </div>
                  <section class="option-section">
                    <div class="option-section-title">
                      <span>{{ $t('shou-quan-shi-jian') }}</span>
                      <a-button size="small" v-if="isEdit" @click="handleClearAuthTime">
                        {{ $t('qing-kong') }}
                      </a-button>
                    </div>
                    <div class="content">
                      <div class="ranges" v-if="isEdit">
                        <RadioGroup v-model="curRangeKey" type="button" @on-change="handleRangeChange">
                          <Radio class="date-btns" v-for="range in ranges1" :value="range.key" :key="range.key" :label="range.key">
                            {{ range.label }}
                          </Radio>
                        </RadioGroup>
                        <RadioGroup v-model="curRangeKey" type="button" @on-change="handleRangeChange" style="padding-top: 5px">
                          <Radio class="date-btns" v-for="range in ranges2" :value="range.key" :key="range.key" :label="range.key">
                            {{ range.label }}
                          </Radio>
                        </RadioGroup>
                      </div>
                      <div class="time">
                        <a-date-picker
                          v-model:value="authStartTime"
                          show-time
                          :disabled="!isEdit"
                          format="YYYY-MM-DD HH:mm:ss"
                          :placeholder="$t('kai-shi-shi-jian')"
                          @change="handleStartTimeChange"
                        />
                        <div class="time-mid">~</div>
                        <a-date-picker
                          v-model:value="authEndTime"
                          :disabled-date="disabledEndDate"
                          show-time
                          :disabled="!isEdit"
                          format="YYYY-MM-DD HH:mm:ss"
                          :placeholder="$t('jie-shu-shi-jian')"
                          @change="handleEndTimeChange"
                        />
                      </div>
                    </div>
                  </section>
                  <section class="option-section">
                    <div class="option-section-title">{{ $t('quan-bu-zi-yuan-quan-xian') }}</div>
                    <div class="all-resource-option">
                      <i-switch
                        true-color="#52C41A"
                        :disabled="resourceManageDisabled"
                        :loading="resourceManageLoading"
                        v-model="authTarget.resourceManage"
                        @on-change="handleResourceManageChange"
                      />
                      <div class="all-resource-tip">{{ $t('shou-quan-quan-bu-zi-yuan-gei-yong-hu') }}</div>
                    </div>
                  </section>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="option-wrap">
      <!-- main ,previous flow -->
      <Button @click="goSubAccountPage" v-if="!previewMode && isEdit" style="margin-right: 10px">
        {{ $t('fan-hui-zi-zhang-hao-lie-biao') }}
      </Button>
      <!--      <Button-->
      <!--        @click="cancelAuth"-->
      <!--        v-if="!previewMode && isEdit"-->
      <!--        style="margin-right: 10px"-->
      <!--      >-->
      <!--        {{ $t("qu-xiao") }}-->
      <!--      </Button>-->
      <Button @click="handleCancelPreviewModeForDm('cancel')" v-if="previewMode" style="margin-right: 10px">
        {{ $t('shang-yi-bu') }}
      </Button>
      <!-- main ,next flow -->
      <!-- 20250310本期暂禁用批量授权 -->
      <!-- TODO::开放批量授权能力 -->
      <Button v-if="false" @click="handleSwitchBatchModeForDm" style="margin-right: 10px">
        {{ batchMode ? $t('tui-chu-pi-liang-shou-quan') : $t('pi-liang-shou-quan') }}
      </Button>
      <!--      <Button-->
      <!--        @click="handleGoAuth"-->
      <!--        type="primary"-->
      <!--        v-if="isView && !resourceManager"-->
      <!--        style="margin-right: 10px"-->
      <!--      >-->
      <!--        {{ $t("pei-zhi") }}-->
      <!--      </Button>-->
      <Button @click="handlePreviewForDm" type="primary" v-if="!previewMode && isEdit" style="margin-right: 10px">
        {{ $t('shou-quan-yu-lan') }}
      </Button>
      <Button type="primary" @click="handleSubmit" v-if="previewMode" style="margin-right: 10px">
        {{ $t('bao-cun') }}
      </Button>
    </div>
    <a-modal v-model="showAuthedTreeModal" v-if="showAuthedTreeModal" :title="$t('shou-quan-que-ren')" :width="800">
      <div class="show-authed-tree-modal">
        <div class="left"></div>
        <div class="right"></div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="jsx">
import dayjs from 'dayjs';
import VTree from '@wsfe/vue-tree';
import deepClone from 'lodash.clonedeep';
import { mapGetters, mapState } from 'vuex';
import i18n from '@/i18n';
import { ELEMENT_REVERSE_TYPE_MAP, ELEMENT_TYPE_MAP, ELEMENT_TYPE_REF_MAP, START_RECORD_NAMES_CONUT } from './constant';
import { getResTypeToNames, findNodeByKey, fetchWithTimeout, flattenTree } from './utils';

export default {
  name: 'MyAuth',
  components: {
    VTree
  },
  data() {
    return {
      resourceManager: false,
      resourceManageLoading: false,
      globalResourceAuthId: null,
      globalResourceOriginalEnabled: false,
      globalResourceOriginalStartTime: null,
      globalResourceOriginalEndTime: null,
      authTarget: {
        uid: '',
        username: '',
        resourceManage: false,
        disable: false
      },
      selectedNodeKey: null,
      canCheckedChange: false,
      selectedCcCluster: '',
      leftWidth: 460,
      isDragging: false,
      curRangeKey: '',
      authedData: {},
      showAuthedTreeModal: false,
      batchMode: false,
      previewMode: false,
      uid: '',
      isEdit: false,
      isView: false,
      loadingAuth: false,
      activeAuthTab: 'DataSource',
      activeAuthType: 'datasource',
      authTabs: [
        {
          label: i18n.global.t('shu-ju-yuan'),
          value: 'DataSource',
          type: 'datasource'
        },
        { label: i18n.global.t('ren-wu'), value: 'DataJob', type: 'task' }
      ],
      ranges1: [
        {
          key: '1',
          label: i18n.global.t('jin-tian'),
          startTime: dayjs(),
          endTime: dayjs().endOf('day')
        },
        {
          key: '2',
          label: i18n.global.t('yi-tian'),
          startTime: dayjs(),
          endTime: dayjs().add(1, 'day')
        },
        {
          key: '3',
          label: i18n.global.t('yi-zhou'),
          startTime: dayjs(),
          endTime: dayjs().add(1, 'week')
        }
      ],
      ranges2: [
        {
          key: '4',
          label: i18n.global.t('yi-ge-yue'),
          startTime: dayjs(),
          endTime: dayjs().add(1, 'month')
        },
        {
          key: '5',
          label: i18n.global.t('ban-nian'),
          startTime: dayjs(),
          endTime: dayjs().add(6, 'month')
        },
        {
          key: '6',
          label: i18n.global.t('yi-nian'),
          startTime: dayjs(),
          endTime: dayjs().add(1, 'year')
        }
      ],
      selectedRange: {},
      datasource: {
        batchCheckedKeys: [],
        treeData: [],
        stashTreeData: [],
        originalTreeData: [],
        searchType: 'all',
        searchKey: '',
        loading: false,
        selectedNode: null
      },
      task: {
        batchCheckedKeys: [],
        treeData: [],
        stashTreeData: [],
        originalTreeData: [],
        searchKey: '',
        searchType: 'all',
        loading: false,
        selectedNode: null
      },
      authList: {},
      subAccount: {
        searchKey: '',
        selectedNode: '',
        treeData: []
      },
      auth: {
        checkedKeys: [],
        startTime: null,
        endTime: null,
        originalTreeData: [],
        batchTreeData: [],
        diffuse: false,
        treeData: [],
        searchKey: '',
        loading: false
      },
      expandedKeys: [],
      curNode: [],
      curElementType: '', // Instance ｜ Catalog ｜ Schema｜ Table |  AllType
      originLeftTree: [],
      originRightTree: {
        Instance: [],
        Schema: [],
        Catalog: [],
        Table: []
      },
      lastRightTreeData: [],
      lastLeftTreeClickNode: '',
      rightTreeKeyword: '',
      leftTreeKeyword: '',
      isSingleSelect: true,
      curRightTreeTab: 'Instance',
      leftTreeLoading: false,
      authTime: {
        startTime: null,
        endTime: null
      },
      timeList: {},
      authMap: {},
      userAuthResList: [],
      parentAuthTree: [],
      pageLoading: false
    };
  },
  computed: {
    ...mapGetters(['includesDM', 'includesCC']),
    ...mapState(['userInfo', 'globalSetting', 'dmGlobalSetting', 'productClusterList', 'myAuth']),
    getCcProductClusterList() {
      const ccList = [];
      this.productClusterList.forEach((cluster) => {
        if (cluster.product === 'CloudCanal') {
          ccList.push(cluster);
        }
      });
      return ccList;
    },
    resourceManageDisabled() {
      return !this.isEdit || this.previewMode || this.authTarget.disable || this.resourceManageLoading || !this.myAuth.includes('RDP_AUTH_MANAGE');
    },
    datasourceTreeSearchKey: {
      get() {
        return this.activeAuthType === 'datasource' ? this.datasource.searchKey : this.task.searchKey;
      },
      set(value) {
        if (this.activeAuthType === 'datasource') {
          this.datasource.searchKey = value;
        } else {
          this.task.searchKey = value;
        }
      }
    },
    datasourceTreeSearchType: {
      get() {
        return this.activeAuthType === 'datasource' ? this.datasource.searchType : this.task.searchType;
      },
      set(value) {
        if (this.activeAuthType === 'datasource') {
          this.datasource.searchType = value;
        } else {
          this.task.searchType = value;
        }
      }
    },
    showAuthTree() {
      if (this.activeAuthType === 'datasource') {
        return this.datasource.selectedNode || this.batchMode;
      } else {
        return this.task.selectedNode || this.batchMode;
      }
    },
    authStartTime: {
      get() {
        return this.authTime?.startTime;
      },
      set(value) {
        this.authTime.startTime = value;
      }
    },
    authEndTime: {
      get() {
        return this.authTime?.endTime;
      },
      set(value) {
        this.authTime.endTime = value;
      }
    },
    disableAuthTab() {
      return (auth) => {
        let disable = false;
        if (this.previewMode) {
          return true;
        }
        if (!this.includesCC && auth === 'DataJob') {
          disable = true;
        }
        return disable;
      };
    },
    processedTimeList() {
      if (this.timeList[this.curNode?.key]) {
        return this.timeList[this.curNode?.key];
      }
      return [];
    }
  },
  watch: {
    '$route.query.type': {
      handler(newVal) {
        this.initData();
      },
      deep: true,
      immediate: true
    },
    '$route.params.uid': {
      async handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.uid = this.isEdit || this.isView ? this.$route.params.uid : this.userInfo.uid;
          this.subAccount = this.isEdit || this.isView ? this.$route.query.name : '';
          await this.listLevelsForDM(null, true);
        }
      },
      deep: true
    }
  },
  methods: {
    handleReloadPage() {
      this.originLeftTree = [];
      this.initData();
    },
    async initData() {
      this.pageLoading = true;
      try {
        this.isEdit = this.$route.query.type === 'edit';
        this.isView = this.$route.query.type === 'view';
        this.uid = this.isEdit || this.isView ? this.$route.params.uid : this.userInfo.uid;
        this.subAccount = this.isEdit || this.isView ? this.$route.query.name : '';
        this.authTime = {
          startTime: null,
          endTime: null
        };
        await this.loadAuthTarget();
        this.activeAuthTab = 'DataSource';
        this.activeAuthType = 'datasource';
        this.lastRightTreeData = [];
        this.lastLeftTreeClickNode = '';
        this.rightTreeKeyword = '';
        this.leftTreeKeyword = '';
        this.isSingleSelect = true;
        this.curElementType = null;
        this.curRightTreeTab = null;
        this.originLeftTree = [];
        this.previewMode = false;
        await this.listLevelsForDM();

        // 初次默认展开第一个节点的第一层
        this.$nextTick(async () => {
          let firstRoot = null;
          if (this.originLeftTree && this.originLeftTree.length > 0) {
            this.originLeftTree.forEach(async (node, idx) => {
              await this.listLevelsForDM(node);
              if (this.originLeftTree[idx]?.children && this.originLeftTree[idx]?.children?.key) {
                firstRoot = this.originLeftTree[idx];
              }
            });
            if (firstRoot) {
              if (firstRoot.children && firstRoot.children.length > 0) {
                // 只在 expandedKeys 不包含时 push，避免强制赋值导致无法收起
                if (!this.expandedKeys.includes(firstRoot.key)) {
                  this.expandedKeys.push(firstRoot.key);
                }
              }
            }
          }
        });
      } finally {
        this.pageLoading = false;
      }
    },
    async loadAuthTarget() {
      this.authTarget = {
        uid: this.uid,
        username: this.subAccount,
        resourceManage: false,
        disable: false
      };
      if (!this.isEdit && !this.isView) {
        await this.loadGlobalResourceAuth();
        return;
      }
      const res = await this.$services.rdpUserManagerListSubAccounts({
        data: {
          roleId: 0,
          userNameOrSubAccountPrefix: ''
        }
      });
      if (res.success && Array.isArray(res.data)) {
        const target = res.data.find((item) => item.uid === this.uid);
        if (target) {
          this.authTarget = {
            ...target,
            resourceManage: false,
            username: target.username || this.subAccount
          };
        }
      }
      await this.loadGlobalResourceAuth();
    },
    async loadGlobalResourceAuth() {
      this.globalResourceAuthId = null;
      this.globalResourceOriginalEnabled = false;
      this.globalResourceOriginalStartTime = null;
      this.globalResourceOriginalEndTime = null;
      const res = await this.$services.rdpAuthListUserAuthOfRes({
        data: {
          authKind: 'DataSource',
          targetUid: this.uid,
          groups: [
            {
              resId: 0,
              resPaths: []
            }
          ]
        }
      });
      const globalAuth = Array.isArray(res.data) && res.data.length ? res.data[0] : null;
      this.authTarget.resourceManage = !!globalAuth;
      this.globalResourceOriginalEnabled = !!globalAuth;
      if (globalAuth) {
        this.globalResourceAuthId = globalAuth.id;
        this.globalResourceOriginalStartTime = this.formatAuthTime(globalAuth.startTime);
        this.globalResourceOriginalEndTime = this.formatAuthTime(globalAuth.endTime);
        if (globalAuth.startTime) {
          this.authTime.startTime = dayjs(globalAuth.startTime);
        }
        if (globalAuth.endTime) {
          this.authTime.endTime = dayjs(globalAuth.endTime);
        }
      }
    },
    async handleResourceManageChange() {
      this.originLeftTree = this.markGlobalResourceAuthState(this.originLeftTree);
      this.$refs.dataSourceTree.setData(this.getFilterOfTypeAndSearch(this.originLeftTree));
      if (this.curNode?.key && this.curNode?.objType !== 'ENV') {
        await this.handleGetAuthTreeForDm(this.curNode);
      }
      await this.handleGetPreviewData();
    },
    formatAuthTime(value) {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : null;
    },
    // 对比权限树差异并标记编辑状态
    handleAuthCheck(selectedNodes) {
      if (this.canCheckedChange) {
        const idx = this.parentAuthTree.findIndex((item) => item?.key === this.curNode?.key);

        if (idx !== -1) {
          this.parentAuthTree[idx].authTree = selectedNodes;
        }
        this.markLeftTreeEdited(this.curNode, this.curElementType, this.lastRightTreeData, selectedNodes);
      }
    },
    cancelAuth() {
      this.$router
        .push({
          path: `${this.uid}`,
          query: {
            name: this.subAccount,
            type: 'edit'
          }
        })
        .catch((err) => {
          if (err.name !== 'NavigationDuplicated') {
            throw err;
          }
        });
      this.initData();
    },
    handleCancelPreviewModeForDm() {
      this.previewMode = false;
      this.isEdit = true;
      this.selectedNodeKey = null;

      if (this.batchMode) {
        this.isSingleSelect = false;
      } else {
        this.isSingleSelect = true;
      }
      this.$refs.dataSourceTree.setData(this.originLeftTree);
    },
    formattedTime(item) {
      if (!item.startTime && !item.endTime) {
        return this.$t('yong-jiu');
      } else if (item.startTime && item.endTime) {
        return `${item.startTime.format('YYYY-MM-DD HH:mm:ss')} - ${item.endTime.format('YYYY-MM-DD HH:mm:ss')}`;
      } else if (item.startTime) {
        return `${this.$t('cong-0')} ${item.startTime.format('YYYY-MM-DD HH:mm:ss')} ${this.$t('kai-shi-zhi-yong-jiu')}`;
      } else {
        return `${this.$t('cong-shen-pi-tong-guo-dao')} ${item.endTime.format('YYYY-MM-DD HH:mm:ss')} ${this.$t('jie-shu')}`;
      }
    },
    async handlePreviewForDm() {
      // 检查是否有编辑权限节点
      function hasEditNode(tree) {
        return tree.some((node) => node.isEdit || (node.children && hasEditNode(node.children)));
      }
      const hasGlobalResourceEdit = this.hasGlobalResourceAuthChanges();
      if (!hasEditNode(this.originLeftTree) && !hasGlobalResourceEdit) {
        this.$Message.warning(this.$t('huan-mei-you-bian-ji-quan-xian'));
        return;
      }
      this.previewMode = true;
      this.isEdit = false;
      this.isSingleSelect = true;

      if (this.batchMode) {
        this.originLeftTree = this.$refs.dataSourceTree.getTreeData();
        const filterTree = this.filterTreeWithCheckedNodes(this.originLeftTree);
        this.$refs.dataSourceTree.setData(filterTree);
        return;
      }

      const filterTree = hasGlobalResourceEdit
        ? this.getFilterOfTypeAndSearch(this.originLeftTree)
        : this.filterTreeWithEditedNodes(this.originLeftTree);
      this.$refs.dataSourceTree.setData(filterTree);
      this.$refs.instanceTree.setData([]);
      this.$refs.schemaTree.setData([]);
      this.$refs.catalogTree.setData([]);
      this.$refs.tableTree.setData([]);
      this.$nextTick(() => {
        // 查找第一个isEdit为true的节点
        const findFirstEditedNode = (nodes) => {
          for (const node of nodes) {
            if (node.isEdit) {
              return node;
            }
            if (node.children && node.children.length > 0) {
              const editedChild = findFirstEditedNode(node.children);
              if (editedChild) {
                return editedChild;
              }
            }
          }
          return null;
        };
        const findFirstAuthNode = (nodes) => {
          for (const node of nodes) {
            if (node.objType && node.objType !== 'ENV') {
              return node;
            }
            if (node.children && node.children.length > 0) {
              const authNode = findFirstAuthNode(node.children);
              if (authNode) {
                return authNode;
              }
            }
          }
          return null;
        };

        const firstEditedNode = findFirstEditedNode(this.originLeftTree);
        if (firstEditedNode) {
          this.selectedNodeKey = firstEditedNode.key;
          this.$nextTick(() => {
            this.leftTreeNodeClick(firstEditedNode);
          });
        } else if (hasGlobalResourceEdit) {
          const firstAuthNode = findFirstAuthNode(this.originLeftTree);
          if (firstAuthNode) {
            this.selectedNodeKey = firstAuthNode.key;
            this.$nextTick(() => {
              this.leftTreeNodeClick(firstAuthNode);
            });
          }
        } else if (this.originLeftTree && this.originLeftTree.length > 0) {
          const firstNode = this.originLeftTree[0];
          if (firstNode) {
            this.selectedNodeKey = firstNode.key;
            this.$nextTick(() => {
              this.leftTreeNodeClick(firstNode);
            });
          }
        }
      });
    },
    async handleSubmit() {
      const submitData = {};
      submitData.authKind = this.activeAuthTab;
      submitData.targetUid = this.uid;
      const authData = this.getSubmitAuthData();
      const res = await this.$services.rdpAuthModifyUserAuth({
        data: { ...submitData, ...authData }
      });
      if (res?.data) {
        this.$message.success(this.$t('shu-ju-ku-shou-quan-cheng-gong'));
        this.previewMode = false;
        this.isEdit = true;
        this.cancelAuth();
      }
    },
    getSubmitAuthData() {
      const appends = [];
      const updates = [];
      const deletes = [];

      const filterTree = this.filterTreeWithEditedNodes(this.originLeftTree);
      filterTree.forEach((envItem) => {
        envItem.children.forEach((instanceItem) => {
          const getAll = function (authDataTree) {
            if (!authDataTree) return;
            const flattenAuthArr = [];
            let isUpdates = false;

            if (authDataTree.markedWithActionRightTree?.length) {
              authDataTree.markedWithActionRightTree.forEach((item) => {
                flattenAuthArr.push(...item.children);
              });
            }
            flattenAuthArr.forEach((item) => {
              if (item?.checked && !item.action) isUpdates = true;
            });
            flattenAuthArr.forEach((authItem) => {
              // 更新
              if (isUpdates && authItem.checked && authDataTree.isEdit) {
                appends.push({
                  startTime: authDataTree?.authTime?.startTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  endTime: authDataTree?.authTime?.endTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  resId: instanceItem?.objId,
                  authLabels: [authItem?.key],
                  resPaths: getResTypeToNames(authDataTree)
                });
              }

              // 新增
              if (!isUpdates && authItem.checked && authDataTree.isEdit) {
                appends.push({
                  startTime: authDataTree?.authTime?.startTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  endTime: authDataTree?.authTime?.endTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  resId: instanceItem?.objId,
                  authLabels: [authItem?.key],
                  resPaths: getResTypeToNames(authDataTree)
                });
              }

              // 删除
              if (!isUpdates && authItem.action === 'deletes' && authDataTree.isEdit) {
                deletes.push({
                  startTime: authDataTree?.authTime?.startTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  endTime: authDataTree?.authTime?.endTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  resId: instanceItem?.objId,
                  authLabels: [authItem?.key],
                  resPaths: getResTypeToNames(authDataTree)
                });
              }
            });

            // 遍历子节点并递归调用 getAll
            if (authDataTree.children && authDataTree.children.length) {
              authDataTree.children.forEach((child) => getAll(child));
            }
          };

          getAll(instanceItem);
        });
      });

      const globalResourceAuthChanges = this.getGlobalResourceAuthChanges();
      return {
        appends: this.mergeSubmitAuthData(appends).concat(globalResourceAuthChanges.appends),
        updates: this.mergeSubmitAuthData(updates).concat(globalResourceAuthChanges.updates),
        deletes: this.mergeSubmitAuthData(deletes).concat(globalResourceAuthChanges.deletes)
      };
    },
    handleGetPreviewData() {
      this.authedData = this.getSubmitAuthData();
      return this.authedData;
    },
    hasGlobalResourceAuthChanges() {
      const globalResourceAuthChanges = this.getGlobalResourceAuthChanges();
      return !!(globalResourceAuthChanges.appends.length || globalResourceAuthChanges.updates.length || globalResourceAuthChanges.deletes.length);
    },
    getGlobalResourceAuthChanges() {
      const changes = {
        appends: [],
        updates: [],
        deletes: []
      };
      if (this.activeAuthTab !== 'DataSource') {
        return changes;
      }
      const startTime = this.formatAuthTime(this.authStartTime);
      const endTime = this.formatAuthTime(this.authEndTime);
      const authData = {
        resId: 0,
        resPaths: [],
        authLabels: [],
        startTime,
        endTime
      };
      if (this.authTarget.resourceManage && !this.globalResourceOriginalEnabled) {
        changes.appends.push(authData);
        return changes;
      }
      if (!this.authTarget.resourceManage && this.globalResourceOriginalEnabled && this.globalResourceAuthId) {
        changes.deletes.push({
          authId: this.globalResourceAuthId,
          resId: 0,
          resPaths: []
        });
        return changes;
      }
      if (
        this.authTarget.resourceManage &&
        this.globalResourceOriginalEnabled &&
        this.globalResourceAuthId &&
        (startTime !== this.globalResourceOriginalStartTime || endTime !== this.globalResourceOriginalEndTime)
      ) {
        changes.updates.push({
          ...authData,
          authId: this.globalResourceAuthId
        });
      }
      return changes;
    },
    mergeSubmitAuthData(appends) {
      const map = new Map();
      appends.forEach((item) => {
        const key = `${item.resId}-${JSON.stringify(item.resPaths)}`;
        if (map?.has(key)) {
          map.get(key).authLabels.push(...item.authLabels);
        } else {
          map.set(key, { ...item, authLabels: [...item.authLabels] });
        }
      });

      return Array.from(map?.values());
    },
    renderNode(node) {
      const style = {
        marginLeft: '6px',
        width: '6px',
        height: '6px',
        borderRadius: '10px'
      };
      let iconType = '';
      const prefix = 'icon-v2-';
      const enableQuery = node?.objAttr?.enableQuery || false;

      if (node?.objAttr?.dsType) iconType = prefix + node.objAttr.dsType;
      else {
        switch (node?.objType) {
          case 'ENV':
            iconType = 'MachineENV';
            break;
          case 'TABLE':
            iconType = 'TABLE';
            break;
          case 'EXTERNAL_SCHEMA':
            iconType = 'SCHEMA';
            break;
          case 'SCHEMA':
            iconType = 'SCHEMA';
            break;
          case 'CATALOG':
            iconType = 'CATALOG';
            break;
          case 'EXTERNAL_CATALOG':
            iconType = 'EXTERNAL_CATALOG';
            break;
          default:
            break;
        }
      }

      if (node?.isEdit) {
        style.background = '#ee8435';
      }
      return (
        <div class='node-wrap' data-key={node?.key}>
          <div style='display: flex; align-items: center;'>
            {this.leftTreeLoading && this.lastLeftTreeClickNode?.key === node?.key ? (
              <i class='loading-circle'></i>
            ) : node?.objType === 'ENV' ? (
              <cc-svg-icon name='ENV' style='margin-right: 5px' />
            ) : (
              <CustomIcon type={iconType} rightMargin='5px' />
            )}
            <div>
              {node?.objDesc ? `${node?.objName}(${node?.objDesc})` : node?.objName}
              {this.isNodeAuthed(node) && <span class='authed-tip'></span>}
            </div>
          </div>
          {enableQuery && <i style='position: absolute; right: 10px' class='iconfont iconkechaxun'></i>}
        </div>
      );
    },
    startDragging() {
      this.isDragging = true;
      document.body.style.cursor = 'col-resize';
    },
    stopDragging() {
      this.isDragging = false;
      document.body.style.cursor = 'default';
    },
    handleMouseMove(e) {
      if (this.isDragging) {
        const min = 600;
        const max = 2000;
        if (e.clientX >= min && e.clientX <= max) {
          this.leftWidth = e.clientX - 260;
        }
      }
    },
    renderAuthNode(node) {
      const style = {
        color: '#000'
      };
      if (this.previewMode && node?.isLeaf) {
        if (node?.action === 'deletes') {
          style.color = 'red';
        } else if (node?.action === 'appends') {
          style.color = 'green';
        }
      }
      return (
        <div class='node' style={style}>
          {node?.i18nName || '-'}
        </div>
      );
    },
    getFilterOfTypeAndSearch(tree) {
      tree = this.filterTreeOfType(tree);
      tree = this.handleDataSourceSearch(tree);
      return tree;
    },
    // 左树点击
    async leftTreeNodeClick(node, isExpand = false) {
      const idx = this.expandedKeys.indexOf(node?.key);
      const shouldExpand = isExpand ? node?.expand : !node?.expand;
      this.curNode = node;
      this.curElementType = node?.objType;
      this.canCheckedChange = false;
      this.curRangeKey = '';

      // 兼容箭头收起行为
      if (!shouldExpand) {
        // 收起行为，不往下执行逻辑
        if (idx !== -1) this.expandedKeys.splice(idx, 1);
        this.lastLeftTreeClickNode = node;
        this.curElementType = node?.objType;
        this.curRightTreeTab = node?.objType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : node?.objType === 'EXTERNAL_CATALOG' ? 'CATALOG' : node?.objType;

        this.authTime = node?.authTime || { startTime: null, endTime: null };

        this.handleGetAuthTreeForDm(node);
        return;
      }
      this.expandedKeys = [...new Set(this.expandedKeys)];

      if (this.batchMode) {
        return;
      }

      if (this.previewMode) {
        this.renderPreviewLeftTree(node);
        if (idx === -1) this.expandedKeys.push(node?.key);
        return;
      }

      // 实例名称是否合法
      const noLegal = node?.objName?.includes('/');
      if (noLegal) {
        this.$Message.warning(this.$t('fa-mi-ming-cheng-bu-zhi-chi'));
        return;
      }

      // 实例未开启数据管理
      if (node?.objType === 'Instance' && !node?.objAttr?.enableQuery) {
        let final = [];
        final = this.removeChildrenByKey(this.originLeftTree, node?.key);
        this.originLeftTree = final;

        final = this.getFilterOfTypeAndSearch(this.originLeftTre);
        this.$Message.warning(this.$t('shu-ju-cha-xun-wei-kai-qi'));
        this.$refs.dataSourceTree.setData(final);
        return;
      }

      this.listLevelsForDM(node);
    },
    // 左树渲染
    async listLevelsForDM(node = null) {
      try {
        this.leftTreeLoading = true;

        // 0、记录当前的左树
        if (node) {
          this.lastLeftTreeClickNode = node;
          this.curElementType = node?.objType;
          this.curRightTreeTab = node?.objType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : node?.objType === 'EXTERNAL_CATALOG' ? 'CATALOG' : node?.objType;
        }

        // 1、复用之前的数据
        if (node?.children[0]?.levels?.length) {
          let final = [];

          // 1.1 带上过滤条件和search条件
          final = this.getFilterOfTypeAndSearch(this.originLeftTree);

          this.$refs.dataSourceTree.setData(final);

          this.handleGetAuthTreeForDm(node);
          this.leftTreeLoading = false;
          const idx = this.expandedKeys.indexOf(node?.key);
          if (idx === -1) this.expandedKeys.push(node?.key);
          return;
        }

        if (node?.objType === 'TABLE') {
          const final = this.getFilterOfTypeAndSearch(this.originLeftTree);
          this.$refs.dataSourceTree.setData(final);
          this.handleGetAuthTreeForDm(node);
          this.leftTreeLoading = false;
          const idx = this.expandedKeys.indexOf(node?.key);
          if (idx === -1) this.expandedKeys.push(node?.key);
          return;
        }

        // 1、按层查询树节点
        let res = {
          data: []
        };

        // 查询用户拥有资源
        if (this.isView) {
          // 叶子节点
          if (this.isLeafNode(node)) {
            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListUserElementOfLeaf({
                data: {
                  levels: this.getResPathByIdAndName(node),
                  leafType: this.getResTypeToIds(node), // Instance|Catalog|Schema|Table
                  uid: this.uid
                },
                ...config
              })
            );
          } else {
            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListUserElementsOfLevel({
                data: {
                  authKind: this.activeAuthTab,
                  resPaths: this.getResPathByIdAndName(node),
                  uid: this.uid
                },
                ...config
              })
            );
          }
        } else {
          if (this.isLeafNode(node)) {
            const resPaths = this.getResPathByIdAndName(node);

            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListElementOfLeaf({
                data: {
                  levels: resPaths,
                  leafType: this.getResTypeToIds(node) // Instance|Catalog|Schema|Table
                },
                ...config
              })
            );
            if (res.success) {
              if (resPaths.length > 2) {
                // 4、Table 已授权
                this.userAuthResList.forEach((item, idx) => {
                  const paths = item.level.split('/').slice(0, -1);
                  const isCatlog = resPaths?.length === 4;
                  const isSameIns = item?.resInstId === node._parent?.objName || item?.resInstId === node._parent?._parent?.objName;
                  const isSameCatalog = node?._parent?.objName === paths[1];

                  res.data.forEach((ds) => {
                    if (isSameIns && isCatlog && ds.objName === paths[3] && isSameCatalog) {
                      ds.isAuthed = true;
                    }
                    if (isSameIns && !isCatlog && ds.objName === paths[2]) {
                      ds.isAuthed = true;
                    }
                  });
                });
              }
            }
          } else {
            const resPaths = this.getResPathByIdAndName(node);

            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListElementsOfLevel({
                data: {
                  authKind: this.activeAuthTab,
                  resPaths
                },
                ...config
              })
            );
            if (this.isEdit && res?.success) {
              if (resPaths.length === 0) {
                // res1：用户有权限的资源，仅请求一次
                let res1 = {};
                res1 = await this.$services.rdpAuthListUserAuthRes({
                  data: {
                    authKind: this.activeAuthTab,
                    targetUid: this.uid
                  }
                });
                if (res1.data?.length && res1?.success) {
                  this.userAuthResList = res1.data;
                }
              } else if (resPaths.length === 1) {
                // 1、Instance 已授权
                this.userAuthResList.forEach((item, idx) => {
                  const paths = item.level.split('/').slice(0, -1);
                  res.data.forEach((ds) => {
                    if (ds.objName === item.resInstId) {
                      ds.isAuthed = true;
                    }
                  });
                });
              } else if (resPaths.length === 2) {
                // 2、Schema 已授权
                this.userAuthResList.forEach((item, idx) => {
                  const paths = item.level.split('/').slice(0, -1);
                  res.data.forEach((ds) => {
                    if (item?.resInstId === node.objName && ds.objName === paths[1]) {
                      ds.isAuthed = true;
                    }
                  });
                });
              }
              if (resPaths.length === 3 && (node?.objType === 'CATALOG' || node?.objType === 'EXTERNAL_CATALOG')) {
                // 3、CATALOG 已授权
                this.userAuthResList.forEach((item) => {
                  const paths = item.level.split('/').slice(0, -1);
                  const isSameCatalog = node?.objName === paths[1];

                  res.data.forEach((ds) => {
                    if (item?.resInstId === node._parent?.objName && ds.objName === paths[2] && isSameCatalog) {
                      ds.isAuthed = true;
                    }
                  });
                });
              }
            }
          }
        }
        if (!res?.data?.length) {
          if (res.msg) {
            if (res.data === null) this.$message.warn(res.msg);
          }

          if (this.originLeftTree?.length) {
            this.removeChildrenByKey(this.originLeftTree, node?.key);
            this.originLeftTree = this.getFilterOfTypeAndSearch(this.originLeftTree);
            this.$refs.dataSourceTree.setData(this.originLeftTree);
            this.leftTreeLoading = false;
            this.curElementType = node?.objType;
            this.curRightTreeTab = node?.objType;
            this.handleGetAuthTreeForDm(node);
          }
          return;
        }

        const idx = this.expandedKeys.indexOf(node?.key);
        if (idx === -1) this.expandedKeys.push(node?.key);

        res.data.map((item) => {
          item.children = [{}];
          item.loaded = false;
          item.levels = [item?.objId];
          item.key = this.genUniqueId();
          item.parent = node;
          // 2、递归获取所有父级 objId, 存入levels中
          const parentObjIds = this.getParentObjIds(item);
          item.levels = [...parentObjIds, item.objId];
          return item;
        });
        res.data = this.markGlobalResourceAuthState(res.data);

        // 3、渲染左侧资源树
        if (!this.originLeftTree?.length) {
          this.originLeftTree = res.data;
          const final = this.getFilterOfTypeAndSearch(res.data);
          await this.$refs.dataSourceTree.setData(final);
        } else {
          let final = [];
          // 3.1 将新子树数据插入到原tree对应节点下
          final = this.replaceChildren(this.originLeftTree, res.data, node?.key);

          // 3.2 去掉leaf节点children属性
          final = this.removeChildrenForTableNodes(final);

          // 3.3 标记根节点的auth情况
          final = this.getRootTreeAuth(final);
          final = this.markGlobalResourceAuthState(final);

          this.originLeftTree = final;

          // 3.4 带上过滤条件和search条件
          final = this.getFilterOfTypeAndSearch(final);

          // 3.5 渲染左树
          await this.$refs.dataSourceTree.setData(final);
        }

        // 4、渲染右侧权限树
        this.handleGetAuthTreeForDm(node);
        this.leftTreeLoading = false;
      } catch (err) {
        this.leftTreeLoading = false;

        const idx = this.expandedKeys.indexOf(node?.key);
        if (idx !== -1) this.expandedKeys.splice(idx, 1);
        this.$Message.error(this.$t('chu-xian-yi-chang-qing-shua-xin-ye-mian-hou-zhong-shi'));
      }
    },
    removeChildrenForTableNodes(tree, depth = 0, maxDepth = 5) {
      if (depth >= maxDepth) {
        return tree;
      }

      return tree.map((node) => {
        if (node.objType === 'TABLE') {
          const { children, ...rest } = node;
          return rest;
        }

        if (node.children && node.children.length > 0) {
          node.children = this.removeChildrenForTableNodes(node.children, depth + 1, maxDepth);
        }

        return node;
      });
    },
    renderPreviewLeftTree(node) {
      const filterTree = this.hasGlobalResourceAuthChanges()
        ? this.getFilterOfTypeAndSearch(this.originLeftTree)
        : this.filterTreeWithEditedNodes(this.originLeftTree);
      this.$refs.dataSourceTree.setData(filterTree);

      this.handleGetAuthTreeForDm(node);
    },
    // 返回id + name 组成的resPaths参数
    getResPathByIdAndName(node) {
      if (!node) return [];
      let resPath = node?.levels;

      if (node?.levels?.length > START_RECORD_NAMES_CONUT) {
        resPath = node.levels.slice(0, 2).concat(node.objName);

        const parentNames = [];
        let curNode = node.parent;
        while (curNode && curNode.levels?.length > START_RECORD_NAMES_CONUT) {
          parentNames.push(curNode.objName);
          curNode = curNode.parent;
        }
        resPath = resPath.concat(parentNames);
      }
      // 对CATALOG做特殊处理
      if (resPath.length === 4) {
        [resPath[2], resPath[3]] = [resPath[3], resPath[2]];
      }
      return resPath;
    },

    getRootTreeAuth(tree) {
      tree.forEach((env) => {
        if (env?.children?.[0]?.levels?.length > 1) {
          env.children.forEach((instance) => {
            this.userAuthResList.forEach((auth) => {
              if (auth.resInstId === instance.objName) {
                env.isAuthed = true;
              }
            });
          });
        }
      });
      return tree;
    },
    isGlobalResourceAuthActive() {
      return this.activeAuthTab === 'DataSource' && !!this.authTarget.resourceManage;
    },
    isNodeAuthed(node) {
      return !!(node?.isAuthed || node?.globalAuthed);
    },
    markGlobalResourceAuthState(tree = []) {
      const active = this.isGlobalResourceAuthActive();
      const traverse = (nodes) =>
        nodes?.map?.((node) => {
          const next = { ...node };
          if (next.objName || next.objId || next.objType) {
            next.globalAuthed = active;
          }
          if (node.children && node.children.length > 0 && node.children[0]?.objType) {
            next.children = traverse(node.children);
          } else if (node.children) {
            next.children = node.children;
          }
          return next;
        }) || [];
      return traverse(tree);
    },

    getResTypeToIds(node = {}) {
      const dsType = node?.objAttr?.dsType || '';
      if (!node) {
        return 'Env';
      }
      if (dsType) {
        const typeLevels = this.dmGlobalSetting.dsSettingDef?.[dsType]?.categories?.levels || [];
        if (typeLevels.length) {
          const idx = typeLevels.indexOf(ELEMENT_TYPE_MAP[node?.objType] || '');
          if (idx > START_RECORD_NAMES_CONUT) return 'Table';
          return ELEMENT_REVERSE_TYPE_MAP[typeLevels[idx + 1]]; // +1 指向子节点的elemenType;
        }
      }
      if (!dsType && node?.objType !== 'Env') {
        return 'Table';
      }
      return '';
    },
    genUniqueId() {
      return `${Date.now()}-${Math.floor(Math.random() * 100000000)}`;
    },
    filterTreeWithEditedNodes(tree) {
      return tree
        .map((node) => {
          const filteredChildren = this.filterTreeWithEditedNodes(node.children || []);

          if (node.isEdit || filteredChildren.length > 0) {
            return { ...node, children: filteredChildren };
          }

          return null;
        })
        .filter(Boolean);
    },
    filterTreeWithCheckedNodes(tree) {
      return tree
        .map((node) => {
          const filteredChildren = this.filterTreeWithCheckedNodes(node.children || []);
          if (node.checked || filteredChildren.length > 0) {
            return { ...node, children: filteredChildren };
          }

          return null;
        })
        .filter(Boolean);
    },

    getParentObjIds(currentNode) {
      const parentObjIds = [];
      let current = currentNode;
      while (current && current.parent) {
        parentObjIds.push(current.parent.objId);
        current = current.parent;
      }
      return parentObjIds.reverse();
    },
    // 手动拼接新tree
    replaceChildren(originData, newData, nodeKey, maxDepth = 5) {
      if (!newData.length) return originData;
      if (originData === newData) return originData;

      const stack = originData.map((node) => ({ node, depth: 1 }));
      while (stack.length) {
        const { node, depth } = stack.pop();
        if (node?.key === nodeKey) {
          node.children = newData;
          return originData;
        }
        if (node.children && node.children.length > 0 && depth < maxDepth) {
          stack.push(...node.children.map((child) => ({ node: child, depth: depth + 1 })));
        }
      }

      return originData;
    },

    handleAuthFromParent(node, auth) {
      const filterAuth = JSON.parse(JSON.stringify(auth));
      const parentKey = node?.parent?.key;

      const parentAuthInfo = this.parentAuthTree.find((item) => item.key === parentKey);
      if (!parentAuthInfo) {
        return filterAuth;
      }

      const parentAuthTree = flattenTree(parentAuthInfo.authTree) || [];

      filterAuth.forEach((item) => {
        item.children.forEach((child) => {
          const parentAuth = parentAuthTree.find((parent) => parent.key === child.key);
          if (parentAuth && parentAuth.checked) {
            child.checked = true;
            child.disabled = true;
            child.inherited = true;
          }
        });
      });

      return filterAuth;
    },
    handleAuthFromGlobal(auth) {
      const filterAuth = JSON.parse(JSON.stringify(auth || []));
      if (!this.isGlobalResourceAuthActive()) {
        return filterAuth;
      }

      const traverse = (nodes = []) => {
        nodes.forEach((item) => {
          if (item.children?.length) {
            traverse(item.children);
          } else {
            item.checked = true;
            item.disabled = true;
            item.inherited = true;
            item.globalInherited = true;
          }
        });
      };
      traverse(filterAuth);
      return filterAuth;
    },
    handleAuthFromSelf(auth, hasAuth, node) {
      let selfAuth = [];
      hasAuth.forEach((item) => {
        if (item.level?.includes?.(node?.objName)) {
          selfAuth = item?.dsAuthKinds;
        }
      });
      auth.forEach((item) => {
        item.children.forEach((child) => {
          if (selfAuth?.includes?.(child.key)) {
            child.checked = true;
          }
        });
      });
      return auth;
    },

    async handleGetAuthTreeForDm(node = {}) {
      try {
        const elementType = node?.objType || '';
        if (elementType === 'ENV') {
          return;
        }
        let allAuth = { data: [] };
        let hasAutn = { data: [] };
        let filterAuth;
        if (elementType) {
          this.curElementType = elementType;
          this.curRightTreeTab = elementType;

          // 渲染时间
          const lastestNode = findNodeByKey(this.originLeftTree, node?.key);
          this.authTime = lastestNode?.authTime || {
            startTime: null,
            endTime: null
          };

          if (node?.markedWithActionRightTree && node.markedWithActionRightTree?.length) {
            // 复用上次数据
            filterAuth = node?.markedWithActionRightTree;
            this.lastRightTreeData = filterAuth;
          } else {
            // 获取全部权限树和用户拥有权限树，比对映射
            const normalizedElementType = elementType === 'EXTERNAL_CATALOG' ? 'CATALOG' : elementType;
            const normalizedElementType2 = normalizedElementType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : normalizedElementType;
            allAuth = await this.$services.rdpAuthFetchAuthTreeDef({
              data: {
                kind: this.activeAuthTab,
                dsType: node?.objAttr?.dsType || '',
                elementType: ELEMENT_REVERSE_TYPE_MAP[normalizedElementType2] || normalizedElementType2
              }
            });
            const flattenAuthTree = flattenTree(allAuth.data);
            flattenAuthTree.forEach((item) => {
              if (!this.authMap[item.key]) {
                this.authMap[item.key] = item.i18nName;
              }
            });
            if (this.findSchemaNodeId(node)) {
              hasAutn = await this.$services.rdpAuthListUserAuthOfRes({
                data: {
                  authKind: this.activeAuthTab,
                  targetUid: this.uid,
                  groups: [
                    {
                      resId: this.findSchemaNodeId(node),
                      resPaths: getResTypeToNames(node)
                    }
                  ]
                }
              });
            }

            const hasAuthList = [];
            const rawAuthData = Array.isArray(hasAutn.data) ? hasAutn.data : [];
            const authData = this.isGlobalResourceAuthActive() ? rawAuthData : rawAuthData.filter((authWrap) => authWrap.level !== '/');
            authData.forEach((authWrap) => {
              if (authWrap.startTime) this.authTime.startTime = dayjs(authWrap.startTime);
              if (authWrap.endTime) this.authTime.endTime = dayjs(authWrap.endTime);
              if (authWrap?.dsAuthKinds.length) hasAuthList.push(...authWrap.dsAuthKinds);

              if (!this.timeList[node.key]) {
                this.timeList[node.key] = [];
              }

              const exists = this.timeList[node.key].some((item) => item.level === authWrap.level);
              const allExistInFlatten = (authWrap?.dsAuthKinds || []).some((kind) => flattenTree(allAuth.data).find((item) => item.key === kind));

              if (!exists && allExistInFlatten) {
                this.timeList[node.key].push({
                  auths: authWrap?.dsAuthKinds,
                  startTime: authWrap.startTime ? dayjs(authWrap.startTime) : null,
                  endTime: authWrap.endTime ? dayjs(authWrap.endTime) : null,
                  level: authWrap.level
                });
              }
            });

            filterAuth = this.markRightTreeChecked(allAuth.data, [...new Set(hasAuthList)]);

            // 3.1 记录上次用户的完整权限树，用于比对修改情况
            this.lastRightTreeData = deepClone(filterAuth);

            // 3.2 记录父节点权限
            this.parentAuthTree.push({
              key: node?.key,
              authTree: deepClone(filterAuth)
            });

            // 3.3 先处理继承父节点权限
            filterAuth = this.handleAuthFromParent(node, filterAuth);

            // 3.4 再处理来自自身的权限
            filterAuth = this.handleAuthFromSelf(filterAuth, authData, node);
          }
          // 全部资源授权等同于每个层级都拥有权限
          filterAuth = this.handleAuthFromGlobal(filterAuth);
          this.$nextTick(() => {
            switch (elementType) {
              case 'Instance':
              case 'INSTANCE':
                this.$refs.instanceTree.setData(filterAuth);
                break;
              case 'Schema':
              case 'SCHEMA':
              case 'EXTERNAL_SCHEMA':
                this.$refs.schemaTree.setData(filterAuth);
                break;
              case 'CATALOG':
              case 'Catalog':
              case 'EXTERNAL_CATALOG':
                this.$refs.catalogTree.setData(filterAuth);
                break;
              case 'Table':
              case 'TABLE':
                this.$refs.tableTree.setData(filterAuth);
                break;
              default:
                break;
            }
            this.canCheckedChange = true;
          });
        }
        this.$refs.dataSourceTree.scrollTo(node?.key, 'center');
      } catch (err) {
        this.$Message.error(this.$t('chu-xian-yi-chang-qing-shua-xin-ye-mian-hou-zhong-shi'));
      }
    },
    findSchemaNodeId(node) {
      while (node) {
        if (node.objType === 'Instance') {
          return node.objId;
        }
        node = node?.parent;
      }
      return null;
    },
    markRightTreeChecked(tree, userPermissions) {
      function traverse(nodes) {
        return nodes?.map?.((node) => {
          const checked = userPermissions?.includes?.(node?.key);
          const newNode = { ...node, checked };
          if (node.children && node.children.length > 0) {
            newNode.children = traverse(node.children);
          }
          return newNode;
        });
      }
      return traverse(tree);
    },
    markLeftTreeEdited(node, type = this.curElementType, oldTree, newTree) {
      let markedWithActionRightTree = [];

      [markedWithActionRightTree] = this.markRightTreeActions(oldTree, newTree);

      const updateNodeInTree = function (tree, targetKey) {
        return tree?.map?.((item) => {
          if (item?.key === targetKey) {
            let isEdit = false;
            // 权限变化判断
            if (markedWithActionRightTree) {
              markedWithActionRightTree.forEach((authWrap) => {
                authWrap.children.forEach((auth) => {
                  if (auth.action) isEdit = true;
                });
              });
            }
            // 时间变化判断
            const oldTime = item.authTime || {};
            const newTime = this.authTime || {};
            // 只要 startTime 或 endTime 有变化就算编辑
            const oldStart = oldTime.startTime ? (oldTime.startTime.valueOf ? oldTime.startTime.valueOf() : oldTime.startTime) : '';
            const oldEnd = oldTime.endTime ? (oldTime.endTime.valueOf ? oldTime.endTime.valueOf() : oldTime.endTime) : '';
            const newStart = newTime.startTime ? (newTime.startTime.valueOf ? newTime.startTime.valueOf() : newTime.startTime) : '';
            const newEnd = newTime.endTime ? (newTime.endTime.valueOf ? newTime.endTime.valueOf() : newTime.endTime) : '';
            if (oldStart !== newStart || oldEnd !== newEnd) {
              isEdit = true;
            }
            item.markedWithActionRightTree = markedWithActionRightTree;
            item.isEdit = isEdit;
            item.authTime = this.authTime;
          }
          if (item.children && item.children.length > 0) {
            item.children = updateNodeInTree(item.children, targetKey);
          }
          return item;
        });
      }.bind(this);
      const res = updateNodeInTree(this.originLeftTree, node?.key);
      this.$refs.dataSourceTree.setData(res);
      this.originLeftTree = res;
    },

    markRightTreeActions(originalTree, modifiedTree) {
      let isEdit = false;

      const modifiedMap = new Map();
      function flattenModifiedTree(nodes) {
        nodes.forEach((node) => {
          if (node?.key) {
            modifiedMap.set(node.key, true);
          }
          if (node.children) {
            flattenModifiedTree(node.children);
          }
        });
      }
      flattenModifiedTree(modifiedTree);

      function traverse(nodes) {
        return nodes?.map((originalNode) => {
          const isInModified = modifiedMap.has(originalNode.key);
          const originalChecked = !!originalNode.checked;

          let action = originalNode.action;
          let checked = originalChecked;

          if (originalChecked && !isInModified) {
            isEdit = true;
            action = 'deletes';
            checked = false;
          } else if (!originalChecked && isInModified) {
            isEdit = true;
            action = 'appends';
            checked = true;
          }
          const newNode = {
            ...originalNode,
            action,
            checked,
            key: originalNode.key
          };
          if (originalNode.children) {
            newNode.children = traverse(originalNode.children);
          }
          return newNode;
        });
      }
      return [traverse(originalTree), isEdit];
    },

    async handleSwitchAuth(value, type) {
      if (this.activeAuthTab === value) {
        return;
      }
      if (value === 'DataJob') {
        this.$router.push({
          path: `/system/account/authdm/${this.uid}`,
          query: {
            name: this.subAccount,
            type: this.isEdit ? 'edit' : 'view'
          }
        });
        return;
      }

      this.datasourceTreeSearchType = 'all';
      await this.handleGetPreviewData();
      if (
        this.authedData.appends &&
        this.authedData.appends.length === 0 &&
        this.authedData.deletes.length === 0 &&
        this.authedData.updates.length === 0
      ) {
        this.activeAuthTab = value;
        this.activeAuthType = type;
        this.datasource.selectedNode = null;
        this.task.selectedNode = null;
        this.auth = {
          checkedKeys: [],
          startTime: null,
          endTime: null,
          originalTreeData: [],
          batchTreeData: [],
          diffuse: false,
          treeData: [],
          searchKey: '',
          loading: false
        };
        if (this.activeAuthTab === 'DataJob' && this.getCcProductClusterList.length > 0) {
          this.selectedCcCluster = this.getCcProductClusterList[0].clusterCode;
        }
        await this.listLevelsForDM();

        this.authedData = [];
        this.datasource.searchKey = '';
        this.task.searchKey = '';
      } else {
        this.$Modal.confirm({
          title: this.$t('zi-yuan-shou-quan-ti-shi'),
          content: this.$t(
            'dang-qian-lei-xing-de-zi-yuan-you-wei-ti-jiao-de-shou-quan-qing-ti-jiao-hou-zai-qie-huan-zi-yuan-lei-xing-ru-xuan-ze-hu-lve-bing-ji-xu-ben-ci-bian-geng-jiang-qing-kong'
          ),
          okText: this.$t('guan-bi'),
          cancelText: this.$t('hu-lve-bing-ji-xu'),
          onCancel: () => {
            this.datasource.authedTreeData = [];
            this.task.authedTreeData = [];

            this.authedData = [];
            this.datasource.searchKey = '';
            this.task.searchKey = '';
            this.activeAuthTab = value;
            this.activeAuthType = type;
            this.datasource.selectedNode = null;
            this.task.selectedNode = null;
            this.auth = {
              checkedKeys: [],
              startTime: null,
              endTime: null,
              originalTreeData: [],
              batchTreeData: [],
              diffuse: false,
              treeData: [],
              searchKey: '',
              loading: false
            };
            this.listLevelsForDM();
          }
        });
      }
    },
    handleChangeCcCluster(data) {
      this.handleGetPreviewData();
      if (
        this.authedData.appends &&
        this.authedData.appends.length === 0 &&
        this.authedData.deletes.length === 0 &&
        this.authedData.updates.length === 0
      ) {
        this.selectedCcCluster = data;
        this.datasource.selectedNode = null;
        this.task.selectedNode = null;
        this.auth = {
          checkedKeys: [],
          startTime: null,
          endTime: null,
          originalTreeData: [],
          batchTreeData: [],
          diffuse: false,
          treeData: [],
          searchKey: '',
          loading: false
        };
        this.listLevelsForDM();
      } else {
        this.$Modal.confirm({
          title: this.$t('zi-yuan-shou-quan-ti-shi'),
          content: this.$t(
            'dang-qian-chan-pin-ji-qun-xia-you-wei-ti-jiao-de-shou-quan-qing-ti-jiao-hou-zai-qie-huan-chan-pin-ji-qun-ru-xuan-ze-hu-lve-bing-ji-xu-ben-ci-bian-geng-jiang-qing-kong'
          ),
          onText: this.$t('guan-bi'),
          cancelText: this.$t('hu-lve-bing-ji-xu'),
          onCancel: () => {
            this.datasource.authedTreeData = [];
            this.task.authedTreeData = [];

            this.authedData = [];
            this.selectedCcCluster = data;
            this.datasource.searchKey = '';
            this.task.searchKey = '';
            this.datasource.selectedNode = null;
            this.task.selectedNode = null;
            this.auth = {
              checkedKeys: [],
              startTime: null,
              endTime: null,
              originalTreeData: [],
              batchTreeData: [],
              diffuse: false,
              treeData: [],
              searchKey: '',
              loading: false
            };
            this.listLevelsForDM();
          }
        });
      }
    },
    onSearchTypChange() {
      let res = [];
      res = this.filterTreeOfType(deepClone(this.originLeftTree));
      res = this.handleDataSourceSearch(res);
      this.$refs.dataSourceTree.setData(res);
    },
    filterTreeOfType(origin = this.originLeftTree) {
      const type = this.datasourceTreeSearchType;
      let newOrigin = origin;

      if (type === 'authed') {
        newOrigin = origin.filter((node) => node?.children);
      }

      const filtered = newOrigin
        ?.map((node) => {
          const children = node.children || [];

          if (children[0]?.objType === 'Instance') {
            let newChildren = children;
            if (type === 'authed') {
              newChildren = children.filter((child) => this.isNodeAuthed(child));
            } else if (type === 'unAuth') {
              newChildren = children.filter((child) => !this.isNodeAuthed(child));
            }
            if (type === 'authed' && node.objType === 'ENV' && newChildren.length === 0) {
              return null;
            }
            return {
              ...node,
              children: newChildren
            };
          }

          return node;
        })
        // 过滤掉为null的节点（即所有子节点都没有isAuthed的ENV节点）
        .filter(Boolean)
        // 最外层环境若没有任何子数据则不展示
        .filter((node) => !(node?.objType === 'ENV' && (!node.children || node.children.length === 0)));

      return filtered;
    },
    onSearchKeyChange() {
      let res = [];
      // 使用深拷贝，避免污染原始数据
      res = this.filterTreeOfType(deepClone(this.originLeftTree));
      res = this.handleDataSourceSearch(res);
      this.$refs.dataSourceTree.setData(res);
    },
    handleDataSourceSearch(tree = this.originLeftTree) {
      return this.filterTree(tree, this.leftTreeKeyword, true);
    },
    handleAuthSearch() {
      switch (this.curElementType) {
        case 'Instance':
          this.$refs.instanceTree.filter(this.rightTreeKeyword);
          break;
        case 'SCHEMA':
          this.$refs.schemaTree.filter(this.rightTreeKeyword);
          break;
        case 'EXTERNAL_SCHEMA':
          this.$refs.schemaTree.filter(this.rightTreeKeyword);
          break;
        case 'CATALOG':
          this.$refs.schemaTree.filter(this.rightTreeKeyword);
          break;
        case 'EXTERNAL_CATALOG':
          this.$refs.schemaTree.filter(this.rightTreeKeyword);
          break;
        case 'TABLE':
          this.$refs.tableTree.filter(this.rightTreeKeyword);
          break;
        default:
          break;
      }
    },
    handleUserSearch() {
      this.$refs.userTree.filter(this.subAccount.searchKey);
    },
    handleAuthTimeChange() {
      const refMap = {
        Instance: 'instanceTree',
        INSTANCE: 'instanceTree',
        Schema: 'schemaTree',
        SCHEMA: 'schemaTree',
        EXTERNAL_SCHEMA: 'schemaTree',
        Catalog: 'catalogTree',
        CATALOG: 'catalogTree',
        EXTERNAL_CATALOG: 'catalogTree',
        Table: 'tableTree',
        TABLE: 'tableTree'
      };
      const normalizedTab =
        this.curRightTreeTab === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : this.curRightTreeTab === 'EXTERNAL_CATALOG' ? 'CATALOG' : this.curRightTreeTab;
      const curTreeRef = refMap[normalizedTab];
      const rightTreeData = this.$refs[curTreeRef]?.getCheckedNodes?.() || [];

      // 仅在选中了节点，切更改了时间才更新
      if (this.curNode?.objId) {
        this.markLeftTreeEdited(this.curNode, this.curElementType, this.lastRightTreeData, rightTreeData);
      }
    },
    handleStartTimeChange() {
      this.selectedRange = {};
      this.handleAuthTimeChange();
    },
    handleEndTimeChange() {
      this.selectedRange = {};
      this.handleAuthTimeChange();
    },
    handleClearAuthTime() {
      this.curRangeKey = '';
      this.selectedRange = {};
      this.authTime.startTime = null;
      this.authTime.endTime = null;
      this.handleAuthTimeChange();
    },
    handleRangeChange(rangeKey) {
      let selectedObj;
      if (Number(rangeKey) < 4) {
        selectedObj = this.ranges1.find((item) => item.key === rangeKey);
      } else {
        selectedObj = this.ranges2.find((item) => item.key === rangeKey);
      }
      this.curRangeKey = rangeKey;
      this.selectedRange = selectedObj;
      this.authTime.startTime = selectedObj.startTime;
      this.authTime.endTime = selectedObj.endTime;
      // 获取当前tab对应的权限树ref
      this.handleAuthTimeChange();
    },
    disabledStartDate(startValue) {
      const endValue = this.endTime;
      if (!startValue || !endValue) {
        return false;
      }
      return startValue.valueOf() > endValue.valueOf();
    },
    disabledEndDate(endValue) {
      const startValue = this.startTime;
      if (!endValue || !startValue) {
        return false;
      }
      return startValue.valueOf() >= endValue.valueOf();
    },
    async handleSwitchBatchModeForDm() {
      try {
        this.batchMode = !this.batchMode;
        if (this.batchMode) {
          this.curElementType = 'AllType';
          this.isSingleSelect = false;
          this.$refs.dataSourceTree.clearChecked();
        } else {
          this.curElementType = 'Instance';
          this.isSingleSelect = true;
        }

        Object.keys(this.originRightTree).forEach(async (key) => {
          const res = await this.$services.rdpAuthFetchAuthTreeDef({
            data: {
              kind: this.activeAuthTab,
              elementType: key
            }
          });
          this.originRightTree[key] = res.data;
          this.$refs[ELEMENT_TYPE_REF_MAP[key]].setData(res.data);
        });

        this.curRightTreeTab = 'Instance';
      } catch (err) {
        console.log(err);
      }
    },
    async handleSwitchBatchMode(needSwitch = true) {
      this.$refs.dataSourceTree.setSelected('test', true);
      this.$refs.dataSourceTree.clearChecked();
      this.auth = {
        checkedKeys: [],
        startTime: null,
        endTime: null,
        originalTreeData: [],
        batchTreeData: [],
        diffuse: false,
        treeData: [],
        searchKey: '',
        loading: false
      };

      if (needSwitch) {
        this.batchMode = !this.batchMode;
      }
      if (this.batchMode) {
        if (this.activeAuthType === 'task') {
          await this.handleGetAuthTree('DataJob');
          this.auth.batchTreeData = deepClone(this.authList.DataJob);
        }
        if (this.activeAuthType === 'datasource') {
          await this.handleGetAuthTree('Instance');
          this.auth.batchTreeData = deepClone(this.authList.Instance);
        }
        this.$refs.authTree.setData(this.auth.batchTreeData);
      } else {
        this.handleReloadPage();
      }
    },
    goSubAccountPage() {
      this.$router.push({ name: 'System_Sub_Account' });
    },
    handleGoAuth() {
      this.$router.push({
        path: `/system/account/authdm/${this.uid}`,
        query: {
          name: this.subAccount,
          type: 'edit'
        }
      });
    },
    handleDsExpand(node) {
      this.selectedNodeKey = node?.key;
      const isExpand = true;
      this.leftTreeNodeClick(node, isExpand);
    },
    filterTree(tree, keyword, isEnableQuery = false, depth = 5, level = 0) {
      if (!Array.isArray(tree) || depth <= 0) return [];

      return tree
        .map((node) => {
          if (level >= depth) return null;
          const match = node.objName && node.objName?.includes?.(keyword);
          const children = node.children ? this.filterTree(node.children, keyword, isEnableQuery, depth, level + 1) : [];

          if (match || children.length > 0) {
            const newNode = { ...node };

            if (children.length > 0) {
              newNode.children = children;
            } else if (!isEnableQuery) {
              newNode.children = [{}];
            }

            return newNode;
          }

          return null;
        })
        .filter((node) => node !== null);
    },
    isLeafNode(node) {
      return node?.levels?.length > START_RECORD_NAMES_CONUT && node?.objType !== 'CATALOG' && node?.objType !== 'EXTERNAL_CATALOG';
    },
    removeChildrenByKey(tree, targetKey) {
      function traverse(nodes) {
        if (!nodes || !Array.isArray(nodes)) return;
        for (const node of nodes) {
          if (node?.key === targetKey) {
            delete node.children;
          } else if (node.children) {
            traverse(node.children);
          }
        }
      }
      traverse(tree);
      return tree;
    },
    handleCloseExpand() {
      this.$refs.dataSourceTree.setExpandAll(false);
      this.expandedKeys = [];
    }
  }
};
</script>

<style scoped lang="less">
.auth-container-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 16px;
  padding-bottom: 0;

  .header {
    margin-bottom: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .auth-content {
    height: calc(100% - 100px);

    .users {
      width: 300px;
      height: 100%;
      display: flex;
      flex-direction: column;

      .user-tree {
        border: 1px solid #ccc;
        border-top: none;
        border-right: none;
        flex: 1;
      }
    }

    .auth-container {
      flex: 1;
      min-width: 0;
      display: flex;
      flex-direction: column;
      height: 100%;

      .auth {
        display: flex;
        width: 100%;
        flex: 1;
        min-height: 0;
        height: 100%;

        .left {
          height: 100%;
          min-height: 0;
          display: flex;
          flex-direction: column;
          border: 1px solid #ccc;

          .search {
            display: flex;
            :deep(.ant-select-selector) {
              border-radius: 0 !important;
            }
            :deep(.ant-input-affix-wrapper) {
              border-radius: 0 !important;
            }
            :deep(.ant-input-search-button) {
              border-radius: 0 !important;
              display: flex;
              justify-content: center;
              align-items: center;
            }
          }

          .auth-btns {
            width: 100%;
            display: flex;

            .auth-btn {
              flex: 1;
            }
            :deep(.ant-btn) {
              border-radius: 0 !important;
            }
          }

          :deep(.search .ant-input) {
            border-top: none;
          }

          .datasource-tree {
            flex: 1;
            min-height: 0;
            border: 1px solid #ccc;
            border-top: none;
          }
          :deep(.vtree-tree__wrapper) {
            height: calc(100% - 64px);
          }

          .no-indent {
            :deep(.vtree-tree-node__square:first-child) {
              display: none;
            }
          }
        }

        .middle {
          flex: 1;
          min-height: 0;
          display: flex;
          flex-direction: column;

          &.no-auth {
            border: 1px solid #ccc;
            border-left: none;
            border-right: none;
          }

          .auth-tree-container {
            display: flex;
            flex: 1;
            min-height: 0;
            // flex-direction: column;
            position: relative;

            .auth-loading {
              position: absolute;
              width: 100%;
              height: 100%;
              display: flex;
              align-items: center; /* 垂直居中 */
              justify-content: center; /* 水平居中，如果需要的话 */
              left: 0;
              top: 0;
              z-index: 999;
              background: rgba(255, 255, 255, 0.8);
            }

            :deep(.search .ant-input) {
              border-left: none;
            }

            .auth-tree {
              flex: 1;
              min-height: 0;
              border-top: none;
              border-left: none;
            }

            .auth-tree-container-right {
              height: 100%;
              border-left: 1px solid #ccc;
              border-right: 1px solid #ccc;
              .label-title {
                height: 45px;
                border-bottom: 1px solid #ccc;
                padding: 14px 16px;
              }
              .option-section {
                padding: 14px 16px;
                border-bottom: 1px solid #eee;
              }
              .option-section-title {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 10px;
                font-weight: 500;
              }
              .all-resource-option {
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 8px;
              }
              .all-resource-tip {
                color: #7a8499;
                font-size: 13px;
                line-height: 20px;
              }
              .time {
                display: flex;
                flex-direction: column;
                align-items: center;
              }
              .time-mid {
                display: flex;
                justify-content: center;
              }
              .ranges {
                padding-top: 4px;
                padding-bottom: 12px;
                text-align: center;
                width: 210px;
                margin: 0 auto;
              }
              .date-btns {
                width: 68px;
                padding: 0 8px;
              }
            }
          }
        }

        .right {
        }

        .tree {
          display: flex;
          flex-direction: column;
        }
      }

      .footer {
        height: 50px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    &:focus {
      background-color: #e6e6e6 !important;
    }
    &:disabled {
      background-color: #f5f5f5 !important;
    }
  }
  :deep(.ivu-tabs-nav-next) {
    line-height: 45px;
  }
  :deep(.ivu-tabs-nav-prev) {
    line-height: 45px;
  }
  :deep(.ivu-tabs-nav-next) {
    border-right: none !important;
  }
  :deep(.ivu-tabs-nav-prev) {
    border-left: none !important;
  }
  :deep(.ivu-tabs-nav-next:hover) {
    background-color: #f5f5f5 !important;
  }
  :deep(.ivu-tabs-nav-prev:hover) {
    background-color: #f5f5f5 !important;
  }
  :deep(.ivu-tabs-nav-next:active) {
    background-color: #e6e6e6 !important;
  }
  :deep(.ivu-tabs-nav-prev:active) {
    background-color: #e6e6e6 !important;
  }
  :deep(.ivu-tabs-nav-next:focus) {
    background-color: #e6e6e6 !important;
  }
  :deep(.ivu-tabs-nav-prev:focus) {
    background-color: #e6e6e6 !important;
  }
  :deep(.ivu-tabs-nav-next:disabled) {
    background-color: #f5f5f5 !important;
  }
  :deep(.ivu-tabs-nav-prev:disabled) {
    background-color: #f5f5f5 !important;
  }
  :deep(.vtree-tree__empty-text_default) {
    position: relative;
    top: 100px;
  }
}

.option-wrap {
  display: flex;
  border-right: 1px solid #ccc;
  border-left: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
  border-top: none;
  height: 60px;
  margin-bottom: 12px;
  padding: 12px;
  justify-content: center;
}

:deep(.node-wrap) {
  display: flex;
  align-items: center;
  .loading-circle {
    display: inline-block;
    width: 15px;
    height: 15px;
    border: 1.5px solid #474747;
    border-radius: 50%;
    border-top-color: transparent;
    margin-right: 5px;
    animation: spin 1s linear infinite;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
}

.operate-btn {
  display: flex;
  justify-content: right;
}

.extra-tab {
  margin-right: 22px;
  line-height: 46px;
}

.time-range-item {
  margin: 8px 0;
}

.time-range {
  color: #515a6e;
  font-size: 12px;
  margin-bottom: 6px;
  display: flex;
  align-items: center;
}

.auth-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.auth-tag {
  border-radius: 3px;
}

.ivu-icon-ios-time-outline {
  margin-right: 6px;
  color: #808695;
}

.divider {
  width: 3px;
  background: linear-gradient(to bottom, #e0e0e0, #f8f8f8, #e0e0e0);
  cursor: col-resize;
  transition:
    background 0.2s,
    box-shadow 0.2s;
  user-select: none;
}

.divider:hover {
  background: linear-gradient(to bottom, #c8c8c8, #eaeaea, #c8c8c8);
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.divider:active {
  background: #b0b0b0;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.15) inset;
}

.page-loading-mask {
  position: fixed;
  z-index: 9999;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
