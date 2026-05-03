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
        add column access_token text DEFAULT NULL;

    CREATE TABLE IF NOT EXISTS rdp_csrf_token
    (
        id           bigint       NOT NULL AUTO_INCREMENT primary key,
        gmt_create   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        gmt_modified datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        token        varchar(127) NOT NULL,
        jump_url     text,
        secret_token text
    );


end
$$

call proc_init();
