<template>
  <div class="step step-one">
    <a-form-model ref="stepOneRef" :model="stepOneData" :rules="stepOneDataValidate" :labelCol="{ style: 'min-width: 100px' }">
      <a-form-model-item :label="$t('bu-shu-lei-xing')" prop="deployEnvType" v-if="!isDesktop">
        <a-radio-group v-model="stepOneData.deployEnvType" button-style="solid">
          <a-radio-button v-for="type in Object.values(deployEnvListMap)" :key="type.value" :value="type.value">
            {{ type.name }}
          </a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item :label="$t('shu-ju-ku-lei-xing')" prop="dataSourceType">
        <a-radio-group v-model="stepOneData.dataSourceType" button-style="solid">
          <a-radio-button v-for="type in dsTypeList" :key="type.name" :value="type.type" style="width: 160px">
            <cc-data-source-icon
              style="background: transparent; width: 20px; height: 20px; line-height: 20px"
              :instanceType="stepOneData.deployEnvType"
              :size="18"
              :type="type.type"
              color="#0087c7"
            />
            <span>{{ type.name }}</span>
          </a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item :label="$t('bang-ding-ji-qun')" prop="bindClusterId" v-if="!isDesktop">
        <div style="display: flex">
          <a-select
            v-model="stepOneData.bindClusterId"
            :dropdownMatchSelectWidth="false"
            :placeholder="$t('qing-xuan-ze')"
            style="width: 240px"
            @change="handleClusterSelect"
          >
            <a-select-option
              v-for="cluster in Object.values(clusterListMap)"
              :key="cluster.value"
              :disabled="!cluster.runningCount"
              :value="cluster.value"
            >
              {{ cluster.name }}/{{ cluster.desc }} {{ $t('shu-liang') }}:{{ cluster.runningCount }}/{{ cluster.workerCount }}
            </a-select-option>
          </a-select>
          <div v-if="allClusterWorkers === 0 || allClusterRunningWorkers === 0" class="cluster-error">
            <cc-iconfont name="warning" size="16" color="#FFA30E" style="margin: 0 10px" />
            <a-button type="link" @click="handleGoAddMachine" style="padding: 0">
              {{
                `${$t('zan-wu')}${allClusterRunningWorkers === 0 ? $t('cun-huo') : ''}${$t(
                  'ji-qi-qing-xian-zhi-ji-qi-guan-li-jie-mian-tian-jia-ji-qi'
                )}!`
              }}
            </a-button>
          </div>
        </div>
      </a-form-model-item>
      <a-form-model-item :label="$t('yun-hang-huan-jing')" prop="envId" v-if="!isDesktop">
        <a-select v-model="stepOneData.envId" :placeholder="$t('qing-xuan-ze')" style="width: 240px" @change="handleEnvChange">
          <a-select-option v-for="env in Object.values(envListMap)" :key="env.id" :value="env.id">
            {{ env.envName }}
          </a-select-option>
        </a-select>
        <a-button v-if="Object.values(envListMap).length === 0" style="margin-left: 10px" @click="goEnvPage">
          {{ $t('tian-jia-huan-jing') }}
        </a-button>
      </a-form-model-item>
      <Self v-if="isDesktop" :stepData="stepDataIfDeskTop"></Self>
      <a-form-model-item label=" " v-if="!isDesktop">
        <a-button type="primary" @click="handleNext">
          {{ $t('xia-yi-bu-xuan-ze-shu-ju-yuan') }}
        </a-button>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import * as Vue from 'vue';
import { CLUSTER_ENV } from '@/const';
import { ACTIONS_TYPE } from '@/store/actions';
import Self from './Self';

