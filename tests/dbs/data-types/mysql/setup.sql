-- Recreates only the isolated cdm_data_types acceptance schema.
-- Run with a client that supports DELIMITER, such as mysql.

SET NAMES utf8mb4;
SET @cdm_saved_sql_mode = @@SESSION.sql_mode;
SET @cdm_saved_time_zone = @@SESSION.time_zone;
SET SESSION sql_mode = '';
SET SESSION time_zone = '+00:00';

DROP DATABASE IF EXISTS cdm_data_types;
CREATE DATABASE cdm_data_types
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE cdm_data_types;

CREATE TABLE cdm_dt_numeric (
  case_id VARCHAR(32) NOT NULL PRIMARY KEY,
  c_bit BIT(64) NULL,
  c_tinyint TINYINT NULL,
  c_tinyint_unsigned TINYINT UNSIGNED NULL,
  c_smallint SMALLINT NULL,
  c_smallint_unsigned SMALLINT UNSIGNED NULL,
  c_mediumint MEDIUMINT NULL,
  c_mediumint_unsigned MEDIUMINT UNSIGNED NULL,
  c_int INT NULL,
  c_integer INTEGER NULL,
  c_int_unsigned INT UNSIGNED NULL,
  c_integer_unsigned INTEGER UNSIGNED NULL,
  c_bigint BIGINT NULL,
  c_bigint_unsigned BIGINT UNSIGNED NULL,
  c_decimal DECIMAL(65,30) NULL,
  c_dec DEC(20,5) NULL,
  c_numeric NUMERIC(30,10) NULL,
  c_fixed FIXED(20,5) NULL,
  c_float FLOAT NULL,
  c_float_unsigned FLOAT UNSIGNED NULL,
  c_double DOUBLE NULL,
  c_double_precision DOUBLE PRECISION NULL,
  c_real REAL NULL
) ENGINE=InnoDB;

INSERT INTO cdm_dt_numeric (case_id) VALUES ('NULL');

INSERT INTO cdm_dt_numeric VALUES (
  'ZERO',
  b'0',
  0, 0,
  0, 0,
  0, 0,
  0, 0, 0, 0,
  0, 0,
  0, 0, 0, 0,
  0, 0,
  0, 0, 0
);

INSERT INTO cdm_dt_numeric VALUES (
  'NORMAL',
  b'101010',
  -42, 200,
  -12345, 54321,
  -456789, 12345678,
  -123456789, 123456789, 4000000000, 3000000000,
  -9007199254740993, 18014398509481987,
  12345.678901234567890123456789012345,
  123456789012345.12345,
  12345678901234567890.1234567890,
  123456789012345.54321,
  -12345.125, 12345.125,
  -1234567890.1234567,
  1234567890.7654321,
  -9876543210.125
);

INSERT INTO cdm_dt_numeric VALUES (
  'MIN',
  b'0',
  -128, 0,
  -32768, 0,
  -8388608, 0,
  -2147483648, -2147483648, 0, 0,
  -9223372036854775808, 0,
  -99999999999999999999999999999999999.999999999999999999999999999999,
  -999999999999999.99999,
  -99999999999999999999.9999999999,
  -999999999999999.99999,
  -3.4028234E+38, 0,
  -1.7976931348623155E+308,
  -1.7976931348623155E+308,
  -1.7976931348623155E+308
);

INSERT INTO cdm_dt_numeric VALUES (
  'MAX',
  b'1111111111111111111111111111111111111111111111111111111111111111',
  127, 255,
  32767, 65535,
  8388607, 16777215,
  2147483647, 2147483647, 4294967295, 4294967295,
  9223372036854775807, 18446744073709551615,
  99999999999999999999999999999999999.999999999999999999999999999999,
  999999999999999.99999,
  99999999999999999999.9999999999,
  999999999999999.99999,
  3.4028234E+38, 3.4028234E+38,
  1.7976931348623155E+308,
  1.7976931348623155E+308,
  1.7976931348623155E+308
);

CREATE TABLE cdm_dt_alias (
  serial_value SERIAL,
  case_id VARCHAR(32) NOT NULL,
  c_bool BOOL NULL,
  c_boolean BOOLEAN NULL,
  UNIQUE KEY uk_cdm_dt_alias_case (case_id)
) ENGINE=InnoDB;

INSERT INTO cdm_dt_alias (case_id, c_bool, c_boolean) VALUES
  ('ZERO', 0, 0),
  ('NORMAL', 1, -7),
  ('NULL', NULL, NULL);

