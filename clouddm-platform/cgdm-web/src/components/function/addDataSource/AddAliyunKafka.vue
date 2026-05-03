<template>
  <div>
    <FormItem :label="$t('ren-zheng-fang-shi')" prop="securityType">
      <Select v-model="addDataSourceForm.rdsList[index].securityType" style="width: 280px" @on-change="handleUpdateAddForm">
        <Option v-if="!!addDataSourceForm.rdsList[index].publicHost" value="USER_PASSWD_WITH_TLS">
          {{ $t('zhang-hao-mi-ma-he-tls') }}
        </Option>
        <Option v-if="!!addDataSourceForm.rdsList[index].privateHost" value="USER_PASSWD">
          {{ $t('zhang-hao-mi-ma') }}
        </Option>
        <Option v-if="!addDataSourceForm.rdsList[index].publicHost" value="NONE">
          {{ $t('wu-zhang-hao-mi-ma') }}
        </Option>
      </Select>
    </FormItem>
    <div
      v-if="
        addDataSourceForm.rdsList[index].securityType === 'USER_PASSWD_WITH_TLS' || addDataSourceForm.rdsList[index].securityType === 'USER_PASSWD'
      "
    >
      <FormItem :label="$t('zhang-hao')">
        <Input v-model="addDataSourceForm.rdsList[index].account" style="width: 280px" />
      </FormItem>
      <FormItem :label="$t('mi-ma')">
        <Input v-model="addDataSourceForm.rdsList[index].password" style="width: 280px" type="password" password />
        <Tooltip placement="right-start">
          <Icon type="ios-help-circle-outline" style="font-size: 14px; margin-left: 5px" />
          <template #content>
            {{ $t('mi-ma-jing-guo-jia-mi-cun-chu-bao-zhang-an-quan-hou-xu-chuang-jian-shu-ju-ren-wu-ke-zhi-jie-lian-jie-wu-xu-zhong-xin-tian-xie') }}
          </template>
        </Tooltip>
      </FormItem>
      <FormItem
        :label="$t('ssl-pei-zhi-wen-jian')"
        prop="securityType"
        v-if="addDataSourceForm.rdsList[index].securityType === 'USER_PASSWD_WITH_TLS'"
      >
        <input @change="handleFileChange(index, $event)" type="file" name="uploadfile" id="uploadfile1" />
        <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
      </FormItem>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    index: Number,
    addDataSourceForm: Object,
    handleFileChange: Function,
    handleKeyTabFileChange: Function,
    handleUpdateAddForm: Function
  }
};
</script>
<style lang="less" scoped>
.datasource-setting-title {
  font-weight: 500;
  margin-bottom: 20px;
}
</style>
