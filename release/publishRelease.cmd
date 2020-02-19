@echo off
mkdir spring-properties-encryption
RMDIR /Q /S spring-properties-encryption || goto :error
git clone git@github.com:arykov/spring-properties-encryption.git || goto :error
set /p mvn_repo_user=maven central user:
set /p mvn_repo_pass=maven central pass:
set /p BUILD_NUMBER=revision:
cd spring-properties-encryption || goto :error
git tag -a "%BUILD_NUMBER%" -m "Jenkins released %BUILD_NUMBER%" || goto :error
call mvn deploy -Dpublish -DskipTests -Drevision=%BUILD_NUMBER%|| goto :error
git push origin "%BUILD_NUMBER%"
cd ..
echo "Success"
goto :success
:error
echo Failed with error #%errorlevel%.


:success
