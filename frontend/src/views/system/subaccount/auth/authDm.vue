<template>
  <div class="auth-container-wrapper">
    <div v-if="pageLoading" class="page-loading-mask">
      <a-spin size="large" tip="加载中..." />
    </div>
    <div class="auth-content">
      <div class="auth-container">
        <div class="auth" @mousemove="handleMouseMove" @mouseup="stopDragging">
          <div class="left" :style="{ width: leftWidth + 'px' }">
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
              <div class="auth-main">
                <div class="resource-summary">
                  <div class="resource-summary__main">
                    <div class="resource-summary__label">{{ $t('dang-qian-zi-yuan') }}</div>
                    <div class="resource-summary__path" :title="currentResourceText">
                      <template v-for="(item, index) in currentResourceBreadcrumb" :key="`${item}-${index}`">
                        <span class="resource-summary__path-item">{{ item }}</span>
                        <span v-if="index < currentResourceBreadcrumb.length - 1" class="resource-summary__separator">/</span>
                      </template>
                    </div>
                  </div>
                </div>
                <div class="auth-tree">
                  <nav class="auth-tabs">
                    <div class="auth-tabs__items">
                      <span
                        class="auth-tabs__item"
                        :class="{
                          'is-active': curRightTreeTab === 'Instance',
                          'is-disabled': !['Instance', 'INSTANCE', 'AllType'].includes(curElementType)
                        }"
                        @click="handleAuthTabClick('Instance')"
                      >
                        {{ $t('shi-li-quan-xian') }}
                      </span>
                      <span
                        class="auth-tabs__item"
                        :class="{
                          'is-active': curRightTreeTab === 'CATALOG',
                          'is-disabled': !['Catalog', 'CATALOG', 'EXTERNAL_CATALOG', 'AllType'].includes(curElementType)
                        }"
                        @click="handleAuthTabClick('CATALOG')"
                      >
                        {{ $t('catalog-quan-xian') }}
                      </span>
                      <span
                        class="auth-tabs__item"
                        :class="{
                          'is-active': curRightTreeTab === 'SCHEMA',
                          'is-disabled': !['Schema', 'SCHEMA', 'EXTERNAL_SCHEMA', 'AllType'].includes(curElementType)
                        }"
                        @click="handleAuthTabClick('SCHEMA')"
                      >
                        {{ $t('schema-quan-xian') }}
                      </span>
                      <span
                        class="auth-tabs__item"
                        :class="{
                          'is-active': curRightTreeTab === 'TABLE',
                          'is-disabled': !['Table', 'TABLE', 'AllType'].includes(curElementType)
                        }"
                        @click="handleAuthTabClick('TABLE')"
                      >
                        {{ $t('biao-quan-xian') }}
                      </span>
                    </div>
                    <div class="auth-tabs__extra">
                      <Poptip v-show="timeList?.[curNode.key]?.length" trigger="hover" placement="bottom-end" width="350">
                        <span class="auth-tabs__time-link">{{ $t('shou-quan-shi-jian-0') }}</span>
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
                    </div>
                  </nav>
                  <div class="auth-tabs__content">
                    <div v-show="curRightTreeTab === 'Instance'">
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
                    </div>
                    <div v-show="curRightTreeTab === 'CATALOG'">
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
                    </div>
                    <div v-show="curRightTreeTab === 'SCHEMA'">
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
                    </div>
                    <div v-show="curRightTreeTab === 'TABLE'">
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
                    </div>
                  </div>
                </div>
              </div>
              <div class="auth-tree-container-right">
                <div class="setting" v-if="!isView || previewMode">
                  <div class="label-title">
                    {{ $t('xuan-xiang') }}
                  </div>
                  <section class="option-section">
                    <div class="option-section-title option-section-title--required">
                      <span class="required-title">
                        <span class="required-mark">*</span>
                        {{ $t('shou-quan-shi-jian') }}
                      </span>
                    </div>
                    <div class="content">
                      <div class="ranges" v-if="isEdit || previewMode">
                        <div class="range-button-grid">
                          <button
                            v-for="range in authTimeRanges"
                            :key="range.key"
                            type="button"
                            class="date-btns"
                            :class="{ 'is-active': curRangeKey === range.key }"
                            :aria-pressed="curRangeKey === range.key"
                            :disabled="!isEdit"
                            @click="handleRangeChange(range.key)"
                          >
                            {{ range.label }}
                          </button>
                        </div>
                      </div>
                      <div class="time" v-if="showCustomAuthTime">
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
      <!-- 批量授权已启用(验证通过后如需保留) -->
      <Button v-if="!previewMode && isEdit" @click="handleSwitchBatchModeForDm" style="margin-right: 10px">
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
import dayjs from '@/utils/dayjsSetup';
import VTree from '@wsfe/vue-tree';
import { cloneDeep as deepClone } from '@/utils/lodash';
import { mapGetters, mapState } from 'vuex';
import i18n from '@/i18n';
import { AUTH_ELEMENT_TYPES, ELEMENT_REVERSE_TYPE_MAP, ELEMENT_TYPE_MAP, ELEMENT_TYPE_REF_MAP, START_RECORD_NAMES_CONUT } from './constant';
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
      leftWidth: 360,
      isDragging: false,
      curRangeKey: 'permanent',
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
      authTreeRequestSeq: 0,
      authTreeDefAvailabilityCache: {},
      authTime: {
        startTime: null,
        endTime: null
      },
      timeList: {},
      authMap: {},
      userAuthResList: [],
      parentAuthTree: [],
      pageLoading: false,
      selectedAuthCount: 0
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
    },
    currentResourceBreadcrumb() {
      if (!this.curNode?.objName) {
        return [this.$t('qing-xuan-ze-zuo-ce-zi-yuan')];
      }
      const path = [];
      let current = this.curNode;
      while (current) {
        if (current.objName) {
          path.unshift(this.getNodeDisplayText(current));
        }
        current = current.parent;
      }
      return path;
    },
    currentResourceText() {
      return this.currentResourceBreadcrumb.join(' / ');
    },
    authTimeRanges() {
      return [
        {
          key: 'permanent',
          label: this.$t('yong-jiu')
        },
        ...this.ranges1,
        ...this.ranges2,
        {
          key: 'custom',
          label: this.$t('zi-ding-yi')
        }
      ];
    },
    showCustomAuthTime() {
      return this.curRangeKey === 'custom';
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
    handleAuthTabClick(name) {
      if (
        (name === 'Instance' && !['Instance', 'INSTANCE', 'AllType'].includes(this.curElementType)) ||
        (name === 'CATALOG' && !['Catalog', 'CATALOG', 'EXTERNAL_CATALOG', 'AllType'].includes(this.curElementType)) ||
        (name === 'SCHEMA' && !['Schema', 'SCHEMA', 'EXTERNAL_SCHEMA', 'AllType'].includes(this.curElementType)) ||
        (name === 'TABLE' && !['Table', 'TABLE', 'AllType'].includes(this.curElementType))
      ) {
        return;
      }
      this.curRightTreeTab = name;
    },
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
        this.curRangeKey = 'permanent';
        await this.loadAuthTarget();
        this.activeAuthTab = 'DataSource';
        this.activeAuthType = 'datasource';
        this.lastRightTreeData = [];
        this.lastLeftTreeClickNode = '';
        this.parentAuthTree = [];
        this.timeList = {};
        this.authMap = {};
        this.userAuthResList = [];
        this.authTreeRequestSeq = 0;
        this.rightTreeKeyword = '';
        this.leftTreeKeyword = '';
        this.isSingleSelect = true;
        this.curElementType = null;
        this.curRightTreeTab = null;
        this.selectedAuthCount = 0;
        this.originLeftTree = [];
        this.previewMode = false;
        await this.listLevelsForDM();

        // Initial Default Start First Level
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
                // Push only when extandedkeys are not included to avoid forced attributions that cannot be collected
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
        this.syncAuthRangeKeyFromTime();
      }
    },
    async handleResourceManageChange() {
      this.originLeftTree = this.markGlobalResourceAuthState(this.originLeftTree);
      this.$refs.dataSourceTree?.setData(this.getFilterOfTypeAndSearch(this.originLeftTree));
      if (this.curNode?.key && this.curNode?.objType !== 'ENV') {
        await this.handleGetAuthTreeForDm(this.curNode);
      }
      await this.handleGetPreviewData();
    },
    formatAuthTime(value) {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : null;
    },
    syncAuthRangeKeyFromTime() {
      this.curRangeKey = this.authTime?.startTime || this.authTime?.endTime ? 'custom' : 'permanent';
    },
    // Compare permission tree differences and mark editing status
    handleAuthCheck(selectedNodes) {
      const checkedAuthNodes = deepClone(Array.isArray(selectedNodes) ? selectedNodes : []);
      const currentAuthTree = this.getCurrentAuthTreeData();
      this.selectedAuthCount = this.getCheckedPermissionCount(checkedAuthNodes, true);
      if (this.canCheckedChange) {
        const originalRightTreeData = this.curNode?.originalRightTreeData?.length ? this.curNode.originalRightTreeData : this.lastRightTreeData;
        const comparableCheckedNodes = this.getComparableCheckedAuthNodes(checkedAuthNodes, originalRightTreeData);
        this.markLeftTreeEdited(this.curNode, this.curElementType, originalRightTreeData, comparableCheckedNodes);
        this.upsertParentAuthTree(this.curNode?.key, currentAuthTree);
        this.syncDescendantInheritedAuth(this.curNode);
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
      this.expandedKeys = this.getLoadedExpandedKeys(this.originLeftTree);
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
      // Check for editing permission nodes
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
        const filterTree = this.cleanPreviewLeftTreePlaceholders(this.filterTreeWithCheckedNodes(this.originLeftTree));
        this.$refs.dataSourceTree.setData(filterTree);
        return;
      }

      await this.preloadExpandedPreviewNodes({ includeAllInstances: hasGlobalResourceEdit });
      const filterTree = this.getPreviewLeftTreeData();
      this.$refs.dataSourceTree.setData(filterTree);
      this.$refs.instanceTree.setData([]);
      this.$refs.schemaTree.setData([]);
      this.$refs.catalogTree.setData([]);
      this.$refs.tableTree.setData([]);
      this.$nextTick(() => {
        // Finds the first IsEdit for true
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
            this.leftTreeNodeClick(firstEditedNode, false, { loadPreviewChildren: false });
          });
        } else if (hasGlobalResourceEdit) {
          const firstAuthNode = findFirstAuthNode(this.originLeftTree);
          if (firstAuthNode) {
            this.selectedNodeKey = firstAuthNode.key;
            this.$nextTick(() => {
              this.leftTreeNodeClick(firstAuthNode, false, { loadPreviewChildren: false });
            });
          }
        } else if (this.originLeftTree && this.originLeftTree.length > 0) {
          const firstNode = this.originLeftTree[0];
          if (firstNode) {
            this.selectedNodeKey = firstNode.key;
            this.$nextTick(() => {
              this.leftTreeNodeClick(firstNode, false, { loadPreviewChildren: false });
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
      const getAuthLeafNodes = this.getAuthLeafNodes;

      const filterTree = this.filterTreeWithEditedNodes(this.originLeftTree);
      filterTree.forEach((envItem) => {
        envItem.children.forEach((instanceItem) => {
          const getAll = (authDataTree) => {
            if (!authDataTree) return;
            const flattenAuthArr = [];

            if (authDataTree.markedWithActionRightTree?.length) {
              flattenAuthArr.push(...getAuthLeafNodes(authDataTree.markedWithActionRightTree));
            }
            flattenAuthArr.forEach((authItem) => {
              if (authItem.checked && authDataTree.isEdit) {
                appends.push({
                  startTime: authDataTree?.authTime?.startTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  endTime: authDataTree?.authTime?.endTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  resId: instanceItem?.objId,
                  authLabels: [authItem?.key],
                  resPaths: getResTypeToNames(authDataTree)
                });
              }

              if (authItem.action === 'deletes' && authDataTree.isEdit) {
                deletes.push({
                  startTime: authDataTree?.authTime?.startTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  endTime: authDataTree?.authTime?.endTime?.format?.('YYYY-MM-DD HH:mm:ss'),
                  resId: instanceItem?.objId,
                  authLabels: [authItem?.key],
                  resPaths: getResTypeToNames(authDataTree)
                });
              }
            });
            deletes.push(...this.getCascadeDeleteAuthData(authDataTree, instanceItem));

            // Walk Through Subnodes and Call Back
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
          const existingItem = map.get(key);
          existingItem.authLabels = Array.from(new Set(existingItem.authLabels.concat(item.authLabels || [])));
          if (!existingItem.authId && item.authId) {
            existingItem.authId = item.authId;
          }
        } else {
          map.set(key, { ...item, authLabels: Array.from(new Set(item.authLabels || [])) });
        }
      });

      return Array.from(map?.values());
    },
    isInstanceNode(node) {
      return Boolean(node?.objAttr?.dsType) || ['Instance', 'INSTANCE'].includes(node?.objType);
    },
    getNodeHostText(node) {
      const attr = node?.objAttr || {};
      const host = attr.dsHost || attr.host || attr.publicHost || attr.privateHost || '';
      const port = attr.dsPort || attr.port || attr.publicPort || attr.privatePort || '';
      if (!host || !port) {
        return host || port;
      }
      const hostText = String(host);
      const portText = String(port);
      if (hostText.endsWith(`:${portText}`) || /:\d+(\/.*)?$/.test(hostText)) {
        return hostText;
      }
      return `${hostText}:${portText}`;
    },
    getNodeDisplayText(node) {
      if (!node?.objName) {
        return '';
      }
      if (!this.isInstanceNode(node)) {
        return node?.objDesc ? `${node.objName}(${node.objDesc})` : node.objName;
      }
      const desc = node?.objDesc || node.objName;
      const host = this.getNodeHostText(node);
      return host ? `${desc}(${host})` : desc;
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
              {this.getNodeDisplayText(node)}
              {this.isNodeAuthed(node) && <span class='authed-tip'></span>}
            </div>
          </div>
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
    getCheckedPermissionCount(tree = [], selectedOnly = false) {
      let count = 0;
      const traverse = (nodes = []) => {
        nodes.forEach((item) => {
          const children = Array.isArray(item?.children) ? item.children.filter((child) => child?.key || child?.i18nName) : [];
          if (children.length) {
            traverse(children);
            return;
          }
          if (selectedOnly || item?.checked) {
            count += 1;
          }
        });
      };
      traverse(Array.isArray(tree) ? tree : []);
      return count;
    },
    upsertParentAuthTree(key, authTree = []) {
      if (!key) {
        return;
      }
      const idx = this.parentAuthTree.findIndex((item) => item?.key === key);
      const nextAuthTree = deepClone(authTree || []);
      if (idx !== -1) {
        this.parentAuthTree[idx].authTree = nextAuthTree;
        return;
      }
      this.parentAuthTree.push({
        key,
        authTree: nextAuthTree
      });
    },
    clearInheritedAuthState(authTree = []) {
      const traverse = (nodes = []) =>
        nodes.map((node) => {
          const next = { ...node };
          if (next.inherited || next.globalInherited) {
            next.checked = false;
            next.disabled = false;
            delete next.inherited;
            delete next.globalInherited;
            delete next.action;
          }
          if (Array.isArray(next.children)) {
            next.children = traverse(next.children);
          }
          return next;
        });
      return traverse(deepClone(authTree || []));
    },
    filterExplicitAuthNodes(authTree = []) {
      return this.getCheckedLeafAuthNodes(authTree)
        .filter((item) => !item.inherited && !item.globalInherited)
        .map((item) => ({ ...item }));
    },
    getCheckedLeafAuthNodes(authTree = []) {
      const seen = new Set();
      return flattenTree(authTree || []).filter((item) => {
        if (!item?.checked || item?.children?.length || seen.has(item.key)) return false;
        seen.add(item.key);
        return true;
      });
    },
    getAuthLeafNodes(authTree = []) {
      const seen = new Set();
      return flattenTree(authTree || []).filter((item) => {
        if (!item?.key || item?.children?.length || seen.has(item.key)) return false;
        seen.add(item.key);
        return true;
      });
    },
    isPathPrefix(parentPath = [], childPath = []) {
      if (parentPath.length > childPath.length) {
        return false;
      }
      return parentPath.every((item, index) => item === childPath[index]);
    },
    isFullAuthRevoke(authDataTree = {}) {
      if (!authDataTree?.isEdit || !authDataTree?.markedWithActionRightTree?.length) {
        return false;
      }
      const leafNodes = this.getAuthLeafNodes(authDataTree.markedWithActionRightTree);
      if (!leafNodes.length) {
        return false;
      }
      return leafNodes.some((item) => item.action === 'deletes') && leafNodes.every((item) => !item.checked);
    },
    getCascadeDeleteAuthData(authDataTree = {}, instanceItem = {}) {
      if (!this.isFullAuthRevoke(authDataTree)) {
        return [];
      }
      const currentPath = getResTypeToNames(authDataTree);
      const instanceName = instanceItem?.objName;
      const resId = instanceItem?.objId;
      if (!instanceName || !resId) {
        return [];
      }
      return (this.userAuthResList || [])
        .filter((item) => item?.resInstId === instanceName)
        .map((item) => ({
          ...item,
          resPaths: this.normalizeAuthLevel(item?.level),
          authLabels: Array.isArray(item?.dsAuthKinds) ? item.dsAuthKinds.filter(Boolean) : []
        }))
        .filter((item) => this.isPathPrefix(currentPath, item.resPaths))
        .map((item) => ({
          authId: item?.id || 0,
          startTime: item?.startTime ? dayjs(item.startTime).format('YYYY-MM-DD HH:mm:ss') : null,
          endTime: item?.endTime ? dayjs(item.endTime).format('YYYY-MM-DD HH:mm:ss') : null,
          resId,
          authLabels: item.authLabels,
          resPaths: item.resPaths
        }));
    },
    normalizeAuthLevel(level) {
      if (Array.isArray(level)) {
        return level.filter(Boolean);
      }
      if (!level || level === '/') {
        return [];
      }
      return String(level).split('/').filter(Boolean);
    },
    isSameAuthLevel(authWrap, node) {
      const authLevel = this.normalizeAuthLevel(authWrap?.level);
      const currentLevel = getResTypeToNames(node);
      return authLevel.length === currentLevel.length && authLevel.every((item, index) => item === currentLevel[index]);
    },
    getNodeInstanceName(node) {
      let current = node;
      while (current) {
        if (current?.objAttr?.dsType || current?.objType === 'Instance' || current?.objType === 'INSTANCE') {
          return current.objName;
        }
        current = current.parent || current._parent;
      }
      return '';
    },
    getNodeDataSourceType(node) {
      let current = node;
      while (current) {
        if (current?.objAttr?.dsType) {
          return current.objAttr.dsType;
        }
        current = current.parent || current._parent;
      }
      return this.findDataSourceTypeByInstanceId(node?.levels?.[1]);
    },
    findDataSourceTypeByInstanceId(instanceId) {
      if (!instanceId) {
        return '';
      }
      let dsType = '';
      const traverse = (nodes = []) => {
        nodes.some((item) => {
          if (!item || this.isLazyPlaceholderNode(item)) {
            return false;
          }
          if ((item.objId === instanceId || item.levels?.[1] === instanceId) && item.objAttr?.dsType) {
            dsType = item.objAttr.dsType;
            return true;
          }
          if (Array.isArray(item.children) && traverse(item.children)) {
            return true;
          }
          return false;
        });
        return !!dsType;
      };
      traverse(this.originLeftTree || []);
      return dsType;
    },
    getCurrentAuthTreeRef() {
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
      return this.$refs[refMap[normalizedTab]];
    },
    clearRightAuthTreeData() {
      this.$refs.instanceTree?.setData?.([]);
      this.$refs.schemaTree?.setData?.([]);
      this.$refs.catalogTree?.setData?.([]);
      this.$refs.tableTree?.setData?.([]);
    },
    getCurrentAuthTreeData(fallbackTree = []) {
      const currentTree = this.getCurrentAuthTreeRef()?.getTreeData?.();
      return deepClone(currentTree?.length ? currentTree : fallbackTree || []);
    },
    getComparableCheckedAuthNodes(currentAuthTree = [], originalAuthTree = []) {
      const originalCheckedKeys = new Set(this.getCheckedLeafAuthNodes(originalAuthTree || []).map((item) => item.key));
      return this.getCheckedLeafAuthNodes(currentAuthTree || [])
        .filter((item) => {
          const inherited = item.inherited || item.globalInherited;
          return !inherited || originalCheckedKeys.has(item.key);
        })
        .map((item) => ({ ...item }));
    },
    getSyncedInheritedAuthTree(node, authTree = []) {
      let nextAuthTree = this.clearInheritedAuthState(authTree);
      nextAuthTree = this.handleAuthFromParent(node, nextAuthTree);
      nextAuthTree = this.handleAuthFromGlobal(nextAuthTree);
      return nextAuthTree;
    },
    getSyncedSelectedAuthTree(node, selectedAuthTree = []) {
      const explicitSelectedNodes = this.getCheckedLeafAuthNodes(this.clearInheritedAuthState(selectedAuthTree));
      const parentAuthInfo = this.parentAuthTree.find((item) => item?.key === node?.parent?.key);
      const inheritedNodes = this.getCheckedLeafAuthNodes(parentAuthInfo?.authTree || []).map((item) => ({
        ...item,
        checked: true,
        disabled: true,
        inherited: true,
        action: undefined
      }));
      const nodeMap = new Map();
      inheritedNodes.forEach((item) => nodeMap.set(item.key, item));
      explicitSelectedNodes.forEach((item) => nodeMap.set(item.key, item));
      return Array.from(nodeMap.values());
    },
    syncDescendantInheritedAuth(parentNode) {
      if (!parentNode?.children?.length) {
        return;
      }
      const syncNode = (node) => {
        if (!node?.key) {
          return;
        }
        if (node?.objType && node.objType !== 'ENV') {
          let nextAuthTree = null;
          if (node.markedWithActionRightTree?.length) {
            nextAuthTree = this.getSyncedInheritedAuthTree(node, node.markedWithActionRightTree);
            node.markedWithActionRightTree = nextAuthTree;
          } else {
            const parentAuthInfo = this.parentAuthTree.find((item) => item?.key === node.key);
            if (parentAuthInfo?.authTree?.length) {
              nextAuthTree = this.getSyncedSelectedAuthTree(node, parentAuthInfo.authTree);
            }
          }
          if (nextAuthTree) {
            this.upsertParentAuthTree(node.key, nextAuthTree);
          }
        }
        if (node.children?.length) {
          node.children.forEach(syncNode);
        }
      };
      parentNode.children.forEach(syncNode);
    },
    getFilterOfTypeAndSearch(tree) {
      tree = this.filterTreeOfType(tree);
      tree = this.handleDataSourceSearch(tree);
      return tree;
    },
    // Left Tree Click
    async leftTreeNodeClick(node, isExpand = false, options = {}) {
      const idx = this.expandedKeys.indexOf(node?.key);
      const shouldExpand = isExpand ? node?.expand : !node?.expand;
      this.curNode = node;
      this.curElementType = node?.objType;
      this.canCheckedChange = false;
      this.curRangeKey = 'permanent';

      // Keep the arrows together.
      if (!shouldExpand) {
        // Put your behavior together and don't follow the logic.
        if (idx !== -1) this.expandedKeys.splice(idx, 1);
        this.lastLeftTreeClickNode = node;
        this.curElementType = node?.objType;
        this.curRightTreeTab = node?.objType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : node?.objType === 'EXTERNAL_CATALOG' ? 'CATALOG' : node?.objType;

        this.authTime = node?.authTime || { startTime: null, endTime: null };
        this.syncAuthRangeKeyFromTime();

        this.handleGetAuthTreeForDm(node);
        return;
      }
      this.expandedKeys = [...new Set(this.expandedKeys)];

      if (this.batchMode) {
        return;
      }

      if (this.previewMode) {
        const shouldLoadPreviewChildren = options.loadPreviewChildren !== false;
        if (this.isResourceLeafNode(node) || !(await this.ensureResourceNodeCanExpand(node))) {
          this.renderPreviewLeftTree(node);
        } else if (shouldLoadPreviewChildren && this.hasLazyPlaceholderChildren(node) && !this.isLeafNode(node)) {
          await this.listLevelsForDM(node);
          this.$refs.dataSourceTree?.setData(this.getPreviewLeftTreeData());
        } else {
          this.renderPreviewLeftTree(node);
        }
        if (this.expandedKeys.indexOf(node?.key) === -1 && this.canKeepResourceNodeExpanded(node)) {
          this.expandedKeys.push(node?.key);
        }
        return;
      }

      // Invalid Name of Example
      const noLegal = node?.objName?.includes('/');
      if (noLegal) {
        this.$Message.warning(this.$t('fa-mi-ming-cheng-bu-zhi-chi'));
        return;
      }

      if (this.isResourceLeafNode(node)) {
        this.selectLeftTreeResourceNode(node);
        return;
      }

      if (!(await this.ensureResourceNodeCanExpand(node))) {
        this.selectLeftTreeResourceNode(node);
        return;
      }

      // Example not started data management
      if (node?.objType === 'Instance' && !node?.objAttr?.enableQuery) {
        let final = [];
        final = this.removeChildrenByKey(this.originLeftTree, node?.key);
        this.originLeftTree = final;

        final = this.getFilterOfTypeAndSearch(this.originLeftTree);
        this.$Message.warning(this.$t('shu-ju-cha-xun-wei-kai-qi'));
        this.$refs.dataSourceTree?.setData(final);
        return;
      }

      this.listLevelsForDM(node);
    },
    // Left tree rendering
    async listLevelsForDM(node = null, options = {}) {
      const shouldLoadAuthTree = !options || typeof options !== 'object' || options.loadAuthTree !== false;
      try {
        this.leftTreeLoading = true;

        // 0, log the current left tree
        if (node) {
          this.lastLeftTreeClickNode = node;
          this.curElementType = node?.objType;
          this.curRightTreeTab = node?.objType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : node?.objType === 'EXTERNAL_CATALOG' ? 'CATALOG' : node?.objType;
        }

        // Data before reuse
        if (node?.children[0]?.levels?.length) {
          let final = [];

          // 1.1 Filtering and search conditions
          final = this.getFilterOfTypeAndSearch(this.originLeftTree);

          this.$refs.dataSourceTree?.setData(final);

          if (node?.key && shouldLoadAuthTree) {
            this.handleGetAuthTreeForDm(node);
          }
          this.leftTreeLoading = false;
          const idx = this.expandedKeys.indexOf(node?.key);
          if (idx === -1) this.expandedKeys.push(node?.key);
          return;
        }

        if (node?.objType === 'TABLE') {
          const final = this.getFilterOfTypeAndSearch(this.originLeftTree);
          this.$refs.dataSourceTree?.setData(final);
          if (node?.key && shouldLoadAuthTree) {
            this.handleGetAuthTreeForDm(node);
          }
          this.leftTreeLoading = false;
          const idx = this.expandedKeys.indexOf(node?.key);
          if (idx === -1) this.expandedKeys.push(node?.key);
          return;
        }

        // 1 Query tree nodes by layer
        let res = {
          data: []
        };

        // Query User Ownership Resource
        if (this.isView) {
          // Leaf Node
          if (this.isLeafNode(node)) {
            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListUserElementOfLeaf({
                data: {
                  levels: this.getResPathByIdAndName(node),
                  leafType: this.getResTypeToIds(node),
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
                  leafType: this.getResTypeToIds(node)
                },
                ...config
              })
            );
            if (res.success) {
              if (resPaths.length > 2) {
                // Four, Table, authorized.
                const currentPath = getResTypeToNames(node);
                const instanceName = this.getNodeInstanceName(node);
                this.userAuthResList.forEach((item) => {
                  const paths = this.normalizeAuthLevel(item?.level);
                  const isSameIns = item?.resInstId === instanceName;

                  res.data.forEach((ds) => {
                    const isSameParentPath = currentPath.every((pathItem, index) => pathItem === paths[index]);
                    const isParentAuthed = paths.length === currentPath.length;
                    const isChildAuthed = ds.objName === paths[currentPath.length];
                    if (isSameIns && isSameParentPath && (isParentAuthed || isChildAuthed)) {
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
                // res1: Accessible resources for users, requested only once
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
                // 1. Instance authorized
                this.userAuthResList.forEach((item) => {
                  res.data.forEach((ds) => {
                    if (ds.objName === item.resInstId) {
                      ds.isAuthed = true;
                    }
                  });
                });
              } else if (resPaths.length === 2) {
                // 2 Schema authorized
                this.userAuthResList.forEach((item) => {
                  const paths = this.normalizeAuthLevel(item?.level);
                  res.data.forEach((ds) => {
                    if (item?.resInstId === node.objName && (paths.length === 0 || ds.objName === paths[0])) {
                      ds.isAuthed = true;
                    }
                  });
                });
              }
              if (resPaths.length === 3 && (node?.objType === 'CATALOG' || node?.objType === 'EXTERNAL_CATALOG')) {
                // 3, CATALOG has been authorized
                this.userAuthResList.forEach((item) => {
                  const paths = this.normalizeAuthLevel(item?.level);
                  const isSameCatalog = node?.objName === paths[0];

                  res.data.forEach((ds) => {
                    if (item?.resInstId === this.getNodeInstanceName(node) && isSameCatalog && (paths.length === 1 || ds.objName === paths[1])) {
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
            this.$refs.dataSourceTree?.setData(this.originLeftTree);
            this.leftTreeLoading = false;
            this.curElementType = node?.objType;
            this.curRightTreeTab = node?.objType;
            if (node?.key && shouldLoadAuthTree) {
              this.handleGetAuthTreeForDm(node);
            }
          }
          return;
        }

        const idx = this.expandedKeys.indexOf(node?.key);
        if (idx === -1) this.expandedKeys.push(node?.key);

        res.data = await Promise.all(
          res.data.map(async (item) => {
            item.children = [{}];
            item.loaded = false;
            item.levels = [item?.objId];
            item.key = this.genUniqueId();
            item.parent = node;
            // 2. Recursively retrieve all parent grades
            const parentObjIds = this.getParentObjIds(item);
            item.levels = [...parentObjIds, item.objId];
            const canLoadChildren = await this.canLoadResourceChildren(item);
            return this.setNodeExpandCapability(item, canLoadChildren);
          })
        );
        res.data = this.markGlobalResourceAuthState(res.data);

        // 3. Render left resource tree
        if (!this.originLeftTree?.length) {
          this.originLeftTree = res.data;
          const final = this.getFilterOfTypeAndSearch(res.data);
          await this.$refs.dataSourceTree?.setData(final);
        } else {
          let final = [];
          // 3.1 New subtree data inserted into the original tree corresponding node Down
          final = this.replaceChildren(this.originLeftTree, res.data, node?.key);

          // 3.2 Remove leaf node phildren properties
          final = this.removeChildrenForTableNodes(final);

          // 3.3 Auth for marking root nodes
          final = this.getRootTreeAuth(final);
          final = this.markGlobalResourceAuthState(final);

          this.originLeftTree = final;

          // 3.4 Filtering and search conditions
          final = this.getFilterOfTypeAndSearch(final);

          // 3.5 Rendering left tree
          await this.$refs.dataSourceTree?.setData(final);
        }

        // 4. Render Right Permission Tree
        if (node?.key && shouldLoadAuthTree) {
          this.handleGetAuthTreeForDm(node);
        }
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
      const filterTree = this.getPreviewLeftTreeData();
      this.$refs.dataSourceTree.setData(filterTree);

      this.handleGetAuthTreeForDm(node);
    },
    getPreviewLeftTreeData() {
      const filterTree = this.hasGlobalResourceAuthChanges()
        ? this.getFilterOfTypeAndSearch(this.originLeftTree)
        : this.filterTreeWithEditedNodes(this.originLeftTree);
      return this.cleanPreviewLeftTreePlaceholders(filterTree);
    },
    isLazyPlaceholderNode(node) {
      return !!node && !node.key && !node.objName && !node.objType && !node.i18nName && !node.levels?.length;
    },
    hasLazyPlaceholderChildren(node) {
      return Array.isArray(node?.children) && node.children.some((child) => this.isLazyPlaceholderNode(child));
    },
    canKeepResourceNodeExpanded(node) {
      return Array.isArray(node?.children) && node.children.length > 0 && !this.hasLazyPlaceholderChildren(node);
    },
    getLoadedExpandedKeys(tree = [], expandedKeys = this.expandedKeys) {
      const expandedKeySet = new Set(expandedKeys || []);
      const loadedExpandedKeys = [];
      const traverse = (nodes = []) => {
        nodes.forEach((node) => {
          if (!node?.key) {
            return;
          }
          const canKeepExpanded = this.canKeepResourceNodeExpanded(node);
          if (expandedKeySet.has(node.key) && canKeepExpanded) {
            loadedExpandedKeys.push(node.key);
          }
          if (canKeepExpanded) {
            traverse(node.children);
          }
        });
      };
      traverse(Array.isArray(tree) ? tree : []);
      return loadedExpandedKeys;
    },
    getNormalizedNodeType(node) {
      return ELEMENT_TYPE_MAP[node?.objType] || node?.objType;
    },
    normalizeAuthElementType(elementType) {
      const normalized = elementType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : elementType === 'EXTERNAL_CATALOG' ? 'CATALOG' : elementType;
      return ELEMENT_REVERSE_TYPE_MAP[normalized] || normalized;
    },
    isSupportedAuthElementType(elementType) {
      return AUTH_ELEMENT_TYPES.includes(this.normalizeAuthElementType(elementType));
    },
    isResourceLeafNode(node) {
      return !!node?.isLeaf || this.getNormalizedNodeType(node) === 'TABLE';
    },
    canUseLazyPlaceholder(node) {
      return !this.isResourceLeafNode(node);
    },
    selectLeftTreeResourceNode(node) {
      this.lastLeftTreeClickNode = node;
      this.curElementType = node?.objType;
      this.curRightTreeTab = node?.objType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : node?.objType === 'EXTERNAL_CATALOG' ? 'CATALOG' : node?.objType;
      this.authTime = node?.authTime || { startTime: null, endTime: null };
      this.syncAuthRangeKeyFromTime();
      this.handleGetAuthTreeForDm(node);
    },
    setNodeExpandCapability(node, canLoadChildren) {
      if (canLoadChildren) {
        node.children = [{}];
        node.loaded = false;
        node.isLeaf = false;
      } else {
        delete node.children;
        node.loaded = true;
        node.isLeaf = true;
      }
      return node;
    },
    async ensureResourceNodeCanExpand(node) {
      if (!node || this.isResourceLeafNode(node)) {
        return false;
      }
      const canLoadChildren = await this.canLoadResourceChildren(node);
      if (!canLoadChildren) {
        this.setNodeExpandCapability(node, false);
        const latestNode = findNodeByKey(this.originLeftTree, node?.key);
        if (latestNode && latestNode !== node) {
          this.setNodeExpandCapability(latestNode, false);
        }
        const idx = this.expandedKeys.indexOf(node?.key);
        if (idx !== -1) {
          this.expandedKeys.splice(idx, 1);
        }
        const final = this.getFilterOfTypeAndSearch(this.originLeftTree);
        this.$refs.dataSourceTree?.setData(final);
      }
      return canLoadChildren;
    },
    async hasAuthTreeDefForElementType(dsType, elementType) {
      const normalizedElementType = this.normalizeAuthElementType(elementType);
      if (!normalizedElementType) {
        return false;
      }
      const cacheKey = [this.activeAuthTab, dsType || '', normalizedElementType].join('|');
      if (Object.prototype.hasOwnProperty.call(this.authTreeDefAvailabilityCache, cacheKey)) {
        return await this.authTreeDefAvailabilityCache[cacheKey];
      }
      const request = this.$services
        .rdpAuthFetchAuthTreeDef({
          data: {
            kind: this.activeAuthTab,
            dsType,
            elementType: normalizedElementType
          }
        })
        .then((res) => Array.isArray(res?.data) && res.data.length > 0)
        .catch(() => false);
      this.authTreeDefAvailabilityCache[cacheKey] = request;
      const hasDefinition = await request;
      this.authTreeDefAvailabilityCache[cacheKey] = hasDefinition;
      return hasDefinition;
    },
    async canLoadResourceChildren(node) {
      const nodeType = this.getNormalizedNodeType(node);
      if (!node || nodeType === 'TABLE') {
        return false;
      }
      if (nodeType === 'ENV' || nodeType === 'Env') {
        return true;
      }
      const nextElementType = this.getResTypeToIds(node);
      if (!nextElementType || !this.isSupportedAuthElementType(nextElementType)) {
        return false;
      }
      return this.hasAuthTreeDefForElementType(this.getNodeDataSourceType(node), nextElementType);
    },
    cleanPreviewLeftTreePlaceholders(tree = []) {
      const traverse = (nodes = []) =>
        nodes
          .filter((node) => !this.isLazyPlaceholderNode(node))
          .map((node) => {
            const next = { ...node };
            if (Array.isArray(next.children)) {
              const realChildren = next.children.filter((child) => !this.isLazyPlaceholderNode(child));
              if (realChildren.length) {
                next.children = traverse(realChildren);
              } else {
                delete next.children;
              }
            }
            return next;
          });
      return traverse(Array.isArray(tree) ? tree : []);
    },
    getEditedAncestorKeySet() {
      const editedAncestorKeySet = new Set();
      const traverse = (nodes = [], ancestors = []) => {
        nodes.forEach((node) => {
          if (!node?.key) {
            return;
          }
          if (node.isEdit) {
            ancestors.forEach((ancestor) => {
              if (ancestor?.key) {
                editedAncestorKeySet.add(ancestor.key);
              }
            });
          }
          if (Array.isArray(node.children) && !this.hasLazyPlaceholderChildren(node)) {
            traverse(node.children, ancestors.concat(node));
          }
        });
      };
      traverse(this.originLeftTree || []);
      return editedAncestorKeySet;
    },
    async preloadExpandedPreviewNodes(options = {}) {
      const expandedKeySet = new Set(this.expandedKeys || []);
      const editedAncestorKeySet = this.getEditedAncestorKeySet();
      const includeAllInstances = options.includeAllInstances === true;
      const nodesToLoad = [];
      const traverse = (nodes = []) => {
        nodes.forEach((node) => {
          if (!node?.key) {
            return;
          }
          const shouldLoadPreviewChildren = includeAllInstances
            ? expandedKeySet.has(node.key) || node.objType === 'Instance'
            : editedAncestorKeySet.has(node.key);
          if (shouldLoadPreviewChildren && this.hasLazyPlaceholderChildren(node) && !this.isLeafNode(node)) {
            nodesToLoad.push(node);
            return;
          }
          if (Array.isArray(node.children) && !this.hasLazyPlaceholderChildren(node)) {
            traverse(node.children);
          }
        });
      };
      traverse(this.originLeftTree || []);
      for (const node of nodesToLoad) {
        await this.listLevelsForDM(node, { loadAuthTree: false });
      }
    },
    // Returns resPaths parameters consisting of id + name
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
      // Special treatment for CATALOG.
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

    getResTypeToIds(node = null) {
      const dsType = this.getNodeDataSourceType(node);
      const nodeType = ELEMENT_TYPE_MAP[node?.objType] || node?.objType;
      if (!node) {
        return 'Env';
      }
      if (dsType) {
        const categories = this.dmGlobalSetting.dsSettingDef?.[dsType]?.categories || {};
        const typeLevels = (categories.levels || []).map((level) => ELEMENT_TYPE_MAP[level] || level);
        if (typeLevels.length) {
          const idx = typeLevels.indexOf(nodeType || '');
          if (idx === -1) {
            return '';
          }
          if (idx < typeLevels.length - 1) {
            return ELEMENT_REVERSE_TYPE_MAP[typeLevels[idx + 1]] || typeLevels[idx + 1]; // +1 elemenType pointing to subnodes;
          }
          const leafGroup = categories.leafGroup?.[typeLevels[idx]] || [];
          const leafTypes = leafGroup.map((leaf) => (typeof leaf === 'string' ? leaf : leaf?.type)).filter(Boolean);
          const tableLeafType = leafTypes.find((leafType) => this.normalizeAuthElementType(leafType) === 'Table');
          const authLeafType = tableLeafType || leafTypes.find((leafType) => this.isSupportedAuthElementType(leafType));
          return authLeafType ? this.normalizeAuthElementType(authLeafType) : '';
        }
      }
      if (node?.objType !== 'Env') {
        return '';
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
    // Manually Spell Newtree
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
        item.children?.forEach((child) => {
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
      const selfAuth = new Set();
      hasAuth.forEach((item) => {
        if (this.isSameAuthLevel(item, node)) {
          const dsAuthKinds = Array.isArray(item?.dsAuthKinds) ? item.dsAuthKinds : [];
          dsAuthKinds.forEach((authKey) => selfAuth.add(authKey));
        }
      });

      const traverse = (nodes = []) => {
        nodes.forEach((item) => {
          if (item.children?.length) {
            traverse(item.children);
          } else if (selfAuth.has(item.key)) {
            item.checked = true;
          }
        });
      };
      traverse(auth);
      return auth;
    },

    async handleGetAuthTreeForDm(node = {}) {
      const requestId = ++this.authTreeRequestSeq;
      try {
        const elementType = node?.objType || '';
        if (!node?.key || !elementType || elementType === 'ENV') {
          this.selectedAuthCount = 0;
          this.clearRightAuthTreeData();
          return;
        }
        this.loadingAuth = true;
        this.canCheckedChange = false;
        this.clearRightAuthTreeData();
        let allAuth = { data: [] };
        let hasAutn = { data: [] };
        let filterAuth;
        if (elementType) {
          this.curElementType = elementType;
          this.curRightTreeTab = elementType;

          // Render Time
          const lastestNode = findNodeByKey(this.originLeftTree, node?.key);
          this.authTime = lastestNode?.authTime || {
            startTime: null,
            endTime: null
          };
          this.syncAuthRangeKeyFromTime();

          if (node?.markedWithActionRightTree && node.markedWithActionRightTree?.length) {
            // Other Organiser
            filterAuth = deepClone(node.markedWithActionRightTree);
            this.lastRightTreeData = deepClone(filterAuth);
            filterAuth = this.handleAuthFromParent(node, filterAuth);
          } else {
            // Retrieving all permissions tree and user-owned permissions tree, contrasting map
            const normalizedElementType = elementType === 'EXTERNAL_CATALOG' ? 'CATALOG' : elementType;
            const normalizedElementType2 = normalizedElementType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : normalizedElementType;
            allAuth = await this.$services.rdpAuthFetchAuthTreeDef({
              data: {
                kind: this.activeAuthTab,
                dsType: this.getNodeDataSourceType(node),
                elementType: ELEMENT_REVERSE_TYPE_MAP[normalizedElementType2] || normalizedElementType2
              }
            });
            const allAuthTree = Array.isArray(allAuth.data) ? allAuth.data : [];
            const flattenAuthTree = flattenTree(allAuthTree);
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
            const authData = rawAuthData;
            authData.forEach((authWrap) => {
              if (authWrap.startTime) this.authTime.startTime = dayjs(authWrap.startTime);
              if (authWrap.endTime) this.authTime.endTime = dayjs(authWrap.endTime);
              const dsAuthKinds = Array.isArray(authWrap?.dsAuthKinds) ? authWrap.dsAuthKinds : [];
              if (dsAuthKinds.length) hasAuthList.push(...dsAuthKinds);

              if (!this.timeList[node.key]) {
                this.timeList[node.key] = [];
              }

              const exists = this.timeList[node.key].some((item) => item.level === authWrap.level);
              const allExistInFlatten = dsAuthKinds.some((kind) => flattenAuthTree.find((item) => item.key === kind));

              if (!exists && allExistInFlatten) {
                this.timeList[node.key].push({
                  auths: dsAuthKinds,
                  startTime: authWrap.startTime ? dayjs(authWrap.startTime) : null,
                  endTime: authWrap.endTime ? dayjs(authWrap.endTime) : null,
                  level: authWrap.level
                });
              }
            });
            this.syncAuthRangeKeyFromTime();

            filterAuth = this.markRightTreeChecked(allAuthTree, [...new Set(hasAuthList)]);

            // 3.1 The full permission tree of the last user is recorded for matching changes
            this.lastRightTreeData = deepClone(filterAuth);

            // 3.2 Inheritance of paternity rights first
            filterAuth = this.handleAuthFromParent(node, filterAuth);

            // 3.3 Reprocessing from its own authority
            filterAuth = this.handleAuthFromSelf(filterAuth, authData, node);
          }
          // All resource mandates are equal to those at every level.
          filterAuth = this.handleAuthFromGlobal(filterAuth);
          if (requestId !== this.authTreeRequestSeq || this.curNode?.key !== node?.key) {
            return;
          }
          const originalRightTreeData = deepClone(this.lastRightTreeData);
          node.originalRightTreeData = originalRightTreeData;
          const latestNode = findNodeByKey(this.originLeftTree, node?.key);
          if (latestNode) {
            latestNode.originalRightTreeData = deepClone(originalRightTreeData);
          }
          this.upsertParentAuthTree(node?.key, filterAuth);
          this.selectedAuthCount = this.getCheckedPermissionCount(filterAuth);
          this.$nextTick(() => {
            if (requestId !== this.authTreeRequestSeq || this.curNode?.key !== node?.key) {
              return;
            }
            switch (elementType) {
              case 'Instance':
              case 'INSTANCE':
                this.$refs.instanceTree?.setData(filterAuth);
                break;
              case 'Schema':
              case 'SCHEMA':
              case 'EXTERNAL_SCHEMA':
                this.$refs.schemaTree?.setData(filterAuth);
                break;
              case 'CATALOG':
              case 'Catalog':
              case 'EXTERNAL_CATALOG':
                this.$refs.catalogTree?.setData(filterAuth);
                break;
              case 'Table':
              case 'TABLE':
                this.$refs.tableTree?.setData(filterAuth);
                break;
              default:
                break;
            }
            this.canCheckedChange = true;
          });
        }
        this.$refs.dataSourceTree?.scrollTo?.(node?.key, 'center');
      } catch (err) {
        this.$Message.error(this.$t('chu-xian-yi-chang-qing-shua-xin-ye-mian-hou-zhong-shi'));
      } finally {
        if (requestId === this.authTreeRequestSeq) {
          this.loadingAuth = false;
        }
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
            // Rights change judgement
            if (markedWithActionRightTree) {
              isEdit = this.getAuthLeafNodes(markedWithActionRightTree).some((auth) => auth.action);
            }
            // Time change judgement
            const oldTime = item.authTime || {};
            const newTime = this.authTime || {};
            // As long as the starttime or endtime changes
            const oldStart = oldTime.startTime ? (oldTime.startTime.valueOf ? oldTime.startTime.valueOf() : oldTime.startTime) : '';
            const oldEnd = oldTime.endTime ? (oldTime.endTime.valueOf ? oldTime.endTime.valueOf() : oldTime.endTime) : '';
            const newStart = newTime.startTime ? (newTime.startTime.valueOf ? newTime.startTime.valueOf() : newTime.startTime) : '';
            const newEnd = newTime.endTime ? (newTime.endTime.valueOf ? newTime.endTime.valueOf() : newTime.endTime) : '';
            if (oldStart !== newStart || oldEnd !== newEnd) {
              isEdit = true;
            }
            item.markedWithActionRightTree = markedWithActionRightTree;
            item.originalRightTreeData = deepClone(oldTree || []);
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
      this.originLeftTree = res;
      this.$refs.dataSourceTree.setData(this.getFilterOfTypeAndSearch(res));
      return markedWithActionRightTree;
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
        // Filtered to Null Node
        .filter(Boolean)
        // The outermost environment is not displayed without any subdata
        .filter((node) => !(node?.objType === 'ENV' && (!node.children || node.children.length === 0)));

      return filtered;
    },
    onSearchKeyChange() {
      let res = [];
      // Use deep copy to avoid contamination of raw data
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
      const currentAuthTree = this.getCurrentAuthTreeData();
      const rightTreeData = this.getComparableCheckedAuthNodes(currentAuthTree, this.lastRightTreeData);

      // Update only when nodes are selected and times are changed
      if (this.curNode?.objId) {
        this.markLeftTreeEdited(this.curNode, this.curElementType, this.lastRightTreeData, rightTreeData);
      }
    },
    handleStartTimeChange() {
      this.curRangeKey = 'custom';
      this.selectedRange = {};
      this.handleAuthTimeChange();
    },
    handleEndTimeChange() {
      this.curRangeKey = 'custom';
      this.selectedRange = {};
      this.handleAuthTimeChange();
    },
    handleClearAuthTime() {
      this.curRangeKey = 'permanent';
      this.selectedRange = {};
      this.authTime.startTime = null;
      this.authTime.endTime = null;
      this.handleAuthTimeChange();
    },
    handleRangeChange(rangeKey) {
      this.curRangeKey = rangeKey;
      if (rangeKey === 'permanent') {
        this.handleClearAuthTime();
        return;
      }
      if (rangeKey === 'custom') {
        this.curRangeKey = rangeKey;
        this.selectedRange = {};
        this.authTime.startTime = null;
        this.authTime.endTime = null;
        this.handleAuthTimeChange();
        return;
      }
      const selectedObj = [...this.ranges1, ...this.ranges2].find((item) => item.key === rangeKey);
      if (!selectedObj) return;
      this.selectedRange = selectedObj;
      this.authTime.startTime = selectedObj.startTime;
      this.authTime.endTime = selectedObj.endTime;
      // Update authorization time after selecting the range.
      this.handleAuthTimeChange();
    },
    disabledStartDate(startValue) {
      const endValue = this.authEndTime;
      if (!startValue || !endValue) {
        return false;
      }
      return startValue.valueOf() > endValue.valueOf();
    },
    disabledEndDate(endValue) {
      const startValue = this.authStartTime;
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
      this.$router.push({ name: 'Management_Accounts_Account' });
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
      const lowerKeyword = String(keyword || '').toLowerCase();

      return tree
        .map((node) => {
          if (level >= depth) return null;
          const matchText = this.isInstanceNode(node) ? node.objDesc || node.objName : `${node.objName || ''} ${node.objDesc || ''}`;
          const match = String(matchText || '')
            .toLowerCase()
            .includes(lowerKeyword);
          const children = node.children ? this.filterTree(node.children, keyword, isEnableQuery, depth, level + 1) : [];

          if (match || children.length > 0) {
            const newNode = { ...node };

            if (children.length > 0) {
              newNode.children = children;
            } else if (!isEnableQuery && this.canUseLazyPlaceholder(newNode)) {
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
  min-height: 0;
  padding: 16px;
  padding-bottom: 0;
  overflow: hidden;
  background: #fff;

  .auth-content {
    flex: 1;
    min-height: 0;

    .auth-container {
      display: flex;
      flex-direction: column;
      height: 100%;
      min-width: 0;
      min-height: 0;

      .auth {
        display: flex;
        gap: 12px;
        width: 100%;
        height: 100%;
        min-height: 0;

        .left {
          flex-shrink: 0;
          min-width: 320px;
          max-width: 520px;
          height: 100%;
          min-height: 0;
          display: flex;
          flex-direction: column;
          overflow: hidden;
          background: #fff;
          border: 1px solid #e6eaf0;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(18, 38, 63, 0.04);

          > .search {
            display: flex;
            flex-shrink: 0;
            height: 36px;
            border-bottom: 1px solid #eef1f5;

            :deep(.ant-select) {
              height: 100%;
            }

            :deep(.ant-select-selector) {
              height: 100% !important;
              display: flex !important;
              align-items: center !important;
              border: 0 !important;
              border-right: 1px solid #eef1f5 !important;
              border-radius: 0 !important;
              box-shadow: none !important;
            }

            :deep(.ant-select-selection-item) {
              line-height: 1 !important;
            }

            :deep(.ant-input-search) {
              flex: 1;
              min-width: 0;
              height: 100%;
            }

            :deep(.ant-input-wrapper),
            :deep(.ant-input-group) {
              height: 100%;
            }

            :deep(.ant-input-affix-wrapper) {
              height: 100%;
              display: flex;
              align-items: center;
              padding: 0 10px 0 12px;
              border: 0 !important;
              border-radius: 0 !important;
              box-shadow: none !important;
            }

            :deep(.ant-input) {
              height: auto;
              padding: 0;
              line-height: 1.4;
            }

            :deep(.ant-input-suffix) {
              height: 100%;
              display: inline-flex;
              align-items: center;
            }

            :deep(.ant-input-search-button) {
              height: 100%;
              width: 36px;
              padding: 0;
              border: 0 !important;
              border-left: 1px solid #eef1f5 !important;
              border-radius: 0 !important;
              display: flex;
              align-items: center;
              justify-content: center;
              box-shadow: none !important;
            }
          }

          .datasource-tree {
            flex: 1;
            min-height: 0;
            padding: 8px 10px 12px;
            overflow: hidden;
            border: 0;
          }

          :deep(.vtree-tree__wrapper) {
            height: 100%;
            overflow: auto;
          }

          :deep(.vtree-tree-node) {
            min-height: 34px;
          }
        }

        .middle {
          flex: 1;
          min-width: 0;
          min-height: 0;
          display: flex;
          flex-direction: column;

          .auth-tree-container {
            display: flex;
            gap: 12px;
            flex: 1;
            min-width: 0;
            min-height: 0;
            position: relative;

            .auth-loading {
              position: absolute;
              inset: 0;
              z-index: 999;
              display: flex;
              align-items: center;
              justify-content: center;
              background: rgba(255, 255, 255, 0.78);
              border-radius: 8px;
            }

            .auth-main {
              flex: 1;
              min-width: 0;
              min-height: 0;
              display: flex;
              flex-direction: column;
              gap: 12px;
            }

            .resource-summary {
              display: flex;
              align-items: center;
              justify-content: flex-start;
              min-height: 82px;
              padding: 16px 22px;
              background: #fff;
              border: 1px solid #e0f3e9;
              border-radius: 8px;
              box-shadow: 0 2px 8px rgba(18, 38, 63, 0.04);

              &__main {
                min-width: 0;
              }

              &__label {
                margin-bottom: 8px;
                color: #6b778c;
                font-size: 13px;
                line-height: 18px;
              }

              &__path {
                display: flex;
                align-items: center;
                min-width: 0;
                color: #27364b;
                font-size: 15px;
                line-height: 22px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              }

              &__path-item {
                min-width: 0;
                overflow: hidden;
                text-overflow: ellipsis;
              }

              &__separator {
                flex: none;
                margin: 0 7px;
                color: #33c785;
                font-weight: 600;
              }
            }

            .auth-tree {
              flex: 1;
              min-height: 0;
              display: flex;
              flex-direction: column;
              overflow: hidden;
              background: #fff;
              border: 1px solid #e6eaf0;
              border-radius: 8px;
              box-shadow: 0 2px 8px rgba(18, 38, 63, 0.04);
            }

            .auth-tabs {
              display: flex;
              align-items: center;
              justify-content: space-between;
              flex-shrink: 0;
              min-height: 58px;
              padding: 0 22px;
              border-bottom: 1px solid #edf1f5;

              &__items {
                display: flex;
                align-items: center;
                gap: 28px;
              }

              &__item {
                position: relative;
                display: inline-flex;
                align-items: center;
                min-height: 58px;
                color: #6b778c;
                font-size: 14px;
                cursor: pointer;
                transition: color 0.12s ease;

                &:hover {
                  color: #253044;
                }

                &.is-active {
                  color: #18b978;
                  font-weight: 600;

                  &::after {
                    content: '';
                    position: absolute;
                    left: 0;
                    right: 0;
                    bottom: -1px;
                    height: 2px;
                    background: #33c785;
                    border-radius: 2px;
                  }
                }

                &.is-disabled {
                  color: #c1c7d0;
                  cursor: not-allowed;
                }
              }

              &__extra {
                flex: none;
              }

              &__time-link {
                color: #6b778c;
                font-size: 13px;
                cursor: pointer;

                &:hover {
                  color: #18b978;
                }
              }

              &__content {
                flex: 1;
                min-height: 0;
                padding: 18px 24px;
                overflow: auto;

                > div {
                  height: 100%;
                  min-height: 0;
                }

                :deep(.vtree-tree__wrapper) {
                  height: 100%;
                  overflow: auto;
                }

                :deep(.vtree-tree-node) {
                  min-height: 40px;
                }

                :deep(.vtree-tree-node__checkbox:focus) {
                  box-shadow: 0 0 0 2px rgba(62, 207, 142, 0.2);
                }

                :deep(.vtree-tree-node__checkbox:hover) {
                  border-color: #3ecf8e;
                }

                :deep(.vtree-tree-node__checkbox_checked),
                :deep(.vtree-tree-node__checkbox_indeterminate) {
                  border-color: #3ecf8e;
                  background-color: #3ecf8e;
                }

                :deep(.vtree-tree-node__checkbox_checked:hover),
                :deep(.vtree-tree-node__checkbox_indeterminate:hover) {
                  border-color: #3ecf8e;
                  background-color: #3ecf8e;
                }

                :deep(.vtree-tree-node__checkbox_checked.vtree-tree-node__checkbox_disabled),
                :deep(.vtree-tree-node__checkbox_indeterminate.vtree-tree-node__checkbox_disabled) {
                  border-color: #3ecf8e;
                  background-color: #3ecf8e;
                  opacity: 0.68;
                }

                :deep(.vtree-tree-node__checkbox_checked.vtree-tree-node__checkbox_disabled::after),
                :deep(.vtree-tree-node__checkbox_indeterminate.vtree-tree-node__checkbox_disabled::after) {
                  border-color: #fff;
                }

                :deep(.vtree-tree-node__title:hover) {
                  background-color: #eefaf4;
                }

                :deep(.vtree-tree-node__title_selected),
                :deep(.vtree-tree-node__title_selected:hover) {
                  background-color: #def6eb;
                }
              }
            }

            .auth-tree-container-right {
              flex: 0 0 320px;
              min-height: 0;
              overflow: hidden;
              background: #fff;
              border: 1px solid #e6eaf0;
              border-radius: 8px;
              box-shadow: 0 2px 8px rgba(18, 38, 63, 0.04);

              .setting {
                height: 100%;
                min-height: 0;
                display: flex;
                flex-direction: column;
              }

              .label-title {
                flex-shrink: 0;
                height: 58px;
                padding: 18px 22px;
                border-bottom: 1px solid #edf1f5;
                color: #253044;
                font-size: 16px;
                font-weight: 600;
                line-height: 22px;
              }

              .option-section {
                flex-shrink: 0;
                padding: 20px 22px;
                border-bottom: 1px solid #edf1f5;
              }

              .option-section-title {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 14px;
                color: #253044;
                font-size: 14px;
                font-weight: 600;

                :deep(.ant-btn) {
                  height: 24px;
                  padding: 0;
                  border: 0;
                  color: #18b978;
                  background: transparent;
                  box-shadow: none;
                }

                &--required {
                  justify-content: flex-start;
                }

                .required-title {
                  display: inline-flex;
                  align-items: center;
                }

                .required-mark {
                  margin-right: 4px;
                  color: #ff4d4f;
                  font-weight: 600;
                }
              }

              .content {
                min-width: 0;
              }

              .ranges {
                width: 100%;
                padding: 0 0 14px;
              }

              .range-button-grid {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 10px;
                width: 100%;
              }

              .date-btns {
                width: 100%;
                height: 38px;
                padding: 0;
                color: #536079;
                background: #fff;
                border: 1px solid #dcdee2;
                border-radius: 6px;
                text-align: center;
                line-height: 36px;
                cursor: pointer;
                transition:
                  color 0.12s ease,
                  border-color 0.12s ease,
                  background-color 0.12s ease;

                &:hover {
                  color: #18b978;
                  border-color: #3ecf8e;
                }

                &:disabled {
                  color: #8a94a6;
                  background: #f7f8fa;
                  border-color: #e4e7ec;
                  cursor: not-allowed;
                }

                &:focus,
                &:focus-visible,
                &:active {
                  outline: none;
                  box-shadow: none;
                }

                &.is-active {
                  color: #18b978;
                  background: #eaf9f3;
                  border-color: #3ecf8e;
                }
              }

              .time {
                display: flex;
                flex-direction: column;
                gap: 10px;
                align-items: stretch;
              }

              .time-mid {
                display: flex;
                justify-content: center;
                color: #6b778c;
                line-height: 18px;
              }

              :deep(.ant-picker) {
                width: 100%;
                height: 38px;
                border-radius: 6px;
              }

              .all-resource-option {
                display: flex;
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
              }

              .all-resource-tip {
                color: #98a2b3;
                font-size: 13px;
                line-height: 20px;
              }
            }
          }
        }
      }
    }
  }

  :deep(.vtree-tree__empty-text_default) {
    position: relative;
    top: 100px;
  }

  :deep(.left .vtree-tree-node__wrapper_is-leaf > .vtree-tree-node__node-body > .vtree-tree-node__square.vtree-tree-node__expand) {
    visibility: hidden;
    pointer-events: none;
  }
}

.option-wrap {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 72px;
  margin: 12px 0 16px;
  padding: 14px 16px;
  background: #fff;
  border: 1px solid #e6eaf0;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(18, 38, 63, 0.04);
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
