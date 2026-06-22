export const initApi = {
  // Get Default Configuration Field Definition
  dmInitDefaultConfig: '/clouddm/console/api/v1/init/defaultConfig',
  // Test database connection + empty library detection + installed testing
  dmInitTestDb: '/clouddm/console/api/v1/init/testDb',
  // Preview pending script
  dmInitPreviewScripts: '/clouddm/console/api/v1/init/previewScripts',
  // Save Initialisation Configuration (complete mode)
  dmInitApplyConfig: '/clouddm/console/api/v1/init/applyConfig',
  // Trigger system restart
  dmInitRestart: '/clouddm/console/api/v1/init/restart'
};
