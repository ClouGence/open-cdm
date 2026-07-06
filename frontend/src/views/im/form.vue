<template>
  <div class="im-form-page">
    <Spin v-if="loading" fix />
    <section class="im-form-card">
      <Form v-if="imDefList.length !== 0" ref="imForm" class="im-form" :model="imForm" :rules="computedImRules" label-position="top">
        <FormItem :label="$t('lei-xing')" prop="imType">
          <div class="im-type-list">
            <button
              v-for="item in visibleImDefList"
              :key="item.imType"
              type="button"
              class="im-type-card"
              :class="{ 'is-selected': item.imType === selectedImDef.imType, 'is-readonly': isEdit }"
              @click="handleChangeImType(item)"
            >
              <CustomIcon v-if="item.iconResource" :resource="item.iconResource" :alt="item.imTypeI18n" size="24px" />
              <span>{{ item.imTypeI18n }}</span>
            </button>
          </div>
        </FormItem>

        <div class="im-form-grid">
          <FormItem :label="$t('zhan-shi-ming-cheng')" prop="display">
            <Input v-model="imForm.display" />
          </FormItem>
          <FormItem :label="$t('im-web-hook')" prop="webhookUrl">
            <Input v-model="imForm.webhookUrl" />
          </FormItem>
          <FormItem :label="$t('mi-yao')" prop="secret">
            <Input v-model="imForm.secret" />
          </FormItem>
        </div>
      </Form>

      <div v-else-if="!loading" class="im-form-empty">{{ $t('zan-wu-shu-ju') }}</div>

      <template v-if="imDefList.length !== 0">
        <div class="im-form-help">
          <a v-if="selectedImDef && selectedImDef.helpUrl" @click="jumpToHelp">{{ $t('ru-he-pei-zhi-webhook') }}</a>
          <span v-else></span>
          <div class="im-test-result">
            <span v-show="isCorrect !== 'init'" :class="isCorrect ? 'green-text' : 'error-text'">
              {{ isCorrect ? $t('ce-shi-tong-guo') : $t('ce-shi-shi-bai') }}
            </span>
            <Button @click="handleImTest" :loading="testLoading">{{ $t('ce-shi') }}</Button>
          </div>
        </div>

        <div class="im-form-footer">
          <Button v-if="isEdit" type="error" @click.stop="handleImDelete">{{ $t('shan-chu') }}</Button>
          <div class="im-form-footer__right">
            <Button @click="goBack">{{ $t('qu-xiao') }}</Button>
            <Button type="primary" :loading="submitLoading" @click="handleSubmit">{{ isEdit ? $t('bao-cun') : $t('tian-jia') }}</Button>
          </div>
        </div>
      </template>
    </section>
  </div>
</template>

<script>
const EMPTY_IM = {
  imId: 0,
  imType: '',
  display: '',
  webhookUrl: '',
  secret: ''
};

