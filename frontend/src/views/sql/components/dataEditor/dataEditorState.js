import { cloneDeep as deepClone } from '@/utils/lodash';

export const DATA_EXTRACTORS = [
  { id: 'tsv', label: 'TSV' },
  { id: 'csv', label: 'CSV' },
  { id: 'json', label: 'JSON' },
  { id: 'sql-insert', label: 'SQL-Insert-Statements' },
  { id: 'sql-update', label: 'SQL-Update-Statements' },
  { id: 'markdown', label: 'Markdown' }
];

/**
 * Safely resolves a display label for the current database/schema node across all datasource families.
 */
export function resolveDatabaseLabel(node) {
  if (!node) return '';
  return node.SCHEMA?.name || node.CATALOG?.name || node.INSTANCE?.name || '';
}

/**
 * Returns whether a specific cell is read-only.
 */
export function isCellReadOnly(rowState, column, tableReadOnly) {
  if (tableReadOnly || !rowState || !column) return true;
  if (rowState.markedDelete) return true;
  if (rowState.isNew) {
    return Boolean(column.insertReadOnly);
  }
  return Boolean(column.updateReadOnly);
}

/**
 * Returns the database placeholder text for null cells, or null if the cell has a concrete value.
 */
export function getCellPlaceholder(rowState, column, value) {
  if (value !== null && value !== undefined) return null;
  if (rowState?.isNew && !rowState.explicitNull?.[column.column]) {
    if (column.autoincrement) return '<AUTO>';
    if (column.hasDefault) return '<DEFAULT>';
  }
  return '<NULL>';
}

/**
 * Computes reasonable initial column pixel widths based on column names, types, and sample row values.
 */
export function computeColumnWidths(columns, resultSet = []) {
  const widths = {};
  const sampleRows = resultSet.slice(0, 60);
  columns.forEach((col) => {
    let maxChars = Math.max((col.column || '').length + 5, (col.columnType || '').length + 2);
    sampleRows.forEach((row) => {
      const val = row?.[col.column];
      if (val !== null && val !== undefined) {
        const len = String(val).length;
        if (len > maxChars) {
          maxChars = len;
        }
      }
    });
    widths[col.column] = Math.min(Math.max(maxChars * 8.5 + 48, 120), 360);
  });
  return widths;
}

/**
 * Computes auto-fit pixel width for a single column (triggered by double-clicking resize handle).
 */
export function computeAutoFitColumnWidth(column, rows = []) {
  if (!column) return 140;
  const colName = column.column;
  let maxChars = Math.max((colName || '').length + 6, (column.columnType || '').length + 2);
  rows.slice(0, 500).forEach((row) => {
    const val = row?.values?.[colName];
    if (val !== null && val !== undefined) {
      const firstLine = String(val).split('\n')[0];
      if (firstLine.length > maxChars) {
        maxChars = firstLine.length;
      }
    }
  });
  return Math.min(Math.max(Math.round(maxChars * 8.5 + 48), 100), 600);
}

/**
 * Builds editable row models from raw backend resultSet + resultSetMore.
 */
export function createRowModels(resultSet = [], resultSetMore = []) {
  return resultSet.map((row, idx) => ({
    uid: `row_${idx}_${Date.now()}_${Math.random().toString(36).slice(2, 6)}`,
    isNew: false,
    markedDelete: false,
    originalValues: deepClone(row || {}),
    values: deepClone(row || {}),
    more: deepClone(resultSetMore?.[idx] || {}),
    dirtyFields: {},
    explicitNull: {}
  }));
}

/**
 * Creates a brand-new row model (empty or copied from an existing row).
 */
export function createNewRowModel(columns = [], sourceRow = null) {
  const values = {};
  const explicitNull = {};
  columns.forEach((col) => {
    const colName = col.column;
    if (sourceRow) {
      const copiedVal = sourceRow.values[colName];
      values[colName] = copiedVal === undefined ? null : copiedVal;
      if (values[colName] === null && !col.autoincrement) {
        explicitNull[colName] = true;
      }
    } else {
      values[colName] = null;
    }
  });
  return {
    uid: `new_${Date.now()}_${Math.random().toString(36).slice(2, 7)}`,
    isNew: true,
    markedDelete: false,
    originalValues: {},
    values,
    more: {},
    dirtyFields: {},
    explicitNull
  };
}

