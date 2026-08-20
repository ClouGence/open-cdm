-- Reports source-database facts. Large values are represented by length and SHA-256.

SET NAMES utf8mb4;
SET @cdm_saved_time_zone = @@SESSION.time_zone;
SET SESSION time_zone = '+00:00';
USE cdm_data_types;

SELECT
  'ENVIRONMENT' AS section_name,
  VERSION() AS server_version,
  @@SESSION.time_zone AS session_time_zone,
  @@SESSION.sql_mode AS session_sql_mode,
  @@character_set_connection AS connection_charset,
  @@collation_connection AS connection_collation,
  @@max_allowed_packet AS max_allowed_packet;

SELECT
  'METADATA' AS section_name,
  table_name,
  ordinal_position,
  column_name,
  data_type,
  column_type,
  character_maximum_length,
  character_octet_length,
  numeric_precision,
  numeric_scale,
  datetime_precision,
  character_set_name,
  collation_name,
  is_nullable,
  column_default,
  extra
FROM information_schema.columns
WHERE table_schema = 'cdm_data_types'
ORDER BY table_name, ordinal_position;

SELECT
  'NUMERIC' AS section_name,
  case_id,
  BIN(c_bit) AS c_bit_bin,
  CAST(c_tinyint AS CHAR) AS c_tinyint,
  CAST(c_tinyint_unsigned AS CHAR) AS c_tinyint_unsigned,
  CAST(c_smallint AS CHAR) AS c_smallint,
  CAST(c_smallint_unsigned AS CHAR) AS c_smallint_unsigned,
  CAST(c_mediumint AS CHAR) AS c_mediumint,
  CAST(c_mediumint_unsigned AS CHAR) AS c_mediumint_unsigned,
  CAST(c_int AS CHAR) AS c_int,
  CAST(c_integer AS CHAR) AS c_integer,
  CAST(c_int_unsigned AS CHAR) AS c_int_unsigned,
  CAST(c_integer_unsigned AS CHAR) AS c_integer_unsigned,
  CAST(c_bigint AS CHAR) AS c_bigint,
  CAST(c_bigint_unsigned AS CHAR) AS c_bigint_unsigned,
  CAST(c_decimal AS CHAR) AS c_decimal,
  CAST(c_dec AS CHAR) AS c_dec,
  CAST(c_numeric AS CHAR) AS c_numeric,
  CAST(c_fixed AS CHAR) AS c_fixed,
  CAST(c_float AS CHAR) AS c_float,
  CAST(c_float_unsigned AS CHAR) AS c_float_unsigned,
  CAST(c_double AS CHAR) AS c_double,
  CAST(c_double_precision AS CHAR) AS c_double_precision,
  CAST(c_real AS CHAR) AS c_real
FROM cdm_dt_numeric
ORDER BY FIELD(case_id, 'NULL', 'ZERO', 'NORMAL', 'MIN', 'MAX');

SELECT
  'ALIASES' AS section_name,
  case_id,
  CAST(serial_value AS CHAR) AS serial_value,
  CAST(c_bool AS CHAR) AS c_bool,
  CAST(c_boolean AS CHAR) AS c_boolean
FROM cdm_dt_alias
ORDER BY serial_value;

SELECT
  'TEMPORAL_UTC' AS section_name,
  case_id,
  CAST(c_date AS CHAR) AS c_date,
  CAST(c_time0 AS CHAR) AS c_time0,
  CAST(c_time6 AS CHAR) AS c_time6,
  CAST(c_datetime0 AS CHAR) AS c_datetime0,
  CAST(c_datetime6 AS CHAR) AS c_datetime6,
  CAST(c_timestamp0 AS CHAR) AS c_timestamp0,
  CAST(c_timestamp6 AS CHAR) AS c_timestamp6,
  CAST(c_year AS CHAR) AS c_year
FROM cdm_dt_temporal
ORDER BY FIELD(case_id, 'NULL', 'ZERO', 'NORMAL', 'MIN', 'MAX', 'NEGATIVE_TIME');

