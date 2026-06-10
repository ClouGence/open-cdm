<template>
  <div>
    <div class="deploy-type-warp">
      <div class="field-wrap">{{ $t('bu-shu-lei-xing-0') }}</div>
      <RadioGroup v-model="localSelectDeployType" type="button" class="value-warp radio-group-radius-warp">
        <Radio v-for="type of deployTypes" :label="type.deployEnvType" :key="type.deployEnvType">
          {{ type.i18nName }}
        </Radio>
      </RadioGroup>
    </div>
    <div class="datasource-warp">
      <div class="field-wrap">{{ type === 'source' ? $t('yuan-lei-xing') : $t('mu-biao-lei-xing') }}</div>
      <RadioGroup v-model="localSelectDataSourceType" type="button" class="value-warp radio-group-radius-warp-datasource custom-radio-group">
        <div class="mb-6 radio-group" v-for="(dataSourceGroup, index) in dataSourceGroups" :key="index">
          <Radio
            class="custom-radio"
            v-for="ds in dataSourceGroup"
            :label="ds.dataSourceType"
            :key="ds.dataSourceType"
            :disabled="!ds.authorized"
            style="width: 160px; text-align: center; display: inline-flex; align-items: center; justify-content: center; border-radius: 4px"
          >
            <span v-if="ds.authorized">
              <span class="mid-text">
                {{ getShowNameByDeployTypeAndDsName(localSelectDeployType, ds.dataSourceType) }}
              </span>
              <CustomIcon :type="ds.dataSourceType" leftMargin="6px" size="18px" :instanceType="selectDeployType" />
            </span>
          </Radio>
        </div>
      </RadioGroup>
    </div>
  </div>
</template>
<script>
import Mapping from '@/views/util';

export default {
  props: {
    // Type of deployment
    type: {
      type: String,
      default: 'source'
    },
    deployTypes: {
      type: Array,
      default: () => []
    },
    // Type of deployment
    selectDeployType: {
      type: String,
      default: ''
    },
    // Type of data source
    selectDataSourceType: {
      type: String,
      default: ''
    },
    // Data source grouping data
    dataSourceGroups: {
      type: Array,
      default: () => []
    },
    // Deployment type Change back
    handleDeployTypeChange: {
      type: Function,
      default: () => {}
    },
    // Data source typechange echo
    handleTypeChange: {
      type: Function,
      default: () => {}
    }
  },
  computed: {
    localSelectDeployType: {
      get() {
        return this.selectDeployType;
      },
      set(val) {
        this.$emit('update:selectDeployType', val);
      }
    },
    localSelectDataSourceType: {
      get() {
        return this.selectDataSourceType;
      },
      set(val) {
        this.$emit('update:selectDataSourceType', val);
      }
    }
  },
  methods: {
    // Get the corresponding deployment type map data source name
    getShowNameByDeployTypeAndDsName(instanceType, type) {
      if (instanceType) {
        const typeNameList = Mapping.deployDsMap[instanceType];
        return typeNameList[type] === undefined ? type : typeNameList[type];
      }
    }
  }
};
</script>

<style scoped lang="less">
.deploy-type-warp {
  display: flex;
  margin-bottom: 20px;
}
.datasource-warp {
  display: flex;
}
.field-wrap {
  width: 100px;
}
.value-warp {
  flex: 1;
}
</style>
