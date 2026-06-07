<template>
  <div class="devops">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('im') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option">
            <div class="left">
              <Input v-model="searchText" style="width: 280px; margin-right: 10px" clearable :placeholder="$t('shu-ru-ming-cheng-cha-zhao')" />
              <Button type="primary">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="handleImCreateDrawer" type="primary" ghost style="margin-right: 10px" icon="md-add">
                {{ $t('ti-gong-zhe') }}
              </Button>
              <Button @click="init" :loading="loading">
                <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="imColumns" :data="filteredImList" :loading="loading" :locale="{ emptyText: $t('zan-wu-shu-ju') }" size="small" border>
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
                  <a type="primary" @click="handleImEditDrawer(row)" style="margin-right: 10px">{{ $t('pei-zhi') }}</a>
                  <a type="primary" @click="handleImTest(row.imId)">{{ $t('ce-shi') }}</a>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
    </div>

    <Drawer
      :title="imEdit ? '编辑提供者' : '添加提供者'"
      width="400"
      class="drawer-wrap"
      v-model="imDialogDrawerShow"
      :mask-closable="false"
      @on-close="handleClose"
    >
      <div v-if="imDefList.length === 0">
        <span>{{ $t('zan-wu-shu-ju') }}</span>
      </div>
      <Form v-if="imDefList.length !== 0" :label-width="100" ref="imForm" :model="imForm" :rules="computedImRules">
        <div class="im-list">
          <div
            v-for="im in imEdit && imDefSelected ? [imDefSelected] : imDefList"
            :key="im.imType"
            @click="handleImDefOne(im)"
            :class="`im ${imDefSelected.imType === im.imType ? 'im-selected' : ''} ${imEdit ? 'im-read' : ''}`"
          >
            <CustomIcon v-if="im.iconResource" :resource="im.iconResource" :alt="im.imTypeI18n" size="24px" />
            <div>{{ im.imTypeI18n }}</div>
          </div>
        </div>
        <FormItem :label="$t('zhan-shi-ming-cheng')" prop="display">
          <Input v-model="imForm.display"></Input>
        </FormItem>
        <FormItem :label="$t('im-web-hook')" prop="webhookUrl">
          <Input v-model="imForm.webhookUrl" />
        </FormItem>
        <FormItem :label="$t('mi-yao')" prop="accessToken">
          <Input v-model="imForm.secret"></Input>
        </FormItem>
      </Form>
      <div class="bottom-wrap">
        <a @click="jumpToHelp">{{ $t('ru-he-huo-qu-accesstoken') }}</a>
        <div>
          <span v-show="isCorrect !== 'init'" :class="isCorrect ? 'green-text' : 'error-text'">
            {{ isCorrect ? $t('ce-shi-tong-guo') : $t('ce-shi-shi-bai') }}
          </span>
          <Button @click="handleImTest(null)" :loading="loading">{{ $t('ce-shi') }}</Button>
        </div>
      </div>
      <div class="drawer-footer">
        <div class="left">
          <Button type="error" @click.stop="handleImDelete" v-if="imEdit">{{ $t('shan-chu') }}</Button>
        </div>
        <div class="right">
          <Button @click="handleClose" style="margin-right: 10px">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" @click="handleImSave" v-if="imEdit">{{ $t('bao-cun') }}</Button>
          <Button type="primary" @click="handleImCreate" v-else>{{ $t('tian-jia') }}</Button>
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

