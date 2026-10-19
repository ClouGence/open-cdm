<template>
  <div v-if="running || hasDetails" class="ai-activity-log">
    <button v-if="hasDetails" type="button" class="activity-heading" :aria-expanded="expanded" @click="expanded = !expanded">
      <component :is="headerIcon" :class="{ 'activity-pulse': running }" aria-hidden="true" />
      <span :class="{ 'activity-pulse': running }">{{ title }}</span>
      <RightOutlined class="activity-chevron" :class="{ 'activity-expanded': expanded }" aria-hidden="true" />
    </button>
    <div v-else class="activity-heading" role="status">
      <BulbOutlined class="activity-pulse" aria-hidden="true" />
      <span class="activity-pulse">{{ title }}</span>
    </div>
    <div v-if="hasDetails" v-show="expanded" class="activity-timeline">
      <template v-for="(segment, index) in segments" :key="index">
        <div v-if="segment.text" class="activity-interim">
          <slot name="message" :text="segment.text">{{ segment.text }}</slot>
        </div>
        <component v-else :is="segment.activities.length > 1 ? 'details' : 'div'" class="activity-tool-group">
          <summary v-if="segment.activities.length > 1">
            <ToolOutlined aria-hidden="true" />
            <span>{{ groupTitle(segment.activities) }}</span>
            <RightOutlined class="activity-chevron" aria-hidden="true" />
          </summary>
          <div :class="{ 'activity-tools': segment.activities.length > 1 }">
            <div v-for="activity in segment.activities" :key="activity.toolCallId" class="activity-tool">
              <details v-if="activity.objects?.length" class="activity-tool-detail">
                <summary>
                  <component :is="toolIcon(activity)" aria-hidden="true" />
                  <span>{{ $t(`ai.tools.${activity.toolName}`) }}</span>
                  <span class="activity-state" :class="{ 'activity-error': activity.status === 'ERROR' }">{{ toolStatus(activity) }}</span>
                  <RightOutlined class="activity-chevron" aria-hidden="true" />
                </summary>
                <ul class="activity-objects">
                  <li v-for="(object, index) in activity.objects" :key="index">{{ object.name }}</li>
                </ul>
              </details>
              <div v-else class="activity-tool-label">
                <component :is="toolIcon(activity)" :class="{ 'activity-pulse': isToolRunning(activity) }" aria-hidden="true" />
                <span>{{ $t(`ai.tools.${activity.toolName}`) }}</span>
                <span class="activity-state" :class="{ 'activity-error': activity.status === 'ERROR' }">{{ toolStatus(activity) }}</span>
              </div>
            </div>
          </div>
        </component>
      </template>
    </div>
  </div>
</template>

<script>
import {
  BulbOutlined,
  CheckCircleFilled,
  FileTextOutlined,
  QuestionCircleOutlined,
  ReadOutlined,
  RightOutlined,
  SearchOutlined,
  ToolOutlined
} from '@ant-design/icons-vue';

