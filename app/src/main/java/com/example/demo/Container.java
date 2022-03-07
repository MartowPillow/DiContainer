package com.example.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Container {

    private Set<Object> Instances = new HashSet<>();

    public Container(Collection<Class<?>> Classes) throws Exception {
        // create an instance of each class
        for(Class<?> Class : Classes){
            Constructor<?> constructor = Class.getConstructor();
            constructor.setAccessible(true);
            Object Instance = constructor.newInstance();
            this.Instances.add(Instance);
        }
        // wire them together
        for(Object Instance : this.Instances){
            for(Field field : Instance.getClass().getDeclaredFields()){
                if(field.isAnnotationPresent(Injectable.class)) {
                    Class<?> fieldType = field.getType();
                    field.setAccessible(true);
                    // find a suitable matching instance
                    for (Object matchPartner : this.Instances) {
                        if (fieldType.isInstance(matchPartner)) {
                            field.set(Instance, matchPartner);
                        }
                    }
                }
            }
        }
    }

    public static Container ContainerFromScan(String rootPackageName) throws Exception {
        Set<Class<?>> allClassesInPackage = Scanner.getAllClassesInPackage(rootPackageName);
        Set<Class<?>> Classes = new HashSet<>();
        for(Class<?> Class : allClassesInPackage){
            if(Class.isAnnotationPresent(Scannable.class)){
                Classes.add(Class);
            }
        }
        return new Container(Classes);
    }

    //add a class after container creation
    public void register(Class<?> Class) throws Exception {
        // create an instance of that class
        Constructor<?> constructor = Class.getConstructor();
        constructor.setAccessible(true);
        this.Instances.add(constructor.newInstance());

        // re-wire instances together
        for(Object Instance : this.Instances){
            for(Field field : Instance.getClass().getDeclaredFields()){
                if(field.isAnnotationPresent(Injectable.class)) {
                    Class<?> fieldType = field.getType();
                    field.setAccessible(true);
                    // find a suitable matching instance
                    for (Object matchPartner : this.Instances) {
                        if (fieldType.isInstance(matchPartner)) {
                            field.set(Instance, matchPartner);
                        }
                    }
                }
            }
        }
    }

    public <T> T newInstance(Class<T> Class){
        for(Object Instance : this.Instances){
            if(Class.isInstance(Instance)){
                return (T)Instance;
            }
        }
        return null;
    }
}
