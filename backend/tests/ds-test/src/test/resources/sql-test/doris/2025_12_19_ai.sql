-- Create basic task load group
CREATE WORKLOAD GROUP my_workload_group;

-- Create a task load group with IF NOT EXISTS conditions
CREATE WORKLOAD GROUP IF NOT EXISTS my_workload_group;

-- Create a task load with calculation group associated Group
CREATE WORKLOAD GROUP my_workload_group FOR my_compute_group;

-- Create a task load group with attributes
CREATE WORKLOAD GROUP my_workload_group
PROPERTIES (
    "cpu_limit" = "50%",
    "memory_limit" = "30%",
    "concurrency_limit" = "10"
);

-- Create a fully configured task load group
CREATE WORKLOAD GROUP IF NOT EXISTS my_workload_group
FOR my_compute_group
PROPERTIES (
    "cpu_limit" = "70%",
    "memory_limit" = "40%",
    "concurrency_limit" = "20",
    "max_query_timeout" = "3600"
);
-- Create a restricted line policy for users
CREATE ROW POLICY IF NOT EXISTS sales_policy
ON my_database.sales_table
AS RESTRICTIVE
TO 'john_doe'@'%'
USING (region = 'North');

-- Create a loose-line strategy for characters
CREATE ROW POLICY marketing_policy
ON my_database.customer_table
AS PERMISSIVE
TO ROLE 'marketing_team'
USING (department = 'Marketing' AND active = true);

-- A line strategy for complex conditions
CREATE ROW POLICY finance_restrictive_policy
ON my_database.financial_data
AS RESTRICTIVE
TO 'finance_user'
USING (amount <= 10000 OR access_level = 'senior');

-- Basic line policy (no IF NOT EXISTS)
CREATE ROW POLICY basic_policy
ON my_table
AS PERMISSIVE
TO ROLE 'analyst'
USING (status = 'active');
-- Create basic task load policy
CREATE WORKLOAD POLICY my_workload_policy;

-- Create a task load policy with IF NOT EXISTS conditions
CREATE WORKLOAD POLICY IF NOT EXISTS my_workload_policy;

-- Create a task load policy with conditions
CREATE WORKLOAD POLICY my_workload_policy
CONDITIONS (query_cpu_cost > 1000, memory_usage > 1024);

-- Create a task load policy with action
CREATE WORKLOAD POLICY my_workload_policy
ACTIONS (SET_SESSION_VARIABLE 'resource_group');

-- Create a fully configured taskload policy
CREATE WORKLOAD POLICY IF NOT EXISTS my_workload_policy
CONDITIONS (query_cpu_cost > 1000, memory_usage > 1024)
ACTIONS (SET_SESSION_VARIABLE 'resource_group', high_priority)
PROPERTIES ("priority" = "HIGH", "timeout" = "3600");
-- Create Encryption Key
CREATE ENCRYPTKEY my_database.my_encrypt_key AS "my_secret_key_123";

-- Create encryption key with IF NOT EXISTS conditions
CREATE ENCRYPTKEY IF NOT EXISTS my_database.my_encrypt_key AS "my_secret_key_123";

-- Create simple encryption keys
CREATE ENCRYPTKEY encrypt_key_001 AS "abcdefg1234567";

-- Create encryption keys in the current database
CREATE ENCRYPTKEY my_key AS "secure_password_here";

-- Create a repository
CREATE STORAGE VAULT IF NOT EXISTS my_storage_vault
PROPERTIES (
    "type" = "s3",
    "access_key" = "your_access_key",
    "secret_key" = "your_secret_key",
    "endpoint" = "s3.amazonaws.com"
);


ALTER SYSTEM ADD BACKEND "host1:9050", "host2:9050";
ALTER SYSTEM DROP BACKEND "host1:9050", "host2:9050";
ALTER SYSTEM DECOMMISSION BACKEND "host1:9050", "host2:9050";
ALTER SYSTEM ADD OBSERVER "observer_host:9050";
ALTER SYSTEM DROP OBSERVER "observer_host:9050";
ALTER SYSTEM ADD FOLLOWER "follower_host:9050";
ALTER SYSTEM DROP FOLLOWER "follower_host:9050";
ALTER SYSTEM ADD BROKER broker_name "broker_host1:8000", "broker_host2:8000";
ALTER SYSTEM DROP BROKER broker_name "broker_host1:8000", "broker_host2:8000";
ALTER SYSTEM DROP ALL BROKER broker_name;
ALTER SYSTEM SET LOAD ERRORS HUB
PROPERTIES (
    "max_err_num" = "100",
    "err_hub_timeout" = "60"
);
ALTER SYSTEM MODIFY BACKEND "host1:9050", "host2:9050"
SET ("attr1" = "value1", "attr2" = "value2");
ALTER SYSTEM MODIFY FRONTEND "old_host:9050" HOSTNAME "new_host";
ALTER SYSTEM MODIFY BACKEND "old_host:9050" HOSTNAME "new_host";
ALTER VIEW my_database.my_view MODIFY COMMENT 'This is a updated view comment';
ALTER VIEW my_view
(
    id COMMENT 'User ID',
    name COMMENT 'User name',
    age COMMENT 'User age'
)
COMMENT 'Updated user information view'
AS
SELECT user_id, user_name, user_age FROM users WHERE active = true;
ALTER VIEW sales_summary AS
SELECT
    product_id,
    SUM(sales_amount) as total_sales,
    COUNT(*) as transaction_count
