import { Legacy_Token } from './token';
/**
 * @param {String} sql
 * @deprecated use parser.createLexer() instead.
 */
declare function legacy_lexer(input: string): Legacy_Token[];
/**
 * split sql
 * @param {String} sql
 * @deprecated use parser.splitSQLByStatement() instead.
 */
declare function legacy_splitSql(sql: string): any[];
/**
 * clean comment
 * @param {String} sql
 * @deprecated will be removed in future.
 */
declare function legacy_cleanSql(sql: string): string;
export { legacy_cleanSql, legacy_splitSql, legacy_lexer };
