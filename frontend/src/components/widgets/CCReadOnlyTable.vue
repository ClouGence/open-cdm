<script>
import { cloneDeep as deepClone } from '@/utils/lodash';

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
          copy: true, // Copy
          copyAs: false, // Copy As
          paste: false, // Paste
          insertRow: false, // Insert Row
          insertColumn: false, // Insert Columns
          deleteRow: false, // Delete Selected Lines
          deleteColumn: false, // Delete Selected Columns
          deleteCell: false, // Remove Cells
          hideRow: false, // Hide selected rows and show selected rows
          hideColumn: false, // Hide selected columns and show selected columns
          rowHeight: false, // Line height
          columnWidth: false, // Column width
          clear: false, // Clear Contents
          matrix: false, // Matrix Operating Selection
          sort: false, // Sort Selection
          filter: false, // Selection Selection
          chart: false, // Chart Generation
          image: false, // Insert Picture
          link: false, // Insert Link
          data: false, // Data validation
          cellFormat: false, // Set cell format
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
