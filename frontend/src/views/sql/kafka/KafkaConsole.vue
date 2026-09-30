<template>
  <div class="kafka-console">
    <div class="kafka-console__toolbar">
      <div class="kafka-console__toolbar-left">
        <Button size="small" :loading="refreshing" @click="handleRefresh">
          {{ $t('kafka-console-refresh') }}
        </Button>
        <template v-if="canModify && isTopic">
          <Button size="small" type="primary" @click="openCreateTopic">
            {{ $t('kafka-console-create-topic') }}
          </Button>
          <Button v-if="selectedNode" size="small" @click="openAlterLayout">
            {{ $t('kafka-console-alter-layout') }}
          </Button>
          <Button v-if="selectedNode" size="small" @click="openAlterConfigs">
            {{ $t('kafka-console-alter-configs') }}
          </Button>
          <Button v-if="selectedNode" size="small" type="error" ghost @click="confirmDeleteTopic">
            {{ $t('kafka-console-delete-topic') }}
          </Button>
        </template>
        <template v-if="canModify && isConsumerGroup && selectedNode">
          <Button size="small" @click="openResetOffsets">
            {{ $t('kafka-console-reset-offsets') }}
          </Button>
          <Button size="small" type="error" ghost @click="confirmDeleteGroup">
            {{ $t('kafka-console-delete-group') }}
          </Button>
        </template>
      </div>
      <div class="kafka-console__toolbar-right">
        <slot name="connection-context" />
      </div>
    </div>

    <div class="kafka-console__body">
      <div v-if="!selectedNode" class="kafka-console__empty">
        <div class="kafka-console__empty-title">{{ dimensionTitle }}</div>
        <div class="kafka-console__empty-desc">{{ $t('kafka-console-select-object') }}</div>
        <div class="kafka-console__summary">
          <div class="kafka-console__summary-item">
            <div class="kafka-console__summary-label">{{ $t('kafka-console-object-count') }}</div>
            <div class="kafka-console__summary-value">{{ objectCount }}</div>
          </div>
        </div>
      </div>

      <template v-else-if="isEndpoint">
        <div class="kafka-console__section-title">{{ selectedNode.title }}</div>
        <table class="kafka-console__kv">
          <tbody>
            <tr v-for="row in brokerRows" :key="row.label">
              <th>{{ row.label }}</th>
              <td>{{ row.value }}</td>
            </tr>
          </tbody>
        </table>
      </template>

      <template v-else-if="isTopic">
        <div class="kafka-console__section-title">{{ selectedNode.title }}</div>
        <table class="kafka-console__kv">
          <tbody>
            <tr v-for="row in topicRows" :key="row.label">
              <th>{{ row.label }}</th>
              <td>{{ row.value }}</td>
            </tr>
          </tbody>
        </table>
        <div class="kafka-console__section-title kafka-console__section-title--sub">
          {{ $t('kafka-console-partitions') }}
        </div>
        <Table :columns="partitionColumns" :data="partitionRows" size="small" border stripe />
      </template>

      <template v-else-if="isConsumerGroup">
        <div class="kafka-console__section-title">{{ selectedNode.title }}</div>
        <table class="kafka-console__kv">
          <tbody>
            <tr v-for="row in consumerGroupRows" :key="row.label">
              <th>{{ row.label }}</th>
              <td>{{ row.value }}</td>
            </tr>
          </tbody>
        </table>
        <div class="kafka-console__section-title kafka-console__section-title--sub">
          {{ $t('kafka-console-consumed-topics') }}
        </div>
        <Table :columns="consumedTopicColumns" :data="consumedTopicRows" size="small" border stripe />
      </template>
    </div>

    <Modal v-model="createTopicVisible" :title="$t('kafka-console-create-topic')" :loading="submitting" @on-ok="submitCreateTopic">
      <Form :label-width="120">
        <FormItem :label="$t('kafka-console-topic')">
          <Input v-model="createTopicForm.topicName" />
        </FormItem>
        <FormItem :label="$t('kafka-console-partitions')">
          <InputNumber v-model="createTopicForm.partitions" :min="1" />
        </FormItem>
        <FormItem :label="$t('kafka-console-replicas')">
          <InputNumber v-model="createTopicForm.replicationFactor" :min="1" />
        </FormItem>
      </Form>
    </Modal>

    <Modal v-model="alterLayoutVisible" :title="$t('kafka-console-alter-layout')" :loading="submitting" @on-ok="submitAlterLayout">
      <Alert type="info" show-icon style="margin-bottom: 12px">
        {{ $t('kafka-console-alter-layout-hint') }}
      </Alert>
      <Form :label-width="120">
        <FormItem :label="$t('kafka-console-partitions')">
          <InputNumber v-model="alterLayoutForm.partitions" :min="alterLayoutForm.minPartitions" style="width: 100%" />
        </FormItem>
        <FormItem :label="$t('kafka-console-replicas')">
          <InputNumber v-model="alterLayoutForm.replicationFactor" :min="1" style="width: 100%" />
        </FormItem>
      </Form>
    </Modal>

    <Modal v-model="alterConfigsVisible" :title="$t('kafka-console-alter-configs')" :loading="submitting" width="640" @on-ok="submitAlterConfigs">
      <div class="kafka-console__config-hint">{{ $t('kafka-console-alter-configs-hint') }}</div>
      <Input v-model="configsText" type="textarea" :rows="6" />
    </Modal>

    <Modal v-model="resetOffsetsVisible" :title="$t('kafka-console-reset-offsets')" :loading="submitting" width="560" @on-ok="submitResetOffsets">
      <Alert type="warning" show-icon style="margin-bottom: 12px">
        {{ $t('kafka-console-reset-offsets-hint') }}
      </Alert>
      <Form :label-width="120">
        <FormItem :label="$t('kafka-console-reset-mode')">
          <Select v-model="resetForm.mode">
            <Option value="EARLIEST">{{ $t('kafka-console-mode-earliest') }}</Option>
            <Option value="LATEST">{{ $t('kafka-console-mode-latest') }}</Option>
            <Option value="TIMESTAMP">{{ $t('kafka-console-mode-timestamp') }}</Option>
            <Option value="OFFSET">{{ $t('kafka-console-mode-offset') }}</Option>
          </Select>
        </FormItem>
        <FormItem v-if="resetForm.mode === 'TIMESTAMP'" :label="$t('kafka-console-log-time')">
          <DatePicker v-model="resetForm.timestamp" type="datetime" style="width: 100%" :placeholder="$t('kafka-console-log-time-placeholder')" />
        </FormItem>
        <FormItem v-if="resetForm.mode === 'OFFSET'" :label="$t('kafka-console-absolute-offset')">
          <InputNumber v-model="resetForm.offset" :min="0" style="width: 100%" />
          <div class="kafka-console__config-hint">{{ $t('kafka-console-absolute-offset-hint') }}</div>
        </FormItem>
        <FormItem :label="$t('kafka-console-topics-optional')">
          <Select
            v-model="resetForm.topics"
            multiple
            filterable
            allow-create
            style="width: 100%"
            :placeholder="$t('kafka-console-topics-placeholder')"
          >
            <Option v-for="topic in resetTopicOptions" :key="topic" :value="topic">{{ topic }}</Option>
          </Select>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import { KAFKA_ATTR, KAFKA_JSON, KAFKA_LEAF, attrOf, parseJsonArray } from './attrKeys';

