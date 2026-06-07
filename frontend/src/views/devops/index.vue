<template>
  <div class="devops">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('ci-cd') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
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
              <Button type="primary" @click="handleQuery">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="handleAdd" type="primary" ghost style="margin-right: 10px" icon="md-add">
                {{ $t('ti-gong-zhe') }}
              </Button>
              <Button @click="init" :loading="loading">
                <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
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
                  <a type="primary" @click="handleShowEditScmDrawer(row)" style="margin-right: 10px">{{ $t('pei-zhi') }}</a>
                  <a type="primary" @click="handleTestScm(row.scmId)">{{ $t('ce-shi') }}</a>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
    </div>
    <Drawer
      :title="scmEdit ? $t('bian-ji-ti-gong-zhe') : $t('tian-jia-ti-gong-zhe')"
      width="400"
      class="drawer-wrap"
      v-model="showDrawer"
      :mask-closable="false"
      @on-close="handleClose"
    >
      <Form :label-width="100" ref="scmForm" :model="scmForm" :rules="computedScmRules">
        <div class="scm-wrap">
          <div
            :class="`${scmEdit ? 'scm-item-read' : 'scm-item-editor'} ${item.scmType === selectedScmType.scmType ? 'scm-item-selected' : ''}`"
            v-for="item in scmEdit && selectedScmType ? [selectedScmType] : scmTypeList"
            :key="item.scmType"
            @click="handleChangeScmType(item)"
          >
            <CustomIcon v-if="item.iconResource" :resource="item.iconResource" :alt="item.scmTypeI18n" size="24px" />
            <div>{{ item.scmTypeI18n }}</div>
          </div>
        </div>
        <FormItem :label="$t('zhan-shi-ming-cheng')" prop="display">
          <Input v-model="scmForm.display"></Input>
        </FormItem>
        <FormItem :label="$t('fu-wu-di-zhi')" prop="serviceUrl">
          <Input v-model="scmForm.serviceUrl" :disabled="selectedScmType?.custom === 'false'"></Input>
        </FormItem>
        <FormItem :label="$t('accesstoken')" prop="accessToken">
          <Input v-model="scmForm.accessToken"></Input>
        </FormItem>
      </Form>
      <div class="bottom-wrap">
        <a @click="jumpToHelp">{{ $t('ru-he-huo-qu-accesstoken') }}</a>
        <div>
          <span v-show="isCorrect !== 'init'" :class="isCorrect ? 'green-text' : 'error-text'">
            {{ isCorrect ? $t('ce-shi-tong-guo') : $t('ce-shi-shi-bai') }}
          </span>
          <Button @click="handleTestScm(null)" :loading="loading">{{ $t('ce-shi') }}</Button>
        </div>
      </div>

      <div class="drawer-footer">
        <div class="left">
          <Button type="error" @click="handleDeleteScm" v-if="scmEdit">{{ $t('shan-chu') }}</Button>
        </div>
        <div class="right">
          <Button @click="handleClose" style="margin-right: 10px">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" @click="handleEditScm" v-if="scmEdit">{{ $t('bao-cun') }}</Button>
          <Button type="primary" @click="handleAddScm" v-else>{{ $t('tian-jia') }}</Button>
        </div>
      </div>
    </Drawer>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import { encryptMixin } from '@/mixins/encryptMixin';
import { scmColumns } from './constant';

const EMPTY_SCM = {
  scmType: '',
  display: '',
  serviceUrl: '',
  accessToken: ''
};