SELECT
  'CHARACTER' AS section_name,
  case_id,
  c_char,
  CHAR_LENGTH(c_char) AS c_char_chars,
  OCTET_LENGTH(c_char) AS c_char_bytes,
  HEX(c_char) AS c_char_hex,
  c_varchar,
  CHAR_LENGTH(c_varchar) AS c_varchar_chars,
  OCTET_LENGTH(c_varchar) AS c_varchar_bytes,
  HEX(c_varchar) AS c_varchar_hex,
  c_tinytext,
  c_text,
  c_mediumtext,
  c_longtext,
  c_enum,
  c_set
FROM cdm_dt_character
ORDER BY FIELD(case_id, 'NULL', 'EMPTY', 'NORMAL', 'UNICODE', 'BOUNDARY');

SELECT
  'CHARACTER_LONG' AS section_name,
  case_id,
  expected_characters,
  CHAR_LENGTH(c_longtext) AS actual_characters,
  expected_bytes,
  OCTET_LENGTH(c_longtext) AS actual_bytes,
  SHA2(c_longtext, 256) AS sha256
FROM cdm_dt_character_long
ORDER BY expected_characters;

SELECT
  'BINARY_LOB' AS section_name,
  case_id,
  HEX(c_binary) AS c_binary_hex,
  OCTET_LENGTH(c_binary) AS c_binary_bytes,
  HEX(c_varbinary) AS c_varbinary_hex,
  OCTET_LENGTH(c_varbinary) AS c_varbinary_bytes,
  HEX(c_tinyblob) AS c_tinyblob_hex,
  OCTET_LENGTH(c_tinyblob) AS c_tinyblob_bytes,
  CASE
    WHEN OCTET_LENGTH(c_blob) <= 257 THEN HEX(c_blob)
    ELSE CONCAT('sha256:', SHA2(c_blob, 256))
  END AS c_blob_fact,
  OCTET_LENGTH(c_blob) AS c_blob_bytes,
  CASE
    WHEN OCTET_LENGTH(c_mediumblob) <= 257 THEN HEX(c_mediumblob)
    ELSE CONCAT('sha256:', SHA2(c_mediumblob, 256))
  END AS c_mediumblob_fact,
  OCTET_LENGTH(c_mediumblob) AS c_mediumblob_bytes,
  CASE
    WHEN OCTET_LENGTH(c_longblob) <= 257 THEN HEX(c_longblob)
    ELSE CONCAT('sha256:', SHA2(c_longblob, 256))
  END AS c_longblob_fact,
  OCTET_LENGTH(c_longblob) AS c_longblob_bytes
FROM cdm_dt_binary_lob
ORDER BY FIELD(case_id, 'NULL', 'EMPTY', 'BYTES', 'BOUNDARY_256', 'BOUNDARY_257', 'ONE_MIB');

SELECT
  'BINARY_LONG' AS section_name,
  case_id,
  expected_bytes,
  OCTET_LENGTH(c_longblob) AS actual_bytes,
  SHA2(c_longblob, 256) AS sha256
FROM cdm_dt_binary_long
ORDER BY expected_bytes;

SELECT
  'SPATIAL' AS section_name,
  case_id,
  ST_AsText(c_geometry) AS c_geometry_wkt,
  ST_SRID(c_geometry) AS c_geometry_srid,
  ST_AsText(c_point) AS c_point_wkt,
  ST_SRID(c_point) AS c_point_srid,
  ST_AsText(c_linestring) AS c_linestring_wkt,
  ST_SRID(c_linestring) AS c_linestring_srid,
  ST_AsText(c_polygon) AS c_polygon_wkt,
  ST_SRID(c_polygon) AS c_polygon_srid,
  ST_AsText(c_multipoint) AS c_multipoint_wkt,
  ST_SRID(c_multipoint) AS c_multipoint_srid,
  ST_AsText(c_multilinestring) AS c_multilinestring_wkt,
  ST_SRID(c_multilinestring) AS c_multilinestring_srid,
  ST_AsText(c_multipolygon) AS c_multipolygon_wkt,
  ST_SRID(c_multipolygon) AS c_multipolygon_srid,
  ST_AsText(c_geometrycollection) AS c_geometrycollection_wkt,
  ST_SRID(c_geometrycollection) AS c_geometrycollection_srid
