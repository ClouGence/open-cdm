<script>
export default {
  name: 'RuleEditor',
  props: {},
  data() {
    return {
      text: {
        type: String,
        default: ''
      },
      language: {
        type: String,
        default: 'sql'
      }
    };
  }
};
</script>

<style scoped lang="less"></style>
