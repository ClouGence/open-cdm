<template>
  <Modal v-model="showAkSk" width="720px" footer-hide :title="$t('a-li-yun-lin-shi-fang-wen-quan-xian')">
    <Alert type="warning" show-icon>
      <p>
        {{ $t('a-li-yun-aksk-xu-yao-fu-yu-bi-yao-de-quan-xian') }}
        <a :href="`${store.state.docUrlPrefix}/reference/rds_mysql_ram_least_privilege`" target="_blank">
          {{ $t('ru-he-fu-quan') }}
        </a>
      </p>
      <p style="margin-top: 8px">
        {{ $t('a-li-yun-aksk-jin-yong-lai-shen-qingali-yun-lin-shi-fang-wen-quan-xian-cloudcanal-bu-hui-cun-chu') }}
      </p>
    </Alert>
    <Form style="margin-top: 12px" label-position="right" :label-width="130">
      <FormItem label="AccessKey ID" prop="aliyunAk">
        <Input v-model="aliyunAk" style="width: 280px" />
      </FormItem>
      <FormItem label="AccessKey Secret" prop="aliyunSk">
        <Input v-model="aliyunSk" type="password" password style="width: 280px" />
      </FormItem>
    </Form>
    <template #footer>
      <Button type="primary" @click="handleApplyStToken">{{ $t('que-ding') }}</Button>
      <Button @click="handleCancelEdit">{{ $t('qu-xiao') }}</Button>
    </template>
  </Modal>
</template>
<script>
import store from '@/store';

export default {
  props: {
    // nextStep:Function
  },
  data() {
    return {
      store,
      aliyunAk: '',
      aliyunSk: '',
      showAkSk: false
    };
  },
  methods: {
    handleApplyStToken() {
      this.showAkSk = false;
      this.$services
        .ccAliyunStsApplyStsToken({
          data: { userAk: this.aliyunAk, userSk: this.aliyunSk }
        })
        .then((res) => {
          if (res.success) {
            // if (this.nextStep){
            //     this.nextStep();
            // }
          }
        });
    },
    handleShowAkSk() {
      this.showAkSk = true;
    },
    handleCancelEdit() {
      this.showAkSk = false;
    }
  }
};
</script>
