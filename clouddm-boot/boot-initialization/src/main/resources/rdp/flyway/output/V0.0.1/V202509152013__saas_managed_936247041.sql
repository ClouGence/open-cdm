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
   -- on-premise deployments may have this role
   INSERT IGNORE INTO `rdp_role` (`id`, `gmt_create`, `gmt_modified`, `owner_uid`, `role_name`, `role_auth_labels`, `alias_name`, `inner_tag`)
   VALUES (4, now(), now(), '6258151610403310', 'CC_SaaS_Developer', '["CC_DATAJOB_MANAGE","CC_DATAJOB_READ","CC_WORKER_READ","RDP_DS_READ","RDP_DS_MANAGE","RDP_ROLE_READ"]', 'SaaS 开发者角色', '1' );

end
$$

call proc_init();
