<template>
  <div class="approval-editor" :class="{ 'page-shell': !embedded, 'approval-editor--embedded': embedded }">
    <div class="approval-editor__body" :class="{ 'page-shell__body': !embedded }">
      <section class="page-section">
        <div class="approval-workspace">
          <div class="approval-canvas-panel">
            <div class="approval-toolbar">
              <h2 class="page-section__title">{{ $t('approval-stages') }}</h2>
              <div v-if="editable" class="approval-toolbar__actions">
                <Button class="approval-toolbar__button" type="primary" ghost icon="md-add" @click="addNode(selectedIndex)">
                  {{ $t('tian-jia') }}
                </Button>
                <Button
                  class="approval-toolbar__button approval-toolbar__button--delete"
                  icon="ios-trash-outline"
                  :disabled="draft.nodes.length === 1"
                  @click="deleteNode(selectedIndex)"
                >
                  {{ $t('shan-chu') }}
                </Button>
              </div>
            </div>
            <div class="approval-canvas-wrap">
              <FlowCanvasZoom :model-value="zoomScale" @update:model-value="setZoom" @fit="fitCanvas" />
              <div ref="canvas" class="approval-canvas" @dragover.prevent="scrollDragEdge($event)" @drop.prevent="dropOnCanvas($event)">
                <div class="approval-stage-shell" :style="{ width: `${chainWidth * zoomScale}px`, height: `${chainHeight * zoomScale}px` }">
                  <div ref="chain" class="approval-chain" :style="{ transform: `translate(-50%, -50%) scale(${zoomScale})` }">
                    <article
                      v-for="(node, index) in draft.nodes"
                      :key="node.nodeId"
                      :data-node-id="node.nodeId"
                      class="approval-node"
                      :class="{ selected: node.nodeId === selectedId, dragging: node.nodeId === draggingId }"
                      :draggable="editable"
                      tabindex="0"
                      :aria-label="$t('approval-stage-index', { index: index + 1 })"
                      @click="selectedId = node.nodeId"
                      @keydown.enter="selectedId = node.nodeId"
                      @dragstart="startDrag(node, $event)"
                      @dragover.prevent
                      @drop.prevent="dropNode(node)"
                      @dragend="draggingId = ''"
                      @pointerdown="startTouchDrag(node, $event)"
                      @pointermove="moveTouchDrag($event)"
                      @pointerup="finishTouchDrag($event)"
                      @pointercancel="cancelTouchDrag($event)"
                    >
                      <div class="approval-node__header">
                        <Icon :type="node.approverType === participantTypes.TEAM ? 'ios-people-outline' : 'ios-person-outline'" />
                        {{ $t('approval-stage-index', { index: index + 1 }) }}
                      </div>
                      <strong class="approval-node__name" :title="participantName(node)">{{ participantName(node) }}</strong>
                      <div class="approval-node__footer">
                        <span>
                          {{
                            node.approverType === participantTypes.TEAM
                              ? $t('approval-required-count', { count: node.requiredCount })
                              : $t('approval-person-rule')
                          }}
                        </span>
                        <div v-if="editable" class="approval-node__actions">
                          <button type="button" :aria-label="$t('tian-jia')" @click.stop="addNode(index)"><Icon type="md-add" /></button>
                          <button type="button" :aria-label="$t('shan-chu')" :disabled="draft.nodes.length === 1" @click.stop="deleteNode(index)">
                            <Icon type="ios-trash-outline" />
                          </button>
                        </div>
                      </div>
                    </article>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <aside v-if="selected" class="page-aside approval-inspector">
            <header class="approval-inspector__header">
              <h2 class="page-section__title">{{ $t('approval-stages') }}</h2>
            </header>
            <div class="approval-inspector__body">
              <Form :key="selectedId" :model="selected" label-position="top">
                <FormItem
                  :label="$t('approval-participant-type')"
                  prop="approverType"
                  :rules="[{ required: true, message: $t('approval-complete-configuration'), trigger: 'change' }]"
                  required
                >
                  <RadioGroup :model-value="selected.approverType" @on-change="changeNodeType">
                    <Radio :label="participantTypes.TEAM" :disabled="!editable">{{ $t('approval-team') }}</Radio>
                    <Radio :label="participantTypes.PERSON" :disabled="!editable">{{ $t('approval-person') }}</Radio>
                  </RadioGroup>
                </FormItem>
                <FormItem
                  v-if="selected.approverType === participantTypes.TEAM"
                  :label="$t('approval-team')"
                  prop="teamId"
                  :rules="[{ required: true, message: $t('approval-select-team'), trigger: 'change' }]"
                  required
                >
                  <Select
                    v-model="selected.teamId"
                    transfer-class-name="approval-team-dropdown"
                    filterable
                    :disabled="!editable"
                    :placeholder="$t('approval-select-team')"
                  >
                    <Option
                      v-for="team in teams"
                      :key="team.teamId"
                      :value="team.teamId"
                      :label="team.teamName + $t('approval-member-count', { count: team.memberUids.length })"
                      :disabled="Boolean(teamDisabledReason(team, 'canApprove'))"
                    >
                      <Tooltip
                        class="approval-team-option"
                        :content="teamDisabledReason(team, 'canApprove')"
                        :disabled="!teamDisabledReason(team, 'canApprove')"
                        :max-width="320"
                        placement="top"
                        transfer
                      >
                        <span class="approval-team-option__name">
                          {{ team.teamName }}{{ $t('approval-member-count', { count: team.memberUids.length }) }}
                        </span>
                      </Tooltip>
                    </Option>
                  </Select>
                </FormItem>
                <FormItem
                  v-else
                  :label="$t('shen-pi-ren')"
                  prop="userUid"
                  :rules="[{ required: true, message: $t('approval-select-person'), trigger: 'change' }]"
                  required
                >
                  <Select v-model="selected.userUid" filterable :disabled="!editable" :placeholder="$t('approval-select-person')">
                    <Option v-for="person in approvers" :key="person.userUid" :value="person.userUid">{{ person.userName }}</Option>
                  </Select>
                </FormItem>
                <FormItem
                  v-if="selected.approverType === participantTypes.TEAM"
                  :label="$t('approval-required')"
                  prop="requiredCount"
                  :rules="[{ type: 'number', required: true, min: 1, message: $t('approval-complete-configuration'), trigger: 'change' }]"
                  required
                >
                  <InputNumber
                    v-model="selected.requiredCount"
                    :min="1"
                    :max="Math.max(1, selectedMemberCount)"
                    :precision="0"
                    :disabled="!editable"
                  />
                  <span class="approval-count-label">{{ $t('approval-of-members', { count: selectedMemberCount }) }}</span>
                </FormItem>
              </Form>
              <section class="page-section approval-confirmation">
                <h2 class="page-section__title">{{ $t('approval-confirmation-stage') }}</h2>
                <Form :model="draft" label-position="top" class="approval-confirmation__fields">
                  <FormItem
                    :label="$t('approval-execution-mode')"
                    prop="executionMode"
                    :rules="[{ required: true, message: $t('approval-complete-configuration'), trigger: 'change' }]"
                    required
                  >
                    <div class="approval-execution-options">
                      <RadioGroup :model-value="draft.executionMode" @on-change="changeExecutionMode">
                        <Radio :label="executionModes.MANUAL" :disabled="!editable">{{ $t('approval-execution-manual') }}</Radio>
                        <Radio :label="executionModes.AUTO" :disabled="!editable">{{ $t('approval-execution-auto') }}</Radio>
                      </RadioGroup>
                      <Poptip trigger="hover" transfer popper-class="description-popup" :content="$t('approval-execution-auto-hint')">
                        <Icon type="ios-help-circle-outline" :size="16" role="img" :aria-label="$t('shuo-ming')" />
                      </Poptip>
                    </div>
                  </FormItem>
                  <FormItem
                    v-if="draft.executionMode === executionModes.MANUAL"
                    :label="$t('approval-confirmation-participants')"
                    :prop="
                      draft.executionAssignment.participantType === participantTypes.TEAM
                        ? 'executionAssignment.teamId'
                        : 'executionAssignment.userUid'
                    "
                    :rules="[{ required: true, message: $t('approval-complete-configuration'), trigger: 'change' }]"
                    required
                  >
                    <div class="approval-confirmation__fields">
                      <RadioGroup :model-value="draft.executionAssignment.participantType" @on-change="changeExecutionType">
                        <Radio :label="participantTypes.TEAM" :disabled="!editable">{{ $t('approval-team') }}</Radio>
                        <Radio :label="participantTypes.PERSON" :disabled="!editable">{{ $t('approval-person') }}</Radio>
                      </RadioGroup>
                      <Select
                        v-if="draft.executionAssignment.participantType === participantTypes.TEAM"
                        v-model="draft.executionAssignment.teamId"
                        transfer-class-name="approval-team-dropdown"
                        filterable
                        :disabled="!editable"
                        :placeholder="$t('approval-select-team')"
                      >
                        <Option
                          v-for="team in teams"
                          :key="team.teamId"
                          :value="team.teamId"
                          :label="team.teamName + $t('approval-member-count', { count: team.memberUids.length })"
                          :disabled="Boolean(teamDisabledReason(team, 'canExecute'))"
                        >
                          <Tooltip
                            class="approval-team-option"
                            :content="teamDisabledReason(team, 'canExecute')"
                            :disabled="!teamDisabledReason(team, 'canExecute')"
                            :max-width="320"
                            placement="top"
                            transfer
                          >
                            <span class="approval-team-option__name">
                              {{ team.teamName }}{{ $t('approval-member-count', { count: team.memberUids.length }) }}
                            </span>
                          </Tooltip>
                        </Option>
                      </Select>
                      <Select
                        v-else
                        v-model="draft.executionAssignment.userUid"
                        filterable
                        :disabled="!editable"
                        :placeholder="$t('approval-select-person')"
                      >
                        <Option v-for="person in executors" :key="person.userUid" :value="person.userUid">{{ person.userName }}</Option>
                      </Select>
                      <span class="approval-confirmation__rule">
                        {{
                          draft.executionAssignment.participantType === participantTypes.TEAM ? $t('approval-any-member') : $t('approval-person-rule')
                        }}
                      </span>
                    </div>
                  </FormItem>
                </Form>
              </section>
            </div>
          </aside>
        </div>
      </section>
    </div>
    <footer v-if="!embedded" class="page-shell__footer">
      <Button @click="$emit('back')">{{ $t('qu-xiao') }}</Button>
      <Button v-if="editable" type="primary" :loading="saving" @click="save">{{ $t('bao-cun') }}</Button>
    </footer>
  </div>
