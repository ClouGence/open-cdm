const { app, BrowserWindow, dialog } = require('electron');
const path = require('path');
const { spawn, execFile } = require('child_process');
const http = require('http');
const fs = require('fs');

// ---------------------------------------------------------------------------
// Constants — desktop uses dedicated ports to avoid clashing with dev (8222/3306/8008)
// ---------------------------------------------------------------------------
const APP_WEB_PORT = 18222;
const DB_PORT = 3307;
const RSOCKET_PORT = 18008;
const MYSQL_ROOT_PASSWORD = 'cgdm';

const USER_DATA_DIR = path.join(app.getPath('home'), '.cgdm-desktop');
const RUNTIME_DIR = path.join(USER_DATA_DIR, 'runtime');
const CONF_DIR = path.join(RUNTIME_DIR, 'conf');
const LOG_DIR = path.join(USER_DATA_DIR, 'logs');
const MYSQL_RUN_DIR = path.join(USER_DATA_DIR, 'mysql_run');
const MYSQL_DATA_DIR = path.join(USER_DATA_DIR, 'mysql_data');

const BACKEND_DIR = path.join(process.resourcesPath, 'backend');
const MYSQL_DIR = path.join(process.resourcesPath, 'mysql');

const MYSQL_SOCKET = path.join(MYSQL_RUN_DIR, 'mysqld.sock');
const MYSQL_PID_FILE = path.join(MYSQL_RUN_DIR, 'mysqld.pid');
const MYSQL_INIT_MARKER = path.join(MYSQL_DATA_DIR, '.cgdm_initialized');
const MYSQL_CONFIGURED_MARKER = path.join(USER_DATA_DIR, '.mysql_configured');

let mysqlProcess = null;
let javaProcess = null;
let mainWindow = null;
let isQuitting = false;

