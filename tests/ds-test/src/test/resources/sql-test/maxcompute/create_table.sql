CREATE TABLE IF NOT EXISTS ee(aaaq INT, qqq INT) STORED AS ALIORC TBLPROPERTIES ('columnar.nested.type'='true');
----------------------------
CREATE TABLE IF NOT EXISTS clougence_use_v2_schema.`default`.example_full_types(col_tinyint TINYINT, col_smallint SMALLINT, col_int INT, col_bigint BIGINT, col_boolean BOOLEAN, col_float FLOAT, col_double DOUBLE, col_decimal DECIMAL(38,18), col_string STRING, col_varchar VARCHAR(1000), col_char CHAR(100), col_binary BINARY, col_date DATE, col_datetime DATETIME, col_timestamp TIMESTAMP) STORED AS ALIORC TBLPROPERTIES ('columnar.nested.type'='true');
----------------------------
CREATE TABLE IF NOT EXISTS auto_sale_detail2(
 shop_name     STRING,
 customer_id   STRING,
 total_price   DOUBLE,
 _partitiontime TIMESTAMP_NTZ)
AUTO PARTITIONED BY (TRUNC_TIME(_partitiontime, 'day') AS sale_date)
TBLPROPERTIES('ingestion_time_partition'='true');
----------------------------
-- 对表中sale_date列，按月为粒度进行截取，生成了名为sale_month的分区列，并根据该分区列进行表的分区
CREATE TABLE IF NOT EXISTS auto_sale_detail(
 shop_name     STRING,
 customer_id   STRING,
 total_price   DOUBLE,
 sale_date    DATE )
AUTO PARTITIONED BY (TRUNC_TIME(sale_date, 'month') AS sale_month);