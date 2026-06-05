<template>
  <a-modal :visible="visible" :title="`${ds.authTitle}${$t('shou-quan')}`" class="auth-modal" @cancel="handleCloseModal" :width="620">
    <div class="header">
      {{ ds.authTitle }}{{ $t('ming-cheng') }}:
      <cc-data-source-icon :instanceType="ds.deployEnvType" :size="18" :type="ds.dataSourceType" color="#4DBAEE" />
      {{ ds.key }}
    </div>
    <div class="auth">
      <div style="display: flex">
        <a-popover v-if="checkAll.instanceCheck > 0 || checkAll.dsCheck > 0" placement="left">
          <template #content>
            {{ checkAll.instanceCheck > 0 ? $t('yi-zai-shi-li-zhong-shou-quan') : $t('yi-zai-ku-zhong-shou-quan') }}
          </template>
          <div class="auth-item">
            <a-checkbox @change="handleCheckAll" :disabled="checkAll.instanceCheck > 0 || checkAll.dsCheck > 0">
              {{ $t('quan-bu') }}
            </a-checkbox>
          </div>
        </a-popover>
        <div class="auth-item" v-else>
          <a-checkbox
            v-model="checkAll.check"
            @change="handleCheckAll"
            :indeterminate="checkAll.num !== 0 && checkAll.num !== editDsAuthKind.length"
            :disabled="checkAll.instanceCheck > 0 || checkAll.dsCheck > 0"
          >
            {{ $t('quan-bu') }}
          </a-checkbox>
        </div>
      </div>
      <div v-for="auth in editDsAuthKind" :key="auth.kindValue">
        <a-popover v-if="auth.instanceCheck || auth.dsCheck" placement="left">
          <template #content>
            {{ auth.instanceCheck ? $t('yi-zai-shi-li-zhong-shou-quan') : $t('yi-zai-ku-zhong-shou-quan') }}
            {{ renderDurationTime(auth.instanceCheck || auth.dsCheck) }}
          </template>
          <div>
            <a-checkbox
              @change="handleAuthCheck(auth.kindValue, $event)"
              :disabled="!!auth.instanceCheck || !!auth.dsCheck"
              :default-checked="auth.check || !!auth.instanceCheck || !!auth.dsCheck"
            >
              {{ auth.kindNameI18n }}
            </a-checkbox>
          </div>
        </a-popover>
        <div v-else class="auth-item">
          <a-checkbox v-model="auth.check" @change="handleAuthCheck(auth.kindValue, $event)" :disabled="!!auth.instanceCheck || !!auth.dsCheck">
            {{ auth.kindNameI18n }}
          </a-checkbox>
        </div>
      </div>
    </div>
    <a-date-picker
      ref="start-time-picker"
      v-model="startTime"
      show-time
      format="YYYY-MM-DD HH:mm:ss"
      :placeholder="$t('kai-shi-shi-jian')"
      @openChange="handleStartOpenChange"
      renderExtraFooter
      size="small"
    >
      <template #renderExtraFooter>
        <div style="display: flex">
          <div
            v-for="r in ranges"
            :key="r.key"
            @click="handleRangeClick(r.key)"
            :style="`cursor: pointer;margin-right: 5px;color: ${range === r.key ? '#0BB9F8' : 'rgba(0, 0, 0, 0.65)'}`"
          >
            {{ r.label }}
          </div>
        </div>
      </template>
    </a-date-picker>
    ~
    <a-date-picker
      v-model="endTime"
      show-time
      format="YYYY-MM-DD HH:mm:ss"
      :placeholder="$t('jie-shu-shi-jian')"
      :open="endTimeOpen"
      @openChange="handleEndOpenChange"
      size="small"
    />
    <div class="footer">
      <a-button type="primary" @click="handleSave">{{ $t('bao-cun') }}</a-button>
      <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment';
