<template>
  <div>
    <FormItem :label="$t('hive-principal')" v-if="getSecurity(addDataSourceForm.securityType).userNamePrinciple">
      <Input v-model="addDataSourceForm.account" style="width: 280px" :placeholder="$t('li-ru-hivefqdndomain')" />
    </FormItem>
    <p class="datasource-setting-title">{{ $t('e-wai-xin-xi-she-zhi') }}</p>

    <!--        <Divider orientation="left" v-if="addDataSourceForm.instanceType==='SELF_MAINTENANCE'&&addDataSourceForm.type==='Hive'">额外信息设置</Divider>-->
    <FormItem
      :label="$t('hdfs-host')"
      prop="hdfsIp"
      v-if="addDataSourceForm.instanceType === 'SELF_MAINTENANCE' && addDataSourceForm.type === 'Hive'"
    >
      <Input v-model="addDataSourceForm.hdfsIp" style="width: 280px" />
      <span>:</span>
      <Input v-model="addDataSourceForm.hdfsPort" style="width: 100px" />
      <span style="margin-left: 10px; color: rgb(128, 134, 149)">
        {{ $t('yong-yu-sheng-cheng-fsdefaultfs-lian-jie') }}
      </span>
    </FormItem>
    <FormItem
      :label="$t('hdfs-principal')"
      prop="hdfsSecurityType"
      v-if="
        addDataSourceForm.instanceType === 'SELF_MAINTENANCE' && addDataSourceForm.type === 'Hive' && addDataSourceForm.securityType === 'KERBEROS'
      "
    >
      <Input v-model="addDataSourceForm.hdfsPrincipal" style="width: 280px" :placeholder="$t('li-ru-hdfsfqdndomain')" />
    </FormItem>
    <FormItem
      label="HadoopUser"
      prop="account"
      v-if="addDataSourceForm.instanceType === 'SELF_MAINTENANCE' && addDataSourceForm.type === 'Hive' && addDataSourceForm.securityType === 'NONE'"
    >
      <Input v-model="addDataSourceForm.account" style="width: 280px" />
      <span style="margin-left: 10px; color: rgb(128, 134, 149)">
        {{ $t('yi-gai-shen-fen-cao-zuo-hdfs') }}
      </span>
    </FormItem>
    <FormItem
      :label="$t('hdfs-shu-cang-lu-jing')"
      prop="hdfsDwDir"
      v-if="addDataSourceForm.instanceType === 'SELF_MAINTENANCE' && addDataSourceForm.type === 'Hive'"
    >
      <Input v-model="addDataSourceForm.hdfsDwDir" style="width: 280px" :placeholder="$t('li-ru-userhivewarehouse')" />
    </FormItem>
  </div>
</template>
<script>
export default {
  props: {
    addDataSourceForm: Object,
    handleFileChange: Function,
    handleKeyTabFileChange: Function,
    getSecurity: Function
  }
};
</script>
<style lang="less" scoped>
.datasource-setting-title {
  font-weight: 500;
  margin-bottom: 20px;
}
</style>
