const browseMixin = {
  methods: {
    browseApplyPermissions(node, leaf = null) {
      const query = { type: 'apply', levels: JSON.stringify(this.browseGenLevelsData(node).map(String)) };
      if (leaf) {
        query.targetType = leaf.nodeType;
        query.targetName = leaf.objName || leaf.title;
      }
      this.$router.push({ path: '/system/permission', query });
    },
    browseGenLevelsData(node, levels = node.levels, field = 'id') {
      return levels.map((level) => node[level][field]);
    },
    browseGenLevelsDataKey(node, levels = node.levels, field = 'id') {
      return levels.map((level) => `\`${node[level][field]}\``).join('.');
    },
    async browseGenAction(
      actionType,
      levels,
      callback = null,
      other = {
        targetType: '',
        targetName: '',
        targetNewName: '',
        options: {}
      }
    ) {
      const { targetType, targetName, targetNewName, options, targetExactName } = other;
      const data = {
        levels,
        targetType,
        targetName,
        targetNewName,
        actionType,
        options,
        targetExactName
      };

      const res = await this.$services.dmBrowseActionsGenAction({ data });

      if (res.success) {
        const { sql = '', danger = false } = res.data;
        const { permission = false, permissionI18n = '' } = res;

        if (callback) {
          callback(permission, danger, sql, data);
        }
      }
    },
    async browseDoAction(data, callback = null, callbackFail = null) {
      try {
        const res = await this.$services.dmBrowseActionsDoAction({ data });

        if (res.success) {
          if (callback) {
            callback();
          }
        } else {
          if (callbackFail) {
            callbackFail();
          }
        }
      } catch (e) {
        if (callbackFail) {
          callbackFail();
        }
      }
    }
  }
};

export default browseMixin;
