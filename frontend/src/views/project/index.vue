<template>
  <div class="project-account">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem to="/project">{{ $t('xiang-mu-lie-biao') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option">
            <div class="left">
              <Input
                style="width: 280px; margin-right: 10px"
                clearable
                v-model="searchKeywords"
                @on-enter="handleQuery"
                @on-clear="handleQueryClear"
              />
              <Button type="primary" @click="handleQuery">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button
                @click="handleShowAddProjectModal"
                type="primary"
                ghost
                style="margin-right: 10px"
                icon="md-add"
                v-if="myAuth.includes('DM_PROJECT_MANAGE')"
              >
                {{ $t('xin-jian-xiang-mu') }}
              </Button>
              <Button @click="handleQuery" :loading="loading">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table
              :columns="projectTableColumns"
              :data="projectList"
              :loading="loading"
              :locale="{ emptyText: $t('zan-wu-shu-ju') }"
              size="small"
              border
              stripe
            >
              <template #mark="{ row }">
                <CustomIcon type="icon-v2-Archive" v-if="row.status === 'ARCHIVE'" />
                <CustomIcon type="icon-v2-Delete2" v-if="row.status === 'DELETE'" />
                <CustomIcon :type="`icon-v2-${row.mark}`" v-if="row.status === 'NORMAL'" />
              </template>
              <template #action="{ row }">
                <div class="action">
                  <Button type="text" @click="goDetail(row)" :disabled="row.status === 'DELETE'">{{ $t('jin-ru') }}</Button>
                </div>
              </template>
            </Table>
          </div>
          <div class="footer">
            <Page
              :total="pageTotal"
              show-total
              show-elevator
              @on-change="handlePageChange"
              show-sizer
              v-model="pageNum"
              :page-size="pageSize"
              @on-page-size-change="handlePageSizeChange"
            />
          </div>
        </div>
      </div>
    </div>
    <CCModal :title="getModalTitle" v-model="projectDialogShow" @on-cancel="handleCloseModal" :autoClean="false" :width="projectDialogWidth">
      <!-- Step BASIC1 -->
      <div v-show="step === PROJECT_STEP.BASIC1" class="step-basic1">
        <div>
          <img src="@/assets/create-project.png" class="img-wrap" />
        </div>
        <Form :ref="`form${PROJECT_STEP.BASIC1}`" :model="projectBasicForm" :rules="projectFormBasicRule" inline :label-width="100">
          <FormItem :label="$t('xiang-mu-ming-cheng')" prop="projectName">
            <Input class="basic1-base" type="text" v-model="projectBasicForm.projectName" />
          </FormItem>
          <FormItem :label="$t('xiang-mu-miao-shu')" prop="projectDesc">
            <Input class="basic1-base" type="textarea" :rows="4" v-model="projectBasicForm.projectDesc" />
          </FormItem>
          <FormItem :label="$t('guan-li-yuan')" prop="projectOwnerUid">
            <Select class="basic1-base" v-model="projectBasicForm.projectOwnerUid">
              <template #prefix>
                <CustomIcon type="icon-v2-svg-USER" />
              </template>
              <Option v-for="item in devopsUsers" :value="item.userUid" :key="item.userUid" :label="item.userName">
                {{ item.userName }}
              </Option>
            </Select>
          </FormItem>
        </Form>
      </div>
      <!-- Step BASIC2 -->
      <div v-show="step === PROJECT_STEP.BASIC2" class="step-basic2">
        <div class="btn-wrap">
          <div @click="handleNextStep()">
            <div>{{ $t('shi') }}</div>
            <div>{{ $t('gen-ju-xiang-dao-pei-zhi-zhe-ge-xiang-mu') }}</div>
          </div>
          <div @click="handleAddProject('empty')">
            <div>{{ $t('fou') }}</div>
            <div>
              {{ $t('chuang-jian-yi-ge-kong-xiang-mu-shao-hou-pei-zhi-ta') }}
            </div>
          </div>
        </div>
      </div>

      <!-- Step 0 -->
      <div v-show="step === PROJECT_STEP.S0" class="step-0">
        <div class="flow-container">
          <div class="icon github">
            <CustomIcon :type="devopsFrom" size="26px" />
          </div>

          <svg class="flow-line" width="300" height="4">
            <line x1="0" y1="2" x2="300" y2="2" stroke="#ccc" stroke-dasharray="6,6" stroke-width="2" />
            <circle r="4" fill="#4A90E2">
              <animateMotion dur="2s" repeatCount="indefinite" rotate="auto">
                <mpath xlink:href="#motionPath" />
              </animateMotion>
            </circle>
            <path id="motionPath" d="M0,2 L300,2" fill="none" />
          </svg>

          <div class="icon mysql">
            <CustomIcon :type="devopsTo" size="26px" />
          </div>
        </div>
        <div class="content">
          <Form class="left" :ref="`form${PROJECT_STEP.S0}`" :model="projectDevopsForm" :rules="projectPipelineRule" inline :label-width="80">
            <FormItem :label="$t('fu-wu-shang')" prop="repoScmId">
              <Select class="s0-base" v-if="devopsScmList.length" v-model="projectDevopsForm.repoScmId" @on-change="handleDevopsScmSelected">
                <Option v-for="item in devopsScmList" :value="item.scmId" :key="item.scmId">
                  <CustomIcon :type="item?.scmType" rightMargin />
                  {{ item.scmDisplay }}
                </Option>
              </Select>
              <Button v-else type="text" @click="goToAddScm">{{ $t('qu-pei-zhi') }}</Button>
            </FormItem>
            <FormItem :label="$t('xuan-ze-cang-ku')" prop="repoName">
              <div style="display: flex; align-items: center">
                <Select
                  v-model="projectDevopsForm.repoName"
                  class="s0-base select-warp"
                  :disabled="!devopsScmSelected"
                  @on-change="handleDevopsRepoSelected"
                  filterable
                >
                  <OptionGroup v-for="(repoGroup, namespace) in devopsRepoListByGroup" :label="namespace" :key="namespace">
                    <Option v-for="repo in repoGroup" :value="repo.repoName" :key="repo.repoUrl" :label="repo.repoName">
                      <span>{{ repo.repoName }}</span>
                      <span style="float: right">
                        <CustomIcon type="icon-v2-jicheng" @click.native.stop="handleDevopsJumpToRepo(repo.repoHome)" />
                      </span>
                    </Option>
                  </OptionGroup>
                </Select>
                <div v-if="repoLoading" class="spinner"></div>
                <CustomIcon type="icon-v2-Refresh" @click="handleDevopsScmSelected" leftMargin v-if="!repoLoading" />
              </div>
            </FormItem>
            <FormItem :label="$t('mu-biao-fen-zhi')" prop="repoBranch">
              <Input class="s0-base" type="text" v-model="projectDevopsForm.repoBranch">
                <template #prefix>
                  <CustomIcon type="icon-v2-branches" style="height: 100%" />
                </template>
              </Input>
            </FormItem>
            <FormItem :label="$t('jiao-ben-lu-jin')" prop="repoScriptPath">
              <Input class="s0-base" type="text" v-model="projectDevopsForm.repoScriptPath">
                <template #prefix>
                  <CustomIcon type="icon-v2-jiaobenrenwu" style="height: 100%" />
                </template>
              </Input>
              <div>{{ $t('devops-script-hint') }}</div>
            </FormItem>
            <FormItem :label="$t('shi-jian-0')">
              <RadioGroup v-model="projectDevopsForm.eventType">
                <Radio label="Push">{{ EVEN_TYPE_MAP.push }}</Radio>
                <Radio label="PullRequest">{{ EVEN_TYPE_MAP.pr }}</Radio>
              </RadioGroup>
            </FormItem>
          </Form>
          <Form class="right" :ref="`form${PROJECT_STEP.S1}`" :model="projectDevopsForm" :rules="projectOptionRule" inline :label-width="90">
            <FormItem :label="$t('shi-li-1')" prop="instanceId">
              <Select
                class="s0-base select-warp"
                v-if="devopsInsList.length"
                v-model="projectDevopsForm.instanceId"
                @on-change="handleDevopsChangeIns"
                filterable
              >
                <Option v-for="ins in devopsInsList" :value="ins.objId" :key="ins.objId">
                  <CustomIcon :type="ins.objAttr.dsType" style="margin-right: 5px" />
                  {{ ins.objName }}
                </Option>
              </Select>
              <Button v-else type="text" @click="goToDsSetting">{{ $t('qu-pei-zhi') }}</Button>
            </FormItem>
            <FormItem :label="$t('shu-ju-ku')" prop="catalogName" v-if="projectDevopsForm.devopsInsHasCatalog">
              <div style="display: flex; align-items: center">
                <Select
                  class="s0-base select-warp"
                  v-model="projectDevopsForm.catalogName"
                  :disabled="!projectDevopsForm.devopsInsHasCatalog"
                  @on-change="handleChangeCatalog"
                  filterable
                >
                  <Option v-for="catalog in devopsInsCatalogList" :value="catalog.objName" :key="catalog.objName">
                    {{ catalog.objName }}
                  </Option>
                </Select>
                <CustomIcon type="icon-v2-Refresh" @click="fetchCatalogList(true)" leftMargin v-if="!repoLoading" />
              </div>
            </FormItem>
            <FormItem label="schema" prop="schemaName">
              <div style="display: flex; align-items: center">
                <Select
                  class="s0-base select-warp"
                  v-model="projectDevopsForm.schemaName"
                  :disabled="!projectDevopsForm.devopsInsHasSchema"
                  filterable
                >
                  <Option v-for="schema in devopsInsSchemaList" :value="schema.objName" :key="schema.objName">
                    {{ schema.objName }}
                  </Option>
                </Select>
                <CustomIcon type="icon-v2-Refresh" @click="fetchSchemaList(true)" leftMargin v-if="!repoLoading" />
              </div>
            </FormItem>
            <FormItem :label="$t('chu-shi-hua-fang-shi')" prop="initScript">
              <RadioGroup size="small" v-model="projectDevopsForm.initScript" type="button">
                <Radio label="Snapshot">{{ INIT_TYPE_MAP.snapshot }}</Radio>
                <Radio label="CreateChange">{{ INIT_TYPE_MAP.change }}</Radio>
                <Radio label="None">{{ INIT_TYPE_MAP.none }}</Radio>
              </RadioGroup>
              <div style="padding-top: 5px">
                {{ fetchProjectGitOpsDescription(projectDevopsForm.initScript) }}
              </div>
            </FormItem>
          </Form>
        </div>
      </div>
      <!-- Step 1 -->
      <div v-if="step === PROJECT_STEP.S1" class="step-1">
        <div class="content">
          <div class="im-group">
            <div class="im-list">
              <div
                v-for="im in imDefList"
                :class="`im ${imDefSelected.imType === im.imType ? 'im-selected' : ''}`"
                :key="im.imType"
                @click="handleImDefOne(im)"
              >
                <CustomIcon :type="im.imType === 'none' ? 'Disable' : im.imType" />
                <div>{{ im.imTypeI18n }}</div>
              </div>
            </div>
            <Select
              class="im-select"
              v-model="projectImForm.imId"
              :disabled="imDefSelected.imType === 'none'"
              @on-change="handleImProviderSelected"
              :placeholder="$t('qing-xuan-ze-yi-ge-im-ti-gong-zhe')"
              :not-found-text="$t('zan-wu-shu-ju')"
            >
              <template #prefix>
                <CustomIcon :type="imDefSelected.imType === 'none' ? 'Disable' : imDefSelected.imType" rightMargin />
              </template>
              <Option v-for="item in imProviderList" :key="item.imId" :value="item.imId" :label="item.display" :disabled="!item.enable">
                <span>{{ item.display }}</span>
              </Option>
            </Select>
          </div>
          <div class="im-item-wrap">
            <div>
              <CCTitle :content="$t('ding-yue-de-xiao-xi')" />
              <div>
                <i-switch true-color="#52C41A" v-model="projectImForm.eventProjectStatus" :disabled="imDefSelected.imType === 'none'" />
                <span class="switch-text switch-btn">
                  {{ $t('xiang-mu-zhuang-tai-de-bian-hua') }}
                </span>
              </div>
              <div>
                <i-switch true-color="#52C41A" v-model="projectImForm.eventProjectConfig" :disabled="imDefSelected.imType === 'none'" />
                <span class="switch-text switch-btn">{{ $t('xiang-mu-pei-zhi-gai-bian') }}</span>
              </div>
              <div>
                <i-switch true-color="#52C41A" v-model="projectImForm.eventChangeLife" :disabled="imDefSelected.imType === 'none'" />
                <span class="switch-text switch-btn">{{ $t('bian-geng-jie-duan-bian-hua') }}</span>
              </div>
              <div>
                <i-switch true-color="#52C41A" v-model="projectImForm.eventChangeNotice" :disabled="imDefSelected.imType === 'none'" />
                <span class="switch-text switch-btn">{{ $t('bian-geng-zhuang-tai-xiao-xi') }}</span>
              </div>
            </div>
            <div>
              <CCTitle :content="$t('mo-ren-yu-yan')" />
              <RadioGroup size="small" v-model="projectImForm.language" type="button">
                <Radio label="zh">{{ defaultLanguageMap.zh }}</Radio>
                <!-- <Radio label="en">{{ defaultLanguageMap.en }}</Radio> -->
              </RadioGroup>
            </div>
          </div>
        </div>
      </div>
      <!-- Step 2 -->
      <div v-if="step === PROJECT_STEP.S2" class="step-2">
        <div class="content">
          <Steps class="flow-drawer-steps" size="small" :current="-1">
            <Step :title="$t('sql-shen-he-0')" />
            <Step :title="$t('shen-pi-liu')"></Step>
            <Step :title="$t('fa-bu-fang-shi')"></Step>
          </Steps>
          <Row :gutter="12">
            <Col :span="8">
              <div class="flow-card">
                <CCTitle :content="$t('sql-shen-he-0')" />
                <RadioGroup v-model="projectBasicForm.checkStrategy" size="small" type="button">
                  <Radio label="Always">{{ SQL_REVIEW_MAP.always }}</Radio>
                  <Radio label="Suggest">{{ SQL_REVIEW_MAP.suggest }}</Radio>
                  <Radio label="Failure">{{ SQL_REVIEW_MAP.failure }}</Radio>
                  <Radio label="Skip">{{ SQL_REVIEW_MAP.skip }}</Radio>
                </RadioGroup>
                <div class="flow-drawer-step-tips">
                  {{ fetchProjectFlowDescription('check', projectBasicForm.checkStrategy) }}
                </div>
              </div>
            </Col>

            <Col :span="8">
              <div class="flow-card">
                <CCTitle :content="$t('shen-pi-liu')" />
                <RadioGroup v-model="projectBasicForm.approveStrategy" size="small" type="button">
                  <Radio label="Enable">{{ APPROVE_MAP.Enable }}</Radio>
                  <Radio label="Disable">{{ APPROVE_MAP.Disable }}</Radio>
                </RadioGroup>
                <div class="flow-drawer-step-tips">
                  {{ fetchProjectFlowDescription('approve', projectBasicForm.approveStrategy) }}
                </div>
              </div>
            </Col>

            <Col :span="8">
              <div class="flow-card">
                <CCTitle :content="$t('fa-bu-fang-shi')" />
                <RadioGroup v-model="projectBasicForm.executeStrategy" size="small" type="button" @on-change="handleFlowOfExecuteOption">
                  <Radio label="Auto">{{ PUBLISH_MAP.auto }}</Radio>
                  <Radio label="Manual">{{ PUBLISH_MAP.manual }}</Radio>
                  <Radio label="Disabled">{{ PUBLISH_MAP.disabled }}</Radio>
                </RadioGroup>
                <div class="flow-drawer-step-tips">
                  {{ fetchProjectFlowDescription('execute', projectBasicForm.executeStrategy) }}
                </div>

                <div class="flow-section">
                  <div class="flow-section-label">
                    {{ $t('shi-yong-shi-wu') }}
                  </div>
                  <RadioGroup v-model="projectBasicForm.transactional" size="small" type="button">
                    <Radio label="false" :disabled="!projectFlowExecuteIsAuto">{{ APPROVE_MAP.Disable }}</Radio>
                    <Radio label="true" :disabled="!projectFlowExecuteIsAuto">{{ APPROVE_MAP.Enable }}</Radio>
                  </RadioGroup>
                  <div class="flow-drawer-step-tips">
                    {{ fetchProjectFlowDescription('transactional', projectBasicForm.transactional) }}
                  </div>
                </div>

                <div class="flow-section">
                  <div class="flow-section-label">
                    {{ $t('cuo-wu-ce-lve') }}
                  </div>
                  <RadioGroup v-model="projectBasicForm.errorStrategy" size="small" type="button">
                    <Radio label="NONE" :disabled="!projectFlowExecuteIsAuto">{{ ERROR_STRATEGY_MAP.abort }}</Radio>
                    <Radio label="RETRY" :disabled="!projectFlowExecuteIsAuto">{{ ERROR_STRATEGY_MAP.retry }}</Radio>
                    <Radio label="SKIP" :disabled="!projectFlowExecuteIsAuto">{{ ERROR_STRATEGY_MAP.ignore }}</Radio>
                  </RadioGroup>
                  <div class="flow-drawer-step-tips">
                    {{ fetchProjectFlowDescription('error', projectBasicForm.errorStrategy) }}
                  </div>
                </div>
              </div>
            </Col>
          </Row>
        </div>
      </div>

      <!-- Step 3 -->
      <div v-if="step === PROJECT_STEP.FINISH || (step === 3 && imDefSelected.imType === 'none')">
        <div>
          <div class="title-text">
            <CustomIcon type="icon-v2-SuccessColorful" size="70px" bottomMargin="20px" />
            <div class="title-text">{{ $t('cha-kan-xiang-mu-webhook') }}</div>
          </div>
          <div class="finish-wrap">
            <div class="finish-left">
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('cang-ku-di-zhi') }}：</span>
                <Input v-model="webhook.repoUrl" style="width: 380px" readonly>
                  <template #suffix>
                    <Icon type="ios-link" @click="handleJumpUrl(webhook.repoUrl)" />
                  </template>
                </Input>
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('webhook-url') }}：</span>
                <Input v-model="webhook.url" style="width: 380px" readonly>
                  <template #suffix>
                    <Icon type="ios-copy" @click="handleCopyTemp(webhook.url)" />
                  </template>
                </Input>
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('webhook-mi-ma') }}：</span>
                <Input v-model="webhook.password" style="width: 380px" readonly>
                  <template #suffix>
                    <Icon type="ios-copy" @click="handleCopyTemp(webhook.password)" />
                  </template>
                </Input>
              </div>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <Button type="primary" @click="handlePreviousStep" v-if="btnStatusShow()" :disabled="step === PROJECT_STEP.BASIC1">
          {{ $t('shang-yi-bu') }}
        </Button>
        <Button type="primary" @click="handleNextStep()" v-if="btnStatusShow() && step !== PROJECT_STEP.S2" :disabled="step === PROJECT_STEP.FIVE">
          {{ $t('xia-yi-bu') }}
        </Button>
        <Button
          type="primary"
          @click="handleAddProject"
          v-if="(btnStatusShow() && step === PROJECT_STEP.FINISH && imDefSelected.imType === 'none') || step === PROJECT_STEP.S2"
          :loading="createLoading"
        >
          {{ $t('chuang-jian-xiang-mu') }}
        </Button>
        <Button type="primary" @click="jumpToWebhookDoc" v-if="step === PROJECT_STEP.FINISH">{{ $t('cha-kan-wen-dang') }}</Button>
        <Button @click="handleCloseModal" v-if="step === PROJECT_STEP.FINISH">{{ $t('zhi-dao-le') }}</Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import { handleCopy } from '@/utils/clipboard';
