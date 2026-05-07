package com.clougence.drivers.factory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.clougence.drivers.DriverLoader;
import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.DriverXmlLoader;
import com.clougence.drivers.def.FamilyDef;
import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.def.VerDef;
import com.clougence.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDriverLoader implements DriverLoader {

    private final File                                 localDir;
    private final Properties                           config;
    private final DriverXmlLoader                      driverXmlLoader;
    private final List<FamilyDef>                      familyDefs;
    private final List<DsFactoryDef>                   dsFactoryDefs;
    private final Map<String, ResourcePreparerFactory> resourcePreparerFactories;
    private final Map<String, ResourcePreparer>        resourcePreparers;

    public AbstractDriverLoader(File localDir, Properties config){
        this.localDir = localDir;
        this.config = config != null ? config : new Properties();
        this.familyDefs = new ArrayList<>();
        this.dsFactoryDefs = new ArrayList<>();
        this.driverXmlLoader = new DriverXmlLoader();
        this.resourcePreparerFactories = new ConcurrentHashMap<>();
        this.resourcePreparers = new ConcurrentHashMap<>();
    }

    @Override
    public void registerPreparer(String resourceType, ResourcePreparerFactory preparerFactory) {
        if (preparerFactory == null) {
            unregisterPreparer(resourceType);
            return;
        }

        this.resourcePreparerFactories.put(resourceType, preparerFactory);
        this.resourcePreparers.remove(resourceType);
    }

    @Override
    public void unregisterPreparer(String resourceType) {
        this.resourcePreparerFactories.remove(resourceType);
        this.resourcePreparers.remove(resourceType);
    }

    //

    @Override
    public File getDriverHome() { return this.localDir; }

    @Override
    public List<String> familyNames() {
        return this.familyDefs.stream().map(FamilyDef::getFamilyName).distinct().collect(Collectors.toList());
    }

    @Override
    public FamilyDef findDriver(String driverFamily) {
        if (StringUtils.isBlank(driverFamily)) {
            return null;
        }

        synchronized (this.familyDefs) {
            for (FamilyDef familyDef : this.familyDefs) {
                if (StringUtils.equalsIgnoreCase(familyDef.familyName, driverFamily)) {
                    return familyDef;
                }
            }
        }
        return null;
    }

    @Override
    public VerDef findDriver(String driverFamily, String driverVersion) {
        FamilyDef familyDef = findDriver(driverFamily);
        return familyDef == null ? null : familyDef.findVersion(driverVersion);
    }

    @Override
    public FamilyDef addDriver(String driverFamily) {
        if (StringUtils.isBlank(driverFamily)) {
            return null;
        }

        synchronized (this.familyDefs) {
            for (FamilyDef familyDef : this.familyDefs) {
                if (StringUtils.equalsIgnoreCase(familyDef.familyName, driverFamily)) {
                    return familyDef;
                }
            }

            FamilyDef familyDef = new FamilyDef();
            familyDef.familyName = driverFamily.trim();
            this.familyDefs.add(familyDef);
            return familyDef;
        }
    }

    @Override
    public void loadDriverXml(InputStream xmlInputStream) {
        for (FamilyDef loadedDriver : this.driverXmlLoader.load(xmlInputStream)) {
            if (loadedDriver == null || StringUtils.isBlank(loadedDriver.familyName)) {
                continue;
            }

            FamilyDef targetDriver = addDriver(loadedDriver.familyName);
            if (targetDriver == null) {
                continue;
            }
            targetDriver.familyName = StringUtils.defaultIfBlank(targetDriver.familyName, loadedDriver.familyName);

            for (VerDef loadedVersion : loadedDriver.versions) {
                if (loadedVersion == null) {
                    continue;
                }

                VerDef targetVersion = targetDriver.addVersion(loadedVersion.getVersion());
                if (targetVersion == null) {
                    continue;
                }

                targetVersion.setFamilyName(StringUtils.defaultIfBlank(targetVersion.getFamilyName(), loadedVersion.getFamilyName()));
                targetVersion.setLocalDir(this.localDir);
                if (StringUtils.isNotBlank(loadedVersion.getDsFactory())) {
                    targetVersion.setDsFactory(loadedVersion.getDsFactory());
                }
                if (StringUtils.isNotBlank(loadedVersion.getComment())) {
                    targetVersion.setComment(loadedVersion.getComment());
                }
                if (loadedVersion.getLoader() != null) {
                    targetVersion.setLoader(loadedVersion.getLoader());
                }
                if (loadedVersion.getDsFactoryDef() != null) {
                    targetVersion.setDsFactoryDef(loadedVersion.getDsFactoryDef());
                }
                if (targetVersion.getDsFactoryDef() == null && StringUtils.isNotBlank(targetVersion.getDsFactory())) {
                    targetVersion.setDsFactoryDef(findDsFactoryDef(targetVersion.getDsFactory()));
                }
                if (loadedVersion.getLoaderMap() != null) {
                    targetVersion.setLoaderMap(loadedVersion.getLoaderMap());
                }

                for (ResDef resource : loadedVersion.getResources()) {
                    targetVersion.addResource(resource);
                }
            }
        }
    }

    //

    @Override
    public void refreshDriverVersion(DriverVersion driverVersion) {
        this.refreshPreparedState((VerDef) driverVersion);
    }

    private void refreshPreparedState(VerDef driverVersion) {
        if (driverVersion == null) {
            return;
        }

        List<ResDef> resources = driverVersion.getResources();
        if (resources == null || resources.isEmpty()) {
            driverVersion.setPrepared(true);
            return;
        }

        ClassLoader dsFactoryClassLoader = resolveDsFactoryClassLoader(driverVersion);
        boolean allPrepared = true;
        for (ResDef driverResource : resources) {
            if (driverResource == null) {
                allPrepared = false;
                continue;
            }

            ResourcePreparer preparer = getPreparer(driverResource.getResourceType());
            if (preparer == null) {
                driverResource.setPrepared(false);
                allPrepared = false;
                continue;
            }

            try {
                if (restoreFilesIndexQuietly(preparer, driverVersion, driverResource)) {
                    preparer.refresh(driverVersion, driverResource, dsFactoryClassLoader, ResourcePreparer.NONE);
                    allPrepared = allPrepared && driverResource.isPrepared();
                    continue;
                }

                preparer.analysis(driverVersion, driverResource, dsFactoryClassLoader, ResourcePreparer.NONE);
                syncFilesIndex(preparer, driverVersion, driverResource);
                preparer.refresh(driverVersion, driverResource, dsFactoryClassLoader, ResourcePreparer.NONE);
                allPrepared = allPrepared && driverResource.isPrepared();
            } catch (Exception e) {
                if (!restoreFilesIndex(preparer, driverVersion, driverResource, e)) {
                    log.error(e.getMessage(), e);
                    driverResource.setPrepared(false);
                    allPrepared = false;
                    continue;
                }

                try {
                    preparer.refresh(driverVersion, driverResource, dsFactoryClassLoader, ResourcePreparer.NONE);
                    allPrepared = allPrepared && driverResource.isPrepared();
                } catch (Exception refreshException) {
                    log.error(refreshException.getMessage(), refreshException);
                    driverResource.setPrepared(false);
                    allPrepared = false;
                }
            }
        }

        driverVersion.setPrepared(allPrepared);
    }

    private boolean restoreFilesIndexQuietly(ResourcePreparer preparer, DriverVersion driverVersion, ResDef driverResource) {
        if (!(preparer instanceof com.clougence.drivers.factory.prepare.AbstractResourcePreparer abstractResourcePreparer)) {
            return false;
        }

        try {
            return abstractResourcePreparer.restoreFilesIndex(driverVersion, driverResource);
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
            return false;
        }
    }

    @Override
    public void prepareDriverVersion(DriverVersion driverVersion, Predicate<ResDef> skip, DriverPrepareProgress progress) {
        List<ResDef> resources = driverVersion.getResources();
        if (resources == null || resources.isEmpty()) {
            driverVersion.setPrepared(true);
            return;
        }

        ClassLoader dsFactoryClassLoader = resolveDsFactoryClassLoader(driverVersion);
        boolean allPrepared = true;
        for (int i = 0; i < resources.size(); i++) {
            ResDef driverResource = resources.get(i);
            int currentIndex = i + 1;

            if (skip != null && skip.test(driverResource)) {
                continue;
            }

            progress.onStart(driverVersion, driverResource, currentIndex, resources.size());

            ResourcePreparer preparer = getPreparer(driverResource.getResourceType());
            if (preparer == null) {
                throw new IllegalArgumentException("unsupported resourceType: " + driverResource.getResourceType());
            }

            try {
                preparer.analysis(driverVersion, driverResource, dsFactoryClassLoader, progress);
                syncFilesIndex(preparer, driverVersion, driverResource);
                preparer.resolve(driverVersion, driverResource, dsFactoryClassLoader, progress);
                syncFilesIndex(preparer, driverVersion, driverResource);
                allPrepared = allPrepared && driverResource.isPrepared();
                progress.onComplete(driverVersion, driverResource, currentIndex, resources.size());
            } catch (Exception e) {
                progress.onError(driverVersion, driverResource, e);
                return;
            }
        }

        driverVersion.setPrepared(allPrepared);
    }

    private ResourcePreparer getPreparer(String resourceType) {
        if (StringUtils.isBlank(resourceType)) {
            return null;
        }

        ResourcePreparerFactory currentFactory = this.resourcePreparerFactories.get(resourceType);
        if (currentFactory == null) {
            this.resourcePreparers.remove(resourceType);
            return null;
        }

        ResourcePreparer cachedPreparer = this.resourcePreparers.get(resourceType);
        if (cachedPreparer != null) {
            return cachedPreparer;
        }

        ResourcePreparer preparer = currentFactory.create(this.localDir, this.config);
        if (preparer == null) {
            this.resourcePreparers.remove(resourceType);
            return null;
        }

        this.resourcePreparers.put(resourceType, preparer);
        return preparer;
    }

    private ClassLoader resolveDsFactoryClassLoader(DriverVersion driverVersion) {
        if (!(driverVersion instanceof VerDef)) {
            return null;
        }

        VerDef version = (VerDef) driverVersion;
        if (version.getDsFactoryDef() == null) {
            return null;
        }
        return version.getDsFactoryDef().getDsFactoryClassLoader();
    }

    private void syncFilesIndex(ResourcePreparer preparer, DriverVersion driverVersion, ResDef driverResource) throws IOException {
        if (preparer instanceof com.clougence.drivers.factory.prepare.AbstractResourcePreparer abstractResourcePreparer) {
            abstractResourcePreparer.updateFilesIndex(driverVersion, driverResource);
        }
    }

    private boolean restoreFilesIndex(ResourcePreparer preparer, DriverVersion driverVersion, ResDef driverResource, Exception cause) {
        if (!(preparer instanceof com.clougence.drivers.factory.prepare.AbstractResourcePreparer abstractResourcePreparer)) {
            return false;
        }

        try {
            boolean restored = abstractResourcePreparer.restoreFilesIndex(driverVersion, driverResource);
            if (restored) {
                log.warn("analysis failed, fallback to files.idx, family={}, version={}, resourceType={}, coordinate={}", driverVersion.getFamilyName(),
                    driverVersion.getVersion(), driverResource.getResourceType(), driverResource.getCoordinate(), cause);
            }
            return restored;
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
            return false;
        }
    }

    //

    @Override
    public void loadDsFactory(ClassLoader classLoader) throws Exception {
        if (classLoader == null) {
            return;
        }

        synchronized (this.dsFactoryDefs) {
            for (String dsFactoryName : loadDsFactoryNames(classLoader)) {
                registerDsFactoryDef(dsFactoryName, classLoader);
            }
        }

        List<VerDef> driverVersions = new ArrayList<>();
        synchronized (this.familyDefs) {
            for (FamilyDef familyDef : this.familyDefs) {
                for (VerDef driverVersion : familyDef.versions) {
                    if (driverVersion == null || StringUtils.isBlank(driverVersion.getDsFactory())) {
                        continue;
                    }

                    if (driverVersion.getDsFactoryDef() == null) {
                        driverVersion.setDsFactoryDef(findDsFactoryDef(driverVersion.getDsFactory()));
                    }
                    driverVersions.add(driverVersion);
                }
            }
        }

        for (VerDef driverVersion : driverVersions) {
            this.refreshPreparedState(driverVersion);
        }
    }

    protected DsFactoryDef findDsFactoryDef(String dsFactoryName) {
        if (StringUtils.isBlank(dsFactoryName)) {
            return null;
        }

        synchronized (this.dsFactoryDefs) {
            for (DsFactoryDef dsFactoryDef : this.dsFactoryDefs) {
                if (StringUtils.equals(dsFactoryDef.getDsFactoryName(), dsFactoryName)) {
                    return dsFactoryDef;
                }
            }
        }
        return null;
    }

    private void registerDsFactoryDef(String dsFactoryName, ClassLoader classLoader) {
        if (dsFactoryName == null) {
            return;
        }

        for (DsFactoryDef dsFactoryDef : this.dsFactoryDefs) {
            boolean testFactoryName = StringUtils.equals(dsFactoryDef.getDsFactoryName(), dsFactoryName);
            if (testFactoryName) {
                return;
            }
        }

        this.dsFactoryDefs.add(new DsFactoryDef(dsFactoryName, classLoader));
    }

    private List<String> loadDsFactoryNames(ClassLoader classLoader) {
        List<String> dsFactoryNames = new ArrayList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources("META-INF/services/com.clougence.drivers.DsFactory");
            boolean foundResource = false;
            while (resources.hasMoreElements()) {
                foundResource = true;
                URL resource = resources.nextElement();
                if (resource == null) {
                    continue;
                }
                try (InputStream inputStream = resource.openStream()) {
                    loadDsFactoryNames(inputStream, dsFactoryNames);
                }
            }
            if (!foundResource) {
                try (InputStream inputStream = classLoader.getResourceAsStream("META-INF/services/com.clougence.drivers.DsFactory")) {
                    loadDsFactoryNames(inputStream, dsFactoryNames);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("load DsFactory SPI failed", e);
        }
        return dsFactoryNames;
    }

    private void loadDsFactoryNames(InputStream inputStream, List<String> dsFactoryNames) throws IOException {
        if (inputStream == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String factoryName = normalizeFactoryName(line);
                if (factoryName != null) {
                    dsFactoryNames.add(factoryName);
                }
            }
        }
    }

    private String normalizeFactoryName(String line) {
        if (line == null) {
            return null;
        }

        int commentIndex = line.indexOf('#');
        String candidate = commentIndex >= 0 ? line.substring(0, commentIndex) : line;
        return StringUtils.trimToNull(candidate);
    }
}
