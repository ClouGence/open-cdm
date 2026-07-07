<template>
  <div class="sql-empty-state">
    <div class="empty-content">
      <div class="empty-illustration" aria-hidden="true">
        <svg viewBox="0 0 220 150" role="presentation">
          <defs>
            <linearGradient id="sql-empty-db" x1="0" x2="0" y1="0" y2="1">
              <stop offset="0" stop-color="#ffffff" />
              <stop offset="1" stop-color="#f6fbff" />
            </linearGradient>
            <linearGradient id="sql-empty-primary" x1="0" x2="1" y1="0" y2="1">
              <stop offset="0" stop-color="#52d69a" />
              <stop offset="1" stop-color="#20b978" />
            </linearGradient>
          </defs>
          <ellipse class="illustration-ground" cx="110" cy="115" rx="70" ry="20" />
          <g class="illustration-db">
            <path d="M58 41c0-13.8 21.5-25 48-25s48 11.2 48 25v54c0 13.8-21.5 25-48 25s-48-11.2-48-25z" />
            <ellipse cx="106" cy="41" rx="48" ry="25" />
            <path d="M58 68c0 13.8 21.5 25 48 25s48-11.2 48-25" />
            <path d="M58 94c0 13.8 21.5 25 48 25s48-11.2 48-25" />
          </g>
          <g class="illustration-search">
            <circle cx="146" cy="78" r="26" />
            <path d="M164 97l28 28" />
          </g>
          <path class="illustration-star star-left" d="M43 58l5 10 10 5-10 5-5 10-5-10-10-5 10-5z" />
          <path class="illustration-star star-right" d="M176 33l5 10 10 5-10 5-5 10-5-10-10-5 10-5z" />
          <path class="illustration-star star-small" d="M32 94l3 6 6 3-6 3-3 6-3-6-6-3 6-3z" />
        </svg>
      </div>

      <h3>{{ $t('sql-empty-title') }}</h3>
      <p class="empty-description">
        {{ emptyDescription }}
      </p>

      <div class="empty-actions">
        <Tooltip :content="$t('shen-qing-quan-xian')" :disabled="canAddDataSource" transfer placement="top">
          <span>
            <Button class="empty-action empty-primary-action" type="primary" :disabled="!canAddDataSource" @click="handleAddDataSource">
              <CustomIcon type="icon-v2-tianjiashujuyuan1" size="18" />
              <span>{{ $t('sql-empty-add-datasource') }}</span>
            </Button>
          </span>
        </Tooltip>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  name: 'SqlEmptyState',
  computed: {
    ...mapState(['myAuth']),
    canAddDataSource() {
      return (this.myAuth || []).includes('RDP_DS_MANAGE');
    },
    emptyDescription() {
      return this.$t('sql-empty-description');
    }
  },
  methods: {
    handleAddDataSource() {
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
  width: 100%;
  max-width: 720px;
}

.empty-illustration {
  width: 220px;
  height: 150px;
  margin: 0 auto 24px;

  svg {
    display: block;
    width: 100%;
    height: 100%;
  }

  .illustration-ground {
    fill: rgba(62, 207, 142, 0.12);
  }

  .illustration-db {
    fill: url('#sql-empty-db');
    stroke: #d8e2ec;
    stroke-width: 4;
    stroke-linecap: round;
    stroke-linejoin: round;
  }

  .illustration-db path:nth-of-type(3),
  .illustration-db path:nth-of-type(4) {
    stroke: rgba(62, 207, 142, 0.5);
  }

  .illustration-search {
    fill: none;
    stroke: url('#sql-empty-primary');
    stroke-width: 12;
    stroke-linecap: round;
    stroke-linejoin: round;
  }

  .illustration-star {
    fill: rgba(62, 207, 142, 0.5);
  }

  .star-small {
    opacity: 0.6;
  }
}

h3 {
  margin: 0 0 12px;
  color: var(--text-primary);
  font-size: 30px;
  font-weight: 500;
  line-height: 1.3;
  letter-spacing: 0;
}

.empty-description {
  max-width: 620px;
  margin: 0 auto 32px;
  color: var(--text-secondary);
  font-size: 15px;
  line-height: 1.7;
}

.empty-actions {
  display: flex;
  justify-content: center;
}

.empty-action {
  min-width: 220px;
  height: 44px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-radius: 6px !important;
  font-size: 15px;
  font-weight: 500;
  line-height: 1;
}

.empty-primary-action {
  background: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
  color: #ffffff !important;
}

@media (max-width: 768px) {
  .empty-content {
    width: 100%;
  }

  h3 {
    font-size: 24px;
  }

  .empty-actions {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .empty-action {
    width: 100%;
  }
}
</style>
