<template>
  <div style="height: 100%">
    <Card :model="model" :time="time" :emitter="emitter" :filters="filters" :panel="panel" :filterOthers="filterOthers" :resourceType="resourceType" :dataJobId="dataJobId" :dataTaskId="dataTaskId" :workerId="workerId" :consoleId="consoleId"></Card>
  </div>
</template>
<script>
import Card from './card';
export default {
  components: { Card },
  props: {
    model: Object,
    time: Object,
    emitter: Object,
    panel: Object,
    filters: Object,
    filterOthers: Object,
    resourceType: String,
    dataJobId: String,
    dataTaskId: String,
    workerId: String,
    consoleId: String
  },
  data() {
    return {
    }
  },
  mounted() {
    console.log('this.props', this.model);
  }
}
</script>
