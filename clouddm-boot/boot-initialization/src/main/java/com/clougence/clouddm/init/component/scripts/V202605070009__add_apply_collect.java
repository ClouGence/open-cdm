package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070009__add_apply_collect extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List
            .of("""
                        CREATE TABLE IF NOT EXISTS `rdp_apply_collect_info`
                            (
                                `id`                    int                                    NOT NULL AUTO_INCREMENT,
                                `gmt_create`            datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                `gmt_modified`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
                                `report_time`           datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                `apply_id`              int                                    NOT NULL,
                                `job_id`                int                                             DEFAULT NULL,
                                `task_id`               int                                             DEFAULT NULL,
                                `link_type`             text                                            DEFAULT NULL,
                                `src_ds_type`           text                                            DEFAULT NULL,
                                `dst_ds_type`           text                                            DEFAULT NULL,
                                `collect_name`          longtext                                        DEFAULT NULL,
                                `collect_value`         longtext                                        DEFAULT NULL,
                                PRIMARY KEY (`id`)
                            ) ENGINE = InnoDB
                            DEFAULT CHARSET = utf8mb4\
                    """, """
                        INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
                            VALUES ('4.2.2',
                                    'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRF3SMmNlERlBapOkgBCwi0ra3qAykB1qMpacQ2euFy5DFXecoBGSoJLICssoHU+gIsVyd1wN8VvrxFb2vi0UilvVcWFAS5rsDm3Qw248InTX')\
                    """);
    }

}
