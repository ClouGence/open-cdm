<template>
  <aside class="ai-sql-panel page-shell" :aria-label="$t('ai.assistant')">
    <div class="ai-panel-heading">
      <div class="ai-panel-title">
        <Button
          v-if="historyOpen"
          type="text"
          class="ai-icon-button"
          :aria-label="$t('ai.backToChat')"
          :title="$t('ai.backToChat')"
          @click="historyOpen = false"
        >
          <ArrowLeftOutlined />
        </Button>
        <span>{{ historyOpen ? $t('ai.history') : $t('ai.assistant') }}</span>
      </div>
      <div class="ai-panel-actions">
        <Button
          type="text"
          class="ai-icon-button"
          :disabled="busy || stopping || !!editingRequestId"
          :aria-label="$t('ai.newConversation')"
          :title="$t('ai.newConversation')"
          @click="newConversation"
        >
          <PlusOutlined />
        </Button>
        <Button
          v-if="!historyOpen && turns.some((turn) => turn.confirmed && turn.result)"
          type="text"
          class="ai-icon-button"
          :disabled="conversationLocked || !!editingRequestId"
          :aria-label="$t('ai.shareConversation')"
          :title="$t('ai.shareConversation')"
          @click="openShare()"
        >
          <ShareAltOutlined />
        </Button>
        <Button
          v-if="!historyOpen"
          type="text"
          class="ai-icon-button"
          :disabled="busy || stopping || !!editingRequestId"
          :aria-label="$t('ai.history')"
          :title="$t('ai.history')"
          @click="openHistory"
        >
          <HistoryOutlined />
        </Button>
        <Button
          type="text"
          class="ai-icon-button"
          icon="icon-v2-close2"
          :aria-label="$t('ai.close')"
          :title="$t('ai.close')"
          @click="$emit('close')"
        />
      </div>
    </div>
    <div v-if="historyOpen" class="ai-history-search">
      <a-input
        v-model:value="historyKeyword"
        allow-clear
        :maxlength="128"
        :placeholder="$t('ai.searchConversations')"
        :aria-label="$t('ai.searchConversations')"
        @pressEnter="loadHistory(true)"
      >
        <template #prefix><SearchOutlined /></template>
      </a-input>
    </div>
    <div v-if="historyOpen" ref="historyBody" class="page-shell__body ai-history-view" @scroll="historyScroll">
      <p v-if="historyError" role="alert" class="ai-panel-error">{{ historyError }}</p>
      <div class="ai-history-list">
        <div v-for="item in historyItems" :key="item.id" class="ai-history-row" :class="{ 'ai-history-row--active': item.id === sessionId }">
          <Button type="text" class="ai-history-open" :disabled="busy || !item.accessible" @click="restore(item)">
            <span class="ai-history-title">{{ historyTitle(item) }}</span>
            <small>{{ formatTime(item.modifiedAt) }}</small>
          </Button>
          <a-dropdown :trigger="['click']">
            <Button
              type="text"
              class="ai-icon-button ai-history-menu"
              :disabled="busy"
              :aria-label="$t('ai.conversationActions')"
              :title="$t('ai.conversationActions')"
            >
              <EllipsisOutlined />
            </Button>
            <template #overlay>
              <a-menu @click="askDelete(item)">
                <a-menu-item key="delete" danger>
                  <DeleteOutlined />
                  {{ $t('ai.deleteConversation') }}
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
        <p v-if="!historyItems.length && !historyLoading && !historyError" class="ai-inline-notice">{{ $t('ai.empty') }}</p>
      </div>
      <p v-if="historyLoading" role="status" class="ai-history-status">
        <LoadingOutlined spin />
        {{ $t('ai.loadingHistory') }}
      </p>
      <Button v-else-if="historyError" type="text" @click="loadHistory()">{{ $t('ai.retry') }}</Button>
    </div>
    <div
      v-show="!historyOpen"
      class="page-shell__body ai-messages"
      :class="{ 'ai-messages--loading': loading }"
      :aria-busy="loading"
      ref="body"
      @scroll="trackScroll"
    >
      <p v-if="error" role="alert" class="ai-panel-error">{{ error }}</p>
      <div v-if="loading" class="ai-conversation-loading" role="status" :aria-label="$t('ai.loadingMessages')">
        <LoadingOutlined spin aria-hidden="true" />
        <span>{{ $t('ai.loadingMessages') }}</span>
      </div>
      <div
        class="ai-conversation-content"
        :class="{ 'ai-conversation-content--preparing': loading }"
        :aria-hidden="loading"
        :inert="loading || undefined"
      >
        <div v-if="!capabilities?.generationAvailable" class="ai-inline-notice">
          <p>{{ unavailableMessage }}</p>
          <div class="ai-panel-actions">
            <Button v-if="capabilities?.canManageEngines" size="small" @click="$router.push('/settings/ai')">{{ $t('ai.settings') }}</Button>
            <Button size="small" :disabled="busy" @click="initialize">{{ $t('ai.retry') }}</Button>
          </div>
        </div>
        <div v-if="!turns.length && capabilities?.generationAvailable" class="ai-empty-chat">
          <AppBrandLogo />
          <div class="ai-prompt-presets">
            <Button
              v-for="preset in promptPresets"
              :key="preset"
              class="ai-prompt-preset"
              :disabled="conversationLocked || !accessible"
              @click="selectPreset(preset)"
            >
              <span>{{ $t(`ai.presets.${preset}.label`) }}</span>
              <RightOutlined aria-hidden="true" />
            </Button>
          </div>
        </div>
        <p v-if="!accessible" role="alert">{{ $t('ai.inaccessible') }}</p>
        <article v-for="turn in turns" :key="turn.requestId" class="ai-turn">
          <div class="ai-user-message">
            <Button
              v-if="turn.context?.selection"
              class="ai-sql-selection"
              :title="turn.context.selection.title"
              :aria-label="$t('ai.sqlReferenceDetails')"
              @click="showSelectionDetails(turn.context.selection, turn.context.dialect)"
            >
              <DataSourceIcon :type="turn.context.dialect" size="14px" leftMargin="0" />
              <span class="ai-selection-title">{{ turn.context.selection.title }}</span>
              <span class="ai-selection-lines">
                {{ $t('ai.selectionLines', { start: turn.context.selection.startLine, end: turn.context.selection.endLine }) }}
              </span>
            </Button>
            <div v-if="editingRequestId === turn.requestId" class="ai-message-edit">
              <a-textarea
                ref="editPromptInput"
                v-model:value="editedPrompt"
                :auto-size="{ minRows: 2, maxRows: 8 }"
                :aria-label="$t('ai.editMessage')"
                @keydown="editKeydown($event, turn)"
              />
              <div class="ai-edit-actions">
                <span v-if="editedPromptLength > 14745" :class="{ 'ai-panel-error': editedPromptLength > 16384 }">
                  {{ $t('ai.promptCount', { count: editedPromptLength }) }}
                </span>
                <Button size="small" @click="cancelEdit">{{ $t('ai.cancel') }}</Button>
                <Button size="small" type="primary" :disabled="!canSubmitEdit" @click="submitEdit(turn)">{{ $t('ai.save') }}</Button>
              </div>
            </div>
            <p v-else class="ai-question">{{ turn.prompt }}</p>
            <div v-if="editingRequestId !== turn.requestId" class="ai-message-actions ai-user-actions">
              <Button
                v-if="turn.id && !['RUNNING', 'UNKNOWN'].includes(turn.status)"
                type="text"
                class="ai-icon-button"
                :disabled="!canRegenerate || !!editingRequestId"
                :aria-label="$t('ai.editMessage')"
                :title="$t('ai.editMessage')"
                @click="startEdit(turn)"
              >
                <EditOutlined />
              </Button>
              <Button type="text" class="ai-icon-button" :aria-label="$t('ai.copyMessage')" :title="$t('ai.copyMessage')" @click="copyMessage(turn)">
                <CheckOutlined v-if="copiedMessage === turn.requestId" />
                <CopyOutlined v-else />
              </Button>
            </div>
          </div>
          <div class="ai-assistant-message">
            <AiActivityLog :turn="turn">
              <template #message="{ text }">
                <div class="ai-markdown" v-html="renderMessage(text)"></div>
              </template>
            </AiActivityLog>
            <p v-if="!turn.errorCode && ['CANCELLED', 'EXPIRED', 'UNKNOWN', 'FAILED'].includes(turn.status)" class="ai-inline-notice" role="status">
              {{ $t(`ai.turnStatus.${turn.status}`) }}
            </p>
            <div v-if="turn.draft && !turn.result" class="ai-markdown" v-html="renderMessage(turn.draft)"></div>
            <p v-if="turn.errorCode" role="alert" class="ai-panel-error">{{ errorText(turn.errorCode, turn.errorMessage) }}</p>
            <template v-if="turn.confirmed && turn.result">
              <div class="ai-markdown" v-html="renderMessage(turn.result.message)"></div>
            </template>
            <AiUserInputCard
              v-if="turn.confirmed && turn.result?.userInputRequest"
              :key="turn.result.userInputRequest.request_id"
              :request="turn.result.userInputRequest"
              :state="userInputState(turn)"
              :answers="userInputAnswers(turn)"
              :request-history-id="turn.id"
              :submitting="submittingUserInputRequestId === turn.result.userInputRequest.request_id"
              :disabled="conversationLocked || !sessionEngineAvailable || !capabilities?.generationAvailable || !!editingRequestId"
              @submit="answerUserInput(turn, $event)"
            />
            <div v-if="sqlOutput(turn)" class="ai-sql-result">
              <div class="ai-sql-toolbar">
                <span>{{ $t('ai.sqlCode') }}</span>
                <div class="ai-panel-actions">
                  <Button
                    type="text"
                    size="small"
                    :aria-label="$t('ai.copy')"
                    :disabled="busy || streaming || stopping"
                    @click="useResult(turn, 'copy')"
                  >
                    <span class="ai-result-action-content">
                      <span :class="{ 'ai-result-action-hidden': resultFeedback[`${turn.requestId}:copy`] }">
                        {{ $t('ai.copy') }}
                      </span>
                      <span class="ai-result-action-success" :class="{ 'ai-result-action-hidden': !resultFeedback[`${turn.requestId}:copy`] }">
                        <CheckOutlined />
                        {{ $t('ai.copied') }}
                      </span>
                    </span>
                  </Button>
                  <Button
                    type="text"
                    size="small"
                    :aria-label="$t('ai.insert')"
                    :disabled="busy || streaming || stopping || !targetActive"
                    @click="useResult(turn, 'insert')"
                  >
                    <span class="ai-result-action-content">
                      <span :class="{ 'ai-result-action-hidden': resultFeedback[`${turn.requestId}:insert`] }">
                        {{ $t('ai.insert') }}
                      </span>
                      <span class="ai-result-action-success" :class="{ 'ai-result-action-hidden': !resultFeedback[`${turn.requestId}:insert`] }">
                        <CheckOutlined />
                        {{ $t('ai.inserted') }}
                      </span>
                    </span>
                  </Button>
                </div>
              </div>
              <ReadOnlyEditor
                :text="sqlOutput(turn)"
                :font-size="sqlFontSize"
                :ds-type="turn.context.dialect"
                :border="0"
                :always-consume-mouse-wheel="false"
                :max-height="Math.min(260, sqlOutput(turn).split('\n').length * 21 + 24)"
              />
            </div>
            <div v-if="turn.id && !['RUNNING', 'UNKNOWN'].includes(turn.status)" class="ai-message-actions ai-reply-actions">
              <Button
                v-if="turn.confirmed && turn.result"
                type="text"
                class="ai-icon-button"
                :disabled="conversationLocked"
                :aria-label="$t('ai.copyReply')"
                :title="$t('ai.copyReply')"
                @click="copyReply(turn)"
              >
                <CheckOutlined v-if="copiedMessage === `${turn.requestId}:reply`" />
                <CopyOutlined v-else />
              </Button>
              <Button
                type="text"
                class="ai-icon-button"
                :disabled="!canRegenerate || !!editingRequestId"
                :aria-label="$t('ai.regenerate')"
                :title="$t('ai.regenerate')"
                @click="regenerate(turn)"
              >
                <ReloadOutlined />
              </Button>
              <Button
                v-if="turn.confirmed && turn.result"
                type="text"
                class="ai-icon-button"
                :disabled="conversationLocked || !!editingRequestId"
                :aria-label="$t('ai.shareConversation')"
                :title="$t('ai.shareConversation')"
                @click="openShare(turn)"
              >
                <ShareAltOutlined />
              </Button>
              <time
                v-if="turn.modifiedAt || turn.createdAt"
                :datetime="turn.modifiedAt || turn.createdAt"
                :title="formatTime(turn.modifiedAt || turn.createdAt)"
              >
                {{ formatReplyTime(turn.modifiedAt || turn.createdAt) }}
              </time>
            </div>
          </div>
        </article>
        <Button v-if="unresolved && !streaming" size="small" :disabled="busy || stopping" @click="retryTurn">{{ $t('ai.retrySameRequest') }}</Button>
        <p v-if="notice" role="status">{{ notice }}</p>
      </div>
    </div>
    <div v-show="!historyOpen" class="ai-composer">
      <Button
        v-if="!loading && !followTail && turns.length"
        class="ai-scroll-bottom"
        :aria-label="$t('ai.scrollToBottom')"
        :title="$t('ai.scrollToBottom')"
        @click="jumpToTail"
      >
        <ArrowDownOutlined />
      </Button>
      <div class="ai-composer-input">
        <Button
          v-if="sqlSelection"
          class="ai-sql-selection"
          :title="sqlSelection.title"
          :aria-label="$t('ai.sqlReferenceDetails')"
          @click="showSelectionDetails(sqlSelection, binding.dsType)"
        >
          <DataSourceIcon :type="binding.dsType" size="14px" leftMargin="0" />
          <span class="ai-selection-title">{{ sqlSelection.title }}</span>
          <span class="ai-selection-lines">{{ $t('ai.selectionLines', { start: sqlSelection.startLine, end: sqlSelection.endLine }) }}</span>
        </Button>
        <a-textarea
          ref="promptInput"
          v-model:value="prompt"
          :bordered="false"
          :auto-size="{ minRows: 2, maxRows: 6 }"
          :placeholder="$t('ai.promptHint')"
          :aria-label="$t('ai.promptLabel')"
          :disabled="loading || busy || !accessible || !!editingRequestId"
          @keydown="promptKeydown"
        />
        <div class="ai-composer-actions">
          <div class="ai-composer-tools">
            <a-dropdown :trigger="['click']" placement="topLeft" :disabled="loading || conversationLocked || !accessible || !!editingRequestId">
              <Button
                type="text"
                class="ai-model-select"
                :disabled="loading || conversationLocked || !accessible || !!editingRequestId"
                :aria-label="$t('ai.chooseModel')"
                :title="selectedEngine?.model"
              >
                <span>{{ selectedEngine?.name || $t('ai.chooseModel') }}</span>
                <DownOutlined />
              </Button>
              <template #overlay>
                <a-menu class="ai-model-menu" :selected-keys="[selectedEngineId]" @click="selectModel($event.key)">
                  <a-menu-item v-for="model in models" :key="model.id">
                    <div class="ai-model-option">
                      <span>
                        {{ model.name }}
                        <small>{{ model.model }}</small>
                      </span>
                      <CheckOutlined v-if="model.id === selectedEngineId" />
                    </div>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <a-popover :trigger="['hover', 'click']" placement="topRight">
            <template #content>
              <div class="ai-context-usage-detail">
                <strong>{{ $t('ai.contextUsage') }}</strong>
                <span v-if="contextUsage">
                  {{
                    $t('ai.contextUsageDetail', {
                      used: formatTokens(contextUsage.usedTokens),
                      max: formatTokens(contextUsage.maxTokens),
                      percent: contextPercent
                    })
                  }}
                </span>
                <span v-else>{{ $t('ai.contextUsageUnavailable') }}</span>
              </div>
            </template>
            <Button type="text" class="ai-context-usage" :aria-label="$t('ai.contextUsage')">
              <a-progress
                type="circle"
                :size="22"
                :stroke-width="10"
                :percent="contextPercent"
                :show-info="false"
                :stroke-color="contextUsageColor"
                trail-color="var(--border-primary)"
              />
            </Button>
          </a-popover>
          <Button
            v-if="activeRequestId"
            class="ai-submit-button"
            :loading="stopping"
            :disabled="stopping"
            :aria-label="$t('ai.stop')"
            :title="$t('ai.stop')"
            @click="stop"
          >
            <span v-if="!stopping" class="ai-stop-icon" aria-hidden="true"></span>
          </Button>
          <Button
            v-else
            type="primary"
            class="ai-submit-button"
            :disabled="!canSend"
            :aria-label="$t('ai.send')"
            :title="$t('ai.send')"
            @click="send()"
          >
            <ArrowUpOutlined />
          </Button>
        </div>
      </div>
      <div v-if="promptLength > 14745" class="ai-composer-hint">
        <span :class="{ 'ai-panel-error': promptLength > 16384 }">{{ $t('ai.promptCount', { count: promptLength }) }}</span>
      </div>
    </div>
    <AiShareModal v-if="shareOpen" :rows="shareRows" :initial-selected-ids="shareSelectedIds" @close="shareOpen = false" />
    <CCModal
      v-model="selectionDetailsOpen"
      :title="$t('ai.sqlReferenceDetails')"
      :width="800"
      class="dm-modal-footer-center"
      @on-cancel="selectionDetailsOpen = false"
    >
      <template v-if="selectionDetails">
        <div class="ai-selection-detail-source">
          <DataSourceIcon :type="selectionDetails.dialect" size="16px" leftMargin="0" />
          <span>{{ selectionDetails.title }}</span>
          <span>{{ $t('ai.selectionLines', { start: selectionDetails.startLine, end: selectionDetails.endLine }) }}</span>
        </div>
        <div class="ai-selection-detail-code">
          <ReadOnlyEditor
            :text="selectionDetails.sql"
            :font-size="sqlFontSize"
            :ds-type="selectionDetails.dialect"
            :line-number-start="selectionDetails.startLine"
            :border="0"
            :always-consume-mouse-wheel="false"
            :max-height="Math.min(360, Math.max(110, selectionDetails.sql.split('\n').length * 21 + 24))"
          />
        </div>
      </template>
      <template #footer>
        <Button @click="selectionDetailsOpen = false">{{ $t('ai.close') }}</Button>
      </template>
    </CCModal>
    <CCModal class="dm-modal-footer-center" v-model="deleteOpen" :title="$t('ai.deleteConversation')" :width="480" :closable="!busy">
      <p>{{ $t('ai.deleteConversationHint') }}</p>
      <p v-if="historyError" role="alert" class="ai-panel-error">{{ historyError }}</p>
      <template #footer>
        <Button :disabled="busy" @click="deleteOpen = false">{{ $t('ai.cancel') }}</Button>
        <Button type="error" :loading="busy" @click="deleteConversation">{{ $t('ai.delete') }}</Button>
      </template>
    </CCModal>
  </aside>
