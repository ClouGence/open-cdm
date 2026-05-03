<script>
export default {
  name: 'CCFieldEdit',
  props: {
    value: {
      type: String,
      default: '-'
    },
    fieldName: {
      type: String,
      default: '-'
    },
    noFieldName: {
      type: Boolean,
      default: false
    },
    onOK: {
      type: Function,
      default: () => {}
    },
    icon: {
      type: String,
      default: 'Edit'
    },
    isColon: {
      type: Boolean,
      default: true
    },
    readOnly: {
      type: Boolean,
      default: false
    },
    valueMargin: {
      type: String,
      default: ''
    },
    maxWidth: {
      type: String,
      default: '100px'
    },
    large: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      showModal: false,
      localValue: this.value,
      cacheValue: null
    };
  },
  watch: {
    value(newVal) {
      this.localValue = newVal;
    }
  },
  methods: {
    handleOk() {
      this.onOK(this.localValue);
      this.showModal = false;
    },
    handleKeyUp(e) {
      if (e.keyCode === 27) {
        this.localValue = this.cacheValue;
        this.cacheValue = null;
        this.showModal = false;
      }
    },
    openModal() {
      this.cacheValue = this.value;
      this.localValue = this.value;
      this.showModal = true;
      this.$nextTick(() => {
        this.$refs.fieldInput.focus({ cursor: 'start' });
      });
    }
  }
};
</script>

<template>
  <div>
    <div class="field-wrap">
      <div style="display: contents">
        <span v-if="!noFieldName">{{ fieldName }}</span>
        <span v-if="isColon && !noFieldName">:</span>

        <!-- 普通显示状态 -->
        <div v-if="!showModal" class="field-value" :style="{ paddingLeft: valueMargin || '0' }">
          <Poptip trigger="hover" placement="top" :content="value">
            <span class="text-ellipsis" :class="{ 'text-large': large }" :style="{ maxWidth: maxWidth }">
              {{ value }}
            </span>
          </Poptip>
          <CustomIcon v-if="!readOnly" @click="openModal" :type="icon" leftMargin hoverStyle />
        </div>

        <!-- 编辑状态 -->
        <div v-if="showModal" class="field-value" :style="{ paddingLeft: valueMargin || '0' }">
          <Input ref="fieldInput" type="text" size="small" v-model="localValue" @on-enter="handleOk" @on-keyup="handleKeyUp">
            <template #suffix>
              <CustomIcon @click="handleOk" type="icon-v2-baocun" size="12px" topMargin="6px" hoverStyle />
            </template>
          </Input>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.field-wrap {
  display: flex;
}
.field-value {
  display: flex;
  align-items: center;
}
.text-ellipsis {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}
.text-large {
  font-size: 16px;
}
</style>