/**
 * Synchronizes tab.addRows, tab.deleteRows, and tab.updateCellList so sqlMixin.dataViewIsEditing(tab)
 * works consistently on tab switch / close.
 */
export function syncTabEditTracking(tab, rows = []) {
  if (!tab) return;
  const addRows = [];
  const deleteRows = [];
  const updateCellList = [];

  rows.forEach((row, idx) => {
    if (row.isNew && !row.markedDelete) {
      addRows.push(idx);
    }
    if (row.markedDelete && !row.isNew) {
      deleteRows.push(idx);
    }
    const dirtyMap = {};
    if (!row.isNew && !row.markedDelete) {
      Object.keys(row.dirtyFields || {}).forEach((colName) => {
        if (row.dirtyFields[colName]) {
          dirtyMap[colName] = true;
        }
      });
    }
    updateCellList[idx] = dirtyMap;
  });

  tab.addRows = addRows;
  tab.deleteRows = deleteRows;
  tab.updateCellList = updateCellList;
}

/**
 * Counts breakdown of pending row changes: added, modified, deleted, and total.
 */
export function countChangeBreakdown(rows = []) {
  let added = 0;
  let modified = 0;
  let deleted = 0;
  rows.forEach((row) => {
    if (row.isNew && !row.markedDelete) {
      added += 1;
    } else if (!row.isNew && row.markedDelete) {
      deleted += 1;
    } else if (!row.isNew && Object.values(row.dirtyFields || {}).some(Boolean)) {
      modified += 1;
    }
  });
  return {
    added,
    modified,
    deleted,
    total: added + modified + deleted
  };
}

/**
 * Counts total pending row operations (inserts + updates + deletes).
 */
export function countPendingChanges(rows = []) {
  return countChangeBreakdown(rows).total;
}

/**
 * Builds the generateDml / saveData request payload adhering strictly to the data editor contract.
 */
export function buildChangeRowsPayload(rows = [], visibleColumns = [], whereKeyList = []) {
  const changeRows = [];
  const renewData = {};

  rows.forEach((row, rowIndex) => {
    if (row.isNew) {
      if (row.markedDelete) return;
      const newData = {};
      visibleColumns.forEach((col) => {
        const colName = col.column;
        const val = row.values[colName];
        const isExplicitNull = Boolean(row.explicitNull?.[colName]);
        if (col.insertReadOnly) return;
        if ((col.hasDefault || col.autoincrement) && val === null && !isExplicitNull) {
          return;
        }
        newData[colName] = val === undefined ? null : val;
      });
      const item = {
        newData,
        sequence: rowIndex,
        type: 'createParam',
        dmlType: 'INSERT'
      };
      changeRows.push(item);
      renewData[rowIndex] = item;
      return;
    }

    if (row.markedDelete) {
      const whereData = {};
      whereKeyList.forEach((key) => {
        whereData[key] = row.originalValues[key];
      });
      const item = {
        whereData,
        sequence: rowIndex,
        type: 'deleteParam',
        dmlType: 'DELETE'
      };
      changeRows.push(item);
      renewData[rowIndex] = item;
      return;
    }

    const updateData = {};
    visibleColumns.forEach((col) => {
      const colName = col.column;
      if (row.dirtyFields?.[colName]) {
        updateData[colName] = row.values[colName] === undefined ? null : row.values[colName];
      }
    });

    if (Object.keys(updateData).length > 0) {
      const whereData = {};
      whereKeyList.forEach((key) => {
        whereData[key] = row.originalValues[key];
      });
      const item = {
        whereData,
        newData: updateData,
        sequence: rowIndex,
        type: 'updateParam',
        dmlType: 'UPDATE'
      };
      changeRows.push(item);
      renewData[rowIndex] = item;
    }
  });

  return { changeRows, renewData };
}

/**
 * Formats a SQL literal for client-side Data Extractor (SQL INSERT / UPDATE / Filter clause).
 */
