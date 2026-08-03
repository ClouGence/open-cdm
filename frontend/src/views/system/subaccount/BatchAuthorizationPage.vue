<template>
  <div class="batch-authorization-page">
    <Spin v-if="loading" fix />

    <div class="batch-authorization-page__body">
      <div class="batch-authorization-content">
        <section class="page-section">
          <div class="page-section__title">{{ $t('shou-quan-fang-shi') }}</div>
          <div class="page-section__content">
            <RadioGroup v-model="operation" class="authorization-methods">
              <Radio :label="BATCH_AUTH_OPERATION.GRANT" class="authorization-method">
                {{ $t('zhui-jia-quan-xian') }}
              </Radio>
              <Radio :label="BATCH_AUTH_OPERATION.REVOKE" class="authorization-method">
                {{ $t('yi-chu-quan-xian') }}
              </Radio>
            </RadioGroup>
          </div>
        </section>

        <section class="page-section">
          <div class="page-section__title">{{ $t('cao-zuo-yong-hu') }}</div>
          <div class="page-section__content">
            <div class="user-field-toolbar">
              <p class="page-section__description">{{ $t('cao-zuo-yong-hu-shuo-ming') }}</p>
              <div v-if="accounts.length && !loadFailed" class="user-selector-actions">
                <button v-if="targetUids.length" type="button" class="text-action" @click="targetUids = []">
                  {{ $t('qing-chu') }}
                </button>
                <button type="button" class="text-action" @click="selectAllEnabledAccounts">
                  {{ $t('quan-xuan') }}
                </button>
              </div>
            </div>

            <div v-if="loadFailed" class="user-list-state">
              <span>{{ $t('cao-zuo-yong-hu-jia-zai-shi-bai') }}</span>
              <Button type="text" @click="loadAccounts">{{ $t('zhong-shi') }}</Button>
            </div>
            <div v-else-if="!accounts.length" class="user-list-state">
              {{ $t('zan-wu-shu-ju') }}
            </div>
            <template v-else>
              <Select
                v-model="targetUids"
                class="user-selector"
                multiple
                filterable
                transfer
                :placeholder="$t('qing-xuan-ze-cao-zuo-yong-hu')"
                :not-found-text="$t('zan-wu-shu-ju')"
              >
                <Option
                  v-for="account in accounts"
                  :key="account.uid"
                  :value="account.uid"
                  :label="accountSearchLabel(account)"
                  :disabled="account.disable"
                >
                  <div class="account-option">
                    <span class="account-option__name">{{ accountPrimaryLabel(account) }}</span>
                    <span v-if="accountSecondaryLabel(account)" class="account-option__meta">
                      {{ accountSecondaryLabel(account) }}
                    </span>
                    <span v-if="account.disable" class="account-option__status">{{ $t('jin-yong') }}</span>
                  </div>
                </Option>
              </Select>
              <div class="user-selector-meta">
                {{ $t('yi-xuan-ze-n-ge-zhang-hao', [targetUids.length]) }}
              </div>
            </template>
          </div>
        </section>
      </div>
    </div>

    <div class="batch-authorization-page__footer">
      <Button @click="goBack">{{ $t('qu-xiao') }}</Button>
      <Button type="primary" :disabled="loading || loadFailed || !accounts.length" @click="handleNext">
        {{ $t('xia-yi-bu') }}
      </Button>
    </div>
  </div>
</template>

<script>
const BATCH_AUTH_OPERATION = Object.freeze({
  GRANT: 'GRANT',
  REVOKE: 'REVOKE'
});

