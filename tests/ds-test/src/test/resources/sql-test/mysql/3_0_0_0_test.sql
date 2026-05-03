CREATE DEFINER=`root`@`%` FUNCTION `GetEmployeeSalary`(emp_id int) RETURNS decimal(10,0)
    READS SQL DATA
begin

     DECLARE emp_salary DECIMAL(10, 2);

    SELECT salary INTO emp_salary FROM employees WHERE id = emp_id;

    RETURN emp_salary;

end;
CREATE DEFINER=`root`@`%` PROCEDURE `GetEmployeesAboveSalary`(IN min_salary decimal(10, 2))
begin
 SELECT * FROM employees WHERE salary > min_salary;
end;
CREATE DEFINER=`root`@`%` TRIGGER `check_age_before_insert3` BEFORE INSERT ON `students` FOR EACH ROW BEGIN
    IF NEW.age < 18 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '年龄不能低于18岁';
    END IF;
END;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `v_biz_model3` AS select `biz_model`.`id` AS `id`,`biz_model`.`created_time` AS `created_time`,`biz_model`.`updated_time` AS `updated_time`,`biz_model`.`content` AS `content` from `biz_model` limit 20
