<script>
import deepClone from 'lodash.clonedeep';

export default {
  name: 'CCReadOnlyTable',
  props: {
    selectedConfig: Object,
    cellData: {
      type: Array,
      default: () => []
    },
    columnList: Array,
    width: Number
  },
  data() {
    return {
      rawOptions: {
        container: 'luckysheet',
        lang: 'zh',
        allowCopy: false,
        showtoolbar: false,
        showtoolbarConfig: false,
        showinfobar: false,
        showsheetbar: false,
        showsheetbarConfig: false,
        // showstatisticBar: false,
        // showstatisticBarConfig: false,
        enableAddBackTop: false,
        enableAddRow: false,
        cellRightClickConfig: {
          copy: true, // 复制
          copyAs: false, // 复制为
          paste: false, // 粘贴
          insertRow: false, // 插入行
          insertColumn: false, // 插入列
          deleteRow: false, // 删除选中行
          deleteColumn: false, // 删除选中列
          deleteCell: false, // 删除单元格
          hideRow: false, // 隐藏选中行和显示选中行
          hideColumn: false, // 隐藏选中列和显示选中列
          rowHeight: false, // 行高
          columnWidth: false, // 列宽
          clear: false, // 清除内容
          matrix: false, // 矩阵操作选区
          sort: false, // 排序选区
          filter: false, // 筛选选区
          chart: false, // 图表生成
          image: false, // 插入图片
          link: false, // 插入链接
          data: false, // 数据验证
          cellFormat: false, // 设置单元格格式
          customs: []
        },
        hook: {
          columnTitleCellRenderAfter: (_, position, ctx) => {
            const { c, left, height, width } = position;

            const column = this.columnList[c];

            ctx.clearRect(left, 0, width - 1, height - 1);

            ctx.fillText(column, left, height / 2);
          }
        },
        data: [
          {
            name: 'data',
            celldata: [],
            config: {
              columnlen: {},
              authority: {
                allowRangeList: [{}]
              }
            },
            dataVerification: {}
          }
        ]
      },
      options: null
    };
  },
  mounted() {
    this.options = deepClone(this.rawOptions);
    if (this.cellData.length) {
      this.options.data[0].celldata = this.cellData;
      this.options.data[0].row = this.cellData.length / this.columnList.length;
      this.options.data[0].column = this.columnList.length;
      window.luckysheet.create(this.options);
    }
  },
  watch: {
    'selectedConfig.name': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.options = deepClone(this.rawOptions);
          console.log(this.cellData);
          if (this.cellData.length) {
            window.luckysheet.destroy();
            this.options.data[0].celldata = this.cellData;
            this.options.data[0].row = this.cellData.length / this.columnList.length;
            this.options.data[0].column = this.columnList.length;
            window.luckysheet.create(this.options);
          } else {
            window.luckysheet.destroy();
          }
        }
      },
      deep: true
    }
  },
  methods: {}
};
</script>

<template>
  <div id="luckysheet" :style="`width: ${width}px`" />
</template>

<style scoped lang="less">
#luckysheet {
  position: absolute;
  height: 500px;
}

:deep(.luckysheet-work-area) {
  display: none;
}

:deep(.luckysheet-copy-btn) {
  display: none;
}

:deep(.luckysheet-rows-h) {
  top: -11px;
}

:deep(#luckysheet-dataVerification-dropdown-List .dropdown-List-item.multi.checked) {
  background: yellow;
}

:deep(#luckysheet-dataVerification-dropdown-List .dropdown-List-item.single.checked) {
  background: yellow;
}

:deep(.luckysheet-stat-area) {
  display: none !important;
}

:deep(.luckysheet-cell-main) {
  background: #fff;
}

:deep(.luckysheet-cs-draghandle) {
  display: none !important;
}

:deep(.luckysheet-cs-fillhandle) {
  display: none !important;
}

:deep(.luckysheet-scrollbar-ltr) {
  z-index: 1000 !important;
}
</style>
