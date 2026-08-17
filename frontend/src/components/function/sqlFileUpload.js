export function formatSqlFileSize(size) {
  if (size < 1024) {
    return `${size} B`;
  }
  if (size < 1024 * 1024) {
    return `${(size / 1024).toFixed(1)} KB`;
  }
  return `${(size / 1024 / 1024).toFixed(1)} MB`;
}

export function validateSqlFiles(fileList, options = {}) {
  const files = Array.from(fileList || []);
  const selectedFiles = files.slice(0, 1);
  const maxMegaByte = options.maxMegaByte || 20;
  const maxBytes = maxMegaByte * 1024 * 1024;

  if (files.length > 1) {
    return { files: [], errorKey: 'ticket-sql-single-file-only' };
  }
  if (selectedFiles.length === 0) {
    return { files: [], errorKey: 'ticket-sql-select-file' };
  }

  for (const file of selectedFiles) {
    if (!file?.name?.toLowerCase().endsWith('.sql')) {
      return { files: [], errorKey: 'ticket-sql-only-sql' };
    }
    if (file.size === 0) {
      return { files: [], errorKey: 'sql-wen-jian-nei-rong-wei-kong' };
    }
    if (file.size > maxBytes) {
      return {
        files: [],
        errorKey: 'ticket-sql-upload-limit',
        errorParams: { size: maxMegaByte }
      };
    }
  }

  return { files: selectedFiles };
}
