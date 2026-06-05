<template>
  <CCModal v-model="showAliyunAkSk" width="720px" :title="$t('a-li-yun-aksk')">
    <div>
      <Alert type="warning" show-icon>
        <p>
          {{ $t('a-li-yun-aksk-xu-yao-fu-yu-bi-yao-de-quan-xian') }}
          <a :href="`${store.state.docUrlPrefix}/reference/rds_mysql_ram_least_privilege`" target="_blank">
            {{ $t('ru-he-fu-quan') }}
          </a>
        </p>
        <!--        <p style="margin-top: 8px">{{ $t('a-li-yun-aksk-jin-bao-cun-2-xiao-shi-guo-qi-hou-zi-dong-shan-chu') }}</p>-->
      </Alert>
      <Form style="margin-top: 12px" label-position="right" :label-width="150" ref="aksk-form" :rules="validate" :model="form">
        <FormItem label="AccessKey ID" prop="aliyunAk">
          <Input v-model="form.aliyunAk" style="width: 280px" autocomplete="new-password" />
        </FormItem>
        <FormItem label="AccessKey Secret" prop="aliyunSk">
          <Input v-model="form.aliyunSk" type="password" password style="width: 280px" autocomplete="new-password" />
        </FormItem>
      </Form>
    </div>
    <template #footer>
      <Button @click="handleCancelEdit">{{ $t('qu-xiao') }}</Button>
      <Button type="primary" @click="handleApplyStToken">{{ $t('que-ding') }}</Button>
    </template>
  </CCModal>
</template>
<script>
import store from '@/store';

export default {
  props: {
    nextStep: Function
  },
  data() {
    return {
      store,
      form: {
        aliyunAk: '',
        aliyunSk: ''
      },
      validate: {
        aliyunAk: [
          {
            required: true,
            message: this.$t('a-li-yun-ak-bu-neng-wei-kong')
          }
        ],
        aliyunSk: [
          {
            required: true,
            message: this.$t('a-li-yun-sk-bu-neng-wei-kong')
          }
        ]
      },
      showAliyunAkSk: false
    };
  },
  methods: {
    handleApplyStToken() {
      this.$refs['aksk-form'].validate((valid) => {
        if (valid) {
          this.$services
            .rdpUserUpdateAliyunAkSk({
              data: {
                ...this.form
              }
            })
            .then((res) => {
              if (res.success) {
                this.showAliyunAkSk = false;
                if (this.nextStep) {
                  this.nextStep();
                }
              }
            });
        }
      });
    },
    handleShowAkSk() {
      this.showAliyunAkSk = true;
    },
    handleCancelEdit() {
      this.showAliyunAkSk = false;
    }
  }
};
</script>
