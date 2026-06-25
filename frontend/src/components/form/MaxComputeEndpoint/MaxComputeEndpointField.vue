<template>
  <div class="maxcompute-endpoint-field">
    <Dropdown
      trigger="custom"
      placement="bottom-start"
      transfer
      transfer-class-name="maxcompute-endpoint-transfer"
      :visible="dropdownVisible"
      @on-clickoutside="closeDropdown"
    >
      <div
        class="maxcompute-endpoint-select"
        :class="{ 'maxcompute-endpoint-select-disabled': disabled, 'maxcompute-endpoint-select-opened': dropdownVisible }"
        @click="toggleDropdown"
      >
        <span class="maxcompute-endpoint-selected">{{ selectedLabel }}</span>
        <Icon class="maxcompute-endpoint-arrow" type="ios-arrow-down" />
      </div>
      <template #list>
        <div class="maxcompute-endpoint-menu" @click.stop>
          <div class="maxcompute-endpoint-tabs">
            <button
              type="button"
              class="maxcompute-endpoint-tab"
              :class="{ 'maxcompute-endpoint-tab-active': activeAccessType === 'public' }"
              @click="activeAccessType = 'public'"
            >
              {{ publicTabLabel }}
            </button>
            <button
              type="button"
              class="maxcompute-endpoint-tab"
              :class="{ 'maxcompute-endpoint-tab-active': activeAccessType === 'vpc' }"
              @click="activeAccessType = 'vpc'"
            >
              {{ vpcTabLabel }}
            </button>
          </div>
          <div class="maxcompute-endpoint-options">
            <button
              v-for="option in currentAccessOptions"
              :key="option.key"
              type="button"
              class="maxcompute-endpoint-option"
              :class="{ 'maxcompute-endpoint-option-selected': option.key === selectedValue }"
              @click="selectEndpoint(option)"
            >
              <span>{{ option.displayLabel }}</span>
              <span class="maxcompute-endpoint-region">{{ option.regionId }}</span>
            </button>
          </div>
        </div>
      </template>
    </Dropdown>
  </div>
</template>

