一、编译源码需要啥？
那么问题来了，如果要了解运行时编译的过程和对应的接口，首先要明白的就是编译这个过程都会涉及哪些工具和要解决的问题？从我们熟悉的构建过程开始：

编译工具（编译器）：显然没有这个东西我们啥也干不了；
要编译的源代码文件：没有这个东西，到底编啥呢？
源代码、字节码文件的管理：其实这里靠的是文件系统的支持，包括文件的创建和管理；
编译过程中的选项：要编译的代码版本、目标，源代码位置，classpath和编码等等，见相关文章；
编译中编译器输出的诊断信息：告诉你编译成功还是失败，会有什么隐患提出警告信息；
按照这些信息，JDK也提供了可编程的接口对象上述信息，这些API全部放在javax.tools包下，对应上面的信息如下：

编译器：涉及到的接口和类如下：

JavaCompiler
JavaCompiler.CompilationTask
ToolProvider
在上面的接口和类中，ToolProvider类似是一个工具箱，它可以提供JavaCompiler类的实例并返回，然后该实例可以获取JavaCompiler.CompilationTask实例，然后由JavaCompiler.CompilationTask实例来执行对应的编译任务，其实这个执行过程是一个并发的过程。


参考文章：

  https://docs.oracle.com/javase/8/docs/api/javax/tools/JavaCompiler.html
  https://blog.csdn.net/lmy86263/article/details/59742557

