<template>
  <div class="certificate-input-field">
    <RadioGroup v-model="inputMode" type="button" :disabled="disabled" class="certificate-input-field__mode" @on-change="handleInputModeChange">
      <Radio :label="INPUT_MODE_TEXT">
        <Icon type="ios-create-outline" />
      </Radio>
      <Radio :label="INPUT_MODE_FILE">
        <Icon type="ios-document-outline" />
      </Radio>
    </RadioGroup>
    <Input
      :model-value="displayValue"
      readonly
      :disabled="disabled"
      class="certificate-input-field__value"
      @click="openDialog"
      @on-focus="openDialog"
    />
    <Modal v-model="dialogVisible" :title="field.titleI18N || field.field" width="640">
      <div class="certificate-input-field__dialog">
        <textarea
          v-if="inputMode === INPUT_MODE_TEXT"
          v-model="textValue"
          class="certificate-input-field__textarea"
          :placeholder="$t('qing-shu-ru-zheng-shu-huo-mi-yao-wan-zheng-nei-rong')"
          @input="clearError"
          @dragover.prevent
          @drop.prevent="handleTextDrop"
        ></textarea>
        <Upload
          v-else
          type="drag"
          action="#"
          :accept="acceptFormats"
          :before-upload="handleBeforeUpload"
          :show-upload-list="false"
          class="certificate-input-field__upload"
        >
          <div class="certificate-input-field__drop">
            <Icon type="ios-cloud-upload-outline" class="certificate-input-field__drop-icon" />
            <div class="certificate-input-field__drop-title">
              {{ selectedFileName || $t('dian-ji-zhe-li-shang-chuan-wen-jian') }}
            </div>
          </div>
        </Upload>
        <div class="certificate-input-field__tip">
          <div>{{ formatTip }}</div>
          <div>{{ limitTip }}</div>
        </div>
        <div v-if="errorMessage" class="certificate-input-field__error">
          <Icon type="ios-close-circle" />
          <span>{{ errorMessage }}</span>
        </div>
      </div>
      <template #footer>
        <Button @click="dialogVisible = false">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="saving" @click="confirmValue">{{ $t('que-ren') }}</Button>
      </template>
    </Modal>
  </div>
</template>

<script>
const CERTIFICATE_FORMATS = ['pem', 'key', 'crt', 'cer', 'pk8', 'p12', 'pfx', 'jks'];
const TEXT_MAX_SIZE = 1024 * 1024;
const BINARY_MAX_SIZE = 10 * 1024 * 1024;
const INPUT_MODE_TEXT = 'text';
const INPUT_MODE_FILE = 'file';

