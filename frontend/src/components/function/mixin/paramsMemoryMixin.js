const paramsMemoryMixin = {
  data() {
    return {
      memoryParamsKey: ''
    };
  },
  created() {},
  methods: {
    initParams() {
      const searchParams = JSON.parse(sessionStorage.getItem(this.memoryParamsKey));

      if (searchParams) {
        this[`${this.memoryParamsKey}_data`] = searchParams;
      }
    }
  }
};
