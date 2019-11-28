REM appears that I am missing instrumentation of some of the jars

set REVISION=0.0.1-SNAPSHOT
set ASPECTJ_REVISION=1.9.4
set ROOT_DIR=%~dp0
set ORIGINAL_JAR=sample-app-%REVISION%.jar
set AJCED_JAR_NAME=sample-app-ajc-%REVISION%.jar
set ASPECT_JAR_NAME=spring-properties-encryption-%REVISION%.jar
set ASPECT_JAR=%ROOT_DIR%\..\spring-properties-encryption\target\%ASPECT_JAR_NAME%
mkdir target\ajc
copy target\%ORIGINAL_JAR% target\ajc
pushd target\ajc
jar xvf %ORIGINAL_JAR%
rem mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get org.aspectj:aspectjtools:1.9.4:jar -Dtransitive=false -Ddest=aspectjtools.jar

java -classpath %ROOT_DIR%\lib\aspectjtools-%ASPECTJ_REVISION%.jar org.aspectj.tools.ajc.Main -injars BOOT-INF\lib\spring-core-5.1.4.RELEASE.jar -aspectpath %ASPECT_JAR% -outjar BOOT-INF\lib\spring-core-5.1.4.RELEASE-withencr.jar

java -classpath %ROOT_DIR%\lib\aspectjtools-%ASPECTJ_REVISION%.jar org.aspectj.tools.ajc.Main -injars %ASPECT_JAR% -aspectpath %ASPECT_JAR% -outjar BOOT-INF\lib\%ASPECT_JAR_NAME%

del BOOT-INF\lib\spring-core-5.1.4.RELEASE.jar
del %ORIGINAL_JAR%

copy %ROOT_DIR%\lib\aspectjrt-%ASPECTJ_REVISION%.jar BOOT-INF\lib\
rem repack with the same manifest and no compression
jar -cMfv0 %AJCED_JAR_NAME% . *.*
popd
move %ROOT_DIR%\target\ajc\%AJCED_JAR_NAME% %ROOT_DIR%\target\

