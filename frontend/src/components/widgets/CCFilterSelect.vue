<template>
  <a-select
    :v-model="vModal"
    :default-value="defaultValue"
    :style="selectStyle"
    @change="handleChange"
    :placeholder="placeholder"
    :filter-option="filterOption"
    show-search
    @popupScroll="handlePopupScroll"
    @search="handleSearch"
  >
    <a-select-option v-for="data in frontDataZ" :value="data.name || data" :key="data.name || data">
      {{ data.name || data }}
    </a-select-option>
  </a-select>
</template>
<script>
export default {
  name: 'CCFilterSelect',
  props: {
    dataList: Array,
    placeholder: String,
    handleChange: Function,
    vModal: String,
    defaultValue: String,
    selectStyle: String
  },
  data() {
    return {
      dataZ: [],
      frontDataZ: [],
      sourceOwnerSystems: [],
      valueData: '',
      treePageSize: 100,
      scrollPage: 1
    };
  },
  mounted() {
    if (this.dataList) {
      this.dataZ = this.dataList;
      this.frontDataZ = this.dataList.slice(0, this.treePageSize);
    }
  },
  methods: {
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    handleSearch(val) {
      this.valueData = val;
      if (val) {
        this.frontDataZ = [];
        this.scrollPage = 1;
        this.dataZ.forEach((item) => {
          if (item.name && item.name.indexOf(val) >= 0) {
            this.frontDataZ.push(item);
          }
        });
        this.allDataZ = this.frontDataZ;
        this.frontDataZ = this.frontDataZ.slice(0, 100);
      }
    },
    handlePopupScroll(e) {
      const { target } = e;
      const scrollHeight = target.scrollHeight - target.scrollTop;
      const clientHeight = target.clientHeight;
      // 下拉框不下拉的时候
      if (scrollHeight === 0 && clientHeight === 0) {
        this.scrollPage = 1;
      } else {
        // 当下拉框滚动条到达底部的时候
        if (scrollHeight < clientHeight + 5) {
          this.scrollPage += 1;
          const scrollPage = this.scrollPage; // 获取当前页
          const treePageSize = this.treePageSize * (scrollPage || 1); // 新增数据量
          const newData = []; // 存储新增数据
          let max = ''; // max 为能展示的数据的最大条数
          if (this.dataZ.length > treePageSize) {
            // 如果总数据的条数大于需要展示的数据
            max = treePageSize;
          } else {
            // 否则
            max = this.dataZ.length;
          }
          // 判断是否有搜索
          if (this.valueData) {
            this.allDataZ.forEach((item, index) => {
              if (index < max) {
                // 当data数组的下标小于max时
                newData.push(item);
              }
            });
          } else {
            this.dataZ.forEach((item, index) => {
              if (index < max) {
                // 当data数组的下标小于max时
                newData.push(item);
              }
            });
          }

          this.frontDataZ = newData; // 将新增的数据赋值到要显示的数组中
        }
      }
    }
  },
  watch: {
    dataList() {
      this.dataZ = this.dataList;
      this.frontDataZ = this.dataList.slice(0, this.treePageSize);
    }
  }
};
</script>