// ---------------------------------------------------------------------------
// Single instance lock
// ---------------------------------------------------------------------------
const gotLock = app.requestSingleInstanceLock();
if (!gotLock) {
  app.quit();
} else {
  app.on('second-instance', () => {
    if (mainWindow) {
      if (mainWindow.isMinimized()) mainWindow.restore();
      mainWindow.focus();
    }
  });
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

function buildJdbcUrl() {
  return `jdbc:mysql://127.0.0.1:${DB_PORT}/cdmgr?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai`;
}

function sendStatus(msg) {
  console.log(`[cgdm] ${msg}`);
  if (mainWindow && mainWindow.webContents) {
    mainWindow.webContents.send('status', msg);
  }
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function ensurePortFree(port) {
  const { execSync } = require('child_process');
  try {
    execSync(`lsof -ti :${port} | xargs kill -9 2>/dev/null`, { timeout: 5000 });
  } catch (_) {}
}

function clearRestartFlag() {
  try {
    fs.rmSync(path.join(RUNTIME_DIR, '.restarting'), { force: true });
  } catch (_) {}
}

async function waitForBackendExit(timeoutMs = 120000) {
  if (!javaProcess) {
    return;
  }

  const start = Date.now();
  while (javaProcess) {
    if (Date.now() - start > timeoutMs) {
      console.warn('[cgdm] Backend did not exit after init restart, forcing stop...');
      await stopJavaBackend();
      break;
    }
    await sleep(500);
  }

  ensurePortFree(APP_WEB_PORT);
  await sleep(1000);
}

async function waitForAppReady(timeoutMs = 300000) {
  const base = `http://127.0.0.1:${APP_WEB_PORT}`;
  const settingsUrl = `${base}/api/entry/dmGlobalSettings`;
  const initUrl = `${base}/api/entry/init/defaultConfig`;
  const start = Date.now();

  while (Date.now() - start < timeoutMs) {
    try {
      const settingsResp = await fetch(settingsUrl, { method: 'POST' });
      if (settingsResp.ok) {
        const result = await settingsResp.json();
        const status = result?.data?.systemStatus?.status;
        if (status === 'Ready') {
          return;
        }
        if (status === 'Starting') {
          sendStatus('Loading plugins...');
          await sleep(2000);
          continue;
        }
      }
    } catch (_) {}

    try {
      const initResp = await fetch(initUrl, { method: 'POST' });
      if (!initResp.ok) {
        await sleep(2000);
        continue;
      }
    } catch (_) {
      await sleep(2000);
      continue;
    }

    await sleep(2000);
  }

  throw new Error('CloudDM did not become ready within ' + timeoutMs / 1000 + 's');
}

async function loadAppWindow() {
  const appUrl = `http://127.0.0.1:${APP_WEB_PORT}/`;
  sendStatus('Opening CloudDM...');
  await mainWindow.loadURL(appUrl);
}

function findPlexusClassworldsJar() {
  const binDir = path.join(BACKEND_DIR, 'bin');
  const entries = fs.readdirSync(binDir);
  const jar = entries.find(e => e.startsWith('plexus-classworlds-') && e.endsWith('.jar'));
  if (!jar) throw new Error('plexus-classworlds jar not found in ' + binDir);
  return path.join(binDir, jar);
}

function execFileAsync(file, args, options = {}) {
  return new Promise((resolve, reject) => {
    execFile(file, args, options, (err, stdout, stderr) => {
      if (err) {
        err.stdout = stdout;
        err.stderr = stderr;
        reject(err);
      } else {
        resolve({ stdout, stderr });
      }
    });
  });
}

function runMysqlAdmin(args) {
  const mysqladminPath = path.join(MYSQL_DIR, 'bin', 'mysqladmin');
  return execFileAsync(mysqladminPath, [
    `--protocol=socket`,
    `--socket=${MYSQL_SOCKET}`,
    '-uroot',
    ...args,
  ], { timeout: 15000, env: mysqlClientEnv() });
}

function runMysql(args, extraArgs = []) {
  const mysqlPath = path.join(MYSQL_DIR, 'bin', 'mysql');
  return execFileAsync(mysqlPath, [
    `--protocol=socket`,
    `--socket=${MYSQL_SOCKET}`,
    '-uroot',
    ...extraArgs,
    ...args,
  ], { timeout: 30000, env: mysqlClientEnv() });
}

function mysqlClientEnv() {
  return { ...process.env, DYLD_LIBRARY_PATH: path.join(MYSQL_DIR, 'lib') };
}

function mysqldEnv() {
  return { ...process.env, DYLD_LIBRARY_PATH: path.join(MYSQL_DIR, 'lib') };
}

async function autoInit() {
  const apiBase = `http://127.0.0.1:${APP_WEB_PORT}`;
  const initPath = '/api/entry/init';

  try {
    const resp = await fetch(`${apiBase}${initPath}/defaultConfig`, { method: 'POST' });
    if (!resp.ok) return;
  } catch (_) {
    return;
  }

  console.log('[cgdm] Auto-initializing...');
  sendStatus('Initializing database schema...');

  const payload = {
    'server.port': String(APP_WEB_PORT),
    'clouddm.rsocket.dns': '127.0.0.1',
    'clouddm.rsocket.console.port': String(RSOCKET_PORT),
    'jwt.secret': 'jwt67843ad4s118123ycgve45uk12ghd3vli4u510fd9z35hec2hegre876n1g3sa8s2o',
    'spring.datasource.jdbcurl': buildJdbcUrl(),
    'spring.datasource.username': 'root',
    'spring.datasource.password': MYSQL_ROOT_PASSWORD,
    'clougence.init.admin.account': 'admin',
    'clougence.init.admin.email': 'admin@localhost',
    'clougence.init.admin.password': 123456,
  };

  for (let attempt = 1; attempt <= 3; attempt++) {
    console.log(`[cgdm] Auto-init attempt ${attempt}/3`);
    try {
      const initResp = await fetch(`${apiBase}${initPath}/applyConfig`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      const result = await initResp.json();
      if (!result.success) {
        console.error('[cgdm] Auto-init API returned failure:', result.error);
        return;
      }

      console.log('[cgdm] Init accepted, waiting for backend restart...');
      sendStatus('Initialization done, restarting...');

      await waitForBackendExit();

      await startJavaBackend();
      clearRestartFlag();
      await waitForAppReady();

      try {
        const checkResp = await fetch(`${apiBase}${initPath}/defaultConfig`, { method: 'POST' });
        if (!checkResp.ok) {
          console.log('[cgdm] Backend is now in FULL mode.');
          return;
        }
        console.log('[cgdm] Backend still in INIT mode after restart.');
      } catch {
        console.log('[cgdm] Backend is now in FULL mode (connection error).');
        return;
      }
    } catch (err) {
      console.error(`[cgdm] Auto-init attempt ${attempt} error:`, err.message);
    }
    if (attempt < 3) {
      await new Promise(r => setTimeout(r, 3000));
    }
  }

  console.error('[cgdm] Auto-init failed after 3 attempts.');
}

function waitForHttp(url, timeoutMs = 180000) {
  return new Promise((resolve, reject) => {
    const start = Date.now();
    const poll = () => {
      const req = http.get(url, () => {
        req.destroy();
        resolve();
      });
      req.on('error', () => {
        if (Date.now() - start > timeoutMs) {
          reject(new Error('Backend did not start within ' + timeoutMs / 1000 + 's'));
        } else {
          setTimeout(poll, 1000);
        }
      });
      req.setTimeout(3000, () => {
        req.destroy();
        if (Date.now() - start > timeoutMs) {
          reject(new Error('Backend did not start'));
        } else {
          setTimeout(poll, 1000);
        }
      });
    };
    poll();
  });
}

function findJava() {
  const javaHome = process.env.JAVA_HOME;
  if (javaHome) return path.join(javaHome, 'bin', 'java');

  const candidates = [
    '/opt/homebrew/opt/openjdk@17/bin/java',
    '/usr/local/opt/openjdk@17/bin/java',
    '/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java',
    '/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin/java',
    'java',
  ];
  for (const c of candidates) {
    if (c === 'java') return c;
    if (fs.existsSync(c)) return c;
  }
  return 'java';
}

function ensureJavaAvailable() {
  const javaCmd = findJava();
  if (javaCmd !== 'java') return javaCmd;

  try {
    require('child_process').execSync('java -version', { stdio: 'ignore' });
    return 'java';
  } catch (_) {
    throw new Error(
      'JDK 17+ is required but not found.\n\n'
      + 'Install OpenJDK 17 (e.g. brew install openjdk@17) and retry.\n'
      + 'Logs: ' + path.join(LOG_DIR, 'java.log')
    );
  }
}

// ---------------------------------------------------------------------------
// Writable runtime layout (app.home must be writable for init config persistence)
// ---------------------------------------------------------------------------

function ensureSymlink(linkPath, targetPath) {
  if (fs.existsSync(linkPath)) {
    const stat = fs.lstatSync(linkPath);
    if (stat.isSymbolicLink()) {
      const current = fs.readlinkSync(linkPath);
      if (current === targetPath) return;
    }
    fs.rmSync(linkPath, { recursive: true, force: true });
  }
  fs.symlinkSync(targetPath, linkPath, 'dir');
}

function prepareRuntimeLayout() {
  fs.mkdirSync(RUNTIME_DIR, { recursive: true });
  fs.mkdirSync(CONF_DIR, { recursive: true });
  fs.mkdirSync(LOG_DIR, { recursive: true });
  fs.mkdirSync(MYSQL_RUN_DIR, { recursive: true });
  fs.mkdirSync(MYSQL_DATA_DIR, { recursive: true });

  ensureSymlink(path.join(RUNTIME_DIR, 'libs'), path.join(BACKEND_DIR, 'libs'));
  ensureSymlink(path.join(RUNTIME_DIR, 'plugins'), path.join(BACKEND_DIR, 'plugins'));
  ensureSymlink(path.join(RUNTIME_DIR, 'bin'), path.join(BACKEND_DIR, 'bin'));
}

function prepareConfig() {
  const srcConfDir = path.join(BACKEND_DIR, 'conf');
  const aloneProps = path.join(CONF_DIR, 'alone.properties');

  if (fs.existsSync(srcConfDir)) {
    for (const f of fs.readdirSync(srcConfDir)) {
      const src = path.join(srcConfDir, f);
      const dst = path.join(CONF_DIR, f);
      if (fs.statSync(src).isDirectory()) {
        if (!fs.existsSync(dst)) {
          fs.mkdirSync(dst, { recursive: true });
          for (const sf of fs.readdirSync(src)) {
            fs.copyFileSync(path.join(src, sf), path.join(dst, sf));
          }
        }
      } else if (!fs.existsSync(dst)) {
        fs.copyFileSync(src, dst);
      }
    }
  }

  // Always overwrite version file to reflect current bundled version
  const srcVersion = path.join(BACKEND_DIR, 'conf', 'version');
  const dstVersion = path.join(CONF_DIR, 'version');
  if (fs.existsSync(srcVersion)) {
    fs.copyFileSync(srcVersion, dstVersion);
  }

  if (!fs.existsSync(aloneProps)) {
    throw new Error('alone.properties not found in ' + CONF_DIR);
  }

  let content = fs.readFileSync(aloneProps, 'utf-8');
  content = content.replace(/server\.port=\d+/, `server.port=${APP_WEB_PORT}`);
  content = content.replace(
    /clouddm\.rsocket\.console\.port=\d+/,
    `clouddm.rsocket.console.port=${RSOCKET_PORT}`
  );
  content = content.replace(
    /spring\.datasource\.jdbcurl=.*/,
    `spring.datasource.jdbcurl=${buildJdbcUrl()}`
  );
  content = content.replace(
    /^spring\.datasource\.username=.*/m,
    'spring.datasource.username=root'
  );
  content = content.replace(
    /^spring\.datasource\.password=.*/m,
    `spring.datasource.password=${MYSQL_ROOT_PASSWORD}`
  );
  fs.writeFileSync(aloneProps, content);
}

// ---------------------------------------------------------------------------
// MySQL lifecycle
// ---------------------------------------------------------------------------

function cleanupMysqlRuntimeFiles() {
  for (const f of [MYSQL_SOCKET, `${MYSQL_SOCKET}.lock`, MYSQL_PID_FILE]) {
    try {
      fs.rmSync(f, { force: true });
    } catch (_) {}
  }
}

function cleanupLegacyMysqlRuntimeFiles() {
  for (const f of ['/tmp/cgdm-mysqld.sock', '/tmp/cgdm-mysqld.sock.lock', '/tmp/cgdm-mysqld.pid']) {
    try {
      fs.rmSync(f, { force: true });
    } catch (_) {}
  }
}

function killEmbeddedMysqldProcesses() {
  const { execSync } = require('child_process');
  const datadirNeedle = `--datadir=${MYSQL_DATA_DIR}`;

  let listing = '';
  try {
    listing = execSync('ps -eo pid=,command=', { encoding: 'utf8', timeout: 5000 });
  } catch (_) {
    return;
  }

  for (const line of listing.split('\n')) {
    const trimmed = line.trim();
    if (!trimmed) continue;

    const spaceIdx = trimmed.indexOf(' ');
    if (spaceIdx <= 0) continue;

    const pid = Number(trimmed.slice(0, spaceIdx));
    const command = trimmed.slice(spaceIdx + 1);
    if (!pid || !command.includes('mysqld')) continue;
    if (!command.includes(datadirNeedle)) continue;

    try {
      process.kill(pid, 'SIGKILL');
      console.log(`[cgdm] Killed orphan mysqld pid=${pid}`);
    } catch (_) {}
  }
}

function startMySQL() {
  const mysqldPath = path.join(MYSQL_DIR, 'bin', 'mysqld');
  cleanupMysqlRuntimeFiles();

  if (!fs.existsSync(MYSQL_INIT_MARKER)) {
    sendStatus('Initializing database...');
    return new Promise((resolve, reject) => {
      const init = spawn(mysqldPath, [
        '--initialize-insecure',
        `--datadir=${MYSQL_DATA_DIR}`,
      ], { stdio: ['ignore', 'pipe', 'pipe'], env: mysqldEnv() });

      let stderr = '';
      init.stderr.on('data', d => { stderr += d.toString(); });
      init.on('exit', code => {
        if (code !== 0) {
          reject(new Error('mysqld --initialize-insecure failed:\n' + stderr));
        } else {
          fs.writeFileSync(MYSQL_INIT_MARKER, new Date().toISOString());
          startMysqldProcess().then(resolve).catch(reject);
        }
      });
      init.on('error', reject);
    });
  }

  return startMysqldProcess();
}

function startMysqldProcess() {
  const mysqldPath = path.join(MYSQL_DIR, 'bin', 'mysqld');
  const logErrorPath = path.join(LOG_DIR, 'mysqld.log');
  fs.writeFileSync(logErrorPath, '');

  return new Promise((resolve, reject) => {
    mysqlProcess = spawn(mysqldPath, [
      `--datadir=${MYSQL_DATA_DIR}`,
      `--socket=${MYSQL_SOCKET}`,
      `--port=${DB_PORT}`,
      `--pid-file=${MYSQL_PID_FILE}`,
      '--bind-address=127.0.0.1',
      '--character-set-server=utf8mb4',
      '--collation-server=utf8mb4_unicode_ci',
      '--mysqlx=0',
      '--log-error-verbosity=1',
      `--log-error=${logErrorPath}`,
    ], {
      stdio: ['ignore', 'pipe', 'pipe'],
      env: mysqldEnv(),
    });

    let started = false;
    const stderrChunks = [];

    mysqlProcess.stderr.on('data', d => {
      const s = d.toString();
      stderrChunks.push(s);
      if (!started && s.includes('ready for connections')) {
        started = true;
        resolve();
      }
    });

    mysqlProcess.on('error', reject);
    mysqlProcess.on('exit', code => {
      if (!isQuitting) {
        console.error(`mysqld exited unexpectedly code=${code}`);
      }
      mysqlProcess = null;
    });

    setTimeout(() => {
      if (started) return;
      let attempts = 0;
      const check = setInterval(() => {
        if (fs.existsSync(MYSQL_SOCKET)) {
          started = true;
          clearInterval(check);
          resolve();
        } else if (attempts++ > 60) {
          clearInterval(check);
          reject(new Error('MySQL did not start:\n' + stderrChunks.join('')));
        }
      }, 500);
    }, 3000);
  });
}

async function configureMySQL() {
  if (fs.existsSync(MYSQL_CONFIGURED_MARKER)) return;

  sendStatus('Configuring database...');
  const sql = [
    `ALTER USER 'root'@'localhost' IDENTIFIED BY '${MYSQL_ROOT_PASSWORD}';`,
    `CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED BY '${MYSQL_ROOT_PASSWORD}';`,
    `ALTER USER 'root'@'127.0.0.1' IDENTIFIED BY '${MYSQL_ROOT_PASSWORD}';`,
    `GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION;`,
    `CREATE DATABASE IF NOT EXISTS cdmgr CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`,
    `FLUSH PRIVILEGES;`,
  ].join('\n');

  try {
    await runMysql(['-e', sql]);
  } catch (err) {
    await runMysql(['-e', sql], [`-p${MYSQL_ROOT_PASSWORD}`]);
  }

  fs.writeFileSync(MYSQL_CONFIGURED_MARKER, new Date().toISOString());
}

function stopMySQL() {
  return new Promise(resolve => {
    let done = false;
    const finish = () => {
      if (done) return;
      done = true;
      mysqlProcess = null;
      killEmbeddedMysqldProcesses();
      cleanupMysqlRuntimeFiles();
      cleanupLegacyMysqlRuntimeFiles();
      resolve();
    };

    if (!mysqlProcess) {
      finish();
      return;
    }

    runMysqlAdmin(['-p' + MYSQL_ROOT_PASSWORD, 'shutdown']).then(finish).catch(() => {
      runMysqlAdmin(['shutdown']).then(finish).catch(() => {
        if (mysqlProcess) mysqlProcess.kill('SIGTERM');
        finish();
      });
    });

    setTimeout(() => {
      if (mysqlProcess) mysqlProcess.kill('SIGKILL');
      finish();
    }, 15000);
  });
}

// ---------------------------------------------------------------------------
// Java backend lifecycle
// ---------------------------------------------------------------------------

function startJavaBackend() {
  const javaCmd = ensureJavaAvailable();
  let classpath;

  try {
    classpath = findPlexusClassworldsJar();
  } catch (e) {
    return Promise.reject(e);
  }

  return new Promise((resolve, reject) => {
    const javaLogPath = path.join(LOG_DIR, 'java.log');
    const javaLog = fs.createWriteStream(javaLogPath, { flags: 'a' });
    javaLog.write(`\n[cgdm] === startup ${new Date().toISOString()} ===\n`);
    javaLog.write(`[cgdm] Java cmd: ${javaCmd}\n`);
    javaLog.write(`[cgdm] app.home: ${RUNTIME_DIR}\n`);
    javaLog.write(`[cgdm] JDBC: ${buildJdbcUrl()}\n`);

    const args = [
      '-server',
      '-Xms1024m', '-Xmx2048m',
      '-XX:+HeapDumpOnOutOfMemoryError',
      '-XX:-OmitStackTraceInFastThrow',
      '-XX:+DisableExplicitGC',
      `-Dapp.logPath=${LOG_DIR}`,
      `-DJM.LOG.PATH=${LOG_DIR}`,
      `-Dapp.data=${path.join(USER_DATA_DIR, 'data')}`,
      `-Dapp.home=${RUNTIME_DIR}`,
      '-Djava.net.preferIPv4Stack=true',
      '-Dfile.encoding=UTF-8',
      '-DdriverDiscovery=false',
      `-Dclassworlds.conf=${path.join(BACKEND_DIR, 'bin', 'app.conf')}`,
      `-Dspring.config.location=optional:classpath:/,optional:file:${CONF_DIR}/`,
      '-classpath', classpath,
      'org.codehaus.plexus.classworlds.launcher.Launcher',
      'start',
      `--server.port=${APP_WEB_PORT}`,
    ];

    console.log('[cgdm] Starting Java backend...');
    javaProcess = spawn(javaCmd, args, {
      cwd: RUNTIME_DIR,
      stdio: ['ignore', 'pipe', 'pipe'],
    });

    let settled = false;

    const fail = (err) => {
      if (!settled) {
        settled = true;
        javaLog.end();
        reject(err);
      }
    };
    const done = () => {
      if (!settled) {
        settled = true;
        resolve();
      }
    };

    javaProcess.stdout.on('data', d => {
      const s = d.toString();
      process.stdout.write(`[java] ${s}`);
      javaLog.write(s);
    });
    javaProcess.stderr.on('data', d => {
      const s = d.toString();
      process.stderr.write(`[java] ${s}`);
      javaLog.write(`[stderr] ${s}`);
    });
    javaProcess.on('error', err => {
      javaLog.write(`[cgdm] Java spawn error: ${err.message}\n`);
      fail(err);
    });
    javaProcess.on('exit', code => {
      javaLog.write(`[cgdm] Java exited code=${code}\n`);
      if (!isQuitting) {
        console.error(`Java exited unexpectedly code=${code}`);
      }
      javaProcess = null;
      if (!settled) {
        fail(new Error('Java process exited immediately. Check logs at ' + javaLogPath));
      }
    });

    setTimeout(done, 3000);
  });
}

function stopJavaBackend() {
  return new Promise(resolve => {
    if (!javaProcess) return resolve();

    let classpath;
    try {
      classpath = findPlexusClassworldsJar();
    } catch (_e) {
      if (javaProcess) javaProcess.kill('SIGTERM');
      javaProcess = null;
      return resolve();
    }

    const javaCmd = findJava();
    const stop = spawn(javaCmd, [
      '-classpath', classpath,
      `-Dclassworlds.conf=${path.join(BACKEND_DIR, 'bin', 'app.conf')}`,
      `-Dapp.home=${RUNTIME_DIR}`,
      'org.codehaus.plexus.classworlds.launcher.Launcher',
      'stop',
    ], { stdio: 'ignore', timeout: 15000 });

    stop.on('exit', () => {
      javaProcess = null;
      resolve();
    });
    stop.on('error', () => {
      if (javaProcess) javaProcess.kill('SIGTERM');
      javaProcess = null;
      resolve();
    });
    setTimeout(() => {
      if (javaProcess) javaProcess.kill('SIGKILL');
      javaProcess = null;
      resolve();
    }, 20000);
  });
}

// ---------------------------------------------------------------------------
// Window
// ---------------------------------------------------------------------------

function createLoadingWindow() {
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 800,
    minWidth: 1024,
    minHeight: 768,
    show: false,
    title: 'CloudDM',
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
    },
  });

  mainWindow.loadFile(path.join(__dirname, 'loading.html'));

  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
  });

  mainWindow.on('close', e => {
    if (!isQuitting) {
      e.preventDefault();
      mainWindow.hide();
      startShutdown();
    }
  });
}

