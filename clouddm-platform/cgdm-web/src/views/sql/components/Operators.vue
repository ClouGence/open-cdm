<template>
  <div class="operators">
    <div class="operator-character">
      <div class="left">
        <ButtonGroup class="operator-btn-group">
          <Button size="small" type="primary" :disabled="isRunning" :loading="tab.running" @click="handleRun('run')">
            <div style="display: flex; align-items: center; padding-top: 2px">
              <CustomIcon v-if="!tab.running" type="icon-v2-ConsoleRun" :color="tab.running ? '#999' : '#fff'" size="14px" right-margin="4px" />
              <span>{{ $t('zhi-hang') }}</span>
            </div>
          </Button>
          <Button size="small" v-if="isSupportExplain" :disabled="isRunning" @click="handlePlan('plan')">
            <div style="display: flex; align-items: center; padding-top: 2px">
              <CustomIcon type="icon-v2-ConsolePlan" :color="tab.running ? '#999' : isDark ? '#fff' : '#000'" size="14px" right-margin="4px" />
              <span>{{ $t('ji-hua') }}</span>
            </div>
          </Button>
          <Button size="small" v-if="isSupportCancel" :disabled="isStoping" :loading="tab.stopping" @click="handleStop">
            <div style="display: flex; align-items: center; padding-top: 2px">
              <CustomIcon
                v-if="!tab.stopping"
                type="icon-v2-ConsoleStop"
                :color="tab.running ? '#999' : isDark ? '#fff' : '#000'"
                size="14px"
                right-margin="4px"
              />
              <span>{{ $t('zhong-duan-zhi-hang') }}</span>
            </div>
          </Button>
          <Button size="small" v-if="isSupportFormat" @click="formatSql">
            <div style="display: flex; align-items: center; padding-top: 2px">
              <CustomIcon type="icon-v2-ConsoleFormat" size="12px" right-margin="2px" />
              <span>{{ $t('ge-shi') }}</span>
            </div>
          </Button>
        </ButtonGroup>
        <ButtonGroup class="operator-btn-group" v-if="isSupportTx || isSupportIsolation || isSupportReadOnly" style="padding-left: 5px">
          <Button size="small" v-if="isSupportIsolation">
            <Dropdown trigger="click" @on-click="handleSet" transfer>
              {{ tab.autoCommit ? $t('shi-wu-zi-dong') : $t('shi-wu-shou-dong') }}
              <Icon type="ios-arrow-down"></Icon>
              <template #list>
                <DropdownMenu>
                  <DropdownItem v-if="isSupportTx" disabled>
                    {{ $t('shi-wu') }}
                  </DropdownItem>
                  <DropdownItem name="txAuto" v-if="isSupportTx" :selected="tab.autoCommit" :disabled="isRunning">
                    <div v-show="tab.autoCommit">
                      <CustomIcon type="icon-v2-seleted" size="12px" />
                      <span style="padding-left: 11px">{{ $t('zi-dong') }}</span>
                    </div>
                    <div v-show="!tab.autoCommit">
                      <span style="padding-left: 23px">{{ $t('zi-dong') }}</span>
                    </div>
                  </DropdownItem>
                  <DropdownItem name="txManual" v-if="isSupportTx" :selected="!tab.autoCommit" :disabled="isRunning">
                    <div v-show="!tab.autoCommit">
                      <CustomIcon type="icon-v2-seleted" size="12px" />
                      <span style="padding-left: 11px">{{ $t('shou-dong') }}</span>
                    </div>
                    <div v-show="tab.autoCommit">
                      <span style="padding-left: 23px">{{ $t('shou-dong') }}</span>
                    </div>
                  </DropdownItem>
                  <DropdownItem v-if="isSupportIsolation" disabled>
                    {{ $t('ge-li-ji-bie') }}
                  </DropdownItem>
                  <DropdownItem
                    :name="isolation.key"
                    v-for="isolation in listSupportIsolation"
                    :key="isolation.key"
                    :selected="tab.isolation === isolation.key"
                    :disabled="isRunning"
                  >
                    <div v-show="tab.isolation === isolation.key">
                      <CustomIcon type="icon-v2-seleted" size="12px" />
                      <span style="padding-left: 11px">{{ isolation.i18n }}</span>
                    </div>
                    <div v-show="tab.isolation !== isolation.key">
                      <span style="padding-left: 23px">{{ isolation.i18n }}</span>
                    </div>
                  </DropdownItem>
                </DropdownMenu>
              </template>
            </Dropdown>
          </Button>
          <Button size="small" v-if="isSupportTx" :disabled="isRunning || tab.autoCommit" @click="handleCommit">
            {{ $t('ti-jiao') }}
          </Button>
          <Button size="small" v-if="isSupportTx" :disabled="isRunning || tab.autoCommit" @click="handleRollback">
            {{ $t('hui-gun') }}
          </Button>
          <Button size="small" v-if="isSupportReadOnly">
            <Checkbox v-model="tab.readOnly" @on-change="handleReadOnly">
              {{ $t('zhi-du') }}
            </Checkbox>
          </Button>
        </ButtonGroup>
      </div>
      <div class="right"></div>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapState } from 'vuex';
