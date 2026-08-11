<template>
  <div class="ticket-detail-container">
    <Card class="ticket-detail-status">
      <template #title>
        <p class="ticket-title-p" style="display: flex; align-items: center">
          <span class="ticket-title">【{{ APPROV_BIZ_MAP[ticketDetail.approBiz] }}】</span>
          <span class="ticket-title">{{ ticketDetail.ticketTitle }}</span>
          <span :class="['ticket-status-total', { 'analysis-status': ['PRE_INIT_WAIT', 'PRE_INIT_RUN'].includes(ticketDetail.ticketStatus) }]">
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
        <div :class="['ticket-detail-summary', { 'with-analysis-summary': showAnalysisSummary }]">
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
          <div class="ticket-detail-item">
            <span class="ticket-detail-item-title">{{ $t('miao-shu') }}：</span>
            <span>{{ ticketDetail.description }}</span>
          </div>
        </div>
        <div class="ticket-analysis-summary" v-if="showAnalysisSummary">
          <div>
            <span>{{ $t('ticket-current-stage') }}：</span>
            <strong>{{ $t('ticket-analysis-stage') }}</strong>
          </div>
          <div>
            <span>{{ $t('zhuang-tai') }}：</span>
            <strong>{{ analysisProcessStatusText }}</strong>
          </div>
          <div>
            <span>{{ $t('ticket-elapsed') }}：</span>
            <strong>{{ analysisStageElapsed }}</strong>
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
          :class="`step-item ${currentStep === index ? 'current-step' : ''} ${process.ticketStage === 'EXPLAIN' ? 'analysis-step' : ''}`"
          v-for="(process, index) in ticketDetail.ticketProcessVOList"
          :key="process.ticketProcessId"
        >
          <div class="step-item-item" v-if="process.ticketStage === 'EXPLAIN' || !process.activityList || process.activityList.length === 0">
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
                {{ process.ticketStage === 'EXPLAIN' ? analysisProcessStatusText : TICKET_PROCESS_STATUS[process.ticketProcessStatus] }}
              </div>
              <Button
                v-if="process.ticketStage === 'EXPLAIN' && analysisItems.length"
                type="text"
                class="analysis-toggle"
                @click="analysisDetailsExpanded = !analysisDetailsExpanded"
              >
                {{ analysisDetailsExpanded ? $t('ticket-collapse-details') : $t('ticket-expand-details') }}
                <Icon :type="analysisDetailsExpanded ? 'ios-arrow-up' : 'ios-arrow-down'" />
              </Button>
            </div>
          </div>
          <div class="step-item-item" v-if="process.ticketStage !== 'EXPLAIN' && process.activityList && process.activityList.length > 0">
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
            <div v-if="process.ticketStage === 'EXECUTION'" class="execution-progress-list">
              <div class="execution-progress-row execution-progress-header">
                <div>{{ $t('ticket-execution-process') }}</div>
                <div>{{ $t('zhuang-tai') }}</div>
                <div>{{ $t('ticket-execution-progress') }}</div>
              </div>
              <div class="execution-progress-row" v-for="activity in process.activityList" :key="activity.activityTitle">
                <div>{{ executionActivityTitle(activity.activityTitle) }}</div>
                <div>
                  <span :class="['analysis-item-status', analysisStatusClass(activity.activityStatus)]">
                    {{ executionStatusText(activity.activityStatus) }}
                  </span>
                </div>
                <div>{{ executionProgressText(activity) }}</div>
              </div>
            </div>
            <div v-else class="step-item-item" style="flex-grow: 3">
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
          <div v-if="process.ticketStage === 'EXPLAIN' && analysisDetailsExpanded && analysisItems.length" class="analysis-detail-list">
            <div class="analysis-detail-row analysis-detail-header">
              <div>{{ $t('ticket-analysis-content') }}</div>
              <div>{{ $t('zhuang-tai') }}</div>
              <div>{{ $t('ticket-analysis-result') }}</div>
              <div>{{ $t('hao-shi') }}</div>
            </div>
            <div class="analysis-detail-row" v-for="item in analysisItems" :key="item.activityTitle">
              <div>{{ analysisTypeText(item.activityTitle) }}</div>
              <div>
                <span :class="['analysis-item-status', analysisStatusClass(item.activityStatus)]">{{ analysisStatusText(item.activityStatus) }}</span>
              </div>
              <div class="analysis-result">{{ analysisResultText(item) }}</div>
              <div>{{ analysisElapsed(item) }}</div>
            </div>
          </div>
        </div>
      </div>
    </Card>
    <Card class="ticket-content analysis-results-card" v-if="showAnalysisResults">
      <template #title>
        <div class="collapsible-card-title">
          <span>{{ $t('ticket-analysis-results') }}</span>
          <Button type="text" size="small" @click="analysisResultsExpanded = !analysisResultsExpanded">
            {{ analysisResultsExpanded ? $t('ticket-collapse') : $t('ticket-expand') }}
            <Icon :type="analysisResultsExpanded ? 'ios-arrow-up' : 'ios-arrow-down'" />
          </Button>
        </div>
      </template>
      <Tabs v-show="analysisResultsExpanded" v-model="analysisResultTab" type="card" :animated="false" class="analysis-result-tabs">
        <TabPane v-for="item in analysisItems" :key="item.activityTitle" :label="analysisTypeText(item.activityTitle)" :name="item.activityTitle">
          <template v-if="item.activityTitle === 'BEHAVIOR_ANALYSIS'">
            <div v-if="behaviorStatementCount(item) != null" class="behavior-analysis-summary">
              {{ behaviorSummaryText(item) }}
            </div>
            <Table v-if="recognizedBehaviorRows.length" :columns="recognizedContentColumns" :data="recognizedBehaviorRows" border size="small">
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
              {{ analysisRuleResults.length && showCheckedOnlyError ? $t('ticket-analysis-security-passed') : analysisResultText(item) }}
            </div>
          </template>

          <template v-else-if="item.activityTitle === 'DML_EXPLAIN'">
            <div class="behavior-analysis-summary">{{ dmlExplainDetailText(item) }}</div>
            <Table
              v-if="item.explainResults && item.explainResults.length"
              :columns="dmlExplainColumns"
              :data="dmlExplainRows(item)"
              border
              size="small"
            >
              <template #estimatedAffectedRows="{ row }">
                <span>{{ row.estimatedAffectedRows ?? '--' }}</span>
              </template>
              <template #actions="{ row }">
                <Tag v-for="action in row.actions" :key="action" color="primary">{{ action }}</Tag>
              </template>
              <template #subjects="{ row }">
                <span>{{ row.subjects.join($t('ticket-analysis-dml-explain-index-separator')) }}</span>
              </template>
              <template #statementCount="{ row }">
                <span>{{ dmlExplainStatementText(row) }}</span>
              </template>
              <template #description="{ row }">
                <span>{{ dmlExplainDescription(row) }}</span>
              </template>
            </Table>
          </template>

          <div v-else class="analysis-result-empty">{{ analysisResultText(item) }}</div>
        </TabPane>
      </Tabs>
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
            <Button type="text" size="small" @click="taskExecutionExpanded = !taskExecutionExpanded">
              {{ taskExecutionExpanded ? $t('ticket-collapse') : $t('ticket-expand') }}
              <Icon :type="taskExecutionExpanded ? 'ios-arrow-up' : 'ios-arrow-down'" />
            </Button>
          </div>
        </div>
      </template>

      <div v-show="taskExecutionExpanded">
        <Table :columns="autoExecTaskColumns" :data="autoExecTaskList" border size="small">
          <template #status="{ row }">
            <Tag :color="AUTO_EXEC_TASK_STATUS_COLOR[row.status]">
              {{ AUTO_EXEC_TASK_STATUS_I18N[row.status] }}
            </Tag>
          </template>
          <template #action="{ row }">
            <Button type="text" size="small" @click="handleAutoExecSQL(row)">{{ $t('cha-kan') }}</Button>
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
      </div>
    </Card>

    <Card class="ticket-content compact-ticket-content" v-if="this.ticketType === 'DM_QUERY' || this.ticketType === 'DM_CHANGE'" :padding="0">
      <div
        class="ticket-content-entry"
        role="button"
        tabindex="0"
        @click="handleShowTicketContentModal"
        @keydown.enter="handleShowTicketContentModal"
      >
        <div class="ticket-content-title-main">
          <strong>{{ $t('gong-dan-nei-rong') }}</strong>
          <Button type="link" @click.stop="handleShowRollbackSqlModal" v-if="ticketDetail.rollBackSql">
            {{ $t('cha-kan-hui-gun-sql') }}
          </Button>
          <span v-if="ticketDetail.ticketMessage" class="parse-error-msgContent">*{{ ticketDetail.ticketMessage }}</span>
        </div>
        <div class="ticket-content-entry-meta">
          <template v-if="ticketDetail.contentType === 'ATTACHMENT'">
            <Icon type="ios-document-outline" />
            <span>{{ ticketDetail.attachmentFileName }}</span>
            <span>{{ formatFileSize(ticketDetail.attachmentFileSize || 0) }}</span>
            <span>{{ $t('ticket-sql-readonly') }}</span>
          </template>
          <span>{{ $t('ticket-view-content') }}</span>
          <Icon type="ios-arrow-forward" />
        </div>
      </div>
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
    <CCModal v-model="showTicketContentModal" :title="$t('gong-dan-nei-rong')" width="80vw" centered :draggable="false" class="responsive-sql-modal">
      <div class="ticket-content-modal-editor ticket-sql-preview" @wheel="handleSqlPreviewWheel">
        <read-only-editor
          ref="sqlPreviewEditor"
          :text="sqlPreview"
          key="raw"
          :border="0"
          :ds-type="ticketDetail.dataSourceType"
          fit-viewport
          virtual-scroll-mode
          :line-number-start="sqlPreviewStartLine"
          @viewport-line-count-change="handleSqlPreviewViewportChange"
        />
        <input
          v-if="ticketSqlContentInitialized"
          v-model.number="sqlPreviewStartLine"
          class="ticket-virtual-scrollbar"
          type="range"
          min="1"
          :max="sqlPreviewMaxStartLine"
          step="1"
          :aria-label="$t('ticket-sql-virtual-scrollbar')"
          aria-orientation="vertical"
          @pointerdown="handleSqlPreviewDragStart"
          @change="loadSqlPreview"
        />
      </div>
      <template #footer>
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
import { TICKET_STATUS, TICKET_STATUS_COLOR, TICKET_PROCESS_STATUS } from '@/const';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import copyMixin from '@/mixins/copyMixin';
import { RULE_WARN_LEVEL, isCk, isMongoDB } from '@/utils';
import { APPROV_BIZ_MAP } from './constant';

