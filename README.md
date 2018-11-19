<h1>CodeAnalysisAgent</h1>
<br>
Java agent project that injects the byte codes into target java project to allow dynamic code analysis

***Run Guide***
For details, read **Makefile**

NOTE: src agent project jarfile = CodeAnalysisAgnet.jar
```
$ make clean
  : deletes all class files and CodeAnalysisAgnet.jar
$ make all
  : creates class files then build CodeAnalysisAgnet.jar
$ make run target={java project(.jar)} arg={(optional) if target requires any}
  : run ```$make all``` then run target java project file on top of
javaagent=CodeAnalysisAgnet.jar
```

Examples
```
make run target=samples/CPSC410.jar arg=cli
make run target=samples/DNSLookupService.jar arg=199.7.83.42
```