import { encryptMixin } from '@/mixins/encryptMixin';
import CCTitle from '@/components/widgets/CCTitle';
import {
  projectTableColumns,
  projectFormBasicRule,
  PROJECT_STEP,
  projectPipelineRule,
  INIT_TYPE_MAP,
  SQL_REVIEW_MAP,
  PUBLISH_MAP,
  APPROVE_MAP,
  PROJECT_FLOW_DESCRIPTION,
  ERROR_STRATEGY_MAP,
  defaultLanguageMap,
  EVEN_TYPE_MAP,
  projectOptionRule,
  GITOPS_DESCRIPTION
} from './constant';
import { DEFAULT_PROJECT_INFO, DEFAULT_DEVOPS_INFO, groupByRepoNamespace } from './utils';

export default {
  name: 'Project',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  data() {
    return {
      searchKeywords: '',
      projectId: '',
      devopsId: '',
      createLoading: false,
      mark: '',
      status: '',
      webhook: {
        password: '',
        url: '',
        webHookHelpUrl: '',
        repoUrl: ''
      },
      pageTotal: '',
      pageNum: 1,
      pageSize: 10,
      projectList: [],
      projectDialogShow: false,
      // projectDialogWidth: 520,
      projectDialogWidth: 800,
      // step BASIC
      projectBasicForm: { ...DEFAULT_PROJECT_INFO },
      devopsUsers: [],
      // step CREATE
      // step ONE
      projectDevopsForm: { ...DEFAULT_DEVOPS_INFO },
      devopsScmList: [],
      ...DEFAULT_PROJECT_INFO,
      devopsScmSelected: null,
      devopsRepoList: [],
      devopsRepoListByGroup: [],
      devopsRepoSelected: null,
      // step TWO
      devopsInsSelected: null,
      devopsInsList: [],
      devopsInsCatalogList: [],
      devopsInsSchemaList: [],
      // step THREE
      projectImForm: {
        imId: '',
        imType: 'none',
        language: 'zh',
        eventProjectStatus: false,
        eventProjectConfig: false,
        eventChangeLife: false,
        eventChangeNotice: false
      },
      imDefList: [],
      imDefSelected: {
        imId: '',
        imType: 'none'
      },
      imProviderList: [],
      projectFlowExecuteIsAuto: true,
      //
      //
      step: PROJECT_STEP.BASIC1, // 控制步骤的显示
      webhookList: [],
      loading: false,
      repoLoading: false,
      devopsFrom: 'icon-v2-cangku',
      devopsTo: 'icon-v2-credits',
      projectFormBasicRule,
      projectPipelineRule,
      projectTableColumns,
      INIT_TYPE_MAP,
      PUBLISH_MAP,
      SQL_REVIEW_MAP,
      PROJECT_FLOW_DESCRIPTION,
      ERROR_STRATEGY_MAP,
      defaultLanguageMap,
      EVEN_TYPE_MAP,
      PROJECT_STEP,
      APPROVE_MAP,
      projectOptionRule
    };
  },
  components: {
    CCTitle
  },
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'dmGlobalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['isSaas']),
    getModalTitle() {
      let prefix = `${this.step}/ 5`;

      if (this.step === PROJECT_STEP.FINISH) {
        return this.$t('xiang-mu-chuang-jian-cheng-gong');
      } else {
        switch (this.step) {
          case PROJECT_STEP.BASIC1:
            prefix = this.$t('ji-ben-xin-xi');
            break;
          case PROJECT_STEP.BASIC2:
            prefix = this.$t('qing-xuan-ze');
            break;
          case PROJECT_STEP.S0:
            prefix = this.$t('tian-jia-git-ops');
            break;
          case PROJECT_STEP.S1:
            prefix = this.$t('im-xiao-xi');
            break;
          case PROJECT_STEP.S2:
            prefix = this.$t('fa-bu-pei-zhi');
            break;
          default:
            break;
        }
        return `${this.$t('chuang-jian-xiang-mu')}: ${prefix}`;
      }
    }
  },
  async mounted() {
    await this.fetchProjectList();
  },
  methods: {
    handleCopy,
    groupByRepoNamespace,
    handleQuery() {
      this.fetchProjectList();
    },
    handleQueryClear() {
      this.searchKeywords = '';
      this.fetchProjectList();
    },
    async fetchProjectList() {
      this.loading = true;

      const res = await this.$services.dmProjectProjectList({
        data: {
          searchKeywords: this.searchKeywords,
          mark: this.mark,
          status: this.status,
          page: {
            pageSize: this.pageSize,
            pageNum: this.pageNum
          }
        }
      });

      this.loading = false;

      if (res.success) {
        this.projectList = res.data.records;
        this.pageTotal = res.data?.total;
        this.pageSize = res.data?.size;
        this.pageNum = res.data?.current;
      }
    },
    async handleShowAddProjectModal() {
      // step -1
      this.step = PROJECT_STEP.BASIC1;
      this.projectBasicForm = {
        ...DEFAULT_PROJECT_INFO,
        projectOwnerUid: this.userInfo.uid
      };
      await this.fetchDevopsUsers();
      this.projectDialogWidth = 520;
      this.projectDialogShow = true;
    },
    //
    async fetchDevopsUsers() {
      this.loading = true;
      const res = await this.$services.dmProjectDevopsUsers({
        data: { search: '' }
      });
      this.loading = false;

      if (res.success) {
        this.devopsUsers = res.data;
      } else {
        this.devopsUsers = [];
      }
    },
    handleFlowOfExecuteOption() {
      if (this.projectBasicForm.executeStrategy === 'Auto') {
        this.projectBasicForm.transactional = 'false';
        this.projectBasicForm.errorStrategy = 'NONE';
        this.projectFlowExecuteIsAuto = true;
      } else {
        this.projectFlowExecuteIsAuto = false;
        this.projectBasicForm.errorStrategy = '';
        this.projectBasicForm.transactional = '';
      }
    },
    //
    fetchProjectGitOpsDescription(option) {
      if (option === '') {
        return this.$t('zan-wu-miao-shu');
      }
      try {
        return GITOPS_DESCRIPTION[option];
      } catch (e) {
        console.error(e);
        return this.$t('zan-wu-miao-shu');
      }
    },
    async fetchDevopsScmList() {
      const res = await this.$services.dmProjectDevopsScmList();

      if (res.success) {
        this.devopsScmList = res.data;
      }
    },
    async handleDevopsScmSelected() {
      this.devopsScmSelected = this.devopsScmList.find((scm) => scm.scmId === this.projectDevopsForm.repoScmId);
      this.devopsFrom = this.devopsScmSelected?.scmType || 'icon-v2-Save-fromCloud';
      if (this.projectDevopsForm.repoScmId) {
        await this.fetchDevopsScmRepos();
      }
    },
    async fetchDevopsScmRepos() {
      this.repoLoading = true;
      const res = await this.$services.dmProjectDevopsRepos({
        data: {
          scmId: this.projectDevopsForm.repoScmId
        }
      });
      this.repoLoading = false;

      if (res.success) {
        this.devopsRepoList = res.data;
        this.devopsRepoListByGroup = this.groupByRepoNamespace(res.data || []);
      }
    },
    handleDevopsJumpToRepo(url) {
      if (url !== '') {
        window.open(url, '_blank');
      }
    },
    handleDevopsRepoSelected() {
      this.devopsRepoSelected = this.devopsRepoList.find((repo) => repo.repoName === this.projectDevopsForm.repoName);
      if (this.devopsRepoSelected) {
        this.projectDevopsForm.repoScmUrl = this.devopsRepoSelected.repoUrl;
        this.projectDevopsForm.repoBranch = this.devopsRepoSelected.repoBranch;
        this.projectDevopsForm.repoSpace = this.devopsRepoSelected.repoSpace;
      }
    },
    async fetchInsList() {
      const res = await this.$services.dmProjectDevopsDsInsLevels();

      if (res.success) {
        this.devopsInsList = res.data;
      }
    },
    async handleDevopsChangeIns() {
      this.devopsInsCatalogList = [];
      this.devopsInsSchemaList = [];
      this.projectDevopsForm.catalogName = '';
      this.projectDevopsForm.schemaName = '';
      if (this.projectDevopsForm.instanceId === '') {
        return;
      }

      this.devopsInsSelected = this.devopsInsList.find((ins) => ins.objId === this.projectDevopsForm.instanceId);

      this.devopsTo = this.devopsInsSelected?.objAttr?.dsType || 'icon-v2-shili';

      if (!this.devopsInsSelected) {
        return;
      }

      const dsConf = this.dmGlobalSetting.dsSettingDef[this.devopsInsSelected.objAttr.dsType];
      this.projectDevopsForm.devopsInsHasCatalog = dsConf.categories.levels.includes('CATALOG');
      this.projectDevopsForm.devopsInsHasSchema = dsConf.categories.levels.includes('SCHEMA');

      if (this.projectDevopsForm.devopsInsHasCatalog) {
        await this.fetchCatalogList();
      } else if (this.projectDevopsForm.devopsInsHasSchema) {
        await this.fetchSchemaList();
      }
    },
    async handleChangeCatalog() {
      this.projectDevopsForm.schemaName = '';
      await this.fetchSchemaList();
    },
    async fetchCatalogList(isRefreshCache = false) {
      const res = await this.$services.dmProjectDevopsDsDbLevels({
        data: {
          levels: [this.devopsInsSelected.objAttr.dsEnvId, this.projectDevopsForm.instanceId],
          refreshCache: isRefreshCache
        }
      });

      if (res.success) {
        this.devopsInsCatalogList = res.data;
      }
    },
    async fetchSchemaList(isRefreshCache = false) {
      const levels = [this.devopsInsSelected.objAttr.dsEnvId, this.projectDevopsForm.instanceId];
      if (this.projectDevopsForm.devopsInsHasCatalog && this.projectDevopsForm.devopsInsHasSchema) {
        if (this.projectDevopsForm.catalogName === '') {
          this.$Message.error(this.$t('cao-zuo-shi-bai'));
          return;
        } else {
          levels.push(this.projectDevopsForm.catalogName);
        }
      }

      const res = await this.$services.dmProjectDevopsDsDbLevels({
        data: {
          levels,
          refreshCache: isRefreshCache
        }
      });

      if (res.success) {
        this.devopsInsSchemaList = res.data;
      }
    },
    fetchFormDsLevels() {
      const levels = [this.devopsInsSelected.objAttr.dsEnvId, this.projectDevopsForm.instanceId];
      if (this.projectDevopsForm.devopsInsHasCatalog) {
        levels.push(this.projectDevopsForm.catalogName);
      }
      if (this.projectDevopsForm.devopsInsHasSchema) {
        levels.push(this.projectDevopsForm.schemaName);
      }
      return levels;
    },
    async fetchImDefList() {
      if (this.imDefList.length !== 0) {
        return;
      }

      const res = await this.$services.dmDevopsImDefList();
      if (res.success) {
        this.imDefList = res.data;
        this.imDefList.unshift({
          helpUrl: '',
          imType: 'none',
          imTypeI18n: '禁用'
        });
      } else {
        this.imDefList = [
          {
            helpUrl: '',
            imType: 'none',
            imTypeI18n: '禁用'
          }
        ];
      }
    },
    async handleImDefOne(imDef = {}) {
      this.imDefSelected = imDef;
      if (imDef.imType === 'none') {
        this.imProviderList = [];
        this.projectImForm.imId = null;
        this.projectImForm.imType = 'none';
        return;
      }

      const res = await this.$services.dmDevopsImList({
        data: {
          imType: imDef.imType
        }
      });
      if (res.success) {
        this.imProviderList = res.data;
      }

      if (imDef.imType !== this.projectImForm.imType) {
        // just keep switch status.
        this.projectImForm = { ...this.projectImForm };
        this.projectImForm.imId = null;
        this.projectImForm.imType = this.imDefSelected.imType;
      }
    },
    async handleImProviderSelected(im) {
      this.projectImForm.imId = im;
      this.projectImForm.imType = this.imDefSelected.imType;
    },
    fetchProjectFlowDescription(type, option) {
      try {
        return PROJECT_FLOW_DESCRIPTION[type][option];
      } catch (e) {
        console.error(e);
        return '';
      }
    },
    //
    //
    //
    btnStatusShow() {
      return this.step !== PROJECT_STEP.BASIC2 && this.step !== PROJECT_STEP.FINISH;
    },
    handlePreviousStep() {
      if (this.step === PROJECT_STEP.BASIC1) {
        return;
      } else {
        this.step--;
      }

      if (this.step < PROJECT_STEP.S0 || this.step > PROJECT_STEP.S2) {
        this.projectDialogWidth = 520;
      }
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.fetchProjectList();
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.fetchProjectList();
    },
    async handleNextStep() {
      let isValid = false;
      if (this.$refs[`form${this.step}`]) {
        this.$refs[`form${this.step}`].validate((valid) => {
          if (valid) {
            if (this.step === PROJECT_STEP.S0) {
              this.$refs[`form${PROJECT_STEP.S1}`].validate((validS1) => {
                if (validS1) {
                  this.step += 1;
                  isValid = true;
                }
              });
              return;
            }
            this.step += 1;
            isValid = true;
          }
        });
      } else {
        this.step += 1;
        isValid = true;
      }

      if (!isValid) {
        return;
      }

      if (this.step < PROJECT_STEP.S0) {
        this.projectDialogWidth = 520;
      } else if (this.step === PROJECT_STEP.S0) {
        this.projectDialogWidth = 800;
        await this.fetchDevopsScmList();
        await this.fetchInsList();
      } else if (this.step === PROJECT_STEP.S1) {
        this.fetchFormDsLevels();
        await this.fetchImDefList();
        await this.handleImDefOne(this.imDefSelected);
      } else if (this.step === PROJECT_STEP.FINISH) {
        this.projectDialogWidth = 520;
      }
    },
    async handleAddProject(type = '') {
      this.createLoading = true;
      if (type !== 'empty') {
        this.projectForm = {
          projectName: this.projectBasicForm.projectName,
          projectDesc: this.projectBasicForm.projectDesc,
          projectOwnerUid: this.projectBasicForm.projectOwnerUid,
          option: {
            initScript: this.projectDevopsForm.initScript,
            checkStrategy: this.projectBasicForm.checkStrategy,
            approveStrategy: this.projectBasicForm.approveStrategy,
            executeStrategy: this.projectBasicForm.executeStrategy,
            errorStrategy: this.projectBasicForm.errorStrategy,
            transactional: this.projectBasicForm.transactional,
            retryWaitTime: null,
            retryCount: null
          },
          pipeline: {
            repoScmId: this.projectDevopsForm.repoScmId,
            repoScmUrl: this.projectDevopsForm.repoScmUrl,
            repoSpace: this.projectDevopsForm.repoSpace,
            repoName: this.projectDevopsForm.repoName,
            repoBranch: this.projectDevopsForm.repoBranch,
            repoScriptPath: this.projectDevopsForm.repoScriptPath,
            dsLevels: this.getLevelsRes(),
            eventType: this.projectDevopsForm.eventType
          },
          messenger:
            this.projectImForm.imType === 'none'
              ? null
              : {
                  imId: this.projectImForm.imId,
                  language: this.projectImForm.language,
                  eventProjectStatus: this.projectImForm.eventProjectStatus,
                  eventProjectConfig: this.projectImForm.eventProjectConfig,
                  eventChangeLife: this.projectImForm.eventChangeLife,
                  eventChangeNotice: this.projectImForm.eventChangeNotice
                }
        };
      } else {
        this.projectForm = {
          projectName: this.projectBasicForm.projectName,
          projectDesc: this.projectBasicForm.projectDesc,
          projectOwnerUid: this.projectBasicForm.projectOwnerUid,
          option: {
            initScript: this.projectDevopsForm.initScript,
            checkStrategy: this.projectBasicForm.checkStrategy,
            approveStrategy: this.projectBasicForm.approveStrategy,
            executeStrategy: this.projectBasicForm.executeStrategy,
            errorStrategy: this.projectBasicForm.errorStrategy,
            transactional: this.projectBasicForm.transactional,
            retryWaitTime: null,
            retryCount: null
          }
        };
      }

      const res = await this.$services.dmProjectCreate({
        data: type === 'empty' ? this.projectBasicForm : this.projectForm
      });

      if (res.success) {
        this.$Message.success('新建项目成功');
        this.projectId = res.data?.projectId || '';
        this.devopsId = res.data?.devopsId || '';
        this.webhook.url = res.data?.webHookUrl;
        this.webhook.repoUrl = res.data?.repoUrl;
        this.webhook.password = res.data?.webHookPwd;
        this.webhook.webHookHelpUrl = res.data?.webHookHelpUrl;
        if (type === 'empty') {
          this.handleCloseModal();
        }
        this.handleNextStep();
        this.fetchProjectList();
      }
      this.createLoading = false;
    },
    handleCloseModal() {
      this.resetFormRule();
      this.projectDialogShow = false;
      this.step = -1;
      this.selectedIm = '';
    },
    goDetail(row) {
      this.$router.push(`/project/${row?.projectId || 1}`);
    },
    getLevels() {
      return this.devopsInsSelected?.objAttr.dsEnvId === 'aa'
        ? [this.devopsInsSelected?.objAttr.dsEnvId, this.projectDevopsForm.instanceId, this.projectDevopsForm.databaseName]
        : [this.devopsInsSelected?.objAttr?.dsEnvId, this.projectDevopsForm.instanceId];
    },
    getLevelsRes() {
      return this.devopsInsSelected?.objAttr.dsEnvId === 'aa'
        ? [
            this.devopsInsSelected?.objAttr.dsEnvId,
            this.projectDevopsForm.instanceId,
            this.projectDevopsForm.databaseName,
            this.projectDevopsForm.schemaName
          ]
        : [this.devopsInsSelected?.objAttr?.dsEnvId, this.projectDevopsForm.instanceId, this.projectDevopsForm.schemaName];
    },
    resetFormRule() {
      const stepsToReset = [PROJECT_STEP.BASIC1, PROJECT_STEP.S0, PROJECT_STEP.S1];

      stepsToReset.forEach((step) => {
        const formRef = this.$refs[`form${step}`];
        if (formRef) {
          formRef.resetFields();
        }
      });

      this.devopsFrom = 'icon-v2-Save-fromCloud';
      this.devopsTo = 'icon-v2-shili';
    },
    jumpToWebhookDoc() {
      window.open(this.webhook?.webHookHelpUrl, '_blank');
    },
    goToAddScm() {
      this.$router.push('system/devops');
    },
    goToDsSetting() {
      this.$router.push('ccdatasource');
    },
    handleCopyTemp(item) {
      this.handleCopy(item);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleJumpUrl(item) {
      if (item !== '') {
        window.open(item, '_blank');
      }
    }
  }
};
</script>

