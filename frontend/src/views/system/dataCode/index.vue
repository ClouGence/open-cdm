<template>
  <div>
    <query-header
      :handle-click-add-btn="handleShowUploadCode"
      :handle-query="handleRefresh"
      :handle-refresh="handleRefresh"
      :query="queryForm"
      :show-add-btn="userInfo.authArr.includes('DS_ENV_ADD')"
      :showSelect="false"
      :add-btn-text="$t('shang-chuan-dai-ma-bao')"
      :placeholder="$t('qing-shu-ru-guan-jian-ci-cha-xun')"
    />
    <a-table border size="small" :columns="dataCodeColumns" :data-source="dataCodeData">
      <template #dataCodeTitle="{ record }">
        <span>{{ record.description }}</span>
        <cc-iconfont
          v-if="userInfo.authArr.includes('WORKER_UPDATE_DESC')"
          :size="8"
          name="edit"
          style="margin-left: 4px"
          @click="handleClickEditDataCodeTitleBtn(record)"
        />
      </template>
      <template #action="{ record }">
        <a-button type="link" @click="handleShowUpdateCode(record)">
          {{ $t('geng-xin-dai-ma-bao') }}
        </a-button>
        <a @click="handleDownloadPkg(record)">{{ $t('xia-zai-dai-ma-bao') }}</a>
        <a-popconfirm
          v-if="userInfo.authArr.includes('SYSTEM_ROLE_DELETE')"
          :cancel-text="$t('qu-xiao')"
          :ok-text="$t('que-ren')"
          :title="$t('que-ding-shan-chu-gai-jiao-se-ma')"
          @confirm="handleDeleteDataCode(record)"
        >
          <a-button type="link">{{ $t('shan-chu') }}</a-button>
        </a-popconfirm>
      </template>
    </a-table>
    <a-modal :key="index" :visible="showUploadCode" :title="isUpdate ? $t('geng-xin-dai-ma-bao') : $t('shang-chuan-dai-ma-bao')">
      <div>
        <div>
          <a-form-model ref="dataCodeForm" :model="dataCode" :label-col="{ span: 7 }" :wrapper-col="{ span: 17 }" :rules="formValidate">
            <a-form-model-item :label="$t('shu-ju-chu-li-dai-ma-bao-ming-cheng')" prop="description">
              <p v-if="isUpdate">
                {{ currentRecord.description }}
              </p>
              <a-input v-if="!isUpdate" v-model="dataCode.description" />
            </a-form-model-item>
            <a-form-model-item :label="$t('dai-ma-bao')" prop="file">
              <input @change="handleFileChange" type="file" name="uploadfile" id="uploadfile1" />
            </a-form-model-item>
          </a-form-model>
        </div>
        <div class="footer">
          <a-button type="primary" :loading="confirmLoading" @click="handleUpdateCode">
            {{ $t('que-ren') }}
          </a-button>
          <a-button type="default" @click="handleCancel">{{ $t('qu-xiao') }}</a-button>
        </div>
      </div>
    </a-modal>
    <a-modal
      v-model="showEditTitle"
      :mask-closable="false"
      :width="400"
      :cancelText="$t('qu-xiao')"
      :okText="$t('bao-cun')"
      :title="$t('xiu-gai-shu-ju-chu-li-dai-ma-bao-ming-cheng')"
      wrapClassName="have-footer"
      @ok="handleUpdateTitle"
    >
      <a-input v-model="currentRecord.description" />
    </a-modal>
  </div>
</template>

<script>
import QueryHeader from '@/views/system/components/QueryHeader';
import { mapState } from 'vuex';
import deepClone from 'lodash.clonedeep';