import browseMixin from '@/mixins/browseMixin';

export default {
  name: 'Operators',
  components: {},
  mixins: [browseMixin],
  props: {
    tab: Object,
    storeQueryTabs: Function,
    formatSql: Function,
    handleReadOnly: Function,
    handleRun: Function,
    handlePlan: Function,
    handleStop: Function,
    handleRollback: Function,
    handleCommit: Function,
    handleSetIsolation: Function,
    handleSetTx: Function
  },
  data() {
    return {};
  },
  computed: {
    ...mapGetters(['isDesktop', 'supportRdbTx', 'isolations', 'isDark']),
    ...mapState(['userInfo', 'globalSetting', 'socket']),
    isSupportIsolation() {
      return this.tab.support.isolation.conf !== 'No';
    },
    listSupportIsolation() {
      return this.tab.support.isolation.conf !== 'No' ? this.isolations(this.tab.dsType) : [];
    },
    isSupportTx() {
      return this.tab.support.autoCommit.conf !== 'No';
    },
    isSupportReadOnly() {
      return this.tab.support.readOnly.conf !== 'No';
    },
    isSupportCancel() {
      return this.tab.support.cancel.conf !== 'No';
    },
    isSupportExplain() {
      return this.tab.support.explain.conf !== 'No';
    },
    isSupportFormat() {
      return this.tab.support.format.conf !== 'No';
    },
    isRunning() {
      return this.tab.stopping || this.tab.running || !this.socket.connected || !this.tab.connected;
    },
    isStoping() {
      return this.tab.stopping || !this.socket.connected || !this.tab.connected;
    }
  },
  watch: {
    'tab.sessionId': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.handleSetInitTx();
        }
      },
      immediate: true,
      deep: true
    },
    tab: {
      handler(newVal, oldVal) {
        console.log('tab', newVal);
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    handleSet(e) {
      if (e === 'txAuto') {
        this.handleSetTx(true);
      } else if (e === 'txManual') {
        this.handleSetTx(false);
      } else {
        this.handleSetIsolation(e);
      }
    },
    handleSetInitTx() {
      if (this.tab.support.autoCommit.conf === 'Allow' && this.tab.sessionId) {
        this.handleSetTx(this.tab.autoCommit);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.operators {
  padding: 3px;
  border-bottom: 1px solid #c7c7c7;
  position: relative;

  .operator-character {
    display: flex;
    justify-content: space-between;
    align-items: center;
    overflow: hidden;
    white-space: nowrap;

    .left {
    }

    .right {
    }
  }
}

.operator-btn-group {
  overflow: hidden;
  white-space: nowrap;
  display: inline-flex;
}
</style>