<style lang="less" scoped>
.project-account {
  height: 100%;
}

.project {
  display: flex;
  flex-direction: column;
  height: 100%;

  .uid {
    display: flex;
    cursor: pointer;

    .copy {
      display: none;
    }

    &:hover {
      .copy {
        display: block;
      }
    }
  }

  .copy-account {
    display: flex;
    align-items: center;
    cursor: pointer;

    .square {
      width: 15px;
      height: 12px;
    }

    i {
      display: none;
    }

    &:hover {
      i {
        display: block;
      }

      .square {
        display: none;
      }
    }
  }

  .action {
    //button {
    //  margin-right: 12px;
    //}
    //.ivu-dropdown {
    //  padding: 0 7px;
    //}
    a {
      margin-right: 16px;
    }
  }

  .actions {
    font-size: 12px;
  }
}

.manage-role-modal {
  display: flex;

  .left {
    .title {
      margin-bottom: 10px;

      span {
        color: #888;

        &:first-child {
          color: #333;
          font-weight: bold;
          margin-right: 10px;
        }
      }
    }

    .role-table {
      display: flex;
      flex-direction: column;
      height: 400px;
      border: 1px solid rgba(234, 234, 234, 1);
    }
  }

  .new-role {
    flex: 1;
    padding: 20px;
  }
}

