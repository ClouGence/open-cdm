<template>
  <section class="ai-user-input" :aria-label="request.title || $t('ai.userInputDefaultTitle')">
    <div class="ai-user-input-heading">
      <span>{{ request.title || $t('ai.userInputDefaultTitle') }}</span>
      <small v-if="state !== 'pending'">{{ $t(state === 'answered' ? 'ai.userInputAnswered' : 'ai.userInputExpired') }}</small>
    </div>
    <fieldset v-for="question in request.questions" :key="question.id" class="ai-user-input-question">
      <legend>
        {{ question.question }}
        <span v-if="state === 'pending' && question.required" class="ai-user-input-required" aria-hidden="true">*</span>
      </legend>
      <p v-if="state !== 'pending'" class="ai-user-input-answer" :class="{ 'ai-user-input-no-answer': !answerValues(question.id).length }">
        {{ answerValues(question.id).join('；') || $t('ai.userInputNoAnswer') }}
      </p>
      <template v-else>
        <div
          v-if="question.type !== 'FREE_TEXT'"
          class="ai-user-input-options"
          :role="question.type === 'SINGLE_CHOICE' ? 'radiogroup' : 'group'"
          :aria-label="question.question"
        >
          <button
            v-for="option in question.options"
            :key="option.id"
            type="button"
            :role="question.type === 'SINGLE_CHOICE' ? 'radio' : 'checkbox'"
            :aria-checked="isSelected(question.id, option.id)"
            :disabled="disabled || submitting"
            @click="selectOption(question, option.id)"
          >
            <span>{{ option.label }}</span>
            <small v-if="option.description">{{ option.description }}</small>
          </button>
        </div>
        <button
          v-if="(question.type === 'FREE_TEXT' || question.allow_custom) && mobileLayout"
          type="button"
          class="ai-user-input-mobile-trigger"
          :class="{ 'has-answer': customAnswers[question.id] }"
          :aria-label="question.question"
          :disabled="disabled || submitting"
          @click="editMobileAnswer(question)"
        >
          <span>
            {{
              customAnswers[question.id] || $t(question.type === 'FREE_TEXT' ? 'ai.userInputFreeTextPlaceholder' : 'ai.userInputCustomPlaceholder')
            }}
          </span>
          <RightOutlined aria-hidden="true" />
        </button>
        <textarea
          v-else-if="question.type === 'FREE_TEXT' || question.allow_custom"
          v-model="customAnswers[question.id]"
          :aria-label="question.question"
          :placeholder="$t(question.type === 'FREE_TEXT' ? 'ai.userInputFreeTextPlaceholder' : 'ai.userInputCustomPlaceholder')"
          :aria-required="question.required"
          :disabled="disabled || submitting"
          rows="2"
        ></textarea>
      </template>
    </fieldset>
    <p v-if="state === 'pending' && validationMessage" role="alert" class="ai-user-input-error">{{ validationMessage }}</p>
    <div v-if="state === 'pending'" class="ai-user-input-footer">
      <Button
        type="primary"
        :disabled="disabled || submitting"
        :loading="submitting"
        :aria-label="request.submit_label || $t('ai.userInputSubmit')"
        @click="submit"
      >
        {{ request.submit_label || $t('ai.userInputSubmit') }}
      </Button>
    </div>
    <CCModal
      v-model="mobileEditorOpen"
      wrap-class-name="ai-user-input-mobile-editor"
      :title="request.title || $t('ai.userInputDefaultTitle')"
      :draggable="false"
      @on-cancel="mobileEditorOpen = false"
    >
      <div v-if="mobileQuestion" class="ai-user-input-mobile-question" @click.self="mobileEditorOpen = false">
        <small v-if="mobileQuestion.label">{{ mobileQuestion.label }}</small>
        <p>{{ mobileQuestion.question }}</p>
      </div>
      <template #footer>
        <div v-if="mobileQuestion" class="ai-user-input-mobile-composer">
          <textarea
            ref="mobileAnswer"
            v-model="mobileEditorValue"
            :aria-label="mobileQuestion.question"
            :aria-required="mobileQuestion.required"
            :placeholder="$t(mobileQuestion.type === 'FREE_TEXT' ? 'ai.userInputFreeTextPlaceholder' : 'ai.userInputCustomPlaceholder')"
            :disabled="disabled || submitting"
            rows="3"
          ></textarea>
          <Button type="primary" :disabled="disabled || submitting" :aria-label="$t('ai.save')" @click="saveMobileAnswer">
            <CheckOutlined aria-hidden="true" />
          </Button>
        </div>
      </template>
    </CCModal>
  </section>
