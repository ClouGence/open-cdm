<script>
export default {
  name: 'CCSelectEdit',
  props: {
    value: {
      type: String,
      default: '-'
    },
    label: {
      type: String,
      default: '-'
    },
    options: {
      type: Array,
      default: () => []
    },
    valueOfValue: {
      type: Function,
      default: (item) => item.value
    },
    valueOfLabel: {
      type: Function,
      default: (item) => item.label
    },
    fieldName: {
      type: String,
      default: '-'
    },
    onOK: {
      type: Function,
      default: () => {}
    },
    onEditor: {
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
    }
  },
  data() {
    return {
      showModal: false,
      localValue: this.value
    };
  },
  methods: {
    handleOk(e) {
      this.onOK({ label: e.label, value: e.value });
      this.showModal = false;
    },
    openModal() {
      this.localValue = this.value;
      this.showModal = true;
      this.onEditor();
    },
    handleClear() {
      this.showModal = false;
    }
  }
};
</script>

<template>
  <div>
    <div class="field-wrap">
      <div style="display: contents">
        <span>{{ fieldName }}</span>
        <span v-if="isColon">:</span>
        <div v-if="!showModal" class="field-value" :style="`padding-left: ${valueMargin || '0'}`">
          <Poptip trigger="hover" placement="top" :content="label">
            <span class="text-ellipsis">{{ label }}</span>
          </Poptip>
          <CustomIcon v-if="!readOnly" @click="openModal" :type="icon" leftMargin hoverStyle />
        </div>
        <div v-if="showModal" class="field-value" :style="`padding-left: ${valueMargin || '0'}`">
          <Select ref="fieldSelect" size="small" v-model="localValue" :label-in-value="true" clearable @on-clear="handleClear" @on-change="handleOk">
            <template #prefix>
              <CustomIcon type="icon-v2-svg-USER" />
            </template>
            <Option v-for="item in options" :value="valueOfValue(item)" :key="valueOfValue(item)" :label="valueOfLabel(item)">
              <Tooltip :content="valueOfLabel(item)" transfer>
                <div class="text-overflow">
                  {{ valueOfLabel(item) }}
                </div>
              </Tooltip>
            </Option>
          </Select>
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
  max-width: 150px;
}

.text-ellipsis {
  display: inline-block;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}

.text-overflow {
  width: 100%;
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
