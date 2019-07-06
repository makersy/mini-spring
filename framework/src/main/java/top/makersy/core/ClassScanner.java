package top.makersy.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by makersy on 2019
 */

public class ClassScanner {

    /**
     * 扫描一个包下面的所有类
     * @param packageName 包名
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> scanClass(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        String path = packageName.replace(".", "/");  //将包名转化为路径名
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();  //获取类加载器
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().contains("jar")) {  //如果资源类型是jar包
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarFilePath, path));
            } else {
                //todo
            }
        }
        return classList;
    }

    /**
     * 获取一个jar包下所有类
     * @param jarFilePath
     * @param path
     * @return 类的list
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length() - 6);  //类的全限定名
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
