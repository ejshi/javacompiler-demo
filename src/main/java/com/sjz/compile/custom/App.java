package com.sjz.compile.custom;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态编译生成java代码，以及生成class对象
 * 参考网址：https://blog.csdn.net/lmy86263/article/details/59742557
 */
public class App {
    //内存存放动态编译生成的class字节信息
    private static Map<String, MyJavaFileObject> fileObjects = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        String code = "public class Man {\n" +
                "\tpublic void hello(){\n" +
                "\t\tSystem.out.println(\"hello world\");\n" +
                "\t}\n" +
                "}";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        MyJavaFileManager myJavaFileManager =
                new MyJavaFileManager(compiler.getStandardFileManager(null, null, Charset.defaultCharset()));

        List<String> options = new ArrayList<>();
        options.add("-target");
        options.add("1.8");

        Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");
        Matcher matcher = CLASS_PATTERN.matcher(code);
        String cls;
        if (matcher.find()) {
            cls = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in " + code);
        }

        System.out.println("className===================: "+cls);

        MyJavaFileObject myJavaFileObject = new MyJavaFileObject(cls, code);
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, myJavaFileManager, null, options, null, Arrays.asList(myJavaFileObject));

        Boolean call = compilerTask.call();
        System.out.println("======================动态编译结果============：" + call);

//        int length = myJavaFileManager.getMyJavaFileObject().getCompileBytes().length;
//
//        System.out.println("编译后的class字节长度=============================="+length);

        MyClassLoader myClassLoader = new MyClassLoader();

        Class<?> aClass = null;
        try {
            aClass = myClassLoader.loadClass(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Method method = null;
        try {
            method = aClass.getMethod("hello");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object instance = null;
        try {
            instance = aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            MyJavaFileObject javaFileObject = fileObjects.get(name);
            if (javaFileObject != null) {
                byte[] bytes = javaFileObject.getCompileBytes();
                Class<?> aClass = defineClass(name, bytes, 0, bytes.length);
                return aClass;
            }

            return ClassLoader.getSystemClassLoader().loadClass(name);
        }
    }

    static class MyJavaFileObject extends SimpleJavaFileObject {

        private String code;

        private ByteArrayOutputStream outputStream;

        MyJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
                    JavaFileObject.Kind.SOURCE);
            this.code = code;
        }

        //该构造器用来输出字节码
        public MyJavaFileObject(String name, JavaFileObject.Kind kind) {
            super(URI.create("String:///" + name + kind.extension), kind);
            this.code = null;
        }


        @Override
        public OutputStream openOutputStream() throws IOException {
            this.outputStream = new ByteArrayOutputStream();
            return this.outputStream;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            System.out.println("code代码======================: " + code);
            return this.code;
        }

        public byte[] getCompileBytes() {
            byte[] bytes = this.outputStream.toByteArray();
//            System.out.println("class文件字节码长度=============：" + bytes.length);
            return bytes;
        }
    }

    static class MyJavaFileManager extends ForwardingJavaFileManager {

        /*private MyJavaFileObject myJavaFileObject; //类生成完成后，数据才会有值


        public MyJavaFileObject getMyJavaFileObject() {
            return myJavaFileObject;
        }

        public void setMyJavaFileObject(MyJavaFileObject myJavaFileObject) {
            this.myJavaFileObject = myJavaFileObject;
        }*/

        protected MyJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
            JavaFileObject javaFileObject = fileObjects.get(className);
            if (javaFileObject == null) {
                return super.getJavaFileForInput(location, className, kind);
            }
            return javaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
          MyJavaFileObject myJavaFileObject = new MyJavaFileObject(className, kind);
//            myJavaFileObject = new MyJavaFileObject(className, kind);

            fileObjects.put(className, myJavaFileObject);

            return myJavaFileObject;
        }

        @Override
        public void close() throws IOException {
            //TODO JavaFileManager关闭的时候调用一下数据的清洗，防止内存溢出
        }
    }
}
