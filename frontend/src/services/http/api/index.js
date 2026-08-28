import { userApi } from '@/services/http/api/user';
import { roleApi } from '@/services/http/api/role';
import { clusterApi } from '@/services/http/api/cluster';
import { workerApi } from '@/services/http/api/worker';
import { queryApi } from '@/services/http/api/query';
import { ticketApi } from '@/services/http/api/ticket';
import { aliyunApi } from '@/services/http/api/aliyun';
import { editorApi } from '@/services/http/api/editor';
import { browseApi } from '@/services/http/api/browse';
import { versionApi } from '@/services/http/api/version';
import { authApi } from '@/services/http/api/auth';
import { fakerApi } from '@/services/http/api/faker';
import { asyncTaskApi } from '@/services/http/api/asyncTask';
import { securityApi } from '@/services/http/api/security';
import { datasourceApi } from '@/services/http/api/datasource';
import { devopsApi } from '@/services/http/api/devops';
import { cicdApi } from '@/services/http/api/cicd';
import { desktopApi } from '@/services/http/api/desktop';
import { dsEnvApi } from '@/services/http/api/dsEnv';
import { envParamApi } from '@/services/http/api/envParam';
import { auditApi } from '@/services/http/api/audit';
import { logViewApi } from '@/services/http/api/logView';
import { consolejobApi } from '@/services/http/api/consoleJob';
import { initApi } from '@/services/http/api/init';
import { mfaApi } from '@/services/http/api/mfa';
import { sshConfigApi } from '@/services/http/api/sshConfig';

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
  ...ticketApi,
  ...aliyunApi,
  ...versionApi,
  ...fakerApi,
  ...asyncTaskApi,
  ...securityApi,
  ...auditApi,
  ...devopsApi,
  ...cicdApi,
  ...desktopApi,
  ...dsEnvApi,
  ...envParamApi,
  ...logViewApi,
  ...consolejobApi,
  ...initApi,
  ...mfaApi,
  ...sshConfigApi
};
