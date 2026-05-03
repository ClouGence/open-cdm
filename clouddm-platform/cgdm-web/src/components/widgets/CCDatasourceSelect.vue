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
    // 部署类型
    type: {
      type: String,
      default: 'source'
    },
    deployTypes: {
      type: Array,
      default: () => []
    },
    // 部署类型
    selectDeployType: {
      type: String,
      default: ''
    },
    // 数据源类型
    selectDataSourceType: {
      type: String,
      default: ''
    },
    // 数据源分组数据
    dataSourceGroups: {
      type: Array,
      default: () => []
    },
    // 部署类型change回调
    handleDeployTypeChange: {
      type: Function,
      default: () => {}
    },
    // 数据源类型change回调
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
    // 获取对应部署类型映射数据源名称
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
