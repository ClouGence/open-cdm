DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init ()
begin
    declare continue handler for 1060 begin end;
    declare continue handler for 1061 begin end;
    declare continue handler for 1062 begin end;
    declare continue handler for 1050 begin end;
    declare continue handler for 1072 begin end;
    declare continue handler for 1091 begin end;

-- your DDLs here

ALTER TABLE `rdp_op_audit`
    ADD COLUMN `ip` varchar(255) DEFAULT NULL;

ALTER TABLE `rdp_op_audit`
    ADD COLUMN `audit_type` varchar(255) DEFAULT NULL;

ALTER TABLE `rdp_op_audit`
    ADD COLUMN `operation_uri` varchar(255) DEFAULT NULL;

ALTER TABLE `rdp_op_audit`
    MODIFY COLUMN `resource_value` varchar(512) DEFAULT NULL;

end
$$

call proc_init();