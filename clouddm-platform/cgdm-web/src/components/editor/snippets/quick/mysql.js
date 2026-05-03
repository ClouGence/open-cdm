/* eslint-disable no-template-curly-in-string */
export const MYSQL_QUICK = [
  {
    label: 'CREATE TABLE',
    insertText: 'CREATE TABLE ${1: `table_name`} (\n' + '${2: `column_name` INT}' + '\n);',
    detail: 'CREATE TABLE `table_name` ( `column_name` datatype ...);'
  },
  {
    label: 'DROP TABLE',
    insertText: 'DROP TABLE IF EXISTS ${1: `table_name`};',
    detail: 'DROP TABLE IF EXISTS `table_name`;'
  },
  {
    label: 'INSERT INTO',
    insertText: 'INSERT INTO ${1: `table_name`} (${2: `column_name`}) \n' + 'VALUES (${3: value});',
    detail: 'INSERT INTO table_name (`column_name` ...) VALUES (value ...);'
  },
  {
    label: 'SELECT',
    insertText: 'SELECT ${1: *} FROM ${2: `table_name`};',
    detail: 'SELECT `column_name` FROM `table_name`;'
  },
  {
    label: 'SELECT COUNT',
    insertText: 'SELECT count(${1: *}) FROM ${2: `table_name`};',
    detail: 'SELECT count(*) FROM `table_name`'
  },
  {
    label: 'SELECT LIMIT',
    insertText: 'SELECT ${1: *} FROM ${2: `table_name`} LIMIT ${3: 20};',
    detail: 'SELECT `column_name` FROM `table_name` [LIMIT number];'
  },
  {
    label: 'SELECT WHERE',
    insertText: 'SELECT ${1: *} FROM ${2: `table_name`} \nWHERE ${3: `column_name`} = ${4: value};',
    detail: 'SELECT `column_name` FROM `table_name` [WHERE condition];'
  },
  {
    label: 'SELECT ORDER BY',
    insertText: 'SELECT ${1: *} \nFROM ${2: `table_name`} \nORDER BY ${3: `column_name`} ${4: ASC};',
    detail: 'SELECT `column_name` FROM `table_name` [ORDER BY `column_name` [ASC | DESC];'
  },
  {
    label: 'ADD COLUMN',
    insertText: 'ALTER TABLE ${1: `table_name`} ADD COLUMN ${2: `column_name`} ${3: INT};',
    detail: 'ALTER TABLE `table_name` ADD COLUMN `column_name` datatype;'
  },
  {
    label: 'DELETE COLUMN',
    insertText: 'ALTER TABLE ${1: `table_name`} DROP ${2: `new_table_name`};',
    detail: 'ALTER TABLE `table_name` DROP `new_table_name`;'
  },
  {
    label: 'RENAME COLUMN',
    insertText: 'ALTER TABLE ${1: `table_name`} RENAME ${2: `new_table_name`};',
    detail: 'ALTER TABLE `table_name` RENAME `new_table_name`;'
  },
  {
    label: 'DELETE ROW',
    insertText: 'DELETE FROM ${1: `table_name`} WHERE ${2: `column_name`} = ${3: value};',
    detail: 'DELETE FROM `table_name` [WHERE condition];'
  },
  {
    label: 'UPDATE ROW',
    insertText: 'UPDATE ${1: `table_name`} \nSET ${2: `column_name`} = ${3: value} \nWHERE ${4: `column_name`} = ${5: value};',
    detail: 'UPDATE `table_name` SET `column_name` = value WHERE `column_name` = value;'
  }
];
