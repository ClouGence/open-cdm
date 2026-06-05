<script>
export default {
  name: 'TableListLayout',
  props: {
    setShowData: Function,
    breadcrumbList: Array,
    tableData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      pageNum: 1,
      pageSize: 20
    };
  },
  methods: {
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      const data = this.tableData.slice((this.pageNum - 1) * this.pageSize, this.pageNum * this.pageSize);
      this.setShowData(data);
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handlePageChange(1);
    }
  }
};
</script>

<template>
  <div class="table-list-layout">
    <div class="table-list">
      <div class="header" v-if="$slots.header || breadcrumbList">
        <slot name="header" />
        <Breadcrumb v-if="!$slots.header && breadcrumbList">
          <BreadcrumbItem v-for="breadcrumb in breadcrumbList" :to="breadcrumb.to" :key="breadcrumb.label">
            {{ breadcrumb.label }}
          </BreadcrumbItem>
        </Breadcrumb>
      </div>
      <div class="content">
        <div class="option" v-if="$slots.left || $slots.right">
          <div class="left">
            <slot name="left"></slot>
          </div>
          <div class="right">
            <slot name="right"></slot>
          </div>
        </div>
        <div class="table-container">
          <slot name="table"></slot>
        </div>
      </div>
    </div>
    <div class="footer">
      <slot name="page" />
      <Page
        :total="tableData.length"
        show-total
        show-elevator
        @on-change="handlePageChange"
        v-if="!$slots.page"
        show-sizer
        v-model="pageNum"
        :page-size="pageSize"
        @on-page-size-change="handlePageSizeChange"
      />
    </div>
  </div>
</template>
