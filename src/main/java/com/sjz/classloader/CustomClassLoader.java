package com.sjz.classloader;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

public class CustomClassLoader {

    private static ClassPool classPool = ClassPool.getDefault();

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\demospace\\javacompile\\src\\main\\java\\com\\sjz\\demo\\HelloCompile.class");

        Class<?> aClass = makeClass(file);
        Method method = aClass.getMethod("hello");

        Object invokeResult = method.invoke(aClass.newInstance());
        System.out.println(invokeResult);
    }

    public static Class<?> makeClass(File file) throws Exception {

        CtClass ctClass = classPool.makeClass(new FileInputStream(file));

        //ClassLoader管理动态生产的class
        Class<?> aClass =
                ctClass.toClass(new SimpleClassLoader(), CustomClassLoader.class.getProtectionDomain());

        return aClass;
    }

    static class SimpleClassLoader extends ClassLoader {

    }

}
