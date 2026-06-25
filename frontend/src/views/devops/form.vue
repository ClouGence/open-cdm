<template>
  <div class="devops-form-page">
    <Spin v-if="loading" fix />
    <section class="devops-form-card">
      <Form ref="scmForm" class="devops-form" :model="scmForm" :rules="computedScmRules" label-position="top">
        <FormItem :label="$t('lei-xing')" prop="scmType">
          <div class="scm-type-list">
            <button
              v-for="item in visibleScmTypeList"
              :key="item.scmType"
              type="button"
              class="scm-type-card"
              :class="{ 'is-selected': item.scmType === selectedScmType.scmType, 'is-readonly': isEdit }"
              @click="handleChangeScmType(item)"
            >
              <CustomIcon v-if="item.iconResource" :resource="item.iconResource" :alt="item.scmTypeI18n" size="24px" />
              <span>{{ item.scmTypeI18n }}</span>
            </button>
          </div>
        </FormItem>

        <div class="devops-form-grid">
          <FormItem :label="$t('zhan-shi-ming-cheng')" prop="display">
            <Input v-model="scmForm.display" />
          </FormItem>
          <FormItem :label="$t('fu-wu-di-zhi')" prop="serviceUrl">
            <Input v-model="scmForm.serviceUrl" :disabled="selectedScmType?.custom === 'false'" />
          </FormItem>
          <FormItem :label="$t('accesstoken')" prop="accessToken" class="devops-form-grid__wide">
            <Input v-model="scmForm.accessToken" />
          </FormItem>
        </div>
      </Form>

      <div class="devops-form-help">
        <a v-if="selectedScmType && selectedScmType.helpUrl" @click="jumpToHelp">{{ $t('ru-he-huo-qu-accesstoken') }}</a>
        <span v-else></span>
        <div class="devops-test-result">
          <span v-show="isCorrect !== 'init'" :class="isCorrect ? 'green-text' : 'error-text'">
            {{ isCorrect ? $t('ce-shi-tong-guo') : $t('ce-shi-shi-bai') }}
          </span>
          <Button @click="handleTestScm" :loading="testLoading">{{ $t('ce-shi') }}</Button>
        </div>
      </div>

      <div class="devops-form-footer">
        <Button v-if="isEdit" type="error" @click="handleDeleteScm">{{ $t('shan-chu') }}</Button>
        <div class="devops-form-footer__right">
          <Button @click="goBack">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" :loading="submitLoading" @click="handleSubmit">{{ isEdit ? $t('bao-cun') : $t('tian-jia') }}</Button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
const EMPTY_SCM = {
  scmType: '',
  display: '',
  serviceUrl: '',
  accessToken: ''
};

export default {
  name: 'DevopsForm',
  data() {
    return {
      loading: false,
      submitLoading: false,
      testLoading: false,
      scmTypeList: [],
      selectedScmType: {},
      scmForm: { ...EMPTY_SCM },
      isCorrect: 'init',
      scmRules: {
        scmType: [
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
            message: '类型不能为空'
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
      }
    };
  },
  computed: {
    isEdit() {
      return this.$route.name === 'DMDevopsEdit';
    },
    scmId() {
      return this.$route.params.scmId;
    },
    computedScmRules() {
      return this.isEdit ? this.editScmRules : this.scmRules;
    },
    visibleScmTypeList() {
      return this.isEdit && this.selectedScmType?.scmType ? [this.selectedScmType] : this.scmTypeList;
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    async init() {
      this.loading = true;
      await this.getScmTypeList();
      if (this.isEdit) {
        await this.fetchScmDetail();
      }
      this.loading = false;
    },
    async getScmTypeList() {
      const res = await this.$services.dmDevopsScmDefList();
      if (res.success) {
        this.scmTypeList = res.data || [];
        if (!this.isEdit && this.scmTypeList.length) {
          this.handleChangeScmType(this.scmTypeList[0]);
        }
      }
    },
    async fetchScmDetail() {
      const res = await this.$services.dmDevopsScmList();
      if (!res.success) {
        return;
      }

      const scm = (res.data || []).find((item) => String(item.scmId) === String(this.scmId));
      if (!scm) {
        this.$Message.error(this.$t('zan-wu-shu-ju'));
        this.goBack();
        return;
      }

      this.scmForm = { ...scm };
      this.selectedScmType = this.scmTypeList.find((item) => item.scmType === scm.scmType) || {
        scmType: scm.scmType,
        scmTypeI18n: scm.scmTypeI18n,
        iconResource: ''
      };
    },
    handleChangeScmType(item) {
      if (this.isEdit) {
        return;
      }
      this.selectedScmType = item;
      this.scmForm.scmType = item.scmType;
      this.scmForm.serviceUrl = item.serviceUrl;
    },
    handleSubmit() {
      if (this.isEdit) {
        this.handleEditScm();
        return;
      }
      this.handleAddScm();
    },
    handleAddScm() {
      this.$refs.scmForm.validate(async (valid) => {
        if (!valid) {
          return;
        }
        this.submitLoading = true;
        const res = await this.$services.dmDevopsScmAdd({ data: this.scmForm });
        this.submitLoading = false;
        if (res.success) {
          this.$Message.success('提供者添加成功');
          this.goBack();
        }
      });
    },
    handleEditScm() {
      this.$refs.scmForm.validate(async (valid) => {
        if (!valid) {
          return;
        }
        this.submitLoading = true;
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
            const res2 = await this.$services.dmDevopsScmUpdate({
              data: {
                scmId: this.scmForm.scmId,
                newDisplay: this.scmForm.display,
                newServiceUrl: this.scmForm.serviceUrl,
                newAccessToken: this.scmForm.accessToken,
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
      });
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
            this.goBack();
          }
        }
      });
    },
    async handleTestScm() {
      this.testLoading = true;
      const testData = {
        ...this.scmForm,
        scmId: this.isEdit ? this.scmForm.scmId : null
      };
      const res = await this.$services.dmDevopsScmTest({ data: testData });
      this.testLoading = false;
      this.isCorrect = res.success;
      if (res.success) {
        this.$Message.success(this.$t('ce-shi-tong-guo'));
      }
    },
    jumpToHelp() {
      const url = this.selectedScmType?.helpUrl || '';
      if (url) {
        window.open(url, 'blank');
      }
    },
    goBack() {
      this.$router.push('/integrations/git');
    }
  }
};
</script>

<style lang="less" scoped>
.devops-form-page {
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  padding: 30px 36px 18px;
  background: #f6f9fc;
  overflow: auto;
}

.devops-form-card {
  box-sizing: border-box;
  min-height: 100%;
  padding: 20px 24px 22px;
  border: 1px solid #e3eaf2;
  border-radius: 10px;
  background: #fff;
  box-shadow: 0 10px 28px rgba(31, 41, 55, 0.04);
}

.devops-form {
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

.scm-type-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.scm-type-card {
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

.devops-form-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  gap: 12px;
  max-width: 720px;
}

.devops-form-grid__wide {
  grid-column: auto;
}

.devops-form-help {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  max-width: 720px;
  margin-top: 12px;
  padding-top: 18px;
  border-top: 1px solid #edf2f7;
}

.devops-test-result {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.devops-form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  max-width: 720px;
  margin-top: 28px;
  padding-top: 18px;
  border-top: 1px solid #edf2f7;
}

.devops-form-footer__right {
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
  .devops-form-page {
    padding: 12px;
  }

  .devops-form-card {
    padding: 18px;
  }

  .devops-form-grid {
    grid-template-columns: 1fr;
  }

  .devops-form-help,
  .devops-form-footer {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
