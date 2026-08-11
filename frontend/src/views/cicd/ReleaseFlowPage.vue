<template>
  <div class="release-flow-page">
    <ReleaseFlowSuccess
      v-if="createResult"
      :webhook="webhook"
      :is-built-in="isBuiltIn"
      :has-parent="Boolean(flowGitOpsForm.parentFlowId)"
      :batch-result="batchCreateResult"
      @copy="handleCopyTemp"
      @open-url="handleJumpUrl"
      @jump-doc="jumpToWebhookDoc"
      @go-created-flow="goCreatedFlow"
    />

    <template v-else>
      <Spin v-if="loading" fix />

      <div v-if="!batchCreateMode" class="release-flow-shell" :class="`release-flow-shell-${currentStepKey}`">
        <div class="release-flow-main">
          <ReleaseFlowBasicInfo
            v-if="currentStepKey === 'basic'"
            ref="basicInfo"
            :create-mode="createFlowMode"
            :flow-basic-form="flowBasicForm"
            :basic-rules="basicRules"
            :devops-users="devopsUsers"
            @select-open-change="handleSelectDropdownOpen"
          />

          <ReleaseFlowPipelineConfig
            v-if="currentStepKey === 'basic'"
            ref="pipelineConfig"
            :flow-git-ops-form="flowGitOpsForm"
            :release-rules="releaseRules"
            :source-scm-type="sourceScmType"
            :source-type-card-list="sourceTypeCardList"
            :is-built-in="isBuiltIn"
            :parent-flow-list="parentFlowList"
            :parent-flow-loading="parentFlowLoading"
            :database-type-locked="isBuiltIn && Boolean(flowGitOpsForm.parentFlowId)"
            :filtered-devops-scm-list="filteredDevopsScmList"
            :devops-scm-selected="devopsScmSelected"
            :devops-repo-list-by-group="devopsRepoListByGroup"
            :repo-loading="repoLoading"
            :devops-to="devopsTo"
            :database-type-options="databaseTypeOptions"
            :devops-ins-list="devopsInsList"
            :filtered-devops-ins-list="filteredDevopsInsList"
            :devops-ins-catalog-list="devopsInsCatalogList"
            :devops-ins-schema-list="devopsInsSchemaList"
            :schema-select-disabled="schemaSelectDisabled"
            :schema-loading="schemaLoading"
            :init-options="initOptions"
            :event-type-map="EVEN_TYPE_MAP"
            :flow-git-ops-description="fetchFlowGitOpsDescription"
            @source-type-select="handleSourceTypeSelect"
            @parent-flow-change="handleParentFlowSelected"
            @devops-scm-change="handleDevopsScmSelected"
            @devops-repo-change="handleDevopsRepoSelected"
            @repo-jump="handleDevopsJumpToRepo"
            @database-type-select="handleDatabaseTypeSelect"
            @devops-ins-change="handleDevopsChangeIns"
            @catalog-change="handleChangeCatalog"
            @refresh-catalog-list="fetchCatalogList"
            @select-open-change="handleSelectDropdownOpen"
            @refresh-schema-list="handleRefreshSchemaList"
            @add-scm="goToAddScm"
            @ds-setting="goToDsSetting"
          />

          <ReleaseFlowNoticeConfig
            v-if="currentStepKey === 'config'"
            :flow-im-form="flowImForm"
            :im-def-list="imDefList"
            :im-def-selected="imDefSelected"
            :is-im-disabled="isImDisabled"
            :im-provider-list="imProviderList"
            :subscription-items="subscriptionItems"
            @im-def-select="handleImDefOne"
            @im-provider-change="handleImProviderSelected"
            @select-open-change="handleSelectDropdownOpen"
            @add-im="goToAddIm"
          />
          <ReleaseFlowWorkOrderConfig v-if="currentStepKey === 'config'" />
        </div>

        <ReleaseFlowSummary
          :create-mode="createFlowMode"
          :flow-basic-form="flowBasicForm"
          :flow-git-ops-form="flowGitOpsForm"
          :selected-manager-name="selectedManagerName"
          :selected-scm-name="selectedScmName"
          :selected-instance-name="selectedInstanceName"
          :selected-init-label="selectedInitLabel"
          :summary-im-channel="summaryImChannel"
          :selected-im-provider-name="selectedImProviderName"
          :subscription-summary="subscriptionSummary"
          :is-built-in="isBuiltIn"
          :selected-parent-flow-name="selectedParentFlowName"
          @open-help="openHelp"
        />
      </div>

      <template v-else>
        <ReleaseFlowBatchOrchestration
          v-show="currentStepKey === 'basic'"
          ref="batchOrchestration"
          :devops-users="devopsUsers"
          :devops-ins-list="devopsInsList"
          :database-type-options="databaseTypeOptions"
          :ds-setting-def="dmGlobalSetting.dsSettingDef || {}"
          :initial-manager-uid="userInfo?.uid || ''"
          @created="handleBatchCreated"
        />

        <div v-if="currentStepKey === 'config'" class="release-flow-shell release-flow-shell-config release-flow-batch-config-shell">
          <div class="release-flow-main">
            <ReleaseFlowNoticeConfig
              :flow-im-form="flowImForm"
              :im-def-list="imDefList"
              :im-def-selected="imDefSelected"
              :is-im-disabled="isImDisabled"
              :im-provider-list="imProviderList"
              :subscription-items="subscriptionItems"
              @im-def-select="handleImDefOne"
              @im-provider-change="handleImProviderSelected"
              @select-open-change="handleSelectDropdownOpen"
              @add-im="goToAddIm"
            />
            <ReleaseFlowWorkOrderConfig />
          </div>
        </div>
      </template>

      <div class="page-footer">
        <Button v-if="currentStep > 1" @click="handlePreviousStep">{{ $t('shang-yi-bu') }}</Button>
        <Button type="primary" class="primary-action" :loading="submitting" @click="handleSubmit">
          {{ submitButtonText }}
        </Button>
      </div>
    </template>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { handleCopy } from '@/utils/clipboard';
import ReleaseFlowBasicInfo from './components/ReleaseFlowBasicInfo.vue';
import ReleaseFlowBatchOrchestration from './components/ReleaseFlowBatchOrchestration.vue';
import ReleaseFlowNoticeConfig from './components/ReleaseFlowNoticeConfig.vue';
import ReleaseFlowPipelineConfig from './components/ReleaseFlowPipelineConfig.vue';
import ReleaseFlowSuccess from './components/ReleaseFlowSuccess.vue';
import ReleaseFlowSummary from './components/ReleaseFlowSummary.vue';
import ReleaseFlowWorkOrderConfig from './components/ReleaseFlowWorkOrderConfig.vue';
import { defaultLanguageMap, EVEN_TYPE_MAP, GITOPS_DESCRIPTION } from './constant';
import { DEFAULT_DEVOPS_INFO, DEFAULT_FLOW_INFO, getRepoSelectionKey, getScmDisplayName, groupByRepoNamespace } from './utils';

const getDefaultFlowInfo = () => ({ ...DEFAULT_FLOW_INFO });

const getDefaultGitOpsInfo = () => ({
  ...DEFAULT_DEVOPS_INFO,
  databaseType: 'MySQL',
  parentFlowId: null
});

