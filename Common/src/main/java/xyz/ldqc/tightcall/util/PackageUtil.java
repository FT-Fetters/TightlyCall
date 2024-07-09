package xyz.ldqc.tightcall.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Fetters
 */
public class PackageUtil {

    private PackageUtil() {
        throw new UnsupportedOperationException();
    }

    private static final String CLASS_SUFFIX = ".class";
    public static final String FILE_PREFIX = "file:";
    public static final String MAGIC_MARK = "!";
    public static final String OS_NAME = "os.name";
    public static final String WIN = "win";

    public static List<Class<?>> getPackageClasses(final String packageName, Class<?> runClass) {
        if (isClassRunningFromJar(runClass)) {
            return new ArrayList<>(getClassNamesFromJarPackage(packageName, runClass));
        }
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

    /**
     * 获取当前JAR包中指定包下的所有类名。
     *
     * @param packageName 要搜索的包名。
     * @return 包含所有类名的集合。
     */
    public static Set<Class<?>> getClassNamesFromJarPackage(final String packageName,
        Class<?> runClass) {
        Set<String> classNames = new HashSet<>();
        // 获取当前类的保护域代码源的位置
        String jarPath = runClass.getProtectionDomain().getCodeSource().getLocation()
            .getPath();

        // 处理文件路径在不同操作系统和环境下的差异
        if (jarPath.startsWith(FILE_PREFIX)) {
            if (jarPath.contains(MAGIC_MARK)) {
                jarPath = jarPath.substring(5, jarPath.indexOf(MAGIC_MARK));
            } else {
                jarPath = jarPath.substring(5);
            }
        }
        // 对于Windows系统，去除路径前的"/"
        if (System.getProperty(OS_NAME).toLowerCase().contains(WIN) && jarPath.startsWith("/")) {
            jarPath = jarPath.substring(1);
        }

        try (JarFile jarFile = new JarFile(jarPath)) {
            // 获取JAR文件中的所有实体
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                // 将包名转换为路径格式，并检查实体是否在指定包下
                if (entryName.startsWith(packageName.replace(".", "/")) && entryName.endsWith(
                    CLASS_SUFFIX)) {
                    // 移除.class扩展名，并将路径格式转换回包名格式
                    String className = entryName.replace('/', '.')
                        .substring(0, entryName.length() - 6);
                    classNames.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Class<?>> classes = new HashSet<>();
        classNames.forEach(className -> {
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return classes;
    }

    /**
     * 判断指定类是否运行在jar包中。
     *
     * @param clazz 要检查的类
     * @return 如果该类运行在jar包中，返回true；否则返回false。
     */
    public static boolean isClassRunningFromJar(Class<?> clazz) {
        // 获取类的保护域
        String className = clazz.getName().replace('.', '/') + CLASS_SUFFIX;
        URL resource = clazz.getClassLoader().getResource(className);
        if (resource == null) {
            return false;
        }
        String classPath = resource.toString();
        return classPath.startsWith("jar");
    }

    private static Class<?> getSingleClass(
        File classFile, String basePath, ClassLoader classLoader) {
        String classFileName = classFile.getName();
        if (!classFileName.endsWith(CLASS_SUFFIX)) {
            return null;
        }
        String className = basePath.replace("/", ".") +
            "." +
            classFileName.replace(CLASS_SUFFIX, "");
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
        String className = basePath + classFile.getAbsolutePath()
            .replace(baseFile.getAbsolutePath(), "")
            .replace("\\", ".")
            .replace(CLASS_SUFFIX, "");
        if (className.startsWith(".")) {
            className = className.substring(1);
        }
        return className;

    }

}
