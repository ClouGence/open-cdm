import {
  appendFilterCondition,
  buildChangeRowsPayload,
  buildFilterClause,
  buildOrderByString,
  computeSelectionStats,
  countChangeBreakdown,
  createNewRowModel,
  createRowModels,
  formatSelectionWithExtractor,
  getCellPlaceholder,
  isCellReadOnly,
  parseClipboardMatrix,
  resolveDatabaseLabel,
  syncTabEditTracking,
  toggleMultiColumnSort
} from '@/views/sql/components/dataEditor/dataEditorState';

describe('dataEditorState', () => {
  const columns = [
    { column: 'id', columnType: 'BIGINT', isPk: true, autoincrement: true, hasDefault: false, insertReadOnly: false, updateReadOnly: true },
    { column: 'label', columnType: 'VARCHAR', isPk: false, autoincrement: false, hasDefault: true, insertReadOnly: false, updateReadOnly: false },
    { column: 'gen_col', columnType: 'VARCHAR', isPk: false, autoincrement: false, hasDefault: false, insertReadOnly: true, updateReadOnly: true },
    { column: 'amount', columnType: 'INT', isPk: false, autoincrement: false, hasDefault: false, insertReadOnly: false, updateReadOnly: false }
  ];

  test('all-default new row omits autoincrement, default, and insertReadOnly columns and sends dmlType INSERT', () => {
    const newRow = createNewRowModel(columns);
    expect(getCellPlaceholder(newRow, columns[0], newRow.values.id)).toBe('<AUTO>');
    expect(getCellPlaceholder(newRow, columns[1], newRow.values.label)).toBe('<DEFAULT>');
    expect(getCellPlaceholder(newRow, columns[3], newRow.values.amount)).toBe('<NULL>');

    const { changeRows } = buildChangeRowsPayload([newRow], columns, ['id']);
    expect(changeRows).toHaveLength(1);
    expect(changeRows[0].dmlType).toBe('INSERT');
    expect(changeRows[0].newData).toEqual({ amount: null });
    expect(Object.prototype.hasOwnProperty.call(changeRows[0].newData, 'id')).toBe(false);
    expect(Object.prototype.hasOwnProperty.call(changeRows[0].newData, 'label')).toBe(false);
    expect(Object.prototype.hasOwnProperty.call(changeRows[0].newData, 'gen_col')).toBe(false);
  });

  test('explicit NULL on default column is sent as null and displays <NULL>, empty string stays empty string', () => {
    const rowNull = createNewRowModel(columns);
    rowNull.explicitNull.label = true;
    expect(getCellPlaceholder(rowNull, columns[1], rowNull.values.label)).toBe('<NULL>');

    const rowEmpty = createNewRowModel(columns);
    rowEmpty.values.label = '';

    const { changeRows } = buildChangeRowsPayload([rowNull, rowEmpty], columns, ['id']);
    expect(changeRows[0].newData).toEqual({ label: null, amount: null });
    expect(changeRows[1].newData).toEqual({ label: '', amount: null });
  });

  test('copied row preserves NULL as explicitNull for non-autoincrement columns', () => {
    const existing = createRowModels([{ id: '1', label: null, gen_col: 'x', amount: '10' }], [{}])[0];
    const copied = createNewRowModel(columns, existing);
    expect(copied.explicitNull.label).toBe(true);
    expect(getCellPlaceholder(copied, columns[1], copied.values.label)).toBe('<NULL>');

    const { changeRows } = buildChangeRowsPayload([copied], columns, ['id']);
    expect(changeRows[0].newData).toEqual({ id: '1', label: null, amount: '10' });
  });

  test('update, delete, and breakdown counts work accurately', () => {
    const rows = createRowModels(
      [
        { id: '1', label: 'old', gen_col: 'x', amount: '10' },
        { id: '2', label: 'del', gen_col: 'y', amount: '20' }
      ],
      [{}, {}]
    );
    rows[0].values.label = 'new';
    rows[0].dirtyFields.label = true;
    rows[1].markedDelete = true;
    rows.push(createNewRowModel(columns));

    expect(countChangeBreakdown(rows)).toEqual({ added: 1, modified: 1, deleted: 1, total: 3 });

    const tab = { addRows: [], deleteRows: [], updateCellList: [] };
    syncTabEditTracking(tab, rows);
    expect(tab.addRows).toEqual([2]);
    expect(tab.deleteRows).toEqual([1]);
    expect(tab.updateCellList[0]).toEqual({ label: true });

    const { changeRows } = buildChangeRowsPayload(rows, columns, ['id']);
    expect(changeRows).toHaveLength(3);
    expect(changeRows[0]).toMatchObject({ dmlType: 'UPDATE', whereData: { id: '1' }, newData: { label: 'new' } });
    expect(changeRows[1]).toMatchObject({ dmlType: 'DELETE', whereData: { id: '2' } });
    expect(changeRows[2]).toMatchObject({ dmlType: 'INSERT' });
  });

  test('readOnly and resolveDatabaseLabel handle MySQL/Oracle/PG nodes safely', () => {
    const existing = createRowModels([{ id: '1', label: 'a' }], [{}])[0];
    expect(isCellReadOnly(existing, columns[0], false)).toBe(true);
    expect(isCellReadOnly(existing, columns[1], false)).toBe(false);
    expect(isCellReadOnly(existing, columns[1], true)).toBe(true);

    expect(resolveDatabaseLabel({ SCHEMA: { name: 'public' }, CATALOG: { name: 'mydb' } })).toBe('public');
    expect(resolveDatabaseLabel({ CATALOG: { name: 'mysql_db' } })).toBe('mysql_db');
    expect(resolveDatabaseLabel({ INSTANCE: { name: 'inst1' } })).toBe('inst1');
    expect(resolveDatabaseLabel(null)).toBe('');
  });

  test('Data Extractors format TSV, CSV, JSON, SQL-INSERT, SQL-UPDATE, and Markdown', () => {
    const rows = createRowModels(
      [
        { id: '1', label: "a'b", gen_col: 'g1', amount: '100' },
        { id: '2', label: null, gen_col: 'g2', amount: '200' }
      ],
      [{}, {}]
    );
    const range = { startRow: 0, endRow: 1, startCol: 0, endCol: 1 };

    expect(formatSelectionWithExtractor('tsv', rows, columns, range)).toBe("1\ta'b\n2\t");
    expect(formatSelectionWithExtractor('csv', rows, columns, range)).toBe("id,label\n1,a'b\n2,");
    expect(JSON.parse(formatSelectionWithExtractor('json', rows, columns, range))).toEqual([
      { id: '1', label: "a'b" },
      { id: '2', label: null }
    ]);
    expect(formatSelectionWithExtractor('sql-insert', rows, columns, range, { tableName: 't_demo' })).toBe(
      "INSERT INTO t_demo (id, label) VALUES (1, 'a''b');\nINSERT INTO t_demo (id, label) VALUES (2, NULL);"
    );
    expect(
      formatSelectionWithExtractor(
        'sql-update',
        rows,
        columns,
        { startRow: 0, endRow: 0, startCol: 1, endCol: 1 },
        { tableName: 't_demo', whereKeyList: ['id'] }
      )
    ).toBe("UPDATE t_demo SET label = 'a''b' WHERE id = 1;");
    expect(formatSelectionWithExtractor('markdown', rows, columns, range)).toContain('| id | label |');
  });

  test('Filter clause builder, multi-column sort, clipboard matrix parser, and selection stats', () => {
    expect(buildFilterClause(columns[0], '42', '=')).toBe('id = 42');
    expect(buildFilterClause(columns[1], null, '=')).toBe('label IS NULL');
    expect(buildFilterClause(columns[1], null, '!=')).toBe('label IS NOT NULL');
    expect(appendFilterCondition('id > 10', "label = 'ok'")).toBe("(id > 10) AND label = 'ok'");

    let sorts = toggleMultiColumnSort([], 'id', false);
    sorts = toggleMultiColumnSort(sorts, 'amount', true);
    expect(buildOrderByString(sorts)).toBe('id asc, amount asc');
    sorts = toggleMultiColumnSort(sorts, 'id', true);
    expect(buildOrderByString(sorts)).toBe('id desc, amount asc');

    expect(parseClipboardMatrix('a\tb\r\nc\td\n')).toEqual([
      ['a', 'b'],
      ['c', 'd']
    ]);

    const rows = createRowModels(
      [
        { id: '1', label: 'a', gen_col: 'g', amount: '10' },
        { id: '2', label: 'b', gen_col: 'g', amount: '30' }
      ],
      [{}, {}]
    );
    const stats = computeSelectionStats(rows, columns, { startRow: 0, endRow: 1, startCol: 3, endCol: 3 });
    expect(stats).toEqual({
      rowCount: 2,
      colCount: 1,
      cellCount: 2,
      nonNullCount: 2,
      numericCount: 2,
      sum: 40,
      avg: 20,
      min: 10,
      max: 30
    });
  });
});
