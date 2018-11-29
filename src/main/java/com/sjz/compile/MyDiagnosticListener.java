package com.sjz.compile;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import java.util.Locale;

/**
 * DiagnosticListener用于获取Diagnostic信息，Diagnostic信息包括：错误，警告和说明性信息
 */
public class MyDiagnosticListener implements DiagnosticListener {

    @Override
    public void report(Diagnostic diagnostic) {
        System.out.println("Code->" + diagnostic.getCode());
        System.out.println("Column Number->" + diagnostic.getColumnNumber());
        System.out.println("End Position->" + diagnostic.getEndPosition());
        System.out.println("Kind->" + diagnostic.getKind());
        System.out.println("Line Number->" + diagnostic.getLineNumber());
        System.out.println("Message->" + diagnostic.getMessage(Locale.ENGLISH));
        System.out.println("Position->" + diagnostic.getPosition());
        System.out.println("Source" + diagnostic.getSource());
        System.out.println("Start Position->" + diagnostic.getStartPosition());
        System.out.println("\n");
    }
}