<script>
export default {
  name: 'MaxComputeEndpointField',
  props: {
    field: {
      type: Object,
      required: true
    },
    form: {
      type: Object,
      required: true
    },
    dataSourceForm: {
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
      selectedValue: '',
      activeAccessType: 'public',
      dropdownVisible: false,
      publicTabLabel: '公网',
      vpcTabLabel: 'VPC'
    };
  },
  computed: {
    endpointOptions() {
      return (this.field.options || [])
        .map((option) => {
          const value = option?.value || {};
          const regionId = value.regionId || '';
          const accessType = value.accessType || '';
          if (!regionId || !accessType) {
            return null;
          }
          return {
            key: `${regionId}|${accessType}`,
            label: option.label || option.labelI18N || `${regionId} / ${accessType}`,
            displayLabel: this.displayLabel(option.label || option.labelI18N || `${regionId} / ${accessType}`),
            regionId,
            accessType,
            host: value.host || '',
            sdkEndpoint: value.sdkEndpoint || ''
          };
        })
        .filter(Boolean);
    },
    currentAccessOptions() {
      return this.endpointOptions.filter((option) => option.accessType === this.activeAccessType);
    },
    selectedOption() {
      return this.endpointOptions.find((option) => option.key === this.selectedValue);
    },
    selectedLabel() {
      return this.selectedOption ? this.selectedOption.label : '';
    }
  },
  watch: {
    field: {
      handler() {
        this.initSelectedEndpoint();
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    initSelectedEndpoint() {
      const current = this.endpointOptions.find((option) => option.host === this.form.host && option.sdkEndpoint === this.form.sdkEndpoint);
      const defaultValue = this.fieldDefaultValue(this.field);
      const next = current || this.endpointOptions.find((option) => option.key === defaultValue) || this.endpointOptions[0];
      if (!next) {
        return;
      }
      this.selectedValue = next.key;
      this.activeAccessType = next.accessType || 'public';
      this.applyEndpoint(next);
    },
    displayLabel(label) {
      return String(label || '').replace(/\s*\/\s*(公网|VPC)\s*$/, '');
    },
    fieldDefaultValue(field) {
      const defaultValue = field?.defaultValue;
      if (defaultValue && typeof defaultValue === 'object' && Object.prototype.hasOwnProperty.call(defaultValue, 'value')) {
        return defaultValue.value || '';
      }
      return defaultValue || '';
    },
    applySelectedEndpoint(value) {
      const endpoint = this.endpointOptions.find((option) => option.key === value);
      if (endpoint) {
        this.applyEndpoint(endpoint);
      }
    },
    toggleDropdown() {
      if (this.disabled) {
        return;
      }
      this.dropdownVisible = !this.dropdownVisible;
    },
    closeDropdown() {
      this.dropdownVisible = false;
    },
    selectEndpoint(endpoint) {
      this.selectedValue = endpoint.key;
      this.activeAccessType = endpoint.accessType || this.activeAccessType;
      this.applyEndpoint(endpoint);
      this.closeDropdown();
    },
    applyEndpoint(endpoint) {
      this.form[this.field.field] = endpoint.key;
      this.form.host = endpoint.host;
      this.form.sdkEndpoint = endpoint.sdkEndpoint;
      this.dataSourceForm.host = endpoint.host;
      this.dataSourceForm.publicHost = endpoint.host;
      this.dataSourceForm.port = '';
      this.dataSourceForm.publicPort = '';
      this.dataSourceForm.resolvedHost = endpoint.host;
      const hostList = Array.isArray(this.dataSourceForm.hostList) ? [...this.dataSourceForm.hostList] : [];
      hostList[0] = {
        type: 'public',
        display: true,
        ...(hostList[0] || {}),
        host: endpoint.host,
        port: ''
      };
      this.dataSourceForm.hostList = hostList;
    }
  }
};
</script>

<style lang="less" scoped>
.maxcompute-endpoint-field {
  display: inline-flex;
  vertical-align: middle;
}

.maxcompute-endpoint-select {
  width: 280px;
  height: 32px;
  line-height: 30px;
  padding: 0 28px 0 10px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  cursor: pointer;
  position: relative;
  background: #fff;
  transition:
    border-color 0.2s ease-in-out,
    box-shadow 0.2s ease-in-out;
}

.maxcompute-endpoint-select:hover,
.maxcompute-endpoint-select-opened {
  border-color: #57a3f3;
}

.maxcompute-endpoint-select-disabled {
  color: #c5c8ce;
  cursor: not-allowed;
  background: #f3f3f3;
}

.maxcompute-endpoint-selected {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.maxcompute-endpoint-arrow {
  position: absolute;
  right: 8px;
  top: 8px;
  color: #808695;
}

.maxcompute-endpoint-menu {
  width: 280px;
  padding: 8px;
  background: #fff;
}

.maxcompute-endpoint-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 6px;
  margin-bottom: 8px;
  padding: 4px;
  background: #f5f7f9;
  border-radius: 4px;
}

.maxcompute-endpoint-tab {
  height: 28px;
  border: 0;
  border-radius: 3px;
  color: #515a6e;
  background: transparent;
  cursor: pointer;
}

.maxcompute-endpoint-tab-active {
  color: #2d8cf0;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.maxcompute-endpoint-options {
  max-height: 260px;
  overflow-y: auto;
}

.maxcompute-endpoint-option {
  width: 100%;
  min-height: 32px;
  padding: 6px 8px;
  border: 0;
  border-radius: 3px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  color: #515a6e;
  background: transparent;
  cursor: pointer;
}

.maxcompute-endpoint-option:hover,
.maxcompute-endpoint-option-selected {
  color: #2d8cf0;
  background: #f3f9ff;
}

.maxcompute-endpoint-region {
  color: #808695;
  font-size: 12px;
}
</style>