</template>

<script>
import { markRaw } from 'vue';
import { CheckOutlined, RightOutlined } from '@ant-design/icons-vue';
import CCModal from '@/components/ui/CCModal.vue';

export default {
  name: 'AiUserInputCard',
  components: { CheckOutlined, RightOutlined, CCModal },
  props: {
    request: { type: Object, required: true },
    state: { type: String, default: 'pending' },
    answers: { type: Array, default: () => [] },
    disabled: Boolean,
    submitting: Boolean,
    requestHistoryId: { type: String, required: true }
  },
  emits: ['submit'],
  data() {
    return {
      selections: {},
      customAnswers: {},
      validationMessage: '',
      viewportQuery: null,
      narrowViewport: window.matchMedia('(max-width: 767px)').matches,
      mobileEditorOpen: false,
      mobileQuestion: null,
      mobileEditorValue: ''
    };
  },
  computed: {
    mobileLayout() {
      return this.narrowViewport || /Android|iP(hone|ad|od)/.test(navigator.userAgent);
    }
  },
  watch: {
    mobileEditorOpen(value) {
      if (value) this.$nextTick(() => this.$refs.mobileAnswer?.focus());
    },
    state(value) {
      if (value !== 'pending') this.mobileEditorOpen = false;
    },
    disabled(value) {
      if (value) this.mobileEditorOpen = false;
    },
    mobileLayout(value) {
      if (!value) this.mobileEditorOpen = false;
    }
  },
  mounted() {
    this.viewportQuery = markRaw(window.matchMedia('(max-width: 767px)'));
    this.viewportQuery.addEventListener('change', this.updateViewport);
  },
  beforeUnmount() {
    this.viewportQuery.removeEventListener('change', this.updateViewport);
  },
  methods: {
    updateViewport(event) {
      this.narrowViewport = event.matches;
    },
    editMobileAnswer(question) {
      if (this.disabled || this.submitting) return;
      this.mobileQuestion = question;
      this.mobileEditorValue = this.customAnswers[question.id] || '';
      this.mobileEditorOpen = true;
    },
    saveMobileAnswer() {
      if (this.disabled || this.submitting) return;
      this.customAnswers[this.mobileQuestion.id] = this.mobileEditorValue;
      this.validationMessage = '';
      this.$refs.mobileAnswer?.blur();
      this.mobileEditorOpen = false;
    },
    isSelected(questionId, optionId) {
      return (this.selections[questionId] || []).includes(optionId);
    },
    selectOption(question, optionId) {
      if (this.disabled || this.submitting) return;
      this.validationMessage = '';
      if (question.type === 'SINGLE_CHOICE') {
        if (this.isSelected(question.id, optionId)) delete this.selections[question.id];
        else this.selections[question.id] = [optionId];
        return;
      }
      const selected = [...(this.selections[question.id] || [])];
      const index = selected.indexOf(optionId);
      if (index >= 0) selected.splice(index, 1);
      else selected.push(optionId);
      this.selections[question.id] = selected;
    },
    answerValues(questionId) {
      return this.answers.find((answer) => answer.questionId === questionId)?.values || [];
    },
    submit() {
      if (this.disabled || this.submitting) return;
      const lines = [];
      const answers = [];
      for (const question of this.request.questions) {
        const values = question.options.filter((option) => this.isSelected(question.id, option.id)).map((option) => option.label);
        const custom = (this.customAnswers[question.id] || '').trim();
        if (custom) values.push(custom);
        if (question.required && !values.length) {
          this.validationMessage = this.$t('ai.userInputRequiredHint');
          return;
        }
        lines.push(`> ${question.question}\n${values.join('；') || this.$t('ai.userInputNoAnswer')}`);
        answers.push({ questionId: question.id, question: question.question, values });
      }
      const answerText = lines.join('\n\n');
      this.validationMessage = '';
      this.$emit('submit', { requestId: this.request.request_id, requestHistoryId: this.requestHistoryId, answers, answerText });
    }
  }
};
</script>

