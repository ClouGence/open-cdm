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

    alter table dm_files
        modify unique_id varchar(80) not null;
    alter table dm_files
        add query_id varchar(40) not null;
    create index idx_files_query_id
        on dm_files (query_id);
    alter table dm_files
        add try_count int not null default 0;

-- sample user and worker (end) --------------------
end
$$

call proc_init();