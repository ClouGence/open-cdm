package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070025__aws_marketplace extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
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
                             DEFAULT CHARSET = utf8mb4\
                """, """
                    ALTER TABLE `rdp_user`
                            ADD COLUMN `marketplace_type` varchar(64) DEFAULT 'NONE'\
                """);
    }
}
