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
package com.clougence.clouddm.inner.drivers.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;

import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.FileDef;
import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.factory.prepare.AbstractResourcePreparer;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FileUtils;

public class MavenResourcePreparer extends AbstractResourcePreparer {

    public static final String                    REPOSITORY_KEY           = "cg.driver.maven.repository";
    public static final String                    DEFAULT_MAVEN_REPOSITORY = "https://repo1.maven.org/maven2";
    private final RepositorySystem                repositorySystem;
    protected final File                          analysisDir;
    private final Map<String, List<MavenFileDef>> analysisCache            = new ConcurrentHashMap<>();

    public MavenResourcePreparer(File localDir, Properties config){
        super(localDir, config);
        this.analysisDir = new File(localDir, ".maven-analysis");
        this.repositorySystem = createRepositorySystem();
    }

    @Override
    public void analysis(DriverVersion driverVersion, ResDef driverResource, ClassLoader classLoader, DriverPrepareProgress progress) throws Exception {
        File versionDir = resolveVersionDir(driverVersion);
        String analysisCacheKey = buildAnalysisCacheKey(versionDir, driverResource);
        List<MavenFileDef> cachedFileDefs = this.analysisCache.get(analysisCacheKey);
        if (cachedFileDefs != null) {
            driverResource.setFileDefList(cloneFileDefs(cachedFileDefs));
            return;
        }

        DefaultArtifact artifact = buildArtifact(driverResource);
        File driverRootDir = this.localDir;
        if (!driverRootDir.exists() && !driverRootDir.mkdirs()) {
            throw new IOException("failed to create driver root directory: " + driverRootDir.getAbsolutePath());
        }

        this.analysisDir.mkdirs();
        File tempRepoDir = Files.createTempDirectory(analysisDir.toPath(), ".analysis-").toFile();
        DefaultRepositorySystemSession session = createSession(driverVersion, driverResource, tempRepoDir, null);
        List<RemoteRepository> repositories = resolveRepositories();

        try {
            List<Artifact> jarArtifacts = collectJarArtifacts(session, artifact, repositories);
            List<MavenFileDef> analyzedFileDefs = buildJarFileDefs(jarArtifacts, versionDir, repositories);
            this.analysisCache.put(analysisCacheKey, cloneMavenFileDefs(analyzedFileDefs));
            driverResource.setFileDefList(cloneFileDefs(analyzedFileDefs));
            updateFilesIndex(driverVersion, driverResource);
        } finally {
            if (tempRepoDir.exists()) {
                FileUtils.forceDelete(tempRepoDir);
            }
        }
    }

    private String buildAnalysisCacheKey(File versionDir, ResDef driverResource) {
        return versionDir.getAbsolutePath() + "::" + StringUtils.trim(driverResource.getCoordinate());
    }

    private List<FileDef> cloneFileDefs(List<MavenFileDef> fileDefs) {
        return new ArrayList<>(cloneMavenFileDefs(fileDefs));
    }

    private List<MavenFileDef> cloneMavenFileDefs(List<MavenFileDef> fileDefs) {
        List<MavenFileDef> clones = new ArrayList<>(fileDefs.size());
        for (MavenFileDef source : fileDefs) {
            MavenFileDef target = new MavenFileDef();
            target.setRelativePath(source.getRelativePath());
            target.setAbsolutePath(source.getAbsolutePath());
            target.setPrepared(source.isPrepared());
            target.setUrl(source.getUrl());
            clones.add(target);
        }
        return clones;
    }

    @Override
    public void refresh(DriverVersion driverVersion, ResDef resDef, ClassLoader classLoader, DriverPrepareProgress progress) throws IOException {
        super.refresh(driverVersion, resDef, classLoader, progress);
    }

    @Override
    public void resolve(DriverVersion driverVersion, ResDef driverResource, ClassLoader classLoader, DriverPrepareProgress progress) throws Exception {
        File versionDir = resolveVersionDir(driverVersion);
        DefaultArtifact artifact = buildArtifact(driverResource);
        File tempRepoDir = new File(versionDir, ".maven-temp-repo");
        if (tempRepoDir.exists()) {
            FileUtils.forceDelete(tempRepoDir);
        }

        DefaultRepositorySystemSession session = createSession(driverVersion, driverResource, tempRepoDir, progress);
        List<RemoteRepository> repositories = resolveRepositories();

        try {
            List<Artifact> jarArtifacts = collectJarArtifacts(session, artifact, repositories);
            Map<String, MavenFileDef> fileDefMap = buildJarFileDefMap(jarArtifacts, versionDir, repositories);

            for (Artifact jarArtifact : jarArtifacts) {
                ArtifactResult artifactResult = repositorySystem.resolveArtifact(session, buildArtifactRequest(jarArtifact, repositories));
                File sourceFile = artifactResult == null || artifactResult.getArtifact() == null ? null : artifactResult.getArtifact().getFile();
                if (sourceFile == null || !sourceFile.isFile()) {
                    throw new IOException("resolved maven artifact file not found: " + buildRepositoryPath(jarArtifact));
                }

                MavenFileDef fileDef = fileDefMap.get(buildRepositoryPath(jarArtifact));
                File targetFile = new File(versionDir, fileDef.getRelativePath());
                File parent = targetFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                fileDef.setAbsolutePath(targetFile.getAbsolutePath());
                fileDef.setPrepared(true);
            }

            List<FileDef> fileDefs = new ArrayList<>(fileDefMap.values());
            driverResource.setFileDefList(fileDefs);
            driverResource.setPrepared(fileDefs.stream().allMatch(FileDef::isPrepared));
            updateFilesIndex(driverVersion, driverResource);
        } finally {
            if (tempRepoDir.exists()) {
                FileUtils.forceDelete(tempRepoDir);
            }
        }
    }