</template>

<script>
import { markRaw } from 'vue';
import { createUuid } from '@/utils/uuid';
import AiActivityLog from './AiActivityLog.vue';
import AiUserInputCard from './AiUserInputCard.vue';
import {
  ArrowLeftOutlined,
  ArrowUpOutlined,
  DeleteOutlined,
  PlusOutlined,
  HistoryOutlined,
  ArrowDownOutlined,
  CheckOutlined,
  CopyOutlined,
  DownOutlined,
  EllipsisOutlined,
  EditOutlined,
  LoadingOutlined,
  RightOutlined,
  ReloadOutlined,
  SearchOutlined,
  ShareAltOutlined
} from '@ant-design/icons-vue';
import { renderAiMessage } from '@/services/ai/aiMarkdown';
import AiShareModal from './AiShareModal.vue';
import CCModal from '@/components/ui/CCModal.vue';
import AppBrandLogo from '@/components/layout/AppBrandLogo.vue';
import DataSourceIcon from '@/components/function/DataSourceIcon.vue';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor.vue';
import { SQL_EDITOR_TYPOGRAPHY } from '@/components/editor/sqlEditorTypography';
import { aiRequest, aiErrorMessage } from '@/services/ai';
import { streamAiTurn, resumeAiTurn } from '@/services/ai/aiStream';