FROM cdm_dt_spatial
ORDER BY FIELD(case_id, 'NULL', 'SRID_0', 'SRID_4326');

DELIMITER $$
CREATE PROCEDURE cdm_verify_optional_types()
BEGIN
  IF EXISTS (
    SELECT 1
    FROM cdm_dt_character_long
    WHERE case_id = 'LIMIT_PLUS_ONE'
  ) THEN
    SELECT 'LIMIT_PLUS_ONE_FIXTURE' AS section_name,
      'PRESENT' AS feature_status,
      @@max_allowed_packet AS max_allowed_packet;
  ELSE
    SELECT 'LIMIT_PLUS_ONE_FIXTURE' AS section_name,
      'OMITTED_MAX_ALLOWED_PACKET_TOO_SMALL' AS feature_status,
      @@max_allowed_packet AS max_allowed_packet;
  END IF;

  IF EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'cdm_data_types'
      AND table_name = 'cdm_dt_spatial'
      AND column_name = 'c_geomcollection'
  ) THEN
    SET @cdm_sql = 'SELECT
      ''SPATIAL_GEOMCOLLECTION_ALIAS'' AS section_name,
      case_id,
      ST_AsText(c_geomcollection) AS c_geomcollection_wkt,
      ST_SRID(c_geomcollection) AS c_geomcollection_srid
    FROM cdm_dt_spatial
    ORDER BY FIELD(case_id, ''NULL'', ''SRID_0'', ''SRID_4326'')';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;
  ELSE
    SELECT 'SPATIAL_GEOMCOLLECTION_ALIAS' AS section_name,
      'NOT_APPLICABLE_BEFORE_8.0' AS feature_status;
  END IF;

  IF EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'cdm_data_types'
      AND table_name = 'cdm_dt_json'
  ) THEN
    SET @cdm_sql = 'SELECT
      ''JSON'' AS section_name,
      case_id,
      c_json,
      JSON_TYPE(c_json) AS json_type,
      JSON_VALID(c_json) AS json_valid,
      CHAR_LENGTH(CAST(c_json AS CHAR)) AS normalized_characters,
      SHA2(CAST(c_json AS CHAR), 256) AS normalized_sha256
    FROM cdm_dt_json
    ORDER BY FIELD(case_id,
      ''SQL_NULL'', ''JSON_NULL'', ''SCALAR'', ''OBJECT'',
      ''ARRAY'', ''DEEP'', ''UNICODE'', ''LONG'')';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;
  ELSE
    SELECT 'JSON' AS section_name, 'NOT_APPLICABLE_BEFORE_5.7' AS feature_status;
  END IF;

  IF EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'cdm_data_types'
      AND table_name = 'cdm_dt_vector'
  ) THEN
    SET @cdm_sql = 'SELECT
      ''VECTOR'' AS section_name,
      case_id,
      VECTOR_TO_STRING(c_vector_1) AS c_vector_1,
      VECTOR_DIM(c_vector_1) AS c_vector_1_dim,
      VECTOR_TO_STRING(c_vector_3) AS c_vector_3,
      VECTOR_DIM(c_vector_3) AS c_vector_3_dim,
      VECTOR_TO_STRING(c_vector_1024) AS c_vector_1024,
      VECTOR_DIM(c_vector_1024) AS c_vector_1024_dim
    FROM cdm_dt_vector
    ORDER BY FIELD(case_id, ''NULL'', ''ONE_DIMENSION'', ''NORMAL'', ''LONG'')';
    PREPARE cdm_stmt FROM @cdm_sql;
    EXECUTE cdm_stmt;
    DEALLOCATE PREPARE cdm_stmt;
  ELSE
    SELECT 'VECTOR' AS section_name, 'NOT_APPLICABLE_BEFORE_9.0' AS feature_status;
  END IF;
END$$
DELIMITER ;

CALL cdm_verify_optional_types();
DROP PROCEDURE cdm_verify_optional_types;

SET SESSION time_zone = @cdm_saved_time_zone;