export default {
  name: 'KafkaConsole',
  props: {
    tab: {
      type: Object,
      required: true
    },
    listLeaf: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      refreshing: false,
      submitting: false,
      createTopicVisible: false,
      alterLayoutVisible: false,
      alterConfigsVisible: false,
      resetOffsetsVisible: false,
      createTopicForm: {
        topicName: '',
        partitions: 1,
        replicationFactor: 1
      },
      alterLayoutForm: {
        partitions: 1,
        minPartitions: 1,
        replicationFactor: 1
      },
      configsText: '',
      resetForm: {
        mode: 'EARLIEST',
        timestamp: new Date(),
        offset: 0,
        topics: []
      }
    };
  },
  computed: {
    canModify() {
      return !this.tab?.readOnly && !this.tab?.consoleQueryOnly;
    },
    leafType() {
      return this.tab?.leafType || '';
    },
    treeData() {
      const leafType = this.leafType;
      if (!leafType || !this.tab?.[leafType]) {
        return [];
      }
      return this.tab[leafType].treeData || [];
    },
    objectCount() {
      return this.treeData.length;
    },
    selectedTitle() {
      return this.tab?.selectedTable?.title || '';
    },
    selectedNode() {
      if (!this.selectedTitle) {
        return null;
      }
      return this.treeData.find((item) => item.title === this.selectedTitle || item.objName === this.selectedTitle) || null;
    },
    isEndpoint() {
      return this.leafType === KAFKA_LEAF.ENDPOINT;
    },
    isTopic() {
      return this.leafType === KAFKA_LEAF.TOPIC;
    },
    isConsumerGroup() {
      return this.leafType === KAFKA_LEAF.CONSUMER_GROUP;
    },
    dimensionTitle() {
      const leaf = (this.tab.leafGroup || []).find((item) => item.type === this.leafType);
      if (leaf?.i18n) {
        return leaf.i18n;
      }
      return this.leafType;
    },
    brokerRows() {
      const node = this.selectedNode;
      return [
        { label: this.$t('kafka-console-broker-id'), value: attrOf(node, KAFKA_ATTR.BROKER_ID) },
        { label: this.$t('kafka-console-host'), value: attrOf(node, KAFKA_ATTR.HOST) },
        { label: this.$t('kafka-console-port'), value: attrOf(node, KAFKA_ATTR.PORT) },
        {
          label: this.$t('kafka-console-controller'),
          value: attrOf(node, KAFKA_ATTR.IS_CONTROLLER) === 'true' ? this.$t('shi') : this.$t('fou')
        },
        { label: this.$t('kafka-console-cluster-id'), value: attrOf(node, KAFKA_ATTR.CLUSTER_ID) }
      ];
    },
    topicRows() {
      const node = this.selectedNode;
      return [
        { label: this.$t('kafka-console-partitions'), value: attrOf(node, KAFKA_ATTR.PARTITION_COUNT) },
        { label: this.$t('kafka-console-replicas'), value: attrOf(node, KAFKA_ATTR.REPLICA_COUNT) },
        { label: this.$t('kafka-console-under-replicated'), value: attrOf(node, KAFKA_ATTR.UNDER_REPLICATED) }
      ];
    },
    partitionColumns() {
      return [
        { title: this.$t('kafka-console-partition-id'), key: KAFKA_JSON.PARTITION_ID, minWidth: 90 },
        { title: this.$t('kafka-console-leader'), key: KAFKA_JSON.PARTITION_LEADER, minWidth: 90 },
        { title: this.$t('kafka-console-replicas'), key: KAFKA_JSON.PARTITION_REPLICAS, minWidth: 120 },
        { title: this.$t('kafka-console-isr'), key: KAFKA_JSON.PARTITION_ISR, minWidth: 120 }
      ];
    },
    partitionRows() {
      return parseJsonArray(attrOf(this.selectedNode, KAFKA_ATTR.PARTITIONS_JSON));
    },
    consumerGroupRows() {
      const node = this.selectedNode;
      return [
        { label: this.$t('kafka-console-state'), value: attrOf(node, KAFKA_ATTR.STATE) },
        { label: this.$t('kafka-console-members'), value: attrOf(node, KAFKA_ATTR.MEMBER_COUNT) }
      ];
    },
    consumedTopicColumns() {
      return [
        { title: this.$t('kafka-console-topic'), key: KAFKA_JSON.TOPIC_NAME, minWidth: 180 },
        { title: this.$t('kafka-console-committed-offset'), key: KAFKA_JSON.TOPIC_COMMITTED, minWidth: 140 },
        { title: this.$t('kafka-console-latest-offset'), key: KAFKA_JSON.TOPIC_LATEST, minWidth: 140 },
        { title: this.$t('kafka-console-lag'), key: KAFKA_JSON.TOPIC_LAG, minWidth: 100 }
      ];
    },
    consumedTopicRows() {
      return parseJsonArray(attrOf(this.selectedNode, KAFKA_ATTR.TOPICS_JSON));
    },
    resetTopicOptions() {
      const fromGroup = this.consumedTopicRows.map((row) => row[KAFKA_JSON.TOPIC_NAME]).filter(Boolean);
      const selected = this.resetForm.topics || [];
      return Array.from(new Set([...fromGroup, ...selected]));
    }
  },
  methods: {
    async handleRefresh() {
      this.refreshing = true;
      try {
        await this.listLeaf(true);
      } finally {
        this.refreshing = false;
      }
    },
    openCreateTopic() {
      this.createTopicForm = {
        topicName: '',
        partitions: 1,
        replicationFactor: 1
      };
      this.createTopicVisible = true;
    },
    openAlterLayout() {
      const partitions = Number(attrOf(this.selectedNode, KAFKA_ATTR.PARTITION_COUNT)) || 1;
      const replicationFactor = Number(attrOf(this.selectedNode, KAFKA_ATTR.REPLICA_COUNT)) || 1;
      this.alterLayoutForm = {
        partitions,
        minPartitions: partitions,
        replicationFactor
      };
      this.alterLayoutVisible = true;
    },
    async submitAlterLayout() {
      const topicName = this.selectedNode?.title || this.selectedNode?.objName;
      if (this.alterLayoutForm.partitions < this.alterLayoutForm.minPartitions) {
        this.$Message.warning(this.$t('kafka-console-alter-layout-partitions-min'));
        return false;
      }
      this.submitting = true;
      try {
        const res = await this.$services.dmKafkaAlterTopicLayout({
          data: {
            dsId: this.tab.dsId,
            topicName,
            partitions: this.alterLayoutForm.partitions,
            replicationFactor: this.alterLayoutForm.replicationFactor
          }
        });
        if (!res.success) {
          return false;
        }
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.alterLayoutVisible = false;
        await this.listLeaf(true);
      } finally {
        this.submitting = false;
      }
    },
    async submitCreateTopic() {
      if (!this.createTopicForm.topicName) {
        this.$Message.warning(this.$t('kafka-console-topic-required'));
        return false;
      }
      this.submitting = true;
      try {
        const res = await this.$services.dmKafkaCreateTopic({
          data: {
            dsId: this.tab.dsId,
            topicName: this.createTopicForm.topicName,
            partitions: this.createTopicForm.partitions,
            replicationFactor: this.createTopicForm.replicationFactor
          }
        });
        if (!res.success) {
          return false;
        }
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.createTopicVisible = false;
        await this.listLeaf(true);
      } finally {
        this.submitting = false;
      }
    },
    confirmDeleteTopic() {
      const topicName = this.selectedNode?.title || this.selectedNode?.objName;
      this.$Modal.confirm({
        title: this.$t('kafka-console-delete-topic'),
        content: this.$t('kafka-console-delete-topic-confirm', { name: topicName }),
        onOk: async () => {
          const res = await this.$services.dmKafkaDeleteTopic({
            data: {
              dsId: this.tab.dsId,
              topicName
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            if (this.tab.selectedTable) {
              this.tab.selectedTable = null;
            }
            await this.listLeaf(true);
          }
        }
      });
    },
    async openAlterConfigs() {
      const topicName = this.selectedNode?.title || this.selectedNode?.objName;
      const res = await this.$services.dmKafkaDescribeTopicConfigs({
        data: {
          dsId: this.tab.dsId,
          topicName
        }
      });
      if (!res.success) {
        return;
      }
      const configs = res.data || {};
      this.configsText = Object.keys(configs)
        .map((key) => `${key}=${configs[key]}`)
        .join(',');
      this.alterConfigsVisible = true;
    },
    async submitAlterConfigs() {
      const topicName = this.selectedNode?.title || this.selectedNode?.objName;
      const configs = {};
      (this.configsText || '')
        .split(',')
        .map((item) => item.trim())
        .filter(Boolean)
        .forEach((item) => {
          const idx = item.indexOf('=');
          if (idx <= 0) {
            return;
          }
          const key = item.slice(0, idx).trim();
          const value = item.slice(idx + 1).trim();
          if (key) {
            configs[key] = value;
          }
        });
      this.submitting = true;
      try {
        const res = await this.$services.dmKafkaAlterTopicConfigs({
          data: {
            dsId: this.tab.dsId,
            topicName,
            configs
          }
        });
        if (!res.success) {
          return false;
        }
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.alterConfigsVisible = false;
        await this.listLeaf(true);
      } finally {
        this.submitting = false;
      }
    },
    confirmDeleteGroup() {
      const groupId = this.selectedNode?.title || this.selectedNode?.objName;
      this.$Modal.confirm({
        title: this.$t('kafka-console-delete-group'),
        content: this.$t('kafka-console-delete-group-confirm', { name: groupId }),
        onOk: async () => {
          const res = await this.$services.dmKafkaDeleteConsumerGroup({
            data: {
              dsId: this.tab.dsId,
              groupId
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            if (this.tab.selectedTable) {
              this.tab.selectedTable = null;
            }
            await this.listLeaf(true);
          }
        }
      });
    },
    openResetOffsets() {
      const topics = this.consumedTopicRows.map((row) => row[KAFKA_JSON.TOPIC_NAME]).filter(Boolean);
      this.resetForm = {
        mode: 'EARLIEST',
        timestamp: new Date(),
        offset: 0,
        topics: [...topics]
      };
      this.resetOffsetsVisible = true;
    },
    async submitResetOffsets() {
      const groupId = this.selectedNode?.title || this.selectedNode?.objName;
      const topics = (this.resetForm.topics || []).map((item) => String(item).trim()).filter(Boolean);
      let timestampMs = null;
      if (this.resetForm.mode === 'TIMESTAMP') {
        const ts = this.resetForm.timestamp;
        if (!ts) {
          this.$Message.warning(this.$t('kafka-console-log-time-required'));
          return false;
        }
        timestampMs = ts instanceof Date ? ts.getTime() : new Date(ts).getTime();
        if (Number.isNaN(timestampMs)) {
          this.$Message.warning(this.$t('kafka-console-log-time-required'));
          return false;
        }
      }
      if (this.resetForm.mode === 'OFFSET' && (this.resetForm.offset === null || this.resetForm.offset === undefined || this.resetForm.offset < 0)) {
        this.$Message.warning(this.$t('kafka-console-absolute-offset-required'));
        return false;
      }
      this.submitting = true;
      try {
        const res = await this.$services.dmKafkaResetConsumerGroupOffsets({
          data: {
            dsId: this.tab.dsId,
            groupId,
            mode: this.resetForm.mode,
            topics,
            timestampMs,
            offset: this.resetForm.mode === 'OFFSET' ? this.resetForm.offset : null
          }
        });
        if (!res.success) {
          return false;
        }
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.resetOffsetsVisible = false;
        await this.listLeaf(true);
      } finally {
        this.submitting = false;
      }
    }
  }
};
</script>

<style scoped lang="less">
.kafka-console {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  background: var(--bg-color, #fff);
}

.kafka-console__toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 8px 12px;
  border-bottom: 1px solid var(--border-color, #e8e8e8);
}

.kafka-console__toolbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.kafka-console__toolbar-right {
  display: flex;
  align-items: center;
  min-width: 0;
}

.kafka-console__body {
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: 16px;
}

.kafka-console__empty-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.kafka-console__empty-desc {
  color: #888;
  margin-bottom: 16px;
}

.kafka-console__summary {
  display: flex;
  gap: 12px;
}

.kafka-console__summary-item {
  min-width: 140px;
  padding: 12px 16px;
  border: 1px solid var(--border-color, #e8e8e8);
  border-radius: 4px;
}

.kafka-console__summary-label {
  color: #888;
  font-size: 12px;
  margin-bottom: 6px;
}

.kafka-console__summary-value {
  font-size: 22px;
  font-weight: 600;
}

.kafka-console__section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  word-break: break-all;
}

.kafka-console__section-title--sub {
  margin-top: 20px;
  font-size: 14px;
}

.kafka-console__kv {
  width: 100%;
  max-width: 720px;
  border-collapse: collapse;

  th,
  td {
    border: 1px solid var(--border-color, #e8e8e8);
    padding: 8px 12px;
    text-align: left;
    vertical-align: top;
  }

  th {
    width: 180px;
    background: rgba(0, 0, 0, 0.02);
    font-weight: 500;
    color: #666;
  }
}

.kafka-console__config-hint {
  margin-bottom: 12px;
  color: #888;
}
</style>
