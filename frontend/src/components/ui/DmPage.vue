<template>
  <a-pagination
    class="list-page-pagination"
    :current="currentPage"
    :page-size="currentPageSize"
    :total="total"
    :show-size-changer="showSizer"
    :show-quick-jumper="showElevator"
    :show-total="showTotalFn"
    :page-size-options="pageSizeOptions"
    @change="handleChange"
    @showSizeChange="handleSizeChange"
  />
</template>

<script>
export default {
  name: 'DmPage',
  props: {
    total: {
      type: Number,
      default: 0
    },
    modelValue: {
      type: Number,
      default: 1
    },
    current: {
      type: Number,
      default: undefined
    },
    pageSize: {
      type: Number,
      default: 10
    },
    showTotal: {
      type: Boolean,
      default: false
    },
    showElevator: {
      type: Boolean,
      default: false
    },
    showSizer: {
      type: Boolean,
      default: false
    },
    pageSizeOpts: {
      type: Array,
      default: () => [10, 20, 40, 50, 100]
    }
  },
  emits: ['update:modelValue', 'on-change', 'on-page-size-change'],
  computed: {
    currentPage() {
      if (this.current !== undefined) {
        return this.current;
      }
      return this.modelValue;
    },
    currentPageSize() {
      return this.pageSize;
    },
    pageSizeOptions() {
      return this.pageSizeOpts.map(String);
    },
    showTotalFn() {
      if (!this.showTotal) {
        return undefined;
      }
      return (total) => `${this.$t('gong')} ${total} ${this.$t('tiao')}`;
    }
  },
  methods: {
    handleChange(page, pageSize) {
      this.$emit('update:modelValue', page);
      this.$emit('on-change', page);
      if (pageSize !== this.pageSize) {
        this.$emit('on-page-size-change', pageSize);
      }
    },
    handleSizeChange(page, pageSize) {
      this.$emit('update:modelValue', page);
      this.$emit('on-page-size-change', pageSize);
      this.$emit('on-change', page);
    }
  }
};
</script>
