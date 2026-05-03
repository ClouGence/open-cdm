export const workerApi = {
  // createWorker: '/clouddm/console/api/v1/worker/createinitialworker',
  // getWorkerList: '/clouddm/console/api/v1/worker/listworkers',
  // getDownloadUrl: '/clouddm/console/api/v1/worker/downloadclienturl',
  // deleteWorker: '/clouddm/console/api/v1/worker/deleteworker',
  // startWorker: '/clouddm/console/api/v1/worker/waittoonline',
  // stopWorker: '/clouddm/console/api/v1/worker/waittooffline',

  // dm
  dmWorkerCreateInitialWorker: '/clouddm/console/api/v1/worker/createinitialworker',
  dmWorkerListWorkers: '/clouddm/console/api/v1/worker/listworkers',
  dmWorkerDownloadClientUrl: '/clouddm/console/api/v1/worker/downloadclienturl',
  dmWorkerDeleteWorker: '/clouddm/console/api/v1/worker/deleteworker',
  dmWorkerWaitToOnline: '/clouddm/console/api/v1/worker/waittoonline',
  dmWorkerWaitToOffline: '/clouddm/console/api/v1/worker/waittooffline',
  dmWorkerClientCoreConfig: '/clouddm/console/api/v1/worker/clientcoreconfig',
  dmWorkerUpdateWorkerDesc: '/clouddm/console/api/v1/worker/updateworkerdesc',

  // cc
  ccWorkerListWorker: '/cloudcanal/console/api/v1/inner/worker/listworker',
  ccWorkerUpdate: '/cloudcanal/console/api/v1/inner/worker/update',
  ccWorkerDelete: '/cloudcanal/console/api/v1/inner/worker/delete',
  ccWorkerDownloadClientUrl: '/cloudcanal/console/api/v1/inner/worker/downloadclienturl',
  ccWorkerClientCoreConfig: '/cloudcanal/console/api/v1/inner/worker/clientcoreconfig',
  ccWorkerUpdateAlertConfig: '/cloudcanal/console/api/v1/inner/worker/updatealertconfig',
  ccWorkerWaitToOffline: '/cloudcanal/console/api/v1/inner/worker/waittooffline',
  ccWorkerWaitToOnline: '/cloudcanal/console/api/v1/inner/worker/waittoonline',
  ccWorkerCreateSelfMaintainWorker: '/cloudcanal/console/api/v1/inner/worker/createselfmaintainworker',
  ccWorkerQueryWorkerById: '/cloudcanal/console/api/v1/inner/worker/queryworkerbyid',
  ccWorkerCheckWorkerWhiteIp: '/cloudcanal/console/api/v1/inner/worker/checkworkerwhiteip',
  ccWorkerAddWorkerWhiteListIp: '/cloudcanal/console/api/v1/inner/worker/addworkerwhitelistip',
  ccWorkerUpdateMemOverSoldPercent: '/cloudcanal/console/api/v1/inner/worker/updatememoversoldpercent',
  ccWorkerUpdateExternalIp: '/cloudcanal/console/api/v1/inner/worker/updateexternalip',
  ccWorkerListWorkerTaskDetails: '/cloudcanal/console/api/v1/inner/worker/listworkertaskdetails',
  ccWorkerClearLog: '/cloudcanal/console/api/v1/inner/worker/clearlog',
  ccWorkerListExternalIps: '/cloudcanal/console/api/v1/inner/worker/listExternalIps',
  ccWorkerExportTaskLog: '/cloudcanal/console/api/v1/inner/worker/exporttasklog',
  ccWorkerExportWorkerLog: '/cloudcanal/console/api/v1/inner/worker/exportworkerlog'
};
