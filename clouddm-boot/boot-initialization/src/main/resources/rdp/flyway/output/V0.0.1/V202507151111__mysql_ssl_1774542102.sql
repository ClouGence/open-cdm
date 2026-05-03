DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init ()
begin
    declare continue handler for 1060 begin end;
    declare continue handler for 1062 begin end;
    declare continue handler for 1050 begin end;
    declare continue handler for 1072 begin end;
    declare continue handler for 1091 begin end;

   -- your DDLs here
    ALTER TABLE `rdp_data_source` ADD COLUMN `security_file_password` varchar(512) DEFAULT NULL;
    ALTER TABLE `rdp_data_source` ADD COLUMN `client_security_file_password` varchar(512) DEFAULT NULL;

end
$$

call proc_init();
