<template>
  <div class="table-list-layout approval-teams">
    <div class="table-list">
      <div class="content">
        <div class="option">
          <div class="left">
            <Input
              v-model="keyword"
              class="approval-team-search"
              clearable
              :placeholder="$t('shu-ru-ming-cheng-cha-zhao')"
              @on-enter="search"
              @on-clear="clearSearch"
            />
            <Button type="primary" ghost @click="search">{{ $t('cha-xun') }}</Button>
          </div>
          <div class="right">
            <Button v-if="editable" type="primary" icon="md-add" @click="editTeam()">{{ $t('xin-zeng') }}</Button>
          </div>
        </div>
        <div class="table-container">
          <Table :columns="columns" :data="filteredTeams" :loading="loading" :scroll="{ x: 800 }" size="small" border>
            <template #members="{ row }">{{ row.memberUids.map((uid) => participantName(uid)).join('、') }}</template>
            <template #actions="{ row }">
              <Button type="text" size="small" @click="editTeam(row)">{{ editable ? $t('bian-ji') : $t('cha-kan') }}</Button>
              <Button v-if="editable" type="text" size="small" @click="removeTeam(row)">{{ $t('shan-chu') }}</Button>
            </template>
          </Table>
        </div>
      </div>
    </div>
    <Modal v-model="visible" :title="editingId ? $t('approval-edit-team') : $t('approval-new-team')" :mask-closable="false" :width="560">
      <Form class="approval-team-form" :model="draft" label-position="top">
        <FormItem
          :label="$t('approval-team-name')"
          prop="teamName"
          :rules="[{ required: true, message: $t('approval-complete-team'), trigger: 'blur' }]"
          required
        >
          <Input v-model="draft.teamName" :maxlength="128" :disabled="!editable" />
        </FormItem>
        <FormItem :label="$t('miao-shu')"><Input v-model="draft.description" :maxlength="200" :disabled="!editable" /></FormItem>
        <FormItem
          :label="$t('approval-team-members')"
          prop="memberUids"
          :rules="[{ type: 'array', required: true, min: 1, message: $t('approval-complete-team'), trigger: 'change' }]"
          required
        >
          <Select v-model="draft.memberUids" multiple filterable :disabled="!editable">
            <Option v-for="person in participants" :key="person.userUid" :value="person.userUid">{{ person.userName }}</Option>
          </Select>
        </FormItem>
      </Form>
      <template #footer>
        <div class="approval-team-modal-actions">
          <Button @click="visible = false">{{ $t('qu-xiao') }}</Button>
          <Button v-if="editable" type="primary" :loading="saving" @click="saveTeam">{{ $t('bao-cun') }}</Button>
        </div>
      </template>
    </Modal>
  </div>
</template>
<script>
export default {
  name: 'ApprovalTeams',
  props: { editable: Boolean },
  data() {
    return {
      loading: false,
      saving: false,
      visible: false,
      keyword: '',
      appliedKeyword: '',
      editingId: '',
      config: { version: 0, teams: [] },
      participants: [],
      draft: { teamName: '', description: '', memberUids: [] }
    };
  },
  computed: {
    columns() {
      return [
        { title: this.$t('approval-team-name'), key: 'teamName', minWidth: 160, tooltip: true },
        { title: this.$t('miao-shu'), key: 'description', minWidth: 200, tooltip: true },
        { title: this.$t('approval-team-members'), slot: 'members', minWidth: 240 },
        { title: this.$t('cao-zuo'), slot: 'actions', fixed: 'right', width: 140 }
      ];
    },
    filteredTeams() {
      return this.config.teams.filter((t) => t.teamName.toLowerCase().includes(this.appliedKeyword));
    }
  },
  mounted() {
    this.load();
  },
  methods: {
    search() {
      this.appliedKeyword = this.keyword.trim().toLowerCase();
    },
    clearSearch() {
      this.keyword = '';
      this.appliedKeyword = '';
    },
    async load() {
      this.loading = true;
      try {
        const res = await this.$services.dmApprovalConfigurationQuery();
        if (res.success) {
          this.config = res.data.teamConfig;
          this.participants = res.data.participants;
        }
      } finally {
        this.loading = false;
      }
    },
    participantName(uid) {
      return this.participants.find((p) => p.userUid === uid)?.userName || uid;
    },
    editTeam(team) {
      this.editingId = team?.teamId || '';
      this.draft = team ? JSON.parse(JSON.stringify(team)) : { teamName: '', description: '', memberUids: [] };
      this.visible = true;
    },
    async persist(teams) {
      this.saving = true;
      try {
        const res = await this.$services.dmApprovalSaveTeams({ data: { ...this.config, teams } });
        if (res.success) {
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
          this.visible = false;
          await this.load();
        }
      } finally {
        this.saving = false;
      }
    },
    async saveTeam() {
      if (!this.draft.teamName.trim() || !this.draft.memberUids.length) {
        this.$Message.error(this.$t('approval-complete-team'));
        return;
      }
      const teams = this.config.teams.filter((t) => t.teamId !== this.editingId);
      teams.push({ ...this.draft, teamName: this.draft.teamName.trim() });
      await this.persist(teams);
    },
    removeTeam(team) {
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('approval-delete-team-confirm', [team.teamName]),
        className: 'dm-modal-destructive',
        onOk: () => this.persist(this.config.teams.filter((t) => t.teamId !== team.teamId))
      });
    }
  }
};
</script>
<style lang="less" scoped>
.approval-team-search {
  width: 280px;
  max-width: 100%;
  margin-right: 10px;
}
.approval-team-form :deep(.ivu-form-item) {
  margin-bottom: 16px;
}
.approval-team-form :deep(.ivu-form-item:last-child) {
  margin-bottom: 0;
}
.approval-team-modal-actions {
  display: flex;
  justify-content: center;
  gap: 8px;
}
.approval-team-modal-actions :deep(button) {
  margin: 0;
}
</style>
