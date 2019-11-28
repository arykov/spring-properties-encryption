mkdir lib
call mvn com.googlecode.maven-download-plugin:download-maven-plugin:1.4.2:artifact -DgroupId=org.aspectj -DartifactId=aspectjtools -Dversion=1.9.4 -DoutputDirectory=lib
call mvn com.googlecode.maven-download-plugin:download-maven-plugin:1.4.2:artifact -DgroupId=org.aspectj -DartifactId=aspectjrt -Dversion=1.9.4 -DoutputDirectory=lib
call mvn com.googlecode.maven-download-plugin:download-maven-plugin:1.4.2:artifact -DgroupId=org.aspectj -DartifactId=aspectjweaver -Dversion=1.9.4 -DoutputDirectory=lib