.new-subaccount-modal {
  .ivu-input-wrapper {
    width: 420px;
  }

  .title {
    font-weight: bold;
    font-size: 14px;
    margin-bottom: 18px;
  }
}

.rule-default-tag {
  display: flex;
  align-items: center;
}

.role-name-container {
  display: flex;
  align-items: center;
}

.img-wrap {
  min-width: 100px;
}

// Step Basic
.step-basic1,
.step-basic2 {
  width: 488px;
}
.step-basic1 {
  display: flex;
  height: 200px;

  .basic1-base {
    width: 250px;
  }
}
.step-basic2 {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 232px;

  .step-title {
    font-weight: bold;
  }

  .btn-wrap {
    display: flex;
    width: 350px;
    margin-top: 55px;
    justify-content: space-between;
  }

  .btn-wrap > div {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    width: 160px;
    text-align: center;
    padding: 15px;
    border: 1px solid #e8e8e8;
    border-radius: 10px;

    div:nth-child(1) {
      font-size: 16px;
    }

    div:nth-child(2) {
      font-size: 10px;
      color: #333;
    }
  }

  .btn-wrap > div:hover {
    border-color: #54b6f2;
    cursor: pointer;
  }
}

// Step Full
.step-0,
.step-1,
.step-2,
.step-3,
.step-4,
.step-5,
.step-6 {
  width: 700px;
  height: 350px;

  .form-base {
    width: 350px;
  }

  :deep(.ivu-form-item) {
    margin-bottom: 20px;
  }
  :deep(.ivu-form-item-error-tip) {
    padding-top: 2px;
  }
}

