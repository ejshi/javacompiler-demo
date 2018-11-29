package com.sjz.compile;

import java.io.File;
import java.io.IOException;

public class JavaCompileTest {
    public static void main(String[] args) throws IOException {

//        File file = new File("");
//        new JavaCompileUtil().javaComileByFile(file);

        File infile = new File("D:\\demospace\\javacompile\\src\\main\\java\\com\\sjz\\demo\\HelloCompile.java");


        JavaCompileUtil javaCompileUtil = new JavaCompileUtil();
//        javaCompileUtil.javaComileByFile(infile);

//        File outfile = new File("D:\\compile\\HelloCompile.class");
//        javaCompileUtil.javaCompileByFileAndReturnClassPath(infile,outfile);


//        javaCompileUtil.javaCompileByFile("D:\\demospace\\javacompile\\src\\main\\java\\com\\sjz\\demo\\HelloCompile.java");

        StringBuilder javaFileContents = new StringBuilder("class TestClass{ " + "   public void testMethod(){" + "       " +
                "System.out.println("+ "\"test\"" + ");" + "}" + "}");

        javaCompileUtil.javaCompileByStringContent(javaFileContents.toString());
    }
}
