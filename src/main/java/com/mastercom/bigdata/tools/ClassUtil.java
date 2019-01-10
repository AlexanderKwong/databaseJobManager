package com.mastercom.bigdata.tools;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Kwong on 2017/9/23.
 */
public final class ClassUtil {

    /**
     * 根据参数
     * @param clazz
     * @param index
     * @return
     */
    public static Class getGeneralClass(Class clazz, int index){
        Type type = clazz.getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        return (Class) types[index];
    }

    public static List<Class> getChildrenClasses(Class cls, String packageName) throws IOException,
            ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        for (Class<?> c : getClasses(cls, packageName)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    public static List<Class> getClasses(Class cls, String packageName) throws IOException,
            ClassNotFoundException {
        if (cls != null){
            cls = ClassUtil.class;
        }
        String classPath = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (classPath.endsWith(".jar")) {
            return getClassFromJarFile(classPath, packageName.replace(".", "/"));
        } else return getClassFromPackage(packageName);
    }

    /**
     * 通过流将jar中的一个文件的内容输出
     *
     * @param jarPaht
     *            jar文件存放的位置
     * @param filePaht
     *            指定的文件目录
     */
    public static void getStream(String jarPaht, String filePaht) {
        try (JarFile jarFile = new JarFile(jarPaht)){
            Enumeration<JarEntry> ee = jarFile.entries();

            List<JarEntry> jarEntryList = new ArrayList<JarEntry>();
            while (ee.hasMoreElements()) {
                JarEntry entry = (JarEntry) ee.nextElement();
                // 过滤我们出满足我们需求的东西，这里的fileName是指向一个具体的文件的对象的完整包路径，比如com/mypackage/test.txt
                if (entry.getName().startsWith(filePaht)) {
                    jarEntryList.add(entry);
                }
            }
            try (InputStream in = jarFile.getInputStream(jarEntryList.get(0));
                BufferedReader br = new BufferedReader(new InputStreamReader(in))){
                String s = "";
                while ((s = br.readLine()) != null) {
                    System.out.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * 从jar文件中读取指定目录下面的所有的class文件
     *
     * @param jarPaht
     *            jar文件存放的位置
     * @param filePaht
     *            指定的文件目录
     * @return 所有的的class的对象
     */
    public static List<Class> getClassFromJarFile(String jarPaht, String filePaht) {
        List<Class> clazzs = new ArrayList<Class>();

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPaht);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        List<JarEntry> jarEntryList = new ArrayList<JarEntry>();

        Enumeration<JarEntry> ee = jarFile.entries();
        while (ee.hasMoreElements()) {
            JarEntry entry = (JarEntry) ee.nextElement();
            // 过滤我们出满足我们需求的东西
            if (entry.getName().startsWith(filePaht) && entry.getName().endsWith(".class")) {
                jarEntryList.add(entry);
            }
        }
        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);

            try {
                clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return clazzs;
    }

    /**
     * 获得包下面的所有的class
     *
     * @param pack
     *            package完整名称
     * @return List包含所有class的实例
     */
    public static List<Class> getClassFromPackage(String pack) {
        List<Class> clazzs = new ArrayList<Class>();

        // 是否循环搜索子包
        boolean recursive = true;

        // 包名字
        String packageName = pack;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
//                    System.out.println("file类型的扫描");
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
                } else if ("jar".equals(protocol)) {
//                    System.out.println("jar类型的扫描");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzs;
    }

    /**
     * 在package对应的路径下找到所有的class
     *
     * @param packageName
     *            package名称
     * @param filePath
     *            package对应的路径
     * @param recursive
     *            是否查找子package
     * @param clazzs
     *            找到class以后存放的集合
     */
    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
                return acceptDir || acceptClass;
            }
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