export default {
  name: 'AiActivityLog',
  components: { BulbOutlined, RightOutlined, ToolOutlined },
  props: { turn: { type: Object, required: true } },
  data() {
    return { expanded: false };
  },
  computed: {
    running() {
      return this.turn.status === 'RUNNING';
    },
    activities() {
      return this.turn.activities || [];
    },
    hasDetails() {
      return this.activities.length > 0;
    },
    activeTool() {
      return this.activities.find((activity) => this.isToolRunning(activity));
    },
    title() {
      if (!this.running) return this.$t('ai.toolActivity');
      if (this.turn.phase === 'COMPACTING') return this.$t('ai.phase.COMPACTING');
      if (this.activeTool) return this.$t(`ai.tools.${this.activeTool.toolName}`);
      if (this.turn.draft) return this.$t('ai.responding');
      return this.$t('ai.turnStatus.RUNNING');
    },
    headerIcon() {
      if (!this.running) return CheckCircleFilled;
      if (this.activeTool) return this.toolIcon(this.activeTool);
      return BulbOutlined;
    },
    segments() {
      const segments = [];
      for (const activity of this.activities) {
        if (activity.text) segments.push({ text: activity.text });
        const previous = segments[segments.length - 1];
        if (previous?.activities) previous.activities.push(activity);
        else segments.push({ activities: [activity] });
      }
      return segments;
    }
  },
  watch: {
    'turn.status': {
      immediate: true,
      handler(status) {
        if (status === 'RUNNING') this.expanded = true;
      }
    }
  },
  methods: {
    groupTitle(activities) {
      const names = [...new Set(activities.map((activity) => this.$t(`ai.tools.${activity.toolName}`)))];
      return this.$t('ai.toolCalls', { actions: names.join(this.$t('ai.toolSeparator')), count: activities.length });
    },
    isToolRunning(activity) {
      return this.running && !activity.status;
    },
    toolIcon(activity) {
      if (activity.toolName === 'listTables') return SearchOutlined;
      if (activity.toolName === 'generateSql') return FileTextOutlined;
      if (activity.toolName === 'ask_user_questions') return QuestionCircleOutlined;
      return ReadOutlined;
    },
    toolStatus(activity) {
      if (this.isToolRunning(activity)) return this.$t('ai.toolRunning');
      if (activity.status === 'ERROR') return this.$t('ai.toolFailed');
      if (activity.status === 'OK') return this.$t('ai.toolCompleted');
      return this.$t(`ai.turnStatus.${this.turn.status}`);
    }
  }
};
</script>

<style scoped lang="less">
.ai-activity-log {
  margin-bottom: 16px;
  color: var(--text-secondary);
  font-size: 14px;
  .activity-heading,
  summary,
  .activity-tool-label {
    display: flex;
    align-items: center;
    gap: 8px;
    min-width: 0;
    padding: 4px 0;
    line-height: 24px;
    overflow-wrap: anywhere;
    .anticon {
      flex-shrink: 0;
    }
  }
  button.activity-heading {
    max-width: 100%;
    border: 0;
    background: transparent;
    color: inherit;
    font: inherit;
    text-align: left;
    cursor: pointer;
  }
  summary {
    list-style: none;
    cursor: pointer;
  }
  summary::-webkit-details-marker {
    display: none;
  }
  button:focus-visible,
  summary:focus-visible {
    outline: 2px solid var(--primary-color);
    outline-offset: 2px;
  }
  .activity-chevron {
    font-size: 12px;
    transition: transform 0.15s;
  }
  .activity-expanded,
  details[open] > summary > .activity-chevron {
    transform: rotate(90deg);
  }
  .activity-timeline {
    margin: 8px 0 0 8px;
    padding-left: 16px;
    border-left: 1px solid var(--border-primary);
  }
  .activity-tools {
    margin-left: 8px;
    padding: 8px 0 0 16px;
  }
  .activity-tool,
  .activity-tool-group {
    position: relative;
  }
  .activity-timeline > .activity-tool::before,
  .activity-tool-group::before {
    content: '';
    position: absolute;
    top: 16px;
    left: -19px;
    width: 5px;
    height: 5px;
    border-radius: 50%;
    background: var(--text-secondary);
  }
  .activity-state {
    margin-left: auto;
    flex-shrink: 0;
    font-size: 12px;
  }
  .activity-error {
    color: var(--error-color);
  }
  .activity-objects {
    margin: 8px 0;
    padding: 8px 16px;
    list-style: none;
    background: var(--bg-secondary);
    overflow-wrap: anywhere;
    max-height: 240px;
    overflow-y: auto;
    line-height: 24px;
  }
  .activity-interim {
    margin: 8px 0;
    overflow-wrap: anywhere;
  }
  .activity-pulse {
    animation: activity-pulse 1.6s ease-in-out infinite;
  }
}
@keyframes activity-pulse {
  50% {
    opacity: 0.45;
  }
}
@media (prefers-reduced-motion: reduce) {
  .ai-activity-log .activity-pulse {
    animation: none;
  }
}
</style>