export function formatSqlLiteral(value, columnType = '') {
  if (value === null || value === undefined) return 'NULL';
  const str = String(value);
  const upperType = String(columnType || '').toUpperCase();
  const isNumericType = /(INT|DECIMAL|NUMERIC|FLOAT|DOUBLE|REAL|NUMBER|BIGINT|SMALLINT|TINYINT)/.test(upperType);
  if (isNumericType && /^-?\d+(\.\d+)?$/.test(str.trim())) {
    return str.trim();
  }
  return `'${str.replace(/'/g, "''")}'`;
}

/**
 * Builds a WHERE filter clause from a cell value (DataGrip "Filter by" / "Exclude").
 */
export function buildFilterClause(column, value, operator = '=') {
  const colName = typeof column === 'string' ? column : column?.column;
  const colType = typeof column === 'string' ? '' : column?.columnType || '';
  if (!colName) return '';
  if (value === null || value === undefined) {
    return operator === '=' ? `${colName} IS NULL` : `${colName} IS NOT NULL`;
  }
  return `${colName} ${operator} ${formatSqlLiteral(value, colType)}`;
}

/**
 * Appends a filter clause to an existing WHERE condition using AND.
 */
export function appendFilterCondition(existingCondition, clause) {
  if (!clause) return existingCondition || '';
  const trimmed = String(existingCondition || '').trim();
  if (!trimmed) return clause;
  return `(${trimmed}) AND ${clause}`;
}

/**
 * Toggles sorting for a column, supporting single-column and Shift+Click multi-column sorting.
 */
export function toggleMultiColumnSort(sortList = [], colName, multi = false) {
  const next = multi ? sortList.map((s) => ({ ...s })) : [];
  const existingIdx = sortList.findIndex((s) => s.column === colName);
  const currentDir = existingIdx >= 0 ? sortList[existingIdx].sortBy : '';
  const nextDir = currentDir === '' ? 'asc' : currentDir === 'asc' ? 'desc' : '';

  if (multi) {
    if (existingIdx >= 0) {
      if (nextDir) {
        next[existingIdx].sortBy = nextDir;
      } else {
        next.splice(existingIdx, 1);
      }
    } else if (nextDir) {
      next.push({ column: colName, sortBy: nextDir });
    }
    return next;
  }

  return nextDir ? [{ column: colName, sortBy: nextDir }] : [];
}

/**
 * Builds an ORDER BY clause string from a list of { column, sortBy } entries.
 */
export function buildOrderByString(sortList = []) {
  return sortList
    .filter((s) => s && s.column && s.sortBy)
    .map((s) => `${s.column} ${s.sortBy}`)
    .join(', ');
}