export default {
  name: 'CertificateInputField',
  props: {
    field: {
      type: Object,
      required: true
    },
    form: {
      type: Object,
      required: true
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      INPUT_MODE_TEXT,
      INPUT_MODE_FILE,
      dialogVisible: false,
      inputMode: INPUT_MODE_TEXT,
      textValue: '',
      selectedFile: null,
      configuredFileName: '',
      errorMessage: '',
      saving: false
    };
  },
  computed: {
    limitTip() {
      if (this.inputMode === INPUT_MODE_TEXT) {
        return this.$t('pem-key-crt-cer-jian-yi-bu-chao-guo-1mb');
      }
      return this.$t('pem-key-crt-cer-jian-yi-bu-chao-guo-1mb') + '；' + this.$t('p12-pfx-jks-jian-yi-bu-chao-guo-10mb');
    },
    formatTip() {
      if (this.inputMode === INPUT_MODE_TEXT) {
        return this.$t('zheng-shu-wen-ben-zhi-chi-ge-shi');
      }
      return this.$t('zheng-shu-wen-jian-zhi-chi-ge-shi');
    },
    acceptFormats() {
      return CERTIFICATE_FORMATS.map((format) => `.${format}`).join(',');
    },
    displayValue() {
      if (!this.form[this.field.field]) {
        return this.$t('zheng-shu-wei-pei-zhi');
      }
      return this.configuredFileName || this.$t('zheng-shu-yi-pei-zhi');
    },
    currentValue() {
      return this.form[this.field.field];
    },
    selectedFileName() {
      return this.selectedFile?.name || '';
    }
  },
  watch: {
    currentValue: {
      immediate: true,
      handler(value) {
        if (String(value || '').includes('://upload:')) {
          this.inputMode = INPUT_MODE_FILE;
        }
      }
    }
  },
  methods: {
    openDialog() {
      if (this.disabled) {
        return;
      }
      this.textValue = '';
      this.selectedFile = null;
      this.clearError();
      this.dialogVisible = true;
    },
    handleInputModeChange() {
      this.textValue = '';
      this.selectedFile = null;
      this.clearError();
    },
    handleBeforeUpload(file) {
      if (!file) {
        return false;
      }
      const format = this.resolveFileFormat(file.name);
      if (!format) {
        this.setError(this.formatTip);
        return false;
      }
      const maxSize = ['pk8', 'p12', 'pfx', 'jks'].includes(format) ? BINARY_MAX_SIZE : TEXT_MAX_SIZE;
      if (file.size > maxSize) {
        this.setError(this.limitTip);
        return false;
      }
      this.selectedFile = file;
      this.clearError();
      return false;
    },
    async handleTextDrop(event) {
      const file = event.dataTransfer?.files?.[0];
      if (!file) {
        return;
      }
      if (file.size > TEXT_MAX_SIZE) {
        this.setError(this.limitTip);
        return;
      }
      const bytes = new Uint8Array(await file.arrayBuffer());
      const text = this.decodeText(bytes);
      if (!text || !this.isTextContent(bytes, text)) {
        this.setError(this.$t('wen-ben-mo-shi-zhi-zhi-chi-wen-ben-zheng-shu-wen-jian'));
        return;
      }
      this.textValue = text;
      this.clearError();
    },
    async confirmValue() {
      if (this.inputMode === INPUT_MODE_TEXT) {
        if (!this.textValue) {
          this.setError(this.$t('qing-shu-ru-zheng-shu-huo-mi-yao-nei-rong'));
          return;
        }
        const bytes = new Uint8Array(await new Blob([this.textValue]).arrayBuffer());
        if (bytes.length > TEXT_MAX_SIZE) {
          this.setError(this.limitTip);
          return;
        }
        this.form[this.field.field] = `text://${this.toBase64(bytes)}`;
        this.configuredFileName = '';
        this.clearError();
        this.dialogVisible = false;
        return;
      }
      await this.uploadFile();
    },
    async uploadFile() {
      if (!this.selectedFile) {
        this.setError(this.$t('qing-xuan-ze-wen-jian'));
        return;
      }
      this.saving = true;
      try {
        const data = new FormData();
        data.append('file', this.selectedFile);
        const res = await this.$services.dmDataSourceUploadCertificate({
          data,
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        if (res.success && res.data?.fileId) {
          this.form[this.field.field] = `${res.data.format}://upload:${res.data.fileId}`;
          this.configuredFileName = res.data.fileName || this.selectedFile.name;
          this.clearError();
          this.dialogVisible = false;
        } else {
          this.setError(res.message || res.msg || this.$t('shang-chuan-shi-bai'));
        }
      } catch (e) {
        this.setError(e?.response?.data?.message || e?.response?.data?.msg || e?.message || this.$t('shang-chuan-shi-bai'));
      } finally {
        this.saving = false;
      }
    },
    resolveFileFormat(fileName) {
      const index = String(fileName || '').lastIndexOf('.');
      if (index < 0) {
        return '';
      }
      const format = fileName.substring(index + 1).toLowerCase();
      return CERTIFICATE_FORMATS.includes(format) ? format : '';
    },
    decodeText(bytes) {
      try {
        return new TextDecoder('utf-8', { fatal: true }).decode(bytes);
      } catch (e) {
        return '';
      }
    },
    isTextContent(bytes, text) {
      if (!text) {
        return false;
      }
      let controlChars = 0;
      for (const byte of bytes) {
        if (byte === 0) {
          return false;
        }
        if (byte < 32 && byte !== 9 && byte !== 10 && byte !== 13) {
          controlChars++;
        }
      }
      return controlChars / Math.max(bytes.length, 1) < 0.01;
    },
    toBase64(bytes) {
      let binary = '';
      const chunkSize = 8192;
      for (let i = 0; i < bytes.length; i += chunkSize) {
        binary += String.fromCharCode(...bytes.slice(i, i + chunkSize));
      }
      return btoa(binary);
    },
    setError(message) {
      this.errorMessage = message || '';
    },
    clearError() {
      this.errorMessage = '';
    }
  }
};
</script>

<style lang="less" scoped>
.certificate-input-field {
  display: inline-flex;
  align-items: center;
  width: 280px;
}

.certificate-input-field__mode {
  flex: none;
}

.certificate-input-field__mode :deep(.ivu-radio-wrapper) {
  width: 44px;
  height: 32px;
  line-height: 30px;
  padding: 0;
  text-align: center;
}

.certificate-input-field__mode :deep(.ivu-icon) {
  font-size: 20px;
  vertical-align: middle;
}

.certificate-input-field__value {
  flex: 1;
  width: 1px;
  margin-left: 8px;
  cursor: pointer;
}

.certificate-input-field__dialog {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.certificate-input-field__upload {
  width: 100%;
}

.certificate-input-field__textarea {
  width: 100%;
  min-height: 220px;
  padding: 6px 7px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  background: #fff;
  line-height: 1.5;
  resize: vertical;
  outline: none;
  transition: border-color 0.2s ease;
}

.certificate-input-field__textarea:focus {
  border-color: #57a3f3;
}

.certificate-input-field__drop {
  padding: 28px 16px;
  text-align: center;
}

.certificate-input-field__drop-icon {
  color: #2d8cf0;
  font-size: 36px;
}

.certificate-input-field__drop-title {
  margin-top: 8px;
  color: #515a6e;
}

.certificate-input-field__tip {
  color: #808695;
  font-size: 12px;
}

.certificate-input-field__error {
  display: flex;
  align-items: flex-start;
  gap: 6px;
  padding: 8px 10px;
  border: 1px solid #ffd5d5;
  border-radius: 4px;
  color: #ed4014;
  background: #fff5f5;
  line-height: 1.5;
}

.certificate-input-field__error .ivu-icon {
  margin-top: 2px;
  font-size: 16px;
}
</style>
