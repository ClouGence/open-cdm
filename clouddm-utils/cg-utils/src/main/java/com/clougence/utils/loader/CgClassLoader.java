/*
 * Copyright 2008-2009 the original author or authors.
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
package com.clougence.utils.loader;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.clougence.utils.MatchUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * ResourceLoader 转 ClassLoader
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
public class CgClassLoader extends ClassLoader {

    @Getter
    private final ResourceLoader        resourceLoader;
    private final Map<String, Class<?>> loadedClass   = new ConcurrentHashMap<>();
    private final Set<String>           includePackages;
    private final Set<String>           excludePackages;
    private final String                tempDirectory = "cobbleLoader/" + System.currentTimeMillis();
    private File                        tempDir;

    public CgClassLoader(ClassLoader parent, ResourceLoader resourceLoader){
        super(parent);
        this.includePackages = new HashSet<>();
        this.excludePackages = new HashSet<>();
        this.resourceLoader = resourceLoader;
    }

    public void addIncludePackages(String packageOrClass) {
        this.includePackages.add(packageOrClass);
    }

    public void addExcludePackages(String packageOrClass) {
        this.excludePackages.add(packageOrClass);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (this.loadedClass.containsKey(name)) {
            return this.loadedClass.get(name);
        }

        if (this.includePackages.isEmpty()) {
            return super.loadClass(name, resolve);
        } else {
            for (String include : this.includePackages) {
                if (StringUtils.startsWith(name, include) || MatchUtils.matchWild(include, name)) {
                    for (String exclude : this.excludePackages) {
                        if (StringUtils.startsWith(name, exclude) || MatchUtils.matchWild(exclude, name)) {
                            return super.loadClass(name, resolve);
                        }
                    }

                    Class<?> c = this.findClass(name);
                    if (c != null && resolve) {
                        this.resolveClass(c);
                    }
                    return c;
                }
            }
            return super.loadClass(name, resolve);
        }
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        String resource = className.replace(".", "/") + ".class";

        try (InputStream inStream = this.resourceLoader.getResourceAsStream(resource)) {
            if (inStream != null) {
                return innerLoadClass(className, resource, inStream);
            }
        } catch (IOException e2) {
            throw new ClassNotFoundException(className, e2);
        }

        try (InputStream inStream = super.getResourceAsStream(resource)) {
            if (inStream != null) {
                return innerLoadClass(className, resource, inStream);
            }
        } catch (IOException e2) {
            throw new ClassNotFoundException(className, e2);
        }

        return super.findClass(className);
    }

    private Class<?> innerLoadClass(String className, String resource, InputStream inStream) throws IOException {
        int i = className.lastIndexOf('.');
        if (i != -1) {
            String pkgname = className.substring(0, i);
            Manifest man = this.resourceLoader.getManifest(resource);
            definePackageInternal(pkgname, man);
        }

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ioCopy(inStream, byteOutput);
        byte[] bs = byteOutput.toByteArray();
        Class<?> defined = this.defineClass(className, bs, 0, bs.length);
        this.loadedClass.put(className, defined);
        return defined;
    }

    @Override
    protected URL findResource(String resource) {
        try {
            return this.resourceLoader.getResource(resource);
        } catch (IOException ignored) {
        }
        return null;
    }

    @Override
    public Enumeration<URL> findResources(String resource) throws IOException {
        List<URL> resultList = new ArrayList<>();

        List<URL> resources = this.resourceLoader.getResources(resource);
        if (resources != null) {
            resultList.addAll(resources);
        }

        Iterator<URL> urlIterator = resultList.iterator();
        return new Enumeration<URL>() {

            public boolean hasMoreElements() {
                return urlIterator.hasNext();
            }

            public URL nextElement() {
                return urlIterator.next();
            }
        };
    }

    @Override
    public InputStream getResourceAsStream(String resource) {
        try {
            return this.resourceLoader.getResourceAsStream(resource);
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * @see ClassLoader#findLibrary(String)
     * @return The absolute path of the native library.
     */
    @Override
    protected String findLibrary(String sLib) {
        try {
            File tempLib = findJarNativeEntry(sLib);
            if (tempLib != null) {
                return tempLib.getAbsolutePath();
            } else {
                return super.findLibrary(sLib);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds native library entry.
     *
     * @param sLib Library name. For example for the library name "Native"
     *  - Windows returns entry "Native.dll"
     *  - Linux returns entry "libNative.so"
     *  - Mac returns entry "libNative.jnilib" or "libNative.dylib"
     *    (depending on Apple or Oracle JDK and/or JDK version)
     * @return Native library entry.
     */
    private File findJarNativeEntry(String sLib) throws IOException {
        String sName = System.mapLibraryName(sLib);
        File foundFile = this.resourceLoader.scanOneResource(ResourceLoader.MatchType.Suffix, event -> {
            // Example: sName is "Native.dll"
            String sEntry = event.getName(); // "Native.dll" or "abc/xyz/Native.dll"
            // sName "Native.dll" could be found, for example
            //   - in the path: abc/Native.dll/xyz/my.dll <-- do not load this one!
            //   - in the partial name: abc/aNative.dll   <-- do not load this one!
            String[] token = sEntry.split("/"); // the last token is library name
            if (token.length > 0 && token[token.length - 1].equals(sName)) {
                File fileTmp = createTempFile(event);
                fileTmp.deleteOnExit();
                return fileTmp;
            }
            return null;
        }, new String[] { sName });

        if (foundFile != null && foundFile.exists()) {
            return foundFile;
        }
        return null;
    }

    /**
     * Using temp files (one per inner JAR/DLL) solves many issues:
     * 1. There are no ways to load JAR defined in a JarEntry directly
     *    into the JarFile object (see also #6 below).
     * 2. Cannot use memory-mapped files because they are using
     *    nio channels, which are not supported by JarFile ctor.
     * 3. JarFile object keeps opened JAR files handlers for fast access.
     * 4. Deep resource in a jar-in-jar does not have well defined URL.
     *    Making temp file with JAR solves this problem.
     * 5. Similar issues with native libraries:
     *    <code>ClassLoader.findLibrary()</code> accepts ONLY string with
     *    absolute path to the file with native library.
     * 6. Option "java.protocol.handler.pkgs" does not allow access to nested JARs(?).
     *
     * @param inf JAR entry information.
     * @return temporary file object presenting JAR entry.
     */
    private File createTempFile(ResourceLoader.ScanEvent inf) throws IOException {
        // Temp files directory:
        //   WinXP: C:/Documents and Settings/username/Local Settings/Temp/cobbleLoader/xxxxx
        //   Unix: /var/tmp/cobbleLoader/xxxxx
        if (tempDir == null) {
            File dir = new File(System.getProperty("java.io.tmpdir"), tempDirectory);
            if (!dir.exists()) {
                dir.mkdir();
            }
            chmod777(dir); // Unix - allow temp directory RW access to all users.
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IOException("Cannot create temp directory " + dir.getAbsolutePath());
            }
            tempDir = dir;
            tempDir.deleteOnExit();
        }
        File fileTmp = File.createTempFile(inf.getName() + ".", null, tempDir);
        fileTmp.deleteOnExit();

        chmod777(fileTmp); // Unix - allow temp file deletion by any user
        //
        try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(fileTmp.toPath())); InputStream in = inf.getStream()) {
            ioCopy(in, out);
        }
        return fileTmp;
    }

    private static void ioCopy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = in.read(buffer))) {
            out.write(buffer, 0, n);
        }
    }

    private void chmod777(File file) {
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false); // Unix: allow content for dir, redundant for file
    }

    // Also called by VM to define Package for classes loaded from the CDS
    // archive
    private void definePackageInternal(String pkgname, Manifest man) {
        if (getPackage(pkgname) == null) {
            try {
                if (man != null) {
                    definePackage(pkgname, man);
                } else {
                    definePackage(pkgname, null, null, null, null, null, null, null);
                }
            } catch (IllegalArgumentException iae) {
                // parallel-capable class loaders: re-verify in case of a
                // race condition
                if (getPackage(pkgname) == null) {
                    // Should never happen
                    throw new AssertionError("Cannot find package " + pkgname);
                }
            }
        }
    }

    /**
     * Defines a new package by name in this ClassLoader. The attributes
     * contained in the specified Manifest will be used to obtain package
     * version and sealing information. For sealed packages, the additional
     * URL specifies the code source URL from which the package was loaded.
     *
     * @param name  the package name
     * @param man   the Manifest containing package version and sealing
     *              information
     * @exception   IllegalArgumentException if the package name duplicates
     *              an existing package either in this class loader or one
     *              of its ancestors
     * @return the newly defined Package object
     */
    protected Package definePackage(String name, Manifest man) throws IllegalArgumentException {
        String specTitle = null, specVersion = null, specVendor = null;
        String implTitle = null, implVersion = null, implVendor = null;
        String sealed = null;
        URL sealBase = null;

        Attributes attr = man.getMainAttributes();
        if (attr != null) {
            specTitle = attr.getValue(Attributes.Name.SPECIFICATION_TITLE);
            specVersion = attr.getValue(Attributes.Name.SPECIFICATION_VERSION);
            specVendor = attr.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            implTitle = attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            implVersion = attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            implVendor = attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            sealed = attr.getValue(Attributes.Name.SEALED);
        }
        attr = man.getMainAttributes();
        if (attr != null) {
            if (specTitle == null) {
                specTitle = attr.getValue(Attributes.Name.SPECIFICATION_TITLE);
            }
            if (specVersion == null) {
                specVersion = attr.getValue(Attributes.Name.SPECIFICATION_VERSION);
            }
            if (specVendor == null) {
                specVendor = attr.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            }
            if (implTitle == null) {
                implTitle = attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            }
            if (implVersion == null) {
                implVersion = attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            }
            if (implVendor == null) {
                implVendor = attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            }
            if (sealed == null) {
                sealed = attr.getValue(Attributes.Name.SEALED);
            }
        }
        return definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
    }
}
