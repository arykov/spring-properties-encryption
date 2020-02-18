@echo off
RMDIR /Q /S spring-properties-encryption || goto :error
git clone git@github.com:arykov/spring-properties-encryption.git || goto :error
set /p mvn_repo_user=maven central user:
set /p mvn_repo_pass=maven central pass:
set /p BUILD_NUMBER=revision:
cd spring-properties-encryption || goto :error
git tag -a "%BUILD_NUMBER%" -m "Jenkins released %BUILD_NUMBER%" || goto :error
call mvn deploy -DskipTests -Drevision=%BUILD_NUMBER%|| goto :error
git push origin "%BUILD_NUMBER%"
cd ..
echo "Success"
exit 0
:error
echo Failed with error #%errorlevel%.
exit /b %errorlevel%

