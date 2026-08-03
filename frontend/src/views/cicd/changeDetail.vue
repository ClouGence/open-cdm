<template>
  <div class="change-detail-page">
    <div class="change-detail-shell">
      <div class="change-detail-hero">
        <div class="left-wrap">
          <div class="title-wrap">
            <Tooltip :content="changeInfo?.changeName">
              <span class="title-text-ellipsis">{{ changeInfo?.changeName || '-' }}</span>
            </Tooltip>
            <span class="change-status-pill" :class="changeStatusClass">{{ changeStatusLabel }}</span>
            <span class="change-step-pill" v-if="showChangeStepPill">{{ changeStepLabel }}</span>
            <Tooltip :content="changeInfo.remark" style="width: 450px" v-if="changeInfo.remark">
              <span class="collapse-text-ellipsis" :class="changeInfo.currentStatus === 'FAILED' ? 'red-text' : 'gray-text'" v-if="changeInfo.remark">
                {{ '(' + changeInfo.remark + ')' }}
              </span>
            </Tooltip>
          </div>
          <div class="release-grid change-detail-pipeline">
            <div class="release-panel">
              <div class="panel-subheading">
                <CustomIcon
                  :resource="getScmIconResource(changeInfo?.scmType)"
                  :type="changeInfo?.scmType"
                  :alt="getScmDisplayName(changeInfo?.scmType)"
                  size="24px"
                  rightMargin="8px"
                />
                <span>{{ $t('cang-ku') }}</span>
              </div>
              <div class="endpoint-summary-lines">
                <div class="endpoint-summary-line">
                  <span>{{ $t('cang-ku') }}：</span>
                  <strong>{{ changeInfo?.repoName || '-' }}</strong>
                </div>
                <div class="endpoint-summary-line">
                  <span>{{ $t('fen-zhi') }}：</span>
                  <strong>{{ changeInfo?.repoBranch || '-' }}</strong>
                </div>
                <div class="endpoint-summary-line">
                  <span>{{ $t('lu-jing') }}：</span>
                  <strong>{{ changeInfo?.repoScriptPath || '-' }}</strong>
                </div>
              </div>
            </div>
            <div class="link-divider" aria-hidden="true">
              <span>
                <svg class="flow-link-arrows" viewBox="0 0 28 28">
                  <path d="M7 14h14"></path>
                  <path d="m16.8 9.8 4.2 4.2-4.2 4.2"></path>
                </svg>
              </span>
            </div>
            <div class="release-panel">
              <div class="panel-subheading">
                <CustomIcon :type="changeInfo?.dsType" size="24px" rightMargin="8px" />
                <span>{{ $t('shu-ju-ku') }}</span>
              </div>
              <div class="endpoint-summary-lines">
                <div class="endpoint-summary-line">
                  <span>{{ $t('shi-li-0') }}：</span>
                  <strong>{{ changeInfo?.dsInstance || '-' }}</strong>
                </div>
                <div class="endpoint-summary-line">
                  <span>{{ $t('miao-shu') }}：</span>
                  <strong>{{ changeInfo?.dsDesc || '-' }}</strong>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="right-wrap">
          <div class="btns">
            <Button
              class="detail-action-btn primary-action"
              @click="skipCheck"
              type="primary"
              v-if="!(isBtnOnlyRead || canJumpCheck || changeInfo.currentStep !== 'CHECK')"
            >
              {{ isErrorCheck ? $t('tiao-guo') : $t('tiao-guo-jian-ce') }}
            </Button>
            <Button
              class="detail-action-btn"
              @click="retryChange"
              v-if="changeInfo.currentStep !== 'APPROVAL' && !(cantRetry || isBtnOnlyRead || isReadyStatus)"
            >
              {{ $t('zhong-shi-bian-geng') }}
            </Button>
            <Button
              class="detail-action-btn"
              @click="retryChange"
              v-if="changeInfo.currentStep === 'APPROVAL' && !(cantRetry || isBtnOnlyRead || isReadyStatus)"
            >
              {{ $t('zhong-xin-fa-qi-gong-dan') }}
            </Button>
            <Button class="detail-action-btn" @click="closeChange" v-if="!isBtnOnlyRead">
              {{ $t('guan-bi-bian-geng') }}
            </Button>
            <Button class="detail-action-btn refresh-action-btn" @click="handleRefresh" :loading="loading">
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
      <div class="change-detail-body">
        <div class="content-wrap">
          <AppPageTabs v-model="currentTab" class="tab-wrap" :tabs="detailTabs" @change="tabClick">
            <template #label="{ tab }">
              <Dropdown v-if="tab.name === 'sql-change'" transfer @on-click="handleDropdownClick">
                <span class="change-content-tab-label">
                  {{ $t('sql-bian-geng-nei-rong') }}：{{ subTabLabel }}
                  <CustomIcon type="icon-v2-ArrowDown" size="13px" leftMargin="3px" />
                </span>
                <template #list>
                  <DropdownMenu>
                    <DropdownItem name="result">{{ $t('bian-geng-jie-guo') }}</DropdownItem>
                    <DropdownItem name="diff">{{ $t('bian-geng-diff') }}</DropdownItem>
                  </DropdownMenu>
                </template>
              </Dropdown>
              <span v-else>{{ tab.label }}</span>
            </template>
          </AppPageTabs>
          <div class="tab-item-wrap">
            <div v-if="currentTab === 'sql-change'" class="tab-item">
              <div v-if="isNotChangeReady" style="height: 100%">
                <div v-if="subTab === 'result'" class="change-sql-preview" @wheel="handleSqlPreviewWheel">
                  <read-only-editor
                    ref="changeSqlPreviewEditor"
                    :text="rowSql"
                    key="raw"
                    v-if="rowSql.length"
                    :ds-type="changeInfo?.dsType"
                    virtual-scroll-mode
                    :line-number-start="sqlPreviewStartLine"
                    @viewport-line-count-change="handleSqlPreviewViewportChange"
                  />
                  <input
                    v-if="sqlPreviewInitialized"
                    v-model.number="sqlPreviewStartLine"
                    class="change-virtual-scrollbar"
                    type="range"
                    min="1"
                    :max="sqlPreviewMaxStartLine"
                    step="1"
                    :aria-label="$t('ticket-sql-virtual-scrollbar')"
                    aria-orientation="vertical"
                    @input="scheduleSqlPreview"
                  />
                  <CCEmptyContent v-if="!rowSql.length" :content="changeInfo?.remark ? changeInfo?.remark : $t('wu-bian-geng-nei-rong')" />
                </div>
                <div v-if="subTab === 'diff'" style="height: 100%">
                  <Collapse v-model="activeNames" accordion v-if="changeBody.length" @on-change="handleDiffPanelChange">
                    <Panel v-for="(item, index) in changeBody" :key="index" :name="index.toString()">
                      {{ item.contentName }}
                      <template #content>
                        <div style="height: 400px">
                          <ChangeBodyDiff
                            :original="item.oldBody || ''"
                            :modified="item.newBody || ''"
                            language="sql"
                            :ds-type="changeInfo?.dsType"
                          />
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
              <div v-if="checkSummary?.length || checkedSql?.length" class="change-check-results">
                <section class="change-check-summary">
                  <div class="change-check-section-title">{{ $t('gui-ze-xiao-yan-jie-guo') }}</div>
                  <template v-if="checkSummary?.length">
                    <div v-for="(rule, index) in checkSummary" :key="index" class="change-check-rule">
                      <div class="change-check-rule-header">
                        <Tag :color="ERROR_LEVEL_COLOR_MAP[rule.ruleLevel]">{{ ERROR_LEVEL_MAP[rule.ruleLevel] }}</Tag>
                        <strong>{{ rule.name }}</strong>
                        <span v-if="rule.lines?.length" class="change-check-lines">
                          {{ $t('wei-zhi-0') }}：
                          <span v-for="line in rule.lines" :key="line">{{ line }}</span>
                          <span v-if="rule.hitCount > rule.lines.length">{{ $t('ticket-rule-location-total', { count: rule.hitCount }) }}</span>
                        </span>
                      </div>
                      <div class="change-check-rule-desc">{{ rule.desc }}</div>
                    </div>
                  </template>
                  <div v-else class="change-check-summary-empty">{{ $t('zan-wu-shu-ju') }}</div>
                </section>

                <section v-if="checkedSql?.length" class="change-check-details">
                  <div class="change-check-details-header">
                    <div class="change-check-section-title">{{ $t('sql-ming-xi') }}</div>
                    <span class="change-check-limit-tip">{{ $t('cicd-check-detail-limit-tip', { count: checkMaxDetails }) }}</span>
                  </div>
                  <Collapse v-model="curCollapse" simple>
                    <Panel v-for="(item, index) in checkedSql" :key="index">
                      <span class="collapse-text-ellipsis">{{ item.content }}</span>
                      <Button type="text" class="collapse-btn" @click.stop="getSqlDetail(item?.content)">
                        {{ $t('cha-kan') }}
                      </Button>
                      <template #content>
                        <Table
                          :columns="sqlReviewTableColumns"
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
                      </template>
                    </Panel>
                  </Collapse>
                </section>
              </div>
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
                  <section class="page-section exec-task-section">
                    <div class="exec-section-header">
                      <div class="exec-section-leading">
                        <div class="page-section__title exec-section-title">{{ $t('ren-wu-zhi-hang') }}</div>
                        <div class="exec-section-meta">
                          <Poptip :content="autoExecJobInfo.message" trigger="hover" v-if="autoExecJobInfo && !autoExecJobInfo.normal">
                            <Icon type="ios-alert-outline" />
                          </Poptip>
                          <Tag v-if="autoExecJobInfo?.status" :color="AUTO_EXEC_JOB_STATUS_COLOR[autoExecJobInfo.status]">
                            {{ AUTO_EXEC_JOB_STATUS_I18N[autoExecJobInfo?.status] }}
                          </Tag>
                          <div v-if="autoExecJobInfo?.execTime">
                            {{ $t('ji-hua-zhi-hang-shi-jian') }}
                            {{ autoExecJobInfo?.execTime }}
                          </div>
                          <div v-if="autoExecJobInfo?.workerIp">
                            {{ $t('ji-qi-ip-0') }}
                            {{ autoExecJobInfo?.workerIp }}
                          </div>
                          <div v-if="autoExecJobInfo?.workerStatus">
                            {{ $t('ji-qi-zhuang-tai-0') }}
                            {{ autoExecJobInfo?.workerStatus }}
                          </div>
                        </div>
                      </div>
                      <div class="exec-section-actions">
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
                        <Button v-if="autoExecJobInfo?.id" type="text" size="small" @click="handleAutoExecLog(null)" :disabled="isBtnOnlyRead">
                          {{ $t('tiao-du-ri-zhi') }}
                        </Button>
                      </div>
                    </div>
                    <Table
                      v-if="executeLoading || autoExecJobInfo"
                      :columns="autoExecTaskColumns"
                      :data="autoExecTaskList"
                      :loading="executeLoading"
                      border
                      stripe
                      size="small"
                    >
                      <template #status="{ row }">
                        <Tag :color="AUTO_EXEC_TASK_STATUS_COLOR[row?.status]">
                          {{ AUTO_EXEC_TASK_STATUS_I18N[row?.status] }}
                        </Tag>
                      </template>
                      <template #actualStartTime="{ row }">{{ row.actualStartTime || '-' }}</template>
                      <template #actualEndTime="{ row }">{{ row.actualEndTime || '-' }}</template>
                      <template #action="{ row }">
                        <Button type="text" size="small" @click="handleAutoExecLog(row)">
                          {{ $t('ri-zhi') }}
                        </Button>
                        <Button type="text" size="small" @click="getSqlDetail(row?.execSql)">
                          {{ $t('cha-kan-sql') }}
                        </Button>
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
                    <CCEmptyContent v-else :content="$t('zan-wu-shu-ju')" />
                    <div v-if="total > pageSize" style="width: 100%; text-align: right">
                      <Page
                        v-model="page"
                        :page-size="pageSize"
                        :total="total"
                        @on-change="handleTaskPageChange"
                        size="small"
                        style="margin-top: 10px"
                      />
                    </div>
                  </section>
                </div>
                <div class="exec-right" v-if="changeInfo.currentStatus === 'OPEN'">
                  <section class="page-section exec-confirm-section">
                    <Form :model="confirmInfo.config" :label-width="60">
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
                  </section>
                </div>
              </div>
              <CCEmptyContent v-else :content="$t('xi-tong-tiao-du-zhong')" loading />
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
    <CCModal v-model="showContinueAutoExecTaskModal" :title="$t('qu-xiao-tiao-guo')" @ok="handleContinueAutoExecTask">
      {{ $t('qu-xiao-tiao-guo-hou-xia-ci-zhong-shi-ren-wu-shi-jiang-zhi-hang-gai-sql') }}
    </CCModal>
    <CCModal v-model="showAllSql" :title="$t('cha-kan-sql')" :width="840" :draggable="false" footer-hide>
      <div class="sql-viewer-panel">
        <read-only-editor :text="allSql" key="raw" :border="0" :font-weight="400" :ds-type="changeInfo?.dsType" fit-viewport />
      </div>
    </CCModal>
    <CCModal width="800" v-model="showFinishTicket" :title="$t('jie-shu-gong-dan')">
      <Alert type="warning">
        {{
          $t(
            'xia-mian-sql-xi-tong-bu-hui-zhi-hang-xu-yao-ren-gong-fang-shi-zai-shu-ju-ku-zhong-shou-dong-zhi-hang-dang-nin-du-chu-li-hao-hou-ke-yi-dian-ji-xia-mian-que-ren-bing-guan-bi-lai-jie-shu-gong-dan'
          )
        }}
      </Alert>
      <div class="change-finish-sql-preview" @wheel="handleSqlPreviewWheel">
        <read-only-editor
          ref="finishSqlPreviewEditor"
          :text="rowSql"
          key="finish-raw"
          :max-height="600"
          :ds-type="changeInfo?.dsType"
          virtual-scroll-mode
          :line-number-start="sqlPreviewStartLine"
          @viewport-line-count-change="handleSqlPreviewViewportChange"
        />
        <input
          v-if="sqlPreviewInitialized"
          v-model.number="sqlPreviewStartLine"
          class="change-virtual-scrollbar"
          type="range"
          min="1"
          :max="sqlPreviewMaxStartLine"
          step="1"
          :aria-label="$t('ticket-sql-virtual-scrollbar')"
          aria-orientation="vertical"
          @input="scheduleSqlPreview"
        />
      </div>
      <template #footer>
        <div>
          <Button type="primary" @click="confirmFinishTicket">{{ $t('que-ren-jie-shu') }}</Button>
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
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import CCEmptyContent from '@/components/widgets/CCEmptyContent';
import AppPageTabs from '@/components/layout/AppPageTabs';
import ChangeBodyDiff from './changeBodyDiff';
import { getScmDisplayName, getScmIconResource } from './utils';

