**sample-app** illustrates use of spring-properties-encryption. 

To run it you need to perform the following steps:
*   build all the modules
*   download aspectj dependencies. In Windows run download.cmd

Prior to running the application as described bellow have a look at application.properties and comp.properties. They contain not encrypted passwords. Once you execute this sample this will change.

There are two modes you can run in:
1) Using load time weaving, you can simply execute run.cmd. 
2) Using ajc precompilation you will need to execute applyaspects.cmd. To run precompiled version you should execute runprecompiled.cmd