<template>
  <div class="sub-account">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem to="/project">{{ $t('xiang-mu-lie-biao') }}</BreadcrumbItem>
            <BreadcrumbItem :to="'/project/' + changeInfo.projectId">
              {{ changeInfo.projectName }}
            </BreadcrumbItem>
            <BreadcrumbItem>{{ changeInfo?.changeName + $t('bian-geng') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="sql-change-container">
          <div class="header">
            <div class="left-wrap">
              <div class="title-wrap">
                <CustomIcon type="icon-v2-hebing" rightMargin size="18px" />
                <Tooltip :content="changeInfo?.changeName">
                  <span class="title-text-ellipsis">{{ $t('bian-geng-ming-cheng') + ': ' + changeInfo?.changeName }}</span>
                </Tooltip>
                <Tooltip :content="changeInfo.remark" style="width: 450px" v-if="changeInfo.remark">
                  <span
                    class="collapse-text-ellipsis"
                    :class="changeInfo.currentStatus === 'FAILED' ? 'red-text' : 'gray-text'"
                    v-if="changeInfo.remark"
                  >
                    {{ '(' + changeInfo.remark + ')' }}
                  </span>
                </Tooltip>
              </div>
              <div class="card-wrap">
                <div class="left">
                  <CustomIcon :type="changeInfo?.scmType" size="25px" rightMargin="10px" />
                  <div>
                    <div>
                      {{ $t('cang-ku') + ': ' + changeInfo?.repoName || '-' }}
                    </div>
                    <div>
                      {{ $t('fen-zhi') + ': ' + changeInfo?.repoBranch || '-' }}
                    </div>
                    <div>
                      {{ $t('lu-jing') + changeInfo?.repoScriptPath || '-' }}
                    </div>
                  </div>
                </div>
                <div class="mid">
                  <CustomIcon type="icon-v2-shuang-you" size="50px" />
                </div>
                <div class="right">
                  <CustomIcon :type="changeInfo?.dsType" size="25px" rightMargin="10px" />
                  <div>
                    <div>
                      {{ $t('shi-li-0') + ': ' + changeInfo?.dsInstance || '-' }}
                    </div>
                    <div>
                      {{ $t('miao-shu') + ': ' + changeInfo?.dsDesc || '-' }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="right-wrap">
              <div class="btns">
                <Button @click="skipCheck" type="primary" v-if="!(isBtnOnlyRead || canJumpCheck || changeInfo.currentStep !== 'CHECK')">
                  {{ isErrorCheck ? $t('tiao-guo') : $t('tiao-guo-jian-ce') }}
                </Button>
                <Button @click="retryChange" v-if="changeInfo.currentStep !== 'APPROVAL' && !(cantRetry || isBtnOnlyRead || isReadyStatus)">
                  {{ $t('zhong-shi-bian-geng') }}
                </Button>
                <Button @click="retryChange" v-if="changeInfo.currentStep === 'APPROVAL' && !(cantRetry || isBtnOnlyRead || isReadyStatus)">
                  {{ $t('zhong-xin-fa-qi-gong-dan') }}
                </Button>
                <Button @click="closeChange" v-if="!isBtnOnlyRead">{{ $t('guan-bi-bian-geng') }}</Button>
                <Button @click="handleRefresh" :loading="loading">
                  <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
                </Button>
              </div>
              <Steps
                :current="CHANGE_STATUS_MAP[changeInfo?.currentStep]"
                :status="STATUS_MAP[changeInfo?.currentStatus]"
                size="small"
                class="step-wrap"
                v-if="changeInfo.currentStatus !== 'CLOSED' && changeInfo.currentStep !== 'INIT_SNAPSHOT'"
              >
                <Step :title="$t('di-jiao')"></Step>
                <Step :title="$t('sql-shen-he')"></Step>
                <Step :title="$t('shen-pi-liu')"></Step>
                <Step :title="$t('zhi-xing')"></Step>
              </Steps>
              <Steps
                :current="CHANGE_STATUS_MAP[changeInfo?.currentStep]"
                :status="STATUS_MAP[changeInfo?.currentStatus]"
                size="small"
                class="step-wrap"
                v-if="changeInfo.currentStatus === 'CLOSED' && changeInfo.currentStep !== 'INIT_SNAPSHOT'"
              >
                <Step :title="$t('di-jiao')"></Step>
                <Step :title="$t('bian-geng-guan-bi')"></Step>
              </Steps>
              <Steps
                :current="CHANGE_STATUS_MAP[changeInfo?.currentStep]"
                :status="STATUS_MAP[changeInfo?.currentStatus]"
                size="small"
                class="step-wrap"
                v-if="changeInfo.currentStep === 'INIT_SNAPSHOT'"
              >
                <Step :title="$t('di-jiao')"></Step>
                <Step :title="$t('kuai-zhao-bian-geng')"></Step>
              </Steps>
            </div>
          </div>
          <div class="content-wrap">
            <Tabs v-model="currentTab" class="tab-wrap" @on-click="tabClick">
              <TabPane
                :label="renderDropdownTab"
                name="sql-change"
                :disabled="CHANGE_STATUS_MAP[changeInfo?.currentStep] < PROJECT_STEP.S0 || changeInfo?.currentStep === 'INIT_SNAPSHOT'"
              ></TabPane>
              <TabPane
                :label="$t('sql-shen-he')"
                name="sql-audit"
                :disabled="CHANGE_STATUS_MAP[changeInfo?.currentStep] < PROJECT_STEP.S1 || changeInfo?.currentStep === 'INIT_SNAPSHOT'"
              ></TabPane>

              <TabPane
                :label="$t('shen-pi-liu-cheng')"
                name="approval"
                :disabled="
                  CHANGE_STATUS_MAP[changeInfo?.currentStep] < PROJECT_STEP.S2 || changeInfo?.currentStep === 'INIT_SNAPSHOT' || isDisabledApproval
                "
              ></TabPane>

              <TabPane
                :label="$t('bian-geng-zhi-xing')"
                name="execute"
                :disabled="
                  CHANGE_STATUS_MAP[changeInfo?.currentStep] < PROJECT_STEP.FINISH || changeInfo?.currentStep === 'INIT_SNAPSHOT' || isManualExec
                "
              ></TabPane>
            </Tabs>
            <div class="tab-item-wrap">
              <div v-if="currentTab === 'sql-change'" class="tab-item">
                <div v-if="isNotChangeReady" style="height: 100%">
                  <div v-if="subTabLabel === '结果'" style="height: 100%">
                    <read-only-editor :text="rowSql" key="raw" v-if="rowSql.length" />
                    <CCEmptyContent v-else :content="changeInfo?.remark ? changeInfo?.remark : $t('wu-bian-geng-nei-rong')" />
                  </div>
                  <div v-if="subTabLabel === 'Diff'" style="height: 100%">
                    <Collapse v-model="activeNames" accordion v-if="changeBody.length">
                      <Panel v-for="(item, index) in changeBody" :key="index" :name="index.toString()">
                        {{ item.contentName }}
                        <template #content>
                          <div style="height: 400px">
                            <ChangeBodyDiff :original="item.oldBody || ''" :modified="item.newBody || ''" language="sql" />
                          </div>
                        </template>
                      </Panel>
                    </Collapse>
                    <CCEmptyContent v-else :content="changeInfo?.remark ? changeInfo?.remark : $t('wu-bian-geng-nei-rong')" />
                  </div>
                </div>
                <CCEmptyContent v-else loading :content="$t('bian-geng-nei-rong-fen-xi-zhong')" />
              </div>
              <div v-if="currentTab === 'sql-audit'" class="tab-item">
                <Collapse v-model="curCollapse" v-if="checkedSql?.length" simple>
                  <Panel v-for="(item, index) in checkedSql" :key="index">
                    <span class="collapse-text-ellipsis">{{ item.content }}</span>
                    <Button type="text" class="collapse-btn" @click.stop="getSqlDetail(item?.content)">
                      {{ $t('cha-kan') }}
                    </Button>
                    <template #content>
                      <div>
                        <Table
                          :columns="projectBecomeTableColumns"
                          :data="item.checkList"
                          :loading="loading"
                          :locale="{ emptyText: $t('zan-wu-shu-ju') }"
                          size="small"
                          border
                          stripe
                        >
                          <template #level="{ row }">
                            <Tag :color="ERROR_LEVEL_COLOR_MAP[row?.level]">
                              {{ ERROR_LEVEL_MAP[row?.level] }}
                            </Tag>
                          </template>
                        </Table>
                      </div>
                    </template>
                  </Panel>
                </Collapse>
                <div v-else class="empty-div">
                  <CCEmptyContent v-if="isReadyStatus" loading :content="$t('bian-geng-nei-rong-fen-xi-zhong')" />
                  <CCEmptyContent v-else-if="!isReadyStatus && !isErrorCheck" :content="$t('dang-qian-mei-you-yi-chang-sql')" />
                  <CCEmptyContent v-else icon="icon-v2-Error2" :content="changeInfo?.remark" />
                </div>
              </div>
              <div v-if="currentTab === 'approval'" class="tab-item">
                <CCEmptyContent v-if="approveText" :link="`/ticket/${currentTicket?.ticketId}`" :icon="approveIcon" :content="approveText" />
                <CCEmptyContent v-else loading :content="$t('gong-dan-xin-xi-huo-qu-zhong')" />
              </div>
              <div v-if="currentTab === 'execute'" class="tab-item">
                <div class="exec-wrap" v-if="!isScheduling">
                  <div class="exec-left" v-if="changeInfo.currentStatus !== 'OPEN'">
                    <Card class="ticket-content">
                      <template #title>
                        <div style="display: flex; align-items: center; width: 100%; justify-content: space-between">
                          <div class="left" style="display: flex; align-items: center">
                            <div style="margin-right: 10px">
                              {{ $t('ren-wu-zhi-hang') }}
                            </div>
                            <Poptip :content="autoExecJobInfo?.message" trigger="hover" style="margin-right: 10px" v-if="!autoExecJobInfo?.normal">
                              <Icon type="ios-alert-outline" />
                            </Poptip>
                            <Tag :color="AUTO_EXEC_JOB_STATUS_COLOR[autoExecJobInfo?.status]" style="margin-right: 10px">
                              {{ AUTO_EXEC_JOB_STATUS_I18N[autoExecJobInfo?.status] }}
                            </Tag>
                            <div v-if="autoExecJobInfo?.execTime" style="margin-right: 10px">
                              {{ $t('ji-hua-zhi-hang-shi-jian') }}
                              {{ autoExecJobInfo?.execTime }}
                            </div>
                            <div v-if="autoExecJobInfo?.workerIp" style="margin-right: 10px">
                              {{ $t('ji-qi-ip-0') }}
                              {{ autoExecJobInfo?.workerIp }}
                            </div>
                            <div v-if="autoExecJobInfo?.workerStatus" style="margin-right: 10px">
                              {{ $t('ji-qi-zhuang-tai-0') }}
                              {{ autoExecJobInfo?.workerStatus }}
                            </div>
                          </div>
                          <div class="right" style="display: flex; align-items: center">
                            <!--          <div v-if="autoExecJobInfo.lastReportTime">-->
                            <!--            {{ $t('zui-hou-yi-ci-shang-bao-shi-jian') }} {{autoExecJobInfo.lastReportTime }}-->
                            <!--          </div>-->
                            <Button
                              type="text"
                              size="small"
                              v-if="autoExecJobInfo?.canEnd"
                              :disabled="isBtnOnlyRead"
                              @click="handleShowEndAutoExecJobModal"
                            >
                              {{ $t('zhong-zhi') }}
                            </Button>
                            <Button
                              type="text"
                              size="small"
                              v-if="autoExecJobInfo?.canPause"
                              :disabled="isBtnOnlyRead"
                              @click="handleShowStopAutoExecJobModal"
                            >
                              {{ $t('zan-ting') }}
                            </Button>
                            <Button
                              type="text"
                              size="small"
                              v-if="autoExecJobInfo?.canRestart"
                              :disabled="isBtnOnlyRead"
                              @click="handleShowRetryAutoExecJobModal"
                            >
                              {{ $t('hui-fu') }}
                            </Button>
                            <Button
                              type="text"
                              size="small"
                              v-if="autoExecJobInfo?.canRetry"
                              @click="handleShowRetryAutoExecJobModal"
                              :disabled="isBtnOnlyRead"
                            >
                              {{ $t('zhong-shi') }}
                            </Button>
                            <Button type="text" size="small" @click="handleAutoExecLog(null)" :disabled="isBtnOnlyRead">
                              {{ $t('tiao-du-ri-zhi') }}
                            </Button>
                            <!-- <Button
                              type="text"
                              size="small"
                              @click="handleRefreshTaskList"
                              >{{ $t("shua-xin") }}</Button
                            > -->
                          </div>
                        </div>
                      </template>
                      <Table :columns="autoExecTaskColumns" :data="autoExecTaskList" border stripe size="small">
                        <template #status="{ row }">
                          <Tag :color="AUTO_EXEC_TASK_STATUS_COLOR[row?.status]">{{ AUTO_EXEC_TASK_STATUS_I18N[row?.status] }}</Tag>
                        </template>
                        <template #sql="{ row }">
                          <span style="display: inline-block; width: 100%; overflow: hidden; text-overflow: ellipsis; white-space: nowrap">
                            {{ row.execSql }}
                          </span>
                        </template>
                        <template #action="{ row }">
                          <!--          <Button type="text" size="small" @click="handleAutoExecSQL(row)">{{ $t('cha-kan-sql') }}</Button>-->
                          <Button type="text" size="small" @click="handleAutoExecLog(row)">{{ $t('ri-zhi') }}</Button>
                          <Button type="text" size="small" @click="getSqlDetail(row?.execSql)">{{ $t('cha-kan') }}</Button>
                          <Button type="text" size="small" @click="handleShowSkipAutoExecTaskModal(row)" :disabled="isBtnOnlyRead" v-if="row.canSkip">
                            {{ $t('tiao-guo') }}
                          </Button>
                          <Button
                            type="text"
                            size="small"
                            @click="handleShowContinueAutoExecTaskModal(row)"
                            :disabled="isBtnOnlyRead"
                            v-if="row.canCancelSkip"
                          >
                            {{ $t('qu-xiao-tiao-guo') }}
                          </Button>
                        </template>
                      </Table>
                      <div style="width: 100%; text-align: right">
                        <Page
                          v-model="page"
                          :page-size="pageSize"
                          :total="total"
                          @on-change="handleTaskPageChange"
                          size="small"
                          style="margin-top: 10px"
                        />
                      </div>
                    </Card>
                  </div>
                  <div class="exec-right" v-if="changeInfo.currentStatus === 'OPEN'">
                    <div class="exec-border">
                      <Form :model="confirmInfo.config" :label-width="60" style="border: 1px">
                        <FormItem style="margin-bottom: 0" :label="$t('zhi-hang-ce-lve')" prop="autoExecType">
                          <RadioGroup v-model="confirmInfo.config.autoExecType">
                            <Radio label="MANUAL_EXEC">{{ $t('bu-zhi-hang') }}</Radio>
                            <Radio label="IMMEDIATE">{{ $t('li-ji') }}</Radio>
                            <Radio label="SPECIFY_TIME">{{ $t('ding-shi') }}</Radio>
                          </RadioGroup>
                          <DatePicker
                            v-if="confirmInfo.config.autoExecType === 'SPECIFY_TIME'"
                            v-model="confirmInfo.config.execTime"
                            size="small"
                            type="datetime"
                            :placeholder="$t('qing-xuan-ze-zhi-hang-shi-jian')"
                          />
                        </FormItem>
                        <FormItem style="margin-bottom: 0" :label="$t('shi-wu')" prop="enableTransactional">
                          <i-switch
                            v-model="confirmInfo.config.enableTransactional"
                            size="large"
                            :disabled="confirmInfo.config.autoExecType === 'MANUAL_EXEC'"
                          >
                            <template #open>
                              <span>{{ $t('kai-qi-0') }}</span>
                            </template>
                            <template #close>
                              <span>{{ $t('wu-0') }}</span>
                            </template>
                          </i-switch>
                          <span style="color: #aaa">
                            {{ $t('ru-guo-sql-yu-ju-zhong-cun-zai-fei-dml-yu-ju-ke-neng-hui-bei-fen-wei-duo-ge-shi-wu-zhi-hang') }}
                          </span>
                        </FormItem>
                      </Form>
                      <div class="right-footer">
                        <Button
                          type="primary"
                          @click="handleFinishTicket"
                          :disabled="isBtnOnlyRead"
                          v-if="confirmInfo.config.autoExecType === 'MANUAL_EXEC'"
                        >
                          {{ $t('jie-shu-gong-dan') }}
                        </Button>
                        <Button
                          type="primary"
                          @click="handleConfirmTicketByNow"
                          :disabled="isBtnOnlyRead"
                          v-if="confirmInfo.config.autoExecType === 'IMMEDIATE'"
                        >
                          {{ $t('li-ji-zhi-hang') }}
                        </Button>
                        <Button
                          type="primary"
                          @click="handleConfirmTicketByTime"
                          :disabled="isBtnOnlyRead"
                          v-if="confirmInfo.config.autoExecType === 'SPECIFY_TIME'"
                        >
                          {{ $t('ding-shi-zhi-hang') }}
                        </Button>
                      </div>
                    </div>
                  </div>
                </div>
                <CCEmptyContent v-else :content="$t('xi-tong-tiao-du-zhong')" loading />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <CCModal v-model="showAutoExecJobLogModal" :title="$t('ri-zhi')" @ok="handleCloseModal" :width="800">
      <Table :columns="autoExecJobLogColumns" :data="autoExecJobLogList" border stripe size="small" />
    </CCModal>
    <CCModal v-model="showAutoExecTaskLogModal" :title="$t('ri-zhi')" @ok="handleCloseModal" :width="800">
      <Table :columns="autoExecJobLogColumns" :data="autoExecTaskLogList" border stripe size="small" />
    </CCModal>
    <CCModal v-model="showAutoExecTaskSQLModal" :title="$t('sql-yu-ju')" @ok="handleCloseModal" :width="800">
      {{ selectedAutoExecTask.execSql }}
    </CCModal>
    <CCModal v-model="showStopAutoExecJobModal" :title="$t('zan-ting')" @ok="handleStopAutoExecJob">
      {{
        $t(
          'zan-ting-jiang-zhong-duan-dang-qian-zheng-zai-zhi-hang-de-sql-bing-ting-zhi-tiao-du-hou-xu-de-sql-yi-cheng-gong-de-sql-bu-shou-yin-xiang-ru-guo-dang-qian-zheng-zai-de-sql-chu-yu-shi-wu-zhi-zhong-zheng-ge-shi-wu-jiang-hui-bei-hui-gun'
        )
      }}
    </CCModal>
    <CCModal v-model="showRetryAutoExecJobModal" :title="$t('zhong-shi')" @ok="handleRetryAutoExecJob">
      {{ $t('jiang-zhong-xin-zhi-hang-yi-shi-bai-dai-zhi-hang-hui-gun-he-dai-que-ren-de-ren-wu') }}
    </CCModal>
    <CCModal v-model="showEndAutoExecJobModal" :title="$t('zhong-zhi')" @ok="handleEndAutoExecJob">
      {{ $t('zhong-zhi-hou-jiang-wu-fa-zhi-hang-ren-wu-qie-hui-guan-bi-gong-dan') }}
    </CCModal>
    <CCModal v-model="showSkipAutoExecTaskModal" :title="$t('tiao-guo')" @ok="handleSkipAutoExecTask">
      {{ $t('tiao-guo-hou-zhong-shi-ren-wu-shi-jiang-hui-tiao-guo-gai-sql-zhi-hang') }}
    </CCModal>
    <CCModal v-model="showContinueSkipAutoExecTaskModal" :title="$t('qu-xiao-tiao-guo')" @ok="handleContinueAutoExecTask">
      {{ $t('qu-xiao-tiao-guo-hou-xia-ci-zhong-shi-ren-wu-shi-jiang-zhi-hang-gai-sql') }}
    </CCModal>
    <CCModal v-model="showContinueSkipAutoExecTaskModal" :title="$t('qu-xiao-tiao-guo')" @ok="handleContinueAutoExecTask">
      {{ $t('qu-xiao-tiao-guo-hou-xia-ci-zhong-shi-ren-wu-shi-jiang-zhi-hang-gai-sql') }}
    </CCModal>
    <CCModal width="800" v-model="showAllSql" :title="$t('cha-kan-wan-zheng-sql')">
      <read-only-editor :text="allSql" key="raw" :max-height="300" />
    </CCModal>
    <CCModal width="800" v-model="showFinishTicket" :title="$t('jie-shu-gong-dan')">
      <Alert type="warning">
        {{
          $t(
            'xia-mian-sql-xi-tong-bu-hui-zhi-hang-xu-yao-ren-gong-fang-shi-zai-shu-ju-ku-zhong-shou-dong-zhi-hang-dang-nin-du-chu-li-hao-hou-ke-yi-dian-ji-xia-mian-que-ren-bing-guan-bi-lai-jie-shu-gong-dan'
          )
        }}
      </Alert>
      <read-only-editor :text="rowSql" key="raw" :max-height="600" />
      <template #footer>
        <div>
          <Button @click="copySql">{{ $t('fu-zhi-sql') }}</Button>
          <Button type="primary" @click="confirmFinishTicket">{{ $t('que-ren-jie-shu') }}</Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import { encryptMixin } from '@/mixins/encryptMixin';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import { handleCopy } from '@/utils/clipboard';
import CCEmptyContent from '@/components/widgets/CCEmptyContent';
import ChangeBodyDiff from './changeBodyDiff';
import { h, resolveComponent } from 'vue';

import {
  projectBecomeTableColumns,
  ERROR_LEVEL_MAP,
  PROJECT_STEP,
  CHANGE_STATUS_MAP,
  STATUS_MAP,
  AUTO_EXEC_JOB_STATUS_COLOR,
  AUTO_EXEC_JOB_STATUS_I18N,
  AUTO_EXEC_TASK_STATUS_COLOR,
  AUTO_EXEC_TASK_STATUS_I18N,
  ERROR_LEVEL_COLOR_MAP,
  PROJECT_STEP_NUM
} from './constant';

export default {
  name: 'changeDetail',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['isSaas']),
    approveStatusMap() {
      return {
        WAIT: {
          icon: 'icon-v2-Progress',
          text: `相关联的${this.changeInfo?.changeName || ''}变更工单，正在审批中`
        },
        FAILED: {
          icon: 'icon-v2-error',
          text: `相关联的${this.changeInfo?.changeName || ''}变更工单，审批被拒绝`
        },
        FINISH: {
          icon: 'icon-v2-Success2',
          text: `相关联的${this.changeInfo?.changeName || ''}变更工单，审批通过`
        },
        CLOSED: {
          icon: 'icon-v2-Warning2',
          text: `相关联的${this.changeInfo?.changeName || ''}变更工单，审批关闭`
        }
      };
    },
    currentApproveStatus() {
      const enumStatus = this.currentTicket?.ticketStatus;
      switch (enumStatus) {
        case 'WAIT_APPROVAL':
        case 'WAIT_CONFIRM':
          return 'WAIT';
        case 'REJECTED':
        case 'FAILED':
          return 'FAILED';
        case 'FINISHED':
          return 'FINISH';
        case 'CLOSED':
        case 'CANCELED':
          return 'CLOSED';
        default:
          return null;
      }
    },
    approveIcon() {
      const status = this.currentTicket?.ticketStatus || '';
      return this.approveStatusMap[this.currentApproveStatus]?.icon || '';
    },
    approveText() {
      const status = this.currentTicket?.ticketStatus || '';
      return this.approveStatusMap[this.currentApproveStatus]?.text || '';
    },
    isBtnOnlyRead() {
      return this.changeInfo.locked;
    },
    isNotChangeReady() {
      return PROJECT_STEP_NUM[this.changeInfo.currentStep] > PROJECT_STEP_NUM.INIT || this.changeInfo.currentStatus !== 'READY';
    },
    isNotCheckReady() {
      return PROJECT_STEP_NUM[this.changeInfo.currentStep] > PROJECT_STEP_NUM.CHECK || this.changeInfo.currentStatus !== 'READY';
    },
    isNotApproveReady() {
      return PROJECT_STEP_NUM[this.changeInfo.currentStep] > PROJECT_STEP_NUM.APPROVAL || this.changeInfo.currentStatus !== 'READY';
    },
    isNotExecuteReady() {
      return PROJECT_STEP_NUM[this.changeInfo.currentStep] === PROJECT_STEP_NUM.EXECUTE || this.changeInfo.currentStatus !== 'READY';
    },
    isErrorCheck() {
      return this.changeInfo.currentStatus === 'FAILED' && PROJECT_STEP_NUM[this.changeInfo.currentStep] === PROJECT_STEP_NUM.CHECK;
    },
    isReadyStatus() {
      return this.changeInfo.currentStatus === 'READY';
    },
    cantRetry() {
      return this.changeInfo.currentStatus !== 'FAILED' || PROJECT_STEP_NUM[this.changeInfo.currentStep] === PROJECT_STEP_NUM.EXECUTE;
    },
    isDisabledApproval() {
      return this.changeInfo.flowApprove === 'Disable';
    },
    canJumpCheck() {
      return this.currentTab !== 'sql-audit';
    }
  },
  watch: {
    currentTab(newVal) {
      this.$router.replace({ query: { ...this.$route.query, tab: newVal } });
    }
  },
  components: {
    ReadOnlyEditor,
    ChangeBodyDiff,
    CCEmptyContent
  },
  data() {
    return {
      showContinueSkipAutoExecTaskModal: false,
      showSkipAutoExecTaskModal: false,
      showEndAutoExecJobModal: false,
      showRetryAutoExecJobModal: false,
      showStopAutoExecJobModal: false,
      showAutoExecTaskSQLModal: false,
      showAutoExecTaskLogModal: false,
      showAutoExecJobLogModal: false,
      showFinishTicket: false,
      showAllSql: false,
      isManualExec: false,
      isScheduling: false,
      activeNames: [],
      autoExecTaskList: [],
      autoExecJobLogList: [],
      autoExecTaskLogList: [],
      selectedAutoExecTask: {},
      projectBecomeTableColumns,
      showAddModal: false,
      loading: false,
      changeName: '',
      allSql: '',
      changeInfo: {},
      rowSql: '',
      checkedSql: [],
      changeBody: [],
      currentTab: '',
      subTabLabel: this.$t('jie-guo'),
      confirmInfo: {
        changeId: '',
        config: {
          enableTransactional: false,
          errorStrategy: 'NONE',
          retryWaitTime: 111,
          retryCount: 2,
          autoExecType: 'IMMEDIATE',
          execTime: new Date()
        }
      },
      page: 1,
      pageSize: 10,
      total: 0,
      formModal: {
        name: '',
        code: ''
      },
      changeId: '',
      currentTicket: {},
      autoExecJobInfo: {
        message: '',
        normal: true,
        status: '',
        execTime: '',
        workerIp: '',
        workerStatus: '',
        canEnd: false,
        canPause: false,
        canRestart: false,
        canRetry: false,
        lastReportTime: ''
      },
      autoExecJobLogColumns: [
        {
          title: '等级',
          key: 'logLevel',
          width: 100
        },
        {
          title: '时间',
          key: 'time',
          width: 200
        },
        {
          title: '内容',
          key: 'content'
        }
      ],
      autoExecTaskColumns: [
        {
          title: '序号',
          key: 'executeOrder',
          width: 80
        },
        {
          title: '执行次数',
          key: 'execCount',
          width: 100
        },
        {
          title: '状态',
          slot: 'status',
          width: 100
        },
        {
          title: 'SQL 语句',
          slot: 'sql'
        },
        {
          title: '操作',
          width: 200,
          fixed: 'right',
          slot: 'action'
        }
      ],
      curCollapse: [],
      showEditBecomeName: false,
      ERROR_LEVEL_MAP,
      PROJECT_STEP_NUM,
      PROJECT_STEP,
      CHANGE_STATUS_MAP,
      AUTO_EXEC_JOB_STATUS_COLOR,
      AUTO_EXEC_JOB_STATUS_I18N,
      AUTO_EXEC_TASK_STATUS_COLOR,
      AUTO_EXEC_TASK_STATUS_I18N,
      STATUS_MAP,
      ERROR_LEVEL_COLOR_MAP
    };
  },
  created() {
    if (this.$route.query.tab) {
      this.currentTab = this.$route.query.tab;
    }
  },
  async mounted() {
    await this.init();
  },
  methods: {
    async init() {
      this.changeId = this.$route.params.id;
      await this.getDetail();
      this.moveToCurrentTab();
      if (this.currentTab === 'sql-change' && this.isNotChangeReady) {
        this.getRowSql();
      }
      if (this.currentTab === 'sql-audit' && this.isNotCheckReady) {
        this.getCheckedSql();
      }
      if (this.currentTab === 'approval' && this.isNotApproveReady) {
        this.getApproval();
      }
      if (this.currentTab === 'execute' && this.isNotExecuteReady) {
        this.getExecuteState();
      }
    },
    async getDetail() {
      this.loading = true;
      const res = await this.$services.dmProjectChangeChangeDetail({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.changeInfo = res.data;
      }
      this.loading = false;
    },
    async getRowSql() {
      this.loading = true;
      const res = await this.$services.dmProjectChangeChangeBody({
        data: {
          changeId: this.changeId
        }
      });

      this.loading = false;
      this.changeBody = res.data?.itemList || [];
      this.rowSql = res.data?.changeBody;
    },
    async getApproval() {
      const res = await this.$services.dmProjectChangeChangeApproval({
        data: {
          changeId: this.changeId
        }
      });
      if (res.success) {
        this.currentTicket = res.data;
      }
    },
    async getCheckedSql() {
      this.loading = true;

      const res = await this.$services.dmProjectChangeChangeChecks({
        data: {
          changeId: this.changeId
        }
      });

      this.loading = false;
      this.checkedSql = res.data;
    },

    async getExecuteState() {
      if (this.isReadyStatus) {
        this.isScheduling = true;
        return;
      }
      const res = await this.$services.dmProjectChangeChangeExecute({
        data: {
          changeId: this.changeId
        }
      });

      // 手动执行
      if (res.success && res?.data) {
        this.isScheduling = false;
        if (res?.data?.execType === 'MANUAL_EXEC') {
          this.isManualExec = true;
          this.currentTab = 'sql-change';
          this.$Message.info(this.$t('shou-dong-zhi-hang-mo-shi-xia-qing-zi-hang-fu-zhi-bian-geng-nei-rong-qian-qu-zhi-hang'));
          return;
        }
        this.handleRefreshTaskList();
      }
      this.isScheduling = false;
    },
    handleAdd() {
      this.showAddModal = true;
    },
    handleClose() {
      this.showAddModal = false;
    },
    getSqlDetail(content) {
      this.allSql = content;
      this.showAllSql = true;
    },
    handleSubmit() {},
    goDetail(row) {
      this.$router.push(`/project/${row?.id || 1}`);
    },
    getCyclicKey(type, index) {
      const items = {
        nameItems: ['empty-condition', 'with-clause', 'subquery-insert'],
        typeItems: ['submit-type', 'sql-audit-type', 'audit-method', 'execute-type']
      };
      return items[type][index % items[type].length];
    },
    async closeChange() {
      const res = await this.$services.dmProjectChangeChangeClose({
        data: {
          changeId: this.changeId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('guan-bi-cheng-gong'));
        this.init();
      }
    },

    async tabClick(name) {
      await await this.getDetail();
      if (name === 'sql-change' && !this.isReadyStatus) {
        this.getRowSql();
      }
      if (name === 'sql-audit' && !this.isReadyStatus) {
        this.getCheckedSql();
      }
      if (name === 'approval' && !this.isReadyStatus) {
        this.getApproval();
      }
      if (name === 'execute') {
        this.getExecuteState();
      }
    },
    goTicket() {
      if (this.currentTicket) {
        this.$router.push({ path: `/ticket/${this.currentTicket?.ticketId}` });
      }
    },
    async handleFinishTicket() {
      await this.getRowSql();
      this.showFinishTicket = true;
    },
    async confirmFinishTicket() {
      const data = { ...this.confirmInfo };
      data.changeId = this.changeId;
      data.config.execTime = null;

      const res = await this.$services.dmProjectChangeConfirmExec({ data });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.showFinishTicket = false;
        this.init();
        this.queryAutoExecJobInfo();
      }
    },
    copySql() {
      handleCopy(this.rowSql);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    async handleConfirmTicketByNow() {
      const data = { ...this.confirmInfo };
      data.changeId = this.changeId;
      data.config.execTime = null;

      const res = await this.$services.dmProjectChangeConfirmExec({ data });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.init();
        this.queryAutoExecJobInfo();
      }
    },
    async handleConfirmTicketByTime() {
      const data = { ...this.confirmInfo };
      data.changeId = this.changeId;
      if (data.config.execTime == null) {
        this.$Message.success(this.$t('shi-jian-wei-kong'));
        return;
      } else {
        data.config.execTime = Date.parse(data.config.execTime);
      }

      const res = await this.$services.dmProjectChangeConfirmExec({ data });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.init();
        this.queryAutoExecJobInfo();
      }
    },
    async queryAutoExecJobInfo() {
      const res = await this.$services.dmTicketQueryAutoExecJobInfo({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.autoExecJobInfo = res.data;
      }
    },
    async handleAutoExecLog(task = null) {
      const res = await this.$services.dmTicketAutoExecLog({
        data: {
          changeId: this.changeId,
          taskId: task ? task.taskId : null,
          jobId: this.autoExecJobInfo.id,
          bizType: task ? 'AUTO_EXEC_TASK' : 'AUTO_EXEC_JOB'
        }
      });

      if (res.success) {
        if (!task) {
          this.autoExecJobLogList = res.data;
          this.showAutoExecJobLogModal = true;
        } else {
          this.autoExecTaskLogList = res.data;
          this.showAutoExecTaskLogModal = true;
        }
      }
    },
    async queryAutoExecTaskList() {
      const res = await this.$services.dmTicketQueryAutoExecTaskList({
        data: {
          changeId: this.changeId,
          taskStatus: null,
          page: {
            pageNum: this.page,
            pageSize: this.pageSize
          }
        }
      });

      if (res.success) {
        this.autoExecTaskList = res.data.records;
        this.page = res.data.current;
        this.pageSize = res.data.size;
        this.total = res.data.total;
      }
    },
    async handleStopAutoExecJob() {
      const res = await this.$services.dmTicketStopAutoExecJob({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.handleCloseModal();
        this.$Message.success('暂停成功');
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
    },
    async handleRetryAutoExecJob() {
      const res = await this.$services.dmTicketRetryAutoExecJob({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.handleCloseModal();
        this.$Message.success('重试成功');
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
    },
    async handleEndAutoExecJob() {
      const res = await this.$services.dmTicketEndAutoExecJob({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.handleCloseModal();
        this.$Message.success('终止成功');
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
    },
    async handleSkipAutoExecTask() {
      const res = await this.$services.dmTicketSkipAutoExecTask({
        data: {
          taskId: this.selectedAutoExecTask.taskId,
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.$Message.success('跳过成功');
        this.handleCloseModal();
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
    },
    async handleRefresh() {
      await this.init();
      const curStep = this.changeInfo?.currentStep;
      this.moveToCurrentTab(curStep);
    },
    async handleContinueAutoExecTask() {
      const res = await this.$services.dmTicketContinueAutoExecTask({
        data: {
          taskId: this.selectedAutoExecTask.taskId,
          ticketId: this.ticketId
        }
      });

      if (res.success) {
        this.$Message.success('取消跳过成功');
        this.handleCloseModal();
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
    },
    handleShowEndAutoExecJobModal() {
      this.showEndAutoExecJobModal = true;
    },
    handleShowRetryAutoExecJobModal() {
      this.showRetryAutoExecJobModal = true;
    },
    handleShowStopAutoExecJobModal() {
      this.showStopAutoExecJobModal = true;
    },
    handleShowSkipAutoExecTaskModal(task) {
      this.showSkipAutoExecTaskModal = true;
      this.selectedAutoExecTask = task;
    },
    handleShowContinueAutoExecTaskModal(task) {
      this.showContinueSkipAutoExecTaskModal = true;
      this.selectedAutoExecTask = task;
    },
    handleRefreshTaskList() {
      this.queryAutoExecJobInfo();
      this.queryAutoExecTaskList();
    },
    handleTaskPageChange(page) {
      this.page = page;
      this.queryAutoExecTaskList();
    },
    handleCloseModal() {
      this.showAutoExecJobLogModal = false;
      this.showAutoExecTaskLogModal = false;
      this.showAutoExecTaskSQLModal = false;
      this.showStopAutoExecJobModal = false;
      this.showRetryAutoExecJobModal = false;
      this.showEndAutoExecJobModal = false;
      this.showSkipAutoExecTaskModal = false;
      this.showContinueSkipAutoExecTaskModal = false;
    },
    async skipCheck() {
      const res = await this.$services.dmProjectChangeSkipChecks({
        data: {
          changeId: this.changeId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('tiao-guo-jian-ce-cheng-gong'));
        this.init();
      }
    },
    async retryChange() {
      const res = await this.$services.dmProjectChangeChangeRetry({
        data: {
          changeId: this.changeId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('zhong-shi-bian-geng-cheng-gong'));
        this.init();
      }
    },
    moveToCurrentTab(step = this.changeInfo.currentStep) {
      // if (this.$route.query.tab) {
      //   return;
      // }
      switch (step) {
        case 'INIT':
          this.currentTab = 'sql-change';
          break;
        case 'CHECK':
          this.currentTab = 'sql-audit';
          break;
        case 'APPROVAL':
          this.currentTab = 'approval';
          break;
        case 'EXECUTE':
          this.currentTab = 'execute';
          break;
        default:
          this.currentTab = 'sql-change';
      }
    },
    handleDropdownClick(name) {
      this.subTabLabel = name;
    },
    renderDropdownTab() {
      return h(
        resolveComponent('Dropdown'),
        {
          transfer: true,
          'onOn-click': this.handleDropdownClick
        },
        {
          default: () => [
            h(
              'span',
              {
                class: 'ivu-dropdown-link'
              },
              [
                `${this.$t('sql-bian-geng-nei-rong')}：${this.subTabLabel}`,
                h(resolveComponent('CustomIcon'), {
                  type: 'icon-v2-ArrowDown',
                  size: '13px',
                  leftMargin: '3px'
                })
              ]
            )
          ],
          list: () => [
            h(
              resolveComponent('DropdownItem'),
              {
                name: '结果'
              },
              () => this.$t('bian-geng-jie-guo')
            ),
            h(
              resolveComponent('DropdownItem'),
              {
                name: 'Diff'
              },
              () => this.$t('bian-geng-diff')
            )
          ]
        }
      );
    }
  }
};
</script>

<style lang="less" scoped>
.sub-account {
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

.sql-change-container {
  display: flex;
  flex-direction: column;
  border: 1px solid #ededed;
  position: relative;
  height: 100%;
  overflow-y: hidden;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    margin-bottom: 0 !important;
    border-bottom: 1px solid #ededed;
  }

  .read-only-editor {
    border: none;
  }
}

.progress-bar {
  width: 200px;
}

.section {
  padding: 15px;
}

.item-list {
  list-style: none;
  padding-left: 0;
}

.item-list li {
  padding: 8px 0;
  border-bottom: 1px solid #e8eaec;
  display: flex;
  align-items: center;
}

.item-list li i {
  margin-right: 8px;
  color: #2d8cf0;
}

:deep(.ivu-tabs-bar) {
  margin-bottom: 0;
}

:deep(.ivu-card) {
  margin-bottom: 20px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
}

:deep(.ivu-timeline-item-tail) {
  border-left: 2px solid #e8eaec;
}

.card-wrap {
  position: relative;
  display: flex;
  max-width: 1000px;
  justify-content: space-between;
  align-items: center;
  padding: 5px;

  .left,
  .right {
    display: flex;
  }

  .right {
    margin-right: 30px;
  }

  .right::after {
    margin-right: 30px;
  }

  .delete-icon {
    position: absolute;
    right: 5px;
    bottom: 2px;
  }
}

.left-wrap {
  display: flex;
  flex-direction: column;
  min-width: 700px;
}

.right-wrap {
  height: 120px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  .btns {
    display: flex;
    button {
      margin-right: 10px;
    }
  }
  :deep(.ivu-steps.ivu-steps-small .ivu-steps-title) {
    width: 100px;
  }
}

.title-wrap {
  font-weight: bold;
  display: flex;
  margin-bottom: 20px;
}

.step-wrap {
  border-left: 1px solid;
  padding-left: 60px;
  width: 500px;
}

.approv-wrap {
  display: flex;
  height: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  div {
    margin-bottom: 20px;
  }
}

.content-wrap {
  flex: 1;
  height: calc(100% - 161px);

  .tab-wrap {
    width: 100%;
    // position: fixed;
    z-index: 999;
    background-color: #fff;
  }

  .tab-item {
    height: 100%;
  }

  .tab-item-wrap {
    height: calc(100% - 45px);
    overflow-y: auto;
  }

  :deep(.ivu-tabs-content) {
    height: calc(100% - 45px);
  }
}

.extra-btn {
  margin: 5px 10px 0 0;
}

.exec-wrap {
  display: flex;
  height: 100%;
  width: 100%;

  .exec-left {
    flex: 1;
    height: 100%;
    overflow: hidden;
  }

  .exec-right {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: flex-end;
    flex-direction: column;
  }

  .exec-border {
    padding: 10px;
    height: 100%;
    border-left: 1px solid #ccc;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .right-footer {
    display: flex;
    justify-content: right;
  }
}

.empty-wrap {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.collapse-text-ellipsis {
  display: inline-block;
  max-width: 90%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: middle;
}
.title-text-ellipsis {
  display: inline-block;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: middle;
}

.collapse-btn {
  float: right;
}

.red-text {
  color: #ea3323;
  margin-left: 5px;
}

.gray-text {
  color: #aaa;
  margin-left: 5px;
}

.empty-div {
  display: flex;
  height: 100%;
  justify-content: center;
  align-items: center;
}
</style>
