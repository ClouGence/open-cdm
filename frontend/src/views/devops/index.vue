<template>
  <div class="devops">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option">
            <div class="left">
              <Input
                style="width: 280px; margin-right: 10px"
                :placeholder="$t('shu-ru-ming-cheng-cha-zhao')"
                v-model="searchKeywords"
                @on-enter="handleQuery"
                @on-clear="handleQueryClear"
                clearable
              />
              <Button type="primary" ghost @click="handleQuery">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="goCreateScm" type="primary" style="margin-right: 10px" icon="md-add">
                {{ $t('xin-zeng') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="scmColumns" :data="scmList" :loading="loading" :locale="{ emptyText: $t('zan-wu-shu-ju') }" size="small" border>
              <template #provider="{ row }">
                <div class="provider-cell">
                  <CustomIcon
                    v-if="providerIconResource(row.scmType)"
                    :resource="providerIconResource(row.scmType)"
                    :alt="row.scmTypeI18n"
                    size="20px"
                  />
                  <span>{{ row.scmTypeI18n }}</span>
                </div>
              </template>
              <template #action="{ row }">
                <div class="action">
                  <a type="primary" @click="goEditScm(row)" style="margin-right: 10px">{{ $t('bian-ji') }}</a>
                  <a type="primary" @click="handleTestScm(row.scmId)">{{ $t('ce-shi') }}</a>
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
import { scmColumns } from './constant';

export default {
  name: 'Devops',
  data() {
    return {
      scmList: [],
      rawScmList: [], // For front-end search filter
      scmTypeList: [],
      searchKeywords: '',
      loading: false,
      scmColumns
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.getScmList();
      this.getScmTypeList();
    },
    providerIconResource(scmType) {
      return this.scmTypeList.find((item) => item.scmType === scmType)?.iconResource || '';
    },
    handleQuery() {
      const keyword = this.searchKeywords.trim().toLowerCase();
      this.scmList = this.rawScmList.filter((item) => item.display.toLowerCase().includes(keyword));
    },
    handleQueryClear() {
      this.searchKeywords = '';
      this.scmList = [...this.rawScmList];
    },
    async handleTestScm(configId) {
      const res = await this.$services.dmDevopsScmTest({ data: { scmId: configId } });
      if (res.success) {
        this.$Message.success(this.$t('ce-shi-tong-guo'));
      }
    },
    async getScmTypeList() {
      this.loading = true;
      const res = await this.$services.dmDevopsScmDefList();
      this.loading = false;

      if (res.success) {
        this.scmTypeList = res.data || [];
      }
    },
    async getScmList() {
      this.loading = true;
      const res = await this.$services.dmDevopsScmList();
      this.loading = false;

      if (res.success) {
        this.rawScmList = res.data;
        this.scmList = res.data;
      }
    },
    goCreateScm() {
      this.$router.push('/integrations/git/create');
    },
    goEditScm(row) {
      this.$router.push(`/integrations/git/${row.scmId}/edit`);
    }
  }
};
</script>

<style lang="less" scoped>
.sub-account {
  display: flex;
  flex-direction: column;
  height: 100%;

  .uid {
    display: flex;
    cursor: pointer;

    .copy {
      display: none;
    }

    &:hover {
      .copy {
        display: block;
      }
    }
  }

  .copy-account {
    display: flex;
    align-items: center;
    cursor: pointer;

    .square {
      width: 15px;
      height: 12px;
    }

    i {
      display: none;
    }

    &:hover {
      i {
        display: block;
      }

      .square {
        display: none;
      }
    }
  }

  .action {
    //button {
    //  margin-right: 12px;
    //}
    //.ivu-dropdown {
    //  padding: 0 7px;
    //}
    a {
      margin-right: 16px;
    }
  }

  .actions {
    font-size: 12px;
  }
}

.devops .action a:hover {
  border-bottom: none;
  box-shadow: inset 0 -1px 0 currentColor;
}

.provider-cell {
  display: flex;
  align-items: center;
  gap: 6px;
}

.manage-role-modal {
  display: flex;

  .left {
    .title {
      margin-bottom: 10px;

      span {
        color: #888;

        &:first-child {
          color: #333;
          font-weight: bold;
          margin-right: 10px;
        }
      }
    }

    .role-table {
      display: flex;
      flex-direction: column;
      height: 400px;
      border: 1px solid rgba(234, 234, 234, 1);
    }
  }

  .new-role {
    flex: 1;
    padding: 20px;
  }
}

.new-subaccount-modal {
  .ivu-input-wrapper {
    width: 420px;
  }

  .title {
    font-weight: bold;
    font-size: 14px;
    margin-bottom: 18px;
  }
}

.rule-default-tag {
  display: flex;
  align-items: center;
}

.role-name-container {
  display: flex;
  align-items: center;
}

.project-base {
  width: 250px;
}

.img-wrap {
  min-width: 100px;
}

.step-one {
  display: flex;
}

.step-two-title {
  font-weight: bold;
}

.step-two {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .btn-wrap {
    display: flex;
    width: 350px;
    justify-content: space-between;
  }

  .btn-wrap > div {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    width: 160px;
    text-align: center;
    padding: 15px;
    border: 1px solid #e8e8e8;
    border-radius: 10px;

    div:nth-child(1) {
      font-size: 16px;
    }

    div:nth-child(2) {
      font-size: 10px;
      color: #333;
    }
  }

  .btn-wrap > div:hover {
    border-color: #54b6f2;
    cursor: pointer;
  }
}

.scm-wrap {
  display: flex;
  padding: 20px;
  margin-bottom: 50px;
}

.scm-item-read,
.scm-item-editor {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 80px;
  height: 80px;
  border-radius: 10px;
  border: 1px solid #ccc;
  padding: 10px;
  margin-right: 10px;
  cursor: default;

  &.scm-item-selected {
    border: 2px solid #00bb00;
  }
}

.scm-item-editor {
  cursor: pointer;
}

.drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;

  .left {
    display: flex;
    align-items: center;
    float: left;
  }

  .right {
    display: flex;
    align-items: center;
    float: right;
  }
}

.drawer-wrap {
  position: relative;

  :deep(.ivu-drawer-content) {
    padding-top: 0;
  }

  :deep(.ivu-divider-inner-text) {
    display: flex;
    align-items: flex-end;
    color: #636363;
  }

  :deep(.ivu-input-prefix),
  .ivu-select-prefix {
    display: flex;
    justify-content: center;
  }
}

.bottom-wrap {
  display: flex;
  justify-content: space-between;
}

.green-text {
  color: #00bb00;
  margin-right: 5px;
}

.error-text {
  color: red;
  margin-right: 5px;
}
</style>
