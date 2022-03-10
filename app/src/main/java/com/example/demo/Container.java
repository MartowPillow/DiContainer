package com.example.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Container {

    private Set<Object> Instances = new HashSet<>();

    public Container(Collection<Class<?>> Classes) throws Exception {
        //create an instance of each class
        for(Class<?> Class : Classes){
            Constructor<?> constructor = Class.getConstructor();
            constructor.setAccessible(true);
            Object Instance = constructor.newInstance();
            this.Instances.add(Instance);
        }
        //link instances together
        for(Object Instance : this.Instances){
            for(Field field : Instance.getClass().getDeclaredFields()){
                if(field.isAnnotationPresent(Injectable.class)) {
                    Class<?> fieldType = field.getType();
                    field.setAccessible(true);
                    //find matching instances
                    for (Object otherInstance : this.Instances) {
                        if (fieldType.isInstance(otherInstance)) {
                            field.set(Instance, otherInstance);
                        }
                    }
                }
            }
        }
    }

    public static Container ContainerFromScan(String rootPackageName) throws Exception {
        Set<Class<?>> packageAllClasses = Scanner.getClassesInPackage(rootPackageName);
        Set<Class<?>> scannableClasses = new HashSet<>();
        for(Class<?> Class : packageAllClasses){
            if(Class.isAnnotationPresent(Scannable.class)){
                scannableClasses.add(Class);
            }
        }
        return new Container(scannableClasses);
    }
    //empty container, to use with 'register'
    public Container() {
    }

    //add a class after container creation
    public void register(Class<?> Class) throws Exception {
        //create an instance of that class
        Constructor<?> constructor = Class.getConstructor();
        constructor.setAccessible(true);
        this.Instances.add(constructor.newInstance());

        //re-link instances together
        for(Object Instance : this.Instances){
            for(Field field : Instance.getClass().getDeclaredFields()){
                if(field.isAnnotationPresent(Injectable.class)) {
                    Class<?> fieldType = field.getType();
                    field.setAccessible(true);
                    //find a matching instance
                    for (Object otherInstance : this.Instances) {
                        if (fieldType.isInstance(otherInstance)) {
                            field.set(Instance, otherInstance);
                        }
                    }
                }
            }
        }
    }

    //in class 'in', binds field 'fieldName' to class 'to'.
    public void bind(String fieldName, Class<?> in, Class<?> to) throws IllegalAccessException {
        //find 'in'
        for(Object Instance : this.Instances){
            if(in.isInstance(Instance)) {
                //find 'fieldName'
                for (Field field : Instance.getClass().getDeclaredFields()) {
                    if (field.getName().equals(fieldName) && field.isAnnotationPresent(Injectable.class)) {
                        field.setAccessible(true);
                        //find 'to'
                        for (Object otherInstance : this.Instances) {
                            if (to.isInstance(otherInstance)) {
                                //bind
                                field.set(Instance, otherInstance);
                            }
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

    public Set<Object> getInstances(){
        return this.Instances;
    }
}
