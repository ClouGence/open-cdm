SELECT * FROM func_dup_sql(42);
--------------------------
SELECT i, (compute(i)).*        --First call the Compute function, then expand all columns returned by the function
FROM generate_series(1, 3)      --Generate a temporary sequence containing 1,2,3 values for i
    AS t(i);                        --Make the results provisional, alias t, and list as i