FROM sales_transactions
GROUP BY product_id;
ALTER VIEW employee_view
(
    emp_id,
    full_name,
    department_name,
    salary
)
AS
SELECT e.id, e.name, d.name, e.salary
FROM employees e
JOIN departments d ON e.dept_id = d.id;
ALTER ROLE guest_role COMMENT 'Guest role with limited read-only access';
ALTER ROLE developer_role COMMENT 'Developer role with read and write access';
ALTER ROLE admin_role COMMENT 'Administrator role with full privileges';
-- Modify repository properties
ALTER STORAGE VAULT my_storage_vault PROPERTIES ("endpoint" = "http://new-endpoint.com");

-- Update repository authentication information
ALTER STORAGE VAULT s3_vault PROPERTIES (
    "access_key" = "new_access_key",
    "secret_key" = "new_secret_key"
);

-- Change repository position and area
ALTER STORAGE VAULT aws_vault PROPERTIES (
    "location" = "s3a://new-bucket/path",
    "region" = "us-west-2"
);

-- Modify Directory Properties
ALTER CATALOG my_catalog SET PROPERTIES ("key1" = "value1");

-- Set multiple directory properties
ALTER CATALOG test_catalog SET PROPERTIES (
    "access_key" = "ak123456",
    "secret_key" = "sk123456",
    "endpoint" = "http://example.com"
);

-- Update Directory Configuration
ALTER CATALOG s3_catalog SET PROPERTIES (
    "region" = "us-east-1",
    "bucket" = "my-bucket"
);

-- Modify Task Load Policy Properties
ALTER WORKLOAD POLICY my_workload_policy PROPERTIES ("cpu_limit" = "80");

-- Set multiple taskload policy attributes
ALTER WORKLOAD POLICY test_policy PROPERTIES (
    "memory_limit" = "10GB",
    "concurrency_limit" = "100"
);

-- Update Task Load Policy Configuration
ALTER WORKLOAD POLICY production_policy PROPERTIES (
    "query_timeout" = "300",
    "max_scan_rows" = "1000000"
);

-- Modify SQL block rule properties
ALTER SQL_BLOCK_RULE my_block_rule PROPERTIES ("enable" = "true");

-- Update multiple properties of SQL blocking rules
ALTER SQL_BLOCK_RULE test_block_rule PROPERTIES (
    "sql" = "^select \\* from test_table",
    "enable" = "false"
);

-- Modify the limitations of the SQL blocking rule
ALTER SQL_BLOCK_RULE production_block_rule PROPERTIES (
    "user" = "test_user",
    "db" = "test_db"
);
-- Modify Storage Policy Properties
ALTER STORAGE POLICY my_storage_policy PROPERTIES ("cool_down_ttl" = "86400");

-- Update multiple properties of storage policy
ALTER STORAGE POLICY test_storage_policy PROPERTIES (
    "storage_medium" = "SSD",
    "storage_cooldown_time" = "2023-12-31 23:59:59"
);

-- Number of copies and storage paths to modify storage policy
ALTER STORAGE POLICY production_storage_policy PROPERTIES (
    "replication_num" = "3",
    "storage_path" = "/data/doris/storage"
);

-- Modify Resource Properties
ALTER RESOURCE my_resource PROPERTIES ("access_key" = "new_access_key");

-- Update multiple properties of the resource
ALTER RESOURCE test_resource PROPERTIES (
    "secret_key" = "new_secret_key",
    "endpoint" = "http://new-endpoint.example.com"
);

-- Modify the type and configuration of resources
ALTER RESOURCE s3_resource PROPERTIES (
    "type" = "s3",
    "region" = "us-west-2",
    "root.path" = "/data/warehouse"
);
-- Modify properties of the Routie Load task
ALTER ROUTINE LOAD FOR my_database.my_load_job
PROPERTIES ("desired_concurrent_number" = "2");

-- Update multiple properties and custom parameters for Routie Load tasks
ALTER ROUTINE LOAD FOR test_load_task
COLUMNS TERMINATED BY ","
PROPERTIES (
    "max_batch_interval" = "20",
    "max_batch_rows" = "300000"
)
FROM kafka (
    "bootstrap.servers" = "host:port",
    "group.id" = "test_group"
);

-- Modify the data source configuration for the Routie Load task
ALTER ROUTINE LOAD FOR production_load
PROPERTIES (
    "strip_outer_array" = "true",
    "jsonpaths" = "$.data[*]"
)
FROM pulsar (
    "pulsar.broker.url" = "pulsar://localhost:6650",
    "subscription.name" = "doris-subscription"
);
-- Modify Colocate Group Properties
ALTER COLOCATE GROUP my_colocate_group SET (" replication_num " = " 3 ");

-- Update multiple properties of Colocate Group
ALTER COLOCATE GROUP test_colocate_group SET (
    "bucket_num" = "10",
    "replication_num" = "2"
);

-- Set Table Properties for Colocate Group
ALTER COLOCATE GROUP production_colocate_group SET (
    "table_property" = "persistent",
    "storage_medium" = "SSD"
);
-- Remove items from the directorybin
DROP CATALOG RECYCLE BIN WHERE "id_type" = 12345;

-- Remove Encryption Key
DROP ENCRYPTKEY IF EXISTS my_database.my_encrypt_key;

-- Remove Role
DROP ROLE IF EXISTS admin_role;

-- Remove SQL block rules
DROP SQL_BLOCK_RULE IF EXISTS rule1, rule2, rule3;

-- Delete User
DROP USER IF EXISTS 'test_user'@'192.168.1.1';

-- Remove Storage Policy
DROP STORAGE POLICY IF EXISTS hot_storage_policy;

-- Other Organiser
DROP WORKLOAD GROUP IF EXISTS olap_workload_group FOR compute_group_name;

-- Remove Directory
DROP CATALOG IF EXISTS hive_catalog;

-- Delete File
DROP FILE 'data_file.txt' FROM my_database PROPERTIES ("timeout" = "300");

-- Remove Task Load Policy
DROP WORKLOAD POLICY IF EXISTS query_limit_policy;
