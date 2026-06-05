SELECT * FROM func_dup_sql(42);
--------------------------
SELECT i, (compute(i)).*        --先调用compute函数，再展开函数返回的所有列
FROM generate_series(1, 3)      --生成一个包含1，2，3的临时序列，用于i的赋值
    AS t(i);                        --将结果作为临时表，别名为t，列名为i