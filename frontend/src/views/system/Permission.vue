<template>
  <div class="auth-container-wrapper">
    <div v-if="pageLoading" class="page-loading-mask">
      <a-spin size="large" :tip="$t('zheng-zai-jia-zai')" />
    </div>
    <div class="auth-content">
      <div class="auth-container">
        <div class="auth" @mousemove="handleMouseMove" @mouseup="stopDragging">
          <div class="left" :style="{ width: leftWidth + 'px' }">
            <div class="search">
              <!-- <a-select v-if="isEdit" v-model="datasourceTreeSearchType" style="width: 130px" @change="onSearchTypChange">
                <a-select-option value="all">{{ $t('quan-bu') }}</a-select-option>
                <a-select-option value="authed">{{ $t('yi-shou-quan') }}</a-select-option>
                <a-select-option value="unAuth">{{ $t('wei-shou-quan') }}</a-select-option>
              </a-select> -->
              <a-input-search
                class="search"
                @search="onSearchKeyChange"
                :placeholder="$t('sou-suo-shu-xing-jie-gou-zhong-zhan-kai-de-nei-rong')"
                allow-clear
                v-model:value="leftTreeKeyword"
                @change="onSearchKeyChange"
              />
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
                        @checked-change="handleAuthCheck"
                        checkable
                        titleField="i18nName"
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
                        @checked-change="handleAuthCheck"
                        checkable
                        titleField="i18nName"
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
                        @checked-change="handleAuthCheck"
                        checkable
                        titleField="i18nName"
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
                        @checked-change="handleAuthCheck"
                        checkable
                        titleField="i18nName"
                        :defaultExpandAll="true"
                        :disableAll="previewMode || isView"
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div class="auth-tree-container-right" v-if="!isView || previewMode">
                <div class="setting">
                  <div class="label-title">{{ $t('xuan-xiang') }}</div>
                  <section class="option-section">
                    <div class="option-section-title option-section-title--required">
                      <span class="required-title">
                        <span class="required-mark">*</span>
                        {{ $t('shou-quan-shi-jian') }}
                      </span>
                      <a-button size="small" v-if="isEdit" @click="handleClearAuthTime">
                        {{ $t('qing-kong') }}
                      </a-button>
                    </div>
                    <div class="content">
                      <div class="ranges" v-if="isEdit">
                        <div class="range-button-grid">
                          <button
                            v-for="range in authTimeRanges"
                            :key="range.key"
                            type="button"
                            class="date-btns"
                            :class="{ 'is-active': curRangeKey === range.key }"
                            :aria-pressed="curRangeKey === range.key"
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
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="option-wrap">
      <Button @click="backToMyAuth" v-if="!isView && !previewMode" style="margin-right: 10px">
        {{ $t('fan-hui') }}
      </Button>
      <Button @click="continueAuth" v-if="previewMode" style="margin-right: 10px">
        {{ $t('shang-yi-bu') }}
      </Button>
      <Tooltip v-if="isView && !previewMode" :content="rootAccountUnsupportedTip" :disabled="!isRootAccount" transfer placement="top">
        <span style="display: inline-block; margin-right: 10px">
          <Button @click="goApplAuth" type="primary" :disabled="isRootAccount">
            {{ $t('shen-qing-quan-xian') }}
          </Button>
        </span>
      </Tooltip>
      <Tooltip v-if="!isView" :content="rootAccountUnsupportedTip" :disabled="!isRootAccount" transfer placement="top">
        <span style="display: inline-block; margin-right: 10px">
          <Button @click="previewAuth" type="primary" :disabled="isRootAccount">
            {{ $t('ti-jiao-shen-qing') }}
          </Button>
        </span>
      </Tooltip>
      <Tooltip v-if="previewMode" :content="rootAccountUnsupportedTip" :disabled="!isRootAccount" transfer placement="top">
        <span style="display: inline-block; margin-right: 10px">
          <Button @click="submitAuthApply" type="primary" :disabled="isRootAccount">
            {{ $t('ti-jiao-que-ren') }}
          </Button>
        </span>
      </Tooltip>
    </div>
  </div>
</template>

<script lang="jsx">
import dayjs from '@/utils/dayjsSetup';
import VTree from '@wsfe/vue-tree';
import { cloneDeep as deepClone } from '@/utils/lodash';
import { mapGetters, mapState } from 'vuex';
import i18n from '@/i18n';
import { ELEMENT_REVERSE_TYPE_MAP, ELEMENT_TYPE_MAP, ELEMENT_TYPE_REF_MAP, START_RECORD_NAMES_CONUT } from './subaccount/auth/constant';
import { getResTypeToNames, findNodeByKey, fetchWithTimeout, flattenTree } from './subaccount/auth/utils';