export default {
  name: 'CicdReleaseFlowPage',
  components: {
    ReleaseFlowBasicInfo,
    ReleaseFlowBatchOrchestration,
    ReleaseFlowNoticeConfig,
    ReleaseFlowPipelineConfig,
    ReleaseFlowSuccess,
    ReleaseFlowSummary,
    ReleaseFlowWorkOrderConfig
  },
  data() {
    return {
      loading: false,
      submitting: false,
      creationMode: 'single',
      batchCreateResult: null,
      currentStep: 1,
      flowId: '',
      flowInfo: null,
      createResult: null,
      webhook: {
        password: '',
        url: '',
        webHookHelpUrl: '',
        repoUrl: ''
      },
      flowBasicForm: getDefaultFlowInfo(),
      flowGitOpsForm: getDefaultGitOpsInfo(),
      flowImForm: {
        imId: null,
        imType: 'none',
        language: 'zh',
        eventChangeFlowStatus: false,
        eventFlowConfig: false,
        eventChangeLife: false,
        eventChangeNotice: false
      },
      devopsUsers: [],
      sourceScmType: 'BUILT_IN',
      parentFlowList: [],
      parentFlowLoading: false,
      devopsScmList: [],
      devopsScmSelected: null,
      devopsRepoList: [],
      devopsRepoListByGroup: {},
      devopsRepoSelected: null,
      devopsInsList: [],
      devopsInsSelected: null,
      devopsInsCatalogList: [],
      devopsInsSchemaList: [],
      imDefList: [],
      imDefSelected: {
        imType: 'none',
        imTypeI18n: ''
      },
      imProviderList: [],
      repoLoading: false,
      schemaLoading: false,
      devopsTo: 'MySQL',
      releaseFlowDropdownCleanup: null,
      defaultLanguageMap,
      EVEN_TYPE_MAP
    };
  },
  computed: {
    ...mapState(['userInfo', 'dmGlobalSetting']),
    createFlowMode() {
      return this.$route.path === '/cicd/create';
    },
    batchCreateMode() {
      return this.createFlowMode && this.creationMode === 'batch';
    },
    wizardSteps() {
      const steps = [{ key: 'basic', label: this.$t('ji-ben-xin-xi') }];
      if (this.createFlowMode) {
        steps.push({ key: 'config', label: this.$t('liu-cheng-pei-zhi-pei-zhi') });
      }
      return steps;
    },
    currentStepKey() {
      return this.wizardSteps[this.currentStep - 1]?.key || 'basic';
    },
    isLastStep() {
      return this.currentStep >= this.wizardSteps.length;
    },
    submitButtonText() {
      return this.isLastStep ? this.$t('wan-cheng') : this.$t('xia-yi-bu');
    },
    basicRules() {
      return {
        flowName: [
          {
            required: true,
            message: this.getInputRequiredMessage('xiang-mu-ming-cheng'),
            trigger: 'blur'
          }
        ],
        flowManagerUid: [
          {
            required: true,
            message: this.getSelectRequiredMessage('fu-ze-ren'),
            trigger: 'change'
          }
        ]
      };
    },
    releaseRules() {
      return {
        repoScmId: [{ validator: this.validateRepoScm, trigger: 'change' }],
        repoSelectionKey: [
          {
            required: true,
            message: this.$t('qing-xuan-ze-cang-ku'),
            trigger: 'change'
          }
        ],
        repoBranch: [
          {
            required: true,
            message: this.getInputRequiredMessage('mu-biao-fen-zhi'),
            trigger: 'blur'
          }
        ],
        eventType: [
          {
            required: true,
            message: this.getSelectRequiredMessage('chu-fa-fang-shi'),
            trigger: 'change'
          }
        ],
        databaseType: [
          {
            required: true,
            message: this.getSelectRequiredMessage('shu-ju-ku-lei-xing'),
            trigger: 'change'
          }
        ],
        instanceId: [
          {
            required: true,
            message: this.$t('qing-xuan-ze-shu-ju-ku-shi-li'),
            trigger: 'change'
          }
        ],
        catalogName: [{ validator: this.validateCatalog, trigger: 'change' }],
        schemaName: [{ validator: this.validateSchema, trigger: 'change' }],
        initScript: [
          {
            required: true,
            message: this.getSelectRequiredMessage('chu-shi-hua-fang-shi'),
            trigger: 'change'
          }
        ]
      };
    },
    initOptions() {
      return [
        { value: 'Snapshot', label: this.$t('jian-li-ji-xian-kuai-zhao') },
        { value: 'CreateChange', label: this.$t('sheng-cheng-chu-shi-hua-bian-geng') },
        { value: 'None', label: this.$t('tiao-guo-chu-shi-hua') }
      ];
    },
    databaseTypeList() {
      const types = this.devopsInsList.map((item) => item?.objAttr?.dsType).filter(Boolean);
      return [...new Set(types)];
    },
    databaseTypeOptions() {
      return ['MySQL', ...this.databaseTypeList.filter((type) => type !== 'MySQL')];
    },
    sourceTypeCardList() {
      const sourceTypeMap = new Map();

      this.devopsScmList.forEach((item) => {
        const sourceType = item?.scmType || 'Gitee';
        const key = this.normalizeSourceType(sourceType);
        if (sourceTypeMap.has(key)) {
          return;
        }

        sourceTypeMap.set(key, {
          value: sourceType,
          label: this.formatSourceTypeLabel(sourceType) || item?.scmTypeI18n,
          iconResource: item?.iconResource || ''
        });
      });

      [
        { value: 'Gitee', label: 'Gitee' },
        { value: 'Gitlab', label: 'GitLab' }
      ].forEach((sourceType) => {
        const key = this.normalizeSourceType(sourceType.value);
        if (!sourceTypeMap.has(key)) {
          sourceTypeMap.set(key, {
            ...sourceType,
            iconResource: ''
          });
        }
      });

      if (this.createFlowMode) {
        sourceTypeMap.set('built_in', {
          value: 'BUILT_IN',
          label: this.$t('nei-zhi-bian-geng-liu'),
          iconResource: ''
        });
      }

      const sourceTypeOrder = { built_in: 0, gitee: 1, gitlab: 2 };
      return [...sourceTypeMap.values()].sort((left, right) => {
        const leftType = this.normalizeSourceType(left.value);
        const rightType = this.normalizeSourceType(right.value);
        const leftOrder = sourceTypeOrder[leftType] ?? Number.MAX_SAFE_INTEGER;
        const rightOrder = sourceTypeOrder[rightType] ?? Number.MAX_SAFE_INTEGER;
        if (leftOrder !== rightOrder) {
          return leftOrder - rightOrder;
        }
        return left.label.localeCompare(right.label);
      });
    },
    filteredDevopsScmList() {
      const selectedType = this.normalizeSourceType(this.sourceScmType);
      return this.devopsScmList.filter((item) => this.normalizeSourceType(item?.scmType) === selectedType);
    },
    isBuiltIn() {
      return this.sourceScmType === 'BUILT_IN';
    },
    selectedParentFlow() {
      return this.parentFlowList.find((flow) => String(flow.flowId) === String(this.flowGitOpsForm.parentFlowId)) || null;
    },
    selectedParentFlowName() {
      if (this.selectedParentFlow?.flowName) {
        return this.selectedParentFlow.flowName;
      }
      if (this.isBuiltIn) {
        return this.$t('wu-shang-ji-bian-geng-liu-shou-dong-ti-gong-sql');
      }
      return '';
    },
    filteredDevopsInsList() {
      if (!this.flowGitOpsForm.databaseType) {
        return this.devopsInsList;
      }
      return this.devopsInsList.filter((item) => item?.objAttr?.dsType === this.flowGitOpsForm.databaseType);
    },
    selectedManagerName() {
      const user = this.devopsUsers.find((item) => item.userUid === this.flowBasicForm.flowManagerUid);
      return user?.userName || '';
    },
    selectedScmName() {
      return this.devopsScmSelected?.scmDisplay || '';
    },
    selectedInstanceName() {
      return this.devopsInsSelected?.objName || '';
    },
    selectedInitLabel() {
      return this.initOptions.find((item) => item.value === this.flowGitOpsForm.initScript)?.label || '';
    },
    isImDisabled() {
      return this.flowImForm.imType === 'none';
    },
    subscriptionItems() {
      return [
        { key: 'eventChangeFlowStatus', label: this.$t('xiang-mu-zhuang-tai-de-bian-hua') },
        { key: 'eventFlowConfig', label: this.$t('xiang-mu-pei-zhi-gai-bian') },
        { key: 'eventChangeLife', label: this.$t('bian-geng-jie-duan-bian-hua') },
        { key: 'eventChangeNotice', label: this.$t('bian-geng-zhuang-tai-xiao-xi') }
      ];
    },
    subscriptionCount() {
      return this.subscriptionItems.filter((item) => this.flowImForm[item.key]).length;
    },
    subscriptionSummary() {
      if (this.isImDisabled) {
        return this.$t('jin-yong');
      }
      return this.$t('yi-xiang-ding-yue', { count: this.subscriptionCount });
    },
    summaryImChannel() {
      return this.isImDisabled ? this.$t('jin-yong') : this.imDefSelected.imTypeI18n;
    },
    selectedImProviderName() {
      const provider = this.imProviderList.find((item) => item.imId === this.flowImForm.imId);
      return provider?.display || '';
    },
    schemaSelectDisabled() {
      if (!this.flowGitOpsForm.devopsInsHasSchema || !this.devopsInsSelected) {
        return true;
      }
      return this.flowGitOpsForm.devopsInsHasCatalog && !this.flowGitOpsForm.catalogName;
    }
  },
  watch: {
    '$route.fullPath': {
      handler() {
        this.initPage();
      },
      immediate: true
    }
  },
  beforeUnmount() {
    this.cleanupReleaseFlowDropdown();
  },
  methods: {
    getRepoSelectionKey,
    groupByRepoNamespace,
    async initPage() {
      this.resetState();
      this.loading = true;
      try {
        if (!this.createFlowMode) {
          this.flowId = this.$route.params.id;
          await this.fetchFlowDetail();
        }

        await Promise.all([
          this.fetchDevopsScmList(),
          this.fetchInsList(),
          this.createFlowMode ? this.fetchDevopsUsers() : Promise.resolve(),
          this.createFlowMode ? this.fetchParentFlows() : Promise.resolve()
        ]);
        if (this.createFlowMode) {
          this.flowBasicForm.flowManagerUid = this.userInfo?.uid || '';
          await this.fetchImDefList();
        }
      } finally {
        this.loading = false;
      }
    },
    resetState() {
      this.submitting = false;
      this.creationMode = this.$route.query.mode === 'batch' ? 'batch' : 'single';
      this.batchCreateResult = null;
      this.currentStep = 1;
      this.createResult = null;
      this.flowId = '';
      this.flowInfo = null;
      this.webhook = {
        password: '',
        url: '',
        webHookHelpUrl: '',
        repoUrl: ''
      };
      this.flowBasicForm = getDefaultFlowInfo();
      this.flowGitOpsForm = getDefaultGitOpsInfo();
      this.sourceScmType = 'Gitee';
      if (this.createFlowMode) {
        this.sourceScmType = 'BUILT_IN';
        this.flowGitOpsForm.initScript = 'None';
      }
      this.parentFlowList = [];
      this.parentFlowLoading = false;
      this.flowImForm = {
        imId: null,
        imType: 'none',
        language: 'zh',
        eventChangeFlowStatus: false,
        eventFlowConfig: false,
        eventChangeLife: false,
        eventChangeNotice: false
      };
      this.devopsScmSelected = null;
      this.devopsRepoList = [];
      this.devopsRepoListByGroup = {};
      this.devopsRepoSelected = null;
      this.devopsInsSelected = null;
      this.devopsInsCatalogList = [];
      this.devopsInsSchemaList = [];
      this.imProviderList = [];
      this.imDefSelected = this.buildDisabledImDef();
      this.devopsTo = this.flowGitOpsForm.databaseType || 'icon-v2-shili';
    },
    buildDisabledImDef() {
      return {
        helpUrl: '',
        imType: 'none',
        imTypeI18n: this.$t('jin-yong')
      };
    },
    async fetchFlowDetail() {
      const res = await this.$services.dmCicdFlowDetail({
        data: {
          flowId: this.flowId
        }
      });
      if (res.success) {
        this.flowInfo = res.data;
      } else {
        this.$Message.error(this.$t('bu-cun-zai-de-xiang-mu'));
        this.goBack();
      }
    },
    async fetchDevopsUsers() {
      const res = await this.$services.dmCicdDevopsUsers({
        data: { search: '' }
      });
      this.devopsUsers = res.success ? res.data || [] : [];
    },
    async fetchDevopsScmList() {
      const res = await this.$services.dmCicdDevopsScmList();
      if (res.success) {
        this.devopsScmList = res.data || [];
        this.syncSourceTypeWithProvider();
      }
    },
    async fetchParentFlows() {
      this.parentFlowLoading = true;
      try {
        const res = await this.$services.dmCicdFlowParentCandidates({ data: {} });
        this.parentFlowList = res.success ? (res.data || []).filter((flow) => flow.flowType === 'BUILT_IN') : [];
      } finally {
        this.parentFlowLoading = false;
      }
    },
    normalizeSourceType(sourceType) {
      return String(sourceType || '')
        .replace(/^icon-v2-/i, '')
        .toLowerCase();
    },
    formatSourceTypeLabel(sourceType) {
      const label = String(sourceType || '').replace(/^icon-v2-/i, '');
      return getScmDisplayName(label) || 'Gitee';
    },
    syncSourceTypeWithProvider() {
      const selectedProvider = this.devopsScmList.find((scm) => String(scm.scmId) === String(this.flowGitOpsForm.repoScmId));
      if (selectedProvider?.scmType) {
        this.sourceScmType = selectedProvider.scmType;
      }
    },
    handleSourceTypeSelect(sourceType) {
      if (this.sourceScmType === sourceType) {
        return;
      }

      this.sourceScmType = sourceType;
      this.flowGitOpsForm.repoScmId = '';
      this.handleDevopsScmSelected('');
      this.flowGitOpsForm.parentFlowId = null;
      if (sourceType === 'BUILT_IN') {
        this.flowGitOpsForm.initScript = 'None';
      } else if (this.createFlowMode) {
        this.flowGitOpsForm.initScript = 'Snapshot';
      }
    },
    handleParentFlowSelected(parentFlowId) {
      this.flowGitOpsForm.parentFlowId = parentFlowId || null;
      this.$nextTick(() => {
        this.$refs.pipelineConfig?.clearSourceValidate?.('parentFlowId');
      });
      const parent = this.parentFlowList.find((flow) => String(flow.flowId) === String(parentFlowId));
      if (!parent?.dsType) {
        return;
      }
      this.flowGitOpsForm.databaseType = parent.dsType;
      this.handleDatabaseTypeChange();
    },
    async handleDevopsScmSelected(repoScmId) {
      const nextRepoScmId = this.hasFormValue(repoScmId) && typeof repoScmId !== 'object' ? repoScmId : this.flowGitOpsForm.repoScmId;
      this.flowGitOpsForm.repoScmId = nextRepoScmId;
      this.devopsScmSelected = this.devopsScmList.find((scm) => String(scm.scmId) === String(this.flowGitOpsForm.repoScmId)) || null;
      this.syncSourceTypeWithProvider();
      this.$nextTick(() => {
        this.$refs.pipelineConfig?.clearSourceValidate?.('repoScmId');
      });

      this.flowGitOpsForm.repoName = '';
      this.flowGitOpsForm.repoSelectionKey = '';
      this.flowGitOpsForm.repoId = '';
      this.flowGitOpsForm.repoPath = '';
      this.flowGitOpsForm.repoScmUrl = '';
      this.flowGitOpsForm.repoSpace = '';
      this.flowGitOpsForm.repoBranch = '';
      this.devopsRepoSelected = null;
      this.devopsRepoList = [];
      this.devopsRepoListByGroup = {};

      if (this.hasFormValue(this.flowGitOpsForm.repoScmId)) {
        await this.fetchDevopsScmRepos();
      }
    },
    async fetchDevopsScmRepos() {
      this.repoLoading = true;
      try {
        const res = await this.$services.dmCicdDevopsRepos({
          data: {
            scmId: this.flowGitOpsForm.repoScmId
          }
        });

        if (res.success) {
          this.devopsRepoList = res.data || [];
          this.devopsRepoListByGroup = this.groupByRepoNamespace(this.devopsRepoList);
        }
      } finally {
        this.repoLoading = false;
      }
    },
    handleDevopsRepoSelected() {
      this.devopsRepoSelected = this.devopsRepoList.find((repo) => this.getRepoSelectionKey(repo) === this.flowGitOpsForm.repoSelectionKey) || null;
      if (this.devopsRepoSelected) {
        this.flowGitOpsForm.repoName = this.devopsRepoSelected.repoName;
        this.flowGitOpsForm.repoId = this.devopsRepoSelected.repoId;
        this.flowGitOpsForm.repoPath = this.devopsRepoSelected.repoPath;
        this.flowGitOpsForm.repoScmUrl = this.devopsRepoSelected.repoUrl;
        this.flowGitOpsForm.repoBranch = this.devopsRepoSelected.repoBranch;
        this.flowGitOpsForm.repoSpace = this.devopsRepoSelected.repoSpace;
      }
    },
    handleDevopsJumpToRepo(url) {
      if (url) {
        window.open(url, '_blank');
      }
    },
    async fetchInsList() {
      const res = await this.$services.dmCicdDevopsDsInsLevels();
      if (res.success) {
        this.devopsInsList = res.data || [];
      }
    },
    handleDatabaseTypeChange() {
      this.flowGitOpsForm.instanceId = '';
      this.flowGitOpsForm.catalogName = '';
      this.flowGitOpsForm.schemaName = '';
      this.flowGitOpsForm.devopsInsHasCatalog = false;
      this.flowGitOpsForm.devopsInsHasSchema = false;
      this.devopsInsSelected = null;
      this.devopsInsCatalogList = [];
      this.devopsInsSchemaList = [];
      this.devopsTo = this.flowGitOpsForm.databaseType || 'icon-v2-shili';
    },
    handleDatabaseTypeSelect(type) {
      if (this.flowGitOpsForm.databaseType === type) {
        return;
      }
      this.flowGitOpsForm.databaseType = type;
      this.handleDatabaseTypeChange();
    },
    async handleDevopsChangeIns() {
      this.devopsInsCatalogList = [];
      this.devopsInsSchemaList = [];
      this.flowGitOpsForm.catalogName = '';
      this.flowGitOpsForm.schemaName = '';
      this.flowGitOpsForm.devopsInsHasCatalog = false;
      this.flowGitOpsForm.devopsInsHasSchema = false;

      if (!this.flowGitOpsForm.instanceId) {
        return;
      }

      this.devopsInsSelected = this.devopsInsList.find((ins) => ins.objId === this.flowGitOpsForm.instanceId) || null;
      if (!this.devopsInsSelected) {
        return;
      }

      this.flowGitOpsForm.databaseType = this.devopsInsSelected?.objAttr?.dsType || this.flowGitOpsForm.databaseType;
      this.devopsTo = this.flowGitOpsForm.databaseType || 'icon-v2-shili';

      const dsConf = this.dmGlobalSetting.dsSettingDef?.[this.devopsInsSelected.objAttr.dsType];
      const dsLevels = dsConf?.categories?.levels || [];
      this.flowGitOpsForm.devopsInsHasCatalog = dsLevels.includes('CATALOG');
      this.flowGitOpsForm.devopsInsHasSchema = dsLevels.includes('SCHEMA');

      if (this.flowGitOpsForm.devopsInsHasCatalog) {
        await this.fetchCatalogList();
      } else if (this.flowGitOpsForm.devopsInsHasSchema) {
        await this.fetchSchemaList();
      }
    },
    async handleChangeCatalog() {
      this.flowGitOpsForm.schemaName = '';
      await this.fetchSchemaList();
    },
    async handleRefreshSchemaList() {
      if (this.schemaSelectDisabled || this.schemaLoading) {
        return;
      }
      await this.fetchSchemaList(true);
    },
    async fetchCatalogList(isRefreshCache = false) {
      if (!this.devopsInsSelected?.objAttr?.dsEnvId) {
        return;
      }

      const res = await this.$services.dmCicdDevopsDsDbLevels({
        data: {
          levels: [this.devopsInsSelected.objAttr.dsEnvId, this.flowGitOpsForm.instanceId].filter(Boolean),
          refreshCache: isRefreshCache
        }
      });

      if (res.success) {
        this.devopsInsCatalogList = res.data || [];
      }
    },
    async fetchSchemaList(isRefreshCache = false) {
      if (!this.devopsInsSelected?.objAttr?.dsEnvId) {
        return;
      }

      const levels = [this.devopsInsSelected.objAttr.dsEnvId, this.flowGitOpsForm.instanceId];
      if (this.flowGitOpsForm.devopsInsHasCatalog && this.flowGitOpsForm.devopsInsHasSchema) {
        if (!this.flowGitOpsForm.catalogName) {
          return;
        }
        levels.push(this.flowGitOpsForm.catalogName);
      }

      this.schemaLoading = true;
      try {
        const res = await this.$services.dmCicdDevopsDsDbLevels({
          data: {
            levels: levels.filter(Boolean),
            refreshCache: isRefreshCache
          }
        });

        if (res.success) {
          this.devopsInsSchemaList = res.data || [];
        }
      } finally {
        this.schemaLoading = false;
      }
    },
    fetchFormDsLevels() {
      if (!this.devopsInsSelected?.objAttr?.dsEnvId) {
        return [this.flowGitOpsForm.instanceId].filter(Boolean);
      }

      const levels = [this.devopsInsSelected.objAttr.dsEnvId, this.flowGitOpsForm.instanceId];
      if (this.flowGitOpsForm.devopsInsHasCatalog) {
        levels.push(this.flowGitOpsForm.catalogName);
      }
      if (this.flowGitOpsForm.devopsInsHasSchema) {
        levels.push(this.flowGitOpsForm.schemaName);
      }
      return levels.filter(Boolean);
    },
    hasFormValue(value) {
      return value !== undefined && value !== null && value !== '';
    },
    getPromptMessage(prefixKey, labelKey) {
      const prefix = this.$t(prefixKey);
      const separator = /[\u4e00-\u9fa5]/.test(prefix) ? '' : ' ';
      return `${prefix}${separator}${this.$t(labelKey)}`;
    },
    getInputRequiredMessage(labelKey) {
      return this.getPromptMessage('qing-shu-ru', labelKey);
    },
    getSelectRequiredMessage(labelKey) {
      return this.getPromptMessage('qing-xuan-ze', labelKey);
    },
    validateRepoScm(rule, value, callback) {
      if (this.isBuiltIn) {
        callback();
        return;
      }
      if (!this.hasFormValue(value)) {
        callback(new Error(this.getSelectRequiredMessage('nav-git-ops')));
        return;
      }
      callback();
    },
    validateCatalog(rule, value, callback) {
      if (this.flowGitOpsForm.devopsInsHasCatalog && !value) {
        callback(new Error(this.$t('qing-xuan-ze-shu-ju-ku')));
        return;
      }
      callback();
    },
    validateSchema(rule, value, callback) {
      if (this.flowGitOpsForm.devopsInsHasSchema && !value) {
        callback(new Error(this.$t('qing-xuan-ze-schema')));
        return;
      }
      callback();
    },
    async fetchImDefList() {
      const disabled = this.buildDisabledImDef();
      const res = await this.$services.dmDevopsImDefList();
      this.imDefList = res.success ? [disabled, ...(res.data || [])] : [disabled];
      this.imDefSelected = disabled;
    },
    async handleImDefOne(imDef) {
      this.imDefSelected = imDef;
      if (imDef.imType === 'none') {
        this.imProviderList = [];
        this.flowImForm.imId = null;
        this.flowImForm.imType = 'none';
        this.resetFlowImSubscriptions();
        return;
      }

      const res = await this.$services.dmDevopsImList({
        data: {
          imType: imDef.imType
        }
      });
      this.imProviderList = res.success ? res.data || [] : [];

      this.flowImForm.imId = null;
      this.flowImForm.imType = imDef.imType;
    },
    resetFlowImSubscriptions() {
      this.subscriptionItems.forEach((item) => {
        this.flowImForm[item.key] = false;
      });
    },
    handleImProviderSelected(imId) {
      this.flowImForm.imId = imId;
      this.flowImForm.imType = this.imDefSelected.imType;
    },
    fetchFlowGitOpsDescription(option) {
      try {
        return GITOPS_DESCRIPTION[option] || '';
      } catch (e) {
        return '';
      }
    },
    async handleSubmit() {
      if (this.batchCreateMode) {
        if (this.currentStepKey === 'basic') {
          const valid = this.$refs.batchOrchestration?.validate?.();
          if (!valid) {
            return;
          }
          this.currentStep += 1;
          this.scrollToStepTop();
          return;
        }

        const valid = await this.validateCurrentStep();
        if (!valid) {
          return;
        }
        this.submitting = true;
        try {
          await this.$refs.batchOrchestration?.submit?.({
            option: this.buildFlowOptionPayload(),
            messenger: this.buildMessengerPayload()
          });
        } finally {
          this.submitting = false;
        }
        return;
      }

      const valid = await this.validateCurrentStep();
      if (!valid) {
        return;
      }

      if (!this.isLastStep) {
        this.currentStep += 1;
        this.scrollToStepTop();
        return;
      }

      if (this.createFlowMode && !this.isImDisabled && !this.flowImForm.imId) {
        this.$Message.error(this.$t('qing-xuan-ze-yi-ge-im-ti-gong-zhe'));
        return;
      }

      this.submitting = true;
      try {
        if (this.createFlowMode) {
          await this.createFlow();
        } else {
          await this.createReleaseFlow();
        }
      } finally {
        this.submitting = false;
      }
    },
    handlePreviousStep() {
      if (this.currentStep <= 1) {
        return;
      }
      this.currentStep -= 1;
      this.scrollToStepTop();
    },
    handleBatchCreated(result) {
      this.batchCreateResult = result || null;
      this.createResult = result ? { ...result, batch: true } : null;
      this.flowId = result?.rootFlowId || '';
      if (result) {
        this.$Message.success(this.$t('cicd-batch-create-success', { count: result.flowCount || 0 }));
      }
    },
    scrollToStepTop() {
      this.$nextTick(() => {
        const pageEl = this.$el;
        if (!pageEl || !pageEl.classList?.contains('release-flow-page')) {
          return;
        }

        const resetScroll = () => {
          pageEl.scrollTop = 0;
          pageEl.scrollLeft = 0;
        };

        resetScroll();
        window.requestAnimationFrame(resetScroll);
      });
    },
    resolveSelectRef(selectRef) {
      return Array.isArray(selectRef) ? selectRef[0] : selectRef;
    },
    cleanupReleaseFlowDropdown() {
      if (this.releaseFlowDropdownCleanup) {
        this.releaseFlowDropdownCleanup();
        this.releaseFlowDropdownCleanup = null;
      }
    },
    handleSelectDropdownOpen(opened, selectRef) {
      this.cleanupReleaseFlowDropdown();

      if (!opened) {
        return;
      }

      this.$nextTick(() => {
        const select = this.resolveSelectRef(selectRef);
        if (!select) {
          return;
        }

        const syncDropdown = () => {
          const dropdown = select.$refs?.dropdown;
          const drop = dropdown?.$refs?.drop;
          const popper = dropdown?.popper;

          if (!dropdown || !drop) {
            return;
          }

          drop.setAttribute('x-placement', 'bottom-start');
          drop.style.transformOrigin = 'center top';

          if (popper?.options) {
            popper.options.placement = 'bottom-start';
            popper.options.modifiers = popper.options.modifiers || {};
            popper.options.modifiers.flip = {
              ...(popper.options.modifiers.flip || {}),
              enabled: false
            };
          }

          if (Array.isArray(popper?.modifiers)) {
            const flipModifier = popper.modifiers.find((modifier) => modifier.name === 'flip');
            if (flipModifier) {
              flipModifier.enabled = false;
            }
          }

          dropdown?.update?.();
          popper?.update?.();

          const reference = select.$refs?.reference || select.$el;
          const referenceRect = reference?.getBoundingClientRect?.();
          if (!referenceRect) {
            return;
          }

          const viewportHeight = window.innerHeight || document.documentElement.clientHeight;
          const viewportWidth = window.innerWidth || document.documentElement.clientWidth;
          const dropdownGap = 4;
          const bottomSafeGap = 24;
          const horizontalSafeGap = 12;
          const dropdownTop = Math.round(referenceRect.bottom + dropdownGap);
          const dropdownWidth = Math.round(referenceRect.width);
          const dropdownLeft = Math.round(
            Math.min(Math.max(horizontalSafeGap, referenceRect.left), Math.max(horizontalSafeGap, viewportWidth - dropdownWidth - horizontalSafeGap))
          );
          const availableBelow = Math.max(32, Math.floor(viewportHeight - dropdownTop - bottomSafeGap));
          const dropdownMaxHeight = Math.min(320, availableBelow);
          const dropdownList = drop.querySelector('.ivu-select-dropdown-list');

          drop.style.position = 'fixed';
          drop.style.top = `${dropdownTop}px`;
          drop.style.left = `${dropdownLeft}px`;
          drop.style.right = 'auto';
          drop.style.bottom = 'auto';
          drop.style.width = `${dropdownWidth}px`;
          drop.style.maxHeight = `${dropdownMaxHeight}px`;
          drop.style.overflow = 'hidden';
          drop.style.transform = 'none';
          drop.style.willChange = 'auto';
          drop.style.setProperty('--release-flow-dropdown-max-height', `${dropdownMaxHeight}px`);

          if (dropdownList) {
            dropdownList.style.maxHeight = `${dropdownMaxHeight}px`;
            dropdownList.style.overflowY = 'auto';
          }
        };

        syncDropdown();
        window.requestAnimationFrame?.(syncDropdown);
        window.setTimeout(syncDropdown, 80);
        window.setTimeout(syncDropdown, 220);

        const handleResize = () => syncDropdown();
        window.addEventListener('resize', handleResize, { passive: true });
        this.releaseFlowDropdownCleanup = () => {
          window.removeEventListener('resize', handleResize);
        };
      });
    },
    async validateCurrentStep() {
      if (this.currentStepKey === 'config') {
        if (!this.isImDisabled && !this.flowImForm.imId) {
          this.$Message.error(this.$t('qing-xuan-ze-yi-ge-im-ti-gong-zhe'));
          return false;
        }
        return true;
      }

      const checks = [];
      if (this.createFlowMode) {
        checks.push(this.$refs.basicInfo.validate());
      }
      checks.push(this.$refs.pipelineConfig.validate());
      const result = await Promise.all(checks);
      return result.every(Boolean);
    },
    buildPipelinePayload() {
      if (this.isBuiltIn) {
        return {
          repoScmId: 0,
          dsLevels: this.fetchFormDsLevels()
        };
      }
      return {
        repoScmId: this.flowGitOpsForm.repoScmId,
        repoScmUrl: this.flowGitOpsForm.repoScmUrl,
        repoId: this.flowGitOpsForm.repoId,
        repoPath: this.flowGitOpsForm.repoPath,
        repoSpace: this.flowGitOpsForm.repoSpace,
        repoName: this.flowGitOpsForm.repoName,
        repoBranch: this.flowGitOpsForm.repoBranch,
        repoScriptPath: this.flowGitOpsForm.repoScriptPath,
        eventType: this.flowGitOpsForm.eventType,
        dsLevels: this.fetchFormDsLevels()
      };
    },
    buildFlowOptionPayload() {
      return {
        initScript: this.flowGitOpsForm.initScript
      };
    },
    buildMessengerPayload() {
      if (this.isImDisabled) {
        return null;
      }

      return {
        imId: this.flowImForm.imId,
        language: this.flowImForm.language,
        eventChangeFlowStatus: this.flowImForm.eventChangeFlowStatus,
        eventFlowConfig: this.flowImForm.eventFlowConfig,
        eventChangeLife: this.flowImForm.eventChangeLife,
        eventChangeNotice: this.flowImForm.eventChangeNotice
      };
    },
    async createFlow() {
      const res = await this.$services.dmCicdCreate({
        data: {
          flowName: this.flowBasicForm.flowName,
          flowDesc: this.flowBasicForm.flowDesc,
          flowManagerUid: this.flowBasicForm.flowManagerUid,
          flowType: this.isBuiltIn ? 'BUILT_IN' : 'SCM',
          parentFlowId: this.isBuiltIn ? this.flowGitOpsForm.parentFlowId : null,
          option: this.buildFlowOptionPayload(),
          pipeline: this.buildPipelinePayload(),
          messenger: this.buildMessengerPayload()
        }
      });

      if (res.success) {
        this.$Message.success(this.$t('xiang-mu-chuang-jian-cheng-gong'));
        (res.data?.warnings || []).forEach((warning) => this.$Message.warning(warning));
        this.createResult = res.data || {};
        this.flowId = res.data?.flowId || '';
        this.webhook = {
          url: res.data?.webHookUrl || '',
          repoUrl: res.data?.repoUrl || '',
          password: res.data?.webHookPwd || '',
          webHookHelpUrl: res.data?.webHookHelpUrl || ''
        };
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async createReleaseFlow() {
      const res = await this.$services.dmCicdFlowDevopsCreate({
        data: {
          flowId: this.flowId,
          eventType: this.flowGitOpsForm.eventType,
          pipeline: this.buildPipelinePayload(),
          option: {
            initScript: this.flowGitOpsForm.initScript
          }
        }
      });

      if (res.success) {
        (res.data?.warnings || []).forEach((warning) => this.$Message.warning(warning));
        this.$Message.success(this.$t('fa-bu-liu-pei-zhi-cheng-gong'));
        this.$router.push(`/cicd/${this.flowId}`);
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    goBack() {
      if (this.createFlowMode) {
        this.$router.push('/cicd');
        return;
      }
      this.$router.push(`/cicd/${this.flowId || this.$route.params.id}`);
    },
    goCreatedFlow() {
      if (this.flowId) {
        this.$router.push(`/cicd/${this.flowId}`);
      } else {
        this.$router.push('/cicd');
      }
    },
    openHelp() {
      if (this.webhook.webHookHelpUrl) {
        window.open(this.webhook.webHookHelpUrl, '_blank');
        return;
      }
      window.open('https://www.clougence.com/dm-doc/devops/devops_guild', '_blank');
    },
    jumpToWebhookDoc() {
      this.openHelp();
    },
    goToAddScm() {
      this.$router.push('/integrations/git');
    },
    goToDsSetting() {
      this.$router.push('/datasource');
    },
    goToAddIm() {
      this.$router.push('/integrations/im');
    },
    handleCopyTemp(item) {
      handleCopy(item);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleJumpUrl(item) {
      if (item) {
        window.open(item, '_blank');
      }
    }
  }
};
</script>

<style>
.release-flow-page {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  width: 100%;
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  overflow: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  background: #fff;
  color: #1f2937;
}

.release-flow-page::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

.release-flow-shell {
  display: grid;
  flex: 1 1 auto;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 24px;
  min-height: 0;
  margin: 0;
  overflow-y: auto;
  padding: 20px 24px;
  align-items: stretch;
}

.release-flow-main {
  grid-column: 1;
  display: flex;
  flex-direction: column;
  gap: 32px;
  min-width: 0;
}

.page-section {
  min-width: 0;
}

.page-section__title {
  position: relative;
  margin-bottom: 16px;
  padding-left: 12px;
  color: #181d26;
  font-size: 16px;
  font-weight: 500;
  line-height: 1.4;
}

.page-section__title::before {
  position: absolute;
  top: 50%;
  left: 0;
  width: 3px;
  height: 16px;
  border-radius: 2px;
  background: #18b566;
  transform: translateY(-50%);
  content: '';
}

.panel-subheading {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  color: #181d26;
  font-size: 14px;
  font-weight: 500;
  line-height: 1.4;
}

.basic-info-section {
  min-height: 0;
}

.basic-form {
  display: grid;
  width: 100%;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  column-gap: 48px;
  justify-items: stretch;
  row-gap: 18px;
}

.basic-form .ivu-form-item {
  width: 100%;
  min-width: 0;
  margin-bottom: 0;
}

.basic-form .ivu-form-item-content {
  width: 100%;
  min-width: 0;
}

.basic-form .ivu-input-wrapper,
.basic-form .ant-input-affix-wrapper,
.basic-form .dm-input,
.basic-form .ivu-select {
  display: block;
  width: 100%;
  box-sizing: border-box;
}

.basic-form .ivu-form-item-error-tip {
  position: absolute;
  top: auto;
  bottom: 0;
  left: 0;
  padding-top: 0;
  line-height: 18px;
}

.release-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 64px minmax(0, 1fr);
  align-items: stretch;
  gap: 24px;
}

.release-panel {
  min-width: 0;
}

.basic-info-section .ivu-form-item-label,
.release-config-section .ivu-form-item-label {
  color: #5f6c80;
  font-size: 16px;
  font-weight: 500;
}

.basic-info-section .ivu-form-item-label {
  padding: 0 0 14px;
}

.basic-info-section .ivu-form-item-content,
.release-config-section .ivu-form-item-content {
  position: relative;
  padding-bottom: 20px;
}

.release-panel .ivu-form-item-content {
  padding-right: 30px;
}

.release-config-section .ivu-form-item {
  margin-bottom: 0;
}

.release-config-section .ivu-form-item-label {
  position: relative;
  padding: 12px 12px 12px 14px;
}

.release-config-section .force-required .ivu-form-item-label::before {
  color: #ed4014;
  content: '*';
}

.release-config-section .ivu-form-item-required .ivu-form-item-label::before,
.release-config-section .force-required .ivu-form-item-label::before {
  position: absolute;
  top: 50%;
  left: 0;
  width: 10px;
  margin-right: 0;
  line-height: 1;
  text-align: left;
  transform: translateY(-50%);
}

.release-config-section .ivu-form-item-error-tip {
  position: absolute;
  top: auto;
  bottom: 0;
  left: 0;
  padding-top: 0;
  line-height: 18px;
}

.basic-info-section .ivu-input,
.release-config-section .ivu-input {
  height: 42px;
  border-color: #dce3eb;
  border-radius: 6px;
  color: #1f2937;
  font-size: 15px;
}

.basic-info-section .ant-input,
.basic-info-section .ant-input-affix-wrapper {
  height: 42px;
  border-color: #dce3eb !important;
  border-radius: 6px !important;
  color: #1f2937;
  font-size: 15px;
}

.basic-info-section .ant-input {
  padding: 0 14px;
  line-height: 40px;
}

.basic-info-section .ant-input-affix-wrapper {
  display: flex;
  align-items: center;
  padding: 0 14px;
}

.basic-info-section .ant-input-affix-wrapper .ant-input {
  height: 40px;
  padding: 0;
  border: 0 !important;
  box-shadow: none !important;
}

.basic-info-section .ivu-select-selection,
.release-config-section .ivu-select-selection {
  min-height: 42px;
  border-color: #dce3eb;
  border-radius: 6px;
}

.release-config-section .ivu-select.ivu-select-disabled .ivu-select-selection,
.release-config-section .ivu-select-disabled .ivu-select-selection {
  background: #f7f8fa !important;
  background-color: #f7f8fa !important;
  border-color: #dce3eb !important;
  box-shadow: none !important;
  cursor: not-allowed;
  opacity: 1;
}

.release-config-section .ivu-select.ivu-select-disabled .ivu-select-selection:hover,
.release-config-section .ivu-select-disabled .ivu-select-selection:hover {
  background: #f7f8fa !important;
  background-color: #f7f8fa !important;
  border-color: #dce3eb !important;
}

.basic-info-section .ivu-select-placeholder,
.basic-info-section .ivu-select-selected-value,
.basic-info-section .ivu-select-input,
.release-config-section .ivu-select-placeholder,
.release-config-section .ivu-select-selected-value,
.release-config-section .ivu-select-input {
  height: 40px;
  line-height: 40px;
  font-size: 15px;
}

.release-config-section .gitops-select-form-item .ivu-select-single .ivu-select-selection {
  display: flex;
  align-items: center;
}

.release-config-section .gitops-select-form-item .ivu-select-single .ivu-select-placeholder,
.release-config-section .gitops-select-form-item .ivu-select-single .ivu-select-selected-value {
  float: none;
  flex: 1 1 auto;
  min-width: 0;
  height: auto;
  line-height: 1.2;
}

.release-config-section .ivu-select-disabled .ivu-select-placeholder,
.release-config-section .ivu-select-disabled .ivu-select-selected-value,
.release-config-section .ivu-select-disabled .ivu-select-input,
.release-config-section .ivu-select-disabled .ivu-select-arrow {
  color: #8b98aa !important;
  -webkit-text-fill-color: #8b98aa;
  cursor: not-allowed;
}

.release-config-section .ivu-select-disabled .ivu-select-input {
  background-color: transparent !important;
}

.target-heading {
  color: #172033;
}

.type-card-group {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(112px, 118px));
  gap: 8px;
  justify-content: start;
  width: 100%;
}

.type-card {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 0;
  min-height: 38px;
  padding: 0 10px;
  border: 1px solid #dce3eb;
  border-radius: 8px;
  background: #fff;
  color: #516074;
  font-size: 13px;
  font-weight: 400;
  line-height: 1;
  cursor: pointer;
  gap: 6px;
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease,
    color 0.2s ease,
    transform 0.2s ease;
}

.type-card:hover {
  border-color: #9adfbb;
  color: #0f9f55;
}

.type-card:active {
  transform: translateY(1px);
}

.type-card.active {
  border-color: #18b566;
  background: #eefbf4;
  color: #0f9f55;
  box-shadow: inset 0 0 0 1px #18b566;
}

.type-card span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.inline-control {
  display: flex;
  align-items: center;
  gap: 8px;
}

.inline-control .ivu-select,
.inline-control .ivu-input-wrapper {
  flex: 1;
  min-width: 0;
}

.inline-refresh-slot {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 24px;
  width: 24px;
  height: 24px;
  padding: 0;
  border: 1px solid transparent;
  border-radius: 50%;
  background: transparent;
  color: #64748b;
  cursor: pointer;
  pointer-events: auto;
  position: relative;
  z-index: 1;
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease,
    color 0.2s ease;
}

.schema-form-item .inline-control {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 24px;
  align-items: center;
  column-gap: 12px;
  width: calc(100% + 36px);
}

.schema-form-item .inline-control .ivu-select {
  display: block;
  width: 100%;
}

.inline-refresh-slot:hover,
.inline-refresh-slot:focus,
.inline-refresh-slot:active,
.repo-refresh-action:hover,
.repo-refresh-action:focus,
.repo-refresh-action:active {
  border-color: transparent;
  background: transparent;
  color: #0f9f55;
}

.inline-refresh-slot .data-source-icon,
.repo-refresh-action .data-source-icon {
  pointer-events: none;
}

.inline-refresh-slot:disabled {
  border-color: transparent;
  background: transparent;
  color: #93a1b3;
  cursor: not-allowed;
}

.repo-control {
  position: relative;
}

.repo-control .ivu-select {
  flex-basis: 100%;
}

.repo-refresh-action {
  position: absolute;
  top: 50%;
  left: calc(100% + 14px);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  padding: 0;
  border: 1px solid transparent;
  border-radius: 50%;
  background: transparent;
  color: #64748b;
  cursor: pointer;
  pointer-events: auto;
  z-index: 1;
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease,
    color 0.2s ease;
  transform: translateY(-50%);
}

.repo-refresh-action:disabled {
  border-color: transparent;
  background: transparent;
  color: #93a1b3;
  cursor: not-allowed;
}

.inline-refresh-slot:disabled:hover,
.inline-refresh-slot:disabled:focus,
.inline-refresh-slot:disabled:active,
.repo-refresh-action:disabled:hover,
.repo-refresh-action:disabled:focus,
.repo-refresh-action:disabled:active {
  border-color: transparent;
  background: transparent;
  color: #93a1b3;
}

.schema-form-item .inline-refresh-slot {
  position: relative;
}

.repo-link {
  float: right;
  color: #64748b;
}

.field-hint {
  margin-top: 6px;
  color: #7b8494;
  font-size: 13px;
  line-height: 1.6;
}

.link-divider {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.link-divider::before {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  border-left: 1px dashed #d5e0eb;
  content: '';
  transform: translateX(-50%);
}

.link-divider span {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 74px;
  width: 74px;
  min-width: 74px;
  max-width: 74px;
  height: 74px;
  min-height: 74px;
  max-height: 74px;
  box-sizing: border-box;
  aspect-ratio: 1 / 1;
  border: 1px dashed #d8ecdf;
  border-radius: 999px;
  background: #fff;
  color: #0fa958;
}

.link-divider span::before {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 52px;
  min-width: 52px;
  height: 52px;
  min-height: 52px;
  aspect-ratio: 1 / 1;
  border-radius: 999px;
  background: #dff7eb;
  content: '';
  transform: translate(-50%, -50%);
}

.link-divider span::after {
  display: none;
}

.link-divider span .data-source-icon {
  position: relative;
  z-index: 1;
}

.flow-link-arrows {
  position: relative;
  z-index: 1;
  width: 30px;
  height: 30px;
  fill: none;
  stroke: currentColor;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke-width: 2.3;
}

.init-script-form-item .ivu-form-item-content {
  margin-left: 0 !important;
  padding-right: 0 !important;
}

.init-script-field {
  display: block;
  min-width: 0;
}

.init-script-label {
  display: grid;
  grid-template-columns: 16px minmax(0, max-content);
  align-items: center;
  margin-bottom: 12px;
  padding: 0;
  color: #5f6c80;
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  white-space: nowrap;
}

.init-script-label span {
  color: #ed4014;
}

.init-script-control {
  min-width: 0;
  overflow: visible;
  padding-left: 16px;
}

.init-radio-row {
  display: flex;
  align-items: center;
  gap: 24px;
  min-height: 24px;
  width: 100%;
}

.init-radio-row .ivu-radio-wrapper {
  flex: 0 1 auto;
  margin-right: 0;
  color: #1f2937;
  font-size: 13px;
  font-weight: 400;
  line-height: 22px;
  white-space: nowrap;
}

.init-radio-hint {
  max-width: 96%;
  margin-top: 12px;
  color: #6b7789;
  font-size: 12px;
  font-weight: 400;
  line-height: 1.5;
}

.field-label {
  margin-bottom: 12px;
  color: #293449;
  font-size: 14px;
  font-weight: 600;
}

.field-label.required::after {
  margin-left: 4px;
  color: #ef4444;
  content: '*';
}

.flow-notice-section .ivu-form-item-label {
  padding: 0 0 10px;
  color: #5f6c80;
  font-size: 14px;
  font-weight: 500;
}

.flow-notice-section .ivu-select-selection {
  min-height: 38px;
  border-color: #dce3eb;
  border-radius: 6px;
}

.flow-notice-section .ivu-select-placeholder,
.flow-notice-section .ivu-select-selected-value,
.flow-notice-section .ivu-select-input {
  height: 36px;
  line-height: 36px;
  font-size: 13px;
}

.flow-notice-section .ivu-select-disabled .ivu-select-selection {
  background: #f7f8fa !important;
  border-color: #e0e6ee !important;
  opacity: 1;
}

.flow-notice-section .ivu-select-disabled .ivu-select-placeholder,
.flow-notice-section .ivu-select-disabled .ivu-select-selected-value,
.flow-notice-section .ivu-select-disabled .ivu-select-input,
.flow-notice-section .ivu-select-disabled .ivu-select-arrow {
  color: #b4bfcc !important;
  -webkit-text-fill-color: #b4bfcc;
}

.notice-layout {
  display: block;
}

.notice-section-label {
  margin-bottom: 12px;
  color: #41454d;
  font-size: 14px;
  font-weight: 500;
}

.channel-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(92px, 112px));
  justify-content: start;
  gap: 8px;
  margin-bottom: 18px;
}

.channel-card {
  min-height: 38px;
  padding: 0 10px;
  font-size: 13px;
  font-weight: 400;
  gap: 6px;
}

.channel-card.active,
.channel-card.selected {
  border-color: #13a95f;
  background: #f2fbf6;
  color: #0f9f55;
  box-shadow: inset 0 0 0 1px #48cf86;
}

.notice-form-row form {
  display: grid;
  grid-template-columns: minmax(260px, 1fr) minmax(260px, 1fr);
  gap: 42px;
}

.notice-form-row .ivu-form-item {
  margin-bottom: 0;
}

.subscription-panel {
  padding: 0;
}

.subscription-title {
  margin-bottom: 12px;
  color: #41454d;
  font-size: 14px;
  font-weight: 500;
}

.subscription-list {
  display: flex;
  flex-direction: column;
  gap: 0;
  overflow: hidden;
  border: none;
  border-radius: 0;
  background: transparent;
}

.subscription-row {
  display: flex;
  align-items: center;
  gap: 18px;
  min-height: 52px;
  padding: 0 0;
  border-bottom: 1px dashed #e5ecf3;
  color: #6b7789;
  font-size: 14px;
  font-weight: 500;
}

.subscription-row:last-child {
  border-bottom: 0;
}

.subscription-row .ivu-switch-disabled {
  opacity: 1;
}

.subscription-row .ivu-switch-disabled.ivu-switch-checked,
.subscription-row .ivu-switch-disabled {
  background-color: #dfe4ec !important;
  border-color: #dfe4ec !important;
}

.flow-config-list {
  display: grid;
  gap: 22px;
}

.flow-execute-section .flow-config-list {
  gap: 22px;
  border-top: none;
}

.flow-config-row {
  display: grid;
  grid-template-columns: 132px minmax(0, 1fr);
  align-items: start;
  gap: 24px;
  min-width: 0;
  padding-bottom: 22px;
  border-bottom: 1px solid #edf2f7;
}

.flow-config-row:last-child {
  padding-bottom: 0;
  border-bottom: 0;
}

.flow-execute-section .flow-config-row {
  grid-template-columns: 132px minmax(0, 1fr);
  align-items: center;
  gap: 28px;
  min-height: 72px;
  padding: 0;
  border-bottom: 1px solid #edf2f7;
}

.flow-execute-section .flow-config-row:last-child {
  border-bottom: 0;
}

.flow-config-label {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  min-height: 24px;
  color: #5c6b82;
  font-size: 15px;
  font-weight: 500;
  line-height: 24px;
  white-space: nowrap;
}

.flow-config-control {
  min-width: 0;
  overflow: visible;
}

.flow-execute-section .flow-config-control {
  display: grid;
  grid-template-columns: minmax(220px, 0.78fr) minmax(260px, 1fr);
  align-items: center;
  gap: 30px;
}

.flow-config-hint {
  max-width: 96%;
  margin-top: 12px;
  font-size: 12px;
  font-weight: 400;
  line-height: 1.5;
}

.flow-execute-section .flow-config-hint {
  max-width: none;
  min-height: 22px;
  margin-top: 0;
  color: #6b7789;
  font-size: 13px;
  line-height: 1.55;
}

.work-order-config-value {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  min-height: 32px;
  color: #0f9f55;
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
}

.work-order-config-value .ivu-icon {
  font-size: 18px;
}

.release-flow-summary.page-aside {
  grid-column: 2;
  grid-row: 1;
  display: flex;
  flex-direction: column;
  align-self: stretch;
  box-sizing: border-box;
  width: 100%;
  min-height: 0;
  padding: 24px;
  background: var(--bg-secondary, #f8fafc);
  border: none;
  border-radius: 10px;
  box-shadow: none;
  overflow: hidden;
}

.summary-body {
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  overscroll-behavior: contain;
}

.summary-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex: 0 0 auto;
  margin-bottom: 20px;
  color: #181d26;
  font-size: 16px;
  font-weight: 500;
}

.summary-title-main {
  display: inline-flex;
  align-items: center;
  min-width: 0;
  gap: 10px;
}

.summary-help-link {
  flex: 0 0 auto;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  height: auto;
  padding: 0;
  color: #0f9f55;
  font-size: 13px;
  font-weight: 700;
  line-height: 1;
}

.summary-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 0 0 24px;
  padding: 0;
}

.summary-group:last-child {
  margin-bottom: 0;
}

.summary-group h3 {
  position: relative;
  margin: 0;
  padding-left: 12px;
  color: #181d26;
  font-size: 14px;
  font-weight: 500;
}

.summary-group h3::before {
  position: absolute;
  top: 50%;
  left: 0;
  width: 3px;
  height: 14px;
  border-radius: 2px;
  background: #18b566;
  transform: translateY(-50%);
  content: '';
}

.summary-group h3:not(:first-child) {
  margin-top: 16px;
}

.summary-row,
.summary-row-reserved {
  display: grid;
  grid-template-columns: 96px minmax(0, 1fr);
  gap: 12px;
  min-height: 22px;
  align-items: start;
  color: #637083;
  font-size: 13px;
  line-height: 1.5;
}

.summary-subtitle {
  margin: 14px 0 8px;
  color: #111827;
  font-size: 13px;
  font-weight: 700;
  line-height: 1.4;
}

.summary-subtitle:first-of-type {
  margin-top: 0;
}

.summary-row strong {
  overflow: hidden;
  color: #1f2937;
  font-weight: 600;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.summary-row strong.summary-value-with-icon {
  display: inline-flex;
  align-items: center;
  min-width: 0;
  gap: 5px;
}

.summary-value-with-icon .data-source-icon {
  flex: 0 0 auto;
}

.summary-value-with-icon span {
  overflow: hidden;
  min-width: 0;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.page-footer {
  display: flex;
  flex: 0 0 auto;
  justify-content: center;
  gap: 12px;
  padding: 16px 24px 20px;
  background: transparent;
  border-top: none;
  box-shadow: none;
}

.primary-action {
  min-width: 148px;
}

.release-flow-success {
  display: flex;
  flex: 1 1 auto;
  align-items: flex-start;
  justify-content: center;
  min-height: 0;
  padding: 48px 24px 32px;
}

.success-card {
  width: min(680px, 100%);
  padding: 0;
  background: transparent;
  border: none;
  border-radius: 0;
  box-shadow: none;
  text-align: center;
}

.success-card h2 {
  margin: 18px 0 8px;
  color: #181d26;
  font-size: 22px;
  font-weight: 500;
}

.success-card p {
  margin: 0 0 26px;
  color: #64748b;
}

.webhook-fields {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin: 0 auto 28px;
  text-align: left;
}

.webhook-row {
  display: grid;
  grid-template-columns: 112px minmax(0, 1fr);
  align-items: center;
  gap: 12px;
}

.webhook-row span {
  color: #475569;
  font-weight: 600;
}

.success-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.mini-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #d6e1ec;
  border-top-color: #0f9f55;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 1500px) {
  .release-flow-shell {
    grid-template-columns: minmax(0, 1fr) 300px;
    gap: 18px;
    padding: 20px 24px;
  }

  .notice-section-label,
  .subscription-title {
    font-size: 13px;
  }

  .channel-grid {
    gap: 14px;
    margin-bottom: 24px;
  }

  .channel-card {
    min-height: 38px;
    gap: 8px;
    font-size: 14px;
  }

  .notice-form-row form {
    gap: 28px;
  }

  .subscription-row {
    min-height: 48px;
    font-size: 13px;
  }

  .basic-form {
    column-gap: 28px;
  }

  .release-grid {
    grid-template-columns: minmax(0, 1fr) 36px minmax(0, 1fr);
    gap: 16px;
  }

  .basic-info-section .ivu-form-item-label,
  .release-config-section .ivu-form-item-label {
    font-size: 13px;
  }

  .basic-info-section .ivu-input,
  .release-config-section .ivu-input {
    height: 34px;
    font-size: 12px;
  }

  .basic-info-section .ivu-select-selection,
  .release-config-section .ivu-select-selection {
    min-height: 34px;
  }

  .basic-info-section .ivu-select-placeholder,
  .basic-info-section .ivu-select-selected-value,
  .basic-info-section .ivu-select-input,
  .release-config-section .ivu-select-placeholder,
  .release-config-section .ivu-select-selected-value,
  .release-config-section .ivu-select-input {
    height: 32px;
    line-height: 32px;
    font-size: 12px;
  }

  .release-config-section .gitops-select-form-item .ivu-select-single .ivu-select-placeholder,
  .release-config-section .gitops-select-form-item .ivu-select-single .ivu-select-selected-value {
    height: auto;
    line-height: 1.2;
  }

  .basic-info-section .ivu-form-item-content,
  .release-config-section .ivu-form-item-content {
    padding-bottom: 18px;
  }

  .release-panel .ivu-form-item-content {
    padding-right: 24px;
  }

  .basic-form .ivu-form-item-error-tip,
  .release-config-section .ivu-form-item-error-tip {
    line-height: 16px;
  }

  .release-config-section .ivu-form-item {
    margin-bottom: 0;
  }

  .type-card-group {
    grid-template-columns: repeat(auto-fit, minmax(132px, 1fr));
    gap: 6px;
  }

  .type-card {
    min-height: 32px;
    padding: 0 8px;
    border-radius: 6px;
    font-size: 11px;
    gap: 5px;
  }

  .release-config-section .ivu-form-item-label {
    padding: 7px 10px 7px 14px;
  }

  .field-hint {
    margin-top: 6px;
    font-size: 11px;
    line-height: 1.45;
  }

  .release-config-section .init-script-form-item.ivu-form-item .ivu-form-item-content {
    display: block !important;
    margin-left: 0 !important;
    padding-right: 0 !important;
    width: 100% !important;
  }

  .init-script-field {
    display: block;
  }

  .init-script-label {
    grid-template-columns: 14px minmax(0, max-content);
    margin-bottom: 10px;
    padding: 0;
    font-size: 13px;
    font-weight: 500;
    line-height: 18px;
  }

  .init-script-control {
    padding-left: 14px;
  }

  .init-radio-row {
    gap: 18px;
    min-height: 20px;
  }

  .init-radio-row .ivu-radio-wrapper {
    font-size: 11px;
    font-weight: 400;
    line-height: 18px;
  }

  .init-radio-hint {
    margin-top: 12px;
    font-size: 10px;
    font-weight: 400;
    line-height: 1.45;
  }

  .link-divider span {
    flex-basis: 56px;
    width: 56px;
    min-width: 56px;
    max-width: 56px;
    height: 56px;
    min-height: 56px;
    max-height: 56px;
  }

  .link-divider span::before {
    width: 40px;
    min-width: 40px;
    height: 40px;
    min-height: 40px;
  }

  .flow-link-arrows {
    width: 26px;
    height: 26px;
    stroke-width: 2.4;
  }

  .release-flow-summary.page-aside {
    min-height: 0;
    padding: 20px;
  }

  .summary-title {
    margin-bottom: 16px;
    font-size: 15px;
  }

  .summary-group {
    margin-bottom: 20px;
  }

  .summary-group h3:not(:first-child) {
    margin-top: 16px;
  }

  .summary-row,
  .summary-row-reserved {
    grid-template-columns: 96px minmax(0, 1fr);
    gap: 12px;
    min-height: 22px;
    font-size: 13px;
    line-height: 1.5;
  }
}

.release-flow-shell-config {
  align-items: stretch;
}

.release-flow-batch-config-shell {
  grid-template-columns: minmax(0, 1fr);
}

.release-flow-shell-config .release-flow-main {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 32px;
  min-height: 0;
}

.release-flow-shell-config .release-flow-summary.page-aside {
  align-self: stretch;
  height: 100%;
  min-height: 0;
}

.release-flow-shell-config .notice-layout {
  display: grid;
  grid-template-columns: minmax(340px, 0.92fr) minmax(380px, 1.08fr);
  align-items: start;
  gap: 24px;
}

.release-flow-shell-config .notice-channel-panel,
.release-flow-shell-config .subscription-panel {
  min-width: 0;
}

.release-flow-shell-config .notice-section-label,
.release-flow-shell-config .subscription-title {
  font-size: 15px;
}

.release-flow-shell-config .notice-section-label {
  margin-bottom: 14px;
}

.release-flow-shell-config .channel-grid {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  justify-content: start;
  gap: 7px;
  margin-bottom: 16px;
}

.release-flow-shell-config .channel-card {
  min-height: 34px;
  padding: 0 6px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 400;
  gap: 4px;
}

.release-flow-shell-config .channel-card span {
  overflow: visible;
  text-overflow: clip;
}

.release-flow-shell-config .notice-form-row form {
  display: block;
}

.release-flow-shell-config .flow-notice-section .ivu-form-item-label {
  padding-bottom: 8px;
  font-size: 14px;
}

.release-flow-shell-config .flow-notice-section .ivu-select-selection {
  min-height: 38px;
}

.release-flow-shell-config .flow-notice-section .ivu-select-placeholder,
.release-flow-shell-config .flow-notice-section .ivu-select-selected-value,
.release-flow-shell-config .flow-notice-section .ivu-select-input {
  height: 36px;
  line-height: 36px;
  font-size: 13px;
}

.release-flow-shell-config .subscription-title {
  margin-bottom: 14px;
}

.release-flow-shell-config .subscription-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  min-height: 162px;
}

.release-flow-shell-config .subscription-row {
  min-height: 38px;
  gap: 10px;
  padding: 0 12px;
  font-size: 13px;
}

.release-flow-shell-config .subscription-row:nth-child(odd) {
  border-right: 1px dashed #e5ecf3;
}

.release-flow-shell-config .subscription-row:nth-last-child(-n + 2) {
  border-bottom: 0;
}

.release-flow-shell-config .flow-config-list {
  gap: 18px;
}

.release-flow-shell-config .flow-execute-section .flow-config-list {
  gap: 0;
  border-top: none;
}

.release-flow-shell-config .flow-config-row {
  grid-template-columns: 116px minmax(0, 1fr);
  gap: 18px;
  padding-bottom: 18px;
}

.release-flow-shell-config .flow-execute-section .flow-config-row {
  grid-template-columns: 116px minmax(0, 1fr);
  gap: 20px;
  min-height: 64px;
  padding: 0;
}

.release-flow-shell-config .flow-config-label {
  font-size: 14px;
  line-height: 22px;
}

.release-flow-shell-config .flow-execute-section .flow-config-control {
  grid-template-columns: minmax(190px, 0.76fr) minmax(220px, 1fr);
  gap: 22px;
}

.release-flow-shell-config .flow-config-hint {
  margin-top: 10px;
  font-size: 12px;
  line-height: 1.5;
}

.release-flow-shell-config .flow-execute-section .flow-config-hint {
  min-height: 20px;
  margin-top: 0;
}

@media (max-width: 1240px) {
  .release-flow-shell {
    grid-template-columns: 1fr;
  }

  .release-flow-shell-config .release-flow-main {
    grid-template-columns: minmax(0, 1fr);
    gap: 32px;
  }

  .release-flow-shell-config .channel-grid {
    grid-template-columns: repeat(4, minmax(56px, 1fr));
    gap: 7px;
  }

  .release-flow-main,
  .release-flow-summary.page-aside {
    grid-column: auto;
    grid-row: auto;
  }

  .release-flow-main {
    height: max-content;
    min-height: max-content;
  }

  .release-flow-summary.page-aside {
    display: block;
    height: auto;
    min-height: auto;
  }
}

@media (max-width: 1060px) {
  .release-flow-shell-config .notice-layout {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 900px) {
  .release-flow-shell {
    padding: 10px 16px 16px;
  }

  .basic-form,
  .release-grid,
  .notice-layout {
    grid-template-columns: 1fr;
  }

  .flow-config-row {
    grid-template-columns: 1fr;
    gap: 10px;
  }

  .flow-execute-section .flow-config-row,
  .release-flow-shell-config .flow-execute-section .flow-config-row {
    grid-template-columns: 1fr;
    gap: 8px;
    min-height: 0;
    padding: 14px 0;
    align-items: start;
  }

  .flow-execute-section .flow-config-control,
  .release-flow-shell-config .flow-execute-section .flow-config-control {
    grid-template-columns: 1fr;
    gap: 8px;
  }

  .link-divider {
    display: none;
  }

  .notice-layout > div:first-child {
    padding-right: 0;
    border-right: 0;
  }

  .channel-grid {
    grid-template-columns: repeat(2, minmax(92px, 1fr));
  }

  .notice-form-row form {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 600px) {
  .release-flow-shell {
    padding: 10px 12px 16px;
    overflow-x: hidden;
  }

  .release-flow-main,
  .page-section,
  .release-grid,
  .release-panel,
  .release-panel form {
    width: 100%;
    max-width: 100%;
    min-width: 0;
    box-sizing: border-box;
  }

  .release-config-section .ivu-form-item {
    display: block;
  }

  .release-config-section .ivu-form-item-label {
    float: none;
    width: 100% !important;
    padding: 0 0 7px 14px;
    line-height: 20px;
  }

  .release-config-section .ivu-form-item-content,
  .release-panel .ivu-form-item-content {
    width: 100% !important;
    margin-left: 0 !important;
    padding-right: 0;
  }

  .repo-control,
  .schema-form-item .inline-control {
    display: grid;
    grid-template-columns: minmax(0, 1fr) 24px;
    width: 100%;
    column-gap: 8px;
  }

  .repo-refresh-action {
    position: relative;
    top: auto;
    left: auto;
    align-self: center;
    transform: none;
  }

  .init-script-control {
    padding-left: 0;
  }

  .init-radio-row {
    flex-wrap: wrap;
    gap: 8px 14px;
  }

  .init-radio-row .ivu-radio-wrapper {
    white-space: normal;
  }
}
</style>

<style>
.release-flow-select-dropdown {
  position: fixed !important;
  max-height: var(--release-flow-dropdown-max-height, min(320px, calc(100vh - 96px))) !important;
  overflow: hidden !important;
  transform-origin: center top !important;
  overscroll-behavior: contain;
}

.release-flow-select-dropdown .ivu-select-dropdown-list {
  max-height: var(--release-flow-dropdown-max-height, min(320px, calc(100vh - 96px))) !important;
  overflow-y: auto !important;
  overscroll-behavior: contain;
}

.release-flow-select-dropdown .database-type-option-content {
  display: inline-flex;
  max-width: 100%;
  align-items: center;
  gap: 8px;
  vertical-align: middle;
}

.release-flow-select-dropdown .database-type-option-content > span:last-child {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
