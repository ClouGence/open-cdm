<template>
  <div class="ticket-detail-container">
    <Breadcrumb>
      <BreadcrumbItem to="/ticket">{{ $t('gong-dan') }}</BreadcrumbItem>
      <BreadcrumbItem>{{ $t('gong-dan-xiang-qing') }}</BreadcrumbItem>
    </Breadcrumb>
    <Card class="ticket-detail-status">
      <template #title>
        <p class="ticket-title-p" style="display: flex; align-items: center">
          <span class="ticket-title">【{{ APPROV_BIZ_MAP[ticketDetail.approBiz] }}】</span>
          <span class="ticket-title">{{ ticketDetail.ticketTitle }}</span>
          <span class="ticket-status-total">
            <span>{{ TICKET_STATUS[ticketDetail.ticketStatus] }}</span>
            <span v-if="ticketDetail.ticketStatus === 'FAILED'">
              <Tooltip :content="ticketDetail.statusMessage" transfer style="margin-left: 3px">
                <div style="display: flex; align-items: center">
                  <span
                    style="
                      max-width: 700px;
                      display: inline-block;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      word-break: break-all;
                      white-space: nowrap;
                    "
                  >
                    ，{{ ticketDetail.statusMessage }}
                  </span>
                  <Icon type="ios-alert-outline" v-if="ticketDetail.ticketStatus === 'FAILED'" />
                </div>
              </Tooltip>
            </span>
          </span>
        </p>
        <div class="ticket-detail-summary">
          <div class="ticket-detail-item">
            <span class="ticket-detail-item-title">{{ $t('shen-qing-ren') }}：</span>
            <span>{{ ticketDetail.userName }}</span>
          </div>
          <div class="ticket-detail-item" v-if="ticketDetail.approBiz === 'DM_QUERY'">
            <span class="ticket-detail-item-title">{{ $t('huan-jing') }}：</span>
            <span>{{ ticketDetail.dsEnvName }}</span>
          </div>
          <div class="ticket-detail-item" v-if="ticketDetail.approBiz === 'DM_QUERY'">
            <span class="ticket-detail-item-title">{{ $t('db-shi-li-ku-ming') }}：</span>
            <CustomIcon
              :type="`icon-v2-${ticketDetail.dataSourceType}`"
              :instanceType="ticketDetail.dsDeployType"
              style="margin-right: 3px"
            ></CustomIcon>
            <span>{{ ticketDetail.targetInfo }}</span>
          </div>
          <div class="ticket-detail-item" v-if="ticketDetail.approTemplateName">
            <div style="display: flex; align-items: center">
              <span class="ticket-detail-item-title">{{ $t('mo-ban-he-liu-cheng') }}：</span>
              <CustomIcon
                :type="`icon-v2-${ticketDetail.approType}`"
                :instanceType="ticketDetail.dsDeployType"
                style="margin-right: 3px"
              ></CustomIcon>
              <span>{{ ticketDetail.approTemplateName }}</span>
            </div>
          </div>
        </div>
        <div class="ticket-detail-operators">
          <Button class="warning-btn" v-if="ticketDetail.canApproval" type="primary" @click="handleShowApprovalModal">
            {{ $t('shen-pi') }}
          </Button>
          <Button type="primary" v-if="ticketDetail.canExecute" @click="handleShowAutoExecuteModal('CONFIRM')">
            {{ $t('zhi-xing') }}
          </Button>
          <Button type="primary" v-if="ticketDetail.pcUrl" @click="handleGoToTheApproval">
            {{ thirdPartyName[ticketDetail.approType] }}
          </Button>
          <Button v-if="ticketDetail.canClose" @click="handleShowCloseTicketModal">
            {{ $t('guan-bi') }}
          </Button>
          <Button @click="getTicketDetail('refresh')" class="refresh-btn" :loading="loading">
            <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
          </Button>
        </div>
      </template>
      <div class="ticket-detail-wrapper" v-if="ticketDetail.ticketProcessVOList">
        <div class="step-item">
          <div class="step-item-item" style="width: 100px">
            <div class="status" style="border: 1px solid #52c41a">
              <Icon type="ios-checkmark-circle" size="20" style="margin-right: 3px" color="#52C41A" />
              <div class="content">{{ $t('yi-ti-jiao') }}</div>
            </div>
            <div>{{ $t('chuang-jian') }}</div>
          </div>
          <div class="step-item-item">
            <div class="step-detail-label">{{ $t('shen-qing-ren') }}:</div>
            <Tooltip transfer>
              <div class="step-detail-value ellipsis">{{ ticketDetail.userName }}</div>
              <template #content>{{ ticketDetail.userName }}</template>
            </Tooltip>
          </div>
          <div class="step-item-item">
            <div class="step-detail-label">{{ $t('shi-jian') }}:</div>
            <div class="step-detail-value">{{ ticketDetail.gmtCreate }}</div>
          </div>
          <div class="step-item-item">
            <div class="step-detail-label" style="min-width: 40px">{{ $t('miao-shu') }}:</div>
            <Tooltip transfer>
              <div class="step-detail-value ellipsis">{{ ticketDetail.description }}</div>
              <template #content>{{ ticketDetail.description }}</template>
            </Tooltip>
          </div>
        </div>
        <div
          :class="`step-item ${currentStep === index ? 'current-step' : ''}`"
          v-for="(process, index) in ticketDetail.ticketProcessVOList"
          :key="process.ticketProcessId"
        >
          <div class="step-item-item" v-if="!process.activityList || process.activityList.length === 0">
            <div class="step-item-item">
              <div class="line" :style="`background: ${process.color}`"></div>
              <div class="status" :style="`border: 1px solid ${process.color};`">
                <Icon :type="process.icon" size="20" style="margin-right: 3px; z-index: 9" :color="process.color" />
                <div class="content">{{ process.label }}</div>
              </div>
              <div>{{ process.ticketStageTitle }}</div>
              <!--              <a style="margin-left: 4px" v-if="process.ticketStage === 'APPROVAL'&&ticketDetail.pcUrl" :href="ticketDetail.pcUrl" target="_blank">-->
              <!--                <Icon type="ios-link" />-->
              <!--              </a>-->
            </div>
            <div class="step-item-item">
              <div v-if="ticketDetail.approBiz === 'DATA_SOURCE_AUTH' && process.ticketStage == 'EXECUTION'" class="step-detail-label">
                {{ $t('zi-dong-zhi-hang') }}
              </div>
              <div v-else class="step-detail-label">{{ $t('chu-li-ren') }}:</div>
              <Tooltip transfer>
                <div class="step-detail-value ellipsis">{{ process.execUserName }}</div>
                <template #content>
                  <div v-for="userName in process.execUserNameList" :key="userName">
                    {{ userName }}
                  </div>
                </template>
              </Tooltip>
            </div>
            <div class="step-item-item">
              <div class="step-detail-label">{{ $t('shi-jian') }}:</div>
              <div class="step-detail-value">{{ process.finishTime }}</div>
            </div>
            <div class="step-item-item">
              <div class="step-detail-label" style="min-width: 40px">{{ $t('zhuang-tai') }}:</div>
              <Tooltip transfer v-if="process.execMsg">
                <div class="step-detail-value ellipsis">
                  {{ TICKET_PROCESS_STATUS[process.ticketProcessStatus] }}
                  <span>({{ process.execMsg }})</span>
                </div>
                <template #content>{{ process.execMsg }}</template>
              </Tooltip>
              <div v-if="!process.execMsg" class="step-detail-value ellipsis">
                {{ TICKET_PROCESS_STATUS[process.ticketProcessStatus] }}
              </div>
            </div>
          </div>
          <div class="step-item-item" v-if="process.activityList && process.activityList.length > 0">
            <div class="step-item-item" style="flex-grow: 1">
              <div class="line" :style="`background: ${process.color}`"></div>
              <div class="status" :style="`border: 1px solid ${process.color};`">
                <Icon :type="process.icon" size="20" style="margin-right: 3px; z-index: 9" :color="process.color" />
                <div class="content">{{ process.label }}</div>
              </div>
              <div>{{ process.ticketStageTitle }}</div>
              <!--              <a style="margin-left: 4px" v-if="process.ticketStage === 'APPROVAL'&&ticketDetail.pcUrl" :href="ticketDetail.pcUrl" target="_blank">-->
              <!--                <Icon type="ios-link" />-->
              <!--              </a>-->
            </div>
            <div class="step-item-item" style="flex-grow: 3">
              <div style="width: 100%">
                <div class="step-item-item" style="margin: 10px 0" v-for="(activity, index) in process.activityList" :key="index">
                  <div class="step-item-item">
                    <div class="step-detail-label">{{ $t('chu-li-ren') }}:</div>
                    <div class="step-detail-value ellipsis">
                      {{ activity.approvalUserList ? activity.approvalUserList.join(',') : $t('zan-wei-huo-qu-dao') }}
                      ({{ activity.activityTitle }})
                    </div>
                  </div>
                  <div class="step-item-item">
                    <div class="step-detail-label">{{ $t('shi-jian') }}:</div>
                    <div class="step-detail-value">{{ activity.finishTime }}</div>
                  </div>
                  <div class="step-item-item">
                    <div class="step-detail-label" style="min-width: 40px">{{ $t('zhuang-tai') }}:</div>
                    <Tooltip transfer>
                      <div class="step-detail-value ellipsis">
                        {{ activityStatus[activity.activityStatus] }}
                        <span v-if="activity.remark">（{{ activity.remark }}）</span>
                      </div>
                      <template #content>
                        {{ activityStatus[activity.activityStatus] }}
                        <span v-if="activity.remark">（{{ activity.remark }}）</span>
                      </template>
                    </Tooltip>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Card>
    <Card class="ticket-content" v-if="this.ticketType === 'DM_QUERY' && autoExec">
      <template #title>
        <div style="display: flex; align-items: center; width: 100%; justify-content: space-between">
          <div class="left" style="display: flex; align-items: center">
            <div style="margin-right: 10px">{{ $t('ren-wu-zhi-hang') }}</div>
            <Poptip :content="autoExecJobInfo.message" trigger="hover" style="margin-right: 10px" v-if="!autoExecJobInfo.normal">
              <Icon type="ios-alert-outline" />
            </Poptip>
            <Tag :color="AUTO_EXEC_JOB_STATUS_COLOR[autoExecJobInfo.status]" style="margin-right: 10px">
              {{ AUTO_EXEC_JOB_STATUS_I18N[autoExecJobInfo.status] }}
            </Tag>
            <div v-if="autoExecJobInfo.execTime" style="margin-right: 10px">{{ $t('ji-hua-zhi-hang-shi-jian') }} {{ autoExecJobInfo.execTime }}</div>
            <div v-if="autoExecJobInfo.workerIp" style="margin-right: 10px">{{ $t('ji-qi-ip-0') }} {{ autoExecJobInfo.workerIp }}</div>
            <div v-if="autoExecJobInfo.workerStatus" style="margin-right: 10px">
              {{ $t('ji-qi-zhuang-tai-0') }} {{ autoExecJobInfo.workerStatus }}
            </div>
          </div>
          <div class="right" style="display: flex; align-items: center">
            <!--          <div v-if="autoExecJobInfo.lastReportTime">-->
            <!--            {{ $t('zui-hou-yi-ci-shang-bao-shi-jian') }} {{autoExecJobInfo.lastReportTime }}-->
            <!--          </div>-->
            <Button type="text" size="small" v-if="autoExecJobInfo.canEnd" @click="handleShowEndAutoExecJobModal">
              {{ $t('zhong-zhi') }}
            </Button>
            <Button type="text" size="small" v-if="autoExecJobInfo.canPause" @click="handleShowStopAutoExecJobModal">
              {{ $t('zan-ting') }}
            </Button>
            <Button type="text" size="small" v-if="autoExecJobInfo.canRestart" @click="handleShowRetryAutoExecJobModal">
              {{ $t('hui-fu') }}
            </Button>
            <Button type="text" size="small" v-if="autoExecJobInfo.canRetry" @click="handleShowRetryAutoExecJobModal">
              {{ $t('zhong-shi') }}
            </Button>
            <Button type="text" size="small" @click="handleAutoExecLog(null)">
              {{ $t('tiao-du-ri-zhi') }}
            </Button>
            <Button type="text" size="small" @click="handleRefreshTaskList">
              {{ $t('shua-xin') }}
            </Button>
          </div>
        </div>
      </template>

      <Table :columns="autoExecTaskColumns" :data="autoExecTaskList" border size="small">
        <template #status="{ row }">
          <Tag :color="AUTO_EXEC_TASK_STATUS_COLOR[row.status]">
            {{ AUTO_EXEC_TASK_STATUS_I18N[row.status] }}
          </Tag>
        </template>
        <template #sql="{ row }">
          <Poptip :content="row.execSql" trigger="hover" transfer>
            <span style="width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap">
              {{ row.execSql }}
            </span>
          </Poptip>
        </template>
        <template #action="{ row }">
          <!--          <Button type="text" size="small" @click="handleAutoExecSQL(row)">{{ $t('cha-kan-sql') }}</Button>-->
          <Button type="text" size="small" @click="handleAutoExecLog(row)">
            {{ $t('ri-zhi') }}
          </Button>
          <Button type="text" size="small" @click="handleShowSkipAutoExecTaskModal(row)" v-if="row.canSkip">
            {{ $t('tiao-guo') }}
          </Button>
          <Button type="text" size="small" @click="handleShowContinueAutoExecTaskModal(row)" v-if="row.canCancelSkip">
            {{ $t('qu-xiao-tiao-guo') }}
          </Button>
        </template>
      </Table>
      <div style="width: 100%; text-align: right">
        <Page v-model="page" :page-size="pageSize" :total="total" @on-change="handleTaskPageChange" size="small" style="margin-top: 10px" />
      </div>
    </Card>

    <!-- 规则校验结果 -->
    <Card class="ticket-content" v-if="(this.ticketType === 'DM_QUERY' || this.ticketType === 'DM_CHANGE') && showValidationResult">
      <template #title>
        <div>
          <span>{{ $t('gui-ze-xiao-yan-jie-guo') }}</span>
        </div>
      </template>
      <template #extra>
        <div>
          <Checkbox v-model="showCheckedOnlyError">{{ $t('jin-xian-shi-yan-zhong') }}</Checkbox>
        </div>
      </template>
      <div class="validation-content" style="padding: 16px">
        <div v-for="(rule, index) in checkRoleResultList()" :key="index" class="rule-item">
          <div class="rule-header">
            <Tag :color="rule.ruleLevel === 'SUGGEST' ? 'warning' : 'error'" class="rule-level">
              {{ RULE_WARN_LEVEL[rule.ruleLevel] }}
            </Tag>
            <span class="rule-name">{{ rule.name }}</span>
            <div v-if="rule.lines && rule.lines.length" class="rule-lines">
              <span class="lines-label">{{ $t('wei-zhi-0') }}:</span>
              <span v-for="line in rule.lines" :key="line" class="lines-content">{{ line }}</span>
            </div>
          </div>
          <div class="rule-desc">{{ rule.desc }}</div>
        </div>
      </div>
    </Card>

    <Card class="ticket-content" v-if="this.ticketType === 'DM_QUERY' || this.ticketType === 'DM_CHANGE'" :padding="0">
      <template #title>
        <div style="display: flex; align-items: center">
          <div>{{ $t('gong-dan-nei-rong') }}</div>
          <Button type="link" @click="handleShowRollbackSqlModal" v-if="ticketDetail.rollBackSql" style="margin-left: 10px">
            {{ $t('cha-kan-hui-gun-sql') }}
          </Button>
          <span v-if="ticketDetail.ticketMessage" class="parse-error-msgContent">*{{ ticketDetail.ticketMessage }}</span>
        </div>
      </template>
      <read-only-editor :text="ticketDetail.rawSql" key="raw" :border="0" :ds-type="ticketDetail.dataSourceType" />
    </Card>
    <Card class="ticket-content" v-if="ticketType === 'DATA_SOURCE_AUTH'">
      <template #title>
        <div style="display: flex; align-items: center; justify-content: space-between">
          <div style="display: flex; align-items: center">
            <div>{{ $t('gong-dan-nei-rong') }}</div>
          </div>
        </div>
      </template>
      <div>
        <Table :columns="columns" :data="formattedAuths" border>
          <template #time="{ row }">{{ row?.startTime }} - {{ row?.endTime }}</template>
          <template #authLabels="{ row }">
            <div style="padding: 5px">
              <Tag v-for="(label, index) in row.authLabels" :key="index">{{ label }}</Tag>
            </div>
          </template>
        </Table>
      </div>
    </Card>
    <CCModal v-model="showApprovalModal" :title="$t('shen-pi')" :closable="false">
      <Form>
        <FormItem :label="$t('yi-jian')">
          <RadioGroup v-model="approvalData.rejected">
            <Radio label="false">{{ $t('tong-yi') }}</Radio>
            <Radio label="true">{{ $t('ju-jue') }}</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem :label="$t('li-you')">
          <Input type="textarea" v-model="approvalData.comment"></Input>
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="handleApproval" type="primary">{{ $t('ti-jiao') }}</Button>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showCancelTicketModal" :title="$t('che-xiao-gong-dan-que-ren')">
      <p>{{ $t('gong-dan-che-xiao-hou-bu-ke-hui-fu-que-ren-yao-che-xiao-gai-gong-dan-ma') }}</p>
      <template #footer>
        <Button type="primary" @click="cancelTicket">{{ $t('que-ding') }}</Button>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showRollbackSqlModal" :title="$t('cha-kan-hui-gun-sql')" :width="1000">
      <read-only-editor :text="ticketDetail.rollBackSql" key="rollback" :max-height="500" :ds-type="ticketDetail.dataSourceType" />
      <template #footer>
        <Button type="primary" @click="copyText(ticketDetail.rollBackSql)">
          {{ $t('fu-zhi-sql') }}
        </Button>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
    <CCModal :title="$t('ti-shi')" v-model="showCloseTicketModal" @on-ok="closeTicket" @on-cancel="handleCloseModal">
      {{ $t('que-ding-yao-guan-bi-gong-dan-ma') }}
      <template #footer>
        <Button type="primary" @click="closeTicket">{{ $t('que-ding') }}</Button>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
      </template>
    </CCModal>
    <CCModal :title="$t('gong-dan-zhi-xing')" v-model="showAutoExecuteModal" width="800px">
      <Form :model="confirmInfo.autoExecConfig" :label-width="80" style="border: 1px">
        <FormItem style="margin-bottom: 0" :label="$t('zhi-hang-ce-lve')" prop="autoExecType">
          <RadioGroup v-model="confirmInfo.autoExecConfig.autoExecType">
            <Radio label="MANUAL_EXEC">{{ $t('yi-shou-dong-wan-cheng') }}</Radio>
            <Radio label="IMMEDIATE">{{ $t('li-ji-zhi-xing') }}</Radio>
            <Radio label="SPECIFY_TIME">{{ $t('ding-shi-zhi-xing') }}</Radio>
          </RadioGroup>
          <DatePicker
            v-if="confirmInfo.autoExecConfig.autoExecType === 'SPECIFY_TIME'"
            v-model="confirmInfo.autoExecConfig.execTime"
            size="small"
            type="datetime"
            :placeholder="$t('qing-xuan-ze-zhi-hang-shi-jian')"
          />
        </FormItem>
        <FormItem
          style="margin-bottom: 0"
          :label="$t('shi-wu')"
          prop="enableTransactional"
          v-if="!isCk(ticketDetail.dataSourceType) && !isMongoDB(ticketDetail.dataSourceType)"
        >
          <i-switch
            v-model="confirmInfo.autoExecConfig.enableTransactional"
            size="large"
            :disabled="confirmInfo.autoExecConfig.autoExecType === 'MANUAL_EXEC'"
          >
            <template #open>{{ $t('kai-qi-0') }}</template>
            <template #close>{{ $t('wu-0') }}</template>
          </i-switch>
          {{ $t('ru-guo-sql-yu-ju-zhong-cun-zai-fei-dml-yu-ju-ke-neng-hui-bei-fen-wei-duo-ge-shi-wu-zhi-hang') }}
        </FormItem>
        <FormItem style="margin-bottom: 0" :label="$t('bei-zhu')">
          <Input v-model="confirmInfo.comment" type="textarea" />
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary" @click="handleFinishTicket" v-if="confirmInfo.autoExecConfig.autoExecType === 'MANUAL_EXEC'">
          {{ $t('jie-shu-gong-dan') }}
        </Button>
        <Button type="primary" @click="handleConfirmTicket" v-else>
          {{ confirmInfo.autoExecConfig.autoExecType == 'IMMEDIATE' ? $t('li-ji-zhi-hang') : $t('ding-shi-zhi-hang') }}
        </Button>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showAutoExecJobLogModal" :title="$t('ri-zhi')" @ok="handleCloseModal" :width="800">
      <Table :columns="autoExecJobLogColumns" :data="autoExecJobLogList" border size="small" />
    </CCModal>
    <CCModal v-model="showAutoExecTaskLogModal" :title="$t('ri-zhi')" @ok="handleCloseModal" :width="800">
      <Table :columns="autoExecJobLogColumns" :data="autoExecTaskLogList" border size="small" />
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
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { TICKET_STATUS, TICKET_STATUS_COLOR, TICKET_PROCESS_STATUS } from '@/const';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import copyMixin from '@/mixins/copyMixin';
import { RULE_WARN_LEVEL, isCk, isMongoDB } from '@/utils';
import { APPROV_BIZ_MAP } from './constant';

