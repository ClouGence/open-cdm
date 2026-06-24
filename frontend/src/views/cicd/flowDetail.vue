<template>
  <div class="table-list-layout">
    <div class="table-list flow-wrap">
      <div class="content" v-if="flowInfo === null">
        <div class="empty">
          <img src="@/assets/not-exist.svg" class="empty_image" />
          {{ $t('bu-cun-zai-de-xiang-mu') }}
        </div>
      </div>
      <div class="content flow-detail-content" v-if="flowInfo !== null">
        <div class="detail-hero-grid">
          <section class="detail-card overview-card">
            <div class="overview-card-header">
              <div class="overview-title-row">
                <strong class="overview-flow-name">{{ flowInfo.flowName || '-' }}</strong>
                <span class="flow-status-pill" :class="flowStatusClass">{{ flowStatusText }}</span>
              </div>
              <div class="detail-toolbar">
                <Button class="detail-toolbar-btn" @click="showRecordPanel">
                  <span>{{ $t('bian-geng-ji-lu') }}</span>
                </Button>
                <Button
                  class="detail-toolbar-btn detail-trigger-btn"
                  type="primary"
                  :disabled="flowReadOnly || !primaryDevops || !primaryDevops.enable"
                  @click="triggerPrimaryChange"
                >
                  <span>{{ $t('chu-fa-bian-geng') }}</span>
                </Button>
                <Button
                  class="detail-toolbar-btn detail-snapshot-btn"
                  :disabled="flowReadOnly || !primaryDevops || !primaryDevops.enable"
                  @click="triggerPrimarySnapshot"
                >
                  <span>{{ $t('jian-li-kuai-zhao') }}</span>
                </Button>
              </div>
            </div>
            <div class="overview-meta-grid">
              <div class="overview-meta-item">
                <div class="overview-meta-copy">
                  <span>{{ $t('cicd-xiang-mu-code-colon') }}</span>
                  <Tooltip :content="flowInfo.flowUid || '-'">
                    <strong>{{ compactText(flowInfo.flowUid, 12) }}</strong>
                  </Tooltip>
                  <Icon class="inline-action-icon" type="ios-copy-outline" @click="handleCopyTemp(flowInfo.flowUid)" />
                </div>
              </div>
              <div class="overview-meta-item">
                <div class="overview-meta-copy">
                  <span>{{ $t('cicd-guan-li-yuan-colon') }}</span>
                  <strong>{{ flowManagerInfo.flowManagerName || '-' }}</strong>
                  <Icon class="inline-action-icon" type="ios-copy-outline" @click="handleCopyTemp(flowManagerInfo.flowManagerName || '-')" />
                </div>
              </div>
            </div>
            <div class="pipeline-overview">
              <div class="endpoint-card">
                <div class="endpoint-title">
                  <CustomIcon :type="scmIconType" size="24px" />
                  <span>{{ $t('cicd-git-cang-ku') }}</span>
                </div>
                <div class="endpoint-row">
                  <span>{{ $t('cicd-yuan-ma-cang-ku-colon') }}</span>
                  <Tooltip :content="repoNameText">
                    <strong>{{ compactText(repoNameText, 26) }}</strong>
                  </Tooltip>
                </div>
                <div class="endpoint-row">
                  <span>{{ $t('fen-zhi') }}：</span>
                  <Tooltip :content="repoBranchText">
                    <strong>{{ compactText(repoBranchText, 26) }}</strong>
                  </Tooltip>
                </div>
                <div class="endpoint-row">
                  <span>{{ $t('cicd-jiao-ben-lu-jin-colon') }}</span>
                  <Tooltip :content="repoScriptPathText">
                    <strong>{{ compactText(repoScriptPathText, 28) }}</strong>
                  </Tooltip>
                </div>
              </div>
              <div class="pipeline-link" aria-hidden="true">
                <span class="pipeline-dash"></span>
                <span class="pipeline-link-node">
                  <svg class="flow-link-arrows" viewBox="0 0 28 28" aria-hidden="true">
                    <path d="M7 14h14" />
                    <path d="m16.8 9.8 4.2 4.2-4.2 4.2" />
                  </svg>
                </span>
                <span class="pipeline-dash"></span>
              </div>
              <div class="endpoint-card">
                <div class="endpoint-title database-title">
                  <CustomIcon :type="primaryDevops?.dsType || 'icon-v2-DataBase2'" size="22px" />
                  <span>{{ $t('shu-ju-ku') }}</span>
                </div>
                <div class="endpoint-row">
                  <span>{{ $t('shu-ju-yuan-shi-li-0') }}</span>
                  <Tooltip :content="primaryDevops?.dsInstance || '-'">
                    <strong>{{ compactText(primaryDevops?.dsInstance, 22) }}</strong>
                  </Tooltip>
                </div>
                <div class="endpoint-row">
                  <span>{{ $t('schema') }}：</span>
                  <Tooltip :content="targetDatabaseText">
                    <strong>{{ compactText(targetDatabaseText, 22) }}</strong>
                  </Tooltip>
                </div>
              </div>
            </div>
          </section>
        </div>

        <section class="detail-card config-card">
          <div class="detail-card-title">{{ $t('cicd-pei-zhi-xiang') }}</div>
          <div class="config-list">
            <div class="config-row" v-for="item in configItems" :key="item.key">
              <div class="config-leading">
                <span class="config-name">{{ item.title }}</span>
              </div>
              <span class="config-status" :class="item.statusClass">{{ item.status }}</span>
              <div class="config-desc">{{ item.desc }}</div>
              <div class="config-actions">
                <span v-for="(action, actionIndex) in item.actions" :key="`${item.key}-${action.type}`" class="config-action-item">
                  <button type="button" class="config-action-link" @click="handleConfigAction(action.type)">
                    {{ action.label }}
                  </button>
                  <span v-if="actionIndex < item.actions.length - 1" class="config-action-divider"></span>
                </span>
              </div>
              <Icon class="config-arrow" type="ios-arrow-forward" />
            </div>
          </div>
        </section>
      </div>
    </div>
    <a-drawer
      :title="$t('git-ops-config')"
      width="420"
      class="drawer-wrap"
      :visible="imDialogDevOpsShow"
      :maskClosable="false"
      @close="handleCloseAllDrawer"
    >
      <div class="drawer-content">
        <Form :label-width="100" ref="formModal" :model="formModal" :rules="formRule">
          <!-- src -->
          <Divider orientation="left">
            <CustomIcon type="icon-v2-One" size="30px" />
            <div>{{ $t('yuan-tou-cang-ku-bian-geng') }}</div>
          </Divider>
          <FormItem :label="$t('fu-wu-shang')" prop="repoScmId" key="repoScmId">
            <Select v-model="formModal.repoScmId" class="flow-base" @on-change="handleDevopsScmSelected">
              <Option v-for="item in devopsScmList" :value="item.scmId" :key="item.scmId">
                <CustomIcon :type="item.scmType" rightMargin />
                {{ item.scmDisplay }}
              </Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('xuan-ze-cang-ku')" prop="repoName" key="repoName">
            <div style="display: flex; align-items: center">
              <Select class="flow-base" v-model="formModal.repoName" :disabled="!devopsScmSelected" @on-change="handleDevopsRepoSelected" filterable>
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
          <FormItem :label="$t('mu-biao-fen-zhi')" prop="repoBranch" key="repoBranch">
            <Input class="flow-base" type="text" v-model="formModal.repoBranch">
              <template #prefix>
                <CustomIcon type="icon-v2-branches" style="height: 100%" />
              </template>
            </Input>
          </FormItem>
          <FormItem :label="$t('jiao-ben-lu-jin')" prop="repoScriptPath" key="repoScriptPath">
            <Input class="flow-base" type="text" v-model="formModal.repoScriptPath">
              <template #prefix>
                <CustomIcon type="icon-v2-jiaobenrenwu" style="height: 100%" />
              </template>
            </Input>
            <br />
            <span>{{ $t('devops-script-hint') }}</span>
          </FormItem>
          <FormItem :label="$t('shi-jian-0')" key="eventType">
            <RadioGroup v-model="formModal.eventType">
              <Radio label="Push">{{ EVEN_TYPE_MAP.push }}</Radio>
              <Radio label="PullRequest">{{ EVEN_TYPE_MAP.pr }}</Radio>
            </RadioGroup>
          </FormItem>
          <!-- dst -->
          <Divider orientation="left">
            <CustomIcon type="icon-v2-Two" size="30px" />
            <div>{{ $t('mu-biao-fa-bu-shu-ju-ku') }}</div>
          </Divider>
          <FormItem :label="$t('shi-li-1')" prop="instanceId" key="instanceId">
            <Select v-model="formModal.instanceId" class="flow-base" @on-change="handleDevopsChangeIns" filterable>
              <Option v-for="ins in devopsInsList" :value="ins.objId" :key="ins.objId">
                <CustomIcon :type="ins?.objAttr?.dsType" style="margin-right: 5px" />
                {{ ins.objName }}
              </Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('shu-ju-ku')" prop="catalogName" v-show="devopsInsHasCatalog" key="catalogName">
            <Select v-model="formModal.catalogName" class="flow-base" :disabled="!devopsInsHasCatalog" @on-change="handleChangeCatalog" filterable>
              <Option v-for="catalog in devopsInsCatalogList" :value="catalog.objName" :key="catalog.objName">
                {{ catalog.objName }}
              </Option>
            </Select>
            <CustomIcon type="icon-v2-Refresh" @click="fetchCatalogList(true)" leftMargin v-if="!repoLoading" />
          </FormItem>
          <FormItem :label="$t('schema')" prop="schemaName" key="schemaName">
            <Select v-model="formModal.schemaName" class="flow-base" :disabled="!devopsInsHasSchema" filterable>
              <Option v-for="schema in devopsInsSchemaList" :value="schema.objName" :key="schema.objName">
                {{ schema.objName }}
              </Option>
            </Select>
            <CustomIcon type="icon-v2-Refresh" @click="fetchSchemaList(true)" leftMargin v-if="!repoLoading" />
          </FormItem>
          <!-- options -->
          <Divider orientation="left">
            <CustomIcon type="icon-v2-Three" size="30px" />
            <div>{{ $t('chu-shi-hua-fang-shi') }}</div>
          </Divider>
          <div style="padding-left: 50px">
            <RadioGroup v-model="formModal.initScript" size="small" type="button" key="initScript">
              <Radio label="Snapshot">{{ INIT_TYPE_MAP.snapshot }}</Radio>
              <Radio label="CreateChange">{{ INIT_TYPE_MAP.change }}</Radio>
              <Radio label="None">{{ INIT_TYPE_MAP.none }}</Radio>
            </RadioGroup>
            <div style="padding-top: 5px">
              {{ fetchFlowGitOpsDescription(formModal.initScript) }}
            </div>
          </div>
        </Form>
      </div>
      <div class="drawer-footer">
        <Button type="primary" @click="handleDevopsSubmit">{{ $t('wan-cheng') }}</Button>
      </div>
    </a-drawer>
    <CCModal v-model="imDialogDrawerShow" width="1040px" class="notify-config-modal-wrap" :maskClosable="false" @on-cancel="handleCloseAllDrawer">
      <div class="notify-config-modal">
        <div class="notify-config-title">{{ $t('tong-zhi-pei-zhi') }}</div>
        <div class="notify-config-layout">
          <div class="notify-config-left">
            <div class="notify-config-label required">{{ $t('tong-zhi-qu-dao') }}</div>
            <div class="notify-channel-list">
              <button
                v-for="im in imDefList"
                :key="im.imType"
                type="button"
                class="notify-channel-btn"
                :class="{ active: imDefSelected.imType === im.imType }"
                @click="handleImDefOne(im)"
              >
                <CustomIcon v-if="im.imType === 'none'" type="Disable" size="22px" />
                <CustomIcon v-else-if="im.iconResource" :resource="im.iconResource" :alt="im.imTypeI18n" size="22px" />
                <span>{{ im.imTypeI18n }}</span>
              </button>
            </div>
            <div class="notify-config-label service-label">{{ $t('im-fu-wu') }}</div>
            <Select
              class="notify-service-select"
              v-model="imProviderSelected.imId"
              :disabled="imDefSelected.imType === 'none'"
              @on-change="handleImProviderSelected"
              :placeholder="$t('qing-xuan-ze-yi-ge-im-ti-gong-zhe')"
              :not-found-text="$t('zan-wu-shu-ju')"
            >
              <template #prefix>
                <CustomIcon v-if="imDefSelected.imType === 'none'" type="Disable" rightMargin />
                <CustomIcon
                  v-else-if="imDefSelected.iconResource"
                  :resource="imDefSelected.iconResource"
                  :alt="imDefSelected.imTypeI18n"
                  size="20px"
                  rightMargin="5px"
                />
              </template>
              <Option v-for="item in imProviderList" :key="item.imId" :value="item.imId" :label="item.display" :disabled="!item.enable">
                <span>{{ item.display }}</span>
              </Option>
            </Select>
          </div>
          <div class="notify-config-right">
            <div class="notify-panel-title">{{ $t('ding-yue-xiao-xi') }}</div>
            <div class="notify-subscription-grid">
              <label class="notify-subscription-cell">
                <i-switch true-color="#52C41A" v-model="imProviderSelected.eventChangeFlowStatus" :disabled="imDefSelected.imType === 'none'" />
                <span>{{ $t('zhuang-tai-bian-geng-tong-zhi') }}</span>
              </label>
              <label class="notify-subscription-cell">
                <i-switch true-color="#52C41A" v-model="imProviderSelected.eventFlowConfig" :disabled="imDefSelected.imType === 'none'" />
                <span>{{ $t('pei-zhi-bian-geng-tong-zhi') }}</span>
              </label>
              <label class="notify-subscription-cell">
                <i-switch true-color="#52C41A" v-model="imProviderSelected.eventChangeLife" :disabled="imDefSelected.imType === 'none'" />
                <span>{{ $t('liu-cheng-tui-jin-tong-zhi') }}</span>
              </label>
              <label class="notify-subscription-cell">
                <i-switch true-color="#52C41A" v-model="imProviderSelected.eventChangeNotice" :disabled="imDefSelected.imType === 'none'" />
                <span>{{ $t('chu-li-zhuang-tai-tong-zhi') }}</span>
              </label>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCloseAllDrawer">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleImSubmit" :disabled="isImSubmitDisabled">{{ $t('bao-cun') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="imDialogFlowShow" width="960px" class="execution-config-modal-wrap" :maskClosable="false" @on-cancel="handleCloseAllDrawer">
      <div class="execution-config-modal">
        <div class="execution-config-title">{{ $t('zhi-xing-pei-zhi') }}</div>
        <div class="execution-config-list">
          <div class="execution-config-row">
            <div class="execution-config-name">
              <span>{{ $t('sql-jian-cha') }}</span>
              <Tooltip :content="fetchChangeFlowDescription('check', flowOption.checkStrategy)">
                <Icon type="ios-information-circle-outline" />
              </Tooltip>
            </div>
            <RadioGroup v-model="flowOption.checkStrategy" class="execution-config-options">
              <Radio label="Always">{{ SQL_REVIEW_MAP.always }}</Radio>
              <Radio label="Suggest">{{ SQL_REVIEW_MAP.suggest }}</Radio>
              <Radio label="Failure">{{ SQL_REVIEW_MAP.failure }}</Radio>
            </RadioGroup>
            <div class="execution-config-desc">{{ fetchChangeFlowDescription('check', flowOption.checkStrategy) }}</div>
          </div>
          <div class="execution-config-row">
            <div class="execution-config-name">
              <span>{{ $t('gong-dan-shen-pi') }}</span>
              <Tooltip :content="fetchChangeFlowDescription('approve', flowOption.approveStrategy)">
                <Icon type="ios-information-circle-outline" />
              </Tooltip>
            </div>
            <RadioGroup v-model="flowOption.approveStrategy" class="execution-config-options">
              <Radio label="Enable">{{ APPROVE_MAP.Enable }}</Radio>
              <Radio label="Disable">{{ APPROVE_MAP.Disable }}</Radio>
            </RadioGroup>
            <div class="execution-config-desc">{{ fetchChangeFlowDescription('approve', flowOption.approveStrategy) }}</div>
          </div>
          <div class="execution-config-row">
            <div class="execution-config-name">
              <span>{{ $t('fa-bu-bian-geng') }}</span>
              <Tooltip :content="fetchChangeFlowDescription('execute', flowOption.executeStrategy)">
                <Icon type="ios-information-circle-outline" />
              </Tooltip>
            </div>
            <RadioGroup v-model="flowOption.executeStrategy" class="execution-config-options" @on-change="handleFlowOfExecuteOption">
              <Radio label="Auto">{{ PUBLISH_MAP.auto }}</Radio>
              <Radio label="Manual">{{ PUBLISH_MAP.manual }}</Radio>
              <Radio label="Disabled">{{ PUBLISH_MAP.disabled }}</Radio>
            </RadioGroup>
            <div class="execution-config-desc">{{ fetchChangeFlowDescription('execute', flowOption.executeStrategy) }}</div>
          </div>
          <div class="execution-config-row">
            <div class="execution-config-name">
              <span>{{ $t('shi-wu') }}</span>
              <Tooltip :content="flowTransactionalDescription">
                <Icon type="ios-information-circle-outline" />
              </Tooltip>
            </div>
            <RadioGroup v-model="flowOption.transactional" class="execution-config-options">
              <Radio label="Enable" :disabled="!flowExecuteIsAuto">{{ APPROVE_MAP.Enable }}</Radio>
              <Radio label="Disable" :disabled="!flowExecuteIsAuto">{{ APPROVE_MAP.Disable }}</Radio>
            </RadioGroup>
            <div class="execution-config-desc">{{ flowTransactionalDescription }}</div>
          </div>
          <div class="execution-config-row">
            <div class="execution-config-name">
              <span>{{ $t('cuo-wu-ce-lve') }}</span>
              <Tooltip :content="fetchChangeFlowDescription('error', flowOption.errorStrategy)">
                <Icon type="ios-information-circle-outline" />
              </Tooltip>
            </div>
            <RadioGroup v-model="flowOption.errorStrategy" class="execution-config-options">
              <Radio label="NONE" :disabled="!flowExecuteIsAuto">{{ ERROR_STRATEGY_MAP.abort }}</Radio>
              <Radio label="RETRY" :disabled="!flowExecuteIsAuto">{{ ERROR_STRATEGY_MAP.retry }}</Radio>
              <Radio label="SKIP" :disabled="!flowExecuteIsAuto">{{ ERROR_STRATEGY_MAP.ignore }}</Radio>
            </RadioGroup>
            <div class="execution-config-desc">{{ fetchChangeFlowDescription('error', flowOption.errorStrategy) }}</div>
          </div>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCloseAllDrawer">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleOptionSubmit">{{ $t('bao-cun') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showTriggerModal" width="860px" class="trigger-config-modal-wrap">
      <div class="trigger-config-modal">
        <div class="trigger-config-title">{{ $t('chu-fa-pei-zhi') }}</div>
        <Tabs v-model="triggerTab" class="config-modal-tabs" size="small" :animated="false" type="line">
          <TabPane :label="triggerTabLabel('WebHook')" name="WebHook">
            <div class="config-section-heading">{{ $t('cha-kan-xiang-mu-webhook') }}</div>
            <div class="config-modal-list">
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('webhook-config') }}</div>
                <div class="config-modal-control">
                  <i-switch true-color="#52C41A" v-model="trigger.hookEnable" />
                </div>
                <div class="config-modal-desc">{{ trigger.hookEnable ? $t('yi-kai-qi') : $t('wei-qi-yong') }}</div>
              </div>
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('cang-ku') }}</div>
                <div class="config-modal-control">
                  <Input v-model="trigger.hookRepoUrl" readonly>
                    <template #suffix>
                      <Icon type="ios-link" @click="handleJumpUrl(trigger.hookRepoUrl)" />
                    </template>
                  </Input>
                </div>
                <div class="config-modal-desc">{{ $t('cicd-devops-git-repository') }}</div>
              </div>
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('webhook-url') }}</div>
                <div class="config-modal-control">
                  <Input v-model="trigger.hookUrl" readonly :disabled="!trigger.hookEnable">
                    <template #suffix>
                      <Icon type="ios-copy" @click="handleCopyTemp(trigger.hookUrl)" />
                    </template>
                  </Input>
                </div>
                <div class="config-modal-desc">{{ $t('cicd-webhook-url-desc') }}</div>
              </div>
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('webhook-mi-ma') }}</div>
                <div class="config-modal-control">
                  <Input v-model="trigger.hookPassword" readonly :disabled="!trigger.hookEnable">
                    <template #suffix>
                      <Icon type="ios-copy" @click="handleCopyTemp(trigger.hookPassword)" />
                    </template>
                  </Input>
                </div>
                <div class="config-modal-desc">{{ $t('cicd-webhook-password-desc') }}</div>
              </div>
            </div>
          </TabPane>
          <TabPane :label="triggerTabLabel('TriggerUrl')" name="TriggerUrl">
            <div class="config-section-heading">{{ $t('cha-kan-xiang-mu-chu-fa') }}</div>
            <div class="config-modal-list">
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('webhook-config') }}</div>
                <div class="config-modal-control">
                  <i-switch true-color="#52C41A" v-model="trigger.triggerEnable" />
                </div>
                <div class="config-modal-desc">{{ trigger.triggerEnable ? $t('yi-kai-qi') : $t('wei-qi-yong') }}</div>
              </div>
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('token') }}</div>
                <div class="config-modal-control">
                  <Input v-model="trigger.triggerToken" readonly>
                    <template #suffix>
                      <Icon type="ios-copy" @click="handleCopyTemp(trigger.triggerToken)" />
                    </template>
                  </Input>
                </div>
                <div class="config-modal-desc">{{ $t('cicd-trigger-token-desc') }}</div>
              </div>
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('shi-yong-fang-shi') }}</div>
                <div class="config-modal-control">
                  <RadioGroup
                    v-model="trigger.triggerMethod"
                    class="config-modal-radio"
                    type="button"
                    size="small"
                    @on-change="handleTriggerUrlBuild"
                  >
                    <Radio label="http">HTTP(GET)</Radio>
                    <Radio label="wget">wget</Radio>
                    <Radio label="curl">curl</Radio>
                  </RadioGroup>
                </div>
                <div class="config-modal-desc">{{ $t('cicd-trigger-method-desc') }}</div>
              </div>
              <div class="config-modal-row">
                <div class="config-modal-label">{{ $t('format') }}</div>
                <div class="config-modal-control">
                  <RadioGroup
                    v-model="trigger.triggerFormat"
                    class="config-modal-radio"
                    type="button"
                    size="small"
                    @on-change="handleTriggerUrlBuild"
                  >
                    <Radio label="text">{{ $t('text') }}</Radio>
                    <Radio label="json">{{ $t('json') }}</Radio>
                  </RadioGroup>
                </div>
                <div class="config-modal-desc">{{ $t('cicd-trigger-format-desc') }}</div>
              </div>
              <div class="config-modal-row config-modal-row-wide">
                <div class="config-modal-label">{{ $t('yuan-cheng-chu-fa') }}</div>
                <div class="config-modal-control">
                  <Input v-model="trigger.triggerUrlShow" readonly :disabled="!trigger.triggerEnable">
                    <template #suffix>
                      <Icon type="ios-copy" @click="handleCopyTemp(trigger.triggerUrlShow)" />
                    </template>
                  </Input>
                </div>
                <div class="config-modal-desc">{{ $t('cicd-trigger-url-desc') }}</div>
              </div>
            </div>
          </TabPane>
        </Tabs>
      </div>
      <template #footer>
        <div class="config-modal-footer">
          <Button @click="showTriggerModal = false">{{ $t('qu-xiao') }}</Button>
          <Button @click="handleJumpUrl(triggerTab === 'WebHook' ? trigger.hookHelpUrl : 'https://www.clougence.com/dm-doc/devops/devops_trigger')">
            {{ $t('cha-kan-wen-dang') }}
          </Button>
          <Button type="primary" @click="handleSaveTrigger">{{ $t('bao-cun') }}</Button>
        </div>
      </template>
    </CCModal>
    <CCModal v-model="showCallbackModal" width="780px" class="callback-config-modal-wrap">
      <div class="callback-config-modal">
        <div class="callback-config-title">{{ $t('callback-pei-zhi') }}</div>
        <div class="callback-config-summary">
          <CustomIcon :type="`${callbackData.enable ? 'icon-v2-SuccessColorful' : 'icon-v2-InfoColorful'}`" size="38px" rightMargin="12px" />
          <span>{{ $t('she-zhi-hui-diao-di-zhi-desc') }}</span>
        </div>
        <div class="config-modal-list">
          <div class="config-modal-row callback-config-row">
            <div class="config-modal-label">{{ $t('callback-status') }}</div>
            <div class="config-modal-control">
              <i-switch true-color="#52C41A" v-model="callbackData.enable" />
            </div>
            <div class="config-modal-desc">{{ callbackData.enable ? $t('yi-kai-qi') : $t('wei-qi-yong') }}</div>
          </div>
          <div class="config-modal-row callback-config-row">
            <div class="config-modal-label">{{ $t('callback-method') }}</div>
            <div class="config-modal-control">
              <RadioGroup v-model="callbackData.method" class="config-modal-radio" type="button" size="small">
                <Radio label="POST" :disabled="!callbackData.enable">POST</Radio>
                <Radio label="GET" :disabled="!callbackData.enable">GET</Radio>
              </RadioGroup>
            </div>
            <div class="config-modal-desc">{{ $t('cicd-callback-method-desc') }}</div>
          </div>
          <div class="config-modal-row callback-config-row config-modal-row-wide">
            <div class="config-modal-label">{{ $t('callback-url') }}</div>
            <div class="config-modal-control">
              <Input v-model="callbackData.url" placeholder="http://... or https://..." :disabled="!callbackData.enable">
                <template #suffix>
                  <Icon type="ios-copy" @click="handleCopyTemp(callbackData.url)" />
                </template>
              </Input>
            </div>
            <div class="config-modal-desc">{{ $t('cicd-callback-url-desc') }}</div>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="config-modal-footer">
          <Button @click="showCallbackModal = false">{{ $t('qu-xiao') }}</Button>
          <Button @click="handleJumpUrl('https://clougence.com/dm-doc/devops/devops_callback')">{{ $t('cha-kan-wen-dang') }}</Button>
          <Button type="primary" @click="handleSaveCallBack">{{ $t('bao-cun') }}</Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { mapGetters, mapState } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import { encryptMixin } from '@/mixins/encryptMixin';
