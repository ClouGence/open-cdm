<template>
  <div style="padding: 20px">
    <div v-if="!addWorker && !addMoreWorker">
      <Form :model="addClusterForm" label-position="right" :label-width="100">
        <FormItem :label="$t('ji-qun-miao-shu-0')">
          <Input v-model="addClusterForm.clusterDesc" style="width: 300px" />
        </FormItem>
        <FormItem :label="$t('yun-huo-ji-fang-ming-cheng')">
          <RadioGroup v-model="addClusterForm.cloudOrIdcName" type="button">
            <Radio v-for="cloudOrIdcName of computedCloudOrIdcNames" :label="cloudOrIdcName.cloudOrIdcName" :key="cloudOrIdcName.cloudOrIdcName">
              {{ cloudOrIdcName.i18nName }}
            </Radio>
          </RadioGroup>
        </FormItem>
        <FormItem :label="$t('di-yu')">
          <RadioGroup v-model="addClusterForm.region" type="button">
            <Radio v-for="region of regions" :label="region.region" :key="region.region" :disabled="supportedRegions.indexOf(region.region) === -1">
              {{ region.i18nName }}
            </Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <div style="margin-top: 20px">
        <Button type="primary" @click="handleSaveWorker">
          {{ $t('bao-cun-bing-tian-jia-ji-qi') }}
        </Button>
      </div>
    </div>
  </div>
</template>
<script>
// import { createCluster } from '@/services/cc/api/cluster';
import UtilMapping from '@/views/util';

export default {
  props: {
    closeShowAddCluster: Function,
    updateStep: Function,
    addClusterForm: Object,
    getClusterId: Function
  },
  data() {
    return {
      addWorker: false,
      addMoreWorker: false,
      UtilMapping,
      isEdit: false,
      cloudOrIdcNames: [],
      regions: [],
      supportedRegions: [],
      columns1: [
        {
          title: this.$t('xu-hao'),
          type: 'index',
          width: 100,
          align: 'center'
        },
        {
          title: this.$t('miao-shu'),
          key: 'remark'
        },
        {
          title: 'IP',
          key: 'ip'
        }
      ],
      data1: [
        {
          remark: this.$t('ce-shi-ji-qi-1'),
          ip: '127.0.0.1'
        }
      ]
    };
  },
  mounted() {
    // this.listRegions();
    this.listCloudOrIdcNames();
  },
  methods: {
    listRegions() {
      this.$services.ccConstantRegion().then((res) => {
        if (res.success) {
          this.regions = res.data;
          this.$services.ccConstantSupportedRegion({ data: { cloudOrIdcName: this.addClusterForm.cloudOrIdcName } }).then((res2) => {
            if (res2.success) {
              this.supportedRegions = res2.data.map((region) => region.region);
            }
          });
        }
      });
    },
    listCloudOrIdcNames() {
      this.$services.ccConstantCloudOrIdcName().then((res) => {
        if (res.success) {
          this.cloudOrIdcNames = res.data;
        }
      });
    },
    handleSaveWorker() {
      this.$services.ccClusterCreate({ data: this.addClusterForm }).then((res) => {
        if (res.success) {
          this.getClusterId(res.data);
          this.updateStep();
        }
      });
    }
  },
  computed: {
    computedCloudOrIdcNames() {
      return this.cloudOrIdcNames.filter((cloudOrIdcName) => cloudOrIdcName === 'ALIBABA_CLOUD' || cloudOrIdcName === 'SELF_MAINTENANCE');
    }
  }
};
</script>
