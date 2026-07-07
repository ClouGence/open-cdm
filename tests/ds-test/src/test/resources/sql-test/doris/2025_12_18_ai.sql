show backend config;

show FRONTEND config;
SHOW BACKEND CONFIG LIKE '%disk%' FROM 12345;
SHOW BACKEND CONFIG FROM 12345;
SHOW FRONTEND CONFIG LIKE '%memory%';
ADMIN SHOW FRONTEND CONFIG;
SHOW BACKEND CONFIG;
SHOW FRONTEND CONFIG;
CLEAN ALL PROFILE;
CLEAN LABEL my_label FROM my_database;
CLEAN LABEL my_label IN my_database;
CLEAN QUERY STATS FOR my_database;
CLEAN QUERY STATS FROM my_database.my_table;
CLEAN QUERY STATS IN my_database.my_table;
CLEAN ALL QUERY STATS;
UNSET VARIABLE my_variable;
UNSET SESSION VARIABLE ALL;
UNSET GLOBAL VARIABLE my_global_var;
UNSET DEFAULT STORAGE VAULT;
   REFRESH LDAP ALL;
   REFRESH LDAP FOR my_user;
      REFRESH TABLE my_table;
   REFRESH TABLE my_database.my_table;
   REFRESH TABLE my_catalog.my_database.my_table;
      REFRESH DATABASE my_database;
   REFRESH DATABASE my_catalog.my_database;
   REFRESH DATABASE my_database PROPERTIES ("key" = "value");
      REFRESH CATALOG my_catalog;
   REFRESH CATALOG my_catalog PROPERTIES ("key" = "value");
SHOW ROUTINE LOAD TASK FROM my_database WHERE status = "RUNNING";
SHOW ROUTINE LOAD FOR my_database.my_job;
SHOW ROUTINE LOAD LIKE '%pattern%';
-- Or show all
SHOW ALL ROUTINE LOAD;
STOP ROUTINE LOAD FOR my_database.my_job;
RESUME ROUTINE LOAD FOR my_database.my_job;
-- Or restore everything.
RESUME ALL ROUTINE LOAD;
PAUSE ROUTINE LOAD FOR my_database.my_job;
-- Or pause everything.
PAUSE ALL ROUTINE LOAD;
LOAD DATA LOCAL INFILE '/path/to/data.csv'
INTO TABLE my_table
COLUMNS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
PROPERTIES (
    "timeout" = "3600",
    "max_filter_ratio" = "0.1"
);
CREATE ROUTINE LOAD my_database.my_job ON my_table
PROPERTIES (
    "desired_concurrent_number" = "3",
    "max_batch_interval" = "20"
)
FROM KAFKA (
    "kafka_broker_list" = "broker1:9092,broker2:9092",
    "kafka_topic" = "my_topic",
    "property_group_id" = "my_group"
);
SYNC;

KILL CONNECTION 12345;
-- Or in short form.
KILL 12345;
-- Termination of queries by query ID (integer form)
KILL QUERY 67890;

-- Termination of query by query ID (string form)
KILL QUERY 'query_1234567890';

-- Basic restoration database
RECOVER DATABASE my_database;

-- Specify database ID recovery
RECOVER DATABASE my_database 12345;

-- Restore Database and Rename
RECOVER DATABASE my_database AS new_database_name;

-- Specify ID and rename recovery
RECOVER DATABASE my_database 12345 AS new_database_name;
-- Basic rehabilitation section
RECOVER PARTITION my_partition FROM my_database.my_table;

-- Specify Partition ID Restore
RECOVER PARTITION my_partition 54321 FROM my_database.my_table;

-- Restore partitions and rename them
RECOVER PARTITION my_partition AS new_partition_name FROM my_database.my_table;

-- Specify ID and rename recovery
RECOVER PARTITION my_partition 54321 AS new_partition_name FROM my_database.my_table;

-- Refresh selected partitions
REFRESH MATERIALIZED VIEW my_database.my_mv PARTITION (p1, p2);

-- completely refresh
REFRESH MATERIALIZED VIEW my_database.my_mv COMPLETE;

-- Auto Refresh
REFRESH MATERIALIZED VIEW my_database.my_mv AUTO;
ALTER MATERIALIZED VIEW my_database.old_mv RENAME new_mv_name;
-- Set a new way to brush
ALTER MATERIALIZED VIEW my_database.my_mv REFRESH COMPLETE;

-- Set Refresh Trigger
ALTER MATERIALIZED VIEW my_database.my_mv REFRESH ON SCHEDULE EVERY 1 HOUR;

-- And set up a new brush and trigger.
ALTER MATERIALIZED VIEW my_database.my_mv REFRESH COMPLETE ON SCHEDULE EVERY 30 MINUTE;
ALTER MATERIALIZED VIEW my_database.my_mv
REPLACE WITH MATERIALIZED VIEW new_mv_name
PROPERTIES ("replication_num" = "3");
ALTER MATERIALIZED VIEW my_database.my_mv
SET ("partition_refresh_number" = "10", "excluded_trigger_tables" = "table1,table2");
-- Basic LOAD statement
LOAD LABEL my_database.my_label
(
    DATA INFILE( "/path/to/data.csv")
    INTO TABLE my_table
    COLUMNS TERMINATED BY ","
    LINES TERMINATED BY "\n"
);

-- LOAD statement with partition
LOAD LABEL my_database.my_label
(
    DATA INFILE ("/path/to/data.csv")
    INTO TABLE my_table
    PARTITION (p1, p2)
    COLUMNS TERMINATED BY "|"
    LINES TERMINATED BY "\n"
    (col1, col2, col3)
);

-- LOAD statement with attributes and comments
LOAD LABEL my_database.my_label
(
    DATA INFILE ("/path/to/data.csv")
    INTO TABLE my_table
    COLUMNS TERMINATED BY ","
    LINES TERMINATED BY "\n"
    (col1, col2, col3)
    SET (col1 = col1, col2 = UPPER(col2))
    WHERE col1 > 100
)
PROPERTIES (
    "timeout" = "3600",
    "max_filter_ratio" = "0.1"
)
COMMENT "Loading sales data";

-- Load data from remote storage system
LOAD LABEL my_database.my_label
(
    DATA INFILE ("s3://bucket/path/data.csv")
    INTO TABLE my_table
    COLUMNS TERMINATED BY ","
    LINES TERMINATED BY "\n"
)
WITH S3 (
    "AWS_ENDPOINT" = "http://s3.amazonaws.com",
    "AWS_ACCESS_KEY" = "access_key",
    "AWS_SECRET_KEY" = "secret_key"
)
PROPERTIES (
    "timeout" = "7200"
);
