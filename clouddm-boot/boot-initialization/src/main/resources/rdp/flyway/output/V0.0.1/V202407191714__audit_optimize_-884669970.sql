# noinspection SqlNoDataSourceInspectionForFile

DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init()
    begin
        declare continue handler for 1060 begin
    end;
        declare continue handler for 1061 begin
    end;
        declare continue handler for 1062 begin
    end;
        declare continue handler for 1050 begin
    end;
        declare continue handler for 1072 begin
    end;
        declare continue handler for 1091 begin
    end;

    ALTER TABLE `rdp_op_audit` add COLUMN `log_info` text;
end
$$

call proc_init();
