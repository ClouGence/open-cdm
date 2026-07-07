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
package com.clougence.clouddm.pkg.drivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.inner.drivers.loader.MavenResourcePreparer;
import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.FamilyDef;
import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.def.VerDef;
import com.clougence.drivers.factory.DefaultDriverLoader;
import com.clougence.utils.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Build-time tool that resolves the built-in driver bundle for release images.
 *
 * It reuses the exact runtime code path (DefaultDriverLoader + MavenResourcePreparer), so the
 * produced layout — flat jars plus files.idx per "family/version" directory — is byte-compatible
 * with what the runtime driver directory fallback expects.
 *
 * IMPORTANT: the family / version / maven coordinate in the input xml must exactly match the
 * corresponding plugin drivers.xml entry, otherwise the files.idx resource id will not match at
 * runtime and the bundled driver will be ignored.
 */
@Slf4j
public class BuiltinDriverBundler {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            log.error("usage: BuiltinDriverBundler <built-in-drivers.xml> <outputDir>");
            System.exit(2);
        }

        File configXml = new File(args[0]);
        File outputDir = new File(args[1]);
        if (!configXml.isFile()) {
            log.error("built-in driver config not found: {}", configXml.getAbsolutePath());
            System.exit(2);
        }

        DefaultDriverLoader loader = new DefaultDriverLoader(outputDir, System.getProperties());
        loader.registerPreparer("maven", MavenResourcePreparer::new);
        try (InputStream input = new FileInputStream(configXml)) {
            loader.loadDriverXml(input);
        }

        List<String> failures = new ArrayList<>();
        for (String familyName : loader.familyNames()) {
            FamilyDef family = loader.findDriver(familyName);
            for (VerDef version : family.versions) {
                prepareVersion(loader, version, failures);
            }
        }

        if (!failures.isEmpty()) {
            log.error("built-in driver bundle failed: {}", failures);
            System.exit(1);
        }

        // drop the maven analysis working directory so the bundle only contains driver files.
        File analysisDir = new File(outputDir, ".maven-analysis");
        if (analysisDir.exists()) {
            FileUtils.forceDelete(analysisDir);
        }
        log.info("built-in driver bundle completed, outputDir={}", outputDir.getAbsolutePath());
    }

    private static void prepareVersion(DefaultDriverLoader loader, VerDef version, List<String> failures) {
        String versionKey = version.getFamilyName() + "/" + version.getVersion();
        log.info("preparing built-in driver: {}", versionKey);

        loader.prepareDriverVersion(version, null, new DriverPrepareProgress() {

            @Override
            public void onError(DriverVersion driverVersion, ResDef resource, Exception exception) {
                log.error("prepare built-in driver failed: {}, coordinate={}", versionKey, resource == null ? null : resource.getCoordinate(), exception);
            }
        });

        if (!version.isPrepared()) {
            failures.add(versionKey);
            return;
        }
        log.info("prepared built-in driver: {}, files={}", versionKey, version.getFiles().size());
    }
}
