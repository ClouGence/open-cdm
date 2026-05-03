-- 创建基本的工作负载组
CREATE WORKLOAD GROUP my_workload_group;

-- 创建带 IF NOT EXISTS 条件的工作负载组
CREATE WORKLOAD GROUP IF NOT EXISTS my_workload_group;

-- 创建带计算组关联的工作负载组
CREATE WORKLOAD GROUP my_workload_group FOR my_compute_group;

-- 创建带属性的工作负载组
CREATE WORKLOAD GROUP my_workload_group
PROPERTIES (
    "cpu_limit" = "50%",
    "memory_limit" = "30%",
    "concurrency_limit" = "10"
);

-- 创建完整配置的工作负载组
CREATE WORKLOAD GROUP IF NOT EXISTS my_workload_group
FOR my_compute_group
PROPERTIES (
    "cpu_limit" = "70%",
    "memory_limit" = "40%",
    "concurrency_limit" = "20",
    "max_query_timeout" = "3600"
);
-- 为用户创建限制性行策略
CREATE ROW POLICY IF NOT EXISTS sales_policy
ON my_database.sales_table
AS RESTRICTIVE
TO 'john_doe'@'%'
USING (region = 'North');

-- 为角色创建宽松性行策略
CREATE ROW POLICY marketing_policy
ON my_database.customer_table
AS PERMISSIVE
TO ROLE 'marketing_team'
USING (department = 'Marketing' AND active = true);

-- 复杂条件的行策略
CREATE ROW POLICY finance_restrictive_policy
ON my_database.financial_data
AS RESTRICTIVE
TO 'finance_user'
USING (amount <= 10000 OR access_level = 'senior');

-- 基本行策略（无 IF NOT EXISTS）
CREATE ROW POLICY basic_policy
ON my_table
AS PERMISSIVE
TO ROLE 'analyst'
USING (status = 'active');
-- 创建基本的工作负载策略
CREATE WORKLOAD POLICY my_workload_policy;

-- 创建带 IF NOT EXISTS 条件的工作负载策略
CREATE WORKLOAD POLICY IF NOT EXISTS my_workload_policy;

-- 创建带条件的工作负载策略
CREATE WORKLOAD POLICY my_workload_policy
CONDITIONS (query_cpu_cost > 1000, memory_usage > 1024);

-- 创建带动作的工作负载策略
CREATE WORKLOAD POLICY my_workload_policy
ACTIONS (SET_SESSION_VARIABLE 'resource_group');

-- 创建完整配置的工作负载策略
CREATE WORKLOAD POLICY IF NOT EXISTS my_workload_policy
CONDITIONS (query_cpu_cost > 1000, memory_usage > 1024)
ACTIONS (SET_SESSION_VARIABLE 'resource_group', high_priority)
PROPERTIES ("priority" = "HIGH", "timeout" = "3600");
-- 创建加密密钥
CREATE ENCRYPTKEY my_database.my_encrypt_key AS "my_secret_key_123";

-- 创建带 IF NOT EXISTS 条件的加密密钥
CREATE ENCRYPTKEY IF NOT EXISTS my_database.my_encrypt_key AS "my_secret_key_123";

-- 创建简单的加密密钥
CREATE ENCRYPTKEY encrypt_key_001 AS "abcdefg1234567";

-- 在当前数据库中创建加密密钥
CREATE ENCRYPTKEY my_key AS "secure_password_here";

-- 创建一个存储库（storage vault）
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
-- 修改存储库属性
ALTER STORAGE VAULT my_storage_vault PROPERTIES ("endpoint" = "http://new-endpoint.com");

-- 更新存储库认证信息
ALTER STORAGE VAULT s3_vault PROPERTIES (
    "access_key" = "new_access_key",
    "secret_key" = "new_secret_key"
);

-- 更改存储库位置和区域
ALTER STORAGE VAULT aws_vault PROPERTIES (
    "location" = "s3a://new-bucket/path",
    "region" = "us-west-2"
);

-- 修改目录属性
ALTER CATALOG my_catalog SET PROPERTIES ("key1" = "value1");

-- 设置多个目录属性
ALTER CATALOG test_catalog SET PROPERTIES (
    "access_key" = "ak123456",
    "secret_key" = "sk123456",
    "endpoint" = "http://example.com"
);

-- 更新目录配置
ALTER CATALOG s3_catalog SET PROPERTIES (
    "region" = "us-east-1",
    "bucket" = "my-bucket"
);