function escapeCsvField(val) {
  if (val === null || val === undefined) return '';
  const str = String(val);
  if (/[",\r\n]/.test(str)) {
    return `"${str.replace(/"/g, '""')}"`;
  }
  return str;
}

/**
 * Formats a selected rectangular range (or full page) using the chosen DataGrip Data Extractor.
 */
export function formatSelectionWithExtractor(
  extractorId,
  rows = [],
  columns = [],
  range = null,
  { tableName = 'table_name', whereKeyList = [] } = {}
) {
  if (!rows.length || !columns.length) return '';
  const r1 = range ? Math.max(0, range.startRow) : 0;
  const r2 = range ? Math.min(rows.length - 1, range.endRow) : rows.length - 1;
  const c1 = range ? Math.max(0, range.startCol) : 0;
  const c2 = range ? Math.min(columns.length - 1, range.endCol) : columns.length - 1;

  const targetRows = rows.slice(r1, r2 + 1);
  const targetCols = columns.slice(c1, c2 + 1);

  switch (extractorId) {
    case 'csv': {
      const header = targetCols.map((c) => escapeCsvField(c.column)).join(',');
      const body = targetRows.map((row) => targetCols.map((c) => escapeCsvField(row.values[c.column])).join(','));
      return [header, ...body].join('\n');
    }
    case 'json': {
      const arr = targetRows.map((row) => {
        const obj = {};
        targetCols.forEach((c) => {
          obj[c.column] = row.values[c.column] === undefined ? null : row.values[c.column];
        });
        return obj;
      });
      return JSON.stringify(arr, null, 2);
    }
    case 'sql-insert': {
      const colNames = targetCols.map((c) => c.column).join(', ');
      return targetRows
        .map((row) => {
          const vals = targetCols.map((c) => formatSqlLiteral(row.values[c.column], c.columnType)).join(', ');
          return `INSERT INTO ${tableName} (${colNames}) VALUES (${vals});`;
        })
        .join('\n');
    }
    case 'sql-update': {
      const keys = whereKeyList.length ? whereKeyList : [columns[0]?.column].filter(Boolean);
      return targetRows
        .map((row) => {
          const sets = targetCols.map((c) => `${c.column} = ${formatSqlLiteral(row.values[c.column], c.columnType)}`).join(', ');
          const wheres = keys
            .map((k) => {
              const colObj = columns.find((c) => c.column === k);
              return buildFilterClause(colObj || k, row.originalValues?.[k] ?? row.values?.[k], '=');
            })
            .join(' AND ');
          return `UPDATE ${tableName} SET ${sets} WHERE ${wheres};`;
        })
        .join('\n');
    }
    case 'markdown': {
      const escMd = (v) => (v === null || v === undefined ? 'NULL' : String(v).replace(/\|/g, '\\|').replace(/\r?\n/g, ' '));
      const header = `| ${targetCols.map((c) => escMd(c.column)).join(' | ')} |`;
      const sep = `| ${targetCols.map(() => '---').join(' | ')} |`;
      const body = targetRows.map((row) => `| ${targetCols.map((c) => escMd(row.values[c.column])).join(' | ')} |`);
      return [header, sep, ...body].join('\n');
    }
    case 'tsv':
    default: {
      return targetRows
        .map((row) =>
          targetCols
            .map((c) => {
              const v = row.values[c.column];
              return v === null || v === undefined ? '' : String(v);
            })
            .join('\t')
        )
        .join('\n');
    }
  }
}

/**
 * Parses clipboard text into a 2D matrix of strings for single-cell or TSV matrix pasting.
 */
export function parseClipboardMatrix(text) {
  if (typeof text !== 'string') return [];
  const normalized = text.replace(/\r\n/g, '\n').replace(/\r/g, '\n');
  const trimmed = normalized.endsWith('\n') ? normalized.slice(0, -1) : normalized;
  if (!trimmed.length) return [['']];
  return trimmed.split('\n').map((line) => line.split('\t'));
}

/**
 * Computes DataGrip-style selection statistics (dimensions + numeric Sum/Avg/Min/Max).
 */
export function computeSelectionStats(rows = [], columns = [], range = null) {
  if (!range || !rows.length || !columns.length) {
    return null;
  }
  const r1 = Math.max(0, range.startRow);
  const r2 = Math.min(rows.length - 1, range.endRow);
  const c1 = Math.max(0, range.startCol);
  const c2 = Math.min(columns.length - 1, range.endCol);
  if (r1 > r2 || c1 > c2) return null;

  const rowCount = r2 - r1 + 1;
  const colCount = c2 - c1 + 1;
  const cellCount = rowCount * colCount;
  let nonNullCount = 0;
  let numericCount = 0;
  let sum = 0;
  let min = Infinity;
  let max = -Infinity;

  for (let r = r1; r <= r2; r++) {
    const row = rows[r];
    if (!row) continue;
    for (let c = c1; c <= c2; c++) {
      const col = columns[c];
      if (!col) continue;
      const val = row.values[col.column];
      if (val !== null && val !== undefined && val !== '') {
        nonNullCount += 1;
        const num = Number(val);
        if (Number.isFinite(num)) {
          numericCount += 1;
          sum += num;
          if (num < min) min = num;
          if (num > max) max = num;
        }
      }
    }
  }

  return {
    rowCount,
    colCount,
    cellCount,
    nonNullCount,
    numericCount,
    sum: numericCount > 0 ? Number(sum.toFixed(6)) : null,
    avg: numericCount > 0 ? Number((sum / numericCount).toFixed(4)) : null,
    min: numericCount > 0 ? min : null,
    max: numericCount > 0 ? max : null
  };
}
