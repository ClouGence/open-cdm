<template>
  <section class="batch-orchestration page-section">
    <div class="batch-workspace">
      <div class="batch-canvas-panel">
        <div class="batch-toolbar">
          <div class="batch-toolbar__summary">
            <h2 class="batch-toolbar__title">{{ $t('cicd-batch-flow-orchestration') }}</h2>
            <span class="batch-toolbar__count">{{ $t('cicd-batch-node-count', { count: nodes.length }) }}</span>
          </div>
          <div class="batch-toolbar__group">
            <Button class="batch-toolbar__button batch-toolbar__button--add" icon="md-add" @click="addChild(selectedNode)">
              {{ $t('tian-jia') }}
            </Button>
            <Button
              class="batch-toolbar__button batch-toolbar__button--delete"
              icon="ios-trash-outline"
              :disabled="!selectedNode?.parentClientId"
              @click="removeNode(selectedNode)"
            >
              {{ $t('shan-chu') }}
            </Button>
          </div>
        </div>

        <div ref="canvasWrap" class="batch-canvas-wrap">
          <div class="batch-zoom-toolbar" :aria-label="$t('cicd-batch-zoom-controls')">
            <button
              type="button"
              :title="$t('cicd-batch-zoom-out')"
              :aria-label="$t('cicd-batch-zoom-out')"
              :disabled="zoomScale <= 0.5"
              @click="zoomOut"
            >
              <Icon type="ios-remove" />
            </button>
            <span class="batch-zoom-value">{{ zoomPercent }}%</span>
            <button
              type="button"
              :title="$t('cicd-batch-zoom-in')"
              :aria-label="$t('cicd-batch-zoom-in')"
              :disabled="zoomScale >= 1.5"
              @click="zoomIn"
            >
              <Icon type="ios-add" />
            </button>
            <span class="batch-zoom-divider" aria-hidden="true"></span>
            <button type="button" :title="$t('cicd-batch-fit-canvas')" :aria-label="$t('cicd-batch-fit-canvas')" @click="fitCanvas">
              <Icon type="md-expand" />
            </button>
          </div>

          <div ref="canvasHost" class="batch-canvas" @scroll.passive="handleCanvasScroll">
            <div class="batch-stage-shell" :style="{ width: `${layout.width * zoomScale}px`, height: `${layout.height * zoomScale}px` }">
              <div
                class="batch-stage"
                :style="{
                  width: `${layout.width}px`,
                  height: `${layout.height}px`,
                  transform: `translate(-50%, -50%) scale(${zoomScale})`
                }"
              >
                <svg class="batch-edges" :width="layout.width" :height="layout.height" :viewBox="`0 0 ${layout.width} ${layout.height}`">
                  <path v-for="edge in layout.edges" :key="edge.id" :d="edge.path" />
                </svg>

                <div
                  v-for="node in nodes"
                  :key="node.clientId"
                  class="batch-node-slot"
                  :class="{ 'batch-node-slot--drop': dropTargetId === node.clientId }"
                  :data-drop-id="node.clientId"
                  :style="nodePositionStyle(node)"
                  @dragenter.prevent="enterNativeDropTarget(node)"
                  @dragover.prevent
                  @drop.prevent="finishNativeDrop(node)"
                >
                  <article
                    class="batch-node"
                    :class="{
                      'batch-node--selected': selectedId === node.clientId,
                      'batch-node--invalid': nodeErrors(node).length,
                      'batch-node--dragging': draggingId === node.clientId
                    }"
                    tabindex="0"
                    :draggable="Boolean(node.parentClientId)"
                    @click="selectNode(node.clientId)"
                    @keydown.enter.prevent="selectNode(node.clientId)"
                    @keydown.space.prevent="selectNode(node.clientId)"
                    @dragstart="startNativeDrag(node, $event)"
                    @dragend="cancelNativeDrag"
                    @pointerdown="startPointerDrag(node, $event)"
                    @pointermove="movePointerDrag($event)"
                    @pointerup="finishPointerDrag($event)"
                    @pointercancel="finishPointerDrag($event)"
                  >
                    <div class="batch-node__header">
                      <span class="batch-node__level">
                        <img src="/dm.ico" alt="" />
                        {{ levelLabel(node) }}
                      </span>
                      <span class="batch-node__header-actions">
                        <span v-if="nodeErrors(node).length" class="batch-node__issue">{{ $t('cicd-batch-node-pending') }}</span>
                      </span>
                    </div>
                    <strong class="batch-node__name" :title="node.flowName || $t('cicd-batch-unnamed-flow')">
                      {{ node.flowName || $t('cicd-batch-unnamed-flow') }}
                    </strong>
                    <div class="batch-node__meta">
                      <span>{{ $t('fu-ze-ren') }}：{{ managerName(node.flowManagerUid) || $t('wei-she-zhi') }}</span>
                    </div>
                    <div class="batch-node__footer">
                      <span :title="targetLabel(node)">{{ targetLabel(node) }}</span>
                      <span class="batch-node__actions">
                        <button type="button" :title="$t('cicd-batch-add-downstream')" @click.stop="addChild(node)"><Icon type="md-add" /></button>
                        <button type="button" :title="$t('cicd-batch-delete-node')" :disabled="!node.parentClientId" @click.stop="removeNode(node)">
                          <Icon type="ios-trash-outline" />
                        </button>
                      </span>
                    </div>
                  </article>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <aside v-if="selectedNode" class="batch-inspector page-aside">
        <div class="batch-inspector__header">
          <div>
            <strong>{{ selectedNode.flowName || $t('cicd-batch-unnamed-flow') }}</strong>
            <span>{{ levelLabel(selectedNode) }}</span>
          </div>
        </div>

        <div class="batch-inspector__body">
          <section class="batch-form-section">
            <h3>{{ $t('ji-ben-xin-xi') }}</h3>
            <Form label-position="top">
              <FormItem :label="$t('xiang-mu-ming-cheng')" class="batch-form-item--required" :error="fieldError(selectedNode, 'flowName')">
                <Input v-model="selectedNode.flowName" maxlength="50" :placeholder="$t('qing-shu-ru-bian-geng-liu-cheng-ming-cheng')" />
              </FormItem>
              <FormItem :label="$t('miao-shu')">
                <Input v-model="selectedNode.flowDesc" maxlength="200" :placeholder="$t('qing-shu-ru-miao-shu-ke-xuan')" />
              </FormItem>
              <FormItem :label="$t('fu-ze-ren')" class="batch-form-item--required" :error="fieldError(selectedNode, 'flowManagerUid')">
                <Select v-model="selectedNode.flowManagerUid" filterable transfer>
                  <Option v-for="user in devopsUsers" :key="user.userUid" :value="user.userUid" :label="user.userName">
                    {{ user.userName }}
                  </Option>
                </Select>
              </FormItem>
            </Form>
          </section>

          <section class="batch-form-section">
            <h3>{{ $t('mu-biao-fa-bu-shu-ju-ku') }}</h3>
            <Form label-position="top">
              <FormItem :label="$t('shu-ju-ku-lei-xing')" class="batch-form-item--required" :error="fieldError(selectedNode, 'databaseType')">
                <ReleaseFlowDatabaseTypeSelect
                  v-model="selectedNode.databaseType"
                  :options="availableDatabaseTypes(selectedNode)"
                  :placeholder="$t('qing-xuan-ze-shu-ju-ku-lei-xing')"
                  @change="handleDatabaseTypeChange(selectedNode)"
                />
              </FormItem>
              <FormItem :label="$t('shi-li-1')" class="batch-form-item--required" :error="fieldError(selectedNode, 'instanceId')">
                <ReleaseFlowInstanceSelect
                  v-model="selectedNode.instanceId"
                  :options="availableInstances(selectedNode)"
                  :disabled="!selectedNode.databaseType"
                  :placeholder="$t('qing-xuan-ze-shu-ju-ku-shi-li')"
                  @change="handleInstanceChange(selectedNode)"
                />
              </FormItem>
              <FormItem
                v-if="selectedNode.hasCatalog"
                :label="$t('shu-ju-ku')"
                class="batch-form-item--required"
                :error="fieldError(selectedNode, 'catalogName')"
              >
                <Select
                  v-model="selectedNode.catalogName"
                  filterable
                  transfer
                  :loading="selectedNode.catalogLoading"
                  @on-change="handleCatalogChange(selectedNode)"
                >
                  <Option v-for="catalog in selectedNode.catalogOptions" :key="catalog.objName" :value="catalog.objName">
                    {{ catalog.objName }}
                  </Option>
                </Select>
              </FormItem>
              <FormItem
                v-if="selectedNode.hasSchema"
                :label="$t('schema')"
                class="batch-form-item--required"
                :error="fieldError(selectedNode, 'schemaName')"
              >
                <Select
                  v-model="selectedNode.schemaName"
                  filterable
                  transfer
                  :disabled="selectedNode.hasCatalog && !selectedNode.catalogName"
                  :loading="selectedNode.schemaLoading"
                >
                  <Option v-for="schema in selectedNode.schemaOptions" :key="schema.objName" :value="schema.objName">
                    {{ schema.objName }}
                  </Option>
                </Select>
              </FormItem>
            </Form>
          </section>
        </div>
      </aside>
    </div>
  </section>
