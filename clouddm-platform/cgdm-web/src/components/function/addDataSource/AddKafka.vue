<template>
  <div>
    <FormItem :label="$t('ren-zheng-fang-shi')" prop="securityType">
      <Select v-model="addDataSourceForm.securityType" style="width: 280px">
        <Option value="KERBEROS">KERBEROS</Option>
        <!--        <Option value="USER_PASSWD_WITH_TLS">USER_PASSWD_WITH_TLS</Option>-->
        <!--        <Option value="USER_PASSWD">USER_PASSWD</Option>-->
        <Option value="NONE">{{ $t('wu-zhang-hao-mi-ma') }}</Option>
      </Select>
    </FormItem>
    <FormItem :label="$t('kerberos-pei-zhi-wen-jian')" prop="securityType" v-if="addDataSourceForm.securityType === 'KERBEROS'">
      <input @change="handleFileChange" type="file" name="uploadfile" id="uploadfile" />
      <span style="margin-left: 10px; color: rgb(128, 134, 149)">
        {{ $t('kerberos-ke-hu-duan-pei-zhi-yi-ban-wei-yu-yi-jia-ru-ren-zheng-ti-xi-ji-qi-de-etckrb5conf') }}
      </span>
    </FormItem>
    <FormItem :label="$t('keytab-wen-jian')" prop="securityType" v-if="addDataSourceForm.securityType === 'KERBEROS'">
      <input @change="handleKeyTabFileChange" type="file" name="uploadKeytabFile" id="uploadKeytabFile" />
      <span style="margin-left: 10px; color: rgb(128, 134, 149)">
        {{ $t('jian-yi-zhong-xin-sheng-cheng-he-cheng-hive-he-dui-ying-hdfs-principal-ren-zheng') }}
      </span>
    </FormItem>
    <div
      v-if="
        addDataSourceForm.securityType === 'USER_PASSWD_WITH_TLS' ||
        addDataSourceForm.securityType === 'USER_PASSWD' ||
        addDataSourceForm.securityType === 'USER_PASSWD'
      "
    >
      <FormItem :label="$t('zhang-hao')">
        <Input v-model="addDataSourceForm.account" style="width: 280px" />
      </FormItem>
      <FormItem :label="$t('mi-ma')">
        <Input v-model="addDataSourceForm.password" style="width: 280px" type="password" password />
        <Tooltip placement="right-start">
          <Icon type="ios-help-circle-outline" style="font-size: 14px; margin-left: 5px" />
          <template #content>
            {{ $t('mi-ma-jing-guo-jia-mi-cun-chu-bao-zhang-an-quan-hou-xu-chuang-jian-shu-ju-ren-wu-ke-zhi-jie-lian-jie-wu-xu-zhong-xin-tian-xie') }}
          </template>
        </Tooltip>
      </FormItem>
      <FormItem :label="$t('ssl-pei-zhi-wen-jian')" prop="securityType" v-if="addDataSourceForm.securityType === 'USER_PASSWD_WITH_TLS'">
        <input @change="handleFileChange" type="file" name="uploadfile" id="uploadfile1" />
        <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
      </FormItem>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    addDataSourceForm: Object,
    handleFileChange: Function,
    handleKeyTabFileChange: Function
  }
};
</script>
<style lang="less" scoped>
.datasource-setting-title {
  font-weight: 500;
  margin-bottom: 20px;
}
</style>
