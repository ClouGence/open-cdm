<template>
  <div class="sql-empty-state">
    <div class="empty-content">
      <div class="empty-title">
        <h3>{{ $t('sql-empty-title') }}</h3>
        <p class="empty-description">
          {{ $t('sql-empty-description') }}
        </p>
      </div>

      <div class="empty-actions">
        <div class="action-step">
          <div class="action-icon">
            <CustomIcon type="icon-v2-tianjiashujuyuan1" size="56" />
          </div>
          <div class="action-text">{{ $t('sql-empty-add-datasource') }}</div>
          <Button type="primary" :disabled="!myAuth.includes('RDP_DS_MANAGE')" @click="handleAddDataSource">
            {{ $t('sql-empty-add-datasource') }}
          </Button>
        </div>

        <div class="flow-arrow">
          <CustomIcon type="icon-v2-right-circle-fill" size="28" />
        </div>

        <div class="action-step">
          <div class="action-icon">
            <CustomIcon type="icon-v2-peizhishujuyuan" size="56" />
          </div>
          <div class="action-text">{{ $t('sql-empty-config-datasource') }}</div>
          <Button type="primary" :disabled="!myAuth.includes('DM_DS_MANAGE')" @click="handleConfigDataSource">
            {{ $t('sql-empty-config-datasource') }}
          </Button>
        </div>

        <div class="action-step" v-if="!myAuth.includes('DM_DS_MANAGE')">
          <div class="action-icon">
            <CustomIcon type="icon-v2-TicketAuth" size="56" color="#000" />
          </div>
          <div class="action-text">{{ $t('shen-qing-quan-xian') }}</div>
          <Button type="primary" @click="handleAuthDataSource">
            {{ $t('shen-qing-quan-xian') }}
          </Button>
        </div>

        <div class="flow-arrow" v-if="!myAuth.includes('DM_DS_MANAGE')">
          <CustomIcon type="icon-v2-right-circle-fill" size="28" />
        </div>

        <div class="action-step">
          <div class="action-icon">
            <CustomIcon type="icon-v2-zhihangSQLchaxun" size="56" />
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
    }
  },
  methods: {
    handleAuthDataSource() {
      console.log('handleAuthDataSource');
      this.$router.push({ path: '/system/permission', query: { type: 'apply' } });
    },
    handleAddDataSource() {
      this.$router.push('/system/ccdatasource');
    },
    handleConfigDataSource() {
      this.$router.push('/system/ccdatasource');
    }
  }
};
</script>

<style scoped lang="less">
.sql-empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  padding: 40px 20px;
  background: #ffffff;
}

.empty-content {
  text-align: center;
  max-width: 1100px;
  width: 100%;
}

.empty-title {
  margin-bottom: 56px;

  h3 {
    font-size: 28px;
    font-weight: 500;
    color: #262626;
    margin: 0 0 14px 0;
  }

  .empty-description {
    font-size: 15px;
    color: #8c8c8c;
    margin: 0;
    line-height: 1.5;
  }
}

.empty-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 56px;
  flex-wrap: wrap;
}

.action-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 18px;
}

.action-icon {
  color: #1890ff;
}

.action-text {
  font-size: 15px;
  color: #262626;
  font-weight: 500;
}

.flow-arrow {
  color: #1890ff;
}

.action-button-placeholder {
  height: 32px;
}

// 响应式设计
@media (max-width: 768px) {
  .sql-empty-state {
    min-height: 50vh;
    padding: 20px 16px;
  }

  .empty-title {
    margin-bottom: 44px;
  }

  .empty-title h3 {
    font-size: 22px;
  }

  .empty-description {
    font-size: 14px;
  }

  .empty-actions {
    flex-direction: column;
    gap: 44px;
  }

  .flow-arrow {
    transform: rotate(90deg);
  }

  .action-step {
    gap: 15px;
  }
}

@media (max-width: 480px) {
  .empty-title {
    margin-bottom: 36px;
  }

  .empty-title h3 {
    font-size: 18px;
  }

  .empty-description {
    font-size: 13px;
  }

  .empty-actions {
    gap: 36px;
  }

  .action-step {
    gap: 13px;
  }
}
</style>