</template>

<script>
import ReleaseFlowDatabaseTypeSelect from './ReleaseFlowDatabaseTypeSelect.vue';
import ReleaseFlowInstanceSelect from './ReleaseFlowInstanceSelect.vue';

const NODE_WIDTH = 264;
const NODE_HEIGHT = 128;
const LEVEL_GAP = 320;
const ROW_GAP = 160;
const CANVAS_PADDING = 32;
const NODE_SLOT_PADDING = 16;

export default {
  name: 'ReleaseFlowBatchOrchestration',
  components: { ReleaseFlowDatabaseTypeSelect, ReleaseFlowInstanceSelect },
  props: {
    devopsUsers: { type: Array, required: true },
    devopsInsList: { type: Array, required: true },
    databaseTypeOptions: { type: Array, default: () => [] },
    dsSettingDef: { type: Object, default: () => ({}) },
    initialManagerUid: { type: String, default: '' }
  },
  emits: ['created'],
  data() {
    return {
      nodes: [],
      selectedId: '',
      nodeSequence: 1,
      canvasHostWidth: 720,
      canvasHostHeight: 520,
      resizeObserver: null,
      pointerDrag: null,
      draggingId: '',
      dropTargetId: '',
      suppressNextClick: false,
      zoomScale: 1
    };
  },
  computed: {
    selectedNode() {
      return this.nodes.find((node) => node.clientId === this.selectedId) || this.nodes[0] || null;
    },
    rootNode() {
      return this.nodes.find((node) => !node.parentClientId) || null;
    },
    rootDatabaseType() {
      return this.rootNode?.databaseType || this.instanceById(this.rootNode?.instanceId)?.objAttr?.dsType || '';
    },
    zoomPercent() {
      return Math.round(this.zoomScale * 100);
    },
    layout() {
      const positions = {};
      let leafIndex = 0;
      const visit = (node) => {
        const children = this.childrenOf(node.clientId);
        if (!children.length) {
          positions[node.clientId] = { y: leafIndex * ROW_GAP };
          leafIndex += 1;
        } else {
          children.forEach(visit);
          const childY = children.map((child) => positions[child.clientId].y);
          positions[node.clientId] = { y: (Math.min(...childY) + Math.max(...childY)) / 2 };
        }
        positions[node.clientId].x = this.nodeDepth(node) * LEVEL_GAP;
      };
      if (this.rootNode) {
        visit(this.rootNode);
      }
      this.nodes.forEach((node) => {
        if (!positions[node.clientId]) {
          positions[node.clientId] = { x: 0, y: leafIndex * ROW_GAP };
          leafIndex += 1;
        }
      });
      const maxDepth = Math.max(0, ...this.nodes.map((node) => this.nodeDepth(node)));
      const contentWidth = NODE_WIDTH + maxDepth * LEVEL_GAP;
      const contentHeight = NODE_HEIGHT + Math.max(0, leafIndex - 1) * ROW_GAP;
      const width = Math.max(this.canvasHostWidth, contentWidth + CANVAS_PADDING * 2);
      const height = Math.max(this.canvasHostHeight, contentHeight + CANVAS_PADDING * 2);
      const offsetX = Math.max(CANVAS_PADDING, (width - contentWidth) / 2);
      const offsetY = Math.max(CANVAS_PADDING, (height - contentHeight) / 2);
      Object.values(positions).forEach((position) => {
        position.x += offsetX;
        position.y += offsetY;
      });
      const edges = this.nodes
        .filter((node) => node.parentClientId && positions[node.parentClientId])
        .map((node) => {
          const from = positions[node.parentClientId];
          const to = positions[node.clientId];
          const x1 = from.x + NODE_WIDTH;
          const y1 = from.y + NODE_HEIGHT / 2;
          const x2 = to.x;
          const y2 = to.y + NODE_HEIGHT / 2;
          const middle = x1 + (x2 - x1) / 2;
          return {
            id: `${node.parentClientId}-${node.clientId}`,
            path: `M ${x1} ${y1} H ${middle} V ${y2} H ${x2}`
          };
        });
      return { positions, width, height, edges };
    }
  },
  watch: {
    initialManagerUid(value) {
      if (value && this.rootNode && !this.rootNode.flowManagerUid) {
        this.rootNode.flowManagerUid = value;
      }
    }
  },
  mounted() {
    const root = this.createNodeData(null);
    root.flowName = this.$t('cicd-batch-default-root-name');
    this.nodes = [root];
    this.selectedId = root.clientId;
    this.$nextTick(() => {
      this.syncCanvasSize();
      if (window.ResizeObserver) {
        this.resizeObserver = new ResizeObserver(this.syncCanvasSize);
        this.resizeObserver.observe(this.$refs.canvasWrap);
      }
    });
  },
  beforeUnmount() {
    this.resizeObserver?.disconnect();
  },
  methods: {
    createNodeData(parent) {
      return {
        clientId: `draft_${Date.now()}_${this.nodeSequence++}`,
        parentClientId: parent?.clientId || null,
        flowName: '',
        flowDesc: '',
        flowManagerUid: parent?.flowManagerUid || this.initialManagerUid || '',
        instanceId: parent?.instanceId || '',
        databaseType: parent?.databaseType || 'MySQL',
        catalogName: '',
        schemaName: '',
        hasCatalog: Boolean(parent?.hasCatalog),
        hasSchema: Boolean(parent?.hasSchema),
        catalogOptions: [...(parent?.catalogOptions || [])],
        schemaOptions: [...(parent?.schemaOptions || [])],
        catalogLoading: false,
        schemaLoading: false
      };
    },
    syncCanvasSize() {
      this.canvasHostWidth = Math.max(480, this.$refs.canvasWrap?.clientWidth || 720);
      this.canvasHostHeight = Math.max(480, this.$refs.canvasWrap?.clientHeight || 520);
    },
    setZoom(nextScale, fit = false) {
      const canvas = this.$refs.canvasHost;
      const oldWidth = this.layout.width * this.zoomScale;
      const oldHeight = this.layout.height * this.zoomScale;
      const oldOffsetX = canvas ? Math.max(0, (canvas.clientWidth - oldWidth) / 2) : 0;
      const oldOffsetY = canvas ? Math.max(0, (canvas.clientHeight - oldHeight) / 2) : 0;
      const centerX = canvas ? (canvas.scrollLeft + canvas.clientWidth / 2 - oldOffsetX) / this.zoomScale : 0;
      const centerY = canvas ? (canvas.scrollTop + canvas.clientHeight / 2 - oldOffsetY) / this.zoomScale : 0;
      this.zoomScale = Math.min(1.5, Math.max(0.5, Math.round(nextScale * 10) / 10));
      this.$nextTick(() => {
        if (!canvas) return;
        const width = this.layout.width * this.zoomScale;
        const height = this.layout.height * this.zoomScale;
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
    zoomOut() {
      this.setZoom(this.zoomScale - 0.1);
    },
    zoomIn() {
      this.setZoom(this.zoomScale + 0.1);
    },
    fitCanvas() {
      const canvas = this.$refs.canvasHost;
      if (!canvas) return;
      const scale = Math.min(1, canvas.clientWidth / this.layout.width, canvas.clientHeight / this.layout.height);
      this.setZoom(scale, true);
    },
    childrenOf(clientId) {
      return this.nodes.filter((node) => node.parentClientId === clientId);
    },
    nodeDepth(node) {
      let depth = 0;
      let cursor = node;
      const walked = new Set();
      while (cursor?.parentClientId && !walked.has(cursor.clientId)) {
        walked.add(cursor.clientId);
        depth += 1;
        cursor = this.nodes.find((item) => item.clientId === cursor.parentClientId);
      }
      return depth;
    },
    levelLabel(node) {
      const depth = this.nodeDepth(node);
      return depth === 0 ? this.$t('cicd-dependency-root-flow') : this.$t('cicd-dependency-level-flow', { level: depth });
    },
    nodePositionStyle(node) {
      const position = this.layout.positions[node.clientId] || { x: 0, y: 0 };
      return {
        left: `${position.x - NODE_SLOT_PADDING}px`,
        top: `${position.y - NODE_SLOT_PADDING}px`
      };
    },
    selectNode(clientId) {
      if (this.suppressNextClick) {
        this.suppressNextClick = false;
        return;
      }
      this.selectedId = clientId;
    },
    addChild(parent) {
      if (!parent) return;
      if (this.nodes.length >= 50) {
        this.$Message.warning(this.$t('cicd-batch-max-flow-count'));
        return;
      }
      const node = this.createNodeData(parent);
      node.flowName = this.$t('cicd-batch-default-child-name', { count: this.nodes.length });
      this.nodes.push(node);
      this.selectedId = node.clientId;
      if (node.instanceId) {
        this.handleInstanceChange(node, true);
      }
    },
    descendantsOf(clientId) {
      const result = [];
      const pending = [clientId];
      while (pending.length) {
        const current = pending.shift();
        this.childrenOf(current).forEach((child) => {
          result.push(child.clientId);
          pending.push(child.clientId);
        });
      }
      return result;
    },
    removeNode(node) {
      if (!node?.parentClientId) return;
      const removingIds = new Set([node.clientId, ...this.descendantsOf(node.clientId)]);
      this.nodes = this.nodes.filter((item) => !removingIds.has(item.clientId));
      if (removingIds.has(this.selectedId)) {
        this.selectedId = node.parentClientId;
      }
    },
    managerName(uid) {
      return this.devopsUsers.find((user) => user.userUid === uid)?.userName || '';
    },
    instanceById(instanceId) {
      return this.devopsInsList.find((instance) => String(instance.objId) === String(instanceId)) || null;
    },
    availableInstances(node) {
      if (!node.databaseType) return [];
      return this.devopsInsList.filter((instance) => instance.objAttr?.dsType === node.databaseType);
    },
    availableDatabaseTypes(node) {
      if (node.parentClientId && this.rootDatabaseType) return [this.rootDatabaseType];
      return this.databaseTypeOptions;
    },
    handleDatabaseTypeChange(node) {
      Object.assign(node, {
        instanceId: '',
        catalogName: '',
        schemaName: '',
        hasCatalog: false,
        hasSchema: false,
        catalogOptions: [],
        schemaOptions: []
      });
      if (node.clientId === this.rootNode?.clientId) {
        this.nodes.forEach((child) => {
          if (child.clientId === node.clientId) return;
          Object.assign(child, {
            databaseType: node.databaseType,
            instanceId: '',
            catalogName: '',
            schemaName: '',
            hasCatalog: false,
            hasSchema: false,
            catalogOptions: [],
            schemaOptions: []
          });
        });
      }
    },
    targetLabel(node) {
      const instance = this.instanceById(node.instanceId);
      if (!instance) return this.$t('cicd-batch-target-unset');
      const path = node.schemaName || node.catalogName;
      return [instance.objAttr?.dsType, path].filter(Boolean).join(' · ');
    },
    async handleInstanceChange(node, preservePath = false) {
      const instance = this.instanceById(node.instanceId);
      if (!instance) {
        Object.assign(node, {
          catalogName: '',
          schemaName: '',
          hasCatalog: false,
          hasSchema: false,
          catalogOptions: [],
          schemaOptions: []
        });
        return;
      }
      const previousType = node.databaseType;
      node.databaseType = instance.objAttr?.dsType || '';
      if (!preservePath) {
        node.catalogName = '';
        node.schemaName = '';
      }
      node.catalogOptions = [];
      node.schemaOptions = [];
      const levels = this.dsSettingDef?.[node.databaseType]?.categories?.levels || [];
      node.hasCatalog = levels.includes('CATALOG');
      node.hasSchema = levels.includes('SCHEMA');
      if (node.clientId === this.rootNode?.clientId && previousType !== node.databaseType) {
        this.nodes.forEach((child) => {
          if (child.clientId !== node.clientId && child.databaseType && child.databaseType !== node.databaseType) {
            Object.assign(child, {
              instanceId: '',
              databaseType: '',
              catalogName: '',
              schemaName: '',
              hasCatalog: false,
              hasSchema: false,
              catalogOptions: [],
              schemaOptions: []
            });
          }
        });
      }
      if (node.hasCatalog) {
        await this.fetchCatalogs(node);
      } else if (node.hasSchema) {
        await this.fetchSchemas(node);
      }
    },
    async handleCatalogChange(node) {
      node.schemaName = '';
      node.schemaOptions = [];
      if (node.hasSchema && node.catalogName) await this.fetchSchemas(node);
    },
    async fetchCatalogs(node) {
      const instance = this.instanceById(node.instanceId);
      if (!instance?.objAttr?.dsEnvId) return;
      node.catalogLoading = true;
      try {
        const res = await this.$services.dmCicdDevopsDsDbLevels({
          data: { levels: [instance.objAttr.dsEnvId, node.instanceId], refreshCache: false }
        });
        node.catalogOptions = res.success ? res.data || [] : [];
      } finally {
        node.catalogLoading = false;
      }
    },
    async fetchSchemas(node) {
      const instance = this.instanceById(node.instanceId);
      if (!instance?.objAttr?.dsEnvId) return;
      const levels = [instance.objAttr.dsEnvId, node.instanceId];
      if (node.hasCatalog) levels.push(node.catalogName);
      node.schemaLoading = true;
      try {
        const res = await this.$services.dmCicdDevopsDsDbLevels({
          data: {
            levels,
            refreshCache: false
          }
        });
        node.schemaOptions = res.success ? res.data || [] : [];
      } finally {
        node.schemaLoading = false;
      }
    },
    nodeErrors(node) {
      const errors = [];
      if (!String(node.flowName || '').trim())
        errors.push({
          field: 'flowName',
          message: this.$t('qing-shu-ru-bian-geng-liu-cheng-ming-cheng')
        });
      if (!node.flowManagerUid)
        errors.push({
          field: 'flowManagerUid',
          message: this.$t('qing-xuan-ze-fu-ze-ren')
        });
      if (!node.databaseType) {
        errors.push({ field: 'databaseType', message: this.$t('qing-xuan-ze-shu-ju-ku-lei-xing') });
      } else if (!node.instanceId) {
        errors.push({ field: 'instanceId', message: this.$t('qing-xuan-ze-shu-ju-ku-shi-li') });
      }
      if (node.instanceId && this.rootDatabaseType && node.databaseType !== this.rootDatabaseType) {
        errors.push({ field: 'instanceId', message: this.$t('cicd-batch-target-type-mismatch') });
      }
      if (node.hasCatalog && !node.catalogName)
        errors.push({
          field: 'catalogName',
          message: this.$t('qing-xuan-ze-shu-ju-ku')
        });
      if (node.hasSchema && !node.schemaName)
        errors.push({
          field: 'schemaName',
          message: this.$t('qing-xuan-ze-schema')
        });
      return errors;
    },
    fieldError(node, field) {
      return this.nodeErrors(node).find((error) => error.field === field)?.message || '';
    },
    nodeDsLevels(node) {
      const instance = this.instanceById(node.instanceId);
      const levels = [instance?.objAttr?.dsEnvId, node.instanceId];
      if (node.hasCatalog) levels.push(node.catalogName);
      if (node.hasSchema) levels.push(node.schemaName);
      return levels.filter((value) => value !== undefined && value !== null && value !== '');
    },
    validate() {
      const invalid = this.nodes.find((node) => this.nodeErrors(node).length);
      if (!invalid) return true;
      this.selectedId = invalid.clientId;
      this.$Message.error(this.$t('cicd-batch-complete-all-config'));
      return false;
    },
    async submit(config = {}) {
      if (!this.validate()) return null;
      const confirmed = await new Promise((resolve) => {
        this.$Modal.confirm({
          title: this.$t('cicd-batch-confirm-title'),
          content: this.$t('cicd-batch-confirm-description', {
            count: this.nodes.length,
            relations: Math.max(0, this.nodes.length - 1)
          }),
          onOk: () => resolve(true),
          onCancel: () => resolve(false)
        });
      });
      if (!confirmed) return null;
      const res = await this.$services.dmCicdBatchCreate({
        data: {
          flows: this.nodes.map((node) => ({
            clientId: node.clientId,
            parentClientId: node.parentClientId,
            flow: {
              flowName: node.flowName.trim(),
              flowDesc: node.flowDesc,
              flowManagerUid: node.flowManagerUid,
              flowType: 'BUILT_IN',
              parentFlowId: null,
              option: config.option || {
                initScript: 'None'
              },
              pipeline: { repoScmId: 0, dsLevels: this.nodeDsLevels(node) },
              messenger: config.messenger || null
            }
          }))
        }
      });
      if (!res.success) {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
        return null;
      }
      this.$emit('created', res.data);
      return res.data;
    },
    startPointerDrag(node, event) {
      if (event.pointerType === 'mouse' || !node.parentClientId || event.button !== 0 || event.target.closest('button, input, .ivu-checkbox')) {
        return;
      }
      this.pointerDrag = {
        clientId: node.clientId,
        pointerId: event.pointerId,
        startX: event.clientX,
        startY: event.clientY,
        element: event.currentTarget,
        moved: false
      };
      event.currentTarget.setPointerCapture?.(event.pointerId);
    },
    movePointerDrag(event) {
      if (!this.pointerDrag || this.pointerDrag.pointerId !== event.pointerId) return;
      const distance = Math.hypot(event.clientX - this.pointerDrag.startX, event.clientY - this.pointerDrag.startY);
      if (!this.pointerDrag.moved && distance > 6) {
        this.pointerDrag.moved = true;
        this.draggingId = this.pointerDrag.clientId;
      }
      if (!this.pointerDrag.moved) return;
      event.preventDefault();
      const slot = document.elementFromPoint(event.clientX, event.clientY)?.closest('.batch-node-slot');
      const targetId = slot?.dataset.dropId || '';
      const invalid = !targetId || targetId === this.draggingId || this.descendantsOf(this.draggingId).includes(targetId);
      this.dropTargetId = invalid ? '' : targetId;
    },
    startNativeDrag(node, event) {
      if (!node.parentClientId) {
        event.preventDefault();
        return;
      }
      this.draggingId = node.clientId;
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', node.clientId);
    },
    enterNativeDropTarget(node) {
      if (!this.draggingId) return;
      const invalid = node.clientId === this.draggingId || this.descendantsOf(this.draggingId).includes(node.clientId);
      this.dropTargetId = invalid ? '' : node.clientId;
    },
    finishNativeDrop(node) {
      const moving = this.nodes.find((item) => item.clientId === this.draggingId);
      if (moving && this.dropTargetId === node.clientId) {
        this.updateParentRelation(moving, node.clientId);
      }
      this.cancelNativeDrag();
    },
    cancelNativeDrag() {
      this.draggingId = '';
      this.dropTargetId = '';
    },
    updateParentRelation(moving, parentClientId) {
      moving.parentClientId = parentClientId;
      this.selectedId = moving.clientId;
      this.suppressNextClick = true;
    },
    finishPointerDrag(event) {
      if (!this.pointerDrag || this.pointerDrag.pointerId !== event.pointerId) return;
      const moved = this.pointerDrag.moved;
      const moving = this.nodes.find((node) => node.clientId === this.pointerDrag.clientId);
      this.pointerDrag.element.releasePointerCapture?.(event.pointerId);
      if (moved && moving && this.dropTargetId) {
        this.updateParentRelation(moving, this.dropTargetId);
      }
      this.pointerDrag = null;
      this.draggingId = '';
      this.dropTargetId = '';
    },
    handleCanvasScroll() {
      if (this.pointerDrag?.moved) this.dropTargetId = '';
    }
  }
};
</script>

<style scoped>
.batch-orchestration {
  display: flex;
  flex: 1 1 auto;
  min-height: 0;
  flex-direction: column;
  overflow: hidden;
  padding: 20px 24px 8px;
}

.batch-toolbar,
.batch-node__header,
.batch-node__footer,
.batch-inspector__header {
  display: flex;
  align-items: center;
}

.batch-toolbar {
  min-height: 56px;
  justify-content: space-between;
  gap: 16px;
  padding: 4px 0 12px;
}

.batch-toolbar__summary {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 10px;
}

.batch-toolbar__title {
  position: relative;
  margin: 0;
  padding-left: 12px;
  color: var(--text-primary, #181d26);
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
}

.batch-toolbar__title::before {
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

.batch-toolbar__count {
  padding: 2px 10px;
  border-radius: 9999px;
  color: var(--text-secondary, #707070);
  background: var(--bg-secondary, #fafafa);
  font-size: 12px;
  line-height: 20px;
  white-space: nowrap;
}

.batch-toolbar__group {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: auto;
}

.batch-toolbar__button {
  height: 36px;
  padding: 0 14px;
  border-radius: 6px;
  box-shadow: none;
  font-size: 14px;
  font-weight: 500;
}

.batch-toolbar__button--add {
  border-color: var(--primary-color, #3ecf8e);
  color: #24b47e;
  background: #fff;
}

.batch-toolbar__button--add:hover {
  border-color: #24b47e;
  color: #188f63;
  background: rgba(62, 207, 142, 0.08);
}

.batch-toolbar__button--delete {
  border-color: var(--border-primary, #dfdfdf);
  color: var(--text-secondary, #707070);
  background: #fff;
}

.batch-toolbar__button--delete:not(:disabled):hover {
  border-color: var(--error-color, #ff1815);
  color: var(--error-color, #ff1815);
  background: rgba(255, 24, 21, 0.05);
}

.batch-toolbar__button--delete:disabled {
  border-color: var(--border-primary, #dfdfdf);
  color: var(--text-disabled, #b2b2b2);
  background: var(--bg-secondary, #fafafa);
}

.batch-workspace {
  display: grid;
  flex: 1 1 auto;
  grid-template-columns: minmax(0, 1fr) 360px;
  align-items: stretch;
  gap: 16px;
  height: 100%;
  min-height: 0;
}

.batch-canvas-panel {
  display: flex;
  min-width: 0;
  min-height: 0;
  flex-direction: column;
  overflow: hidden;
  background: transparent;
}

.batch-canvas-wrap {
  position: relative;
  display: flex;
  flex: 1 1 auto;
  min-width: 0;
  min-height: 0;
}

.batch-canvas {
  position: relative;
  display: flex;
  height: auto;
  flex: 1 1 auto;
  min-width: 0;
  min-height: 0;
  overflow: auto;
  overscroll-behavior: contain;
  scrollbar-gutter: stable;
  border-radius: 8px;
  background: var(--bg-secondary, #f8fafc);
}

.batch-zoom-toolbar {
  position: absolute;
  z-index: 4;
  top: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  gap: 0;
  padding: 2px;
  border: 1px solid var(--border-primary, #dfe4e1);
  border-radius: 8px;
  background: #fff;
}

.batch-zoom-toolbar button {
  display: inline-flex;
  width: 30px;
  height: 30px;
  align-items: center;
  justify-content: center;
  padding: 0;
  border: 0;
  border-radius: 5px;
  color: #657184;
  background: transparent;
  font-size: 18px;
  cursor: pointer;
}

.batch-zoom-toolbar button:hover:not(:disabled) {
  color: #188f63;
  background: rgba(62, 207, 142, 0.1);
}

.batch-zoom-toolbar button:disabled {
  color: #c7ced6;
  cursor: not-allowed;
}

.batch-zoom-toolbar button:focus-visible {
  outline: 2px solid rgba(36, 180, 126, 0.3);
  outline-offset: -2px;
}

.batch-zoom-value {
  width: 44px;
  color: #707b89;
  font-size: 12px;
  font-variant-numeric: tabular-nums;
  line-height: 30px;
  text-align: center;
  user-select: none;
}

.batch-zoom-divider {
  width: 1px;
  height: 18px;
  margin: 0 2px;
  background: var(--border-primary, #e5e9e7);
}

.batch-stage-shell {
  position: relative;
  flex: 0 0 auto;
  margin: auto;
}

.batch-stage {
  position: absolute;
  top: 50%;
  left: 50%;
  transform-origin: center;
  transition: transform 160ms ease;
}

.batch-edges {
  position: absolute;
  inset: 0;
  overflow: visible;
  pointer-events: none;
}

.batch-edges path {
  fill: none;
  stroke: #cfd7d2;
  stroke-width: 1.5;
}

.batch-node-slot {
  position: absolute;
  width: 296px;
  height: 160px;
  padding: 16px;
  border: 1px solid transparent;
  border-radius: 8px;
  transition:
    border-color 140ms ease,
    background-color 140ms ease;
}

.batch-node-slot--drop {
  border-color: #24b47e;
  background: rgba(36, 180, 126, 0.07);
}

.batch-node {
  position: relative;
  width: 264px;
  height: 128px;
  padding: 12px 14px;
  border: 1px solid #dfe4e1;
  border-radius: 8px;
  outline: 0;
  background: #fff;
  cursor: pointer;
  user-select: none;
  transition:
    border-color 140ms ease,
    box-shadow 140ms ease,
    opacity 140ms ease;
}

.batch-node:hover {
  border-color: #b7c1bb;
}

.batch-node--selected {
  border-color: #24b47e;
}

.batch-node--invalid:not(.batch-node--selected) {
  border-color: #dfe4e1;
}

.batch-node--dragging {
  opacity: 0.45;
}

.batch-node__header {
  justify-content: space-between;
  gap: 8px;
  color: #707b89;
  font-size: 12px;
}

.batch-node__level {
  display: inline-flex;
  align-items: center;
  gap: 7px;
}

.batch-node__level img {
  width: 17px;
  height: 17px;
}

.batch-node__header-actions {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.batch-node__issue {
  color: #ad6517;
  font-size: 11px;
  white-space: nowrap;
}

.batch-node__name {
  display: block;
  overflow: hidden;
  margin-top: 8px;
  color: #181d26;
  font-size: 15px;
  line-height: 22px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.batch-node__meta {
  overflow: hidden;
  margin-top: 3px;
  color: #8993a2;
  font-size: 12px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.batch-node__footer {
  justify-content: space-between;
  gap: 8px;
  margin-top: 7px;
  color: #657184;
  font-size: 12px;
}

.batch-node__footer > span:first-child {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.batch-node__actions {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  gap: 2px;
}

.batch-node__actions button {
  width: 24px;
  height: 24px;
  padding: 0;
  border: 0;
  color: #657184;
  background: transparent;
  cursor: pointer;
}

.batch-node__actions button:hover {
  color: #181d26;
  background: #f1f3f2;
}

.batch-node__actions button:disabled {
  color: #c7ced6;
  background: transparent;
  cursor: not-allowed;
}

.batch-inspector {
  display: flex;
  align-self: stretch;
  height: 100%;
  min-width: 0;
  min-height: 0;
  flex-direction: column;
  overflow: hidden;
  background: #f7f9f8;
  border-radius: 8px;
}

.batch-inspector__header {
  min-height: 72px;
  justify-content: space-between;
  gap: 16px;
  padding: 0 18px;
}

.batch-inspector__header strong,
.batch-inspector__header span {
  display: block;
}

.batch-inspector__header strong {
  overflow: hidden;
  max-width: 220px;
  color: #181d26;
  font-size: 15px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.batch-inspector__header span {
  margin-top: 3px;
  color: #8993a2;
  font-size: 12px;
}

.batch-inspector__body {
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  overscroll-behavior: contain;
  scrollbar-gutter: stable;
  padding: 0 18px 18px;
}

.batch-form-section {
  padding: 18px 0;
  border-bottom: 1px solid #edf0ee;
}

.batch-form-section:last-child {
  border-bottom: 0;
}

.batch-form-section h3 {
  position: relative;
  margin: 0 0 20px;
  padding-left: 12px;
  color: #181d26;
  font-size: 16px;
  font-weight: 500;
  line-height: 1.4;
}

.batch-form-section h3::before {
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

.batch-form-section :deep(.ivu-form-item) {
  margin-bottom: 24px;
}

.batch-form-section :deep(.ivu-form-item:last-child) {
  margin-bottom: 0;
}

.batch-form-item--required :deep(.ivu-form-item-label)::before {
  display: inline-block;
  margin-right: 4px;
  color: #ed4014;
  content: '*';
}

.batch-form-section :deep(.ivu-input),
.batch-form-section :deep(.ivu-select-selection) {
  min-height: 40px;
  background: #fff;
}

.batch-form-section :deep(.ivu-select-single .ivu-select-selection .ivu-select-placeholder),
.batch-form-section :deep(.ivu-select-single .ivu-select-selection .ivu-select-selected-value),
.batch-form-section :deep(.ivu-select-single .ivu-select-selection .ivu-select-input) {
  height: 38px;
  line-height: 38px;
}

@media (max-width: 1180px) {
  .batch-workspace {
    grid-template-columns: minmax(0, 1fr) 320px;
  }
}

@media (max-width: 900px) {
  .batch-orchestration {
    padding: 16px;
  }

  .batch-toolbar {
    align-items: stretch;
    flex-direction: column;
    padding: 4px 0 12px;
  }

  .batch-toolbar__group {
    align-self: flex-end;
    margin-left: 0;
  }

  .batch-workspace {
    display: flex;
    flex: 0 0 auto;
    flex-direction: column;
  }

  .batch-canvas-panel {
    position: relative;
    top: auto;
    width: 100%;
    flex: 0 0 auto;
  }

  .batch-canvas {
    height: 500px;
    flex: 0 0 500px;
    min-height: 500px;
  }

  .batch-inspector {
    width: 100%;
    flex: 0 0 auto;
  }
}
</style>
