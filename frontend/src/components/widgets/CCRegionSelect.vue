<template>
  <div class="cc-region-select">
    <div
      v-click-out-hide="hideRegions"
      :style="`width: ${width}px;${defaultText === '获取数据源' ? 'display: flex;justify-content: center;border: none' : ''}`"
      class="choose"
      @click="showRegions = !showRegions"
    >
      <div>{{ selectedRegion.i18nName || defaultText }}</div>
      <div v-if="defaultText === '请选择'">
        <Icon :type="showRegions ? 'ios-arrow-up' : 'ios-arrow-down'" />
      </div>
    </div>
    <transition :duration="1000" name="fade">
      <div v-show="showRegions" class="regions">
        <div v-for="region in Object.values(regionList)" :key="region.name" class="region">
          <!--          <div class="name">{{ region.name }}</div>-->
          <a-button
            v-for="city in region.children"
            :key="city.region"
            style="width: 91px; margin-right: 20px; margin-bottom: 10px"
            @click="handleSelectRegion(city)"
          >
            {{ city.i18nName }}
          </a-button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { CLUSTER_ENV } from '@/const';

export default {
  name: 'CCRegionSelect',
  model: {
    prop: '__selected_region_name__',
    event: '__handle_select_region_name__'
  },
  computed: {
    ...mapState({
      aliyunRegionListMap: (state) => state.aliyunRegionListMap,
      selfRegionListMap: (state) => state.selfRegionListMap
    }),
    regionList() {
      console.log(this.aliyunRegionListMap, this.selfRegionListMap);
      return this.env === CLUSTER_ENV.ALIBABA_CLOUD_HOSTED ? this.aliyunRegionListMap : this.selfRegionListMap;
    }
  },
  props: {
    defaultText: {
      type: String,
      default: '请选择'
    },
    env: String,
    width: String,
    handleClickBtn: {
      type: Function,
      default: () => {}
    }
  },
  data() {
    return {
      selectedRegion: {},
      showRegions: false
    };
  },
  created() {
    this.selectedRegion = this.__selected_region_name__ || '请选择';
    if (this.env === 'SELF_MAINTENANCE') {
      this.selectedRegion = this.selfRegionListMap.其他.children[0];
      this.hideRegions();
      this.handleClickBtn(this.selfRegionListMap.其他.children[0].regionName);
    }
    // this.$store.dispatch(ACTIONS_TYPE.GET_REGION_LIST, this.env || CLUSTER_ENV.ALIBABA_CLOUD_HOSTED);
  },
  methods: {
    handleSelectRegion(city) {
      this.selectedRegion = city;
      this.hideRegions();
      this.handleClickBtn(city.region);
    },
    hideRegions() {
      this.showRegions = false;
    }
  },
  watch: {
    selectedRegion(city) {
      this.$emit('__handle_select_region_name__', city.region);
    },
    env: {
      handler(newValue, oldValue) {
        if (newValue !== oldValue) {
          // this.$store.dispatch(ACTIONS_TYPE.GET_REGION_LIST, newValue);
          this.selectedRegion = {};
          this.showRegions = false;
        }
      }
    }
  }
};
</script>

<style lang="less" scoped>
.cc-region-select {
  line-height: 1.5;

  .choose {
    width: 180px;
    height: 30px;
    background: #fff;
    padding: 0 10px;
    border: 1px solid rgba(218, 218, 218, 1);
    display: flex;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
    position: relative;
  }

  .regions {
    z-index: 9;
    width: 580px;
    height: 350px;
    position: absolute;
    margin-top: 10px;
    background: white;
    box-shadow: 1px 1px 6px 0 rgba(164, 164, 164, 0.66);
    border: 1px solid rgba(186, 189, 197, 1);
    padding: 20px 0 20px 20px;
    overflow: auto;

    .region {
      font-weight: bold;

      .name {
        margin-bottom: 10px;
        color: #555;
      }
    }
  }

  .ant-btn-default {
    background: none;
    border-color: #dadada;

    &:hover {
      border-color: #abdcee;
      color: #0bb9f8;
    }
  }

  .ant-btn[disabled]:hover {
    color: #dddddd;
  }
}
</style>
