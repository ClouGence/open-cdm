<template>
  <div class="sql-empty-state">
    <div class="empty-content">
      <div class="empty-title">
        <h3>{{ $t('sql-empty-title') }}</h3>
        <p class="empty-description">
          {{ emptyDescription }}
        </p>
      </div>

      <div class="empty-actions">
        <div v-if="showDataSourceSetupActions" class="action-step">
          <div class="action-icon">
            <CustomIcon type="icon-v2-tianjiashujuyuan1" size="48" />
          </div>
          <div class="action-text">{{ $t('sql-empty-add-datasource') }}</div>
          <Button type="primary" :disabled="!myAuth.includes('RDP_DS_MANAGE')" @click="handleAddDataSource">
            {{ $t('sql-empty-add-datasource') }}
          </Button>
        </div>

        <div v-if="showDataSourceSetupActions" class="flow-arrow">
          <CustomIcon type="icon-v2-right-circle-fill" size="24" />
        </div>

        <div v-if="showDataSourceSetupActions" class="action-step">
          <div class="action-icon">
            <CustomIcon type="icon-v2-peizhishujuyuan" size="48" />
          </div>
          <div class="action-text">{{ $t('sql-empty-config-datasource') }}</div>
          <Button type="primary" :disabled="!myAuth.includes('DM_DS_MANAGE')" @click="handleConfigDataSource">
            {{ $t('sql-empty-config-datasource') }}
          </Button>
        </div>

        <div class="action-step" v-if="!myAuth.includes('DM_DS_MANAGE')">
          <div class="action-icon">
            <CustomIcon type="icon-v2-TicketAuth" size="48" />
          </div>
          <div class="action-text">{{ $t('shen-qing-quan-xian') }}</div>
          <Tooltip :content="rootAccountUnsupportedTip" :disabled="!isRootAccount" transfer placement="top">
            <span>
              <Button type="primary" @click="handleAuthDataSource" :disabled="isRootAccount">
                {{ $t('shen-qing-quan-xian') }}
              </Button>
            </span>
          </Tooltip>
        </div>

        <div class="flow-arrow" v-if="!myAuth.includes('DM_DS_MANAGE')">
          <CustomIcon type="icon-v2-right-circle-fill" size="24" />
        </div>

        <div class="action-step">
          <div class="action-icon">
            <CustomIcon type="icon-v2-zhihangSQLchaxun" size="48" />
          </div>
          <div class="action-text">{{ $t('sql-empty-start-query') }}</div>
          <div class="action-button-placeholder" aria-hidden="true"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import Mapping from '@/views/util';

export default {
  name: 'SqlEmptyState',
  data() {
    return {
      Mapping
    };
  },
  props: {
    hasDatasource: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapState(['userInfo', 'myAuth']),
    hasRdpDsReadPermission() {
      return this.userInfo.authArr && this.userInfo.authArr.includes('RDP_DS_READ');
    },
    isRootAccount() {
      return this.userInfo.accountType === 'PRIMARY_ACCOUNT';
    },
    showDataSourceSetupActions() {
      return this.isRootAccount;
    },
    emptyDescription() {
      return this.showDataSourceSetupActions ? this.$t('sql-empty-description') : this.$t('sql-empty-subaccount-description');
    },
    rootAccountUnsupportedTip() {
      return '管理员账号不支持此操作';
    }
  },
  methods: {
    handleAuthDataSource() {
      if (this.isRootAccount) {
        this.$Message.warning(this.rootAccountUnsupportedTip);
        return;
      }
      this.$router.push({ path: '/system/permission', query: { type: 'apply' } });
    },
    handleAddDataSource() {
      this.$router.push('/datasource');
    },
    handleConfigDataSource() {
      this.$router.push('/datasource');
    }
  }
};
</script>

<style scoped lang="less">
.sql-empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 120px);
  padding: 48px 24px;
  background: var(--bg-primary);
}

.empty-content {
  text-align: center;
  max-width: 960px;
  width: 100%;
}

.empty-title {
  margin-bottom: 48px;

  h3 {
    font-size: 24px;
    font-weight: 500;
    color: var(--text-primary);
    margin: 0 0 10px;
    letter-spacing: -0.02em;
  }

  .empty-description {
    font-size: 14px;
    color: var(--text-secondary);
    margin: 0;
    line-height: 1.55;
  }
}

.empty-actions {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  gap: 40px;
  flex-wrap: wrap;
}

.action-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
  width: 160px;
}

.action-icon {
  color: var(--primary-color);
  opacity: 0.9;
}

.action-text {
  font-size: 13px;
  color: var(--text-primary);
  font-weight: 500;
}

.flow-arrow {
  color: var(--text-tertiary);
  margin-top: 12px;
}

.action-button-placeholder {
  height: 32px;
}

@media (max-width: 768px) {
  .empty-actions {
    flex-direction: column;
    gap: 32px;
    align-items: center;
  }

  .flow-arrow {
    transform: rotate(90deg);
    margin-top: 0;
  }
}
</style>