export default {
  name: 'MyAuth',
  components: {
    VTree
  },
  data() {
    return {
      authColorMap: {
        DM_QUERY: 'primary',
        DM_DML: 'success'
      },
      resourceManager: false,
      selectedNodeKey: null,
      selectedCcCluster: '',
      curRangeKey: 'permanent',
      authedData: {},
      showAuthedTreeModal: false,
      batchMode: false,
      previewMode: false,
      uid: '',
      isEdit: false,
      isView: true,
      loadingAuth: false,
      activeAuthTab: 'DataSource',
      activeAuthType: 'datasource',
      authTabs: [
        { label: i18n.global.t('shu-ju-yuan'), value: 'DataSource', type: 'datasource' },
        { label: i18n.global.t('ren-wu'), value: 'DataJob', type: 'task' }
      ],
      ranges: [
        {
          label: i18n.global.t('jin-tian'),
          startTime: dayjs(),
          endTime: dayjs().endOf('day')
        },
        {
          label: i18n.global.t('yi-tian'),
          startTime: dayjs(),
          endTime: dayjs().add(1, 'day')
        },
        {
          label: i18n.global.t('yi-zhou'),
          startTime: dayjs(),
          endTime: dayjs().add(1, 'week')
        },
        {
          label: i18n.global.t('yi-ge-yue'),
          startTime: dayjs(),
          endTime: dayjs().add(1, 'month')
        },
        {
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
      curNode: '',
      curElementType: '', // Instance ｜ Catalog ｜ Schema｜ Table |  AllType
      originLeftTree: [],
      originRightTree: {
        Instance: [],
        Schema: [],
        Catalog: [],
        Table: []
      },
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
      isPrimaryAccount: false,
      userHadResource: [],
      timeList: {},
      authMap: {},
      canCheckedChange: false,
      leftWidth: 460,
      isDragging: false,
      userAuthResList: [],
      parentAuthTree: [],
      pageLoading: false
    };
  },
  computed: {
    ...mapGetters(['includesDM', 'includesCC']),
    ...mapState(['userInfo', 'globalSetting', 'dmGlobalSetting', 'productClusterList']),
    getCcProductClusterList() {
      const ccList = [];
      this.productClusterList.forEach((cluster) => {
        if (cluster.product === 'CloudCanal') {
          ccList.push(cluster);
        }
      });
      return ccList;
    },
    isRootAccount() {
      return this.userInfo.accountType === 'PRIMARY_ACCOUNT';
    },
    rootAccountUnsupportedTip() {
      return '管理员账号不支持此操作';
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
        if (this.includesCC && this.includesDM && auth === 'DataJob') {
          disable = true;
        }
        return disable;
      };
    }
  },
  watch: {
    '$route.query.type': {
      handler(newVal) {
        console.log(newVal);
        if (newVal === 'apply') {
          this.goApplAuth();
        } else {
          this.initData();
        }
      },
      deep: true,
      immediate: true
    },
    '$route.params.uid': {
      async handler(newVal, oldVal) {
        console.log('uid', newVal, oldVal);
        if (newVal !== oldVal) {
          this.uid = this.$route.params.uid || this.userInfo.uid;
          this.subAccount = this.$route.params.uid ? this.$route.query.name : '';
          await this.listLevelsForDM();
        }
      },
      deep: true
    }
  },
  methods: {
    resetPermissionModeState(isView) {
      this.isView = isView;
      this.isEdit = true;
      this.activeAuthTab = 'DataSource';
      this.activeAuthType = 'datasource';
      this.lastRightTreeData = [];
      this.lastLeftTreeClickNode = '';
      this.rightTreeKeyword = '';
      this.leftTreeKeyword = '';
      this.selectedNodeKey = null;
      this.isSingleSelect = true;
      this.curElementType = null;
      this.curRightTreeTab = null;
      this.curNode = '';
      this.originLeftTree = [];
      this.expandedKeys = [];
      this.previewMode = false;
      this.batchMode = false;
      this.curRangeKey = 'permanent';
      this.selectedRange = {};
      this.authTime = {
        startTime: null,
        endTime: null
      };
      this.timeList = {};
      this.authMap = {};
      this.canCheckedChange = false;
      this.parentAuthTree = [];
      this.datasource.selectedNode = null;
      this.datasource.searchKey = '';
      this.datasource.searchType = 'all';
      this.task.selectedNode = null;
      this.task.searchKey = '';
      this.task.searchType = 'all';
      this.$nextTick(() => {
        this.$refs.instanceTree?.setData?.([]);
        this.$refs.catalogTree?.setData?.([]);
        this.$refs.schemaTree?.setData?.([]);
        this.$refs.tableTree?.setData?.([]);
      });
    },
    syncAuthRangeKeyFromTime() {
      this.curRangeKey = this.authTime?.startTime || this.authTime?.endTime ? 'custom' : 'permanent';
    },
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
      this.initData();
    },
    async initData() {
      this.pageLoading = true;
      try {
        this.resetPermissionModeState(true);
        this.uid = this.$route.params.uid || this.userInfo.uid;
        this.subAccount = this.$route.params.uid ? this.$route.query.name : '';
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
    async handlePreviewForDm() {
      // Check for editing permission nodes
      function hasEditNode(tree) {
        return tree.some((node) => node.isEdit || (node.children && hasEditNode(node.children)));
      }
      if (!hasEditNode(this.originLeftTree)) {
        this.$Message.warning(this.$t('huan-mei-you-bian-ji-quan-xian'));
        return;
      }
      this.previewMode = true;
      this.isEdit = false;
      this.isView = true;
      this.isSingleSelect = true;

      if (this.batchMode) {
        this.originLeftTree = this.$refs.dataSourceTree.getTreeData();
        const filterTree = this.filterTreeWithCheckedNodes(this.originLeftTree);
        this.$refs.dataSourceTree.setData(filterTree);
        return;
      }

      const filterTree = this.filterTreeWithEditedNodes(this.originLeftTree);
      this.$refs.dataSourceTree.setData(filterTree);
      this.$refs.instanceTree.setData([]);
      this.$refs.schemaTree.setData([]);
      this.$refs.catalogTree.setData([]);
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

        const firstEditedNode = findFirstEditedNode(this.originLeftTree);
        if (firstEditedNode) {
          this.selectedNodeKey = firstEditedNode.key;
          this.$nextTick(() => {
            this.leftTreeNodeClick(firstEditedNode);
          });
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
    // Compare permission tree differences and mark editing status
    handleAuthCheck(selectedNodes) {
      if (this.canCheckedChange) {
        const idx = this.parentAuthTree.findIndex((item) => item?.key === this.curNode?.key);

        if (idx !== -1) {
          this.parentAuthTree[idx].authTree = selectedNodes;
        }
        this.markLeftTreeEdited(this.curNode, this.curElementType, this.lastRightTreeData, selectedNodes);
      }
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
        this.$router.push({ path: '/manager/account' });
      }
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

      if (node?.edit === 'appends') {
        style.background = 'green';
      }

      if (node?.action === 'deletes') {
        style.background = 'red';
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
            <div>{this.getNodeDisplayText(node)}</div>
          </div>
        </div>
      );
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
    getFilterOfTypeAndSearch(tree) {
      tree = this.filterTreeOfType(tree);
      tree = this.handleDataSourceSearch(tree);
      return tree;
    },
    async leftTreeNodeClick(node, isExpand = false) {
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

      // Invalid Name of Example
      const noLegal = node?.objName?.includes('/');
      if (noLegal) {
        this.$Message.warning(this.$t('fa-mi-ming-cheng-bu-zhi-chi'));
        return;
      }

      // Example not started data management
      if (node?.objType === 'Instance' && !node?.objAttr?.enableQuery) {
        let final = [];
        final = this.removeChildrenByKey(this.originLeftTree, node?.key);
        this.originLeftTree = final;

        final = this.getFilterOfTypeAndSearch(this.originLeftTre);
        this.$Message.warning(this.$t('shu-ju-cha-xun-wei-kai-qi'));
        this.$refs.dataSourceTree.setData(final);
        return;
      }

      if (this.previewMode) {
        this.renderPreviewLeftTree(node);
        if (idx === -1) this.expandedKeys.push(node?.key);
        return;
      }

      this.listLevelsForDM(node);
    },
    // Left tree rendering
    async listLevelsForDM(node = null) {
      try {
        this.leftTreeLoading = true;

        // 0, log the current left tree
        if (node) {
          this.lastLeftTreeClickNode = node;
          this.curElementType = node?.objType;
          this.curRightTreeTab = node?.objType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : node?.objType === 'EXTERNAL_CATALOG' ? 'CATALOG' : node?.objType;
        }

        if (node?.children[0]?.levels?.length) {
          let final = [];

          // 1.1 Filtering and search conditions
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

        // 1 Query tree nodes by layer
        let res = {
          data: []
        };
        // Query User Ownership Resource
        if (this.isView) {
          // Leaf Node
          if (this.isLeafNode(node)) {
            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListMyElementOfLeaf({
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
              this.$services.dmAuthListMyElementsOfLevel({
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
            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListElementOfLeaf({
                data: {
                  levels: this.getResPathByIdAndName(node),
                  leafType: this.getResTypeToIds(node) // Instance|Catalog|Schema|Table
                },
                ...config
              })
            );
          } else {
            res = await fetchWithTimeout((config) =>
              this.$services.dmAuthListElementsOfLevel({
                data: {
                  authKind: this.activeAuthTab,
                  resPaths: this.getResPathByIdAndName(node)
                },
                ...config
              })
            );
          }
        }

        if (!res?.data?.length) {
          if (res.msg) {
            if (res.data === null) this.$message.warn(res.msg);
          }

          this.removeChildrenByKey(this.originLeftTree, node?.key);
          this.$refs.dataSourceTree.setData(this.originLeftTree);
          this.leftTreeLoading = false;
          this.curElementType = node?.objType;
          this.curRightTreeTab = node?.objType;
          this.handleGetAuthTreeForDm(node);
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
          // 2. Recursively retrieve all parent grades
          const parentObjIds = this.getParentObjIds(item);
          item.levels = [...parentObjIds, item.objId];
          return item;
        });

        // 3. Render left resource tree
        if (!this.originLeftTree?.length) {
          this.originLeftTree = res.data;
          const final = this.getFilterOfTypeAndSearch(res.data);
          await this.$refs.dataSourceTree.setData(final);
        } else {
          let final = [];
          // 3.1 Insert new subtree data into original tree
          final = this.replaceChildren(this.originLeftTree, res.data, node?.key);

          // 3.2 Remove leaf node phildren properties
          final = this.removeChildrenForTableNodes(final);
          this.originLeftTree = final;

          // 3.3 filtration and search conditions
          final = this.getFilterOfTypeAndSearch(final);

          // 3.4 Rendering left tree
          await this.$refs.dataSourceTree.setData(final);
        }

        // 4. Render Right Permission Tree
        this.handleGetAuthTreeForDm(node);
        this.leftTreeLoading = false;
      } catch (err) {
        this.leftTreeLoading = false;

        const idx = this.expandedKeys.indexOf(node?.key);
        if (idx !== -1) this.expandedKeys.splice(idx, 1);
        this.$Message.error(this.$t('chu-xian-yi-chang-qing-shua-xin-ye-mian-hou-zhong-shi'));
      }
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
          child.checked = !!(parentAuth && parentAuth.checked);
        });
      });
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
      const filterTree = this.filterTreeWithEditedNodes(this.originLeftTree);
      this.$refs.dataSourceTree.setData(filterTree);

      this.handleGetAuthTreeForDm(node);
    },
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
    getResTypeToIds(node) {
      const dsType = node?.objAttr?.dsType || '';
      if (!node) {
        return 'Env';
      }
      if (dsType) {
        const typeLevels = this.dmGlobalSetting.dsSettingDef?.[dsType]?.categories?.levels || [];
        if (typeLevels.length) {
          const idx = typeLevels.indexOf(ELEMENT_TYPE_MAP[node?.objType] || '');
          if (idx > START_RECORD_NAMES_CONUT) return 'Table';
          return ELEMENT_REVERSE_TYPE_MAP[typeLevels[idx + 1]]; // +1 elemenType pointing to subnodes;
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
      return (
        tree
          .map((node) => {
            const filteredChildren = this.filterTreeWithEditedNodes(node.children || []);

            if (node.isEdit || filteredChildren.length > 0) {
              return { ...node, children: filteredChildren };
            }

            return null;
          })
          .filter(Boolean)
          // The outermost environment is not displayed without any subdata
          .filter((node) => !(node?.objType === 'ENV' && (!node.children || node.children.length === 0)))
      );
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
    async handleGetAuthTreeForDm(node = {}) {
      const elementType = node?.objType || '';
      let allAuth = { data: [] };
      let hasAutn = { data: [] };
      let filterAuth;
      if (elementType !== 'ENV') {
        this.curElementType = elementType;
        this.curRightTreeTab = elementType;

        // Render Time
        const lastestNode = findNodeByKey(this.originLeftTree, node?.key);
        this.authTime = lastestNode?.authTime || { startTime: null, endTime: null };
        this.syncAuthRangeKeyFromTime();

        if (node?.markedWithActionRightTree && node.markedWithActionRightTree?.length) {
          // Other Organiser
          filterAuth = node?.markedWithActionRightTree;
          this.lastRightTreeData = filterAuth;
        } else {
          // Retrieving all permissions tree and user-owned permissions tree, contrasting map
          const normalizedElementType = elementType === 'EXTERNAL_CATALOG' ? 'CATALOG' : elementType;
          const normalizedElementType2 = normalizedElementType === 'EXTERNAL_SCHEMA' ? 'SCHEMA' : normalizedElementType;
          allAuth = await this.$services.rdpAuthFetchAuthTreeDef({
            data: {
              kind: this.activeAuthTab,
              elementType: ELEMENT_REVERSE_TYPE_MAP[normalizedElementType2] || normalizedElementType2
            }
          });
          // Local Cache Map
          const flattenAuthTree = flattenTree(allAuth.data);
          flattenAuthTree.forEach((item) => {
            if (!this.authMap[item.key]) {
              this.authMap[item.key] = item.i18nName;
            }
          });
          if (this.findSchemaNodeId(node)) {
            hasAutn = await this.$services.rdpAuthListMyAuthOfRes({
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
          hasAutn.data.forEach((authWrap) => {
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
          this.syncAuthRangeKeyFromTime();
          filterAuth = this.markRightTreeChecked(allAuth.data, [...new Set(hasAuthList)]);
          // 3.1 The full permission tree of the last user is recorded for matching changes
          this.lastRightTreeData = deepClone(filterAuth);

          // 3.2 Parental permission to record
          this.parentAuthTree.push({
            key: node?.key,
            authTree: deepClone(filterAuth)
          });

          // 3.3 Inheritance of paternity rights first
          filterAuth = this.handleAuthFromParent(node, filterAuth);

          // 3.4 Reprocessing from its own authority
          filterAuth = this.handleAuthFromSelf(filterAuth, hasAutn.data, node);
        }
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
            // Rights change judgement
            let isEdit = false;
            if (markedWithActionRightTree) {
              markedWithActionRightTree.forEach((authWrap) => {
                authWrap.children.forEach((auth) => {
                  if (auth.action) isEdit = true;
                });
              });
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
          onText: this.$t('guan-bi'),
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
      res = this.filterTreeOfType();
      res = this.handleDataSourceSearch(res);
      this.$refs.dataSourceTree.setData(res);
    },
    filterTreeOfType(origin = this.originLeftTree) {
      const type = this.datasourceTreeSearchType;
      const filtered = origin?.map((node) => {
        const children = node.children || [];

        if (children[0]?.objType === 'Instance') {
          let newChildren = children;
          if (type === 'authed') {
            newChildren = children.filter((child) => child.isAuthed);
          } else if (type === 'unAuth') {
            newChildren = children.filter((child) => !child.isAuthed);
          }
          return {
            ...node,
            children: newChildren
          };
        }

        // Do not meet filter conditions, return directly to original node
        return node;
      });

      return filtered;
    },
    onSearchKeyChange() {
      const res = this.handleDataSourceSearch();
      this.$refs.dataSourceTree.setData(res);
    },
    handleDataSourceSearch(tree = this.originLeftTree) {
      return this.filterTree(tree, this.leftTreeKeyword, true);
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

      // this.datasource.selectedNode = null;
      // this.task.selectedNode = null;
      if (needSwitch) {
        this.batchMode = !this.batchMode;
      }
      if (this.batchMode) {
        if (this.activeAuthType === 'task') {
          await this.handleGetAuthTree('DataJob');
          console.log('set task');
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
      // this.batchMode = true;
    },
    goSubAccountPage() {
      this.$router.push({ name: 'Management_Accounts_Account' });
    },
    handleGoAuth() {
      this.$router.push({
        path: `/system/account/authdm/${this.uid}?name=${this.subAccount}&&type=edit`
      });
    },
    handleDsExpand(node) {
      this.selectedNodeKey = node?.key;
      const isExpand = true;
      this.leftTreeNodeClick(node, isExpand);
    },
    previewAuth() {
      if (this.isRootAccount) {
        this.$Message.warning(this.rootAccountUnsupportedTip);
        return;
      }
      this.handlePreviewForDm();
    },
    async goApplAuth() {
      if (this.isRootAccount) {
        this.$Message.warning(this.rootAccountUnsupportedTip);
        return;
      }
      this.resetPermissionModeState(false);
      this.uid = this.$route.params.uid || this.userInfo.uid;
      this.subAccount = this.$route.params.uid ? this.$route.query.name : '';
      await this.listLevelsForDM();
      this.$nextTick(async () => {
        if (this.originLeftTree && this.originLeftTree.length > 0) {
          const firstRoot = this.originLeftTree[0];
          if (firstRoot) {
            await this.listLevelsForDM(firstRoot);
            if (firstRoot.children && firstRoot.children.length > 0) {
              // Push only when extandedkeys are not included to avoid forced attributions that cannot be collected
              if (!this.expandedKeys.includes(firstRoot.key)) {
                this.expandedKeys.push(firstRoot.key);
              }
            }
          }
        }
      });
    },
    async submitAuthApply() {
      if (this.isRootAccount) {
        this.$Message.warning(this.rootAccountUnsupportedTip);
        return;
      }
      // this.handlePreviewForDm();
      const filterTree = this.filterTreeWithEditedNodes(this.originLeftTree);
      const authData = this.getApplyAuthData(filterTree);
      const res = await this.$services.rdpTicketCreateDataSourceAuthTicket({
        data: {
          authKind: 'DataSource',
          targetUid: this.uid,
          applyAuths: authData.applyAuths
        }
      });
      if (res?.success) {
        this.$message.success(this.$t('shen-qing-cheng-gong'));
        this.backToMyAuth();
      }
    },
    getApplyAuthData(filterTree) {
      const applyAuths = [];

      const traverse = function (tree) {
        tree.forEach((item) => {
          let resId = null;
          let resPaths = null;
          const authLabels = [];

          resId = this.getInstanceId(item);
          resPaths = getResTypeToNames(item);

          if (item.markedWithActionRightTree?.length) {
            item.markedWithActionRightTree.forEach((auth) => {
              auth.children.forEach((authItem) => {
                if (authItem.checked) {
                  authLabels.push(authItem?.key);
                }
              });
            });
          }

          if (authLabels.length > 0) {
            applyAuths.push({
              resPaths,
              startTime: item?.authTime?.startTime?.format?.('YYYY-MM-DD HH:mm:ss'),
              endTime: item?.authTime?.endTime?.format?.('YYYY-MM-DD HH:mm:ss'),
              authId: 1,
              resId,
              authLabels
            });
          }

          if (item.children?.length) {
            traverse(item.children);
          }
        });
      }.bind(this);

      traverse(filterTree);
      return { applyAuths };
    },
    backToMyAuth() {
      this.isEdit = false;
      this.isView = true;
      this.previewMode = false;
      this.handleReloadPage();
    },
    continueAuth() {
      this.isView = false;
      this.isEdit = true;
      this.previewMode = false;
      this.$refs.dataSourceTree.setData(this.originLeftTree);
    },
    getInstanceId(node) {
      while (node) {
        if (node.objType === 'Instance') {
          return node.objId;
        }
        node = node?.parent;
      }
      return null;
    },
    filterTree(tree, keyword, isEnableQuery = false, depth = 5, level = 0) {
      if (!Array.isArray(tree) || depth <= 0) return [];

      return tree
        .map((node) => {
          if (level >= depth) return null;
          const match = node.objName && node.objName.includes(keyword);
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
  min-height: 0;
  padding: 16px;
  padding-bottom: 0;
  overflow: hidden;
  background: #f7f8fb;

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
            height: 56px;
            border-bottom: 1px solid #eef1f5;

            :deep(.ant-select) {
              height: 100%;
            }

            :deep(.ant-select-selector) {
              height: 100% !important;
              border: 0 !important;
              border-right: 1px solid #eef1f5 !important;
              border-radius: 0 !important;
              box-shadow: none !important;
            }

            :deep(.ant-select-selection-item) {
              line-height: 56px !important;
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
              padding: 0 12px 0 14px;
              border: 0 !important;
              border-radius: 0 !important;
              box-shadow: none !important;
            }

            :deep(.ant-input) {
              height: 100%;
              padding: 0;
              line-height: 56px;
            }

            :deep(.ant-input-suffix) {
              height: 100%;
              display: inline-flex;
              align-items: center;
            }

            :deep(.ant-input-search-button) {
              height: 100%;
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
                  justify-content: space-between;
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

:deep(.node),
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
