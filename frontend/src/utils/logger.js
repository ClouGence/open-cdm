const write = (method, args) => {
  if (typeof window === 'undefined') {
    return;
  }

  const browserConsole = window.console;
  if (browserConsole && typeof browserConsole[method] === 'function') {
    browserConsole[method](...args);
  }
};

const logger = Object.freeze({
  debug(...args) {
    if (process.env.NODE_ENV === 'development') {
      write('log', args);
    }
  },
  error(...args) {
    write('error', args);
  },
  warn(...args) {
    write('warn', args);
  }
});

export default logger;