export default {
  name: 'Devops',
  mixins: [copyMixin, enterOpPwdMixin, encryptMixin],
  data() {
    return {
      imEdit: false,
      searchText: '',
      isCorrect: 'init',
      imForm: {
        imId: 0,
        imType: '',
        display: '',
        webhookUrl: '',
        secret: ''
      },
      testImErrorMsg: '',
      imDialogDrawerShow: false,
      imDefList: [],
      imDefSelected: {
        imId: '',
        imType: 'none'
      },
      imRulesOfCreate: {
        scmType: [{ required: true, message: '提供方类型不能为空' }],
        display: [{ required: true, message: '展示名称不能为空' }],
        webhookUrl: [{ required: true, message: '服务地址不能为空' }]
      },
      imRulesOfEdit: {
        scmType: [{ required: true, message: '提供方类型不能为空' }],
        display: [{ required: true, message: '展示名称不能为空' }],
        webhookUrl: [{ required: true, message: '服务地址不能为空' }]
      },
      imList: [],
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
          width: 120
        }
      ]
    };
  },
  computed: {
    ...mapState(['userInfo', 'globalSetting', 'myCatLog', 'myAuth']),
    ...mapGetters(['isSaas']),
    computedImRules() {
      return this.imEdit ? this.imRulesOfEdit : this.imRulesOfCreate;
    },
    filteredImList() {
      const keyword = this.searchText.trim().toLowerCase();
      if (!keyword) return this.imList;
      return this.imList.filter((item) => item.display && item.display.toLowerCase().includes(keyword));
    }
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
    async getImList() {
      this.loading = true;
      const res = await this.$services.dmDevopsImList({ data: { imType: null } });
      this.loading = false;
      if (res.success) {
        this.imList = res.data;
      }
    },
    async fetchImDefList() {
      if (this.imDefList.length !== 0) return;
      const res = await this.$services.dmDevopsImDefList();
      this.imDefList = res.success ? res.data : [];
    },
    async handleImCreateDrawer() {
      await this.fetchImDefList();
      this.imDefSelected = this.imDefList[0];
      await this.handleImDefOne(this.imDefSelected);
      this.imEdit = false;
      this.imForm = {
        imId: 0,
        imType: this.imDefSelected.imType,
        display: '',
        webhookUrl: '',
        secret: ''
      };
      this.imDialogDrawerShow = true;
    },
    async handleImEditDrawer(row) {
      await this.fetchImDefList();
      this.imDefSelected = this.imDefList.find((def) => def.imType === row.imType);
      await this.handleImDefOne(this.imDefSelected);
      this.imEdit = true;
      this.imForm = {
        imId: row.imId,
        imType: row.imType,
        display: row.display,
        webhookUrl: row.webhookUrl,
        secret: ''
      };
      this.imDialogDrawerShow = true;
    },
    async handleImDefOne(imDef = {}) {
      if (this.imEdit) return;
      this.imDefSelected = imDef;
      this.imForm.imType = imDef.imType;
    },
    async handleImTest(configId) {
      if (configId) {
        const res = await this.$services.dmDevopsImTest({ data: { imId: configId } });
        if (res.success) {
          this.$Message.success(this.$t('ce-shi-tong-guo'));
        }
      } else {
        this.imForm.imId = null;
        const res = await this.$services.dmDevopsImTest({ data: this.imForm });
        this.isCorrect = res.success;
      }
    },
    async handleImCreate() {
      const valid = await this.$refs.imForm.validate();
      if (!valid) return;

      const res = await this.$services.dmDevopsImAdd({ data: this.imForm });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        await this.getImList();
        this.imDialogDrawerShow = false;
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    jumpToHelp() {
      const url = this.imDefSelected?.helpUrl || '';
      window.open(url, 'blank');
    },
    async handleImDelete() {
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('shi-fou-yao-shan-chu'),
        onOk: async () => {
          const res = await this.$services.dmDevopsImDelete({
            data: {
              imId: this.imForm.imId,
              force: false
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            await this.getImList();
            this.imDialogDrawerShow = false;
          } else {
            this.$Message.error(this.$t('cao-zuo-shi-bai'));
          }
        }
      });
    },
    async handleImSave() {
      const res = await this.$services.dmDevopsImUpdate({
        modal: false,
        data: {
          imId: this.imForm.imId,
          newDisplay: this.imForm.display,
          newWebhookUrl: this.imForm.webhookUrl,
          newSecret: this.imForm.secret,
          force: false
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        await this.getImList();
        this.imDialogDrawerShow = false;
      } else {
        this.$Modal.confirm({
          title: this.$t('cao-zuo-shi-bai'),
          content: res.msg,
          okText: this.$t('guan-bi'),
          cancelText: this.$t('hu-lve-bing-ji-xu'),
          onOk: async () => {},
          onCancel: async () => {
            const res2 = await this.$services.dmDevopsImUpdate({
              data: {
                imId: this.imForm.imId,
                newDisplay: this.imForm.display,
                newWebhookUrl: this.imForm.webhookUrl,
                newSecret: this.imForm.secret,
                force: true
              }
            });
            if (res2.success) {
              this.$Message.success(this.$t('cao-zuo-cheng-gong'));
              await this.getImList();
              this.imDialogDrawerShow = false;
            }
          }
        });
      }
    },
    handleClose() {
      this.imDialogDrawerShow = false;
      this.isCorrect = 'init';
      setTimeout(() => {
        this.$refs.imForm.resetFields();
      }, 100);
    }
  }
};
</script>

<style lang="less" scoped>
.im-wrap {
  display: flex;
  padding: 20px;
  margin-bottom: 50px;
}

.provider-cell {
  display: flex;
  align-items: center;
  gap: 6px;
}

.im-list {
  display: flex;
  padding: 20px;
  margin-bottom: 50px;

  .im {
    cursor: pointer;
    width: 80px;
    height: 80px;
    border: 1px solid #ccc;
    border-radius: 6px;
    margin-right: 5px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .im-read {
    cursor: default;
  }

  .im-selected {
    border: 2px solid #43cf7c;
  }
}

.im-select {
  padding: 20px 40px 20px 30px;
}

.project-base {
  width: 250px;
}

.img-wrap {
  min-width: 100px;
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