-- 修改工作负载策略属性
ALTER WORKLOAD POLICY my_workload_policy PROPERTIES ("cpu_limit" = "80");

-- 设置多个工作负载策略属性
ALTER WORKLOAD POLICY test_policy PROPERTIES (
    "memory_limit" = "10GB",
    "concurrency_limit" = "100"
);

-- 更新工作负载策略配置
ALTER WORKLOAD POLICY production_policy PROPERTIES (
    "query_timeout" = "300",
    "max_scan_rows" = "1000000"
);

-- 修改 SQL 阻断规则属性
ALTER SQL_BLOCK_RULE my_block_rule PROPERTIES ("enable" = "true");

-- 更新 SQL 阻断规则的多个属性
ALTER SQL_BLOCK_RULE test_block_rule PROPERTIES (
    "sql" = "^select \\* from test_table",
    "enable" = "false"
);

-- 修改 SQL 阻断规则的限制条件
ALTER SQL_BLOCK_RULE production_block_rule PROPERTIES (
    "user" = "test_user",
    "db" = "test_db"
);
-- 修改存储策略属性
ALTER STORAGE POLICY my_storage_policy PROPERTIES ("cool_down_ttl" = "86400");

-- 更新存储策略的多个属性
ALTER STORAGE POLICY test_storage_policy PROPERTIES (
    "storage_medium" = "SSD",
    "storage_cooldown_time" = "2023-12-31 23:59:59"
);

-- 修改存储策略的副本数和存储路径
ALTER STORAGE POLICY production_storage_policy PROPERTIES (
    "replication_num" = "3",
    "storage_path" = "/data/doris/storage"
);

-- 修改资源属性
ALTER RESOURCE my_resource PROPERTIES ("access_key" = "new_access_key");

-- 更新资源的多个属性
ALTER RESOURCE test_resource PROPERTIES (
    "secret_key" = "new_secret_key",
    "endpoint" = "http://new-endpoint.example.com"
);

-- 修改资源的类型和配置
ALTER RESOURCE s3_resource PROPERTIES (
    "type" = "s3",
    "region" = "us-west-2",
    "root.path" = "/data/warehouse"
);
-- 修改 Routine Load 任务的属性
ALTER ROUTINE LOAD FOR my_database.my_load_job
PROPERTIES ("desired_concurrent_number" = "2");

-- 更新 Routine Load 任务的多个属性和自定义参数
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

-- 修改 Routine Load 任务的数据源配置
ALTER ROUTINE LOAD FOR production_load
PROPERTIES (
    "strip_outer_array" = "true",
    "jsonpaths" = "$.data[*]"
)
FROM pulsar (
    "pulsar.broker.url" = "pulsar://localhost:6650",
    "subscription.name" = "doris-subscription"
);
-- 修改 Colocate Group 属性
ALTER COLOCATE GROUP my_colocate_group SET (" replication_num " = " 3 ");

-- 更新 Colocate Group 的多个属性
ALTER COLOCATE GROUP test_colocate_group SET (
    "bucket_num" = "10",
    "replication_num" = "2"
);

-- 设置 Colocate Group 的表属性
ALTER COLOCATE GROUP production_colocate_group SET (
    "table_property" = "persistent",
    "storage_medium" = "SSD"
);
-- 删除目录回收站中的项目
DROP CATALOG RECYCLE BIN WHERE "id_type" = 12345;

-- 删除加密密钥
DROP ENCRYPTKEY IF EXISTS my_database.my_encrypt_key;

-- 删除角色
DROP ROLE IF EXISTS admin_role;

-- 删除 SQL 阻断规则
DROP SQL_BLOCK_RULE IF EXISTS rule1, rule2, rule3;

-- 删除用户
DROP USER IF EXISTS 'test_user'@'192.168.1.1';

-- 删除存储策略
DROP STORAGE POLICY IF EXISTS hot_storage_policy;

-- 删除工作负载组
DROP WORKLOAD GROUP IF EXISTS olap_workload_group FOR compute_group_name;

-- 删除目录
DROP CATALOG IF EXISTS hive_catalog;

-- 删除文件
DROP FILE 'data_file.txt' FROM my_database PROPERTIES ("timeout" = "300");

-- 删除工作负载策略
DROP WORKLOAD POLICY IF EXISTS query_limit_policy;
