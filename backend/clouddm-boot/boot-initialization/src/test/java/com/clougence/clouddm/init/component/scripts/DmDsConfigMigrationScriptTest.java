/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.init.component.scripts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class DmDsConfigMigrationScriptTest {

    @Test
    public void shouldUnifyLegacyDsConfigStorageInOrder() {
        List<String> scripts = new V202606190001__dm_ds_config_unify().collectScript();

        assertEquals(11, scripts.size());
        assertContains(scripts.get(0), "INSERT INTO dm_ds_config_kv_4dm");
        assertContains(scripts.get(0), "FROM dm_ds_config_kv_4rdp r");
        assertContains(scripts.get(0), "WHERE NOT EXISTS");
        assertContains(scripts.get(0), "d.data_source_id = r.data_source_id");
        assertContains(scripts.get(0), "d.config_name = r.config_name");
        assertFalse(scripts.get(0).contains("conf_val_type"));

        assertContains(scripts.get(1), "ADD COLUMN `status`");
        assertContains(scripts.get(2), "ADD COLUMN `status_message`");
        assertContains(scripts.get(3), "ADD COLUMN `bind_cluster_id`");

        String migrateScript = scripts.get(4);
        assertContains(migrateScript, "UPDATE dm_ds d");
        assertContains(migrateScript, "JOIN dm_ds_config c");
        assertContains(migrateScript, "d.status = COALESCE(d.status, c.status)");
        assertContains(migrateScript, "d.bind_cluster_id = COALESCE(d.bind_cluster_id, c.bind_cluster_id)");
        assertContains(migrateScript, "d.ds_env_id = COALESCE(d.ds_env_id, c.bind_env_id)");
        assertContains(migrateScript, "d.host_type = COALESCE(d.host_type, c.host_type)");

        String usageScript = scripts.get(5);
        assertContains(usageScript, "UPDATE dm_ds_usage u");
        assertContains(usageScript, "u.res_instance_id = c.config_instance_id");
        assertContains(usageScript, "SET u.res_instance_id = d.instance_id");

        assertContains(scripts.get(6), "ALTER TABLE dm_ds_usage");
        assertContains(scripts.get(6), "DROP COLUMN endpoint");

        assertContains(scripts.get(7), "ALTER TABLE dm_ds_config_kv_4dm");
        assertContains(scripts.get(7), "DROP COLUMN `config_group`");
        assertContains(scripts.get(7), "DROP COLUMN `display`");
        assertContains(scripts.get(7), "DROP COLUMN `desc_key`");
        assertContains(scripts.get(7), "DROP COLUMN `value_require`");
        assertContains(scripts.get(7), "DROP COLUMN `value_valid_regex`");
        assertContains(scripts.get(7), "DROP COLUMN `default_value`");
        assertContains(scripts.get(7), "DROP COLUMN `value_advance`");
        assertContains(scripts.get(7), "DROP COLUMN `read_only`");
        assertContains(scripts.get(7), "DROP COLUMN `is_secret`");
        assertFalse(scripts.get(7).contains("conf_val_type"));

        assertContains(scripts.get(8), "ALTER TABLE dm_sys_user_conf");
        assertContains(scripts.get(8), "DROP COLUMN `default_value`");
        assertContains(scripts.get(8), "DROP COLUMN `value_range`");
        assertContains(scripts.get(8), "DROP COLUMN `read_only`");
        assertContains(scripts.get(8), "DROP COLUMN `user_config_tag_type`");
        assertContains(scripts.get(8), "DROP COLUMN `conf_belong`");
        assertContains(scripts.get(8), "DROP COLUMN `conf_val_type`");
        assertContains(scripts.get(8), "DROP COLUMN `is_secret`");
        assertContains(scripts.get(8), "DROP COLUMN `desc_key`");

        assertContains(scripts.get(9), "DROP TABLE IF EXISTS dm_ds_config_kv_4rdp");
        assertContains(scripts.get(10), "DROP TABLE IF EXISTS dm_ds_config");
    }

    private void assertContains(String actual, String expected) {
        assertTrue("Expected SQL to contain: " + expected + "\nActual SQL: " + actual,
            actual.toLowerCase(Locale.ROOT).contains(expected.toLowerCase(Locale.ROOT)));
    }
}
