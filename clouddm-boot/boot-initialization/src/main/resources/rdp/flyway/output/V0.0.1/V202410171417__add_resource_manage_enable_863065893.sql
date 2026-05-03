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
    ALTER TABLE rdp_user
        ADD COLUMN resource_manage_enable tinyint(1) DEFAULT 0;

end
$$

call proc_init();