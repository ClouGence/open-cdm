<template>
  <div class="page-shell ticket-detail-page">
    <div class="page-shell__body ticket-detail-container">
      <section class="page-section ticket-info-section">
        <div class="ticket-info-section__header">
          <div class="page-section__title">{{ $t('ticket-information') }}</div>
          <div class="ticket-overview__actions">
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
            <Button class="refresh-btn" :loading="loading" @click="getTicketDetail('refresh')">
              <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
            </Button>
          </div>
        </div>
        <div class="ticket-overview">
          <div class="ticket-overview__hero">
            <div class="ticket-overview__headline">
              <h2>{{ ticketDetail.ticketTitle || '-' }}</h2>
            </div>
          </div>

          <div
            :class="[
              'ticket-overview__primary-grid',
              { 'ticket-overview__primary-grid--with-target-database': ['DM_QUERY', 'DM_CHANGE'].includes(ticketDetail.approBiz) }
            ]"
          >
            <div class="ticket-meta-item">
              <span class="ticket-meta-item__label">{{ $t('ticket-number') }}</span>
              <span class="ticket-meta-item__value ticket-meta-item__ticket-id">
                <strong>{{ ticketDetail.bizId || `#${ticketDetail.id || ticketId}` }}</strong>
                <button
                  type="button"
                  class="ticket-meta-item__copy"
                  :aria-label="$t('fu-zhi')"
                  :title="$t('fu-zhi')"
                  @click="copyText(ticketDetail.bizId || `#${ticketDetail.id || ticketId}`)"
                >
                  <Icon type="ios-copy-outline" />
                </button>
              </span>
            </div>
            <div class="ticket-meta-item">
              <span class="ticket-meta-item__label ticket-meta-item__label--with-icon">
                <Icon type="ios-person-outline" />
                {{ $t('shen-qing-ren') }}
              </span>
              <span class="ticket-meta-item__value">
                {{ ticketDetail.userName || '-' }}
              </span>
            </div>
            <div v-if="['DM_QUERY', 'DM_CHANGE'].includes(ticketDetail.approBiz)" class="ticket-meta-item ticket-meta-item--target-database">
              <span class="ticket-meta-item__label ticket-meta-item__label--with-icon">
                <Icon type="ios-server-outline" />
                {{ $t('db-shi-li-ku-ming') }}
              </span>
              <Tooltip transfer :content="ticketDetail.targetInfo">
                <span class="ticket-meta-item__value ticket-meta-item__database">
                  <DataSourceIcon
                    v-if="ticketDetail.dataSourceType"
                    class="ticket-meta-item__database-icon"
                    :type="ticketDetail.dataSourceType"
                    :instanceType="ticketDetail.dsDeployType"
                    size="24px"
                    leftMargin="0"
                  />
                  <span>{{ ticketDetail.targetInfo || '-' }}</span>
                </span>
              </Tooltip>
            </div>
            <div class="ticket-meta-item">
              <span class="ticket-meta-item__label ticket-meta-item__label--with-icon">
                <Icon type="ios-layers-outline" />
                {{ $t('ticket-application-template') }}
              </span>
              <span class="ticket-meta-item__value">
                <img
                  v-if="['Internal', 'Inner'].includes(ticketDetail.approType)"
                  class="ticket-meta-item__product-icon"
                  src="/dm.ico"
                  alt="CloudDM"
                />
                <CustomIcon
                  v-else-if="ticketDetail.approType"
                  :type="`icon-v2-${ticketDetail.approType}`"
                  :instanceType="ticketDetail.dsDeployType"
                />
                {{ ticketDetail.approTemplateName || ticketDetail.approTypeName || '-' }}
              </span>
            </div>
          </div>

          <div
            :class="[
              'ticket-overview__secondary-grid',
              { 'ticket-overview__secondary-grid--with-target-database': ['DM_QUERY', 'DM_CHANGE'].includes(ticketDetail.approBiz) }
            ]"
          >
            <div class="ticket-meta-item">
              <span class="ticket-meta-item__label ticket-meta-item__label--with-icon">
                <Icon type="ios-time-outline" />
                {{ $t('ticket-trigger-time') }}
              </span>
              <span>{{ ticketDetail.gmtCreate || '-' }}</span>
            </div>
            <div class="ticket-meta-item">
              <span class="ticket-meta-item__label ticket-meta-item__label--with-icon">
                <Icon type="ios-time-outline" />
                {{ $t('wan-cheng-shi-jian') }}
              </span>
              <span>{{ ticketDetail.finishTime || '-' }}</span>
            </div>
            <div class="ticket-meta-item ticket-meta-item--description">
              <span class="ticket-meta-item__label ticket-meta-item__label--with-icon">
                <Icon type="ios-document-outline" />
                {{ $t('miao-shu') }}
              </span>
              <Tooltip transfer :content="ticketDetail.description">
                <span class="ticket-meta-item__description">{{ ticketDetail.description || '-' }}</span>
              </Tooltip>
            </div>
          </div>
          <div v-if="ticketDetail.ticketStatus === 'FAILED' && ticketDetail.statusMessage" class="ticket-overview__error">
            <Icon type="ios-alert-outline" />
            <span>{{ ticketDetail.statusMessage }}</span>
          </div>
        </div>
      </section>

      <section v-if="ticketProgressSteps.length" class="page-section ticket-progress-card">
        <div class="page-section__title">{{ $t('jin-du') }}</div>
        <div class="ticket-progress-section">
          <div class="ticket-progress-scroll">
            <div class="ticket-progress">
              <template v-for="(step, index) in ticketProgressSteps" :key="step.key">
                <button
                  type="button"
                  :class="['ticket-progress-step', `ticket-progress-step--${step.state}`, { 'is-selected': selectedStepKey === step.key }]"
                  @click="selectTicketStep(step)"
                >
                  <span class="ticket-progress-step__icon"><Icon :type="step.icon" /></span>
                  <span class="ticket-progress-step__content">
                    <strong>{{ step.title }}</strong>
                    <span>{{ step.time || '-' }}</span>
                    <span class="ticket-progress-step__handler">{{ $t('chu-li-ren') }}：{{ step.handler || '-' }}</span>
                  </span>
                </button>
                <span
                  v-if="index < ticketProgressSteps.length - 1"
                  :class="['ticket-progress-connector', { 'is-reached': ticketProgressSteps[index + 1].state !== 'pending' }]"
                ></span>
              </template>
            </div>
          </div>
        </div>

        <div v-if="selectedTicketStep" class="ticket-step-detail">
          <div class="page-section__title">{{ $t('ticket-step-details') }}</div>
          <div class="ticket-step-summary">
            <div class="ticket-step-summary__item">
              <span>{{ $t('ticket-current-step') }}</span>
              <strong class="ticket-step-name">{{ selectedTicketStep.title }}</strong>
            </div>
            <div class="ticket-step-summary__item">
              <span>{{ $t('chu-li-ren') }}</span>
              <strong>{{ selectedTicketStep.handler || '-' }}</strong>
            </div>
            <div class="ticket-step-summary__item">
              <span>{{ $t('kai-shi-shi-jian') }}</span>
              <strong>{{ selectedTicketStep.startTime || '-' }}</strong>
            </div>
            <div class="ticket-step-summary__item">
              <span>{{ $t('zhuang-tai') }}</span>
              <strong :class="['analysis-item-status', `status-${selectedTicketStep.statusClass}`]">{{ selectedTicketStep.statusText }}</strong>
            </div>
          </div>

          <div v-if="selectedTicketRejectionReason" class="ticket-step-rejection-reason">
            <span class="ticket-step-rejection-reason__icon">
              <Icon type="ios-alert-outline" />
            </span>
            <strong>{{ $t('ticket-rejection-reason') }}</strong>
            <p>{{ selectedTicketRejectionReason }}</p>
          </div>

          <template v-if="selectedTicketStep.stage === 'EXPLAIN' && analysisItems.length">
            <div class="analysis-summary-list">
              <div
                v-for="item in analysisItems"
                :key="item.activityTitle"
                :class="[
                  'analysis-summary-item',
                  {
                    'analysis-summary-item--table': item.activityTitle === 'BEHAVIOR_ANALYSIS',
                    'is-expanded': item.activityTitle === 'BEHAVIOR_ANALYSIS' && isAnalysisResultExpanded(item.activityTitle)
                  }
                ]"
              >
                <div class="analysis-summary-row">
                  <button
                    type="button"
                    :class="['analysis-summary-row__expand', { 'is-expanded': isAnalysisResultExpanded(item.activityTitle) }]"
                    :aria-label="isAnalysisResultExpanded(item.activityTitle) ? $t('ticket-collapse-details') : $t('ticket-expand-details')"
                    :aria-expanded="isAnalysisResultExpanded(item.activityTitle)"
                    :aria-controls="`analysis-result-${item.activityTitle}`"
                    @click="toggleAnalysisResult(item.activityTitle)"
                  >
                    <Icon type="ios-arrow-forward" />
                  </button>
                  <span class="analysis-summary-row__icon">
                    <Icon :type="item.activityTitle === 'SECURITY_RULE' ? 'ios-lock-outline' : 'ios-search'" />
                  </span>
                  <strong>{{ analysisTypeText(item.activityTitle) }}</strong>
                  <span :class="['analysis-item-status', analysisStatusClass(item.activityStatus)]">
                    {{ analysisStatusText(item.activityStatus) }}
                  </span>
                  <span class="analysis-summary-row__result">
                    {{ item.activityTitle === 'BEHAVIOR_ANALYSIS' ? behaviorSummaryText(item) : analysisResultText(item) }}
                  </span>
                  <span class="analysis-summary-row__elapsed">
                    <span>{{ $t('hao-shi') }}</span>
                    {{ analysisElapsed(item) }}
                  </span>
                </div>

                <div
                  :id="`analysis-result-${item.activityTitle}`"
                  :class="['analysis-result-collapse', { 'is-expanded': isAnalysisResultExpanded(item.activityTitle) }]"
                  :aria-hidden="!isAnalysisResultExpanded(item.activityTitle)"
                  :inert="!isAnalysisResultExpanded(item.activityTitle)"
                >
                  <div class="analysis-result-collapse__content">
                    <div :class="['analysis-result-details', { 'analysis-result-details--table': item.activityTitle === 'BEHAVIOR_ANALYSIS' }]">
                      <div class="page-panel-body">
                        <template v-if="item.activityTitle === 'BEHAVIOR_ANALYSIS'">
                          <Table
                            v-if="recognizedBehaviorRows.length"
                            :columns="recognizedContentColumns"
                            :data="recognizedBehaviorRows"
                            border
                            size="small"
                          >
                            <template #resourceType="{ row }">
                              <Tag>{{ row.resourceType || '--' }}</Tag>
                            </template>
                            <template #actions="{ row }">
                              <Tag v-for="action in row.actionItems" :key="action.action" color="primary">
                                {{ behaviorActionText(action) }}
                              </Tag>
                            </template>
                          </Table>
                          <div v-else class="analysis-result-empty">{{ analysisResultText(item) }}</div>
                        </template>

                        <template v-else-if="item.activityTitle === 'SECURITY_RULE'">
                          <div v-if="analysisRuleResults.length" class="analysis-rule-toolbar">
                            <Checkbox v-model="showCheckedOnlyError">{{ $t('jin-xian-shi-yan-zhong') }}</Checkbox>
                          </div>
                          <div v-if="checkRoleResultList().length" class="validation-content">
                            <div v-for="(rule, index) in checkRoleResultList()" :key="index" class="rule-item">
                              <div class="rule-header">
                                <Tag :color="rule.ruleLevel === 'SUGGEST' ? 'warning' : 'error'" class="rule-level">
                                  {{ RULE_WARN_LEVEL[rule.ruleLevel] }}
                                </Tag>
                                <span class="rule-name">{{ rule.name }}</span>
                                <div v-if="rule.lines && rule.lines.length" class="rule-lines">
                                  <span class="lines-label">{{ $t('wei-zhi-0') }}:</span>
                                  <span v-for="line in rule.lines" :key="line" class="lines-content">{{ line }}</span>
                                  <span v-if="rule.hitCount > rule.lines.length" class="lines-content">
                                    {{ $t('ticket-rule-location-total', { count: rule.hitCount }) }}
                                  </span>
                                </div>
                              </div>
                              <div class="rule-desc">{{ rule.desc }}</div>
                            </div>
                          </div>
                          <div v-else class="analysis-result-empty">
                            {{
                              analysisRuleResults.length && showCheckedOnlyError ? $t('ticket-analysis-security-passed') : analysisResultText(item)
                            }}
                          </div>
                        </template>

                        <div v-else class="analysis-result-empty">{{ analysisResultText(item) }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>

          <template v-else-if="selectedTicketStep.stage === 'EXECUTION' && autoExec && ['DM_QUERY', 'DM_CHANGE'].includes(ticketType)">
            <div class="ticket-execution-detail">
              <div :class="['analysis-summary-item', 'ticket-execution-card', { 'is-expanded': taskExecutionExpanded }]">
                <div class="analysis-summary-row ticket-execution-summary-row">
                  <button
                    type="button"
                    :class="['analysis-summary-row__expand', { 'is-expanded': taskExecutionExpanded }]"
                    :aria-label="taskExecutionExpanded ? $t('ticket-collapse-details') : $t('ticket-expand-details')"
                    :aria-expanded="taskExecutionExpanded"
                    aria-controls="ticket-execution-result"
                    @click="taskExecutionExpanded = !taskExecutionExpanded"
                  >
                    <Icon type="ios-arrow-forward" />
                  </button>
                  <span class="analysis-summary-row__icon ticket-execution-summary-row__icon">
                    <Icon type="ios-play-outline" />
                  </span>
                  <strong>{{ $t('ren-wu-zhi-hang') }}</strong>
                  <span class="ticket-execution-status-wrap">
                    <Poptip v-if="!autoExecJobInfo.normal" :content="autoExecJobInfo.message" trigger="hover">
                      <Icon type="ios-alert-outline" />
                    </Poptip>
                    <span :class="['ticket-execution-status', `is-${autoExecJobInfo.status || 'INIT'}`]">
                      {{ AUTO_EXEC_JOB_STATUS_I18N[autoExecJobInfo.status] }}
                    </span>
                  </span>
                  <div class="ticket-execution-context">
                    <span v-if="autoExecJobInfo.execTime">{{ $t('ji-hua-zhi-hang-shi-jian') }} {{ autoExecJobInfo.execTime }}</span>
                    <span v-if="autoExecJobInfo.workerIp">{{ $t('ji-qi-ip-0') }} {{ autoExecJobInfo.workerIp }}</span>
                    <span v-if="autoExecJobInfo.workerStatus">{{ $t('ji-qi-zhuang-tai-0') }} {{ autoExecJobInfo.workerStatus }}</span>
                  </div>
                  <div class="ticket-execution-actions">
                    <Button v-if="autoExecJobInfo.canEnd" type="text" size="small" @click="handleShowEndAutoExecJobModal">
                      {{ $t('zhong-zhi') }}
                    </Button>
                    <Button v-if="autoExecJobInfo.canPause" type="text" size="small" @click="handleShowStopAutoExecJobModal">
                      {{ $t('zan-ting') }}
                    </Button>
                    <Button v-if="autoExecJobInfo.canRestart" type="text" size="small" @click="handleShowRetryAutoExecJobModal">
                      {{ $t('hui-fu') }}
                    </Button>
                    <Button v-if="autoExecJobInfo.canRetry" type="text" size="small" @click="handleShowRetryAutoExecJobModal">
                      {{ $t('zhong-shi') }}
                    </Button>
                    <Button type="text" size="small" @click="handleAutoExecLog(null)">
                      <Icon type="ios-list-box-outline" />
                      {{ $t('tiao-du-ri-zhi') }}
                    </Button>
                    <Button type="text" size="small" @click="handleRefreshTaskList">
                      <Icon type="md-refresh" />
                      {{ $t('shua-xin') }}
                    </Button>
                  </div>
                </div>

                <div
                  id="ticket-execution-result"
                  :class="['analysis-result-collapse', { 'is-expanded': taskExecutionExpanded }]"
                  :aria-hidden="!taskExecutionExpanded"
                  :inert="!taskExecutionExpanded"
                >
                  <div class="analysis-result-collapse__content">
                    <div class="analysis-result-details ticket-execution-body">
                      <div class="ticket-execution-table">
                        <Table :columns="autoExecTaskColumns" :data="autoExecTaskList" border size="small">
                          <template #status="{ row }">
                            <span :class="['ticket-task-status', `is-${row.status}`]">
                              {{ AUTO_EXEC_TASK_STATUS_I18N[row.status] }}
                            </span>
                          </template>
                          <template #action="{ row }">
                            <Button type="text" size="small" @click="handleAutoExecSQL(row)">
                              {{ $t('cha-kan') }}
                            </Button>
                            <Button type="text" size="small" @click="handleAutoExecLog(row)">
                              {{ $t('ri-zhi') }}
                            </Button>
                            <Button v-if="row.canSkip" type="text" size="small" @click="handleShowSkipAutoExecTaskModal(row)">
                              {{ $t('tiao-guo') }}
                            </Button>
                            <Button v-if="row.canCancelSkip" type="text" size="small" @click="handleShowContinueAutoExecTaskModal(row)">
                              {{ $t('qu-xiao-tiao-guo') }}
                            </Button>
                          </template>
                        </Table>
                      </div>
                      <div class="ticket-execution-pagination">
                        <Page v-model="page" :page-size="pageSize" :total="total" @on-change="handleTaskPageChange" size="small" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>

          <div v-else-if="selectedTicketStep.activities.length" class="ticket-activity-list">
            <div
              class="ticket-activity-row"
              v-for="activity in selectedTicketStep.activities"
              :key="activity.processActivityId || activity.activityTitle"
            >
              <div>
                <span>{{ $t('shen-pi-jie-dian') }}</span>
                <strong>{{ activity.activityTitle }}</strong>
              </div>
              <div>
                <span>{{ $t('chu-li-ren') }}</span>
                <strong>{{ activity.approvalUserList && activity.approvalUserList.length ? activity.approvalUserList.join(', ') : '-' }}</strong>
              </div>
              <div>
                <span>{{ $t('wan-cheng-shi-jian') }}</span>
                <strong>{{ activity.finishTime || '-' }}</strong>
              </div>
              <span :class="['analysis-item-status', analysisStatusClass(activity.activityStatus)]">
                {{ activityStatus[activity.activityStatus] }}
              </span>
            </div>
          </div>
        </div>
      </section>

      <section v-if="ticketType === 'DM_QUERY' || ticketType === 'DM_CHANGE'" class="page-section ticket-sql-section">
        <div class="ticket-sql-toolbar">
          <div class="ticket-sql-toolbar__meta">
            <div class="page-section__title">{{ $t('sql-nei-rong') }}</div>
            <span class="ticket-sql-file">
              <Icon type="ios-document-outline" />
              <span>{{ ticketDetail.attachmentFileName || $t('ticket-inline-sql') }}</span>
              <span v-if="ticketDetail.contentType === 'ATTACHMENT'">{{ formatFileSize(ticketDetail.attachmentFileSize || 0) }}</span>
            </span>
            <span v-if="ticketDetail.ticketMessage" class="parse-error-msgContent">{{ ticketDetail.ticketMessage }}</span>
          </div>
          <div class="ticket-sql-toolbar__actions">
            <Button type="text" @click="handleShowTicketContentModal">
              <Icon type="ios-eye-outline" />
              {{ $t('ticket-view-content') }}
            </Button>
            <Button type="text" :loading="sqlContentAction === 'copy'" @click="handleCopyTicketSql">
              <Icon type="ios-copy-outline" />
              {{ $t('fu-zhi') }}
            </Button>
            <Button type="text" :loading="sqlContentAction === 'download'" @click="handleDownloadTicketSql">
              <Icon type="ios-download-outline" />
              {{ $t('xia-zai') }}
            </Button>
            <Button v-if="ticketDetail.rollBackSql" type="text" @click="handleShowRollbackSqlModal">
              {{ $t('cha-kan-hui-gun-sql') }}
            </Button>
          </div>
        </div>
        <read-only-editor :text="ticketSqlContent" key="ticket-sql-content" :ds-type="ticketDetail.dataSourceType" />
      </section>
      <section v-if="ticketType === 'DATA_SOURCE_AUTH'" class="page-section ticket-auth-section">
        <div class="page-section__title">{{ $t('gong-dan-nei-rong') }}</div>
        <div v-if="formattedAuths.length" class="ticket-auth-list">
          <article v-for="authItem in formattedAuths" :key="authItem.resId" class="ticket-auth-record">
            <div class="ticket-auth-record__meta">
              <div class="ticket-auth-record__field">
                <span>{{ $t('shu-ju-yuan-shi-li') }}</span>
                <strong class="ticket-auth-record__datasource">
                  <DataSourceIcon v-if="authItem.dataSourceType" :type="authItem.dataSourceType" size="22px" leftMargin="0" />
                  <span>{{ authItem.resInstId }}</span>
                </strong>
              </div>
              <div class="ticket-auth-record__field">
                <span>{{ $t('zi-yuan-lu-jing') }}</span>
                <strong>{{ authItem.resPaths }}</strong>
              </div>
              <div class="ticket-auth-record__field">
                <span>{{ $t('sheng-xiao-shi-jian') }}</span>
                <strong>{{ authItem.effectiveTime }}</strong>
              </div>
            </div>
            <div class="ticket-auth-record__permissions">
              <span class="ticket-auth-record__permissions-title">{{ $t('quan-xian-lie-biao') }}</span>
              <div class="ticket-auth-labels">
                <span v-for="(label, index) in authItem.authLabels" :key="index" class="ticket-auth-label">{{ label }}</span>
                <span v-if="!authItem.authLabels.length">-</span>
              </div>
            </div>
          </article>
        </div>
        <div v-else class="ticket-auth-empty">
          <span class="ticket-auth-empty__icon"><Icon type="ios-key-outline" /></span>
          <span>{{ $t('ticket-auth-empty') }}</span>
        </div>
      </section>
    </div>
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
    <CCModal v-model="showTicketContentModal" :title="$t('gong-dan-nei-rong')" width="80vw" centered :draggable="false" class="responsive-sql-modal">
      <read-only-editor :text="ticketSqlContent" key="ticket-sql-content-modal" :max-height="500" :ds-type="ticketDetail.dataSourceType" />
      <template #footer>
        <Button type="primary" :loading="sqlContentAction === 'copy'" @click="handleCopyTicketSql">
          <Icon type="ios-copy-outline" />
          {{ $t('fu-zhi') }}
        </Button>
        <Button :loading="sqlContentAction === 'download'" @click="handleDownloadTicketSql">
          <Icon type="ios-download-outline" />
          {{ $t('xia-zai') }}
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
      <Form class="ticket-execute-form" :model="confirmInfo.autoExecConfig" label-position="top">
        <FormItem :label="$t('zhi-hang-ce-lve')" prop="autoExecType">
          <RadioGroup class="ticket-execute-mode-options" v-model="confirmInfo.autoExecConfig.autoExecType">
            <Radio label="MANUAL_EXEC">{{ $t('yi-shou-dong-wan-cheng') }}</Radio>
            <Radio label="IMMEDIATE">{{ $t('li-ji-zhi-xing') }}</Radio>
            <Radio label="SPECIFY_TIME">{{ $t('ding-shi-zhi-xing') }}</Radio>
          </RadioGroup>
          <div v-if="confirmInfo.autoExecConfig.autoExecType === 'SPECIFY_TIME'" class="ticket-execute-schedule-row">
            <span class="ticket-execute-schedule-label">{{ $t('zhi-hang-shi-jian') }}</span>
            <DatePicker
              class="ticket-execute-schedule"
              v-model="confirmInfo.autoExecConfig.execTime"
              type="datetime"
              :placeholder="$t('qing-xuan-ze-zhi-hang-shi-jian')"
            />
          </div>
        </FormItem>
        <FormItem
          :label="$t('shi-wu')"
          prop="enableTransactional"
          v-if="!isCk(ticketDetail.dataSourceType) && !isMongoDB(ticketDetail.dataSourceType)"
        >
          <div class="ticket-execute-transaction">
            <i-switch
              v-model="confirmInfo.autoExecConfig.enableTransactional"
              size="large"
              :disabled="confirmInfo.autoExecConfig.autoExecType === 'MANUAL_EXEC'"
            >
              <template #open>{{ $t('kai-qi-0') }}</template>
              <template #close>{{ $t('guan-bi') }}</template>
            </i-switch>
            <span>{{ $t('ru-guo-sql-yu-ju-zhong-cun-zai-fei-dml-yu-ju-ke-neng-hui-bei-fen-wei-duo-ge-shi-wu-zhi-hang') }}</span>
          </div>
        </FormItem>
        <FormItem :label="$t('bei-zhu')">
          <a-textarea class="ticket-execute-comment" v-model:value="confirmInfo.comment" :rows="3" />
        </FormItem>
      </Form>
      <template #footer>
        <Button
          type="primary"
          :loading="confirmSubmitting"
          :disabled="confirmSubmitting"
          @click="handleFinishTicket"
          v-if="confirmInfo.autoExecConfig.autoExecType === 'MANUAL_EXEC'"
        >
          {{ $t('jie-shu-gong-dan') }}
        </Button>
        <Button type="primary" :loading="confirmSubmitting" :disabled="confirmSubmitting" @click="handleConfirmTicket" v-else>
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
    <CCModal v-model="showAutoExecTaskSQLModal" :title="$t('sql-yu-ju')" width="80vw" centered :draggable="false" class="responsive-sql-modal">
      <div class="responsive-sql-modal-editor">
        <read-only-editor :text="selectedAutoExecTaskSql" key="auto-exec-task-sql" :ds-type="ticketDetail.dataSourceType" />
      </div>
      <template #footer>
        <Button :disabled="!canViewPreviousAutoExecTask || autoExecTaskSqlLoading" @click="handleSwitchAutoExecSQL(-1)">
          <Icon type="ios-arrow-back" />
          {{ $t('ticket-previous-item') }}
        </Button>
        <Button :disabled="!canViewNextAutoExecTask || autoExecTaskSqlLoading" @click="handleSwitchAutoExecSQL(1)">
          {{ $t('ticket-next-item') }}
          <Icon type="ios-arrow-forward" />
        </Button>
        <Button type="primary" @click="copyText(selectedAutoExecTaskSql)">{{ $t('fu-zhi-sql') }}</Button>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
      </template>
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
import appLogger from '@/utils/logger';
import { mapState } from 'vuex';
import { TICKET_PROCESS_STATUS } from '@/const';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import copyMixin from '@/mixins/copyMixin';
import { isCk, isMongoDB, RULE_WARN_LEVEL } from '@/utils';

const TICKET_AUTO_REFRESH_INTERVAL_MS = 5000;
const TICKET_TERMINAL_STATUSES = new Set(['REJECTED', 'FINISHED', 'CLOSED', 'CANCELED', 'FAILED']);

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

const AUTO_EXEC_TASK_STATUS_I18N = {
  WAIT_EXEC: '待执行',
  EXECUTING: '执行中',
  WAIT_CONFIRM: '等待确认',
  FAILED: '失败',
  FINISH: '完成',
  ROLLBACK: '回滚',
  CANCELED: '取消'
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
      analysisBehaviors: [],
      analysisSqlCount: null,
      analysisResultTab: 'BEHAVIOR_ANALYSIS',
      recognizedContentColumns: [
        {
          title: this.$t('zi-yuan-lei-xing'),
          slot: 'resourceType',
          width: 180
        },
        {
          title: this.$t('zi-yuan-lu-jing'),
          key: 'resourcePath'
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'actions',
          width: 320
        }
      ],
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
      selectedAutoExecTaskSql: '',
      autoExecTaskSqlLoading: false,
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
          key: 'execSql',
          ellipsis: true
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
          key: 'execSql',
          ellipsis: true
        },
        {
          title: '操作',
          width: 200,
          fixed: 'right',
          slot: 'action'
        }
      ],
      AUTO_EXEC_JOB_STATUS_I18N,
      AUTO_EXEC_TASK_STATUS_I18N,
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
      showTicketContentModal: false,
      sqlContentAction: '',
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
      ticketSqlContent: '',
      ticketSqlTotalLines: 1,
      ticketSqlContentInitialized: false,
      ticketAutoRefreshActive: false,
      ticketAutoRefreshTimer: null,
      analysisResultsExpanded: false,
      taskExecutionExpanded: true,
      durationNow: Date.now(),
      durationTimer: null,
      TICKET_PROCESS_STATUS,
      loading: false,
      confirmSubmitting: false,
      confirmInfo: {
        autoExecConfig: {}
      },
      autoExecuteRule: {},
      showCancelTicketModal: false,
      selectedStepKey: '',
      ticketStepManuallySelected: false,
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
      authList: []
    };
  },
  async mounted() {
    this.ticketId = this.$route.params.id;
    this.ticketAutoRefreshActive = true;
    await this.getTicketDetail('init');
    this.scheduleTicketAutoRefresh();
    this.durationTimer = window.setInterval(() => {
      this.durationNow = Date.now();
    }, 1000);
  },
  beforeUnmount() {
    this.stopTicketAutoRefresh();
    if (this.durationTimer) {
      window.clearInterval(this.durationTimer);
    }
  },
  computed: {
    ...mapState(['userInfo', 'myAuth']),
    ticketProgressSteps() {
      const steps = [
        {
          key: 'CREATE',
          title: this.$t('chuang-jian'),
          time: this.ticketDetail.gmtCreate,
          startTime: this.ticketDetail.gmtCreate,
          handler: this.ticketDetail.userName,
          statusText: this.TICKET_PROCESS_STATUS.FINISH,
          statusClass: 'finished',
          state: 'finished',
          icon: 'md-checkmark',
          stage: 'CREATE',
          activities: []
        }
      ];
      const processes = this.ticketDetail.ticketProcessVOList || [];
      let activeFound = TICKET_TERMINAL_STATUSES.has(this.ticketDetail.ticketStatus);
      processes.forEach((process) => {
        let state = 'pending';
        let icon = 'md-time';
        if (process.ticketProcessStatus === 'FINISH') {
          state = 'finished';
          icon = 'md-checkmark';
        } else if (['REJECT', 'FAIL', 'CLOSED'].includes(process.ticketProcessStatus)) {
          state = 'failed';
          icon = 'md-close';
          activeFound = true;
        } else if (!activeFound) {
          state = 'active';
          icon = 'md-time';
          activeFound = true;
        }
        let statusText = this.TICKET_PROCESS_STATUS[process.ticketProcessStatus] || '-';
        if (process.ticketStage === 'EXPLAIN' && state !== 'finished') {
          statusText = this.analysisProcessStatusText;
        }
        if (state === 'active' && process.ticketStage === 'APPROVAL') {
          statusText = this.$t('deng-dai-shen-pi');
        }
        let startTime = process.gmtCreate;
        const activityStartTimes = (process.activityList || []).map((activity) => activity.startTime).filter(Boolean);
        if (activityStartTimes.length) {
          startTime = activityStartTimes[0];
        }
        steps.push({
          key: String(process.ticketProcessId),
          title: process.ticketStageTitle,
          time: process.finishTime || startTime,
          startTime,
          handler: state === 'pending' ? '' : process.execUserName,
          statusText,
          statusClass: state === 'pending' ? 'init' : state,
          state,
          icon,
          stage: process.ticketStage,
          activities: process.activityList || [],
          process
        });
      });
      return steps;
    },
    selectedTicketStep() {
      return this.ticketProgressSteps.find((step) => step.key === this.selectedStepKey) || this.ticketProgressSteps[0];
    },
    selectedTicketRejectionReason() {
      if (this.selectedTicketStep?.stage !== 'APPROVAL' || this.selectedTicketStep.process?.ticketProcessStatus !== 'REJECT') {
        return '';
      }
      return this.ticketDetail.approComment?.trim() || '';
    },
    formattedAuths() {
      return this.authList.map((authItem) => {
        let effectiveTime = this.$t('yong-jiu');
        if (authItem.startTime && authItem.endTime) {
          effectiveTime = `${authItem.startTime} - ${authItem.endTime}`;
        } else if (authItem.startTime) {
          effectiveTime = `${this.$t('cong-0')} ${authItem.startTime} ${this.$t('kai-shi-zhi-yong-jiu')}`;
        } else if (authItem.endTime) {
          effectiveTime = `${this.$t('cong-shen-pi-tong-guo-dao')} ${authItem.endTime} ${this.$t('jie-shu')}`;
        }
        return {
          resId: authItem.resId,
          resInstId: authItem.resInstId,
          dataSourceType: authItem.dataSourceType,
          resPaths: `/${authItem.resPaths.join(' / ')}`,
          authLabels: authItem.authLabels,
          effectiveTime
        };
      });
    },
    recognizedBehaviorRows() {
      const behaviorItem = this.analysisItems.find((item) => item.activityTitle === 'BEHAVIOR_ANALYSIS');
      const behaviors = behaviorItem?.behaviors ?? this.analysisBehaviors;
      return [...behaviors]
        .map((behavior) => ({
          ...behavior,
          actionItems: Object.keys(behavior.actionCounts || {}).length
            ? Object.entries(behavior.actionCounts).map(([action, count]) => ({ action, count }))
            : [...(behavior.actions || [])].sort().map((action) => ({ action, count: null }))
        }))
        .sort((left, right) => {
          const typeCompare = (left.resourceType || '').localeCompare(right.resourceType || '');
          return typeCompare || (left.resourcePath || '').localeCompare(right.resourcePath || '');
        });
    },
    analysisProcess() {
      return (this.ticketDetail.ticketProcessVOList || []).find((item) => item.ticketStage === 'EXPLAIN');
    },
    analysisItems() {
      return [...(this.analysisProcess?.activityList || [])].sort(
        (left, right) => (left.displayOrder ?? Number.MAX_SAFE_INTEGER) - (right.displayOrder ?? Number.MAX_SAFE_INTEGER)
      );
    },
    analysisRuleResults() {
      const ruleItem = this.analysisItems.find((item) => item.activityTitle === 'SECURITY_RULE');
      return ruleItem?.ruleResults ?? this.noPassedRuleList;
    },
    analysisRunningCount() {
      return this.analysisItems.filter((item) => item.activityStatus === 'RUNNING').length;
    },
    analysisProcessStatusText() {
      if (this.analysisItems.length > 0 && this.analysisItems.every((item) => item.activityStatus === 'COMPLETED')) {
        return this.$t('ticket-analysis-complete');
      }
      if (this.analysisRunningCount > 0) {
        return this.$t('ticket-analysis-running-count', { count: this.analysisRunningCount });
      }
      if (this.analysisItems.some((item) => item.activityStatus === 'REFUSE')) {
        return this.$t('ticket-analysis-failed');
      }
      return this.$t('ticket-analysis-waiting');
    },
    hasError() {
      return this.analysisRuleResults.some((rule) => rule.ruleLevel !== 'SUGGEST');
    },
    selectedAutoExecTaskIndex() {
      return this.autoExecTaskList.findIndex((task) => task.taskId === this.selectedAutoExecTask.taskId);
    },
    canViewPreviousAutoExecTask() {
      if (this.selectedAutoExecTaskIndex < 0) {
        return false;
      }
      return (this.page - 1) * this.pageSize + this.selectedAutoExecTaskIndex > 0;
    },
    canViewNextAutoExecTask() {
      if (this.selectedAutoExecTaskIndex < 0) {
        return false;
      }
      return (this.page - 1) * this.pageSize + this.selectedAutoExecTaskIndex < this.total - 1;
    }
  },
  watch: {
    analysisItems(items) {
      if (!items.some((item) => item.activityTitle === this.analysisResultTab)) {
        this.analysisResultTab = items[0]?.activityTitle || '';
      }
    }
  },
  methods: {
    isCk,
    isMongoDB,
    selectTicketStep(step) {
      this.ticketStepManuallySelected = true;
      this.selectedStepKey = step.key;
    },
    isAnalysisResultExpanded(activityTitle) {
      return this.analysisResultsExpanded && this.analysisResultTab === activityTitle;
    },
    toggleAnalysisResult(activityTitle) {
      if (this.isAnalysisResultExpanded(activityTitle)) {
        this.analysisResultsExpanded = false;
        return;
      }
      this.analysisResultTab = activityTitle;
      this.analysisResultsExpanded = true;
    },
    behaviorStatementCount(item) {
      return item.statementCount ?? this.analysisSqlCount;
    },
    behaviorSummaryText(item) {
      const values = {
        statementCount: this.behaviorStatementCount(item) || 0,
        objectCount: this.recognizedBehaviorRows.length,
        behaviorCount: item.behaviorCount || 0
      };
      return item.behaviorCount == null
        ? this.$t('ticket-analysis-behavior-summary-legacy', values)
        : this.$t('ticket-analysis-behavior-summary', values);
    },
    behaviorActionText(action) {
      return action.count == null ? action.action : this.$t('ticket-analysis-action-summary', action);
    },
    analysisTypeText(type) {
      const keyMap = {
        SQL_RECOGNITION: 'ticket-analysis-sql-recognition',
        BEHAVIOR_ANALYSIS: 'ticket-analysis-behavior',
        SECURITY_RULE: 'ticket-analysis-security-rule'
      };
      return this.$t(keyMap[type] || type);
    },
    analysisStatusText(status) {
      const keyMap = {
        NEW: 'ticket-analysis-waiting',
        RUNNING: 'ticket-analysis-running',
        COMPLETED: 'ticket-analysis-complete',
        REFUSE: 'ticket-analysis-failed'
      };
      return this.$t(keyMap[status] || status);
    },
    analysisStatusClass(status) {
      const classMap = {
        NEW: 'init',
        RUNNING: 'running',
        COMPLETED: 'finished',
        REFUSE: 'failed',
        CANCELED: 'failed'
      };
      return `status-${classMap[status] || 'init'}`;
    },
    analysisResultText(item) {
      if (item.activityStatus === 'REFUSE') {
        return item.remark || this.$t('ticket-analysis-failed');
      }
      if (item.activityStatus === 'NEW') {
        return '--';
      }
      if (item.activityStatus === 'RUNNING') {
        if (item.totalBytes > 0 && item.processedBytes != null) {
          const percentage = Math.min(100, Math.floor((item.processedBytes * 100) / item.totalBytes));
          return this.$t('ticket-analysis-read-progress', {
            processed: this.formatFileSize(item.processedBytes),
            total: this.formatFileSize(item.totalBytes),
            percentage,
            count: item.processedCount || 0
          });
        }
        return item.processedCount == null
          ? this.$t('ticket-analysis-running')
          : this.$t('ticket-analysis-processed-count', { count: item.processedCount });
      }
      if (item.activityTitle === 'SQL_RECOGNITION' && item.statementCount != null) {
        return this.$t('ticket-analysis-sql-result', { count: item.statementCount });
      }
      if (item.activityTitle === 'BEHAVIOR_ANALYSIS' && item.objectCount != null) {
        return this.$t('ticket-analysis-behavior-summary-legacy', {
          statementCount: item.statementCount ?? this.analysisSqlCount ?? 0,
          objectCount: item.objectCount
        });
      }
      if (item.activityTitle === 'SECURITY_RULE' && item.ruleCount != null) {
        return item.ruleCount === 0
          ? this.$t('ticket-analysis-security-passed')
          : this.$t('ticket-analysis-security-result', { count: item.ruleCount });
      }
      return '--';
    },
    analysisElapsed(item) {
      if (!item.startTimeUtc) {
        return '--';
      }
      const end = item.finishTimeUtc || this.durationNow;
      return this.formatElapsed(end - item.startTimeUtc);
    },
    formatElapsed(milliseconds) {
      const totalSeconds = Math.max(0, Math.floor(milliseconds / 1000));
      const hours = Math.floor(totalSeconds / 3600);
      const minutes = Math.floor((totalSeconds % 3600) / 60);
      const seconds = totalSeconds % 60;
      return [hours, minutes, seconds].map((value) => String(value).padStart(2, '0')).join(':');
    },
    async loadTicketSqlContent() {
      const res = await this.$services.dmTicketPreviewApprovalSql({
        data: {
          ticketId: this.ticketId,
          startLine: 1,
          lineCount: 1000
        }
      });
      if (res.success) {
        this.ticketSqlContent = res.data?.content || '';
        this.ticketSqlTotalLines = res.data?.totalLines || 1;
      }
    },
    formatFileSize(size) {
      if (size < 1024) {
        return `${size} B`;
      }
      if (size < 1024 * 1024) {
        return `${(size / 1024).toFixed(1)} KB`;
      }
      return `${(size / 1024 / 1024).toFixed(1)} MB`;
    },
    stopTicketAutoRefresh() {
      this.ticketAutoRefreshActive = false;
      if (this.ticketAutoRefreshTimer) {
        window.clearTimeout(this.ticketAutoRefreshTimer);
        this.ticketAutoRefreshTimer = null;
      }
    },
    scheduleTicketAutoRefresh() {
      if (this.ticketAutoRefreshTimer) {
        window.clearTimeout(this.ticketAutoRefreshTimer);
        this.ticketAutoRefreshTimer = null;
      }

      if (!this.ticketAutoRefreshActive || TICKET_TERMINAL_STATUSES.has(this.ticketDetail.ticketStatus)) {
        return;
      }

      this.ticketAutoRefreshTimer = window.setTimeout(() => {
        this.refreshTicketAutomatically();
      }, TICKET_AUTO_REFRESH_INTERVAL_MS);
    },
    async refreshTicketAutomatically() {
      this.ticketAutoRefreshTimer = null;
      if (document.hidden || this.loading) {
        this.scheduleTicketAutoRefresh();
        return;
      }

      try {
        await this.getTicketDetail('auto');
      } finally {
        this.scheduleTicketAutoRefresh();
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
    async handleAutoExecSQL(task) {
      this.autoExecTaskSqlLoading = true;
      try {
        const res = await this.$services.dmTicketQueryAutoExecTaskSql({
          data: {
            ticketId: this.ticketId,
            taskId: task.taskId
          }
        });
        if (res.success) {
          this.selectedAutoExecTask = task;
          this.selectedAutoExecTaskSql = res.data || '';
          this.showAutoExecTaskSQLModal = true;
        }
      } finally {
        this.autoExecTaskSqlLoading = false;
      }
    },
    async handleSwitchAutoExecSQL(direction) {
      let targetIndex = this.selectedAutoExecTaskIndex + direction;
      if (targetIndex < 0 || targetIndex >= this.autoExecTaskList.length) {
        const loaded = await this.queryAutoExecTaskList(this.page + direction);
        if (!loaded) {
          return;
        }
        targetIndex = direction < 0 ? this.autoExecTaskList.length - 1 : 0;
      }
      const targetTask = this.autoExecTaskList[targetIndex];
      if (targetTask) {
        await this.handleAutoExecSQL(targetTask);
      }
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
    async queryAutoExecTaskList(targetPage = this.page) {
      const res = await this.$services.dmTicketQueryAutoExecTaskList({
        data: {
          ticketId: this.ticketId,
          page: {
            pageNum: targetPage,
            pageSize: this.pageSize
          }
        }
      });

      if (res.success) {
        this.autoExecTaskList = res.data.records;
        this.page = res.data.current;
        this.pageSize = res.data.size;
        this.total = res.data.total;
        return true;
      }
      return false;
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
          retryWaitTime: 111, // Unit seconds
          retryCount: 2, // Number of retries
          autoExecType: 'IMMEDIATE', // [IMMEDITE, SPECIFY TIME]
          execTime: new Date() // Scheduled implementation time
        }
      };
      this.showAutoExecuteModal = true;
    },
    handleShowRollbackSqlModal() {
      this.showRollbackSqlModal = true;
    },
    handleShowTicketContentModal() {
      this.showTicketContentModal = true;
    },
    async loadFullTicketSql() {
      const chunks = [];
      let startLine = 1;
      let totalLines = Math.max(1, this.ticketSqlTotalLines);
      while (startLine <= totalLines) {
        const res = await this.$services.dmTicketPreviewApprovalSql({
          data: {
            ticketId: this.ticketId,
            startLine,
            lineCount: 1000
          }
        });
        if (!res.success) {
          return '';
        }
        chunks.push(res.data?.content || '');
        totalLines = Math.max(1, res.data?.totalLines || totalLines);
        startLine += 1000;
      }
      return chunks.join('\n');
    },
    async handleCopyTicketSql() {
      this.sqlContentAction = 'copy';
      try {
        const content = await this.loadFullTicketSql();
        if (content) {
          await this.copyText(content);
        }
      } finally {
        this.sqlContentAction = '';
      }
    },
    async handleDownloadTicketSql() {
      this.sqlContentAction = 'download';
      try {
        const content = await this.loadFullTicketSql();
        if (!content) {
          return;
        }
        const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = this.ticketDetail.attachmentFileName || `ticket-${this.ticketDetail.id || this.ticketId}.sql`;
        link.click();
        URL.revokeObjectURL(url);
      } finally {
        this.sqlContentAction = '';
      }
    },
    handleShowCloseTicketModal() {
      this.showCloseTicketModal = true;
    },
    async getTicketDetail(type) {
      const showLoading = type !== 'auto';
      if (showLoading) {
        this.loading = true;
      }
      const data = {
        ticketId: this.ticketId,
        refreshCache: type === 'refresh'
      };
      const res = await this.$services.rdpTicketQueryTicketBaseInfo({ data, modal: showLoading });

      if (showLoading) {
        this.loading = false;
      }
      if (res.success) {
        this.ticketType = res.data?.approBiz;
        this.ticketDetail = res.data;
        this.ticketDetail.ticketProcessVOList.forEach((item) => {
          item.execUserName = '';
          item.execMsg = '';
          if (item.stageContext) {
            const stageContext = JSON.parse(item.stageContext);
            item.execUserName = stageContext.execUserName ? stageContext.execUserName.join(',') : '';
            item.execUserNameList = stageContext.execUserName;
            item.execMsg = stageContext.execMsg;
          }
          if (!item.execUserName && ['EXPLAIN', 'EXECUTION'].includes(item.ticketStage)) {
            const systemHandler = this.$t('clouddm-system-handler');
            item.execUserName = systemHandler;
            item.execUserNameList = [systemHandler];
          }
        });
        const processSteps = this.ticketDetail.ticketProcessVOList || [];
        const validStepKeys = new Set(['CREATE', ...processSteps.map((process) => String(process.ticketProcessId))]);
        if (!this.ticketStepManuallySelected || !validStepKeys.has(this.selectedStepKey)) {
          const currentStep = this.ticketProgressSteps.find((step) => ['active', 'failed'].includes(step.state));
          const defaultStep = currentStep || this.ticketProgressSteps[this.ticketProgressSteps.length - 1];
          this.selectedStepKey = defaultStep?.key || 'CREATE';
          this.ticketStepManuallySelected = false;
        }

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
            const initializeSqlContent = !this.ticketSqlContentInitialized;
            const resQuery = await this.$services.dmTicketQueryQueryTicketDetail({
              data: {
                ticketId: this.ticketId
              }
            });
            if (resQuery.success) {
              this.noPassedRuleList = resQuery.data.checkedList || [];
              this.analysisBehaviors = resQuery.data.behaviors || [];
              this.analysisSqlCount = resQuery.data.totalCount ?? null;
              this.autoExec = resQuery.data.autoExec;
              if (resQuery.data?.autoExec) {
                await this.queryAutoExecJobInfo();
                await this.queryAutoExecTaskList();
              }
              this.ticketDetail.ticketMessage = resQuery.data?.ticketMessage || '';
              this.ticketDetail.rollBackSql = resQuery.data?.rollBackSql || '';
              this.ticketDetail.contentType = resQuery.data?.contentType || 'INLINE';
              this.ticketDetail.attachmentId = resQuery.data?.attachmentId;
              this.ticketDetail.attachmentFileName = resQuery.data?.attachmentFileName || '';
              this.ticketDetail.attachmentFileSize = resQuery.data?.attachmentFileSize || 0;
              if (initializeSqlContent) {
                await this.loadTicketSqlContent();
                this.ticketSqlContentInitialized = true;
              }
            }
            break;
          default:
            break;
        }
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
      if (this.confirmSubmitting) {
        return;
      }
      this.confirmSubmitting = true;
      appLogger.debug(this.confirmInfo.confirmActionType);
      try {
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
      } finally {
        this.confirmSubmitting = false;
      }
    },
    async handleFinishTicket() {
      if (this.confirmSubmitting) {
        return;
      }
      this.confirmSubmitting = true;
      this.confirmInfo.confirmActionType = 'CONFIRM';
      try {
        const data = { ...this.confirmInfo };
        data.autoExecConfig.execTime = null;
        const res = await this.$services.dmTicketConfirm({ data });
        if (res.success) {
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
          this.handleCloseModal();
          await this.getTicketDetail();
        }
      } finally {
        this.confirmSubmitting = false;
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
      this.showTicketContentModal = false;
      this.showManualExecuteModal = false;
      this.showCloseTicketModal = false;
      this.showAutoExecuteModal = false;
      this.showAutoExecJobLogModal = false;
      this.showAutoExecTaskLogModal = false;
      this.showAutoExecTaskSQLModal = false;
      this.selectedAutoExecTaskSql = '';
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
        return this.analysisRuleResults;
      } else {
        return this.analysisRuleResults.filter((rule) => rule.ruleLevel !== 'SUGGEST');
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
  position: relative;
  width: 100%;
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  padding: 20px;
  overflow-x: hidden;
  overflow-y: auto;

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

    &.with-analysis-summary {
      padding-right: 500px;
    }

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

  .ticket-analysis-summary {
    position: absolute;
    top: 18px;
    right: 132px;
    width: 300px;
    padding-left: 24px;
    border-left: 1px solid #e8eaec;
    font-size: 12px;

    > div {
      display: grid;
      grid-template-columns: 76px 1fr;
      gap: 8px;
      margin-bottom: 10px;
    }

    span {
      color: @icon-color;
    }

    strong {
      font-weight: 500;
    }
  }

  .ticket-status-total.analysis-status {
    color: #1677ff;
    border-color: #91caff;
    background: #e6f4ff;
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

    .analysis-result-empty,
    .analysis-result-overview {
      padding: 24px;
      color: @icon-color;
      text-align: center;
    }

    .analysis-result-overview {
      color: @text-color;
      font-size: 14px;
    }

    .analysis-result-tabs {
      :deep(.ivu-tabs-bar) {
        margin-bottom: 0;
      }

      :deep(.ivu-tabs-tabpane) {
        min-height: 72px;
      }
    }

    .analysis-rule-toolbar {
      display: flex;
      justify-content: flex-end;
      padding: 12px 16px 0;
    }

    .collapsible-card-title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
    }

    .ticket-content-title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      min-width: 0;
    }

    .ticket-content-title-main,
    .ticket-attachment-meta {
      display: flex;
      align-items: center;
    }

    .ticket-attachment-meta {
      gap: 8px;
      margin-left: 16px;
      color: @icon-color;
      font-size: 12px;
      font-weight: 400;
    }
  }

  .compact-ticket-content {
    :deep(.ivu-card-body) {
      padding: 0;
    }

    .ticket-content-entry {
      display: flex;
      align-items: center;
      justify-content: space-between;
      min-height: 52px;
      padding: 0 20px;
      cursor: pointer;

      &:hover {
        background: #f8f8f9;
      }

      &:focus-visible {
        outline: 2px solid #57a3f3;
        outline-offset: -2px;
      }
    }

    .ticket-content-title-main {
      min-width: 0;

      .parse-error-msgContent {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .ticket-content-entry-meta {
      display: flex;
      flex-shrink: 0;
      align-items: center;
      gap: 8px;
      margin-left: 16px;
      color: @icon-color;
      font-size: 12px;
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

      &.analysis-step {
        flex-direction: column;
        align-items: stretch;

        &.current-step {
          box-shadow: none;
          border: 1px solid #d6e4ff;
          background: #f7faff;
        }
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

    .analysis-toggle {
      margin-left: auto;
      padding: 0 4px;
      color: @font-color;
    }

    .analysis-detail-list {
      width: 75%;
      margin: 4px 0 0 25%;
    }

    .analysis-detail-row {
      display: grid;
      grid-template-columns: 22% 16% 1fr 100px;
      align-items: center;
      min-height: 42px;
      border-top: 1px solid #e8eaec;

      > div {
        padding: 8px 12px;
      }
    }

    .analysis-detail-header {
      min-height: 36px;
      color: @icon-color;
      font-weight: 500;
    }

    .analysis-item-status {
      display: inline-block;
      padding: 2px 8px;
      border-radius: 3px;
      font-size: 12px;

      &.status-finished {
        color: #389e0d;
        background: #f6ffed;
      }

      &.status-running {
        color: #1677ff;
        background: #e6f4ff;
      }

      &.status-failed {
        color: #cf1322;
        background: #fff1f0;
      }

      &.status-init {
        color: #8c8c8c;
        background: #f5f5f5;
      }
    }

    .analysis-result {
      min-width: 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
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

.responsive-sql-modal-editor {
  position: relative;
  height: clamp(320px, 62vh, 720px);
  overflow: hidden;

  :deep(.read-only-editor-wrapper),
  :deep(.read-only-editor) {
    height: 100% !important;
  }
}

.ticket-execute-form {
  padding: 8px 4px 12px;

  :deep(.ivu-form-item) {
    margin-bottom: 28px;
  }

  :deep(.ivu-form-item:last-child) {
    margin-bottom: 0;
  }

  :deep(.ivu-form-item-label) {
    float: none;
    display: block;
    width: auto !important;
    margin-bottom: 12px;
    padding: 0;
    color: var(--text-secondary);
    font-size: 14px;
    font-weight: 500;
    line-height: 22px;
    text-align: left;
  }

  :deep(.ivu-form-item-content) {
    margin-left: 0 !important;
    line-height: 22px;
  }

  :deep(.ivu-input) {
    padding: 10px 12px;
    line-height: 22px;
  }

  :deep(.ticket-execute-comment) {
    min-height: 88px;
    padding: 10px 12px;
    line-height: 22px;
    resize: vertical;
  }
}

.ticket-execute-mode-options {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 32px;

  :deep(.ivu-radio-wrapper) {
    margin-right: 0;
    color: var(--text-primary);
  }
}

.ticket-execute-schedule-row {
  display: flex;
  align-items: flex-start;
  flex-direction: column;
  gap: 8px;
  margin-top: 16px;
}

.ticket-execute-schedule-label {
  flex: none;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  line-height: 22px;
}

.ticket-execute-schedule {
  width: 100%;
  max-width: 320px;
}

.ticket-execute-transaction {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 14px 16px;
  border-radius: 8px;
  color: var(--text-secondary);
  background: var(--bg-secondary);
  line-height: 22px;

  :deep(.ivu-switch) {
    flex: none;
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
  padding: 16px;

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

.ticket-detail-page {
  width: 100%;
  height: 100%;
  min-height: 0;
  color: var(--text-primary);
  background: var(--bg-card);
  overflow: hidden;
}

.ticket-detail-page .ticket-detail-container {
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 14px;
  width: 100%;
  height: 100%;
  min-height: 0;
  padding: 16px 24px 24px;
  overflow-x: hidden;
  overflow-y: auto;
  scrollbar-gutter: stable;
}

.ticket-detail-page .page-section {
  flex: none;
  margin: 0;
}

.ticket-info-section,
.ticket-progress-card,
.ticket-auth-section {
  padding: 20px 24px;
  border: 1px solid var(--border-primary);
  border-radius: 8px;
  background: var(--bg-card);
  box-shadow: 0 2px 8px rgba(31, 41, 55, 0.05);
}

.ticket-auth-list {
  margin-top: 18px;
  border-top: 1px solid var(--border-light);
  border-bottom: 1px solid var(--border-light);
}

.ticket-auth-record {
  padding: 18px 0;
}

.ticket-auth-record + .ticket-auth-record {
  border-top: 1px solid var(--border-light);
}

.ticket-auth-record__meta {
  display: grid;
  grid-template-columns: minmax(180px, 0.8fr) minmax(180px, 1fr) minmax(240px, 1.2fr);
  gap: 24px;
}

.ticket-auth-record__field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
}

.ticket-auth-record__field > span,
.ticket-auth-record__permissions-title {
  color: var(--text-secondary);
  font-size: 12px;
  line-height: 18px;
}

.ticket-auth-record__field > strong {
  overflow-wrap: anywhere;
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
  line-height: 22px;
}

.ticket-auth-record__datasource {
  display: flex;
  gap: 8px;
  align-items: center;
}

.ticket-auth-record__permissions {
  display: grid;
  grid-template-columns: 112px minmax(0, 1fr);
  gap: 16px;
  align-items: start;
  margin-top: 18px;
  padding-top: 16px;
  border-top: 1px dashed var(--border-light);
}

.ticket-auth-record__permissions-title {
  padding-top: 3px;
}

.ticket-auth-labels {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.ticket-auth-label {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  padding: 3px 10px;
  border: 1px solid var(--border-light);
  border-radius: 6px;
  color: var(--text-primary);
  background: var(--bg-secondary);
  font-size: 13px;
  line-height: 20px;
}

.ticket-auth-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  min-height: 132px;
  margin-top: 18px;
  border-radius: 8px;
  color: var(--text-secondary);
  background: var(--bg-secondary);
}

.ticket-auth-empty__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  color: var(--primary-color);
  background: color-mix(in srgb, var(--primary-color) 10%, var(--bg-card));
  font-size: 20px;
}

.ticket-overview {
  margin-top: 20px;
}

.ticket-info-section__header,
.ticket-overview__actions,
.ticket-meta-item__value,
.ticket-meta-item__label--with-icon,
.ticket-state-badge,
.ticket-progress,
.ticket-progress-step,
.ticket-progress-step__content,
.analysis-summary-row__elapsed,
.ticket-sql-toolbar,
.ticket-sql-toolbar__meta,
.ticket-sql-toolbar__actions,
.ticket-sql-file {
  display: flex;
  align-items: center;
}

.ticket-info-section__header,
.ticket-sql-toolbar {
  justify-content: space-between;
}

.ticket-info-section__header {
  align-items: flex-start;
  gap: 24px;
}

.ticket-overview__hero {
  padding: 6px 0 22px;
}

.ticket-overview__headline {
  display: flex;
  align-items: center;
  gap: 14px;
  min-width: 0;
}

.ticket-overview__headline h2 {
  min-width: 0;
  margin: 0;
  color: var(--text-primary);
  font-size: 20px;
  font-weight: 600;
  line-height: 30px;
  word-break: break-word;
}

.ticket-overview__actions {
  flex: none;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 8px;
}

.ticket-overview__actions :deep(.ivu-btn) {
  margin: 0;
}

.ticket-state-badge,
.analysis-item-status {
  width: fit-content;
  min-height: 24px;
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  line-height: 20px;
  white-space: nowrap;
}

.ticket-state-badge {
  gap: 5px;
}

.ticket-state-badge--success,
.analysis-item-status.status-finished {
  color: var(--success-color);
  background: color-mix(in srgb, var(--success-color) 10%, var(--bg-card));
}

.ticket-state-badge--running,
.analysis-item-status.status-running,
.analysis-item-status.status-active {
  color: var(--info-color);
  background: color-mix(in srgb, var(--info-color) 10%, var(--bg-card));
}

.ticket-state-badge--error,
.analysis-item-status.status-failed {
  color: var(--error-color);
  background: color-mix(in srgb, var(--error-color) 8%, var(--bg-card));
}

.ticket-state-badge--neutral,
.analysis-item-status.status-init,
.analysis-item-status.status-pending {
  color: var(--text-secondary);
  background: var(--bg-tertiary);
}

.ticket-overview__primary-grid,
.ticket-overview__secondary-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 0;
  padding: 22px 0;
  border-top: 1px solid var(--border-light);
}

.ticket-overview__primary-grid--with-target-database {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.ticket-overview__secondary-grid--with-target-database {
  grid-template-columns: minmax(180px, 1fr) minmax(180px, 1fr) minmax(320px, 2fr);
}

.ticket-overview__secondary-grid {
  padding-bottom: 4px;
}

.ticket-meta-item {
  display: flex;
  flex-direction: column;
  gap: 7px;
  min-width: 0;
  padding: 0 20px;
  border-left: 1px solid var(--border-light);
  color: var(--text-primary);
  font-size: 14px;
}

.ticket-overview__primary-grid .ticket-meta-item:first-child,
.ticket-overview__secondary-grid .ticket-meta-item:first-child {
  padding-left: 0;
  border-left: 0;
}

.ticket-overview__primary-grid .ticket-meta-item:last-child,
.ticket-overview__secondary-grid .ticket-meta-item:last-child {
  padding-right: 0;
}

.ticket-meta-item__label,
.ticket-step-summary__item > span,
.ticket-activity-row > div > span,
.analysis-summary-row__elapsed > span {
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 400;
}

.ticket-meta-item__label--with-icon {
  gap: 7px;
}

.ticket-meta-item__label--with-icon :deep(.ivu-icon) {
  color: var(--primary-color);
  font-size: 17px;
}

.ticket-meta-item strong,
.ticket-step-summary__item strong,
.ticket-activity-row strong {
  color: var(--text-primary);
  font-weight: 600;
}

.ticket-meta-item__value,
.ticket-sql-file {
  gap: 6px;
  min-width: 0;
}

.ticket-meta-item__ticket-id {
  justify-content: flex-start;
}

.ticket-meta-item__copy {
  display: inline-flex;
  flex: none;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  padding: 0;
  border: 0;
  border-radius: 4px;
  color: var(--text-secondary);
  background: transparent;
  cursor: pointer;
}

.ticket-meta-item__copy:hover,
.ticket-meta-item__copy:focus-visible {
  color: var(--primary-color);
  background: color-mix(in srgb, var(--primary-color) 8%, transparent);
}

.ticket-meta-item__copy:focus-visible {
  outline: 2px solid color-mix(in srgb, var(--primary-color) 35%, transparent);
  outline-offset: 1px;
}

.ticket-meta-item__product-icon {
  display: block;
  flex: none;
  width: 20px;
  height: 20px;
  object-fit: contain;
}

.ticket-meta-item__database {
  display: flex;
  align-items: center;
}

.ticket-meta-item__database-icon {
  flex: none;
  line-height: 1;
}

.ticket-meta-item__database > span:last-child {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ticket-meta-item--target-database :deep(.ivu-tooltip-rel) {
  display: block;
  min-width: 0;
  max-width: 100%;
}

.ticket-meta-item--description {
  min-width: 0;
}

.ticket-meta-item__description {
  display: block;
  max-width: 100%;
  line-height: 22px;
  overflow-wrap: anywhere;
}

.ticket-meta-item--description :deep(.ivu-tooltip-rel) {
  display: block;
  max-width: 100%;
}

.ticket-overview__error {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-top: 16px;
  padding: 10px 12px;
  border-radius: 6px;
  color: var(--error-color);
  background: color-mix(in srgb, var(--error-color) 6%, var(--bg-card));
}

.ticket-progress-section {
  margin-top: 18px;
}

.ticket-progress-scroll {
  width: 100%;
  overflow-x: auto;
  scrollbar-width: none;
}

.ticket-progress-scroll::-webkit-scrollbar {
  display: none;
}

.ticket-progress {
  position: relative;
  align-items: flex-start;
  min-width: 960px;
}

.ticket-progress-step {
  position: relative;
  flex: none;
  gap: 10px;
  min-width: 0;
  padding: 3px 0;
  border: 0;
  color: var(--text-secondary);
  background: transparent;
  font: inherit;
  text-align: left;
  cursor: pointer;
}

.ticket-progress-step:hover {
  background: transparent;
}

.ticket-progress-step:hover .ticket-progress-step__icon {
  transform: translateY(-1px);
}

.ticket-progress-step.is-selected .ticket-progress-step__content strong,
.ticket-progress-step--active .ticket-progress-step__content strong {
  color: var(--primary-color);
}

.ticket-progress-step:focus-visible {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

.ticket-progress-step__icon {
  display: inline-flex;
  flex: none;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  color: var(--text-tertiary);
  background: var(--bg-tertiary);
  font-size: 17px;
  transition: transform 0.15s ease;
}

.ticket-progress-step--finished .ticket-progress-step__icon {
  color: var(--text-inverse);
  background: var(--success-color);
}

.ticket-progress-step--active .ticket-progress-step__icon {
  color: var(--text-inverse);
  background: var(--primary-color);
  box-shadow: 0 0 0 4px color-mix(in srgb, var(--primary-color) 12%, transparent);
}

.ticket-progress-step--failed .ticket-progress-step__icon {
  color: var(--text-inverse);
  background: var(--error-color);
}

.ticket-progress-step__content {
  flex-direction: column;
  align-items: flex-start;
  gap: 3px;
  width: 140px;
  min-width: 140px;
  background: transparent;
}

.ticket-progress-step__content strong {
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 600;
}

.ticket-progress-step__content span {
  display: block;
  max-width: 100%;
  overflow: hidden;
  color: var(--text-tertiary);
  font-size: 12px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ticket-progress-step__content .ticket-progress-step__handler {
  color: var(--text-secondary);
}

.ticket-progress-connector {
  flex: 1 1 48px;
  min-width: 36px;
  height: 2px;
  margin: 18px 12px 0;
  border-radius: 1px;
  background: var(--border-primary);
}

.ticket-progress-connector.is-reached {
  background: var(--success-color);
}

.ticket-sql-section {
  padding: 20px 24px;
  border: 1px solid var(--border-primary);
  border-radius: 8px;
  background: var(--bg-card);
  box-shadow: 0 2px 8px rgba(31, 41, 55, 0.05);
}

.ticket-detail-page .ticket-sql-section {
  display: flex;
  flex: 1 0 344px;
  flex-direction: column;
  min-height: 344px;
}

.ticket-sql-section > :deep(.read-only-editor-wrapper) {
  flex: 1;
  min-height: 240px;
}

.ticket-sql-section > :deep(.read-only-editor-wrapper .read-only-editor) {
  height: 100% !important;
  min-height: 240px;
}

.ticket-sql-section :deep(.monaco-scrollable-element > .scrollbar.vertical) {
  display: none !important;
}

.ticket-step-detail {
  margin-top: 20px;
  padding-top: 20px;
}

.page-section__title {
  position: relative;
  padding-left: 12px;
  color: var(--text-primary);
  font-size: 16px;
  font-weight: 600;
  line-height: 24px;
}

.page-section__title::before {
  position: absolute;
  top: 3px;
  bottom: 3px;
  left: 0;
  width: 3px;
  border-radius: 2px;
  background: var(--primary-color);
  content: '';
}

.ticket-step-summary {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 24px;
  margin-top: 12px;
  padding: 18px 0 20px;
  background: var(--bg-card);
}

.ticket-step-summary__item,
.ticket-activity-row > div {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
}

.ticket-step-summary__item + .ticket-step-summary__item {
  padding-left: 24px;
  border-left: 1px solid var(--border-light);
}

.ticket-step-rejection-reason {
  display: grid;
  grid-template-columns: 40px minmax(120px, 0.8fr) minmax(240px, 2fr);
  gap: 16px;
  align-items: center;
  min-height: 72px;
  margin: 12px 0 20px;
  padding: 12px 16px;
  border: 1px solid var(--border-light);
  border-radius: 8px;
  background: var(--bg-card);
}

.ticket-step-rejection-reason__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  color: var(--error-color);
  background: color-mix(in srgb, var(--error-color) 8%, var(--bg-card));
  font-size: 22px;
}

.ticket-step-rejection-reason > strong {
  color: var(--text-primary);
  font-weight: 500;
}

.ticket-step-rejection-reason p {
  min-width: 0;
  margin: 0;
  color: var(--text-secondary);
  line-height: 22px;
  overflow-wrap: anywhere;
  white-space: pre-wrap;
}

.ticket-step-name {
  color: var(--primary-color) !important;
}

.ticket-execution-detail {
  margin-top: 20px;
}

.ticket-execution-summary-row {
  grid-template-columns: 28px 40px minmax(110px, 0.7fr) auto minmax(220px, 1.4fr) auto;
}

.analysis-summary-item.analysis-summary-item--table,
.analysis-summary-item.ticket-execution-card {
  overflow: hidden;
  border: 1px solid var(--border-light);
  border-radius: 8px;
  background: var(--bg-card);
}

.analysis-summary-item--table > .analysis-summary-row,
.ticket-execution-card > .ticket-execution-summary-row {
  border: 0;
  border-radius: 0;
  background: var(--bg-card);
}

.analysis-summary-item--table.is-expanded > .analysis-summary-row,
.ticket-execution-card.is-expanded > .ticket-execution-summary-row {
  border-radius: 0;
}

.ticket-execution-status-wrap,
.ticket-execution-context,
.ticket-execution-actions {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px 12px;
}

.ticket-execution-summary-row__icon {
  color: var(--info-color);
}

.ticket-execution-context {
  min-width: 0;
  color: var(--text-secondary);
  font-size: 13px;
}

.ticket-execution-context > span + span {
  position: relative;
  padding-left: 13px;
}

.ticket-execution-context > span + span::before {
  position: absolute;
  top: 50%;
  left: 0;
  width: 3px;
  height: 3px;
  border-radius: 50%;
  background: var(--border-primary);
  transform: translateY(-50%);
  content: '';
}

.ticket-execution-status,
.ticket-task-status {
  display: inline-flex;
  align-items: center;
  min-height: 24px;
  padding: 2px 8px;
  border-radius: 6px;
  color: var(--text-secondary);
  background: var(--bg-secondary);
  font-size: 12px;
  line-height: 18px;
}

.ticket-execution-status.is-FINISH,
.ticket-task-status.is-FINISH {
  color: #16845b;
  background: #edf9f4;
}

.ticket-execution-status.is-EXECUTING,
.ticket-task-status.is-EXECUTING {
  color: #1b61c9;
  background: #eef5ff;
}

.ticket-execution-status.is-FAILED,
.ticket-execution-status.is-TERMINATION,
.ticket-task-status.is-FAILED {
  color: #c53b3b;
  background: #fff1f0;
}

.ticket-execution-actions {
  flex: none;
  flex-wrap: nowrap;
  justify-content: flex-end;
  gap: 4px;
  white-space: nowrap;
}

.ticket-execution-actions :deep(.ivu-btn) {
  min-height: 30px;
  padding: 0 8px;
  border: 0;
  border-radius: 6px;
  color: var(--text-secondary);
  background: transparent;
  box-shadow: none;
  transition:
    color 0.2s ease,
    background-color 0.2s ease,
    transform 0.2s ease;
}

.ticket-execution-actions :deep(.ivu-btn:hover) {
  color: var(--primary-color);
  background: color-mix(in srgb, var(--primary-color) 8%, var(--bg-card));
}

.ticket-execution-actions :deep(.ivu-btn:active) {
  transform: translateY(1px);
}

.analysis-result-details.ticket-execution-body {
  padding: 0;
  border-top: 0;
  background: var(--bg-card);
}

.ticket-execution-table {
  overflow-x: auto;
}

.ticket-execution-table :deep(.ivu-table-wrapper-with-border),
.analysis-result-details--table :deep(.ivu-table-wrapper-with-border),
.ticket-execution-table :deep(.ant-table-wrapper),
.analysis-result-details--table :deep(.ant-table-wrapper) {
  border: 0;
  border-radius: 0;
}

.ticket-execution-table :deep(.ivu-table::before),
.ticket-execution-table :deep(.ivu-table::after),
.analysis-result-details--table :deep(.ivu-table::before),
.analysis-result-details--table :deep(.ivu-table::after) {
  display: none;
}

.ticket-execution-table :deep(.ivu-table-border th:last-child),
.ticket-execution-table :deep(.ivu-table-border td:last-child),
.analysis-result-details--table :deep(.ivu-table-border th:last-child),
.analysis-result-details--table :deep(.ivu-table-border td:last-child) {
  border-right: 0;
}

.ticket-execution-table :deep(.ivu-table-tbody tr:last-child td),
.analysis-result-details--table :deep(.ivu-table-tbody tr:last-child td),
.analysis-result-details--table :deep(.ant-table-tbody > tr:last-child > td) {
  border-bottom: 0 !important;
}

.ticket-execution-table :deep(.ivu-table-wrapper),
.ticket-execution-table :deep(.ant-table-wrapper) {
  min-width: 720px;
}

.ticket-execution-table :deep(.ivu-table td:nth-child(4) .ivu-table-cell) {
  font-family: ui-monospace, Menlo, Monaco, Consolas, 'Liberation Mono', monospace;
  font-size: 13px;
}

.ticket-execution-table :deep(.ivu-btn-text) {
  padding: 0 4px;
  color: var(--text-secondary);
}

.ticket-execution-table :deep(.ivu-btn-text:hover) {
  color: var(--primary-color);
  background: transparent;
}

.ticket-execution-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 12px 16px;
}

.analysis-summary-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 12px;
}

.analysis-summary-item {
  overflow: hidden;
  border: 1px solid var(--border-light);
  border-radius: 8px;
  background: var(--bg-card);
}

.analysis-summary-row {
  display: grid;
  grid-template-columns: 28px 40px minmax(120px, 0.8fr) 96px minmax(240px, 2fr) 130px;
  gap: 16px;
  align-items: center;
  min-height: 72px;
  padding: 12px 16px;
}

.analysis-summary-row__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  color: var(--info-color);
  background: color-mix(in srgb, var(--info-color) 8%, var(--bg-card));
  font-size: 22px;
}

.analysis-summary-row > strong {
  color: var(--text-primary);
  font-weight: 500;
}

.analysis-summary-row__result {
  min-width: 0;
  overflow: hidden;
  color: var(--text-secondary);
  text-overflow: ellipsis;
  white-space: nowrap;
}

.analysis-summary-row__elapsed {
  justify-content: flex-end;
  gap: 12px;
  color: var(--text-primary);
  font-variant-numeric: tabular-nums;
}

.analysis-summary-row__expand {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  padding: 0;
  border: 0;
  border-radius: 6px;
  color: var(--text-secondary);
  background: transparent;
  font-size: 18px;
  cursor: pointer;
}

.analysis-summary-row__expand:hover {
  color: var(--primary-color);
  background: var(--bg-hover);
}

.analysis-summary-row__expand :deep(.ivu-icon) {
  transition: transform 0.24s cubic-bezier(0.4, 0, 0.2, 1);
}

.analysis-summary-row__expand.is-expanded :deep(.ivu-icon) {
  transform: rotate(90deg);
}

.analysis-result-collapse {
  display: grid;
  grid-template-rows: 0fr;
  opacity: 0;
  transition:
    grid-template-rows 0.32s cubic-bezier(0.4, 0, 0.2, 1),
    opacity 0.22s ease-out;
}

.analysis-result-collapse.is-expanded {
  grid-template-rows: 1fr;
  opacity: 1;
}

.analysis-result-collapse__content {
  min-height: 0;
  overflow: hidden;
}

.analysis-result-details {
  padding: 16px;
  border-top: 1px solid var(--border-light);
  background: var(--bg-secondary);
  opacity: 0;
  transform: translateY(-6px);
  transition:
    opacity 0.24s ease-out 0.06s,
    transform 0.32s cubic-bezier(0.4, 0, 0.2, 1);
}

.analysis-result-details.analysis-result-details--table {
  padding: 0;
  border-top: 0;
  background: var(--bg-card);
}

.analysis-result-collapse.is-expanded .analysis-result-details {
  opacity: 1;
  transform: translateY(0);
}

.analysis-result-details .page-panel-body {
  padding: 0;
}

.analysis-result-empty {
  min-height: 80px;
  padding: 28px 16px;
  color: var(--text-tertiary);
  text-align: center;
}

.analysis-rule-toolbar {
  display: flex;
  justify-content: flex-end;
  padding-bottom: 12px;
}

.ticket-activity-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
}

.ticket-activity-row {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr)) auto;
  gap: 24px;
  align-items: center;
  min-height: 60px;
  padding: 10px 16px;
  border: 1px solid var(--border-light);
  border-radius: 8px;
  background: var(--bg-card);
}

.ticket-detail-page .ticket-content {
  flex: none;
  margin: 0;
  border: 0;
  background: var(--bg-card);
  box-shadow: none;
}

.ticket-detail-page .ticket-content :deep(.ivu-card-head) {
  border-bottom: 0;
}

.ticket-sql-toolbar {
  gap: 24px;
  min-height: 44px;
  margin-bottom: 12px;
}

.ticket-sql-toolbar__meta,
.ticket-sql-toolbar__actions {
  flex-wrap: wrap;
  gap: 8px 16px;
}

.ticket-sql-toolbar__meta {
  min-width: 0;
}

.ticket-sql-file {
  padding: 6px 10px;
  border-radius: 6px;
  color: var(--text-secondary);
  background: var(--bg-secondary);
  font-size: 13px;
}

.ticket-sql-toolbar__actions {
  flex: none;
  justify-content: flex-end;
}

.ticket-sql-toolbar__actions :deep(.ivu-btn) {
  height: 32px;
  padding: 0 4px;
  border: 0;
  color: var(--text-secondary);
  background: transparent;
  box-shadow: none;
}

.ticket-sql-toolbar__actions :deep(.ivu-btn:hover) {
  color: var(--primary-color);
}

.ticket-sql-toolbar__actions :deep(.ivu-icon) {
  margin-right: 5px;
}

.parse-error-msgContent {
  margin-left: 0;
  color: var(--error-color);
}

@media (max-width: 1200px) {
  .ticket-overview__primary-grid,
  .ticket-overview__primary-grid--with-target-database {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    row-gap: 22px;
  }

  .ticket-overview__primary-grid .ticket-meta-item:nth-child(odd) {
    padding-left: 0;
    border-left: 0;
  }

  .ticket-overview__secondary-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    row-gap: 22px;
  }

  .ticket-overview__secondary-grid .ticket-meta-item--description {
    grid-column: 1 / -1;
    padding-left: 0;
    border-left: 0;
  }
}

@media (max-width: 1100px) {
  .ticket-step-summary {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .ticket-step-summary__item:nth-child(odd) {
    padding-left: 0;
    border-left: 0;
  }

  .analysis-summary-row {
    grid-template-columns: 28px 40px minmax(120px, 1fr) 96px minmax(180px, 2fr);
  }

  .analysis-summary-row__elapsed {
    grid-column: 3 / 6;
    justify-content: flex-start;
  }
}

@media (max-width: 760px) {
  .ticket-detail-page .ticket-detail-container {
    gap: 20px;
    padding: 12px 16px 20px;
  }

  .ticket-overview,
  .ticket-info-section,
  .ticket-progress-card,
  .ticket-auth-section {
    padding: 16px;
  }

  .ticket-overview {
    padding: 0;
  }

  .ticket-info-section__header,
  .ticket-sql-toolbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .ticket-overview__actions {
    justify-content: flex-start;
  }

  .ticket-overview__primary-grid,
  .ticket-overview__secondary-grid,
  .ticket-step-summary,
  .ticket-activity-row {
    grid-template-columns: 1fr;
  }

  .ticket-step-summary__item + .ticket-step-summary__item {
    padding-left: 0;
    border-left: 0;
  }

  .ticket-step-rejection-reason {
    grid-template-columns: 40px minmax(0, 1fr);
  }

  .ticket-step-rejection-reason p {
    grid-column: 2;
  }

  .ticket-overview__primary-grid,
  .ticket-overview__secondary-grid {
    row-gap: 0;
  }

  .ticket-meta-item,
  .ticket-overview__primary-grid .ticket-meta-item:nth-child(odd),
  .ticket-overview__secondary-grid .ticket-meta-item--description {
    padding: 12px 0;
    border-top: 1px solid var(--border-light);
    border-left: 0;
  }

  .ticket-overview__secondary-grid .ticket-meta-item--description {
    grid-column: auto;
  }

  .ticket-overview__primary-grid .ticket-meta-item:first-child,
  .ticket-overview__secondary-grid .ticket-meta-item:first-child {
    padding-top: 0;
    border-top: 0;
  }

  .ticket-overview__headline {
    align-items: flex-start;
    flex-direction: column;
    gap: 10px;
  }

  .ticket-overview__headline h2 {
    font-size: 18px;
    line-height: 28px;
  }

  .ticket-progress {
    min-width: 900px;
  }

  .analysis-summary-row {
    grid-template-columns: 28px 40px 1fr auto;
  }

  .ticket-execution-summary-row {
    grid-template-columns: 28px 40px 1fr auto;
  }

  .analysis-summary-row__result,
  .analysis-summary-row__elapsed {
    grid-column: 3 / 5;
    justify-content: flex-start;
  }

  .ticket-sql-toolbar__actions {
    justify-content: flex-start;
  }

  .ticket-execution-actions {
    grid-column: 3 / 5;
    width: auto;
    flex-wrap: wrap;
    justify-content: flex-start;
    white-space: normal;
  }

  .ticket-execution-context {
    grid-column: 3 / 5;
  }

  .ticket-auth-record__meta,
  .ticket-auth-record__permissions {
    grid-template-columns: 1fr;
  }

  .ticket-auth-record__meta {
    gap: 14px;
  }

  .ticket-auth-record__permissions {
    gap: 8px;
  }

  .ticket-auth-record__permissions-title {
    padding-top: 0;
  }
}

@media (prefers-reduced-motion: reduce) {
  .analysis-result-collapse,
  .analysis-result-details,
  .analysis-summary-row__expand :deep(.ivu-icon),
  .ticket-execution-actions :deep(.ivu-btn) {
    transition-duration: 0.01ms;
  }
}
</style>