import cloneDeep from 'lodash.clonedeep';
import isEqual from 'lodash.isequal';
import dayjs from 'dayjs';
import { mapState } from 'vuex';
import { dateRange } from '@/utils';
import authMixin from '../../mixins/authMixin';

export default {
  name: 'AuthModal',
  props: {
    isTicket: Boolean,
    getUserDsAuth: Function,
    visible: Boolean,
    dsAuthKind: Array,
    ds: Object,
    handleCloseModal: Function,
    refreshCurrentTable: Function
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo
    })
  },
  mixins: [authMixin],
  watch: {
    dsAuthKind: {
      handler(newValue) {
        let num = 0;
        const value = cloneDeep(newValue);
        if (this.preAuth) {
          value.forEach((auth) => {
            if (this.preAuth[auth.kindValue]) {
              num++;
              auth.check = true;
            }
          });
        }

        this.checkAll.num = num;
        this.checkAll.check = num === this.dsAuthKind.length;
        this.editDsAuthKind = value;
      },
      immediate: true
    }
  },
  data() {
    return {
      dateRange,
      instanceAuth: {},
      dsAuth: {},
      preAuth: [],
      currentAuth: {},
      checkAll: {
        check: false,
        num: 0
      },
      editDsAuthKind: [],
      ranges: [
        { key: 1, label: this.$t('1-tian') },
        { key: 7, label: this.$t('7-tian') },
        { key: 30, label: this.$t('30-tian') }
      ],
      startTime: null,
      endTime: null,
      endTimeOpen: false,
      range: -1,
      durationClicked: false
    };
  },
  methods: {
    moment,
    handleRangeClick(key) {
      this.range = key;
      this.startTime = moment();
      this.endTime = moment().add(key, 'd');
      this.durationClicked = true;
    },
    handleStartOpenChange(open) {
      if (!open && !this.durationClicked) {
        this.endTimeOpen = true;
      }

      if (open) {
        this.durationClicked = false;
      }
    },
    handleEndOpenChange(open) {
      this.endTimeOpen = open;
    },
    async handleSave() {
      const authArr = [];
      this.editDsAuthKind.forEach((auth) => {
        if (auth.check) {
          authArr.push(auth.kindValue);
        }
      });

      const { key, id } = this.ds;

      const dsOpsAuthList = [];
      const deletes = [];
      const updates = [];

      if (!this.currentAuth.dsAuthKinds && authArr.length) {
        dsOpsAuthList.push({
          dataSourceId: id,
          diffuse: true,
          levelElements: key.split('/').slice(2),
          dsAuthKinds: authArr
        });
      }

      if (this.currentAuth.dsAuthKinds && this.currentAuth.dsAuthKinds.length && authArr.length === 0) {
        deletes.push({
          dataSourceId: id,
          dsAuthId: this.currentAuth.id
        });
      }

      if (this.currentAuth.dsAuthKinds && this.currentAuth.dsAuthKinds.length && authArr.length) {
        updates.push({
          dataSourceId: id,
          diffuse: true,
          dsAuthId: this.currentAuth.id,
          dsAuthKinds: authArr
        });
      }

      if ((!dsOpsAuthList.length && !deletes.length && !updates.length) || (this.isTicket && !dsOpsAuthList.length && !updates.length)) {
        this.$Message.error(this.$t('qing-xuan-ze-quan-xian'));
      } else {
        const data = {
          authedUid: this.$route.params.uid || this.userInfo.uid,
          dsOpsAuthList,
          deletes,
          updates,
          startTime: null,
          endTime: null
        };

        if (this.startTime) {
          data.startTime = dayjs(this.startTime).valueOf();
        }

        if (this.endTime) {
          data.endTime = dayjs(this.endTime).valueOf();
        }

        const url = !this.isTicket ? 'rdpDataAuthModifyUserDsAuth' : 'dmTicketRequestUserDsAuth';
        const res = await this.$services[url]({
          data,
          msg: `${this.ds.authTitle}${this.isTicket ? this.$t('quan-xian-shen-qing-gong-dan-yi-sheng-cheng') : this.$t('shou-quan-cheng-gong')}`
        });

        if (res.success) {
          this.handleCloseModal();
          this.refreshCurrentTable();
        }
      }
    },
    handleAuthCheck(type, e) {
      const { checked } = e.target;
      this.editDsAuthKind.forEach((auth) => {
        if (auth.kindValue === type) {
          auth.check = checked;
        }
      });

      // eslint-disable-next-line operator-assignment
      this.checkAll.num = this.checkAll.num + (checked ? 1 : -1);
      this.checkAll.check = this.checkAll.num && this.checkAll.num === this.editDsAuthKind.length;
      this.$forceUpdate();
    },
    handleCheckAll(e) {
      const { checked } = e.target;
      this.editDsAuthKind.forEach((auth) => {
        auth.check = checked;
      });

      this.checkAll = {
        check: checked,
        num: checked ? this.editDsAuthKind.length : 0
      };
    },
    async getAuth() {
      const { key, id } = this.ds;
      const levelElements = key.split('/').slice(2);
      const url = !this.isTicket ? 'rdpDataAuthListUserDsAuth' : 'rdpDataAuthListMyAllDsAuth';
      const res = await this.$services[url]({
        data: {
          dataSourceId: id,
          levelElements: levelElements.length ? levelElements : ['/'],
          ownerUid: this.$route.params.uid || this.userInfo.uid
        }
      });

      if (res.success && res.data && res.data.length) {
        this.preAuth = res.data;
        const len = levelElements.length + 1;
        const path = `/${levelElements.join('/')}`;
        let currentAuth = {};
        let instanceAuth = {};
        let dsAuth = {};
        res.data.forEach((auth) => {
          if (auth.path && auth.path !== '/' && auth.path[auth.path.length - 1] === '/') {
            auth.path = auth.path.substr(0, auth.path.length - 1);
          }
        });
        res.data.forEach((auth) => {
          if (isEqual(path, auth.path)) {
            currentAuth = auth;
            this.currentAuth = auth;
            this.startTime = auth.startTime;
            this.endTime = auth.endTime;
          }

          const authLen = auth.path === '/' ? 1 : auth.path.split('/').length;
          if (authLen < len) {
            if (authLen === 1) {
              instanceAuth = auth;
              this.instanceAuth = auth;
            } else if (authLen === 2) {
              dsAuth = auth;
              this.dsAuth = auth;
            }
          }
        });
        const editDsAuthKind = cloneDeep(this.dsAuthKind);
        let num = 0;
        let instanceCheck = 0;
        let dsCheck = 0;
        if (res.data.length) {
          editDsAuthKind.forEach((auth) => {
            if (instanceAuth.dsAuthKinds && instanceAuth.dsAuthKinds.includes(auth.kindValue)) {
              instanceCheck++;
              auth.instanceCheck = instanceAuth;
            }

            if (dsAuth.dsAuthKinds && dsAuth.dsAuthKinds.includes(auth.kindValue)) {
              dsCheck++;
              auth.dsCheck = dsAuth;
            }

            if (currentAuth.dsAuthKinds && currentAuth.dsAuthKinds.includes(auth.kindValue)) {
              auth.check = true;
              num++;
            }
          });
        }

        this.checkAll = {
          num,
          check: num === this.dsAuthKind.length,
          instanceCheck,
          dsCheck
        };
        this.editDsAuthKind = editDsAuthKind;
      }
    }
  },
  created() {
    this.getAuth();
  }
};
</script>

<style scoped lang="less">
.auth-modal {
  .header {
    width: 100%;
    height: 32px;
    line-height: 32px;
    padding: 0 10px;
    background: #f4f4f4;
    margin-bottom: 20px;
  }

  .auth {
    margin-left: 10px;

    & > div {
      margin-bottom: 10px;
    }

    .auth-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }
}
</style>
