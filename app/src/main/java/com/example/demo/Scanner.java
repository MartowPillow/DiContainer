package com.example.demo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scanner {

    public static Set<Class<?>> getClassesInPackage(String packageName) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
        List<File> folders = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            folders.add(new File(resource.getFile()));
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File folder : folders) {
            classes.addAll(getClassesInFolder(folder, packageName));
        }
        return classes;
    }

    private static List<Class<?>> getClassesInFolder(File folder, String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) { //recursion in subfolder
                classes.addAll(getClassesInFolder(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))); //6 = len(".class")
            }
        }
        return classes;
    }
}
