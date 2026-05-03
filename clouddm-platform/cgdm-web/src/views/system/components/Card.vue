<template>
  <div class="card">
    <div class="icon">
      <cc-iconfont :name="name" size="32" color="#979797" />
    </div>
    <div class="content">
      <div class="title">
        {{ title }}
      </div>
      <div class="info">
        <div class="num" @click="handleRoute">
          {{ num }}
        </div>
        <div class="quantifier">
          {{ quantifier }}
        </div>
        <div class="btn" @click="handleClickBtn" v-if="showBtn">
          {{ btnText }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Card',
  props: {
    routerName: String,
    name: String,
    icon: String,
    title: String,
    num: Number,
    quantifier: String,
    btnText: String,
    showBtn: {
      type: Boolean,
      default: true
    },
    handleClickBtn: {
      type: Function,
      default: () => {}
    }
  },
  methods: {
    handleRoute() {
      this.$router.push({ path: this.routerName });
      this.$bus.emit('changeSidebar', this.routerName);
    }
  }
};
</script>

<style lang="less" scoped>
.card {
  display: flex;
  align-items: center;

  .icon {
    width: 80px;
    text-align: center;
    height: 80px;
    line-height: 80px;
    background: #fff;
    border-radius: 50%;
    box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.24);
  }

  .content {
    display: flex;
    flex-direction: column;
    color: rgba(0, 0, 0, 0.88);
    font-size: 14px;
    margin-left: 20px;

    .info {
      margin-top: 10px;
      display: flex;
      align-items: baseline;

      .num {
        font-size: 36px;
        line-height: 40px;
        margin-right: 5px;
        cursor: pointer;

        &:hover {
          text-decoration: underline;
        }
      }

      .btn {
        margin-left: 10px;
        font-size: 12px;
        color: @primary-color;

        &:hover {
          cursor: pointer;
        }
      }
    }
  }
}
</style>