// ---------------------------------------------------------------------------
// Shutdown
// ---------------------------------------------------------------------------

async function startShutdown() {
  if (isQuitting) return;
  isQuitting = true;
  sendStatus('Shutting down...');
  console.log('[cgdm] Shutting down...');
  await stopJavaBackend();
  await stopMySQL();
  app.quit();
}

// ---------------------------------------------------------------------------
// App lifecycle
// ---------------------------------------------------------------------------

function cleanupOrphanProcesses() {
  const { execSync } = require('child_process');

  killEmbeddedMysqldProcesses();
  cleanupMysqlRuntimeFiles();
  cleanupLegacyMysqlRuntimeFiles();

  for (const port of [APP_WEB_PORT, DB_PORT, RSOCKET_PORT]) {
    try {
      execSync(`lsof -ti :${port} | xargs kill -9 2>/dev/null`, { timeout: 3000 });
    } catch (_) {}
  }

  try {
    execSync(`pkill -9 -f "${BACKEND_DIR}.*plexus-classworlds.launcher.Launcher"`, { timeout: 3000 });
  } catch (_) {}
}

app.whenReady().then(async () => {
  cleanupOrphanProcesses();
  createLoadingWindow();

  try {
    prepareRuntimeLayout();
    prepareConfig();

    sendStatus('Starting database...');
    await startMySQL();
    await configureMySQL();

    sendStatus('Starting CloudDM server...');
    await startJavaBackend();

    sendStatus('Waiting for CloudDM to be ready...');
    await waitForHttp(`http://127.0.0.1:${APP_WEB_PORT}/`, 180000);

    await autoInit();

    sendStatus('Ready');
    clearRestartFlag();
    await waitForAppReady();
    await loadAppWindow();
  } catch (err) {
    console.error('[cgdm] Startup failed:', err);
    const logHint = `\n\nLogs:\n  ${path.join(LOG_DIR, 'java.log')}\n  ${path.join(LOG_DIR, 'mysqld.log')}`;
    dialog.showErrorBox('Startup Error', (err.message || String(err)) + logHint);
    app.quit();
  }
});

app.on('before-quit', async e => {
  if (!isQuitting) {
    e.preventDefault();
    await startShutdown();
  }
});

app.on('window-all-closed', () => {
  // keep running in dock
});