.step-0 {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;

  .s0-base {
    width: 230px;
  }

  .content {
    display: flex;
    justify-content: center;

    :deep(.ivu-radio-wrapper) {
      font-size: 12px;
    }

    .left {
      width: 350px;
      border-right: 1px solid #e8e8e8;
      border-bottom: 1px solid #e8e8e8;
    }
    .right {
      width: 350px;
      border-bottom: 1px solid #e8e8e8;
    }
  }
}
.footer-btn {
  margin-top: 10px;
}

.step-1 {
  display: flex;
  flex-direction: column;
  width: 100%;
  align-items: center;

  .im-group {
    display: flex;
    align-items: center;
    flex-direction: column;
  }

  :deep(.ivu-radio-wrapper) {
    font-size: 12px;
  }

  .im-list {
    margin-top: 10px;
    align-items: center;
    justify-content: center;
    display: flex;
    .im {
      cursor: pointer;
      width: 60px;
      height: 60px;
      margin: 0 10px;
      border: 1px solid #ccc;
      border-radius: 6px;
      margin-right: 5px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }

    .im-selected {
      border: 2px solid #43cf7c;
    }
  }
  .im-select {
    width: 450px;
    margin: 20px 40px 20px 30px;
  }

  .content {
    padding: 0 50px 50px 50px;
  }
}