const AUTO_EXEC_JOB_STATUS_I18N = {
  INIT: '待执行',
  WAIT_EXEC: '待执行',
  EXECUTING: '执行中',
  FAILED: '失败',
  PAUSE: '暂停',
  PAUSING: '暂停中',
  FINISH: '已完成',
  TERMINATION: '终止'
};

const AUTO_EXEC_JOB_STATUS_COLOR = {
  INIT: 'default',
  WAIT_EXEC: 'default',
  EXECUTING: 'default',
  FAILED: 'error',
  PAUSE: 'default',
  PAUSING: 'default',
  FINISH: 'success',
  TERMINATION: 'error'
};

const AUTO_EXEC_TASK_STATUS_I18N = {
  WAIT_EXEC: '待执行',
  EXECUTING: '执行中',
  WAIT_CONFIRM: '等待确认',
  FAILED: '失败',
  FINISH: '完成',
  ROLLBACK: '回滚',
  CANCELED: '取消'
};

const AUTO_EXEC_TASK_STATUS_COLOR = {
  WAIT_EXEC: 'default',
  EXECUTING: 'default',
  WAIT_CONFIRM: 'default',
  FAILED: 'error',
  FINISH: 'success',
  ROLLBACK: 'default',
  CANCELED: 'default'
};

