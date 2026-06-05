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
package com.clougence.drivers.def;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.drivers.DriverFamily;
import com.clougence.utils.StringUtils;

public class FamilyDef implements DriverFamily {

    private static final Comparator<VerDef> VERSION_COMPARATOR = Comparator.comparing((VerDef item) -> {
                                                                   return StringUtils.defaultString(item.getVersion());
                                                               }, String.CASE_INSENSITIVE_ORDER).reversed();
    public final List<VerDef>               versions           = new ArrayList<>();
    public String                           familyName;

    @Override
    public String getFamilyName() { return this.familyName; }

    @Override
    public List<String> getVersions() {
        return this.versions.stream()//
            .map(VerDef::getVersion)
            .distinct()
            .sorted((o1, o2) -> -o1.compareTo(o2))
            .collect(Collectors.toList());
    }

    @Override
    public VerDef findVersion(String version) {
        if (this.versions.isEmpty()) {
            return null;
        }
        if (StringUtils.isBlank(version)) {
            return this.versions.get(0);
        }

        for (VerDef item : this.versions) {
            if (StringUtils.equalsIgnoreCase(item.getVersion(), version)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public VerDef addVersion(String version) {
        VerDef exists = findVersion(version);
        if (exists != null) {
            return exists;
        }

        VerDef driverVersion = new VerDef();
        driverVersion.setFamilyName(this.familyName);
        driverVersion.setVersion(version);
        this.versions.add(driverVersion);
        this.versions.sort(VERSION_COMPARATOR);
        return driverVersion;
    }

    @Override
    public boolean hasVersion(String version) {
        return findVersion(version) != null;
    }
}