export default {
  name: 'ImForm',
  data() {
    return {
      loading: false,
      submitLoading: false,
      testLoading: false,
      imDefList: [],
      selectedImDef: {},
      imForm: { ...EMPTY_IM },
      isCorrect: 'init',
      imRules: {
        imType: [
          {
            required: true,
            message: '类型不能为空'
          }
        ],
        display: [
          {
            required: true,
            message: '展示名称不能为空'
          }
        ],
        webhookUrl: [
          {
            required: true,
            message: '服务地址不能为空'
          }
        ]
      },
      editImRules: {
        imType: [
          {
            required: true,
            message: '类型不能为空'
          }
        ],
        display: [
          {
            required: true,
            message: '展示名称不能为空'
          }
        ],
        webhookUrl: [
          {
            required: true,
            message: '服务地址不能为空'
          }
        ]
      }
    };
  },
  computed: {
    isEdit() {
      return this.$route.name === 'DMImEdit';
    },
    imId() {
      return this.$route.params.imId;
    },
    computedImRules() {
      return this.isEdit ? this.editImRules : this.imRules;
    },
    visibleImDefList() {
      return this.isEdit && this.selectedImDef?.imType ? [this.selectedImDef] : this.imDefList;
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    async init() {
      this.loading = true;
      await this.fetchImDefList();
      if (this.isEdit) {
        await this.fetchImDetail();
      } else if (this.imDefList.length) {
        this.handleChangeImType(this.imDefList[0]);
      }
      this.loading = false;
    },
    async fetchImDefList() {
      const res = await this.$services.dmDevopsImDefList();
      this.imDefList = res.success ? res.data || [] : [];
    },
    async fetchImDetail() {
      const res = await this.$services.dmDevopsImList({ data: { imType: null } });
      if (!res.success) {
        return;
      }

      const im = (res.data || []).find((item) => String(item.imId) === String(this.imId));
      if (!im) {
        this.$Message.error(this.$t('zan-wu-shu-ju'));
        this.goBack();
        return;
      }

      this.imForm = {
        imId: im.imId,
        imType: im.imType,
        display: im.display,
        webhookUrl: im.webhookUrl,
        secret: ''
      };
      this.selectedImDef = this.imDefList.find((item) => item.imType === im.imType) || {
        imType: im.imType,
        imTypeI18n: im.imTypeI18n,
        iconResource: ''
      };
    },
    handleChangeImType(item) {
      if (this.isEdit) {
        return;
      }
      this.selectedImDef = item;
      this.imForm.imType = item.imType;
    },
    handleSubmit() {
      if (this.isEdit) {
        this.handleImSave();
        return;
      }
      this.handleImCreate();
    },
    async handleImCreate() {
      const valid = await this.$refs.imForm.validate();
      if (!valid) return;

      this.submitLoading = true;
      const res = await this.$services.dmDevopsImAdd({ data: this.imForm });
      this.submitLoading = false;

      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.goBack();
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleImSave() {
      const valid = await this.$refs.imForm.validate();
      if (!valid) return;

      this.submitLoading = true;
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
      this.submitLoading = false;

      if (res.success) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.goBack();
        return;
      }

      this.$Modal.confirm({
        title: this.$t('cao-zuo-shi-bai'),
        content: res.msg,
        okText: this.$t('guan-bi'),
        cancelText: this.$t('hu-lve-bing-ji-xu'),
        onOk: async () => {},
        onCancel: async () => {
          this.submitLoading = true;
          const res2 = await this.$services.dmDevopsImUpdate({
            data: {
              imId: this.imForm.imId,
              newDisplay: this.imForm.display,
              newWebhookUrl: this.imForm.webhookUrl,
              newSecret: this.imForm.secret,
              force: true
            }
          });
          this.submitLoading = false;

          if (res2.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            this.goBack();
          }
        }
      });
    },
    async handleImDelete() {
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('shi-fou-yao-shan-chu'),
        className: 'dm-modal-destructive',
        onOk: async () => {
          const res = await this.$services.dmDevopsImDelete({
            data: {
              imId: this.imForm.imId,
              force: false
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            this.goBack();
          } else {
            this.$Message.error(this.$t('cao-zuo-shi-bai'));
          }
        }
      });
    },
    async handleImTest() {
      this.testLoading = true;
      const testData = {
        ...this.imForm,
        imId: this.isEdit ? this.imForm.imId : null
      };
      const res = await this.$services.dmDevopsImTest({ data: testData });
      this.testLoading = false;
      this.isCorrect = res.success;
      if (res.success) {
        this.$Message.success(this.$t('ce-shi-tong-guo'));
      }
    },
    jumpToHelp() {
      const url = this.selectedImDef?.helpUrl || '';
      if (url) {
        window.open(url, 'blank');
      }
    },
    goBack() {
      this.$router.push('/integrations/im');
    }
  }
};
</script>

<style lang="less" scoped>
.im-form-page {
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  padding: 30px 36px 18px;
  overflow: auto;
}

.im-form-card {
  box-sizing: border-box;
  min-height: 100%;
  padding: 20px 24px 22px;
}

.im-form {
  padding-top: 0;

  :deep(.ivu-form-item-label) {
    display: inline-flex;
    align-items: center;
    min-height: 22px;
    padding: 0 0 8px;
    color: #5f6f87;
    font-size: 14px;
    font-weight: 600;
    line-height: 22px;
  }

  :deep(.ivu-form-item-required .ivu-form-item-label::before) {
    margin-right: 4px;
  }
}

.im-type-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.im-type-card {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-width: 112px;
  height: 48px;
  padding: 0 14px;
  border: 1px solid #d8e4ef;
  border-radius: 7px;
  color: #1f2937;
  font-size: 14px;
  font-weight: 700;
  background: #fff;
  cursor: pointer;
  transition:
    border-color 0.18s ease,
    background 0.18s ease,
    color 0.18s ease;

  &:hover {
    border-color: #13a86a;
    color: #0f9f55;
  }

  &.is-selected {
    border-color: #13a86a;
    background: #effbf5;
    color: #0f9f55;
  }

  &.is-readonly {
    cursor: default;
  }
}

.im-form-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  gap: 12px;
  max-width: 720px;
}

.im-form-empty {
  color: #667085;
  font-size: 14px;
}

.im-form-help {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  max-width: 720px;
  margin-top: 12px;
  padding-top: 18px;
  border-top: 1px solid #edf2f7;
}

.im-test-result {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.im-form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  max-width: 720px;
  margin-top: 28px;
  padding-top: 18px;
  border-top: 1px solid #edf2f7;
}

.im-form-footer__right {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.green-text {
  color: #0fac69;
}

.error-text {
  color: #ed4014;
}

@media (max-width: 900px) {
  .im-form-page {
    padding: 12px;
  }

  .im-form-grid {
    grid-template-columns: 1fr;
  }

  .im-form-help,
  .im-form-footer {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