export default {
  name: 'BatchAuthorizationPage',
  data() {
    return {
      loading: false,
      loadFailed: false,
      BATCH_AUTH_OPERATION,
      operation: BATCH_AUTH_OPERATION.GRANT,
      targetUids: [],
      accounts: []
    };
  },
  created() {
    this.restoreSelectionFromRoute();
    this.loadAccounts();
  },
  methods: {
    restoreSelectionFromRoute() {
      const operation = String(this.$route.query.operation || '').toUpperCase();
      if (Object.values(BATCH_AUTH_OPERATION).includes(operation)) {
        this.operation = operation;
      }
      this.targetUids = String(this.$route.query.uids || '')
        .split(',')
        .filter(Boolean);
    },
    async loadAccounts() {
      this.loading = true;
      this.loadFailed = false;
      const res = await this.$services.rdpUserManagerListSubAccounts({
        data: {
          roleId: 0,
          userNameOrSubAccountPrefix: ''
        }
      });
      this.loading = false;
      if (!res.success || !Array.isArray(res.data)) {
        this.loadFailed = true;
        return;
      }
      this.accounts = res.data;
      const enabledUids = new Set(this.accounts.filter((account) => !account.disable).map((account) => account.uid));
      this.targetUids = this.targetUids.filter((uid) => enabledUids.has(uid));
    },
    accountDisplay(account) {
      if (account.bindType && account.bindType !== 'INTERNAL') {
        return account.bindAccount || account.account || '';
      }
      return account.account || '';
    },
    accountPrimaryLabel(account) {
      return account.username || this.accountDisplay(account) || account.uid;
    },
    accountSecondaryLabel(account) {
      const accountName = this.accountDisplay(account);
      if (accountName && accountName !== this.accountPrimaryLabel(account)) {
        return accountName;
      }
      return account.roleName || '';
    },
    accountSearchLabel(account) {
      const primaryLabel = this.accountPrimaryLabel(account);
      const accountName = this.accountDisplay(account);
      if (accountName && accountName !== primaryLabel) {
        return `${primaryLabel}（${accountName}）`;
      }
      return primaryLabel;
    },
    selectAllEnabledAccounts() {
      this.targetUids = this.accounts.filter((account) => !account.disable).map((account) => account.uid);
    },
    goBack() {
      this.$router.push({ name: 'Management_Accounts_Account' });
    },
    handleNext() {
      if (!this.targetUids.length) {
        this.$Message.warning(this.$t('zhi-shao-xuan-ze-yi-ge-zhang-hao'));
        return;
      }
      this.$router.push({
        name: 'Management_Accounts_Batch_Authorization_Permissions',
        query: {
          type: 'batch',
          operation: this.operation,
          uids: this.targetUids.join(',')
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
.batch-authorization-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  background: var(--bg-card);

  &__body {
    flex: 1;
    min-height: 0;
    overflow: auto;
    padding: 16px 24px;
  }

  &__footer {
    flex-shrink: 0;
    display: flex;
    justify-content: center;
    gap: 8px;
    padding: 16px 24px;
    border-top: 1px solid var(--border-light);
    background: var(--bg-card);
  }
}

.batch-authorization-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
  width: 100%;
  max-width: 720px;
}

.page-section {
  min-width: 0;
}

.page-section__title {
  position: relative;
  margin-bottom: 12px;
  padding-left: 12px;
  color: var(--text-primary);
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
}

.page-section__title::before {
  position: absolute;
  top: 50%;
  left: 0;
  width: 3px;
  height: 16px;
  border-radius: 2px;
  background: var(--primary-color);
  transform: translateY(-50%);
  content: '';
}

.page-section__content {
  padding-left: 12px;
}

.user-field-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 8px;
}

.page-section__description {
  min-width: 0;
  margin: 0;
  color: var(--text-secondary);
  font-size: 13px;
  line-height: 20px;
}

.authorization-methods {
  display: flex;
  align-items: center;
  gap: 32px;
  min-height: 32px;
}

.authorization-method {
  margin: 0;
  color: var(--text-primary);
  font-size: 14px;
}

.user-selector {
  width: 100%;

  :deep(.ivu-select-selection) {
    min-height: 40px;
    max-height: 104px;
    border-radius: 6px;
    overflow-y: auto;
  }

  :deep(.ivu-select-input) {
    height: 38px;
    line-height: 38px;
    top: 0;
  }

  :deep(.ivu-select-selection .ivu-tag) {
    max-width: 200px;
    height: 26px;
    margin-top: 6px;
    margin-bottom: 4px;
    border-color: var(--border-light) !important;
    border-radius: 4px;
    background: var(--bg-secondary) !important;
    color: var(--text-primary) !important;
    line-height: 24px;
  }

  :deep(.ivu-select-selection .ivu-tag-text) {
    display: inline-block;
    max-width: 160px;
    overflow: hidden;
    color: var(--text-primary) !important;
    text-overflow: ellipsis;
    vertical-align: middle;
    white-space: nowrap;
  }

  :deep(.ivu-select-selection .ivu-tag .ivu-icon-ios-close) {
    color: var(--text-tertiary) !important;
  }
}

.account-option {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.account-option__name {
  min-width: 0;
  overflow: hidden;
  color: var(--text-primary);
  font-weight: 500;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.account-option__meta {
  min-width: 0;
  overflow: hidden;
  color: var(--text-tertiary);
  font-size: 12px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.account-option__status {
  margin-left: auto;
  color: var(--text-tertiary);
  font-size: 12px;
}

.user-selector-meta {
  margin-top: 8px;
  color: var(--text-secondary);
  font-size: 13px;
  line-height: 20px;
}

.user-selector-actions {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  gap: 4px;
}

.text-action {
  height: 28px;
  border: 1px solid transparent;
  border-radius: 4px;
  padding: 0 8px;
  background: transparent;
  color: var(--text-secondary);
  font-family: inherit;
  font-size: 13px;
  line-height: 26px;
  cursor: pointer;
  transition:
    background-color 0.12s ease,
    color 0.12s ease,
    transform 0.12s ease;

  &:hover {
    background: var(--bg-secondary);
    color: var(--text-primary);
  }

  &:active {
    transform: translateY(1px);
  }

  &:focus-visible {
    outline: 2px solid var(--primary-color);
    outline-offset: 1px;
  }
}

.user-list-state {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-height: 96px;
  border-radius: 10px;
  background: var(--bg-secondary);
  color: var(--text-secondary);
  font-size: 13px;
}

@media (max-width: 767px) {
  .batch-authorization-page__body {
    padding: 16px;
  }

  .batch-authorization-page__footer {
    padding: 16px;
  }

  .user-field-toolbar {
    align-items: flex-start;
    flex-direction: column;
    gap: 4px;
  }

  .user-selector-actions {
    margin-left: -8px;
  }
}
</style>