.step-2 {
  width: 100%;
  display: flex;
  justify-content: center;
  .flow-drawer-steps {
    width: 100%;
    display: flex;
    justify-content: center;
    padding-left: 20px;
  }
  :deep(.ivu-steps-title) {
    width: 55px;
  }
}

.switch-text {
  margin-left: 10px;
}

.switch-btn {
  display: inline-block;
  margin-bottom: 10px;
}

.spinner {
  display: flex;
  margin-left: 5px;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-left-color: #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.flow-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}
.icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}
.icon img {
  width: 40px;
  height: 40px;
}
.flow-line {
  flex-shrink: 0;
  margin: 0 10px;
}

.flow-card {
  background: #fff;
  border: 1px solid #e8eaec;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
  padding: 16px;
  margin-bottom: 16px;
}

.flow-card .flow-drawer-step-tips {
  margin-top: 8px;
  color: #999;
  font-size: 12px;
}

.flow-section {
  margin-top: 16px;
}

.flow-section-label {
  font-weight: 500;
  margin-bottom: 8px;
  color: #333;
}

.im-item-wrap {
  display: flex;
  justify-content: space-between;
}

.select-warp {
  :deep(.ivu-select-dropdown) {
    max-height: 300px !important;
  }
}

.finish-wrap {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  :deep(.ant-input) {
    border-radius: 5px 0 0 5px;
  }
  :deep(.ant-btn) {
    border-radius: 0 5px 5px 0;
  }
}

.finish-title {
  display: flex;
  align-items: center;
  margin-bottom: 10px;

  .title-left {
    display: flex;
    align-items: center;
    justify-content: right;
    vertical-align: middle;
    font-weight: bold;
    margin-right: 5px;
  }
}

.title-text {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  justify-content: center;
}
</style>
