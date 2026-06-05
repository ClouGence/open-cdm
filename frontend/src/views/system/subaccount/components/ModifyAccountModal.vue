<template>
  <a-modal :visible="visible" :width="560" :title="$t('xiu-gai-zhang-hao')" @cancel="handleClose">
    <a-form-model ref="newAccountFormRef" :model="newAccountForm" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
      <a-form-model-item :label="$t('xing-ming')" prop="userName">
        <a-input v-model="newAccountForm.userName" style="width: 420px" />
      </a-form-model-item>
      <a-form-model-item :label="$t('zi-zhang-hao')" prop="subAccount">
        <a-input v-model="newAccountForm.accountName" :addon-after="`@${selectedSubaccount.userDomain}`" style="width: 420px" />
      </a-form-model-item>
    </a-form-model>
    <div class="footer">
      <a-button type="primary" @click="handleModifySubAccount">{{ $t('que-ding') }}</a-button>
      <a-button @click="handleClose">{{ $t('qu-xiao') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'ModifyAccountModal',
  props: {
    visible: Boolean,
    roleList: Array,
    selectedSubaccount: Object,
    getSubaccountList: Function,
    handleClose: Function
  },
  data() {
    return {
      newAccountForm: {}
    };
  },
  methods: {
    async handleModifySubAccount() {
      const { uid, userDomain } = this.selectedSubaccount;
      const data = {
        targetUid: uid,
        userName: this.newAccountForm.userName,
        subAccount: `${this.newAccountForm.accountName}@${userDomain}`
      };

      const res = await this.$services.rdpUserManagerUpdateSubAccount({
        data,
        msg: this.$t('xiu-gai-zhang-hao-cheng-gong')
      });

      if (res.success) {
        await this.getSubaccountList();
        this.handleClose();
      }
    }
  },
  created() {
    this.newAccountForm.userName = this.selectedSubaccount.username;
    this.newAccountForm.accountName = this.selectedSubaccount.subAccount.split('@')[0];
  }
};
</script>

<style lang="less"></style>