export default {
  name: 'DataCode',
  components: {
    QueryHeader
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo
    })
  },
  mounted() {
    this.handleRefresh();
  },
  data() {
    return {
      index: 0,
      queryForm: { text: '' },
      showUploadCode: false,
      isUpdate: false,
      showEditTitle: false,
      confirmLoading: false,
      currentRecord: {},
      dataCode: {
        description: '',
        file: ''
      },
      dataCodeColumns: [
        {
          title: this.$t('shu-ju-chu-li-dai-ma-bao-id'),
          key: 'pkgInstanceName',
          dataIndex: 'pkgInstanceName'
        },
        {
          title: this.$t('shu-ju-chu-li-dai-ma-bao-ming-cheng'),
          key: 'dataCodeTitle',
          slots: { title: 'dataCodeTitle' },
          scopedSlots: { customRender: 'dataCodeTitle' }
        },
        {
          title: this.$t('chuang-jian-shi-jian'),
          key: 'gmtCreate',
          dataIndex: 'gmtCreate'
        },
        {
          title: this.$t('geng-xin-shi-jian'),
          key: 'gmtModified',
          dataIndex: 'gmtModified'
        },
        {
          title: this.$t('cao-zuo'),
          key: '',
          slots: { title: 'action' },
          scopedSlots: { customRender: 'action' }
        }
      ],
      dataCodeData: [
        {
          id: 1,
          title: 'test',
          createTime: '2021-05-01 12:00:00',
          updateTime: '2021-05-01 12:00:00'
        }
      ],
      formValidate: {
        description: [
          {
            required: true,
            message: this.$t('dai-ma-bao-ming-cheng-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        file: [
          {
            required: true,
            validator: (rule, value, callback) => {
              if (this.dataCode.file) {
                callback();
              } else {
                return callback(new Error(this.$t('qing-xuan-ze-xu-yao-shang-chuan-de-dai-ma-bao')));
              }
              callback();
            },
            trigger: 'change'
          }
        ]
      }
    };
  },
  methods: {
    handleRefresh() {
      this.listPkg();
    },
    async listPkg() {
      const data = {
        descLike: this.queryForm.text,
        startId: 0,
        pageSize: 100
      };
      const res = await this.$services.dmDataHandlePackageListPkg({ data });
      if (res.success) {
        this.dataCodeData = res.data;
      }
    },
    async deletePkg(pkgId) {
      const data = {
        pkgId
      };
      const res = await this.$services.dmDataHandlePackageDeletePkgAndBind({ data });
      if (res.success) {
        this.handleRefresh();
      }
    },
    handleShowUploadCode() {
      this.isUpdate = false;
      this.dataCode = {
        description: '',
        code: '',
        file: ''
      };
      this.index++;
      this.showUploadCode = true;
    },
    handleUpdateCode() {
      this.confirmLoading = true;
      this.updatePkg();
    },
    async updatePkg() {
      const formData = new FormData();
      let res = null;
      if (this.isUpdate) {
        formData.append('pkgId', this.dataCode.id);
        formData.append('packageFile', this.dataCode.file);
        res = await this.$services.dmDataHandlePackageUpdatePkg({
          data: formData,
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        });
      } else {
        formData.append('description', this.dataCode.description);
        formData.append('packageFile', this.dataCode.file);
        res = await this.$services.dmDataHandlePackageUpload({
          data: formData,
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        });
      }
      if (res.success) {
        this.showUploadCode = false;
        this.handleRefresh();
      }
      this.confirmLoading = false;
    },
    handleCancel() {
      this.showUploadCode = false;
    },
    handleFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024 * 1024) {
          return false;
        }
        this.dataCode.file = file;
      }
    },
    handleShowUpdateCode(record) {
      this.currentRecord = deepClone(record);
      this.dataCode = record;
      this.isUpdate = true;
      this.showUploadCode = true;
    },
    handleDeleteDataCode(record) {
      this.deletePkg(record.id);
    },
    handleClickEditDataCodeTitleBtn(record) {
      this.currentRecord = deepClone(record);
      this.showEditTitle = true;
    },
    handleUpdateTitle() {
      this.editPkg();
    },
    async editPkg() {
      const data = {
        pkgId: this.currentRecord.id,
        description: this.currentRecord.description
      };
      const res = await this.$services.dmDataHandlePackageUpdateInfo({ data });
      if (res.success) {
        this.handleRefresh();
        this.showEditTitle = false;
      }
    },
    handleDownloadPkg(record) {
      this.downloadPkg(record);
    },
    async downloadPkg(record) {
      const data = {
        pkgId: record.id
      };
      const res = await this.$services.dmDataHandlePackageDownload({
        data,
        responseType: 'blob',
        noStatus: true
      });
      const a = document.createElement('a');
      const url = window.URL.createObjectURL(res);
      const filename = record.fileName;
      a.href = url;
      a.download = filename;
      a.click();
      window.URL.revokeObjectURL(url);
    }
  }
};
</script>

<style scoped lang="less"></style>
