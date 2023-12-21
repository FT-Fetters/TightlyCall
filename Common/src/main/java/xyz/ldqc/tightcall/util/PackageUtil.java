package xyz.ldqc.tightcall.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Fetters
 */
public class PackageUtil {

    private static final String CLASS_SUFFIX = ".class";

    public static List<Class<?>> getPackageClasses(String packageName) throws IOException {
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return Collections.emptyList();
        }
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            return getMultipleClasses(file, path, classLoader);
        } else {
            return Collections.singletonList(getSingleClass(file, path, classLoader));
        }
    }

    private static Class<?> getSingleClass(
            File classFile, String basePath, ClassLoader classLoader) {
        String classFileName = classFile.getName();
        if (!classFileName.endsWith(CLASS_SUFFIX)) {
            return null;
        }
        String className = basePath.replace("/", ".") +
                "." +
                classFileName.replace(".class", "");
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Class<?>> getMultipleClasses(
            File baseFile, String basePath, ClassLoader classLoader) {
        List<File> classFiles = new ArrayList<>();
        Queue<File> q = new ArrayDeque<>();
        q.offer(baseFile);
        bfs(q, classFiles);
        if (classFiles.isEmpty()) {
            return Collections.emptyList();
        }
        return classFileList2ClassList(classFiles, baseFile, basePath, classLoader);
    }

    private static void bfs(Queue<File> q, List<File> result) {
        while (!q.isEmpty()) {
            doBfs(q, result);
        }
    }

    private static void doBfs(Queue<File> q, List<File> result) {
        File cur = q.poll();
        if (cur == null) {
            return;
        }
        File[] subs = cur.listFiles();
        if (subs == null) {
            return;
        }
        for (File file : subs) {
            if (file.isDirectory()) {
                q.offer(file);
            } else if (file.getName().endsWith(CLASS_SUFFIX)) {
                result.add(file);
            }
        }
    }

    private static List<Class<?>> classFileList2ClassList(
            List<File> classFiles, File baseFile, String basePath, ClassLoader classLoader) {
        List<Class<?>> classes = new ArrayList<>();
        for (File classFile : classFiles) {
            String className = classFile2ClassName(classFile, baseFile, basePath);
            try {
                classes.add(classLoader.loadClass(className));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }

    private static String classFile2ClassName(
            File classFile, File baseFile, String basePath) {
        basePath = basePath.replace("/", ".");
        return basePath + classFile.getAbsolutePath()
                .replace(baseFile.getAbsolutePath(), "")
                .replace("\\", ".")
                .replace(".class", "");

    }

}