export default {
  name: 'Devops',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  data() {
    return {
      scmEdit: false,
      selectedScmType: {},
      scmList: [],
      rawScmList: [], // 用于前端搜索过滤
      adminList: [],
      scmTypeList: [],
      searchKeywords: '',
      step: -1,
      showDrawer: false,
      loading: false,
      scmForm: { ...EMPTY_SCM },
      isCorrect: 'init',
      scmRules: {
        scmType: [
          {
            required: true,
            message: '提供方类型不能为空'
          }
        ],
        display: [
          {
            required: true,
            message: '展示名称不能为空'
          }
        ],
        serviceUrl: [
          {
            required: true,
            message: '服务地址不能为空'
          }
        ]
      },
      editScmRules: {
        scmType: [
          {
            required: true,
            message: '提供方类型不能为空'
          }
        ],
        display: [
          {
            required: true,
            message: '展示名称不能为空'
          }
        ],
        serviceUrl: [
          {
            required: true,
            message: '服务地址不能为空'
          }
        ]
      },
      scmColumns
    };
  },
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['isSaas']),
    computedScmRules() {
      return this.scmEdit ? this.editScmRules : this.scmRules;
    }
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
    handleChangeScmType(item) {
      if (this.scmEdit) return;
      this.selectedScmType = item;
      this.scmForm.scmType = item.scmType;
      this.scmForm.serviceUrl = item.serviceUrl;
    },
    handleAddScm() {
      this.$refs.scmForm.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.dmDevopsScmAdd({ data: this.scmForm });
          if (res.success) {
            this.$Message.success('提供者添加成功');
            this.handleClose();
            this.init();
          }
        }
      });
    },
    handleQuery() {
      const keyword = this.searchKeywords.trim().toLowerCase();
      this.scmList = this.rawScmList.filter((item) => item.display.toLowerCase().includes(keyword));
    },
    handleQueryClear() {
      this.searchKeywords = '';
      this.scmList = [...this.rawScmList];
    },
    handleEditScm() {
      this.$refs.scmForm.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.dmDevopsScmUpdate({
            modal: false,
            data: {
              scmId: this.scmForm.scmId,
              newDisplay: this.scmForm.display,
              newServiceUrl: this.scmForm.serviceUrl,
              newAccessToken: this.scmForm.accessToken,
              force: false
            }
          });

          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            this.handleClose();
            await this.getScmList();
          } else {
            this.$Modal.confirm({
              title: this.$t('cao-zuo-shi-bai'),
              content: res.msg,
              okText: this.$t('guan-bi'),
              cancelText: this.$t('hu-lve-bing-ji-xu'),
              onOk: async () => {},
              onCancel: async () => {
                const res2 = await this.$services.dmDevopsScmUpdate({
                  data: {
                    scmId: this.scmForm.scmId,
                    newDisplay: this.scmForm.display,
                    newServiceUrl: this.scmForm.serviceUrl,
                    newAccessToken: this.scmForm.accessToken,
                    force: true
                  }
                });

                if (res2.success) {
                  this.$Message.success(this.$t('cao-zuo-cheng-gong'));
                  this.handleClose();
                  this.getScmList();
                }
              }
            });
          }
        }
      });
    },
    jumpToHelp() {
      const url = this.selectedScmType?.helpUrl || '';
      window.open(url, 'blank');
    },
    handleDeleteScm() {
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('shi-fou-yao-shan-chu'),
        onOk: async () => {
          const res = await this.$services.dmDevopsScmDelete({
            data: {
              scmId: this.scmForm.scmId,
              force: false
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('shan-chu-cheng-gong-0'));
            this.handleClose();
            await this.getScmList();
          }
        }
      });
    },
    async handleTestScm(configId) {
      if (configId) {
        const res = await this.$services.dmDevopsScmTest({ data: { scmId: configId } });
        if (res.success) {
          this.$Message.success(this.$t('ce-shi-tong-guo'));
        }
      } else {
        this.loading = true;
        if (!this.scmEdit) this.scmForm.scmId = null;
        const res = await this.$services.dmDevopsScmTest({ data: this.scmForm });
        this.loading = false;
        this.isCorrect = res.success;
      }
    },
    async getScmTypeList() {
      this.loading = true;
      const res = await this.$services.dmDevopsScmDefList();
      this.loading = false;

      if (res.success) {
        this.scmTypeList = res.data;
        if (this.scmTypeList?.length) {
          this.selectedScmType = this.scmTypeList[0];
          this.scmForm.scmType = this.scmTypeList[0].scmType;
          this.scmForm.serviceUrl = this.scmTypeList[0].serviceUrl;
        }
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
    handleAdd() {
      this.showDrawer = true;
    },
    handleClose() {
      this.showDrawer = false;
      this.scmEdit = false;
      this.isCorrect = 'init';
      setTimeout(() => {
        this.$refs.scmForm.resetFields();
      }, 100);
    },
    goDetail(row) {
      this.$router.push(`/project/${row?.id || 1}`);
    },
    nextStep() {},
    confirmGuid() {
      this.nextStep();
    },
    createEmptyProject() {
      this.showAddModal = false;
    },
    handleShowEditScmDrawer(scm) {
      this.scmEdit = true;
      this.selectedScmType = this.scmTypeList.find((item) => item.scmType === scm?.scmType);
      this.scmForm = { ...scm };
      this.selectedScmType.scmType = scm?.scmType;
      this.showDrawer = true;
    },
    handleCloseModal() {}
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