export default {
  name: 'TicketDetail',
  components: {
    ReadOnlyEditor
  },
  mixins: [copyMixin],
  data() {
    return {
      autoExec: false,
      RULE_WARN_LEVEL,
      noPassedRuleList: [],
      showCheckedOnlyError: false,
      showContinueSkipAutoExecTaskModal: false,
      showSkipAutoExecTaskModal: false,
      showStopAutoExecJobModal: false,
      showEndAutoExecJobModal: false,
      showRetryAutoExecJobModal: false,
      showAutoExecTaskSQLModal: false,
      showAutoExecJobLogModal: false,
      showAutoExecTaskLogModal: false,
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
      autoExecJobLogList: [],
      autoExecTaskLogList: [],
      selectedAutoExecTask: {},
      autoExecTaskColumns: [],
      autoExecTaskColumnsWithTrans: [
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
        // {
        //   title: '影响行数',
        //   key: 'affectLine',
        //   width: 100
        // },
        // {
        //   title: '事务编号',
        //   key: 'transactionGroup',
        //   width: 100
        // },
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
      autoExecTaskColumnsWithoutTrans: [
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
        // {
        //   title: '影响行数',
        //   key: 'affectLine',
        //   width: 100
        // },
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
      AUTO_EXEC_JOB_STATUS_I18N,
      AUTO_EXEC_JOB_STATUS_COLOR,
      AUTO_EXEC_TASK_STATUS_I18N,
      AUTO_EXEC_TASK_STATUS_COLOR,
      APPROV_BIZ_MAP,
      autoExecJobInfo: {},
      autoExecTaskList: [],
      page: 1,
      pageSize: 10,
      total: 0,
      showCloseTicketModal: false,
      activeSqlTab: 'raw',
      showAutoExecuteModal: false,
      showManualExecuteModal: false,
      showRollbackSqlModal: false,
      showApprovalModal: false,
      approvalData: {
        rejected: 'false',
        comment: ''
      },
      taskList: [],
      startId: 0,
      exportJobList: [],
      preStartIds: [],
      ticketId: 0,
      ticketDetail: {},
      TICKET_STATUS,
      TICKET_STATUS_COLOR,
      TICKET_PROCESS_STATUS,
      loading: false,
      confirmInfo: {
        autoExecConfig: {}
      },
      autoExecuteRule: {},
      showCancelTicketModal: false,
      currentStep: 0,
      activityStatus: {
        NEW: this.$t('chu-shi-hua'),
        RUNNING: this.$t('deng-dai-shen-pi'),
        CANCELED: this.$t('yi-qu-xiao'),
        COMPLETED: this.$t('yi-tong-guo'),
        REFUSE: this.$t('yi-ju-jue')
      },
      thirdPartyName: {
        DingTalk: this.$t('ding-ding-shen-pi'),
        Feishu: this.$t('fei-shu-shen-pi'),
        Wechat: this.$t('wei-xin-shen-pi')
      },
      ticketType: '',
      authList: [],
      columns: [
        {
          title: this.$t('shu-ju-yuan-shi-li'),
          key: 'resInstId'
        },
        {
          title: this.$t('zi-yuan-lu-jing'),
          key: 'resPaths'
        },
        {
          title: this.$t('sheng-xiao-shi-jian'),
          key: 'time',
          render: (h, { row }) => {
            let time = '';
            if (!row.startTime && !row.endTime) {
              time = this.$t('yong-jiu');
            } else if (row.startTime && row.endTime) {
              time = `${row.startTime} - ${row.endTime}`;
            } else if (row.startTime) {
              time = `${this.$t('cong-0')} ${row.startTime} ${this.$t('kai-shi-zhi-yong-jiu')}`;
            } else {
              time = `${this.$t('cong-shen-pi-tong-guo-dao')} ${row.endTime} ${this.$t('jie-shu')}`;
            }
            return h('span', time);
          }
        },
        {
          title: this.$t('quan-xian-lie-biao'),
          slot: 'authLabels'
        }
      ]
    };
  },
  async mounted() {
    this.ticketId = this.$route.params.id;
    await this.getTicketDetail('init');
  },
  computed: {
    ...mapState(['userInfo', 'myAuth']),
    formattedAuths() {
      return this.authList.map((authItem) => ({
        resId: authItem.resId,
        resInstId: authItem.resInstId,
        resPaths: `/${authItem.resPaths.join(' / ')}`,
        authLabels: authItem.authLabels,
        startTime: authItem.startTime,
        endTime: authItem.endTime
      }));
    },
    showValidationResult() {
      return this.noPassedRuleList && this.noPassedRuleList.length > 0;
    },
    hasError() {
      return this.noPassedRuleList.some((rule) => rule.ruleLevel !== 'SUGGEST');
    }
  },
  methods: {
    isCk,
    isMongoDB,
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
    async handleEndAutoExecJob() {
      const res = await this.$services.dmTicketEndAutoExecJob({
        data: {
          ticketId: this.ticketId
        }
      });

      if (res.success) {
        this.$Message.success('终止成功');
        await this.getTicketDetail();
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
    },
    async handleRetryAutoExecJob() {
      const res = await this.$services.dmTicketRetryAutoExecJob({
        data: {
          ticketId: this.ticketId
        }
      });

      if (res.success) {
        this.$Message.success('重试成功');
        await this.getTicketDetail();
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
      this.handleCloseModal();
    },
    async handleStopAutoExecJob() {
      const res = await this.$services.dmTicketStopAutoExecJob({
        data: {
          ticketId: this.ticketId
        }
      });

      if (res.success) {
        this.$Message.success('暂停成功');
        await this.getTicketDetail();
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
      this.handleCloseModal();
    },
    async handleSkipAutoExecTask() {
      const res = await this.$services.dmTicketSkipAutoExecTask({
        data: {
          taskId: this.selectedAutoExecTask.taskId,
          ticketId: this.ticketId
        }
      });

      if (res.success) {
        this.$Message.success('跳过成功');
        await this.getTicketDetail();
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
      this.handleCloseModal();
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
        await this.getTicketDetail();
        await this.queryAutoExecJobInfo();
        await this.queryAutoExecTaskList();
      }
    },
    handleTaskPageChange(page) {
      this.page = page;
      this.queryAutoExecTaskList();
    },
    handleAutoExecSQL(task) {
      this.selectedAutoExecTask = task;
      this.showAutoExecTaskSQLModal = true;
    },
    handleRefreshTaskList() {
      this.queryAutoExecJobInfo();
      this.queryAutoExecTaskList();
      this.queryAutoExecJobInfo();
    },
    async handleAutoExecLog(task = null) {
      const res = await this.$services.dmTicketAutoExecLog({
        data: {
          taskId: task ? task.taskId : null,
          jobId: this.autoExecJobInfo.id,
          dependBizType: task ? 'AUTO_EXEC_TASK' : 'AUTO_EXEC_JOB'
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
    async queryAutoExecJobInfo() {
      const res = await this.$services.dmTicketQueryAutoExecJobInfo({
        data: {
          ticketId: this.ticketId
        }
      });

      if (res.success) {
        this.autoExecJobInfo = res.data;
        this.autoExecTaskColumns = res.data.enableTransactional ? this.autoExecTaskColumnsWithTrans : this.autoExecTaskColumnsWithoutTrans;
      }
    },
    async queryAutoExecTaskList() {
      const res = await this.$services.dmTicketQueryAutoExecTaskList({
        data: {
          ticketId: this.ticketId,
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
    handleShowManualExecuteModal(type) {
      this.confirmInfo = {
        ticketId: this.ticketId,
        confirmActionType: type,
        confirmUid: this.userInfo.uid,
        conformerUid: this.userInfo.uid,
        comment: '',
        ddlSqlExecType: 'DIRECT',
        noneDdlSqlExecType: 'DIRECT'
      };
      this.showManualExecuteModal = true;
    },
    handleShowAutoExecuteModal(type) {
      this.confirmInfo = {
        ticketId: this.ticketId,
        confirmActionType: type,
        comment: '',
        ddlSqlExecType: 'DIRECT',
        noneDdlSqlExecType: 'DIRECT',
        autoExecConfig: {
          enableTransactional: false,
          errorStrategy: 'NONE',
          retryWaitTime: 111, // 单位秒
          retryCount: 2, // 重试次数
          autoExecType: 'IMMEDIATE', // [IMMEDIATE,SPECIFY_TIME]  立即执行，指定时间
          execTime: new Date() // 预定执行时间
        }
      };
      this.showAutoExecuteModal = true;
    },
    handleShowRollbackSqlModal() {
      this.showRollbackSqlModal = true;
    },
    handleShowCloseTicketModal() {
      this.showCloseTicketModal = true;
    },
    async getTicketDetail(type) {
      this.loading = true;
      const data = {
        ticketId: this.ticketId,
        refreshCache: type === 'refresh'
      };
      if (type === 'init') {
        this.currentStep = 0;
      }
      let theCurrentStep = 0;
      const res = await this.$services.rdpTicketQueryTicketBaseInfo({ data });
      this.ticketType = res.data?.approBiz;

      this.loading = false;
      if (res.success) {
        this.ticketDetail = res.data;
        this.ticketDetail.ticketProcessVOList.forEach((item, index) => {
          item.execUserName = '';
          item.execMsg = '';
          if (item.stageContext) {
            const stageContext = JSON.parse(item.stageContext);
            item.execUserName = stageContext.execUserName ? stageContext.execUserName.join(',') : '';
            item.execUserNameList = stageContext.execUserName;
            item.execMsg = stageContext.execMsg;
          }
          if (item.finishTime) {
            if (type === 'init') {
              this.currentStep++;
            }
            theCurrentStep++;
          }

          if (item.ticketProcessStatus === 'FINISH') {
            item.label = this.$t('yi-wan-cheng');
            item.labelColor = '#52C41A';
            item.icon = 'ios-checkmark-circle';
            item.color = '#52C41A';
          } else if (item.ticketProcessStatus === 'INIT') {
            item.label = this.$t('wei-kai-shi');
            item.labelColor = '#ccc';
            item.icon = 'ios-time';
            item.color = '#ccc';
            if (this.currentStep === index) {
              item.icon = 'ios-loading';
              item.color = '#0087c7';
              item.label = this.$t('jin-hang-zhong');
              item.labelColor = '#0087c7';
            }
          } else if (item.ticketProcessStatus === 'CLOSED') {
            item.label = this.$t('yi-guan-bi');
            item.labelColor = 'red';
            item.icon = 'ios-close-circle';
            item.color = 'red';
          } else if (item.ticketProcessStatus === 'REJECT') {
            console.log('reject');
            this.currentStep = -1;
            item.label = this.$t('yi-ju-jue');
            item.labelColor = 'red';
            item.icon = 'ios-remove-circle';
            item.color = 'red';
          } else if (item.ticketProcessStatus === 'FAIL') {
            item.label = this.$t('yi-shi-bai');
            item.labelColor = 'red';
            item.icon = 'ios-remove-circle';
            item.color = 'red';
          } else if (item.ticketProcessStatus === 'PAUSE') {
            item.label = this.$t('yi-zan-ting');
            item.labelColor = '#ccc';
            item.icon = 'ios-remove-circle';
            item.color = '#ccc';
          }
        });
        this.currentStep = theCurrentStep;

        switch (res.data?.approBiz) {
          case 'DATA_SOURCE_AUTH':
            const resAuth = await this.$services.rdpTicketQueryDataSourceAuthTicketDetail({ data: { ticketId: this.ticketId } });
            if (resAuth.success) {
              this.ticketDetail.applyAuths = resAuth.data.applyAuths;
              this.authList = resAuth.data.applyAuths;
            }
            break;
          case 'DM_QUERY':
          case 'DM_CHANGE':
            const resQuery = await this.$services.dmTicketQueryQueryTicketDetail({ data: { ticketId: this.ticketId } });
            if (resQuery.success) {
              this.autoExec = resQuery.data.autoExec;
              if (resQuery.data?.autoExec) {
                await this.queryAutoExecJobInfo();
                await this.queryAutoExecTaskList();
              }
              this.ticketDetail.rollBackSql = resQuery.data?.rollBackSql || '';
              this.ticketDetail.ticketMessage = resQuery.data?.ticketMessage || '';
              this.ticketDetail.rawSql = resQuery.data?.rawSql || '';
              this.noPassedRuleList = resQuery.data.checkedList || [];
              this.noPassedRuleList = resQuery.data.checkedList || [];
            }
            break;
          default:
            break;
        }
        setTimeout(() => {
          let thirdParent = null;
          document.querySelectorAll('.step-item').forEach((item, index) => {
            const line = item.querySelector('.line');
            if (index === 2) {
              thirdParent = item;
            }
            if (line) {
              const parentHeight = item.offsetHeight;
              if (index === 3) {
                line.style.height = `${(thirdParent.offsetHeight - 22) / 2 + 7}px`;
              } else {
                line.style.height = `${(parentHeight - 22) / 2 + 7}px`;
              }
            }
          });
        }, 200);
      }
    },
    async cancelTicket() {
      const data = {
        ticketId: this.ticketId,
        approvalType: this.ticketDetail.approType,
        approIdentity: this.ticketDetail.approIdentity
      };
      const res = await this.$services.dmTicketCancel({ data });
      if (res.success) {
        this.$Message.success(this.$t('che-xiao-cheng-gong'));
        this.showCancelTicketModal = false;
        await this.getTicketDetail();
      }
    },
    async handleConfirmTicket() {
      console.log(this.confirmInfo.confirmActionType);
      const data = { ...this.confirmInfo };
      if (this.confirmInfo.confirmActionType === 'CONFIRM') {
        data.autoExecConfig.execTime = Date.parse(data.autoExecConfig.execTime);
      }
      const res = await this.$services.dmTicketConfirm({ data });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.handleCloseModal();
        await this.getTicketDetail();
      }
    },
    async handleFinishTicket() {
      this.confirmInfo.confirmActionType = 'CONFIRM';
      const data = { ...this.confirmInfo };
      data.autoExecConfig.execTime = null;
      const res = await this.$services.dmTicketConfirm({ data });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.handleCloseModal();
        await this.getTicketDetail();
      }
    },

    handleShowApprovalModal() {
      this.showApprovalModal = true;
    },
    async handleApproval() {
      const { rejected, comment } = this.approvalData;
      const res = await this.$services.rdpTicketApproval({
        data: {
          ticketId: this.ticketId,
          comment,
          rejected: rejected === 'true'
        }
      });

      if (res.success) {
        this.$Message.success(this.$t('shen-pi-cheng-gong'));
        this.handleCloseModal();
        await this.getTicketDetail();
      }
    },
    handleCloseModal() {
      this.approvalData = {
        rejected: 'false',
        comment: ''
      };
      this.showApprovalModal = false;
      this.showCancelTicketModal = false;
      this.showRollbackSqlModal = false;
      this.showManualExecuteModal = false;
      this.showCloseTicketModal = false;
      this.showAutoExecuteModal = false;
      this.showAutoExecJobLogModal = false;
      this.showAutoExecTaskLogModal = false;
      this.showAutoExecTaskSQLModal = false;
      this.showStopAutoExecJobModal = false;
      this.showRetryAutoExecJobModal = false;
      this.showEndAutoExecJobModal = false;
      this.showSkipAutoExecTaskModal = false;
      this.showContinueSkipAutoExecTaskModal = false;
    },
    async closeTicket() {
      const data = {
        ticketId: this.ticketId
      };
      const res = await this.$services.rdpTicketClose({ data });
      if (res.success) {
        this.$Message.success(this.$t('guan-bi-cheng-gong'));
        await this.getTicketDetail();
      }
      this.handleCloseModal();
    },
    handleGoToTheApproval() {
      window.open(this.ticketDetail.pcUrl);
    },
    checkRoleResultList() {
      if (!this.showCheckedOnlyError) {
        return this.noPassedRuleList;
      } else {
        return this.noPassedRuleList.filter((rule) => rule.ruleLevel !== 'SUGGEST');
      }
    }
  }
};
</script>

<style lang="less" scoped>
.horizontal-align {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ticket-detail-container {
  padding: 20px;
  position: relative;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .ticket-detail-status {
    margin-top: 16px;

    :deep(.ant-card-body) {
      padding: 12px !important;
    }
  }

  .ticket-title-p {
    line-height: 20px;
    margin-bottom: 12px;
  }

  .ivu-card-head p,
  .ivu-card-head-inner {
    overflow: visible;
  }

  .ticket-title {
    font-size: 14px;
    font-family: PingFangSC-Semibold;
    font-weight: 500;
  }

  .ticket-status-total {
    display: flex;
    align-items: center;
    border: 1px solid #f8d090;
    background: #fff8ec;
    border-radius: 10px;
    color: #ffa30e;
    font-size: 12px;
    padding: 2px 8px;
    margin-left: 8px;
    margin-right: 5px;
  }

  .ticket-detail-summary {
    font-size: 12px;
    font-family: PingFangSC-Regular;
    font-weight: 400;
    padding-right: 200px;

    .ticket-detail-item {
      margin-top: 6px;
      margin-right: 80px;
      color: @font-color;
      display: inline-block;

      .ticket-detail-item-title {
        color: @icon-color;
      }
    }
  }

  .ticket-detail-operators {
    position: absolute;
    right: 14px;
    top: 10px;
    display: flex;

    button {
      margin-left: 10px;
    }
  }

  .ticket-content {
    margin-top: 20px;

    :deep(.ivu-card-body) {
      padding: 0 0;
    }
    :deep(.ivu-table-wrapper-with-border) {
      border: 0;
    }
  }

  .ticket-detail-wrapper {
    position: relative;
    color: @font-color;

    .step-item {
      padding: 7px;
      display: flex;
      align-items: center;
      width: 100%;

      &.current-step {
        box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;
        border-radius: 5px;
        cursor: pointer;
      }

      .step-item-item {
        position: relative;
        width: 100%;
        display: flex;
        align-items: center;
        flex: 1;

        .step-detail-label {
          min-width: 40px;
        }

        .step-detail-value,
        .content {
          display: inline-block;
          vertical-align: middle;
        }

        .line {
          //height: 20px;
          width: 2px;
          background: red;
          position: absolute;
          left: 9px;
          bottom: 22px;
        }

        .status {
          display: flex;
          align-items: center;
          border-radius: 12px;
          padding-right: 4px;
          font-weight: bold;
          margin-right: 5px;
        }
      }

      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .ticket-status {
    display: flex;
    align-items: center;
    margin-left: 5px;

    .content {
      padding: 2px 5px;
      border-radius: 2px;
      color: #fff;
      font-weight: bold;
    }
  }
}

.ellipsis {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
}

.parse-error-msgContent {
  margin-left: 20px;
  color: red;
}

.validation-content {
  .rule-item {
    background: white;
    border: 1px solid #f0f0f0;
    border-radius: 6px;
    padding: 12px;
    padding-bottom: 6px;
    margin-bottom: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

    &:last-child {
      margin-bottom: 0;
    }

    .rule-header {
      display: flex;
      align-items: center;
      margin-bottom: 5px;

      .rule-level {
        margin-right: 8px;
        font-weight: 500;
      }

      .rule-name {
        font-weight: 600;
        color: #262626;
      }

      .rule-lines {
        display: flex;
        align-items: center;
        font-size: 12px;
        padding-left: 10px;

        .lines-label {
          color: #8c8c8c;
          margin-right: 4px;
        }

        .lines-content {
          color: #595959;
          background: #f5f5f5;
          padding: 2px 6px;
          margin-right: 5px;
          border-radius: 3px;
          font-family: monospace;
        }
      }
    }

    .rule-desc {
      color: #595959;
      line-height: 1.5;
      margin-bottom: 5px;
    }
  }
}
</style>
