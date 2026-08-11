import { formatSqlFileSize, validateSqlFiles } from '../../src/components/function/sqlFileUpload';

function sqlFile(name, size) {
  return { name, size };
}

describe('SQL file upload helpers', () => {
  it('rejects multiple SQL files', () => {
    const first = sqlFile('first.sql', 128);
    const second = sqlFile('second.sql', 256);

    expect(validateSqlFiles([first, second], { maxMegaByte: 20 })).toEqual({
      files: [],
      errorKey: 'ticket-sql-single-file-only'
    });
  });

  it('rejects non-SQL, empty and oversized files', () => {
    expect(validateSqlFiles([sqlFile('notes.txt', 1)])).toMatchObject({ errorKey: 'ticket-sql-only-sql' });
    expect(validateSqlFiles([sqlFile('empty.sql', 0)])).toMatchObject({ errorKey: 'sql-wen-jian-nei-rong-wei-kong' });
    expect(validateSqlFiles([sqlFile('large.sql', 2 * 1024 * 1024)], { maxMegaByte: 1 })).toEqual({
      files: [],
      errorKey: 'ticket-sql-upload-limit',
      errorParams: { size: 1 }
    });
  });

  it('formats selected file sizes consistently', () => {
    expect(formatSqlFileSize(12)).toBe('12 B');
    expect(formatSqlFileSize(1536)).toBe('1.5 KB');
    expect(formatSqlFileSize(1572864)).toBe('1.5 MB');
  });
});