    private DefaultArtifact buildArtifact(ResDef driverResource) {
        String coordinate = driverResource.getCoordinate().trim();
        String[] segments = parseMavenCoordinate(coordinate);
        String classifier = segments[4] != null ? segments[4] : "";
        return new DefaultArtifact(segments[0], segments[1], classifier, segments[3], segments[2]);
    }

    private File resolveVersionDir(DriverVersion driverVersion) {
        if (driverVersion == null || StringUtils.isBlank(driverVersion.getFamilyName()) || StringUtils.isBlank(driverVersion.getVersion())) {
            throw new IllegalArgumentException("driverVersion is incomplete for maven resource preparation");
        }

        File absoluteDir = driverVersion.getAbsoluteDir();
        if (absoluteDir == null) {
            throw new IllegalArgumentException("driverVersion absoluteDir is unavailable for maven resource preparation");
        }

        return absoluteDir;
    }

    private static RepositorySystem createRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }

    private String trimRepository(String repository) {
        String target = repository.trim();
        while (target.endsWith("/")) {
            target = target.substring(0, target.length() - 1);
        }
        return target;
    }

    private DefaultRepositorySystemSession createSession(DriverVersion driverVersion, ResDef driverResource, File versionDir, DriverPrepareProgress progress) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepositoryManager repositoryManager = repositorySystem.newLocalRepositoryManager(session, new LocalRepository(versionDir));
        session.setLocalRepositoryManager(repositoryManager);
        session.setOffline(false);
        if (progress != null) {
            session.setTransferListener(new MavenProgressTransferListener(driverVersion, driverResource, progress));
        }
        return session;
    }

    private List<Artifact> collectJarArtifacts(DefaultRepositorySystemSession session, Artifact artifact, List<RemoteRepository> repositories) throws Exception {
        CollectRequest collectRequest = new CollectRequest(new Dependency(artifact, JavaScopes.RUNTIME), repositories);
        CollectResult collectResult = repositorySystem.collectDependencies(session, collectRequest);
        if (!collectResult.getExceptions().isEmpty()) {
            throw collectResult.getExceptions().get(0);
        }

        Map<String, Artifact> artifactMap = new LinkedHashMap<>();
        collectJarArtifacts(collectResult.getRoot(), artifactMap);
        return new ArrayList<>(artifactMap.values());
    }

    private List<RemoteRepository> resolveRepositories() {
        String repoUrl = trimRepository(config.getProperty(REPOSITORY_KEY, DEFAULT_MAVEN_REPOSITORY));
        RemoteRepository remoteRepo = new RemoteRepository.Builder("central", "default", repoUrl).build();
        return Collections.singletonList(remoteRepo);
    }

    private ArtifactRequest buildArtifactRequest(Artifact artifact, List<RemoteRepository> repositories) {
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(repositories);
        return artifactRequest;
    }

    private void collectJarArtifacts(DependencyNode node, Map<String, Artifact> artifactMap) {
        if (node == null) {
            return;
        }

        Dependency dependency = node.getDependency();
        Artifact artifact = dependency != null ? dependency.getArtifact() : null;
        addJarArtifact(artifact, artifactMap);

        if (node.getChildren() == null) {
            return;
        }
        for (DependencyNode child : node.getChildren()) {
            collectJarArtifacts(child, artifactMap);
        }
    }

    private void addJarArtifact(Artifact artifact, Map<String, Artifact> artifactMap) {
        if (!isJarArtifact(artifact)) {
            return;
        }

        String repositoryPath = buildRepositoryPath(artifact);
        if (!artifactMap.containsKey(repositoryPath)) {
            artifactMap.put(repositoryPath, artifact);
        }
    }

    private List<MavenFileDef> buildJarFileDefs(List<Artifact> jarArtifacts, File versionDir, List<RemoteRepository> repositories) {
        return new ArrayList<>(buildJarFileDefMap(jarArtifacts, versionDir, repositories).values());
    }

    private Map<String, MavenFileDef> buildJarFileDefMap(List<Artifact> jarArtifacts, File versionDir, List<RemoteRepository> repositories) {
        Map<String, MavenFileDef> fileDefMap = new LinkedHashMap<>();
        Map<String, String> usedRelativePathMap = new LinkedHashMap<>();
        for (Artifact artifact : jarArtifacts) {
            String relativePath = resolveFlatRelativePath(artifact, usedRelativePathMap);
            MavenFileDef fileDef = new MavenFileDef();
            fileDef.setRelativePath(relativePath);
            fileDef.setAbsolutePath(new File(versionDir, relativePath).getAbsolutePath());
            fileDef.setUrl(buildArtifactUrl(artifact, repositories));
            fileDefMap.put(buildRepositoryPath(artifact), fileDef);
        }

        return fileDefMap;
    }

    private String resolveFlatRelativePath(Artifact artifact, Map<String, String> usedRelativePathMap) {
        String baseName = buildFlatFileName(artifact);
        if (!usedRelativePathMap.containsKey(baseName)) {
            usedRelativePathMap.put(baseName, buildRepositoryPath(artifact));
            return baseName;
        }

        String candidate = artifact.getGroupId().replace('.', '-') + "-" + baseName;
        if (!usedRelativePathMap.containsKey(candidate)) {
            usedRelativePathMap.put(candidate, buildRepositoryPath(artifact));
            return candidate;
        }

        int index = 2;
        while (usedRelativePathMap.containsKey(index + "-" + candidate)) {
            index++;
        }
        String resolved = index + "-" + candidate;
        usedRelativePathMap.put(resolved, buildRepositoryPath(artifact));
        return resolved;
    }

    private String buildFlatFileName(Artifact artifact) {
        StringBuilder builder = new StringBuilder();
        builder.append(artifact.getArtifactId()).append('-').append(artifact.getVersion());
        if (StringUtils.isNotBlank(artifact.getClassifier())) {
            builder.append('-').append(artifact.getClassifier());
        }
        builder.append('.').append(artifact.getExtension());
        return builder.toString();
    }

    private boolean isJarArtifact(Artifact artifact) {
        return artifact != null && StringUtils.equalsIgnoreCase("jar", artifact.getExtension());
    }

    private String buildArtifactUrl(Artifact artifact, List<RemoteRepository> repositories) {
        if (repositories == null || repositories.isEmpty()) {
            return null;
        }

        String repository = trimRepository(repositories.get(0).getUrl());
        return repository + "/" + buildRepositoryPath(artifact);
    }

    private String buildRepositoryPath(Artifact artifact) {
        StringBuilder builder = new StringBuilder();
        builder.append(artifact.getGroupId().replace('.', '/')).append('/');
        builder.append(artifact.getArtifactId()).append('/');
        builder.append(artifact.getVersion()).append('/');
        builder.append(artifact.getArtifactId()).append('-').append(artifact.getVersion());
        if (StringUtils.isNotBlank(artifact.getClassifier())) {
            builder.append('-').append(artifact.getClassifier());
        }
        builder.append('.').append(artifact.getExtension());
        return builder.toString();
    }

    private String[] parseMavenCoordinate(String coordinate) {
        String[] segments = coordinate.trim().split(":");
        if (segments.length < 3 || segments.length > 5) {
            throw new IllegalArgumentException("unsupported maven coordinate: " + coordinate);
        }

        String groupId = segments[0].trim();
        String artifactId = segments[1].trim();
        String version;
        String packaging = "jar";
        String classifier = null;
        if (segments.length == 3) {
            version = segments[2].trim();
        } else if (segments.length == 4) {
            version = segments[3].trim();
            if (isPackagingName(segments[2])) {
                packaging = segments[2].trim();
            } else {
                classifier = segments[2].trim();
            }
        } else {
            packaging = segments[2].trim();
            classifier = StringUtils.trimToNull(segments[3]);
            version = segments[4].trim();
        }

        if (StringUtils.isBlank(groupId) || StringUtils.isBlank(artifactId) || StringUtils.isBlank(version)) {
            throw new IllegalArgumentException("bad maven coordinate: " + coordinate);
        }
        return new String[] { groupId, artifactId, version, StringUtils.defaultIfBlank(packaging, "jar"), classifier };
    }

    private boolean isPackagingName(String value) {
        String normalized = StringUtils.trimToNull(value);
        if (normalized == null) {
            return false;
        }
        switch (normalized.toLowerCase()) {
            case "jar":
            case "pom":
            case "zip":
            case "tar":
            case "tar.gz":
            case "aar":
            case "war":
                return true;
            default:
                return false;
        }
    }
}
