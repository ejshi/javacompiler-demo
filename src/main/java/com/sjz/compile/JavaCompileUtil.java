package com.sjz.compile;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * https://docs.oracle.com/javase/8/docs/api/javax/tools/JavaCompiler.html
 */
public class JavaCompileUtil {

    /**
     * 编译文件
     * @param file java源文件
     */
    public void javaComileByFile(File file){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, Charset.forName("utf-8"));

        //源文件路径
        Iterable<? extends JavaFileObject> javaFileObjectsFromFiles =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file));

        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, null, null, null, javaFileObjectsFromFiles);
        Boolean call = compilerTask.call();
        System.out.println("============ javaCompile execute =============" + call);

        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void javaCompileByFileAndReturnClassPath(File infile, File outFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, Charset.forName("utf-8"));

        Iterable<? extends JavaFileObject> javaFileObjectsFromFiles = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(infile));

        FileWriter fileWriter = new FileWriter(outFile);
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(fileWriter, fileManager, null, null, null, javaFileObjectsFromFiles);

        Boolean call = compilerTask.call();

        System.out.println("============ javaCompile execute =============" + call);

        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 参考这个：http://blog.onlycatch.com/post/java-Compiler-API
     * @param fileToCompile
     * @throws IOException
     */
    public void javaCompileByFile(String fileToCompile) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        MyDiagnosticListener myDiagnosticListener = new MyDiagnosticListener();

        //Locale.CHINA可为空，使用默认
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(myDiagnosticListener, Locale.CHINA, Charset.forName("utf-8"));

        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(fileToCompile);

        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, myDiagnosticListener, null, null, javaFileObjects);

        Boolean call = compilerTask.call();

        System.out.println("============ javaCompile execute =============" + call);

        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void javaCompileByStringContent(String content){
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

        //编译报错信息监听
//        MyDiagnosticListener myDiagnosticListener = new MyDiagnosticListener();

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();

        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(diagnosticCollector, null, Charset.forName("utf-8"));

        JavaObjectFromString javaObjectFromString = new JavaObjectFromString("TestClass", content);

        Iterable<? extends JavaFileObject> javaFileObjects = Arrays.asList(javaObjectFromString);

        JavaCompiler.CompilationTask compilerTask = javaCompiler.getTask(null, fileManager, diagnosticCollector, null, null, javaFileObjects);

        Boolean call = compilerTask.call();

        System.out.println("============ javaCompile execute =============" + call);

        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();

        //展示编译错误信息
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            System.out.println(String.format("Error Message : %s on line %d in %s%n",
                    diagnostic.getMessage(Locale.ENGLISH),
                    diagnostic.getLineNumber(),
                    diagnostic.getSource().toUri()));
        }

        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