CREATE TABLE cdm_dt_temporal (
  case_id VARCHAR(32) NOT NULL PRIMARY KEY,
  c_date DATE NULL,
  c_time0 TIME NULL,
  c_time6 TIME(6) NULL,
  c_datetime0 DATETIME NULL,
  c_datetime6 DATETIME(6) NULL,
  c_timestamp0 TIMESTAMP NULL,
  c_timestamp6 TIMESTAMP(6) NULL,
  c_year YEAR NULL
) ENGINE=InnoDB;

INSERT INTO cdm_dt_temporal (case_id) VALUES ('NULL');
INSERT INTO cdm_dt_temporal VALUES (
  'ZERO',
  '0000-00-00',
  '00:00:00',
  '00:00:00.000000',
  '0000-00-00 00:00:00',
  '0000-00-00 00:00:00.000000',
  '0000-00-00 00:00:00',
  '0000-00-00 00:00:00.000000',
  0
);
INSERT INTO cdm_dt_temporal VALUES (
  'NORMAL',
  '2024-02-29',
  '12:34:56',
  '12:34:56.123456',
  '2024-02-29 12:34:56',
  '2024-02-29 12:34:56.123456',
  '2024-02-29 12:34:56',
  '2024-02-29 12:34:56.123456',
  2024
);
INSERT INTO cdm_dt_temporal VALUES (
  'MIN',
  '1000-01-01',
  '-838:59:59',
  '-838:59:59.000000',
  '1000-01-01 00:00:00',
  '1000-01-01 00:00:00.000000',
  '1971-01-01 00:00:01',
  '1971-01-01 00:00:01.000000',
  1901
);
INSERT INTO cdm_dt_temporal VALUES (
  'MAX',
  '9999-12-31',
  '838:59:59',
  '838:59:59.000000',
  '9999-12-31 23:59:59',
  '9999-12-31 23:59:59.999999',
  '2038-01-19 03:14:07',
  '2038-01-19 03:14:07.499999',
  2155
);
INSERT INTO cdm_dt_temporal (
  case_id,
  c_time0,
  c_time6
) VALUES (
  'NEGATIVE_TIME',
  '-12:34:56',
  '-12:34:56.123456'
);

