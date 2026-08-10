<template>
  <Select
    ref="select"
    :model-value="value"
    :placeholder="placeholder || $t('qing-xuan-ze')"
    placement="bottom-start"
    transfer
    transfer-class-name="release-flow-select-dropdown"
    events-enabled
    filterable
    :not-found-text="$t('zan-wu-shu-ju')"
    :disabled="disabled"
    @on-change="handleChange"
    @on-query-change="handleQueryChange"
    @on-open-change="handleOpenChange"
  >
    <Option v-for="type in filteredOptions" :key="type" :value="type" :label="type">
      <span class="database-type-option-content">
        <CustomIcon :type="type" size="18px" />
        <span>{{ type }}</span>
      </span>
    </Option>
  </Select>
</template>

<script>
export default {
  name: 'ReleaseFlowDatabaseTypeSelect',
  props: {
    value: { type: String, default: '' },
    options: { type: Array, default: () => [] },
    placeholder: { type: String, default: '' },
    disabled: { type: Boolean, default: false }
  },
  emits: ['input', 'change', 'open-change'],
  data() {
    return {
      dropdownOpen: false,
      searchKeyword: ''
    };
  },
  computed: {
    filteredOptions() {
      if (!this.dropdownOpen || !this.searchKeyword) {
        return this.options;
      }

      const keyword = this.searchKeyword.toLowerCase();
      return this.options.filter((type) => String(type).toLowerCase().includes(keyword));
    }
  },
  methods: {
    handleChange(type) {
      this.searchKeyword = '';
      this.$emit('input', type);
      this.$emit('change', type);
    },
    handleQueryChange(query) {
      if (!this.dropdownOpen) {
        return;
      }

      this.searchKeyword = query === (this.value || '') ? '' : query.trim();
    },
    handleOpenChange(open) {
      this.dropdownOpen = open;
      if (!open) {
        this.searchKeyword = '';
      }
      this.$emit('open-change', open, this.$refs.select);
    }
  }
};
</script>
