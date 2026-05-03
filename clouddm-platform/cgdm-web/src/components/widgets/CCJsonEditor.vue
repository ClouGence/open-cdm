<template>
  <CCModal v-model="visible" :title="modalTitle" :width="width" @on-cancel="handleCancel">
    <div style="position: relative">
      <!-- 参数信息头部 -->
      <div v-if="showHeader" class="param-header">
        <p class="param-title">
          <Icon type="ios-settings" class="param-icon" />
          {{ $t('can-shu-ming-cheng') }}: {{ paramName }}
        </p>
        <p v-if="paramDescription" class="param-desc">{{ $t('can-shu-miao-shu') }}: {{ paramDescription }}</p>
      </div>

      <!-- JSON编辑器主体 -->
      <div class="json-editor-container">
        <!-- 工具栏 -->
        <div class="json-editor-toolbar">
          <div style="display: flex; align-items: center">
            <Icon type="ios-code" class="toolbar-icon" />
          </div>
          <div>
            <Button size="small" @click="formatJson" :disabled="!jsonValue || !isJSON(jsonValue)" style="margin-right: 8px">
              <Icon type="ios-code" style="margin-right: 4px" />
              {{ $t('ge-shi-hua') }}
            </Button>
            <Button size="small" @click="resetJson" :disabled="!originalValue">
              <Icon type="ios-refresh" style="margin-right: 4px" />
              {{ $t('chong-zhi') }}
            </Button>
          </div>
        </div>

        <!-- 编辑区域 -->
        <div class="json-editor-content">
          <Input v-model="jsonValue" type="textarea" :rows="rows" :placeholder="placeholderText" class="json-textarea" />
        </div>
      </div>
    </div>

    <!-- 底部操作按钮 -->
    <template #footer>
      <div style="display: flex; justify-content: space-between; align-items: center; padding: 12px 0">
        <div>
          <Button @click="handleCancel" style="margin-right: 8px">
            {{ $t('qu-xiao') }}
          </Button>
          <Button type="primary" @click="handleConfirm">
            {{ $t('que-ding') }}
          </Button>
        </div>
      </div>
    </template>
  </CCModal>
</template>

<script>
import * as JSONB from '@jsonjoy.com/json-pack/lib/json-binary/index.js';

