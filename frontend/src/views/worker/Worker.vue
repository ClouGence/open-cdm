<template>
  <div style="padding: 16px" :class="{ 'is-dark': currentTheme === 'dark' }">
    <second-confirm-modal
      :title="$t('shan-chu-ji-qi')"
      :event="SECOND_CONFIRM_EVENT_LIST.DELETE_WORKER"
      :text="$t('yi-wan-cheng-shan-chu-selectworkerid-ji-qi-de-cao-zuo', [selectWorker.workerDesc])"
      v-model:visible="showConfirmDelete"
      ref="second-confirm-modal"
      :handle-confirm="handleConfirmDeleteCluster"
      :handle-close="handleCancelUpdateAlarm"
    />
    <Breadcrumb>
      <BreadcrumbItem to="/ccsystem/resource">{{ $t('tong-bu-ji-qi') }}</BreadcrumbItem>
      <BreadcrumbItem>{{ $t('ji-qi-lie-biao') }}</BreadcrumbItem>
    </Breadcrumb>
    <div class="worker-list-content">
      <div class="job-header border-radius-card-top">
        <span class="job-header-db">
          <i class="iconfont iconClass"></i>
        </span>
        <div class="job-header-name">
          <p>
            <span class="job-header-name-main">{{ clusterInfo.clusterName }}</span>
            <span class="job-header-name-desc" v-if="clusterInfo.cloudOrIdcName">
              {{ Mapping.cloudOrIdcName[clusterInfo.cloudOrIdcName] }}
            </span>
            <span class="job-header-name-desc" v-if="clusterInfo.region">
              <i class="iconfont icondingwei"></i>
              {{ Mapping.region[clusterInfo.region] }}
            </span>
          </p>
          <p class="job-header-desc">
            {{ $t('miao-shu-rdsinstancedesc', [clusterInfo.clusterDesc]) }}
          </p>
        </div>
        <div class="job-header-buttons">
          <!-- <div style="display: inline-block;">
            <a style="margin-right: 12px" v-if="!showSearch" @click="handleShowSearch(true)">{{ $t('sou-suo-bang-ding-ren-wu') }}</a>
            <Form v-if="showSearch" ref="formInline" inline label-position="right" style="position: relative;top: -6px">
              <FormItem>
                <Select v-model="searchType" style="width:160px" @on-change="handleChangeSearchType">
                  <Option value="sourceDs" :label="$t('yuan-shu-ju-ku')">
                    <span>{{ $t('yuan-shu-ju-ku') }}</span>
                  </Option>
                  <Option value="targetDs" :label="$t('mu-biao-shu-ju-ku')">
                    <span>{{ $t('mu-biao-shu-ju-ku') }}</span>
                  </Option>
                </Select>
              </FormItem>
              <FormItem v-if="searchType==='sourceDs'">
                <Select v-model="searchKey.sourceInstanceId" style="width:250px" filterable>
                  <Option value="all">{{ $t('quan-bu') }}</Option>
                  <Option v-for="(instance) in dsList"
                          :value="instance.id"
                          :key="instance.instanceId"
                          :label="instance.instanceDesc?`${instance.instanceId} (${instance.instanceDesc})`:`${instance.instanceId}`">
                    <p>{{ instance.instanceId }}</p>
                    <p style="margin-top:5px;color:#ccc">{{ instance.instanceDesc }}</p>
                  </Option>
                </Select>
              </FormItem>
              <FormItem v-if="searchType==='targetDs'">
                <Select v-model="searchKey.targetInstanceId" style="width:250px" filterable>
                  <Option value="all">{{ $t('quan-bu') }}</Option>
                  <Option v-for="(instance) in dsList"
                          :value="instance.id"
                          :key="instance.instanceId"
                          :label="instance.instanceDesc?`${instance.instanceId} (${instance.instanceDesc})`:`${instance.instanceId}`">
                    <p>{{ instance.instanceId }}</p>
                    <p style="margin-top:5px;color:#ccc">{{ instance.instanceDesc }}</p>
                  </Option>
                </Select>
              </FormItem>
              <FormItem>
                <Button type="primary" @click="handleRefresh" :loading="refreshLoading">{{ $t('cha-xun') }}</Button>
              </FormItem>
            </Form>
          </div>
          <a style="display:inline-block;cursor: pointer;margin-right: 12px" v-if="showSearch" @click="handleShowSearch(false)">
            {{ $t('he-shang') }}
          </a> -->
          <Button v-if="myAuth.includes('CC_WORKER_MANAGE')" type="primary" style="margin-right: 6px" @click="handleAddWorker" ghost>
            <Icon type="md-add" />
            {{ $t('xin-zeng-ji-qi') }}
          </Button>
          <Button type="default" @click="handleRefresh" :loading="refreshLoading">
            <CustomIcon type="icon-v2-Refresh" v-if="!refreshLoading" />
          </Button>
        </div>
      </div>
      <div class="worker-body">
        <div class="worker-item-container">
          <p class="worker-item-title">{{ $t('ji-qi-lie-biao') }}</p>
          <div class="task-list-none mt-20" v-if="resourceData.length === 0 && !refreshLoading">
            <svg style="margin: 0 auto" class="icon svg-icon" aria-hidden="true">
              <use xlink:href="#icon-zanwushujuimg"></use>
            </svg>
            <p style="margin-bottom: 6px">
              {{ $t('nin-huan-mei-you-tian-jia-ji-qi') }}
              <a class="text-cc-primary" @click="handleAddWorker" v-if="myAuth.includes('CC_WORKER_MANAGE')">
                {{ $t('xin-zeng-ji-qi') }}
              </a>
            </p>
            <a target="_blank" :href="`${store.state.docUrlPrefix}/productOP/docker/install_worker_docker`">
              {{ $t('ru-he-tian-jia-ji-qi') }}
            </a>
          </div>
          <div
            v-for="(worker, index) in resourceData"
            :key="index"
            :class="`worker-item-content ${selectWorker.id === worker.id ? 'worker-item-selected' : ''}`"
            @click="handleSelectWorker(worker)"
          >
            <div class="worker-info-content">
              <span class="status-point" :style="`background-color: ${getPointColor(worker)}`"></span>
              <p>
                <span class="worker-ip" @click="handleGoMonitor(worker)">
                  {{ worker.privateIp ? worker.privateIp : $t('dai-que-ren') }}
                </span>
                <span :style="`color: ${getWorkerStatusColor(worker)}`" class="worker-status">
                  {{ getWorkerStatus(worker) }}
                  <span
                    v-if="
                      worker.deployStatus &&
                      ![
                        OPERATION_STATUS.UPGRADED,
                        OPERATION_STATUS.ROLLED_BACK,
                        OPERATION_STATUS.INSTALLED,
                        OPERATION_STATUS.CANCEL_UPGRADE,
                        OPERATION_STATUS.CANCEL_ROLL_BACK
                      ].includes(worker.deployStatus)
                    "
                  >
                    ({{ OPERATION_STATUS_I18N[worker.deployStatus] }})
                  </span>
                </span>
                <Tooltip
                  placement="right"
                  class="alarm-icon"
                  transfer
                  :content="$t('cun-zai-yi-chang-de-hou-tai-ren-wu-qing-dian-ji-chu-li')"
                  v-if="worker.consoleTaskState === 'FAILED'"
                >
                  <span style="display: inline-block; position: relative; z-index: 999" @click="handleGoConsoleJob(worker)">
                    <i class="iconfont iconyibuforce"></i>
                  </span>
                </Tooltip>
              </p>
              <div class="worker-info">
                <div class="worker-desc flex items-center">
                  {{ $t('worker-miao-shu', [worker.workerDesc ? worker.workerDesc : $t('zan-wu-miao-shu')]) }}
                  <Poptip placement="right" class="show-more-info">
                    <i class="iconfont iconbianji1 font-[20px]"></i>
                    <template #content>
                      <div>
                        <span>{{ $t('gong-wang-xin-xi') }}:</span>
                        <span style="font-weight: 500; width: 200px; display: inline-block">
                          {{ worker.publicIp }}
                        </span>
                        <span>{{ $t('di-qu-0') }}</span>
                        <span style="font-weight: 500; width: 200px; display: inline-block">
                          {{ Mapping.region[worker.region] }}
                        </span>
                      </div>
                      <div style="margin-top: 10px">
                        <span>{{ $t('chuang-jian-shi-jian-0') }}</span>
                        <span style="font-weight: 500; width: 200px; display: inline-block">
                          {{ fecha.format(new Date(worker.gmtCreate), 'YYYY-MM-DD HH:mm:ss') }}
                        </span>
                        <span>{{ $t('ji-qi-lei-xing') }}:</span>
                        <span style="font-weight: 500; width: 200px; display: inline-block">
                          {{ Mapping.workerType[worker.workerType] }}
                        </span>
                      </div>
                      <div style="margin-top: 10px">
                        <span>{{ $t('ji-qi-wei-yi-biao-shi') }}</span>
                        <Tooltip :content="worker.workerSeqNumber" placement="top-start">
                          <span
                            style="
                              font-weight: 500;
                              width: 200px;
                              display: inline-block;
                              overflow: hidden;
                              text-overflow: ellipsis;
                              vertical-align: middle;
                            "
                          >
                            {{ worker.workerSeqNumber }}
                          </span>
                        </Tooltip>
                        <i style="color: #0bb9f8; cursor: pointer" @click="handleCopy(worker.workerSeqNumber)" class="iconfont iconcopy"></i>
                      </div>
                      <div style="margin-top: 10px">
                        <span>{{ $t('zui-jin-geng-xin-ri-qi') }}</span>
                        <span style="font-weight: 500; width: 200px; display: inline-block">
                          {{ fecha.format(new Date(worker.installOrUpgradeDate), 'YYYY-MM-DD HH:mm:ss') }}
                        </span>
                        <span>{{ $t('dang-qian-ban-ben') }}</span>
                        <span style="font-weight: 500; width: 200px; display: inline-block">
                          {{ worker.version }}
                        </span>
                      </div>
                      <div style="margin-top: 10px">
                        <span>{{ $t('ji-qi-chao-mai-bi-li') }}</span>
                        <span>
                          <span style="font-weight: 500; display: inline-block">{{ worker.memOverSoldPercent }}%</span>
                          <Poptip :title="$t('xiu-gai-chao-mai-bi-li')" placement="left" v-model="worker.showMenOverEdit">
                            <Icon style="margin-left: 6px" type="md-create" @click="handleShowMenOverEdit(worker)" />
                            <template #content>
                              <p style="margin-bottom: 20px; color: rgb(237, 64, 20)">
                                {{ $t('xiu-gai-chao-mai-bi-li-guo-du-ke-neng-hui-dao-zhi-gai-ji-qi-shang-de-ren-wu-yi-chang-qing-jin-shen-cao-zuo') }}
                              </p>
                              <Form :label-width="120">
                                <FormItem :label="$t('xiu-gai-bi-li-wei')">
                                  <Input
                                    v-model="currentMenOver"
                                    style="width: 200px; margin-right: 6px"
                                    :placeholder="$t('qing-shu-ru-50400-de-zhi')"
                                  ></Input>
                                  %
                                </FormItem>
                              </Form>
                              <div style="margin: 10px 0; text-align: center">
                                <Button @click="handleCancelMenEdit(worker)">
                                  {{ $t('qu-xiao') }}
                                </Button>
                                <Button type="primary" style="margin-right: 10px" @click="handleConfirmMenOverEdit(worker)">
                                  {{ $t('que-ding') }}
                                </Button>
                              </div>
                            </template>
                          </Poptip>
                        </span>
                      </div>
                    </template>
                  </Poptip>
                </div>
              </div>
              <div v-if="worker.version" style="line-height: 24px">{{ $t('ban-ben-workerversion', [worker.version]) }}</div>
              <div class="worker-download-status flex items-center" v-if="worker.downloadDsStatus" style="line-height: 24px">
                {{ $t('tuo-zhan-shu-ju-yuan-bao') }}：{{ worker.downloadDsStatusI18n }}
                <i-circle
                  :percent="worker.downloadDsPercent"
                  :size="14"
                  :stroke-width="8"
                  :trail-width="8"
                  stroke-color="#5cb85c"
                  class="ml-[4px]"
                  v-if="worker.downloadDsStatus === DOWNLOAD_STATUS.DOWNLOADING"
                />
                <span class="ml-[4px]" v-if="worker.downloadDsStatus === DOWNLOAD_STATUS.DOWNLOADING">{{ worker.downloadDsPercent }}%</span>
              </div>
              <div class="worker-item-circle">
                <Row :gutter="8">
                  <Col :span="6">
                    <span
                      :style="`font-size:24px;line-height: 56px;color:${
                        worker.taskScheduleVOs && worker.taskScheduleVOs.length > 100 ? 'rgb(255, 24, 21)' : 'rgba(0, 0, 0, 0.88)'
                      }`"
                    >
                      {{ worker.taskCount }}
                    </span>
                    <p style="font-size: 13px">{{ $t('ren-wu-shu') }}</p>
                  </Col>
                  <Col :span="6">
                    <span
                      :style="`font-size:24px;line-height: 56px;color:${
                        worker.workerLoad > worker.logicalCoreNum ? 'rgb(255, 24, 21)' : 'rgba(0, 0, 0, 0.88)'
                      }`"
                    >
                      {{ worker.workerLoad }}
                    </span>
                    <p style="font-size: 13px">{{ $t('5m-ping-jun-cpu-fu-zai') }}</p>
                  </Col>
                  <Col :span="6">
                    <i-circle
                      :size="50"
                      :trail-width="8"
                      :stroke-width="8"
                      :percent="worker.cpuUseRatio"
                      :stroke-color="worker.cpuUseRatio < 40 ? '#19be6b' : worker.cpuUseRatio > 70 ? '#ed4014' : '#ff9900'"
                    >
                      <span class="demo-Circle-inner" style="font-size: 24px">{{ worker.cpuUseRatio }}%</span>
                    </i-circle>
                    <p style="font-size: 13px">{{ $t('dang-qian-cpu-shi-yong-shuai') }}</p>
                  </Col>
                  <Col :span="6">
                    <i-circle
                      :size="50"
                      :trail-width="8"
                      :stroke-width="8"
                      :percent="(worker.totalTaskMemMb / worker.physicMemMb) * 100"
                      :stroke-color="
                        worker.totalTaskMemMb / worker.physicMemMb < 0.44
                          ? '#19be6b'
                          : worker.totalTaskMemMb / worker.physicMemMb > 0.7
                            ? '#ed4014'
                            : '#ff9900'
                      "
                    >
                      <span class="demo-Circle-inner" style="font-size: 24px">
                        {{ ((worker.totalTaskMemMb / worker.physicMemMb) * 100).toFixed(2) }}%
                      </span>
                    </i-circle>
                    <p style="font-size: 13px">{{ $t('dang-qian-nei-cun-shi-yong-shuai') }}</p>
                  </Col>
                </Row>
              </div>
              <Icon type="ios-pulse" class="to-monitor-icon" @click="toMonitorPage(worker)" />
            </div>
            <div class="worker-item-action" v-if="myAuth.includes('CC_WORKER_MANAGE') && selectWorker.id === worker.id">
              <span
                class="worker-action-item"
                @click="handleShowConfirmOnline('START_CLIENT')"
                v-if="
                  (worker.workerState === 'OFFLINE' || worker.workerState === 'WAIT_TO_OFFLINE') &&
                  ((worker.deployStatus === 'INSTALLED' && worker.cloudOrIdcName === 'ALIBABA_CLOUD') || worker.cloudOrIdcName !== 'ALIBABA_CLOUD')
                "
              >
                <i class="iconfont icononline"></i>
                {{ $t('qi-dong') }}
              </span>
              <span
                class="worker-action-item worker-action-item-disabled"
                v-if="
                  (worker.workerState !== 'OFFLINE' && worker.workerState !== 'WAIT_TO_OFFLINE') ||
                  (worker.deployStatus !== 'INSTALLED' && worker.cloudOrIdcName === 'ALIBABA_CLOUD')
                "
              >
                <i class="iconfont icononline"></i>
                {{ $t('qi-dong') }}
              </span>
              <span
                class="worker-action-item"
                @click="handleShowConfirmOffline"
                v-if="worker.workerState === 'ONLINE' || worker.workerState === 'WAIT_TO_ONLINE' || worker.workerState === 'ABNORMAL'"
              >
                <i class="iconfont iconoutline"></i>
                {{ $t('ting-zhi') }}
              </span>
              <span
                class="worker-action-item worker-action-item-disabled"
                v-if="!(worker.workerState === 'ONLINE' || worker.workerState === 'WAIT_TO_ONLINE' || worker.workerState === 'ABNORMAL')"
              >
                <i class="iconfont iconoutline"></i>
                {{ $t('ting-zhi') }}
              </span>
              <!--              <span class="worker-action-item" @click="handleDeployCore(worker)"-->
              <!--                    v-if="worker.cloudOrIdcName==='ALIBABA_CLOUD'&&-->
              <!--                    (worker.workerState==='OFFLINE'&&-->
              <!--                    (worker.deployStatus==='UNINSTALLED'||worker.deployStatus==='')||-->
              <!--                    ((worker.workerState==='WAIT_TO_OFFLINE'&&-->
              <!--                    worker.deployStatus==='UNINSTALLED')))"><i-->
              <!--                class="iconfont iconanzhuang"></i>{{ $t('an-zhuang') }}</span>-->
              <!--              <span class="worker-action-item worker-action-item-disabled"-->
              <!--                    v-if="worker.cloudOrIdcName==='ALIBABA_CLOUD'&&-->
              <!--                    ((worker.workerState!=='OFFLINE'&&-->
              <!--                    !(worker.workerState==='WAIT_TO_OFFLINE'&&-->
              <!--                    worker.deployStatus==='UNINSTALLED'))||-->
              <!--                    (worker.deployStatus!=='UNINSTALLED'&&worker.deployStatus!==''))"><i-->
              <!--                class="iconfont iconanzhuang"></i>{{ $t('an-zhuang') }}</span>-->
              <!--              <span class="worker-action-item worker-action-item-disabled"-->
              <!--                    v-if="worker.cloudOrIdcName==='ALIBABA_CLOUD'&&-->
              <!--                    (worker.deployStatus!=='INSTALLED'||-->
              <!--                    worker.consoleTaskState!=='SUCCESS')"><i-->
              <!--                class="iconfont iconxiezai"></i>{{ $t('geng-xin') }}</span>-->
              <!--              <span class="worker-action-item"-->
              <!--                    v-if="worker.cloudOrIdcName==='ALIBABA_CLOUD'&&!(worker.deployStatus!=='INSTALLED'||worker.consoleTaskState!=='SUCCESS')"-->
              <!--                    @click="handleUpgradeAll(worker)"><i class="iconfont iconxiezai"></i>{{ $t('geng-xin') }}</span>-->
              <!--              <span class="worker-action-item"-->
              <!--                    v-if="worker.cloudOrIdcName==='ALIBABA_CLOUD'&&(worker.workerState==='WAIT_TO_OFFLINE'||worker.workerState==='OFFLINE')&&worker.deployStatus==='INSTALLED'"-->
              <!--                    @click="handleUnInstall(worker)"><i class="iconfont iconxiezai"></i>{{ $t('xie-zai') }}</span>-->
              <!--              <span class="worker-action-item worker-action-item-disabled"-->
              <!--                    v-if="worker.cloudOrIdcName==='ALIBABA_CLOUD'&&(!(worker.workerState==='WAIT_TO_OFFLINE'||worker.workerState==='OFFLINE')||worker.deployStatus!=='INSTALLED')"><i-->
              <!--                class="iconfont iconxiezai"></i>{{ $t('xie-zai') }}</span>-->
              <!--              <span class="worker-action-item" @click="handleDownloadClient(worker)"-->
              <!--                    v-if="worker.cloudOrIdcName==='SELF_MAINTENANCE'&&!store.getters.isProductTrail"><i class="iconfont iconKHD"></i>{{ $t('xia-zai-ke-hu-duan') }}</span>-->
              <Tooltip placement="bottom">
                <template #content>
                  <p>
                    {{ $t('xiang-yao-zeng-jia-ji-qi-qing-cha-kan-wen-dang') }}
                    <a :href="`${store.state.docUrlPrefix}/productOP/systemDeploy/ha_install`" target="_blank">
                      {{ $t('gao-ke-yong-bu-shu') }}
                    </a>
                  </p>
                </template>
                <span
                  class="worker-action-item worker-action-item-disabled"
                  v-if="worker.cloudOrIdcName !== 'ALIBABA_CLOUD' && store.getters.isProductTrail"
                >
                  <i class="iconfont iconKHD"></i>
                  {{ $t('xia-zai-ke-hu-duan') }}
                </span>
              </Tooltip>
              <span class="worker-action-item" @click="handleDownloadConfig(worker)">
                <i class="iconfont iconPZWJ"></i>
                {{ $t('cha-kan-pei-zhi-wen-jian') }}
              </span>
              <span
                class="worker-action-item"
                @click="handleDeleteWorker(worker)"
                v-if="
                  ((worker.workerState === 'OFFLINE' || worker.workerState === 'WAIT_TO_OFFLINE') &&
                    ((worker.deployStatus === 'UNINSTALLED' && worker.cloudOrIdcName === 'ALIBABA_CLOUD') ||
                      worker.cloudOrIdcName === 'SELF_MAINTENANCE')) ||
                  (worker.cloudOrIdcName !== 'ALIBABA_CLOUD' && !worker.privateIp) ||
                  worker.healthLevel !== 'Health'
                "
              >
                <i class="iconfont icondel"></i>
                {{ $t('shan-chu') }}
              </span>
              <span
                class="worker-action-item worker-action-item-disabled"
                v-if="
                  (!(worker.workerState === 'OFFLINE' || worker.workerState === 'WAIT_TO_OFFLINE') ||
                    (worker.deployStatus !== 'UNINSTALLED' && worker.cloudOrIdcName === 'ALIBABA_CLOUD')) &&
                  !(worker.cloudOrIdcName !== 'ALIBABA_CLOUD' && !worker.privateIp) &&
                  worker.healthLevel === 'Health' &&
                  (!store.getters.isProductTrail || worker.id !== 1)
                "
              >
                <i class="iconfont icondel"></i>
                {{ $t('shan-chu') }}
              </span>
              <Dropdown class="worker-operation-dropdown" style="margin-left: 10px">
                <span>
                  {{ $t('geng-duo') }}
                  <Icon type="ios-arrow-down"></Icon>
                </span>
                <template #list>
                  <DropdownMenu>
                    <DropdownItem>
                      <span class="worker-action-item dropdown-content" @click="showCheckWhiteListModal = true">
                        {{ $t('jian-cha-bai-ming-dan') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem>
                      <span class="worker-action-item dropdown-content" @click="showAddWhiteListModal = true">
                        {{ $t('tian-jia-bai-ming-dan') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem :disabled="disableUpgradeWorker(worker)" v-if="worker.cloudOrIdcName === 'SELF_MAINTENANCE' && upgradeSidecar">
                      <span
                        class="worker-action-item dropdown-content"
                        @click="disableUpgradeWorker(worker) ? null : handleShowConfirmOnline(WORKER_OPERATION.UPGRADE_ALL)"
                      >
                        {{ $t('sheng-ji-ke-hu-duan') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem
                      :disabled="disableCancelUpgradeWorker(worker)"
                      v-if="worker.cloudOrIdcName === 'SELF_MAINTENANCE' && upgradeSidecar"
                    >
                      <span
                        class="worker-action-item dropdown-content"
                        @click="disableCancelUpgradeWorker(worker) ? null : handleShowCancelModal(WORKER_OPERATION.CANCEL_UPGRADE)"
                        :disabled="![OPERATION_STATUS.PREPARING_UPGRADE].includes(worker.deployStatus)"
                      >
                        {{ $t('qu-xiao-sheng-ji-ke-hu-duan') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem :disabled="disableRollbackWorker(worker)" v-if="worker.cloudOrIdcName === 'SELF_MAINTENANCE' && upgradeSidecar">
                      <span
                        class="worker-action-item dropdown-content"
                        @click="disableRollbackWorker(worker) ? null : handleShowConfirmOnline(WORKER_OPERATION.ROLLBACK_CLIENT)"
                      >
                        {{ $t('hui-gun-ke-hu-duan') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem
                      :disabled="disableCancelRollbackWorker(worker)"
                      v-if="worker.cloudOrIdcName === 'SELF_MAINTENANCE' && upgradeSidecar"
                    >
                      <span
                        class="worker-action-item"
                        @click="disableCancelRollbackWorker(worker) ? null : handleShowCancelModal(WORKER_OPERATION.CANCEL_ROLLBACK)"
                        :disabled="![OPERATION_STATUS.PREPARING_ROLL_BACK].includes(worker.deployStatus)"
                      >
                        {{ $t('qu-xiao-hui-gun-ke-hu-duan') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem>
                      <span class="worker-action-item dropdown-content" @click="goConsoleJobList(worker)">
                        {{ $t('cha-kan-yi-bu-ren-wu') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem>
                      <span class="worker-action-item dropdown-content" @click="handleCleanWorkerLogModal(worker)">
                        {{ $t('qing-li-ji-qi-ri-zhi') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem>
                      <span class="worker-action-item dropdown-content" @click="handleShowUpdateExternalIp(worker)">
                        {{ $t('geng-xin-gong-wang-ip') }}
                      </span>
                    </DropdownItem>
                  </DropdownMenu>
                </template>
              </Dropdown>
              <span class="worker-action-off">
                <Checkbox @on-change="handleAlarm(worker, $event)" v-model="worker.alertConfigVO.dingding">
                  {{ $t('cun-huo-xing-jian-ce') }}
                </Checkbox>
              </span>
            </div>
          </div>
        </div>
        <div class="worker-detail-container">
          <div v-if="!selectWorker.workerName" style="width: 100%; height: 700px"></div>
          <div
            class="install-worker-process-content"
            v-if="
              ((selectWorker && selectWorker.currentStatus === 'process') || selectWorker.consoleTaskState === 'EXECUTE') &&
              selectWorker &&
              selectWorker.deployStatus === 'INSTALLING'
            "
          >
            <p class="install-worker-process">
              <a class="install-worker-status" @click="handleGoConsoleJob(selectWorker)">
                {{ $t('ke-hu-duan-an-zhuang-zhong') }}
              </a>
              {{ $t('zhe-ge-guo-cheng-ke-neng-xu-yao-ji-fen-zhong') }}
            </p>
            <!--                        <div class="install-worker-process-img"></div>-->
            <Progress style="position: absolute; bottom: -8px" :percent="99.99" status="active" hide-info :stroke-width="2" />
          </div>
          <div
            class="install-worker-process-content"
            v-if="
              ((selectWorker && selectWorker.currentStatus === 'process') || selectWorker.consoleTaskState === 'EXECUTE') &&
              selectWorker &&
              selectWorker.deployStatus === 'UNINSTALLING'
            "
          >
            <p class="install-worker-process">
              <a class="install-worker-status" @click="handleGoConsoleJob(selectWorker)">
                {{ $t('ke-hu-duan-xie-zai-zhong') }}
              </a>
              {{ $t('zhe-ge-guo-cheng-ke-neng-xu-yao-ji-fen-zhong') }}
            </p>
            <!--                        <div class="install-worker-process-img"></div>-->
            <Progress style="position: absolute; bottom: -8px" :percent="99.99" status="active" hide-info :stroke-width="2" />
          </div>
          <div v-if="selectWorker && selectWorker.currentStatus === 'error' && selectWorker.deployStatus === 'INSTALLING'">
            <Alert type="error" show-icon>
              {{ $t('an-zhuang-shi-bai') }}
              <a @click="handleStopInstallAndClean" style="margin-left: 4px">
                {{ $t('qu-xiao-an-zhuang') }}
              </a>
            </Alert>
          </div>
          <div v-if="selectWorker && selectWorker.currentStatus === 'error' && selectWorker.deployStatus === 'UNINSTALLING'">
            <Alert type="error" show-icon>
              {{ $t('xie-zai-shi-bai') }}
              <a @click="handleStopInstallAndClean" style="margin-left: 4px">
                {{ $t('qu-xiao-xie-zai') }}
              </a>
            </Alert>
          </div>
          <div class="worker-detail-metric border-radius-card" v-if="selectWorker.workerName">
            <div class="worker-detail-metric-item">
              <p class="worker-count">{{ selectWorker.logicalCoreNum }}</p>
              <p>{{ $t('cpu-luo-ji-he-shu-0') }}</p>
            </div>
            <div class="worker-detail-metric-item">
              <p class="worker-count">
                {{ selectWorker.physicDiskGb }}
                <span style="font-size: 16px; font-family: PingFangSC-Regular, serif">GB</span>
              </p>
              <p>{{ $t('wu-li-ci-pan-rong-liang-0') }}</p>
            </div>
            <div class="worker-detail-metric-item">
              <p class="worker-count">
                {{ selectWorker.physicMemMb }}
                <span style="font-size: 16px; font-family: PingFangSC-Regular, serif">MB</span>
              </p>
              <p>{{ $t('wu-li-nei-cun-rong-liang-0') }}</p>
            </div>
            <div class="worker-detail-metric-item">
              <p class="worker-count">
                {{ selectWorker.totalTaskMemMb }}
                <span style="font-size: 16px; font-family: PingFangSC-Regular, serif">MB</span>
              </p>
              <p>{{ $t('ren-wu-yi-zhan-yong-nei-cun') }}</p>
            </div>
            <div class="worker-detail-metric-item">
              <p class="worker-count">
                {{ selectWorker.freeMemMb }}
                <span style="font-size: 16px; font-family: PingFangSC-Regular, serif">MB</span>
              </p>
              <p>{{ $t('kong-xian-nei-cun-0') }}</p>
            </div>
            <div class="worker-detail-metric-item">
              <p class="worker-count">
                {{ selectWorker.freeDiskGb }}
                <span style="font-size: 16px; font-family: PingFangSC-Regular, serif">GB</span>
              </p>
              <p>{{ $t('kong-xian-ci-pan-0') }}</p>
            </div>
          </div>
          <div class="worker-task-container" v-if="selectWorker.workerName">
            <p style="font-weight: 500; font-size: 16px">
              <span class="warn-point" style="background-color: rgba(0, 0, 0, 0.88)"></span>
              {{ $t('yun-hang-zai-gai-ji-qi-shang-de-ren-wu-lie-biao') }}
            </p>
            <Button class="show-log-btn" @click="handleShowLogContainer">
              {{ $t('cha-kan-ji-qi-ri-zhi') }}
            </Button>
            <Table
              :max-height="maxHeight"
              style="margin-top: 10px"
              :row-class-name="rowClassName"
              size="small"
              :loading="loadingTasks"
              ref="selection"
              @on-select-all="handleSelectAllTasks"
              @on-select-all-cancel="handleSelectAllCancel"
              @on-select="handleSelectTask"
              @on-select-cancel="handleSelectCancel"
              :columns="enableBatch ? taskColumnBatch : taskColumn"
              :data="selectWorker.taskScheduleVOs ? selectWorker.taskScheduleVOs : []"
            >
              <template #action="{ row }">
                <a @click="handleShowDispatchModal(row)">{{ $t('zhong-xin-tiao-du') }}</a>
              </template>
              <template #jobName="{ row }">
                <span>{{ row.jobName }}</span>
              </template>
            </Table>
            <div class="batch-schedule-footer" v-if="enableBatch">
              <span>{{ $t('yi-xuan-zhong-selectedtasks-ge-ren-wu', [selectedTasks.length]) }}</span>
              <Button @click="handleShowBatchSchedule" type="primary" size="small">
                {{ $t('que-ding') }}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <CCModal v-model="showLog" :title="$t('ji-qi-ri-zhi')" width="1200px" class-name="show-log-container">
      <div class="log-modal-wrapper">
        <a-tabs type="card" v-model:activeKey="logFileName" @change="handleSelectLog" :animated="false" class="log-tabs">
          <a-tab-pane v-for="log in logData" :tab="log.fileName" :key="log.fileName">
            <div class="log-content-card">
              <div class="log-info-section">
                <div class="log-info-item">
                  <span class="log-info-label">{{ $t('miao-shu-0') }}</span>
                  <span class="log-info-value">{{ selectedLog.desc }}</span>
                </div>
                <div class="log-info-item">
                  <span class="log-info-label">{{ $t('lu-jing-0') }}</span>
                  <span class="log-info-value">
                    {{ selectedLog.path }}
                    <Tooltip :content="$t('fu-zhi-lu-jing')">
                      <Icon class="copy-icon" type="ios-photos-outline" @click="copyText(selectedLog.path)" />
                    </Tooltip>
                    <Tooltip :content="$t('dao-chu-ri-zhi')">
                      <Icon class="copy-icon" type="ios-open-outline" @click="handleDownloadLog" />
                    </Tooltip>
                  </span>
                </div>
                <div v-if="selectedLog.logExportTime" class="log-info-item">
                  <span class="log-info-label">{{ $t('ri-zhi-dao-chu-shi-jian') }}</span>
                  <span class="log-info-value mr-[16px]">
                    {{ dayjs(selectedLog.logExportTime).format('YYYY-MM-DD HH:mm:ss') }}
                  </span>

                  <span class="log-info-item">
                    <span class="log-info-label">{{ $t('ri-zhi-dao-chu-zhuang-tai') }}</span>
                    <span class="log-info-value" v-if="selectedLog.logExportStatus === 'FAILURE' || selectedLog.logExportStatus === 'TIMEOUT'">
                      {{ $t('ri-zhi-dao-chu-shi-bai') }}
                      <router-link :to="`/ccsystem/state/task/${selectedLog.logExportJobId}`">{{ $t('dian-ji-cha-kan-xiang-qing') }}</router-link>
                    </span>
                    <span class="log-info-value" v-if="selectedLog.logExportStatus === 'RUNNING'">
                      {{ $t('ri-zhi-dao-chu-zhong-dian-ji-shua-xin-cha-kan-zui-xin-zhuang-tai') }}
                      <Tooltip :content="$t('shua-xin')">
                        <Icon class="copy-icon" type="ios-loading" v-if="logLoading" />
                        <Icon class="copy-icon" type="ios-refresh" v-else @click="handleShowLog(null, null, 0, parseInt(selectedLog.endRow))" />
                      </Tooltip>
                    </span>
                    <span class="log-info-value" v-if="selectedLog.logExportStatus === 'SUCCESS' && selectedLog.logUrl">
                      <span v-if="selectedLog.logExpireTime && dayjs(selectedLog.logExpireTime).isBefore(dayjs())">
                        {{ $t('dao-chu-lian-jie-yi-guo-qi-qing-dian-ji-dao-chu-an-niu-zhong-xin-dao-chu') }}
                      </span>
                      <span v-else>
                        {{ $t('xia-zai-lian-jie-yi-sheng-cheng') }}
                        <Tooltip :content="$t('fu-zhi-xia-zai-di-zhi')">
                          <Icon class="copy-icon" type="ios-photos-outline" @click="copyText(selectedLog.logUrl)" />
                        </Tooltip>
                        <Tooltip :content="$t('xia-zai-ri-zhi')">
                          <Icon class="copy-icon" type="ios-cloud-download-outline" @click="downloadLink(selectedLog.logUrl)" />
                        </Tooltip>
                      </span>
                    </span>
                  </span>
                </div>
              </div>
              <div class="log-content-wrapper">
                <Icon
                  class="copy-icon"
                  type="ios-photos-outline"
                  @click="copyText(selectedLog.content)"
                  style="position: absolute; right: 10px; top: 10px; z-index: 9"
                />
                <div class="log-content-container" v-if="selectedLog.content" ref="logContentText">
                  <pre class="log-content-text">{{ selectedLog.content }}</pre>
                </div>
                <div class="log-empty-state" v-else>
                  <p class="empty-text">{{ $t('mei-you-ri-zhi') }}</p>
                </div>
              </div>
            </div>
          </a-tab-pane>
        </a-tabs>
      </div>
      <template #footer>
        <Button
          @click="handleShowLog(null, -200, parseInt(selectedLog.endRow))"
          style="margin-right: 5px"
          :disabled="parseInt(selectedLog.endRow) <= 200 || logLoading || logNextLoading"
          :loading="logPreLoading"
        >
          {{ $t('shang-yi-ye') }}
        </Button>
        <Button
          @click="handleShowLog(null, 200, parseInt(selectedLog.endRow))"
          style="margin-right: 5px"
          :loading="logNextLoading"
          :disabled="logLoading || logPreLoading"
        >
          {{ $t('xia-yi-ye') }}
        </Button>
        <Button
          @click="handleShowLog(null, 0, parseInt(selectedLog.endRow))"
          style="margin-right: 5px"
          :loading="logLoading"
          :disabled="logPreLoading || logNextLoading"
        >
          {{ $t('shua-xin') }}
        </Button>
        <Button type="default" @click="handleCancel">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showConfirmUpdateAlarm" :title="$t('ting-zhi-huo-xing-jian-ce-que-ren')">
      <div>
        <p>
          {{
            $t('que-ren-yao-dui-wu-li-ip-wei-selectworker-selectworkerprivateip-de-ji-qi-ting-zhi-huo-xing-jian-ce-ma', [
              selectWorker ? selectWorker.privateIp : ''
            ])
          }}
        </p>
      </div>
      <template #footer>
        <Button @click="handleCancelUpdateAlarm">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleUpdateAlarm">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showConfirmUninstall" :title="$t('xie-zai-cloudcanal-ke-hu-duan-que-ren')">
      <p>
        {{
          $t('que-ren-yao-xie-zai-wu-li-ip-wei-selectworker-selectworkerprivateip-de-ji-qi-shang-de-cloudcanal-ke-hu-duan-ma', [
            selectWorker ? selectWorker.privateIp : ''
          ])
        }}
      </p>
      <template #footer>
        <Button @click="handleCancelUpdateAlarm">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleConfirmUnInstall">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showConfirmUpgrade" :title="$t('geng-xin-cloudcanal-ke-hu-duan-que-ren')">
      <div>
        <p>
          {{
            $t('que-ren-yao-geng-xin-wu-li-ip-wei-selectworker-selectworkerprivateip-de-ji-qi-shang-de-cloudcanal-ke-hu-duan-ma', [
              selectWorker ? selectWorker.privateIp : ''
            ])
          }}
        </p>
      </div>
      <template #footer>
        <Button @click="handleCancelUpdateAlarm">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleConfirmUpgradeAll">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showConfirmOffline" :title="$t('ting-zhi-cloudcanal-ke-hu-duan-que-ren')">
      <div>
        <p>
          {{
            $t('que-ren-yao-ting-zhi-wu-li-ip-wei-selectworker-selectworkerprivateip-de-ji-qi-shang-de-cloudcanal-ke-hu-duan-ma', [
              selectWorker ? selectWorker.privateIp : ''
            ])
          }}
        </p>
      </div>
      <template #footer>
        <Button @click="handleCancelUpdateAlarm">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleChangeWorkerStatus(false)">
          {{ $t('que-ding') }}
        </Button>
      </template>
    </CCModal>
    <CCModal
      v-model="showConfirmOnline"
      :title="$t('workeroperationi18nconfirmonlinetypecloudcanal-ke-hu-duan-que-ren', [WORKER_OPERATION_I18N[confirmOnlineType]])"
    >
      <div v-if="showConfirmOnline">
        <p style="margin-bottom: 10px">
          {{
            $t(
              'que-ren-yao-workeroperationi18nconfirmonlinetype-wu-li-ip-wei-selectworker-selectworkerprivateip-de-ji-qi-shang-de-cloudcanal-ke-hu-duan-ma',
              [WORKER_OPERATION_I18N[confirmOnlineType], selectWorker ? selectWorker.privateIp : '']
            )
          }}
        </p>
        <Checkbox
          v-model="autoStart"
          v-if="
            !isSaas &&
            upgradeSidecar &&
            selectWorker &&
            selectWorker.cloudOrIdcName === 'SELF_MAINTENANCE' &&
            confirmOnlineType === WORKER_OPERATION.START_CLIENT
          "
        >
          {{ $t('zi-dong-qi-dong') }}
        </Checkbox>
        <Form
          :model="autoDeploy"
          :rules="autoDeployValidate"
          :label-width="120"
          ref="auto-deploy-form"
          v-if="autoStart || [WORKER_OPERATION.UPGRADE_ALL, WORKER_OPERATION.ROLLBACK_CLIENT].includes(confirmOnlineType)"
        >
          <FormItem label="IP">
            {{ selectWorker ? selectWorker.privateIp : '' }}
          </FormItem>
          <FormItem :label="$t('duan-kou')" prop="sshPort">
            <Input v-model="autoDeploy.sshPort" style="width: 300px" autocomplete="new-password" />
          </FormItem>
          <FormItem :label="$t('yuan-cheng-an-zhuang-lu-jing')" prop="remoteInstallWorkerPath">
            <Input v-model="autoDeploy.remoteInstallWorkerPath" style="width: 300px" autocomplete="new-password" />
          </FormItem>
          <FormItem :label="$t('an-zhuang-bao-lu-jing')" prop="localInstallPackagePath" v-if="confirmOnlineType === WORKER_OPERATION.UPGRADE_ALL">
            <Input v-model="autoDeploy.localInstallPackagePath" style="width: 300px" />
          </FormItem>
          <FormItem
            :label="$t('hui-gun-ji-qi-lu-jing')"
            prop="remoteWorkerRollbackPath"
            v-if="confirmOnlineType === WORKER_OPERATION.ROLLBACK_CLIENT"
          >
            <Select v-model="autoDeploy.remoteWorkerRollbackPath" v-if="rollbackMode" style="width: 300px">
              <Option v-for="path in rollbackPaths" :value="path" :key="path">
                {{ path }}
              </Option>
            </Select>
            <Input v-model="autoDeploy.remoteWorkerRollbackPath" style="width: 300px" v-if="!rollbackMode" />
            <div style="display: inline-flex; align-items: center">
              <Checkbox v-if="confirmOnlineType === WORKER_OPERATION.ROLLBACK_CLIENT" style="margin-left: 10px" v-model="rollbackMode" />
              <Poptip style="display: flex; align-items: center">
                <Icon type="ios-help-circle-outline" size="20" style="display: flex; align-items: center" />
                <template #content>
                  {{ $t('zi-ding-yi-hui-gun-lu-jing') }}
                </template>
              </Poptip>
            </div>
          </FormItem>
          <FormItem :label="$t('ssh-lei-xing')" prop="sshAuthType">
            <Select v-model="autoDeploy.sshAuthType" style="width: 300px" :disabled="autoDeploy.hasConnectInfo && !useNewCredentials">
              <Option value="password">{{ $t('mi-ma') }}</Option>
              <Option value="secret_key">{{ $t('mi-yue') }}</Option>
            </Select>
          </FormItem>
          <FormItem
            :label="$t('si-yue-lu-jing')"
            prop="privateKeyPath"
            v-if="autoDeploy.sshAuthType === 'secret_key' && (!autoDeploy.hasConnectInfo || useNewCredentials)"
            key="privateKeyPath"
          >
            <Input v-model="autoDeploy.privateKeyPath" style="width: 300px" />
          </FormItem>
          <FormItem :label="$t('zhang-hao')" prop="remoteUser" key="remoteUser">
            <Input
              v-model="autoDeploy.remoteUser"
              style="width: 300px"
              autocomplete="new-password"
              :disabled="autoDeploy.hasConnectInfo && !useNewCredentials"
            />
            <div v-if="autoDeploy.hasConnectInfo && !useNewCredentials" style="margin-top: 8px">
              <a @click="handleUseNewCredentials" style="color: #0bb9f8; cursor: pointer">{{ $t('shi-yong-xin-ping-zheng') }}</a>
            </div>
          </FormItem>
          <FormItem
            :label="$t('mi-ma')"
            prop="passphrase"
            v-if="autoDeploy.sshAuthType === 'secret_key' && (!autoDeploy.hasConnectInfo || useNewCredentials)"
            key="passphrase"
          >
            <Input v-model="autoDeploy.passphrase" style="width: 300px" />
          </FormItem>
          <FormItem
            :label="$t('mi-ma')"
            prop="remotePassword"
            v-if="autoDeploy.sshAuthType === 'password' && (!autoDeploy.hasConnectInfo || useNewCredentials)"
            key="remotePassword"
          >
            <Input v-model="autoDeploy.remotePassword" style="width: 300px" type="password" autocomplete="new-password" password />
          </FormItem>
          <FormItem :label="$t('ce-shi-lian-jie-0')" prop="connection">
            <Button @click="checkConnection" :loading="checkConnectionLoading">{{ $t('ce-shi-lian-jie') }}</Button>
            <span v-if="connectionSucceeded" style="color: #19be6b; margin-left: 8px">{{ $t('lian-jie-cheng-gong') }}</span>
            <!-- {{ this.connectionErrorMsg }} -->
          </FormItem>
          <FormItem
            :label="$t('shi-fou-chuan-shu-an-zhuang-bao')"
            v-if="![WORKER_OPERATION.ROLLBACK_CLIENT, WORKER_OPERATION.START_CLIENT].includes(confirmOnlineType)"
          >
            <Checkbox v-model="autoDeploy.isDownloadPackage" />
            <Poptip transfer>
              <Icon type="ios-help-circle-outline" size="20" style="margin-left: 10px" />
              <template #content>
                {{ $t('qu-xiao-chuan-shu-an-zhuang-bao-xu-yao-jiang-an-zhuang-bao-fang-zhi-dao-yuan-cheng-an-zhuang-lu-jing-xia') }}
              </template>
            </Poptip>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancelUpdateAlarm">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :disabled="checkConnectionLoading" @click="handleChangeWorkerStatus(true)">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showAddWorker" :title="isEdit ? $t('bian-ji-ji-qun') : $t('xin-zeng-ji-qi')" width="1260px" footer-hide>
      <div>
        <AddWorker
          :handleCancel="handleCancel"
          :clusterInfo="clusterInfo"
          :handleShowSighin="handleShowSighin"
          :getWorkList="getWorkList"
        ></AddWorker>
      </div>
    </CCModal>
    <CCModal v-model="showUnInstallLocal" @on-ok="handleConfirmUnInstall" :title="$t('xie-zai-ti-shi')">
      <div>
        <Form :model="addWorkerForm" label-position="right" :label-width="60">
          <FormItem :label="$t('zhang-hao')">
            <Input v-model="addWorkerForm.remoteUser" style="width: 300px" />
          </FormItem>
          <FormItem :label="$t('mi-ma')">
            <Input v-model="addWorkerForm.remotePassword" type="password" password style="width: 300px" />
          </FormItem>
        </Form>
      </div>
    </CCModal>
    <CCModal v-model="showStopLocal" @on-ok="stopInstall" :title="$t('ting-zhi-bing-qing-chu-ti-shi')">
      <div>
        <Form :model="addWorkerForm" label-position="right" :label-width="60">
          <FormItem :label="$t('zhang-hao')">
            <Input v-model="addWorkerForm.remoteUser" style="width: 300px" />
          </FormItem>
          <FormItem :label="$t('mi-ma')">
            <Input v-model="addWorkerForm.remotePassword" type="password" password style="width: 300px" />
          </FormItem>
        </Form>
      </div>
    </CCModal>
    <CCModal v-model="showDispatch" :title="$t('zhong-xin-tiao-du')">
      <div>
        <p style="margin-bottom: 10px; word-break: break-all">
          {{ $t('xu-yao-ba-selectedrowtaskname-cong-selectedrowworkerip-tiao-du-dao', [getTaskName, getCurrentIp]) }}
        </p>
        <Select v-model="workerToDispatch" style="width: 400px">
          <Option v-for="item in getWorkerList(selectedRow)" :value="item.id" :key="item.id">{{ item.privateIp }}({{ item.workerDesc }})</Option>
        </Select>
      </div>
      <template #footer>
        <Button @click="handleCancel">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleDispatch">{{ $t('que-ren') }}</Button>
      </template>
    </CCModal>
    <StToken ref="stToken" :nextStep="nextStep"></StToken>
    <CCModal v-model="showDownloadClient" :title="$t('xia-zai-ke-hu-duan')" width="1100px">
      <div>
        <Alert type="warning" show-icon>
          {{ $t('gai-xia-zai-lian-jie-you-xiao-shi-jian-wei-1-xiao-shi-qing-jin-kuai-xia-zai') }}
        </Alert>
        <div style="white-space: pre-wrap; word-wrap: break-word; border: 1px solid #dadada; padding: 20px">
          {{ downloadUrl }}
        </div>
      </div>
      <template #footer>
        <Button type="primary" @click="handleDownloadDireactly">{{ $t('zhi-jie-xia-zai') }}</Button>
        <Button type="primary" @click="handleCopy(downloadUrl)">{{ $t('fu-zhi-lian-jie') }}</Button>
        <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showConfigData" :title="$t('pei-zhi-wen-jian')" width="1100px">
      <div>
        <Alert type="warning">
          {{ $t('qing-jiang-xia-lie-pei-zhi-wan-zheng-fu-zhi-dao-cloudcanal-jie-ya-mu-lu-cloudcanalglobalconfconfproperties-wen-jian-zhong') }}
        </Alert>
        <div style="line-height: 30px; border: 1px solid #dadada; padding: 20px; margin-top: 20px">
          <p>{{ configData.userAkLabel }}={{ configData.userAkValue }}</p>
          <p>{{ configData.userSkLabel }}={{ configData.userSkValue }}</p>
          <p>{{ configData.wsnLabel }}={{ configData.wsnValue }}</p>
          <p>{{ configData.consoleDomainLabel }}={{ configData.consoleDomainValue }}</p>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="handleCopy(getConfigData(configData))">
          {{ $t('fu-zhi') }}
        </Button>
      </template>
    </CCModal>
    <CCModal v-model="showCheckWhiteListModal" :title="$t('jian-cha-bai-ming-dan')">
      <div style="max-height: 400px">
        {{ $t('jian-ce-gai-ji-qi-suo-zai-ji-qun-xia-de-suo-you-ren-wu-guan-lian-de-shu-ju-yuan-de-bai-ming-dan') }}
        <Select v-model="hostType" style="margin-top: 10px">
          <Option value="PRIVATE_IP_ONLY">
            {{ $t('ji-qi-nei-wang-ip') }}
          </Option>
          <Option value="PUBLIC_IP_ONLY">
            {{ $t('ji-qi-wai-wang-ip') }}
          </Option>
          <Option value="ADD_ALL">
            {{ $t('ji-qi-nei-wai-wang-ip') }}
          </Option>
        </Select>
        <div v-if="hasCheckWhiteList" style="margin-top: 10px">
          <div v-if="noWhiteListInstanceList.length">
            {{
              $t('yi-shang-ji-qun-ren-wu-guan-lian-de-yun-shu-ju-ku-ip-bai-ming-dan-mei-you-suo-xuan-ji-qi-ip-qing-zuo-tian-jia-bai-ming-dan-cao-zuo')
            }}
            <div style="height: 240px; overflow: scroll">
              <div v-for="instanceId in noWhiteListInstanceList" :key="instanceId">
                {{ instanceId }}
              </div>
            </div>
          </div>
          <div v-else>
            {{ $t('suo-xuan-ze-de-ji-qi-ip-zai-ji-qun-ren-wu-guan-lian-de-yun-shu-ju-ku-shang') }}
          </div>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
        <Button @click="handleCheckWhiteList" type="primary" :loading="checkWhiteListLoading">
          {{ $t('jian-cha-bai-ming-dan') }}
        </Button>
      </template>
    </CCModal>
    <CCModal v-model="showAddWhiteListModal" :title="$t('tian-jia-bai-ming-dan')">
      <div style="max-height: 400px">
        <Select v-model="hostType">
          <Option value="PRIVATE_IP_ONLY">
            {{ $t('ji-qi-nei-wang-ip') }}
          </Option>
          <Option value="PUBLIC_IP_ONLY">
            {{ $t('ji-qi-wai-wang-ip') }}
          </Option>
          <Option value="ADD_ALL">
            {{ $t('ji-qi-nei-wai-wang-ip') }}
          </Option>
        </Select>
      </div>
      <template #footer>
        <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
        <Button @click="handleAddWhiteList" type="primary" :loading="addWhiteListLoading">
          {{ $t('tian-jia-bai-ming-dan') }}
        </Button>
      </template>
    </CCModal>
    <CCModal :title="$t('ke-hu-duan-zi-dong-sheng-ji')" v-model="showUpgradeModal">
      {{ $t('zi-dong-sheng-ji-yi-jing-kai-shi-ke-yi-zhi-geng-duo') }}
      <router-link :to="`/ccsystem/state/task?workerIds=${selectWorker.id}`">
        {{ $t('cha-kan-yi-bu-ren-wu') }}
      </router-link>
      {{ $t('cha-kan-sheng-ji-jin-du') }}
    </CCModal>
    <CCModal v-model="showCancelOperationModal" :title="$t('ti-shi')" @on-ok="handleCancelOperation">
      {{ $t('que-ren-yao-cao-zuo', [WORKER_OPERATION_I18N[confirmOnlineType]]) }}
    </CCModal>
    <CCModal v-model="showUpdateExternalIp" :title="$t('geng-xin-gong-wang-ip')">
      <div>
        <Form :label-width="120">
          <FormItem :label="$t('dang-qian-gong-wang-ip')">
            {{ selectWorker.publicIp }}
          </FormItem>
          <FormItem :label="$t('geng-xin-gong-wang-ip-wei')">
            <Input v-model="externalIp" style="width: 200px"></Input>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancel">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleUpdateExternalIp">{{ $t('que-ren') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showCleanWokrerLog" :title="$t('qing-li-ji-qi-ri-zhi')" @on-cancel="handleCancel">
      <div>
        <div v-if="!isLogCleanStarted && !cleanLogLoading">
          <div>{{ $t('jian-yi-zai-ren-wu-kong-xian-shi-qi-jin-hang-qing-li') }}</div>
          <div class="workder-log-clean">
            <div>{{ $t('cong-he-shi-de-ri-zhi-kai-shi-qing-li') }}</div>
            <DatePicker v-model="startCleanLogTime" :options="pickerOptions" format="yyyy-MM-dd" type="date" style="width: 200px"></DatePicker>
          </div>
        </div>
        <Spin v-if="cleanLogLoading" size="large" style="display: flex; justify-content: center" />
        <div v-if="isLogCleanStarted" class="jump-to-log">
          <div>
            <div>{{ $t('ri-zhi-qing-li-yi-kai-shi') }}</div>
            <a @click="goConsolejobDetail">{{ $t('dian-ji-cha-kan-yi-bu-ren-wu-xiang-qing') }}</a>
          </div>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCancel" v-if="!isLogCleanStarted">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="cleanLogLoading" @click="handleCleanWorkerLog" v-if="!isLogCleanStarted">
          {{ $t('que-ren') }}
        </Button>
        <Button type="primary" @click="handleCancel" v-if="isLogCleanStarted">
          {{ $t('guan-bi') }}
        </Button>
      </template>
    </CCModal>
    <CCModal v-model="showDownloadLogModal" :title="$t('ti-shi')">
      {{ $t('ri-zhi-shang-chuan-zhong-dian-ji-shua-xin-an-niu-ke-yi-cha-kan-zui-xin-jin-du') }}
      <template #footer>
        <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="handleShowLog(null, null, 0, parseInt(selectedLog.endRow))" :loading="queryMqPartitionLoading">
          {{ $t('shua-xin') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import fecha from 'fecha';
import store from '@/store';
import JobOnWorker from '@/components/function/JobOnWorker';
import StToken from '@/components/function/ApplyStToken';
import AddWorker from '@/components/function/cluster/AddWorker';
import dayjs from 'dayjs';
import { OPERATION_STATUS, OPERATION_STATUS_I18N, SECOND_CONFIRM_EVENT_LIST, WORKER_OPERATION, WORKER_OPERATION_I18N, WORKER_STATE } from '@/const';
import { mapGetters, mapMutations, mapState } from 'vuex';
import SecondConfirmModal from '@/components/modal/SecondConfirmModal';
import { DOWNLOAD_STATUS } from '@/utils';
import Mapping from '../util';

export default {
  name: 'Worker',
  components: {
    // eslint-disable-next-line vue/no-unused-components
    JobOnWorker,
    StToken,
    AddWorker,
    SecondConfirmModal
  },
  created() {
    this.queryIp = this.$route.query.ip;
    this.taskId = this.$route.query.task;
    this.clusterId = this.$route.params.id;
    if (localStorage.getItem(`cluster-${this.clusterId}`)) {
      this.clusterInfo = JSON.parse(localStorage.getItem(`cluster-${this.clusterId}`));
    }
  },
  mounted() {
    this.addWorker.cloudOrIdcName = this.clusterInfo.cloudOrIdcName;
    this.addWorker.region = this.clusterInfo.region;
    const totalHeight = window.innerHeight;

    // eslint-disable-next-line no-undef
    $('.worker-body').css('height', `${totalHeight - 200}px`);
    this.maxHeight = totalHeight - 400;
    this.getWorkList();
    this.listRegions();
    this.listCloudOrIdcNames();
    this.listWorkerTypes();
    this.getDataSourceList();
  },
  beforeUnmount() {
    clearInterval(this.getProcess);
    clearInterval(this.sendCodeAgainTime);
  },
  data() {
    const validateConnection = (rule, value, callback) => {
      if (!this.hasConnection) {
        callback(new Error(this.$t('qing-xian-ce-shi-lian-jie')));
      } else if (!this.connectionSucceeded) {
        callback(new Error(this.$t('qing-xian-ce-shi-lian-jie-cheng-gong')));
      } else {
        callback();
      }
    };
    const validatePath = (rule, value, callback) => {
      if (value.endsWith('/')) {
        callback();
      } else {
        callback(new Error(this.$t('lu-jing-xu-yao-yi-xie-jie-wei-di-zhi')));
      }
    };
    return {
      showDownloadLogModal: false,
      downloadTaskId: null,
      downloadLogLoading: false,
      DOWNLOAD_STATUS,
      showCancelOperationModal: false,
      OPERATION_STATUS,
      OPERATION_STATUS_I18N,
      showUpgradeModal: false,
      WORKER_OPERATION,
      WORKER_OPERATION_I18N,
      confirmOnlineType: '',
      autoStart: false,
      hasConnection: false,
      connectionSucceeded: false,
      confirmFetchLoading: false,
      checkConnectionLoading: false,
      connectionErrorMsg: '',
      autoDeploy: {
        isDownloadPackage: true,
        sshPort: 22,
        executeTag: 'DEFAULT_REMOTE',
        remoteIp: '',
        remoteUser: '',
        remotePassword: '',
        remoteInstallWorkerPath: '/home/clougence/',
        localInstallPackagePath: '/home/clougence/tar_gz/cloudcanal.tgz',
        remoteWorkerRollbackPath: '',
        sshAuthType: 'password',
        privateKeyPath: '',
        passphrase: ''
      },
      rollbackMode: true,
      rollbackPaths: [],
      useNewCredentials: false,
      autoDeployValidate: {
        sshPort: [{ required: true, message: this.$t('duan-kou-bu-neng-wei-kong') }],
        privateKeyPath: [{ required: true, message: this.$t('si-yue-lu-jing-bu-neng-wei-kong') }],
        remoteWorkerRollbackPath: [
          { required: true, message: this.$t('hui-gun-ji-qi-lu-jing-bu-neng-wei-kong') },
          { validator: validatePath, trigger: 'blur' }
        ],
        localInstallPackagePath: [{ required: true, message: this.$t('an-zhuang-bao-lu-jing-bu-neng-wei-kong') }],
        remoteInstallWorkerPath: [
          { required: true, message: this.$t('yuan-cheng-an-zhuang-lu-jing-bu-neng-wei-kong') },
          { validator: validatePath, trigger: 'blur' }
        ],
        remoteIp: [{ required: true, message: this.$t('ip-bu-neng-wei-kong') }],
        remoteUser: [
          {
            required: true,
            message: this.$t('zhang-hao-bu-neng-wei-kong')
          }
        ],
        remotePassword: [
          {
            required: true,
            message: this.$t('mi-ma-bu-neng-wei-kong')
          }
        ],
        connection: [
          {
            validator: validateConnection,
            trigger: 'blur'
          }
        ]
      },
      hasCheckWhiteList: false,
      noWhiteListInstanceList: [],
      hostType: 'ADD_ALL',
      showAddWhiteListModal: false,
      showCheckWhiteListModal: false,
      showUpdateExternalIp: false,
      addWhiteListLoading: false,
      checkWhiteListLoading: false,
      showLog: false,
      logFileName: '',
      logData: '',
      selectedLog: '',
      logLoading: false,
      logPreLoading: false,
      logNextLoading: false,
      maxHeight: 500,
      showConfirmOnline: false,
      showConfirmOffline: false,
      showConfirmUninstall: false,
      showDownloadClient: false,
      downloadUrl: '',
      configData: '',
      externalIp: '',
      showConfigData: false,
      enableBatch: false,
      isBatch: false,
      selectedTasks: [],
      currentMenOver: 0,
      offAlarm: false,
      workers: [],
      showDispatch: false,
      selectWorker: {},
      selectedRow: {},
      workerToDispatch: '',
      fecha,
      Mapping,
      store,
      nextStep: '',
      taskColumnBatch: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: this.$t('cao-zuo'),
          width: 120,
          slot: 'action'
        },
        {
          title: this.$t('task-ming-cheng'),
          key: 'taskName',
          width: 280
        },
        {
          title: this.$t('ren-wu-id'),
          slot: 'jobName',
          width: 200
        },
        {
          title: this.$t('ren-wu-miao-shu'),
          key: 'jobDesc',
          minWidth: 150
        },
        {
          title: this.$t('ren-wu-jvm-fgc-shu'),
          key: 'fgcCount',
          sortable: true,
          minWidth: 164,
          render: (h, params) => {
            if (params.row.fgcCount != null && params.row.taskConnStatus !== 'DISCONNECTED') {
              return h('div', {}, `${params.row.fgcCount}`);
            }
            return h('div', {}, '-');
          }
        },
        {
          title: this.$t('ren-wu-cpu-shi-yong-shuai'),
          key: 'userCpuUsage',
          sortable: true,
          minWidth: 160,
          render: (h, params) => {
            if (params.row.userCpuUsage != null && params.row.taskConnStatus !== 'DISCONNECTED') {
              return h('div', {}, `${params.row.userCpuUsage}%`);
            }
            return h('div', {}, '-');
          }
        },
        {
          title: this.$t('gui-ge-0'),
          key: 'jvmHeapMb',
          width: 130,
          render: (h, params) => h('div', {}, `${params.row.jvmHeapMb / 1024}G`)
        },
        {
          title: this.$t('chuang-jian-shi-jian'),
          key: 'gmtCreate',
          width: 180,
          render: (h, params) => h('div', {}, fecha.format(new Date(params.row.gmtCreate), 'YYYY-MM-DD HH:mm:ss'))
        },
        {
          title: this.$t('yuan-shu-ju-ku'),
          key: 'srcDsDesc',
          width: 260,
          render: (h, params) => h('div', {}, `${this.getInstanceId(params.row.srcDsId)}/${params.row.srcDsDesc}`)
        },
        {
          title: this.$t('mu-biao-shu-ju-ku'),
          key: 'dstDsDesc',
          width: 260,
          render: (h, params) => h('div', {}, `${this.getInstanceId(params.row.dstDsId)}/${params.row.dstDsDesc}`)
        }
      ],
      taskColumn: [
        {
          title: this.$t('cao-zuo'),
          width: 120,
          slot: 'action'
        },
        {
          title: this.$t('task-ming-cheng'),
          key: 'taskName',
          width: 280
        },
        {
          title: this.$t('ren-wu-id'),
          slot: 'jobName',
          width: 200
        },
        {
          title: this.$t('ren-wu-miao-shu'),
          key: 'jobDesc',
          minWidth: 150
        },
        {
          title: this.$t('ren-wu-jvm-fgc-shu'),
          key: 'fgcCount',
          sortable: true,
          minWidth: 164,
          render: (h, params) => {
            if (params.row.fgcCount != null && params.row.taskConnStatus !== 'DISCONNECTED') {
              return h('div', {}, `${params.row.fgcCount}`);
            }
            return h('div', {}, '-');
          }
        },
        {
          title: this.$t('ren-wu-cpu-shi-yong-shuai'),
          key: 'userCpuUsage',
          sortable: true,
          minWidth: 160,
          render: (h, params) => {
            if (params.row.userCpuUsage != null && params.row.taskConnStatus !== 'DISCONNECTED') {
              return h('div', {}, `${params.row.userCpuUsage}%`);
            }
            return h('div', {}, '-');
          }
        },
        {
          title: this.$t('gui-ge-0'),
          key: 'jvmHeapMb',
          width: 130,
          render: (h, params) => h('div', {}, `${params.row.jvmHeapMb / 1024}G`)
        },
        {
          title: this.$t('chuang-jian-shi-jian'),
          key: 'gmtCreate',
          width: 180,
          render: (h, params) => h('div', {}, fecha.format(new Date(params.row.gmtCreate), 'YYYY-MM-DD HH:mm:ss'))
        },
        {
          title: this.$t('yuan-shu-ju-ku'),
          key: 'srcDsDesc',
          width: 260,
          render: (h, params) => h('div', {}, `${this.getInstanceId(params.row.srcDsId)}/${params.row.srcDsDesc}`)
        },
        {
          title: this.$t('mu-biao-shu-ju-ku'),
          key: 'dstDsDesc',
          width: 260,
          render: (h, params) => h('div', {}, `${this.getInstanceId(params.row.dstDsId)}/${params.row.dstDsDesc}`)
        }
      ],
      taskData: [],
      addDataSourceRuleAkSk: {},
      showUnInstall: false,
      showStop: false,
      showInstallLocal: false,
      showUnInstallLocal: false,
      showStopLocal: false,
      showConfirmUpgrade: false,
      pageData: {
        startId: 0,
        pageSize: 5,
        pageNumber: 1
      },
      showConfirmUpdateAlarm: false,
      showDeployProgress: false,
      taskId: 0,
      currentState: false,
      avgLoad: 0,
      avgCpu: 0,
      avgMem: 0,
      showWorkerListInterval: '',
      queryIp: '',
      refreshLoading: false,
      regions: [],
      cloudOrIdcNames: [],
      workerStates: [],
      workerTypes: [],
      isEdit: false,
      showAddWorker: false,
      showConfirmDelete: false,
      clusterId: 0,
      clusterInfo: {},
      clusterName: '',
      searchType: 'sourceDs',
      searchKey: {},
      showSearch: false,
      loadingTasks: false,
      dsList: [],
      addWorker: {
        clusterId: this.$route.params.id,
        workerIp: '',
        cloudOrIdcName: 'aliyun',
        region: 'hangzhou',
        physicMemMb: 0,
        physicCoreNum: 0,
        physicDiskMb: 0,
        workerType: 'BARE_METAL',
        workerState: ''
      },
      resourceColumns: [
        {
          title: this.$t('xiang-qing'),
          type: 'expand',
          width: 60,
          render: (h, params) =>
            h(JobOnWorker, {
              props: {
                row: params.row,
                index: params.index,
                workerList: this.resourceData,
                getWorkList: this.getWorkList,
                taskId: this.taskId
              }
            })
        },
        {
          title: this.$t('wu-li-ip'),
          key: 'privateIp',
          render: (h, params) =>
            h(
              'a',
              {
                on: {
                  click: () => {
                    this.$router.push({
                      path: `/monitor/worker/graph`,
                      query: {
                        ip: params.row.privateIp,
                        id: params.row.id
                      }
                    });
                  }
                }
              },
              params.row.privateIp
            )
        },
        {
          title: this.$t('chuang-jian-shi-jian'),
          key: 'gmtCreate',
          render: (h, params) => h('div', {}, fecha.format(new Date(params.row.gmtCreate), 'YYYY-MM-DD HH:mm:ss')),
          width: 150
        },
        {
          title: this.$t('lei-xing'),
          key: 'workerType',
          width: 120,
          render: (h, params) => h('div', {}, Mapping.workerType[params.row.workerType])
        },
        {
          title: this.$t('ji-qi-zhuang-tai'),
          key: 'workerState',
          width: 140,
          render: (h, params) =>
            h(
              'div',
              {
                style: {
                  color: params.row.workerState === 'ABNORMAL' ? '#ed4014' : ''
                }
              },
              Mapping.workerStatus[params.row.workerState]
            )
        },
        {
          title: this.$t('fu-zai'),
          key: 'workerLoad',
          sortable: true,
          width: 120,
          render: (h, params) => {
            let color = '#19be6b';

            if (params.row.workerLoad > 40 && params.row.workerLoad < 70) {
              color = '#ff9900';
            } else if (params.row.workerLoad > 70) {
              color = '#ed4014';
            }
            return h(
              'div',
              {
                style: {
                  color
                }
              },
              `${params.row.workerLoad}%`
            );
          }
        },
        {
          title: this.$t('cpu-shi-yong-shuai'),
          key: 'cpuUseRatio',
          width: 120,
          sortable: true,
          render: (h, params) => {
            let color = '#19be6b';

            if (params.row.cpuUseRatio > 40 && params.row.cpuUseRatio < 70) {
              color = '#ff9900';
            } else if (params.row.cpuUseRatio > 70) {
              color = '#ed4014';
            }
            return h(
              'div',
              {
                style: {
                  color
                }
              },
              `${params.row.cpuUseRatio}%`
            );
          }
        },
        {
          title: this.$t('nei-cun-shi-yong-shuai'),
          key: 'memUseRatio',
          sortable: true,
          width: 120,
          render: (h, params) => {
            let color = '#19be6b';

            if (params.row.memUseRatio > 40 && params.row.memUseRatio < 70) {
              color = '#ff9900';
            } else if (params.row.memUseRatio > 70) {
              color = '#ed4014';
            }
            return h(
              'div',
              {
                style: {
                  color
                }
              },
              `${params.row.memUseRatio}%`
            );
          }
        },
        {
          title: this.$t('jian-kang-cheng-du'),
          key: 'healthLevel',
          render: (h, params) =>
            h('div', {
              style: {
                background: params.row.healthLevel === 'Health' ? '#19be6b' : params.row.healthLevel === 'Unhealthy' ? '#ed4014' : '#ff9900',
                width: '12px',
                height: '12px',
                borderRadius: '50%',
                marginLeft: '25px'
              }
            }),
          width: 90
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          width: 200
        }
      ],
      resourceData: [],
      showCleanWokrerLog: false,
      isLogCleanStarted: false,
      cleanLogLoading: false,
      startCleanLogTime: '',
      cleanLogConsoleJobId: ''
    };
  },
  computed: {
    SECOND_CONFIRM_EVENT_LIST() {
      return SECOND_CONFIRM_EVENT_LIST;
    },
    ...mapGetters(['verifyType', 'upgradeSidecar']),
    currentTheme() {
      return this.$store.state.theme || 'light';
    },
    pickerOptions() {
      return {
        disabledDate: (date) => {
          const yesterday = new Date(Date.now() - 86400000 * 2);
          yesterday.setHours(23, 59, 59, 999);
          return date > yesterday;
        }
      };
    },
    getWorkerStatus() {
      return (worker) => {
        if (worker.workerState === 'ONLINE') {
          return this.$t('zai-xian');
        }
        if (worker.workerState === 'ABNORMAL') {
          return this.$t('yi-chang');
        }
        if (worker.workerState === 'WAIT_TO_ONLINE') {
          return this.$t('deng-dai-shang-xian');
        }
        if (worker.workerState === 'WAIT_TO_OFFLINE') {
          return this.$t('deng-dai-xia-xian');
        }
        if (worker.workerState === 'OFFLINE') {
          return this.$t('li-xian');
        }
      };
    },
    getPointColor() {
      return (worker) => {
        if (worker.healthLevel === 'Unhealthy') {
          return '#FF6E0D';
        }
        if (worker.healthLevel === 'SubHealth') {
          return '#FFA30E';
        }
        return '#52C41A';
      };
    },
    getWorkerStatusColor() {
      return (worker) => {
        if (worker.workerState === 'ABNORMAL') {
          return '#FF6E0D';
        }
        if (this.currentTheme === 'dark') {
          return 'rgba(255, 255, 255, 0.7)';
        }
        return 'rgba(0, 0, 0, 0.88)';
      };
    },
    getWorkerList() {
      return (selectedRow) => {
        const list = [];

        this.resourceData.map((item) => {
          if (item.workerName !== selectedRow.workerName && item.healthLevel === 'Health') {
            list.push(item);
          }
          return null;
        });
        return list;
      };
    },
    getInstanceId() {
      return (dsId) => {
        let instanceId = '';
        this.dsList.forEach((ds) => {
          if (ds.id === dsId) {
            instanceId = ds.instanceId;
          }
        });
        return instanceId;
      };
    },
    getTaskName() {
      let finalName = '';
      if (this.isBatch) {
        const taskList = [];
        this.selectedTasks.forEach((task) => {
          taskList.push(task.taskName);
        });
        finalName = taskList.join(',');
      } else {
        finalName = this.selectedRow.taskName;
      }
      return finalName;
    },
    getCurrentIp() {
      let finalIp = '';
      if (this.isBatch) {
        if (this.selectedTasks[0]) {
          finalIp = this.selectedTasks[0].workerIp;
        }
      } else {
        finalIp = this.selectedRow.workerIp;
      }
      return finalIp;
    },
    ...mapState(['myAuth'])
  },
  watch: {
    'autoDeploy.remotePassword'(newVal, oldVal) {
      // 当密码变化且之前连接成功时，重置连接状态，需要重新测试连接
      if (this.connectionSucceeded && oldVal !== undefined && newVal !== oldVal) {
        this.connectionSucceeded = false;
        this.connectionErrorMsg = '';
        this.hasConnection = false;
      }
    },
    'autoDeploy.sshAuthType'(newVal, oldVal) {
      // 当SSH认证类型变化时（从密钥切换到密码或反之），重置连接状态，需要重新测试连接
      if (this.connectionSucceeded && oldVal !== undefined && newVal !== oldVal) {
        this.connectionSucceeded = false;
        this.connectionErrorMsg = '';
        this.hasConnection = false;
      }
    }
  },
  methods: {
    ...mapMutations(['updateSelectedWorker']),
    dayjs,
    handleGoTaskDetail() {
      console.log('handle go task detail');
      this.$router.push({ path: `/ccsystem/state/task/${this.downloadTaskId}` });
    },
    // 滚动日志内容到底部
    scrollLogToBottom() {
      this.$nextTick(() => {
        const logElements = this.$refs.logContentText;
        if (logElements && logElements.length > 0) {
          // 找到当前激活的tab对应的元素
          const activeLogElement = Array.from(logElements).find((el) => el.offsetParent !== null);
          if (activeLogElement) {
            activeLogElement.scrollTop = activeLogElement.scrollHeight;
          }
        }
      });
    },
    async handleDownloadLog() {
      try {
        this.downloadLogLoading = true;
        const filename = this.selectedLog.fileName;

        await this.$nextTick();

        const res = await this.$services.ccWorkerExportWorkerLog({
          data: {
            workerId: this.selectWorker.id,
            logFileType: this.selectedLog.logFileType
          }
        });

        if (res?.success) {
          if (res?.data) {
            this.showDownloadLogModal = true;
            this.downloadTaskId = res.data;
          }
        }
        this.downloadLogLoading = false;
      } catch (e) {
        this.downloadLogLoading = false;
        console.log(e);
      }
    },
    disableUpgradeWorker(worker) {
      return (
        [
          OPERATION_STATUS.PREPARING_UPGRADE,
          OPERATION_STATUS.UPGRADING,
          OPERATION_STATUS.PREPARING_ROLL_BACK,
          OPERATION_STATUS.ROLLING_BACK
        ].includes(worker.deployStatus) || [WORKER_STATE.WAIT_TO_ONLINE].includes(worker.workerState)
      );
    },
    disableCancelUpgradeWorker(worker) {
      return ![OPERATION_STATUS.PREPARING_UPGRADE].includes(worker.deployStatus);
    },
    disableRollbackWorker(worker) {
      return (
        [
          OPERATION_STATUS.PREPARING_UPGRADE,
          OPERATION_STATUS.UPGRADING,
          OPERATION_STATUS.PREPARING_ROLL_BACK,
          OPERATION_STATUS.ROLLING_BACK
        ].includes(worker.deployStatus) || [WORKER_STATE.WAIT_TO_ONLINE].includes(worker.workerState)
      );
    },
    disableCancelRollbackWorker(worker) {
      return ![OPERATION_STATUS.PREPARING_ROLL_BACK].includes(worker.deployStatus);
    },
    checkConnection() {
      this.checkConnectionLoading = true;
      this.hasConnection = true;
      const data = {
        ...this.autoDeploy,
        manualFill: this.useNewCredentials || !this.autoDeploy.hasConnectInfo,
        workerId: this.selectWorker.id
      };
      this.$services
        .ccAllWorkerCheckConnection({ data })
        .then((res) => {
          if (res.success) {
            this.connectionSucceeded = true;
            this.connectionErrorMsg = this.$t('ce-shi-lian-jie-cheng-gong');
            this.$refs['auto-deploy-form'] && this.$refs['auto-deploy-form'].validateField('connection');
          } else {
            this.connectionSucceeded = false;
            this.connectionErrorMsg = res.msg;
          }
        })
        .finally(() => {
          this.checkConnectionLoading = false;
        });
    },
    handleUseNewCredentials() {
      this.useNewCredentials = true;
    },
    hideConfigModal() {
      this.showConfigVerify = false;
    },
    handleRefresh() {
      this.getWorkList();
    },
    handleAddWorker() {
      this.isEdit = false;
      this.showAddWorker = true;
    },
    handleDeployCore(row) {
      this.selectWorker = row;
      this.showDeployProgress = false;
      if (this.selectWorker.cloudOrIdcName === 'ALIBABA_CLOUD') {
        this.handleInstall();
      } else {
        this.showInstallLocal = true;
      }
    },
    handleUnInstall(row) {
      this.showConfirmUninstall = true;
      this.selectWorker = row;
    },
    handleUpgradeAll(row) {
      this.showConfirmUpgrade = true;
      this.selectWorker = row;
    },
    handleConfirmDeleteCluster() {
      this.showConfirmDelete = false;
      this.selectWorker.workerId = this.selectWorker.id;
      this.$services.ccWorkerDelete({ data: this.selectWorker }).then((res) => {
        if (res.success) {
          this.selectWorker = {};
          this.getWorkList();
          this.$Message.success(this.$t('shan-chu-cheng-gong'));
        } else {
          this.getWorkList();
        }
        clearInterval(this.getProcess);
      });
    },
    handleDeleteWorker(row) {
      this.selectWorker = row;
      this.showConfirmDelete = true;
    },
    handleAddWhiteList() {
      this.selectWorker.workerId = this.selectWorker.id;
      this.addWhiteListLoading = true;
      this.$services
        .ccWorkerAddWorkerWhiteListIp({
          data: {
            workerId: this.selectWorker.id,
            whiteListAddType: this.hostType
          }
        })
        .then((res) => {
          this.addWhiteListLoading = false;
          if (res.success) {
            this.$Message.success(this.$t('tian-jia-bai-ming-dan-cheng-gong'));
            this.handleCancel();
          }
        });
    },
    handleCheckWhiteList() {
      this.selectWorker.workerId = this.selectWorker.id;
      this.checkWhiteListLoading = true;
      this.$services
        .ccWorkerCheckWorkerWhiteIp({
          data: {
            workerId: this.selectWorker.id,
            whiteListAddType: this.hostType
          }
        })
        .then((res) => {
          this.checkWhiteListLoading = false;
          if (res.success) {
            this.hasCheckWhiteList = true;
            this.noWhiteListInstanceList = res.data;
          }
        });
    },
    getWorkList() {
      this.refreshLoading = true;
      this.$services
        .ccWorkerListWorker({
          data: {
            clusterId: this.clusterId,
            sourceInstanceId: this.searchKey.sourceInstanceId === 'all' ? null : this.searchKey.sourceInstanceId,
            targetInstanceId: this.searchKey.targetInstanceId === 'all' ? null : this.searchKey.targetInstanceId
          }
        })
        .then((res) => {
          if (res.data) {
            res.data.forEach((data) => {
              if (data.taskScheduleVOs && data.taskScheduleVOs.length > 0) {
                data.taskScheduleVOs.forEach((task) => {
                  if (task.taskConnStatus === 'DISCONNECTED') {
                    task.fgcCount = -1;
                    task.userCpuUsage = -1;
                  }
                });
              }
            });
          }
          this.resourceData = res.data;
          if (!this.selectWorker.id) {
            console.log(1);
            if (res.data.length > 0) {
              let hasSelectedWorker = false;
              if (this.$store.state.selectedWorker.clusterId === this.clusterId) {
                this.resourceData.forEach((resource) => {
                  if (resource.id === this.$store.state.selectedWorker.id) {
                    console.log(2, resource);
                    this.selectWorker = resource;
                    hasSelectedWorker = true;
                  }
                });
              }
              if (!hasSelectedWorker) {
                this.selectWorker = this.resourceData[0];
              }
            } else {
              this.selectWorker = {};
            }
          } else {
            res.data.map((item) => {
              if (item.id === this.selectWorker.id) {
                this.selectWorker = item;
              }
              return null;
            });
          }

          if (this.queryIp) {
            this.resourceData.map((worker) => {
              if (worker.privateIp === this.queryIp) {
                this.selectWorker = worker;
              }
              return null;
            });
          }
          let totalCpu = 0;
          let totalMem = 0;
          let totalWorkerCount = 0;
          let totalWorkerLoad = 0;

          this.resourceData.map((worker) => {
            totalWorkerLoad += worker.workerLoad;
            totalCpu += worker.cpuUseRatio;
            totalMem += worker.memUseRatio;
            totalWorkerCount++;
            return null;
          });
          if (totalWorkerCount !== 0) {
            this.avgLoad = (totalWorkerLoad / totalWorkerCount).toFixed(2);
            this.avgCpu = (totalCpu / totalWorkerCount).toFixed(2);
            this.avgMem = (totalMem / totalWorkerCount).toFixed(2);
          }
          this.refreshLoading = false;
          if (this.selectWorker.id) {
            const workerIds = [];

            workerIds.push(this.selectWorker.id);
            if (this.selectWorker.consoleJobId) {
              this.selectWorker.currentStatus = 'process';
              if (this.selectWorker.consoleTaskState === 'SUCCESS') {
                this.selectWorker.currentStatus = 'finish';
              } else if (this.selectWorker.consoleTaskState === 'FAILED') {
                this.selectWorker.currentStatus = 'error';
              } else if (this.selectWorker.consoleTaskState === 'CANCELED') {
                this.selectWorker.currentStatus = '';
              }
            }
            if (this.selectWorker.consoleTaskState === 'EXECUTE') {
              this.queryProcess(workerIds);
            }
          }
          if (this.selectWorker.id) {
            this.listWorkerTaskDetails(this.selectWorker.id);
          }
        })
        .catch(() => {
          this.refreshLoading = false;
        })
        .finally(() => {
          this.refreshLoading = false;
        });
    },
    listRegions() {
      this.$services.ccConstantRegion().then((res) => {
        if (res.success) {
          this.regions = res.data;
        }
      });
    },
    listCloudOrIdcNames() {
      this.$services.ccConstantCloudOrIdcName().then((res) => {
        if (res.success) {
          this.cloudOrIdcNames = res.data;
        }
      });
    },
    listWorkerTypes() {
      this.$services.ccConstantWorkerType().then((res) => {
        if (res.success) {
          this.workerTypes = res.data;
        }
      });
    },
    handleChangeWorkerStatus(value) {
      this.showConfirmOffline = false;
      this.addWorker.workerId = this.selectWorker.id;
      if (value) {
        this.addWorker.workerState = 'WAIT_TO_ONLINE';
        if (this.autoStart || [WORKER_OPERATION.UPGRADE_ALL, WORKER_OPERATION.ROLLBACK_CLIENT].includes(this.confirmOnlineType)) {
          this.$refs['auto-deploy-form'].validate((valid) => {
            if (valid) {
              const worker = {
                actionType: this.confirmOnlineType,
                cloudOrIdcName: this.selectWorker.cloudOrIdcName,
                workerId: this.selectWorker.id,
                ...this.autoDeploy,
                remoteIp: this.selectWorker.privateIp,
                manualFill: this.useNewCredentials || !this.autoDeploy.hasConnectInfo
              };
              this.$services
                .ccAllWorkerOperateWorkersClient({
                  data: {
                    clusterId: this.clusterId,
                    workers: [worker]
                  }
                })
                .then((res) => {
                  this.showConfirmOnline = false;
                  this.connectionErrorMsg = '';
                  this.autoDeploy.remoteWorkerRollbackPath = '';

                  if (res.success) {
                    if (this.confirmOnlineType === WORKER_OPERATION.UPGRADE_ALL) {
                      this.showUpgradeModal = true;
                    }
                    this.confirmOnlineType = '';
                    this.$Message.success(
                      this.$t('zi-dong-workeroperationi18nthisconfirmonlinetype-cheng-gong', [WORKER_OPERATION_I18N[this.confirmOnlineType]])
                    );
                    this.getWorkList();
                  } else {
                    this.$Message.error(
                      this.$t('zi-dong-workeroperationi18nthisconfirmonlinetype-shi-bai', [WORKER_OPERATION_I18N[this.confirmOnlineType]])
                    );
                  }
                });
            }
          });
        } else {
          this.$services.ccWorkerWaitToOnline({ data: this.addWorker }).then((res) => {
            if (res.success) {
              this.showConfirmOnline = false;
              this.getWorkList();
            } else if (res.code === '6028') {
              // this.nextStep = this.handleChangeWorkerStatus(true);
              this.$refs.stToken.handleShowAkSk();
            }
          });
        }
      } else {
        this.addWorker.workerState = 'WAIT_TO_OFFLINE';
        this.$services.ccWorkerWaitToOffline({ data: this.addWorker }).then((res) => {
          if (res.success) {
            this.getWorkList();
          } else if (res.code === '6028') {
            // this.nextStep = this.handleChangeWorkerStatus(false);
            this.$refs.stToken.handleShowAkSk();
          }
        });
      }
      // this.isEdit = true;
      // this.handleConfirmAddWorker();
    },
    handleCloseDepoly() {
      this.showDeployProgress = false;
    },
    showDeployProgressFunc(workerIds) {
      // this.showDeployProgress = true;
      if (this.selectWorker.cloudOrIdcName === 'ALIBABA_CLOUD') {
        this.$services
          .ccAliyunEcsQueryProcess({
            data: {
              clusterId: this.clusterId,
              workerIds,
              pageData: this.pageData,
              deployActionType: 'INSTALL'
            }
          })
          .then((response) => {
            if (response.success) {
              this.selectWorker.consoleTasks = response.data[this.selectWorker.id];
              this.selectWorker.currentStatus = 'process';
              if (this.selectWorker.consoleTasks.length > 0) {
                if (
                  this.selectWorker.consoleTasks[this.selectWorker.consoleTasks.length - 1] &&
                  this.selectWorker.consoleTasks[this.selectWorker.consoleTasks.length - 1].taskState === 'SUCCESS'
                ) {
                  this.selectWorker.currentStatus = 'finish';
                  this.getWorkList();
                }
                this.selectWorker.step = 0;
                this.selectWorker.consoleTasks.map((task) => {
                  if (task.taskState !== 'WAIT_START') {
                    this.selectWorker.step++;
                  }
                  if (task.taskState === 'FAILED') {
                    this.selectWorker.currentStatus = 'error';
                    this.getWorkList();
                    this.selectWorker.step--;
                  }
                  return null;
                });
                if (this.selectWorker.consoleTaskState === 'CANCELED') {
                  this.selectWorker.currentStatus = '';
                }
              }

              this.selectWorker = { ...this.selectWorker };

              const that = this;

              this.getProcess = setInterval(() => {
                this.$services
                  .ccAliyunEcsQueryProcess({
                    data: {
                      clusterId: that.clusterId,
                      workerIds,
                      pageData: that.pageData,
                      deployActionType: 'INSTALL'
                    }
                  })
                  .then((response1) => {
                    if (response1.success) {
                      let isFinished = true;

                      that.selectWorker.consoleTasks = response1.data[that.selectWorker.id];
                      if (that.selectWorker.consoleTasks.length === 0) {
                        clearInterval(that.getProcess);
                      }
                      that.selectWorker.currentStatus = 'process';
                      if (that.selectWorker.consoleTasks.length > 0) {
                        if (that.selectWorker.consoleTasks[that.selectWorker.consoleTasks.length - 1].taskState === 'SUCCESS') {
                          that.selectWorker.current = 'finish';
                          that.getWorkList();
                        }
                        that.selectWorker.step = 0;
                        that.selectWorker.consoleTasks.map((task) => {
                          if (task.taskState !== 'WAIT_START') {
                            that.selectWorker.step++;
                          }
                          if (task.taskState === 'FAILED') {
                            that.selectWorker.currentStatus = 'error';
                            that.selectWorker.step--;
                          }
                          return null;
                        });
                        if (that.selectWorker.consoleTaskState === 'CANCELED') {
                          that.selectWorker.currentStatus = '';
                        }
                      }
                      if (that.selectWorker.currentStatus === 'process') {
                        isFinished = false;
                      }
                      if (isFinished) {
                        clearInterval(that.getProcess);
                      }
                      that.selectWorker = { ...that.selectWorker };
                    } else if (response1.code === '6028') {
                      clearInterval(that.getProcess);
                      that.nextStep = that.showDeployProgressFunc(workerIds);
                      that.$refs.stToken.handleShowAkSk();
                    }
                  });
              }, 5000);
            } else if (response.code === '6028') {
              this.nextStep = this.showDeployProgressFunc(workerIds);
              this.$refs.stToken.handleShowAkSk();
            }
          });
      }
    },
    handleShowDeploy(row) {
      this.selectWorker = row;
      const workerIds = [];

      workerIds.push(this.selectWorker.id);
      this.showDeployProgressFunc(workerIds);
    },
    handleShowSighin(workers) {
      this.showAddWorker = false;
      this.workers = workers;
      this.getWorkList();
      this.addWorkerForm = {
        clusterId: this.$route.params.id,
        workerIp: '',
        cloudOrIdcName: 'aliyun',
        region: 'hangzhou',
        physicMemMb: 0,
        physicCoreNum: 0,
        physicDiskMb: 0,
        workerType: 'BARE_METAL',
        workerState: ''
      };
      this.addWorker.cloudOrIdcName = this.clusterInfo.cloudOrIdcName;
    },
    handleInstall() {
      const workerIds = [];

      workerIds.push(this.selectWorker.id);
      if (this.selectWorker.cloudOrIdcName === 'ALIBABA_CLOUD') {
        this.$services
          .ccAliyunEcsInstall({
            data: {
              clusterId: this.clusterId,
              workerIds,
              pageData: this.pageData,
              deployActionType: 'INSTALL'
            }
          })
          .then((res) => {
            if (res.success) {
              this.$Message.success(this.$t('kai-shi-an-zhuang'));
              this.getWorkList();
              this.showDeployProgressFunc(workerIds);
            } else if (res.code === '6028') {
              this.nextStep = this.handleInstall;
              this.$refs.stToken.handleShowAkSk();
            }
          });
      }
    },
    handleConfirmUnInstall() {
      this.showConfirmUninstall = false;
      const workerIds = [];

      workerIds.push(this.selectWorker.id);
      if (this.selectWorker.cloudOrIdcName === 'ALIBABA_CLOUD') {
        this.$services
          .ccAliyunEcsUninstall({
            data: {
              clusterId: this.clusterId,
              workerIds,
              pageData: this.pageData,
              deployActionType: 'UNINSTALL'
            }
          })
          .then((res) => {
            if (res.success) {
              this.$Message.success(this.$t('kai-shi-xie-zai'));
              this.getWorkList();
              this.showDeployProgressFunc(workerIds);
            } else if (res.code === '6028') {
              this.nextStep = this.handleConfirmUnInstall;
              this.$refs.stToken.handleShowAkSk();
            }
          });
      }
    },
    handleConfirmUpgradeAll() {
      this.showConfirmUpgrade = false;
      const workerIds = [];

      workerIds.push(this.selectWorker.id);
      this.$services
        .ccAliyunEcsUpgradeAll({
          data: {
            clusterId: this.clusterId,
            workerIds,
            pageData: this.pageData,
            deployActionType: 'UPGRADE_ALL'
          }
        })
        .then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('kai-shi-geng-xin'));
            this.getWorkList();
            this.showDeployProgressFunc(workerIds);
          } else if (res.code === '6028') {
            this.$refs.stToken.handleShowAkSk();
          }
        });
    },
    handleStopInstallAndClean() {
      this.showDeployProgress = false;
      if (this.selectWorker.cloudOrIdcName === 'ALIBABA_CLOUD') {
        this.stopInstall();
      } else {
        this.showStopLocal = true;
      }
    },
    stopInstall() {
      this.showStop = false;
      const workerIds = [];

      workerIds.push(this.selectWorker.id);
      if (this.selectWorker.cloudOrIdcName === 'ALIBABA_CLOUD') {
        this.$services
          .ccAliyunEcsStopInstallAndClean({
            data: {
              clusterId: this.clusterId,
              workerIds,
              pageData: this.pageData,
              deployActionType: 'INSTALL'
            }
          })
          .then((res) => {
            if (res.success) {
              this.$Message.success(this.$t('kai-shi-ting-zhi-bing-qing-chu'));
              clearInterval(this.getProcess);
              this.showDeployProgress = false;
              this.getWorkList();
            } else if (res.code === '6028') {
              clearInterval(this.getProcess);
              this.nextStep = this.stopInstall;
              this.$refs.stToken.handleShowAkSk();
            }
          });
      }
    },
    rowClassName(row) {
      if (this.taskId) {
        if (row.taskId.toString() === this.taskId.toString()) {
          return 'current-task-row';
        }
        return '';
      }
      return '';
    },
    handleSelectWorker(worker) {
      if (worker.id === this.selectWorker.id) return;
      this.selectWorker = worker;
      this.updateSelectedWorker({ clusterId: this.clusterId, id: worker.id });
      // this.queryProcess(workerIds);
      this.listWorkerTaskDetails(worker.id);
      clearInterval(this.getProcess);
    },
    listWorkerTaskDetails(id) {
      this.loadingTasks = true;
      this.$services
        .ccWorkerListWorkerTaskDetails({
          data: {
            workerId: id
          }
        })
        .then((res) => {
          if (res.success) {
            this.selectWorker.taskScheduleVOs = res.data;
          }
          this.loadingTasks = false;
        })
        .finally(() => {
          this.loadingTasks = false;
        });
    },
    toMonitorPage(worker) {
      const { privateIp, publicIp, id } = worker;
      const ip = privateIp || publicIp || '';

      if (ip && id) {
        this.$router.push({ path: `/monitor/worker/graph`, query: { ip: ip, id: id } });
      }
    },
    handleDispatch() {
      this.showDispatch = false;

      if (this.isBatch) {
        const ids = [];
        this.selectedTasks.forEach((task) => {
          ids.push(task.taskId);
        });
        const data = {
          oldWorkerId: this.selectedTasks[0].workerId,
          targetWorkerId: this.workerToDispatch,
          dataTaskIds: ids
        };
        this.$services
          .ccScheduleManualBatchSchedule({
            data
          })
          .then((res) => {
            if (res.success) {
              this.$Message.success(this.$t('shou-dong-tiao-du-cheng-gong'));
              this.getWorkList();
              this.selectedTasks = [];
            }
          });
      } else {
        const data = {
          oldWorkerId: this.selectedRow.workerId,
          targetWorkerId: this.workerToDispatch,
          dataTaskId: this.selectedRow.taskId
        };
        this.$services
          .ccScheduleManualSchedule({
            data
          })
          .then((res) => {
            if (res.success) {
              this.$Message.success(this.$t('shou-dong-tiao-du-cheng-gong'));
              this.getWorkList();
            }
          });
      }
    },
    handleCancelDownloadModal() {
      this.showDownloadLogModal = false;
    },
    handleCancel() {
      this.hostType = 'ADD_ALL';
      this.hasCheckWhiteList = false;
      this.showCheckWhiteListModal = false;
      this.showAddWhiteListModal = false;
      this.showAddWorker = false;
      this.showConfigData = false;
      this.showDownloadClient = false;
      this.showConfigVerify = false;
      this.isBatch = false;
      this.addWorkerForm = {
        clusterId: this.$route.params.id,
        workerIp: '',
        cloudOrIdcName: 'aliyun',
        region: 'hangzhou',
        physicMemMb: 0,
        physicCoreNum: 0,
        physicDiskMb: 0,
        workerType: 'BARE_METAL',
        workerState: ''
      };
      this.addWorker.cloudOrIdcName = this.clusterInfo.cloudOrIdcName;
      this.showDispatch = false;
      this.showLog = false;
      this.verifyCode = '';
      this.showUpdateExternalIp = false;
      this.externalIp = '';
      this.showCleanWokrerLog = false;
      this.isLogCleanStarted = false;
    },
    handleSelectAllTasks(data) {
      this.selectedTasks = data;
    },
    handleSelectAllCancel() {
      this.selectedTasks = [];
    },
    handleSelectTask(data) {
      this.selectedTasks = data;
    },
    handleSelectCancel(data) {
      this.selectedTasks = data;
    },
    handleShowBatchSchedule() {
      if (this.selectedTasks.length < 1) {
        this.$Modal.warning({
          title: this.$t('pi-liang-tiao-du-yi-chang'),
          content: this.$t('qing-zhi-shao-xuan-ze-yi-ge-ren-wu')
        });
        return;
      }
      this.isBatch = true;
      this.showDispatch = true;
    },
    handleCancelMenEdit(worker) {
      worker.showMenOverEdit = false;
    },
    queryProcess(workerIds) {
      this.$services
        .ccAliyunEcsQueryProcess({
          data: {
            clusterId: this.clusterId,
            workerIds,
            pageData: this.pageData,
            deployActionType: 'INSTALL'
          }
        })
        .then((res) => {
          if (res.success) {
            this.selectWorker.consoleTasks = res.data[this.selectWorker.id];
            this.selectWorker.currentStatus = 'process';
            if (this.selectWorker.consoleTasks.length > 0) {
              if (this.selectWorker.consoleTasks[this.selectWorker.consoleTasks.length - 1].taskState === 'SUCCESS') {
                this.selectWorker.currentStatus = 'finish';
                // this.getWorkList();
              }
              this.selectWorker.step = 0;
              this.selectWorker.consoleTasks.map((task) => {
                if (task.taskState !== 'WAIT_START') {
                  this.selectWorker.step++;
                }
                if (task.taskState === 'FAILED') {
                  this.selectWorker.currentStatus = 'error';
                  this.selectWorker.step--;
                }
                return null;
              });
            }
            this.selectWorker = { ...this.selectWorker };
          } else if (res.code === '6028') {
            clearInterval(this.getProcess);
            this.nextStep = this.showDeployProgressFunc(workerIds);
            this.$refs.stToken.handleShowAkSk();
          }
        });
    },
    handleCopy(data) {
      const input = document.createElement('textarea');

      input.value = data;
      document.body.appendChild(input);
      input.select();
      document.execCommand('Copy');
      document.body.removeChild(input);

      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleDownloadClient(worker) {
      this.$services.ccWorkerDownloadClientUrl({ data: { workerId: worker.id } }).then((res) => {
        if (res.success) {
          this.downloadUrl = res.data;
          this.showDownloadClient = true;
        }
      });
    },
    handleDownloadConfig(worker) {
      this.selectWorker = worker;
      this.handleConfirmDownload();
    },
    handleConfirmDownload() {
      this.confirmFetchLoading = true;
      this.$services
        .ccWorkerClientCoreConfig({
          data: {
            workerId: this.selectWorker.id
          }
        })
        .then((res) => {
          if (res.success) {
            this.configData = res.data;
            this.showConfigData = true;
          }
          this.confirmFetchLoading = false;
        });
    },
    handleDownloadDireactly() {
      window.open(this.downloadUrl);
    },
    getConfigData(data) {
      let text = '';

      text += `${data.userAkLabel}=${data.userAkValue}\n`;
      text += `${data.userSkLabel}=${data.userSkValue}\n`;
      text += `${data.wsnLabel}=${data.wsnValue}\n`;
      text += `${data.consoleDomainLabel}=${data.consoleDomainValue}`;
      return text;
    },
    handleAlarm(worker, data) {
      const alertConfigVO = {
        phone: false,
        email: true,
        dingding: true,
        sms: true,
        workerId: worker.id,
        id: worker.alertConfigVO.id
      };

      if (!data) {
        this.showConfirmUpdateAlarm = true;
        this.selectWorker = worker;
      } else {
        this.$services.ccWorkerUpdateAlertConfig({ data: alertConfigVO }).then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
            this.getWorkList();
          } else {
            worker.alertConfigVO.dingding = false;
          }
        });
      }
    },
    handleUpdateAlarm() {
      this.showConfirmUpdateAlarm = false;
      const alertConfigVO = {
        phone: false,
        email: false,
        dingding: false,
        sms: false,
        workerId: this.selectWorker.id,
        id: this.selectWorker.alertConfigVO.id
      };

      this.$services.ccWorkerUpdateAlertConfig({ data: alertConfigVO }).then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('xiu-gai-cheng-gong'));
          this.getWorkList();
        } else {
          this.selectWorker.alertConfigVO.dingding = true;
        }
      });
    },
    handleCancelUpdateAlarm() {
      this.selectWorker.alertConfigVO.dingding = true;
      this.showConfirmUpdateAlarm = false;
      this.showConfirmDelete = false;
      this.showConfirmUninstall = false;
      this.showConfirmOffline = false;
      this.showConfirmOnline = false;
      this.showConfirmUpgrade = false;
      this.autoStart = false;
      this.connectionSucceeded = false;
      this.connectionErrorMsg = '';
      this.useNewCredentials = false;
    },
    handleGoConsoleJob(worker) {
      console.warn(123, worker);

      this.$router.push({ path: `/ccsystem/state/task/${worker.consoleJobId}` });
    },
    handleRetryTask(task) {
      this.$services
        .ccConsoleJobRetryTask({
          data: {
            consoleJobId: this.consoleJobId,
            consoleTaskId: task.id
          }
        })
        .then((res) => {
          if (res.success) {
            this.getConsoleJobInfo();
          }
        });
    },
    handleShowConfirmOffline() {
      this.showConfirmOffline = true;
    },
    handleShowCancelModal(actionType) {
      this.confirmOnlineType = actionType;
      this.showCancelOperationModal = true;
    },
    async handleCancelOperation() {
      const data = {
        clusterId: this.clusterId,
        workers: [
          {
            actionType: this.confirmOnlineType,
            cloudOrIdcName: this.selectWorker.cloudOrIdcName,
            workerId: this.selectWorker.id,
            ...this.autoDeploy,
            remoteIp: this.selectWorker.privateIp,
            manualFill: false
          }
        ]
      };
      const res =
        this.confirmOnlineType === OPERATION_STATUS.CANCEL_UPGRADE
          ? await this.$services.ccAllWorkerStopUpgrade({ data })
          : await this.$services.ccAllWorkerStopRollback({ data });

      if (res.success) {
        this.$Message.success(this.$t('workeroperationi18nthisconfirmonlinetype-cheng-gong', [WORKER_OPERATION_I18N[this.confirmOnlineType]]));
        this.getWorkList();
      }
    },
    async handleShowConfirmOnline(type) {
      const res = await this.$services.ccAllWorkerFetchInstallInfo({
        data: {
          workerIds: [this.selectWorker.id]
        }
      });
      if (res.success) {
        this.useNewCredentials = false;
        res.data.forEach((path) => {
          if (path.workerId === this.selectWorker.id) {
            this.autoDeploy.remoteInstallWorkerPath = path.workerInstallPath || '/home/clougence/';
            this.autoDeploy.localInstallPackagePath = path.installPackagePath;
            this.autoDeploy.privateKeyPath = path.privateKeyPath;
            this.autoDeploy.remoteUser = path.remoteUser;
            this.autoDeploy.remotePassword = path.remotePwd;
            this.autoDeploy.sshAuthType = path.sshAuthType;
            this.autoDeploy.sshPort = path.sshPort;
            this.autoDeploy.hasConnectInfo = path.hasConnectInfo;
            this.rollbackPaths = path.rollbackPaths;
          }
        });
        this.showConfirmOnline = true;
        this.confirmOnlineType = type;
        this.autoDeploy.remoteIp = this.selectWorker.privateIp;
      }
    },
    goConsoleJobList(row) {
      this.$router.push({ path: '/ccsystem/state/task', query: { workerIds: row.id } });
    },
    handleGoMonitor(worker) {
      this.$router.push({ path: `/monitor/worker/graph`, query: { ip: worker.privateIp, id: worker.id } });
    },
    handleShowLogContainer() {
      this.showLog = true;
      this.handleShowLog();
    },
    handleEnableBatchSchedule() {
      this.enableBatch = !this.enableBatch;
      if (!this.enableBatch) {
        this.$refs.selection.selectAll(false);
        this.selectedTasks = [];
      }
    },
    handleSelectLog(logName) {
      this.logData.map((item) => {
        if (item.fileName === logName) {
          this.selectedLog = item;
        }
        return null;
      });
      // 切换日志文件后滚动到底部
      this.scrollLogToBottom();
    },
    handleShowLog(taskId, step, endRow) {
      this.showDownloadLogModal = false;
      if (step === 200) {
        this.logNextLoading = true;
      } else if (step === -200) {
        this.logPreLoading = true;
      } else {
        this.logLoading = true;
      }
      this.$services
        .ccLogViewTailWorkerLog({
          data: {
            workerId: this.selectWorker.id,
            endRow: endRow + step < 0 || step === 0 ? 0 : endRow + step
          }
        })
        .then((res) => {
          if (step === 200) {
            this.logNextLoading = false;
          } else if (step === -200) {
            this.logPreLoading = false;
          } else {
            this.logLoading = false;
          }
          if (res.success) {
            this.logData = res.data;
            if (this.selectedLog && this.selectedLog.fileName) {
              this.logData.map((item) => {
                if (item.fileName === this.selectedLog.fileName) {
                  this.selectedLog = item;
                }
                return null;
              });
            } else if (this.logData.length > 0) {
              this.selectedLog = this.logData[0];
              this.logFileName = this.selectedLog.fileName;
            }
            this.showLog = true;
            // 加载日志内容后滚动到底部
            this.scrollLogToBottom();
          }
        });
    },
    getDataSourceList() {
      this.$services.rdpDataSourceListByCondition().then((res) => {
        if (res.success) {
          this.dsList = res.data;
        }
      });
    },
    handleChangeSearchType() {
      this.searchKey = {};
    },
    handleShowSearch(data) {
      this.showSearch = data;
      if (!data) {
        this.searchKey = {};
        this.getWorkList();
      }
    },
    handleShowDispatchModal(row) {
      console.log('handleShowDispatchModal');
      this.isBatch = false;
      this.showDispatch = true;
      this.selectedRow = row;
    },
    handleConfirmMenOverEdit(worker) {
      this.$services
        .ccWorkerUpdateMemOverSoldPercent({
          data: {
            workerId: worker.id,
            memOverSoldPercent: this.currentMenOver
          }
        })
        .then((res) => {
          if (res.success) {
            worker.showMenOverEdit = false;
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
            this.getWorkList();
          }
        });
    },
    handleShowMenOverEdit(worker) {
      this.currentMenOver = worker.memOverSoldPercent;
    },
    handleShowUpdateExternalIp(worker) {
      this.showUpdateExternalIp = true;
      this.selectWorker = worker;
    },
    handleUpdateExternalIp() {
      this.$services
        .ccWorkerUpdateExternalIp({
          data: {
            workerId: this.selectWorker.id,
            externalIp: this.externalIp
          }
        })
        .then((res) => {
          if (res.success) {
            this.getWorkList();
          }
          this.showUpdateExternalIp = false;
        });
    },
    handleCleanWorkerLogModal(worker) {
      this.startCleanLogTime = dayjs(worker.gmtCreate).format('YYYY-MM-DD');
      this.showCleanWokrerLog = true;
    },
    async handleCleanWorkerLog() {
      try {
        if (!this.startCleanLogTime) return this.$Message.warning(this.$t('qing-xian-xuan-ze-qing-li-kai-shi-shi-jian'));
        this.cleanLogLoading = true;
        const lastDateToClean = dayjs(this.startCleanLogTime).format('YYYY-MM-DD');

        const res = await this.$services.ccWorkerClearLog({
          data: {
            workerId: this.selectWorker.id,
            lastDateToClean
          }
        });
        if (res?.success) {
          setTimeout(() => {
            this.cleanLogLoading = false;
            this.cleanLogConsoleJobId = res?.data;
            this.startCleanLogTime = '';
            this.isLogCleanStarted = true;
          }, 1000);
          return;
        }
        this.cleanLogLoading = false;
      } catch (err) {
        console.error(err);
        this.cleanLogLoading = false;
      }
    },
    goConsolejobDetail() {
      if (this.cleanLogConsoleJobId) {
        this.$router.push({ path: `/ccsystem/state/task/${this.cleanLogConsoleJobId}` });
      }
    }
  }
};
</script>
<style scoped lang="less">
.demo-Circle-inner {
  font-size: 12px !important;
}

.job-header {
  height: 80px;
  background-color: #deefff;
  border: 1px solid #dadada;
  border-right: none;
  position: relative;
  padding: 10px 20px;

  span {
    display: inline-block;
  }

  .job-header-db {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    background-color: #f4c22d;
    color: #ffffff;
    text-align: center;
    line-height: 48px;
    box-shadow: 0 3px 12px 0 rgba(244, 194, 45, 0.79);
    margin-right: 16px;
    vertical-align: middle;

    .iconfont {
      font-size: 20px;
    }
  }

  .job-header-name {
    display: inline-block;
    vertical-align: middle;

    .job-header-desc {
      color: #808ca7;
      margin-top: 4px;
    }

    .job-header-name-main {
      font-size: 16px;
      font-family: PingFangSC-Medium, serif;
      margin-right: 6px;
    }

    .job-info-status-running {
      margin-left: 8px;
    }
  }

  .task-detail-step {
    width: 670px;
    display: inline-block;
    vertical-align: middle;
    margin-left: 100px;

    .ivu-steps .ivu-steps-title {
      background: #deefff;
      margin-left: 16px;
      margin-top: 2px;
      display: inline-block;
    }

    .ivu-steps .ivu-steps-head {
      background: none;
    }

    .ivu-steps .ivu-steps-tail > i {
      height: 1px;
      margin-top: 4px;
      margin-left: 6px;
    }

    .ivu-steps-item.ivu-steps-status-process .ivu-steps-tail > i {
      background-color: #a2a9b6;
    }

    .ivu-steps-item.ivu-steps-status-wait .ivu-steps-head-inner {
      background-color: #a2a9b6;
    }

    .ivu-steps-item.ivu-steps-status-wait .ivu-steps-head-inner span,
    .ivu-steps-item.ivu-steps-status-wait .ivu-steps-head-inner > .ivu-steps-icon {
      color: #ffffff;
    }

    .ivu-steps-item.ivu-steps-status-wait .ivu-steps-tail > i {
      background-color: #a2a9b6;
    }
  }

  .job-header-buttons {
    position: absolute;
    right: 16px;
    top: 24px;
  }
}

.worker-body {
  border-left: 1px solid #dadada;
  border-right: 1px solid #dadada;
  border-bottom: 1px solid #dadada;
  border-radius: 0 0 4px 4px;
}

.worker-list-content {
  margin-top: 10px;

  .worker-item-container {
    width: 740px;
    float: left;

    .worker-item-title {
      font-size: 16px;
      font-weight: 500;
      line-height: 40px;
      background: #f5f5f5;
      padding-left: 16px;
      border-bottom: 1px solid #dadada;
    }

    .worker-item-content {
      background-color: #ffffff;
      border-bottom: 1px solid #dadada;
      position: relative;
      cursor: pointer;

      .worker-info-content {
        padding: 28px 20px 28px 28px;
        position: relative;

        .status-point {
          position: absolute;
          left: 12px;
          top: 37px;
        }

        .alarm-icon {
          width: 20px;
          height: 20px;
          display: inline-block;
          /*border-radius: 50%;*/
          /*background-color: #FF6E0D;*/
          color: #ff6e0d;
          text-align: center;
          line-height: 20px;
          cursor: pointer;
          /*position: absolute;*/
          /*top: 4px;*/
          /*left: 4px;*/
          /*margin-left: 8px;*/

          .iconyibuforce {
            font-size: 14px;
          }
        }

        .to-monitor-icon {
          position: absolute;
          cursor: pointer;
          right: 10px;
          top: 10px;
          font-size: 18px;
        }
      }

      .worker-ip {
        font-size: 18px;
        font-family: PingFangSC-Semibold, serif;
        vertical-align: middle;
        max-width: 260px;
        white-space: nowrap; /* 保持文本在一行显示 */
        overflow: hidden; /* 隐藏超出部分 */
        text-overflow: ellipsis;
        display: inline-block;
      }

      .worker-status {
        display: inline-block;
        padding: 0 10px;
        border-radius: 14px;
        color: #ffffff;
        font-size: 14px;
        vertical-align: middle;
        margin-left: 4px;
      }

      .worker-info {
        margin-top: 6px;
        color: #555;

        .show-more-info {
          margin-left: 10px;
          color: rgba(0, 0, 0, 0.88);
        }
      }

      .worker-item-circle {
        position: absolute;
        right: 20px;
        top: 0;
        width: 440px;
        text-align: center;
        margin-top: 36px;
      }
    }

    .worker-item-selected {
      border-left: 4px solid #0bb9f8;
      background-color: #f3f8ff;

      .worker-item-action {
        width: 100%;
        height: 36px;
        line-height: 36px;
        background-color: #e9f4ff;
        border-top: 1px solid #dadada;
        //padding: 0 20px;

        .worker-action-off {
          float: right;
        }

        .worker-action-item {
          padding: 10px 10px 10px 10px;

          .iconfont {
            margin-right: 6px;
          }
        }

        .worker-action-item-disabled {
          color: #999999;
          cursor: not-allowed;
        }
      }
    }
  }

  .worker-detail-container {
    padding: 20px;
    border-left: 1px solid #dadada;
    background-color: #ffffff;
    margin-left: 740px;
    height: 100%;

    .install-worker-process-content {
      position: relative;

      .install-worker-process {
        background-color: #e7f9ff;
        padding: 8px 10px;
        margin-bottom: 10px;
        border-bottom: 2px solid #cbe3ec;
      }

      .install-worker-process-img {
        position: absolute;
        bottom: 0;
        left: 0;
        background-color: #0bb9f8;
        height: 2px;
        width: 25%;
      }

      .ivu-progress-bg {
        background-color: #0bb9f8 !important;
      }
    }

    .worker-detail-metric {
      background-color: #f5f5f5;
      padding: 28px 0;

      .worker-detail-metric-item {
        display: inline-block;
        width: 16%;
        /*padding: 0 28px;*/
        border-right: 1px solid #dadada;
        text-align: center;

        .worker-count {
          font-size: 21px;
          font-family: Avenir-Medium, serif;
          margin-bottom: 8px;
        }

        &:last-child {
          border-right: none;
        }
      }
    }

    .worker-task-container {
      margin-top: 20px;
      position: relative;

      .show-log-btn {
        position: absolute;
        right: 0;
        top: -8px;
      }

      .batch-schedule-btn {
        position: absolute;
        right: 124px;
        top: -8px;
      }

      .batch-schedule-footer {
        margin-top: 16px;

        button {
          margin-left: 10px;
        }
      }
    }
  }
}

.ivu-table .current-task-row td {
  background-color: rgba(45, 183, 245, 0.1);
}

.install-worker-status {
  font-weight: 500;
  color: rgba(0, 0, 0, 0.88);
}

.worker-operation-dropdown .ivu-dropdown-item {
  padding: 0 !important;
}

.dropdown-content {
  padding: 7px 16px;
  display: block;
}

.workder-log-clean {
  margin-top: 20px;
  margin-right: 10px;
  display: flex;
  align-items: center;
}

.jump-to-log {
  display: flex;
  flex-direction: column;
  align-items: center;

  a {
    display: block;
    color: #0bb9f8;
    margin-top: 10px;
  }
}

.log-tabs {
  background: #ffffff;
  border-radius: 12px;
  //box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: none;
  overflow: hidden;
  transition: all 0.3s ease;

  .ant-tabs-nav {
    margin: 0;
    background: #ffffff;
    border-bottom: none;
  }

  .ant-tabs-tab:first-child {
    border-top-left-radius: 13px;
  }

  .ant-tabs-tab {
    border: 1px solid #e2e8f0;
    background: #ffffff;
    margin-right: 2px;
    border-radius: 8px 8px 0 0;
    color: #666666;

    &:hover {
      color: #1890ff;
    }

    &.ant-tabs-tab-active {
      background: #ffffff;
      color: #1890ff;
      border-color: #1890ff;
      border-bottom: 2px solid #ffffff;
      margin-bottom: -1px;
      z-index: 2;
      position: relative;

      &::after {
        content: '';
        position: absolute;
        left: 0;
        right: 0;
        bottom: -2px;
        height: 2px;
        z-index: 1111;
        background: #ffffff;
      }
    }
  }

  .ant-tabs-content-holder {
    padding: 0;
    background: #ffffff;
  }

  .ant-tabs-tabpane {
    padding: 0;
  }
}

.log-content-card {
  background: transparent;
  border-radius: 0;
  box-shadow: none;
  border-left: 1px solid #e2e8f0;
  border-right: 1px solid #e2e8f0;
  border-bottom: 1px solid #e2e8f0;
  overflow: visible;
  border-bottom-left-radius: 13px;
  border-bottom-right-radius: 13px;
}

.log-info-section {
  padding: 12px 20px;
  border-bottom: 1px solid #e2e8f0;
}

.log-info-item {
  align-items: center;
  margin-right: 16px;
  margin-bottom: 0;
  height: 26px;
}

.log-info-label {
  font-size: 14px;
  color: #64748b;
  margin-right: 8px;
  white-space: nowrap;
}

.log-info-value {
  font-size: 14px;
  color: #334155;
  font-weight: 500;
  padding: 2px 6px;
  border-radius: 4px;
  word-break: break-all;
  white-space: pre-wrap;
}

.log-content-wrapper {
  min-height: 400px;
  max-height: 500px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
}

.log-content-container {
  flex: 1;
  background: #fafafa;
  border-radius: 0 0 12px 12px;
  overflow: auto;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
  }
}

.log-content-wrapper {
  overflow-x: auto !important;
  overflow-y: auto;
  max-height: 500px;
}

.log-content-text {
  font-size: 12px;
  line-height: 1.6;
  color: #1e293b;
  background: transparent;
  border: none;
  padding: 20px;
  margin: 0;
  white-space: pre !important;
  overflow: visible !important;
}

.log-content-wrapper::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.log-content-wrapper::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.log-content-wrapper::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.log-content-wrapper::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.log-empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background: #fafafa;
  border-radius: 0 0 12px 12px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.6;
}

.empty-text {
  color: #64748b;
  font-size: 14px;
  margin: 0;
  font-weight: 400;
}

.show-log-container {
  .ant-modal-content {
    border-radius: 12px;
    overflow: hidden;
    box-shadow:
      0 20px 25px -5px rgba(0, 0, 0, 0.1),
      0 10px 10px -5px rgba(0, 0, 0, 0.04);
  }

  .ant-modal-header {
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    border-bottom: 1px solid #e2e8f0;
    padding: 20px 24px 16px;
  }

  .ant-modal-title {
    font-size: 16px;
    font-weight: 600;
    color: #1e293b;
  }

  .ant-modal-body {
    padding: 0;
    background: #ffffff;
  }

  .ant-modal-footer {
    background: #ffffff;
    border-top: 1px solid #e2e8f0;
    padding: 16px 24px;
    text-align: right;

    .ant-btn {
      border-radius: 6px;
      font-weight: 500;

      &:not(:last-child) {
        margin-right: 8px;
      }
    }
  }
}
</style>
