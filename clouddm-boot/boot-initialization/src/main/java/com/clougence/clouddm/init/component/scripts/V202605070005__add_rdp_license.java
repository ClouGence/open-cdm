package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070005__add_rdp_license extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List
            .of("""
                        CREATE TABLE IF NOT EXISTS `rdp_auth_version_field`
                            (
                                `id` int(11)                     NOT NULL AUTO_INCREMENT,
                                `gmt_create` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified` datetime          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                `license_version` varchar(32)    NOT NULL,
                                `fields` text                    NOT NULL,
                                UNIQUE license_version_unique(license_version),
                                PRIMARY KEY (`id`)
                            ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4\
                    """, """
                         CREATE TABLE IF NOT EXISTS `rdp_auth_code_info`
                            (
                                `id`                  int(11)                                NOT NULL AUTO_INCREMENT,
                                `gmt_create`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                `gmt_modified`        datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
                                `auth_code`           varchar(128)                           NOT NULL,
                                `second_auth_code`    text                                   NOT NULL,
                                `remind`              boolean                                NOT NULL DEFAULT FALSE,
                                PRIMARY KEY (`id`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4\
                    """, """
                        CREATE TABLE IF NOT EXISTS `rdp_apply_code_info`
                            (
                                `id`                    int(11)                                NOT NULL AUTO_INCREMENT,
                                `gmt_create`            datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                `gmt_modified`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
                                `report_time`           datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                `cluster_ip`            text                                   NOT NULL,
                                `cluster_mac_address`   text                                   NOT NULL,
                                `cluster_hardware_uuid` text                                   NOT NULL,
                                `info`                  text                                   NOT NULL,
                                `apply_type`            varchar(32)                            NOT NULL,
                                PRIMARY KEY (`id`)
                            ) ENGINE = InnoDB
                            DEFAULT CHARSET = utf8mb4\
                    """, """
                        CREATE TABLE IF NOT EXISTS `rdp_auth_result_info`
                            (
                            `id`                    int(11)                                NOT NULL AUTO_INCREMENT,
                            `gmt_create`            datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            `gmt_modified`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
                            `report_time`           datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            `active`                boolean                                NOT NULL DEFAULT FALSE,
                            `auth_result_status`    varchar(32)                            NOT NULL,
                            `msg`                   text                                   NOT NULL,
                            `success`               boolean                                NOT NULL DEFAULT FALSE,
                            PRIMARY KEY (`id`)
                            ) ENGINE = InnoDB
                            DEFAULT CHARSET = utf8mb4\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBy6FoCIkoegei97YUFRSW0w=')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.2.5',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBy6FoCIkoegei97YUFRSW0w=')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.3.1',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcXiH0+o2ZgPhdQOHbFaVg90')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.3.2',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxW3IDq62Ihy0Degb6DkiDww==')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.3.3',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxW3IDq62Ihy0Degb6DkiDww==')\
                    """, """
                         INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.3.4',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.4',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.5',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.6',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.7',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('2.8',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('3.0',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRG3Ak108G26AY4vsbVzn5s4=')\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('3.2.1',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRF3SMmNlERlBapOkgBCwi0pD2ntXtj4DoTwsJKRmYXcD')\
                    """, """
                         INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('4.2.0',
                                     'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRF3SMmNlERlBapOkgBCwi0ra3qAykB1qMpacQ2euFy5DFXecoBGSoJLICssoHU+gIi0tY3HHABeCTx/JQw5rXAA=')\
                    """);
    }

}
