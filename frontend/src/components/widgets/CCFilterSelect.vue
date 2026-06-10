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
      // When you can't pull it down.
      if (scrollHeight === 0 && clientHeight === 0) {
        this.scrollPage = 1;
      } else {
        // When the bottom of the zipper is reached
        if (scrollHeight < clientHeight + 5) {
          this.scrollPage += 1;
          const scrollPage = this.scrollPage; // Get the current page
          const treePageSize = this.treePageSize * (scrollPage || 1); // Add Data Volume
          const newData = []; // Store new data
          let max = ''; // max is the maximum number of bars that can be displayed
          if (this.dataZ.length > treePageSize) {
            // If the number of entries in the total data is greater than the data to be displayed
            max = treePageSize;
          } else {
            // Otherwise...
            max = this.dataZ.length;
          }
          // To determine if there is a search.
          if (this.valueData) {
            this.allDataZ.forEach((item, index) => {
              if (index < max) {
                // When the lower mark of the data array is less than max
                newData.push(item);
              }
            });
          } else {
            this.dataZ.forEach((item, index) => {
              if (index < max) {
                // When the lower mark of the data array is less than max
                newData.push(item);
              }
            });
          }

          this.frontDataZ = newData; // Adds new data values to arrays to display Medium
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
