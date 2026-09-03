<template>
  <div class="ticket-ds-select">
    <span v-if="required" class="ticket-ds-select__required">*</span>
    <div class="ticket-ds-select__instance" :class="{ 'is-error': error }">
      <CustomIcon v-if="selectedInstance" :type="selectedInstance.objAttr.dsType" rightMargin />
      <Select
        class="ticket-ds-select__control"
        v-model="ticketData.instanceId"
        @on-change="handleChangeInstance"
        :placeholder="$t('qing-xuan-ze-shu-ju-yuan-shi-li')"
        filterable
      >
        <OptionGroup v-for="group in groupedDsList" :key="group.envId" :label="group.envName">
          <Option v-for="ds in group.dsList" :value="ds.objId" :key="ds.objId" :label="ds.objName">
            <div class="ticket-ds-select__option">
              <CustomIcon :type="ds.objAttr.dsType" rightMargin />
              {{ ds.objName }}
            </div>
          </Option>
        </OptionGroup>
      </Select>
    </div>

    <template v-if="ticketData.showCatalogSelect">
      <Select
        style="width: 200px; margin-right: 8px"
        v-model="ticketData.catalog"
        @on-change="handleCatalogChange(true)"
        :placeholder="$t('qing-xuan-ze-shu-ju-ku')"
        show-search
      >
        <Option v-for="catalog in selectedDs.CATALOG_LIST" :value="catalog.objName" :key="catalog.objName" :label="catalog.objName">
          {{ catalog.objName }}
        </Option>
      </Select>
      <div
        style="cursor: pointer; margin-right: 8px; display: flex; align-items: center"
        @click="handleRefreshCacheCatalog"
        :title="$t('shua-xin-huan-cun')"
      >
        <CustomIcon type="icon-v2-Refresh" />
      </div>
    </template>

    <template v-if="ticketData.showSchemaSelect">
      <Select style="width: 200px; margin-right: 8px" v-model="ticketData.schema" show-search>
        <Option v-for="schema in selectedDs.SCHEMA_LIST" :value="schema.objName" :key="schema.objName" :label="schema.objName">
          {{ schema.objName }}
        </Option>
      </Select>
      <div
        style="cursor: pointer; margin-right: 8px; display: flex; align-items: center"
        @click="handleRefreshCacheSchema"
        :title="$t('shua-xin-huan-cun')"
      >
        <CustomIcon type="icon-v2-Refresh" />
      </div>
    </template>

    <div v-if="!ticketData.ticketEnable" style="line-height: 32px; color: red">
      {{ $t('huan-jing-mei-you-qi-yong-gong-dan') }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    ds: Object,
    ticketData: Object,
    allDsList: Array,
    allEnvList: Array,
    handleChangeInstance: Function,
    handleCatalogChange: Function,
    selectedDs: Object,
    required: Boolean,
    error: Boolean
  },
  data() {
    return {
      lastSchemaValue: '',
      lastCatalogValue: ''
    };
  },
  watch: {
    'ticketData.schema': function (newVal, oldVal) {
      this.lastSchemaValue = newVal;
    },
    'ticketData.catalog': function (newVal, oldVal) {
      this.lastCatalogValue = newVal;
    }
  },
  computed: {
    groupedDsList() {
      const dsListByEnvId = new Map();
      this.allDsList.forEach((ds) => {
        const envId = String(ds.objAttr.dsEnvId);
        if (!dsListByEnvId.has(envId)) {
          dsListByEnvId.set(envId, []);
        }
        dsListByEnvId.get(envId).push(ds);
      });

      return this.allEnvList
        .map((env) => ({
          envId: String(env.id),
          envName: env.envName,
          dsList: dsListByEnvId.get(String(env.id)) || []
        }))
        .filter((group) => group.dsList.length > 0);
    },
    selectedInstance() {
      return this.allDsList.find((ds) => ds.objId === this.ticketData.instanceId);
    }
  },
  methods: {
    handleRefreshCacheCatalog() {
      this.$emit('restore-catalog', this.lastCatalogValue);
    },
    handleRefreshCacheSchema() {
      this.$emit('restore-schema', this.lastSchemaValue);
    }
  }
};
</script>

<style lang="less" scoped>
.ticket-ds-select {
  display: flex;
  align-items: center;
}

.ticket-ds-select__required {
  margin-right: 4px;
  color: #f5222d;
  line-height: 32px;
}

.ticket-ds-select__instance {
  display: flex;
  align-items: center;
  width: 240px;
  margin-right: 8px;

  &.is-error {
    :deep(.ivu-select-selection) {
      border-color: #f5222d;
    }
  }
}

.ticket-ds-select__control {
  flex: 1;
}

.ticket-ds-select__option {
  display: flex;
  align-items: center;
}
</style>
