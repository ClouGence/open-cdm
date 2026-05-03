# noinspection SqlNoDataSourceInspectionForFile

DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init()
begin
    declare continue handler for 1060 begin end;
    declare continue handler for 1061 begin end;
    declare continue handler for 1062 begin end;
    declare continue handler for 1050 begin end;
    declare continue handler for 1072 begin end;
    declare continue handler for 1091 begin end;
    declare continue handler for 1051 begin end;

    alter table dm_ticket_details_inst
        add column checked_info text null;

-- sample user and worker (end) --------------------
end
$$

call proc_init();