export default {
  name: 'AiSqlPanel',
  components: {
    AiActivityLog,
    AiShareModal,
    AiUserInputCard,
    AppBrandLogo,
    DataSourceIcon,
    CCModal,
    ReadOnlyEditor,
    ArrowLeftOutlined,
    ArrowUpOutlined,
    DeleteOutlined,
    PlusOutlined,
    HistoryOutlined,
    ArrowDownOutlined,
    CheckOutlined,
    CopyOutlined,
    DownOutlined,
    EllipsisOutlined,
    EditOutlined,
    LoadingOutlined,
    RightOutlined,
    SearchOutlined,
    ReloadOutlined,
    ShareAltOutlined
  },
  props: {
    binding: { type: Object, required: true },
    sqlSelection: { type: Object, default: null },
    sqlFontSize: { type: Number, default: SQL_EDITOR_TYPOGRAPHY.fontSize },
    targetActive: Boolean,
    initialPrompt: { type: String, default: '' }
  },
  emits: ['close', 'insert', 'draft-change'],
  data() {
    return {
      shareOpen: false,
      shareRows: [],
      shareSelectedIds: [],
      capabilities: null,
      models: [],
      selectedEngineId: '',
      sessionEngineId: '',
      loading: true,
      busy: false,
      error: '',
      notice: '',
      prompt: this.initialPrompt,
      submittingUserInputRequestId: '',
      editingRequestId: null,
      editedPrompt: '',
      promptPresets: ['generate', 'explain', 'error', 'optimize'],
      selectionDetailsOpen: false,
      selectionDetails: null,
      sessionId: null,
      streamClientId: createUuid(),
      contextUsage: null,
      sessionDrafts: {},
      sessionVersion: null,
      turns: [],
      hasMoreTurns: false,
      accessible: true,
      activeRequestId: null,
      streaming: false,
      stopping: false,
      unresolved: false,
      recoveryTimer: null,
      recoveryDelay: 1000,
      controller: null,
      streamController: null,
      disposed: false,
      followTail: true,
      copiedMessage: '',
      resultFeedback: {},
      resultFeedbackTimers: markRaw(new Map()),
      copyTimer: null,
      scrollFrame: null,
      historyOpen: false,
      historyItems: [],
      historyKeyword: '',
      historyLoading: false,
      historyHasMore: true,
      historyCursor: null,
      historySequence: 0,
      historySearchTimer: null,
      historyError: '',
      deleteOpen: false,
      deleteTarget: null
    };
  },
  computed: {
    appLocale() {
      return this.$i18n.global.locale.value;
    },
    contextPercent() {
      if (!this.contextUsage) return 0;
      return Math.min(100, Math.ceil((this.contextUsage.usedTokens * 100) / this.contextUsage.maxTokens));
    },
    contextUsageColor() {
      if (this.contextPercent >= 90) return 'var(--error-color)';
      if (this.contextPercent >= 75) return 'var(--warning-color)';
      return 'var(--text-secondary)';
    },
    needsRecovery() {
      return (
        !this.disposed &&
        !this.loading &&
        !this.busy &&
        this.accessible &&
        !!this.activeRequestId &&
        !this.streaming &&
        !this.stopping &&
        !this.unresolved
      );
    },
    conversationLocked() {
      return this.busy || this.streaming || this.stopping || !!this.activeRequestId;
    },
    editedPromptLength() {
      return this.editedPrompt.length;
    },
    canSubmitEdit() {
      return !!this.editingRequestId && this.canRegenerate && !!this.editedPrompt.trim() && this.editedPromptLength <= 16384;
    },
    promptLength() {
      return this.prompt.length;
    },
    selectedEngine() {
      return this.models.find((model) => model.id === this.selectedEngineId);
    },
    sessionEngineAvailable() {
      return this.selectedEngine?.id === this.sessionEngineId && this.selectedEngine?.version === this.sessionVersion;
    },
    canRegenerate() {
      return !this.loading && !this.conversationLocked && this.accessible && this.capabilities?.generationAvailable && this.sessionEngineAvailable;
    },
    canStartTurn() {
      return (
        !this.loading &&
        !this.busy &&
        !this.streaming &&
        !this.stopping &&
        !this.activeRequestId &&
        !this.editingRequestId &&
        this.accessible &&
        this.capabilities?.generationAvailable &&
        this.selectedEngine &&
        (!this.sessionId || this.sessionEngineAvailable)
      );
    },
    canSend() {
      return this.canStartTurn && this.prompt.trim() && this.promptLength <= 16384;
    },
    unavailableMessage() {
      if (!this.capabilities) return this.$t('ai.retryCapabilities');
      if (!this.capabilities.canGenerate) return this.$t('ai.errors.FORBIDDEN');
      return this.errorText(this.capabilities.unavailableCode || 'ENGINE_UNAVAILABLE');
    }
  },
  watch: {
    sessionId(value) {
      this.$store.commit('SET_AI_SESSION_ID', value);
      this.$router.replace({ query: { ...this.$route.query, aiSession: value || undefined } });
    },
    historyKeyword: {
      flush: 'sync',
      handler() {
        if (!this.historyOpen) return;
        clearTimeout(this.historySearchTimer);
        this.historySequence++;
        this.historyItems = [];
        this.historyCursor = null;
        this.historyHasMore = true;
        this.historyError = '';
        this.historyLoading = true;
        this.historySearchTimer = setTimeout(() => this.loadHistory(true), 300);
      }
    },
    historyOpen(value) {
      if (value) return;
      clearTimeout(this.historySearchTimer);
      this.historySequence++;
      this.historyLoading = false;
    },
    needsRecovery(value) {
      clearTimeout(this.recoveryTimer);
      if (value) this.scheduleRecovery();
    },
    prompt: {
      flush: 'sync',
      handler(value) {
        this.$emit('draft-change', value);
      }
    }
  },
  mounted() {
    this.controller = markRaw(new AbortController());
    const sessionId = this.$store.state.aiSessionId || this.$route.query.aiSession;
    if (sessionId) this.restore({ id: sessionId });
    else this.initialize();
  },
  beforeUnmount() {
    this.disposed = true;
    cancelAnimationFrame(this.scrollFrame);
    clearTimeout(this.copyTimer);
    clearTimeout(this.recoveryTimer);
    clearTimeout(this.historySearchTimer);
    this.resultFeedbackTimers.forEach((timer) => clearTimeout(timer));
    this.controller?.abort();
    this.streamController?.abort();
  },
  methods: {
    async openShare(selectedTurn) {
      if (this.conversationLocked || this.editingRequestId) return;
      this.busy = true;
      this.error = '';
      try {
        const shareTurns = [];
        let beforeTurnId;
        let data;
        do {
          data = await this.request('turn/getSession', { sessionId: this.sessionId, beforeTurnId, pageSize: 100 });
          if (this.disposed) return;
          if (!data.accessible) {
            this.acceptSession(data, 'confirm');
            return;
          }
          shareTurns.unshift(...data.turns.slice().reverse());
          beforeTurnId = shareTurns[0]?.id;
        } while (data.hasMore);
        const rows = [];
        for (const turn of shareTurns) {
          if (!['COMPLETED', 'NEEDS_INPUT'].includes(turn.status) || !turn.result) continue;
          rows.push({
            id: `${turn.requestId}:user`,
            role: 'user',
            text: turn.prompt,
            selection: turn.context?.selection
          });
          rows.push({
            id: `${turn.requestId}:assistant`,
            role: 'assistant',
            text: turn.result.message,
            sql: this.sqlOutput(turn),
            request: turn.result.userInputRequest,
            answers: turn.result.userInputRequest ? this.userInputAnswers(turn) : [],
            inputState: turn.result.userInputRequest ? this.userInputState(turn, shareTurns) : 'expired'
          });
        }
        this.shareRows = rows;
        this.shareSelectedIds = rows.filter((row) => !selectedTurn || row.id.startsWith(`${selectedTurn.requestId}:`)).map((row) => row.id);
        this.shareOpen = true;
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.busy = false;
      }
    },
    startEdit(turn) {
      if (!this.canRegenerate || !turn.id) return;
      this.editingRequestId = turn.requestId;
      this.editedPrompt = turn.prompt;
      this.$nextTick(() => this.$refs.editPromptInput?.[0]?.focus());
    },
    cancelEdit() {
      this.editingRequestId = null;
      this.editedPrompt = '';
    },
    async submitEdit(turn) {
      if (!this.canSubmitEdit || this.editingRequestId !== turn.requestId) return;
      const prompt = this.editedPrompt;
      this.cancelEdit();
      await this.regenerate(turn, prompt);
    },
    editKeydown(event, turn) {
      if (event.isComposing || event.keyCode === 229) return;
      if (event.key === 'Escape') {
        event.preventDefault();
        this.cancelEdit();
      }
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        this.submitEdit(turn);
      }
    },
    showSelectionDetails(selection, dialect) {
      this.selectionDetails = { ...selection, dialect };
      this.selectionDetailsOpen = true;
    },
    selectPreset(preset) {
      this.prompt = this.$t(`ai.presets.${preset}.prompt`);
      this.$nextTick(() => this.$refs.promptInput?.focus());
    },
    jumpToTail() {
      this.followTail = true;
      this.scrollToTail();
    },
    async copyMessage(turn) {
      try {
        await navigator.clipboard.writeText(turn.prompt);
        if (!this.disposed) this.showCopied(turn.requestId);
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      }
    },
    async copyReply(turn) {
      if (this.conversationLocked) return;
      this.busy = true;
      try {
        const data = await this.request('turn/getSession', { sessionId: this.sessionId, requestId: turn.requestId });
        if (this.disposed) return;
        this.acceptSession(data, 'confirm');
        const reply = data.turns?.find((item) => item.requestId === turn.requestId && ['COMPLETED', 'NEEDS_INPUT'].includes(item.status));
        if (!data.accessible || !reply?.result) return;
        const parts = [reply.result.message];
        const request = reply.result.userInputRequest;
        if (request) {
          parts.push(request.title || this.$t('ai.userInputDefaultTitle'));
          for (const question of request.questions) {
            parts.push(question.question);
            parts.push(...question.options.map((option) => `${option.label} ${option.description || ''}`.trim()));
            const values = this.userInputAnswers(turn).find((answer) => answer.questionId === question.id)?.values;
            if (values?.length) parts.push(`${this.$t('ai.userInputAnswerLabel')}${values.join('；')}`);
          }
        }
        const sql = this.sqlOutput(reply);
        if (sql) parts.push('```sql\n' + sql + '\n```');
        await navigator.clipboard.writeText(parts.join('\n\n'));
        if (!this.disposed) this.showCopied(`${turn.requestId}:reply`);
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.busy = false;
      }
    },
    showCopied(key) {
      clearTimeout(this.copyTimer);
      this.copiedMessage = key;
      this.copyTimer = setTimeout(() => {
        this.copiedMessage = '';
      }, 2000);
    },
    renderMessage(message) {
      return renderAiMessage(message);
    },
    trackScroll() {
      if (this.loading) return;
      const body = this.$refs.body;
      this.followTail = body.scrollHeight - body.scrollTop - body.clientHeight < 48;
      if (body.scrollTop < 80 && this.hasMoreTurns && !this.busy && !this.streaming) this.loadOlder();
    },
    scrollToTail() {
      if (this.loading || !this.followTail || this.scrollFrame !== null) return;
      this.scrollFrame = requestAnimationFrame(() => {
        this.scrollFrame = null;
        if (!this.disposed && !this.loading && this.$refs.body) this.$refs.body.scrollTop = this.$refs.body.scrollHeight;
      });
    },
    request(path, data = {}) {
      return aiRequest(path, data, { signal: this.controller.signal });
    },
    errorText(code, msg) {
      return aiErrorMessage({ code, msg });
    },
    formatTime(value) {
      return new Date(value).toLocaleString(this.appLocale);
    },
    formatTokens(value) {
      if (value < 1000) return value.toLocaleString(this.appLocale);
      return (value / 1000).toLocaleString(this.appLocale, { maximumFractionDigits: 1 }) + 'k';
    },
    formatReplyTime(value) {
      return new Date(value).toLocaleString(this.appLocale, { month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', hour12: false });
    },
    async initialize() {
      if (this.busy) return;
      this.loading = true;
      this.busy = true;
      this.error = '';
      try {
        const capabilities = await this.request('turn/getCapabilities');
        if (this.disposed) return;
        this.capabilities = capabilities;
        if (!this.sessionId) this.contextUsage = { usedTokens: 0, maxTokens: capabilities.contextTokens };
        this.models = capabilities.models;
        if (!this.sessionId && !this.selectedEngine) this.selectedEngineId = this.models[0]?.id || '';
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.loading = false;
        this.busy = false;
      }
    },
    async selectModel(engineId) {
      if (this.busy || this.streaming || this.stopping || this.activeRequestId || this.editingRequestId) return;
      const model = this.models.find((item) => item.id === engineId);
      if (!model) return;
      if (!this.sessionId) {
        this.selectedEngineId = engineId;
        return;
      }
      if (engineId === this.sessionEngineId && model.version === this.sessionVersion) return;
      this.busy = true;
      this.error = '';
      try {
        const result = await this.request('turn/switchSessionModel', {
          sessionId: this.sessionId,
          engineId,
          engineVersion: model.version,
          previousEngineId: this.sessionEngineId,
          previousEngineVersion: this.sessionVersion
        });
        if (this.disposed) return;
        this.selectedEngineId = result.engineId;
        this.sessionEngineId = result.engineId;
        this.sessionVersion = result.engineVersion;
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.busy = false;
      }
    },
    detachStream() {
      clearTimeout(this.recoveryTimer);
      this.streamController?.abort();
      this.streamController = null;
      this.streaming = false;
    },
    resetConversation() {
      this.detachStream();
      this.cancelEdit();
      this.sessionId = null;
      this.contextUsage = null;
      this.sessionVersion = null;
      this.sessionEngineId = '';
      this.turns = [];
      this.hasMoreTurns = false;
      this.activeRequestId = null;
      this.accessible = true;
      this.error = '';
      this.notice = '';
      this.unresolved = false;
      this.followTail = true;
    },
    newConversation() {
      if (this.sessionId) this.sessionDrafts[this.sessionId] = this.prompt;
      this.historyOpen = false;
      this.prompt = '';
      this.resetConversation();
      this.initialize().then(() => this.$refs.promptInput?.focus());
    },
    promptKeydown(event) {
      if (event.isComposing || event.keyCode === 229) return;
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        this.send();
      }
    },
    async send(userInputAnswer = null) {
      if (userInputAnswer) {
        if (!this.canStartTurn || !userInputAnswer.answer_text?.trim()) return false;
      } else if (!this.canSend) return false;
      this.busy = true;
      this.error = '';
      this.notice = '';
      let prompt = this.prompt;
      let selection = null;
      if (userInputAnswer) prompt = userInputAnswer.answer_text.trim();
      else if (this.sqlSelection) selection = { ...this.sqlSelection };
      const context = { levels: [...this.binding.levels], dialect: this.binding.dsType, selection, userInputAnswer };
      let turn;
      try {
        if (!this.sessionId) {
          const data = await this.request('turn/createSession', {
            engineId: this.selectedEngine.id,
            engineVersion: this.selectedEngine.version
          });
          if (this.disposed) return;
          this.sessionId = data.sessionId;
          this.sessionVersion = data.engineVersion;
          this.sessionEngineId = data.engineId;
        }
        const requestId = createUuid();
        turn = {
          requestId,
          prompt,
          context,
          engineId: this.selectedEngine.id,
          engineVersion: this.selectedEngine.version,
          engineName: this.selectedEngine.name,
          status: 'RUNNING',
          draft: '',
          activities: [],
          confirmed: false,
          result: null
        };
        this.turns.push(turn);
        if (!userInputAnswer) this.prompt = '';
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
        return false;
      } finally {
        this.busy = false;
      }
      return this.transmit(turn);
    },
    async regenerate(source, prompt = source.prompt) {
      if (!this.canRegenerate || !source.id) return;
      const turn = {
        requestId: createUuid(),
        regenerateTurnId: source.id,
        prompt,
        context: {
          levels: [...this.binding.levels],
          dialect: this.binding.dsType,
          selection: source.context.selection,
          userInputAnswer: source.context.userInputAnswer
        },
        engineId: this.selectedEngine.id,
        engineVersion: this.selectedEngine.version,
        engineName: this.selectedEngine.name,
        status: 'RUNNING',
        draft: '',
        activities: [],
        confirmed: false,
        result: null
      };
      this.turns.push(turn);
      await this.transmit(turn);
    },
    async transmit(turn, resume = false) {
      const sessionId = this.sessionId;
      const requestId = turn.requestId;
      this.activeRequestId = requestId;
      this.unresolved = false;
      this.streaming = true;
      if (!resume) this.followTail = true;
      this.scrollToTail();
      this.error = '';
      this.notice = '';
      const controller = markRaw(new AbortController());
      this.streamController = controller;
      const current = () => !this.disposed && this.sessionId === sessionId && this.streamController === controller;
      try {
        const data = { sessionId, requestId, streamClientId: this.streamClientId };
        let subscribe = resumeAiTurn;
        if (!resume) {
          subscribe = streamAiTurn;
          Object.assign(data, {
            prompt: turn.prompt,
            engineId: turn.engineId,
            engineVersion: turn.engineVersion,
            regenerateTurnId: turn.regenerateTurnId,
            levels: turn.context.levels,
            selection: turn.context.selection,
            userInputAnswer: turn.context.userInputAnswer
          });
        }
        await subscribe(data, {
          signal: controller.signal,
          onEvent: (event) => {
            if (current() && !controller.signal.aborted)
              this.receiveEvent(
                this.turns.find((item) => item.requestId === requestId),
                event
              );
          }
        });
        if (current()) await this.readTurn(requestId, controller);
        if (current()) this.recoveryDelay = 1000;
      } catch (error) {
        if (!current() || this.stopping) return;
        this.recoveryDelay = Math.min(this.recoveryDelay * 2, 10000);
        try {
          await this.readTurn(requestId, controller);
          if (current() && this.unresolved) {
            const pending = this.turns.find((item) => item.requestId === requestId);
            if (pending) {
              pending.errorCode = error.code;
              pending.errorMessage = error.msg;
            }
            this.notice = '';
            if (
              pending?.context.userInputAnswer &&
              error.code &&
              !['NETWORK_ERROR', 'REQUEST_TIMEOUT'].includes(error.code) &&
              !error.code.startsWith('HTTP_')
            ) {
              const session = await this.request('turn/getSession', { sessionId, pageSize: 20 });
              if (current()) {
                this.acceptSession(session, 'reset');
                this.unresolved = false;
              }
            }
          }
          if (current() && !this.activeRequestId) this.error = '';
        } catch (confirmationError) {
          if (current()) {
            this.error = aiErrorMessage(confirmationError);
            if (['FORBIDDEN', 'NOT_FOUND', 'HTTP_401', 'HTTP_403', 'HTTP_404'].includes(confirmationError.code)) {
              this.accessible = false;
              this.activeRequestId = null;
            }
          }
        }
      } finally {
        if (current()) {
          this.streaming = false;
          this.streamController = null;
        }
      }
      return this.turns.some((item) => item.requestId === requestId && item.id);
    },
    async retryTurn() {
      if (!this.unresolved || this.busy || this.streaming || this.stopping) return;
      const turn = this.turns.find((item) => item.requestId === this.activeRequestId);
      if (turn) await this.transmit(turn);
    },
    receiveEvent(turn, event) {
      if (!turn) return;
      turn.id = event.turnId;
      this.scrollToTail();
      const data = event.data;
      if (event.type === 'started') {
        if (turn.regenerateTurnId) {
          const index = this.turns.findIndex((item) => item.id === turn.regenerateTurnId);
          if (index >= 0) this.turns.splice(index, this.turns.length - index, turn);
        }
        turn.status = 'RUNNING';
        turn.errorCode = null;
        turn.errorMessage = null;
        // Keep received content until the recovery snapshot arrives; the connection may drop after started.
        turn.result = null;
        turn.confirmed = false;
      }
      if (event.type === 'snapshot') {
        turn.draft = data.draft;
        turn.phase = data.phase;
        turn.activities = data.activities;
        if (data.contextUsage) this.contextUsage = data.contextUsage;
      }
      if (event.type === 'phase') {
        turn.phase = data.phase;
        if (data.contextUsage) this.contextUsage = data.contextUsage;
      }
      if (event.type === 'textDelta') turn.draft = turn.draft + data.text;
      if (event.type === 'toolStarted') {
        turn.activities.push(data);
        turn.draft = '';
      }
      if (event.type === 'toolCompleted') {
        const activity = turn.activities.find((item) => item.toolCallId === data.toolCallId);
        if (activity) Object.assign(activity, data);
      }
      if (event.type === 'result') turn.result = data;
      if (event.type === 'ERROR') {
        turn.errorCode = data.code;
        turn.errorMessage = data.error || data.msg;
      }
      if (event.type === 'done') {
        turn.status = data.status;
        turn.confirmed =
          !!turn.result &&
          ((data.status === 'COMPLETED' && turn.result.status === 'GENERATED') ||
            (data.status === 'NEEDS_INPUT' && turn.result.status === 'NEEDS_INPUT'));
        if (!['COMPLETED', 'NEEDS_INPUT'].includes(data.status)) turn.result = null;
        this.activeRequestId = null;
      }
    },
    acceptSession(data, mode) {
      if (!data.accessible) {
        this.contextUsage = null;
        this.turns = [];
        this.prompt = '';
        this.sessionVersion = null;
        this.sessionEngineId = '';
        this.accessible = false;
        this.hasMoreTurns = false;
        this.activeRequestId = data.activeTurn?.status === 'RUNNING' ? data.activeTurn.requestId : null;
        this.error = this.$t('ai.inaccessible');
        return;
      }
      this.accessible = true;
      this.contextUsage = data.contextUsage;
      this.sessionEngineId = data.engineId;
      this.sessionVersion = data.engineVersion;
      this.selectedEngineId = data.engineId;
      const values = data.turns
        .slice()
        .reverse()
        .map((turn) => ({ ...turn, confirmed: ['COMPLETED', 'NEEDS_INPUT'].includes(turn.status) && !!turn.result }));
      if (mode === 'reset') this.turns = values;
      else if (mode === 'older') this.turns = [...values.filter((turn) => !this.turns.some((existing) => existing.id === turn.id)), ...this.turns];
      else {
        for (const turn of values) {
          const index = this.turns.findIndex((item) => item.requestId === turn.requestId);
          if (index >= 0) {
            // A running snapshot requested before completion must not replace the confirmed terminal turn.
            if (turn.status === 'RUNNING' && !['RUNNING', 'UNKNOWN'].includes(this.turns[index].status)) continue;
            const target = this.turns[index].regenerateTurnId;
            const sourceIndex = this.turns.findIndex((item) => item.id === target);
            if (target && sourceIndex >= 0) this.turns.splice(sourceIndex, this.turns.length - sourceIndex, turn);
            else if (turn.status === 'RUNNING') Object.assign(this.turns[index], turn);
            else this.turns.splice(index, 1, turn);
          }
        }
      }
      if (mode !== 'confirm') this.hasMoreTurns = data.hasMore;
      if (mode !== 'older') this.scrollToTail();
      if (mode !== 'older') {
        this.activeRequestId = null;
        if (data.activeTurn?.status === 'RUNNING') {
          const active = this.turns.find((turn) => turn.requestId === data.activeTurn.requestId);
          if (!active || ['RUNNING', 'UNKNOWN'].includes(active.status)) this.activeRequestId = data.activeTurn.requestId;
        }
      }
    },
    async readTurn(requestId, controller = null) {
      const sessionId = this.sessionId;
      const data = await this.request('turn/getSession', { sessionId, requestId });
      if (this.disposed || this.sessionId !== sessionId || (controller && this.streamController !== controller)) return;
      this.acceptSession(data, 'confirm');
      this.unresolved = data.accessible && !data.turns.length;
      if (this.unresolved) {
        this.activeRequestId = requestId;
        const turn = this.turns.find((item) => item.requestId === requestId);
        if (turn) {
          turn.status = 'UNKNOWN';
          turn.confirmed = false;
          turn.result = null;
        }
        this.notice = this.$t('ai.requestNotAccepted');
      }
    },
    scheduleRecovery() {
      const sessionId = this.sessionId;
      const requestId = this.activeRequestId;
      this.recoveryTimer = setTimeout(async () => {
        this.recoveryTimer = null;
        if (!this.needsRecovery || this.sessionId !== sessionId || this.activeRequestId !== requestId) return;
        const turn = this.turns.find((item) => item.requestId === requestId);
        if (turn) await this.transmit(turn, true);
      }, this.recoveryDelay);
    },
    async stop() {
      if (!this.activeRequestId || this.stopping) return;
      this.stopping = true;
      const requestId = this.activeRequestId;
      try {
        await this.request('turn/cancelTurn', { sessionId: this.sessionId, requestId });
        this.streamController?.abort();
        await this.readTurn(requestId);
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.stopping = false;
      }
    },
    async loadOlder() {
      if (this.busy || !this.turns[0]?.id) return;
      this.busy = true;
      const sessionId = this.sessionId;
      const body = this.$refs.body;
      const height = body.scrollHeight;
      const top = body.scrollTop;
      try {
        const data = await this.request('turn/getSession', { sessionId, beforeTurnId: this.turns[0].id, pageSize: 20 });
        if (!this.disposed && this.sessionId === sessionId) {
          this.acceptSession(data, 'older');
          await this.$nextTick();
          if (!this.disposed) body.scrollTop = top + body.scrollHeight - height;
        }
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.busy = false;
      }
    },
    userInputAnswers(turn) {
      const answer = turn.result.userInputAnswer;
      if (answer) return answer.answers.map((item) => ({ ...item, questionId: item.question_id }));
      return [];
    },
    userInputState(turn, turns = this.turns) {
      if (turn.result.userInputAnswer) return 'answered';
      const next = turns[turns.findIndex((item) => item.requestId === turn.requestId) + 1];
      if (!next) return 'pending';
      return 'expired';
    },
    async answerUserInput(turn, payload) {
      if (!this.canStartTurn || this.userInputState(turn) !== 'pending' || this.submittingUserInputRequestId === payload.requestId) return;
      const answer = {
        schema_version: 2,
        request_id: payload.requestId,
        request_history_id: payload.requestHistoryId,
        answer_text: payload.answerText,
        answers: payload.answers.map((item) => ({ question_id: item.questionId, question: item.question, values: item.values }))
      };
      const result = turn.result;
      const previousAnswer = result.userInputAnswer;
      result.userInputAnswer = answer;
      this.submittingUserInputRequestId = payload.requestId;
      try {
        const accepted = await this.send(answer);
        if (!accepted) {
          const question = this.turns.find((item) => item.requestId === turn.requestId);
          if (question?.result === result) result.userInputAnswer = previousAnswer;
        }
      } finally {
        if (this.submittingUserInputRequestId === payload.requestId) this.submittingUserInputRequestId = '';
      }
    },
    sqlOutput(turn) {
      if (['RUNNING', 'UNKNOWN'].includes(turn.status)) return '';
      if (turn.confirmed && turn.status === 'COMPLETED') return turn.result.sql || '';
      const outputs = (turn.activities || [])
        .filter((activity) => activity.toolName === 'generateSql' && activity.status === 'OK')
        .map((activity) => activity.result.sql.replace(/^[\r\n]+|[\r\n]+$/g, ''));
      return outputs.join('\n');
    },
    async useResult(turn, action) {
      if (this.busy || (action === 'insert' && !this.targetActive)) return;
      const target = this.binding;
      this.busy = true;
      this.notice = '';
      try {
        const data = await this.request('turn/getSession', { sessionId: this.sessionId, requestId: turn.requestId });
        if (this.disposed) return;
        this.acceptSession(data, 'confirm');
        const confirmed = data.turns?.find((item) => item.requestId === turn.requestId && !['RUNNING', 'UNKNOWN'].includes(item.status));
        if (!data.accessible || !confirmed) return;
        const sql = this.sqlOutput(confirmed);
        if (!sql) return;
        if (action === 'insert') {
          if (!this.targetActive || this.binding !== target) return;
          this.$emit('insert', { binding: target, sql });
        } else {
          await navigator.clipboard.writeText(sql);
        }
        if (this.disposed) return;
        const key = `${turn.requestId}:${action}`;
        clearTimeout(this.resultFeedbackTimers.get(key));
        this.resultFeedback[key] = true;
        this.resultFeedbackTimers.set(
          key,
          setTimeout(() => {
            delete this.resultFeedback[key];
            this.resultFeedbackTimers.delete(key);
          }, 2000)
        );
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.busy = false;
      }
    },
    openHistory() {
      this.historyOpen = true;
      this.loadHistory(true);
    },
    historyTitle(item) {
      if (!item.accessible) return `${this.$t('ai.inaccessible')} · ${item.id}`;
      return item.summary || item.engineName;
    },
    historyScroll() {
      const body = this.$refs.historyBody;
      if (!body || this.historyError) return;
      if (body.scrollHeight - body.scrollTop - body.clientHeight < 80) this.loadHistory();
    },
    async loadHistory(reset = false) {
      if (!reset && (this.historyLoading || !this.historyHasMore)) return;
      clearTimeout(this.historySearchTimer);
      const sequence = ++this.historySequence;
      if (reset) {
        this.historyItems = [];
        this.historyCursor = null;
        this.historyHasMore = true;
        if (this.$refs.historyBody) this.$refs.historyBody.scrollTop = 0;
      }
      this.historyLoading = true;
      this.historyError = '';
      try {
        const data = await this.request('turn/listSessions', {
          pageSize: 20,
          keyword: this.historyKeyword.trim(),
          ...this.historyCursor
        });
        if (this.disposed || sequence !== this.historySequence) return;
        const existing = new Set(this.historyItems.map((item) => item.id));
        this.historyItems.push(...data.items.filter((item) => !existing.has(item.id)));
        this.historyHasMore = data.hasMore;
        this.historyCursor = { beforeModifiedAt: data.nextModifiedAt, beforeId: data.nextId };
      } catch (error) {
        if (!this.disposed && sequence === this.historySequence) this.historyError = aiErrorMessage(error);
      } finally {
        if (!this.disposed && sequence === this.historySequence) {
          this.historyLoading = false;
          this.$nextTick(() => this.historyScroll());
        }
      }
    },
    async restore(item) {
      if (this.busy) return;
      this.cancelEdit();
      this.loading = true;
      this.busy = true;
      this.historyOpen = false;
      this.historyError = '';
      this.error = '';
      try {
        await this.$nextTick();
        if (this.disposed) return;
        this.$refs.body.scrollTop = 0;
        const capabilities = await this.request('turn/getCapabilities');
        if (this.disposed) return;
        this.capabilities = capabilities;
        if (!this.sessionId) this.contextUsage = { usedTokens: 0, maxTokens: capabilities.contextTokens };
        this.models = capabilities.models;
        const data = await this.request('turn/getSession', { sessionId: item.id, pageSize: 20 });
        if (this.disposed) return;
        if (this.sessionId) this.sessionDrafts[this.sessionId] = this.prompt;
        this.detachStream();
        this.unresolved = false;
        this.prompt = this.sessionDrafts[data.id] || '';
        this.sessionId = data.id;
        this.followTail = true;
        this.error = '';
        this.acceptSession(data, 'reset');
        if (data.accessible) {
          this.sessionEngineId = data.engineId;
          this.sessionVersion = data.engineVersion;
        }
        this.notice = '';
        await this.$nextTick();
        if (!this.disposed) this.$refs.body.scrollTop = this.$refs.body.scrollHeight;
      } catch (error) {
        if (!this.disposed) this.error = aiErrorMessage(error);
      } finally {
        this.loading = false;
        this.busy = false;
        if (this.needsRecovery) {
          clearTimeout(this.recoveryTimer);
          this.scheduleRecovery();
        }
      }
    },
    askDelete(item) {
      this.deleteTarget = item;
      this.deleteOpen = true;
      this.historyError = '';
    },
    async deleteConversation() {
      if (this.busy || !this.deleteTarget) return;
      this.busy = true;
      this.historyError = '';
      try {
        await this.request('turn/deleteSession', { sessionId: this.deleteTarget.id });
        if (this.disposed) return;
        if (this.sessionId === this.deleteTarget.id) this.resetConversation();
        this.deleteOpen = false;
        this.historyOpen = true;
        await this.loadHistory(true);
      } catch (error) {
        if (!this.disposed) this.historyError = aiErrorMessage(error);
      } finally {
        this.busy = false;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.ai-sql-panel {
  display: flex;
  flex-direction: column;
  min-width: 0;
  height: 100%;
  p {
    margin: 0;
  }
  .page-shell__body {
    flex: 1;
    min-height: 0;
    padding: 16px 24px;
    overflow-y: auto;
    overscroll-behavior: contain;
  }
  .ai-messages {
    position: relative;
    display: flex;
    flex-direction: column;
    &--loading {
      overflow: hidden;
    }
  }
  .ai-conversation-content {
    display: flex;
    flex: 1;
    flex-direction: column;
    &--preparing {
      opacity: 0;
      pointer-events: none;
    }
  }
  .ai-conversation-loading {
    position: absolute;
    inset: 0;
    z-index: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 12px;
    background: var(--bg-primary);
    color: var(--text-secondary);
    font-size: 14px;
    > .anticon {
      color: var(--primary-color);
      font-size: 32px;
    }
  }
  .ai-panel-heading {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-shrink: 0;
    gap: 8px;
    padding: 12px 16px;
  }
  .ai-panel-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 500;
  }
  .ai-panel-heading .ai-panel-actions {
    margin: 0;
    gap: 4px;
  }
  .ai-icon-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    width: 32px;
    height: 32px;
    padding: 0;
    color: var(--text-secondary);
  }
  .ai-icon-button[disabled] {
    opacity: 0.45;
  }
  .ai-inline-notice,
  .ai-reply-meta,
  small {
    color: var(--text-secondary);
  }
  .ai-inline-notice {
    margin: 8px 0;
    overflow-wrap: anywhere;
  }
  .ai-empty-chat {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 16px;
    padding: 32px 0;
    color: var(--text-secondary);
  }
  .ai-prompt-presets {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(144px, 1fr));
    gap: 12px;
    width: 100%;
    max-width: 400px;
    margin-top: 8px;
  }
  .ai-prompt-preset {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    min-height: 48px;
    height: auto;
    padding: 12px 16px;
    border: 0;
    border-radius: 6px;
    background: var(--bg-tertiary);
    color: var(--text-primary);
    font-size: 14px;
    text-align: left;
    white-space: normal;
    .anticon {
      color: var(--text-secondary);
      flex-shrink: 0;
    }
  }
  .ai-turn {
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
    gap: 8px;
    padding: 8px 0 32px;
    overflow-wrap: anywhere;
    line-height: 1.75;
    font-size: 16px;
    details {
      color: var(--text-secondary);
      font-size: 12px;
    }
    summary {
      cursor: pointer;
    }
    ul {
      margin: 0;
      padding-left: 20px;
    }
  }
  .ai-question {
    font-size: 16px;
    align-self: flex-end;
    max-width: 88%;
    margin-bottom: 0;
    background: var(--bg-tertiary);
    border-radius: 16px;
    padding: 10px 16px;
    white-space: pre-wrap;
  }
  .ai-message-edit {
    width: 100%;
    min-width: 0;
  }
  .ai-edit-actions {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    flex-wrap: wrap;
    gap: 8px;
    margin-top: 8px;
  }
  .ai-assistant-message {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  .ai-user-message {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    margin-bottom: 12px;
  }
  .ai-result-action-content {
    display: inline-grid;
    align-items: center;
    justify-items: center;
    > * {
      grid-row: 1;
      grid-column: 1;
    }
  }
  .ai-result-action-success {
    display: inline-flex;
    align-items: center;
    gap: 4px;
  }
  .ai-result-action-hidden {
    visibility: hidden;
  }
  .ai-message-actions {
    display: flex;
    gap: 4px;
  }
  .ai-reply-actions {
    align-items: center;
    flex-wrap: wrap;
    margin-top: 8px;
    color: var(--text-secondary);
    time {
      margin-left: 8px;
      font-size: 12px;
      white-space: nowrap;
    }
  }
  .ai-user-actions {
    opacity: 0;
  }
  .ai-user-message:hover .ai-user-actions,
  .ai-user-message:focus-within .ai-user-actions {
    opacity: 1;
  }
  .ai-scroll-bottom {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    position: absolute;
    top: -40px;
    left: calc(50% - 18px);
    width: 36px;
    height: 36px;
    padding: 0;
    border-radius: 50%;
  }
  .ai-markdown {
    font-size: 16px;
    :deep(p),
    :deep(li) {
      font-size: inherit;
    }
    :deep(p) {
      margin: 0 0 12px;
    }
    :deep(p:last-child) {
      margin-bottom: 0;
    }
    :deep(h1),
    :deep(h2),
    :deep(h3) {
      font-size: 16px;
      font-weight: 500;
      margin: 16px 0 8px;
    }
    :deep(ul),
    :deep(ol) {
      padding-left: 20px;
      margin: 8px 0;
    }
    :deep(ul) {
      list-style-type: disc;
    }
    :deep(ol) {
      list-style-type: decimal;
    }
    :deep(pre) {
      overflow-x: auto;
      padding: 12px;
      background: var(--bg-tertiary);
      border-radius: 6px;
    }
    :deep(code) {
      font-family: Menlo, Monaco, monospace;
      font-size: 13px;
    }
    :deep(table) {
      display: block;
      max-width: 100%;
      overflow-x: auto;
      border-collapse: collapse;
    }
    :deep(td),
    :deep(th) {
      padding: 8px;
      border: 1px solid var(--border-light);
    }
  }
  .ai-sql-result {
    min-width: 0;
    overflow: hidden;
    border: 1px solid var(--border-primary);
    border-radius: 6px;
    background: var(--bg-card);
  }
  .ai-sql-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 8px;
    padding: 4px 8px;
    border-bottom: 1px solid var(--border-light);
    background: var(--bg-secondary);
    color: var(--text-secondary);
    font-size: 12px;
    .ai-panel-actions {
      margin: 0;
      gap: 4px;
    }
  }
  .ai-composer {
    position: relative;
    flex-shrink: 0;
    padding: 8px 16px 12px;
    background: var(--bg-card);
  }
  .ai-composer-input {
    border: 1px solid var(--border-light);
    border-radius: 10px;
    padding: 12px;
    &:focus-within {
      border-color: var(--primary-color);
    }
    :deep(textarea.ant-input) {
      padding: 0;
      border: 0 !important;
      border-radius: 0 !important;
      box-shadow: none !important;
      outline: none;
      resize: none;
      background: transparent;
      line-height: 1.6;
    }
  }
  .ai-sql-selection {
    display: inline-flex;
    align-items: center;
    align-self: flex-end;
    gap: 8px;
    max-width: 100%;
    height: auto;
    margin-bottom: 8px;
    padding: 4px 8px;
    border: 1px solid var(--border-light);
    border-radius: 6px;
    background: var(--bg-secondary);
    font-size: 12px;
    line-height: 20px;
    color: var(--text-primary);
    text-align: left;
    > :first-child,
    .ai-selection-lines {
      flex-shrink: 0;
    }
  }
  .ai-selection-title {
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .ai-selection-lines {
    color: var(--text-secondary);
  }
  .ai-composer-actions,
  .ai-composer-tools {
    display: flex;
    align-items: center;
    gap: 8px;
    min-width: 0;
  }
  .ai-composer-actions {
    justify-content: space-between;
    margin-top: 8px;
  }
  .ai-composer-tools {
    flex: 1;
    gap: 4px;
    overflow: hidden;
  }
  .ai-model-select {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    min-width: 0;
    max-width: 100%;
    padding: 0 4px;
    color: var(--text-primary);
    :deep(> span:first-child) {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    :deep(.anticon) {
      font-size: 10px;
      flex-shrink: 0;
    }
  }
  .ai-context-usage {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    width: 28px;
    height: 32px;
    padding: 0;
    :deep(.ant-progress) {
      line-height: 1;
    }
  }
  .ai-stop-icon {
    width: 12px;
    height: 12px;
    background: currentColor;
    border-radius: 2px;
  }
  .ai-submit-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    width: 32px;
    height: 32px;
    padding: 0;
    border-radius: 50%;
  }
  .ai-composer-hint {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 4px;
    padding-top: 8px;
    color: var(--text-secondary);
    font-size: 11px;
  }
  .ai-history-search {
    padding: 0 24px 8px;
  }
  .ai-history-status {
    padding: 16px 8px;
    color: var(--text-secondary);
    text-align: center;
  }
  .ai-history-row {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 4px 8px;
    border-radius: 6px;
  }
  .ai-history-menu {
    opacity: 0;
  }
  .ai-history-row:hover .ai-history-menu,
  .ai-history-row:focus-within .ai-history-menu {
    opacity: 1;
  }
  .ai-history-row--active {
    background: var(--bg-tertiary);
  }
  .ai-history-open {
    flex: 1;
    min-width: 0;
    height: auto;
    padding: 8px 0;
    text-align: left;
    color: var(--text-primary);
    white-space: normal;
    :deep(> span) {
      display: block;
      min-width: 0;
    }
    small {
      display: block;
      font-size: 12px;
      font-weight: 400;
    }
  }
  .ai-history-title {
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  @media (hover: hover) and (pointer: fine) {
    .ai-reply-actions {
      opacity: 0;
      transition: opacity 150ms ease;
    }
    .ai-assistant-message:hover .ai-reply-actions,
    .ai-assistant-message:focus-within .ai-reply-actions {
      opacity: 1;
    }
  }
  @media (hover: none) {
    .ai-user-actions,
    .ai-history-menu {
      opacity: 1;
    }
  }
  @media (prefers-reduced-motion: reduce) {
    .ai-reply-actions {
      transition: none;
    }
    .ai-pending span {
      animation: none;
    }
  }
  @media (max-width: 767px) {
    .ai-history-search {
      padding: 0 16px 8px;
    }
    .page-shell__body {
      padding: 12px 16px;
    }
    .ai-composer {
      padding: 8px 12px;
    }
    .ai-icon-button,
    .ai-submit-button {
      width: 36px;
      height: 36px;
    }
  }
}

.ai-context-usage-detail {
  display: flex;
  flex-direction: column;
  gap: 4px;
  max-width: 280px;
  color: var(--text-secondary);
  strong {
    color: var(--text-primary);
    font-weight: 500;
  }
}
.ai-model-menu {
  max-width: calc(100vw - 32px);
  min-width: 200px;
  max-height: 280px;
  overflow-y: auto;
}
.ai-model-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  small {
    display: block;
    font-size: 12px;
    color: var(--text-secondary);
  }
}

.ai-panel-actions {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 8px;
}
.ai-panel-error {
  color: var(--error-color);
  overflow-wrap: anywhere;
}
.ai-selection-detail-source {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
  overflow-wrap: anywhere;
  color: var(--text-secondary);
}
.ai-selection-detail-code {
  overflow: hidden;
  border: 1px solid var(--border-light);
  border-radius: 6px;
}
</style>
