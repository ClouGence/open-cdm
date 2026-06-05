<template>
  <div class="table-list-layout">
    <div class="table-list project-wrap">
      <div class="header">
        <Breadcrumb>
          <BreadcrumbItem to="/project">{{ $t('xiang-mu-lie-biao') }}</BreadcrumbItem>
          <BreadcrumbItem class="project-overflow">
            {{ projectInfo === null ? $t('wei-zhi') : projectInfo.name }}
          </BreadcrumbItem>
        </Breadcrumb>
      </div>
      <div class="content" v-if="projectInfo === null">
        <div class="empty">
          <img src="@/assets/not-exist.svg" class="empty_image" />
          {{ $t('bu-cun-zai-de-xiang-mu') }}
        </div>
      </div>
      <div class="content" v-if="projectInfo !== null">
        <div class="option-wrap">
          <div class="warp-top">
            <div class="left">
              <div class="project-base-info">
                <CustomIcon v-if="projectInfo.status === 'DELETE'" type="icon-v2-Delete2" size="18px" rightMargin="5px" />
                <CustomIcon v-if="projectInfo.status === 'ARCHIVE'" type="icon-v2-Archive" size="18px" rightMargin="5px" />
                <Dropdown v-if="projectInfo.status === 'NORMAL'" placement="bottom-start" @on-click="handleProjectMark">
                  <CustomIcon :type="PROJECT_MARK_MAP[projectInfo.mark]" hoverStyle size="18px" topMargin="5px" rightMargin="5px" />
                  <template #list>
                    <DropdownItem :name="key" v-for="(key, index) in Object.keys(PROJECT_MARK_MAP)" :key="index">
                      <CustomIcon :type="PROJECT_MARK_MAP[key]" />
                    </DropdownItem>
                  </template>
                </Dropdown>
                <CCFieldEdit
                  class="info-item"
                  value-margin="5px"
                  :fieldName="$t('xiang-mu-code')"
                  :value="projectInfo.projectCode"
                  :readOnly="true"
                />
                <CCFieldEdit
                  class="info-item"
                  value-margin="5px"
                  :fieldName="$t('xiang-mu-ming-cheng')"
                  :value="projectInfo.name"
                  :read-only="projectReadOnly"
                  :onOK="handleNameEdit"
                />
                <CCSelectEdit
                  class="info-item"
                  value-margin="5px"
                  :fieldName="$t('guan-li-yuan')"
                  :read-only="projectReadOnly"
                  :value="projectOwnerInfo.ownerUserUID"
                  :label="projectOwnerInfo.ownerUserName"
                  :options="devopsUsersList"
                  :valueOfValue="fetchDevopsUsersListValueOfValue"
                  :valueOfLabel="fetchDevopsUsersListValueOfLabel"
                  :onOK="handleAdminEdit"
                  :onEditor="fetchDevopsUsersList"
                />
                <div class="info-item">
                  <div>{{ $t('im-xiao-xi') + ':' || '-' }}</div>
                  <div @click="handleImEdit" :class="projectReadOnly ? '' : 'hoverStyle'" style="display: flex">
                    <CustomIcon :type="imProviderInfo.imType === 'none' ? 'Disable' : imProviderInfo.imType" leftMargin="5px" rightMargin="5px" />
                    <span v-if="imProviderInfo.imType !== 'none'" :class="`im-tag${projectReadOnly ? '-readonly' : ''} im-text-ellipsis`">
                      {{ imProviderInfo.name }}
                    </span>
                  </div>
                </div>
                <div class="info-item">
                  <div>{{ $t('liu-cheng-pei-zhi') + ':' || '-' }}</div>
                  <div @click="handleFlowEdit" :class="`im-tag${projectReadOnly ? '-readonly' : ''} flow-icons${!projectReadOnly ? '-cursor' : ''}`">
                    <CustomIcon type="icon-v2-shenhe" leftMargin :color="`${projectInfo.flowCheck !== 'Skip' ? '#43cf7c' : ''}`" />
                    <CustomIcon type="icon-v2-shenpi" leftMargin :color="`${projectInfo.flowApprove === 'Enable' ? '#43cf7c' : ''}`" />
                    <CustomIcon type="icon-v2-DataBase2" leftMargin :color="`${projectInfo.flowExecute !== 'Disabled' ? '#43cf7c' : ''}`" />
                  </div>
                </div>
              </div>
            </div>
            <div class="right">
              <Button class="btn-wrap" type="primary" ghost icon="md-add" @click="handleGitOpsAdd" v-if="projectInfo.status === 'NORMAL'">
                {{ $t('tian-jia-git-ops') }}
              </Button>
              <Button class="btn-wrap" @click="handleOperate('delate')" v-if="projectInfo.status === 'ARCHIVE'">
                <span style="color: red">{{ $t('shan-chu-xiang-mu') }}</span>
              </Button>
              <Button class="btn-wrap" @click="handleOperate('archive')" v-if="projectInfo.status === 'NORMAL'">
                {{ $t('gui-dang-xiang-mu') }}
              </Button>
              <Button class="btn-wrap" @click="handleOperate('resetArchive')" v-if="projectInfo.status === 'ARCHIVE'">
                {{ $t('hui-fu-gui-dang') }}
              </Button>
              <Button class="btn-wrap" @click="init">
                <CustomIcon type="icon-v2-Refresh" :disabled="loading" />
              </Button>
            </div>
          </div>
          <div class="warp-empty" v-if="devopsList.length === 0">
            <Button type="info" ghost :disabled="projectReadOnly" @click="handleGitOpsAdd">
              {{ $t('qing-tian-jia-fa-bu-liu') }}
            </Button>
          </div>
          <div class="bottom-card" v-if="devopsList.length !== 0">
            <div class="card-wrap" v-for="(item, index) in devopsList" :key="index">
              <div class="flow flex items-center gap-3 w-full">
                <div class="part1 flex items-center gap-2 overflow-hidden">
                  <CustomIcon :type="item.scmType" size="22" />
                  <div class="text-overflow flex flex-col gap-2">
                    <div class="card-item flex">
                      <span class="label text-right">{{ $t('git-repo') }}：</span>
                      <Tooltip :content="item.repoName + ':' + item.repoBranch">
                        <span class="value im-text-ellipsis">{{ item.repoName }}:{{ item.repoBranch }}</span>
                      </Tooltip>
                    </div>
                    <div class="card-item flex max-w-[250px]">
                      <span class="label text-right">{{ $t('lu-jing') }}：</span>
                      <Tooltip :content="item.repoScriptPath">
                        <span class="value im-text-ellipsis">{{ item.repoScriptPath }}</span>
                      </Tooltip>
                    </div>
                  </div>
                </div>
                <div class="part2 flex-shrink-0">
                  <CustomIcon type="ArrorRight2" size="23px" />
                </div>
                <div class="part3 flex items-center gap-2 overflow-hidden">
                  <CustomIcon :type="item.dsType" size="22" />
                  <div class="text-overflow flex flex-col gap-2">
                    <div class="card-item flex">
                      <span class="label text-right">{{ $t('shi-li-0') }}：</span>
                      <Tooltip :content="item.dsInstance || '-'">
                        <span class="value im-text-ellipsis">{{ item.dsInstance || '-' }}</span>
                      </Tooltip>
                    </div>
                    <div class="card-item flex">
                      <span class="label text-right">{{ $t('bian-geng-mu-biao') }}：</span>
                      <Tooltip :content="envDevOpsTarget(item)">
                        <span class="value im-text-ellipsis">{{ envDevOpsTarget(item) }}</span>
                      </Tooltip>
                    </div>
                  </div>
                </div>
              </div>
              <hr class="my-3 ml-10 w-[calc(100%-40px)]" />
              <div class="flow info flex flex-col gap-3 w-full">
                <div class="flex gap-6">
                  <div class="card-item flex items-start">
                    <span class="label">{{ $t('chu-fa-pei-zhi') }}：</span>
                    <span class="value flex items-center">
                      <CustomIcon
                        :type="`${item.webHookEnable || item.triggerEnable ? 'icon-v2-SuccessColorful' : 'icon-v2-Disable2'}`"
                        rightMargin="3px"
                        size="12"
                      />
                      <span @click="openTriggerConfig(item)" class="webhook-text">{{ $t('cha-kan-pei-zhi') }}</span>
                    </span>
                  </div>
                  <div class="card-item flex items-start">
                    <span class="label">{{ $t('callback-pei-zhi') }}：</span>
                    <span class="value flex items-center">
                      <CustomIcon :type="`${item.callbackEnable ? 'icon-v2-SuccessColorful' : 'icon-v2-Disable2'}`" rightMargin="3px" size="12" />
                      <span @click="openCallBack(item)" class="webhook-text">{{ $t('cha-kan-pei-zhi') }}</span>
                    </span>
                  </div>
                </div>
                <div v-if="!projectReadOnly" class="flex gap-2 flex-wrap justify-end">
                  <Button v-if="item.enable" size="small" type="primary" @click="triggerChange(item)">
                    {{ $t('chu-fa-bian-geng') }}
                  </Button>
                  <Button v-if="item.enable" size="small" @click="triggerSnapshot(item)">
                    {{ $t('jian-li-kuai-zhao') }}
                  </Button>
                  <Button v-if="item.enable" size="small" type="error" @click="disableDevops(item)">
                    {{ $t('jin-yong-bian-geng') }}
                  </Button>
                  <Button v-if="!item.enable" size="small" type="success" @click="enableDevops(item)">
                    {{ $t('ji-huo-bian-geng') }}
                  </Button>
                  <Button v-if="!item.enable" size="small" type="error" @click="deleteDevops(item)">
                    {{ $t('shan-chu-bian-geng') }}
                  </Button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <CCTitle :content="$t('bian-geng-ji-lu')" />
        <div class="option">
          <div class="left">
            <Input style="width: 280px; margin-right: 10px" clearable v-model="keyword" @on-enter="handleQuery" @on-clear="handleQueryClear" />
            <Button @click="handleQuery" type="primary">{{ $t('cha-xun') }}</Button>
          </div>
        </div>
        <div class="table-container">
          <Table
            :columns="projectDetailTableColumns"
            :data="changeList"
            :loading="loading"
            :locale="{ emptyText: $t('zan-wu-shu-ju') }"
            size="small"
            border
            stripe
          >
            <template #status="{ row }">
              <div style="display: flex; align-items: center; justify-content: center">
                <CustomIcon :type="changeStatueIcon(row)" size="18px" />
              </div>
            </template>
            <template #flow="{ row }">
              <div style="display: flex; flex-direction: column; max-width: 200px">
                <div style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden">
                  <Tag color="blue">{{ $t('mu-biao') }}</Tag>
                  <CustomIcon :type="row.dsType" size="16px" />
                  <span>{{ row.dsInstance }}</span>
                </div>
              </div>
            </template>
            <template #step="{ row }">
              <div class="step-wrap" v-if="row.currentStep === 'INIT_SNAPSHOT'">
                <CustomIcon type="icon-v2-Snapshot" :color="changeStepColor('INIT_SNAPSHOT', row)" size="18px" />
              </div>
              <div class="step-wrap" v-if="row.currentStep !== 'INIT_SNAPSHOT'">
                <CustomIcon type="icon-v2-hebing" :color="changeStepColor('INIT', row)" size="18px" />
                <CustomIcon type="icon-v2-shuang-you" size="12px" />
                <CustomIcon type="icon-v2-shenhe" :color="changeStepColor('CHECK', row)" size="18px" />
                <CustomIcon type="icon-v2-shuang-you" size="12px" />
                <CustomIcon type="icon-v2-shenpi" :color="changeStepColor('APPROVAL', row)" size="18px" />
                <CustomIcon type="icon-v2-shuang-you" size="12px" />
                <CustomIcon type="icon-v2-DataBase2" :color="changeStepColor('EXECUTE', row)" size="18px" />
              </div>
            </template>
            <template #action="{ row }">
              <div class="action">
                <router-link :to="`/project/change/${row?.changeId}`">
                  {{ $t('xiang-qing') }}
                </router-link>
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
            <Select v-model="formModal.repoScmId" class="project-base" @on-change="handleDevopsScmSelected">
              <Option v-for="item in devopsScmList" :value="item.scmId" :key="item.scmId">
                <CustomIcon :type="item.scmType" rightMargin />
                {{ item.scmDisplay }}
              </Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('xuan-ze-cang-ku')" prop="repoName" key="repoName">
            <div style="display: flex; align-items: center">
              <Select
                class="project-base"
                v-model="formModal.repoName"
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
          <FormItem :label="$t('mu-biao-fen-zhi')" prop="repoBranch" key="repoBranch">
            <Input class="project-base" type="text" v-model="formModal.repoBranch">
              <template #prefix>
                <CustomIcon type="icon-v2-branches" style="height: 100%" />
              </template>
            </Input>
          </FormItem>
          <FormItem :label="$t('jiao-ben-lu-jin')" prop="repoScriptPath" key="repoScriptPath">
            <Input class="project-base" type="text" v-model="formModal.repoScriptPath">
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
            <Select v-model="formModal.instanceId" class="project-base" @on-change="handleDevopsChangeIns" filterable>
              <Option v-for="ins in devopsInsList" :value="ins.objId" :key="ins.objId">
                <CustomIcon :type="ins?.objAttr?.dsType" style="margin-right: 5px" />
                {{ ins.objName }}
              </Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('shu-ju-ku')" prop="catalogName" v-show="devopsInsHasCatalog" key="catalogName">
            <Select v-model="formModal.catalogName" class="project-base" :disabled="!devopsInsHasCatalog" @on-change="handleChangeCatalog" filterable>
              <Option v-for="catalog in devopsInsCatalogList" :value="catalog.objName" :key="catalog.objName">
                {{ catalog.objName }}
              </Option>
            </Select>
            <CustomIcon type="icon-v2-Refresh" @click="fetchCatalogList(true)" leftMargin v-if="!repoLoading" />
          </FormItem>
          <FormItem :label="$t('schema')" prop="schemaName" key="schemaName">
            <Select v-model="formModal.schemaName" class="project-base" :disabled="!devopsInsHasSchema" filterable>
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
              {{ fetchProjectGitOpsDescription(formModal.initScript) }}
            </div>
          </div>
        </Form>
      </div>
      <div class="drawer-footer">
        <Button type="primary" @click="handleDevopsSubmit">{{ $t('wan-cheng') }}</Button>
      </div>
    </a-drawer>
    <a-drawer
      :title="$t('im-pei-zhi')"
      width="420"
      class="drawer-wrap"
      :visible="imDialogDrawerShow"
      :maskClosable="false"
      @close="handleCloseAllDrawer"
    >
      <div class="drawer-content">
        <CCTitle :content="$t('fu-wu-ti-gong-shang')" />
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
          v-model="imProviderSelected.imId"
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
        <CCTitle :content="$t('ding-yue-de-xiao-xi')" />
        <div>
          <i-switch true-color="#52C41A" v-model="imProviderSelected.eventProjectStatus" :disabled="imDefSelected.imType === 'none'" />
          <span class="switch-text switch-btn">{{ $t('xiang-mu-zhuang-tai-de-bian-hua') }}</span>
        </div>
        <div>
          <i-switch true-color="#52C41A" v-model="imProviderSelected.eventProjectConfig" :disabled="imDefSelected.imType === 'none'" />
          <span class="switch-text switch-btn">{{ $t('xiang-mu-pei-zhi-gai-bian') }}</span>
        </div>
        <div>
          <i-switch true-color="#52C41A" v-model="imProviderSelected.eventChangeLife" :disabled="imDefSelected.imType === 'none'" />
          <span class="switch-text switch-btn">{{ $t('bian-geng-jie-duan-bian-hua') }}</span>
        </div>
        <div>
          <i-switch true-color="#52C41A" v-model="imProviderSelected.eventChangeNotice" :disabled="imDefSelected.imType === 'none'" />
          <span class="switch-text switch-btn">{{ $t('bian-geng-zhuang-tai-xiao-xi') }}</span>
        </div>
        <CCTitle :content="$t('mo-ren-yu-yan')" />
        <RadioGroup size="small" v-model="imProviderSelected.language" type="button">
          <Radio label="zh">{{ defaultLanguageMap.zh }}</Radio>
          <!-- <Radio label="en">{{ defaultLanguageMap.en }}</Radio> -->
        </RadioGroup>
      </div>
      <div class="drawer-footer">
        <Button type="primary" @click="handleImSubmit" :disabled="isImSubmitDisabled">{{ $t('wan-cheng') }}</Button>
      </div>
    </a-drawer>
    <a-drawer
      :title="$t('fa-bu-pei-zhi')"
      width="420"
      class="drawer-wrap"
      :visible="imDialogFlowShow"
      :maskClosable="false"
      @close="handleCloseAllDrawer"
    >
      <div class="flow-drawer drawer-content">
        <Steps class="flow-drawer-steps" size="small" :current="-1">
          <Step :title="$t('sql-shen-he-0')" />
          <Step :title="$t('shen-pi-liu')"></Step>
          <Step :title="$t('fa-bu-fang-shi')"></Step>
        </Steps>
        <CCTitle :content="$t('sql-shen-he-0')" />
        <div class="flow-drawer-step">
          <RadioGroup v-model="projectFlowOption.checkStrategy" size="small" type="button">
            <Radio label="Always">{{ SQL_REVIEW_MAP.always }}</Radio>
            <Radio label="Suggest">{{ SQL_REVIEW_MAP.suggest }}</Radio>
            <Radio label="Failure">{{ SQL_REVIEW_MAP.failure }}</Radio>
            <Radio label="Skip">{{ SQL_REVIEW_MAP.skip }}</Radio>
          </RadioGroup>
          <div class="flow-drawer-step-tips">
            {{ fetchProjectFlowDescription('check', projectFlowOption.checkStrategy) }}
          </div>
        </div>
        <CCTitle :content="$t('shen-pi-liu')" />
        <div class="flow-drawer-step">
          <RadioGroup v-model="projectFlowOption.approveStrategy" size="small" type="button">
            <Radio label="Enable">{{ APPROVE_MAP.Enable }}</Radio>
            <Radio label="Disable">{{ APPROVE_MAP.Disable }}</Radio>
          </RadioGroup>
          <div class="flow-drawer-step-tips">
            {{ fetchProjectFlowDescription('approve', projectFlowOption.approveStrategy) }}
          </div>
        </div>
        <CCTitle :content="$t('fa-bu-fang-shi')" />
        <div class="flow-drawer-step">
          <RadioGroup v-model="projectFlowOption.executeStrategy" size="small" type="button" @on-change="handleFlowOfExecuteOption">
            <Radio label="Auto">{{ PUBLISH_MAP.auto }}</Radio>
            <Radio label="Manual">{{ PUBLISH_MAP.manual }}</Radio>
            <Radio label="Disabled">{{ PUBLISH_MAP.disabled }}</Radio>
          </RadioGroup>
          <div class="flow-drawer-step-tips">
            {{ fetchProjectFlowDescription('execute', projectFlowOption.executeStrategy) }}
          </div>
          <br />
          <div style="padding-bottom: 5px">{{ $t('shi-yong-shi-wu') }}</div>
          <RadioGroup v-model="projectFlowOption.transactional" size="small" type="button">
            <Radio label="Enable" :disabled="!projectFlowExecuteIsAuto">
              {{ APPROVE_MAP.Enable }}
            </Radio>
            <Radio label="Disable" :disabled="!projectFlowExecuteIsAuto">
              {{ APPROVE_MAP.Disable }}
            </Radio>
          </RadioGroup>
          <div class="flow-drawer-step-tips">
            {{ fetchProjectFlowDescription('transactional', projectFlowOption.transactional) }}
          </div>
          <br />
          <div style="padding-bottom: 5px">{{ $t('cuo-wu-ce-lve') }}</div>
          <RadioGroup v-model="projectFlowOption.errorStrategy" size="small" type="button">
            <Radio label="NONE" :disabled="!projectFlowExecuteIsAuto">
              {{ ERROR_STRATEGY_MAP.abort }}
            </Radio>
            <Radio label="RETRY" :disabled="!projectFlowExecuteIsAuto">
              {{ ERROR_STRATEGY_MAP.retry }}
            </Radio>
            <Radio label="SKIP" :disabled="!projectFlowExecuteIsAuto">
              {{ ERROR_STRATEGY_MAP.ignore }}
            </Radio>
          </RadioGroup>
          <div class="flow-drawer-step-tips">
            {{ fetchProjectFlowDescription('error', projectFlowOption.errorStrategy) }}
          </div>
        </div>
      </div>
      <div class="drawer-footer">
        <Button type="primary" @click="handleOptionSubmit">{{ $t('wan-cheng') }}</Button>
      </div>
    </a-drawer>
    <CCModal v-model="showTriggerModal" :title="$t('chu-fa')">
      <Tabs v-model="triggerTab" size="small" :animated="false" type="line">
        <TabPane :label="triggerTabLabel('WebHook')" name="WebHook">
          <div class="title-text">{{ $t('cha-kan-xiang-mu-webhook') }}</div>
          <div class="finish-wrap">
            <div class="finish-left">
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('webhook-config') }}：</span>
                <i-switch true-color="#52C41A" v-model="trigger.hookEnable" />
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('cang-ku') }}：</span>
                <Input v-model="trigger.hookRepoUrl" style="width: 380px" readonly>
                  <template #suffix>
                    <Icon type="ios-link" @click="handleJumpUrl(trigger.hookRepoUrl)" />
                  </template>
                </Input>
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('webhook-url') }}：</span>
                <Input v-model="trigger.hookUrl" style="width: 380px" readonly :disabled="!trigger.hookEnable">
                  <template #suffix>
                    <Icon type="ios-copy" @click="handleCopyTemp(trigger.hookUrl)" />
                  </template>
                </Input>
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('webhook-mi-ma') }}：</span>
                <Input v-model="trigger.hookPassword" style="width: 380px" readonly :disabled="!trigger.hookEnable">
                  <template #suffix>
                    <Icon type="ios-copy" @click="handleCopyTemp(trigger.hookPassword)" />
                  </template>
                </Input>
              </div>
              <div class="finish-title" style="height: 32px"></div>
            </div>
          </div>
        </TabPane>
        <TabPane :label="triggerTabLabel('TriggerUrl')" name="TriggerUrl">
          <div class="title-text">{{ $t('cha-kan-xiang-mu-chu-fa') }}</div>
          <div class="finish-wrap">
            <div class="finish-left">
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('webhook-config') }}：</span>
                <i-switch true-color="#52C41A" v-model="trigger.triggerEnable" />
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('token') }}：</span>
                <Input v-model="trigger.triggerToken" style="width: 380px" readonly>
                  <template #suffix>
                    <Icon type="ios-copy" @click="handleCopyTemp(trigger.triggerToken)" />
                  </template>
                </Input>
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('shi-yong-fang-shi') }}：</span>
                <RadioGroup v-model="trigger.triggerMethod" type="button" size="small" @on-change="handleTriggerUrlBuild">
                  <Radio label="http">HTTP(GET)</Radio>
                  <Radio label="wget">wget</Radio>
                  <Radio label="curl">curl</Radio>
                </RadioGroup>
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('format') }}：</span>
                <RadioGroup v-model="trigger.triggerFormat" type="button" size="small" @on-change="handleTriggerUrlBuild">
                  <Radio label="text">{{ $t('text') }}</Radio>
                  <Radio label="json">{{ $t('json') }}</Radio>
                </RadioGroup>
              </div>
              <div class="finish-title">
                <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right" />
                <Input v-model="trigger.triggerUrlShow" style="width: 380px" readonly :disabled="!trigger.triggerEnable">
                  <template #suffix>
                    <Icon type="ios-copy" @click="handleCopyTemp(trigger.triggerUrlShow)" />
                  </template>
                </Input>
              </div>
            </div>
          </div>
        </TabPane>
      </Tabs>
      <template #footer>
        <Button type="primary" @click="handleSaveTrigger">{{ $t('bao-cun') }}</Button>
        <Button
          type="primary"
          @click="handleJumpUrl(triggerTab === 'WebHook' ? trigger.hookHelpUrl : 'https://www.clougence.com/dm-doc/devops/devops_trigger')"
        >
          {{ $t('cha-kan-wen-dang') }}
        </Button>
      </template>
    </CCModal>
    <CCModal v-model="showCallbackModal" :title="$t('callback')">
      <div class="title-text">
        <CustomIcon :type="`${callbackData.enable ? 'icon-v2-SuccessColorful' : 'icon-v2-InfoColorful'}`" size="70px" bottomMargin="20px" />
        {{ $t('she-zhi-hui-diao-di-zhi-desc') }}
      </div>
      <div class="finish-wrap">
        <div class="finish-left">
          <div class="finish-title">
            <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('callback-status') }}：</span>
            <i-switch true-color="#52C41A" v-model="callbackData.enable" />
          </div>
          <div class="finish-title">
            <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('callback-method') }}：</span>
            <RadioGroup v-model="callbackData.method" type="button" size="small">
              <Radio label="POST" style="width: 60px; text-align: center" :disabled="!this.callbackData.enable">POST</Radio>
              <Radio label="GET" style="width: 60px; text-align: center" :disabled="!this.callbackData.enable">GET</Radio>
            </RadioGroup>
          </div>
          <div class="finish-title">
            <span style="padding-right: 5px; line-height: 32px; width: 100px; text-align: right">{{ $t('callback-url') }}：</span>
            <Input v-model="callbackData.url" style="width: 380px" placeholder="http://... or https://..." :disabled="!this.callbackData.enable">
              <template #suffix>
                <Icon type="ios-copy" @click="handleCopyTemp(callbackData.url)" />
              </template>
            </Input>
          </div>
        </div>
      </div>
      <template #footer>
        <Button type="primary" @click="handleSaveCallBack">{{ $t('bao-cun') }}</Button>
        <Button type="primary" @click="handleJumpUrl('https://clougence.com/dm-doc/devops/devops_callback')">
          {{ $t('cha-kan-wen-dang') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { mapState, mapGetters } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import { encryptMixin } from '@/mixins/encryptMixin';
import CCTitle from '@/components/widgets/CCTitle';
import { handleCopy } from '@/utils/clipboard';
import CCFieldEdit from '@/components/widgets/CCFieldEdit';
import CCSelectEdit from '@/components/widgets/CCSelectEdit';
import {
  projectDetailTableColumns,
  formRule,
  defaultLanguageMap,
  IM_PROVIDER_MAP,
  PROJECT_MARK_MAP,
  PROJECT_STEP_NUM,
  BECOME_STATUS_MAP,
  EVEN_TYPE_MAP,
  INIT_TYPE_MAP,
  SQL_REVIEW_MAP,
  APPROVE_MAP,
  PUBLISH_MAP,
  PROJECT_FLOW_DESCRIPTION,
  GITOPS_DESCRIPTION,
  ERROR_STRATEGY_MAP,
  CHANGE_STATUS_MAP
} from './constant';
import { DEFAULT_FLOW_OPTION, groupByRepoNamespace } from './utils';

export default {
  name: 'project',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'dmGlobalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['isSaas']),
    // 判断IM配置完成按钮是否应该禁用
    isImSubmitDisabled() {
      // 如果选择了禁用类型，按钮不禁用
      if (this.imProviderSelected?.imType === 'none') {
        return false;
      }
      // 如果选择了非禁用类型但没有选择具体的提供者，按钮禁用
      if (this.imProviderSelected?.imType !== 'none' && !this.imProviderSelected?.imId) {
        return true;
      }
      return false;
    }
  },
  data() {
    return {
      loading: false,
      projectId: '',
      projectInfo: null,
      projectReadOnly: true,
      projectOwnerInfo: {
        ownerUserName: null,
        ownerUserUID: null
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
      projectFlowExecuteIsAuto: true,
      projectFlowOption: DEFAULT_FLOW_OPTION,
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
        devOpsId: 0,
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
        devOpsId: 0,
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
      projectDetailTableColumns,
      IM_PROVIDER_MAP,
      PROJECT_MARK_MAP,
      PROJECT_STEP_NUM,
      BECOME_STATUS_MAP,
      INIT_TYPE_MAP,
      APPROVE_MAP,
      SQL_REVIEW_MAP,
      PUBLISH_MAP,
      PROJECT_FLOW_DESCRIPTION,
      GITOPS_DESCRIPTION,
      ERROR_STRATEGY_MAP,
      EVEN_TYPE_MAP,
      CHANGE_STATUS_MAP
    };
  },
  components: {
    CCTitle,
    CCFieldEdit,
    CCSelectEdit
  },
  mounted() {
    this.init();
  },
  methods: {
    groupByRepoNamespace,
    handleCopy,
    init() {
      this.projectId = this.$route.params.id;
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
        eventProjectStatus: false,
        eventProjectConfig: false,
        eventChangeLife: false,
        eventChangeNotice: false
      };
    },
    // inner methods
    async fetchDetail(successCall) {
      this.loading = true;
      const res = await this.$services.dmProjectProjectDetail({
        data: { projectId: this.projectId }
      });

      if (res.success) {
        this.projectInfo = res.data;
      }
      this.loading = false;

      if (successCall) {
        successCall(this.projectInfo);
      }
    },
    async fetchDetailApply() {
      this.projectReadOnly = this.projectInfo.status === 'DELETE' || this.projectInfo.status === 'ARCHIVE';
      this.projectInfo.mark = this.projectInfo.mark || 'CircleGray';
      this.projectOwnerInfo = {
        ownerUserName: this.projectInfo.projectOwnerName,
        ownerUserUID: this.projectInfo.projectOwnerUID
      };
      this.fetchDetailFlowOptionApply();
    },
    fetchDetailFlowOptionApply() {
      this.projectFlowOption = {
        checkStrategy: this.projectInfo.flowCheck,
        approveStrategy: this.projectInfo.flowApprove,
        executeStrategy: this.projectInfo.flowExecute,
        errorStrategy: this.projectInfo.options?.errorStrategy || 'NONE',
        transactional: (this.projectInfo.options?.transactional ? 'Enable' : 'Disable') || 'Disable'
      };
      if (this.projectFlowOption.executeStrategy === 'Auto') {
        this.projectFlowExecuteIsAuto = true;
      } else {
        this.projectFlowExecuteIsAuto = false;
        this.projectFlowOption.errorStrategy = '';
        this.projectFlowOption.transactional = '';
      }
    },
    async fetchMsgInfo() {
      const res = await this.$services.dmProjectProjectFetchImConfig({
        data: {
          projectId: this.projectId
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
      const res = await this.$services.dmProjectProjectDevopsList({
        data: {
          projectId: this.projectId
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
      const res = await this.$services.dmProjectDevopsUsers({
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
    fetchProjectFlowDescription(type, option) {
      if (type === '' || option === '') {
        return this.$t('zan-wu-miao-shu');
      }
      try {
        return PROJECT_FLOW_DESCRIPTION[type][option];
      } catch (e) {
        console.error(e);
        return this.$t('zan-wu-miao-shu');
      }
    },
    //
    // handle methods
    async handleProjectMark(key) {
      const res = await this.$services.dmProjectProjectUpdate({
        data: {
          projectId: this.projectId,
          newMark: key
        }
      });
      if (res.success) {
        this.projectInfo.mark = key;
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleNameEdit(data) {
      const res = await this.$services.dmProjectProjectUpdate({
        data: {
          projectId: this.projectId,
          newName: data
        }
      });
      if (res.success) {
        this.projectInfo.name = data;
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleAdminEdit(data) {
      const res = await this.$services.dmProjectProjectUpdate({
        data: {
          projectId: this.projectId,
          newAdminUid: data.value
        }
      });
      if (res.success) {
        this.projectOwnerInfo = {
          ownerUserName: data.label,
          ownerUserUID: data.value
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
              const res = await this.$services.dmProjectProjectDelete({
                data: {
                  projectId: this.projectId
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
          fn = this.$services.dmProjectProjectArchive;
          break;
        case 'resetArchive':
          fn = this.$services.dmProjectProjectRecover;
          break;
        default:
          return;
      }
      const res = await fn({
        data: {
          projectId: this.projectId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.init();
      }
    },
    //
    async handleImEdit() {
      if (!this.projectReadOnly) {
        await this.fetchImDefList();
        await this.handleImDefOne(this.imDefSelected);
        this.imDialogDrawerShow = true;
      }
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
        res = await this.$services.dmProjectProjectPushImConfig({
          data: {
            projectId: this.projectId,
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
      if (!this.projectReadOnly) {
        this.fetchDetailFlowOptionApply();
        this.imDialogFlowShow = true;
      }
    },
    handleFlowOfExecuteOption() {
      if (this.projectFlowOption.executeStrategy === 'Auto') {
        this.projectFlowOption.transactional = (this.projectInfo.options?.transactional ? 'Enable' : 'Disable') || 'Disable';
        this.projectFlowOption.errorStrategy = this.projectInfo.options?.errorStrategy || 'NONE';
        this.projectFlowExecuteIsAuto = true;
      } else {
        this.projectFlowExecuteIsAuto = false;
        this.projectFlowOption.errorStrategy = '';
        this.projectFlowOption.transactional = '';
      }
    },
    async handleOptionSubmit() {
      const res = await this.$services.dmProjectProjectPushFlowConfig({
        data: {
          projectId: this.projectId,
          ...this.projectFlowOption,
          transactional: this.projectFlowOption.transactional === 'Enable'
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
      if (!this.projectReadOnly) {
        this.fetchDevopsScmList();
        this.fetchInsList();
        this.devopsScmSelected = null;
        this.devopsRepoList = [];
        this.devopsRepoSelected = null;
        this.devopsInsSelected = null;
        this.devopsInsHasCatalog = false;
        this.devopsInsHasSchema = false;
        this.devopsInsCatalogList = [];
        this.devopsInsSchemaList = [];

        this.formModal.eventType = 'Push';
        this.formModal.initScript = 'Snapshot';
        this.formModal.catalogName = '';
        this.formModal.schemaName = '';

        Promise.resolve().then(() => {
          this.imDialogDevOpsShow = true;
        });
      }
    },
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
      this.devopsScmSelected = this.devopsScmList.find((scm) => scm.scmId === this.formModal.repoScmId);
      if (this.formModal.repoScmId) {
        await this.fetchDevopsScmRepos();
      }
    },
    async fetchDevopsScmRepos() {
      this.repoLoading = true;
      const res = await this.$services.dmProjectDevopsRepos({
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
      const res = await this.$services.dmProjectDevopsDsInsLevels();

      if (res.success) {
        this.devopsInsList = res.data;
      }
    },
    async fetchCatalogList(isRefreshCache = false) {
      if (!this.devopsInsSelected || !this.devopsInsSelected?.objAttr?.dsEnvId) {
        return;
      }

      const res = await this.$services.dmProjectDevopsDsDbLevels({
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

      const res = await this.$services.dmProjectDevopsDsDbLevels({
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

      const projectForm = {
        projectId: this.projectId,
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

      const res = await this.$services.dmProjectProjectDevopsCreate({
        data: projectForm
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
        this.$refs.formModal.resetFields();
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
        devOpsId: item.devopsId,
        hookEnable: item.webHookEnable,
        hookUrl: item.webHookUrl,
        hookPassword: item.webHookPwd,
        hookHelpUrl: item.webHookHelpUrl,
        hookRepoUrl: item.repoUrl,
        triggerEnable: item.triggerEnable,
        triggerUrl: item.triggerUrl,
        triggerUrlShow: `${item.triggerUrl}&format=text`,
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
          h('span', { style: { marginLeft: '5px', marginRight: '5px' } }, `${this.$t('yuan-cheng-chu-fa')}`)
        ];
      } else {
        return 'Unknown';
      }
    },
    handleTriggerUrlBuild() {
      if (this.trigger.triggerMethod === 'http') {
        this.trigger.triggerUrlShow = `${this.trigger.triggerUrl}&format=${this.trigger.triggerFormat}`;
      } else if (this.trigger.triggerMethod === 'wget') {
        this.trigger.triggerUrlShow = `wget -q -O- "${this.trigger.triggerUrl}&format=${this.trigger.triggerFormat}"`;
      } else if (this.trigger.triggerMethod === 'curl') {
        this.trigger.triggerUrlShow = `curl "${this.trigger.triggerUrl}&format=${this.trigger.triggerFormat}"`;
      }
    },
    async handleSaveTrigger() {
      const res = await this.$services.dmProjectProjectTriggerConfig({
        data: {
          projectId: this.projectId,
          devopsId: this.trigger.devOpsId,
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
      this.callbackData.devOpsId = item.devopsId;
      this.callbackData.enable = item.callbackEnable;
      this.callbackData.method = item.callbackMethod;
      this.callbackData.url = item.callbackUrl;
    },
    async handleSaveCallBack() {
      const res = await this.$services.dmProjectProjectCallBackConfig({
        data: {
          projectId: this.projectId,
          devopsId: this.callbackData.devOpsId,
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

      const res = await this.$services.dmProjectChangeChangeList({
        data: {
          projectId: this.projectId,
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
      if (PROJECT_STEP_NUM[step] < PROJECT_STEP_NUM[row.currentStep]) {
        return '#59c378';
      } else if (PROJECT_STEP_NUM[step] > PROJECT_STEP_NUM[row.currentStep]) {
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
          const res = await this.$services.dmProjectProjectDevopsDelete({
            data: {
              projectId: this.projectId,
              devopsId: data.devopsId
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
          const res = await this.$services.dmProjectProjectDevopsSwitch({
            data: {
              projectId: this.projectId,
              devopsId: data.devopsId,
              enable: true
            }
          });
        }
      });
    },
    disableDevops(data) {
      this.$Modal.confirm({
        title: this.$t('que-ren-shi-fou-jin-yong'),
        content: this.$t('jin-yong-fa-bu-liu'),
        onOk: async () => {
          const res = await this.$services.dmProjectProjectDevopsSwitch({
            data: {
              projectId: this.projectId,
              devopsId: data.devopsId,
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
          const res = await this.$services.dmProjectProjectTriggerChange({
            data: {
              projectId: this.projectId,
              devopsId: devopsItem.devopsId
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
          const res = await this.$services.dmProjectProjectTriggerSnapshot({
            data: {
              projectId: this.projectId,
              devopsId: devopsItem.devopsId
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
.project-wrap {
  padding-bottom: 0 !important;

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

.project-base {
  width: 230px;
}

.project-base-info {
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

.project-overflow {
  display: inline-block;
  width: 150px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  vertical-align: bottom;
}
</style>