</template>
<script>
import FlowCanvasZoom from '@/components/widgets/FlowCanvasZoom.vue';
import { createUuid } from '@/utils/uuid';
import { APPROVAL_FLOW_CONTEXT_TYPE, APPROVAL_FLOW_SCHEMA_VERSION, APPROVAL_PARTICIPANT_TYPES, APPROVAL_EXECUTION_MODES } from './constant';
function newNode() {
  return { nodeId: createUuid(), approverType: APPROVAL_PARTICIPANT_TYPES.TEAM, teamId: '', userUid: null, requiredCount: 1 };
}
export default {
  name: 'ApprovalFlowEditor',
  components: { FlowCanvasZoom },
  props: {
    definition: { type: Object, default: null },
    teams: { type: Array, default: () => [] },
    participants: { type: Array, default: () => [] },
    editable: Boolean,
    embedded: Boolean,
    saving: Boolean
  },
  emits: ['save', 'back', 'update:draft'],
  data() {
    const draft = this.definition
      ? JSON.parse(JSON.stringify(this.definition))
      : {
          contextType: APPROVAL_FLOW_CONTEXT_TYPE,
          schemaVersion: APPROVAL_FLOW_SCHEMA_VERSION,
          executionMode: APPROVAL_EXECUTION_MODES.MANUAL,
          nodes: [newNode()],
          executionAssignment: { participantType: APPROVAL_PARTICIPANT_TYPES.TEAM, teamId: '', userUid: null }
        };
    return {
      participantTypes: APPROVAL_PARTICIPANT_TYPES,
      executionModes: APPROVAL_EXECUTION_MODES,
      draft,
      selectedId: draft.nodes[0].nodeId,
      draggingId: '',
      touchStart: null,
      zoomScale: 1,
      chainWidth: 328,
      chainHeight: 264
    };
  },
  computed: {
    selectedIndex() {
      return this.draft.nodes.findIndex((n) => n.nodeId === this.selectedId);
    },
    selected() {
      return this.draft.nodes[this.selectedIndex];
    },
    approvers() {
      return this.participants.filter((p) => p.canApprove);
    },
    executors() {
      return this.participants.filter((p) => p.canExecute);
    },
    selectedMemberCount() {
      const team = this.teams.find((t) => t.teamId === this.selected?.teamId);
      return team?.memberUids.filter((uid) => this.approvers.some((p) => p.userUid === uid)).length || 0;
    }
  },
  watch: {
    draft: {
      deep: true,
      handler(value) {
        this.$emit('update:draft', value);
      }
    }
  },
  mounted() {
    this.chainObserver = new ResizeObserver(() => {
      this.chainWidth = this.$refs.chain.offsetWidth;
      this.chainHeight = this.$refs.chain.offsetHeight;
    });
    this.chainObserver.observe(this.$refs.chain);
  },
  beforeUnmount() {
    this.chainObserver.disconnect();
  },
  methods: {
    setZoom(nextScale, fit = false) {
      const canvas = this.$refs.canvas;
      const oldWidth = this.chainWidth * this.zoomScale;
      const oldHeight = this.chainHeight * this.zoomScale;
      const oldOffsetX = Math.max(0, (canvas.clientWidth - oldWidth) / 2);
      const oldOffsetY = Math.max(0, (canvas.clientHeight - oldHeight) / 2);
      const centerX = (canvas.scrollLeft + canvas.clientWidth / 2 - oldOffsetX) / this.zoomScale;
      const centerY = (canvas.scrollTop + canvas.clientHeight / 2 - oldOffsetY) / this.zoomScale;
      this.zoomScale = Math.min(1.5, Math.max(0.5, Math.round(nextScale * 10) / 10));
      this.$nextTick(() => {
        const width = this.chainWidth * this.zoomScale;
        const height = this.chainHeight * this.zoomScale;
        if (fit) {
          canvas.scrollLeft = Math.max(0, (width - canvas.clientWidth) / 2);
          canvas.scrollTop = Math.max(0, (height - canvas.clientHeight) / 2);
          return;
        }
        const offsetX = Math.max(0, (canvas.clientWidth - width) / 2);
        const offsetY = Math.max(0, (canvas.clientHeight - height) / 2);
        canvas.scrollLeft = Math.max(0, offsetX + centerX * this.zoomScale - canvas.clientWidth / 2);
        canvas.scrollTop = Math.max(0, offsetY + centerY * this.zoomScale - canvas.clientHeight / 2);
      });
    },
    fitCanvas() {
      const canvas = this.$refs.canvas;
      const scale = Math.min(1, canvas.clientWidth / this.chainWidth, canvas.clientHeight / this.chainHeight);
      this.setZoom(Math.floor(scale * 10) / 10, true);
    },
    teamDisabledReason(team, role) {
      if (!team.memberUids.length) return this.$t('approval-team-empty-reason');
      const members = team.memberUids.map((uid) => this.participants.find((person) => person.userUid === uid));
      const unavailableCount = members.filter((person) => !person).length;
      const missingRoleNames = members.filter((person) => person && !person[role]).map((person) => person.userName);
      const reasons = [];
      if (unavailableCount) {
        reasons.push(this.$t('approval-team-unavailable-reason', { count: unavailableCount }));
      }
      if (missingRoleNames.length) {
        const key = role === 'canApprove' ? 'approval-team-approve-reason' : 'approval-team-execute-reason';
        reasons.push(this.$t(key, { members: missingRoleNames.join(', ') }));
      }
      return reasons.join('; ');
    },
    scrollDragEdge(event) {
      const canvas = this.$refs.canvas;
      const bounds = canvas.getBoundingClientRect();
      if (event.clientX < bounds.left + 32) canvas.scrollLeft -= 16;
      if (event.clientX > bounds.right - 32) canvas.scrollLeft += 16;
    },
    dropOnCanvas(event) {
      if (!this.draggingId) return;
      const cards = Array.from(this.$refs.canvas.querySelectorAll('[data-node-id]'));
      const target = cards.find((card) => event.clientX < card.getBoundingClientRect().right);
      const targetNode = this.draft.nodes.find((node) => node.nodeId === target?.dataset.nodeId) || this.draft.nodes[this.draft.nodes.length - 1];
      this.dropNode(targetNode);
    },
    startTouchDrag(node, event) {
      if (!this.editable || event.pointerType === 'mouse' || event.target.closest('button')) return;
      this.touchStart = { x: event.clientX, y: event.clientY, id: node.nodeId };
      event.currentTarget.setPointerCapture(event.pointerId);
    },
    moveTouchDrag(event) {
      if (!this.touchStart || event.pointerType === 'mouse') return;
      if (Math.abs(event.clientX - this.touchStart.x) > 8) this.draggingId = this.touchStart.id;
      if (this.draggingId) this.scrollDragEdge(event);
    },
    cancelTouchDrag(event) {
      if (event.pointerType === 'mouse') return;
      this.draggingId = '';
      this.touchStart = null;
    },
    finishTouchDrag(event) {
      if (event.pointerType === 'mouse') return;
      if (this.draggingId) this.dropOnCanvas(event);
      this.touchStart = null;
    },
    participantName(node) {
      if (node.approverType === APPROVAL_PARTICIPANT_TYPES.TEAM) {
        const team = this.teams.find((t) => t.teamId === node.teamId);
        return team ? `${team.teamName}${this.$t('approval-member-count', { count: team.memberUids.length })}` : this.$t('approval-select-team');
      }
      return this.participants.find((p) => p.userUid === node.userUid)?.userName || this.$t('approval-select-person');
    },
    changeNodeType(type) {
      Object.assign(this.selected, {
        approverType: type,
        teamId: null,
        userUid: null,
        requiredCount: type === APPROVAL_PARTICIPANT_TYPES.TEAM ? 1 : null
      });
    },
    changeExecutionType(type) {
      this.draft.executionAssignment = { participantType: type, teamId: null, userUid: null };
    },
    changeExecutionMode(mode) {
      this.draft.executionMode = mode;
      this.draft.executionAssignment = null;
      if (mode === APPROVAL_EXECUTION_MODES.MANUAL) {
        this.draft.executionAssignment = { participantType: APPROVAL_PARTICIPANT_TYPES.TEAM, teamId: null, userUid: null };
      }
    },
    addNode(index) {
      const node = newNode();
      this.draft.nodes.splice(index + 1, 0, node);
      this.selectedId = node.nodeId;
    },
    deleteNode(index) {
      if (this.draft.nodes.length < 2) return;
      this.draft.nodes.splice(index, 1);
      this.selectedId = this.draft.nodes[Math.min(index, this.draft.nodes.length - 1)].nodeId;
    },
    startDrag(node, event) {
      this.draggingId = node.nodeId;
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', node.nodeId);
    },
    dropNode(target) {
      const from = this.draft.nodes.findIndex((n) => n.nodeId === this.draggingId);
      const to = this.draft.nodes.indexOf(target);
      if (from >= 0 && from !== to) {
        const [node] = this.draft.nodes.splice(from, 1);
        this.draft.nodes.splice(to, 0, node);
      }
      this.draggingId = '';
    },
    save() {
      const valid = this.draft.nodes.every((node) =>
        node.approverType === APPROVAL_PARTICIPANT_TYPES.TEAM
          ? node.teamId && Number.isInteger(node.requiredCount) && node.requiredCount > 0
          : node.userUid
      );
      const assignment = this.draft.executionAssignment;
      if (
        !valid ||
        (this.draft.executionMode === APPROVAL_EXECUTION_MODES.MANUAL &&
          !(assignment.participantType === APPROVAL_PARTICIPANT_TYPES.TEAM ? assignment.teamId : assignment.userUid))
      ) {
        this.$Message.error(this.$t('approval-complete-configuration'));
        return;
      }
      this.$emit('save', this.draft);
    }
  }
};
</script>
<style lang="less" scoped>
@import '@/styles/flow-orchestration.less';

