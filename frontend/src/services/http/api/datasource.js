export const datasourceApi = {
  // datasource
  rdpDataSourceListByCondition: '/api/entry/datasource/listByCondition',
  rdpDataSourceDelete: '/api/entry/datasource/delete',
  rdpDataSourceUpdateDsDesc: '/api/entry/datasource/updateDsDesc',
  rdpDataSourceDeleteAccount: '/api/entry/datasource/deleteAccount',
  rdpDataSourceQueryDs: '/api/entry/datasource/queryDs',
  rdpDataSourceAddDs: '/api/entry/datasource/addDs',
  rdpDataSourceUpdateDs: '/api/entry/datasource/updateDs',
  rdpDataSourceCheckDriverStatus: '/api/entry/datasource/checkDriverStatus',
  rdpDataSourceDownloadDriver: '/api/entry/datasource/downloadDriver',
  rdpDataSourceUpdateAccountAndPassword: '/api/entry/datasource/updateAccountAndPassword',

  // dm
  dmDataSourceConnectDs: '/api/entry/datasource/connectDs',
  dmDataSourceAddDs: '/api/entry/datasource/addDs',
  dmDataSourceListByCondition: '/api/entry/datasource/listByCondition',
  dmDataSourceQueryDs: '/api/entry/datasource/queryDs',
  dmDataSourceUpdateDsAccount: '/api/entry/datasource/updateDsAccount',
  dmDataSourceFetchDsConfig: '/api/entry/datasource/fetchDsConfig',
  dmDataSourceFetchBindInfo: '/api/entry/datasource/fetchBindInfo',
  dmDataSourceUploadCertificate: '/api/entry/datasource/uploadCertificate',
  dmDataSourceTestConnect: '/api/entry/datasource/testConnect',
  dmDataSourceSpecialRedisTopKeysWithLimit: '/api/entry/datasource/special/redis/top_keys_with_limit',
  dmDataSourceSchemaListFirstLevel: '/api/entry/datasource/schema/listfirstlevel',
  dmDataSourceSchemaListSchemas: '/api/entry/datasource/schema/listschemas',
  dmDataSourceSchemaListTables: '/api/entry/datasource/schema/listtables',
  dmDataSourceSchemaListColumns: '/api/entry/datasource/schema/listcolumns'
};
