package com.clougence.drivers.factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import com.clougence.drivers.DriverBinding;
import com.clougence.drivers.DriverFile;
import com.clougence.drivers.def.VerDef;
import com.clougence.utils.StringUtils;
import com.clougence.utils.loader.CgClassLoader;
import com.clougence.utils.loader.ResourceLoader;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;
import com.clougence.utils.loader.providers.ImportResourceLoader;
import com.clougence.utils.loader.providers.JarResourceLoader;

final class DriverBindingImpl implements DriverBinding {

    private final ImportResourceLoader resourceChain;
    private final CgClassLoader        classLoader;
    private final VerDef               driverVersion;
    private final long                 stateTimestamp;

    DriverBindingImpl(ClassLoader parent, VerDef driverVersion){
        this.driverVersion = driverVersion;
        this.stateTimestamp = driverVersion.getTimestamp();
        this.resourceChain = new ImportResourceLoader();

        if (driverVersion.getLoader() != null) {
            this.resourceChain.importResources(driverVersion.getLoader());
        }

        this.bindPreparedDriverFiles(this.driverVersion);

        this.classLoader = this.resourceChain.toClassLoader(parent);
        this.bind(driverVersion.getDsFactoryDef().getDsFactoryClassLoader());
    }

    private void bindPreparedDriverFiles(VerDef driverVersion) {
        if (driverVersion == null) {
            return;
        }

        for (DriverFile driverFile : driverVersion.getFiles()) {
            if (driverFile == null || !driverFile.isPrepared()) {
                continue;
            }

            String absolutePath = StringUtils.trimToNull(driverFile.getAbsolutePath());
            if (absolutePath == null) {
                throw new IllegalStateException("load prepared driver file failed: absolutePath is blank, relativePath=" + driverFile.getRelativePath());
            }

            File sourceFile = new File(absolutePath);
            if (!sourceFile.exists()) {
                throw new IllegalStateException("load prepared driver file failed: file not found, path=" + sourceFile.getAbsolutePath());
            }
            if (!sourceFile.isFile()) {
                throw new IllegalStateException("load prepared driver file failed: path is not a file, path=" + sourceFile.getAbsolutePath());
            }
            if (!sourceFile.canRead()) {
                throw new IllegalStateException("load prepared driver file failed: file is not readable, path=" + sourceFile.getAbsolutePath());
            }
            if (!isArchiveFile(sourceFile.getName())) {
                throw new IllegalStateException("load prepared driver file failed: unsupported archive file, path=" + sourceFile.getAbsolutePath());
            }

            try {
                this.resourceChain.importResources(new JarResourceLoader(sourceFile));
            } catch (IOException e) {
                throw new IllegalStateException("load prepared driver file failed: path=" + sourceFile.getAbsolutePath(), e);
            }
        }
    }

    private boolean isArchiveFile(String fileName) {
        String normalized = StringUtils.trimToNull(fileName);
        if (normalized == null) {
            return false;
        }

        String lowerCase = normalized.toLowerCase();
        return lowerCase.endsWith(".jar") || lowerCase.endsWith(".zip");
    }

    @Override
    public void bind(ClassLoader classLoader, String... imports) {
        if (classLoader == null || classLoader == this.classLoader) {
            return;
        }

        this.importResources(new ClassPathResourceLoader(classLoader, ""), imports);
    }

    @Override
    public void bind(ResourceLoader resourceLoader, String... imports) {
        if (resourceLoader == null || resourceLoader == this.resourceChain) {
            return;
        }

        this.importResources(resourceLoader, imports);
    }

    @Override
    public void bind(ClassLoader classLoader) {
        this.bind(classLoader, (String[]) null);
    }

    @Override
    public void bind(ResourceLoader resourceLoader) {
        this.bind(resourceLoader, (String[]) null);
    }

    @Override
    public CgClassLoader asClassLoader() {
        return this.classLoader;
    }

    @Override
    public boolean isExpired() {
        long currentTimestamp = this.driverVersion != null ? this.driverVersion.getTimestamp() : -1L;
        return this.stateTimestamp != currentTimestamp;
    }

    private void importResources(ResourceLoader resourceLoader, String... imports) {
        List<String> normalizedImports = normalizeImports(imports);
        if (normalizedImports.isEmpty()) {
            this.resourceChain.importResources(resourceLoader);
            return;
        }

        for (String item : normalizedImports) {
            if (item.isEmpty()) {
                this.resourceChain.importResources(resourceLoader);
            } else {
                this.resourceChain.importResources(item, resourceLoader);
            }
        }
    }

    private static List<String> normalizeImports(String... imports) {
        if (imports == null || imports.length == 0) {
            return Collections.emptyList();
        }

        LinkedHashSet<String> result = new LinkedHashSet<>();
        for (String item : imports) {
            String normalized = StringUtils.trimToNull(item);
            if (normalized == null) {
                continue;
            }

            if ("*".equals(normalized)) {
                result.add("");
                continue;
            }

            normalized = normalized.replace('\\', '/');
            while (normalized.startsWith("/")) {
                normalized = normalized.substring(1);
            }

            result.add(normalized);
            if (normalized.indexOf('/') < 0 && normalized.indexOf('.') >= 0) {
                result.add(normalized.replace('.', '/'));
            }
        }
        return new ArrayList<>(result);
    }
}
