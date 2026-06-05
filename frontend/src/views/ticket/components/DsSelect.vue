<template>
  <div style="display: flex">
    <div style="display: flex; align-items: center; width: 240px; margin-right: 8px">
      <CustomIcon v-if="selectedInstance" :type="selectedInstance.objAttr.dsType" rightMargin />
      <Select
        style="flex: 1"
        v-model="ticketData.instanceId"
        @on-change="handleChangeInstance"
        :placeholder="$t('qing-xuan-ze-shu-ju-yuan-shi-li')"
        show-search
      >
        <Option v-for="ds in allDsList" :value="ds.objId" :key="ds.objId" :label="ds.objName">
          <div style="display: flex; align-items: center">
            <!-- <cc-svg-icon :name="ds.objAttr.dsType" style="margin-right: 5px" /> -->
            <CustomIcon :type="ds.objAttr.dsType" rightMargin />
            {{ ds.objName }}
          </div>
        </Option>
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
    handleChangeInstance: Function,
    handleCatalogChange: Function,
    selectedDs: Object
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