<style scoped lang="less">
.ai-user-input {
  padding: 16px;
  margin-top: 16px;
  background: var(--bg-secondary);
  border-radius: 6px;
  overflow-wrap: anywhere;
}
.ai-user-input-heading {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  font-weight: 500;
  small {
    color: var(--text-secondary);
    font-weight: 400;
    flex-shrink: 0;
    padding: 2px 8px;
    background: var(--bg-tertiary);
    border-radius: 6px;
  }
}
.ai-user-input-question {
  border: 0;
  padding: 0;
  margin: 16px 0 0;
  min-width: 0;
  legend {
    font-size: 14px;
    margin-bottom: 8px;
    white-space: normal;
  }
  textarea {
    width: 100%;
    margin-top: 8px;
    padding: 8px 12px;
    border: 1px solid var(--border-light);
    border-radius: 6px;
    background: var(--bg-primary);
    color: var(--text-primary);
    resize: none;
    box-sizing: border-box;
    &:focus-visible {
      outline: 1px solid var(--primary-color);
    }
  }
}
.ai-user-input-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  button {
    max-width: 100%;
    padding: 8px 12px;
    min-height: 40px;
    border: 1px solid var(--border-light);
    border-radius: 6px;
    background: var(--bg-primary);
    color: var(--text-primary);
    text-align: left;
    white-space: normal;
    overflow-wrap: anywhere;
    cursor: pointer;
    display: inline-flex;
    flex-wrap: wrap;
    align-items: center;
    column-gap: 4px;
    &[aria-checked='true'] {
      border-color: var(--primary-color);
      background: var(--bg-tertiary);
    }
    &:disabled {
      cursor: default;
      opacity: 0.6;
    }
  }
  small {
    font-size: 12px;
    color: var(--text-secondary);
  }
}
.ai-user-input-answer {
  margin: 0;
  width: fit-content;
  max-width: 100%;
  padding: 6px 10px;
  border-radius: 6px;
  background: var(--bg-primary);
  white-space: pre-wrap;
  color: var(--text-secondary);
}
.ai-user-input-required {
  margin-left: 4px;
  color: var(--error-color);
}
.ai-user-input-no-answer {
  padding: 0;
  background: transparent;
}
.ai-user-input-mobile-trigger {
  display: flex;
  width: 100%;
  min-height: 48px;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  border: 1px solid var(--border-light);
  border-radius: 6px;
  background: var(--bg-primary);
  color: var(--text-secondary);
  text-align: left;
  cursor: pointer;
  &.has-answer {
    color: var(--text-primary);
  }
  span:first-child {
    flex: 1;
    min-width: 0;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  &:disabled {
    cursor: default;
    opacity: 0.6;
  }
}
.ai-user-input-error {
  margin: 8px 0 0;
  color: var(--error-color);
}
.ai-user-input-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
@media (max-width: 768px) {
  .ai-user-input-options button,
  .ai-user-input-footer :deep(button) {
    min-height: 48px;
  }
}
</style>

<style lang="less">
.ai-user-input-mobile-editor {
  .ant-modal {
    top: 0;
    width: 100% !important;
    max-width: none;
    margin: 0;
    padding: 0;
  }
  .ant-modal-content {
    height: 100dvh;
    display: flex;
    flex-direction: column;
    border-radius: 0;
  }
  .ant-modal-body {
    flex: 1;
    min-height: 0;
    overflow-y: auto;
    p {
      margin-top: 8px;
      overflow-wrap: anywhere;
    }
  }
  .ant-modal-footer {
    padding-bottom: max(16px, env(safe-area-inset-bottom));
  }
  .ai-user-input-mobile-question {
    min-height: 100%;
  }
  .ai-user-input-mobile-composer {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    text-align: left;
    textarea {
      width: 100%;
      min-width: 0;
      padding: 8px 12px;
      border: 1px solid var(--border-light);
      border-radius: 6px;
      background: var(--bg-primary);
      color: var(--text-primary);
      resize: none;
    }
    button.ant-btn {
      width: 48px;
      min-width: 48px !important;
      height: 48px;
      margin: 0;
      flex-shrink: 0;
    }
  }
}
</style>