const TICKET_AUTO_REFRESH_INTERVAL_MS = 5000;
const TICKET_TERMINAL_STATUSES = new Set(['REJECTED', 'FINISHED', 'CLOSED', 'CANCELED', 'FAILED']);

const dmlExplainChange = (row) => ({
  actions: [...(row.actions || [])].sort(),
  subjects: [...(row.subjects || [])].sort()
});

const dmlExplainChangeKey = (row) => {
  const change = dmlExplainChange(row);
  return JSON.stringify([change.actions, change.subjects]);
};

const aggregateDmlExplainDetails = (details) => {
  const groups = new Map();
  details.forEach((row) => {
    const key = dmlExplainChangeKey(row);
    if (!groups.has(key)) {
      groups.set(key, {
        ...dmlExplainChange(row),
        details: []
      });
    }
    groups.get(key).details.push({
      ...row,
      _disableExpand: !row.explainPlan?.nodes?.length
    });
  });
  return [...groups.values()].map((group) => {
    const indices = [...new Set(group.details.map((row) => row.index))].sort((left, right) => left - right);
    const statuses = [...new Set(group.details.map((row) => row.status).filter(Boolean))];
    const skipReasons = [...new Set(group.details.map((row) => row.skipReason).filter(Boolean))];
    const estimates = group.details.map((row) => row.estimatedAffectedRows);
    const allEstimated = estimates.every((value) => value != null);
    return {
      ...group,
      indices,
      statementCount: indices.length,
      status: statuses.join(' / '),
      skipReason: skipReasons.join(' / '),
      estimatedAffectedRows: allEstimated ? estimates.reduce((total, value) => total + value, 0) : null
    };
  });
};

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
      dmlExplainColumns: [
        {
          title: this.$t('ticket-analysis-dml-explain-rows'),
          slot: 'estimatedAffectedRows',
          width: 150
        },
        {
          title: this.$t('ticket-analysis-dml-explain-actions'),
          slot: 'actions',
          width: 180
        },
        {
          title: this.$t('ticket-analysis-dml-explain-subjects'),
          slot: 'subjects'
        },
        {
          title: this.$t('ticket-analysis-dml-explain-statement-count'),
          slot: 'statementCount',
          width: 320
        },
        {
          title: this.$t('shuo-ming'),
          slot: 'description',
          width: 220
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
      showTicketContentModal: false,
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
      sqlPreview: '',
      sqlPreviewStartLine: 1,
      sqlPreviewTotalLines: 1,
      sqlPreviewLineCount: 25,
      sqlPreviewTimer: null,
      sqlPreviewRequestSequence: 0,
      ticketSqlContentInitialized: false,
      ticketAutoRefreshActive: false,
      ticketAutoRefreshTimer: null,
      analysisDetailsExpanded: true,
      analysisResultsExpanded: true,
      taskExecutionExpanded: true,
      durationNow: Date.now(),
      durationTimer: null,
      TICKET_STATUS,
      TICKET_STATUS_COLOR,
      TICKET_PROCESS_STATUS,
      loading: false,
      confirmSubmitting: false,
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
    if (this.sqlPreviewTimer) {
      window.clearTimeout(this.sqlPreviewTimer);
    }
    this.sqlPreviewRequestSequence++;
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
    showAnalysisResults() {
      return ['DM_QUERY', 'DM_CHANGE'].includes(this.ticketDetail.approBiz) && this.analysisItems.length > 0;
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
    showAnalysisSummary() {
      return (
        ['DM_QUERY', 'DM_CHANGE'].includes(this.ticketDetail.approBiz) &&
        this.ticketDetail.ticketStatus === 'PRE_INIT_RUN' &&
        this.analysisItems.length > 0
      );
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
    analysisStageElapsed() {
      const started = this.analysisItems.map((item) => item.startTimeUtc).filter(Boolean);
      if (!started.length) {
        return '--';
      }
      const start = Math.min(...started);
      const finished = this.analysisItems.map((item) => item.finishTimeUtc).filter(Boolean);
      const end = this.analysisRunningCount > 0 || finished.length !== this.analysisItems.length ? this.durationNow : Math.max(...finished);
      return this.formatElapsed(end - start);
    },
    hasError() {
      return this.analysisRuleResults.some((rule) => rule.ruleLevel !== 'SUGGEST');
    },
    sqlPreviewMaxStartLine() {
      return Math.max(1, this.sqlPreviewTotalLines - this.sqlPreviewLineCount + 1);
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
        SECURITY_RULE: 'ticket-analysis-security-rule',
        DML_EXPLAIN: 'ticket-analysis-dml-explain'
      };
      return this.$t(keyMap[type] || type);
    },
    executionActivityTitle(type) {
      const keyMap = {
        EXECUTION_PREPARATION: 'ticket-execution-preparation',
        EXECUTION_DISPATCH: 'ticket-execution-dispatch',
        EXECUTION_RUNNING: 'ticket-execution-running'
      };
      return this.$t(keyMap[type] || type);
    },
    executionStatusText(status) {
      const keyMap = {
        NEW: 'ticket-execution-waiting',
        RUNNING: 'ticket-execution-processing',
        COMPLETED: 'ticket-execution-complete',
        REFUSE: 'ticket-execution-failed',
        CANCELED: 'ticket-execution-canceled'
      };
      return this.$t(keyMap[status] || status);
    },
    executionProgressText(activity) {
      if (activity.activityStatus === 'REFUSE') {
        return activity.remark || this.$t('ticket-execution-failed');
      }
      if (activity.activityTitle === 'EXECUTION_PREPARATION' && activity.processedCount != null && activity.statementCount != null) {
        const percentage = activity.statementCount > 0 ? Math.min(100, Math.floor((activity.processedCount * 100) / activity.statementCount)) : 0;
        return this.$t('ticket-execution-preparation-progress', {
          processed: activity.processedCount,
          total: activity.statementCount,
          percentage
        });
      }
      if (activity.activityStatus === 'NEW') {
        return '--';
      }
      const keyMap = {
        EXECUTION_DISPATCH: activity.activityStatus === 'COMPLETED' ? 'ticket-execution-dispatched' : 'ticket-execution-dispatching',
        EXECUTION_RUNNING: activity.activityStatus === 'COMPLETED' ? 'ticket-execution-finished' : 'ticket-execution-sidecar-running'
      };
      return this.$t(keyMap[activity.activityTitle] || 'ticket-execution-processing');
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
      if (item.activityTitle === 'DML_EXPLAIN' && item.dmlStatementCount != null) {
        return this.$t('ticket-analysis-dml-explain-result', {
          total: item.dmlStatementCount,
          skipped: (item.skippedBySizeLimit || 0) + (item.skippedByCountLimit || 0)
        });
      }
      return '--';
    },
    dmlExplainDetailText(item) {
      const failed = item.failedExplainCount || 0;
      const total = item.dmlStatementCount || 0;
      const sizeSkipped = item.skippedBySizeLimit || 0;
      const countSkipped = item.skippedByCountLimit || 0;
      const skipped = sizeSkipped + countSkipped;
      let text = this.$t('ticket-analysis-dml-explain-detail-total', { total });
      if (skipped > 0) {
        text = this.$t('ticket-analysis-dml-explain-detail', {
          total,
          sizeSkipped,
          countSkipped,
          skipped
        });
      }
      if (failed > 0) {
        text += this.$t('ticket-analysis-dml-explain-detail-failed', { failed });
      }
      return text;
    },
    dmlExplainStatementText(row) {
      return this.$t('ticket-analysis-dml-explain-statement-summary', {
        count: row.statementCount,
        indices: row.indices.join(this.$t('ticket-analysis-dml-explain-index-separator'))
      });
    },
    dmlExplainDescription(row) {
      const status = row.status
        .split(' / ')
        .map((value) => this.$t(`ticket-analysis-dml-explain-status-${value}`))
        .join(' / ');
      if (!row.skipReason) {
        return status;
      }
      const reason = row.skipReason
        .split(' / ')
        .map((value) => this.$t(`ticket-analysis-dml-explain-reason-${value}`))
        .join(' / ');
      return this.$t('ticket-analysis-dml-explain-description-with-reason', { status, reason });
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
    dmlExplainRows(item) {
      const statements = new Map();
      [...(item.explainResults || [])]
        .sort((left, right) => left.index - right.index)
        .forEach((row) => {
          if (!statements.has(row.index)) {
            statements.set(row.index, []);
          }
          statements.get(row.index).push(row);
        });

      // A segment only combines adjacent SQL statements whose complete action and object sets match.
      const segments = [];
      for (const [index, details] of statements) {
        const signature = JSON.stringify(details.map(dmlExplainChangeKey).sort());
        const previous = segments[segments.length - 1];
        if (previous && index === previous.lastIndex + 1 && signature === previous.signature) {
          previous.lastIndex = index;
          previous.details.push(...details);
        } else {
          segments.push({
            signature,
            lastIndex: index,
            details: [...details]
          });
        }
      }
      return segments.flatMap((segment) => aggregateDmlExplainDetails(segment.details));
    },
    async loadSqlPreview() {
      this.sqlPreviewLineCount = this.$refs.sqlPreviewEditor?.getVisibleLineCount() || 25;
      const requestSequence = ++this.sqlPreviewRequestSequence;
      const res = await this.$services.dmTicketPreviewApprovalSql({
        data: {
          ticketId: this.ticketId,
          startLine: this.sqlPreviewStartLine,
          lineCount: this.sqlPreviewLineCount
        }
      });
      if (res.success && requestSequence === this.sqlPreviewRequestSequence) {
        this.sqlPreview = res.data?.content || '';
        this.sqlPreviewStartLine = res.data?.startLine || 1;
        this.sqlPreviewTotalLines = res.data?.totalLines || 1;
      }
    },
    scheduleSqlPreview() {
      if (this.sqlPreviewTimer) {
        window.clearTimeout(this.sqlPreviewTimer);
      }
      this.sqlPreviewTimer = window.setTimeout(() => {
        this.sqlPreviewTimer = null;
        this.loadSqlPreview();
      }, 120);
    },
    handleSqlPreviewDragStart() {
      if (this.sqlPreviewTimer) {
        window.clearTimeout(this.sqlPreviewTimer);
        this.sqlPreviewTimer = null;
      }
    },
    handleSqlPreviewViewportChange(lineCount) {
      if (!this.ticketSqlContentInitialized || !lineCount || lineCount === this.sqlPreviewLineCount) {
        return;
      }
      this.sqlPreviewLineCount = lineCount;
      this.scheduleSqlPreview();
    },
    handleSqlPreviewWheel(event) {
      if (!this.ticketSqlContentInitialized) {
        return;
      }
      event.preventDefault();
      const step = Math.max(1, Math.floor(this.sqlPreviewLineCount / 3));
      const delta = event.deltaY > 0 ? step : -step;
      this.sqlPreviewStartLine = Math.max(1, Math.min(this.sqlPreviewMaxStartLine, this.sqlPreviewStartLine + delta));
      this.scheduleSqlPreview();
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
    async handleShowTicketContentModal() {
      this.showTicketContentModal = true;
      await this.$nextTick();
      this.sqlPreviewLineCount = this.$refs.sqlPreviewEditor?.getVisibleLineCount() || this.sqlPreviewLineCount;
      await this.loadSqlPreview();
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
      if (type === 'init') {
        this.currentStep = 0;
      }
      let theCurrentStep = 0;
      const res = await this.$services.rdpTicketQueryTicketBaseInfo({ data, modal: showLoading });

      if (showLoading) {
        this.loading = false;
      }
      if (res.success) {
        this.ticketType = res.data?.approBiz;
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
            appLogger.debug('reject');
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
                this.sqlPreviewStartLine = 1;
                this.sqlPreviewTotalLines = 1;
                await this.$nextTick();
                await this.loadSqlPreview();
                this.ticketSqlContentInitialized = true;
              }
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

    .behavior-analysis-summary {
      padding: 16px 24px;
      border-bottom: 1px solid #e8eaec;
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

    .ticket-sql-preview {
      position: relative;
      padding-right: 18px;
    }

    .ticket-virtual-scrollbar {
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
        background: transparent;
      }

      &::-moz-range-thumb {
        width: 10px;
        height: 20px;
        border: 0;
        border-radius: 0;
        background: rgba(100, 100, 100, 0.45);
      }

      &:hover::-webkit-slider-thumb {
        background: rgba(100, 100, 100, 0.7);
      }

      &:hover::-moz-range-thumb {
        background: rgba(100, 100, 100, 0.7);
      }
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

    .execution-progress-list {
      flex-grow: 3;
      width: 100%;
      margin: 8px 0;
    }

    .execution-progress-row {
      display: grid;
      grid-template-columns: 28% 20% 1fr;
      align-items: center;
      min-height: 40px;
      border-top: 1px solid #e8eaec;

      > div {
        padding: 8px 12px;
      }
    }

    .execution-progress-header {
      min-height: 34px;
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

.ticket-content-modal-editor,
.responsive-sql-modal-editor {
  position: relative;
  height: clamp(320px, 62vh, 720px);
  overflow: hidden;

  :deep(.read-only-editor-wrapper),
  :deep(.read-only-editor) {
    height: 100% !important;
  }
}

.ticket-content-modal-editor {
  padding-right: 28px;

  .ticket-virtual-scrollbar {
    position: absolute;
    z-index: 3;
    top: 4px;
    right: 2px;
    width: 24px;
    height: calc(100% - 8px);
    margin: 0;
    writing-mode: vertical-lr;
    direction: ltr;
    appearance: none;
    background: transparent;
    cursor: pointer;
    touch-action: none;

    &::-webkit-slider-runnable-track {
      width: 12px;
      height: 100%;
      border-radius: 6px;
      background: rgba(50, 50, 50, 0.08);
    }

    &::-webkit-slider-thumb {
      width: 16px;
      height: 48px;
      border: 0;
      border-radius: 8px;
      appearance: none;
      background: rgba(80, 80, 80, 0.55);
    }

    &::-moz-range-track {
      width: 12px;
      height: 100%;
      border-radius: 6px;
      background: rgba(50, 50, 50, 0.08);
    }

    &::-moz-range-thumb {
      width: 16px;
      height: 48px;
      border: 0;
      border-radius: 8px;
      background: rgba(80, 80, 80, 0.55);
    }

    &:hover::-webkit-slider-thumb,
    &:focus-visible::-webkit-slider-thumb {
      background: rgba(60, 60, 60, 0.8);
    }

    &:hover::-moz-range-thumb,
    &:focus-visible::-moz-range-thumb {
      background: rgba(60, 60, 60, 0.8);
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
</style>
