<template>
  <div class="devops">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option">
            <div class="left">
              <Input
                v-model="searchText"
                style="width: 280px; margin-right: 10px"
                clearable
                :placeholder="$t('shu-ru-ming-cheng-cha-zhao')"
                @on-enter="handleQuery"
                @on-clear="handleQueryClear"
              />
              <Button type="primary" ghost @click="handleQuery">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="goCreateIm" type="primary" style="margin-right: 10px" icon="md-add">
                {{ $t('xin-zeng') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="imColumns" :data="imList" :loading="loading" :locale="{ emptyText: $t('zan-wu-shu-ju') }" size="small" border>
              <template #provider="{ row }">
                <div class="provider-cell">
                  <CustomIcon
                    v-if="providerIconResource(row.imType)"
                    :resource="providerIconResource(row.imType)"
                    :alt="row.imTypeI18n"
                    size="20px"
                  />
                  <span>{{ row.imTypeI18n }}</span>
                </div>
              </template>
              <template #action="{ row }">
                <div class="action">
                  <a type="primary" @click="goEditIm(row)">{{ $t('bian-ji') }}</a>
                  <a type="primary" @click="handleImTest(row.imId)">{{ $t('ce-shi') }}</a>
                  <a @click="handleImDelete(row)">{{ $t('shan-chu') }}</a>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImConfig',
  data() {
    return {
      searchText: '',
      imList: [],
      rawImList: [],
      imDefList: [],
      loading: false,
      imColumns: [
        {
          title: this.$t('ti-gong-zhe'),
          slot: 'provider',
          width: 120
        },
        {
          title: this.$t('zhan-shi-ming-cheng'),
          key: 'display',
          minWidth: 200
        },
        {
          title: this.$t('fu-wu-di-zhi'),
          key: 'webhookUrl',
          minWidth: 600
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          fixed: 'right',
          width: 160
        }
      ]
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.fetchImDefList();
      this.getImList();
    },
    providerIconResource(imType) {
      return this.imDefList.find((item) => item.imType === imType)?.iconResource || '';
    },
    handleQuery() {
      const keyword = this.searchText.trim().toLowerCase();
      if (!keyword) {
        this.imList = [...this.rawImList];
        return;
      }

      this.imList = this.rawImList.filter((item) => (item.display || '').toLowerCase().includes(keyword));
    },
    handleQueryClear() {
      this.searchText = '';
      this.imList = [...this.rawImList];
    },
    async getImList() {
      this.loading = true;
      const res = await this.$services.dmDevopsImList({ data: { imType: null } });
      this.loading = false;

      if (res.success) {
        this.rawImList = res.data || [];
        this.handleQuery();
      }
    },
    async fetchImDefList() {
      if (this.imDefList.length !== 0) return;
      const res = await this.$services.dmDevopsImDefList();
      this.imDefList = res.success ? res.data || [] : [];
    },
    async handleImTest(imId) {
      const res = await this.$services.dmDevopsImTest({ data: { imId } });
      if (res.success) {
        this.$Message.success(this.$t('ce-shi-tong-guo'));
      }
    },
    goCreateIm() {
      this.$router.push('/integrations/im/create');
    },
    goEditIm(row) {
      this.$router.push(`/integrations/im/${row.imId}/edit`);
    },
    handleImDelete(row) {
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('shi-fou-yao-shan-chu'),
        className: 'dm-modal-destructive',
        onOk: async () => {
          const res = await this.$services.dmDevopsImDelete({
            data: {
              imId: row.imId,
              force: false
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            this.getImList();
          } else {
            this.$Message.error(this.$t('cao-zuo-shi-bai'));
          }
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
.provider-cell {
  display: flex;
  align-items: center;
  gap: 6px;
}

.action {
  display: inline-flex;
  align-items: center;
  gap: 12px;

  a:hover {
    border-bottom: none;
    box-shadow: inset 0 -1px 0 currentColor;
  }
}
</style>
