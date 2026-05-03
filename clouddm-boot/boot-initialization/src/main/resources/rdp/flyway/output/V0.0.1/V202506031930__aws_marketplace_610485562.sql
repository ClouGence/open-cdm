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
    CREATE TABLE IF NOT EXISTS `rdp_market_sub`
    (
        `id`                         bigint                                  NOT NULL AUTO_INCREMENT,
        `gmt_create`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`               datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `uid`                        varchar(127) NOT NULL,
        `market_type`                varchar(255) NOT NULL,
        `aws_customer_id`            varchar(255) DEFAULT NULL,
        `aws_product_code`           varchar(255) DEFAULT NULL,
        `aws_account_id`             varchar(255) DEFAULT NULL,
        `sub_status`                 varchar(64)  DEFAULT 'SUBSCRIBE',
        PRIMARY KEY (`id`),
        KEY `idx_uid` (`uid`),
        KEY `idx_aws_uniq` (`aws_customer_id`, `aws_product_code`,`aws_account_id`)
    ) ENGINE = InnoDB
         DEFAULT CHARSET = utf8mb4
         COLLATE = utf8mb4_general_ci;

    ALTER TABLE `rdp_user`
        ADD COLUMN `marketplace_type` varchar(64) DEFAULT 'NONE';

end
$$

call proc_init();
