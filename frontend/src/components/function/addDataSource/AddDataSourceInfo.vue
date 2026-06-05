<template>
  <div>
    <Form :model="addDataSourceForm" label-position="right" :label-width="100">
      <Divider orientation="left">{{ $t('xin-zeng-shu-ju-yuan-xin-xi-que-ren') }}</Divider>
      <FormItem :label="$t('bu-shu-lei-xing')" prop="instanceType">
        <span>{{ Mapping.cloudOrIdcName[addDataSourceForm.instanceType] }}</span>
      </FormItem>
      <FormItem :label="$t('shu-ju-ku-lei-xing')" prop="type">
        <span>
          {{ addDataSourceForm.instanceType === 'ALIBABA_CLOUD_HOSTED' ? Mapping.aliyunType[addDataSourceForm.type] : addDataSourceForm.type }}
        </span>
      </FormItem>
      <FormItem label="host" prop="host" v-if="addDataSourceForm.instanceType === 'SELF_MAINTENANCE' || addDataSourceForm.ifAkSK === 'false'">
        <span>{{ addDataSourceForm.host }}:{{ addDataSourceForm.port }}</span>
      </FormItem>
      <FormItem :label="$t('miao-shu')" v-if="addDataSourceForm.instanceType === 'SELF_MAINTENANCE' || addDataSourceForm.ifAkSK === 'false'">
        <span>{{ addDataSourceForm.instanceDesc }}</span>
      </FormItem>
      <FormItem
        :label="$t('zhang-hao')"
        prop="host"
        v-if="
          (addDataSourceForm.instanceType === 'SELF_MAINTENANCE' || addDataSourceForm.ifAkSK === 'false') &&
          addDataSourceForm.account &&
          addDataSourceForm.type !== 'Hive'
        "
      >
        <span>{{ addDataSourceForm.account }}</span>
      </FormItem>
      <FormItem
        :label="$t('mi-ma')"
        prop="host"
        v-if="(addDataSourceForm.instanceType === 'SELF_MAINTENANCE' || addDataSourceForm.ifAkSK === 'false') && addDataSourceForm.password"
      >
        <span>******</span>
      </FormItem>
      <FormItem label="AK" prop="aliyunAk" v-if="addDataSourceForm.instanceType !== 'SELF_MAINTENANCE' && addDataSourceForm.ifAkSK === 'true'">
        <span>{{ addDataSourceForm.aliyunAk }}</span>
      </FormItem>
      <FormItem label="SK" prop="aliyunSk" v-if="addDataSourceForm.instanceType !== 'SELF_MAINTENANCE' && addDataSourceForm.ifAkSK === 'true'">
        <span>******</span>
      </FormItem>
      <FormItem
        :label="$t('rds-lie-biao')"
        prop="instanceId"
        v-if="addDataSourceForm.instanceType !== 'SELF_MAINTENANCE' && addDataSourceForm.ifAkSK === 'true'"
      >
        <Table size="small" border :columns="rdsColumns" :data="addDataSourceForm.rdsList">
          <template #action="{ row, index }">
            <div>
              <Checkbox :disabled="addDataSourceForm.type === 'Greenplum'" v-model="row.autoCreate" @on-change="handleAutoCreate(row, index, $event)">
                <span style="color: #0bb9f8">{{ $t('zi-dong-chuang-jian-zhang-hao-mi-ma') }}</span>
              </Checkbox>
              <Tooltip
                transfer
                :content="
                  $t(
                    'xuan-ze-zi-dong-chuang-jian-zhang-hao-mi-ma-hou-cloudcanal-hui-zi-dong-qu-dui-ying-shu-ju-yuan-chuang-jian-zhang-hao-chuang-jian-shu-ju-ren-wu-shi-wu-xu-tian-xie-xiang-guan-xin-xi-ru-mei-you-xuan-ze-zi-dong-chuang-jian-zhang-hao-mi-ma-ze-hou-xu-xu-yao-nin-qin-zi-dao-rds-jin-hang-chuang-jian-bing-qie-hou-xu-chuang-jian-shu-ju-chuan-shu-ren-wu-xu-yao-shou-dong-shu-ru-zhang-hao-mi-ma'
                  )
                "
                placement="top-end"
              >
                <Icon style="font-size: 14px" type="ios-information-circle-outline" />
              </Tooltip>
            </div>
          </template>
        </Table>
      </FormItem>
      <div v-if="addDataSourceForm.instanceType === 'SELF_MAINTENANCE' && addDataSourceForm.type === 'Hive'">
        <FormItem :label="$t('ren-zheng-fang-shi')" prop="hdfsSecurityType">
          <span>{{ addDataSourceForm.securityType }}</span>
        </FormItem>
        <FormItem :label="$t('hive-principal')" prop="hdfsSecurityType" v-if="addDataSourceForm.securityType === 'KERBEROS'">
          <span>{{ addDataSourceForm.account }}</span>
        </FormItem>
        <FormItem :label="$t('hdfs-host')" prop="hdfsIp">
          <span>{{ addDataSourceForm.hdfsIp }}:{{ addDataSourceForm.hdfsPort }}</span>
        </FormItem>
        <FormItem :label="$t('hdfs-principal')" prop="hdfsSecurityType" v-if="addDataSourceForm.securityType === 'KERBEROS'">
          <span>{{ addDataSourceForm.hdfsPrincipal }}</span>
        </FormItem>
        <FormItem :label="$t('hdfs-shu-cang-lu-jing')" prop="hdfsDwPath">
          <span>{{ addDataSourceForm.hdfsDwDir }}</span>
        </FormItem>
        <FormItem label="HadoopUser" prop="hadoopUser" v-if="addDataSourceForm.securityType === 'NONE'">
          <span>{{ addDataSourceForm.account }}</span>
        </FormItem>
      </div>
    </Form>
  </div>
</template>
<script>
import Mapping from '@/views/util';

export default {
  props: {
    addDataSourceForm: Object
  },
  data() {
    return {
      Mapping,
      selectedRds: {},
      rdsColumns: [
        {
          title: this.$t('shi-li-id'),
          key: 'instanceId'
        },
        {
          title: 'host',
          key: 'host'
        },
        {
          title: this.$t('miao-shu'),
          key: 'instanceDesc'
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action'
        }
      ]
    };
  },
  created() {
    this.addDataSourceForm.rdsList.map((rds) => {
      rds.autoCreate = true;
      return null;
    });
  },
  methods: {
    handleConfirmEditDesc() {
      this.addDataSourceForm.rdsList.map((item) => {
        if (item.instanceId === this.selectedRds.instanceId) {
          item.instanceDesc = this.selectedRds.instanceDesc;
        }
        return null;
      });
    },
    handleAutoCreate(row, index, data) {
      this.addDataSourceForm.rdsList[index].autoCreate = data;
    }
  }
};
</script>
