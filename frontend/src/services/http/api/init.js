export const initApi = {
  // Get Default Configuration Field Definition
  dmInitDefaultConfig: '/clouddm/console/api/v1/init/defaultConfig',
  // Test database connection + empty library detection + installed testing
  dmInitTestDb: '/clouddm/console/api/v1/init/testDb',
  // Check initializing program driver status
  dmInitCheckDriverStatus: '/clouddm/console/api/v1/init/checkDriverStatus',
  // Download Initialise Program Driver
  dmInitDownloadDriver: '/clouddm/console/api/v1/init/downloadDriver',
  // Preview pending script
  dmInitPreviewScripts: '/clouddm/console/api/v1/init/previewScripts',
  // Save Initialisation Configuration (complete mode)
  dmInitApplyConfig: '/clouddm/console/api/v1/init/applyConfig',
  // Trigger system restart
  dmInitRestart: '/clouddm/console/api/v1/init/restart'
};
