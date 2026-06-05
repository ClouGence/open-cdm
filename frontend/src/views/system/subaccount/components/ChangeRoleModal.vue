<template>
  <a-modal :visible="visible" :width="320" :title="$t('xiu-gai-jiao-se')" @cancel="handleClose">
    <Select v-model="subaccount.roleId" style="width: 276px">
      <Option v-for="role in roleList" :label="role.name" :key="role.value" :value="role.value">
        {{ role.name }}
      </Option>
    </Select>
    <div class="footer">
      <a-button type="primary" @click="handleChangeUserRole">{{ $t('que-ding') }}</a-button>
      <a-button @click="handleClose">{{ $t('qu-xiao') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'ChangeRoleModal',
  props: {
    visible: Boolean,
    roleList: Array,
    selectedSubaccount: Object,
    getSubaccountList: Function,
    handleClose: Function
  },
  data() {
    return {
      subaccount: {}
    };
  },
  methods: {
    async handleChangeUserRole() {
      const { parentId, uid, roleId } = this.subaccount;
      const data = {
        parentId,
        subAccountUid: uid,
        roleId
      };

      const res = await this.$services.rdpUserManagerUpdateUserRole({
        data,
        msg: this.$t('geng-xin-jiao-se-cheng-gong')
      });

      if (res.success) {
        await this.getSubaccountList();
        window.location.reload();
      }
    }
  },
  created() {
    this.subaccount = this.selectedSubaccount;
  }
};
</script>

<style lang="less"></style>