import { handleCopy } from '@/utils/clipboard';
import {
  APPROVE_MAP,
  BECOME_STATUS_MAP,
  CHANGE_FLOW_DESCRIPTION,
  CHANGE_STATUS_MAP,
  defaultLanguageMap,
  ERROR_STRATEGY_MAP,
  EVEN_TYPE_MAP,
  FLOW_MARK_MAP,
  FLOW_STEP_NUM,
  flowDetailTableColumns,
  formRule,
  GITOPS_DESCRIPTION,
  IM_PROVIDER_MAP,
  INIT_TYPE_MAP,
  PUBLISH_MAP,
  SQL_REVIEW_MAP
} from './constant';
import { DEFAULT_FLOW_OPTION, groupByRepoNamespace } from './utils';

export default {
  name: 'cicd-flow',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'dmGlobalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['isSaas']),
    // Determines whether the IM configuration complete button should be disabled
    isImSubmitDisabled() {
      // Buttons are not disabled if the disabled type is selected
      if (this.imProviderSelected?.imType === 'none') {
        return false;
      }
      // Button disabled if non-disable type selected but no specific provider selected
      if (this.imProviderSelected?.imType !== 'none' && !this.imProviderSelected?.imId) {
        return true;
      }
      return false;
    },
    primaryDevops() {
      return this.devopsList?.[0] || null;
    },
    scmIconType() {
      return this.primaryDevops?.scmType || 'icon-v2-Gitee';
    },
    repoNameText() {
      return this.primaryDevops?.repoName || '-';
    },
    repoBranchText() {
      return this.primaryDevops?.repoBranch || '-';
    },
    repoScriptPathText() {
      return this.primaryDevops?.repoScriptPath || '-';
    },
    targetDatabaseText() {
      if (!this.primaryDevops) {
        return '-';
      }
      const target = this.envDevOpsTarget(this.primaryDevops);
      return target && target !== 'Unknown' ? target : '-';
    },
    flowStatusText() {
      if (this.flowInfo?.flowStatus === 'DELETE') {
        return this.$t('yi-shan-chu');
      }
      if (this.flowInfo?.flowStatus === 'ARCHIVE') {
        return this.$t('cicd-yi-gui-dang');
      }
      if (this.primaryDevops && !this.primaryDevops.enable) {
        return this.$t('yi-jin-yong');
      }
      return this.$t('cicd-qi-yong-zhong');
    },
    flowStatusClass() {
      if (this.flowInfo?.flowStatus === 'DELETE') {
        return 'danger';
      }
      if (this.flowInfo?.flowStatus === 'ARCHIVE' || (this.primaryDevops && !this.primaryDevops.enable)) {
        return 'muted';
      }
      return 'success';
    },
    devopsStatusText() {
      if (!this.primaryDevops) {
        return this.$t('cicd-wei-pei-zhi');
      }
      return this.primaryDevops.enable ? this.$t('cicd-qi-yong-zhong') : this.$t('yi-jin-yong');
    },
    imStatusText() {
      return this.imProviderInfo?.imType && this.imProviderInfo.imType !== 'none' ? this.$t('yi-kai-qi') : this.$t('cicd-wei-kai-qi');
    },
    flowConfigStatusText() {
      return this.flowInfo?.flowCheck || this.flowInfo?.flowApprove || this.flowInfo?.flowExecute
        ? this.$t('cicd-yi-pei-zhi')
        : this.$t('cicd-wei-pei-zhi');
    },
    flowTransactionalDescription() {
      return this.fetchChangeFlowDescription('transactional', this.flowOption.transactional === 'Enable');
    },
    triggerConfigured() {
      return !!(this.primaryDevops && (this.primaryDevops.webHookEnable || this.primaryDevops.triggerEnable));
    },
    callbackConfigured() {
      return !!(this.primaryDevops && this.primaryDevops.callbackEnable);
    },
    imConfigured() {
      return this.imProviderInfo?.imType && this.imProviderInfo.imType !== 'none';
    },
    recentOperationTime() {
      return this.changeList?.[0]?.changeTime || this.flowInfo?.createTime || '-';
    },
    configItems() {
      const statusClass = (configured) => (configured ? 'success' : 'muted');
      const statusText = (configured) => (configured ? this.$t('cicd-yi-pei-zhi') : this.$t('cicd-wei-pei-zhi'));

      return [
        {
          key: 'trigger',
          title: this.$t('chu-fa-pei-zhi'),
          status: statusText(this.triggerConfigured),
          statusClass: statusClass(this.triggerConfigured),
          desc: this.$t('cicd-pei-zhi-chu-fa-tiao-jian-yu-zhi-xing-ce-lve'),
          actions: [{ label: this.$t('cha-kan-pei-zhi'), type: this.triggerConfigured ? 'viewTrigger' : 'editTrigger' }]
        },
        {
          key: 'callback',
          title: this.$t('callback-pei-zhi'),
          status: statusText(this.callbackConfigured),
          statusClass: statusClass(this.callbackConfigured),
          desc: this.$t('cicd-pei-zhi-bian-geng-jie-guo-hui-diao-tong-zhi'),
          actions: [{ label: this.$t('cha-kan-pei-zhi'), type: 'editCallback' }]
        },
        {
          key: 'flow',
          title: this.$t('zhi-xing-pei-zhi'),
          status: this.primaryDevops ? this.$t('cicd-yi-pei-zhi') : this.$t('cicd-wei-pei-zhi'),
          statusClass: this.primaryDevops ? 'success' : 'muted',
          desc: this.$t('cicd-pei-zhi-fa-bu-bu-zhou-yu-shen-pi-liu-cheng'),
          actions: [
            {
              label: this.$t('cha-kan-pei-zhi'),
              type: this.primaryDevops ? 'viewFlow' : 'addGitOps'
            }
          ]
        },
        {
          key: 'im',
          title: this.$t('im-xiao-xi'),
          status: this.imConfigured ? this.$t('yi-kai-qi') : this.$t('cicd-wei-kai-qi'),
          statusClass: this.imConfigured ? 'success' : 'muted',
          desc: this.$t('cicd-pei-zhi-im-tong-zhi-jie-shou-yu-nei-rong-mo-ban'),
          actions: [{ label: this.$t('cha-kan-pei-zhi'), type: 'editIm' }]
        }
      ];
    }
  },
  data() {
    return {
      loading: false,
      flowId: '',
      flowInfo: null,
      flowReadOnly: true,
      flowManagerInfo: {
        flowManagerName: null,
        flowManagerUid: null
      },
      devopsList: [],
      devopsListEmpty: true,
      devopsUsersList: [],
      //
      imDialogDrawerShow: false,
      imDefList: [],
      imDefSelected: {
        imId: '',
        imType: 'none'
      },
      imProviderList: [],
      imProviderInfo: this.initImProviderInfo(),
      imProviderSelected: this.initImProviderInfo(),
      //
      imDialogFlowShow: false,
      flowExecuteIsAuto: true,
      flowOption: DEFAULT_FLOW_OPTION,
      //
      imDialogDevOpsShow: false,
      devopsScmList: [],
      devopsScmSelected: null,
      devopsRepoList: [],
      devopsRepoSelected: null,
      devopsInsSelected: null,
      devopsInsHasCatalog: false,
      devopsInsHasSchema: false,
      defaultLanguageMap,
      devopsInsList: [],
      devopsInsCatalogList: [],
      devopsInsSchemaList: [],
      formModal: {
        initScript: 'CreateChange'
      },
      formRule,
      repoLoading: false,
      //
      showTriggerModal: false,
      triggerTab: 'WebHook',
      triggerOriginal: {},
      trigger: {
        flowId: 0,
        hookEnable: false,
        hookUrl: '',
        hookPassword: '',
        hookRepoUrl: '',
        hookHelpUrl: '',
        triggerEnable: false,
        triggerUrl: '',
        triggerUrlShow: '',
        triggerToken: '',
        triggerMethod: 'http',
        triggerFormat: 'json'
      },
      showCallbackModal: false,
      callbackData: {
        flowId: 0,
        enable: false,
        method: 'POST',
        url: ''
      },
      //
      changeList: [],
      pageTotal: null,
      pageNum: 1,
      pageSize: 10,
      keyword: '',
      devopsRepoListByGroup: [],
      //
      flowDetailTableColumns,
      IM_PROVIDER_MAP,
      FLOW_MARK_MAP,
      FLOW_STEP_NUM,
      BECOME_STATUS_MAP,
      INIT_TYPE_MAP,
      APPROVE_MAP,
      SQL_REVIEW_MAP,
      PUBLISH_MAP,
      CHANGE_FLOW_DESCRIPTION,
      GITOPS_DESCRIPTION,
      ERROR_STRATEGY_MAP,
      EVEN_TYPE_MAP,
      CHANGE_STATUS_MAP
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    groupByRepoNamespace,
    handleCopy,
    init() {
      this.flowId = this.$route.params.id;
      this.fetchImDefList();
      this.fetchDetail(() => {
        this.fetchDetailApply();
        this.fetchMsgInfo();
        this.fetchListDevops();

        this.fetchChangeList();
      });
    },
    initImProviderInfo() {
      return {
        imId: '',
        imType: 'none',
        language: 'zh',
        eventChangeFlowStatus: false,
        eventFlowConfig: false,
        eventChangeLife: false,
        eventChangeNotice: false
      };
    },
    // inner methods
    async fetchDetail(successCall) {
      this.loading = true;
      const res = await this.$services.dmCicdFlowDetail({
        data: { flowId: this.flowId }
      });

      if (res.success) {
        this.flowInfo = res.data;
      }
      this.loading = false;

      if (successCall) {
        successCall(this.flowInfo);
      }
    },
    async fetchDetailApply() {
      this.flowReadOnly = this.flowInfo.flowStatus === 'DELETE' || this.flowInfo.flowStatus === 'ARCHIVE';
      this.flowInfo.mark = this.flowInfo.mark || 'CircleGray';
      this.flowManagerInfo = {
        flowManagerName: this.flowInfo.flowManagerName,
        flowManagerUid: this.flowInfo.flowManagerUid
      };
      this.fetchDetailFlowOptionApply();
    },
    fetchDetailFlowOptionApply() {
      this.flowOption = {
        checkStrategy: this.flowInfo.flowCheck,
        approveStrategy: this.flowInfo.flowApprove,
        executeStrategy: this.flowInfo.flowExecute,
        errorStrategy: this.flowInfo.options?.errorStrategy || 'NONE',
        transactional: (this.flowInfo.options?.transactional ? 'Enable' : 'Disable') || 'Disable'
      };
      if (this.flowOption.executeStrategy === 'Auto') {
        this.flowExecuteIsAuto = true;
      } else {
        this.flowExecuteIsAuto = false;
        this.flowOption.errorStrategy = '';
        this.flowOption.transactional = '';
      }
    },
    async fetchMsgInfo() {
      const res = await this.$services.dmCicdFlowFetchImConfig({
        data: {
          flowId: this.flowId
        }
      });
      if (res.success) {
        const data = res.data || this.initImProviderInfo();
        this.imDefSelected = {
          imId: data.imId,
          imType: data.imType
        };
        this.imProviderInfo = { ...data };
        this.imProviderSelected = { ...data };
      }
    },
    async fetchListDevops() {
      this.loading = true;
      const res = await this.$services.dmCicdFlowDevopsList({
        data: {
          flowId: this.flowId
        }
      });
      if (res.success) {
        this.devopsList = res.data;
        this.devopsListEmpty = this.devopsList.length === 0;
      }
      this.loading = false;
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
    async fetchDevopsUsersList() {
      this.loading = true;
      const res = await this.$services.dmCicdDevopsUsers({
        data: { search: '' }
      });
      this.loading = false;

      if (res.success) {
        this.devopsUsersList = res.data;
      } else {
        this.devopsUsersList = [];
      }
    },
    fetchDevopsUsersListValueOfValue(u) {
      return u.userUid;
    },
    fetchDevopsUsersListValueOfLabel(u) {
      return u.userName;
    },
    fetchChangeFlowDescription(type, option) {
      if (type === '' || option === '') {
        return this.$t('zan-wu-miao-shu');
      }
      try {
        return CHANGE_FLOW_DESCRIPTION[type][option];
      } catch (e) {
        console.error(e);
        return this.$t('zan-wu-miao-shu');
      }
    },
    //
    // handle methods
    async handleFlowMark(key) {
      const res = await this.$services.dmCicdFlowUpdate({
        data: {
          flowId: this.flowId,
          newMark: key
        }
      });
      if (res.success) {
        this.flowInfo.mark = key;
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleNameEdit(data) {
      const res = await this.$services.dmCicdFlowUpdate({
        data: {
          flowId: this.flowId,
          newName: data
        }
      });
      if (res.success) {
        this.flowInfo.flowName = data;
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleAdminEdit(data) {
      const res = await this.$services.dmCicdFlowUpdate({
        data: {
          flowId: this.flowId,
          newAdminUid: data.value
        }
      });
      if (res.success) {
        this.flowManagerInfo = {
          flowManagerName: data.label,
          flowManagerUid: data.value
        };
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleOperate(type) {
      let fn;
      switch (type) {
        case 'delate':
          this.$Modal.confirm({
            title: this.$t('que-ren-shan-chu-xiang-mu'),
            onOk: async () => {
              const res = await this.$services.dmCicdFlowDelete({
                data: {
                  flowId: this.flowId
                }
              });
              if (res.success) {
                this.$Message.success(this.$t('cao-zuo-cheng-gong'));
                this.init();
              }
            }
          });
          return;
        case 'archive':
          fn = this.$services.dmCicdFlowArchive;
          break;
        case 'resetArchive':
          fn = this.$services.dmCicdFlowRecover;
          break;
        default:
          return;
      }
      const res = await fn({
        data: {
          flowId: this.flowId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.init();
      }
    },
    //
    async handleImEdit() {
      if (!this.flowReadOnly) {
        await this.fetchImDefList();
        await this.handleImDefOne(this.imDefSelected);
        this.imDialogDrawerShow = true;
      }
    },
    providerIconResource(imType) {
      return this.imDefList.find((item) => item.imType === imType)?.iconResource || '';
    },
    async handleImDefOne(imDef = {}) {
      this.imDefSelected = imDef;
      if (imDef.imType === 'none') {
        this.imProviderList = [];
        this.imProviderSelected.imId = null;
        this.imProviderSelected.imType = this.imDefSelected.imType;
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

      this.imProviderSelected = { ...this.imProviderInfo };
      if (imDef.imType !== this.imProviderInfo.imType) {
        // just keep switch status.
        this.imProviderSelected = { ...this.imProviderInfo };
        this.imProviderSelected.imId = null;
        this.imProviderSelected.imType = this.imDefSelected.imType;
      }
    },
    async handleImProviderSelected(im) {
      this.imProviderSelected.imId = im;
      this.imProviderSelected.imType = this.imDefSelected.imType;
    },
    async handleImSubmit() {
      let res;
      if (this.imProviderSelected?.imType === 'none' || (this.imProviderSelected?.imType !== 'none' && this.imProviderSelected?.imId)) {
        res = await this.$services.dmCicdFlowPushImConfig({
          data: {
            flowId: this.flowId,
            ...this.imProviderSelected,
            imId: this.imProviderSelected.imType === 'none' ? null : this.imProviderSelected.imId,
            imType: this.imProviderSelected.imType === 'none' ? null : this.imProviderSelected.imType,
            delete: this.imProviderSelected.imType === 'none'
          }
        });
      }
      if (res?.success) {
        await this.fetchMsgInfo();
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.handleCloseAllDrawer();
      } else {
        // this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    //
    async handleFlowEdit() {
      if (!this.flowReadOnly) {
        this.fetchDetailFlowOptionApply();
        this.imDialogFlowShow = true;
      }
    },
    handleFlowOfExecuteOption() {
      if (this.flowOption.executeStrategy === 'Auto') {
        this.flowOption.transactional = (this.flowInfo.options?.transactional ? 'Enable' : 'Disable') || 'Disable';
        this.flowOption.errorStrategy = this.flowInfo.options?.errorStrategy || 'NONE';
        this.flowExecuteIsAuto = true;
      } else {
        this.flowExecuteIsAuto = false;
        this.flowOption.errorStrategy = '';
        this.flowOption.transactional = '';
      }
    },
    async handleOptionSubmit() {
      const res = await this.$services.dmCicdFlowPushFlowConfig({
        data: {
          flowId: this.flowId,
          ...this.flowOption,
          transactional: this.flowOption.transactional === 'Enable'
        }
      });
      if (res.success) {
        await this.fetchDetail(() => this.fetchDetailFlowOptionApply());
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.handleCloseAllDrawer();
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    //
    handleGitOpsAdd() {
      if (!this.flowReadOnly) {
        this.$router.push(`/cicd/${this.flowId}/release-flow/add`);
      }
    },
    fetchFlowGitOpsDescription(option) {
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
    compactText(value, maxLength = 16) {
      const text = value || '-';
      if (text.length <= maxLength) {
        return text;
      }
      return `${text.slice(0, maxLength)}...`;
    },
    showRecordPanel() {
      this.$router.push(`/cicd/${this.flowId}/change-records`);
    },
    triggerPrimaryChange() {
      if (this.primaryDevops) {
        this.triggerChange(this.primaryDevops);
      }
    },
    triggerPrimarySnapshot() {
      if (this.primaryDevops) {
        this.triggerSnapshot(this.primaryDevops);
      }
    },
    handlePrimaryDevopsSwitch() {
      if (!this.primaryDevops) {
        return;
      }
      if (this.primaryDevops.enable) {
        this.disableDevops(this.primaryDevops);
      } else {
        this.enableDevops(this.primaryDevops);
      }
    },
    handleConfigAction(type) {
      if (['viewTrigger', 'editTrigger'].includes(type)) {
        if (this.primaryDevops) {
          this.openTriggerConfig(this.primaryDevops);
        } else {
          this.handleGitOpsAdd();
        }
        return;
      }
      if (type === 'editCallback') {
        if (this.primaryDevops) {
          this.openCallBack(this.primaryDevops);
        } else {
          this.handleGitOpsAdd();
        }
        return;
      }
      if (type === 'viewFlow') {
        this.handleFlowEdit();
        return;
      }
      if (type === 'addGitOps') {
        this.handleGitOpsAdd();
        return;
      }
      if (type === 'editIm') {
        this.handleImEdit();
      }
    },
    async fetchDevopsScmList() {
      const res = await this.$services.dmCicdDevopsScmList();

      if (res.success) {
        this.devopsScmList = res.data;
      }
    },
    async handleDevopsScmSelected() {
      this.devopsScmSelected = this.devopsScmList.find((scm) => scm.scmId === this.formModal.repoScmId);
      if (this.formModal.repoScmId) {
        await this.fetchDevopsScmRepos();
      }
    },
    async fetchDevopsScmRepos() {
      this.repoLoading = true;
      const res = await this.$services.dmCicdDevopsRepos({
        data: {
          scmId: this.formModal.repoScmId
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
      this.devopsRepoSelected = this.devopsRepoList.find((repo) => repo.repoName === this.formModal.repoName);
      if (this.devopsRepoSelected) {
        this.formModal.repoScmUrl = this.devopsRepoSelected.repoUrl;
        this.formModal.repoBranch = this.devopsRepoSelected.repoBranch;
        this.formModal.repoSpace = this.devopsRepoSelected.repoSpace;
      }
    },
    async handleDevopsChangeIns() {
      this.devopsInsCatalogList = [];
      this.devopsInsSchemaList = [];
      this.formModal.catalogName = '';
      this.formModal.schemaName = '';
      if (this.formModal.instanceId === '') {
        return;
      }

      this.devopsInsSelected = this.devopsInsList.find((ins) => ins.objId === this.formModal.instanceId);
      if (!this.devopsInsSelected) {
        return;
      }

      const dsConf = this.dmGlobalSetting.dsSettingDef[this.devopsInsSelected?.objAttr?.dsType];
      this.devopsInsHasCatalog = dsConf.categories.levels.includes('CATALOG');
      this.devopsInsHasSchema = dsConf.categories.levels.includes('SCHEMA');

      if (this.devopsInsHasCatalog) {
        await this.fetchCatalogList();
      } else if (this.devopsInsHasSchema) {
        await this.fetchSchemaList();
      }
    },
    async handleChangeCatalog() {
      this.formModal.schemaName = '';
      await this.fetchSchemaList();
    },
    async fetchInsList() {
      const res = await this.$services.dmCicdDevopsDsInsLevels();

      if (res.success) {
        this.devopsInsList = res.data;
      }
    },
    async fetchCatalogList(isRefreshCache = false) {
      if (!this.devopsInsSelected || !this.devopsInsSelected?.objAttr?.dsEnvId) {
        return;
      }

      const res = await this.$services.dmCicdDevopsDsDbLevels({
        data: {
          levels: [this.devopsInsSelected?.objAttr?.dsEnvId, this.formModal.instanceId].filter(Boolean),
          refreshCache: isRefreshCache
        }
      });

      if (res.success) {
        this.devopsInsCatalogList = res.data;
      }
    },
    async fetchSchemaList(isRefreshCache = false) {
      if (!this.devopsInsSelected || !this.devopsInsSelected?.objAttr?.dsEnvId) {
        return;
      }
      const levels = [this.devopsInsSelected?.objAttr?.dsEnvId, this.formModal.instanceId];
      if (this.devopsInsHasCatalog && this.devopsInsHasSchema) {
        if (this.formModal.catalogName === '') {
          this.$Message.error(this.$t('cao-zuo-shi-bai'));
          return;
        } else {
          levels.push(this.formModal.catalogName);
        }
      }

      const res = await this.$services.dmCicdDevopsDsDbLevels({
        data: {
          levels: levels.filter(Boolean),
          refreshCache: isRefreshCache
        }
      });

      if (res.success) {
        this.devopsInsSchemaList = res.data;
      }
    },
    fetchFormDsLevels() {
      if (!this.devopsInsSelected || !this.devopsInsSelected?.objAttr?.dsEnvId) {
        return [this.formModal.instanceId].filter(Boolean);
      }
      const levels = [this.devopsInsSelected?.objAttr?.dsEnvId, this.formModal.instanceId];
      if (this.devopsInsHasCatalog) {
        levels.push(this.formModal.catalogName);
      }
      if (this.devopsInsHasSchema) {
        levels.push(this.formModal.schemaName);
      }
      return levels.filter(Boolean);
    },
    async handleDevopsSubmit() {
      const valid = await this.$refs.formModal.validate();
      if (!valid) return;

      const flowForm = {
        flowId: this.flowId,
        eventType: this.formModal.eventType,
        pipeline: {
          repoScmId: this.formModal.repoScmId,
          repoScmUrl: this.formModal.repoScmUrl,
          repoSpace: this.formModal.repoSpace,
          repoName: this.formModal.repoName,
          repoBranch: this.formModal.repoBranch,
          repoScriptPath: this.formModal.repoScriptPath,
          eventType: this.formModal.eventType,
          dsLevels: this.fetchFormDsLevels()
        },
        option: {
          initScript: this.formModal.initScript
        }
      };

      const res = await this.$services.dmCicdFlowDevopsCreate({
        data: flowForm
      });
      if (res.success) {
        this.$Message.success(this.$t('fa-bu-liu-pei-zhi-cheng-gong'));
        const reloadChange = this.formModal.initScript !== 'None';
        this.handleCloseAllDrawer();

        if (reloadChange) {
          this.$nextTick(() => {
            this.fetchListDevops();
            this.fetchChangeList();
          });
        } else {
          this.$nextTick(() => this.fetchListDevops());
        }
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    handleCloseAllDrawer() {
      this.imDialogDrawerShow = false;
      this.imDialogFlowShow = false;
      this.imDialogDevOpsShow = false;
      setTimeout(() => {
        this.$refs.formModal?.resetFields?.();
      }, 100);
    },
    envDevOpsTarget(row) {
      if (row.dsLevels.length <= 2) {
        return 'Unknown';
      } else if (row.dsLevels.length === 3) {
        return row.dsLevels[2];
      } else {
        return `/${row.dsLevels.slice(2, row.dsLevels.length).join('/')}`;
      }
    },
    //
    openTriggerConfig(item) {
      this.showTriggerModal = true;
      this.triggerTab = item.webHookEnable || (!item.webHookEnable && !item.triggerEnable) ? 'WebHook' : 'TriggerUrl';
      this.triggerOriginal = item;
      this.trigger = {
        flowId: item.flowId,
        hookEnable: item.webHookEnable,
        hookUrl: item.webHookUrl,
        hookPassword: item.webHookPwd,
        hookHelpUrl: item.webHookHelpUrl,
        hookRepoUrl: item.repoUrl,
        triggerEnable: item.triggerEnable,
        triggerUrl: item.triggerUrl,
        triggerUrlShow: this.buildTriggerUrl(item.triggerUrl, 'text'),
        triggerToken: item.triggerToken,
        triggerMethod: 'http',
        triggerFormat: 'text'
      };
    },
    triggerTabLabel(type) {
      if (type === 'WebHook') {
        return (h) => [
          h('CustomIcon', { props: { type: this.triggerOriginal.webHookEnable ? 'SuccessColorful' : 'Disable2' } }),
          h('span', { style: { marginLeft: '5px', marginRight: '5px' } }, `${this.$t('webhook')}`)
        ];
      } else if (type === 'TriggerUrl') {
        return (h) => [
          h('CustomIcon', { props: { type: this.triggerOriginal.triggerEnable ? 'SuccessColorful' : 'Disable2' } }),
          h(
            'span',
            {
              style: {
                marginLeft: '5px',
                marginRight: '5px'
              }
            },
            `${this.$t('yuan-cheng-chu-fa')}`
          )
        ];
      } else {
        return 'Unknown';
      }
    },
    buildTriggerUrl(url, format) {
      if (!url) return '';
      if (/[?&]format=/.test(url)) {
        return url.replace(/([?&])format=[^&]*/, `$1format=${format}`);
      }
      return `${url}${url.includes('?') ? '&' : '?'}format=${format}`;
    },
    handleTriggerUrlBuild() {
      const triggerUrl = this.buildTriggerUrl(this.trigger.triggerUrl, this.trigger.triggerFormat);
      if (this.trigger.triggerMethod === 'http') {
        this.trigger.triggerUrlShow = triggerUrl;
      } else if (this.trigger.triggerMethod === 'wget') {
        this.trigger.triggerUrlShow = `wget -q -O- "${triggerUrl}"`;
      } else if (this.trigger.triggerMethod === 'curl') {
        this.trigger.triggerUrlShow = `curl "${triggerUrl}"`;
      }
    },
    async handleSaveTrigger() {
      const res = await this.$services.dmCicdFlowTriggerConfig({
        data: {
          flowId: this.trigger.flowId,
          updateHook: this.triggerTab === 'WebHook',
          updateTrigger: this.triggerTab === 'TriggerUrl',
          hookEnable: this.trigger.hookEnable,
          triggerEnable: this.trigger.triggerEnable
        }
      });
      if (res.success) {
        const msg = this.trigger.hookEnable ? this.$t('qi-yong-webhook') : this.$t('jin-yong-webhook');
        this.$Message.success(msg);
        await this.fetchListDevops();
        this.showTriggerModal = false;
      } else {
        this.trigger.hookEnable = !this.trigger.hookEnable;
      }
    },
    openCallBack(item) {
      this.showCallbackModal = true;
      this.callbackData.flowId = item.flowId;
      this.callbackData.enable = item.callbackEnable;
      this.callbackData.method = item.callbackMethod;
      this.callbackData.url = item.callbackUrl;
    },
    async handleSaveCallBack() {
      const res = await this.$services.dmCicdFlowCallBackConfig({
        data: {
          flowId: this.callbackData.flowId,
          enable: this.callbackData.enable,
          method: this.callbackData.method,
          url: this.callbackData.url
        }
      });
      if (res.success) {
        const msg = this.$t('cao-zuo-cheng-gong');
        this.$Message.success(msg);
        this.showCallbackModal = false;
        await this.fetchListDevops();
      } else {
        this.callbackData.enable = !this.callbackData.enable;
      }
    },
    //
    async fetchChangeList() {
      this.loading = true;

      const res = await this.$services.dmCicdChangeList({
        data: {
          flowId: this.flowId,
          searchKeywords: this.keyword,
          page: {
            pageSize: this.pageSize,
            pageNum: this.pageNum
          }
        }
      });

      this.loading = false;
      this.changeList = res.data.records;
      this.pageNum = res.data.current;
      this.pageSize = res.data.size;
      this.pageTotal = res.data.total;
    },
    changeStepColor(step, row) {
      if (row.currentStep === 'INIT_SNAPSHOT') {
        switch (row.currentStatus) {
          case 'OPEN':
          case 'READY':
          case 'WAIT':
            return '#1296DB';
          case 'FAILED':
            return '#E44245';
          case 'FINISH':
            return '#59c378';
          case 'CLOSED':
          default:
            return '#636363';
        }
      }
      if (FLOW_STEP_NUM[step] < FLOW_STEP_NUM[row.currentStep]) {
        return '#59c378';
      } else if (FLOW_STEP_NUM[step] > FLOW_STEP_NUM[row.currentStep]) {
        return '#636363';
      } else {
        switch (row.currentStatus) {
          case 'OPEN':
          case 'READY':
          case 'WAIT':
            return '#1296DB';
          case 'FAILED':
            return '#E44245';
          case 'FINISH':
            return '#59c378';
          case 'CLOSED':
            return '#636363';
          default:
            return '#59c378';
        }
      }
    },
    changeStatueIcon(row) {
      if (row.currentStep === 'INIT_SNAPSHOT') {
        switch (row.currentStatus) {
          case 'FAILED':
            return 'icon-v2-Progress3';
          case 'FINISH':
            return 'icon-v2-Success3';
          case 'CLOSED':
            return 'icon-v2-Close3';
          default:
            return 'icon-v2-Progress1';
        }
      }

      const isFinishStep = row.currentStep === 'FINISH';
      const isFinishStatus = row.currentStatus === 'FINISH';
      if (row.locked) {
        return isFinishStatus && isFinishStep ? 'icon-v2-Success3' : 'icon-v2-Close3';
      }
      if (isFinishStep) {
        return isFinishStatus ? 'icon-v2-Success3' : 'icon-v2-Progress1';
      }
      return 'icon-v2-Progress1';
    },
    async handleQuery() {
      await this.fetchChangeList();
      this.changeList = this.changeList || [];
    },
    async handleQueryClear() {
      this.keyword = '';
      await this.fetchChangeList();
    },
    deleteDevops(data) {
      this.$Modal.confirm({
        title: this.$t('que-ren-shi-fou-shan-chu'),
        content: this.$t('shan-chu-fa-bu-liu'),
        onOk: async () => {
          const res = await this.$services.dmCicdFlowDevopsDelete({
            data: {
              flowId: data.flowId
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            await this.fetchListDevops();
          }
        }
      });
    },
    async enableDevops(data) {
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('shi-fou-qi-yong-fa-bu-liu'),
        onOk: async () => {
          const res = await this.$services.dmCicdFlowDevopsSwitch({
            data: {
              flowId: data.flowId,
              enable: true
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            await this.fetchListDevops();
          }
        }
      });
    },
    disableDevops(data) {
      this.$Modal.confirm({
        title: this.$t('que-ren-shi-fou-jin-yong'),
        content: this.$t('jin-yong-fa-bu-liu'),
        onOk: async () => {
          const res = await this.$services.dmCicdFlowDevopsSwitch({
            data: {
              flowId: data.flowId,
              enable: false
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            await this.fetchListDevops();
          }
        }
      });
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.fetchChangeList();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.init();
    },
    async triggerChange(devopsItem) {
      this.$Modal.confirm({
        title: this.$t('que-ren-shi-fou-chu-fa'),
        content: this.$t('chu-fa-fa-bu-liu'),
        onOk: async () => {
          const res = await this.$services.dmCicdFlowTriggerChange({
            data: {
              flowId: devopsItem.flowId
            }
          });

          if (res?.success) {
            this.$message.success(this.$t('chu-fa-bian-geng-cheng-gong'));
            this.init();
          }
        }
      });
    },
    async triggerSnapshot(devopsItem) {
      this.$Modal.confirm({
        title: this.$t('que-ren-shi-fou-gou-jian'),
        content: this.$t('gou-jian-fa-bu-liu'),
        onOk: async () => {
          const res = await this.$services.dmCicdFlowTriggerSnapshot({
            data: {
              flowId: devopsItem.flowId
            }
          });

          if (res.success) {
            this.$message.success(this.$t('cao-zuo-cheng-gong'));
            this.init();
          }
        }
      });
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
.flow-wrap {
  padding-bottom: 0 !important;
  min-height: 0;
  overflow: hidden;
  background: #f6f9fc;

  .empty {
    display: flex;
    flex-direction: column;
    align-items: center;

    .empty_image {
      width: 90px;
      height: 90px;
      filter: drop-shadow(0 0 0 #8b8b8b);
    }
  }

  .flow-detail-content {
    flex: 1 1 auto;
    height: 100%;
    max-height: none;
    overflow: auto;
  }
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
  width: 80%;
  margin: 20px 0 20px 30px;
}

.flow-icons,
.flow-icons-cursor {
  border-radius: 15px;
  background: #ececec;
  margin-left: 5px;
  padding-right: 5px;
  display: flex;
  align-items: center;
  justify-items: center;
}

.flow-icons-cursor {
  cursor: pointer;
}

.flow-drawer {
  .flow-drawer-steps {
    padding: 20px 0 40px 20px;
  }

  .flow-drawer-step {
    padding: 0 0 30px 20px;
  }

  .flow-drawer-step-tips {
    padding-top: 5px;
  }
}

.flow-base {
  width: 230px;
}

.flow-base-info {
  display: flex;
  font-weight: bold;
  flex-wrap: wrap;
  gap: 10px;

  .info-item {
    display: flex;
    align-items: center;
    margin-right: 20px;
    line-height: 27px;
  }

  .im-tag,
  .im-tag-readonly {
    position: relative;
    display: inline-block;
  }

  .im-tag::after {
    content: '';
    position: absolute;
    top: 18px;
    right: 2px;
    width: 0;
    height: 0;
    border-style: solid;
    border-width: 0 0 6px 6px;
    border-color: transparent transparent #aaa transparent;
  }

  :deep(.ivu-select-dropdown) {
    width: 50px;
    overflow: hidden;
  }

  .hoverStyle {
    cursor: pointer;
  }
}

.warp-top {
  display: flex;
  justify-content: space-between;

  .left {
    display: flex;
    align-items: center;
  }
}

.option-wrap {
  display: flex;
  flex-direction: column;
  border: 1px solid #ededed;
  padding: 10px;
  margin-bottom: 10px;
  min-height: 110px;
}

.bottom-card {
  display: flex;
  flex-wrap: wrap;
}

.card-wrap {
  position: relative;
  display: flex;
  flex-direction: column;
  min-width: 550px;
  border: 1px solid #ededed;
  margin-right: 10px;
  padding: 16px;
  border-radius: 4px;
  margin-top: 10px;

  .label {
    text-align: right;
    width: 85px;
    flex-shrink: 0;
  }

  .flow {
    .part1,
    .part3 {
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }

    .part2 {
      width: 36px;
      flex-shrink: 0;
    }
  }
}

.warp-empty {
  display: flex;
  justify-content: center;
  width: 100%;
  height: 50px;
  align-items: center;
  width: 100%;
  height: 50px;
}

.drawer-wrap {
  .drawer-content {
    height: calc(100% - 53px);
    overflow: auto;
  }

  position: relative;

  :deep(.ant-drawer-body) {
    padding-top: 0;
  }

  :deep(.ivu-divider-inner-text) {
    display: flex;
    align-items: flex-end;
    color: #636363;
  }

  :deep(.ivu-input-prefix),
  .ivu-select-prefix {
    display: flex;
    justify-content: center;
  }

  :deep(.ivu-form-item) {
    margin-bottom: 20px;
  }

  :deep(.ivu-radio-wrapper) {
    font-size: 12px;
  }
}

.drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}

.btn-wrap {
  margin-right: 10px;
}

.switch-btn {
  display: inline-block;
  margin-bottom: 10px;
  margin-left: 10px;
}

:deep(.devops-i-src) {
  border-radius: 20px;
  border: 1px solid #dfdfdf;
  z-index: 2;
}

:deep(.devops-i-dst) {
  border-radius: 20px;
  border: 1px solid #dfdfdf;
  z-index: 1;
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

.card-item {
  display: flex;
  overflow: hidden;
  white-space: nowrap;

  .label {
    flex-shrink: 0;
  }

  .value {
    flex: 1;
    color: #7a7a7a;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.text-overflow {
  width: 85%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.webhook-text {
  display: inline-block;
  color: #3498db;
  cursor: pointer;
}

.finish-wrap {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;

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
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  justify-content: center;
}

.im-text-ellipsis {
  max-width: 100px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}

.flow-overflow {
  display: inline-block;
  width: 150px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  vertical-align: bottom;
}

.flow-detail-content {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  height: 100%;
  max-height: none;
  padding: 14px 20px 18px;
  background: #f6f9fc;
  overflow: auto;
}

.detail-toolbar {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 10px;
  margin-bottom: 0;
  min-width: 0;

  .detail-toolbar-btn {
    height: 32px;
    padding: 0 12px;
    border-color: #d9e3ee;
    border-radius: 5px;
    color: #111827;
    font-size: 12px;
    font-weight: 400;
    background: #fff;
    box-shadow: 0 3px 8px rgba(31, 45, 61, 0.04);

    :deep(span) {
      display: inline-flex;
      align-items: center;
      gap: 5px;
      font-weight: 400;
    }

    :deep(.ivu-icon) {
      color: #536173;
      font-size: 14px;
    }
  }

  .detail-trigger-btn {
    border-color: #0fac69;
    color: #fff;
    background: linear-gradient(180deg, #16be72 0%, #08a95e 100%);
    box-shadow: 0 8px 18px rgba(15, 172, 105, 0.18);

    :deep(.ivu-icon) {
      color: currentColor;
    }

    &[disabled],
    &.ivu-btn-disabled {
      border-color: #d9e3ee;
      color: #94a3b8;
      background: #f4f7fa;
      box-shadow: none;
    }
  }

  .detail-snapshot-btn {
    border-color: #22c779;
    color: #0fac69;
    background: #fff;

    :deep(.ivu-icon) {
      color: currentColor;
    }

    &[disabled],
    &.ivu-btn-disabled {
      border-color: #d9e3ee;
      color: #94a3b8;
      background: #f4f7fa;
    }
  }
}

.detail-card {
  min-width: 0;
  background: #fff;
  border: 1px solid #dbe6f1;
  border-radius: 10px;
  box-shadow: 0 12px 30px rgba(31, 45, 61, 0.05);
}

.detail-card-title {
  display: flex;
  align-items: center;
  gap: 16px;
  color: #111827;
  font-size: 18px;
  font-weight: 800;
  line-height: 1;
}

.detail-card-title::before {
  display: inline-block;
  width: 4px;
  height: 26px;
  border-radius: 999px;
  background: #14b86f;
  content: '';
}

.detail-hero-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  gap: 24px;
  flex-shrink: 0;
}

.overview-card {
  min-height: auto;
  padding: 20px 24px 22px;
}

.overview-card-header {
  display: grid;
  grid-template-columns: minmax(110px, auto) minmax(0, 1fr);
  align-items: center;
  gap: 12px;
}

.overview-title-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 0;
  min-width: 0;
}

.overview-flow-name {
  max-width: 320px;
  overflow: hidden;
  color: #0f172a;
  font-size: 20px;
  font-weight: 800;
  line-height: 24px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.flow-status-pill {
  display: inline-flex;
  align-items: center;
  height: 26px;
  padding: 0 14px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 800;

  &.success {
    color: #0ea568;
    background: #dcf8e8;
  }

  &.muted {
    color: #64748b;
    background: #eef2f7;
  }

  &.danger {
    color: #e5484d;
    background: #ffe8e8;
  }
}

.overview-meta-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  row-gap: 14px;
  margin-top: 28px;
  padding-left: 22px;
  color: #0f172a;
}

.overview-meta-item {
  display: flex;
  align-items: center;
  min-width: 0;
  gap: 10px;
  color: #111827;
}

.overview-meta-copy {
  display: flex;
  align-items: center;
  min-width: 0;
  color: #111827;
  font-size: 12px;
  line-height: 1;

  > span:first-child {
    flex-shrink: 0;
    color: #111827;
    font-weight: 700;
    white-space: nowrap;
  }

  strong {
    min-width: 0;
    color: #111827;
    font-size: 12px;
    font-weight: 800;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.inline-action-icon {
  margin-left: 8px;
  color: #8795a8;
  cursor: pointer;
  font-size: 15px;
  transition: color 0.18s ease;

  &:hover {
    color: #0fac69;
  }
}

.text-link {
  margin-left: 10px;
  padding: 0;
  border: 0;
  color: #20c967;
  font-size: 12px;
  font-weight: 800;
  background: transparent;
  cursor: pointer;

  &:hover {
    color: #07864f;
  }
}

.pipeline-overview {
  display: grid;
  grid-template-columns: minmax(260px, 1fr) 116px minmax(260px, 1fr);
  align-items: stretch;
  gap: 0;
  margin-top: 30px;
}

.endpoint-card {
  min-width: 0;
  min-height: 132px;
  padding: 12px 22px;
  border: 0;
  border-radius: 0;
  background: transparent;
}

.endpoint-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
  color: #111827;
  font-size: 14px;
  font-weight: 800;

  &.database-title {
    :deep(.data-source-icon) {
      color: #12b76a;
    }
  }
}

.endpoint-row {
  display: flex;
  align-items: center;
  min-width: 0;
  margin-top: 10px;
  color: #111827;
  font-size: 12px;
  line-height: 18px;

  span {
    flex: 0 0 84px;
    color: #5f6c80;
    font-weight: 700;
  }

  strong {
    min-width: 0;
    overflow: hidden;
    color: #111827;
    font-weight: 800;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.pipeline-link {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0fa958;
  min-height: 132px;

  .pipeline-dash {
    display: none;
  }
}

.pipeline-link::before {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  border-left: 1px dashed #d5e0eb;
  content: '';
  transform: translateX(-50%);
}

.pipeline-link-node {
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
}

.pipeline-link-node::before {
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

.config-card {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  margin-top: 14px;
  padding: 18px 24px 20px;
}

.config-list {
  display: grid;
  flex: 1 1 auto;
  grid-template-columns: minmax(0, 1fr);
  align-content: start;
  gap: 0;
  min-height: 0;
  margin-top: 18px;
  border-top: 1px solid #e1ebf3;
  overflow: auto;
  background: #fff;
}

.config-row {
  display: grid;
  grid-template-columns: 220px 132px minmax(260px, 1fr) minmax(180px, auto) 16px;
  align-items: center;
  min-width: 770px;
  min-height: 64px;
  padding: 0 0 0 20px;
  border-bottom: 1px solid #e1ebf3;
  color: #1f2937;
  font-size: 14px;
  background: #fff;
}

.config-leading {
  display: flex;
  align-items: center;
  min-width: 0;
}

.config-name {
  color: #5b6a80;
  font-size: 14px;
  font-weight: 800;
  white-space: nowrap;
}

.config-status {
  display: inline-flex;
  justify-content: center;
  width: fit-content;
  min-width: 64px;
  height: 22px;
  padding: 0 7px;
  align-items: center;
  border-radius: 7px;
  font-size: 14px;
  font-weight: 700;

  &.success {
    color: #0fac69;
    background: #e0f8e9;
  }

  &.muted {
    color: #667085;
    background: #eef2f7;
  }
}

.config-desc {
  color: #465467;
  font-size: 14px;
}

.config-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.config-action-item {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.config-action-link {
  padding: 0;
  border: 0;
  color: #0fac69;
  font-size: 14px;
  font-weight: 700;
  background: transparent;
  cursor: pointer;

  &:hover {
    color: #07864f;
  }
}

.config-action-divider {
  color: #a9b4c2;
}

.config-arrow {
  justify-self: end;
  color: #44546a;
  font-size: 14px;
}

.trigger-config-modal,
.callback-config-modal {
  padding: 6px 0 12px;
  color: #111827;
}

.trigger-config-title,
.callback-config-title {
  display: flex;
  align-items: center;
  gap: 16px;
  height: 48px;
  color: #111827;
  font-size: 20px;
  font-weight: 800;
  line-height: 1;
}

.trigger-config-title::before,
.callback-config-title::before {
  display: inline-block;
  width: 4px;
  height: 28px;
  border-radius: 999px;
  background: #14b86f;
  content: '';
}

.config-modal-tabs {
  margin-top: 10px;

  :deep(.ivu-tabs-bar) {
    margin-bottom: 0;
    border-bottom: 1px solid #e1ebf3;
  }

  :deep(.ivu-tabs-nav .ivu-tabs-tab) {
    padding: 14px 18px 13px;
    color: #66758a;
    font-size: 14px;
    font-weight: 800;
  }

  :deep(.ivu-tabs-nav .ivu-tabs-tab-active) {
    color: #0fac69;
  }

  :deep(.ivu-tabs-ink-bar) {
    background-color: #14b86f;
  }
}

.config-section-heading {
  margin-top: 18px;
  color: #111827;
  font-size: 17px;
  font-weight: 800;
  line-height: 24px;
}

.config-modal-list {
  margin-top: 14px;
  border-top: 1px solid #e1ebf3;
}

.config-modal-row {
  display: grid;
  grid-template-columns: 150px minmax(300px, 1fr) minmax(180px, 0.72fr);
  column-gap: 20px;
  align-items: center;
  min-height: 76px;
  border-bottom: 1px solid #e1ebf3;
}

.callback-config-row {
  grid-template-columns: 150px minmax(260px, 1fr) minmax(180px, 0.75fr);
}

.config-modal-label {
  min-width: 0;
  color: #5b6a80;
  font-size: 15px;
  font-weight: 800;
  line-height: 22px;
  white-space: nowrap;
}

.config-modal-control {
  min-width: 0;

  :deep(.ivu-input-wrapper) {
    width: 100%;
  }

  :deep(.ivu-input) {
    height: 36px;
    border-color: #d9e3ee;
    border-radius: 6px;
    color: #111827;
    font-size: 13px;
    font-weight: 600;
  }

  :deep(.ivu-input[disabled]) {
    color: #8b98aa;
    background: #f6f9fc;
  }

  :deep(.ivu-input-suffix) {
    display: inline-flex;
    align-items: center;
    cursor: pointer;
  }
}

.config-modal-radio {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  :deep(.ivu-radio-wrapper) {
    min-width: 68px;
    height: 32px;
    margin-right: 0;
    padding: 0 12px;
    border-color: #d9e3ee;
    color: #111827;
    font-size: 13px;
    font-weight: 700;
    line-height: 30px;
    text-align: center;
    background: #fff;
  }

  :deep(.ivu-radio-wrapper-checked) {
    border-color: #14b86f;
    color: #0fac69;
    box-shadow: -1px 0 0 0 #14b86f;
  }

  :deep(.ivu-radio-wrapper-disabled) {
    color: #9aa7b7;
    background: #f6f9fc;
  }
}

.config-modal-desc {
  min-width: 0;
  color: #66758a;
  font-size: 13px;
  line-height: 20px;
}

.callback-config-summary {
  display: flex;
  align-items: center;
  min-height: 58px;
  margin-top: 16px;
  padding: 12px 16px;
  border: 1px solid #dbe6f1;
  border-radius: 8px;
  color: #465467;
  font-size: 14px;
  font-weight: 700;
  line-height: 22px;
  background: #fbfdff;
}

.config-modal-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.notify-config-modal {
  padding: 6px 0 12px;
  color: #111827;
}

.notify-config-title {
  display: flex;
  align-items: center;
  gap: 16px;
  height: 48px;
  color: #111827;
  font-size: 20px;
  font-weight: 800;
  line-height: 1;
}

.notify-config-title::before {
  display: inline-block;
  width: 4px;
  height: 28px;
  border-radius: 999px;
  background: #14b86f;
  content: '';
}

.notify-config-layout {
  display: grid;
  grid-template-columns: minmax(360px, 1fr) minmax(440px, 1.15fr);
  gap: 36px;
  margin-top: 22px;
}

.notify-config-label,
.notify-panel-title {
  color: #111827;
  font-size: 17px;
  font-weight: 800;
  line-height: 24px;
}

.notify-config-label.required::after {
  margin-left: 6px;
  color: #ed4014;
  content: '*';
}

.notify-channel-list {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
  margin-top: 18px;
}

.notify-channel-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-width: 0;
  height: 40px;
  padding: 0 10px;
  border: 1px solid #d9e3ee;
  border-radius: 6px;
  color: #5f6c80;
  font-size: 14px;
  font-weight: 700;
  background: #fff;
  cursor: pointer;
  transition:
    border-color 0.18s ease,
    color 0.18s ease,
    background 0.18s ease;

  span {
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &.active {
    border-color: #14b86f;
    color: #0fac69;
    background: #f0fbf5;
    box-shadow: inset 0 0 0 1px #14b86f;
  }
}

.service-label {
  margin-top: 22px;
  color: #5b6a80;
}

.notify-service-select {
  width: 100%;
  margin-top: 12px;

  :deep(.ivu-select-selection) {
    min-height: 42px;
    border-color: #d9e3ee;
    border-radius: 6px;
  }
}

.notify-config-right {
  min-width: 0;
}

.notify-subscription-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  margin-top: 18px;
  border: 1px solid #d9e3ee;
  border-radius: 8px;
  overflow: hidden;
}

.notify-subscription-cell {
  display: flex;
  align-items: center;
  min-height: 92px;
  padding: 0 28px;
  border-right: 1px dashed #d9e3ee;
  border-bottom: 1px dashed #d9e3ee;
  color: #5f6c80;
  font-size: 15px;
  font-weight: 800;

  &:nth-child(2n) {
    border-right: 0;
  }

  &:nth-last-child(-n + 2) {
    border-bottom: 0;
  }

  span {
    margin-left: 12px;
  }
}

.execution-config-modal {
  padding: 6px 0 12px;
  color: #111827;
}

.execution-config-title {
  display: flex;
  align-items: center;
  gap: 16px;
  height: 48px;
  margin-bottom: 0;
  color: #111827;
  font-size: 20px;
  font-weight: 800;
  line-height: 1;
}

.execution-config-title::before {
  display: inline-block;
  width: 4px;
  height: 28px;
  border-radius: 999px;
  background: #14b86f;
  content: '';
}

.execution-config-list {
  margin-top: 16px;
  border-top: 1px solid #e1ebf3;
}

.execution-config-row {
  display: grid;
  grid-template-columns: 210px 390px minmax(260px, 1fr);
  align-items: center;
  min-height: 78px;
  border-bottom: 1px solid #e1ebf3;
}

.execution-config-name {
  display: flex;
  align-items: center;
  min-width: 0;
  color: #5b6a80;
  font-size: 17px;
  font-weight: 800;
  white-space: nowrap;

  :deep(.ivu-icon) {
    margin-left: 10px;
    color: #64748b;
    font-size: 15px;
    cursor: default;
  }
}

.execution-config-options {
  display: flex;
  align-items: center;
  min-width: 0;

  :deep(.ivu-radio-wrapper) {
    margin-right: 38px;
    color: #111827;
    font-size: 15px;
    line-height: 22px;
  }

  :deep(.ivu-radio-wrapper-disabled) {
    color: #9aa7b7;
  }

  :deep(.ivu-radio) {
    margin-right: 8px;
  }
}

.execution-config-desc {
  min-width: 0;
  color: #66758a;
  font-size: 14px;
  line-height: 22px;
}

@media (max-width: 1280px) {
  .detail-hero-grid {
    grid-template-columns: 1fr;
  }

  .pipeline-overview {
    grid-template-columns: 1fr;
  }

  .pipeline-link {
    flex-direction: column;
    min-height: 96px;
  }

  .pipeline-link .pipeline-dash {
    display: none;
  }

  .pipeline-link-node {
    margin: 10px 0;
  }

  .config-row {
    grid-template-columns: 220px 140px minmax(180px, 1fr) minmax(220px, auto) 22px;
  }
}

@media (max-width: 1120px), (max-height: 760px) {
  .flow-detail-content {
    padding: 10px 16px 16px;
  }

  .overview-card {
    padding: 16px 20px 18px;
  }

  .overview-meta-grid {
    margin-top: 22px;
    padding-left: 18px;
  }

  .pipeline-overview {
    margin-top: 22px;
  }

  .endpoint-card {
    min-height: 120px;
    padding: 12px 18px;
  }

  .config-card {
    padding: 14px 20px 18px;
  }
}

@media (max-width: 980px) {
  .overview-card-header {
    grid-template-columns: 1fr;
    align-items: start;
  }

  .detail-toolbar {
    justify-content: flex-start;
  }

  .overview-meta-grid {
    grid-template-columns: 1fr;
    row-gap: 10px;
  }

  .notify-config-layout {
    grid-template-columns: 1fr;
  }

  .notify-channel-list,
  .notify-subscription-grid {
    grid-template-columns: 1fr;
  }

  .notify-subscription-cell {
    border-right: 0;

    &:nth-last-child(2) {
      border-bottom: 1px dashed #d9e3ee;
    }
  }

  .execution-config-row {
    grid-template-columns: 1fr;
    gap: 12px;
    padding: 18px 0;
  }

  .config-modal-row,
  .callback-config-row {
    grid-template-columns: 1fr;
    gap: 10px;
    padding: 16px 0;
  }

  .config-modal-desc {
    font-size: 12px;
  }
}
</style>