export default {
  name: 'One',
  components: { Self },
  props: {
    edit: {
      type: Boolean,
      default: false
    },
    stepOneDataProps: {
      type: Object
    },
    handleSubmitData: {
      type: Function,
      default: () => {}
    },
    handleUpdateDs: {
      type: Function,
      default: () => {}
    },
    closeModal: {
      type: Function,
      default: () => {}
    }
  },
  computed: {
    ...mapState({
      deployEnvListMap: (state) => state.deployEnvListMap,
      clusterListMap: (state) => state.clusterListMap,
      dsTypeList: (state) => state.dsTypeList,
      allClusterWorkers: (state) => state.allClusterWorkers,
      allClusterRunningWorkers: (state) => state.allClusterRunningWorkers
    }),
    ...mapGetters(['isDesktop'])
  },
  data() {
    return {
      labelCol: { span: 2 },
      wrapperCol: { span: 14 },
      envListMap: {},
      stepOneData: {
        deployEnvType: CLUSTER_ENV.SELF_MAINTENANCE,
        clusterEnvName: this.$t('zi-jian'),
        dataSourceType: 'MySQL',
        bindClusterId: undefined,
        bindClusterName: '',
        envId: undefined,
        envName: ''
      },
      stepOneDataValidate: {
        deployEnvType: [
          {
            required: true,
            message: this.$t('bu-shu-lei-xing-bu-neng-wei-kong')
          }
        ],
        dataSourceType: [
          {
            required: true,
            message: this.$t('shu-ju-ku-lei-xing-bu-neng-wei-kong')
          }
        ],
        bindClusterId: [
          {
            required: true,
            message: this.$t('bang-ding-ji-qun-bu-neng-wei-kong')
          }
        ],
        envId: [
          {
            required: true,
            message: this.$t('yun-hang-huan-jing-bu-neng-wei-kong')
          }
        ]
      },
      selectedAccessMachine: {},
      selectedEnv: {},
      stepDataIfDeskTop: [
        {
          dataSourceType: 'MySQL',
          deployEnvType: 'SELF_MAINTENANCE'
        }
      ]
    };
  },
  methods: {
    handleClusterSelect(value) {
      this.stepOneData.bindClusterName = this.clusterListMap[value].clusterName;
    },
    goEnvPage() {
      this.$router.push({ name: 'System_Env' });
    },
    handleGoAddMachine() {
      this.$router.push({
        name: 'System_Machine'
      });
      this.$bus.emit('changeSidebar', '/ccsystem/machine');
    },
    handleEnvChange(envId) {
      this.stepOneData.envName = this.envListMap[envId].envName;
    },
    async listEnv() {
      const res = await this.$services.rdpDsEnvList({ data: { envName: '' } });
      if (res.success) {
        const temp = {};
        res.data.forEach((env) => {
          temp[env.id] = env;
        });
        this.envListMap = temp;
      }
    },
    handleChangeDsType(type) {
      console.log(type);
      this.stepOneData.dataSourceType = type;
    },
    handleNext() {
      this.$refs.stepOneRef.validate((valid) => {
        if (this.clusterListMap[this.stepOneData.bindClusterId].workerCount && valid) {
          this.handleSubmitData(0, this.stepOneData);
        }
      });
    }
  },
  async created() {
    this.stepOneData = { ...this.stepOneData, ...this.stepOneDataProps };
    await this.listEnv();
    await this.$store.dispatch(ACTIONS_TYPE.GET_DEPLOY_ENV_LIST);
    await this.$store.dispatch(ACTIONS_TYPE.GET_CLUSTER_LIST, this.stepOneData.deployEnvType);
    await this.$store.dispatch(ACTIONS_TYPE.GET_DS_TYPE_LIST, this.stepOneData.deployEnvType);

    Object.values(this.clusterListMap).forEach((cluster) => {
      if (cluster.runningCount) {
        this.stepOneData.bindClusterId = cluster.id;
        this.stepOneData.bindClusterName = cluster.name;
      }
    });

    Object.values(this.envListMap).forEach((env) => {
      this.stepOneData.envId = env.id;
      this.stepOneData.envName = env.envName;
    });
    this.stepDataIfDeskTop.push({
      dataSourceType: this.stepOneData.dataSourceType,
      deployEnvType: this.stepOneData.deployEnvType
    });
    console.log('this.stepDataIfDeskTop', this.stepDataIfDeskTop);
    if (this.isDesktop) {
      this.stepDataIfDeskTop[0].bindClusterId = this.stepOneData.bindClusterId;
      this.stepDataIfDeskTop[0].envId = this.stepOneData.envId;
    }
  },
  watch: {
    'stepOneData.deployEnvType': {
      handler(newValue, oldVue) {
        if (newValue !== oldVue) {
          this.$store.dispatch(ACTIONS_TYPE.GET_CLUSTER_LIST, newValue);
          this.$store.dispatch(ACTIONS_TYPE.GET_DS_TYPE_LIST, newValue);
          this.stepOneData.clusterEnvName = this.deployEnvListMap[newValue].name;
        }
      }
    },
    'stepOneData.dataSourceType': {
      handler(newValue) {
        this.stepDataIfDeskTop[0].dataSourceType = newValue;
      }
    }
  }
};
</script>

<style lang="less">
.step-one {
  .ant-form-item {
    display: flex;
  }
  .ant-form-item-label {
    width: 85px;
  }
}
</style>