export default {
  name: 'CCJsonEditor',
  emits: ['confirm', 'cancel', 'input'],
  props: {
    // 控制弹窗显示/隐藏
    value: {
      type: Boolean,
      default: false
    },
    // 弹窗标题
    title: {
      type: String,
      default: ''
    },
    // 弹窗宽度
    width: {
      type: String,
      default: '900px'
    },
    // 初始JSON值
    initialValue: {
      type: String,
      default: ''
    },
    // 参数名称
    paramName: {
      type: String,
      default: ''
    },
    // 参数描述
    paramDescription: {
      type: String,
      default: ''
    },
    // 是否显示参数信息头部
    showHeader: {
      type: Boolean,
      default: true
    },
    // 文本区域行数
    rows: {
      type: Number,
      default: 18
    },
    // 占位符文本
    placeholder: {
      type: String,
      default: ''
    },
    // 是否自动格式化初始值
    autoFormat: {
      type: Boolean,
      default: true
    },
    // 格式化数据量阈值（字符数）
    formatThreshold: {
      type: Number,
      default: 5000000 // 5000k
    }
  },
  data() {
    return {
      jsonValue: '',
      originalValue: ''
    };
  },
  computed: {
    visible: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit('input', val);
      }
    },
    // 弹窗标题
    modalTitle() {
      return this.title || this.$t('json-can-shu-bian-ji-qi');
    },
    // 占位符文本
    placeholderText() {
      return this.placeholder || this.$t('qing-shu-ru-json-ge-shi-de-can-shu');
    }
  },
  watch: {
    value(newVal) {
      if (newVal) {
        this.$nextTick(() => {
          this.initEditor();
        });
      } else {
        this.resetEditor();
      }
    },
    initialValue: {
      handler(newVal) {
        if (newVal) {
          this.$nextTick(() => {
            this.initEditor(newVal);
          });
        }
      },
      immediate: true
    }
  },
  mounted() {
    // 组件挂载时，如果弹窗已经打开，则初始化编辑器
    if (this.value) {
      this.initEditor();
    }
  },
  methods: {
    // 初始化编辑器
    initEditor(value) {
      this.originalValue = value || '';
      this.jsonValue = this.originalValue;
    },

    // 重置编辑器
    resetEditor() {
      this.jsonValue = '';
      this.originalValue = '';
    },

    // 检查是否为有效JSON
    isJSON(str) {
      if (typeof str === 'string') {
        try {
          const obj = JSONB.parse(str);
          return Boolean(typeof obj === 'object' && obj);
        } catch (e) {
          return false;
        }
      }
      return false;
    },

    // 检查数据量是否超过阈值
    isDataTooLarge(str) {
      return str && str.length > this.formatThreshold;
    },

    // 获取数据量描述
    getDataSizeDescription(str) {
      if (!str) return '';
      const size = str.length;
      const unit = this.$t('zi-fu');
      if (size < 1000) {
        return `${size} ${unit}`;
      } else if (size < 10000) {
        return `${(size / 1000).toFixed(1)}K ${unit}`;
      } else {
        return `${(size / 1000).toFixed(1)}K ${unit}`;
      }
    },

    // 重置到原始值
    resetJson() {
      this.jsonValue = this.originalValue || '';
    },

    // 格式化JSON
    formatJson() {
      if (!this.jsonValue || !this.isJSON(this.jsonValue)) {
        this.$Message.error(this.$t('qing-shu-ru-you-xiao-de-json-ge-shi'));
        return;
      }

      // 检查数据量是否超过阈值
      if (this.isDataTooLarge(this.jsonValue)) {
        const dataSize = this.getDataSizeDescription(this.jsonValue);
        this.$Modal.confirm({
          title: this.$t('ge-shi-hua-ti-xing'),
          content: `<p>${this.$t('shu-ju-liang-da')} (${dataSize})，${this.$t('ge-shi-hua-hui-hen-man')}</p><p>${this.$t('que-ding-yao-ge-shi-hua-ma')}</p>`,
          onOk: () => {
            this.performFormat();
          }
        });
      } else {
        this.performFormat();
      }
    },

    // 执行格式化操作
    performFormat() {
      try {
        // 使用JSONB解析并重新格式化
        const parsed = JSONB.parse(this.jsonValue);
        this.jsonValue = JSON.stringify(parsed, null, 2);
        this.$Message.success(this.$t('ge-shi-hua-cheng-gong'));
      } catch (error) {
        this.$Message.error(this.$t('ge-shi-hua-shi-bai') + ': ' + error.message);
      }
    },

    // 确认保存
    handleConfirm() {
      if (!this.isJSON(this.jsonValue)) {
        this.$Message.error(this.$t('qing-shu-ru-you-xiao-de-json-ge-shi'));
        return;
      }
      this.$emit('confirm', this.jsonValue);
      this.visible = false;
    },

    // 取消编辑
    handleCancel() {
      this.$emit('cancel');
      this.visible = false;
    }
  }
};
</script>

<style lang="less" scoped>
.param-header {
  margin-bottom: 15px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 4px solid #1890ff;
}

.param-title {
  font-size: 14px;
  margin-bottom: 5px;
  font-weight: 500;
  color: #262626;
  display: flex;
  align-items: center;
}

.param-icon {
  margin-right: 6px;
  color: #1890ff;
}

.param-desc {
  font-size: 12px;
  color: #666;
  margin: 0;
}

.json-editor-container {
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
}

.json-editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  background: #fafafa;
  border-bottom: 1px solid #e8e8e8;
  border-radius: 6px 6px 0 0;
}

.toolbar-icon {
  margin-right: 6px;
  color: #1890ff;
}

.json-editor-content {
  padding: 15px;
}

.json-textarea {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', monospace;
  font-size: 13px;
  line-height: 1.5;
  border: none;
  box-shadow: none;
  resize: vertical;
}
</style>
