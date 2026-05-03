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

create table `rdp_ticket_process_activity`
(
    id              bigint auto_increment primary key,
    gmt_create      datetime     default CURRENT_TIMESTAMP not null,
    gmt_modified    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    activity_id     varchar(64)                            not null,
    process_id      bigint                                 not null,
    ticket_id       bigint                                 not null,
    activity_title  varchar(128)                           not null,
    context         text,
    deleted         tinyint(1)   default 0                 not null,
    order_number    int                                    not null
) collate = utf8mb4_general_ci;

create index idx_process_activity
    on `rdp_ticket_process_activity` (process_id, activity_id);
create index idx_ticket_id
    on `rdp_ticket_process_activity` (ticket_id);

ALTER TABLE `rdp_ticket_inst` add COLUMN `approval_url` text comment '第三方审批流跳转连接';

delete from rdp_async_task where handler_name = 'rdpTicketAsyncTask';

end
$$

call proc_init();