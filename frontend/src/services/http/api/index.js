import { userApi } from '@/services/http/api/user';
import { roleApi } from '@/services/http/api/role';
import { clusterApi } from '@/services/http/api/cluster';
import { workerApi } from '@/services/http/api/worker';
import { queryApi } from '@/services/http/api/query';
import { schemaApi } from '@/services/http/api/schema';
import { ticketApi } from '@/services/http/api/ticket';
import { aliyunApi } from '@/services/http/api/aliyun';
import { desensitizationApi } from '@/services/http/api/desensitization';
import { dataRuleApi } from '@/services/http/api/dataRule';
import { dsApproTemplatesApi } from '@/services/http/api/dsApproTemplates';
import { editorApi } from '@/services/http/api/editor';
import { browseApi } from '@/services/http/api/browse';
import { versionApi } from '@/services/http/api/version';
import { authApi } from '@/services/http/api/auth';
import { constantApi } from '@/services/http/api/constant';
import { fakerApi } from '@/services/http/api/faker';
import { authCodeApi } from '@/services/http/api/authCode';
import { asyncTaskApi } from '@/services/http/api/asyncTask';
import { securityApi } from '@/services/http/api/security';
import { billingApi } from '@/services/http/api/billing';
import { calculateApi } from '@/services/http/api/calculate';
import { datasourceApi } from '@/services/http/api/datasource';
import { devopsApi } from '@/services/http/api/devops';
import { projectApi } from '@/services/http/api/project';
import { desktopApi } from '@/services/http/api/desktop';
import { dataAuthApi } from '@/services/http/api/dataAuth';
import { dsEnvApi } from '@/services/http/api/dsEnv';
import { envParamApi } from '@/services/http/api/envParam';
import { saasApi } from '@/services/http/api/saas';
import { auditApi } from '@/services/http/api/audit';
import { voucherApi } from '@/services/http/api/voucher';
import { consoleApi } from '@/services/http/api/console';
import { jarPackageApi } from '@/services/http/api/jarPackage';
import { exceptionApi } from '@/services/http/api/exception';
import { fsmApi } from '@/services/http/api/fsm';
import { specApi } from '@/services/http/api/spec';
import { logViewApi } from '@/services/http/api/logView';
import { monitorConfigApi } from '@/services/http/api/monitorConfig';
import { monitorApi } from '@/services/http/api/monitor';
import { allWorkerApi } from '@/services/http/api/allWorker';
import { scheduleApi } from '@/services/http/api/schedule';
import { webOpApi } from '@/services/http/api/webOp';
import { alertApi } from '@/services/http/api/alert';
import { positionApi } from '@/services/http/api/position';
import { dataHandleApi } from '@/services/http/api/dataHandle';
import { consolejobApi } from '@/services/http/api/consoleJob';
import { systemConfigApi } from '@/services/http/api/systemConfig';
import { taskRestartHistoryApi } from '@/services/http/api/taskRestartHistory';
import { transferApi } from '@/services/http/api/transfer';
import { initApi } from '@/services/http/api/init';
import { verifyApi } from '@/services/http/api/verify';
import { mfaApi } from '@/services/http/api/mfa';
import { financeApi } from '@/services/http/api/finance';
import { taskTemplateApi } from '@/services/http/api/taskTemplate';
import { taskTemplateDetailApi } from '@/services/http/api/taskTemplateDetail';
import { metaSchemaApi } from '@/services/http/api/metaSchema';

export const api = {
  ...authApi,
  ...browseApi,
  ...editorApi,
  ...userApi,
  ...roleApi,
  ...clusterApi,
  ...workerApi,
  ...queryApi,
  ...datasourceApi,
  ...schemaApi,
  ...ticketApi,
  ...aliyunApi,
  ...dataAuthApi,
  ...desensitizationApi,
  ...dataRuleApi,
  ...dsApproTemplatesApi,
  ...versionApi,
  ...constantApi,
  ...fakerApi,
  ...authCodeApi,
  ...asyncTaskApi,
  ...securityApi,
  ...auditApi,
  ...securityApi,
  ...billingApi,
  ...calculateApi,
  ...devopsApi,
  ...projectApi,
  ...desktopApi,
  ...dsEnvApi,
  ...envParamApi,
  ...saasApi,
  ...voucherApi,
  ...consoleApi,
  ...jarPackageApi,
  ...exceptionApi,
  ...fsmApi,
  ...specApi,
  ...logViewApi,
  ...monitorConfigApi,
  ...monitorApi,
  ...allWorkerApi,
  ...scheduleApi,
  ...webOpApi,
  ...alertApi,
  ...positionApi,
  ...dataHandleApi,
  ...consolejobApi,
  ...systemConfigApi,
  ...taskRestartHistoryApi,
  ...transferApi,
  ...initApi,
  ...verifyApi,
  ...mfaApi,
  ...financeApi,
  ...taskTemplateApi,
  ...taskTemplateDetailApi,
  ...transferApi,
  ...metaSchemaApi
};