.approval-execution-options {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
}

.approval-execution-options :deep(.ivu-radio-wrapper:last-child) {
  margin-right: 0;
}

.approval-editor {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
}
.approval-editor--embedded {
  flex-shrink: 0;
  height: auto;
}
.approval-editor:not(.approval-editor--embedded) .approval-editor__body {
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: 16px 24px;
}
.approval-editor .page-shell__footer {
  display: flex;
  flex: none;
  justify-content: center;
  gap: 8px;
  padding: 16px 24px;
  border-top: 1px solid var(--border-primary);
}
.approval-editor .page-section {
  min-width: 0;
}
.approval-editor .page-section__title {
  .flow-section-title();
}
.approval-editor__body {
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.approval-workspace {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 360px;
  gap: 16px;
}
.approval-canvas-panel {
  display: flex;
  flex-direction: column;
  min-width: 0;
}
.approval-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  min-height: 48px;
  padding: 4px 0 12px;
}
.approval-toolbar__actions {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  gap: 8px;
  margin-left: auto;
}
.approval-toolbar__button--delete:not(:disabled):hover,
.approval-toolbar__button--delete:not(:disabled):focus,
.approval-toolbar__button--delete:not(:disabled):active {
  border-color: var(--error-color, #ff1815);
  color: var(--error-color, #ff1815);
  background: rgba(255, 24, 21, 0.05);
  outline-color: var(--error-color, #ff1815);
  box-shadow: none;
}
.approval-toolbar .page-section__title,
.approval-inspector__header .page-section__title {
  margin: 0;
  line-height: 24px;
}
.approval-canvas-wrap {
  position: relative;
  display: flex;
  flex: 1;
  min-width: 0;
  min-height: 264px;
}
.approval-canvas {
  display: flex;
  flex: 1;
  min-width: 0;
  overflow: auto;
  overscroll-behavior: contain;
  scrollbar-gutter: stable;
  border-radius: 8px;
  background: var(--bg-secondary, #f8fafc);
}
.approval-stage-shell {
  position: relative;
  flex: 0 0 auto;
  margin: auto;
}
.approval-chain {
  position: absolute;
  left: 50%;
  top: 50%;
  display: flex;
  align-items: center;
  flex: 0 0 auto;
  min-height: 264px;
  padding: 32px;
  transform-origin: center;
  width: max-content;
}
.approval-node {
  .flow-node-card();
  display: flex;
  flex-direction: column;
  flex: 0 0 264px;
  cursor: grab;
  touch-action: pan-y;
  margin-right: 56px;
}
.approval-node:last-child {
  margin-right: 0;
}
.approval-node:not(:last-child)::after {
  content: '';
  position: absolute;
  left: calc(100% + 1px);
  top: 50%;
  width: 56px;
  height: 1.5px;
  background: #cfd7d2;
  transform: translateY(-50%);
}
.approval-node.selected {
  border-color: var(--primary-color);
}
.approval-node.dragging {
  opacity: 0.45;
}
.approval-node__header {
  .flow-node-header();
  justify-content: flex-start;
}
.approval-node__name {
  .flow-node-name();
  flex: 1;
  display: flex;
  align-items: center;
  min-height: 0;
  margin-top: 0;
}
.approval-node__footer {
  .flow-node-footer();
  margin-top: 0;
}
.approval-node__actions {
  .flow-node-actions();
}
.approval-node__actions button {
  .flow-node-action();
}
.approval-node__actions button:disabled {
  color: #c7ced6;
  cursor: not-allowed;
}
.approval-inspector {
  .flow-inspector();
}
.approval-inspector__header {
  display: flex;
  align-items: center;
  min-height: 48px;
  padding: 0 16px;
}
.approval-inspector__body {
  padding: 0 16px 16px;
}
.approval-inspector :deep(.ivu-form-item) {
  margin-bottom: 16px;
}
.approval-inspector :deep(.ivu-form-item:last-child) {
  margin-bottom: 0;
}
:global(.approval-team-dropdown) {
  max-width: min(360px, calc(100vw - 32px));
}
.approval-team-option,
.approval-team-option :deep(.ivu-tooltip-rel) {
  display: block;
}
.approval-team-option__name {
  display: block;
  white-space: normal;
  overflow-wrap: anywhere;
}
.approval-count-label {
  margin-left: 8px;
}
.approval-confirmation {
  margin-top: 24px;
}
.approval-confirmation__fields {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 16px;
}
.approval-confirmation__fields :deep(.ivu-select) {
  width: 100%;
}
.approval-confirmation :deep(.ivu-form-item) {
  margin-bottom: 0;
}
.approval-confirmation__rule {
  color: var(--text-secondary);
}
@media (max-width: 1180px) {
  .approval-workspace {
    grid-template-columns: minmax(0, 1fr) 320px;
  }
}
@media (max-width: 900px) {
  .approval-toolbar {
    align-items: stretch;
    flex-direction: column;
    gap: 8px;
  }
  .approval-toolbar__actions {
    align-self: flex-end;
  }
  .approval-workspace {
    grid-template-columns: minmax(0, 1fr);
  }
  .approval-canvas {
    width: 100%;
  }
  .approval-chain {
    padding: 24px 0;
  }
}
</style>
