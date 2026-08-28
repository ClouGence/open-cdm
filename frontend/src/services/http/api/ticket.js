export const ticketApi = {
  dmTicketCreate: '/api/entry/approval/create',
  dmTicketUploadSqlFile: '/api/entry/resource/sqlfile/upload',
  dmTicketPreviewSqlFile: '/api/entry/resource/sqlfile/preview',
  dmTicketPreviewApprovalSql: '/api/entry/approval/previewSqlFile',
  dmTicketCancel: '/api/entry/approval/cancel',
  dmTicketConfirm: '/api/entry/approval/confirm',
  dmTicketTicketType: '/api/entry/approval/approvalType',
  dmTicketListTemplates: '/api/entry/approval/listTemplates',
  dmTicketListDsInsLevels: '/api/entry/approval/listDsInsLevels',
  dmTicketListDbLevels: '/api/entry/approval/listDbLevels',
  dmTicketRefreshTemplates: '/api/entry/approval/refreshTemplates',
  dmTicketAddTemplate: '/api/entry/approval/addTemplate',
  dmTicketRemoveTemplate: '/api/entry/approval/removeTemplate',
  dmTicketQueryAutoExecJobInfo: '/api/entry/approval/queryAutoExecJobInfo',
  dmTicketQueryAutoExecTaskList: '/api/entry/approval/queryAutoExecTaskList',
  dmTicketQueryAutoExecTaskSql: '/api/entry/approval/queryAutoExecTaskSql',
  dmTicketEndAutoExecJob: '/api/entry/approval/endAutoExecJob',
  dmTicketRetryAutoExecJob: '/api/entry/approval/retryAutoExecJob',
  dmTicketStopAutoExecJob: '/api/entry/approval/stopAutoExecJob',
  dmTicketSkipAutoExecTask: '/api/entry/approval/skipAutoExecTask',
  dmTicketContinueAutoExecTask: '/api/entry/approval/continueAutoExecTask',
  dmTicketAutoExecLog: '/api/entry/approval/autoExecLog',

  dmTicketApproUpdateKey: '/api/entry/approval/appro/updatekey',

  dmTicketQueryQueryTicketDetail: '/api/entry/approval/queryQueryApprovalDetail',

  // rdp
  rdpTicketListBasic: '/api/entry/approval/listBasic',
  rdpTicketClose: '/api/entry/approval/close',
  rdpTicketCreateDataSourceAuthTicket: '/api/entry/approval/createDataSourceAuthApproval',
  rdpTicketQueryDataSourceAuthTicketDetail: '/api/entry/approval/queryDataSourceAuthApprovalDetail',
  rdpTicketApproval: '/api/entry/approval/approval',
  rdpTicketQueryTicketBaseInfo: '/api/entry/approval/queryApprovalBaseInfo'
};