CREATE TABLE cdm_dt_character (
  case_id VARCHAR(32) NOT NULL PRIMARY KEY,
  c_char CHAR(16) NULL,
  c_varchar VARCHAR(1024) NULL,
  c_tinytext TINYTEXT NULL,
  c_text TEXT NULL,
  c_mediumtext MEDIUMTEXT NULL,
  c_longtext LONGTEXT NULL,
  c_enum ENUM('', 'alpha', '中文', 'emoji😀') NULL,
  c_set SET('alpha', 'beta', '中文', 'emoji😀') NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO cdm_dt_character (case_id) VALUES ('NULL');
INSERT INTO cdm_dt_character VALUES (
  'EMPTY',
  '', '', '', '', '', '', '', ''
);
INSERT INTO cdm_dt_character VALUES (
  'NORMAL',
  'trail  ',
  CONCAT('quote '' slash \\', CHAR(10), 'line2'),
  'tiny text',
  CONCAT('line1', CHAR(10), 'line2'),
  'medium text',
  'long text',
  'alpha',
  'alpha,beta'
);
INSERT INTO cdm_dt_character VALUES (
  'UNICODE',
  '中文',
  '中文😀é',
  '中文😀',
  CONCAT('组合字符 é', CHAR(10), 'emoji 😀'),
  '多语言 Καλημέρα مرحبا',
  '中文😀é',
  'emoji😀',
  '中文,emoji😀'
);
INSERT INTO cdm_dt_character VALUES (
  'BOUNDARY',
  '1234567890123456',
  REPEAT('v', 257),
  REPEAT('t', 255),
  REPEAT('x', 257),
  REPEAT('m', 1024),
  REPEAT('l', 1024),
  '中文',
  'beta'
);

CREATE TABLE cdm_dt_character_long (
  case_id VARCHAR(32) NOT NULL PRIMARY KEY,
  c_longtext LONGTEXT NOT NULL,
  expected_characters INT UNSIGNED NOT NULL,
  expected_bytes INT UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO cdm_dt_character_long VALUES
  ('CHARS_255', REPEAT('a', 255), 255, 255),
  ('CHARS_256', REPEAT('b', 256), 256, 256),
  ('CHARS_257', REPEAT('c', 257), 257, 257),
  ('ONE_MIB', REPEAT('d', 1048576), 1048576, 1048576),
  ('LIMIT_MINUS_ONE', REPEAT('e', 4194303), 4194303, 4194303),
  ('LIMIT_EXACT', REPEAT('f', 4194304), 4194304, 4194304);

CREATE TABLE cdm_dt_binary_lob (
  case_id VARCHAR(32) NOT NULL PRIMARY KEY,
  c_binary BINARY(8) NULL,
  c_varbinary VARBINARY(257) NULL,
  c_tinyblob TINYBLOB NULL,
  c_blob BLOB NULL,
  c_mediumblob MEDIUMBLOB NULL,
  c_longblob LONGBLOB NULL
) ENGINE=InnoDB;

INSERT INTO cdm_dt_binary_lob (case_id) VALUES ('NULL');
INSERT INTO cdm_dt_binary_lob VALUES (
  'EMPTY',
  X'', X'', X'', X'', X'', X''
);
INSERT INTO cdm_dt_binary_lob VALUES (
  'BYTES',
  X'00FF010203040506',
  X'00FF7F80',
  X'00FF',
  X'0001027F80FEFF',
  X'00FF',
  X'00FF'
);
INSERT INTO cdm_dt_binary_lob VALUES (
  'BOUNDARY_256',
  X'0102030405060708',
  REPEAT(UNHEX('A1'), 256),
  REPEAT(UNHEX('A2'), 255),
  REPEAT(UNHEX('A3'), 256),
  REPEAT(UNHEX('A4'), 256),
  REPEAT(UNHEX('A5'), 256)
);
INSERT INTO cdm_dt_binary_lob VALUES (
  'BOUNDARY_257',
  X'0807060504030201',
  REPEAT(UNHEX('B1'), 257),
  REPEAT(UNHEX('B2'), 255),
  REPEAT(UNHEX('B3'), 257),
  REPEAT(UNHEX('B4'), 257),
  REPEAT(UNHEX('B5'), 257)
);
INSERT INTO cdm_dt_binary_lob (
  case_id,
  c_mediumblob,
  c_longblob
) VALUES (
  'ONE_MIB',
  REPEAT(UNHEX('C4'), 1048576),
  REPEAT(UNHEX('C5'), 1048576)
);

CREATE TABLE cdm_dt_binary_long (
  case_id VARCHAR(32) NOT NULL PRIMARY KEY,
  c_longblob LONGBLOB NOT NULL,
  expected_bytes INT UNSIGNED NOT NULL
) ENGINE=InnoDB;

INSERT INTO cdm_dt_binary_long VALUES
  ('BYTES_255', REPEAT(UNHEX('D1'), 255), 255),
  ('BYTES_256', REPEAT(UNHEX('D2'), 256), 256),
  ('BYTES_257', REPEAT(UNHEX('D3'), 257), 257),
  ('ONE_MIB', REPEAT(UNHEX('D4'), 1048576), 1048576),
  ('LIMIT_MINUS_ONE', REPEAT(UNHEX('D5'), 4194303), 4194303),
  ('LIMIT_EXACT', REPEAT(UNHEX('D6'), 4194304), 4194304);

CREATE TABLE cdm_dt_spatial (
  case_id VARCHAR(32) NOT NULL PRIMARY KEY,
  c_geometry GEOMETRY NULL,
  c_point POINT NULL,
  c_linestring LINESTRING NULL,
  c_polygon POLYGON NULL,
  c_multipoint MULTIPOINT NULL,
  c_multilinestring MULTILINESTRING NULL,
  c_multipolygon MULTIPOLYGON NULL,
  c_geometrycollection GEOMETRYCOLLECTION NULL
) ENGINE=InnoDB;

INSERT INTO cdm_dt_spatial (case_id) VALUES ('NULL');
INSERT INTO cdm_dt_spatial VALUES (
  'SRID_0',
  ST_GeomFromText('POINT(1 2)', 0),
  ST_GeomFromText('POINT(1 2)', 0),
  ST_GeomFromText('LINESTRING(0 0,1 1,2 1)', 0),
  ST_GeomFromText('POLYGON((0 0,0 2,2 2,2 0,0 0))', 0),
  ST_GeomFromText('MULTIPOINT((1 1),(2 2))', 0),
  ST_GeomFromText('MULTILINESTRING((0 0,1 1),(2 2,3 3))', 0),
  ST_GeomFromText('MULTIPOLYGON(((0 0,0 1,1 1,1 0,0 0)))', 0),
  ST_GeomFromText('GEOMETRYCOLLECTION(POINT(1 1),LINESTRING(0 0,1 1))', 0)
);
INSERT INTO cdm_dt_spatial VALUES (
  'SRID_4326',
  ST_GeomFromText('POINT(30 120)', 4326),
  ST_GeomFromText('POINT(30 120)', 4326),
  ST_GeomFromText('LINESTRING(0 0,1 1,2 1)', 4326),
  ST_GeomFromText('POLYGON((0 0,0 2,2 2,2 0,0 0))', 4326),
  ST_GeomFromText('MULTIPOINT((1 1),(2 2))', 4326),
  ST_GeomFromText('MULTILINESTRING((0 0,1 1),(2 2,3 3))', 4326),
  ST_GeomFromText('MULTIPOLYGON(((0 0,0 1,1 1,1 0,0 0)))', 4326),
  ST_GeomFromText('GEOMETRYCOLLECTION(POINT(1 1),LINESTRING(0 0,1 1))', 4326)
);

DELIMITER $$
CREATE PROCEDURE cdm_setup_optional_types()
BEGIN
  DECLARE cdm_major INT DEFAULT 0;
  DECLARE cdm_minor INT DEFAULT 0;

  SET cdm_major = CAST(SUBSTRING_INDEX(VERSION(), '.', 1) AS UNSIGNED);
  SET cdm_minor = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(VERSION(), '.', 2), '.', -1) AS UNSIGNED);

  IF @@max_allowed_packet > 4194305 THEN
    INSERT INTO cdm_dt_character_long VALUES
      ('LIMIT_PLUS_ONE', REPEAT('g', 4194305), 4194305, 4194305);
    INSERT INTO cdm_dt_binary_long VALUES
      ('LIMIT_PLUS_ONE', REPEAT(UNHEX('D7'), 4194305), 4194305);
  END IF;

  IF cdm_major >= 8 THEN
    SET @cdm_sql = 'ALTER TABLE cdm_dt_spatial
      ADD COLUMN c_geomcollection GEOMCOLLECTION NULL';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;

    SET @cdm_sql = 'UPDATE cdm_dt_spatial
      SET c_geomcollection = ST_GeomFromText(''GEOMETRYCOLLECTION()'',
        CASE case_id WHEN ''SRID_4326'' THEN 4326 ELSE 0 END)
      WHERE case_id IN (''SRID_0'', ''SRID_4326'')';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;
  END IF;

  IF cdm_major > 5 OR (cdm_major = 5 AND cdm_minor >= 7) THEN
    SET @cdm_sql = 'CREATE TABLE cdm_dt_json (
      case_id VARCHAR(32) NOT NULL PRIMARY KEY,
      c_json JSON NULL
    ) ENGINE=InnoDB';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;

    SET @cdm_sql = 'INSERT INTO cdm_dt_json VALUES
      (''SQL_NULL'', NULL),
      (''JSON_NULL'', ''null''),
      (''SCALAR'', ''123.456''),
      (''OBJECT'', ''{"ascii":"alpha","number":123,"boolean":true}''),
      (''ARRAY'', ''[1,"two",null,true]''),
      (''DEEP'', ''{"l1":{"l2":{"l3":{"value":"deep"}}}}''),
      (''UNICODE'', ''{"text":"中文😀","combining":"é"}''),
      (''LONG'', CONCAT(''{"payload":"'',
        REPEAT(''j'', 1024), ''"}''))';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;
  END IF;

  IF cdm_major >= 9 THEN
    SET @cdm_sql = 'CREATE TABLE cdm_dt_vector (
      case_id VARCHAR(32) NOT NULL PRIMARY KEY,
      c_vector_1 VECTOR(1) NULL,
      c_vector_3 VECTOR(3) NULL,
      c_vector_1024 VECTOR(1024) NULL
    ) ENGINE=InnoDB';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;

    SET @cdm_sql = 'INSERT INTO cdm_dt_vector VALUES
      (''NULL'', NULL, NULL, NULL),
      (''ONE_DIMENSION'', STRING_TO_VECTOR(''[1.5]''), NULL, NULL),
      (''NORMAL'', NULL, STRING_TO_VECTOR(''[1.0,-2.5,3.25]''), NULL),
      (''LONG'', NULL, NULL,
        STRING_TO_VECTOR(CONCAT(''['', REPEAT(''1,'', 1023), ''1]'')))';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;
  END IF;
END$$
DELIMITER ;

CALL cdm_setup_optional_types();
DROP PROCEDURE cdm_setup_optional_types;

SET SESSION time_zone = @cdm_saved_time_zone;
SET SESSION sql_mode = @cdm_saved_sql_mode;