import {
  AUTO_EXEC_JOB_STATUS_COLOR,
  AUTO_EXEC_JOB_STATUS_I18N,
  AUTO_EXEC_TASK_STATUS_COLOR,
  AUTO_EXEC_TASK_STATUS_I18N,
  CHANGE_STATUS_MAP,
  ERROR_LEVEL_COLOR_MAP,
  ERROR_LEVEL_MAP,
  FLOW_STEP,
  FLOW_STEP_NUM,
  sqlReviewTableColumns,
  STATUS_MAP
} from './constant';

export default {
  name: 'changeDetail',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['isSaas']),
    detailTabs() {
      const currentStep = this.changeInfo?.currentStep;
      const currentStepIndex = CHANGE_STATUS_MAP[currentStep] ?? -1;
      const isSnapshot = currentStep === 'INIT_SNAPSHOT';
      return [
        {
          name: 'sql-change',
          label: this.$t('sql-bian-geng-nei-rong'),
          disabled: currentStepIndex < FLOW_STEP.S0 || isSnapshot
        },
        {
          name: 'sql-audit',
          label: this.$t('sql-shen-he'),
          disabled: currentStepIndex < FLOW_STEP.S1 || isSnapshot
        },
        {
          name: 'approval',
          label: this.$t('shen-pi-liu-cheng'),
          disabled: currentStepIndex < FLOW_STEP.S2 || isSnapshot || this.isDisabledApproval
        },
        {
          name: 'execute',
          label: this.$t('bian-geng-zhi-xing'),
          disabled: currentStepIndex < FLOW_STEP.FINISH || isSnapshot || this.isManualExec
        }
      ];
    },
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
    changeStatusLabel() {
      const statusMap = {
        OPEN: this.$t('jin-hang-zhong'),
        READY: this.$t('jin-hang-zhong'),
        WAIT: this.$t('deng-dai-zhi-hang'),
        FAILED: this.$t('shi-bai'),
        FINISH: this.$t('wan-cheng'),
        CLOSED: this.$t('yi-guan-bi')
      };
      return statusMap[this.changeInfo?.currentStatus] || this.changeInfo?.currentStatus || '-';
    },
    changeStatusClass() {
      const status = this.changeInfo?.currentStatus;
      if (status === 'FAILED') {
        return 'danger';
      }
      if (status === 'CLOSED') {
        return 'muted';
      }
      if (status === 'FINISH') {
        return 'success';
      }
      return 'progress';
    },
    changeStepLabel() {
      const stepMap = {
        INIT_SNAPSHOT: this.$t('kuai-zhao-bian-geng'),
        INIT: this.$t('di-jiao'),
        CHECK: this.$t('sql-shen-he'),
        APPROVAL: this.$t('shen-pi-liu'),
        EXECUTE: this.$t('zhi-xing'),
        FINISH: this.$t('wan-cheng')
      };
      return stepMap[this.changeInfo?.currentStep] || this.changeInfo?.currentStep || '-';
    },
    showChangeStepPill() {
      return this.changeStepLabel !== '-' && this.changeStepLabel !== this.changeStatusLabel;
    },
    subTabLabel() {
      return this.subTab === 'diff' ? this.$t('bian-geng-diff') : this.$t('bian-geng-jie-guo');
    },
    sqlPreviewMaxStartLine() {
      return Math.max(1, this.sqlPreviewTotalLines - this.sqlPreviewLineCount + 1);
    },
    isBtnOnlyRead() {
      return this.changeInfo.locked;
    },
    isNotChangeReady() {
      return FLOW_STEP_NUM[this.changeInfo.currentStep] > FLOW_STEP_NUM.INIT || this.changeInfo.currentStatus !== 'READY';
    },
    isNotCheckReady() {
      return FLOW_STEP_NUM[this.changeInfo.currentStep] > FLOW_STEP_NUM.CHECK || this.changeInfo.currentStatus !== 'READY';
    },
    isNotApproveReady() {
      return FLOW_STEP_NUM[this.changeInfo.currentStep] > FLOW_STEP_NUM.APPROVAL || this.changeInfo.currentStatus !== 'READY';
    },
    isNotExecuteReady() {
      return FLOW_STEP_NUM[this.changeInfo.currentStep] === FLOW_STEP_NUM.EXECUTE || this.changeInfo.currentStatus !== 'READY';
    },
    isErrorCheck() {
      return this.changeInfo.currentStatus === 'FAILED' && FLOW_STEP_NUM[this.changeInfo.currentStep] === FLOW_STEP_NUM.CHECK;
    },
    isReadyStatus() {
      return this.changeInfo.currentStatus === 'READY';
    },
    cantRetry() {
      return this.changeInfo.currentStatus !== 'FAILED' || FLOW_STEP_NUM[this.changeInfo.currentStep] === FLOW_STEP_NUM.EXECUTE;
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
    AppPageTabs,
    ReadOnlyEditor,
    ChangeBodyDiff,
    CCEmptyContent
  },
  data() {
    return {
      showContinueAutoExecTaskModal: false,
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
      executeLoading: false,
      activeNames: [],
      autoExecTaskList: [],
      autoExecJobLogList: [],
      autoExecTaskLogList: [],
      selectedAutoExecTask: {},
      sqlReviewTableColumns,
      showAddModal: false,
      loading: false,
      changeName: '',
      allSql: '',
      changeInfo: {},
      rowSql: '',
      sqlPreviewStartLine: 1,
      sqlPreviewTotalLines: 1,
      sqlPreviewLineCount: 30,
      sqlPreviewInitialized: false,
      sqlPreviewTimer: null,
      checkedSql: [],
      checkSummary: [],
      checkMaxDetails: 50,
      changeBody: [],
      currentTab: '',
      subTab: 'result',
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
          title: this.$t('xu-hao'),
          key: 'executeOrder',
          width: 80
        },
        {
          title: this.$t('zhi-xing-ci-shu'),
          key: 'execCount',
          width: 100
        },
        {
          title: this.$t('zhuang-tai'),
          slot: 'status',
          width: 100
        },
        {
          title: this.$t('shi-ji-kai-shi-shi-jian'),
          slot: 'actualStartTime',
          width: 180
        },
        {
          title: this.$t('shi-ji-jie-shu-shi-jian'),
          slot: 'actualEndTime',
          width: 180
        },
        {
          title: this.$t('cao-zuo'),
          minWidth: 200,
          align: 'right',
          slot: 'action'
        }
      ],
      curCollapse: [],
      showEditBecomeName: false,
      ERROR_LEVEL_MAP,
      FLOW_STEP_NUM,
      FLOW_STEP,
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
    getScmDisplayName,
    getScmIconResource,
    async init() {
      this.changeId = this.$route.params.id;
      await this.getDetail();
      this.moveToCurrentTab();
      if (this.currentTab === 'sql-change' && this.isNotChangeReady) {
        this.loadSqlPreview();
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
      const res = await this.$services.dmCicdChangeDetail({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.changeInfo = res.data;
        this.syncFlowIdQuery();
      }
      this.loading = false;
    },
    syncFlowIdQuery() {
      const flowId = this.changeInfo?.flowId;
      if (!flowId || String(this.$route.query.flowId || '') === String(flowId)) {
        return;
      }
      this.$router.replace({
        path: this.$route.path,
        query: {
          ...this.$route.query,
          flowId: String(flowId)
        }
      });
    },
    async loadSqlPreview() {
      const editor = this.$refs.finishSqlPreviewEditor || this.$refs.changeSqlPreviewEditor;
      this.sqlPreviewLineCount = Math.min(200, editor?.getVisibleLineCount() || this.sqlPreviewLineCount);
      const res = await this.$services.dmCicdChangeSqlPreview({
        data: {
          changeId: this.changeId,
          startLine: this.sqlPreviewStartLine,
          lineCount: this.sqlPreviewLineCount
        }
      });
      if (res.success) {
        this.sqlPreviewStartLine = res.data?.startLine || 1;
        this.sqlPreviewTotalLines = res.data?.totalLines || 1;
        this.rowSql = res.data?.content || '';
        if (res.data?.itemList) {
          this.changeBody = res.data.itemList;
        }
        this.sqlPreviewInitialized = true;
      }
    },
    async handleDiffPanelChange(name) {
      const selected = Array.isArray(name) ? name[0] : name;
      if (selected === undefined || selected === null || selected === '') {
        return;
      }
      const index = Number(selected);
      const item = this.changeBody[index];
      if (!item || item.loaded) {
        return;
      }
      const res = await this.$services.dmCicdChangeSqlPreview({
        data: {
          changeId: this.changeId,
          contentName: item.contentName,
          startLine: 1,
          lineCount: 1
        }
      });
      if (res.success && res.data?.itemList?.length) {
        this.changeBody.splice(index, 1, { ...res.data.itemList[0], loaded: true });
      }
    },
    scheduleSqlPreview() {
      clearTimeout(this.sqlPreviewTimer);
      this.sqlPreviewTimer = setTimeout(() => this.loadSqlPreview(), 120);
    },
    handleSqlPreviewViewportChange(lineCount) {
      if (!lineCount || lineCount === this.sqlPreviewLineCount) {
        return;
      }
      this.sqlPreviewLineCount = lineCount;
      this.scheduleSqlPreview();
    },
    handleSqlPreviewWheel(event) {
      if (!this.sqlPreviewInitialized) {
        return;
      }
      event.preventDefault();
      const step = Math.max(1, Math.floor(this.sqlPreviewLineCount / 3));
      const delta = event.deltaY > 0 ? step : -step;
      this.sqlPreviewStartLine = Math.max(1, Math.min(this.sqlPreviewMaxStartLine, this.sqlPreviewStartLine + delta));
      this.scheduleSqlPreview();
    },
    async getApproval() {
      const res = await this.$services.dmCicdChangeApproval({
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

      const res = await this.$services.dmCicdChangeChecks({
        data: {
          changeId: this.changeId
        }
      });

      this.loading = false;
      if (Array.isArray(res.data)) {
        this.checkedSql = res.data;
        this.checkSummary = [];
        this.checkMaxDetails = 50;
      } else {
        this.checkedSql = res.data?.detailList || [];
        this.checkSummary = res.data?.summaryList || [];
      }
    },

    async getExecuteState() {
      if (this.isReadyStatus) {
        this.isScheduling = true;
        return;
      }
      const res = await this.$services.dmCicdChangeExecute({
        data: {
          changeId: this.changeId
        }
      });

      if (!res.success) {
        this.isScheduling = false;
        return;
      }

      // Execution configuration and execution records are independent. A
      // completed automatic change may have a job even when no EXECUTE item
      // was persisted, so always load the job and tasks unless it is manual.
      if (res?.data?.execType === 'MANUAL_EXEC') {
        this.isManualExec = true;
        this.currentTab = 'sql-change';
        this.$Message.info(this.$t('shou-dong-zhi-hang-mo-shi-xia-qing-zi-hang-fu-zhi-bian-geng-nei-rong-qian-qu-zhi-hang'));
        this.isScheduling = false;
        return;
      }

      this.isManualExec = false;
      await this.handleRefreshTaskList();
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
      this.$router.push(`/cicd/${row?.id || 1}`);
    },
    getCyclicKey(type, index) {
      const items = {
        nameItems: ['empty-condition', 'with-clause', 'subquery-insert'],
        typeItems: ['submit-type', 'sql-audit-type', 'audit-method', 'execute-type']
      };
      return items[type][index % items[type].length];
    },
    async closeChange() {
      const res = await this.$services.dmCicdChangeClose({
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
      await this.getDetail();
      if (name === 'sql-change' && !this.isReadyStatus) {
        this.loadSqlPreview();
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
      this.showFinishTicket = true;
      await this.$nextTick();
      await this.loadSqlPreview();
    },
    async confirmFinishTicket() {
      const data = { ...this.confirmInfo };
      data.changeId = this.changeId;
      data.config.execTime = null;

      const res = await this.$services.dmCicdChangeConfirmExec({ data });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.showFinishTicket = false;
        this.init();
        this.queryAutoExecJobInfo();
      }
    },
    async handleConfirmTicketByNow() {
      const data = { ...this.confirmInfo };
      data.changeId = this.changeId;
      data.config.execTime = null;

      const res = await this.$services.dmCicdChangeConfirmExec({ data });
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

      const res = await this.$services.dmCicdChangeConfirmExec({ data });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.init();
        this.queryAutoExecJobInfo();
      }
    },
    async queryAutoExecJobInfo() {
      const res = await this.$services.dmCicdChangeExecJobInfo({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.autoExecJobInfo = res.data;
      }
    },
    async handleAutoExecLog(task = null) {
      if (!this.autoExecJobInfo?.id) {
        return;
      }
      const res = await this.$services.dmCicdChangeExecLog({
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
      const res = await this.$services.dmCicdChangeExecTaskList({
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
        const pageData = res.data || {};
        this.autoExecTaskList = pageData.records || [];
        this.page = pageData.current || 1;
        this.pageSize = pageData.size || this.pageSize;
        this.total = pageData.total || 0;
      }
    },
    async handleStopAutoExecJob() {
      const res = await this.$services.dmCicdChangeExecJobPause({
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
      let service = this.$services.dmCicdChangeExecJobRetry;
      if (this.autoExecJobInfo?.canRestart) {
        service = this.$services.dmCicdChangeExecJobStart;
      }
      const res = await service({
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
      const res = await this.$services.dmCicdChangeExecJobAbort({
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
      const res = await this.$services.dmCicdChangeExecTaskSkip({
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
      const res = await this.$services.dmCicdChangeExecTaskContinue({
        data: {
          taskId: this.selectedAutoExecTask.taskId,
          changeId: this.changeId
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
      this.showContinueAutoExecTaskModal = true;
      this.selectedAutoExecTask = task;
    },
    async handleRefreshTaskList() {
      this.executeLoading = true;
      try {
        await Promise.all([this.queryAutoExecJobInfo(), this.queryAutoExecTaskList()]);
      } finally {
        this.executeLoading = false;
      }
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
      this.showContinueAutoExecTaskModal = false;
    },
    async skipCheck() {
      const res = await this.$services.dmCicdChangeSkipChecks({
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
      const res = await this.$services.dmCicdChangeRetry({
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
      const requestedTab = this.$route.query.tab;
      const requestedTabConfig = this.detailTabs.find((tab) => tab.name === requestedTab);
      if (requestedTabConfig && !requestedTabConfig.disabled) {
        this.currentTab = requestedTab;
        return;
      }

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
        case 'FINISH':
          this.currentTab = 'execute';
          break;
        default:
          this.currentTab = 'sql-change';
      }
    },
    handleDropdownClick(name) {
      this.subTab = name;
      if (name === 'result' || !this.changeBody.length) {
        this.loadSqlPreview();
      }
    }
  }
};
</script>

<style lang="less" scoped>
.change-detail-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  background: #fff;
  color: #1f2937;

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
    a {
      margin-right: 16px;
    }
  }

  .actions {
    font-size: 12px;
  }
}

.change-detail-shell {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  padding: 20px 24px;
  overflow: hidden;
  gap: 32px;
}

.change-detail-body {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
}

.page-section {
  min-width: 0;
}

.page-section__title {
  position: relative;
  margin-bottom: 0;
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

.release-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 64px minmax(0, 1fr);
  align-items: stretch;
  gap: 24px;
}

.release-panel {
  min-width: 0;
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

.change-detail-hero {
  display: grid;
  flex: 0 0 auto;
  grid-template-columns: minmax(620px, 1.35fr) minmax(420px, 0.9fr);
  gap: 24px;
  align-items: start;
  margin-bottom: 0 !important;
  padding: 0;
}

.left-wrap {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.title-wrap {
  display: flex;
  align-items: center;
  min-width: 0;
  gap: 10px;
  margin-bottom: 18px;
}

.title-text-ellipsis {
  display: inline-block;
  max-width: 520px;
  overflow: hidden;
  color: #111827;
  font-size: 20px;
  font-weight: 800;
  line-height: 28px;
  text-overflow: ellipsis;
  vertical-align: middle;
  white-space: nowrap;
}

.change-status-pill,
.change-step-pill {
  display: inline-flex;
  flex: 0 0 auto;
  align-items: center;
  height: 26px;
  padding: 0 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 800;
  line-height: 1;
}

.change-status-pill {
  &.success {
    color: #0fac69;
    background: #e0f8e9;
  }

  &.progress {
    color: #2175d9;
    background: #e8f2ff;
  }

  &.danger {
    color: #d92d20;
    background: #fff0ee;
  }

  &.muted {
    color: #667085;
    background: #eef2f7;
  }
}

.change-step-pill {
  color: #5b6a80;
  background: #eef4fa;
}

.change-detail-pipeline {
  margin-top: 8px;
}

.endpoint-summary-lines {
  display: grid;
  gap: 8px;
  min-width: 0;
}

.endpoint-summary-line {
  display: grid;
  grid-template-columns: 88px minmax(0, 1fr);
  align-items: center;
  min-width: 0;
  color: #111827;
  font-size: 14px;
  line-height: 22px;

  span {
    color: #66758a;
    font-weight: 500;
    white-space: nowrap;
  }

  strong {
    min-width: 0;
    overflow: hidden;
    color: #111827;
    font-weight: 500;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.right-wrap {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  min-width: 0;
  min-height: 150px;

  .btns {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-end;
    gap: 8px;
    min-width: 0;
  }

  :deep(.ivu-steps.ivu-steps-small .ivu-steps-title) {
    width: auto;
    color: #5b6a80;
    font-size: 12px;
  }
}

.detail-action-btn {
  height: 32px;
  padding: 0 12px;
  border-color: #d9e3ee;
  border-radius: 5px;
  color: #111827;
  font-size: 12px;
  font-weight: 500;
  background: #fff;
  box-shadow: 0 3px 8px rgba(31, 45, 61, 0.04);
}

.primary-action {
  border-color: #14b86f;
  background: #14b86f;
}

.refresh-action-btn {
  width: 34px;
  padding: 0;
}

.step-wrap {
  width: 100%;
  max-width: 560px;
  margin-top: 20px;
  padding: 0;
  border-left: 0;
}

.progress-bar {
  width: 200px;
}

.content-wrap {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;

  .read-only-editor {
    border: none;
  }

  .tab-wrap {
    flex: 0 0 auto;
    width: 100%;
  }

  .change-content-tab-label {
    display: inline-flex;
    align-items: center;
  }

  .tab-item-wrap {
    flex: 1 1 auto;
    min-height: 0;
    padding: 18px 0 0;
    overflow: auto;
  }

  .tab-item {
    height: 100%;
    min-height: 0;
  }
}

.sql-viewer-panel {
  overflow: hidden;
  border: 1px solid var(--border-primary);
  border-radius: 6px;
  background: var(--bg-secondary);
}

:deep(.ivu-table-wrapper) {
  border-color: #dbe6f1;
}

:deep(.ivu-timeline-item-tail) {
  border-left: 2px solid #e8eaec;
}

.section {
  padding: 15px;
}

.item-list {
  list-style: none;
  padding-left: 0;
}

.item-list li {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #e8eaec;
}

.item-list li i {
  margin-right: 8px;
  color: #2d8cf0;
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

.extra-btn {
  margin: 5px 10px 0 0;
}

.exec-wrap {
  display: flex;
  height: 100%;
  width: 100%;
  min-height: 0;

  .exec-left {
    flex: 1;
    height: 100%;
    min-width: 0;
    overflow: auto;
  }

  .exec-right {
    display: flex;
    align-items: flex-end;
    flex-direction: column;
    width: 100%;
    height: 100%;
    min-width: 0;
  }

  .exec-confirm-section {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 100%;
    max-width: 820px;
    height: auto;
    min-height: 220px;
    padding: 0;
  }

  .exec-task-section {
    display: flex;
    flex-direction: column;
    gap: 16px;
    width: 100%;
    min-height: 0;
  }

  .exec-section-header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 16px;
    min-width: 0;
  }

  .exec-section-leading {
    display: flex;
    flex-direction: column;
    gap: 12px;
    min-width: 0;
  }

  .exec-section-meta {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
    padding-left: 12px;
    color: #465467;
    font-size: 14px;
  }

  .exec-section-actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: flex-end;
    gap: 4px;
    flex: 0 0 auto;
  }

  .right-footer {
    display: flex;
    justify-content: flex-end;
    margin-top: 14px;
  }
}

.empty-wrap,
.empty-div {
  display: flex;
  height: 100%;
  min-height: 260px;
  align-items: center;
  justify-content: center;
}

.collapse-text-ellipsis {
  display: inline-block;
  max-width: 90%;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: middle;
  white-space: nowrap;
}

.collapse-btn {
  float: right;
}

.change-check-results {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.change-check-summary,
.change-check-details {
  border: 1px solid #e8eaec;
  border-radius: 4px;
  background: #fff;
}

.change-check-section-title {
  color: #17233d;
  font-size: 16px;
  font-weight: 600;
}

.change-check-summary > .change-check-section-title,
.change-check-details-header {
  padding: 14px 16px;
  border-bottom: 1px solid #e8eaec;
}

.change-check-rule {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: 0;
  }
}

.change-check-rule-header,
.change-check-lines,
.change-check-details-header {
  display: flex;
  align-items: center;
}

.change-check-rule-header {
  flex-wrap: wrap;
  gap: 8px;
}

.change-check-lines {
  flex-wrap: wrap;
  gap: 8px;
  color: #808695;
}

.change-check-rule-desc {
  margin-top: 8px;
  color: #515a6e;
}

.change-check-summary-empty {
  padding: 28px 16px;
  color: #808695;
  text-align: center;
}

.change-check-details-header {
  justify-content: space-between;
  gap: 16px;
}

.change-check-limit-tip {
  color: #808695;
  font-size: 13px;
}

.change-sql-preview {
  position: relative;
  height: 100%;
  padding-right: 18px;
}

.change-finish-sql-preview {
  position: relative;
  min-height: 300px;
  padding-right: 18px;
}

.change-virtual-scrollbar {
  position: absolute;
  z-index: 3;
  top: 8px;
  right: 3px;
  width: 14px;
  height: calc(100% - 16px);
  margin: 0;
  writing-mode: vertical-lr;
  direction: ltr;
  appearance: none;
  background: transparent;
  cursor: pointer;

  &::-webkit-slider-runnable-track {
    width: 10px;
    height: 100%;
    background: transparent;
  }

  &::-webkit-slider-thumb {
    width: 10px;
    height: 20px;
    border: 0;
    border-radius: 0;
    appearance: none;
    background: rgba(100, 100, 100, 0.45);
  }

  &::-moz-range-track {
    width: 10px;
    height: 100%;
    background: transparent;
  }

  &::-moz-range-thumb {
    width: 10px;
    height: 20px;
    border: 0;
    border-radius: 0;
    background: rgba(100, 100, 100, 0.45);
  }
}

.red-text {
  color: #ea3323;
  margin-left: 5px;
}

.gray-text {
  color: #66758a;
  margin-left: 5px;
}

@media (max-width: 1320px) {
  .change-detail-hero {
    grid-template-columns: 1fr;
  }

  .right-wrap {
    align-items: flex-start;
    min-height: auto;

    .btns {
      justify-content: flex-start;
    }
  }

  .step-wrap {
    width: 100%;
  }
}

@media (max-width: 980px) {
  .change-detail-shell {
    padding: 16px;
  }

  .change-detail-hero {
    gap: 20px;
  }

  .title-wrap {
    flex-wrap: wrap;
  }

  .title-text-ellipsis {
    max-width: 100%;
  }

  .change-detail-pipeline,
  .release-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .link-divider {
    min-height: 64px;
  }

  .content-wrap .tab-item-wrap {
    padding-top: 16px;
  }
}
</style>
