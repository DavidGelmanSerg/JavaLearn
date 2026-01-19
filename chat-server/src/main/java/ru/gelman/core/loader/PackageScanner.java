package ru.gelman.core.loader;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class PackageScanner {
    public static Set<Class<?>> loadClasses(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String packageFolderName = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(packageFolderName);
            Set<Class<?>> result = new HashSet<>();
            while (resources.hasMoreElements()) {
                File directory = new File(resources.nextElement().getFile());
                if (directory.exists()) {
                    result.addAll(findClasses(directory, packageName));
                }
            }
            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<Class<?>> loadImplementations(String packageName, Class<?> clazz) {
        return loadClasses(packageName).stream()
                .filter(c -> Arrays.stream(c.getInterfaces()).toList().contains(clazz))
                .collect(Collectors.toSet());
    }

    private static Class<?> getClass(String packageName, String classFileName) {
        try {
            String className = String.format("%s.%s", packageName, classFileName.substring(0, classFileName.lastIndexOf('.')));
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private static Set<Class<?>> findClasses(File directory, String packageName) {
        File[] rootPackageDirectoryFiles = directory.listFiles();
        if (rootPackageDirectoryFiles == null) {
            return Set.of();
        }

        Set<Class<?>> result = new HashSet<>();
        for (File file : rootPackageDirectoryFiles) {
            if (file.isDirectory()) {
                String nextPackageName = String.format("%s.%s", packageName, file.getName());
                result.addAll(findClasses(file, nextPackageName));
            } else if (file.getName().endsWith(".class")) {
                String classFileName = file.getName();
                Class<?> clazz = getClass(packageName, classFileName);
                result.add(clazz);
            } else {
                log.debug("skipping file {} which is not a class", file.getName());
            }
        }
        return result;
    }
}
